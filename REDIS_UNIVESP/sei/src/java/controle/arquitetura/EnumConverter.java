package controle.arquitetura;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

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
