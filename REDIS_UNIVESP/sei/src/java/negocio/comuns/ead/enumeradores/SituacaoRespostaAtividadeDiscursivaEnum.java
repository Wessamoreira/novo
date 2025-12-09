package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoRespostaAtividadeDiscursivaEnum {

	AGUARDANDO_RESPOSTA, AGUARDANDO_AVALIACAO_PROFESSOR, AGUARDANDO_NOVA_RESPOSTA, AVALIADO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoRespostaAtividadeDiscursivaEnum_" + this.name());
	}
	
	public boolean isAguardandoAvaliacaoProfessor() {
		return name().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_AVALIACAO_PROFESSOR.name());
	}
	
	public boolean isAvaliado() {
		return name().equals(SituacaoRespostaAtividadeDiscursivaEnum.AVALIADO.name());
	}
	
	public boolean isResponder() {
		return equals(AGUARDANDO_RESPOSTA) || equals(AGUARDANDO_NOVA_RESPOSTA);
	}
}
