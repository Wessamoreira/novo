package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoDesconto;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;

/**
 * 
 * @author PedroOtimize
 *
 */
public class ConfiguracaoContabilRegraVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5975617533176859581L;
	private Integer codigo;
	private ConfiguracaoContabilVO configuracaoContabilVO;
	private PlanoContaVO planoContaVO;
	private List<ConfiguracaoContabilRegraPlanoContaVO> listaConfiguracaoContabilRegraPlanoContaVO;
	private TipoRegraContabilEnum tipoRegraContabilEnum;
	private ContaCorrenteVO contaCorrenteOrigemVO;
	private ContaCorrenteVO contaCorrenteDestinoVO;
	private FormaPagamentoVO formaPagamentoVO;
	private OperadoraCartaoVO operadoraCartaoVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;

	private Integer codigoSacado;
	private TipoOrigemContaReceber tipoOrigemContaReceber;
	private TipoPessoa tipoSacadoReceber;

	private CentroReceitaVO centroReceitaVO;
	private boolean considerarValorDataCompensacao = false;

	private OrigemContaPagar origemContaPagar;
	private TipoSacado tipoSacadoPagar;

	private CategoriaDespesaVO categoriaDespesaVO;

	private TipoDesconto tipoDescontoEnum;

	/**
	 * Nota Fisca Entrada
	 */
	private CategoriaProdutoVO categoriaProdutoVO;
	private ImpostoVO impostoVO;

	/**
	 * Campos transient
	 */
	private ParceiroVO parceiroVO;
	private FornecedorVO fornecedorVO;
	private BancoVO bancoVO;
	private FuncionarioVO funcionarioVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConfiguracaoContabilVO getConfiguracaoContabilVO() {
		if (configuracaoContabilVO == null) {
			configuracaoContabilVO = new ConfiguracaoContabilVO();
		}
		return configuracaoContabilVO;
	}

	public void setConfiguracaoContabilVO(ConfiguracaoContabilVO configuracaoContabilVO) {
		this.configuracaoContabilVO = configuracaoContabilVO;
	}

	public String getTipoRegraContabilEnumApresentar() {
		switch (getTipoRegraContabilEnum()) {
		case RECEBIMENTO:
			return "Regras Para Recebimento";
		case JURO_MULTA_ACRESCIMO:
			return "Regras Para Juro/Multa/Acréscimo";
		case TAXA_CARTAOES:
			return "Regras Para Taxa de Cartões";
		case CARTAO_CREDITO:
			return "Regras Para Cartões de Créditos";
		case DESCONTO:
			return "Regras Para Desconto";
		case PAGAMENTO:
			return "Regras Para Conta Corrente Pagamento";
		case MOVIMENTACAO_FINANCEIRA:
			return "Regras Para Movimentação Financeira";
		case NOTA_FISCAL_ENTRADA_IMPOSTO:
			return "Regras Para Imposto";
		case NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO:
			return "Regras Para Categoria Produto";
		case SACADO:
			return "Regras Para Conta Pagar Sacado";
		case JURO_MULTA_PAGAR:
			return "Regras Para Juro/Multa Pagar";
		case DESCONTO_PAGAR:
			return "Regras Para Desconto Pagar";
		default:
			return "";
		}
	}

	public TipoRegraContabilEnum getTipoRegraContabilEnum() {
		return tipoRegraContabilEnum;
	}

	public void setTipoRegraContabilEnum(TipoRegraContabilEnum tipoRegraContabilEnum) {
		this.tipoRegraContabilEnum = tipoRegraContabilEnum;
	}

	public String getContaCorrenteOrigemApresentar() {
		if (Uteis.isAtributoPreenchido(getContaCorrenteOrigemVO())) {
			return getContaCorrenteOrigemVO().getNomeApresentacaoSistema();
		}
		return "Todas";
	}

	public ContaCorrenteVO getContaCorrenteOrigemVO() {
		if (contaCorrenteOrigemVO == null) {
			contaCorrenteOrigemVO = new ContaCorrenteVO();
		}
		return contaCorrenteOrigemVO;
	}

	public void setContaCorrenteOrigemVO(ContaCorrenteVO contaCorrenteOrigemVO) {
		this.contaCorrenteOrigemVO = contaCorrenteOrigemVO;
	}

	public String getContaCorrenteDestinoApresentar() {
		if (Uteis.isAtributoPreenchido(getContaCorrenteDestinoVO())) {
			return getContaCorrenteDestinoVO().getNumeroDigito();
		}
		return "Todas";
	}

	public ContaCorrenteVO getContaCorrenteDestinoVO() {
		if (contaCorrenteDestinoVO == null) {
			contaCorrenteDestinoVO = new ContaCorrenteVO();
		}
		return contaCorrenteDestinoVO;
	}

	public void setContaCorrenteDestinoVO(ContaCorrenteVO contaCorrenteDestinoVO) {
		this.contaCorrenteDestinoVO = contaCorrenteDestinoVO;
	}

	public String getFormaPagamentoApresentar() {
		if (Uteis.isAtributoPreenchido(getFormaPagamentoVO())) {
			return getFormaPagamentoVO().getNome();
		}
		return "Todas";
	}

	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public String getTipoOrigemContaReceber_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoOrigemContaReceber())) {
			return getTipoOrigemContaReceber().getDescricao();
		}
		return "Todas";
	}

	public TipoOrigemContaReceber getTipoOrigemContaReceber() {
		return tipoOrigemContaReceber;
	}

	public void setTipoOrigemContaReceber(TipoOrigemContaReceber tipoOrigemContaReceber) {
		this.tipoOrigemContaReceber = tipoOrigemContaReceber;
	}

	public String getTipoSacadoReceber_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoSacadoReceber())) {
			return getTipoSacadoReceber().getDescricao();
		}
		return "Todos";
	}

	public TipoPessoa getTipoSacadoReceber() {
		return tipoSacadoReceber;
	}

	public void setTipoSacadoReceber(TipoPessoa tipoSacadoReceber) {
		this.tipoSacadoReceber = tipoSacadoReceber;
	}

	public CentroReceitaVO getCentroReceitaVO() {
		if (centroReceitaVO == null) {
			centroReceitaVO = new CentroReceitaVO();
		}
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
	}

	public boolean isConsiderarValorDataCompensacao() {
		return considerarValorDataCompensacao;
	}

	public void setConsiderarValorDataCompensacao(boolean considerarValorDataCompensacao) {
		this.considerarValorDataCompensacao = considerarValorDataCompensacao;
	}

	public String getOrigemContaPagar_Apresentar() {
		if (Uteis.isAtributoPreenchido(getOrigemContaPagar())) {
			return getOrigemContaPagar().getDescricao();
		}
		return "Todas";
	}

	public OrigemContaPagar getOrigemContaPagar() {
		return origemContaPagar;
	}

	public void setOrigemContaPagar(OrigemContaPagar origemContaPagar) {
		this.origemContaPagar = origemContaPagar;
	}

	public String getTipoSacadoPagar_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoSacadoPagar())) {
			return getTipoSacadoPagar().getDescricao();
		}
		return "Todos";
	}

	public TipoSacado getTipoSacadoPagar() {
		return tipoSacadoPagar;
	}

	public void setTipoSacadoPagar(TipoSacado tipoSacadoPagar) {
		this.tipoSacadoPagar = tipoSacadoPagar;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public String getNomeSacado() {
		if (Uteis.isAtributoPreenchido(getCodigoSacado())) {
			if ((Uteis.isAtributoPreenchido(getTipoSacadoReceber()) && getTipoSacadoReceber().isParceiro()) ||
					(Uteis.isAtributoPreenchido(getTipoSacadoPagar()) && getTipoSacadoPagar().isParceiro())) {
				return getParceiroVO().getNome();
			} else if ((Uteis.isAtributoPreenchido(getTipoSacadoReceber()) && getTipoSacadoReceber().isFornecedor()) ||
					(Uteis.isAtributoPreenchido(getTipoSacadoPagar()) && getTipoSacadoPagar().isFornecedor())) {
				return getFornecedorVO().getNome();
			} else if (Uteis.isAtributoPreenchido(getTipoSacadoPagar()) && getTipoSacadoPagar().isFuncionario()) {
				return getFuncionarioVO().getPessoa().getNome();
			} else if (Uteis.isAtributoPreenchido(getTipoSacadoPagar()) && getTipoSacadoPagar().isBanco()) {
				return getBancoVO().getNome();
			}
		}
		return "Todos";
	}

	public String getTipoSacadoNomeSacado_Apresentar() {
		return getTipoSacadoPagar_Apresentar() + " - " + getNomeSacado();
	}

	public Integer getCodigoSacado() {
		if (codigoSacado == null) {
			codigoSacado = 0;
		}
		return codigoSacado;
	}

	public void setCodigoSacado(Integer codigoSacado) {
		this.codigoSacado = codigoSacado;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public BancoVO getBancoVO() {
		if (bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public String getTipoDescontoEnum_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoDescontoEnum())) {
			return getTipoDescontoEnum().getDescricao();
		}
		return "Todos";
	}

	public TipoDesconto getTipoDescontoEnum() {
		return tipoDescontoEnum;
	}

	public void setTipoDescontoEnum(TipoDesconto tipoDescontoEnum) {
		this.tipoDescontoEnum = tipoDescontoEnum;
	}

	public PlanoContaVO getPlanoContaVO() {
		planoContaVO = Optional.ofNullable(planoContaVO).orElse(new PlanoContaVO());
		return planoContaVO;
	}

	public void setPlanoContaVO(PlanoContaVO planoContaVO) {
		this.planoContaVO = planoContaVO;
	}

	public String getCategoriaProdutoVO_Apresentar() {
		if (Uteis.isAtributoPreenchido(getCategoriaProdutoVO())) {
			return getCategoriaProdutoVO().getNome();
		}
		return "Todas";
	}

	public CategoriaProdutoVO getCategoriaProdutoVO() {
		if (categoriaProdutoVO == null) {
			categoriaProdutoVO = new CategoriaProdutoVO();
		}
		return categoriaProdutoVO;
	}

	public void setCategoriaProdutoVO(CategoriaProdutoVO categoriaProdutoVO) {
		this.categoriaProdutoVO = categoriaProdutoVO;
	}

	public String getImpostoVO_Apresentar() {
		if (Uteis.isAtributoPreenchido(getImpostoVO())) {
			return getImpostoVO().getNome();
		}
		return "Todos";
	}

	public ImpostoVO getImpostoVO() {
		if (impostoVO == null) {
			impostoVO = new ImpostoVO();
		}
		return impostoVO;
	}

	public void setImpostoVO(ImpostoVO impostoVO) {
		this.impostoVO = impostoVO;
	}

	public boolean isCursoInformado() {
		return Uteis.isAtributoPreenchido(getCursoVO());
	}

	public String getPlanoContaUsados_ApresentarHtml() {
		StringBuilder sb = new StringBuilder("");
		if (Uteis.isAtributoPreenchido(getListaConfiguracaoContabilRegraPlanoContaVO())) {
			sb.append("<table cellspacing=0 cellpadding=1 style=\"width:100%\">");
			sb.append("<tr><th style=\"text-align:left;border:none;\">Crédito</th><th style=\"text-align:left;border:none;\">Débito</th></tr>");
			for (ConfiguracaoContabilRegraPlanoContaVO ccrpc : getListaConfiguracaoContabilRegraPlanoContaVO()) {
				sb.append("<tr><td style=\"width:50%\">").append(ccrpc.getPlanoContaCreditoVO().getIdentificacaoPlanoContaComDescricao()).append("</td>");
				sb.append("<td style=\"width:50%\">").append(ccrpc.getPlanoContaDebitoVO().getIdentificacaoPlanoContaComDescricao()).append("</td></tr>");
			}
			sb.append("</table>");
		} else if (Uteis.isAtributoPreenchido(getPlanoContaVO())) {
			sb.append("").append(getPlanoContaVO().getIdentificacaoPlanoContaComDescricao()).append("");
		}
		return sb.toString();
	}

	public List<ConfiguracaoContabilRegraPlanoContaVO> getListaConfiguracaoContabilRegraPlanoContaVO() {
		if (listaConfiguracaoContabilRegraPlanoContaVO == null) {
			listaConfiguracaoContabilRegraPlanoContaVO = new ArrayList<>();
		}
		return listaConfiguracaoContabilRegraPlanoContaVO;
	}

	public void setListaConfiguracaoContabilRegraPlanoContaVO(List<ConfiguracaoContabilRegraPlanoContaVO> listaConfiguracaoContabilRegraPlanoContaVO) {
		this.listaConfiguracaoContabilRegraPlanoContaVO = listaConfiguracaoContabilRegraPlanoContaVO;
	}

	/**
	 * Para decidir qual atributo tem mais prioridade para o TipoRegra foi criado o conceito de peso por atributo assim quando existe na lista duas regras com a quantidade de atributos preenchido iguais a um o que tem maior prioridade sera o que tiver maior peso;
	 * 
	 * @return
	 */
	public Integer getQdtAtributosPreenchido() {
		int quantidade = 0;
		if (getTipoRegraContabilEnum().isRecebimento()) {
			if (Uteis.isAtributoPreenchido(getFormaPagamentoVO())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getContaCorrenteOrigemVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTipoOrigemContaReceber())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTipoSacadoReceber())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCodigoSacado())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}			
			if (isConsiderarValorDataCompensacao()) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCursoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTurnoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getOperadoraCartaoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCentroReceitaVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isPagamento()) {
			if (Uteis.isAtributoPreenchido(getOrigemContaPagar())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getContaCorrenteOrigemVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getFormaPagamentoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getOperadoraCartaoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCategoriaDespesaVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isMovimentacaoFinanceira()) {
			if (Uteis.isAtributoPreenchido(getContaCorrenteOrigemVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getFormaPagamentoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isDesconto()) {
			if (Uteis.isAtributoPreenchido(getTipoDescontoEnum())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCursoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTurnoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTipoSacadoReceber())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCodigoSacado())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isDescontoPagar()) {
			if (Uteis.isAtributoPreenchido(getTipoDescontoEnum())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTipoSacadoPagar())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCodigoSacado())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isJuroMultaAcrescimo()) {
			if (Uteis.isAtributoPreenchido(getTipoDescontoEnum())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCursoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTurnoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isJuroMultaPagar()) {
			if (Uteis.isAtributoPreenchido(getTipoDescontoEnum())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isNotaFiscaEntradaCategoriaProduto()) {
			if (Uteis.isAtributoPreenchido(getCategoriaProdutoVO())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isNotaFiscaEntradaImposto()) {
			if (Uteis.isAtributoPreenchido(getImpostoVO())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isSacado()) {
			if (Uteis.isAtributoPreenchido(getCategoriaDespesaVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTipoSacadoPagar())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCodigoSacado())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
		} else if (getTipoRegraContabilEnum().isTaxaCartoes()) {
			if (Uteis.isAtributoPreenchido(getOperadoraCartaoVO())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getCursoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getTurnoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getFormaPagamentoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}

		} else if (getTipoRegraContabilEnum().isCartaoCredito()) {			
			if (Uteis.isAtributoPreenchido(getOperadoraCartaoVO())) {
				quantidade = quantidade + 2;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getContaCorrenteOrigemVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
			if (Uteis.isAtributoPreenchido(getFormaPagamentoVO())) {
				quantidade = quantidade + 1;// numero representa a prioridade do atributo
			}
		}
		return quantidade;
	}

	public boolean equalsCampoSelecaoLista(ConfiguracaoContabilRegraVO obj) {
		if (getTipoRegraContabilEnum().isRecebimento() && obj.getTipoRegraContabilEnum().isRecebimento()
				&& ((getTipoOrigemContaReceber() == null && obj.getTipoOrigemContaReceber() == null) || (Uteis.isAtributoPreenchido(getTipoOrigemContaReceber()) && Uteis.isAtributoPreenchido(obj.getTipoOrigemContaReceber()) && getTipoOrigemContaReceber().equals(obj.getTipoOrigemContaReceber())))
				&& ((getTipoSacadoReceber() == null && obj.getTipoSacadoReceber() == null) || (Uteis.isAtributoPreenchido(getTipoSacadoReceber()) && Uteis.isAtributoPreenchido(obj.getTipoSacadoReceber()) && getTipoSacadoReceber().equals(obj.getTipoSacadoReceber())))
				&& ((getCodigoSacado() == 0 && obj.getCodigoSacado() == 0) || (Uteis.isAtributoPreenchido(getCodigoSacado()) && Uteis.isAtributoPreenchido(obj.getCodigoSacado()) && getCodigoSacado().equals(obj.getCodigoSacado())))
				&& (getContaCorrenteOrigemVO().getCodigo().equals(obj.getContaCorrenteOrigemVO().getCodigo()))
				&& (isConsiderarValorDataCompensacao() == obj.isConsiderarValorDataCompensacao())
				&& (getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo()))
				&& (getTurnoVO().getCodigo().equals(obj.getTurnoVO().getCodigo()))
				&& (getFormaPagamentoVO().getCodigo().equals(obj.getFormaPagamentoVO().getCodigo()))
				&& (getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()))
				&& (getCentroReceitaVO().getCodigo().equals(obj.getCentroReceitaVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isPagamento() && obj.getTipoRegraContabilEnum().isPagamento()
				&& ((getOrigemContaPagar() == null && obj.getOrigemContaPagar() == null) || (Uteis.isAtributoPreenchido(getOrigemContaPagar()) && Uteis.isAtributoPreenchido(obj.getOrigemContaPagar()) && getOrigemContaPagar().equals(obj.getOrigemContaPagar())))
				&& (getContaCorrenteOrigemVO().getCodigo().equals(obj.getContaCorrenteOrigemVO().getCodigo()))
				&& (getFormaPagamentoVO().getCodigo().equals(obj.getFormaPagamentoVO().getCodigo()))
				&& (getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()))
				&& (getCategoriaDespesaVO().getCodigo().equals(obj.getCategoriaDespesaVO().getCodigo()))) {
			return true;

		} else if (getTipoRegraContabilEnum().isMovimentacaoFinanceira() && obj.getTipoRegraContabilEnum().isMovimentacaoFinanceira()
				&& (getContaCorrenteOrigemVO().getCodigo().equals(obj.getContaCorrenteOrigemVO().getCodigo()))
				&& (getFormaPagamentoVO().getCodigo().equals(obj.getFormaPagamentoVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isDesconto() && obj.getTipoRegraContabilEnum().isDesconto()
				&& ((getTipoDescontoEnum() == null && obj.getTipoDescontoEnum() == null) || (Uteis.isAtributoPreenchido(getTipoDescontoEnum()) && Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum()) && getTipoDescontoEnum().equals(obj.getTipoDescontoEnum())))
				&& ((getCodigoSacado() == 0 && obj.getCodigoSacado() == 0) || (Uteis.isAtributoPreenchido(getCodigoSacado()) && Uteis.isAtributoPreenchido(obj.getCodigoSacado()) && getCodigoSacado().equals(obj.getCodigoSacado())))
				&& ((getTipoSacadoReceber() == null && obj.getTipoSacadoReceber() == null) || (Uteis.isAtributoPreenchido(getTipoSacadoReceber()) && Uteis.isAtributoPreenchido(obj.getTipoSacadoReceber()) && getTipoSacadoReceber().equals(obj.getTipoSacadoReceber())))
				&& (getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo()))
				&& (getTurnoVO().getCodigo().equals(obj.getTurnoVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isDescontoPagar() && obj.getTipoRegraContabilEnum().isDescontoPagar()
				&& ((getTipoDescontoEnum() == null && obj.getTipoDescontoEnum() == null) || (Uteis.isAtributoPreenchido(getTipoDescontoEnum()) && Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum()) && getTipoDescontoEnum().equals(obj.getTipoDescontoEnum())))
				&& ((getCodigoSacado() == 0 && obj.getCodigoSacado() == 0) || (Uteis.isAtributoPreenchido(getCodigoSacado()) && Uteis.isAtributoPreenchido(obj.getCodigoSacado()) && getCodigoSacado().equals(obj.getCodigoSacado())))
				&& ((getTipoSacadoPagar() == null && obj.getTipoSacadoPagar() == null) || (Uteis.isAtributoPreenchido(getTipoSacadoPagar()) && Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar()) && getTipoSacadoPagar().equals(obj.getTipoSacadoPagar())))) {
			return true;
		} else if (getTipoRegraContabilEnum().isJuroMultaAcrescimo() && obj.getTipoRegraContabilEnum().isJuroMultaAcrescimo()
				&& ((getTipoDescontoEnum() == null && obj.getTipoDescontoEnum() == null)
						|| (Uteis.isAtributoPreenchido(getTipoDescontoEnum()) && Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum()) && getTipoDescontoEnum().equals(obj.getTipoDescontoEnum())
								&& getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo())))
				&& (getTurnoVO().getCodigo().equals(obj.getTurnoVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isJuroMultaPagar() && obj.getTipoRegraContabilEnum().isJuroMultaPagar()
				&& ((getTipoDescontoEnum() == null && obj.getTipoDescontoEnum() == null)
						|| (Uteis.isAtributoPreenchido(getTipoDescontoEnum()) && Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum()) && getTipoDescontoEnum().equals(obj.getTipoDescontoEnum())))) {
			return true;
		} else if (getTipoRegraContabilEnum().isNotaFiscaEntradaImposto() && obj.getTipoRegraContabilEnum().isNotaFiscaEntradaImposto()
				&& (getImpostoVO().getCodigo().equals(obj.getImpostoVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isNotaFiscaEntradaCategoriaProduto() && obj.getTipoRegraContabilEnum().isNotaFiscaEntradaCategoriaProduto()
				&& (getCategoriaProdutoVO().getCodigo().equals(obj.getCategoriaProdutoVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isSacado() && obj.getTipoRegraContabilEnum().isSacado()
				&& ((getTipoSacadoPagar() == null && obj.getTipoSacadoPagar() == null) || (Uteis.isAtributoPreenchido(getTipoSacadoPagar()) && Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar()) && getTipoSacadoPagar().equals(obj.getTipoSacadoPagar())))
				&& ((getCodigoSacado() == 0 && obj.getCodigoSacado() == 0) || (Uteis.isAtributoPreenchido(getCodigoSacado()) && Uteis.isAtributoPreenchido(obj.getCodigoSacado()) && getCodigoSacado().equals(obj.getCodigoSacado())))
				&& (getCategoriaDespesaVO().getCodigo().equals(obj.getCategoriaDespesaVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isTaxaCartoes() && obj.getTipoRegraContabilEnum().isTaxaCartoes()
				&& (getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo()))
				&& (getTurnoVO().getCodigo().equals(obj.getTurnoVO().getCodigo()))
				&& (getFormaPagamentoVO().getCodigo().equals(obj.getFormaPagamentoVO().getCodigo()))
				&& (getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()))) {
			return true;
		} else if (getTipoRegraContabilEnum().isCartaoCredito() && obj.getTipoRegraContabilEnum().isCartaoCredito()
				&& (getContaCorrenteOrigemVO().getCodigo().equals(obj.getContaCorrenteOrigemVO().getCodigo()))
				&& (getFormaPagamentoVO().getCodigo().equals(obj.getFormaPagamentoVO().getCodigo()))
				&& (getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()))) {
			return true;
		}
		return false;
	}

	public ConfiguracaoContabilRegraVO getClone() throws CloneNotSupportedException {
		ConfiguracaoContabilRegraVO clone = (ConfiguracaoContabilRegraVO) this.clone();
		clone.setListaConfiguracaoContabilRegraPlanoContaVO(new ArrayList<ConfiguracaoContabilRegraPlanoContaVO>(0));
		for (ConfiguracaoContabilRegraPlanoContaVO configuracaoContabilRegraPlanoContaVO : getListaConfiguracaoContabilRegraPlanoContaVO()) {
			ConfiguracaoContabilRegraPlanoContaVO cloneContabil = (ConfiguracaoContabilRegraPlanoContaVO) configuracaoContabilRegraPlanoContaVO.clone();
			cloneContabil.setConfiguracaoContabilRegraVO(clone);
			clone.getListaConfiguracaoContabilRegraPlanoContaVO().add(cloneContabil);
		}
		return clone;
	}

}
