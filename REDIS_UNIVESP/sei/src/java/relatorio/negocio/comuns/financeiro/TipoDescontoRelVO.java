/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class TipoDescontoRelVO {

	private String turma;
	private String curso;
	private List<AlunoTipoDescontoRelVO> alunos;

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public List<AlunoTipoDescontoRelVO> getAlunos() {
		if (alunos == null) {
			alunos = new ArrayList<AlunoTipoDescontoRelVO>(0);
		}
		return alunos;
	}

	public void setAlunos(List<AlunoTipoDescontoRelVO> alunos) {
		this.alunos = alunos;
	}

	public JRDataSource getAlunosJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getAlunos().toArray());
		return jr;
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

}
