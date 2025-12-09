package relatorio.controle.administrativo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRel;

@SuppressWarnings("unchecked")
@Controller("UnidadeEnsinoRelControle")
@Scope("request")
@Lazy
public class UnidadeEnsinoRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoRel unidadeEnsinoRel;
    private String unidadeEnsino_Erro;
    private String curso_Erro;
    protected String nomeUnidade = "";
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemCurso;

    public UnidadeEnsinoRelControle() throws Exception {
        setUnidadeEnsinoRel(new UnidadeEnsinoRel());
        inicializarNomeUnidade(unidadeEnsinoRel.getUnidadeEnsino());
        inicializarListasSelectItemTodosComboBox();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void inicializarNomeUnidade(Integer codigo) {
        try {
            UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidade(codigo, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (unidadeEnsinoVO != null) {
                setNomeUnidade(unidadeEnsinoVO.getNome());
            }
        } catch (Exception e) {
            setNomeUnidade("");
        }
    }

    public void imprimirPDF() {
        try {
            unidadeEnsinoRel.setOrdenarPor(getOpcaoOrdenacao().intValue());
            unidadeEnsinoRel.setDescricaoFiltros("");
            String titulo = " Relatório de Unidade de Ensino";
            String xml = unidadeEnsinoRel.emitirRelatorio(getUsuarioLogadoClone());
            String design = unidadeEnsinoRel.getDesignIReportRelatorio();
            apresentarRelatorio(unidadeEnsinoRel.getIdEntidade(), xml, titulo, this.getNomeUnidade(), "", "PDF", "/" + unidadeEnsinoRel.getIdEntidade() + "/registros", design,
                    getUsuarioLogado().getNome(), unidadeEnsinoRel.getDescricaoFiltros());
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por gerar o relatório de <code>ServidorRel</code> no formato HTML
     */
    public void imprimirHTML() {
        try {
            unidadeEnsinoRel.setOrdenarPor(getOpcaoOrdenacao().intValue());
            unidadeEnsinoRel.setDescricaoFiltros("");
            String titulo = " Relatório de Unidade de Ensino";
            String xml = unidadeEnsinoRel.emitirRelatorio(getUsuarioLogadoClone());
            String design = unidadeEnsinoRel.getDesignIReportRelatorio();

            apresentarRelatorio(unidadeEnsinoRel.getIdEntidade(), xml, titulo, this.getNomeUnidade(), "", "HTML", "/" + unidadeEnsinoRel.getIdEntidade() + "/registros", design,
                    getUsuarioLogado().getNome(), unidadeEnsinoRel.getDescricaoFiltros());
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
        }
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemOrdenacao();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemCurso();

    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getUnidadeEnsinoRel().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List objs = new ArrayList(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String opcao = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), opcao));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);

    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
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

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
           // //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCurso(Integer prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = new ArrayList(0);
            if (prm == 0) {
                resultadoConsulta = consultarCursoPorNome("");
            } else {
                resultadoConsulta = consultarCursoPorUnidadeEnsino(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CursoVO obj = (CursoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCurso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCurso() {
        try {
            montarListaSelectItemCurso(unidadeEnsinoRel.getUnidadeEnsino());
        } catch (Exception e) {
           // //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarCursoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCursoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public List consultarCursoPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
        List lista = getFacadeFactory().getCursoFacade().consultarPorNomeUnidadeEnsino("", unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public String getCurso_Erro() {
        return curso_Erro;
    }

    public void setCurso_Erro(String curso_Erro) {
        this.curso_Erro = curso_Erro;
    }

    public List getListaSelectItemCurso() {
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getUnidadeEnsino_Erro() {
        return unidadeEnsino_Erro;
    }

    public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
        this.unidadeEnsino_Erro = unidadeEnsino_Erro;
    }

    public UnidadeEnsinoRel getUnidadeEnsinoRel() {
        return unidadeEnsinoRel;
    }

    public void setUnidadeEnsinoRel(UnidadeEnsinoRel unidadeEnsinoRel) {
        this.unidadeEnsinoRel = unidadeEnsinoRel;
    }

    public String getNomeUnidade() {
        return nomeUnidade;
    }

    public void setNomeUnidade(String nomeUnidade) {
        this.nomeUnidade = nomeUnidade;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        unidadeEnsinoRel = null;
        unidadeEnsino_Erro = null;
        curso_Erro = null;
        nomeUnidade = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemCurso);
    }
}
