/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.text.NumberFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Edigar
 */
@FacesConverter("controle.arquitetura.FormatadorNumerico")
public class FormatadorNumerico implements Converter {

    public FormatadorNumerico() {

    }

    public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
        try {
            final NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            
            return new Double(nf.parse(value).doubleValue());
        } catch (final Exception e) {
            return new Double(0.0);
        }
    }

    public String getAsString(final FacesContext context,
            final UIComponent component, Object value) {
        if ((value == null) || (value.toString().trim().equals(""))) {
            value = new Double(0.0);
        }
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        
        
        Integer quantidadeCasasDecimaisPermitirAposVirgula = 0;
        Integer quantidadeMinimaCasasDecimaisPermitirAposVirgula = 0;
		if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") != null) {
			if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") instanceof String) {
				quantidadeCasasDecimaisPermitirAposVirgula = Integer.valueOf((String) component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula"));
			} else if (component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula") instanceof Integer) {
				quantidadeCasasDecimaisPermitirAposVirgula = (Integer) component.getAttributes().get("quantidadeCasasDecimaisPermitirAposVirgula");
			}
		}
		
		if (component.getAttributes().get("quantidadeMinimaCasasDecimaisPermitirAposVirgula") != null) {
			if (component.getAttributes().get("quantidadeMinimaCasasDecimaisPermitirAposVirgula") instanceof String) {
				quantidadeMinimaCasasDecimaisPermitirAposVirgula = Integer.valueOf((String) component.getAttributes().get("quantidadeMinimaCasasDecimaisPermitirAposVirgula"));
			} else if (component.getAttributes().get("quantidadeMinimaCasasDecimaisPermitirAposVirgula") instanceof Integer) {
				quantidadeMinimaCasasDecimaisPermitirAposVirgula = (Integer) component.getAttributes().get("quantidadeMinimaCasasDecimaisPermitirAposVirgula");
			}
		}
		if (quantidadeCasasDecimaisPermitirAposVirgula != null && quantidadeCasasDecimaisPermitirAposVirgula > 0) {
			nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		} else {
			nf.setMaximumFractionDigits(2);
		}

		if (quantidadeMinimaCasasDecimaisPermitirAposVirgula != null && quantidadeMinimaCasasDecimaisPermitirAposVirgula > 0) {
			nf.setMinimumFractionDigits(quantidadeMinimaCasasDecimaisPermitirAposVirgula);
		} else {
			nf.setMinimumFractionDigits(2);
		}
        return nf.format(Double.valueOf(value.toString()));
    }
}

