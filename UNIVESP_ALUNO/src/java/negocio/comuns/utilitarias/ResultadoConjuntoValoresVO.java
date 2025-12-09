package negocio.comuns.utilitarias;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class ResultadoConjuntoValoresVO <T extends SuperVO> {

	String codigosDoConjunto;
	Double valoresDoConjunto;
	List<T> listaDeConjunto;

	public String getCodigosDoConjunto() {
		if (codigosDoConjunto == null) {
			codigosDoConjunto = "";
		}
		return codigosDoConjunto;
	}

	public void setCodigosDoConjunto(String codigosDoConjunto) {
		this.codigosDoConjunto = codigosDoConjunto;
	}

	public Double getValoresDoConjunto() {
		if (valoresDoConjunto == null) {
			valoresDoConjunto = 0.0;
		}
		return valoresDoConjunto;
	}

	public void setValoresDoConjunto(Double valoresDoConjunto) {
		this.valoresDoConjunto = valoresDoConjunto;
	}

	public List<T> getListaDeConjunto() {
		if (listaDeConjunto == null) {
			listaDeConjunto = new ArrayList<T>();
		}
		return listaDeConjunto;
	}

	public void setListaDeConjunto(List<T> listaDeConjunto) {
		this.listaDeConjunto = listaDeConjunto;
	}

}
