package webservice.nfse.vitoria;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("InfDeclaracaoPrestacaoServico")
public class InfDeclaracaoPrestacaoServico {
	
//	@XStreamAsAttribute
//	private String xmlns = "http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd";

	@XStreamAsAttribute
	private String Id;
	
	@XStreamAlias("Rps")
	private RpsDetalhe Rps;
	
//	private IdentificacaoRps IdentificacaoRps;

	@XStreamConverter(DateConverter.class)
	private Calendar Competencia;
	
	@XStreamAlias("NaturezaOperacao")
	private String NaturezaOperacao;
	
	private String RegimeEspecialTributacao;
	
	@XStreamAlias("Status")
	private Status status;
	
	private Servico Servico;
	
	private Prestador Prestador;
	
	private Tomador Tomador;
	
	private Intermediario Intermediario;
	
	@XStreamAlias("OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("IncentivoFiscal")
	private SimNao IncentivoFiscal;
	
	@XStreamAlias("IncentivadorCultural")
	private SimNao IncentivadorCultural;
			
	public InfDeclaracaoPrestacaoServico() {
		this.Rps = new RpsDetalhe();
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

	public Calendar getCompetencia() {
		return Competencia;
	}

	public void setCompetencia(Calendar Competencia) {
		this.Competencia = Competencia;
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

	public RpsDetalhe getRps() {
		return Rps;
	}

	public void setRps(RpsDetalhe rps) {
		Rps = rps;
	}

	public String getRegimeEspecialTributacao() {
		return RegimeEspecialTributacao;
	}

	public void setRegimeEspecialTributacao(String regimeEspecialTributacao) {
		RegimeEspecialTributacao = regimeEspecialTributacao;
	}

	public SimNao getIncentivoFiscal() {
		return IncentivoFiscal;
	}

	public void setIncentivoFiscal(SimNao incentivoFiscal) {
		IncentivoFiscal = incentivoFiscal;
	}

	@Override
	public String toString() {
		return "InfDeclaracaoPrestacaoServico [Rps=" + Rps + ", Competencia=" + Competencia + ", NaturezaOperacao=" + NaturezaOperacao + ", RegimeEspecialTributacao=" + RegimeEspecialTributacao + ", Servico=" + Servico + ", Prestador=" + Prestador + ", Tomador=" + Tomador + ", Intermediario=" + Intermediario + ", OptanteSimplesNacional=" + OptanteSimplesNacional + ", IncentivoFiscal=" + IncentivoFiscal + ", IncentivadorCultural=" + IncentivadorCultural + "]";
	}
}
