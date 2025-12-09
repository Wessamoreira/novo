package controle.crm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DownloadVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.ArquivoEtapaWorkflowVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.MotivoInsucessoVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.TipoProspectVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoOrigemCadastroProspectEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilNavegacao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.crm.EtapaWorkflow;
import negocio.facade.jdbc.crm.InteracaoWorkflow;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas interacaoWorkflowForm.jsp interacaoWorkflowCons.jsp) com as
 * funcionalidades da classe
 * <code>InteracaoWorkflow</code>. Implemtação da camada controle (Backing
 * Bean).
 *
 * @see SuperControle
 * @see InteracaoWorkflow
 * @see InteracaoWorkflowVO
 */
@Controller("InteracaoWorkflowControle")
@Scope("viewScope")
@Lazy
public class InteracaoWorkflowControle extends SuperControle {

    private InteracaoWorkflowVO interacaoWorkflowVO;
    private ProspectsVO prospectsVO;
    private Date dataInicioPosterior;
    private ArquivoVO arquivoVO;
    private UsuarioVO usuarioAdministrador;
    private Boolean habilitarConsultaProspect;
    private Boolean prospeccaoIniciada;
    private List listaConsultaTipoProspect;
    private List listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemMotivoEtapaWorkflow;
    private List listaSelectItemTipoMidia;
    private List listaSelectItemMotivoInsucesso;
    private List listaSelectItemTurno;
    private List listaSelectItemCursoInteresse;
    private String tempoDecorridoTemporario;
    private int cont = 0;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List<CursoVO> listaConsultaCurso;
    private List<CampanhaVO> listaCampanha;
    private Boolean apresentarRichModalCampanha;
    private Boolean azul;
    private Boolean vermelho;
    private Boolean amarelo;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    private List listaConsultaCidade;
    private CursoInteresseVO cursoInteresseVO;
    private CursoInteresseVO cursoInteresseVO2;
    private List<String> observacoesEtapasAnteriores;
    // os boolean novoProspect e novoCompromisso sao para controlar as entidades respectivas que ainda nao foram gravadas.
    private Boolean novoProspect;
    private Boolean ligacaoAtiva;
    private Boolean novoCompromisso;
    private Boolean adiarCompromisso;
    private Date dataCompromissoAdiado;
    private String horaCompromissoAdiado;
    private String horaFimCompromissoAdiado;
    private List etapasAnteriores;
    private Boolean abrirModalMensagemErro;
    private Boolean abrirModalPreencherObservacoes;
    private String modalApresentadoPermissaoVoltarEtapa;
    private Boolean scriptExpandido;
    private List<InteracaoWorkflowVO> listaInteracoesGravar;
    private List<InteracaoWorkflowVO> listaInteracoesPercorridas;
    private HashMap<Integer, String> hashMapObservacoesAnteriores;
    private List<InteracaoWorkflowVO> listaTodasEtapasWorkflow;
    private List<InteracaoWorkflowVO> listaInteracoesObservacaoObrigatoriaNaoPreenchida;
    private InteracaoWorkflowVO interacaoTemp;
    private String popUpMatricula;
    private AgendaPessoaHorarioVO agendaPessoaHorarioVO;
    private Boolean iniciarMatricula;
    private List listaCompromissoFuturo;
    private FormacaoAcademicaVO formacaoAcademicaVO;
    private List listaSelectItemAreaConhecimento;
    private List listaSelectItemNacionalidade;
    private String campoConsultaNaturalidade;
    private String valorConsultaNaturalidade;
    private List listaConsultaNaturalidade;
    private boolean botaoIniciar = Boolean.FALSE;
    private Boolean filtrarTabPanel;
    private Boolean campanhaCobranca;
    private CampanhaVO campanhaVO;
    
    private MatriculaVO matriculaVO;
    private List<SelectItem> listaMatricula;
    private String identificadorTurma;
    private String nomeUnidade;
	private String autocompleteValorCurso;
	private String autocompleteValorCurso2;
    
    
    public InteracaoWorkflowControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    @PostConstruct
    public void realizarInicializacaoPeloServlet() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            montarListaSelectItemMotivoInsucesso();
            FacesContext contexto = FacesContext.getCurrentInstance();
            contexto.getELContext().getELResolver().getValue(contexto.getELContext(), null, "InteracaoWorkflowNivelAplicacaoControle");
            getListaInteracoesGravar().clear();
            getEtapasAnteriores().clear();
            Integer codigoProspectLigacaoAtivaSemAgenda = 0;
            Integer codigoCompromissoAgenda = 0;
            Integer codigoEtapaAtual = 0;
            Boolean novoProspect = false;
            Boolean ligacaoAtivaSemAgenda = false;
            if (request.getAttribute("codigo") != null) {
                codigoCompromissoAgenda = Integer.parseInt((String) request.getAttribute("codigo"));
            }
            if (request.getAttribute("etapaAtual") != null) {
                codigoEtapaAtual = Integer.parseInt((String) request.getAttribute("etapaAtual"));
            }
            if (request.getAttribute("novoProspect") != null) {
                novoProspect = Boolean.parseBoolean((String) request.getAttribute("novoProspect"));
            }
            if (request.getAttribute("ligacaoAtivaSemAgenda") != null) {
                ligacaoAtivaSemAgenda = Boolean.parseBoolean((String) request.getAttribute("ligacaoAtivaSemAgenda"));
            }
            if (request.getAttribute("codigoProspectLigacaoAtivaSemAgenda") != null) {
                codigoProspectLigacaoAtivaSemAgenda = Integer.parseInt((String) request.getAttribute("codigoProspectLigacaoAtivaSemAgenda"));
            }
            
            if (novoProspect) {
                setNovoProspect(true);
                setLigacaoAtiva(false);
                setNovoCompromisso(true);
                montarListaSelectItemUnidadeEnsino();
                montarListaSelectItemNacionalidade();
                montarListaSelectItemAreaConhecimento();
                montarListaSelectItemTipoMidia();
                //if (getControladorInteracaoWorkflowNivelAplicacaoControle().getInteracaoWorkflowNivelAplicacaoVONovoProspect().getEtapaWorkflow().getCodigo().equals(0)) {
                if (!getControladorInteracaoWorkflowNivelAplicacaoControle().getMapaInteracaoNovoProspectVOs().containsKey(TipoCampanhaEnum.LIGACAO_RECEPTIVA.name() + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().toString())) {
                    realizarPreenchimentoInteracaoNovoProspect();
                    getControladorInteracaoWorkflowNivelAplicacaoControle().getMapaInteracaoNovoProspectVOs().put(TipoCampanhaEnum.LIGACAO_RECEPTIVA.name() + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().toString(), getInteracaoWorkflowVO());
                    //getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaBasico(getControladorInteracaoWorkflowNivelAplicacaoControle().getInteracaoWorkflowNivelAplicacaoVONovoProspect(), getInteracaoWorkflowVO());
                } else {
                    //getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaBasico(getInteracaoWorkflowVO(), getControladorInteracaoWorkflowNivelAplicacaoControle().getInteracaoWorkflowNivelAplicacaoVONovoProspect());
                    verificarSeInteracaoWorkFlowExisteAplicacao();
                }
              
                setMensagem("");
                setMensagemID("", Uteis.ALERTA);
            } else if (ligacaoAtivaSemAgenda) {
                setNovoProspect(false);
                setLigacaoAtiva(true);
                setNovoCompromisso(true);
                ProspectsVO prospect = new ProspectsVO();
                prospect.setCodigo(codigoProspectLigacaoAtivaSemAgenda);
                getFacadeFactory().getProspectsFacade().carregarDados(prospect, NivelMontarDados.TODOS, usuarioAdministrador);
                //if (getControladorInteracaoWorkflowNivelAplicacaoControle().getInteracaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda().getEtapaWorkflow().getCodigo().equals(0)) {
                if (!getControladorInteracaoWorkflowNivelAplicacaoControle().getMapaInteracaoNovoProspectVOs().containsKey(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo())) {
                    realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(prospect);
                    getControladorInteracaoWorkflowNivelAplicacaoControle().getMapaInteracaoNovoProspectVOs().put(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA.name() + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().toString(), getInteracaoWorkflowVO());
                    //getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaBasicoLigacaoAtivaSemAgenda(getControladorInteracaoWorkflowNivelAplicacaoControle().getInteracaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda(), getInteracaoWorkflowVO());
                } else {
                    //getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaBasicoLigacaoAtivaSemAgenda(getInteracaoWorkflowVO(), getControladorInteracaoWorkflowNivelAplicacaoControle().getInteracaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda());
                    verificarSeInteracaoWorkFlowSemAgendaExisteAplicacao(prospect);
                    getInteracaoWorkflowVO().setProspect(prospect);
                }
            } else {
                realizarPreenchimentoInteracaoProspectExistente(codigoCompromissoAgenda, codigoEtapaAtual);
                setNovoProspect(false);
                setLigacaoAtiva(false);
                setNovoCompromisso(false);
                setListaInteracoesPercorridas(getFacadeFactory().getInteracaoWorkflowFacade().consultarInteracoesWorkflowExistentesPorCodigoCompromisso(getInteracaoWorkflowVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getObservacoesEtapasAnteriores().clear();
                adicionarObservacoesAnteriores(getListaInteracoesPercorridas());
                getFacadeFactory().getProspectsFacade().carregarDados(getInteracaoWorkflowVO().getProspect(), NivelMontarDados.TODOS, usuarioAdministrador);
                getFacadeFactory().getProspectsFacade().executarValidarDadosProspectsConformePessoa(getInteracaoWorkflowVO().getProspect());
            }
            montarCampanhaPorCodigoCompromisso(codigoCompromissoAgenda);
            if(getCampanhaCobranca()){
            	 montarListaMatricula(getInteracaoWorkflowVO().getProspect().getCodigo());
            }
            montarListaSelectItemUnidadeEnsino();
            montarListaSelectItemNacionalidade();
            montarListaSelectItemAreaConhecimento();
            montarListaSelectItemTipoMidia();
            setListaTodasEtapasWorkflow(getFacadeFactory().getInteracaoWorkflowFacade().preencherListaTodasEtapasWorkflow(EtapaWorkflow.consultarEtapaWorkflows(getInteracaoWorkflowVO().getWorkflow().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()), getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getListaTodasEtapasWorkflow()) && getListaCampanha().size() <=1 && novoProspect) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarExistenciaCampanhaFuncionario") );
			}
            pesquisarEtapasAnteriores();
            getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
            getInteracaoWorkflowVO().getResponsavel().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
            if (getInteracaoWorkflowVO().getCurso().getCodigo() != 0) {
                getCursoInteresseVO2().getCurso().setCodigo(getInteracaoWorkflowVO().getCurso().getCodigo());
                getCursoInteresseVO2().getCurso().setNome(getInteracaoWorkflowVO().getCurso().getNome());
            } else {
                CursoInteresseVO cursoInteresse = getFacadeFactory().getCursoInteresseFacade().consultarPorCodigoProspect(getInteracaoWorkflowVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
                getCursoInteresseVO2().getCurso().setCodigo(cursoInteresse.getCurso().getCodigo());
                getCursoInteresseVO2().getCurso().setNome(cursoInteresse.getCurso().getNome());
                getInteracaoWorkflowVO().getCurso().setCodigo(cursoInteresse.getCurso().getCodigo());
                getInteracaoWorkflowVO().getCurso().setNome(cursoInteresse.getCurso().getNome());
            }
            setScriptExpandido(!getUsuarioLogado().getPessoa().getOcultarDadosCRM());
            setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void verificarSeInteracaoWorkFlowExisteAplicacao() throws Exception {
        InteracaoWorkflowVO obj = new InteracaoWorkflowVO();
        obj = getControladorInteracaoWorkflowNivelAplicacaoControle().obterInteracaoWorkFlowPorTipoCampanhaUnidadeEnsino(TipoCampanhaEnum.LIGACAO_RECEPTIVA.name(), getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo());
        if (obj == null || obj.getCampanha().getCodigo().equals(0)) {
            realizarPreenchimentoInteracaoNovoProspect();
        } else {
            setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().realizarPreenchimentoInteracaoNovoProspect(obj.getCampanha().getCodigo(), getUsuarioLogado(), getUnidadeEnsinoLogado()));
            montarListaSelectItemMotivoEtapaWorkflow();
            getInteracaoWorkflowVO().getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado()));
            //getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaBasico(getInteracaoWorkflowVO(), obj);
        }
    }

    public void verificarSeInteracaoWorkFlowSemAgendaExisteAplicacao(ProspectsVO prospect) throws Exception {
        InteracaoWorkflowVO obj = new InteracaoWorkflowVO();
        obj = getControladorInteracaoWorkflowNivelAplicacaoControle().obterInteracaoWorkFlowPorTipoCampanhaUnidadeEnsino(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA.name(), getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo());
        if (obj == null || obj.getCampanha().getCodigo().equals(0)) {
            realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(prospect);
            getControladorInteracaoWorkflowNivelAplicacaoControle().getMapaInteracaoNovoProspectVOs().put(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA.name() + "_" + getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().toString(), getInteracaoWorkflowVO());
        } else {
            setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(prospect, obj.getCampanha().getCodigo(), getUsuarioLogado(), getUnidadeEnsinoLogado()));
            montarListaSelectItemMotivoEtapaWorkflow();
            getInteracaoWorkflowVO().getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado()));
            //getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaBasicoLigacaoAtivaSemAgenda(getInteracaoWorkflowVO(), obj);
        }
    }

    public void montarListaSelectItemMotivoEtapaWorkflow() {
        getListaSelectItemMotivoEtapaWorkflow().clear();
        if (getInteracaoWorkflowVO().getEtapaWorkflow().getMotivo().contains(";")) {
            getListaSelectItemMotivoEtapaWorkflow().add(new SelectItem("", ""));
            String[] motivos = getInteracaoWorkflowVO().getEtapaWorkflow().getMotivo().split(";");
            for (String motivo : motivos) {
                if (!motivo.trim().isEmpty()) {
                    getListaSelectItemMotivoEtapaWorkflow().add(new SelectItem(motivo.trim(), motivo.trim()));
                }
            }
        } else if (!getInteracaoWorkflowVO().getEtapaWorkflow().getMotivo().trim().isEmpty()) {
            getListaSelectItemMotivoEtapaWorkflow().add(new SelectItem("", ""));
            getListaSelectItemMotivoEtapaWorkflow().add(new SelectItem(getInteracaoWorkflowVO().getEtapaWorkflow().getMotivo().trim(), getInteracaoWorkflowVO().getEtapaWorkflow().getMotivo().trim()));
        }
    }

    public void realizarPreenchimentoInteracaoNovoProspect() throws Exception {
        getListaCampanha().clear();
        getListaCampanha().addAll(getFacadeFactory().getCampanhaFacade().consultarPorCodigoCampanhaColaboradoFuncionarioPorTipoCampanha(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), "AT", TipoCampanhaEnum.LIGACAO_RECEPTIVA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        if (getListaCampanha().isEmpty()) {
            throw new Exception("Nenhuma campanha do tipo ligação receptiva foi encontrada para esse usuário " + getUsuarioLogado().getPessoa().getNome());
        } else if (getListaCampanha().size() == 1) {
            setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().realizarPreenchimentoInteracaoNovoProspect(getListaCampanha().get(0).getCodigo(), getUsuarioLogado(), getUnidadeEnsinoLogado()));
            montarListaSelectItemMotivoEtapaWorkflow();
            getInteracaoWorkflowVO().getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado()));
        } else {
            setApresentarRichModalCampanha(true);
        }
    }

    public void realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(ProspectsVO prospect) throws Exception {
        getListaCampanha().clear();
        Integer unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
        if (unidadeEnsino.intValue() == 0) {
            unidadeEnsino = prospect.getUnidadeEnsino().getCodigo();
        }
        getListaCampanha().addAll(getFacadeFactory().getCampanhaFacade().consultarPorCodigoCampanhaColaboradoFuncionarioPorTipoCampanha(getUsuarioLogado().getPessoa().getCodigo(), unidadeEnsino, "AT", TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        if (getListaCampanha().isEmpty()) {
            throw new Exception("Nenhuma campanha do tipo ligação ativa sem agenda foi encontrada para esse usuário " + getUsuarioLogado().getPessoa().getNome());
        } else if (getListaCampanha().size() == 1) {
            setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(prospect, getListaCampanha().get(0).getCodigo(), getUsuarioLogado(), getUnidadeEnsinoLogado()));
            montarListaSelectItemMotivoEtapaWorkflow();
            getInteracaoWorkflowVO().getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado()));
        } else {
            setApresentarRichModalCampanha(true);
        }
    }

    public void realizarPreenchimentoInteracaoProspectExistente(Integer codigoCompromissoAgenda, Integer codigoEtapaAtual) throws Exception {
        if (codigoCompromissoAgenda != 0) {
            //setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().consultarInteracaoWorkflowExistentePorCodigoCompromissoPorEtapaAtual(codigoCompromissoAgenda, codigoEtapaAtual, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            if (getInteracaoWorkflowVO().getCodigo() == 0) {
                setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().executarPreenchimnetoNovaInteracaoWorkflowPorCompromissoPorEtapaAtual(codigoCompromissoAgenda, codigoEtapaAtual, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

                getInteracaoWorkflowVO().setNovoObj(true);
            } else {
                getInteracaoWorkflowVO().setNovoObj(false);
            }
            montarListaSelectItemMotivoEtapaWorkflow();
            getInteracaoWorkflowVO().getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
        } else {
            throw new Exception("Nenhum Compromissso informado.");
        }
    }

    public void realizarSelecaoCampanha() {
        try {
            CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
            setInteracaoWorkflowVO(getFacadeFactory().getInteracaoWorkflowFacade().realizarPreenchimentoInteracaoNovoProspect(obj.getCodigo(), getUsuarioLogado(), getUnidadeEnsinoLogado()));
            montarListaSelectItemMotivoEtapaWorkflow();
            getInteracaoWorkflowVO().getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
            getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
            setApresentarRichModalCampanha(false);
            setMensagem("");
            setMensagemID("", Uteis.ALERTA);
            obj = null;
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void voltarEtapaPermissaoUsuarioAdministrador() {
        try {
            setModalApresentadoPermissaoVoltarEtapa("");
            InteracaoWorkflowVO obj = (InteracaoWorkflowVO) context().getExternalContext().getRequestMap().get("interacaoItens");
            if (obj != null) {
                getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaCompleto(getInteracaoTemp(), obj);
            }
            getInteracaoWorkflowVO().getWorkflow().setLivreAcessoEtapas(Boolean.TRUE);
            if (!getInteracaoWorkflowVO().getWorkflow().getLivreAcessoEtapas()) {
                if (!getLoginControle().getPermissaoAcessoMenuVO().getVoltarEtapaAnterior()) {
                    if (!getUsuarioAdministrador().getUsername().equals("")) {
                        setUsuarioAdministrador(ControleAcesso.verificarLoginUsuario(getUsuarioAdministrador().getUsername(), getUsuarioAdministrador().getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS));
                        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("VoltarEtapaAnterior", getUsuarioAdministrador());
                        selecionarEtapa(getInteracaoTemp());
                        setInteracaoTemp(new InteracaoWorkflowVO());

                        setModalApresentadoPermissaoVoltarEtapa("RichFaces.$('panelPermissaoVoltarEtapa').hide()");
                    } else {
                        setModalApresentadoPermissaoVoltarEtapa("RichFaces.$('panelPermissaoVoltarEtapa').show()");
                    }
                } else {
                    selecionarEtapa(getInteracaoTemp());
                    setInteracaoTemp(new InteracaoWorkflowVO());
                }
            } else {
                selecionarEtapa(getInteracaoTemp());
                setInteracaoTemp(new InteracaoWorkflowVO());
            }
            montarListaSelectItemMotivoEtapaWorkflow();
            setUsuarioAdministrador(new UsuarioVO());
        } catch (Exception e) {
            setUsuarioAdministrador(new UsuarioVO());
            setInteracaoTemp(new InteracaoWorkflowVO());
            setModalApresentadoPermissaoVoltarEtapa("RichFaces.$('panelMensagemErro').show()");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getCssObservacaoEtapa() {
        if (!getInteracaoWorkflowVO().getEtapaWorkflow().getObrigatorioInformarObservacao()) {
            return "campos";
        }
        return "camposObrigatoriosTextArea";
    }

    public String realizarNavegacaoProximaEtapa() {
        try {
            getInteracaoWorkflowVO().setDataTermino(new Date());
            getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraMinutoComMascara(getInteracaoWorkflowVO().getDataTermino()));

            EtapaWorkflowAntecedenteVO obj = (EtapaWorkflowAntecedenteVO) context().getExternalContext().getRequestMap().get("etapaWorkflowAntecedenteItens");
            InteracaoWorkflowVO interacaoTemporaria = new InteracaoWorkflowVO();
            getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaCompleto(interacaoTemporaria, getInteracaoWorkflowVO());
            adicionarInteracaoWorkflowGravar(interacaoTemporaria);
            getFacadeFactory().getInteracaoWorkflowFacade().realizarNavegacaoProximaEtapa(getListaInteracoesGravar(), getListaInteracoesPercorridas(), getInteracaoWorkflowVO(), obj, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            pesquisarEtapasAnteriores();
            getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
            getObservacoesEtapasAnteriores().clear();
            adicionarObservacoesAnteriores(getListaInteracoesGravar());
            adicionarObservacoesAnteriores(getListaInteracoesPercorridas());
            setProspeccaoIniciada(Boolean.FALSE);
            cont = 0;
            inicializarContagemTempoProspeccao();
        } catch (Exception e) {
            if (getNovoProspect()) {
                getInteracaoWorkflowVO().getProspect().setCodigo(0);
            }
            if (getNovoCompromisso()) {
                getInteracaoWorkflowVO().getCompromissoAgendaPessoaHorario().setCodigo(0);
                getInteracaoWorkflowVO().getCompromissoAgendaPessoaHorario().setNovoObj(true);
            }
            getInteracaoWorkflowVO().setDataTermino(null);
            setAbrirModalMensagemErro(Boolean.TRUE);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        montarListaSelectItemMotivoEtapaWorkflow();
        return "";
    }

    public String realizarGravacaoEtapa() {
        try {
            realizarPersistenciaInteracaoProspect2();
            getListaInteracoesObservacaoObrigatoriaNaoPreenchida().clear();
            for (InteracaoWorkflowVO obj : getListaInteracoesGravar()) {
                if (obj.getEtapaWorkflow().getObrigatorioInformarObservacao() && obj.getObservacao().isEmpty() && !obj.getEtapaWorkflow().getCodigo().equals(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo())) {
                    setAbrirModalPreencherObservacoes(Boolean.TRUE);
                    getListaInteracoesObservacaoObrigatoriaNaoPreenchida().add(obj);
                }
            }
            if (getInteracaoWorkflowVO().getEtapaWorkflow().getObrigatorioInformarObservacao() && getInteracaoWorkflowVO().getObservacao().isEmpty()) {
                setAbrirModalPreencherObservacoes(Boolean.TRUE);
                getListaInteracoesObservacaoObrigatoriaNaoPreenchida().add(getInteracaoWorkflowVO());
            }
            if (!getListaInteracoesObservacaoObrigatoriaNaoPreenchida().isEmpty()) {
            	setMensagemDetalhada("msg_entre_dados", "Informe as observações obrigatórios de cada etapa.", Uteis.ALERTA);
                return "";
            }
            setAbrirModalPreencherObservacoes(Boolean.FALSE);
            getInteracaoWorkflowVO().setDataTermino(new Date());
            getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraMinutoComMascara(getInteracaoWorkflowVO().getDataTermino()));
            if (!getTempoDecorridoTemporario().equals("00:00:00")) {
                getInteracaoWorkflowVO().setTempoDecorrido(getTempoDecorridoTemporario());
            }
            adicionarInteracaoWorkflowGravar(getInteracaoWorkflowVO());
            getFacadeFactory().getInteracaoWorkflowFacade().realizarGravacaoInteracaoWorkflow(getListaInteracoesGravar(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			if (!getInteracaoWorkflowVO().getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO) && !getInteracaoWorkflowVO().getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
				getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarAtualizacaoEtapaAtualCompromisso(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), getListaInteracoesGravar().get(0).getCompromissoAgendaPessoaHorario().getCodigo(), getUsuarioLogado());
            }

            setDataInicioPosterior(null);
            setNovoCompromisso(false);
            setNovoProspect(false);
            setLigacaoAtiva(false);
            setProspeccaoIniciada(false);
            for (InteracaoWorkflowVO interacaoGravada : getListaInteracoesGravar()) {
                interacaoGravada.setGravada(Boolean.TRUE);
            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            for (InteracaoWorkflowVO interacaoRollBack : getListaInteracoesGravar()) {
                if (getNovoProspect()) {
                    interacaoRollBack.getProspect().setCodigo(0);
                    interacaoRollBack.getProspect().setNovoObj(true);
                }
                if (getNovoCompromisso()) {
                    interacaoRollBack.getCompromissoAgendaPessoaHorario().setCodigo(0);
                    interacaoRollBack.getCompromissoAgendaPessoaHorario().setNovoObj(true);
                }

                if (!interacaoRollBack.getGravada()) {
                    interacaoRollBack.setCodigo(0);
                    interacaoRollBack.setNovoObj(Boolean.TRUE);
                }
            }
            setAbrirModalMensagemErro(Boolean.TRUE);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return "";
    }

    public void selecionarEtapa(InteracaoWorkflowVO obj) throws Exception {
        InteracaoWorkflowVO interacaoTemporaria = new InteracaoWorkflowVO();
        getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaCompleto(interacaoTemporaria, getInteracaoWorkflowVO());
        adicionarInteracaoWorkflowGravar(interacaoTemporaria);
        getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaDadosExclusivosInteracaoEtapa(getInteracaoWorkflowVO(), obj, getUsuarioLogado());
        getFacadeFactory().getInteracaoWorkflowFacade().preencherInteracaoWorkflowSemReferenciaMemoriaSemEtapaSemInteracao(obj, interacaoTemporaria);
        pesquisarEtapasAnteriores();
        getObservacoesEtapasAnteriores().clear();
        adicionarObservacoesAnteriores(getListaInteracoesGravar());
        adicionarObservacoesAnteriores(getListaInteracoesPercorridas());
        cont = 0;
        setProspeccaoIniciada(Boolean.FALSE);
        inicializarContagemTempoProspeccao();
        getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto
     * da classe
     * <code>InteracaoWorkflow</code>. Caso o objeto seja novo (ainda não
     * gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o
     * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
     * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem
     * de erro.
     */
//    public String persistir() {
//        try {
//            getFacadeFactory().getInteracaoWorkflowFacade().persistir(interacaoWorkflowVO);
//            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
//        } catch (ConsistirException e) {
//            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//        } finally {
//            return "editar";
//        }
//    }
    public void consultarTipoProspect() throws Exception {
        setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspect(getProspectsVO().getCpf(), getProspectsVO().getCnpj(), getProspectsVO().getCodigo()));
        setHabilitarConsultaProspect(true);
    }

    public void incluirProspect() {
        try {
            getProspectsVO().setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum.MANUAL_INDICACAO_OUTRO_PROSPECT);
            realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel(getProspectsVO());
            getFacadeFactory().getProspectsFacade().persistirRegistroProspectRapido(getProspectsVO(), getInteracaoWorkflowVO().getProspect(), getInteracaoWorkflowVO().getCampanha(), getInteracaoWorkflowVO().getResponsavel(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void limparDadosReferenteProspect() {
        setProspectsVO(new ProspectsVO());
        montarListaSelectItemUnidadeEnsino();
        setMensagemID("msg_entre_prmconsulta", Uteis.SUCESSO);
    }

    public void limparDadosReferenteTipoProspect() {
    	getProspectsVO().setNovoObj(true);
    	getProspectsVO().setCodigo(0);
        getProspectsVO().setNome("");
        getProspectsVO().setCEP("");
        getProspectsVO().setEndereco("");
        getProspectsVO().setSetor("");
        getProspectsVO().setCidade(new CidadeVO());
        getProspectsVO().setEmailPrincipal("");
        getProspectsVO().setEmailSecundario("");
        getProspectsVO().setSkype("");
        getProspectsVO().setCelular("");
        getProspectsVO().setTelefoneComercial("");
        getProspectsVO().setTelefoneRecado("");
        getProspectsVO().setTelefoneResidencial("");
        getProspectsVO().setUnidadeEnsino(new UnidadeEnsinoVO());
        if (getProspectsVO().getFisico()) {
            getProspectsVO().setCnpj("");
            getProspectsVO().setRazaoSocial("");
        } else if (getProspectsVO().getJuridico()) {
            getProspectsVO().setCpf("");
            getProspectsVO().setSexo("");
            getProspectsVO().setRg("");
            getProspectsVO().setDataNascimento(new Date());
        }
    }

    public String getAbrirModalProspect() {
        if (getHabilitarConsultaProspect()) {
            setHabilitarConsultaProspect(false);
            if (getListaConsultaTipoProspect().isEmpty()) {
                return "RichFaces.$('panelTipoProspect').hide();";
            } else if (getListaConsultaTipoProspect().size() == 1) {
                selecionarProspectPesquisaCpfCnpjSemModal();
                return "RichFaces.$('panelTipoProspect').hide();";
            } else {
                return "RichFaces.$('panelTipoProspect').show();";
            }
        }
        return "RichFaces.$('panelTipoProspect').hide();";
    }

    public void selecionarProspectPesquisaCpfCnpjSemModal() {
        TipoProspectVO obj = (TipoProspectVO) getListaConsultaTipoProspect().get(0);
        try {
            selecionarProspectPesquisaCpfCnpj(obj);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void adicionarObservacoesInteracoesComObservacaoObrigatoriaNaoPreenchida() {
        for (InteracaoWorkflowVO obj : getListaInteracoesObservacaoObrigatoriaNaoPreenchida()) {
            for (InteracaoWorkflowVO obj1 : getListaInteracoesGravar()) {
                if (obj.getEtapaWorkflow().getCodigo().equals(obj1.getEtapaWorkflow().getCodigo())) {
                    obj1.setObservacao(obj.getObservacao());
                }
            }
        }
        realizarGravacaoEtapa();
    }

    public void selecionarProspectPesquisaCpfCnpj(TipoProspectVO obj) throws Exception {
        try {
            if (obj.getPerfisProspect().equals("PESSOA")) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
                if (!getProspectsVO().getArquivoFoto().isNovoObj()) {
                    setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString().toLowerCase(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
                }
            } else if (obj.getPerfisProspect().equals("PARCEIRO") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCnpj(obj.getCnpj(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("PARCEIRO") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCpf(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("FORNECEDOR") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCnpj(obj.getCnpj(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("FORNECEDOR") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCpf(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("PROSPECT")) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getProspectsVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            } else {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorUnidade(obj.getCnpj(), obj.getNome(), getProspectsVO(), getUsuarioLogado()));
            }
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getObterCaminhoFotoUsuario() throws Exception {
        return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getInteracaoWorkflowVO().getProspect().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), null, "foto_usuario.png", false);
    }

    public String getCaminhoServidorDownloadArquivo() {
        try {

            return "abrirPopup('" + getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getArquivoVO(), PastaBaseArquivoEnum.ARQUIVO, getConfiguracaoGeralPadraoSistema()) + "', 'ArquivoInteracao', 930,580)";
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public void registrarDownloadWF() throws Exception {
        selecionarArquivo();
        registrarDownload();
    }

    public String selecionarArquivo() {
        try {
            ArquivoEtapaWorkflowVO obj = (ArquivoEtapaWorkflowVO) context().getExternalContext().getRequestMap().get("arquivoWFItens");
            setArquivoVO(obj.getArquivo());
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public void registrarDownload() throws Exception {
        if (getArquivoVO() != null && getUsuarioLogado().getPessoa().getAluno()) {
            DownloadVO download = new DownloadVO();
            download.setArquivo(getArquivoVO());
            download.setPessoa(getUsuarioLogado().getPessoa());
            download.setDataDownload(new Date());
            //download.setTurma(getTurma());
            download.setDisciplina(getArquivoVO().getDisciplina().getCodigo());
            getFacadeFactory().getDownloadFacade().incluir(download, getUsuarioLogado());
        }
    }

    public void realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel(ProspectsVO prospectAtualizar) throws Exception {
        if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getAssociarNovoProspectComConsultorResponsavelCadastro()) {
            return;
        }
        try {
            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(),
                    Boolean.FALSE, getUsuarioLogado());
            prospectAtualizar.setConsultorPadrao(funcionario);
        } catch (Exception e) {
        }
    }

    public void realizarPersistenciaInteracaoProspect() {
        try {
            if (getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getObrigarTipoMidiaProspect()) {
                if ((getInteracaoWorkflowVO().getProspect().getTipoMidia().getCodigo() == null) || (getInteracaoWorkflowVO().getProspect().getTipoMidia().getCodigo().intValue() == 0)) {
                    throw new Exception("O campo COMO FICOU SABENDO DA INSTITUIÇÃO deve ser informado");
                }
            }
            if (getInteracaoWorkflowVO().getProspect().getCodigo().intValue() == 0) {
            	
            }
            if (getInteracaoWorkflowVO().getProspect().getCodigo().intValue() == 0) {            	
                getInteracaoWorkflowVO().getProspect().setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum.MANUAL_LIGACAO_RECEPTIVA);
                getInteracaoWorkflowVO().getProspect().setResponsavelCadastro(getUsuarioLogadoClone());
                realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel(getInteracaoWorkflowVO().getProspect());
                getFacadeFactory().getProspectsFacade().incluirRapidaPorLigacaoReceptia(getInteracaoWorkflowVO().getProspect(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            } else {
                getFacadeFactory().getProspectsFacade().alterarComValidarDados(getInteracaoWorkflowVO().getProspect(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            }
            setNovoProspect(false);
            setLigacaoAtiva(false);
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarPersistenciaInteracaoProspect2() throws Exception {
        if (getInteracaoWorkflowVO().getProspect().getCodigo().intValue() == 0) {
            getFacadeFactory().getProspectsFacade().incluirRapidaPorLigacaoReceptia(getInteracaoWorkflowVO().getProspect(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
        } else {
            getFacadeFactory().getProspectsFacade().alterarSemValidarDados(getInteracaoWorkflowVO().getProspect(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
        }
        setNovoProspect(false);
        setLigacaoAtiva(false);
//        setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
    }

    public void limparDadosReferenteTipoInteracaoProspect() {
        setHabilitarConsultaProspect(true);
        if (getProspectsVO().getFisico()) {
            getProspectsVO().setRazaoSocial("");
            getProspectsVO().setInscricaoEstadual("");
            getProspectsVO().setCnpj("");
        } else if (getProspectsVO().getJuridico()) {
            getProspectsVO().setCpf("");
            getProspectsVO().setSexo("");
            getProspectsVO().setRg("");
            getProspectsVO().setDataNascimento(new Date());
        }
    }

    public void consultarTipoInteracaoProspect() throws Exception {
    	if (!getInteracaoWorkflowVO().getProspect().getCpf().trim().equals("")) {
	        setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspect(getInteracaoWorkflowVO().getProspect().getCpf(), getInteracaoWorkflowVO().getProspect().getCnpj(), getInteracaoWorkflowVO().getProspect().getCodigo()));
	        setHabilitarConsultaProspect(true);
    	}
    }

    public void consultarTipoInteracaoProspectEmail() throws Exception {
    	if (!getInteracaoWorkflowVO().getProspect().getEmailPrincipal().trim().equals("")) {
	        setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspectEmail(getInteracaoWorkflowVO().getProspect().getEmailPrincipal(), getUnidadeEnsinoLogado().getCodigo(), getInteracaoWorkflowVO().getProspect().getCodigo()));
	        setHabilitarConsultaProspect(true);
    	}
    }

	public String getAbrirModalInteracaoProspect() {
		try {
			if (getHabilitarConsultaProspect()) {
				setHabilitarConsultaProspect(false);
				if (getListaConsultaTipoProspect().isEmpty()) {
					return "RichFaces.$('panelTipoProspect').hide();";
				} else if (getListaConsultaTipoProspect().size() == 1) {
					selecionarInteracaoProspectPesquisaCpfCnpj((TipoProspectVO) getListaConsultaTipoProspect().get(0));
					return "RichFaces.$('panelTipoProspect').hide();";
				} else {
					return "RichFaces.$('panelTipoProspect').show();";
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "RichFaces.$('panelTipoProspect').hide();";
	}

    public void selecionarInteracaoProspectPesquisaCpfCnpjSemModal() {
        TipoProspectVO obj = (TipoProspectVO) getRequestMap().get("tipoProspectItens");
        try {
            selecionarInteracaoProspectPesquisaCpfCnpj(obj);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarInteracaoProspectPesquisaCpfCnpj(TipoProspectVO obj) throws Exception {
        try {
			//this.setInteracaoWorkflowVO(null);        	
            if (obj.getPerfisProspect().equals("PESSOA")) {
        		getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(obj.getCpf(), getInteracaoWorkflowVO().getProspect(), getUsuarioLogado()));
        		if (!obj.getEmail().equals("")) {
        			ProspectsVO p = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(obj.getEmail(), false, getUsuarioLogado());
                	getInteracaoWorkflowVO().getProspect().setCodigo(p.getCodigo());
                    if (!getInteracaoWorkflowVO().getProspect().getArquivoFoto().isNovoObj()) {
                        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getInteracaoWorkflowVO().getProspect().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString().toLowerCase(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
                    }
        		} else {
        			ProspectsVO p = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getInteracaoWorkflowVO().getProspect(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                	getInteracaoWorkflowVO().getProspect().setCodigo(p.getCodigo());
                    if (!getInteracaoWorkflowVO().getProspect().getArquivoFoto().isNovoObj()) {
                        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getInteracaoWorkflowVO().getProspect().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString().toLowerCase(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
                    }
        		}
        		return;
            } else if (obj.getPerfisProspect().equals("PARCEIRO")) {
            	if (obj.getCpf().equals("") || getInteracaoWorkflowVO().getProspect().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
            		getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCnpj(obj.getCnpj(), getInteracaoWorkflowVO().getProspect(), getUsuarioLogado()));
            	} else if (obj.getCnpj().equals("") || getInteracaoWorkflowVO().getProspect().getTipoProspect().equals(TipoProspectEnum.FISICO)) {            
            		getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCpf(obj.getCpf(), getInteracaoWorkflowVO().getProspect(), getUsuarioLogado()));
            	}
            } else if (obj.getPerfisProspect().equals("FORNECEDOR")) {
            	if (obj.getCpf().equals("") || getInteracaoWorkflowVO().getProspect().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
            		getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCnpj(obj.getCnpj(), getInteracaoWorkflowVO().getProspect(), getUsuarioLogado()));
            	} else if (obj.getCnpj().equals("") || getInteracaoWorkflowVO().getProspect().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
            		getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCpf(obj.getCpf(), getInteracaoWorkflowVO().getProspect(), getUsuarioLogado()));
            	}
        	} else if (obj.getPerfisProspect().equals("PROSPECT")) {
        		getInteracaoWorkflowVO().getProspect().setCpf(obj.getCpf());
    			getInteracaoWorkflowVO().getProspect().setCnpj(obj.getCnpj());
        		if (!getInteracaoWorkflowVO().getProspect().getJuridico() && getInteracaoWorkflowVO().getProspect().getCpf().equals("")) {
        			getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(obj.getEmail(), false, getUsuarioLogado()));        			
        		} else {
        			getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getInteracaoWorkflowVO().getProspect(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        		}
        		getFacadeFactory().getProspectsFacade().carregarDados(getInteracaoWorkflowVO().getProspect(), getUsuarioLogado());
            } else {
                getInteracaoWorkflowVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorUnidade(obj.getCnpj(), obj.getNome(), getInteracaoWorkflowVO().getProspect(), getUsuarioLogado()));
            }            
            if (getInteracaoWorkflowVO().getProspect().getPessoa().getCodigo().intValue() != 0) {
            	getFacadeFactory().getPessoaFacade().carregarDados(getInteracaoWorkflowVO().getProspect().getPessoa(), getUsuarioLogado());
            }
            if (getInteracaoWorkflowVO().getProspect().getCodigo().intValue() != 0) {
            	getFacadeFactory().getProspectsFacade().carregarDados(getInteracaoWorkflowVO().getProspect(), getUsuarioLogado());
            }            
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarBuscaEndereco() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(getInteracaoWorkflowVO().getProspect(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO
     * <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getInteracaoWorkflowVO().getProspect().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    public void selecionarCidadeFormacao() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeFormacaoItens");
        getFormacaoAcademicaVO().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    public List getListaSelectItemEstadoCivilProspect() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
        Enumeration keys = estadoCivils.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estadoCivils.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de
     * Cidade
     * <code>Cidade/code>.
     */
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public void limparDadosCidade() {
        getInteracaoWorkflowVO().getProspect().setCidade(new CidadeVO());
    }

    public void inicializarDadosMensagem() {
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
    }

    public List<CursoVO> autocompleteCursoInteresse(Object suggest) {
        try {
            return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, 0, 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<CursoVO>();
        }
    }

    public void adicionarFormacaoAcademica() throws Exception {
        try {
            if (!getInteracaoWorkflowVO().getProspect().getCodigo().equals(0)) {
                formacaoAcademicaVO.setProspectsVO(getProspectsVO());
            }
            getFacadeFactory().getProspectsFacade().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO(), getInteracaoWorkflowVO().getProspect());
            this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void editarFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItens");
        setFormacaoAcademicaVO(obj);
    }

    public void removerFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItens");
        getFacadeFactory().getProspectsFacade().excluirObjFormacaoAcademicaVOs(obj.getCurso(), getInteracaoWorkflowVO().getProspect());
        setMensagemID("msg_dados_excluidos");
    }

    public void adicionarCursoInteresse() throws Exception {
        try {
            if (getCursoInteresseVO().getCurso().getCodigo().intValue() != 0) {
                getCursoInteresseVO().setDataCadastro(new Date());
                getFacadeFactory().getProspectsFacade().adicionarObjCursoInteresseVOs(getInteracaoWorkflowVO().getProspect(), getCursoInteresseVO());
                setMensagemDetalhada("", "Curso adicionado com sucesso");
                setAutocompleteValorCurso("");
            } else {
                setMensagemDetalhada("msg_erro", "Curso não encontrado");
            }
            this.setCursoInteresseVO(new CursoInteresseVO());
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void adicionarCursoInteresse2() throws Exception {
        try {
            if (getCursoInteresseVO2().getCurso().getCodigo().intValue() != 0) {
                CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
                getCursoInteresseVO2().setDataCadastro(new Date());
                cursoInteresseVO.getCurso().setNome(getCursoInteresseVO2().getCurso().getNome());
                getFacadeFactory().getProspectsFacade().adicionarObjCursoInteresseVOs(getInteracaoWorkflowVO().getProspect(), getCursoInteresseVO2());
                getInteracaoWorkflowVO().setCurso(getCursoInteresseVO2().getCurso());
                this.setCursoInteresseVO2(new CursoInteresseVO());
                this.getCursoInteresseVO2().getCurso().setNome(cursoInteresseVO.getCurso().getNome());
                setMensagemID("Curso adicionado com sucesso", Uteis.ALERTA, true);
                setAutocompleteValorCurso2("");
            } else {
                setMensagemDetalhada("msg_erro", "Curso não encontrado");
            }
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerCursoInteresse() throws Exception {
        CursoInteresseVO obj = (CursoInteresseVO) context().getExternalContext().getRequestMap().get("cursoInteresseItens");
        if (obj.getCurso().getCodigo().equals(getCursoInteresseVO2().getCurso().getCodigo())) {
            getInteracaoWorkflowVO().setCurso(new CursoVO());
            setCursoInteresseVO2(new CursoInteresseVO());
        }
        getFacadeFactory().getProspectsFacade().excluirObjCursoInteresseVOs(getInteracaoWorkflowVO().getProspect(), obj.getCurso().getCodigo());
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
    }

    public void realizarPersistenciaAdiarCompromisso() {
        try {
            getInteracaoWorkflowVO().setDataTermino(new Date());
            getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraMinutoComMascara(getInteracaoWorkflowVO().getDataTermino()));
            if (!getTempoDecorridoTemporario().equals("00:00:00")) {
                getInteracaoWorkflowVO().setTempoDecorrido(getTempoDecorridoTemporario());
            }
            getInteracaoWorkflowVO().setDataInicio(new Date());
            getInteracaoWorkflowVO().setDataTermino(new Date());
            getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
            getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraAtual());
            adicionarInteracaoWorkflowGravar(getInteracaoWorkflowVO());
            getFacadeFactory().getInteracaoWorkflowFacade().realizarRemarcacaoCompromissoPorInteracaoWorkflow(getListaInteracoesGravar(), getInteracaoWorkflowVO(), getDataCompromissoAdiado(), getHoraCompromissoAdiado(), getHoraFimCompromissoAdiado(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());            
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarAtualizacaoEtapaAtualCompromisso(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo(), getListaInteracoesGravar().get(0).getCompromissoAgendaPessoaHorario().getCodigo(), getUsuarioLogado());
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarAtualizacaoTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO, getListaInteracoesGravar().get(0).getCompromissoAgendaPessoaHorario().getCodigo(), getUsuarioLogado());
            getInteracaoWorkflowVO().getEtapaWorkflow().setPermitirFinalizarDessaEtapa(false);
            getInteracaoWorkflowVO().getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().clear();
            setAdiarCompromisso(true);
            setDataInicioPosterior(null);
            setNovoCompromisso(false);
            setNovoProspect(false);
            setLigacaoAtiva(false);
            for (InteracaoWorkflowVO interacaoGravada : getListaInteracoesGravar()) {
                interacaoGravada.setGravada(Boolean.TRUE);
            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            for (InteracaoWorkflowVO interacaoRollBack : getListaInteracoesGravar()) {
                if (getNovoProspect()) {
                    interacaoRollBack.getProspect().setCodigo(0);
                    if (interacaoRollBack.getProspect().getNome().contains("Ligação Receptiva")) {
                        interacaoRollBack.getProspect().setNome("Prospect Ligação Receptiva ");
                    }
                }
                if (getNovoCompromisso()) {
                    interacaoRollBack.getCompromissoAgendaPessoaHorario().setCodigo(0);
                    interacaoRollBack.getCompromissoAgendaPessoaHorario().setNovoObj(true);
                }

                if (!interacaoRollBack.getGravada()) {
                    interacaoRollBack.setCodigo(0);
                    interacaoRollBack.setNovoObj(Boolean.TRUE);
                }
            }
            setAdiarCompromisso(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarVerificacaoObservacaoPendente() {

        getListaInteracoesObservacaoObrigatoriaNaoPreenchida().clear();
        for (InteracaoWorkflowVO obj : getListaInteracoesGravar()) {
            if (obj.getEtapaWorkflow().getObrigatorioInformarObservacao() && obj.getObservacao().isEmpty() && !obj.getEtapaWorkflow().getCodigo().equals(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo())) {
                setAbrirModalPreencherObservacoes(Boolean.TRUE);
                getListaInteracoesObservacaoObrigatoriaNaoPreenchida().add(obj);
            }
        }
        if (getInteracaoWorkflowVO().getEtapaWorkflow().getObrigatorioInformarObservacao() && getInteracaoWorkflowVO().getObservacao().isEmpty()) {
            setAbrirModalPreencherObservacoes(Boolean.TRUE);
            getListaInteracoesObservacaoObrigatoriaNaoPreenchida().add(getInteracaoWorkflowVO());
        }
        if (getListaInteracoesObservacaoObrigatoriaNaoPreenchida().isEmpty()) {
            setAbrirModalPreencherObservacoes(Boolean.FALSE);
        }


    }

    public void consultarHorariosNaoDisponiveis() {
        try {
            setAgendaPessoaHorarioVO(getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getInteracaoWorkflowVO().getCampanha().getCodigo(), getDataCompromissoAdiado(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado()));
        } catch (Exception e) {
            setAbrirModalMensagemErro(Boolean.TRUE);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getAbrirModalAdiarCompromisso() {
        if (!getAbrirModalPreencherObservacoes()) {
            return "RichFaces.$('panelAdiarCompromisso').show()";
        }
        return "";
    }

    public Date getDataCompromissoAdiado() {
        if (dataCompromissoAdiado == null) {
            dataCompromissoAdiado = Uteis.obterDataAvancada(new Date(), 1);
        }
        return dataCompromissoAdiado;
    }

    public void setDataCompromissoAdiado(Date dataCompromissoAdiado) {
        this.dataCompromissoAdiado = dataCompromissoAdiado;
    }

    public String getHoraCompromissoAdiado() {
        if (horaCompromissoAdiado == null) {
            horaCompromissoAdiado = "";
        }
        return horaCompromissoAdiado;
    }

    public void setHoraCompromissoAdiado(String horaCompromissoAdiado) {
        this.horaCompromissoAdiado = horaCompromissoAdiado;
    }

    public String getHoraFimCompromissoAdiado() {
        if (horaFimCompromissoAdiado == null) {
            horaFimCompromissoAdiado = "";
        }
        return horaFimCompromissoAdiado;
    }

    public void setHoraFimCompromissoAdiado(String horaFimCompromissoAdiado) {
        this.horaFimCompromissoAdiado = horaFimCompromissoAdiado;
    }

    public InteracaoWorkflowVO getInteracaoWorkflowVO() {
        if (interacaoWorkflowVO == null) {
            interacaoWorkflowVO = new InteracaoWorkflowVO();
        }
        return interacaoWorkflowVO;
    }

    public void setInteracaoWorkflowVO(InteracaoWorkflowVO interacaoWorkflowVO) {
        this.interacaoWorkflowVO = interacaoWorkflowVO;
    }

    public ProspectsVO getProspectsVO() {
        if (prospectsVO == null) {
            prospectsVO = new ProspectsVO();
        }
        return prospectsVO;
    }

    public void setProspectsVO(ProspectsVO prospectsVO) {
        this.prospectsVO = prospectsVO;
    }

    public Boolean getHabilitarConsultaProspect() {
        if (habilitarConsultaProspect == null) {
            habilitarConsultaProspect = Boolean.FALSE;
        }
        return habilitarConsultaProspect;
    }

    public void setHabilitarConsultaProspect(Boolean habilitarConsultaProspect) {
        this.habilitarConsultaProspect = habilitarConsultaProspect;
    }

    public List getListaConsultaTipoProspect() {
        if (listaConsultaTipoProspect == null) {
            listaConsultaTipoProspect = new ArrayList(0);
        }
        return listaConsultaTipoProspect;
    }

    public void setListaConsultaTipoProspect(List listaConsultaTipoProspect) {
        this.listaConsultaTipoProspect = listaConsultaTipoProspect;
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

    public String getRelogio() throws Exception {
        Calendar dataInicial = Calendar.getInstance();
        if (getInteracaoWorkflowVO().getDataInicio() != null && getInteracaoWorkflowVO().getHoraTermino() != null) {
            int segundosPassados = Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(6, 8))
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(3, 5)) * 60
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(0, 2)) * 3600;
            if (segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo()) {
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.FALSE);
                setAmarelo(Boolean.TRUE);
            } else if (segundosPassados >= getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo() && segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo()) {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.TRUE);
            } else {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.TRUE);
                setAzul(Boolean.FALSE);
            }
            return getInteracaoWorkflowVO().getTempoDecorrido();
        } else if (getDataInicioPosterior() != null) {
            int segundosAtrasar = Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(6, 8))
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(3, 5)) * 60
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(0, 2)) * 3600;
            if (cont == 0) {
                setDataInicioPosterior(Uteis.getDataFutura(getDataInicioPosterior(), Calendar.SECOND, -segundosAtrasar));
                cont++;
            }
            dataInicial.setTime(getDataInicioPosterior());
            String tempoDecorrido = Uteis.getObterTempoEntreDuasData(new Date(), dataInicial.getTime());
            getInteracaoWorkflowVO().setTempoDecorrido(tempoDecorrido);
            int segundosPassados = Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(6, 8))
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(3, 5)) * 60
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(0, 2)) * 3600;
            if (segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo()) {
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.FALSE);
                setAmarelo(Boolean.TRUE);
            } else if (segundosPassados >= getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo() && segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo()) {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.TRUE);
                if (getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo() - segundosPassados <= getInteracaoWorkflowVO().getWorkflow().getNumeroSegundosAlertarUsuarioTempoMaximoInteracao()) {
                    if ((getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo() - segundosPassados) % 2 == 0) {
                        setAzul(Boolean.FALSE);
                    } else {
                        setAzul(Boolean.TRUE);
                    }
                }
            } else {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.TRUE);
                setAzul(Boolean.FALSE);
            }
            return getInteracaoWorkflowVO().getTempoDecorrido();
        } else if (getInteracaoWorkflowVO().getDataInicio() != null && getInteracaoWorkflowVO().getDataTermino() == null) {
            dataInicial.setTime(getInteracaoWorkflowVO().getDataInicio());
            String tempoDecorrido = Uteis.getObterTempoEntreDuasData(new Date(), dataInicial.getTime());
            getInteracaoWorkflowVO().setTempoDecorrido(tempoDecorrido);
            int segundosPassados = Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(6, 8))
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(3, 5)) * 60
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(0, 2)) * 3600;
            if (segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo()) {
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.FALSE);
                setAmarelo(Boolean.TRUE);
            } else if (segundosPassados >= getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo() && segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo()) {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.TRUE);
                if (getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo() - segundosPassados <= getInteracaoWorkflowVO().getWorkflow().getNumeroSegundosAlertarUsuarioTempoMaximoInteracao()) {
                    if ((getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo() - segundosPassados) % 2 == 0) {
                        setAzul(Boolean.FALSE);
                    } else {
                        setAzul(Boolean.TRUE);
                    }
                }
            } else {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.TRUE);
                setAzul(Boolean.FALSE);
            }
            return getInteracaoWorkflowVO().getTempoDecorrido();
        } else if (getInteracaoWorkflowVO().getTempoDecorrido() != null) {
            int segundosPassados = Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(6, 8))
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(3, 5)) * 60
                    + Integer.parseInt(getInteracaoWorkflowVO().getTempoDecorrido().substring(0, 2)) * 3600;
            if (segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo()) {
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.FALSE);
                setAmarelo(Boolean.TRUE);
            } else if (segundosPassados >= getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMinimo() && segundosPassados < getInteracaoWorkflowVO().getEtapaWorkflow().getTempoMaximo()) {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.FALSE);
                setAzul(Boolean.TRUE);
            } else {
                setAmarelo(Boolean.FALSE);
                setVermelho(Boolean.TRUE);
                setAzul(Boolean.FALSE);
            }
            return getInteracaoWorkflowVO().getTempoDecorrido();
        } else {
            return "00:00:00";
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemTipoMidia() {
        try {
            montarListaSelectItemTipoMidia("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemMotivoInsucesso() {
        try {
            montarListaSelectItemMotivoInsucesso("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (resultadoConsulta.size() > 1) {
                objs.add(new SelectItem(0, ""));
            }
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

    public void montarListaSelectItemTurno() throws Exception {
        montarListaSelectItemTurno(getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), getInteracaoWorkflowVO().getCurso().getCodigo());
    }

    public void montarListaSelectItemTurno(Integer unidadeEnsino, Integer curso) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCurso(unidadeEnsino, curso, Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (resultadoConsulta.size() > 1) {
                objs.add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                TurnoVO obj = (TurnoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemTurno(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCursoInteresse() throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getInteracaoWorkflowVO().getProspect().getCursoInteresseVOs();
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (resultadoConsulta.isEmpty()) {
                objs.add(new SelectItem(0, ""));
            } else {
                while (i.hasNext()) {
                    CursoInteresseVO obj = (CursoInteresseVO) i.next();
                    objs.add(new SelectItem(obj.getCurso().getCodigo(), obj.getCurso().getNome()));
                }
            }
            setListaSelectItemCursoInteresse(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            //Uteis.liberarListaMemoria(resultadoConsulta);
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

    public void montarListaSelectItemMotivoInsucesso(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarMotivoInsucessoPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            while (i.hasNext()) {
                MotivoInsucessoVO obj = (MotivoInsucessoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            setListaSelectItemMotivoInsucesso(objs);
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

    public List consultarTipoMidiaPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List consultarMotivoInsucessoPorDescricao(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getMotivoInsucessoFacade().consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void inicializarContagemTempoProspeccao() {
        if (!getProspeccaoIniciada()) {
            if (getInteracaoWorkflowVO().getDataInicio() == null) {
                getInteracaoWorkflowVO().setDataInicio(new Date());
                setDataInicioPosterior(null);
            } else {
                setDataInicioPosterior(new Date());
            }
            getInteracaoWorkflowVO().setDataTermino(null);
            getInteracaoWorkflowVO().setHoraTermino(null);
            getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraMinutoComMascara(getInteracaoWorkflowVO().getDataInicio()));
            setProspeccaoIniciada(Boolean.TRUE);
            setMensagem("");
            setMensagemID("", Uteis.SUCESSO);
        }
    }

    public void iniciarNegociacaoAluno() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute("codigoProspect", getInteracaoWorkflowVO().getProspect().getCodigo().intValue());
            context().getExternalContext().getSessionMap().put("matricula", getMatriculaVO().getMatricula());
//            getFacadeFactory().getProspectsFacade().alterarSemValidarDados(getInteracaoWorkflowVO().getProspect(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
//            getInteracaoWorkflowVO().setNovoObj(Boolean.FALSE);
//            getFacadeFactory().getInteracaoWorkflowFacade().persistir(getInteracaoWorkflowVO());
            setPopUpMatricula("RichFaces.$('panelIniciarMatricula').hide(); abrirPopup('visaoAdministrativo/financeiro/negociacaoContaReceberForm.xhtml?interacaoWorkflow=" + getInteracaoWorkflowVO().getCodigo() + "','NegociacaoContaReceber', 950, 595);");
            setMensagemDetalhada("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            //setAbrirModalMensagemErro(Boolean.TRUE);
            //setPopUpMatricula("Richfaces.showModalPanel('panelIniciarMatricula');");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
   
    public void iniciarSerasaAluno() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute("codigoProspect", getInteracaoWorkflowVO().getProspect().getCodigo().intValue());
            context().getExternalContext().getSessionMap().put("matricula", getMatriculaVO().getMatricula());
//            getFacadeFactory().getProspectsFacade().alterarSemValidarDados(getInteracaoWorkflowVO().getProspect(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
//            getInteracaoWorkflowVO().setNovoObj(Boolean.FALSE);
//            getFacadeFactory().getInteracaoWorkflowFacade().persistir(getInteracaoWorkflowVO());
            setPopUpMatricula("RichFaces.$('panelIniciarMatricula').hide(); abrirPopup('visaoAdministrativo/financeiro/matriculaSerasaForm.xhtml?interacaoWorkflow=" + getInteracaoWorkflowVO().getCodigo() + "','MatriculaSerasa', 950, 595);");
            setMensagemDetalhada("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            //setAbrirModalMensagemErro(Boolean.TRUE);
            //setPopUpMatricula("Richfaces.showModalPanel('panelIniciarMatricula');");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void iniciarMatriculaAluno() {
        try {
            if (getInteracaoWorkflowVO().getProspect().getNome().equals("")) {
                throw new Exception("O campo NOME (prospect) deve ser informado.");
            }
            if (getInteracaoWorkflowVO().getProspect().getCpf().equals("")) {
                throw new Exception("O campo CPF (prospect) deve ser informado.");
            }
            if (getInteracaoWorkflowVO().getProspect().getDataNascimento() == null) {
                throw new Exception("O campo Data Nascimento (prospect) deve ser informado.");
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute("matricula", Boolean.TRUE);
            if (getInteracaoWorkflowVO().getProspect().getConsultorPadrao().getPessoa().getCodigo() != getUsuarioLogado().getPessoa().getCodigo()) {
                FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuario());
                getInteracaoWorkflowVO().getProspect().setConsultorPadrao(funcionarioVO);            	
            }
            getFacadeFactory().getProspectsFacade().alterarSemValidarDados(getInteracaoWorkflowVO().getProspect(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
            getInteracaoWorkflowVO().setNovoObj(Boolean.FALSE);
            getFacadeFactory().getInteracaoWorkflowFacade().persistir(getInteracaoWorkflowVO(), getUsuarioLogado());
            setPopUpMatricula("abrirPopup('visaoAdministrativo/academico/alunoForm.xhtml?interacaoWorkflow=" + getInteracaoWorkflowVO().getCodigo() + "','Aluno"+ getInteracaoWorkflowVO().getCodigo()+"', 950, 595); window.close();");
            setMensagemDetalhada("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            //setAbrirModalMensagemErro(Boolean.TRUE);
            setPopUpMatricula("RichFaces.$('panelIniciarMatricula').show();");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void prepararDadosIniciarMatricula() {
        try {
            getInteracaoWorkflowVO().getUnidadeEnsino().setCodigo(getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().getCodigo());
            montarListaSelectItemCursoInteresse();
            montarListaSelectItemTurno(getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), getInteracaoWorkflowVO().getCurso().getCodigo());
            setPopUpMatricula("RichFaces.$('panelIniciarMatricula').show()");
            setBotaoIniciar(Boolean.FALSE);
            setIniciarMatricula(Boolean.TRUE);
        } catch (Exception e) {
            //setAbrirModalMensagemErro(Boolean.TRUE);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void validarDadosMatricularAluno() {
        try {
//            if (getInteracaoWorkflowVO().getProspect().getNome().equals("")) {
//                setPopUpMatricula("");
//                throw new Exception("O campo NOME (prospect) deve ser informado.");
//            }
//            if (getInteracaoWorkflowVO().getProspect().getCpf().equals("")) {
//                setPopUpMatricula("");
//                throw new Exception("O campo CPF (prospect) deve ser informado.");
//            }
            if (getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo() != 0 && getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().getCodigo() == 0) {
                getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().setCodigo(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo());
            }
            if (verificarComromissoFuturo()) {
                setPopUpMatricula("RichFaces.$('panelCompromissoFuturo').show()");
                setBotaoIniciar(Boolean.TRUE);
                setIniciarMatricula(Boolean.FALSE);
            } else {
                prepararDadosIniciarMatricula();
//                getInteracaoWorkflowVO().getUnidadeEnsino().setCodigo(getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().getCodigo());
//                montarListaSelectItemCursoInteresse();
//                montarListaSelectItemTurno(getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), getInteracaoWorkflowVO().getCurso().getCodigo());
//                setPopUpMatricula("Richfaces.showModalPanel('panelIniciarMatricula')");
//                setBotaoIniciar(Boolean.FALSE);
//                setIniciarMatricula(Boolean.TRUE);
            }
        } catch (Exception e) {
            //setAbrirModalMensagemErro(Boolean.TRUE);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerComromissoFuturo() throws Exception {
        CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
        getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluir(obj, getUsuarioLogado());
        getListaCompromissoFuturo().remove(obj);
    }

    public boolean verificarComromissoFuturo() throws Exception {
        if (getBotaoIniciar()) {
            return false;
        }
        setListaCompromissoFuturo(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoFuturoProspect(getInteracaoWorkflowVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        if (getListaCompromissoFuturo().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void adicionarInteracaoWorkflowGravar(InteracaoWorkflowVO obj) {
        int index = 0;
        for (InteracaoWorkflowVO objExistente : getListaInteracoesGravar()) {
            if (objExistente.getEtapaWorkflow().getCodigo().equals(obj.getEtapaWorkflow().getCodigo())) {
                getListaInteracoesGravar().set(index, obj);
                return;
            }
            index++;
        }        
        getListaInteracoesGravar().add(obj);
    }

    public Boolean getPosssibilidadeVoltarEtapa() {
        if (!getInteracaoWorkflowVO().getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
            return true;
        } else {
            return false;
        }
    }

    public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);
        return objs;
    }

    public void montarListaSelectItemAreaConhecimento() {
        try {
            montarListaSelectItemAreaConhecimento("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemAreaConhecimento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemNacionalidade() {
        try {
            montarListaSelectItemNacionalidade("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemNacionalidade(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPaizPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PaizVO obj = (PaizVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNacionalidade()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemNacionalidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }

    }

    public List consultarPaizPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
        Enumeration keys = situacaoFormacaoAcademicas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoFormacaoAcademicas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemTipoInstFormacaoAcademica() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoInstFormacaoAcademicas = (Hashtable) Dominios.getTipoInstFormacaoAcademica();
        Enumeration keys = tipoInstFormacaoAcademicas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoInstFormacaoAcademicas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }

    public Boolean getProspeccaoIniciada() {
        if (prospeccaoIniciada == null) {
            prospeccaoIniciada = Boolean.FALSE;
        }
        return prospeccaoIniciada;
    }

    public void setProspeccaoIniciada(Boolean prospeccaoIniciada) {
        this.prospeccaoIniciada = prospeccaoIniciada;
    }

    public Integer getTamanhoColunaEtapasAntecendentes() {
        if (getInteracaoWorkflowVO().getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().isEmpty()) {
            return 0;
        }
        return getInteracaoWorkflowVO().getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().size();
    }

    public List getListaSelectItemMotivoInsucesso() {
        if (listaSelectItemMotivoInsucesso == null) {
            listaSelectItemMotivoInsucesso = new ArrayList(0);
        }
        return listaSelectItemMotivoInsucesso;
    }

    public void setListaSelectItemMotivoInsucesso(List listaSelectItemMotivoInsucesso) {
        this.listaSelectItemMotivoInsucesso = listaSelectItemMotivoInsucesso;
    }

    public UsuarioVO getUsuarioAdministrador() {
        if (usuarioAdministrador == null) {
            usuarioAdministrador = new UsuarioVO();
        }
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(UsuarioVO usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }

    public Date getDataInicioPosterior() {
        return dataInicioPosterior;
    }

    public void setDataInicioPosterior(Date dataInicioPosterior) {
        this.dataInicioPosterior = dataInicioPosterior;
    }

    public String getTempoDecorridoTemporario() {
        if (tempoDecorridoTemporario == null) {
            tempoDecorridoTemporario = "00:00:00";
        }
        return tempoDecorridoTemporario;
    }

    public void setTempoDecorridoTemporario(String tempoDecorridoTemporario) {
        this.tempoDecorridoTemporario = tempoDecorridoTemporario;
    }

    public void consultarCurso() {
        try {
            getListaConsultaCurso().clear();
            if (getCampoConsultaCurso().equals("nome")) {
                setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarNaturalidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaNaturalidade().equals("codigo")) {
                if (getValorConsultaNaturalidade().equals("")) {
                    setValorConsultaNaturalidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaNaturalidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaNaturalidade().equals("nome")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaNaturalidade(), false, getUsuarioLogado());
            }

            setListaConsultaNaturalidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaNaturalidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getInteracaoWorkflowVO().setCurso(obj);
            listaConsultaCurso.clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            setMensagemID("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void selecionarNaturalidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidadeItens");
        getInteracaoWorkflowVO().getProspect().setNaturalidade(obj);
        getListaConsultaNaturalidade().clear();
        this.setValorConsultaNaturalidade("");
        this.setCampoConsultaNaturalidade("");
    }

    public List getTipoConsultaNaturalidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void limparCampoCurso() {
        getInteracaoWorkflowVO().setCurso(null);
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

    public String getCss_Relogio() {
        if (getAzul()) {
            return "background-color:#7ebaf7 !important;color:white !important";
        } else if (getVermelho()) {
            return "background-color:#f74a4a;color:white !important";
        } else if (getAmarelo()) {
            return "background-color:#f5f76b";
        } else {
            return "";
        }
    }

    public Boolean getAzul() {
        if (azul == null) {
            azul = Boolean.FALSE;
        }
        return azul;
    }

    public void setAzul(Boolean azul) {
        this.azul = azul;
    }

    public Boolean getVermelho() {
        if (vermelho == null) {
            vermelho = Boolean.FALSE;
        }
        return vermelho;
    }

    public void setVermelho(Boolean vermelho) {
        this.vermelho = vermelho;
    }

    public Boolean getAmarelo() {
        if (amarelo == null) {
            amarelo = Boolean.FALSE;
        }
        return amarelo;
    }

    public void setAmarelo(Boolean amarelo) {
        this.amarelo = amarelo;
    }

    public List<CampanhaVO> getListaCampanha() {
        if (listaCampanha == null) {
            listaCampanha = new ArrayList<CampanhaVO>();
        }
        return listaCampanha;
    }

    public void setListaCampanha(List<CampanhaVO> listaCampanha) {
        this.listaCampanha = listaCampanha;
    }

    public Boolean getApresentarRichModalCampanha() {
        if (apresentarRichModalCampanha == null) {
            apresentarRichModalCampanha = false;
        }
        return apresentarRichModalCampanha;
    }

    public void setApresentarRichModalCampanha(Boolean apresentarRichModalCampanha) {
        this.apresentarRichModalCampanha = apresentarRichModalCampanha;
    }

    public String getCampoConsultaCidade() {
        if (campoConsultaCidade == null) {
            campoConsultaCidade = "";
        }
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    public List getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
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

    public Boolean getNovoCompromisso() {
        if (novoCompromisso == null) {
            novoCompromisso = false;
        }
        return novoCompromisso;
    }

    public void setNovoCompromisso(Boolean novoCompromisso) {
        this.novoCompromisso = novoCompromisso;
    }

    public Boolean getNovoProspect() {
        if (novoProspect == null) {
            novoProspect = false;
        }
        return novoProspect;
    }

    public void setNovoProspect(Boolean novoProspect) {
        this.novoProspect = novoProspect;
    }

    public Boolean getAdiarCompromisso() {
        if (adiarCompromisso == null) {
            adiarCompromisso = false;
        }
        return adiarCompromisso;
    }

    public void setAdiarCompromisso(Boolean adiarCompromisso) {
        this.adiarCompromisso = adiarCompromisso;
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

    public String getAbrirModalPermissao() throws Exception {
        if (getLoginControle().getPermissaoAcessoMenuVO().getVoltarEtapaAnterior()) {
            return "RichFaces.$('panelVoltarEtapa').show()";
        }
        return "RichFaces.$('panelPermissaoVoltarEtapa').show()";
    }

    public void pesquisarEtapasAnteriores() throws Exception {

        getEtapasAnteriores().clear();
        for (InteracaoWorkflowVO obj : getListaInteracoesGravar()) {
            if (obj.getEtapaWorkflow().getNivelApresentacao() <= getInteracaoWorkflowVO().getEtapaWorkflow().getNivelApresentacao()
                    && !obj.getEtapaWorkflow().getCodigo().equals(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo())) {
                adicionarEtapasAnterioresVoltarEtapa(getEtapasAnteriores(), obj);
            }
        }
        for (InteracaoWorkflowVO obj : getListaInteracoesPercorridas()) {
            if (obj.getEtapaWorkflow().getNivelApresentacao() <= getInteracaoWorkflowVO().getEtapaWorkflow().getNivelApresentacao()
                    && !obj.getEtapaWorkflow().getCodigo().equals(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo())) {
                adicionarEtapasAnterioresVoltarEtapa(getEtapasAnteriores(), obj);
            }
        }
        for (InteracaoWorkflowVO obj : getListaTodasEtapasWorkflow()) {
            if (obj.getEtapaWorkflow().getNivelApresentacao() <= getInteracaoWorkflowVO().getEtapaWorkflow().getNivelApresentacao()
                    && !obj.getEtapaWorkflow().getCodigo().equals(getInteracaoWorkflowVO().getEtapaWorkflow().getCodigo())) {
                adicionarEtapasAnterioresVoltarEtapa(getEtapasAnteriores(), obj);
            }
        }

    }

    public void adicionarEtapasAnterioresVoltarEtapa(List<InteracaoWorkflowVO> lista, InteracaoWorkflowVO obj) {
        for (InteracaoWorkflowVO objExistente : lista) {
            if (obj.getEtapaWorkflow().getCodigo().equals(objExistente.getEtapaWorkflow().getCodigo())) {
                return;
            }
        }
        getEtapasAnteriores().add(obj);
    }

    public void adicionarObservacoesAnteriores(List<InteracaoWorkflowVO> lista) {
    	
    	
        for (InteracaoWorkflowVO objExistente : lista) {
//            if ((objExistente.getEtapaWorkflow().getNivelApresentacao() >= getInteracaoWorkflowVO().getEtapaWorkflow().getNivelApresentacao() || objExistente.getObservacao().trim().equals("")) && getHashMapObservacoesAnteriores().containsKey(objExistente.getEtapaWorkflow().getCodigo())) {
//                getHashMapObservacoesAnteriores().remove(objExistente.getEtapaWorkflow().getCodigo());
//            } else if (objExistente.getEtapaWorkflow().getNivelApresentacao() < getInteracaoWorkflowVO().getEtapaWorkflow().getNivelApresentacao() && !objExistente.getObservacao().trim().equals("")) {
//                if (!getHashMapObservacoesAnteriores().containsKey(objExistente.getEtapaWorkflow().getCodigo())) {
//                    getHashMapObservacoesAnteriores().put(objExistente.getEtapaWorkflow().getCodigo(), objExistente.getObservacao());
//                    getObservacoesEtapasAnteriores().add(objExistente.getEtapaWorkflow().getNome() + ":  " + objExistente.getObservacao());
//                } else {
//                    if (getObservacoesEtapasAnteriores().isEmpty()) {
//                        getObservacoesEtapasAnteriores().add(objExistente.getEtapaWorkflow().getNome() + ":  " + objExistente.getObservacao());
//                    } else {
//                        for (int j = 0; j < getObservacoesEtapasAnteriores().size(); j++) {
//                            String observacao = getObservacoesEtapasAnteriores().get(j);
//                            if (observacao.contains(objExistente.getEtapaWorkflow().getNome())) {
//                                getObservacoesEtapasAnteriores().set(j, objExistente.getEtapaWorkflow().getNome() + ":  " + objExistente.getObservacao());
//                                break;
//                            }
//                            if (j == getObservacoesEtapasAnteriores().size() - 1) {
//                                getObservacoesEtapasAnteriores().add(objExistente.getEtapaWorkflow().getNome() + ":  " + objExistente.getObservacao());
//                            }
//                        }
//                    }
//                }
//            } else if(objExistente.getEtapaWorkflow().getNivelApresentacao() == getInteracaoWorkflowVO().getEtapaWorkflow().getNivelApresentacao()  && !objExistente.getObservacao().trim().equals("")){
        		if(!objExistente.getObservacao().trim().equals("")){
        			
        			getObservacoesEtapasAnteriores().add("<div><strong>"+objExistente.getDataHora_Apresentar()+" - "+objExistente.getEtapaWorkflow().getNome() + "</strong></div><div>" + objExistente.getObservacao()+"</div>");
        		}
//            }
        }
        Collections.sort(getObservacoesEtapasAnteriores());
        Collections.reverse(getObservacoesEtapasAnteriores());
    }

    public List getEtapasAnteriores() {
        if (etapasAnteriores == null) {
            etapasAnteriores = new ArrayList(0);
        }
        return etapasAnteriores;
    }

    public void setEtapasAnteriores(List etapasAnteriores) {
        this.etapasAnteriores = etapasAnteriores;
    }

    public Boolean getAbrirModalMensagemErro() {
        if (abrirModalMensagemErro == null) {
            abrirModalMensagemErro = Boolean.FALSE;
        }
        return abrirModalMensagemErro;
    }

    public void setAbrirModalMensagemErro(Boolean abrirModalMensagemErro) {
        this.abrirModalMensagemErro = abrirModalMensagemErro;
    }

    public void fecharModalMensagemErro() {
        setAbrirModalMensagemErro(Boolean.FALSE);
    }

    public String getModalApresentadoPermissaoVoltarEtapa() {
        if (modalApresentadoPermissaoVoltarEtapa == null) {
            modalApresentadoPermissaoVoltarEtapa = "";
        }
        return modalApresentadoPermissaoVoltarEtapa;
    }

    public void setModalApresentadoPermissaoVoltarEtapa(String modalApresentadoPermissaoVoltarEtapa) {
        this.modalApresentadoPermissaoVoltarEtapa = modalApresentadoPermissaoVoltarEtapa;
    }

    public Boolean getScriptExpandido() {
        if (scriptExpandido == null) {
            scriptExpandido = Boolean.FALSE;
        }
        return scriptExpandido;
    }

    public void setScriptExpandido(Boolean scriptExpandido) {
        this.scriptExpandido = scriptExpandido;
    }

    public String getBotaoExpandirMinimizar() {
        if (getScriptExpandido()) {
            return "./resources/imagens/botaoMinimizar.png";

        }
        return "./resources/imagens/botaoExpandir.png";
    }

    public void expandirMinimizar() {
        if (getScriptExpandido()) {
            setScriptExpandido(Boolean.FALSE);
        } else {
            setScriptExpandido(Boolean.TRUE);
        }
        getUsuarioLogado().getPessoa().setOcultarDadosCRM(!getScriptExpandido());
        getFacadeFactory().getPessoaFacade().alterarOcultarDadosCRM(getUsuarioLogado().getPessoa().getCodigo(), !getScriptExpandido());
    }

    public List<String> getObservacoesEtapasAnteriores() {
        if (observacoesEtapasAnteriores == null) {
            observacoesEtapasAnteriores = new ArrayList<String>(0);
        }
        return observacoesEtapasAnteriores;
    }

    public CursoInteresseVO getCursoInteresseVO2() {
        if (cursoInteresseVO2 == null) {
            cursoInteresseVO2 = new CursoInteresseVO();
        }
        return cursoInteresseVO2;
    }

    public void setCursoInteresseVO2(CursoInteresseVO cursoInteresseVO2) {
        this.cursoInteresseVO2 = cursoInteresseVO2;
    }

    public InteracaoWorkflowNivelAplicacaoControle getControladorInteracaoWorkflowNivelAplicacaoControle() {
        return (InteracaoWorkflowNivelAplicacaoControle) UtilNavegacao.getControlador("InteracaoWorkflowNivelAplicacaoControle");
    }

    public List<InteracaoWorkflowVO> getListaInteracoesGravar() {
        if (listaInteracoesGravar == null) {
            listaInteracoesGravar = new ArrayList<InteracaoWorkflowVO>(0);
        }
        return listaInteracoesGravar;
    }

    public void setListaInteracoesGravar(List<InteracaoWorkflowVO> listaInteracoesGravar) {
        this.listaInteracoesGravar = listaInteracoesGravar;
    }

    public List<InteracaoWorkflowVO> getListaInteracoesPercorridas() {
        if (listaInteracoesPercorridas == null) {
            listaInteracoesPercorridas = new ArrayList<InteracaoWorkflowVO>(0);
        }
        return listaInteracoesPercorridas;
    }

    public void setListaInteracoesPercorridas(List<InteracaoWorkflowVO> listaInteracoesPercorridas) {
        this.listaInteracoesPercorridas = listaInteracoesPercorridas;
    }

    public HashMap<Integer, String> getHashMapObservacoesAnteriores() {
        if (hashMapObservacoesAnteriores == null) {
            hashMapObservacoesAnteriores = new HashMap<Integer, String>();
        }
        return hashMapObservacoesAnteriores;
    }

    public void setHashMapObservacoesAnteriores(HashMap<Integer, String> hashMapObservacoesAnteriores) {
        this.hashMapObservacoesAnteriores = hashMapObservacoesAnteriores;
    }

    public String realizarNavegacaoFollowUp() {
        UtilNavegacao.executarMetodoControle("FollowUpControle", "realizarValidacaoProspectExiste", getInteracaoWorkflowVO().getProspect());
        return "followup";
    }

    public Boolean getApresentarBotaoFollowUp() {
        if (!getInteracaoWorkflowVO().getProspect().getCodigo().equals(0)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<InteracaoWorkflowVO> getListaTodasEtapasWorkflow() {
        if (listaTodasEtapasWorkflow == null) {
            listaTodasEtapasWorkflow = new ArrayList<InteracaoWorkflowVO>(0);
        }
        return listaTodasEtapasWorkflow;
    }

    public void setListaTodasEtapasWorkflow(List<InteracaoWorkflowVO> listaTodasEtapasWorkflow) {
        this.listaTodasEtapasWorkflow = listaTodasEtapasWorkflow;
    }

    public InteracaoWorkflowVO getInteracaoTemp() {
        if (interacaoTemp == null) {
            interacaoTemp = new InteracaoWorkflowVO();
        }
        return interacaoTemp;
    }

    public void setInteracaoTemp(InteracaoWorkflowVO interacaoTemp) {
        this.interacaoTemp = interacaoTemp;
    }

    public Boolean getAbrirModalPreencherObservacoes() {
        if (abrirModalPreencherObservacoes == null) {
            abrirModalPreencherObservacoes = Boolean.FALSE;
        }
        return abrirModalPreencherObservacoes;
    }

    public void setAbrirModalPreencherObservacoes(Boolean abrirModalPreencherObservacoes) {
        this.abrirModalPreencherObservacoes = abrirModalPreencherObservacoes;
    }

    public List<InteracaoWorkflowVO> getListaInteracoesObservacaoObrigatoriaNaoPreenchida() {
        if (listaInteracoesObservacaoObrigatoriaNaoPreenchida == null) {
            listaInteracoesObservacaoObrigatoriaNaoPreenchida = new ArrayList<InteracaoWorkflowVO>(0);
        }
        return listaInteracoesObservacaoObrigatoriaNaoPreenchida;
    }

    public void setListaInteracoesObservacaoObrigatoriaNaoPreenchida(List<InteracaoWorkflowVO> listaInteracoesObservacaoObrigatoriaNaoPreenchida) {
        this.listaInteracoesObservacaoObrigatoriaNaoPreenchida = listaInteracoesObservacaoObrigatoriaNaoPreenchida;
    }

    public Boolean getApresentarMatriculaCRM() {
        if (getInteracaoWorkflowVO().getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
            if (!getInteracaoWorkflowVO().getCodigo().equals(0)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public String getPopUpMatricula() {
        if (popUpMatricula == null) {
            popUpMatricula = "";
        }
        return popUpMatricula;
    }

    public void setPopUpMatricula(String popUpMatricula) {
        this.popUpMatricula = popUpMatricula;
    }

    public Boolean getLigacaoAtiva() {
        if (ligacaoAtiva == null) {
            ligacaoAtiva = Boolean.FALSE;
        }
        return ligacaoAtiva;
    }

    public void setLigacaoAtiva(Boolean ligacaoAtiva) {
        this.ligacaoAtiva = ligacaoAtiva;
    }

    public AgendaPessoaHorarioVO getAgendaPessoaHorarioVO() {
        if (agendaPessoaHorarioVO == null) {
            agendaPessoaHorarioVO = new AgendaPessoaHorarioVO();
        }
        return agendaPessoaHorarioVO;
    }

    public void setAgendaPessoaHorarioVO(AgendaPessoaHorarioVO agendaPessoaHorarioVO) {
        this.agendaPessoaHorarioVO = agendaPessoaHorarioVO;
    }

    public List getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList(0);
        }
        return listaSelectItemTurno;
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public void naoIniciarMatricula() {
        setIniciarMatricula(Boolean.FALSE);
        setBotaoIniciar(Boolean.FALSE);
    }

    public Boolean getIniciarMatricula() {
        if (iniciarMatricula == null) {
            iniciarMatricula = Boolean.FALSE;
        }
        return iniciarMatricula;
    }

    public void setIniciarMatricula(Boolean iniciarMatricula) {
        this.iniciarMatricula = iniciarMatricula;
    }

    public List getListaSelectItemCursoInteresse() {
        if (listaSelectItemCursoInteresse == null) {
            listaSelectItemCursoInteresse = new ArrayList(0);
        }
        return listaSelectItemCursoInteresse;
    }

    public void setListaSelectItemCursoInteresse(List listaSelectItemCursoInteresse) {
        this.listaSelectItemCursoInteresse = listaSelectItemCursoInteresse;
    }

    public List<SelectItem> getListaSelectItemMotivoEtapaWorkflow() {
        if (listaSelectItemMotivoEtapaWorkflow == null) {
            listaSelectItemMotivoEtapaWorkflow = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemMotivoEtapaWorkflow;
    }

    public void setListaSelectItemMotivoEtapaWorkflow(List<SelectItem> listaSelectItemMotivoEtapaWorkflow) {
        this.listaSelectItemMotivoEtapaWorkflow = listaSelectItemMotivoEtapaWorkflow;
    }

    /**
     * @return the listaCompromissoFuturo
     */
    public List getListaCompromissoFuturo() {
        if (listaCompromissoFuturo == null) {
            listaCompromissoFuturo = new ArrayList();
        }
        return listaCompromissoFuturo;
    }

    /**
     * @param listaCompromissoFuturo the listaCompromissoFuturo to set
     */
    public void setListaCompromissoFuturo(List listaCompromissoFuturo) {
        this.listaCompromissoFuturo = listaCompromissoFuturo;
    }

    /**
     * @return the botaoIniciar
     */
    public boolean getBotaoIniciar() {
        return botaoIniciar;
    }

    /**
     * @param botaoIniciar the botaoIniciar to set
     */
    public void setBotaoIniciar(boolean botaoIniciar) {
        this.botaoIniciar = botaoIniciar;
    }

    public FormacaoAcademicaVO getFormacaoAcademicaVO() {
        if (formacaoAcademicaVO == null) {
            formacaoAcademicaVO = new FormacaoAcademicaVO();
        }
        return formacaoAcademicaVO;
    }

    public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
        this.formacaoAcademicaVO = formacaoAcademicaVO;
    }

    public List getListaSelectItemAreaConhecimento() {
        if (listaSelectItemAreaConhecimento == null) {
            listaSelectItemAreaConhecimento = new ArrayList(0);
        }
        return listaSelectItemAreaConhecimento;
    }

    public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
        this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
    }

    public List getListaSelectItemNacionalidade() {
        if (listaSelectItemNacionalidade == null) {
            listaSelectItemNacionalidade = new ArrayList(0);
        }
        return listaSelectItemNacionalidade;
    }

    public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
        this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
    }

    public String getCampoConsultaNaturalidade() {
        if (campoConsultaNaturalidade == null) {
            campoConsultaNaturalidade = "";
        }
        return campoConsultaNaturalidade;
    }

    public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
        this.campoConsultaNaturalidade = campoConsultaNaturalidade;
    }

    public String getValorConsultaNaturalidade() {
        if (valorConsultaNaturalidade == null) {
            valorConsultaNaturalidade = "";
        }
        return valorConsultaNaturalidade;
    }

    public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
        this.valorConsultaNaturalidade = valorConsultaNaturalidade;
    }

    public List getListaConsultaNaturalidade() {
        if (listaConsultaNaturalidade == null) {
            listaConsultaNaturalidade = new ArrayList(0);
        }
        return listaConsultaNaturalidade;
    }

    public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
        this.listaConsultaNaturalidade = listaConsultaNaturalidade;
    }

    public void prepararDadosFichaAluno() {
        try {
            getFacadeFactory().getPessoaFacade().carregarDados(this.getInteracaoWorkflowVO().getProspect().getPessoa(), getUsuarioLogado());
            PessoaVO obj = this.getInteracaoWorkflowVO().getProspect().getPessoa();
            if (!obj.getCodigo().equals(0)) {
                setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
                context().getExternalContext().getSessionMap().put("alunoFichaVO", obj);
            }
        } catch (Exception ex) {
            //setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public boolean getApresentarFichaAluno() {
        if (!this.getInteracaoWorkflowVO().getProspect().getPessoa().getCodigo().equals(0)) {
            return true;
        }
        return false;
    }

	public Boolean getCampanhaCobranca() {
		if(campanhaCobranca == null){
			campanhaCobranca = Boolean.FALSE;
		}
		return campanhaCobranca;
	}

	public void setCampanhaCobranca(Boolean campanhaCobranca) {
		this.campanhaCobranca = campanhaCobranca;
	}

	public CampanhaVO getCampanhaVO() {
		if(campanhaVO == null){
			campanhaVO = new CampanhaVO();
		}
		return campanhaVO;
	}

	public void setCampanhaVO(CampanhaVO campanhaVO) {
		this.campanhaVO = campanhaVO;
	}
    
	public void montarCampanhaPorCodigoCompromisso(Integer codigoCompromisso) throws Exception{
		try {
			if(codigoCompromisso!= null && codigoCompromisso !=0){
				setCampanhaVO(getFacadeFactory().getCampanhaFacade().consultarCampanhaPorCodigoCompromisso(codigoCompromisso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				if(getCampanhaVO().getCodigo() != 0){
					setCampanhaCobranca(getCampanhaVO().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA));
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void navegarPaginaUtilizandoMatricula(){ 
		try {
			if (!getInteracaoWorkflowVO().getProspect().getResponsavelFinanceiro()) {
				context().getExternalContext().getSessionMap().put("matriculaItens", getMatriculaVO().getMatricula());
			} else {
				context().getExternalContext().getSessionMap().put("responsavelFinanceiroInteracaoWorkFlow", getInteracaoWorkflowVO().getProspect().getPessoa());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void navegarPaginaUtilizandoMatriculaUnidadeEnsino(){ 
		try {
			if (!getInteracaoWorkflowVO().getProspect().getResponsavelFinanceiro()) {
				context().getExternalContext().getSessionMap().put("matricula", getMatriculaVO().getMatricula());
				context().getExternalContext().getSessionMap().put("unidadeEnsino", getMatriculaVO().getUnidadeEnsino().getCodigo());
			} else {
				context().getExternalContext().getSessionMap().put("responsavelFinanceiroInteracaoWorkFlow", getInteracaoWorkflowVO().getProspect().getPessoa());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null){
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<SelectItem> getListaMatricula() {
		if(listaMatricula == null){
			listaMatricula  = new ArrayList<SelectItem>(0);
		}
		return listaMatricula;
	}

	public void setListaMatricula(List<SelectItem> listaMatricula) {
		this.listaMatricula = listaMatricula;
	}
	
	public void montarListaMatricula(Integer codigoProspect) throws Exception{
		try {
			List<MatriculaVO> listaMatriculaVO = new ArrayList<MatriculaVO>();
			listaMatriculaVO.addAll(getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoProspect(codigoProspect, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			for(MatriculaVO obj : listaMatriculaVO){
				getListaMatricula().add(new SelectItem(obj.getMatricula(), obj.getMatricula() + " - " + (obj.getCurso().getAbreviatura().trim().isEmpty()? obj.getCurso().getNome(): obj.getCurso().getAbreviatura())));
			}
			if(!listaMatriculaVO.isEmpty()){
				setMatriculaVO(listaMatriculaVO.get(0));
				montarDadosMatricula(getMatriculaVO());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void montarDadosMatricula(MatriculaVO obj) throws Exception{
		try {
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), 0, NivelMontarDados.BASICO, getUsuarioLogado()));
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setIdentificadorTurma(matriculaPeriodoVO.getTurma().getIdentificadorTurma());
			setNomeUnidade(getMatriculaVO().getUnidadeEnsino().getNome());			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void montarDadosMatriculaSelectItem() throws Exception{
		try {
			montarDadosMatricula(getMatriculaVO());
		} catch (Exception e) {
			  setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}

	public String getIdentificadorTurma() {
		if(identificadorTurma == null){
			identificadorTurma ="";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public String getNomeUnidade() {
		if(nomeUnidade == null){
			nomeUnidade = "";
		}
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}
	
    public Boolean getApresentarSerasa() {
        if (getInteracaoWorkflowVO().getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO)) {
            if (!getInteracaoWorkflowVO().getCodigo().equals(0)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
	
    public List getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable estados = (Hashtable) Dominios.getEstado();
		Enumeration keys = estados.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estados.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}
    
	public void selecionarCursoPorCodigo() {
		if (getAutocompleteValorCurso() != "") {
			consultarCursoPorCodigo(getValorAutoComplete(getAutocompleteValorCurso()));
		}
		if (getAutocompleteValorCurso2() != "") {
			consultarCursoPorCodigo(getValorAutoComplete(getAutocompleteValorCurso2()));
		}
	}
	public void consultarCursoPorCodigo(int codigo) {
		try {
			List<CursoVO> cursos = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigo(codigo, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getCursoInteresseVO().setCurso(cursos.get(0));
			getCursoInteresseVO2().setCurso(cursos.get(0));
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
	
	public String getAutocompleteValorCurso2() {
		if (autocompleteValorCurso2 == null) {
			autocompleteValorCurso2 = "";
		}
		return autocompleteValorCurso2;
	}

	public void setAutocompleteValorCurso2(String autocompleteValorCurso2) {
		this.autocompleteValorCurso2 = autocompleteValorCurso2;
	}

    public void realizarNavegacaoProspectTelaInscricao() {
		CompromissoAgendaPessoaHorarioVO obj = getInteracaoWorkflowVO().getCompromissoAgendaPessoaHorario();
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("compromissoAgendaPessoaHorarioCandidatoVO", obj);
		}
	}
    
    public void limparCidadeFormacaoAcademica() {
    	getFormacaoAcademicaVO().setCidade(new CidadeVO());
    }
    
	public void setarNomeBatismo() {
		if(!Uteis.isAtributoPreenchido(getInteracaoWorkflowVO().getProspect().getNomeBatismo())) {
			getInteracaoWorkflowVO().getProspect().setNomeBatismo(getInteracaoWorkflowVO().getProspect().getNome());
		}
	}
	
	public void setarNomeSocial() {
		if(!Uteis.isAtributoPreenchido(getInteracaoWorkflowVO().getProspect().getNome())) {
			getInteracaoWorkflowVO().getProspect().setNome(getInteracaoWorkflowVO().getProspect().getNomeBatismo());
		}
	}
	
	public void setarNomeBatismoProspect() {
		if(!Uteis.isAtributoPreenchido(getProspectsVO().getNomeBatismo())) {
			getProspectsVO().setNomeBatismo(getProspectsVO().getNome());
		}
	}
	
	public void setarNomeSocialProspect() {
		if(!Uteis.isAtributoPreenchido(getProspectsVO().getNome())) {
			getProspectsVO().setNome(getProspectsVO().getNomeBatismo());
		}
	}
	
	public Boolean getFiltrarTabPanel() {
		if (filtrarTabPanel == null) {
			filtrarTabPanel = false;
		}
		return filtrarTabPanel;
	}
	
	public void setFiltrarTabPanel(boolean filtrarTabPanel) {
		this.filtrarTabPanel = filtrarTabPanel;
	}
}