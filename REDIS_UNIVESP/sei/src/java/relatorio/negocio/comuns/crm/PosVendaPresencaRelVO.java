package relatorio.negocio.comuns.crm;

import java.io.Serializable;
import java.util.Optional;

public class PosVendaPresencaRelVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9045028785940301708L;
	private String disciplina;
	private String tituloApresentacao;
	private String presenca;

	public String getDisciplina() {
		disciplina = Optional.ofNullable(disciplina).orElse("");
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getTituloApresentacao() {
		tituloApresentacao = Optional.ofNullable(tituloApresentacao).orElse("");
		return tituloApresentacao;
	}

	public void setTituloApresentacao(String tituloApresentacao) {
		this.tituloApresentacao = tituloApresentacao;
	}

	public String getPresenca() {
		presenca = Optional.ofNullable(presenca).orElse("");
		return presenca;
	}

	public void setPresenca(String presenca) {
		this.presenca = presenca;
	}

	

}
