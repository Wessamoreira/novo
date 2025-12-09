package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoPessoaInteracaoDuvidaProfessorEnum {

    PROFESSOR, ALUNO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoPessoaInteracaoDuvidaProfessorEnum_"+this.name());
    }
}
