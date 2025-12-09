package negocio.comuns.financeiro;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class UnidadeEnsinoContaCorrenteVO extends SuperVO {

    private UnidadeEnsinoVO unidadeEnsino;
    private Integer contaCorrente;
    private Boolean utilizarRemessa;
    private Boolean utilizarNegociacao;
    private Boolean usarPorDefaultMovimentacaoFinanceira;
    public static final long serialVersionUID = 1L;

    public UnidadeEnsinoContaCorrenteVO() {
        super();
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setContaCorrente(Integer contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Integer getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = 0;
        }
        return contaCorrente;
    }

	public Boolean getUtilizarRemessa() {
		if (utilizarRemessa == null) {
			utilizarRemessa = Boolean.FALSE;
		}
		return utilizarRemessa;
	}

	public void setUtilizarRemessa(Boolean utilizarRemessa) {
		this.utilizarRemessa = utilizarRemessa;
	}

	public Boolean getUtilizarNegociacao() {
		if (utilizarNegociacao == null) {
			utilizarNegociacao = Boolean.FALSE;
		}
		return utilizarNegociacao;
	}
	
	public void setUtilizarNegociacao(Boolean utilizarNegociacao) {
		this.utilizarNegociacao = utilizarNegociacao;
	}

	public Boolean getUsarPorDefaultMovimentacaoFinanceira() {
		if (usarPorDefaultMovimentacaoFinanceira == null) {
			usarPorDefaultMovimentacaoFinanceira = Boolean.FALSE;
		}
		return usarPorDefaultMovimentacaoFinanceira;
	}

	public void setUsarPorDefaultMovimentacaoFinanceira(Boolean usarPorDefaultMovimentacaoFinanceira) {
		this.usarPorDefaultMovimentacaoFinanceira = usarPorDefaultMovimentacaoFinanceira;
	}
	
	
}
