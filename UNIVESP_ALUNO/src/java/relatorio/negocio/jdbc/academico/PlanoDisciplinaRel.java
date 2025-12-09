package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ReferenciaBibliograficaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import relatorio.negocio.comuns.academico.PlanoDisciplinaConteudoPlanejamentoRelVO;
import relatorio.negocio.comuns.academico.PlanoDisciplinaReferenciaBibliograficaRelVO;
import relatorio.negocio.comuns.academico.PlanoDisciplinaRelVO;
import relatorio.negocio.interfaces.academico.PlanoDisciplinaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PlanoDisciplinaRel extends SuperRelatorio implements PlanoDisciplinaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public PlanoDisciplinaRel() {

	}

	public void validarDados(Integer unidadeEnsino, Integer curso, Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception {
//		if (unidadeEnsino.equals(0)) {
//			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
//		}
//		if (curso.equals(0) && turma.equals(0)) {
//			throw new Exception("O campo CURSO ou TURMA deve ser informado.");
//		}
//		if (gradeCurricular.equals(0)) {
//			throw new Exception("O campo GRADE CURRICULAR deve ser informado.");
//		}
//		if (ano == null || ano.trim().isEmpty()) {
//			throw new Exception("O campo ANO deve ser informado.");
//		}
//		if (ano != null && ano.trim().length() != 4) {
//			throw new Exception("O campo ANO deve ter 4 dígitos.");
//		}
//
//		if (semestre == null || semestre.trim().isEmpty()) {
//			throw new Exception("O campo SEMESTRE deve ser informado.");
//		}
	}

	public List<PlanoEnsinoVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, Integer gradeCurricular, Integer disciplina, String ano, String semestre, UsuarioVO usuarioVO,
			PessoaVO professor, PeriodicidadeEnum periodicidadeEnum, String situacao, Integer codigoQuestionarioPlanoEnsino) throws Exception {
		validarDados(unidadeEnsino, curso, turma, gradeCurricular, ano, semestre);
		return executarConsultarParametrizada(gradeCurricular, disciplina, usuarioVO, ano, semestre, professor, periodicidadeEnum, situacao, codigoQuestionarioPlanoEnsino);
	}

	public List<PlanoEnsinoVO> executarConsultarParametrizada(Integer gradeCurricular, Integer disciplina, UsuarioVO usuarioVO, String ano, String semestre,
			PessoaVO professor, PeriodicidadeEnum periodicidadeEnum, String situacao, Integer codigoQuestionarioPlanoEnsino) throws Exception {
		StringBuilder sql = new StringBuilder();
		List<Object> filtros = new ArrayList<>(0);
		
		sql.append(" select  planoEnsino.codigo as \"planoEnsino.codigo\", curso.codigo as \"curso.codigo\" ");
		sql.append(" from planoEnsino ");
		sql.append(" inner join disciplina on disciplina.codigo = planoEnsino.disciplina  ");
		sql.append(" inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" inner join gradecurricular on periodoletivo.gradecurricular = gradecurricular.codigo ");  
		sql.append(" left join curso on curso.codigo = gradecurricular.curso and planoEnsino.curso =curso.codigo ");

		realizarGeracaoWhere(gradeCurricular, disciplina, ano, semestre, professor, situacao, periodicidadeEnum, sql, filtros);
		
		sql.append(" group by  disciplina.codigo , disciplina.nome , disciplina.tipodisciplina , ");
		sql.append(" gradedisciplina.cargahoraria , planoEnsino.ementa , planoEnsino.perfilegresso , ");
		sql.append(" planoEnsino.objetivogeral , planoEnsino.objetivoEspecifico , ");
		sql.append(" planoEnsino.estrategiaAvaliacao, planoEnsino.procedimentoDidatico , ");
		sql.append(" planoEnsino.ano, planoEnsino.semestre , planoEnsino.codigo , curso.codigo, ");
		sql.append(" periodoletivo.periodoletivo, CASE WHEN(select disciplinaprerequisito.disciplina from disciplinaprerequisito  ");
		sql.append(" inner join gradedisciplina gradedisciplina1 on gradedisciplina1.codigo = disciplinaprerequisito.gradedisciplina ");
		sql.append(" where disciplinaprerequisito.disciplina = disciplina.codigo limit 1) > 0 then true else false end  ");
		sql.append(" order by periodoletivo.periodoletivo, disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		return montarDadosConsulta(tabelaResultado, usuarioVO, codigoQuestionarioPlanoEnsino);
	}

	private void realizarGeracaoWhere(Integer gradeCurricular, Integer disciplina, String ano, String semestre,
			PessoaVO professor, String situacao, PeriodicidadeEnum periodicidadeEnum, StringBuilder sql, List<Object> filtros) {
		sql.append(" where 1 = 1 ");
		if (Uteis.isAtributoPreenchido(gradeCurricular)) {
			sql.append(" and gradecurricular = ?");
			filtros.add(gradeCurricular);
		}
		
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and  planoEnsino.ano = ?");
			filtros.add(ano);
		}
		
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and  planoEnsino.semestre = '").append(semestre).append("' ");
		}
		if (!disciplina.equals(0)) {
			sql.append(" and disciplina.codigo = ?");
			filtros.add(disciplina);
		}

		if (Uteis.isAtributoPreenchido(situacao) && !situacao.equals("TO")) {
			sql.append(" and planoEnsino.situacao = ?");
			filtros.add(situacao);
		}

		if (Uteis.isAtributoPreenchido(professor)) {
			sql.append(" and planoEnsino.professorresponsavel = ?");
			filtros.add(professor.getCodigo());
		}

		if (Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			sql.append(" and planoEnsino.periodicidade = ?");
			filtros.add(periodicidadeEnum.getValor());
		}
	}

	public List<PlanoDisciplinaConteudoPlanejamentoRelVO> consultarConteudoPlanejamentoPorDisciplina(Integer planoEstudo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select habilidade habilidade, conteudo from conteudoplanejamento ");
		sb.append(" where planoensino = ").append(planoEstudo);
		sb.append(" order by ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<PlanoDisciplinaConteudoPlanejamentoRelVO> vetResultado = new ArrayList<PlanoDisciplinaConteudoPlanejamentoRelVO>(0);
		while (tabelaResultado.next()) {
			PlanoDisciplinaConteudoPlanejamentoRelVO obj = new PlanoDisciplinaConteudoPlanejamentoRelVO();
			obj.setHabilidade(tabelaResultado.getString("habilidade"));
			obj.setConteudo(tabelaResultado.getString("conteudo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<PlanoDisciplinaReferenciaBibliograficaRelVO> consultarReferenciaBibliograficaPorDisciplina(Integer planoEstudo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select case when publicacaoExistenteBiblioteca then catalogo.titulo else referenciabibliografica.titulo end as titulo, ");
		sb.append(" case when publicacaoExistenteBiblioteca then catalogo.anoPublicacao else referenciabibliografica.anoPublicacao end as anoPublicacao, ");
		sb.append(" case when publicacaoExistenteBiblioteca then catalogo.edicao else referenciabibliografica.edicao end as edicao, ");
		sb.append(" case when publicacaoExistenteBiblioteca then cidadepublicacaocatalogo.nome else referenciabibliografica.localPublicacao end AS localPublicacao, ");
		sb.append(" case when publicacaoExistenteBiblioteca then catalogo.isbn else referenciabibliografica.ISBN end as ISBN, ");
		sb.append(" case when publicacaoExistenteBiblioteca then tipocatalogo.nome else case tipoPublicacao when 'LI' then 'Livro' when 'RE' then 'Revista' when 'AR' then 'Artigo' when 'MO' then 'Monografia'  when 'VI' then 'Video' else 'Livro' end end AS tipopublicacao,  ");
		sb.append(" case when (referenciabibliografica.tiporeferencia = 'CO') then 'Complementar' else 'Básica' end AS tiporeferencia ");
		sb.append(" from referenciabibliografica ");
		sb.append(" left join catalogo on catalogo.codigo = referenciabibliografica.catalogo ");
		sb.append(" left join cidadepublicacaocatalogo on cidadepublicacaocatalogo.codigo = catalogo.cidadepublicacao ");
		sb.append(" left join tipocatalogo on tipocatalogo.codigo = catalogo.tipocatalogo ");
		sb.append(" where planoensino = ").append(planoEstudo);
		sb.append(" order by referenciabibliografica.tiporeferencia, titulo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<PlanoDisciplinaReferenciaBibliograficaRelVO> vetResultado = new ArrayList<PlanoDisciplinaReferenciaBibliograficaRelVO>(0);
		while (tabelaResultado.next()) {
			PlanoDisciplinaReferenciaBibliograficaRelVO obj = new PlanoDisciplinaReferenciaBibliograficaRelVO();
			obj.setTitulo(tabelaResultado.getString("titulo"));
			obj.setAnoPublicacao(tabelaResultado.getString("anoPublicacao"));
			obj.setEdicao(tabelaResultado.getString("edicao"));
			obj.setLocalPublicacao(tabelaResultado.getString("localPublicacao"));
			obj.setIsbn(tabelaResultado.getString("isbn"));
			obj.setTipoPublicacao(tabelaResultado.getString("tipoPublicacao"));
			obj.setTipoReferencia(tabelaResultado.getString("tipoReferencia"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<PlanoEnsinoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO, Integer codigoQuestionarioPlanoEnsino) throws Exception {
		List<PlanoEnsinoVO> vetResultado = new ArrayList<PlanoEnsinoVO>(0);
		while (tabelaResultado.next()) {
			PlanoEnsinoVO obj = new PlanoEnsinoVO();
			montarDados(tabelaResultado, obj, usuarioVO, codigoQuestionarioPlanoEnsino);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void montarDados(SqlRowSet dadosSQL, PlanoEnsinoVO obj, UsuarioVO usuarioVO, Integer codigoQuestionarioPlanoEnsino) throws Exception {

		Integer codigoQuestionario = 0;
		obj.setCodigo(dadosSQL.getInt("planoensino.codigo"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		
		if(Uteis.isAtributoPreenchido(obj.getCurso())) {
			obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
			codigoQuestionario = obj.getCurso().getQuestionarioVO().getCodigo();
		}else {
			codigoQuestionario = codigoQuestionarioPlanoEnsino;
		}
		
		
		obj.setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
		if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemVO())) {
			obj.getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
				 
			for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : obj.getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), usuarioVO);
			}		
		}
		
		//obj.setListaPlanoConteudoPlanejamentoVOs(consultarConteudoPlanejamentoPorDisciplina(dadosSQL.getInt("planoensino.codigo"), usuarioVO));
		//obj.setListaPlanoReferenciaBibliograficaVOs(consultarReferenciaBibliograficaPorDisciplina(dadosSQL.getInt("planoensino.codigo"), usuarioVO));
	}

	public String definirTipoDisciplina(String tipoDisciplina) {
		return TipoDisciplina.getDescricao(tipoDisciplina);
	}

	public List<DisciplinaVO> consultarDisciplina(Integer gradeCurricular, String campoConsultaDisciplina, String valorConsultaDisciplina, UsuarioVO usuarioVO) throws Exception {
		List<DisciplinaVO> objs = new ArrayList<>(0);
		if (campoConsultaDisciplina.equals("nome")) {
			objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(valorConsultaDisciplina, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		}
		if (campoConsultaDisciplina.equals("codigo")) {
			if (valorConsultaDisciplina.equals("")) {
				valorConsultaDisciplina = ("0");
			}
			int valorInt = Integer.parseInt(valorConsultaDisciplina);
			objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		}

		return objs;
	}

	@Override
	public PlanoDisciplinaRelVO realizarGeracaoRelatorioPlanoEnsinoSintetico(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		List<PlanoDisciplinaRelVO> listaPlanoDisciplinaRelVOs = new ArrayList<PlanoDisciplinaRelVO>(0);
		PlanoDisciplinaRelVO planoDisciplinaRelVO = new PlanoDisciplinaRelVO();
		
		planoDisciplinaRelVO.setProfessor(planoEnsinoVO.getProfessorResponsavel().getNome().isEmpty() ? planoEnsinoVO.getProfessor() : planoEnsinoVO.getProfessorResponsavel().getNome());
		planoDisciplinaRelVO.setPlanoEnsinoHorarioAulaVOs(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs());
		planoDisciplinaRelVO.setCargaHoraria(planoEnsinoVO.getCargaHoraria());
		planoDisciplinaRelVO.setCodigoDisciplina(planoEnsinoVO.getDisciplina().getCodigo());
		planoDisciplinaRelVO.setPerfilEgresso(planoEnsinoVO.getPerfilEgresso());
		planoDisciplinaRelVO.setDisciplina(planoEnsinoVO.getDisciplina().getNome());
		planoDisciplinaRelVO.setEmenta(planoEnsinoVO.getEmenta());
		planoDisciplinaRelVO.setListaPlanoConteudoPlanejamentoVOs(new ArrayList<PlanoDisciplinaConteudoPlanejamentoRelVO>(0));
		planoDisciplinaRelVO.setSituacao(planoEnsinoVO.getSituacao_Apresentar());
		if(Uteis.isAtributoPreenchido(planoEnsinoVO.getTurno().getCodigo()) && !Uteis.isAtributoPreenchido(planoEnsinoVO.getTurno().getNome())) {
			planoEnsinoVO.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(planoEnsinoVO.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		planoDisciplinaRelVO.setTurno(planoEnsinoVO.getTurno().getNome());
		planoDisciplinaRelVO.setCurso(planoEnsinoVO.getCurso());

		if (planoEnsinoVO.getUnidadeEnsino().getCodigo() > 0 && planoEnsinoVO.getUnidadeEnsino().getNome().trim().isEmpty()) {
			planoEnsinoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(planoEnsinoVO.getUnidadeEnsino().getCodigo(), false, usuarioVO));
		}		
		planoDisciplinaRelVO.setUnidadeEnsino(planoEnsinoVO.getUnidadeEnsino());
		
		planoDisciplinaRelVO.setAnoSemestre(planoEnsinoVO.getAno() + "/" + planoEnsinoVO.getSemestre());
		
		return planoDisciplinaRelVO;
	}

	@Override
	public List<PlanoDisciplinaRelVO> realizarGeracaoRelatorioPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {

		List<PlanoDisciplinaRelVO> listaPlanoDisciplinaRelVOs = new ArrayList<PlanoDisciplinaRelVO>(0);
		PlanoDisciplinaRelVO planoDisciplinaRelVO = new PlanoDisciplinaRelVO();				

		realizarPreenchimentoDadosGradeCurricularPlanoEnsino(planoEnsinoVO);		

		if(Uteis.isAtributoPreenchido(planoEnsinoVO.getQuestionarioRespostaOrigemVO())) {
			planoDisciplinaRelVO.setPerguntaRespostaOrigemVOs(planoEnsinoVO.getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs());
			
			for (PerguntaRespostaOrigemVO perguntaRespostaOrigemVO : planoDisciplinaRelVO.getPerguntaRespostaOrigemVOs()) {
				perguntaRespostaOrigemVO.setListaPerguntaItemRespostaOrigemRel(new ArrayList<PerguntaItemRespostaOrigemVO>(0));
				if(Uteis.isAtributoPreenchido(perguntaRespostaOrigemVO.getPerguntaItemRespostaOrigemAdicionadaVOs())) {
					for (List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigemVOs : perguntaRespostaOrigemVO.getPerguntaItemRespostaOrigemAdicionadaVOs()) {
						for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : listaPerguntaItemRespostaOrigemVOs) {
							perguntaRespostaOrigemVO.getListaPerguntaItemRespostaOrigemRel().add(perguntaItemRespostaOrigemVO);
						}
					}
				}			
				
			}
		}
		
		planoDisciplinaRelVO.setProfessor(planoEnsinoVO.getProfessorResponsavel().getNome().isEmpty() ? planoEnsinoVO.getProfessor() : planoEnsinoVO.getProfessorResponsavel().getNome());
		planoDisciplinaRelVO.setPlanoEnsinoHorarioAulaVOs(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs());
		planoDisciplinaRelVO.setCargaHoraria(planoEnsinoVO.getCargaHoraria());
		planoDisciplinaRelVO.setCodigoDisciplina(planoEnsinoVO.getDisciplina().getCodigo());
		planoDisciplinaRelVO.setPerfilEgresso(planoEnsinoVO.getPerfilEgresso());
		planoDisciplinaRelVO.setDisciplina(planoEnsinoVO.getDisciplina().getNome());
		planoDisciplinaRelVO.setEmenta(planoEnsinoVO.getEmenta());
		planoDisciplinaRelVO.setListaPlanoConteudoPlanejamentoVOs(new ArrayList<PlanoDisciplinaConteudoPlanejamentoRelVO>(0));
		planoDisciplinaRelVO.setSituacao(planoEnsinoVO.getSituacao_Apresentar());
		if(Uteis.isAtributoPreenchido(planoEnsinoVO.getTurno().getCodigo()) && !Uteis.isAtributoPreenchido(planoEnsinoVO.getTurno().getNome())) {
			planoEnsinoVO.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(planoEnsinoVO.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		planoDisciplinaRelVO.setTurno(planoEnsinoVO.getTurno().getNome());
		PlanoDisciplinaConteudoPlanejamentoRelVO planoDisciplinaConteudoPlanejamentoRelVO = null;
		Ordenacao.ordenarLista(planoEnsinoVO.getConteudoPlanejamentoVOs(), "ordem");
		for (ConteudoPlanejamentoVO conteudoPlanejamentoVO : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			planoDisciplinaConteudoPlanejamentoRelVO = new PlanoDisciplinaConteudoPlanejamentoRelVO();
			planoDisciplinaConteudoPlanejamentoRelVO.setConteudo(conteudoPlanejamentoVO.getConteudo());
			planoDisciplinaConteudoPlanejamentoRelVO.setHabilidade(conteudoPlanejamentoVO.getHabilidade());
			planoDisciplinaConteudoPlanejamentoRelVO.setAtitude(conteudoPlanejamentoVO.getAtitude());
			planoDisciplinaConteudoPlanejamentoRelVO.setMetodologia(conteudoPlanejamentoVO.getMetodologia());
			planoDisciplinaConteudoPlanejamentoRelVO.setCargaHoraria(conteudoPlanejamentoVO.getCargahoraria());
			planoDisciplinaConteudoPlanejamentoRelVO.setClassificacao(conteudoPlanejamentoVO.getClassificacao_Apresentar());
			planoDisciplinaConteudoPlanejamentoRelVO.setPraticaSupervisionada(conteudoPlanejamentoVO.getPraticaSupervisionada());
			planoDisciplinaRelVO.getListaPlanoConteudoPlanejamentoVOs().add(planoDisciplinaConteudoPlanejamentoRelVO);
		}
		planoDisciplinaRelVO.setListaPlanoReferenciaBibliograficaVOs(new ArrayList<PlanoDisciplinaReferenciaBibliograficaRelVO>(0));
		PlanoDisciplinaReferenciaBibliograficaRelVO planoDisciplinaReferenciaBibliograficaRelVO = null;
		for (ReferenciaBibliograficaVO referenciaBibliograficaVO : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			planoDisciplinaReferenciaBibliograficaRelVO = new PlanoDisciplinaReferenciaBibliograficaRelVO();
			if (referenciaBibliograficaVO.getPublicacaoExistenteBiblioteca()) {
				planoDisciplinaReferenciaBibliograficaRelVO.setAnoPublicacao(referenciaBibliograficaVO.getCatalogo().getAnoPublicacao());
				planoDisciplinaReferenciaBibliograficaRelVO.setAutores(referenciaBibliograficaVO.getCatalogo().getApresentarListaConcatenadaAutores());
				planoDisciplinaReferenciaBibliograficaRelVO.setEdicao(referenciaBibliograficaVO.getCatalogo().getEdicao());
				planoDisciplinaReferenciaBibliograficaRelVO.setIsbn(referenciaBibliograficaVO.getCatalogo().getIsbn());
				planoDisciplinaReferenciaBibliograficaRelVO.setLocalPublicacao(referenciaBibliograficaVO.getCatalogo().getCidadePublicacaoCatalogo().getNome());
				planoDisciplinaReferenciaBibliograficaRelVO.setTipoPublicacao(referenciaBibliograficaVO.getCatalogo().getTipoCatalogo().getNome());
				planoDisciplinaReferenciaBibliograficaRelVO.setTipoReferencia(referenciaBibliograficaVO.getTipoReferencia_Apresentar());
				planoDisciplinaReferenciaBibliograficaRelVO.setTitulo(referenciaBibliograficaVO.getCatalogo().getTitulo());
				planoDisciplinaReferenciaBibliograficaRelVO.setSubtitulo(referenciaBibliograficaVO.getCatalogo().getSubtitulo());
				planoDisciplinaReferenciaBibliograficaRelVO.setJustificativa(referenciaBibliograficaVO.getJustificativa());
			} else {
				planoDisciplinaReferenciaBibliograficaRelVO.setAnoPublicacao(referenciaBibliograficaVO.getAnoPublicacao());
				planoDisciplinaReferenciaBibliograficaRelVO.setAutores(referenciaBibliograficaVO.getAutores());
				planoDisciplinaReferenciaBibliograficaRelVO.setEdicao(referenciaBibliograficaVO.getEdicao());
				planoDisciplinaReferenciaBibliograficaRelVO.setIsbn(referenciaBibliograficaVO.getISBN());
				planoDisciplinaReferenciaBibliograficaRelVO.setLocalPublicacao(referenciaBibliograficaVO.getLocalPublicacao());
				planoDisciplinaReferenciaBibliograficaRelVO.setTipoPublicacao(referenciaBibliograficaVO.getTipoPublicacao_Apresentar());
				planoDisciplinaReferenciaBibliograficaRelVO.setTipoReferencia(referenciaBibliograficaVO.getTipoReferencia_Apresentar());
				planoDisciplinaReferenciaBibliograficaRelVO.setTitulo(referenciaBibliograficaVO.getTitulo());
				planoDisciplinaReferenciaBibliograficaRelVO.setSubtitulo(referenciaBibliograficaVO.getSubtitulo());
				planoDisciplinaReferenciaBibliograficaRelVO.setJustificativa(referenciaBibliograficaVO.getJustificativa());
			}
			planoDisciplinaRelVO.getListaPlanoReferenciaBibliograficaVOs().add(planoDisciplinaReferenciaBibliograficaRelVO);
		}
		planoDisciplinaRelVO.setObjetivosEspecificos(planoEnsinoVO.getObjetivoEspecifico());
		planoDisciplinaRelVO.setObjetivosGerais(planoEnsinoVO.getObjetivoGeral());

		if (planoEnsinoVO.getUnidadeEnsino().getCodigo() > 0 && planoEnsinoVO.getUnidadeEnsino().getNome().trim().isEmpty()) {
			planoEnsinoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(planoEnsinoVO.getUnidadeEnsino().getCodigo(), false, usuarioVO));
		}		
		
		planoDisciplinaRelVO.setPeriodoLetivo(planoEnsinoVO.getPeriodoLetivo().getPeriodoLetivo() + "º Período");
		planoDisciplinaRelVO.setPreRequisito(planoEnsinoVO.getPreRequisito());
		planoDisciplinaRelVO.setProcedimentoDidatico(planoEnsinoVO.getProcedimentoDidatico());
		planoDisciplinaRelVO.setEstrategiasAvaliacao(planoEnsinoVO.getEstrategiaAvaliacao());
		planoDisciplinaRelVO.setTipoDisciplina(planoEnsinoVO.getGradeDisciplinaVO().getTipoDisciplina_Apresentar());
		planoDisciplinaRelVO.setAnoSemestre(planoEnsinoVO.getAno() + "/" + planoEnsinoVO.getSemestre());
		listaPlanoDisciplinaRelVOs.add(planoDisciplinaRelVO);
		return listaPlanoDisciplinaRelVOs;
	}
	

	@Override
	public void realizarPreenchimentoDadosGradeCurricularPlanoEnsino(PlanoEnsinoVO planoEnsinoVO) throws Exception{
		
		StringBuilder sqlStr = new StringBuilder("SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA' AS origem, periodoLetivo.codigo as periodoLetivo, periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
		sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplina = gradedisciplina.codigo limit 1 ) is not null as prerequisito, ");
		sqlStr.append(" disciplina.nome as disciplina, gradedisciplina.tipodisciplina, "); 
		sqlStr.append(" gradedisciplina.codigo as codigoOrigem, gradedisciplina.cargahoraria, gradedisciplina.horaaula, gradedisciplina.cargahorariapratica, gradedisciplina.cargahoraria - gradedisciplina.cargahorariapratica as  cargahorariateorica ");
		sqlStr.append(" FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
		sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido((planoEnsinoVO.getGradeCurricular().getCodigo()))){
			sqlStr.append(" and GradeCurricular.codigo = ").append(planoEnsinoVO.getGradeCurricular().getCodigo().intValue()).append(" ");
		}
		sqlStr.append(" and  disciplina.codigo = ").append(planoEnsinoVO.getDisciplina().getCodigo());
		
		sqlStr.append(" union SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA_COMPOSTA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
		sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo limit 1 ) is not null as prerequisito, ");
		sqlStr.append(" disciplina.nome as disciplina, gradedisciplina.tipodisciplina, ");
		sqlStr.append(" gradedisciplinacomposta.codigo as codigoOrigem, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.horaaula, gradedisciplinacomposta.cargahorariapratica, gradedisciplinacomposta.cargahorariateorica ");
		sqlStr.append(" FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
		sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido((planoEnsinoVO.getGradeCurricular().getCodigo()))){
			sqlStr.append(" and GradeCurricular.codigo = ").append(planoEnsinoVO.getGradeCurricular().getCodigo().intValue()).append(" ");
		}

		sqlStr.append(" and  disciplina.codigo = ").append(planoEnsinoVO.getDisciplina().getCodigo());
		
		sqlStr.append(" union (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRUPO_OPTATIVA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
		sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo limit 1 ) is not null as prerequisito, ");
		sqlStr.append(" disciplina.nome as disciplina, 'OP' as tipodisciplina, ");
		sqlStr.append(" gradecurriculargrupooptativadisciplina.codigo as codigoOrigem, gradecurriculargrupooptativadisciplina.cargahoraria, gradecurriculargrupooptativadisciplina.horaaula, gradecurriculargrupooptativadisciplina.cargahorariapratica, gradecurriculargrupooptativadisciplina.cargahoraria - gradecurriculargrupooptativadisciplina.cargahorariapratica as  cargahorariateorica ");
		sqlStr.append(" FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido((planoEnsinoVO.getGradeCurricular().getCodigo()))){
			sqlStr.append(" and GradeCurricular.codigo = ").append(planoEnsinoVO.getGradeCurricular().getCodigo().intValue()).append(" ");
		}

		sqlStr.append(" and  disciplina.codigo = ").append(planoEnsinoVO.getDisciplina().getCodigo());
		sqlStr.append(" order by periodoLetivo.periodoLetivo limit 1) ");
		
		sqlStr.append(" union (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA_COMPOSTA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
		sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo limit 1 ) is not null as prerequisito, ");
		sqlStr.append(" disciplina.nome as disciplina, 'OP' as tipodisciplina, ");
		sqlStr.append(" gradedisciplinacomposta.codigo as codigoOrigem, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.horaaula, gradedisciplinacomposta.cargahorariapratica, gradedisciplinacomposta.cargahorariateorica ");		
		sqlStr.append(" FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sqlStr.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido((planoEnsinoVO.getGradeCurricular().getCodigo()))){
			sqlStr.append(" and GradeCurricular.codigo = ").append(planoEnsinoVO.getGradeCurricular().getCodigo().intValue()).append(" ");
		}

		sqlStr.append(" and  disciplina.codigo = ").append(planoEnsinoVO.getDisciplina().getCodigo());
		sqlStr.append(" order by periodoLetivo.periodoLetivo limit 1) ");
		sqlStr.append(" limit 1 ");
		planoEnsinoVO.setPeriodoLetivo(null);
		planoEnsinoVO.setGradeDisciplinaCompostaVO(null);
		planoEnsinoVO.setGradeDisciplinaVO(null);
		planoEnsinoVO.setGradeCurricularGrupoOptativaDisciplinaVO(null);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			planoEnsinoVO.getGradeCurricular().setNome(rs.getString("GradeCurricular"));
			planoEnsinoVO.getDisciplina().setNome(rs.getString("disciplina"));
			planoEnsinoVO.setPreRequisito(rs.getBoolean("prerequisito"));
			planoEnsinoVO.getPeriodoLetivo().setCodigo(rs.getInt("periodoletivo"));
			planoEnsinoVO.getPeriodoLetivo().setPeriodoLetivo(rs.getInt("periodoletivo_periodoLetivo"));
			planoEnsinoVO.getPeriodoLetivo().setNomeCertificacao(rs.getString("periodoletivo_nomeCertificacao"));
			planoEnsinoVO.getPeriodoLetivo().setDescricao(rs.getString("periodoletivo_descricao"));			
			if(rs.getString("origem").equals("GRADE_DISCIPLINA")){
				planoEnsinoVO.getGradeDisciplinaVO().setCodigo(rs.getInt("codigoOrigem"));
				planoEnsinoVO.getGradeDisciplinaVO().setTipoDisciplina(rs.getString("tipoDisciplina"));
				planoEnsinoVO.getGradeDisciplinaVO().setCargaHoraria(rs.getInt("cargahoraria"));
				planoEnsinoVO.getGradeDisciplinaVO().setCargaHorariaPratica(rs.getInt("cargaHorariapratica"));
				planoEnsinoVO.getGradeDisciplinaVO().setCargaHorariaTeorica(rs.getInt("cargaHorariaTeorica"));
				planoEnsinoVO.getGradeDisciplinaVO().setHoraAula(rs.getInt("horaaula"));
			}else if(rs.getString("origem").equals("GRADE_DISCIPLINA_COMPOSTA")){
				planoEnsinoVO.getGradeDisciplinaCompostaVO().setCodigo(rs.getInt("codigoOrigem"));
				planoEnsinoVO.getGradeDisciplinaCompostaVO().setCargaHoraria(rs.getInt("cargahoraria"));
				planoEnsinoVO.getGradeDisciplinaVO().setTipoDisciplina(rs.getString("tipoDisciplina"));
				planoEnsinoVO.getGradeDisciplinaCompostaVO().setCargaHorariaPratica(rs.getInt("cargaHorariapratica"));
				planoEnsinoVO.getGradeDisciplinaCompostaVO().setCargaHorariaTeorica(rs.getInt("cargaHorariaTeorica"));
				planoEnsinoVO.getGradeDisciplinaCompostaVO().setHoraAula(rs.getInt("horaaula"));
			}else if(rs.getString("origem").equals("GRUPO_OPTATIVA")){
				planoEnsinoVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(rs.getInt("codigoOrigem"));
				planoEnsinoVO.getGradeDisciplinaVO().setTipoDisciplina(rs.getString("tipoDisciplina"));
				planoEnsinoVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCargaHoraria(rs.getInt("cargahoraria"));
				planoEnsinoVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCargaHorariaPratica(rs.getInt("cargaHorariapratica"));				
				planoEnsinoVO.getGradeCurricularGrupoOptativaDisciplinaVO().setHoraAula(rs.getInt("horaaula"));
			}
		}
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorioSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "PlanoDisciplinaRelSintetico" + ".jrxml");
	}
	
	public static String getDesignIReportRelatorio(String nomeRelatorio) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + nomeRelatorio + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}
	
	public static String getDesignIReportRelatorioLink() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "planoDisciplinaLinkRel.jrxml");
	}

	public static String getIdEntidade() {
		return ("PlanoDisciplinaRel");
	}

}
