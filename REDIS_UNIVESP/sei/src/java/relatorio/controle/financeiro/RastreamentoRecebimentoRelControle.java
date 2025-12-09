package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.RastreamentoRecebimentoRelVO;
import controle.arquitetura.SelectItemOrdemValor;

@Controller("RastreamentoRecebimentoRelControle")
@Scope("request")
@Lazy
public class RastreamentoRecebimentoRelControle extends SuperControleRelatorio {

    private List<RastreamentoRecebimentoRelVO> listaRastreamentoRecebimento;

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private String mes;
    private String ano;

    private String mes1;
    private String mes2;
    private String mes3;
    private String mes4;
    private String mes5;
    private String mes6;

    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemCurso;
    private List<SelectItem> listaSelectItemTurno;
    private List<SelectItem> listaSelectItemMes;

    private Boolean unidadeEnsinoSelecionada;

    public RastreamentoRecebimentoRelControle() {

    }

    public void novo() throws Exception {
        apresentarNomesMeses();
        getUnidadeEnsinoCursoVO().setCurso(new CursoVO());
        getUnidadeEnsinoCursoVO().setTurno(new TurnoVO());

        getListaRastreamentoRecebimento().clear();
        getListaSelectItemCurso().clear();
        getListaSelectItemTurno().clear();

        setUnidadeEnsinoVO(new UnidadeEnsinoVO());
        montarListaSelectItemUnidadeEnsino();
        getUnidadeEnsinoVO().setCodigo(0);

        setUnidadeEnsinoSelecionada(Boolean.TRUE);
        setAno(null);
    }

    public void apresentarNomesMeses() {
        try {
            if (getMes() != null && !getMes().equals("") && getAno() != null && !getAno().equals("")) {
                Date dataTemp = getFacadeFactory().getRastreamentoRecebimentoRelFacade().montarData(1, Integer.valueOf(getMes()), Integer.valueOf(getAno()));
                setMes1(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(Uteis.getMesData(dataTemp)));
                setMes2(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 1))));
                setMes3(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 2))));
                setMes4(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 3))));
                setMes5(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 4))));
                setMes6(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 5))));
            } else {
                Date dataTemp = getFacadeFactory().getRastreamentoRecebimentoRelFacade().montarData(1, 01, 2010);
                setMes1(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(Uteis.getMesData(dataTemp)));
                setMes2(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 1))));
                setMes3(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 2))));
                setMes4(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 3))));
                setMes5(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 4))));
                setMes6(getFacadeFactory().getRastreamentoRecebimentoRelFacade().nomeMesExtenso(
                    Uteis.getMesData(getFacadeFactory().getRastreamentoRecebimentoRelFacade().atualizarData(dataTemp, 5))));
            }
        } catch (Exception e) {
            setMensagemID(e.getMessage());
        }
    }

    public void montarListaRastreamentoRecebimento() throws Exception {
        try {
            getFacadeFactory().getRastreamentoRecebimentoRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo(), getMes(), getAno());
            setListaRastreamentoRecebimento(getFacadeFactory().getRastreamentoRecebimentoRelFacade().consultarRastreamentoRecebimento(
                getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getMes(),
                getAno()));
            apresentarNomesMeses();
            int cont = 1;
            int i = getListaRastreamentoRecebimento().size();

            while (i < 6) {
                RastreamentoRecebimentoRelVO recebimentoRelVO = new RastreamentoRecebimentoRelVO();
                recebimentoRelVO.setFaturamento("0.0");
                recebimentoRelVO.setNumeroParcela("0");
                recebimentoRelVO.setValor1("0");
                recebimentoRelVO.setValor2("0");
                recebimentoRelVO.setValor3("0");
                recebimentoRelVO.setValor4("0");
                recebimentoRelVO.setValor5("0");
                recebimentoRelVO.setValor6("0");
                getListaRastreamentoRecebimento().add(recebimentoRelVO);
                i++;
            }

            for (RastreamentoRecebimentoRelVO rastreamento : getListaRastreamentoRecebimento()) {
                switch (cont) {
                    case 1:
                        rastreamento.setMes(getMes1());
                        break;
                    case 2:
                        rastreamento.setMes(getMes2());
                        break;
                    case 3:
                        rastreamento.setMes(getMes3());
                        break;
                    case 4:
                        rastreamento.setMes(getMes4());
                        break;
                    case 5:
                        rastreamento.setMes(getMes5());
                        break;
                    case 6:
                        rastreamento.setMes(getMes6());
                        break;
                    default:
                        rastreamento.setMes("");
                        break;
                }
                cont++;
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaRastreamentoRecebimento(new ArrayList<RastreamentoRecebimentoRelVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(),
                false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally{
            Uteis.liberarListaMemoria(resultadoConsulta);
         }
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<Integer> listaCursos = new ArrayList<Integer>(0);
        try {
            List<UnidadeEnsinoCursoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(
                getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            if (getUnidadeEnsinoVO() == null || getUnidadeEnsinoVO().getCodigo() == null || getUnidadeEnsinoVO().getCodigo() == 0) {
                setUnidadeEnsinoSelecionada(Boolean.TRUE);
                getListaRastreamentoRecebimento().clear();
                getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
                setAno("");
            } else {
                setUnidadeEnsinoSelecionada(Boolean.FALSE);
            }
            setListaSelectItemCurso(new ArrayList<SelectItem>(0));
            getListaSelectItemCurso().add(new SelectItem(0, ""));

            for (UnidadeEnsinoCursoVO unidadeEnsinoCurso : resultadoConsulta) {
                if (!listaCursos.contains(unidadeEnsinoCurso.getCurso().getCodigo())) {
                    getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCurso().getCodigo(), unidadeEnsinoCurso.getCurso().getNome()));
                    listaCursos.add(unidadeEnsinoCurso.getCurso().getCodigo());
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            listaCursos = null;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Turno</code>.
     */
    public void montarListaSelectItemTurno() throws Exception {
        if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() != null && !getUnidadeEnsinoCursoVO().getCurso().getCodigo().equals(0)
            && getUnidadeEnsinoCursoVO().getCurso().getCodigo() > 0) {
            List<TurnoVO> resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorCodigoCurso(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            Iterator i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TurnoVO obj = (TurnoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemTurno(objs);
        } else {
            setListaSelectItemTurno(new ArrayList<SelectItem>(0));
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>)
     * utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List<TurnoVO> consultarTurnoPorNome() throws Exception {
        List<TurnoVO> lista = getFacadeFactory().getTurnoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void selecinarCurso() {
        try {
            if (getUnidadeEnsinoCursoVO() == null || getUnidadeEnsinoCursoVO().getCurso() == null || getUnidadeEnsinoCursoVO().getCurso().getCodigo() == null
                || getUnidadeEnsinoCursoVO().getCurso().getCodigo() == 0) {
                setUnidadeEnsinoSelecionada(Boolean.TRUE);
                getListaRastreamentoRecebimento().clear();
                getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
                setAno("");
            } else {
                montarListaSelectItemTurno();
                setUnidadeEnsinoSelecionada(Boolean.FALSE);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getListaSelectItemMeses() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("01", "Janeiro"));
        objs.add(new SelectItem("02", "Fevereiro"));
        objs.add(new SelectItem("03", "Março"));
        objs.add(new SelectItem("04", "Abril"));
        objs.add(new SelectItem("05", "Maio"));
        objs.add(new SelectItem("06", "Junho"));
        objs.add(new SelectItem("07", "julho"));
        objs.add(new SelectItem("08", "Agosto"));
        objs.add(new SelectItem("09", "Setembro"));
        objs.add(new SelectItem("10", "Outubro"));
        objs.add(new SelectItem("11", "Novembro"));
        objs.add(new SelectItem("12", "Dezembro"));
        return objs;
    }

    public List<RastreamentoRecebimentoRelVO> getListaRastreamentoRecebimento() {
        if (listaRastreamentoRecebimento == null) {
            listaRastreamentoRecebimento = new ArrayList<RastreamentoRecebimentoRelVO>(0);
        }
        return listaRastreamentoRecebimento;
    }

    public void setListaRastreamentoRecebimento(List<RastreamentoRecebimentoRelVO> listaRastreamentoRecebimento) {
        this.listaRastreamentoRecebimento = listaRastreamentoRecebimento;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public List<SelectItem> getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTurno;
    }

    public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public List<SelectItem> getListaSelectItemMes() {
        return listaSelectItemMes;
    }

    public void setListaSelectItemMes(List<SelectItem> listaSelectItemMes) {
        this.listaSelectItemMes = listaSelectItemMes;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMes1() {
        return mes1;
    }

    public void setMes1(String mes1) {
        this.mes1 = mes1;
    }

    public String getMes2() {
        return mes2;
    }

    public void setMes2(String mes2) {
        this.mes2 = mes2;
    }

    public String getMes3() {
        return mes3;
    }

    public void setMes3(String mes3) {
        this.mes3 = mes3;
    }

    public String getMes4() {
        return mes4;
    }

    public void setMes4(String mes4) {
        this.mes4 = mes4;
    }

    public String getMes5() {
        return mes5;
    }

    public void setMes5(String mes5) {
        this.mes5 = mes5;
    }

    public String getMes6() {
        return mes6;
    }

    public void setMes6(String mes6) {
        this.mes6 = mes6;
    }

    public Boolean getUnidadeEnsinoSelecionada() {
        return unidadeEnsinoSelecionada;
    }

    public void setUnidadeEnsinoSelecionada(Boolean unidadeEnsinoSelecionada) {
        this.unidadeEnsinoSelecionada = unidadeEnsinoSelecionada;
    }

}
