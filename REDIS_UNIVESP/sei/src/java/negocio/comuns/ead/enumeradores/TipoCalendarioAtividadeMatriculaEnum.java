package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo 02/10/2014
 */
public enum TipoCalendarioAtividadeMatriculaEnum {

	ACESSO_CONTEUDO_ESTUDO("../../resources/imagens/ead/calendarioatividadematricula/ACESSO_CONTEUDO_ESTUDO_ini.png", "ACESSO_CONTEUDO_ESTUDO.png", "color:blue;"), 
	ACESSO_CONTEUDO_CONSULTA("../resources/imagens/ead/calendarioatividadematricula/ACESSO_CONTEUDO_CONSULTA_ini.png", "ACESSO_CONTEUDO_ESTUDO_ini.png", "color:blue;"), 
	PERIODO_REALIZACAO_AVALIACAO_ONLINE("../resources/imagens/ead/calendarioatividadematricula/PERIODO_REALIZACAO_AVALIACAO_ONLINE_ini.png", "PERIODO_REALIZACAO_AVALIACAO_ONLINE.png", "color:blue;"),
	PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA("../resources/imagens/ead/calendarioatividadematricula/PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA_ini.png", "PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA.png", "color:blue;"),
	AGENDA_PROVA_PRESENCIAL("../resources/imagens/ead/calendarioatividadematricula/ACESSO_CONTEUDO_ESTUDO_ini.png", "ACESSO_CONTEUDO_ESTUDO_ini.png", "color:blue;"),
	DATA_PROVA_PRESENCIAL("../resources/imagens/ead/calendarioatividadematricula/ACESSO_CONTEUDO_ESTUDO_ini.png", "ACESSO_CONTEUDO_ESTUDO_ini.png", "color:blue;"),
	PERIODO_SOLICITACAO_2_CHAMADA("../resources/imagens/ead/calendarioatividadematricula/ACESSO_CONTEUDO_ESTUDO_ini.png", "ACESSO_CONTEUDO_ESTUDO_ini.png", "color:blue;"),
	DATA_PROVA_2_CHAMADA("../resources/imagens/ead/calendarioatividadematricula/ACESSO_CONTEUDO_ESTUDO_ini.png", "ACESSO_CONTEUDO_ESTUDO_ini.png", "color:blue;"),
	PERIODO_MAXIMO_CONCLUSAO_CURSO("../resources/imagens/ead/calendarioatividadematricula/PERIODO_MAXIMO_CONCLUSAO_CURSO_ini.png", "PERIODO_MAXIMO_CONCLUSAO_CURSO.png", "color:blue;"),
	PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA("../resources/imagens/ead/calendarioatividadematricula/PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA_ini.png", "PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA.png", "color:blue;"),
	PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS("../resources/imagens/ead/calendarioatividadematricula/PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS_ini.png", "PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS.png", "color:blue;");
	
	private String caminhoImagem;
	private String caminhoImagemGrande;
	private String cor;
	
	private TipoCalendarioAtividadeMatriculaEnum(String caminhoImagem, String caminhoImagemGrande, String cor) {
		this.caminhoImagem = caminhoImagem;
		this.caminhoImagemGrande = caminhoImagemGrande;
		this.cor = cor;
	}
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoCalendarioAtividadeMatriculaEnum_"+this.name());
	}

	public String getCaminhoImagem() {

		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

	public String getCaminhoImagemGrande() {
		return caminhoImagemGrande;
	}

	public void setCaminhoImagemGrande(String caminhoImagemGrande) {
		this.caminhoImagemGrande = caminhoImagemGrande;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isAcessoConteudoEstudo(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO.name());
	}
	
	public boolean isAcessoConteudoConsulta(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_CONSULTA.name());
	}
	
	public boolean isAgendarProvaPresencial(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.AGENDA_PROVA_PRESENCIAL.name());
	}
	
	public boolean isDataProva2Chamada(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.DATA_PROVA_2_CHAMADA.name());
	}
	
	public boolean isDataProvaChamada(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.DATA_PROVA_PRESENCIAL.name());
	}
	
	public boolean isPeriodoMaximoAtividadeDiscursiva(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA.name());
	}
	
	public boolean isPeriodoMaximoConclusaoCurso(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO.name());
	}
	
	public boolean isPeriodoMaximoConclusaoDisciplina(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS.name());
	}
	
	public boolean isPeriodoRealizacaoAvaliacaoOnline(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE.name());
	}
	
	public boolean isPeriodoRealizacaoAvaliacaoOnlineRea(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA.name());
	}
	
	public boolean isPeriodoSolicitacao2Chamada(){
		return this.name().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_SOLICITACAO_2_CHAMADA.name());
	}
}
