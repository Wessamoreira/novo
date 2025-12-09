package webservice.moodle;

import com.google.gson.annotations.SerializedName;

public class NotasMotivoItemRSVO extends NotasItemRSVO {
	
	
	@SerializedName("motivo")
	private String motivo;
	
	
	public String getMotivo() {
		if (motivo == null)
			motivo = "";
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}