package controle.arquitetura;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import jakarta.faces. application.FacesMessage;
import jakarta.faces. component.UIComponent;
import jakarta.faces. context.FacesContext;
import jakarta.faces. convert.Converter;
import jakarta.faces. convert.ConverterException;
import jakarta.faces. convert.FacesConverter;

import org.apache.commons.lang3.math.NumberUtils;

@FacesConverter("bigDecimalConverter")
public class BigDecimalConverter implements Converter {

	private static final BigDecimal UPPER_LIMIT = new BigDecimal("999999999999");
	private static final BigDecimal LOWER_LIMIT = new BigDecimal("-999999999999");

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		value = value.replace(".","");
		value = value.replace(",", ".");
		value = value.trim();
		if (!NumberUtils.isNumber(value)) {
			throw new ConverterException(new FacesMessage("not a number"));
		}
		if (value.contains(".")) {
			String decimalPlace = value.substring(value.indexOf("."));
			if (decimalPlace.length() > 3) { // 3 as decimal point is included in the String
				throw new ConverterException(new FacesMessage("Muitos numeros depois da casa decimal"));
			}
		}
		BigDecimal convertedValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
		if (convertedValue.compareTo(UPPER_LIMIT) > 0) {
			throw new ConverterException(new FacesMessage("Valor não pode ser maior que " + UPPER_LIMIT));
		}
		if (convertedValue.compareTo(LOWER_LIMIT) < 0) {
			throw new ConverterException(new FacesMessage("Valor não pode ser menor que " + LOWER_LIMIT));
		}
		return convertedValue;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		BigDecimal bd = ((BigDecimal) value);
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return nf.format(bd).replace("R$ ", "");
	}
}