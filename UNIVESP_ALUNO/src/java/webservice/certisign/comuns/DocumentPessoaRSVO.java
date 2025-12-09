package webservice.certisign.comuns;

import negocio.comuns.utilitarias.dominios.TipoPessoa;

public class DocumentPessoaRSVO {

	private Integer signerId;
	private String name;
	private String email;
	private String individualIdentificationCode;
	private String action;
	private String signUrl;
	private String title;
	private Integer step;
	private Integer order;
	private DeadlineRSVO deadline;
	private Boolean blockDocumentAfterLimit;
	private String identifier;
	private TipoPessoa tipoPessoa;


	public Integer getSignerId() {		
		return signerId;
	}

	public void setSignerId(Integer signerId) {
		this.signerId = signerId;
	}

	public String getName() {		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {		
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIndividualIdentificationCode() {		
		return individualIdentificationCode;
	}

	public void setIndividualIdentificationCode(String individualIdentificationCode) {
		this.individualIdentificationCode = individualIdentificationCode;
	}

	public String getAction() {		
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSignUrl() {		
		return signUrl;
	}

	public void setSignUrl(String signUrl) {
		this.signUrl = signUrl;
	}

	public String getTitle() {		
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStep() {		
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Integer getOrder() {		
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public DeadlineRSVO getDeadline() {
		if(deadline == null) {
			deadline = new DeadlineRSVO();
		}
		return deadline;
	}
	
	public void setDeadline(DeadlineRSVO deadline) {
		this.deadline = deadline;
	}
	
	public Boolean getBlockDocumentAfterLimit() {
		if(blockDocumentAfterLimit == null) {
			blockDocumentAfterLimit = Boolean.FALSE;
		}
		return blockDocumentAfterLimit;
	}
	
	public void setBlockDocumentAfterLimit(Boolean blockDocumentAfterLimit) {
		this.blockDocumentAfterLimit = blockDocumentAfterLimit;
	}

	public String getIdentifier() {
		if (identifier == null) {
			identifier = "";
		}
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
}
