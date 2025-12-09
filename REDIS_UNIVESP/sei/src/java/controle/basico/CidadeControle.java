package controle.basico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cidadeForm.jsp cidadeCons.jsp) com as funcionalidades da classe <code>Cidade</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cidade
 * @see CidadeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import jobs.JobMatriculaCRM;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("CidadeControle")
@Scope("viewScope")
@Lazy
public class CidadeControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private CidadeVO cidadeVO;
    /**
     * Interface <code>CidadeInterfaceFacade</code> responsável pela interconexão da camada de controle com a camada de negócio.
     * Criando uma independência da camada de controle com relação a tenologia de persistência dos dados (DesignPatter: Façade).
     */
    protected List<SelectItem> listaSelectItemEstado;
    protected List<SelectItem> listaSelectItemPaiz;

    public CidadeControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");

    }

    @PostConstruct
    public void init() {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Cidade</code>
     * para edição pelo usuário da aplicação.
     */
    public void executarThread() throws Exception {
        Thread exec = new Thread(new JobMatriculaCRM());
        exec.start();
    }

    public String novo() throws Exception {
        removerObjetoMemoria(this);
        removerObjetoMemoria(getCidadeVO());
        setCidadeVO(new CidadeVO());
        cidadeVO.setNovoObj(true);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("cidadeForm");
    }

    public void enviarMensagemSms() {
        try {
//            HumanSms.enviarMensagem(getConfiguracaoGeralPadraoSistema(),Uteis.formatarTelefoneParaEnvioSms("(62)91813128"), "TESTE OTIMIZE: "
//                    + " seu resultado (registro:01) ja esta disponivel em nosso site (www.otimize-ti.com.br)");
        } catch (Exception e) {
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Cidade</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
        obj.setNovoObj(Boolean.FALSE);
        setCidadeVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("cidadeForm");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Cidade</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (cidadeVO.isNovoObj().booleanValue()) {
        	List<CidadeVO> cidadeExistente = new ArrayList<>(); 
        	cidadeExistente = getFacadeFactory().getCidadeFacade().consultarPorNomeCodigoEstado(cidadeVO.getNome(), false, cidadeVO.getEstado().getCodigo(), getUsuarioLogado());
        	if (cidadeExistente.size() > 0){
        	    throw new Exception("Já existe uma cidade com este nome para este estado.");
        	}
                getFacadeFactory().getCidadeFacade().incluir(cidadeVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getCidadeFacade().alterar(cidadeVO, getUsuarioLogado());
                getAplicacaoControle().removerCidade(cidadeVO.getCodigo());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CidadeCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            //if (getControleConsulta().getCampoConsulta().equals("estado")) {
            //    objs = getFacadeFactory().getCidadeFacade().consultarPorEstado(getControleConsulta().getValorConsulta(), true);
            //}
            if (getControleConsulta().getCampoConsulta().equals("siglaEstado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("estado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorEstado(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("cep")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorCep(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }

            //objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            //definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CidadeVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {            
            getFacadeFactory().getCidadeFacade().excluir(cidadeVO, getUsuarioLogado());
            removerObjetoMemoria(getCidadeVO());
            setCidadeVO(new CidadeVO());
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

    public void irPaginaInicial() throws Exception {
//        JobNotificacaoRegistroAulaNota job = new JobNotificacaoRegistroAulaNota();
//        job.realizarNotificacaoRegistroAulaNota();
        removerObjetoMemoria(getCidadeVO());
        //controleConsulta.setPaginaAtual(1);
        this.consultar();
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

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPaizPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Paiz</code>.
     */
    public void montarListaSelectItemPaiz(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPaizPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PaizVO obj = (PaizVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemPaiz(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Paiz</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemPaiz() {
        try {
            montarListaSelectItemPaiz("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>estado</code>
     */
    public void montarListaSelectItemEstado() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem(0, ""));
        Integer codigoPaiz = null;
        try {
            codigoPaiz = cidadeVO.getEstado().getPaiz().getCodigo();
            if (codigoPaiz == null || codigoPaiz.equals(0)) {
                setListaSelectItemEstado(objs);
                return;
            }
        } catch (NullPointerException e) {
            setListaSelectItemEstado(objs);
            return;
        }
        List<EstadoVO> resultadoConsulta = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(codigoPaiz, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

        for (EstadoVO obj : resultadoConsulta) {
            objs.add(new SelectItem(obj.getCodigo(), obj.getSigla().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        setListaSelectItemEstado(objs);
    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemPaiz();
        montarListaSelectItemEstado();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("estado", "Estado"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        //setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        //definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("cidadeCons");
    }

    public CidadeVO getCidadeVO() {
        return cidadeVO;
    }

    public void setCidadeVO(CidadeVO cidadeVO) {
        this.cidadeVO = cidadeVO;
    }

    public List<SelectItem> getListaSelectItemEstado() {
        return listaSelectItemEstado;
    }

    public void setListaSelectItemEstado(List<SelectItem> listaSelectItemEstado) {
        this.listaSelectItemEstado = listaSelectItemEstado;
    }

    public List<SelectItem> getListaSelectItemPaiz() {
        return listaSelectItemPaiz;
    }

    public void setListaSelectItemPaiz(List<SelectItem> listaSelectItemPaiz) {
        this.listaSelectItemPaiz = listaSelectItemPaiz;
    }
}
