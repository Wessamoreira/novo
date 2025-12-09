package webservice.nfse.araguaina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomadorBuilder {

	private final Tomador tomador;
	private static final Logger LOG = LoggerFactory.getLogger(TomadorBuilder.class);

	public TomadorBuilder() {
		tomador = new Tomador();
	}
	
	public Tomador build() {
		return tomador;
	}
	
	public TomadorBuilder withCnpj(String string) {
		LOG.debug("Tomador: {}", string);
		
		tomador.getIdentificacaoTomador().getCpfCnpj().setCnpj(string);
		return this;
	}

	public TomadorBuilder withCpf(String string) {
		LOG.debug("Tomador: {}", string);
		
		tomador.getIdentificacaoTomador().getCpfCnpj().setCpf(string);
		return this;
	}

	public TomadorBuilder withEndereco(Endereco endereco) {
		tomador.setEndereco(endereco);
		return this;
	}
	
	public TomadorBuilder withContato(Contato contato) {
		tomador.setContato(contato);
		return this;
	}

	public TomadorBuilder withRazaoSocial(String string) {
		LOG.debug("Tomador: {}", string);
		
		tomador.setRazaoSocial(string);
		return this;
	}
	
}