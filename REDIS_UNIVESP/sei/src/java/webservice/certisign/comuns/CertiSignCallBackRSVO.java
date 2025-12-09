package webservice.certisign.comuns;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "")
public class CertiSignCallBackRSVO {

	private Integer documentId;
	private String name;
	private String identifier;
	private String action;
	private String message;
	private String apiDownload;
	private Date dataAssinaturaOrRejeicao;
	
	
	

	@XmlElement(name = "documentId")
	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "identifier")
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@XmlElement(name = "action")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlElement(name = "message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement(name = "apiDownload")
	public String getApiDownload() {
		return apiDownload;
	}

	public void setApiDownload(String apiDownload) {
		this.apiDownload = apiDownload;
	}
	/**
	 *  SIGNATURE-ELETRONIC string Assinatura eletrônica realizada pelo signatário sem certificado digital.		 
	 */
	public boolean isActionSignatureEletronic() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("SIGNATURE-ELETRONIC");
	}
	
	/**
	 * SIGNATURE-DIGITAL string Assinatura digital realizada pelo signatário usando um certificado digital.
	 * @return
	 */
	public boolean isActionSignatureDigital() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("SIGNATURE-DIGITAL");
	}
	
	/**
	 * SIGNATURE-SERVER string Assinatura digital realizada pelo servidor usando um certificado digital da empresa.
	 * @return
	 */
	public boolean isActionSignatureServe() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("SIGNATURE-SERVER");
	}
	
	/**
	 * SIGNATURE-NOK string Assinatura rejeitada pelo signatário.
	 * @return
	 */
	public boolean isActionSignatureNok() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("SIGNATURE-NOK");
	}
	
	/**
	 * AUTORIZAÇÃO-OK string Documento autorizado pelo autorizador.
	 * @return
	 */
	public boolean isActionAutorizationOk() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("AUTHORIZATION-OK");
	}
	
	/**
	 * AUTHORIZATION-NOK string Documento não autorizado pelo autorizador.
	 * @return
	 */
	public boolean isActionAutorizationNok() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("APPROVAL-NOK");
	}
	
	/**
	 * APROVAÇÃO-OK string Documento aprovado pela aprover.
	 * @return
	 */
	public boolean isActionApprovalOk() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("APPROVAL-OK");
	}
	
	/**
	 * APPROVAL-NOK string Documento não aprovado pela aprover.
	 * @return
	 */
	public boolean isActionApprovalNok() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().equals("APPROVAL-NOK");
	}
	
	/**
	 * Todas as assinaturas executadas para a etapa.
	 * @return
	 */
	public boolean isActionStep() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().contains("STEP");
	}
	
	/**
	 * FLOW string Fluxo do documento concluído. Todas as assinaturas realizadas.
	 * @return
	 */
	public boolean isActionFlow() {
		return Uteis.isAtributoPreenchido(getAction()) && getAction().contains("FLOW");
	}

	
	
	public Date getDataAssinaturaOrRejeicao() {
		return dataAssinaturaOrRejeicao;
	}

	public void setDataAssinaturaOrRejeicao(Date dataAssinaturaOrRejeicao) {
		this.dataAssinaturaOrRejeicao = dataAssinaturaOrRejeicao;
	} 
	
	

}
