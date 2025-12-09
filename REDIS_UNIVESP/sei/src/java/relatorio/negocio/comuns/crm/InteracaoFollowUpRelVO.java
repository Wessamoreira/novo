package relatorio.negocio.comuns.crm;

public class InteracaoFollowUpRelVO {

	private Integer codigoProspect;
	private String nomeProspect;
	private String emailProspect;
	private String dataContato;
	private String nomeResponsavel;
	private String tipo;
	private String observacao;
	private String curso;
	private String telefoneResidencial;
	private String celular;
	
	public String getNomeProspect() {
		if (nomeProspect == null) {
			nomeProspect = "";
		}
		return nomeProspect;
	}
	
	public void setNomeProspect(String nomeProspect) {
		this.nomeProspect = nomeProspect;
	}
	
	public String getEmailProspect() {
		if (nomeProspect == null) {
			nomeProspect = "";
		}
		return emailProspect;
	}
	
	public void setEmailProspect(String emailProspect) {
		this.emailProspect = emailProspect;
	}
	
	public String getDataContato() {
		if (dataContato == null) {
			dataContato = "";
		}
		return dataContato;
	}
	
	public void setDataContato(String dataContato) {
		this.dataContato = dataContato;
	}
	
	public String getNomeResponsavel() {
		if (nomeResponsavel == null) {
			nomeResponsavel = "";
		}
		return nomeResponsavel;
	}
	
	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}
	
	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}
	
	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTelefoneResidencial() {
		if (telefoneResidencial == null) {
			telefoneResidencial = "";
		}
		return telefoneResidencial;
	}

	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Integer getCodigoProspect() {
		if (codigoProspect == null) {
			codigoProspect = 0;
		}
		return codigoProspect;
	}

	public void setCodigoProspect(Integer codigoProspect) {
		this.codigoProspect = codigoProspect;
	}
	
	
	

}
