package relatorio.negocio.comuns.contabil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.arquitetura.*;
import negocio.comuns.contabil.DREVO;
import negocio.comuns.contabil.PlanoContaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade ItemNotaFiscal. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see NotaFiscal
 */
public class BalancoVO extends SuperVO implements Serializable {

    protected PlanoContaVO conta;
    protected Double saldoAnterior;
    protected Double debito;
    protected Double credito;
    protected Double saldoAtual;
    protected List<BalancoVO> nivelSubordinado;
    protected List<DREVO> listaDRE;
    protected Double valorDRE; //Não Persiste no banco;


    /**
     * Construtor padrão da classe <code>ItemNotaFiscal</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public BalancoVO() {
        super();
    }

    public PlanoContaVO getConta() {
        if(conta == null){
            conta = new PlanoContaVO();
        }
        return conta;
    }

    public void setConta(PlanoContaVO conta) {
        this.conta = conta;
    }

    public Double getCredito() {
        if(credito == null){
            credito = 0.0;
        }
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
    }

    public Double getDebito() {
        if(debito == null){
            debito = 0.0;
        }
        return debito;
    }

    public void setDebito(Double debito) {
        this.debito = debito;
    }

    public List<BalancoVO> getNivelSubordinado() {
        if(nivelSubordinado == null){
            nivelSubordinado = new ArrayList<BalancoVO>(0);
        }
        return nivelSubordinado;
    }

    public JRDataSource getNivelSubordinadoJRDataSource() {
        JRDataSource jr = new JRBeanArrayDataSource(getNivelSubordinado().toArray());
        return jr;
    }

    public void setNivelSubordinado(List<BalancoVO> nivelSubordinado) {
        this.nivelSubordinado = nivelSubordinado;
    }


    public Double getSaldoAnterior() {
        if(saldoAnterior == null){
            saldoAnterior = 0.0;
        }
        return saldoAnterior;
    }

    public void setSaldoAnterior(Double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Double getSaldoAtual() {
        if(saldoAtual == null){
            saldoAtual = 0.0;
        }
        return saldoAtual;
    }

    public void setSaldoAtual(Double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public JRDataSource getListaDREJRDataSource() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaDRE().toArray());
        return jr;
    }

    public List<DREVO> getListaDRE() {
        if(listaDRE == null){
            listaDRE = new ArrayList<DREVO>(0);
        }
        return listaDRE;
    }

    public void setListaDRE(List<DREVO> listaDRE) {
        this.listaDRE = listaDRE;
    }

    public Double getValorDRE() {
        if(valorDRE == null){
            valorDRE = 0.0;
        }
        return valorDRE;
    }

    public void setValorDRE(Double valorDRE) {
        this.valorDRE = valorDRE;
    }


}