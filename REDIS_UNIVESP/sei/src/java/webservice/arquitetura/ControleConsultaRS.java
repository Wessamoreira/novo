package webservice.arquitetura;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "controleConsulta")
@XmlSeeAlso(value = {AdvertenciaVO.class, DocumentoAssinadoVO.class})
public class ControleConsultaRS<T extends SuperVO> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9132239252442282341L;
	private Integer totalRegistros;
	private Integer limitePorPagina;
	private Integer paginaAtual;
	private Integer totalPagina;	
	private List<T> listaConsulta;
		
	
	public ControleConsultaRS() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public ControleConsultaRS(DataModelo dataModelo) {
		this.paginaAtual =  dataModelo.getPaginaAtual();
		this.totalRegistros =  dataModelo.getTotalRegistrosEncontrados();
		this.limitePorPagina =  dataModelo.getLimitePorPagina();
		this.totalPagina =  dataModelo.getTotalPaginas();
		this.listaConsulta =  (List<T>)dataModelo.getListaConsulta();
	}
	
	
	@XmlElement(name = "paginaAtual")
	public Integer getPaginaAtual() {
		return paginaAtual;
	}
	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}
	@XmlElement(name = "totalPagina")
	public Integer getTotalPagina() {
		return totalPagina;
	}
	public void setTotalPagina(Integer totalPagina) {
		this.totalPagina = totalPagina;
	}
	@XmlElement(name = "totalRegistros")
	public Integer getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	
	@XmlElement(name = "limitePorPagina")
	public Integer getLimitePorPagina() {
		return limitePorPagina;
	}
	public void setLimitePorPagina(Integer limitePorPagina) {
		this.limitePorPagina = limitePorPagina;
	}
	
	@XmlElementWrapper(name = "listaConsulta")
	@XmlElement(name = "item")	
	public List<T> getListaConsulta() {
		return listaConsulta;
	}
	public void setListaConsulta(List<T> listaConsulta) {
		this.listaConsulta = listaConsulta;
	}
		
	
}
