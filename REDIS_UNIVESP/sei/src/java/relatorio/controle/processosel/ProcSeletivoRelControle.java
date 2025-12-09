package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.processosel.ProcSeletivoRel;

@SuppressWarnings("unchecked")
@Controller("ProcSeletivoRelControle")
@Scope("request")
@Lazy
public class ProcSeletivoRelControle extends SuperControleRelatorio {

    protected ProcSeletivoRel procSeletivoRel;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemCurso;
    protected List listaSelectItemProcSeletivo;
    protected String unidadeEnsino_Erro;
    protected String curso_Erro;
    protected String valorConsultaProcSeletivo;
    protected String campoConsultaProcSeletivo;
    protected List listaConsultaProcSeletivo;
    private Integer tmpCodigoProcessoSeletivo = 0;

    public ProcSeletivoRelControle() throws Exception {
        setProcSeletivoRel(null);
        setValorConsultaProcSeletivo("");
        setCampoConsultaProcSeletivo("");
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        try {
            getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getProcSeletivoRelFacade().designIReportRelatorio());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getProcSeletivoRelFacade().caminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setTituloRelatorio(" Relatório de Processo Seletivo");
            getSuperParametroRelVO().setListaObjetos(procSeletivoRel.criarObjeto());
            getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getProcSeletivoRelFacade().caminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeEmpresa("");
            getSuperParametroRelVO().setVersaoSoftware("");
            getSuperParametroRelVO().setFiltros("");

            realizarImpressaoRelatorio();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
            setProcSeletivoRel(null);
            setValorConsultaProcSeletivo("");
            setCampoConsultaProcSeletivo("");
            inicializarListasSelectItemTodosComboBox();
        }


    }

    public void imprimirExcel() {
        try {
            getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getProcSeletivoRelFacade().designIReportRelatorio());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
            getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getProcSeletivoRelFacade().caminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setTituloRelatorio("Relatório de Processo Seletivo");
            getSuperParametroRelVO().setListaObjetos(procSeletivoRel.criarObjeto());
            getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getProcSeletivoRelFacade().caminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeEmpresa("");
            getSuperParametroRelVO().setVersaoSoftware("");
            getSuperParametroRelVO().setFiltros("");

            realizarImpressaoRelatorio();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
            setProcSeletivoRel(null);
            setValorConsultaProcSeletivo("");
            setCampoConsultaProcSeletivo("");
            inicializarListasSelectItemTodosComboBox();
        }
    }

    public void montarListaSelectItemProcSeletivo(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarProcSeletivoPorDescricao(prm);
            Ordenacao.ordenarListaDecrescente(resultadoConsulta, "dataInicio");
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            setListaSelectItemProcSeletivo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ProcSeletivo</code>. Buscando todos os objetos correspondentes a entidade <code>ProcSeletivo</code>. Esta rotina não
     * recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemProcSeletivo() {
        try {
            montarListaSelectItemProcSeletivo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a
     * serem apresentados no ComboBox correspondente
     */
    public List consultarProcSeletivoPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(descricaoPrm,  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if ((procSeletivoRel.getDescricao() == null) || (procSeletivoRel.getDescricao().intValue() == 0)) {
                List objs = new ArrayList(0);
                setListaSelectItemUnidadeEnsino(objs);
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                List unidadesValidas = consultarPorProcessoSeletivoPorCodigo();
                Iterator j = unidadesValidas.iterator();
                while (j.hasNext()) {
                    ProcSeletivoUnidadeEnsinoVO proc = (ProcSeletivoUnidadeEnsinoVO) j.next();
                    if (proc.getUnidadeEnsino().getCodigo().equals(obj.getCodigo())) {
                        objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
                        break;
                    }
                }
                setListaSelectItemUnidadeEnsino(objs);
            }
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
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public List consultarPorProcessoSeletivoPorCodigo() throws Exception {
        ProcSeletivoVO obj = getFacadeFactory().getProcSeletivoFacade().consultarCodigo(procSeletivoRel.getDescricao().intValue(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        List lista = obj.getProcSeletivoUnidadeEnsinoVOs();
        return lista;
    }

    public void montarListaSelectItemCursoOpcao(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if ((procSeletivoRel.getDescricao() == null) || (tmpCodigoProcessoSeletivo.intValue() == 0)) {
                List objs = new ArrayList(0);
                setListaSelectItemCurso(objs);
                return;
            }
            if ((procSeletivoRel.getUnidadeEnsino() == null) || (procSeletivoRel.getUnidadeEnsino().intValue() == 0)) {
                List objs = new ArrayList(0);
                setListaSelectItemCurso(objs);
                return;
            }
            resultadoConsulta = consultarPorProcessoSeletivoCurso();
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcSeletivoCursoVO proc = (ProcSeletivoCursoVO) i.next();
                objs.add(new SelectItem(proc.getUnidadeEnsinoCurso().getCodigo(), proc.getUnidadeEnsinoCurso().getCurso().getNome() + " - " + proc.getUnidadeEnsinoCurso().getTurno().getNome()));
            }
            setListaSelectItemCurso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>CursoOpcao1</code>. Buscando todos os objetos correspondentes a entidade <code>Curso</code>. Esta rotina não recebe
     * parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCursoOpcao() {
        try {
            montarListaSelectItemCursoOpcao("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarPorProcessoSeletivoCurso() throws Exception {
        ProcSeletivoUnidadeEnsinoVO obj = getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().consultarPorCodigoUnidadeEnsino(procSeletivoRel.getUnidadeEnsino(), procSeletivoRel.getDescricao(),
                false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        List lista = obj.getProcSeletivoCursoVOs();
        return lista;
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getProcSeletivoRel().getOrdenacoesRelatorio();
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

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemProcSeletivo();
        montarListaSelectItemOrdenacao();
        setListaSelectItemUnidadeEnsino(null);
        setListaSelectItemCurso(null);

    }

    public void consultarUnidadeEnsinoPorChavePrimaria() {
        try {
            Integer campoConsulta = procSeletivoRel.getUnidadeEnsino();
            UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            procSeletivoRel.setUnidadeEnsino(unidadeEnsinoVO.getCodigo());
        } catch (Exception e) {
            procSeletivoRel.setUnidadeEnsino(0);
        }
        montarListaSelectItemCursoOpcao();

    }

    public void consultarProcessoSeletivoPorChavePrimaria() {
        try {
            Integer campoConsulta = procSeletivoRel.getDescricao();
            ProcSeletivoVO procSeletivoVO = getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            procSeletivoRel.setDescricao(procSeletivoVO.getCodigo());
            procSeletivoRel.setUnidadeEnsino(0);
            tmpCodigoProcessoSeletivo = campoConsulta;
        } catch (Exception e) {
            procSeletivoRel.setDescricao(0);
            procSeletivoRel.setUnidadeEnsino(0);
            tmpCodigoProcessoSeletivo = 0;
        }
        montarListaSelectItemUnidadeEnsino();
    }

    public void consultarProcSeletivo() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaProcSeletivo().equals("descricao")) {
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(),  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(),false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFim")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataProva")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaProcSeletivo(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProcSeletivo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public String getMascaraConsultaProcSeletivo() {
        if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
            return "return mascara(this.form,'this.id','99/99/9999',event);";
        }
        return "";
    }

    public List getTipoConsultaComboProcSeletivo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("dataInicio", "Data Início"));
        itens.add(new SelectItem("dataFim", "Data Fim"));
        itens.add(new SelectItem("dataProva", "Data Prova"));
        return itens;
    }

    public void selecionarProcSeletivo() {
        ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivo");
        getProcSeletivoRel().setDescricao(obj.getCodigo());
        montarListaSelectItemUnidadeEnsino();
    }

    public ProcSeletivoRel getProcSeletivoRel() {
        if (procSeletivoRel == null) {
            procSeletivoRel = new ProcSeletivoRel();
        }
        return procSeletivoRel;
    }

    public void setProcSeletivoRel(ProcSeletivoRel procSeletivoRel) {
        this.procSeletivoRel = procSeletivoRel;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCurso_Erro() {
        return curso_Erro;
    }

    public void setCurso_Erro(String curso_Erro) {
        this.curso_Erro = curso_Erro;
    }

    public List getListaSelectItemProcSeletivo() {
        return listaSelectItemProcSeletivo;
    }

    public void setListaSelectItemProcSeletivo(List listaSelectItemProcSeletivo) {
        this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
    }

    public String getUnidadeEnsino_Erro() {
        return unidadeEnsino_Erro;
    }

    public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
        this.unidadeEnsino_Erro = unidadeEnsino_Erro;
    }

    public String getCampoConsultaProcSeletivo() {
        if (campoConsultaProcSeletivo == null) {
            campoConsultaProcSeletivo = "";
        }
        return campoConsultaProcSeletivo;
    }

    public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
        this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
    }

    public List getListaConsultaProcSeletivo() {
        return listaConsultaProcSeletivo;
    }

    public void setListaConsultaProcSeletivo(List listaConsultaProcSeletivo) {
        this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
    }

    public String getValorConsultaProcSeletivo() {
        if (valorConsultaProcSeletivo == null) {
            valorConsultaProcSeletivo = "";
        }
        return valorConsultaProcSeletivo;
    }

    public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
        this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
    }
}
