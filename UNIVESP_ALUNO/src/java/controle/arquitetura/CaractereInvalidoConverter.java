package controle.arquitetura;

import java.util.Objects;

import jakarta.faces. component.UIComponent;
import jakarta.faces. context.FacesContext;
import jakarta.faces. convert.Converter;

import negocio.comuns.utilitarias.Constantes;

public class CaractereInvalidoConverter implements Converter {

	public CaractereInvalidoConverter() {
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		try {
			String string = value;
			StringBuilder texto = new StringBuilder();
			for (char caracter : string.toCharArray()) {
				if (Objects.isNull(caracter)) {
					texto.append(caracter);
					continue;
				}
				if (Objects.equals(String.valueOf(caracter),  "")) {
					texto.append(" ");
				} else {
					texto.append(caracter);
				}
			}
			return texto.toString();
		} catch (final Exception e) {
			return Constantes.EMPTY;
		}
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, Object value) {
		try {
			String string = (String) value;
			if (!(value instanceof String)) {
				return (String) value;
			}
			StringBuilder texto = new StringBuilder();
			for (char caracter : string.toCharArray()) {
				if (Objects.isNull(caracter)) {
					texto.append(caracter);
					continue;
				}
				if (Objects.equals(String.valueOf(caracter),  "")) {
					texto.append(" ");
				} else {
					texto.append(caracter);
				}
			}
			return texto.toString();
		} catch (final Exception e) {
			return Constantes.EMPTY;
		}
	}

}
