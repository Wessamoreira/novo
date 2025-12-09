package webservice.nfse.belohorizonte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrestadorBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(Prestador.class);
	private Prestador prestador;
	
	public PrestadorBuilder() {
		prestador = new Prestador();
	}
	
	public Prestador build() {
		return prestador;
	}

	public PrestadorBuilder withCnpj(String string) {
		LOG.debug("Pretador CNPJ: {}", string);
		
		prestador.setCnpj(string);
		return this;
	}
	
	public PrestadorBuilder withInscricaoMunicipal(String string) {
		LOG.debug("Pretador INSCRICAO MUNICIPAL: {}", string);		
		prestador.setInscricaoMunicipal(string);
		return this;
	}

}
