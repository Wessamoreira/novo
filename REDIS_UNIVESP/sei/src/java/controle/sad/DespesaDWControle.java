package controle.sad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.sad.NivelGraficoDWVO;
import negocio.comuns.utilitarias.Uteis;
@Controller("DespesaDWControle") 
@Scope("session") 
@Lazy
public class DespesaDWControle extends SuperControle implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5803979713248867188L;
    private NivelGraficoDWVO nivelGraficoDWVO1;
    private NivelGraficoDWVO nivelGraficoDWVO2;
    private List<NivelGraficoDWVO> nivelGraficoDWVO1s;
    private List<NivelGraficoDWVO> nivelGraficoDWVO2s;
    private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
    private List listaConsultarUnidadeEnsino;    
   
    protected String nivel;
    protected String teste;
    protected Date data;
    protected Date dataFim;

    public DespesaDWControle() throws Exception {
        //obterUsuarioLogado();
        novo();
    }

      public void removerUnidadeEnsino() {
        UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsino");
        int index = 0;
        for (UnidadeEnsinoVO unidadeEnsinoVO1 : getUnidadeEnsinoVOs()) {
            if (unidadeEnsinoVO1.getCodigo().intValue() == unidadeEnsinoVO.getCodigo().intValue()) {
                getUnidadeEnsinoVOs().remove(index);
                return;
            }
            index++;
        }

    }

      /**
     * Método responsável por processar a consulta na entidade <code>UnidadeEnsino</code> por meio dos parametros
     * informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos
     * parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarUnidadeEnsino() {
        try {
            setListaConsultarUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setListaConsultarUnidadeEnsino(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

     public void selecionarUnidadeEnsino() throws Exception {
        UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCons");
        for (UnidadeEnsinoVO unidadeEnsinoVO1 : getUnidadeEnsinoVOs()) {
            if (unidadeEnsinoVO1.getCodigo().intValue() == unidadeEnsinoVO.getCodigo().intValue()) {
                return;
            }
        }
        getUnidadeEnsinoVOs().add(unidadeEnsinoVO);
        consultarUnidadeEnsino();
    }

      public Integer getColumn() {
        if (getUnidadeEnsinoVOs().size() > 4) {
            return 4;
        }
        return getUnidadeEnsinoVOs().size();
    }

    public Integer getElement() {
        return getUnidadeEnsinoVOs().size();
    }
    
     public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
        if (unidadeEnsinoVOs == null) {
            unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
            if (getUnidadeEnsinoLogado().getCodigo() > 0) {
                unidadeEnsinoVOs.add(getUnidadeEnsinoLogadoClone());
            }else{
                consultarUnidadeEnsino();
                unidadeEnsinoVOs.addAll(getListaConsultarUnidadeEnsino());
               getListaConsultarUnidadeEnsino().clear();
            }
        }
        return unidadeEnsinoVOs;
    }

    public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
        this.unidadeEnsinoVOs = unidadeEnsinoVOs;
    }

    public List getListaConsultarUnidadeEnsino() {
        if(listaConsultarUnidadeEnsino == null){
            listaConsultarUnidadeEnsino = new ArrayList(0);
        }
        return listaConsultarUnidadeEnsino;
    }

    public void setListaConsultarUnidadeEnsino(List listaConsultarUnidadeEnsino) {
        this.listaConsultarUnidadeEnsino = listaConsultarUnidadeEnsino;
    }





    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>DespesaDW</code>
     * para edição pelo usuário da aplicação.
     */
    public void novo() throws Exception {
        setNivelGraficoDWVO1(new NivelGraficoDWVO());
        setNivelGraficoDWVO2(new NivelGraficoDWVO());
        setNivelGraficoDWVO1s(new ArrayList<NivelGraficoDWVO>(0));
        setNivelGraficoDWVO2s(new ArrayList<NivelGraficoDWVO>(0));
        setData(Uteis.gerarDataInicioMes(Uteis.getMesData(Uteis.obterDataAntiga(new Date(), 60)), Uteis.getAnoData(Uteis.obterDataAntiga(new Date(), 60))));
        setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        setNivel("");
        setTeste("");
        gerarGraficos();
        getNivelGraficoDWVO1s().add(nivelGraficoDWVO1);
        getNivelGraficoDWVO2s().add(nivelGraficoDWVO2);
        setMensagemID("msg_visualizarDetalhes", Uteis.ALERTA);
    }

    public List listaGrafico() {
        return getNivelGraficoDWVO1().getLegendaGraficoVOs();
    }

    public List listaGrafico2() {
        return getNivelGraficoDWVO2().getLegendaGraficoVOs();
    }

//    public void gerarGraficos(Date dataInicio, Date dataFim) {
    public void gerarGraficos() {
        try {
            getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(nivelGraficoDWVO1, nivelGraficoDWVO2, nivel, data, dataFim, "RE", getUnidadeEnsinoVOs(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    
    public Boolean selecionarLegenda(Integer codigo, String nome, Double valor, String nivel) {
        try {
            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, 0, true, "RE");
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, 0, 0, 0, 0, 0, "", true, "RE");
            getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
                setNivel(obj.getNivel());
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                return false;
            }
            if (novoNivel2.getGrafico() != null) {
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);
                setNivel(obj.getNivel());
            } else {
                getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                return false;
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return true;
    }
    
    public void selecionarLegendaPainel(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigo, String nome, Double valor, String nivel) {
        try {
            getUnidadeEnsinoVOs().clear();
            getUnidadeEnsinoVOs().addAll(unidadeEnsinoVOs);
            setData(dataInicio);
            setDataFim(dataTermino);

            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            removerNiveisPosteriores1(0);
            removerNiveisPosteriores2(0);
//            this.data = data;
//            this.dataFim = dataFim;
            //LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, 0, true, "RE");
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, 0, 0, 0, 0, 0, "", true, "RE");
            getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
                setNivel(obj.getNivel());
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
               //return "despesaDW";
            }
            if (novoNivel2.getGrafico() != null) {
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);
                setNivel(obj.getNivel());
            } else {
                getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                //return "despesaDW";
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        //return "despesaDW";
    }

    public void adicionarNivelGrafico1(NivelGraficoDWVO obj) {
        int index = 0;
        for (NivelGraficoDWVO objExistente : getNivelGraficoDWVO1s()) {
            if (obj.getNumeroNivel().intValue() == objExistente.getNumeroNivel().intValue()) {

                removerNiveisPosteriores1(index);
                return;
            }
            index++;
        }
        getNivelGraficoDWVO1s().add(obj);
    }

    public void removerNiveisPosteriores1(int indice) {
        int size = getNivelGraficoDWVO1s().size();
        if ((indice + 1) < size) {
            getNivelGraficoDWVO1s().remove(indice + 1);
            removerNiveisPosteriores1(indice);
        }
    }

    public void adicionarNivelGrafico2(NivelGraficoDWVO obj) {
        int index = 0;
        for (NivelGraficoDWVO objExistente : getNivelGraficoDWVO2s()) {
            if (obj.getNumeroNivel().intValue() == objExistente.getNumeroNivel().intValue()) {
                removerNiveisPosteriores2(index);
                return;
            }
            index++;
        }
        getNivelGraficoDWVO2s().add(obj);
    }

    public NivelGraficoDWVO voltarNivelGrafico2(NivelGraficoDWVO nivel1) {
        for (NivelGraficoDWVO objExistente : getNivelGraficoDWVO2s()) {
            if (nivel1.getUnidadeEnsino().intValue() == objExistente.getUnidadeEnsino().intValue() && nivel1.getDepartamento().intValue() == objExistente.getDepartamento().intValue() && nivel1.getFuncionario().intValue() == objExistente.getFuncionario().intValue() && nivel1.getCategoriaDespesa().intValue() == objExistente.getCategoriaDespesa().intValue()) {
                return objExistente.getVoltarNivel2(nivel1.getDepartamento(), nivel1.getFuncionario(), 0, 0, 0, "");
            }
        }
        return getNivelGraficoDWVO2().getVoltarNivel2(nivel1.getDepartamento(), nivel1.getFuncionario(), 0, 0, 0, "");
    }

    public NivelGraficoDWVO voltarNivelGrafico1(NivelGraficoDWVO nivel2) {
        for (NivelGraficoDWVO objExistente : getNivelGraficoDWVO1s()) {
            if (nivel2.getUnidadeEnsino().intValue() == objExistente.getUnidadeEnsino().intValue() && nivel2.getDepartamento().intValue() == objExistente.getDepartamento().intValue() && nivel2.getFuncionario().intValue() == objExistente.getFuncionario().intValue() && nivel2.getCategoriaDespesa().intValue() == objExistente.getCategoriaDespesa().intValue()) {
                return objExistente.getVoltarNivel1(nivel2.getCategoriaDespesa());
            }
        }
        return getNivelGraficoDWVO1().getVoltarNivel1(nivel2.getCategoriaDespesa());
    }

    public void removerNiveisPosteriores2(int indice) {
        int size = getNivelGraficoDWVO2s().size();
        if ((indice + 1) < size) {
            getNivelGraficoDWVO2s().remove(indice + 1);
            removerNiveisPosteriores2(indice);
        }
    }

    public Boolean selecionarLegenda1(Integer codigo, String nome, Double valor, String nivel) {

        try {
            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, getNivelGraficoDWVO2().getCategoriaDespesa(), true, "RE");
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, novoNivel1.getDepartamento(), novoNivel1.getFuncionario(), 0, 0, 0, "", false, "RE");
            getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                if (getNivelGraficoDWVO1().getUnidadeEnsino().intValue() == novoNivel1.getUnidadeEnsino().intValue() && getNivelGraficoDWVO1().getDepartamento().intValue() == novoNivel1.getDepartamento().intValue() && getNivelGraficoDWVO1().getFuncionario().intValue() == novoNivel1.getFuncionario().intValue() && getNivelGraficoDWVO1().getCategoriaDespesa().intValue() == novoNivel1.getCategoriaDespesa().intValue()) {
                    novoNivel1.setNumeroNivel(getNivelGraficoDWVO1().getNumeroNivel());
                }
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
                setNivel(obj.getNivel());
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                return true;
            }
            if (novoNivel2.getGrafico() != null) {
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);
            } else {
                getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                return true;
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        if (nivel != null && !nivel.equals("")) {
            return true;
        }
        setMensagemID("msg_visualizarDetalhes", Uteis.ALERTA);
        return false;
    }

    public Boolean selecionarLegenda2(Integer codigo, String nome, Double valor, String nivel) {

        try {
            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, getNivelGraficoDWVO1().getDepartamento(), getNivelGraficoDWVO1().getFuncionario(), 0, 0, 0, "", true, "RE");
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, novoNivel2.getCategoriaDespesa(), false, "RE");
            getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o último nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                return true;
            }
            if (novoNivel2.getGrafico() != null) {
                if (getNivelGraficoDWVO2().getUnidadeEnsino().intValue() == novoNivel2.getUnidadeEnsino().intValue() && getNivelGraficoDWVO2().getDepartamento().intValue() == novoNivel2.getDepartamento().intValue() && getNivelGraficoDWVO2().getFuncionario().intValue() == novoNivel2.getFuncionario().intValue() && getNivelGraficoDWVO2().getCategoriaDespesa().intValue() == novoNivel2.getCategoriaDespesa().intValue()) {
                    novoNivel2.setNumeroNivel(getNivelGraficoDWVO2().getNumeroNivel());
                }
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);

            } else {
                getNivelGraficoDWVO2().setMsg("Você atingiu o último nível de consulta.");
                setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                return true;
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        if (nivel != null && !nivel.equals("")) {
            return true;
        }
        setMensagemID("msg_visualizarDetalhes", Uteis.ALERTA);
        return false;
    }

    public boolean getNivelUnidade() {
        if (getNivelGraficoDWVO1().getNumeroNivel().intValue() == 1 || getNivelGraficoDWVO2().getNumeroNivel().intValue() == 1) {
            return true;
        }
        return false;
    }

//    public Boolean voltarFiltro1(NivelGraficoDWVO obj, Date data, Date dataFim) {
    public Boolean voltarFiltro1() {
        NivelGraficoDWVO obj = (NivelGraficoDWVO) context().getExternalContext().getRequestMap().get("filtro1Itens");
//        this.data = data;
//        this.dataFim = dataFim;
        try {
            if (obj.getNumeroNivel().intValue() != getNivelGraficoDWVO1s().size()) {
                NivelGraficoDWVO novoNivel2 = voltarNivelGrafico2(obj);
                NivelGraficoDWVO novoNivel1 = obj.getVoltarNivel1(novoNivel2.getCategoriaDespesa());
                getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(novoNivel1, novoNivel2, novoNivel1.getDefinirNivelVoltar(), data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
                if (novoNivel1.getGrafico() != null) {
                    setNivelGraficoDWVO1(novoNivel1);
                    adicionarNivelGrafico1(novoNivel1);
                    setNivel(obj.getNivelEntidade());
                } else {
                    getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                    setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                    return true;
                }
                if (novoNivel2.getGrafico() != null) {
                    setNivelGraficoDWVO2(novoNivel2);
                    adicionarNivelGrafico2(novoNivel2);
                } else {
                    getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                    setMensagemID("msg_ultimoNivelConsulta", Uteis.ALERTA);
                    return true;
                }
            } else {
                getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(getNivelGraficoDWVO1(), getNivelGraficoDWVO2(), nivel, data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

        if(nivel != null && !nivel.equals("")){
                return true;
        }
        setMensagemID("msg_visualizarDetalhes", Uteis.ALERTA);
        return false;
    }

//    public Boolean voltarFiltro2(NivelGraficoDWVO obj, Date data, Date dataFim) {
    public Boolean voltarFiltro2() {
      NivelGraficoDWVO obj = (NivelGraficoDWVO) context().getExternalContext().getRequestMap().get("filtro2Itens");
//        this.data = data;
//        this.dataFim = dataFim;
        try {
            if (obj.getNumeroNivel().intValue() != getNivelGraficoDWVO2s().size()) {
                NivelGraficoDWVO novoNivel1 = voltarNivelGrafico1(obj);
                NivelGraficoDWVO novoNivel2 = obj.getVoltarNivel2(novoNivel1.getDepartamento(), novoNivel1.getFuncionario(), 0, 0, 0, "");
                getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(novoNivel1, novoNivel2, novoNivel1.getDefinirNivelVoltar(), data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
                if (novoNivel1.getGrafico() != null) {
                    setNivelGraficoDWVO1(novoNivel1);
                    adicionarNivelGrafico1(novoNivel1);
                    setNivel(obj.getNivelEntidade());
                } else {
                    getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                }
                if (novoNivel2.getGrafico() != null) {
                    setNivelGraficoDWVO2(novoNivel2);
                    adicionarNivelGrafico2(novoNivel2);
                } else {
                    getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                }
            } else {
                getFacadeFactory().getDespesaDWFacade().gerarRelatorioGrafico(getNivelGraficoDWVO1(), getNivelGraficoDWVO2(), nivel, data, dataFim, "RE",getUnidadeEnsinoVOs(), getUsuarioLogado());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

        if(nivel != null && !nivel.equals("")){
                return true;
        }
        return false;
    }

    public Integer getElement1() {
        return getNivelGraficoDWVO1s().size();
    }

    public Integer getElement2() {
        return getNivelGraficoDWVO2s().size();
    }

    public Integer getColumn1() {
        if (getNivelGraficoDWVO1s().size() > 4) {
            return 4;
        }
        return getNivelGraficoDWVO1s().size();
    }

    public Integer getColumn2() {
        if (getNivelGraficoDWVO2s().size() > 4) {
            return 4;
        }
        return getNivelGraficoDWVO2s().size();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public NivelGraficoDWVO getNivelGraficoDWVO1() {
        return nivelGraficoDWVO1;
    }

    public void setNivelGraficoDWVO1(NivelGraficoDWVO nivelGraficoDWVO1) {
        this.nivelGraficoDWVO1 = nivelGraficoDWVO1;
    }

    public List<NivelGraficoDWVO> getNivelGraficoDWVO1s() {
        return nivelGraficoDWVO1s;
    }

    public void setNivelGraficoDWVO1s(List<NivelGraficoDWVO> nivelGraficoDWVO1s) {
        this.nivelGraficoDWVO1s = nivelGraficoDWVO1s;
    }

    public NivelGraficoDWVO getNivelGraficoDWVO2() {
        return nivelGraficoDWVO2;
    }

    public void setNivelGraficoDWVO2(NivelGraficoDWVO nivelGraficoDWVO2) {
        this.nivelGraficoDWVO2 = nivelGraficoDWVO2;
    }

    public List<NivelGraficoDWVO> getNivelGraficoDWVO2s() {
        return nivelGraficoDWVO2s;
    }

    public void setNivelGraficoDWVO2s(List<NivelGraficoDWVO> nivelGraficoDWVO2s) {
        this.nivelGraficoDWVO2s = nivelGraficoDWVO2s;
    }

    public String getNivel() {
        if (nivel == null) {
            nivel = "";
        }
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getTeste() {
        if (teste == null) {
            teste = "";
        }
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }
    
    public String getDadosGraficoConsumoNivel1(){
       return Uteis.realizarMontagemDadosGraficoPizza(getNivelGraficoDWVO1().getLegendaGraficoVOs());
    }
    public String getDadosGraficoConsumoNivel2(){
        return Uteis.realizarMontagemDadosGraficoPizza(getNivelGraficoDWVO2().getLegendaGraficoVOs());
    }
    
	public String getMontarGraficoConsumoVisible(){
		DespesaDWControle ddc = (DespesaDWControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DespesaDWControle");
		if (!ddc.getDadosGraficoConsumoNivel1().isEmpty()){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoConsumoNivelDoisVisible(){
		DespesaDWControle ddc = (DespesaDWControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DespesaDWControle");
		if (!ddc.getDadosGraficoConsumoNivel2().isEmpty()){
			return "true";
		}
		return "false";
	}
	
	public String getMontarDataGraficoConsumo(){
		return getDadosGraficoConsumoNivel1().toString();
	}
	
	public String getMontarDataGraficoConsumoNivelDois(){
		return getDadosGraficoConsumoNivel2().toString();
	}
}
