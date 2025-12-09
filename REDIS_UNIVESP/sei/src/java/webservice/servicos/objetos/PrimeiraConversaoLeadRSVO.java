package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "first_conversion")
public class PrimeiraConversaoLeadRSVO {
	
	private ConteudoConversaoDoLeadRSVO conteudo;
	private String dataCriacao;
	private Integer somaAcumulativa;
	private String fonte;
	private OrigemConversaoLeadPrimeiraConversaoRSVO origemConversao;

	@XmlElement(name = "content")
	public ConteudoConversaoDoLeadRSVO getConteudo() {
		if(conteudo == null)
			conteudo = new ConteudoConversaoDoLeadRSVO();
		
		return conteudo;
	}
	public void setConteudo(ConteudoConversaoDoLeadRSVO conteudo) {
		this.conteudo = conteudo;
	}
	
	@XmlElement(name = "created_at")
	public String getDataCriacao() {
		if(dataCriacao == null)
			dataCriacao = "";
		
		return dataCriacao;
	}
	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	@XmlElement(name = "cumulative_sum")
	public Integer getSomaAcumulativa() {
		if(somaAcumulativa == null)
			somaAcumulativa = 0;
		
		return somaAcumulativa;
	}
	public void setSomaAcumulativa(Integer somaAcumulativa) {
		this.somaAcumulativa = somaAcumulativa;
	}
	
	@XmlElement(name = "source")
	public String getOrigem() {
		if(fonte == null)
			fonte = "";
		
		return fonte;
	}
	public void setOrigem(String origem) {
		this.fonte = origem;
	}
	
	
	public OrigemConversaoLeadPrimeiraConversaoRSVO getOrigemConversao() {
		if(origemConversao == null)
			origemConversao = new OrigemConversaoLeadPrimeiraConversaoRSVO();
		
		return origemConversao;
	}
	public void setOrigemConversao(OrigemConversaoLeadPrimeiraConversaoRSVO origemConversao) {
		this.origemConversao = origemConversao;
	}
	
}