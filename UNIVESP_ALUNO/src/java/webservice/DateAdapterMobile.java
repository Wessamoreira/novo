package webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import negocio.comuns.utilitarias.Uteis;



public class DateAdapterMobile extends XmlAdapter<String, Date> {
    
    private static final String FORMAT_OUTPUT = "MM-dd-yyyy HH:mm:ss";
    private static final String FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    public DateAdapterMobile() {
        super();
    }
    
    
    private static final ThreadLocal<SimpleDateFormat> dateFormat = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_OUTPUT));
    
    private static final ThreadLocal<SimpleDateFormat> dateFormatISO = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_ISO));
    
    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return "";
        }
        return dateFormat.get().format(v);
    }
    
    @Override
    public Date unmarshal(String v) throws Exception {
        if (!Uteis.isAtributoPreenchido(v)) {
            return null;
        }
        
        try {
            
            if (v.contains("T") || v.contains("Z")) {
                return dateFormatISO.get().parse(v);
            }
            
            return dateFormat.get().parse(v);
            
        } catch (Exception e) {
            
            return null;
        }
    }
}