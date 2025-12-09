package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
*
* @author Pedro
*/
public enum PublicoAlvoForumEnum {
	NENHUM, ALUNO, PROFESSOR;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_PublicoAlvoForumEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public static PublicoAlvoForumEnum getEnum(String valor) {
		PublicoAlvoForumEnum[] valores = values();
        for (PublicoAlvoForumEnum obj : valores) {
            if (obj.getName().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
	
	public boolean isPublicoAluno() {
		return getName().equals(PublicoAlvoForumEnum.ALUNO.name()) ? true : false;
	}
	
	public boolean isPublicoProfessor() {
		return getName() != null && getName().equals(PublicoAlvoForumEnum.PROFESSOR.name()) ? true : false;
	}

}
