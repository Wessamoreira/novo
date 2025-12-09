package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.diplomaDigital.versao1_05.TTipoAto;
import negocio.comuns.diplomaDigital.versao1_05.TTipoAtoComAtoProprio;

public enum TipoAutorizacaoCursoEnum {

    PARECER(TTipoAtoComAtoProprio.PARECER),
    RESOLUCAO(TTipoAtoComAtoProprio.RESOLUÇÃO),
    DECRETO(TTipoAtoComAtoProprio.DECRETO),
    PORTARIA(TTipoAtoComAtoProprio.PORTARIA),
    LEI_FEDERAL(TTipoAtoComAtoProprio.LEI_FEDERAL),
    LEI_ESTADUAL(TTipoAtoComAtoProprio.LEI_ESTADUAL),
    LEI_MUNICIPAL(TTipoAtoComAtoProprio.LEI_MUNICIPAL),
    ATO_PROPRIO(TTipoAtoComAtoProprio.ATO_PRÓPRIO),
    DELIBERACAO(TTipoAtoComAtoProprio.DELIBERAÇÃO);

    private final TTipoAtoComAtoProprio tipoAtoDiploma;

    TipoAutorizacaoCursoEnum(TTipoAtoComAtoProprio tipoAtoDiploma) {
        this.tipoAtoDiploma = tipoAtoDiploma;
    }

    public TTipoAtoComAtoProprio getTipoAtoDiploma() {
        return tipoAtoDiploma;
    }

    public String getValorApresentar() {
        return UteisJSF.internacionalizar("enum_TipoAutorizacaoCursoEnum_" + this.name());
    }

}
