package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * questionarioAlunoForm.jsp questionarioAlunoCons.jsp) com as funcionalidades da classe <code>QuestionarioAluno</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see QuestionarioAluno
 * @see QuestionarioAlunoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("QuestionarioAlunoControle")
@Scope("request")
@Lazy
public class QuestionarioAlunoControle extends SuperControle implements Serializable {

    private QuestionarioAlunoVO questionarioAlunoVO;
    protected List listaSelectItemPerguntaQuestionario;
    
    public QuestionarioAlunoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>QuestionarioAluno</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         removerObjetoMemoria(this);
        setQuestionarioAlunoVO(new QuestionarioAlunoVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>QuestionarioAluno</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        QuestionarioAlunoVO obj = (QuestionarioAlunoVO) context().getExternalContext().getRequestMap().get("questionarioAluno");
        obj.setNovoObj(Boolean.FALSE);
        setQuestionarioAlunoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>QuestionarioAluno</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (questionarioAlunoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getQuestionarioAlunoFacade().incluir(questionarioAlunoVO);
            } else {
                getFacadeFactory().getQuestionarioAlunoFacade().alterar(questionarioAlunoVO);
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP QuestionarioAlunoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getQuestionarioAlunoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("respostaQuestionarioAluno")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
      //          objs = getFacadeFactory().getQuestionarioAlunoFacade().consultarPorRespostaQuestionarioAluno(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("texto")) {
        //        objs = getFacadeFactory().getQuestionarioAlunoFacade().consultarPorTexto(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoResposta")) {
      //          objs = getFacadeFactory().getQuestionarioAlunoFacade().consultarPorTipoResposta(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
            }
            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>QuestionarioAlunoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getQuestionarioAlunoFacade().excluir(questionarioAlunoVO);
            setQuestionarioAlunoVO(new QuestionarioAlunoVO());
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
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

    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>tipoResposta</code>
     */
    public List getListaSelectItemTipoRespostaQuestionarioAluno() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoRespostaQuestionarios = (Hashtable) Dominios.getTipoRespostaQuestionario();
        Enumeration keys = tipoRespostaQuestionarios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoRespostaQuestionarios.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>PerguntaQuestionario</code>.
     */
    public void montarListaSelectItemPerguntaQuestionario(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPerguntaPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PerguntaVO obj = (PerguntaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemPerguntaQuestionario(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PerguntaQuestionario</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Pergunta</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemPerguntaQuestionario() {
        try {
            montarListaSelectItemPerguntaQuestionario("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPerguntaPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getPerguntaFacade().consultarPorDescricao(descricaoPrm, EscopoPerguntaEnum.PROCESSO_SELETIVO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemPerguntaQuestionario();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Codigo"));
        itens.add(new SelectItem("respostaQuestionarioAluno", "Resposta Questionário Aluno"));
        itens.add(new SelectItem("texto", "Texto"));
        itens.add(new SelectItem("tipoResposta", "Tipo de Resposta"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public void escolherPeso1() {
        getQuestionarioAlunoVO().setPeso(1);
    }

    public void escolherPeso2() {
        getQuestionarioAlunoVO().setPeso(2);
    }

    public void escolherPeso3() {
        getQuestionarioAlunoVO().setPeso(3);
    }

    public void escolherPeso4() {
        getQuestionarioAlunoVO().setPeso(4);
    }

    public void escolherPeso5() {
        getQuestionarioAlunoVO().setPeso(5);
    }

    public List getListaSelectItemPerguntaQuestionario() {
        return (listaSelectItemPerguntaQuestionario);
    }

    public void setListaSelectItemPerguntaQuestionario(List listaSelectItemPerguntaQuestionario) {
        this.listaSelectItemPerguntaQuestionario = listaSelectItemPerguntaQuestionario;
    }

    public QuestionarioAlunoVO getQuestionarioAlunoVO() {
        return questionarioAlunoVO;
    }

    public void setQuestionarioAlunoVO(QuestionarioAlunoVO questionarioAlunoVO) {
        this.questionarioAlunoVO = questionarioAlunoVO;
    }
}