package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tc:IdentificacaoRps")
public class IdentificacaoRps {

	@XStreamAlias("tc:Numero")
	private Long Numero;
	
	@XStreamAlias("tc:Serie")
	private String serie;
	
	@XStreamAlias("tc:Tipo")
	private Tipo tipo;

	public IdentificacaoRps() {
		tipo = Tipo.RPS;
	}

	public Long getNumero() {
		return Numero;
	}

	public void setNumero(Long numero) {
		Numero = numero;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "IdentificacaoRps [Numero=" + Numero + ", Serie=" + serie
				+ ", Tipo=" + tipo + "]";
	}
}