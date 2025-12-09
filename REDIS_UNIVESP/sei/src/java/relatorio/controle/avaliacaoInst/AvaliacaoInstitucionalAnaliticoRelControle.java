package relatorio.controle.avaliacaoInst;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalCursoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalPorSinteticoPorCursoVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalPorTurmaSinteticoVO;

@Controller("AvaliacaoInstitucionalAnaliticoRelControle")
@Lazy
@Scope("viewScope")
public class AvaliacaoInstitucionalAnaliticoRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5271468584872797824L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private TurmaVO turmaVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemAvaliacaoInstitucional;
	private List<SelectItem> listaSelectItemLayout;
	private List<SelectItem> listaSelectItemSituacaoResposta;
	private List<SelectItem> listaSelectItemOrdenarPor;
	private List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs;
	private List<AvaliacaoInstitucionalPorTurmaSinteticoVO> avaliacaoInstitucionalPorTurmaSinteticoVOs;
	private List<AvaliacaoInstitucionalPorSinteticoPorCursoVO> avaliacaoInstitucionalPorSinteticoPorCursoVOs;	
	private Boolean enviarEmail;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	protected List<TurmaVO> listaConsultaTurma;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO;
	protected String situacaoResposta;
	protected String layout;
	protected String ordenarPor;
	protected Date dataInicio;
	protected Date dataFim;
	private Integer qtdeTotal;
	private Integer qtdeTotalResponderam;
	private Integer qtdeTotalNaoResponderam;
	private Double percentualResponderam;
	private Double percentualNaoResponderam;
	private String campoConsultaAvaliacao;
	private String valorConsultaAvaliacao;
	private List<QuestionarioVO> questionarioVOs;
	
	public List<SelectItem> tipoConsultaComboCurso;
	public List<SelectItem> tipoConsultaComboTurma;
	private Boolean utilizarListagemRespondente;

	public void alterarAvaliacaoInstitucional() {
		try {
			setAvaliacaoInstitucionalVO(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorChavePrimaria(getAvaliacaoInstitucionalVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getUnidadeEnsinoVO().setCodigo(getAvaliacaoInstitucionalVO().getUnidadeEnsino().getCodigo());
			getCursoVO().setCodigo(getAvaliacaoInstitucionalVO().getCurso().getCodigo());
			getCursoVO().setNome(getAvaliacaoInstitucionalVO().getCurso().getNome());
			getTurmaVO().setCodigo(getAvaliacaoInstitucionalVO().getTurma().getCodigo());
			getTurmaVO().setIdentificadorTurma(getAvaliacaoInstitucionalVO().getTurma().getIdentificadorTurma());
			if(getAvaliacaoInstitucionalVO().getAvaliacaoUltimoModulo()) {
				setDataInicio(Uteis.obterDataAntiga(new Date(), getAvaliacaoInstitucionalVO().getDiasDisponivel()));
				setDataFim(new Date());
			}else {
				setDataInicio(getAvaliacaoInstitucionalVO().getDataInicio());
				setDataFim(getAvaliacaoInstitucionalVO().getDataFinal());
			}
			montarListaSelectItemTurnos();
			getAvaliacaoInstitucionalAnaliticoRelVOs().clear();
			getTurnoVO().setCodigo(0);
			getListaConsultaCurso().clear();
			if(!getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
				for(AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVOs : getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalCursoVOs()) {
					getListaConsultaCurso().add(avaliacaoInstitucionalCursoVOs.getCursoVO());
				}
			}
			setLayout("ANALITICO");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTodos() {
		for (AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO : getAvaliacaoInstitucionalAnaliticoRelVOs()) {
			if(!avaliacaoInstitucionalAnaliticoRelVO.getJaRespondeu()){
				avaliacaoInstitucionalAnaliticoRelVO.setEnviarEmail(getEnviarEmail());
			}
		}
	}

	public void alterarUnidadeEnsino() {
		limparCurso();
		limparTurma();
		setTurnoVO(null);
		montarListaSelectItemTurnos();
	}

	public void realizarGeracaoRelatorio() {
		try {
			if(getAvaliacaoInstitucionalVO().getSituacao().equals("AT") || getAvaliacaoInstitucionalVO().getSituacao().equals("FI")) {
				setUtilizarListagemRespondente(Boolean.TRUE);
			}
			else {
				setUtilizarListagemRespondente(Boolean.FALSE);
			}
			
			if (getLayout().equals("ANALITICO")) {
				getAvaliacaoInstitucionalAnaliticoRelVOs().clear();
				
				setAvaliacaoInstitucionalAnaliticoRelVOs(getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarGeracaoRelatorioAnalitico(getUnidadeEnsinoVO().getCodigo(), getAvaliacaoInstitucionalVO(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoResposta(), getOrdenarPor(), getDataInicio(), getDataFim(), getUtilizarListagemRespondente(), getUsuarioLogado(), false));
			} else if (getLayout().equals("SINTETICO")) {
				getAvaliacaoInstitucionalPorTurmaSinteticoVOs().clear();
				setAvaliacaoInstitucionalPorTurmaSinteticoVOs(getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().consultarRelatorioSintetico(getUnidadeEnsinoVO().getCodigo(), getAvaliacaoInstitucionalVO(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoResposta(), getDataInicio(), getDataFim(), getUsuarioLogado()));
			} else {
				//SINTCURSO				
				getAvaliacaoInstitucionalPorSinteticoPorCursoVOs().clear();
				setAvaliacaoInstitucionalPorSinteticoPorCursoVOs(getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().consultarRelatorioSinteticoPorCurso(getUnidadeEnsinoVO().getCodigo(), getAvaliacaoInstitucionalVO(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoResposta(), getDataInicio(), getDataFim(), getUsuarioLogado()));				
			}
			realizarCalculoTotais();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarImpressaoRelatorioPDF() {
		realizarImpressaoRelatorio(TipoRelatorioEnum.PDF);
	}

	public void realizarImpressaoRelatorioEXCEL() {
		realizarImpressaoRelatorio(TipoRelatorioEnum.EXCEL);
	}

	@SuppressWarnings("rawtypes")
	public void realizarImpressaoRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
		try {
			if(getAvaliacaoInstitucionalVO().getSituacao().equals("AT") || getAvaliacaoInstitucionalVO().getSituacao().equals("FI")) {
				setUtilizarListagemRespondente(Boolean.TRUE);
			}
			else {
				setUtilizarListagemRespondente(Boolean.FALSE);
			}
			List listaResultado = null;
			if (getLayout().equals("ANALITICO")) {
				listaResultado = getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarGeracaoRelatorioAnalitico(getUnidadeEnsinoVO().getCodigo(), getAvaliacaoInstitucionalVO(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoResposta(), getOrdenarPor(), getDataInicio(), getDataFim(), getUtilizarListagemRespondente(), getUsuarioLogado(), false);				
			} else if (getLayout().equals("SINTETICO")) {
				listaResultado = getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().consultarRelatorioSintetico(getUnidadeEnsinoVO().getCodigo(), getAvaliacaoInstitucionalVO(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoResposta(), getDataInicio(), getDataFim(), getUsuarioLogado());
			}else {
				//SINTCURSO
				listaResultado = getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().consultarRelatorioSinteticoPorCurso(getUnidadeEnsinoVO().getCodigo(), getAvaliacaoInstitucionalVO(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoResposta(), getDataInicio(), getDataFim(), getUsuarioLogado());
			}

			if (!listaResultado.isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				if (getLayout().equals("ANALITICO")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional" + File.separator + "ListagemRespondenteAvaliacaoInstitucionalAnaliticoRel.jrxml");
				} else if (getLayout().equals("SINTETICO")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional" + File.separator + "ListagemTurmaAvaliacaoInstitucionalSintetivoRel.jrxml");
				} else {
					//SINTCURSO
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional" + File.separator + "ListagemCursoAvaliacaoInstitucionalSintetivoRel.jrxml");
				}
				
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional");
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(UteisJSF.internacionalizar("prt_AvaliacaoInstitucionalAnaliticoRel_tituloForm"));
				getSuperParametroRelVO().setListaObjetos(listaResultado);
				getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional");
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("avaliacaoInstitucional", getAvaliacaoInstitucionalVO().getNome());
				if (!getAvaliacaoInstitucionalVO().getPublicoAlvo_FuncionarioGestor()) {
					if (getUnidadeEnsinoVO().getCodigo() > 0) {
						setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
						getSuperParametroRelVO().adicionarParametro("unidadeEnsino", getUnidadeEnsinoVO().getNome());
					} else {
						getSuperParametroRelVO().adicionarParametro("unidadeEnsino", "Todas");
					}

					if (getCursoVO().getCodigo() > 0) {
						getSuperParametroRelVO().adicionarParametro("curso", getCursoVO().getNome());
					} else {
						getSuperParametroRelVO().adicionarParametro("curso", "Todos");
					}
					if (getTurnoVO().getCodigo() > 0) {
						setTurnoVO(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(getTurnoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
						getSuperParametroRelVO().adicionarParametro("turno", getTurnoVO().getNome());
					} else {
						getSuperParametroRelVO().adicionarParametro("turno", "Todos");
					}
					if (getTurmaVO().getCodigo() > 0) {
						getSuperParametroRelVO().adicionarParametro("turma", getTurmaVO().getIdentificadorTurma());
					} else {
						getSuperParametroRelVO().adicionarParametro("turma", "Todas");
					}
				}
				getSuperParametroRelVO().adicionarParametro("aluno", getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso() || getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos() || getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma());
				getSuperParametroRelVO().adicionarParametro("funcionario", getAvaliacaoInstitucionalVO().getPublicoAlvo_FuncionarioGestor());
				getSuperParametroRelVO().adicionarParametro("situacaoResposta", getSituacaoResposta().equals("TODAS") ? "Todas" : getSituacaoResposta().equals("RESPONDIDO") ? "Respondidos" : "Não Respondidos");
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private Boolean emailEnviadoSucesso;

	public void realizarEnvioEmail() {
		try {
			getPersonalizacaoMensagemAutomaticaVO().setDesabilitarEnvioMensagemAutomatica(false);
			getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarEnvioEmail(getAvaliacaoInstitucionalVO(), getAvaliacaoInstitucionalAnaliticoRelVOs(), getPersonalizacaoMensagemAutomaticaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setEmailEnviadoSucesso(true);
			setMensagemID("msg_msg_emailsEnviados");
		} catch (Exception e) {
			setEmailEnviadoSucesso(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getFecharModalEmail() {
		if (getEmailEnviadoSucesso()) {
			return "RichFaces.$('panelMensagem').hide()";
		}
		return "";
	}

	public Boolean getEmailEnviadoSucesso() {
		if (emailEnviadoSucesso == null) {
			emailEnviadoSucesso = false;
		}
		return emailEnviadoSucesso;
	}

	public void setEmailEnviadoSucesso(Boolean emailEnviadoSucesso) {
		this.emailEnviadoSucesso = emailEnviadoSucesso;
	}

	public void inicializarDadosMensagem() {
		try {
			setPersonalizacaoMensagemAutomaticaVO(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().executarGeracaoMensagemPadraoTemplateEspecifico(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_RESPONDENTE_AVALIACAO_INSTITUCIONAL, false, getUsuarioLogado()));
			getPersonalizacaoMensagemAutomaticaVO().setMensagem(getPersonalizacaoMensagemAutomaticaVO().getMensagem().replace("src=\"../resources/", "src=\"../../resources/"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemID("msg_erro", e.getMessage());
		}		
	}

	@PostConstruct
	public void inicializarListaSelectItem() {
		//montarListaSelectItemAvaliacaoInstitucional();
		montarListaSelectItemUnidadesEnsino();
		montarListaSelectItemTurnos();
	}

	public void montarListaSelectItemAvaliacaoInstitucional() {
		try {
			List<AvaliacaoInstitucionalVO> resultadoConsulta = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorNome("", null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//			Ordenacao.ordenarListaDecrescente(resultadoConsulta, "dataInicio");
			Iterator<AvaliacaoInstitucionalVO> i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			while (i.hasNext()) {
				AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
				if (getAvaliacaoInstitucionalVO().getCodigo() == 0) {
					getAvaliacaoInstitucionalVO().setCodigo(obj.getCodigo());
					alterarAvaliacaoInstitucional();
				}
			}
			setListaSelectItemAvaliacaoInstitucional(objs);
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemUnidadesEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			Iterator<UnidadeEnsinoVO> i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				objs.add(new SelectItem(0, ""));
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List<SelectItem>) objs, ordenador);
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurnos() {
		try {

			List<TurnoVO> resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorCodigoCursoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			Iterator<TurnoVO> i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, "TODOS"));
			while (i.hasNext()) {
				TurnoVO obj = (TurnoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List<SelectItem>) objs, ordenador);
			setListaSelectItemTurno(objs);
		} catch (Exception e) {

		}
	}

	public void consultarCurso() {
		try {
			super.consultar();
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				if (!getValorConsultaCurso().equals("")) {
					if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
						objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					} else {
						objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					}
				} else {
					throw new Exception("Pelo menos um valor deve ser Informado.");
				}
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		setCursoVO((CursoVO) getRequestMap().get("cursoItens"));
		limparTurma();
		montarListaSelectItemTurnos();
	}

	public void limparCurso() {
		setCursoVO(null);
		montarListaSelectItemTurnos();
	}
	
	public void limparAvaliacao() {
		getAvaliacaoInstitucionalAnaliticoRelVOs().clear();
		setAvaliacaoInstitucionalVO(null);
		getUnidadeEnsinoVO().setCodigo(0);		
		getCursoVO().setCodigo(0);
		getCursoVO().setNome("");
		getTurmaVO().setCodigo(0);
		getTurmaVO().setIdentificadorTurma("");
		setDataInicio(null);
		setDataFim(null);
		getListaSelectItemTurno().clear();
		getTurnoVO().setCodigo(0);
		setLayout("ANALITICO");
	}

	public void consultarTurmaPorIdentificador() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnicoCursoTurno(getTurmaVO().getIdentificadorTurma(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setCursoVO(getTurmaVO().getCurso());
			setTurnoVO(getTurmaVO().getTurno());
			setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,"", getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		setTurmaVO(null);
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			setCursoVO(obj.getCurso());
			setTurnoVO(obj.getTurno());
			setUnidadeEnsinoVO(obj.getUnidadeEnsino());
			obj = null;
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void realizarCalculoTotais(){
		setQtdeTotal(0);
		setQtdeTotalNaoResponderam(0);
		setQtdeTotalResponderam(0);		
		setPercentualNaoResponderam(0.0);
		setPercentualResponderam(0.0);
		if(getLayout().equals("ANALITICO")){
			realizarCalculoTotaisAnalitico();
		}else if(getLayout().equals("SINTETICO")){
			realizarCalculoTotaisSinteticoPorTurma();
		}else{
			realizarCalculoTotaisSinteticoPorCurso();
		}
		if(getQtdeTotal()>0){
			setPercentualNaoResponderam(Uteis.arrendondarForcando2CadasDecimais((getQtdeTotalNaoResponderam()*100.0)/getQtdeTotal()));
			setPercentualResponderam(Uteis.arrendondarForcando2CadasDecimais((getQtdeTotalResponderam()*100.0)/getQtdeTotal()));
		}
	}	
	
	private void realizarCalculoTotaisAnalitico(){
		for(AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO : avaliacaoInstitucionalAnaliticoRelVOs){
			setQtdeTotal(getQtdeTotal()+1);
			if(avaliacaoInstitucionalAnaliticoRelVO.getJaRespondeu()){
				setQtdeTotalResponderam(getQtdeTotalResponderam()+1);
			}else{
				setQtdeTotalNaoResponderam(getQtdeTotalNaoResponderam()+1);
			}
		}
	}
	
	private void realizarCalculoTotaisSinteticoPorTurma(){
		for(AvaliacaoInstitucionalPorTurmaSinteticoVO avaliacaoInstitucionalPorTurmaSinteticoVO:getAvaliacaoInstitucionalPorTurmaSinteticoVOs()){
			setQtdeTotal(getQtdeTotal()+avaliacaoInstitucionalPorTurmaSinteticoVO.getQtdeTotal());			
			setQtdeTotalResponderam(getQtdeTotalResponderam()+avaliacaoInstitucionalPorTurmaSinteticoVO.getQtdeRespondeu());			
			setQtdeTotalNaoResponderam(getQtdeTotalNaoResponderam()+avaliacaoInstitucionalPorTurmaSinteticoVO.getQtdeNaoRespondeu());			
		}
	}
	private void realizarCalculoTotaisSinteticoPorCurso(){
		for(AvaliacaoInstitucionalPorSinteticoPorCursoVO avaliacaoInstitucionalPorSinteticoPorCursoVO:getAvaliacaoInstitucionalPorSinteticoPorCursoVOs()){
			setQtdeTotal(getQtdeTotal()+avaliacaoInstitucionalPorSinteticoPorCursoVO.getQtdeTotal());			
			setQtdeTotalResponderam(getQtdeTotalResponderam()+avaliacaoInstitucionalPorSinteticoPorCursoVO.getQtdeRespondeu());			
			setQtdeTotalNaoResponderam(getQtdeTotalNaoResponderam()+avaliacaoInstitucionalPorSinteticoPorCursoVO.getQtdeNaoRespondeu());			
		}
	}
	
	
	public void consultarAvaliacaoInstitucionalResponder() {
        try {
        	AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO = (AvaliacaoInstitucionalAnaliticoRelVO) getRequestMap().get("pessoaItens");
        	if(!getAvaliacaoInstitucionalVO().getNivelMontarDados().equals(NivelMontarDados.TODOS)){
        		setAvaliacaoInstitucionalVO(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorChavePrimaria(getAvaliacaoInstitucionalVO().getCodigo(),Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        		getAvaliacaoInstitucionalVO().setNivelMontarDados(NivelMontarDados.TODOS);
        	}
        	if(getUsuario() == null){
        		setUsuario(new UsuarioVO());
        	}        	
        	getUsuario().getPessoa().setNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
        	if (!avaliacaoInstitucionalAnaliticoRelVO.getMatricula().equals("") && !avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
        		avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().setMatricula(avaliacaoInstitucionalAnaliticoRelVO.getMatricula());
        		getFacadeFactory().getMatriculaFacade().carregarDados(avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO(), getUsuarioLogado());
        		avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getMatriculaPeriodoVOs().add(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getMatricula(), getAvaliacaoInstitucionalVO().getAno(), getAvaliacaoInstitucionalVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), ""));
        		avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().setNivelMontarDados(NivelMontarDados.TODOS);
        	}
//            if (getAvaliacaoInstitucionalVO().getQuestionarioVO().getEscopo().equals("UM") && !avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getMatriculaPeriodoVOs().isEmpty()) {
//                if (!avaliacaoInstitucionalAnaliticoRelVO.getMatricula().equals("")) {
//	                Date dataUltimoModulo = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaProgramadaMenorDataAtual(avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getMatriculaPeriodoVOs().get(0).getTurma().getCodigo());
//	                getAvaliacaoInstitucionalVO().setNome(getAvaliacaoInstitucionalVO().getNome() + " - " + Uteis.getData(dataUltimoModulo));
//                }
//            }
            if(getAvaliacaoInstitucionalVO().getAvaliacaoUltimoModulo()) {
            	getAvaliacaoInstitucionalVO().setDataInicioAula(getDataInicio());
            	getAvaliacaoInstitucionalVO().setDataTerminoAula(getDataFim());
            }
            setQuestionarioVOs(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(getAvaliacaoInstitucionalVO(), avaliacaoInstitucionalAnaliticoRelVO.getUsuarioVO(), avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO(), avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getMatriculaPeriodoVOs().isEmpty() ? null :avaliacaoInstitucionalAnaliticoRelVO.getMatriculaVO().getMatriculaPeriodoVOs().get(0), true));
            setMensagemDetalhada("", "");
            setMensagemID("");
            setMensagem("");
        } catch (Exception e) {            
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public Boolean getEnviarEmail() {
		if (enviarEmail == null) {
			enviarEmail = false;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
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

	public AvaliacaoInstitucionalVO getAvaliacaoInstitucionalVO() {
		if (avaliacaoInstitucionalVO == null) {
			avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
		}
		return avaliacaoInstitucionalVO;
	}

	public void setAvaliacaoInstitucionalVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) {
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
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

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemAvaliacaoInstitucional() {
		if (listaSelectItemAvaliacaoInstitucional == null) {
			listaSelectItemAvaliacaoInstitucional = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAvaliacaoInstitucional;
	}

	public void setListaSelectItemAvaliacaoInstitucional(List<SelectItem> listaSelectItemAvaliacaoInstitucional) {
		this.listaSelectItemAvaliacaoInstitucional = listaSelectItemAvaliacaoInstitucional;
	}

	public List<AvaliacaoInstitucionalAnaliticoRelVO> getAvaliacaoInstitucionalAnaliticoRelVOs() {
		if (avaliacaoInstitucionalAnaliticoRelVOs == null) {
			avaliacaoInstitucionalAnaliticoRelVOs = new ArrayList<AvaliacaoInstitucionalAnaliticoRelVO>(0);
		}
		return avaliacaoInstitucionalAnaliticoRelVOs;
	}

	public void setAvaliacaoInstitucionalAnaliticoRelVOs(List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs) {
		this.avaliacaoInstitucionalAnaliticoRelVOs = avaliacaoInstitucionalAnaliticoRelVOs;
	}

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	/**
	 * @param campoConsultaCurso
	 *            the campoConsultaCurso to set
	 */
	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	/**
	 * @return the valorConsultaCurso
	 */
	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	/**
	 * @param valorConsultaCurso
	 *            the valorConsultaCurso to set
	 */
	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	/**
	 * @return the listaConsultaCurso
	 */
	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaVO() {
		if (personalizacaoMensagemAutomaticaVO == null) {
			personalizacaoMensagemAutomaticaVO = new PersonalizacaoMensagemAutomaticaVO();

		}
		return personalizacaoMensagemAutomaticaVO;
	}

	public void setPersonalizacaoMensagemAutomaticaVO(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO) {
		this.personalizacaoMensagemAutomaticaVO = personalizacaoMensagemAutomaticaVO;
	}

	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
			listaSelectItemLayout.add(new SelectItem("ANALITICO", "Analítico"));
			listaSelectItemLayout.add(new SelectItem("SINTETICO", "Sintético por Turma"));
			listaSelectItemLayout.add(new SelectItem("SINTCURSO", "Sintético por Curso"));
		}
		return listaSelectItemLayout;
	}

	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}

	public List<SelectItem> getListaSelectItemSituacaoResposta() {
		if (listaSelectItemSituacaoResposta == null) {
			listaSelectItemSituacaoResposta = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoResposta.add(new SelectItem("TODAS", "Todas"));
			listaSelectItemSituacaoResposta.add(new SelectItem("RESPONDIDO", "Respondidos"));
			listaSelectItemSituacaoResposta.add(new SelectItem("NAO_RESPONDIDO", "Não Respondidos"));
		}
		return listaSelectItemSituacaoResposta;
	}

	public void setListaSelectItemSituacaoResposta(List<SelectItem> listaSelectItemSituacaoResposta) {
		this.listaSelectItemSituacaoResposta = listaSelectItemSituacaoResposta;
	}

	public String getSituacaoResposta() {
		if (situacaoResposta == null) {
			situacaoResposta = "TODAS";
		}
		return situacaoResposta;
	}

	public void setSituacaoResposta(String situacaoResposta) {
		this.situacaoResposta = situacaoResposta;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "ANALITICO";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		if (listaSelectItemOrdenarPor == null) {
			listaSelectItemOrdenarPor = new ArrayList<SelectItem>(0);
			listaSelectItemOrdenarPor.add(new SelectItem("pessoa.nome", "Aluno"));
			listaSelectItemOrdenarPor.add(new SelectItem("curso.nome", "Curso"));
			listaSelectItemOrdenarPor.add(new SelectItem("identificadorturma", "Turma"));
			listaSelectItemOrdenarPor.add(new SelectItem("turno.nome, curso.nome", "Turno"));
		}
		return listaSelectItemOrdenarPor;
	}

	public void setListaSelectItemOrdenarPor(List<SelectItem> listaSelectItemOrdenarPor) {
		this.listaSelectItemOrdenarPor = listaSelectItemOrdenarPor;
	}

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "pessoa.nome";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public List<AvaliacaoInstitucionalPorTurmaSinteticoVO> getAvaliacaoInstitucionalPorTurmaSinteticoVOs() {
		if(avaliacaoInstitucionalPorTurmaSinteticoVOs == null){
			avaliacaoInstitucionalPorTurmaSinteticoVOs = new ArrayList<AvaliacaoInstitucionalPorTurmaSinteticoVO>(0);
		}
		return avaliacaoInstitucionalPorTurmaSinteticoVOs;
	}

	public void setAvaliacaoInstitucionalPorTurmaSinteticoVOs(List<AvaliacaoInstitucionalPorTurmaSinteticoVO> avaliacaoInstitucionalPorTurmaSinteticoVOs) {
		this.avaliacaoInstitucionalPorTurmaSinteticoVOs = avaliacaoInstitucionalPorTurmaSinteticoVOs;
	}
	
	public boolean getIsApresentarColunaRespondido() {
		return getSituacaoResposta().equals("RESPONDIDO") || getSituacaoResposta().equals("TODAS");
	}
	
	public boolean getIsApresentarColunaNaoRespondido() {
		return getSituacaoResposta().equals("NAO_RESPONDIDO") || getSituacaoResposta().equals("TODAS");
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<AvaliacaoInstitucionalPorSinteticoPorCursoVO> getAvaliacaoInstitucionalPorSinteticoPorCursoVOs() {
		if(avaliacaoInstitucionalPorSinteticoPorCursoVOs == null){
			avaliacaoInstitucionalPorSinteticoPorCursoVOs = new ArrayList<AvaliacaoInstitucionalPorSinteticoPorCursoVO>(0);
		}
		return avaliacaoInstitucionalPorSinteticoPorCursoVOs;
	}
	
	public void setAvaliacaoInstitucionalPorSinteticoPorCursoVOs(List<AvaliacaoInstitucionalPorSinteticoPorCursoVO> avaliacaoInstitucionalPorSinteticoPorCursoVOs) {
		this.avaliacaoInstitucionalPorSinteticoPorCursoVOs = avaliacaoInstitucionalPorSinteticoPorCursoVOs;
	}
	public Integer getQtdeTotal() {
		if(qtdeTotal == null){
			qtdeTotal = 0;
		}
		return qtdeTotal;
	}

	public void setQtdeTotal(Integer qtdeTotal) {
		this.qtdeTotal = qtdeTotal;
	}

	public Integer getQtdeTotalResponderam() {
		if(qtdeTotalResponderam == null){
			qtdeTotalResponderam = 0;
		}
		return qtdeTotalResponderam;
	}

	public void setQtdeTotalResponderam(Integer qtdeTotalResponderam) {
		this.qtdeTotalResponderam = qtdeTotalResponderam;
	}

	public Integer getQtdeTotalNaoResponderam() {
		if(qtdeTotalNaoResponderam == null){
			qtdeTotalNaoResponderam = 0;
		}
		return qtdeTotalNaoResponderam;
	}

	public void setQtdeTotalNaoResponderam(Integer qtdeTotalNaoResponderam) {
		this.qtdeTotalNaoResponderam = qtdeTotalNaoResponderam;
	}

	public Double getPercentualResponderam() {
		if(percentualResponderam == null){
			percentualResponderam = 0.0;
		}
		return percentualResponderam;
	}

	public void setPercentualResponderam(Double percentualResponderam) {
		this.percentualResponderam = percentualResponderam;
	}

	public Double getPercentualNaoResponderam() {
		if(percentualNaoResponderam == null){
			percentualNaoResponderam = 0.0;
		}
		return percentualNaoResponderam;
	}

	public void setPercentualNaoResponderam(Double percentualNaoResponderam) {
		this.percentualNaoResponderam = percentualNaoResponderam;
	}

	public List<QuestionarioVO> getQuestionarioVOs() {
		if(questionarioVOs == null){
			questionarioVOs = new ArrayList<QuestionarioVO>(0);
		}
		return questionarioVOs;
	}

	public void setQuestionarioVOs(List<QuestionarioVO> questionarioVOs) {
		this.questionarioVOs = questionarioVOs;
	}

	public void consultarAvaliacao() {
		try {
			getControleConsulta().getListaConsulta().clear();
			List<AvaliacaoInstitucionalVO> objs = new ArrayList<AvaliacaoInstitucionalVO>(0);
			if (getCampoConsultaAvaliacao().equals("nome")) {
				if (!getValorConsultaAvaliacao().equals("")) {
					objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorNome(getValorConsultaAvaliacao(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
				}
			}
			getControleConsulta().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaAvaliacao() {
		if (campoConsultaAvaliacao == null) {
			campoConsultaAvaliacao = "";
		}
		return campoConsultaAvaliacao;
	}

	public void setCampoConsultaAvaliacao(String campoConsultaAvaliacao) {
		this.campoConsultaAvaliacao = campoConsultaAvaliacao;
	}

	public String getValorConsultaAvaliacao() {
		if (valorConsultaAvaliacao == null) {
			valorConsultaAvaliacao = "";
		}
		return valorConsultaAvaliacao;
	}

	public void setValorConsultaAvaliacao(String valorConsultaAvaliacao) {
		this.valorConsultaAvaliacao = valorConsultaAvaliacao;
	}
	
	public void selecionarAvaliacao() throws Exception {		
		setAvaliacaoInstitucionalVO((AvaliacaoInstitucionalVO) context().getExternalContext().getRequestMap().get("avaliacaoItens"));
		alterarAvaliacaoInstitucional();		
	}
	
	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "nome"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTurma;
	}

	public Boolean getUtilizarListagemRespondente() {
		if(utilizarListagemRespondente == null) {
			utilizarListagemRespondente = Boolean.FALSE;
		}
		return utilizarListagemRespondente;
	}

	public void setUtilizarListagemRespondente(Boolean utilizarListagemRespondente) {
		this.utilizarListagemRespondente = utilizarListagemRespondente;
	}	
	
}
