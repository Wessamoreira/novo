package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoControleLiberarAcessoProximaDisciplinaEnum {
	APROVADO_AVALIACAO_ONLINE, PONTUACAO_ALCANCADA_ESTUDOS, 
//	APROVADO_PROVA_PRESENCIAL, 
	CONTEUDO_LIDO, APOS_APROVACAO_REPROVACAO_DISCIPLINA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoControleLiberarAcessoProximaDisciplinaEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isAprovadoAvaliacaoOnline(){
		return this.name().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_AVALIACAO_ONLINE.name());
	}
	
	public boolean isPontuacaoAlcancadaEstudo(){
		return this.name().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.PONTUACAO_ALCANCADA_ESTUDOS.name());
	}
	
//	public boolean isAprovadoProvaPresencial(){
//		return this.name().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_PROVA_PRESENCIAL.name());
//	}
	
	public boolean isConteudoLido(){
		return this.name().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.CONTEUDO_LIDO.name());
	}
	
	public boolean isAposAprovacaoReprovacaoDisciplina(){
		return this.name().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APOS_APROVACAO_REPROVACAO_DISCIPLINA.name());
	}
}
