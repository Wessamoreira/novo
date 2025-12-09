package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Tomador {

	@XStreamAlias("IdentificacaoTomador")
	private final IdentificacaoTomador IdentificacaoTomador;

	@XStreamAlias("RazaoSocial")
	private String RazaoSocial;

	@XStreamAlias("Endereco")
	private Endereco Endereco;

	@XStreamAlias("Contato")
	private Contato Contato;

	public Tomador() {
		IdentificacaoTomador = new IdentificacaoTomador();
		Endereco = new Endereco();
	}

	public Endereco getEndereco() {
		return Endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.Endereco = endereco;
	}

	public String getRazaoSocial() {
		return RazaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		RazaoSocial = razaoSocial;
	}

	public IdentificacaoTomador getIdentificacaoTomador() {
		return IdentificacaoTomador;
	}

	public Contato getContato() {
		return Contato;
	}

	public void setContato(Contato contato) {
		Contato = contato;
	}

}
