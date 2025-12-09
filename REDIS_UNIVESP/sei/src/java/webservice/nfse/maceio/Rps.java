package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ns3:Rps")
public class Rps {

	@XStreamAlias("ns3:InfRps")
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
