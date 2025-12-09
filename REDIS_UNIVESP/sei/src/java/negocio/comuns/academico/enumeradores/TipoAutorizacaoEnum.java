package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.diplomaDigital.versao1_05.TTipoAto;

public enum TipoAutorizacaoEnum {

    PARECER(TTipoAto.PARECER),
    RESOLUCAO(TTipoAto.RESOLUÇÃO),
    DECRETO(TTipoAto.DECRETO),
    PORTARIA(TTipoAto.PORTARIA),
    LEI_FEDERAL(TTipoAto.LEI_FEDERAL),
    LEI_ESTADUAL(TTipoAto.LEI_ESTADUAL),
    LEI_MUNICIPAL(TTipoAto.LEI_MUNICIPAL),
    DESPACHO(TTipoAto.DESPACHO),
    DELIBERACAO(TTipoAto.DELIBERAÇÃO);

    private final TTipoAto tipoAtoDiploma;

    TipoAutorizacaoEnum(TTipoAto tipoAtoDiploma) {
        this.tipoAtoDiploma = tipoAtoDiploma;
    }

    public TTipoAto getTipoAtoDiploma() {
        return tipoAtoDiploma;
    }

    public String getValorApresentar() {
        return UteisJSF.internacionalizar("enum_TipoAutorizacaoCursoEnum_" + this.name());
    }

}
