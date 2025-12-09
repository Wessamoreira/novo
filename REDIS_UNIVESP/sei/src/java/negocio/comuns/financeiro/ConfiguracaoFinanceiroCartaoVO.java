package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoFinanceiro. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes
 * atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ConfiguracaoFinanceiroCartaoVO extends SuperVO {

    private Integer codigo;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
    private OperadoraCartaoVO operadoraCartaoVO;
    private ContaCorrenteVO contaCorrenteVO;
    private CategoriaDespesaVO categoriaDespesaVO;
    private CentroResultadoVO centroResultadoAdministrativo;
	private Boolean permitiRecebimentoCartaoOnline;
	private Double taxaDeOperacao;
	private List<TaxaOperacaoCartaoVO> confFinCartaoTaxaOperacao;
	private Double taxaDeAntecipacao;
	private Integer diaBaseCreditoConta;
	private Boolean diaBaseCreditoContaDiasUteis;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public ContaCorrenteVO getContaCorrenteVO() {
        if (contaCorrenteVO == null) {
            contaCorrenteVO = new ContaCorrenteVO();
        }
        return contaCorrenteVO;
    }

    public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
        this.contaCorrenteVO = contaCorrenteVO;
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

    public CategoriaDespesaVO getCategoriaDespesaVO() {
        if (categoriaDespesaVO == null) {
            categoriaDespesaVO = new CategoriaDespesaVO();
        }
        return categoriaDespesaVO;
    }

    public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
        this.categoriaDespesaVO = categoriaDespesaVO;
    }

    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
        if (configuracaoFinanceiroVO == null) {
            configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
        }
        return configuracaoFinanceiroVO;
    }

    public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
        this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
    }
    
    /**
	 * @author Victor Hugo de Paula Costa 20/05/2015 10:54
	 */
	public Boolean getPermitiRecebimentoCartaoOnline() {
		if(permitiRecebimentoCartaoOnline == null) {
			permitiRecebimentoCartaoOnline = false;
		}
		return permitiRecebimentoCartaoOnline;
	}

	public void setPermitiRecebimentoCartaoOnline(Boolean permitiRecebimentoCartaoOnline) {
		this.permitiRecebimentoCartaoOnline = permitiRecebimentoCartaoOnline;
	}

	public Double getTaxaDeOperacao() {
		if(taxaDeOperacao == null) {
			taxaDeOperacao = 0.0;
		}
		return taxaDeOperacao;
	}

	public Double getTaxaBancaria(Integer parcela, TipoFinanciamentoEnum tipoFinanciamento) {
		if (parcela > 0 && !getConfFinCartaoTaxaOperacao().isEmpty()) {
			for (int i = 0; i < getConfFinCartaoTaxaOperacao().size(); i++) {
				TaxaOperacaoCartaoVO taxa = getConfFinCartaoTaxaOperacao().get(i);
				if (taxa.getParcela() >= parcela && tipoFinanciamento.equals(taxa.getTipoFinanciamentoEnum())) {
					return taxa.getTaxa();
				}
			}
			return 0.0;
		} else {
			return getTaxaDeOperacao();
		}
	}

	public void setTaxaDeOperacao(Double taxaDeOperacao) {
		this.taxaDeOperacao = taxaDeOperacao;
	}

	public Double getTaxaDeAntecipacao() {
		if(taxaDeAntecipacao == null) {
			taxaDeAntecipacao = 0.0;
		}
		return taxaDeAntecipacao;
	}

	public void setTaxaDeAntecipacao(Double taxaDeAntecipacao) {
		this.taxaDeAntecipacao = taxaDeAntecipacao;
	}

	public Integer getDiaBaseCreditoConta() {
		if(diaBaseCreditoConta == null) {
			diaBaseCreditoConta = 0;
		}
		return diaBaseCreditoConta;
	}

	public void setDiaBaseCreditoConta(Integer diaBaseCreditoConta) {
		this.diaBaseCreditoConta = diaBaseCreditoConta;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 29/02/2016 09:59
	 */
	private Boolean boletoBancario;

	public Boolean getBoletoBancario() {
		if(boletoBancario == null) {
			boletoBancario = false;
		}
		return boletoBancario;
	}

	public void setBoletoBancario(Boolean boletoBancario) {
		this.boletoBancario = boletoBancario;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 25/04/2016 09:50
	 */ 
	private Integer quantidadeParcelasCartaoCredito;

	public Integer getQuantidadeParcelasCartaoCredito() {
		if (quantidadeParcelasCartaoCredito == null) {
			quantidadeParcelasCartaoCredito = 0;
		}
		return quantidadeParcelasCartaoCredito;
	}

	public void setQuantidadeParcelasCartaoCredito(Integer quantidadeParcelasCartaoCredito) {
		this.quantidadeParcelasCartaoCredito = quantidadeParcelasCartaoCredito;
	}

	public Boolean getDiaBaseCreditoContaDiasUteis() {
		if (diaBaseCreditoContaDiasUteis == null) {
			diaBaseCreditoContaDiasUteis = Boolean.FALSE;
		}
		return diaBaseCreditoContaDiasUteis;
	}

	public void setDiaBaseCreditoContaDiasUteis(Boolean diaBaseCreditoContaDiasUteis) {
		this.diaBaseCreditoContaDiasUteis = diaBaseCreditoContaDiasUteis;
	}

	public List<TaxaOperacaoCartaoVO> getConfFinCartaoTaxaOperacao() {
		if (confFinCartaoTaxaOperacao == null) {
			confFinCartaoTaxaOperacao = new ArrayList<TaxaOperacaoCartaoVO>();
		}
		return confFinCartaoTaxaOperacao;
	}

	public void setConfFinCartaoTaxaOperacao(List<TaxaOperacaoCartaoVO> confFinCartaoTaxaOperacao) {
		this.confFinCartaoTaxaOperacao = confFinCartaoTaxaOperacao;
	}

	public CentroResultadoVO getCentroResultadoAdministrativo() {
		centroResultadoAdministrativo = Optional.ofNullable(centroResultadoAdministrativo).orElse(new CentroResultadoVO());
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(CentroResultadoVO centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}
	
	
}
