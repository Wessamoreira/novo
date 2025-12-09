package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum MotivoNaoProcessamentoRespostaProcessoSeletivoEnum {
    
    INSCRICAO_NAO_LOCALIZADA,
    GABARITO_NAO_LOCALIZADO,
    PROVA_NAO_LOCALIZADA;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_MotivoNaoProcessamentoRespostaProcessoSeletivoEnum_"+this.name());
    }

}
