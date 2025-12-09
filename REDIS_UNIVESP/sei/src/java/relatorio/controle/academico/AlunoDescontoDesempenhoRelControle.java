/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunoDescontoDesempenhoRelVO;
import relatorio.negocio.jdbc.academico.AlunoDescontoDesempenhoRel;

/**
 *
 * @author Carlos
 */
@Controller("AlunoDescontoDesempenhoRelControle")
@Scope("request")
@Lazy
public class AlunoDescontoDesempenhoRelControle extends SuperControleRelatorio {

    private AlunoDescontoDesempenhoRelVO alunoDescontoDesempenhoRelVO;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemCurso;
    private List listaSelectItemTurma;    
    private List listaAlunoDescontoDesempenhoRel;
    private String tipoConsulta;

    public AlunoDescontoDesempenhoRelControle() throws Exception {
        inicializarDadosListaSelectItemUnidadelEnsino();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void realizarImprimirPDF() {

        try {
            getFacadeFactory().getAlunoDescontoDesempenhoRelFacade().validarDadosGeracaoRelatorio(getTipoConsulta());
            String titulo = "Relatório Aluno Desconto x Desempenho";
            realizarCriacaoObjetos();
            String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            String design = getFacadeFactory().getAlunoDescontoDesempenhoRelFacade().getDesignIReportRelatorio();
            String caminho = getFacadeFactory().getAlunoDescontoDesempenhoRelFacade().getCaminhoBaseRelatorio();
            apresentarRelatorioObjetos(AlunoDescontoDesempenhoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), "", getListaAlunoDescontoDesempenhoRel(), caminho);
            setMensagemID("msg_relatorio_ok");
            removerObjetoMemoria(this);
            inicializarDadosListaSelectItemUnidadelEnsino();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void realizarCriacaoObjetos() throws Exception {
        setListaAlunoDescontoDesempenhoRel(getFacadeFactory().getAlunoDescontoDesempenhoRelFacade().executarCriacaoObjetos(getAlunoDescontoDesempenhoRelVO(), tipoConsulta, opcaoOrdenacao, opcaoOrdenacao, opcaoOrdenacao));
        if (getListaAlunoDescontoDesempenhoRel().isEmpty()) {
            throw new Exception("Não há dados a serem exibidos com esses parâmetros.");
        }
    }

    public void inicializarDadosListaSelectItemUnidadelEnsino() {
        List<UnidadeEnsinoVO> listaResultado = new ArrayList<UnidadeEnsinoVO>(0);
        try {
            listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally{
            listaResultado.clear();
            listaResultado = null;
        }
    }

    public void realizarMontagemListaSelectItemTipoRelatorio() {
        if (getTipoConsulta().equals("curso")) {
            realizarMontagemListaSelectItemCurso();
        } else if (getTipoConsulta().equals("turma")) {
            realizarMontagemListaSelectItemTurma();
        }
    }

    public void realizarMontagemListaSelectItemCurso() {
        try {
            setListaSelectItemCurso(getFacadeFactory().getAlunoDescontoDesempenhoRelFacade().realizarMontagemListaSelectItemCurso(getAlunoDescontoDesempenhoRelVO().getMatriculaVO().getUnidadeEnsino().getCodigo()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarMontagemListaSelectItemTurma() {
        try {
            setListaSelectItemTurma(getFacadeFactory().getAlunoDescontoDesempenhoRelFacade().realizarMontagemListaSelectItemTurma(getAlunoDescontoDesempenhoRelVO().getMatriculaVO().getUnidadeEnsino().getCodigo()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getListaSelectItemTipoRelatorio() {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        objs.add(new SelectItem("curso", "Curso"));
        objs.add(new SelectItem("turma", "Turma"));
        return objs;
    }

    /**
     * @return the alunoDescontoDesempenhoRelVO
     */
    public AlunoDescontoDesempenhoRelVO getAlunoDescontoDesempenhoRelVO() {
        if (alunoDescontoDesempenhoRelVO == null) {
            alunoDescontoDesempenhoRelVO = new AlunoDescontoDesempenhoRelVO();
        }
        return alunoDescontoDesempenhoRelVO;
    }

    /**
     * @param alunoDescontoDesempenhoRelVO the alunoDescontoDesempenhoRelVO to set
     */
    public void setAlunoDescontoDesempenhoRelVO(AlunoDescontoDesempenhoRelVO alunoDescontoDesempenhoRelVO) {
        this.alunoDescontoDesempenhoRelVO = alunoDescontoDesempenhoRelVO;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the tipoRelatorio
     */
    public String getTipoConsulta() {
        if (tipoConsulta == null) {
            tipoConsulta = "";
        }
        return tipoConsulta;
    }

    /**
     * @param tipoRelatorio the tipoRelatorio to set
     */
    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    /**
     * @return the listaSelectItemCurso
     */
    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }

        return listaSelectItemCurso;
    }

    /**
     * @param listaSelectItemCurso the listaSelectItemCurso to set
     */
    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    /**
     * @return the listaSelectItemTurma
     */
    public List getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList(0);
        }
        return listaSelectItemTurma;
    }

    /**
     * @param listaSelectItemTurma the listaSelectItemTurma to set
     */
    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    /**
     * @return the listaAlunoDescontoDesempenhoRel
     */
    public List getListaAlunoDescontoDesempenhoRel() {
        if (listaAlunoDescontoDesempenhoRel == null) {
            listaAlunoDescontoDesempenhoRel = new ArrayList(0);
        }
        return listaAlunoDescontoDesempenhoRel;
    }

    /**
     * @param listaAlunoDescontoDesempenhoRel the listaAlunoDescontoDesempenhoRel to set
     */
    public void setListaAlunoDescontoDesempenhoRel(List listaAlunoDescontoDesempenhoRel) {
        this.listaAlunoDescontoDesempenhoRel = listaAlunoDescontoDesempenhoRel;
    }

    public boolean getIsApresentarSelectMenuCurso() {
        if (getTipoConsulta().equals("curso")) {
            return true;
        }
        return false;
    }

    public boolean getIsApresentarSelectMenuTurma() {
        if (getTipoConsulta().equals("turma")) {
            return true;
        }
        return false;
    }
}
