package negocio.comuns.utilitarias.dominios;


/**
 * 
 * @author Diego
 */
public enum SituacaoEmprestimo {

	EMPRESTADO("EM", "Emprestado"), EMPRESTADO_COM_ATRASO("ECA", "Emprestado com atraso"), EMPRESTADO_SEM_ATRASO("ESA", "Emprestado sem atraso"),
	DEVOLVIDO("DE", "Devolvido"),  DEVOLVIDO_COM_ATRASO("DCA", "Devolvido com atraso"), DEVOLVIDO_SEM_ATRASO("DSA", "Devolvido sem atraso");

	String valor;
	String descricao;

	SituacaoEmprestimo(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoEmprestimo getEnum(String valor) {
		SituacaoEmprestimo[] valores = values();
		for (SituacaoEmprestimo obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		SituacaoEmprestimo obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
