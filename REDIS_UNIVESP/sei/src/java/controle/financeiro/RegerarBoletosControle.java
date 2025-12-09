package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@SuppressWarnings("unchecked")
@Controller("RegerarBoletosControle")
@Scope("request")
@Lazy
public class RegerarBoletosControle extends SuperControle implements Serializable {

    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private CursoVO cursoVO;
    private TurmaVO turmaVO;
    private String ano;
    private String semestre;
    private List listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemCursoTurno;
    private List listaSelectItemTurma;

    public RegerarBoletosControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        incializarDados();
    }

    public void incializarDados() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCursoTurno() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoTurnoPorUnidadeEnsino(getUnidadeEnsinoCursoVO().getUnidadeEnsino());
            setListaSelectItemTurma(new ArrayList(0));
            setListaSelectItemCursoTurno(new ArrayList(0));
            i = resultadoConsulta.iterator();
            getListaSelectItemCursoTurno().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
                getListaSelectItemCursoTurno().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    private List<UnidadeEnsinoCursoVO> consultarCursoTurnoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemTurma() throws Exception {
        try {
            List<TurmaVO> resultadoConsulta = consultarTurmasPorUnidadeEnsinoCurso(getUnidadeEnsinoCursoVO().getCodigo());
            setListaSelectItemTurma(new ArrayList(0));
            setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<TurmaVO> consultarTurmasPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) throws Exception {
        List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoCurso(codigoUnidadeEnsinoCurso, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void regerarBoletos() throws Exception {
//        RegerarBoletos reg = new RegerarBoletos();
        int contador = 0;
        List<ContaReceberVO> listaContaReceber = getFacadeFactory().getRegerarBoletosFacade().consultarContaReceber(contador, getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getTurmaVO().getCodigo(), getAno(), getSemestre());
        while (!listaContaReceber.isEmpty()) {
            getFacadeFactory().getRegerarBoletosFacade().regerarBoletos(listaContaReceber, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), new ControleRemessaContaReceberVO());
            contador += 10000;
            listaContaReceber.clear();
            listaContaReceber = getFacadeFactory().getRegerarBoletosFacade().consultarContaReceber(contador, getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getTurmaVO().getCodigo(), getAno(), getSemestre());
        }
        setMensagemDetalhada("Foram regerados " + String.valueOf(getFacadeFactory().getRegerarBoletosFacade().getQtdeContas()) + " boletos.");
    }

    public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
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

    public List getListaSelectItemCursoTurno() {
        if (listaSelectItemCursoTurno == null) {
            listaSelectItemCursoTurno = new ArrayList(0);
        }
        return listaSelectItemCursoTurno;
    }

    public void setListaSelectItemCursoTurno(List listaSelectItemCursoTurno) {
        this.listaSelectItemCursoTurno = listaSelectItemCursoTurno;
    }

    public List getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
