/**
 * 
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rodrigo Wind
 *
 */
public class ChoqueHorarioAlunoVO {
	/**
	 * @author Rodrigo Wind - 16/09/2015
	 */
	
	private MatriculaVO matricula;
	private Integer qtdeDiaComChoque;
	private Integer qtdeAulasComChoque;
	private List<ChoqueHorarioAlunoDetalheVO> choqueHorarioAlunoDetalheVOs;
	private List<TurmaDisciplinaVO> turmaDisciplinaVOs;
	/**
	 * @return the matricula
	 */
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}
	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}
	/**
	 * @return the choqueHorarioAlunoDetalheVOs
	 */
	public List<ChoqueHorarioAlunoDetalheVO> getChoqueHorarioAlunoDetalheVOs() {
		if (choqueHorarioAlunoDetalheVOs == null) {
			choqueHorarioAlunoDetalheVOs = new ArrayList<ChoqueHorarioAlunoDetalheVO>();
		}
		return choqueHorarioAlunoDetalheVOs;
	}
	/**
	 * @param choqueHorarioAlunoDetalheVOs the choqueHorarioAlunoDetalheVOs to set
	 */
	public void setChoqueHorarioAlunoDetalheVOs(List<ChoqueHorarioAlunoDetalheVO> choqueHorarioAlunoDetalheVOs) {
		this.choqueHorarioAlunoDetalheVOs = choqueHorarioAlunoDetalheVOs;
	}
	/**
	 * @return the turmaDisciplinaVOs
	 */
	public List<TurmaDisciplinaVO> getTurmaDisciplinaVOs() {
		if (turmaDisciplinaVOs == null) {
			turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		}
		return turmaDisciplinaVOs;
	}
	/**
	 * @param turmaDisciplinaVOs the turmaDisciplinaVOs to set
	 */
	public void setTurmaDisciplinaVOs(List<TurmaDisciplinaVO> turmaDisciplinaVOs) {
		this.turmaDisciplinaVOs = turmaDisciplinaVOs;
	}
	/**
	 * @return the qtdeDiaComChoque
	 */
	public Integer getQtdeDiaComChoque() {
		if (qtdeDiaComChoque == null) {
			qtdeDiaComChoque = 0;
		}
		return qtdeDiaComChoque;
	}
	/**
	 * @param qtdeDiaComChoque the qtdeDiaComChoque to set
	 */
	public void setQtdeDiaComChoque(Integer qtdeDiaComChoque) {
		this.qtdeDiaComChoque = qtdeDiaComChoque;
	}
	/**
	 * @return the qtdeAulasComChoque
	 */
	public Integer getQtdeAulasComChoque() {
		if (qtdeAulasComChoque == null) {
			qtdeAulasComChoque = 0;
		}
		return qtdeAulasComChoque;
	}
	/**
	 * @param qtdeAulasComChoque the qtdeAulasComChoque to set
	 */
	public void setQtdeAulasComChoque(Integer qtdeAulasComChoque) {
		this.qtdeAulasComChoque = qtdeAulasComChoque;
	}
	
	public Integer getQtdeTurmaDisciplinaVOs(){
		return getTurmaDisciplinaVOs().size();
	}
	
	
}
