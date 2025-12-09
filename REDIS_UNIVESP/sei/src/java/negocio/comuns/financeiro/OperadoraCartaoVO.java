package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.OperadoraCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoFormaArredondamentoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsável por manter os dados da entidade OperadoraCartao. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class OperadoraCartaoVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;;
	private String nome;
	private String tipo;
	private OperadoraCartaoCreditoEnum operadoraCartaoCreditoEnum;
	private FormaPagamentoVO formaPagamentoPadraoRecebimentoOnline;
	private TipoFinanciamentoEnum tipoFinanciamentoEnum;
	private boolean regraAplicarDiferencaValorReceberPrimeiraParcela = false;
	private TipoFormaArredondamentoEnum tipoFormaArredondamentoEnum;

	/**
	 * Construtor padrão da classe <code>OperadoraCartao</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public OperadoraCartaoVO() {
		super();
	}

	public String getTipo_Apresentar() {
		if(Uteis.isAtributoPreenchido(getTipo())){
			return UteisJSF.internacionalizar("enum_TipoCartaoOperadoraCartaoEnum_" + getTipo());	
		}
		return "";
	}
	
	public String getTipoApresentarReduzido() {
		if ("CARTAO_DEBITO".equals(getTipo())) {
			return "Débito";
		} else {
			return "Crédito";
		}
	}
	
	public String getTipo() {
		if (tipo == null) {
			return "CARTAO_CREDITO";
		}
		return (tipo);
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		if (nome == null) {
			return "";
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

	public OperadoraCartaoCreditoEnum getOperadoraCartaoCreditoEnum() {
		if (operadoraCartaoCreditoEnum == null) {
			operadoraCartaoCreditoEnum = OperadoraCartaoCreditoEnum.VISA;
		}
		return operadoraCartaoCreditoEnum;
	}

	public String getOperadoraCartaoCreditoApresentar() {
		return getOperadoraCartaoCreditoEnum().getValorApresentar();	
	}

	public void setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum operadoraCartaoCreditoEnum) {
		this.operadoraCartaoCreditoEnum = operadoraCartaoCreditoEnum;
	}
	
	public String getOperadoraCartaoCreditoApresentarDetalhado() {
		if(getTipo().equals("CARTAO_DEBITO")  || (getTipo().equals("CARTAO_CREDITO") &&  getNome().trim().equalsIgnoreCase(getOperadoraCartaoCreditoEnum().getValorApresentar()))) {
			return getNome();
		}else if((getTipo().equals("CARTAO_CREDITO") &&  !getNome().trim().equalsIgnoreCase(getOperadoraCartaoCreditoEnum().getValorApresentar()))) {
			return getNome()+" - "+getOperadoraCartaoCreditoEnum().getValorApresentar();
		}
		return getNome();
	}
	
	private Boolean filtrarOperadoraCartaoVO;

	public Boolean getFiltrarOperadoraCartaoVO() {
		if (filtrarOperadoraCartaoVO == null) {
			filtrarOperadoraCartaoVO = false;
		}
		return filtrarOperadoraCartaoVO;
	}

	public void setFiltrarOperadoraCartaoVO(Boolean filtrarOperadoraCartaoVO) {
		this.filtrarOperadoraCartaoVO = filtrarOperadoraCartaoVO;
	}
	
	public FormaPagamentoVO getFormaPagamentoPadraoRecebimentoOnline() {
		if (formaPagamentoPadraoRecebimentoOnline == null) {
			formaPagamentoPadraoRecebimentoOnline = new FormaPagamentoVO();
		}
		return formaPagamentoPadraoRecebimentoOnline;
	}
	
	public void setFormaPagamentoPadraoRecebimentoOnline(FormaPagamentoVO formaPagamentoPadraoRecebimentoOnline) {
		this.formaPagamentoPadraoRecebimentoOnline = formaPagamentoPadraoRecebimentoOnline;
	}
	public TipoFinanciamentoEnum getTipoFinanciamentoEnum() {
		if (tipoFinanciamentoEnum == null) {
			tipoFinanciamentoEnum = TipoFinanciamentoEnum.AMBAS;
		}
		return tipoFinanciamentoEnum;
	}


	public void setTipoFinanciamentoEnum(TipoFinanciamentoEnum tipoFinanciamentoEnum) {
		this.tipoFinanciamentoEnum = tipoFinanciamentoEnum;
	}

	
	public boolean isRegraAplicarDiferencaValorReceberPrimeiraParcela() {
		return regraAplicarDiferencaValorReceberPrimeiraParcela;
	}

	public void setRegraAplicarDiferencaValorReceberPrimeiraParcela(boolean regraAplicarDiferencaValorReceberPrimeiraParcela) {
		this.regraAplicarDiferencaValorReceberPrimeiraParcela = regraAplicarDiferencaValorReceberPrimeiraParcela;
	}

	public TipoFormaArredondamentoEnum getTipoFormaArredondamentoEnum() {
		if (tipoFormaArredondamentoEnum == null) {
			tipoFormaArredondamentoEnum = TipoFormaArredondamentoEnum.ARREDONDADO;
		}
		return tipoFormaArredondamentoEnum;
	}

	public void setTipoFormaArredondamentoEnum(TipoFormaArredondamentoEnum tipoFormaArredondamentoEnum) {
		this.tipoFormaArredondamentoEnum = tipoFormaArredondamentoEnum;
	}
	
	
	
	

}
