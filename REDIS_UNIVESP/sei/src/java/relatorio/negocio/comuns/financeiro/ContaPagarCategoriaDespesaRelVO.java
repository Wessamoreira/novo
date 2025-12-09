package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ContaPagarCategoriaDespesaRelVO {

    private String descricaoCategoriaDespesa;
    private Double valor;
    private Double valorTotal;
    private Double porcentagemValorTotal;
    private String identificadorCategoriaDespesa;
    private Integer codigoCategoriaDespesa;
    private Integer codigoCategoriaDespesaPrincipal;
    private Boolean valorJaCalculado;
    private Boolean valorTotalJaCalculado;

    private List<ContaPagarCategoriaDespesaRelVO> contaPagarCategoriaDespesaRelVOs;

    public String getDescricaoCategoriaDespesa() {
        return descricaoCategoriaDespesa;
    }

    public void setDescricaoCategoriaDespesa(String descricaoCategoriaDespesa) {
        this.descricaoCategoriaDespesa = descricaoCategoriaDespesa;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getPorcentagemValorTotal() {
        if (porcentagemValorTotal == null) {
            porcentagemValorTotal = 0.0;
        }
        return porcentagemValorTotal;
    }

    public void setPorcentagemValorTotal(Double porcentagemValorTotal) {
        this.porcentagemValorTotal = porcentagemValorTotal;
    }

    public String getIdentificadorCategoriaDespesa() {
        return identificadorCategoriaDespesa;
    }

    public void setIdentificadorCategoriaDespesa(String identificadorCategoriaDespesa) {
        this.identificadorCategoriaDespesa = identificadorCategoriaDespesa;
    }

    public Integer getCodigoCategoriaDespesa() {
        return codigoCategoriaDespesa;
    }

    public void setCodigoCategoriaDespesa(Integer codigoCategoriaDespesa) {
        this.codigoCategoriaDespesa = codigoCategoriaDespesa;
    }

    public Integer getCodigoCategoriaDespesaPrincipal() {
        return codigoCategoriaDespesaPrincipal;
    }

    public void setCodigoCategoriaDespesaPrincipal(Integer codigoCategoriaDespesaPrincipal) {
        this.codigoCategoriaDespesaPrincipal = codigoCategoriaDespesaPrincipal;
    }
    
    public JRDataSource getContaPagarCategoriaDespesaRelVOsJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getContaPagarCategoriaDespesaRelVOs().toArray());
        return jr;
    }

    public List<ContaPagarCategoriaDespesaRelVO> getContaPagarCategoriaDespesaRelVOs() {
        if (contaPagarCategoriaDespesaRelVOs == null) {
            contaPagarCategoriaDespesaRelVOs = new ArrayList<ContaPagarCategoriaDespesaRelVO>(0);
        }
        return contaPagarCategoriaDespesaRelVOs;
    }

    public void setContaPagarCategoriaDespesaRelVOs(List<ContaPagarCategoriaDespesaRelVO> contaPagarCategoriaDespesaRelVOs) {
        this.contaPagarCategoriaDespesaRelVOs = contaPagarCategoriaDespesaRelVOs;
    }

    
    public Boolean getValorJaCalculado() {
        if (valorJaCalculado == null) {
            valorJaCalculado = false;
        }
        return valorJaCalculado;
    }

    
    public void setValorJaCalculado(Boolean valorJaCalculado) {
        this.valorJaCalculado = valorJaCalculado;
    }

	public Double getValorTotal() {
		if(valorTotal == null){
			valorTotal = 0.0;
		}
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Boolean getValorTotalJaCalculado() {
		if(valorTotalJaCalculado == null){
			valorTotalJaCalculado = false;
		}
		return valorTotalJaCalculado;
	}

	public void setValorTotalJaCalculado(Boolean valorTotalJaCalculado) {
		this.valorTotalJaCalculado = valorTotalJaCalculado;
	}
    
    

}
