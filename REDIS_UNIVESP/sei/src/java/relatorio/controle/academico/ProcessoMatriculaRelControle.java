package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ProcessoMatriculaRel;

@SuppressWarnings("unchecked")
@Controller("ProcessoMatriculaRelControle")
@Scope("request")
@Lazy
public class ProcessoMatriculaRelControle extends SuperControleRelatorio {

    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemCurso;
    protected String unidadeEnsino_Erro;
    protected String curso_Erro;
    protected String situacao_Erro;

    public ProcessoMatriculaRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List listaObjetos = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ProcessoMatriculaRelControle", "Inicializando Geração de Relatório Processo Matrícula", "Emitindo Relatório");
            getFacadeFactory().getProcessoMatriculaRelFacade().setOrdenarPor(getOpcaoOrdenacao().intValue());

            listaObjetos = getFacadeFactory().getProcessoMatriculaRelFacade().criarObjeto(getUnidadeEnsino_Erro(), getCurso_Erro(), getSituacao_Erro());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(ProcessoMatriculaRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(ProcessoMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Processo de Matrícula");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ProcessoMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                if (!getUnidadeEnsino_Erro().equals("0")) {
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(Integer.valueOf(getUnidadeEnsino_Erro()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "ProcessoMatriculaRelControle", "Finalizando Geração de Relatório Processo Matrícula", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemOrdenacao();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemCurso();

    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getProcessoMatriculaRelFacade().getOrdenacoesRelatorio();
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
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
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

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não
     * recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            ////System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros
     * para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCurso() throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getCursoFacade().consultarPorNomeUnidadeEnsino("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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

    public List getListaSelectItemSituacaoProcessoMatricula() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoProcessoMatriculas = (Hashtable) Dominios.getSituacaoProcessoMatricula();
        Enumeration keys = situacaoProcessoMatriculas.keys();
        objs.add(new SelectItem("", ""));
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoProcessoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    public List consultarCursoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCursoFacade().consultarPorNomeCurso_UnidadeEnsino(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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

    public String getSituacao_Erro() {
        return situacao_Erro;
    }

    public void setSituacao_Erro(String situacao_Erro) {
        this.situacao_Erro = situacao_Erro;
    }

    public String getUnidadeEnsino_Erro() {
        return unidadeEnsino_Erro;
    }

    public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
        this.unidadeEnsino_Erro = unidadeEnsino_Erro;
    }
}
