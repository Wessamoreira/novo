package negocio.comuns.recursoshumanos;

import java.io.Serializable;
import java.math.BigDecimal;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author gilberto.nery
 *
 */
public class EventoFixoCargoFuncionarioVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1375225188515615475L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargoVO;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	private BigDecimal valor;
	private Boolean lancamentoFixo;
	private Integer numeroLancamento;
	private Integer numeroTotalLancamento;
	
	//Transiente
	private Boolean itemEmEdicao;

	public enum EnumCampoConsultaEventoFixoCargoFuncionario {
		MATRICULA_FUNCIONARIO, MATRICULA_CARGO, FUNCIONARIO, EVENTO, CARGO;
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

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamentoVO() {
		if (eventoFolhaPagamentoVO == null) {
			eventoFolhaPagamentoVO = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamentoVO;
	}

	public void setEventoFolhaPagamentoVO(EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		this.eventoFolhaPagamentoVO = eventoFolhaPagamentoVO;
	}

	public BigDecimal getValor() {
		if (valor == null) {
			valor = BigDecimal.ZERO;
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getLancamentoFixo() {
		if (lancamentoFixo == null) {
			lancamentoFixo = Boolean.TRUE;
		}
		return lancamentoFixo;
	}

	public void setLancamentoFixo(Boolean lancamentoFixo) {
		this.lancamentoFixo = lancamentoFixo;
	}

	public Integer getNumeroLancamento() {
		if (numeroLancamento == null) {
			numeroLancamento = 0;
		}
		return numeroLancamento;
	}

	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
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

	public Integer getNumeroTotalLancamento() {
		if(numeroTotalLancamento == null)
			numeroTotalLancamento = 0;
		return numeroTotalLancamento;
	}

	public void setNumeroTotalLancamento(Integer numeroTotalLancamento) {
		this.numeroTotalLancamento = numeroTotalLancamento;
	}
}