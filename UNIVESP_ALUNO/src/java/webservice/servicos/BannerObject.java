package webservice.servicos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "banner")
public class BannerObject {

	private Integer codigoBanner;
	private String descricao;
	private String caminhoWebImagem;
	private CursoObject cursoObject;

	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@XmlElement(name = "caminhoWebImagem")
	public String getCaminhoWebImagem() {
		if (caminhoWebImagem == null) {
			caminhoWebImagem = "";
		}
		return caminhoWebImagem;
	}

	public void setCaminhoWebImagem(String caminhoWebImagem) {
		this.caminhoWebImagem = caminhoWebImagem;
	}

	@XmlElement(name = "curso")
	public CursoObject getCursoObject() {
		if(cursoObject == null) {
			cursoObject = new CursoObject();
		}
		return cursoObject;
	}

	public void setCursoObject(CursoObject cursoObject) {
		this.cursoObject = cursoObject;
	}

	@XmlElement(name = "codigoBanner")
	public Integer getCodigoBanner() {
		if(codigoBanner == null) {
			codigoBanner = 0;
		}
		return codigoBanner;
	}

	public void setCodigoBanner(Integer codigoBanner) {
		this.codigoBanner = codigoBanner;
	}
}
