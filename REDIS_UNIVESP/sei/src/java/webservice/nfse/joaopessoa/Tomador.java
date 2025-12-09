package webservice.nfse.joaopessoa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Tomador {
	
	@XStreamAlias("nfse:IdentificacaoTomador")
	private final IdentificacaoTomador IdentificacaoTomador;
	
	@XStreamAlias("nfse:RazaoSocial")
	private String RazaoSocial;
	
	@XStreamAlias("nfse:Endereco")
	private Endereco Endereco;
	
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

}
