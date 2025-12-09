package relatorio.negocio.comuns.financeiro;

import java.util.Date;


public class ExtratoContaCorrenteCartaoRelVO {
    
    private String operadoraCartao;
    private Date dataVencimento;
    private Double valor;
    
    public String getOperadoraCartao() {
        if(operadoraCartao ==null){
            operadoraCartao = "";
        }
        return operadoraCartao;
    }
    
    public void setOperadoraCartao(String operadoraCartao) {
        this.operadoraCartao = operadoraCartao;
    }
    
    public Date getDataVencimento() {
        
        return dataVencimento;
    }
    
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    
    public Double getValor() {
        if(valor ==null){
            valor = 0.0;
        }
        return valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }

    
    
}
