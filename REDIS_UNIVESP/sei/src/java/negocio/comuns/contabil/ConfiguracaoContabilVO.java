package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class ConfiguracaoContabilVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2996181862734276533L;
	private Integer codigo;
	private String nome;
	private LayoutIntegracaoVO layoutIntegracaoVO;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasRecebimento;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasPagamento;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasMovimentacaoFinanceira;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasDesconto;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasJuroMultaAcrescimo;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasTaxaCartoes;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasCartaoCredito;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasJuroMultaPagar;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasDescontoPagar;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasNotaFiscalEntradaCategoriaProduto;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasSacado;
	private List<ConfiguracaoContabilRegraVO> listaContabilRegrasNotaFiscalEntradaImposto;
	/**
	 * Transient
	 */
	private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
	
	public void validarDadosListaConfiguracaoContabilRegra(List<ConfiguracaoContabilRegraVO> listaConfiguracaoRegras, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
		Long count = listaConfiguracaoRegras.stream().filter(p-> p.equalsCampoSelecaoLista(configuracaoRegra)).count();
		Uteis.checkState((configuracaoRegra.isEdicaoManual() && count > 1) ||(!configuracaoRegra.isEdicaoManual() && count > 0) , "Não é possível adicionar a "+configuracaoRegra.getTipoRegraContabilEnumApresentar()+" pois será gerado uma duplicidade.");
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

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LayoutIntegracaoVO getLayoutIntegracaoVO() {
		if (layoutIntegracaoVO == null) {
			layoutIntegracaoVO = new LayoutIntegracaoVO();
		}
		return layoutIntegracaoVO;
	}

	public void setLayoutIntegracaoVO(LayoutIntegracaoVO layoutIntegracaoContabilVO) {
		this.layoutIntegracaoVO = layoutIntegracaoContabilVO;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasRecebimento() {
		if (listaContabilRegrasRecebimento == null) {
			listaContabilRegrasRecebimento = new ArrayList<>();
		}
		return listaContabilRegrasRecebimento;
	}

	public void setListaContabilRegrasRecebimento(List<ConfiguracaoContabilRegraVO> listaContabilRegrasRecebimento) {
		this.listaContabilRegrasRecebimento = listaContabilRegrasRecebimento;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasPagamento() {
		if (listaContabilRegrasPagamento == null) {
			listaContabilRegrasPagamento = new ArrayList<>();
		}
		return listaContabilRegrasPagamento;
	}

	public void setListaContabilRegrasPagamento(List<ConfiguracaoContabilRegraVO> listaContabilRegrasPagamento) {
		this.listaContabilRegrasPagamento = listaContabilRegrasPagamento;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasMovimentacaoFinanceira() {
		if (listaContabilRegrasMovimentacaoFinanceira == null) {
			listaContabilRegrasMovimentacaoFinanceira = new ArrayList<>();
		}
		return listaContabilRegrasMovimentacaoFinanceira;
	}

	public void setListaContabilRegrasMovimentacaoFinanceira(List<ConfiguracaoContabilRegraVO> listaContabilRegrasMovimentacaoFinanceira) {
		this.listaContabilRegrasMovimentacaoFinanceira = listaContabilRegrasMovimentacaoFinanceira;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasDesconto() {
		if (listaContabilRegrasDesconto == null) {
			listaContabilRegrasDesconto = new ArrayList<>();
		}
		return listaContabilRegrasDesconto;
	}

	public void setListaContabilRegrasDesconto(List<ConfiguracaoContabilRegraVO> listaContabilRegrasDesconto) {
		this.listaContabilRegrasDesconto = listaContabilRegrasDesconto;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasJuroMultaAcrescimo() {
		if (listaContabilRegrasJuroMultaAcrescimo == null) {
			listaContabilRegrasJuroMultaAcrescimo = new ArrayList<>();
		}
		return listaContabilRegrasJuroMultaAcrescimo;
	}

	public void setListaContabilRegrasJuroMultaAcrescimo(List<ConfiguracaoContabilRegraVO> listaContabilRegrasJuroMultaAcrescimo) {
		this.listaContabilRegrasJuroMultaAcrescimo = listaContabilRegrasJuroMultaAcrescimo;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasNotaFiscalEntradaCategoriaProduto() {
		listaContabilRegrasNotaFiscalEntradaCategoriaProduto = Optional.ofNullable(listaContabilRegrasNotaFiscalEntradaCategoriaProduto).orElse(new ArrayList<>());
		return listaContabilRegrasNotaFiscalEntradaCategoriaProduto;
	}

	public void setListaContabilRegrasNotaFiscalEntradaCategoriaProduto(List<ConfiguracaoContabilRegraVO> listaContabilRegrasNotaFiscalEntradaCategoriaProduto) {
		this.listaContabilRegrasNotaFiscalEntradaCategoriaProduto = listaContabilRegrasNotaFiscalEntradaCategoriaProduto;
	}	

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasSacado() {
		listaContabilRegrasSacado = Optional.ofNullable(listaContabilRegrasSacado).orElse(new ArrayList<>());
		return listaContabilRegrasSacado;
	}

	public void setListaContabilRegrasSacado(List<ConfiguracaoContabilRegraVO> listaContabilRegrasSacado) {
		this.listaContabilRegrasSacado = listaContabilRegrasSacado;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasNotaFiscalEntradaImposto() {
		listaContabilRegrasNotaFiscalEntradaImposto = Optional.ofNullable(listaContabilRegrasNotaFiscalEntradaImposto).orElse(new ArrayList<>());
		return listaContabilRegrasNotaFiscalEntradaImposto;
	}

	public void setListaContabilRegrasNotaFiscalEntradaImposto(List<ConfiguracaoContabilRegraVO> listaContabilRegrasNotaFiscalEntradaImposto) {
		this.listaContabilRegrasNotaFiscalEntradaImposto = listaContabilRegrasNotaFiscalEntradaImposto;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasJuroMultaPagar() {
		listaContabilRegrasJuroMultaPagar = Optional.ofNullable(listaContabilRegrasJuroMultaPagar).orElse(new ArrayList<>());
		return listaContabilRegrasJuroMultaPagar;
	}

	public void setListaContabilRegrasJuroMultaPagar(List<ConfiguracaoContabilRegraVO> listaContabilRegrasJuroMultaPagar) {
		this.listaContabilRegrasJuroMultaPagar = listaContabilRegrasJuroMultaPagar;
	}
	
	

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasDescontoPagar() {
		listaContabilRegrasDescontoPagar = Optional.ofNullable(listaContabilRegrasDescontoPagar).orElse(new ArrayList<>());
		return listaContabilRegrasDescontoPagar;
	}

	public void setListaContabilRegrasDescontoPagar(List<ConfiguracaoContabilRegraVO> listaContabilRegrasDescontoPagar) {
		this.listaContabilRegrasDescontoPagar = listaContabilRegrasDescontoPagar;
	}
	
	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasTaxaCartoes() {
		listaContabilRegrasTaxaCartoes = Optional.ofNullable(listaContabilRegrasTaxaCartoes).orElse(new ArrayList<>());
		return listaContabilRegrasTaxaCartoes;
	}
	
	public void setListaContabilRegrasTaxaCartoes(List<ConfiguracaoContabilRegraVO> listaContabilRegrasTaxaCartoes) {
		this.listaContabilRegrasTaxaCartoes = listaContabilRegrasTaxaCartoes;
	}

	public List<ConfiguracaoContabilRegraVO> getListaContabilRegrasCartaoCredito() {
		listaContabilRegrasCartaoCredito = Optional.ofNullable(listaContabilRegrasCartaoCredito).orElse(new ArrayList<>());
		return listaContabilRegrasCartaoCredito;
	}

	public void setListaContabilRegrasCartaoCredito(List<ConfiguracaoContabilRegraVO> listaContabilRegrasCartaoCredito) {
		this.listaContabilRegrasCartaoCredito = listaContabilRegrasCartaoCredito;
	}

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if (unidadeEnsinoVOs == null) {
			unidadeEnsinoVOs = new ArrayList<>();
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

}
