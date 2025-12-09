/**
 * 
 */
package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.administrativo.ComunicacaoInternaControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.LoginControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
//import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.RegistroAulaVO;
//import negocio.comuns.academico.RegistroEntregaArtefatoAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SimularAcessoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.biblioteca.TimeLineVO;
import negocio.comuns.biblioteca.enumeradores.TipoOrigemTimeLineEnum;

import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.protocolo.Requerimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
//import relatorio.negocio.comuns.academico.FichaAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRel;
//import relatorio.negocio.jdbc.academico.FichaAlunoRel;

/**
 * @author Carlos Eugênio
 *
 */
@Controller("FichaAlunoControle")
@Scope("viewScope")
@Lazy
public class FichaAlunoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private PessoaVO alunoVO;
	private MatriculaVO matriculaVO;
	private List<MatriculaVO> listaMatriculaVOs;
	private List<HistoricoVO> listaHistoricoVOs;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private Boolean imprimirContrato;
	private Integer tipoLayoutComprovanteMatricula;
//	private PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO;

	private String abrirModalImpressaoBoleto;
	private String tipoBoleto;

	private List<MatriculaVO> listaMatriculaAbaFinanceiroVOs;
	private TipoOrigemContaReceber tipoOrigemContaReceber;
	private String mesAno;
	private SituacaoContaReceber situacaoContaReceber;
	private List<SelectItem> listaSelectItemMesAnoContaReceberVOs;

	private List<MatriculaVO> listaMatriculaAbaRequerimentoVOs;
	private TipoRequerimentoVO tipoRequerimentoVO;
	private SituacaoRequerimento situacaoRequerimento;
	private List<SelectItem> listaSelectItemMesAnoRequerimentoVOs;
	private List<SelectItem> listaSelectItemTipoRequerimentoVOs;
	private RequerimentoVO requerimentoVO;
	
	private Boolean possuiPermissaoEnviarComunicadoInternoAluno;
	private Boolean possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro;
	
	private String situacaoBloqueioMatricula;
	private String situacaoCancelamentoBloqueioMatricula;

    private Boolean realizarAdiamentoSuspensaoMatricula;
	private Boolean apresentarBotaoEditarContaReceber ;
	private Boolean apresentarBotaoImprimirBoletoContaReceber;
	private Boolean apresentarBotaoExcluirContaReceber;
	private Boolean apresentarBotaoCancelarContaReceber;
	private Boolean apresentarBotaoEditarRequerimento;
	private Boolean apresentarBotaoAdicionarRequerimento;
	private Boolean ApresentarBotaoExcluirRequerimento;
	private Boolean apresentarBotaoLancamentoNota;
	private Boolean apresentarAbaNegativacaoCobranca;
	
	private List<MatriculaVO> listaMatriculaAbaBibliotecaVOs;
	private SituacaoItemEmprestimo situacaoItemEmprestimo;
	private List<SelectItem> listaSelectItemMesAnoItemEmprestimoVOs;
	private List<SelectItem> listaSelectItemTipoOrigemBibliotecaVOs;
	private String mesAnoBiblioteca;
	private TipoOrigemTimeLineEnum tipoOrigemTimeLineEnum;

	private UsuarioVO responsavelInteracaoWorkflowVO;
	private List<SelectItem> listaSelectItemMesAnoInteracaoWorkflowVOs;
	private List<SelectItem> listaSelectItemResponsavelInteracaoWorkflowVOs;

	
	private List<SelectItem> listaSelectItemMesAnoComunicacaoInternaVOs;
	
	
	private boolean alunoSimulacaoValido = false;
	private List<List<RegistroAulaVO>> listaFaltas;
	private List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs;
//	private List<RegistroEntregaArtefatoAlunoVO> listaArtefatosEntregueAluno;
	
	private Boolean centralAluno;
	private Boolean permitirVisualizarDadosMatricula;
	private Boolean permitirVisualizarDadosFinanceiro;
	private Boolean permitirVisualizarDadosRequerimento;
	private Boolean permitirVisualizarDadosBiblioteca;
	private Boolean permitirVisualizarDadosCRM;
	private Boolean permitirVisualizarDadosProcessoSeletivo;
	private Boolean permitirVisualizarDadosComunicacaoInterna;
	private String mensagemListaHistoricoDisciplinas;
	private Integer quantidadeEmprestimoAberto;
	private Integer quantidadeEmprestimoAtrasado;
	private Integer quantidadeReservaPendente;
	private String  valorTotalMulta;
	private List<TimeLineVO> listaTimeLineVOs;
	private List<TimeLineVO> listaReservaTimeLineVOs;
	private List<TimeLineVO> dadosTimeLineUsuario;
	private List<TimeLineVO> dadosTimeLineUsuarioRemovido;
	private List<DocumentacaoGEDVO> listaDocumentacaoGED;
	private Boolean possuiPerfilAcessoDocumentacaoGED;
	public TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum;
	private List<SelectItem> listaSelectItemTipoOrigemDocumentoAssinadoEnum;
	private List<SelectItem> listaSelectItemMatriculaVOs;
	private String matriculaSelecionada;
	private DataModelo listaDocumentosPendentes;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private DataModelo listaDocumentosAssinados;
	private DataModelo listaDocumentosRejeitados;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum;
	private List<SelectItem> listaComboboxMatriculaVOs;
	private String filtroMatricula;
	
	public FichaAlunoControle() {
		super();
	}

	@PostConstruct
	public void realizarCarregamentoDadosAluno() {
		PessoaVO aluno = (PessoaVO) context().getExternalContext().getSessionMap().get("alunoFichaVO");
		context().getExternalContext().getSessionMap().remove("alunoFichaVO");
		if(aluno == null && ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("aluno") != null){
			aluno = new PessoaVO();
			aluno.setCodigo(Integer.valueOf(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("aluno")));			
			
		}
		if (aluno != null && !aluno.getCodigo().equals(0)) {
			try {
				getFacadeFactory().getPessoaFacade().carregarDados(aluno, NivelMontarDados.BASICO, getUsuarioLogado());
				aluno.setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarPorCodigoPessoaTipo(aluno.getCodigo(), "", false, getUsuarioLogado()));
				setAlunoVO(aluno);
				consultarDadosAcademico(getAlunoVO());
				inicializarDadosFotoUsuario();
//				setApresentarAbaNegativacaoCobranca(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().verificarMatriculaPossuiNegativacaoCobranca(getAlunoVO().getCodigo()));				
				this.verificarPermissoesUsuario();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}finally {
				if((PessoaVO) context().getExternalContext().getSessionMap().get("alunoFichaVO") != null){
					context().getExternalContext().getSessionMap().remove("alunoFichaVO");
				}
			}
		}		
		if(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("idControlador") != null) {
			setIdControlador(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("idControlador"));			
		}else {
		setIdControlador(FichaAlunoControle.class.getSimpleName()+"_"+aluno.getCodigo().toString());
		}
		carregarPermissoesExibicaoAbasFichaAluno();
		LayoutPadraoVO layoutPadraoVO = null;
		try {
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(FichaAlunoControle.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", false, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (Uteis.isAtributoPreenchido(layoutPadraoVO)) {
			setVersaoNova(Boolean.valueOf(layoutPadraoVO.getValor()));
			setVersaoAntiga(!Boolean.valueOf(layoutPadraoVO.getValor()));
		} else {
			setVersaoAntiga(false);
			setVersaoNova(true);
		}
		LayoutPadraoVO layoutPadraoVO1 = null;
		try {
			layoutPadraoVO1 = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(FichaAlunoControle.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNovaFinanceiro", false, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (Uteis.isAtributoPreenchido(layoutPadraoVO1)) {
			setVersaoNovaFinanceiro(Boolean.valueOf(layoutPadraoVO1.getValor()));
			setVersaoAntigaFinanceiro(!Boolean.valueOf(layoutPadraoVO1.getValor()));
		} else {
			setVersaoAntigaFinanceiro(false);
			setVersaoNovaFinanceiro(true);
		}
	}
	
	public void consultarDadosAcademico(PessoaVO alunoVO) throws Exception {
		setListaMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultarPorCodigoAlunoDadosFichaAluno(alunoVO.getCodigo(),getFiltroMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
		setListaSelectItemMatriculaVOs(new ArrayList<>());
		boolean primeiro = true;
		if(!Uteis.isAtributoPreenchido(getFiltroMatricula())) {
			setListaComboboxMatriculaVOs(null);
		}
		for (MatriculaVO matriculaVO : getListaMatriculaVOs()) {
			if (primeiro) {
				setMatriculaSelecionada(matriculaVO.getMatricula());
				primeiro = false;
			}
			getListaSelectItemMatriculaVOs().add(new SelectItem(matriculaVO.getMatricula(), matriculaVO.getMatricula()));
			if(!Uteis.isAtributoPreenchido(getFiltroMatricula())) {
				getListaComboboxMatriculaVOs().add(new SelectItem(matriculaVO.getMatricula(), matriculaVO.getMatricula() + " - " + matriculaVO.getCurso().getNome()+" ("+matriculaVO.getSituacao_Apresentar()+")"));
			}
		}
	}

	public void consultarDisciplinaHistoricoPorMatriculaPerdiodo() {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
		MatriculaVO matriVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		setMatriculaPeriodoVO(obj);
		try {
			setListaHistoricoVOs(getFacadeFactory().getHistoricoFacade().consultarHistoricoPorMatriculaPeriodoFichaAluno(obj.getCodigo(), 0, true, getUsuarioLogado()));
			if (getListaHistoricoVOs().isEmpty()) {
				matriVO.setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(matriVO.getGradeCurricularAtual().getCodigo(), 
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				setMensagemListaHistoricoDisciplinas("Não foram encontradas disciplinas vinculadas ou aproveitadas para a grade atual: " + matriVO.getGradeCurricularAtual().getNome() + ".");
			} else {
				setMensagemListaHistoricoDisciplinas("");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaHistoricoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosNavegacaoTelaDadosMatricula() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("HistoricoAlunoRelControle");
			removerControleMemoriaFlashTela("GradeCurricularAlunoRelControle");
			removerControleMemoriaFlashTela("AlteracoesCadastraisMatriculaControle");
			removerControleMemoriaFlashTela("ProcessoInclusaoExclusaoDisciplinaMatriculaControle");
			removerControleMemoriaFlashTela("DocumetacaoMatriculaControle");
			removerControleMemoriaFlashTela("RequerimentoControle");
			context().getExternalContext().getSessionMap().put("matriculaFichaAluno", obj);
		}
	}
	
	public void buscarMatriculaAtiva() {		
		for (MatriculaVO obj : getListaMatriculaVOs()) {
			if(obj.getSituacao().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) ) {
				removerControleMemoriaFlashTela("RequerimentoControle");
				context().getExternalContext().getSessionMap().put("matriculaFichaAlunoParaRequerimento", obj);
			}
		}
	}
	
	public void realizarNavegacaoTelaAluno() {
		if (getAlunoVO() != null && !getAlunoVO().getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("AlunoControle");
			context().getExternalContext().getSessionMap().put("alunoFichaAluno", getAlunoVO());
		}
	}

	public void realizarNavegacaoTelaResponsavelFinanceiro() {
		PessoaVO responsavelFinanceiroVO = getAlunoVO().getResponsavelFinanceiroAluno();
		if (responsavelFinanceiroVO != null && !responsavelFinanceiroVO.getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("AlunoControle");
			context().getExternalContext().getSessionMap().put("responsavelFinanceiroFichaAluno", responsavelFinanceiroVO);
		}
	}

	public void realizarNavegacaoTelaLancamentoNota() {
		HistoricoVO obj = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("LancamentoNotaControle");
			context().getExternalContext().getSessionMap().put("historicoFichaAluno", obj);
		}
	}

	public void realizarNavegacaoTelaBoletimAcademico() {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("BoletimAcademicoRelControle");
			context().getExternalContext().getSessionMap().put("matriculaPeriodoFichaAluno", obj);
		}
	}

	public void realizarImpressaoContratoMatricula() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null) {
			removerControleMemoriaFlashTela("ImpressaoContratoControle");
			context().getExternalContext().getSessionMap().put("matriculaFichaAluno", obj);
		}
//		limparMensagem();
//		if (obj != null && !obj.getCodigo().equals(0)) {
//			try {				
//				getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatriculaVO(), getUsuarioLogado());
//				setCaminhoRelatorio(getFacadeFactory().getImpressaoContratoFacade().imprimirContratoRenovarMatricula(obj.getMatriculaVO().getTipoMatricula(), obj.getMatriculaVO(), obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getMatriculaVO().getUnidadeEnsino().getCodigo()), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
//			if (!Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
//				setImprimirContrato(true);
//				setFazerDownload(false);
//			}
//			if (Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
//				setImprimirContrato(false);
//				setFazerDownload(true);
//			}			
//			} catch (Exception e) {
//				setMensagemDetalhada("msg_erro", e.getMessage());
//			}
//		}
	}

	public void realizarImpressaoComprovanteMatricula() {
		if (getMatriculaPeriodoVO() != null && !getMatriculaPeriodoVO().getCodigo().equals(0)) {
			try {			
				imprimirComprovanteMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}	
		}
	}

	public void imprimirComprovanteMatricula() {
		String titulo = null;
		String design = null;
		String nomeRelatorio = null;
		String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
		List listaObjetos = null;
		try {
			if (getTipoLayoutComprovanteMatricula() == 2) {
				titulo = "FICHA DE INSCRIÇÃO";
				design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().substring(0, ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().lastIndexOf(".")) + getTipoLayoutComprovanteMatricula() + ".jrxml";
				nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade() + getTipoLayoutComprovanteMatricula();
			} else {
				if (getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNivelEducacional().equals("PO") || getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNivelEducacional().equals("EX")) {
					titulo = "FICHA DE INSCRIÇÃO";
					design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio();
					nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade();
				} else {
					titulo = "COMPROVANTE DE RENOVAÇÃO DE MATRÍCULA";
					ConfiguracoesVO configuracoesVO = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, super.getUnidadeEnsinoLogado().getCodigo());
					if (configuracoesVO == null || configuracoesVO.getLayoutPadraoComprovanteMatricula() == null || configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_01)) {
						design = ComprovanteRenovacaoMatriculaRel.getDesignComprovanteMatriculaIReportRelatorio();
						nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidadeComprovanteMatricula();
					} else if (configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_02)) {
						design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "ComprovanteRenovacaoMatriculaRelLayout2" + ".jrxml");
						nomeRelatorio = "ComprovanteRenovacaoMatriculaRelLayout2";
					}

				}
			}

			listaObjetos = getFacadeFactory().getComprovanteRenovacaoMatriculaRelFacade().criarObjeto(getMatriculaPeriodoVO().getCodigo(), getMatriculaPeriodoVO().getMatricula(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
				if (ue.getExisteLogoRelatorio()) {
					String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
					String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
				} else {
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
			context().getExternalContext().getSessionMap().remove(RenovarMatriculaControle.class.getSimpleName());
		}
	}

	public void verificarLayoutPadraoComprovanteMatricula() throws Exception {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			setMatriculaPeriodoVO((MatriculaPeriodoVO)obj.clone());
			LayoutPadraoVO layoutPadraoVO = layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("comprovanteMatricula", "tipoDesignRelatorioComprovanteMatricula", false, getUsuarioLogado());
			if (!layoutPadraoVO.getValor().equals("")) {
				setTipoLayoutComprovanteMatricula(Integer.parseInt(layoutPadraoVO.getValor()));
			}
		}
	}

	public List getComboBoxTipoLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("1", "Layout 1"));
		itens.add(new SelectItem("2", "Layout 2"));
		return itens;
	}

	

	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545)";
		} else if (getFazerDownload()) {
			return getDownload();
		}
		return "";
	}

	public void realizarNavegacaoTelaHorarioAula() {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("HorarioAulaAlunoControle");
			context().getExternalContext().getSessionMap().put("matriculaPeriodoFichaAluno", obj);
		}
	}

	public void realizarVisualizacaoPlanoFinanceiro() {
//		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
//		if (obj != null && !obj.getCodigo().equals(0)) {
//			try {
//				setPlanoFinanceiroAlunoVO(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodoFichaAluno(obj.getCodigo(), getUsuarioLogado()));
//				setMensagemID("msg_dados_consultados");
//			} catch (Exception e) {
//				setMensagemDetalhada("msg_erro", e.getMessage());
//			}
//		}
	}

	
	

	public Boolean verificarUsuarioPossuiPermissaoImprimirBoletoBloqueadoMatriculaAlunoComDebitos() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ContaReceber_permitirImpressaoBoletoBloqueadoMatriculaAlunoComDebitos", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
		}
		return Boolean.FALSE;
	}

	

	public String definirBoletoParaImpressao() {
		return "PF('panelCobrarReimpressao').hide()";
	}

	
	private List<SelectItem> tipoConsultaComboTipoBoleto;

	public List<SelectItem> getTipoConsultaComboTipoBoleto() {
		if (tipoConsultaComboTipoBoleto == null) {
			tipoConsultaComboTipoBoleto = new ArrayList<SelectItem>();
			tipoConsultaComboTipoBoleto.add(new SelectItem("boleto", "Boleto"));
			tipoConsultaComboTipoBoleto.add(new SelectItem("boletoSegundo", "Boleto (LayOut 2)"));
			tipoConsultaComboTipoBoleto.add(new SelectItem("carne", "Carnê"));
		}
		return tipoConsultaComboTipoBoleto;
	}

	

	
	public void consultarDadosAbaArtefatosEntregaAluno() {
//		try {
//			getListaArtefatosEntregueAluno().clear();
//			setListaArtefatosEntregueAluno(getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().consultarPorAluno(getAlunoVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}
	
	public void consultarDadosAbaFinanceiraPorAluno() {
//		try {
//			consultarContaReceberPorMatriculaAluno();
//			setListaSelectItemMesAnoContaReceberVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosListaSelectItemMesAnoBaseadoContaReceber(getAlunoVO().getCodigo(), getUsuarioLogado()));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}
	
	public void consultarContaReceberPorMatriculaAluno() {
//		try {
//			getListaMatriculaAbaFinanceiroVOs().clear();
//			setListaMatriculaAbaFinanceiroVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosContaReceberAbaFinanceira(getAlunoVO().getCodigo(), getListaMatriculaVOs(), getTipoOrigemContaReceber(), getSituacaoContaReceber(), getMesAno(), getUsuarioLogado()));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public void realizarNavegacaoContaReceberIncluirNovaConta() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("ContaReceberControle");
			context().getExternalContext().getSessionMap().put("matriculaIncluirNovaContaFichaAluno", obj);
		}
	}

	public void realizarNavegacaoRenegociacao() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("NegociacaoContaReceberControle");
			context().getExternalContext().getSessionMap().put("matriculaRenegociacaoContaReceberFichaAluno", obj);
		}
	}

	public void realizarNavegacaoDeclaracaoImpostoRenda() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("DeclaracaoImpostoRendaRelControle");
			context().getExternalContext().getSessionMap().put("matriculaImprimirDeclaracaoImpostoRendaFichaAluno", obj);
		}
	}

	public void realizarNavegacaoCartaCobranca() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("CartaCobrancaRelControle");
			context().getExternalContext().getSessionMap().put("matriculaImprimirCartaCobrancaFichaAluno", obj);
		}
	}

	public void realizarNavegacaoRecebimentoCarregandoTodasContasAReceber() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("NegociacaoRecebimentoControle");
			context().getExternalContext().getSessionMap().put("matriculaRealizarRecebimentoTodasContasAReceberFichaAluno", obj);
		}
	}

	public void consultarDadosAbaRequerimentoPorAluno() {
//		try {
//			consultarRequerimentoPorMatriculaAluno();
//			setListaSelectItemMesAnoRequerimentoVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosListaSelectItemMesAnoBaseadoRequerimento(getAlunoVO().getCodigo(), getUsuarioLogado()));
//			montarListaSelectItemTipoRequerimento();
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public void consultarRequerimentoPorMatriculaAluno() {
		try {
			getListaMatriculaAbaRequerimentoVOs().clear();
			setListaMatriculaAbaRequerimentoVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosRequerimentoFichaAluno(getAlunoVO().getCodigo(), getListaMatriculaVOs(), getTipoRequerimentoVO().getCodigo(), getSituacaoRequerimento() != null ? getSituacaoRequerimento().getValor() : "", getMesAno(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarNavegacaoRequerimentoInclusaoNovoRequerimento() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			context().getExternalContext().getSessionMap().put("requerimentoIncluirNovoRequerimentoFichaAluno", obj);
		}
	}

	public void realizarNavegacaoRequerimentoEditarDados() {
		RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("RequerimentoControle");
			context().getExternalContext().getSessionMap().put("requerimentoEditarRequerimentoFichaAluno", obj);
		}
	}



	public void inicializarDadosRequerimento() {
		RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			setRequerimentoVO(obj);
		}
	}

	public void excluirRequerimento() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Inicializando Excluir Requerimento", "Excluindo");
			validarDadosExclusaoRequerimento(getRequerimentoVO());
			getFacadeFactory().getRequerimentoFacade().excluir(getRequerimentoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);
			consultarRequerimentoPorMatriculaAluno();
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Excluir requerimento", "Excluindo");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosExclusaoRequerimento(RequerimentoVO requerimentoVO) throws Exception {
		if (requerimentoVO.getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())) {
			throw new Exception("Este Requerimento Não pode ser excluído!");
		}
	}

	public void montarListaSelectItemTipoRequerimento() {
		List<TipoRequerimentoVO> listaTipoRequerimentoVOs = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoPorAlunoFichaAluno(getAlunoVO().getCodigo(), getUsuarioLogado());
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		for (TipoRequerimentoVO tipoRequerimentoVO : listaTipoRequerimentoVOs) {
			itens.add(new SelectItem(tipoRequerimentoVO.getCodigo(), tipoRequerimentoVO.getNome()));
		}
		setListaSelectItemTipoRequerimentoVOs(itens);
	}

	public List<SelectItem> getListaSelectItemSituacaoRequerimentoVOs() {
		return SituacaoRequerimento.getListaSelectItemSituacaoRequerimentoVOs(false);
	}
	
	public void realizarNavegacaoComunicadoInterno() {
		removerControleMemoriaFlashTela("ComunicacaoInternaControle");
		if (!getAlunoVO().getCodigo().equals(0)) {
			ComunicacaoInternaControle comunicacaoInternaControle = (ComunicacaoInternaControle) context().getExternalContext().getSessionMap().get("ComunicacaoInternaControle");
			if (comunicacaoInternaControle == null) {
				try {
					comunicacaoInternaControle = new ComunicacaoInternaControle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			comunicacaoInternaControle.realizarCarregamentoComunicacaoInternaFichaAluno(getAlunoVO());
		}
	}
	
	public void verificarUsuarioPossuiPermissaoEnviarComunicadoInternoParaAluno(String identificadorAcaoPermissao) {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
			setPossuiPermissaoEnviarComunicadoInternoAluno(true);
		} catch (Exception e) {
			setPossuiPermissaoEnviarComunicadoInternoAluno(false);
		}
		
	}

	public void consultarDadosAbaBibliotecaPorAluno() {
		try {
			getDadosTimeLineUsuarioRemovido().clear();
			getListaTimeLineVOs().clear();
			setMesAnoBiblioteca("");
			setTipoOrigemTimeLineEnum(null);
			setQuantidadeEmprestimoAberto(0);
			setQuantidadeEmprestimoAtrasado(0);
//			setListaTimeLineVOs(getFacadeFactory().getMinhaBibliotecaInterfaceFacade().listarDadosTimeLine(getAlunoVO().getCodigo() , getUsuarioLogado()));
			Double valorMulta = 0.0;
//			for (TimeLineVO obj : getListaTimeLineVOs()) {
//				
//				if (obj.getTipoEmprestimo().equals("ATRASADO") && !obj.getSituacaoEmprestimo().equals("DE")) {
//					setQuantidadeEmprestimoAtrasado(getQuantidadeEmprestimoAtrasado() + 1);
//					obj.setValorMulta(getFacadeFactory().getMinhaBibliotecaInterfaceFacade().realizarCalculoMultaParcial(obj, TipoPessoa.getEnum(obj.getTipoPessoaEmprestimo()), obj.getConfiguracaoBibliotecaVO(), obj.getCidadeBibliotecaVO()));
//				}
				
//				if (obj.getSituacaoEmprestimo().equals("AT") || obj.getSituacaoEmprestimo().equals("EX")) {
//					setQuantidadeEmprestimoAberto(getQuantidadeEmprestimoAberto() + 1);
//				}
//				
//				if (obj.getReserva() == true && (obj.getSituacaoReserva().equals("EX") || obj.getSituacaoReserva().equals("DI"))) {
//					setQuantidadeReservaPendente(getQuantidadeReservaPendente() + 1);
//				}
				
//				if (obj.getMultaPaga() == false) {
//					valorMulta += obj.getValorMulta();
//				}
//			}
			
			
			setQuantidadeReservaPendente(0);
//			setListaReservaTimeLineVOs(getFacadeFactory().getMinhaBibliotecaInterfaceFacade().listarDadosTimeLineReservas(getAlunoVO().getCodigo() , getUsuario()));
			for (TimeLineVO obj : getListaReservaTimeLineVOs()) {			
					if (obj.getReserva() && (obj.getSituacaoReserva().equals("EX") || obj.getSituacaoReserva().equals("DI"))) {
						setQuantidadeReservaPendente(getQuantidadeReservaPendente() + 1);
					}				
				}
			
			
			setDadosTimeLineUsuario(null); 
			UsuarioVO usuarioVO =  new UsuarioVO();
			usuarioVO.getPessoa().setCodigo(getAlunoVO().getCodigo());
//			getDadosTimeLineUsuario().addAll(getFacadeFactory().getMinhaBibliotecaInterfaceFacade().montarDadosTimeLine(usuarioVO));
			
			List<String> listAnoMes =  new ArrayList<String>(0);
			List<TipoOrigemTimeLineEnum> listTipoOrigem =  new ArrayList<TipoOrigemTimeLineEnum>(0);
			for(TimeLineVO obj : getDadosTimeLineUsuario()) {
				if(!listAnoMes.contains(obj.getMesAno())) {
					listAnoMes.add(obj.getMesAno());
				}
				if(!listTipoOrigem.contains(obj.getTipoOrigemTimeLine())) {
					listTipoOrigem.add(obj.getTipoOrigemTimeLine());
				}
				if(obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.CONTA_RECEBER_VENCIMENTO) && (obj.getDataRecebimentoContaReceber() == null || obj.getDataRecebimentoContaReceber().isEmpty()) ) {
					valorMulta += obj.getValorRecebido();
					getListaTimeLineVOs().add(obj);
				}
				if(obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.EMPRESTIMO_ATRASADO)) {		
					getListaTimeLineVOs().add(obj);
					setQuantidadeEmprestimoAtrasado(getQuantidadeEmprestimoAtrasado() + 1);
//					obj.setValorMulta(getFacadeFactory().getMinhaBibliotecaInterfaceFacade().realizarCalculoMultaParcial(obj, TipoPessoa.getEnum(obj.getTipoPessoaEmprestimo()), obj.getConfiguracaoBibliotecaVO(), obj.getCidadeBibliotecaVO()));
				}
				if(obj.getTipoOrigemTimeLine().equals(TipoOrigemTimeLineEnum.EMPRESTIMO_EM_DIA)) {		
					getListaTimeLineVOs().add(obj);
					setQuantidadeEmprestimoAberto(getQuantidadeEmprestimoAberto() + 1);
				}
			}
			setValorTotalMulta(Uteis.getDoubleFormatado(valorMulta));
			getListaSelectItemMesAnoItemEmprestimoVOs().clear();
			getListaSelectItemMesAnoItemEmprestimoVOs().add(new SelectItem("", ""));
			for(String mesAno : listAnoMes) {
				getListaSelectItemMesAnoItemEmprestimoVOs().add(new SelectItem(mesAno, mesAno));
			}
			
			getListaSelectItemTipoOrigemBibliotecaVOs().clear();
			getListaSelectItemTipoOrigemBibliotecaVOs().add(new SelectItem(null, ""));
			for(TipoOrigemTimeLineEnum timeLineEnum : listTipoOrigem) {
				getListaSelectItemTipoOrigemBibliotecaVOs().add(new SelectItem(timeLineEnum, timeLineEnum.getValorApresentar()));
			}
			//consultarItemEmprestimoPorMatriculaAluno();
			//setListaSelectItemMesAnoItemEmprestimoVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosListaSelectItemMesAnoBaseadoItemEmprestimo(getAlunoVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarItemEmprestimoPorMatriculaAluno() {
		try {
			//getListaMatriculaAbaBibliotecaVOs().clear();
			//setListaMatriculaAbaBibliotecaVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosBibliotecaFichaAluno(getAlunoVO().getCodigo(), getListaMatriculaVOs(), getSituacaoItemEmprestimo() != null ? getSituacaoItemEmprestimo().getValor() : "", getMesAno(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarNavegacaoEmprestimoDevolucao() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("EmprestimoControle");
			context().getExternalContext().getSessionMap().put("matriculaEmprestimoFichaAluno", obj);
		}
	}

	public void realizarNavegacaoImpressaoEmprestimo() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("EmprestimoBibliotecaRelControle");
			context().getExternalContext().getSessionMap().put("matriculaImpressaoEmprestimoFichaAluno", obj);
		}
	}

	

	

	public void montarListaSelectItemResponsavelInteracaoWorkFlow() {
		List<UsuarioVO> listaResponsavelInteracaoWorkflowVOs = getFacadeFactory().getUsuarioFacade().consultarResponsavelInteracaoWorkflowPorAlunoFichaAluno(getAlunoVO().getCodigo(), getUsuarioLogado());
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		for (UsuarioVO usuarioVO : listaResponsavelInteracaoWorkflowVOs) {
			itens.add(new SelectItem(usuarioVO.getCodigo(), usuarioVO.getPessoa().getNome()));
		}
		setListaSelectItemResponsavelInteracaoWorkflowVOs(itens);
	}

	

	public void consultarDadosAbaProcessoSeletivoPorAluno() {
		try {
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	

	public void consultarDadosAbaComunicacaoInternaPorAluno() {
		try {
			consultarComunicacaoInternaPorAluno();
			setListaSelectItemMesAnoComunicacaoInternaVOs(getFacadeFactory().getMatriculaFacade().inicializarDadosListaSelectItemMesAnoBaseadoComunicacaoInterna(getAlunoVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private DataModelo controleConsultaComunicadoInterno;
	
	public DataModelo getControleConsultaComunicadoInterno() {
		if(controleConsultaComunicadoInterno == null) {
			controleConsultaComunicadoInterno = new DataModelo();
			controleConsultaComunicadoInterno.setLimitePorPagina(10);			
			controleConsultaComunicadoInterno.setPaginaAtual(1);			
			controleConsultaComunicadoInterno.setPage(0);			
		}
		return controleConsultaComunicadoInterno;
	}

	public void setControleConsultaComunicadoInterno(DataModelo controleConsultaComunicadoInterno) {
		this.controleConsultaComunicadoInterno = controleConsultaComunicadoInterno;
	}

	public void consultarComunicacaoInternaPorAluno() {
		try {
			
			getControleConsultaComunicadoInterno().setListaConsulta(getFacadeFactory().getComunicacaoInternaFacade().consultarComunicacaoInternaPorAlunoFichaAluno(getAlunoVO(), getMesAno(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), getControleConsultaComunicadoInterno().getLimitePorPagina(), getControleConsultaComunicadoInterno().getOffset()));
			getControleConsultaComunicadoInterno().setTotalRegistrosEncontrados(getFacadeFactory().getComunicacaoInternaFacade().consultarTotalRegistroComunicacaoInternaPorAlunoFichaAluno(getAlunoVO().getCodigo(), getMesAno(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void paginarComunicadoInterno() {
		
		consultarComunicacaoInternaPorAluno();	
	}

	public void realizarEdicaoComunicadoInterno() {
		ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap().get("comunicacaoInternaItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("comunicacaoInternaFichaAluno", obj);
		}	
	}

	public PessoaVO getAlunoVO() {
		if (alunoVO == null) {
			alunoVO = new PessoaVO();
		}
		return alunoVO;
	}

	public void setAlunoVO(PessoaVO alunoVO) {
		this.alunoVO = alunoVO;
	}

	public List<MatriculaVO> getListaMatriculaVOs() {
		if (listaMatriculaVOs == null) {
			listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaVOs;
	}

	public void setListaMatriculaVOs(List<MatriculaVO> listaMatriculaVOs) {
		this.listaMatriculaVOs = listaMatriculaVOs;
	}

	public List<HistoricoVO> getListaHistoricoVOs() {
		if (listaHistoricoVOs == null) {
			listaHistoricoVOs = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricoVOs;
	}

	public void setListaHistoricoVOs(List<HistoricoVO> listaHistoricoVOs) {
		this.listaHistoricoVOs = listaHistoricoVOs;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = false;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public Integer getTipoLayoutComprovanteMatricula() {
		if (tipoLayoutComprovanteMatricula == null) {
			tipoLayoutComprovanteMatricula = 1;
		}
		return tipoLayoutComprovanteMatricula;
	}

	public void setTipoLayoutComprovanteMatricula(Integer tipoLayoutComprovanteMatricula) {
		this.tipoLayoutComprovanteMatricula = tipoLayoutComprovanteMatricula;
	}

//	public PlanoFinanceiroAlunoVO getPlanoFinanceiroAlunoVO() {
//		if (planoFinanceiroAlunoVO == null) {
//			planoFinanceiroAlunoVO = new PlanoFinanceiroAlunoVO();
//		}
//		return planoFinanceiroAlunoVO;
//	}
//
//	public void setPlanoFinanceiroAlunoVO(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO) {
//		this.planoFinanceiroAlunoVO = planoFinanceiroAlunoVO;
//	}

	

	

	public String getAbrirModalImpressaoBoleto() {
		if (abrirModalImpressaoBoleto == null) {
			abrirModalImpressaoBoleto = "";
		}
		return abrirModalImpressaoBoleto;
	}

	public void setAbrirModalImpressaoBoleto(String abrirModalImpressaoBoleto) {
		this.abrirModalImpressaoBoleto = abrirModalImpressaoBoleto;
	}

	public String getTipoBoleto() {
		if (tipoBoleto == null) {
			tipoBoleto = "boleto";
		}
		return tipoBoleto;
	}

	public void setTipoBoleto(String tipoBoleto) {
		this.tipoBoleto = tipoBoleto;
	}

	public List<MatriculaVO> getListaMatriculaAbaFinanceiroVOs() {
		if (listaMatriculaAbaFinanceiroVOs == null) {
			listaMatriculaAbaFinanceiroVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaAbaFinanceiroVOs;
	}

	public void setListaMatriculaAbaFinanceiroVOs(List<MatriculaVO> listaMatriculaAbaFinanceiroVOs) {
		this.listaMatriculaAbaFinanceiroVOs = listaMatriculaAbaFinanceiroVOs;
	}

	public List<SelectItem> getListaSelectItemTipoOrigemContaReceberVOs() {
		return TipoOrigemContaReceber.getListaSelectItemTipoOrigemContaReceberVOs();
	}

	public List<SelectItem> getListaSelectItemSituacaoContaReceberVOs() {
		return SituacaoContaReceber.getListaSelectItemSituacaoContaReceberVOs();
	}

	public TipoOrigemContaReceber getTipoOrigemContaReceber() {
		return tipoOrigemContaReceber;
	}

	public void setTipoOrigemContaReceber(TipoOrigemContaReceber tipoOrigemContaReceber) {
		this.tipoOrigemContaReceber = tipoOrigemContaReceber;
	}

	public String getMesAno() {
		if (mesAno == null) {
			mesAno = "";
		}
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public SituacaoContaReceber getSituacaoContaReceber() {
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(SituacaoContaReceber situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public List<SelectItem> getListaSelectItemMesAnoContaReceberVOs() {
		if (listaSelectItemMesAnoContaReceberVOs == null) {
			listaSelectItemMesAnoContaReceberVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMesAnoContaReceberVOs;
	}

	public void setListaSelectItemMesAnoContaReceberVOs(List<SelectItem> listaSelectItemMesAnoContaReceberVOs) {
		this.listaSelectItemMesAnoContaReceberVOs = listaSelectItemMesAnoContaReceberVOs;
	}

	public List<MatriculaVO> getListaMatriculaAbaRequerimentoVOs() {
		if (listaMatriculaAbaRequerimentoVOs == null) {
			listaMatriculaAbaRequerimentoVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaAbaRequerimentoVOs;
	}

	public void setListaMatriculaAbaRequerimentoVOs(List<MatriculaVO> listaMatriculaAbaRequerimentoVOs) {
		this.listaMatriculaAbaRequerimentoVOs = listaMatriculaAbaRequerimentoVOs;
	}

	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if (tipoRequerimentoVO == null) {
			tipoRequerimentoVO = new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}

	public SituacaoRequerimento getSituacaoRequerimento() {
		return situacaoRequerimento;
	}

	public void setSituacaoRequerimento(SituacaoRequerimento situacaoRequerimento) {
		this.situacaoRequerimento = situacaoRequerimento;
	}

	public List<SelectItem> getListaSelectItemMesAnoRequerimentoVOs() {
		if (listaSelectItemMesAnoRequerimentoVOs == null) {
			listaSelectItemMesAnoRequerimentoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMesAnoRequerimentoVOs;
	}

	public void setListaSelectItemMesAnoRequerimentoVOs(List<SelectItem> listaSelectItemMesAnoRequerimentoVOs) {
		this.listaSelectItemMesAnoRequerimentoVOs = listaSelectItemMesAnoRequerimentoVOs;
	}

	public List<SelectItem> getListaSelectItemTipoRequerimentoVOs() {
		if (listaSelectItemTipoRequerimentoVOs == null) {
			listaSelectItemTipoRequerimentoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoRequerimentoVOs;
	}

	public void setListaSelectItemTipoRequerimentoVOs(List<SelectItem> listaSelectItemTipoRequerimentoVOs) {
		this.listaSelectItemTipoRequerimentoVOs = listaSelectItemTipoRequerimentoVOs;
	}

	public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}

	public Boolean getPossuiPermissaoEnviarComunicadoInternoAluno() {
		if (possuiPermissaoEnviarComunicadoInternoAluno == null) {
			possuiPermissaoEnviarComunicadoInternoAluno = false;
		}
		return possuiPermissaoEnviarComunicadoInternoAluno;
	}

	public void setPossuiPermissaoEnviarComunicadoInternoAluno(Boolean possuiPermissaoEnviarComunicadoInternoAluno) {
		this.possuiPermissaoEnviarComunicadoInternoAluno = possuiPermissaoEnviarComunicadoInternoAluno;
	}

	public Boolean getPossuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro() {
		if (possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro == null) {
			possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro = false;
		}
		return possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro;
	}

	public void setPossuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro(Boolean possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro) {
		this.possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro = possuiPermissaoEnviarComunicadoInternoResponsavelFinanceiro;
	}

	public void inicializarDadosMatriculaBloqueioMatricula() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			setMatriculaVO(obj);
			getMatriculaVO().setResponsavelSuspensaoMatricula(getUsuarioLogadoClone());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}		
	}
	
	public void inicializarDadosMatriculaCancelamentoBloqueioMatricula() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			setMatriculaVO(obj);
			getMatriculaVO().setResponsavelCancelamentoSuspensaoMatricula(getUsuarioLogadoClone());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}		
	}
	
	public String getOncompleteModalBloqueioMatricula() {
		if (getSituacaoBloqueioMatricula().equals("BLOQUEIO_REALIZADO_COM_SUCESSO")) {
			return "PF('panelBloquearMatricula').hide()";
		}
		return "";
	}
	
	public String getOncompleteCancelamentoModalBloqueioMatricula() {
		if (getSituacaoCancelamentoBloqueioMatricula().equals("CANCELAMENTO_REALIZADO_COM_SUCESSO") || getSituacaoCancelamentoBloqueioMatricula().equals("ADIAMENTO_REALIZADO_COM_SUCESSO")) {
			return "PF('panelCancelarBloquearMatricula').hide()";
		}
		return "";
	}
	
	public void realizarBloqueioMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarBloqueioMatricula(getMatriculaVO(), getUsuarioLogado());
			setSituacaoBloqueioMatricula("BLOQUEIO_REALIZADO_COM_SUCESSO");
			this.consultarDadosAcademico(getAlunoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSituacaoBloqueioMatricula("");
		}
	}
	
	public void realizarCancelamentoBloqueioMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarCancelamentoBloqueioMatricula(getMatriculaVO(), getUsuarioLogado());
			setSituacaoCancelamentoBloqueioMatricula("CANCELAMENTO_REALIZADO_COM_SUCESSO");
			this.consultarDadosAcademico(getAlunoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSituacaoCancelamentoBloqueioMatricula("");
		}
	}
	
	public void realizarAdiamentoBloqueioMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarAdiamentoBloqueioMatricula(getMatriculaVO(), getUsuarioLogado());
			setSituacaoCancelamentoBloqueioMatricula("ADIAMENTO_REALIZADO_COM_SUCESSO");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSituacaoCancelamentoBloqueioMatricula("");
		}
	}
	
	public void realizarLiberacaoBloqueioAlunoInadimplente() {
		getFacadeFactory().getMatriculaFacade().realizarLiberacaoBloqueioAlunoInadimplente(getMatriculaVO(), getUsuarioLogado());
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

	public String getSituacaoBloqueioMatricula() {
		if (situacaoBloqueioMatricula == null) {
			situacaoBloqueioMatricula = "";
		}
		return situacaoBloqueioMatricula;
	}

	public void setSituacaoBloqueioMatricula(String situacaoBloqueioMatricula) {
		this.situacaoBloqueioMatricula = situacaoBloqueioMatricula;
	}

	public String getSituacaoCancelamentoBloqueioMatricula() {
		if (situacaoCancelamentoBloqueioMatricula == null) {
			situacaoCancelamentoBloqueioMatricula = "";
		}
		return situacaoCancelamentoBloqueioMatricula;
	}

	public void setSituacaoCancelamentoBloqueioMatricula(String situacaoCancelamentoBloqueioMatricula) {
		this.situacaoCancelamentoBloqueioMatricula = situacaoCancelamentoBloqueioMatricula;
	}

	public Boolean getRealizarAdiamentoSuspensaoMatricula() {
		if (realizarAdiamentoSuspensaoMatricula == null) {
			realizarAdiamentoSuspensaoMatricula = false;
		}
		return realizarAdiamentoSuspensaoMatricula;
	}

	public void setRealizarAdiamentoSuspensaoMatricula(Boolean realizarAdiamentoSuspensaoMatricula) {
		this.realizarAdiamentoSuspensaoMatricula = realizarAdiamentoSuspensaoMatricula;
	}

	public Boolean getApresentarBotaoEditarContaReceber() {
		if(apresentarBotaoEditarContaReceber == null){
			apresentarBotaoEditarContaReceber = false;
		}
		return apresentarBotaoEditarContaReceber;
	}

	public void setApresentarBotaoEditarContaReceber(Boolean apresentarBotaoEditarContaReceber) {
		this.apresentarBotaoEditarContaReceber = apresentarBotaoEditarContaReceber;
	}
 

	public Boolean getApresentarBotaoImprimirBoletoContaReceber() {
		if(apresentarBotaoImprimirBoletoContaReceber == null){
			apresentarBotaoImprimirBoletoContaReceber = false;
		}
		return apresentarBotaoImprimirBoletoContaReceber;
	}

	public void setApresentarBotaoImprimirBoletoContaReceber(Boolean apresentarBotaoImprimirBoletoContaReceber) {
		this.apresentarBotaoImprimirBoletoContaReceber = apresentarBotaoImprimirBoletoContaReceber;
	}

	public Boolean getApresentarBotaoExcluirContaReceber() {
		if(apresentarBotaoExcluirContaReceber == null){
			apresentarBotaoExcluirContaReceber = false;
		}
		return apresentarBotaoExcluirContaReceber;
	}

	public void setApresentarBotaoExcluirContaReceber(Boolean apresentarBotaoExcluirContaReceber) {
		this.apresentarBotaoExcluirContaReceber = apresentarBotaoExcluirContaReceber;
	}

	public Boolean getApresentarBotaoCancelarContaReceber() {
		if(apresentarBotaoCancelarContaReceber == null){
			apresentarBotaoCancelarContaReceber = false;
		}
		return apresentarBotaoCancelarContaReceber;
	}

	public void setApresentarBotaoCancelarContaReceber(Boolean apresentarBotaoCancelarContaReceber) {
		this.apresentarBotaoCancelarContaReceber = apresentarBotaoCancelarContaReceber;
	}
 
	public Boolean getApresentarBotaoEditarRequerimento() {
		if(apresentarBotaoEditarRequerimento == null){
			apresentarBotaoEditarRequerimento = false;
		}
		return apresentarBotaoEditarRequerimento;
	}

	public void setApresentarBotaoEditarRequerimento(Boolean apresentarBotaoEditarRequerimento) {
		this.apresentarBotaoEditarRequerimento = apresentarBotaoEditarRequerimento;
	}

	public Boolean getApresentarBotaoAdicionarRequerimento() {
		if(apresentarBotaoAdicionarRequerimento == null){
			apresentarBotaoAdicionarRequerimento = false;
		}
		return apresentarBotaoAdicionarRequerimento;
	}

	public void setApresentarBotaoAdicionarRequerimento(Boolean apresentarBotaoAdicionarRequerimento) {
		this.apresentarBotaoAdicionarRequerimento = apresentarBotaoAdicionarRequerimento;
	}

	public Boolean getApresentarBotaoExcluirRequerimento() {
		if(ApresentarBotaoExcluirRequerimento == null){
			ApresentarBotaoExcluirRequerimento = false;
		}
		return ApresentarBotaoExcluirRequerimento;
	}

	public void setApresentarBotaoExcluirRequerimento(Boolean apresentarBotaoExcluirRequerimento) {
		ApresentarBotaoExcluirRequerimento = apresentarBotaoExcluirRequerimento;
	}
	
	public Boolean getApresentarBotaoLancamentoNota() {
		if(apresentarBotaoLancamentoNota == null){
			apresentarBotaoLancamentoNota = false;
		}
		return apresentarBotaoLancamentoNota;
	}

	public void setApresentarBotaoLancamentoNota(Boolean apresentarBotaoLancamentoNota) {
		this.apresentarBotaoLancamentoNota = apresentarBotaoLancamentoNota;
	}
	

	public void verificarPermissoesUsuario(){
		this.verificarUsuarioPossuiPermissaoEnviarComunicadoInternoParaAluno("ComunicacaoInterna_enviarParaAluno");
		this.verificarUsuarioPossuiPermissaoCancelarContaReceber("PermitirCancelarContaReceber");
//		this.verificarUsuarioPossuiPermissaoEditarContaReceber();
//		this.verificarUsuarioPossuiPermissaoExcluirContaReceber();
		this.verificarUsuarioPossuiPermissaoEditarRequerimento();
		this.verificarUsuarioPossuiPermissaoAdicionarRequerimento();
		this.verificarUsuarioPossuiPermissaoExcluirRequerimento();
		this.verificarUsuarioPossuiPermissaoLancamentoNota();
 
	}
	

	public void verificarUsuarioPossuiPermissaoCancelarContaReceber(String identificadorAcaoPermissao) {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
			setApresentarBotaoCancelarContaReceber(true);
		} catch (Exception e) {
			setApresentarBotaoCancelarContaReceber(false);
		}
	}
	
//	public void verificarUsuarioPossuiPermissaoEditarContaReceber() {
//		try {
//			ContaReceber.alterar(ContaReceber.getIdEntidade(), getUsuarioLogado());
//			setApresentarBotaoEditarContaReceber(true);
//		} catch (Exception e) {
//			setApresentarBotaoEditarContaReceber(false);
//		}
//	}
	

	
	
	public void verificarUsuarioPossuiPermissaoEditarRequerimento(){
		try {
			getFacadeFactory().getControleAcessoFacade().alterar(Requerimento.getIdEntidade(),getUsuarioLogado());
			setApresentarBotaoEditarRequerimento(true);
		} catch (Exception e) {
			setApresentarBotaoEditarRequerimento(false);
		}
	}
	
	public void verificarUsuarioPossuiPermissaoAdicionarRequerimento(){
		try {
			getFacadeFactory().getControleAcessoFacade().incluir(Requerimento.getIdEntidade(),getUsuarioLogado());
			setApresentarBotaoAdicionarRequerimento(true);
		} catch (Exception e) {
			setApresentarBotaoAdicionarRequerimento(false);
		}
	}
	
	public void verificarUsuarioPossuiPermissaoExcluirRequerimento(){
		try {
			getFacadeFactory().getControleAcessoFacade().excluir(Requerimento.getIdEntidade(),getUsuarioLogado());
			setApresentarBotaoExcluirRequerimento(true);
		} catch (Exception e) {
			setApresentarBotaoExcluirRequerimento(false);
		}
	}
 
	
	public void verificarUsuarioPossuiPermissaoLancamentoNota(){
		try {
			if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoOperacao("LancamentoNota", Uteis.INCLUIR, getUsuarioLogado())){
				setApresentarBotaoLancamentoNota(true);
			}else{
				setApresentarBotaoLancamentoNota(false);
			}
		} catch (Exception e) {
			setApresentarBotaoLancamentoNota(false);
		}
	}
	
	@PostConstruct
	public void recuperarPessoaVindoDoRequerimento() {
		if (context().getExternalContext().getSessionMap().get("pessoaItem") != null) {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getSessionMap().get("pessoaItem");
			context().getExternalContext().getSessionMap().remove("pessoaItem");
			setAlunoVO(obj);
		}
	}
	
	public List<MatriculaVO> getListaMatriculaAbaBibliotecaVOs() {
		if (listaMatriculaAbaBibliotecaVOs == null) {
			listaMatriculaAbaBibliotecaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaAbaBibliotecaVOs;
	}

	public void setListaMatriculaAbaBibliotecaVOs(List<MatriculaVO> listaMatriculaAbaBibliotecaVOs) {
		this.listaMatriculaAbaBibliotecaVOs = listaMatriculaAbaBibliotecaVOs;
	}

	public SituacaoItemEmprestimo getSituacaoItemEmprestimo() {
		return situacaoItemEmprestimo;
	}

	public void setSituacaoItemEmprestimo(SituacaoItemEmprestimo situacaoItemEmprestimo) {
		this.situacaoItemEmprestimo = situacaoItemEmprestimo;
	}

	public List<SelectItem> getListaSelectItemSituacaoItemEmprestimoVOs() {
		return SituacaoItemEmprestimo.getListaSelectItemSituacaoItemEmprestimoVOs(false);
	}

	public List<SelectItem> getListaSelectItemMesAnoItemEmprestimoVOs() {
		if (listaSelectItemMesAnoItemEmprestimoVOs == null) {
			listaSelectItemMesAnoItemEmprestimoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMesAnoItemEmprestimoVOs;
	}

	public void setListaSelectItemMesAnoItemEmprestimoVOs(List<SelectItem> listaSelectItemMesAnoItemEmprestimoVOs) {
		this.listaSelectItemMesAnoItemEmprestimoVOs = listaSelectItemMesAnoItemEmprestimoVOs;
	}

	

	public UsuarioVO getResponsavelInteracaoWorkflowVO() {
		if (responsavelInteracaoWorkflowVO == null) {
			responsavelInteracaoWorkflowVO = new UsuarioVO();
		}
		return responsavelInteracaoWorkflowVO;
	}

	public void setResponsavelInteracaoWorkflowVO(UsuarioVO responsavelInteracaoWorkflowVO) {
		this.responsavelInteracaoWorkflowVO = responsavelInteracaoWorkflowVO;
	}

	public List<SelectItem> getListaSelectItemMesAnoInteracaoWorkflowVOs() {
		if (listaSelectItemMesAnoInteracaoWorkflowVOs == null) {
			listaSelectItemMesAnoInteracaoWorkflowVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMesAnoInteracaoWorkflowVOs;
	}

	public void setListaSelectItemMesAnoInteracaoWorkflowVOs(List<SelectItem> listaSelectItemMesAnoInteracaoWorkflowVOs) {
		this.listaSelectItemMesAnoInteracaoWorkflowVOs = listaSelectItemMesAnoInteracaoWorkflowVOs;
	}

	public List<SelectItem> getListaSelectItemResponsavelInteracaoWorkflowVOs() {
		if (listaSelectItemResponsavelInteracaoWorkflowVOs == null) {
			listaSelectItemResponsavelInteracaoWorkflowVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemResponsavelInteracaoWorkflowVOs;
	}

	public void setListaSelectItemResponsavelInteracaoWorkflowVOs(List<SelectItem> listaSelectItemResponsavelInteracaoWorkflowVOs) {
		this.listaSelectItemResponsavelInteracaoWorkflowVOs = listaSelectItemResponsavelInteracaoWorkflowVOs;
	}

	

	public List<SelectItem> getListaSelectItemMesAnoComunicacaoInternaVOs() {
		if (listaSelectItemMesAnoComunicacaoInternaVOs == null) {
			listaSelectItemMesAnoComunicacaoInternaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMesAnoComunicacaoInternaVOs;
	}

	public void setListaSelectItemMesAnoComunicacaoInternaVOs(List<SelectItem> listaSelectItemMesAnoComunicacaoInternaVOs) {
		this.listaSelectItemMesAnoComunicacaoInternaVOs = listaSelectItemMesAnoComunicacaoInternaVOs;
	}



	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getAlunoVO().getArquivoImagem().setCpfRequerimento(getAlunoVO().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getAlunoVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getAlunoVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			inicializarBooleanoFoto();
			getAlunoVO().getArquivoImagem().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
			getFacadeFactory().getPessoaFacade().alterarFoto(getAlunoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setOncompleteModal("PF('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("PF('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void removerImagensUploadArquivoTemp() throws Exception {
		try {
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getAlunoVO().getCPF();
			File arquivo = new File(arquivoExterno);
			getArquivoHelper().deleteRecursivo(arquivo);
		} catch (Exception e) {
			throw e;
		}
	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public void cancelar()  {
		try {
			if(getAlunoVO().getArquivoImagem().getDescricao() != "" && getAlunoVO().getArquivoImagem() != null) {
				if(getAlunoVO().getArquivoImagem().getPastaBaseArquivoEnum() != null && getAlunoVO().getArquivoImagem().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM_TMP)) {
					removerImagensUploadArquivoTemp();
				}
				if(getAlunoVO().getArquivoImagem().getCodigo()  > 0) {
					getAlunoVO().setArquivoImagem(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(getAlunoVO().getArquivoImagem().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}else {					
					getAlunoVO().setArquivoImagem(null);
				}
				setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getAlunoVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false)+"?uid="+new Date().getTime());				
				inicializarBooleanoFoto();
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getShowFotoCrop() {
		try {
			if (getAlunoVO().getArquivoImagem().getNome() == null) {
				return "imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario()+"?UID="+new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}
	
	public void inicializarDadosFotoUsuario() throws Exception {
		getAlunoVO().getArquivoImagem().setCpfRequerimento(getAlunoVO().getCPF());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getAlunoVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "", false));
	}

	public void inicializarBooleanoFoto() {
		setRemoverFoto((Boolean) false);
		setExibirUpload(true);
		setExibirBotao(false);
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void loginComSimulacaoAcessoVisaoAluno() {
		UsuarioVO usuarioVO = null;
		SimularAcessoAlunoVO simulacao = new SimularAcessoAlunoVO();
		try {
			if (!getAlunoVO().getCodigo().equals(0)) {
				usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioSimulacaoVisaoAluno(getAlunoVO().getCodigo(), false);
				simulacao.setDataSimulacao(new Date());
				simulacao.setResponsavelSimulacaoAluno(new UsuarioVO());
				simulacao.getResponsavelSimulacaoAluno().setCodigo(getUsuarioLogado().getCodigo());
				simulacao.getResponsavelSimulacaoAluno().setUsername(getUsuarioLogado().getUsername());
				simulacao.getResponsavelSimulacaoAluno().setNome(getUsuarioLogado().getNome());
				simulacao.getResponsavelSimulacaoAluno().setSenha(getUsuarioLogado().getSenha());
				simulacao.getResponsavelSimulacaoAluno().setTipoUsuario(getUsuarioLogado().getTipoUsuario());
				simulacao.getResponsavelSimulacaoAluno().setUnidadeEnsinoLogado(getUnidadeEnsinoLogadoClone());
				
				LoginControle login = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
				simulacao.setOpcaoLogin(login.getOpcao());
				
				String retorno = executarLogin(usuarioVO.getUsername(), usuarioVO.getSenha(), "");
				if (!Uteis.isAtributoPreenchido(retorno)) {
					throw new Exception("Foi encontrada uma irregularidade em sua matrícula. Entre em contato com o departamento pedagógico.");
				}
				simulacao.setUsuarioSimulado(getUsuarioLogadoClone());
				getFacadeFactory().getSimularAcessoAlunoFacade().incluir(simulacao, false, getUsuarioLogado());
				getUsuarioLogado().setSimularAcessoAluno(simulacao);
				alunoSimulacaoValido = true;
			}
		} catch (Exception e) {
			try {
				if (Uteis.isAtributoPreenchido(simulacao.getResponsavelSimulacaoAluno())) {
					executarLogin(simulacao.getResponsavelSimulacaoAluno().getUsername(), simulacao.getResponsavelSimulacaoAluno().getSenha(), simulacao.getResponsavelSimulacaoAluno().getTipoUsuario());
				}
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e2.getMessage(), Uteis.ERRO);
			}
			alunoSimulacaoValido = false;
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public String executarLogin(String userName, String senha, String tipoUsuarioSimulacaoAcesso) throws Exception {
		LoginControle loginControle = (LoginControle) getControlador("LoginControle");
		inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
		Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("usuarioLogado"));
		loginControle.setUsuario(new UsuarioVO());
		loginControle.getUsuario().setPerfilAcesso(new PerfilAcessoVO());
		loginControle.setUsername(userName);
		loginControle.setSenha(senha);
		if (tipoUsuarioSimulacaoAcesso.equals("DM")) {
			String retorno = loginControle.loginSistema(true, false);
			return retorno.isEmpty() ? loginControle.logarDiretamenteComoDiretorMultiCampus() : retorno;
		} else if (tipoUsuarioSimulacaoAcesso.equals("FU")) {
			String retorno = loginControle.loginSistema(true, false);
			return retorno.isEmpty() ? loginControle.logarDiretamenteComoFuncionario() : retorno;
		}
		String retorno = loginControle.loginSistema(true, false);
		return retorno.isEmpty() ? loginControle.logarDiretamenteComoAluno(true) : retorno;
	}
	
	public boolean isAlunoSimulacaoValido() {
		return alunoSimulacaoValido;
	}

	public void setAlunoSimulacaoValido(boolean alunoSimulacaoValido) {
		this.alunoSimulacaoValido = alunoSimulacaoValido;
	}

	public String getExecutarNavegacaoSimulacaoVisaoAluno() {
		return isAlunoSimulacaoValido() ? !getLoginControle().getOpcao().contains("telaInicialVisaoAluno") ? "removerPopup('"+getLoginControle().getFinalizarPopups()+"');simularAcessoFichaAlunoAvaliacaoInst();window.close()" : "removerPopup('"+getLoginControle().getFinalizarPopups()+"');simularAcessoFichaAluno();window.close();" : "";
	}
	
	public void consultarFaltasAlunoPorMatriculaPeriodo() {
		MatriculaPeriodoVO matriculaPeriodoItem = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
		try {
			setMatriculaPeriodoVO(matriculaPeriodoItem);
			setListaFaltas(null);
			List<RegistroAulaVO> registroAulaSecundarioVOs = null;
			List<RegistroAulaVO> registroAulaVOs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoQuantidade(0, matriculaPeriodoItem.getMatricula(), matriculaPeriodoItem.getSemestre(), matriculaPeriodoItem.getAno(), false, getUsuarioLogado());
			Map<String, List<RegistroAulaVO>> map = new HashMap<String, List<RegistroAulaVO>>(0);
			
			for (RegistroAulaVO obj : registroAulaVOs) {
				if (!map.containsKey(obj.getAnoSemestreApresentar())) {
					registroAulaSecundarioVOs = new ArrayList<RegistroAulaVO>(0);
					map.put(obj.getAnoSemestreApresentar(), registroAulaSecundarioVOs);
				}
				registroAulaSecundarioVOs = map.get(obj.getAnoSemestreApresentar());
				registroAulaSecundarioVOs.add(obj);
			}
			SortedSet<String> keys = new TreeSet<String>(map.keySet());
			for (String anoSemestre : keys) {
				getListaFaltas().add(map.get(anoSemestre));
			}
			setMensagemID("msg_dados_consultados");
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void visualizarFaltas() {
		try {
			setListaDetalhesMinhasFaltasVOs(new ArrayList<RegistroAulaVO>(0));
			RegistroAulaVO reg = (RegistroAulaVO) context().getExternalContext().getRequestMap().get("faltas");
			setListaDetalhesMinhasFaltasVOs(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(reg.getTurma().getCodigo(), getMatriculaPeriodoVO().getMatricula(), reg.getDisciplina().getCodigo(), reg.getSemestre(), reg.getAno(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<RegistroAulaVO> getListaDetalhesMinhasFaltasVOs() {
		if (listaDetalhesMinhasFaltasVOs == null) {
			listaDetalhesMinhasFaltasVOs = new ArrayList<RegistroAulaVO>();
		}
		return listaDetalhesMinhasFaltasVOs;
	}

	public void setListaDetalhesMinhasFaltasVOs(List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs) {
		this.listaDetalhesMinhasFaltasVOs = listaDetalhesMinhasFaltasVOs;
	}

	public List<List<RegistroAulaVO>> getListaFaltas() {
		if (listaFaltas == null) {
			listaFaltas = new ArrayList<List<RegistroAulaVO>>(0);
		}
		return listaFaltas;
	}

	public void setListaFaltas(List<List<RegistroAulaVO>> listaFaltas) {
		this.listaFaltas = listaFaltas;
	}
	
	
	
	/**
	 * Método responsavel por invocar outro Controlador passando por parâmetro o
	 * método a ser executado e o obj.
	 * 
	 * @Autor Alberto
	 */
	public void executarVisualizacaoRelatorioFichaAluno() {
//		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
//		  try {
//	            String titulo = "FICHA DO ALUNO";
//	            String nomeEntidade = obj.getUnidadeEnsino().getNome();
////	            String design = FichaAlunoRel.getDesignIReportRelatorio();	            	            
////	            List<FichaAlunoRelVO> fichaAlunoRelVOs = getFacadeFactory().getFichaAlunoRelFacade().criarObjeto(obj.getMatricula(),getUsuarioLogado());
//	            if (!fichaAlunoRelVOs.isEmpty()) {
//	            	getSuperParametroRelVO().setTituloRelatorio(titulo);
//	            	getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
//					getSuperParametroRelVO().setNomeDesignIreport(design);
//					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
////					getSuperParametroRelVO().setSubReport_Dir(FichaAlunoRel.getCaminhoBaseRelatorio());
//					getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());				
//					getSuperParametroRelVO().setListaObjetos(fichaAlunoRelVOs);
////					getSuperParametroRelVO().setCaminhoBaseRelatorio(FichaAlunoRel.getCaminhoBaseRelatorio());
//					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());				
//					realizarImpressaoRelatorio();								
//					setMensagemID("msg_relatorio_ok");
//				} else {
//					setMensagemID("msg_relatorio_sem_dados");
//				}        	        
//	        } catch (Exception e) {
//	            setMensagemDetalhada("msg_erro", e.getMessage());
//	        } finally { 
//	        }
	}
	
	
	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getAlunoVO().getArquivoImagem().setCpfRequerimento(getAlunoVO().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(getAlunoVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getAlunoVO().getCPF() + File.separator + getAlunoVO().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void carregarPermissoesExibicaoAbasFichaAluno() {
		setCentralAluno(getLoginControle().getPermissaoAcessoMenuVO().getCentralAluno());
		setPermitirVisualizarDadosMatricula(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosMatricula());
		setPermitirVisualizarDadosFinanceiro(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosFinanceiro());
		setPermitirVisualizarDadosRequerimento(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosRequerimento());
		setPermitirVisualizarDadosBiblioteca(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosBiblioteca());
		setPermitirVisualizarDadosCRM(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosCRM());
		setPermitirVisualizarDadosProcessoSeletivo(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosProcessoSeletivo());
		setPermitirVisualizarDadosComunicacaoInterna(getLoginControle().getPermissaoAcessoMenuVO().getPermitirVisualizarDadosComunicacaoInterna());
	}

	public Boolean getCentralAluno() {
		if (centralAluno == null) {
			centralAluno = Boolean.FALSE;
		}
		return centralAluno;
	}

	public void setCentralAluno(Boolean centralAluno) {
		this.centralAluno = centralAluno;
	}

	public Boolean getPermitirVisualizarDadosMatricula() {
		if (permitirVisualizarDadosMatricula == null) {
			permitirVisualizarDadosMatricula = Boolean.FALSE;
		}
		return permitirVisualizarDadosMatricula;
	}

	public void setPermitirVisualizarDadosMatricula(Boolean permitirVisualizarDadosMatricula) {
		this.permitirVisualizarDadosMatricula = permitirVisualizarDadosMatricula;
	}

	public Boolean getPermitirVisualizarDadosFinanceiro() {
		if (permitirVisualizarDadosFinanceiro == null) {
			permitirVisualizarDadosFinanceiro = Boolean.FALSE;
		}
		return permitirVisualizarDadosFinanceiro;
	}

	public void setPermitirVisualizarDadosFinanceiro(Boolean permitirVisualizarDadosFinanceiro) {
		this.permitirVisualizarDadosFinanceiro = permitirVisualizarDadosFinanceiro;
	}

	public Boolean getPermitirVisualizarDadosRequerimento() {
		if (permitirVisualizarDadosRequerimento == null) {
			permitirVisualizarDadosRequerimento = Boolean.FALSE;
		}
		return permitirVisualizarDadosRequerimento;
	}

	public void setPermitirVisualizarDadosRequerimento(Boolean permitirVisualizarDadosRequerimento) {
		this.permitirVisualizarDadosRequerimento = permitirVisualizarDadosRequerimento;
	}

	public Boolean getPermitirVisualizarDadosBiblioteca() {
		if (permitirVisualizarDadosBiblioteca == null) {
			permitirVisualizarDadosBiblioteca = Boolean.FALSE;
		}
		return permitirVisualizarDadosBiblioteca;
	}

	public void setPermitirVisualizarDadosBiblioteca(Boolean permitirVisualizarDadosBiblioteca) {
		this.permitirVisualizarDadosBiblioteca = permitirVisualizarDadosBiblioteca;
	}

	public Boolean getPermitirVisualizarDadosCRM() {
		if (permitirVisualizarDadosCRM == null) {
			permitirVisualizarDadosCRM = Boolean.FALSE;
		}
		return permitirVisualizarDadosCRM;
	}

	public void setPermitirVisualizarDadosCRM(Boolean permitirVisualizarDadosCRM) {
		this.permitirVisualizarDadosCRM = permitirVisualizarDadosCRM;
	}

	public Boolean getPermitirVisualizarDadosProcessoSeletivo() {
		if (permitirVisualizarDadosProcessoSeletivo == null) {
			permitirVisualizarDadosProcessoSeletivo = Boolean.FALSE;
		}
		return permitirVisualizarDadosProcessoSeletivo;
	}

	public void setPermitirVisualizarDadosProcessoSeletivo(Boolean permitirVisualizarDadosProcessoSeletivo) {
		this.permitirVisualizarDadosProcessoSeletivo = permitirVisualizarDadosProcessoSeletivo;
	}

	public Boolean getPermitirVisualizarDadosComunicacaoInterna() {
		if (permitirVisualizarDadosComunicacaoInterna == null) {
			permitirVisualizarDadosComunicacaoInterna = Boolean.FALSE;
		}
		return permitirVisualizarDadosComunicacaoInterna;
	}

	public void setPermitirVisualizarDadosComunicacaoInterna(Boolean permitirVisualizarDadosComunicacaoInterna) {
		this.permitirVisualizarDadosComunicacaoInterna = permitirVisualizarDadosComunicacaoInterna;
	}

	public Boolean getApresentarAbaNegativacaoCobranca() {
		if (apresentarAbaNegativacaoCobranca == null) {
			apresentarAbaNegativacaoCobranca = Boolean.FALSE;
		}
		return apresentarAbaNegativacaoCobranca;
	}

	public void setApresentarAbaNegativacaoCobranca(Boolean apresentarAbaNegativacaoCobranca) {
		this.apresentarAbaNegativacaoCobranca = apresentarAbaNegativacaoCobranca;
	}

	
	
//	public List<RegistroEntregaArtefatoAlunoVO> getListaArtefatosEntregueAluno() {
//		if (listaArtefatosEntregueAluno == null) {
//			listaArtefatosEntregueAluno = new ArrayList<RegistroEntregaArtefatoAlunoVO>(0);
//		}
//		return listaArtefatosEntregueAluno;
//	}
//
//	public void setListaArtefatosEntregueAluno(List<RegistroEntregaArtefatoAlunoVO> listaArtefatosEntregueAluno) {
//		this.listaArtefatosEntregueAluno = listaArtefatosEntregueAluno;
//	}

	public String getMensagemListaHistoricoDisciplinas() {
		if (mensagemListaHistoricoDisciplinas == null) {
			mensagemListaHistoricoDisciplinas = "";
		}
		return mensagemListaHistoricoDisciplinas;
	}

	public void setMensagemListaHistoricoDisciplinas(String mensagemListaHistoricoDisciplinas) {
		this.mensagemListaHistoricoDisciplinas = mensagemListaHistoricoDisciplinas;
	}
	
	/*public void consultarArtefatosEntregue() {
		try {
			List objs = new ArrayList<RegistroEntregaArtefatoAlunoVO>(0);

			
				objs = getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().consultarPorAluno(getPessoaVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			

			setListaArtefatosEntregueAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaArtefatosEntregueAluno(new ArrayList<RegistroEntregaArtefatoAlunoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}*/
	
	public void inicializarDadosNavegacaoTelaMapaSuspensaoMatricula() throws Exception{
		try {
			removerControleMemoriaFlashTela("MapaSuspensaoMatriculaControle");
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			context().getExternalContext().getSessionMap().put("matricula", obj);			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}
	
	public Boolean getQuantidadeMatriculaAluno() {
		if (getListaMatriculaVOs().size() > 1) {
			return true;
		}else {
		return false;
		}
	}
	
	public void inicializarDadosNavegacaoTelaDadosMatriculaParaNovoRequerimento() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		if (obj != null && !obj.getMatricula().equals("")) {
			removerControleMemoriaFlashTela("RequerimentoControle");
			context().getExternalContext().getSessionMap().put("matriculaFichaAlunoParaRequerimento", obj);
		}
	}
	
	
	public Integer getQuantidadeEmprestimoAberto() {
		if (quantidadeEmprestimoAberto == null) {
			quantidadeEmprestimoAberto = 0;
		}
		return quantidadeEmprestimoAberto;
	}
	public void setQuantidadeEmprestimoAberto(Integer quantidadeEmprestimoAberto) {
		this.quantidadeEmprestimoAberto = quantidadeEmprestimoAberto;
	}
	public Integer getQuantidadeEmprestimoAtrasado() {
		if (quantidadeEmprestimoAtrasado == null) {
			quantidadeEmprestimoAtrasado = 0;
		}
		return quantidadeEmprestimoAtrasado;
	}
	public void setQuantidadeEmprestimoAtrasado(Integer quantidadeEmprestimoAtrasado) {
		this.quantidadeEmprestimoAtrasado = quantidadeEmprestimoAtrasado;
	}
	
	public Integer getQuantidadeReservaPendente() {
		if (quantidadeReservaPendente == null) {
			quantidadeReservaPendente = 0;
		}
		return quantidadeReservaPendente;
	}

	public void setQuantidadeReservaPendente(Integer quantidadeReservaPendente) {
		this.quantidadeReservaPendente = quantidadeReservaPendente;
	}
	
	public String getValorTotalMulta() {
		if (valorTotalMulta == null) {
			valorTotalMulta = "0,00";
		}
		return valorTotalMulta;
	}

	public void setValorTotalMulta(String valorTotalMulta) {
		this.valorTotalMulta = valorTotalMulta;
	}
	
	public List<TimeLineVO> getListaTimeLineVOs() {
		if (listaTimeLineVOs == null) {
			listaTimeLineVOs = new ArrayList<TimeLineVO>(0);
		}
		return listaTimeLineVOs;
	}

	public void setListaTimeLineVOs(List<TimeLineVO> listaTimeLineVOs) {
		this.listaTimeLineVOs = listaTimeLineVOs;
	}

	public List<TimeLineVO> getListaReservaTimeLineVOs() {
		if (listaReservaTimeLineVOs == null) {
			listaReservaTimeLineVOs = new ArrayList<TimeLineVO>(0);
		}
		return listaReservaTimeLineVOs;
	}

	public void setListaReservaTimeLineVOs(List<TimeLineVO> listaReservaTimeLineVOs) {
		this.listaReservaTimeLineVOs = listaReservaTimeLineVOs;
	}
	
	public List<TimeLineVO> getDadosTimeLineUsuario() {
		if (dadosTimeLineUsuario == null) {
			dadosTimeLineUsuario = new ArrayList<TimeLineVO>(0);
		}
		return dadosTimeLineUsuario;
	}

	public void setDadosTimeLineUsuario(List<TimeLineVO> dadosTimeLineUsuario) {
		this.dadosTimeLineUsuario = dadosTimeLineUsuario;
	}

	public List<SelectItem> getListaSelectItemTipoOrigemBibliotecaVOs() {
		if(listaSelectItemTipoOrigemBibliotecaVOs == null) {
			listaSelectItemTipoOrigemBibliotecaVOs = new ArrayList<SelectItem>(0);
			listaSelectItemTipoOrigemBibliotecaVOs.add(new SelectItem(null, ""));
			for(TipoOrigemTimeLineEnum timeLineEnum:TipoOrigemTimeLineEnum.values()) {				
				listaSelectItemTipoOrigemBibliotecaVOs.add(new SelectItem(timeLineEnum, timeLineEnum.getValorApresentar()));
			}
		}
		return listaSelectItemTipoOrigemBibliotecaVOs;
	}

	public void setListaSelectItemTipoOrigemBibliotecaVOs(List<SelectItem> listaSelectItemTipoOrigemBibliotecaVOs) {
		this.listaSelectItemTipoOrigemBibliotecaVOs = listaSelectItemTipoOrigemBibliotecaVOs;
	}

	public String getMesAnoBiblioteca() {
		return mesAnoBiblioteca;
	}

	public void setMesAnoBiblioteca(String mesAnoBiblioteca) {
		this.mesAnoBiblioteca = mesAnoBiblioteca;
	}

	public TipoOrigemTimeLineEnum getTipoOrigemTimeLineEnum() {
		return tipoOrigemTimeLineEnum;
	}

	public void setTipoOrigemTimeLineEnum(TipoOrigemTimeLineEnum tipoOrigemTimeLineEnum) {
		this.tipoOrigemTimeLineEnum = tipoOrigemTimeLineEnum;
	}

	public void setTipoConsultaComboTipoBoleto(List<SelectItem> tipoConsultaComboTipoBoleto) {
		this.tipoConsultaComboTipoBoleto = tipoConsultaComboTipoBoleto;
	}

	public List<TimeLineVO> getDadosTimeLineUsuarioRemovido() {
		if(dadosTimeLineUsuarioRemovido == null) {
			dadosTimeLineUsuarioRemovido = new ArrayList<TimeLineVO>(0);
		}
		return dadosTimeLineUsuarioRemovido;
	}

	public void setDadosTimeLineUsuarioRemovido(List<TimeLineVO> dadosTimeLineUsuarioRemovido) {
		this.dadosTimeLineUsuarioRemovido = dadosTimeLineUsuarioRemovido;
	}

	public void realizarFitroTimeLineBiblioteca() {
		if(!getDadosTimeLineUsuarioRemovido().isEmpty()) {
			getDadosTimeLineUsuario().addAll(getDadosTimeLineUsuarioRemovido());
			getDadosTimeLineUsuarioRemovido().clear();
		}
		for (Iterator<TimeLineVO> iterator = getDadosTimeLineUsuario().iterator(); iterator.hasNext();) {
			TimeLineVO timeLineVO = iterator.next();
			if(!timeLineVO.filter(getMesAnoBiblioteca(), getTipoOrigemTimeLineEnum())) {
				iterator.remove();
				getDadosTimeLineUsuarioRemovido().add(timeLineVO);
			}			
		}
		Ordenacao.ordenarListaDecrescente(getDadosTimeLineUsuario(), "dataOcorrenciaApresentar");
	}
	public List<DocumentacaoGEDVO> getListaDocumentacaoGED() {
		return listaDocumentacaoGED;
	}

	public void setListaDocumentacaoGED(List<DocumentacaoGEDVO> listaDocumentacaoGED) {
		this.listaDocumentacaoGED = listaDocumentacaoGED;
	}

	public TipoOrigemDocumentoAssinadoEnum getTipoOrigemDocumentoAssinadoEnum() {
		return tipoOrigemDocumentoAssinadoEnum;
	}

	public void setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
		this.tipoOrigemDocumentoAssinadoEnum = tipoOrigemDocumentoAssinadoEnum;
	}

	public void consultarDadosDocumetacaoGED() {
		try {
			consultarDocumetacaoMatricula();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPossuiPerfilAcessoDocumentacaoGED() {
		if (possuiPerfilAcessoDocumentacaoGED == null) {
			try {
				possuiPerfilAcessoDocumentacaoGED = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("ApresentarConfiguracaoGEDAluno", getUsuarioLogado());
			} catch (Exception e) {
				possuiPerfilAcessoDocumentacaoGED = false;
			}
		}
		return possuiPerfilAcessoDocumentacaoGED;
	}

	public void realizarDownloadArquivoAssinado() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			DocumentoAssinadoVO doc = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorArquivo(obj.getArquivoVOAssinado().getCodigo(), getUsuarioLogadoClone());
			if (Uteis.isAtributoPreenchido(doc) && doc.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
				doc.setArquivo(obj.getArquivoVOAssinado());
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(doc, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(doc.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			}
			if (Uteis.isAtributoPreenchido(doc) && doc.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
				doc.setArquivo(obj.getArquivoVOAssinado());
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(doc, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(doc.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			}
			realizarDownloadArquivo(obj.getArquivoVOAssinado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultarDocumetacaoMatricula() {
		try {
			for (MatriculaVO matriculaVO : getListaMatriculaVOs()) {
				getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, getUsuarioLogado());
				matriculaVO.setListaDocumentacaoGED(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatricula(matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("fichaAlunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("fichaAlunoForm.xhtml");
		}
	}

	public List<SelectItem> getListaSelectItemTipoOrigemDocumentoAssinadoEnum() {
		if (listaSelectItemTipoOrigemDocumentoAssinadoEnum == null) {
			listaSelectItemTipoOrigemDocumentoAssinadoEnum = new ArrayList<SelectItem>();
			listaSelectItemTipoOrigemDocumentoAssinadoEnum.add(new SelectItem(null, ""));
			for (TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum : TipoOrigemDocumentoAssinadoEnum.values()) {
				if (!tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.NENHUM) && tipoOrigemDocumentoAssinadoEnum.isDigitacaoGED()) {
					if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
						listaSelectItemTipoOrigemDocumentoAssinadoEnum.add(new SelectItem(tipoOrigemDocumentoAssinadoEnum, tipoOrigemDocumentoAssinadoEnum.getDescricao()));
					}
				}
			}
		}
		return listaSelectItemTipoOrigemDocumentoAssinadoEnum;
	}

	public void setListaSelectItemTipoOrigemDocumentoAssinadoEnum(List<SelectItem> listaSelectItemTipoOrigemDocumentoAssinadoEnum) {
		this.listaSelectItemTipoOrigemDocumentoAssinadoEnum = listaSelectItemTipoOrigemDocumentoAssinadoEnum;
	}

	public List<SelectItem> getListaSelectItemMatriculaVOs() {
		if (listaSelectItemMatriculaVOs == null) {
			listaSelectItemMatriculaVOs = new ArrayList<>();
		}
		return listaSelectItemMatriculaVOs;
	}

	public void setListaSelectItemMatriculaVOs(List<SelectItem> listaSelectItemMatriculaVOs) {
		this.listaSelectItemMatriculaVOs = listaSelectItemMatriculaVOs;
	}

	public String getMatriculaSelecionada() {
		if (matriculaSelecionada == null) {
			matriculaSelecionada = "";
		}
		return matriculaSelecionada;
	}

	public void setMatriculaSelecionada(String matriculaSelecionada) {
		this.matriculaSelecionada = matriculaSelecionada;
	}

	public DataModelo getListaDocumentosPendentes() {
		if (listaDocumentosPendentes == null) {
			listaDocumentosPendentes = new DataModelo();
		}
		return listaDocumentosPendentes;
	}

	public void setListaDocumentosPendentes(DataModelo listaDocumentosPendentes) {
		this.listaDocumentosPendentes = listaDocumentosPendentes;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public DataModelo getListaDocumentosAssinados() {
		if (listaDocumentosAssinados == null) {
			listaDocumentosAssinados = new DataModelo();
		}
		return listaDocumentosAssinados;
	}

	public void setListaDocumentosAssinados(DataModelo listaDocumentosAssinados) {
		this.listaDocumentosAssinados = listaDocumentosAssinados;
	}

	public DataModelo getListaDocumentosRejeitados() {
		if (listaDocumentosRejeitados == null) {
			listaDocumentosRejeitados = new DataModelo();
		}
		return listaDocumentosRejeitados;
	}

	public void setListaDocumentosRejeitados(DataModelo listaDocumentosRejeitados) {
		this.listaDocumentosAssinados = listaDocumentosRejeitados;
	}

	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}

	public SituacaoDocumentoAssinadoPessoaEnum getSituacaoDocumentoAssinadoPessoaEnum() {
		if (situacaoDocumentoAssinadoPessoaEnum == null) {
			situacaoDocumentoAssinadoPessoaEnum = SituacaoDocumentoAssinadoPessoaEnum.ASSINADO;
		}
		return situacaoDocumentoAssinadoPessoaEnum;
	}

	public void setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum) {
		this.situacaoDocumentoAssinadoPessoaEnum = situacaoDocumentoAssinadoPessoaEnum;
	}

	public void consultarDocumentos() {
		getListaDocumentosAssinados().setOffset(0);
		getListaDocumentosPendentes().setOffset(0);
		getListaDocumentosRejeitados().setOffset(0);

		getListaDocumentosAssinados().setLimitePorPagina(10);
		getListaDocumentosAssinados().setPage(0);
		getListaDocumentosAssinados().setPaginaAtual(0);

		getListaDocumentosPendentes().setLimitePorPagina(10);
		getListaDocumentosPendentes().setPage(0);
		getListaDocumentosPendentes().setPaginaAtual(0);

		getListaDocumentosRejeitados().setLimitePorPagina(10);
		getListaDocumentosRejeitados().setPage(0);
		getListaDocumentosRejeitados().setPaginaAtual(0);

		consultarDocumentosPendentes();
		consultarDocumentosRejeitados();
		consultarDocumentosAssinados();
	}

	public void consultarDocumentosPendentes() {
		try {
			getMatriculaVO().setMatricula(matriculaSelecionada);
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosPendentes(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getDisciplinaVO(), null, null, getMatriculaVO(), getTipoOrigemDocumentoAssinadoEnum(), null, null, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, null, getUsuarioLogado(), getListaDocumentosPendentes().getLimitePorPagina(), getListaDocumentosPendentes().getOffset());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDocumentosRejeitados() {
		try {
			getMatriculaVO().setMatricula(matriculaSelecionada);
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosRejeitados(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getDisciplinaVO(), null, null, getMatriculaVO(), getTipoOrigemDocumentoAssinadoEnum(), null, null, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, null, getUsuarioLogado(), getListaDocumentosRejeitados().getLimitePorPagina(), getListaDocumentosRejeitados().getOffset());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDocumentosAssinados() {
		try {
			getMatriculaVO().setMatricula(matriculaSelecionada);
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosAssinados(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getDisciplinaVO(), null, null, getMatriculaVO(), getTipoOrigemDocumentoAssinadoEnum(), null, null, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, null, getUsuarioLogado(), getListaDocumentosAssinados().getLimitePorPagina(), getListaDocumentosAssinados().getOffset());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarDocumentoAssinado() {
		setDocumentoAssinadoVO((DocumentoAssinadoVO) getRequestMap().get("documentoAssinadoItem"));
		try {
			// setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(getDocumentoAssinadoVO().getCodigo(),
			// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
			for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa()) {
				if (documentoAssinadoPessoaVO.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())) {
					setSituacaoDocumentoAssinadoPessoaEnum(documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum());
				}
			}
		} catch (Exception e) {

		}
	}

	public String getUrlDonloadSV() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		} else {
			return "location.href='../DownloadSV'";
		}
	}

	public void realizarDownloadArquivoProvedorCertisin() {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO) getRequestMap().get("documentoAssinadoItem");
			getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			realizarDownloadArquivo(obj.getArquivo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDownloadArquivoProvedorTechCert() {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO) getRequestMap().get("documentoAssinadoItem");
			getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			realizarDownloadArquivo(obj.getArquivo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void paginarDocumentosPendentes() {
		
		consultarDocumentosPendentes();
	}

	public void paginarDocumentosAssinados() {
		
		consultarDocumentosAssinados();
	}

	public void paginarDocumentosRejeitados() {
		
		consultarDocumentosRejeitados();
	}

	

	public String getFiltroMatricula() {
		if(filtroMatricula == null) {
			filtroMatricula = Constantes.EMPTY;
		}
		return filtroMatricula;
	}

	public void setFiltroMatricula(String filtroMatricula) {
		this.filtroMatricula = filtroMatricula;
	}
	
	public List<SelectItem> getListaComboboxMatriculaVOs() {
		if (listaComboboxMatriculaVOs == null) {
			listaComboboxMatriculaVOs = new ArrayList<>();
			getListaComboboxMatriculaVOs().add(new SelectItem("",""));
		}
		return listaComboboxMatriculaVOs;
	}

	public void setListaComboboxMatriculaVOs(List<SelectItem> listaComboboxMatriculaVOs) {
		this.listaComboboxMatriculaVOs = listaComboboxMatriculaVOs;
	}
	
	public void carregamentoFiltro() {
		try {

			consultarDadosAcademico(getAlunoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			e.printStackTrace();
		}
	}
	private Boolean versaoAntiga;
	private Boolean versaoNova;
	
	public Boolean getVersaoAntiga() {
		if (versaoAntiga == null) {
			versaoAntiga = false;
		}
		return versaoAntiga;
	}
	
	public void setVersaoAntiga(Boolean versaoAntiga) {
		this.versaoAntiga = versaoAntiga;
	}
	
	public Boolean getVersaoNova() {
		if (versaoNova == null) {
			versaoNova = false;
		}
		return versaoNova;
	}
	
	public void setVersaoNova(Boolean versaoNova) {
		this.versaoNova = versaoNova;
	}
	
	public void mudarLayoutConsulta() {
		setVersaoAntiga(true);
		setVersaoNova(false);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), FichaAlunoControle.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mudarLayoutConsulta2() {
		setVersaoAntiga(false);
		setVersaoNova(true);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), FichaAlunoControle.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Boolean versaoAntigaFinanceiro;
	private Boolean versaoNovaFinanceiro;
	
	public Boolean getVersaoAntigaFinanceiro() {
		if (versaoAntigaFinanceiro == null) {
			versaoAntigaFinanceiro = false;
		}
		return versaoAntigaFinanceiro;
	}
	
	public void setVersaoAntigaFinanceiro(Boolean versaoAntigaFinanceiro) {
		this.versaoAntigaFinanceiro = versaoAntigaFinanceiro;
	}
	
	public Boolean getVersaoNovaFinanceiro() {
		if (versaoNovaFinanceiro == null) {
			versaoNovaFinanceiro = false;
		}
		return versaoNovaFinanceiro;
	}
	
	public void setVersaoNovaFinanceiro(Boolean versaoNovaFinanceiro) {
		this.versaoNovaFinanceiro = versaoNovaFinanceiro;
	}
	
	public void mudarLayoutConsultaFinanceiro() {
		setVersaoAntigaFinanceiro(true);
		setVersaoNovaFinanceiro(false);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), FichaAlunoControle.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mudarLayoutConsultaFinanceiro2() {
		setVersaoAntigaFinanceiro(false);
		setVersaoNovaFinanceiro(true);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), FichaAlunoControle.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
