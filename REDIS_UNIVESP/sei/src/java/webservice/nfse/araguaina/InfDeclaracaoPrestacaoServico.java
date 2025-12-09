package webservice.nfse.araguaina;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

public class InfDeclaracaoPrestacaoServico {


	@XStreamAlias("Id")
	@XStreamAsAttribute
	private String id;
	
	@XStreamAlias("Rps")
	private RpsDetalhe Rps;
	
	@XStreamAlias("Competencia")
	@XStreamConverter(DateConverter.class)
	private Calendar Competencia;
	
	@XStreamAlias("Servico")
	private Servico Servico;
	
	@XStreamAlias("Prestador")
	private Prestador Prestador;
	
	@XStreamAlias("Tomador")
	private Tomador Tomador;
	
	@XStreamAlias("Intermediario")
	private Intermediario Intermediario;
	
	@XStreamAlias("RegimeEspecialTributacao")
	private RegimeEspecialTributacao RegimeEspecialTributacao;

	@XStreamAlias("OptanteSimplesNacional")
	private SimNao OptanteSimplesNacional;
	
	@XStreamAlias("IncentivoFiscal")
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
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "InfDeclaracaoPrestacaoServico [  Servico=" + Servico
				+ ", Prestador=" + Prestador + ", Tomador=" + Tomador
				+ ", Id=" + id
				+ ", Intermediario=" + Intermediario
				+ ", RegimeEspecialTributacao=" +RegimeEspecialTributacao
				+ ", OptanteSimplesNacional=" + OptanteSimplesNacional
				+ ", IncentivoFiscal=" + IncentivoFiscal + "]";
	}

}
