package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum DefinicoesTutoriaOnlineEnum {

	 PROGRAMACAO_DE_AULA, DINAMICA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_DefinicoesTutoriaOnlineEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isProgramacaoAula() {
		return name().equals(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.name());
	}

	public boolean isDinamica() {
		return name().equals(DefinicoesTutoriaOnlineEnum.DINAMICA.name());
	}
	
	/*public boolean isMenhum() {
		return name().equals(DefinicoesTutoriaOnlineEnum.NENHUM.name());
	}*/
}
