package negocio.facade.jdbc.academico;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.FileItem;
import org.richfaces.event.FileUploadEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.academico.DocumentacaoCursoVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoIndeferimentoDocumentoAlunoVO;
import negocio.comuns.academico.TipoDocumentoEquivalenteVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ConverterImgToPdf;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UnificadorPDF;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DocumetacaoMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DocumetacaoMatriculaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>DocumetacaoMatriculaVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see DocumetacaoMatriculaVO
 * @see ControleAcesso
 * @see Matricula
 */
@Repository
public class DocumetacaoMatricula extends ControleAcesso implements DocumetacaoMatriculaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public DocumetacaoMatricula() throws Exception {
		super();
		setIdEntidade("Matricula");
	}

	public DocumetacaoMatriculaVO novo() throws Exception {
		DocumetacaoMatricula.incluir(getIdEntidade());
		DocumetacaoMatriculaVO obj = new DocumetacaoMatriculaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>DocumetacaoMatriculaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>DocumetacaoMatriculaVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DocumetacaoMatriculaVO obj, PessoaVO aluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			DocumetacaoMatriculaVO.validarDados(obj);
			final String sql = "INSERT INTO DocumetacaoMatricula( tipoDeDocumento, situacao, matricula, entregue, dataEntrega, usuario, arquivo, arquivoVerso, gerarSuspensaoMatricula, entreguePorEquivalencia, arquivoAprovadoPeloDep, localUpload, ArquivoGED , ano , semestre ,arquivoAssinado ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ? , ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if (obj.getUsuario().getCodigo() == 0) {
				obj.getUsuario().setCodigo(usuario.getCodigo());
			}
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getTipoDeDocumentoVO().getCodigo().intValue());
					sqlInserir.setString(2, obj.getSituacao());
					sqlInserir.setString(3, obj.getMatricula());
					sqlInserir.setBoolean(4, obj.isEntregue().booleanValue());
					
					if (obj.isEntregue().booleanValue() || obj.getIsPossuiArquivo()) {
						sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataEntrega()));
						sqlInserir.setInt(6, obj.getUsuario().getCodigo());
					}else {
						sqlInserir.setNull(5, 0);
						sqlInserir.setNull(6, 0);
					}
					if (obj.isEntregue().booleanValue()) {
						if (!obj.getArquivoVO().getCodigo().equals(0)) {
							sqlInserir.setInt(7, obj.getArquivoVO().getCodigo());
						} else {
							sqlInserir.setNull(7, 0);
						}
						if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
							sqlInserir.setInt(8, obj.getArquivoVOVerso().getCodigo());
						} else {
							sqlInserir.setNull(8, 0);
						}
					} else {
						sqlInserir.setNull(7, 0);
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setBoolean(9, obj.getGerarSuspensaoMatricula());
					sqlInserir.setBoolean(10, obj.getEntreguePorEquivalencia());
					if (obj.getArquivoAprovadoPeloDep() == null) {
						sqlInserir.setNull(11, 0);
					} else {
						sqlInserir.setBoolean(11, obj.getArquivoAprovadoPeloDep());
					}
					sqlInserir.setString(12, obj.getLocalUpload());
					if(Uteis.isAtributoPreenchido(obj.getArquivoGED())) {
						sqlInserir.setInt(13, obj.getArquivoGED().getCodigo());
					}else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setString(14, obj.getAno());
					sqlInserir.setString(15, obj.getSemestre());
					if (obj.isEntregue().booleanValue() && Uteis.isAtributoPreenchido(obj.getArquivoVOAssinado())) {
						sqlInserir.setInt(16, obj.getArquivoVOAssinado().getCodigo());
					} else {
						sqlInserir.setNull(16, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			if (!obj.getArquivoVO().getNome().equals("") && !obj.getExcluirArquivo()) {
				obj.getArquivoVO().setCodOrigem(obj.getCodigo());
				if (!obj.getArquivoVO().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
					alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
				}
			}
			if (!obj.getArquivoVOVerso().getNome().equals("") && !obj.getExcluirArquivo()) {
				obj.getArquivoVOVerso().setCodOrigem(obj.getCodigo());
				if(!obj.getArquivoVOVerso().getDescricao().contains("_VERSO")) {
					obj.getArquivoVOVerso().setDescricao(obj.getArquivoVOVerso().getDescricao() + "_VERSO");
					obj.getArquivoVOVerso().setDescricaoAntesAlteracao(obj.getArquivoVOVerso().getDescricao());
				}
				if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
					alterarCodigoArquivoVerso(obj, obj.getArquivoVOVerso().getCodigo(), usuario);
				}
			}
			if (!obj.getArquivoGED().getNome().equals("") && !obj.getExcluirArquivo()) {
				if (!obj.getArquivoGED().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoGED(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoGED(), false, usuario, configuracaoGeralSistema);
					alterarCodigoArquivo(obj, obj.getArquivoGED().getCodigo(), usuario);
				}
			}
			obj.setNovoObj(Boolean.FALSE);
			if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO) && (obj.getDocumentoEntregue() || (obj.getIsPossuiArquivo() || obj.getIsPossuiArquivoVerso() || obj.getIsPossuiArquivoAssinado()))) {
				alteraDocumentoAluno(obj.getSituacao(), obj.getDataEntrega(), obj.getArquivoVO().getCodigo(), obj.getArquivoVOVerso().getCodigo(), obj.getArquivoVOAssinado().getCodigo(), aluno.getCodigo(), obj.getTipoDeDocumentoVO().getCodigo(), obj.getUsuario().getCodigo(), obj.getMatricula(), obj.getEntregue(), obj.getIsPossuiArquivo());
			}
			
			if(usuario.getVisaoLogar().equals("funcionario")) {
				if(obj.getEntregue()) {
					getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(obj, usuario);
				}
			}
			
//			registrarLog(obj, "Incluir", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public void registrarLog(DocumetacaoMatriculaVO documentacaoMatri, String operacao, UsuarioVO usuario) throws Exception {
//		StringBuilder dados = new StringBuilder();
//		dados.append("Operação -> ").append(operacao).append(" - ");
//		dados.append(" Matricula -> ").append(documentacaoMatri.getMatricula()).append(" - ");
//		dados.append(" Código Tipo Documento -> ").append(documentacaoMatri.getTipoDeDocumentoVO().getCodigo()).append(" - ");
//		dados.append(" Tipo Documento -> ").append(documentacaoMatri.getTipoDeDocumentoVO().getNome()).append(" - ");
//		dados.append(" Entregue -> ").append(documentacaoMatri.getEntregue()).append(" - ");
//		dados.append(" Entregue Por Equivalencia-> ").append(documentacaoMatri.getEntreguePorEquivalencia()).append(" - ");
//		dados.append(" Situação-> ").append(documentacaoMatri.getSituacao()).append(" - ");
//		dados.append(" Data Entrega-> ").append(Uteis.getData(documentacaoMatri.getDataEntrega())).append(" - ");
//		dados.append(" Usuario-> ").append(documentacaoMatri.getUsuario().getCodigo()).append(" - ");
//		dados.append(" Nome Usuario-> ").append(documentacaoMatri.getUsuario().getNome()).append(" - ");
//		dados.append(" Codigo Arquivo-> ").append(documentacaoMatri.getArquivoVO().getCodigo()).append(" - ");
//		dados.append(" Arquivo-> ").append(documentacaoMatri.getArquivoVO().getNome()).append(" - ");
//		dados.append(" Codigo Arquivo Verso-> ").append(documentacaoMatri.getArquivoVOVerso().getCodigo()).append(" - ");
//		dados.append(" Arquivo Verso-> ").append(documentacaoMatri.getArquivoVOVerso().getNome()).append(" - ");
//		dados.append(" Codigo UsuarioRespAprovacao-> ").append(documentacaoMatri.getRespAprovacaoDocDep().getCodigo()).append(" - ");
//		dados.append(" UsuarioRespAprovacao-> ").append(documentacaoMatri.getRespAprovacaoDocDep().getNome()).append(" - ");
//		dados.append(" Codigo RespNegarDocDep-> ").append(documentacaoMatri.getRespNegarDocDep().getCodigo()).append(" - ");
//		dados.append(" RespNegarDocDep-> ").append(documentacaoMatri.getRespNegarDocDep().getNome()).append(" - ");
//		dados.append(" Data Aprovação-> ").append(Uteis.getData(documentacaoMatri.getDataAprovacaoDocDep())).append(" - ");
//		dados.append(" Data Negar Documento-> ").append(Uteis.getData(documentacaoMatri.getDataNegarDocDep())).append(" - ");
//		dados.append(" Justificativa-> ").append(documentacaoMatri.getJustificativaNegacao()).append(" - ");
//		incluirLog(dados.toString(), new Date(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLog(final String dados, final Date data, final UsuarioVO usuario) throws Exception {
		final String sql = "INSERT INTO documetacaomatriculalog ( usuario, dataLog, dados) VALUES ( ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, usuario.getCodigo());
				sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(data));
				sqlInserir.setString(3, dados);
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		});
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>DocumetacaoMatriculaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica
	 * a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code>
	 * da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>DocumetacaoMatriculaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DocumetacaoMatriculaVO obj, PessoaVO aluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		DocumetacaoMatriculaVO.validarDados(obj);
		// if (!configuracaoGeralSistema.getControlaAprovacaoDocEntregue()) {
		// obj.setArquivoAprovadoPeloDep(Boolean.TRUE);
		// } else {
		// obj.setArquivoAprovadoPeloDep(Boolean.FALSE);
		// }
		if (obj.getUsuario().getCodigo() == 0) {
			obj.getUsuario().setCodigo(usuario.getCodigo());
		}

		final String sql = "UPDATE DocumetacaoMatricula set tipoDeDocumento=?, situacao=?, matricula=?, entregue=?, dataentrega=?, usuario=?, arquivo=?, arquivoVerso=?, gerarSuspensaoMatricula=?, entreguePorEquivalencia=?, arquivoAprovadoPeloDep=?, respnegardocdep=?, dataNegarDocDep=?, justificativaNegacao=?, localUpload=?, arquivoGed=?, ano = ?, semestre = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getTipoDeDocumentoVO().getCodigo().intValue());
				sqlAlterar.setString(2, obj.getSituacao());
				sqlAlterar.setString(3, obj.getMatricula());
				sqlAlterar.setBoolean(4, obj.isEntregue().booleanValue());
				if (obj.isEntregue().booleanValue() || obj.getIsPossuiArquivo()) {
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataEntrega()));
					sqlAlterar.setInt(6, obj.getUsuario().getCodigo());
				}else {
					sqlAlterar.setNull(5, 0);
					sqlAlterar.setNull(6, 0);
				}
				if (obj.isEntregue().booleanValue()) {					
					if (!obj.getArquivoVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(7, obj.getArquivoVO().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
						sqlAlterar.setInt(8, obj.getArquivoVOVerso().getCodigo());
					} else {
						sqlAlterar.setNull(8, 0);
					}
				} else {								
					sqlAlterar.setNull(7, 0);
					sqlAlterar.setNull(8, 0);
				}
				sqlAlterar.setBoolean(9, obj.getGerarSuspensaoMatricula());
				sqlAlterar.setBoolean(10, obj.getEntreguePorEquivalencia());
				if (obj.getArquivoAprovadoPeloDep() == null) {
					sqlAlterar.setNull(11, 0);
				} else {
					sqlAlterar.setBoolean(11, obj.getArquivoAprovadoPeloDep());
				}
				if (obj.isEntregue().booleanValue()) {
					if (!obj.getArquivoVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(12, 0);
						sqlAlterar.setNull(13, 0);
						sqlAlterar.setString(14, "");
					} else if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
						sqlAlterar.setNull(12, 0);
						sqlAlterar.setNull(13, 0);
						sqlAlterar.setString(14, "");
					} else {
						if (obj.getRespNegarDocDep().getCodigo() == 0) {
							sqlAlterar.setNull(12, 0);
						} else {
							sqlAlterar.setInt(12, obj.getRespNegarDocDep().getCodigo());
						}
						sqlAlterar.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataNegarDocDep()));
						sqlAlterar.setString(14, obj.getJustificativaNegacao());
					}
				} else {
					if (obj.getRespNegarDocDep().getCodigo() == 0) {
						sqlAlterar.setNull(12, 0);
					} else {
						sqlAlterar.setInt(12, obj.getRespNegarDocDep().getCodigo());
					}
					if(obj.getDataNegarDocDep() == null) {
						sqlAlterar.setNull(13, 0);
					}else {
						sqlAlterar.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataNegarDocDep()));
					}
					sqlAlterar.setString(14, obj.getJustificativaNegacao());
				}
				sqlAlterar.setString(15, obj.getLocalUpload());
				if(Uteis.isAtributoPreenchido(obj.getArquivoGED())) {
					sqlAlterar.setInt(16, obj.getArquivoGED().getCodigo());
				}else {
					sqlAlterar.setNull(16, 0);
				}
				sqlAlterar.setString(17, obj.getAno());
				sqlAlterar.setString(18, obj.getSemestre());
					
				sqlAlterar.setInt(19, obj.getCodigo().intValue());
				

				return sqlAlterar;
			}
		});
		
		if (!obj.getArquivoVO().getNome().equals("")) {
			obj.getArquivoVO().setCodOrigem(obj.getCodigo());
			if (!obj.getArquivoVO().getCodigo().equals(0)) {
				if(obj.getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
				}
			} else {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
			}
			alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
		}
		if (!obj.getArquivoVOVerso().getNome().equals("")) {
			obj.getArquivoVOVerso().setCodOrigem(obj.getCodigo());
			if(!obj.getArquivoVOVerso().getDescricao().contains("_VERSO")) {
				obj.getArquivoVOVerso().setDescricao(obj.getArquivoVOVerso().getDescricao() + "_VERSO");
				obj.getArquivoVOVerso().setDescricaoAntesAlteracao(obj.getArquivoVOVerso().getDescricao());
			}
			if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
				if(obj.getArquivoVOVerso().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
				}
			} else {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
			}			
			alterarCodigoArquivoVerso(obj, obj.getArquivoVOVerso().getCodigo(), usuario);
		}

		if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO) && (obj.getDocumentoEntregue() || (obj.getIsPossuiArquivo() || obj.getIsPossuiArquivoVerso() || obj.getIsPossuiArquivoAssinado()))) {
			alteraDocumentoAluno(obj.getSituacao(), obj.getDataEntrega(), obj.getArquivoVO().getCodigo(), obj.getArquivoVOVerso().getCodigo(), obj.getArquivoVOAssinado().getCodigo(), aluno.getCodigo(), obj.getTipoDeDocumentoVO().getCodigo(), obj.getUsuario().getCodigo(), obj.getMatricula(), obj.getEntregue(), obj.getIsPossuiArquivo());
		}

//		if(usuario.getVisaoLogar().equals("funcionario")) {
//			if(obj.getEntregue()) {
//				alterarDocumentoMatriculaAprovaPeloDep(obj, usuario);						
//			}else {
//				cancelarDocumentoMatriculaAprovaPeloDep(obj, usuario);
//			}
//		}
		
//		if(usuario != null) {
//			registrarLog(obj, "Alterar", usuario);
//		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>DocumetacaoMatriculaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>DocumetacaoMatriculaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoArquivo(DocumetacaoMatriculaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set arquivo=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { Uteis.isAtributoPreenchido(codArquivo) ? codArquivo : null, obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoArquivoVerso(DocumetacaoMatriculaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set arquivoVerso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { Uteis.isAtributoPreenchido(codArquivo) ? codArquivo : null, obj.getCodigo() });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void removerVinculoArquivoDocumentacaoMatricula(String ids, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE DocumetacaoMatricula set arquivo = null, arquivoVerso = null, arquivoAssinado = null, situacao='PE', arquivoAprovadoPeloDep = false, dataNegarDocDep = null, entregue = false, dataentrega = null, usuario = null ");
		sql.append("WHERE codigo in ( ").append(ids).append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	public static synchronized void alterarDocumentoMatriculaAprovaPeloDepSincronizado(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(obj, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaAprovaPeloDep(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		UsuarioVO usuarioVO = Uteis.isAtributoPreenchido(obj.getRespAprovacaoDocDep()) ? obj.getRespAprovacaoDocDep() : usuario;
		String sql = "UPDATE DocumetacaoMatricula set entregue=true, arquivoAprovadoPeloDep=true, notificou = false, respAprovacaoDocDep=?, dataAprovacaoDocDep=?, justificativaNegacao='', respNegarDocDep=null, dataNegarDocDep=null, situacao='OK', motivoindeferimentodocumentoaluno=null WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		Boolean bloqueioPorSolicitacaoLiberacaoMatricula = getFacadeFactory().getMatriculaFacade().verificarBloqueioPorSolicitacaoLiberacaoMatricula(obj.getMatricula());
		if(Uteis.isAtributoPreenchido(obj.getFileAssinar().getName())) {
			assinarDocumentacaoMatricula(obj, usuarioVO);
			if (obj.getValidarPdfA() && Uteis.isAtributoPreenchido(obj.getCaminhoPreviewPdfA())) {
				return;
			}
		}
		getConexao().getJdbcTemplate().update(sql, new Object[] { usuarioVO.getCodigo(), new Date(), obj.getCodigo() });
		if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
			alterarDocumentoMatriculaAprovaPeloDepAluno(obj, usuarioVO);
		}
		if(!bloqueioPorSolicitacaoLiberacaoMatricula) {
			getFacadeFactory().getMatriculaFacade().realizarVerificacaoELiberacaoSuspensaoMatricula(obj.getMatricula());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaAprovaPeloDep(DocumetacaoMatriculaVO obj, MatriculaVO matricula, ConfiguracaoFinanceiroVO config, UsuarioVO usuario) throws Exception {
		validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(matricula, obj ,usuario);
		getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(obj, usuario);
		if (obj.getValidarPdfA() && Uteis.isAtributoPreenchido(obj.getCaminhoPreviewPdfA())) {
			return;
		}
		getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(matricula.getMatricula() , config , Boolean.TRUE, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void  assinarDocumentacaoMatricula(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {		 
		 ArquivoVO arquivo = null;
//		 ArquivoVO arquivoAssindo = null;
		 MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(obj.getMatricula(), 0, false, usuario);
		 ConfiguracaoGEDVO configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), false, usuario);
		 List<DocumetacaoMatriculaVO> listaDocumentacaoMatricula = consultarDocumentacaoMatriculaPorArquivo(obj,usuario);
		 if(!Uteis.isAtributoPreenchido(listaDocumentacaoMatricula)) {
			 throw new Exception("Não foi Encontrado Documento Vinculado a esta matricula.");
		 }
		 String ids = UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaDocumentacaoMatricula, "codigo");
		 if (obj.getMontarDadosArquivo()) {
			 if(obj.getArquivoVO().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
				 String pastaBase = obj.getArquivoVO().getPastaBaseArquivo().replace(PastaBaseArquivoEnum.DOCUMENTOS.getValue(), PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue());
				 if(pastaBase.contains("DM  ")) {
					 pastaBase = pastaBase.replace("DM  ", "DM");
				 }
				 arquivo = montarDadosArquivo(obj.getArquivoVO(), obj.getFileAssinar(), pastaBase, obj.getArquivoVO().getPastaBaseArquivoEnum(), matricula, usuario);
//			  	arquivoAssindo  = getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDocumentacaoAluno(matricula.getUnidadeEnsino(), arquivo ,configGEDVO, obj.getFileAssinar(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario), usuario);
				 realizarAssinaturaDocumentoAluno(obj, matricula, arquivo, configGEDVO, usuario);
				 if (obj.getValidarPdfA()) {
					 return;
				 }
			 }else if(obj.getArquivoVO().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.APACHE)) {
				 arquivo = montarDadosArquivo(obj.getArquivoVO(), obj.getFileAssinar(),  obj.getArquivoVO().getPastaBaseArquivo(),  obj.getArquivoVO().getPastaBaseArquivoEnum(), matricula, usuario);
//			 	arquivoAssindo  = getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDocumentacaoAluno(matricula.getUnidadeEnsino(), arquivo ,configGEDVO, obj.getFileAssinar(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario), usuario);
				 realizarAssinaturaDocumentoAluno(obj, matricula, arquivo, configGEDVO, usuario);
				 if (obj.getValidarPdfA()) {
					 return;
				 }
			 }
		 } else {
			 arquivo = obj.getArquivoAssinado();
			 arquivo.setArquivoConvertidoPDFA(Boolean.TRUE);
			 File fileDest = new File(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getLocalUploadArquivoFixo() + File.separator + arquivo.getPastaBaseArquivo() + File.separator + arquivo.getNome());
			 getFacadeFactory().getArquivoHelper().copiar(obj.getFileAssinar(), fileDest);
			 getFacadeFactory().getArquivoFacade().persistir(arquivo, false, usuario, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario));
		 }
		inserirDocumentoAssinado(ids, arquivo, false, usuario);
		removerVinculoArquivoFrenteVersoDocumentacaoMatricula(ids, usuario);
		removerVinculoArquivosDocumentoEntregue(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario), usuario);
		obj.setArquivoVOAssinado(arquivo);
		if(listaDocumentacaoMatricula.size() > 1) {
			List<DocumetacaoMatriculaVO> listaDocumentacaoComMatriculaDiferente = listaDocumentacaoMatricula.stream().filter(dm -> !dm.getCodigo().equals(obj.getCodigo())).collect(Collectors.toList());
			for(DocumetacaoMatriculaVO documetacaoMatricula : listaDocumentacaoComMatriculaDiferente) {				 
				if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && (!obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)
						|| documetacaoMatricula.getEntreguePorEquivalencia())) {
					String sql = "UPDATE DocumetacaoMatricula set entregue=true, arquivoAprovadoPeloDep=true, notificou = false, respAprovacaoDocDep=?, dataAprovacaoDocDep=?, justificativaNegacao='', respNegarDocDep=null, dataNegarDocDep=null, situacao='OK', dataentrega= ?, motivoindeferimentodocumentoaluno=null WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
					getConexao().getJdbcTemplate().update(sql, new Object[] { usuario.getCodigo(), new Date(), obj.getDataEntrega(), documetacaoMatricula.getCodigo()});
				}	
	
			}			 
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarStatusDocumentacaoMatricula(DocumetacaoMatriculaVO documetacaoMatriculaVO, String descricao, Boolean statusProcessamento,  final UsuarioVO usuarioVO)  {
		alterar(documetacaoMatriculaVO, "documetacaomatricula",
				new AtributoPersistencia().add("descricaoprocessamento", descricao)
				.add("processadocomerro", statusProcessamento)
				.add("dataprocessamento", new Date()),
				new AtributoPersistencia().add("codigo", documetacaoMatriculaVO.getCodigo()), usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAssinaturaDocumentoJOB(DocumetacaoMatriculaVO documetacaoMatriculaVO, final UsuarioVO usuarioVO) throws Exception  {
		assinarDocumentacaoMatricula(documetacaoMatriculaVO, usuarioVO);
		if (documetacaoMatriculaVO.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && documetacaoMatriculaVO.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
			alterarDocumentoMatriculaAprovaPeloDepAluno(documetacaoMatriculaVO, usuarioVO);
		}
		atualizarStatusDocumentacaoMatricula(documetacaoMatriculaVO, "Processado com sucesso", Boolean.FALSE, usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarOutroDocumentoMatriculaAluno(List<DocumetacaoMatriculaVO> listaDocumentacaoMatricula, UsuarioVO usuarioVO) throws Exception {
		for(DocumetacaoMatriculaVO obj : listaDocumentacaoMatricula) {
			getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(obj, usuarioVO);
			}
		}

	private List<DocumetacaoMatriculaVO> consultarDocumentacaoMatriculaPorArquivo(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		Integer codigoArquivo = 0;
		StringBuilder sql = new StringBuilder("SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso") ;
		sql.append(" WHERE ");
		if(Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo())) {
			codigoArquivo = obj.getArquivoVO().getCodigo();
			sql.append(" DocumetacaoMatricula.arquivo = ? ");
		}else if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getCodigo())) {
			codigoArquivo = obj.getArquivoVOVerso().getCodigo();
			sql.append(" DocumetacaoMatricula.arquivoverso = ? ");
		}else if(Uteis.isAtributoPreenchido(obj.getArquivoVOAssinado().getCodigo())){
			codigoArquivo = obj.getArquivoVOAssinado().getCodigo();
			sql.append(" DocumetacaoMatricula.arquivoassinado = ? ");
		}else {
			throw new Exception("Não Foi Encontrado nenhum Arquivo Para essa documentação Matrícula!");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoArquivo });
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void removerVinculoArquivosDocumentoEntregue(List<DocumetacaoMatriculaVO> listadocumetacaoMatriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception{
		for(DocumetacaoMatriculaVO documetacaoMatriculaVO : listadocumetacaoMatriculaVO) {
			if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO())) {
				alterarCodigoArquivo(documetacaoMatriculaVO, 0, usuarioVO);
				getFacadeFactory().getArquivoFacade().excluir(documetacaoMatriculaVO.getArquivoVO(), usuarioVO, configuracaoGeralSistemaVO);
			}
			if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOVerso())) {
				alterarCodigoArquivoVerso(documetacaoMatriculaVO, 0, usuarioVO);
				getFacadeFactory().getArquivoFacade().excluir(documetacaoMatriculaVO.getArquivoVOVerso(), usuarioVO, configuracaoGeralSistemaVO);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void removerVinculoArquivosDocumentoEntregue(DocumetacaoMatriculaVO documetacaoMatriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception{
			if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO())) {
//				alterarCodigoArquivo(documetacaoMatriculaVO, 0, usuarioVO);
				getFacadeFactory().getArquivoFacade().excluir(documetacaoMatriculaVO.getArquivoVO(), usuarioVO, configuracaoGeralSistemaVO);
				documetacaoMatriculaVO.setArquivoVO(null);
			}
			if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOVerso())) {
//				alterarCodigoArquivoVerso(documetacaoMatriculaVO, 0, usuarioVO);
				getFacadeFactory().getArquivoFacade().excluir(documetacaoMatriculaVO.getArquivoVOVerso(), usuarioVO, configuracaoGeralSistemaVO);
				documetacaoMatriculaVO.setArquivoVOVerso(null);
			}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDocumentacaoMatricula(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo()) || Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getCodigo()) || Uteis.isAtributoPreenchido(obj.getArquivoVOAssinado().getCodigo())) {
			List<DocumetacaoMatriculaVO> listaDocumentacaoMatricula = consultarDocumentacaoMatriculaPorArquivo(obj,usuario);	
			String ids = UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaDocumentacaoMatricula, "codigo");	
		
			if(!ids.trim().isEmpty()) {
					removerVinculoArquivoDocumentacaoMatricula(ids, usuario);
			}
		}
			if(Uteis.isAtributoPreenchido(obj.getArquivoVO())) {
				getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(obj.getArquivoVO(), verificarAcesso, "Upload", usuario, configuracaoGeralSistemaVO);
			}else if(obj.getArquivoVO().getPastaBaseArquivoEnum() != null && obj.getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP) && Uteis.isAtributoPreenchido(obj.getArquivoVO().getNome())) {
				getFacadeFactory().getArquivoHelper().delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+obj.getArquivoVO().getPastaBaseArquivo()+File.separator+obj.getArquivoVO().getNome()));
				obj.getArquivoVO().setPastaBaseArquivoEnum(null);
				obj.getArquivoVO().setPastaBaseArquivo(null);
				obj.getArquivoVO().setNome(null);
			}
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso())) {
				getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(obj.getArquivoVOVerso(), verificarAcesso, "Upload", usuario, configuracaoGeralSistemaVO);	
			}else if(obj.getArquivoVOVerso().getPastaBaseArquivoEnum() != null && obj.getArquivoVOVerso().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP) && Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getNome())) {
				getFacadeFactory().getArquivoHelper().delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+obj.getArquivoVOVerso().getPastaBaseArquivo()+File.separator+obj.getArquivoVOVerso().getNome()));
				obj.getArquivoVOVerso().setPastaBaseArquivoEnum(null);
				obj.getArquivoVOVerso().setPastaBaseArquivo(null);
				obj.getArquivoVOVerso().setNome(null);
			}
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOAssinado())) {
				getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(obj.getArquivoVOAssinado(), verificarAcesso, "Upload", usuario, configuracaoGeralSistemaVO);	
			}
		obj.setSituacao("PE");
		obj.setEntregue(false);
		obj.setDataEntrega(null);
		obj.setArquivoAprovadoPeloDep(false); 
		obj.setDataNegarDocDep(null);
		obj.setUsuario(null);
		obj.setArquivoVO(new ArquivoVO());
		obj.setArquivoVOVerso(new ArquivoVO());
		obj.setArquivoVOAssinado(new ArquivoVO());
		obj.setRespNegarDocDep(null);
		obj.setJustificativaNegacao("");
	}

	private void removerVinculoArquivoAmazon(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
			getFacadeFactory().getArquivoFacade().excluirRegistroArquivoAmazonPorDocumentacaoMatricula(obj.getArquivoVO(), false, usuario);
			obj.setArquivoVO(new ArquivoVO());
	}

	private ArquivoVO montarDadosArquivo(ArquivoVO obj,File file,String pastaBase, PastaBaseArquivoEnum baseArquivoEnum,  MatriculaVO matriculaVO ,  UsuarioVO usuario) {
			ArquivoVO arquivo = new ArquivoVO();
			arquivo.setNome(file.getName());
			 try {
				if(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuario).getIntegracaoServidorOnline()) {
					 arquivo.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuario).getServidorArquivoOnline()));	 
				 }else {
					 arquivo.setServidorArquivoOnline(obj.getServidorArquivoOnline());
				 }
			} catch (Exception e) {
				arquivo.setServidorArquivoOnline(obj.getServidorArquivoOnline());
			}
			arquivo.setDescricao(arquivo.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3) ? obj.getDescricao()+"_ASSINADO" : file.getName().substring(file.getName().lastIndexOf(File.separator) + 1, file.getName().lastIndexOf(".")) );
			arquivo.setDescricaoArquivo( arquivo.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3) ? obj.getDescricao() : file.getName().substring(file.getName().lastIndexOf(File.separator) + 1, file.getName().lastIndexOf(".")));
			arquivo.setCpfAlunoDocumentacao(obj.getCpfAlunoDocumentacao());
			arquivo.setCodOrigem(obj.getCodOrigem());
			arquivo.setExtensao("pdf");
			arquivo.setResponsavelUpload(usuario);
			arquivo.setDataUpload(new Date());
			arquivo.setDataDisponibilizacao(new Date());
			arquivo.setManterDisponibilizacao(true);
			arquivo.setControlarDownload(true);
			arquivo.setDataIndisponibilizacao(null);
			arquivo.setSituacao(SituacaoArquivo.ATIVO.getValor());
			arquivo.setPastaBaseArquivo(pastaBase);
			arquivo.setPastaBaseArquivoEnum(baseArquivoEnum);
			arquivo.setPessoaVO(matriculaVO.getAluno());
			arquivo.setArquivoAssinadoDigitalmente(true);
			arquivo.setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
			arquivo.setValidarDisciplina(false);
			arquivo.setArquivoIsPdfa(obj.getArquivoIsPdfa());
			arquivo.setNovoObj(true);
			return arquivo;
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaAprovaPeloDepAluno(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set entregue=true, arquivoAprovadoPeloDep=true, notificou = false, respAprovacaoDocDep=?, dataAprovacaoDocDep=?, justificativaNegacao='', respNegarDocDep=null, dataNegarDocDep=null, situacao='OK', dataentrega=?, arquivo=?, arquivoverso=?, arquivoassinado=?, motivoindeferimentodocumentoaluno=null WHERE codigo in (";
		sql += " select codigo from documetacaomatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula where matricula.aluno in ( ";
		sql += " select aluno from matricula where matricula = ?) and tipodedocumento = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { usuario.getCodigo(), new Date(), obj.getDataEntrega(), Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo()) ? obj.getArquivoVO().getCodigo() : null, Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getCodigo()) ? obj.getArquivoVOVerso().getCodigo() : null, Uteis.isAtributoPreenchido(obj.getArquivoVOAssinado().getCodigo()) ? obj.getArquivoVOAssinado().getCodigo() : null, obj.getMatricula(), obj.getTipoDeDocumentoVO().getCodigo() });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarDocumentoMatriculaAprovaPeloDep(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set entregue=false, arquivoAprovadoPeloDep=false, respAprovacaoDocDep=null, dataAprovacaoDocDep=null WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
			cancelarDocumentoMatriculaAprovaPeloDepAluno(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarDocumentoMatriculaAprovaPeloDepAluno(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set entregue=false, arquivoAprovadoPeloDep=false, respAprovacaoDocDep=null, dataAprovacaoDocDep=null  WHERE codigo in (";
		sql += " select codigo from documetacaomatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula where matricula.aluno in ( ";
		sql += " select aluno from matricula where matricula = ?) and tipodedocumento = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula(), obj.getTipoDeDocumentoVO().getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaNotificado(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set notificou = true WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
			alterarDocumentoMatriculaNotificadoAluno(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaAlunoPostagemNotificado(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set notificouAluno = true WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaNotificadoAluno(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set notificou = true WHERE codigo in (";
		sql += " select codigo from documetacaomatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula where matricula.aluno in ( ";
		sql += " select aluno from matricula where matricula = ?) and tipodedocumento = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula(), obj.getTipoDeDocumentoVO().getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaNegadoPeloDep(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		List<DocumetacaoMatriculaVO> listaDocumentacaoMatricula = consultarDocumentacaoMatriculaPorArquivo(obj,usuario);
		 if(!Uteis.isAtributoPreenchido(listaDocumentacaoMatricula)) {
			 throw new Exception("Não foi Encontrado Documento Vinculado a esta matricula.");
		}
		String ids = UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaDocumentacaoMatricula, "codigo");
		String sql = "UPDATE DocumetacaoMatricula set situacao = 'PE', entregue =  false, arquivoAprovadoPeloDep=false, respNegarDocDep=?, dataNegarDocDep=?, justificativaNegacao=?, respAprovacaoDocDep=null, dataAprovacaoDocDep=null, motivoindeferimentodocumentoaluno=?  WHERE (codigo in ("+ids+")) "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { usuario.getCodigo(), new Date(), obj.getJustificativaNegacao(), obj.getMotivoIndeferimentoDocumentoAlunoVO().getCodigo()});
		if (obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && obj.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
			alterarDocumentoMatriculaNegadoPeloDepAluno(obj, usuario);
		}
		if(obj.getDeletarArquivo()) {
			if(listaDocumentacaoMatricula.size() > 1) {
				removerVinculoArquivoFrenteVersoDocumentacaoMatricula(ids, usuario);
			}
			removerVinculoArquivos(obj, obj.getArquivoVO(), obj.getArquivoVOVerso(), configuracaoGeralSistemaVO, usuario);
		}
	}

	private void removerVinculoArquivoFrenteVersoDocumentacaoMatricula(String ids,UsuarioVO usuario) {
		String sql = "UPDATE DocumetacaoMatricula set arquivo = null, arquivoverso = null WHERE (codigo in ("+ids+")) "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentoMatriculaNegadoPeloDepAluno(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoMatricula set arquivoAprovadoPeloDep=false, entregue=false, situacao='PE', respNegarDocDep=?, dataNegarDocDep=?, justificativaNegacao=?, respAprovacaoDocDep=null, dataAprovacaoDocDep=null, motivoindeferimentodocumentoaluno=?  WHERE codigo in (";
		sql += " select documetacaomatricula.codigo from documetacaomatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join arquivo on documetacaomatricula.arquivo = arquivo.codigo where matricula.aluno in ( ";
		sql += " select aluno from matricula where matricula = ?) and tipodedocumento = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { usuario.getCodigo(), new Date(), obj.getJustificativaNegacao(), obj.getMotivoIndeferimentoDocumentoAlunoVO().getCodigo(), obj.getMatricula(), obj.getTipoDeDocumentoVO().getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		DocumetacaoMatricula.excluir(getIdEntidade());
		String sql = "DELETE FROM DocumetacaoMatricula WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario) + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/**
	 * Responsável por realizar uma consulta de <code>DocumetacaoMatricula</code> através do valor do atributo <code>String situacao</code>. Retorna
	 * os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DocumetacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DocumetacaoMatriculaVO> consultarPorSituacao(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso WHERE documetacaomatricula.situacao like('" + valorConsulta + "%') ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	@Override
	public List<DocumetacaoMatriculaVO> consultarPorProcessadoComErro(Boolean status, String matricula, String aluno, String nomeDocumento, Date dataInicio, Date dataFim, int nivelMontarDados, int limite, int pagina, boolean controlarAcesso, DataModelo dataModelo,  UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> parametros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder("select count(documetacaomatricula.codigo) over() as qtde_total_registros, pessoa.nome nome_aluno, documetacaomatricula.* from documetacaomatricula  ");
		sqlStr.append(" inner join matricula on documetacaomatricula.matricula = matricula.matricula ");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
		sqlStr.append(" inner join tipodocumento on documetacaomatricula.tipodedocumento = tipodocumento.codigo ");
		sqlStr.append(" where documetacaomatricula.processadocomerro = ? ");
		parametros.add(status);
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" AND matricula.matricula ILIKE ? ");
			parametros.add(matricula + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(aluno)) {
			sqlStr.append(" AND pessoa.nome ILIKE  ? ");
			parametros.add(aluno + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(nomeDocumento)) {
			sqlStr.append(" AND tipodocumento.nome ILIKE ? ");
			parametros.add(nomeDocumento + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" AND documetacaomatricula.dataprocessamento >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" AND documetacaomatricula.dataprocessamento <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		sqlStr.append(" order by  documetacaomatricula.dataprocessamento desc ");
		if (limite != 0) {
			sqlStr.append(" limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		}
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return (montarDadosConsultaResumido(tabelaResultado, nivelMontarDados, usuario));
	} 
	
	public List<DocumetacaoMatriculaVO> montarDadosConsultaResumido(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<DocumetacaoMatriculaVO> vetResultado = new ArrayList<DocumetacaoMatriculaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosResumido(tabelaResultado,usuario));
		}
		return vetResultado;
	}
	
	private DocumetacaoMatriculaVO montarDadosResumido(SqlRowSet dadosSQL,UsuarioVO usuario) throws Exception {
		DocumetacaoMatriculaVO obj = new DocumetacaoMatriculaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getTipoDeDocumentoVO().setCodigo(new Integer(dadosSQL.getInt("tipoDeDocumento")));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDescricaoprocessamento(dadosSQL.getString("descricaoprocessamento"));
		obj.setDataProcessamento(dadosSQL.getTimestamp(("dataprocessamento")));
		montarDadosTipoDeDocumento(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	
	@Override
	public Integer consultarTotalDocumentosProcessados(Boolean status, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select count (distinct documetacaomatricula.codigo) as total from documetacaomatricula where documetacaomatricula.processadocomerro =  ?");
		Integer totalRegistro = 0;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { status });
		if (rs.next()) {
			totalRegistro = rs.getInt("total");
		}
		return totalRegistro;
	}

	public List<DocumetacaoMatriculaVO> consultarPorSituacaoMatricula(String situacao, String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT documetacaomatricula.*, curso.nome as nomeCurso FROM documetacaoMatricula ";
		sqlStr += " inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento ";
		sqlStr += " inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso ";
		sqlStr += " WHERE  documetacaomatricula.matricula = '" + matricula + "'";
		if(situacao.endsWith("PE")) {
			sqlStr += " and coalesce(documetacaomatricula.entregue, false) = false ";
		}else if (situacao.endsWith("OK")) {
			sqlStr += " and coalesce(documetacaomatricula.entregue, false) = true ";
		}		
		sqlStr += " ORDER BY tipodocumento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>DocumetacaoMatricula</code> através do valor do atributo <code>String tipoDocumento</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DocumetacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DocumetacaoMatriculaVO> consultarPorTipoDeDocumento(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso  WHERE DocumetacaoMatricula.tipoDeDocumento >= " + valorConsulta + " ORDER BY DocumetacaoMatricula.tipoDeDocumento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>DocumetacaoMatricula</code> através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DocumetacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DocumetacaoMatriculaVO> consultarPorCodigo(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso  WHERE DocumetacaoMatricula.codigo >= " + valorConsulta.intValue() + " ORDER BY DocumetacaoMatricula.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DocumetacaoMatriculaVO> consultarPorMatricula(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso   WHERE matricula.matricula = '" + valorConsulta + "' ORDER BY DocumetacaoMatricula.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public DocumetacaoMatriculaVO consultarDocumentacaoMatriculaExigenciaAlunoASerReaproveitado(Integer aluno, Integer tipoDocumento, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula ");
		sql.append(" inner join matricula on matricula.matricula = DocumetacaoMatricula.matricula ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" where aluno = ").append(aluno).append(" and tipodedocumento = ").append(tipoDocumento);
		sql.append(" and DocumetacaoMatricula.situacao = 'OK' and (documetacaomatricula.arquivo IS NOT NULL or documetacaomatricula.arquivoassinado IS NOT NULL) order by DocumetacaoMatricula.dataentrega desc, codigo desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return new DocumetacaoMatriculaVO();
	}

	/**
	 * Responsável por realizar uma consulta de <code>DocumetacaoMatricula</code> através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DocumetacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DocumetacaoMatriculaVO> consultarPorNomeDoAluno(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT pessoa.nome, tipodocumento.nome, documetacaomatricula.*, curso.nome as nomecurso from documetacaomatricula" + " LEFT JOIN matricula ON( matricula.matricula = documetacaomatricula.matricula )" + " LEFT JOIN pessoa ON(pessoa.codigo = matricula.aluno)" + " LEFT JOIN tipodocumento ON(tipodocumento.codigo = documetacaomatricula.tipodedocumento)" + " LEFT JOIN curso ON(curso.codigo = matricula.curso)" + " WHERE upper(pessoa.nome) like (?)";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>DocumetacaoMatriculaVO</code> resultantes da consulta.
	 */
	public List<DocumetacaoMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<DocumetacaoMatriculaVO> vetResultado = new ArrayList<DocumetacaoMatriculaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DocumetacaoMatriculaVO</code>.
	 *
	 * @return O objeto da classe <code>DocumetacaoMatriculaVO</code> com os dados devidamente montados.
	 */
	private DocumetacaoMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		DocumetacaoMatriculaVO obj = new DocumetacaoMatriculaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getTipoDeDocumentoVO().setCodigo(new Integer(dadosSQL.getInt("tipoDeDocumento")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setCurso(dadosSQL.getString("nomeCurso"));
		obj.setEntregue(dadosSQL.getBoolean("entregue"));
		obj.setEntreguePorEquivalencia(dadosSQL.getBoolean("entreguePorEquivalencia"));
		obj.setArquivoAprovadoPeloDep(dadosSQL.getBoolean("arquivoAprovadoPeloDep"));
		obj.setGerarSuspensaoMatricula(dadosSQL.getBoolean("gerarSuspensaoMatricula"));
		obj.setDataEntrega(dadosSQL.getTimestamp("dataentrega"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
		obj.getArquivoVOVerso().setCodigo(dadosSQL.getInt("arquivoVerso"));
		obj.getArquivoVOAssinado().setCodigo(dadosSQL.getInt("arquivoassinado"));
		obj.getArquivoGED().setCodigo(dadosSQL.getInt("arquivoged"));
		obj.setLocalUpload(dadosSQL.getString("localUpload"));
		obj.setDataNegarDocDep(dadosSQL.getTimestamp("dataNegarDocDep"));
		obj.setJustificativaNegacao(dadosSQL.getString("justificativaNegacao"));
		if(obj.getEntregue()) {
			obj.setJustificativaNegacao("");
		}
		obj.getRespNegarDocDep().setCodigo(dadosSQL.getInt("respNegarDocDep"));		
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		if (Uteis.isColunaExistente(dadosSQL, "documentacaoCursoExistente") && dadosSQL.getBoolean("documentacaoCursoExistente")) {
			obj.setDocumentacaoCursoExistente(dadosSQL.getBoolean("documentacaoCursoExistente"));
		}
		obj.getRespAprovacaoDocDep().setCodigo(dadosSQL.getInt("respaprovacaodocdep"));
		obj.setDataAprovacaoDocDep(dadosSQL.getTimestamp("dataaprovacaodocdep"));
		montarDadosTipoDeDocumento(obj, usuario);
		if (Uteis.isColunaExistente(dadosSQL, "motivoindeferimentodocumentoaluno")) {
			obj.getMotivoIndeferimentoDocumentoAlunoVO().setCodigo(dadosSQL.getInt("motivoindeferimentodocumentoaluno"));
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosArquivoVerso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosArquivoAssinado(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		if (!obj.getArquivoVO().isNovoObj()) {
			obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
		}
		if (!obj.getArquivoVOVerso().isNovoObj()) {
			obj.getArquivoVOVerso().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
		}
		if (Uteis.isAtributoPreenchido(obj.getArquivoGED().getCodigo())) {
			obj.getArquivoGED().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);			
			obj.setArquivoGED(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoGED().getCodigo(), nivelMontarDados, usuario));
		}
		montarDadosUsuario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosRespAprovacaoDocDep(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosMotivoIndeferimentoDocumentoAluno(obj, usuario);
		montarDadosUsuarioRespNegarDocumento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>DocumetacaoMatriculaVO</code> no BD. Faz uso da operação <code>excluir</code>
	 * disponível na classe <code>DocumetacaoMatricula</code>.
	 *
	 * @param <code>matricula</code>
	 *            campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDocumetacaoMatriculas(List<DocumetacaoMatriculaVO> objetos, String matricula, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" DELETE FROM DocumetacaoMatricula WHERE (matricula = ?) and codigo not in (0");
		for (DocumetacaoMatriculaVO documetacaoMatriculaVO : objetos) {
			sql.append(", ").append(documetacaoMatriculaVO.getCodigo());
		}
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { matricula });
	}
	
	/**
	 * Realiza a alteração da documentação GED.
	 * 
	 * @param configuracaoGeralSistemaVO
	 * @param listaDocumentacaoGED
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentacaoGED(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,List<DocumentacaoGEDVO> listaDocumentacaoGED, UsuarioVO usuario) throws Exception {
		for (DocumentacaoGEDVO documentoGED : listaDocumentacaoGED) {
			if (documentoGED.getArquivo().getNome() != null && !documentoGED.getArquivo().getNome().isEmpty() && documentoGED.getArquivo().getCodigo().equals(0)) {
				getFacadeFactory().getArquivoFacade().incluir(documentoGED.getArquivo(), false, usuario, configuracaoGeralSistemaVO);
				getFacadeFactory().getDocumentacaoGEDInterfaceFacade().alterar(documentoGED, false, usuario);
			}
		}
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>DocumetacaoMatriculaVO</code> contidos em um Hashtable no BD. Faz uso da operação
	 * <code>excluirDocumetacaoMatriculas</code> e <code>incluirDocumetacaoMatriculas</code> disponíveis na classe <code>DocumetacaoMatricula</code>.
	 *
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumetacaoMatriculas(MatriculaVO matriculaVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema , Boolean validarExclusaoDocumentos) throws Exception {
		if(validarExclusaoDocumentos != null && Boolean.TRUE.equals(validarExclusaoDocumentos)) {
			excluirDocumetacaoMatriculas(matriculaVO.getDocumetacaoMatriculaVOs(), matriculaVO.getMatricula(), usuario);
		}		
		for (DocumetacaoMatriculaVO documetacaoMatriculaVO : matriculaVO.getDocumetacaoMatriculaVOs()) {
			documetacaoMatriculaVO.setMatricula(matriculaVO.getMatricula());
			if (documetacaoMatriculaVO.getEntregue()) {
				documetacaoMatriculaVO.setSituacao("OK");
				documetacaoMatriculaVO.setDataNegarDocDep(null);
				documetacaoMatriculaVO.setRespNegarDocDep(null);
				documetacaoMatriculaVO.setJustificativaNegacao("");
				if (!Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getDataEntrega())) {
					documetacaoMatriculaVO.setDataEntrega(new Date());
				}
				if (!documetacaoMatriculaVO.getArquivoVO().getNome().equals("")) {
					documetacaoMatriculaVO.getArquivoVO().setCpfAlunoDocumentacao(matriculaVO.getAluno().getCPF());
					documetacaoMatriculaVO.getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
					documetacaoMatriculaVO.getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
				}
			} else {
				documetacaoMatriculaVO.setSituacao("PE");
				if(!documetacaoMatriculaVO.getIsPossuiArquivo()) {
					documetacaoMatriculaVO.setDataEntrega(null);
					documetacaoMatriculaVO.setUsuario(null);
				}
			}
			documetacaoMatriculaVO.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getServidorArquivoOnline()));
			if(documetacaoMatriculaVO.getIsPossuiArquivoVerso()) {
				documetacaoMatriculaVO.getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getServidorArquivoOnline()));
			}
			if (documetacaoMatriculaVO.isNovoObj()) {
				incluir(documetacaoMatriculaVO, matriculaVO.getAluno(), usuario, configuracaoGeralSistema);
			} else {
				alterar(documetacaoMatriculaVO, matriculaVO.getAluno(), usuario, configuracaoGeralSistema);
			}
		}
		if(!matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
			getFacadeFactory().getMatriculaFacade().realizarVerificacaoELiberacaoSuspensaoMatricula(matriculaVO.getMatricula());
		}
		getFacadeFactory().getMatriculaFacade().alterarLocalArmazenamentoDocumentosMatricula(matriculaVO.getLocalArmazenamentoDocumentosMatricula(), matriculaVO.getMatricula(), usuario);
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarDataUltimaAlteracao_Online(matriculaVO.getMatricula(), usuario);
	}

	/**
	 * Operação responsável por incluir objetos da <code>DocumetacaoMatriculaVO</code> no BD. Garantindo o relacionamento com a entidade principal
	 * <code>academico.Matricula</code> através do atributo de vínculo.
	 *
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDocumetacaoMatriculas(MatriculaVO matriculaVO, String matriculaPrm, List<DocumetacaoMatriculaVO> objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		Iterator<DocumetacaoMatriculaVO> e = objetos.iterator();
		while (e.hasNext()) {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) e.next();
			obj.setMatricula(matriculaPrm);
			if (obj.getEntregue()) {
				if (!Uteis.isAtributoPreenchido(obj.getDataEntrega())) {
					obj.setDataEntrega(new Date());
				}
				if (!Uteis.isAtributoPreenchido(obj.getUsuario())) {
					obj.setUsuario(matriculaVO.getUsuario());
				}
				if (!obj.getArquivoVO().getNome().equals("")) {
					obj.getArquivoVO().setCpfAlunoDocumentacao(matriculaVO.getAluno().getCPF());
					obj.getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
					obj.getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
					// obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
				}
			}
			incluir(obj, matriculaVO.getAluno(), usuario, configuracaoGeralSistema);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>DocumetacaoMatriculaVO</code> relacionados a um objeto da classe
	 * <code>academico.Matricula</code>.
	 *
	 * @param matricula
	 *            Atributo de <code>academico.Matricula</code> a ser utilizado para localizar os objetos da classe <code>DocumetacaoMatriculaVO</code>
	 *            .
	 * @return List Contendo todos os objetos da classe <code>DocumetacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculas(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso, tipodocumento.nome as nomeTipoDocumento, ");
		sql.append("case when ( ");
		sql.append("select true from documentacaocurso  ");
		sql.append("where documentacaocurso.tipodedocumento = documetacaomatricula.tipodedocumento and documentacaocurso.curso = curso.codigo");
		sql.append(" limit 1) then true else false end as documentacaoCursoExistente ");
		sql.append("FROM DocumetacaoMatricula ");
		sql.append("inner join matricula on matricula.matricula = documetacaomatricula.matricula ");
		sql.append("inner join tipodocumento on	tipodocumento.codigo = documetacaomatricula.tipodedocumento ");
		sql.append("inner join curso on	curso.codigo = matricula.curso ");
		//sql.append("left join documentacaocurso on curso.codigo = documentacaocurso.curso and documetacaomatricula.tipodedocumento = documentacaocurso.tipodedocumento ");
		sql.append("WHERE matricula.matricula = ? ");
		sql.append("order by tipodocumento.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { matricula });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAluno(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultarDocumetacaoMatriculaPorMatriculaAluno(matricula, nivelMontarDados, Boolean.FALSE, controlarAcesso, usuario);
	}
	
	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAluno(String matricula, int nivelMontarDados, Boolean enviarDocumentoXml, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso, tipodocumento.nome as nomeTipoDocumento FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento WHERE matricula.matricula = ? " + (enviarDocumentoXml ? " AND tipodocumento.enviarDocumentoXml and (documetacaomatricula.arquivo is not null or documetacaomatricula.arquivoAssinado is not null or documetacaomatricula.arquivoGed is not null) " : "") + " order by  tipodocumento.nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(String matricula, int nivelMontarDados, boolean entregue, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso, tipodocumento.nome as nomeTipoDocumento FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento WHERE matricula.matricula = ? AND entregue = ?  order by  tipodocumento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula, entregue });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public Boolean consultarContratoAssinadoPorMatriculaAlunoEntregue(String matricula, UsuarioVO usuario) throws Exception {
		String sql = "SELECT entregue FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento WHERE matricula.matricula = ? and tipodocumento.contrato = true and DocumetacaoMatricula.arquivo is not null order by  tipodocumento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula });
		if (tabelaResultado.next()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAlunoPendenteEntregaSuspendeMatricula(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		String sql = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso, tipodocumento.nome as nomeTipoDocumento FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento WHERE matricula.matricula = ? AND entregue = false AND gerarSuspensaoMatricula = true  order by  tipodocumento.nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>DocumetacaoMatriculaVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public DocumetacaoMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join curso on curso.codigo = matricula.curso WHERE DocumetacaoMatricula.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public Boolean verificaAlunoDevendoDocumentoQueSuspendeMatricula(String matriculaAluno) throws Exception {
		String sql = " select distinct documetacaomatricula.matricula from documetacaomatricula " + " where documetacaomatricula.matricula = ? " + " and entregue = false " + " and gerarsuspensaomatricula = true ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matriculaAluno });
		return tabelaResultado.next();
	}
	
	
	public boolean verificarAlunoEntregouTodosDocumentosQueSuspendeMatriculaParaAtivacaoMatricula(String matriculaAluno) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select m.matricula  from matricula  m ");
		sql.append(" inner join curso on curso.codigo = m.curso ");
		sql.append(" where m.matricula = ?	 ");
		sql.append(" and curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios = true " );
		sql.append(" and not exists (select (codigo) from documetacaomatricula where documetacaomatricula.matricula = m.matricula  ");		
		sql.append(" and documetacaomatricula.gerarsuspensaomatricula = true ");	
		sql.append(" and (coalesce(documetacaomatricula.entregue, false) = false ))	 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matriculaAluno );
		return tabelaResultado.next();
	}

	public static void montarDadosArquivo(DocumetacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVO().getCodigo().intValue() == 0) {
			obj.setArquivoVO(new ArquivoVO());
			return;
		}
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosArquivoVerso(DocumetacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVOVerso().getCodigo().intValue() == 0) {
			obj.setArquivoVOVerso(new ArquivoVO());
			return;
		}
		obj.setArquivoVOVerso(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVOVerso().getCodigo(), nivelMontarDados, usuario));
	}
	
	public static void montarDadosArquivoAssinado(DocumetacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVOAssinado().getCodigo().intValue() == 0) {
			obj.setArquivoVOAssinado(new ArquivoVO());
			return;
		}
		obj.setArquivoVOAssinado(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVOAssinado().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosTipoDeDocumento(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTipoDeDocumentoVO().getCodigo().intValue() == 0) {
			obj.setTipoDeDocumentoVO(new TipoDocumentoVO());
			return;
		}
		obj.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(obj.getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public static void montarDadosUsuario(DocumetacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUsuario().getCodigo().intValue() == 0) {
			obj.setUsuario(new UsuarioVO());
			return;
		}
		obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), nivelMontarDados, usuario));
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT documetacaoMatricula.codigo, documetacaoMatricula.matricula, documetacaoMatricula.entregue ,documetacaoMatricula.gerarSuspensaoMatricula, documetacaoMatricula.situacao, ");
		sql.append("documetacaoMatricula.dataEntrega, tipoDocumento.codigo AS \"tipoDocumento.codigo\", tipoDocumento.nome AS \"tipoDocumento.nome\", ");
		sql.append("documetacaoMatricula.usuario, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, ");
		sql.append("arquivo.descricao as descricaoArquivo, arquivo.cpfAlunoDocumentacao,");
		sql.append("arquivo.servidorArquivoOnline as servidorArquivoOnline, ");
		sql.append("arquivoGed.codigo AS codArquivoGed, arquivoGed.pastaBaseArquivo as pastaBaseArquivoGed, arquivoGed.nome AS nomeArquivoGed, ");
		sql.append("arquivoGed.descricao as descricaoArquivoGed, arquivoGed.cpfAlunoDocumentacao as cpfAlunoDocumentacaoGed,");
		sql.append("arquivoGed.servidorArquivoOnline as servidorArquivoOnlineGed, "); 
		sql.append("arquivoVerso.codigo AS codArquivoVerso, arquivoVerso.pastaBaseArquivo as pastaBaseArquivoVerso, arquivoVerso.nome AS nomeArquivoVerso, ");
		sql.append("arquivoVerso.servidorArquivoOnline as servidorArquivoOnlineVerso, ");
		sql.append("arquivoVerso.descricao as descricaoArquivoVerso, arquivoVerso.cpfAlunoDocumentacao as cpfAlunoDocumentacaoVerso, curso.nome as nomeCurso, usuario.nome as usuarioUpload , documetacaoMatricula.ano , documetacaoMatricula.semestre , ");
		sql.append("arquivoAssinado.codigo as codArquivoAssinado, arquivoAssinado.pastaBaseArquivo as pastaBaseArquivoAssinado, arquivoAssinado.nome as nomeArquivoAssinado , arquivoAssinado.descricao as descricaoArquivoAssinado, ");		
		sql.append("arquivoAssinado.cpfAlunoDocumentacao as cpfAlunoDocumentacaoArquivoAssinado, arquivoAssinado.servidorArquivoOnline as servidorArquivoOnlineAssinado, documetacaoMatricula.respnegardocdep, documetacaoMatricula.datanegardocdep  , documetacaoMatricula.justificativanegacao, documetacaoMatricula.motivoindeferimentodocumentoaluno, ");
		sql.append("documetacaoMatricula.respaprovacaodocdep, documetacaoMatricula.dataaprovacaodocdep, documetacaomatricula.respnegardocdep, documetacaomatricula.datanegardocdep, ");
		sql.append("arquivo.arquivoispdfa arquivo_arquivoispdfa, arquivoverso.arquivoispdfa arquivoverso_arquivoispdfa, arquivoassinado.arquivoispdfa arquivoassinado_arquivoispdfa ");
		sql.append("FROM DocumetacaoMatricula ");
		sql.append("INNER JOIN tipoDocumento ON tipoDocumento.codigo = documetacaoMatricula.tipodeDocumento ");
		sql.append("LEFT JOIN arquivo ON arquivo.codigo = documetacaoMatricula.arquivo ");
		sql.append("LEFT JOIN arquivo as arquivoVerso ON arquivoVerso.codigo = documetacaoMatricula.arquivoVerso ");
		sql.append("LEFT JOIN arquivo as arquivoAssinado on	arquivoAssinado.codigo = documetacaoMatricula.arquivoassinado ");
		sql.append("LEFT JOIN arquivo as arquivoGed on	arquivoGed.codigo = documetacaoMatricula.arquivoGed ");
		sql.append("INNER JOIN matricula ON matricula.matricula = documetacaoMatricula.matricula ");
		sql.append("INNER JOIN curso ON curso.codigo = matricula.curso ");
		sql.append("LEFT JOIN usuario ON usuario.codigo=documetacaoMatricula.usuario");
		return sql;
	}

	public List<DocumetacaoMatriculaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<DocumetacaoMatriculaVO> vetResultado = new ArrayList<DocumetacaoMatriculaVO>(0);
		while (tabelaResultado.next()) {
			DocumetacaoMatriculaVO obj = new DocumetacaoMatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(DocumetacaoMatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setCurso(dadosSQL.getString("nomeCurso"));
		obj.setEntregue(dadosSQL.getBoolean("entregue"));
		obj.setGerarSuspensaoMatricula(dadosSQL.getBoolean("gerarSuspensaoMatricula"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setDataEntrega(dadosSQL.getTimestamp("dataEntrega"));
		obj.getTipoDeDocumentoVO().setCodigo(dadosSQL.getInt("tipoDocumento.codigo"));
		obj.getTipoDeDocumentoVO().setNome(dadosSQL.getString("tipoDocumento.nome"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getArquivoVO().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
		obj.getArquivoVO().setNome(dadosSQL.getString("nomeArquivo"));
		obj.getArquivoVO().setDescricao(dadosSQL.getString("descricaoArquivo"));
		obj.getArquivoVO().setDescricaoAntesAlteracao(dadosSQL.getString("descricaoArquivo"));
		obj.getArquivoVO().setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacao"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnline"))){
			obj.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnline")));	
		}
		obj.getArquivoVO().setArquivoIsPdfa(Objects.nonNull(dadosSQL.getObject("arquivo_arquivoispdfa")) ? dadosSQL.getBoolean("arquivo_arquivoispdfa") : null);
		obj.getArquivoGED().setCodigo(dadosSQL.getInt("codArquivoGed"));
		obj.getArquivoGED().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivoGed"));
		obj.getArquivoGED().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
		obj.getArquivoGED().setNome(dadosSQL.getString("nomeArquivoGed"));
		obj.getArquivoGED().setDescricao(dadosSQL.getString("descricaoArquivoGed"));
		obj.getArquivoGED().setDescricaoAntesAlteracao(dadosSQL.getString("descricaoArquivoGed"));
		obj.getArquivoGED().setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacaoGed"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnlineGed"))){
			obj.getArquivoGED().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnlineGed")));	
		}
		obj.getArquivoVOVerso().setCodigo(dadosSQL.getInt("codArquivoVerso"));
		obj.getArquivoVOVerso().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivoVerso"));
		obj.getArquivoVOVerso().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
		obj.getArquivoVOVerso().setNome(dadosSQL.getString("nomeArquivoVerso"));
		obj.getArquivoVOVerso().setDescricao(dadosSQL.getString("descricaoArquivoVerso"));
		obj.getArquivoVOVerso().setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacaoVerso"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnlineVerso"))){
			obj.getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnlineVerso")));	
		}
		obj.getArquivoVOVerso().setArquivoIsPdfa(Objects.nonNull(dadosSQL.getObject("arquivoverso_arquivoispdfa")) ? dadosSQL.getBoolean("arquivoverso_arquivoispdfa") : null);
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("usuarioUpload"))){
			obj.getUsuario().setNome(dadosSQL.getString("usuarioUpload"));
		}
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getArquivoVOAssinado().setCodigo(dadosSQL.getInt("codArquivoAssinado"));
		obj.getArquivoVOAssinado().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivoAssinado"));
		obj.getArquivoVOAssinado().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
		obj.getArquivoVOAssinado().setNome(dadosSQL.getString("nomeArquivoAssinado"));
		obj.getArquivoVOAssinado().setDescricao(dadosSQL.getString("descricaoArquivoAssinado"));
		obj.getArquivoVOAssinado().setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacaoArquivoAssinado"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnlineAssinado"))){
			obj.getArquivoVOAssinado().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnlineAssinado")));	
		}
		obj.getArquivoVOAssinado().setArquivoIsPdfa(Objects.nonNull(dadosSQL.getObject("arquivoassinado_arquivoispdfa")) ? dadosSQL.getBoolean("arquivoassinado_arquivoispdfa") : null);
		obj.getRespNegarDocDep().setCodigo(dadosSQL.getInt("respnegardocdep"));
		obj.setDataNegarDocDep(dadosSQL.getTimestamp("datanegardocdep"));
		obj.setJustificativaNegacao(dadosSQL.getString("justificativanegacao"));
		if(obj.getEntregue()) {
			obj.setJustificativaNegacao(null);
		}
		if (Uteis.isColunaExistente(dadosSQL, "motivoindeferimentodocumentoaluno")) {
			obj.setMotivoIndeferimentoDocumentoAlunoVO(Uteis.montarDadosVO(dadosSQL.getInt("motivoindeferimentodocumentoaluno"), MotivoIndeferimentoDocumentoAlunoVO.class, p -> getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().consultarPorChavePrimaria(p, false, new UsuarioVO())));
		}
		obj.getRespAprovacaoDocDep().setCodigo(dadosSQL.getInt("respaprovacaodocdep"));
		obj.setDataAprovacaoDocDep(dadosSQL.getTimestamp("dataaprovacaodocdep"));
		obj.getRespNegarDocDep().setCodigo(dadosSQL.getInt("respnegardocdep"));
		obj.setDataNegarDocDep(dadosSQL.getTimestamp("datanegardocdep"));
		montarDadosRespAprovacaoDocDep(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new UsuarioVO());
		montarDadosUsuarioRespNegarDocumento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new UsuarioVO());
		
		
	}

	public List<DocumetacaoMatriculaVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(documetacaoMatricula.matricula) = '").append(matricula.toUpperCase()).append("' ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}
	
	public List<DocumetacaoMatriculaVO> consultaRapidaPorMatricula(String matricula) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(documetacaoMatricula.matricula) = '").append(matricula.toUpperCase()).append("' ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<DocumetacaoMatriculaVO> consultarDocumentoPendenteAprovacao(String matricula, Boolean trazerDocumentosIndeferidos, Boolean trazerDocumentosDeferidos, Boolean trazerDocumentosPendentes) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE documetacaoMatricula.matricula = '").append(matricula.toUpperCase()).append("' ");
		sql.append(" and ((COALESCE(arquivoaprovadopelodep, FALSE) = false and arquivo.codigo is not null and arquivo.codigo <> 0 and (respnegardocdep is null or respnegardocdep = 0)) ");
		if(trazerDocumentosIndeferidos) {
			sql.append(" or (COALESCE(arquivoaprovadopelodep, FALSE) = false and (respnegardocdep is not null and respnegardocdep > 0)) ");
		}
		if(trazerDocumentosDeferidos) {
			sql.append(" or (arquivoaprovadopelodep = true and entregue = true) ");
		}
		if(trazerDocumentosPendentes) {
			sql.append(" or (entregue = false and arquivo.codigo is  null ) ");
		}
		sql.append(" ) ");
		sql.append(" order by tipoDocumento.nome");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<DocumetacaoMatriculaVO> consultarDocumentoAprovadoPendenteNotificar(String matricula) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE documetacaoMatricula.matricula = '").append(matricula.toUpperCase()).append("' ");
		sql.append(" and coalesce(notificou, false) = false and arquivoaprovadopelodep = true and (arquivo.codigo is not null or arquivoAssinado.codigo is not null) ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<DocumetacaoMatriculaVO> consultarDocumentoAprovadoPendenteNotificarAlunoDocPostado(String matricula) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE documetacaoMatricula.matricula = '").append(matricula.toUpperCase()).append("' ");
		sql.append(" and notificouAluno = false and arquivo.codigo is not null ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return DocumetacaoMatricula.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		DocumetacaoMatricula.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alteraDocumentoAluno(final String situacao, final Date dataEntrega, final Integer arquivo, final Integer arquivoVerso, final Integer arquivoAssinado, final Integer aluno, final Integer tipoDeDocumento, final Integer responsavel, final String matricula, final Boolean entregue, final Boolean possuiArquivo) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE documetacaomatricula set situacao = ?, arquivo = ?, dataentrega = ?, entregue=?, usuario = ?, arquivoVerso = ?, arquivoassinado = ? ");
				sql.append(" from matricula where matricula.matricula = documetacaomatricula.matricula and matricula.aluno = ? and documetacaomatricula.tipodedocumento = ? ");
				sql.append(" and entregue = false and arquivo is null");
				if (Uteis.isAtributoPreenchido(matricula)) {
					sql.append(" and documetacaomatricula.matricula <>  '").append(matricula).append("'");
				}
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, situacao);

				if (arquivo == null || arquivo.equals(0)) {
					ps.setNull(2, 0);
				} else {
					ps.setInt(2, arquivo);
				}
				if (entregue || possuiArquivo) {
					ps.setTimestamp(3, Uteis.getDataJDBCTimestamp(dataEntrega));
				} else {
					ps.setNull(3, 0);
				}
				ps.setBoolean(4, entregue);
				if (Uteis.isAtributoPreenchido(responsavel) && (entregue || possuiArquivo) && Uteis.isAtributoPreenchido(dataEntrega)) {
					ps.setInt(5, responsavel);
				} else {
					ps.setNull(5, 0);
				}
				if (arquivoVerso == null || arquivoVerso.equals(0)) {
					ps.setNull(6, 0);
				} else {
					ps.setInt(6, arquivoVerso);
				}
				if (Uteis.isAtributoPreenchido(arquivoAssinado)) {
					ps.setInt(7, arquivoAssinado);
				} else {
					ps.setNull(7, 0);
				}
				ps.setInt(8, aluno);
				ps.setInt(9, tipoDeDocumento);
				return ps;
			}
		});
	}

	@Override
	public String consultarExistenciaPendenciaDocumentacaoPorMatricula(Integer pessoa, String matricula, List<TipoDocumentoVO> documentosValidarPendenciaVOs, boolean validarTodosDocumentosEntregues) throws Exception {
		StringBuilder sql = new StringBuilder("select array_to_string(array_agg(tipodocumento.nome), ', ') as documentosPendentes from documetacaomatricula ");
		sql.append(" inner join matricula on documetacaomatricula.matricula = matricula.matricula ");
		sql.append(" inner join tipodocumento on documetacaomatricula.tipodedocumento = tipodocumento.codigo ");
		sql.append(" left join documentacaocurso on documentacaocurso.curso = matricula.curso ");
		sql.append(" and documentacaocurso.tipodedocumento = documetacaomatricula.tipodedocumento ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" where entregue = false ");
		if(Uteis.isAtributoPreenchido(documentosValidarPendenciaVOs)) {
			sql.append(" and documetacaomatricula.tipodedocumento in ( ");
			int x = 0;
			for(TipoDocumentoVO tipoDocumentoVO: documentosValidarPendenciaVOs) {
				if(x>0) {
					sql.append(", ");
				}
				sql.append(tipoDocumentoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}else if(!validarTodosDocumentosEntregues) {
			sql.append(" and ((documentacaocurso.codigo is not null and documentacaocurso.gerarsuspensaomatricula = true )");
			sql.append(" or (documentacaocurso.codigo is null and documetacaomatricula.gerarsuspensaomatricula = true)) ");
		}
		SqlRowSet rs = null;
		if (!matricula.equals("")) {
			sql.append(" and matricula.matricula = ? ");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		} else {
			sql.append(" and pessoa.codigo = ? ");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), pessoa);
		}
		if(rs.next()) {
			return rs.getString("documentosPendentes");
		}
		return null;
	}
	
	@Override
	public String consultarExistenciaPendenciaDocumentacaoPorMatriculaPorGerarSuspensao(String matricula, boolean isGerarSuspensaoMatricula ) throws Exception {
		StringBuilder sql = new StringBuilder("select array_to_string(array_agg(tipodocumento.nome), ', ') as documentosPendentes from documetacaomatricula ");
		sql.append(" inner join matricula on documetacaomatricula.matricula = matricula.matricula ");
		sql.append(" inner join tipodocumento on documetacaomatricula.tipodedocumento = tipodocumento.codigo ");
		sql.append(" where entregue = false ");
		sql.append(" and gerarsuspensaomatricula = ").append(isGerarSuspensaoMatricula);
		sql.append(" and matricula.matricula = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		if(rs.next()) {
			return rs.getString("documentosPendentes");
		}
		return null;
	}
	
	@Override
	public void executarGeracaoSituacaoDocumentacaoMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		List<DocumentacaoCursoVO> documentacaoCursoVOs = getFacadeFactory().getDocumentacaoCursoFacade().consultarPorCurso(matriculaVO.getCurso().getCodigo(), false, usuario);
		for (Iterator<DocumetacaoMatriculaVO> iterator = matriculaVO.getDocumetacaoMatriculaVOs().iterator(); iterator.hasNext();) {
			DocumetacaoMatriculaVO documetacaoMatriculaVO = (DocumetacaoMatriculaVO) iterator.next();
			for (DocumentacaoCursoVO documentacaoCursoVO : documentacaoCursoVOs) {
				if (documetacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo().equals(documentacaoCursoVO.getTipoDeDocumentoVO().getCodigo())) {
					documetacaoMatriculaVO.setGerarSuspensaoMatricula(documentacaoCursoVO.getGerarSuspensaoMatricula());
					break;
				}
			}
			if ((documetacaoMatriculaVO.isEntregue().booleanValue()) && (!documetacaoMatriculaVO.getSituacao().equals("EI"))) {
				documetacaoMatriculaVO.setSituacao("OK");
			} else {
				if (!documetacaoMatriculaVO.isEntregue().booleanValue()) {
					documetacaoMatriculaVO.setSituacao("PE");
				}
			}
		}
	}
	
	@Override
	public DocumetacaoMatriculaVO consultarPorArquivoGED(ArquivoVO arquivo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo, entregue, arquivoged FROM documetacaomatricula WHERE arquivoged = ").append(arquivo.getCodigo());
		
		SqlRowSet tabelaResultado  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDadosConsultaArquivoGED(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		return null;
	}

	private DocumetacaoMatriculaVO montarDadosConsultaArquivoGED(SqlRowSet tabelaResultado, int nivelmontardadosDadosbasicos) {
		DocumetacaoMatriculaVO obj = new DocumetacaoMatriculaVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setEntregue(tabelaResultado.getBoolean("entregue"));
		obj.getArquivoGED().setCodigo(tabelaResultado.getInt("arquivoged"));
		return obj;
	}
	
	public DocumetacaoMatriculaVO consultarPorTipoDeDocumentoMatricula(Integer codigoTipoDocumento, String matricula, int nivelmontardadosTodos, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSQLPadraoConsultaBasica());
		sql.append(" WHERE documetacaoMatricula.tipodedocumento = ? and documetacaoMatricula.matricula = ?");

		SqlRowSet tabelaResultado  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoTipoDocumento, matricula);
		if (tabelaResultado.next()) {
			DocumetacaoMatriculaVO documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
			montarDadosBasico(documetacaoMatriculaVO, tabelaResultado);
			return documetacaoMatriculaVO;
		}
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentacaoGed(final DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getUsuario().getCodigo() == 0 && usuario != null) {
			obj.getUsuario().setCodigo(usuario.getCodigo());
		}
		final String sql = "UPDATE DocumetacaoMatricula set entregue= ?, dataentrega= ?, usuario= ?, arquivoGED= ?,  situacao=?, arquivoassinado = ? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.isEntregue().booleanValue());
				if (obj.isEntregue().booleanValue()) {
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataEntrega()));
					if(Uteis.isAtributoPreenchido(obj.getUsuario())) {
					sqlAlterar.setInt(3, obj.getUsuario().getCodigo());					
				} else {
						sqlAlterar.setNull(3, 0);
					}
				} else {
					sqlAlterar.setNull(2, 0);
					sqlAlterar.setNull(3, 0);					
				}				
				if(Uteis.isAtributoPreenchido(obj.getArquivoGED())) {
					sqlAlterar.setInt(4, obj.getArquivoGED().getCodigo());
				}else {
					sqlAlterar.setNull(4, 0);
				}
				sqlAlterar.setString(5, obj.getSituacao());
				if(Uteis.isAtributoPreenchido(obj.getArquivoVOAssinado())) {
					sqlAlterar.setInt(6, obj.getArquivoVOAssinado().getCodigo());
				}else {
					sqlAlterar.setNull(6, 0);
				}
				sqlAlterar.setInt(7, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
		
	}
	
	@Override
	public Boolean realizarVerificacaoDocumentacaoMatriculaVinculadoArquivoGed(Integer arquivo, Integer documentacaoMatricula) throws Exception {
		StringBuilder sql  =  new StringBuilder("select codigo from DocumetacaoMatricula ");
		sql.append(" where arquivoGED = ").append(arquivo);
		sql.append(" and codigo = ").append(documentacaoMatricula);
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoDocumentacaoGed(final DocumentacaoGEDVO obj, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo())) {
			final String sql = "UPDATE DocumetacaoMatricula set entregue= ?, dataentrega= ?, usuario= ?, arquivoGED= ?, situacao= ? WHERE ((arquivoGED = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, false);
					sqlAlterar.setNull(2, 0);
					sqlAlterar.setNull(3, 0);
					sqlAlterar.setNull(4, 0);
					sqlAlterar.setString(5,"PE");
					sqlAlterar.setInt(6, obj.getArquivo().getCodigo().intValue());
					return sqlAlterar;
				}
			});
			
			final String sql2 = "UPDATE DocumetacaoMatricula set entregue= ?, dataentrega= ?, usuario= ?, arquivoAssinado= ?, situacao= ? WHERE ((arquivoAssinado = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql2);
					sqlAlterar.setBoolean(1, false);
					sqlAlterar.setNull(2, 0);
					sqlAlterar.setNull(3, 0);
					sqlAlterar.setNull(4, 0);
					sqlAlterar.setString(5,"PE");
					sqlAlterar.setInt(6, obj.getArquivo().getCodigo().intValue());
					return sqlAlterar;
				}
			});
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoDocumentacaoGed(final DocumentacaoGEDVO obj, final DocumetacaoMatriculaVO documetacaoMatriculaVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo()) && Uteis.isAtributoPreenchido(documetacaoMatriculaVO)) {
			final String sql = "UPDATE DocumetacaoMatricula set entregue= ?, dataentrega= ?, usuario= ?, arquivoGED= ?, situacao= ? WHERE ((arquivoGED = ?)) and codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, false);
					sqlAlterar.setNull(2, 0);
					sqlAlterar.setNull(3, 0);
					sqlAlterar.setNull(4, 0);
					sqlAlterar.setString(5,"PE");
					sqlAlterar.setInt(6, obj.getArquivo().getCodigo().intValue());
					sqlAlterar.setInt(7, documetacaoMatriculaVO.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		}
	}
	
	/**
	 * Método que exclui o ArquivoVO e ArquivoVOVerso de DocumetacaoMatriculaVO e zera os códigos referentes a estes objetos
	 * @param documetacaoMatriculaVO
	 * @param arquivoVO
	 * @param arquivoVersoVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	private void removerVinculoArquivos(DocumetacaoMatriculaVO documetacaoMatriculaVO, ArquivoVO arquivoVO, ArquivoVO arquivoVersoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception{
		if (Uteis.isAtributoPreenchido(arquivoVO)) {
			alterarCodigoArquivo(documetacaoMatriculaVO, 0, usuarioVO);
			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuarioVO, configuracaoGeralSistemaVO);
		}
		if (Uteis.isAtributoPreenchido(arquivoVersoVO)) {
			alterarCodigoArquivoVerso(documetacaoMatriculaVO, 0, usuarioVO);
			getFacadeFactory().getArquivoFacade().excluir(arquivoVersoVO, usuarioVO, configuracaoGeralSistemaVO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void inserirDocumentoAssinado(String ids, ArquivoVO arquivoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("update documetacaomatricula set arquivoassinado = ").append(arquivoVO.getCodigo());
		sqlStr.append(" where codigo in (").append(ids).append(") ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	public Boolean consultarSeExistemDocumentosPendentesAluno(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DocumetacaoMatricula.codigo FROM DocumetacaoMatricula inner join matricula on matricula.matricula = documetacaomatricula.matricula inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento inner join curso on curso.codigo = matricula.curso WHERE documetacaomatricula.matricula = ? and entregue is false and (arquivo is null or arquivo = 0) and tipodocumento.permitirpostagemportalaluno ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { matricula });
		if (tabelaResultado.next()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Boolean validaAlunoAssinouContratoMatricula(String matricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT validaAlunoAssinouContratoMatricula((");
		sqlStr.append(" select codigo from matriculaperiodo where matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("' AND MatriculaPeriodo.situacaomatriculaperiodo != 'PC'");
		sqlStr.append(" order by (MatriculaPeriodo.ano||'/'||MatriculaPeriodo.semestre) desc,");
		sqlStr.append(" case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO')");
		sqlStr.append(" then 1 else 2 end,");
		sqlStr.append(" MatriculaPeriodo.codigo desc limit 1)) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		tabelaResultado.next();
		return tabelaResultado.getBoolean("validaAlunoAssinouContratoMatricula");
	}
	
	public List<DocumetacaoMatriculaVO> consultarDocumentosPendentesPorMatricula(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DocumetacaoMatricula.*, curso.nome as nomeCurso ");
		sql.append(" FROM DocumetacaoMatricula ");
		sql.append(" inner join matricula on matricula.matricula = documetacaomatricula.matricula ");
		sql.append(" inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" WHERE documetacaomatricula.matricula = '").append(matricula).append("'");
		sql.append(" and entregue is false and (arquivo is null or arquivo = 0)");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(resultado, nivelMontarDados, usuario);
	}
	
	public List<DocumetacaoMatriculaVO> consultarDocumentoMatriculaEntregue(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO,  Integer nivelMontarDados, UsuarioVO usuario) throws Exception{		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select curso.nome as nomeCurso, documetacaomatricula.* from documetacaomatricula  ");
		sqlStr.append(" inner join arquivo on arquivo.codigo = documetacaomatricula.arquivo ");
		sqlStr.append(" inner join matricula on matricula.matricula = documetacaomatricula.matricula ");
		
//		sqlStr.append(" inner join Pessoa ON (Matricula.aluno = pessoa.codigo)  ");
//		sqlStr.append(" and  Matricula.Matricula = (select mt.matricula from matricula mt where pessoa.codigo =  mt.aluno order by mt.data limit 1)  ");
		
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join lateral ( ");
		sqlStr.append(" select m.aluno, d.tipodedocumento, arquivo, coalesce(arquivoverso, 0), max(m.matricula) as matricula from documetacaomatricula d ");
		sqlStr.append("	inner join matricula as m on m.matricula = d.matricula ");
		sqlStr.append("	inner join arquivo as a on a.codigo = d.arquivo ");
		sqlStr.append("	where d.entregue = true and d.arquivo is not null and d.arquivoassinado is null AND d.situacao = 'OK' ");
		sqlStr.append("	and a.arquivoAssinadoFuncionario = false and a.arquivoAssinadoUnidadeEnsino = false and a.arquivoAssinadoUnidadeCertificadora = false ");
		if(assinarDocumentoEntregueVO.getConsiderarDocumentosProcessadorComErro()) {
			sqlStr.append(" and (d.processadocomerro = true or d.processadocomerro is null)");
		}else {
			sqlStr.append(" AND d.processadocomerro is null  ");
		}
			sqlStr.append(" AND d.dataentrega >= '").append(Uteis.getDataBD0000(assinarDocumentoEntregueVO.getDataInicioEntrega())).append("' ");
			if(Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getDataFimEntrega())) {
				sqlStr.append(" AND d.dataentrega <= '").append(Uteis.getDataBD2359(assinarDocumentoEntregueVO.getDataFimEntrega())).append("' ");
			}
		sqlStr.append(" AND a.origem ilike 'DM%' ");
		sqlStr.append(" AND a.servidorarquivoonline = '").append(assinarDocumentoEntregueVO.getServidorArquivo()).append("' ");			
		sqlStr.append("	group by m.aluno, d.tipodedocumento, d.arquivo, coalesce(d.arquivoverso, 0) ");
		sqlStr.append("	order by 4 ");
		if(assinarDocumentoEntregueVO.getQtdProcessarLote() != null && assinarDocumentoEntregueVO.getQtdProcessarLote() > 0) {
			sqlStr.append(" limit   ").append(assinarDocumentoEntregueVO.getQtdProcessarLote());	
		} else {
			sqlStr.append(" limit   100 ");
		}
		sqlStr.append(" ) as dm on dm.matricula = matricula.matricula and dm.tipodedocumento = documetacaomatricula.tipodedocumento ");
		sqlStr.append(" where arquivo.arquivoAssinadoFuncionario = false and arquivo.arquivoAssinadoUnidadeEnsino = false and arquivo.arquivoAssinadoUnidadeCertificadora = false ");
		sqlStr.append(" AND documetacaomatricula.dataentrega >= '").append(Uteis.getDataBD0000(assinarDocumentoEntregueVO.getDataInicioEntrega())).append("' ");
		if(Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getDataFimEntrega())) {
			sqlStr.append(" AND documetacaomatricula.dataentrega <= '").append(Uteis.getDataBD2359(assinarDocumentoEntregueVO.getDataFimEntrega())).append("' ");
		}
		sqlStr.append(" AND documetacaomatricula.entregue = true ");
		sqlStr.append(" AND documetacaomatricula.situacao = 'OK' ");
		if(assinarDocumentoEntregueVO.getConsiderarDocumentosProcessadorComErro()) {
			sqlStr.append(" and (documetacaomatricula.processadocomerro = true or documetacaomatricula.processadocomerro is null)");
		}else {
			sqlStr.append(" AND documetacaomatricula.processadocomerro is null  ");
		}
		if(Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getCodigoUnidadeEnsino())) {
			sqlStr.append(" AND matricula.unidadeensino =  ").append(assinarDocumentoEntregueVO.getCodigoUnidadeEnsino()).append(" ");
		}
		sqlStr.append(" AND arquivo.origem ilike 'DM%' ");
		sqlStr.append(" AND arquivo.servidorarquivoonline = '").append(assinarDocumentoEntregueVO.getServidorArquivo()).append("' ");
		
		sqlStr.append(" order by documetacaomatricula.matricula ");
		
		if(assinarDocumentoEntregueVO.getQtdProcessarLote() != null && assinarDocumentoEntregueVO.getQtdProcessarLote() > 0) {
			sqlStr.append(" limit   ").append(assinarDocumentoEntregueVO.getQtdProcessarLote());	
		} else {
			sqlStr.append(" limit   100 ");
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<DocumetacaoMatriculaVO> listaArquivoVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
		while (dadosSQL.next()) {		
			listaArquivoVOs.add(montarDados(dadosSQL, nivelMontarDados, usuario));
		}
		return listaArquivoVOs;
	}
	
	
	public File unificarFrenteVersoDocumentoMatricula(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception {
		
		if(!obj.getArquivoVO().getIsImagem().booleanValue() && !obj.getArquivoVO().getIsPdF().booleanValue()){
			throw new Exception("Formato de Documento Inválido.(Formatos Permitidos: JPG/PNG e PDF)");
		}
		boolean teste =  false;
		if(teste) {
			File fileOrigem = new File("C:\\Desenvolvimento\\Apache24\\htdocs\\upload\\arquivo\\file.pdf");
			obj.setRespAprovacaoDocDep(usuarioVO);
			obj.getArquivoVO().setPastaBaseArquivo(obj.getArquivoVO().getPastaBaseArquivo().replaceAll(" ", ""));
			obj.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
			if(!obj.getArquivoVO().getNome().endsWith(".pdf")) {
				obj.getArquivoVO().setNome(obj.getArquivoVO().getNome().substring(0, obj.getArquivoVO().getNome().lastIndexOf("."))+".pdf");				
				obj.getArquivoVO().setExtensao(".pdf");
			}
			File arquivoFrente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+obj.getArquivoVO().getPastaBaseArquivo());
			if(!arquivoFrente.exists()) {
				arquivoFrente.mkdirs();
			}
			arquivoFrente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+obj.getArquivoVO().getPastaBaseArquivo()+File.separator+obj.getArquivoVO().getNome());
			if(!arquivoFrente.exists()) {				
				arquivoFrente.createNewFile();
				getFacadeFactory().getArquivoHelper().copiar(fileOrigem, arquivoFrente);
			}
		
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso())){
				obj.getArquivoVOVerso().setPastaBaseArquivo(obj.getArquivoVOVerso().getPastaBaseArquivo().replaceAll(" ", ""));
				if(!obj.getArquivoVOVerso().getNome().endsWith(".pdf")) {
					obj.getArquivoVOVerso().setNome(obj.getArquivoVOVerso().getNome().substring(0, obj.getArquivoVOVerso().getNome().lastIndexOf("."))+".pdf");
					obj.getArquivoVOVerso().setExtensao(".pdf");
				}
				obj.getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
				File arquivoVerso = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+obj.getArquivoVOVerso().getPastaBaseArquivo());
				if(!arquivoVerso.exists()) {
					arquivoVerso.mkdirs();
				}
				arquivoVerso = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+obj.getArquivoVOVerso().getPastaBaseArquivo()+File.separator+obj.getArquivoVOVerso().getNome());
				if(!arquivoVerso.exists()) {
					arquivoVerso.createNewFile();
					getFacadeFactory().getArquivoHelper().copiar(fileOrigem, arquivoVerso);
				}
			}
//			String caminhoTemporario = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj.getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue() , configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
//			File fileDestino = new File(caminhoTemporario + File.separator + obj.getMatricula()+"_"+usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf");
//			getFacadeFactory().getArquivoHelper().copiar(fileOrigem, fileDestino);
//			return fileDestino;
		}
		
		if(obj.getArquivoVO().getIsImagem()) {
			return unificarFrenteVersoDocumentoIMG(obj, configuracaoGeralSistemaVO,usuarioVO);
		}else {
			return unificarFrenteVersoDocumentoPDF(obj, configuracaoGeralSistemaVO, usuarioVO);
		}
		
	}
	
	private File unificarFrenteVersoDocumentoIMG(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception {
		List<String> files = new ArrayList<String>();
		String caminhoPdf ="";	
		verificarEspacoCaminhoArquivo(obj);
		if(obj.getArquivoVO().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
			String caminhoTemporario = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj.getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue() , configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
			caminhoPdf = caminhoTemporario + File.separator + obj.getMatricula()+"_"+usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
			
			files.add(realizarDownloadArquivoAmazon(obj.getArquivoVO(), caminhoTemporario,configuracaoGeralSistemaVO));
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso())) {
				files.add(realizarDownloadArquivoAmazon(obj.getArquivoVOVerso(), caminhoTemporario,configuracaoGeralSistemaVO));
			}
		}else {
			String caminhoFrente = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo()+File.separator +obj.getArquivoVO().getNome();
			String caminhoVerso = Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getNome()) ? configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+obj.getArquivoVOVerso().getPastaBaseArquivo() +File.separator + obj.getArquivoVOVerso().getNome() : "";
			
			files.add(caminhoFrente);
			if(Uteis.isAtributoPreenchido(caminhoVerso)) {
				files.add(caminhoVerso);
			}
			String nomeNovoArquivo = obj.getMatricula()+"_"+usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
			String caminhoBasePdf = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo() + File.separator;  
			caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
		}
		ConverterImgToPdf.realizarConversaoPdf(files, caminhoPdf);
		return new File(caminhoPdf);
	}

	private void verificarEspacoCaminhoArquivo(DocumetacaoMatriculaVO obj) {
		if(obj.getArquivoVO().getOrigem().contains("DM  ")) {
			obj.getArquivoVO().setOrigem(obj.getArquivoVO().getOrigem().replace("DM  ", "DM"));
		}
	}

	private File unificarFrenteVersoDocumentoPDF(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception {
		List<File> files = new ArrayList<File>(0);
		String caminhoPdf = "";
		verificarEspacoCaminhoArquivo(obj);
		if(obj.getArquivoVO().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
			String caminhoTemporario = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj.getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue() , configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
			caminhoPdf = caminhoTemporario + File.separator + obj.getMatricula()+"_"+usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
			
			files.add(new File(realizarDownloadArquivoAmazon(obj.getArquivoVO(), caminhoTemporario,configuracaoGeralSistemaVO)));
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso())) {
				files.add(new File(realizarDownloadArquivoAmazon(obj.getArquivoVOVerso(), caminhoTemporario,configuracaoGeralSistemaVO)));
			}
			
		}else {
			files.add(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo()+File.separator +obj.getArquivoVO().getNome()));
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getNome())) {
				files.add(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator + obj.getArquivoVOVerso().getPastaBaseArquivo() +File.separator + obj.getArquivoVOVerso().getNome()));
			}
			
			String nomeNovoArquivo = obj.getMatricula().replace("/", "")+"_"+usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
			String caminhoBasePdf =configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoVO().getPastaBaseArquivo() + File.separator;  
			caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
		}
		UnificadorPDF.realizarUnificacaoListaPdf(files, caminhoPdf);
		// ao realizar a unificação dos arquivos, caso algum deles ou os dois sejam do tipo PDF/A vai ser marcados que eles não são PDF/A
		// porque quando realiza a unificação o PDF perde a sua conformidade, sendo assim precisando depois realizar a conversão
		obj.getArquivoVO().setArquivoIsPdfa(Boolean.FALSE);
		obj.getArquivoVOVerso().setArquivoIsPdfa(Boolean.FALSE);
		return new File(caminhoPdf);
	}
	
	
	public String  realizarDownloadArquivoAmazon(ArquivoVO obj, String caminhoTemporario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws IOException, ConsistirException {
		
		File file = new File(caminhoTemporario);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		String nomeArquivo = caminhoTemporario + File.separator +  obj.getNome();
		file = new File (nomeArquivo);
		if(!file.exists()) {
			file.createNewFile();
		}
		
		
		getFacadeFactory().getArquivoHelper().copiarArquivoDaAmazonS3ParaServidorLocal(obj.getPastaBaseArquivo()+ "/" +obj.getDescricao(),nomeArquivo, configuracaoGeralSistemaVO);
		return nomeArquivo;
	}
	
			
	public void validarExtensaoArquivoFrente(String fileName, DocumetacaoMatriculaVO documetacaoMatriculaVO) throws Exception {
		boolean isImage = isImagem(fileName);
		boolean isPDF = isPDF(fileName);
		if(isImage) {
			if(Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOVerso().getNome())) {
				if(!documetacaoMatriculaVO.getArquivoVOVerso().getIsImagem()) {
					 throw new Exception("O Arquivo Frente não possui a mesma extensão do Arquivo Verso"); 
				}
			}
		}
		if(isPDF) {
			if(Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOVerso().getNome())) {
				if(!documetacaoMatriculaVO.getArquivoVOVerso().getIsPdF()) {
					 throw new Exception("O Arquivo Frente não possui a mesma extensão do Arquivo Verso"); 
				}
			}
		}
	}
	
	
	public void validarExtensaoArquivoVerso(String fileName, DocumetacaoMatriculaVO documetacaoMatriculaVO) throws Exception {
		boolean isImage = isImagem(fileName);
		boolean isPDF = isPDF(fileName);
		if(isImage) {
			if(Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO().getNome())) {
				if(!documetacaoMatriculaVO.getArquivoVO().getIsImagem()) {
					throw new Exception("O Arquivo Verso não possui a mesma extensão do Arquivo Frente"); 
				}
			}
		}
		if(isPDF) {
			if(Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO().getNome())) {
				if(!documetacaoMatriculaVO.getArquivoVO().getIsPdF()) {
					 throw new Exception("O Arquivo Verso não possui a mesma extensão do Arquivo Frente"); 
				}
			}
		}
	}

	public Boolean isPDF(String name) {
		if (name.endsWith(".pdf") || name.endsWith(".PDF")) {
			return true;
		}
		return false;
	}

	private boolean isImagem(String name) {
		if (name.endsWith(".jpeg") || name.endsWith(".JPEG") || name.endsWith(".jpg")
		        || name.endsWith(".JPG") || name.endsWith(".png") || name.endsWith(".PNG")
		        || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".bmp")
		        || name.endsWith(".BMP") || name.endsWith(".ico") || name.endsWith(".ICO")) {
			return true;
		}
		return false;
	}
	
	@Override
	public void verificarDocumentosObrigatoriosACadaRenovacao(MatriculaVO matriculaVO , String ano , String semestre) throws Exception{
		
		
		List<DocumetacaoMatriculaVO> listaDocumentoMatriculaRenovacao = new ArrayList<DocumetacaoMatriculaVO>();
		
		for (Iterator iterator = matriculaVO.getDocumetacaoMatriculaVOs().iterator(); iterator.hasNext();) {
			DocumetacaoMatriculaVO documentacaoMatricula = (DocumetacaoMatriculaVO) iterator.next();
			
			if (documentacaoMatricula.getDocumentacaoCursoExistente() && documentacaoMatricula.getEntregue()  && documentacaoMatricula.getTipoDeDocumentoVO().getRenovacao() &&
					!Uteis.isAtributoPreenchido(documentacaoMatricula.getAno())) {
				
				DocumetacaoMatriculaVO documentoMatriculaRenovacao = new DocumetacaoMatriculaVO();
				
				documentoMatriculaRenovacao.setMatricula(matriculaVO.getMatricula());
				documentoMatriculaRenovacao.setSituacao("PE");
				documentoMatriculaRenovacao.setEntregue(false);
				documentoMatriculaRenovacao.setTipoDeDocumentoVO(documentacaoMatricula.getTipoDeDocumentoVO());
				documentoMatriculaRenovacao.setGerarSuspensaoMatricula(documentacaoMatricula.getGerarSuspensaoMatricula());
				documentoMatriculaRenovacao.setEntreguePorEquivalencia(documentacaoMatricula.getEntreguePorEquivalencia());
				documentoMatriculaRenovacao.setAno(ano);
				if (Uteis.isAtributoPreenchido(semestre)) {
					documentoMatriculaRenovacao.setSemestre(semestre);
				}
				listaDocumentoMatriculaRenovacao.add(documentoMatriculaRenovacao);
				
			}
			
		}
		
		if (!listaDocumentoMatriculaRenovacao.isEmpty()) {
			try {
			for(DocumetacaoMatriculaVO documentoMatriculaRenovacao: listaDocumentoMatriculaRenovacao) {
				matriculaVO.adicionarObjDocumetacaoMatriculaVOs(documentoMatriculaRenovacao);
			}			
			listaDocumentoMatriculaRenovacao.clear();
			Ordenacao.ordenarLista(matriculaVO.getDocumetacaoMatriculaVOs(), "tipoDeDocumentoVO.nome");
			}catch (Exception e) {
				e.printStackTrace();;
			}
		}
		
	}
	
	@Override
	public void gravarDocumentacaoMatriculaVisaoAluno(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs, MatriculaVO matriculaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO ,Boolean validarExclusaoDocumentosAluno) throws Exception {
		Iterator k = documetacaoMatriculaVOs.iterator();
		while (k.hasNext()) {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) k.next();
			if (obj.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno().booleanValue()) {
				if (obj.getArquivoVO().getCodigo().intValue() != 0 && (!obj.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && (obj.getTipoDeDocumentoVO().getDocumentoFrenteVerso().booleanValue() && obj.getArquivoVOVerso().getCodigo().intValue() != 0))) {
					obj.setUsuario(usuarioVO);
					obj.setEntregue(Boolean.TRUE);
					if (!obj.getTipoDeDocumentoVO().getTipoDocumentoEquivalenteVOs().isEmpty()) {
						Iterator j = obj.getTipoDeDocumentoVO().getTipoDocumentoEquivalenteVOs().iterator();
						while (j.hasNext()) {
							TipoDocumentoEquivalenteVO tipo = (TipoDocumentoEquivalenteVO) j.next();
							Iterator i = documetacaoMatriculaVOs.iterator();
							while (i.hasNext()) {
								DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO) i.next();
								if (tipo.getTipoDocumentoEquivalente().getCodigo().intValue() == doc.getTipoDeDocumentoVO().getCodigo().intValue()) {										
									if (obj.getEntregue()) {
										if (!doc.getEntregue()) {
											doc.setEntregue(Boolean.TRUE);
											doc.setDataEntrega(new Date());
											doc.setEntreguePorEquivalencia(Boolean.TRUE);
										} else {
											if (doc.getEntreguePorEquivalencia()) {
												doc.setEntregue(Boolean.FALSE);
												doc.setDataEntrega(null);
												doc.setEntreguePorEquivalencia(Boolean.FALSE);
											}
										}
									} else {
										if (doc.getEntreguePorEquivalencia()) {
											doc.setEntregue(Boolean.FALSE);
											doc.setDataEntrega(null);
											doc.setEntreguePorEquivalencia(Boolean.FALSE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		matriculaVO.setDocumetacaoMatriculaVOs(documetacaoMatriculaVOs);
		getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumetacaoMatriculas(matriculaVO, usuarioVO, configuracaoGeralSistemaVO, validarExclusaoDocumentosAluno);
		getFacadeFactory().getMatriculaFacade().alterarMatriculaSuspensaoDocumentacaoMatricula(matriculaVO);
		getFacadeFactory().getMatriculaFacade().realizarEnvioMensagemConfirmacaoMatriculaValidandoRegrasEntregaDocumentacao(matriculaVO,  usuarioVO);
		//getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(matriculaVO.getMatricula() ,  getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(),usuarioVO) , usuarioVO);
	}
	
	@Override
	public void removerProfessorListaAssinaturaDigital(ArquivoVO arquivoVO,FuncionarioVO obj, UsuarioVO usuarioLogado) throws Exception {
	  	int index = 0;
    	for (FuncionarioVO funcionarioVO : arquivoVO.getListaFuncionarioAssinarDigitalmenteVOs()) {
			if (funcionarioVO.getCodigo().equals(obj.getCodigo())) {
				arquivoVO.getListaFuncionarioAssinarDigitalmenteVOs().remove(index);
				break;
			}
			index++;
		}
    	if(Uteis.isAtributoPreenchido(arquivoVO)) {
    		DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorArquivoEPessoaDocumentoAssinado(obj.getPessoa(), arquivoVO, false, usuarioLogado);
        	if(Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getDocumentoAssinadoVO().getCodigo())){
        		getFacadeFactory().getDocumentoAssinadoPessoaFacade().excluir(documentoAssinadoPessoaVO, usuarioLogado);
        	}
        	
//        	if(!Uteis.isAtributoPreenchido(arquivoVO.getListaFuncionarioAssinarDigitalmenteVOs())) {
//        		DocumentoAssinadoVO documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorArquivo(arquivoVO.getCodigo(), usuarioLogado);
//        		getFacadeFactory().getDocumentoAssinadoFacade().excluir(documentoAssinadoVO, false, usuarioLogado, configuracaoGeralSistemaVO);
//        	}
    	}
	}
	
	public static void montarDadosRespAprovacaoDocDep(DocumetacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getRespAprovacaoDocDep())) {
			obj.setRespAprovacaoDocDep(new UsuarioVO());
			return;
		}
		obj.setRespAprovacaoDocDep(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getRespAprovacaoDocDep().getCodigo(), nivelMontarDados, usuario));
	}
	
	@Override
	public  void realizarPreencherCaminhoAnexoImagemDocumentacaoMatricula(List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO2)  throws Exception {
		for(DocumetacaoMatriculaVO documento : listaDocumentacaoMatriculaVO) {
			if(Uteis.isAtributoPreenchido(documento.getArquivoVO().getPastaBaseArquivo()) && Uteis.isAtributoPreenchido(documento.getArquivoVO().getNome() )) {
				String caminhoImagemAnexo = configuracaoGeralSistemaVO2.getUrlExternoDownloadArquivo()+ File.separator+documento.getArquivoVO().getPastaBaseArquivo()  +  File.separator+documento.getArquivoVO().getNome();
				documento.getArquivoVO().setCaminhoImagemAnexo(caminhoImagemAnexo);
			}
			if(Uteis.isAtributoPreenchido(documento.getArquivoVOVerso().getPastaBaseArquivo()) && Uteis.isAtributoPreenchido(documento.getArquivoVOVerso().getNome() )) {
				String caminhoImagemAnexoVerso = configuracaoGeralSistemaVO2.getUrlExternoDownloadArquivo()+ File.separator+documento.getArquivoVOVerso().getPastaBaseArquivo()  +  File.separator+documento.getArquivoVOVerso().getNome();				
				documento.getArquivoVOVerso().setCaminhoImagemAnexo(caminhoImagemAnexoVerso);
			}
			
			if(Uteis.isAtributoPreenchido(documento.getArquivoVOAssinado().getPastaBaseArquivo()) && Uteis.isAtributoPreenchido(documento.getArquivoVOAssinado().getNome() )) {
				String caminhoImagemAnexoAssinado = configuracaoGeralSistemaVO2.getUrlExternoDownloadArquivo()+ File.separator+documento.getArquivoVOAssinado().getPastaBaseArquivo()  +  File.separator+documento.getArquivoVOAssinado().getNome();				
				documento.getArquivoVOAssinado().setCaminhoImagemAnexo(caminhoImagemAnexoAssinado);
			}
    		
    		
		}
		
	}
	
	@Override
	public void realizarUploadArquivo(FileUploadEvent uploadEvent, FileItem fileItem, DocumetacaoMatriculaVO documetacaoMatriculaVO, PessoaVO aluno, boolean frente, boolean ged, DocumentacaoGEDVO documentacaoGEDVO, UsuarioVO usuarioVO, String ambiente) throws Exception {
		ByteArrayInputStream inpuStream = null;
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO);
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaUnidadeEnsino =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(documetacaoMatriculaVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), usuarioVO);
		getFacadeFactory().getMatriculaFacade().validarExtensaoArquivo(uploadEvent != null ? uploadEvent.getUploadedFile().getName() : fileItem.getName(), documetacaoMatriculaVO, ged);
		if(frente) {
			getFacadeFactory().getDocumetacaoMatriculaFacade().validarExtensaoArquivoFrente(uploadEvent != null ? uploadEvent.getUploadedFile().getName() : fileItem.getName(), documetacaoMatriculaVO);
		}else if(!ged) {
			getFacadeFactory().getDocumetacaoMatriculaFacade().validarExtensaoArquivoVerso(uploadEvent != null ? uploadEvent.getUploadedFile().getName() : fileItem.getName(), documetacaoMatriculaVO);
		}
		if (uploadEvent != null) {
			String nomeArquivo = uploadEvent.getUploadedFile().getName();
			if (isPDF(nomeArquivo)) {
				inpuStream = ArquivoHelper.criarByteArrayOutputStream(uploadEvent.getUploadedFile().getInputStream());
				if (Objects.nonNull(inpuStream)) {
					try {
						inpuStream.mark(Integer.MAX_VALUE);
						ArquivoHelper.validarArquivoPdfCriptografado(inpuStream, false);
						inpuStream.reset();
					} catch (Exception e) {
						if (Objects.nonNull(inpuStream)) {
							inpuStream.close();
							inpuStream = null;
						}
						throw e;
					}
				}
				
				getFacadeFactory().getArquivoHelper().validarConformidadeArquivoPdf(frente ? documetacaoMatriculaVO.getArquivoVO() : documetacaoMatriculaVO.getArquivoVOVerso(), inpuStream);
			}
		} else if (fileItem != null) {
			String nomeArquivo = fileItem.getName();
			if (isPDF(nomeArquivo)) {
				inpuStream = ArquivoHelper.criarByteArrayOutputStream(fileItem.getInputStream());
				if (Objects.nonNull(inpuStream)) {
					try {
						inpuStream.mark(Integer.MAX_VALUE);
						ArquivoHelper.validarArquivoPdfCriptografado(inpuStream, false);
						inpuStream.reset();
					} finally {
						if (Objects.nonNull(inpuStream)) {
							inpuStream.close();
							inpuStream = null;
						}
					}
				}
				
				getFacadeFactory().getArquivoHelper().validarConformidadeArquivoPdf(frente ? documetacaoMatriculaVO.getArquivoVO() : documetacaoMatriculaVO.getArquivoVOVerso(), inpuStream);
			}
		}
		try {
			if(ged) {
				documentacaoGEDVO.getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(configuracaoGeralSistemaVO.getServidorArquivoOnline()));
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, Objects.nonNull(inpuStream) ? inpuStream : uploadEvent.getUploadedFile().getInputStream(), documentacaoGEDVO.getArquivo(), configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP, usuarioVO);
				documentacaoGEDVO.setUploadNovoArquivo(true);
			}else {
				documetacaoMatriculaVO.getUsuario().setCodigo(usuarioVO.getCodigo());
				documetacaoMatriculaVO.getUsuario().setNome(usuarioVO.getNome());	
				documetacaoMatriculaVO.setDataEntrega(new Date());
				documetacaoMatriculaVO.setLocalUpload(ambiente);
				if (!configuracaoGeralSistemaUnidadeEnsino.getControlaAprovacaoDocEntregue()) {
					documetacaoMatriculaVO.setArquivoAprovadoPeloDep(Boolean.TRUE);
					documetacaoMatriculaVO.getRespAprovacaoDocDep().setCodigo(usuarioVO.getCodigo());
					documetacaoMatriculaVO.getRespAprovacaoDocDep().setNome(usuarioVO.getNome());
					documetacaoMatriculaVO.setEntregue(true);
				} else {
					documetacaoMatriculaVO.setArquivoAprovadoPeloDep(Boolean.FALSE);
					documetacaoMatriculaVO.setRespAprovacaoDocDep(null);
					documetacaoMatriculaVO.setEntregue(false);
				}			
				if(!documetacaoMatriculaVO.getEntregue()) {
					documetacaoMatriculaVO.setDataNegarDocDep(null);
					documetacaoMatriculaVO.setRespNegarDocDep(null);
					documetacaoMatriculaVO.setJustificativaNegacao(null);
				}
				if(!frente) {
					documetacaoMatriculaVO.getArquivoVOVerso().setCpfAlunoDocumentacao(aluno.getCPF());
					if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO)) {
						documetacaoMatriculaVO.getArquivoVOVerso().setCodOrigem(documetacaoMatriculaVO.getCodigo());
					}
					documetacaoMatriculaVO.getArquivoVOVerso().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
					documetacaoMatriculaVO.getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
					documetacaoMatriculaVO.getArquivoVOVerso().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
					if(uploadEvent != null) {
						getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent, Objects.nonNull(inpuStream) ? inpuStream : uploadEvent.getUploadedFile().getInputStream(), documetacaoMatriculaVO.getArquivoVOVerso(), documetacaoMatriculaVO.getArquivoVOVerso().getCpfAlunoDocumentacao() + documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome(), configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DOCUMENTOS_TMP, usuarioVO);
					}else {
						getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(fileItem, documetacaoMatriculaVO.getArquivoVOVerso(), documetacaoMatriculaVO.getArquivoVOVerso().getCpfAlunoDocumentacao() + documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome(),configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DOCUMENTOS_TMP, usuarioVO);
					}
					documetacaoMatriculaVO.getArquivoVOVerso().setPastaBaseArquivoWeb(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(documetacaoMatriculaVO.getArquivoVOVerso(), documetacaoMatriculaVO.getArquivoVOVerso().getPastaBaseArquivoEnum(), configuracaoGeralSistemaVO));
				}else {
					if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO)) {
						documetacaoMatriculaVO.getArquivoVO().setCodOrigem(documetacaoMatriculaVO.getCodigo());
					}			
					documetacaoMatriculaVO.getArquivoVO().setCpfAlunoDocumentacao(aluno.getCPF());
					documetacaoMatriculaVO.getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
					documetacaoMatriculaVO.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
					documetacaoMatriculaVO.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
					if(uploadEvent != null) {
						getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent, Objects.nonNull(inpuStream) ? inpuStream : uploadEvent.getUploadedFile().getInputStream(), documetacaoMatriculaVO.getArquivoVO(), documetacaoMatriculaVO.getArquivoVO().getCpfAlunoDocumentacao() + documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome(), configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DOCUMENTOS_TMP, usuarioVO);
					}else {
						getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(fileItem, documetacaoMatriculaVO.getArquivoVO(), documetacaoMatriculaVO.getArquivoVO().getCpfAlunoDocumentacao() + documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome(),configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DOCUMENTOS_TMP, usuarioVO);
					}
					documetacaoMatriculaVO.getArquivoVO().setPastaBaseArquivoWeb(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(documetacaoMatriculaVO.getArquivoVO(), documetacaoMatriculaVO.getArquivoVO().getPastaBaseArquivoEnum(), configuracaoGeralSistemaVO));
				}
			}
		} catch (Exception e) {
			if (frente) {
				documetacaoMatriculaVO.getArquivoVO().setArquivoIsPdfa(null);
			} else {
				documetacaoMatriculaVO.getArquivoVOVerso().setArquivoIsPdfa(null);
			}
			throw e;
		} finally {
			if (Objects.nonNull(inpuStream)) {
				inpuStream.close();
				inpuStream = null;
			}
		}
	} 

	private static void montarDadosMotivoIndeferimentoDocumentoAluno(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getMotivoIndeferimentoDocumentoAlunoVO().getCodigo())) {
			getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().carregarMotivoIndeferimentoDocumentoAluno(obj.getMotivoIndeferimentoDocumentoAlunoVO(), false, usuario);
		}
	}

	private static void montarDadosUsuarioRespNegarDocumento(DocumetacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getRespNegarDocDep().getCodigo())) {
			obj.setRespNegarDocDep(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getRespNegarDocDep().getCodigo(), nivelMontarDados, usuario));
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoMotivoIndeferimentoDocumentoAluno(final DocumetacaoMatriculaVO documetacaoMatriculaVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO) && Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getMotivoIndeferimentoDocumentoAlunoVO())) {
			String sql = "UPDATE DocumetacaoMatricula set motivoindeferimentodocumentoaluno = null  WHERE  (codigo = ("+documetacaoMatriculaVO.getCodigo()+")) "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql);
			documetacaoMatriculaVO.setMotivoIndeferimentoDocumentoAlunoVO(null);

			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(MatriculaVO matricula, DocumetacaoMatriculaVO doc, UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.ENTREGA_DOCUMENTO_PERMITE_UPLOAD_DEFERIMENTO_INDEFERIMENTO_DOCUMENTO_CALOURODENTROPRAZOCHAMADAPROCESSOSELETIVO.getValor(), usuario);
			} catch (Exception e) {
			return ;
		}
		String mensagem ="Usuário " + usuario.getNome().toUpperCase()+" não possui permissão para realizar Upload Documento Indeferido/Não Entregue ou Aprovar/Negar documentação fora do prazo do período de chamada ou fora período de upload de documentação Documento Indeferido do processo seletivo.";
        Uteis.checkState(getFacadeFactory().getDocumetacaoMatriculaFacade().verificarBloqueioAprovacaoDocumentoMatriculaCalouroDeAcordoComPeriodoChamadaDoProcessoSeletivo(matricula.getMatricula(), false, false ,usuario), mensagem);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean verificarBloqueioAprovacaoDocumentoMatriculaCalouroDeAcordoComPeriodoChamadaDoProcessoSeletivo(String matricula, Boolean documentoIndeferido ,Boolean validarVisaoAluno, UsuarioVO usuario) throws Exception {	
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("  select mat.matricula  from	matricula mat ");		
		sqlStr.append("  inner join inscricao on inscricao.codigo = mat.inscricao ");		
		sqlStr.append("  inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");		
		sqlStr.append("  inner join periodochamadaprocseletivo pcps on pcps.procseletivo = procseletivo.codigo  and pcps.nrchamada = inscricao.chamada ");		
		sqlStr.append("  INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = mat.matricula ");
		sqlStr.append("  order by mp.ano || mp.semestre desc, mp.codigo desc limit 1) ");
		sqlStr.append("  where mat.matricula = ?  ");		
		// regra calouro 
		sqlStr.append("  and ( select count(mp.codigo) from matriculaPeriodo  mp where mp.matricula = ? ");		
		sqlStr.append("  and ((mp.situacaoMatriculaPeriodo = 'PR')  or (mp.situacaoMatriculaPeriodo = 'AT') or (mp.situacaoMatriculaPeriodo = 'FI') or (mp.situacaoMatriculaPeriodo = 'TR'))) <= 1 ");	
		// periodo chamada ou upload doc indeferido 	
		
		if(!Uteis.isAtributoPreenchido(validarVisaoAluno) || !validarVisaoAluno) {
			sqlStr.append(" and ( pcps.periodoinicialchamada > now()  or  (pcps.periodofinalchamada <= now()  ");
			sqlStr.append(" and ( ( pcps.periodoinicialuploaddocumentoindeferido is null  or pcps.periodoinicialuploaddocumentoindeferido > now() ) ");
			sqlStr.append("       or (pcps.periodofinaluploaddocumentoindeferido  is null or pcps.periodofinaluploaddocumentoindeferido <= now()) ))) ");
		}else {			
			sqlStr.append(" and ( pcps.periodoinicialchamada > now()  or   pcps.periodofinalchamada <= now() ) ");
			if(documentoIndeferido != null && documentoIndeferido) {			
				sqlStr.append(" and ( ( pcps.periodoinicialuploaddocumentoindeferido is null  or pcps.periodoinicialuploaddocumentoindeferido > now() ) ");
				sqlStr.append(" or (pcps.periodofinaluploaddocumentoindeferido  is null or pcps.periodofinaluploaddocumentoindeferido <= now()) ) ");
			}			
		}
		sqlStr.append(" and matriculaperiodo.situacao = 'PR' ");		
		
		
			
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString() ,matricula,matricula);		
		if (dadosSQL.next()) {
			return true;
		}
		return false;		
	}
	
	@Override
	public List<DocumetacaoMatriculaVO> consultarPorTipoDeDocumentoAluno(Integer codigoTipoDocumento, Integer aluno, int nivelmontardadosTodos, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSQLPadraoConsultaBasica());
		sql.append(" WHERE documetacaoMatricula.tipodedocumento = ? and matricula.aluno = ? and (documetacaoMatricula.arquivoAssinado is null or documetacaoMatricula.arquivoGed is null) ");
		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs =  new ArrayList<DocumetacaoMatriculaVO>(0);
		SqlRowSet tabelaResultado  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoTipoDocumento, aluno);
		while (tabelaResultado.next()) {
			DocumetacaoMatriculaVO documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
			montarDadosBasico(documetacaoMatriculaVO, tabelaResultado);
			documetacaoMatriculaVOs.add(documetacaoMatriculaVO);
		}
		return documetacaoMatriculaVOs;
	}
	
	/**
	 * Método para realizar a assinatura da documentação de matrícula de acordo com
	 * a configuração ged e realizar a preparação do preview do PDF/A
	 * 
	 * @param obj
	 * @param matricula
	 * @param arquivo
	 * @param configGEDVO
	 * @param usuario
	 * @throws Exception
	 * @author Felipi Alves
	 */
	private void realizarAssinaturaDocumentoAluno(DocumetacaoMatriculaVO obj, MatriculaVO matricula, ArquivoVO arquivo, ConfiguracaoGEDVO configGEDVO, UsuarioVO usuario) throws Exception {
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario);
		File fileAssinado = getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDocumentacaoAlunoV2(matricula.getUnidadeEnsino(), arquivo, configGEDVO, obj.getFileAssinar(), configuracaoGeralSistemaVO, usuario, false, obj.getRealizarConversaoPdfAImagem(), obj.getValidarPdfA() ? Boolean.FALSE : Boolean.TRUE, obj.getIdDocumetacao());
		if (obj.getValidarPdfA()) {
			File fileDest = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + new Date().getTime() + Constantes.UNDERSCORE + arquivo.getNome());
			getFacadeFactory().getArquivoHelper().copiar(fileAssinado, fileDest);
			if (Uteis.isAtributoPreenchido(obj.getFileAssinar())) {
				obj.getFileAssinar().delete();
			}
			if (Uteis.isAtributoPreenchido(fileAssinado)) {
				fileAssinado.delete();
			}
			obj.setFileAssinar(fileDest);
			obj.setArquivoAssinado(arquivo);
			obj.setCaminhoPreviewPdfA((configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + fileDest.getName() + "?embedded=true").replace(File.separator, "/"));
		}
	}
	
	@Override
	public List<DocumetacaoMatriculaVO> consultarDocumentacoesMatriculasAssinadosParaCorrecaoPdfA(String matricula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT documetacaomatricula.codigo documetacaomatricula_codigo, documetacaomatricula.dataaprovacaodocdep documetacaomatricula_dataaprovacaodocdep, ");
		sql.append("tipodocumento.codigo tipodocumento_codigo, tipodocumento.nome tipodocumento_nome, ");
		sql.append("pessoarespaprovacaodocdep.codigo pessoarespaprovacaodocdep_codigo, pessoarespaprovacaodocdep.nome pessoarespaprovacaodocdep_nome, pessoarespaprovacaodocdep.cpf pessoarespaprovacaodocdep_cpf, pessoarespaprovacaodocdep.senhacertificadoparadocumento pessoarespaprovacaodocdep_senhacertificadoparadocumento, ");
		sql.append("arquivoassinado.codigo arquivoassinado_codigo, arquivoassinado.nome arquivoassinado_nome, arquivoassinado.descricao arquivoassinado_descricao, arquivoassinado.pastabasearquivo arquivoassinado_pastabasearquivo, arquivoassinado.servidorarquivoonline arquivoassinado_servidorarquivoonline, arquivoassinado.dataupload arquivoassinado_dataupload, arquivoassinado.origem arquivoassinado_origem, arquivoassinado.arquivoassinadofuncionario, arquivoassinado.arquivoassinadounidadeensino ");
		sql.append("FROM documetacaomatricula ");
		sql.append("INNER JOIN arquivo arquivoassinado ON arquivoassinado.codigo = documetacaomatricula.arquivoassinado ");
		sql.append("INNER JOIN tipodocumento ON tipodocumento.codigo = documetacaomatricula.tipodedocumento ");
		sql.append("LEFT JOIN usuario respaprovacaodocdep ON respaprovacaodocdep.codigo = documetacaomatricula.respaprovacaodocdep ");
		sql.append("LEFT JOIN pessoa pessoarespaprovacaodocdep ON pessoarespaprovacaodocdep.codigo = respaprovacaodocdep.pessoa ");
		sql.append("WHERE documetacaomatricula.matricula = ? ");
		sql.append("AND documetacaomatricula.entregue AND documetacaomatricula.situacao = 'OK' AND arquivoassinado.arquivoconvertidopdfa = FALSE ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			DocumetacaoMatriculaVO documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
			documetacaoMatriculaVO.setCodigo(tabelaResultado.getInt("documetacaomatricula_codigo"));
			documetacaoMatriculaVO.setDataAprovacaoDocDep(tabelaResultado.getDate("documetacaomatricula_dataaprovacaodocdep"));
			documetacaoMatriculaVO.getTipoDeDocumentoVO().setCodigo(tabelaResultado.getInt("tipodocumento_codigo"));
			documetacaoMatriculaVO.getTipoDeDocumentoVO().setNome(tabelaResultado.getString("tipodocumento_nome"));
			documetacaoMatriculaVO.getRespAprovacaoDocDep().getPessoa().setCodigo(tabelaResultado.getInt("pessoarespaprovacaodocdep_codigo"));
			documetacaoMatriculaVO.getRespAprovacaoDocDep().getPessoa().setNome(tabelaResultado.getString("pessoarespaprovacaodocdep_nome"));
			documetacaoMatriculaVO.getRespAprovacaoDocDep().getPessoa().setCPF(tabelaResultado.getString("pessoarespaprovacaodocdep_cpf"));
			documetacaoMatriculaVO.getRespAprovacaoDocDep().getPessoa().setSenhaCertificadoParaDocumento(tabelaResultado.getString("pessoarespaprovacaodocdep_senhacertificadoparadocumento"));
			documetacaoMatriculaVO.getArquivoAssinado().setCodigo(tabelaResultado.getInt("arquivoassinado_codigo"));
			documetacaoMatriculaVO.getArquivoAssinado().setNome(tabelaResultado.getString("arquivoassinado_nome"));
			documetacaoMatriculaVO.getArquivoAssinado().setDescricao(tabelaResultado.getString("arquivoassinado_descricao"));
			documetacaoMatriculaVO.getArquivoAssinado().setPastaBaseArquivo(tabelaResultado.getString("arquivoassinado_pastabasearquivo"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("arquivoassinado_servidorarquivoonline"))) {
				documetacaoMatriculaVO.getArquivoAssinado().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(tabelaResultado.getString("arquivoassinado_servidorarquivoonline")));
			}
			documetacaoMatriculaVO.getArquivoAssinado().setDataUpload(tabelaResultado.getDate("arquivoassinado_dataupload"));
			documetacaoMatriculaVO.getArquivoAssinado().setOrigem(tabelaResultado.getString("arquivoassinado_origem"));
			documetacaoMatriculaVO.getArquivoAssinado().setArquivoAssinadoFuncionario(tabelaResultado.getBoolean("arquivoassinadofuncionario"));
			documetacaoMatriculaVO.getArquivoAssinado().setArquivoAssinadoUnidadeEnsino(tabelaResultado.getBoolean("arquivoassinadounidadeensino"));
			documetacaoMatriculaVOs.add(documetacaoMatriculaVO);
		}
		return documetacaoMatriculaVOs;
	}
}
