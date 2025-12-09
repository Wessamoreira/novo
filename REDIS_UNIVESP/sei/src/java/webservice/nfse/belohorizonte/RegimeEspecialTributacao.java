package webservice.nfse.belohorizonte;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.enums.EnumToStringConverter;

@XStreamConverter(value = EnumToStringConverter.class)
public enum RegimeEspecialTributacao {
	
	MICROEMPRESA_MUNICIPAL(1), ESTIMATIVA(2), SOCIEDADE_DE_PROFISSIONAIS(3), COOPERATIVA(4), MEI_SIMPLES_NACIONAL(5), ME_EPP_SIMPLES_NACIONAL(6);
	
	private int value;

	RegimeEspecialTributacao(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
