package webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import negocio.comuns.utilitarias.Uteis;

public class DateAdapterMobile extends XmlAdapter<String, Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    List<SimpleDateFormat> lista = new ArrayList<SimpleDateFormat>();
    @Override
    public String marshal(Date v) throws Exception {
        synchronized (dateFormat) {
        	if(v == null) {
        		return "";
        	}
            return dateFormat.format(v);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
    	
        synchronized (dateFormat2) {
        	Date data = null ;
        	if(!Uteis.isAtributoPreenchido(v)) {
        		return null;
        	}        	
            if(v.contains("T") || v.contains("Z")) {
            	if(Uteis.isAtributoPreenchido(dateFormat2.parse(v)) ) {
        			data = dateFormat2.parse(v);
        		}
        	 }else if(Uteis.isAtributoPreenchido(dateFormat.parse(v)) ) {
     			data = dateFormat.parse(v);
     		}        	
            return data; 
        }
    }

}
