package webservice.moodle;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NotasRSVO {

	@SerializedName("notas")
	private List<NotasItemRSVO> notas;
	
	@SerializedName("notas_inseridas")
	private List<NotasItemRSVO> notasInseridas;
	
	@SerializedName("notas_nao_inseridas")
	private List<NotasMotivoItemRSVO> notasNaoInseridas;

	public List<NotasItemRSVO> getNotas() {
		if (notas == null)
			notas = new ArrayList<>();
		return notas;
	}

	public void setNotas(List<NotasItemRSVO> notas) {
		this.notas = notas;
	}

	public List<NotasItemRSVO> getNotasInseridas() {
		if (notasInseridas == null)
			notasInseridas = new ArrayList<>();
		return notasInseridas;
	}

	public void setNotasInseridas(List<NotasItemRSVO> notasInseridas) {
		this.notasInseridas = notasInseridas;
	}

	public List<NotasMotivoItemRSVO> getNotasNaoInseridas() {
		if (notasNaoInseridas == null)
			notasNaoInseridas = new ArrayList<>();
		return notasNaoInseridas;
	}

	public void setNotasNaoInseridas(List<NotasMotivoItemRSVO> notasNaoInseridas) {
		this.notasNaoInseridas = notasNaoInseridas;
	}
	
}