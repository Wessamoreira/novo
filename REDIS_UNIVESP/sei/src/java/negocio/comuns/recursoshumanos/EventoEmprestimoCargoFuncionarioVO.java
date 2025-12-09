package negocio.comuns.recursoshumanos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * @author gilberto.nery
 */
public class EventoEmprestimoCargoFuncionarioVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1375225188515615475L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargoVO;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	
	private Integer numeroParcela;
	private BigDecimal valorParcela;
	private BigDecimal valorTotal;
	private Date dataEmprestimo;
	private Date inicioDesconto;
	private Integer parcelaPaga;
	private Boolean quitado;
	private Date dataPagamento;
	
	@Deprecated
	private TipoEmprestimoVO tipoEmprestimoVO;
	
	//Transiente
	private Boolean itemEmEdicao;

	public enum EnumCampoConsultaEventoEmprestimoCargoFuncionario {
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

	public BigDecimal getValorParcela() {
		if(valorParcela == null)
			valorParcela = BigDecimal.ZERO;
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public BigDecimal getValorTotal() {
		if(valorTotal == null)
			valorTotal = BigDecimal.ZERO;
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getDataEmprestimo() {
		if (dataEmprestimo == null)
			dataEmprestimo = new Date();
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public Date getInicioDesconto() {
		if (inicioDesconto == null)
			inicioDesconto = new Date();
		return inicioDesconto;
	}

	public void setInicioDesconto(Date inicioDesconto) {
		this.inicioDesconto = inicioDesconto;
	}

	public Boolean getItemEmEdicao() {
		if(itemEmEdicao == null)
			itemEmEdicao = false;
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public TipoEmprestimoVO getTipoEmprestimoVO() {
		if(tipoEmprestimoVO == null)
			tipoEmprestimoVO = new TipoEmprestimoVO();
		return tipoEmprestimoVO;
	}

	public void setTipoEmprestimoVO(TipoEmprestimoVO tipoEmprestimoVO) {
		this.tipoEmprestimoVO = tipoEmprestimoVO;
	}

	public Integer getNumeroParcela() {
		if(numeroParcela == null)
			numeroParcela = 0;
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Integer getParcelaPaga() {
		if(parcelaPaga == null)
			parcelaPaga = 0;
		return parcelaPaga;
	}

	public void setParcelaPaga(Integer parcelaPaga) {
		this.parcelaPaga = parcelaPaga;
	}

	public Boolean getQuitado() {
		if (quitado == null) {
			quitado = Boolean.FALSE;
		}
		return quitado;
	}

	public void setQuitado(Boolean quitado) {
		this.quitado = quitado;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
}