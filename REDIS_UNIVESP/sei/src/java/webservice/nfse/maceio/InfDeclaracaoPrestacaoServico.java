package webservice.nfse.maceio;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("ns3:InfRps")
public class InfDeclaracaoPrestacaoServico {
	
//	@XStreamAsAttribute
//	private String xmlns = "http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd";

	@XStreamAsAttribute
	private String Id;
	
	@XStreamAlias("ns3:IdentificacaoRps")
	private IdentificacaoRps IdentificacaoRps;

	@XStreamConverter(DateConverter.class)
	@XStreamAlias("ns3:DataEmissao")
	private Calendar DataEmissao;
	
	@XStreamAlias("ns3:NaturezaOperacao")
	private String NaturezaOperacao;
		
	@XStreamAlias("ns3:OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("ns3:IncentivadorCultural")
	private SimNao IncentivadorCultural;
	
	@XStreamAlias("ns3:Status")
	private Status status;
	
	@XStreamAlias("ns3:Servico")
	private Servico Servico;
	
	@XStreamAlias("ns3:Prestador")
	private Prestador Prestador;
	
	@XStreamAlias("ns3:Tomador")
	private Tomador Tomador;
	
	@XStreamAlias("ns3:Intermediario")
	private Intermediario Intermediario;
			
	public InfDeclaracaoPrestacaoServico() {
		this.IdentificacaoRps = new IdentificacaoRps();
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

	public Intermediario getIntermediario() {
		return Intermediario;
	}

	public void setIntermediario(Intermediario intermediario) {
		Intermediario = intermediario;
	}

	public SimNao getOptanteSimplesNacional() {
		return OptanteSimplesNacional;
	}

	public void setOptanteSimplesNacional(SimNao optanteSimplesNacional) {
		OptanteSimplesNacional = optanteSimplesNacional;
	}

	public Calendar getDataEmissao() {
		return DataEmissao;
	}

	public void setDataEmissao(Calendar dataEmissao) {
		DataEmissao = dataEmissao;
	}

	public IdentificacaoRps getIdentificacaoRps() {
		return IdentificacaoRps;
	}

	public void setIdentificacaoRps(IdentificacaoRps identificacaoRps) {
		IdentificacaoRps = identificacaoRps;
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

	public SimNao getIncentivadorCultural() {
		return IncentivadorCultural;
	}

	public void setIncentivadorCultural(SimNao incentivadorCultural) {
		IncentivadorCultural = incentivadorCultural;
	}

	@Override
	public String toString() {
		return "InfDeclaracaoPrestacaoServico [   IdentificacaoRps=" + IdentificacaoRps + ", status=" + status + ", DataEmissao=" + DataEmissao + ", NaturezaOperacao=" + NaturezaOperacao + ", OptanteSimplesNacional=" + OptanteSimplesNacional + ", IncentivadorCultural=" + IncentivadorCultural + ", Servico=" + Servico + ", Prestador=" + Prestador + ", Tomador=" + Tomador + ", Intermediario=" + Intermediario + "]";
	}
}
