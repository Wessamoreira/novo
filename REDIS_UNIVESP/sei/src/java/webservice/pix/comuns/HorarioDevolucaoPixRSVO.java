package webservice.pix.comuns;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "horario")
public class HorarioDevolucaoPixRSVO {

	/*
	 * Horário no qual a devolução foi solicitada no PSP.
	 */
	private String solicitacao;

	/*
	 * Horário no qual a devolução foi liquidada no PSP.
	 */
	private String liquidacao;

	@XmlElement(name = "solicitacao")
	public String getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(String solicitacao) {
		this.solicitacao = solicitacao;
	}

	@XmlElement(name = "liquidacao")
	public String getLiquidacao() {
		return liquidacao;
	}

	public void setLiquidacao(String liquidacao) {
		this.liquidacao = liquidacao;
	}

}
