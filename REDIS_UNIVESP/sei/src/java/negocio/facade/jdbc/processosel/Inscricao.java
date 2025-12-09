package negocio.facade.jdbc.processosel;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import jobs.JobEnvioEmail;
import jobs.JobProvaProcessoSeletivo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutComprovanteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.InscricaoInterfaceFacade;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoAtaProvaRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscritoSalaRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoRedacaoRelVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>InscricaoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe <code>InscricaoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see InscricaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Inscricao extends ControleAcesso implements InscricaoInterfaceFacade {

	protected static String idEntidade;

	public Inscricao() throws Exception {
		super();
		setIdEntidade("Inscricao");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>InscricaoVO</code>.
	 */
	public InscricaoVO novo() throws Exception {
		Inscricao.incluir(getIdEntidade());
		InscricaoVO obj = new InscricaoVO();
		return obj;
	}

	public static void validarDados(InscricaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws ConsistirException {
		if ((obj.getProcSeletivo().getValorInscricao().doubleValue() == 0) && (obj.getSituacao().equals("PF"))) {
			obj.setSituacao("CO");
		}
		// if (obj.getProcSeletivo().getNrOpcoesCurso().equals("2")) {
		// if ((obj.getCursoOpcao2() == null) ||
		// (obj.getCursoOpcao2().getCodigo().intValue() == 0)) {
		// throw new
		// ConsistirException("O campo CURSO OPÇÃO 2 (Inscrição) deve ser informado.");
		// }
		// }
		// if (obj.getProcSeletivo().getNrOpcoesCurso().equals("3")) {
		// if ((obj.getCursoOpcao2() == null) ||
		// (obj.getCursoOpcao2().getCodigo().intValue() == 0)) {
		// throw new
		// ConsistirException("O campo CURSO OPÇÃO 2 (Inscrição) deve ser informado.");
		// }
		// if ((obj.getCursoOpcao3() == null) ||
		// (obj.getCursoOpcao3().getCodigo().intValue() == 0)) {
		// throw new
		// ConsistirException("O campo CURSO OPÇÃO 3 (Inscrição) deve ser informado.");
		// }
		// }
		if (!Uteis.isAtributoPreenchido(obj.getCandidato())) {
			throw new ConsistirException("O campo CANDIDATO (Inscrição) deve ser informado.");
		} else {
			if(obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {	
				InscricaoVO InscricaoExistente = null;
				try {
					InscricaoInterfaceFacade inscricaoInterfaceFacade = new Inscricao();
					InscricaoExistente = inscricaoInterfaceFacade.consultarPorCandidatoEProcessoSeletivo(obj.getUnidadeEnsino().getCodigo(),
							obj.getCandidato().getCodigo(), obj.getProcSeletivo().getCodigo(),
							obj.getItemProcessoSeletivoDataProva().getCodigo(), obj.getCodigo(), verificarAcesso,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
				} catch (Exception e) {}
				if(InscricaoExistente.getCodigo() > 0 ) {
					 throw new ConsistirException("Este CANDIDATO já possui uma inscrição ativa neste PROCESSO SELETIVO (Número da inscrição: " + InscricaoExistente.getCodigo() + ") para esta DATA.");
				}
			   }
		}
		if (!Uteis.isAtributoPreenchido(obj.getProcSeletivo())) {
			throw new ConsistirException("O campo PROCESSO SELETIVO (Inscrição) deve ser informado.");
		} else {
			if (obj.getLiberadaForaPrazo().equals(Boolean.FALSE)) {
				Date dataInicial = Uteis.getDateTime(obj.getProcSeletivo().getDataInicioInternet(), 0, 0, 0);
				Date dataFinal = Uteis.getDateTime(obj.getProcSeletivo().getDataFimInternet(), 23, 59, 59);
				if ((obj.getData().compareTo(dataInicial) < 0) || (obj.getData().compareTo(dataFinal) > 0)) {
					throw new ConsistirException("O PROCESSO SELETIVO (Inscrição) está fora do prazo regular. O período para inscrição é "  + Uteis.getData(dataInicial) + " à " + Uteis.getData(dataFinal) + ".");
				}
			}
		}
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Inscrição) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(obj.getCursoOpcao1())) {
			throw new ConsistirException("O campo CURSO OPÇÃO 1 (Inscrição) deve ser informado.");
		}

		if (obj.getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Inscrição) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getItemProcessoSeletivoDataProva()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(obj.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo())) {
			throw new ConsistirException("O campo DATA PROVA (Inscrição) deve ser informado.");
		}
		// if ((obj.getOpcaoLinguaEstrangeira() == null) ||
		// (obj.getOpcaoLinguaEstrangeira().getCodigo().intValue() == 0)) {
		// throw new
		// ConsistirException("O campo OPÇÃO LINGUA ESTRANGEIRA (Inscrição) deve ser informado.");
		// }
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>InscricaoVO</code>. Todos os tipos de consistência de dados são e devem
	 * ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(InscricaoVO obj, UsuarioVO usuario) throws ConsistirException {
		validarDados(obj, true, usuario);
	}

	public void validarDadosSituacaoInscricao(InscricaoVO inscricaoVO) throws Exception {
		if (inscricaoVO.getSituacao().equals("PF") || (inscricaoVO.getSituacao().equals("CO") && (inscricaoVO.getProcSeletivo().getValorInscricao() <= 0.0))) {
			if(UteisData.getDataComMinutos(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()).before(new Date())) {
				throw new Exception(UteisJSF.internacionalizar("msg_processoSeletivo_naoPossivelAlterarInscricaoAposDataProva"));
			}
		} 
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InscricaoVO gravarInscricaoCandidato(InscricaoVO inscricaoVO, ArquivoVO arquivoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroPadraoSistemaVO, UsuarioVO usuarioVO) throws Exception {
		inscricaoVO.setInscricaoPresencial(Boolean.FALSE);
	    getFacadeFactory().getInscricaoFacade().validarDadosUnicidadeCandidatoCurso(inscricaoVO.getCursoOpcao1().getCurso().getCodigo(), inscricaoVO.getCursoOpcao2().getCurso().getCodigo(), inscricaoVO.getCursoOpcao3().getCurso().getCodigo(), inscricaoVO.getCandidato().getCodigo() ,inscricaoVO.getProcSeletivo().getPermitirAlunosMatriculadosInscreverMesmoCurso());
		if (inscricaoVO.getCodigo().intValue() == 0) {
			validarDadosSituacaoInscricao(inscricaoVO);
			getFacadeFactory().getInscricaoFacade().incluir(inscricaoVO, arquivoVO, false, configuracaoFinanceiroPadraoSistemaVO, usuarioVO);
		} else {
			Boolean houveAlteracaoDadosEtapa1 = consultarAlteracaoDadosJaGravadosEtapa1PorCodigoInscricao(inscricaoVO.getCodigo(), inscricaoVO.getUnidadeEnsino().getCodigo(), inscricaoVO.getProcSeletivo().getCodigo(), inscricaoVO.getCursoOpcao1().getCodigo(), inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva(), usuarioVO);
			if (houveAlteracaoDadosEtapa1) {
				inscricaoVO = realizarCriacaoNovaInscricaoCandidatoAlterouDadosEtapa1(inscricaoVO, usuarioVO);
				inscricaoVO.setSituacaoInscricao(SituacaoInscricaoEnum.ATIVO);
				if (UteisData.getDataComMinutos(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()).before(new Date())) {
					throw new Exception(UteisJSF.internacionalizar("msg_processoSeletivo_naoPossivelAlterarInscricaoAposDataProva"));
				}
				if(inscricaoVO.getProcSeletivo().getValorInscricao().equals(0.0)){
					inscricaoVO.setContaReceber(0);
					inscricaoVO.setContaReceberVO(null);
				}
				getFacadeFactory().getInscricaoFacade().incluir(inscricaoVO, arquivoVO, false, configuracaoFinanceiroPadraoSistemaVO, usuarioVO);
				
			} else {
				if(inscricaoVO.getProcSeletivo().getValorInscricao().equals(0.0)){
					inscricaoVO.setContaReceber(0);
					inscricaoVO.setContaReceberVO(null);
				}
				validarDadosSituacaoInscricao(inscricaoVO);
				getFacadeFactory().getInscricaoFacade().alterar(inscricaoVO, arquivoVO, false, usuarioVO, configuracaoFinanceiroPadraoSistemaVO);
			}
		}
		return inscricaoVO;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InscricaoVO realizarCriacaoNovaInscricaoCandidatoAlterouDadosEtapa1(InscricaoVO inscricaoAtualVO, UsuarioVO usuarioVO) {
		InscricaoVO inscricaoNovaVO = new InscricaoVO();
		inscricaoNovaVO.setNrdocumento(inscricaoAtualVO.getNrdocumento());
		inscricaoNovaVO.setData(new Date());
		inscricaoNovaVO.setSituacao("PF");
		inscricaoNovaVO.setSituacaoInscricao(inscricaoAtualVO.getSituacaoInscricao());
		inscricaoNovaVO.setDataLiberacaoPgtoInsc(inscricaoAtualVO.getDataLiberacaoPgtoInsc());
		inscricaoNovaVO.setOpcaoLinguaEstrangeira(inscricaoAtualVO.getOpcaoLinguaEstrangeira()); 
		inscricaoNovaVO.setFormaAcessoProcSeletivo(inscricaoAtualVO.getFormaAcessoProcSeletivo()); 
		inscricaoNovaVO.setPortadorNecessidadeEspecial(inscricaoAtualVO.getPortadorNecessidadeEspecial());
		inscricaoNovaVO.setDescricaoNecessidadeEspecial(inscricaoAtualVO.getDescricaoNecessidadeEspecial());
		inscricaoNovaVO.setLiberadaForaPrazo(inscricaoAtualVO.getLiberadaForaPrazo());
		inscricaoNovaVO.setDataLiberacaoForaPrazo(inscricaoAtualVO.getDataLiberacaoForaPrazo());
		inscricaoNovaVO.setChamada(inscricaoAtualVO.getChamada());
		inscricaoNovaVO.setSala(inscricaoAtualVO.getSala());
		inscricaoNovaVO.setUnidadeEnsino(inscricaoAtualVO.getUnidadeEnsino());
		inscricaoNovaVO.setCandidato(inscricaoAtualVO.getCandidato());
		inscricaoNovaVO.setProcSeletivo(inscricaoAtualVO.getProcSeletivo());
		inscricaoNovaVO.setCursoOpcao1(inscricaoAtualVO.getCursoOpcao1());
		inscricaoNovaVO.setCursoOpcao2(inscricaoAtualVO.getCursoOpcao2());
		inscricaoNovaVO.setCursoOpcao3(inscricaoAtualVO.getCursoOpcao3());
		inscricaoNovaVO.setResponsavel(usuarioVO);
		inscricaoNovaVO.setRespLiberacaoPgtoInsc(inscricaoAtualVO.getRespLiberacaoPgtoInsc());
		inscricaoNovaVO.setResponsavelLiberacaoForaPrazo(inscricaoAtualVO.getResponsavelLiberacaoForaPrazo());
		inscricaoNovaVO.setQuestionarioVO(inscricaoAtualVO.getQuestionarioVO());
		inscricaoNovaVO.setInscricaoPresencial(inscricaoAtualVO.getInscricaoPresencial());
		inscricaoNovaVO.setCandidatoConvocadoMatricula(inscricaoAtualVO.getCandidatoConvocadoMatricula());
		inscricaoNovaVO.setItemProcessoSeletivoDataProva(inscricaoAtualVO.getItemProcessoSeletivoDataProva());
		inscricaoNovaVO.setSalaAlterar(inscricaoAtualVO.getSalaAlterar());
		inscricaoNovaVO.setSelecionar(inscricaoAtualVO.getSelecionar());
		inscricaoNovaVO.setMatriculaVO(inscricaoAtualVO.getMatriculaVO());
		inscricaoNovaVO.setFormaIngresso(inscricaoAtualVO.getFormaIngresso());
		inscricaoNovaVO.setArquivoVO(inscricaoAtualVO.getArquivoVO());
		return inscricaoNovaVO;
	}
	
	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>InscricaoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(InscricaoVO obj, ArquivoVO arquivoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		incluir(obj, arquivoVO, true, configuracaoFinanceiro, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InscricaoVO obj, ArquivoVO arquivoVO, boolean verificarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, verificarAcesso, usuario);
			validarArquivoInscricao(obj, arquivoVO);
			if (obj.getQuestionarioVO().getCodigo().intValue() > 0) {
				List<RespostaAvaliacaoInstitucionalDWVO> listaRespostaQuestionario = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().gerarListaRespostaAluno(null, null, obj.getCandidato().getCodigo(), TipoPessoa.CANDIDATO.getValor(), obj.getUnidadeEnsino().getCodigo(), null, obj.getQuestionarioVO(), "", obj.getCodigo(), obj.getProcSeletivo().getCodigo(), 0, 0, 0, false, null);
				for (RespostaAvaliacaoInstitucionalDWVO objResp : listaRespostaQuestionario) {
					RespostaAvaliacaoInstitucionalDWVO.validarDados(objResp);
				}
			}
			if (!Uteis.isAtributoPreenchido(obj.getProcSeletivo().getValorInscricao())) {
				obj.setSituacao("CO");
			}
			Inscricao.incluir(getIdEntidade(), verificarAcesso, usuario);
			final String sql = "INSERT INTO Inscricao( unidadeEnsino, candidato, procSeletivo, cursoOpcao1, cursoOpcao2, cursoOpcao3, data, situacao, responsavel, respLiberacaoPgtoInsc, " + "dataLiberacaoPgtoInsc, opcaoLinguaEstrangeira, formaAcessoProcSeletivo, portadorNecessidadeEspecial, descricaoNecessidadeEspecial, liberadaForaPrazo, responsavelLiberacaoForaPrazo, " + "dataLiberacaoForaPrazo, contaReceber, nrdocumento, inscricaoPresencial, itemProcessoSeletivoDataProva, chamada, situacaoInscricao, gabarito, provaProcessoSeletivo, formaIngresso, classificacao, sobreBolsasEAuxilios, autodeclaracaoPretoPardoOuIndigena, inscricaoProvenienteImportacao, escolaPublica, candidatoConvocadoMatricula ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlInserir.setInt(2, obj.getCandidato().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getProcSeletivo().getCodigo().intValue());
					sqlInserir.setInt(4, obj.getCursoOpcao1().getCodigo().intValue());
					if (obj.getCursoOpcao2().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getCursoOpcao2().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getCursoOpcao3().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getCursoOpcao3().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(8, obj.getSituacao());
					sqlInserir.setInt(9, obj.getResponsavel().getCodigo().intValue());
					sqlInserir.setInt(10, obj.getRespLiberacaoPgtoInsc().getCodigo().intValue());
					sqlInserir.setDate(11, Uteis.getDataJDBC(obj.getDataLiberacaoPgtoInsc()));
					if (obj.getOpcaoLinguaEstrangeira().getCodigo().intValue() > 0) {
						sqlInserir.setInt(12, obj.getOpcaoLinguaEstrangeira().getCodigo());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setString(13, obj.getFormaAcessoProcSeletivo());
					sqlInserir.setBoolean(14, obj.isPortadorNecessidadeEspecial().booleanValue());
					sqlInserir.setString(15, obj.getDescricaoNecessidadeEspecial());
					sqlInserir.setBoolean(16, obj.isLiberadaForaPrazo().booleanValue());
					sqlInserir.setInt(17, obj.getResponsavelLiberacaoForaPrazo().getCodigo().intValue());
					sqlInserir.setDate(18, Uteis.getDataJDBC(obj.getDataLiberacaoForaPrazo()));
					if (obj.getContaReceber().intValue() != 0) {
						sqlInserir.setInt(19, obj.getContaReceber().intValue());
					} else {
						sqlInserir.setNull(19, 0);
					}
					sqlInserir.setString(20, obj.getNrdocumento());
					// if (obj.getDataProva() == null) {
					// sqlInserir.setNull(21, 0);
					// } else {
					// sqlInserir.setDate(21,
					// Uteis.getDataJDBC(obj.getDataProva()));
					// }
					// sqlInserir.setString(22, obj.getHora());
					sqlInserir.setBoolean(21, obj.getInscricaoPresencial());
					sqlInserir.setInt(22, obj.getItemProcessoSeletivoDataProva().getCodigo());
					sqlInserir.setInt(23, obj.getChamada());
					sqlInserir.setString(24, obj.getSituacaoInscricao().name());
					if (Uteis.isAtributoPreenchido(obj.getGabaritoVO())) {
						sqlInserir.setInt(25, obj.getGabaritoVO().getCodigo());
					} else {
						sqlInserir.setNull(25, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getProvaProcessoSeletivoVO())) {
						sqlInserir.setInt(26, obj.getProvaProcessoSeletivoVO().getCodigo());
					} else {
						sqlInserir.setNull(26, 0);
					}
					sqlInserir.setString(27, obj.getFormaIngresso());
					sqlInserir.setInt(28, obj.getClassificacao());
					sqlInserir.setBoolean(29, obj.getSobreBolsasEAuxilios());
					sqlInserir.setBoolean(30, obj.getAutodeclaracaoPretoPardoOuIndigena());
					sqlInserir.setBoolean(31, obj.getInscricaoProvenienteImportacao());
					sqlInserir.setBoolean(32, obj.getEscolaPublica());
					sqlInserir.setBoolean(33, obj.getCandidatoConvocadoMatricula());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			if (Uteis.isAtributoPreenchido(obj.getProcSeletivo().getValorInscricao())) {
				montarContaReceber(obj, configuracaoFinanceiro, usuario, obj.getLiberarPagamento().booleanValue());
			}

			incluirRespostaQuestionario(obj, usuario);
			obj.setNovoObj(Boolean.FALSE);
			if (!obj.getInscricaoProvenienteImportacao()) {
				Boolean usuarioExiste = getFacadeFactory().getUsuarioFacade().consultarPorCpfPessoaSeUsuarioExiste(obj.getCandidato().getCPF(), false, usuario);
				if (!usuarioExiste && !obj.getCandidato().getAluno() && !obj.getCandidato().getFuncionario() && !obj.getCandidato().getProfessor() && !obj.getCandidato().getCoordenador()) {
					criarUsuarioVO(obj, usuario);
				}
				persistirArquivoInscricaoProcessoSeletivo(obj, arquivoVO, usuario);
			}
			
			Thread gerarCampanhaCRM = new Thread(new CampanhaCRMInscricaoProcessoSeletivo((InscricaoVO)obj.clone(), usuario != null ? (UsuarioVO)usuario.clone() : usuario));
			gerarCampanhaCRM.start();					
			Thread enviarComunicadoInscricaoProcessoSeletivo = new Thread(new EnviarComunicadoNovaInscricaoProcessoSeletivo((InscricaoVO)obj.clone()));
			enviarComunicadoInscricaoProcessoSeletivo.start();
		} catch (Exception e) {			
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	public void persistirArquivoInscricaoProcessoSeletivo(InscricaoVO inscricaoVO, ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(arquivoVO.getCodigo())) {
			arquivoVO.setCodOrigem(inscricaoVO.getCodigo());  
			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES_TMP);
			getFacadeFactory().getArquivoFacade().alterar(arquivoVO, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO));
		} else if (!arquivoVO.getNome().equals("") && arquivoVO.getPastaBaseArquivoEnum() != null && arquivoVO.getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES_TMP)) {
			arquivoVO.setCodOrigem(inscricaoVO.getCodigo());            			
			arquivoVO.setOrigem(OrigemArquivo.PROCESSO_SELETIVO_INSCRICAO.getValor());
			if(arquivoVO.getCpfAlunoDocumentacao().equals("")) {
				arquivoVO.setCpfAlunoDocumentacao(inscricaoVO.getCandidato().getCPF());
			}
			getFacadeFactory().getArquivoFacade().incluir(arquivoVO, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO));			   
		}    
	}

	public void validarOutraInscricaoMudandoSituacaoDaMesmaParaCancelaOutraInscricao(InscricaoVO obj) {

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirRespostaQuestionario(InscricaoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getQuestionarioVO().getCodigo().intValue() > 0) {
			List<RespostaAvaliacaoInstitucionalDWVO> listaRespostaQuestionario = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().gerarListaRespostaAluno(null, null, obj.getCandidato().getCodigo(), TipoPessoa.CANDIDATO.getValor(), obj.getUnidadeEnsino().getCodigo(), null, obj.getQuestionarioVO(), "", obj.getCodigo(), obj.getProcSeletivo().getCodigo(), 0, 0, 0, false, null);
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().incluirTodas(listaRespostaQuestionario, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarUsuarioVO(InscricaoVO obj, UsuarioVO usuario) throws Exception {
		UsuarioVO usuarioVO = new UsuarioVO();
		usuarioVO.getPessoa().setCodigo(obj.getCandidato().getCodigo());
		usuarioVO.getPessoa().setNome(obj.getCandidato().getNome());
		usuarioVO.setNome(obj.getCandidato().getNome());
		usuarioVO.setSenha(Uteis.retirarMascaraCPF(obj.getCandidato().getCPF()));
		usuarioVO.setUsername(Uteis.retirarMascaraCPF(obj.getCandidato().getCPF()));
		usuarioVO.setTipoUsuario("CA");
		UsuarioPerfilAcessoVO usuarioPerfilAcesso = new UsuarioPerfilAcessoVO();
		usuarioPerfilAcesso.getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsino().getCodigo());
		usuarioPerfilAcesso.setPerfilAcesso(consultarPerfilAcessoPadrao(obj.getUnidadeEnsino().getCodigo(), usuario));
		usuarioVO.adicionarObjUsuarioPerfilAcessoVOs(usuarioPerfilAcesso);
		getFacadeFactory().getUsuarioFacade().incluir(usuarioVO, false, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarNumeroContaReceber(final InscricaoVO obj, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Inscricao set contaReceber=?, nrDocumento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getContaReceber().intValue());
				sqlAlterar.setString(2, obj.getNrdocumento());
				sqlAlterar.setInt(3, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroNotificacaoVencimentoInscricao(final Integer inscricao, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Inscricao set dataNotificouVencInscricao=current_date WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, inscricao.intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void montarContaReceber(InscricaoVO inscricao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, Boolean liberarPagamento) throws Exception {
		ProcSeletivoVO procSeletivo;
		procSeletivo = inscricao.getProcSeletivo();
		ContaReceberVO obj = new ContaReceberVO();
		ConfiguracaoFinanceiroVO config = consultarConfiguracaoFinanceiro(usuario, inscricao.getUnidadeEnsino().getCodigo());
		obj.setCandidato(inscricao.getCandidato());
		obj.getCentroReceita().setCodigo(config.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo().intValue());
		obj.setSituacao("AR");
		obj.setCodOrigem(String.valueOf(inscricao.getCodigo()));
		if (!inscricao.getUnidadeEnsino().getContaCorrentePadraoProcessoSeletivo().equals(0)) {
			obj.setContaCorrente(inscricao.getUnidadeEnsino().getContaCorrentePadraoProcessoSeletivo());
		} else {
			obj.setContaCorrente(config.getContaCorrentePadraoProcessoSeletivo());
		}
		obj.setData(inscricao.getData());
		inscricao.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(inscricao.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		if (inscricao.getProcSeletivo().getInformarQtdeDiasVencimentoAposInscricao() && inscricao.getProcSeletivo().getQtdeDiasVencimentoAposInscricao() > 0) {
			obj.setDataVencimento(Uteis.obterDataAvancada(inscricao.getData(), inscricao.getProcSeletivo().getQtdeDiasVencimentoAposInscricao()));
		} else {
			obj.setDataVencimento(inscricao.getItemProcessoSeletivoDataProva().getDataProva());
		}
		if(obj.getDataVencimento().after(inscricao.getItemProcessoSeletivoDataProva().getDataLimiteAdiarVencimentoInscricao())){
			obj.setDataVencimento(inscricao.getItemProcessoSeletivoDataProva().getDataLimiteAdiarVencimentoInscricao());
		}
		obj.setDescricaoPagamento("Nº inscrição: " + inscricao.getCodigo() + "\nTaxa de Inscrição em " + inscricao.getProcSeletivo().getDescricao());
		obj.setJuro(0.0);
		obj.setTipoBoleto("PC");
		obj.setJuroPorcentagem(0.0);
		obj.setMulta(0.0);
		obj.setMultaPorcentagem(0.0);
		obj.setTipoPessoa("CA");
		obj.setParcela("1/1");
		obj.setTipoOrigem("IPS");
		obj.setPessoa(inscricao.getCandidato());
		obj.getUnidadeEnsino().setCodigo(inscricao.getUnidadeEnsino().getCodigo());
		obj.setValor(procSeletivo.getValorInscricao());
		obj.setValorBaseContaReceber(procSeletivo.getValorInscricao());
		obj.setValorDescontoRecebido(0.0);
		obj.setValorDescontoInstituicao(0.0);
		obj.setValorDescontoConvenio(0.0);
		
		getFacadeFactory().getContaReceberFacade().incluir(obj, false, configuracaoFinanceiro, usuario);
		if (liberarPagamento) {
			realizarBaixaContaReceberInscricaoPorLiberacaoFinanceira(inscricao, obj, configuracaoFinanceiro, usuario);			
		}
		inscricao.setContaReceber(obj.getCodigo().intValue());
		inscricao.setContaReceberVO(obj);
		inscricao.setNrdocumento(obj.getNrDocumento());
		gravarNumeroContaReceber(inscricao, usuario);
		realizarValidacaoPermissaoPagamentoInscricao(inscricao, configuracaoFinanceiro, usuario) ;
			
	}

	public ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiro(UsuarioVO usuario, Integer codigoUnidadeEnsino) throws Exception {
		ConfiguracaoFinanceiroVO config = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, usuario, codigoUnidadeEnsino);
		if (!Uteis.isAtributoPreenchido(config.getContaCorrentePadraoProcessoSeletivo())) {
			throw new ConsistirException("Não existe Conta Corrente padrão para processo seletivo. Vá até Configurações/Financeiro e configure uma.");
		}
		if (!Uteis.isAtributoPreenchido(config.getCentroReceitaInscricaoProcessoSeletivoPadrao())) {
			throw new ConsistirException("Não existe Centro de Receita padrão para processo seletivo. Vá até Configurações/Financeiro e configure uma.");
		}
		return config;
	}

	public PerfilAcessoVO consultarPerfilAcessoPadrao(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, unidadeEnsino);
		if (Uteis.isAtributoPreenchido(conf) && Uteis.isAtributoPreenchido(conf.getPerfilPadraoAluno())) {
			return conf.getPerfilPadraoAluno();
		}
		throw new Exception("Não Existe nenhum perfil de acesso padrão cadastrado para efetuar a matricula.");
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>InscricaoVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(InscricaoVO obj, ArquivoVO arquivoVO, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		alterar(obj, arquivoVO, true, usuario, configuracaoFinanceiroVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InscricaoVO obj, ArquivoVO arquivoVO, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		Integer codigoContaReceber = obj.getContaReceber();		
		try {
			validarDados(obj, verificarAcesso, usuario);
			validarArquivoInscricao(obj, arquivoVO);
			boolean valor = false;
			Inscricao.alterar(getIdEntidade(), verificarAcesso, usuario);
			if (obj.getLiberarPagamento()) {
				ContaReceberVO contaReceberVO = new ContaReceberVO();
				contaReceberVO.setCodigo(obj.getContaReceber().intValue());
				getFacadeFactory().getContaReceberFacade().carregarDados(contaReceberVO, NivelMontarDados.BASICO, configuracaoFinanceiroVO, usuario);
				realizarBaixaContaReceberInscricaoPorLiberacaoFinanceira(obj, contaReceberVO, configuracaoFinanceiroVO, usuario);				
				valor = true;
			}
			final String sql = "UPDATE Inscricao set unidadeEnsino=?, candidato=?, procSeletivo=?, cursoOpcao1=?, cursoOpcao2=?,  cursoOpcao3=?, data=?, situacao=?, responsavel=?, respLiberacaoPgtoInsc=?, dataLiberacaoPgtoInsc=?, opcaoLinguaEstrangeira=?, formaAcessoProcSeletivo=?, portadorNecessidadeEspecial=?, descricaoNecessidadeEspecial=?, liberadaForaPrazo=?, responsavelLiberacaoForaPrazo=?, dataLiberacaoForaPrazo=?, contaReceber=?, nrdocumento=?, inscricaoPresencial=?, sala = ?, itemProcessoSeletivoDataProva = ?, chamada = ?, situacaoInscricao=?, gabarito=?, provaProcessoSeletivo=?, formaIngresso=? , dataHoraInicio=?  , dataHoraTermino=?, classificacao=?, sobreBolsasEAuxilios=?, autodeclaracaoPretoPardoOuIndigena=?, escolaPublica=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlAlterar.setInt(2, obj.getCandidato().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getProcSeletivo().getCodigo().intValue());
					sqlAlterar.setInt(4, obj.getCursoOpcao1().getCodigo().intValue());
					if (obj.getCursoOpcao2().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getCursoOpcao2().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getCursoOpcao3().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getCursoOpcao3().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(8, obj.getSituacao());
					sqlAlterar.setInt(9, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setInt(10, obj.getRespLiberacaoPgtoInsc().getCodigo().intValue());
					sqlAlterar.setDate(11, Uteis.getDataJDBC(obj.getDataLiberacaoPgtoInsc()));
					if (obj.getOpcaoLinguaEstrangeira().getCodigo().intValue() > 0) {
						sqlAlterar.setInt(12, obj.getOpcaoLinguaEstrangeira().getCodigo());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setString(13, obj.getFormaAcessoProcSeletivo());
					sqlAlterar.setBoolean(14, obj.isPortadorNecessidadeEspecial().booleanValue());
					sqlAlterar.setString(15, obj.getDescricaoNecessidadeEspecial());
					sqlAlterar.setBoolean(16, obj.isLiberadaForaPrazo().booleanValue());
					sqlAlterar.setInt(17, obj.getResponsavelLiberacaoForaPrazo().getCodigo().intValue());
					sqlAlterar.setDate(18, Uteis.getDataJDBC(obj.getDataLiberacaoForaPrazo()));
					if (obj.getContaReceber().intValue() != 0) {
						sqlAlterar.setInt(19, obj.getContaReceber().intValue());
					} else {
						sqlAlterar.setNull(19, 0);
					}
					sqlAlterar.setString(20, obj.getNrdocumento());
					// if (obj.getDataProva() == null) {
					// sqlAlterar.setNull(21, 0);
					// } else {
					// sqlAlterar.setDate(21,
					// Uteis.getDataJDBC(obj.getDataProva()));
					// }
					// sqlAlterar.setString(22, obj.getHora());
					sqlAlterar.setBoolean(21, obj.getInscricaoPresencial());
					if (obj.getSala().getCodigo() > 0) {
						sqlAlterar.setInt(22, obj.getSala().getCodigo());
					} else {
						sqlAlterar.setNull(22, 0);
					}
					sqlAlterar.setInt(23, obj.getItemProcessoSeletivoDataProva().getCodigo().intValue());
					sqlAlterar.setInt(24, obj.getChamada());
					sqlAlterar.setString(25, obj.getSituacaoInscricao().name());
					if (Uteis.isAtributoPreenchido(obj.getGabaritoVO())) {
						sqlAlterar.setInt(26, obj.getGabaritoVO().getCodigo());
					} else {
						sqlAlterar.setNull(26, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getProvaProcessoSeletivoVO())) {
						sqlAlterar.setInt(27, obj.getProvaProcessoSeletivoVO().getCodigo());
					} else {
						sqlAlterar.setNull(27, 0);
					}
					sqlAlterar.setString(28, obj.getFormaIngresso());
					sqlAlterar.setTimestamp(29, obj.getDataHoraInicio());
				 	sqlAlterar.setTimestamp(30, obj.getDataHoraTermino());
				 	sqlAlterar.setInt(31, obj.getClassificacao());
				 	sqlAlterar.setBoolean(32, obj.getSobreBolsasEAuxilios());
				 	sqlAlterar.setBoolean(33, obj.getAutodeclaracaoPretoPardoOuIndigena());
				 	sqlAlterar.setBoolean(34, obj.getEscolaPublica());
					sqlAlterar.setInt(35, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().excluirPorCodigoInscricaoProcSeletivo(obj.getCodigo());
			if ((!obj.getLiberarPagamento().booleanValue()) && (obj.getProcSeletivo().getValorInscricao().doubleValue() != 0.0)) {

				if(Uteis.isAtributoPreenchido(obj.getContaReceber())){
				
					Date dataVencimento = null; 
					obj.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
					if (obj.getProcSeletivo().getInformarQtdeDiasVencimentoAposInscricao() && obj.getProcSeletivo().getQtdeDiasVencimentoAposInscricao() > 0) {
						dataVencimento = (Uteis.obterDataAvancada(obj.getData(), obj.getProcSeletivo().getQtdeDiasVencimentoAposInscricao()));
					} else {
						dataVencimento = (obj.getItemProcessoSeletivoDataProva().getDataProva());
					}
					if(dataVencimento.after(obj.getItemProcessoSeletivoDataProva().getDataLimiteAdiarVencimentoInscricao())){
						dataVencimento = (obj.getItemProcessoSeletivoDataProva().getDataLimiteAdiarVencimentoInscricao());
					}					
					obj.getContaReceberVO().setCodigo(obj.getContaReceber());
					
					if(getFacadeFactory().getContaReceberFacade().consultarExistenciaContaReceber(obj.getContaReceber())) {
			   	      getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceberVO(), NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuario);
			   	    }								
					if (!obj.getContaReceberVO().getSituacaoEQuitada() && 
							(!obj.getContaReceberVO().getValor().equals(obj.getProcSeletivo().getValorInscricao())
							|| !obj.getContaReceberVO().getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsino().getCodigo())
							|| !Uteis.getData(obj.getContaReceberVO().getDataVencimento()).equals(Uteis.getData(dataVencimento)))) {
						removerVinculoContaReceber(obj, usuario);
						valor = true;
						getFacadeFactory().getContaReceberFacade().excluir(obj.getContaReceberVO(), false, usuario);
						montarContaReceber(obj, configuracaoFinanceiroVO, usuario, obj.getLiberarPagamento());
						if(obj.getSituacao().equals("CO")){
							obj.getSituacao().equals("PF");
							alterarSituacaoFinanceira(obj.getCodigo(), "PF", obj.getUnidadeEnsino().getCodigo(), usuario);
						}
					}else if(!obj.getContaReceberVO().getSituacaoEQuitada() && 
								!obj.getSituacaoInscricaoOrigem().equals(SituacaoInscricaoEnum.CANCELADO) && 
								!obj.getSituacaoInscricaoOrigem().equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO) && 								
								   (obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO) 
										   || obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO))){
							
						if (Uteis.isAtributoPreenchido(obj.getContaReceberVO().getCodigo())) {						
							 obj.getContaReceberVO().setMotivoCancelamento("Cancelada Devido Nova Inscrição");
							 obj.getContaReceberVO().setDataCancelamento(new Date());
							 obj.getContaReceberVO().setResponsavelCancelamentoVO(usuario);
							 obj.setSituacaoInscricaoOrigem(obj.getSituacaoInscricao());
							try {
								getFacadeFactory().getContaReceberFacade().cancelarContaReceber(obj.getContaReceberVO(), usuario);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else if(!obj.getContaReceberVO().getSituacaoEQuitada() && ( (!obj.getSituacaoInscricaoOrigem().equals(SituacaoInscricaoEnum.ATIVO) &&  obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO))
							                                                      ||( obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)    &&  obj.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())))){
						       getFacadeFactory().getContaReceberFacade().reativarContaReceberCancelada(obj.getContaReceberVO(), usuario);
						       obj.getContaReceberVO().setSituacao(SituacaoContaReceber.A_RECEBER.getValor());
						       obj.setSituacaoInscricaoOrigem(obj.getSituacaoInscricao());
						       realizarValidacaoPermissaoPagamentoInscricao(obj, configuracaoFinanceiroVO, usuario) ;
					}
				
				}else{
					montarContaReceber(obj, configuracaoFinanceiroVO, usuario, obj.getLiberarPagamento());
					if(obj.getSituacao().equals("CO")){
						obj.getSituacao().equals("PF");
						alterarSituacaoFinanceira(obj.getCodigo(), "PF", obj.getUnidadeEnsino().getCodigo(), usuario);
					}
				}
		}
		if (obj.getProcSeletivo().getValorInscricao().doubleValue() <= 0.0) {
			if (obj.getContaReceber().intValue() > 0) {				
				ContaReceberVO contaReceber = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuario);
				if (!contaReceber.getSituacaoEQuitada()) {
					getFacadeFactory().getContaReceberFacade().excluir(contaReceber, usuario);
					removerVinculoContaReceber(obj, usuario);
					obj.setContaReceber(0);
					obj.setContaReceberVO(null);
				}
			}
			if(obj.getSituacao().equals("PF")){
				obj.getSituacao().equals("CO");
				alterarSituacaoFinanceira(obj.getCodigo(), "CO", obj.getUnidadeEnsino().getCodigo(), usuario);
			}
		}
			if (valor) {
				obj.setLiberarPagamento(false);
			}
			incluirRespostaQuestionario(obj, usuario);
			persistirArquivoInscricaoProcessoSeletivo(obj, arquivoVO, usuario);
		} catch (Exception e) {
			obj.setContaReceber(codigoContaReceber);
			throw e;
		}
		// incluirRespostaQuestionario(obj);
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoContaReceber(final InscricaoVO inscricao, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Inscricao set contareceber=?, nrdocumento=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setNull(1, 0);
				sqlAlterar.setString(2, "");
				sqlAlterar.setInt(3, inscricao.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoFinanceira(final Integer inscricao, final String situacao, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Inscricao set situacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, situacao);
				sqlAlterar.setInt(2, inscricao.intValue());
				return sqlAlterar;
			}
		});
		if (situacao.equals("CO")) {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemConfirmacaoPagamentoInscricaoProcessoSeletivo(inscricao, unidadeEnsino);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoInscricao(final InscricaoVO inscricao, final SituacaoInscricaoEnum situacaoTrocar, final SituacaoInscricaoEnum situacaoAtual, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "update inscricao set situacaoinscricao = ? where candidato = ? and inscricao.codigo <> ? and inscricao.procseletivo = ? ";				
				if(situacaoAtual != null) {
					sql += " and situacaoinscricao = ? ";
				}
				if(situacaoTrocar.equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO)) {
					sql += " and not exists (select resultadoprocessoseletivo.inscricao from resultadoprocessoseletivo where resultadoprocessoseletivo.inscricao = inscricao.codigo ) ";
				}
				sql += " returning contareceber ";
				sql += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, situacaoTrocar.name());
				sqlAlterar.setInt(2, inscricao.getCandidato().getCodigo());
				sqlAlterar.setInt(3, inscricao.getCodigo());
				sqlAlterar.setInt(4, inscricao.getProcSeletivo().getCodigo());
				if(situacaoAtual != null) {
					sqlAlterar.setString(5, situacaoAtual.name());
				}
				return sqlAlterar;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					if (Uteis.isAtributoPreenchido(rs.getInt("contareceber"))) {
						ContaReceberVO contaReceber = new ContaReceberVO();
						contaReceber.setCodigo(rs.getInt("contareceber"));
						contaReceber.setMotivoCancelamento("Cancelada Devido Nova Inscrição");
						contaReceber.setDataCancelamento(new Date());
						contaReceber.setResponsavelCancelamentoVO(usuarioVO);
						try {
							getFacadeFactory().getContaReceberFacade().cancelarContaReceber(contaReceber, usuarioVO);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return null;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>InscricaoVO</code>. Sempre localiza o registro a ser excluído através da chave
	 * primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InscricaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			ContaReceberVO contaReceber = null;
			if (obj.getSituacao().equals("CO")) {
				ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarResultadoProcessoSeletivoPorCodigoInscricao(obj.getCodigo(), false, null, null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (resultadoProcessoSeletivoVO != null && resultadoProcessoSeletivoVO.getCodigo() > 0) {
					throw new ConsistirException("Esta INSCRIÇÃO não pode ser EXCLUÍDA já existe um resultado do processo seletivo cadastrado.");
				}
				if (Uteis.isAtributoPreenchido(obj.getContaReceber())) {
					contaReceber = new ContaReceberVO();
					contaReceber.setCodigo(obj.getContaReceber());
					contaReceber.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
					getFacadeFactory().getContaReceberFacade().carregarDados(contaReceber, NivelMontarDados.BASICO, null, usuario);
					if (contaReceber.getValor() > 0.0 && contaReceber.getSituacao().equals("RE")) {
						throw new ConsistirException("Esta INSCRIÇÃO não pode ser EXCLUÍDA por que já foi PAGA");
					}
				}
			} else {
				contaReceber = new ContaReceberVO();
				contaReceber.setCodigo(obj.getContaReceber());
			}
			Inscricao.excluir(getIdEntidade());
			String sql = "DELETE FROM Inscricao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
			if (Uteis.isAtributoPreenchido(contaReceber)) {
				getFacadeFactory().getContaReceberFacade().excluir(contaReceber, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>String situacao</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Inscricao WHERE lower (situacao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List consultarPorSituacaoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM Inscricao WHERE lower (situacao) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		sqlStr.append(" order by situacao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>Date data</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorDataUnidadeEnsino(prmIni, prmFim, 0, controlarAcesso, nivelMontarDados, usuario);
	}

	@Override
	public List consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM Inscricao WHERE ((data >= '");
		sqlStr.append(Uteis.getDataJDBC(prmIni)).append("') AND (data <= '");
		sqlStr.append(Uteis.getDataJDBC(prmFim)).append("')) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" order by data ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNomeCursoUnidadeEnsino(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
	}

	@Override
	public List consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT Inscricao.* FROM Inscricao, UnidadeEnsinoCurso, Curso WHERE Inscricao.cursoOpcao1 = UnidadeEnsinoCurso.codigo and UnidadeEnsinoCurso.curso = Curso.codigo and  sem_acentos(Curso.nome) ilike(sem_acentos(?)) ");		
		if (unidadeEnsino != 0) {
			sqlStr.append(" and Inscricao.unidadeEnsino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY sem_acentos(Curso.nome) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>descricao</code> da classe
	 * <code>ProcSeletivo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoProcSeletivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorDescricaoProcSeletivoUnidadeEnsino(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
	}

	@Override
	public List consultarPorDescricaoProcSeletivoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT Inscricao.* FROM Inscricao, ProcSeletivo WHERE Inscricao.procSeletivo = ProcSeletivo.codigo and sem_acentos(ProcSeletivo.descricao) ilike(sem_acentos(?)) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY sem_acentos(ProcSeletivo.descricao)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoProcSeletivoCodigoAlunoComprovanteInscricao(Integer processoSeletivo, Integer aluno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append("where procSeletivo.codigo = ").append(processoSeletivo);
		if (Uteis.isAtributoPreenchido(aluno)) {
			sqlStr.append(" and pessoa.codigo = ").append(aluno);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosComprovanteConsultaCompleta(tabelaResultado, usuario);
		
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>nome</code> da classe <code>Pessoa</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNomePessoaUnidadeEnsino(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
	}

	@Override
	public List consultarPorNomePessoaUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNomePessoa(valorConsulta, unidadeEnsino, 0, controlarAcesso, nivelMontarDados, usuario);		
	}

	@Override
	public InscricaoVO consultarUltimaInscricaoPessoa(Integer codigoPessoa, Integer codigoInscricao ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		if(Uteis.isAtributoPreenchido(codigoInscricao) && codigoInscricao > 0 ) {
			sqlStr.append(" WHERE (Inscricao.codigo = " + codigoInscricao + ") ");
		}else {
			sqlStr.append(" WHERE (Inscricao.candidato = " + codigoPessoa + ") ");
		}		
		sqlStr.append(" ORDER BY Inscricao.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			montarDadosCompleto(obj, tabelaResultado, usuario);					
			montarDadosContaReceber(obj, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), usuario);			
			obj.getProcSeletivo().setValorInscricaoApresentar(new BigDecimal(obj.getProcSeletivo().getValorInscricao()));	
			return obj;
		}
		return new InscricaoVO();
	}

	@Override
	public Boolean consultarSeExisteInscricaoParaPessoa(Integer codigoPessoa) throws Exception {
		String sqlStr = "SELECT Inscricao.codigo FROM Inscricao WHERE Inscricao.candidato = " + codigoPessoa + " limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return tabelaResultado.next();
	}

	public List consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNomePessoa(valorConsulta, unidadeEnsino, 0, controlarAcesso, nivelMontarDados, usuario);
	}
	
	public List consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Inscricao.* FROM Inscricao inner join Pessoa on Inscricao.candidato = Pessoa.codigo WHERE  sem_acentos(Pessoa.nome) ilike (sem_acentos(?)) ";
		if (unidadeEnsino != 0) {
			sqlStr += " and inscricao.unidadeEnsino = " + unidadeEnsino;
		}
		if (procSeletivo != 0) {
			sqlStr += " and inscricao.procSeletivo = " + procSeletivo;
		}
		sqlStr += " ORDER BY sem_acentos(Pessoa.nome)";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public InscricaoVO consultarPorCPFPessoaUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT Inscricao.* FROM Inscricao, Pessoa WHERE Inscricao.candidato = Pessoa.codigo and Pessoa.cpf ilike ? ";
		// if (unidadeEnsino != 0) {
		// sqlStr += " and inscricao.unidadeEnsino = " + unidadeEnsino;
		// }
		sqlStr += "  order by inscricao.codigo desc limit 1 ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		if (!tabelaResultado.next()) {
			throw new Exception("Inscrição não encontrada, verifique o CPF informado.");
		}
		InscricaoVO montarDados = montarDados(tabelaResultado, nivelMontarDados, usuario);
		montarDadosContaReceber(montarDados, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(montarDados.getUnidadeEnsino().getCodigo()), usuario);
		return montarDados;
	}

	public List consultarPorCPFPessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorCPFPessoa(valorConsulta, unidadeEnsino, 0, controlarAcesso, nivelMontarDados, usuario);
	}
	
	public List consultarPorCPFPessoa(String valorConsulta, Integer unidadeEnsino, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Inscricao.* FROM Inscricao, Pessoa WHERE Inscricao.candidato = Pessoa.codigo and Pessoa.cpf ilike(?) ";
		if (unidadeEnsino != 0) {
			sqlStr += " and inscricao.unidadeEnsino = " + unidadeEnsino;
		}
		if (procSeletivo != 0) {
			sqlStr += " and inscricao.procSeletivo = " + procSeletivo;
		}
		sqlStr += " ORDER BY sem_acentos(Pessoa.nome), Pessoa.cpf";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoInscricao(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Inscricao.* FROM Inscricao WHERE inscricao.codigo = " + valorConsulta;
		if (unidadeEnsino != 0) {
			sqlStr += " and inscricao.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY inscricao.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>nome</code> da classe
	 * <code>UnidadeEnsino</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Inscricao.* FROM Inscricao inner join UnidadeEnsino on Inscricao.unidadeEnsino = UnidadeEnsino.codigo WHERE sem_acentos(UnidadeEnsino.nome) ilike(sem_acentos(?)) ORDER BY sem_acentos(UnidadeEnsino.nome)";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Inscricao</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos
	 * com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorCodigo(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
	}
	
	public List consultarPorCodigo(Integer valorConsulta, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Inscricao WHERE codigo = " + valorConsulta.intValue() + " ";
		if (procSeletivo != 0) {
			sqlStr += "and inscricao.procSeletivo = " + procSeletivo + " ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List consultarPorCodigoUnidadeEnsino(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM Inscricao WHERE codigo = ").append(valorConsulta.intValue());
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoVO</code> resultantes da consulta.
	 */
	public static List<InscricaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<InscricaoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso
	 * da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelLiberacaoForaPrazo(InscricaoVO obj, int nivelMontarDados) throws Exception {
		if (obj.getResponsavelLiberacaoForaPrazo().getCodigo().intValue() == 0) {
			obj.setResponsavelLiberacaoForaPrazo(new UsuarioVO());
			return;
		}
		obj.setResponsavelLiberacaoForaPrazo(obj.getResponsavelLiberacaoForaPrazo());
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso
	 * da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosRespLiberacaoPgtoInsc(InscricaoVO obj, int nivelMontarDados) throws Exception {
		if (obj.getRespLiberacaoPgtoInsc().getCodigo().intValue() == 0) {
			obj.setRespLiberacaoPgtoInsc(new UsuarioVO());
			return;
		}
		obj.setRespLiberacaoPgtoInsc(obj.getRespLiberacaoPgtoInsc());
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso
	 * da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(InscricaoVO obj, int nivelMontarDados) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(obj.getResponsavel());
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso da
	 * chave primária da classe <code>CursoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCursoOpcao3(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoOpcao3().getCodigo().intValue() == 0) {
			obj.setCursoOpcao3(new UnidadeEnsinoCursoVO());
			return;
		}
		obj.setCursoOpcao3(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
	}

	public static void montarDadosOpcaoLinguaEstrangeira(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getOpcaoLinguaEstrangeira().getCodigo().intValue() == 0) {
			obj.setOpcaoLinguaEstrangeira(new DisciplinasProcSeletivoVO());
			return;
		}
		obj.setOpcaoLinguaEstrangeira(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(obj.getOpcaoLinguaEstrangeira().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso da
	 * chave primária da classe <code>CursoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCursoOpcao2(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoOpcao2().getCodigo().intValue() == 0) {
			obj.setCursoOpcao2(new UnidadeEnsinoCursoVO());
			return;
		}
		obj.setCursoOpcao2(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso da
	 * chave primária da classe <code>CursoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCursoOpcao1(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoOpcao1().getCodigo().intValue() == 0) {
			obj.setCursoOpcao1(new UnidadeEnsinoCursoVO());
			return;
		}
		obj.setCursoOpcao1(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>ProcSeletivoVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz
	 * uso da chave primária da classe <code>ProcSeletivoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosProcSeletivo(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProcSeletivo().getCodigo().intValue() == 0) {
			obj.setProcSeletivo(new ProcSeletivoVO());
			return;
		}
		// / obj.setProcSeletivo(obj.getProcSeletivo());
		obj.setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(obj.getProcSeletivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static void montarDadosArquivo(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		
			obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemInscricao(obj.getCodigo() ,nivelMontarDados, usuario));
		
		
	}
	
	

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>InscricaoVO</code>. Faz uso
	 * da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCandidato(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCandidato().getCodigo().intValue() == 0) {
			obj.setCandidato(new PessoaVO());
			return;
		}
		// // obj.setCandidato(obj.getCandidato());
		// obj.setCandidato(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCandidato().getCodigo(),
		// false, nivelMontarDados, usuario));
		obj.setCandidato(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getCandidato().getCodigo(), false, false, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>InscricaoVO</code>.
	 * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		// obj.setUnidadeEnsino(obj.getUnidadeEnsino());
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>InscricaoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public InscricaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Inscricao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public InscricaoVO consultarPorInscricaoCPF(Integer codigoPrm, String cpfPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("select inscricao.* from inscricao, pessoa");
		sql.append(" WHERE inscricao.codigo = ? AND (inscricao.candidato = pessoa.codigo AND pessoa.cpf = ?)  order by inscricao.codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm.intValue(), cpfPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		InscricaoVO montarDados = montarDados(tabelaResultado, nivelMontarDados, usuario);
		montarDadosContaReceber(montarDados, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(montarDados.getUnidadeEnsino().getCodigo()), usuario);
		return montarDados;
	}

	public InscricaoVO consultarPorInscricaoUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("select inscricao.* from inscricao, pessoa");
		sql.append(" WHERE inscricao.codigo = ? order by data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Inscrição não encontrada, verifique o nº de inscrição informado.");
		}
		InscricaoVO montarDados = montarDados(tabelaResultado, nivelMontarDados, usuario);
		montarDadosContaReceber(montarDados, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(montarDados.getUnidadeEnsino().getCodigo()), usuario);
		return montarDados;
	}

	public InscricaoVO consultarPorInscricao(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM inscricao");
		sql.append(" LEFT JOIN resultadoprocessoseletivo ON resultadoprocessoseletivo.inscricao = inscricao.codigo");
		sql.append(" WHERE inscricao = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public InscricaoVO consultarPorCandidatoEProcessoSeletivo(Integer unidadeEnsino ,Integer codigoCandidato, Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer codigoInscricao ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr =  new StringBuffer();
		sqlStr.append("SELECT * FROM Inscricao WHERE  ");		
		sqlStr.append("  (Inscricao.candidato = " + codigoCandidato + ") and (Inscricao.procseletivo = " + codigoProcessoSeletivo + ") and (inscricao.itemprocessoseletivodataprova = " + itemProcSeletivoDataProva + " )  and (Inscricao.codigo <>  "+ codigoInscricao+ " ) and (Inscricao.situacaoinscricao = 'ATIVO' ) ");
		sqlStr.append(" and Inscricao.unidadeensino = " + unidadeEnsino + " ");
		sqlStr.append(" and not exists (select resultadoprocessoseletivo.inscricao from resultadoprocessoseletivo where resultadoprocessoseletivo.inscricao = Inscricao.codigo ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());	
		InscricaoVO inscricaoVO = new InscricaoVO();
		if (tabelaResultado.next()) {
			return	montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return inscricaoVO ;
	}

	public void liberarInscricaoForaPrazo(InscricaoVO obj, UsuarioVO usuarioResponsavel) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidade("Inscricao_LiberarForaPrazo", usuarioResponsavel);
		obj.setLiberadaForaPrazo(Boolean.TRUE);
		obj.setDataLiberacaoForaPrazo(new Date());
		obj.setResponsavelLiberacaoForaPrazo(usuarioResponsavel);
	}

	public void liberarPagamentoInscricao(InscricaoVO obj, UsuarioVO usuarioResponsavel) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidade("Inscricao_LiberarPgto", usuarioResponsavel);
		obj.setSituacao("CO");
		obj.setDataLiberacaoPgtoInsc(new Date());
		obj.setRespLiberacaoPgtoInsc(usuarioResponsavel);
	}

	public List consultaCompletaPorCodigoInscricao(Integer inscricao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Inscricao.codigo = " + inscricao + ") ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + ") ");
		}
		sqlStr.append(" ORDER BY codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado, usuario);
	}

	public List consultaRapidaPorCodigoInscricao(Integer inscricao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Inscricao.codigo = " + inscricao + ") ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + ") ");
		}
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, usuario);
	}

	public List consultaRapidaPorCodigoInscricaoCpf(Integer inscricao, String cpf, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String concatena = "";
		if ((inscricao != null && inscricao != 0) || (cpf != null && !cpf.equals("")) || (unidadeEnsino != null && unidadeEnsino.intValue() != 0)) {
			sqlStr.append(" WHERE ");
			if (inscricao != null && inscricao != 0) {
				sqlStr.append("Inscricao.codigo = " + inscricao + ") ");
				concatena = " AND ";
			}
			if (cpf != null && !cpf.equals("")) {
				sqlStr.append(concatena).append("Pessoa.cpf = " + inscricao + ") ");
				concatena = "AND";
			}
			if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
				sqlStr.append(concatena).append("(UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + ") ");
			}
		}
		sqlStr.append(" ORDER BY Inscricao.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, usuario);
	}

	public InscricaoVO consultaRapidaInscricaoUnicaPorCodigoInscricaoCpf(Integer inscricao, String cpf, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String concatena = "";
		if ((inscricao != null && inscricao != 0) || (cpf != null && !cpf.equals("")) || (unidadeEnsino != null && unidadeEnsino.intValue() != 0)) {
			sqlStr.append(" WHERE ");
			if (inscricao != null && inscricao != 0) {
				sqlStr.append("(Inscricao.codigo = " + inscricao + ") ");
				concatena = " AND ";
			}
			if (cpf != null && !cpf.equals("")) {
				
				sqlStr.append(concatena).append(" (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE('");
				sqlStr.append(Uteis.retirarMascaraCPF(cpf));
				sqlStr.append("%')");
				concatena = " AND ";
			}
			if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
				sqlStr.append(concatena).append("(UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + ") ");
			}
		}
		sqlStr.append(" ORDER BY Inscricao.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		InscricaoVO inscricaoVO = new InscricaoVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(inscricaoVO, tabelaResultado, usuario);
			montarDadosContaReceber(inscricaoVO, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(inscricaoVO.getUnidadeEnsino().getCodigo()), usuario);
		}
		return inscricaoVO;
	}

	public List<InscricaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			montarDadosBasico(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<InscricaoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void carregarDados(InscricaoVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((InscricaoVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(InscricaoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((InscricaoVO) obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((InscricaoVO) obj, resultado, usuario);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Inscricao.codigo= " + codInscricao + ")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Inscricao.codigo= " + codInscricao + ")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT inscricao.*, ");
		str.append("       pessoa.codigo as \"Pessoa.codigo\", pessoa.nome as \"Pessoa.nome\", pessoa.email as \"Pessoa.email\", pessoa.datanasc as \"Pessoa.dataNasc\", pessoa.sexo as \"Pessoa.sexo\", ");
		str.append("       pessoa.tipoNecessidadesEspeciais as \"Pessoa.TipoNecessidadesEspeciais\", pessoa.cpf as \"Pessoa.cpf\", pessoa.rg as \"Pessoa.rg\", ");
		str.append("       pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", pessoa.estadoEmissaoRG as \"Pessoa.estadoEmissaoRG\", cidade.codigo as \"Cidade.codigo\", cidade.nome as \"Cidade.nome\", ");
		str.append("       procSeletivo.codigo as \"ProcSeletivo.codigo\", procSeletivo.descricao as \"ProcSeletivo.descricao\", procSeletivo.dataInicio as \"ProcSeletivo.dataInicio\", ");
		str.append("       procSeletivo.dataFim as \"ProcSeletivo.dataFim\", procSeletivo.dataProva as \"ProcSeletivo.dataProva\", procSeletivo.valorInscricao as \"ProcSeletivo.valorInscricao\", procSeletivo.horarioProva as \"ProcSeletivo.horarioProva\", ");
		str.append("       procSeletivo.documentaoObrigatoria as \"ProcSeletivo.documentaoObrigatoria\", procSeletivo.ano as \"procSeletivo.ano\", procSeletivo.semestre \"procSeletivo.semestre\", ");
		str.append("       procSeletivo.nrOpcoesCurso as \"ProcSeletivo.nrOpcoesCurso\", procSeletivo.requisitosGerais as \"ProcSeletivo.requisitosGerais\", ");
		str.append("       procSeletivo.nivelEducacional as \"ProcSeletivo.nivelEducacional\", procSeletivo.mediaMinimaAprovacao as \"ProcSeletivo.mediaMinimaAprovacao\", ");
		str.append("       procSeletivo.questionario as \"ProcSeletivo.questionario\", unidadeensino.codigo as \"Unidadeensino.codigo\", unidadeensino.nome as \"Unidadeensino.nome\", ");
		str.append("       disciplinasProcSeletivo.codigo as \"DisciplinasProcSeletivo.codigo\", disciplinasProcSeletivo.nome as \"DisciplinasProcSeletivo.nome\", ");
		str.append("       itemProcessoSeletivoDataProva.dataProva as \"itemProcessoSeletivoDataProva.dataProva\", ");
		str.append("       itemProcessoSeletivoDataProva.hora as \"itemProcessoSeletivoDataProva.hora\", itemProcessoSeletivoDataProva.dataLimiteAdiarVencimentoInscricao as \"itemProcessoSeletivoDataProva.dataLimiteAdiarVencimento\", ");
		str.append("       itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCandidato as \"itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCandidato\", ");
		str.append("       itemProcessoSeletivoDataProva.codigo as \"itemProcessoSeletivoDataProva.codigo\", inscricao.gabarito, inscricao.provaprocessoseletivo ");
		str.append(" FROM inscricao ");
		str.append("      LEFT JOIN pessoa ON (pessoa.codigo = inscricao.candidato) ");
		str.append("      LEFT JOIN cidade ON (cidade.codigo = pessoa.cidade) ");
		str.append("      LEFT JOIN procSeletivo ON (procSeletivo.codigo = inscricao.procSeletivo) ");
		str.append("      LEFT JOIN UnidadeEnsino ON (inscricao.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("      LEFT JOIN DisciplinasProcSeletivo ON (DisciplinasProcSeletivo.codigo = inscricao.opcaoLinguaEstrangeira) ");
		str.append("      LEFT JOIN ItemProcSeletivoDataProva as itemProcessoSeletivoDataProva ON (itemProcessoSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva) ");
		return str;
	}

	private void montarDadosBasico(InscricaoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados da Pessoa
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNrdocumento(dadosSQL.getString("nrdocumento"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getItemProcessoSeletivoDataProva().setCodigo(dadosSQL.getInt("itemProcessoSeletivoDataProva.codigo"));
		obj.getItemProcessoSeletivoDataProva().setDataProva(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataProva"));
		obj.getItemProcessoSeletivoDataProva().setDataLimiteAdiarVencimentoInscricao(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataLimiteAdiarVencimento"));
		obj.getItemProcessoSeletivoDataProva().setDataLimiteApresentarDadosVisaoCandidato(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCan"));
		obj.getItemProcessoSeletivoDataProva().setHora(dadosSQL.getString("itemProcessoSeletivoDataProva.hora"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.setSituacaoInscricaoOrigem(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
                obj.setClassificacao(dadosSQL.getInt("classificacao"));
                obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigoFinanceiroMatricula"));
                obj.setDescDisciplinasProcSeletivo(dadosSQL.getString("descDisciplinasProcSeletivo"));
		obj.setDataLiberacaoPgtoInsc(dadosSQL.getDate("dataLiberacaoPgtoInsc"));
		obj.setFormaAcessoProcSeletivo(dadosSQL.getString("formaAcessoProcSeletivo"));
		obj.setPortadorNecessidadeEspecial(dadosSQL.getBoolean("portadorNecessidadeEspecial"));
		obj.setDescricaoNecessidadeEspecial(dadosSQL.getString("descricaoNecessidadeEspecial"));
		obj.setLiberadaForaPrazo(dadosSQL.getBoolean("liberadaForaPrazo"));
		obj.setDataLiberacaoForaPrazo(dadosSQL.getDate("dataLiberacaoForaPrazo"));
		obj.setContaReceber(dadosSQL.getInt("contaReceber"));
		obj.setChamada(dadosSQL.getInt("chamada"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getRespLiberacaoPgtoInsc().setCodigo(dadosSQL.getInt("respLiberacaoPgtoInsc"));
		obj.getResponsavelLiberacaoForaPrazo().setCodigo(dadosSQL.getInt("responsavelLiberacaoForaPrazo"));
		obj.getCursoOpcao1().setCodigo(dadosSQL.getInt("cursoOpcao1"));
		obj.getCursoOpcao2().setCodigo(dadosSQL.getInt("cursoOpcao2"));
		obj.getCursoOpcao3().setCodigo(dadosSQL.getInt("cursoOpcao3"));
		obj.setInscricaoPresencial(dadosSQL.getBoolean("inscricaoPresencial"));
		obj.setSobreBolsasEAuxilios(dadosSQL.getBoolean("sobreBolsasEAuxilios"));
		obj.setAutodeclaracaoPretoPardoOuIndigena(dadosSQL.getBoolean("autodeclaracaoPretoPardoOuIndigena"));
		obj.setEscolaPublica(dadosSQL.getBoolean("escolaPublica"));
		obj.setNovoObj(false);
		// DADOS OPCAO LINGUA ESTRANGEIRA
		obj.getOpcaoLinguaEstrangeira().setCodigo(dadosSQL.getInt("DisciplinasProcSeletivo.codigo"));
		obj.getOpcaoLinguaEstrangeira().setNome(dadosSQL.getString("DisciplinasProcSeletivo.nome"));
		// DADOS UNIDADE ENSINO
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("Unidadeensino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("Unidadeensino.nome"));
		// DADOS CANDIDATO
		obj.getCandidato().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getCandidato().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getCandidato().setEmail(dadosSQL.getString("Pessoa.email"));
		obj.getCandidato().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getCandidato().setSexo(dadosSQL.getString("Pessoa.sexo"));
		obj.getCandidato().setTipoNecessidadesEspeciais(dadosSQL.getString("Pessoa.TipoNecessidadesEspeciais"));
		obj.getCandidato().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getCandidato().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getCandidato().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
		obj.getCandidato().setEstadoEmissaoRG(dadosSQL.getString("Pessoa.estadoEmissaoRG"));
		obj.getCandidato().getCidade().setCodigo(dadosSQL.getInt("Cidade.codigo"));
		obj.getCandidato().getCidade().setNome(dadosSQL.getString("Cidade.nome"));
		// DADOS PROCESSO SELETIVO
		obj.getProcSeletivo().setCodigo(dadosSQL.getInt("ProcSeletivo.codigo"));
		obj.getProcSeletivo().setDescricao(dadosSQL.getString("ProcSeletivo.descricao"));
		obj.getProcSeletivo().setAno(dadosSQL.getString("ProcSeletivo.ano"));
		obj.getProcSeletivo().setSemestre(dadosSQL.getString("ProcSeletivo.semestre"));
		obj.getProcSeletivo().setDataInicio(dadosSQL.getDate("ProcSeletivo.dataInicio"));
		obj.getProcSeletivo().setDataFim(dadosSQL.getDate("ProcSeletivo.dataFim"));
		obj.getProcSeletivo().setDataProva(dadosSQL.getDate("ProcSeletivo.dataProva"));
		obj.getProcSeletivo().setValorInscricao(dadosSQL.getDouble("ProcSeletivo.valorInscricao"));
		obj.getProcSeletivo().setHorarioProva(dadosSQL.getString("ProcSeletivo.horarioProva"));
		obj.getProcSeletivo().setDocumentaoObrigatoria(dadosSQL.getString("ProcSeletivo.documentaoObrigatoria"));
		obj.getProcSeletivo().setNrOpcoesCurso(dadosSQL.getString("ProcSeletivo.nrOpcoesCurso"));
		obj.getProcSeletivo().setRequisitosGerais(dadosSQL.getString("ProcSeletivo.requisitosGerais"));
		obj.getProcSeletivo().setNivelEducacional(dadosSQL.getString("ProcSeletivo.nivelEducacional"));
		obj.getProcSeletivo().setMediaMinimaAprovacao(dadosSQL.getDouble("ProcSeletivo.mediaMinimaAprovacao"));
		obj.getProcSeletivo().getQuestionario().setCodigo(dadosSQL.getInt("ProcSeletivo.questionario"));
		montarDadosCursoOpcao1(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoOpcao2(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoOpcao3(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.getGabaritoVO().setCodigo(dadosSQL.getInt("gabarito"));
		obj.getProvaProcessoSeletivoVO().setCodigo(dadosSQL.getInt("provaprocessoseletivo"));
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT inscricao.*, ");
		str.append("       pessoa.codigo as \"Pessoa.codigo\", pessoa.nome as \"Pessoa.nome\", pessoa.cep as \"Pessoa.cep\", pessoa.gravida AS \"pessoa.gravida\", pessoa.canhoto AS \"pessoa.canhoto\", pessoa.portadorNecessidadeEspecial AS \"pessoa.portadorNecessidadeEspecial\", ");
		str.append("       pessoa.endereco as \"Pessoa.endereco\", pessoa.setor as \"Pessoa.setor\", pessoa.numero as \"Pessoa.numero\", pessoa.complemento as \"Pessoa.complemento\", ");
		str.append("       pessoa.telefonecomer as \"Pessoa.telefoneComer\", pessoa.telefoneres as \"Pessoa.telefoneRes\", pessoa.telefonerecado as \"Pessoa.telefoneRecado\", ");
		str.append("       pessoa.celular as \"Pessoa.celular\", pessoa.email as \"Pessoa.email\", pessoa.datanasc as \"Pessoa.dataNasc\", pessoa.sexo as \"Pessoa.sexo\", ");
		str.append("       pessoa.tipoNecessidadesEspeciais as \"Pessoa.TipoNecessidadesEspeciais\", pessoa.cpf as \"Pessoa.cpf\", pessoa.rg as \"Pessoa.rg\", ");
		str.append("       pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", pessoa.estadoEmissaoRG as \"Pessoa.estadoEmissaoRG\", pessoa.dataemissaorg  as \"Pessoa.dataemissaorg\" , pessoa.necessidadesespeciais  as \"Pessoa.necessidadesespeciais\" , filiacao.codigo as \"Filiacao.codigo\", filiacao.aluno as \"Filiacao.aluno\", ");
		str.append("       filiacao.tipo as \"Filiacao.tipo\", pais.nome as \"Filiacao.nome\", pais.cpf as \"Filiacao.cpf\", pais.rg as \"Filiacao.rg\", cidade.codigo as \"Cidade.codigo\", cidade.nome as \"Cidade.nome\", cidade.estado as \"Cidade.estado\", ");
		str.append("       procSeletivo.codigo as \"ProcSeletivo.codigo\", procSeletivo.descricao as \"ProcSeletivo.descricao\", procSeletivo.dataInicio as \"ProcSeletivo.dataInicio\", ");
		str.append("       procSeletivo.ano as \"ProcSeletivo.ano\", procSeletivo.semestre as \"ProcSeletivo.semestre\", procSeletivo.dataFim as \"ProcSeletivo.dataFim\", procSeletivo.dataProva as \"ProcSeletivo.dataProva\", procSeletivo.dataInicioInternet as \"ProcSeletivo.dataInicioInternet\", ");
		str.append("       procSeletivo.dataFimInternet as \"ProcSeletivo.dataFimInternet\", procSeletivo.valorInscricao as \"ProcSeletivo.valorInscricao\", procSeletivo.horarioProva as \"ProcSeletivo.horarioProva\", ");
		str.append("       procSeletivo.responsavel as \"ProcSeletivo.responsavel\", procSeletivo.documentaoObrigatoria as \"ProcSeletivo.documentaoObrigatoria\", ");
		str.append("       procSeletivo.nrOpcoesCurso as \"ProcSeletivo.nrOpcoesCurso\", procSeletivo.requisitosGerais as \"ProcSeletivo.requisitosGerais\", procSeletivo.orientacaoTransferencia as \"ProcSeletivo.orientacaoTransferencia\" , procSeletivo.orientacaoUploadEnem as \"ProcSeletivo.orientacaoUploadEnem\" ,procSeletivo.orientacaoUploadPortadorDiploma as \"ProcSeletivo.orientacaoUploadPortadorDiploma\", ");
		str.append("       procSeletivo.nivelEducacional as \"ProcSeletivo.nivelEducacional\", procSeletivo.mediaMinimaAprovacao as \"ProcSeletivo.mediaMinimaAprovacao\", procSeletivo.qtdeDiasVencimentoAposInscricao as \"procSeletivo.qtdeDiasVencimentoAposInscricao\", ");
		str.append("       procSeletivo.questionario as \"ProcSeletivo.questionario\", procSeletivo.utilizarAutenticacaoEmail as \"procSeletivo.utilizarAutenticacaoEmail\", procSeletivo.utilizarAutenticacaoSMS as \"procSeletivo.utilizarAutenticacaoSMS\", procSeletivo.termoaceiteprova as \"procSeletivo.termoaceiteprova\",  procSeletivo.realizarProvaOnline as \"procSeletivo.realizarProvaOnline\",  procSeletivo.tempoRealizacaoProvaOnline as \"procSeletivo.tempoRealizacaoProvaOnline\", unidadeensino.codigo as \"Unidadeensino.codigo\", unidadeensino.nome as \"Unidadeensino.nome\", procSeletivo.tipoLayoutComprovante AS \"procSeletivo.tipoLayoutComprovante\", ");
		str.append("       disciplinasProcSeletivo.codigo as \"DisciplinasProcSeletivo.codigo\", disciplinasProcSeletivo.nome as \"DisciplinasProcSeletivo.nome\", ");
		str.append("       itemProcessoSeletivoDataProva.dataProva as \"itemProcessoSeletivoDataProva.dataProva\", ");
		str.append("       itemProcessoSeletivoDataProva.hora as \"itemProcessoSeletivoDataProva.hora\", ");
		str.append("       itemProcessoSeletivoDataProva.codigo as \"itemProcessoSeletivoDataProva.codigo\", itemProcessoSeletivoDataProva.dataLimiteAdiarVencimentoInscricao as \"itemProcessoSeletivoDataProva.dataLimiteAdiarVencimento\", ");
		str.append("       itemProcessoSeletivoDataProva.codigo as \"itemProcessoSeletivoDataProva.codigo\", itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCandidato as \"itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCandidato\", itemProcessoSeletivoDataProva.dataLiberacaoResultado as \"itemProcessoSeletivoDataProva.dataLiberacaoResultado\" , ");
		str.append("       itemProcessoSeletivoDataProva.tipoProvaGabarito as \"itemProcessoSeletivoDataProva.tipoProvaGabarito\", inscricao.gabarito, inscricao.provaprocessoseletivo, ");
		str.append("LocalAula.codigo as LocalAula_codigo, LocalAula.local as LocalAula_local, LocalAula.unidadeEnsino as LocalAula_unidadeEnsino, LocalAula.endereco as LocalAula_endereco , sala.codigo as sala_codigo, sala.sala as sala_sala, ");
		str.append("procSeletivo.tipoAvaliacaoProcessoSeletivo, inscricao.formaIngresso, procSeletivo.uploadcomprovanteenem \"procSeletivo.uploadcomprovanteenem\", procSeletivo.uploadcomprovanteportadordiploma \"procSeletivo.uploadcomprovanteportadordiploma\", procSeletivo.obrigaruploadcomprovantetransferencia \"procSeletivo.obrigaruploadcomprovantetransferencia\", ");
		str.append(" procSeletivo.tipoenem \"procSeletivo.tipoenem\", procSeletivo.tipotransferencia \"procSeletivo.tipotransferencia\", procSeletivo.tipoportadordiploma \"procSeletivo.tipoportadordiploma\" , procSeletivo.tipoProcessoSeletivo \"procSeletivo.tipoProcessoSeletivo\" ");
		str.append(" FROM inscricao ");
		str.append("      LEFT JOIN pessoa ON (pessoa.codigo = inscricao.candidato) ");
		str.append("      LEFT JOIN filiacao ON (filiacao.aluno = pessoa.codigo) ");
		str.append("      LEFT JOIN pessoa as pais ON (filiacao.pais = pais.codigo) ");
		str.append("      LEFT JOIN cidade ON (cidade.codigo = pessoa.cidade) ");
		str.append("      LEFT JOIN procSeletivo ON (procSeletivo.codigo = inscricao.procSeletivo) ");
		str.append("      LEFT JOIN UnidadeEnsino ON (inscricao.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("      LEFT JOIN DisciplinasProcSeletivo ON (DisciplinasProcSeletivo.codigo = inscricao.opcaoLinguaEstrangeira) ");
		str.append("      LEFT JOIN ItemProcSeletivoDataProva as itemProcessoSeletivoDataProva ON (itemProcessoSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva) ");
		str.append("left JOIN SalaLocalAula sala ON inscricao.sala = sala.codigo ");
		str.append("left JOIN LocalAula ON LocalAula.codigo = sala.LocalAula ");
		return str;
	}

	private void montarDadosCompleto(InscricaoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNrdocumento(dadosSQL.getString("nrdocumento"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setFormaIngresso(dadosSQL.getString("formaIngresso"));	
		obj.getItemProcessoSeletivoDataProva().setCodigo(dadosSQL.getInt("itemProcessoSeletivoDataProva.codigo"));
		obj.getItemProcessoSeletivoDataProva().setDataProva(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataProva"));
		obj.getItemProcessoSeletivoDataProva().setDataLimiteAdiarVencimentoInscricao(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataLimiteAdiarVencimento"));
		obj.getItemProcessoSeletivoDataProva().setDataLimiteApresentarDadosVisaoCandidato(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCan"));
		obj.getItemProcessoSeletivoDataProva().setHora(dadosSQL.getString("itemProcessoSeletivoDataProva.hora"));
		obj.getItemProcessoSeletivoDataProva().setTipoProvaGabarito(dadosSQL.getString("itemProcessoSeletivoDataProva.tipoProvaGabarito"));
		obj.getItemProcessoSeletivoDataProva().setDataLiberacaoResultado(dadosSQL.getTimestamp("itemProcessoSeletivoDataProva.dataLiberacaoResultado"));
		obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setClassificacao(dadosSQL.getInt("classificacao"));
        obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigoFinanceiroMatricula"));
        obj.setDescDisciplinasProcSeletivo(dadosSQL.getString("descDisciplinasProcSeletivo"));
		obj.setDataLiberacaoPgtoInsc(dadosSQL.getDate("dataLiberacaoPgtoInsc"));
		obj.setFormaAcessoProcSeletivo(dadosSQL.getString("formaAcessoProcSeletivo"));
		obj.setPortadorNecessidadeEspecial(dadosSQL.getBoolean("portadorNecessidadeEspecial"));
		obj.setDescricaoNecessidadeEspecial(dadosSQL.getString("descricaoNecessidadeEspecial"));
		obj.setLiberadaForaPrazo(dadosSQL.getBoolean("liberadaForaPrazo"));
		obj.setDataLiberacaoForaPrazo(dadosSQL.getDate("dataLiberacaoForaPrazo"));
		obj.setContaReceber(dadosSQL.getInt("contaReceber"));
		obj.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.setSituacaoInscricaoOrigem(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getRespLiberacaoPgtoInsc().setCodigo(dadosSQL.getInt("respLiberacaoPgtoInsc"));
		obj.getResponsavelLiberacaoForaPrazo().setCodigo(dadosSQL.getInt("responsavelLiberacaoForaPrazo"));
		obj.getCursoOpcao1().setCodigo(dadosSQL.getInt("cursoOpcao1"));
		obj.getCursoOpcao2().setCodigo(dadosSQL.getInt("cursoOpcao2"));
		obj.getCursoOpcao3().setCodigo(dadosSQL.getInt("cursoOpcao3"));
		obj.setInscricaoPresencial(dadosSQL.getBoolean("inscricaoPresencial"));
		obj.setChamada(dadosSQL.getInt("chamada"));
		obj.setCodigoAutenticacao(dadosSQL.getInt("codigoAutenticacao"));
		obj.setSobreBolsasEAuxilios(dadosSQL.getBoolean("sobreBolsasEAuxilios"));
		obj.setAutodeclaracaoPretoPardoOuIndigena(dadosSQL.getBoolean("autodeclaracaoPretoPardoOuIndigena"));
		obj.setEscolaPublica(dadosSQL.getBoolean("escolaPublica"));
		// DADOS OPCAO LINGUA ESTRANGEIRA
		obj.getOpcaoLinguaEstrangeira().setCodigo(dadosSQL.getInt("DisciplinasProcSeletivo.codigo"));
		obj.getOpcaoLinguaEstrangeira().setNome(dadosSQL.getString("DisciplinasProcSeletivo.nome"));
		// DADOS PROVA PROCESSO SELETIVO
 		obj.setDataHoraInicio(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataHoraInicio")));
		obj.setDataHoraTermino(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataHoraTermino")));
		obj.setTermoFoiAceito(dadosSQL.getBoolean("termoFoiAceito"));
		// DADOS UNIDADE ENSINO
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("Unidadeensino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("Unidadeensino.nome"));
		// DADOS CANDIDATO
		obj.getCandidato().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getCandidato().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getCandidato().setCEP(dadosSQL.getString("Pessoa.cep"));
		obj.getCandidato().setEndereco(dadosSQL.getString("Pessoa.endereco"));
		obj.getCandidato().setSetor(dadosSQL.getString("Pessoa.setor"));
		obj.getCandidato().setNumero(dadosSQL.getString("Pessoa.numero"));
		obj.getCandidato().setComplemento(dadosSQL.getString("Pessoa.complemento"));
		obj.getCandidato().setTelefoneComer(dadosSQL.getString("Pessoa.telefoneComer"));
		obj.getCandidato().setTelefoneRes(dadosSQL.getString("Pessoa.telefoneRes"));
		obj.getCandidato().setTelefoneRecado(dadosSQL.getString("Pessoa.telefoneRecado"));
		obj.getCandidato().setCelular(dadosSQL.getString("Pessoa.celular"));
		obj.getCandidato().setEmail(dadosSQL.getString("Pessoa.email"));
		obj.getCandidato().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getCandidato().setSexo(dadosSQL.getString("Pessoa.sexo"));
		obj.getCandidato().setTipoNecessidadesEspeciais(dadosSQL.getString("Pessoa.TipoNecessidadesEspeciais"));
		obj.getCandidato().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getCandidato().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getCandidato().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
		obj.getCandidato().setEstadoEmissaoRG(dadosSQL.getString("Pessoa.estadoEmissaoRG"));
		obj.getCandidato().getCidade().setCodigo(dadosSQL.getInt("Cidade.codigo"));
		obj.getCandidato().getCidade().setNome(dadosSQL.getString("Cidade.nome"));
		obj.getCandidato().setGravida(dadosSQL.getBoolean("pessoa.gravida"));
		obj.getCandidato().setCanhoto(dadosSQL.getBoolean("pessoa.canhoto"));
		obj.getCandidato().setPortadorNecessidadeEspecial(dadosSQL.getBoolean("pessoa.portadorNecessidadeEspecial"));
		// DADOS PROCESSO SELETIVO
		obj.getProcSeletivo().setCodigo(dadosSQL.getInt("ProcSeletivo.codigo"));
		obj.getProcSeletivo().setDescricao(dadosSQL.getString("ProcSeletivo.descricao"));
		obj.getProcSeletivo().setDataInicio(dadosSQL.getDate("ProcSeletivo.dataInicio"));
		obj.getProcSeletivo().setDataFim(dadosSQL.getDate("ProcSeletivo.dataFim"));
		obj.getProcSeletivo().setDataProva(dadosSQL.getDate("ProcSeletivo.dataProva"));
		obj.getProcSeletivo().setDataInicioInternet(dadosSQL.getDate("ProcSeletivo.dataInicioInternet"));
		obj.getProcSeletivo().setDataFimInternet(dadosSQL.getDate("ProcSeletivo.dataFimInternet"));
		obj.getProcSeletivo().setValorInscricao(dadosSQL.getDouble("ProcSeletivo.valorInscricao"));
		obj.getProcSeletivo().setHorarioProva(dadosSQL.getString("ProcSeletivo.horarioProva"));
		obj.getProcSeletivo().getResponsavel().setCodigo(dadosSQL.getInt("ProcSeletivo.responsavel"));
		obj.getProcSeletivo().setDocumentaoObrigatoria(dadosSQL.getString("ProcSeletivo.documentaoObrigatoria"));
		obj.getProcSeletivo().setNrOpcoesCurso(dadosSQL.getString("ProcSeletivo.nrOpcoesCurso"));
		obj.getProcSeletivo().setRequisitosGerais(dadosSQL.getString("ProcSeletivo.requisitosGerais"));
		obj.getProcSeletivo().setNivelEducacional(dadosSQL.getString("ProcSeletivo.nivelEducacional"));
		obj.getProcSeletivo().setMediaMinimaAprovacao(dadosSQL.getDouble("ProcSeletivo.mediaMinimaAprovacao"));
		obj.getProcSeletivo().getQuestionario().setCodigo(dadosSQL.getInt("ProcSeletivo.questionario"));
		obj.getProcSeletivo().setQtdeDiasVencimentoAposInscricao(dadosSQL.getInt("procSeletivo.qtdeDiasVencimentoAposInscricao"));
		obj.getProcSeletivo().setAno(dadosSQL.getString("procSeletivo.ano"));
		obj.getProcSeletivo().setSemestre(dadosSQL.getString("procSeletivo.semestre"));
		obj.getProcSeletivo().setUploadComprovantePortadorDiploma(dadosSQL.getBoolean("procSeletivo.uploadcomprovanteportadordiploma"));
		obj.getProcSeletivo().setUploadComprovanteEnem(dadosSQL.getBoolean("procSeletivo.uploadcomprovanteenem"));
		obj.getProcSeletivo().setObrigarUploadComprovanteTransferencia(dadosSQL.getBoolean("procSeletivo.obrigaruploadcomprovantetransferencia"));
		obj.getProcSeletivo().setTipoEnem(dadosSQL.getBoolean("procSeletivo.tipoenem"));
		obj.getProcSeletivo().setTipoTransferencia(dadosSQL.getBoolean("procSeletivo.tipotransferencia"));
		obj.getProcSeletivo().setTipoPortadorDiploma(dadosSQL.getBoolean("procSeletivo.tipoportadordiploma"));
		obj.getProcSeletivo().setTipoProcessoSeletivo(dadosSQL.getBoolean("procSeletivo.tipoProcessoSeletivo"));
		obj.getProcSeletivo().setUtilizarAutenticacaoEmail(dadosSQL.getBoolean("procSeletivo.utilizarAutenticacaoEmail"));
		obj.getProcSeletivo().setUtilizarAutenticacaoSMS(dadosSQL.getBoolean("procSeletivo.utilizarAutenticacaoSMS"));
		obj.getProcSeletivo().setTermoAceiteProva(dadosSQL.getString("procSeletivo.termoaceiteprova"));
		obj.getProcSeletivo().setRealizarProvaOnline(dadosSQL.getBoolean("procSeletivo.realizarProvaOnline"));
		obj.getProcSeletivo().setOrientacaoTransferencia(dadosSQL.getString("procSeletivo.orientacaoTransferencia"));
		obj.getProcSeletivo().setOrientacaoUploadEnem(dadosSQL.getString("procSeletivo.orientacaoUploadEnem"));
		obj.getProcSeletivo().setOrientacaoUploadPortadorDiploma(dadosSQL.getString("procSeletivo.orientacaoUploadPortadorDiploma"));	
		obj.getProcSeletivo().setTempoRealizacaoProvaOnline(dadosSQL.getInt("ProcSeletivo.tempoRealizacaoProvaOnline"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAvaliacaoProcessoSeletivo"))) {
			obj.getProcSeletivo().setTipoAvaliacaoProcessoSeletivo(TipoAvaliacaoProcessoSeletivoEnum.valueOf(dadosSQL.getString("tipoAvaliacaoProcessoSeletivo")));
		}
		obj.getSala().setCodigo(dadosSQL.getInt("sala_codigo"));
		obj.getSala().setSala(dadosSQL.getString("sala_sala"));
		obj.getSala().getLocalAula().setCodigo(dadosSQL.getInt("LocalAula_codigo"));
		obj.getSala().getLocalAula().setLocal(dadosSQL.getString("LocalAula_local"));
		obj.getSala().getLocalAula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("LocalAula_unidadeEnsino"));
		
		
		if (dadosSQL.getString("procSeletivo.tipoLayoutComprovante") == null) {
			obj.getProcSeletivo().setTipoLayoutComprovante(TipoLayoutComprovanteEnum.LAYOUT_1);
		} else {
			obj.getProcSeletivo().setTipoLayoutComprovante(TipoLayoutComprovanteEnum.valueOf(dadosSQL.getString("procSeletivo.tipoLayoutComprovante")));
		}
		obj.setNovoObj(false);
		montarDadosCursoOpcao1(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoOpcao2(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoOpcao3(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.getGabaritoVO().setCodigo(dadosSQL.getInt("gabarito"));
		obj.getProvaProcessoSeletivoVO().setCodigo(dadosSQL.getInt("provaprocessoseletivo"));
		obj.getCandidato().setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(obj.getCandidato().getCodigo(), false, usuario));
		montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);	
		montarDadosResultadoProcessoSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>InscricaoVO</code>.
	 * 
	 * @return O objeto da classe <code>InscricaoVO</code> com os dados devidamente montados.
	 */
	public static InscricaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		InscricaoVO obj = new InscricaoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getCandidato().setCodigo(new Integer(dadosSQL.getInt("candidato")));
		obj.getProcSeletivo().setCodigo(new Integer(dadosSQL.getInt("procSeletivo")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.setSituacaoInscricaoOrigem(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.setContaReceber(new Integer(dadosSQL.getInt("contaReceber")));
		obj.getOpcaoLinguaEstrangeira().setCodigo(new Integer(dadosSQL.getInt("opcaoLinguaEstrangeira")));
		obj.getItemProcessoSeletivoDataProva().setCodigo(new Integer(dadosSQL.getInt("itemProcessoSeletivoDataProva")));
        obj.setClassificacao(dadosSQL.getInt("classificacao"));
        obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigoFinanceiroMatricula"));
        obj.setDescDisciplinasProcSeletivo(dadosSQL.getString("descDisciplinasProcSeletivo"));
        obj.setCodigoAutenticacaoNavegador(dadosSQL.getString("codigoAutenticacaoNavegador"));  
        obj.setNavegadorAcesso(dadosSQL.getString("navegadorAcesso"));
        obj.setSobreBolsasEAuxilios(dadosSQL.getBoolean("sobreBolsasEAuxilios"));
		obj.setAutodeclaracaoPretoPardoOuIndigena(dadosSQL.getBoolean("autodeclaracaoPretoPardoOuIndigena"));
		obj.setEscolaPublica(dadosSQL.getBoolean("escolaPublica"));
		obj.setChamada(dadosSQL.getInt("chamada"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosCandidato(obj, nivelMontarDados, usuario);
			montarDadosProcSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (obj.getItemProcessoSeletivoDataProva().getCodigo() > 0) {
				obj.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			return obj;
		}
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.getCursoOpcao1().setCodigo(new Integer(dadosSQL.getInt("cursoOpcao1")));
		obj.getCursoOpcao2().setCodigo(new Integer(dadosSQL.getInt("cursoOpcao2")));
		obj.getCursoOpcao3().setCodigo(new Integer(dadosSQL.getInt("cursoOpcao3")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getRespLiberacaoPgtoInsc().setCodigo(new Integer(dadosSQL.getInt("respLiberacaoPgtoInsc")));
		obj.setDataLiberacaoPgtoInsc(dadosSQL.getDate("dataLiberacaoPgtoInsc"));
		obj.setFormaAcessoProcSeletivo(dadosSQL.getString("formaAcessoProcSeletivo"));
		obj.setPortadorNecessidadeEspecial(new Boolean(dadosSQL.getBoolean("portadorNecessidadeEspecial")));
		obj.setDescricaoNecessidadeEspecial(dadosSQL.getString("descricaoNecessidadeEspecial"));
		obj.setLiberadaForaPrazo(new Boolean(dadosSQL.getBoolean("liberadaForaPrazo")));
		obj.getResponsavelLiberacaoForaPrazo().setCodigo(new Integer(dadosSQL.getInt("responsavelLiberacaoForaPrazo")));
		obj.getItemProcessoSeletivoDataProva().setCodigo(new Integer(dadosSQL.getInt("itemProcessoSeletivoDataProva")));
		obj.setDataLiberacaoForaPrazo(dadosSQL.getDate("dataLiberacaoForaPrazo"));
		obj.getGabaritoVO().setCodigo(dadosSQL.getInt("gabarito"));
		obj.getProvaProcessoSeletivoVO().setCodigo(dadosSQL.getInt("provaProcessoSeletivo"));
		obj.setNrdocumento(dadosSQL.getString("nrdocumento"));
		obj.setInscricaoPresencial(dadosSQL.getBoolean("inscricaoPresencial"));
		obj.setFormaIngresso(dadosSQL.getString("formaIngresso"));
		obj.setNovoObj(Boolean.FALSE);
		obj.getSala().setCodigo(dadosSQL.getInt("sala"));
		obj.setDataAutenticacao(dadosSQL.getDate("dataAutenticacao"));
		obj.setDataHoraInicio(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataHoraInicio")));
		obj.setDataHoraTermino(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataHoraTermino")));
		obj.setTermoFoiAceito(dadosSQL.getBoolean("termoFoiAceito"));
		obj.setCodigoAutenticacao(dadosSQL.getInt("codigoAutenticacao"));
		obj.setDataHoraVencimentoCodigoAutenticacao(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataHoraVencimentoCodigoAutenticacao")));
		montarDadosCandidato(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosProcSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosArquivo(obj, nivelMontarDados, usuario);
		if (obj.getItemProcessoSeletivoDataProva().getCodigo() > 0) {
			obj.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCursoOpcao1(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoOpcao2(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoOpcao3(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosOpcaoLinguaEstrangeira(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		montarDadosRespLiberacaoPgtoInsc(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		montarDadosResponsavelLiberacaoForaPrazo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		if(Uteis.isAtributoPreenchido(obj.getSala().getCodigo())) {
			obj.setSala(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(obj.getSala().getCodigo()));
		}
		return obj;
	}

	public void montarDadosItemProcSeletivoDataProva(InscricaoVO inscricaoVO, UsuarioVO usuario) throws Exception {
		if (inscricaoVO.getItemProcessoSeletivoDataProva().getCodigo() == null || inscricaoVO.getItemProcessoSeletivoDataProva().getCodigo() == 0) {
			return;
		}
		inscricaoVO.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(inscricaoVO.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public void validarDadosUnicidadeCandidatoCurso(Integer curso1, Integer curso2, Integer curso3, Integer pessoa ,Boolean permitirAlunosMatriculadosInscreverMesmoCurso) throws Exception {
		if(!permitirAlunosMatriculadosInscreverMesmoCurso) {			
		 Boolean pessoaMatriculadoCurso = consultarExistenciaMatriculaCandidatoCurso(curso1, curso2, curso3, pessoa);
		 if (pessoaMatriculadoCurso) {
			throw new Exception("O candidato já está matriculado no curso selecionado em uma das unidades de ensino.");
		 }
		}
	}

	public Boolean consultarExistenciaMatriculaCandidatoCurso(Integer curso1, Integer curso2, Integer curso3, Integer pessoa) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matricula.matricula from matricula inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  where matricula.situacao in ('AT', 'FO') and matriculaperiodo.situacaomatriculaperiodo != 'PC' and aluno = ").append(pessoa).append(" and (curso = ").append(curso1).append(" or curso = ").append(curso2).append(" or curso = ").append(curso3).append(") limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public List<InscricaoVO> consultarCanditadoPorCodigoProcSeletivo(ProcSeletivoVO processoSeletivo, Integer dataProva, Integer codigoCurso , Integer sala, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT inscricao.codigo, inscricao.situacaoinscricao, inscricao.situacao, pessoa.nome AS candidato, ItemProcSeletivoDataProva.dataProva, ItemProcSeletivoDataProva.codigo as itemProcessoSeletivoDataProva, sala.sala, local.local, inscricao.cursoOpcao1, inscricao.cursoOpcao2, inscricao.cursoOpcao3, ");
		sqlStr.append(" ResultadoProcessoSeletivo.*, usuario.nome as \"usuario.nome\", ");
		sqlStr.append(" (select max(ResultadoProcessoSeletivo.codigo) from ResultadoProcessoSeletivo where ResultadoProcessoSeletivo.inscricao = inscricao.codigo) as ResultadoProcessoSeletivo ");
		sqlStr.append(" FROM inscricao");
		sqlStr.append(" INNER JOIN pessoa ON inscricao.candidato = pessoa.codigo");
		sqlStr.append(" left JOIN SalaLocalAula sala ON inscricao.sala = sala.codigo");
		sqlStr.append(" inner join ProcSeletivo on ProcSeletivo.codigo = inscricao.procSeletivo ");
		sqlStr.append(" left JOIN LocalAula local ON sala.LocalAula = local.codigo");
		sqlStr.append(" left JOIN ResultadoProcessoSeletivo on ResultadoProcessoSeletivo.inscricao = inscricao.codigo ");
		sqlStr.append(" left JOIN usuario ON usuario.codigo = ResultadoProcessoSeletivo.responsavel");
		sqlStr.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ");
		if (dataProva != null && dataProva > 0) {
			sqlStr.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProva);
		}
		if (codigoCurso != null && codigoCurso > 0) {
			if(Integer.parseInt(processoSeletivo.getNrOpcoesCurso()) >= 1) {			
					sqlStr.append(" and ( inscricao.cursoopcao1 = ").append(codigoCurso);			
			}
			if(Integer.parseInt(processoSeletivo.getNrOpcoesCurso()) >=2) {			
					sqlStr.append(" or inscricao.cursoopcao2 = ").append(codigoCurso);			
			}
			if(Integer.parseInt(processoSeletivo.getNrOpcoesCurso()) > 2) {			
					sqlStr.append(" or inscricao.cursoopcao3 = ").append(codigoCurso);			
			}
			sqlStr.append(" )");
		}
		sqlStr.append(" WHERE inscricao.procSeletivo = ").append(processoSeletivo.getCodigo());
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {
			sqlStr.append(" AND inscricao.sala = ").append(sala);
		} else if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {
			sqlStr.append(" AND inscricao.sala is null");
		}
		sqlStr.append(" AND inscricao.situacao = 'CO'");
		sqlStr.append(" ORDER BY sem_acentos(pessoa.nome)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(tabelaResultado.getString("situacaoInscricao")));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.getCandidato().setNome(tabelaResultado.getString("candidato"));
			obj.getItemProcessoSeletivoDataProva().setDataProva(tabelaResultado.getDate("dataProva"));
			obj.getItemProcessoSeletivoDataProva().setCodigo(new Integer(tabelaResultado.getInt("itemProcessoSeletivoDataProva")));

			obj.setProcSeletivo(processoSeletivo);

			if (obj.getItemProcessoSeletivoDataProva().getCodigo() > 0) {
				obj.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			}

			obj.getSala().setSala(tabelaResultado.getString("sala"));
			obj.getSala().getLocalAula().setLocal(tabelaResultado.getString("local"));
			obj.setResultadoProcessoSeletivo(tabelaResultado.getInt("ResultadoProcessoSeletivo"));

			obj.getCursoOpcao1().setCodigo(new Integer(tabelaResultado.getInt("cursoOpcao1")));
			obj.getCursoOpcao2().setCodigo(new Integer(tabelaResultado.getInt("cursoOpcao2")));
			obj.getCursoOpcao3().setCodigo(new Integer(tabelaResultado.getInt("cursoOpcao3")));

			montarDadosCursoOpcao1(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCursoOpcao2(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCursoOpcao3(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);

			obj.getResultadoProcessoSeletivoVO().setCodigo(tabelaResultado.getInt("ResultadoProcessoSeletivo"));
			obj.getResultadoProcessoSeletivoVO().setInscricao(obj);
			obj.getResultadoProcessoSeletivoVO().setNotaRedacao(tabelaResultado.getDouble("notaRedacao"));
			obj.getResultadoProcessoSeletivoVO().setSomatorioAcertos(tabelaResultado.getDouble("somatorioAcertos"));
			obj.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao(tabelaResultado.getString("resultadoPrimeiraOpcao"));
			obj.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao(tabelaResultado.getString("resultadoSegundaOpcao"));
			obj.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao(tabelaResultado.getString("resultadoTerceiraOpcao"));
			obj.getResultadoProcessoSeletivoVO().setRespostaProvaProcessoSeletivo(tabelaResultado.getString("respostaProvaProcessoSeletivo"));
			obj.getResultadoProcessoSeletivoVO().setMediaNotasProcSeletivo(new Double(tabelaResultado.getDouble("mediaNotasProcSeletivo")));
			obj.getResultadoProcessoSeletivoVO().setMediaPonderadaNotasProcSeletivo(new Double(tabelaResultado.getDouble("mediaPonderadaNotasProcSeletivo")));
			obj.getResultadoProcessoSeletivoVO().setRedacao(tabelaResultado.getString("redacao"));

			obj.getResultadoProcessoSeletivoVO().getResponsavel().setCodigo(new Integer(tabelaResultado.getInt("responsavel")));
			obj.getResultadoProcessoSeletivoVO().getResponsavel().setNome(tabelaResultado.getString("usuario.nome"));
			obj.getResultadoProcessoSeletivoVO().setDataRegistro(tabelaResultado.getDate("dataRegistro"));

			if (obj.getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
				obj.getResultadoProcessoSeletivoVO().setNovoObj(true);
			} else {
				obj.getResultadoProcessoSeletivoVO().setNovoObj(false);
			}

			obj.getResultadoProcessoSeletivoVO().setResultadoDisciplinaProcSeletivoVOs(getFacadeFactory().getResultadoDisciplinaProcSeletivoFacade().consultarResultadoDisciplinaProcSeletivos(obj.getResultadoProcessoSeletivoVO().getCodigo(), usuario));
			obj.getResultadoProcessoSeletivoVO().setResultadoProcessoSeletivoGabaritoRespostaVOs(getFacadeFactory().getResultadoProcessoSeletivoGabaritoRespostaFacade().consultaRapidaPorResultadoProcessoSeletivo(obj.getResultadoProcessoSeletivoVO().getCodigo(), usuario));
			obj.getResultadoProcessoSeletivoVO().setResultadoProcessoSeletivoProvaRespostaVOs(getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().consultarPorResultadoProcessoSeletivo(obj.getResultadoProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));

			vetResultado.add(obj);
		}
		return vetResultado;

	}

	public List<InscricaoVO> consultarCanditadoPorCodigo(PessoaVO candidato, Integer dataProva, Integer sala, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT inscricao.codigo, inscricao.procseletivo as codprocseletivo, pessoa.nome AS candidato, ItemProcSeletivoDataProva.dataProva, ItemProcSeletivoDataProva.codigo as itemProcessoSeletivoDataProva, sala.sala, local.local, inscricao.cursoOpcao1, inscricao.cursoOpcao2, inscricao.cursoOpcao3, ");
		sqlStr.append(" ResultadoProcessoSeletivo.*, usuario.nome as \"usuario.nome\", matricula.matricula, ");
		sqlStr.append(" (select max(ResultadoProcessoSeletivo.codigo) from ResultadoProcessoSeletivo where ResultadoProcessoSeletivo.inscricao = inscricao.codigo) as ResultadoProcessoSeletivo ");
		sqlStr.append(" FROM inscricao");
		sqlStr.append(" INNER JOIN pessoa ON inscricao.candidato = pessoa.codigo");
		sqlStr.append(" left JOIN SalaLocalAula sala ON inscricao.sala = sala.codigo");
		sqlStr.append(" inner join ProcSeletivo on ProcSeletivo.codigo = inscricao.procSeletivo ");
		sqlStr.append(" left JOIN LocalAula local ON sala.LocalAula = local.codigo");
		sqlStr.append(" left JOIN ResultadoProcessoSeletivo on ResultadoProcessoSeletivo.inscricao = inscricao.codigo ");
		sqlStr.append(" left JOIN usuario ON usuario.codigo = ResultadoProcessoSeletivo.responsavel");
		sqlStr.append(" left JOIN matricula ON matricula.inscricao = inscricao.codigo");
		sqlStr.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ");
		if (dataProva != null && dataProva > 0) {
			sqlStr.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProva);
		}
		sqlStr.append(" WHERE inscricao.candidato = ").append(candidato.getCodigo());
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {
			sqlStr.append(" AND inscricao.sala = ").append(sala);
		} else if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {
			sqlStr.append(" AND inscricao.sala is null");
		}

		//sqlStr.append(" and matricula.matricula is null ");
		sqlStr.append(" ORDER BY sem_acentos(pessoa.nome), ProcSeletivo.ano||ProcSeletivo.semestre desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.getCandidato().setNome(tabelaResultado.getString("candidato"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getItemProcessoSeletivoDataProva().setDataProva(tabelaResultado.getDate("dataProva"));
			obj.getItemProcessoSeletivoDataProva().setCodigo(new Integer(tabelaResultado.getInt("itemProcessoSeletivoDataProva")));

			obj.getProcSeletivo().setCodigo(new Integer(tabelaResultado.getInt("codprocseletivo")));
			montarDadosProcSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);

			if (obj.getItemProcessoSeletivoDataProva().getCodigo() > 0) {
				obj.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}

			obj.getSala().setSala(tabelaResultado.getString("sala"));
			obj.getSala().getLocalAula().setLocal(tabelaResultado.getString("local"));
			obj.setResultadoProcessoSeletivo(tabelaResultado.getInt("ResultadoProcessoSeletivo"));

			obj.getCursoOpcao1().setCodigo(new Integer(tabelaResultado.getInt("cursoOpcao1")));
			obj.getCursoOpcao2().setCodigo(new Integer(tabelaResultado.getInt("cursoOpcao2")));
			obj.getCursoOpcao3().setCodigo(new Integer(tabelaResultado.getInt("cursoOpcao3")));

			montarDadosCursoOpcao1(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCursoOpcao2(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCursoOpcao3(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);

			obj.getResultadoProcessoSeletivoVO().setCodigo(tabelaResultado.getInt("ResultadoProcessoSeletivo"));
			obj.getResultadoProcessoSeletivoVO().setInscricao(obj);
			obj.getResultadoProcessoSeletivoVO().setNotaRedacao(tabelaResultado.getDouble("notaRedacao"));
			obj.getResultadoProcessoSeletivoVO().setSomatorioAcertos(tabelaResultado.getDouble("somatorioAcertos"));
			obj.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao(tabelaResultado.getString("resultadoPrimeiraOpcao"));
			obj.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao(tabelaResultado.getString("resultadoSegundaOpcao"));
			obj.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao(tabelaResultado.getString("resultadoTerceiraOpcao"));
			obj.getResultadoProcessoSeletivoVO().setRespostaProvaProcessoSeletivo(tabelaResultado.getString("respostaProvaProcessoSeletivo"));
			obj.getResultadoProcessoSeletivoVO().setMediaNotasProcSeletivo(new Double(tabelaResultado.getDouble("mediaNotasProcSeletivo")));
			obj.getResultadoProcessoSeletivoVO().setMediaPonderadaNotasProcSeletivo(new Double(tabelaResultado.getDouble("mediaPonderadaNotasProcSeletivo")));
			obj.getResultadoProcessoSeletivoVO().setRedacao(tabelaResultado.getString("redacao"));
			
			obj.getResultadoProcessoSeletivoVO().getResponsavel().setCodigo(new Integer(tabelaResultado.getInt("responsavel")));
			obj.getResultadoProcessoSeletivoVO().getResponsavel().setNome(tabelaResultado.getString("usuario.nome"));
			obj.getResultadoProcessoSeletivoVO().setDataRegistro(tabelaResultado.getDate("dataRegistro"));

			if (obj.getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
				obj.getResultadoProcessoSeletivoVO().setNovoObj(true);
			} else {
				obj.getResultadoProcessoSeletivoVO().setNovoObj(false);
			}

			obj.getResultadoProcessoSeletivoVO().setResultadoDisciplinaProcSeletivoVOs(getFacadeFactory().getResultadoDisciplinaProcSeletivoFacade().consultarResultadoDisciplinaProcSeletivos(obj.getResultadoProcessoSeletivoVO().getCodigo(), usuario));
			obj.getResultadoProcessoSeletivoVO().setResultadoProcessoSeletivoGabaritoRespostaVOs(getFacadeFactory().getResultadoProcessoSeletivoGabaritoRespostaFacade().consultaRapidaPorResultadoProcessoSeletivo(obj.getResultadoProcessoSeletivoVO().getCodigo(), usuario));
			obj.getResultadoProcessoSeletivoVO().setResultadoProcessoSeletivoProvaRespostaVOs(getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().consultarPorResultadoProcessoSeletivo(obj.getResultadoProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));

			vetResultado.add(obj);
		}
		return vetResultado;

	}

	public List<InscricaoVO> montarDadosCanditadoPorCodigoProcSeletivo(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.getCandidato().setNome(tabelaResultado.getString("candidato"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public InscricaoVO consultarDadosParaResultadoProcessoSeletivo(Integer inscricao, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala) {
		InscricaoVO inscricaoVO = new InscricaoVO();
		StringBuilder sb = new StringBuilder("SELECT inscricao.codigo, candidato.codigo as candidato_codigo, candidato.nome as candidato_nome, candidato.email as candidato_email, ");
		sb.append(" procseletivo.codigo as procseletivo_codigo, procseletivo.descricao as procseletivo_descricao, procseletivo.mediaMinimaAprovacao as procseletivo_mediaMinimaAprovacao, procseletivo.regimeAprovacao as procseletivo_regimeAprovacao, procseletivo.quantidadeAcertosMinimosAprovacao as procseletivo_quantidadeAcertosMinimosAprovacao, procseletivo.notaMinimaRedacao as procseletivo_notaMinimaRedacao, procseletivo.valorPorAcerto as procseletivo_valorPorAcerto, ");
		sb.append(" cursoopcao1.codigo as cursoopcao1_codigo, cursoopcao1.nome as cursoopcao1_nome, ");
		sb.append(" turnocursoopcao1.codigo as turnocursoopcao1_codigo, turnocursoopcao1.nome as turnocursoopcao1_nome, ");
		sb.append(" cursoopcao2.codigo as cursoopcao2_codigo, cursoopcao2.nome as cursoopcao2_nome, ");
		sb.append(" turnocursoopcao2.codigo as turnocursoopcao2_codigo, turnocursoopcao2.nome as turnocursoopcao2_nome, ");
		sb.append(" cursoopcao3.codigo as cursoopcao3_codigo, cursoopcao3.nome as cursoopcao3_nome, ");
		sb.append(" turnocursoopcao3.codigo as turnocursoopcao3_codigo, turnocursoopcao3.nome as turnocursoopcao3_nome, ");
		sb.append(" opcaolinguaestrangeira.codigo as opcaolinguaestrangeira_codigo, opcaolinguaestrangeira.nome as opcaolinguaestrangeira_nome,  ");
		sb.append(" unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, itemprocseletivodataprova.tipoProvaGabarito ");
		sb.append(" from inscricao ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = inscricao.unidadeensino ");
		sb.append(" left join salalocalaula on salalocalaula.codigo = inscricao.sala ");
		sb.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		sb.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sb.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao2 on unidadeensinocursoopcao2.codigo = inscricao.cursoopcao2 ");
		sb.append(" left join curso as cursoopcao2 on cursoopcao2.codigo = unidadeensinocursoopcao2.curso ");
		sb.append(" left join turno as turnocursoopcao2 on turnocursoopcao2.codigo = unidadeensinocursoopcao2.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao3 on unidadeensinocursoopcao3.codigo = inscricao.cursoopcao3 ");
		sb.append(" left join curso as cursoopcao3 on cursoopcao3.codigo = unidadeensinocursoopcao3.curso ");
		sb.append(" left join turno as turnocursoopcao3 on turnocursoopcao3.codigo = unidadeensinocursoopcao3.turno ");
		sb.append(" left join disciplinasprocseletivo as opcaolinguaestrangeira on opcaolinguaestrangeira.codigo = inscricao.opcaolinguaestrangeira ");
		sb.append(" where inscricao.codigo = ").append(inscricao);
		if (procSeletivo != null && !procSeletivo.equals(0)) {
			sb.append(" AND procseletivo.codigo = ").append(procSeletivo);
		}
		if (itemProcSeletivoDataProva != null && !itemProcSeletivoDataProva.equals(0)) {
			sb.append(" AND itemprocseletivodataprova.codigo = ").append(itemProcSeletivoDataProva);
		}
		if (sala != null && !sala.equals(0)) {
			sb.append(" AND salalocalaula.codigo = ").append(sala);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.getCandidato().setCodigo(rs.getInt("candidato_codigo"));
			inscricaoVO.getCandidato().setNome(rs.getString("candidato_nome"));
			inscricaoVO.getCandidato().setEmail(rs.getString("candidato_email"));
			inscricaoVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino_codigo"));
			inscricaoVO.getUnidadeEnsino().setNome(rs.getString("unidadeensino_nome"));
			inscricaoVO.getProcSeletivo().setCodigo(rs.getInt("procseletivo_codigo"));
			inscricaoVO.getProcSeletivo().setDescricao(rs.getString("procseletivo_descricao"));
			inscricaoVO.getProcSeletivo().setMediaMinimaAprovacao(rs.getDouble("procseletivo_mediaMinimaAprovacao"));
			inscricaoVO.getProcSeletivo().setQuantidadeAcertosMinimosAprovacao(rs.getInt("procseletivo_quantidadeAcertosMinimosAprovacao"));
			inscricaoVO.getProcSeletivo().setNotaMinimaRedacao(rs.getDouble("procseletivo_notaMinimaRedacao"));
			inscricaoVO.getProcSeletivo().setRegimeAprovacao(rs.getString("procseletivo_regimeAprovacao"));
			inscricaoVO.getProcSeletivo().setValorPorAcerto(rs.getDouble("procseletivo_valorPorAcerto"));
			inscricaoVO.getOpcaoLinguaEstrangeira().setCodigo(rs.getInt("opcaolinguaestrangeira_codigo"));
			inscricaoVO.getOpcaoLinguaEstrangeira().setNome(rs.getString("opcaolinguaestrangeira_nome"));
			inscricaoVO.getCursoOpcao1().getCurso().setCodigo(rs.getInt("cursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getCurso().setNome(rs.getString("cursoopcao1_nome"));
			inscricaoVO.getCursoOpcao1().getTurno().setCodigo(rs.getInt("turnocursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getTurno().setNome(rs.getString("turnocursoopcao1_nome"));
			inscricaoVO.getCursoOpcao2().getCurso().setCodigo(rs.getInt("cursoopcao2_codigo"));
			inscricaoVO.getCursoOpcao2().getTurno().setNome(rs.getString("cursoopcao2_nome"));
			inscricaoVO.getCursoOpcao2().getTurno().setCodigo(rs.getInt("turnocursoopcao2_codigo"));
			inscricaoVO.getCursoOpcao2().getCurso().setNome(rs.getString("turnocursoopcao2_nome"));
			inscricaoVO.getCursoOpcao3().getCurso().setCodigo(rs.getInt("cursoopcao3_codigo"));
			inscricaoVO.getCursoOpcao3().getCurso().setNome(rs.getString("cursoopcao3_nome"));
			inscricaoVO.getCursoOpcao3().getTurno().setCodigo(rs.getInt("turnocursoopcao3_codigo"));
			inscricaoVO.getCursoOpcao3().getTurno().setNome(rs.getString("turnocursoopcao3_nome"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setTipoProvaGabarito(rs.getString("tipoProvaGabarito"));
			inscricaoVO.setNovoObj(false);
		}

		return inscricaoVO;
	}

	@Override
	public InscricaoVO consultarDadosEnvioNotificacaoEmail(Integer inscricao) {
		InscricaoVO inscricaoVO = new InscricaoVO();
		StringBuilder sb = new StringBuilder("SELECT inscricao.codigo, candidato.codigo as candidato_codigo, candidato.nome as candidato_nome, candidato.email as candidato_email, ");
		sb.append(" procseletivo.codigo as procseletivo_codigo, procseletivo.descricao as procseletivo_descricao, ");
		sb.append(" cursoopcao1.codigo as cursoopcao1_codigo, cursoopcao1.nome as cursoopcao1_nome, ");
		sb.append(" turnocursoopcao1.codigo as turnocursoopcao1_codigo, turnocursoopcao1.nome as turnocursoopcao1_nome, ");
		sb.append(" cursoopcao2.codigo as cursoopcao2_codigo, cursoopcao2.nome as cursoopcao2_nome, ");
		sb.append(" turnocursoopcao2.codigo as turnocursoopcao2_codigo, turnocursoopcao2.nome as turnocursoopcao2_nome, ");
		sb.append(" cursoopcao3.codigo as cursoopcao3_codigo, cursoopcao3.nome as cursoopcao3_nome, ");
		sb.append(" turnocursoopcao3.codigo as turnocursoopcao3_codigo, turnocursoopcao3.nome as turnocursoopcao3_nome, ");
		sb.append(" opcaolinguaestrangeira.codigo as opcaolinguaestrangeira_codigo, opcaolinguaestrangeira.nome as opcaolinguaestrangeira_nome ");
		sb.append(" from inscricao ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		sb.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sb.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao2 on unidadeensinocursoopcao2.codigo = inscricao.cursoopcao2 ");
		sb.append(" left join curso as cursoopcao2 on cursoopcao2.codigo = unidadeensinocursoopcao2.curso ");
		sb.append(" left join turno as turnocursoopcao2 on turnocursoopcao2.codigo = unidadeensinocursoopcao2.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao3 on unidadeensinocursoopcao3.codigo = inscricao.cursoopcao3 ");
		sb.append(" left join curso as cursoopcao3 on cursoopcao3.codigo = unidadeensinocursoopcao3.curso ");
		sb.append(" left join turno as turnocursoopcao3 on turnocursoopcao3.codigo = unidadeensinocursoopcao3.turno ");

		sb.append(" left join disciplinasprocseletivo as opcaolinguaestrangeira on opcaolinguaestrangeira.codigo = inscricao.opcaolinguaestrangeira ");
		sb.append(" where inscricao.codigo = ").append(inscricao);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.getCandidato().setCodigo(rs.getInt("candidato_codigo"));
			inscricaoVO.getCandidato().setNome(rs.getString("candidato_nome"));
			inscricaoVO.getCandidato().setEmail(rs.getString("candidato_email"));
			inscricaoVO.getProcSeletivo().setCodigo(rs.getInt("procseletivo_codigo"));
			inscricaoVO.getProcSeletivo().setDescricao(rs.getString("procseletivo_descricao"));
			inscricaoVO.getOpcaoLinguaEstrangeira().setCodigo(rs.getInt("opcaolinguaestrangeira_codigo"));
			inscricaoVO.getOpcaoLinguaEstrangeira().setNome(rs.getString("opcaolinguaestrangeira_nome"));

			inscricaoVO.getCursoOpcao1().getCurso().setCodigo(rs.getInt("cursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getCurso().setNome(rs.getString("cursoopcao1_nome"));

			inscricaoVO.getCursoOpcao1().getTurno().setCodigo(rs.getInt("turnocursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getTurno().setNome(rs.getString("turnocursoopcao1_nome"));

			inscricaoVO.getCursoOpcao2().getCurso().setCodigo(rs.getInt("cursoopcao2_codigo"));
			inscricaoVO.getCursoOpcao2().getTurno().setNome(rs.getString("cursoopcao2_nome"));

			inscricaoVO.getCursoOpcao2().getTurno().setCodigo(rs.getInt("turnocursoopcao2_codigo"));
			inscricaoVO.getCursoOpcao2().getCurso().setNome(rs.getString("turnocursoopcao2_nome"));

			inscricaoVO.getCursoOpcao3().getCurso().setCodigo(rs.getInt("cursoopcao3_codigo"));
			inscricaoVO.getCursoOpcao3().getCurso().setNome(rs.getString("cursoopcao3_nome"));

			inscricaoVO.getCursoOpcao3().getTurno().setCodigo(rs.getInt("turnocursoopcao3_codigo"));
			inscricaoVO.getCursoOpcao3().getTurno().setNome(rs.getString("turnocursoopcao3_nome"));
			inscricaoVO.setNovoObj(false);
		}

		return inscricaoVO;
	}

	@Override
	public List<InscricaoVO> consultaRapidaPorSalaProcSeletivoData(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer sala) throws Exception {

		StringBuilder sql = new StringBuilder("Select inscricao.codigo, pessoa.nome AS candidato, sala.codigo as \"sala.codigo\", sala.sala as \"sala.sala\", local.local, cursoopcao1.nome as  \"curso.nome\", turnocursoopcao1.nome as \"turno.nome\" from inscricao ");
		sql.append(" inner join Pessoa on Pessoa.codigo = inscricao.candidato ");
		sql.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		sql.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sql.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sql.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sql.append(" left join LocalAula as local on local.codigo = sala.localAula ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' ");
		if (sala != null && sala > 0) {
			sql.append(" and sala.codigo = ").append(sala);
		} else if (sala != null && sala == 0) {
			sql.append(" and inscricao.sala is null ");
		}
		sql.append(" order by sem_acentos(pessoa.nome) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<InscricaoVO> inscricaoVOs = new ArrayList<InscricaoVO>(0);
		InscricaoVO inscricaoVO = null;
		while (rs.next()) {
			inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.getCandidato().setNome(rs.getString("candidato"));
			inscricaoVO.getSala().setCodigo(rs.getInt("sala.codigo"));
			inscricaoVO.getSala().setSala(rs.getString("sala.sala"));
			inscricaoVO.getSala().getLocalAula().setLocal(rs.getString("local"));
			inscricaoVO.getCursoOpcao1().getCurso().setNome(rs.getString("curso.nome"));
			inscricaoVO.getCursoOpcao1().getTurno().setNome(rs.getString("turno.nome"));
			inscricaoVOs.add(inscricaoVO);
		}
		return inscricaoVOs;
	}

	@Override
	public List<InscricaoVO> consultaRapidaParaImpressaoEtiqueta(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer sala, Integer inscricao) throws Exception {

		StringBuilder sql = new StringBuilder("Select inscricao.codigo, pessoa.nome AS \"pessoa.nome\", pessoa.rg as \"pessoa.rg\", pessoa.cpf as \"pessoa.cpf\", pessoa.canhoto as \"pessoa.canhoto\", sala.codigo as \"sala.codigo\", sala.sala as \"sala.sala\", ");
		sql.append(" cursoopcao1.nome as  \"curso.nome\", turnocursoopcao1.nome as \"turno.nome\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ItemProcSeletivoDataProva.dataProva, ItemProcSeletivoDataProva.hora ");
		sql.append(" from inscricao ");
		sql.append(" inner join Pessoa on Pessoa.codigo = inscricao.candidato ");
		sql.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = inscricao.UnidadeEnsino ");
		sql.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		sql.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sql.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sql.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' ");
		if (sala != null && sala > 0) {
			sql.append(" and sala.codigo = ").append(sala);
		}
		if (inscricao != null && inscricao > 0) {
			sql.append(" and inscricao.codigo = ").append(inscricao);
		}
		sql.append(" order by sala.sala, sem_acentos(pessoa.nome) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<InscricaoVO> inscricaoVOs = new ArrayList<InscricaoVO>(0);
		InscricaoVO inscricaoVO = null;
		while (rs.next()) {
			inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.getCandidato().setNome(rs.getString("pessoa.nome"));
			inscricaoVO.getCandidato().setRG(rs.getString("pessoa.rg"));
			inscricaoVO.getCandidato().setCPF(rs.getString("pessoa.cpf"));
			inscricaoVO.getCandidato().setCanhoto(rs.getBoolean("pessoa.canhoto"));
			inscricaoVO.getSala().setCodigo(rs.getInt("sala.codigo"));
			inscricaoVO.getSala().setSala(rs.getString("sala.sala"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setHora(rs.getString("hora"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setDataProva(rs.getDate("dataProva"));
			inscricaoVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
			inscricaoVO.getCursoOpcao1().getCurso().setNome(rs.getString("curso.nome"));
			inscricaoVO.getCursoOpcao1().getTurno().setNome(rs.getString("turno.nome"));
			inscricaoVOs.add(inscricaoVO);
		}
		return inscricaoVOs;
	}

	@Override
	public Integer consultarNumeroInscritosConfirmadosProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva) throws Exception {
		StringBuilder sql = new StringBuilder("Select count(distinct inscricao.codigo) as qtde from inscricao ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public Integer consultarNumeroInscritosConfirmadosSemSalaProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva) throws Exception {
		StringBuilder sql = new StringBuilder("Select count(distinct inscricao.codigo) as qtde from inscricao ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' and (inscricao.sala = 0 or inscricao.sala is null) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public Integer consultarNumeroInscritosConfirmadosNecessidadesEspeciaisProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva) throws Exception {
		StringBuilder sql = new StringBuilder("Select count(distinct inscricao.codigo) as qtde from inscricao ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where inscricao.portadorNecessidadeEspecial and  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public Integer consultarNumeroInscritosSalaEspecifica(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer salaLocalAula) throws Exception {
		StringBuilder sql = new StringBuilder("Select count(distinct inscricao.codigo) as qtde from inscricao ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva");
		sql.append(" where inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' and inscricao.sala = ").append(salaLocalAula);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public List<ProcessoSeletivoInscritoSalaRelVO> consultarQtdeInscritosPorSala(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Boolean trazerInscricaoSemSala) throws Exception {
		StringBuilder sql = new StringBuilder("Select count(distinct inscricao.codigo) as numeroInscrito, sala.codigo as \"sala.codigo\", sala.sala as \"sala.sala\", local.local, sum(case when portadorNecessidadeEspecial  then 1 else 0 end) as numeroInscritoNecessidadesEspeciais from inscricao ");
		sql.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sql.append(" left join LocalAula as local on sala.localAula = local.codigo ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		if (!trazerInscricaoSemSala) {
			sql.append(" and inscricao.sala > 0 ");
		}
		sql.append(" and inscricao.situacao = 'CO' group by sala.codigo, sala.sala, local.local order by local.local, sala.sala ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ProcessoSeletivoInscritoSalaRelVO> processoSeletivoInscritoSalaRelVOs = new ArrayList<ProcessoSeletivoInscritoSalaRelVO>(0);
		ProcessoSeletivoInscritoSalaRelVO obj = null;
		while (rs.next()) {
			obj = new ProcessoSeletivoInscritoSalaRelVO();
			obj.setNumeroInscrito(rs.getInt("numeroInscrito"));
			obj.getSala().setCodigo(rs.getInt("sala.codigo"));
			obj.getSala().setSala(rs.getString("sala.sala"));
			obj.getSala().getLocalAula().setLocal(rs.getString("local"));
			obj.setNumeroInscritoNecessidadesEspeciais(rs.getInt("numeroInscritoNecessidadesEspeciais"));
			processoSeletivoInscritoSalaRelVOs.add(obj);
		}
		return processoSeletivoInscritoSalaRelVOs;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void realizarDistribuicaoSalaProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Boolean agruparPorNecessidadesEspeciais, String formaDistribuicao, Boolean distribuirCandidatosSemSala, List<SalaLocalAulaVO> salaLocalAulaVOs, UsuarioVO usuarioVO) throws Exception {

		if (salaLocalAulaVOs == null || salaLocalAulaVOs.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_processoSeletivo_numeroSalas"));
		}
		int x = 0;
		for (SalaLocalAulaVO salaLocalAulaVO : salaLocalAulaVOs) {
			if (salaLocalAulaVO.getUtilizarDistribuicao()) {
				x = 1;
				break;
			}
		}
		if (x == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_processoSeletivo_numeroSalas"));
		}

		StringBuilder sql = new StringBuilder("Select inscricao.codigo as codigoInscricao from inscricao ");
		sql.append(" inner join Pessoa on Pessoa.codigo = inscricao.candidato  ");
		sql.append(" inner join UnidadeEnsinoCurso on UnidadeEnsinoCurso.codigo = inscricao.cursoOpcao1  ");
		sql.append(" inner join Curso on UnidadeEnsinoCurso.curso = Curso.codigo ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
		sql.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" where  inscricao.procSeletivo = ").append(codigoProcessoSeletivo);
		sql.append(" and inscricao.situacao = 'CO' ");
		if (distribuirCandidatosSemSala != null && distribuirCandidatosSemSala) {
			sql.append(" and (inscricao.sala is null or inscricao.sala = 0) ");
		}
		sql.append(" order by ");
		if (agruparPorNecessidadesEspeciais) {
			sql.append(" case when trim(Pessoa.necessidadesEspeciais) = '' then 1 else 0 end, ");
		}
		if (formaDistribuicao.equals("CURSO")) {
			sql.append(" sem_acentos(curso.nome), sem_acentos(pessoa.nome) ");
		} else {
			sql.append(" sem_acentos(pessoa.nome) ");
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		Ordenacao.ordenarLista(salaLocalAulaVOs, "ordemDistribuicao");
		boolean todosProcessado = false;
		q: for (SalaLocalAulaVO salaLocalAulaVO : salaLocalAulaVOs) {
			if (todosProcessado) {
				break;
			}
			if (salaLocalAulaVO.getUtilizarDistribuicao()) {
				Integer capacidade = salaLocalAulaVO.getCapacidade();
				if (distribuirCandidatosSemSala != null && distribuirCandidatosSemSala) {
					capacidade = capacidade - consultarNumeroInscritosSalaEspecifica(codigoProcessoSeletivo, itemProcSeletivoDataProva, salaLocalAulaVO.getCodigo());
				}
				for (int y = 1; y <= capacidade; y++) {
					if (!rs.next()) {
						todosProcessado = true;
						continue q;
					}
					alterarSalaInscricao(rs.getInt("codigoInscricao"), salaLocalAulaVO.getCodigo(), usuarioVO);
				}
			}
		}
		/**
		 * Este é executado para garantir que em uma redistribuição os distribuidos anteriormente que agora não cabem nesta nova distribuição seja
		 * desvinculado da sala de aula ficando pendente de distribuição, caso contrario iria estourar a capacidade maxima da sala de aula
		 */
		if (!todosProcessado) {
			while (rs.next()) {
				alterarSalaInscricao(rs.getInt("codigoInscricao"), 0, usuarioVO);
			}
		}

	}

	public static final float PONTO = 2.83f;

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta. Tem como passagem por parâmetros a <code>LayoutEtiquetaVO</code> e
	 * <code>OrigemImagemVO</code>
	 */
	@Override
	public String realizarImpressaoEtiquetaInscricaoProcessoSeletiva(LayoutEtiquetaVO layoutEtiqueta, List<InscricaoVO> inscricaoVOs, Integer numeroCopias, Integer linha, Integer coluna, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {
			if (layoutEtiqueta.getAlturaEtiqueta().equals(0) || layoutEtiqueta.getAlturaFolhaImpressao().equals(0) || layoutEtiqueta.getLarguraEtiqueta().equals(0) || layoutEtiqueta.getLarguraFolhaImpressao().equals(0)) {
				throw new Exception("Insira um valor para altura ou largura");
			}
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, writer, pdf, inscricaoVOs, numeroCopias, linha, coluna);
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdf != null) {
				pdf.close();
			}
			if (arquivo != null) {
				arquivo.close();
			}
		}

	}

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta para o tipo de impressora Laser/Tinta. Tem como passagem por
	 * parâmetros a <code>LayoutEtiquetaVO</code>>
	 */

	public void realizarMontagemPreviewEtiquetLaserTinta(LayoutEtiquetaVO layoutEtiqueta, PdfWriter writer, Document pdf, List<InscricaoVO> inscricaoVOs, Integer numeroCopias, Integer linha, Integer coluna) throws Exception {
		float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;

		float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;

		float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

		float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
		float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
		float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
		float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
		Float margemSuperiorColuna = 0f;
		Float margemEsquerdaLabel = 0f;
		Float margemSuperiorLabel = 0f;
		Float margemEsquerdaColuna = 0f;

		PdfContentByte canvas = writer.getDirectContent();

		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();

		for (InscricaoVO inscricaoVO : inscricaoVOs) {
			for (int copia = 1; copia <= numeroCopias; copia++) {

				margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);

				for (LayoutEtiquetaTagVO tag : layoutEtiqueta.getLayoutEtiquetaTagVOs()) {
					margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
					margemSuperiorLabel = tag.getMargemTopo() * PONTO;
					PdfTemplate tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
					tmp.beginText();
					tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), tag.getTamanhoFonte().floatValue());
					tmp.showTextAligned(Element.ALIGN_LEFT, (tag.getLabelTag() + " " + getValorImprimirEtiqueta(inscricaoVO, tag.getTagEtiqueta())).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
					tmp.endText();
					canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);

				}
				if (coluna + 1 > colunas) {
					coluna = 1;
					if (linha + 1 > linhas) {
						pdf.newPage();
						linha = 1;
					} else {
						linha++;
					}
				} else {
					coluna++;
				}
			}
		}
	}

	private String getValorImprimirEtiqueta(InscricaoVO inscricaoVO, TagEtiquetaEnum tag) {
		switch (tag) {
		case CPF_PESSOA:
			return inscricaoVO.getCandidato().getCPF();
		case DATA:
			return Uteis.getData(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva(), "dd/MM/yy");
		case HORA:
			return inscricaoVO.getItemProcessoSeletivoDataProva().getHora();
		case NOME_CURSO:
			return inscricaoVO.getCursoOpcao1().getCurso().getNome();
		case NOME_PESSOA:
			return inscricaoVO.getCandidato().getNome();
		case NUMERO_INSCRICAO_PROCESSO_SELETIVO:
			return String.valueOf(inscricaoVO.getCodigo());
		case NOME_TURNO:
			return inscricaoVO.getCursoOpcao1().getTurno().getNome();
		case NOME_UNIDADE_ENSINO:
			return inscricaoVO.getUnidadeEnsino().getNome();
		case RG_PESSOA:
			return inscricaoVO.getCandidato().getRG();
		case SALA:
			return inscricaoVO.getSala().getSala();
		case CANHOTO:
			if (inscricaoVO.getCandidato().getCanhoto()) {
				return "Canhoto";
			}
		default:
			return "";
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarSalaInscricao(Integer inscricao, Integer sala, UsuarioVO usuario) throws Exception {
		if (sala != null && sala > 0) {
			getConexao().getJdbcTemplate().update("UPDATE inscricao set sala = " + sala + " where codigo = " + inscricao + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		} else {
			getConexao().getJdbcTemplate().update("UPDATE inscricao set sala = null where codigo = " + inscricao + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return Inscricao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Inscricao.idEntidade = idEntidade;
	}

	public List<SalaLocalAulaVO> consultarSalaPorProcessoSeletivo(Integer procSeletivo, Integer unidadeEnsino, Integer unidadeEnsinoCurso, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct sala.* from inscricao ");
		sb.append(" inner join SalaLocalAula sala on sala.codigo = inscricao.sala ");
		sb.append(" where procseletivo = ").append(procSeletivo);
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sb.append(" and unidadeensino = ").append(unidadeEnsino);
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sb.append(" and cursoOpcao1 = ").append(unidadeEnsinoCurso);
		}
		sb.append(" order by sala.sala ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SalaLocalAulaVO> vetResultado = new ArrayList<SalaLocalAulaVO>(0);
		SalaLocalAulaVO salaLocalAulaVO = null;
		while (tabelaResultado.next()) {
			salaLocalAulaVO = new SalaLocalAulaVO();
			salaLocalAulaVO.setCodigo(tabelaResultado.getInt("codigo"));
			salaLocalAulaVO.setSala(tabelaResultado.getString("sala"));
			vetResultado.add(salaLocalAulaVO);
		}
		return vetResultado;
	}

	@Override
	public List<SalaLocalAulaVO> consultarSalaPorProcessoSeletivoEDataAula(Integer procSeletivo, Integer itemProcSeletivoDataProva) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sala.codigo, sala.sala, local.local  from inscricao ");
		sb.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala ");
		sb.append(" left join LocalAula local on local.codigo = sala.LocalAula ");
		if (itemProcSeletivoDataProva != null && itemProcSeletivoDataProva > 0) {
			sb.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProva);
			sb.append(" and ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		}
		sb.append(" where inscricao.procseletivo = ").append(procSeletivo);
		sb.append(" group by sala.codigo, sala.sala, local.local ");
		sb.append(" order by local.local, sala.sala ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SalaLocalAulaVO> vetResultado = new ArrayList<SalaLocalAulaVO>(0);
		SalaLocalAulaVO salaLocalAulaVO = null;
		while (tabelaResultado.next()) {
			salaLocalAulaVO = new SalaLocalAulaVO();
			if (tabelaResultado.getInt("codigo") == 0) {
				salaLocalAulaVO.setCodigo(-1);
				salaLocalAulaVO.setSala("Sem Sala");
				salaLocalAulaVO.getLocalAula().setLocal("");
			} else {
				salaLocalAulaVO.setCodigo(tabelaResultado.getInt("codigo"));
				salaLocalAulaVO.setSala(tabelaResultado.getString("sala"));
				salaLocalAulaVO.getLocalAula().setLocal(tabelaResultado.getString("local"));
			}
			vetResultado.add(salaLocalAulaVO);
		}
		return vetResultado;
	}

	@Override
	public int consultarChamadaProcSeletivo(ProcSeletivoVO procSeletivoVO, Integer curso, Integer unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(distinct(inscricao.chamada)) as qtdeChamada from inscricao ");
		sqlStr.append(" where inscricao.chamada is not null and inscricao.chamada != 0 and inscricao.procseletivo = ").append(procSeletivoVO.getCodigo().intValue());
		sqlStr.append(" and (cursoopcao1 = ").append(curso.intValue());
		sqlStr.append(" or cursoopcao2 = ").append(curso.intValue());
		sqlStr.append(" or cursoopcao3 = ").append(curso.intValue()).append(") ");
		sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getInt("qtdeChamada");
		}
		return 0;
	}

	@Override
	public List<Integer> consultarNumeroChamada(ProcSeletivoVO procSeletivoVO, Integer curso, Integer unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct(inscricao.chamada) as chamada from inscricao ");
		sqlStr.append(" where inscricao.chamada is not null and inscricao.chamada != 0 and inscricao.procseletivo = ").append(procSeletivoVO.getCodigo().intValue());
		sqlStr.append(" and (cursoopcao1 = ").append(curso.intValue());
		sqlStr.append(" or cursoopcao2 = ").append(curso.intValue());
		sqlStr.append(" or cursoopcao3 = ").append(curso.intValue()).append(") ");
		sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<Integer> listaChamada = new ArrayList<Integer>(0);
		while (dadosSQL.next()) {
			listaChamada.add(dadosSQL.getInt("chamada"));
		}
		return listaChamada;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoPorCandidato(final String numeroInscricaoCandidato, final String situacaoInscricao, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Inscricao set situacaoInscricao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, situacaoInscricao);
				if (Uteis.isAtributoPreenchido(numeroInscricaoCandidato)) {
					sqlAlterar.setInt(2, Integer.parseInt(numeroInscricaoCandidato));
				}else {
					sqlAlterar.setInt(2, 0);
				}
				return sqlAlterar;
			}
		});
	}

	@Override
	/**
	 * Método responsável por executar a verificação se existe alguma Inscrição vincula a este Processo Seletivo e a esta UnidadeEnsinoCurso.
	 */
	public boolean verificarExisteInscricaoVinculadaProcSeletivoUnidadeEnsinoCurso(Integer procSeletivo, Integer unidadeEnsinoCurso, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(codigo) from inscricao ");
		sqlStr.append("where procseletivo = ").append(procSeletivo);
		sqlStr.append(" and (cursoopcao1 = ").append(unidadeEnsinoCurso);
		sqlStr.append(" or cursoopcao2 = ").append(unidadeEnsinoCurso);
		sqlStr.append(" or cursoopcao3 = ").append(unidadeEnsinoCurso).append(")");
		return getConexao().getJdbcTemplate().queryForInt(sqlStr.toString()) > 0;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoInscricaoNaoCompareceu(final InscricaoVO inscricao, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "update inscricao set situacaoinscricao = ? where inscricao.codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, inscricao.getSituacaoInscricao().name());
					sqlAlterar.setInt(2, inscricao.getCodigo());
					return sqlAlterar;
				}
			});
			ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			getFacadeFactory().getResultadoProcessoSeletivoFacade().excluir(resultadoProcessoSeletivoVO, usuarioVO);
			gerarCampanhaCRMInscricaoProcessoSeletivoLancamentoResultado(inscricao, usuarioVO);
		} catch (Exception e) {
			inscricao.setSituacaoInscricao(SituacaoInscricaoEnum.ATIVO);
			throw e;
		}
	}

	public void gerarCampanhaCRMInscricaoProcessoSeletivoLancamentoResultado(final InscricaoVO obj, UsuarioVO usuario) {
		try {
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gerarAgendaCampanhaCRMInscricaoProcessoSeletivo(obj.getProcSeletivo().getCodigo(), obj, PoliticaGerarAgendaEnum.GERAR_AO_LANCAR_RESULTADO_INSCRICAO, false, usuario);
		} catch (Exception e) {
			// Nao gera-se excecao pois este processo nao pode impedir o lancamento do resultado
			String erro = e.getMessage();
		}
	}

	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoInscricao(final InscricaoVO inscricao, final SituacaoInscricaoEnum situacao, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "update inscricao set situacaoinscricao = ? where inscricao.codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, situacao.toString());
					sqlAlterar.setInt(2, inscricao.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<InscricaoVO> executarMontagemInscricaoVOs(Integer aluno, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Inscricao.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT Inscricao.codigo, Inscricao.codigoFinanceiroMatricula, Inscricao.situacaoInscricao, Inscricao.contareceber, Inscricao.situacao as situacaoInsc, ProcSeletivo.descricao, ItemProcSeletivoDataProva.dataProva, Matricula.matricula, resultadoPrimeiraOpcao ");
		sqlStr.append("FROM Inscricao ");
		sqlStr.append("INNER JOIN ProcSeletivo ON ProcSeletivo.codigo = Inscricao.procSeletivo ");
		sqlStr.append("INNER JOIN ItemProcSeletivoDataProva ON ItemProcSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ");
		sqlStr.append("LEFT JOIN ResultadoProcessoSeletivo ON ResultadoProcessoSeletivo.inscricao = Inscricao.codigo ");
		sqlStr.append("LEFT JOIN Matricula ON Matricula.inscricao = Inscricao.codigo ");
		sqlStr.append("WHERE Inscricao.candidato = ").append(aluno);
		sqlStr.append(" ORDER BY Inscricao.codigo");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosInscricaoFichaAluno(rs);
	}

	private List<InscricaoVO> montarDadosInscricaoFichaAluno(SqlRowSet rs) throws Exception {
		List<InscricaoVO> inscricaoVOs = new ArrayList<InscricaoVO>(0);
		while (rs.next()) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(rs.getString("situacaoInscricao")));
			inscricaoVO.setSituacao(rs.getString("situacaoInsc"));
                        inscricaoVO.setCodigoFinanceiroMatricula(rs.getString("codigoFinanceiroMatricula"));
			inscricaoVO.setContaReceber(rs.getInt("contareceber"));
			inscricaoVO.getProcSeletivo().setDescricao(rs.getString("descricao"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setDataProva(rs.getDate("dataProva"));
			inscricaoVO.getMatriculaVO().setMatricula(rs.getString("matricula"));
			if (inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao(rs.getString("resultadoPrimeiraOpcao"));
				if (inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP") || inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("A2") || inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("A3")) {
					if (!Uteis.isAtributoPreenchido(inscricaoVO.getMatriculaVO().getMatricula())) {
						inscricaoVO.getMatriculaVO().setMatricula("Não Matrículado");
					}

				}
			} else {
				inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao(inscricaoVO.getSituacaoInscricao_Apresentar());
			}
			inscricaoVOs.add(inscricaoVO);
		}
		return inscricaoVOs;
	}
	

	public List<ProcessoSeletivoAtaProvaRelVO> consultaInscricaoProcessoSeletivoAtaProvaVOs(Integer procSeletivo, Integer sala, ItemProcSeletivoDataProvaVO dataProva, Integer qtdeLinhas, String ordenarPor, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select inscricao.procseletivo, salalocalaula.codigo, count(inscricao.codigo) as qtdInscricao, salalocalaula.sala, itemprocseletivodataprova.codigo, itemprocseletivodataprova.dataprova from inscricao ");
		sqlStr.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.procseletivo = inscricao.procseletivo ");
		sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 ");
		sqlStr.append(" inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = inscricao.candidato ");
		sqlStr.append(" left join salalocalaula on salalocalaula.codigo = inscricao.sala ");
		sqlStr.append(" where 1=1 and inscricao.situacao not in ('PF') and inscricao.situacaoinscricao = 'ATIVO' ");
		if (procSeletivo > 0) {
			sqlStr.append(" and inscricao.procseletivo = " + procSeletivo);
		}
		if (sala.intValue() > 0) {
			sqlStr.append(" and salalocalaula.codigo = " + sala);
		}
		if (dataProva.getCodigo().intValue() > 0) {
			sqlStr.append(" and itemprocseletivodataprova.codigo = " + dataProva.getCodigo());
		}
		sqlStr.append(" group by inscricao.procseletivo, salalocalaula.codigo, salalocalaula.sala, itemprocseletivodataprova.codigo, itemprocseletivodataprova.dataprova  ");
		if (ordenarPor.equals("curso")) {
			sqlStr.append(" order by sem_acentos(curso.nome), itemprocseletivodataprova.dataprova, salalocalaula.sala ");			
		} else if (ordenarPor.equals("candidato")) {
			sqlStr.append(" order by sem_acentos(pessoa.nome), itemprocseletivodataprova.dataprova, salalocalaula.sala ");			
		} else {
			sqlStr.append(" order by itemprocseletivodataprova.dataprova, salalocalaula.sala ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ProcessoSeletivoAtaProvaRelVO> inscricaoVOs = new ArrayList<ProcessoSeletivoAtaProvaRelVO>(0);
		while (rs.next()) {
			ProcessoSeletivoAtaProvaRelVO inscricaoVO = new ProcessoSeletivoAtaProvaRelVO();
			inscricaoVO.setQtdInscricoes(rs.getInt("qtdInscricao"));
			inscricaoVO.getSala().setSala(rs.getString("sala"));
			inscricaoVO.getDataProva().setDataProva(rs.getDate("dataprova"));
			if (qtdeLinhas > 1) {
				inscricaoVOs.add(inscricaoVO);
				String vazio = " ";
				for (int x = 1; x < qtdeLinhas; x++) {					
					ProcessoSeletivoAtaProvaRelVO insc2 = new ProcessoSeletivoAtaProvaRelVO();
					insc2.setQtdInscricoes(rs.getInt("qtdInscricao"));
					insc2.getSala().setSala(rs.getString("sala") + vazio);
					insc2.getDataProva().setDataProva(rs.getDate("dataprova"));
					inscricaoVOs.add(insc2);
					vazio += " ";
				}
			} else {
				inscricaoVOs.add(inscricaoVO);
			}
		}
		return inscricaoVOs;		
	}

	public List<ProcessoSeletivoRedacaoRelVO> consultaInscricaoProcessoSeletivoRedacaoVOs(Integer procSeletivo, Integer unidadeEnsino, Integer curso,  Integer sala, ItemProcSeletivoDataProvaVO dataProva, Integer qtdeLinhas, String ordenarPor, Boolean ocultaLinha, String texto, Integer inscricao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select inscricao.codigo as inscricao, curso.nome, pessoa.nome, inscricao.procseletivo, salalocalaula.codigo, count(inscricao.codigo) as qtdInscricao, salalocalaula.sala, itemprocseletivodataprova.codigo, itemprocseletivodataprova.dataprova from inscricao ");
		sqlStr.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 ");
		sqlStr.append(" inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = inscricao.candidato ");
		sqlStr.append(" left join salalocalaula on salalocalaula.codigo = inscricao.sala ");
		sqlStr.append(" where 1=1 and inscricao.situacao not in ('PF') and inscricao.situacaoinscricao = 'ATIVO' ");
		if (procSeletivo > 0) {
			sqlStr.append(" and inscricao.procseletivo = " + procSeletivo);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensinocurso.unidadeEnsino = " + unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and unidadeensinocurso.curso = " + curso);
		}
		if (sala.intValue() > 0) {
			sqlStr.append(" and salalocalaula.codigo = " + sala);
		}else if (sala.equals(0)){
			sqlStr.append(" and inscricao.sala is null ");
		}
		if (dataProva.getCodigo().intValue() > 0) {
			sqlStr.append(" and itemprocseletivodataprova.codigo = " + dataProva.getCodigo());
		}
		if (inscricao.intValue() > 0) {
			sqlStr.append(" and inscricao.codigo = " + inscricao.intValue());
		}
		sqlStr.append(" group by curso.nome, pessoa.nome, inscricao.codigo, inscricao.procseletivo, salalocalaula.codigo, salalocalaula.sala, itemprocseletivodataprova.codigo, itemprocseletivodataprova.dataprova  ");
		if (ordenarPor.equals("curso")) {
			sqlStr.append(" order by sem_acentos(curso.nome), sem_acentos(pessoa.nome), itemprocseletivodataprova.dataprova, salalocalaula.sala ");			
		} else if (ordenarPor.equals("dataProva")) {
			sqlStr.append(" order by sem_acentos(pessoa.nome), itemprocseletivodataprova.dataprova, salalocalaula.sala ");			
		} else if (ordenarPor.equals("sala")) {
			sqlStr.append(" order by salalocalaula.sala , sem_acentos(pessoa.nome), itemprocseletivodataprova.dataprova");			
		} else {
			sqlStr.append(" order by sem_acentos(pessoa.nome), itemprocseletivodataprova.dataprova, salalocalaula.sala ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ProcessoSeletivoRedacaoRelVO> inscricaoVOs = new ArrayList<ProcessoSeletivoRedacaoRelVO>(0);
		while (rs.next()) {
			ProcessoSeletivoRedacaoRelVO inscricaoVO = new ProcessoSeletivoRedacaoRelVO(qtdeLinhas, ocultaLinha);
			inscricaoVO.setInscricao(rs.getInt("inscricao"));
			inscricaoVO.setQtdInscricoes(rs.getInt("qtdInscricao"));
			inscricaoVO.getSala().setSala(rs.getString("sala"));
			inscricaoVO.setTextoOrientacaoRodape(texto);
			inscricaoVO.getDataProva().setDataProva(rs.getDate("dataprova"));
			inscricaoVOs.add(inscricaoVO);
		}
		return inscricaoVOs;		
	}
	
	public List<InscricaoVO> consultaInscricaoPendenteParaNotificacao() throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select inscricao.codigo, contareceber.datavencimento, configuracaogeralsistema.nrDiasNotifVencCand, (contareceber.datavencimento::date - configuracaogeralsistema.nrDiasNotifVencCand) as datanotf from inscricao ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = inscricao.unidadeensino ");
		sqlStr.append(" inner join configuracoes on configuracoes.codigo = unidadeensino.configuracoes ");
		sqlStr.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = configuracoes.codigo ");
		sqlStr.append(" inner join contareceber on contareceber.codigo = inscricao.contareceber ");
		sqlStr.append(" where contareceber.datavencimento >= current_date and contareceber.situacao = 'AR' ");
		sqlStr.append(" and current_date = (contareceber.datavencimento::date - configuracaogeralsistema.nrDiasNotifVencCand) and dataNotificouVencInscricao is null ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<InscricaoVO> inscricaoVOs = new ArrayList<InscricaoVO>(0);
		while (rs.next()) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVOs.add(inscricaoVO);
		}
		return inscricaoVOs;		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarAlteracaoDadosJaGravadosEtapa1PorCodigoInscricao(Integer inscricao, Integer unidadeEnsino, Integer procSeletivo, Integer cursoOpcao1, Date dataProva, UsuarioVO usuarioVO)  throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct case ");
		sb.append(" when (inscricao.procseletivo = ").append(procSeletivo);
		sb.append(") then false else true end AS condicao ");
		sb.append(" from inscricao ");
		sb.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.procseletivo = inscricao.procseletivo ");
		sb.append(" where inscricao.codigo = ").append(inscricao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("condicao");
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoVinculoInscricaoComGabarito(final InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "update inscricao set gabarito = ? where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				if (Uteis.isAtributoPreenchido(inscricaoVO.getGabaritoVO())) {
					sqlAlterar.setInt(1, inscricaoVO.getGabaritoVO().getCodigo());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoVinculoInscricaoComProvaProcessoSeletivo(final InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "update inscricao set provaProcessoSeletivo = ? where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				if (Uteis.isAtributoPreenchido(inscricaoVO.getProvaProcessoSeletivoVO())) {
					sqlAlterar.setInt(1, inscricaoVO.getProvaProcessoSeletivoVO().getCodigo());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public List<InscricaoVO> consultarCandidatoNotificacaoAlteracaoDataProva(ProcSeletivoVO processoSeletivo, ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProva,int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append(" candidato.codigo as candidato,candidato.email,candidato.email2,candidato.celular, ");
		sqlStr.append(" inscricao.codigo as numeroinscricao, candidato.nome as nomecandidato,itemprocseletivodataprova.dataprova, ");
		sqlStr.append(" procseletivo.descricao as processoseletivo, itemprocseletivodataprova.motivoalteracaodataprova as motivo,unidadeensino.nome as unidadeensino,curso.nome as curso ");
		sqlStr.append("FROM inscricao ");
		sqlStr.append(" INNER JOIN pessoa as candidato        ON candidato.codigo                 = inscricao.candidato ");
		sqlStr.append(" INNER JOIN procseletivo               ON procseletivo.codigo              = inscricao.procseletivo ");
		sqlStr.append(" INNER JOIN unidadeensinocurso         ON unidadeensinocurso.codigo        = inscricao.cursoopcao1 ");
		sqlStr.append(" INNER JOIN curso                      ON unidadeensinocurso.curso         = curso.codigo ");
		sqlStr.append(" INNER JOIN unidadeensino              ON unidadeensino.codigo             = unidadeensinocurso.unidadeensino ");
		sqlStr.append(" INNER JOIN ItemProcSeletivoDataProva  ON ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sqlStr.append("WHERE 1=1 ");
		sqlStr.append(" AND inscricao.situacao = 'CO'");
		sqlStr.append(" AND inscricao.situacaoInscricao = 'ATIVO'");
		sqlStr.append(" AND procseletivo.codigo = ").append(processoSeletivo.getCodigo());
		sqlStr.append(" AND ItemProcSeletivoDataProva.codigo = ").append(itemProcessoSeletivoDataProva.getCodigo()).append(" ;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaNotificacaoCandidato(tabelaResultado, nivelMontarDados, usuario));		
	}
	
	public static List<InscricaoVO> montarDadosConsultaNotificacaoCandidato(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosNotificacaoCandidato(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static InscricaoVO montarDadosNotificacaoCandidato(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		InscricaoVO obj = new InscricaoVO();
		obj.getCandidato().setCodigo(dadosSQL.getInt("candidato"));
		obj.getCandidato().setEmail(dadosSQL.getString("email"));
		obj.getCandidato().setEmail2(dadosSQL.getString("email2"));
		obj.getCandidato().setCelular(dadosSQL.getString("celular"));
		obj.getCandidato().setNome(dadosSQL.getString("nomecandidato"));
		obj.setCodigo(dadosSQL.getInt("numeroinscricao"));
		obj.getCursoOpcao1().getCurso().setNome(dadosSQL.getString("curso"));
		obj.getItemProcessoSeletivoDataProva().setDataProva(dadosSQL.getDate("dataprova"));
		obj.getProcSeletivo().setDescricao(dadosSQL.getString("processoseletivo"));
		obj.getItemProcessoSeletivoDataProva().setMotivoAlteracaoDataProva(dadosSQL.getString("motivo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
		return obj;
	}
	
	@Override
	public Integer consultarQuantidadeIncricaoVinculadaItemProcessoSeletivoDataProva(Integer itemProcessoSeletivoDataProva) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT COUNT(inscricao.itemprocessoseletivodataprova) as quantidade_registro FROM inscricao WHERE inscricao.itemprocessoseletivodataprova = ").append(itemProcessoSeletivoDataProva).append(" ;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("quantidade_registro");
		}
		return 0;
	}
	
	@Override
	public List<InscricaoVO> inicializarDadosInscricaoProcessoSeletivoFichaAluno(Integer aluno, UsuarioVO usuarioVO) throws Exception {
		List<InscricaoVO> listaInscricaoVOs = consultarPorAlunoFichaAluno(aluno, usuarioVO);
		return listaInscricaoVOs;
	}
	
	public List<InscricaoVO> consultarPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select inscricao.codigo, inscricao.data, itemprocseletivodataprova.dataprova, inscricao.situacaoinscricao, inscricao.contaReceber, inscricao.situacao, ");
		sb.append(" procseletivo.codigo AS \"procseletivo.codigo\", procseletivo.descricao AS \"procseletivo.descricao\", ");
		sb.append(" ue1.codigo AS \"ue1.codigo\", ue1.nome AS \"ue1.nome\", c1.codigo AS \"c1.codigo\", c1.nome AS \"c1.nome\", t1.codigo AS \"t1.codigo\", t1.nome AS \"t1.nome\", ");
		sb.append(" ue2.codigo AS \"ue2.codigo\", ue2.nome AS \"ue2.nome\", c2.codigo AS \"c2.codigo\", c2.nome AS \"c2.nome\", t2.codigo AS \"t2.codigo\", t2.nome AS \"t2.nome\", ");
		sb.append(" ue3.codigo AS \"ue3.codigo\", ue3.nome AS \"ue3.nome\", c3.codigo AS \"c3.codigo\", c3.nome AS \"c3.nome\", t3.codigo AS \"t3.codigo\", t3.nome AS \"t3.nome\", ");
		sb.append(" resultadoprocessoseletivo.codigo AS \"resultadoprocessoseletivo.codigo\", ");
		sb.append(" resultadoprocessoseletivo.resultadoprimeiraopcao, resultadoprocessoseletivo.resultadosegundaopcao, resultadoprocessoseletivo.resultadoterceiraopcao, matricula.matricula ");
		sb.append(" from inscricao ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sb.append(" left join resultadoprocessoseletivo on resultadoprocessoseletivo.inscricao = inscricao.codigo ");
		sb.append(" left join matricula on matricula.inscricao = inscricao.codigo ");
		
		sb.append(" left join unidadeensinocurso uec1 on uec1.codigo = inscricao.cursoopcao1 ");
		sb.append(" left join unidadeensino ue1 on ue1.codigo = uec1.unidadeensino ");
		sb.append(" left join curso c1 on c1.codigo = uec1.curso ");
		sb.append(" left join turno t1 on t1.codigo = uec1.turno ");
		
		sb.append(" left join unidadeensinocurso uec2 on uec2.codigo = inscricao.cursoopcao2 ");
		sb.append(" left join unidadeensino ue2 on ue2.codigo = uec2.unidadeensino ");
		sb.append(" left join curso c2 on c2.codigo = uec2.curso ");
		sb.append(" left join turno t2 on t2.codigo = uec2.turno ");
		
		sb.append(" left join unidadeensinocurso uec3 on uec3.codigo = inscricao.cursoopcao3 ");
		sb.append(" left join unidadeensino ue3 on ue3.codigo = uec3.unidadeensino ");
		sb.append(" left join curso c3 on c3.codigo = uec3.curso ");
		sb.append(" left join turno t3 on t3.codigo = uec3.turno ");
		sb.append(" where inscricao.candidato = ").append(aluno);
		sb.append(" order by inscricao.data desc ");
		
		List<InscricaoVO> listaInscricaoVOs = new ArrayList<InscricaoVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setData(tabelaResultado.getDate("data"));
			obj.setContaReceber(tabelaResultado.getInt("contaReceber"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.getItemProcessoSeletivoDataProva().setDataProva(tabelaResultado.getDate("dataProva"));
			if (tabelaResultado.getString("situacaoInscricao") != null) {
				obj.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(tabelaResultado.getString("situacaoInscricao")));
			}
			obj.getProcSeletivo().setCodigo(tabelaResultado.getInt("procseletivo.codigo"));
			obj.getProcSeletivo().setDescricao(tabelaResultado.getString("procseletivo.descricao"));
			
			obj.getCursoOpcao1().setUnidadeEnsino(tabelaResultado.getInt("ue1.codigo"));
			obj.getCursoOpcao1().setNomeUnidadeEnsino(tabelaResultado.getString("ue1.nome"));
			obj.getCursoOpcao1().getCurso().setCodigo(tabelaResultado.getInt("c1.codigo"));
			obj.getCursoOpcao1().getCurso().setNome(tabelaResultado.getString("c1.nome"));
			obj.getCursoOpcao1().getTurno().setCodigo(tabelaResultado.getInt("t1.codigo"));
			obj.getCursoOpcao1().getTurno().setNome(tabelaResultado.getString("t1.nome"));
			
			obj.getCursoOpcao2().setUnidadeEnsino(tabelaResultado.getInt("ue2.codigo"));
			obj.getCursoOpcao2().setNomeUnidadeEnsino(tabelaResultado.getString("ue2.nome"));
			obj.getCursoOpcao2().getCurso().setCodigo(tabelaResultado.getInt("c2.codigo"));
			obj.getCursoOpcao2().getCurso().setNome(tabelaResultado.getString("c2.nome"));
			obj.getCursoOpcao2().getTurno().setCodigo(tabelaResultado.getInt("t2.codigo"));
			obj.getCursoOpcao2().getTurno().setNome(tabelaResultado.getString("t2.nome"));
			
			obj.getCursoOpcao3().setUnidadeEnsino(tabelaResultado.getInt("ue3.codigo"));
			obj.getCursoOpcao3().setNomeUnidadeEnsino(tabelaResultado.getString("ue3.nome"));
			obj.getCursoOpcao3().getCurso().setCodigo(tabelaResultado.getInt("c3.codigo"));
			obj.getCursoOpcao3().getCurso().setNome(tabelaResultado.getString("c3.nome"));
			obj.getCursoOpcao3().getTurno().setCodigo(tabelaResultado.getInt("t3.codigo"));
			obj.getCursoOpcao3().getTurno().setNome(tabelaResultado.getString("t3.nome"));
			
			obj.getResultadoProcessoSeletivoVO().setCodigo(tabelaResultado.getInt("resultadoprocessoseletivo.codigo"));
			obj.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao(tabelaResultado.getString("resultadoPrimeiraOpcao"));
			obj.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao(tabelaResultado.getString("resultadoSegundaOpcao"));
			obj.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao(tabelaResultado.getString("resultadoTerceiraOpcao"));
			
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			
			listaInscricaoVOs.add(obj);
		}
		return listaInscricaoVOs;
	}

	@Override
	public List<InscricaoVO> consultarInscricaoNaoCompareceuProcessamentoResultadoProcessoSeletivo(List<Integer> listaInscricaoArquivoVOs, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala) {
		
		StringBuilder sb = new StringBuilder("SELECT inscricao.codigo, inscricao.situacao AS \"inscricao.situacao\", inscricao.situacaoInscricao AS \"inscricao.situacaoInscricao\", candidato.codigo as candidato_codigo, candidato.nome as candidato_nome, candidato.email as candidato_email, ");
		sb.append(" procseletivo.codigo as procseletivo_codigo, procseletivo.descricao as procseletivo_descricao, procseletivo.mediaMinimaAprovacao as procseletivo_mediaMinimaAprovacao, procseletivo.regimeAprovacao as procseletivo_regimeAprovacao, procseletivo.quantidadeAcertosMinimosAprovacao as procseletivo_quantidadeAcertosMinimosAprovacao, procseletivo.notaMinimaRedacao as procseletivo_notaMinimaRedacao, procseletivo.valorPorAcerto as procseletivo_valorPorAcerto, ");
		sb.append(" cursoopcao1.codigo as cursoopcao1_codigo, cursoopcao1.nome as cursoopcao1_nome, ");
		sb.append(" turnocursoopcao1.codigo as turnocursoopcao1_codigo, turnocursoopcao1.nome as turnocursoopcao1_nome, ");
		sb.append(" cursoopcao2.codigo as cursoopcao2_codigo, cursoopcao2.nome as cursoopcao2_nome, ");
		sb.append(" turnocursoopcao2.codigo as turnocursoopcao2_codigo, turnocursoopcao2.nome as turnocursoopcao2_nome, ");
		sb.append(" cursoopcao3.codigo as cursoopcao3_codigo, cursoopcao3.nome as cursoopcao3_nome, ");
		sb.append(" turnocursoopcao3.codigo as turnocursoopcao3_codigo, turnocursoopcao3.nome as turnocursoopcao3_nome, ");
		sb.append(" opcaolinguaestrangeira.codigo as opcaolinguaestrangeira_codigo, opcaolinguaestrangeira.nome as opcaolinguaestrangeira_nome,  ");
		sb.append(" unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, itemprocseletivodataprova.tipoProvaGabarito, itemprocseletivodataprova.dataProva ");
		sb.append(" from inscricao ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = inscricao.unidadeensino ");
		sb.append(" left join salalocalaula on salalocalaula.codigo = inscricao.sala ");
		sb.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		sb.append(" inner join curso as cursoopcao1 on cursoopcao1.codigo = unidadeensinocursoopcao1.curso ");
		sb.append(" inner join turno as turnocursoopcao1 on turnocursoopcao1.codigo = unidadeensinocursoopcao1.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao2 on unidadeensinocursoopcao2.codigo = inscricao.cursoopcao2 ");
		sb.append(" left join curso as cursoopcao2 on cursoopcao2.codigo = unidadeensinocursoopcao2.curso ");
		sb.append(" left join turno as turnocursoopcao2 on turnocursoopcao2.codigo = unidadeensinocursoopcao2.turno ");
		sb.append(" left join unidadeensinocurso as unidadeensinocursoopcao3 on unidadeensinocursoopcao3.codigo = inscricao.cursoopcao3 ");
		sb.append(" left join curso as cursoopcao3 on cursoopcao3.codigo = unidadeensinocursoopcao3.curso ");
		sb.append(" left join turno as turnocursoopcao3 on turnocursoopcao3.codigo = unidadeensinocursoopcao3.turno ");
		sb.append(" left join disciplinasprocseletivo as opcaolinguaestrangeira on opcaolinguaestrangeira.codigo = inscricao.opcaolinguaestrangeira ");
		sb.append(" where inscricao.situacaoInscricao = '").append(SituacaoInscricaoEnum.ATIVO.toString()).append("' ");
		sb.append(" and inscricao.situacao = 'CO' ");
		sb.append(" and inscricao.codigo not in( ");
		sb.append(" select resultadoprocessoseletivo.inscricao from resultadoprocessoseletivo where resultadoprocessoseletivo.inscricao = inscricao.codigo ");
		sb.append(") ");
		if (listaInscricaoArquivoVOs != null && !listaInscricaoArquivoVOs.isEmpty()) {
			sb.append(" and inscricao.codigo not in (");
			boolean primeiraVez = true;
			for (Integer inscricao : listaInscricaoArquivoVOs) {
				if (primeiraVez) {
					sb.append(inscricao);
					primeiraVez = false;
				} else {
					sb.append(", ").append(inscricao);
				}
			}
			sb.append(") ");
		}
		
		if (procSeletivo != null && !procSeletivo.equals(0)) {
			sb.append(" AND procseletivo.codigo = ").append(procSeletivo);
		}
		if (itemProcSeletivoDataProva != null && !itemProcSeletivoDataProva.equals(0)) {
			sb.append(" AND itemprocseletivodataprova.codigo = ").append(itemProcSeletivoDataProva);
		}
		if (sala != null && !sala.equals(0)) {
			sb.append(" AND salalocalaula.codigo = ").append(sala);
		}
		sb.append(" ORDER BY sem_acentos(candidato.nome)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<InscricaoVO> listaInscricaoVOs = new ArrayList<InscricaoVO>(0);
		while (rs.next()) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(rs.getInt("codigo"));
			inscricaoVO.setSituacao(rs.getString("inscricao.situacao"));
			if (rs.getString("inscricao.situacaoInscricao") != null) {
				inscricaoVO.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(rs.getString("inscricao.situacaoInscricao")));
			}
			inscricaoVO.getCandidato().setCodigo(rs.getInt("candidato_codigo"));
			inscricaoVO.getCandidato().setNome(rs.getString("candidato_nome"));
			inscricaoVO.getCandidato().setEmail(rs.getString("candidato_email"));
			inscricaoVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino_codigo"));
			inscricaoVO.getUnidadeEnsino().setNome(rs.getString("unidadeensino_nome"));
			inscricaoVO.getProcSeletivo().setCodigo(rs.getInt("procseletivo_codigo"));
			inscricaoVO.getProcSeletivo().setDescricao(rs.getString("procseletivo_descricao"));
			inscricaoVO.getProcSeletivo().setMediaMinimaAprovacao(rs.getDouble("procseletivo_mediaMinimaAprovacao"));
			inscricaoVO.getProcSeletivo().setQuantidadeAcertosMinimosAprovacao(rs.getInt("procseletivo_quantidadeAcertosMinimosAprovacao"));
			inscricaoVO.getProcSeletivo().setNotaMinimaRedacao(rs.getDouble("procseletivo_notaMinimaRedacao"));
			inscricaoVO.getProcSeletivo().setRegimeAprovacao(rs.getString("procseletivo_regimeAprovacao"));
			inscricaoVO.getProcSeletivo().setValorPorAcerto(rs.getDouble("procseletivo_valorPorAcerto"));
			inscricaoVO.getOpcaoLinguaEstrangeira().setCodigo(rs.getInt("opcaolinguaestrangeira_codigo"));
			inscricaoVO.getOpcaoLinguaEstrangeira().setNome(rs.getString("opcaolinguaestrangeira_nome"));
			inscricaoVO.getCursoOpcao1().getCurso().setCodigo(rs.getInt("cursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getCurso().setNome(rs.getString("cursoopcao1_nome"));
			inscricaoVO.getCursoOpcao1().getTurno().setCodigo(rs.getInt("turnocursoopcao1_codigo"));
			inscricaoVO.getCursoOpcao1().getTurno().setNome(rs.getString("turnocursoopcao1_nome"));
			inscricaoVO.getCursoOpcao2().getCurso().setCodigo(rs.getInt("cursoopcao2_codigo"));
			inscricaoVO.getCursoOpcao2().getTurno().setNome(rs.getString("cursoopcao2_nome"));
			inscricaoVO.getCursoOpcao2().getTurno().setCodigo(rs.getInt("turnocursoopcao2_codigo"));
			inscricaoVO.getCursoOpcao2().getCurso().setNome(rs.getString("turnocursoopcao2_nome"));
			inscricaoVO.getCursoOpcao3().getCurso().setCodigo(rs.getInt("cursoopcao3_codigo"));
			inscricaoVO.getCursoOpcao3().getCurso().setNome(rs.getString("cursoopcao3_nome"));
			inscricaoVO.getCursoOpcao3().getTurno().setCodigo(rs.getInt("turnocursoopcao3_codigo"));
			inscricaoVO.getCursoOpcao3().getTurno().setNome(rs.getString("turnocursoopcao3_nome"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setTipoProvaGabarito(rs.getString("tipoProvaGabarito"));
			inscricaoVO.getItemProcessoSeletivoDataProva().setDataProva(rs.getDate("dataProva"));
			inscricaoVO.setNovoObj(false);
			listaInscricaoVOs.add(inscricaoVO);
		}

		return listaInscricaoVOs;
	}
	
	@Override
	public void realizarAlteracaoInscricaoNaoCompareceu(List<InscricaoVO> listaInscricaoVOs, UsuarioVO usuarioVO) throws Exception {
		for (InscricaoVO inscricaoVO : listaInscricaoVOs) {
			if (inscricaoVO.getSelecionar()) {
				inscricaoVO.setSituacaoInscricao(SituacaoInscricaoEnum.NAO_COMPARECEU);
				getFacadeFactory().getInscricaoFacade().alterarSituacaoInscricaoNaoCompareceu(inscricaoVO, usuarioVO);
				inscricaoVO.setResultadoProcessoSeletivoVO(null);
				inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao("");
				inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao("");
				inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao("");
				inscricaoVO.getResultadoProcessoSeletivoVO().setInscricao(inscricaoVO);
			}
		}
	}
	
	class EnviarComunicadoNovaInscricaoProcessoSeletivo implements Runnable{
		private InscricaoVO inscricaoVO;

		public EnviarComunicadoNovaInscricaoProcessoSeletivo(InscricaoVO inscricaoVO) {
			super();
			this.inscricaoVO = inscricaoVO;
		}

		@Override
		public void run() {
			try {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemInscricaoProcessoSeletivo(inscricaoVO);
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		
		
	}
	
	class CampanhaCRMInscricaoProcessoSeletivo implements Runnable{
		
		private InscricaoVO inscricaoVO;
		private UsuarioVO usuarioVO;
		
		
		
		public CampanhaCRMInscricaoProcessoSeletivo(InscricaoVO inscricaoVO, UsuarioVO usuarioVO) {
			super();
			this.inscricaoVO = inscricaoVO;
			this.usuarioVO = usuarioVO;
		}

		public void run() {
			try {
				getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gerarAgendaCampanhaCRMInscricaoProcessoSeletivo(inscricaoVO.getProcSeletivo().getCodigo(), inscricaoVO, PoliticaGerarAgendaEnum.GERAR_NO_ATO_INSCRICAO, false, usuarioVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
	}
/**
	 * Consulta  se existe resultado processado pelo {@link GabaritoVO} informado.
	 * 
	 * @param gabaritoVO
	 * @return
	 * @throws Exception
	 */
	public Boolean existeResultadoProcessamentoRespostaPorGabarito(GabaritoVO gabaritoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CASE WHEN COUNT(inscricao.gabarito) > 0 THEN true ELSE false end AS existeResultado FROM resultadoprocessoseletivo ");
		sql.append(" INNER JOIN inscricao ON inscricao.codigo = resultadoprocessoseletivo.inscricao");
		sql.append(" WHERE gabarito = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), gabaritoVO.getCodigo());
        return tabelaResultado.next() ? tabelaResultado.getBoolean("existeResultado") : Boolean.FALSE;
	}
	
	public List<InscricaoVO> consultarInscricao(Integer processoSeletivo, Integer unidadeEnsino, String numeroInscricao, String nomeCandidato, Date dataInicioInscricao, Date dataFimInscricao, Date dataInicioProva, Date dataFimProva, Boolean filtroSituacaoInscricaoConsultaAtivo, Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao, Boolean filtroSituacaoInscricaoConsultaNaoCompareceu, Boolean filtroSituacaoInscricaoConsultaCancelado, Integer limite, Integer pagina, boolean controlarAcesso, Integer nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		List<Object> parametros = new ArrayList<>();
		sqlStr.append(" SELECT distinct (inscricao.codigo), inscricao.* ");
		getSQLPadraoConsultarInscricao(processoSeletivo, unidadeEnsino, numeroInscricao, nomeCandidato, dataInicioInscricao,
				dataFimInscricao, dataInicioProva, dataFimProva, filtroSituacaoInscricaoConsultaAtivo,
				filtroSituacaoInscricaoConsultaCancelouOutraInscricao, filtroSituacaoInscricaoConsultaNaoCompareceu,
				filtroSituacaoInscricaoConsultaCancelado, sqlStr, parametros);
		sqlStr.append(" ORDER BY inscricao.codigo");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario)); 
	}
	
	public Integer totalConsultaInscricao(Integer processoSeletivo, Integer unidadeEnsino, String numeroInscricao, String nomeCandidato, Date dataInicioInscricao, Date dataFimInscricao, Date dataInicioProva, Date dataFimProva, Boolean filtroSituacaoInscricaoConsultaAtivo, Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao, Boolean filtroSituacaoInscricaoConsultaNaoCompareceu, Boolean filtroSituacaoInscricaoConsultaCancelado, Integer limite, Integer pagina, boolean controlarAcesso, Integer nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> parametros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder(" SELECT count(distinct inscricao.codigo) as qtde ");
		getSQLPadraoConsultarInscricao(processoSeletivo, unidadeEnsino, numeroInscricao, nomeCandidato, dataInicioInscricao,
				dataFimInscricao, dataInicioProva, dataFimProva, filtroSituacaoInscricaoConsultaAtivo,
				filtroSituacaoInscricaoConsultaCancelouOutraInscricao, filtroSituacaoInscricaoConsultaNaoCompareceu,
				filtroSituacaoInscricaoConsultaCancelado, sqlStr, parametros);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}
	
	public void validarArquivoInscricao(InscricaoVO inscricaoVO, ArquivoVO arquivoVO) throws Exception {
		
		if (!inscricaoVO.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR) &&  !Uteis.isAtributoPreenchido(inscricaoVO.getFormaIngresso())) {
			throw new ConsistirException("É obrigatório informar o tipo de ingresso.");
		}
		
		if (inscricaoVO.getFormaIngresso().equals("PD")	&& inscricaoVO.getProcSeletivo().getUploadComprovantePortadorDiploma()
				&& !Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
			throw new ConsistirException("É obrigatório realizar o upload do comprovante de diploma.");
		}
		if (inscricaoVO.getFormaIngresso().equals("EN") && inscricaoVO.getProcSeletivo().getUploadComprovanteEnem()
				&& !Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
			throw new ConsistirException("É obrigatório realizar o upload do comprovante de realização do ENEM.");
		}
		if (inscricaoVO.getFormaIngresso().equals("TR") && inscricaoVO.getProcSeletivo().getObrigarUploadComprovanteTransferencia()
				&& !Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
			throw new ConsistirException("É obrigatório realizar o upload do comprovante de transferência.");
		}
		if (inscricaoVO.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR)
				&& !Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
			throw new ConsistirException("É obrigatório realizar o upload do comprovante de avaliação curricular.");
		}
	}
	
	private void realizarBaixaContaReceberInscricaoPorLiberacaoFinanceira(InscricaoVO inscricaoVO, ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		if(inscricaoVO.getLiberarPagamento() && contaReceberVO.getSituacaoAReceber()) { 
			getFacadeFactory().getContaReceberFacade().baixarContaReceberVOConcedendoDescontoTotalAMesma(contaReceberVO, new Date(), "Isenção de Taxa de Inscrição", true, usuario, configuracaoFinanceiro, usuario);
//			contaReceberVO.setRealizandoRecebimento(true);
//			contaReceberVO.getCalcularValorFinal(configuracaoFinanceiro, usuario);
//			contaReceberVO.setTipoDescontoLancadoRecebimento("VA");
//			contaReceberVO.setValorDescontoLancadoRecebimento(contaReceberVO.getValorReceberCalculado());
//			contaReceberVO.setValorCalculadoDescontoLancadoRecebimento(contaReceberVO.getValorReceberCalculado());			
//			contaReceberVO.setValorFinalCalculado(0.0);
//			contaReceberVO.setValorRecebido(0.0);
//			contaReceberVO.setValorDescontoCalculado(0.0);
//			contaReceberVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(0.0);
//			contaReceberVO.setValorDescontoCalculadoSegundaFaixaDescontos(0.0);
//			contaReceberVO.setValorDescontoCalculadoTerceiraFaixaDescontos(0.0);
//			contaReceberVO.setValorDescontoCalculadoQuartaFaixaDescontos(0.0);
//			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
//			contaReceberNegociacaoRecebimentoVO.setContaReceber(contaReceberVO);
//			NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
//			negociacaoRecebimentoVO.setResponsavel(usuario);
//			negociacaoRecebimentoVO.setUnidadeEnsino(inscricaoVO.getUnidadeEnsino());
//			negociacaoRecebimentoVO.setContaCorrenteCaixa(contaReceberVO.getContaCorrenteVO());
//			negociacaoRecebimentoVO.setTipoPessoa("CA");
//			negociacaoRecebimentoVO.setPessoa(inscricaoVO.getCandidato());
//			negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(contaReceberNegociacaoRecebimentoVO);
//			getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO, configuracaoFinanceiro, false, usuario);
		}
	}
	
	public InscricaoVO consultarPorCodigoCertidaoNascimentoCPF(Integer codigoInscricao , String cpf  ,String  certidaoNasimento,  int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from Inscricao insc ");
		sqlStr.append(" inner join pessoa p on p.codigo = insc.candidato  where 1=1  ");		
		if(Uteis.isAtributoPreenchido(certidaoNasimento)) {	
		   sqlStr.append(" and p.certidadonascimento = ").append(certidaoNasimento);
		}
		if(Uteis.isAtributoPreenchido(cpf)) {	
			sqlStr.append(" and (replace(replace((p.cpf),'.',''),'-','')) = '").append(cpf).append("'");
		}		
		sqlStr.append(" and insc.codigo = ").append(codigoInscricao);		
		sqlStr.append(" order by insc.codigo desc  ");		
	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public InscricaoVO consultarUltimaInscricaoPessoaProvaOnlinePorNumeroDocumento(String tipoDocumento, String numeroDocumento,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE 1=1 ");
		if(tipoDocumento.equals("CPF") && (Uteis.isAtributoPreenchido(numeroDocumento) ||  numeroDocumento != null )) {	
			   sqlStr.append(" and (replace(replace((pessoa.cpf),'.',''),'-','')) = ").append("(replace(replace(('").append(numeroDocumento).append("'),'.',''),'-',''))");
		}
		if(tipoDocumento.equals("CERTIDAO_NASCIMENTO") && (Uteis.isAtributoPreenchido(numeroDocumento)||  numeroDocumento != null )) {	
			   sqlStr.append(" and pessoa.certidaonascimento = '").append(numeroDocumento).append("'");
		}
		if(tipoDocumento.equals("CODIGO_INSCRICAO") && (Uteis.isAtributoPreenchido(numeroDocumento) ||  numeroDocumento != null )) {	
			   sqlStr.append(" and inscricao.codigo = ").append(Integer.parseInt(numeroDocumento));
		}
		sqlStr.append(" ORDER BY Inscricao.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			montarDadosCompleto(obj, tabelaResultado, usuario);
			montarDadosContaReceber(obj, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), usuario);
			return obj;
		}
		return new InscricaoVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO, Integer codigoAutenticacao, String meioAutenticacao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean controlarAcesso , Boolean codigoAutenticacaoExpirada, Boolean apenasAlterar, UsuarioVO usuario) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(inscricaoVO.getCodigoAutenticacao()) || codigoAutenticacaoExpirada) {
			inscricaoVO.setCodigoAutenticacao(codigoAutenticacao);
			inscricaoVO.setDataHoraVencimentoCodigoAutenticacao(Uteis.getDataJDBCTimestamp(UteisData.adicionarDiasEmData(new Date(), 30)));
			final String sql = "UPDATE Inscricao set codigoAutenticacao=? , dataHoraVencimentoCodigoAutenticacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
	
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(codigoAutenticacao)) {
						sqlAlterar.setInt(1, codigoAutenticacao);
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraVencimentoCodigoAutenticacao())) {
						sqlAlterar.setTimestamp(2, inscricaoVO.getDataHoraVencimentoCodigoAutenticacao());
					}else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, inscricaoVO.getCodigo());
					return sqlAlterar;
				}
			});
		}

		if (!apenasAlterar) {
			List<EmailVO> listaEmail = new ArrayList<EmailVO>();
			List<SMSVO> listaSMS = new ArrayList<SMSVO>();
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_CODIGO_AUTENTICACAO, false, usuario);
			if (mensagemTemplate != null) {
				ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
				comunicacaoInternaVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
				comunicacaoInternaVO.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
				comunicacaoInternaVO.setTipoMarketing(Boolean.FALSE);
				comunicacaoInternaVO.setTipoLeituraObrigatoria(Boolean.FALSE);
				comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
				comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
				comunicacaoInternaVO.setTipoDestinatario(inscricaoVO.getCandidato().getTipoPessoa());
				comunicacaoInternaVO.setTipoRemetente("FU");
				comunicacaoInternaVO.setAluno(inscricaoVO.getCandidato());
				comunicacaoInternaVO.setUnidadeEnsino(inscricaoVO.getCandidato().getUnidadeEnsinoVO());
	
				ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
				destinatario.setCiJaLida(Boolean.FALSE);
				destinatario.setDestinatario(inscricaoVO.getCandidato());
				destinatario.setEmail(inscricaoVO.getCandidato().getEmail());
				destinatario.setNome(inscricaoVO.getCandidato().getNome());
				destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
				comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(destinatario);
				if (meioAutenticacao.equals("E_MAIL")) {
					comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
				}
				comunicacaoInternaVO.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoInternaVO.setMensagem(realizarSubstituicaoTagsMensagemInscricao(inscricaoVO, destinatario, mensagemTemplate.getMensagem()));
				comunicacaoInternaVO.setMensagemSMS(realizarSubstituicaoTagsMensagemInscricao(inscricaoVO, destinatario, mensagemTemplate.getMensagemSMS()));
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				JobEnvioEmail jobEnvioEmail = new JobEnvioEmail();
				jobEnvioEmail.setIpServidor(config.getIpServidor());
				jobEnvioEmail.setSmtpPadrao(config.getSmptPadrao());
				jobEnvioEmail.setLoginServidorSmtp(config.getLogin());
				jobEnvioEmail.setEmailRemetente(config.getEmailRemetente());
				jobEnvioEmail.setSenhaServidorSmtp(config.getSenha());
				jobEnvioEmail.setPortaSmtpPadrao(config.getPortaSmtpPadrao());
				jobEnvioEmail.setListaAnexosExcluir(new ArrayList<String>());
				
				if (meioAutenticacao.equals("SMS")) {
					try {
						comunicacaoInternaVO.setEnviarSMS(true);
						SMSVO smsVO = new SMSVO();
						smsVO.setAssunto(mensagemTemplate.getAssunto());
						smsVO.setCelular(inscricaoVO.getCandidato().getCelular());
						smsVO.setEnviouSMS(false);
						smsVO.setMensagem(comunicacaoInternaVO.getMensagemSMS());
						listaSMS.add(smsVO);
						jobEnvioEmail.enviarSMSs(listaSMS, config);
						getFacadeFactory().getSmsFacade().incluir(smsVO);
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
				if (meioAutenticacao.equals("E_MAIL")) {
					try {
						if (inscricaoVO.getProcSeletivo().getUtilizarAutenticacaoEmail()) {
							EmailVO emailVO = new EmailVO();
							emailVO.setEmailDest(inscricaoVO.getCandidato().getEmail());
							if (emailVO.getEmailDest().contains("/")) {
								emailVO.setEmailDest(emailVO.getEmailDest().substring(0, emailVO.getEmailDest().indexOf("/")));
							} else if (emailVO.getEmailDest().contains(";")) {
								emailVO.setEmailDest(emailVO.getEmailDest().substring(0, emailVO.getEmailDest().indexOf(";")));
							}
							emailVO.setAssunto(mensagemTemplate.getAssunto());
							emailVO.setNomeDest(inscricaoVO.getCandidato().getNome());
							emailVO.setNomeRemet(config.getResponsavelPadraoComunicadoInterno().getNome());
							emailVO.setNovoObj(Boolean.FALSE);
							emailVO.setAnexarImagensPadrao(true);
							emailVO.setEmailRemet(config.getEmailRemetente());
							if (emailVO.getEmailRemet().equals("")) {
								emailVO.setEmailRemet("envio@sistema.com");
							}
							emailVO.setMensagem(comunicacaoInternaVO.getMensagem());
							listaEmail.add(emailVO);
							jobEnvioEmail.enviarEmails(listaEmail, config, new RegistroExecucaoJobVO(), new StringBuilder());
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarSubstituicaoTagsMensagemInscricao(InscricaoVO inscricaoVO, ComunicadoInternoDestinatarioVO destinatario, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), inscricaoVO.getCandidato().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), inscricaoVO.getCandidato().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), inscricaoVO.getCursoOpcao1().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), inscricaoVO.getUnidadeEnsino().getNome());
		if (Uteis.isAtributoPreenchido(inscricaoVO.getCodigoAutenticacao())) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_AUTENTICACAO.name(), inscricaoVO.getCodigoAutenticacao().toString());
		}
		return mensagemTexto;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO , Integer codigoAutenticacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from Inscricao insc ");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" and insc.codigo = ").append(inscricaoVO.getCodigo());
		sqlStr.append(" and insc.codigoAutenticacao = ").append(codigoAutenticacao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Código de Autenticação Inválido");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataAutenticacaoLiberacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO, Date data, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Inscricao set dataAutenticacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setDate(1, Uteis.getDataJDBC(data));
				sqlAlterar.setInt(2, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void aceitarTermosAceiteProvaProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception {
		final String sql = "UPDATE Inscricao set termofoiaceito=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setBoolean(1, true);
				sqlAlterar.setInt(2, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataHoraInicioProvaProcessoSeletivo(InscricaoVO inscricaoVO, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Inscricao set dataHoraInicio=? , navegadorAcesso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, inscricaoVO.getDataHoraInicio());
				sqlAlterar.setString(2, inscricaoVO.getNavegadorAcesso());
				sqlAlterar.setInt(3, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataHoraTerminoProvaProcessoSeletivo(InscricaoVO inscricaoVO ,UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Inscricao set dataHoraTermino=? , navegadorAcesso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, inscricaoVO.getDataHoraTermino());				
				sqlAlterar.setString(2, inscricaoVO.getNavegadorAcesso());				
				sqlAlterar.setInt(3, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public void validarApresentacaoResultadoProcessoSeletivo(InscricaoVO inscricaoVO) {
		if (inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo().equals(0) || inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AG") || inscricaoVO.getItemProcessoSeletivoDataProva().getDataLiberacaoResultado().compareTo(new Date()) > 0 || ( inscricaoVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) && inscricaoVO.getProcSeletivo().getRealizarProvaOnline() && ! inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AG") && !Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraTermino())))  {
			inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao("");
			inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao("");
			inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao("");
			inscricaoVO.getResultadoProcessoSeletivoVO().setNotaRedacao(null);
			inscricaoVO.getResultadoProcessoSeletivoVO().setMediaNotasProcSeletivo(null);
			inscricaoVO.getResultadoProcessoSeletivoVO().setMediaPonderadaNotasProcSeletivo(null);
			inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVOs().clear();
			for(QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : inscricaoVO.getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setAcertouQuestao(null);
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setErrouQuestao(null);
				for(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
					opcaoRespostaQuestaoProcessoSeletivoVO.setCorreta(null);
				}
			}
			inscricaoVO.setApresentarResultadoProcessoSeletivo(false);
		} else  {
			inscricaoVO.setApresentarResultadoProcessoSeletivo(true);
			for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : inscricaoVO.getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
				getFacadeFactory().getQuestaoProcessoSeletivoFacade().realizarCorrecaoQuestao(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo());
			}
		}
	}
	
	@Override
	public void validarPrazoRealizacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(inscricaoVO.getItemProcessoSeletivoDataProva().getDataLimiteApresentarDadosVisaoCandidato()) && inscricaoVO.getItemProcessoSeletivoDataProva().getDataLimiteApresentarDadosVisaoCandidato().compareTo(UteisData.getDataSemHora(new Date())) < 0) {
			throw new Exception("Prazo limite para apresentação de dados esgotado.");
		}		
	}
	
	@Override
	public void validarPrazoAlteracaoProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()) && inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva().compareTo(UteisData.getDataSemHora(new Date())) < 0) {
			throw new Exception("Prazo Alteração de dados esgotado.");
		}		
	}
	
	@Override
	public void validarPrazoRealizacaoProvaProcessoSeletivoOnline(InscricaoVO inscricaoVO, String tipoRequisicao,String mensagemErro) throws Exception {
		if(!inscricaoVO.getProcSeletivo().getRealizarProvaOnline() && !inscricaoVO.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.ON_LINE)) {
			
			if (tipoRequisicao.equals("PROVA")) {
				if (inscricaoVO.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()
						.equals(TipoAvaliacaoProcessoSeletivoEnum.PRESENCIAL)) {
					if(inscricaoVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) ) {
						mensagemErro = mensagemErro + " A sua inscrição está habilitada para PROVA PRESENCIAL, procure a instituição de ensino para a realização da mesma.";
						inscricaoVO.setMensagemErro(mensagemErro);
						throw new Exception(mensagemErro);
						
					}
					 mensagemErro  = mensagemErro +" Não é possível apresentar Prova do  processo Seletivo . Modalidade de  Ingresso "+inscricaoVO.getFormaIngresso_Apresentar()+".";
					 inscricaoVO.setMensagemErro(mensagemErro);
					 throw new Exception(mensagemErro);
				}
				if (inscricaoVO.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()
						.equals(TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR)) {
					mensagemErro = mensagemErro +" A sua inscrição está habilitada para AVALIAÇÃO CURRICULAR, procure a instituição de ensino para informações da mesma.";
					inscricaoVO.setMensagemErro(mensagemErro);
					throw new Exception(mensagemErro);
				}
			} 

		}
	}
	
	@Override
	public void validarDadosProvaProcessoSeletivo(InscricaoVO inscricaoVO, Boolean apresentarResultadoProcessoSeletivo) throws Exception {
		if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraTermino()) && !apresentarResultadoProcessoSeletivo) {
			throw new Exception("A sua prova já está encerrada");
		}
		if (Uteis.isAtributoPreenchido(UteisData.getDataComMinutos(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva())) && UteisData.getDataComMinutos(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()).compareTo(new Date()) > 0 ) {
			throw new Exception("A Prova Ainda Não Está Liberada Para Ser Realizada");
		}
		if (Uteis.isAtributoPreenchido(inscricaoVO.getProcSeletivo().getTermoAceiteProva()) && !inscricaoVO.getTermoFoiAceito()) {
			throw new Exception("É Necessário Aceitar os Termos Para Realizar Essa Prova");
		}
		if ((inscricaoVO.getProcSeletivo().getUtilizarAutenticacaoEmail() || inscricaoVO.getProcSeletivo().getUtilizarAutenticacaoSMS()) && !Uteis.isAtributoPreenchido(inscricaoVO.getDataAutenticacao())) {
			throw new Exception("É Necessário Realizar a Autenticação para Iniciar a Prova");
		}
	}
	
	@Override
	public Date consultarDataHoraTerminoInscricao(Integer codigoInscricao) throws Exception {
		String sqlStr = "SELECT Inscricao.datahoratermino FROM Inscricao WHERE Inscricao.codigo = " + codigoInscricao + " limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		Date datahoratermino = new Date();
		while (tabelaResultado.next()) {
			if (tabelaResultado.getDate("dataHoraTermino") != null) {
				datahoratermino = Uteis.getDataJDBCTimestamp(tabelaResultado.getTimestamp("dataHoraTermino"));
			} else {
				datahoratermino = null;
			}
		}
		return datahoratermino;
	}	
	
	private void realizarValidacaoPermissaoPagamentoInscricao(InscricaoVO inscricao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(inscricao.getContaReceberVO()) && inscricao.getContaReceberVO().getSituacaoAReceber()) {
			getFacadeFactory().getContaReceberFacade().executarRegerarBoletoVencidoInscricaoCandidato(inscricao, configuracaoFinanceiro, usuario);
			if (inscricao.getContaReceberVO().getContaCorrenteVO().getBloquearEmitirBoletoSemRegistroRemessa()) {
				inscricao.getContaReceberVO().setBloquearPagamentoVisaoAluno(getFacadeFactory().getControleRemessaContaReceberFacade().verificaContaReceberRegistrada(inscricao.getContaReceberVO()));		
			}
			getFacadeFactory().getContaReceberFacade().verificaBloqueioEmissaoBoleto(inscricao.getContaReceberVO(), usuario);
			inscricao.getContaReceberVO().setPermiteRecebimentoCartaoCreditoOnline(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(configuracaoFinanceiro.getCodigo(), inscricao.getContaReceberVO().getValor(), "permitirecebimentocartaoonline"));
			inscricao.setPermitirRecebimentoCartaoCreditoOnline(inscricao.getContaReceberVO().getPermiteRecebimentoCartaoCreditoOnline());
			inscricao.setPermitirEmissaoBoleto(!inscricao.getContaReceberVO().getBloquearPagamentoVisaoAluno());
		}else {
			inscricao.getContaReceberVO().setBloquearPagamentoVisaoAluno(true);
			inscricao.getContaReceberVO().setPermiteRecebimentoCartaoCreditoOnline(false);
			inscricao.setPermitirRecebimentoCartaoCreditoOnline(false);
			inscricao.setPermitirEmissaoBoleto(false);
		}
	}
	
	@Override
	public void consultarInscricaoProvaProcessoSeletivoEmRealizacao() throws Exception {

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select inscricao.codigo, procseletivo.temporealizacaoprovaonline, itemprocseletivodataprova.dataprova from inscricao ");
		sqlStr.append("inner join procseletivo on inscricao.procseletivo = procseletivo.codigo ");
		sqlStr.append("inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova  ");
		sqlStr.append("where datahorainicio is not null and datahoratermino is null");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		while (rs.next()) {			
			Date prazoProva = UteisData.getDataFuturaAvancandoMinuto(rs.getDate("dataprova"), rs.getInt("temporealizacaoprovaonline"));
			JobProvaProcessoSeletivo jobProva =  getAplicacaoControle().getJobProvaProcessoSeletivoPorInscricao(rs.getInt("codigo"));			
			if(jobProva == null) {
				 jobProva = new JobProvaProcessoSeletivo(rs.getInt("codigo"), prazoProva.getTime());
				 Thread threadProvaProcessoSeletivo = new Thread(jobProva);
				 threadProvaProcessoSeletivo.start();
				 getAplicacaoControle().adicionarJobProvaProcessoSeletivo(rs.getInt("codigo"), jobProva);
			}else {					
				jobProva.setTempoLimite(prazoProva.getTime());	
			}		
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<EmailVO> enviarCodigosAutenticacaoProvaOnlineDiaAtual(boolean controlarAcesso) throws Exception {
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(config.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
		List<EmailVO> listaEmail = new ArrayList<EmailVO>();
		List<SMSVO> listaSMS = new ArrayList<SMSVO>();
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_CODIGO_AUTENTICACAO, false, usuario);
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append(" and procSeletivo.realizarProvaOnline = true");
		sqlStr.append(" and itemProcessoSeletivoDataProva.tipoProvaGabarito = 'PR'");
		sqlStr.append(" and itemProcessoSeletivoDataProva.dataprova::date = now()::date");
		sqlStr.append(" and inscricao.situacao = 'CO'");
		sqlStr.append(" and inscricao.situacaoinscricao = 'ATIVO'");
		sqlStr.append(" and inscricao.datahoratermino is null");
		sqlStr.append(" and (procSeletivo.utilizarautenticacaosms = true or procSeletivo.utilizarautenticacaoemail = true)");
		sqlStr.append(" ORDER BY Inscricao.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		while (tabelaResultado.next()) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			inscricaoVO.setCodigo(tabelaResultado.getInt("codigo"));
			montarDadosCompleto(inscricaoVO, tabelaResultado, usuario);
			if (!Uteis.isAtributoPreenchido(inscricaoVO.getCodigoAutenticacao())) {
				Random rnd = new Random();
				Integer codigoAutenticacao = 100000 + rnd.nextInt(900000);
				enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, codigoAutenticacao, null, config, controlarAcesso,false , true, usuario);
				inscricaoVO.setCodigoAutenticacao(codigoAutenticacao);
			}
			ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();			
			ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
			destinatario.setCiJaLida(Boolean.FALSE);
			destinatario.setDestinatario(inscricaoVO.getCandidato());
			destinatario.setEmail(inscricaoVO.getCandidato().getEmail());
			destinatario.setNome(inscricaoVO.getCandidato().getNome());
			destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			comunicacaoInternaVO.setMensagem(realizarSubstituicaoTagsMensagemInscricao(inscricaoVO, destinatario, mensagemTemplate.getMensagem()));
			comunicacaoInternaVO.setMensagemSMS(realizarSubstituicaoTagsMensagemInscricao(inscricaoVO, destinatario, mensagemTemplate.getMensagemSMS()));
			
			if (inscricaoVO.getProcSeletivo().getUtilizarAutenticacaoEmail()) {
				EmailVO emailVO = new EmailVO();
				emailVO.setEmailDest(inscricaoVO.getCandidato().getEmail());
				if (emailVO.getEmailDest().contains("/")) {
					emailVO.setEmailDest(emailVO.getEmailDest().substring(0, emailVO.getEmailDest().indexOf("/")));
				} else if (emailVO.getEmailDest().contains(";")) {
					emailVO.setEmailDest(emailVO.getEmailDest().substring(0, emailVO.getEmailDest().indexOf(";")));
				}
				emailVO.setAssunto(mensagemTemplate.getAssunto());
				emailVO.setNomeDest(inscricaoVO.getCandidato().getNome());
				emailVO.setNomeRemet(config.getResponsavelPadraoComunicadoInterno().getNome());
				emailVO.setNovoObj(Boolean.FALSE);
				emailVO.setAnexarImagensPadrao(true);
				emailVO.setEmailRemet(config.getEmailRemetente());
				if (emailVO.getEmailRemet().equals("")) {
					emailVO.setEmailRemet("envio@sistema.com");
				}
				emailVO.setMensagem(comunicacaoInternaVO.getMensagem());
				listaEmail.add(emailVO);
			}
			if (inscricaoVO.getProcSeletivo().getUtilizarAutenticacaoSMS()) {
				SMSVO smsVO = new SMSVO();
				smsVO.setAssunto(mensagemTemplate.getAssunto());
				smsVO.setCelular(inscricaoVO.getCandidato().getCelular());
				smsVO.setEnviouSMS(false);
				smsVO.setMensagem(comunicacaoInternaVO.getMensagemSMS());
				listaSMS.add(smsVO);
			}
		}
		JobEnvioEmail jobEnvioEmail = new JobEnvioEmail();
		jobEnvioEmail.setIpServidor(config.getIpServidor());
		jobEnvioEmail.setSmtpPadrao(config.getSmptPadrao());
		jobEnvioEmail.setLoginServidorSmtp(config.getLogin());
		jobEnvioEmail.setEmailRemetente(config.getEmailRemetente());
		jobEnvioEmail.setSenhaServidorSmtp(config.getSenha());
		jobEnvioEmail.setPortaSmtpPadrao(config.getPortaSmtpPadrao());
		jobEnvioEmail.setListaAnexosExcluir(new ArrayList<String>());
		jobEnvioEmail.enviarEmails(listaEmail, config, new RegistroExecucaoJobVO(), new StringBuilder());
		jobEnvioEmail.enviarSMSs(listaSMS, config);
		return listaEmail;
	}
	
	private void montarDadosContaReceber(InscricaoVO inscricaoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(inscricaoVO.getContaReceber())) {
			if(getFacadeFactory().getContaReceberFacade().consultarExistenciaContaReceber(inscricaoVO.getContaReceber())) {
			inscricaoVO.setContaReceberVO((getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(inscricaoVO.getContaReceber(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO)));
			}
			
		}
		realizarValidacaoPermissaoPagamentoInscricao(inscricaoVO, configuracaoFinanceiroVO, usuarioVO) ;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoAutenticacaoNavegador(InscricaoVO inscricaoVO ,UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Inscricao set codigoAutenticacaoNavegador=? , navegadorAcesso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, inscricaoVO.getCodigoAutenticacaoNavegador());
				sqlAlterar.setString(2, inscricaoVO.getNavegadorAcesso());				
				sqlAlterar.setInt(3, inscricaoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InscricaoVO validarSessaoNavegadorProvaOnline(InscricaoVO inscricaoVO , String codigoautenticacaoNavegadorValidar ,  String codigoautenticacaoNavegadorAtualizar , Boolean validarSessao , UsuarioVO usuarioVO) throws Exception {
        if(validarSessao) {       
	        	if(inscricaoVO.getCodigoAutenticacaoNavegador().equals(codigoautenticacaoNavegadorValidar)) {
	        		inscricaoVO.setPermiteAcessarNavegador(true);
	        		inscricaoVO.setCodigoAutenticacaoNavegador(codigoautenticacaoNavegadorAtualizar);				
					getFacadeFactory().getInscricaoFacade().alterarCodigoAutenticacaoNavegador(inscricaoVO,usuarioVO);
	        	}else {
	        		inscricaoVO.setPermiteAcessarNavegador(false);
					throw new Exception("Está página foi invalidada por outro acesso, para que possa continuar a prova aqui entre novamente com seus dados.");
	        	}				
			}else {		
				if(codigoautenticacaoNavegadorValidar.equals("0")) {
					 codigoautenticacaoNavegadorValidar = "";
				}
				if(inscricaoVO.getCodigoAutenticacaoNavegador().equals(codigoautenticacaoNavegadorValidar) ) {					
	        		inscricaoVO.setPermiteAcessarNavegador(true);
			     }else {
			    	 inscricaoVO.setPermiteAcessarNavegador(false);
			     }        	
        }	
		return inscricaoVO;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InscricaoVO validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(Integer codigoInscricao , String codigoautenticacaoNavegadorValidar ,  String navegador , Boolean somenteGravarSessao , String action, UsuarioVO usuarioVO) throws Exception {
		   InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);	
		   if ((inscricaoVO.getCodigoAutenticacaoNavegador().equals(codigoautenticacaoNavegadorValidar) &&  inscricaoVO.getNavegadorAcesso().equals(navegador) ) ||  (action.equals("MATRICULA") && somenteGravarSessao) ) {
			   Random rnd = new Random();
			   String novoCodigoAutenticacaoGerado ="" + (100000 + rnd.nextInt(900000));
			   inscricaoVO.setPermiteAcessarNavegador(true);		  
			   inscricaoVO.setCodigoAutenticacaoNavegador(novoCodigoAutenticacaoGerado);
			   inscricaoVO.setNavegadorAcesso(navegador);	
			   return inscricaoVO;
		   } else {			  			   
			   throw new Exception("Esta página foi invalidada por outro acesso, para que possa continuar entre novamente com seus dados. A pagina será redirecionada a tela inicial.");
		   }	
	}
	
	
	public static void montarDadosResultadoProcessoSeletivo(InscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCodigo().intValue() == 0) {
			obj.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			return;
		}
		obj.setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(obj.getCodigo(),false, nivelMontarDados, usuario));
	}

	private void getSQLPadraoConsultarInscricao(Integer processoSeletivo, Integer unidadeEnsino, String numeroInscricao,
			String nomeCandidato, Date dataInicioInscricao, Date dataFimInscricao, Date dataInicioProva,
			Date dataFimProva, Boolean filtroSituacaoInscricaoConsultaAtivo,
			Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao,
			Boolean filtroSituacaoInscricaoConsultaNaoCompareceu, Boolean filtroSituacaoInscricaoConsultaCancelado,
			StringBuilder sqlStr, List<Object> parametros) {
		sqlStr.append(" FROM inscricao ");		
		sqlStr.append(" INNER JOIN procSeletivo on inscricao.procSeletivo = procSeletivo.codigo");
		sqlStr.append(" INNER JOIN pessoa on inscricao.candidato = pessoa.codigo");
		sqlStr.append(" INNER JOIN itemprocseletivodataprova on itemprocseletivodataprova.procseletivo = procseletivo.codigo");
		sqlStr.append(" AND itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sqlStr.append(" WHERE 1=1 ");
		if(Uteis.isAtributoPreenchido(processoSeletivo)) {
			sqlStr.append(" AND inscricao.procSeletivo = ").append(processoSeletivo.intValue());
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND inscricao.unidadeensino = ").append(unidadeEnsino.intValue());
		}
		if(Uteis.isAtributoPreenchido(numeroInscricao)) {
			sqlStr.append(" AND inscricao.codigo = ").append(numeroInscricao);
		}
		if(Uteis.isAtributoPreenchido(nomeCandidato)) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE (sem_acentos(?))");
			parametros.add(nomeCandidato + PERCENT);
		}
		if(Uteis.isAtributoPreenchido(dataInicioInscricao)) {
			sqlStr.append(" AND itemprocseletivodataprova.dataInicioInscricao::date >= '").append(Uteis.getDataJDBC(dataInicioInscricao)).append("'");			
		}
		if(Uteis.isAtributoPreenchido(dataFimInscricao)) {
			sqlStr.append(" AND itemprocseletivodataprova.dataTerminoInscricao::date <= '").append(Uteis.getDataJDBC(dataFimInscricao)).append("'");			
		}		
		if(Uteis.isAtributoPreenchido(dataInicioProva)) {
			sqlStr.append(" AND itemprocseletivodataprova.dataProva::date >= '").append(Uteis.getDataJDBC(dataInicioProva)).append("'");			
		}
		if(Uteis.isAtributoPreenchido(dataFimProva)) {
			sqlStr.append(" AND itemprocseletivodataprova.dataProva::date <= '").append(Uteis.getDataJDBC(dataFimProva)).append("'");			
		}
		if(filtroSituacaoInscricaoConsultaAtivo || filtroSituacaoInscricaoConsultaCancelouOutraInscricao || filtroSituacaoInscricaoConsultaNaoCompareceu || filtroSituacaoInscricaoConsultaCancelado) {
			boolean virgula = false;
			sqlStr.append(" AND situacaoInscricao IN(");
			if (filtroSituacaoInscricaoConsultaAtivo) {
				sqlStr.append(virgula ?  ", " : "" ).append(" 'ATIVO' ");
				virgula = true;
			}
			if (filtroSituacaoInscricaoConsultaCancelouOutraInscricao) {
				sqlStr.append(virgula ?  ", " : "" ).append(" 'CANCELADO_OUTRA_INSCRICAO' ");
				virgula = true;
			}
			if (filtroSituacaoInscricaoConsultaNaoCompareceu) {
				sqlStr.append(virgula ?  ", " : "" ).append(" 'NAO_COMPARECEU' ");
				virgula = true;
			}
			if (filtroSituacaoInscricaoConsultaCancelado) {
				sqlStr.append(virgula ?  ", " : "" ).append(" 'CANCELADO' ");
				virgula = true;
			}
			sqlStr.append(") ");
		}
	}

	@Override
	public void validarInscricaoAptoApresentarProvaProcessoSeletivoResultadoProcessoSeletivo(InscricaoVO inscricaoVO ,String tipoRequisicao) throws Exception {
		
		String mensagemErro  = "Olá  "+inscricaoVO.getCandidato().getNome() + "  localizamos a sua inscrição de número "+inscricaoVO.getCodigo()+" para o curso "+inscricaoVO.getCursoOpcao1().getNomeCursoTurno() +" na unidade "+inscricaoVO.getUnidadeEnsino().getNome() +".";
		if (inscricaoVO.getCodigo().equals(0) ) {
			throw new Exception("Inscrição Não Localizada.");
		}
		if (inscricaoVO.getSituacao().equals("PF")) {			
			throw new Exception("A Inscrição Não Está Confirmada Para Realização da Prova Pois Possui Pendência Financeira.");
		}
		if(!inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)){		
		    mensagemErro  = mensagemErro +" Não é possível apresentar "+tipoRequisicao.toLowerCase()+"  do processo Seletivo. inscrição com situação "+inscricaoVO.getSituacaoInscricao().getValorApresentar() +".";
			inscricaoVO.setMensagemErro(mensagemErro);
			throw new Exception("Não é possível apresentar "+tipoRequisicao.toLowerCase()+" do  processo Seletivo . inscrição com situação "+inscricaoVO.getSituacaoInscricao().getValorApresentar() +".");	
		}
		if(tipoRequisicao.equals("PROVA") || tipoRequisicao.equals("PROVA_APLICADA")) {
			validarPrazoRealizacaoProvaProcessoSeletivoOnline(inscricaoVO,tipoRequisicao,mensagemErro);		
			if(!inscricaoVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) || !inscricaoVO.getProcSeletivo().getRealizarProvaOnline()) {
				 mensagemErro  = mensagemErro +" Não é possível apresentar Prova do  processo Seletivo. Modalidade de  Ingresso "+inscricaoVO.getFormaIngresso_Apresentar()+".";
				 inscricaoVO.setMensagemErro(mensagemErro);
				 throw new Exception(mensagemErro);
			}
			if (Uteis.isAtributoPreenchido(UteisData.getDataComMinutos(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva())) && UteisData.getDataComMinutos(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()).compareTo(new Date()) > 0 ) {
				 mensagemErro  = mensagemErro +" A Prova será Liberada Para Ser Realizada as  "+inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva_Apresentar()+"";
				 inscricaoVO.setMensagemErro(mensagemErro);
				 throw new Exception(mensagemErro);
			}			
			if(!inscricaoVO.getProcSeletivo().getRealizarProvaOnline() && !inscricaoVO.getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("PR")) {
			    mensagemErro  = "Candidato Não Está Apto a Realizar Essa Prova";
			    inscricaoVO.setMensagemErro(mensagemErro);
			    throw new Exception(mensagemErro);
			}
			if(tipoRequisicao.equals("PROVA_APLICADA") || inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AG")) {
				if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraTermino())) {
					 mensagemErro  = mensagemErro +" Não é possível realizar a prova para este  processo Seletivo. A sua prova já se encontra encerrada.";
					inscricaoVO.setMensagemErro(mensagemErro);
					 throw new Exception(mensagemErro);
				}
				Date prazoProva = UteisData.getDataFuturaAvancandoMinuto(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva(), inscricaoVO.getProcSeletivo().getTempoRealizacaoProvaOnline());
				if(UteisData.diferencaEmSegundosEntreDatas(new Date(), prazoProva) <= 0L) {
					 mensagemErro  = "Não é possível apresentar a prova. Tempo de realização de prova esgotada";
					 inscricaoVO.setMensagemErro(mensagemErro);
					 throw new Exception(mensagemErro);
				}
				
			}
		}
		if(tipoRequisicao.equals("RESULTADO")) {		
		   if(Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraInicio()) && !Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraTermino()) && inscricaoVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) && inscricaoVO.getProcSeletivo().getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.ON_LINE)) {				
			   mensagemErro  = mensagemErro +"  O resultado de sua prova ainda não está disponível. Finalize sua prova e aguarde a liberação do resultado.";
			   inscricaoVO.setMensagemErro(mensagemErro);
			   throw new Exception(inscricaoVO.getMensagemErro());  
		   }		   
		   
		   if(inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AG") && inscricaoVO.getItemProcessoSeletivoDataProva().getDataLiberacaoResultado().compareTo(new Date()) < 0) {
			   inscricaoVO.setMensagemErro(mensagemErro +" Não é possível apresentar o resultado do processo Seletivo. O resultado está sendo processsado  Procure a instituição de ensino para informações da mesma.");
		   }else if(inscricaoVO.getItemProcessoSeletivoDataProva().getDataLiberacaoResultado().compareTo(new Date()) > 0 ){
			   inscricaoVO.setMensagemErro(mensagemErro+ " Não é possível apresentar o resultado do processo Seletivo. O resultado está previsto para ser liberado dia "+inscricaoVO.getItemProcessoSeletivoDataProva().getDataLiberacaoResultado_Apresentar() +".Procure a instituição de ensino para informações da mesma.");
		   }else if(inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {			  
	    	   inscricaoVO.setMensagemErro(mensagemErro+" O resultado de sua prova ainda não está disponível. Procure a instituição de ensino para informações da mesma.");
	    	   
	       }
		   validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
		}
		validarApresentacaoResultadoProcessoSeletivo(inscricaoVO);
		if(tipoRequisicao.equals("RESULTADO") && !inscricaoVO.getApresentarResultadoProcessoSeletivo()) {
			if(Uteis.isAtributoPreenchido(inscricaoVO.getMensagemErro())) {
				throw new Exception(inscricaoVO.getMensagemErro());
			}
			 throw new Exception("Não é possível apresentar o resultado do processo Seletivo. Procure a instituição de ensino para informações da mesma.");
		}
		validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);	
		
		
	}
	
	
	
	@Override
	public InscricaoVO consultarUltimaInscricaoAtivaDentroPrazoDiferenteAtualSemResultadoPorPessoa(Integer codigoPessoa ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);		
		if(  Uteis.isAtributoPreenchido(codigoPessoa)  &&  codigoPessoa > 0  ) {
			StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
			sqlStr.append(" WHERE  Inscricao.candidato = " + codigoPessoa + " ");	
			sqlStr.append(" and Inscricao.situacaoinscricao = 'ATIVO' ");
			sqlStr.append(" and not exists (select resultadoprocessoseletivo.inscricao from resultadoprocessoseletivo where resultadoprocessoseletivo.inscricao = Inscricao.codigo ) ");
			sqlStr.append(" and (itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCandidato >= current_date or    itemProcessoSeletivoDataProva.dataLimiteApresentarDadosVisaoCandidato is null) ");
			sqlStr.append(" ORDER BY Inscricao.codigo desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				InscricaoVO obj = new InscricaoVO();
				obj.setCodigo(tabelaResultado.getInt("codigo"));
				montarDadosCompleto(obj, tabelaResultado, usuario);					
				montarDadosContaReceber(obj, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), usuario);
				return obj;
			}
		}		
		return new InscricaoVO();
	}
	
	
	public List<InscricaoVO> montarDadosComprovanteConsultaCompleta(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		while (dadosSQL.next()) {
			InscricaoVO obj = new InscricaoVO();
			
			// DADOS PROCESSO SELETIVO
			obj.getProcSeletivo().setCodigo(dadosSQL.getInt("ProcSeletivo.codigo"));
			obj.getProcSeletivo().setDescricao(dadosSQL.getString("ProcSeletivo.descricao"));
			
			// DADOS INSCRICAO
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setSituacao(dadosSQL.getString("situacao"));
			obj.setFormaIngresso(dadosSQL.getString("formaIngresso"));	
			obj.setData(dadosSQL.getDate("data")); 
			
			// DADOS PROVA
			obj.getItemProcessoSeletivoDataProva().setCodigo(dadosSQL.getInt("itemProcessoSeletivoDataProva.codigo"));
			obj.getItemProcessoSeletivoDataProva().setDataProva(dadosSQL.getDate("itemProcessoSeletivoDataProva.dataProva"));					
			
			// DADOS CANDIDATO
			obj.getCandidato().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
			obj.getCandidato().setNome(dadosSQL.getString("Pessoa.nome")); 
			obj.getCandidato().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
			obj.getCandidato().setSexo(dadosSQL.getString("Pessoa.sexo")); 		
			obj.getCandidato().setTelefoneRes(dadosSQL.getString("Pessoa.telefoneRes")); 	
			obj.getCandidato().setTelefoneComer(dadosSQL.getString("Pessoa.telefoneComer")); 
			obj.getCandidato().setCelular(dadosSQL.getString("Pessoa.celular"));
			obj.getCandidato().setEmail(dadosSQL.getString("Pessoa.email"));
			obj.getCandidato().setCPF(dadosSQL.getString("Pessoa.cpf"));
			obj.getCandidato().setRG(dadosSQL.getString("Pessoa.rg")); 
			obj.getCandidato().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
			obj.getCandidato().setDataEmissaoRG(dadosSQL.getDate("Pessoa.dataemissaorg")); 
			obj.getCandidato().setNecessidadesEspeciais(dadosSQL.getString("Pessoa.necessidadesEspeciais"));
			obj.getCandidato().setGravida(dadosSQL.getBoolean("pessoa.gravida")); 
			obj.getCandidato().setCanhoto(dadosSQL.getBoolean("pessoa.canhoto")); 
			obj.getCandidato().setPortadorNecessidadeEspecial(dadosSQL.getBoolean("pessoa.portadorNecessidadeEspecial")); 
			obj.getCandidato().setEndereco(dadosSQL.getString("Pessoa.endereco")); 
			obj.getCandidato().setNumero(dadosSQL.getString("Pessoa.numero")); 
			obj.getCandidato().setComplemento(dadosSQL.getString("Pessoa.complemento")); 
			obj.getCandidato().setSetor(dadosSQL.getString("Pessoa.setor"));
			obj.getCandidato().getCidade().setCodigo(dadosSQL.getInt("Cidade.codigo"));
			obj.getCandidato().getCidade().setNome(dadosSQL.getString("Cidade.nome")); 		
			obj.getCandidato().setCEP(dadosSQL.getString("Pessoa.cep"));			
			if(Uteis.isAtributoPreenchido(dadosSQL.getInt("Cidade.estado"))) {
				obj.getCandidato().getCidade().getEstado().setCodigo(dadosSQL.getInt("Cidade.estado")); 
				obj.getCandidato().getCidade().setEstado(getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(obj.getCandidato().getCidade().getEstado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			
			// DADOS SALA
			obj.getSala().setCodigo(dadosSQL.getInt("sala_codigo"));
			obj.getSala().setSala(dadosSQL.getString("sala_sala"));
			obj.getSala().getLocalAula().setCodigo(dadosSQL.getInt("LocalAula_codigo"));
			obj.getSala().getLocalAula().setLocal(dadosSQL.getString("LocalAula_local")); 
			obj.getSala().getLocalAula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("LocalAula_unidadeEnsino"));
			obj.getSala().getLocalAula().setEndereco(dadosSQL.getString("LocalAula_endereco"));	
			
			// DADOS CURSO
			obj.getCursoOpcao1().setCodigo(dadosSQL.getInt("cursoOpcao1"));
			montarDadosCursoOpcao1(obj, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);																						
			// DADOS UNIDADE ENSINO
			obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("Unidadeensino.codigo")); 
			obj.getUnidadeEnsino().setNome(dadosSQL.getString("Unidadeensino.nome")); 
			
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Override
	public List<InscricaoVO> consultarInscricoesPessoa(Integer codigoPessoa, Integer codigoInscricao ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<InscricaoVO> listInscricaoVOs = new ArrayList<InscricaoVO>();
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		if(Uteis.isAtributoPreenchido(codigoInscricao) && codigoInscricao > 0 ) {
			sqlStr.append(" WHERE (Inscricao.codigo = " + codigoInscricao + ") ");
		}else {
			sqlStr.append(" WHERE (Inscricao.candidato = " + codigoPessoa + ") ");
		}		
		sqlStr.append(" ORDER BY Inscricao.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			montarDadosCompleto(obj, tabelaResultado, usuario);					
			montarDadosContaReceber(obj, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), usuario);			
			obj.getProcSeletivo().setValorInscricaoApresentar(new BigDecimal(obj.getProcSeletivo().getValorInscricao()));
			listInscricaoVOs.add(obj);
		}
		return listInscricaoVOs;
	}

	@Override
	public List<InscricaoVO> consultarInscricaoPessoaProvaOnlinePorNumeroDocumento(String tipoDocumento, String numeroDocumento,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<InscricaoVO> listInscricaoVOs = new ArrayList<InscricaoVO>();
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		if(tipoDocumento.equals("CPF") && (Uteis.isAtributoPreenchido(numeroDocumento) ||  numeroDocumento != null )) {	
			   sqlStr.append(" WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) = ").append("(replace(replace(('").append(numeroDocumento).append("'),'.',''),'-',''))");
		}else if(tipoDocumento.equals("CODIGO_INSCRICAO") && (Uteis.isAtributoPreenchido(numeroDocumento) ||  numeroDocumento != null )) {	
			   sqlStr.append(" WHERE inscricao.codigo = ").append(Integer.parseInt(numeroDocumento));
		}
		sqlStr.append(" ORDER BY Inscricao.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			InscricaoVO obj = new InscricaoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			montarDadosCompleto(obj, tabelaResultado, usuario);
			montarDadosContaReceber(obj, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), usuario);
			listInscricaoVOs.add(obj);
		}
		return listInscricaoVOs;
	}
	@Override
	public InscricaoVO consultarPorProcSeletivoECPF(Integer procSeletivo, String cpf, Integer unidadeEnsino, Integer curso, Integer turno, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select insc.* from Inscricao insc ");
		sqlStr.append(" inner join pessoa p on p.codigo = insc.candidato ");
		sqlStr.append(" inner join unidadeEnsinoCurso AS curso1 on curso1.codigo = insc.cursoopcao1 ");
		sqlStr.append(" left join unidadeEnsinoCurso AS curso2 on curso2.codigo = insc.cursoopcao2 ");
		sqlStr.append(" left join unidadeEnsinoCurso AS curso3 on curso3.codigo = insc.cursoopcao3 ");
		sqlStr.append(" where 1=1  ");		
		sqlStr.append(" and insc.procSeletivo = ").append(procSeletivo);
		if(Uteis.isAtributoPreenchido(cpf)) {	
			sqlStr.append(" and (replace(replace((p.cpf),'.',''),'-','')) LIKE(?) ");
		}		
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and insc.unidadeEnsino =  ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(curso) && Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and ((curso1.curso =  ").append(curso).append(" and curso1.turno = ").append(turno).append(") ");
			sqlStr.append(" or (curso2.curso =  ").append(curso).append(" and curso2.turno = ").append(turno).append(") ");
			sqlStr.append(" or (curso2.curso =  ").append(curso).append(" and curso2.turno = ").append(turno).append(") ");
			sqlStr.append(" ) ");
		}else	if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and (curso1.curso =  ").append(curso);
			sqlStr.append(" or curso2.curso =  ").append(curso);
			sqlStr.append(" or curso2.curso =  ").append(curso);
			sqlStr.append(" ) ");
		}else	if(Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and (curso1.turno =  ").append(curso);
			sqlStr.append(" or curso2.turno =  ").append(curso);
			sqlStr.append(" or curso2.turno =  ").append(curso);
			sqlStr.append(" ) ");
		}	
		sqlStr.append(" order by insc.codigo desc  ");		

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(cpf) + PERCENT);
		if (!tabelaResultado.next()) {
			return new InscricaoVO();
		}
		return (montarDadosMinimos(tabelaResultado, nivelMontarDados, usuario));
	}
	@Override
	public InscricaoVO consultarInscricaoAptaMatricula(String tipoDocumento, String numeroDocumento ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuilder str = new StringBuilder();
		str.append("select * from (");
		str.append("select inscricao.*  ,");
		str.append("  case when resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' then inscricao.cursoopcao1");
		str.append("  when resultadoprocessoseletivo.resultadosegundaopcao = 'AP' then inscricao.cursoopcao2");
		str.append("  when resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' then inscricao.cursoopcao3");
		str.append(" end as cursoAprovado , ");
		str.append(" pchamada.periodoinicialchamada as periodoinicialchamada , ");
		str.append(" pchamada.periodofinalchamada as periodofinalchamada , ");
		str.append(" pchamada.periodoinicialuploaddocumentoindeferido as periodoinicialuploaddocumentoindeferido , ");
		str.append(" pchamada.periodofinaluploaddocumentoindeferido  as periodofinaluploaddocumentoindeferido  ");
		str.append(" from resultadoprocessoseletivo  ");
		str.append(" inner join inscricao on resultadoprocessoseletivo.inscricao = inscricao.codigo");
		str.append(" inner join pessoa on inscricao.candidato = pessoa.codigo");
		str.append(" inner join procseletivo on inscricao.procseletivo = procseletivo.codigo");
		str.append(" inner join periodochamadaprocseletivo pchamada on pchamada.procseletivo = procseletivo.codigo and  pchamada.nrchamada = inscricao.chamada  ");
		str.append(" inner join itemprocseletivodataprova on inscricao.itemprocessoseletivodataprova = itemprocseletivodataprova.codigo");
		str.append(" where (itemprocseletivodataprova.dataLimiteApresentarDadosVisaoCandidato >= current_date or itemprocseletivodataprova.dataLimiteApresentarDadosVisaoCandidato is null)");
		str.append(" and inscricao.situacaoinscricao = '").append(SituacaoInscricaoEnum.ATIVO.toString()).append("' ");
		if (Uteis.isAtributoPreenchido(tipoDocumento) && tipoDocumento.equals("CODIGO_INSCRICAO")) {
			str.append("and inscricao.codigo = ").append(numeroDocumento);
		}
		if (Uteis.isAtributoPreenchido(tipoDocumento) && tipoDocumento.equals("CPF")) {
			str.append("and replace(replace(CPF,'.',''),'-','') = '").append(Uteis.retirarMascaraCPF(numeroDocumento)).append("'");		
		}
		str.append(" and inscricao.candidatoConvocadoMatricula = true and coalesce(inscricao.chamada, 0) > 0 ) as resultadoProcesso ");		
		str.append(" where   ");		
		str.append(" (resultadoProcesso.periodoinicialchamada <= now()  and resultadoProcesso.periodofinalchamada >= now() ");		
		str.append("  and (  not exists (  select matricula.matricula from matricula ");
		str.append("  inner join unidadeensinocurso on unidadeensinocurso.curso = matricula.curso");
		str.append("  and unidadeensinocurso.unidadeensino = matricula.unidadeensino and unidadeensinocurso.turno = matricula.turno ");
		str.append(" where aluno = resultadoProcesso.candidato and unidadeensinocurso.codigo = cursoAprovado and matricula.situacao <> 'CA') ");
		str.append(getExistsMatriculaRealizadaPendenteDocumentacao());
		str.append(getExistsMatriculaRealizadaPendenteAssinaturaContrato()).append(" ) ) ");	
		
		str.append("  or ( resultadoProcesso.periodofinalchamada < now() ");
		str.append("  and resultadoProcesso.periodoinicialuploaddocumentoindeferido <= now() ");
		str.append("  and resultadoProcesso.periodofinaluploaddocumentoindeferido >= now() ");
		str.append(getExistsMatriculaRealizadaIndeferidoDocumentacao());		
		str.append("  )");
		str.append("  order by resultadoProcesso.codigo desc limit 1 ");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		InscricaoVO obj = new InscricaoVO();
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Prezado Candidato não encontramos nenhuma inscrição liberada para realização da matrícula, ou está fora do periodo de realização da matrícula.");
		}
		obj  = montarDados(tabelaResultado, nivelMontarDados, usuario);
		obj.setCursoAprovado(tabelaResultado.getInt("cursoAprovado"));		
		return obj;
	}
	
	private String getExistsMatriculaRealizadaPendenteDocumentacao() {
		StringBuilder str = new StringBuilder();
		str.append("   or exists (");
		str.append("  select matricula.matricula from matricula ");
		str.append("  inner join unidadeensinocurso on unidadeensinocurso.curso = matricula.curso");
		str.append("  and unidadeensinocurso.unidadeensino = matricula.unidadeensino and unidadeensinocurso.turno = matricula.turno ");
		str.append("  inner join documetacaomatricula on documetacaomatricula.matricula  = matricula.matricula ");
		str.append("  where aluno = resultadoProcesso.candidato and unidadeensinocurso.codigo = cursoAprovado and matricula.situacao <> 'CA' ");
		str.append(" and documetacaomatricula.matricula = matricula.matricula  and documetacaomatricula.entregue is false) ");
		return str.toString() ;
	}	
	
	private String getExistsMatriculaRealizadaIndeferidoDocumentacao() {
		StringBuilder str = new StringBuilder();
		str.append(" and exists (select matricula.matricula from	matricula ");
		str.append(" inner join unidadeensinocurso on unidadeensinocurso.curso = matricula.curso ");
		str.append(" and unidadeensinocurso.unidadeensino = matricula.unidadeensino and unidadeensinocurso.turno = matricula.turno ");
		str.append(" inner join documetacaomatricula on	documetacaomatricula.matricula = matricula.matricula ");
		str.append(" where aluno = resultadoProcesso.candidato  ");
		str.append(" and unidadeensinocurso.codigo = cursoAprovado  and matricula.situacao <> 'CA'");
		str.append(" and documetacaomatricula.matricula = matricula.matricula  and documetacaomatricula.entregue is false ");
		str.append(" and documetacaomatricula.respnegardocdep is not null  ");
		str.append(" and documetacaomatricula.respnegardocdep != 0  and documetacaomatricula.datanegardocdep is not  null limit 1 ) ");
		return str.toString() ;
	}
	
	private String getExistsMatriculaRealizadaPendenteAssinaturaContrato() {
		StringBuilder str = new StringBuilder();
		str.append("  or exists (");
		str.append("  select matricula.matricula from matricula ");
		str.append("  inner join unidadeensinocurso on unidadeensinocurso.curso = matricula.curso");
		str.append("  and unidadeensinocurso.unidadeensino = matricula.unidadeensino and unidadeensinocurso.turno = matricula.turno ");
		str.append("  inner join matriculaperiodo mp on mp.matricula  = matricula.matricula ");
		str.append("  left join documentoassinado on  documentoassinado.matricula  = matricula.matricula and mp.codigo =  documentoassinado.matriculaperiodo and documentoassinado.tipoOrigemDocumentoAssinado =  'CONTRATO'  ");
		str.append("  left join documentoassinadopessoa on documentoassinadopessoa.documentoassinado = documentoassinado.codigo   ");
		str.append("  where aluno = resultadoProcesso.candidato and unidadeensinocurso.codigo = cursoAprovado and matricula.situacao <> 'CA'  ");
		str.append("   and ((documentoassinadopessoa.tipoPessoa = 'ALUNO' and documentoassinadopessoa.situacaodocumentoassinadopessoa ='PENDENTE') or documentoassinado.codigo is null) ");						
		str.append("  ) ");
		return str.toString() ;
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InscricaoVO inicializarDadosInscricaoImportacaoCandidato(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoVO procSeletivoVO, ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProvaVO, PessoaVO candidatoVO,  UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(procSeletivoVO.getCodigo())) {
			InscricaoVO inscricaoVO = consultarPorProcSeletivoECPF(procSeletivoVO.getCodigo(), importarCandidatoVO.getCPF(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), importarCandidatoVO.getCursoVO().getCodigo(), importarCandidatoVO.getTurnoVO().getCodigo(),  Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(inscricaoVO.getCodigo())) {
				inscricaoVO.setClassificacao(importarCandidatoVO.getClassificacao());
				
				if(Uteis.isAtributoPreenchido(importarCandidatoVO.getNumeroChamada()) 
				 && !inscricaoVO.getChamada().equals(importarCandidatoVO.getNumeroChamada())) {
					inscricaoVO.setChamada(importarCandidatoVO.getNumeroChamada());
					inscricaoVO.setAlterarNumeroChamada(Boolean.TRUE);
				}				
				if (Uteis.isAtributoPreenchido(SituacaoInscricaoEnum.valueOf(importarCandidatoVO.getSituacaoInscricao()))) {
					inscricaoVO.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(importarCandidatoVO.getSituacaoInscricao()));
				}
				inscricaoVO.setSobreBolsasEAuxilios(Uteis.isAtributoPreenchido(importarCandidatoVO.getSobreBolsasEAuxilios()) && importarCandidatoVO.getSobreBolsasEAuxilios().toUpperCase().equals("SIM"));
				inscricaoVO.setAutodeclaracaoPretoPardoOuIndigena(Uteis.isAtributoPreenchido(importarCandidatoVO.getAutodeclaracaoPretoPardoOuIndigena()) && importarCandidatoVO.getAutodeclaracaoPretoPardoOuIndigena().toUpperCase().equals("SIM"));
				inscricaoVO.setEscolaPublica(Uteis.isAtributoPreenchido(importarCandidatoVO.getEscolaPublica()) && importarCandidatoVO.getEscolaPublica().toUpperCase().equals("SIM"));
				inscricaoVO.setLiberadaForaPrazo(true);
				inscricaoVO.setInscricaoProvenienteImportacao(true);
				inscricaoVO.setCandidatoConvocadoMatricula(importarCandidatoVO.getResultadoProcessoSeletivo().trim().toUpperCase().equals("AP") && importarCandidatoVO.getNumeroChamada() > 0);
				return inscricaoVO;
			}
		}
		InscricaoVO inscricaoVO = new InscricaoVO();
		inscricaoVO.setCandidato(candidatoVO);
		inscricaoVO.setData(importarCandidatoVO.getDataInscricao());
		UnidadeEnsinoCursoVO cursoOpcao1 = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeTurno(importarCandidatoVO.getCursoVO().getCodigo(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), importarCandidatoVO.getTurnoVO().getCodigo(), usuarioVO);
		if (Uteis.isAtributoPreenchido(cursoOpcao1.getCodigo())) {
			inscricaoVO.setCursoOpcao1(cursoOpcao1);
		} else {
			UnidadeEnsinoCursoVO cursoOpcao = getFacadeFactory().getUnidadeEnsinoCursoFacade().inicializarDadosUnidadeEnsinoCursoImportarCandidatoProcSeletivo(importarCandidatoVO, usuarioVO);
			inscricaoVO.setCursoOpcao1(cursoOpcao);
		}
		if (!Uteis.isAtributoPreenchido(procSeletivoVO.getNivelEducacional())) {
			procSeletivoVO.setNivelEducacional(importarCandidatoVO.getCursoVO().getNivelEducacional());
		} else {
			if (!procSeletivoVO.getNivelEducacional().equals(importarCandidatoVO.getCursoVO().getNivelEducacional())) {
				
				throw new Exception("O Curso "+importarCandidatoVO.getCursoVO().getNome().toUpperCase()+", possui nível educacional diferente do Processo Seletivo: "+procSeletivoVO.getDescricao().toUpperCase()+". Favor rever dados contidos no Excel.");
			}
		}
		inscricaoVO.setProcSeletivo(procSeletivoVO);
		inscricaoVO.setFormaAcessoProcSeletivo("VE");
		inscricaoVO.setResponsavel(usuarioVO);
		inscricaoVO.setSituacao("CO");
		inscricaoVO.setUnidadeEnsino(importarCandidatoVO.getUnidadeEnsinoVO());
		inscricaoVO.setItemProcessoSeletivoDataProva(itemProcessoSeletivoDataProvaVO);
		if (Uteis.isAtributoPreenchido(SituacaoInscricaoEnum.valueOf(importarCandidatoVO.getSituacaoInscricao()))) {
			inscricaoVO.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(importarCandidatoVO.getSituacaoInscricao()));
		}
		inscricaoVO.setFormaIngresso(importarCandidatoVO.getFormaIngresso());
		inscricaoVO.setSobreBolsasEAuxilios(Uteis.isAtributoPreenchido(importarCandidatoVO.getSobreBolsasEAuxilios()) && importarCandidatoVO.getSobreBolsasEAuxilios().toUpperCase().equals("SIM"));
		inscricaoVO.setAutodeclaracaoPretoPardoOuIndigena(Uteis.isAtributoPreenchido(importarCandidatoVO.getAutodeclaracaoPretoPardoOuIndigena()) && importarCandidatoVO.getAutodeclaracaoPretoPardoOuIndigena().toUpperCase().equals("SIM"));
		inscricaoVO.setEscolaPublica(Uteis.isAtributoPreenchido(importarCandidatoVO.getEscolaPublica()) && importarCandidatoVO.getEscolaPublica().toUpperCase().equals("SIM"));
		inscricaoVO.setClassificacao(importarCandidatoVO.getClassificacao());
		inscricaoVO.setChamada(importarCandidatoVO.getNumeroChamada());		
		inscricaoVO.setLiberadaForaPrazo(true);
		inscricaoVO.setInscricaoProvenienteImportacao(true);
		inscricaoVO.setCandidatoConvocadoMatricula(importarCandidatoVO.getResultadoProcessoSeletivo().trim().toUpperCase().equals("AP") && importarCandidatoVO.getNumeroChamada() > 0);
		return inscricaoVO;
	}
	
	
	@Override
	public void validarEmailTelefoneCandidato(InscricaoVO inscricaoVO , String meioAutenticacao , String valorValidarCandidato) throws Exception{
		if (meioAutenticacao != null 
				&& ((meioAutenticacao.equals("SMS") && !inscricaoVO.getCandidato().getCelular().equals(valorValidarCandidato)) 
					|| (meioAutenticacao.equals("E_MAIL") && !inscricaoVO.getCandidato().getEmail().toLowerCase().equals(valorValidarCandidato.toLowerCase())))) {
			throw new ConsistirException("Valor Informado esta diferente do cadastrado");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarNumeroChamadaInscricao(InscricaoVO inscricaoVO , UsuarioVO usuario) throws Exception {
		final StringBuilder sql = new StringBuilder();		
		sql.append("UPDATE inscricao set chamada=?  WHERE chamada <> ? and codigo = ? and not exists (select matricula.matricula from matricula where inscricao = codigo  )");
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setInt(1, inscricaoVO.getChamada());
				sqlAlterar.setInt(2, inscricaoVO.getChamada());
				sqlAlterar.setInt(3, inscricaoVO.getCodigo());				
				return sqlAlterar;
			}
		});		
	}
	
	
	public static InscricaoVO montarDadosMinimos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		InscricaoVO obj = new InscricaoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getCandidato().setCodigo(new Integer(dadosSQL.getInt("candidato")));
		obj.getProcSeletivo().setCodigo(new Integer(dadosSQL.getInt("procSeletivo")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setSituacaoInscricao(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.setSituacaoInscricaoOrigem(SituacaoInscricaoEnum.valueOf(dadosSQL.getString("situacaoInscricao")));
		obj.setContaReceber(new Integer(dadosSQL.getInt("contaReceber")));
		obj.getOpcaoLinguaEstrangeira().setCodigo(new Integer(dadosSQL.getInt("opcaoLinguaEstrangeira")));
		obj.getItemProcessoSeletivoDataProva().setCodigo(new Integer(dadosSQL.getInt("itemProcessoSeletivoDataProva")));
        obj.setClassificacao(dadosSQL.getInt("classificacao"));
        obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigoFinanceiroMatricula"));
        obj.setDescDisciplinasProcSeletivo(dadosSQL.getString("descDisciplinasProcSeletivo"));
        obj.setCodigoAutenticacaoNavegador(dadosSQL.getString("codigoAutenticacaoNavegador"));  
        obj.setNavegadorAcesso(dadosSQL.getString("navegadorAcesso"));
        obj.setSobreBolsasEAuxilios(dadosSQL.getBoolean("sobreBolsasEAuxilios"));
		obj.setAutodeclaracaoPretoPardoOuIndigena(dadosSQL.getBoolean("autodeclaracaoPretoPardoOuIndigena"));
		obj.setEscolaPublica(dadosSQL.getBoolean("escolaPublica"));
		obj.setChamada(dadosSQL.getInt("chamada"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosCandidato(obj, nivelMontarDados, usuario);
			montarDadosProcSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (obj.getItemProcessoSeletivoDataProva().getCodigo() > 0) {
				obj.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			return obj;
		}		
		return obj;
	}

}