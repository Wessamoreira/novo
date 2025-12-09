package controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.crm.InteracaoWorkflowNivelAplicacaoControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaMidiaVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.administrativo.enumeradores.TipoGerarAgendaCampanhaEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecorrenciaCampanhaEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.MetaVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.crm.enumerador.PoliticaRedistribuicaoProspectAgendaEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoWorkflowEnum;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilNavegacao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("CampanhaControle")
@Scope("viewScope")
@Lazy
public class CampanhaControle extends SuperControle implements Serializable {

	private Boolean considerarSabado;
	private Boolean considerarFeriados;
	private Boolean revisitacaoCarteira;
	private Boolean abrirModalMensagemPeriodoCampanha;
	private CampanhaColaboradorVO campanhaColaboradorVO;
	private CampanhaVO campanhaVO;
	private CampanhaPublicoAlvoVO campanhaPublicoAlvoVO;
	private FuncionarioVO funcionarioVO;
	private CargoVO cargoVO;
	private CursoInteresseVO cursoInteresseVO;
	private CampanhaMidiaVO campanhaMidiaVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaFuncionarioCargo;
	private String valorConsultaFuncionarioCargo;
	private String campoConsultaCampanhaCaptacao;
	private String valorConsultaCampanhaCaptacao;
	private String campoConsultaCargo;
	private String valorConsultaCargo;
	private String campoConsultaCursoInteresse;
	private String valorConsultaCursoInteresse;
	private String campoConsultaCursoCampanhaPublicoAlvo;
	private String valorConsultaCursoCampanhaPublicoAlvo;
	private String campoConsultaRegistroEntrada;
	private String valorConsultaRegistroEntrada;
	private List listaConsultaFuncionarioCargo;
	private List listaConsultaCampanhaCaptacao;
	private List listaConsultaRegistroEntrada;
	private List listaConsultaCargo;
	private List listaConsultaCursoInteresse;
	private List listaConsultaCursoCampanhaPublicoAlvo;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemTipoMidia;
	private List listaSelectItemMeta;
	private List listaSelectItemWorkflow;
	private List listaSelectItemFormacaoAcademica;
	private List listaConsultaCurso;
	private List listaCampanhaColaboradorAlterarCompromisso;
	private List listaCompromissoGeradoConsultor;
	private Date dataIncialAlteracaoCompromisso;
	private Date dataFinalAlteracaoCompromisso;
	private Date dataNovoCompromisso;
	private String horaNovoCompromisso;
	private String tipoAlteracaoColaborador;
	private CampanhaColaboradorCursoVO campanhaColaboradorCursoVO;

	private List<SelectItem> listaProcSeletivo;
	private List<SelectItem> listaTipoRecorrencia;
	private List<SelectItem> listaTipoCampanha;
	private String urlSegmentacao;
	private List<SegmentacaoProspectVO> listaSegmentacoes;
    private List<SelectItem> listaPoliticaGerarAgenda;
    private ProgressBarVO progressBar;
    private ProgressBarVO progressBarPublicoEspecifico;
    private ProgressBarVO progressBarRegerarAgenda;
    private ProgressBarVO progressBarAdicionarProspect;
    private FuncionarioVO consultorPublicoAlvoSelecionadoVO;
    private Boolean bloquearCamposParaVisualizacao;
    private ProspectsVO prospectVisualizacaoDadosGeraisVO;
    private Boolean marcarTodasSituacoesAcademicas;
    private List<FuncionarioVO> listaConsultorPublicoAlvoVOs;
    private List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVisualizarDadosVOs;
    private Integer quantidadeCompromissoIniciouAgendaCampanha;
    private Boolean apresentarBotaoRegerarAgenda;
    private Boolean redistribuirAgendaPublicoAlvoEspecifico;
    private Boolean publicoAlvoEspecifico;
    private String autocompleteValorCurso;
    private List<PessoaVO> listaPessoaDuplicadaVOs;
    private Boolean alterarConsultorPadraoProspect;
    private Boolean alterarConsultorCompromissoProspectJaIniciado;

	public CampanhaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setSituacao("AC");
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		setCampanhaVO(new CampanhaVO());
		setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("campanhaForm.xhtml");
	}

	public String editar() {
		try{
		CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
		setCampanhaVO(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getFacadeFactory().getCampanhaFacade().realizarCriacaoCampanhaPublicoAlvoProspectAdicionadoDinamicamente(getCampanhaVO(), getUsuarioLogado());
		inicializarDadosConsultorCampanhaPublicoAlvo(getCampanhaVO());
		inicializarListasSelectItemTodosComboBox();
		getCampanhaVO().setPossuiAgenda(getFacadeFactory().getCampanhaFacade().consultarExistenciaCampanhaPossuiAgenda(getCampanhaVO().getCodigo(), getUsuarioLogado()));
		setApresentarBotaoRegerarAgenda(false);		
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("campanhaForm.xhtml");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("campanhaCons.xhtml");
		}
	}
	
	public void inicializarDadosConsultorCampanhaPublicoAlvo(CampanhaVO campanhaVO) throws Exception {
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
			getFacadeFactory().getCampanhaFacade().inicializarDadosMediaProspectPorColaborador(campanhaVO, campanhaPublicoAlvoVO, getUsuarioLogado());
			getFacadeFactory().getCampanhaFacade().realizarMontagemListaConsultorProspect(campanhaVO, campanhaPublicoAlvoVO, getUsuarioLogado());
			Ordenacao.ordenarLista(campanhaPublicoAlvoVO.getListaCampanhaConsultorProspectVOs(), "nomeConsultor") ;
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Parceiro</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getCampanhaVO().isNovoObj().booleanValue()) {
				if (getCampanhaVO().getRecorrente()) {
					getCampanhaVO().setDataRecorrencia(new Date());
				}
				getFacadeFactory().getCampanhaFacade().incluir(getCampanhaVO(), getCampanhaVO().getListaCampanhaColaborador(), getCampanhaVO().getListaCampanhaMidia(), getCampanhaVO().getListaCampanhaPublicoAlvo(), getUsuarioLogado());
			} else {
				if (getCampanhaVO().getSituacao().equals("AT")) {
					getFacadeFactory().getCampanhaColaboradorFacade().validarDadosAlterarCampanhaColaborador(getCampanhaVO());
					getFacadeFactory().getCampanhaColaboradorFacade().alterarCampanhaColaborador(getCampanhaVO().getCodigo(), getCampanhaVO().getListaCampanhaColaborador());
					getFacadeFactory().getCampanhaPublicoAlvoFacade().alterarCampanhaPublicoAlvo(getCampanhaVO().getCodigo(), getCampanhaVO().getListaCampanhaPublicoAlvo(), getUsuarioLogado());
				} else {
					getFacadeFactory().getCampanhaFacade().alterar(getCampanhaVO(), getCampanhaVO().getListaCampanhaColaborador(), getCampanhaVO().getListaCampanhaMidia(), getCampanhaVO().getListaCampanhaPublicoAlvo(), getUsuarioLogado());
				}
			}
			setMensagemID("msg_dados_gravados");
			getListaConsulta().clear();
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void ativar() {
		try {
			setOncompleteModal("");
			getCampanhaVO().setSituacao("AT");
			getFacadeFactory().getCampanhaFacade().gravarSituacao(getCampanhaVO(), getUsuarioLogado());
			setMensagemID("msg_ativar_dados");
			if(getApresentarBotaoGerarAgenda() && !getCampanhaVO().getPossuiAgenda() && !getCampanhaVO().getCampanhaSemPeriodo() && !getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA)){				
				setOncompleteModal("RichFaces.$('panelGerarAgenda').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}

	public String finalizarCampanha() {
		try {
			getFacadeFactory().getCampanhaFacade().finalizarCampanha(getCampanhaVO(), getUsuarioLogado());
			if(getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.LIGACAO_RECEPTIVA) || getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA)){
				InteracaoWorkflowNivelAplicacaoControle interacaoWorkflowNivelAplicacaoControle = (InteracaoWorkflowNivelAplicacaoControle) UtilNavegacao.getControlador("InteracaoWorkflowNivelAplicacaoControle");
				if(interacaoWorkflowNivelAplicacaoControle != null){
					interacaoWorkflowNivelAplicacaoControle.getMapaInteracaoNovoProspectVOs().remove(getCampanhaVO().getTipoCampanha().name() + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().toString());
				}
			}			
			setMensagemID("msg_cancelar_dados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String cancelar() {
		try {
			getFacadeFactory().getCampanhaFacade().cancelarCampanha(getCampanhaVO(), getUsuarioLogado());
			setMensagemID("msg_cancelar_dados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void clonar() throws Exception {
		getCampanhaVO().setCodigo(0);
		getCampanhaVO().setDescricao(getCampanhaVO().getDescricao() + " - Clone");
		getCampanhaVO().setPossuiAgenda(false);
		getCampanhaVO().setSituacao("EC");
		getCampanhaVO().setNovoObj(Boolean.TRUE);
        // regerando todo o publico alvo da campanha, pois ao ser clonada, outros
        // prospects podem passar a existir ou podem ser deixar de estar no escopo
        // da campanha
        adicionarCampanhaPublicoAlvoCampanhaClonada();
		setMensagemID("msg_dados_clonados");
	}

	public void limparTelaCampanhaColaborador() {
		setFuncionarioVO(new FuncionarioVO());
		setCargoVO(new CargoVO());
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CampanhaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			setListaConsulta(getFacadeFactory().getCampanhaFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsulta().getSituacao(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("campanhaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("campanhaCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ParceiroVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getCampanhaFacade().excluir(getCampanhaVO(), getUsuarioLogado());
			if(getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.LIGACAO_RECEPTIVA) || getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA)){
				InteracaoWorkflowNivelAplicacaoControle interacaoWorkflowNivelAplicacaoControle = (InteracaoWorkflowNivelAplicacaoControle) UtilNavegacao.getControlador("InteracaoWorkflowNivelAplicacaoControle");
				if(interacaoWorkflowNivelAplicacaoControle != null){
					interacaoWorkflowNivelAplicacaoControle.getMapaInteracaoNovoProspectVOs().remove(getCampanhaVO().getTipoCampanha().name() + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().toString());
				}
			}			
			setCampanhaVO(new CampanhaVO());
			setMensagemID("msg_dados_excluidos");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public boolean getCampanhaDeCobranca() {
		if (this.getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)) {
			return true;
		}
		return false;
	}

	public void prepararParametrosRevisitacaoCarteiraTipoCampanha() {
		try {
			setBloquearCamposParaVisualizacao(false);
			if (this.getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)) {
				this.getCampanhaPublicoAlvoVO().setTipoPublicoAlvo("AL");
			} else {
				this.getCampanhaPublicoAlvoVO().setTipoPublicoAlvo("PR");
				this.getCampanhaPublicoAlvoVO().setSegmentacao(Boolean.FALSE);
			}
		} catch (Exception e) {
		}
	}

	public void gerarAgenda() {
		try {
			getFacadeFactory().getCampanhaFacade().validarCampanhaGerarAgenda(getCampanhaVO());
			Date maiorDataCompromisso = getFacadeFactory().getCampanhaFacade().gerarAgendaDistribuindoProspectsPorConsultor(getCampanhaVO(), getProgressBar(), getUsuarioLogado());
			// Date maiorDataCompromisso =
			// getFacadeFactory().getCampanhaFacade().gerarAgenda(getCampanhaVO(),
			// getUsuarioLogado(), getConsiderarSabado(),
			// getConsiderarFeriados());
			getCampanhaVO().setPossuiAgenda(Boolean.TRUE);
			getFacadeFactory().getCampanhaFacade().gravarAgenda(getCampanhaVO());
			if (maiorDataCompromisso.after(getCampanhaVO().getPeriodoFim())) {
				setAbrirModalMensagemPeriodoCampanha(Boolean.TRUE);
			}
			setMensagemID("msg_agendaGerada");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void inicializarDadosMensagem() {
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public List getListaSelectItemTipoCampanha() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoCampanha = new Hashtable();
		Enumeration keys = tipoCampanha.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoCampanha.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemTipoPublicoAlvo() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("PR", "Prospect"));
		objs.add(new SelectItem("AL", "Aluno"));
		objs.add(new SelectItem("CD", "Candidato"));
		objs.add(new SelectItem("RF", "Responsável Financeiro"));
		return objs;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("unidadeensino", "Unidade Ensino"));
		itens.add(new SelectItem("curso", "Curso"));
		return itens;
	}

	public List getTipoSituacaoCampanhaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("AC", "Ativas e em construção"));
		itens.add(new SelectItem("AT", "Ativas"));
		itens.add(new SelectItem("TO", "Todas"));
		itens.add(new SelectItem("FI", "Finalizadas"));
		itens.add(new SelectItem("CA", "Canceladas"));
		itens.add(new SelectItem("EC", "Em Construção"));
		return itens;
	}

	public List getTipoConsultaFuncionarioCargo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeFuncionario", "Nome Funcionário"));
		itens.add(new SelectItem("nomeCargo", "Nome Cargo"));
		return itens;
	}

	public List getTipoConsultaComboPorNome() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaComboPorDescricao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List getTipoConsultaComboSituacao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("AT", "Ativada"));
		itens.add(new SelectItem("CA", "Cancelada"));
		itens.add(new SelectItem("EC", "Em construção"));
		return itens;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), getCampanhaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getCampanhaVO().setCurso(obj);
			getFacadeFactory().getCampanhaColaboradorFacade().realizarExclusaoPorCursoNaCampanha(getCampanhaVO());
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarRegistroEntrada() {
		try {
			List<RegistroEntradaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaRegistroEntrada().equals("descricao")) {
				objs = getFacadeFactory().getRegistroEntradaFacade().consultarPorDescricao(getValorConsultaRegistroEntrada(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getCampanhaVO().getUnidadeEnsino());
			}
			objs.stream().forEach(p->p.setListaSelectItemFuncionarioCargo(getCampanhaVO().getListaSelectItemFuncionarioCargo()));
			setListaConsultaRegistroEntrada(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarRegistroEntrada() {
		try {
			getCampanhaPublicoAlvoVO().setRegistroEntrada((RegistroEntradaVO) context().getExternalContext().getRequestMap().get("registroEntradaItens"));
			listaConsultaRegistroEntrada.clear();
			this.setValorConsultaRegistroEntrada("");
			this.setCampoConsultaRegistroEntrada("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarCursoCampanhaPublicoAlvo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCursoCampanhaPublicoAlvo().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCursoCampanhaPublicoAlvo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCursoCampanhaPublicoAlvo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCursoCampanhaPublicoAlvo() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getCampanhaPublicoAlvoVO().setCurso(obj);
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarCargo() {
		try {
			List objs = new ArrayList(0);
			if (getCampanhaVO().getUnidadeEnsino() == null || getCampanhaVO().getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_campanhaMarketing_unidadeEnsino"));
			}
			if (getCampanhaVO().getMeta().getCodigo() == null || getCampanhaVO().getMeta().getCodigo() == 0) {
				throw new Exception("O campo META (aba Dados Básicos) deve ser informado.");
			}
			if (getCampoConsultaCargo().equals("nome")) {
				objs = getFacadeFactory().getCargoFacade().consultarPorNome(getValorConsultaCargo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCargo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public void selecionarCargo() {
	// CargoVO obj = (CargoVO)
	// context().getExternalContext().getRequestMap().get("cargo");
	// try {
	// setCargoVO(obj);
	// listaConsultaFuncionarioCargo.clear();
	// this.setValorConsultaFuncionarioCargo("");
	// this.setCampoConsultaFuncionarioCargo("");
	// setMensagemID("", "");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	//
	// }
	public void consultarFuncionarioCargo() {
		try {
			List objs = new ArrayList(0);
			if (getCampanhaVO().getUnidadeEnsino() == null || getCampanhaVO().getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_campanhaMarketing_unidadeEnsino"));
			}
			if (getCampanhaVO().getMeta().getCodigo() == null || getCampanhaVO().getMeta().getCodigo() == 0) {
				throw new Exception("O campo META (aba Dados Básicos) deve ser informado.");
			}
			Boolean consultor = realizarVerificacaoConsultaDeveraTrazerApenasFuncionarioConsultorFuncionario();
			if (getCampoConsultaFuncionarioCargo().equals("nomeFuncionario")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioUnidadeEnsinoSituacao(getValorConsultaFuncionarioCargo(), getCampanhaVO().getUnidadeEnsino().getCodigo(), true, consultor, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioCargo().equals("nomeCargo")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnidadeEnsinoSituacao(getValorConsultaFuncionarioCargo(), getCampanhaVO().getUnidadeEnsino().getCodigo(), true, consultor, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionarioCargo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean realizarVerificacaoConsultaDeveraTrazerApenasFuncionarioConsultorFuncionario() {
		if (!getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA) && !getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA)) {
			return true;
		}
		return false;
	}

	// public void selecionarFuncionarioCargo() {
	// FuncionarioCargoVO obj = (FuncionarioCargoVO)
	// context().getExternalContext().getRequestMap().get("funcionarioCargo");
	// try {
	// getCampanhaColaboradorVO().setFuncionarioCargoVO(obj);
	// setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionarioVO().getCodigo(),
	// getCampanhaVO().getUnidadeEnsino().getCodigo(), true,
	// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
	// listaConsultaFuncionarioCargo.clear();
	// this.setValorConsultaFuncionarioCargo("");
	// this.setCampoConsultaFuncionarioCargo("");
	// setMensagemID("", "");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	//
	// }
	public void consultarCampanhaCaptacao() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCampanhaCaptacao().equals("descricao")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorDescricao(getValorConsultaCampanhaCaptacao(), "", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCampanhaCaptacao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCampanhaCaptacao() {
		CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
		try {
			getCampanhaPublicoAlvoVO().setCampanhaCaptacao(obj);
			listaConsultaCampanhaCaptacao.clear();
			this.setValorConsultaCampanhaCaptacao("");
			this.setCampoConsultaCampanhaCaptacao("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarCursoInteresse() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCursoInteresse().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCursoInteresse(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCursoInteresse(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCursoInteresse() {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoInteresseItens");
		try {
			getCampanhaPublicoAlvoVO().getCursoInteresse().setCurso(obj);
			listaConsultaCursoInteresse.clear();
			this.setValorConsultaCursoInteresse("");
			this.setCampoConsultaCursoInteresse("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarPreenchimnentoAlteracaoCompromisso() {
		try {
			CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("campanhaColaboradorItens");
			setCampanhaColaboradorVO(obj);
			setDataIncialAlteracaoCompromisso(getCampanhaVO().getPeriodoInicio());
			setDataFinalAlteracaoCompromisso(getCampanhaVO().getPeriodoFim());
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarQuantidadeCompromissoPorColaborador(getCampanhaColaboradorVO(), getDataIncialAlteracaoCompromisso(), getDataFinalAlteracaoCompromisso(), false, getUsuarioLogado());
			setTipoAlteracaoColaborador("");
			setCampoConsultaCargo("");
			setCampoConsultaFuncionarioCargo("");
			setValorConsultaCargo("");
			setValorConsultaFuncionarioCargo("");
			getListaConsultaCargo().clear();
			getListaConsultaFuncionarioCargo().clear();
			getListaCampanhaColaboradorAlterarCompromisso().clear();
			setHoraNovoCompromisso("");
			setDataNovoCompromisso(new Date());
			setMensagemID("", "");
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarAgendaGeradaConsultor() {
		try {
			CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("campanhaColaboradorItens");
			setCampanhaColaboradorVO(obj);
			setDataIncialAlteracaoCompromisso(getCampanhaVO().getPeriodoInicio());
			setDataFinalAlteracaoCompromisso(getCampanhaVO().getPeriodoFim());
			// setListaCompromissoGeradoConsultor(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarQuantidadeCompromissoPorColaborador(getCampanhaColaboradorVO(),
			// getDataIncialAlteracaoCompromisso(),
			// getDataFinalAlteracaoCompromisso(), false, getUsuarioLogado()));
			setMensagemID("", "");
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarAtualizacaoQtdCompromissoNoPeriodo() {
		try {
			if (getDataIncialAlteracaoCompromisso().before(getCampanhaVO().getPeriodoInicio())) {
				throw new Exception("O campo Período não pode ser menor que a data inicial da campanha (" + Uteis.getData(getCampanhaVO().getPeriodoInicio()) + ") .");
			}
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarQuantidadeCompromissoPorColaborador(getCampanhaColaboradorVO(), getDataIncialAlteracaoCompromisso(), getDataFinalAlteracaoCompromisso(), false, getUsuarioLogado());
			setMensagemDetalhada("", "");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarFuncionarioCargoAlteracaoCompromisso() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItens");
			if (obj.getFuncionarioVO().getPessoa().getCodigo().equals(getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo())) {
				throw new Exception("Não é Possível Adicionar o Mesmo Consultor.");
			}
			CampanhaColaboradorVO novoColaboradorVO = new CampanhaColaboradorVO(this.getCampanhaVO());
			novoColaboradorVO.setFuncionarioCargoVO(obj);
			setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionarioVO().getCodigo(), getCampanhaVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			List<FuncionarioCargoVO> listaFuncionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (!listaFuncionarioCargo.isEmpty()) {				
				Iterator i = listaFuncionarioCargo.iterator();
				while (i.hasNext()) {
					FuncionarioCargoVO fun = (FuncionarioCargoVO)i.next();
					if ((fun.getUnidade().getCodigo().intValue() == getCampanhaVO().getUnidadeEnsino().getCodigo().intValue())
							&& fun.getConsultor().booleanValue()) {
						novoColaboradorVO.setFuncionarioCargoVO(fun);
					}
				}				
			}
			getFacadeFactory().getCampanhaColaboradorFacade().adicionarObjCampanhaColaboradorAlterarCompromisso(getListaCampanhaColaboradorAlterarCompromisso(), getCampanhaVO(), novoColaboradorVO);
			novoColaboradorVO = null;
			setFuncionarioVO(null);
			setCargoVO(null);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void definirSegmentacao() {
		if (getCampanhaPublicoAlvoVO().getSegmentacao().booleanValue()) {
			consultarSegmentacaoProspect();
			setUrlSegmentacao("RichFaces.$('panelSegmentacao').show()");
		} else {
			setUrlSegmentacao("");
		}
	}
	
	public List<CursoVO> autocompleteCampanhaColaboradorCurso(Object suggest) {
		try {
			return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, getCampanhaVO().getUnidadeEnsino().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<CursoVO>();
		}
	}

	public void realizarSelecaoCampanhaColaboradorParaAdicaoCurso() {
		try {
			CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("campanhaColaboradorItens");
			setCampanhaColaboradorVO(obj);
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarLimpezaCampanhaColaborador() {
		try {
			setCampanhaColaboradorVO(new CampanhaColaboradorVO(this.getCampanhaVO()));
			setMensagem("");
			setMensagemID("", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarCampanhaColaboradorCurso() {
		try {
			if (getCampanhaColaboradorCursoVO().getCursoVO().getCodigo().intValue() != 0) {
				getFacadeFactory().getCampanhaColaboradorFacade().adicionarObjCampanhaColaboradorCursoVOs(getCampanhaColaboradorVO(), getCampanhaColaboradorCursoVO());
			} else {
				throw new Exception("Curso não encontrado.");
			}
			this.setCampanhaColaboradorCursoVO(new CampanhaColaboradorCursoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarTodosCursosCampanha() {
		getFacadeFactory().getCampanhaColaboradorFacade().adicionarTodosCursosCampanhaColaborador(getCampanhaColaboradorVO());
		setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
	}

	public void removerTodosCursosCampanha() {
		getCampanhaColaboradorVO().getListaCampanhaColaboradorCursoVOs().clear();
	}

	public void removerCampanhaColaboradorCurso() {
		try {
			CampanhaColaboradorCursoVO obj = (CampanhaColaboradorCursoVO) context().getExternalContext().getRequestMap().get("campanhaColaboradorCursoItens");
			getFacadeFactory().getCampanhaColaboradorFacade().excluirObjCampanhaColaboradorCursoVOs(getCampanhaColaboradorVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public void adicionarCargoAlteracaoCompromisso() {
		try {
			CargoVO cargo = (CargoVO) context().getExternalContext().getRequestMap().get("cargoItens");
			setCargoVO(cargo);
//			List<FuncionarioCargoVO> listaFuncionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnico(getCargoVO().getNome(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			List<FuncionarioCargoVO> listaFuncionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnicoUnidadeEnsino(getCargoVO().getNome(), getCampanhaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			Iterator i = listaFuncionarioCargo.iterator();
			while (i.hasNext()) {
				CampanhaColaboradorVO obj = new CampanhaColaboradorVO(this.getCampanhaVO());
				obj.setFuncionarioCargoVO((FuncionarioCargoVO) i.next());
				getFacadeFactory().getCampanhaColaboradorFacade().adicionarObjCampanhaColaboradorAlterarCompromisso(getListaCampanhaColaboradorAlterarCompromisso(), getCampanhaVO(), obj);
			}
			setFuncionarioVO(null);
			setCargoVO(null);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluirFuncionarioCargoAlteracaoCompromisso() {
		try {
			CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("campanhaColaboradorItens");
			getFacadeFactory().getCampanhaColaboradorFacade().excluirObjCampanhaColaborador(getListaCampanhaColaboradorAlterarCompromisso(), getCampanhaVO(), obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistirAlteracaoCompromisso() {
		try {
			Date maiorDataCompromisso = getFacadeFactory().getCampanhaFacade().persistirAlteracaoCompromissoPorCampanha(getCampanhaVO(), getCampanhaColaboradorVO(), getListaCampanhaColaboradorAlterarCompromisso(), getDataIncialAlteracaoCompromisso(), getDataFinalAlteracaoCompromisso(), getTipoAlteracaoColaborador(), getDataNovoCompromisso(), getHoraNovoCompromisso(), getConsiderarSabado(), getConsiderarFeriados(), getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(maiorDataCompromisso) && maiorDataCompromisso.after(getCampanhaVO().getPeriodoFim())) {
				setAbrirModalMensagemPeriodoCampanha(Boolean.TRUE);
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("campanhaCons.xhtml");
	}

	public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
		List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
		return objs;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemWorkflow(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarWorkflowPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				WorkflowVO obj = (WorkflowVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemWorkflow(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemTipoMidia(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTipoMidiaPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNomeMidia()));
			}
			setListaSelectItemTipoMidia(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemMeta(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarMetaPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				MetaVO obj = (MetaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
			setListaSelectItemMeta(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List consultarWorkflowPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getWorkflowFacade().consultarPorNomePorSituacao(nomePrm, TipoSituacaoWorkflowEnum.ATIVO, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List consultarTipoMidiaPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List consultarMetaPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getMetaFacade().consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemWorkflow() {
		try {
			montarListaSelectItemWorkflow("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemTipoMidia() {
		try {
			montarListaSelectItemTipoMidia("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemMeta() {
		try {
			montarListaSelectItemMeta("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemMeta();
		montarListaSelectItemWorkflow();
		montarListaSelectItemTipoMidia();
		montarListaSelectItemTipoRecorrencia();
		montarListaSelectItemTipoCampanha();
                montarListaSelectItemPoliticaGerarAgenda();
	}
        
	public void montarListaSelectItemTipoCampanha() {
		try {
			Boolean visualizarCobranca = verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca();
			Boolean visualizarVendas = verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas();
			if (visualizarCobranca && visualizarVendas) {
				for (TipoCampanhaEnum obj : TipoCampanhaEnum.values()) {
					getListaTipoCampanha().add(new SelectItem(obj.toString(),obj.getDescricao()));
				}
			} else if (visualizarCobranca) {
				for (TipoCampanhaEnum obj : TipoCampanhaEnum.values()) {
					if(obj.equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)){
						getListaTipoCampanha().add(new SelectItem(obj.toString(),obj.getDescricao()));
					}
				}
			} else if (visualizarVendas) {
				for (TipoCampanhaEnum obj : TipoCampanhaEnum.values()) {
					if(!obj.equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)){
						getListaTipoCampanha().add(new SelectItem(obj.toString(),obj.getDescricao()));
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaCobranca", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public Boolean verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaVendas", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public void realizarSelecaoMeta() {
		try {
			if (getCampanhaVO().getMeta().getCodigo() != 0) {
				getCampanhaVO().setMeta(getFacadeFactory().getMetaFacade().consultarPorChavePrimaria(getCampanhaVO().getMeta().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			getCampanhaVO().getListaCampanhaColaborador().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarSelecaoWorkflow() {
		try {
			if (getCampanhaVO().getWorkflow().getCodigo() != 0) {
				getCampanhaVO().setWorkflow(getFacadeFactory().getWorkflowFacade().consultarPorChavePrimaria(getCampanhaVO().getWorkflow().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public CampanhaVO getCampanhaVO() {
		if (campanhaVO == null) {
			campanhaVO = new CampanhaVO();
		}
		return campanhaVO;
	}

	public void setCampanhaVO(CampanhaVO campanhaVO) {
		this.campanhaVO = campanhaVO;
	}

	public boolean getClonar() {
		if (getCampanhaVO().isNovoObj().booleanValue()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean getAtivar() {
		if (getCampanhaVO().isNovoObj().booleanValue()) {
			return false;
		} else if (getCampanhaVO().getSituacao().equalsIgnoreCase("EC")) {
			return true;
		}
		return false;
	}

	public boolean getApresentarBotaoGerarAgenda() {
		if ((!getCampanhaVO().getPossuiAgenda()) && (getCampanhaVO().getSituacao().equals("AT") && getCampanhaVO().getTipoCampanha() != TipoCampanhaEnum.LIGACAO_RECEPTIVA)) {
			return true;
		}
		return false;
	}

	public boolean getPodeSerFinalizada() {
		return getCampanhaVO().getSituacao().equals("AT");
	}

	public boolean getPodeSerExcluida() {
		if ((!getCampanhaVO().getCodigo().equals(0)) && (!getCampanhaVO().getPossuiAgenda())) {
			return true;
		}
		return false;
	}

	public boolean getCancelar() {
		return getCampanhaVO().getSituacao().equals("AT") || getCampanhaVO().getSituacao().equals("FI");
	}

	public boolean getPossibilidadeGravar() {
		if (getCampanhaVO().getSituacao().equalsIgnoreCase("EC") || getCampanhaVO().getSituacao().equalsIgnoreCase("AT")) {
			return true;
		} else if (getCampanhaVO().isNovoObj().booleanValue()) {
			return true;
		}
		return false;

	}

	public boolean getPesquisaPorSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

	public boolean getPesquisarNaoPorSituacao() {
		if (getPesquisaPorSituacao()) {
			return false;
		}
		return true;
	}

	public CampanhaColaboradorVO getCampanhaColaboradorVO() {
		if (campanhaColaboradorVO == null) {
			campanhaColaboradorVO = new CampanhaColaboradorVO(this.getCampanhaVO());
		}
		return campanhaColaboradorVO;
	}

	public void setCampanhaColaboradorVO(CampanhaColaboradorVO campanhaColaboradorVO) {
		this.campanhaColaboradorVO = campanhaColaboradorVO;
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
			listaConsultaCurso = new ArrayList();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	// CampanhaColaborador
	public List getListaSelectItemTipoCampanhaColaborador() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoCampanhaColaborador = new Hashtable();
		Enumeration keys = tipoCampanhaColaborador.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoCampanhaColaborador.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemDistribuicaoEntreColaborador() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("SelecionarParticipante", "Selecionar Novo(s) Participante(s)"));
		objs.add(new SelectItem("DistribuirParticipantes", "Distribuir Entres os Participantes"));
		return objs;
	}

	public Boolean getSelecionarParticipantes() {
		if (getTipoAlteracaoColaborador().equals("SelecionarParticipante")) {
			return true;
		}
		return false;
	}

	public Boolean getDistribuirParticipantes() {
		if (getTipoAlteracaoColaborador().equals("DistribuirParticipantes")) {
			return true;
		}
		return false;
	}

	public String getTipoAlteracaoColaborador() {
		if (tipoAlteracaoColaborador == null) {
			tipoAlteracaoColaborador = "";
		}
		return tipoAlteracaoColaborador;
	}

	public void setTipoAlteracaoColaborador(String tipoAlteracaoColaborador) {
		this.tipoAlteracaoColaborador = tipoAlteracaoColaborador;
	}

	public void adicionarCampanhaPublicoAlvoRevisitacaoCarteira() {
		setRevisitacaoCarteira(Boolean.TRUE);
		adicionarCampanhaPublicoAlvo();
	}

	public String controleAbrirFecharModal;

	public String getControleAbrirFecharModal() {
		if (controleAbrirFecharModal == null) {
			controleAbrirFecharModal = "";
		}
		return controleAbrirFecharModal;
	}

	public void setControleAbrirFecharModal(String controleAbrirFecharModal) {
		this.controleAbrirFecharModal = controleAbrirFecharModal;
	}

	public void adicionarCampanhaPublicoAlvoRegristroEntrada() {
		getCampanhaPublicoAlvoVO().setRegistroEntrada((RegistroEntradaVO) context().getExternalContext().getRequestMap().get("registroEntradaItens"));
		getListaConsultaRegistroEntrada().clear();
		this.setValorConsultaRegistroEntrada("");
		this.setCampoConsultaRegistroEntrada("");
		setRevisitacaoCarteira(Boolean.FALSE);
		adicionarCampanhaPublicoAlvo();
	}

	public void adicionarCampanhaPublicoAlvo() {
		try {
			getCampanhaVO().setRealizandoRedistribuicaoProspectAgenda(false);
			setControleAbrirFecharModal("");
			validarSituacaoMatriculaPublicoAlvoAluno();
			getFacadeFactory().getCampanhaPublicoAlvoFacade().adicionarObjCampanhaPublicoAlvoVOs(getCampanhaVO(), getCampanhaPublicoAlvoVO(), getRevisitacaoCarteira(), getProgressBarAdicionarProspect(), getUsuarioLogado(), getListaSegmentacoes());
			getProgressBarAdicionarProspect().setStatus("Carregando Lista dos Prospect nos Consultores");
			getFacadeFactory().getCampanhaFacade().realizarMontagemListaConsultorProspect(getCampanhaVO(), getCampanhaPublicoAlvoVO(), getUsuarioLogado());
			setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
			setControleAbrirFecharModal("RichFaces.$('panelRevisitacaoCarteira').hide()");			
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);			
		} catch (Exception e) {			
			getProgressBarAdicionarProspect().setForcarEncerramento(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}
	
	public void validarSituacaoMatriculaPublicoAlvoAluno() throws Exception{
		try {
			if(getCampanhaPublicoAlvoVO().getTipoPublicoAlvo().equals("AL")){
				if(!getCampanhaPublicoAlvoVO().getFormandos() && !getCampanhaPublicoAlvoVO().getPossiveisFormandos() && !getCampanhaPublicoAlvoVO().getCursando()
						&& !getCampanhaPublicoAlvoVO().getPreMatriculados() && !getCampanhaPublicoAlvoVO().getTrancados() && !getCampanhaPublicoAlvoVO().getCancelado()
						&& !getCampanhaPublicoAlvoVO().getInadimplentes()
						&& !getCampanhaPublicoAlvoVO().getAbandonado()
						&& !getCampanhaPublicoAlvoVO().getTransferenciaInterna()
						&& !getCampanhaPublicoAlvoVO().getTransferenciaExterna()
						&& !getCampanhaPublicoAlvoVO().getPreMatriculaCancelada()
						){
					throw new Exception(UteisJSF.internacionalizar("msg_CampanhaPublicoAlvo_erroSituacaoMatricula"));
				}
			}
                        if (getCampanhaPublicoAlvoVO().getTipoPublicoAlvo().equals("CD")) {
                        	if ((getCampanhaPublicoAlvoVO().getProcessoSeletivoVO().getCodigo().equals(0))) {
            					throw new Exception(UteisJSF.internacionalizar("Deve ser informando o Processo Seletivo para qual deseja adicionar candidatos"));
            				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void alterarCampanhaPublicoAlvo() throws Exception {
		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO) context().getExternalContext().getRequestMap().get("publicoAlvoItens");
		if (getCampanhaVO().getCodigo() != 0 && obj.getCodigo() != 0) {
			setCampanhaPublicoAlvoVO(getFacadeFactory().getCampanhaPublicoAlvoFacade().montarCampanhaPublicoAlvo(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} else {
			setCampanhaPublicoAlvoVO(obj);
		}
	}

	public void excluirCampanhaPublicoAlvo() {
		try {
			CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO) context().getExternalContext().getRequestMap().get("publicoAlvo");
			getFacadeFactory().getCampanhaPublicoAlvoFacade().excluirObjCampanhaPublicoAlvoVOs(getCampanhaVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public void adicionarCargo() {
		try {
			CargoVO cargo = (CargoVO) context().getExternalContext().getRequestMap().get("cargoItens");
			setCargoVO(cargo);
			List<FuncionarioCargoVO> listaFuncionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnicoUnidadeEnsino(getCargoVO().getNome(), getCampanhaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			Iterator i = listaFuncionarioCargo.iterator();
			while (i.hasNext()) {
				CampanhaColaboradorVO obj = new CampanhaColaboradorVO(this.getCampanhaVO());
				obj.setFuncionarioCargoVO((FuncionarioCargoVO) i.next());
				getFacadeFactory().getCampanhaColaboradorFacade().adicionarObjCampanhaColaborador(getCampanhaVO().getListaCampanhaColaborador(), getCampanhaVO(), obj);
			}
			setCampanhaColaboradorVO(new CampanhaColaboradorVO(this.getCampanhaVO()));
			setFuncionarioVO(null);
			setCargoVO(null);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarFuncionarioCargo() {
		try {
			setCampanhaColaboradorVO(new CampanhaColaboradorVO(this.getCampanhaVO()));
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItens");
			getCampanhaColaboradorVO().setFuncionarioCargoVO(obj);			
			List<FuncionarioCargoVO> listaFuncionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(obj.getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (!listaFuncionarioCargo.isEmpty()) {				
				Iterator i = listaFuncionarioCargo.iterator();
				while (i.hasNext()) {
					FuncionarioCargoVO fun = (FuncionarioCargoVO)i.next();
					if ((fun.getUnidade().getCodigo().intValue() == getCampanhaVO().getUnidadeEnsino().getCodigo().intValue())
							&& fun.getConsultor().booleanValue()) {
						getCampanhaColaboradorVO().setFuncionarioCargoVO(fun);
					}
				}				
			}
			getFacadeFactory().getCampanhaColaboradorFacade().adicionarObjCampanhaColaborador(getCampanhaVO().getListaCampanhaColaborador(), getCampanhaVO(), getCampanhaColaboradorVO());
			setCampanhaColaboradorVO(new CampanhaColaboradorVO(this.getCampanhaVO()));			
			
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCursoListaCampanhaColaborador() {
		try {
			getCampanhaVO().getListaCampanhaColaborador().clear();
			getCampanhaVO().setCurso(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCampoCurso() {
		getCampanhaVO().setCurso(new CursoVO());
	}

	public void limparCampoCampanhaCaptacaoCampanhaPublicoAlvo() {
		getCampanhaPublicoAlvoVO().setCampanhaCaptacao(new CampanhaVO());
	}

	public void limparCampoCursoCampanhaPublicoAlvo() {
		getCampanhaPublicoAlvoVO().setCurso(new CursoVO());
	}

	public void limparCursoInteresseCampanhaPublicoAlvo() {
		getCampanhaPublicoAlvoVO().setCursoInteresse(new CursoInteresseVO());
	}

	public void excluirFuncionarioCargo() {
		try {
			CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("campanhaColaboradorItens");
			getFacadeFactory().getCampanhaColaboradorFacade().excluirObjCampanhaColaborador(getCampanhaVO().getListaCampanhaColaborador(), getCampanhaVO(), obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarCampanhaMidia() {
		try {
			getFacadeFactory().getCampanhaMidiaFacade().adicionarObjCampanhaMidiaVOs(getCampanhaVO(), getCampanhaMidiaVO());
			setCampanhaMidiaVO(new CampanhaMidiaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarTodasCampanhaMidia() {
		try {
			List resultadoConsulta = consultarTipoMidiaPorNome("");
			Iterator i = resultadoConsulta.iterator();
			while (i.hasNext()) {
				TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
				getCampanhaMidiaVO().setTipoMidia(obj);
				getFacadeFactory().getCampanhaMidiaFacade().adicionarObjCampanhaMidiaVOs(getCampanhaVO(), getCampanhaMidiaVO());
				CampanhaMidiaVO c = new CampanhaMidiaVO();
				c.setDataInicioVinculacao(getCampanhaMidiaVO().getDataInicioVinculacao());
				c.setDataFimVinculacao(getCampanhaMidiaVO().getDataFimVinculacao());
				c.setImpactoEsperado(getCampanhaMidiaVO().getImpactoEsperado().intValue());
				c.setApresentarPreInscricao(getCampanhaMidiaVO().getApresentarPreInscricao().booleanValue());
				setCampanhaMidiaVO(c);
			}
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirCampanhaMidia() {
		try {
			CampanhaMidiaVO obj = (CampanhaMidiaVO) context().getExternalContext().getRequestMap().get("campanhaMidiaItens");
			getFacadeFactory().getCampanhaMidiaFacade().excluirObjCampanhaMidiaVOs(getCampanhaVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaFuncionarioCargo() {
		if (campoConsultaFuncionarioCargo == null) {
			campoConsultaFuncionarioCargo = "";
		}
		return campoConsultaFuncionarioCargo;
	}

	public void setCampoConsultaFuncionarioCargo(String campoConsultaFuncionarioCargo) {
		this.campoConsultaFuncionarioCargo = campoConsultaFuncionarioCargo;
	}

	public String getValorConsultaFuncionarioCargo() {
		if (valorConsultaFuncionarioCargo == null) {
			valorConsultaFuncionarioCargo = "";
		}
		return valorConsultaFuncionarioCargo;
	}

	public void setValorConsultaFuncionarioCargo(String valorConsultaFuncionarioCargo) {
		this.valorConsultaFuncionarioCargo = valorConsultaFuncionarioCargo;
	}

	public List getListaConsultaFuncionarioCargo() {
		if (listaConsultaFuncionarioCargo == null) {
			listaConsultaFuncionarioCargo = new ArrayList(0);
		}
		return listaConsultaFuncionarioCargo;
	}

	public void setListaConsultaFuncionarioCargo(List listaConsultaFuncionarioCargo) {
		this.listaConsultaFuncionarioCargo = listaConsultaFuncionarioCargo;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public CargoVO getCargoVO() {
		if (cargoVO == null) {
			cargoVO = new CargoVO();
		}
		return cargoVO;
	}

	public void setCargoVO(CargoVO cargoVO) {
		this.cargoVO = cargoVO;
	}

	public List getListaSelectItemMeta() {
		if (listaSelectItemMeta == null) {
			listaSelectItemMeta = new ArrayList(0);
		}
		return listaSelectItemMeta;
	}

	public void setListaSelectItemMeta(List listaSelectItemMeta) {
		this.listaSelectItemMeta = listaSelectItemMeta;
	}

	public List getListaSelectItemWorkflow() {
		if (listaSelectItemWorkflow == null) {
			listaSelectItemWorkflow = new ArrayList(0);
		}
		return listaSelectItemWorkflow;
	}

	public void setListaSelectItemWorkflow(List listaSelectItemWorkflow) {
		this.listaSelectItemWorkflow = listaSelectItemWorkflow;
	}

	public String getCampoConsultaCargo() {
		if (campoConsultaCargo == null) {
			campoConsultaCargo = "";
		}
		return campoConsultaCargo;
	}

	public void setCampoConsultaCargo(String campoConsultaCargo) {
		this.campoConsultaCargo = campoConsultaCargo;
	}

	public String getValorConsultaCargo() {
		if (valorConsultaCargo == null) {
			valorConsultaCargo = "";
		}
		return valorConsultaCargo;
	}

	public void setValorConsultaCargo(String valorConsultaCargo) {
		this.valorConsultaCargo = valorConsultaCargo;
	}

	public List getListaConsultaCargo() {
		if (listaConsultaCargo == null) {
			listaConsultaCargo = new ArrayList(0);
		}
		return listaConsultaCargo;
	}

	public void setListaConsultaCargo(List listaConsultaCargo) {
		this.listaConsultaCargo = listaConsultaCargo;
	}

	public CampanhaMidiaVO getCampanhaMidiaVO() {
		if (campanhaMidiaVO == null) {
			campanhaMidiaVO = new CampanhaMidiaVO();
		}
		return campanhaMidiaVO;
	}

	public void setCampanhaMidiaVO(CampanhaMidiaVO campanhaMidiaVO) {
		this.campanhaMidiaVO = campanhaMidiaVO;
	}

	public List getListaSelectItemTipoMidia() {
		if (listaSelectItemTipoMidia == null) {
			listaSelectItemTipoMidia = new ArrayList(0);
		}
		return listaSelectItemTipoMidia;
	}

	public void setListaSelectItemTipoMidia(List listaSelectItemTipoMidia) {
		this.listaSelectItemTipoMidia = listaSelectItemTipoMidia;
	}

	public String getCampoConsultaCursoInteresse() {
		return campoConsultaCursoInteresse;
	}

	public void setCampoConsultaCursoInteresse(String campoConsultaCursoInteresse) {
		this.campoConsultaCursoInteresse = campoConsultaCursoInteresse;
	}

	public String getValorConsultaCursoInteresse() {
		return valorConsultaCursoInteresse;
	}

	public void setValorConsultaCursoInteresse(String valorConsultaCursoInteresse) {
		this.valorConsultaCursoInteresse = valorConsultaCursoInteresse;
	}

	public List getListaConsultaCursoInteresse() {
		return listaConsultaCursoInteresse;
	}

	public void setListaConsultaCursoInteresse(List listaConsultaCursoInteresse) {
		this.listaConsultaCursoInteresse = listaConsultaCursoInteresse;
	}

	public CursoInteresseVO getCursoInteresseVO() {
		if (cursoInteresseVO == null) {
			cursoInteresseVO = new CursoInteresseVO();
		}
		return cursoInteresseVO;
	}

	public void setCursoInteresseVO(CursoInteresseVO cursoInteresseVO) {
		this.cursoInteresseVO = cursoInteresseVO;
	}

	public CampanhaPublicoAlvoVO getCampanhaPublicoAlvoVO() {
		if (campanhaPublicoAlvoVO == null) {
			campanhaPublicoAlvoVO = new CampanhaPublicoAlvoVO();
		}
		return campanhaPublicoAlvoVO;
	}

	public void setCampanhaPublicoAlvoVO(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO) {
		this.campanhaPublicoAlvoVO = campanhaPublicoAlvoVO;
	}

	public List getListaConsultaCursoCampanhaPublicoAlvo() {
		if (listaConsultaCursoCampanhaPublicoAlvo == null) {
			listaConsultaCursoCampanhaPublicoAlvo = new ArrayList(0);
		}
		return listaConsultaCursoCampanhaPublicoAlvo;
	}

	public void setListaConsultaCursoCampanhaPublicoAlvo(List listaConsultaCursoCampanhaPublicoAlvo) {
		this.listaConsultaCursoCampanhaPublicoAlvo = listaConsultaCursoCampanhaPublicoAlvo;
	}

	public String getCampoConsultaCursoCampanhaPublicoAlvo() {
		if (campoConsultaCursoCampanhaPublicoAlvo == null) {
			campoConsultaCursoCampanhaPublicoAlvo = "";
		}
		return campoConsultaCursoCampanhaPublicoAlvo;
	}

	public void setCampoConsultaCursoCampanhaPublicoAlvo(String campoConsultaCursoCampanhaPublicoAlvo) {
		this.campoConsultaCursoCampanhaPublicoAlvo = campoConsultaCursoCampanhaPublicoAlvo;
	}

	public String getValorConsultaCursoCampanhaPublicoAlvo() {
		if (valorConsultaCursoCampanhaPublicoAlvo == null) {
			valorConsultaCursoCampanhaPublicoAlvo = "";
		}
		return valorConsultaCursoCampanhaPublicoAlvo;
	}

	public void setValorConsultaCursoCampanhaPublicoAlvo(String valorConsultaCursoCampanhaPublicoAlvo) {
		this.valorConsultaCursoCampanhaPublicoAlvo = valorConsultaCursoCampanhaPublicoAlvo;
	}

	public String getCampoConsultaRegistroEntrada() {
		if (campoConsultaRegistroEntrada == null) {
			campoConsultaRegistroEntrada = "";
		}
		return campoConsultaRegistroEntrada;
	}

	public void setCampoConsultaRegistroEntrada(String campoConsultaRegistroEntrada) {
		this.campoConsultaRegistroEntrada = campoConsultaRegistroEntrada;
	}

	public String getValorConsultaRegistroEntrada() {
		if (valorConsultaRegistroEntrada == null) {
			valorConsultaRegistroEntrada = "";
		}
		return valorConsultaRegistroEntrada;
	}

	public void setValorConsultaRegistroEntrada(String valorConsultaRegistroEntrada) {
		this.valorConsultaRegistroEntrada = valorConsultaRegistroEntrada;
	}

	public List getListaConsultaRegistroEntrada() {
		if (listaConsultaRegistroEntrada == null) {
			listaConsultaRegistroEntrada = new ArrayList(0);
		}
		return listaConsultaRegistroEntrada;
	}

	public void setListaConsultaRegistroEntrada(List listaConsultaRegistroEntrada) {
		this.listaConsultaRegistroEntrada = listaConsultaRegistroEntrada;
	}

	public String getCampoConsultaCampanhaCaptacao() {
		if (campoConsultaCampanhaCaptacao == null) {
			campoConsultaCampanhaCaptacao = "";
		}
		return campoConsultaCampanhaCaptacao;
	}

	public void setCampoConsultaCampanhaCaptacao(String campoConsultaCampanhaCaptacao) {
		this.campoConsultaCampanhaCaptacao = campoConsultaCampanhaCaptacao;
	}

	public String getValorConsultaCampanhaCaptacao() {
		if (valorConsultaCampanhaCaptacao == null) {
			valorConsultaCampanhaCaptacao = "";
		}
		return valorConsultaCampanhaCaptacao;
	}

	public void setValorConsultaCampanhaCaptacao(String valorConsultaCampanhaCaptacao) {
		this.valorConsultaCampanhaCaptacao = valorConsultaCampanhaCaptacao;
	}

	public List getListaConsultaCampanhaCaptacao() {
		if (listaConsultaCampanhaCaptacao == null) {
			listaConsultaCampanhaCaptacao = new ArrayList(0);
		}
		return listaConsultaCampanhaCaptacao;
	}

	public void setListaConsultaCampanhaCaptacao(List listaConsultaCampanhaCaptacao) {
		this.listaConsultaCampanhaCaptacao = listaConsultaCampanhaCaptacao;
	}

	public Boolean getConsiderarSabado() {
		if (considerarSabado == null) {
			considerarSabado = Boolean.FALSE;
		}
		return considerarSabado;
	}

	public void setConsiderarSabado(Boolean considerarSabado) {
		this.considerarSabado = considerarSabado;
	}

	public Boolean getConsiderarFeriados() {
		if (considerarFeriados == null) {
			considerarFeriados = Boolean.FALSE;
		}
		return considerarFeriados;
	}

	public void setConsiderarFeriados(Boolean considerarFeriados) {
		this.considerarFeriados = considerarFeriados;
	}

	public List getListaSelectItemFormacaoAcademica() {
		if (listaSelectItemFormacaoAcademica == null) {
			listaSelectItemFormacaoAcademica = new ArrayList(0);
		}
		return listaSelectItemFormacaoAcademica;
	}

	public void setListaSelectItemFormacaoAcademica(List listaSelectItemFormacaoAcademica) {
		this.listaSelectItemFormacaoAcademica = listaSelectItemFormacaoAcademica;
	}

	public Boolean getRevisitacaoCarteira() {
		if (revisitacaoCarteira == null) {
			revisitacaoCarteira = Boolean.FALSE;
		}
		return revisitacaoCarteira;
	}

	public void setRevisitacaoCarteira(Boolean revisitacaoCarteira) {
		this.revisitacaoCarteira = revisitacaoCarteira;
	}

	public Date getDataFinalAlteracaoCompromisso() {
		return dataFinalAlteracaoCompromisso;
	}

	public void setDataFinalAlteracaoCompromisso(Date dataFinalAlteracaoCompromisso) {
		this.dataFinalAlteracaoCompromisso = dataFinalAlteracaoCompromisso;
	}

	public Date getDataIncialAlteracaoCompromisso() {
		return dataIncialAlteracaoCompromisso;
	}

	public void setDataIncialAlteracaoCompromisso(Date dataIncialAlteracaoCompromisso) {
		this.dataIncialAlteracaoCompromisso = dataIncialAlteracaoCompromisso;
	}

	public List getListaCampanhaColaboradorAlterarCompromisso() {
		if (listaCampanhaColaboradorAlterarCompromisso == null) {
			listaCampanhaColaboradorAlterarCompromisso = new ArrayList();
		}
		return listaCampanhaColaboradorAlterarCompromisso;
	}

	public void setListaCampanhaColaboradorAlterarCompromisso(List listaCampanhaColaboradorAlterarCompromisso) {
		this.listaCampanhaColaboradorAlterarCompromisso = listaCampanhaColaboradorAlterarCompromisso;
	}

	public Date getDataNovoCompromisso() {
		if (dataNovoCompromisso == null) {
			dataNovoCompromisso = new Date();
		}
		return dataNovoCompromisso;
	}

	public void setDataNovoCompromisso(Date dataNovoCompromisso) {
		this.dataNovoCompromisso = dataNovoCompromisso;
	}

	public String getHoraNovoCompromisso() {
		if (horaNovoCompromisso == null) {
			horaNovoCompromisso = "";
		}
		return horaNovoCompromisso;
	}

	public void setHoraNovoCompromisso(String horaNovoCompromisso) {
		this.horaNovoCompromisso = horaNovoCompromisso;
	}

	public String getModalMensagemPeriodoCampanha() {
		if (getAbrirModalMensagemPeriodoCampanha()) {
			return "RichFaces.$('panelMensagemPeriodoCampanha').show()";
		}
		return "RichFaces.$('panelAlterarCompromisso').hide(); RichFaces.$('panelMensagemPeriodoCampanha').hide()";
	}

	public Boolean getAbrirModalMensagemPeriodoCampanha() {
		if (abrirModalMensagemPeriodoCampanha == null) {
			abrirModalMensagemPeriodoCampanha = Boolean.FALSE;
		}
		return abrirModalMensagemPeriodoCampanha;
	}

	public void setAbrirModalMensagemPeriodoCampanha(Boolean abrirModalMensagemPeriodoCampanha) {
		this.abrirModalMensagemPeriodoCampanha = abrirModalMensagemPeriodoCampanha;
	}

	public CampanhaColaboradorCursoVO getCampanhaColaboradorCursoVO() {
		if (campanhaColaboradorCursoVO == null) {
			campanhaColaboradorCursoVO = new CampanhaColaboradorCursoVO();
		}
		return campanhaColaboradorCursoVO;
	}

	public void setCampanhaColaboradorCursoVO(CampanhaColaboradorCursoVO campanhaColaboradorCursoVO) {
		this.campanhaColaboradorCursoVO = campanhaColaboradorCursoVO;
	}

	public void atualizarDataNaoGerarAgendaDuplicada() {
		if (this.getCampanhaPublicoAlvoVO().getNaoGerarAgendaParaProspectsComAgendaJaExistente()) {
			this.getCampanhaVO().setDataInicialVerificarJaExisteAgendaProspect(getCampanhaVO().getPeriodoInicio());
			this.getCampanhaVO().setDataFinalVerificarJaExisteAgendaProspect(getCampanhaVO().getPeriodoFim());
		}
	}

	public List getListaCompromissoGeradoConsultor() {
		if (listaCompromissoGeradoConsultor == null) {
			listaCompromissoGeradoConsultor = new ArrayList();
		}
		return listaCompromissoGeradoConsultor;
	}

	public void setListaCompromissoGeradoConsultor(List listaCompromissoGeradoConsultor) {
		this.listaCompromissoGeradoConsultor = listaCompromissoGeradoConsultor;
	}

	public List<SelectItem> getListaProcSeletivo() {
		if (listaProcSeletivo == null) {
			listaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaProcSeletivo;
	}

	public void setListaProcSeletivo(List<SelectItem> listaProcSeletivo) {
		this.listaProcSeletivo = listaProcSeletivo;
	}

	public void montarListSelectItemProcSeletivo(Integer unidadeEnsino) throws Exception {
		try {
			getListaProcSeletivo().clear();
			List<ProcSeletivoVO> listaProcessoSeletivo = new ArrayList<ProcSeletivoVO>(0);
			listaProcessoSeletivo.addAll(getFacadeFactory().getProcSeletivoFacade().consultarPorUnidadeEnsinoUltimosProcessosSeletivos(unidadeEnsino, 5, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                        getListaProcSeletivo().add(new SelectItem(0, ""));
			for (ProcSeletivoVO obj : listaProcessoSeletivo) {
				getListaProcSeletivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void atualizarListaProcessoSeletivo() {
		try {
			getCampanhaPublicoAlvoVO().getUnidadeEnsino().getCodigo();
			montarListSelectItemProcSeletivo(getCampanhaPublicoAlvoVO().getUnidadeEnsino().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTipoRecorrencia() {
		try {
			getListaTipoRecorrencia().clear();
			if (getCampanhaVO().getRecorrente()) {
				for (TipoRecorrenciaCampanhaEnum obj : TipoRecorrenciaCampanhaEnum.values()) {
					getListaTipoRecorrencia().add(new SelectItem(obj.getValor().toString(), obj.getDescricao()));
				}
			} else {
				getListaTipoRecorrencia().clear();
				getCampanhaVO().setTipoRecorrencia("");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarSegmentacaoProspect() {
        try {
        	if(getListaSegmentacoes().isEmpty()) {
        		List<SegmentacaoProspectVO> objs = new ArrayList<SegmentacaoProspectVO>(0);
        		objs = getFacadeFactory().getSegmentacaoProspectFacade().consultarSegmentosAtivos("", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());       		
        		getListaSegmentacoes().addAll(objs);
        		setMensagemID("msg_dados_consultados");
        	}
        } catch (Exception e) {
        	setListaSegmentacoes(new ArrayList<SegmentacaoProspectVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
	public List<SelectItem> getListaTipoRecorrencia() {
		if (listaTipoRecorrencia == null) {
			listaTipoRecorrencia = new ArrayList<SelectItem>(0);
		}
		return listaTipoRecorrencia;
	}

	public void setListaTipoRecorrencia(List<SelectItem> listaTipoRecorrencia) {
		this.listaTipoRecorrencia = listaTipoRecorrencia;
	}

	public List<SelectItem> getListaTipoCampanha() {
		if (listaTipoCampanha == null) {
			listaTipoCampanha = new ArrayList<SelectItem>(0);
		}
		return listaTipoCampanha;
	}

	public void setListaTipoCampanha(List<SelectItem> listaTipoCampanha) {
		this.listaTipoCampanha = listaTipoCampanha;
	}

	public String getUrlSegmentacao() {
		if (urlSegmentacao == null) {
			urlSegmentacao = "";
		}
		return urlSegmentacao;
	}

	public void setUrlSegmentacao(String urlSegmentacao) {
		this.urlSegmentacao = urlSegmentacao;
	}

	public List<SegmentacaoProspectVO> getListaSegmentacoes() {
		if (listaSegmentacoes == null) {
			listaSegmentacoes = new ArrayList<SegmentacaoProspectVO>();
		}
		return listaSegmentacoes;
	}

	public void setListaSegmentacoes(List<SegmentacaoProspectVO> listaSegmentacoes) {
		this.listaSegmentacoes = listaSegmentacoes;
	}

		public void montarListaSelectItemPoliticaGerarAgenda() {
		try {
                    for (PoliticaGerarAgendaEnum obj : PoliticaGerarAgendaEnum.values()) {
                        getListaPoliticaGerarAgenda().add(new SelectItem(obj.toString(),obj.getDescricao()));
                    }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public List<SelectItem> getListaPoliticaGerarAgenda() {
        if (listaPoliticaGerarAgenda == null) {
            listaPoliticaGerarAgenda = new ArrayList<SelectItem>(0);
        }
        return listaPoliticaGerarAgenda;
    }

    public void setListaPoliticaGerarAgenda(List<SelectItem> listaPoliticaGerarAgenda) {
        this.listaPoliticaGerarAgenda = listaPoliticaGerarAgenda;
    }

        
        public void adicionarCampanhaPublicoAlvoCampanhaClonada() {
            List<CampanhaPublicoAlvoVO> listaPublicoAlvoReadicionarCampanhaGerandoProspects = this.getCampanhaVO().getListaCampanhaPublicoAlvo();
            this.getCampanhaVO().setListaCampanhaPublicoAlvo(null);
            for (CampanhaPublicoAlvoVO publicoAlvo : listaPublicoAlvoReadicionarCampanhaGerandoProspects) {
                publicoAlvo.setCampanhaPublicoAlvoProspectVOs(null);
                this.setCampanhaPublicoAlvoVO(publicoAlvo);
                if (publicoAlvo.getRegistroEntrada().getCodigo().equals(0)) {
                    this.setRevisitacaoCarteira(true);
                } else {
                    this.setRevisitacaoCarteira(false);
                }
                adicionarCampanhaPublicoAlvo();
            }
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
    	

    	public void inicializarDadosProgressBarCampanhaPublicoAlvo() {
    		try {
    			getProgressBar().resetar();
    			if(getCampanhaVO().getListaCampanhaPublicoAlvo().size() > 0){
    				Integer prospects = 0;
    				for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : getCampanhaVO().getListaCampanhaPublicoAlvo()) {    					
    						prospects += campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs().size();    					
    				}    				    				
    				getProgressBar().iniciar(0l, prospects, "( 1 de " + prospects + " )" , true, this, "executarGeracaoAgendaDeAcordoPublicoAlvo"); 
    			}
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
       
    	
    	public void inicializarDadosProgressBarCampanhaPublicoAlvoEspecifico() {
    		try {
    			CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO) context().getExternalContext().getRequestMap().get("publicoAlvo");
    			setCampanhaPublicoAlvoVO(obj);
    			getProgressBarPublicoEspecifico().resetar();
    			getProgressBarPublicoEspecifico().iniciar(0l, getCampanhaPublicoAlvoVO().getCampanhaPublicoAlvoProspectVOs().size(), "( 1 de " + getCampanhaPublicoAlvoVO().getCampanhaPublicoAlvoProspectVOs().size() + " ) ", true, this, "regerarAgendaCampanhaPublicoAlvoEspecifico");    			
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	public void removerCompromissosNaoIniciacaoCampanha() {
    		try {
    			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().removerCompromissosNaoIniciacaoCampanha(getCampanhaVO().getCodigo(), false, getUsuarioLogado());    			
    			setMensagemID("msg_dados_excluidos");
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	
    	public void prepararParametrosRevisitacaoCarteiraAluno() {
    		try {
    			setBloquearCamposParaVisualizacao(false);
    			this.setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
    			this.getCampanhaPublicoAlvoVO().setTipoPublicoAlvo("AL");
    		} catch (Exception e) {
    		}
    	}
    	
    	public void prepararParametrosRevisitacaoCarteiraProspect() {
    		try {
    			setBloquearCamposParaVisualizacao(false);
    			this.setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
    			this.getCampanhaPublicoAlvoVO().setTipoPublicoAlvo("PR");
    			this.getCampanhaPublicoAlvoVO().setSegmentacao(Boolean.FALSE);
    		} catch (Exception e) {
    		}
    	}
    	
    	public void prepararParametrosRevisitacaoCarteiraCandidato() {
    		try {
    			setBloquearCamposParaVisualizacao(false);
    			this.setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
    			this.getCampanhaPublicoAlvoVO().setTipoPublicoAlvo("CD");
    		} catch (Exception e) {
    		}
    	}
    	
    	public void prepararParametrosRevisitacaoCarteiraResponsavelFinanceiro() {
    		try {
    			setBloquearCamposParaVisualizacao(false);
    			this.setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
    			this.getCampanhaPublicoAlvoVO().setTipoPublicoAlvo("RF");
    		} catch (Exception e) {
    		}
    	}
    	
    	public List<SelectItem> getListaSelectItemTipoGerarAgendaCampanha() {
    		List<SelectItem> itens = new ArrayList<SelectItem>(0);
    		itens.add(new SelectItem(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_DISTRIBUINDO_IGUALITARIAMENTE_ENTRE_CONSULTORES_CAMPANHA, TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_DISTRIBUINDO_IGUALITARIAMENTE_ENTRE_CONSULTORES_CAMPANHA.getValorApresentar()));
    		itens.add(new SelectItem(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_RESPONSAVEL, TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_RESPONSAVEL.getValorApresentar()));
    		itens.add(new SelectItem(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_ULTIMA_INTERACAO, TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_ULTIMA_INTERACAO.getValorApresentar()));
    		return itens;
    	}
    	
    	public void selecionarCampanhaPublicoAlvo() throws Exception {
    		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO)context().getExternalContext().getRequestMap().get("publicoAlvo");
    		setCampanhaPublicoAlvoVO(obj);
    		getCampanhaPublicoAlvoVO().getListaCampanhaConsultorProspectVOs().clear();
    		getFacadeFactory().getCampanhaFacade().realizarMontagemListaConsultorProspect(getCampanhaVO(), getCampanhaPublicoAlvoVO(), getUsuarioLogado());
    	}
    	
    	public void selecionarTotalProspectSelecionadoCampanhaPublicoAlvo() {
    		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO)context().getExternalContext().getRequestMap().get("publicoAlvo");
    		setCampanhaPublicoAlvoVO(obj);
    		setConsultorPublicoAlvoSelecionadoVO(null);
    		getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs().clear();
    		getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs().addAll(obj.getCampanhaPublicoAlvoProspectVOs());
    	}
    	
    	public void seleciontarTotalProsectSemConsultor() {
    		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO)context().getExternalContext().getRequestMap().get("publicoAlvo");
    		setCampanhaPublicoAlvoVO(obj);
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(getFacadeFactory().getCampanhaFacade().realizarVisualizacaoProspectsSemConsultor(getCampanhaPublicoAlvoVO(), getUsuarioLogado()));	
    	}
    	
    	public void seleciontarTotalGeralProsectSemConsultor() {
    		getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs().clear();
    		for (CampanhaPublicoAlvoVO obj : getCampanhaVO().getListaCampanhaPublicoAlvo()) {
    			getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs().addAll(getFacadeFactory().getCampanhaFacade().realizarVisualizacaoProspectsSemConsultor(obj, getUsuarioLogado()));
    		}
    	}

    	
    	public void selecionarTotalGeralProspectSelecionadoCampanha() throws Exception {
    		for (CampanhaPublicoAlvoVO obj : getCampanhaVO().getListaCampanhaPublicoAlvo()) {
    			getFacadeFactory().getCampanhaFacade().realizarMontagemListaConsultorProspect(getCampanhaVO(), obj, getUsuarioLogado());
    		}
    	}
    	
    	public void selecionarConsultorParaVisualizarProspects() {
    		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("itemConsultorProspect");
    		setConsultorPublicoAlvoSelecionadoVO(obj);
    		setCampanhaPublicoAlvoVO(obj.getCampanhaPublicoAlvoVO());
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(getFacadeFactory().getCampanhaFacade().realizarVisualizacaoProspectsPorConsultor(getCampanhaVO(), getCampanhaPublicoAlvoVO(), obj, false, getUsuarioLogado()));
    	}
    	
    	
    	public void selecionarQuantidadeCompromissoUltrapassouDataCampanhaParaVisualizarProspects() {
    		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("itemConsultorProspect");
    		getConsultorPublicoAlvoSelecionadoVO().setCodigo(obj.getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setNome(obj.getPessoa().getNome());
    		setCampanhaPublicoAlvoVO(new CampanhaPublicoAlvoVO());
    		setCampanhaPublicoAlvoVO(obj.getCampanhaPublicoAlvoVO());
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(getFacadeFactory().getCampanhaFacade().realizarVisualizacaoProspectsPorConsultor(getCampanhaVO(), getCampanhaPublicoAlvoVO(), obj, true, getUsuarioLogado()));
    	}
    	
    	
    	//Método visualizar Consultor Dados Gerais
    	public void selecionarProspectsVisualizarDadosGerais() {
    		CampanhaPublicoAlvoProspectVO obj = (CampanhaPublicoAlvoProspectVO) context().getExternalContext().getRequestMap().get("itemProspectPorConsultor");
    		setProspectVisualizacaoDadosGeraisVO(new ProspectsVO());
    		setProspectVisualizacaoDadosGeraisVO(obj.getProspect());		
    		getConsultorPublicoAlvoSelecionadoVO().setCodigo(obj.getConsultorDistribuicaoVO().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setCodigo(obj.getConsultorDistribuicaoVO().getPessoa().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setNome(obj.getConsultorDistribuicaoVO().getPessoa().getNome());
    		try {
    			getProspectVisualizacaoDadosGeraisVO().setConsultorSugerido((FuncionarioVO)getConsultorPublicoAlvoSelecionadoVO().clone());
    		} catch (CloneNotSupportedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
    	public void selecionarProspectQueNaoPossuiAgenda() {
    		CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("itemConsultorRegerarAgenda");
    		getConsultorPublicoAlvoSelecionadoVO().setCodigo(obj.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setCodigo(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setNome(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome());
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(obj.getListaProspectSemAgendaVOs());
    	}
    	
    	public void selecionarProspectComAgendaNaoRealizada() {
    		CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("itemConsultorRegerarAgenda");
    		getConsultorPublicoAlvoSelecionadoVO().setCodigo(obj.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setCodigo(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setNome(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome());
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(obj.getListaProspectComAgendaNaoRealizadaVOs());
    	}

    	public void selecionarProspectIniciouAgenda() {
    		CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("itemConsultorRegerarAgenda");
    		getConsultorPublicoAlvoSelecionadoVO().setCodigo(obj.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setCodigo(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setNome(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome());
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().realizarCarregamentoProspectIniciouAgendaParaVisualizacao(getCampanhaVO(), obj.getListaCompromissoAgendaIniciouCampanhaVOs(), getPublicoAlvoEspecifico(), getCampanhaPublicoAlvoVO().getCampanhaPublicoAlvoProspectVOs()));
    	}
    	//Método visualizar Prospect Dados Gerais
    	public void selecionarConsultorParaVisualizarProspectsGerais() {
    		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("itemConsultorProspect");
    		getConsultorPublicoAlvoSelecionadoVO().setCodigo(obj.getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
    		getConsultorPublicoAlvoSelecionadoVO().getPessoa().setNome(obj.getPessoa().getNome());
    		setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(getFacadeFactory().getCampanhaFacade().realizarVisualizacaoProspectsPorConsultorGeral(getCampanhaVO(), obj, getUsuarioLogado()));
    	}
    	
    	public void realizarRemocaoListaCompromissoProspects(){
    		for (CampanhaColaboradorVO campanhaColaboradorVO : getCampanhaVO().getListaCampanhaColaborador()) {
    			for(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO: campanhaColaboradorVO.getListaCompromissoAgendaIniciouCampanhaVOs()){
    				compromissoAgendaPessoaHorarioVO =  null;
    			}
    			Uteis.liberarListaMemoria(campanhaColaboradorVO.getListaCompromissoAgendaIniciouCampanhaVOs());			
    		}
    	}
    	
    	public void realizarRemocaoListaProspectsVisualizados(){
//    		for(CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO: getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs()){
//    			campanhaPublicoAlvoProspectVO.setCompromissoCampanhaPublicoAlvoProspectVO(null);
//    			campanhaPublicoAlvoProspectVO = null;
//    		}
//    		Uteis.liberarListaMemoria(getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs());
    	}

    	public FuncionarioVO getConsultorPublicoAlvoSelecionadoVO() {
    		if (consultorPublicoAlvoSelecionadoVO == null) {
    			consultorPublicoAlvoSelecionadoVO = new FuncionarioVO();
    		}
    		return consultorPublicoAlvoSelecionadoVO;
    	}

    	public void setConsultorPublicoAlvoSelecionadoVO(FuncionarioVO consultorPublicoAlvoSelecionadoVO) {
    		this.consultorPublicoAlvoSelecionadoVO = consultorPublicoAlvoSelecionadoVO;
    	}
    	
    	public String getCssCamposPanelRevisitacaoCarteira() {
    		if (!getBloquearCamposParaVisualizacao()) {
    			return "campos";
    		}
    		return "camposSomenteLeitura";
    	}
    	
    	public String getCssCamposPanelRevisitacaoCarteiraObrigatorio() {
    		if (!getBloquearCamposParaVisualizacao()) {
    			return "camposObrigatorios";
    		}
    		return "camposSomenteLeitura";
    	}

    	public Boolean getBloquearCamposParaVisualizacao() {
    		if (bloquearCamposParaVisualizacao == null) {
    			bloquearCamposParaVisualizacao = false;
    		}
    		return bloquearCamposParaVisualizacao;
    	}

    	public void setBloquearCamposParaVisualizacao(Boolean bloquearCamposParaVisualizacao) {
    		this.bloquearCamposParaVisualizacao = bloquearCamposParaVisualizacao;
    	}
    	
    	public void selecionarCampanhaPublicoAlvoParaVisualizarFiltros() {
    		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO)context().getExternalContext().getRequestMap().get("publicoAlvo");
    		setCampanhaPublicoAlvoVO(obj);
    		setBloquearCamposParaVisualizacao(true);
    	}
    	
    	public void executarGeracaoAgendaDeAcordoPublicoAlvo() {
    		try {    	    			
				getFacadeFactory().getCampanhaFacade().executarGeracaoAgendaDeAcordoPublicoAlvo(getCampanhaVO(), getProgressBar(), getUsuarioLogado());						
    			setMensagemID("msg_agendaGerada");
    		} catch (Exception e) {
    			getProgressBar().setForcarEncerramento(true);
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}    	    
    	
    	public void regerarAgendaCampanha() {
    		try {
    			getFacadeFactory().getCampanhaFacade().regerarAgendaCampanha(getCampanhaVO(), getProgressBarRegerarAgenda(), getUsuarioLogado());
    			setApresentarBotaoRegerarAgenda(false);
    			setMensagemID("msg_agendaRegerada");
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	public void regerarAgendaCampanhaPublicoAlvoEspecifico() {
    		try {
    			getFacadeFactory().getCampanhaFacade().regerarAgendaCampanhaPublicoAlvoEspecifico(getCampanhaVO(), getProgressBarPublicoEspecifico(), getCampanhaPublicoAlvoVO(), getUsuarioLogado());
    			getCampanhaPublicoAlvoVO().setApresentarBotaoRegerarAgendaPublicoAlvoEspecifico(false);
    			setApresentarBotaoRegerarAgenda(false);
    			setMensagemID("msg_agendaRegerada");
    		} catch (Exception e) {
    			getProgressBarPublicoEspecifico().setForcarEncerramento(true);
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}


    	public ProspectsVO getProspectVisualizacaoDadosGeraisVO() {
    		if (prospectVisualizacaoDadosGeraisVO == null) {
    			prospectVisualizacaoDadosGeraisVO = new ProspectsVO();
    		}
    		return prospectVisualizacaoDadosGeraisVO;
    	}

    	public void setProspectVisualizacaoDadosGeraisVO(ProspectsVO prospectVisualizacaoDadosGeraisVO) {
    		this.prospectVisualizacaoDadosGeraisVO = prospectVisualizacaoDadosGeraisVO;
    	}
    	
    	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoAcademica() {
    		if (getMarcarTodasSituacoesAcademicas()) {
    			realizarMarcarTodasSituacoes();
    		} else {
    			realizarDesmarcarTodasSituacoes();
    		}
    	}
    	
    	public void realizarMarcarTodasSituacoes(){
    		realizarSelecionarTodosSituacoes(true);
    	}
    	
    	public void realizarDesmarcarTodasSituacoes(){
    		realizarSelecionarTodosSituacoes(false);
    	}
    	
    	public void realizarSelecionarTodosSituacoes(boolean selecionado){
    		getCampanhaPublicoAlvoVO().setAbandonado(selecionado);
    		getCampanhaPublicoAlvoVO().setCursando(selecionado);
    		getCampanhaPublicoAlvoVO().setCancelado(selecionado);
    		getCampanhaPublicoAlvoVO().setPossiveisFormandos(selecionado);
    		getCampanhaPublicoAlvoVO().setFormandos(selecionado);
    		getCampanhaPublicoAlvoVO().setPreMatriculados(selecionado);
    		getCampanhaPublicoAlvoVO().setPreMatriculaCancelada(selecionado);
    		getCampanhaPublicoAlvoVO().setTrancados(selecionado);
    		getCampanhaPublicoAlvoVO().setTransferenciaExterna(selecionado);
    		getCampanhaPublicoAlvoVO().setTransferenciaInterna(selecionado);		
    		getCampanhaPublicoAlvoVO().setInadimplentes(selecionado);
    	}

    	public Boolean getMarcarTodasSituacoesAcademicas() {
    		if (marcarTodasSituacoesAcademicas == null) {
    			marcarTodasSituacoesAcademicas = false;
    		}
    		return marcarTodasSituacoesAcademicas;
    	}

    	public void setMarcarTodasSituacoesAcademicas(Boolean marcarTodasSituacoesAcademicas) {
    		this.marcarTodasSituacoesAcademicas = marcarTodasSituacoesAcademicas;
    	}

    	public void selecionarTotalizadorPublicoAlvo() {
    		setListaConsultorPublicoAlvoVOs(getFacadeFactory().getCampanhaFacade().realizarMontagemListaConsultorProspectTotalizador(getCampanhaVO(), getUsuarioLogado()));
    	}

    	public List<FuncionarioVO> getListaConsultorPublicoAlvoVOs() {
    		if (listaConsultorPublicoAlvoVOs == null) {
    			listaConsultorPublicoAlvoVOs = new ArrayList<FuncionarioVO>(0);
    		}
    		return listaConsultorPublicoAlvoVOs;
    	}

    	public void setListaConsultorPublicoAlvoVOs(List<FuncionarioVO> listaConsultorPublicoAlvoVOs) {
    		this.listaConsultorPublicoAlvoVOs = listaConsultorPublicoAlvoVOs;
    	}

    	public List<CampanhaPublicoAlvoProspectVO> getListaCampanhaPublicoAlvoProspectVisualizarDadosVOs() {
    		if (listaCampanhaPublicoAlvoProspectVisualizarDadosVOs == null) {
    			listaCampanhaPublicoAlvoProspectVisualizarDadosVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
    		}
    		return listaCampanhaPublicoAlvoProspectVisualizarDadosVOs;
    	}

    	public void setListaCampanhaPublicoAlvoProspectVisualizarDadosVOs(List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVisualizarDadosVOs) {
    		this.listaCampanhaPublicoAlvoProspectVisualizarDadosVOs = listaCampanhaPublicoAlvoProspectVisualizarDadosVOs;
    	}
    	
    	public void realizarRedistribuicaoProspectAgenda() {
    		try {
    			getFacadeFactory().getCampanhaFacade().realizarRedistribuicaoConsultorPublicoAlvo(getCampanhaVO(), getProgressBar(), getAlterarConsultorPadraoProspect(),  getUsuarioLogado(), getAlterarConsultorCompromissoProspectJaIniciado());
    			setApresentarBotaoRegerarAgenda(true);
    			setMensagemID("msg_registribuicaoRealizadaComSucesso");
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	public void inicializarDadosRedistribuicaoPublicoAlvoEspecifico() {
    		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO) context().getExternalContext().getRequestMap().get("publicoAlvo");
    		setCampanhaPublicoAlvoVO(obj);
    		getFacadeFactory().getCampanhaFacade().realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanhaPublicoAlvoEspecifico(getCampanhaVO(), getCampanhaPublicoAlvoVO(), getUsuarioLogado());
    		getFacadeFactory().getCampanhaFacade().inicializarDadosQuantidadeProspectPorSituacaoAtualCampanhaPorCampanhaPublicoAlvoEspecifico(getCampanhaPublicoAlvoVO(), getCampanhaVO().getListaCampanhaColaborador(), getUsuarioLogado());
    		consultarQuantidadeCompromissoIniciouAgendaCampanhaPublicoAlvoEspecifico(getCampanhaPublicoAlvoVO());
    		Ordenacao.ordenarLista(getCampanhaVO().getListaCampanhaColaborador(), "nomeConsultor");
    		setRedistribuirAgendaPublicoAlvoEspecifico(true);
    		setPublicoAlvoEspecifico(true);
    	}
    	
    	public void realizarRedistribuicaoConsultorPublicoAlvoEspecifico() {
    		try {
    			getFacadeFactory().getCampanhaFacade().realizarRedistribuicaoConsultorPublicoAlvoEspecifico(getCampanhaVO(), getCampanhaPublicoAlvoVO(), getProgressBar(), getAlterarConsultorPadraoProspect(),  getUsuarioLogado(), getAlterarConsultorCompromissoProspectJaIniciado());
    			getCampanhaPublicoAlvoVO().setApresentarBotaoRegerarAgendaPublicoAlvoEspecifico(true);
    			setRedistribuirAgendaPublicoAlvoEspecifico(false);
    			setMensagemID("msg_registribuicaoRealizadaComSucesso");
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	public void inicializarDadosRedistribuicaoCampanha() {
    		getFacadeFactory().getCampanhaFacade().realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanha(getCampanhaVO(), getUsuarioLogado());
    		getFacadeFactory().getCampanhaFacade().inicializarDadosQuantidadeProspectPorSituacaoAtualCampanha(getCampanhaVO(), getCampanhaVO().getListaCampanhaColaborador(), getUsuarioLogado());
    		consultarQuantidadeCompromissoIniciouAgendaCampanha();
    		setPublicoAlvoEspecifico(false);
    		Ordenacao.ordenarLista(getCampanhaVO().getListaCampanhaColaborador(), "nomeConsultor");
    	}
    	
    	public void consultarQuantidadeCompromissoIniciouAgendaCampanhaPublicoAlvoEspecifico(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO) {
    		getFacadeFactory().getCampanhaColaboradorFacade().realizarCarregamentoQuantidadeProspectIniciouAgendaPorConsultorPublicoAlvoEspecifico(getCampanhaVO().getCodigo(), campanhaPublicoAlvoVO, getCampanhaVO().getListaCampanhaColaborador(), getUsuarioLogado());
    	}
    	
    	public void consultarQuantidadeCompromissoIniciouAgendaCampanha() {
    		getFacadeFactory().getCampanhaColaboradorFacade().realizarCarregamentoQuantidadeProspectIniciouAgendaPorConsultor(getCampanhaVO(), getCampanhaVO().getListaCampanhaColaborador(), getUsuarioLogado());
    	}
    	
    	public List<SelectItem> getLisSelectItemPoliticaRedistribuicaoProspectAgendaVOs() {
    		List<SelectItem> itens = new ArrayList<SelectItem>(0);
    		itens.add(new SelectItem(PoliticaRedistribuicaoProspectAgendaEnum.TODOS, PoliticaRedistribuicaoProspectAgendaEnum.TODOS.getDescricao()));
    		itens.add(new SelectItem(PoliticaRedistribuicaoProspectAgendaEnum.PROSPECT_SEM_AGENDA, PoliticaRedistribuicaoProspectAgendaEnum.PROSPECT_SEM_AGENDA.getDescricao()));
    		itens.add(new SelectItem(PoliticaRedistribuicaoProspectAgendaEnum.PROSPECT_COM_AGENDA_NAO_REALIZADA, PoliticaRedistribuicaoProspectAgendaEnum.PROSPECT_COM_AGENDA_NAO_REALIZADA.getDescricao()));
    		return itens;
    	}

    	public Integer getQuantidadeCompromissoIniciouAgendaCampanha() {
    		if (quantidadeCompromissoIniciouAgendaCampanha == null) {
    			quantidadeCompromissoIniciouAgendaCampanha = 0;
    		}
    		return quantidadeCompromissoIniciouAgendaCampanha;
    	}

    	public void setQuantidadeCompromissoIniciouAgendaCampanha(Integer quantidadeCompromissoIniciouAgendaCampanha) {
    		this.quantidadeCompromissoIniciouAgendaCampanha = quantidadeCompromissoIniciouAgendaCampanha;
    	}

    	public Boolean getApresentarBotaoRegerarAgenda() {
    		if (apresentarBotaoRegerarAgenda == null) {
    			apresentarBotaoRegerarAgenda = false;
    		}
    		return apresentarBotaoRegerarAgenda;
    	}

    	public void setApresentarBotaoRegerarAgenda(Boolean apresentarBotaoRegerarAgenda) {
    		this.apresentarBotaoRegerarAgenda = apresentarBotaoRegerarAgenda;
    	}
    	
    	public Boolean getRedistribuirAgendaPublicoAlvoEspecifico() {
    		if (redistribuirAgendaPublicoAlvoEspecifico == null) {
    			redistribuirAgendaPublicoAlvoEspecifico = false;
    		}
    		return redistribuirAgendaPublicoAlvoEspecifico;
    	}

    	public void setRedistribuirAgendaPublicoAlvoEspecifico(Boolean redistribuirAgendaPublicoAlvoEspecifico) {
    		this.redistribuirAgendaPublicoAlvoEspecifico = redistribuirAgendaPublicoAlvoEspecifico;
    	}
    	
    	public List<SelectItem> getListaSelectItemTipoDistribuicaoConsultorVOs() {
    		List<SelectItem> itens = new ArrayList<SelectItem>(0);
    		CampanhaColaboradorVO obj = (CampanhaColaboradorVO) context().getExternalContext().getRequestMap().get("itemConsultorRegerarAgenda");
    		if (obj.getFuncionarioCargoVO().getAtivo()) {
    			itens.add(new SelectItem(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR, "Redistribuir"));
    		}
    		itens.add(new SelectItem(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.NAO_REDISTRIBUIR, "Não Redistribuir"));
    		itens.add(new SelectItem(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA, "Remover Agenda"));
    		return itens;
    	}

    	public Boolean getPublicoAlvoEspecifico() {
    		if (publicoAlvoEspecifico == null) {
    			publicoAlvoEspecifico = false;
    		}
    		return publicoAlvoEspecifico;
    	}

    	public void setPublicoAlvoEspecifico(Boolean publicoAlvoEspecifico) {
    		this.publicoAlvoEspecifico = publicoAlvoEspecifico;
    	}
    	
    	public void inicializarDadosProgressBarAdicionarCampanhaPublicoAlvo() {
    		try {    			
    			getProgressBarAdicionarProspect().resetar();    			
    			getProgressBarAdicionarProspect().iniciar(0l, 1000, "Consultando "+getCampanhaPublicoAlvoVO().getTipoPublicoAlvo_Apresentar()+"....", true, this, "adicionarCampanhaPublicoAlvoRevisitacaoCarteira");    			    		
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	public String getAutocompleteValorCurso() {
    		if (autocompleteValorCurso == null) {
    			autocompleteValorCurso = "";
    		}
    		return autocompleteValorCurso;
    	}

    	public void setAutocompleteValorCurso(String autocompleteValorCurso) {
    		this.autocompleteValorCurso = autocompleteValorCurso;
    	}
    	
    	public void selecionarCursoPorCodigo() {
    		if (!getAutocompleteValorCurso().isEmpty()) {
    			consultarCursoPorCodigo(getValorAutoComplete(getAutocompleteValorCurso()));
    		}
    	}
    	
    	public void consultarCursoPorCodigo(int codigo) {
    		try {
    			getCampanhaColaboradorCursoVO().getCursoVO().setCodigo(codigo);
    			getFacadeFactory().getCursoFacade().carregarDados(getCampanhaColaboradorCursoVO().getCursoVO(), NivelMontarDados.BASICO, getUsuarioLogado());
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
    	}
    	
    	private int getValorAutoComplete(String valor) {
    		if (valor != null) {
    			java.util.regex.Pattern p = java.util.regex.Pattern.compile("^.*\\((-?\\d+)\\)[ \\t]*$");
    			java.util.regex.Matcher m = p.matcher(valor);
    			try {
    				if (m.matches()) {
    					// save the entity id in the managed bean and strip the
    					// entity id from the suggested string
    					valor = valor.substring(0, valor.lastIndexOf('('));
    					return Integer.parseInt(m.group(1));
    				}
    			} catch (java.lang.NumberFormatException e) {
    				e.printStackTrace();
    			}
    		}
    		return 0;
    	}
    	
    	public void realizarVerificacaoGerarAgendaFinalizou(){
    		if(!getProgressBar().getAtivado() || getProgressBar().getException() != null){
    			if(getProgressBar().getException() != null){
    				setMensagemDetalhada("msg_erro", getProgressBar().getException().getMessage(), Uteis.ERRO);
    				setProgressBar(null);
    			} 
    			getProgressBar().setPollAtivado(false);
    			//return  "RichFaces.$('panelStatusNota').hide();RichFaces.$('panelGerarAgenda').hide();";
    		}
    		//return "";
    	}
    	    
    	public List<PessoaVO> getListaPessoaDuplicadaVOs() {
    		if (listaPessoaDuplicadaVOs == null) {
    			listaPessoaDuplicadaVOs = new ArrayList<PessoaVO>(0);
    		}
    		return listaPessoaDuplicadaVOs;
    	}

    	public void setListaPessoaDuplicadaVOs(List<PessoaVO> listaPessoaDuplicadaVOs) {
    		this.listaPessoaDuplicadaVOs = listaPessoaDuplicadaVOs;
    	}
    	
    	public void visualizarResponsavelFinanceiroDuplicado() {
    		CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO) context().getExternalContext().getRequestMap().get("publicoAlvo");
    		setCampanhaPublicoAlvoVO(obj);
    	}
    	
    	public void imprimirResponsavelDuplicado() {
    		
    	}

		public ProgressBarVO getProgressBarPublicoEspecifico() {
			if(progressBarPublicoEspecifico == null){
				progressBarPublicoEspecifico = new ProgressBarVO();
			}
			return progressBarPublicoEspecifico;
		}

		public void setProgressBarPublicoEspecifico(ProgressBarVO progressBarPublicoEspecifico) {
			this.progressBarPublicoEspecifico = progressBarPublicoEspecifico;
		}

		public ProgressBarVO getProgressBarAdicionarProspect() {
			if(progressBarAdicionarProspect == null){
				progressBarAdicionarProspect = new ProgressBarVO();
			}
			return progressBarAdicionarProspect;
		}

		public void setProgressBarAdicionarProspect(ProgressBarVO progressBarAdicionarProspect) {
			this.progressBarAdicionarProspect = progressBarAdicionarProspect;
		}

		public ProgressBarVO getProgressBarRegerarAgenda() {
			if(progressBarRegerarAgenda == null){
				progressBarRegerarAgenda = new ProgressBarVO();
			}
			return progressBarRegerarAgenda;
		}

		public void setProgressBarRegerarAgenda(ProgressBarVO progressBarRegerarAgenda) {
			this.progressBarRegerarAgenda = progressBarRegerarAgenda;
		}

		public Boolean getAlterarConsultorPadraoProspect() {
			if(alterarConsultorPadraoProspect == null){
				alterarConsultorPadraoProspect = false;
			}
			return alterarConsultorPadraoProspect;
		}

		public void setAlterarConsultorPadraoProspect(Boolean alterarConsultorPadraoProspect) {
			this.alterarConsultorPadraoProspect = alterarConsultorPadraoProspect;
		}
 
		/**
		 * 
		 * Sincroniza todos os prospects na lista que ainda nao estao sincronizados com o RD Station
		 * 
		 */
		public void sincronizarTodosProspectsRdStation() {
	    	
			try {
				
				ArrayList<ProspectsVO> campanhaPublicoAlvoProspect = filtrarProspectsQueSeraoSincronizados( (ArrayList<CampanhaPublicoAlvoProspectVO>) campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs());
				
				if(campanhaPublicoAlvoProspect.isEmpty()) {
					setMensagemID("msg_dados_nenhum_registro");
					return;
				}
				
				setMensagemID("msg_SolicitacaoProcessadaEmSegundoPlano", Uteis.SUCESSO);
				enviarTodosProspectsRdStation(campanhaPublicoAlvoProspect);
				consultar();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
	 
	    }
		
		public ArrayList<ProspectsVO> filtrarProspectsQueSeraoSincronizados(ArrayList<CampanhaPublicoAlvoProspectVO> listaCompleta){
			
			ArrayList<ProspectsVO> prospectsVos = new ArrayList<>();
			
			for(CampanhaPublicoAlvoProspectVO busca : listaCompleta) {
				if(!busca.getProspect().getSincronizadoRDStation())
					prospectsVos.add(busca.getProspect());
			}
			
			return prospectsVos;
			
		}

		/**
		 * Envia a lista de prospects para sincronizacao com o RD Station
		 * @param prospectsVos
		 */
	    public void enviarTodosProspectsRdStation(List<ProspectsVO> prospectsVos) {
	    	getFacadeFactory().getLeadInterfaceFacade().incluirListaDeLeadsNoRdStation(prospectsVos, getConfiguracaoGeralPadraoSistema());
	    }

		public Boolean getAlterarConsultorCompromissoProspectJaIniciado() {
			if (alterarConsultorCompromissoProspectJaIniciado == null) {
				alterarConsultorCompromissoProspectJaIniciado = false;
			}
			return alterarConsultorCompromissoProspectJaIniciado;
		}

		public void setAlterarConsultorCompromissoProspectJaIniciado(Boolean alterarConsultorCompromissoProspectJaIniciado) {
			this.alterarConsultorCompromissoProspectJaIniciado = alterarConsultorCompromissoProspectJaIniciado;
		}
	    
	    
}