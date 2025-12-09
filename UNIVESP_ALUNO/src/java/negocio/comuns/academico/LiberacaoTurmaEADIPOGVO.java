package negocio.comuns.academico;


import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Censo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class LiberacaoTurmaEADIPOGVO extends SuperVO {

	private Integer codigo;
	private Integer modulo;
	private Integer qtdTurma;
	private Integer qtdTurmaCursandoEAD;
	private Integer qtdAlunosCursandoEAD;
//	private List<TurmaEADIPOGVO> listaTurmaEAD;
	public static final long serialVersionUID = 1L;

	public LiberacaoTurmaEADIPOGVO() {
		super();
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getModulo() {
		if (modulo == null) {
			modulo = 0;
		}
		return modulo;
	}

	public void setModulo(Integer modulo) {
		this.modulo = modulo;
	}

	public Integer getQtdTurma() {
		if (qtdTurma == null) {
			qtdTurma = 0;
		}
		return qtdTurma;
	}

	public void setQtdTurma(Integer qtdTurma) {
		this.qtdTurma = qtdTurma;
	}

	public Integer getQtdTurmaCursandoEAD() {
		if (qtdTurmaCursandoEAD == null) {
			qtdTurmaCursandoEAD = 0;
		}
		return qtdTurmaCursandoEAD;
	}

	public void setQtdTurmaCursandoEAD(Integer qtdTurmaCursandoEAD) {
		this.qtdTurmaCursandoEAD = qtdTurmaCursandoEAD;
	}

	public Integer getQtdAlunosCursandoEAD() {
		if (qtdAlunosCursandoEAD == null) {
			qtdAlunosCursandoEAD = 0;
		}
		return qtdAlunosCursandoEAD;
	}

	public void setQtdAlunosCursandoEAD(Integer qtdAlunosCursandoEAD) {
		this.qtdAlunosCursandoEAD = qtdAlunosCursandoEAD;
	}

	
}
