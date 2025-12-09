package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * matriculaPeriodoTurmaDisciplinaForm.jsp matriculaPeriodoTurmaDisciplinaCons.jsp) com as funcionalidades da classe
 * <code>MatriculaPeriodoTurmaDisciplina</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see MatriculaPeriodoTurmaDisciplina
 * @see MatriculaPeriodoTurmaDisciplinaVO
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
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("MatriculaPeriodoTurmaDisciplinaControle")
@Scope("request")
@Lazy
public class MatriculaPeriodoTurmaDisciplinaControle extends SuperControle implements Serializable {

    private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
    protected List listaSelectItemTurma;

    public MatriculaPeriodoTurmaDisciplinaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>MatriculaPeriodoTurmaDisciplina</code> para
     * edição pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe
     * <code>MatriculaPeriodoTurmaDisciplina</code> para alteração. O objeto desta classe é disponibilizado na session
     * da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplina");
        obj.setNovoObj(Boolean.FALSE);
        setMatriculaPeriodoTurmaDisciplinaVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>MatriculaPeriodoTurmaDisciplina</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a
     * operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
     * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (matriculaPeriodoTurmaDisciplinaVO.isNovoObj().booleanValue()) {
            } else {
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP MatriculaPeriodoTurmaDisciplinaCons.jsp. Define o
     * tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);

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
     * Operação responsável por processar a exclusão um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {

            setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
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

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turma</code>.
     */
    public void montarListaSelectItemTurma(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTurmaPorIdentificadorTurma(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TurmaVO obj = (TurmaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
            }
            setListaSelectItemTurma(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turma</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turma</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTurma() {
        try {
            montarListaSelectItemTurma("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>identificadorTurma</code> Este atributo é uma lista (<code>List</code>)
     * utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTurmaPorIdentificadorTurma(String identificadorTurmaPrm) throws Exception {
        List lista = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(identificadorTurmaPrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemTurma();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("matriculaPeriodo", "Matricula Periodo"));
        itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
        itens.add(new SelectItem("gradeDisciplina", "Grade Disciplina"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public List getListaSelectItemTurma() {
        return (listaSelectItemTurma);
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
        return matriculaPeriodoTurmaDisciplinaVO;
    }

    public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
        this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
    }
}
