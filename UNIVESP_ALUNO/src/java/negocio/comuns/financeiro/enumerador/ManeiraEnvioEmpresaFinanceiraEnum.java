package negocio.comuns.financeiro.enumerador;

public enum ManeiraEnvioEmpresaFinanceiraEnum {
    MANUALMENTE,
    VENCIDAS_AUTOMATICAMENTE,
    TODAS_AUTOMATICAMENTE;
    
    public Boolean isManual() {
        return equals(MANUALMENTE);
    }

    public Boolean isVencidasAutomaticamente() {
        return equals(VENCIDAS_AUTOMATICAMENTE);
    }

    public Boolean isTodasAutomaticamente() {
        return equals(TODAS_AUTOMATICAMENTE);
    }
}