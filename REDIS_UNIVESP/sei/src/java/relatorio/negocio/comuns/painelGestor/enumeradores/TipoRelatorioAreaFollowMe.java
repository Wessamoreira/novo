package relatorio.negocio.comuns.painelGestor.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoRelatorioAreaFollowMe {
    GASTO_POR_DEPARTAMENTO, 
    RECEITA_DESPESA,
    DESPESA,
    RECEITA_DESPESA_NIVEL_EDUCACIONAL,
    CATEGORIA_DESPESA_PADRAO_ALTERADO,
    CATEGORIA_DESPESA,
    POSICAO_FINANCEIRA,    
    ACADEMICO_FINANCEIRO,
    INADIMPLENCIA;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoRelatorioAreaFollowMe_"+this.name());
    }
    
}
