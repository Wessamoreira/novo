package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum OrdemEstudoDisciplinasOnlineEnum {
	SEQUENCIADAS, SIMULTANEAS;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_OrdemEstudoDisciplinasOnlineEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isSequenciadas(){
		return this.name().equals(OrdemEstudoDisciplinasOnlineEnum.SEQUENCIADAS.name());
	}
	public boolean isSimultaneas(){
		return this.name().equals(OrdemEstudoDisciplinasOnlineEnum.SIMULTANEAS.name());
	}

}
