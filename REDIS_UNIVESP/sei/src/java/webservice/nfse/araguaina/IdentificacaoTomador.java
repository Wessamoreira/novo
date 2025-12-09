package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class IdentificacaoTomador {
	
	@XStreamAlias("CpfCnpj")
	private CpfCnpj CpfCnpj;
	
	@XStreamAlias("InscricaoMunicipal")
	private String InscricaoMunicipal;
	
	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		InscricaoMunicipal = inscricaoMunicipal;
	}

	public IdentificacaoTomador() {
		CpfCnpj = new CpfCnpj();
	}

	public CpfCnpj getCpfCnpj() {
		return CpfCnpj;
	}

	public void setCpfCnpj(CpfCnpj cpfCnpj) {
		CpfCnpj = cpfCnpj;
	}
	
	@Override
	public String toString() {
		return "IdentificacaoTomador [CpfCnpj=" + CpfCnpj + ", InscricaoMunicipal="+ InscricaoMunicipal +"]";
	}
	
}
