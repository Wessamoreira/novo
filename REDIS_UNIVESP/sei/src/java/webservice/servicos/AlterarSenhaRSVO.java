package webservice.servicos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "alterarSenha")
public class AlterarSenhaRSVO {

	private String senhaAtual;
	private String senhaNova;
	private Integer perfilAcesso;
	
	@XmlElement(name = "senhaAtual", required= true)
	public String getSenhaAtual() {
		return senhaAtual;
	}
	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}
	
	@XmlElement(name = "senhaNova", required= true)
	public String getSenhaNova() {
		return senhaNova;
	}
	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}
	
	@XmlElement(name = "perfilAcesso", required= true)
	public Integer getPerfilAcesso() {
		return perfilAcesso;
	}
	public void setPerfilAcesso(Integer perfilAcesso) {
		this.perfilAcesso = perfilAcesso;
	}
	
	
	
}
