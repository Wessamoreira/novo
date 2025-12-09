package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.ArtefatoEntregaEnum;
import negocio.comuns.ead.enumeradores.DefinicaoDataEntregaAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.PublicoAlvoAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.ead.AtividadeDiscursivaInterfaceFacade;



/**
 * @author Victor Hugo 17/09/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AtividadeDiscursiva extends ControleAcesso implements AtividadeDiscursivaInterfaceFacade, Serializable {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AtividadeDiscursiva.idEntidade = idEntidade;
	}

	public AtividadeDiscursiva() throws Exception {
		super();
		setIdEntidade("AtividadeDiscursiva");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(atividadeDiscursivaVO);
			realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(atividadeDiscursivaVO, usuarioVO);
			atividadeDiscursivaVO.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(atividadeDiscursivaVO.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			if(atividadeDiscursivaVO.getVincularNotaEspecifica()) {
				verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("VincularNotaEspecifica", usuarioVO);				
			}
			final String sql = "insert into atividadediscursiva(enunciado, turma, disciplina, dataliberacao, datalimiteentrega, ano, semestre, artefatoentrega, vincularnotaespecifica, notacorrespondente, responsavelcadastro, datacadastro, matriculaPeriodoTurmaDisciplina, publicoAlvo, qtdDiasAposInicioLiberar, qtddiasparaconclusao,definicaoDataEntregaAtividadeDiscursiva) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			atividadeDiscursivaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, atividadeDiscursivaVO.getEnunciado());
					sqlInserir.setInt(2, atividadeDiscursivaVO.getTurmaVO().getCodigo());
					sqlInserir.setInt(3, atividadeDiscursivaVO.getDisciplinaVO().getCodigo());
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getDataLiberacao())) {
						sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(atividadeDiscursivaVO.getDataLiberacao()));
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getDataLimiteEntrega())) {
						sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(atividadeDiscursivaVO.getDataLimiteEntrega()));
					} else {
						sqlInserir.setNull(5, 0);
					}

					sqlInserir.setString(6, atividadeDiscursivaVO.getAno());
					sqlInserir.setString(7, atividadeDiscursivaVO.getSemestre());
					sqlInserir.setString(8, atividadeDiscursivaVO.getArtefatoEntrega().toString());
					sqlInserir.setBoolean(9, atividadeDiscursivaVO.getVincularNotaEspecifica());
					sqlInserir.setString(10, atividadeDiscursivaVO.getNotaCorrespondente());
					sqlInserir.setInt(11, atividadeDiscursivaVO.getResponsavelCadastro().getCodigo());
					sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(atividadeDiscursivaVO.getDataCadastro()));
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO())) {
						sqlInserir.setInt(13, atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					} else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setString(14, atividadeDiscursivaVO.getPublicoAlvo().name());
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getQtdDiasAposInicioLiberar())) {
						sqlInserir.setInt(15, atividadeDiscursivaVO.getQtdDiasAposInicioLiberar());
					} else {
						sqlInserir.setNull(15, 0);
					}
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getQtdDiasParaConclusao())) {
						sqlInserir.setInt(16, atividadeDiscursivaVO.getQtdDiasParaConclusao());
					} else {
						sqlInserir.setNull(16, 0);
					}
					sqlInserir.setString(17, atividadeDiscursivaVO.getDefinicaoDataEntregaAtividadeDiscursivaEnum().toString());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			atividadeDiscursivaVO.setNovoObj(Boolean.TRUE);
			atividadeDiscursivaVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AtividadeDiscursivaVO obj) throws Exception {
		
		if (obj.getTurmaVO().getSemestral() || obj.getTurmaVO().getAnual()) {
			if (!Uteis.isAtributoPreenchido(obj.getAno()) || obj.getAno().trim().length() != 4) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_ano"));
			}
		}
		if (obj.getTurmaVO().getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_selecioneATurma"));
		}
		if (obj.getDisciplinaVO().getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_selecioneUmaDisciplina"));
		}
		
		if (obj.getPublicoAlvo().getIsTipoAluno()) {
			if (obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula().trim().isEmpty() && !Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_aluno"));
			}
			if (!Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_matriculaPeriodoTurmaDisciplina"));
			}
		}
		
		if (obj.getPublicoAlvo().getIsTipoTurma()) {
			validarPeriodicidadeTurmaAtividadeDiscursiva(obj);
			if (obj.getTurmaVO().getCodigo() == 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_selecioneATurma"));
			}
			if (obj.getDisciplinaVO().getCodigo() == 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_selecioneUmaDisciplina"));
			}
			if ((obj.getTurmaVO().getSemestral() || obj.getTurmaVO().getAnual()) && (!Uteis.isAtributoPreenchido(obj.getAno()) || obj.getAno().trim().length() != 4)) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_ano"));
			}
			if (obj.getTurmaVO().getSemestral() && !Uteis.isAtributoPreenchido(obj.getSemestre())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_semestre"));
			}
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getEnunciado())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_digiteOEnunciado"));
		}

		if (obj.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isDataFixa() && !Uteis.isAtributoPreenchido(obj.getDataLiberacao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_dataLiberacao"));
		}
		if (obj.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isDataFixa() && !Uteis.isAtributoPreenchido(obj.getDataLimiteEntrega())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_dataLimiteEntrega"));
		}
		if (obj.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isDataFixa() && UteisData.getCompareData(obj.getDataLiberacao(), obj.getDataLimiteEntrega()) == 1) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_dataLiberacaoMaiorDataLimiteEntrega"));
		}
		if (obj.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isDataFixa() && UteisData.getCompareData(obj.getDataLimiteEntrega(), obj.getDataLiberacao()) == -1) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_dataLimiteEntregaMenordataLiberacao"));
		}
		if (obj.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isInicioEstudoOnline() && !Uteis.isAtributoPreenchido(obj.getQtdDiasAposInicioLiberar())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_qtdDiasAposInicioLiberar"));
		}
		if (obj.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isInicioEstudoOnline() && !Uteis.isAtributoPreenchido(obj.getQtdDiasParaConclusao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_qtdDiasParaConclusao"));
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (usuarioVO.getIsApresentarVisaoProfessor()) {
			setIdEntidade("AtividadeDiscursivaProfessor");
		}
		if (atividadeDiscursivaVO.getCodigo().equals(0)) {
			incluir(atividadeDiscursivaVO, verificarAcesso, usuarioVO);
		} else {
			alterar(atividadeDiscursivaVO, verificarAcesso, usuarioVO);
		}
		getFacadeFactory().getArquivoFacade().excluirArquivoQueNaoEstaNaListaComCodigoOrigemComOrigemArquivo(atividadeDiscursivaVO.getListaMaterialDeApoio(), atividadeDiscursivaVO.getCodigo().toString(), OrigemArquivo.ATIVIDADE_DISCURSIVA, PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, configuracaoGeralSistema);
		if(Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getListaMaterialDeApoio())){
			getFacadeFactory().getArquivoFacade().incluirArquivos(atividadeDiscursivaVO.getListaMaterialDeApoio(), atividadeDiscursivaVO.getCodigo(), OrigemArquivo.ATIVIDADE_DISCURSIVA, usuarioVO, configuracaoGeralSistema);	
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			validarDados(atividadeDiscursivaVO);
			realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(atividadeDiscursivaVO, usuarioVO);
			realizarAlteracaoPeriodoAtividadeDiscursivaRespotaAlunoIniciadas(atividadeDiscursivaVO, usuarioVO);
			if(atividadeDiscursivaVO.getVincularNotaEspecifica()) {
				verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("VincularNotaEspecifica", usuarioVO);				
			}	
			final String sql = "UPDATE atividadediscursiva set enunciado=?, turma=?, disciplina=?, dataliberacao=?, datalimiteentrega=?, ano=?, semestre=?, artefatoentrega=?, vincularnotaespecifica=?, notacorrespondente=?, responsavelcadastro=?, datacadastro=?, matriculaPeriodoTurmaDisciplina=?, publicoAlvo=?, qtdDiasAposInicioLiberar=?, qtddiasparaconclusao=? , definicaoDataEntregaAtividadeDiscursiva=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, atividadeDiscursivaVO.getEnunciado());
					sqlAlterar.setInt(2, atividadeDiscursivaVO.getTurmaVO().getCodigo());
					sqlAlterar.setInt(3, atividadeDiscursivaVO.getDisciplinaVO().getCodigo());
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getDataLiberacao())) {
						sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(atividadeDiscursivaVO.getDataLiberacao()));
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getDataLimiteEntrega())) {
						sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(atividadeDiscursivaVO.getDataLimiteEntrega()));
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, atividadeDiscursivaVO.getAno());
					sqlAlterar.setString(7, atividadeDiscursivaVO.getSemestre());
					sqlAlterar.setString(8, atividadeDiscursivaVO.getArtefatoEntrega().toString());
					sqlAlterar.setBoolean(9, atividadeDiscursivaVO.getVincularNotaEspecifica());
					sqlAlterar.setString(10, atividadeDiscursivaVO.getNotaCorrespondente());
					sqlAlterar.setInt(11, atividadeDiscursivaVO.getResponsavelCadastro().getCodigo());
					sqlAlterar.setTimestamp(12, Uteis.getDataJDBCTimestamp(atividadeDiscursivaVO.getDataCadastro()));
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO())) {
						sqlAlterar.setInt(13, atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					} else {
						sqlAlterar.setNull(13, 0);
					}
					sqlAlterar.setString(14, atividadeDiscursivaVO.getPublicoAlvo().name());
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getQtdDiasAposInicioLiberar())) {
						sqlAlterar.setInt(15, atividadeDiscursivaVO.getQtdDiasAposInicioLiberar());
					} else {
						sqlAlterar.setNull(15, 0);
					}
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getQtdDiasParaConclusao())) {
						sqlAlterar.setInt(16, atividadeDiscursivaVO.getQtdDiasParaConclusao());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					sqlAlterar.setString(17, atividadeDiscursivaVO.getDefinicaoDataEntregaAtividadeDiscursivaEnum().toString());
					sqlAlterar.setInt(18, atividadeDiscursivaVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarAlteracaoPeriodoAtividadeDiscursivaRespotaAlunoIniciadas(AtividadeDiscursivaVO atividadeDiscursivaVO, UsuarioVO usuarioVO) throws Exception{		
//		if (atividadeDiscursivaVO.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isDataFixa()) {
//			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().atualizarPeriodoAtividadeDiscursivaRespostaAluno(atividadeDiscursivaVO, usuarioVO);
//			List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarAtividadeDiscursivaCodOrigem(atividadeDiscursivaVO.getCodigo(), TipoOrigemEnum.ATIVIDADE_DISCURSIVA ,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
//			for (CalendarioAtividadeMatriculaVO obj : calendarioAtividadeMatriculaVOs) {
//				obj.setDataInicio(atividadeDiscursivaVO.getDataLiberacao());
//				obj.setDataFim(atividadeDiscursivaVO.getDataLimiteEntrega());
//				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().alterarDataCalendarioAtividadeMatricula(obj, false, usuarioVO);
//			}
//		} else {
//			List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarAtividadeDiscursivaCodOrigem(atividadeDiscursivaVO.getCodigo(), TipoOrigemEnum.ATIVIDADE_DISCURSIVA ,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
//			for (CalendarioAtividadeMatriculaVO obj : calendarioAtividadeMatriculaVOs) {
//				AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO =  getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeRespostaAlunoPorCodAtividadediscursivaMatriculaperiodoturmadisciplina(obj.getCodOrigem(),obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo() ,usuarioVO);
//				CalendarioAtividadeMatriculaVO calendarioAcessoConteudo = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, TipoOrigemEnum.NENHUM, "", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
//				if (Uteis.isAtributoPreenchido(atividadeDiscursivaRespostaAlunoVO)) {					
//				   atividadeDiscursivaRespostaAlunoVO.setDataInicioAtividade(Uteis.obterDataAvancada(calendarioAcessoConteudo.getDataInicio(), atividadeDiscursivaVO.getQtdDiasAposInicioLiberar()));
//				   atividadeDiscursivaRespostaAlunoVO.setDataLimiteEntrega(Uteis.obterDataAvancada(calendarioAcessoConteudo.getDataInicio(), atividadeDiscursivaVO.getQtdDiasParaConclusao()));
//				   getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().atualizarPeriodoAtividadeDiscursivaRespostaAlunoPorCodigo(atividadeDiscursivaRespostaAlunoVO, usuarioVO);
//				}
//				obj.setDataInicio(Uteis.obterDataAvancada(calendarioAcessoConteudo.getDataInicio(), atividadeDiscursivaVO.getQtdDiasAposInicioLiberar()));
//				obj.setDataFim(Uteis.obterDataAvancada(calendarioAcessoConteudo.getDataInicio(), atividadeDiscursivaVO.getQtdDiasParaConclusao()));
//				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().alterarDataCalendarioAtividadeMatricula(obj, false, usuarioVO);
//			}
//		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			if (usuarioVO.getIsApresentarVisaoProfessor()) {
				setIdEntidade("AtividadeDiscursivaProfessor");
			}
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			atividadeDiscursivaVO.getListaMaterialDeApoio().clear();
			getFacadeFactory().getArquivoFacade().excluirArquivoQueNaoEstaNaListaComCodigoOrigemComOrigemArquivo(atividadeDiscursivaVO.getListaMaterialDeApoio(), atividadeDiscursivaVO.getCodigo().toString(), OrigemArquivo.ATIVIDADE_DISCURSIVA, PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, configuracaoGeralSistema);
			String sql = "DELETE FROM atividadediscursiva WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, atividadeDiscursivaVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void uploadMaterialApoio(FileUploadEvent upload, AtividadeDiscursivaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ArquivoVO arquivo = new ArquivoVO();
			arquivo.setNome(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(upload.getFile().getFileName())));
			arquivo.setExtensao(ArquivoHelper.getExtensaoArquivo(upload.getFile().getFileName()));
			arquivo.setDescricao(upload.getFile().getFileName());
			arquivo.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO_TMP);
			arquivo.setPastaBaseArquivo(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO_TMP.getValue());
			arquivo.setOrigem(OrigemArquivo.ATIVIDADE_DISCURSIVA.getValor());
			arquivo.setDataDisponibilizacao(new Date());
			arquivo.setDataUpload(new Date());
			arquivo.setDisciplina(obj.getDisciplinaVO());
			arquivo.setManterDisponibilizacao(true);
			arquivo.setProfessor(obj.getResponsavelCadastro().getPessoa());
			arquivo.setResponsavelUpload(obj.getResponsavelCadastro());
			arquivo.setSituacao(SituacaoArquivo.ATIVO.getValor());
			arquivo.setTurma(obj.getTurmaVO());
			arquivo.setUploadRealizado(true);
			ArquivoHelper.salvarArquivoNaPastaTemp(upload, arquivo.getNome(), arquivo.getPastaBaseArquivo(), configuracaoGeralSistemaVO, usuarioVO);
			for (ArquivoVO objExistente : obj.getListaMaterialDeApoio()) {
				if (objExistente.getNome().equals(arquivo.getNome())) {
					return;
				}
			}
			obj.getListaMaterialDeApoio().add(arquivo);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerMaterialApoio(ArquivoVO arquivo, AtividadeDiscursivaVO obj, UsuarioVO usuarioVO) throws Exception {
		Iterator<ArquivoVO> i = obj.getListaMaterialDeApoio().iterator();
		while (i.hasNext()) {
			ArquivoVO objExistente = (ArquivoVO) i.next();
			if (objExistente.getNome().equals(arquivo.getNome())) {
				i.remove();
				return;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public AtividadeDiscursivaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AtividadeDiscursivaVO obj = new AtividadeDiscursivaVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setEnunciado(tabelaResultado.getString("enunciado"));
		obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.setDataLiberacao(tabelaResultado.getTimestamp("dataliberacao"));
		obj.setDataLimiteEntrega(tabelaResultado.getTimestamp("datalimiteentrega"));
		obj.setQtdDiasAposInicioLiberar(tabelaResultado.getInt("qtdDiasAposInicioLiberar"));
		obj.setQtdDiasParaConclusao(tabelaResultado.getInt("qtddiasparaconclusao"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setArtefatoEntrega(ArtefatoEntregaEnum.valueOf(tabelaResultado.getString("artefatoentrega")));
		obj.setVincularNotaEspecifica(tabelaResultado.getBoolean("vincularnotaespecifica"));
		obj.setNotaCorrespondente(tabelaResultado.getString("notacorrespondente"));
		obj.getResponsavelCadastro().setCodigo(tabelaResultado.getInt("responsavelcadastro"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("matriculaPeriodoTurmaDisciplina"));
		obj.setPublicoAlvo(PublicoAlvoAtividadeDiscursivaEnum.valueOf(tabelaResultado.getString("publicoAlvo")));
		obj.setDataCadastro(tabelaResultado.getTimestamp("datacadastro"));
		obj.setDefinicaoDataEntregaAtividadeDiscursivaEnum(DefinicaoDataEntregaAtividadeDiscursivaEnum.valueOf(tabelaResultado.getString("definicaoDataEntregaAtividadeDiscursiva")));
		obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
		obj.setTurmaDisciplinaDefinicoesTutoriaOnlineEnum(getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(obj.getTurmaVO().getCodigo(), obj.getDisciplinaVO().getCodigo()));
		montarDadosMatriculaPeriodoTurmaDisciplina(obj);
		obj.setListaMaterialDeApoio(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(), OrigemArquivo.ATIVIDADE_DISCURSIVA.getValor(), nivelMontarDados, usuarioLogado));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setAtividadeDiscursivaRepostaAlunoVO(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarDadosTelaConsultaProfessorAluno(obj.getDisciplinaVO().getCodigo(), obj.getTurmaVO().getCodigo(), obj.getAno(), obj.getSemestre(), obj.getCodigo(), obj.getTurmaVO().getUnidadeEnsino().getCodigo(), usuarioLogado));
		}

		return obj;
	}
	
	public void montarDadosMatriculaPeriodoTurmaDisciplina(AtividadeDiscursivaVO atividadeDiscursivaVO) throws Exception{
		if(Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO())){
			atividadeDiscursivaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorChavePrimariaTrazendoMatricula(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()));
		}
	}

	@Override
	public List<AtividadeDiscursivaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AtividadeDiscursivaVO> vetResultado = new ArrayList<AtividadeDiscursivaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AtividadeDiscursivaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM atividadediscursiva WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AtividadeDiscursivaVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<AtividadeDiscursivaVO> consultarDadosTelaConsulta(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select atividadediscursiva.codigo,  enunciado, turma.codigo as turmacodigo, turma.identificadorturma as turmaidentificadorturma,disciplina.codigo as disciplinacodigo, disciplina.nome as disciplinanome, atividadediscursiva.ano, atividadediscursiva.semestre, atividadediscursiva.datalimiteentrega, ");
		
		sql.append(" (select count(distinct atividadediscursivarespostaaluno.codigo) from atividadediscursivarespostaaluno inner join atividadeDiscursivaInteracao on atividadeDiscursivaInteracao.atividadediscursivarespostaaluno = atividadediscursivarespostaaluno.codigo inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo where atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo and atividadeDiscursivaInteracao.interacaoJaLida = false and atividadeDiscursivaInteracao.interagidopor = 'ALUNO' and matriculaperiodo.ano = '").append(ano).append("' and matriculaperiodo.semestre = '").append(semestre).append("' ");
		getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(usuarioLogado.getPessoa().getCodigo(), sql);
		sql.append(" ) as qtdeNovaInteracao, ");
		
		sql.append(" (select count(atividadediscursivarespostaaluno.codigo) from atividadediscursivarespostaaluno inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo where atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo and atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' and matriculaperiodo.ano = '").append(ano).append("' and matriculaperiodo.semestre = '").append(semestre).append("' ");
		getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(usuarioLogado.getPessoa().getCodigo(), sql);
		sql.append(" ) as qtdeAguardandoAvaliacaoProfessor, ");
		
		sql.append(" (select count(atividadediscursivarespostaaluno.codigo) from atividadediscursivarespostaaluno inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo where atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo and atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_NOVA_RESPOSTA' and matriculaperiodo.ano = '").append(ano).append("' and matriculaperiodo.semestre = '").append(semestre).append("' ");
		getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(usuarioLogado.getPessoa().getCodigo(), sql);
		sql.append(" ) as qtdeAguardandoNovaResposta, ");
		
		sql.append(" (select count(atividadediscursivarespostaaluno.codigo) from atividadediscursivarespostaaluno inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo where atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo and atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AVALIADO' and matriculaperiodo.ano = '").append(ano).append("' and matriculaperiodo.semestre = '").append(semestre).append("' ");
		getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(usuarioLogado.getPessoa().getCodigo(), sql);
		sql.append(" ) as qtdeAvaliado, ");
		
		  sql.append(" ( SELECT COUNT(resultado.matricula) ");
		  sql.append(" FROM ( ");
		  sql.append(" SELECT DISTINCT matricula.matricula ");
		  sql.append(" FROM matricula ");
		  sql.append(" INNER JOIN matriculaperiodo 				  ON Matricula.matricula = MatriculaPeriodo.matricula ");
	      sql.append(" INNER JOIN pessoa 						  ON Pessoa.codigo = Matricula.aluno ");
	      sql.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo ");
	      sql.append(" INNER JOIN disciplina disc                 ON matriculaperiodoturmadisciplina.disciplina = disc.codigo ");
	      sql.append(" INNER JOIN historico 					  ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
	      sql.append(" LEFT JOIN atividadediscursivarespostaaluno ON atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
	      sql.append(" AND atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo ");
	      sql.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula ");
	      sql.append(" AND Pessoa.codigo = Matricula.aluno ");
	      sql.append(" AND  disc.codigo = disciplina.codigo ");
		  sql.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		  sql.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		  getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(usuarioLogado.getPessoa().getCodigo(), sql);
	      sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
	      sql.append(" AND (atividadediscursivarespostaaluno.codigo is null or atividadediscursivarespostaaluno.situacaoRespostaAtividadeDiscursiva = 'AGUARDANDO_RESPOSTA')  ");
		  
		  sql.append(" UNION ALL   ");
		  sql.append(" SELECT  DISTINCT matricula.matricula   ");
		  sql.append(" FROM matricula ");
		  sql.append(" INNER JOIN matriculaperiodo 				  ON Matricula.matricula = MatriculaPeriodo.matricula ");
	      sql.append(" INNER JOIN pessoa 						  ON Pessoa.codigo = Matricula.aluno ");
	      sql.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo ");
	      sql.append(" INNER JOIN disciplina disc                 ON matriculaperiodoturmadisciplina.disciplina = disc.codigo ");
	      sql.append(" INNER JOIN historico 					  ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
	      sql.append(" LEFT JOIN atividadediscursivarespostaaluno ON atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
	      sql.append(" AND atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo ");
	      sql.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula ");
	      sql.append(" AND Pessoa.codigo = Matricula.aluno ");
	      sql.append(" AND  disc.codigo IN (SELECT disciplinaequivalente.equivalente FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = disciplina.codigo) ");
	      sql.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		  sql.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		  getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(usuarioLogado.getPessoa().getCodigo(), sql);
	      sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
	      sql.append(" AND (atividadediscursivarespostaaluno.codigo is null or atividadediscursivarespostaaluno.situacaoRespostaAtividadeDiscursiva = 'AGUARDANDO_RESPOSTA')  ");
	      sql.append(" ) AS resultado ) AS qtdeNaoRespondido 	 ");
		
		sql.append(" FROM atividadediscursiva ");
		sql.append("  INNER JOIN turma 				ON turma.codigo 	   = atividadediscursiva.turma ");
		sql.append("  INNER JOIN disciplina 		ON disciplina.codigo   = atividadediscursiva.disciplina ");
		sql.append(" WHERE turma.codigo =  ").append(codigoTurma);
		sql.append(" AND disciplina.codigo = ").append(codigoDisciplina);

		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" AND ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" AND semestre = '").append(semestre).append("'");
		}
		if (usuarioLogado.getIsApresentarVisaoProfessor()) {
			sql.append(" and atividadediscursiva.responsavelcadastro IN (select distinct usuario.codigo from usuario where usuario.pessoa = ").append(usuarioLogado.getPessoa().getCodigo()).append(") ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<AtividadeDiscursivaVO> atividadeDiscursivas = new ArrayList<AtividadeDiscursivaVO>(0);
		AtividadeDiscursivaVO obj = null;
		while (tabelaResultado.next()) {
			obj = new AtividadeDiscursivaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setEnunciado(tabelaResultado.getString("enunciado"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turmacodigo"));
			obj.setAno(tabelaResultado.getString("ano"));
			obj.setSemestre(tabelaResultado.getString("semestre"));
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplinacodigo"));
			obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turmaidentificadorturma"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplinanome"));
			obj.setQuantidadeAguardandoAvalProfessor(tabelaResultado.getInt("qtdeAguardandoAvaliacaoProfessor"));
			obj.setQuantidadeNovaResposta(tabelaResultado.getInt("qtdeAguardandoNovaResposta"));
			obj.setQuantidadeAvaliado(tabelaResultado.getInt("qtdeAvaliado"));
			obj.setQuantidadeNaoRespondido(tabelaResultado.getInt("qtdeNaoRespondido"));
			obj.setQuantidadeInteracao(tabelaResultado.getInt("qtdeNovaInteracao"));
			obj.setDataLimiteEntrega(Uteis.getDataJDBC(tabelaResultado.getTimestamp("dataLimiteEntrega")));
			atividadeDiscursivas.add(obj);
		}
		return atividadeDiscursivas;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<AtividadeDiscursivaVO> consultarAtividadeDiscursivaParaGeracaoCalendarioAtividade(Integer matriculaPeriodoTurmaDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT atividadediscursiva.codigo, atividadediscursiva.enunciado, atividadediscursiva.dataliberacao, atividadediscursiva.datalimiteentrega, atividadediscursiva.qtddiasaposinicioliberar, atividadediscursiva.qtddiasparaconclusao, turmadisciplina.definicoestutoriaonline ");
		sql.append(" FROM matriculaperiodoturmadisciplina ");
		sql.append(" INNER JOIN disciplina ON matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		sql.append(" INNER JOIN LATERAL ( ");
		sql.append(" SELECT ");
		sql.append("  turma.codigo AS turma ");
		sql.append(" FROM turma ");
		sql.append(" WHERE turma.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT ");
		sql.append(" turmaagrupada.turmaorigem AS turma ");
		sql.append(" FROM turmaagrupada ");
		sql.append(" WHERE turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" UNION ALL 	");
		sql.append(" SELECT	turma.codigo as turma	from turma	");
		sql.append(" WHERE	turma.codigo = matriculaperiodoturmadisciplina.turmateorica ");
		sql.append(" UNION ALL");
		sql.append(" SELECT	turma.codigo as turma	from turma 	");
		sql.append(" WHERE	turma.codigo = matriculaperiodoturmadisciplina.turmapratica ) as turmas on	");
		sql.append(" 1 = 1");
        sql.append(" INNER JOIN  turma ON turmas.turma = turma.codigo ");
		sql.append(" INNER JOIN atividadediscursiva on ((turma.turmaagrupada = false and atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina) or (turma.turmaagrupada and (");
		sql.append(" matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplina = atividadediscursiva.disciplina)");
		sql.append(" or atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina ))) ");
		sql.append(" AND COALESCE(atividadediscursiva.ano, '') = COALESCE(matriculaperiodoturmadisciplina.ano, '') AND COALESCE(atividadediscursiva.semestre, '') = COALESCE(matriculaperiodoturmadisciplina.semestre, '') ");
		sql.append(" INNER JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo AND turmadisciplina.disciplina = disciplina.codigo ");
		sql.append(" LEFT JOIN turma as turmaavaliacao on turmaavaliacao.codigo = atividadediscursiva.turma and turmaavaliacao.codigo = turma.codigo");
		sql.append(" WHERE matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		sql.append(" AND atividadediscursiva.ano = '").append(ano).append("' ");
		sql.append(" AND atividadediscursiva.semestre = '").append(semestre).append("' ");
		sql.append(" AND ((atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' AND (");		
		sql.append(" (turmaavaliacao.turmaagrupada = false and turmaavaliacao.subturma = false and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null)  ");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmapratica )");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmateorica )");
		sql.append(" or (turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.GERAL.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null)");
		sql.append(" or (turmaavaliacao.turmaagrupada and turmaavaliacao.subturma =  false and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ))");
		sql.append(" )) ");
		sql.append(" or  (atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.ALUNO.name()).append("' and atividadediscursiva.matriculaPeriodoTurmaDisciplina = matriculaperiodoturmadisciplina.codigo )) ");
		sql.append(" AND atividadediscursiva.codigo not in ( ");
		sql.append(" select codorigem::numeric from calendarioatividadematricula where matriculaperiodoturmadisciplina  = ").append(matriculaPeriodoTurmaDisciplina).append(" and tipocalendarioatividade  = '").append(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA.name()).append("' ");
		sql.append(" AND codorigem is not null) ");
		sql.append(" AND NOT EXISTS (SELECT FROM atividadediscursivarespostaaluno adra WHERE adra.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" AND adra.atividadediscursiva = atividadediscursiva.codigo) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<AtividadeDiscursivaVO> atividadeDiscursivas = new ArrayList<AtividadeDiscursivaVO>(0);
		AtividadeDiscursivaVO obj = null;
		while (rs.next()) {
			obj = new AtividadeDiscursivaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDataLiberacao(rs.getDate("dataliberacao"));
			obj.setDataLimiteEntrega(rs.getDate("datalimiteentrega"));
			obj.setQtdDiasAposInicioLiberar(rs.getInt("qtddiasaposinicioliberar"));
			obj.setQtdDiasParaConclusao(rs.getInt("qtddiasparaconclusao"));
			if (rs.getString("definicoestutoriaonline") != null) {
				obj.setTurmaDisciplinaDefinicoesTutoriaOnlineEnum(DefinicoesTutoriaOnlineEnum.valueOf(rs.getString("definicoestutoriaonline")));
			}
			atividadeDiscursivas.add(obj);
		}
		return atividadeDiscursivas;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet consultarNovasAtividadeDiscursivaParaEadPorJob(Date data) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  ad.codigo as \"atividadediscursiva.codigo\", ad.dataliberacao as \"atividadediscursiva.dataliberacao\",  ad.datalimiteentrega as \"atividadediscursiva.datalimiteentrega\", ");
		sql.append(" turmadisciplina.definicoestutoriaonline, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" mptd.codigo as \"matriculaperiodoturmadisciplina.codigo\", usuario.codigo as \"usuario.codigo\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\", ");
		sql.append(" calendarioatividadematricula.datainicio as \"calendarioatividadematricula.datainicio\", calendarioatividadematricula.datafim as \"calendarioatividadematricula.datafim\" ");
		sql.append(" from atividadediscursiva  ad ");
		sql.append(" left join turmadisciplina on turmadisciplina.turma = ad.turma and turmadisciplina.disciplina =ad.disciplina ");
		sql.append(" inner join matriculaperiodoturmadisciplina as mptd on mptd.disciplina = ad.disciplina and mptd.turma = ad.turma and mptd.ano = ad.ano and mptd.semestre = ad.semestre ");
		sql.append(" inner join disciplina on disciplina.codigo = mptd.disciplina ");
		sql.append(" inner join turma on turma.codigo = mptd.turma ");
		sql.append(" inner join matricula on matricula.matricula = mptd.matricula ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
		sql.append(" inner join calendarioatividadematricula on calendarioatividadematricula.codorigem::numeric = ad.codigo and calendarioatividadematricula.matriculaperiodoturmadisciplina =  mptd.codigo and calendarioatividadematricula.tipocalendarioatividade = 'PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA' ");
		sql.append(" where turma.configuracaoead is not null ");
		sql.append(" and calendarioatividadematricula.datainicio between '").append(UteisData.getData(data)).append(" 00:00:00' and '").append(UteisData.getData(data)).append(" 23:59:59' ");
		
		sql.append("  ");
		return  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet consultarNovasAtividadeDiscursivaParaPresencialPorJob(Date data) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  ad.codigo as \"atividadediscursiva.codigo\", ad.dataliberacao as \"atividadediscursiva.dataliberacao\",  ad.datalimiteentrega as \"atividadediscursiva.datalimiteentrega\", ");
		sql.append(" turmadisciplina.definicoestutoriaonline, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" mptd.codigo as \"matriculaperiodoturmadisciplina.codigo\", usuario.codigo as \"usuario.codigo\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\", ");
		sql.append(" ad.dataliberacao as \"calendarioatividadematricula.datainicio\", ad.datalimiteentrega as \"calendarioatividadematricula.datafim\" ");
		sql.append(" from atividadediscursiva  ad ");
		sql.append(" left join turmadisciplina on turmadisciplina.turma = ad.turma and turmadisciplina.disciplina =ad.disciplina ");
		sql.append(" inner join matriculaperiodoturmadisciplina as mptd on mptd.disciplina = ad.disciplina and mptd.turma = ad.turma and mptd.ano = ad.ano and mptd.semestre = ad.semestre ");
		sql.append(" inner join disciplina on disciplina.codigo = mptd.disciplina ");
		sql.append(" inner join turma on turma.codigo = mptd.turma ");
		sql.append(" inner join matricula on matricula.matricula = mptd.matricula ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
		sql.append(" where turma.configuracaoead is null and turmadisciplina.definicoestutoriaonline = 'PROGRAMACAO_DE_AULA' ");
		sql.append(" and ad.dataliberacao between '").append(UteisData.getData(data)).append(" 00:00:00' and '").append(UteisData.getData(data)).append(" 23:59:59' ");
		
		sql.append("  ");
		return  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarCriacaoAtividadeDiscursivaPorRequerimento(AtividadeDiscursivaVO atividadeDiscursivaVO, RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception{		
		atividadeDiscursivaVO.setPublicoAlvo(PublicoAlvoAtividadeDiscursivaEnum.ALUNO);
		atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(requerimentoVO.getMatricula());
		if(!requerimentoVO.getMatricula().getMatricula().trim().isEmpty()){
			getFacadeFactory().getMatriculaFacade().carregarDados(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO(), NivelMontarDados.BASICO, usuarioVO);
		}
		atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(requerimentoVO.getDisciplina());
		atividadeDiscursivaVO.setDisciplinaVO(requerimentoVO.getDisciplina());
		atividadeDiscursivaVO.setTurmaVO(requerimentoVO.getTurma());
		if(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getAnual() || atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getSemestral()){
			atividadeDiscursivaVO.setAno(Uteis.getAnoDataAtual4Digitos());
		}
		if(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getSemestral()){
			atividadeDiscursivaVO.setSemestre(Uteis.getSemestreAtual());
		}		
		realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(atividadeDiscursivaVO, usuarioVO);		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursivaVO, UsuarioVO usuario) throws Exception{
		if (atividadeDiscursivaVO.getPublicoAlvo().getIsTipoAluno() 
				&& Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getDisciplinaVO().getCodigo()) 
				&& Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getTurmaVO().getCodigo())
				&& Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula())) {
			MatriculaVO matriculaVO = atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO();
			atividadeDiscursivaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula(), atividadeDiscursivaVO.getTurmaVO(), atividadeDiscursivaVO.getDisciplinaVO().getCodigo(), atividadeDiscursivaVO.getAno(), atividadeDiscursivaVO.getSemestre(), 0, false, true, usuario));
			atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(matriculaVO);
			if(!Uteis.isAtributoPreenchido(atividadeDiscursivaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())){
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_matriculaPeriodoTurmaDisciplina"));
			}			
		}
	} 
	
	@Override
	public List<AtividadeDiscursivaVO> consultarInteracoesNaoLidasAlunosPorCodigoProfessor(Integer codigoProfessor, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSQLConsultarInteracoesNaoLidasAlunosPorCodigoProfessor(codigoProfessor);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<AtividadeDiscursivaVO> atividadeDiscursivas = new ArrayList<AtividadeDiscursivaVO>(0);
		AtividadeDiscursivaVO obj = null;
		while (rs.next()) {
			obj = new AtividadeDiscursivaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setEnunciado(rs.getString("enunciado"));
			obj.getTurmaVO().setCodigo(rs.getInt("turmacodigo"));
			obj.setAno(rs.getString("ano"));
			obj.setSemestre(rs.getString("semestre"));
			obj.getDisciplinaVO().setCodigo(rs.getInt("disciplinacodigo"));
			obj.getTurmaVO().setIdentificadorTurma(rs.getString("turmaidentificadorturma"));
			obj.getDisciplinaVO().setNome(rs.getString("disciplinanome"));
			obj.setQuantidadeAguardandoAvalProfessor(rs.getInt("qtdeAguardandoAvaliacaoProfessor"));
			obj.setQuantidadeNovaResposta(rs.getInt("qtdeAguardandoNovaResposta"));
			obj.setQuantidadeAvaliado(rs.getInt("qtdeAvaliado"));
			obj.setQuantidadeInteracao(rs.getInt("quantidadeInteracoes"));
			obj.setQuantidadeNaoRespondido(rs.getInt("qtdeNaoRespondido"));
			obj.setDataLimiteEntrega(Uteis.getDataJDBC(rs.getTimestamp("atividadediscursiva.dataLimiteEntrega")));
			
			atividadeDiscursivas.add(obj);
		}
		return atividadeDiscursivas;
	}

	public StringBuilder getSQLConsultarInteracoesNaoLidasAlunosPorCodigoProfessor(Integer codigoProfessor) {
		StringBuilder sql = new StringBuilder();		
		  sql.append("select distinct atividadediscursiva.codigo,");
		  sql.append("	atividadediscursiva.datalimiteentrega as \"atividadediscursiva.datalimiteentrega\",");
		  sql.append("	enunciado,");
		  sql.append("	turma.codigo as turmacodigo,");
		  sql.append("	turma.identificadorturma as turmaidentificadorturma,");
		  sql.append("	disciplina.codigo as disciplinacodigo,");
		  sql.append("	disciplina.nome as disciplinanome,");
		  sql.append("	atividadediscursiva.ano,");
		  sql.append("	atividadediscursiva.semestre,");
		  sql.append("	count(matricula.matriculaperiodoturmadisciplina) as qtdeAlunos, ");
		  sql.append("	sum(case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' then 1 else 0 end) as qtdeAguardandoAvaliacaoProfessor,");
		  sql.append("	sum(case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_NOVA_RESPOSTA' then 1 else 0 end) as qtdeAguardandoNovaResposta,");
		  sql.append("	sum(case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AVALIADO' then 1 else 0 end) as qtdeAvaliado,");
		  sql.append("	sum(case when (atividadediscursivarespostaaluno.codigo is null and matricula.matriculaperiodoturmadisciplina is not null) or  atividadediscursivarespostaaluno.situacaoRespostaAtividadeDiscursiva = 'AGUARDANDO_RESPOSTA' then 1 else 0 end) as qtdeNaoRespondido,");
		  sql.append("	sum(case when exists(select codigo from atividadediscursivainteracao where atividadediscursivainteracao.atividadediscursivarespostaaluno = atividadediscursivarespostaaluno.codigo and atividadediscursivainteracao.interacaojalida = 'f' and atividadediscursivainteracao.interagidopor = 'ALUNO' ) then 1 else 0 end) as quantidadeInteracoes,");
		  sql.append("	min(atividadediscursivarespostaaluno.datalimiteentrega) as datalimiteentrega				");
		  sql.append("  from atividadediscursiva");
		  sql.append("  inner join disciplina on disciplina.codigo = atividadediscursiva.disciplina ");
		  sql.append("  inner join turma on turma.codigo = atividadediscursiva.turma");
		  sql.append("  inner join (	");
		  sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		  sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		  sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		  sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		  sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		  sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		  sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		  sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		  sql.append("	and horarioturmadiaitem.professor  = ").append(codigoProfessor);
		  sql.append("	");
		  sql.append(") as horarioprofessor on horarioprofessor.turma = turma.codigo and horarioprofessor.disciplina = disciplina.codigo");
		  sql.append("  and horarioprofessor.ano = atividadediscursiva.ano");
		  sql.append("  and horarioprofessor.semestre = atividadediscursiva.semestre ");
		  sql.append("  left join (");
		  sql.append("  select matriculaperiodoturmadisciplina.matricula, matriculaperiodoturmadisciplina.matriculaperiodo, matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina, ");
		  sql.append("  matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.turmapratica , matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.disciplina, ");
		  sql.append("  matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre,");
		  sql.append("  matriculaperiodoturmadisciplina.turma as turmaprogramada, matriculaperiodoturmadisciplina.disciplina as disciplinaprogramada ");
		  sql.append("  from  matricula ");
		  sql.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		  sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
		  sql.append("  inner join historico on  historico.matricula = matriculaperiodoturmadisciplina.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo  ");
		  sql.append("  inner join curso on curso.codigo = matricula.curso ");
		  sql.append("  inner join (		");
		  sql.append("	select distinct 'TUTORIA' as origem, programacaotutoriaonlineprofessor.codigo as codigoorigem, programacaotutoriaonline.turma, t.subturma, t.tiposubturma, t.turmaagrupada, programacaotutoriaonline.disciplina, programacaotutoriaonline.ano, ");
		  sql.append("	programacaotutoriaonline.semestre, programacaotutoriaonlineprofessor.professor, programacaotutoriaonline.disciplina  as disciplinaprogramada, programacaotutoriaonline.turma  as turmaprogramada");
		  sql.append("	from programacaotutoriaonline ");
		  sql.append("	inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		  sql.append("	left join turma as t on programacaotutoriaonline.turma = t.codigo");
		  sql.append("	where programacaotutoriaonlineprofessor.professor  = ").append(codigoProfessor);
		  sql.append(") as horarioprofessor on matriculaperiodoturmadisciplina.programacaotutoriaonlineprofessor is not null and horarioprofessor.origem = 'TUTORIA' and matriculaperiodoturmadisciplina.programacaotutoriaonlineprofessor = horarioprofessor.codigoorigem");
		  sql.append("  inner join pessoa as professor on horarioprofessor.professor = professor.codigo ");
		  sql.append("  where (curso.periodicidade = 'IN' or (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("')");
		  sql.append("  or (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("'and matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("'))");
		  sql.append("  and professor.codigo = ").append(codigoProfessor);
		  sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		  sql.append(" union all ");
		  getSQLVincularAtividadeDiscursivaTurmaOrigem(codigoProfessor , sql);
		  sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		  sql.append(" union all ");
		  getSQLVincularAtividadeDiscursivaTurmaPratica(codigoProfessor , sql);
		  sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		  sql.append(" union all ");
		  getSQLVincularAtividadeDiscursivaTurmaTeorica(codigoProfessor , sql);
		  sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		  sql.append(") as matricula on ((atividadediscursiva.publicoalvo = 'TURMA' and  matricula.turmaprogramada = atividadediscursiva.turma ");
		  sql.append(" and matricula.disciplinaprogramada = atividadediscursiva.disciplina");
		  sql.append(" and matricula.ano = atividadediscursiva.ano");
		  sql.append(" and matricula.semestre = atividadediscursiva.semestre) or (atividadediscursiva.publicoalvo = 'ALUNO' and atividadediscursiva.matriculaperiodoturmadisciplina = matricula.matriculaperiodoturmadisciplina ))");
		  sql.append(" left join atividadediscursivarespostaaluno on atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo");
		  sql.append(" and atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matricula.matriculaperiodoturmadisciplina");
		  sql.append(" group by atividadediscursiva.codigo, atividadediscursiva.datalimiteentrega, enunciado, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, atividadediscursiva.ano, atividadediscursiva.semestre");
		  return sql;
	}
	
	private void validarPeriodicidadeTurmaAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursivaVO) {
		if (atividadeDiscursivaVO.getTurmaVO().getAnual()) {
			atividadeDiscursivaVO.setSemestre("");
		} else if (!atividadeDiscursivaVO.getTurmaVO().getAnual() && !atividadeDiscursivaVO.getTurmaVO().getSemestral()) {
			atividadeDiscursivaVO.setAno("");
			atividadeDiscursivaVO.setSemestre("");
		}
	}
	
	@Override
	public List<SelectItem> consultarVariavelTituloConfiguracaoAcademicoEAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursivaVO, UsuarioVO usuarioVO) throws Exception {
		if (atividadeDiscursivaVO.getTurmaVO().getCodigo().equals(0)) {
			throw new Exception("O campo TURMA deve ser informado.");
		}
		if (atividadeDiscursivaVO.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
		List<ConfiguracaoAcademicoVO> listaConfiguracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorDisciplinaPorTurmaConfiguracaoTurmaDisciplinaOuGradeDisciplina(atividadeDiscursivaVO.getDisciplinaVO().getCodigo(), atividadeDiscursivaVO.getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		if (listaConfiguracaoAcademicoVOs.isEmpty()) {
			listaConfiguracaoAcademicoVOs.add(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoVinculadaCursoPorTurma(atividadeDiscursivaVO.getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct variavel, titulo from configuracaoacademiconota where tipousonota = 'ATIVIDADE_DISCURSIVA' and utilizarnota and utilizarcomomediafinal = false and length(formulacalculo) = 0 ");
		if (Uteis.isAtributoPreenchido(listaConfiguracaoAcademicoVOs)) {
			sb.append("and configuracaoacademiconota.configuracaoAcademico in(");
			for (ConfiguracaoAcademicoVO configuracaoAcademicoVO : listaConfiguracaoAcademicoVOs) {
				sb.append(configuracaoAcademicoVO.getCodigo()).append(", ");
			}
			sb.append("0) ");
		}
		sb.append(" order by titulo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			lista.add(new SelectItem(tabelaResultado.getString("variavel"), tabelaResultado.getString("titulo")));
		}
		return lista;
	}
	
	@Override
	public List<AtividadeDiscursivaVO> consultarAtividadeDiscursivaPorEnunciado(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT atividadediscursiva.codigo, atividadediscursiva.enunciado, disciplina.codigo as codigoDisciplina, disciplina.nome as disciplina");
		sqlStr.append(" FROM atividadediscursiva ");
		sqlStr.append(" left join disciplina on disciplina.codigo = atividadediscursiva.disciplina");
		sqlStr.append(" WHERE upper(sem_acentos(atividadediscursiva.enunciado)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ");

		sqlStr.append(" ORDER BY atividadediscursiva.codigo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AtividadeDiscursivaVO obj = new AtividadeDiscursivaVO();
		List<AtividadeDiscursivaVO> list = new ArrayList<AtividadeDiscursivaVO>(0);
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setEnunciado(tabelaResultado.getString("enunciado"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("disciplina"))) {
				obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigoDisciplina"));
				obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina"));
}

			list.add(obj);
			obj = new AtividadeDiscursivaVO();
		}
		return list;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AtividadeDiscursivaVO consultarPorCodigoAtividadeDisciplina(Integer codigo, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM atividadediscursiva WHERE codigo = ?";
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sql += "and disciplina = ?";
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo, disciplina);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AtividadeDiscursivaVO();
	}
	
	@Override
	public List<AtividadeDiscursivaVO> consultarAtividadeDiscursivaPorEnunciadoDisciplina(String valorConsulta, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT atividadediscursiva.codigo, atividadediscursiva.enunciado, disciplina.codigo as codigoDisciplina, disciplina.nome as disciplina");
		sqlStr.append(" FROM atividadediscursiva ");
		sqlStr.append(" left join disciplina on disciplina.codigo = atividadediscursiva.disciplina");
		sqlStr.append(" WHERE upper(sem_acentos(atividadediscursiva.enunciado)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina = ").append(disciplina);
		}
		sqlStr.append(" ORDER BY atividadediscursiva.codigo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AtividadeDiscursivaVO obj = new AtividadeDiscursivaVO();
		List<AtividadeDiscursivaVO> list = new ArrayList<AtividadeDiscursivaVO>(0);
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setEnunciado(tabelaResultado.getString("enunciado"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("disciplina"))) {
				obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigoDisciplina"));
				obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina"));
			}

			list.add(obj);
			obj = new AtividadeDiscursivaVO();
		}
		return list;
	}
	
	private void getSQLPadraoVincularAtividadeDiscursivaTurmaMatriculaPeriodoTurmaDisciplina(Integer professorCodigo, StringBuilder sql) {
		sql.append(" AND (matriculaperiodoturmadisciplina.disciplina = atividadediscursiva.disciplina or (turma.turmaagrupada and matriculaperiodoturmadisciplina.disciplina in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = atividadediscursiva.disciplina)))");
		sql.append(" AND ((matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE' and matriculaperiodoturmadisciplina.professor = ").append(professorCodigo).append(" and atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' AND matriculaperiodoturmadisciplina.turma = atividadediscursiva.turma) ");
		sql.append(" or (matriculaperiodoturmadisciplina.modalidadedisciplina != 'ON_LINE' and atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' ");
		sql.append(" AND ((matriculaperiodoturmadisciplina.turmapratica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turma.codigo = matriculaperiodoturmadisciplina.turmapratica )");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turma.codigo =  matriculaperiodoturmadisciplina.turmateorica )");
		sql.append(" or (turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ))");
		sql.append(" or (turma.turmaagrupada = false and turma.subturma = false and matriculaperiodoturmadisciplina.turma = turma.codigo) ");
		sql.append(" )) or (matriculaperiodoturmadisciplina.codigo = atividadediscursiva.matriculaperiodoturmadisciplina and atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.ALUNO.name()).append("')) ");
	}
	
	private void getSQLVincularAtividadeDiscursivaTurmaTeorica(Integer professorCodigo, StringBuilder sql) {
		
		 sql.append("  select  matriculaperiodoturmadisciplina.matricula, matriculaperiodoturmadisciplina.matriculaperiodo, matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina, ");
		 sql.append("  matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.turmapratica , matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.disciplina, ");
		 sql.append("  matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre,");
		 sql.append("  turmaprogramada as turmaprogramada, disciplinaprogramada as disciplinaprogramada");
		 sql.append("  from  ");
		 sql.append("  matricula ");
		 sql.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		 sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.turmateorica is not null");
		 sql.append("  inner join historico on  historico.matricula = matriculaperiodoturmadisciplina.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo  ");
		 sql.append("  inner join curso on curso.codigo = matricula.curso ");
		 sql.append("  inner join (	");
		 sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		 sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		 sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		 sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		 sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		 sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		 sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		 sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		 sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		 sql.append("	and t.turmaagrupada = false and t.subturma and t.tiposubturma = 'TEORICA'");
		 sql.append("	union all");
		 sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		 sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		 sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		 sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		 sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		 sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		 sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		 sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		 sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		 sql.append("	and t.turmaagrupada = true and t.subturma = true and t.tiposubturma = 'TEORICA'");
		 sql.append("	union all ");
		 sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.equivalente as disciplina, horarioturma.anovigente  as ano, ");
		 sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		 sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		 sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		 sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		 sql.append("	inner join disciplinaequivalente on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
		 sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		 sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		 sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		 sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		 sql.append("	and t.turmaagrupada = true and t.subturma = true and t.tiposubturma = 'TEORICA'");
		 sql.append("	union all ");
		 sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.disciplina , horarioturma.anovigente  as ano, ");
		 sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		 sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		 sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		 sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		 sql.append("	inner join disciplinaequivalente on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
		 sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		 sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		 sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		 sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		 sql.append("	and t.turmaagrupada = true and t.subturma = true and t.tiposubturma = 'TEORICA'");
		 sql.append("	");
		 sql.append(") as horarioprofessor on horarioprofessor.origem = 'PROGRAMACAO_AULA' and matriculaperiodoturmadisciplina.turmateorica is not null and horarioprofessor.subturma and  horarioprofessor.tiposubturma = 'TEORICA' and matriculaperiodoturmadisciplina.turmateorica = horarioprofessor.turma ");
		 sql.append(" and matriculaperiodoturmadisciplina.disciplina = horarioprofessor.disciplina and matriculaperiodoturmadisciplina.ano = horarioprofessor.ano and matriculaperiodoturmadisciplina.semestre = horarioprofessor.semestre");
		 sql.append(" inner join pessoa as professor on horarioprofessor.professor = professor.codigo ");
		 sql.append(" where (curso.periodicidade = 'IN' or (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("')");
		 sql.append(" or (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("' and matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("'))");
		 sql.append(" and professor.codigo = ").append(professorCodigo);		
	}
	
	private void getSQLVincularAtividadeDiscursivaTurmaPratica(Integer professorCodigo, StringBuilder sql) {
		sql.append(" select  matriculaperiodoturmadisciplina.matricula, matriculaperiodoturmadisciplina.matriculaperiodo, matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina, ");
		sql.append("  matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.turmapratica , matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.disciplina, ");
		sql.append("  matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre,");
		sql.append("  turmaprogramada as turmaprogramada, disciplinaprogramada as disciplinaprogramada");
		sql.append("  from  ");
		sql.append("  matricula ");
		sql.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.turmapratica is not null");
		sql.append("  inner join historico on  historico.matricula = matriculaperiodoturmadisciplina.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo  ");
		sql.append("  inner join curso on curso.codigo = matricula.curso ");
		sql.append("  inner join (	");
		sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("	and t.turmaagrupada = false and t.subturma = true and t.tiposubturma = 'PRATICA'");
		sql.append("	union all");
		sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("	and t.turmaagrupada = true and t.subturma = true and t.tiposubturma = 'PRATICA'");
		sql.append("	union all ");
		sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.equivalente as disciplina, horarioturma.anovigente  as ano, ");
		sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("	inner join disciplinaequivalente on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
		sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("	and t.turmaagrupada = true and t.subturma = true and t.tiposubturma = 'PRATICA'");
		sql.append("	union all ");
		sql.append("	select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.disciplina , horarioturma.anovigente  as ano, ");
		sql.append("	horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("	inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("	inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("	inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("	inner join disciplinaequivalente on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
		sql.append("	where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("	or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("	or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("	and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("	and t.turmaagrupada = true and t.subturma = true and t.tiposubturma = 'PRATICA'");
		sql.append("	");
		sql.append(") as horarioprofessor on horarioprofessor.origem = 'PROGRAMACAO_AULA' and matriculaperiodoturmadisciplina.turmapratica is not null and horarioprofessor.subturma and  horarioprofessor.tiposubturma = 'PRATICA' ");
		sql.append("  and matriculaperiodoturmadisciplina.turmapratica = horarioprofessor.turma ");
		sql.append("  and matriculaperiodoturmadisciplina.disciplina = horarioprofessor.disciplina and matriculaperiodoturmadisciplina.ano = horarioprofessor.ano and matriculaperiodoturmadisciplina.semestre = horarioprofessor.semestre");
		sql.append("  inner join pessoa as professor on horarioprofessor.professor = professor.codigo ");
		sql.append("  where (curso.periodicidade = 'IN' or (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("' and matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("'))");
		sql.append("  and professor.codigo = ").append(professorCodigo);
	}
	
	private void getSQLVincularAtividadeDiscursivaTurmaOrigem(Integer professorCodigo, StringBuilder sql) {
		sql.append("  select  matriculaperiodoturmadisciplina.matricula, matriculaperiodoturmadisciplina.matriculaperiodo, matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina, ");
		sql.append("  matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.turmapratica , matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.disciplina, ");
		sql.append("  matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre,");
		sql.append("  turmaprogramada as turmaprogramada, disciplinaprogramada as disciplinaprogramada ");
		sql.append("  from  ");
		sql.append("  matricula ");
		sql.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.turmapratica is  null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append("  inner join historico on  historico.matricula = matriculaperiodoturmadisciplina.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo  ");
		sql.append("  inner join curso on curso.codigo = matricula.curso ");
		sql.append("  inner join (	");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = false");
		sql.append("  union all ");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, turmaagrupada.turma as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = true and t.subturma = false");
		sql.append("  union all ");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, turmaagrupada.turma as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.equivalente as disciplina, horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  inner join disciplinaequivalente on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
		sql.append("  inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = true and t.subturma = false");
		sql.append("  union all ");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, turmaagrupada.turma as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.disciplina , horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  inner join disciplinaequivalente on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
		sql.append("  inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = true and t.subturma = false");
		sql.append("  union all ");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, horarioturmadiaitem.disciplina, horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = true and t.subturma = true");
		sql.append("  union all ");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.equivalente as disciplina, horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  inner join disciplinaequivalente on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = true and t.subturma = true");
		sql.append("  union all ");
		sql.append("  select distinct 'PROGRAMACAO_AULA' as origem, horarioturma.codigo as codigoorigem, t.codigo as turma, t.subturma, t.tiposubturma, t.turmaagrupada, disciplinaequivalente.disciplina , horarioturma.anovigente  as ano, ");
		sql.append("  horarioturma.semestrevigente as semestre, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina as disciplinaprogramada, horarioturma.turma as turmaprogramada from horarioturma 	");
		sql.append("  inner join horarioturmadia on  horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on  horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma as t on t.codigo  = horarioturma.turma");
		sql.append("  inner join disciplinaequivalente on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
		sql.append("  where ((t.semestral and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("' and horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("')");
		sql.append("  or (t.anual and horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (t.semestral = false and t.anual = false and horarioturmadiaitem.data >= (current_date - '1 year'::interval )))		");
		sql.append("  and horarioturmadiaitem.professor  = ").append(professorCodigo);
		sql.append("  and t.turmaagrupada = true and t.subturma = true");
		sql.append(") as horarioprofessor on  horarioprofessor.origem = 'PROGRAMACAO_AULA' and matriculaperiodoturmadisciplina.turmapratica is  null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append("  and matriculaperiodoturmadisciplina.turma = horarioprofessor.turma ");
		sql.append("  and matriculaperiodoturmadisciplina.disciplina = horarioprofessor.disciplina and matriculaperiodoturmadisciplina.ano = horarioprofessor.ano and matriculaperiodoturmadisciplina.semestre = horarioprofessor.semestre");
		sql.append("  inner join pessoa as professor on horarioprofessor.professor = professor.codigo ");
		sql.append("  where (curso.periodicidade = 'IN' or (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("')");
		sql.append("  or (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("' and matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("'))");
		sql.append("  and professor.codigo = ").append(professorCodigo);
	}
}
