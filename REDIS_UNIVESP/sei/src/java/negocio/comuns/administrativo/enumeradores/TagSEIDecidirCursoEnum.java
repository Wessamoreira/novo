package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirCursoEnum implements PerfilTagSEIDecidirEnum {
	CURSO_CODIGO("curso", "Código Curso", "curso.codigo", TipoCampoEnum.INTEIRO, 20), 
	CURSO_NOME("curso", "Nome Curso", "curso.nome", TipoCampoEnum.TEXTO, 40), 
	CURSO_PERIODICIDADE_DESCRICAO("curso", "Periodicidade Curso - Descrição", "case curso.periodicidade when 'IN' then 'Integral' when 'AN' then 'Anual' else 'Semestral' end ", TipoCampoEnum.TEXTO, 20), 
	CURSO_PERIODICIDADE_SIGLA("curso", "Periodicidade Curso - Sigla", "curso.periodicidade", TipoCampoEnum.TEXTO, 10), 
	CURSO_NR_REGISTRO_INTERNO("curso", "Número do Registro Interno", "curso.nrRegistroInterno", TipoCampoEnum.TEXTO, 20), 
	CURSO_NIVEL_EDUCACIONAL_SIGLA("curso", "Nível Educacional - Sigla", "curso.nivelEducacional", TipoCampoEnum.TEXTO, 10), 
	CURSO_NIVEL_EDUCACIONAL_DESCRICAO("curso", "Nível Educacional - Descrição", "case curso.nivelEducacional when 'IN' then 'Educação Infantil'		when 'BA' then 'Ensino Fundamental'		when 'ME' then 'Ensino Médio'		when 'EX' then 'Extensão'		when 'SE' then 'Sequencial'		when 'GT' then 'Graduação Tecnológica'		when 'SU' then 'Graduação'		when 'PO' then 'Pós-graduação' when 'PR' then 'Técnico/Profissionalizante' when 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' else ''	end ", TipoCampoEnum.TEXTO, 30), 
	CURSO_EIXO_CURSO("eixocurso", "Eixo Curso", "eixocurso.nome", TipoCampoEnum.TEXTO, 30),
	;
	
	private TagSEIDecidirCursoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
		this.entidade = entidade;
		this.campo = campo;
		this.atributo = atributo;
		this.tipoCampo = tipoCampo;
	}

	private String entidade;
	private String campo;
	private String atributo;
	private TipoCampoEnum tipoCampo;
	private Integer tamanhoCampo;
	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getCampo() {	
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public TipoCampoEnum getTipoCampo() {		
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	@Override
	public String getTag() {	
		return this.name();
	}

	public String getAtributo() {
		if (atributo == null) {
			atributo = "";
		}
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	
	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#getTamanhoCampo()
	 */
	@Override
	public Integer getTamanhoCampo() {

		return tamanhoCampo;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#setTamanhoCampo(java.lang.Integer)
	 */
	@Override
	public void setTamanhoCampo(Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;		
	}
	
}
