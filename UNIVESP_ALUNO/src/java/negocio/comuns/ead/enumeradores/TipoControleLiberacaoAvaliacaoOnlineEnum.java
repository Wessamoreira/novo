package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoControleLiberacaoAvaliacaoOnlineEnum {

	PONTUACAO_ALCANCADA_ESTUDOS, CONTEUDO_LIDO, IMEDIATO_APOS_O_INICIO_DO_CURSO, NR_DIAS_APOS_O_INICIO_DO_CURSO, NAO_APLICAR_AVALIACAO_ONLINE;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoControleLiberacaoAvaliacaoOnlineEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isPontuacaoAlcancadaEstudo(){
		return this.name().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.PONTUACAO_ALCANCADA_ESTUDOS.name());
	}
	public boolean isConteudoLido(){
		return this.name().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.CONTEUDO_LIDO.name());
	}
	public boolean isImediatoAposInicioCurso(){
		return this.name().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.IMEDIATO_APOS_O_INICIO_DO_CURSO.name());
	}
	public boolean isNrDiasAposInicioCurso(){
		return this.name().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NR_DIAS_APOS_O_INICIO_DO_CURSO.name());
	}
	public boolean isNaoAplicarAvaliacaoOnline(){
		return this.name().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE.name());
	}
}
