package negocio.comuns.utilitarias;

import java.io.Serializable; 
public class AcessoException extends RuntimeException implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5179127719568644339L;

	public AcessoException(String msgErro) {
        super(msgErro);
    }
    
}
