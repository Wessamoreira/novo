package negocio.comuns.avaliacaoinst;

import negocio.comuns.arquitetura.SuperVO;

public class AvaliacaoInstitucionalRespondenteVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private String matricula;
	private Integer pessoa;
	private Integer avaliacaoInstitucionalVO;
	private Boolean jrespondeu;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Integer getPessoa() {
		if (pessoa == null) {
			pessoa = 0;
		}
		return pessoa;
	}

	public void setPessoa(Integer pessoa) {
		this.pessoa = pessoa;
	}

	public Integer getAvaliacaoInstitucionalVO() {
		if (avaliacaoInstitucionalVO == null) {
			avaliacaoInstitucionalVO = 0;
		}
		return avaliacaoInstitucionalVO;
	}

	public void setAvaliacaoInstitucionalVO(Integer avaliacaoInstitucionalVO) {
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
	}

	public Boolean getJrespondeu() {
		return jrespondeu;
	}

	public void setJrespondeu(Boolean jrespondeu) {
		this.jrespondeu = jrespondeu;
	}
}
