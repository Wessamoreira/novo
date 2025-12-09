package negocio.comuns.arquitetura;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;

import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;

public class ExcluirJsonStrategy implements ExclusionStrategy {
	
	    // This method is called for all fields. if the method returns false the
	    // field is excluded from serialization
		@Override
	    public boolean shouldSkipField(FieldAttributes f) {			
	        return (f.getAnnotation(ExcluirJsonAnnotation.class) == null) && f.getAnnotation(Expose.class) == null ? false : true;
	    }
	    
		@Override
		public boolean shouldSkipClass(Class<?> arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	 
	    

}
