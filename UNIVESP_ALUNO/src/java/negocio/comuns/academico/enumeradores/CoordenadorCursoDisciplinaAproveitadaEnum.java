package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.UteisJSF;

public enum CoordenadorCursoDisciplinaAproveitadaEnum {
	
	APENAS_APROVEITAMENTO_SEM_PROFESSOR		("AAP", 			"Apenas Aproveitamentos Sem Professor"),
	TODOS_APROVEITAMENTO					("TA", 				"Todos Aproveitamentos"),
	NENHUM									(Constantes.EMPTY, 	Constantes.EMPTY);

	String valor;
	String descricao;

	CoordenadorCursoDisciplinaAproveitadaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static CoordenadorCursoDisciplinaAproveitadaEnum getEnum(String valor) {
		CoordenadorCursoDisciplinaAproveitadaEnum[] valores = values();
		for (CoordenadorCursoDisciplinaAproveitadaEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		CoordenadorCursoDisciplinaAproveitadaEnum obj = getEnum(valor);
		if (obj != null) {
			return UteisJSF.internacionalizar(obj.getDescricao());
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
		return UteisJSF.internacionalizar(descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
