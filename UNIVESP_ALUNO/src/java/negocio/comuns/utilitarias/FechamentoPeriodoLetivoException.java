package negocio.comuns.utilitarias;

import java.io.Serializable; public class FechamentoPeriodoLetivoException extends Exception implements Serializable{

	public static final long serialVersionUID = 1L;

	public FechamentoPeriodoLetivoException(String msgErro) {
		super(msgErro);
	}

	public FechamentoPeriodoLetivoException() {

	}

}
