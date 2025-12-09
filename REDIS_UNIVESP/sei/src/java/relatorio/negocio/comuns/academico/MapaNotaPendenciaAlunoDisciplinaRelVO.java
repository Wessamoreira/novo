package relatorio.negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Wellington Rodrigues - 22 de jul de 2015
 *
 */
public class MapaNotaPendenciaAlunoDisciplinaRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String disciplina;
	private Integer codigoDisciplina;
	private Integer qtdeAlunoDisciplina;

	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public Integer getQtdeAlunoDisciplina() {
		if (qtdeAlunoDisciplina == null) {
			qtdeAlunoDisciplina = 0;
		}
		return qtdeAlunoDisciplina;
	}

	public void setQtdeAlunoDisciplina(Integer qtdeAlunoDisciplina) {
		this.qtdeAlunoDisciplina = qtdeAlunoDisciplina;
	}

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}
}
