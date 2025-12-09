package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;


public class IntegracaoFinanceiroContaReceberVO extends SuperVO {

    private Integer codigo;
    private IntegracaoFinanceiroVO integracaoFinanceiro;
    private Boolean processadoComSucesso;
    private ContaReceberVO contaReceber;
    public static final long serialVersionUID = 1L;

    public IntegracaoFinanceiroContaReceberVO() {
        super();
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

	public Boolean getProcessadoComSucesso() {
		if (processadoComSucesso == null) {
			processadoComSucesso = false;
		}
		return processadoComSucesso;
	}

	public void setProcessadoComSucesso(Boolean processadoComSucesso) {
		this.processadoComSucesso = processadoComSucesso;
	}

	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public IntegracaoFinanceiroVO getIntegracaoFinanceiro() {
		if (integracaoFinanceiro == null) {
			integracaoFinanceiro = new IntegracaoFinanceiroVO();
		}
		return integracaoFinanceiro;
	}

	public void setIntegracaoFinanceiro(IntegracaoFinanceiroVO integracaoFinanceiro) {
		this.integracaoFinanceiro = integracaoFinanceiro;
	}

}
