package negocio.comuns.utilitarias.formula;

public class FormulaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FormulaException(String message) {
		super(message);
	}

	public FormulaException(Throwable throwable) {
		super(throwable);
	}

	public FormulaException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
