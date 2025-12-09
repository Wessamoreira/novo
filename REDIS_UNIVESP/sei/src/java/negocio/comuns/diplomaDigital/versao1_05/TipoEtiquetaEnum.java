package negocio.comuns.diplomaDigital.versao1_05;

import negocio.comuns.utilitarias.Constantes;

public enum TipoEtiquetaEnum {

	EAD("ead", "Ead"),
	TEORICA("teorica", "Teórica"),
	PRATICA("pratica", "Prática"),
	ESTAGIO("estagio", "Estágio"),
	EXTENSAO("extensao", "Extensão"),
	OPTATIVA("optativa", "Optativa"),
	OBRIGATORIA("obrigatoria", "Obrigatória"),
	MODALIDADE_ONLINE("modalidade-online", "Modalidade On-line"),
	LABORATORIAL_OPTATIVA("laboratorial-optativa", "Laboratorial Optativa"),
	MODALIDADE_PRESENCIAL("modalidade-presencial", "Modalidade Presencial"),
	LABORATORIAL_OBRIGATORIA("laboratorial-obrigatoria", "Laboratorial Obrigatória");

	private String valor;
	private String nome;

	private TipoEtiquetaEnum(String valor, String nome) {
		this.valor = valor;
		this.nome = nome;
	}

	public static TipoEtiquetaEnum getEnum(String valor) {
		TipoEtiquetaEnum[] valores = values();
		for (TipoEtiquetaEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public String getValor() {
		if (valor == null) {
			valor = Constantes.EMPTY;
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getNome() {
		if (nome == null) {
			nome = Constantes.EMPTY;
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
