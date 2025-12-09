package webservice.servicos.objetos;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;

@XmlRootElement(name = "documentacaoMatriculaRSVO")
public class DocumentacaoMatriculaRSVO {
	
	private String matricula;
	private List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaVO;
	private Integer codigoPerfilAcesso;
	
	// campos utilizados na matricula externa do processo seletivo
	private DocumetacaoMatriculaVO documentacaoMatriculaVO;
	private String  codigoAutenticacaoNavegador ;
	private String  navegadorAcesso;
	private Integer codigoInscricao;
	private Boolean permiteAcessarNavegador;
	private Integer codigoDocumentacaoMatricula;

	public List<DocumetacaoMatriculaVO> getListaDocumentacaoMatriculaVO() {
		if (listaDocumentacaoMatriculaVO == null) {
			listaDocumentacaoMatriculaVO = new ArrayList<DocumetacaoMatriculaVO>();
		}
		return listaDocumentacaoMatriculaVO;
	}
	
	public void setListaDocumentacaoMatriculaVO(List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaVO) {
		this.listaDocumentacaoMatriculaVO = listaDocumentacaoMatriculaVO;
	}

	public Integer getCodigoPerfilAcesso() {
		if (codigoPerfilAcesso == null) {
			codigoPerfilAcesso = 0;
		}
		return codigoPerfilAcesso;
	}
	
	public void setCodigoPerfilAcesso(Integer codigoPerfilAcesso) {
		this.codigoPerfilAcesso = codigoPerfilAcesso;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public DocumetacaoMatriculaVO getDocumentacaoMatriculaVO() {
		if(documentacaoMatriculaVO == null ) {
			documentacaoMatriculaVO = new DocumetacaoMatriculaVO();
		}
		return documentacaoMatriculaVO;
	}

	public void setDocumentacaoMatriculaVO(DocumetacaoMatriculaVO documentacaoMatriculaVO) {
		this.documentacaoMatriculaVO = documentacaoMatriculaVO;
	}
	
	
	
	@XmlElement(name = "codigoInscricao")
	public Integer getCodigoInscricao() {
		return codigoInscricao;
	}

	public void setCodigoInscricao(Integer codigoInscricao) {
		this.codigoInscricao = codigoInscricao;
	}
	
	
	
	
	   @XmlElement(name = "codigoAutenticacaoNavegador")
	 	public String getCodigoAutenticacaoNavegador() {
	 		if(codigoAutenticacaoNavegador == null) {
	 			codigoAutenticacaoNavegador ="";
	 		}
	 		return codigoAutenticacaoNavegador;
	 	}

	 	public void setCodigoAutenticacaoNavegador(String codigoAutenticacaoNavegador) {
	 		this.codigoAutenticacaoNavegador = codigoAutenticacaoNavegador;
	 	}
	
	@XmlElement(name = "navegadorAcesso")
	public String getNavegadorAcesso() {
		if(navegadorAcesso == null) {
			navegadorAcesso = "";
		}
		return navegadorAcesso;
	}

	public void setNavegadorAcesso(String navegadorAcesso) {
		this.navegadorAcesso = navegadorAcesso;
	}
	
	
	@XmlElement(name = "permiteAcessarNavegador")
	public Boolean getPermiteAcessarNavegador() {
		if(permiteAcessarNavegador == null) {
			permiteAcessarNavegador = Boolean.FALSE;
		}
		return permiteAcessarNavegador;
	}

	public void setPermiteAcessarNavegador(Boolean permiteAcessarNavegador) {
		this.permiteAcessarNavegador = permiteAcessarNavegador;
	}

	

	@XmlElement(name = "codigoDocumentacaoMatricula")
	public Integer getCodigoDocumentacaoMatricula() {
		if(codigoDocumentacaoMatricula == null ) {
			codigoDocumentacaoMatricula = 0;
		}
		return codigoDocumentacaoMatricula;
	}

	public void setCodigoDocumentacaoMatricula(Integer codigoDocumentacaoMatricula) {
		this.codigoDocumentacaoMatricula = codigoDocumentacaoMatricula;
	}
}
