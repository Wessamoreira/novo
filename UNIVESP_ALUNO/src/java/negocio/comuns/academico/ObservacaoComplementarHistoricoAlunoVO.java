package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class ObservacaoComplementarHistoricoAlunoVO extends SuperVO {
	
	private Integer codigo;
	private String matricula;
	private Integer gradeCurricular;
	private String observacao;
	private String observacaoTransferenciaMatrizCurricular;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getMatricula() {
		if(matricula == null){
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Integer getGradeCurricular() {
		if(gradeCurricular == null) {
			gradeCurricular = 0;
		}
		return gradeCurricular;
	}
	public void setGradeCurricular(Integer gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}
	public String getObservacao() {
		if(observacao == null) {
			observacao = "";
		}
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public String getObservacaoTransferenciaMatrizCurricular() {
		if (observacaoTransferenciaMatrizCurricular == null) {
			observacaoTransferenciaMatrizCurricular = "";
		}
		return observacaoTransferenciaMatrizCurricular;
	}
	public void setObservacaoTransferenciaMatrizCurricular(String observacaoTransferenciaMatrizCurricular) {
		this.observacaoTransferenciaMatrizCurricular = observacaoTransferenciaMatrizCurricular;
	}
}
