package controle.arquitetura;

import jakarta.faces. component.UIComponent;
import jakarta.faces. context.FacesContext;
import jakarta.faces. convert.Converter;

/**
 *
 * @author Alessandro
 */
public class EnumConverter extends EnumControle implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return internacionalizarEnum((Enum) value);
    }
}
