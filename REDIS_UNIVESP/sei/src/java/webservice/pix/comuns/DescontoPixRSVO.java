package webservice.pix.comuns;

import java.util.List;

public class DescontoPixRSVO {
	/*
	 * Valores aceito de 1 ate 6
	 * Sendo 1 valor fixo ate as datas informadas
	 * Sendo 2 percentual ate as datas informadas
	 * Sendo 3 Valor por antecipação dia corridos
	 * Sendo 4 Valor por antecipação dia útil
	 * Sendo 5 Percentual por antecipação dia corrido
	 * Sendo 6 Percentual por antecipação dia útil
	 */
	private Integer modalidade;
	private List<DescontoDataFixaPixRSVO> descontoDataFixa;
	/*
	 * pattern: \d{1,10}\.\d{2}
	 */
	private String valorPerc;

	public Integer getModalidade() {
		return modalidade;
	}

	public void setModalidade(Integer modalidade) {
		this.modalidade = modalidade;
	}

	public List<DescontoDataFixaPixRSVO> getDescontoDataFixa() {
		return descontoDataFixa;
	}

	public void setDescontoDataFixa(List<DescontoDataFixaPixRSVO> descontoDataFixa) {
		this.descontoDataFixa = descontoDataFixa;
	}

	public String getValorPerc() {
		return valorPerc;
	}

	public void setValorPerc(String valorPerc) {
		this.valorPerc = valorPerc;
	}
	
	

}
