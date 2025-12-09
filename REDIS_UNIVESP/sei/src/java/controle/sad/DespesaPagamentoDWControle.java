package controle.sad;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * despesaDWForm.jsp despesaDWCons.jsp) com as funcionalidades da classe <code>DespesaDW</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see DespesaDW
 * @see DespesaDWVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.sad.NivelGraficoDWVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.sad.DespesaDWInterfaceFacade; @Controller("DespesaPagamentoDWControle") @Scope("request") @Lazy
public class DespesaPagamentoDWControle extends SuperControle implements Serializable {

    private NivelGraficoDWVO nivelGraficoDWVO1;
    private NivelGraficoDWVO nivelGraficoDWVO2;
    private List<NivelGraficoDWVO> nivelGraficoDWVO1s;
    private List<NivelGraficoDWVO> nivelGraficoDWVO2s;
    private DespesaDWInterfaceFacade despesaDWFacade = null;
    protected String nivel;
    protected String teste;
    protected Date data;
    protected Date dataFim;

    public DespesaPagamentoDWControle() throws Exception {
        //obterUsuarioLogado();
        novo();
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>DespesaDW</code>
     * para edição pelo usuário da aplicação.
     */
    public void novo() {
        setNivelGraficoDWVO1(new NivelGraficoDWVO());
        setNivelGraficoDWVO2(new NivelGraficoDWVO());
        setNivelGraficoDWVO1s(new ArrayList<NivelGraficoDWVO>(0));
        setNivelGraficoDWVO2s(new ArrayList<NivelGraficoDWVO>(0));
        setData(Uteis.obterDataFutura(new Date(), -30l));
        setDataFim(new Date());
        setNivel("");
        setTeste("");
        gerarGraficos(data, dataFim);
        getNivelGraficoDWVO1s().add(nivelGraficoDWVO1);
        getNivelGraficoDWVO2s().add(nivelGraficoDWVO2);
        setMensagemID("msg_entre_dados");
    }

    public List listaGrafico(){
        return getNivelGraficoDWVO1().getLegendaGraficoVOs();
    }

    public List listaGrafico2(){
        return getNivelGraficoDWVO2().getLegendaGraficoVOs();
    }

    public void gerarGraficos(Date data, Date dataFim) {
        try {
            this.data = data;
            this.dataFim = dataFim;
            despesaDWFacade.gerarRelatorioGrafico(nivelGraficoDWVO1, nivelGraficoDWVO2, nivel, this.data, this.dataFim, "PA", null, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean selecionarLegenda(Integer codigo, String nome, Double valor, String nivel, Date data, Date dataFim) {
        try {
             setData(data);
        setDataFim(dataFim); 
//            LegendaGraficoVO obj = (LegendaGraficoVO) context().getExternalContext().getRequestMap().get("legenda");
            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, 0, true, "PA");
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, 0, 0, 0, 0, 0,"", true, "PA");
            despesaDWFacade.gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), this.data, this.dataFim, "PA", null, getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
                setNivel(obj.getNivel());
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                return false;
            }
            if (novoNivel2.getGrafico() != null) {
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);
                setNivel(obj.getNivel());
            } else {

                getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                return false;
            }
        } catch (Exception e) {

            setMensagemDetalhada("msg_erro", e.getMessage());
            return false;
        }
        return true;
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
            if (nivel1.getUnidadeEnsino().intValue() == objExistente.getUnidadeEnsino().intValue()
                && nivel1.getDepartamento().intValue() == objExistente.getDepartamento().intValue()
                && nivel1.getFuncionarioFavorecido().intValue() == objExistente.getFuncionarioFavorecido().intValue()
                && nivel1.getBancoFavorecido().intValue() == objExistente.getBancoFavorecido().intValue()
                && nivel1.getFornecedorFavorecido().intValue() == objExistente.getFornecedorFavorecido().intValue()
                && nivel1.getTipoFavorecido().equals(objExistente.getTipoFavorecido())
                && nivel1.getCategoriaDespesa().intValue() == objExistente.getCategoriaDespesa().intValue()) {
                
                return objExistente.getVoltarNivel2(nivel1.getDepartamento(), 0, nivel1.getFuncionarioFavorecido(),
                                                    nivel1.getBancoFavorecido(),nivel1.getFornecedorFavorecido(),
                                                    nivel1.getTipoFavorecido());
            }
        }
         return getNivelGraficoDWVO2().getVoltarNivel2(nivel1.getDepartamento(), 0, nivel1.getFuncionarioFavorecido(),
                                                    nivel1.getBancoFavorecido(),nivel1.getFornecedorFavorecido(),
                                                    nivel1.getTipoFavorecido());
    }

    public NivelGraficoDWVO voltarNivelGrafico1(NivelGraficoDWVO nivel2) {
        for (NivelGraficoDWVO objExistente : getNivelGraficoDWVO1s()) {
             if (nivel2.getUnidadeEnsino().intValue() == objExistente.getUnidadeEnsino().intValue()
                && nivel2.getDepartamento().intValue() == objExistente.getDepartamento().intValue()
                && nivel2.getFuncionarioFavorecido().intValue() == objExistente.getFuncionarioFavorecido().intValue()
                && nivel2.getBancoFavorecido().intValue() == objExistente.getBancoFavorecido().intValue()
                && nivel2.getFornecedorFavorecido().intValue() == objExistente.getFornecedorFavorecido().intValue()
                && nivel2.getTipoFavorecido().equals(objExistente.getTipoFavorecido())
                && nivel2.getCategoriaDespesa().intValue() == objExistente.getCategoriaDespesa().intValue()) {

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

    public Boolean selecionarLegenda1(Integer codigo, String nome, Double valor, String nivel, Date data, Date dataFim) {

        setData(data);
        setDataFim(dataFim); 
        try {
//            LegendaGraficoVO obj = (LegendaGraficoVO) context().getExternalContext().getRequestMap().get("legenda1");
            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, getNivelGraficoDWVO2().getCategoriaDespesa(), true, "PA");
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, 0, 0, 0, 0, 0, "", false, "PA");
            despesaDWFacade.gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), this.data, this.dataFim, "PA", null, getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                if (getNivelGraficoDWVO1().getUnidadeEnsino().intValue() == novoNivel1.getUnidadeEnsino().intValue()
                && getNivelGraficoDWVO1().getDepartamento().intValue() == novoNivel1.getDepartamento().intValue()
                && getNivelGraficoDWVO1().getFuncionarioFavorecido().intValue() == novoNivel1.getFuncionarioFavorecido().intValue()
                && getNivelGraficoDWVO1().getFornecedorFavorecido().intValue() == novoNivel1.getFornecedorFavorecido().intValue()
                && getNivelGraficoDWVO1().getBancoFavorecido().intValue() == novoNivel1.getBancoFavorecido().intValue()
                && getNivelGraficoDWVO1().getTipoFavorecido().equals(novoNivel1.getTipoFavorecido())
                && getNivelGraficoDWVO1().getCategoriaDespesa().intValue() == novoNivel1.getCategoriaDespesa().intValue()) {
                    novoNivel1.setNumeroNivel(getNivelGraficoDWVO1().getNumeroNivel());
                    novoNivel1.setMsg("Você atingiu o ultimo nível de consulta.");
                }else{
                    novoNivel1.setMsg("");
                }
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
                setNivel(obj.getNivel());
                //getNivelGraficoDWVO1().setMsg("")
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
            }
            if (novoNivel2.getGrafico() != null) {
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);
                getNivelGraficoDWVO2().setMsg("");
            } else {
                getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");                
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
        if(nivel != null && !nivel.equals("")){
                return true;
        }
        return false;
    }

   public Boolean selecionarLegenda2(Integer codigo, String nome, Double valor, String nivel, Date data, Date dataFim) {

        setData(data);
        setDataFim(dataFim);

        try {
//            LegendaGraficoVO obj = (LegendaGraficoVO) context().getExternalContext().getRequestMap().get("legenda2");
            LegendaGraficoVO obj = new LegendaGraficoVO(codigo, nome, nivel, valor);
            NivelGraficoDWVO novoNivel2 = getNivelGraficoDWVO2().getNovoNivel2(obj, 0, 0, 0, 0, 0,"", true, "PA");
            NivelGraficoDWVO novoNivel1 = getNivelGraficoDWVO1().getNovoNivel1(obj, novoNivel2.getCategoriaDespesa(), false, "PA");
            despesaDWFacade.gerarRelatorioGrafico(novoNivel1, novoNivel2, obj.getNivel(), this.data, this.dataFim, "PA", null, getUsuarioLogado());
            if (novoNivel1.getGrafico() != null) {
                setNivelGraficoDWVO1(novoNivel1);
                adicionarNivelGrafico1(novoNivel1);
                getNivelGraficoDWVO1().setMsg("");
            } else {
                getNivelGraficoDWVO1().setMsg("Você atingiu o último nível de consulta.");
            }
            if (novoNivel2.getGrafico() != null) {
                if (getNivelGraficoDWVO2().getUnidadeEnsino().intValue() == novoNivel2.getUnidadeEnsino().intValue()
                && getNivelGraficoDWVO2().getDepartamento().intValue() == novoNivel2.getDepartamento().intValue()
                && getNivelGraficoDWVO2().getFuncionarioFavorecido().intValue() == novoNivel2.getFuncionarioFavorecido().intValue()
                && getNivelGraficoDWVO2().getFornecedorFavorecido().intValue() == novoNivel2.getFornecedorFavorecido().intValue()
                && getNivelGraficoDWVO2().getBancoFavorecido().intValue() == novoNivel2.getBancoFavorecido().intValue()
                && getNivelGraficoDWVO2().getTipoFavorecido().equals(novoNivel2.getTipoFavorecido())
                && getNivelGraficoDWVO2().getCategoriaDespesa().intValue() == novoNivel2.getCategoriaDespesa().intValue()) {
                    novoNivel2.setNumeroNivel(getNivelGraficoDWVO2().getNumeroNivel());
                    novoNivel2.setMsg("Você atingiu o último nível de consulta.");
                }else{
                novoNivel2.setMsg("");
                }
                setNivelGraficoDWVO2(novoNivel2);
                adicionarNivelGrafico2(novoNivel2);
                
            } else {
                getNivelGraficoDWVO2().setMsg("Você atingiu o último nível de consulta.");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        if(nivel != null && !nivel.equals("")){
                return true;
        }
        return false;
    }

    public boolean getNivelUnidade() {
        if (getNivelGraficoDWVO1().getNumeroNivel().intValue() == 1 || getNivelGraficoDWVO2().getNumeroNivel().intValue() == 1) {
            return true;
        }
        return false;
    }

    public Boolean voltarFiltro1(NivelGraficoDWVO obj, Date data, Date dataFim) {
        setData(data);
        setDataFim(dataFim);
        //NivelGraficoDWVO obj = (NivelGraficoDWVO) context().getExternalContext().getRequestMap().get("filtro1");

        try {
            if (obj.getNumeroNivel().intValue() != getNivelGraficoDWVO1s().size()) {
                NivelGraficoDWVO novoNivel2 = voltarNivelGrafico2(obj);
                NivelGraficoDWVO novoNivel1 = obj.getVoltarNivel1(novoNivel2.getCategoriaDespesa());
                despesaDWFacade.gerarRelatorioGrafico(novoNivel1, novoNivel2, novoNivel1.getDefinirNivelVoltar(), this.data, this.dataFim, "PA", null, getUsuarioLogado());
                if (novoNivel1.getGrafico() != null) {
                    setNivelGraficoDWVO1(novoNivel1);
                    adicionarNivelGrafico1(novoNivel1);
                    setNivel(obj.getNivelEntidade());
                    getNivelGraficoDWVO1().setMsg("");
                } else {
                    getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                }
                if (novoNivel2.getGrafico() != null) {
                    setNivelGraficoDWVO2(novoNivel2);
                    adicionarNivelGrafico2(novoNivel2);
                    getNivelGraficoDWVO2().setMsg("");
                } else {
                    getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                }
            } else {
                despesaDWFacade.gerarRelatorioGrafico(getNivelGraficoDWVO1(), getNivelGraficoDWVO2(), nivel, this.data, this.dataFim, "PA", null, getUsuarioLogado());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
         if(nivel != null && !nivel.equals("")){
                return true;
        }
        return false;

    }

    public Boolean voltarFiltro2(NivelGraficoDWVO obj, Date data, Date dataFim) {
        //NivelGraficoDWVO obj = (NivelGraficoDWVO) context().getExternalContext().getRequestMap().get("filtro2");
        setData(data);
        setDataFim(dataFim);
        try {
            if (obj.getNumeroNivel().intValue() != getNivelGraficoDWVO2s().size()) {
                NivelGraficoDWVO novoNivel1 = voltarNivelGrafico1(obj);
                NivelGraficoDWVO novoNivel2 = obj.getVoltarNivel2(novoNivel1.getDepartamento(), 0, novoNivel1.getFuncionarioFavorecido(),
                                                                   novoNivel1.getBancoFavorecido(), novoNivel1.getFornecedorFavorecido(),
                                                                   novoNivel1.getTipoFavorecido());
                despesaDWFacade.gerarRelatorioGrafico(novoNivel1, novoNivel2, novoNivel1.getDefinirNivelVoltar(), data, dataFim, "PA", null, getUsuarioLogado());
                if (novoNivel1.getGrafico() != null) {
                    setNivelGraficoDWVO1(novoNivel1);
                    adicionarNivelGrafico1(novoNivel1);
                    setNivel(obj.getNivelEntidade());
                    getNivelGraficoDWVO1().setMsg("");
                } else {
                    getNivelGraficoDWVO1().setMsg("Você atingiu o ultimo nível de consulta.");
                }
                if (novoNivel2.getGrafico() != null) {
                    setNivelGraficoDWVO2(novoNivel2);
                    adicionarNivelGrafico2(novoNivel2);
                    getNivelGraficoDWVO2().setMsg("");
                } else {
                    getNivelGraficoDWVO2().setMsg("Você atingiu o ultimo nível de consulta.");
                }
            } else {
                despesaDWFacade.gerarRelatorioGrafico(getNivelGraficoDWVO1(), getNivelGraficoDWVO2(), nivel, this.data, this.dataFim, "PA", null, getUsuarioLogado());
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
        if(teste == null){
            teste = "";
        }
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }


}
