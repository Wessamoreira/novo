package relatorio.negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

public class FuncionarioCargoRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String matriculaCargo;
	private String nomeFuncionario;
	private String cargo;
	private String departamento;
	private String secao;
	private String identificadorSecao;
	private String formaContratacao;
	
	private String motivoMudanca;
	private String situacao;
	private String dataMudanca;

	public String getMatriculaCargo() {
		return matriculaCargo;
	}

	public void setMatriculaCargo(String matriculaCargo) {
		this.matriculaCargo = matriculaCargo;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getSecao() {
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}

	public String getIdentificadorSecao() {
		return identificadorSecao;
	}

	public void setIdentificadorSecao(String identificadorSecao) {
		this.identificadorSecao = identificadorSecao;
	}

	public String getFormaContratacao() {
		return formaContratacao;
	}

	public void setFormaContratacao(String formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public String getMotivoMudanca() {
		return motivoMudanca;
	}

	public void setMotivoMudanca(String motivoMudanca) {
		this.motivoMudanca = motivoMudanca;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDataMudanca() {
		return dataMudanca;
	}

	public void setDataMudanca(String dataMudanca) {
		this.dataMudanca = dataMudanca;
	}
}
