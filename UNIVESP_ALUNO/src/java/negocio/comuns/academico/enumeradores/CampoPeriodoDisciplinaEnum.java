package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.UteisJSF;

public enum CampoPeriodoDisciplinaEnum {

	NUMERO_PERIODO_LETIVO	("nrPeriodoLetivo", 	"Nº Periodo Letivo"),
	ANO_SEMESTRE_CURSADO	("anoSemestreCursado", 	"Ano/Semestre Cursado"),
	NENHUM					(Constantes.EMPTY, 		Constantes.EMPTY);

	String valor;
	String descricao;

	CampoPeriodoDisciplinaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static CampoPeriodoDisciplinaEnum getEnum(String valor) {
		CampoPeriodoDisciplinaEnum[] valores = values();
		for (CampoPeriodoDisciplinaEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		CampoPeriodoDisciplinaEnum obj = getEnum(valor);
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
