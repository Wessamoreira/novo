package webservice.nfse.riodejaneiro;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("InfRps")
public class InfDeclaracaoPrestacaoServico {
	
	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd";

	@XStreamAsAttribute
	private String Id;
	
	private IdentificacaoRps IdentificacaoRps;
	
	@XStreamConverter(DateTimeConverter.class)
	private Calendar DataEmissao;
	
	@XStreamAlias("NaturezaOperacao")
	private String NaturezaOperacao;
		
	@XStreamAlias("OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("IncentivadorCultural")
	private SimNao IncentivadorCultural;
	
	@XStreamAlias("Status")
	private Status status;
	
	private Servico Servico;
	
	private Prestador Prestador;
	
	private Tomador Tomador;
		
	public InfDeclaracaoPrestacaoServico() {
		IdentificacaoRps = new IdentificacaoRps();
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public Servico getServico() {
		return Servico;
	}

	public void setServico(Servico servico) {
		Servico = servico;
	}

	public Prestador getPrestador() {
		return Prestador;
	}

	public void setPrestador(Prestador prestador) {
		Prestador = prestador;
	}

	public Tomador getTomador() {
		return Tomador;
	}

	public void setTomador(Tomador tomador) {
		Tomador = tomador;
	}

	public SimNao getOptanteSimplesNacional() {
		return OptanteSimplesNacional;
	}

	public void setOptanteSimplesNacional(SimNao optanteSimplesNacional) {
		OptanteSimplesNacional = optanteSimplesNacional;
	}

	public IdentificacaoRps getIdentificacaoRps() {
		return IdentificacaoRps;
	}

	public void setIdentificacaoRps(IdentificacaoRps identificacaoRps) {
		IdentificacaoRps = identificacaoRps;
	}

	public Calendar getDataEmissao() {
		return DataEmissao;
	}

	public void setDataEmissao(Calendar dataEmissao) {
		DataEmissao = dataEmissao;
	}

	public SimNao getIncentivadorCultural() {
		return IncentivadorCultural;
	}

	public void setIncentivadorCultural(SimNao incentivadorCultural) {
		IncentivadorCultural = incentivadorCultural;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNaturezaOperacao() {
		return NaturezaOperacao;
	}

	public void setNaturezaOperacao(String naturezaOperacao) {
		NaturezaOperacao = naturezaOperacao;
	}
}
