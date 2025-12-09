package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * perfilSocioEconomicoForm.jsp perfilSocioEconomicoCons.jsp) com as funcionalidades da classe <code>PerfilSocioEconomico</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see PerfilSocioEconomico
 * @see PerfilSocioEconomicoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerfilSocioEconomicoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("PerfilSocioEconomicoControle")
@Scope("viewScope")
@Lazy
public class PerfilSocioEconomicoControle extends SuperControle implements Serializable {

    private PerfilSocioEconomicoVO perfilSocioEconomicoVO;
    protected List listaSelectItemPessoa;
    protected List listaSelectItemQuestionario;
    private List listaConsultaPessoa;
    private Boolean nomeReadonly;
    private String campoConsultaPessoa;
    private String valorConsultaPessoa;

    public PerfilSocioEconomicoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>PerfilSocioEconomico</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        try {
            setPerfilSocioEconomicoVO(new PerfilSocioEconomicoVO());
            setNomeReadonly(Boolean.FALSE);
            QuestionarioVO quest;
            quest = getConfiguracaoGeralPadraoSistema().getQuestionarioPerfilSocioEconomico();
            getPerfilSocioEconomicoVO().setQuestionario(quest);
            inicializarListasSelectItemTodosComboBox();
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PerfilSocioEconomico</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        PerfilSocioEconomicoVO obj = (PerfilSocioEconomicoVO) context().getExternalContext().getRequestMap().get("perfilSocioEconomicoItens");
        obj.setNovoObj(Boolean.FALSE);
        setNomeReadonly(Boolean.FALSE);
        setPerfilSocioEconomicoVO(obj);
        getFacadeFactory().getPerfilSocioEconomicoFacade().gerarListasRespostaPergunta(perfilSocioEconomicoVO, getUsuarioLogado());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PerfilSocioEconomico</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (perfilSocioEconomicoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getPerfilSocioEconomicoFacade().incluir(perfilSocioEconomicoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getPerfilSocioEconomicoFacade().alterar(perfilSocioEconomicoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PerfilSocioEconomicoCons.jsp.
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
                objs = getFacadeFactory().getPerfilSocioEconomicoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getPerfilSocioEconomicoFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricaoQuestionario")) {
                objs = getFacadeFactory().getPerfilSocioEconomicoFacade().consultarPorDescricaoQuestionario(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
//            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
//            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>PerfilSocioEconomicoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPerfilSocioEconomicoFacade().excluir(perfilSocioEconomicoVO, getUsuarioLogado());
            setPerfilSocioEconomicoVO(new PerfilSocioEconomicoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoForm.xhtml");
        }
    }

    public void consultarPessoa() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaPessoa().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(), "AL", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaPessoa().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaPessoa(), "AL", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaPessoa().equals("RG")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaPessoa(), "AL", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaPessoa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaPessoa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPessoa(){
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
        getPerfilSocioEconomicoVO().setPessoa(obj);
        listaConsultaPessoa.clear();
    }

    public void alterarPessoa() {
        setNomeReadonly(Boolean.FALSE);
        getPerfilSocioEconomicoVO().setPessoa(new PessoaVO());
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
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
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Questionario</code>.
     */
    public void montarListaSelectItemQuestionario(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarQuestionarioPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                QuestionarioVO obj = (QuestionarioVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemQuestionario(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Questionario</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Questionario</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemQuestionario() {
        try {
            montarListaSelectItemQuestionario("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarQuestionarioPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getQuestionarioFacade().consultarPorDescricao(descricaoPrm, "AI", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Pessoa</code>.
     */
    public void montarListaSelectItemPessoa(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPessoaPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PessoaVO obj = (PessoaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemPessoa(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Pessoa</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Pessoa</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemPessoa() {
        try {
            montarListaSelectItemPessoa("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPessoaPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPessoaFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemPessoa();
        montarListaSelectItemQuestionario();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Pessoa"));
        itens.add(new SelectItem("descricaoQuestionario", "Questionário"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getTipoConsultaPessoa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
//        setPaginaAtualDeTodas("0/0");
//        setListaConsulta(new ArrayList(0));
//        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilSocioEconomicoCons.xhtml");
    }

    public void validarDadosListaResposta() throws Exception {
        RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaItens");
        getPerfilSocioEconomicoVO().varrerListaQuestionarioRetornarPerguntaRespondida(obj);
    }

    public List getListaSelectItemQuestionario() {
        return (listaSelectItemQuestionario);
    }

    public void setListaSelectItemQuestionario(List listaSelectItemQuestionario) {
        this.listaSelectItemQuestionario = listaSelectItemQuestionario;
    }

    public List getListaSelectItemPessoa() {
        return (listaSelectItemPessoa);
    }

    public void setListaSelectItemPessoa(List listaSelectItemPessoa) {
        this.listaSelectItemPessoa = listaSelectItemPessoa;
    }

    public PerfilSocioEconomicoVO getPerfilSocioEconomicoVO() {
        return perfilSocioEconomicoVO;
    }

    public void setPerfilSocioEconomicoVO(PerfilSocioEconomicoVO perfilSocioEconomicoVO) {
        this.perfilSocioEconomicoVO = perfilSocioEconomicoVO;
    }

    public String getCampoConsultaPessoa() {
        return campoConsultaPessoa;
    }

    public void setCampoConsultaPessoa(String campoConsultaPessoa) {
        this.campoConsultaPessoa = campoConsultaPessoa;
    }

    public List getListaConsultaPessoa() {
        return listaConsultaPessoa;
    }

    public void setListaConsultaPessoa(List listaConsultaPessoa) {
        this.listaConsultaPessoa = listaConsultaPessoa;
    }

    public String getValorConsultaPessoa() {
        return valorConsultaPessoa;
    }

    public void setValorConsultaPessoa(String valorConsultaPessoa) {
        this.valorConsultaPessoa = valorConsultaPessoa;
    }

    public Boolean getNomeReadonly() {
        return nomeReadonly;
    }

    public void setNomeReadonly(Boolean nomeReadonly) {
        this.nomeReadonly = nomeReadonly;
    }
}
