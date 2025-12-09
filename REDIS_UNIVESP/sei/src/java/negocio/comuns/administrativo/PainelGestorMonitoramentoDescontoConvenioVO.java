/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;

public class PainelGestorMonitoramentoDescontoConvenioVO implements Serializable {

    private String convenio;
    private Double totalFaturadoConvenio;
    private Integer numeroMatriculaConvenio;
    private Double percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio;
    private Double percentualAtualInadimplenciaConvenio;
    private Double totalRecebidoConvenio;
    private Double totalAtrazadoConvenio;
    private Double totalReceberConvenio;
    private Double totalDescontoRecebidoConvenio;
    
    public static final long serialVersionUID = 1L;

    public String getConvenio() {
        if (convenio == null) {
            convenio = "";
        }
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Integer getNumeroMatriculaConvenio() {
        if (numeroMatriculaConvenio == null) {
            numeroMatriculaConvenio = 0;
        }
        return numeroMatriculaConvenio;
    }

    public void setNumeroMatriculaConvenio(Integer numeroMatriculaConvenio) {
        this.numeroMatriculaConvenio = numeroMatriculaConvenio;
    }

    public Double getPercentualAtualInadimplenciaConvenio() {
        if (percentualAtualInadimplenciaConvenio == null) {
            percentualAtualInadimplenciaConvenio = 0.0;
        }
        return percentualAtualInadimplenciaConvenio;
    }

    public void setPercentualAtualInadimplenciaConvenio(Double percentualAtualInadimplenciaConvenio) {
        this.percentualAtualInadimplenciaConvenio = percentualAtualInadimplenciaConvenio;
    }

    public Double getPercentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio() {
        if (percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio == null) {
            percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio = 0.0;
        }
        return percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio;
    }

    public void setPercentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio(Double percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio) {
        this.percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio = percentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio;
    }

    public Double getTotalAtrazadoConvenio() {
        if (totalAtrazadoConvenio == null) {
            totalAtrazadoConvenio = 0.0;
        }
        return totalAtrazadoConvenio;
    }

    public void setTotalAtrazadoConvenio(Double totalAtrazadoConvenio) {
        this.totalAtrazadoConvenio = totalAtrazadoConvenio;
    }

    public Double getTotalFaturadoConvenio() {
        if (totalFaturadoConvenio == null) {
            totalFaturadoConvenio = 0.0;
        }
        return totalFaturadoConvenio;
    }

    public void setTotalFaturadoConvenio(Double totalFaturadoConvenio) {
        this.totalFaturadoConvenio = totalFaturadoConvenio;
    }

    public Double getTotalReceberConvenio() {
        if (totalReceberConvenio == null) {
            totalReceberConvenio = 0.0;
        }
        return totalReceberConvenio;
    }

    public void setTotalReceberConvenio(Double totalReceberConvenio) {
        this.totalReceberConvenio = totalReceberConvenio;
    }

    public Double getTotalRecebidoConvenio() {
        if (totalRecebidoConvenio == null) {
            totalRecebidoConvenio = 0.0;
        }
        return totalRecebidoConvenio;
    }

    public void setTotalRecebidoConvenio(Double totalRecebidoConvenio) {
        this.totalRecebidoConvenio = totalRecebidoConvenio;
    }

	public Double getTotalDescontoRecebidoConvenio() {
		if(totalDescontoRecebidoConvenio == null){
			totalDescontoRecebidoConvenio = 0.0;
		}
		return totalDescontoRecebidoConvenio;
	}

	public void setTotalDescontoRecebidoConvenio(Double totalDescontoRecebidoConvenio) {
		this.totalDescontoRecebidoConvenio = totalDescontoRecebidoConvenio;
	}
    
    
}
