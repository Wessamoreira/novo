package negocio.comuns.crm.enumerador;

/**
 * Reponsável por manter os dados da entidade SexoEnum. Classe do tipo ENUM 
 * composta pelos atributos constantes da entidade.
 * Classe utilizada para apresentar valores fixos nos combobox das telas de consultas e/ou formularios.
*/

public enum SexoEnum {
	

    NENHUM,F,M;
	
	public static Boolean getExisteValor(String valor) {
		for (SexoEnum sexoEnum : SexoEnum.values()) {
			if (sexoEnum.toString().equals(valor)) {
				return true;
			}
		}
		return false;
	}
}