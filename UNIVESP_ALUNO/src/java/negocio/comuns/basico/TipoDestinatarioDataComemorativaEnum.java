package negocio.comuns.basico;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoDestinatarioDataComemorativaEnum {

    TODOS,
    ALUNOS,
    FUNCIONARIOS, 
    PROFESSORES, 
    UNIDADE_ENSINO, 
    CARGO, 
    AREA_CONHECIMENTO, 
    AREA_PROFISSIONAL, 
    DEPARTAMENTO,
    COORDENADORES;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoDestinatarioDataComemorativaEnum_"+this.name());
    }
}
