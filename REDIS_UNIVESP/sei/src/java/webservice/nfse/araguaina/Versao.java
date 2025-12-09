package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.enums.EnumToStringConverter;

@XStreamConverter(EnumToStringConverter.class)
public enum Versao {
	
	V2_00("2.0"), V2_01("2.01"), V2_02("2.02"), V1_02("1.02");
	
	private String value;

	Versao(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}