package relatorio.negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

public class CategoriaDespesaTurmaVO extends SuperVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String categoriaDespesa;
	private String turma;
	private Double valor;
	
	public String getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = "";
		}
		return categoriaDespesa;
	}
	public void setCategoriaDespesa(String categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}
	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
}
