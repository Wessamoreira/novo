/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

/**
 *
 * @author Philippe
 */
public class ContaReceberNaoLocalizadaArquivoRetornoVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3021579109565045935L;
	private Integer codigo;
    private String nossoNumero;
    private Double valor;
    private Double valorRecebido;
    private Date dataVcto;
    private Date dataPagamento;
    private Boolean tratada;
    private String observacao;
    private ContaCorrenteVO contaCorrenteVO;
    private ContaReceberVO contaReceberVO;

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public String getDataPagamento_Apresentar() {
        if (dataPagamento != null) {
            return Uteis.getData(dataPagamento);
        }
        return null;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    private String situacao;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNossoNumero() {
        if (nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorRecebido() {
        if (valorRecebido == null) {
            valorRecebido = 0.0;
        }
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Date getDataVcto() {
        if (dataVcto == null) {
            dataVcto = new Date();
        }
        return dataVcto;
    }

    public String getDataVcto_Apresentar() {
        return Uteis.getData(getDataVcto());
    }

    public void setDataVcto(Date dataVcto) {
        this.dataVcto = dataVcto;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "AR";
        }
        return situacao;
    }

    public String getSituacao_Apresentar() {
        return SituacaoContaReceber.getDescricao(situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getTratada() {
        if (tratada == null) {
            tratada = Boolean.FALSE;
        }
        return tratada;
    }

    public void setTratada(Boolean tratada) {
        this.tratada = tratada;
    }

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}


	public String getApresentarDadosContaReceber() {
		if (getContaReceberVO().getCodigo() > 0) {
			return getContaReceberVO().getNossoNumero() + " - " + getContaReceberVO().getNomePessoa(); 
		}
		return "";
	}
    
    
}
