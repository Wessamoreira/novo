package controle.faturamento.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.faturamento.nfe.CartaCorrecaoVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoIntegracaoNfeEnum;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.Assinador;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.faturamento.nfe.NotaFiscalSaida;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.faturamento.nfe.NotaFiscalSaidaRelControle;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import webservice.nfse.WebServicesNFSEEnum;

@Controller("NotaFiscalSaidaControle")
@Scope("viewScope")
@Lazy
public class NotaFiscalSaidaControle extends SuperControle implements Serializable {

	private List<NotaFiscalSaidaVO> notaFiscalSaidaVOs;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Date dataInicio;
	private Date dataFim;
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private TurmaVO turmaVO;
	private List listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private MatriculaVO matriculaVO;
	private Boolean tipoOrigemContaReceberInscricaoProcessoSeletivo;
	private Boolean tipoOrigemContaReceberMatricula;
	private Boolean tipoOrigemContaReceberRequerimento;
	private Boolean tipoOrigemContaReceberBiblioteca;
	private Boolean tipoOrigemContaReceberMensalidade;
	private Boolean tipoOrigemContaReceberDevolucaoCheque;
	private Boolean tipoOrigemContaReceberNegociacao;
	private Boolean tipoOrigemContaReceberBolsaCusteadaConvenio;
	private Boolean tipoOrigemContaReceberContratoReceita;
	private Boolean tipoOrigemContaReceberInclusaoReposicao;
	private Boolean tipoOrigemContaReceberOutros;
	private Boolean tipoOrigemContaReceberTodos;
	private String situacaoContaReceber;
	private NotaFiscalSaidaVO notaFiscalSaidaVO;
	private List<NotaFiscalSaidaVO> notaFiscalSaidaErroVOs;
	private NotaFiscalSaidaRelControle notaFiscalSaidaRelControle;
	private ProgressBarVO progressBar;
	private String observacaoContribuinte;
	private Double valorTotalGeralNota;
	private Double valorTotalGeralIssqn;
	private File arquivoZip;
	private String statusServicoNFe;
	private ConfiguracaoGeralSistemaVO configuracaoRepositorioArquivo;
	private ConfiguracaoGeralSistemaVO configuracaoEmail;
	private List<PessoaVO> listaDestinatario;
	private List<NotaFiscalSaidaVO> notaFiscalSaidaEnviadasVOs;
	private Double valorTotalGeralNotaEnviada;
	private Double valorTotalGeralIssqnNotaEnviada;
	private Boolean buscarContasAReceber;
	private Boolean buscarContasRecebidas;
	private String tipoValorConsultaContasRecebidas;
	private String tipoValorConsultaContasAReceber;
	private String tipoDataConsideratContasRecebidas;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private Double valorTotalGeralPis;
	private Double valorTotalGeralCofins;
	private Double valorTotalGeralInss;
	private Double valorTotalGeralCsll;
	private Double valorTotalGeralIr;
	private CartaCorrecaoVO cartaCorrecaoVO;
	private ProgressBarVO progressBarEnviarNotasFicasStatusEnviada;
	private Boolean consultaDataScroller;
	private Boolean apresentarColunaIssRetido;
	private String dataValidadeCertificado;
	private String tipoPessoa;
	private String situacaoCertificado;
	private List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs;
	private ProgressBarVO progressBarNotasCompactadas;

	public NotaFiscalSaidaControle() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		getControleConsulta().setCampoConsulta("codigo");
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
	}

	public String novo() {
		try {
			setDataValidadeCertificado("Indefinida");
//			setUnidadeEnsinoVO(null);
			setCursoVO(null);
			setTurmaVO(null);
			setMatriculaVO(null);
			setProgressBar(null);
			getNotaFiscalSaidaVO().reiniciarControleBloqueioCompetencia();
			alterarObservacaoContribuinte();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaFiscalSaidaForm");

	}

	public String editar() {
		try {
			NotaFiscalSaidaVO obj = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaida");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaFiscalSaidaForm");
	}

	public String consultar() {
		try {
			super.consultar();
			List<NotaFiscalSaidaVO> objs = new ArrayList<NotaFiscalSaidaVO>(0);
			setArquivoZip(null);
			getControleConsultaOtimizado().setLimitePorPagina(10);
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			objs = getFacadeFactory().getNotaFiscalSaidaFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getTipoPessoa());
			HashMap<String, Double> total = null;
			getControleConsultaOtimizado().setListaConsulta(objs);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (!getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(1);
					setValorTotalGeralIssqn(0.0);
					setValorTotalGeralNota(0.0);
					setValorTotalGeralPis(0.0);
					setValorTotalGeralCofins(0.0);
					setValorTotalGeralInss(0.0);
					setValorTotalGeralCsll(0.0);
					setValorTotalGeralIr(0.0);
					for (NotaFiscalSaidaVO nota : objs) {
						setValorTotalGeralNota(getValorTotalGeralNota() + nota.getValorTotal());
						setValorTotalGeralIssqn(getValorTotalGeralIssqn() + nota.getTotalIssqn());
						setValorTotalGeralPis(getValorTotalGeralPis() + nota.getValorTotalPIS());
						setValorTotalGeralCofins(getValorTotalGeralCofins() + nota.getValorTotalCOFINS());
						setValorTotalGeralInss(getValorTotalGeralInss() + nota.getValorTotalINSS());
						setValorTotalGeralCsll(getValorTotalGeralCsll() + nota.getValorTotalCSLL());
						setValorTotalGeralIr(getValorTotalGeralIr() + nota.getValorTotalIRRF());
					}
				}
			}
			if (getControleConsulta().getCampoConsulta().equals("sacado")) {
				if (!getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setValorTotalGeralIssqn(0.0);
					setValorTotalGeralNota(0.0);
					setValorTotalGeralPis(0.0);
					setValorTotalGeralCofins(0.0);
					setValorTotalGeralInss(0.0);
					setValorTotalGeralCsll(0.0);
					setValorTotalGeralIr(0.0);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalSaidaFacade().consultarTotalRegistroPorNomeCliente(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					total = getFacadeFactory().getNotaFiscalSaidaFacade().consultarTotalNotaTotalISSQNPorNomeCliente(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					setValorTotalGeralNota(total.get("valorTotal").doubleValue());
					setValorTotalGeralIssqn(total.get("valorTotalISSQN").doubleValue());
				}
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				if (!getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setValorTotalGeralIssqn(0.0);
					setValorTotalGeralNota(0.0);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalSaidaFacade().consultarTotalRegistroPorMatricula(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					total = getFacadeFactory().getNotaFiscalSaidaFacade().consultarTotalNotaTotalISSQNPorMatricula(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					setValorTotalGeralNota(total.get("valorTotal").doubleValue());
					setValorTotalGeralIssqn(total.get("valorTotalISSQN").doubleValue());
					setValorTotalGeralPis(total.get("valorTotalPIS").doubleValue());
					setValorTotalGeralCofins(total.get("valorTotalCOFINS").doubleValue());
					setValorTotalGeralInss(total.get("valorTotalINSS").doubleValue());
					setValorTotalGeralCsll(total.get("valorTotalCSLL").doubleValue());
					setValorTotalGeralIr(total.get("valorTotalIRRF").doubleValue());
				}
			}
			if (getControleConsulta().getCampoConsulta().equals("numero")) {
				if (!getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setValorTotalGeralIssqn(0.0);
					setValorTotalGeralNota(0.0);
					setValorTotalGeralPis(0.0);
					setValorTotalGeralCofins(0.0);
					setValorTotalGeralInss(0.0);
					setValorTotalGeralCsll(0.0);
					setValorTotalGeralIr(0.0);
					for (NotaFiscalSaidaVO nota : objs) {
						setValorTotalGeralNota(getValorTotalGeralNota() + nota.getValorTotal());
						setValorTotalGeralIssqn(getValorTotalGeralIssqn() + nota.getTotalIssqn());
						setValorTotalGeralNota(getValorTotalGeralNota() + nota.getValorTotal());
						setValorTotalGeralIssqn(getValorTotalGeralIssqn() + nota.getTotalIssqn());
						setValorTotalGeralPis(getValorTotalGeralPis() + nota.getValorTotalPIS());
						setValorTotalGeralCofins(getValorTotalGeralCofins() + nota.getValorTotalCOFINS());
						setValorTotalGeralInss(getValorTotalGeralInss() + nota.getValorTotalINSS());
						setValorTotalGeralCsll(getValorTotalGeralCsll() + nota.getValorTotalCSLL());
						setValorTotalGeralIr(getValorTotalGeralIr() + nota.getValorTotalIRRF());
					}
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(1);
				}

			}
			if (getControleConsulta().getCampoConsulta().equals("tipoPessoa")) {
				if (!getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setValorTotalGeralIssqn(0.0);
					setValorTotalGeralNota(0.0);
					setValorTotalGeralPis(0.0);
					setValorTotalGeralCofins(0.0);
					setValorTotalGeralInss(0.0);
					setValorTotalGeralCsll(0.0);
					setValorTotalGeralIr(0.0);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalSaidaFacade().consultarTotalRegistroPorTipoPessoa(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getTipoPessoa()));
					total = getFacadeFactory().getNotaFiscalSaidaFacade().consultarTotalNotaTotalISSQNPorTipoPessoa(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getTipoPessoa());
					setValorTotalGeralNota(total.get("valorTotal").doubleValue());
					setValorTotalGeralIssqn(total.get("valorTotalISSQN").doubleValue());
				}
			}
			setMensagemID("msg_dados_consultados");
			// return "consultar";
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<NotaFiscalSaidaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "consultar";
			return "";
		}
	}

	public String getDownloadNotasCompactadas() {
		try {
			if (getArquivoZip() != null && !getArquivoZip().getAbsolutePath().equals("")) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", arquivoZip.getName());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", arquivoZip.getParent());
				return "location.href='../../DownloadSV'";
				// return
				// "abrirPopup('"+getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo()
				// +"/"+ PastaBaseArquivoEnum.NFE.getValue() +"/" +
				// PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue()+ "/" +
				// getArquivoZip().getName() + "', 'NFEs', 0, 0);";
			} else {
				return "";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void removerItemNotaFiscal() {
		try {
			NotaFiscalSaidaServicoVO obj = (NotaFiscalSaidaServicoVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaServicoItem");

			setValorTotalGeralNota(getValorTotalGeralNota() - obj.getPrecoUnitario());
			setValorTotalGeralIssqn(getValorTotalGeralIssqn() - obj.getTotalIssqn());

			getNotaFiscalSaidaVO().setValorTotal(getNotaFiscalSaidaVO().getValorTotal() - obj.getPrecoUnitario());
			getNotaFiscalSaidaVO().setTotalIssqn(getNotaFiscalSaidaVO().getTotalIssqn() - obj.getTotalIssqn());

			getNotaFiscalSaidaVO().getNotaFiscalSaidaServicoVOs().remove(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarProgressBar() {
		try {
			setArquivoZip(null);
			getProgressBarNotasCompactadas().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarNotasCompactadas().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarNotasCompactadas().resetar();
			
			getProgressBarNotasCompactadas().iniciar(0l, 100000, "Iniciando Processamento",
					true, this, "executarCompactarNotasEnviadas");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarCompactarNotasEnviadas() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("tipoPessoa")) {
				getControleConsulta().setValorConsulta(getTipoPessoa());
			}
			List<NotaFiscalSaidaVO> objs = getFacadeFactory().getNotaFiscalSaidaFacade().consultarDadosParaCompactacaoNotasEnviadas(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getSituacao(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), 0, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarNotasCompactadas().getUsuarioVO());
			if (!objs.isEmpty()) {
				((NotaFiscalSaidaControle)getProgressBarNotasCompactadas().getSuperControle()).setArquivoZip(new File(getProgressBarNotasCompactadas().getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + new Date().getTime() + ".zip"));
				File[] arquivos = getFacadeFactory().getNotaFiscalSaidaFacade().executarCompactarNotasEnviadas(objs, getProgressBarNotasCompactadas().getConfiguracaoGeralSistemaVO(), getProgressBarNotasCompactadas().getUsuarioVO(), getProgressBarNotasCompactadas());
				getFacadeFactory().getArquivoHelper().zip(arquivos, ((NotaFiscalSaidaControle)getProgressBarNotasCompactadas().getSuperControle()).getArquivoZip());
			}
			getProgressBarNotasCompactadas().getSuperControle().setMensagemID("msg_dados_consultados");
			getProgressBarNotasCompactadas().setForcarEncerramento(true);
		} catch (ConsistirException ce) {
			setArquivoZip(null);
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
		} catch (Exception e) {
			setArquivoZip(null);
			getProgressBarNotasCompactadas().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/*
	 * Método criado para corrigir base de dados, após correção no código fonte
	 * @author Alessandro Lima
	 */
	public void realizarCorrecaoLinkAcesso() {
		try {
			NotaFiscalSaidaVO obj = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
			setNotaFiscalSaidaVO(getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			notaFiscalSaidaVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
			ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
			getFacadeFactory().getNotaFiscalServicoEletronicaFacade().imprimirDanfe(notaFiscalSaidaVO, conf, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public synchronized void executarEnviarNotasFiscais() {
		try {
			if (!getNotaFiscalSaidaVOs().isEmpty()) {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				UteisNfe.validarCaminhoCertificado(unidadeEnsinoVO.getConfiguracaoNotaFiscalVO(), getConfiguracaoRepositorioArquivo());
				ConfiguracaoGeralSistemaVO conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe();			
				for (Iterator<NotaFiscalSaidaVO> iterator = getNotaFiscalSaidaVOs().iterator(); iterator.hasNext();) {
					NotaFiscalSaidaVO notaSaidaVO = (NotaFiscalSaidaVO) iterator.next();
					if (notaSaidaVO.getNotaFiscalSaidaSelecionado()) {
						try {
							getFacadeFactory().getNotaFiscalSaidaFacade().realizarEscritaTextoDebugNFE_LOG(" (Controlador) Iterando nota fiscal");	
							notaSaidaVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
							notaSaidaVO.setResponsavel(getUsuarioLogado());
							notaSaidaVO.setObservacaoContribuinte(getObservacaoContribuinte());	
							notaSaidaVO.setPossuiErroWebservice(Boolean.FALSE);						
							if(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getUtilizarServicoWebserviceAuxiliar()) {
								getFacadeFactory().getNotaFiscalSaidaFacade().realizarInclusaoNotaFiscalSaidaEnviandoNFeWebservice( notaSaidaVO,  getConfiguracaoRepositorioArquivo(),conGeralSistemaVO, getConfiguracaoEmail(), getProgressBar(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
								Uteis.checkState(notaSaidaVO.getPossuiErroWebservice(), "Falha ao enviar Nota Fiscal!");							
							}else {
								String resultado = getFacadeFactory().getNotaFiscalSaidaFacade().enviarNFeWebservice(getControladorAplicacaoControle("AplicacaoControle"), notaSaidaVO, true, getObservacaoContribuinte(), getConfiguracaoRepositorioArquivo(), getConfiguracaoEmail(), getProgressBar(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
								Uteis.checkState(Uteis.isAtributoPreenchido(resultado), "Falha ao enviar Nota Fiscal!");								
							}							
							if (notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
								setValorTotalGeralNotaEnviada(getValorTotalGeralNotaEnviada() + notaSaidaVO.getValorTotal());
								setValorTotalGeralIssqnNotaEnviada(getValorTotalGeralIssqnNotaEnviada() + notaSaidaVO.getTotalIssqn());
								setValorTotalGeralIssqn(getValorTotalGeralIssqn() - notaSaidaVO.getTotalIssqn());
								setValorTotalGeralNota(getValorTotalGeralNota() - notaSaidaVO.getValorTotal());
								getNotaFiscalSaidaEnviadasVOs().add(notaSaidaVO);
								iterator.remove();
							}
						} catch (AcessoException e) {
							getFacadeFactory().getNotaFiscalSaidaFacade().realizarEscritaTextoDebugNFE_LOG(" (Controlador) AcessoExcpetion " + e.getMessage());			
						    throw e;
						} catch (Exception e) {
							getFacadeFactory().getNotaFiscalSaidaFacade().realizarEscritaTextoDebugNFE_LOG(" (Controlador) Exception " + e.getMessage());		
							if (e.getMessage().contains("check_numeronota_serie_ja_existente")) {
								throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscal_numeroExistenteCadastro") + ' ' + notaSaidaVO.getNumeroNota() + " ( Código da nota com erro : " + notaSaidaVO.getCodigo());
							}
							getNotaFiscalSaidaErroVOs().add(notaSaidaVO);
							iterator.remove();
						}
						if (!getProgressBar().getAssincrono()) {
							break;
						}
					}
					iterator.remove();
				}
				if (getNotaFiscalSaidaErroVOs().isEmpty() && getNotaFiscalSaidaEnviadasVOs().isEmpty()) {
					setMensagemID("msg_NotaFiscalSaida_avisoCancelamentoCincoMinutosAposEmissao", Uteis.SUCESSO);
				}
			} else {
				getProgressBar().setForcarEncerramento(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			if (!getProgressBar().getAssincrono()) {
				getProgressBar().setForcarEncerramento(true);
			}
		}
	}

	public void executarInicioProgressBarEnviarNotasFiscais() throws Exception {
		try {

			getProgressBar().resetar();
			if (!getNotaFiscalSaidaVOs().isEmpty()) {
				Integer qtdeNota = 0;
				for (NotaFiscalSaidaVO nota : getNotaFiscalSaidaVOs()) {
					if (nota.getNotaFiscalSaidaSelecionado()) {
						qtdeNota += 1;
					}
				}
				if (qtdeNota > 0) {
					getProgressBar().iniciar(0l, qtdeNota, "Iniciando Envio da NF-e", false, this, "executarEnviarNotasFiscais");
				} else {
					getProgressBar().setForcarEncerramento(true);
				}

			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String executarCancelarNotaTelaConsulta() throws Exception {
		try {
			if (getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum() == TipoIntegracaoNfeEnum.NFSE) {
				getNotaFiscalSaidaVOs().clear();
				getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorChavePrimaria(getNotaFiscalSaidaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getNotaFiscalSaidaVOs().add(getNotaFiscalSaidaVO());
				if (!getNotaFiscalSaidaVOs().isEmpty()) {
					getFacadeFactory().getNotaFiscalSaidaFacade().cancelarNFSE(getNotaFiscalSaidaVOs().get(0), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				}
				setNotaFiscalSaidaVO(new NotaFiscalSaidaVO());
			} else if (getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum() == TipoIntegracaoNfeEnum.NFE) {
				getNotaFiscalSaidaVOs().clear();
				getNotaFiscalSaidaVOs().add(getNotaFiscalSaidaVO());
				if (!getNotaFiscalSaidaVOs().isEmpty()) {
					getNotaFiscalSaidaVOs().get(0).setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getNotaFiscalSaidaVO().setUnidadeEnsinoVO(getNotaFiscalSaidaVOs().get(0).getUnidadeEnsinoVO());
					getFacadeFactory().getNotaFiscalSaidaFacade().cancelarNFeWebservice(getNotaFiscalSaidaVOs(), getNotaFiscalSaidaErroVOs(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoRepositorioArquivo(), getUsuarioLogado());
				}
				setNotaFiscalSaidaVO(new NotaFiscalSaidaVO());
			}
			getNotaFiscalSaidaVOs().clear();
			setMensagemID("");
			return Uteis.getCaminhoRedirecionamentoNavegacao("notaFiscalSaidaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void executarCancelarNota() throws Exception {
		try {
			limparMensagem();
			getNotaFiscalSaidaVOs().clear();
			getNotaFiscalSaidaVOs().add(getNotaFiscalSaidaVO());
			if (!getNotaFiscalSaidaVOs().isEmpty()) {
				getNotaFiscalSaidaVOs().get(0).setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getNotaFiscalSaidaVO().setUnidadeEnsinoVO(getNotaFiscalSaidaVOs().get(0).getUnidadeEnsinoVO());
				getFacadeFactory().getNotaFiscalSaidaFacade().cancelarNFeWebservice(getNotaFiscalSaidaVOs(), getNotaFiscalSaidaErroVOs(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoRepositorioArquivo(), getUsuarioLogado());
			}
			getNotaFiscalSaidaVOs().clear();
			setNotaFiscalSaidaVO(new NotaFiscalSaidaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirDanfe() {
		try {
			NotaFiscalSaidaVO obj = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
			setNotaFiscalSaidaVO(getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

			notaFiscalSaidaRelControle = null;
			notaFiscalSaidaRelControle = (NotaFiscalSaidaRelControle) context().getExternalContext().getSessionMap().get(NotaFiscalSaidaRelControle.class.getSimpleName());
			if (notaFiscalSaidaRelControle == null) {
				notaFiscalSaidaRelControle = new NotaFiscalSaidaRelControle();
				context().getExternalContext().getSessionMap().put(NotaFiscalSaidaRelControle.class.getSimpleName(), notaFiscalSaidaRelControle);
			}
			if (!obj.getCodigo().equals(0)) {
				getNotaFiscalSaidaVO().getNotaFiscalSaidaServicoVOs().forEach(nfssvo -> {
					if (nfssvo.getCodigoProduto().equals(0)) {
						nfssvo.setCodigoProduto(nfssvo.getCodigo());
					}
				});
				getNotaFiscalSaidaRelControle().setNotaFiscalSaidaVO(getNotaFiscalSaidaVO());
				getNotaFiscalSaidaRelControle().imprimirPDF();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarCancelarNota() {
		try {
			NotaFiscalSaidaVO obj = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
			obj.setNotaFiscalSaidaSelecionado(true);
			setNotaFiscalSaidaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void fecharCancelarNota() {
		try {
			setNotaFiscalSaidaVO(new NotaFiscalSaidaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void fecharCancelarNotaTelaConsulta() {
		try {
			getNotaFiscalSaidaVOs().clear();
			getNotaFiscalSaidaErroVOs().clear();
			setNotaFiscalSaidaVO(new NotaFiscalSaidaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarConsultar() {
		try {
			setListaConsulta(null);
			setUnidadeEnsinoVO(null);
			setCursoVO(null);
			setTurmaVO(null);
			setMatriculaVO(null);
			getNotaFiscalSaidaVOs().clear();
			getNotaFiscalSaidaErroVOs().clear();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaFiscalSaidaCons");
	}

	public void selecionarTodosTiposOrigemContaReceber() {
		if (getTipoOrigemContaReceberTodos()) {
			getFiltroRelatorioFinanceiroVO().realizarMarcarTodasOrigens();
		} else {
			getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodasOrigens();
		}
	}

	public void alterarDestinatario() {
		try {
			PessoaVO pessoaVO = (PessoaVO) context().getExternalContext().getRequestMap().get("destinatario");
			if (pessoaVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
				getNotaFiscalSaidaVO().setPessoaVO(pessoaVO);
				getNotaFiscalSaidaVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
				getNotaFiscalSaidaVO().setEndereco(getNotaFiscalSaidaVO().getPessoaVO().getEndereco() + " " + getNotaFiscalSaidaVO().getPessoaVO().getNumero() + " " + getNotaFiscalSaidaVO().getPessoaVO().getComplemento());
				getNotaFiscalSaidaVO().setSetor(getNotaFiscalSaidaVO().getPessoaVO().getSetor());
				getNotaFiscalSaidaVO().setNomeRazaoSocial(getNotaFiscalSaidaVO().getPessoaVO().getNome());
				getNotaFiscalSaidaVO().setCnpjCpf(getNotaFiscalSaidaVO().getPessoaVO().getCPF());
				getNotaFiscalSaidaVO().setCep(getNotaFiscalSaidaVO().getPessoaVO().getCEP());
				getNotaFiscalSaidaVO().setUf(getNotaFiscalSaidaVO().getPessoaVO().getCidade().getEstado().getSigla());
				getNotaFiscalSaidaVO().setTelefone(getNotaFiscalSaidaVO().getPessoaVO().getTelefoneComer());
				getNotaFiscalSaidaVO().setMunicipio(getNotaFiscalSaidaVO().getPessoaVO().getCidade().getNome());
				getNotaFiscalSaidaVO().setResponsavelFinanceiro(null);
			} else {
				getNotaFiscalSaidaVO().setResponsavelFinanceiro(pessoaVO);
				getNotaFiscalSaidaVO().setTipoPessoa(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
				getNotaFiscalSaidaVO().setEndereco(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getEndereco() + " " + getNotaFiscalSaidaVO().getResponsavelFinanceiro().getNumero() + " " + getNotaFiscalSaidaVO().getResponsavelFinanceiro().getComplemento());
				getNotaFiscalSaidaVO().setSetor(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getSetor());
				getNotaFiscalSaidaVO().setNomeRazaoSocial(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getNome());
				getNotaFiscalSaidaVO().setCnpjCpf(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getCPF());
				getNotaFiscalSaidaVO().setCep(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getCEP());
				getNotaFiscalSaidaVO().setUf(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getCidade().getEstado().getSigla());
				getNotaFiscalSaidaVO().setTelefone(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getTelefoneComer());
				getNotaFiscalSaidaVO().setMunicipio(getNotaFiscalSaidaVO().getResponsavelFinanceiro().getCidade().getNome());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDestinatario() {
		try {
			selecionarNotaFiscalSaida();
			setListaDestinatario(getFacadeFactory().getPessoaFacade().consultarFiliacaoPorPessoa(getNotaFiscalSaidaVO().getPessoaVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarNotaFiscalSaida() throws Exception {
		try {
			setNotaFiscalSaidaVO((NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens"));
			if (!getNotaFiscalSaidaVO().getCodigo().equals(0)) {
				// comentado pois lista de serviços já está populado, e nem sempre a nota rejeitada vai conter todas contas que tinha quando foi rejeitada.
//				getNotaFiscalSaidaVO().setNotaFiscalSaidaServicoVOs(getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarNotaFiscalSaidaServicos(getNotaFiscalSaidaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getNotaFiscalSaidaVO().setCartaCorrecaoVOs(getFacadeFactory().getCartaCorrecaoNotaFiscalFacade().consultarPorNotaFiscal(getNotaFiscalSaidaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsino().getCodigo());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}
	
	public void validarBloqueioCompetenciaParaEmissaoNFe(List<ContaReceberVO> contasReceberVOs, List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs) throws Exception {
		Map<String,FechamentoMesVO> mapaCompetenciasComBloquio = new HashMap<String,FechamentoMesVO>();
		
		for (ContaReceberVO contaReceberVO : contasReceberVOs) {
			String competencia = Uteis.getMesData(contaReceberVO.getDataVencimento()) + "/" + Uteis.getAnoData(contaReceberVO.getDataVencimento());
		}
		for (ContaReceberRecebimentoVO contaReceberRecebimentoVO : contaReceberRecebimentoVOs) {
			String competencia = Uteis.getMesData(contaReceberRecebimentoVO.getContaReceberVO().getDataVencimento()) + "/" + Uteis.getAnoData(contaReceberRecebimentoVO.getContaReceberVO().getDataVencimento());
			 
		}
	}

	public void consultarContasRecebidas() {
		try {
			setProgressBar(null);
			NotaFiscalSaida.validarDados(getNotaFiscalSaidaVO(), getUnidadeEnsinoVO().getCodigo(), getBuscarContasAReceber(), getBuscarContasRecebidas(), getTipoDataConsideratContasRecebidas());
			
			/**
			 * Responsável por validar se notas já estão sendo emitidas para a configuração
			 * de nota fiscal para esta unidade de ensino selecionada
			 */
			setNotaFiscalSaidaEnviadasVOs(null);
			List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs = new ArrayList<ContaReceberRecebimentoVO>(0);
			List<ContaReceberVO> contasReceberVOs = new ArrayList<ContaReceberVO>(0);
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getControladorAplicacaoControle("AplicacaoControle").executarEnvioNotasFiscais(unidadeEnsinoVO.getConfiguracaoNotaFiscalVO().getCodigo(), true, false, false);
			if (getBuscarContasRecebidas() && !getTipoValorConsultaContasRecebidas().equals("valorCheio")) {
				contaReceberRecebimentoVOs = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContasRecebidasNotaFiscalSaida(
				        getMatriculaVO(),
				        getDataInicio(),
				        getDataFim(),
				        getCursoVO(),
				        getTurmaVO(),
				        getTipoValorConsultaContasRecebidas(),
				        getTipoDataConsideratContasRecebidas(),
				        getFiltroRelatorioFinanceiroVO(),
				        getSituacaoContaReceber(),
				        getConfiguracaoFinanceiroPadraoSistema(),
				        getUnidadeEnsinoVO().getCodigo(),
				        getUsuarioLogado());
			}
			if (getBuscarContasAReceber()) {
				contasReceberVOs = getFacadeFactory().getNotaFiscalSaidaFacade().consultarContasReceberNotaFiscalSaida(
				        getMatriculaVO(),
				        getDataInicio(),
				        getDataFim(),
				        getCursoVO(),
				        getTurmaVO(),
				        getTipoValorConsultaContasAReceber(),
				        getFiltroRelatorioFinanceiroVO(),
				        getSituacaoContaReceber(),
				        getConfiguracaoFinanceiroPadraoSistema(),
				        getUnidadeEnsinoVO().getCodigo(),
				        getUsuarioLogado());
			}

			if (getBuscarContasRecebidas() && getTipoValorConsultaContasRecebidas().equals("valorCheio")) {
				contasReceberVOs.addAll(getFacadeFactory().getNotaFiscalSaidaFacade().consultarContasRecebidasValorCheioNotaFiscal(
				        getMatriculaVO(),
				        getDataInicio(),
				        getDataFim(),
				        getCursoVO(),
				        getTurmaVO(),
				        getTipoValorConsultaContasRecebidas(),
				        getFiltroRelatorioFinanceiroVO(),
				        "RE",
				        getConfiguracaoFinanceiroPadraoSistema(),
				        getUnidadeEnsinoVO().getCodigo(),
				        getUsuarioLogado()));
			}
			
			getFacadeFactory().getNotaFiscalSaidaFacade().montarDadosGeracaoNotaFiscalSaida(getNotaFiscalSaidaVOs(), contaReceberRecebimentoVOs, contasReceberVOs, getUnidadeEnsinoVO().getCodigo(), getNotaFiscalSaidaEnviadasVOs(), getUsuarioLogado());
			setValorTotalGeralIssqn(0.0);
			setValorTotalGeralNota(0.0);
			setValorTotalGeralPis(0.0);
			setValorTotalGeralCofins(0.0);
			setValorTotalGeralInss(0.0);
			setValorTotalGeralCsll(0.0);
			setValorTotalGeralIr(0.0);
			gravarDadosPadroesEmissaoRelatorio();
			verificarAprentarColunaIssRetino();
			setNotaFiscalSaidaErroVOs(new ArrayList<NotaFiscalSaidaVO>(0));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarDadosPadroesEmissaoRelatorio() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getBuscarContasAReceber().toString(), NotaFiscalSaidaControle.class.getSimpleName(), "buscarContasAReceber", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getBuscarContasRecebidas().toString(), NotaFiscalSaidaControle.class.getSimpleName(), "buscarContasRecebidas", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoValorConsultaContasAReceber(), NotaFiscalSaidaControle.class.getSimpleName(), "tipoValorConsultaContasAReceber", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoValorConsultaContasRecebidas(), NotaFiscalSaidaControle.class.getSimpleName(), "tipoValorConsultaContasRecebidas", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoDataConsideratContasRecebidas(), NotaFiscalSaidaControle.class.getSimpleName(), "tipoDataConsideratContasRecebidas", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), NotaFiscalSaidaControle.class.getSimpleName(), getUsuarioLogado());
		} catch (Exception e) {

		}
	}

	@PostConstruct
	public void montarDadosPadroesEmissaoRelatorio() {
		try {
			Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "buscarContasAReceber", "buscarContasRecebidas", "tipoValorConsultaContasAReceber", "tipoValorConsultaContasRecebidas", "tipoDataConsideratContasRecebidas" }, NotaFiscalSaidaControle.class.getSimpleName());
			for (String key : retorno.keySet()) {
				try {
					if (key.equals("buscarContasAReceber")) {
						if (retorno.get(key).equals("true")) {
							setBuscarContasAReceber(true);
						} else {
							setBuscarContasAReceber(false);
						}
					} else if (key.equals("buscarContasRecebidas")) {
						if (retorno.get(key).equals("true")) {
							setBuscarContasRecebidas(true);
						} else {
							setBuscarContasRecebidas(false);
						}
					} else if (key.equals("tipoValorConsultaContasAReceber")) {
						setTipoValorConsultaContasAReceber(retorno.get(key));
					} else if (key.equals("tipoValorConsultaContasRecebidas")) {
						setTipoValorConsultaContasRecebidas(retorno.get(key));
					} else if (key.equals("tipoDataConsideratContasRecebidas")) {
						setTipoDataConsideratContasRecebidas(retorno.get(key));
					}
				} catch (Exception e) {

				}
			}
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), NotaFiscalSaidaControle.class.getSimpleName(), getUsuarioLogado());
		} catch (Exception e) {

		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(null);
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(objAluno.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuario());
			if (matriculaPeriodo == null) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getUnidadeEnsinoVO().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setListaSelectItemUnidadeEnsino(new ArrayList(0));
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		Iterator i = null;
		try {
			setListaUnidadeEnsinoVOs(consultarUnidadeEnsinoPorNome(""));
			i = getListaUnidadeEnsinoVOs().iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome() + " - " + obj.getAbreviatura()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			i = null;
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("-1", " "));
		itens.add(new SelectItem("AU", "Autorizada"));
		itens.add(new SelectItem("RE", "Rejeitada"));
		itens.add(new SelectItem("CA", "Cancelada"));
		return itens;
	}

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
		setTurmaVO(obj);
		setCursoVO(getTurmaVO().getCurso());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(null);
			setCursoVO(null);
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCursoVO(null);
		} catch (Exception e) {
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			setCursoVO(obj);
			Uteis.liberarListaMemoria(getListaConsultaCurso());
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("numero", "Número"));
		itens.add(new SelectItem("sacado", "Destinatário"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("tipoPessoa", "Tipo Pessoa"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getListaSelectItemValorConsiderarContaReceber() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("valorCheio", "Valor Cheio"));
		itens.add(new SelectItem("valorAtual", "Valor Atual"));
		itens.add(new SelectItem("valorBaseDesconto", "Valor Base Com Descontos"));
		return itens;
	}

	public List getListaSelectItemValorConsiderarContaRecebidas() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("valorCheio", "Valor Cheio"));
		itens.add(new SelectItem("valorRecebido", "Valor Recebido"));
		itens.add(new SelectItem("valorCompensado", "Valor Compensado"));
		return itens;
	}

	public List getListaSelectItemDataConsiderar() {
		List itens = new ArrayList(0);
		if (getTipoValorConsultaContasRecebidas().equals("valorCheio") || getTipoValorConsultaContasRecebidas().equals("valorRecebido")) {
			itens.add(new SelectItem("dataVencimento", "Vencimento"));
			itens.add(new SelectItem("dataRecebimento", "Recebimento"));
		} else if (getTipoValorConsultaContasRecebidas().equals("valorCompensado")) {
			itens.add(new SelectItem("dataCompensacao", "Compensação"));
		} else {
			itens.add(new SelectItem("dataVencimento", "Vencimento"));
			itens.add(new SelectItem("dataRecebimento", "Recebimento"));
		}
		return itens;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public String getSituacaoContaReceber() {
		if (situacaoContaReceber == null) {
			situacaoContaReceber = "";
		}
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public List<NotaFiscalSaidaVO> getNotaFiscalSaidaVOs() {
		if (notaFiscalSaidaVOs == null) {
			notaFiscalSaidaVOs = new ArrayList<NotaFiscalSaidaVO>(0);
		}
		return notaFiscalSaidaVOs;
	}

	public void setNotaFiscalSaidaVOs(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs) {
		this.notaFiscalSaidaVOs = notaFiscalSaidaVOs;
	}

	public NotaFiscalSaidaVO getNotaFiscalSaidaVO() {
		if (notaFiscalSaidaVO == null) {
			notaFiscalSaidaVO = new NotaFiscalSaidaVO();
		}
		return notaFiscalSaidaVO;
	}

	public void setNotaFiscalSaidaVO(NotaFiscalSaidaVO notaFiscalSaidaVO) {
		this.notaFiscalSaidaVO = notaFiscalSaidaVO;
	}

	public void preencherTodosListaContasRecebidas() throws Exception {
		getFacadeFactory().getNotaFiscalSaidaFacade().preencherTodosListaContasRecebidas(getNotaFiscalSaidaVOs());
		validarSelecaoNotaFiscalSaidaTotalizadores();
	}

	public void desmarcarTodosListaContasRecebidas() throws Exception {
		getFacadeFactory().getNotaFiscalSaidaFacade().desmarcarTodosListaContasRecebidas(getNotaFiscalSaidaVOs());
		validarSelecaoNotaFiscalSaidaTotalizadores();
	}

	public List<NotaFiscalSaidaVO> getNotaFiscalSaidaErroVOs() {
		if (notaFiscalSaidaErroVOs == null) {
			notaFiscalSaidaErroVOs = new ArrayList<NotaFiscalSaidaVO>(0);
		}
		return notaFiscalSaidaErroVOs;
	}

	public void setNotaFiscalSaidaErroVOs(List<NotaFiscalSaidaVO> notaFiscalSaidaErroVOs) {
		this.notaFiscalSaidaErroVOs = notaFiscalSaidaErroVOs;
	}

	public NotaFiscalSaidaRelControle getNotaFiscalSaidaRelControle() {
		return notaFiscalSaidaRelControle;
	}

	public void setNotaFiscalSaidaRelControle(NotaFiscalSaidaRelControle notaFiscalSaidaRelControle) {
		this.notaFiscalSaidaRelControle = notaFiscalSaidaRelControle;
	}

	public ProgressBarVO getProgressBar() {
		if (progressBar == null) {
			progressBar = new ProgressBarVO();
		}
		return progressBar;
	}

	public void setProgressBar(ProgressBarVO progressBar) {
		this.progressBar = progressBar;
	}

	public String getObservacaoContribuinte() {
		if (observacaoContribuinte == null) {
			observacaoContribuinte = getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal();
		}
		return observacaoContribuinte;
	}

	public void setObservacaoContribuinte(String observacaoContribuinte) {
		this.observacaoContribuinte = observacaoContribuinte;
	}

	public Double getValorTotalGeralNota() {
		if (valorTotalGeralNota == null) {
			valorTotalGeralNota = 0.0;
		}
		return valorTotalGeralNota;
	}

	public void setValorTotalGeralNota(Double valorTotalGeralNota) {
		this.valorTotalGeralNota = valorTotalGeralNota;
	}

	public Double getValorTotalGeralIssqn() {
		if (valorTotalGeralIssqn == null) {
			valorTotalGeralIssqn = 0.0;
		}
		return valorTotalGeralIssqn;
	}

	public void setValorTotalGeralIssqn(Double valorTotalGeralIssqn) {
		this.valorTotalGeralIssqn = valorTotalGeralIssqn;
	}

	public File getArquivoZip() {
		return arquivoZip;
	}

	public void setArquivoZip(File arquivoZip) {
		this.arquivoZip = arquivoZip;
	}

	public Boolean getTipoOrigemContaReceberTodos() {
		if (tipoOrigemContaReceberTodos == null) {
			tipoOrigemContaReceberTodos = Boolean.FALSE;
		}
		return tipoOrigemContaReceberTodos;
	}

	public void setTipoOrigemContaReceberTodos(Boolean tipoOrigemContaReceberTodos) {
		this.tipoOrigemContaReceberTodos = tipoOrigemContaReceberTodos;
	}

	public void consultarStatusServicoNFe() {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			System.out.println("STATUS-NFE-> UnidadeEnsino: " + unidadeEnsinoVO.getNome());
			setStatusServicoNFe(getFacadeFactory().getNotaFiscalSaidaFacade().consultarStatusServicoNFe(unidadeEnsinoVO, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			System.out.println("STATUS-NFE-> Exception: " + e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarStatusServicoNFeWebservice() {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setStatusServicoNFe(getFacadeFactory().getNotaFiscalSaidaFacade().consultarStatusServicoNFeWebservice(unidadeEnsinoVO, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			System.out.println("STATUS-NFE-> Exception: " + e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getStatusServicoNFe() {
		if (statusServicoNFe == null) {
			statusServicoNFe = "";
		}
		return statusServicoNFe;
	}

	public void setStatusServicoNFe(String statusServicoNFe) {
		this.statusServicoNFe = statusServicoNFe;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoRepositorioArquivo() {
		if (configuracaoRepositorioArquivo == null) {
			try {
				configuracaoRepositorioArquivo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
			} catch (Exception e) {
				e.printStackTrace();
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return configuracaoRepositorioArquivo;
	}

	public void setConfiguracaoRepositorioArquivo(ConfiguracaoGeralSistemaVO configuracaoRepositorioArquivo) {
		this.configuracaoRepositorioArquivo = configuracaoRepositorioArquivo;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoEmail() {
		if (configuracaoEmail == null) {
			try {
				configuracaoEmail = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			} catch (Exception e) {
				e.printStackTrace();
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return configuracaoEmail;
	}

	public void setConfiguracaoEmail(ConfiguracaoGeralSistemaVO configuracaoEmail) {
		this.configuracaoEmail = configuracaoEmail;
	}

	public List<PessoaVO> getListaDestinatario() {
		if (listaDestinatario == null) {
			listaDestinatario = new ArrayList<PessoaVO>(0);
		}
		return listaDestinatario;
	}

	public void setListaDestinatario(List<PessoaVO> listaDestinatario) {
		this.listaDestinatario = listaDestinatario;
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		setConsultaDataScroller(true);
		consultar();
	}

	public Boolean getIsApresentarBotaoVerificarStatusServicoNFE() {
		if (!getNotaFiscalSaidaVOs().isEmpty() && getNotaFiscalSaidaVOs().get(0).getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum() != null
		        && getNotaFiscalSaidaVOs().get(0).getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
			return true;
		} else
			return false;
	}

	public void limparDadosConsulta() {
		setDataValidadeCertificado("Indefinida");
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setLimitePorPagina(0);
		getControleConsultaOtimizado().setOffset(0);
	}

	public List<NotaFiscalSaidaVO> getNotaFiscalSaidaEnviadasVOs() {
		if (notaFiscalSaidaEnviadasVOs == null) {
			notaFiscalSaidaEnviadasVOs = new ArrayList<NotaFiscalSaidaVO>();
		}
		return notaFiscalSaidaEnviadasVOs;
	}

	public void setNotaFiscalSaidaEnviadasVOs(List<NotaFiscalSaidaVO> notaFiscalSaidaEnviadasVOs) {
		this.notaFiscalSaidaEnviadasVOs = notaFiscalSaidaEnviadasVOs;
	}

	public Double getValorTotalGeralNotaEnviada() {
		if (valorTotalGeralNotaEnviada == null) {
			valorTotalGeralNotaEnviada = 0.0;
		}
		return valorTotalGeralNotaEnviada;
	}

	public void setValorTotalGeralNotaEnviada(Double valorTotalGeralNotaEnviada) {
		this.valorTotalGeralNotaEnviada = valorTotalGeralNotaEnviada;
	}

	public Double getValorTotalGeralIssqnNotaEnviada() {
		if (valorTotalGeralIssqnNotaEnviada == null) {
			valorTotalGeralIssqnNotaEnviada = 0.0;
		}
		return valorTotalGeralIssqnNotaEnviada;
	}

	public void setValorTotalGeralIssqnNotaEnviada(Double valorTotalGeralIssqnNotaEnviada) {
		this.valorTotalGeralIssqnNotaEnviada = valorTotalGeralIssqnNotaEnviada;
	}

	public synchronized void executarEnviarNotasFiscaisEnviadas() throws Exception {
		if (!getNotaFiscalSaidaEnviadasVOs().isEmpty()) {
			try {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				UteisNfe.validarCaminhoCertificado(unidadeEnsinoVO.getConfiguracaoNotaFiscalVO(), getConfiguracaoRepositorioArquivo());
				for (Iterator<NotaFiscalSaidaVO> iterator = getNotaFiscalSaidaEnviadasVOs().iterator(); iterator.hasNext();) {
					NotaFiscalSaidaVO notaSaidaVO = (NotaFiscalSaidaVO) iterator.next();
					if (notaSaidaVO.getNotaFiscalSaidaSelecionado()) {
						try {
							getProgressBarEnviarNotasFicasStatusEnviada().incrementar();
							String autorizado = "";
							notaSaidaVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
							if(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
								getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorReciboEnvioNFE( notaSaidaVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe(), getConfiguracaoRepositorioArquivo(),   getUsuarioLogado());
								
							}else {
								getFacadeFactory().getNotaFiscalSaidaFacade().consultarNFSE(getControladorAplicacaoControle("AplicacaoControle"), notaSaidaVO, true, getObservacaoContribuinte(), getConfiguracaoRepositorioArquivo(), getProgressBarEnviarNotasFicasStatusEnviada(), autorizado, getUsuarioLogado());
							}
						

							if (notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
								continue;
							} else {
								try {
									getFacadeFactory().getNotaFiscalSaidaFacade().realizarEnvioEmailNotaFiscal(notaSaidaVO, getConfiguracaoRepositorioArquivo(), getConfiguracaoEmail(), unidadeEnsinoVO.getConfiguracaoNotaFiscalVO().getNotificarAlunoNotaFiscalGerada().booleanValue(), getUsuarioLogado());
									setValorTotalGeralNotaEnviada(getValorTotalGeralNotaEnviada() - notaSaidaVO.getValorTotal());
									setValorTotalGeralIssqnNotaEnviada(getValorTotalGeralIssqnNotaEnviada() - notaSaidaVO.getTotalIssqn());
									setValorTotalGeralIssqn(getValorTotalGeralIssqn() + notaSaidaVO.getTotalIssqn());
									setValorTotalGeralNota(getValorTotalGeralNota() + notaSaidaVO.getValorTotal());
									getNotaFiscalSaidaVOs().add(notaSaidaVO);
									iterator.remove();
								} catch (Exception e) {
									continue;
								}
							}
						} catch (AcessoException e) {
							throw e;
						} catch (Exception e) {
							setValorTotalGeralNotaEnviada(getValorTotalGeralNotaEnviada() - notaSaidaVO.getValorTotal());
							setValorTotalGeralIssqnNotaEnviada(getValorTotalGeralIssqnNotaEnviada() - notaSaidaVO.getTotalIssqn());
							getNotaFiscalSaidaErroVOs().add(notaSaidaVO);
							iterator.remove();
						}
						if (!getProgressBarEnviarNotasFicasStatusEnviada().getAssincrono()) {
							break;
						}
					}
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				if (!getProgressBarEnviarNotasFicasStatusEnviada().getAssincrono()) {
					getProgressBarEnviarNotasFicasStatusEnviada().setForcarEncerramento(true);
				}
			}
		}
	}

	public void executarInicioProgressBarEnviarNotasFiscaisEnviadas() throws Exception {
		try {
			getProgressBarEnviarNotasFicasStatusEnviada().resetar();

			if (!getNotaFiscalSaidaVOs().isEmpty()) {
				Integer qtdeNota = 0;
				for (NotaFiscalSaidaVO nota : getNotaFiscalSaidaEnviadasVOs()) {
					if (nota.getNotaFiscalSaidaSelecionado()) {
						qtdeNota += 1;
					}
				}
				if (qtdeNota > 0) {
					getProgressBarEnviarNotasFicasStatusEnviada().iniciar(0l, qtdeNota, "Iniciando Envio da NF-e ", false, this, "executarEnviarNotasFiscaisEnviadas");
				} else {
					getProgressBarEnviarNotasFicasStatusEnviada().setForcarEncerramento(true);
				}

			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preencherTodosListaContasRecebidasEnviadas() throws Exception {
		getFacadeFactory().getNotaFiscalSaidaFacade().preencherTodosListaContasRecebidas(getNotaFiscalSaidaEnviadasVOs());
	}

	public void desmarcarTodosListaContasRecebidasEnviadas() throws Exception {
		getFacadeFactory().getNotaFiscalSaidaFacade().desmarcarTodosListaContasRecebidas(getNotaFiscalSaidaEnviadasVOs());
	}

	public Boolean getBuscarContasAReceber() {
		if (buscarContasAReceber == null) {
			buscarContasAReceber = false;
		}
		return buscarContasAReceber;
	}

	public void setBuscarContasAReceber(Boolean buscarContasAReceber) {
		this.buscarContasAReceber = buscarContasAReceber;
	}

	public Boolean getBuscarContasRecebidas() {
		if (buscarContasRecebidas == null) {
			buscarContasRecebidas = false;
		}
		return buscarContasRecebidas;
	}

	public void setBuscarContasRecebidas(Boolean buscarContasRecebidas) {
		this.buscarContasRecebidas = buscarContasRecebidas;
	}

	public String getTipoValorConsultaContasRecebidas() {
		if (tipoValorConsultaContasRecebidas == null) {
			tipoValorConsultaContasRecebidas = "";
		}
		return tipoValorConsultaContasRecebidas;
	}

	public void setTipoValorConsultaContasRecebidas(String tipoValorConsultaContasRecebidas) {
		this.tipoValorConsultaContasRecebidas = tipoValorConsultaContasRecebidas;
	}

	public String getTipoValorConsultaContasAReceber() {
		if (tipoValorConsultaContasAReceber == null) {
			tipoValorConsultaContasAReceber = "";
		}
		return tipoValorConsultaContasAReceber;
	}

	public void setTipoValorConsultaContasAReceber(String tipoValorConsultaContasAReceber) {
		this.tipoValorConsultaContasAReceber = tipoValorConsultaContasAReceber;
	}

	public String getTipoDataConsideratContasRecebidas() {
		if (tipoDataConsideratContasRecebidas == null) {
			tipoDataConsideratContasRecebidas = "";
		}
		return tipoDataConsideratContasRecebidas;
	}

	public void setTipoDataConsideratContasRecebidas(String tipoDataConsideratContasRecebidas) {
		this.tipoDataConsideratContasRecebidas = tipoDataConsideratContasRecebidas;
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}

	public Double getValorTotalGeralPis() {
		return valorTotalGeralPis;
	}

	public void setValorTotalGeralPis(Double valorTotalGeralPis) {
		this.valorTotalGeralPis = valorTotalGeralPis;
	}

	public Double getValorTotalGeralCofins() {
		return valorTotalGeralCofins;
	}

	public void setValorTotalGeralCofins(Double valorTotalGeralCofins) {
		this.valorTotalGeralCofins = valorTotalGeralCofins;
	}

	public Double getValorTotalGeralInss() {
		return valorTotalGeralInss;
	}

	public void setValorTotalGeralInss(Double valorTotalGeralInss) {
		this.valorTotalGeralInss = valorTotalGeralInss;
	}

	public Double getValorTotalGeralCsll() {
		return valorTotalGeralCsll;
	}

	public void setValorTotalGeralCsll(Double valorTotalGeralCsll) {
		this.valorTotalGeralCsll = valorTotalGeralCsll;
	}

	public Double getValorTotalGeralIr() {
		return valorTotalGeralIr;
	}

	public void setValorTotalGeralIr(Double valorTotalGeralIr) {
		this.valorTotalGeralIr = valorTotalGeralIr;
	}

	public void downloadXmlNota() throws Exception {
		NotaFiscalSaidaVO nota = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
		nota.setXmlEnvio(getFacadeFactory().getNotaFiscalSaidaFacade().consultarXmlEnvioNotaFiscal(nota.getCodigo()));
		try {
			File arquivoXml = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator);
			if (!arquivoXml.exists()) {
				arquivoXml.mkdirs();
			}
			int tamanho = 9;
			if (WebServicesNFSEEnum.NFSE_WEBSERVICE.equals(nota.getWebServicesNFSEEnum())) {
				tamanho = 20;
			}
			arquivoXml = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(nota.getNumeroNota(), tamanho) + ".xml");
			if (!arquivoXml.exists()) {
				PrintWriter xml = getFacadeFactory().getArquivoHelper().criarArquivoTexto(arquivoXml.getParent(), arquivoXml.getName(), false);

				xml.append(nota.getXmlEnvio());

				xml.flush();
				xml.close();
			}

			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivoXml.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivoXml.getPath().substring(0, arquivoXml.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);

			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void downloadXmlCancelamentos() throws Exception {
		NotaFiscalSaidaVO nota = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
		nota.setXmlCancelamento(getFacadeFactory().getNotaFiscalSaidaFacade().consultarXmlCancelamentoNotaFiscal(nota.getCodigo()));
		try {
			File arquivoXml = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_CANCELADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator);
			if (!arquivoXml.exists()) {
				arquivoXml.mkdirs();
			}
			arquivoXml = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_CANCELADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9) + ".xml");
			if (!arquivoXml.exists()) {
				PrintWriter xml = getFacadeFactory().getArquivoHelper().criarArquivoTexto(arquivoXml.getParent(), arquivoXml.getName(), false);

				xml.append(nota.getXmlCancelamento());

				xml.flush();
				xml.close();
			}

			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivoXml.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivoXml.getPath().substring(0, arquivoXml.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);

			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarStatusNotaFiscal() {
		NotaFiscalSaidaVO nota = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
		try {
//			nota.setMensagemRetorno(getFacadeFactory().getNotaFiscalSaidaFacade().consultarLoteEnviar(nota, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			nota.setMensagemRetorno(getFacadeFactory().getNotaFiscalSaidaFacade().consultarLoteEnviarWebservice(nota, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(nota, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public CartaCorrecaoVO getCartaCorrecaoVO() {
		if (cartaCorrecaoVO == null) {
			cartaCorrecaoVO = new CartaCorrecaoVO();
		}
		return cartaCorrecaoVO;
	}

	public void setCartaCorrecaoVO(CartaCorrecaoVO cartaCorrecaoVO) {
		this.cartaCorrecaoVO = cartaCorrecaoVO;
	}

	public String executarEnviarCartaCorrecaoNotaFiscalSaida() {
		try {
			getCartaCorrecaoVO().setNotafiscalsaidaVO(getNotaFiscalSaidaVO());
			getCartaCorrecaoVO().setUsuarioVO(getUsuarioLogadoClone());
			getCartaCorrecaoVO().setUnidadeEnsinoVO(getNotaFiscalSaidaVO().getUnidadeEnsinoVO());
			getCartaCorrecaoVO().setSequenciaCorrecao(getNotaFiscalSaidaVO().getCartaCorrecaoVOs().size() + 1);

			getFacadeFactory().getCartaCorrecaoNotaFiscalFacade().transmitirCartaCorrecao(getCartaCorrecaoVO(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo(), getUsuarioLogado());
			getNotaFiscalSaidaVOs().clear();
			setMensagemID("msq_CartaCorrecao_enviadaComSucesso");
			return Uteis.getCaminhoRedirecionamentoNavegacao("notaFiscalSaidaCons");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getNotaFiscalSaidaVOs().clear();
			return Uteis.getCaminhoRedirecionamentoNavegacao("notaFiscalSaidaCons");
		}
	}

	public void imprimirCartaCorrecao() {
		try {
			CartaCorrecaoVO obj = (CartaCorrecaoVO) context().getExternalContext().getRequestMap().get("carta");

			notaFiscalSaidaRelControle = null;
			notaFiscalSaidaRelControle = (NotaFiscalSaidaRelControle) context().getExternalContext().getSessionMap().get(NotaFiscalSaidaRelControle.class.getSimpleName());
			if (notaFiscalSaidaRelControle == null) {
				notaFiscalSaidaRelControle = new NotaFiscalSaidaRelControle();
				context().getExternalContext().getSessionMap().put(NotaFiscalSaidaRelControle.class.getSimpleName(), notaFiscalSaidaRelControle);
			}
			if (!obj.getCodigo().equals(0)) {
				getNotaFiscalSaidaRelControle().imprimirCartaCorrecao(obj);
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarObservacaoContribuinte() {
		try {
			setDataValidadeCertificado("Indefinida");
			setUnidadeEnsinoVO(getAplicacaoControle().getUnidadeEnsinoVO(getUnidadeEnsinoVO().getCodigo(), getUsuario()));
			setObservacaoContribuinte(getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal());			
			getNotaFiscalSaidaVOs().clear();
			validarCertificado();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public ProgressBarVO getProgressBarEnviarNotasFicasStatusEnviada() {
		if (progressBarEnviarNotasFicasStatusEnviada == null) {
			progressBarEnviarNotasFicasStatusEnviada = new ProgressBarVO();
		}
		return progressBarEnviarNotasFicasStatusEnviada;
	}

	public void setProgressBarEnviarNotasFicasStatusEnviada(ProgressBarVO progressBarEnviarNotasFicasStatusEnviada) {
		this.progressBarEnviarNotasFicasStatusEnviada = progressBarEnviarNotasFicasStatusEnviada;
	}

	public Boolean getConsultaDataScroller() {
		if (consultaDataScroller == null) {
			consultaDataScroller = false;
		}
		return consultaDataScroller;
	}

	public void setConsultaDataScroller(Boolean consultaDataScroller) {
		this.consultaDataScroller = consultaDataScroller;
	}

	public Boolean getDesabilitarFlagIssRetido() {
		NotaFiscalSaidaVO nota = (NotaFiscalSaidaVO) context().getExternalContext().getRequestMap().get("notaFiscalSaidaVOItens");
		if (nota.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor()) || nota.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			if (nota.getParceiroVO().getTipoEmpresa().equals("JU") || nota.getFornecedorVO().getTipoEmpresa().equals("JU")) {
				return false;
			}
		}

		return true;

	}

	public void verificarAprentarColunaIssRetino() {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getConfiguracaoNotaFiscalVO())) {
				if (unidadeEnsinoVO.getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
					setApresentarColunaIssRetido(true);
				} else {
					setApresentarColunaIssRetido(false);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getApresentarColunaIssRetido() {
		if (apresentarColunaIssRetido == null) {
			apresentarColunaIssRetido = false;
		}
		return apresentarColunaIssRetido;
	}

	public void setApresentarColunaIssRetido(Boolean apresentarColunaIssRetido) {
		this.apresentarColunaIssRetido = apresentarColunaIssRetido;
	}
	
	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
    
    public String getDataValidadeCertificado() {
    	if ((dataValidadeCertificado == null) || (dataValidadeCertificado.equals("Indefinida"))) {
    	try {
    		setDataValidadeCertificado("");
    		if (!Uteis.isAtributoPreenchido(dataValidadeCertificado) || dataValidadeCertificado.length() > 0) {
        		if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())) {
        			getUnidadeEnsinoVO().setCodigo(getListaUnidadeEnsinoVOs().get(0).getCodigo());
        		}
    			ConfiguracaoNotaFiscalVO configNotaFiscalVO = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
    			String dataVencimento = UteisData.getData(Assinador.getDataValidadeCertificado(UteisNfe.getCaminhoCertificado(configNotaFiscalVO, getConfiguracaoGeralPadraoSistema()) , configNotaFiscalVO.getSenhaCertificado()));;
    			Date data = UteisData.getData(dataVencimento);
    			dataValidadeCertificado = Uteis.getData(data);
    			if (data.after(new Date())) {
    				setSituacaoCertificado("bandeiraVermelha.png");
    				setMensagemDetalhada("msg_erro", "O certificado está vencido!");
    				throw new Exception(getMensagemDetalhada());
    			} else {
    				setSituacaoCertificado("bandeiraVerde.png");
    				setMensagemID("msg_certificado_notaOk");
    			}
    			return dataValidadeCertificado;
    		}
		} catch (Exception e) {
			setSituacaoCertificado("bandeiraVermelha.png");
			if (e instanceof FileNotFoundException) {
				setDataValidadeCertificado("Certificado Não Localizado");
				setMensagemDetalhada("msg_erro", "O certificado para a unidade " + getUnidadeEnsinoVO().getNome() + " não foi localizado, verifique a configuração da nota fiscal desta unidade.");
			} else if (e.getMessage().contains("Senha")) {
				setDataValidadeCertificado("Senha do certificado inválida.");
				setMensagemDetalhada("msg_erro", "A senha do certificado para a unidade " + getUnidadeEnsinoVO().getNome() + " está incorreta, verifique a configuração da nota fiscal desta unidade.");
			} 
			else if (e.getMessage().contains("Certificado expirado")){
				setMensagemDetalhada("msg_erro", "O certificado para a unidade " + getUnidadeEnsinoVO().getNome() + " está expirado!");
				setDataValidadeCertificado("Certificado Expirado");
			} else {
				setMensagemDetalhada("msg_erro", "O certificado para a unidade " + getUnidadeEnsinoVO().getNome() + " não foi localizado, verifique a configuração da nota fiscal desta unidade.");
				e.printStackTrace();
			}
    	
		}
    	}
    	return dataValidadeCertificado;
    }
    
    public void validarCertificado(){
    	setDataValidadeCertificado(null);
    }
	
	public List<SelectItem> getTipoConsultaComboTipoPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("-1", " "));
		itens.add(new SelectItem(TipoPessoa.ALUNO.getValor(), TipoPessoa.ALUNO.getDescricao()));
		itens.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), TipoPessoa.FUNCIONARIO.getDescricao()));
		itens.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor(), TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()));
		itens.add(new SelectItem(TipoPessoa.PARCEIRO.getValor(), TipoPessoa.PARCEIRO.getDescricao()));
		return itens;
	}

	public String getSituacaoCertificado() {
		if (situacaoCertificado == null) {
			situacaoCertificado = "";
		}
		return situacaoCertificado;
	}

	public void setSituacaoCertificado(String situacaoCertificado) {
		this.situacaoCertificado = situacaoCertificado;
	}

	public void setDataValidadeCertificado(String dataValidadeCertificado) {
		this.dataValidadeCertificado = dataValidadeCertificado;
	}

	public List<UnidadeEnsinoVO> getListaUnidadeEnsinoVOs() {
		if (listaUnidadeEnsinoVOs == null) {
			listaUnidadeEnsinoVOs = new ArrayList<>();
		}
		return listaUnidadeEnsinoVOs;
	}

	public void setListaUnidadeEnsinoVOs(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs) {
		this.listaUnidadeEnsinoVOs = listaUnidadeEnsinoVOs;
	}

    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getNotaFiscalSaidaVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }	
	
	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getNotaFiscalSaidaVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);		
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getNotaFiscalSaidaVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.NFSAIDA, this.getNotaFiscalSaidaVO().getDescricaoBloqueio(), this.getNotaFiscalSaidaVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getNotaFiscalSaidaVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioIncluirAlterarNFSaida", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 	}	
	
	public Boolean getApresentarDataValidadeCertificado() {
		if ((!this.getDataValidadeCertificado().equals("Indefinida")) &&
		    (!this.getDataValidadeCertificado().equals(""))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
    public void validarSelecaoNotaFiscalSaidaTotalizadores() {
		List<NotaFiscalSaidaVO> notas = getNotaFiscalSaidaVOs()
				.stream()
				.filter(NotaFiscalSaidaVO::getNotaFiscalSaidaSelecionado)
				.collect(Collectors.toList());
		setValorTotalGeralNota(notas.stream().mapToDouble(NotaFiscalSaidaVO::getValorTotal).sum());
		setValorTotalGeralIssqn(notas.stream().mapToDouble(NotaFiscalSaidaVO::getTotalIssqn).sum());
		setValorTotalGeralPis(notas.stream().mapToDouble(NotaFiscalSaidaVO::getValorTotalPIS).sum());
		setValorTotalGeralCofins(notas.stream().mapToDouble(NotaFiscalSaidaVO::getValorTotalCOFINS).sum());
		setValorTotalGeralInss(notas.stream().mapToDouble(NotaFiscalSaidaVO::getValorTotalINSS).sum());
		setValorTotalGeralCsll(notas.stream().mapToDouble(NotaFiscalSaidaVO::getValorTotalCSLL).sum());
		setValorTotalGeralIr(notas.stream().mapToDouble(NotaFiscalSaidaVO::getValorTotalIRRF).sum());
	}

	public ProgressBarVO getProgressBarNotasCompactadas() {
		if (progressBarNotasCompactadas == null) {
			progressBarNotasCompactadas = new ProgressBarVO();
		}
		return progressBarNotasCompactadas;
	}

	public void setProgressBarNotasCompactadas(ProgressBarVO progressBarNotasCompactadas) {
		this.progressBarNotasCompactadas = progressBarNotasCompactadas;
	}
    
    
}
