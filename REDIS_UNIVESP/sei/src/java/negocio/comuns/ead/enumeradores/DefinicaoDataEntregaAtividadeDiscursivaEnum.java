package negocio.comuns.ead.enumeradores;

import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.utilitarias.UteisJSF;

public enum DefinicaoDataEntregaAtividadeDiscursivaEnum {

	INICIO_ESTUDO_ONLINE, DATA_FIXA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_DefinicaoDataEntregaAtividadeDiscursivaEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	
	public boolean isInicioEstudoOnline() {
		return name().equals(DefinicaoDataEntregaAtividadeDiscursivaEnum.INICIO_ESTUDO_ONLINE.name());
	}

	public boolean isDataFixa() {
		return name().equals(DefinicaoDataEntregaAtividadeDiscursivaEnum.DATA_FIXA.name());
	}
	
}
