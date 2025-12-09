package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.EstatisticaAcademicaPorConvenioRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.EstatisticaAcademicaPorConvenioRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("EstatisticaAcademicaPorConvenioRelControle")
@Scope("viewScope")
@Lazy
public class EstatisticaAcademicaPorConvenioRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private TurmaVO turmaVO;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	private List<ParceiroVO> listaConsultaParceiro;
	private ParceiroVO parceiroVO;
	private List<SelectItem> listaSelectItemConvenio;
	private ConvenioVO convenioVO;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;
	private Double mediaAproveitamentoIni;
	private Double mediaAproveitamentoFim;
	private Double mediaFrequenciaIni;
	private Double mediaFrequenciaFim;
	private Double mediaNotaIni;
	private Double mediaNotaFim;
	List<EstatisticaAcademicaPorConvenioRelVO> estatisticaAcademicaPorConvenioRelVOs;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private Integer qtdeAlunoEnvioComunicacao;
	private Boolean tipoRelatorioImprimirExcell;
	private Boolean tipoRelatorioImprimirPDF;

	public EstatisticaAcademicaPorConvenioRelControle() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void consultarAlunos() {
		try {
			EstatisticaAcademicaPorConvenioRel.validarDados(getUnidadeEnsinoVO().getCodigo(), getParceiroVO().getCodigo(), getConvenioVO().getCodigo(), getFiltroRelatorioAcademico().getAno(), getFiltroRelatorioAcademico().getSemestre(), "");
			setEstatisticaAcademicaPorConvenioRelVOs(getFacadeFactory().getEstatisticaAcademicaPorConvenioRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getParceiroVO().getCodigo(), getConvenioVO().getCodigo(), getFiltroRelatorioAcademico().getAno(), getFiltroRelatorioAcademico().getSemestre(), getMediaAproveitamentoIni(), getMediaAproveitamentoFim(), getMediaFrequenciaIni(), getMediaFrequenciaFim(), getMediaNotaIni(), getMediaNotaFim(), ""));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirRelatorio() {
		List<EstatisticaAcademicaPorConvenioRelVO> listaObjetos = new ArrayList<EstatisticaAcademicaPorConvenioRelVO>(0);
		try {
			EstatisticaAcademicaPorConvenioRel.validarDados(getUnidadeEnsinoVO().getCodigo(), getParceiroVO().getCodigo(), getConvenioVO().getCodigo(), getFiltroRelatorioAcademico().getAno(), getFiltroRelatorioAcademico().getSemestre(), "");
			listaObjetos = getFacadeFactory().getEstatisticaAcademicaPorConvenioRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getParceiroVO().getCodigo(), getConvenioVO().getCodigo(), getFiltroRelatorioAcademico().getAno(), getFiltroRelatorioAcademico().getSemestre(), getMediaAproveitamentoIni(), getMediaAproveitamentoFim(), getMediaFrequenciaIni(), getMediaFrequenciaFim(), getMediaNotaIni(), getMediaNotaFim(), "");
			if (!listaObjetos.isEmpty()) {
				if (getTipoRelatorioImprimirExcell()) {
					getSuperParametroRelVO().setNomeDesignIreport(EstatisticaAcademicaPorConvenioRel.designIReportRelatorioExcell());	
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(EstatisticaAcademicaPorConvenioRel.designIReportRelatorio());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				}
				getSuperParametroRelVO().setSubReport_Dir(EstatisticaAcademicaPorConvenioRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("ESTATÍSTICA ACADÊMICA POR CONVÊNIO");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EstatisticaAcademicaPorConvenioRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				if (!getUnidadeEnsinoCursoVO().getCurso().getCodigo().equals(0)) {
					getSuperParametroRelVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()).getNome());
				} else {
					getSuperParametroRelVO().setCurso("Todos");
				}
				if (!getTurmaVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("Todas");
				}
				if (!getParceiroVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(getParceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				} else {
					getSuperParametroRelVO().setParceiro("Todas");
				}
				if (!getConvenioVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().adicionarParametro("convenio", getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(getConvenioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("convenio", "Todos");
				}
				getSuperParametroRelVO().adicionarParametro("mediaFrequencia", getMediaFrequenciaIni() + " à " + getMediaFrequenciaFim());
				getSuperParametroRelVO().adicionarParametro("mediaNotas", getMediaNotaIni() + " à " + getMediaNotaFim());
				getSuperParametroRelVO().adicionarParametro("mediaAproveitamento", getMediaAproveitamentoIni() + " à " + getMediaAproveitamentoFim());
				getSuperParametroRelVO().setAno(getFiltroRelatorioAcademico().getAno());
				getSuperParametroRelVO().setSemestre(getFiltroRelatorioAcademico().getSemestre());
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> itens = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(itens, "codigo", "nome"));
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			limparCurso();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCursoVO(obj);
			getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsino());
			limparTurma();
		} catch (Exception e) {
		}
	}

	public void limparCurso() {
		try {
			setUnidadeEnsinoCursoVO(null);
			setListaConsultaCurso(null);
			limparTurma();
		} catch (Exception e) {
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() == 0) {
				setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getTurmaVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getTurmaVO().getTurno().getCodigo(), getUsuarioLogado()));
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoCursoVO().getUnidadeEnsino());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			setListaConsultaTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparParceiro() {
		try {
			setParceiroVO(null);
			setListaConsultaParceiro(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarParceiro() {
		try {
			List<ParceiroVO> objs = new ArrayList<ParceiroVO>(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getValorConsultaParceiro().equals("")) {
					setValorConsultaParceiro("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaParceiro());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaParceiro(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			setParceiroVO(obj);
			List<ConvenioVO> itens = getFacadeFactory().getConvenioFacade().consultarPorParceiro(obj.getCodigo(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemConvenio(UtilSelectItem.getListaSelectItem(itens, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarPreencherTodosLista() {
		try {
			for (EstatisticaAcademicaPorConvenioRelVO obj : getEstatisticaAcademicaPorConvenioRelVOs()) {
				obj.setSelecionado(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarDesmarcarTodosLista() {
		try {
			for (EstatisticaAcademicaPorConvenioRelVO obj : getEstatisticaAcademicaPorConvenioRelVOs()) {
				obj.setSelecionado(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararEnvioComunicadoInternoAluno() {
		try {
			setQtdeAlunoEnvioComunicacao(0);
			for (EstatisticaAcademicaPorConvenioRelVO obj : getEstatisticaAcademicaPorConvenioRelVOs()) {
				if (obj.getSelecionado()) {
					qtdeAlunoEnvioComunicacao += 1;
				}
			}
			if (getQtdeAlunoEnvioComunicacao() <= 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_EstatisticaAcademicaPorConvenioRel_selecionarAlunoEnvioComunicacaoInterna"));
			}
//			if (!getComunicacaoInternaVO().getMensagem().contains("../imagens/")) {
//				getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO(). getComunicacaoInternaVO().getMensagem().replace("imagens/", "../imagens/"));
//			}
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarEnvioComunicadoInterno() {
		try {
			getFacadeFactory().getEstatisticaAcademicaPorConvenioRelFacade().executarEnvioComunicadoInternoAluno(getEstatisticaAcademicaPorConvenioRelVOs(), getComunicacaoInternaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setEstatisticaAcademicaPorConvenioRelVOs(null);
			setMensagemID("msg_msg_emailsEnviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getExibirModalComunicadoInterno() {
		if (getQtdeAlunoEnvioComunicacao() >= 1) {
			return "RichFaces.$('panelComunicacaoInterna').show()";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboParceiro() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public List<SelectItem> getListaSelectItemConvenio() {
		if (listaSelectItemConvenio == null) {
			listaSelectItemConvenio = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConvenio;
	}

	public void setListaSelectItemConvenio(List<SelectItem> listaSelectItemConvenio) {
		this.listaSelectItemConvenio = listaSelectItemConvenio;
	}

	public ConvenioVO getConvenioVO() {
		if (convenioVO == null) {
			convenioVO = new ConvenioVO();
		}
		return convenioVO;
	}

	public void setConvenioVO(ConvenioVO convenioVO) {
		this.convenioVO = convenioVO;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademico() {
		if (filtroRelatorioAcademico == null) {
			filtroRelatorioAcademico = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademico;
	}

	public void setFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademico) {
		this.filtroRelatorioAcademico = filtroRelatorioAcademico;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public List<SelectItem> getListaSelectItemAno() {
		try {
			
			if (listaSelectItemAno == null) {
				listaSelectItemAno = new ArrayList<SelectItem>(0);
				List<String> anos = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnosMatriculaPeriodo();
				for (String ano : anos) {
					listaSelectItemAno.add(new SelectItem(ano, ano));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}

	public Double getMediaFrequenciaIni() {
		if (mediaFrequenciaIni == null) {
			mediaFrequenciaIni = 0.0;
		}
		return mediaFrequenciaIni;
	}

	public void setMediaFrequenciaIni(Double mediaFrequenciaIni) {
		this.mediaFrequenciaIni = mediaFrequenciaIni;
	}

	public Double getMediaFrequenciaFim() {
		if (mediaFrequenciaFim == null) {
			mediaFrequenciaFim = 100.0;
		}
		return mediaFrequenciaFim;
	}

	public void setMediaFrequenciaFim(Double mediaFrequenciaFim) {
		this.mediaFrequenciaFim = mediaFrequenciaFim;
	}

	public Double getMediaNotaIni() {
		if (mediaNotaIni == null) {
			mediaNotaIni = 0.0;
		}
		return mediaNotaIni;
	}

	public void setMediaNotaIni(Double mediaNotaIni) {
		this.mediaNotaIni = mediaNotaIni;
	}

	public Double getMediaNotaFim() {
		if (mediaNotaFim == null) {
			mediaNotaFim = 10.0;
		}
		return mediaNotaFim;
	}

	public void setMediaNotaFim(Double mediaNotaFim) {
		this.mediaNotaFim = mediaNotaFim;
	}

	public Double getMediaAproveitamentoIni() {
		if (mediaAproveitamentoIni == null) {
			mediaAproveitamentoIni = 0.0;
		}
		return mediaAproveitamentoIni;
	}

	public void setMediaAproveitamentoIni(Double mediaAproveitamentoIni) {
		this.mediaAproveitamentoIni = mediaAproveitamentoIni;
	}

	public Double getMediaAproveitamentoFim() {
		if (mediaAproveitamentoFim == null) {
			mediaAproveitamentoFim = 100.0;
		}
		return mediaAproveitamentoFim;
	}

	public void setMediaAproveitamentoFim(Double mediaAproveitamentoFim) {
		this.mediaAproveitamentoFim = mediaAproveitamentoFim;
	}

	public List<EstatisticaAcademicaPorConvenioRelVO> getEstatisticaAcademicaPorConvenioRelVOs() {
		if (estatisticaAcademicaPorConvenioRelVOs == null) {
			estatisticaAcademicaPorConvenioRelVOs = new ArrayList<EstatisticaAcademicaPorConvenioRelVO>(0);
		}
		return estatisticaAcademicaPorConvenioRelVOs;
	}

	public void setEstatisticaAcademicaPorConvenioRelVOs(List<EstatisticaAcademicaPorConvenioRelVO> estatisticaAcademicaPorConvenioRelVOs) {
		this.estatisticaAcademicaPorConvenioRelVOs = estatisticaAcademicaPorConvenioRelVOs;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if (comunicacaoInternaVO == null) {
			comunicacaoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	public Integer getQtdeAlunoEnvioComunicacao() {
		if (qtdeAlunoEnvioComunicacao == null) {
			qtdeAlunoEnvioComunicacao = 0;
		}
		return qtdeAlunoEnvioComunicacao;
	}

	public void setQtdeAlunoEnvioComunicacao(Integer qtdeAlunoEnvioComunicacao) {
		this.qtdeAlunoEnvioComunicacao = qtdeAlunoEnvioComunicacao;
	}

	public Boolean getTipoRelatorioImprimirExcell() {
		if (tipoRelatorioImprimirExcell == null) {
			tipoRelatorioImprimirExcell = false;
		}
		return tipoRelatorioImprimirExcell;
	}

	public void setTipoRelatorioImprimirExcell(Boolean tipoRelatorioImprimirExcell) {
		this.tipoRelatorioImprimirExcell = tipoRelatorioImprimirExcell;
	}

	public Boolean getTipoRelatorioImprimirPDF() {
		if (tipoRelatorioImprimirPDF == null) {
			tipoRelatorioImprimirPDF = false;
		}
		return tipoRelatorioImprimirPDF;
	}

	public void setTipoRelatorioImprimirPDF(Boolean tipoRelatorioImprimirPDF) {
		this.tipoRelatorioImprimirPDF = tipoRelatorioImprimirPDF;
	}

	
}
