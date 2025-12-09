/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;


public class ItemCotacaoUnidadeEnsinoVO extends SuperVO {

	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ProdutoServicoVO produtoVO;
	private ItemCotacaoVO itemCotacao;
	private CotacaoVO cotacao;
	private Double qtdRequisicao;
	private Double totalQtd;
	private Double qtdAdicional;
	private List<RequisicaoItemVO> listaRequisicaoItemVOs;
	public static final long serialVersionUID = 1L;
	/**
	 * campos Transient somemte Para apresentacao Usuario
	 */
	private Double qtdMinimaUnidade;

	public ItemCotacaoUnidadeEnsinoVO getClone() {
		ItemCotacaoUnidadeEnsinoVO clone = new ItemCotacaoUnidadeEnsinoVO();
		clone.setUnidadeEnsinoVO(unidadeEnsinoVO);
		clone.setProdutoVO(produtoVO);
		clone.setCotacao(cotacao);
		clone.setQtdAdicional(qtdAdicional);
		clone.setListaRequisicaoItemVOs(getListaRequisicaoItemVOs());
		return clone;
	}

	public ItemCotacaoUnidadeEnsinoVO() {
		inicializarDados();
	}

	public static void validarDados(ItemCotacaoUnidadeEnsinoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getUnidadeEnsinoVO() == null) || (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo UNIDADE ENSINO (ItemCotacaoUnidadeEnsino) deve ser informado.");
		}
		if ((obj.getProdutoVO() == null) || (obj.getProdutoVO().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo PRODUTO (ItemCotacaoUnidadeEnsino) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getItemCotacao())) {
			throw new ConsistirException("O campo ITEM COTAÇÃO (ItemCotacaoUnidadeEnsino) deve ser informado.");
		}
	}

	public void inicializarDados() {
		setCodigo(0);
		setQtdMinimaUnidade(0.0);
		setQtdAdicional(0.0);
		setItemCotacao(new ItemCotacaoVO());
		setCotacao(new CotacaoVO());
	}

	public void adicionarObjItemRequisicaoVOs(RequisicaoItemVO obj) {
		int index = 0;
		Iterator<RequisicaoItemVO> i = getListaRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO objExistente = i.next();
			if (objExistente.getCodigo().equals(obj.getCodigo())) {
				getListaRequisicaoItemVOs().set(index, obj);
				return;
			}
			index++;
		}
		getListaRequisicaoItemVOs().add(obj);
	}

	public void excluirObjItemRequisicaoVOs(Integer codigo) {
		Iterator<RequisicaoItemVO> i = getListaRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO objExistente = i.next();
			if (objExistente.getCodigo().equals(codigo)) {
				i.remove();
				return;
			}
		}
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public ProdutoServicoVO getProdutoVO() {
		if (produtoVO == null) {
			produtoVO = new ProdutoServicoVO();
		}
		return produtoVO;
	}

	public void setProdutoVO(ProdutoServicoVO produtoVO) {
		this.produtoVO = produtoVO;
	}

	

	public ItemCotacaoVO getItemCotacao() {
		itemCotacao = Optional.ofNullable(itemCotacao).orElse(new ItemCotacaoVO());
		return itemCotacao;
	}

	public void setItemCotacao(ItemCotacaoVO itemCotacao) {
		this.itemCotacao = itemCotacao;
	}

	

	public Double getQtdMinimaUnidade() {
		if (qtdMinimaUnidade == null) {
			qtdMinimaUnidade = 0.0;
		}
		return qtdMinimaUnidade;
	}

	public void setQtdMinimaUnidade(Double qtdMinimaUnidade) {
		this.qtdMinimaUnidade = qtdMinimaUnidade;
	}
	

	public Double getQtdEmEstoque() {
		return getListaRequisicaoItemVOs().stream().mapToDouble(p -> p.getQuantidadeEstoque()).sum();
	}
	
	public Double getQtdRequisicao() {
		return getListaRequisicaoItemVOs().stream().mapToDouble(p -> p.getQuantidadeAutorizada()).sum();
	}
	

	public Double getQtdAdicional() {
		qtdAdicional = Optional.ofNullable(qtdAdicional).orElse(0.0);
		return qtdAdicional;
	}

	public void setQtdAdicional(Double qtdAdicional) {
		this.qtdAdicional = qtdAdicional;
	}

	public Double getTotalQtd() {
		return getQtdRequisicao() + getQtdAdicional();
	}

	public List<RequisicaoItemVO> getListaRequisicaoItemVOs() {
		if (listaRequisicaoItemVOs == null) {
			listaRequisicaoItemVOs = new ArrayList<>();
		}
		return listaRequisicaoItemVOs;
	}

	public void setListaRequisicaoItemVOs(List<RequisicaoItemVO> listaRequisicaoItemVOs) {
		this.listaRequisicaoItemVOs = listaRequisicaoItemVOs;
	}

	public CotacaoVO getCotacao() {
		cotacao = Optional.ofNullable(cotacao).orElse(new CotacaoVO());
		return cotacao;
	}

	public void setCotacao(CotacaoVO cotacao) {
		this.cotacao = cotacao;
	}

}
