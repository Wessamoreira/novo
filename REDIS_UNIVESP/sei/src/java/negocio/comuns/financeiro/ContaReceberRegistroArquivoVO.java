package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ContaReceberRegistroArquivoVO extends SuperVO {

    private Integer codigo;
    private ContaReceberVO contaReceberVO;
    private ContaReceberVO contaReceberBaixaManualmente;
    private String motivoRejeicao;
    private List<String> motivoRejeicao_Apresentar;
    /**
     * Utilizado no momento do processamento do arquivo de retorno;
     */
    private Boolean contaReceberAgrupada;
    private Boolean contaRecebidaDuplicidade;	
    private Boolean contaRecebidaNegociada;	
    private Integer diasVariacaoDataVencimento;
    private Date dataPrimeiroPagamento;
    private String dataPrimeiroPagamento_Apresentar;
    private Double valorPago; 
    private Integer qtdRestripoPorDataPrimeiroPagamento; 
    private Double valorDesconto;
    
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public ContaReceberVO getContaReceberVO() {
        if (contaReceberVO == null) {
            contaReceberVO = new ContaReceberVO();
        }
        return contaReceberVO;
    }

    public void setContaReceberVO(ContaReceberVO contaReceberVO) {
        this.contaReceberVO = contaReceberVO;
    }

    public Integer getDiasVariacaoDataVencimento() {
        if (diasVariacaoDataVencimento == null) {
            diasVariacaoDataVencimento = 0;
        }
        return diasVariacaoDataVencimento;
    }

    public void setDiasVariacaoDataVencimento(Integer diasVariacaoDataVencimento) {
        this.diasVariacaoDataVencimento = diasVariacaoDataVencimento;
    }

    public String getMotivoRejeicao() {
        if (motivoRejeicao == null) {
            motivoRejeicao = "00000000";
        }
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public List<String> getMotivoRejeicao_Apresentar() {
        if (motivoRejeicao_Apresentar == null) {
            motivoRejeicao_Apresentar = new ArrayList<String>(0);
        }
        return motivoRejeicao_Apresentar;
    }

    public void setMotivoRejeicao_Apresentar(List<String> motivoRejeicao_Apresentar) {
        this.motivoRejeicao_Apresentar = motivoRejeicao_Apresentar;
    }

	public Boolean getContaReceberAgrupada() {
		if (contaReceberAgrupada == null) {
			contaReceberAgrupada = Boolean.FALSE;
		}
		return contaReceberAgrupada;
	}

	public void setContaReceberAgrupada(Boolean contaReceberAgrupada) {
		this.contaReceberAgrupada = contaReceberAgrupada;
	}


	public Boolean getContaRecebidaDuplicidade() {
		if (contaRecebidaDuplicidade == null) {
			contaRecebidaDuplicidade = Boolean.FALSE;
		}
		return contaRecebidaDuplicidade;
	}

	public void setContaRecebidaDuplicidade(Boolean contaRecebidaDuplicidade) {
		this.contaRecebidaDuplicidade = contaRecebidaDuplicidade;
	}
	
	public Date getDataPrimeiroPagamento() {
		return dataPrimeiroPagamento;
	}

	public void setDataPrimeiroPagamento(Date dataPrimeiroPagamento) {
		this.dataPrimeiroPagamento = dataPrimeiroPagamento;
	}

	public String getDataPrimeiroPagamento_Apresentar() {
		if (dataPrimeiroPagamento == null) {
			return "";
		}
		return (Uteis.getData(dataPrimeiroPagamento));
	}

	public void setDataPrimeiroPagamento_Apresentar(String dataPrimeiroPagamento_Apresentar) {
		this.dataPrimeiroPagamento_Apresentar = dataPrimeiroPagamento_Apresentar;
	}

	public Double getValorPago() {
		if (valorPago == null) {
			valorPago = 0.0;
		}
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Integer getQtdRestripoPorDataPrimeiroPagamento() {
		if (qtdRestripoPorDataPrimeiroPagamento == null) {
			qtdRestripoPorDataPrimeiroPagamento = 0;
		}
		return qtdRestripoPorDataPrimeiroPagamento;
	}

	public void setQtdRestripoPorDataPrimeiroPagamento(Integer qtdRestripoPorDataPrimeiroPagamento) {
		this.qtdRestripoPorDataPrimeiroPagamento = qtdRestripoPorDataPrimeiroPagamento;
	}

	public Boolean getContaRecebidaNegociada() {
		if (contaRecebidaNegociada == null) {
			contaRecebidaNegociada = Boolean.FALSE;
		}
		return contaRecebidaNegociada;
	}

	public void setContaRecebidaNegociada(Boolean contaRecebidaNegociada) {
		this.contaRecebidaNegociada = contaRecebidaNegociada;
	}

	public ContaReceberVO getContaReceberBaixaManualmente() {
		if (contaReceberBaixaManualmente == null) {
			contaReceberBaixaManualmente = new ContaReceberVO();
		}
		return contaReceberBaixaManualmente;
	}

	public void setContaReceberBaixaManualmente(ContaReceberVO contaReceberBaixaManualmente) {
		this.contaReceberBaixaManualmente = contaReceberBaixaManualmente;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
}
