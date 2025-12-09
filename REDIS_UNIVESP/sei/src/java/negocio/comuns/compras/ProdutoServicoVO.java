package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.enumeradores.OrdenarEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Produto. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os mï¿½todos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProdutoServicoVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	private String descricao;
	private Boolean controlarEstoque;
	private TipoProdutoServicoEnum tipoProdutoServicoEnum;
	private UnidadeMedidaVO unidadeMedida;
	private Boolean exigeCompraCotacao;
	private Double valorUnitario;
	private Double valorUltimaCompra;
	private boolean justificativaRequisicaoObrigatoria = false;
	private CategoriaProdutoVO categoriaProduto;
	private String situacao;
	private Boolean permiteAlterarValorUnitarioRequisicao;
	
	/**
	 * Sempre verificar se Essa lista de Estoque esta Agrupada ou não pois se a mesma estiver agrupada o que sera persistido na base e o estoque que esta dentro da lista 
	 */
	private List<EstoqueVO> estoqueVOs;
	/**
	 * transient
	 */
	private Double qtdeTotal;

	
	public ProdutoServicoVO() {
		super();
		inicializarDados();
	}

	
	public static void validarDados(ProdutoServicoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Produto) deve ser informado.");
		}
		if ((obj.getCategoriaProduto() == null) || (obj.getCategoriaProduto().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CATEGORIA PRODUTO (Produto) deve ser informado.");
		}
		if (obj.getUnidadeMedida().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE MEDIDA (Produto) deve ser informado.");
		}
		if (obj.getControlarEstoque() && (obj.getValorUnitario() == null || obj.getValorUnitario() <= 0.0)) {
			throw new ConsistirException("O campo VALOR UNITÁRIO (Produto) deve ser informado.");
		}
		
	}

	public void inicializarDados() {

	}

	public void montarListaEstoque(List<UnidadeEnsinoVO> listaUnidadeEnsino) {
		if (!Uteis.isAtributoPreenchido(getCodigo())) {
			getEstoqueVOs().clear();
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				EstoqueVO estoque = new EstoqueVO();
				estoque.setUnidadeEnsino(ue);
				estoque.setPrecoUnitario(getValorUnitario());
				ue.setNovoObj(true);
				getEstoqueVOs().add(estoque);
			}
		} else if (listaUnidadeEnsino.size() != getEstoqueVOs().size()) {
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				boolean existe = validarSeExisteUnidadeEnsinoNaListaEstoque(ue);
				if (!existe) {
					EstoqueVO estoque = new EstoqueVO();
					estoque.setPrecoUnitario(getValorUnitario());
					estoque.setUnidadeEnsino(ue);
					getEstoqueVOs().add(estoque);
				}
			}
		}
		agruparListaEstoque();
	}

	private boolean validarSeExisteUnidadeEnsinoNaListaEstoque(UnidadeEnsinoVO ue) {
		for (EstoqueVO e : getEstoqueVOs()) {
			if (e.getUnidadeEnsino().getCodigo().equals(ue.getCodigo())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Rotina que ira percorrer a lista de estoque do produto pois pode existe varios registro de estoque para o mesmo produto.
	 * Sendo assim esse registro do estoque sera agrupado por unidade de ensino e sendo apresentado na tela somente uma vez e ao clicar 
	 * no estoque sera detalhado esse estoque que foi agrupado contendo todos os registro do estoque.
	 */
	public void agruparListaEstoque() {
		setQtdeTotal(0.0);
		List<EstoqueVO> listAgrupada = new ArrayList<>();
		for (EstoqueVO objExistente : getEstoqueVOs()) {
			validarListaEstoqueAgrupada(listAgrupada, objExistente);
		}
		getEstoqueVOs().clear();
		getEstoqueVOs().addAll(listAgrupada);
		Collections.sort(getEstoqueVOs(), OrdenarEstoqueEnum.NOME_UNIDADE_ENSINO);
	}

	private void validarListaEstoqueAgrupada(List<EstoqueVO> listAgrupada, EstoqueVO objExistente) {
		int index = 0;
		for (EstoqueVO objAgrupadoExistente : listAgrupada) {
			if (objAgrupadoExistente.getUnidadeEnsino().getCodigo().equals(objExistente.getUnidadeEnsino().getCodigo())) {
				setQtdeTotal(getQtdeTotal() + objExistente.getQuantidade());
				objAgrupadoExistente.setQuantidade(objExistente.getQuantidade() + objAgrupadoExistente.getQuantidade());
				objAgrupadoExistente.getEstoqueVOs().add(objExistente);
				listAgrupada.set(index, objAgrupadoExistente);
				return;
			}
			index++;
		}
		EstoqueVO objSomenteApresentacao = new EstoqueVO();
		objSomenteApresentacao.setUnidadeEnsino(objExistente.getUnidadeEnsino());
		objSomenteApresentacao.setEstoqueMinimo(objExistente.getEstoqueMinimo());
		objSomenteApresentacao.setEstoqueMaximo(objExistente.getEstoqueMaximo());
		objSomenteApresentacao.setQuantidade(objExistente.getQuantidade());
		objSomenteApresentacao.setPrecoUnitario(objExistente.getPrecoUnitario());
		objSomenteApresentacao.getEstoqueVOs().add(objExistente);
		listAgrupada.add(objSomenteApresentacao);
		setQtdeTotal(getQtdeTotal() + objExistente.getQuantidade());
	}

	public List<EstoqueVO> getEstoqueVOs() {
		if (estoqueVOs == null) {
			estoqueVOs = new ArrayList<>(0);
		}
		return estoqueVOs;
	}

	public void setEstoqueVOs(List<EstoqueVO> estoqueVOs) {
		this.estoqueVOs = estoqueVOs;
	}

	/**
	 * Retorna o objeto da classe <code>CategoriaProduto</code> relacionado com (<code>Produto</code>).
	 */
	public CategoriaProdutoVO getCategoriaProduto() {
		if (categoriaProduto == null) {
			categoriaProduto = new CategoriaProdutoVO();
		}
		return (categoriaProduto);
	}

	/**
	 * Define o objeto da classe <code>CategoriaProduto</code> relacionado com ( <code>Produto</code>).
	 */
	public void setCategoriaProduto(CategoriaProdutoVO obj) {
		this.categoriaProduto = obj;
	}

	public Boolean getControlarEstoque() {
		if (controlarEstoque == null) {
			controlarEstoque = false;
		}
		return controlarEstoque;
	}

	public void setControlarEstoque(Boolean controlarEstoque) {
		this.controlarEstoque = controlarEstoque;
	}

	// public Double getEstoqueMaximo() {
	// return estoqueMaximo;
	// }
	//
	// public void setEstoqueMaximo(Double estoqueMaximo) {
	// this.estoqueMaximo = estoqueMaximo;
	// }
	//
	// public Double getEstoqueMinimo() {
	// return estoqueMinimo;
	// }
	//
	// public void setEstoqueMinimo(Double estoqueMinimo) {
	// this.estoqueMinimo = estoqueMinimo;
	// }
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Boolean getExigeCompraCotacao() {
		if (exigeCompraCotacao == null) {
			exigeCompraCotacao = true;
		}
		return exigeCompraCotacao;
	}

	public void setExigeCompraCotacao(Boolean exigeCompraCotacao) {
		this.exigeCompraCotacao = exigeCompraCotacao;
	}

	public Double getValorUnitario() {
		if (valorUnitario == null) {
			valorUnitario = 0.0;
		}
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getValorUltimaCompra() {
		if (valorUltimaCompra == null) {
			valorUltimaCompra = 0.0;
		}
		return valorUltimaCompra;
	}

	public void setValorUltimaCompra(Double valorUltimaCompra) {
		this.valorUltimaCompra = valorUltimaCompra;
	}

	public UnidadeMedidaVO getUnidadeMedida() {
		if (unidadeMedida == null) {
			unidadeMedida = new UnidadeMedidaVO();
		}
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedidaVO unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public boolean isJustificativaRequisicaoObrigatoria() {
		return justificativaRequisicaoObrigatoria;
	}

	public void setJustificativaRequisicaoObrigatoria(boolean justificativaRequisicaoObrigatoria) {
		this.justificativaRequisicaoObrigatoria = justificativaRequisicaoObrigatoria;
	}

	public TipoProdutoServicoEnum getTipoProdutoServicoEnum() {
		if (tipoProdutoServicoEnum == null) {
			tipoProdutoServicoEnum = TipoProdutoServicoEnum.PRODUTO;
		}
		return tipoProdutoServicoEnum;
	}

	public void setTipoProdutoServicoEnum(TipoProdutoServicoEnum tipoProdutoServicoEnum) {
		this.tipoProdutoServicoEnum = tipoProdutoServicoEnum;
	}

	public String getNome_Abreviado() {
		if (getNome().length() > 16) {
			return getNome().substring(0, 15);
		}
		return getNome();
	}

	/*public Double getQtdeTotal() {
		return getEstoqueVOs().stream().mapToDouble(EstoqueVO::getQuantidade).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}*/
	
	public Double getQtdeTotal() {
		if (qtdeTotal == null) {
			qtdeTotal = 0.0;
		}
		return qtdeTotal;
	}

	public void setQtdeTotal(Double qtdeTotal) {
		this.qtdeTotal = qtdeTotal;
	}
	
	public String getCssJustificativa() {
		return isJustificativaRequisicaoObrigatoria() ? "camposObrigatorios" : "campos";
	}
	
	public String getScriptAceitaValorFracionado() {
		System.out.println(Uteis.isAtributoPreenchido(getUnidadeMedida())  && getUnidadeMedida().isFracionado() ? "aceitarSomenteValorDecimal(this);" : "aceitarSomenteValorNumerico(this);");
		return Uteis.isAtributoPreenchido(getUnidadeMedida())  && getUnidadeMedida().isFracionado() ? "aceitarSomenteValorDecimal(this);" : "aceitarSomenteValorNumerico(this);";
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getPermiteAlterarValorUnitarioRequisicao() {
		if (permiteAlterarValorUnitarioRequisicao == null) {
			permiteAlterarValorUnitarioRequisicao = Boolean.FALSE;
		}
		return permiteAlterarValorUnitarioRequisicao;
	}

	public void setPermiteAlterarValorUnitarioRequisicao(Boolean permiteAlterarValorUnitarioRequisicao) {
		this.permiteAlterarValorUnitarioRequisicao = permiteAlterarValorUnitarioRequisicao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoServicoVO other = (ProdutoServicoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
