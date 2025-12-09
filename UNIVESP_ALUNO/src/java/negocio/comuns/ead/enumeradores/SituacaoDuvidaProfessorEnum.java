package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoDuvidaProfessorEnum {
	NOVA, AGUARDANDO_RESPOSTA_ALUNO, AGUARDANDO_RESPOSTA_PROFESSOR, FINALIZADA, TODAS, PENDENCIAS;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoDuvidaProfessorEnum_" + this.name());
	}

	public String getIconeReduzido() {
		switch (this) {
		case NOVA:
			return "fas fa-plus fs24 bg-success text-white p5 font-weight-bold border-radius-all";
		case AGUARDANDO_RESPOSTA_ALUNO:
			return "fas fa-user-graduate fs24  p5 font-weight-bold border-radius-all";
		case AGUARDANDO_RESPOSTA_PROFESSOR:
			return "fas fa-chalkboard-teacher fs26 p5 font-weight-bold border-radius-all";
		case FINALIZADA:
			return "fas fa-check fs24 p5 font-weight-bold border-radius-all";
		case TODAS:
			return "fas fa-users fs24 p5 font-weight-bold border-radius-all";
		case PENDENCIAS:
			return "fas fa-exclamation-triangle fs24 p5 font-weight-bold border-radius-all";

		default:
			return "/resources/imagens/ead/iconeReduzidoNovaDuvida.png";
		}
	}

	public String getIcone() {
		switch (this) {
		case NOVA:
			return "fas fa-plus fs30 bg-success text-white p6 font-weight-bold border-radius-all";
		case AGUARDANDO_RESPOSTA_ALUNO:
			return "fas fa-user-graduate fs30 bg-info text-white p6 font-weight-bold border-radius-all";
		case AGUARDANDO_RESPOSTA_PROFESSOR:
			return "fas fa-chalkboard-teacher fs30 bg-primary text-white p6 font-weight-bold border-radius-all";
		case FINALIZADA:
			return "fas fa-check fs30 bg-dark text-white p6 font-weight-bold border-radius-all";
		case TODAS:
			return "fas fa-users fs30 bg-warning text-white p6 font-weight-bold border-radius-all";
		case PENDENCIAS:
			return "fas fa-exclamation-triangle fs30 bg-danger text-white p6 font-weight-bold border-radius-all";

		default:
			return "/resources/imagens/ead/iconeNovaDuvida.png";
		}
	}

}
