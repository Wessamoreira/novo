package webservice.pix.comuns;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "devolucoes")
public class DevolucaoPixRSVO {

	private String id;
	private String rtrId;
	private String valor;
	private String status;
	private String motivo;
	private HorarioDevolucaoPixRSVO horario;

	@XmlElement(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name = "rtrId")
	public String getRtrId() {
		return rtrId;
	}

	public void setRtrId(String rtrId) {
		this.rtrId = rtrId;
	}

	@XmlElement(name = "valor")
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "motivo")
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@XmlElement(name = "horario")
	public HorarioDevolucaoPixRSVO getHorario() {
		return horario;
	}

	public void setHorario(HorarioDevolucaoPixRSVO horario) {
		this.horario = horario;
	}

}
