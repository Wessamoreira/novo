/**
 * 
 */
package negocio.comuns.secretaria.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum SituacaoMatriculaProvaPresencialDisciplinaEnum {
	TODAS_DISCIPLINAS,
	DISCIPLINA_LOCALIZADA,
	DISCIPLINA_NAO_LOCALIZADA,
	DISCIPLINA_OUTRA_CONFIGURACAO_ACADEMICA;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoMatriculaProvaPresencialDisciplinaEnum_"+this.name());
    }

}
