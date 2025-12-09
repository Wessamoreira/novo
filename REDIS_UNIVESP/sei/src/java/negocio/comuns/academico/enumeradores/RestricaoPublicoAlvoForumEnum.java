package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
*
* @author Pedro
*/
public enum RestricaoPublicoAlvoForumEnum {
	NENHUM,
	PROFESSORES_AREA_CONHECIMENTO,
	PROFESSORES_CURSO,
	PROFESSORES_DISCIPLINA,
	PROFESSORES_ESPECIFICOS,
	TODOS_PROFESSORES;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RestricaoPublicoAlvoForumEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public static RestricaoPublicoAlvoForumEnum getEnum(String valor) {
		RestricaoPublicoAlvoForumEnum[] valores = values();
        for (RestricaoPublicoAlvoForumEnum obj : valores) {
            if (obj.getName().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
	
	public boolean isRestricaoEspecificos() {
		return getName() != null && getName().equals(RestricaoPublicoAlvoForumEnum.PROFESSORES_ESPECIFICOS.name()) ? true : false;
	}
	
	public boolean isRestricaoCurso() {
		return getName() != null && getName().equals(RestricaoPublicoAlvoForumEnum.PROFESSORES_CURSO.name()) ? true : false;
	}
	public boolean isRestricaoAreaConhecimento() {
		return getName() != null && getName().equals(RestricaoPublicoAlvoForumEnum.PROFESSORES_AREA_CONHECIMENTO.name()) ? true : false;
	}

}
