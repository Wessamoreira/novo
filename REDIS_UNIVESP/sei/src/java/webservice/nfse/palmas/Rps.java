package webservice.nfse.palmas;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Rps")
public class Rps {

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
