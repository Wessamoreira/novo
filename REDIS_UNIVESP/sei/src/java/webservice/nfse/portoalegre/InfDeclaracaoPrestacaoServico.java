package webservice.nfse.portoalegre;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("InfRps")
public class InfDeclaracaoPrestacaoServico {
	
//	@XStreamAsAttribute
//	private String xmlns = "http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd";

	@XStreamAsAttribute
	private String Id;
	
	private IdentificacaoRps IdentificacaoRps;

	@XStreamConverter(DateConverter.class)
	private Calendar DataEmissao;
	
	@XStreamAlias("NaturezaOperacao")
	private String NaturezaOperacao;
	
	private String RegimeEspecialTributacao;
		
	@XStreamAlias("OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("IncentivadorCultural")
	private SimNao IncentivadorCultural;
	
	@XStreamAlias("Status")
	private Status status;
	
	private Servico Servico;
	
	private Prestador Prestador;
	
	private Tomador Tomador;
	
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
		return "InfDeclaracaoPrestacaoServico [ Id=" + Id + ", IdentificacaoRps=" + IdentificacaoRps + ", status=" + status + ", DataEmissao=" + DataEmissao + ", NaturezaOperacao=" + NaturezaOperacao +" RegimeEspecialTributacao="+RegimeEspecialTributacao+ ", OptanteSimplesNacional=" + OptanteSimplesNacional + ", IncentivadorCultural=" + IncentivadorCultural + ", Servico=" + Servico + ", Prestador=" + Prestador + ", Tomador=" + Tomador + ", Intermediario=" + Intermediario + "]";
	}

	public String getRegimeEspecialTributacao() {
		return RegimeEspecialTributacao;
	}

	public void setRegimeEspecialTributacao(String regimeEspecialTributacao) {
		RegimeEspecialTributacao = regimeEspecialTributacao;
	}
}
