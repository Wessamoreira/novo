package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoControleLiberacaoProvaPresencialEnum {
	APROVADO_AVALIACAO_ONLINE, PONTUACAO_ALCANCADA_ESTUDOS, CONTEUDO_LIDO, NR_DIAS_APOS_O_INICIO_DA_DISCIPLINA, NAO_CONTROLAR_PROVA_PRESENCIAL;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoControleLiberacaoProvaPresencialEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isAprovadoAvaliacaoOnline(){
		return this.name().equals(TipoControleLiberacaoProvaPresencialEnum.APROVADO_AVALIACAO_ONLINE.name());
	}
	public boolean isPontuacaoAlcancadaEstudo(){
		return this.name().equals(TipoControleLiberacaoProvaPresencialEnum.PONTUACAO_ALCANCADA_ESTUDOS.name());
	}
	public boolean isConteudoLido(){
		return this.name().equals(TipoControleLiberacaoProvaPresencialEnum.CONTEUDO_LIDO.name());
	}
	public boolean isNrDiasAposInicioDisciplina(){
		return this.name().equals(TipoControleLiberacaoProvaPresencialEnum.NR_DIAS_APOS_O_INICIO_DA_DISCIPLINA.name());
	}
	public boolean isNaoControlarProvaPresencial(){
		return this.name().equals(TipoControleLiberacaoProvaPresencialEnum.NAO_CONTROLAR_PROVA_PRESENCIAL.name());
	}
	
}
