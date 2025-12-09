package webservice.nfse.portoalegre;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpsBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(RpsBuilder.class);

	private final Rps rps;

	private final InfDeclaracaoPrestacaoServico infDeclaracaoPrestacaoServico;

	private final IdentificacaoRps identificacaoRps;

	protected RpsBuilder() {
		rps = new Rps();
		infDeclaracaoPrestacaoServico = new InfDeclaracaoPrestacaoServico();
		identificacaoRps = new IdentificacaoRps();
	}

	public Rps build() {
		rps.setInfDeclaracaoPrestacaoServico(infDeclaracaoPrestacaoServico);
		return rps;
	}

	// public RpsBuilder withInfId(String id) {
	// infDeclaracaoPrestacaoServico.setId(id);
	// return this;
	// }

	public InfDeclaracaoPrestacaoServico getInf() {
		return infDeclaracaoPrestacaoServico;
	}

	public RpsBuilder withId(String id) {
		infDeclaracaoPrestacaoServico.setId(id);
		return this;
	}

	public RpsBuilder withData(Calendar dataEmissao) {
		LOG.debug("Emissão: {}", convertDateTime(dataEmissao));

		infDeclaracaoPrestacaoServico.setDataEmissao(dataEmissao);
		return this;
	}
	
	public RpsBuilder withSerie(Serie serie) {
		LOG.debug("Serie: ", serie);		
		infDeclaracaoPrestacaoServico.getIdentificacaoRps().setSerie(serie);
		return this;
	}

	public RpsBuilder normal() {
		infDeclaracaoPrestacaoServico.setStatus(Status.NORMAL);
		return this;
	}

	public RpsBuilder cancelado() {
		infDeclaracaoPrestacaoServico.setStatus(Status.CANCELADO);
		return this;
	}

	public RpsBuilder withNumero(long numero) {
		LOG.debug("RPS: {}", numero);

		identificacaoRps.setNumero(numero);
		return this;
	}

	public RpsBuilder tipoRps() {
		identificacaoRps.setTipo(Tipo.RPS);
		return this;
	}

	public RpsBuilder tipoMista() {
		identificacaoRps.setTipo(Tipo.MISTA);
		return this;
	}

	public RpsBuilder tipoCupom() {
		identificacaoRps.setTipo(Tipo.CUPOM);
		return this;
	}

//	public RpsBuilder withCompetencia(Calendar competencia) {
//		if (competencia != null) {
//			LOG.debug("Competencia: {}", convertDate(competencia));
//
//			infDeclaracaoPrestacaoServico.setCompetencia(competencia);
//		}
//		return this;
//	}

//	private String convertDate(Calendar date) {
//		return new DateConverter().toString(date);
//	}

	private String convertDateTime(Calendar date) {
		return new DateTimeConverter().toString(date);
	}

	public RpsBuilder withServico(Servico servico) {
		infDeclaracaoPrestacaoServico.setServico(servico);
		return this;
	}

	public RpsBuilder withPrestador(Prestador prestador) {
		infDeclaracaoPrestacaoServico.setPrestador(prestador);
		return this;
	}

	public RpsBuilder withTomador(Tomador tomador) {
		infDeclaracaoPrestacaoServico.setTomador(tomador);
		return this;
	}

	public RpsBuilder optanteSimplesNacional() {
		infDeclaracaoPrestacaoServico.setOptanteSimplesNacional(SimNao.SIM);
		return this;
	}

	public RpsBuilder naoOptanteSimplesNacional() {
		infDeclaracaoPrestacaoServico.setOptanteSimplesNacional(SimNao.NAO);
		return this;
	}

	public RpsBuilder comIncentivoFiscal() {
		infDeclaracaoPrestacaoServico.setIncentivadorCultural(SimNao.SIM);
		return this;
	}

	public RpsBuilder semIncentivoFiscal() {
		infDeclaracaoPrestacaoServico.setIncentivadorCultural(SimNao.NAO);
		return this;
	}
}
