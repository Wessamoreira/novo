/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Diego
 */
public enum OrigemContaPagar {

    COMPRA("CO", "Compra", Boolean.FALSE),
    ADIANTAMENTO("AD", "Adiantamento", Boolean.TRUE),
    RECEBIMENTO_COMPRA("RC", "Recebimento Compra", Boolean.FALSE),
    NOTA_FISCAL_ENTRADA("NE", "Nota Fiscal Entrada", Boolean.FALSE),
    SERVICO("SE", "Serviço", Boolean.FALSE),
    MULTA("MU", "Multa", Boolean.FALSE),
    REQUISICAO("RE", "Requisição", Boolean.FALSE),
    CONTRATO_DESPESA("CD", "Contrato Despesa", Boolean.FALSE),
    PROVISAO_DE_CUSTO("PC", "Provisão de Custo", Boolean.FALSE),
    RESTITUICAO_CONVENIO_MATRICULA_PERIODO("RS", "Restituição Convênio", Boolean.FALSE),
    PROCESSAMENTO_ARQUIVO_RETORNO_PARCEIRO("PA", "Processamento Arquivo retorno Convênio", Boolean.FALSE), 
    CONCILIACAO_BANCARIA("CB", "Conciliação Bancaria", Boolean.FALSE),
    RESTITUICAO_VALOR_MATRICULA_PERIODO("RV", "Restituição Valor Aluno", Boolean.FALSE),
    REGISTRO_MANUAL("CP", "Registro Manual", Boolean.TRUE),
	TAXA_CARTAO("TC", "Taxa Cartão Crédito/Débito", Boolean.FALSE),
    NEGOCICACAO_CONTA_PAGAR("NC", "Negociação Conta Pagar", Boolean.FALSE),
    FOLHA_PAGAMENTO("FP", "Folha de Pagamento", Boolean.FALSE);
    
    String valor;
    String descricao;
    Boolean permiteRegistroManual;

    OrigemContaPagar(String valor, String descricao, boolean permiteRegistroManual) {
        this.valor = valor;
        this.descricao = descricao;
        this.permiteRegistroManual = permiteRegistroManual;
    }

    public static OrigemContaPagar getEnum(String valor) {
        OrigemContaPagar[] valores = values();
        for (OrigemContaPagar obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        OrigemContaPagar obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public void setPermiteRegistroManual(Boolean permiteRegistroManual) {
    	this.permiteRegistroManual = permiteRegistroManual;
    }

    public Boolean getPermiteRegistroManual() {
    	if (permiteRegistroManual == null) { 
    		permiteRegistroManual = Boolean.FALSE;
    	}
    	return permiteRegistroManual;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public boolean isNotaFiscalEntrada(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.NOTA_FISCAL_ENTRADA.name());
    }
    
    public boolean isRecebimentoCompra(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.RECEBIMENTO_COMPRA.name());
    }
    
    public boolean isCompra(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.COMPRA.name());
    }
    
    public boolean isConciliacaoBancaria(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.CONCILIACAO_BANCARIA.name());
    }
    
    public boolean isContratoDespesa(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.CONTRATO_DESPESA.name());
    }
    
    public boolean isMulta(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.MULTA.name());
    }
    
    public boolean isAdiantamento(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.ADIANTAMENTO.name());
    }
    
    public boolean isProcessamentoArquivoRetornoParceiro(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.PROCESSAMENTO_ARQUIVO_RETORNO_PARCEIRO.name());
    }
    
    public boolean isProvisaoCusto(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.PROVISAO_DE_CUSTO.name());
    }
    
    public boolean isRegistroManual(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.REGISTRO_MANUAL.name());
    }
    
    public boolean isRequisicao(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.REQUISICAO.name());
    }
    
    public boolean isREstituicaoConvenioMatriculaPeriodo(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO.name());
    }
    
    public boolean isRestituicaoValorMatriculaPeriodo(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.RESTITUICAO_VALOR_MATRICULA_PERIODO.name());
    }
    
    public boolean isServico(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemContaPagar.SERVICO.name());
    }
    
}
