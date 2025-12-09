package negocio.comuns.compras;

import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.SuperVOSelecionadoInterface;


public class RecebimentoCompraItemVO extends SuperVO implements SuperVOSelecionadoInterface {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private CompraItemVO compraItem;
	private RecebimentoCompraVO recebimentoCompraVO;
	private Double quantidadeRecebida;
	private Double quantidadeRecebidaAntesAlteracao;
	private Double valorTotal;
	private Double valorUnitario;
	/**
	 * transient
	 */
	private boolean selecionado = true;


	public RecebimentoCompraItemVO() {
		super();
		inicializarDados();
	}
	
	public RecebimentoCompraItemVO getClone() {
		try {
			RecebimentoCompraItemVO clone = (RecebimentoCompraItemVO)super.clone();
			clone.setCodigo(0);
			clone.setNovoObj(true);
			clone.setCompraItem((CompraItemVO)getCompraItem().clone());
			clone.setRecebimentoCompraVO((RecebimentoCompraVO)getRecebimentoCompraVO().clone());
			return clone;	
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}


	public static void validarDados(RecebimentoCompraItemVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getCompraItem().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo COMPRA ITEM (Itens do Recebimento) deve ser informado."); 
		}
		if (obj.getQuantidadeRecebida().doubleValue() < 0) {
			throw new ConsistirException("O campo QUANTIDADE RECEBIDA (Itens do Recebimento) não pode ser menor que 0.");
		}
		if (obj.getQuantidadeRecebida().doubleValue() > Uteis.arrendondarForcando2CadasDecimais(obj.getCompraItem().getQuantidade() - obj.getCompraItem().getQuantidadeRecebida())) {
			throw new ConsistirException("A QUANTIDADE RECEBIDA do item (" + obj.getCompraItem().getProduto().getNome() + ") (Itens do Recebimento) não pode ser maior que " + (obj.getCompraItem().getQuantidade() - obj.getCompraItem().getQuantidadeRecebida().doubleValue()) + ".");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados() {
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setQuantidadeRecebida(0.0);
		setValorTotal(0.0);
		setValorUnitario(0.0);
	}

	public Double getValorTotal() {
		valorTotal = quantidadeRecebida * valorUnitario;
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public CompraItemVO getCompraItem() {
		if (compraItem == null) {
			compraItem = new CompraItemVO();
		}
		return compraItem;
	}

	public void setCompraItem(CompraItemVO compraItem) {
		this.compraItem = compraItem;
	}

	public Double getQuantidadeRecebida() {
		if(quantidadeRecebida == null){
			quantidadeRecebida = 0.0;
		}
		return quantidadeRecebida;
	}

	public void setQuantidadeRecebida(Double quantidadeRecebida) {
		this.quantidadeRecebida = quantidadeRecebida;
	}
	
	

	public Double getQuantidadeRecebidaAntesAlteracao() {
		quantidadeRecebidaAntesAlteracao = Optional.ofNullable(quantidadeRecebidaAntesAlteracao).orElse(0.0);
		return quantidadeRecebidaAntesAlteracao;
	}

	public void setQuantidadeRecebidaAntesAlteracao(Double quantidadeRecebidaAntesAlteracao) {
		this.quantidadeRecebidaAntesAlteracao = quantidadeRecebidaAntesAlteracao;
	}

	public RecebimentoCompraVO getRecebimentoCompraVO() {
		if (recebimentoCompraVO == null) {
			recebimentoCompraVO = new RecebimentoCompraVO();
		}
		return recebimentoCompraVO;
	}

	public void setRecebimentoCompraVO(RecebimentoCompraVO recebimentoCompra) {
		this.recebimentoCompraVO = recebimentoCompra;
	}

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@Override
	public boolean isSelecionado() {
		return selecionado;
	}

	@Override
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public boolean equalsCampoSelecaoLista(RecebimentoCompraItemVO obj){
		return Uteis.isAtributoPreenchido(getCompraItem()) && Uteis.isAtributoPreenchido(obj.getCompraItem()) 
				&& getCompraItem().getCodigo().equals(obj.getCompraItem().getCodigo());
		
	}

}
