package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class CriterioAvaliacaoNotaConceitoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7171684180750677301L;
	private Integer codigo;
	private NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacao;
	private Double peso;
	private CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo;
	private Integer ordem;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public NotaConceitoIndicadorAvaliacaoVO getNotaConceitoIndicadorAvaliacao() {
		if (notaConceitoIndicadorAvaliacao == null) {
			notaConceitoIndicadorAvaliacao = new NotaConceitoIndicadorAvaliacaoVO();
		}
		return notaConceitoIndicadorAvaliacao;
	}

	public void setNotaConceitoIndicadorAvaliacao(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacao) {
		this.notaConceitoIndicadorAvaliacao = notaConceitoIndicadorAvaliacao;
	}

	

	public Double getPeso() {
		if (peso == null) {
			peso = 0.0;
		}
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public CriterioAvaliacaoPeriodoLetivoVO getCriterioAvaliacaoPeriodoLetivo() {
		if (criterioAvaliacaoPeriodoLetivo == null) {
			criterioAvaliacaoPeriodoLetivo = new CriterioAvaliacaoPeriodoLetivoVO();
		}
		return criterioAvaliacaoPeriodoLetivo;
	}

	public void setCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo) {
		this.criterioAvaliacaoPeriodoLetivo = criterioAvaliacaoPeriodoLetivo;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	

}
