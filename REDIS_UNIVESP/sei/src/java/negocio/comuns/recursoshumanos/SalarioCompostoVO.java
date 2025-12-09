package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade SalarioComposto. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class SalarioCompostoVO extends SuperVO {

	private static final long serialVersionUID = 2759473056686252885L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	private Integer jornada;
	private BigDecimal valorHora;
	private BigDecimal valorMensal;

	// Transiente
	private Boolean itemEmEdicao;
	private HistoricoSalarialVO historicoSalarialVO;

	public enum EnumCampoConsultaSalarioComposto {
		FUNCIONARIO, MATRICULA_CARGO, MATRICULA_FUNCIONARIO, CARGO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if (eventoFolhaPagamento == null) {
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}

	public Integer getJornada() {
		if (jornada == null) {
			jornada = 0;
		}
		return jornada;
	}

	public void setJornada(Integer jornada) {
		this.jornada = jornada;
	}

	public BigDecimal getValorHora() {
		if (valorHora == null) {
			valorHora = BigDecimal.ZERO;
		}
		return valorHora;
	}

	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}

	public BigDecimal getValorMensal() {
		if (valorMensal == null) {
			valorMensal = BigDecimal.ZERO;
		}
		return valorMensal;
	}

	public void setValorMensal(BigDecimal valorMensal) {
		this.valorMensal = valorMensal;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = Boolean.FALSE;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public HistoricoSalarialVO getHistoricoSalarialVO() {
		if (historicoSalarialVO == null) {
			historicoSalarialVO = new HistoricoSalarialVO();
		}
		return historicoSalarialVO;
	}

	public void setHistoricoSalarialVO(HistoricoSalarialVO historicoSalarialVO) {
		this.historicoSalarialVO = historicoSalarialVO;
	}	
}
