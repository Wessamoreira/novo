package controle.ead;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaInteracaoAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaResponsavelAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.MinhasNotasPBLVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoRecursoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * @author Victor Hugo de Paula Costa - 18 de jul de 2016
 *
 */
@Controller("GestaoEventoConteudoTurmaControle")
@Scope("viewScope")
@Lazy
public class GestaoEventoConteudoTurmaControle extends SuperControleRelatorio {

	/**
	 * @author Victor Hugo de Paula Costa - 18 de jul de 2016
	 */
	private static final long serialVersionUID = 1L;
	private GestaoEventoConteudoTurmaVO filtroConsultaGestaoEventoConteudoTurma;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private ConteudoVO conteudoVO;
	private List<SelectItem> listaSelectItemConteudo;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO;
	private String modalLiberacaoRecursoEducacional;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	//private GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacaoAluno;
	private String modalAlertaNotasNaoLancadasRecursoEducacional;
	private List<SelectItem> listSelectItemVariavelNota;
	private String variavelTipoNota;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private GestaoEventoConteudoTurmaResponsavelAtaVO gestaoEventoConteudoTurmaResponsavelAta;
	private List<SelectItem> listaSelectPessoa;
	private GestaoEventoConteudoTurmaInteracaoAtaVO gestaoEventoConteudoTurmaInteracaoAta;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	private String modalPanelLancarNotaHistorico;
	private List<MinhasNotasPBLVO> listaMinhasNotasPbl;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacaoPbl;
	private List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliados;
	private String modalAvalicaoAlunoMinhaNotaPbl;
	private Double mediaFinalGeralMinhaNotaPbl;
	private String formulaBasicaFinalGeral;
	private String formulaSubstituidaFinalGeral;
	private List<ConteudoRegistroAcessoVO> listaConteudoRegistroAcesso;

	public GestaoEventoConteudoTurmaControle() {

	}

	@PostConstruct
	public void init() {
		montarListaSelectItemTurmaProfessorCons();
		if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("emulandoGestao"))
				|| Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarGestaoEventoMonitoramentoEad"))
				|| Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarGestaoEventoMonitoramentoPbl"))) {
			carregarDadosConteudoControle();
		}
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	
	public void carregarDadosConteudoControle() {
		try {
			novo();
			getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("turmaGestao"));
			montarListaDisciplinaTurmaVisaoProfessor();
			getFiltroConsultaGestaoEventoConteudoTurma().setAno((String) context().getExternalContext().getSessionMap().get("anoGestao"));
			getFiltroConsultaGestaoEventoConteudoTurma().setSemestre((String) context().getExternalContext().getSessionMap().get("semestreGestao"));
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "nome", true));
			getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("disciplinaGestao"));
			montarComboBoxConteudo();
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO((ConteudoVO) context().getExternalContext().getSessionMap().get("conteudoGestao"));
			consultar();
			context().getExternalContext().getSessionMap().remove("emulandoGestao");
			context().getExternalContext().getSessionMap().remove("navegarGestaoEventoMonitoramentoEad");
			context().getExternalContext().getSessionMap().remove("navegarGestaoEventoMonitoramentoPbl");
			context().getExternalContext().getSessionMap().remove("conteudoGestao");
			context().getExternalContext().getSessionMap().remove("turmaGestao");
			context().getExternalContext().getSessionMap().remove("anoGestao");
			context().getExternalContext().getSessionMap().remove("semestreGestao");
			context().getExternalContext().getSessionMap().remove("disciplinaGestao");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String nagevarParaEmularAcessoConteudo() {
		context().getExternalContext().getSessionMap().put("emulandoGestao", true);
		context().getExternalContext().getSessionMap().put("conteudoGestao", getConteudoVO());
		context().getExternalContext().getSessionMap().put("turmaGestao", getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("anoGestao", getFiltroConsultaGestaoEventoConteudoTurma().getAno());
		context().getExternalContext().getSessionMap().put("semestreGestao", getFiltroConsultaGestaoEventoConteudoTurma().getSemestre());
		context().getExternalContext().getSessionMap().put("disciplinaGestao", getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo());
		return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAluno/conteudoAlunoForm.xhtml");
	}

	public String consultar() {
		try {
			if (getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo() == 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
			}
			setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorGestaoEventoConteudoTurma(getFiltroConsultaGestaoEventoConteudoTurma(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void novo() {
		setFiltroConsultaGestaoEventoConteudoTurma(new GestaoEventoConteudoTurmaVO());
		setListaSelectItemTurma(new ArrayList<SelectItem>());
		setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>());
		setConteudoVO(new ConteudoVO());
		getConteudoVO().setUnidadeConteudoVOs(new ArrayList<UnidadeConteudoVO>());
		montarListaSelectItemTurmaProfessorCons();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	public void montarListaSelectItemTurmaProfessorCons() {
		List<TurmaVO> listaTurmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					removerObjetoMemoria(turmaVO);
				}
			}

		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);			
			mapAuxiliarSelectItem = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), false, "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<TurmaVO>();
	}

	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() {
		try {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, false,getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<DisciplinaVO>();
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			limparMensagem();
			getFiltroConsultaGestaoEventoConteudoTurma().setAno("");
			getFiltroConsultaGestaoEventoConteudoTurma().setSemestre("");
			getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(new DisciplinaVO());
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(new ConteudoVO());
			getListaSelectItemDisciplinasTurma().clear();
			setConteudoVO(new ConteudoVO());
			if (getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo() != 0) {
				getFiltroConsultaGestaoEventoConteudoTurma().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));				
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public void limparFiltrosConsultaPorAno() {
		try {
			limparMensagem();
			if(!Uteis.isAtributoPreenchido(getFiltroConsultaGestaoEventoConteudoTurma().getAno())){				
				getFiltroConsultaGestaoEventoConteudoTurma().setSemestre("");
				setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>());
			}else if(Uteis.isAtributoPreenchido(getFiltroConsultaGestaoEventoConteudoTurma().getSemestre())){
				setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "nome", true));
			}
			getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(new DisciplinaVO());
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(new ConteudoVO());
			setListaSelectItemConteudo(new ArrayList<SelectItem>());
			setConteudoVO(new ConteudoVO());
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public void limparFiltrosConsultaPorSemestre() {
		try {
			limparMensagem();
			if(!Uteis.isAtributoPreenchido(getFiltroConsultaGestaoEventoConteudoTurma().getSemestre())){				
				setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>());
			}else if(Uteis.isAtributoPreenchido(getFiltroConsultaGestaoEventoConteudoTurma().getAno())){
				setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "nome", true));
			}
			getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(new DisciplinaVO());
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(new ConteudoVO());
			setListaSelectItemConteudo(new ArrayList<SelectItem>());
			setConteudoVO(new ConteudoVO());
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public String navegarGestaoConteudoAcessosRecursoEducacionalAnterior() {
		String retorno = null;
		GestaoEventoConteudoTurmaVO obj = null;
		setModalLiberacaoRecursoEducacional("");
		try {
			setConteudoUnidadePaginaRecursoEducacionalVO((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoAnterior"));
			if (getConteudoUnidadePaginaRecursoEducacionalVO().getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
				obj = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoComAvaliacaoPbl(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				carregarDadosGestaoEventoConteudo(obj);
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().verificarSePossuiNovasMatriculas(getConteudoUnidadePaginaRecursoEducacionalVO(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getAno(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getSemestre(), getUsuarioLogado().getPessoa().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				montarListaNotaConceitoAvaliacaoPbl();
				retorno = "gestaoEventoConteudoAcessosCons.xhtml";
			} else if (getConteudoUnidadePaginaRecursoEducacionalVO().getTipoRecursoEducacional().isTipoRecursoAtaPbl()) {
				obj = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarRapidaGestaoEventoConteudo(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				carregarDadosGestaoEventoConteudo(obj);
				montarListaSelectItemPessoa();
				retorno = "gestaoEventoConteudoAcessoAtaCons.xhtml";
			}
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
	}

	public String navegarGestaoConteudoAcessosRecursoEducacionalPosterior() {
		String retorno = null;
		GestaoEventoConteudoTurmaVO obj = null;
		setModalLiberacaoRecursoEducacional("");
		try {
			setConteudoUnidadePaginaRecursoEducacionalVO((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoPosterior"));
			if (getConteudoUnidadePaginaRecursoEducacionalVO().getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
				obj = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoComAvaliacaoPbl(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				carregarDadosGestaoEventoConteudo(obj);
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().verificarSePossuiNovasMatriculas(getConteudoUnidadePaginaRecursoEducacionalVO(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getAno(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getSemestre(), getUsuarioLogado().getPessoa().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				montarListaNotaConceitoAvaliacaoPbl();
				retorno = "gestaoEventoConteudoAcessosCons.xhtml";
			} else if (getConteudoUnidadePaginaRecursoEducacionalVO().getTipoRecursoEducacional().isTipoRecursoAtaPbl()) {
				obj = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarRapidaGestaoEventoConteudo(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				carregarDadosGestaoEventoConteudo(obj);
				montarListaSelectItemPessoa();
				retorno = "gestaoEventoConteudoAcessoAtaCons.xhtml";
			}
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
	}

	public void carregarDadosGestaoEventoConteudo(GestaoEventoConteudoTurmaVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj)) {
			setConteudoUnidadePaginaRecursoEducacionalVO(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarRapidaConteudoUnidadePaginaRecursoEducacionalPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacionalVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setTurmaVO(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO());
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setAno(getFiltroConsultaGestaoEventoConteudoTurma().getAno());
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setSemestre(getFiltroConsultaGestaoEventoConteudoTurma().getSemestre());
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setDisciplinaVO(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO());
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setConteudoVO(getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO());
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().inicializarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), getUsuarioLogado());
			if (!getConteudoUnidadePaginaRecursoEducacionalVO().getRequerLiberacaoProfessor()) {
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			}
		} else {
			setConteudoUnidadePaginaRecursoEducacionalVO(obj.getConteudoUnidadePaginaRecursoEducacionalVO());
			getConteudoUnidadePaginaRecursoEducacionalVO().setGestaoEventoConteudoTurmaVO(obj);
			if (Uteis.isAtributoPreenchido(obj) && obj.getSituacao().isPendente() && (Uteis.isAtributoPreenchido(obj.getDateLiberacao()) && obj.getDateLiberacao().compareTo(new Date()) <= 0)) {
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			}
		}
	}	
	
	public String navegarGestaoConteudoRegistroAcessoPorConteudo() {
		String retorno = null;
		try {
			
			getFiltroConsultaGestaoEventoConteudoTurma().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), NivelMontarDados.COMBOBOX, false, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoUnidadePaginaVO(new ConteudoUnidadePaginaVO());
			getFiltroConsultaGestaoEventoConteudoTurma().setUnidadeConteudoVO(new UnidadeConteudoVO());
			getFiltroConsultaGestaoEventoConteudoTurma().setTipoRecurso(TipoRecursoEnum.CONTEUDO);
			consultarGestaoConteudoRegistroAcesso();
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
			retorno = "monitoramentoAlunosRegistroAcessoCons.xhtml";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			retorno = "";
		}	
		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
	}
	
	public String navegarGestaoConteudoRegistroAcessoPorUnidadeConteudo() {
		String retorno = null;
		try {
			getFiltroConsultaGestaoEventoConteudoTurma().setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidade"));
			getFiltroConsultaGestaoEventoConteudoTurma().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), NivelMontarDados.COMBOBOX, false, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoUnidadePaginaVO(new ConteudoUnidadePaginaVO());
			getFiltroConsultaGestaoEventoConteudoTurma().setTipoRecurso(TipoRecursoEnum.UNIDADE_CONTEUDO);
			consultarGestaoConteudoRegistroAcesso();
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
			retorno = "monitoramentoAlunosRegistroAcessoCons.xhtml";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			retorno = "";
		}	
		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
	}
	
	public String navegarGestaoConteudoRegistroAcessoPorConteudoUnidadePagina() {
		String retorno = null;
		try {
			getFiltroConsultaGestaoEventoConteudoTurma().setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidade"));
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoUnidadePaginaVO((ConteudoUnidadePaginaVO) context().getExternalContext().getRequestMap().get("pagina"));
			getFiltroConsultaGestaoEventoConteudoTurma().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), NivelMontarDados.COMBOBOX, false, getUsuarioLogado()));
			getFiltroConsultaGestaoEventoConteudoTurma().setTipoRecurso(TipoRecursoEnum.CONTEUDO_UNIDADE_PAGINA);
			consultarGestaoConteudoRegistroAcesso();
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
			retorno = "monitoramentoAlunosRegistroAcessoCons.xhtml";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			retorno = "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
	}
	
	public void consultarGestaoConteudoRegistroAcesso() {
		try {
			setListaConteudoRegistroAcesso(getFacadeFactory().getConteudoRegistroAcessoFacade().consultarPorGestaoEventoConteudoTurma(getFiltroConsultaGestaoEventoConteudoTurma()));
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String voltarGestaoConteudoRegistroAcesso() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoEventoConteudoCons.xhtml");		
	}

	public void finalizarRecursoEducacional() {
		try {
			if (!getFacadeFactory().getGestaoEventoConteudoTurmaFacade().validarSeGestaoEventoConteudoTurmaNaoPossuiNotasNaoLancadas(getConteudoUnidadePaginaRecursoEducacionalVO())) {
				setModalAlertaNotasNaoLancadasRecursoEducacional("RichFaces.$('panelAlertaNotasNaoLancadasRecursoEducacional').show()");
				return;
			}
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().finalizarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudo_recursoEducacionalFinalizadoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setModalAlertaNotasNaoLancadasRecursoEducacional("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravarGestaoEventoConteudoTurmaFinalizado() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().finalizarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			setModalAlertaNotasNaoLancadasRecursoEducacional("RichFaces.$('panelAlertaNotasNaoLancadasRecursoEducacional').hide()");
			setMensagemID("msg_GestaoEventoConteudo_recursoEducacionalFinalizadoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setModalAlertaNotasNaoLancadasRecursoEducacional("RichFaces.$('panelAlertaNotasNaoLancadasRecursoEducacional').show()");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void reabrirRecursoEducacional() {
		try {
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.LIBERADO);
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().atualizarSituacaoGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO(), SituacaoPBLEnum.LIBERADO, false, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudo_recursoEducacionalRComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String voltarGestaoEventoConteudoTurma() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoEventoConteudoCons.xhtml");
	}

	public void montarComboBoxConteudo() {
		try {
			List<ConteudoVO> resultado = null;
			getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(new ConteudoVO());
			setConteudoVO(new ConteudoVO());
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				resultado = getFacadeFactory().getConteudoFacade().consultarPorCodigoDisciplinaTurmaAnoSemestre(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), NivelMontarDados.BASICO, getUsuarioLogado());
			}
			setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void calcularNotaFinalAcaoTabelaAutoAvaliacao() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = ((GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("gestaoEventoConteudoItem"));
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacionalVO(), obj, TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularNotaFinalAcaoTabelaAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO obj) {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacionalVO(), obj, TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularNotaFinalAcaoTabelaProfessor() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = ((GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("gestaoEventoConteudoItem"));
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacionalVO(), obj.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO(), TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularNotaFinal() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarCalculoNotaFinal(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularNotaFinalGeral() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().realizarCalculoNotaFinalGeralAvaliacaoPBL(getConteudoVO().getGestaoEventoConteudoTurmaVO(), getConteudoVO().getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarValidacaoDaFormulaParaNotaFinal() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().realizarValidacaoDaFormulaParaNotaFinal(getConteudoVO().getGestaoEventoConteudoTurmaVO(), getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_formulaCalculoAlterada", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaDeNotasDaConfiguracaoAcademico() {
		try {
			setConfiguracaoAcademicaVO(new ConfiguracaoAcademicoVO());
			setConfiguracaoAcademicaVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoASerUsadaLancamentoNota(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getListSelectItemVariavelNota().clear();
			if (Uteis.isAtributoPreenchido(getConfiguracaoAcademicaVO().getCodigo())) {
				getListSelectItemVariavelNota().addAll(getFacadeFactory().getConfiguracaoAcademicoFacade().montarListaSelectItemOpcoesDeNotas(getConfiguracaoAcademicaVO(), true, null));
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String realizarFechamentoNotaDosEventos() {
		try {
			getConteudoVO().setGestaoEventoConteudoTurmaVO(new GestaoEventoConteudoTurmaVO());
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().carregarDadosParaFechamentoNotasDosEventos(getConteudoVO(), getFiltroConsultaGestaoEventoConteudoTurma(), getUsuarioLogado());
			montarListaDeNotasDaConfiguracaoAcademico();
			return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoEventoConteudoAvaliacaoNotaFinalCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void gravarNotasHistoricoAlunos() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistirGestaoEventoConteudoTurmaAvaliacaNotaHistorico(getConteudoVO(), getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
			setModalPanelLancarNotaHistorico("RichFaces.$('panelLancarNotaHistorico').hide()");
		} catch (Exception e) {
			setModalPanelLancarNotaHistorico("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public void atualizarTelaGestaoEvento() {
		try {
			GestaoEventoConteudoTurmaVO obj = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoComAvaliacaoPbl(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getConteudoVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getAno(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			carregarDadosGestaoEventoConteudo(obj);
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().verificarSePossuiNovasMatriculas(getConteudoUnidadePaginaRecursoEducacionalVO(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getAno(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getSemestre(), getUsuarioLogado().getPessoa().getCodigo(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			montarListaNotaConceitoAvaliacaoPbl();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String gravarGestaoEventoConteudoValidandoDataLiberacao() {
		try {
			if (getConteudoUnidadePaginaRecursoEducacionalVO().getRequerLiberacaoProfessor() && !Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDateLiberacao()) && getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getSituacao().isPendente()) {
				setModalLiberacaoRecursoEducacional("RichFaces.$('panelLiberacaoGestaoEventoConteudoTurma').show()");
			} else {
				return gravarGestaoEventoConteudo();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String gravarGestaoEventoConteudo() {
		try {
			setModalLiberacaoRecursoEducacional("RichFaces.$('panelLiberacaoGestaoEventoConteudoTurma').hide()");
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String liberarGestaoEventoConteudo() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().preencherDadosGestaoEventoConteudoTurmaLiberadaParaLiberacao(getConteudoUnidadePaginaRecursoEducacionalVO());
			gravarGestaoEventoConteudo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String finalizarGestaoEventoConteudoAta() {
		try {
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.REALIZADO);
			gravarGestaoEventoConteudo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void adicionarGestaoEventoConteudoTurmaResponsavelAta() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().adicionarGestaoEventoConteudoTurmaResponsavelAtaVO(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO(), getGestaoEventoConteudoTurmaResponsavelAta(), getUsuarioLogado());
			setGestaoEventoConteudoTurmaResponsavelAta(new GestaoEventoConteudoTurmaResponsavelAtaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerGestaoEventoConteudoTurmaResponsavelAta() {
		try {
			GestaoEventoConteudoTurmaResponsavelAtaVO obj = (GestaoEventoConteudoTurmaResponsavelAtaVO) context().getExternalContext().getRequestMap().get("gestaoEventoConteudoTurmaResponsavelAtaItens");
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().removerGestaoEventoConteudoTurmaResponsavelAtaVO(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().adicionarGestaoEventoConteudoTurmaInteracaoAtaVO(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO(), getGestaoEventoConteudoTurmaInteracaoAta(), getUsuarioLogado());
			setGestaoEventoConteudoTurmaInteracaoAta(new GestaoEventoConteudoTurmaInteracaoAtaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editaGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			GestaoEventoConteudoTurmaInteracaoAtaVO obj = (GestaoEventoConteudoTurmaInteracaoAtaVO) context().getExternalContext().getRequestMap().get("gestaoInteracao");
			setGestaoEventoConteudoTurmaInteracaoAta(obj);
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			GestaoEventoConteudoTurmaInteracaoAtaVO obj = (GestaoEventoConteudoTurmaInteracaoAtaVO) context().getExternalContext().getRequestMap().get("gestaoInteracao");
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().removerGestaoEventoConteudoTurmaInteracaoAtaVO(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setGestaoEventoConteudoTurmaInteracaoAtaVOs(getFacadeFactory().getGestaoEventoConteudoTurmaInteracaoAtaFacade().consultarPorCodigoGestaoEventoConteudoTurmaVO(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirGestaoEventoConteudoTurmaInteracaoAta() {
		List<GestaoEventoConteudoTurmaVO> lista = new ArrayList<GestaoEventoConteudoTurmaVO>();
		lista.add(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO());
		try {
			getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseDesignIReportRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseDesignIReportRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(" Ata Evento.");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setListaObjetos(lista);
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// ////////////////////////////MINHAS NOTAS
	// PBL///////////////////////////////////////////////////////////////////////////////

	@PostConstruct
	public void navegarParaMinhasNotasPblVisaoAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
		try {
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAlunoControle != null && (Uteis.isAtributoPreenchido(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo()) || Uteis.isAtributoPreenchido(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina()))) {
				setFiltroConsultaGestaoEventoConteudoTurma(new GestaoEventoConteudoTurmaVO());
				getFiltroConsultaGestaoEventoConteudoTurma().setAno(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getAno());
				getFiltroConsultaGestaoEventoConteudoTurma().setSemestre(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
				getFiltroConsultaGestaoEventoConteudoTurma().setDisciplinaVO(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina());
				getFiltroConsultaGestaoEventoConteudoTurma().setTurmaVO(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getTurma());
				getFiltroConsultaGestaoEventoConteudoTurma().setConteudoVO(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo());
				getFiltroConsultaGestaoEventoConteudoTurma().setProfessor(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getProfessor());
				setListaMinhasNotasPbl(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarMinhaNotasPBLRapidaPorCodigoDisciplina(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getAno(), visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getUsuarioLogado().getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
				setMediaFinalGeralMinhaNotaPbl(0.0);
				setFormulaBasicaFinalGeral("");
				setFormulaSubstituidaFinalGeral("");
				if (!getListaMinhasNotasPbl().isEmpty()) {
					String formula = "";
					if(getListaMinhasNotasPbl().get(0).getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().getNota() != null){
						setMediaFinalGeralMinhaNotaPbl(getListaMinhasNotasPbl().get(0).getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().getNota());
						formula = getListaMinhasNotasPbl().get(0).getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().getFormulaResolvidaMediaFinal();
						setFormulaBasicaFinalGeral(formula);
						int index = 1;
						for (MinhasNotasPBLVO minhaNota : getListaMinhasNotasPbl()) {
							for (ConteudoUnidadePaginaRecursoEducacionalVO obj : minhaNota.getListaConteudoUnidadePaginaRecursoEducacional()) {
								if(obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().getNota() != null){
									formula = formula.replace("P" + index, obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().getNota().toString());
								}else{
									formula = formula.replace("P" + index, "0");
								}
								index++;
							}	
						}
						setFormulaSubstituidaFinalGeral(formula);
					}					
				}
			}
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		
		}
		}
	}

	public void carregarAvaliacaoPblVisaoAluno() {
		try {
			//MinhasNotasPBLVO minhaNota = ((MinhasNotasPBLVO) context().getExternalContext().getRequestMap().get("minhaNotaPbl"));
			setConteudoUnidadePaginaRecursoEducacionalVO(((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("conteudoUnidadePaginaRecursoEducacionalItem")));
			MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
			mptd.setAno(getFiltroConsultaGestaoEventoConteudoTurma().getAno());
			mptd.setSemestre(getFiltroConsultaGestaoEventoConteudoTurma().getSemestre());
			mptd.setDisciplina(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO());
			mptd.setTurma(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO());
			mptd.setConteudo(getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO());
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().verificarAvaliacaoPblRequerLiberacaoProfessor(getConteudoUnidadePaginaRecursoEducacionalVO(), mptd.getConteudo(), mptd, getUsuarioLogado());
			getConteudoUnidadePaginaRecursoEducacionalVO().setGestaoEventoConteudoTurmaVO(getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoComAvaliacaoPbl(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO()) && !getConteudoUnidadePaginaRecursoEducacionalVO().getRequerLiberacaoProfessor() ) {
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setProfessor(getFiltroConsultaGestaoEventoConteudoTurma().getProfessor());
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setTurmaVO(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO());
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setAno(getFiltroConsultaGestaoEventoConteudoTurma().getAno());
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setSemestre(getFiltroConsultaGestaoEventoConteudoTurma().getSemestre());
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setDisciplinaVO(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO());
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setConteudoVO(getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO());
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().inicializarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), getUsuarioLogado());
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO(), false, getUsuarioLogado());
			}else if(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO()) && 
					getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getSituacao().isPendente() && 
					(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDateLiberacao()) && getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().getDateLiberacao().compareTo(new Date()) <= 0)){
				getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.LIBERADO);
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().atualizarSituacaoGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO(), SituacaoPBLEnum.LIBERADO, true, getUsuarioLogado());
				
			}
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().verificarSePossuiNovasMatriculas(getConteudoUnidadePaginaRecursoEducacionalVO(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), getFiltroConsultaGestaoEventoConteudoTurma().getProfessor().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			setAutoAvaliacaoPbl(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAutoAvaliacao(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setListaAvaliados(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAvaliador(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaNotaConceitoAvaliacaoPbl();
			setModalAvalicaoAlunoMinhaNotaPbl("RichFaces.$('panelAddRecursoEducacional').show();");
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setModalAvalicaoAlunoMinhaNotaPbl("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void calcularNotaFinalAcaoTabelaAutoAvaliacaoMinhaNotaPbl() {
		try {			
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacionalVO(), getAutoAvaliacaoPbl(), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, getUsuarioLogado());
			if(getConteudoUnidadePaginaRecursoEducacionalVO().getAutoAvaliacao()){
				for (MinhasNotasPBLVO minhaNota : getListaMinhasNotasPbl()) {
					for (ConteudoUnidadePaginaRecursoEducacionalVO cupre : minhaNota.getListaConteudoUnidadePaginaRecursoEducacional()) {
						if(cupre.getCodigo().equals(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo())){
							if(!cupre.getUtilizarNotaConceito()){
								cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setNota(getAutoAvaliacaoPbl().getNota());	
							}else{
								cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setNotaConceitoAvaliacaoPBLVO(getAutoAvaliacaoPbl().getNotaConceitoAvaliacaoPBLVO());
							}
							break;
						}
					}
				}	
			}
			setMensagemID("msg_Conteudo_notasSalvasComSucesso", Uteis.SUCESSO);
			setModalAvalicaoAlunoMinhaNotaPbl("RichFaces.$('panelAddRecursoEducacional').hide();");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	
	
	public void calcularNotaFinalAcaoTabelaAlunoMinhaNotaPbl() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = ((GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("notaColega"));
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacionalVO(), obj, TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, getUsuarioLogado());
			setMensagemID("", "");
			setMensagem(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_notasLancadasComSucessoAlunoAvaliaAluno").replace("{0}", obj.getAvaliado().getNome()));
			setSucesso(true);
			setIconeMensagem("./imagens/sucesso.gif");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setIconeMensagem("./imagens/erro.gif");
			setSucesso(true);
		}
	}
	
	public void validarNotaMinimaMaximaConceitoAlunoAvaliaAluno() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO = (GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("notaColega");
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoAlunoAvaliaAluno(gestaoEventoConteudoTurmaAvaliacaoPBLVO, getConteudoUnidadePaginaRecursoEducacionalVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void validarNotaMinimaMaximaConceitoAutoAvaliacao() {
		try {
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoAutoAvaliacao(getAutoAvaliacaoPbl(), getConteudoUnidadePaginaRecursoEducacionalVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String navegarMonitorConhecimentoPBL() {
		context().getExternalContext().getSessionMap().put("navegarGestaoEventoMonitoramentoPbl", true);
		context().getExternalContext().getSessionMap().put("turmaGestao", getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("anoGestao", getFiltroConsultaGestaoEventoConteudoTurma().getAno());
		context().getExternalContext().getSessionMap().put("semestreGestao", getFiltroConsultaGestaoEventoConteudoTurma().getSemestre());
		context().getExternalContext().getSessionMap().put("disciplinaGestao", getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("conteudoGestao", getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao("monitoramentoAlunosPBLCons.xhtml");
	}
	
	public String navegarMonitorConhecimentoDisciplina() {
		context().getExternalContext().getSessionMap().put("navegarGestaoEventoMonitoramentoEad", true);
		context().getExternalContext().getSessionMap().put("turmaGestao", getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("anoGestao", getFiltroConsultaGestaoEventoConteudoTurma().getAno());
		context().getExternalContext().getSessionMap().put("semestreGestao", getFiltroConsultaGestaoEventoConteudoTurma().getSemestre());
		context().getExternalContext().getSessionMap().put("disciplinaGestao", getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("conteudoGestao", getFiltroConsultaGestaoEventoConteudoTurma().getConteudoVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao("monitoramentoAlunosEADVisaoProfessorCons.xhtml");
	}

	public void montarListaSelectItemPessoa() {
		try {
			montarListaSelectItemPessoa("");
		} catch (Exception e) {

		}
	}

	public void montarListaSelectItemPessoa(String prm) throws Exception {
		List resultadoConsulta = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorDisciplinaTurmaAnoSemestreUnidadeEnsino(getFiltroConsultaGestaoEventoConteudoTurma().getDisciplinaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getCodigo(), getFiltroConsultaGestaoEventoConteudoTurma().getAno(), getFiltroConsultaGestaoEventoConteudoTurma().getSemestre(), getUsuarioLogado());
		Iterator i = resultadoConsulta.iterator();
		getListaSelectPessoa().clear();
		getListaSelectPessoa().add(new SelectItem(0, ""));
		while (i.hasNext()) {
			PessoaVO obj = (PessoaVO) i.next();
			getListaSelectPessoa().add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
	}

	// Getters and Setters
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List<SelectItem> listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public List<SelectItem> getCampoSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}

	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}

	public List<SelectItem> getListaSelectItemConteudo() {
		if (listaSelectItemConteudo == null) {
			listaSelectItemConteudo = new ArrayList<SelectItem>();
		}
		return listaSelectItemConteudo;
	}

	public void setListaSelectItemConteudo(List<SelectItem> listaSelectItemConteudo) {
		this.listaSelectItemConteudo = listaSelectItemConteudo;
	}

	public boolean getIsApresentarAno() {
		return Uteis.isAtributoPreenchido(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO())
				? getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getSemestral() || getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getAnual()
				: false;
	}

	public boolean getIsApresentarSemestre() {
		return Uteis.isAtributoPreenchido(getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO())
				? getFiltroConsultaGestaoEventoConteudoTurma().getTurmaVO().getSemestral() : false;
	}

	public GestaoEventoConteudoTurmaVO getFiltroConsultaGestaoEventoConteudoTurma() {
		if (filtroConsultaGestaoEventoConteudoTurma == null) {
			filtroConsultaGestaoEventoConteudoTurma = new GestaoEventoConteudoTurmaVO();
		}
		return filtroConsultaGestaoEventoConteudoTurma;
	}

	public void setFiltroConsultaGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO) {
		this.filtroConsultaGestaoEventoConteudoTurma = gestaoEventoConteudoTurmaVO;
	}

	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacionalVO() {
		if (conteudoUnidadePaginaRecursoEducacionalVO == null) {
			conteudoUnidadePaginaRecursoEducacionalVO = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalVO(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) {
		this.conteudoUnidadePaginaRecursoEducacionalVO = conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public String getModalLiberacaoRecursoEducacional() {
		if (modalLiberacaoRecursoEducacional == null) {
			modalLiberacaoRecursoEducacional = "";
		}
		return modalLiberacaoRecursoEducacional;
	}

	public void setModalLiberacaoRecursoEducacional(String modalLiberacaoRecursoEducacional) {
		this.modalLiberacaoRecursoEducacional = modalLiberacaoRecursoEducacional;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getGestaoEventoConteudoTurmaAvaliacaoPBLVO() {
		if (gestaoEventoConteudoTurmaAvaliacaoPBLVO == null) {
			gestaoEventoConteudoTurmaAvaliacaoPBLVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLVO(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLVO = gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	}

	public String getModalAlertaNotasNaoLancadasRecursoEducacional() {
		if (modalAlertaNotasNaoLancadasRecursoEducacional == null) {
			modalAlertaNotasNaoLancadasRecursoEducacional = "";
		}
		return modalAlertaNotasNaoLancadasRecursoEducacional;
	}

	public void setModalAlertaNotasNaoLancadasRecursoEducacional(String modalAlertaNotasNaoLancadasRecursoEducacional) {
		this.modalAlertaNotasNaoLancadasRecursoEducacional = modalAlertaNotasNaoLancadasRecursoEducacional;
	}

	public List<SelectItem> getListSelectItemVariavelNota() {
		if (listSelectItemVariavelNota == null) {
			listSelectItemVariavelNota = new ArrayList<SelectItem>();
		}
		return listSelectItemVariavelNota;
	}

	public void setListSelectItemVariavelNota(List<SelectItem> listSelectItemVariavelNota) {
		this.listSelectItemVariavelNota = listSelectItemVariavelNota;
	}

	public String getVariavelTipoNota() {
		if (variavelTipoNota == null) {
			variavelTipoNota = "";
		}
		return variavelTipoNota;
	}

	public void setVariavelTipoNota(String variavelTipoNota) {
		this.variavelTipoNota = variavelTipoNota;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public GestaoEventoConteudoTurmaResponsavelAtaVO getGestaoEventoConteudoTurmaResponsavelAta() {
		if (gestaoEventoConteudoTurmaResponsavelAta == null) {
			gestaoEventoConteudoTurmaResponsavelAta = new GestaoEventoConteudoTurmaResponsavelAtaVO();
		}
		return gestaoEventoConteudoTurmaResponsavelAta;
	}

	public void setGestaoEventoConteudoTurmaResponsavelAta(GestaoEventoConteudoTurmaResponsavelAtaVO gestaoEventoConteudoTurmaResponsavelAta) {
		this.gestaoEventoConteudoTurmaResponsavelAta = gestaoEventoConteudoTurmaResponsavelAta;
	}

	public List<SelectItem> getListaSelectPessoa() {
		if (listaSelectPessoa == null) {
			listaSelectPessoa = new ArrayList<SelectItem>();
		}
		return listaSelectPessoa;
	}

	public void setListaSelectPessoa(List<SelectItem> listaSelectPessoa) {
		this.listaSelectPessoa = listaSelectPessoa;
	}

	public GestaoEventoConteudoTurmaInteracaoAtaVO getGestaoEventoConteudoTurmaInteracaoAta() {
		if (gestaoEventoConteudoTurmaInteracaoAta == null) {
			gestaoEventoConteudoTurmaInteracaoAta = new GestaoEventoConteudoTurmaInteracaoAtaVO();
		}
		return gestaoEventoConteudoTurmaInteracaoAta;
	}

	public void setGestaoEventoConteudoTurmaInteracaoAta(GestaoEventoConteudoTurmaInteracaoAtaVO gestaoEventoConteudoTurmaInteracaoAta) {
		this.gestaoEventoConteudoTurmaInteracaoAta = gestaoEventoConteudoTurmaInteracaoAta;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "ead" + File.separator + "GestaoEventoConteudoTurmaRel.jrxml");
	}

	public static String getCaminhoBaseDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "ead" + File.separator);
	}

	public void montarListaNotaConceitoAvaliacaoPbl() {
		try {
			if (getConteudoUnidadePaginaRecursoEducacionalVO().getUtilizarNotaConceito()) {
				if (getConteudoUnidadePaginaRecursoEducacionalVO().getNotaConceitoAvaliacaoPBLVOs().isEmpty()) {
					getConteudoUnidadePaginaRecursoEducacionalVO().setNotaConceitoAvaliacaoPBLVOs(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().consultarPorCodigoConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}
				if (getConteudoUnidadePaginaRecursoEducacionalVO().getAutoAvaliacao()) {
					setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(getConteudoUnidadePaginaRecursoEducacionalVO().getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, getUsuarioLogado()));
				}
				if (getConteudoUnidadePaginaRecursoEducacionalVO().getAlunoAvaliaAluno()) {
					setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(getConteudoUnidadePaginaRecursoEducacionalVO().getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, getUsuarioLogado()));
				}
				if (getConteudoUnidadePaginaRecursoEducacionalVO().getProfessorAvaliaAluno()) {
					setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(getConteudoUnidadePaginaRecursoEducacionalVO().getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO, getUsuarioLogado()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao() {
		if (listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao == null) {
			listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao) {
		this.listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	}

	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno() {
		if (listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno == null) {
			listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno) {
		this.listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	}

	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno() {
		if (listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno == null) {
			listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno) {
		this.listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	}

	/*public GestaoEventoConteudoTurmaAvaliacaoPBLVO getAvaliacaoAluno() {
		if (avaliacaoAluno == null) {
			avaliacaoAluno = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return avaliacaoAluno;
	}

	public void setAvaliacaoAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacaoAluno) {
		this.avaliacaoAluno = avaliacaoAluno;
	}*/

	public String getModalPanelLancarNotaHistorico() {
		return modalPanelLancarNotaHistorico;
	}

	public void setModalPanelLancarNotaHistorico(String modalPanelLancarNotaHistorico) {
		this.modalPanelLancarNotaHistorico = modalPanelLancarNotaHistorico;
	}

	public List<MinhasNotasPBLVO> getListaMinhasNotasPbl() {
		if (listaMinhasNotasPbl == null) {
			listaMinhasNotasPbl = new ArrayList<MinhasNotasPBLVO>();
		}
		return listaMinhasNotasPbl;
	}

	public void setListaMinhasNotasPbl(List<MinhasNotasPBLVO> listaMinhasNotasPbl) {
		this.listaMinhasNotasPbl = listaMinhasNotasPbl;
	}
	
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> getListaAvaliados() {
		if (listaAvaliados == null) {
			listaAvaliados = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		}
		return listaAvaliados;
	}

	public void setListaAvaliados(List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliados) {
		this.listaAvaliados = listaAvaliados;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getAutoAvaliacaoPbl() {
		if (autoAvaliacaoPbl == null) {
			autoAvaliacaoPbl = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return autoAvaliacaoPbl;
	}

	public void setAutoAvaliacaoPbl(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO) {
		this.autoAvaliacaoPbl = gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	}

	public Double getMediaFinalGeralMinhaNotaPbl() {
		return mediaFinalGeralMinhaNotaPbl;
	}

	public void setMediaFinalGeralMinhaNotaPbl(Double mediaFinalGeralMinhaNotaPbl) {
		this.mediaFinalGeralMinhaNotaPbl = mediaFinalGeralMinhaNotaPbl;
	}	

	public String getModalAvalicaoAlunoMinhaNotaPbl() {
		if(modalAvalicaoAlunoMinhaNotaPbl == null){
			modalAvalicaoAlunoMinhaNotaPbl = "";
		}
		return modalAvalicaoAlunoMinhaNotaPbl;
	}

	public void setModalAvalicaoAlunoMinhaNotaPbl(String modalAvalicaoAlunoMinhaNotaPbl) {
		this.modalAvalicaoAlunoMinhaNotaPbl = modalAvalicaoAlunoMinhaNotaPbl;
	}

	public String getFormulaBasicaFinalGeral() {
		if(formulaBasicaFinalGeral == null){
			formulaBasicaFinalGeral = "";
		}
		return formulaBasicaFinalGeral;
	}

	public void setFormulaBasicaFinalGeral(String formulaBasicaFinalGeral) {
		this.formulaBasicaFinalGeral = formulaBasicaFinalGeral;
	}

	public String getFormulaSubstituidaFinalGeral() {
		if(formulaSubstituidaFinalGeral == null){
			formulaSubstituidaFinalGeral = "";
		}
		return formulaSubstituidaFinalGeral;
	}

	public void setFormulaSubstituidaFinalGeral(String formulaSubstituidaFinalGeral) {
		this.formulaSubstituidaFinalGeral = formulaSubstituidaFinalGeral;
	}
	
	public List<ConteudoRegistroAcessoVO> getListaConteudoRegistroAcesso() {
		if(listaConteudoRegistroAcesso == null){
			listaConteudoRegistroAcesso = new ArrayList<ConteudoRegistroAcessoVO>();
		}
		return listaConteudoRegistroAcesso;
	}

	public void setListaConteudoRegistroAcesso(List<ConteudoRegistroAcessoVO> listaConteudoRegistroAcesso) {
		this.listaConteudoRegistroAcesso = listaConteudoRegistroAcesso;
	}
	
	
	/*public UIDataTable getDataTableAvaliacao() {
		return dataTableAvaliacao;
	}

	public void setDataTableAvaliacao(UIDataTable dataTableAvaliacao) {
		this.dataTableAvaliacao = dataTableAvaliacao;
	}

	public void montarDataTable(){
		setDataTableAvaliacao(getFacadeFactory().getGestaoEventoConteudoTurmaFacade().criarTabelaDinanica(UteisJSF.context(), getConteudoUnidadePaginaRecursoEducacionalVO().getGestaoEventoConteudoTurmaVO()));
		
	}
	
	public List<String> getTestString(){
		ArrayList<String> hobbits = new ArrayList<String>();
		hobbits.add("bilbo");
		hobbits.add("frodo");
		hobbits.add("merry");
		hobbits.add("pippin");
		hobbits.add("lumpy");
		return hobbits;
	}*/
}
