package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;


/**
 * Reponsável por manter os dados da entidade Censo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TurmaEADIPOGVO extends SuperVO {

	private Integer codigo;
	private Integer codTurma;
	private String identificadorTurma;
	private Integer qtdAlunos;
	private Integer qtdAlunosCursandoEAD;
	private Integer qtdDiasAlunosCursandoEAD;
	private Integer usuario;
	private Integer qtdTurmaEAD;
	private Boolean selecionado;
	public static final long serialVersionUID = 1L;

	public TurmaEADIPOGVO() {
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

	public Integer getCodTurma() {
		if (codTurma == null) {
			codTurma = 0;
		}
		return codTurma;
	}

	public void setCodTurma(Integer codTurma) {
		this.codTurma = codTurma;
	}

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
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

	public Integer getQtdDiasAlunosCursandoEAD() {
		if (qtdDiasAlunosCursandoEAD == null) {
			qtdDiasAlunosCursandoEAD = 0;
		}
		return qtdDiasAlunosCursandoEAD;
	}

	public void setQtdDiasAlunosCursandoEAD(Integer qtdDiasAlunosCursandoEAD) {
		this.qtdDiasAlunosCursandoEAD = qtdDiasAlunosCursandoEAD;
	}

	public Integer getUsuario() {
		if (usuario == null) {
			usuario = 0;
		}
		return usuario;
	}

	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}

	public Integer getQtdAlunos() {
		if (qtdAlunos == null) {
			qtdAlunos = 0;
		}
		return qtdAlunos;
	}

	public void setQtdAlunos(Integer qtdAlunos) {
		this.qtdAlunos = qtdAlunos;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getQtdTurmaEAD() {
		if (qtdTurmaEAD == null) {
			qtdTurmaEAD = 0;
		}
		return qtdTurmaEAD;
	}

	public void setQtdTurmaEAD(Integer qtdTurmaEAD) {
		this.qtdTurmaEAD = qtdTurmaEAD;
	}

	
}
