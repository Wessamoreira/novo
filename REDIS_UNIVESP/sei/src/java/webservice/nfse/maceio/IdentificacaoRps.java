package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class IdentificacaoRps {
	
	@XStreamAlias("ns3:Numero")
	private Long Numero;
	
	@XStreamAlias("ns3:Serie")
	private Serie serie;
	
	@XStreamAlias("ns3:Tipo")
	private Tipo tipo;

	public IdentificacaoRps() {
		serie = Serie.TESTE;
		tipo = Tipo.RPS;
	}

	public Long getNumero() {
		return Numero;
	}

	public void setNumero(Long numero) {
		Numero = numero;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
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