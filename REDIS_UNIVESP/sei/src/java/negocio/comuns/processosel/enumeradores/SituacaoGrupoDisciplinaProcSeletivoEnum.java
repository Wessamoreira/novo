package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoGrupoDisciplinaProcSeletivoEnum {

	ATIVA, INATIVA, EM_ELABORACAO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoGrupoDisciplinaProcSeletivoEnum_" + name());
	}

	public String getValor() {
		return name();
	}

}
