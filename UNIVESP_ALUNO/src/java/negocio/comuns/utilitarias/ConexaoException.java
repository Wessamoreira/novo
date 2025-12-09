package negocio.comuns.utilitarias;

import java.io.Serializable; public class ConexaoException extends Exception implements Serializable{
    
    public ConexaoException(String msg) {
        super(msg);
    }

    public ConexaoException(Exception e) {
        super(e);
    }

    
}
