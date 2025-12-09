package relatorio.negocio.comuns.financeiro;

import java.util.Date;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

public class FluxoCaixaRelVO {

    private Double valorMovimentacao;
    private Date dataMovimentacao;
    private String tipoMovimentacao;
    private String tipoOrigem;
    private String nomeResponsavel;
    private String formaPagamento;
    private Date dataAbertura;
    private Date dataFechamento;
    private String contaCaixa;
    private Integer unidadeEnsino;
    private String nomeResponsavelAbertura;
    private String responsavelAbertura;
    private Double valorInicial;
    private Double valorFinal;
    private String nomeUnidadeEnsino;
    private String numeroConta;
    private String nomeFavorecido;
    private String tipoOrigemContaReceber;
    private String tipoPagamento;
    private String descricao;
    private Double valorRecebidoBoletoBancario;
    private String identificadorTurma;
    private String banco;
    private String numeroCheque;
    private Double saldoFinalCaixaCheque;
    private Double saldoFinalCaixaDinheiro;

    public Double getValorMovimentacao() {
        return valorMovimentacao;
    }

    public void setValorMovimentacao(Double valorMovimentacao) {
        this.valorMovimentacao = valorMovimentacao;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public String getTipoOrigem() {
        return tipoOrigem;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(String contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public String getNomeResponsavelAbertura() {
        return nomeResponsavelAbertura;
    }

    public void setNomeResponsavelAbertura(String nomeResponsavelAbertura) {
        this.nomeResponsavelAbertura = nomeResponsavelAbertura;
    }

    public Double getValorInicial() {
    	if(valorInicial == null){
    		valorInicial = 0.0;
    	}
        return valorInicial;
    }

    public void setValorInicial(Double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public Double getValorFinal() {
    	if(valorFinal == null){
    		valorFinal = 0.0;
    	}
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getNomeUnidadeEnsino() {
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public void setResponsavelAbertura(String responsavelAbertura) {
        this.responsavelAbertura = responsavelAbertura;
    }

    public String getResponsavelAbertura() {
    	if (responsavelAbertura == null) {
    		responsavelAbertura = "";
    	}
        return responsavelAbertura;
    }

    public String getNomeFavorecido() {
        return nomeFavorecido;
    }

    public void setNomeFavorecido(String nomeFavorecido) {
        this.nomeFavorecido = nomeFavorecido;
    }

    public String getTipoOrigemContaReceber() {
        return tipoOrigemContaReceber;
    }

    public void setTipoOrigemContaReceber(String tipoOrigemContaReceber) {
        this.tipoOrigemContaReceber = tipoOrigemContaReceber;
    }

    public String getTipoOrigemContaReceber_Apresentar() {
        return TipoOrigemContaReceber.getDescricao(getTipoOrigemContaReceber());
    }

    
    public String getTipoPagamento() {
        return tipoPagamento;
    }

    
    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorRecebidoBoletoBancario() {
        return valorRecebidoBoletoBancario;
    }

    public void setValorRecebidoBoletoBancario(Double valorRecebidoBoletoBancario) {
        this.valorRecebidoBoletoBancario = valorRecebidoBoletoBancario;
    }

    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }

    public String getBanco() {
        if (banco == null) {
            banco = "";
        }
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumeroCheque() {
        if (numeroCheque == null) {
            numeroCheque = "";
        }
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

	public Double getSaldoFinalCaixaCheque() {
		if (saldoFinalCaixaCheque == null) {
			saldoFinalCaixaCheque = 0.0;
		}
		return saldoFinalCaixaCheque;
	}

	public void setSaldoFinalCaixaCheque(Double saldoFinalCaixaCheque) {
		this.saldoFinalCaixaCheque = saldoFinalCaixaCheque;
	}

	public Double getSaldoFinalCaixaDinheiro() {
		if (saldoFinalCaixaDinheiro == null) {
			saldoFinalCaixaDinheiro = 0.0;
		}
		return saldoFinalCaixaDinheiro;
	}

	public void setSaldoFinalCaixaDinheiro(Double saldoFinalCaixaDinheiro) {
		this.saldoFinalCaixaDinheiro = saldoFinalCaixaDinheiro;
	}

}