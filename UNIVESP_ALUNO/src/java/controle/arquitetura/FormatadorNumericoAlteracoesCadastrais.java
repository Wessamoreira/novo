/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.text.NumberFormat;

import jakarta.faces. component.UIComponent;
import jakarta.faces. context.FacesContext;
import jakarta.faces. convert.Converter;

/**
 * 
 * @author Edigar
 */
public class FormatadorNumericoAlteracoesCadastrais implements Converter {

	public FormatadorNumericoAlteracoesCadastrais() {

	}

	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		// try {
		// final NumberFormat nf = NumberFormat.getInstance();
		// nf.setMinimumFractionDigits(2);
		// nf.setMaximumFractionDigits(2);
		//            
		// return new Double(nf.parse(value).doubleValue());
		// } catch (final Exception e) {
		// return new Double(0.0);
		// }
		try {
			if ((value == null) || (value.toString().trim().equals(""))) {
				return null;
			}
			final NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);

			return new Double(nf.parse(value).doubleValue());

		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
			return null;
		}

	}

	public String getAsString(final FacesContext context, final UIComponent component, Object value) {
		if ((value == null) || (value.equals(0.0)) || (value.toString().trim().equals(""))) {
			return null;
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		return nf.format(Double.valueOf(value.toString()));
	}
}
