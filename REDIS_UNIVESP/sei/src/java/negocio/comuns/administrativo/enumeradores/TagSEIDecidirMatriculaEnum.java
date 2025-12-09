package negocio.comuns.administrativo.enumeradores;
/**
 * @author Leonardo Riciolle
 */
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirMatriculaEnum implements PerfilTagSEIDecidirEnum {
	
	MATRICULA_MATRICULA("matricula","Matrícula", "matricula.matricula",TipoCampoEnum.TEXTO, 30),	
	MATRICULA_NOTA_ENEM("matricula","Nota Enem", "matricula.notaEnem",TipoCampoEnum.DOUBLE, 20),	
	MATRICULA_SIGLA_SITUACAO("matricula", "Situação da Matrícula - Sigla", "matricula.situacao",TipoCampoEnum.TEXTO, 15),	
	MATRICULA_DESCRICAO_SITUACAO("matricula", "Situação da Matrícula - Descrição", "(case matricula.situacao when 'AC' then 'Abandono de Curso' "
			+ " when 'PR' then 'Pré-matrícula' when 'DE' then 'Desligado' when 'IN' then 'Inativa' "
			+ " when 'AT' then 'Ativa' when 'CA' then 'Cancelada' when 'CF' then 'Cancelada Financeiro' "
			+ " when 'JU' then 'Jubilado' when 'TS' then 'Transferida' when 'TR' then 'Trancada' "
			+ " when 'FI' then 'Finalizada' when 'PF' then 'Provável Formando' when 'FO' then 'Formado' "
			+ " when 'TI' then 'Transferida Internamente' else 'Problema ao Tentar Definir Situação - Problema Importação' end) ",TipoCampoEnum.TEXTO, 20),	
	MATRICULA_DATA("matricula", "Data Matrícula", "matricula.data",TipoCampoEnum.DATA, 20),
	MATRICULA_INSCRICAO("matricula", "Inscrição do Processo Seletivo", "matricula.inscricao",TipoCampoEnum.INTEIRO, 20),		
	MATRICULA_SIGLA_FORMAINGRESSO("matricula","Forma de Ingresso - Sigla", "matricula.formaingresso",TipoCampoEnum.TEXTO, 20),
	MATRICULA_DESCRICAO_FORMAINGRESSO("matricula","Forma de Ingresso - Descrição", "case matricula.formaingresso when 'ET' then 'Entrevista'"
			+ " when 'PD' then 'Portador de Diploma' when 'PD' then 'Portador de Diploma'  when 'TI' then 'Transferência Interna'"
			+ " when 'PS' then 'Processo Seletivo' when 'VE' then 'Vestibular' when 'TE' then 'Transferência Externa'"
			+ " when 'RE' then 'Reingresso' when 'PR' then 'Prouni' when 'EN' then 'Enem'"
			+ " else 'Outros Tipos de Seleção' end ", TipoCampoEnum.TEXTO, 30),
	MATRICULA_ANO_INGRESSO("matricula", "Ano Ingresso", "matricula.anoingresso",TipoCampoEnum.TEXTO, 10),
	MATRICULA_MES_INGRESSO("matricula", "Mês Ingresso", "matricula.mesingresso",TipoCampoEnum.TEXTO, 10),
	MATRICULA_SEMESTRE_INGRESSO("matricula","Semestre Ingresso", "matricula.semestreingresso",TipoCampoEnum.TEXTO, 10),
	MATRICULA_ANO_CONCLUSAO("matricula", "Ano Conclusão", "matricula.anoconclusao",TipoCampoEnum.TEXTO, 10),
	MATRICULA_SEMESTRE_CONCLUSAO("matricula","Semestre Conclusão", "matricula.semestreconclusao",TipoCampoEnum.TEXTO, 10),
	MATRICULA_PERIODO_DESCRICAO_PERIODO_LETIVO("periodoletivomatriculaperiodo","Periodo Letivo - Descrição", "periodoletivomatriculaperiodo.descricao",TipoCampoEnum.TEXTO, 20),
	MATRICULA_PERIODO_NUMERO_PERIODO_LETIVO("periodoletivomatriculaperiodo","Periodo Letivo - Número", "periodoletivomatriculaperiodo.periodoletivo",TipoCampoEnum.TEXTO, 10),
	MATRICULA_MATRIZ_CURRICULAR_NOME("gradecurricularatual", "Matriz Curricular", "gradecurricularatual.nome", TipoCampoEnum.TEXTO, 30),
	MATRICULA_MATRIZ_CURRICULAR_CH_TOTAL("gradecurricularatual", "Carga Horária Total Matriz Curricular", "gradecurricularatual.cargahoraria", TipoCampoEnum.INTEIRO, 10),
	MATRICULA_MATRIZ_CURRICULAR_CH_OBRIGATORIA("gradecurricularatual", "Carga Horária Obrigatória Matriz Curricular", "(select sum(gd.cargahoraria) from gradedisciplina as gd "+ 
	"inner join periodoletivo pl on pl.codigo = gd.periodoletivo "+
	"where pl.gradecurricular = matricula.gradecurricularatual "+
	"and gd.tipodisciplina not in ('OP', 'LO')) ", TipoCampoEnum.INTEIRO, 20),
	MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDA_OBRIGATORIA("gradecurricularatual", "C.H. Obrigatória Cumprida Matriz Curricular", "(select case when sum(coalesce(gd.cargahoraria,0)) is null then 0 else sum(coalesce(gd.cargahoraria,0)) end  from gradedisciplina as gd "+ 
			"inner join periodoletivo pl on pl.codigo = gd.periodoletivo "+
			"where pl.gradecurricular = matricula.gradecurricularatual "+
			"and gd.tipodisciplina not in ('OP', 'LO') and  exists ("+
			"select his.codigo from historico his where his.matricula = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual "+
			" and his.gradedisciplina = gd.codigo and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')))", TipoCampoEnum.INTEIRO, 10),
	MATRICULA_MATRIZ_CURRICULAR_CH_OPTATIVA("gradecurricularatual", "C.H. Optativa Matriz Curricular", " (gradecurricularatual.cargahoraria - "+
			"(select sum(gd.cargahoraria) from gradedisciplina as gd "+ 
			" inner join periodoletivo pl on pl.codigo = gd.periodoletivo "+
			" where pl.gradecurricular = matricula.gradecurricularatual "+
			" and gd.tipodisciplina not in ('OP', 'LO')) - "+
			" case when gradecurricularatual.totalcargahorariaestagio is null then 0 else gradecurricularatual.totalcargahorariaestagio end - "+
			" case when totalcargahorariaatividadecomplementar is null then 0 else totalcargahorariaatividadecomplementar end ) "
			, TipoCampoEnum.INTEIRO, 10),
	MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDA_OPTATIVA("gradecurricularatual", "C.H. Optativa Cumprida Matriz Curricular", " (select case when sum(coalesce(cargahorariadisciplina,0)) is null then 0 else sum(coalesce(cargahorariadisciplina,0)) end  from ( "+
	" select his.cargahorariadisciplina from historico his "+  
	" inner join gradedisciplina gd on gd.codigo = his.gradedisciplina"+
	" inner join periodoletivo pl on pl.codigo = gd.periodoletivo"+
	" where his.matricula  = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual "+
	" and pl.gradecurricular = matricula.gradecurricularatual "+
	" and gd.tipodisciplina in ('OP', 'LO')	and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')"+
	" union all"+
	" select his.cargahorariadisciplina from historico his "  +
	" inner join gradecurriculargrupooptativadisciplina gcgod on gcgod.codigo = his.gradecurriculargrupooptativadisciplina"+				
	" inner join gradecurriculargrupooptativa as gcgo on gcgod.gradecurriculargrupooptativa = gcgo.codigo"+
	" where his.matricula  = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual	"+
	" and gcgo.gradecurricular = matricula.gradecurricularatual		"+
	" and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))"+
    " as t)", TipoCampoEnum.INTEIRO, 10),
	
	MATRICULA_MATRIZ_CURRICULAR_PERCENTUAL_INTEGRALIZACAO_ESTAGIO("gradecurricularatual", "Percentual de Integralização Estágio", "gradecurricularatual.percentualpermitiriniciarestagio", TipoCampoEnum.DOUBLE, 10),
	MATRICULA_MATRIZ_CURRICULAR_CH_ESTAGIO("gradecurricularatual", "C.H. Estágio Matriz Curricular", "((SELECT sum(coalesce(gradecurricularestagio.cargahorarioobrigatorio,0)) as cargahorarioobrigatorio_estagio from gradecurricularestagio where gradecurricularestagio.gradecurricular = gradecurricularatual.codigo ))", TipoCampoEnum.DOUBLE, 10),
	MATRICULA_MATRIZ_CURRICULAR_CH_CURSANDO_ESTAGIO("gradecurricularatual", "C.H. Estágio Cursando Matriz Curricular", "((SELECT case when sum(coalesce(est_cursando.cargaHorariadeferida,0)) is null then 0 else sum(coalesce(est_cursando.cargaHorariadeferida,0)) end from estagio est_cursando where est_cursando.matricula = matricula.matricula and est_cursando.situacaoestagioenum != 'INDEFERIDO' ))", TipoCampoEnum.DOUBLE, 10),	
	MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDO_ESTAGIO("gradecurricularatual", "C.H. Estágio Cumprido Matriz Curricular", "((SELECT case when sum(coalesce(est.cargaHorariadeferida,0)) is null then 0 else sum(coalesce(est.cargaHorariadeferida,0)) end from estagio est where est.matricula = matricula.matricula and est.situacaoestagioenum = 'DEFERIDO' ))", TipoCampoEnum.DOUBLE, 10),	
	
	MATRICULA_MATRIZ_CURRICULAR_CH_ATIVIDADE_COMPLEMENTAR("gradecurricularatual", "C.H. Atividade Complementar Matriz Curricular", "(case when gradecurricularatual.totalcargahorariaatividadecomplementar is null then 0 else gradecurricularatual.totalcargahorariaatividadecomplementar end)", TipoCampoEnum.INTEIRO, 10),
	MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDO_ATIVIDADE_COMPLEMENTAR("gradecurricularatual", "C.H. Cumprido Atividade Complementar Matriz Curricular", "(select case when cargaHorariaRealizadaAtividadeComplementar is null then 0 else cargaHorariaRealizadaAtividadeComplementar end from (   "+
		" select 1 as ordem, sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) as cargaHorariaRealizadaAtividadeComplementar from ("+ 
		" 	select sum(racm.cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar,"+
		" 	racm.tipoAtividadeComplementar,"+ 
		" 	gctac.cargahoraria"+ 
		" 	from registroatividadecomplementarmatricula racm "+
		" 	inner join gradecurriculartipoatividadecomplementar gctac on gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar"+ 
		" 	where matricula = matricula.matricula and gctac.gradecurricular = matricula.gradecurricularatual"+
		" 	group by racm.tipoAtividadeComplementar, 	gctac.cargahoraria ) as t "+
		"   union "+
		"   select 2 as ordem, case when matricula.atividadecomplementar is null or isnumeric(matricula.atividadecomplementar) = false then 0 else trim(matricula.atividadecomplementar)::INT end as cargaHorariaRealizadaAtividadeComplementar   "+
		"   order by ordem limit 1 "+
		" ) as chcomplementar)", TipoCampoEnum.INTEIRO, 10),
		
		MATRICULA_MATRIZ_CURRICULAR_CH_CURSANDO_OBRIGATORIA("gradecurricularatual", "C.H Cursando Obrigatoria", "(select sum(cargahorariadisciplina) from historico his "+
			     "inner join gradedisciplina as gd on gd.codigo = his.gradedisciplina "+
			     "inner join periodoletivo as pl on pl.codigo = gd.periodoletivo "+     
			     "where his.matricula = matricula.matricula "+
			     "and his.matrizcurricular = matricula.gradecurricularatual "+
			     "and his.matrizcurricular = pl.gradecurricular "+
			     "and his.situacao in ('CS', 'CE') "+    
			     "and gd.tipodisciplina not in ('OP', 'LO'))", TipoCampoEnum.INTEIRO, 10),
				
				MATRICULA_MATRIZ_CURRICULAR_CH_CURSANDO_OPTATIVA("gradecurricularatual", "C.H Cursando Optativa", "	(select sum(cargahoraria) as cargahoraria  from ( "+    
			     "select sum(cargahorariadisciplina) as cargahoraria from historico his "+
			     "inner join gradedisciplina as gd on gd.codigo = his.gradedisciplina "+    
			     "inner join periodoletivo as pl on pl.codigo = gd.periodoletivo "+
			     "where his.matricula = matricula.matricula "+
			     "and his.matrizcurricular = matricula.gradecurricularatual "+
			     "and his.matrizcurricular = pl.gradecurricular "+
			     "and his.situacao in ('CS', 'CE')  "+    
			     "and gd.tipodisciplina in ('OP', 'LO') "+
			     "union all "+
			     "select sum(cargahorariadisciplina) as cargahoraria from historico his "+
			     "inner join gradecurriculargrupooptativadisciplina as gcgod on gcgod.codigo = his.gradecurriculargrupooptativadisciplina "+     
			     "inner join gradecurriculargrupooptativa as gcgo on gcgo.codigo = gcgod.gradecurriculargrupooptativa     "+
			     "where his.matricula = matricula.matricula "+
			     "and his.matrizcurricular = matricula.gradecurricularatual "+
			     "and his.matrizcurricular = gcgo.gradecurricular "+
			     "and his.situacao in ('CS', 'CE') "+         
			    ") as t)", 	TipoCampoEnum.INTEIRO, 10),
				
		   SITUACAO_DOCUMENTO_ASSINADO_MATRICULA_PERIODO("documentoAssinado","Situação Documento Assinado","documentoassinadopessoa.situacaodocumentoassinadopessoa",TipoCampoEnum.TEXTO, 30),
		   DATA_REJEICAO_DOCUMENTO_ASSINADO("documentoAssinado","Data Rejeição","documentoassinadopessoa.datarejeicao",TipoCampoEnum.DATA, 30),
		   MOTIVO_REJEICAO_DOCUMENTO_ASSINADO("documentoAssinado","Motivo Rejeição","documentoassinadopessoa.motivorejeicao",TipoCampoEnum.TEXTO, 130),
		   DATA_ASSINATURA_DOCUMENTO_ASSINADO("documentoAssinado","Data Assinatura","documentoassinadopessoa.dataassinatura",TipoCampoEnum.DATA, 30),
		   NOME_FUNCIONARIO1_DOCUMENTO_ASSINADO("documentoAssinado","Nome Funcionario 1","assinaturafuncionario1.nome",TipoCampoEnum.TEXTO, 130),
		   NOME_FUNCIONARIO2_DOCUMENTO_ASSINADO("documentoAssinado","Nome Funcionario 2","assinaturafuncionario2.nome",TipoCampoEnum.TEXTO, 130),
		   SITUACAO_FUNCIONARIO1_DOCUMENTO_ASSINADO("documentoAssinado","Situação Documento Assinado Funcionario 1","documentoassinadopessoafuncionario1.situacaodocumentoassinadopessoa",TipoCampoEnum.TEXTO, 130),
		   SITUACAO_FUNCIONARIO2_DOCUMENTO_ASSINADO("documentoAssinado","Situação Documento Assinado Funcionario 2","documentoassinadopessoafuncionario2.situacaodocumentoassinadopessoa",TipoCampoEnum.TEXTO, 130),
		
	;

	private TagSEIDecidirMatriculaEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
