package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class IdentificacaoTomador {
	
	@XStreamAlias("ns3:CpfCnpj")
	private CpfCnpj CpfCnpj;
	
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
		return "IdentificacaoTomador [CpfCnpj=" + CpfCnpj + "]";
	}
	
}
