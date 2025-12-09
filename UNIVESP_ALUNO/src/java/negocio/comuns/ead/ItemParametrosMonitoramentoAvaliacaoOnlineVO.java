package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 23/03/2015
 *
 */
public class ItemParametrosMonitoramentoAvaliacaoOnlineVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO;
	private String descricaoParametro;
	private Double percentualAcertosDe;
	private Double percentualAcertosAte;
	private String corGrafico;
	private String corLetraGrafico;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ParametrosMonitoramentoAvaliacaoOnlineVO getParametrosMonitoramentoAvaliacaoOnlineVO() {
		if (parametrosMonitoramentoAvaliacaoOnlineVO == null) {
			parametrosMonitoramentoAvaliacaoOnlineVO = new ParametrosMonitoramentoAvaliacaoOnlineVO();
		}
		return parametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public void setParametrosMonitoramentoAvaliacaoOnlineVO(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO) {
		this.parametrosMonitoramentoAvaliacaoOnlineVO = parametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public String getDescricaoParametro() {
		if(descricaoParametro == null) {
			descricaoParametro = "";
		}
		return descricaoParametro;
	}

	public void setDescricaoParametro(String descricaoParametro) {
		this.descricaoParametro = descricaoParametro;
	}

	public Double getPercentualAcertosDe() {
		if (percentualAcertosDe == null) {
			percentualAcertosDe = 0.0;
		}
		return percentualAcertosDe;
	}

	public void setPercentualAcertosDe(Double percentualAcertosDe) {
		this.percentualAcertosDe = percentualAcertosDe;
	}

	public Double getPercentualAcertosAte() {
		if (percentualAcertosAte == null) {
			percentualAcertosAte = 0.0;
		}
		return percentualAcertosAte;
	}

	public void setPercentualAcertosAte(Double percentualAcertosAte) {
		this.percentualAcertosAte = percentualAcertosAte;
	}

	public String getCorGrafico() {
		if (corGrafico == null) {
			corGrafico = "#FFFFFF";
		}
		return corGrafico;
	}

	public void setCorGrafico(String corGrafico) {
		this.corGrafico = corGrafico;
	}

	public String getCorLetraGrafico() {
		if (corLetraGrafico == null) {
			corLetraGrafico = "#FFFFFF";
		}
		return corLetraGrafico;
	}

	public void setCorLetraGrafico(String corLetraGrafico) {
		this.corLetraGrafico = corLetraGrafico;
	}
}
