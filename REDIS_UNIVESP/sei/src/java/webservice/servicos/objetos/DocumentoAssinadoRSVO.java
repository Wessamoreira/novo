package webservice.servicos.objetos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import negocio.comuns.academico.DocumentoAssinadoVO;

@XmlRootElement(name = "DocumentoAssinadoRSVO")
public class DocumentoAssinadoRSVO implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7066886053770394797L;
	private String senhaAssinatura;
	private String motivoRejeicao;
	private List<DocumentoAssinadoVO> listaDocumentoAssinado;
	private List<DocumentoAssinadoVO> listaDocumentoPendente;
	private List<DocumentoAssinadoVO> listaDocumentoAssinar;
	private Date filtroDataRegistroInicio;
	private Date filtroDataRegistroFim;
	private TipoOrigemDocumentoAssinadoRSVO fitroTipoOrigemDocumentoAssinadoEnum;
	private int totalRegistrosEncontrados = 0;
	private int paginaAtual = 1;
	private int totalPaginas = 0;
	private int limitePorPagina = 10;
	private String tipoDocumentoAssinado;
	private Integer ordemAssinaturaFiltrar;

	@XmlElement(name = "senhaAssinatura")
	public String getSenhaAssinatura() {
		return senhaAssinatura;
	}

	public void setSenhaAssinatura(String senhaAssinatura) {
		this.senhaAssinatura = senhaAssinatura;
	}
	@XmlElement(name = "motivoRejeicao")
	public String getMotivoRejeicao() {
		return motivoRejeicao;
	}

	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}	

	@XmlElement(name = "listaDocumentoAssinado")
	public List<DocumentoAssinadoVO> getListaDocumentoAssinado() {
		return listaDocumentoAssinado;
	}

	public void setListaDocumentoAssinado(List<DocumentoAssinadoVO> listaDocumentoAssinado) {
		this.listaDocumentoAssinado = listaDocumentoAssinado;
	}

	@XmlElement(name = "listaDocumentoPendente")
	public List<DocumentoAssinadoVO> getListaDocumentoPendente() {
		return listaDocumentoPendente;
	}

	public void setListaDocumentoPendente(List<DocumentoAssinadoVO> listaDocumentoPendente) {
		this.listaDocumentoPendente = listaDocumentoPendente;
	}
	
	@XmlElement(name = "listaDocumentoAssinar")
	public List<DocumentoAssinadoVO> getListaDocumentoAssinar() {
		return listaDocumentoAssinar;
	}

	public void setListaDocumentoAssinar(List<DocumentoAssinadoVO> listaDocumentoAssinar) {
		this.listaDocumentoAssinar = listaDocumentoAssinar;
	}

	@XmlElement(name = "totalRegistrosEncontrados")
	public int getTotalRegistrosEncontrados() {
		return totalRegistrosEncontrados;
	}

	public void setTotalRegistrosEncontrados(int totalRegistrosEncontrados) {
		this.totalRegistrosEncontrados = totalRegistrosEncontrados;
	}

	@XmlElement(name = "paginaAtual")
	public int getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	@XmlElement(name = "totalPaginas")
	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	@XmlElement(name = "limitePorPagina")
	public int getLimitePorPagina() {
		return limitePorPagina;
	}

	public void setLimitePorPagina(int limitePorPagina) {
		this.limitePorPagina = limitePorPagina;
	}
	
	@XmlElement(name = "filtroDataRegistroInicio")
	public Date getFiltroDataRegistroInicio() {
		return filtroDataRegistroInicio;
	}

	public void setFiltroDataRegistroInicio(Date filtroDataRegistroInicio) {
		this.filtroDataRegistroInicio = filtroDataRegistroInicio;
	}

	@XmlElement(name = "filtroDataRegistroFim")
	public Date getFiltroDataRegistroFim() {
		return filtroDataRegistroFim;
	}

	public void setFiltroDataRegistroFim(Date filtroDataRegistroFim) {
		this.filtroDataRegistroFim = filtroDataRegistroFim;
	}

	@XmlElement(name = "fitroTipoOrigemDocumentoAssinadoEnum")
	public TipoOrigemDocumentoAssinadoRSVO getFitroTipoOrigemDocumentoAssinadoEnum() {
		return fitroTipoOrigemDocumentoAssinadoEnum;
	}

	public void setFitroTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoRSVO fitroTipoOrigemDocumentoAssinadoEnum) {
		this.fitroTipoOrigemDocumentoAssinadoEnum = fitroTipoOrigemDocumentoAssinadoEnum;
	}

	@XmlElement(name = "tipoDocumentoAssinado")
	public String getTipoDocumentoAssinado() {
		return tipoDocumentoAssinado;
	}

	public void setTipoDocumentoAssinado(String tipoDocumentoAssinado) {
		this.tipoDocumentoAssinado = tipoDocumentoAssinado;
	}
	
	@XmlElement(name = "ordemAssinaturaFiltrar")
	public Integer getOrdemAssinaturaFiltrar() {
		return ordemAssinaturaFiltrar;
	}
	
	public void setOrdemAssinaturaFiltrar(Integer ordemAssinaturaFiltrar) {
		this.ordemAssinaturaFiltrar = ordemAssinaturaFiltrar;
	}
}
