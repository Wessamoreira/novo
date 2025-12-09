package negocio.comuns.faturamento.nfe;

import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class NotaFiscalEntradaItemRecebimentoVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1412686510890027724L;
	private Integer codigo;
	private NotaFiscalEntradaItemVO notaFiscalEntradaItemVO;
	private RequisicaoItemVO requisicaoItemVO;
	private CompraItemVO compraItemVO;
	private Double quantidadeNotaFiscalEntrada;
	private Double valorUnitario;

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public NotaFiscalEntradaItemVO getNotaFiscalEntradaItemVO() {
		notaFiscalEntradaItemVO = Optional.ofNullable(notaFiscalEntradaItemVO).orElse(new NotaFiscalEntradaItemVO());
		return notaFiscalEntradaItemVO;
	}

	public void setNotaFiscalEntradaItemVO(NotaFiscalEntradaItemVO notaFiscalEntradaItemVO) {
		this.notaFiscalEntradaItemVO = notaFiscalEntradaItemVO;
	}

	public RequisicaoItemVO getRequisicaoItemVO() {
		requisicaoItemVO = Optional.ofNullable(requisicaoItemVO).orElse(new RequisicaoItemVO());
		return requisicaoItemVO;
	}

	public void setRequisicaoItemVO(RequisicaoItemVO requisicaoItemVO) {
		this.requisicaoItemVO = requisicaoItemVO;
	}

	public CompraItemVO getCompraItemVO() {
		compraItemVO = Optional.ofNullable(compraItemVO).orElse(new CompraItemVO());
		return compraItemVO;
	}

	public void setCompraItemVO(CompraItemVO compraItemVO) {
		this.compraItemVO = compraItemVO;
	}

	public Double getQuantidadeNotaFiscalEntrada() {
		quantidadeNotaFiscalEntrada = Optional.ofNullable(quantidadeNotaFiscalEntrada).orElse(0.0);
		return quantidadeNotaFiscalEntrada;
	}

	public void setQuantidadeNotaFiscalEntrada(Double quantidadeRequisicao) {
		this.quantidadeNotaFiscalEntrada = quantidadeRequisicao;
	}

	public Double getValorUnitario() {
		valorUnitario = Optional.ofNullable(valorUnitario).orElse(0.0);
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getValorTotal() {
		return getQuantidadeNotaFiscalEntrada() * getValorUnitario();
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getFuncionarioCargoVO();
		}
		return new FuncionarioCargoVO();
	}

	public DepartamentoVO getDepartamentoVO() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getDepartamento();
		}
		return getCompraItemVO().getDepartamentoVO();
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getUnidadeEnsino();
		}
		return getCompraItemVO().getCompra().getUnidadeEnsino();
	}

	public CursoVO getCursoVO() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getCurso();
		}
		return getCompraItemVO().getCursoVO();
	}

	public TurmaVO getTurmaVO() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getTurma();
		}
		return getCompraItemVO().getTurma();
	}

	public TurnoVO getTurnoVO() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getTurno();
		}
		return getCompraItemVO().getTurnoVO();
	}

	public CategoriaDespesaVO getCategoriaDespesa() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getCategoriaDespesa();
		}
		return getCompraItemVO().getCategoriaDespesa();
	}

	public TipoNivelCentroResultadoEnum getTipoNivelCentroResultadoEnum() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getTipoNivelCentroResultadoEnum();
		}
		return getCompraItemVO().getTipoNivelCentroResultadoEnum();
	}
	
	public CentroResultadoVO getCentroResultadoAdministrativo() {
		if (Uteis.isAtributoPreenchido(getRequisicaoItemVO())) {
			return getRequisicaoItemVO().getRequisicaoVO().getCentroResultadoAdministrativo();
		}
		return getCompraItemVO().getCentroResultadoAdministrativo();
	}	

	public boolean equalsCampoSelecaoLista(NotaFiscalEntradaItemRecebimentoVO obj) {
		return (Uteis.isAtributoPreenchido(getRequisicaoItemVO()) && Uteis.isAtributoPreenchido(obj.getRequisicaoItemVO()) && getRequisicaoItemVO().getCodigo().equals(obj.getRequisicaoItemVO().getCodigo()))
				|| (Uteis.isAtributoPreenchido(getCompraItemVO()) && Uteis.isAtributoPreenchido(obj.getCompraItemVO()) && getCompraItemVO().getCodigo().equals(obj.getCompraItemVO().getCodigo()));

	}

}
