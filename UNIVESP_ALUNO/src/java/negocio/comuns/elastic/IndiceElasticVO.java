package negocio.comuns.elastic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndiceElasticVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nome;
	private String url;
	private List<IndiceVersaoElasticVO> versoes;
	
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getUrl() {
		if (url == null) {
			url = "";
		}
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public List<IndiceVersaoElasticVO> getVersoes() {
		if (versoes == null) {
			versoes = new ArrayList<IndiceVersaoElasticVO>();
		}
		return versoes;
	}

	public void setVersoes(List<IndiceVersaoElasticVO> versoes) {
		this.versoes = versoes;
	}
	
}
