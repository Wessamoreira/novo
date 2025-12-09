package webservice.nfse.anapolis;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("tc:InfRps")
public class InfDeclaracaoPrestacaoServico {

//	@XStreamAsAttribute
//	private String Id;
	
	@XStreamAlias("tc:IdentificacaoRps")
	private IdentificacaoRps IdentificacaoRps;
	
	@XStreamAlias("tc:DataEmissao")
	@XStreamConverter(DateConverter.class)
	private Calendar DataEmissao;
	
	@XStreamAlias("tc:NaturezaOperacao")
	private String NaturezaOperacao;
	
	@XStreamAlias("tc:OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("tc:IncentivadorCultural")
	private SimNao IncentivadorCultural;
	
	@XStreamAlias("tc:Competencia")
	private String Competencia;
	
	@XStreamAlias("tc:Status")
	private Status status;
	
	@XStreamAlias("tc:RegimeEspecialTributacao")
	private RegimeEspecialTributacao RegimeEspecialTributacao;
		
	@XStreamAlias("tc:Servico")
	private Servico Servico;
	
	@XStreamAlias("tc:Prestador")
	private Prestador Prestador;
	
	@XStreamAlias("tc:Tomador")
	private Tomador Tomador;
	
	@XStreamAlias("tc:Intermediario")
	private Intermediario Intermediario;
	
	@XStreamAlias("tc:IncentivoFiscal")
	private SimNao IncentivoFiscal;
		
	public InfDeclaracaoPrestacaoServico() {
		this.IdentificacaoRps = new IdentificacaoRps();
	}
	
//	public String getId() {
//		return Id;
//	}
//
//	public void setId(String id) {
//		Id = id;
//	}

//	public Calendar getCompetencia() {
//		return Competencia;
//	}
//
//	public void setCompetencia(Calendar competencia) {
//		Competencia = competencia;
//	}

	public Servico getServico() {
		return Servico;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public RegimeEspecialTributacao getRegimeEspecialTributacao() {
		return RegimeEspecialTributacao;
	}

	public void setRegimeEspecialTributacao(RegimeEspecialTributacao regimeEspecialTributacao) {
		RegimeEspecialTributacao = regimeEspecialTributacao;
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

	public SimNao getIncentivoFiscal() {
		return IncentivoFiscal;
	}

	public void setIncentivoFiscal(SimNao incentivoFiscal) {
		IncentivoFiscal = incentivoFiscal;
	}

	public String getCompetencia() {
		if (Competencia == null) {
			Competencia = "";
		}
		return Competencia;
	}

	public void setCompetencia(String competencia) {
		Competencia = competencia;
	}

	@Override
	public String toString() {
		return "InfDeclaracaoPrestacaoServico [IdentificacaoRps=" + IdentificacaoRps + ", DataEmissao=" + DataEmissao + ", NaturezaOperacao=" + NaturezaOperacao + ", OptanteSimplesNacional=" + OptanteSimplesNacional + ", IncentivadorCultural=" + IncentivadorCultural + ", Competencia=" + Competencia + ", status=" + status + ", RegimeEspecialTributacao=" + RegimeEspecialTributacao + ", Servico=" + Servico + ", Prestador=" + Prestador + ", Tomador=" + Tomador + ", Intermediario=" + Intermediario + ", IncentivoFiscal=" + IncentivoFiscal + "]";
	}
}
