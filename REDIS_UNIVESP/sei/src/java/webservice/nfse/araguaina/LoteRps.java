package webservice.nfse.araguaina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("nfse:LoteRps")
public class LoteRps {
	
//	@XStreamAsAttribute
//	private String Id;
	
	@XStreamAsAttribute
	private Versao versao;
	
	@XStreamAlias("nfse:NumeroLote")
	private Long NumeroLote;
	
	@XStreamAlias("nfse:CpfCnpj")
	private CpfCnpj CpfCnpj;
	
	@XStreamAlias("nfse:InscricaoMunicipal")
	private String InscricaoMunicipal;
	
	@XStreamAlias("nfse:QuantidadeRps")
	private int QuantidadeRps;
	
	@XStreamAlias("nfse:ListaRps")
	private final List<Rps> ListaRps;
	
	public LoteRps() {
//		Id = "";
		ListaRps = new ArrayList<Rps>();
		CpfCnpj = new CpfCnpj();
	}

	@Override
	public String toString() {
		return "LoteRps [versao=" + versao + ", NumeroLote="
				+ NumeroLote + ", CpfCnpj=" + CpfCnpj + ", InscricaoMunicipal="
				+ InscricaoMunicipal + ", QuantidadeRps=" + QuantidadeRps
				+ ", ListaRps=" + ListaRps + "]";
	}

//	public void setId(String id) {
//		Id = id;
//	}
//
//	public String getId() {
//		return Id;
//	}

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

	public CpfCnpj getCpfCnpj() {
		return CpfCnpj;
	}

	public void setCpfCnpj(CpfCnpj cpfCnpj) {
		CpfCnpj = cpfCnpj;
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

	public void setCnpj(String cnpj) {
		CpfCnpj.setCnpj(cnpj);		
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
	
	public void v1_02() {
		versao = Versao.V1_02;
	}

}