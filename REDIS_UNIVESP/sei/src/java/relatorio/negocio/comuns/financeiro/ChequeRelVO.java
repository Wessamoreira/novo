package relatorio.negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class ChequeRelVO extends SuperVO implements Serializable {

    private String numero;
    private String agencia;
    private String numeroContaCorrente;
    private Double valor;
    private Date dataEmissao;
    private String dataPrevisao;
    private String situacao;
    private String matriculaAluno;
    private String nomeAluno;
    private String sacado;

    public ChequeRelVO() {
        super();
    }

    public String getNumero() {
        if (numero == null) {
            numero = "";
        }
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAgencia() {
        if (agencia == null) {
            agencia = "";
        }
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumeroContaCorrente() {
        if (numeroContaCorrente == null) {
            numeroContaCorrente = "";
        }
        return numeroContaCorrente;
    }

    public void setNumeroContaCorrente(String numeroContaCorrente) {
        this.numeroContaCorrente = numeroContaCorrente;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataPrevisao() {
        if (dataPrevisao == null) {
            dataPrevisao = "";
        }
        return dataPrevisao;
    }

    public void setDataPrevisao(String dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
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

    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "";
        }
        return matriculaAluno;
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getSacado() {
        if (sacado == null) {
            sacado = "";
        }
        return sacado;
    }

    public void setSacado(String sacado) {
        this.sacado = sacado;
    }

}
