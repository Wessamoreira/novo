package webservice.nfse.belohorizonte;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Rps")
public class Rps {

	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";
	
	@XStreamAlias("InfRps")
	private InfDeclaracaoPrestacaoServico InfDeclaracaoPrestacaoServico;

	public Rps() {
		InfDeclaracaoPrestacaoServico = new InfDeclaracaoPrestacaoServico();
	}

	@Override
	public String toString() {
		return "Rps [InfDeclaracaoPrestacaoServico=" + InfDeclaracaoPrestacaoServico + "]";
	}

	public InfDeclaracaoPrestacaoServico getInfDeclaracaoPrestacaoServico() {
		return InfDeclaracaoPrestacaoServico;
	}

	public void setInfDeclaracaoPrestacaoServico(InfDeclaracaoPrestacaoServico infDeclaracaoPrestacaoServico) {
		InfDeclaracaoPrestacaoServico = infDeclaracaoPrestacaoServico;
	}
}
