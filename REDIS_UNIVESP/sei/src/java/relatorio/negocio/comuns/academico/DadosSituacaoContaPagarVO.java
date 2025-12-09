package relatorio.negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;

public class DadosSituacaoContaPagarVO {

    private Date dataVencimento;
    private String situacao;
    private String parcela;
    private String nrDocumento;
    private Double valor;
    private Double desconto;
    private Double valorPago;
    private Double valorPagamento;
    private Double juro;
    private Double multa;
    private Date dataPagamento;


    public DadosSituacaoContaPagarVO() {

    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    public String getSituacao_Apresentar() {
        return SituacaoFinanceira.getDescricao(situacao);
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
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

    public Double getDesconto() {
        if (desconto == null) {
            desconto = 0.0;
        }
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
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

    public Double getValorPagamento() {
        if (valorPagamento == null) {
            valorPagamento = 0.0;
        }
        return valorPagamento;
    }

    public void setValorPagamento(Double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public String getDataPagamento_Apresentar() {
        return (Uteis.getData(dataPagamento));
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Double getJuro() {
        if (juro == null) {
            juro = 0.0;
        }
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getMulta() {
        if (multa == null) {
            multa = 0.0;
        }
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }
}
