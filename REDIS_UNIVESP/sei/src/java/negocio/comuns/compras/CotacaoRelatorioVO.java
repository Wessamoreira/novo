package negocio.comuns.compras;

import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.WordUtils;

import com.google.common.base.Strings;

public class CotacaoRelatorioVO {

	private FornecedorVO fornecedorVO;
	private String unidadeEnsino;
	private Long quantidade;
	private BigDecimal precoUnitario;
	private BigDecimal precoTotal;
	private ProdutoServicoVO produto;
	private CotacaoVO cotacao;
	private String formaPagamento;
	private CompraVO compra;

	public CotacaoRelatorioVO(CompraVO compraVO, CotacaoVO cotacao, FornecedorVO fornecedorVO, ProdutoServicoVO produto, String formaPagamento, String unidadeEnsino, Long quantidade, BigDecimal precoUnitario, BigDecimal precoTotal) {
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
		this.precoTotal = precoTotal;
		this.unidadeEnsino = unidadeEnsino;
		this.fornecedorVO = fornecedorVO;
		this.produto = produto;
		this.cotacao = cotacao;
		this.formaPagamento = formaPagamento;
		this.compra = compraVO;
	}

	public String getFornecedorCotacao() {
		return WordUtils.capitalize(fornecedorVO.getNome().toLowerCase());
	}

	public String getUnidadeEnsino() {
		return WordUtils.capitalize(unidadeEnsino.toLowerCase());
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getPrecoTotal() {
		return precoTotal;
	}

	public void setPrecoTotal(BigDecimal precoTotal) {
		this.precoTotal = precoTotal;
	}

	public Integer getCodigoFornecedor() {
		return fornecedorVO.getCodigo();
	}

	public String getFornecedorEndereco() {
		return fornecedorVO.getEnderecoCompleto();
	}

	public String getFornecedorCNPJ() {
		return fornecedorVO.getCNPJ();
	}

	public String getFornecedorTelefone() {
		return Stream.of(fornecedorVO.getTelComercial1(), fornecedorVO.getTelComercial2(), fornecedorVO.getTelComercial3())
		        .filter(p -> !Strings.isNullOrEmpty(p)).collect(Collectors.joining(" / "));
	}

	public String getProdutoNome() {
		return this.produto.getNome();
	}

	public String getProdutoCategoriaProduto() {
		return this.produto.getCategoriaProduto().getNome();
	}

	public String getFornecedorEmail() {
		return fornecedorVO.getEmail();
	}

	public String getCotacaoNumero() {
		return cotacao.getCodigo().toString();
	}

	public String getCotacaoResponsavel() {
		return cotacao.getResponsavelCotacao().getNome();
	}

	public String getCotacaoSituacao() {
		return cotacao.getSituacao_Apresentar();
	}

	public Date getCotacaoData() {
		return cotacao.getDataCotacao();
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public String getCondicaoPagamento() {
		return compra.getCondicaoPagamento().getNome();
	}

}