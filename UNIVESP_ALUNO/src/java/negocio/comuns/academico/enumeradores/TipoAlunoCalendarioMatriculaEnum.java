package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoAlunoCalendarioMatriculaEnum {
	
	AMBOS("AMBOS"),
	CALOURO("CALOURO"), 
	VETERANO("VETERANO");
	
	String valor;
	
	TipoAlunoCalendarioMatriculaEnum(String string) {
	}

	public String getName() {
		return this.name();
	}
	
	public static String retornarValor(String valor) {
		return valor;
	}
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar(this.name());
	}
	
	public static TipoAlunoCalendarioMatriculaEnum getEnum(String valor) {
		TipoAlunoCalendarioMatriculaEnum[] valores = values();
		for (TipoAlunoCalendarioMatriculaEnum obj : valores) {
			if (obj.name().equals(valor)) {
				return obj;
			}
		}
		return null;
	}
	
	public static String getDescricao(String valor) {
		TipoAlunoCalendarioMatriculaEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getValorApresentar();
		}
		return valor;
	}
	
	public static Boolean getExisteValor(String valor) {
		for (TipoAlunoCalendarioMatriculaEnum tipoAlunoCalendarioMatriculaEnum : TipoAlunoCalendarioMatriculaEnum.values()) {
			if (tipoAlunoCalendarioMatriculaEnum.toString().equals(valor)) {
				return true;
			}
		}
		return false;
	}
}
