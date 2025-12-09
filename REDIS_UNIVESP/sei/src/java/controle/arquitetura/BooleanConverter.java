package controle.arquitetura;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("BooleanConverter")
public class BooleanConverter implements Converter {

	private static final String SIM = "SIM";
	private static final String NAO = "NAO";
	private static final String TRUE = "TRUE";
	private static final String FALSE = "FALSE";
	private static final String ZERO = "0";
	private static final String UM = "1";
	

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		if (value.equalsIgnoreCase(SIM) || value.equalsIgnoreCase(TRUE) || value.equalsIgnoreCase(UM)) {
			return Boolean.TRUE;
		}
		if (value.equalsIgnoreCase(NAO) || value.equalsIgnoreCase(FALSE)  || value.equalsIgnoreCase(ZERO)) {
			return Boolean.FALSE;
		}
		throw new ConverterException(new FacesMessage("Valor Boolean Inválido, informar apenas as opções SIM, NAO, 1, 0, TRUE ou FALSE"));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value instanceof Boolean) {
			if((Boolean) value) {
				return (String)component.getAttributes().get("valorTrue");
			}else {
				return (String)component.getAttributes().get("valorFalse");
			}
		}
		return "";
	}
}