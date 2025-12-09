package webservice.nfse.joaopessoa;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

public class InfDeclaracaoPrestacaoServico {

//	@XStreamAsAttribute
//	private String Id;
	@XStreamAlias("nfse:Rps")
	private RpsDetalhe Rps;
	
	@XStreamAlias("nfse:Competencia")
	@XStreamConverter(DateConverter.class)
	private Calendar Competencia;
	
	@XStreamAlias("nfse:Servico")
	private Servico Servico;
	
	@XStreamAlias("nfse:Prestador")
	private Prestador Prestador;
	
	@XStreamAlias("nfse:Tomador")
	private Tomador Tomador;
	
	@XStreamAlias("nfse:Intermediario")
	private Intermediario Intermediario;
	
	@XStreamAlias("nfse:RegimeEspecialTributacao")
	private RegimeEspecialTributacao RegimeEspecialTributacao;

	@XStreamAlias("nfse:OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("nfse:IncentivoFiscal")
	private SimNao IncentivoFiscal;
		
	public InfDeclaracaoPrestacaoServico() {
//		Rps = new RpsDetalhe();
//		Competencia = Calendar.getInstance();
	}
	
//	public String getId() {
//		return Id;
//	}
//
//	public void setId(String id) {
//		Id = id;
//	}

	public RpsDetalhe getRps() {
		return Rps;
	}

	public void setRps(RpsDetalhe rps) {
		Rps = rps;
	}

	public Calendar getCompetencia() {
		return Competencia;
	}

	public void setCompetencia(Calendar competencia) {
		Competencia = competencia;
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

	public SimNao getIncentivoFiscal() {
		return IncentivoFiscal;
	}

	public void setIncentivoFiscal(SimNao incentivoFiscal) {
		IncentivoFiscal = incentivoFiscal;
	}

	public RegimeEspecialTributacao getRegimeEspecialTributacao() {
		return RegimeEspecialTributacao;
	}

	public void setRegimeEspecialTributacao(RegimeEspecialTributacao regimeEspecialTributacao) {
		RegimeEspecialTributacao = regimeEspecialTributacao;
	}

	@Override
	public String toString() {
		return "InfDeclaracaoPrestacaoServico [  Servico=" + Servico
				+ ", Prestador=" + Prestador + ", Tomador=" + Tomador
				+ ", Intermediario=" + Intermediario
				+ ", RegimeEspecialTributacao=" +RegimeEspecialTributacao
				+ ", OptanteSimplesNacional=" + OptanteSimplesNacional
				+ ", IncentivoFiscal=" + IncentivoFiscal + "]";
	}

}
