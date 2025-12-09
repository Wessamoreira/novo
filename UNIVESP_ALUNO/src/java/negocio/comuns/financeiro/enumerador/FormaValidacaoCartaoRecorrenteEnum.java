package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormaValidacaoCartaoRecorrenteEnum {
    AUTORIZACAO, ZERO_DOLLAR;
    
    public boolean isAutorizacao() {
        return equals(AUTORIZACAO);
    }

    public boolean isZeroDollar() {
        return equals(ZERO_DOLLAR);
    }

    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_FormaValidacaoCartaoRecorrenteEnum_"+this.name());
    }
}
