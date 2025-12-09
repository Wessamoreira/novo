package webservice.servicos.objetos;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.biblioteca.CatalogoVO;

@XmlRootElement(name = "biblioteca")
public class bibliotecaObject {
	
	private List<CatalogoVO> catalogoVO; 
	private Integer quantidadeExemplarCatalogo;
	private Integer quantidadeExemplarCatalogoEmprestado;
	

	public List<CatalogoVO> getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new ArrayList<CatalogoVO>();
		}
		return catalogoVO;
	}
	public void setCatalogoVO(List<CatalogoVO> catalogoVO) {
		this.catalogoVO = catalogoVO;
	}
	@XmlElement(name = "quantidadeExemplarCatalogo")
	public int getQuantidadeExemplarCatalogo() {
		if (quantidadeExemplarCatalogo == null) {
			quantidadeExemplarCatalogo = 0;
		}
		return quantidadeExemplarCatalogo;
	}
	public void setQuantidadeExemplarCatalogo(int quantidadeExemplarCatalogo) {
		this.quantidadeExemplarCatalogo = quantidadeExemplarCatalogo;
	}
	@XmlElement(name = "quantidadeExemplarCatalogoEmprestado")
	public int getQuantidadeExemplarCatalogoEmprestado() {
		if (quantidadeExemplarCatalogoEmprestado == null) {
			quantidadeExemplarCatalogoEmprestado = 0;
		}
		return quantidadeExemplarCatalogoEmprestado;
	}
	public void setQuantidadeExemplarCatalogoEmprestado(int quantidadeExemplarCatalogoEmprestado) {
		this.quantidadeExemplarCatalogoEmprestado = quantidadeExemplarCatalogoEmprestado;
	}
	
	
	
	
	
}
