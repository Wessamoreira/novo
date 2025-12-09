package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import negocio.comuns.academico.ArquivoLogVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.SetranspVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ModuloDisponibilizarMaterialEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ArquivoInterfaceFacade;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ArquivoVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ArquivoVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see ArquivoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
public class Arquivo extends ControleAcesso implements ArquivoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;
	private ArquivoHelper arquivoHelper = new ArquivoHelper();
	private PrintWriter pwArquivo;

	public Arquivo() throws Exception {
		super();
		setIdEntidade("Upload");
	}

	public void validarDownloadArquivoAluno(UsuarioVO usuario) throws Exception {
		Arquivo.consultar("DownloadArquivo", true, usuario);		
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ArquivoVO</code>.
	 */
	public ArquivoVO novo() throws Exception {
		Arquivo.incluir(getIdEntidade());
		ArquivoVO obj = new ArquivoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirArquivos(List<ArquivoVO> objs, int codOrigem, OrigemArquivo origemArquivo, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (objs.isEmpty()) {
			throw new Exception("A lista de ARQUIVOS encontra-se vazia. Favor preenche-la!");
		}
		for (ArquivoVO arquivo : objs) {
			if (!Uteis.isAtributoPreenchido(arquivo.getCodigo())) {
				arquivo.setCodOrigem(codOrigem);
				if(origemArquivo != null) {
					arquivo.setOrigem(origemArquivo.getValor());
				}
				this.incluir(arquivo, usuario, configuracaoGeralSistemaVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		incluir(obj, false, usuario, configuracaoGeralSistemaVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirBackUp(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		incluirBackUp(obj, false, usuario, configuracaoGeralSistemaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		Uteis.checkState(obj.getPastaBaseArquivoEnum() == null, "Por favor verificar o Arquivo, pois o caminho do mesmo não foi informado.");
		if (usuario != null && !usuario.getVisaoLogar().equals("aluno") && obj.getPastaBaseArquivoEnum() != null && !obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED)
				&& !obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP)) {
			Arquivo.incluir(getIdEntidade(), verificarAcesso, usuario);
		}
		obj.realizarUpperCaseDados();
		if(obj.getServidorArquivoOnline().isAmazonS3()) {
			ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
			realizarUploadServidorAmazon(servidorExternoAmazon, obj, configuracaoGeralSistemaVO, true);			
		}else if (obj.getServidorArquivoOnline().isApache() &&  ((obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO_TMP)) && obj.getDisciplina().getCodigo() > 0 && (obj.getPastaBaseArquivoEnum().getValue() + File.separator + obj.getDisciplina().getCodigo()).equals(obj.getPastaBaseArquivo()))) {
			incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
			getFacadeFactory().getArquivoHelper().validarDadosExistenciaArquivoDisco(obj, PastaBaseArquivoEnum.ARQUIVO.getValue(), configuracaoGeralSistemaVO);
		} else if (obj.getServidorArquivoOnline().isApache() &&  ((obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.COMUM) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.COMUM_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CURSO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CURSO_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ASSINATURA) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ASSINATURA_TMP))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.UNIDADEENSINO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.UNIDADEENSINO_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.OUVIDORIA) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.OUVIDORIA_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADO_WEB_SERVICE_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADO_WEB_SERVICE))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IREPORT_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IREPORT)  || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP)))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.OFX_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.OFX))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ESTAGIO_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ESTAGIO))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.BANNER_LOGIN_TMP))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES_TMP))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REMESSA_PG_TMP) ||  obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REMESSA_PG))
				||  obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP)
				||  obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP))) {			
			incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
		} else if (obj.getServidorArquivoOnline().isApache() &&  (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM_TMP))) {
			incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
		}
		
		

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			final StringBuilder sql = new StringBuilder("INSERT INTO arquivo ( nome, descricao, extensao, dataUpload, responsavelUpload, ")
					.append(" dataDisponibilizacao, dataIndisponibilizacao, manterDisponibilizacao, origem, disciplina, ")
					.append(" turma, situacao, controlarDownload, permitirArquivoResposta, arquivoResposta, ")
					.append(" codOrigem, pastaBaseArquivo, professor, nivelEducacional, cpfalunodocumentacao, ")
					.append(" cpfrequerimento, apresentarPortalCoordenador, apresentarPortalProfessor, apresentarPortalAluno, curso, ")
					.append(" apresentarDeterminadoPeriodo, indice, agrupador, indiceAgrupador, arquivoAssinadoDigitalmente, ")
					.append(" pessoa, descricaoArquivo, servidorArquivoOnline, arquivoAssinadoFuncionario, arquivoAssinadoUnidadeEnsino, arquivoAssinadoUnidadeCertificadora, ")
					.append(" unidadeEnsino, tipoDocumento, departamento, apresentarDocumentoPortalTransparencia, moduloDisponibilizarMaterial, arquivoIreportPrincipal, tipoRelatorio, arquivoConvertidoPDFA, arquivoispdfa) ")
					.append(" VALUES ( ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				
				sqlInserir.setString(1, obj.getNome());
				sqlInserir.setString(2, obj.getDescricao());
				sqlInserir.setString(3, obj.getExtensao());
				sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataUpload()));
				if (obj.getResponsavelUpload().getCodigo().intValue() != 0) {
					sqlInserir.setInt(5, obj.getResponsavelUpload().getCodigo().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}
				sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataDisponibilizacao()));
				if (obj.isManterDisponibilizacao().booleanValue()) {
					sqlInserir.setNull(7, 0);
				} else {
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataIndisponibilizacao()));
				}
				sqlInserir.setBoolean(8, obj.isManterDisponibilizacao().booleanValue());
				sqlInserir.setString(9, obj.getOrigem());
				if (obj.getDisciplina().getCodigo().intValue() != 0) {
					sqlInserir.setInt(10, obj.getDisciplina().getCodigo().intValue());
				} else {
					sqlInserir.setNull(10, 0);
				}
				if (obj.getTurma().getCodigo().intValue() != 0) {
					sqlInserir.setInt(11, obj.getTurma().getCodigo().intValue());
				} else {
					sqlInserir.setNull(11, 0);
				}
				sqlInserir.setString(12, obj.getSituacao());
				sqlInserir.setBoolean(13, obj.getControlarDownload().booleanValue());
				sqlInserir.setBoolean(14, obj.getPermitirArquivoResposta().booleanValue());
				sqlInserir.setInt(15, obj.getArquivoResposta());
				sqlInserir.setInt(16, obj.getCodOrigem());
				sqlInserir.setString(17, obj.getPastaBaseArquivo());
				if (obj.getProfessor().getCodigo().intValue() != 0) {
					sqlInserir.setInt(18, obj.getProfessor().getCodigo().intValue());
				} else {
					sqlInserir.setNull(18, 0);
				}
				sqlInserir.setString(19, obj.getNivelEducacional());
				sqlInserir.setString(20, obj.getCpfAlunoDocumentacao());
				sqlInserir.setString(21, obj.getCpfRequerimento());
				sqlInserir.setBoolean(22, obj.getApresentarPortalCoordenador());
				sqlInserir.setBoolean(23, obj.getApresentarPortalProfessor());
				sqlInserir.setBoolean(24, obj.getApresentarPortalAluno());
				if (obj.getCurso().getCodigo().intValue() != 0) {
					sqlInserir.setInt(25, obj.getCurso().getCodigo().intValue());
				} else {
					sqlInserir.setNull(25, 0);
				}
				sqlInserir.setBoolean(26, obj.getApresentarDeterminadoPeriodo());
				sqlInserir.setInt(27, obj.getIndice());
				sqlInserir.setString(28, obj.getAgrupador());
				sqlInserir.setInt(29, obj.getIndiceAgrupador());
				sqlInserir.setBoolean(30, obj.getArquivoAssinadoDigitalmente());
				if (obj.getPessoaVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(31, obj.getPessoaVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(31, 0);
				}

				if (!Strings.isNullOrEmpty(obj.getDescricaoArquivo())) {
					sqlInserir.setString(32, obj.getDescricaoArquivo());
				} else {
					sqlInserir.setNull(32, 0);
				}
				
				sqlInserir.setString(33, obj.getServidorArquivoOnline().toString());
				
				int x = 34;
				Uteis.setValuePreparedStatement(obj.getArquivoAssinadoFuncionario(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getArquivoAssinadoUnidadeEnsino(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getArquivoAssinadoUnidadeCertificadora(), x++, sqlInserir);
				
				if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
					sqlInserir.setInt(x++, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getTipoDocumentoVO().getCodigo())) {
					sqlInserir.setInt(x++, obj.getTipoDocumentoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO().getCodigo())) {
					sqlInserir.setInt(x++, obj.getDepartamentoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				sqlInserir.setBoolean(x++, obj.getApresentarDocumentoPortalTransparencia());
				
				if (Uteis.isAtributoPreenchido(obj.getModuloDisponibilizarMaterial())) {
					sqlInserir.setString(x++, obj.getModuloDisponibilizarMaterial().name());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				
				Uteis.setValuePreparedStatement(obj.getArquivoIreportPrincipal(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getTipoRelatorio(), x++, sqlInserir);
				sqlInserir.setBoolean(x++, obj.getArquivoConvertidoPDFA());
				if (Objects.nonNull(obj.getArquivoIsPdfa())) {
					sqlInserir.setBoolean(x++, obj.getArquivoIsPdfa());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setUploadRealizado(false);
		obj.setDescricaoAntesAlteracao(obj.getDescricao());
		ArquivoLogVO arquivoLogVO = new ArquivoLogVO();
		getFacadeFactory().getArquivoLogFacade().inicializarDadosLogInclusaoArquivo(obj, arquivoLogVO, usuario, configuracaoGeralSistemaVO);
		getFacadeFactory().getArquivoLogFacade().incluir(arquivoLogVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	public void realizarUploadServidorAmazon(ServidorArquivoOnlineS3RS servidorExternoAmazon, final ArquivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean isDeletarAqruivoExistente) throws ConsistirException {
		File fileTemp = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + obj.getPastaBaseArquivo() + File.separator + obj.getNome());
		Uteis.checkState(!fileTemp.exists(), "Arquivo não foi encontrado no caminho  " + fileTemp.getAbsolutePath() + " temporario para ser enviado ao servidor externo.");
		String pastaBase = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj, obj.getPastaBaseArquivoEnum().getValue(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
		pastaBase = obj.recuperaNomePastaBaseCorrigido(pastaBase, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
		if (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) {
			servidorExternoAmazon.uploadObjeto(obj.recuperarNomeArquivoServidorExterno(pastaBase, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getNome()), fileTemp, isDeletarAqruivoExistente);
		} else {
			servidorExternoAmazon.uploadObjeto(obj.recuperarNomeArquivoServidorExterno(pastaBase, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getDescricao()), fileTemp, isDeletarAqruivoExistente);
		}
		fileTemp.delete();
		obj.setPastaBaseArquivo(pastaBase);
	}
	
	private String realizarAlteracaoParaServidorAmazon(final ArquivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		String pastaBaseAntesAlteracao = "";
		ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
		if(obj.isUploadRealizado() || (Uteis.isAtributoPreenchido(obj.getDescricaoAntesAlteracao()) && !obj.getDescricao().equals(obj.getDescricaoAntesAlteracao()))) {
			servidorExternoAmazon.deletarArquivoPorAlteracao(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getDescricaoAntesAlteracao()));
			realizarUploadServidorAmazon(servidorExternoAmazon, obj, configuracaoGeralSistemaVO, true);
		}else {
			pastaBaseAntesAlteracao = obj.recuperaNomePastaBaseCorrigido(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
			String pastaBaseNova = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj, obj.getPastaBaseArquivoEnum().getValue(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());			
			pastaBaseNova = obj.recuperaNomePastaBaseCorrigido(pastaBaseNova, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
			if(!pastaBaseAntesAlteracao.equals(pastaBaseNova)) {
				servidorExternoAmazon.alterarEstruturaDeDiretorioArquivo(pastaBaseAntesAlteracao, pastaBaseNova, obj.getDescricao(), obj.getPastaBaseArquivoEnum(), true);
				obj.setPastaBaseArquivo(pastaBaseNova);
			}					
		}
		return pastaBaseAntesAlteracao;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirArquivoNoServidorOnline(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		if(configuracaoGeralSistemaVO.getIntegracaoServidorOnline()) {
			obj.setServidorArquivoOnline(ServidorArquivoOnlineEnum.AMAZON_S3);
		}
		
		incluir(obj, verificarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirBackUp(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (!usuario.getVisaoLogar().equals("aluno")) {
			Arquivo.incluir(getIdEntidade(), verificarAcesso, usuario);
		}
		obj.realizarUpperCaseDados();

		if (((obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO_TMP)) && obj.getDisciplina().getCodigo() > 0 && (obj.getPastaBaseArquivoEnum().getValue() + File.separator + obj.getDisciplina().getCodigo()).equals(obj.getPastaBaseArquivo()))) {
			incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
			getFacadeFactory().getArquivoHelper().validarDadosExistenciaArquivoDisco(obj, PastaBaseArquivoEnum.ARQUIVO.getValue(), configuracaoGeralSistemaVO);
		} else if ((obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.COMUM) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.COMUM_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CURSO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CURSO_TMP)) || (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ASSINATURA) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ASSINATURA_TMP))
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.UNIDADEENSINO) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.UNIDADEENSINO_TMP)) 
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.OUVIDORIA) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.OUVIDORIA_TMP)) 
				|| (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR) 
				|| obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP)
				|| obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP))) {
			incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
		} else if ((obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM_TMP))) {
			// getFacadeFactory().getArquivoHelper().incluirPastaBaseArquivoImagem(obj,
			// PastaBaseArquivoEnum.IMAGEM.getValue(),
			// configuracaoGeralSistemaVO);
			incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
		}

		final String sql = "INSERT INTO arquivobackup( nome, descricao, extensao, " + "dataUpload, responsavelUpload, dataDisponibilizacao, " + "dataIndisponibilizacao, manterDisponibilizacao, origem, disciplina, turma, situacao, controlarDownload, " + "permitirArquivoResposta, arquivoResposta, codOrigem, pastaBaseArquivo, professor, nivelEducacional, cpfalunodocumentacao, cpfrequerimento, apresentarPortalCoordenador, apresentarPortalProfessor, apresentarPortalAluno, curso, "
		        + "apresentarDeterminadoPeriodo, indice, agrupador, indiceAgrupador, descricaoArquivo) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getNome());
				sqlInserir.setString(2, obj.getDescricao());
				sqlInserir.setString(3, obj.getExtensao());
				sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataUpload()));
				if (obj.getResponsavelUpload().getCodigo().intValue() != 0) {
					sqlInserir.setInt(5, obj.getResponsavelUpload().getCodigo().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}
				sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataDisponibilizacao()));
				if (obj.isManterDisponibilizacao().booleanValue()) {
					sqlInserir.setNull(7, 0);
				} else {
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataIndisponibilizacao()));
				}
				sqlInserir.setBoolean(8, obj.isManterDisponibilizacao().booleanValue());
				sqlInserir.setString(9, obj.getOrigem());
				if (obj.getDisciplina().getCodigo().intValue() != 0) {
					sqlInserir.setInt(10, obj.getDisciplina().getCodigo().intValue());
				} else {
					sqlInserir.setNull(10, 0);
				}
				if (obj.getTurma().getCodigo().intValue() != 0) {
					sqlInserir.setInt(11, obj.getTurma().getCodigo().intValue());
				} else {
					sqlInserir.setNull(11, 0);
				}
				sqlInserir.setString(12, obj.getSituacao());
				sqlInserir.setBoolean(13, obj.getControlarDownload().booleanValue());
				sqlInserir.setBoolean(14, obj.getPermitirArquivoResposta().booleanValue());
				sqlInserir.setInt(15, obj.getArquivoResposta());
				sqlInserir.setInt(16, obj.getCodOrigem());
				sqlInserir.setString(17, obj.getPastaBaseArquivo());
				if (obj.getProfessor().getCodigo().intValue() != 0) {
					sqlInserir.setInt(18, obj.getProfessor().getCodigo().intValue());
				} else {
					sqlInserir.setNull(18, 0);
				}
				sqlInserir.setString(19, obj.getNivelEducacional());
				sqlInserir.setString(20, obj.getCpfAlunoDocumentacao());
				sqlInserir.setString(21, obj.getCpfRequerimento());
				sqlInserir.setBoolean(22, obj.getApresentarPortalCoordenador());
				sqlInserir.setBoolean(23, obj.getApresentarPortalProfessor());
				sqlInserir.setBoolean(24, obj.getApresentarPortalAluno());
				if (obj.getCurso().getCodigo().intValue() != 0) {
					sqlInserir.setInt(25, obj.getCurso().getCodigo().intValue());
				} else {
					sqlInserir.setNull(25, 0);
				}
				sqlInserir.setBoolean(26, obj.getApresentarDeterminadoPeriodo());
				sqlInserir.setInt(27, obj.getIndice());
				sqlInserir.setString(28, obj.getAgrupador());
				sqlInserir.setInt(29, obj.getIndiceAgrupador());
				sqlInserir.setString(30, obj.getDescricaoArquivo());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirArquivoSetransp(SetranspVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			this.incluir(obj.getArquivo(), usuario, configuracaoGeralSistemaVO);
			getFacadeFactory().getSetranspFacade().incluir(obj, usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		alterar(obj, false, usuario, configuracaoGeralSistemaVO);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ArquivoVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ArquivoVO</code> que será alterada no banco
	 *            de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {		
		String pastaBaseAntesAlteracao = "";
		try {
			if (!usuario.getVisaoLogar().equals("aluno") && obj.getPastaBaseArquivoEnum() != null && !obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP)) {
				Arquivo.alterar(getIdEntidade(), verificarAcesso, usuario);
			}
			obj.realizarUpperCaseDados();
			if(obj.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
				pastaBaseAntesAlteracao = realizarAlteracaoParaServidorAmazon(obj, configuracaoGeralSistemaVO);
			}else if (Uteis.isAtributoPreenchido(obj.getPastaBaseArquivoEnum()) && (obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM_TMP) || obj.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP))) {
				incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
			} else {
				if (!obj.getCpfAlunoDocumentacao().equals("")) {
					//if ((obj.getPastaBaseArquivoEnum().getValue() + File.separator + obj.getCpfAlunoDocumentacao()).equals(obj.getPastaBaseArquivo())) {
					if (obj.getPastaBaseArquivo().contains(obj.getCpfAlunoDocumentacao()) && obj.getPastaBaseArquivo().contains("TMP")) {
						incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
					}
				} else if (!obj.getCpfRequerimento().equals("")) {
					if (Uteis.isAtributoPreenchido(obj.getPastaBaseArquivoEnum()) && (obj.getPastaBaseArquivoEnum().getValue() + File.separator + obj.getCpfRequerimento()).equals(obj.getPastaBaseArquivo())) {
						incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
					}
				} else {
					if (Uteis.isAtributoPreenchido(obj.getPastaBaseArquivoEnum()) && (obj.getPastaBaseArquivoEnum().getValue().equals(obj.getPastaBaseArquivo()) || obj.getPastaBaseArquivo().contains("TMP"))) {
						incluirArquivoNoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
					}
				}
			}
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder("UPDATE arquivo set nome=?, descricao=?, extensao=?, dataUpload=?, responsavelUpload=?, ")
							.append(" dataDisponibilizacao=?, dataIndisponibilizacao=?, manterDisponibilizacao=?, origem=?, disciplina=?, ")
							.append(" turma=?, situacao=?, controlarDownload=?, permitirArquivoResposta=?, arquivoResposta=?, ")
							.append(" codOrigem=?, pastaBaseArquivo=?, professor=?, nivelEducacional=?, cpfalunodocumentacao=?, ")
							.append(" apresentarPortalCoordenador=?, apresentarPortalProfessor=?, apresentarPortalAluno=?, cpfrequerimento=?, curso = ?, ")
							.append(" apresentarDeterminadoPeriodo=?, indice=?, agrupador=?, indiceAgrupador=?, arquivoAssinadoDigitalmente=?, ")
							.append(" pessoa=?, descricaoArquivo=?, servidorArquivoOnline=?, ")
							.append(" arquivoAssinadoFuncionario = ?, arquivoAssinadoUnidadeEnsino = ? , arquivoAssinadoUnidadeCertificadora = ?, unidadeEnsino=?, tipoDocumento=?, departamento=?, apresentarDocumentoPortalTransparencia=?, moduloDisponibilizarMaterial=?, arquivoIreportPrincipal =?  WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
					
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getDescricao());
					sqlAlterar.setString(3, obj.getExtensao());
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataUpload()));
					if (obj.getResponsavelUpload().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getResponsavelUpload().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataDisponibilizacao()));
					if (obj.isManterDisponibilizacao()) {
						sqlAlterar.setNull(7, 0);
					} else {
						sqlAlterar.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataIndisponibilizacao()));
					}
					sqlAlterar.setBoolean(8, obj.isManterDisponibilizacao().booleanValue());
					sqlAlterar.setString(9, obj.getOrigem());
					if (obj.getDisciplina().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getDisciplina().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getTurma().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, obj.getSituacao());
					sqlAlterar.setBoolean(13, obj.getControlarDownload().booleanValue());
					sqlAlterar.setBoolean(14, obj.getPermitirArquivoResposta().booleanValue());
					sqlAlterar.setInt(15, obj.getArquivoResposta());
					sqlAlterar.setInt(16, obj.getCodOrigem());
					sqlAlterar.setString(17, obj.getPastaBaseArquivo());
					if (obj.getProfessor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(18, obj.getProfessor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(18, 0);
					}
					sqlAlterar.setString(19, obj.getNivelEducacional());
					sqlAlterar.setString(20, obj.getCpfAlunoDocumentacao());
					sqlAlterar.setBoolean(21, obj.getApresentarPortalCoordenador());
					sqlAlterar.setBoolean(22, obj.getApresentarPortalProfessor());
					sqlAlterar.setBoolean(23, obj.getApresentarPortalAluno());
					sqlAlterar.setString(24, obj.getCpfRequerimento());
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(25, obj.getCurso().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(25, 0);
					}
					sqlAlterar.setBoolean(26, obj.getApresentarDeterminadoPeriodo());
					sqlAlterar.setInt(27, obj.getIndice());
					sqlAlterar.setString(28, obj.getAgrupador());
					sqlAlterar.setInt(29, obj.getIndiceAgrupador());
					sqlAlterar.setBoolean(30, obj.getArquivoAssinadoDigitalmente());
					if (obj.getPessoaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(31, obj.getPessoaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(31, 0);
					}
					int x = 32;
					Uteis.setValuePreparedStatement(obj.getDescricaoArquivo(), x++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getServidorArquivoOnline().toString(), x++, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getArquivoAssinadoFuncionario(), x++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getArquivoAssinadoUnidadeEnsino(), x++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getArquivoAssinadoUnidadeCertificadora(), x++, sqlAlterar);
					
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
						sqlAlterar.setInt(x++, obj.getUnidadeEnsinoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoDocumentoVO().getCodigo())) {
						sqlAlterar.setInt(x++, obj.getTipoDocumentoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO().getCodigo())) {
						sqlAlterar.setInt(x++, obj.getDepartamentoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getApresentarDocumentoPortalTransparencia());
					if (Uteis.isAtributoPreenchido(obj.getModuloDisponibilizarMaterial())) {
						sqlAlterar.setString(x++, obj.getModuloDisponibilizarMaterial().name());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					
					Uteis.setValuePreparedStatement(obj.getArquivoIreportPrincipal(), x++, sqlAlterar);
					
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, usuario, configuracaoGeralSistemaVO);
				return;
			}
			obj.setDescricaoAntesAlteracao(obj.getDescricao());
			obj.setUploadRealizado(false);
		} catch (Exception e) {
			if(Uteis.isAtributoPreenchido(pastaBaseAntesAlteracao)) {
				obj.setPastaBaseArquivo(pastaBaseAntesAlteracao);	
			}
			throw e;
		}
	}

	

	private List<ArquivoVO> consultarArquivoQueNaoEstaoNaListaEntidadeComCodigoOrigemComOrigem(List<ArquivoVO> listaArquivo, String codigoOrigem, OrigemArquivo origemArquivo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("select * FROM arquivo where codorigem =  ").append(codigoOrigem).append(" and origem = '").append(origemArquivo.getValor()).append("' ");
		if (!listaArquivo.isEmpty()) {
			sb.append(" and codigo not in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaArquivo)).append(" )");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void excluirArquivoQueNaoEstaNaListaComCodigoOrigemComOrigemArquivo(List<ArquivoVO> listaArquivo, String codigoOrigem, OrigemArquivo origemArquivo, PastaBaseArquivoEnum pastaBaseArquivoEnum, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		List<ArquivoVO> listaArquivoParaExcluir = consultarArquivoQueNaoEstaoNaListaEntidadeComCodigoOrigemComOrigem(listaArquivo, codigoOrigem, origemArquivo, nivelMontarDados, usuario);
		for (ArquivoVO arquivoVO : listaArquivoParaExcluir) {
			excluirArquivoDoDiretorioEspecifico(arquivoVO, configuracaoGeralSistema.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue());
		}
		StringBuilder sb = new StringBuilder("delete FROM arquivo where codorigem =  ").append(codigoOrigem).append(" and origem = '").append(origemArquivo.getValor()).append("' ");
		if (!listaArquivo.isEmpty()) {
			sb.append(" and codigo not in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaArquivo)).append(" )");
		}
		getConexao().getJdbcTemplate().execute(sb.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public boolean validarSeArquivoAptaExclusao(ArquivoVO arquivo, UsuarioVO usuario) {
		StringBuilder sb = new StringBuilder("");
		Integer qtd = null;
		try {
			if(arquivo.getOrigem().equals(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor())) {
				sb.append(" select count(distinct codigo) as qtd from documetacaomatricula  where ");
				sb.append(" arquivo = ").append(arquivo.getCodigo());
				sb.append(" or arquivoverso = ").append(arquivo.getCodigo());
				sb.append(" or arquivoged = ").append(arquivo.getCodigo());	
			}
			if(Uteis.isAtributoPreenchido(sb)) {
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
				qtd = (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtd", TipoCampoEnum.INTEIRO);
			}
			return qtd == null || qtd == 0;
		}finally {
			sb = null;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ArquivoVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ArquivoVO</code> que será removido no banco
	 *            de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		excluir(obj, false, "", usuario, configuracaoGeralSistemaVO);
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ArquivoVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ArquivoVO</code> que será removido no banco
	 *            de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			Arquivo.excluir(nomeEntidade, verificarAcesso, usuario);
			if(validarSeArquivoAptaExclusao(obj, usuario)) {
				String sql = "DELETE FROM arquivo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getFacadeFactory().getDownloadFacade().excluirDownloadsArquivo(obj, usuario);
				getFacadeFactory().getLogExclusaoArquivo().realizarRegistroLogExclusaoArquivo(obj, usuario.getVisaoLogar(), usuario);
				getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
				excluirArquivoDoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
			}
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirRegistroArquivoAmazonPorDocumentacaoMatricula(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			Arquivo.excluir(idEntidade, verificarAcesso, usuario);
			String sql = "DELETE FROM arquivo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getFacadeFactory().getDownloadFacade().excluirDownloadsArquivo(obj, usuario);
			getFacadeFactory().getLogExclusaoArquivo().realizarRegistroLogExclusaoArquivo(obj, usuario.getVisaoLogar(), usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirBackUp(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			Arquivo.excluir(nomeEntidade, verificarAcesso, usuario);

			excluirArquivoDoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);

			String sql = "DELETE FROM arquivobackup WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getFacadeFactory().getDownloadFacade().excluirDownloadsArquivo(obj, usuario);
			getFacadeFactory().getLogExclusaoArquivo().realizarRegistroLogExclusaoArquivo(obj, usuario.getVisaoLogar(), usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirPorDocumentacaoMatriculaRequerimento(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		Arquivo.excluir(nomeEntidade, verificarAcesso, usuario);
		if (validarSeArquivoAptaExclusao(obj, usuario)) {
			String sql = "DELETE FROM arquivo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getFacadeFactory().getDownloadFacade().excluirDownloadsArquivo(obj, usuario);
			if (!obj.getCodigo().equals(0)) {
				getFacadeFactory().getLogExclusaoArquivo().realizarRegistroLogExclusaoArquivo(obj, usuario.getVisaoLogar(), usuario);
			}
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			excluirArquivoDoDiretorioPadrao(obj, usuario, configuracaoGeralSistemaVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarManterDisponibilizacao(ArquivoVO obj, UsuarioVO usuario) throws Exception {
		alterarManterDisponibilizacao(obj, false, "", usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarManterDisponibilizacao(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario) throws Exception {
		try {
			Arquivo.excluir(nomeEntidade, verificarAcesso, usuario);
			String sql = "UPDATE arquivo set manterDisponibilizacao=?, dataIndisponibilizacao=? WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getManterDisponibilizacao(), obj.getDataIndisponibilizacao(), obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>String situacao</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>identificadorTurma</code> da classe
	 * <code>Turma</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorIdentificadorTurmaTurma(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo, Turma WHERE arquivo.turma = Turma.codigo and upper( Turma.identificadorTurma ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Turma.identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>arquivo</code> através do
	 * valor do atributo <code>nome</code> da classe <code>Disciplina</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>arquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorNomeDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo, Disciplina WHERE arquivo.disciplina = Disciplina.codigo and upper( Disciplina.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>Date dataDisponibilizacao</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorDataDisponibilizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo WHERE ((dataDisponibilizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataDisponibilizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataDisponibilizacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>codigo</code> da classe <code>Usuario</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorCodigoUsuario(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo, Usuario WHERE arquivo.responsavelUpload = Usuario.codigo and Usuario.codigo >= " + valorConsulta.intValue() + " ORDER BY Usuario.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>Date dataUpload</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorDataUpload(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo WHERE ((dataUpload >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataUpload <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataUpload";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>String nome</code>. Retorna os objetos, com início do
	 * valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<ArquivoVO> consultarArquivoPorDisciplinaTurma(Integer disciplina, Integer turma, Integer professor,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String campoConsultaSituacao,
			String anoPeriodoUpload, String semestrePeriodoUpload, String anoPeriodoDisponibilizacao,
			String semestrePeriodoDisponibilizacao) throws Exception {
		if (disciplina == 0 && turma == 0 && professor == 0) {
			return new ArrayList<ArquivoVO>(0);
		}
		StringBuilder sqlStr = getSqlConsultaDadosCompletos();
		sqlStr.append(" WHERE arquivo.origem = '").append(OrigemArquivo.PROFESSOR.getValor()).append("' ");
		if (disciplina != 0) {
			sqlStr.append(" AND arquivo.disciplina = ").append(disciplina);
		}
		if (turma != 0) {
			sqlStr.append("AND arquivo.turma = ").append(turma);
		}
		if (professor != 0) {
			sqlStr.append(" AND arquivo.professor = ").append(professor);
		}
		if(!campoConsultaSituacao.equals("")){
			if (campoConsultaSituacao.equals("ativo")) {
				sqlStr.append(" AND (arquivo.situacao = 'AT' AND ((not arquivo.manterDisponibilizacao and arquivo.apresentardeterminadoperiodo and arquivo.dataIndisponibilizacao is not null ");
				sqlStr.append(" and arquivo.dataIndisponibilizacao > now()) or arquivo.manterDisponibilizacao)) ");
			} else if (campoConsultaSituacao.equals("inativo")) {
				sqlStr.append(" AND (arquivo.situacao = 'IN' or (not arquivo.manterDisponibilizacao and arquivo.apresentardeterminadoperiodo and arquivo.dataIndisponibilizacao is not null and arquivo.dataIndisponibilizacao <= now())) ");	
			}
		}
		if(Uteis.isAtributoPreenchido(anoPeriodoUpload)   && Uteis.isAtributoPreenchido(semestrePeriodoUpload)) {
			Map<String, Date> resultado  =	UteisData.getDataInicialFinalPeriodoSemestral(Integer.parseInt(anoPeriodoUpload),Integer.parseInt(semestrePeriodoUpload));
			sqlStr.append(" AND (( arquivo.dataUpload >= '" + Uteis.getDataJDBC(resultado.get("dataInicial")) + "') and (arquivo.dataUpload <= '" + Uteis.getDataJDBC(resultado.get("dataFinal")) + "'))");

		}
		if(Uteis.isAtributoPreenchido(anoPeriodoDisponibilizacao)   && Uteis.isAtributoPreenchido(semestrePeriodoDisponibilizacao)  ) {
			Map<String, Date> resultado  =	UteisData.getDataInicialFinalPeriodoSemestral(Integer.parseInt(anoPeriodoDisponibilizacao),Integer.parseInt(semestrePeriodoDisponibilizacao));
			sqlStr.append(" AND (( arquivo.datadisponibilizacao >= '" + Uteis.getDataJDBC(resultado.get("dataInicial")) + "') and (arquivo.datadisponibilizacao <= '" + Uteis.getDataJDBC(resultado.get("dataFinal")) + "'))");

		}
		if (usuario.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO.getValor()) && Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
			sqlStr.append(" AND (turma.unidadeensino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo()).append(" or turma.unidadeensino is null)");
		}
		sqlStr.append(" ORDER BY arquivo.disciplina, arquivo.turma, arquivo.professor, arquivo.indiceagrupador, arquivo.indice ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaCompleta(tabelaResultado));
	}

	@Override
	public List<ArquivoVO> consultarArquivoPorDisciplinaTurmaBackUp(Integer disciplina, Integer turma, Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (disciplina == 0 && turma == 0 && professor == 0) {
			return new ArrayList<ArquivoVO>(0);
		}
		String sqlStr = "SELECT arquivobackup.* FROM arquivobackup WHERE 1 = 1 ";
		if (disciplina != 0) {
			sqlStr += "AND arquivobackup.disciplina = " + disciplina;
		}
		if (turma != 0) {
			sqlStr += "AND arquivobackup.turma = " + turma + " ";
		}
		if (professor != 0) {
			sqlStr += "AND arquivobackup.professor = " + professor + " ";
		}
		sqlStr += " ORDER BY arquivobackup.disciplina, arquivobackup.turma ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ArquivoVO> consultarArquivosRespostaDosAlunos(OrigemArquivo origemArquivo, Integer codigoArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT arquivo.* FROM arquivo WHERE origem = '" + origemArquivo.getValor() + "' AND arquivoResposta = '" + codigoArquivo + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ArquivoVO> consultarArquivoPorTurmaDisciplinaOrigemAtivos(Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select arquivo.* from arquivo inner join disciplina on arquivo.disciplina = disciplina.codigo left join turma on arquivo.turma = turma.codigo " + "where arquivo.situacao = 'AT' and dataDisponibilizacao <= '" + Uteis.getDataJDBCTimestamp(new Date()) + "' and (dataIndisponibilizacao >= '" + Uteis.getDataJDBCTimestamp(new Date()) + "' or dataIndisponibilizacao is null) ";
		if (!origemArquivo.equals(OrigemArquivo.AMBAS)) {
			sqlStr += "AND arquivo.origem = '" + origemArquivo.getValor() + "'";
		}
		if (turma != null && turma.intValue() > 0) {
			sqlStr += "AND turma.codigo = " + turma;
		}
		sqlStr += "AND disciplina.codigo = " + disciplina + " order by arquivo.dataDisponibilizacao desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ArquivoVO> consultarArquivoPorTurmaDisciplinaOrigemInativos(Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select arquivo.* from arquivo inner join disciplina on arquivo.disciplina = disciplina.codigo left join turma on arquivo.turma = turma.codigo " + "where (arquivo.situacao = 'IN' or dataIndisponibilizacao <'" + Uteis.getDataJDBCTimestamp(new Date()) + "')";
		if (!origemArquivo.equals(OrigemArquivo.AMBAS)) {
			sqlStr += " arquivo.origem = '" + origemArquivo.getValor() + "'";
		}
		if (turma != null && turma.intValue() > 0) {
			sqlStr += " turma.codigo = " + turma;
		}
		if (disciplina != null && disciplina.intValue() > 0) {
			sqlStr += " disciplina.codigo = " + disciplina;
		}
		sqlStr += " order by arquivo.dataDisponibilizacao desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ArquivoVO> consultarArquivoPorResponsavelUploadTurmaDisciplinaOrigemInativos(Integer responsavelUpload, Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select arquivo.* from arquivo inner join disciplina on arquivo.disciplina = disciplina.codigo left join turma on arquivo.turma = turma.codigo " + "where (arquivo.situacao = 'IN' or dataIndisponibilizacao < '" + Uteis.getDataJDBCTimestamp(new Date()) + "')";
		if (!origemArquivo.equals(OrigemArquivo.AMBAS)) {
			sqlStr += " arquivo.origem = '" + origemArquivo.getValor() + "'";
		}
		if (turma != null && turma.intValue() > 0) {
			sqlStr += " turma.codigo = " + turma;
		}
		if (disciplina != null && disciplina.intValue() > 0) {
			sqlStr += " disciplina.codigo = " + disciplina;
		}
		if (responsavelUpload != null && responsavelUpload.intValue() > 0) {
			sqlStr += " arquivo.responsavelUpload = " + responsavelUpload;
		}
		sqlStr += " order by arquivo.dataDisponibilizacao desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ArquivoVO> consultarArquivoPorResponsavelUploadTurmaDisciplinaOrigemAtivos(Integer responsavelUpload, Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select arquivo.* from arquivo inner join disciplina on arquivo.disciplina = disciplina.codigo left join turma on arquivo.turma = turma.codigo " + "where arquivo.situacao = 'AT' and dataDisponibilizacao <= '" + Uteis.getDataJDBCTimestamp(new Date()) + "' and (dataIndisponibilizacao >= '" + Uteis.getDataJDBCTimestamp(new Date()) + "' or dataIndisponibilizacao is null) ";
		if (!origemArquivo.equals(OrigemArquivo.AMBAS)) {
			sqlStr += " and arquivo.origem = '" + origemArquivo.getValor() + "'";
		}
		if (turma != null && turma.intValue() > 0) {
			sqlStr += " and turma.codigo = " + turma;
		}
		// if (disciplina != null && disciplina.intValue() > 0) {
		sqlStr += " and disciplina.codigo = " + disciplina;
		// }
		if (responsavelUpload != null && responsavelUpload.intValue() > 0) {
			sqlStr += " and arquivo.responsavelUpload = " + responsavelUpload;
		}
		sqlStr += " order by arquivo.dataDisponibilizacao desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Arquivo</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ArquivoVO> consultarPorVariosCodigo(String listaCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM arquivo WHERE codigo in ( " + listaCodigo + " ) ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ArquivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivo.* FROM arquivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public byte[] consultarArquivoPorCodigoArquivo(Integer codigo, UsuarioVO usuario) throws Exception {
		String sqlStr = "Select arquivo.arquivo::bytea as arquivo from arquivo where codigo = " + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return (byte[]) tabelaResultado.getObject("arquivo");
		}
		return null;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>ArquivoVO</code>
	 *         resultantes da consulta.
	 */
	public static List<ArquivoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
		List<ArquivoVO> vetResultado = new ArrayList<ArquivoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosCompleto(tabelaResultado));
		}
		return vetResultado;
	}

	public static List<ArquivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ArquivoVO> vetResultado = new ArrayList<ArquivoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe <code>ArquivoVO</code>.
	 *
	 * @return O objeto da classe <code>ArquivoVO</code> com os dados devidamente
	 *         montados.
	 */
	public static ArquivoVO montarDadosCompleto(SqlRowSet dadosSQL) throws Exception {
		ArquivoVO obj = new ArquivoVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setDescricaoAntesAlteracao(dadosSQL.getString("descricao"));
		obj.setDescricaoArquivo(dadosSQL.getString("descricaoArquivo"));
		obj.setExtensao(dadosSQL.getString("extensao"));
		obj.setDataUpload(dadosSQL.getDate("dataUpload"));
		obj.getResponsavelUpload().setCodigo((dadosSQL.getInt("responsavelUpload")));
		obj.setCodOrigem(dadosSQL.getInt("codOrigem"));
		obj.setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getTurma().setCodigo((dadosSQL.getInt("turma")));
		obj.getProfessor().setCodigo((dadosSQL.getInt("professor")));
		obj.setDataDisponibilizacao(dadosSQL.getDate("dataDisponibilizacao"));
		obj.setDataIndisponibilizacao(dadosSQL.getDate("dataIndisponibilizacao"));
		obj.setManterDisponibilizacao((dadosSQL.getBoolean("manterDisponibilizacao")));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoRelatorio"))) {
			obj.setTipoRelatorio(TipoRelatorioEnum.valueOf(dadosSQL.getString("tipoRelatorio")));
		}
		obj.setOrigem(dadosSQL.getString("origem"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.getDisciplina().setCodigo((dadosSQL.getInt("disciplina")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setApresentarPortalProfessor((dadosSQL.getBoolean("apresentarPortalProfessor")));
		obj.setApresentarPortalAluno((dadosSQL.getBoolean("apresentarPortalAluno")));
		obj.setApresentarPortalCoordenador((dadosSQL.getBoolean("apresentarPortalCoordenador")));
		obj.setControlarDownload((dadosSQL.getBoolean("controlarDownload")));
		obj.setPermitirArquivoResposta((dadosSQL.getBoolean("permitirArquivoResposta")));
		obj.setArquivoResposta((dadosSQL.getInt("arquivoResposta")));
		obj.setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacao"));
		obj.setCpfRequerimento(dadosSQL.getString("cpfRequerimento"));
		obj.getResponsavelUpload().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getResponsavelUpload().setNome(dadosSQL.getString("usuario.nome"));
		obj.getProfessor().setCodigo(dadosSQL.getInt("professor.codigo"));
		obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoaVO().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.setApresentarDeterminadoPeriodo(dadosSQL.getBoolean("apresentarDeterminadoPeriodo"));
		obj.setIndice(dadosSQL.getInt("indice"));
		obj.setAgrupador(dadosSQL.getString("agrupador"));
		obj.setIndiceAgrupador(dadosSQL.getInt("indiceAgrupador"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnline"))){
			obj.setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnline")));	
		}
		obj.setArquivoAssinadoFuncionario((dadosSQL.getBoolean("arquivoAssinadoFuncionario")));
		obj.setArquivoAssinadoUnidadeEnsino((dadosSQL.getBoolean("arquivoAssinadoUnidadeEnsino")));
		obj.setArquivoAssinadoUnidadeCertificadora((dadosSQL.getBoolean("arquivoAssinadoUnidadeCertificadora")));
		return obj;
	}
	

	public static ArquivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ArquivoVO obj = new ArquivoVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome").trim());
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setDescricaoAntesAlteracao(dadosSQL.getString("descricao"));
		obj.setDescricaoArquivo(dadosSQL.getString("descricaoArquivo"));
		obj.setExtensao(dadosSQL.getString("extensao"));
		obj.setDataUpload(dadosSQL.getDate("dataUpload"));
		obj.getResponsavelUpload().setCodigo(new Integer(dadosSQL.getInt("responsavelUpload")));
		obj.setCodOrigem(dadosSQL.getInt("codOrigem"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacao"));
		obj.setCpfRequerimento(dadosSQL.getString("cpfRequerimento"));
		obj.setOrigem(dadosSQL.getString("origem"));
		obj.setDataIndisponibilizacao(dadosSQL.getDate("dataIndisponibilizacao"));
		obj.setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.toString(dadosSQL.getString("pastaBaseArquivo")));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor")));
		obj.getPessoaVO().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.setApresentarDeterminadoPeriodo(dadosSQL.getBoolean("apresentarDeterminadoPeriodo"));
		obj.setIndice(dadosSQL.getInt("indice"));
		obj.setAgrupador(dadosSQL.getString("agrupador"));
		obj.setIndiceAgrupador(dadosSQL.getInt("indiceAgrupador"));
		obj.setArquivoAssinadoDigitalmente(dadosSQL.getBoolean("arquivoAssinadoDigitalmente"));
		obj.setArquivoIreportPrincipal(dadosSQL.getBoolean("arquivoireportprincipal"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoRelatorio"))) {
			obj.setTipoRelatorio(TipoRelatorioEnum.valueOf(dadosSQL.getString("tipoRelatorio")));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnline"))){
			obj.setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnline")));	
		}
		obj.setArquivoIsPdfa(Objects.nonNull(dadosSQL.getObject("arquivoispdfa")) ? dadosSQL.getBoolean("arquivoispdfa") : null);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO) {
			return obj;
		}
		
		
		
		obj.setArquivoAssinadoFuncionario((dadosSQL.getBoolean("arquivoAssinadoFuncionario")));
		obj.setArquivoAssinadoUnidadeEnsino((dadosSQL.getBoolean("arquivoAssinadoUnidadeEnsino")));
		obj.setArquivoAssinadoUnidadeCertificadora((dadosSQL.getBoolean("arquivoAssinadoUnidadeCertificadora")));
		obj.setApresentarDocumentoPortalTransparencia(dadosSQL.getBoolean("apresentarDocumentoPortalTransparencia"));
		if (dadosSQL.getString("moduloDisponibilizarMaterial") != null) {
			obj.setModuloDisponibilizarMaterial(ModuloDisponibilizarMaterialEnum.valueOf(dadosSQL.getString("moduloDisponibilizarMaterial")));
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			montarDadosResponsavelUpload(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosTurma(obj, nivelMontarDados, usuario);
			montarDadosProfessor(obj, nivelMontarDados, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		obj.setDataDisponibilizacao(dadosSQL.getDate("dataDisponibilizacao"));
		obj.setApresentarPortalProfessor((dadosSQL.getBoolean("apresentarPortalProfessor")));
		obj.setApresentarPortalAluno((dadosSQL.getBoolean("apresentarPortalAluno")));
		obj.setApresentarPortalCoordenador((dadosSQL.getBoolean("apresentarPortalCoordenador")));
		obj.setManterDisponibilizacao((dadosSQL.getBoolean("manterDisponibilizacao")));

		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setControlarDownload((dadosSQL.getBoolean("controlarDownload")));
		obj.setPermitirArquivoResposta((dadosSQL.getBoolean("permitirArquivoResposta")));
		obj.setArquivoResposta(new Integer(dadosSQL.getInt("arquivoResposta")));
		montarDadosResponsavelUpload(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		// obj.setArquivo((byte[]) dadosSQL.getObject("arquivo"));
		return obj;
	}

	public static void montarDadosPessoa(ArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoaVO().getCodigo().intValue() == 0) {
			obj.setPessoaVO(new PessoaVO());
			return;
		}
		obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoaVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosProfessor(ArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProfessor().getCodigo().intValue() == 0) {
			obj.setProfessor(new PessoaVO());
			return;
		}
		obj.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getProfessor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCurso(ArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TurmaVO</code> relacionado ao objeto <code>ArquivoVO</code>. Faz uso da
	 * chave primária da classe <code>TurmaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTurma(ArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>DisciplinaVO</code> relacionado ao objeto <code>ArquivoVO</code>. Faz
	 * uso da chave primária da classe <code>DisciplinaVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosDisciplina(ArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UsuarioVO</code> relacionado ao objeto <code>ArquivoVO</code>. Faz uso
	 * da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelUpload(ArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelUpload().getCodigo().intValue() == 0) {
			obj.setResponsavelUpload(new UsuarioVO());
			return;
		}
		obj.setResponsavelUpload(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelUpload().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	public ArquivoVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Arquivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new ArquivoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ArquivoVO</code>
	 * através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ArquivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Arquivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Arquivo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public ArquivoVO consultarPorCodOrigemInscricao(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Arquivo WHERE codOrigem = ? and origem = 'PSI' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ArquivoVO consultarPorCodigoIntegracaoMoodle(Long codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql  = new StringBuilder("");
		sql.append(" SELECT arquivo.* FROM Arquivo  where origem = 'PR' and turma is not null and arquivo.codigo::varchar||turma::varchar||arquivo.disciplina::varchar = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		sql  = new StringBuilder("");
		sql.append(" SELECT arquivo.* FROM matriculaperiodoturmadisciplina as mptd");
		sql.append(" inner join arquivo on mptd.disciplina = arquivo.disciplina ");
		sql.append(" where  origem = 'PR' and arquivo.turma is null  ");
		//sql.append(" and arquivo.professor is not null ");
		sql.append(" and arquivo.codigo::varchar||mptd.turma::varchar||arquivo.disciplina::varchar = ? limit 1");		
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Arquivo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	public ArquivoVO consultarPorCodOrigemRequisicao(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Arquivo WHERE codOrigem = ? and origem = 'RS'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	

	/**
	 * Operação responsável por localizar um objeto da classe <code>ArquivoVO</code>
	 * através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ArquivoVO consultarPorChavePrimariaConsultaCompleta(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
		sqlStr.append(" Where arquivo.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Arquivo ).");
		}
		return (montarDadosCompleto(tabelaResultado));

	}

	public ArquivoVO consultarDadosDoArquivoASerRespondido(Integer codigoArquivo, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT t.codigo AS codTurma, t.identificadorTurma AS nomeTurma, d.codigo AS codDisciplina, d.nome AS nomeDisciplina, " + "u.codigo AS codUsuario, u.nome AS nomeUsuario FROM arquivo a " + "INNER JOIN turma t ON a.turma = t.codigo " + "INNER JOIN disciplina d ON a.disciplina = d.codigo " + "INNER JOIN usuario u ON a.responsavelUpload = u.codigo " + "WHERE a.codigo = ?";
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoArquivo });
			if (!tabelaResultado.next()) {
				return new ArquivoVO();
			}
			return montarDadosArquivoASerRespondido(tabelaResultado, usuario);
		} finally {
			sqlStr = null;
		}
	}

	private ArquivoVO montarDadosArquivoASerRespondido(SqlRowSet dadosSQL, UsuarioVO usuario) {
		ArquivoVO arquivoVO = new ArquivoVO();
		arquivoVO.getTurma().setCodigo(dadosSQL.getInt("codTurma"));
		arquivoVO.getTurma().setIdentificadorTurma(dadosSQL.getString("nomeTurma"));
		arquivoVO.getDisciplina().setCodigo(dadosSQL.getInt("codDisciplina"));
		arquivoVO.getDisciplina().setNome(dadosSQL.getString("nomeDisciplina"));
		arquivoVO.getResponsavelUpload().setCodigo(dadosSQL.getInt("codUsuario"));
		arquivoVO.getResponsavelUpload().setNome(dadosSQL.getString("nomeUsuario"));
		return arquivoVO;
	}

	public List<ArquivoVO> consultarPorCodOrigemTipoOrigem(Integer codigo, String tipoOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM arquivo WHERE codOrigem = " + codigo + " AND origem = '" + tipoOrigem + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public String criarNomeArquivoSetransp(String valorConsultaFiltros, UsuarioVO usuario) {
		return "setransp_" + valorConsultaFiltros + "_" + Uteis.getData(new Date(), "dd-MM-yyyy_hh_mm_ss") + ".csv";
	}

	public void excluirArquivoSetransp(SetranspVO setranspVO, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getSetranspFacade().excluir(setranspVO, usuario);
			Arquivo.excluir(getIdEntidade(), usuario);
			String sqlStr = "DELETE FROM arquivo WHERE ((codigo = " + setranspVO.getArquivo().getCodigo() + "))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sqlStr);
		} catch (Exception e) {
			throw e;
		}
	}

	public ArquivoVO montarArquivo(byte[] arquivo, String nome, String extensao, String origem, UsuarioVO usuario) {
		ArquivoVO arquivoVO = new ArquivoVO();
		arquivoVO.setNome(nome);
		arquivoVO.setExtensao(extensao);
		arquivoVO.setOrigem(origem);
		return arquivoVO;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return Arquivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Arquivo.idEntidade = idEntidade;
	}

	public ArquivoHelper getArquivoHelper() {
		return arquivoHelper;
	}

	public void setArquivoHelper(ArquivoHelper arquivoHelper) {
		this.arquivoHelper = arquivoHelper;
	}

	public PrintWriter getPwArquivo() {
		return pwArquivo;
	}

	public void setPwArquivo(PrintWriter pwArquivo) {
		this.pwArquivo = pwArquivo;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirArquivoNoDiretorioPadrao(ArquivoVO arquivoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		try {
			switch (arquivoVO.getPastaBaseArquivoEnum()) {
			case ARQUIVO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.ARQUIVO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
				break;
			case SERASA_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.SERASA.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.SERASA);
				break;
			case CENSO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.CENSO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
				break;
			case SETRANSP_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.SETRANSP.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.SETRANSP);
				break;
			case IMAGEM_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
				break;
			case REMESSA_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.REMESSA.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REMESSA);
				break;
			case REMESSA_PG_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.REMESSA_PG.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REMESSA_PG);
				break;
			case DOCUMENTOS_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.DOCUMENTOS.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS);
				break;
			case REQUERIMENTOS_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.REQUERIMENTOS.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS);
				break;
			case COMUM_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.COMUM.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.COMUM);
				break;
			case CURSO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.CURSO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CURSO);
				break;
			case ASSINATURA_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.ASSINATURA.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ASSINATURA);
				break;
			case UNIDADEENSINO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.UNIDADEENSINO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.UNIDADEENSINO);
				break;
			case OUVIDORIA_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.OUVIDORIA.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.OUVIDORIA);
				break;
			case OFX_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.OFX.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(arquivoVO, PastaBaseArquivoEnum.OFX.getValue(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()).replace(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), ""));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.OFX);
				break;
			case PERGUNTA_RESPOSTA_ORIGEM_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(arquivoVO, PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM.getValue(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()).replace(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), ""));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM);
				break;
			case ATIVIDADECOMPLEMENTAR_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR);
				break;
			case CERTIFICADO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.CERTIFICADO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CERTIFICADO);
				break;
			case CERTIFICADO_DOCUMENTOS_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS);
				break;
			case CERTIFICADO_WEB_SERVICE_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.CERTIFICADO_WEB_SERVICE.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.CERTIFICADO_WEB_SERVICE.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CERTIFICADO_WEB_SERVICE);
				break;
			case DOCUMENTOS_ASSINADOS_TMP:
				getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS.getValue() + File.separator + UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator + UteisData.getMesData(arquivoVO.getDataUpload()));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS);
				break;
			case ESTAGIO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.ESTAGIO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.ESTAGIO.getValue() + File.separator + UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator + UteisData.getMesData(arquivoVO.getDataUpload()));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ESTAGIO);
				break;
			case IREPORT_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.IREPORT.getValue(), configuracaoGeralSistemaVO);
//				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.IREPORT.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IREPORT);
				break;
			case ARQUIVOSBILIOTECAEXTERNA_TMP:
				getFacadeFactory().getArquivoHelper().AlterarArquivoTMPparaPadrao(arquivoVO, PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA);
				break;	
			case ATIVIDADE_DISCURSIVA_MATERIAL_APOIO_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_MATERIAL_APOIO);
				break;
			case DIGITALIZACAO_GED:
				getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
				break;
			case DIGITALIZACAO_GED_TMP:
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator +  UteisData.getAnoData(arquivoVO.getDataUpload())+ File.separator + UteisData.getMesData(arquivoVO.getDataUpload()));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
				break;
			case CERTIFICADOSINSCRICOES_TMP:				
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadraoArquivosComuns(arquivoVO, PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES);
				break;
			case PROCESSO_SELETIVO_QUESTOES:								
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES.getValue()+"/"+arquivoVO.getCodOrigem());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES);
				break;
			case BANNER_LOGIN_TMP:		
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.BANNER_LOGIN.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.BANNER_LOGIN.getValue());
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.BANNER_LOGIN);
				break;
			case LAYOUT_HISTORICO_TMP:						
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.LAYOUT_HISTORICO.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(arquivoVO.getPastaBaseArquivo().replaceAll(PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP.getValue(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.getValue()));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.LAYOUT_HISTORICO);
				break;
			case LAYOUT_ATA_RESULTADOS_FINAIS_TMP:						
				getFacadeFactory().getArquivoHelper().incluirArquivoNoDiretorioPadrao(arquivoVO, PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.getValue(), configuracaoGeralSistemaVO);
				arquivoVO.setPastaBaseArquivo(arquivoVO.getPastaBaseArquivo().replaceAll(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP.getValue(), PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.getValue()));
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS);
				break;
			default:
				break;
			}
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirArquivoDoDiretorioPadrao(ArquivoVO arquivoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if(arquivoVO.getServidorArquivoOnline().getValor().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
			ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
			servidorArquivoOnlineS3RS.deletarArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), arquivoVO.getDescricao()));
		}else if (configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().equals("")) {
			if (arquivoVO.getDisciplina().getCodigo() > 0) {
				File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator + arquivoVO.getDisciplina().getCodigo() + File.separator + arquivoVO.getNome());
				file.delete();
				file = null;
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue() + File.separator + arquivoVO.getNome());
				file.delete();
				file = null;
			} else {
				File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator + File.separator + arquivoVO.getNome());
				file.delete();
				file = null;
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue() + File.separator + arquivoVO.getNome());
				file.delete();
				file = null;
			}

		} else {
			File file = null;
			if(arquivoVO.getPastaBaseArquivoEnum() == null) {
				arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.obterPastaBaseEnum(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), arquivoVO.getPastaBaseArquivo()));
			}
			if(arquivoVO.getPastaBaseArquivoEnum() != null) {
				file = new File(arquivoVO.obterLocalFisico(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), arquivoVO.getPastaBaseArquivoEnum()));
				if(file.exists() && file.isFile()) {
					file.delete();
				}
			}else if (!arquivoVO.getCpfAlunoDocumentacao().equals("")) {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
			} else if (!arquivoVO.getCpfRequerimento().equals("")) {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + File.separator + arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
			} else if (arquivoVO.getDisciplina().getCodigo() > 0) {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator + arquivoVO.getDisciplina().getCodigo() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
			} else {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue() + File.separator + arquivoVO.getNome());
				if(file.exists() && file.isFile()) {
					file.delete();
				}
				file = null;
			}
		}
		excluirArquivoPastaBaseArquivo(arquivoVO, usuario, configuracaoGeralSistemaVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirArquivoPastaBaseArquivo(ArquivoVO arquivoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				File file = new File(arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirArquivoDoDiretorioEspecifico(ArquivoVO arquivoVO, String caminhoPastaCompleto) throws Exception {
		File file = new File(caminhoPastaCompleto + File.separator + arquivoVO.getNome());
		if (file.exists()) {
			file.delete();
		}
		file = null;
		file = new File(caminhoPastaCompleto + "TMP" + File.separator + arquivoVO.getNome());
		if (file.exists()) {
			file.delete();
		}
		file = null;
	}

	public void validarDadosDisponibilizarMaterialAcademico(ArquivoVO obj) throws Exception {
		// if (obj.getTurma().getCodigo() == 0) {
		// throw new Exception("O campo TURMA deve ser informado.");
		// }
		if (obj.getDisciplina().getCodigo() == 0) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
		if (obj.getDescricaoArquivo().equals("")) {
			throw new Exception("O campo DESCRIÇÃO deve ser informado.");
		}
		if (obj.getPastaBaseArquivoEnum() == null) {
			throw new Exception("Selecione um arquivo para upload.");
		}
		if (!obj.getManterDisponibilizacao() && obj.getDataIndisponibilizacao() == null) {
			throw new Exception("O campo DATA INDISPONIBILIZAÇÃO deve ser informado.");
		}
	}

	public String executarDefinicaoUrlAcessoArquivo(ArquivoVO arquivoVO, PastaBaseArquivoEnum pastaBaseArquivoEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		String caminhoAteDiretorio = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoEnum.getValue() , configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
		if (Uteis.isAtributoPreenchido(pastaBaseArquivoEnum)) {
			String time =  arquivoVO.getNome().endsWith(".pdf") ? "" : "?ch="+arquivoVO.getDataUpload().getTime()+"";
			caminhoAteDiretorio = caminhoAteDiretorio.replace("\\", "/");
			if(caminhoAteDiretorio.endsWith("/")) {
				caminhoAteDiretorio = caminhoAteDiretorio.substring(0, caminhoAteDiretorio.length()-1);
			}
			if (pastaBaseArquivoEnum.getValue().contains("arquivo") && arquivoVO.getDisciplina().getCodigo() > 0) {
				return caminhoAteDiretorio + "/" + arquivoVO.getNome()+time;
			} else {
				if (pastaBaseArquivoEnum.getValue().contains("documentos") && !arquivoVO.getCpfAlunoDocumentacao().equals("")) {
					return caminhoAteDiretorio + "/" + arquivoVO.getNome()+time;
				} else if (pastaBaseArquivoEnum.getValue().contains("requerimentos") && !arquivoVO.getCpfRequerimento().equals("")) {
					return caminhoAteDiretorio + "/" + arquivoVO.getNome()+time;
				} else if (pastaBaseArquivoEnum.getValue().contains("comum")) {
					return caminhoAteDiretorio + "/" + arquivoVO.getNome()+time;
				} else if (pastaBaseArquivoEnum.getValue().contains("ouvidoria")) {
					return caminhoAteDiretorio + "/" + arquivoVO.getNome()+time;
				} else if (pastaBaseArquivoEnum.getValue().contains(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())) {
					return arquivoVO.obterUrlParaDownload(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo(),  pastaBaseArquivoEnum)+time;
				} else {
					return caminhoAteDiretorio + "/" + arquivoVO.getNome()+time;
				}
			}
		}
		return "";
	}

	@Override
	public String executarDefinicaoUrlFisicoAcessoArquivo(ArquivoVO arquivoVO, PastaBaseArquivoEnum pastaBaseArquivoEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		if (Uteis.isAtributoPreenchido(pastaBaseArquivoEnum)) {
			if (pastaBaseArquivoEnum.getValue().contains("arquivo") && arquivoVO.getDisciplina().getCodigo() > 0) {
				return (configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().endsWith("/") ? configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() : configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator) + pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getDisciplina().getCodigo() + "/" + arquivoVO.getNome();
			} else {
				if (pastaBaseArquivoEnum.getValue().contains("documentos") && !arquivoVO.getCpfAlunoDocumentacao().equals("")) {
					return configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao() + File.separator + arquivoVO.getNome();
				} else if (pastaBaseArquivoEnum.getValue().contains("requerimentos") && !arquivoVO.getCpfRequerimento().equals("")) {
					return configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome();
				} else if (pastaBaseArquivoEnum.getValue().contains("comum")) {
					return configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getNivelEducacional() + File.separator + arquivoVO.getNome();
				} else if (pastaBaseArquivoEnum.getValue().contains("ouvidoria")) {
					return configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCodOrigem() + File.separator + arquivoVO.getNome();
				} else {
					return configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getNome();
				}
			}
		}
		return "";
	}

	public StringBuilder getSqlConsultaDadosCompletos() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select distinct arquivo.codigo, arquivo.codOrigem, arquivo.nome, arquivo.apresentarPortalCoordenador, arquivo.apresentarPortalProfessor, arquivo.apresentarPortalAluno, arquivo.descricao, arquivo.descricaoArquivo, arquivo.dataupload, arquivo.dataDisponibilizacao, arquivo.dataIndisponibilizacao, ");
		sqlStr.append("arquivo.manterDisponibilizacao, arquivo.origem,arquivo.situacao, arquivo.controlardownload, arquivo.responsavelupload, arquivo.disciplina, arquivo.turma, arquivo.extensao, arquivo.apresentarDeterminadoPeriodo, ");
		sqlStr.append("arquivo.permitirArquivoResposta, arquivo.pastabasearquivo, arquivo.arquivoResposta, arquivo.professor, arquivo.nivelEducacional, arquivo.cpfAlunoDocumentacao, arquivo.cpfRequerimento, arquivo.servidorArquivoOnline, ");
		sqlStr.append("arquivo.indice, arquivo.agrupador, arquivo.indiceagrupador, arquivo.arquivoAssinadoDigitalmente, arquivo.tipoRelatorio,");
		sqlStr.append("disciplina.codigo as \"disciplina.codigo\",disciplina.nome as \"disciplina.nome\",");
		sqlStr.append("turma.codigo as \"turma.codigo\",turma.identificadorTurma as \"turma.identificadorTurma\",");
		sqlStr.append("usuario.codigo as \"usuario.codigo\",usuario.nome as \"usuario.nome\",");
		sqlStr.append("professor.codigo as \"professor.codigo\",professor.nome as \"professor.nome\", ");
		sqlStr.append("pessoa.codigo as \"pessoa.codigo\",pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" curso.codigo as \"curso.codigo\",curso.nome as \"curso.nome\", arquivo.servidorArquivoOnline, arquivo.arquivoAssinadoFuncionario, arquivo.arquivoAssinadoUnidadeEnsino , arquivo.arquivoAssinadoUnidadeCertificadora ");
		sqlStr.append("from arquivo ");
		sqlStr.append("inner join disciplina on arquivo.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on arquivo.turma = turma.codigo ");
		sqlStr.append("left join usuario on arquivo.responsavelupload = usuario.codigo ");
		sqlStr.append("left join pessoa professor on arquivo.professor = professor.codigo ");
		sqlStr.append("left join pessoa  on arquivo.pessoa = pessoa.codigo ");
		sqlStr.append("left join curso on arquivo.curso = curso.codigo ");
		return sqlStr;
	}

	public StringBuilder getSqlConsultaDadosCompletosArquivosComuns() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select distinct arquivo.codigo, arquivo.codOrigem, arquivo.nome, arquivo.apresentarPortalCoordenador, arquivo.apresentarPortalProfessor, arquivo.apresentarPortalAluno, arquivo.descricao, arquivo.descricaoArquivo, arquivo.dataupload, arquivo.dataDisponibilizacao, arquivo.dataIndisponibilizacao, ");
		sqlStr.append("arquivo.manterDisponibilizacao, arquivo.origem,arquivo.situacao, arquivo.controlardownload, arquivo.responsavelupload, arquivo.disciplina, arquivo.turma, arquivo.extensao, arquivo.apresentarDeterminadoPeriodo, ");
		sqlStr.append("arquivo.permitirArquivoResposta, arquivo.pastabasearquivo, arquivo.arquivoResposta, arquivo.professor, arquivo.nivelEducacional, arquivo.cpfAlunoDocumentacao, arquivo.cpfRequerimento, arquivo.servidorArquivoOnline, ");
		sqlStr.append("arquivo.indice, arquivo.agrupador, arquivo.indiceagrupador, arquivo.arquivoAssinadoDigitalmente, arquivo.tipoRelatorio, ");
		sqlStr.append("disciplina.codigo as \"disciplina.codigo\",disciplina.nome as \"disciplina.nome\",");
		sqlStr.append("turma.codigo as \"turma.codigo\",turma.identificadorTurma as \"turma.identificadorTurma\",");
		sqlStr.append("usuario.codigo as \"usuario.codigo\",usuario.nome as \"usuario.nome\",");
		sqlStr.append("professor.codigo as \"professor.codigo\",professor.nome as \"professor.nome\", ");
		sqlStr.append("pessoa.codigo as \"pessoa.codigo\",pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append("curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", arquivo.servidorArquivoOnline, arquivo.arquivoAssinadoFuncionario, arquivo.arquivoAssinadoUnidadeEnsino , arquivo.arquivoAssinadoUnidadeCertificadora ");
		sqlStr.append("from arquivo ");
		sqlStr.append("left join disciplina on arquivo.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on arquivo.turma = turma.codigo ");
		sqlStr.append("left join usuario on arquivo.responsavelupload = usuario.codigo ");
		sqlStr.append("left join pessoa professor on arquivo.professor = professor.codigo ");
		sqlStr.append("left join pessoa on arquivo.pessoa = pessoa.codigo ");
		sqlStr.append("left join curso on arquivo.curso = curso.codigo ");
		sqlStr.append("left join unidadeEnsino on arquivo.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("left join tipoDocumento on arquivo.tipoDocumento = tipoDocumento.codigo ");
		sqlStr.append("left join departamento on arquivo.departamento = departamento.codigo ");
		return sqlStr;
	}

	// public List<ArquivoVO>
	// consultarArquivoAtivosPorProfessorDisciplinaTurma(List<Integer>
	// professores, Integer turma, Integer disciplina) throws Exception {
	//
	// StringBuilder sqlStr = new StringBuilder("");
	// try {
	// sqlStr.append(getSqlConsultaDadosCompletos());
	// sqlStr.append("where arquivo.situacao = 'AT' ");
	// sqlStr.append("and arquivo.dataDisponibilizacao <=
	// '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' ");
	// sqlStr.append("and (arquivo.dataIndisponibilizacao >=
	// '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' or
	// arquivo.dataIndisponibilizacao is null) ");
	// sqlStr.append(" and arquivo.origem = 'PR' ");
	// sqlStr.append(" AND (turma.codigo = ").append(turma).append(" or
	// arquivo.turma is null) ");
	// sqlStr.append(" and disciplina.codigo = ").append(disciplina).append("
	// ");
	// sqlStr.append(" union all ");
	// sqlStr.append(getSqlConsultaDadosCompletos());
	// sqlStr.append("where arquivo.situacao = 'AT' ");
	// sqlStr.append("and arquivo.dataDisponibilizacao <=
	// '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' ");
	// sqlStr.append("and (arquivo.dataIndisponibilizacao >=
	// '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' or
	// arquivo.dataIndisponibilizacao is null) ");
	// sqlStr.append("and arquivo.origem = 'PR' ");
	// sqlStr.append("and disciplina.codigo = ").append(disciplina).append(" ");
	// sqlStr.append("and arquivo.turma is null ");
	// if (!professores.isEmpty()) {
	// sqlStr.append(" and ( ");
	// for (int i = 0; i < professores.size(); i++) {
	// sqlStr.append(" arquivo.professor =
	// ").append(professores.get(i)).append(" ");
	// if (i < professores.size() - 1) {
	// sqlStr.append(" OR ");
	// }
	// }
	// sqlStr.append(" ) ");
	// }
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	// return (montarDadosConsultaCompleta(tabelaResultado));
	// } finally {
	// sqlStr = null;
	// }
	// }
	// public List<ArquivoVO>
	// consultarArquivosAtivosPorDisciplinaTurmaInstituicao(List<Integer>
	// professores, Integer turma, Integer disciplina) throws Exception {
	// StringBuilder sqlStr = new StringBuilder("");
	// try {
	// sqlStr.append(getSqlConsultaDadosCompletos());
	// sqlStr.append(" where arquivo.situacao = 'AT' and
	// arquivo.dataDisponibilizacao <= '").append(Uteis.getDataJDBCTimestamp(new
	// Date())).append("' and (arquivo.dataIndisponibilizacao >=
	// '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' or
	// arquivo.dataIndisponibilizacao is null) ");
	// sqlStr.append(" AND arquivo.origem = 'IN' ");
	// sqlStr.append(" AND (turma.codigo = ").append(turma).append(" or
	// arquivo.turma is null) ");
	// sqlStr.append(" AND disciplina.codigo = ").append(disciplina).append("
	// ");
	// sqlStr.append(" AND arquivo.professor is null ");
	// sqlStr.append(" union all ");
	// sqlStr.append(getSqlConsultaDadosCompletos());
	// sqlStr.append(" where arquivo.situacao = 'AT' and
	// arquivo.dataDisponibilizacao <= '").append(Uteis.getDataJDBCTimestamp(new
	// Date())).append("' and (arquivo.dataIndisponibilizacao >=
	// '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' or
	// arquivo.dataIndisponibilizacao is null) ");
	// sqlStr.append(" AND arquivo.origem = 'IN' ");
	// sqlStr.append(" AND (turma.codigo = ").append(turma).append(" or
	// arquivo.turma is null) ");
	// sqlStr.append(" AND disciplina.codigo = ").append(disciplina).append("
	// ");
	// if (!professores.isEmpty()) {
	// sqlStr.append(" and ( ");
	// for (int i = 0; i < professores.size(); i++) {
	// sqlStr.append(" arquivo.professor =
	// ").append(professores.get(i)).append(" ");
	// if (i < professores.size() - 1) {
	// sqlStr.append(" OR ");
	// }
	// }
	// sqlStr.append(" ) ");
	// }
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	// return (montarDadosConsultaCompleta(tabelaResultado));
	// } finally {
	// sqlStr = null;
	// }
	// }
	public List<ArquivoVO> consultarArquivoAtivosPorMatriculaDisciplinaAnoSemestreOrigem(String matricula, Integer disciplina, String ano, String semestre, String origem) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select arquivo.*, exists(select download.codigo from download inner join matriculaperiodo on matriculaperiodo.codigo = download.matriculaperiodo where download.arquivo = arquivo.codigo and matriculaperiodo.matricula = '").append(matricula).append("') as selecionado from (");
		sqlStr.append(getSqlConsultaDadosCompletos());
		sqlStr.append("left join (");
		sqlStr.append("	select distinct turma, turmaagrupada, disciplina, professor, anovigente, semestrevigente from horarioturma");
		sqlStr.append("	inner join turma on turma.codigo = horarioturma.turma");
		sqlStr.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sqlStr.append("	where 1=1");
		sqlStr.append("	and turma in (");
		sqlStr.append("		select turmapratica from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append(realizarRegraAndAnoSemestre(ano, semestre, "matriculaperiodoturmadisciplina"));
		sqlStr.append("		and turmapratica is not null");
		sqlStr.append("		union");
		sqlStr.append("		select turmateorica from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append(realizarRegraAndAnoSemestre(ano, semestre, "matriculaperiodoturmadisciplina"));
		sqlStr.append("		and turmateorica is not null");
		sqlStr.append("		union");
		sqlStr.append("		select turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append(realizarRegraAndAnoSemestre(ano, semestre, "matriculaperiodoturmadisciplina"));
		sqlStr.append("		and turmateorica is null");
		sqlStr.append("		and turmapratica is null");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append(realizarRegraAndAnoSemestre(ano, semestre, "matriculaperiodoturmadisciplina"));
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append(realizarRegraAndAnoSemestre(ano, semestre, "matriculaperiodoturmadisciplina"));
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and turmateorica is null and turmapratica is null");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append(realizarRegraAndAnoSemestre(ano, semestre, "matriculaperiodoturmadisciplina"));
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("	) ");
		sqlStr.append("	and ((turma.turmaagrupada = false and disciplina = ").append(disciplina).append(")");
		sqlStr.append("		or (turma.turmaagrupada and disciplina in (");
		sqlStr.append("			select ").append(disciplina);
		sqlStr.append("			union select disciplina from disciplinaequivalente  where equivalente = ").append(disciplina);
		sqlStr.append("			union select equivalente from disciplinaequivalente  where disciplina  = ").append(disciplina);
		sqlStr.append("		)");
		sqlStr.append("	))");
		sqlStr.append(" union all");
		sqlStr.append(" select turma.codigo as turma,");
		sqlStr.append(" turma.turmaagrupada,");
		sqlStr.append(" matriculaperiodoturmadisciplina.disciplina,");
		sqlStr.append(" matriculaperiodoturmadisciplina.professor,");
		sqlStr.append(" matriculaperiodoturmadisciplina.ano as anovigente,");
		sqlStr.append(" matriculaperiodoturmadisciplina.semestre as semestrevigente");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join turma on turma.codigo =  matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma =  turma.codigo");
		sqlStr.append(" and turmadisciplina.disciplina =  matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		sqlStr.append(" and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'");
		sqlStr.append(" and turmadisciplina.definicoestutoriaonline = 'DINAMICA'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.professor is not null");
		sqlStr.append(") as turmaprofessor on turmaprofessor.disciplina = arquivo.disciplina ");
		sqlStr.append("where arquivo.situacao = 'AT' ");
		if (Uteis.isAtributoPreenchido(ano) || Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append("and ((turmaprofessor.anovigente = '").append(ano).append("' ");
			sqlStr.append("and turmaprofessor.semestrevigente = '").append(semestre).append("' and arquivo.manterdisponibilizacao = false) or (arquivo.manterdisponibilizacao ))");
		}
		sqlStr.append("and arquivo.dataDisponibilizacao <= current_timestamp ");
		sqlStr.append("and case when arquivo.manterdisponibilizacao = false ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = false then false when arquivo.manterdisponibilizacao = false ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = true ");
		sqlStr.append("	then (arquivo.dataIndisponibilizacao >= current_timestamp or arquivo.dataIndisponibilizacao is null)");
		sqlStr.append("	when arquivo.manterdisponibilizacao = true ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = false then true end  ");
		sqlStr.append("and arquivo.origem = '").append(origem).append("' ");
		sqlStr.append("and (");
		sqlStr.append("	  ((arquivo.disciplina = ").append(disciplina).append(" OR arquivo.disciplina = turmaprofessor.disciplina) and arquivo.turma is null and arquivo.professor is null) ");
		sqlStr.append("or ((arquivo.disciplina = ").append(disciplina).append(" OR arquivo.disciplina = turmaprofessor.disciplina) and arquivo.professor is null and arquivo.turma = turmaprofessor.turma) ");
		sqlStr.append("or ((arquivo.disciplina = ").append(disciplina).append(" OR arquivo.disciplina = turmaprofessor.disciplina) and arquivo.professor = turmaprofessor.professor and arquivo.turma = turmaprofessor.turma) ");
		sqlStr.append("or ((arquivo.disciplina = ").append(disciplina).append(" OR arquivo.disciplina = turmaprofessor.disciplina) and arquivo.professor = turmaprofessor.professor and arquivo.turma is null) ");
		sqlStr.append(") ");
		sqlStr.append(") as arquivo ");		
		sqlStr.append(" order by arquivo.disciplina, arquivo.turma, arquivo.professor, indiceagrupador, indice");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ArquivoVO> vetResultado = new ArrayList<ArquivoVO>(0);
		while (tabelaResultado.next()) {
			ArquivoVO arquivoVO = montarDadosCompleto(tabelaResultado);
			arquivoVO.setSelecionado(tabelaResultado.getBoolean("selecionado"));
			vetResultado.add(arquivoVO);
		}
		return vetResultado;		
	}
	
	public List<ArquivoVO> consultarArquivoAtivosPorDisciplinaTurmaVisaoCoordenador(Integer turma, Integer disciplina) throws Exception {
		return consultarArquivoAtivosPorDisciplinaTurmaVisaoCoordenador(turma, disciplina, null,"","","","","");
	}

	public List<ArquivoVO> consultarArquivoAtivosPorDisciplinaTurmaVisaoCoordenador(Integer turma, Integer disciplina, Integer unidadeEnsino  , String campoConsultaSituacao, String anoPeriodoUpload, String semestrePeriodoUpload, String anoPeriodoDisponibilizacao, String semestrePeriodoDisponibilizacao) throws Exception{

		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletos());
			sqlStr.append("where arquivo.situacao = 'AT' ");
			sqlStr.append("and arquivo.origem = 'PR' ");
			if (turma != null && turma != 0) {
				sqlStr.append(" AND ((turma.codigo = ");
				sqlStr.append(turma);
				sqlStr.append(" OR turma.codigo in(select distinct turma from turmaagrupada where turmaorigem = ").append(turma).append("))");
				sqlStr.append(" OR arquivo.turma is null)");
			}
			if (unidadeEnsino != null && unidadeEnsino != 0) {
				sqlStr.append(" AND (turma.unidadeEnsino = ");
				sqlStr.append(unidadeEnsino);
				sqlStr.append(" OR arquivo.turma is null)");
			}
			/**
			 * Foi adicionado o sql abaixo, para atender as turmas agrupadas através das
			 * disciplinas equivalentes definidas no cadastro da disciplina: Ex: caso uma
			 * turma agrupada X tenha as seguintes turmas Y e Z onde a disciplina na turma Y
			 * é de código 800 e a disciplina na turma Z é de código 801 e a disciplina na
			 * turma agrupada X é 800, estes agrupamento se torna possível devido ao
			 * agrupamento realizado no cadastro da disciplina onde vincula a disciplina 800
			 * com a 801, então os alunos que estão matriculados na disciplina 801 devem ter
			 * acesso ao arquivo disponibilizado na disciplina 800. Porém este só vai
			 * filtrar os arquivos que tem vinculo com turma, para os demais casos o arquivo
			 * deve ser disponibilizado para a disciplina correta do aluno
			 */
			sqlStr.append(" and (case when turma.turmaagrupada then (disciplina.codigo = ").append(disciplina).append(" ");
			sqlStr.append(" or  (disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = " + disciplina + ") ");
			sqlStr.append(" and disciplina.codigo in (select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = turma.codigo ) )) ");
			sqlStr.append(" else disciplina.codigo = ").append(disciplina).append(" end) ");
			
			if(!campoConsultaSituacao.equals("")){
				if (campoConsultaSituacao.equals("ativo")) {
					sqlStr.append(" AND (arquivo.situacao = 'AT' AND ((not arquivo.manterDisponibilizacao and arquivo.apresentardeterminadoperiodo and arquivo.dataIndisponibilizacao is not null ");
					sqlStr.append(" and arquivo.dataIndisponibilizacao > now()) or arquivo.manterDisponibilizacao)) ");
				} else if (campoConsultaSituacao.equals("inativo")) {
					sqlStr.append(" AND (arquivo.situacao = 'IN' or (not arquivo.manterDisponibilizacao and arquivo.apresentardeterminadoperiodo and arquivo.dataIndisponibilizacao is not null and arquivo.dataIndisponibilizacao <= now())) ");	
				}
			}
			if(Uteis.isAtributoPreenchido(anoPeriodoUpload)   &&  Uteis.isAtributoPreenchido(semestrePeriodoUpload)) {
				Map<String, Date> resultado  =	UteisData.getDataInicialFinalPeriodoSemestral(Integer.parseInt(anoPeriodoUpload),Integer.parseInt(semestrePeriodoUpload));
				sqlStr.append(" AND (( arquivo.dataUpload >= '" + Uteis.getDataJDBC(resultado.get("dataInicial")) + "') and (arquivo.dataUpload <= '" + Uteis.getDataJDBC(resultado.get("dataFinal")) + "'))");

			}
			if(Uteis.isAtributoPreenchido(anoPeriodoDisponibilizacao)  &&  Uteis.isAtributoPreenchido(semestrePeriodoDisponibilizacao)) {
				Map<String, Date> resultado  =	UteisData.getDataInicialFinalPeriodoSemestral(Integer.parseInt(anoPeriodoDisponibilizacao),Integer.parseInt(semestrePeriodoDisponibilizacao));
				sqlStr.append(" AND (( arquivo.datadisponibilizacao >= '" + Uteis.getDataJDBC(resultado.get("dataInicial")) + "') and (arquivo.datadisponibilizacao <= '" + Uteis.getDataJDBC(resultado.get("dataFinal")) + "'))");

			}
			
			sqlStr.append(" ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}

	public List<ArquivoVO> consultarArquivosAtivosPorDisciplinaTurmaInstituicaoVisaoCoordenador(Integer turma, Integer disciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletos());
			sqlStr.append(" where arquivo.situacao = 'AT'  ");
			sqlStr.append(" AND arquivo.origem = 'IN' ");
			if (turma != null && turma != 0) {
				sqlStr.append(" AND ((turma.codigo = ");
				sqlStr.append(turma);
				sqlStr.append(" OR turma.codigo in(select distinct turma from turmaagrupada where turmaorigem = ").append(turma).append("))");
				sqlStr.append(" OR arquivo.turma is null)");
			}
			/**
			 * Foi adicionado o sql abaixo, para atender as turmas agrupadas através das
			 * disciplinas equivalentes definidas no cadastro da disciplina: Ex: caso uma
			 * turma agrupada X tenha as seguintes turmas Y e Z onde a disciplina na turma Y
			 * é de código 800 e a disciplina na turma Z é de código 801 e a disciplina na
			 * turma agrupada X é 800, estes agrupamento se torna possível devido ao
			 * agrupamento realizado no cadastro da disciplina onde vincula a disciplina 800
			 * com a 801, então os alunos que estão matriculados na disciplina 801 devem ter
			 * acesso ao arquivo disponibilizado na disciplina 800. Porém este só vai
			 * filtrar os arquivos que tem vinculo com turma, para os demais casos o arquivo
			 * deve ser disponibilizado para a disciplina correta do aluno
			 */
			sqlStr.append(" and (case when turma.turmaagrupada then (disciplina.codigo = ").append(disciplina).append(" ");
			sqlStr.append(" or  (disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = " + disciplina + ") ");
			sqlStr.append(" and disciplina.codigo in (select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = turma.codigo ) )) ");
			sqlStr.append(" else disciplina.codigo = ").append(disciplina).append(" end) ");
			sqlStr.append(" ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}

	@Override
	public List<ArquivoVO> consultarArquivoAtivosPorProfessorDisciplinaTurmaVisaoProfessor(Integer professor,Integer turma, Integer disciplina, String campoConsultaSituacao, String anoPeriodoUpload,
			String semestrePeriodoUpload, String anoPeriodoDisponibilizacao, String semestrePeriodoDisponibilizacao)throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletos());
			//sqlStr.append("where arquivo.situacao = 'AT' ");
			sqlStr.append("where arquivo.origem = 'PR' ");
			if (turma != null && turma != 0) {
				sqlStr.append(" AND ((turma.codigo = ");
				sqlStr.append(turma);
				sqlStr.append(" OR turma.codigo in(select distinct turma from turmaagrupada where turmaorigem = ").append(turma).append("))");
				sqlStr.append(" OR arquivo.turma is null)");
			}

			/**
			 * Foi adicionado o sql abaixo, para atender as turmas agrupadas através das
			 * disciplinas equivalentes definidas no cadastro da disciplina: Ex: caso uma
			 * turma agrupada X tenha as seguintes turmas Y e Z onde a disciplina na turma Y
			 * é de código 800 e a disciplina na turma Z é de código 801 e a disciplina na
			 * turma agrupada X é 800, estes agrupamento se torna possível devido ao
			 * agrupamento realizado no cadastro da disciplina onde vincula a disciplina 800
			 * com a 801, então os alunos que estão matriculados na disciplina 801 devem ter
			 * acesso ao arquivo disponibilizado na disciplina 800. Porém este só vai
			 * filtrar os arquivos que tem vinculo com turma, para os demais casos o arquivo
			 * deve ser disponibilizado para a disciplina correta do aluno
			 */
			sqlStr.append(" and (case when turma.turmaagrupada then (disciplina.codigo = ").append(disciplina).append(" ");
			sqlStr.append(" or  (disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = " + disciplina + ") ");
			sqlStr.append(" and disciplina.codigo in (select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = turma.codigo ) )) ");
			sqlStr.append(" else disciplina.codigo = ").append(disciplina).append(" end) ");
			sqlStr.append(" and (arquivo.professor = ").append(professor).append(" or arquivo.professor is null)");
			
			if(!campoConsultaSituacao.equals("")){
				if (campoConsultaSituacao.equals("ativo")) {
					sqlStr.append(" AND (arquivo.situacao = 'AT' AND ((not arquivo.manterDisponibilizacao and arquivo.apresentardeterminadoperiodo and arquivo.dataIndisponibilizacao is not null ");
					sqlStr.append(" and arquivo.dataIndisponibilizacao > now()) or arquivo.manterDisponibilizacao)) ");
				} else if (campoConsultaSituacao.equals("inativo")) {
					sqlStr.append(" AND (arquivo.situacao = 'IN' or (not arquivo.manterDisponibilizacao and arquivo.apresentardeterminadoperiodo and arquivo.dataIndisponibilizacao is not null and arquivo.dataIndisponibilizacao <= now())) ");	
				}
			}
			if(Uteis.isAtributoPreenchido(anoPeriodoUpload)   && Uteis.isAtributoPreenchido(semestrePeriodoUpload)) {
				Map<String, Date> resultado  =	UteisData.getDataInicialFinalPeriodoSemestral(Integer.parseInt(anoPeriodoUpload),Integer.parseInt(semestrePeriodoUpload));
				sqlStr.append(" AND (( arquivo.dataUpload >= '" + Uteis.getDataJDBC(resultado.get("dataInicial")) + "') and (arquivo.dataUpload <= '" + Uteis.getDataJDBC(resultado.get("dataFinal")) + "'))");

			}
			if( Uteis.isAtributoPreenchido(anoPeriodoDisponibilizacao) &&  Uteis.isAtributoPreenchido(semestrePeriodoDisponibilizacao)) {
				Map<String, Date> resultado  =	UteisData.getDataInicialFinalPeriodoSemestral(Integer.parseInt(anoPeriodoDisponibilizacao),Integer.parseInt(semestrePeriodoDisponibilizacao));
				sqlStr.append(" AND (( arquivo.datadisponibilizacao >= '" + Uteis.getDataJDBC(resultado.get("dataInicial")) + "') and (arquivo.datadisponibilizacao <= '" + Uteis.getDataJDBC(resultado.get("dataFinal")) + "'))");

			}
			
			
			sqlStr.append(" ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}

	public List<ArquivoVO> consultarArquivosAtivosPorDisciplinaTurmaInstituicaoVisaoProfessor(Integer professor, Integer turma, Integer disciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletos());
			sqlStr.append(" where arquivo.situacao = 'AT'  ");
			sqlStr.append(" AND arquivo.origem = 'IN' ");
			if (turma != null && turma != 0) {
				sqlStr.append(" AND ((turma.codigo = ");
				sqlStr.append(turma);
				sqlStr.append(" OR turma.codigo in(select distinct turma from turmaagrupada where turmaorigem = ").append(turma).append("))");
				sqlStr.append(" OR arquivo.turma is null)");
			}
			/**
			 * Foi adicionado o sql abaixo, para atender as turmas agrupadas através das
			 * disciplinas equivalentes definidas no cadastro da disciplina: Ex: caso uma
			 * turma agrupada X tenha as seguintes turmas Y e Z onde a disciplina na turma Y
			 * é de código 800 e a disciplina na turma Z é de código 801 e a disciplina na
			 * turma agrupada X é 800, estes agrupamento se torna possível devido ao
			 * agrupamento realizado no cadastro da disciplina onde vincula a disciplina 800
			 * com a 801, então os alunos que estão matriculados na disciplina 801 devem ter
			 * acesso ao arquivo disponibilizado na disciplina 800. Porém este só vai
			 * filtrar os arquivos que tem vinculo com turma, para os demais casos o arquivo
			 * deve ser disponibilizado para a disciplina correta do aluno
			 */
			sqlStr.append(" and (case when turma.turmaagrupada then (disciplina.codigo = ").append(disciplina).append(" ");
			sqlStr.append(" or  (disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = " + disciplina + ") ");
			sqlStr.append(" and disciplina.codigo in (select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = turma.codigo ) )) ");
			sqlStr.append(" else disciplina.codigo = ").append(disciplina).append(" end) ");
			sqlStr.append(" and (arquivo.professor = ");
			sqlStr.append(professor);
			sqlStr.append(" or arquivo.professor is null) ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}

	@Override
	public List<ArquivoVO> consultarArquivosPorSituacaoPorNivelEducacional(String nivelEducacional, SituacaoArquivo situacaoArquivo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			if (situacaoArquivo.equals(SituacaoArquivo.ATIVO)) {
				sqlStr.append(" where arquivo.situacao = '").append(SituacaoArquivo.ATIVO.getValor()).append("' ");
			} else {
				sqlStr.append(" where arquivo.situacao = '").append(SituacaoArquivo.INATIVO.getValor()).append("' ");
			}
			sqlStr.append(" AND (arquivo.nivelEducacional = '");
			sqlStr.append(nivelEducacional);
			sqlStr.append("') ");
			sqlStr.append(" ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}


	@Override
	public List<ArquivoVO> consultarArquivosPorSituacaoInstitucional(ArquivoVO arquivoVO, SituacaoArquivo situacaoArquivo, boolean apresentarVisaoAluno, boolean apresentarVisaoProfessor, boolean apresentarVisaoCoordenador) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			sqlStr.append(" where arquivo.situacao = '").append(situacaoArquivo.getValor()).append("' ");
			sqlStr.append(" and origem = 'IN' ");
			if (arquivoVO.getNivelEducacional() != null && !arquivoVO.getNivelEducacional().trim().isEmpty() && !arquivoVO.getNivelEducacional().trim().equals("0")) {
				sqlStr.append(" AND (arquivo.nivelEducacional = '");
				sqlStr.append(arquivoVO.getNivelEducacional());
				sqlStr.append("') ");
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getCurso().getCodigo()) && arquivoVO.getCurso().getCodigo() > 0) {
				sqlStr.append(" AND curso.codigo = ").append(arquivoVO.getCurso().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getUnidadeEnsinoVO().getCodigo()) && arquivoVO.getUnidadeEnsinoVO().getCodigo() > 0) {
				sqlStr.append(" AND unidadeEnsino.codigo = ").append(arquivoVO.getUnidadeEnsinoVO().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getModuloDisponibilizarMaterial())) {
				sqlStr.append(" AND arquivo.moduloDisponibilizarMaterial = '").append(arquivoVO.getModuloDisponibilizarMaterial().getValor()).append("' ");
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getTipoDocumentoVO().getCodigo()) && arquivoVO.getTipoDocumentoVO().getCodigo() > 0) {
				sqlStr.append(" AND tipoDocumento.codigo = ").append(arquivoVO.getTipoDocumentoVO().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getDepartamentoVO().getCodigo()) && arquivoVO.getDepartamentoVO().getCodigo() > 0) {
				sqlStr.append(" AND departamento.codigo = ").append(arquivoVO.getDepartamentoVO().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getTurma().getCodigo()) && arquivoVO.getTurma().getCodigo() > 0) {
				sqlStr.append(" AND turma.codigo = ").append(arquivoVO.getTurma().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getDisciplina().getCodigo()) && arquivoVO.getDisciplina().getCodigo() > 0) {
				sqlStr.append(" AND disciplina.codigo = ").append(arquivoVO.getDisciplina().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getProfessor().getCodigo()) && arquivoVO.getProfessor().getCodigo() > 0) {
				sqlStr.append(" AND pessoa.codigo = ").append(arquivoVO.getProfessor().getCodigo());
			}

//			COMENTADO PORQUE A PARTIR DE AGORA 10/10/2019 QUANDO UM ARQUIVO É ASSINADO DIGITALMENTE NÃO É MARCADO NENHUMA OPÇÃO ATÉ O FUNCIONÁRIO ASSINAR O DOCUMENTO.
//			sqlStr.append(" AND ( apresentarPortalAluno = ").append(true);
//			sqlStr.append(" or apresentarPortalCoordenador = ").append(true);
//			sqlStr.append(" or apresentarPortalProfessor = ").append(true).append(") ");

			sqlStr.append(" ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}

	public Boolean verificarDataDownloadDentroQuantidadeDiasMaximoLimite(String matricula, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina) throws Exception {
		Date dataAula = null;
		Integer qtdeDiasLimiteDownload = null;
		try {
			// dataAula =
			// getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(turma.getCodigo(),
			// disciplina.getCodigo());
			// dataAula =
			// getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAnteriorDataAtual(turma.getCodigo());
			dataAula = getFacadeFactory().getHorarioTurmaFacade().consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(matricula);
			if (dataAula != null) {
				if (curso.getLimitarQtdeDiasMaxDownload()) {
					qtdeDiasLimiteDownload = curso.getQtdeMaxDiasDownload();
				} else {
					qtdeDiasLimiteDownload = curso.getConfiguracaoAcademico().getQtdeMaxDiasDownload();
				}
				if (qtdeDiasLimiteDownload < Uteis.nrDiasEntreDatas(new Date(), dataAula)) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<ArquivoVO> consultarArquivoInstituicionalParaProfessor(UsuarioVO usuarioVO, Integer limit) throws Exception {
		if(Uteis.isAtributoPreenchido(usuarioVO) && Uteis.isAtributoPreenchido(usuarioVO.getPessoa())) {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			sqlStr.append(" WHERE arquivo.origem = 'IN' AND arquivo.nivelEducacional <> '' AND arquivo.situacao = 'AT' and apresentarPortalProfessor = true ");
			sqlStr.append(" AND arquivo.professor = ").append(usuarioVO.getPessoa().getCodigo()).append(" ");
			
			sqlStr.append(" UNION ALL ");
			
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			sqlStr.append(" INNER JOIN LATERAL ( ");
			sqlStr.append(" WITH cte_horario AS  ( ");
			sqlStr.append(" SELECT ");
			sqlStr.append("   distinct ht.codigo,c.nivelEducacional,htdi.disciplina,c.codigo AS curso,t.codigo AS turma ");
			sqlStr.append(" FROM horarioturma ht ");
			sqlStr.append("  INNER JOIN horarioturmadia htd 	  ON ht.codigo 	 = htd.horarioturma ");
			sqlStr.append("  INNER JOIN horarioturmadiaitem htdi  ON htd.codigo  = htdi.horarioturmadia ");
			sqlStr.append("  INNER JOIN turma AS t				  ON t.codigo 	 = ht.turma ");
			sqlStr.append("	inner join curso as c on t.turmaagrupada = false and t.curso = c.codigo "); 
			sqlStr.append(" WHERE htdi.professor = ").append(usuarioVO.getPessoa().getCodigo()).append(" ");
			sqlStr.append(" UNION ALL ");
			sqlStr.append(" SELECT ");
			sqlStr.append("   distinct ht.codigo,c.nivelEducacional,htdi.disciplina,c.codigo AS curso,t.codigo AS turma ");
			sqlStr.append(" FROM horarioturma ht ");
			sqlStr.append("  INNER JOIN horarioturmadia htd 	  ON ht.codigo 	 = htd.horarioturma ");
			sqlStr.append("  INNER JOIN horarioturmadiaitem htdi  ON htd.codigo  = htdi.horarioturmadia ");
			sqlStr.append("  INNER JOIN turma AS t				  ON t.codigo 	 = ht.turma ");
			sqlStr.append("	inner join curso as c on t.turmaagrupada "); 
			sqlStr.append("	inner join turmaagrupada ta on ta.turmaorigem = t.codigo ");
			sqlStr.append("	inner join turma as tur on tur.codigo = ta.turma and c.codigo = tur.curso ");
			sqlStr.append(" WHERE htdi.professor = ").append(usuarioVO.getPessoa().getCodigo()).append(" ");
			sqlStr.append(" ) ");
			
			sqlStr.append(" SELECT  ");
			sqlStr.append("  cte_horario.codigo  ");
			sqlStr.append(" FROM cte_horario ");
			sqlStr.append(" WHERE (arquivo.nivelEducacional = cte_horario.nivelEducacional AND arquivo.curso is NULL AND arquivo.turma is NULL AND arquivo.disciplina is NULL AND arquivo.professor is null) ");
			sqlStr.append(" UNION ALL ");

			sqlStr.append(" SELECT  ");
	        sqlStr.append("  cte_horario.codigo ");
	        sqlStr.append(" from cte_horario ");
			sqlStr.append(" WHERE (arquivo.curso = cte_horario.curso AND arquivo.turma is NULL and arquivo.disciplina is NULL AND arquivo.professor is NULL) ");
			
		    sqlStr.append(" UNION ALL "); 
			
			sqlStr.append(" SELECT  ");
	        sqlStr.append("  cte_horario.codigo ");
	        sqlStr.append(" FROM cte_horario ");
			sqlStr.append(" WHERE (arquivo.curso = cte_horario.curso AND arquivo.turma = cte_horario.turma and arquivo.disciplina is NULL AND arquivo.professor is NULL) ");
			
		    sqlStr.append(" UNION ALL ");
			
			sqlStr.append(" SELECT "); 
	        sqlStr.append("  cte_horario.codigo ");
	        sqlStr.append(" FROM cte_horario ");
			sqlStr.append(" WHERE (arquivo.curso = cte_horario.curso AND arquivo.turma is NULL AND arquivo.disciplina = cte_horario.disciplina AND arquivo.professor is NULL) ");
			
			sqlStr.append(" UNION ALL ");
			
			sqlStr.append(" SELECT "); 
	        sqlStr.append("  cte_horario.codigo ");
	        sqlStr.append(" FROM cte_horario ");
			sqlStr.append(" WHERE (arquivo.curso is NULL AND arquivo.turma = cte_horario.turma and arquivo.disciplina = cte_horario.disciplina AND arquivo.professor is NULL) ");
			
			sqlStr.append(" UNION ALL ");
			
			sqlStr.append(" SELECT ");
	        sqlStr.append("  cte_horario.codigo ");
	        sqlStr.append(" FROM cte_horario");
			sqlStr.append(" WHERE  (arquivo.curso is NULL AND arquivo.turma = cte_horario.turma AND arquivo.disciplina is NULL AND arquivo.professor is NULL) ");
			
			sqlStr.append(" UNION ALL ");
			
			sqlStr.append(" SELECT  ");
	        sqlStr.append("  cte_horario.codigo ");
	        sqlStr.append(" FROM cte_horario ");
			sqlStr.append(" WHERE  (arquivo.curso is NULL AND arquivo.turma is NULL AND arquivo.disciplina = cte_horario.disciplina AND arquivo.professor IS NULL) ");

			sqlStr.append(" ) AS temp_arquivos ON 1=1 ");
			
			sqlStr.append(" WHERE arquivo.origem = 'IN' ");
			sqlStr.append(" AND arquivo.nivelEducacional <> '' AND arquivo.situacao = 'AT' AND apresentarPortalProfessor = true ");
			sqlStr.append(" AND temp_arquivos.codigo IS NOT NULL ");
			sqlStr.append(" AND COALESCE(arquivo.professor,0) <> ").append(usuarioVO.getPessoa().getCodigo()).append(" ");
 		
			sqlStr.append(" ORDER BY dataupload DESC ");
			if (limit != null && limit > 0) {
				sqlStr.append(" LIMIT ").append(limit);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
		}else {
			return new ArrayList<ArquivoVO>(0);
	}
	}

	@Override
	public List<ArquivoVO> consultarArquivoInstituicionalParaAluno(UsuarioVO usuarioVO, String matricula, Integer limit) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			sqlStr.append(" where arquivo.origem = 'IN' and arquivo.situacao = 'AT' and apresentarPortalAluno = true ");
			sqlStr.append(" and ( ");
			sqlStr.append("       (arquivo.nivelEducacional is not null and arquivo.nivelEducacional in ( select distinct nivelEducacional from matricula inner join curso on curso.codigo = matricula.curso where matricula.matricula = '").append(matricula).append("')) ");
			sqlStr.append("        or arquivo.nivelEducacional is null  ");
			sqlStr.append("      ) ");
			sqlStr.append(" and ( ");
			sqlStr.append("       (arquivo.curso is not null and arquivo.curso in ( select distinct curso from matricula where matricula.matricula = '").append(matricula).append("')) ");
			sqlStr.append("        or arquivo.curso is null ");
			sqlStr.append("      ) ");
			sqlStr.append(" and ( ");
			sqlStr.append("        ( arquivo.turma is not null and arquivo.disciplina is not null ");
			sqlStr.append("          and exists (select distinct matriculaperiodoturmadisciplina.codigo from matricula ");
			sqlStr.append("          inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula where matricula.matricula = '").append(matricula).append("' ");
			sqlStr.append("          and matriculaperiodoturmadisciplina.disciplina = arquivo.disciplina");
			sqlStr.append("          and matriculaperiodoturmadisciplina.turma = arquivo.turma) ");
			sqlStr.append("        ) ");
			sqlStr.append("        or ");
			sqlStr.append("        ( arquivo.turma is not null and arquivo.disciplina is null ");
			sqlStr.append("          and exists (select distinct matriculaperiodoturmadisciplina.codigo from matricula ");
			sqlStr.append("          inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula where matricula.matricula = '").append(matricula).append("' ");
			sqlStr.append("          and matriculaperiodoturmadisciplina.turma = arquivo.turma)");
			sqlStr.append("         ) ");
			sqlStr.append("         or");
			sqlStr.append("         ( arquivo.turma is null and arquivo.disciplina is not null ");
			sqlStr.append("           and exists (select distinct matriculaperiodoturmadisciplina.codigo from matricula ");
			sqlStr.append("           inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula where matricula.matricula = '").append(matricula).append("' ");
			sqlStr.append("           and matriculaperiodoturmadisciplina.disciplina = arquivo.disciplina)");
			sqlStr.append("         ) ");
			sqlStr.append("         or (arquivo.turma is null and arquivo.disciplina is null)");
			sqlStr.append("  ) ");
			sqlStr.append(" ORDER BY dataupload desc ");
			if (limit != null && limit > 0) {
				sqlStr.append(" limit ").append(limit);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}
	}

	public List<ArquivoVO> consultarArquivoInstituicionalParaCoordenador(UsuarioVO usuarioVO, Integer limit) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		try {
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			sqlStr.append(" where arquivo.origem = 'IN' and arquivo.situacao = 'AT' and apresentarPortalCoordenador = true  ");
			// Verifica arquivos do nivel educacional dos cursos que o mesmo é
			// coordenador
			sqlStr.append(" and ( (arquivo.curso is null and arquivo.turma is null  ");
			sqlStr.append(" and arquivo.disciplina is null and arquivo.professor is null ");
			sqlStr.append(" and arquivo.nivelEducacional in (select distinct nivelEducacional from cursocoordenador ");
			sqlStr.append(" inner join curso on curso.codigo = cursocoordenador.curso  ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sqlStr.append(" where funcionario.pessoa = " + usuarioVO.getPessoa().getCodigo() + " ) ");
			sqlStr.append(" ) or  ");
			// Verifica arquivos os cursos que o mesmo é coordenador
			sqlStr.append(" (arquivo.turma is null ");
			sqlStr.append(" and arquivo.disciplina is null and arquivo.professor is null ");
			sqlStr.append(" and arquivo.curso in (select distinct curso from cursocoordenador ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sqlStr.append(" where funcionario.pessoa = " + usuarioVO.getPessoa().getCodigo() + " ) ");
			sqlStr.append(" ) ");
			sqlStr.append(" or  ");
			// Verifica arquivos os cursos que o mesmo é coordenador			
			sqlStr.append(" ( arquivo.disciplina is null and arquivo.professor is null ");
			sqlStr.append(" and arquivo.curso in (select distinct curso from cursocoordenador ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sqlStr.append(" where funcionario.pessoa = " + usuarioVO.getPessoa().getCodigo() + " ) ");
			sqlStr.append(" ) ");
			sqlStr.append(" or  ");
			// Verifica arquivos as turmas dos cursos que o mesmo é coordenador
			sqlStr.append(" (arquivo.curso is null ");
			sqlStr.append(" and arquivo.disciplina is null and arquivo.professor is null ");
			sqlStr.append(" and arquivo.turma in (select distinct turma.codigo from cursocoordenador ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sqlStr.append(" inner join turma on turma.curso = cursocoordenador.curso and case when cursocoordenador.unidadeensino is null then true else turma.unidadeensino = cursocoordenador.unidadeensino end  = true  ");
			sqlStr.append(" and case when cursocoordenador.turma is null then true else turma.codigo = cursocoordenador.turma end  = true ");
			sqlStr.append(" where funcionario.pessoa = " + usuarioVO.getPessoa().getCodigo() + " ) ");
			sqlStr.append(" ) or  ");
			// Verifica arquivos das disciplinas das turmas dos cursos que o
			// mesmo é coordenador
			sqlStr.append(" (arquivo.curso is null ");
			sqlStr.append(" and arquivo.turma is null and arquivo.professor is null ");
			sqlStr.append(" and arquivo.disciplina in (select distinct turmadisciplina.disciplina from cursocoordenador ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
			sqlStr.append(" inner join turma on turma.curso = cursocoordenador.curso and case when cursocoordenador.unidadeensino is null then true else turma.unidadeensino = cursocoordenador.unidadeensino end  = true ");
			sqlStr.append(" and case when cursocoordenador.turma is null then true else turma.codigo = cursocoordenador.turma end  = true ");
			sqlStr.append(" inner join turmadisciplina on turma.codigo = turmadisciplina.turma ");
			sqlStr.append(" where funcionario.pessoa = " + usuarioVO.getPessoa().getCodigo() + " ) ");
			sqlStr.append(" ) or  ");
			// Verifica arquivos dos professores das turmas dos cursos que o
			// mesmo é coordenador
			sqlStr.append(" (arquivo.curso is null and arquivo.professor is not null ");
			sqlStr.append(" and arquivo.turma is null and arquivo.disciplina is null ");
			sqlStr.append(" and arquivo.professor in (select distinct htd.professor from cursocoordenador ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sqlStr.append(" inner join turma on turma.curso = cursocoordenador.curso and case when cursocoordenador.unidadeensino is null then true else turma.unidadeensino = cursocoordenador.unidadeensino end  = true ");
			sqlStr.append(" and case when cursocoordenador.turma is null then true else turma.codigo = cursocoordenador.turma end  = true ");
			sqlStr.append(" inner join horarioturmadetalhado(null, arquivo.professor, null, null) as htd on htd.turma = turma.codigo ");
			sqlStr.append(" where funcionario.pessoa = " + usuarioVO.getPessoa().getCodigo() + " ) ");
			sqlStr.append(" )) ");
			sqlStr.append(" ORDER BY dataupload desc ");
			if (limit != null && limit > 0) {
				sqlStr.append(" limit ").append(limit);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}

		// List listaArquivos = null;
		// Map<String, String> hasMapNivelEducacional =
		// consultarNivelEducacionalCursoCoordenador(usuarioVO);
		// try {
		// listaArquivos =
		// consultarArquivosNivelEducacionalProfessor(hasMapNivelEducacional);
		// return listaArquivos;
		// } finally {
		// hasMapNivelEducacional = null;
		// }

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<String> consultarNomeArquivoPorCodigoDisciplina(Integer codigoExcluir) throws Exception {
		List<String> listaNomeArquivo = new ArrayList<String>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT nome FROM arquivo WHERE disciplina = ").append(codigoExcluir);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			listaNomeArquivo.add(tabelaResultado.getString("nome"));
		}
		return listaNomeArquivo;
	}

	public List<ArquivoVO> consultarArquivosNivelEducacionalProfessor(Map<String, String> hashNivelEducacional) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		try {
			sqlStr.append(getSqlConsultaDadosCompletosArquivosComuns());
			sqlStr.append(" where arquivo.situacao = 'AT'   ");
			if (!hashNivelEducacional.isEmpty()) {
				sqlStr.append(" AND ");
				for (String nivelEducacional : hashNivelEducacional.keySet()) {
					sqlStr.append(" (nivelEducacional = '");
					sqlStr.append(nivelEducacional);
					sqlStr.append("') OR ");
				}
			}
			sqlStr.replace(sqlStr.length() - 3, sqlStr.length(), "");
			sqlStr.append(" ORDER BY dataupload ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaCompleta(tabelaResultado));
		} finally {
			sqlStr = null;
		}

	}

	public Map<String, String> consultarNivelEducacionalAulaProfessor(UsuarioVO usuarioVO) throws Exception {
		Map<String, String> hashMapNivelEducacional = new HashMap<String, String>(0);
		List<TurmaVO> listaTurma = getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(usuarioVO.getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), "AT", 0, usuarioVO.getVisaoLogar().equals("professor"), false, false);
		try {
			for (TurmaVO turmaVO : listaTurma) {
				turmaVO.getCurso().setNivelEducacional(getFacadeFactory().getCursoFacade().consultarNivelEducacionalPorTurma(turmaVO.getCodigo()));
				if (!hashMapNivelEducacional.containsKey(turmaVO.getCurso().getNivelEducacional())) {
					hashMapNivelEducacional.put(turmaVO.getCurso().getNivelEducacional(), "");
				}
			}
			return hashMapNivelEducacional;
		} finally {
			listaTurma.clear();
		}
	}

	public Map<String, String> consultarNivelEducacionalCursoCoordenador(UsuarioVO usuarioVO) throws Exception {
		Map<String, String> hashMapNivelEducacional = new HashMap<String, String>(0);
		List<String> listaNiveis = getFacadeFactory().getCursoFacade().consultarListaNivelEducacionalCursoPorCodigoPessoaCoordenador(usuarioVO.getPessoa().getCodigo());
		for (String nivel : listaNiveis) {
			if (!hashMapNivelEducacional.containsKey(nivel)) {
				hashMapNivelEducacional.put(nivel, "");
			}
		}
		return hashMapNivelEducacional;
	}

	public void verificarArquivosExistentesHD(ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		List<Integer> listaCodigoDisciplina = new ArrayList<Integer>();
		List<ArquivoVO> listaNomeArquivo = new ArrayList<ArquivoVO>();
		String caminho = configuracaoGeralSistema.getLocalUploadArquivoFixo() + "/" + "arquivo";
		File arquivoCodigoDisciplina = null;
		File arquivoNome = null;
		try {
			listaCodigoDisciplina = consultarDisciplinaArquivoExistentesHD();
			for (Integer disciplina : listaCodigoDisciplina) {
				arquivoCodigoDisciplina = new File(caminho + "/" + disciplina);
				if (arquivoCodigoDisciplina.exists()) {
					listaNomeArquivo = consultarNomeArquivosExistentesHD(disciplina);
					for (ArquivoVO obj : listaNomeArquivo) {
						arquivoNome = new File(arquivoCodigoDisciplina.getAbsolutePath() + File.separator + obj.getNome().trim());
						if (arquivoNome.exists()) {
							alterarExistenciaArquivoHD(obj.getCodigo(), true);
						} else {
							alterarExistenciaArquivoHD(obj.getCodigo(), false);
						}
					}
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			listaCodigoDisciplina = null;
			listaNomeArquivo = null;
			caminho = null;
			arquivoCodigoDisciplina = null;
			arquivoNome = null;
		}

	}

	public List<Integer> consultarDisciplinaArquivoExistentesHD() throws Exception {
		List<Integer> listaDisciplina = new ArrayList<Integer>(0);
		String sqlStr = "select distinct disciplina from arquivo where disciplina is not null and dataupload >= '01-01-2011' order by disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (tabelaResultado.next()) {
			listaDisciplina.add(tabelaResultado.getInt("disciplina"));
		}
		return listaDisciplina;
	}

	public List<ArquivoVO> consultarNomeArquivosExistentesHD(Integer disciplina) throws Exception {
		String sqlStr = "select * from arquivo where disciplina = " + disciplina.intValue() + " and dataupload >= '01-01-2011' order by disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarExistenciaArquivoHD(Integer codigoArquivo, Boolean existe) throws Exception {
		try {
			String sql = "UPDATE arquivo set arquivoExisteHD = ? WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { existe, codigoArquivo });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Override
	public SqlRowSet consultarAlunoNotificarDownloadMaterial() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT matricula.matricula as matricula, unidadeensino.codigo as unidadeensino, pessoa.nome as nome_aluno, pessoa.email as email, pessoa.codigo as codigo, (select horarioturmadia.data from horarioturmadia inner join horarioturmadiaitem on  horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia "); 
		sqlStr.append("		 where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append("		 and horarioturmadia.data > current_date"); 
		sqlStr.append("		 and (horarioturmadia.data - ((configuracaogeralsistema.qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial||' DAY')::INTERVAL)) = current_date "); 
		sqlStr.append("		 order by horarioturmadia.data limit 1 ) as data_aula, matriculaperiodoturmadisciplina.turma AS codigoTurma, matriculaperiodoturmadisciplina.disciplina AS codigoDisciplina, matriculaperiodoturmadisciplina.matriculaPeriodo AS codigoMatriculaPeriodo, disciplina.nome as nome_disciplina, curso.nome as nome_curso ");
		sqlStr.append("from matricula ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append("inner join configuracoes on configuracoes.codigo = unidadeensino.configuracoes ");
		sqlStr.append("inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = configuracoes.codigo ");
		sqlStr.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append("inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaPeriodo = matriculaPeriodo.codigo ");
		sqlStr.append("inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append("inner join horarioturma on horarioturma.turma = turma.codigo and ((turma.anual and horarioturma.anovigente = matriculaperiodoturmadisciplina.ano) ");
		sqlStr.append(" or (turma.semestral and horarioturma.anovigente = matriculaperiodoturmadisciplina.ano and horarioturma.semestrevigente = matriculaperiodoturmadisciplina.semestre) ");
		sqlStr.append(" or (turma.anual = false and turma.semestral = false)) ");
		sqlStr.append("inner join arquivo on arquivo.disciplina = disciplina.codigo and arquivo.situacao = 'AT' and arquivo.origem in ('PR') ");
		sqlStr.append("and ((dataDisponibilizacao <= current_date and dataIndisponibilizacao >= current_date) or manterdisponibilizacao = true) ");
		sqlStr.append("where matricula.situacao = 'AT' and matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ");
		sqlStr.append("and matriculaperiodoturmadisciplina.notificacaoDownloadMaterialEnviado <> true ");
		sqlStr.append(" and configuracaogeralsistema.qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial > 0 ");
		sqlStr.append(" and exists (select horarioturmadiaitem.codigo from horarioturmadia inner join horarioturmadiaitem on  horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append(" where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(" and horarioturmadia.data > current_date ");
		sqlStr.append(" and (horarioturmadia.data - ((configuracaogeralsistema.qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial||' DAY')::INTERVAL)) = current_date ");
		sqlStr.append(" limit 1 ) ");
		sqlStr.append("group by horarioturma.codigo, configuracaogeralsistema.qtdeantecedenciadiasnotificaralunodownloadmaterial, matricula.matricula, unidadeensino.codigo, pessoa.codigo, pessoa.nome, pessoa.email, disciplina.nome, curso.nome, matriculaperiodoturmadisciplina.turma,matriculaperiodoturmadisciplina.disciplina, matriculaperiodoturmadisciplina.matriculaPeriodo ");
		sqlStr.append("order by pessoa.nome, disciplina.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>MetaVendedorVO</code> contidos em um Hashtable no BD. Faz uso da
	 * operação <code>excluirMetaVendedors</code> e
	 * <code>incluirMetaVendedors</code> disponíveis na classe
	 * <code>MetaVendedor</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void alterarArquivos(Integer codigo, List<ArquivoVO> objetos, OrigemArquivo origemArquivo, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuilder sqlexcluir = new StringBuilder("select * FROM arquivo WHERE codOrigem = ").append(codigo).append(" and origem ='").append(origemArquivo.getValor()).append("'");
		Iterator<ArquivoVO> i = objetos.iterator();
		while (i.hasNext()) {
			ArquivoVO objeto = (ArquivoVO) i.next();
			sqlexcluir.append(" AND codigo <> ").append(objeto.getCodigo().intValue());
		}
		SqlRowSet rsArquivoExcluirDiretorio = getConexao().getJdbcTemplate().queryForRowSet(sqlexcluir.toString());
		StringBuilder sql = new StringBuilder("DELETE FROM Arquivo WHERE codOrigem = ").append(codigo).append(" and origem ='").append(origemArquivo.getValor()).append("'");
		Iterator<ArquivoVO> j = objetos.iterator();
		while (j.hasNext()) {
			ArquivoVO objeto = (ArquivoVO) j.next();
			sql.append(" AND codigo <> ").append(objeto.getCodigo().intValue());
		}
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		getConexao().getJdbcTemplate().update(sql.toString());
		Iterator<ArquivoVO> e = objetos.iterator();
		while (e.hasNext()) {
			ArquivoVO objeto = (ArquivoVO) e.next();
			objeto.setCodOrigem(codigo);
			if (objeto.getCodigo().equals(0)) {
				incluir(objeto, usuarioLogado, configuracaoGeralSistemaVO);
			} else {
				alterar(objeto, usuarioLogado, configuracaoGeralSistemaVO);
			}
			objeto = null;
		}
		realizarExclusaoDiretorioOrigemImagem(rsArquivoExcluirDiretorio);
		sql = null;
		i = null;
		e = null;
		sqlexcluir = null;
		rsArquivoExcluirDiretorio = null;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarExclusaoDiretorioOrigemImagem(SqlRowSet rs) throws Exception {
		while (rs.next()) {
			if (rs.getString("pastaBaseArquivo") != null && !rs.getString("pastaBaseArquivo").isEmpty()) {
				File file = new File(rs.getString("pastaBaseArquivo") + File.separator + rs.getString("nome") + "." + rs.getString("extensao"));
				file.delete();
				file = null;
			}
		}
	}

	public List<ArquivoVO> consultarTodosArquivo(UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT codigo, nome, descricao, disciplina FROM arquivo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ArquivoVO> listaArquivoVOs = new ArrayList<ArquivoVO>(0);
		while (tabelaResultado.next()) {
			ArquivoVO obj = new ArquivoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			listaArquivoVOs.add(obj);
		}
		return listaArquivoVOs;
	}

	public void removerAcentosCaracteresEspeciaisArquivos(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		List<ArquivoVO> listaArquivoVOs = consultarTodosArquivo(usuarioVO);
		for (ArquivoVO arquivoVO : listaArquivoVOs) {
			File arquivoAtual = null;
			File arquivoDestino = null;
			File arquivoAtualTemp = null;
			File arquivoDestinoTemp = null;
			if (!arquivoVO.getDisciplina().getCodigo().equals(0)) {
				arquivoAtual = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator + arquivoVO.getDisciplina().getCodigo() + File.separator + arquivoVO.getNome());
			} else {
				arquivoAtual = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator + arquivoVO.getNome());
			}
			arquivoAtualTemp = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator + arquivoVO.getNome());
			String nomeArquivo = "";
			if (arquivoVO.getNome().contains(".")) {
				nomeArquivo = arquivoVO.getNome().substring(0, arquivoVO.getNome().lastIndexOf("."));
			} else {
				nomeArquivo = arquivoVO.getNome();
			}
			StringBuffer nomeArquivoBuffer = new StringBuffer(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(Uteis.removerAcentuacao(nomeArquivo)));
			if (nomeArquivoBuffer.toString().contains("\\")) {
				nomeArquivoBuffer = new StringBuffer(nomeArquivoBuffer.substring(nomeArquivoBuffer.lastIndexOf("\\") + 1, nomeArquivoBuffer.length()));
			} else if (nomeArquivoBuffer.toString().contains("/")) {
				nomeArquivoBuffer = new StringBuffer(nomeArquivoBuffer.substring(nomeArquivoBuffer.lastIndexOf("/") + 1, nomeArquivoBuffer.length()));
			}
			String nomeArquivoFinal = nomeArquivoBuffer.toString();
			nomeArquivoFinal = nomeArquivoFinal.replace(" ", "_");
			if (arquivoVO.getNome().contains(".")) {
				arquivoVO.setNome(nomeArquivoFinal + "." + arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf(".") + 1));
			} else {
				arquivoVO.setNome(nomeArquivoFinal);
			}
			arquivoDestino = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator + arquivoVO.getDisciplina().getCodigo() + File.separator + arquivoVO.getNome());
			arquivoDestinoTemp = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator + arquivoVO.getNome());
			if (arquivoAtual.getName().equals(arquivoDestino.getName())) {
				continue;
			}
			if (arquivoAtual.exists()) {
				FileUtils.copyFile(arquivoAtual, arquivoDestino);
				arquivoAtual.delete();
			} else {
				if (arquivoAtualTemp.exists()) {
					FileUtils.copyFile(arquivoAtualTemp, arquivoDestinoTemp);
					arquivoAtualTemp.delete();
				} else {
					continue;
				}
			}
			alterarNomeArquivo(arquivoVO.getCodigo(), arquivoVO.getNome(), usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarNomeArquivo(final Integer arquivo, final String nome, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Arquivo set nome=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, nome);
					sqlAlterar.setInt(++i, arquivo.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarDataDisponibilizacao(final Integer turma, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Arquivo set dataDisponibilizacao=now() from (select arquivo.codigo from arquivo "
					+ " inner join turma on turma.codigo = arquivo.turma inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino "
					+ " inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes "
					+ " WHERE turma.codigo = ? "
					+ " and configuracaogeralsistema.tokenwebservice  is not null and configuracaogeralsistema.tokenwebservice  <> '' "
					+ " ) as t where t.codigo = arquivo.codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setInt(++i, turma.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public Integer consultarQuantidadeMaterialPostado(Integer professor, Integer disciplina, Integer turma) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) as qtdeMaterialPostado from (SELECT arquivo.codigo AS qtde FROM arquivo ");
		sb.append(" WHERE disciplina = ");
		sb.append(disciplina.intValue());
		sb.append(" AND professor = ");
		sb.append(professor.intValue());
		sb.append(" AND turma is null ");
		sb.append(" union all ");
		sb.append(" SELECT arquivo.codigo AS qtde FROM arquivo ");
		sb.append(" WHERE disciplina = ");
		sb.append(disciplina.intValue());
		sb.append(" AND professor = ");
		sb.append(professor.intValue());
		sb.append(" AND turma = ");
		sb.append(turma.intValue());
		sb.append(") as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtdeMaterialPostado"));
		}
		return 0;
	}

	public List<ArquivoVO> consultarArquivosPorPastaBaseArquivo(String pastaBaseArquivo) throws Exception {
		String urlExternoDownloadArquivo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo().getUrlExternoDownloadArquivo();
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, nome, pastabasearquivo, descricao from arquivo where pastabasearquivo = '").append(pastaBaseArquivo).append("'").append(" and manterDisponibilizacao = true");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ArquivoVO> listaArquivoVOs = new ArrayList<ArquivoVO>(0);
		while (dadosSQL.next()) {
			ArquivoVO obj = new ArquivoVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setPastaBaseArquivo(dadosSQL.getString("pastabasearquivo"));
			if (urlExternoDownloadArquivo.endsWith("/") || urlExternoDownloadArquivo.endsWith("\\")) {
				obj.setDescricao(urlExternoDownloadArquivo + dadosSQL.getString("pastabasearquivo") + File.separator + dadosSQL.getString("nome"));
			} else {
				obj.setDescricao(urlExternoDownloadArquivo + File.separator + dadosSQL.getString("pastabasearquivo") + File.separator + dadosSQL.getString("nome"));
			}
			obj.getDescricao().replace("\\", "/");
			listaArquivoVOs.add(obj);
		}
		return listaArquivoVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisponibilidadeMaterial(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			Arquivo.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE Arquivo set manterDisponibilizacao=?, dataDisponibilizacao=?, apresentarDeterminadoPeriodo=?, dataIndisponibilizacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.isManterDisponibilizacao());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataDisponibilizacao()));
					sqlAlterar.setBoolean(3, obj.getApresentarDeterminadoPeriodo());
					if (obj.getApresentarDeterminadoPeriodo()) {
						sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataIndisponibilizacao()));
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOrdemArquivo(List<ArquivoVO> arquivoVOs, ArquivoVO arquivo1, ArquivoVO arquivo2, UsuarioVO usuario) throws Exception {
		int indice1 = arquivo1.getIndiceAgrupador();
		int indice2 = arquivo2.getIndiceAgrupador();
		try {
			arquivo1.setIndiceAgrupador(indice2);
			arquivo2.setIndiceAgrupador(indice1);
			for (ArquivoVO arquivoVO : arquivo1.getArquivoFilhoVOs()) {
				arquivoVO.setIndiceAgrupador(arquivo1.getIndiceAgrupador());
				alterarAgrupadorIndiceArquivo(arquivoVO, usuario);
			}
			for (ArquivoVO arquivoVO : arquivo2.getArquivoFilhoVOs()) {
				arquivoVO.setIndiceAgrupador(arquivo2.getIndiceAgrupador());
				alterarAgrupadorIndiceArquivo(arquivoVO, usuario);
			}
			alterarAgrupadorIndiceArquivo(arquivo1, usuario);
			alterarAgrupadorIndiceArquivo(arquivo2, usuario);
			Ordenacao.ordenarLista(arquivoVOs, "indiceAgrupador");
		} catch (Exception e) {
			arquivo1.setIndiceAgrupador(indice1);
			arquivo2.setIndiceAgrupador(indice2);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOrdemArquivoFilho(List<ArquivoVO> arquivoVOs, ArquivoVO arquivo1, ArquivoVO arquivo2, UsuarioVO usuario) throws Exception {
		int indice1 = arquivo1.getIndice();
		int indice2 = arquivo2.getIndice();
		try {
			arquivo1.setIndice(indice2);
			arquivo2.setIndice(indice1);
			alterarAgrupadorIndiceArquivo(arquivo1, usuario);
			alterarAgrupadorIndiceArquivo(arquivo2, usuario);
			Ordenacao.ordenarLista(arquivoVOs, "indice");
		} catch (Exception e) {
			arquivo1.setIndice(indice1);
			arquivo2.setIndice(indice2);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOrdemArquivoDragDrop(List<ArquivoVO> arquivoVOs, List<ArquivoVO> arquivoFilhoVOs, ArquivoVO arquivo1, ArquivoVO arquivo2, UsuarioVO usuario) throws Exception {
		int indice1 = arquivo1.getIndice();
		int indice2 = arquivo2.getIndice();
		boolean encontrado = false;
		try {
			for (ArquivoVO arquivoVO : arquivoFilhoVOs) {
				if (arquivoVO.getCodigo().equals(arquivo1.getCodigo())) {
					arquivo1.setIndice(indice2);
					arquivo2.setIndice(indice1);
					alterarAgrupadorIndiceArquivo(arquivo1, usuario);
					alterarAgrupadorIndiceArquivo(arquivo2, usuario);
					Ordenacao.ordenarLista(arquivoFilhoVOs, "indice");
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				arquivo1.setAgrupador(arquivo2.getAgrupador());
				arquivo1.setIndice(indice2);
				boolean removeu = false;
				boolean adicionou = false;
				for (Iterator<ArquivoVO> iterator = arquivoVOs.iterator(); iterator.hasNext();) {
					ArquivoVO arquivoVO = iterator.next();
					if (arquivoVO.getArquivoFilhoVOs().contains(arquivo1) && !removeu) {
						arquivoVO.getArquivoFilhoVOs().remove(arquivo1);
						removeu = true;
					}
					if (arquivoVO.getArquivoFilhoVOs().contains(arquivo2) && !adicionou) {
						arquivoVO.getArquivoFilhoVOs().add(arquivo1);
						adicionou = true;
					}
					Ordenacao.ordenarLista(arquivoVO.getArquivoFilhoVOs(), "indice");
					if (!Uteis.isAtributoPreenchido(arquivoVO.getArquivoFilhoVOs())) {
						iterator.remove();
					}
				}
				Ordenacao.ordenarLista(arquivoVOs, "indiceAgrupador");
				int indiceAgrupador = 1;
				for (ArquivoVO pai : arquivoVOs) {
					pai.setIndiceAgrupador(indiceAgrupador);
					int indice = 1;
					for (ArquivoVO filho : pai.getArquivoFilhoVOs()) {
						filho.setIndice(indice);
						filho.setIndiceAgrupador(pai.getIndiceAgrupador());
						alterarAgrupadorIndiceArquivo(filho, usuario);
						indice++;
					}
					alterarAgrupadorIndiceArquivo(pai, usuario);
					indiceAgrupador++;
				}
			}
		} catch (Exception e) {
			arquivo1.setIndice(indice1);
			arquivo2.setIndice(indice2);
			throw e;
		}
	}

	@Override
	public void adicionarArquivoIndice(List<ArquivoVO> arquivoVOs, ArquivoVO arquivoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(arquivoVO.getAgrupador())) {
			boolean agrupadorIdentificado = false;
			for (ArquivoVO arquivo : arquivoVOs) {
				if (arquivo.getAgrupador().equals(arquivoVO.getAgrupador())) {
					arquivoVO.setIndice(executarObtencaoIndiceFilho(arquivo.getArquivoFilhoVOs()));
					arquivoVO.setIndiceAgrupador(arquivo.getIndiceAgrupador());
					arquivo.getArquivoFilhoVOs().add(arquivoVO);
					Ordenacao.ordenarLista(arquivo.getArquivoFilhoVOs(), "indice");
					agrupadorIdentificado = true;
				}
			}
			if (!agrupadorIdentificado) {
				ArquivoVO novoArquivo = new ArquivoVO();
				novoArquivo.setIndiceAgrupador(executarObtencaoIndice(arquivoVOs));
				novoArquivo.setAgrupador(arquivoVO.getAgrupador());
				arquivoVO.setIndice(executarObtencaoIndiceFilho(novoArquivo.getArquivoFilhoVOs()));
				arquivoVO.setIndiceAgrupador(novoArquivo.getIndiceAgrupador());
				novoArquivo.getArquivoFilhoVOs().add(arquivoVO);
				arquivoVOs.add(novoArquivo);
				Ordenacao.ordenarLista(arquivoVOs, "indiceAgrupador");
			}
		} else {
			arquivoVO.setIndice(executarObtencaoIndiceFilho(arquivoVO.getArquivoFilhoVOs()));
			arquivoVO.setIndiceAgrupador(executarObtencaoIndice(arquivoVOs));
			arquivoVOs.add(arquivoVO);
			Ordenacao.ordenarLista(arquivoVOs, "indiceAgrupador");
		}
	}

	@Override
	public Map<String, ArquivoVO> montarDadosArquivoIndice(List<ArquivoVO> listaArquivoConsultado, UsuarioVO usuario) throws Exception {
		Map<String, ArquivoVO> mapArquivoDisciplinaTurmaProfessor = realizarSeparacaoArquivosPorDisciplinaTurmaProfessor(listaArquivoConsultado);
		for (ArquivoVO arquivoVO : listaArquivoConsultado) {
			ArquivoVO arquivoDisciplinaTurmaProfessor = mapArquivoDisciplinaTurmaProfessor.get(arquivoVO.getKeyAgrupadorTurmaDisciplinaProfessor());
			if (Uteis.isAtributoPreenchido(arquivoVO.getAgrupador())) {
				boolean agrupadorIdentificado = false;
				for (ArquivoVO arquivo : arquivoDisciplinaTurmaProfessor.getArquivoFilhoVOs()) {
					if (arquivo.getAgrupador().equals(arquivoVO.getAgrupador())) {
						arquivo.getArquivoFilhoVOs().add(arquivoVO);
						// Ordenacao.ordenarLista(arquivo.getArquivoFilhoVOs(), "indice");
						agrupadorIdentificado = true;
					}
				}
				if (!agrupadorIdentificado) {
					ArquivoVO novoArquivo = new ArquivoVO();
					novoArquivo.setIndice(arquivoVO.getIndice());
					novoArquivo.setIndiceAgrupador(arquivoVO.getIndiceAgrupador());
					novoArquivo.setAgrupador(arquivoVO.getAgrupador());
					novoArquivo.setTurma(arquivoVO.getTurma().clone());
					novoArquivo.setProfessor((PessoaVO) arquivoVO.getProfessor().clone());
					novoArquivo.setDisciplina(arquivoVO.getDisciplina().clone());
					novoArquivo.getArquivoFilhoVOs().add(arquivoVO);
					arquivoDisciplinaTurmaProfessor.getArquivoFilhoVOs().add(novoArquivo);
				}
			} else {
				arquivoDisciplinaTurmaProfessor.getArquivoFilhoVOs().add(arquivoVO);
			}
		}
		for (String key : mapArquivoDisciplinaTurmaProfessor.keySet()) {
			ArquivoVO arquivoVO = mapArquivoDisciplinaTurmaProfessor.get(key);
			Ordenacao.ordenarLista(arquivoVO.getArquivoFilhoVOs(), "indiceAgrupador");
			int x = 1;
			for (ArquivoVO arquivoVO2 : arquivoVO.getArquivoFilhoVOs()) {
				Ordenacao.ordenarLista(arquivoVO2.getArquivoFilhoVOs(), "indice");
				if (arquivoVO2.getIndiceAgrupador().intValue() != x || arquivoVO2.getIndice().intValue() != x) {
					arquivoVO2.setIndiceAgrupador(x);
					arquivoVO2.setIndice(x);
					if (Uteis.isAtributoPreenchido(usuario) && !usuario.getIsApresentarVisaoAluno() && !usuario.getIsApresentarVisaoPais()) {
						alterarAgrupadorIndiceArquivo(arquivoVO2, usuario);
					}
				}
				int y = 1;
				for (ArquivoVO arquivoVO3 : arquivoVO2.getArquivoFilhoVOs()) {
					if (arquivoVO3.getIndice().intValue() != y || arquivoVO3.getIndiceAgrupador().intValue() != x) {
						arquivoVO3.setIndice(y);
						arquivoVO3.setIndiceAgrupador(x);
						if (Uteis.isAtributoPreenchido(usuario) && !usuario.getIsApresentarVisaoAluno() && !usuario.getIsApresentarVisaoPais()) {
							alterarAgrupadorIndiceArquivo(arquivoVO3, usuario);
						}
					}
					y++;
				}
				x++;
			}
		}
		return mapArquivoDisciplinaTurmaProfessor;
	}

	private int executarObtencaoIndice(List<ArquivoVO> arquivoVOs) throws Exception {
		int indice = 0;
		for (ArquivoVO arquivoVO : arquivoVOs) {
			if (arquivoVO.getIndiceAgrupador() > indice) {
				indice = arquivoVO.getIndiceAgrupador();
			}
		}
		return indice + 1;
	}

	private int executarObtencaoIndiceFilho(List<ArquivoVO> arquivoVOs) throws Exception {
		int indice = 0;
		for (ArquivoVO arquivoVO : arquivoVOs) {
			if (arquivoVO.getIndice() > indice) {
				indice = arquivoVO.getIndice();
			}
		}
		return indice + 1;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAgrupadorIndiceArquivo(final ArquivoVO arquivoVO, UsuarioVO usuario) throws Exception {
		if (" --ul:0".equals(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario))) {
//			Uteis.imprimirStackTrace("LOG_AUDIT_ARQUIVO");
			return;
		}
		final String sql = "UPDATE Arquivo set indice=?, agrupador=?, indiceAgrupador=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, arquivoVO.getIndice());
				sqlAlterar.setString(2, arquivoVO.getAgrupador());
				sqlAlterar.setInt(3, arquivoVO.getIndiceAgrupador());
				sqlAlterar.setInt(4, arquivoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public void persistir(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		validarDados(obj);
		if (obj.isNovoObj()) {
			incluir(obj, verificarAcesso, usuario, configuracaoGeralSistemaVO);
		} else {
			alterar(obj, verificarAcesso, usuario, configuracaoGeralSistemaVO);
		}
	}

	public void validarDados(ArquivoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		
		if (obj.getValidarDisciplina()) {
			if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
				throw new ConsistirException("A DISCIPLINA deve ser informada!");
			}
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("Selecione o ARQUIVO para upload!");
		}
		if (obj.getDescricaoArquivo().equals("")) {
			throw new ConsistirException("A DESCRIÇÃO deve ser informada!");
		}
		if (obj.getDataUpload() == null) {
			throw new ConsistirException("A DATA do UPLOAD deve ser informada!");
		}
		if ((obj.getResponsavelUpload() == null) || (obj.getResponsavelUpload().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O RESPONSÁVEL pelo UPLOAD deve ser informado!");
		}
		if (obj.getDataDisponibilizacao() == null) {
			throw new ConsistirException("A DATA de DISPONIBILIZAÇÃO deve ser informada!");
		}
		if (obj.getSituacao().equals("")) {
			throw new ConsistirException("A SITUAÇÃO deve ser informada!");
		}
		if (obj.getDataDisponibilizacao() != null && obj.getDataIndisponibilizacao() != null) {
			if ((obj.getDataIndisponibilizacao().before(obj.getDataDisponibilizacao()))) {
				throw new ConsistirException("As DATAS da disponibilidade informadas contém erros. Verifique novamente!");
			}
		}
	}

	private Map<String, ArquivoVO> realizarSeparacaoArquivosPorDisciplinaTurmaProfessor(List<ArquivoVO> arquivoVOs) throws Exception {
		Map<String, ArquivoVO> mapArquivoVOs = new HashMap<String, ArquivoVO>(0);
		for (ArquivoVO arquivoVO : arquivoVOs) {
			if (!mapArquivoVOs.containsKey(arquivoVO.getKeyAgrupadorTurmaDisciplinaProfessor())) {
				ArquivoVO arq = new ArquivoVO();
				arq.setTurma((TurmaVO) arquivoVO.getTurma().clone());
				arq.setDisciplina((DisciplinaVO) arquivoVO.getDisciplina().clone());
				arq.setProfessor((PessoaVO) arquivoVO.getProfessor().clone());
				arq.setAgrupadorTurmaDisciplinaProfessor(true);
				if (!Uteis.isAtributoPreenchido(arq.getTurma())) {
					arq.getTurma().setIdentificadorTurma("TODAS");
				}
				if (!Uteis.isAtributoPreenchido(arq.getProfessor())) {
					arq.getProfessor().setNome("TODOS");
				}
				mapArquivoVOs.put(arquivoVO.getKeyAgrupadorTurmaDisciplinaProfessor(), arq);
			}
		}
		List<ArquivoVO> arquivoVOs2 = new ArrayList<ArquivoVO>(0);
		arquivoVOs2.addAll(mapArquivoVOs.values());
		return mapArquivoVOs;
	}

	@Override
	public void carregarArquivoDigitalmenteAssinado(ArquivoVO obj, ConfiguracaoGeralSistemaVO confg, UsuarioVO usuarioLogado) throws Exception {
		obj = (getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
		getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(confg.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo() + File.separator + obj.getNome(), obj.getNome());
	}

	@Override
	public List<File> consultarArquivosNaoVinculadosSistema(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean filtrarPastaArquivo, boolean filtrarPastaImagem,  Integer loteFile) throws Exception {
		ServidorArquivoOnlineS3RS servidorExternoAmazon = null;
		if(!configuracaoGeralSistemaVO.getServidorArquivoOnline().isEmpty() && !configuracaoGeralSistemaVO.getServidorArquivoOnline().equals("APACHE")) {
			servidorExternoAmazon = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());	
		}
		List<File> files = new ArrayList<File>(0);
		File pastaBase = null;
		if (filtrarPastaArquivo) {
			pastaBase = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue());
			realizarObtencaoArquivoNaoReferenciadoSistema(files, pastaBase, configuracaoGeralSistemaVO, servidorExternoAmazon, loteFile);
			pastaBase = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue());
			realizarObtencaoArquivoNaoReferenciadoSistema(files, pastaBase, configuracaoGeralSistemaVO, servidorExternoAmazon, loteFile);
		}
		if (filtrarPastaImagem) {
			pastaBase = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue());
			realizarObtencaoArquivoNaoReferenciadoSistema(files, pastaBase, configuracaoGeralSistemaVO, servidorExternoAmazon, loteFile);
			pastaBase = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue());
			realizarObtencaoArquivoNaoReferenciadoSistema(files, pastaBase, configuracaoGeralSistemaVO, servidorExternoAmazon, loteFile);
		}
		return files;
	}

	public void realizarObtencaoArquivoNaoReferenciadoSistema(List<File> files, File pastaBase, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ServidorArquivoOnlineS3RS servidorExternoAmazon, Integer loteFile) throws Exception {
		if(pastaBase != null) {
			if (pastaBase.isDirectory()) {
				if(pastaBase.listFiles() != null) {
					q: for (File arquivo : pastaBase.listFiles()) {
						if (arquivo.isDirectory()) {
							realizarObtencaoArquivoNaoReferenciadoSistema(files, arquivo, configuracaoGeralSistemaVO, servidorExternoAmazon, loteFile);
							if(Uteis.isAtributoPreenchido(loteFile) && files.size() >= loteFile) {
								return; 
							}
							continue q;
						} else {
							if (!realizarVerificacaoArquivoReferenciadoSistema(arquivo, configuracaoGeralSistemaVO, servidorExternoAmazon)) {
								files.add(arquivo);
								if(Uteis.isAtributoPreenchido(loteFile) && files.size() >= loteFile) {
									return; 
								}
							}
						}
					}
				}
			} else {
				if (!realizarVerificacaoArquivoReferenciadoSistema(pastaBase, configuracaoGeralSistemaVO, servidorExternoAmazon)) {
					files.add(pastaBase);
				}
			}
		}
	}

	private Boolean realizarVerificacaoArquivoReferenciadoSistema(File arquivo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ServidorArquivoOnlineS3RS servidorExternoAmazon) throws Exception {
		if (!arquivo.isDirectory()) {
			if (realizarVerificacaoArquivoReferenciadoTabelaArquivo(arquivo, configuracaoGeralSistemaVO, servidorExternoAmazon) || realizarVerificacaoArquivoReferenciadoTextoPadrao(arquivo)) {
				return true;
			}
			return false;
		} else {
			return true;
		}
	}

	private Boolean realizarVerificacaoArquivoReferenciadoTabelaArquivo(File arquivo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ServidorArquivoOnlineS3RS servidorExternoAmazon) throws Exception {
		StringBuilder sql = new StringBuilder("select  descricao, pastabasearquivo, servidorarquivoonline from arquivo where nome = ? ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), arquivo.getName());
		if (dadosSQL.next()) {
			ArquivoVO obj = new ArquivoVO();
			obj.setDescricao(dadosSQL.getString("descricao"));
			obj.setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("servidorArquivoOnline"))){
				obj.setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("servidorArquivoOnline")));	
			}
			String pastaBaseTemp = "";
			String localUpload = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
			if(localUpload.endsWith("/") || localUpload.endsWith("\\")) {
				localUpload = localUpload.substring(0, localUpload.length()-1);
			}
			
			if(obj.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
				try {
					boolean existeAmazon = servidorExternoAmazon.consultarSeObjetoExiste(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getDescricao()));
					if(existeAmazon) {
						pastaBaseTemp = !obj.getPastaBaseArquivo().startsWith(localUpload) ?  localUpload + File.separator + obj.getPastaBaseArquivo() : obj.getPastaBaseArquivo();
						if(Uteis.isSistemaOperacionalWindows()) {
							pastaBaseTemp = pastaBaseTemp.replace("/", "\\");	
						}else {
							pastaBaseTemp = pastaBaseTemp.replace("\\", "/");
						}
						return arquivo.getParent().equals(pastaBaseTemp);
					}
					return true;
				} catch (Exception e) {
					return true;
				}	
			}else {
				pastaBaseTemp = !obj.getPastaBaseArquivo().startsWith(localUpload) ?  localUpload + File.separator + obj.getPastaBaseArquivo() : obj.getPastaBaseArquivo();
				if(Uteis.isSistemaOperacionalWindows()) {
					pastaBaseTemp = pastaBaseTemp.replace("/", "\\");	
				}else {
					pastaBaseTemp = pastaBaseTemp.replace("\\", "/");
				}
				return arquivo.getParent().equals(pastaBaseTemp);
			}
		}
		return false;
	}

	private Boolean realizarVerificacaoArquivoReferenciadoTextoPadrao(File arquivo) throws Exception {
		StringBuilder sql = new StringBuilder("select codigo from textopadrao where texto ilike ? union select codigo from textopadraodeclaracao where texto ilike ? ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), '%' + arquivo.getName() + '%', '%' + arquivo.getName() + '%').next();
	}

	@Override
	public List<ArquivoVO> consultarBackupPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivobackup.* FROM arquivobackup WHERE codigo >= " + valorConsulta.intValue();
		sqlStr = sqlStr.concat(" ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public ArquivoVO consultarBackupPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM ArquivoBackup WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Arquivo Backup ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public SqlRowSet consultarAlunoEmPeriodoAulaNotificarDownloadMaterial(String dataAtual, Integer disciplina, Integer turma, Integer professor) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT * FROM (");
		sqlStr.append(" SELECT DISTINCT matricula.matricula AS matricula,");
		sqlStr.append(" pessoa.nome AS nome_aluno,");
		sqlStr.append(" pessoa.email AS email,");
		sqlStr.append(" pessoa.codigo AS codigo,");
		sqlStr.append(" min(horarioturmadiaitem.data) AS inicio_aula,");
		sqlStr.append(" max(horarioturmadiaitem.data) AS termino_aula,");
		sqlStr.append(" min(horarioturmadiaitem.data) AS data_aula,");
		sqlStr.append(" matriculaperiodoturmadisciplina.turma AS codigoTurma,");
		sqlStr.append(" matriculaperiodoturmadisciplina.disciplina AS codigoDisciplina,");
		sqlStr.append(" matriculaperiodoturmadisciplina.matriculaPeriodo AS codigoMatriculaPeriodo,");
		sqlStr.append(" disciplina.nome AS nome_disciplina,");
		sqlStr.append(" curso.nome AS nome_curso,");
		sqlStr.append(" horarioturma.anovigente,");
		sqlStr.append(" horarioturma.semestrevigente");
		sqlStr.append(" FROM matricula");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaPeriodo = matriculaPeriodo.codigo");
		sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" INNER JOIN horarioturma ON horarioturma.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" INNER JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" INNER JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo AND matriculaperiodoturmadisciplina.disciplina = horarioturmadiaitem.disciplina");
		sqlStr.append(" INNER JOIN arquivo ON arquivo.disciplina = disciplina.codigo");
		sqlStr.append(" AND arquivo.situacao = 'AT'");
		sqlStr.append(" AND arquivo.origem = 'PR'");
		sqlStr.append(" AND ((dataDisponibilizacao <= CURRENT_DATE AND dataIndisponibilizacao >= CURRENT_DATE) OR manterdisponibilizacao = TRUE)");
		sqlStr.append(" WHERE matricula.situacao = 'AT'");
		sqlStr.append(" AND matriculaPeriodo.situacaoMatriculaPeriodo = 'AT'");
		sqlStr.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma =  ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sqlStr.append(" and horarioturmadiaitem.professor = ").append(professor);
		}
		sqlStr.append(" AND horarioturma.anovigente = '").append(Uteis.getAnoDataAtual()).append("'");
		sqlStr.append(" AND horarioturma.semestrevigente = '").append(Uteis.getSemestreAtual()).append("'");
		sqlStr.append(" GROUP BY matricula.matricula, pessoa.codigo, pessoa.nome, pessoa.email, disciplina.nome, curso.nome, matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.disciplina, matriculaperiodoturmadisciplina.matriculaPeriodo, horarioturma.anovigente,horarioturma.semestrevigente");
		sqlStr.append(" ORDER BY pessoa.nome, disciplina.nome");
		sqlStr.append(" ) AS t");
		sqlStr.append(" WHERE 1=1");
		sqlStr.append(" AND '").append(dataAtual).append("' BETWEEN t.inicio_aula AND termino_aula");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	public List<ArquivoVO> consultarArquivoAtivosPorMatriculaTodasDisciplinasAnoSemestreOrigem(String matricula, Integer disciplina, String ano, String semestre, String origem) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(getSqlConsultaDadosCompletos());
		sqlStr.append("left join (");
		sqlStr.append("	select distinct turma, turmaagrupada, disciplina, professor from horarioturma");
		sqlStr.append("	inner join turma on turma.codigo = horarioturma.turma");
		sqlStr.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sqlStr.append("	where anovigente = '").append(ano).append("'");
		sqlStr.append("	and semestrevigente = '").append(semestre).append("'");
		sqlStr.append("	and turma in (");
		sqlStr.append("		select turmapratica from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("' ");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turmapratica is not null");
		sqlStr.append("		union");
		sqlStr.append("		select turmateorica from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turmateorica is not null");
		sqlStr.append("		union");
		sqlStr.append("		select turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turmateorica is null");
		sqlStr.append("		and turmapratica is null");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and turmateorica is null and turmapratica is null");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("	) ");
		sqlStr.append("	and ((turma.turmaagrupada = false and disciplina = ").append(disciplina).append(")");
		sqlStr.append("		or (turma.turmaagrupada and disciplina in (");
		sqlStr.append("			select ").append(disciplina);
		sqlStr.append("			union select disciplina from disciplinaequivalente  where equivalente = ").append(disciplina);
		sqlStr.append("			union select equivalente from disciplinaequivalente  where disciplina  = ").append(disciplina);
		sqlStr.append("		)");
		sqlStr.append("	))");
		sqlStr.append(") as turmaprofessor on turmaprofessor.disciplina = arquivo.disciplina ");
		sqlStr.append("where arquivo.situacao = 'AT' ");
		sqlStr.append("and arquivo.dataDisponibilizacao <= current_timestamp ");
		sqlStr.append("and case when arquivo.manterdisponibilizacao = false ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = false then false when arquivo.manterdisponibilizacao = false ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = true ");
		sqlStr.append("	then (arquivo.dataIndisponibilizacao >= current_timestamp or arquivo.dataIndisponibilizacao is null)");
		sqlStr.append("	when arquivo.manterdisponibilizacao = true ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = false then true end  ");
		sqlStr.append("and arquivo.origem = '").append(origem).append("' ");
		sqlStr.append("and (");
		sqlStr.append("	(arquivo.disciplina = ").append(disciplina).append(" and arquivo.turma is null and arquivo.professor is null) ");
		sqlStr.append("or (arquivo.disciplina = ").append(disciplina).append(" and arquivo.professor is null");
		sqlStr.append("	and arquivo.turma in (");
		sqlStr.append("		select turmapratica from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("' ");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turmapratica is not null");
		sqlStr.append("		union");
		sqlStr.append("		select turmateorica from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turmateorica is not null");
		sqlStr.append("		union");
		sqlStr.append("		select turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turmateorica is null");
		sqlStr.append("		and turmapratica is null");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append("		union");
		sqlStr.append("		select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("		inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and turmateorica is null and turmapratica is null");
		sqlStr.append("		inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("		where matricula = '").append(matricula).append("'");
		sqlStr.append("		and disciplina = ").append(disciplina);
		sqlStr.append("		and ano = '").append(ano).append("'");
		sqlStr.append("		and semestre = '").append(semestre).append("'");
		sqlStr.append("		and turma.situacao = 'AB'");
		sqlStr.append(")) ");
		sqlStr.append("or ((arquivo.turma = turmaprofessor.turma or arquivo.turma in (");
		sqlStr.append("			select turma from turmaagrupada where turmaorigem = turmaprofessor.turma");
		sqlStr.append("			and turmaagrupada.turma in (");
		sqlStr.append("				select turmapratica from matriculaperiodoturmadisciplina");
		sqlStr.append("				where matricula = '").append(matricula).append("'");
		sqlStr.append("				and disciplina = ").append(disciplina);
		sqlStr.append("				and ano = '").append(ano).append("' ");
		sqlStr.append("				and semestre = '").append(semestre).append("'");
		sqlStr.append("				and turmapratica is not null");
		sqlStr.append("				union");
		sqlStr.append("				select turmateorica from matriculaperiodoturmadisciplina");
		sqlStr.append("				where matricula = '").append(matricula).append("'");
		sqlStr.append("				and disciplina = ").append(disciplina);
		sqlStr.append("				and ano = '").append(ano).append("'");
		sqlStr.append("				and semestre = '").append(semestre).append("'");
		sqlStr.append("				and turmateorica is not null");
		sqlStr.append("				union");
		sqlStr.append("				select turma from matriculaperiodoturmadisciplina");
		sqlStr.append("				where matricula = '").append(matricula).append("'");
		sqlStr.append("				and disciplina = ").append(disciplina);
		sqlStr.append("				and ano = '").append(ano).append("'");
		sqlStr.append("				and semestre = '").append(semestre).append("'");
		sqlStr.append("				and turmateorica is null");
		sqlStr.append("				and turmapratica is null)))");
		sqlStr.append(" and arquivo.professor = turmaprofessor.professor and (arquivo.disciplina = ").append(disciplina).append(" or arquivo.disciplina = turmaprofessor.disciplina))");
		sqlStr.append("or ((arquivo.turma = turmaprofessor.turma or arquivo.turma in (");
		sqlStr.append("			select turma from turmaagrupada where turmaorigem = turmaprofessor.turma");
		sqlStr.append("			and turmaagrupada.turma in (");
		sqlStr.append("				select turmapratica from matriculaperiodoturmadisciplina");
		sqlStr.append("				where matricula = '").append(matricula).append("'");
		sqlStr.append("				and disciplina = ").append(disciplina);
		sqlStr.append("				and ano = '").append(ano).append("' ");
		sqlStr.append("				and semestre = '").append(semestre).append("'");
		sqlStr.append("				and turmapratica is not null");
		sqlStr.append("				union");
		sqlStr.append("				select turmateorica from matriculaperiodoturmadisciplina");
		sqlStr.append("				where matricula = '").append(matricula).append("'");
		sqlStr.append("				and disciplina = ").append(disciplina);
		sqlStr.append("				and ano = '").append(ano).append("'");
		sqlStr.append("				and semestre = '").append(semestre).append("'");
		sqlStr.append("				and turmateorica is not null");
		sqlStr.append("				union");
		sqlStr.append("				select turma from matriculaperiodoturmadisciplina");
		sqlStr.append("				where matricula = '").append(matricula).append("'");
		sqlStr.append("				and disciplina = ").append(disciplina);
		sqlStr.append("				and ano = '").append(ano).append("'");
		sqlStr.append("				and semestre = '").append(semestre).append("'");
		sqlStr.append("				and turmateorica is null");
		sqlStr.append("				and turmapratica is null)))");
		sqlStr.append("and arquivo.professor is null and (arquivo.disciplina = ").append(disciplina).append(" or arquivo.disciplina = turmaprofessor.disciplina))");
		sqlStr.append("	or (arquivo.turma is null and arquivo.professor = turmaprofessor.professor and (arquivo.disciplina = ").append(disciplina).append(" or arquivo.disciplina = turmaprofessor.disciplina))");
		sqlStr.append(")");
		sqlStr.append("order by arquivo.disciplina, arquivo.turma, arquivo.professor, indiceagrupador, indice");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaCompleta(tabelaResultado));
	}

	@Override
	public List<ArquivoVO> consultarMateriaisPendentesAluno(String matricula, String ano, String semestre, String origem, String periodicidade, Integer matriculaPeriodoTurmaDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(getSqlBaseConsultaMaterialPendenteAluno(matricula, ano, semestre, origem, periodicidade, matriculaPeriodoTurmaDisciplina));
		sqlStr.append("order by arquivo.disciplina, arquivo.turma, arquivo.professor, indiceagrupador, indice");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaCompleta(tabelaResultado));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarCodigoOrigemArquivo(ArquivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql  = new StringBuilder();
			sql.append("UPDATE arquivo set codOrigem=? WHERE (codigo = ?) ");
			if (usuario != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			}
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodOrigem(), obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public SqlRowSet consultarMateriaisAlunosQueSofreramAlteracao(Integer horas) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct t.cpf,  t.curso_codigo, t.turma_codigo, t.modulo_codigo,  t.material_codigo, t.material_titulo, t.material_descricao, "); 
		sb.append(" t.material_extensao, t.material_datacriacao, t.material_dataatualizacao, t.material_dataremovacao  from ( ") ;
		sb.append("select distinct prof.codigo as prof_codigo, prof.cpf as cpf, curso.codigo as curso_codigo, turma.codigo as turma_codigo, mptd.disciplina as modulo_codigo, ") 
		.append("arquivo.codigo as material_codigo, arquivo.descricao as material_titulo, arquivo.descricaoarquivo as material_descricao, ") 
		.append("case when arquivo.extensao <> '' then arquivo.extensao else substring( arquivo.nome from POSITION ('.' IN arquivo.nome) +1 for LENGTH(arquivo.nome)) end as material_extensao, arquivo.dataupload as material_datacriacao, arquivo.dataupload as material_dataatualizacao, null as material_dataremovacao ") 
		.append("from matriculaperiodoturmadisciplina mptd inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ") 
		.append("inner join matricula on matricula.matricula = matriculaperiodo.matricula inner join pessoa on pessoa.codigo = matricula.aluno ") 
		.append("inner join curso on curso.codigo = matricula.curso  ")
		.append(" inner join lateral (  ")
		.append(" select  turmanormal.* ")
		.append(" from turma as turmanormal  ")
		.append(" where (turmanormal.turmaagrupada = false and turmanormal.codigo = mptd.turma) ")
		.append("  union all ")
		.append(" select  agrupada.* ")
		.append(" from turma as agrupada ")
		.append("  where (agrupada.turmaagrupada  AND agrupada.codigo  in (SELECT turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turma = mptd.turma)) ")
		.append("  ) as turma on 1=1  ")
		.append(" inner join ( select arquivo.codigo, arquivo.professor, arquivo.disciplina, arquivo.turma, arquivo.nome, arquivo.descricao, arquivo.descricaoarquivo, arquivo.extensao, arquivo.dataupload ") 
		.append("from arquivo inner join disciplina on disciplina.codigo = arquivo.disciplina ") 
		.append("where arquivo.datadisponibilizacao >= (now() - '").append(horas).append(" hour'::interval) and arquivo.situacao = 'AT' and arquivo.origem = 'PR' ") 
		.append("and arquivo.dataDisponibilizacao <= current_timestamp and case when arquivo.manterdisponibilizacao = false  ") 
		.append("and arquivo.apresentarDeterminadoPeriodo = false then false when arquivo.manterdisponibilizacao = false  ") 
		.append("and arquivo.apresentarDeterminadoPeriodo = true then (arquivo.dataIndisponibilizacao >= current_timestamp or arquivo.dataIndisponibilizacao is null) ") 
		.append("when arquivo.manterdisponibilizacao = true and arquivo.apresentarDeterminadoPeriodo = false then true end   ")

		.append(") as arquivo on arquivo.disciplina = mptd.disciplina  ") 
		.append("inner join pessoa as prof on prof.codigo = case when arquivo.professor is null then (" + 
				"	select distinct horarioturmadiaitem.professor from horarioturmadiaitem" + 
				"	inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia" + 
				"	inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo" + 
				"	WHERE 1=1 " + 
				 "  AND (( horarioturma.turma = turma.codigo) OR ( horarioturma.turma IN (SELECT turmaagrupada.turmaorigem FROM turmaagrupada WHERE turmaagrupada.turma = turma.codigo)))  "
				+ " AND horarioturmadiaitem.disciplina = mptd.disciplina ) " + 
				"  ELSE arquivo.professor end ") 	
		.append("and arquivo.turma is null ") 
		.append("and mptd.reposicao = false ") 
		.append("where matricula.situacao = 'AT' and case when curso.niveleducacional = 'SU' then matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO') else 1=1 end ") 
		.append("group by prof.codigo, pessoa.cpf , curso.codigo , mptd.turma,turma.codigo , mptd.disciplina , arquivo.codigo , arquivo.descricao , arquivo.nome, arquivo.descricaoarquivo , arquivo.extensao , arquivo.dataupload , arquivo.dataupload, prof.cpf ");
		sb.append(" union all ");		
		sb.append("select distinct prof.codigo as prof_codigo, prof.cpf as cpf, curso.codigo as curso_codigo, turma.codigo as turma_codigo, mptd.disciplina as modulo_codigo, ") 
		.append("arquivo.codigo as material_codigo, arquivo.descricao as material_titulo, arquivo.descricaoarquivo as material_descricao, ") 
		.append("case when arquivo.extensao <> '' then arquivo.extensao else substring( arquivo.nome from POSITION ('.' IN arquivo.nome) +1 for LENGTH(arquivo.nome)) end as material_extensao, arquivo.dataupload as material_datacriacao, arquivo.dataupload as material_dataatualizacao, null as material_dataremovacao ") 
		.append("from matriculaperiodoturmadisciplina mptd inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ") 
		.append("inner join matricula on matricula.matricula = matriculaperiodo.matricula inner join pessoa on pessoa.codigo = matricula.aluno ") 
		.append("inner join curso on curso.codigo = matricula.curso  ")
		
		.append(" inner join lateral (  ")
		.append(" select  turmanormal.* ")
		.append(" from turma as turmanormal  ")
		.append(" where (turmanormal.turmaagrupada = false and turmanormal.codigo = mptd.turma) ")
		.append("  union all ")
		.append(" select  agrupada.* ")
		.append(" from turma as agrupada ")
		.append("  where (agrupada.turmaagrupada  AND agrupada.codigo  in (SELECT turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turma = mptd.turma)) ")
		.append("  ) as turma on 1=1  ")
		
		.append(" inner join ( select arquivo.codigo, arquivo.professor, arquivo.disciplina, arquivo.turma, arquivo.nome, arquivo.descricao, arquivo.descricaoarquivo, arquivo.extensao, arquivo.dataupload ") 
		.append("from arquivo inner join disciplina on disciplina.codigo = arquivo.disciplina ") 
		.append("where arquivo.datadisponibilizacao >= (now() - '").append(horas).append(" hour'::interval) and arquivo.situacao = 'AT' and arquivo.origem = 'PR' ") 
		.append("and arquivo.dataDisponibilizacao <= current_timestamp and case when arquivo.manterdisponibilizacao = false  ") 
		.append("and arquivo.apresentarDeterminadoPeriodo = false then false when arquivo.manterdisponibilizacao = false  ") 
		.append("and arquivo.apresentarDeterminadoPeriodo = true then (arquivo.dataIndisponibilizacao >= current_timestamp or arquivo.dataIndisponibilizacao is null) ") 
		.append("when arquivo.manterdisponibilizacao = true and arquivo.apresentarDeterminadoPeriodo = false then true end   ")
		.append(") as arquivo on arquivo.disciplina = mptd.disciplina  ") 
		.append("inner join pessoa as prof on prof.codigo = case when arquivo.professor is null then (" + 
				"	SELECT distinct horarioturmadiaitem.professor FROM horarioturmadiaitem " + 
				"	INNER JOIN horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia " + 
				"	INNER JOIN horarioturma 				ON horarioturmadia.horarioturma = horarioturma.codigo " + 
				"  WHERE 1=1 " + 
				 "  AND ((horarioturma.turma = turma.codigo) OR ( horarioturma.turma IN (SELECT turmaagrupada.turmaorigem FROM turmaagrupada WHERE turmaagrupada.turma = turma.codigo)))  "
				+ " AND horarioturmadiaitem.disciplina = mptd.disciplina " + 
				" ) ELSE arquivo.professor END ") 		
		.append("and arquivo.turma = turma.codigo ") 
		.append("and mptd.reposicao = false ")
		.append("where matricula.situacao = 'AT' and case when curso.niveleducacional = 'SU' then matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO') else 1=1 end ") 
		.append("group by prof.codigo, pessoa.cpf , curso.codigo , mptd.turma ,turma.codigo, mptd.disciplina , arquivo.codigo , arquivo.descricao , arquivo.nome, arquivo.descricaoarquivo , arquivo.extensao , arquivo.dataupload , arquivo.dataupload, prof.cpf) as t ");
		sb.append(" where exists (");
		sb.append(" select horarioturma.codigo from horarioturma");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");  		
		sb.append(" and horarioturmadiaitem.disciplina = t.modulo_codigo");
		sb.append(" and horarioturmadiaitem.professor = t.prof_codigo");
		sb.append(" where 1=1 "); 
		sb.append(" AND ((horarioturma.turma = t.turma_codigo) OR ( horarioturma.turma IN (SELECT turmaagrupada.turmaorigem FROM turmaagrupada WHERE turmaagrupada.turma = t.turma_codigo))) )"); 
		
		sb.append(" union");
		sb.append(" select distinct");
		sb.append(" professor.cpf as cpf,");
		sb.append(" curso.codigo as curso_codigo,");
		sb.append(" turma.codigo as turma_codigo,");
		sb.append(" programacaotutoriaonline.disciplina as modulo_codigo,");
		sb.append(" arquivo.codigo as material_codigo,");
		sb.append(" arquivo.descricao as material_titulo,");
		sb.append(" arquivo.descricaoarquivo as material_descricao,");
		sb.append(" case when arquivo.extensao <> '' then arquivo.extensao else substring( arquivo.nome from POSITION ('.' IN arquivo.nome) +1 for LENGTH(arquivo.nome)) end as material_extensao,");
		sb.append(" arquivo.dataupload as material_datacriacao,");
		sb.append(" arquivo.dataupload as material_dataatualizacao,");
		sb.append(" null as material_dataremovacao");
		sb.append(" from programacaotutoriaonline");
		sb.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sb.append(" inner join pessoa as professor on programacaotutoriaonlineprofessor.professor = professor.codigo");
		sb.append(" inner join turma on programacaotutoriaonline.turma = turma.codigo");
		sb.append(" INNER JOIN curso 	ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) ) ");
		sb.append(" inner join arquivo on arquivo.disciplina = programacaotutoriaonline.disciplina and arquivo.origem = 'PR'");
		sb.append(" and ((arquivo.turma is not null and arquivo.turma = turma.codigo) or arquivo.turma is null)");
		sb.append(" and ((arquivo.professor is not null and arquivo.professor = professor.codigo) or arquivo.professor is null)");
		sb.append(" where arquivo.datadisponibilizacao >= (now() - '").append(horas).append(" hour'::interval) and arquivo.situacao = 'AT'	");
		sb.append(" and arquivo.dataDisponibilizacao <= current_timestamp and case when arquivo.manterdisponibilizacao = false  ");
		sb.append(" and arquivo.apresentarDeterminadoPeriodo = false then false when arquivo.manterdisponibilizacao = false  ");
		sb.append(" and arquivo.apresentarDeterminadoPeriodo = true then (arquivo.dataIndisponibilizacao >= current_timestamp or arquivo.dataIndisponibilizacao is null) ");
		sb.append(" when arquivo.manterdisponibilizacao = true and arquivo.apresentarDeterminadoPeriodo = false then true end");
		sb.append(" and exists (");
		
		sb.append(" select mptd.codigo	from matricula");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula");
		sb.append(" and matriculaperiodo.codigo = (");
		sb.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sb.append(" order by mp.ano desc, mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)");
		sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where matricula.situacao = 'AT' ");
		sb.append(" and case when curso.niveleducacional = 'SU' then matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO') else 1=1 end");
		sb.append(" and mptd.professor 	= programacaotutoriaonlineprofessor.professor");
		sb.append(" and mptd.disciplina = programacaotutoriaonline.disciplina");
		sb.append(" AND (( mptd.turma = programacaotutoriaonline.turma) OR ( mptd.turma IN (SELECT turmaagrupada.turma FROM turmaagrupada WHERE turmaagrupada.turmaorigem = programacaotutoriaonline.turma))) ");
		sb.append(" and mptd.modalidadedisciplina = 'ON_LINE'");
		sb.append(" AND case when curso.periodicidade = 'SE' then  (mptd.ano || mptd.semestre) >= (programacaotutoriaonline.ano || programacaotutoriaonline.semestre) ");
		sb.append(" when curso.periodicidade = 'AN' then  (mptd.ano) >= (programacaotutoriaonline.ano) ");
		sb.append(" else 1=1 END ");
		sb.append(")");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}	
	
	public void validarConsultaAnoSemestreConformePeriodicidade(StringBuilder consulta, String periodicidade, String nomeCampoAno, String nomeCamposemestre, String ano, String semestre) {
		if (!periodicidade.equals("IN")) {
			consulta.append(" AND ").append(nomeCampoAno).append(" = '").append(ano).append("' ");
		}
		if (periodicidade.equals("SE")) {
			consulta.append(" AND ").append(nomeCamposemestre).append(" = '").append(semestre).append("' ");
		}
	}
	
	public List<ArquivoVO> consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(String pastaBaseArquivo, Integer codigoDisciplina) throws Exception {
		String urlExternoDownloadArquivo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo().getUrlExternoDownloadArquivo();
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, nome, pastabasearquivo, descricao from arquivo where pastabasearquivo = '").append(pastaBaseArquivo).append("' and disciplina = ").append(codigoDisciplina).append(" and manterDisponibilizacao = true");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ArquivoVO> listaArquivoVOs = new ArrayList<ArquivoVO>(0);
		while (dadosSQL.next()) {
			ArquivoVO obj = new ArquivoVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setPastaBaseArquivo(dadosSQL.getString("pastabasearquivo"));
			if(urlExternoDownloadArquivo.endsWith("/") || urlExternoDownloadArquivo.endsWith("\\")){
				obj.setDescricao(urlExternoDownloadArquivo +  File.separator + dadosSQL.getString("pastabasearquivo") +  File.separator + dadosSQL.getString("nome"));
			} else {
				obj.setDescricao(urlExternoDownloadArquivo + File.separator  + dadosSQL.getString("pastabasearquivo") +  File.separator + dadosSQL.getString("nome"));
			}
			listaArquivoVOs.add(obj);
		}
		return listaArquivoVOs;
	}
	
	public static void validarDadosMaterial(ArquivoVO obj , String nomeEntidade ) throws ConsistirException {
		
		
			if ((obj.getProfessor().getCodigo() == 0) && (nomeEntidade.equals("PostarMaterialComProfessorObrigatoriamenteInformado"))) {
				throw new ConsistirException("O Professor deve ser informado!");			
		   }if ((obj.getTurma().getCodigo() == 0) && (nomeEntidade.equals("PostarMaterialComTurmaObrigatoriamenteInformado"))) {
			throw new ConsistirException("O Turma deve ser informado!");		
	}
	}
	
    public SqlRowSet consultarArquivosQueForamExcluidos(Integer hora) {
    	String sqlStr = "SELECT turma.curso, prof.cpf as cpf, turma.curso as curso_codigo, logexclusaoarquivo.turma as turma_codigo, logexclusaoarquivo.disciplina as modulo_codigo, ";
		sqlStr += " logexclusaoarquivo.codigoarquivo as material_codigo, logexclusaoarquivo.nomearquivo as material_titulo, logexclusaoarquivo.descricaoarquivo as material_descricao, '' as material_extensao, ";
		sqlStr += " logexclusaoarquivo.dataupload as material_datacriacao, logexclusaoarquivo.dataupload as material_dataatualizacao, logexclusaoarquivo.dataexclusao as material_dataremovacao FROM LogExclusaoArquivo  ";
		sqlStr += " inner join turma on turma.codigo = LogExclusaoArquivo.turma ";
		sqlStr += "  INNER JOIN curso 			ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) ) ";
		sqlStr += " inner join pessoa as prof on prof.codigo = case when (coalesce(LogExclusaoArquivo.professor,0)  = 0) then (  ";
		sqlStr += " select distinct horarioturmadiaitem.professor from horarioturmadiaitem   ";
		sqlStr += " inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia   ";
		sqlStr += " inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo   ";
		sqlStr += " where horarioturma.turma = LogExclusaoArquivo.turma and horarioturmadiaitem.disciplina = LogExclusaoArquivo.disciplina  ) else LogExclusaoArquivo.professor end  ";
		sqlStr += " WHERE turma is not null and dataexclusao >= (now() - '" + hora + " hour'::interval) ";
		// adiciona arquivos que foram excluidos porem nao tem vinculo com turma
		sqlStr += " UNION ";
		sqlStr += " SELECT turma.curso, prof.cpf as cpf, turma.curso as curso_codigo, turma.codigo as turma_codigo, logexclusaoarquivo.disciplina as modulo_codigo, ";
		sqlStr += " logexclusaoarquivo.codigoarquivo as material_codigo, logexclusaoarquivo.nomearquivo as material_titulo, logexclusaoarquivo.descricaoarquivo as material_descricao, '' as material_extensao,  ";
		sqlStr += " logexclusaoarquivo.dataupload as material_datacriacao, logexclusaoarquivo.dataupload as material_dataatualizacao, logexclusaoarquivo.dataexclusao as material_dataremovacao 		FROM LogExclusaoArquivo  ";
		sqlStr += " inner join horarioturmadiaitem on horarioturmadiaitem.disciplina = logexclusaoarquivo.disciplina 		inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ";
		sqlStr += " inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma 		inner join turma on turma.codigo = horarioturma.turma  ";
		sqlStr += "  INNER JOIN curso 			ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) ) ";
		sqlStr += " inner join pessoa as prof on prof.codigo = case when (coalesce(LogExclusaoArquivo.professor,0)  = 0)  then (  		select distinct horarioturmadiaitem.professor from horarioturmadiaitem    ";
		sqlStr += " inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia   		inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo   ";
		sqlStr += " where horarioturma.turma = LogExclusaoArquivo.turma and horarioturmadiaitem.disciplina = LogExclusaoArquivo.disciplina  ) 		else LogExclusaoArquivo.professor end  		WHERE 1=1  ";
		sqlStr += " and prof.codigo = horarioturmadiaitem.professor 		and logexclusaoarquivo.turma = 0 ";
		sqlStr += " and dataexclusao >= (now() - '" + hora + " hour'::interval)  ";
		
		sqlStr += " UNION";
		sqlStr += " SELECT turma.curso, prof.cpf as cpf, turma.curso as curso_codigo, turma.codigo as turma_codigo, logexclusaoarquivo.disciplina as modulo_codigo,"; 
		sqlStr += " logexclusaoarquivo.codigoarquivo as material_codigo, logexclusaoarquivo.nomearquivo as material_titulo, logexclusaoarquivo.descricaoarquivo as material_descricao, '' as material_extensao,";  
		sqlStr += " logexclusaoarquivo.dataupload as material_datacriacao, logexclusaoarquivo.dataupload as material_dataatualizacao, logexclusaoarquivo.dataexclusao as material_dataremovacao";
		sqlStr += " FROM LogExclusaoArquivo";
		sqlStr += " inner join programacaotutoriaonline on programacaotutoriaonline.disciplina = logexclusaoarquivo.disciplina"; 		
		sqlStr += " inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo";
		sqlStr += " inner join turma on turma.codigo = programacaotutoriaonline.turma";
		sqlStr += "  INNER JOIN curso 			ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) ) ";
		sqlStr += " inner join pessoa as prof on prof.codigo = programacaotutoriaonlineprofessor.professor"; 		
		sqlStr += " WHERE  dataexclusao >= (now() - '" + hora + "hour'::interval)";
		sqlStr += " and ((LogExclusaoArquivo.turma is not null and LogExclusaoArquivo.turma > 0 and LogExclusaoArquivo.turma = turma.codigo) or (LogExclusaoArquivo.turma is null) or (LogExclusaoArquivo.turma = 0))";
		sqlStr += " and ((LogExclusaoArquivo.professor is not null and LogExclusaoArquivo.professor > 0 and LogExclusaoArquivo.professor = prof.codigo) or (LogExclusaoArquivo.professor is null) or (LogExclusaoArquivo.professor = 0))";
		
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	}
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean verificarExisteArquivoMesmoNomeAnoUpload(String valorConsulta, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT arquivo.codigo FROM arquivo WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') and extract(year from dataupload) = '" + ano +  "' limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ArquivoVO> consultarArquivosBibliotecaExterna(Integer codigoCatalogo) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("select codigo , codOrigem , nome , agrupador , dataupload , pastabasearquivo , descricaoArquivo , dataDisponibilizacao , extensao from arquivo where pastabasearquivo = 'arquivosBibliotecaExterna' and codOrigem ='").append(codigoCatalogo).append("'");
		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ArquivoVO> listaArquivoVOs = new ArrayList<ArquivoVO>(0);
		while (dadosSQL.next()) {
			ArquivoVO obj = new ArquivoVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setCodOrigem(dadosSQL.getInt("codOrigem"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setAgrupador(dadosSQL.getString("agrupador"));
			obj.setDataUpload(dadosSQL.getDate("dataupload"));
			obj.setPastaBaseArquivo(dadosSQL.getString("pastabasearquivo"));
			obj.setDescricaoArquivo(dadosSQL.getString("descricaoArquivo"));
			obj.setDataIndisponibilizacao(dadosSQL.getDate("dataDisponibilizacao"));
			obj.setExtensao(dadosSQL.getString("extensao"));
			
			listaArquivoVOs.add(obj);
		}
		return listaArquivoVOs;
	}

	
	@Override
	public ArquivoVO consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(Integer codigoFuncionario, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select arquivo.* from arquivo ");
		sqlStr.append(" inner join funcionario on funcionario.arquivoAssinatura = arquivo.codigo ");
		sqlStr.append(" where funcionario.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoFuncionario });
		if (!tabelaResultado.next()) {
			return new ArquivoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public String consultarCapaCatalogo(Integer codigoCatalogo) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("select  nome  from arquivo where pastabasearquivo = 'arquivosBibliotecaExterna' and agrupador = 'Capa' and codOrigem ='").append(codigoCatalogo).append("'");
		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		ArquivoVO obj = new ArquivoVO();
		while (dadosSQL.next()) {
			
			obj.setNome(dadosSQL.getString("nome"));
		}
		return obj.getNome();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDescricaoArquivo(final Integer arquivo, final String descricao, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Arquivo set descricao=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, descricao);
					sqlAlterar.setInt(++i, arquivo.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarApresentacaoArquivoInstitucionalProfessorCoordenadorAluno(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			String sql = "UPDATE arquivo set apresentarPortalProfessor=?, apresentarPortalCoordenador=?, apresentarPortalAluno=?, manterdisponibilizacao=?, arquivoAssinadoDigitalmente =? WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getApresentarPortalProfessor(), obj.getApresentarPortalCoordenador(), obj.getApresentarPortalAluno(), obj.getManterDisponibilizacao(),obj.getArquivoAssinadoDigitalmente(), obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarSituacaoArquivo(ArquivoVO obj, SituacaoArquivo situacaoArquivo, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			String sql = "UPDATE arquivo set situacao=? WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { situacaoArquivo.getValor(), obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}
	
	private String realizarRegraAndAnoSemestre(String ano, String semestre, String apelidoTabela) {
		StringBuilder str = new StringBuilder();
		if (Uteis.isAtributoPreenchido(ano)) {
			str.append(" AND ").append(apelidoTabela).append(".ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			str.append(" AND ").append(apelidoTabela).append(".semestre = '").append(semestre).append("' ");
		}
		return str.toString();
	}
	
	@Override
	public List<ArquivoVO> consultarArquivosQuestaoProcessoSeletivoPorDisciplina(String pastaBaseArquivo, Integer codigoDisciplina) throws Exception {
		String urlExternoDownloadArquivo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo().getUrlExternoDownloadArquivo();
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, nome, pastabasearquivo, descricao from arquivo where pastabasearquivo = '").append(pastaBaseArquivo).append("' and codorigem = ").append(codigoDisciplina).append(" and manterDisponibilizacao = true");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ArquivoVO> listaArquivoVOs = new ArrayList<ArquivoVO>(0);
		while (dadosSQL.next()) {
			ArquivoVO obj = new ArquivoVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setPastaBaseArquivo(dadosSQL.getString("pastabasearquivo"));
			if(urlExternoDownloadArquivo.endsWith("/") || urlExternoDownloadArquivo.endsWith("\\")){
				obj.setDescricao(urlExternoDownloadArquivo +  "/" + dadosSQL.getString("pastabasearquivo") +  "/" + dadosSQL.getString("nome"));
			} else {
				obj.setDescricao(urlExternoDownloadArquivo + "/" + dadosSQL.getString("pastabasearquivo") +  "/" + dadosSQL.getString("nome"));
}
			listaArquivoVOs.add(obj);
		}
		return listaArquivoVOs;
	}
	
	
	@Override
	public List<ArquivoVO> consultarPorCodOrigemTipoOrigemTipoRelatorio(Integer codigo, String tipoOrigem, TipoRelatorioEnum tipoRelatorio, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM arquivo WHERE codOrigem = ? AND origem = ? and tipoRelatorio = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, codigo, tipoOrigem, tipoRelatorio.name());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	@Override
	public Integer consultarQtdeMateriaisPendentesAluno(String matricula, String ano, String semestre, String origem, String periodicidade, Integer matriculaPeriodoTurmaDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select count(distinct codigo) as qtde from (");
		sqlStr.append(getSqlBaseConsultaMaterialPendenteAluno(matricula, ano, semestre, origem, periodicidade, matriculaPeriodoTurmaDisciplina));
		sqlStr.append(") as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			return  tabelaResultado.getInt("qtde");
		}
		return 0; 
		
	}
	
	private StringBuilder getSqlBaseConsultaMaterialPendenteAluno(String matricula, String ano, String semestre, String origem, String periodicidade, Integer matriculaPeriodoTurmaDisciplina) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(getSqlConsultaDadosCompletos());
		sqlStr.append(" LEFT JOIN (");
		sqlStr.append(" SELECT DISTINCT turma.codigo as turma, turma.turmaagrupada, mptd.disciplina, horarioturmadiaitem.professor");
		sqlStr.append(" FROM ( SELECT turmapratica as turma, matriculaperiodoturmadisciplina.disciplina FROM matriculaperiodoturmadisciplina WHERE matricula = '");
		sqlStr.append(matricula).append("' ");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND turmapratica IS NOT NULL");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION");
		sqlStr.append(" SELECT turmateorica as turma, matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);	
		sqlStr.append(" AND turmateorica IS NOT NULL");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION SELECT turma, matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND turmateorica IS NULL");
		sqlStr.append(" AND turmapratica IS NULL");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION");
		sqlStr.append(" SELECT t.codigo AS turma, matriculaperiodoturmadisciplina.disciplina FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica");
		sqlStr.append(" INNER JOIN turma AS t ON turmaagrupada.turmaorigem = t.codigo");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND t.situacao = 'AB'");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION SELECT t.codigo AS turma, matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica");
		sqlStr.append(" INNER JOIN turma AS t ON turmaagrupada.turmaorigem = t.codigo");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND t.situacao = 'AB'");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION ");
		sqlStr.append(" SELECT t.codigo AS turma, matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  AND turmateorica IS NULL");
		sqlStr.append(" AND turmapratica IS NULL");
		sqlStr.append("  INNER JOIN turma AS t ON turmaagrupada.turmaorigem = t.codigo ");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND t.situacao = 'AB'");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION");
		sqlStr.append(" SELECT t.codigo AS turma, disciplinaequivalente.equivalente");
		sqlStr.append(" FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" AND turmateorica IS NULL  AND turmapratica IS NULL");
		sqlStr.append(" INNER JOIN turma AS t ON turmaagrupada.turmaorigem = t.codigo");
		sqlStr.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND t.situacao = 'AB'");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" UNION");
		sqlStr.append(" SELECT t.codigo AS turma, disciplinaequivalente.disciplina");
		sqlStr.append(" FROM matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" AND turmateorica IS NULL  AND turmapratica IS NULL");
		sqlStr.append(" INNER JOIN turma AS t ON turmaagrupada.turmaorigem = t.codigo");
		sqlStr.append(" INNER JOIN disciplinaequivalente on disciplinaequivalente.equivalente = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("'");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "ano", "semestre", ano, semestre);
		sqlStr.append(" AND t.situacao = 'AB'");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" ) as mptd");
		sqlStr.append(" inner join  horarioturma on horarioturma.turma = mptd.turma");
		validarConsultaAnoSemestreConformePeriodicidade(sqlStr, periodicidade, "anovigente", "semestrevigente", ano, semestre);
		sqlStr.append(" inner join  turma on turma.codigo = horarioturma.turma");
		sqlStr.append(" inner join  horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner join  horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sqlStr.append(" where horarioturmadiaitem.disciplina = mptd.disciplina");
		sqlStr.append(" ) AS turmaprofessor ON turmaprofessor.disciplina = arquivo.disciplina");
		sqlStr.append(" where arquivo.situacao = 'AT' ");
		sqlStr.append(" and arquivo.dataDisponibilizacao <= current_timestamp ");
		sqlStr.append(" and case when arquivo.manterdisponibilizacao = false ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = false then false when arquivo.manterdisponibilizacao = false ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = true ");
		sqlStr.append("	then (arquivo.dataIndisponibilizacao >= current_timestamp or arquivo.dataIndisponibilizacao is null)");
		sqlStr.append("	when arquivo.manterdisponibilizacao = true ");
		sqlStr.append("	and arquivo.apresentarDeterminadoPeriodo = false then true end  ");
		sqlStr.append(" and arquivo.origem = '").append(origem).append("' ");
		sqlStr.append("	and ((arquivo.disciplina = turmaprofessor.disciplina and arquivo.turma is null and arquivo.professor is null) ");
		sqlStr.append("or (arquivo.disciplina = turmaprofessor.disciplina and arquivo.professor is null and arquivo.turma = turmaprofessor.turma) ");
		sqlStr.append("or (arquivo.disciplina = turmaprofessor.disciplina and arquivo.professor = turmaprofessor.professor and arquivo.turma = turmaprofessor.turma) ");
		sqlStr.append("or (arquivo.disciplina = turmaprofessor.disciplina and arquivo.professor = turmaprofessor.professor and arquivo.turma is null) ");
		sqlStr.append(" ) ");
		sqlStr.append("and not exists (select Download.codigo from Download INNER JOIN matriculaperiodo on download.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" WHERE download.arquivo = arquivo.codigo ");
		sqlStr.append(" AND matriculaperiodo.matricula = '").append(matricula).append("' ");
		sqlStr.append(" ) ");
		return sqlStr;
	}
	
	@Override
	public void validarDadosExtensaoArquivoTipoMarketing(ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws Exception {
		if (arquivoVO.getExtensao().equals("xls") || arquivoVO.getExtensao().equals("xlsx") || arquivoVO.getExtensao().equals("doc") || arquivoVO.getExtensao().equals("docm")
				 || arquivoVO.getExtensao().equals("docx") || arquivoVO.getExtensao().equals("txt") || arquivoVO.getExtensao().equals("pdf")) {
			throw new Exception("Extensão de arquivo não é válida para Comunicado Tipo Marketing. Por Favor realizar o Upload de uma imagem.");
		}
	}
	@Override
	public String  realizarDownloadArquivoAmazon(ArquivoVO obj, ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean disponibilizarArquivoAssinadoParaDowload) throws Exception {
		String nomeArquivo = Constantes.EMPTY;
		String caminhoTemporario = Constantes.EMPTY;
		try {
			if(disponibilizarArquivoAssinadoParaDowload) {
				caminhoTemporario = UteisJSF.getCaminhoWeb() + Constantes.relatorio ;
			}else {
				caminhoTemporario = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj, obj.getPastaBaseArquivo() , configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());	
			}
			File file = new File(caminhoTemporario);
			if(!file.exists()) {
				file.mkdirs();
			}
			nomeArquivo = caminhoTemporario + File.separator +  obj.getNome();
			file = new File (nomeArquivo);
			if(!file.exists()) {
				file.createNewFile();
			}
			String nomeArquivoUsar = obj.getDescricao().contains(".") ? obj.getDescricao() : obj.getDescricao() + (obj.getNome().substring(obj.getNome().lastIndexOf("."), obj.getNome().length()));
			if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getDescricao()))){
				getFacadeFactory().getArquivoHelper().copiarArquivoDaAmazonS3ParaServidorLocal(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getDescricao()), nomeArquivo, configuracaoGeralSistemaVO);
			} else if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), nomeArquivoUsar))){
				getFacadeFactory().getArquivoHelper().copiarArquivoDaAmazonS3ParaServidorLocal(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), nomeArquivoUsar), nomeArquivo, configuracaoGeralSistemaVO);
			} else if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getNome()))){
				getFacadeFactory().getArquivoHelper().copiarArquivoDaAmazonS3ParaServidorLocal(obj.recuperarNomeArquivoServidorExterno(obj.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), obj.getNome()), nomeArquivo, configuracaoGeralSistemaVO);
			}
		} catch (Exception e) {
			throw new Exception("Não foi possível realizar o download do servidor da Amazon - " + e.getMessage());
		}
		return nomeArquivo;
		
	}
	
	@Override
	public String realizarVisualizacaoPreview(ArquivoVO arquivoVO) throws Exception{
			if(arquivoVO.getIsImagem() || arquivoVO.getIsPdF()) {
				if(arquivoVO.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
					ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
					
						String nomeArquivoUsar = arquivoVO.getDescricao().contains(".") ? arquivoVO.getDescricao() : arquivoVO.getDescricao() + (arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf("."), arquivoVO.getNome().length()));
						if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
							return (servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));					
						}else {
							nomeArquivoUsar =  arquivoVO.getDescricao();
							if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
								return (servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
							}else {
								throw new Exception("Não foi encontrado no repositório da AMAZON o aquivo no caminho "+arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)+".");							
							}
						}
						
					
				}else {
					return (getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUrlExternoDownloadArquivo() + "/" + arquivoVO.getPastaBaseArquivo() +"/" + arquivoVO.getNome()+"?embedded=true").replace(File.separator, "/");					
				}
			}
			return "";
			
	}
	
	/**
	 * update vai ser utilizado para atualizar se um arquivo criado é um PDF/A ou não
	 * sendo utilizado a princípio na geração de documento assinado, sendo que
	 * o principal que está sendo utilizado é a ATA DE COLAÇÃO DE GRAU
	 * 
	 * @author Felipi Alves
	 * @chamado 44966
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarArquivoIsPdfA(ArquivoVO arquivoVO) {
		if (Uteis.isAtributoPreenchido(arquivoVO) && Objects.nonNull(arquivoVO.getArquivoIsPdfa())) {
			getConexao().getJdbcTemplate().update("UPDATE arquivo SET arquivoispdfa = ? WHERE codigo = ?", arquivoVO.getArquivoIsPdfa(), arquivoVO.getCodigo());
		}
	}
}