package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tc:Tomador")
public class Tomador {
	
	@XStreamAlias("tc:IdentificacaoTomador")
	private final IdentificacaoTomador IdentificacaoTomador;
	@XStreamAlias("tc:RazaoSocial")
	private String RazaoSocial;
	@XStreamAlias("tc:Endereco")
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
