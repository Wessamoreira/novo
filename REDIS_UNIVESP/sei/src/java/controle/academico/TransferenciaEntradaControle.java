package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
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
import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.facade.jdbc.academico.TransferenciaEntrada;
import negocio.facade.jdbc.academico.TransferenciaEntradaDisciplinasAproveitadas;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas transferenciaEntradaForm.jsp
 * transferenciaEntradaCons.jsp) com as funcionalidades da classe <code>TransferenciaEntrada</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see TransferenciaEntrada
 * @see TransferenciaEntradaVO
 */

@Controller("TransferenciaEntradaControle")
@Scope("viewScope")
@Lazy
public class TransferenciaEntradaControle extends SuperControle {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String popUpAbrir;
    private TransferenciaEntradaVO transferenciaEntradaVO;
    private String matricula_Erro;
    private String matricula_valorApresentar;
    private String codigoRequerimento_Erro;
    private String codigoRequerimento_valorApresentar;
    protected List listaSelectItemPeriodoLetivo;
    protected List listaSelectItemGradeCurricular;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemTurno;
    protected List listaSelectItemCurso;
    private List listaSelectItemPeriodoLetivoMatricula = new ArrayList(0);
    protected List listaConsultaCurso;
    protected String campoConsultaCurso;
    protected String valorConsultaCurso;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    //protected List listaSelectItemTurma;
    protected List listaSelectItemDisciplina;
    protected List listaConsultaMatriculado;
    private TransferenciaEntradaDisciplinasAproveitadasVO transferenciaEntradaDisciplinasAproveitadasVO;
    private List listaSelectItemTipoMidiaCaptacao;
    private String turma_Erro;
    protected Boolean matriculado;
    private List listaConsultaRequisitante;
    private String valorConsultaRequisitante;
    private String campoConsultaRequisitante;
    private List<SelectItem> listaSelectItemProcessoMatricula;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private Boolean realizandoTranferenciaEntradaMatriculaExistente;
	private List<SelectItem> listaSelectItemMatriculado;
	private String situacaoMatriculado;
	private Date dataConsulta;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public TransferenciaEntradaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("pessoa");
        setListaConsulta(new ArrayList<>(0));
        novo();
        setTipoRequerimento("TE");
        setMensagemID("msg_entre_prmconsulta");
    }

    @PostConstruct
    public void iniciarTransferenciaApartirRequerimentoEAproveitamento(){
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if (request.getParameter("requerimento") == null || ((String) request.getParameter("requerimento")).equals("")) {
                return;
            }
            Integer codigoRequerimento = Integer.parseInt((String) request.getParameter("requerimento"));
            if (codigoRequerimento == null || codigoRequerimento == 0) {
                return;
            }

            RequerimentoVO obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(codigoRequerimento, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            AproveitamentoDisciplinaVO aprov = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorCodigoRequerimento(codigoRequerimento, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());

            TransferenciaEntradaVO transferenciaEntradaExistenteVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigoRequerimento(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if ((!transferenciaEntradaExistenteVO.getCodigo().equals(0)) &&
                (transferenciaEntradaExistenteVO.getSituacaoEmAvaliacao())) {
                // se já existe uma transferência de entrada gravada, que ainda está em avalição, 
                // para este requerimento, então temos apresentá-la para edição. Pois, não justifica-se
                // gerar outra transferencia de entrada para o mesmo requerimento.
                transferenciaEntradaVO = transferenciaEntradaExistenteVO;
            } else {
                // gerando uma nova transferencia de entrada para a matricula
                transferenciaEntradaVO.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
                transferenciaEntradaVO.getUnidadeEnsino().setNome(obj.getUnidadeEnsino().getNome());
                transferenciaEntradaVO.setCurso(obj.getCurso());
                transferenciaEntradaVO.setTurno(obj.getTurno());
                transferenciaEntradaVO.setGradeCurricular(aprov.getGradeCurricular());
                transferenciaEntradaVO.setPeridoLetivo(aprov.getPeridoLetivo());
                transferenciaEntradaVO.getCodigoRequerimento().setCodigo(obj.getCodigo());
                transferenciaEntradaVO.setPessoa(obj.getPessoa());
                transferenciaEntradaVO.setMatricula(obj.getMatricula());
                transferenciaEntradaVO.setCodigoRequerimento(obj);
                transferenciaEntradaVO.setCidade(aprov.getCidadeVO());
                transferenciaEntradaVO.setInstituicaoOrigem(aprov.getInstituicao());
                
            }
            if (getTransferenciaEntradaVO().getMatriculado() && !obj.getMatricula().getMatricula().equals("")) {
                adicionarUltimaMatriculaPeriodo();
                montarListaSelectItemDisciplina();
            }
            this.setCodigoRequerimento_valorApresentar(obj.getTipoRequerimento().getNome() + " - " + obj.getData_Apresentar());
            montarListaSelectItemGradeCurricular();
            montarListaSelectItemPeriodoLetivo();
            montarListaSelectItemTipoMidiaCaptacao();
            this.setCodigoRequerimento_Erro("");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>TransferenciaEntrada</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setMatricula_Erro("");
        setMatriculado(Boolean.FALSE);
        setMatricula_valorApresentar("");
        setCodigoRequerimento_valorApresentar("");
        setCodigoRequerimento_Erro("");
        setTransferenciaEntradaVO(new TransferenciaEntradaVO());
        inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
        setTransferenciaEntradaDisciplinasAproveitadasVO(new TransferenciaEntradaDisciplinasAproveitadasVO());
        inicializarListasSelectItemTodosComboBox();
        getTransferenciaEntradaVO().setTipoTransferenciaEntrada(TipoTransferenciaEntrada.EXTERNA.getValor());
        setCampoConsultaRequisitante("");
        setRealizandoTranferenciaEntradaMatriculaExistente(false);
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
    }

    public String novoMatriculado() {
        setMatricula_Erro("");
        setMatricula_valorApresentar("");
        setMatriculado(Boolean.FALSE);
        setCodigoRequerimento_valorApresentar("");
        setCodigoRequerimento_Erro("");
        setTransferenciaEntradaVO(new TransferenciaEntradaVO());
        getTransferenciaEntradaVO().setMatriculado(true);
        inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
        setTransferenciaEntradaDisciplinasAproveitadasVO(new TransferenciaEntradaDisciplinasAproveitadasVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>TipoMidiaCaptacao</code>.
     */
    public void montarListaSelectItemTipoMidiaCaptacao(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNomeMidia()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemTipoMidiaCaptacao(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>TipoMidiaCaptacao</code>. Buscando todos
     * os objetos correspondentes a entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemTipoMidiaCaptacao() {
        try {
            montarListaSelectItemTipoMidiaCaptacao("");
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.montarListaSelectItemTipoMidiaCaptacao: " + e.getMessage());
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeMidia</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
        List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
        return lista;
    }

    public void inicializarListasSelectItemTodosComboBox() {
        // montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemDisciplina();
        montarListaSelectItemGradeCurricular();
        montarListaSelectItemPeriodoLetivo();
        //montarListaSelectItemTurma();
        montarListaSelectItemTipoMidiaCaptacao();
        montarListaSelectItemUnidadeEnsino();

    }

    public void inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado() {
        try {
            transferenciaEntradaVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado: " + e.getMessage());
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TransferenciaEntrada</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        setMatricula_Erro("");
        setMatricula_valorApresentar("");
        setCodigoRequerimento_valorApresentar("");
        setCodigoRequerimento_Erro("");
        TransferenciaEntradaVO obj = (TransferenciaEntradaVO) context().getExternalContext().getRequestMap().get("transferenciaEntradaItens");
        obj = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        setTransferenciaEntradaVO(obj);
        inicializarListasSelectItemTodosComboBox();
        obj.setNovoObj(Boolean.FALSE);
        setTransferenciaEntradaDisciplinasAproveitadasVO(new TransferenciaEntradaDisciplinasAproveitadasVO());
        consultarMatriculaAlunoSelecionado();
        //montarListaSelectItemTurma();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>TransferenciaEntrada</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getTransferenciaEntradaVO().getIsTransferenciaEntradaPodeSerAlterada()) {
                inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
                if (transferenciaEntradaVO.isNovoObj().booleanValue()) {
                    getFacadeFactory().getTransferenciaEntradaFacade().incluir(transferenciaEntradaVO, getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(),true , true , getUsuarioLogado());
                } else {
                    getFacadeFactory().getTransferenciaEntradaFacade().alterar(transferenciaEntradaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                }
				/**
				 * Adicionara regra responsável por realizar o vínculo de uma
				 * Transferência de Entrada a uma Matrícula existente.
				 */
                if (getRealizandoTranferenciaEntradaMatriculaExistente()) {
    				getFacadeFactory().getTransferenciaEntradaFacade().vincularMatriculaTransferenciaEntrada(getTransferenciaEntradaVO(), getUsuarioLogado());
    				setRealizandoTranferenciaEntradaMatriculaExistente(false);
    			}
            } else {
//                throw new Exception("Não é mais possível alterar uma transferência, na qual o aluno já está efetivamente matriculado.");
            	getFacadeFactory().getTransferenciaEntradaFacade().alterarTransferenciaEntradaEfetivada(transferenciaEntradaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
        }finally {
			
		}
    }

	public void indeferir() {
		try {
			getTransferenciaEntradaVO().setSituacao("IN");
			inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
			if (transferenciaEntradaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTransferenciaEntradaFacade().incluir(transferenciaEntradaVO, getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), true , true ,getUsuarioLogado());
			} else {
				getFacadeFactory().getTransferenciaEntradaFacade().alterar(transferenciaEntradaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			/**
			 * Adicionara regra responsável por realizar o vínculo de uma
			 * Transferência de Entrada a uma Matrícula existente.
			 */
			if (getRealizandoTranferenciaEntradaMatriculaExistente()) {
				getFacadeFactory().getTransferenciaEntradaFacade().vincularMatriculaTransferenciaEntrada(getTransferenciaEntradaVO(), getUsuarioLogado());
				setRealizandoTranferenciaEntradaMatriculaExistente(false);
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			getTransferenciaEntradaVO().setSituacao("AV");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void autorizarAproveitamentoDisciplina() {
        try {
            getTransferenciaEntradaVO().setSituacao("EF");
            inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
            if (transferenciaEntradaVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getTransferenciaEntradaFacade().incluir(transferenciaEntradaVO, getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(),  true,true, getUsuarioLogado());
            } else {
                getFacadeFactory().getTransferenciaEntradaFacade().alterar(transferenciaEntradaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            /**
			 * Adicionara regra responsável por realizar o vínculo de uma
			 * Transferência de Entrada a uma Matrícula existente.
			 */
            if (getRealizandoTranferenciaEntradaMatriculaExistente()) {
				getFacadeFactory().getTransferenciaEntradaFacade().vincularMatriculaTransferenciaEntrada(getTransferenciaEntradaVO(), getUsuarioLogado());
				setRealizandoTranferenciaEntradaMatriculaExistente(false);
			}
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            getTransferenciaEntradaVO().setSituacao("AV");
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public String inicializarMatriculaTransferenciaEntrada() throws Exception {
        try {
            TransferenciaEntradaVO.validarDadosParaMatricula(transferenciaEntradaVO, getConfiguracaoGeralPadraoSistema());
            gravar();
            context().getExternalContext().getSessionMap().put("codigoTransferenciaEntrada", this.getTransferenciaEntradaVO().getCodigo());
            return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
            //setPopUpAbrir("");
        }
    }

    public void montarListaSelectItemProcessoMatricula() {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            List<SelectItem> objs = new ArrayList<SelectItem>();
            if (getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo() == null) {
                setListaSelectItemProcessoMatricula(objs);
                return;
            }
            resultadoConsulta = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorNomeUnidadeEnsino("", getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
                if (obj.ativoParaMatriculaEPreMatricula(new Date())) {
                    objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                }
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemProcessoMatricula(objs);
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.montarListaSelectItemProcessoMatricula: " + e.getMessage());
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TransferenciaEntradaCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List<TransferenciaEntradaVO> objs = new ArrayList<>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigo(new Integer(valorInt), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, true,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data") && getDataConsulta() != null) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorData(Uteis.getDateTime(getDataConsulta(), 0, 0, 0), Uteis.getDateTime(getDataConsulta(), 23, 59, 59), getSituacaoMatriculado(),
                        TipoTransferenciaEntrada.EXTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, true,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), true,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("codigoRequerimento")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigoRequerimento(new Integer(valorInt), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, true,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("instituicaoOrigem")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorInstituicaoOrigem(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, 
                		getConfiguracaoFinanceiroPadraoSistema(), TipoTransferenciaEntrada.EXTERNA, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoJustificativa")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorTipoJustificativa(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("curso")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCurso(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, true,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("pessoa")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, true,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaCons.xhtml");
        }
    }

    public String consultarMatriculado() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigo(new Integer(valorInt), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getSituacaoMatriculado(),
                        TipoTransferenciaEntrada.EXTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, false,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("codigoRequerimento")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigoRequerimento(new Integer(valorInt), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, false,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("instituicaoOrigem")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorInstituicaoOrigem(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, 
                		getConfiguracaoFinanceiroPadraoSistema(), TipoTransferenciaEntrada.EXTERNA, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoJustificativa")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorTipoJustificativa(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("curso")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCurso(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, false,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("pessoa")) {
                objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.EXTERNA, false,
                        Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }

            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            setListaConsultaMatriculado(objs);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TransferenciaEntradaVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            if (!transferenciaEntradaVO.getSituacao().equals("EF")) {
                getFacadeFactory().getTransferenciaEntradaFacade().excluir(transferenciaEntradaVO, true, getUsuarioLogado());
                setTransferenciaEntradaVO(new TransferenciaEntradaVO());
                setTransferenciaEntradaDisciplinasAproveitadasVO(new TransferenciaEntradaDisciplinasAproveitadasVO());
            } else {
                throw new Exception("Não é possível excluir uma tranferência de entrada EFETIVADA");
            }
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
        }
    }

    public String excluirMatriculado() {
        try {
            if (!transferenciaEntradaVO.getSituacao().equals("EF")) {
                getFacadeFactory().getTransferenciaEntradaFacade().excluir(transferenciaEntradaVO, true, getUsuarioLogado());
                setTransferenciaEntradaVO(new TransferenciaEntradaVO());
                setTransferenciaEntradaDisciplinasAproveitadasVO(new TransferenciaEntradaDisciplinasAproveitadasVO());
                getTransferenciaEntradaVO().setMatriculado(true);
            } else {
                throw new Exception("Não é possível excluir uma tranferência de entrada EFETIVADA");
            }
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaForm.xhtml");
        }
    }

    @SuppressWarnings("static-access")
    public void atualizarListaDisciplinasAproveitadas() throws Exception {
        transferenciaEntradaVO.setTransferenciaEntradaDisciplinasAproveitadasVOs(new TransferenciaEntradaDisciplinasAproveitadas().consultarTransferenciaEntradaDisciplinasAproveitadass(
                getTransferenciaEntradaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
    }

    /*
     * Método responsável por adicionar um novo objeto da classe
     * <code>TransferenciaEntradaDisciplinasAproveitadas</code> para o objeto <code>transferenciaEntradaVO</code> da
     * classe <code>TransferenciaEntrada</code>
     */
    public String adicionarTransferenciaEntradaDisciplinasAproveitadas() throws Exception {
        try {
            if (!getTransferenciaEntradaVO().getCodigo().equals(0)) {
                transferenciaEntradaDisciplinasAproveitadasVO.setTransferenciaEntrada(getTransferenciaEntradaVO().getCodigo());
            }
            getTransferenciaEntradaDisciplinasAproveitadasVO().setDisciplina(consultarDisciplinaPorCodigo(getTransferenciaEntradaDisciplinasAproveitadasVO().getDisciplina().getCodigo()));
            getTransferenciaEntradaVO().adicionarObjTransferenciaEntradaDisciplinasAproveitadasVOs(getTransferenciaEntradaDisciplinasAproveitadasVO(), getTransferenciaEntradaVO().getCurso().getPeriodicidade());
            this.setTransferenciaEntradaDisciplinasAproveitadasVO(new TransferenciaEntradaDisciplinasAproveitadasVO());
            setMensagemID("msg_dados_adicionados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe
     * <code>TransferenciaEntradaDisciplinasAproveitadas</code> para edição pelo usuário.
     */
    public String editarTransferenciaEntradaDisciplinasAproveitadas() throws Exception {
        TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get(
                "transferenciaEntradaDisciplinasAproveitadas");
        setTransferenciaEntradaDisciplinasAproveitadasVO(obj);
        return "";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code>
     * do objeto <code>transferenciaEntradaVO</code> da classe <code>TransferenciaEntrada</code>
     */
    public String removerTransferenciaEntradaDisciplinasAproveitadas() throws Exception {
        TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get(
                "transferenciaEntradaDisciplinasAproveitadas");
        getTransferenciaEntradaVO().excluirObjTransferenciaEntradaDisciplinasAproveitadasVOs(obj.getDisciplina().getCodigo());
        setMensagemID("msg_dados_excluidos");
        return "";
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaInicialMatriculado() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultarMatriculado();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoJustificativa</code>
     */
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public List getListaSelectItemTipoJustificativaTransferenciaEntrada() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoJustificativaAlteracaoMatriculas = (Hashtable) Dominios.getTipoJustificativaAlteracaoMatricula();
        Enumeration keys = tipoJustificativaAlteracaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoJustificativaAlteracaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorCpf() {
        try {
            String campoConsulta = transferenciaEntradaVO.getPessoa().getCPF();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            transferenciaEntradaVO.setPessoa(pessoa);
            this.consultarMatriculaAlunoSelecionado();
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            transferenciaEntradaVO.getPessoa().setNome("");
            transferenciaEntradaVO.getPessoa().setCodigo(0);
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Requerimento</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarRequerimentoPorChavePrimaria() {
        try {
            Integer campoConsulta = transferenciaEntradaVO.getCodigoRequerimento().getCodigo();
            RequerimentoVO requerimento = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(campoConsulta, "TE",
                    super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            transferenciaEntradaVO.getUnidadeEnsino().setCodigo(requerimento.getUnidadeEnsino().getCodigo());
            transferenciaEntradaVO.getUnidadeEnsino().setNome(requerimento.getUnidadeEnsino().getNome());
            transferenciaEntradaVO.setCurso(requerimento.getMatricula().getCurso());
            transferenciaEntradaVO.setTurno(requerimento.getMatricula().getTurno());
            transferenciaEntradaVO.getCodigoRequerimento().setCodigo(requerimento.getCodigo());
            transferenciaEntradaVO.setPessoa(requerimento.getPessoa());
            transferenciaEntradaVO.setMatricula(requerimento.getMatricula());
            transferenciaEntradaVO.setCodigoRequerimento(requerimento);
            if (getTransferenciaEntradaVO().getMatriculado() && !getTransferenciaEntradaVO().getMatricula().getMatricula().equals("")) {
                adicionarUltimaMatriculaPeriodo();
                montarListaSelectItemDisciplina();
            }

            // this.setCodigoRequerimento_valorApresentar(requerimento.getTipoRequerimento().getNome() + " - " +
            // requerimento.getData_Apresentar());
            // this.setCodigoRequerimento_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            this.setCodigoRequerimento_valorApresentar("");
            setMensagemID("msg_erro_dadosnaoencontrados");
            transferenciaEntradaVO.getCodigoRequerimento().setCodigo(0);
            // this.setCodigoRequerimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public String getMascaraConsultaRequisitante() {
        if (getCampoConsultaRequisitante().equals("CPF")) {
            return "return mascara(this.form,'formRequerente:valorConsultaRequerente','999.999.999-99',event);";
        }
        return "";
    }

    public List getTipoConsultaComboRequisitante() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    public void consultarRequisitante() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaRequisitante().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaRequisitante().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequisitante(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            if (getCampoConsultaRequisitante().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaRequisitante().equals("RG")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaRequisitante(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaRequisitante(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparDadosPesssoa() {
        getTransferenciaEntradaVO().setPessoa(new PessoaVO());
        setListaConsultaAluno(null);
        setListaConsultaRequisitante(null);
        setValorConsultaRequisitante("");
    }

    public void selecionarRequisitante() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("requerenteItens");
            obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            this.getTransferenciaEntradaVO().setPessoa(obj);
            this.consultarMatriculaAlunoSelecionado();
            getTransferenciaEntradaVO().getMatricula().setMatricula("");
            setListaConsultaRequisitante(null);
            setValorConsultaRequisitante("");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public String getCadastrarNovoAluno() throws Exception {
        try {
            navegarPara(AlunoControle.class.getSimpleName(), "novo", "");
            executarMetodoControle(AlunoControle.class.getSimpleName(), "permitirIniciarInscricao");
            return "abrirPopup('alunoForm.jsp', 'alunoForm', 780, 585);";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void selecionarRequerimento() {
        try {
            RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
            obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());

            transferenciaEntradaVO.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
            transferenciaEntradaVO.getUnidadeEnsino().setNome(obj.getUnidadeEnsino().getNome());
            transferenciaEntradaVO.setCurso(obj.getMatricula().getCurso());
            transferenciaEntradaVO.setTurno(obj.getMatricula().getTurno());
            transferenciaEntradaVO.getCodigoRequerimento().setCodigo(obj.getCodigo());
            transferenciaEntradaVO.setPessoa(obj.getPessoa());
            transferenciaEntradaVO.setMatricula(obj.getMatricula());
            transferenciaEntradaVO.setCodigoRequerimento(obj);
            if (getTransferenciaEntradaVO().getMatriculado() && !obj.getMatricula().getMatricula().equals("")) {
                adicionarUltimaMatriculaPeriodo();
                montarListaSelectItemDisciplina();
            }

            this.setCodigoRequerimento_valorApresentar(obj.getTipoRequerimento().getNome() + " - " + obj.getData_Apresentar());
            this.consultarMatriculaAlunoSelecionado();
            this.setCodigoRequerimento_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            this.setCodigoRequerimento_valorApresentar("");
            setMensagemID("msg_erro_dadosnaoencontrados");
            transferenciaEntradaVO.getCodigoRequerimento().setCodigo(0);
            this.setCodigoRequerimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarMatriculaPorChavePrimaria() {
        try {
            String campoConsulta = transferenciaEntradaVO.getMatricula().getMatricula();
            MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_DADOSMINIMOS), getUsuarioLogado());
            if (matricula.getAluno().getCodigo().intValue() != getTransferenciaEntradaVO().getCodigoRequerimento().getPessoa().getCodigo().intValue()) {
                setMatricula_Erro(getMensagemInternalizacao("msg_transferenciaEntrada_alunoNaoConfere"));
                setMensagemDetalhada("msg_erro", "");
                return;
            }
            if (matricula.getSituacao().equalsIgnoreCase("AT") || matricula.getSituacao().equalsIgnoreCase("TR")) {
                transferenciaEntradaVO.setMatricula(matricula);
                transferenciaEntradaVO.setUnidadeEnsino(matricula.getUnidadeEnsino());
                transferenciaEntradaVO.setCurso(matricula.getCurso());
                transferenciaEntradaVO.setTurno(matricula.getTurno());
                transferenciaEntradaVO.setTipoMidiaCaptacao(matricula.getTipoMidiaCaptacao());
                transferenciaEntradaVO.setPessoa(matricula.getAluno());
                adicionarUltimaMatriculaPeriodo();
                getTransferenciaEntradaVO().setMatriculado(true);
                montarListaSelectItemDisciplina();
                setMensagemID("msg_dados_consultados");
            } else {
                novoMatriculado();
                setMensagemID("msg_erro_dadosnaoencontrados");
                setMensagemDetalhada("msg_erro", "");
                this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_matriculanaovalida"));
            }
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            transferenciaEntradaVO.getMatricula().setMatricula("");
            transferenciaEntradaVO.getMatriculaPeriodo().setCodigo(0);
            this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_matriculanaoencontrada"));
        }
    }

    public void adicionarUltimaMatriculaPeriodo() throws Exception {
        List matriculas = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(getTransferenciaEntradaVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        if (matriculas.isEmpty()) {
            throw new Exception("Erro TrasferenciaEntradaControle.adicionarUltimaMatriculaPeriodo");
        } else {
            getTransferenciaEntradaVO().setMatriculaPeriodo((MatriculaPeriodoVO) matriculas.get(0));
            transferenciaEntradaVO.setGradeCurricular(getTransferenciaEntradaVO().getMatriculaPeriodo().getGradeCurricular());
            transferenciaEntradaVO.setPeridoLetivo(getTransferenciaEntradaVO().getMatriculaPeriodo().getPeridoLetivo());
            transferenciaEntradaVO.setTurma(getTransferenciaEntradaVO().getMatriculaPeriodo().getTurma());
            transferenciaEntradaVO.setUnidadeEnsinoCurso(getTransferenciaEntradaVO().getMatriculaPeriodo().getUnidadeEnsinoCurso());
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("pessoa", "Aluno"));
        itens.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
        itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("instituicaoOrigem", "Instituição Origem"));
        itens.add(new SelectItem("curso", "Curso"));
        //itens.add(new SelectItem("codigo", "Código"));
        //itens.add(new SelectItem("situacao", "Situação"));
        //itens.add(new SelectItem("tipoJustificativa", "Tipo Justificativa"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaEntradaCons.xhtml");
    }

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

    public void montarConsultaCurso() {
        setListaConsultaCurso(new ArrayList(0));
    }

    public void selecionarCurso() throws Exception {
        try {
            UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
            getTransferenciaEntradaVO().setCurso(unidadeEnsinoCurso.getCurso());
            getTransferenciaEntradaVO().setTurno(unidadeEnsinoCurso.getTurno());
            GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCurso(getTransferenciaEntradaVO().getCurso().getCodigo(), "AT", false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (gradeCurricularVO.getCodigo() == 0) {
                throw new Exception("Não existe uma Grade Curricular Ativa para este curso.");
            }
            setMensagemDetalhada("");
            montarListaSelectItemGradeCurricular();
            getTransferenciaEntradaVO().setUnidadeEnsinoCurso(unidadeEnsinoCurso.getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getMatricula_Erro() {
        return matricula_Erro;
    }

    public void setMatricula_Erro(String matricula_Erro) {
        this.matricula_Erro = matricula_Erro;
    }

    public TransferenciaEntradaVO getTransferenciaEntradaVO() {
        return transferenciaEntradaVO;
    }

    public void setTransferenciaEntradaVO(TransferenciaEntradaVO transferenciaEntradaVO) {
        this.transferenciaEntradaVO = transferenciaEntradaVO;
    }

    public String getMatricula_valorApresentar() {
        return matricula_valorApresentar;
    }

    public void setMatricula_valorApresentar(String matricula_valorApresentar) {
        this.matricula_valorApresentar = matricula_valorApresentar;
    }

    public String getCodigoRequerimento_Erro() {
        return codigoRequerimento_Erro;
    }

    public void setCodigoRequerimento_Erro(String codigoRequerimento_Erro) {
        this.codigoRequerimento_Erro = codigoRequerimento_Erro;
    }

    public String getCodigoRequerimento_valorApresentar() {
        return codigoRequerimento_valorApresentar;
    }

    public void setCodigoRequerimento_valorApresentar(String codigoRequerimento_valorApresentar) {
        this.codigoRequerimento_valorApresentar = codigoRequerimento_valorApresentar;
    }

    public List getListaSelectItemTipoMidiaCaptacao() {
        return listaSelectItemTipoMidiaCaptacao;
    }

    public void setListaSelectItemTipoMidiaCaptacao(List listaSelectItemTipoMidiaCaptacao) {
        this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                setListaSelectItemUnidadeEnsino(new ArrayList(0));
                UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(unidadeEnsino.getCodigo(), unidadeEnsino.getNome()));
                getTransferenciaEntradaVO().getUnidadeEnsino().setCodigo(unidadeEnsino.getCodigo());
                unidadeEnsino = null;
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.montarListaSelectItemUnidadeEnsino: " + e.getMessage());
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemDisciplina() {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarDisciplinaPorGradeCurricular();
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DisciplinaVO obj = (DisciplinaVO) i.next();
//                objs.add(new SelectItem(obj.getCodigo(), obj.getNome() + " CH: " + obj.getCargaHoraria()));
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemDisciplina(objs);
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.montarListaSelectItemDisciplina: " + e.getMessage());
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarDisciplinaPorGradeCurricular() throws Exception {
        List lista = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorGradeCurricular(getTransferenciaEntradaVO().getGradeCurricular().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public DisciplinaVO consultarDisciplinaPorCodigo(Integer Prm) throws Exception {
        DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(Prm, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return disciplina;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>PeriodoLetivoMatricula</code>.
     */
    public void montarListaSelectItemPeriodoLetivo(Integer prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getTransferenciaEntradaVO().getGradeCurricular().getCodigo().equals(0)) {
                setListaSelectItemPeriodoLetivoMatricula(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarPeriodoLetivoPorSigla(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                if (obj.getPeriodoLetivo().intValue() == 1) {
                    getTransferenciaEntradaVO().setPeridoLetivo(obj);
                }
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemPeriodoLetivoMatricula(objs);
            montarListaSelectItemDisciplina();
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivoMatricula</code>. Buscando
     * todos os objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemPeriodoLetivo() {
        try {
            montarListaSelectItemPeriodoLetivo(transferenciaEntradaVO.getGradeCurricular().getCodigo());
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.montarListaSelectItemPeriodoLetivo: " + e.getMessage());
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>sigla</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPeriodoLetivoPorSigla(Integer siglaPrm) throws Exception {
        List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(siglaPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>PeriodoLetivo</code>.
     */
    public void montarListaSelectItemGradeCurricular(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo().equals(0)) {
                setListaSelectItemGradeCurricular(new ArrayList(0));
                return;
            }
            if (getTransferenciaEntradaVO().getCurso().getCodigo().equals(0)) {
                setListaSelectItemGradeCurricular(new ArrayList(0));
                return;
            }
            
            	resultadoConsulta = consultarGradeCurricularPorDescricao(prm);
            	i = resultadoConsulta.iterator();
            	List objs = new ArrayList(0);
            	objs.add(new SelectItem(0, ""));
            	while (i.hasNext()) {
            		GradeCurricularVO obj = (GradeCurricularVO) i.next();
            		if (obj.getSituacao().equals("AT")) {
            			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            			if (getTransferenciaEntradaVO().isNovoObj()) {
            				getTransferenciaEntradaVO().setGradeCurricular(obj);
            			}
            		}
            	}
            	ordenador = new SelectItemOrdemValor();
            	Collections.sort((List) objs, ordenador);
            	setListaSelectItemGradeCurricular(objs);
            
            montarListaSelectItemPeriodoLetivo();
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivo</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemGradeCurricular() {
        try {
            montarListaSelectItemGradeCurricular("");
        } catch (Exception e) {
            //System.out.println("Erro TrasferenciaEntradaControle.montarListaSelectItemGradeCurricular: " + e.getMessage());
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarGradeCurricularPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getTransferenciaEntradaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /*
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turma</code>.
     */
//    public void montarListaSelectItemTurma(String prm) throws Exception {
//        List objs = new ArrayList(0);
//        if ((getTransferenciaEntradaVO().getCurso() == null) || (transferenciaEntradaVO.getCurso().getCodigo().intValue() == 0)) {
//
//            setListaSelectItemTurma(objs);
//            return;
//        }
//        if ((transferenciaEntradaVO.getTurno() == null) || (transferenciaEntradaVO.getTurno().getCodigo().intValue() == 0)) {
//
//            setListaSelectItemTurma(objs);
//            return;
//        }
//        if (getTransferenciaEntradaVO().getPeridoLetivo() == null) {
//
//            setListaSelectItemTurma(objs);
//            return;
//        }
//
//        List resultadoConsulta = consultarTurmaPorIdentificadorTurma();
//        Iterator i = resultadoConsulta.iterator();
//        objs.add(new SelectItem(0, ""));
//        while (i.hasNext()) {
//            TurmaVO obj = (TurmaVO) i.next();
//            objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
//        }
//        if (objs.isEmpty()) {
//            this.setTurma_Erro("Não existe turma cadastrada para o curso: " + getTransferenciaEntradaVO().getCurso().getNome().toUpperCase() + " no período: "
//                    + getTransferenciaEntradaVO().getTurno().getNome().toUpperCase());
//        } else {
//            this.setTurma_Erro("");
//        }
//        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//        Collections.sort((List) objs, ordenador);
//        setListaSelectItemTurma(objs);
//    }
    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turma</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turma</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
//    public void montarListaSelectItemTurma() {
//        try {
//            montarListaSelectItemTurma("");
//        } catch (Exception e) {
//            //System.out.println("MENSAGEM => " + e.getMessage());;
//        }
//    }
    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>identificadorTurma</code> Este atributo é uma lista (<code>List</code>)
     * utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTurmaPorIdentificadorTurma() throws Exception {
        List listaResultado = getFacadeFactory().getTurmaFacade().consultarPorPeriodoLetivoUnidadeEnsinoCursoTurno(getTransferenciaEntradaVO().getPeridoLetivo().getCodigo(),
                getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo(), getTransferenciaEntradaVO().getCurso().getCodigo(), getTransferenciaEntradaVO().getTurno().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return listaResultado;
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarDisciplinaPeriodoLetivo() {
        try {
            List listaDisciplina = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getTransferenciaEntradaVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
            getTransferenciaEntradaVO().getPeridoLetivo().setGradeDisciplinaVOs(listaDisciplina);
        } catch (Exception e) {
            getTransferenciaEntradaVO().getPeridoLetivo().setGradeDisciplinaVOs(new ArrayList(0));
        }
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaConsultaCurso() {
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public List getListaSelectItemCurso() {
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public List getListaSelectItemGradeCurricular() {
        return listaSelectItemGradeCurricular;
    }

    public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
        this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
    }

    public List getListaSelectItemPeriodoLetivo() {
        return listaSelectItemPeriodoLetivo;
    }

    public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
        this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
    }

    public List getListaSelectItemPeriodoLetivoMatricula() {
        return listaSelectItemPeriodoLetivoMatricula;
    }

    public void setListaSelectItemPeriodoLetivoMatricula(List listaSelectItemPeriodoLetivoMatricula) {
        this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
    }

    public List getListaSelectItemTurno() {
        return listaSelectItemTurno;
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public String getValorConsultaCurso() {
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public String getCampoConsultaCurso() {
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

//    public List getListaSelectItemTurma() {
//        return listaSelectItemTurma;
//    }
//
//    public void setListaSelectItemTurma(List listaSelectItemTurma) {
//        this.listaSelectItemTurma = listaSelectItemTurma;
//    }
    public String getTurma_Erro() {
        return turma_Erro;
    }

    public void setTurma_Erro(String turma_Erro) {
        this.turma_Erro = turma_Erro;
    }

    public TransferenciaEntradaDisciplinasAproveitadasVO getTransferenciaEntradaDisciplinasAproveitadasVO() {
        return transferenciaEntradaDisciplinasAproveitadasVO;
    }

    public void setTransferenciaEntradaDisciplinasAproveitadasVO(TransferenciaEntradaDisciplinasAproveitadasVO transferenciaEntradaDisciplinasAproveitadasVO) {
        this.transferenciaEntradaDisciplinasAproveitadasVO = transferenciaEntradaDisciplinasAproveitadasVO;
    }

    public List getListaSelectItemDisciplina() {
        return listaSelectItemDisciplina;
    }

    public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
        this.listaSelectItemDisciplina = listaSelectItemDisciplina;
    }

    public List getListaConsultaMatriculado() {
        return listaConsultaMatriculado;
    }

    public void setListaConsultaMatriculado(List listaConsultaMatriculado) {
        this.listaConsultaMatriculado = listaConsultaMatriculado;
    }

    public boolean getApresentarResultadoConsultaMatriculado() {
        if (this.getListaConsultaMatriculado() == null || this.getListaConsultaMatriculado().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        transferenciaEntradaVO = null;

        matricula_Erro = null;
        matricula_valorApresentar = null;

        codigoRequerimento_Erro = null;
        codigoRequerimento_valorApresentar = null;
    }

    /**
     * @return the matriculado
     */
    public Boolean getMatriculado() {
    	if (matriculado == null) {
    		matriculado = Boolean.TRUE;
    	}
        return matriculado;
    }

    /**
     * @param matriculado
     *            the matriculado to set
     */
    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

    /**
     * @return the listaConsultaRequisitante
     */
    public List getListaConsultaRequisitante() {
        return listaConsultaRequisitante;
    }

    /**
     * @param listaConsultaRequisitante the listaConsultaRequisitante to set
     */
    public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
        this.listaConsultaRequisitante = listaConsultaRequisitante;
    }

    /**
     * @return the valorConsultaRequisitante
     */
    public String getValorConsultaRequisitante() {
        return valorConsultaRequisitante;
    }

    /**
     * @param valorConsultaRequisitante the valorConsultaRequisitante to set
     */
    public void setValorConsultaRequisitante(String valorConsultaRequisitante) {
        this.valorConsultaRequisitante = valorConsultaRequisitante;
    }

    /**
     * @return the campoConsultaRequisitante
     */
    public String getCampoConsultaRequisitante() {
        return campoConsultaRequisitante;
    }

    /**
     * @param campoConsultaRequisitante the campoConsultaRequisitante to set
     */
    public void setCampoConsultaRequisitante(String campoConsultaRequisitante) {
        this.campoConsultaRequisitante = campoConsultaRequisitante;
    }

    public void setListaSelectItemProcessoMatricula(List<SelectItem> listaSelectItemProcessoMatricula) {
        this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
    }

    public List<SelectItem> getListaSelectItemProcessoMatricula() {
        return listaSelectItemProcessoMatricula;
    }

    public Boolean getApresentarAno() {
        return getTransferenciaEntradaVO().getCurso().getPeriodicidade().equals("AN") || getTransferenciaEntradaVO().getCurso().getPeriodicidade().equals("SE");
    }

    public Boolean getApresentarSemestre() {
        return getTransferenciaEntradaVO().getCurso().getPeriodicidade().equals("SE");
    }
    private String valorConsultaCidade;
    private String campoConsultaCidade;
    private List<CidadeVO> listaConsultaCidade;

    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
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

    public List<CidadeVO> getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList<CidadeVO>(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("estado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparCidade() {
        getTransferenciaEntradaVO().setCidade(null);
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getTransferenciaEntradaVO().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de
     * Cidade <code>Cidade/code>.
     */
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("estado", "Estado"));
        return itens;
    }
//    public Boolean getApresentaAnoSemestre() {
//    	return !getTransferenciaEntradaVO().getCurso().getNivelEducacional().equals("PO") && !getTransferenciaEntradaVO().getCurso().getNivelEducacional().equals("EX");
//    }

    /**
     * @return the popUpAbrir
     */
    public String getPopUpAbrir() {
        if (popUpAbrir == null) {
            popUpAbrir = "";
        }
        return popUpAbrir;
    }

    /**
     * @param popUpAbrir the popUpAbrir to set
     */
    public void setPopUpAbrir(String popUpAbrir) {
        this.popUpAbrir = popUpAbrir;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}
	
	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
	
	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        this.montarDadosMatriculaSelecionada(obj);
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		setListaConsultaAluno(null);
		setMensagemID("msg_dados_consultados");
	}
	
	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public boolean getIsApresentarBotaoRegistrarMatriculaExistente() throws Exception {
		return !getFacadeFactory().getTransferenciaEntradaFacade().executarVerificarTransferenciaEntradaVinculadaMatricula(getTransferenciaEntradaVO().getCodigo(), getUsuarioLogado())
				&& this.getListaConsultaAluno().size() > 0;
	}

	public Boolean getRealizandoTranferenciaEntradaMatriculaExistente() {
		if (realizandoTranferenciaEntradaMatriculaExistente == null) {
			realizandoTranferenciaEntradaMatriculaExistente = false;
		}
		return realizandoTranferenciaEntradaMatriculaExistente;
	}

	public void setRealizandoTranferenciaEntradaMatriculaExistente(Boolean realizandoTranferenciaEntradaMatriculaExistente) {
		this.realizandoTranferenciaEntradaMatriculaExistente = realizandoTranferenciaEntradaMatriculaExistente;
	}
	
	public boolean getIsHabilitarConformeSituacao() {
		return getTransferenciaEntradaVO().getIsTransferenciaEntradaPodeSerAlterada();
	}
	
	public String getEstiloConformeSituacao() {
		if (getTransferenciaEntradaVO().getIsTransferenciaEntradaPodeSerAlterada()) {
			return "campos";
		} else {
			return "camposSomenteLeitura";
		}
	}
	
	public String getEstiloCampoObrigatorioConformeSituacaoObjeto() {
		if (getTransferenciaEntradaVO().getNovoObj()) {
			return "campos";
		} else {
			return "camposObrigatorios";
		}
	}
	
	public String getEstiloCampoObrigatorioSomenteLeituraConformeSituacaoObjeto(){
		if (getTransferenciaEntradaVO().getNovoObj()) {
			return "camposSomenteLeitura";
		}else{
			return "camposSomenteLeituraObrigatorio";
		}
	}
	
	public void consultarMatriculaAlunoSelecionado() throws Exception {
		try {
			List<MatriculaVO> obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoa(getTransferenciaEntradaVO().getPessoa().getCodigo(), null, false, getUsuarioLogado());
			setListaConsultaAluno(obj);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void montarDadosMatriculaSelecionada(MatriculaVO obj) throws Exception {
		try {
			this.getTransferenciaEntradaVO().setMatricula(obj);
			this.getTransferenciaEntradaVO().setPessoa(obj.getAluno());
			this.getTransferenciaEntradaVO().setUnidadeEnsino(obj.getUnidadeEnsino());
			this.getTransferenciaEntradaVO().setCurso(obj.getCurso());
			this.getTransferenciaEntradaVO().setTurno(obj.getTurno());
			this.getTransferenciaEntradaVO().setGradeCurricular(obj.getGradeCurricularAtual());
			this.montarListaSelectItemGradeCurricular(obj.getGradeCurricularAtual().getCodigo());
			this.montarListaSelectItemPeriodoLetivo(obj.getGradeCurricularAtual().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
	}
	
	public void montarListaSelectItemGradeCurricular(Integer gradeCurricular) throws Exception {
		try {
			if (getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo().equals(0)) {
				setListaSelectItemGradeCurricular(new ArrayList(0));
				return;
			}
			if (getTransferenciaEntradaVO().getCurso().getCodigo().equals(0)) {
				setListaSelectItemGradeCurricular(new ArrayList(0));
				return;
			}
			GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(gradeCurricular, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(gradeCurricularVO)) {
				getListaSelectItemGradeCurricular().add(new SelectItem(gradeCurricularVO.getCodigo(), gradeCurricularVO.getNome()));
			} else {
				getListaSelectItemGradeCurricular().add(new SelectItem(0, ""));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemMatriculado() {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("naoMatriculado", "Não"));
		objs.add(new SelectItem("matriculado", "Sim"));
		objs.add(new SelectItem("ambas", "Ambas"));
		return objs;
	}

	public void setListaSelectItemMatriculado(List<SelectItem> listaSelectItemMatriculado) {
		this.listaSelectItemMatriculado = listaSelectItemMatriculado;
	}

	public String getSituacaoMatriculado() {
		if (situacaoMatriculado == null) {
			situacaoMatriculado = "";
		}
		return situacaoMatriculado;
	}

	public void setSituacaoMatriculado(String situacaoMatriculado) {
		this.situacaoMatriculado = situacaoMatriculado;
	}

	public Date getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(Date dataConsulta) {
		this.dataConsulta = dataConsulta;
	}
	
	public Boolean getApresentarCampoDataConsulta() {
		return getControleConsulta().getCampoConsulta().equals("data");
	}
	
	public Boolean getApresentarCampoTextoConsulta() {
		return getControleConsulta().getCampoConsulta().equals("curso") || getControleConsulta().getCampoConsulta().equals("matriculaMatricula") 
				|| getControleConsulta().getCampoConsulta().equals("instituicaoOrigem") || getControleConsulta().getCampoConsulta().equals("pessoa");
	}
	
	public Boolean getApresentarCampoInteiroConsulta() {
		return getControleConsulta().getCampoConsulta().equals("codigoRequerimento");
	}
	
	public void limparValorConsulta() {
		getControleConsulta().setValorConsulta("");
	}
}
