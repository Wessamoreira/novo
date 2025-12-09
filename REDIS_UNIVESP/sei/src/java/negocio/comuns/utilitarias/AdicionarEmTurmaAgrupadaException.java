package negocio.comuns.utilitarias;

import java.io.Serializable; public class AdicionarEmTurmaAgrupadaException extends Exception implements Serializable{
    
    public AdicionarEmTurmaAgrupadaException(String msgErro) {
        super(msgErro);
    }
    
}
