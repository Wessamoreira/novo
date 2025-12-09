package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.academico.TitulacaoCurso;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * titulacaoCursoForm.jsp titulacaoCursoCons.jsp) com as funcionalidades da classe <code>TitulacaoCurso</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see TitulacaoCurso
 * @see TitulacaoCursoVO
 */
@Controller("TitulacaoCursoControle")
@Scope("viewScope")
@Lazy
public class TitulacaoCursoControle extends SuperControle {

    private TitulacaoCursoVO titulacaoCursoVO;
    private String campoConsultarCurso;
    private String valorConsultarCurso;
    private List listaConsultarCurso;
    private List<SelectItem> listaSelectItemTitulacao;
    private ItemTitulacaoCursoVO itemTitulacaoCursoVO;

    public TitulacaoCursoControle() throws Exception {
//        inicializarFacades();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>TitulacaoCurso</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        setTitulacaoCursoVO(new TitulacaoCursoVO());
        setItemTitulacaoCursoVO(new ItemTitulacaoCursoVO());
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TitulacaoCurso</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
    	//TitulacaoCursoVO obj = (TitulacaoCursoVO) context().getExternalContext().getRequestMap().get("titulacaoCurso");
    	TitulacaoCursoVO obj = (TitulacaoCursoVO) context().getExternalContext().getRequestMap().get("titulacaoCursoItem");
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(new Boolean(false));
        setTitulacaoCursoVO(obj);
        setItemTitulacaoCursoVO(new ItemTitulacaoCursoVO());
        setMensagemID("msg_dados_editar", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>TitulacaoCursoVO</code>.
     * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    public void inicializarAtributosRelacionados(TitulacaoCursoVO obj) {
        if (obj.getCurso() == null) {
            obj.setCurso(new CursoVO());
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TitulacaoCurso</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getTitulacaoCursoFacade().persistir(titulacaoCursoVO, getUsuarioLogado());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TitulacaoCursoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getTitulacaoCursoFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoCons");
        }
    }

    public boolean getApresentarResultadoConsulta() {
        if (getListaConsulta() == null || getListaConsulta().isEmpty()) {
            return false;
        }
        return true;
    }

    public void limparConsultaRichModal() {
        getListaConsulta().clear();
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TitulacaoCursoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getTitulacaoCursoFacade().excluir(titulacaoCursoVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
        }
    }

    /* Método responsável por adicionar um novo objeto da classe <code>ItemTitulacaoCurso</code>
     * para o objeto <code>titulacaoCursoVO</code> da classe <code>TitulacaoCurso</code>
     */
    public String adicionarItemTitulacaoCurso() throws Exception {
        try {
            if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
                itemTitulacaoCursoVO.setTitulacaoCurso(getTitulacaoCursoVO());
            }
            getFacadeFactory().getTitulacaoCursoFacade().adicionarObjItemTitulacaoCursoVOs(getTitulacaoCursoVO(), getItemTitulacaoCursoVO());
            this.setItemTitulacaoCursoVO(new ItemTitulacaoCursoVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>ItemTitulacaoCurso</code>
     * para edição pelo usuário.
     */
    public String editarItemTitulacaoCurso() throws Exception {
        ItemTitulacaoCursoVO obj = (ItemTitulacaoCursoVO) context().getExternalContext().getRequestMap().get("ListaItemTitulacaoCurso");
        setItemTitulacaoCursoVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
    }

    /* Método responsável por remover um novo objeto da classe <code>ItemTitulacaoCurso</code>
     * do objeto <code>titulacaoCursoVO</code> da classe <code>TitulacaoCurso</code>
     */
    public String removerItemTitulacaoCurso() throws Exception {
        ItemTitulacaoCursoVO obj = (ItemTitulacaoCursoVO) context().getExternalContext().getRequestMap().get("ListaItemTitulacaoCurso");
        getFacadeFactory().getTitulacaoCursoFacade().excluirObjItemTitulacaoCursoVOs(getTitulacaoCursoVO(), obj.getTitulacao());
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoForm");
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Curso</code> por meio dos parametros informados no richmodal.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal
     * montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarCurso() {
        try {

            setListaConsultarCurso(getFacadeFactory().getCursoFacade().consultar(getControleConsulta().getCampoConsulta().toLowerCase(),
            		         getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultarCurso().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarCurso() throws Exception {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
        getTitulacaoCursoVO().setCurso(obj);
        Uteis.liberarListaMemoria(this.listaConsultarCurso);
    }

    public void limparCampoCurso() {
        this.getTitulacaoCursoVO().setCurso(new CursoVO());
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        getListaConsulta().clear();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("titulacaoCursoCons");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        titulacaoCursoVO = null;
        itemTitulacaoCursoVO = null;
    }

    public ItemTitulacaoCursoVO getItemTitulacaoCursoVO() {
        if (itemTitulacaoCursoVO == null) {
            itemTitulacaoCursoVO = new ItemTitulacaoCursoVO();
        }
        return itemTitulacaoCursoVO;
    }

    public void setItemTitulacaoCursoVO(ItemTitulacaoCursoVO itemTitulacaoCursoVO) {
        this.itemTitulacaoCursoVO = itemTitulacaoCursoVO;
    }

    public String getCampoConsultarCurso() {
        return campoConsultarCurso;
    }

    public void setCampoConsultarCurso(String campoConsultarCurso) {
        this.campoConsultarCurso = campoConsultarCurso;
    }

    public String getValorConsultarCurso() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return valorConsultarCurso.toUpperCase();
        }
        return valorConsultarCurso;
    }

    public void setValorConsultarCurso(String valorConsultarCurso) {
        this.valorConsultarCurso = valorConsultarCurso;
    }

    public List getListaConsultarCurso() {
        if (listaConsultarCurso == null) {
            listaConsultarCurso = new ArrayList();
        }
        return listaConsultarCurso;

    }

    public void setListaConsultarCurso(List listaConsultarCurso) {
        this.listaConsultarCurso = listaConsultarCurso;
    }

    public TitulacaoCursoVO getTitulacaoCursoVO() {
        if (titulacaoCursoVO == null) {
            titulacaoCursoVO = new TitulacaoCursoVO();
        }
        return titulacaoCursoVO;
    }

    public void setTitulacaoCursoVO(TitulacaoCursoVO titulacaoCursoVO) {
        this.titulacaoCursoVO = titulacaoCursoVO;
    }

    public void limparConsultaCurso() {
        getListaConsultarCurso().clear();
    }

    public List getListaSelectItemTitulacao() {
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
        return objs;
    }

    public List getListaSelectItemSegundaTitulacao() {
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
        return objs;
    }
}
