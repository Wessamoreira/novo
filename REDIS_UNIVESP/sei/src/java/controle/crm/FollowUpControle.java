package controle.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoCRMEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.BuscaProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.FollowUpVO;
import negocio.comuns.crm.HistoricoFollowUpVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.TipoContatoVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.crm.FollowUp;
import relatorio.controle.crm.InteracaoFollowUpRelControle;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * followUpForm.jsp followUpCons.jsp) com as funcionalidades da classe <code>FollowUp</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see FollowUp
 * @see FollowUpVO
 */
@Controller("FollowUpControle")
@Scope("viewScope")
@Lazy
public class FollowUpControle extends SuperControle {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8255620630040149749L;
	private FollowUpVO followUpVO;
    private HistoricoFollowUpVO historicoFollowUpVO;
    private Boolean apresentarProximoCompromisso;
    private AgendaPessoaVO agendaPessoaVO;
    private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO;
    private Boolean manterRichModalAberto;
    private Boolean abrirRichModalEdicaoProspect;
    private Boolean apresentarBotaoAdiarIniciar;
    private String campoConsultarCampanha;
    private String valorConsultarCampanha;
    private List listaConsultarCampanha;
    private List<SelectItem> listaSelectItemTipoContato;
    private DataModelo controleConsultarInteracaoWorkflow;
    private WorkflowVO workflowVO;
    private CampanhaVO campanhaVO;
    private UsuarioVO usuarioVO;
    private CursoVO cursoVO;
    private List listaSelectItemWorkflow;
    private List listaSelectItemCampanha;
    private List listaSelectItemResponsavel;
    private List listaSelectItemCurso;
    private List listaConsultaFuncionario;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private InteracaoWorkflowVO interacaoWorkflowVO;
    private String abrirFecharModalInteracao;
    private CursoInteresseVO cursoInteresseVO;
    private InteracaoFollowUpRelControle interacaoFollowUpRelControle;
    protected List listaSelectItemDepartamento;
    private List compromissoAgendaPessoaHorarioVOs;
    private String popUpMatricula;
    private Boolean apresentarMatriculaDireta;
    private Boolean apresentarBotaoRelatorioSituacaoFinaceiraAluno;
    private Boolean alunoSerasa;
    private Boolean apresentarFuncionario;
    private FuncionarioVO funcionarioVO; 
    private String autocompleteCursoInteresse;
    private Boolean permitirExcluirHistoricoFollowUp;
    private InteracaoWorkflowVO novaInteracaoWorkflowVO;
    private boolean liberarEdicaoProspectVinculadoPessoaSomenteComSenha=false;
   	private String userNameLiberarFuncionalidade;
   	private String senhaLiberarFuncionalidade;
   	private List<OperacaoFuncionalidadeVO> listaOperacaoFuncionalidadeVO;
   	private Boolean mobile;

    public FollowUpControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        verificarPermissaoApresentarMatriculaDireta();
        verificarPermissaoExcluirHistoricoFollowUp();
        verificarMobile();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }
    
    private void verificarMobile() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ua = request.getHeader("User-Agent").toLowerCase();

        if (ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge|maemo|midp|mmp|netfront|opera m(ob|in)i|palm(os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows(ce|phone)|xda|xiino).*") || ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|awa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r|s)|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp(i|ip)|hs\\-c|ht(c(\\-||_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac(|\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt(|\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg(g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-||o|v)|zz)|mt(50|p1|v)|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v)|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-|)|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
        	mobile = Boolean.TRUE;
        }
	}

    public void verificarPermissaoApresentarMatriculaDireta() {
        Boolean liberar = false;
        try {
        	ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitiMatriculaDiretaFollowUp", getUsuarioLogado());
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setApresentarMatriculaDireta(liberar);
    }
    
    public void verificarPermissaoSelecionarFuncionario() {
    	Boolean liberar = false;
    	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitiSelecionarFuncionarioCompromisso", getUsuarioLogado());
    		liberar = true;
    	} catch (Exception e) {
    		liberar = false;
    	}
    	this.setApresentarFuncionario(liberar);
    }
    
    @PostConstruct
    public void realizarInicializacaoPeloServlet() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            Integer codigoProspect = 0;
            Integer codigoPessoa = 0;
            if (request.getAttribute("codigo") != null) {
                codigoProspect = Integer.parseInt((String) request.getAttribute("codigo"));
            }
            if (request.getAttribute("codigoPessoa") != null) {
                codigoPessoa = Integer.parseInt((String) request.getAttribute("codigoPessoa"));
            }
            if (request.getParameter("codigoPessoa") != null) {
            	codigoPessoa = Integer.parseInt((String) request.getParameter("codigoPessoa"));
            }
            if (codigoProspect != null && codigoProspect != 0) {
                setFollowUpVO(getFacadeFactory().getFollowUpFacade().consultarFollowUpPorCodigoProspect(codigoProspect, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getFollowUpVO().setCompromissoAgendaPessoaHorario(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoProspect(codigoProspect, getUsuarioLogado()));
                if (getFollowUpVO().getProspect().getNome().equals("") || getFollowUpVO().getProspect().getEmailPrincipal().equals("") || (getFollowUpVO().getProspect().getTelefoneResidencial().equals("") && getFollowUpVO().getProspect().getTelefoneComercial().equals("") && getFollowUpVO().getProspect().getTelefoneRecado().equals("") && getFollowUpVO().getProspect().getCelular().equals(""))) {
                    setAbrirRichModalEdicaoProspect(Boolean.TRUE);
                } else {
                    setAbrirRichModalEdicaoProspect(Boolean.FALSE);
                }
                if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getCodigo() == 0) {
                    setApresentarProximoCompromisso(Boolean.FALSE);
                }
                getFollowUpVO().getProspect().getCursoInteresseVOs().addAll(getFacadeFactory().getCursoInteresseFacade().consultarCursosPorCodigoProspect(getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getControleConsultarInteracaoWorkflow().setLimitePorPagina(5);
                getControleConsultarInteracaoWorkflow().getListaConsulta().clear();
                getControleConsultarInteracaoWorkflow().setPaginaAtual(0);
                getControleConsultarInteracaoWorkflow().setListaConsulta(getFacadeFactory().getFollowUpFacade().consultarInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getControleConsultarInteracaoWorkflow().getLimitePorPagina(), getControleConsultarInteracaoWorkflow().getOffset(), getFollowUpVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getControleConsultarInteracaoWorkflow().setTotalRegistrosEncontrados(getFacadeFactory().getFollowUpFacade().consultarTotalDeRegistroInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                Ordenacao.ordenarListaDecrescente(getFollowUpVO().getHistoricoFollowUpVOs(), "dataregistro");
                inicializarListasSelectItemTodosComboBox();
            } else if (codigoPessoa != null && codigoPessoa != 0) {
            	PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigoPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(pessoaVO, false, getUsuarioLogado());
                setFollowUpVO(getFacadeFactory().getFollowUpFacade().consultarFollowUpPorCodigoPessoa(codigoPessoa, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));                
                if (getFollowUpVO().getProspect().getCodigo().intValue() == 0) {
//                	getFollowUpVO().setProspect(getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(codigoPessoa, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                	getFollowUpVO().getProspect().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//                	if (getFollowUpVO().getProspect().getCodigo().intValue() == 0 && !getFollowUpVO().getProspect().getPessoa().getEmail().equals("")) {            			
//                		getFollowUpVO().setProspect(getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(getFollowUpVO().getProspect().getPessoa().getEmail(), false, getUsuarioLogado()));
//                	}                	
//                	if (getFollowUpVO().getProspect().getCodigo().intValue() == 0) {
//                		getFollowUpVO().getProspect().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//                		getFollowUpVO().setProspect(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(getFollowUpVO().getProspect().getPessoa().getCPF(), getFollowUpVO().getProspect(), getUsuarioLogado()));
//                	}
//                    getFacadeFactory().getProspectsFacade().alterarPessoaProspect(getFollowUpVO().getProspect());
                	if (!getLoginControle().getConfiguracaoGeralPadraoSistema().getCriarProspectAluno()) {
                		getFollowUpVO().getProspect().getPessoa().setGerarProspectInativo(Boolean.TRUE);
                	}
                	getFollowUpVO().setProspect(getFacadeFactory().getPessoaFacade().realizarVinculoPessoaProspect(getFollowUpVO().getProspect().getPessoa(), getUsuarioLogado()));                	
                } else {
                    getFollowUpVO().getProspect().setCursoInteresseVOs(getFacadeFactory().getCursoInteresseFacade().consultarCursosPorCodigoProspect(getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                    getFollowUpVO().setCompromissoAgendaPessoaHorario(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoProspect(getFollowUpVO().getProspect().getCodigo(), getUsuarioLogado()));
                }
                if (getFollowUpVO().getProspect().getNome().equals("") || getFollowUpVO().getProspect().getEmailPrincipal().equals("") || (getFollowUpVO().getProspect().getTelefoneResidencial().equals("") && getFollowUpVO().getProspect().getTelefoneComercial().equals("") && getFollowUpVO().getProspect().getTelefoneRecado().equals("") && getFollowUpVO().getProspect().getCelular().equals(""))) {
                    setAbrirRichModalEdicaoProspect(Boolean.TRUE);
                } else {
                    setAbrirRichModalEdicaoProspect(Boolean.FALSE);
                }
                if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getCodigo() == 0) {
                    setApresentarProximoCompromisso(Boolean.FALSE);
                }
                getControleConsultarInteracaoWorkflow().setLimitePorPagina(5);
                getControleConsultarInteracaoWorkflow().getListaConsulta().clear();
                getControleConsultarInteracaoWorkflow().setPaginaAtual(0);
                getControleConsultarInteracaoWorkflow().setListaConsulta(getFacadeFactory().getFollowUpFacade().consultarInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getControleConsultarInteracaoWorkflow().getLimitePorPagina(), getControleConsultarInteracaoWorkflow().getOffset(), getFollowUpVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getControleConsultarInteracaoWorkflow().setTotalRegistrosEncontrados(getFacadeFactory().getFollowUpFacade().consultarTotalDeRegistroInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                Ordenacao.ordenarListaDecrescente(getFollowUpVO().getHistoricoFollowUpVOs(), "dataregistro");
                inicializarListasSelectItemTodosComboBox();
                if (getFollowUpVO().getProspect().getCodigo().intValue() == 0) {
                    getFacadeFactory().getProspectsFacade().incluirSemValidarDados(getFollowUpVO().getProspect(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
                }
            } else {
                throw new Exception("Nenhum Prospect informado.");
            }
            // VERIFICA SE O USUÁRIO EXISTE UMA AGENDA COMPROMISSO VINCULADA DE COBRANÇA
            setApresentarBotaoRelatorioSituacaoFinaceiraAluno(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarExisteCompromissoCampanhaTipoCobranca(getFollowUpVO().getProspect().getCodigo()));
            setAlunoSerasa(getFacadeFactory().getMatriculaFacade().consultarAlunoSerasa(getFollowUpVO().getProspect().getPessoa().getCodigo()));
            
            
            if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
                    || getUsuarioLogado().getPerfilAdministrador()) {
                setApresentarBotaoAdiarIniciar(Boolean.TRUE);
            }
            verificarPermissaoLiberarEdicaoProspectVinculadoPessoaSomenteComSenha();
            setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
   

    public boolean getApresentarLigarSemAgenda() {
        if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().intValue() == getUsuarioLogado().getPessoa().getCodigo().intValue()) {
            return false;
        } else {
            List interacoes = getControleConsultarInteracaoWorkflow().getListaConsulta();
            if (!interacoes.isEmpty()) {
                InteracaoWorkflowVO interacao = (InteracaoWorkflowVO)getControleConsultarInteracaoWorkflow().getListaConsulta().get(0);
                if (interacao.getResponsavel().getPessoa().getCodigo().intValue() == getUsuarioLogado().getPessoa().getCodigo().intValue()) {
                    return false;
                }
            }
            return true;
        }
    }

    public void consultarCompromissoAgendaProspect() {
        try {
            setCompromissoAgendaPessoaHorarioVOs(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarTodosCompromissoPorCodigoProspect(getFollowUpVO().getProspect().getCodigo()));
        } catch (Exception e) {
            setCompromissoAgendaPessoaHorarioVOs(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarPersistenciaProspect() {
        try {
            getFacadeFactory().getProspectsFacade().alterarProspectTelaFollowUp(getFollowUpVO().getProspect(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
            getInteracaoWorkflowVO().setProspect(getFollowUpVO().getProspect());
            if (!getInteracaoWorkflowVO().getObservacao().trim().equals("") && getLoginControle().getPermissaoAcessoMenuVO().getGravarInteracaoFollowUp()) {
                inicializarDadosNovaInteracao();
				if (!getFollowUpVO().getProspect().getCursoInteresseVOs().isEmpty()) {
                	CursoInteresseVO cursoInt = getFollowUpVO().getProspect().getCursoInteresseVOs().get(0);
                	getInteracaoWorkflowVO().setCurso(cursoInt.getCurso());
                }				
                getFacadeFactory().getInteracaoWorkflowFacade().incluirSemValidarDados(getInteracaoWorkflowVO(), getUsuarioLogado());
                setInteracaoWorkflowVO(new InteracaoWorkflowVO());
                realizarPesquisaInteracoesFiltradas();
            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void iniciarMatriculaAluno() {
        try {
        	getFacadeFactory().getProspectsFacade().carregarDados(getFollowUpVO().getProspect(), getUsuarioLogado());
            if (getFollowUpVO().getProspect().getNome().equals("")) {
                throw new Exception("O campo NOME (prospect) deve ser informado.");
            }
            if (getFollowUpVO().getProspect().getDataNascimento() == null) {
                throw new Exception("O campo Data Nascimento (prospect) deve ser informado.");
            }
        	Integer idadeProspect = Uteis.calcularIdadePessoa(new Date(), getFollowUpVO().getProspect().getDataNascimento());
            if (idadeProspect >= 18 && getFollowUpVO().getProspect().getCpf().equals("")) {
                throw new Exception("O campo CPF (prospect) deve ser informado.");
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute("matricula", Boolean.TRUE);
            getFacadeFactory().getProspectsFacade().alterarSemValidarDados(getFollowUpVO().getProspect(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
            removerControleMemoriaFlashTela("AlunoControle");
            setPopUpMatricula("abrirPopup('./visaoAdministrativo/academico/alunoForm.xhtml?prospect=" + getFollowUpVO().getProspect().getCodigo() + "','Aluno', 950, 595);");
            //setMensagemDetalhada("msg_acao_realizadaComSucesso");
        } catch (Exception e) {
            //setAbrirModalMensagemErro(Boolean.TRUE);
            setPopUpMatricula("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

//    public void realizarValidacaoProspectExiste(ProspectsVO prospect) {
//        try {
//            if (prospect.getCodigo() != 0) {
//                setFollowUpVO(getFacadeFactory().getFollowUpFacade().consultarFollowUpPorCodigoProspect(prospect.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//                getFollowUpVO().setCompromissoAgendaPessoaHorario(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoProspect(prospect.getCodigo()));
//            } else {
//                getFollowUpVO().setProspect(prospect);
//            }
//            if (!getFacadeFactory().getProspectsFacade().isValido(getFollowUpVO().getProspect())) {
//                setAbrirRichModalEdicaoProspect(true);
//            }
//            if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getCodigo() == 0) {
//                setApresentarProximoCompromisso(Boolean.FALSE);
//            }
//            getControleConsultarInteracaoWorkflow().setLimitePorPagina(10);
//            getControleConsultarInteracaoWorkflow().getListaConsulta().clear();
//            getControleConsultarInteracaoWorkflow().setPaginaAtual(1);
//            getControleConsultarInteracaoWorkflow().setListaConsulta(getFacadeFactory().getFollowUpFacade().consultarInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getControleConsultarInteracaoWorkflow().getLimitePorPagina(), getControleConsultarInteracaoWorkflow().getOffset(), getFollowUpVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//            getControleConsultarInteracaoWorkflow().setTotalRegistrosEncontrados(getFacadeFactory().getFollowUpFacade().consultarTotalDeRegistroInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getFollowUpVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//            inicializarListasSelectItemTodosComboBox();
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//        }
//    }
    public String realizarNavegacaoProspects() {
        //UtilNavegacao.executarMetodoControle("ProspectsControle", "editarFollowUpDadosIncompletos", getFollowUpVO().getProspect());
        return ((ProspectsControle) getControlador("ProspectsControle")).editarFollowUpDadosIncompletos(getFollowUpVO().getProspect());
    }

    public void gravarNovaInteracao() {
        try {
            Integer codigoPessoa = 0;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if (request.getAttribute("codigoPessoa") != null) {
                codigoPessoa = Integer.parseInt((String) request.getAttribute("codigoPessoa"));
            }
            getInteracaoWorkflowVO().setProspect(getFollowUpVO().getProspect());
            getFacadeFactory().getInteracaoWorkflowFacade().incluirSemValidarDados(getInteracaoWorkflowVO(), getUsuarioLogado());
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gravarCompromissoRealizadoComEtapa(getInteracaoWorkflowVO().getCompromissoAgendaPessoaHorario().getCodigo(), 0, getUsuarioLogado());
            getFollowUpVO().setCompromissoAgendaPessoaHorario(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoProspect(codigoPessoa, getUsuarioLogado()));
            if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getCodigo() == 0) {
                setApresentarProximoCompromisso(Boolean.FALSE);
            } else {
                setApresentarProximoCompromisso(Boolean.TRUE);
            }
            setInteracaoWorkflowVO(new InteracaoWorkflowVO());
            realizarPesquisaInteracoesFiltradas();
            setAbrirFecharModalInteracao("RichFaces.$('panelNovaInteracao').hide()");
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setAbrirFecharModalInteracao("RichFaces.$('panelNovaInteracao').show()");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String realizarPesquisaInteracoesFiltradas() {
        try {
            getControleConsultarInteracaoWorkflow().getListaConsulta().clear();
            getControleConsultarInteracaoWorkflow().setListaConsulta(getFacadeFactory().getFollowUpFacade().consultarInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getControleConsultarInteracaoWorkflow().getLimitePorPagina(), getControleConsultarInteracaoWorkflow().getOffset(), getFollowUpVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            getControleConsultarInteracaoWorkflow().setTotalRegistrosEncontrados(getFacadeFactory().getFollowUpFacade().consultarTotalDeRegistroInteracoes(getWorkflowVO().getCodigo(), getCampanhaVO().getCodigo(), getUsuarioVO().getCodigo(), getCursoVO().getCodigo(), getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return "";
    }

    public String getObterCaminhoFotoUsuario() throws Exception {
        return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getFollowUpVO().getProspect().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), null, "foto_usuario.png", false);
    }

    /* Método responsável por adicionar um novo objeto da classe <code>HistoricoFollowUp</code>
     * para o objeto <code>followUpVO</code> da classe <code>FollowUp</code>
     */
    public void adicionarHistoricoFollowUp() {
        try {
            if (getHistoricoFollowUpVO().getDepartamento().getCodigo() > 0) {
                getHistoricoFollowUpVO().setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(getHistoricoFollowUpVO().getDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            getFacadeFactory().getHistoricoFollowUpFacade().incluir(getHistoricoFollowUpVO(), getUsuarioLogado());
            getFacadeFactory().getFollowUpFacade().adicionarObjHistoricoFollowUpVOs(getFollowUpVO(), getHistoricoFollowUpVO());
            Ordenacao.ordenarListaDecrescente(getFollowUpVO().getHistoricoFollowUpVOs(), "dataregistro");
            this.setHistoricoFollowUpVO(new HistoricoFollowUpVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarPreenchimentoDataResponsavelHistoricoFollowUp() {
        try {
        	setHistoricoFollowUpVO(new HistoricoFollowUpVO());
            getHistoricoFollowUpVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
            getHistoricoFollowUpVO().getResponsavel().setNome(getUsuarioLogado().getNome());
            getHistoricoFollowUpVO().setDataregistro(new Date());
            getHistoricoFollowUpVO().setProspect((ProspectsVO)getFollowUpVO().getProspect().clone());
            if(Uteis.isAtributoPreenchido(getListaSelectItemTipoContato())) {
            	getHistoricoFollowUpVO().getTipoContato().setCodigo((Integer)getListaSelectItemTipoContato().get(0).getValue());
            }
            setMensagemID("msg_entre_dados", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void imprimirRelatorioInteracoes() {
        List<BuscaProspectVO> listaObjs = new ArrayList<BuscaProspectVO>(0);
        BuscaProspectVO obj = new BuscaProspectVO();
        obj.getProspectNovaInteracao().setCodigo(getFollowUpVO().getProspect().getCodigo());
        listaObjs.add(obj);
        getInteracaoFollowUpRelControle().setBuscaProspectVOs(listaObjs);
        setMensagemID(getInteracaoFollowUpRelControle().imprimirPDF());
    }

    public void alterarObservacaoInteracao() {
        try {
            getFacadeFactory().getInteracaoWorkflowFacade().alterarObservacao(getNovaInteracaoWorkflowVO(), getUsuarioLogado());
            realizarPesquisaInteracoesFiltradas();
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void apresentarTodosHistoricos() {
        try {
            getFollowUpVO().setHistoricoFollowUpVOs(getFacadeFactory().getHistoricoFollowUpFacade().consultarTodosHistoricosFollowUps(getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>HistoricoFollowUp</code>
     * para edição pelo usuário.
     */
    public void editarHistoricoFollowUp() {
        HistoricoFollowUpVO obj = (HistoricoFollowUpVO) context().getExternalContext().getRequestMap().get("historicoFollowUpItem");
        setHistoricoFollowUpVO(obj);
    }

    /* Método responsável por remover um novo objeto da classe <code>HistoricoFollowUp</code>
     * do objeto <code>followUpVO</code> da classe <code>FollowUp</code>
     */
    public void removerHistoricoFollowUp() {
        try {        
        	getFacadeFactory().getFollowUpFacade().excluirObjHistoricoFollowUpVOs(getFollowUpVO(), getHistoricoFollowUpVO(), getUsuarioLogado());
        	setHistoricoFollowUpVO(new HistoricoFollowUpVO());
        	setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        }catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }


    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", getFollowUpVO().getProspect().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), getFollowUpVO().getProspect().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", getFollowUpVO().getProspect().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), getFollowUpVO().getProspect().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", getFollowUpVO().getProspect().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", getFollowUpVO().getProspect().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
        
    public void consultarCampanha() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarCampanha().equals("descricao")) {
                objs = getFacadeFactory().getCampanhaFacade().consultarPorDescricao(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarCampanha().equals("unidadeensino")) {
                objs = getFacadeFactory().getCampanhaFacade().consultarPorUnidadeEnsino(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarCampanha().equals("curso")) {
                objs = getFacadeFactory().getCampanhaFacade().consultarPorCurso(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
//            if (getCampoConsultarCampanha().equals("situacao")) {
//                objs = getFacadeFactory().getCampanhaFacade().consultarPorSituacao(getValorConsultarCampanha(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//            }
            setListaConsultarCampanha(objs);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultarCampanha().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarCampanha() throws Exception {
        CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
        if (getMensagemDetalhada().equals("")) {
            this.getCompromissoAgendaPessoaHorarioVO().setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        }
        Uteis.liberarListaMemoria(this.getListaConsultarCampanha());
        this.setValorConsultarCampanha("");
        this.setCampoConsultarCampanha("");
    }

    public void limparCampoCampanha() {
        getCompromissoAgendaPessoaHorarioVO().setCampanha(new CampanhaVO());
    }

    public List<SelectItem> getTipoConsultaComboCampanha() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("unidadeensino", "Unidade"));
        itens.add(new SelectItem("curso", "Curso"));
//        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemWorkflow();
        montarListaSelectItemCampanha();
        montarListaSelectItemResponsavel();
        montarListaSelectItemCurso();
        montarListaSelectItemDepartamento();
    }

    public void montarListaSelectItemWorkflow() {
        try {
            montarListaSelectItemWorkflow(getFollowUpVO().getProspect().getCodigo());
        } catch (Exception e) {           
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemCampanha() {
        try {
            montarListaSelectItemCampanha(getFollowUpVO().getProspect().getCodigo());
        } catch (Exception e) {           
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemResponsavel() {
        try {
            montarListaSelectItemResponsavel(getFollowUpVO().getProspect().getCodigo());
        } catch (Exception e) {           
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemCurso() {
        try {
            montarListaSelectItemCurso(getFollowUpVO().getProspect().getCodigo());
        } catch (Exception e) {           
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }


    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        setFuncionarioVO(obj);
    }
    
    public void limparConsultaFuncionario() {
        getListaConsultaFuncionario().clear();
        setValorConsultaFuncionario("");
    }

    public void limparDadosFuncionario() {
    	setFuncionarioVO(new FuncionarioVO());
    }    

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        return itens;
    }
    
    public void montarListaSelectItemWorkflow(Integer codigoProspect) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getFollowUpFacade().consultaMontarComboboxWorkflow(codigoProspect);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                WorkflowVO obj = (WorkflowVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemWorkflow(objs);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCampanha(Integer codigoProspect) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getFollowUpFacade().consultaMontarComboboxCampanha(codigoProspect);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CampanhaVO obj = (CampanhaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            setListaSelectItemCampanha(objs);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemResponsavel(Integer codigoProspect) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getFollowUpFacade().consultaMontarComboboxResponsavel(codigoProspect);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UsuarioVO obj = (UsuarioVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getPessoa().getNome()));
            }
            setListaSelectItemResponsavel(objs);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCurso(Integer codigoProspect) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getFollowUpFacade().consultaMontarComboboxCurso(codigoProspect);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CursoVO obj = (CursoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCurso(objs);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public String getAbrirRichModalCompromisso() {
        getCompromissoAgendaPessoaHorarioVO().setProspect(getFollowUpVO().getProspect());
        if (!getManterRichModalAberto()) {
            return "RichFaces.$('panelNovoCompromisso').hide()";
        }
        return "RichFaces.$('panelNovoCompromisso').show()";
    }

    public void editarCompromissoAgendaPessoaHorario() {
        setCompromissoAgendaPessoaHorarioVO(getFollowUpVO().getCompromissoAgendaPessoaHorario());
    }

    public void novoCompromissoAgendaPessoaHorario() {
    	limparMensagem();
    	setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
    	getFuncionarioVO().setPessoa(getUsuarioLogado().getPessoa());
    	verificarPermissaoSelecionarFuncionario();
    }
    
    public void adicionarCompromissoAgendaPessoaHorario() {
        try {
        	if (getFuncionarioVO().getPessoa().getCodigo().intValue() > 0) {
        		getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(getFuncionarioVO().getPessoa());
        	}        	
            if (getCompromissoAgendaPessoaHorarioVO().getCodigo() == 0) {
                preencherCompromissoAgendaPessoaHorario();
                getCompromissoAgendaPessoaHorarioVO().setDataInicialCompromisso(getCompromissoAgendaPessoaHorarioVO().getDataCompromisso());
                CompromissoAgendaPessoaHorarioVO.validarDados(getCompromissoAgendaPessoaHorarioVO());
                getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(getCompromissoAgendaPessoaHorarioVO(), getUsuarioLogado());
                setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
            } else {
                preencherCompromissoAgendaPessoaHorario();
                CompromissoAgendaPessoaHorarioVO.validarDados(getCompromissoAgendaPessoaHorarioVO());
                getCompromissoAgendaPessoaHorarioVO().setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
                getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().alterarSemValidarDados(getCompromissoAgendaPessoaHorarioVO(), getUsuarioLogado());
                setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
            }
            getFollowUpVO().setCompromissoAgendaPessoaHorario(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoProspect(getFollowUpVO().getProspect().getCodigo(), getUsuarioLogado()));
            setApresentarProximoCompromisso(Boolean.TRUE);
            setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
            setManterRichModalAberto(false);

        } catch (Exception e) {
            setManterRichModalAberto(true);
            try {
                getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO(), getAgendaPessoaVO(), false, getUsuarioLogado());
            } catch (Exception ex) {
                setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
            }
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void adicionarCursoInteresse() throws Exception {
        try {
            if (getCursoInteresseVO().getCurso().getCodigo().intValue() != 0) {
                getCursoInteresseVO().setDataCadastro(new Date());
                getFacadeFactory().getProspectsFacade().adicionarObjCursoInteresseVOs(getFollowUpVO().getProspect(), getCursoInteresseVO());
                setMensagemDetalhada("", "Curso adicionado com sucesso");
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

    public void removerCursoInteresse() throws Exception {
        CursoInteresseVO obj = (CursoInteresseVO) context().getExternalContext().getRequestMap().get("cursoInteresseItens");
        getFacadeFactory().getProspectsFacade().excluirObjCursoInteresseVOs(getFollowUpVO().getProspect(), obj.getCurso().getCodigo());
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
    }

    public List<CursoVO> autocompleteCursoInteresse(Object suggest) {
        try {
            return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<CursoVO>();
        }
    }

    public void preencherCompromissoAgendaPessoaHorario() {
        try {
            if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().intValue() == 0) {
                getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(getUsuarioLogado().getPessoa());
            }
            if (!getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()) && getUsuarioLogado().getPerfilAdministrador()) {

                getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().setAgendaPessoa(getFacadeFactory().getFollowUpFacade().realizarValidacaoAgendaFollowUp(getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa(), getUsuarioLogado()));
            } else if (getFollowUpVO().getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())) {
                getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().setAgendaPessoa(getFacadeFactory().getFollowUpFacade().realizarValidacaoAgendaFollowUp(getUsuarioLogado().getPessoa(), getUsuarioLogado()));
            }
            getCompromissoAgendaPessoaHorarioVO().setAgendaPessoaHorario(getFacadeFactory().getFollowUpFacade().realizarValidacaoAgendaPessoaExiste(getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa(), getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario(), getCompromissoAgendaPessoaHorarioVO(), getUsuarioLogado()));
            if (getCompromissoAgendaPessoaHorarioVO().getCampanha().getCodigo() != 0) {
                getCompromissoAgendaPessoaHorarioVO().setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(getCompromissoAgendaPessoaHorarioVO().getCampanha().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void montarListaSelectItemDepartamento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarDepartamentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DepartamentoVO obj = (DepartamentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemDepartamento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemDepartamento() {
        try {
            montarListaSelectItemDepartamento("");
        } catch (Exception e) {           
            // System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public List consultarDepartamentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultarInteracaoWorkflow().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultarInteracaoWorkflow().setPage(DataScrollEvent.getPage());
//        consultar();
        realizarPesquisaInteracoesFiltradas();
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        followUpVO = null;
        historicoFollowUpVO = null;
    }

    public HistoricoFollowUpVO getHistoricoFollowUpVO() {
        if (historicoFollowUpVO == null) {
            historicoFollowUpVO = new HistoricoFollowUpVO();
        }
        return historicoFollowUpVO;
    }

    public void setHistoricoFollowUpVO(HistoricoFollowUpVO historicoFollowUpVO) {
        this.historicoFollowUpVO = historicoFollowUpVO;
    }

    public FollowUpVO getFollowUpVO() {
        if (followUpVO == null) {
            followUpVO = new FollowUpVO();
        }
        return followUpVO;
    }

    public void setFollowUpVO(FollowUpVO followUpVO) {
        this.followUpVO = followUpVO;
    }

    public String getFecharModalHistorico() {
        if (getHistoricoFollowUpVO().getResponsavel().getCodigo() == 0) {
            return "RichFaces.$('panelHistoricoFollowUp').hide()";
        } else {
            return "RichFaces.$('panelHistoricoFollowUp').show()";
        }
    }
    
    public String getUserNameLiberarFuncionalidade() {
		if (userNameLiberarFuncionalidade == null) {
			userNameLiberarFuncionalidade = "";
		}
		return userNameLiberarFuncionalidade;
	}

	public void setUserNameLiberarFuncionalidade(String userNameLiberarValorAcimaPrevisto) {
		this.userNameLiberarFuncionalidade = userNameLiberarValorAcimaPrevisto;
	}

	public String getSenhaLiberarFuncionalidade() {
		if (senhaLiberarFuncionalidade == null) {
			senhaLiberarFuncionalidade = "";
		}
		return senhaLiberarFuncionalidade;
	}

	public void setSenhaLiberarFuncionalidade(String senhaLiberarValorAcimaPrevisto) {
		this.senhaLiberarFuncionalidade = senhaLiberarValorAcimaPrevisto;
	}
    
    public boolean isLiberarEdicaoProspectVinculadoPessoaSomenteComSenha() {
		return liberarEdicaoProspectVinculadoPessoaSomenteComSenha;
	}
	
	public void setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(boolean liberarEdicaoProspectVinculadoPessoaSomenteComSenha) {
		this.liberarEdicaoProspectVinculadoPessoaSomenteComSenha = liberarEdicaoProspectVinculadoPessoaSomenteComSenha;
	}
	
	public void verificarPermissaoLiberarEdicaoProspectVinculadoPessoaSomenteComSenha() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoCRMEnum.LIBERAR_EDICAO_PROSPECT_VINCULADO_PESSOA_SOMENTE_COM_SENHA, getUsuarioLogadoClone());
			if(Uteis.isAtributoPreenchido(getFollowUpVO().getProspect().getPessoa())) {
				setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(true);
			}
		} catch (Exception e) {
			setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(false);
		}
	}
	
	public void persistirLiberarEdicaoProspectVinculadoPessoaSomenteComSenha() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoCRMEnum.LIBERAR_EDICAO_PROSPECT_VINCULADO_PESSOA_SOMENTE_COM_SENHA, usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.PROSPECT, getFollowUpVO().getProspect().getCodigo().toString(), OperacaoFuncionalidadeEnum.LIBERAR_EDICAO_COM_SENHA, usuarioVerif, ""));
			setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(false);
		} catch (Exception e) {
			setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public Boolean getApresentarProximoCompromisso() {
        if (apresentarProximoCompromisso == null) {
            apresentarProximoCompromisso = Boolean.TRUE;
        }
        return apresentarProximoCompromisso;
    }

    public void setApresentarProximoCompromisso(Boolean apresentarProximoCompromisso) {
        this.apresentarProximoCompromisso = apresentarProximoCompromisso;
    }

    public String getCampoConsultarCampanha() {
        if (campoConsultarCampanha == null) {
            campoConsultarCampanha = "";
        }
        return campoConsultarCampanha;
    }

    public void setCampoConsultarCampanha(String campoConsultarCampanha) {
        this.campoConsultarCampanha = campoConsultarCampanha;
    }

    public String getValorConsultarCampanha() {
        if (valorConsultarCampanha == null) {
            valorConsultarCampanha = "";
        }
        return valorConsultarCampanha;
    }

    public void setValorConsultarCampanha(String valorConsultarCampanha) {
        this.valorConsultarCampanha = valorConsultarCampanha;
    }

    public List getListaConsultarCampanha() {
        if (listaConsultarCampanha == null) {
            listaConsultarCampanha = new ArrayList(0);
        }
        return listaConsultarCampanha;
    }

    public void setListaConsultarCampanha(List listaConsultarCampanha) {
        this.listaConsultarCampanha = listaConsultarCampanha;
    }

    public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorarioVO() {
        if (compromissoAgendaPessoaHorarioVO == null) {
            compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
        }
        return compromissoAgendaPessoaHorarioVO;
    }

    public void setCompromissoAgendaPessoaHorarioVO(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) {
        this.compromissoAgendaPessoaHorarioVO = compromissoAgendaPessoaHorarioVO;
    }

    public Boolean getManterRichModalAberto() {
        if (manterRichModalAberto == null) {
            manterRichModalAberto = false;
        }
        return manterRichModalAberto;
    }

    public void setManterRichModalAberto(Boolean manterRichModalAberto) {
        this.manterRichModalAberto = manterRichModalAberto;
    }

    public AgendaPessoaVO getAgendaPessoaVO() {
        if (agendaPessoaVO == null) {
            agendaPessoaVO = new AgendaPessoaVO();
        }
        return agendaPessoaVO;
    }

    public void setAgendaPessoaVO(AgendaPessoaVO agendaPessoaVO) {
        this.agendaPessoaVO = agendaPessoaVO;
    }

    public DataModelo getControleConsultarInteracaoWorkflow() {
        if (controleConsultarInteracaoWorkflow == null) {
            controleConsultarInteracaoWorkflow = new DataModelo();
        }
        return controleConsultarInteracaoWorkflow;
    }

    public void setControleConsultarInteracaoWorkflow(DataModelo controleConsultarInteracaoWorkflow) {
        this.controleConsultarInteracaoWorkflow = controleConsultarInteracaoWorkflow;
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

    public List getListaSelectItemCampanha() {
        if (listaSelectItemCampanha == null) {
            listaSelectItemCampanha = new ArrayList(0);
        }
        return listaSelectItemCampanha;
    }

    public void setListaSelectItemCampanha(List listaSelectItemCampanha) {
        this.listaSelectItemCampanha = listaSelectItemCampanha;
    }

    public List getListaSelectItemResponsavel() {
        if (listaSelectItemResponsavel == null) {
            listaSelectItemResponsavel = new ArrayList(0);
        }
        return listaSelectItemResponsavel;
    }

    public void setListaSelectItemResponsavel(List listaSelectItemResponsavel) {
        this.listaSelectItemResponsavel = listaSelectItemResponsavel;
    }

    public void inicializarDadosNovaInteracao() {    	
        getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
        getInteracaoWorkflowVO().getResponsavel().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
        getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());        
        getInteracaoWorkflowVO().setDataInicio(new Date());
//        setAbrirFecharModalInteracao("");
    }

    public void inicializarDadosInteracao() {
        getInteracaoWorkflowVO().getCompromissoAgendaPessoaHorario().setCodigo(getFollowUpVO().getCompromissoAgendaPessoaHorario().getCodigo());
        getInteracaoWorkflowVO().setProspect(getFollowUpVO().getCompromissoAgendaPessoaHorario().getProspect());
        getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
        getInteracaoWorkflowVO().getResponsavel().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
        getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
        getInteracaoWorkflowVO().setDataInicio(new Date());
        setAbrirFecharModalInteracao("");
    }

    public WorkflowVO getWorkflowVO() {
        if (workflowVO == null) {
            workflowVO = new WorkflowVO();
        }
        return workflowVO;
    }

    public void setWorkflowVO(WorkflowVO workflowVO) {
        this.workflowVO = workflowVO;
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

    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = new UsuarioVO();
        }
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
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

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public String getAbrirModalEdicaoProspect() {
        if (getAbrirRichModalEdicaoProspect()) {
            return "RichFaces.$('panelEdicaoProspect').show()";
        } else {
            return "RichFaces.$('panelEdicaoProspect').hide()";
        }
    }

    public Boolean getAbrirRichModalEdicaoProspect() {
        if (abrirRichModalEdicaoProspect == null) {
            abrirRichModalEdicaoProspect = Boolean.FALSE;
        }
        return abrirRichModalEdicaoProspect;
    }

    public void setAbrirRichModalEdicaoProspect(Boolean abrirRichModalEdicaoProspect) {
        this.abrirRichModalEdicaoProspect = abrirRichModalEdicaoProspect;
    }

    public Boolean getApresentarBotaoAdiarIniciar() {
        if (apresentarBotaoAdiarIniciar == null) {
            apresentarBotaoAdiarIniciar = Boolean.FALSE;
        }
        return apresentarBotaoAdiarIniciar;
    }

    public void setApresentarBotaoAdiarIniciar(Boolean apresentarBotaoAdiarIniciar) {
        this.apresentarBotaoAdiarIniciar = apresentarBotaoAdiarIniciar;
    }

    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList(0);
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
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

    public void setAbrirFecharModalInteracao(String abrirFecharModalInteracao) {
        this.abrirFecharModalInteracao = abrirFecharModalInteracao;
    }

    public String getAbrirFecharModalInteracao() {
        if (abrirFecharModalInteracao == null) {
            abrirFecharModalInteracao = "";
        }
        return abrirFecharModalInteracao;
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

    public InteracaoFollowUpRelControle getInteracaoFollowUpRelControle() {
        if (interacaoFollowUpRelControle == null) {
            interacaoFollowUpRelControle = new InteracaoFollowUpRelControle();
        }
        return interacaoFollowUpRelControle;
    }

    public void setInteracaoFollowUpRelControle(
            InteracaoFollowUpRelControle interacaoFollowUpRelControle) {
        this.interacaoFollowUpRelControle = interacaoFollowUpRelControle;
    }

    /**
     * @return the listaSelectItemDepartamento
     */
    public List getListaSelectItemDepartamento() {
        if (listaSelectItemDepartamento == null) {
            listaSelectItemDepartamento = new ArrayList();
        }
        return listaSelectItemDepartamento;
    }

    /**
     * @param listaSelectItemDepartamento the listaSelectItemDepartamento to set
     */
    public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
        this.listaSelectItemDepartamento = listaSelectItemDepartamento;
    }

    public List<SelectItem> getListaSelectItemTipoContato() {
        if (listaSelectItemTipoContato == null) {
            listaSelectItemTipoContato = new ArrayList<SelectItem>(0);
            try {
                List<TipoContatoVO> tipoContatoVOs = getFacadeFactory().getTipoContatoFacade().consultar("", StatusAtivoInativoEnum.ATIVO, false, getUsuarioLogado());
                for (TipoContatoVO tipoContatoVO : tipoContatoVOs) {
                    listaSelectItemTipoContato.add(new SelectItem(tipoContatoVO.getCodigo(), tipoContatoVO.getDescricao()));
                }
            } catch (Exception e) {
            }
        }
        return listaSelectItemTipoContato;
    }

    public void setListaSelectItemTipoContato(List<SelectItem> listaSelectItemTipoContato) {
        this.listaSelectItemTipoContato = listaSelectItemTipoContato;
    }
    private String acaoLigarSemAgenda;

    public void realizarLigacaoSemAgenda() {
        try {
            List<CompromissoAgendaPessoaHorarioVO> compromissoAgendaPessoaHorarioVOs = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoFuturoProspect(getFollowUpVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (compromissoAgendaPessoaHorarioVOs.isEmpty()) {
                setAcaoLigarSemAgenda("window.open('ConverteGetParaPostServletInteracao?pagina=interacaoWorkflowForm.jsp&paramLigacaoAtivaSemAgenda=ligacaoAtivaSemAgenda&valorLigacaoAtivaSemAgenda=true&paramCodigoProspectLigacaoAtivaSemAgenda=codigoProspectLigacaoAtivaSemAgenda&valorCodigoProspectLigacaoAtivaSemAgenda=" + getFollowUpVO().getProspect().getCodigo() + "','InteracaoWorkFlow','resizable=yes,maximize=false,fullscreen=false,scrollbars=yes, width=850, height=720');");
            } else {
                setAcaoLigarSemAgenda("RichFaces.$('panelCompromissoFuturo').show()");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getAcaoLigarSemAgenda() {
        if (acaoLigarSemAgenda == null) {
            acaoLigarSemAgenda = "";
        }
        return acaoLigarSemAgenda;
    }

    public void setAcaoLigarSemAgenda(String acaoLigarSemAgenda) {
        this.acaoLigarSemAgenda = acaoLigarSemAgenda;
    }

    /**
     * @return the compromissoAgendaPessoaHorarioVOs
     */
    public List getCompromissoAgendaPessoaHorarioVOs() {
        if (compromissoAgendaPessoaHorarioVOs == null) {
            compromissoAgendaPessoaHorarioVOs = new ArrayList();
        }
        return compromissoAgendaPessoaHorarioVOs;
    }

    /**
     * @param compromissoAgendaPessoaHorarioVOs the compromissoAgendaPessoaHorarioVOs to set
     */
    public void setCompromissoAgendaPessoaHorarioVOs(List compromissoAgendaPessoaHorarioVOs) {
        this.compromissoAgendaPessoaHorarioVOs = compromissoAgendaPessoaHorarioVOs;
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

	public Boolean getApresentarMatriculaDireta() {
		if (apresentarMatriculaDireta == null) {
			apresentarMatriculaDireta = Boolean.FALSE;
		}
		return apresentarMatriculaDireta;
	}

	public void setApresentarMatriculaDireta(Boolean apresentarMatriculaDireta) {
		this.apresentarMatriculaDireta = apresentarMatriculaDireta;
	}

	public Boolean getApresentarBotaoRelatorioSituacaoFinaceiraAluno() {
		if(apresentarBotaoRelatorioSituacaoFinaceiraAluno == null){
			apresentarBotaoRelatorioSituacaoFinaceiraAluno = Boolean.FALSE;
		}
		return apresentarBotaoRelatorioSituacaoFinaceiraAluno;
	}

	public void setApresentarBotaoRelatorioSituacaoFinaceiraAluno(Boolean apresentarBotaoRelatorioSituacaoFinaceiraAluno) {
		this.apresentarBotaoRelatorioSituacaoFinaceiraAluno = apresentarBotaoRelatorioSituacaoFinaceiraAluno;
	}

	public Boolean getAlunoSerasa() {
		if(alunoSerasa == null){
			alunoSerasa = Boolean.FALSE;
		}
		return alunoSerasa;
	}

	public void setAlunoSerasa(Boolean alunoSerasa) {
		this.alunoSerasa = alunoSerasa;
	}
	
	
	public void navegarRelatorioSituacaoFinanceiraAluno(){
		try {
			String matricula = getFacadeFactory().getMatriculaFacade().consultarUltimaMatriculaAtivaPorCodigoPessoa(getFollowUpVO().getProspect().getPessoa().getCodigo());
			if(!matricula.trim().equals("")){
				context().getExternalContext().getSessionMap().put("matriculaItens", matricula);
			}else{
				throw new Exception("Não existe MATRÍCULA ativa para esta pessoa");
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	// Metodo que Consulta Todos os Familiares que já Estudarem ou Ainda Estudam Na Instituição
	public void apresentarFamiliares(){
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getFollowUpFacade().consultarFamilires(getFollowUpVO()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public Boolean getApresentarFuncionario() {
		if (apresentarFuncionario == null) {
			apresentarFuncionario = Boolean.FALSE;
		}
		return apresentarFuncionario;
	}

	public void setApresentarFuncionario(Boolean apresentarFuncionario) {
		this.apresentarFuncionario = apresentarFuncionario;
	}

	public String getAutocompleteCursoInteresse() {
		if (autocompleteCursoInteresse == null) {
			autocompleteCursoInteresse = "";
		}
		return autocompleteCursoInteresse;
	}

	public void setAutocompleteCursoInteresse(String autocompleteCursoInteresse) {
		this.autocompleteCursoInteresse = autocompleteCursoInteresse;
	}
	
	public void selecionarCursoInteressePorCodigo() {
		consultarCursoInteressePorCodigo(getValorAutoComplete(getAutocompleteCursoInteresse()));
	}
	
	public void consultarCursoInteressePorCodigo(int codigo) {
		try {
			getCursoInteresseVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado()));
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
	
    public void verificarPermissaoExcluirHistoricoFollowUp() {
        try {
        	ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExcluirHistoricoFollowUp", getUsuarioLogado());
            setPermitirExcluirHistoricoFollowUp(true);
        } catch (Exception e) {
        	setPermitirExcluirHistoricoFollowUp(false);
        }
    }

	public Boolean getPermitirExcluirHistoricoFollowUp() {
		if (permitirExcluirHistoricoFollowUp == null) {
			permitirExcluirHistoricoFollowUp = false;
		}
		return permitirExcluirHistoricoFollowUp;
	}

	public void setPermitirExcluirHistoricoFollowUp(Boolean permitirExcluirHistoricoFollowUp) {
		this.permitirExcluirHistoricoFollowUp = permitirExcluirHistoricoFollowUp;
	}

	public void selecionarObservacao() {
		setNovaInteracaoWorkflowVO((InteracaoWorkflowVO) context().getExternalContext().getRequestMap().get("interacao")); 
	}

	public InteracaoWorkflowVO getNovaInteracaoWorkflowVO() {
		if (novaInteracaoWorkflowVO == null) {
			novaInteracaoWorkflowVO = new InteracaoWorkflowVO();
		}
		return novaInteracaoWorkflowVO;
	}

	public void setNovaInteracaoWorkflowVO(InteracaoWorkflowVO novaInteracaoWorkflowVO) {
		this.novaInteracaoWorkflowVO = novaInteracaoWorkflowVO;
	}

	public Boolean getMobile() {
		if (mobile == null) {
			mobile = Boolean.FALSE;
		}
		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
    
}
