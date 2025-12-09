package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.sad.DespesaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;


public class EntregaRequisicaoVO extends SuperVO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1169788325427394154L;
	private Integer codigo;
	private Date data;
	private List<EntregaRequisicaoItemVO> entregaRequisicaoItemVOs;
	private RequisicaoVO requisicao;
	private UsuarioVO responsavel;
	/**
	 * Transient
	 */
	private Double quantidadeTotalEstoque = 0.0;
	private	Double valorTotalEstoque = 0.0;
	
	

	public EntregaRequisicaoVO() {
		super();
		inicializarDados();
	}

	public static void validarDados(EntregaRequisicaoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getRequisicao() == null) || (obj.getRequisicao().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo REQUISIÇÃO (Entrega Requisição) deve ser informado.");
		}
		if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL (Entrega Requisição) deve ser informado.");
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (Entrega Requisição) deve ser informado.");
		}
		Uteis.checkState(obj.getEntregaRequisicaoItemVOs().isEmpty(), "Dever ser informado pelo menos um Item de Entrega da Requisição.");
		obj.getEntregaRequisicaoItemVOs().stream()
		.filter(p-> Uteis.isAtributoPreenchido(p.getRequisicaoItem().getTipoAutorizacaoRequisicaoEnum()) && p.getRequisicaoItem().getTipoAutorizacaoRequisicaoEnum().isRetirada() &&  !Uteis.isAtributoPreenchido(p.getCentroResultadoEstoque()))
		.forEach(p->{
			throw new StreamSeiException("Não foi definido o Centro Resultado Estoque para o item "+p.getRequisicaoItem().getProdutoServico().getNome()+" da Requisição.");
		});
	}

	public void realizarUpperCaseDados() {
	}

	public void inicializarDados() {
		setCodigo(0);
		setData(new Date());
	}

	public DespesaDWVO getDespesaDW(Double valor, Integer funcionario) {
		DespesaDWVO obj = new DespesaDWVO("RE");
		obj.setAno(Uteis.getAnoData(data));
		obj.setMes(Uteis.getMesDataAtual());
		obj.setData(new Date());
		obj.getCategoriaDespesa().setCodigo(getRequisicao().getCategoriaDespesa().getCodigo());
		obj.getFornecedor().setCodigo(getRequisicao().getSacadoFornecedor().getCodigo());
		obj.getFuncionario().setCodigo(getRequisicao().getSacadoFuncionario().getCodigo());
		obj.getFuncionarioCentroCusto().setCodigo(funcionario);
		obj.getDepartamento().setCodigo(getRequisicao().getDepartamento().getCodigo());
		obj.getTurma().setCodigo(getRequisicao().getTurma().getCodigo());
		obj.getCurso().setCodigo(getRequisicao().getTurma().getCurso().getCodigo());
		obj.getTurno().setCodigo(getRequisicao().getTurma().getTurno().getCodigo());
		obj.getAreaConhecimento().setCodigo(getRequisicao().getTurma().getCurso().getAreaConhecimento().getCodigo());
		obj.getUnidadeEnsino().setCodigo(getRequisicao().getUnidadeEnsino().getCodigo());
		obj.setTipoSacado(getRequisicao().getTipoSacado());
		obj.setValor(valor);
		return obj;
	}

	public void adicionarObjEntregaRequisicaoItemVOs(EntregaRequisicaoItemVO obj) {
		int index = 0;
		for (EntregaRequisicaoItemVO objExistente : getEntregaRequisicaoItemVOs()) {
			if (objExistente.getRequisicaoItem().getCodigo().equals(obj.getRequisicaoItem().getCodigo())) {
				getEntregaRequisicaoItemVOs().set(index, obj);
				return;
			}
			index++;
		}
		obj.setQuantidade(obj.getRequisicaoItem().getQuantidadeAutorizada() - obj.getRequisicaoItem().getQuantidadeEntregue());
		getEntregaRequisicaoItemVOs().add(obj);
	}

	public void excluirObjEntregaRequisicaoItemVOs(Integer requisicaoItem) {
		Iterator<EntregaRequisicaoItemVO> i = getEntregaRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			EntregaRequisicaoItemVO objExistente =  i.next();
			if (objExistente.getRequisicaoItem().getCodigo().equals(requisicaoItem)) {
				i.remove();
				return;
			}
		}
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public RequisicaoVO getRequisicao() {
		if (requisicao == null) {
			requisicao = new RequisicaoVO();
		}
		return (requisicao);
	}

	public void setRequisicao(RequisicaoVO obj) {
		this.requisicao = obj;
	}

	public List<EntregaRequisicaoItemVO> getEntregaRequisicaoItemVOs() {
		if (entregaRequisicaoItemVOs == null) {
			entregaRequisicaoItemVOs = new ArrayList<>();
		}
		return (entregaRequisicaoItemVOs);
	}

	public void setEntregaRequisicaoItemVOs(List<EntregaRequisicaoItemVO> entregaRequisicaoItemVOs) {
		this.entregaRequisicaoItemVOs = entregaRequisicaoItemVOs;
	}

	public Date getData() {
		return (data);
	}

	public String getData_Apresentar() {
		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getQuantidadeTotalEstoque() {
		quantidadeTotalEstoque = Optional.ofNullable(quantidadeTotalEstoque).orElse(0.0);
		return quantidadeTotalEstoque;
	}

	public void setQuantidadeTotalEstoque(Double quantidadeTotalEstoque) {
		this.quantidadeTotalEstoque = quantidadeTotalEstoque;
	}

	public Double getValorTotalEstoque() {
		valorTotalEstoque = Optional.ofNullable(valorTotalEstoque).orElse(0.0);
		return valorTotalEstoque;
	}

	public void setValorTotalEstoque(Double valorTotalEstoque) {
		this.valorTotalEstoque = valorTotalEstoque;	
	}
	
	
	
}
