package controle.arquitetura;

import java.text.NumberFormat;

import jakarta.faces. component.UIComponent;
import jakarta.faces. context.FacesContext;
import jakarta.faces. convert.Converter;

public class FormatadorNumericoPermitindoValoresNulos implements Converter {

	public FormatadorNumericoPermitindoValoresNulos() {
	}

	public Object getAsObject(final FacesContext context, final UIComponent component, String value) {
		try {
			if ((value == null) || (value.toString().trim().equals(""))) {				
				return null;
			}			
			final NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(0);
			Integer quantidadeCasasDecimaisPermitirAposVirgula = 0;
			if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") != null) {
				if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") instanceof String) {
					quantidadeCasasDecimaisPermitirAposVirgula = Integer.valueOf((String) component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula"));
				} else if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") instanceof Integer) {
					quantidadeCasasDecimaisPermitirAposVirgula = (Integer) component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula");
				}
			}
			if (quantidadeCasasDecimaisPermitirAposVirgula != null) {
				nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
			} else {
				nf.setMaximumFractionDigits(1);
			}
			return new Double(nf.parse(value).doubleValue());

		} catch (Exception e) {
			return null;
		}

	}

	public String getAsString(final FacesContext context, final UIComponent component, Object value) {
		if ((value == null) || (value.toString().trim().equals(""))) {
			return null;
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		Integer quantidadeCasasDecimaisPermitirAposVirgula = 0;
		if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") != null) {
			if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") instanceof String) {
				quantidadeCasasDecimaisPermitirAposVirgula = Integer.valueOf((String) component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula"));
			} else if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") instanceof Integer) {
				quantidadeCasasDecimaisPermitirAposVirgula = (Integer) component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula");
			}
		}
		if (quantidadeCasasDecimaisPermitirAposVirgula != null) {
			nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		} else {
			nf.setMaximumFractionDigits(1);
		}
		return nf.format(Double.valueOf(value.toString()));
	}

}
