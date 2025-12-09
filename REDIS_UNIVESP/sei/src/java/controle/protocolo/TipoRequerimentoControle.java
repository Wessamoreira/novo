
package controle.protocolo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.PendenciaTipoDocumentoTipoRequerimentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoAlunoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.TaxaVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioRequerimentoEnum;
import negocio.comuns.protocolo.PossivelResponsavelRequerimentoVO;
import negocio.comuns.protocolo.SituacaoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoFuncionarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoSituacaoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoTurmaVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.TipoControleCobrancaViaRequerimentoEnum;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.protocolo.TipoRequerimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * tipoRequerimentoForm.jsp tipoRequerimentoCons.jsp) com as funcionalidades da classe <code>TipoRequerimento</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see TipoRequerimento
 * @see TipoRequerimentoVO
*/
@Controller("TipoRequerimentoControle")
@Scope("viewScope")
@Lazy
public class TipoRequerimentoControle extends SuperControle implements Serializable {   
	private static final long serialVersionUID = -1449940738795417636L;
	private TipoRequerimentoVO tipoRequerimentoVO;
    private String departamentoResponsavel_Erro;
    protected String campoConsultaDepartamento;
    protected String valorConsultaDepartamento;
    private String apresentarModalUnidadeEnsinoEspecifica;
    protected List<DepartamentoVO> listaConsultaDepartamento;
    private List<UnidadeEnsinoVO> listaUnidades;
    private List<SelectItem> listaSelectItemResponsavelDepartamento;
    private TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamento;
    private List<SelectItem> listaSelectItemCargo;
    private List<SelectItem> listaSelectItemTextoPadrao;
    private List<SelectItem> listaSelectItemQuestionario;
    private List<SelectItem> listaSelectItemQuestionarioDepartamento;
    protected List<SelectItem> listaSelectItemCentroReceita;    
    protected List<SelectItem> listaSelectItemTaxa;    
    protected List<SelectItem> listaSelectItemTipoControleCobrancaViaRequerimento;    
    private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private CursoVO cursoVO;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	protected List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<TurmaVO> listaConsultaTurmaVOs;
	private List<SelectItem> tipoConsultaComboTurma;
	private TurmaVO turmaVO;
	private Boolean possuiRequerimentoVinculado;
    private List<SelectItem> listaSelectItemTipoArquivoUpload;
    private TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO;
    private List<SelectItem> listaSelectItemUnidadeEnsinoFuncionarioTramite;
    private UnidadeEnsinoVO unidadeEnsinoFuncionarioTramite;
    private List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamento;
    private SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO;
    private TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO;
    protected List<SelectItem> listaSelectItemSituacao;
    protected String situacao;
	private List<UnidadeEnsinoVO> unidadeEnsinoVOsRemover;
	private Boolean marcarTodasUnidadeEnsinoRemover;
	private List listaNivelEducacional;
    private List listaTipoLayout;
    private Boolean marcarTodasSituacoesHistorico;
    private List<SelectItem> listaSelectItemTextoPadraoCertificadoImpresso;
	private List<TipoDocumentoVO> tipoDocumentoVOs;
	private TipoDocumentoVO tipoDocumentoVO;
	private Boolean marcarTodosTiposDocumento;
	private FuncionarioVO funcionarioRemover;
    private String campoConsultaCursoTransferenciaInterna;
	private String valorConsultaCursoTransferenciaInterna;
	private List<CursoVO> listaConsultaCursoTransferenciaInterna;
	
	private TipoRequerimentoCursoVO tipoRequerimentoCursoVO;
	private List<CidTipoRequerimentoVO> listaCidOriginalVOs;
	private List<CidTipoRequerimentoVO>  listaCidVOs;
	private String descricaoCid;
	private String codCid;
	private FileUploadEvent fileUploadEvent;
	
	private List<SelectItem> listaBimestre;
	private List<SelectItem> listaTipoNota;
	
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaDeferimento;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaIndeferimento;
	private List<SelectItem> listaSelectItemTemplateMensagemAutomaticaEnum;
	private List<SelectItem> listaSelectItemTipoAluno;
	
	public TipoRequerimentoControle() throws Exception {
        //obterUsuarioLogado();
//        inicializarFacades();
        consultarUnidadeEnsino();
        setControleConsulta(new ControleConsulta());
        montarListaSelectItemCentroReceita();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void consultarUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getListaUnidades(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            if (!lista.isEmpty()) {
                setListaUnidades(lista);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaUnidades(new ArrayList<UnidadeEnsinoVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
    * Rotina responsável por disponibilizar um novo objeto da classe <code>TipoRequerimento</code>
    * para edição pelo usuário da aplicação.
     * @throws Exception 
    */
    public String novo() throws Exception {         
        removerObjetoMemoria(this);
        setTipoRequerimentoVO(new TipoRequerimentoVO());
        setTipoRequerimentoDepartamento(new TipoRequerimentoDepartamentoVO());
        montarListaSelectItemCentroReceita();
        montarListaSelectItemTaxa();
        consultarUnidadeEnsino();
        setPossuiRequerimentoVinculado(false);
        montarListaSelectItemNivelEducacionalCurso();
        consultarTipoDocumento();
        inicializarPersonalizacaoMensagemAutomaticaDeferimento();
        inicializarPersonalizacaoMensagemAutomaticaIndeferimento();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("tipoRequerimentoForm");
    }

    /**
    * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TipoRequerimento</code> para alteração.
    * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
    */
    public String editar() {
        try {
            TipoRequerimentoVO obj = (TipoRequerimentoVO)context().getExternalContext().getRequestMap().get("tipoRequerimentoItem");
            obj = getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            consultarUnidadeEnsino();
            consultarCidTipoRequerimento(obj);
            obj.obterListaUnidadeEnsinoPreenchida(getListaUnidades());
            obj.setNovoObj(Boolean.FALSE);
            tramitaEntreDepartamentos();
            setTipoRequerimentoVO(obj);
            selecionarUnidadeEnsinoEspecifica();
            montarListaSelectItemCentroReceita();
            montarListaSelectItemTaxa();
            montarListaSelectItemNivelEducacionalCurso();
            montarListaTipoLayoutHistorico();
            setPossuiRequerimentoVinculado(getFacadeFactory().getRequerimentoFacade().consultarTipoRequerimentoVinculadoRequerimento(obj));
            consultarTipoDocumento();
            setPersonalizacaoMensagemAutomaticaDeferimento((PersonalizacaoMensagemAutomaticaVO) Uteis.clonar(obj.getPersonalizacaoMensagemAutomaticaDeferimento()));
            setPersonalizacaoMensagemAutomaticaIndeferimento((PersonalizacaoMensagemAutomaticaVO) Uteis.clonar(obj.getPersonalizacaoMensagemAutomaticaIndeferimento()));
            inicializarPersonalizacaoMensagemAutomaticaDeferimento();
            inicializarPersonalizacaoMensagemAutomaticaIndeferimento();
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoRequerimentoForm");
        } catch (Exception e) {
        	e.printStackTrace();
            setMensagemDetalhada("msg_erro", e.getMessage());
//            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoRequerimentoCons");
            return "";
        }
    }

    /**
    * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TipoRequerimento</code>.
    * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    */
    public void gravar() {
        try {
            if (getTipoRequerimentoVO().getUnidadeEnsinoEspecifica()) {
                getTipoRequerimentoVO().preencherListaUnidadeEnsino(getListaUnidades());
            }
            if(getTipoRequerimentoVO().getVerificarPendenciaDocumentacao()) { 
            	montarListaPendenciaTipoDocumentoTipoRequerimento();
            }
	        else {
	        	limparTiposDocumento();
	        	getTipoRequerimentoVO().setPendenciaTipoDocumentoTipoRequerimentoVOs(new ArrayList<PendenciaTipoDocumentoTipoRequerimentoVO>(0));
	        }
            inicializarPersonalizacaoMensagemAutomaticaTipoRequerimento();
            if (tipoRequerimentoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getTipoRequerimentoFacade().incluir(tipoRequerimentoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getTipoRequerimentoFacade().alterar(tipoRequerimentoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inativar() {
        try {
            tipoRequerimentoVO.setSituacao("IN");
            getFacadeFactory().getTipoRequerimentoFacade().inativar(tipoRequerimentoVO, getUsuarioLogado());
            setMensagemID("msg_dados_inativado");
        } catch (Exception e) {
        	tipoRequerimentoVO.setSituacao("AT");
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis no JSP TipoRequerimentoCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    */
    public String consultar(int codigoTipoRequerimento) {
        try {
            super.consultar();
            List<TipoRequerimentoVO> objs = new ArrayList<TipoRequerimentoVO>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
                	throw new Exception("Informe apenas valores numéricos.");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorCodigo(new Integer(valorInt), getSituacao(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), getSituacao(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("valor")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorValor(new Double(valorDouble), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("prazoExecucao")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
                	throw new Exception("Informe apenas valores numéricos.");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPrazoExecucao(new Integer(valorInt), getSituacao(), getUnidadeEnsinoLogado().getCodigo(),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, true,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeDepartamento")) {
                objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorNomeDepartamento(getControleConsulta().getValorConsulta(), getSituacao(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true,getUsuarioLogado());
            }
            if (Uteis.isAtributoPreenchido(codigoTipoRequerimento)) {
            	setListaConsulta(objs.stream().filter(trvo -> !trvo.getCodigo().equals(codigoTipoRequerimento)).collect(Collectors.toList()));
            } else {
            	setListaConsulta(objs);
            }
            setControleConsulta(new ControleConsulta());
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
        	e.printStackTrace();
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
    
    public String consultar() {
    	return consultar(0);
    }
    
    public String consultarTipoRequerimentoDeferirEsteRequerimento() {
    	return consultar(getTipoRequerimentoVO().getCodigo());
    }
    
    public void selecionarTipoRequerimento() {
        try {
            TipoRequerimentoVO obj = (TipoRequerimentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoItem");
            getTipoRequerimentoVO().setTipoRequerimentoAbrirDeferimento(obj);
            setMensagemID("msg_dados_selecionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
  }
    
    public void limparTipoRequerimento() {
    	getTipoRequerimentoVO().setTipoRequerimentoAbrirDeferimento(null);
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TipoRequerimentoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
            getFacadeFactory().getTipoRequerimentoFacade().excluir(tipoRequerimentoVO, getUsuarioLogado());
            setTipoRequerimentoVO( new TipoRequerimentoVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void consultarDepartamento() {
        try {            
            List objs = new ArrayList(0);
            if (getCampoConsultaDepartamento().equals("codigo")) {
                if (getValorConsultaDepartamento().equals("")) {
                    setValorConsultaDepartamento("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDepartamento());
                objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDepartamento().equals("nome")) {
                objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getValorConsultaDepartamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDepartamento().equals("nomePessoa")) {
                objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePessoa(getValorConsultaDepartamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaDepartamento(objs);
            setMensagemID("msg_dados_consultados");
            
        } catch (Exception e) {
            setListaConsultaDepartamento(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());            
        }
    }
    
    public List getTipoConsultaComboDepartamento() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("nomePessoa", "Responsável"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List<SelectItem> getListaSelectItemTipo() throws Exception {
    	return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TiposRequerimento.class,false);
    }
    
    public void montarDadosComboboxResponsavelDepartamento() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getFuncionarioFacade().consultarPorNomeECodigoDepartamentoEMultiDepartamento("%", 
                    getTipoRequerimentoDepartamento().getDepartamento().getCodigo(), "FU", getUnidadeEnsinoLogado().getCodigo(), 
                    false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                FuncionarioVO obj = (FuncionarioVO) i.next();
                objs.add(new SelectItem(obj.getPessoa().getCodigo(), obj.getPessoa().getNome()));
            }
            setListaSelectItemResponsavelDepartamento(objs);
        } catch (Exception e) {
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public boolean getExisteDepartamentoSelecionado() {
        if (getTipoRequerimentoVO().getTramitaEntreDepartamentos()) {
            if (getTipoRequerimentoDepartamento().getDepartamento().getCodigo().equals(0)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (getTipoRequerimentoVO().getDepartamentoResponsavel().getCodigo().equals(0)) {
                return false;
            } else {
                return true;
            }
        }        
    }
    
    public void selecionarDepartamento(){
        DepartamentoVO obj = (DepartamentoVO)context().getExternalContext().getRequestMap().get("departamentoItem");
        if (getTipoRequerimentoVO().getTramitaEntreDepartamentos()) {
            getTipoRequerimentoDepartamento().setDepartamento(obj);
            montarDadosComboboxResponsavelDepartamento();
        } else {
            getTipoRequerimentoVO().setDepartamentoResponsavel(obj);
        }
    }

    public void selecionarUnidadeEnsinoEspecifica() {
        if (getTipoRequerimentoVO().getUnidadeEnsinoEspecifica()) {
            setApresentarModalUnidadeEnsinoEspecifica("RichFaces.$('panelUnidadeEnsinoEspecifica').show();");
        } else {
            setApresentarModalUnidadeEnsinoEspecifica("");
        }
    }


    public void irPaginaInicial() throws Exception{
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Departamento</code> por meio de sua respectiva chave primária.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade
     * montando automaticamente o resultado da consulta para apresentação.
    */
    public void consultarDepartamentoPorChavePrimaria() {
        try {
            Integer campoConsulta = tipoRequerimentoVO.getDepartamentoResponsavel().getCodigo();
            DepartamentoVO departamento = getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            tipoRequerimentoVO.getDepartamentoResponsavel().setNome(departamento.getNome());
            this.setDepartamentoResponsavel_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            tipoRequerimentoVO.getDepartamentoResponsavel().setNome("");
            tipoRequerimentoVO.getDepartamentoResponsavel().setCodigo(0);
            this.setDepartamentoResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }
    

    /**
    * Rotina responsável por preencher a combo de consulta da telas.
    */
    public List getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Codigo"));
        //itens.add(new SelectItem("valor", "Valor"));
        itens.add(new SelectItem("prazoExecucao", "Prazo Execução"));
//        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("nomeDepartamento", "Departamento Responsável"));
        return itens;
    }

    public List getTipoConsultaComboSituacao() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("AT", "ATIVO"));
        itens.add(new SelectItem("IN", "INATIVO"));
        return itens;
    }

    /**
    * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
    */
    public String inicializarConsultar() {         
    	removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("tipoRequerimentoCons");
    }

    public String getCampoConsultaDepartamento() {
        return campoConsultaDepartamento;
    }

    public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
        this.campoConsultaDepartamento = campoConsultaDepartamento;
    }

    public List getListaConsultaDepartamento() {
        return listaConsultaDepartamento;
    }

    public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
        this.listaConsultaDepartamento = listaConsultaDepartamento;
    }

    public String getValorConsultaDepartamento() {
        return valorConsultaDepartamento;
    }

    public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
        this.valorConsultaDepartamento = valorConsultaDepartamento;
    }

    public String getDepartamentoResponsavel_Erro() {
        return departamentoResponsavel_Erro;
    }
     
    public void setDepartamentoResponsavel_Erro(String departamentoResponsavel_Erro) {
        this.departamentoResponsavel_Erro = departamentoResponsavel_Erro;
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

    /**
     * @return the apresentarModalUnidadeEnsinoEspecifica
     */
    public String getApresentarModalUnidadeEnsinoEspecifica() {
        if (apresentarModalUnidadeEnsinoEspecifica == null) {
            apresentarModalUnidadeEnsinoEspecifica = "";
        }
        return apresentarModalUnidadeEnsinoEspecifica;
    }

    /**
     * @param apresentarModalUnidadeEnsinoEspecifica the apresentarModalUnidadeEnsinoEspecifica to set
     */
    public void setApresentarModalUnidadeEnsinoEspecifica(String apresentarModalUnidadeEnsinoEspecifica) {
        this.apresentarModalUnidadeEnsinoEspecifica = apresentarModalUnidadeEnsinoEspecifica;
    }

    public void selecionarUnidadeEnsino() throws Exception {
        UnidadeEnsinoVO unidadesPlano = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCons");
        for (UnidadeEnsinoVO unidadesPlano1 : getListaUnidades()) {
            if (unidadesPlano1.getCodigo().intValue() == unidadesPlano.getCodigo().intValue()) {
                return;
            }
        }
        getListaUnidades().add(unidadesPlano);
        consultarUnidadeEnsino();
    }

    public void removerTodasUnidadeEnsino() {
        getListaUnidades();
        setListaUnidades(null);
    }

    public void desmarcarTodos() {
        Iterator i = getListaUnidades().iterator();
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO)i.next();
            obj.setEscolhidaParaFazerCotacao(Boolean.FALSE);
        }
    }

    public void marcarTodos() {
        Iterator i = getListaUnidades().iterator();
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO)i.next();
            obj.setEscolhidaParaFazerCotacao(Boolean.TRUE);
        }
    }

    public void removerUnidadeEnsino() {
        UnidadeEnsinoVO unidadeEnsino = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsino");
        int index = 0;
        for (UnidadeEnsinoVO unidadesPlano1 : getListaUnidades()) {
            if (unidadesPlano1.getCodigo().intValue() == unidadeEnsino.getCodigo().intValue()) {
                getListaUnidades().remove(index);
                return;
            }
            index++;
        }
    }

    public Integer getColumn() {
        if (getListaUnidades().size() > 2) {
            return 2;
        }
        return getListaUnidades().size();
    }

    public Integer getElement() {
        return getListaUnidades().size();
    }

    /**
     * @return the listaUnidades
     */
    public List<UnidadeEnsinoVO> getListaUnidades() {
        if (listaUnidades == null) {
            listaUnidades = new ArrayList();
        }
        return listaUnidades;
    }

    /**
     * @param listaUnidades the listaUnidades to set
     */
    public void setListaUnidades(List listaUnidades) {
        this.listaUnidades = listaUnidades;
    }
    
    public void tramitaEntreDepartamentos() {
        if (getTipoRequerimentoVO().getTramitaEntreDepartamentos()) {
            getTipoRequerimentoVO().setDepartamentoResponsavel(new DepartamentoVO());
            getTipoRequerimentoVO().getDepartamentoResponsavel().setNome("TRAMITA ENTRE VÁRIOS DEPTOS");
        } else {
            getTipoRequerimentoVO().setDepartamentoResponsavel(new DepartamentoVO());
        }
    }

    /**
     * @return the tipoRequerimentoDepartamento
     */
    public TipoRequerimentoDepartamentoVO getTipoRequerimentoDepartamento() {
        if (tipoRequerimentoDepartamento == null) {
            tipoRequerimentoDepartamento = new TipoRequerimentoDepartamentoVO();
        }
        return tipoRequerimentoDepartamento;
    }

    /**
     * @param tipoRequerimentoDepartamento the tipoRequerimentoDepartamento to set
     */
    public void setTipoRequerimentoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamento) {
        this.tipoRequerimentoDepartamento = tipoRequerimentoDepartamento;
    }
    
    public void adicionarTipoRequerimentoDepartamentoVO() throws Exception {
        try {
            if (!getTipoRequerimentoVO().getCodigo().equals(0)) {
                getTipoRequerimentoDepartamento().setTipoRequerimento(getTipoRequerimentoVO().getCodigo());
            }
           /* if (getTipoRequerimentoDepartamento().getOrdemExecucao().equals(0)) {
                int ordem = getTipoRequerimentoVO().getTipoRequerimentoDepartamentoVOs().size() + 1;
                getTipoRequerimentoDepartamento().setOrdemExecucao(ordem);
            }*/
            if (!getTipoRequerimentoDepartamento().getResponsavelRequerimentoDepartamento().getCodigo().equals(0)) {
                getFacadeFactory().getPessoaFacade().carregarDados(getTipoRequerimentoDepartamento().getResponsavelRequerimentoDepartamento(), NivelMontarDados.TODOS, getUsuarioLogado());
            }
            if (getTipoRequerimentoDepartamento().getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO) && (getTipoRequerimentoDepartamento().getResponsavelRequerimentoDepartamento().getCodigo().equals(0) || getTipoRequerimentoDepartamento().getResponsavelRequerimentoDepartamento().getCodigo() == null) ) {
            	throw new ConsistirException("O campo FUNCIONÁRIO ESPECÍFICO (TipoRequerimentoDepartamento) deve ser informado.");
            }
            if (getTipoRequerimentoDepartamento().getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO) && (getTipoRequerimentoDepartamento().getTipoRequerimentoDepartamentoFuncionarioVOs().isEmpty()) ) {
            	throw new ConsistirException("O campo FUNCIONÁRIOS (TipoRequerimentoDepartamento) deve ser informado.");
            }
            
            if(getTipoRequerimentoDepartamento().getOrdemExecucao() != 0){
            	for (TipoRequerimentoDepartamentoVO object : getTipoRequerimentoVO().getTipoRequerimentoDepartamentoVOs()) {
					if(getTipoRequerimentoDepartamento().getOrdemExecucao().equals(object.getOrdemExecucao())){
						getTipoRequerimentoVO().adicionarTipoRequerimentoDepartamentoVOs(getTipoRequerimentoDepartamento());
						break;
					}
				}
            }else{
            	int ordem = getTipoRequerimentoVO().getTipoRequerimentoDepartamentoVOs().size() + 1;
                getTipoRequerimentoDepartamento().setOrdemExecucao(ordem);
            	getTipoRequerimentoVO().adicionarTipoRequerimentoDepartamentoVOs(getTipoRequerimentoDepartamento());
            }
            
            if(getTipoRequerimentoDepartamento().getQuestionario().getCodigo() > 0){
            	getTipoRequerimentoDepartamento().setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getTipoRequerimentoDepartamento().getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }
            getTipoRequerimentoVO().adicionarTipoRequerimentoDepartamentoVOs(getTipoRequerimentoDepartamento());
            setTipoRequerimentoDepartamento(new TipoRequerimentoDepartamentoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }    
    }
    
    public void subirNaOrdemExecucaoTipoRequerimentoDepartamentoVO() throws Exception {
        try {
            TipoRequerimentoDepartamentoVO obj = (TipoRequerimentoDepartamentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoDepartamentoItem");
            getTipoRequerimentoVO().subirNaOrdemExecucaoTipoRequerimentoDepartamentoVO(obj);
            setMensagemID("msg_acao_realizadaComSucesso");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    } 
    
    public String getCssPrazoExecucao() {
        if (getTipoRequerimentoVO().getTramitaEntreDepartamentos()) {
            return "camposSomenteLeitura";
        }
        return "camposObrigatorios";
    }
    
    public void descerNaOrdemExecucaoTipoRequerimentoDepartamentoVO() throws Exception {
        try {
            TipoRequerimentoDepartamentoVO obj = (TipoRequerimentoDepartamentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoDepartamentoItem");
            getTipoRequerimentoVO().descerNaOrdemExecucaoTipoRequerimentoDepartamentoVO(obj);
            setMensagemID("msg_acao_realizadaComSucesso");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }        
    }    

    public void editarTipoRequerimentoDepartamentoVO() throws Exception {
        TipoRequerimentoDepartamentoVO obj = (TipoRequerimentoDepartamentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoDepartamentoItem");
        TipoRequerimentoDepartamentoVO objEdicao = (TipoRequerimentoDepartamentoVO) obj.clone();
        setTipoRequerimentoDepartamento(objEdicao);
        montarListaSelectItemCargo();
    }

    public void removerTipoRequerimentoDepartamentoVO() throws Exception {
        TipoRequerimentoDepartamentoVO obj = (TipoRequerimentoDepartamentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoDepartamentoItem");
        getTipoRequerimentoVO().excluirTipoRequerimentoDepartamentoVOs(obj.getOrdemExecucao());
        setMensagemID("msg_dados_excluidos");
    }

    /**
     * @return the listaSelectItemResponsavelDepartamento
     */
    public List getListaSelectItemResponsavelDepartamento() {
        if (listaSelectItemResponsavelDepartamento == null) {
            listaSelectItemResponsavelDepartamento = new ArrayList(0);
        }
        return listaSelectItemResponsavelDepartamento;
    }

    /**
     * @param listaSelectItemResponsavelDepartamento the listaSelectItemResponsavelDepartamento to set
     */
    public void setListaSelectItemResponsavelDepartamento(List listaSelectItemResponsavelDepartamento) {
        this.listaSelectItemResponsavelDepartamento = listaSelectItemResponsavelDepartamento;
    }
    
    public List<SelectItem> getListaSelectItemTipoDistribuicaoResponsavel(){		
		return TipoDistribuicaoResponsavelEnum.getListaSelectItemTipoDistribuicaoResponsavel();
	}
    
    public List<SelectItem> getListaSelectItemTipoPoliticaDistribuicao() {		
		return TipoPoliticaDistribuicaoEnum.getListaSelectItemTipoPoliticaDistribuicao();
	}

	public List<SelectItem> getListaSelectItemCargo() {
		if(listaSelectItemCargo == null){
			listaSelectItemCargo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCargo;
	}

	public void setListaSelectItemCargo(List<SelectItem> listaSelectItemCargo) {
		this.listaSelectItemCargo = listaSelectItemCargo;
	}
	
	public void montarListaSelectItemCargo(){
		try{
			getListaSelectItemCargo().clear();
			getListaSelectItemCargo().add(new SelectItem("", ""));
			List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarPorDepartamento(getTipoRequerimentoDepartamento().getDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());			
			for(CargoVO cargoVO:cargoVOs){
				getListaSelectItemCargo().add(new SelectItem(cargoVO.getCodigo(),cargoVO.getNome()));
			}
		}catch(Exception e){
			
		}
	}
	
	public void consultarPossivelResponsavel(){
		try {
			getPossivelResponsavelRequerimentoVOs().clear();
			setPossivelResponsavelRequerimentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPossiveisResponsaveisTipoRequerimentoPorDepartamentoEUnidadeEnsino(getTipoRequerimentoDepartamento().getDepartamento().getCodigo(), getTipoRequerimentoDepartamento().getCargo().getCodigo(), getTipoRequerimentoDepartamento().getResponsavelRequerimentoDepartamento().getCodigo(), getTipoRequerimentoDepartamento().getTipoRequerimentoDepartamentoFuncionarioVOs(), getTipoRequerimentoDepartamento().getTipoDistribuicaoResponsavel(), getTipoRequerimentoVO().getUnidadeEnsinoEspecificaVOs(), null, 0));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void consultarPossivelResponsavelCons(){
		try {
			getPossivelResponsavelRequerimentoVOs().clear();
			TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO = (TipoRequerimentoDepartamentoVO) getRequestMap().get("tipoRequerimentoDepartamentoItem");
			setPossivelResponsavelRequerimentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPossiveisResponsaveisTipoRequerimentoPorDepartamentoEUnidadeEnsino(tipoRequerimentoDepartamentoVO.getDepartamento().getCodigo(), tipoRequerimentoDepartamentoVO.getCargo().getCodigo(),  tipoRequerimentoDepartamentoVO.getResponsavelRequerimentoDepartamento().getCodigo(), tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs(), tipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), getTipoRequerimentoVO().getUnidadeEnsinoEspecificaVOs(), null, null));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaSelectItemCentroReceita(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCentroReceitaPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CentroReceitaVO obj = (CentroReceitaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString() + "-" + obj.getIdentificadorCentroReceita().toString()));
            }
            setListaSelectItemCentroReceita(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCentroReceita() {
        try {
            montarListaSelectItemCentroReceita("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public void montarListaSelectItemQuestionario(){
    	setListaSelectItemQuestionario(null);
    }
    
    public void montarListaSelectItemQuestionarioDepartamento(){
    	setListaSelectItemQuestionarioDepartamento(null);
    }
    

    public List consultarCentroReceitaPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCentroReceitaFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

	private List<PossivelResponsavelRequerimentoVO> possivelResponsavelRequerimentoVOs;


	public List<PossivelResponsavelRequerimentoVO> getPossivelResponsavelRequerimentoVOs() {
		if(possivelResponsavelRequerimentoVOs == null){
			possivelResponsavelRequerimentoVOs = new ArrayList<PossivelResponsavelRequerimentoVO>(0);
		}
		return possivelResponsavelRequerimentoVOs;
	}

	public void setPossivelResponsavelRequerimentoVOs(List<PossivelResponsavelRequerimentoVO> possivelResponsavelRequerimentoVOs) {
		this.possivelResponsavelRequerimentoVOs = possivelResponsavelRequerimentoVOs;
	}

	public void montarListaSelectItemTextoPadrao() {
		setListaSelectItemTextoPadrao(null);
		getTipoRequerimentoVO().setTextoPadrao(null);
		if(getTipoRequerimentoVO().getIsEmissaoCertificado() || getTipoRequerimentoVO().getIsDeclaracao()){
			montarListaSelectItemTextoPadraoCertificadoImpresso();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemTextoPadrao() {
		if(listaSelectItemTextoPadrao == null){
			listaSelectItemTextoPadrao = new ArrayList<SelectItem>(0);
			try {
				if (getTipoRequerimentoVO().getTipo().equals("CENO")) {
					List<TextoPadraoVO> textoPadraoVOs = (List<TextoPadraoVO>) getFacadeFactory().getTextoPadraoFacade().consultaSimplesTextoPadraoFinanceiro(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					for (TextoPadraoVO textoPadraoVO : textoPadraoVOs) {
						listaSelectItemTextoPadrao.add(new SelectItem(textoPadraoVO.getCodigo(), textoPadraoVO.getDescricao()));
					}
				} else {
					List<TextoPadraoDeclaracaoVO> textoPadraoVOs = (List<TextoPadraoDeclaracaoVO>) getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
					listaSelectItemTextoPadrao.add(new SelectItem(0, ""));
					for(TextoPadraoDeclaracaoVO textoPadraoVO: textoPadraoVOs){
						if(getTipoRequerimentoVO().getIsCertificadoModular() || getTipoRequerimentoVO().getIsEmissaoCertificado()){
							if(textoPadraoVO.getTipo().equals("CE")){
								listaSelectItemTextoPadrao.add(new SelectItem(textoPadraoVO.getCodigo(), textoPadraoVO.getDescricao()));
							}
						} else {
							listaSelectItemTextoPadrao.add(new SelectItem(textoPadraoVO.getCodigo(), textoPadraoVO.getDescricao()));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaSelectItemTextoPadrao;
	}

	public void setListaSelectItemTextoPadrao(List<SelectItem> listaSelectItemTextoPadrao) {
		this.listaSelectItemTextoPadrao = listaSelectItemTextoPadrao;
	}
	
	

	public List<SelectItem> getListaSelectItemQuestionarioDepartamento() {
		if (listaSelectItemQuestionarioDepartamento == null) {
			listaSelectItemQuestionarioDepartamento = new ArrayList<SelectItem>(0);
			try {
				List<QuestionarioVO> questionarioVOs = (List<QuestionarioVO>) getFacadeFactory().getQuestionarioFacade().consultarQuestionarioRequerimentoPorEscopoRequerimento(TipoEscopoQuestionarioRequerimentoEnum.DEPARTAMENTO,  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				listaSelectItemQuestionarioDepartamento.add(new SelectItem(0, ""));
				for(QuestionarioVO questionarioVO: questionarioVOs){					
					listaSelectItemQuestionarioDepartamento.add(new SelectItem(questionarioVO.getCodigo(), questionarioVO.getDescricao()+"("+questionarioVO.getSituacao_Apresentar()+")"));					
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		return listaSelectItemQuestionarioDepartamento;
	}

	public void setListaSelectItemQuestionarioDepartamento(List<SelectItem> listaSelectItemQuestionarioDepartamento) {
		this.listaSelectItemQuestionarioDepartamento = listaSelectItemQuestionarioDepartamento;
	}

	public List<SelectItem> getListaSelectItemQuestionario() {
		if(listaSelectItemQuestionario == null){
			listaSelectItemQuestionario = new ArrayList<SelectItem>(0);
			try {
				List<QuestionarioVO> questionarioVOs = (List<QuestionarioVO>) getFacadeFactory().getQuestionarioFacade().consultarQuestionarioRequerimentoPorEscopoRequerimento(TipoEscopoQuestionarioRequerimentoEnum.REQUERENTE,  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				listaSelectItemQuestionario.add(new SelectItem(0, ""));
				for(QuestionarioVO questionarioVO: questionarioVOs){					
					listaSelectItemQuestionario.add(new SelectItem(questionarioVO.getCodigo(), questionarioVO.getDescricao()+"("+questionarioVO.getSituacao_Apresentar()+")"));					
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
		}
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}
	
    
    public List getListaSelectItemCentroReceita() {
        return listaSelectItemCentroReceita;
    }

    public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
        this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
    }
    
    public void consultarValorTaxaApresentar(){
    	try {
			getTipoRequerimentoVO().setValor(getFacadeFactory().getTaxaFacade().consultarValorTaxaAtual(getTipoRequerimentoVO().getTaxa().getCodigo()));
		} catch (Exception e) {
			getTipoRequerimentoVO().setValor(0.0);
		}
    }
    
    public void montarListaSelectItemTaxa(){
    	try{
    		List<TaxaVO> taxas = getFacadeFactory().getTaxaFacade().consultarTaxaPorSituacao("AT",  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    		getListaSelectItemTaxa().clear();
    		getListaSelectItemTaxa().add(new SelectItem(0, ""));
    		Boolean taxaAdd = false;
    		for(TaxaVO taxa :taxas){
    			if(taxa.getCodigo().equals(getTipoRequerimentoVO().getTaxa().getCodigo())){
    				taxaAdd = true;
    			}
    			getListaSelectItemTaxa().add(new SelectItem(taxa.getCodigo(), taxa.getDescricao()));
    		}
    		if(!taxaAdd && getTipoRequerimentoVO().getTaxa().getCodigo() > 0){
    			getListaSelectItemTaxa().add(new SelectItem(getTipoRequerimentoVO().getTaxa().getCodigo(), getTipoRequerimentoVO().getTaxa().getDescricao()));
    		}
    	}catch(Exception e){
    		
    	}
    }

	public List<SelectItem> getListaSelectItemTaxa() {
		if (listaSelectItemTaxa == null) {
			listaSelectItemTaxa = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTaxa;
	}

	public void setListaSelectItemTaxa(List<SelectItem> listaSelectItemTaxa) {
		this.listaSelectItemTaxa = listaSelectItemTaxa;
	}

	public List<SelectItem> getListaSelectItemTipoControleCobrancaViaRequerimento() {
		if (listaSelectItemTipoControleCobrancaViaRequerimento == null) {
			listaSelectItemTipoControleCobrancaViaRequerimento = new ArrayList<SelectItem>(0);
			listaSelectItemTipoControleCobrancaViaRequerimento.add(new SelectItem(TipoControleCobrancaViaRequerimentoEnum.PERIODO_MATRICULA, TipoControleCobrancaViaRequerimentoEnum.PERIODO_MATRICULA.getValorApresentar()));
			listaSelectItemTipoControleCobrancaViaRequerimento.add(new SelectItem(TipoControleCobrancaViaRequerimentoEnum.MENSAL, TipoControleCobrancaViaRequerimentoEnum.MENSAL.getValorApresentar()));
		}
		return listaSelectItemTipoControleCobrancaViaRequerimento;
	}

	public void setListaSelectItemTipoControleCobrancaViaRequerimento(List<SelectItem> listaSelectItemTipoControleCobrancaViaRequerimento) {
		this.listaSelectItemTipoControleCobrancaViaRequerimento = listaSelectItemTipoControleCobrancaViaRequerimento;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				if(getTipoRequerimentoVO().getNivelEducacional().equals("")) {
					objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				else {
					objs = getFacadeFactory().getCursoFacade().consultarPorCodigoNivelEducacionalUnidadeEnsino(new Integer(valorInt), getTipoRequerimentoVO().getNivelEducacional(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if(getTipoRequerimentoVO().getNivelEducacional().equals("")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, Boolean.FALSE, getUsuarioLogado());
				}
				else {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorUnidadeEnsinoPorNivelEducacional(getValorConsultaCurso(), 0,  TipoNivelEducacional.getEnum(getTipoRequerimentoVO().getNivelEducacional()), new DataModelo());
				}
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void adicionarTipoRequerimentoCurso() throws Exception {
		try {
			TipoRequerimentoCursoVO obj = new TipoRequerimentoCursoVO();
			obj.setTipoRequerimento(getTipoRequerimentoVO().getCodigo());
			obj.getCursoVO().setCodigo(getCursoVO().getCodigo());
			obj.getCursoVO().setNome(getCursoVO().getNome());
            getFacadeFactory().getTipoRequerimentoFacade().adicionarTipoRequerimentoCursoVOs(getTipoRequerimentoVO().getTipoRequerimentoCursoVOs(), obj);
            setCursoVO(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void consultarTodosCursos() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);

			if(getTipoRequerimentoVO().getNivelEducacional().equals("")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, Boolean.FALSE, getUsuarioLogado());
			}
			else {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorUnidadeEnsinoPorNivelEducacional(getValorConsultaCurso(), 0, TipoNivelEducacional.getEnum(getTipoRequerimentoVO().getNivelEducacional()), new DataModelo());
			}			

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void adicionarTipoRequerimentoTodosCursos() throws Exception {
		try {
			getTipoRequerimentoVO().getTipoRequerimentoCursoVOs().clear();
			if(!Uteis.isAtributoPreenchido(getListaConsultaCurso())) {
				consultarTodosCursos();	
			}
			if(Uteis.isAtributoPreenchido(getListaConsultaCurso())) {
				for(CursoVO cursoVO : getListaConsultaCurso()) {
					TipoRequerimentoCursoVO obj = new TipoRequerimentoCursoVO();
					obj.setTipoRequerimento(getTipoRequerimentoVO().getCodigo());
					obj.getCursoVO().setCodigo(cursoVO.getCodigo());
					obj.getCursoVO().setNome(cursoVO.getNome());
					getTipoRequerimentoVO().getTipoRequerimentoCursoVOs().add(obj);
				}
			}			
			
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void selecionarCurso() {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		setCursoVO(obj);
		listaConsultaCurso.clear();
		this.setValorConsultaCurso("");
		this.setCampoConsultaCurso("");
	}
	
	public void removerTipoRequerimentoCurso() throws Exception {
		TipoRequerimentoCursoVO obj = (TipoRequerimentoCursoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoCursoItem");
		getFacadeFactory().getTipoRequerimentoFacade().removerTipoRequerimentoCursoVOs(getTipoRequerimentoVO().getTipoRequerimentoCursoVOs(), obj);
		setMensagemID("msg_dados_excluidos");
	}
	
	
	
	
	
	public void montarListaTipoRequerimentoTransferenciaInternaCurso()  {
		try {
			getTipoRequerimentoVO().validarTipoRequerimentoTrasferenciaInterna();		
			TipoRequerimentoCursoVO obj = (TipoRequerimentoCursoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoCursoItem");
			setTipoRequerimentoCursoVO(new TipoRequerimentoCursoVO());		
			setTipoRequerimentoCursoVO(obj);
			getListaConsultaCursoTransferenciaInterna().clear();
			setValorConsultaCursoTransferenciaInterna("");
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	public void adicionarCursoTranferenciaTipoRequerimentoCurso()  {
		try {
			getTipoRequerimentoVO().validarTipoRequerimentoTrasferenciaInterna();
			CursoVO curso = (CursoVO) context().getExternalContext().getRequestMap().get("cursoTransferenciaInternaItem");		
			TipoRequerimentoCursoTransferenciaInternaCursoVO obj = new TipoRequerimentoCursoTransferenciaInternaCursoVO();	
			obj.getCursoVO().setCodigo(curso.getCodigo());
			obj.getCursoVO().setNome(curso.getNome());
			obj.setTipoRequerimentoCursoVO(getTipoRequerimentoCursoVO());
			getFacadeFactory().getTipoRequerimentoCursoFacade().adicionarTipoRequerimentoTransferenciaInternaCursoVOs(getTipoRequerimentoCursoVO().getListaTipoRequerimentoTransferenciaCursoVOs(), obj);
			setCursoVO(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}
	
	
	
	public void consultarCursoTransferenciaInterna() {
		try {
			getTipoRequerimentoVO().validarTipoRequerimentoTrasferenciaInterna();
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCursoTransferenciaInterna().equals("codigo")) {
				if (getCampoConsultaCursoTransferenciaInterna().equals("")) {
					setValorConsultaCursoTransferenciaInterna("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCursoTransferenciaInterna());
				if(getTipoRequerimentoVO().getNivelEducacional().equals("")) {
					objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				else {
					objs = getFacadeFactory().getCursoFacade().consultarPorCodigoNivelEducacionalUnidadeEnsino(new Integer(valorInt), getTipoRequerimentoVO().getNivelEducacional(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
			}
			if (getCampoConsultaCursoTransferenciaInterna().equals("nome")) {
				if(getTipoRequerimentoVO().getNivelEducacional().equals("")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCursoTransferenciaInterna(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, Boolean.FALSE, getUsuarioLogado());
				}
				else {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorUnidadeEnsinoPorNivelEducacional(getValorConsultaCursoTransferenciaInterna(), 0, TipoNivelEducacional.getEnum(getTipoRequerimentoVO().getNivelEducacional()), new DataModelo());
				}
			}

			setListaConsultaCursoTransferenciaInterna(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	
	public void consultarTodosCursosTransferenciaInterna() {
		try {
			getTipoRequerimentoVO().validarTipoRequerimentoTrasferenciaInterna();
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if(getTipoRequerimentoVO().getNivelEducacional().equals("")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCursoTransferenciaInterna(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, Boolean.FALSE, getUsuarioLogado());
			}
			else {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorUnidadeEnsinoPorNivelEducacional(getValorConsultaCursoTransferenciaInterna(), 0, TipoNivelEducacional.getEnum(getTipoRequerimentoVO().getNivelEducacional()), new DataModelo());
			}
			setListaConsultaCursoTransferenciaInterna(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCursoTransferenciaInterna(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void adicionarTipoRequerimentoTransferenciaInternaTodosCursos()  {
		try {
			getTipoRequerimentoVO().validarTipoRequerimentoTrasferenciaInterna();
			getTipoRequerimentoCursoVO().getListaTipoRequerimentoTransferenciaCursoVOs().clear();
			if(!Uteis.isAtributoPreenchido(getListaConsultaCursoTransferenciaInterna())) {
				consultarTodosCursosTransferenciaInterna();	
			}
			if(Uteis.isAtributoPreenchido(getListaConsultaCursoTransferenciaInterna())) {
				for(CursoVO cursoVO : getListaConsultaCursoTransferenciaInterna()) {
					TipoRequerimentoCursoTransferenciaInternaCursoVO obj = new TipoRequerimentoCursoTransferenciaInternaCursoVO();
					obj.setTipoRequerimentoCursoVO(getTipoRequerimentoCursoVO());
					obj.getCursoVO().setCodigo(cursoVO.getCodigo());
					obj.getCursoVO().setNome(cursoVO.getNome());
					getTipoRequerimentoCursoVO().getListaTipoRequerimentoTransferenciaCursoVOs().add(obj);
				}
			}			
			
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	

	
	public void removerTipoRequerimentoTransferenciaInternaCurso()  {
		try {
			getTipoRequerimentoVO().validarTipoRequerimentoTrasferenciaInterna();
			TipoRequerimentoCursoTransferenciaInternaCursoVO obj = (TipoRequerimentoCursoTransferenciaInternaCursoVO) context().getExternalContext().getRequestMap().get("cursoTransferenciaInternaItemSelecionados");
			getFacadeFactory().getTipoRequerimentoCursoFacade().removerTipoRequerimentoTransferenciaInternaCursoVOs(getTipoRequerimentoCursoVO().getListaTipoRequerimentoTransferenciaCursoVOs(), obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
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
	
	public void consultarFuncionarioResponsavel() {
		try {
			getListaConsultaFuncionario().clear();
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), 0, "", 0, null, true, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, 0, null, true, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), 0, "", 0, null, true, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, 0, null, true, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, null, true, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, null, true, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioResponsavel() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		if(getTipoRequerimentoDepartamento().getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO)) {
			getTipoRequerimentoDepartamentoFuncionarioVO().setFuncionarioVO(obj);
		}else {
		getTipoRequerimentoDepartamento().setResponsavelRequerimentoDepartamento(obj.getPessoa());
	}
	}

	public void limparDadosFuncionarioResponsavel() {
		getTipoRequerimentoDepartamentoFuncionarioVO().setFuncionarioVO(null);
		getTipoRequerimentoDepartamento().setResponsavelRequerimentoDepartamento(null);
	}

	/**
	 * @return the campoConsultaFuncionario
	 */
	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	/**
	 * @param campoConsultaFuncionario
	 *            the campoConsultaFuncionario to set
	 */
	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	/**
	 * @return the valorConsultaFuncionario
	 */
	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	/**
	 * @param valorConsultaFuncionario
	 *            the valorConsultaFuncionario to set
	 */
	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	/**
	 * @return the listaConsultaFuncionario
	 */
	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>();
		}
		return listaConsultaFuncionario;
	}

	/**
	 * @param listaConsultaFuncionario
	 *            the listaConsultaFuncionario to set
	 */
	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
			tipoConsultaComboFuncionario.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		}
		return tipoConsultaComboFuncionario;
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadosTipoRequerimentoCurso() {
	 	TipoRequerimentoCursoVO obj = (TipoRequerimentoCursoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoCursoItem");
	 	setCursoVO(obj.getCursoVO());
	}

	public void adicionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem"));
			getTurmaVO().getCurso().setCodigo(getCursoVO().getCodigo());
			getTurmaVO().getCurso().setNome(getCursoVO().getNome());
			TipoRequerimentoTurmaVO tipoRequerimentoTurmaIncluirVO = inicializarDadosTipoRequerimentoTurma(getTurmaVO());
			getFacadeFactory().getTipoRequerimentoCursoFacade().adicionarTipoRequerimentoTurma(getTipoRequerimentoVO().getTipoRequerimentoCursoVOs(), tipoRequerimentoTurmaIncluirVO, getUsuarioLogado());
			setValorConsultaTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public TipoRequerimentoTurmaVO inicializarDadosTipoRequerimentoTurma(TurmaVO turmaVO) {
		TipoRequerimentoTurmaVO obj = new TipoRequerimentoTurmaVO();
		obj.getTurmaVO().setCodigo(turmaVO.getCodigo());
		obj.getTurmaVO().setIdentificadorTurma(turmaVO.getIdentificadorTurma());
		obj.getTipoRequerimentoCursoVO().getCursoVO().setCodigo(turmaVO.getCurso().getCodigo());
		obj.getTipoRequerimentoCursoVO().getCursoVO().setNome(turmaVO.getCurso().getNome());
		
		return obj;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			setValorConsultaTurma(null);
			setListaConsultaTurmaVOs(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerTipoRequerimentoTurma() {
		TipoRequerimentoTurmaVO obj = (TipoRequerimentoTurmaVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoTurmaItem");
		getFacadeFactory().getTipoRequerimentoCursoFacade().removerTipoRequerimentoTurma(getTipoRequerimentoVO().getTipoRequerimentoCursoVOs(), obj.getTurmaVO(), getUsuarioLogado());
		setMensagemID("msg_dados_excluidos");
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

	public List<TurmaVO> getListaConsultaTurmaVOs() {
		if (listaConsultaTurmaVOs == null) {
			listaConsultaTurmaVOs = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurmaVOs;
	}

	public void setListaConsultaTurmaVOs(List<TurmaVO> listaConsultaTurmaVOs) {
		this.listaConsultaTurmaVOs = listaConsultaTurmaVOs;
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

	public Boolean getPossuiRequerimentoVinculado() {
		if (possuiRequerimentoVinculado == null) {
			possuiRequerimentoVinculado = false;
		}
		return possuiRequerimentoVinculado;
	}

	public void setPossuiRequerimentoVinculado(Boolean possuiRequerimentoVinculado) {
		this.possuiRequerimentoVinculado = possuiRequerimentoVinculado;
	}

	public List<SelectItem> getListaSelectItemTipoArquivoUpload() {
		if (listaSelectItemTipoArquivoUpload == null) {
			listaSelectItemTipoArquivoUpload = new ArrayList<SelectItem>(0);
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.AUDIO,TipoUploadArquivoEnum.AUDIO.getValorApresentar()));
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.DOCUMENTO,TipoUploadArquivoEnum.DOCUMENTO.getValorApresentar()));
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.IMAGEM,TipoUploadArquivoEnum.IMAGEM.getValorApresentar()));
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.TODOS,TipoUploadArquivoEnum.TODOS.getValorApresentar()));
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.VIDEO,TipoUploadArquivoEnum.VIDEO.getValorApresentar()));
		}
		return listaSelectItemTipoArquivoUpload;
	}

	public void setListaSelectItemTipoArquivoUpload(List<SelectItem> listaSelectItemTipoArquivoUpload) {
		this.listaSelectItemTipoArquivoUpload = listaSelectItemTipoArquivoUpload;
	}

	public void alterarExtensaoArquivo() {
		getTipoRequerimentoVO().setExtensaoArquivo(getTipoRequerimentoVO().getTipoUploadArquivo().getExtensao());
	}
		
	public void clonar() {
		try {
			setTipoRequerimentoVO((TipoRequerimentoVO) getTipoRequerimentoVO().clone());
			getTipoRequerimentoVO().setCodigo(0);
			getTipoRequerimentoVO().setNovoObj(true);
		    setPossuiRequerimentoVinculado(false);
			getTipoRequerimentoVO().setNome(getTipoRequerimentoVO().getNome() + " - CLONE");
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public TipoRequerimentoDepartamentoFuncionarioVO getTipoRequerimentoDepartamentoFuncionarioVO() {
		if (tipoRequerimentoDepartamentoFuncionarioVO == null) {
			tipoRequerimentoDepartamentoFuncionarioVO = new TipoRequerimentoDepartamentoFuncionarioVO();
		}
		return tipoRequerimentoDepartamentoFuncionarioVO;
	}

	public void setTipoRequerimentoDepartamentoFuncionarioVO(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO) {
		this.tipoRequerimentoDepartamentoFuncionarioVO = tipoRequerimentoDepartamentoFuncionarioVO;
	}
	
	public void adicionarTipoRequerimentoDepartamentoFuncionario() {
		try {
			FuncionarioVO funcionarioVO = (FuncionarioVO) getTipoRequerimentoDepartamentoFuncionarioVO().getFuncionarioVO().clone();
			//getTipoRequerimentoDepartamento().getTipoRequerimentoDepartamentoFuncionarioVOs().clear();
			for (UnidadeEnsinoVO unidadeEnsinoVO : obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs())) {
					getTipoRequerimentoDepartamentoFuncionarioVO().setFuncionarioVO(funcionarioVO);
					getTipoRequerimentoDepartamentoFuncionarioVO().setUnidadeEnsinoVO(unidadeEnsinoVO);
					getFacadeFactory().getTipoRequerimentoDepartamentoFacade().adicionarTipoRequerimentoDepartamentoFuncionario(getTipoRequerimentoDepartamento(), getTipoRequerimentoDepartamentoFuncionarioVO());
					setTipoRequerimentoDepartamentoFuncionarioVO(new TipoRequerimentoDepartamentoFuncionarioVO());
			}
			
			getTipoRequerimentoDepartamentoFuncionarioVO().setFuncionarioVO((FuncionarioVO)funcionarioVO.clone());
			Ordenacao.ordenarLista(getTipoRequerimentoDepartamento().getTipoRequerimentoDepartamentoFuncionarioVOs(), "ordenacao");
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarTipoRequerimentoDepartamentoFuncionario() throws Exception {
		setTipoRequerimentoDepartamentoFuncionarioRemoverVO((TipoRequerimentoDepartamentoFuncionarioVO) getRequestMap().get("tipoRequerimentoDepartamentoFuncionarioVOItens"));
		setRemoverFuncionarioOutraUnidade(true);	
		montaListaUnidadeEnsinoVORemover();
		setFuncionarioRemover(tipoRequerimentoDepartamentoFuncionarioRemoverVO.getFuncionarioVO());

	}
	
	public void removerTipoRequerimentoDepartamentoFuncionario() {
		try {
			
			List<UnidadeEnsinoVO> listaUnidadeEnsinoRemover = new ArrayList<UnidadeEnsinoVO>(0);
			listaUnidadeEnsinoRemover = obterListaUnidadeEnsinoSelecionadaRemover(getUnidadeEnsinoVOsRemover());
			if(listaUnidadeEnsinoRemover.isEmpty()) {
				throw new Exception("Selecione uma Unidade de Ensino para Remoção.");
			}
			getFacadeFactory().getTipoRequerimentoDepartamentoFacade().removerTipoRequerimentoDepartamentoFuncionario(getTipoRequerimentoDepartamento(), getTipoRequerimentoDepartamentoFuncionarioRemoverVO(), listaUnidadeEnsinoRemover);
			
			for (UnidadeEnsinoVO unidadeEnsinoRemover : listaUnidadeEnsinoRemover) {
				
				for (UnidadeEnsinoVO unidadeEnsinoVORemover : getUnidadeEnsinoVOsRemover()) {
					if(unidadeEnsinoRemover.getCodigo().equals(unidadeEnsinoVORemover.getCodigo())) {
						getUnidadeEnsinoVOsRemover().remove(unidadeEnsinoVORemover);
						break;
					}
					
				}
			}
			verificarTodasUnidadesSelecionadas();
			
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void inicializarListaUnidadeEnsinoFuncionarioTramite() {
		try {
			getListaSelectItemUnidadeEnsinoFuncionarioTramite().clear();
			if (getTipoRequerimentoVO().getUnidadeEnsinoEspecifica()) {
				getListaSelectItemUnidadeEnsinoFuncionarioTramite().add(new SelectItem(0, "Todas"));
				for(UnidadeEnsinoVO unidadeEnsinoVO: getListaUnidades()) {
					if(unidadeEnsinoVO.getEscolhidaParaFazerCotacao()) {
						getListaSelectItemUnidadeEnsinoFuncionarioTramite().add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
					}
				}
			} else {
				if(getListaUnidades().isEmpty()) {
					setListaUnidades(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
				}
				setListaSelectItemUnidadeEnsinoFuncionarioTramite(UtilSelectItem.getListaSelectItem(getListaUnidades(), "codigo", "nome", false, false));		
				getListaSelectItemUnidadeEnsinoFuncionarioTramite().add(0, new SelectItem(0, "Todas"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoFuncionarioTramite() {
		if (listaSelectItemUnidadeEnsinoFuncionarioTramite == null) {
			listaSelectItemUnidadeEnsinoFuncionarioTramite = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsinoFuncionarioTramite;
	}

	public void setListaSelectItemUnidadeEnsinoFuncionarioTramite(List<SelectItem> listaSelectItemUnidadeEnsinoFuncionarioTramite) {
		this.listaSelectItemUnidadeEnsinoFuncionarioTramite = listaSelectItemUnidadeEnsinoFuncionarioTramite;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoFuncionarioTramite() {
		if (unidadeEnsinoFuncionarioTramite == null) {
			unidadeEnsinoFuncionarioTramite = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFuncionarioTramite;
	}

	public void setUnidadeEnsinoFuncionarioTramite(UnidadeEnsinoVO unidadeEnsinoFuncionarioTramite) {
		this.unidadeEnsinoFuncionarioTramite = unidadeEnsinoFuncionarioTramite;
	}

	private Boolean removerFuncionarioOutraUnidade;
	private TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioRemoverVO;

	public Boolean getRemoverFuncionarioOutraUnidade() {
		if (removerFuncionarioOutraUnidade == null) {
			removerFuncionarioOutraUnidade = false;
		}
		return removerFuncionarioOutraUnidade;
	}

	public void setRemoverFuncionarioOutraUnidade(Boolean removerFuncionarioOutraUnidade) {
		this.removerFuncionarioOutraUnidade = removerFuncionarioOutraUnidade;
	}

	public TipoRequerimentoDepartamentoFuncionarioVO getTipoRequerimentoDepartamentoFuncionarioRemoverVO() {
		if (tipoRequerimentoDepartamentoFuncionarioRemoverVO == null) {
			tipoRequerimentoDepartamentoFuncionarioRemoverVO = new TipoRequerimentoDepartamentoFuncionarioVO();
		}
		return tipoRequerimentoDepartamentoFuncionarioRemoverVO;
	}

	public void setTipoRequerimentoDepartamentoFuncionarioRemoverVO(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioRemoverVO) {
		this.tipoRequerimentoDepartamentoFuncionarioRemoverVO = tipoRequerimentoDepartamentoFuncionarioRemoverVO;
	}

	public List<SelectItem> getListaSelectItemSituacaoRequerimentoDepartamento() {
		if (listaSelectItemSituacaoRequerimentoDepartamento == null) {
			listaSelectItemSituacaoRequerimentoDepartamento = new ArrayList<SelectItem>(0);			
		}
		return listaSelectItemSituacaoRequerimentoDepartamento;
	}

	public void setListaSelectItemSituacaoRequerimentoDepartamento(List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamento) {
		this.listaSelectItemSituacaoRequerimentoDepartamento = listaSelectItemSituacaoRequerimentoDepartamento;
	}

	public SituacaoRequerimentoDepartamentoVO getSituacaoRequerimentoDepartamentoVO() {
		if (situacaoRequerimentoDepartamentoVO == null) {
			situacaoRequerimentoDepartamentoVO = new SituacaoRequerimentoDepartamentoVO();
		}
		return situacaoRequerimentoDepartamentoVO;
	}

	public void setSituacaoRequerimentoDepartamentoVO(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) {
		this.situacaoRequerimentoDepartamentoVO = situacaoRequerimentoDepartamentoVO;
	}

	public TipoRequerimentoSituacaoDepartamentoVO getTipoRequerimentoSituacaoDepartamentoVO() {
		if (tipoRequerimentoSituacaoDepartamentoVO == null) {
			tipoRequerimentoSituacaoDepartamentoVO = new TipoRequerimentoSituacaoDepartamentoVO();
		}
		return tipoRequerimentoSituacaoDepartamentoVO;
	}

	public void setTipoRequerimentoSituacaoDepartamentoVO(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO) {
		this.tipoRequerimentoSituacaoDepartamentoVO = tipoRequerimentoSituacaoDepartamentoVO;
	}
	
	public void inicializarListaSelectItemSituacaoRequerimentoDepartamento() {
		try {			
			setListaSelectItemSituacaoRequerimentoDepartamento(UtilSelectItem.getListaSelectItem(getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorSituacao("", getUsuarioLogado()), "codigo", "situacao", false, false));
		}catch (Exception e) {
			
		}
	} 
	
	public void novoSituacaoRequerimentoDepartamento() {
		limparMensagem();
		setOncompleteModal("");
		setSituacaoRequerimentoDepartamentoVO(new SituacaoRequerimentoDepartamentoVO());
	}
	
	public void editarSituacaoRequerimentoDepartamento() {
		try {
			novoSituacaoRequerimentoDepartamento();
			setSituacaoRequerimentoDepartamentoVO(getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorChavePrimaria(getTipoRequerimentoSituacaoDepartamentoVO().getSituacaoRequerimentoDepartamentoVO().getCodigo(), getUsuarioLogado()));
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void gravarSituacaoRequerimentoDepartamento() {
		try {
			setOncompleteModal("");
			if (getSituacaoRequerimentoDepartamentoVO().isNovoObj()) {
				getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().incluir(getSituacaoRequerimentoDepartamentoVO(), getUsuarioLogado());
				getTipoRequerimentoSituacaoDepartamentoVO().setSituacaoRequerimentoDepartamentoVO(getSituacaoRequerimentoDepartamentoVO());
				getFacadeFactory().getTipoRequerimentoDepartamentoFacade().adicionarTipoRequerimentoSituacaoDepartamento(getTipoRequerimentoDepartamento(), getTipoRequerimentoSituacaoDepartamentoVO());
				getListaSelectItemSituacaoRequerimentoDepartamento().add(new SelectItem(getSituacaoRequerimentoDepartamentoVO().getCodigo(), getSituacaoRequerimentoDepartamentoVO().getSituacao()));
				getListaSelectItemSituacaoRequerimentoDepartamento().sort(new Comparator<SelectItem>() {
					@Override
					public int compare(SelectItem o1, SelectItem o2) {						
						return o1.getLabel().compareTo(o2.getLabel());
					}				
				});				
			} else {
				getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().alterar(getSituacaoRequerimentoDepartamentoVO(), getUsuarioLogado());
				for(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO: getTipoRequerimentoDepartamento().getTipoRequerimentoSituacaoDepartamentoVOs()) {
					if(tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo().equals(getSituacaoRequerimentoDepartamentoVO().getCodigo())) {
						tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().setSituacao(getSituacaoRequerimentoDepartamentoVO().getSituacao());
						break;
					}
				}
				for(SelectItem item : getListaSelectItemSituacaoRequerimentoDepartamento()) {
					if(item.getValue().equals(getSituacaoRequerimentoDepartamentoVO().getCodigo())) {
						item.setLabel(getSituacaoRequerimentoDepartamentoVO().getSituacao());
						break;
					}
				}
			}
			setTipoRequerimentoSituacaoDepartamentoVO(new TipoRequerimentoSituacaoDepartamentoVO());	
			getTipoRequerimentoSituacaoDepartamentoVO().getSituacaoRequerimentoDepartamentoVO().setCodigo(getSituacaoRequerimentoDepartamentoVO().getCodigo());
			setSituacaoRequerimentoDepartamentoVO(new SituacaoRequerimentoDepartamentoVO());
			setOncompleteModal("RichFaces.$('panelSituacaoRequerimentoDepartamento').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarTipoRequerimentoSituacaoDepartamento() {
		try {
			getFacadeFactory().getTipoRequerimentoDepartamentoFacade().adicionarTipoRequerimentoSituacaoDepartamento(getTipoRequerimentoDepartamento(), getTipoRequerimentoSituacaoDepartamentoVO());
			setTipoRequerimentoSituacaoDepartamentoVO(new TipoRequerimentoSituacaoDepartamentoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerTipoRequerimentoSituacaoDepartamento() {
		try {
			getFacadeFactory().getTipoRequerimentoDepartamentoFacade().removerTipoRequerimentoSituacaoDepartamento(getTipoRequerimentoDepartamento(), (TipoRequerimentoSituacaoDepartamentoVO) getRequestMap().get("tipoRequerimentoSituacaoDepartamentoItem"));			
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacao.add(new SelectItem("AT", "Ativo"));
			listaSelectItemSituacao.add(new SelectItem("IN", "Inativo"));
			listaSelectItemSituacao.add(new SelectItem("TO", "Todas"));
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AT";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public void ativar() {
        try {
            tipoRequerimentoVO.setSituacao("AT");
            getFacadeFactory().getTipoRequerimentoFacade().realizarAtivacaoTipoRequerimento(tipoRequerimentoVO, getUsuarioLogado());
            setMensagemID("msg_dados_inativado");
        } catch (Exception e) {
        	tipoRequerimentoVO.setSituacao("IN");
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	@PostConstruct
	public void consultarUnidadeEnsinoFuncionarioTramite() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("TipoRequerimentoControle");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
		
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void limparUnidadeEnsino(){
		super.limparUnidadeEnsinos();
	}
	
	public List<UnidadeEnsinoVO> obterListaUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		unidadeEnsinoVOs.forEach(obj->{
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOsRemover() {
		if (unidadeEnsinoVOsRemover == null) {
			unidadeEnsinoVOsRemover = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return unidadeEnsinoVOsRemover;
	}

	public void setUnidadeEnsinoVOsRemover(List<UnidadeEnsinoVO> unidadeEnsinoVOsRemover) {
		this.unidadeEnsinoVOsRemover = unidadeEnsinoVOsRemover;
	}
	

	public void marcarTodasUnidadesEnsinoActionRemover() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOsRemover()) {
			if (getMarcarTodasUnidadeEnsinoRemover()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
	}
	
	
	public List<UnidadeEnsinoVO> obterListaUnidadeEnsinoSelecionadaRemover(List<UnidadeEnsinoVO> unidadeEnsinoVOsRemover) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		unidadeEnsinoVOsRemover.forEach(obj->{
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public Boolean getMarcarTodasUnidadeEnsinoRemover() {
		if (marcarTodasUnidadeEnsinoRemover == null) {
			marcarTodasUnidadeEnsinoRemover = Boolean.FALSE;
		}
		return marcarTodasUnidadeEnsinoRemover;
	}

	public void setMarcarTodasUnidadeEnsinoRemover(Boolean marcarTodasUnidadeEnsinoRemover) {
		this.marcarTodasUnidadeEnsinoRemover = marcarTodasUnidadeEnsinoRemover;
	}
	
	public void montaListaUnidadeEnsinoVORemover() throws Exception {
		
		getUnidadeEnsinoVOsRemover().clear();
		
		for (TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO : getTipoRequerimentoDepartamento().getTipoRequerimentoDepartamentoFuncionarioVOs()) {
			if(tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getCodigo().equals(getTipoRequerimentoDepartamentoFuncionarioRemoverVO().getFuncionarioVO().getCodigo())) {
				unidadeEnsinoVOsRemover.add((UnidadeEnsinoVO) tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().clone());	
			}
		}
	
		for (UnidadeEnsinoVO unidadeEnsinoRemover : getUnidadeEnsinoVOsRemover()) {
			if(!getTipoRequerimentoDepartamentoFuncionarioRemoverVO().getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsinoRemover.getCodigo())) {
				unidadeEnsinoRemover.setFiltrarUnidadeEnsino(false);
			}
			else {
				unidadeEnsinoRemover.setFiltrarUnidadeEnsino(true);
			}
		}
		
	}
	
	public List montarListaTipoLayoutHistorico() {
		List itens = new ArrayList<>(0);
		try {
			//validarDadosNivelEducacional();
			itens = getListaTipoLayoutHistorico(getTipoRequerimentoVO().getLayoutHistoricoApresentar(), getTipoRequerimentoVO().getNivelEducacional());
			getTipoRequerimentoVO().setLayoutHistoricoApresentar(getTipoLayoutHistorico());
			setListaTipoLayout(itens);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return itens;
	}
	
	public void validarDadosNivelEducacional() throws Exception {
		if (getTipoRequerimentoVO().getNivelEducacional().equals("")) {
			throw new Exception("O campo NÍVEL EDUCACIONAL deve ser informado.");
		}
	}

	
	public void montarListaSelectItemNivelEducacionalCurso() throws Exception {
		getListaNivelEducacional().clear();
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			if (!item.getValue().equals("SE")) {
				getListaNivelEducacional().add(item);
			}
		}
	}

	public List getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList(0);
		}
		return listaNivelEducacional;
	}

	public void setListaNivelEducacional(List listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public List getListaTipoLayout() {
		if (listaTipoLayout == null) {
			listaTipoLayout = new ArrayList(0);
		}
		return listaTipoLayout;
	}

	public void setListaTipoLayout(List listaTipoLayout) {
		this.listaTipoLayout = listaTipoLayout;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoHistorico() {
		if (this.getMarcarTodasSituacoesHistorico()) {
			getTipoRequerimentoVO().realizarMarcarTodasSituacoesHistorico();;
		} else {
			getTipoRequerimentoVO().realizarDesmarcarTodasSituacoesHistorico();
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosFormacaoHistorico() {
		if (this.getMarcarTodasSituacoesHistorico()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public Boolean getMarcarTodasSituacoesHistorico() {
		if (marcarTodasSituacoesHistorico == null) {
			marcarTodasSituacoesHistorico = false;
		}
		return marcarTodasSituacoesHistorico;
	}

	public void setMarcarTodasSituacoesHistorico(Boolean marcarTodasSituacoesHistorico) {
		this.marcarTodasSituacoesHistorico = marcarTodasSituacoesHistorico;
	}
	
	public void montarListaSelectItemTextoPadraoCertificadoImpresso() {
		setListaSelectItemTextoPadraoCertificadoImpresso(null);
		getTipoRequerimentoVO().setCertificadoImpresso(null);
	}
	
	public List<SelectItem> getListaSelectItemTextoPadraoCertificadoImpresso() {
		if(listaSelectItemTextoPadraoCertificadoImpresso == null){
			listaSelectItemTextoPadraoCertificadoImpresso = new ArrayList<SelectItem>(0);
			try {
				List<TextoPadraoDeclaracaoVO> textoPadraoVOs = (List<TextoPadraoDeclaracaoVO>) getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				listaSelectItemTextoPadraoCertificadoImpresso.add(new SelectItem(0, ""));
				for(TextoPadraoDeclaracaoVO textoPadraoVO: textoPadraoVOs){
					if(getTipoRequerimentoVO().getIsEmissaoCertificado()){
						if(textoPadraoVO.getTipo().equals("CE")){
							listaSelectItemTextoPadraoCertificadoImpresso.add(new SelectItem(textoPadraoVO.getCodigo(), textoPadraoVO.getDescricao()));
						}					}	
					if(getTipoRequerimentoVO().getIsDeclaracao()) {
						listaSelectItemTextoPadraoCertificadoImpresso.add(new SelectItem(textoPadraoVO.getCodigo(), textoPadraoVO.getDescricao()));
					}
				}				
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}

		return listaSelectItemTextoPadraoCertificadoImpresso;
	}

	public void setListaSelectItemTextoPadraoCertificadoImpresso(
			List<SelectItem> listaSelectItemTextoPadraoCertificadoImpresso) {
		this.listaSelectItemTextoPadraoCertificadoImpresso = listaSelectItemTextoPadraoCertificadoImpresso;
	}
	
	public List<TipoDocumentoVO> getTipoDocumentoVOs() {
		if (tipoDocumentoVOs == null) {
			tipoDocumentoVOs = new ArrayList<TipoDocumentoVO>(0);
		}
		return tipoDocumentoVOs;
	}

	public void setTipoDocumentoVOs(List<TipoDocumentoVO> tipoDocumentoVOs) {
		this.tipoDocumentoVOs = tipoDocumentoVOs;
	}
	
	public void verificarTodosTiposDocumentoSelecionados() {
		StringBuilder tipoDocumento = new StringBuilder();
		if (getTipoDocumentoVOs().size() > 1) {
			for (TipoDocumentoVO obj : getTipoDocumentoVOs()) {
				if (obj.getFiltrarTipoDocumento()) {
					tipoDocumento.append(obj.getNome()).append("; ");
				} 
			}
			getTipoDocumentoVO().setNome(tipoDocumento.toString());
		} else {
			if (!getTipoDocumentoVOs().isEmpty()) {
				if (getTipoDocumentoVOs().get(0).getFiltrarTipoDocumento()) {
					getTipoDocumentoVO().setNome(getTipoDocumentoVOs().get(0).getNome());
				}
			} else {
				getTipoDocumentoVO().setNome(tipoDocumento.toString());
			}
		}
		
	}

	public void marcarTodosTiposDocumentoAction() {
		for (TipoDocumentoVO tipoDocumento : getTipoDocumentoVOs()) {
			if (getMarcarTodosTiposDocumento()) {
				tipoDocumento.setFiltrarTipoDocumento(Boolean.TRUE);
			} else {
				tipoDocumento.setFiltrarTipoDocumento(Boolean.FALSE);
			}
		}
		verificarTodosTiposDocumentoSelecionados();
	}
	

	public List<TipoDocumentoVO> obterListaTipoDocumentoSelecionado(List<TipoDocumentoVO> tipoDocumentoVOs) {
		List<TipoDocumentoVO> objs = new ArrayList<TipoDocumentoVO>(0);
		tipoDocumentoVOs.forEach(obj->{
			if (obj.getFiltrarTipoDocumento()) {
				objs.add(obj);
			}
		});
		if(getTipoRequerimentoVO().getVerificarPendenciaDocumentacao() && objs.isEmpty()) {
			objs.addAll(tipoDocumentoVOs);
		}
		return objs;
	}

	public TipoDocumentoVO getTipoDocumentoVO() {
		if(tipoDocumentoVO == null) {
			tipoDocumentoVO = new TipoDocumentoVO();
		}
		return tipoDocumentoVO;
	}

	public void setTipoDocumentoVO(TipoDocumentoVO tipoDocumentoVO) {
		this.tipoDocumentoVO = tipoDocumentoVO;
	}

	public Boolean getMarcarTodosTiposDocumento() {
		if(marcarTodosTiposDocumento == null) {
			marcarTodosTiposDocumento = Boolean.FALSE;
		}
		return marcarTodosTiposDocumento;
	}

	public void setMarcarTodosTiposDocumento(Boolean marcarTodosTiposDocumento) {
		this.marcarTodosTiposDocumento = marcarTodosTiposDocumento;
	}
	
	
	public void limparTiposDocumento(){
		setMarcarTodosTiposDocumento(false);
		marcarTodosTiposDocumentoAction();
	}
	
	public void consultarTipoDocumento() {
		try {
			getTipoDocumentoVOs().clear();			

			setTipoDocumentoVOs(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			
			if(!getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs().isEmpty()) {
				for (PendenciaTipoDocumentoTipoRequerimentoVO pendenciaTipoDocumentoTipoRequerimentoVO : getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs()) {
					 getTipoDocumentoVOs()
					 .stream()
					 .filter(tipoDocumento-> tipoDocumento.getCodigo().equals(pendenciaTipoDocumentoTipoRequerimentoVO.getTipoDocumento().getCodigo()))
					 .forEach(tipoDocumento-> tipoDocumento.setFiltrarTipoDocumento(true));
				}
			}

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTipoDocumentoVOs(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaPendenciaTipoDocumentoTipoRequerimento() {      	       	
            for (TipoDocumentoVO tipoDocumento : obterListaTipoDocumentoSelecionado(getTipoDocumentoVOs())) {
            	if(!getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs().isEmpty()) {
            		if(getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs()
            		.stream()
            		.noneMatch(ptdTipoRequerimento -> ptdTipoRequerimento.getTipoDocumento().getCodigo().equals(tipoDocumento.getCodigo()))) {
            			PendenciaTipoDocumentoTipoRequerimentoVO pendenciaTipoDocumentoTipoRequerimentoVO = new PendenciaTipoDocumentoTipoRequerimentoVO();
                		pendenciaTipoDocumentoTipoRequerimentoVO.getTipoDocumento().setCodigo(tipoDocumento.getCodigo());
                		if(!getTipoRequerimentoVO().getCodigo().equals(0)) {
                			pendenciaTipoDocumentoTipoRequerimentoVO.getTipoRequerimento().setCodigo(getTipoRequerimentoVO().getCodigo());
                		}
                		
                		getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs().add(pendenciaTipoDocumentoTipoRequerimentoVO);
            		}
            	}
            	else {
            		PendenciaTipoDocumentoTipoRequerimentoVO pendenciaTipoDocumentoTipoRequerimentoVO = new PendenciaTipoDocumentoTipoRequerimentoVO();
            		pendenciaTipoDocumentoTipoRequerimentoVO.getTipoDocumento().setCodigo(tipoDocumento.getCodigo());
            		if(!getTipoRequerimentoVO().getCodigo().equals(0)) {
            			pendenciaTipoDocumentoTipoRequerimentoVO.getTipoRequerimento().setCodigo(getTipoRequerimentoVO().getCodigo());
            		}
            		
            		getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs().add(pendenciaTipoDocumentoTipoRequerimentoVO);
            	}
			}
            
        	if(!getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs().isEmpty()) {
        		
        		for (Iterator iterator = getTipoRequerimentoVO().getPendenciaTipoDocumentoTipoRequerimentoVOs().iterator(); iterator.hasNext();) {
        			PendenciaTipoDocumentoTipoRequerimentoVO pendenciaTipoDocumentoTipoRequerimento = (PendenciaTipoDocumentoTipoRequerimentoVO) iterator.next();
					            			
        			if((obterListaTipoDocumentoSelecionado(getTipoDocumentoVOs())
        					.stream().noneMatch(tdSelecionado -> tdSelecionado.getCodigo().equals(pendenciaTipoDocumentoTipoRequerimento.getTipoDocumento().getCodigo()))
        				
        			)) {
        				iterator.remove();
        			}
        		}
        	}  
        	
        	
	}

	public FuncionarioVO getFuncionarioRemover() {
		if(funcionarioRemover == null) {
			funcionarioRemover = new FuncionarioVO();
		}
		return funcionarioRemover;
	}

	public void setFuncionarioRemover(FuncionarioVO funcionarioRemover) {
		this.funcionarioRemover = funcionarioRemover;
	}
	
	public void verificarCursoPorNivelEducacional() {
		
		for (Iterator iterator = getTipoRequerimentoVO().getTipoRequerimentoCursoVOs().iterator(); iterator.hasNext();) {			
			TipoRequerimentoCursoVO tipoRequerimentoCurso = (TipoRequerimentoCursoVO)iterator.next();
			if(!tipoRequerimentoCurso.getCursoVO().getNivelEducacional().equals(getTipoRequerimentoVO().getNivelEducacional())) {
				iterator.remove();
			}		           			

		}
		if (getTipoRequerimentoVO().getPermitirImpressaoHistoricoVisaoAluno()) {
			montarListaTipoLayoutHistorico();
		}
	}
	
	public void limparListaConsulta() {
		setListaConsulta(new ArrayList<>(0));
	}
	
	public void limparTipoRequerimentoAbrirDeferimento() {
		if(getTipoRequerimentoVO().getDeferirAutomaticamente() || getTipoRequerimentoVO().getDeferirAutomaticamenteDocumentoImpresso()) {
			getTipoRequerimentoVO().setAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento(false);
		}
		if (!getTipoRequerimentoVO().getAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento() && Uteis.isAtributoPreenchido(getTipoRequerimentoVO().getTipoRequerimentoAbrirDeferimento())) {
			getTipoRequerimentoVO().getTipoRequerimentoAbrirDeferimento().setNome("");
			getTipoRequerimentoVO().getTipoRequerimentoAbrirDeferimento().setCodigo(0);
		}
	}
	
	public List<SelectItem> getListaSelectTipoBloqueioSimultaneo() { 
		List itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TIPO", "Tipo"));
		itens.add(new SelectItem("CODIGO", "Código"));
		return itens;
	}

	public String getCampoConsultaCursoTransferenciaInterna() {
		if(campoConsultaCursoTransferenciaInterna == null ) {
			campoConsultaCursoTransferenciaInterna ="" ;
		}
		return campoConsultaCursoTransferenciaInterna;
	}

	public void setCampoConsultaCursoTransferenciaInterna(String campoConsultaCursoTransferenciaInterna) {
		this.campoConsultaCursoTransferenciaInterna = campoConsultaCursoTransferenciaInterna;
	}

	public String getValorConsultaCursoTransferenciaInterna() {
		if(valorConsultaCursoTransferenciaInterna == null ) {
			valorConsultaCursoTransferenciaInterna ="";
		}
		return valorConsultaCursoTransferenciaInterna;
	}

	public void setValorConsultaCursoTransferenciaInterna(String valorConsultaCursoTransferenciaInterna) {
		this.valorConsultaCursoTransferenciaInterna = valorConsultaCursoTransferenciaInterna;
	}

	

	public List<CursoVO> getListaConsultaCursoTransferenciaInterna() {
		if(listaConsultaCursoTransferenciaInterna == null ) {
			listaConsultaCursoTransferenciaInterna = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCursoTransferenciaInterna;
	}

	public void setListaConsultaCursoTransferenciaInterna(List<CursoVO> listaConsultaCursoTransferenciaInterna) {
		this.listaConsultaCursoTransferenciaInterna = listaConsultaCursoTransferenciaInterna;
	}
	
	
	 public TipoRequerimentoCursoVO getTipoRequerimentoCursoVO() {
		 if(tipoRequerimentoCursoVO == null ) {
			 tipoRequerimentoCursoVO = new TipoRequerimentoCursoVO();
		 }
			return tipoRequerimentoCursoVO;
		}

		public void setTipoRequerimentoCursoVO(TipoRequerimentoCursoVO tipoRequerimentoCursoVO) {
			this.tipoRequerimentoCursoVO = tipoRequerimentoCursoVO;
		}
		
		
		
		public List<CidTipoRequerimentoVO> getListaCidOriginalVOs() {
			if (listaCidOriginalVOs == null) {
				listaCidOriginalVOs = new ArrayList<CidTipoRequerimentoVO>(0);
			}
			return listaCidOriginalVOs;
		}

		public void setListaCidOriginalVOs(List<CidTipoRequerimentoVO> listaCidOriginalVOs) {
			this.listaCidOriginalVOs = listaCidOriginalVOs;
		}
		
		public List<CidTipoRequerimentoVO> getListaCidVOs() {
			if (listaCidVOs == null) {
				listaCidVOs = new ArrayList<CidTipoRequerimentoVO>(0);
			}
			return listaCidVOs;
		}

		public void setListaCidVOs(List<CidTipoRequerimentoVO> listaCidVOs) {
			this.listaCidVOs = listaCidVOs;
		}
		
		public FileUploadEvent getFileUploadEvent() {
			return fileUploadEvent;
		}

		public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
			this.fileUploadEvent = fileUploadEvent;
		}

		public void inicializarDadosProgressBar() {
			setListaCidVOs(getListaCidOriginalVOs());
			getAplicacaoControle().getProgressBarImportarCid().resetar();		
			getAplicacaoControle().getProgressBarImportarCid().setUsuarioVO(getUsuarioLogado());
			getAplicacaoControle().getProgressBarImportarCid().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getAplicacaoControle().getProgressBarImportarCid().iniciar(0l, (getListaCidOriginalVOs().size()*2)+2, "Iniciando gravação dos dados...", true, this, "persistir");
			
		}
	
		
		public void inicializarDadosProgressBar(FileUploadEvent uploadEvent) {
			if (uploadEvent.getUploadedFile() != null) {
				getAplicacaoControle().getProgressBarImportarCid().resetar();		
				getAplicacaoControle().getProgressBarImportarCid().setUsuarioVO(getUsuarioLogado());
				getAplicacaoControle().getProgressBarImportarCid().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				setFileUploadEvent(uploadEvent);
//				getAplicacaoControle().getProgressBarImportarCandidatoVO().setAssincrono(true);
				try {
					getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().inicializarDadosArquivoImportarCid(getTipoRequerimentoVO().getCidTipoRequerimentoVO(), uploadEvent, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
					e.printStackTrace();
				}
				getAplicacaoControle().getProgressBarImportarCid().iniciar(0l, 100000, "Carregando o arquivo....", true, this, "realizarProcessamentoArquivoExcelCid");
//				realizarProcessamentoArquivoExcelCandidadoInscricaoProcessoSeletivo();
			} else {			
				setMensagemDetalhada("msg_erro", "Selecione um arquivo para a prosseguir com a importação.");
			}
		}
		
		public void adicionarCidTipoRequerimento() throws Exception {
			try {
				CidTipoRequerimentoVO obj = new CidTipoRequerimentoVO();
				obj.setTipoRequerimento(getTipoRequerimentoVO().getCodigo());
				obj.setDescricaoCid(getDescricaoCid());
				obj.setCodCid(getCodCid());
	            getFacadeFactory().getTipoRequerimentoFacade().adicionarCidTipoRequerimentoVOs(getTipoRequerimentoVO().getCidTipoRequerimentoVOs(), obj);
	            setDescricaoCid("");
	            setCodCid("");
				setMensagemID("msg_dados_adicionados");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());

			}
		}
		
		public String getDescricaoCid() {
			return descricaoCid;
		}
		public void setDescricaoCid(String descricaoCid) {
			this.descricaoCid = descricaoCid;
		}
		public String getCodCid() {
			return codCid;
		}
		public void setCodCid(String codCid) {
			this.codCid = codCid;
		}
		
		public void removerCidTipoRequerimento() throws Exception {
			CidTipoRequerimentoVO obj = (CidTipoRequerimentoVO) context().getExternalContext().getRequestMap().get("cidTipoRequerimentoItem");
			getFacadeFactory().getTipoRequerimentoFacade().removerCidTipoRequerimentoVOs(getTipoRequerimentoVO().getCidTipoRequerimentoVOs(), obj);
			setMensagemID("msg_dados_excluidos");
		}
		
		
		  public void consultarCidTipoRequerimento(TipoRequerimentoVO obj) {
		        try {
		            List<CidTipoRequerimentoVO> lista = getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().consultarCidPorTipoRequerimento(obj, getUsuario());
		            if (!lista.isEmpty()) {
		            	obj.setCidTipoRequerimentoVOs(lista);
		            }
		            setMensagemID("msg_dados_consultados");
		        } catch (Exception e) {
		        	obj.setCidTipoRequerimentoVOs(new ArrayList<CidTipoRequerimentoVO>(0));
		            setMensagemDetalhada("msg_erro", e.getMessage());
		        }
		    }
		  
		  public Boolean getApresentarListaCids() {
				return getTipoRequerimentoVO().getTipo().equals("SEGUNDA_CHAMADA");
			}
		  
		  public void scrollerListenerImportarCid(DataScrollEvent DataScrollEvent) throws Exception {
				getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
				getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
				realizarProcessamentoExcelCid();
			}
		  

		  
		  public String realizarProcessamentoExcelCid() {
				try {
					setListaCidVOs(getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().realizarProcessamentoExcelCid(getFileUploadEvent(),  getTipoRequerimentoVO().getCidTipoRequerimentoVO(), getListaCidOriginalVOs(), false,  getAplicacaoControle().getProgressBarImportarCid(), getConfiguracaoGeralSistemaVO(), getUsuarioLogado()));
					setMensagemID("msg_dados_consultados");
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
				return "importacaoCons?faces-redirect=true";
			}
		  
		  
		  public void realizarProcessamentoArquivoExcelCid() {
				try {
					setListaCidVOs(getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().realizarProcessamentoExcelCid(getFileUploadEvent(),  getTipoRequerimentoVO().getCidTipoRequerimentoVO(), getListaCidOriginalVOs(), false,  getAplicacaoControle().getProgressBarImportarCid(), getConfiguracaoGeralSistemaVO(), getUsuarioLogado()));
					getAplicacaoControle().getProgressBarImportarCid().getSuperControle().setMensagemID("msg_dados_importados");			
					getAplicacaoControle().getProgressBarImportarCid().setForcarEncerramento(true);
				} catch (Exception e) {
					getAplicacaoControle().getProgressBarImportarCid().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
//					getAplicacaoControle().getProgressBarImportarCandidatoVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
					getAplicacaoControle().getProgressBarImportarCid().setForcarEncerramento(true);
				}
			}
		  

		  public void adicionarListaImportacaoCidTipoRequerimento() throws Exception {
			    try {
			        Set<String> codigosDuplicados = new HashSet<String>();

			        for (CidTipoRequerimentoVO obj : new ArrayList<>(getListaCidVOs())) {
			            for (CidTipoRequerimentoVO existeNaLista : getTipoRequerimentoVO().getCidTipoRequerimentoVOs()) {
			                if (existeNaLista.getCodCid().equals(obj.getCodCid())) {
			                    codigosDuplicados.add(existeNaLista.getCodCid());
			                    break; 
			                }
			            }

			            if (!codigosDuplicados.contains(obj.getCodCid())) {
			                CidTipoRequerimentoVO cidTipoRequerimentoVO = new CidTipoRequerimentoVO();
			                cidTipoRequerimentoVO.setTipoRequerimento(obj.getCodigo());
			                cidTipoRequerimentoVO.setDescricaoCid(obj.getDescricaoCid());
			                cidTipoRequerimentoVO.setCodCid(obj.getCodCid());
			                cidTipoRequerimentoVO.setImportacaoExcel(true);
			                getTipoRequerimentoVO().getCidTipoRequerimentoVOs().add(cidTipoRequerimentoVO);
			            }
			        }

			        if (!codigosDuplicados.isEmpty()) {
			            verificarRemoverCidDuplicado(new ArrayList<>(codigosDuplicados), getTipoRequerimentoVO().getCidTipoRequerimentoVOs());
			        }
			        setMensagemID("msg_dados_adicionados");
			    } catch (Exception e) {
			        setMensagemDetalhada("msg_erro", e.getMessage());
			    }
			}
		  
		  public void verificarRemoverCidDuplicado(List<String> listaCidExistente, List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs) throws Exception {
			    StringBuilder tipoCid = new StringBuilder();
			    for (String palavra : listaCidExistente) {
			        tipoCid.append(palavra).append(", ");
			    }
			    if (Uteis.isAtributoPreenchido(tipoCid.toString())) {
					throw new Exception("O código CID " + tipoCid + " está/estão duplicado(s).");
			    }
			}
		  
		  
		  public void removerCidTipoRequerimentoImportado() throws Exception {
				CidTipoRequerimentoVO obj = (CidTipoRequerimentoVO) context().getExternalContext().getRequestMap().get("item");
				getFacadeFactory().getTipoRequerimentoFacade().removerCidTipoRequerimentoVOs(getListaCidVOs(), obj);
				setMensagemID("msg_dados_excluidos");
			}
		  
		  
		  public Boolean getApresentarBooleanAproveitarDisciplinaCursando() {
				return getTipoRequerimentoVO().getTipo().equals("AD");
			}
		  
		  
		  public void removerListaCid() throws Exception {
				getListaCidVOs().clear();
			}
		  
		  public void downloadLayoutPadraoExcel() throws Exception {
				try {
					String xmlModeloUtilizar = "ModeloPlanilhaTipoCID.xlsx";
					File arquivo = new File(UteisJSF.getCaminhoWeb() + "arquivos" +  File.separator + xmlModeloUtilizar);
					HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
					request.getSession().setAttribute("nomeArquivo", arquivo.getName());
					request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
					request.getSession().setAttribute("deletarArquivo", false);
					context().getExternalContext().dispatch("/DownloadSV");
					FacesContext.getCurrentInstance().responseComplete();
					
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		  
		  public List<SelectItem> getListaBimestre() {
				if(listaBimestre == null) {
					listaBimestre = new ArrayList<>();
					listaBimestre.add(new SelectItem("", ""));
					listaBimestre.add(new SelectItem("1", "1º Bimestre"));
					listaBimestre.add(new SelectItem("2", "2º Bimestre"));
					listaBimestre.add(new SelectItem("3", "3º Bimestre"));
					listaBimestre.add(new SelectItem("4", "4º Bimestre"));
					listaBimestre.add(new SelectItem("semestre", "Semestre"));
				}
				return listaBimestre;
			}

			public void setListaBimestre(List<SelectItem> listaBimestre) {
				this.listaBimestre = listaBimestre;
			}

			public List<SelectItem> getListaTipoNota() {
				try {
					if(listaTipoNota == null) {
						listaTipoNota = getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarConfiguracaoAcademicaNotaPorMatriculaDisciplina("", 0);
					}
				} catch (Exception e) {
					setMensagemDetalhada(e.getMessage());
				}
				
				return listaTipoNota;
			}

			public void setListaTipoNota(List<SelectItem> listaTipoNota) {
				this.listaTipoNota = listaTipoNota;
			}
			public boolean verificarLicencaMaternidade() {
				return getTipoRequerimentoVO().getNome().contains("MATERNIDADE");
			}
			
			public boolean getVerificarLicencaMaternidade() {
			    return verificarLicencaMaternidade();
			}
			
			public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaDeferimento() {
				if (personalizacaoMensagemAutomaticaDeferimento == null) {
					personalizacaoMensagemAutomaticaDeferimento = new PersonalizacaoMensagemAutomaticaVO();
				}
				return personalizacaoMensagemAutomaticaDeferimento;
			}
			
			public void setPersonalizacaoMensagemAutomaticaDeferimento(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaDeferimento) {
				this.personalizacaoMensagemAutomaticaDeferimento = personalizacaoMensagemAutomaticaDeferimento;
			}
			
			public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaIndeferimento() {
				if (personalizacaoMensagemAutomaticaIndeferimento == null) {
					personalizacaoMensagemAutomaticaIndeferimento = new PersonalizacaoMensagemAutomaticaVO();
				}
				return personalizacaoMensagemAutomaticaIndeferimento;
			}
			
			public void setPersonalizacaoMensagemAutomaticaIndeferimento(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaIndeferimento) {
				this.personalizacaoMensagemAutomaticaIndeferimento = personalizacaoMensagemAutomaticaIndeferimento;
			}
			
			public List<SelectItem> getListaSelectItemTemplateMensagemAutomaticaEnum() {
				if (listaSelectItemTemplateMensagemAutomaticaEnum == null) {
					listaSelectItemTemplateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.getListaSelectItemTemplateMensagemAutomaticaEnum();
				}
				return listaSelectItemTemplateMensagemAutomaticaEnum;
			}
			
			public Boolean getApresentarFiltrosUnidadeEnsinoNivelEducacional() {
				return Boolean.FALSE;
			}
			
			public String getTagsMensagemAutomatica_Apresentar(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum) {
				StringBuilder tags = new StringBuilder(TagsMensagemAutomaticaEnum.NOME_CURSO.name() + ", " + TagsMensagemAutomaticaEnum.MATRICULA.name() + ", " + TagsMensagemAutomaticaEnum.REGISTRO_ACADEMICO.name());
				if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_DEFERIDO)) {
					tags.append(", " + TagsMensagemAutomaticaEnum.NOME_ALUNO.name() + ", " + TagsMensagemAutomaticaEnum.NOME_REQUERENTE.name() + ", " + TagsMensagemAutomaticaEnum.DATA_REQUERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.TIPO_REQUERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.SITUACAO_REQUERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.MOTIVO_DEFERIMENTO.name());
				} else {
					tags.append(", " + TagsMensagemAutomaticaEnum.NOME_ALUNO.name() + ", " + TagsMensagemAutomaticaEnum.NOME_REQUERENTE.name() + ", " + TagsMensagemAutomaticaEnum.DATA_REQUERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.TIPO_REQUERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.SITUACAO_REQUERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.MOTIVO_INDEFERIMENTO.name() + ", " + TagsMensagemAutomaticaEnum.RESPONSAVEL_DEPARTAMENTO.name());
				}
				if (getTipoRequerimentoVO().getIsTipoAproveitamentoDisciplina() || getTipoRequerimentoVO().getIsTipoSegundaChamada()) {
					if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_DEFERIDO)) {
						tags.append(", " + TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_DEFERIDAS.name() + ", " + TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_INDEFERIDAS.name());
					} else {
						tags.append(", " + TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_INDEFERIDAS.name());
					}
				} else if (getTipoRequerimentoVO().getTipo().equals("OU")) {
					tags.append(", " + TagsMensagemAutomaticaEnum.DATA_INICIO_AFASTAMENTO.name() + ", " + TagsMensagemAutomaticaEnum.DATA_TERMINO_AFASTAMENTO);
				}
				return tags.toString();
			}

			public void inicializarPersonalizacaoMensagemAutomaticaDeferimento() {
				getPersonalizacaoMensagemAutomaticaDeferimento().setViaTipoRequerimento(Boolean.TRUE);
				getPersonalizacaoMensagemAutomaticaDeferimento().setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_DEFERIDO);
			}
			
			public void inicializarPersonalizacaoMensagemAutomaticaIndeferimento() {
				getPersonalizacaoMensagemAutomaticaIndeferimento().setViaTipoRequerimento(Boolean.TRUE);
				getPersonalizacaoMensagemAutomaticaIndeferimento().setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_INDEFERIDO);
			}
			
			public void inicializarPersonalizacaoMensagemAutomaticaTipoRequerimento() {
				getPersonalizacaoMensagemAutomaticaDeferimento().setTags(getTagsMensagemAutomatica_Apresentar(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_DEFERIDO));
				getPersonalizacaoMensagemAutomaticaIndeferimento().setTags(getTagsMensagemAutomatica_Apresentar(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_INDEFERIDO));
				getTipoRequerimentoVO().setPersonalizacaoMensagemAutomaticaDeferimento(getPersonalizacaoMensagemAutomaticaDeferimento());
	        	getTipoRequerimentoVO().setPersonalizacaoMensagemAutomaticaIndeferimento(getPersonalizacaoMensagemAutomaticaIndeferimento());
			}
			
			public List<SelectItem> getListaSelectItemTipoAluno() {
				if (listaSelectItemTipoAluno == null) {
					listaSelectItemTipoAluno = TipoAlunoEnum.getListaSelectItemTipoAlunoEnum();
				}
				return listaSelectItemTipoAluno;
			}
			
			public void setListaSelectItemTipoAluno(List<SelectItem> listaSelectItemTipoAluno) {
				this.listaSelectItemTipoAluno = listaSelectItemTipoAluno;
			}
	}