package relatorio.negocio.comuns.academico;

import java.util.Date;

public class EstatisticaMatriculaTurnoRelVO {

	private String tipo;
	private String turno;
	private Integer quantidadeMatricula;
	private Date data;

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public Integer getQuantidadeMatricula() {
		if (quantidadeMatricula == null) {
			quantidadeMatricula = 0;
		}
		return quantidadeMatricula;
	}

	public void setQuantidadeMatricula(Integer quantidadeMatricula) {
		this.quantidadeMatricula = quantidadeMatricula;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
