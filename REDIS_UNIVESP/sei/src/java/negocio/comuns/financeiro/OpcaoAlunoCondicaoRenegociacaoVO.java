package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public class OpcaoAlunoCondicaoRenegociacaoVO implements Serializable {

	private static final long serialVersionUID = 8184602977912744299L;

	private Integer numeroParcela;
	private Boolean possuiEntrada;
	private Double valorEntrada;
	private Double valorParcela;
	private Double valorFinal;
	private Double juroGeral;
	private Double descontoGeral;
	private Double juroEspecifico;
	private Double descontoEspecifico;
	private Double porcentagemEntrada;
	private String descricao;
	private Double valorInicial;
	private Date dataBaseVencimentoParcela;
	private ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO;
	
	public String getDescricao() {
		if (descricao == null) {
			if ((possuiEntrada && numeroParcela == 0) || (possuiEntrada && itemCondicaoRenegociacaoVO.getFaixaEntradaInicial().equals(100.0))) {
				if (getDataBaseVencimentoParcela() != null) {
					descricao = UteisJSF.internacionalizar("prt_opcaoRenegociacao_parcelaUnica_comVencimento")
							.replace("VALOR_PARCELA", Uteis.formatarDoubleParaMoeda((getValorFinal())))
							.replace("DATA_VENCIMENTO", Uteis.getDataAplicandoFormatacao(getDataBaseVencimentoParcela(), "dd/MM/yyyy"));
				} else {
					descricao = UteisJSF.internacionalizar("prt_opcaoRenegociacao_parcelaUnica")
							.replace("VALOR_PARCELA", Uteis.formatarDoubleParaMoeda((getValorEntrada())));
				}
			} else if (possuiEntrada) {
				if (getDataBaseVencimentoParcela() != null) {
					descricao = UteisJSF.internacionalizar("prt_opcaoRenegociacao_entradaDe_eXparcelas_comVencimento")
							 .replace("VALOR_ENTRADA", Uteis.formatarDoubleParaMoeda((getValorEntrada())))
							.replace("NUMERO_PARCELAS", getNumeroParcela().toString() + " Parcela(s)")
							.replace("VALOR_PARCELA", Uteis.formatarDoubleParaMoeda((getValorParcela())))
							.replace("VALOR_TOTAL", Uteis.formatarDoubleParaMoeda((getValorFinal())))
							.replace("DATA_VENCIMENTO", Uteis.getDataAplicandoFormatacao(getDataBaseVencimentoParcela(), "dd/MM/yyyy"));
				} else {
					descricao = UteisJSF.internacionalizar("prt_opcaoRenegociacao_entradaDe_eXparcelas")
							 .replace("VALOR_ENTRADA", Uteis.formatarDoubleParaMoeda((getValorEntrada())))
							.replace("NUMERO_PARCELAS", getNumeroParcela().toString() + " Parcela(s)")
							.replace("VALOR_PARCELA", Uteis.formatarDoubleParaMoeda((getValorParcela())))
							.replace("VALOR_TOTAL", Uteis.formatarDoubleParaMoeda((getValorFinal())));
				}
			} else {
				if (getDataBaseVencimentoParcela() != null) {
					descricao = UteisJSF.internacionalizar("prt_opcaoRenegociacao_parcelasDe_comVencimento")
							.replace("NUMERO_PARCELAS", getNumeroParcela().toString() + " Parcela(s)")
							.replace("VALOR_PARCELA", Uteis.formatarDoubleParaMoeda((getValorParcela())))
							.replace("VALOR_TOTAL", Uteis.formatarDoubleParaMoeda((getValorFinal())))
							.replace("DATA_VENCIMENTO", Uteis.getDataAplicandoFormatacao(getDataBaseVencimentoParcela(), "dd/MM/yyyy"));
				}else {
					descricao = UteisJSF.internacionalizar("prt_opcaoRenegociacao_parcelasDe")
						.replace("NUMERO_PARCELAS", getNumeroParcela().toString() + " Parcela(s)")
						.replace("VALOR_PARCELA", Uteis.formatarDoubleParaMoeda((getValorParcela())))
						.replace("VALOR_TOTAL", Uteis.formatarDoubleParaMoeda((getValorFinal())));
				}
			}
			if (getValorInicial() > getValorFinal()) {
				descricao += " (" + UteisJSF.internacionalizar("prt_opcaoRenegociacao_descontoDe").replace("VALOR_DESCONTO", Uteis.formatarDoubleParaMoeda((getValorInicial() - getValorFinal()))) + ")";
			} else if (getValorInicial() < getValorFinal()) {
				descricao += " (" + UteisJSF.internacionalizar("prt_opcaoRenegociacao_acrescimoDe").replace("VALOR_JURO", Uteis.formatarDoubleParaMoeda((getValorFinal() - getValorInicial()))) + ")";
			}
			descricao += ".";
		}
		return descricao;
	}

	public Integer getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Boolean getPossuiEntrada() {
		return possuiEntrada;
	}

	public void setPossuiEntrada(Boolean possuiEntrada) {
		this.possuiEntrada = possuiEntrada;
	}

	public Double getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(Double valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public Double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Double getJuroGeral() {
		return juroGeral;
	}

	public void setJuroGeral(Double juroGeral) {
		this.juroGeral = juroGeral;
	}

	public Double getDescontoGeral() {
		return descontoGeral;
	}

	public void setDescontoGeral(Double descontoGeral) {
		this.descontoGeral = descontoGeral;
	}

	public Double getJuroEspecifico() {
		return juroEspecifico;
	}

	public void setJuroEspecifico(Double juroEspecifico) {
		this.juroEspecifico = juroEspecifico;
	}

	public Double getDescontoEspecifico() {
		return descontoEspecifico;
	}

	public void setDescontoEspecifico(Double descontoEspecifico) {
		this.descontoEspecifico = descontoEspecifico;
	}

	public Double getPorcentagemEntrada() {
		return porcentagemEntrada;
	}

	public void setPorcentagemEntrada(Double porcentagemEntrada) {
		this.porcentagemEntrada = porcentagemEntrada;
	}

	public Double getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(Double valorFinal) {
		this.valorFinal = valorFinal;
	}

	public Double getValorInicial() {

		return valorInicial;
	}

	public void setValorInicial(Double valorInicial) {
		this.valorInicial = valorInicial;
	}

	public ItemCondicaoRenegociacaoVO getItemCondicaoRenegociacaoVO() {
		return itemCondicaoRenegociacaoVO;
	}

	public void setItemCondicaoRenegociacaoVO(ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO) {
		this.itemCondicaoRenegociacaoVO = itemCondicaoRenegociacaoVO;
	}

	public Date getDataBaseVencimentoParcela() {		
		return dataBaseVencimentoParcela;
	}

	public void setDataBaseVencimentoParcela(Date dataBaseVencimentoParcela) {
		this.dataBaseVencimentoParcela = dataBaseVencimentoParcela;
	}
	
	
}
