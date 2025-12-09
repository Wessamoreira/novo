package webservice.nfse.belohorizonte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class LoteRps {
	
	@XStreamAsAttribute
	private String Id;
	
	@XStreamAsAttribute
	private Versao versao;
	
	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";
	
	private Long NumeroLote;
	
	private String Cnpj;
	
	private String InscricaoMunicipal;
	
	private int QuantidadeRps;
	
	private final List<Rps> ListaRps;
	
	public LoteRps() {
		Id = "";
		ListaRps = new ArrayList<Rps>();
	}

	@Override
	public String toString() {
		return "LoteRps [Id=" + Id + ", versao=" + versao + ", NumeroLote="
				+ NumeroLote + ", Cnpj=" + Cnpj + ", InscricaoMunicipal="
				+ InscricaoMunicipal + ", QuantidadeRps=" + QuantidadeRps
				+ ", ListaRps=" + ListaRps + "]";
	}

	public void setId(String id) {
		Id = id;
	}

	public String getId() {
		return Id;
	}

	public Versao getVersao() {
		return versao;
	}

	public void setVersao(Versao versao) {
		this.versao = versao;
	}

	public Long getNumeroLote() {
		return NumeroLote;
	}

	public void setNumeroLote(Long numeroLote) {
		NumeroLote = numeroLote;
	}

	public String getCnpj() {
		return Cnpj;
	}

	public void setCnpj(String Cnpj) {
		this.Cnpj = Cnpj;
	}

	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String l) {
		InscricaoMunicipal = l;
	}

	public Integer getQuantidadeRps() {
		return QuantidadeRps;
	}
	
	public void setQuantidadeRps(Integer QuantidadeRps) {
		this.QuantidadeRps = QuantidadeRps;
	}

	public List<Rps> getRps() {
		return Collections.unmodifiableList(ListaRps);
	}

	public void addRps(List<Rps> rps) {
		ListaRps.addAll(rps);
		QuantidadeRps = ListaRps.size();
	}
	
	public void addRps(Rps... rps) {
		addRps(Arrays.asList(rps));
	}

	public void v2_02() {
		versao = Versao.V2_02;
	}

	public void v2_01() {
		versao = Versao.V2_01;
	}
	
	public void v2_00() {
		versao = Versao.V2_00;
	}
	
	public void v1_00() {
		versao = Versao.V1_00;
	}
}