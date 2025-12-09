package webservice.certisign.comuns;

import java.util.List;

public class CertiSignRSVO {

	private Integer id;
	private String chave;
	private String signUrl;
	private Boolean inProcessing;
	private DocumentRSVO document;
	private DocumentPessoaRSVO sender;
	private List<DocumentPessoaRSVO> signers;
	private List<DocumentPessoaRSVO> electronicSigners;
	private List<DocumentPessoaRSVO> attendees;
	private Integer documentId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getSignUrl() {		
		return signUrl;
	}

	public void setSignUrl(String signUrl) {
		this.signUrl = signUrl;
	}

	public Boolean getInProcessing() {
		return inProcessing;
	}

	public void setInProcessing(Boolean inProcessing) {
		this.inProcessing = inProcessing;
	}

	public DocumentRSVO getDocument() {
		return document;
	}

	public void setDocument(DocumentRSVO document) {
		this.document = document;
	}

	public DocumentPessoaRSVO getSender() {
		return sender;
	}

	public void setSender(DocumentPessoaRSVO sender) {
		this.sender = sender;
	}

	public List<DocumentPessoaRSVO> getSigners() {
		return signers;
	}

	public void setSigners(List<DocumentPessoaRSVO> signers) {
		this.signers = signers;
	}

	public List<DocumentPessoaRSVO> getElectronicSigners() {
		return electronicSigners;
	}

	public void setElectronicSigners(List<DocumentPessoaRSVO> electronicSigners) {
		this.electronicSigners = electronicSigners;
	}

	public List<DocumentPessoaRSVO> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<DocumentPessoaRSVO> attendees) {
		this.attendees = attendees;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

}
