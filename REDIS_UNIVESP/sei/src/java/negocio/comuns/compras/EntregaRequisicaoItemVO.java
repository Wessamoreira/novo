package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;


public class EntregaRequisicaoItemVO extends SuperVO {

	private Integer codigo;
	private RequisicaoItemVO requisicaoItem;
	private EntregaRequisicaoVO entregaRequisicaoVO;
	private Double quantidade;
	private List<OperacaoEstoqueVO> listaOperacaoEstoqueVOs;
	private CentroResultadoVO centroResultadoEstoque;
	public static final long serialVersionUID = 1L;

	public EntregaRequisicaoItemVO() {
		super();
		inicializarDados();
	}

	public static void validarDados(EntregaRequisicaoItemVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getRequisicaoItem().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo REQUISIÇÃO ITEM (Itens da Entrega) deve ser informado.");
		}
		Double restante = obj.getRequisicaoItem().getQuantidadeAutorizada() - obj.getRequisicaoItem().getQuantidadeEntregue();
		if (obj.getQuantidade().doubleValue() > restante.doubleValue()) {
			throw new ConsistirException("A QUANTIDADE (Itens da Entrega) do Produto (" + obj.getRequisicaoItem().getProdutoServico().getNome() + ") não pode ser maior do que a quantidade restante para a entrega no caso (" + restante + ").");
		}
	}
	

	public void inicializarDados() {
		setCodigo(0);
		setQuantidade(0.0);
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public EntregaRequisicaoVO getEntregaRequisicaoVO() {
		if (entregaRequisicaoVO == null) {
			entregaRequisicaoVO = new EntregaRequisicaoVO();
		}
		return (entregaRequisicaoVO);
	}

	public void setEntregaRequisicaoVO(EntregaRequisicaoVO entregaRequisicao) {
		this.entregaRequisicaoVO = entregaRequisicao;
	}

	public RequisicaoItemVO getRequisicaoItem() {
		if (requisicaoItem == null) {
			requisicaoItem = new RequisicaoItemVO();
		}
		return (requisicaoItem);
	}

	public void setRequisicaoItem(RequisicaoItemVO requisicaoItem) {
		this.requisicaoItem = requisicaoItem;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<OperacaoEstoqueVO> getListaOperacaoEstoqueVOs() {
		listaOperacaoEstoqueVOs = Optional.ofNullable(listaOperacaoEstoqueVOs).orElse(new ArrayList<>());
		return listaOperacaoEstoqueVOs;
	}

	public void setListaOperacaoEstoqueVOs(List<OperacaoEstoqueVO> listaOperacaoEstoqueVOs) {
		this.listaOperacaoEstoqueVOs = listaOperacaoEstoqueVOs;
	}

	public CentroResultadoVO getCentroResultadoEstoque() {
		centroResultadoEstoque = Optional.ofNullable(centroResultadoEstoque).orElse(new CentroResultadoVO());
		return centroResultadoEstoque;
	}

	public void setCentroResultadoEstoque(CentroResultadoVO centroResultadoEstoque) {
		this.centroResultadoEstoque = centroResultadoEstoque;
	}

}
