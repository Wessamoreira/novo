package webservice.nfse.anapolis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class LoteRps {
	
	private String Id;
	
//	@XStreamAsAttribute
//	private Versao versao;
	
	@XStreamAlias("tc:NumeroLote")
	private Long NumeroLote;
	
	@XStreamAlias("tc:CpfCnpj")
	private CpfCnpj CpfCnpj;
	
	@XStreamAlias("tc:InscricaoMunicipal")
	private String InscricaoMunicipal;
	
	@XStreamAlias("tc:QuantidadeRps")
	private int QuantidadeRps;
	
	@XStreamAlias("tc:ListaRps")
	private final List<Rps> ListaRps;
	
	public LoteRps() {
		ListaRps = new ArrayList<Rps>();
		CpfCnpj = new CpfCnpj();
	}

	public void setId(String id) {
		Id = id;
	}

	public String getId() {
		return Id;
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

	public void setCnpj(String cnpj) {
		CpfCnpj.setCnpj(cnpj);		
	}

	@Override
	public String toString() {
		return "LoteRps [Id=" + Id + ", NumeroLote=" + NumeroLote + ", CpfCnpj=" + CpfCnpj + ", InscricaoMunicipal=" + InscricaoMunicipal + ", QuantidadeRps=" + QuantidadeRps + ", ListaRps=" + ListaRps + "]";
	}
}