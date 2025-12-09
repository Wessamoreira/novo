package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tc:IdentificacaoTomador")
public class IdentificacaoTomador {
	
	@XStreamAlias("tc:CpfCnpj")
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
