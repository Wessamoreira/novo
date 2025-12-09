package relatorio.negocio.jdbc.avaliacaoInst;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.TipoLayoutApresentacaoResultadoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.processosel.RespostaPergunta;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO;
import relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.comuns.avaliacaoInst.RespostaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;
import relatorio.negocio.interfaces.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;


@Repository
public class AvaliacaoInstitucionalRel extends SuperRelatorio implements AvaliacaoInstitucionalRelInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AvaliacaoInstitucionalRel() {
		setIdEntidade("AvaliacaoInstitucionalRel");
	}

	
	public AvaliacaoInstucionalRelVO emitirRelatorioGrafico(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
		AvaliacaoInstucionalRelVO obj = new AvaliacaoInstucionalRelVO();
		consultarRelatorio(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional, avaliacaoInst, questionario, perguntaVOs, unidadeEnsino, curso, turno, turma, disciplinaVOs, obj, null, listaProfessor, listaRespondentes, dataInicio, dataFim, agruparResposta, gerarRelatorioPublicacao, usuarioVO, matriculaVO);
		//obj.gerarGrafico();
		executarOrdenarListas(obj);
		return obj;
	}

	public AvaliacaoInstucionalRelVO emitirRelatorio(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta) throws Exception {
		AvaliacaoInstucionalRelVO obj = new AvaliacaoInstucionalRelVO();
		consultarRelatorio(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional, avaliacaoInst, questionario, perguntaVOs, unidadeEnsino, curso, turno, turma, disciplinaVOs, obj, null, listaProfessor, listaRespondentes, dataInicio, dataFim, agruparResposta, false, null, null);
		//incializarDadosPerguntaRespostaSemResposta(obj, avaliacaoInst);
		executarOrdenarListas(obj);
		return obj;
	}

	public AvaliacaoInstucionalRelVO emitirRelatorioPorRespondente(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim) throws Exception {
		AvaliacaoInstucionalRelVO obj = new AvaliacaoInstucionalRelVO();
		consultarRelatorioPorRespondente(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional, avaliacaoInst, questionario, perguntaVOs, unidadeEnsino, curso, turno, turma, disciplinaVOs, obj, null, listaProfessor, listaRespondentes, dataInicio, dataFim); 
		executarOrdenarListas(obj);
		return obj;
	}

	public AvaliacaoInstucionalRelVO emitirRelatorioPerguntasTextuais(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta) throws Exception {
		AvaliacaoInstucionalRelVO obj = new AvaliacaoInstucionalRelVO();
		validarPerguntasTextuais(perguntaVOs);
		consultarRelatorio(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional, avaliacaoInst, questionario, perguntaVOs, unidadeEnsino, curso, turno, turma, disciplinaVOs, obj, true, listaProfessor, listaRespondentes, dataInicio, dataFim, agruparResposta, false, null, null);
		executarOrdenarListas(obj);
		return obj;
	}

	
	public void executarOrdenarListas(final AvaliacaoInstucionalRelVO obj) {
		Ordenacao.ordenarLista(obj.getQuestionarioRelVOs(), "ordenacao");
		ConsistirException consistirException = new ConsistirException();
		ProcessarParalelismo.executar(0, obj.getQuestionarioRelVOs().size(), consistirException, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				final QuestionarioRelVO questionarioRelVO = obj.getQuestionarioRelVOs().get(i);	
				ConsistirException consistirException = new ConsistirException();
				ProcessarParalelismo.executar(0, questionarioRelVO.getPerguntaRelVOs().size(), consistirException, new ProcessarParalelismo.Processo() {
					public void run(int i) {						
						PerguntaRelVO perguntaRelVO = questionarioRelVO.getPerguntaRelVOs().get(i);
						Ordenacao.ordenarLista(perguntaRelVO.getRespostaTexto(), "ordem");
					}
				});				
				Ordenacao.ordenarLista(questionarioRelVO.getPerguntaRelVOs(), "nrPergunta");
			}
		});		
	}

	
	public void incializarDadosPerguntaRespostaSemResposta(AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Integer avaliacaoInst) throws Exception {
		SqlRowSet rs = consultarDadosPergunta(avaliacaoInstucionalRelVO, avaliacaoInst);
		while (rs.next()) {
			avaliacaoInstucionalRelVO.inicializarDadosRespostaQuestionario(rs);
		}
	}

	public SqlRowSet consultarDadosPergunta(AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Integer avaliacaoInst) throws Exception {
		StringBuilder sb = new StringBuilder(" Select Questionario.codigo as Questionario_codigo, Pergunta.codigo as Pergunta_codigo, RespostaPergunta.codigo as RespostaPergunta_codigo, RespostaPergunta.descricao as RespostaPergunta_descricao, RespostaPergunta.ordem as ordem ");
		sb.append(" from AvaliacaoInstitucional ");
		sb.append(" inner join Questionario on Questionario.codigo = AvaliacaoInstitucional.questionario ");
		sb.append(" inner join PerguntaQuestionario on PerguntaQuestionario.questionario = Questionario.codigo ");
		sb.append(" left join Pergunta on Pergunta.codigo = PerguntaQuestionario.pergunta");
		sb.append(" left join RespostaPergunta on Pergunta.codigo = RespostaPergunta.pergunta");
		sb.append(" where  avaliacaoInstitucional.codigo =  " + avaliacaoInst);
		sb.append(" and pergunta.tipoResposta <> 'TE' and ( 1=1 ");
		int x = 0;
		for (QuestionarioRelVO questionarioRelVO : avaliacaoInstucionalRelVO.getQuestionarioRelVOs()) {
			if (x == 0) {
				sb.append(" or questionario.codigo = " + questionarioRelVO.getCodigo());
				x = 1;
			} else {
				sb.append(" or questionario.codigo = " + questionarioRelVO.getCodigo());
			}
		}
		sb.append(" ) order by perguntaquestionario.codigo, ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return rs;
	}

	public void consultarRelatorio(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select (case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then count(distinct matriculaaluno||case when disciplina.codigo is null then '0' else disciplina.codigo::VARCHAR end ||case when curso.codigo is null then '0' else curso.codigo::varchar end||case when turma.codigo is null then '0' else turma.codigo::VARCHAR end) else count(respondente.codigo||case when disciplina.codigo is null then '0' else disciplina.codigo::VARCHAR end ||case when curso.codigo is null then '0' else curso.codigo::varchar end||case when turma.codigo is null then '0' else turma.codigo::VARCHAR end) end) as totalPessoa, ");
		sqlStr.append(" perguntaquestionario.codigo, '' AS texto, resposta, pergunta.codigo AS \"pergunta\", Questionario.codigo as questionario, ");			
		sqlStr.append(" Questionario.escopo as questionario_escopo, UnidadeEnsino.codigo as UnidadeEnsino_codigo,  UnidadeEnsino.nome as UnidadeEnsino_nome, AvaliacaoInstitucional.nome as AvaliacaoInstitucional_nome, ");
		sqlStr.append(" questionario.descricao as questionario_descricao, Pergunta.descricao as Pergunta_descricao, Pergunta.tipoResposta as Pergunta_tipoResposta, ");
		if(getIsApresentarProfessor(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" pessoa.codigo as pessoa_codigo, pessoa.nome as pessoa_nome, ");
		}else{
			sqlStr.append(" 0 as pessoa_codigo, ");
			sqlStr.append(" '' as pessoa_nome, ");
		}
		if(getIsApresentarDisciplina(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" RespostaAvaliacaoInstitucionalDW.disciplina as disciplina, ");
			sqlStr.append(" disciplina.nome as disciplina_nome, ");
		}else{
			sqlStr.append(" 0 as disciplina, ");
			sqlStr.append(" '' as disciplina_nome, ");
		}		
		sqlStr.append(" questionario.descricao as questionario_descricao, pergunta.tipoResultadoGrafico, perguntaquestionario.ordem as ordem, ");
		sqlStr.append(" coordenador.codigo as coordenador_codigo, coordenador.nome as coordenador_nome, ");
		sqlStr.append(" cargo.codigo as cargo_codigo, cargo.nome as cargo_nome, ");
		sqlStr.append(" departamento.codigo as departamento_codigo, departamento.nome as departamento_nome, perguntaquestionario.respostaObrigatoria, ");
		if(getIsApresentarCurso(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append("curso.codigo as	curso, ");
			sqlStr.append("curso.nome as curso_nome, ");
		}else{
			sqlStr.append("0 as	curso, ");
			sqlStr.append("'' as curso_nome, ");
		}
		if(getIsApresentarTurma(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append("turma.codigo as	turma_codigo, ");
			sqlStr.append("turma.identificadorturma as	identificadorturma, ");
		}else{
			sqlStr.append(" 0 as turma_codigo, ");
			sqlStr.append(" '' as	identificadorturma, ");
		}
		sqlStr.append(" AvaliacaoInstitucional.publicoalvo, respostaAvaliacaoInstitucionalDW.respostaadicional as \"respostaAvaliacaoInstitucionalDW_respostaadicional\" ");
		
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW ");
		sqlStr.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa respondente on respondente.codigo = RespostaAvaliacaoInstitucionalDW.pessoa");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.professor = pessoa.codigo");
		sqlStr.append(" left join pessoa coordenador on coordenador.codigo = RespostaAvaliacaoInstitucionalDW.coordenador ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");
		sqlStr.append(" left join cargo on cargo.codigo = RespostaAvaliacaoInstitucionalDW.cargo ");
		sqlStr.append(" left join departamento on departamento.codigo = RespostaAvaliacaoInstitucionalDW.departamento ");
		sqlStr.append(" left join turma on turma.codigo = RespostaAvaliacaoInstitucionalDW.turma ");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.getCodigo().intValue());
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (relatorioTextual != null && !relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
		} else if (relatorioTextual != null && relatorioTextual) {
			sqlStr.append(" and (Pergunta.tipoResposta = 'TE' or (RespostaAvaliacaoInstitucionalDW.respostaadicional != '')) ");
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario != null && questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		String andOr = "and ( ";
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!disciplinaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PerguntaVO perguntaVO : perguntaVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!perguntaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PessoaVO pessoaVO : listaProfessor) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.professor = " + pessoaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!listaProfessor.isEmpty()) {
			sqlStr.append(" ) ");
		}		
		andOr = "and (";
		for (PessoaVO pessoaVO : listaRespondentes) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pessoa = " + pessoaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!listaRespondentes.isEmpty()) {
			sqlStr.append(" ) ");
		}		
		if(gerarRelatorioPublicacao){			
			sqlStr.append(getSqlWhereGerarRelatorioPublicacao(avaliacaoInst, gerarRelatorioPublicacao, usuarioVO, matriculaVO));
		}
		sqlStr.append(" group by resposta, pergunta.codigo, questionario_escopo, perguntaquestionario.ordem, ");
		sqlStr.append(" UnidadeEnsino_codigo, UnidadeEnsino_nome, texto, AvaliacaoInstitucional.publicoalvo, ");
		sqlStr.append(" AvaliacaoInstitucional_nome, questionario_descricao, Pergunta.descricao, Pergunta_tipoResposta, AvaliacaoInstitucional.publicoalvo, ");
		sqlStr.append(" Questionario.codigo, questionario_descricao, perguntaquestionario.codigo, tipoResultadoGrafico, ");
		sqlStr.append(" coordenador.codigo , coordenador.nome , cargo.codigo , cargo.nome , departamento.codigo, departamento.nome, RespostaAvaliacaoInstitucionalDW.tipopessoa ");
		if(getIsApresentarProfessor(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(", pessoa.codigo , pessoa.nome ");
		}
		if(getIsApresentarDisciplina(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(", disciplina.nome, RespostaAvaliacaoInstitucionalDW.disciplina  ");
		}		
		if(getIsApresentarCurso(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" , curso.codigo, curso.nome ");	
		}
		if(getIsApresentarTurma(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" , turma.codigo, turma.identificadorturma ");
		}
		sqlStr.append(" ,respostaAvaliacaoInstitucionalDW.respostaadicional ");
		if(publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.CURSO) || publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)  || publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TURMA)){
		sqlStr.append(" union all ");

		sqlStr.append(" select count(avaliacaoitem.codigo) as totalPessoa, perguntaquestionario.codigo, avaliacaoitem.texto, ");
		sqlStr.append(" '['||respostapergunta.codigo||']' as resposta, avaliacaoitem.pergunta, avaliacaoitem.questionario,  ");
		sqlStr.append(" questionario.escopo as questionario_escopo, avaliacao.unidadeensino as unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome as unidadeensino_nome, avaliacaoinstitucional.nome as avaliacaoinstitucional_nome, questionario.descricao as questionario_descricao, ");
		sqlStr.append(" pergunta.descricao as pergunta_descricao, pergunta.tiporesposta as pergunta_tiporesposta,  ");
		if(getIsApresentarProfessor(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" pessoa.codigo as pessoa_codigo, pessoa.nome as pessoa_nome, ");
		}else{
			sqlStr.append(" 0 as pessoa_codigo, ");
			sqlStr.append(" '' as pessoa_nome, ");
		}
		if(getIsApresentarDisciplina(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" avaliacao.disciplina as disciplina, ");
			sqlStr.append(" disciplina.nome as disciplina_nome, ");
		}else{
			sqlStr.append(" 0 as disciplina, ");
			sqlStr.append(" '' as disciplina_nome, ");
		}		
		sqlStr.append(" questionario.descricao as questionario_descricao, pergunta.tipoResultadoGrafico, perguntaquestionario.ordem as ordem, ");
		sqlStr.append(" 0, '', 0, '', 0, '', perguntaquestionario.respostaObrigatoria, ");
		if(getIsApresentarCurso(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append("curso.codigo as	curso, ");
			sqlStr.append("curso.nome as curso_nome, ");
		}else{
			sqlStr.append("0 as	curso, ");
			sqlStr.append("'' as curso_nome, ");
		}
		if(getIsApresentarTurma(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append("turma.codigo as	turma_codigo, ");
			sqlStr.append("turma.identificadorturma as	identificadorturma, ");
		}else{
			sqlStr.append(" 0 as turma_codigo, ");
			sqlStr.append(" '' as	identificadorturma, ");
		}
		sqlStr.append(" AvaliacaoInstitucional.publicoalvo, '' as respostaAvaliacaoInstitucionalDW_respostaadicional ");
		sqlStr.append(" from avaliacaoinstitucionalpresencialresposta avaliacao ");
		sqlStr.append(" inner join avaliacaoinstitucionalpresencialitemresposta avaliacaoitem on avaliacaoitem.avaliacaoinstitucionalpresencialresposta = avaliacao.codigo ");
		sqlStr.append(" left join respostapergunta on respostapergunta.codigo = avaliacaoitem.respostapergunta ");
		sqlStr.append(" inner join questionario on questionario.codigo = avaliacaoitem.questionario ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = avaliacao.unidadeensino ");
		sqlStr.append(" inner join avaliacaoinstitucional on avaliacaoinstitucional.codigo = avaliacao.avaliacaoinstitucional ");
		sqlStr.append(" inner join pergunta on pergunta.codigo = avaliacaoitem.pergunta ");
		sqlStr.append(" inner join curso on curso.codigo = avaliacao.curso ");
		sqlStr.append(" left join turma on turma.codigo = avaliacao.turma ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = avaliacao.disciplina ");
		sqlStr.append(" left join pessoa pessoa on pessoa.codigo = avaliacao.professor ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");

		sqlStr.append(" where avaliacao.avaliacaoinstitucional = " + avaliacaoInst.getCodigo().intValue());
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" AND respostapergunta.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			sqlStr.append(" AND respostapergunta.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (relatorioTextual != null && !relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
		} else if (relatorioTextual != null && relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta = 'TE' ");
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and curso.codigo = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and turma.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and turma.codigo = " + turma.intValue());
		}
		if (questionario != null && questionario.intValue() != 0) {
			sqlStr.append(" and questionario.codigo = " + questionario.intValue());
		}
		String andOr1 = "and ( ";
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			sqlStr.append(" " + andOr1 + "  disciplina.codigo = " + disciplinaVO.getCodigo().intValue());
			andOr1 = "or";
		}
		if (!disciplinaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr1 = "and (";
		for (PerguntaVO perguntaVO : perguntaVOs) {
			sqlStr.append(" " + andOr1 + "  pergunta.codigo = " + perguntaVO.getCodigo().intValue());
			andOr1 = "or";
		}
		if (!perguntaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr1 = "and (";
		for (PessoaVO pessoaVO : listaProfessor) {
			sqlStr.append(" " + andOr1 + "  pessoa.codigo = " + pessoaVO.getCodigo().intValue());
			andOr1 = "or";
		}
		if (!listaProfessor.isEmpty()) {
			sqlStr.append(" ) ");
						
		}
		if(gerarRelatorioPublicacao){
			getSqlWhereGerarRelatorioPublicacaoRespostaManual(avaliacaoInst, gerarRelatorioPublicacao, usuarioVO, matriculaVO);
		}
		sqlStr.append(" group by respostapergunta.codigo, AvaliacaoInstitucional.publicoalvo, avaliacaoitem.pergunta, avaliacaoitem.questionario, questionario.escopo, perguntaquestionario.ordem, ");
		sqlStr.append(" avaliacao.unidadeensino, avaliacaoinstitucional.nome, questionario.descricao, pergunta.descricao, pergunta.tiporesposta, AvaliacaoInstitucional.publicoalvo, ");
		if(getIsApresentarProfessor(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" pessoa.codigo, pessoa.nome,  ");
		}
		if(getIsApresentarDisciplina(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" disciplina.nome, avaliacao.disciplina, ");
		}
		
		sqlStr.append(" unidadeensino.nome, perguntaquestionario.codigo, qtderespostas, tipoResultadoGrafico, avaliacaoitem.texto ");
		if(getIsApresentarCurso(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" , curso.codigo, curso.nome ");	
		}
		if(getIsApresentarTurma(nivelDetalhamento, publicoAlvoAvaliacaoInstitucional)){
			sqlStr.append(" , turma.codigo, turma.identificadorturma ");
		}
		
		}		
		sqlStr.append(" order by curso_nome, identificadorturma, disciplina_nome, pessoa_codigo, ordem ");				
		//System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		montarDadosConsulta(nivelDetalhamento, tabelaResultado, unidadeEnsino, avaliacaoInstucionalRelVO, avaliacaoInst.getCodigo(), turno, turma, agruparResposta);

	}

	public int consultarQuantidadeResposta(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT sum(valor) totalResposta FROM ( ");
		sqlStr.append("SELECT count(RespostaAvaliacaoInstitucionalDW.codigo) valor ");

		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW ");
		sqlStr.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa respondente on respondente.codigo = RespostaAvaliacaoInstitucionalDW.pessoa");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.professor = pessoa.codigo");
		sqlStr.append(" left join pessoa coordenador on coordenador.codigo = RespostaAvaliacaoInstitucionalDW.coordenador ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");
		sqlStr.append(" left join cargo on cargo.codigo = RespostaAvaliacaoInstitucionalDW.cargo ");
		sqlStr.append(" left join departamento on departamento.codigo = RespostaAvaliacaoInstitucionalDW.departamento ");
		sqlStr.append(" left join turma on turma.codigo = RespostaAvaliacaoInstitucionalDW.turma ");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.getCodigo().intValue());
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (relatorioTextual != null && !relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
		} else if (relatorioTextual != null && relatorioTextual) {
			sqlStr.append(" and (Pergunta.tipoResposta = 'TE' or (RespostaAvaliacaoInstitucionalDW.respostaadicional != '')) ");
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario != null && questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		String andOr = "and ( ";
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!disciplinaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PerguntaVO perguntaVO : perguntaVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!perguntaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PessoaVO pessoaVO : listaProfessor) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.professor = " + pessoaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!listaProfessor.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PessoaVO pessoaVO : listaRespondentes) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pessoa = " + pessoaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!listaRespondentes.isEmpty()) {
			sqlStr.append(" ) ");
		}
		if (gerarRelatorioPublicacao) {
			sqlStr.append(getSqlWhereGerarRelatorioPublicacao(avaliacaoInst, gerarRelatorioPublicacao, usuarioVO, matriculaVO));
		}
		if (publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.CURSO) || publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS) || publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TURMA)) {
			sqlStr.append(" union all ");

			sqlStr.append(" SELECT	count(avaliacao.codigo)  valor ");
			sqlStr.append(" from avaliacaoinstitucionalpresencialresposta avaliacao ");
			sqlStr.append(" inner join avaliacaoinstitucionalpresencialitemresposta avaliacaoitem on avaliacaoitem.avaliacaoinstitucionalpresencialresposta = avaliacao.codigo ");
			sqlStr.append(" left join respostapergunta on respostapergunta.codigo = avaliacaoitem.respostapergunta ");
			sqlStr.append(" inner join questionario on questionario.codigo = avaliacaoitem.questionario ");
			sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = avaliacao.unidadeensino ");
			sqlStr.append(" inner join avaliacaoinstitucional on avaliacaoinstitucional.codigo = avaliacao.avaliacaoinstitucional ");
			sqlStr.append(" inner join pergunta on pergunta.codigo = avaliacaoitem.pergunta ");
			sqlStr.append(" inner join curso on curso.codigo = avaliacao.curso ");
			sqlStr.append(" left join turma on turma.codigo = avaliacao.turma ");
			sqlStr.append(" inner join disciplina on disciplina.codigo = avaliacao.disciplina ");
			sqlStr.append(" left join pessoa pessoa on pessoa.codigo = avaliacao.professor ");
			sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");

			sqlStr.append(" where avaliacao.avaliacaoinstitucional = " + avaliacaoInst.getCodigo().intValue());
			if (dataInicio != null && dataFim != null) {
				sqlStr.append(" AND respostapergunta.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
				sqlStr.append(" AND respostapergunta.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
			}
			if (unidadeEnsino.intValue() != 0) {
				sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
			}
			if (relatorioTextual != null && !relatorioTextual) {
				sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
			} else if (relatorioTextual != null && relatorioTextual) {
				sqlStr.append(" and Pergunta.tipoResposta = 'TE' ");
			}
			if (curso.intValue() != 0) {
				sqlStr.append(" and curso.codigo = " + curso.intValue());
			}
			if (turno.intValue() != 0) {
				sqlStr.append(" and turma.turno = " + turno.intValue());
			}
			if (turma.intValue() != 0) {
				sqlStr.append(" and turma.codigo = " + turma.intValue());
			}
			if (questionario != null && questionario.intValue() != 0) {
				sqlStr.append(" and questionario.codigo = " + questionario.intValue());
			}
			String andOr1 = "and ( ";
			for (DisciplinaVO disciplinaVO : disciplinaVOs) {
				sqlStr.append(" " + andOr1 + "  disciplina.codigo = " + disciplinaVO.getCodigo().intValue());
				andOr1 = "or";
			}
			if (!disciplinaVOs.isEmpty()) {
				sqlStr.append(" ) ");
			}
			andOr1 = "and (";
			for (PerguntaVO perguntaVO : perguntaVOs) {
				sqlStr.append(" " + andOr1 + "  pergunta.codigo = " + perguntaVO.getCodigo().intValue());
				andOr1 = "or";
			}
			if (!perguntaVOs.isEmpty()) {
				sqlStr.append(" ) ");
			}
			andOr1 = "and (";
			for (PessoaVO pessoaVO : listaProfessor) {
				sqlStr.append(" " + andOr1 + "  pessoa.codigo = " + pessoaVO.getCodigo().intValue());
				andOr1 = "or";
			}
			if (!listaProfessor.isEmpty()) {
				sqlStr.append(" ) ");

			}
			if (gerarRelatorioPublicacao) {
				getSqlWhereGerarRelatorioPublicacaoRespostaManual(avaliacaoInst, gerarRelatorioPublicacao, usuarioVO, matriculaVO);
			}
			sqlStr.append(" ) ");
		}else{
			sqlStr.append(" ) ");
		}

		//System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		int totalResposta = 0;
		while (tabelaResultado.next()) {
			totalResposta = tabelaResultado.getInt("totalResposta");
		}
        return totalResposta;
    }
	
	public Boolean getIsApresentarCurso(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional){
		return ((nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.CURSO) 
				|| nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.TURMA) 
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.CURSO)
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TURMA)
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA)
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO)
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES)
				|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO))
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR));
	}
	
	public Boolean getIsApresentarTurma(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional){
		return nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.TURMA) 
				|| ((publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA) || publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES))
						&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.CURSO)
						&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR)
						&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR_CURSO));
	}
	
	public Boolean getIsApresentarDisciplina(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional){
		return (!nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.CURSO) 
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.TURMA)
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL)
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR)
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR_CURSO));
	}
	
	public Boolean getIsApresentarProfessor(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional){
		return (nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR) 
				|| nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR_CURSO))
				|| (!nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.CURSO) 
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.TURMA)
				&& !nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL));
	}

	public void consultarRelatorioPorRespondente(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim) throws Exception {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select distinct respondente.codigo as respondente_codigo, respondente.nome as respondente_nome,  pessoa.codigo, case when respostaadicional <> '' then  substring(respostaadicional from position('{' in respostaadicional)+1 for (position('}' in respostaadicional)) - (position('{' in respostaadicional)) -1)  else respostaadicional end as respostaadicional, ");
		sqlStr.append("  perguntaquestionario.codigo, '' AS texto, resposta, pergunta.codigo AS \"pergunta\", Questionario.codigo as questionario, RespostaAvaliacaoInstitucionalDW.disciplina as disciplina ");
		sqlStr.append(" ,Questionario.escopo as questionario_escopo, UnidadeEnsino.codigo as UnidadeEnsino_codigo,  UnidadeEnsino.nome as UnidadeEnsino_nome, AvaliacaoInstitucional.nome as AvaliacaoInstitucional_nome");
		sqlStr.append(" ,questionario.descricao as questionario_descricao, Pergunta.descricao as Pergunta_descricao, Pergunta.tipoResposta as Pergunta_tipoResposta");
		sqlStr.append(" ,pessoa.codigo as pessoa_codigo, pessoa.nome as pessoa_nome, disciplina.nome as disciplina_nome, questionario.descricao as questionario_descricao, pergunta.tipoResultadoGrafico, perguntaquestionario.ordem as ordem, ");
		sqlStr.append(" coordenador.codigo as coordenador_codigo, coordenador.nome as coordenador_nome, ");
		sqlStr.append(" cargo.codigo as cargo_codigo, cargo.nome as cargo_nome, ");
		sqlStr.append(" departamento.codigo as departamento_codigo, departamento.nome as departamento_nome, ");			
		sqlStr.append("curso.codigo as	curso, ");
		sqlStr.append("curso.nome as curso_nome, ");
		sqlStr.append("turma.identificadorturma as	identificadorturma, ");		
		sqlStr.append(" AvaliacaoInstitucional.publicoalvo ");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa respondente on RespostaAvaliacaoInstitucionalDW.pessoa = respondente.codigo");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.professor = pessoa.codigo");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");
		sqlStr.append(" left join pessoa as coordenador on coordenador.codigo = RespostaAvaliacaoInstitucionalDW.coordenador ");
		sqlStr.append(" left join cargo on cargo.codigo = RespostaAvaliacaoInstitucionalDW.cargo ");
		sqlStr.append(" left join departamento on departamento.codigo = RespostaAvaliacaoInstitucionalDW.departamento ");
		sqlStr.append(" left join turma on turma.codigo = RespostaAvaliacaoInstitucionalDW.turma ");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (relatorioTextual != null && !relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
		} else if (relatorioTextual != null && relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta = 'TE' ");
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario != null && questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		String andOr = "and ( ";
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			if(disciplinaVO.getSelecionado()){
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (!disciplinaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PerguntaVO perguntaVO : perguntaVOs) {
			if(perguntaVO.getSelecionado()){
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (!perguntaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PessoaVO pessoaVO : listaProfessor) {
			if(pessoaVO.getSelecionado()){
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.professor = " + pessoaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (!listaProfessor.isEmpty()) {
			sqlStr.append(" ) ");
		}		
		andOr = "and (";
		for (PessoaVO pessoaVO : listaRespondentes) {
			if(pessoaVO.getSelecionado()){
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pessoa = " + pessoaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (!listaRespondentes.isEmpty()) {
			sqlStr.append(" ) ");
		}		

		sqlStr.append(" group by pessoa.codigo, respostaAdicional, resposta, pergunta.codigo, questionario_escopo, perguntaquestionario.ordem ");
		sqlStr.append(" ,UnidadeEnsino_codigo, UnidadeEnsino_nome, texto ");
		sqlStr.append(" ,respondente.codigo, respondente.nome, AvaliacaoInstitucional.publicoalvo ");
		sqlStr.append(" ,AvaliacaoInstitucional_nome, questionario_descricao, Pergunta.descricao, Pergunta_tipoResposta");
		sqlStr.append(" ,pessoa.codigo , pessoa.nome , Questionario.codigo, RespostaAvaliacaoInstitucionalDW.disciplina, disciplina_nome, questionario_descricao, perguntaquestionario.codigo, tipoResultadoGrafico ");
		sqlStr.append(" ,coordenador.codigo , coordenador.nome , cargo.codigo , cargo.nome , departamento.codigo, departamento.nome ");		
		sqlStr.append(" , curso.codigo, curso.nome ");			
		sqlStr.append(", turma.codigo, turma.identificadorturma ");
		
		sqlStr.append(" order by UnidadeEnsino_nome,pessoa.nome, ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		montarDadosConsultaRespondente(nivelDetalhamento, tabelaResultado, unidadeEnsino, avaliacaoInstucionalRelVO, avaliacaoInst, turno, turma);

	}


	public void montarDadosConsulta(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, SqlRowSet tabelaResultado, Integer unidadeEnsino, AvaliacaoInstucionalRelVO obj, Integer avaliacaoInst, Integer turno, Integer turma, boolean agruparResposta) throws Exception {
		while (tabelaResultado.next()) {
			obj.setNome(tabelaResultado.getString("AvaliacaoInstitucional_nome"));
			Integer codigoQuestionario = tabelaResultado.getInt("questionario");
			Integer codigoUnidadeEnsino = tabelaResultado.getInt("UnidadeEnsino_codigo");
			Integer codigoCurso = tabelaResultado.getInt("curso");
			Integer codigoDisciplina = tabelaResultado.getInt("disciplina");
			Integer codigoProfessor = tabelaResultado.getInt("pessoa_codigo");
			Integer codigoCoordenador = tabelaResultado.getInt("coordenador_codigo");
			Integer codigoCargo = tabelaResultado.getInt("cargo_codigo");
			Integer codigoDepartamento = tabelaResultado.getInt("departamento_codigo");
			Integer codigoTurma = tabelaResultado.getInt("turma_codigo");
			String escopo = tabelaResultado.getString("questionario_escopo");
			QuestionarioRelVO questionarioRelVO = obj.consultaQuestionarioRelVOs(nivelDetalhamento, unidadeEnsino, codigoQuestionario, codigoUnidadeEnsino, codigoCurso, codigoDisciplina, codigoProfessor, escopo, codigoCoordenador, codigoCargo, codigoDepartamento, codigoTurma);
			questionarioRelVO.setEscopo(escopo);
			montarDadosQuestionario(tabelaResultado, questionarioRelVO, avaliacaoInst, turno, turma, agruparResposta);			
			if (nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL) ) {
				if(unidadeEnsino == 0){
					questionarioRelVO.setNomeUnidadeEnsino("");
				}
				questionarioRelVO.setNomeCurso("");
				questionarioRelVO.setNomeDisciplina("");				
				questionarioRelVO.setNomeProfessor("");				
				questionarioRelVO.getCoordenador().setNome("");				
				questionarioRelVO.getCargo().setNome("");				
				questionarioRelVO.getDepartamento().setNome("");
				questionarioRelVO.setIdentificadorTurma("");
				questionarioRelVO.getDepartamento().setCodigo(0);
				questionarioRelVO.getCargo().setCodigo(0);
			}else if (nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.CURSO) ) {								
				questionarioRelVO.getCargo().setNome("");
				questionarioRelVO.getCargo().setCodigo(0);
				questionarioRelVO.getDepartamento().setNome("");
				questionarioRelVO.setIdentificadorTurma("");
				questionarioRelVO.getDepartamento().setCodigo(0);				
				if(!PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR.getValor().equals(tabelaResultado.getString("publicoAlvo"))){					
					questionarioRelVO.setCoordenador(null);
				}
			}else if (nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.TURMA) ) {						
				questionarioRelVO.getCargo().setNome("");
				questionarioRelVO.getCargo().setCodigo(0);
				questionarioRelVO.getDepartamento().setNome("");
				questionarioRelVO.getDepartamento().setCodigo(0);
				if(!PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR.getValor().equals(tabelaResultado.getString("publicoAlvo"))){
					questionarioRelVO.setCoordenador(null);
				}
			}else if(nivelDetalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.PROFESSOR)){
				questionarioRelVO.setNomeDisciplina("");
			}
			obj.adicionarQuestionarioRelVOs(questionarioRelVO, nivelDetalhamento, unidadeEnsino);
		}

		for (QuestionarioRelVO questionarioRelVO : obj.getQuestionarioRelVOs()) {
			for (PerguntaRelVO perguntaRelVO : questionarioRelVO.getPerguntaRelVOs()) {
				Integer qtdeResposta = 0;
				for (RespostaRelVO respostaRelVO : perguntaRelVO.getRespostaTexto()) {
					qtdeResposta += respostaRelVO.getQuantidadePessoa();
				}
				for (RespostaRelVO respostaRelVO : perguntaRelVO.getRespostaTexto()) {
					respostaRelVO.setTotalPessoas(qtdeResposta);
				}
				/**
				 * Criada regra para adicionar ao gráfico as RespostaPerguntaVOs
				 * que não constam em RespostaRelVOs, ou seja, as que não foram
				 * respondidas
				 * 
				 * @author Wellington Rodrigues
				 */
				if (!perguntaRelVO.getTipoTexto()) {
					for (RespostaPerguntaVO rp : perguntaRelVO.getRespostaPerguntaVOs()) {
						if (!verificarTodasRespostasPerguntasAdicionadas(perguntaRelVO.getRespostaTexto(), rp.getCodigo())) {
							RespostaRelVO rr = new RespostaRelVO();
							rr.setResposta("[" + rp.getCodigo() + "]");							
							rr.setListaRespostaAgrupadas(rp.getCodigo().toString());
							rr.setAgruparResposta(perguntaRelVO.consultarAgrupadorResposta(rr.getResposta()));
							rr.setQuantidadePessoa(0);
//								perguntaRelVO.adicionarRespostaPergunta(rr, tabelaResultado.getString("respostaAvaliacaoInstitucionalDW_respostaadicional"));
							perguntaRelVO.adicionarRespostaPergunta(rr, "");
							/*rr.setSiglaResposta(perguntaRelVO.consultarSiglaResposta(rr.getResposta()));
							rr.setNomeResposta(perguntaRelVO.consultarNomeResposta(rr.getResposta()));
							rr.setOrdem(perguntaRelVO.consultarOrdemResposta(rr.getResposta()));
							perguntaRelVO.getRespostaTexto().add(rr);*/
							
							
							
						}
					}
				}
			}
		}
	}

	/**
	 * Método responsável por verificar se todas as Respostas Perguntas
	 * vinculadas a Pergunta consta na RespostaRelVOs vinculada a PerguntaRelVO
	 * 
	 * @author Wellington Rodrigues
	 * @param respostaRelVOs
	 * @param respostaPergunta
	 * @return
	 * @throws Exception
	 */
	public boolean verificarTodasRespostasPerguntasAdicionadas(List<RespostaRelVO> respostaRelVOs, Integer respostaPergunta) throws Exception {
		if (respostaPergunta == null) {
			for (int i = 0; i < respostaRelVOs.size(); i++){
				if (respostaRelVOs.get(i) == null){
					return i >= 0;
				}
			}
		} else {
			for (RespostaRelVO respostaRelVO : respostaRelVOs) {
				if(!"Não Responderam".equals(respostaRelVO.getResposta()) && respostaRelVO.getListaRespostaAgrupadas().contains(respostaPergunta.toString())){
					return true;
				}
			}
		}
		return false;
	}

	public void montarDadosConsultaRespondente(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, SqlRowSet tabelaResultado, Integer unidadeEnsino, AvaliacaoInstucionalRelVO obj, Integer avaliacaoInst, Integer turno, Integer turma) throws Exception {
		while (tabelaResultado.next()) {
			obj.setNome(tabelaResultado.getString("AvaliacaoInstitucional_nome"));
			Integer codigoQuestionario = tabelaResultado.getInt("questionario");
			Integer codigoUnidadeEnsino = tabelaResultado.getInt("UnidadeEnsino_codigo");
			Integer codigoCurso = tabelaResultado.getInt("curso");
			Integer codigoDisciplina = tabelaResultado.getInt("disciplina");
			Integer codigoRespondente = tabelaResultado.getInt("respondente_codigo");
			Integer codigoPessoa = tabelaResultado.getInt("pessoa_codigo");
			String escopo = tabelaResultado.getString("questionario_escopo");
			Integer codigoCoordenador = tabelaResultado.getInt("coordenador_codigo");
			Integer codigoCargo = tabelaResultado.getInt("cargo_codigo");
			Integer codigoDepartamento = tabelaResultado.getInt("departamento_codigo");
			Integer codigoTurma = tabelaResultado.getInt("turma_codigo");
			QuestionarioRelVO questionarioRelVO = obj.consultaQuestionarioRelRespondenteVOs(nivelDetalhamento, codigoUnidadeEnsino, codigoQuestionario, codigoUnidadeEnsino, codigoCurso, codigoDisciplina, codigoPessoa, escopo, codigoRespondente, codigoCoordenador, codigoDepartamento,  codigoCargo, codigoTurma);
			montarDadosQuestionarioRespondente(tabelaResultado, questionarioRelVO, avaliacaoInst, turno, turma);
			obj.adicionarQuestionarioRelVOs(questionarioRelVO, nivelDetalhamento, unidadeEnsino);
		}

	}
	
	public void montarDadosQuestionario(SqlRowSet dadosSQL, QuestionarioRelVO obj, Integer avaliacaoInt, Integer turno, Integer turma,boolean agruparResposta ) throws Exception {
		if (obj.getCodigo().intValue() == 0) {
			obj.setNomeCurso(dadosSQL.getString("curso_nome"));
			obj.setNomeDisciplina(dadosSQL.getString("disciplina_nome"));
			obj.setNomeProfessor(dadosSQL.getString("pessoa_nome"));
			obj.setNomeUnidadeEnsino(dadosSQL.getString("unidadeEnsino_nome"));
			obj.setNome(dadosSQL.getString("questionario_descricao"));
			obj.setCodigoCurso(dadosSQL.getInt("curso"));
			obj.setCodigoDisciplina(dadosSQL.getInt("disciplina"));
			obj.setCodigoProfessor(dadosSQL.getInt("pessoa_codigo"));
			obj.setCodigoUnidadeEnsino(dadosSQL.getInt("UnidadeEnsino_codigo"));
			obj.setCodigo(dadosSQL.getInt("questionario"));
			obj.setEscopo(dadosSQL.getString("questionario_escopo"));
			obj.getCoordenador().setCodigo(dadosSQL.getInt("coordenador_codigo"));
			obj.getCoordenador().setNome(dadosSQL.getString("coordenador_nome"));
			obj.getCargo().setCodigo(dadosSQL.getInt("cargo_codigo"));
			obj.getCargo().setNome(dadosSQL.getString("cargo_nome"));
			obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento_codigo"));
			obj.getDepartamento().setNome(dadosSQL.getString("departamento_nome"));
			obj.setCodigoTurma(dadosSQL.getInt("turma_codigo"));
			obj.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		}
		PerguntaRelVO pergunta = obj.consultarPerguntaRelVOs(new Integer(dadosSQL.getInt("pergunta")));
		montarDadosPergunta(dadosSQL, pergunta, obj, avaliacaoInt, turno, turma, agruparResposta);

	}

	public void montarDadosQuestionarioRespondente(SqlRowSet dadosSQL, QuestionarioRelVO obj, Integer avaliacaoInt, Integer turno, Integer turma) throws Exception {
		if (obj.getCodigo().intValue() == 0) {
			obj.setCodigoRespondente(dadosSQL.getInt("respondente_codigo"));
			obj.setNomeRespondente(dadosSQL.getString("respondente_nome"));
			obj.setNomeCurso(dadosSQL.getString("curso_nome"));
			obj.setNomeDisciplina(dadosSQL.getString("disciplina_nome"));
			obj.setNomeProfessor(dadosSQL.getString("pessoa_nome"));
			obj.setNomeUnidadeEnsino(dadosSQL.getString("unidadeEnsino_nome"));
			obj.setNome(dadosSQL.getString("questionario_descricao"));
			obj.setCodigoCurso(dadosSQL.getInt("curso"));
			obj.setCodigoDisciplina(dadosSQL.getInt("disciplina"));
			obj.setCodigoProfessor(dadosSQL.getInt("pessoa_codigo"));
			obj.setCodigoUnidadeEnsino(dadosSQL.getInt("UnidadeEnsino_codigo"));
			obj.setCodigo(dadosSQL.getInt("questionario"));
			obj.setEscopo(dadosSQL.getString("questionario_escopo"));
			obj.getCoordenador().setCodigo(dadosSQL.getInt("coordenador_codigo"));
			obj.getCoordenador().setNome(dadosSQL.getString("coordenador_nome"));
			obj.getCargo().setCodigo(dadosSQL.getInt("cargo_codigo"));
			obj.getCargo().setNome(dadosSQL.getString("cargo_nome"));
			obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento_codigo"));
			obj.getDepartamento().setNome(dadosSQL.getString("departamento_nome"));
			obj.setCodigoTurma(dadosSQL.getInt("turma_codigo"));
			obj.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		}
		PerguntaRelVO pergunta = obj.consultarPerguntaRelVOs(new Integer(dadosSQL.getInt("pergunta")));
		montarDadosPerguntaRespondente(dadosSQL, pergunta, obj, avaliacaoInt, turno, turma);

	}
	
	public void montarDadosPergunta(SqlRowSet dadosSQL, PerguntaRelVO obj, QuestionarioRelVO questionario, Integer avaliacaoInst, Integer turno, Integer turma, boolean agruparResposta ) throws Exception {
		if (obj.getCodigo().intValue() == 0) {
			obj.setNome(dadosSQL.getString("Pergunta_descricao"));
			obj.setNrPergunta(dadosSQL.getInt("ordem"));
			obj.setCodigo(dadosSQL.getInt("pergunta"));
			obj.setRespostaTextual(dadosSQL.getString("resposta"));
			obj.setRespostaPerguntaVOs(consultarRespostaPerguntaVO(obj.getCodigo()));
			obj.setTipoResposta(dadosSQL.getString("Pergunta_tipoResposta"));	
			obj.setRespostaObrigatoria(dadosSQL.getBoolean("respostaObrigatoria"));
			obj.setAgruparReposta(agruparResposta);
			if (dadosSQL.getString("tipoResultadoGrafico") != null) {
				obj.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.valueOf(dadosSQL.getString("tipoResultadoGrafico")));
			}
		
		}
		montarDadosResposta(dadosSQL, obj);
		questionario.adiconarPerguntaRelVOs(obj);
	}

	public void montarDadosPerguntaRespondente(SqlRowSet dadosSQL, PerguntaRelVO obj, QuestionarioRelVO questionario, Integer avaliacaoInst, Integer turno, Integer turma) throws Exception {
		if (obj.getCodigo().intValue() == 0) {
			obj.setNome(dadosSQL.getString("Pergunta_descricao"));
			obj.setNrPergunta(dadosSQL.getInt("ordem"));
			obj.setCodigo(dadosSQL.getInt("pergunta"));
			obj.setRespostaTextual(dadosSQL.getString("resposta"));			
			obj.setRespostaPerguntaVOs(consultarRespostaPerguntaVO(obj.getCodigo()));
			obj.setTipoResposta(dadosSQL.getString("Pergunta_tipoResposta"));
			if (dadosSQL.getString("tipoResultadoGrafico") != null) {
				obj.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.valueOf(dadosSQL.getString("tipoResultadoGrafico")));
			}			
		}
		montarDadosRespostaRespondente(dadosSQL, obj);
		questionario.adiconarPerguntaRelVOs(obj);
	}

	
	public void montarDadosResposta(SqlRowSet dadosSql, PerguntaRelVO obj) throws Exception {
		RespostaRelVO resposta = new RespostaRelVO();
		String repostaAdicional = dadosSql.getString("respostaAvaliacaoInstitucionalDW_respostaadicional");
		resposta.setResposta(dadosSql.getString("resposta"));
		resposta.setListaRespostaAgrupadas(obj.consultarCodigoResposta(resposta.getResposta()));
		resposta.setAgruparResposta(obj.consultarAgrupadorResposta(resposta.getResposta()));
		resposta.setNomeResposta(dadosSql.getString("resposta"));
		resposta.setQuantidadePessoa(dadosSql.getInt("totalPessoa"));
		resposta.setOrdem(dadosSql.getInt("ordem"));
		if ((obj.getTipoResposta().equals("SE") || obj.getTipoResposta().equals("ME")) && resposta.getResposta().trim().isEmpty()) {
			resposta.setResposta("Não Responderam");
			resposta.setNomeResposta("Não Responderam");
		}		
		if (obj.getTipoResposta().equals("TE") || obj.getTipoResposta().equals("SE")) {
			obj.adicionarRespostaPergunta(resposta, repostaAdicional);
		} else {
			obj.adicionarPerguntaMultiplaEscolha(resposta.getResposta(), resposta.getQuantidadePessoa(), repostaAdicional);
		}
	}
	
	public void montarDadosRespostaRespondente(SqlRowSet dadosSql, PerguntaRelVO obj) throws Exception {
		RespostaRelVO resposta = new RespostaRelVO();
		resposta.setResposta(dadosSql.getString("resposta"));
		resposta.setListaRespostaAgrupadas(obj.consultarCodigoResposta(resposta.getResposta()));
		resposta.setAgruparResposta(obj.consultarAgrupadorResposta(resposta.getResposta()));
		resposta.setNomeResposta(dadosSql.getString("resposta"));
		try {
			resposta.setQuantidadePessoa(dadosSql.getInt("totalPessoa"));
		} catch (Exception e) {
			resposta.setQuantidadePessoa(1);
		}
		if (!dadosSql.getString("respostaadicional").equals("")) {
			resposta.setNomeRespostaAdicional(dadosSql.getString("respostaadicional"));
		}
		resposta.setNomeRespostaAdicional(dadosSql.getString("respostaadicional"));
		resposta.setOrdem(dadosSql.getInt("ordem"));
		if (obj.getTipoResposta().equals("TE") || obj.getTipoResposta().equals("SE")) {
			obj.adicionarRespostaPergunta(resposta, resposta.getNomeRespostaAdicional());
		} else {
			obj.adicionarPerguntaMultiplaEscolha(resposta.getResposta(), resposta.getQuantidadePessoa(), resposta.getNomeRespostaAdicional());
		}
	}

	
	public SqlRowSet consultarPesoPergunta(Integer avaliacaoInst, Integer questionario, Integer perguntaVO, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplinaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("Select count(pesoPergunta) as peso, pesoPergunta");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		if (disciplinaVO.intValue() != 0) {
			sqlStr.append(" and  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.intValue());
		}
		if (perguntaVO.intValue() != 0) {
			sqlStr.append(" and  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.intValue());
		}
		sqlStr.append(" group by pesoPergunta");
		sqlStr.append(" order by pesoPergunta");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;

	}

	
	public List<PessoaVO> consultarProfessor(Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select pessoa.codigo as pessoa_codigo, pessoa.nome as pessoa_nome");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.professor = pessoa.codigo");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		sqlStr.append(" and pessoa.codigo is not null and pessoa.codigo <> 0 ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}

		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		String andOr = "and ( ";
		boolean possuiDisciplina = false;
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			if(disciplinaVO.getSelecionado()){
				possuiDisciplina = true;
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (possuiDisciplina) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		boolean possuiPergunta = false;
		for (PerguntaVO perguntaVO : perguntaVOs) {
			if(perguntaVO.getSelecionado()){
				possuiPergunta = true;
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (possuiPergunta) {
			sqlStr.append(" ) ");
		}
		sqlStr.append(" group by pessoa.codigo , pessoa.nome order by UnidadeEnsino.nome, pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> listaPessoa = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO pessoa = new PessoaVO();
			pessoa.setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			pessoa.setNome(tabelaResultado.getString("pessoa_nome"));
			listaPessoa.add(pessoa);
		}
		return listaPessoa;

	}

	public List<PessoaVO> consultarRespondente(Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select pessoa.codigo as pessoa_codigo, pessoa.nome as pessoa_nome");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.pessoa = pessoa.codigo");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		sqlStr.append(" and pessoa.codigo is not null and pessoa.codigo <> 0 ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}

		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		String andOr = "and ( ";
		boolean possuiDisciplina = false;
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			if(disciplinaVO.getSelecionado()){
				possuiDisciplina = true;
				sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.getCodigo().intValue());
				andOr = "or";
			}
		}
		if (possuiDisciplina) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		boolean possuiPergunta = false;
		for (PerguntaVO perguntaVO : perguntaVOs) {
			if(perguntaVO.getSelecionado()){
				possuiPergunta = true;
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.getCodigo().intValue());
			andOr = "or";
			}
		}
		if (possuiPergunta) {
			sqlStr.append(" ) ");
		}
		sqlStr.append(" group by pessoa.codigo , pessoa.nome order by UnidadeEnsino.nome, pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> listaPessoa = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO pessoa = new PessoaVO();
			pessoa.setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			pessoa.setNome(tabelaResultado.getString("pessoa_nome"));
			listaPessoa.add(pessoa);
		}
		return listaPessoa;

	}

	public List<RespostaPerguntaVO> consultarRespostaPerguntaVO(Integer pergunta) throws Exception {
		return getFacadeFactory().getRespostaPerguntaFacade().consultarRespostaPerguntas(pergunta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}


	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalRel.jrxml");
	}

	public static String getDesignIReportRelatorioRespondente() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalRelRespondente.jrxml");
	}

	public static String getDesignIReportRelatorioGrafico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalGraficoRel.jrxml");
	}
	public static String getDesignIReportRelatorioLink() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalLinkRel.jrxml");
	}

	public static String getDesignIReportRelatorioGeral() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalGeralRel.jrxml");
	}

	public static String getDesignIReportRelatorioGeralExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalRelExcel.jrxml");
	}

	public static String getDesignIReportRelatorioRespondenteGeral() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalRelRespondente.jrxml");
	}

	public static String getDesignIReportRelatorioPerguntasTextuais() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalPerguntasTextuaisRel.jrxml");
	}

	public static String getDesignIReportRelatorioPerguntasTextuaisGeral() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalPerguntasTextuaisGeralRel.jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return AvaliacaoInstitucionalRel.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AvaliacaoInstitucionalRel.idEntidade = idEntidade;
	}
	
	@Override
	public List<QuestionarioRelVO> consultarQuantidadeRelatorioSerGerado(Boolean relatorioRespondente, AvaliacaoInstitucionalVO avaliacaoInst, NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select sum(totalPessoa) as totalPessoa, sum(qtdeRelatorio) as qtdeRelatorio,  ");
		sqlStr.append(" questionario, curso, curso_nome, disciplina_codigo, disciplina_nome, cargo_codigo, cargo_nome, ");		
		sqlStr.append(" questionario_escopo, UnidadeEnsino_codigo,  UnidadeEnsino_nome, AvaliacaoInstitucional_nome, ");		
		sqlStr.append(" questionario_descricao, ");				
		sqlStr.append(" departamento_codigo, departamento_nome, turma_codigo, identificadorTurma ");
		sqlStr.append(" from ( ");
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			if(getIsApresentarProfessor(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct  'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) qtdeRelatorio, ");
				}else{
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct  'P'||pessoa.codigo::varchar||'T'||turma.codigo::varchar) qtdeRelatorio, ");
				}
			}else{
				if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct  'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) qtdeRelatorio, ");
				}else{
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct turma.codigo) qtdeRelatorio, ");
				}
			}
		}else if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D'||disciplina.codigo::varchar)) as totalPessoa, ");
			if(getIsApresentarProfessor(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				sqlStr.append(" count(distinct  'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar) qtdeRelatorio, ");
			}else{
				sqlStr.append(" count(distinct  disciplina.codigo) qtdeRelatorio, ");
			}
		}else{
			sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D0')) as totalPessoa, ");
			if(getIsApresentarProfessor(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				sqlStr.append(" count(distinct  pessoa.codigo) qtdeRelatorio, ");
			}else if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				sqlStr.append(" count(distinct  curso.codigo) qtdeRelatorio, ");
			}else{
				sqlStr.append(" count(distinct  UnidadeEnsino.codigo) qtdeRelatorio, ");
			}
		}
		sqlStr.append(" Questionario.codigo as questionario,  ");
		sqlStr.append(" Questionario.escopo as questionario_escopo, UnidadeEnsino.codigo as UnidadeEnsino_codigo,  UnidadeEnsino.nome as UnidadeEnsino_nome, AvaliacaoInstitucional.nome as AvaliacaoInstitucional_nome, ");
		if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" RespostaAvaliacaoInstitucionalDW.curso as curso, curso.nome as curso_nome, ");
		}else{
			sqlStr.append(" 0 as curso, '' as curso_nome, ");
		}
		if(relatorioRespondente){
			sqlStr.append(" disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, cargo.codigo as cargo_codigo, cargo.nome as cargo_nome,");
		}else{
			sqlStr.append(" 0 as disciplina_codigo, '' as disciplina_nome, 0 as cargo_codigo, '' as cargo_nome, ");
		}
		sqlStr.append(" questionario.descricao as questionario_descricao, ");	
		sqlStr.append(" departamento.codigo as departamento_codigo, departamento.nome as departamento_nome,  ");
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" turma.codigo as turma_codigo, ");
			sqlStr.append(" turma.identificadorturma as identificadorturma ");
		}else{
			sqlStr.append(" 0 as turma_codigo, ");
			sqlStr.append(" '' as identificadorturma ");
		}
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW ");
		sqlStr.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa respondente on respondente.codigo = RespostaAvaliacaoInstitucionalDW.pessoa");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.professor = pessoa.codigo");
		sqlStr.append(" left join pessoa coordenador on coordenador.codigo = RespostaAvaliacaoInstitucionalDW.coordenador ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");
		sqlStr.append(" left join cargo on cargo.codigo = RespostaAvaliacaoInstitucionalDW.cargo ");
		sqlStr.append(" left join departamento on departamento.codigo = RespostaAvaliacaoInstitucionalDW.departamento ");
		sqlStr.append(" left join turma on turma.codigo = RespostaAvaliacaoInstitucionalDW.turma ");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.getCodigo().intValue());
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::DATE >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			sqlStr.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::DATE <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (relatorioTextual != null && !relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
		} else if (relatorioTextual != null && relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta = 'TE' ");
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario != null && questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		String andOr = "and ( ";
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.disciplina= " + disciplinaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!disciplinaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PerguntaVO perguntaVO : perguntaVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pergunta = " + perguntaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!perguntaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr = "and (";
		for (PessoaVO pessoaVO : listaProfessor) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.professor = " + pessoaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!listaProfessor.isEmpty()) {
			sqlStr.append(" ) ");
		}		
		andOr = "and (";
		for (PessoaVO pessoaVO : listaRespondentes) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.pessoa = " + pessoaVO.getCodigo().intValue());
			andOr = "or";
		}
		if (!listaRespondentes.isEmpty()) {
			sqlStr.append(" ) ");
		}		

		sqlStr.append(" group by questionario_escopo, ");
		sqlStr.append(" UnidadeEnsino_codigo, UnidadeEnsino_nome, ");
		sqlStr.append(" AvaliacaoInstitucional_nome, questionario_descricao, ");
		sqlStr.append(" Questionario.codigo, questionario_descricao, ");
	
		sqlStr.append(" departamento.codigo, departamento.nome ");
			sqlStr.append(", RespostaAvaliacaoInstitucionalDW.curso, curso.nome ");
			if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
		}
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(", turma.codigo, identificadorturma ");
		}
		if(relatorioRespondente){
			sqlStr.append(", disciplina.codigo, disciplina.nome, cargo.codigo, cargo.nome");
		}

		sqlStr.append(" union all ");	
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			sqlStr.append(" select count('P'||avaliacaoitem.codigo::VARCHAR||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) as totalPessoa, ");
			sqlStr.append(" count(distinct 'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) as qtdeRelatorio, ");
		}else if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			sqlStr.append(" select count('P'||avaliacaoitem.codigo::VARCHAR||'D'||disciplina.codigo::varchar) as totalPessoa, ");
			sqlStr.append(" count(distinct 'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar) as qtdeRelatorio, ");
		}else{
			sqlStr.append(" select count('P'||avaliacaoitem.codigo::VARCHAR||'D0') as totalPessoa, ");
			sqlStr.append(" count(distinct pessoa.codigo) as qtdeRelatorio, ");
		}
		sqlStr.append(" avaliacaoitem.questionario,  ");
		sqlStr.append(" questionario.escopo as questionario_escopo, avaliacao.unidadeensino as unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome as unidadeensino_nome, avaliacaoinstitucional.nome as avaliacaoinstitucional_nome, ");
		if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" avaliacao.curso, curso.nome AS curso_nome, ");
		}else{
			sqlStr.append(" 0 as curso, '' as curso_nome, ");
		}
		if(relatorioRespondente){
			sqlStr.append(" disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, 0 as cargo_codigo, '' as cargo_nome,");
		}else{
			sqlStr.append(" 0 as disciplina_codigo, '' as disciplina_nome, 0 as cargo_codigo, '' as cargo_nome, ");
		}
		sqlStr.append(" questionario.descricao as questionario_descricao, ");
		sqlStr.append(" 0, '', ");
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" turma.codigo as turma_codigo, ");
			sqlStr.append(" turma.identificadorturma as identificadorturma ");
		}else{
			sqlStr.append(" 0 as turma_codigo, ");
			sqlStr.append(" '' as identificadorturma ");
		}
		sqlStr.append(" from avaliacaoinstitucionalpresencialresposta avaliacao ");
		sqlStr.append(" inner join avaliacaoinstitucionalpresencialitemresposta avaliacaoitem on avaliacaoitem.avaliacaoinstitucionalpresencialresposta = avaliacao.codigo ");
		sqlStr.append(" left join respostapergunta on respostapergunta.codigo = avaliacaoitem.respostapergunta ");
		sqlStr.append(" inner join questionario on questionario.codigo = avaliacaoitem.questionario ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = avaliacao.unidadeensino ");
		sqlStr.append(" inner join avaliacaoinstitucional on avaliacaoinstitucional.codigo = avaliacao.avaliacaoinstitucional ");
		sqlStr.append(" inner join pergunta on pergunta.codigo = avaliacaoitem.pergunta ");
		sqlStr.append(" inner join curso on curso.codigo = avaliacao.curso ");
		sqlStr.append(" left join turma on turma.codigo = avaliacao.turma ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = avaliacao.disciplina ");
		sqlStr.append(" left join pessoa pessoa on pessoa.codigo = avaliacao.professor ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");

		sqlStr.append(" where avaliacao.avaliacaoinstitucional = " + avaliacaoInst.getCodigo().intValue());
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" AND respostapergunta.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			sqlStr.append(" AND respostapergunta.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}
		if (relatorioTextual != null && !relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta <> 'TE' ");
		} else if (relatorioTextual != null && relatorioTextual) {
			sqlStr.append(" and Pergunta.tipoResposta = 'TE' ");
		}
		if (curso.intValue() != 0) {
			sqlStr.append(" and curso.codigo = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and turma.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and turma.codigo = " + turma.intValue());
		}
		if (questionario != null && questionario.intValue() != 0) {
			sqlStr.append(" and questionario.codigo = " + questionario.intValue());
		}
		String andOr1 = "and ( ";
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			sqlStr.append(" " + andOr1 + "  disciplina.codigo = " + disciplinaVO.getCodigo().intValue());
			andOr1 = "or";
		}
		if (!disciplinaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr1 = "and (";
		for (PerguntaVO perguntaVO : perguntaVOs) {
			sqlStr.append(" " + andOr1 + "  pergunta.codigo = " + perguntaVO.getCodigo().intValue());
			andOr1 = "or";
		}
		if (!perguntaVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		andOr1 = "and (";
		for (PessoaVO pessoaVO : listaProfessor) {
			sqlStr.append(" " + andOr1 + "  pessoa.codigo = " + pessoaVO.getCodigo().intValue());
			andOr1 = "or";
		}
		if (!listaProfessor.isEmpty()) {
			sqlStr.append(" ) ");
		}
		sqlStr.append(" group by avaliacaoitem.questionario,  questionario.escopo,  ");
		sqlStr.append(" avaliacao.unidadeensino, avaliacaoinstitucional.nome, questionario.descricao,  ");
		if(relatorioRespondente){
			sqlStr.append(" disciplina.codigo, disciplina.nome, ");
		}
		if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" avaliacao.curso, curso.nome, ");
		}
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" turma_codigo, identificadorturma, ");
		}
		sqlStr.append("  unidadeensino.nome ) as t ");
		sqlStr.append(" group by questionario, curso, ");
		sqlStr.append(" questionario_escopo, UnidadeEnsino_codigo,  UnidadeEnsino_nome, AvaliacaoInstitucional_nome, ");
		sqlStr.append(" questionario_descricao, disciplina_codigo, disciplina_nome, turma_codigo, identificadorturma, ");
		sqlStr.append(" curso_nome, turma_codigo, identificadorturma, ");				
		sqlStr.append(" departamento_codigo, departamento_nome, cargo_codigo, cargo_nome, turma_codigo, identificadorTurma  ");
		sqlStr.append(" order by UnidadeEnsino_nome, curso_nome, identificadorTurma, departamento_nome, disciplina_nome, cargo_nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaQuantidadeRelatorio(relatorioRespondente, tabelaResultado, unidadeEnsino, avaliacaoInstucionalRelVO);

	}
	
	private List<QuestionarioRelVO> montarDadosConsultaQuantidadeRelatorio(Boolean relatorioRespondente, SqlRowSet tabelaResultado, Integer unidadeEnsino, AvaliacaoInstucionalRelVO obj) throws Exception {
		List<QuestionarioRelVO> questionarioRelVOs = new ArrayList<QuestionarioRelVO>(0);
		while (tabelaResultado.next()) {
			obj.setNome(tabelaResultado.getString("AvaliacaoInstitucional_nome"));
			Integer codigoQuestionario = tabelaResultado.getInt("questionario");
			Integer codigoUnidadeEnsino = tabelaResultado.getInt("UnidadeEnsino_codigo");
			Integer codigoCurso = tabelaResultado.getInt("curso");						
			Integer codigoDepartamento = tabelaResultado.getInt("departamento_codigo");
			Integer codigoDisciplina = tabelaResultado.getInt("disciplina_codigo");						
			Integer codigoCargo = tabelaResultado.getInt("cargo_codigo");
			Integer codigoTurma = tabelaResultado.getInt("turma_codigo");
			String escopo = tabelaResultado.getString("questionario_escopo");			
			QuestionarioRelVO questionarioRelVO = obj.consultaQuestionarioRelVOs(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO, unidadeEnsino, codigoQuestionario, codigoUnidadeEnsino, codigoCurso, codigoDisciplina, 0, escopo, 0, codigoCargo, codigoDepartamento, codigoTurma);
			questionarioRelVO.setEscopo(escopo);
			if (questionarioRelVO.getCodigo().intValue() == 0) {
				questionarioRelVO.setNomeCurso(tabelaResultado.getString("curso_nome"));						
				questionarioRelVO.setNomeUnidadeEnsino(tabelaResultado.getString("unidadeEnsino_nome"));
				questionarioRelVO.setNome(tabelaResultado.getString("questionario_descricao"));
				questionarioRelVO.setCodigoCurso(tabelaResultado.getInt("curso"));							
				questionarioRelVO.setCodigoUnidadeEnsino(tabelaResultado.getInt("UnidadeEnsino_codigo"));
				questionarioRelVO.setCodigo(tabelaResultado.getInt("questionario"));
				questionarioRelVO.setEscopo(tabelaResultado.getString("questionario_escopo"));
				questionarioRelVO.getDepartamento().setCodigo(tabelaResultado.getInt("departamento_codigo"));
				questionarioRelVO.getDepartamento().setNome(tabelaResultado.getString("departamento_nome"));
				questionarioRelVO.setCodigoDisciplina(tabelaResultado.getInt("disciplina_codigo"));
				questionarioRelVO.setNomeDisciplina(tabelaResultado.getString("disciplina_nome"));
				questionarioRelVO.getCargo().setCodigo(tabelaResultado.getInt("cargo_codigo"));
				questionarioRelVO.getCargo().setNome(tabelaResultado.getString("cargo_nome"));
				questionarioRelVO.setQtdeRelatorio(tabelaResultado.getInt("qtdeRelatorio"));
				questionarioRelVO.setQtdeRespondentes(tabelaResultado.getInt("totalPessoa"));
				questionarioRelVO.setCodigoTurma(tabelaResultado.getInt("turma_codigo"));
				questionarioRelVO.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
				questionarioRelVOs.add(questionarioRelVO);
			}			
		}
		return questionarioRelVOs;
	}
	
	@Override
	public List<DisciplinaVO> consultarDisciplinas(Integer avaliacaoInst, Integer questionario, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" inner join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.pessoa = pessoa.codigo");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		sqlStr.append(" and pessoa.codigo is not null and pessoa.codigo <> 0 ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}

		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}
		if (turma.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + turma.intValue());
		}
		if (questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}		
		sqlStr.append(" group by disciplina.codigo , disciplina.nome order by disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<DisciplinaVO> listaDisciplinas = new ArrayList<DisciplinaVO>(0);
		while (tabelaResultado.next()) {
			DisciplinaVO disciplinaVO = new DisciplinaVO();
			disciplinaVO.setCodigo(tabelaResultado.getInt("disciplina_codigo"));
			disciplinaVO.setNome(tabelaResultado.getString("disciplina_nome"));
			listaDisciplinas.add(disciplinaVO);
		}
		return listaDisciplinas;

	}
	
	@Override
	public List<TurmaVO> consultarTurmas(String tipoConsulta, String valorConsulta, Integer avaliacaoInst, Integer questionario, Integer unidadeEnsino, Integer curso, Integer turno) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select turma.codigo as turma_codigo, turma.identificadorTurma as turma_nome");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" inner join turma on turma.codigo = RespostaAvaliacaoInstitucionalDW.turma");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.pessoa = pessoa.codigo");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		sqlStr.append(" and pessoa.codigo is not null and pessoa.codigo <> 0 ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}

		if (curso.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.curso = " + curso.intValue());
		}
		if (turno.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turno = " + turno.intValue());
		}		
		if (questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		if(tipoConsulta.equals("codigo")){
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.turma = " + valorConsulta);
		}else{
			sqlStr.append(" and sem_acentos(turma.identificadorTurma) ilike sem_acentos('" + valorConsulta+"%') ");
		}
		sqlStr.append(" group by turma.codigo , turma.identificadorTurma order by turma.identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> listaTurmas = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO turmaVO = new TurmaVO();
			turmaVO.setCodigo(tabelaResultado.getInt("turma_codigo"));
			turmaVO.setIdentificadorTurma(tabelaResultado.getString("turma_nome"));
			listaTurmas.add(turmaVO);
		}
		return listaTurmas;

	}
	
	
	@Override
	public List<CursoVO> consultarCursos(String tipoConsulta, String valorConsulta, Integer avaliacaoInst, Integer questionario, Integer unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select Curso.codigo as curso_codigo, Curso.nome as curso_nome");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");		
		sqlStr.append(" inner join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.pessoa = pessoa.codigo");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.intValue());
		sqlStr.append(" and pessoa.codigo is not null and pessoa.codigo <> 0 ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino.intValue());
		}		
		if (questionario.intValue() != 0) {
			sqlStr.append(" and RespostaAvaliacaoInstitucionalDW.questionario = " + questionario.intValue());
		}
		if(tipoConsulta.equals("codigo")){
			sqlStr.append(" and Curso.codigo = " + valorConsulta);
		}else{
			sqlStr.append(" and sem_acentos(Curso.nome) ilike sem_acentos('" + valorConsulta+"%') ");
		}
		sqlStr.append(" group by curso.codigo , curso.nome order by UnidadeEnsino.nome, curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<CursoVO> listaCursos = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			CursoVO cursoVO = new CursoVO();
			cursoVO.setCodigo(tabelaResultado.getInt("Curso_codigo"));
			cursoVO.setNome(tabelaResultado.getString("Curso_nome"));
			listaCursos.add(cursoVO);
		}
		return listaCursos;

	}


	public StringBuilder getSqlWhereGerarRelatorioPublicacao(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception{
		StringBuilder sql =  new StringBuilder("");
		if(gerarRelatorioPublicacao){			
			if(usuarioVO.getIsApresentarVisaoProfessor()){	
				if(!avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE") && 
						(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CURSO)						
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TURMA))){
					sql.append(" and RespostaAvaliacaoInstitucionalDW.professor =  ").append(usuarioVO.getPessoa().getCodigo());
				}else if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE") && 
						(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TURMA))){
					sql.append(" and exists (");
					sql.append(" select horarioturma.codigo from horarioturma ");
					sql.append(" inner join turma tur on tur.codigo = horarioturma.turma ");
					sql.append(" 	inner join curso as cur on (cur.codigo = tur.curso or (tur.turmaagrupada and cur.codigo in (select turma2.curso from turmaagrupada  ");
					sql.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = tur.codigo )) ");
					sql.append(" 	or (tur.subturma and cur.codigo in (select turma3.curso from turma turma3  where turma3.codigo = tur.turmaprincipal ))) ");
					sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
					sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
					sql.append(" where tur.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
					sql.append(" and horarioturmadiaitem.professor = ").append(usuarioVO.getPessoa().getCodigo());
					if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getCurso().getCodigo())){
						sql.append(" and cur.codigo =  ").append(avaliacaoInstitucionalVO.getCurso().getCodigo());
					}
					if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma().getCodigo())){
						sql.append(" and tur.codigo =  ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
					}
					if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())){
						sql.append(" and cur.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
					}
					if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getAno())){
						sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
					}
					if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getSemestre())){
						sql.append(" and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
					}
					if(!Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getAno()) && !Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getSemestre())){
						sql.append(" and horarioturmadia.data >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' ");
						sql.append(" and horarioturmadia.data <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
					}								
					sql.append(" limit 1) ");
				}else if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE") && 
						(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR))){
					sql.append(" and RespostaAvaliacaoInstitucionalDW.professor =  ").append(usuarioVO.getPessoa().getCodigo());
				}else if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES)){
					List<QuestionarioVO> questionarioVOs = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(avaliacaoInstitucionalVO, usuarioVO, matriculaVO, new MatriculaPeriodoVO(), true);
					sql.append(" and respostaavaliacaoinstitucionaldw.coordenador != ").append(usuarioVO.getPessoa().getCodigo());
					sql.append(" and respostaavaliacaoinstitucionaldw.coordenador is not null and respostaavaliacaoinstitucionaldw.coordenador > 0 and respostaavaliacaoinstitucionaldw.coordenador in (0");
					for(QuestionarioVO questionarioVO: questionarioVOs){
						sql.append(",").append(questionarioVO.getCoordenador().getCodigo());
					}
					sql.append(") ");
				}else if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR) || avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES)){
					sql.append(" ");
				}else {
					sql.append(" and true = false ");
				}
			} else if(usuarioVO.getIsApresentarVisaoCoordenador()){
				if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR) 
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES)){
					sql.append(" and RespostaAvaliacaoInstitucionalDW.coordenador =  ").append(usuarioVO.getPessoa().getCodigo());
				}else if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR) || avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO)){
					sql.append(" and RespostaAvaliacaoInstitucionalDW.unidadeensino =  avaliacaoInstitucional.unidadeensino ");
					sql.append(" and exists (select cursocoordenador.codigo from cursocoordenador ");
					sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
					sql.append(" where cursocoordenador.unidadeensino = avaliacaoInstitucional.unidadeensino  ");
					sql.append(" and funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
					sql.append(" limit 1) ");
				}else if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES) || avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO) || avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES)){
					sql.append(" and RespostaAvaliacaoInstitucionalDW.unidadeensino =  avaliacaoInstitucional.unidadeensino ");
					sql.append(" and exists ( ");
					sql.append(" select cursocoordenador.codigo from cursocoordenador ");
					sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
					sql.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
					sql.append(" where cursocoordenador.unidadeEnsino = avaliacaoinstitucional.unidadeensino  "); 
					sql.append(" and funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
					sql.append(" limit 1)");
				}else if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TURMA)){	
					List<CursoCoordenadorVO> cursoCoordenadorVOs = getFacadeFactory().getCursoCoordenadorFacade().consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(usuarioVO.getPessoa().getCodigo(), avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo(), avaliacaoInstitucionalVO.getNivelEducacional(), avaliacaoInstitucionalVO.getCurso().getCodigo(), avaliacaoInstitucionalVO.getTurma().getCodigo(), Boolean.FALSE, usuarioVO);
					if(!cursoCoordenadorVOs.isEmpty()){
						String andOr = " and ( ";
						for(CursoCoordenadorVO cursoCoordenadorVO: cursoCoordenadorVOs){
							if(!avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA)){
								sql.append(andOr).append(" (RespostaAvaliacaoInstitucionalDW.curso = ").append(cursoCoordenadorVO.getCurso().getCodigo());
								sql.append(" and RespostaAvaliacaoInstitucionalDW.unidadeensino = ").append(cursoCoordenadorVO.getUnidadeEnsino().getCodigo());
								if(Uteis.isAtributoPreenchido(cursoCoordenadorVO.getTurma().getCodigo())){
									sql.append(" and RespostaAvaliacaoInstitucionalDW.turma = ").append(cursoCoordenadorVO.getTurma().getCodigo());
								}
								sql.append(") ");
							}else{
								sql.append(andOr).append(" ( exists (");
								sql.append(" select turma.codigo from turma where turma.curso = ").append(cursoCoordenadorVO.getCurso().getCodigo());
								sql.append(" and turma.unidadeensino = ").append(cursoCoordenadorVO.getUnidadeEnsino().getCodigo());
								sql.append(" and RespostaAvaliacaoInstitucionalDW.turma = turma.codigo ");
								sql.append(" union all ");
								sql.append(" select turma.codigo from turma inner join turmaagrupada on turmaagrupada.turma = turma.codigo where turma.curso = ").append(cursoCoordenadorVO.getCurso().getCodigo());
								sql.append(" and turma.unidadeensino = ").append(cursoCoordenadorVO.getUnidadeEnsino().getCodigo());
								sql.append(" and RespostaAvaliacaoInstitucionalDW.turma = turmaagrupada.turmaorigem ");
								sql.append(" ) ");
								sql.append(" and RespostaAvaliacaoInstitucionalDW.unidadeensino = ").append(cursoCoordenadorVO.getUnidadeEnsino().getCodigo());
								if(Uteis.isAtributoPreenchido(cursoCoordenadorVO.getTurma().getCodigo())){
									sql.append(" and RespostaAvaliacaoInstitucionalDW.turma = ").append(cursoCoordenadorVO.getTurma().getCodigo());
								}
								sql.append(") ");
							}
							andOr = " or ";						
						}
						sql.append(" ) ");
					}else{
						sql.append(" and false = true ");
					}
				}else {
					sql.append(" and false = true ");
				}
				
			} else if(usuarioVO.getIsApresentarVisaoAdministrativa()){
				if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO) 
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO)){
					sql.append(" and respostaavaliacaoinstitucionaldw.cargo in (select funcionariocargo.cargo from  funcionariocargo ");
					sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
					sql.append(" where funcionariocargo.ativo and funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
					sql.append(" and funcionariocargo.unidadeEnsino = avaliacaoinstitucional.unidadeEnsino ) ");
				}else if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO) 
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO)){					
					sql.append(" and respostaavaliacaoinstitucionaldw.departamento in (select cargo.departamento from  funcionariocargo ");
					sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
					sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo  ");
					sql.append(" where funcionariocargo.ativo and funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
					sql.append(" and funcionariocargo.unidadeEnsino = avaliacaoinstitucional.unidadeEnsino ) ");
				}
			} else if(usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais()){
				if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("DI") || avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("UM")){
					sql.append(" and respostaavaliacaoinstitucionaldw.curso =  ").append(matriculaVO.getCurso().getCodigo());
					sql.append(" and avaliacaoinstitucional.unidadeensino =  ").append(matriculaVO.getUnidadeEnsino().getCodigo());
					MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(matriculaVO.getMatricula(), avaliacaoInstitucionalVO.getAno(), avaliacaoInstitucionalVO.getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, "");
					List<QuestionarioVO> questionarioVOs = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(avaliacaoInstitucionalVO, usuarioVO, matriculaVO, matriculaPeriodoVO, true);					
					String andOr = " and ( ";
					for(QuestionarioVO questionarioVO : questionarioVOs){
						sql.append(andOr).append(" (respostaavaliacaoinstitucionaldw.disciplina  = ").append(questionarioVO.getDisciplinaVO().getCodigo());
						sql.append(" and respostaavaliacaoinstitucionaldw.professor  = ").append(questionarioVO.getProfessor().getCodigo());
						sql.append(" ) ");
						andOr = " or ";				
					}
					if(!questionarioVOs.isEmpty()){
						sql.append(" ) ");
					}else{
						/**
						 * para garantir que não seja apresentado nada para o aluno caso não enconte mais o questionario
						 */
						sql.append(" and true = false ");
					}
				}else if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE")){
					sql.append(" and avaliacaoinstitucional.unidadeensino =  ").append(matriculaVO.getUnidadeEnsino().getCodigo());
					sql.append(" and respostaavaliacaoinstitucionaldw.curso =  ").append(matriculaVO.getCurso().getCodigo());
				}
			}
		}
		return sql;
	}
	
	public StringBuilder getSqlWhereGerarRelatorioPublicacaoRespostaManual(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception{
		StringBuilder sql =  new StringBuilder("");
		if(gerarRelatorioPublicacao){
			if(usuarioVO.getIsApresentarVisaoProfessor()){				
				sql.append(" and respostaavaliacaoinstitucionaldw.professor =  ").append(usuarioVO.getPessoa().getCodigo());
			}
			if(usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais()){
					if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("DI") 
							|| avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("UM")){
						List<QuestionarioVO> questionarioVOs = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(avaliacaoInstitucionalVO, usuarioVO, matriculaVO, matriculaVO.getUltimoMatriculaPeriodoVO(), true);					
						String andOr = " and ( ";
						for(QuestionarioVO questionarioVO : questionarioVOs){
							sql.append(andOr).append(" (respostaavaliacaoinstitucionaldw.disciplina  = ").append(questionarioVO.getDisciplinaVO().getCodigo());
							sql.append(" and respostaavaliacaoinstitucionaldw.professor  = ").append(questionarioVO.getProfessor().getCodigo());
							sql.append(" ) ");
							andOr = " or ";				
						}
						sql.append(" ) ");
					}else if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE")){
						sql.append(" and avaliacaoinstitucional.unidadeensino =  ").append(matriculaVO.getUnidadeEnsino().getCodigo());
					}
			}			
		}
		return sql;
	}
	
	@Override
	public List<QuestionarioRelVO> consultarQuantidadeRelatorioSerGeradoVisaoCoordenador(Boolean relatorioRespondente,int unidadeEnsino , AvaliacaoInstitucionalVO avaliacaoInst, NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento,  List<CursoCoordenadorVO> CursoCoordenadorVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select sum(totalPessoa) as totalPessoa, sum(qtdeRelatorio) as qtdeRelatorio,  ");
		sqlStr.append(" questionario, curso, curso_nome, disciplina_codigo, disciplina_nome, cargo_codigo, cargo_nome, ");		
		sqlStr.append(" questionario_escopo, UnidadeEnsino_codigo,  UnidadeEnsino_nome, AvaliacaoInstitucional_nome, ");		
		sqlStr.append(" questionario_descricao, ");				
		sqlStr.append(" departamento_codigo, departamento_nome, turma_codigo, identificadorTurma ");
		sqlStr.append(" from ( ");
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			if(getIsApresentarProfessor(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct  'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) qtdeRelatorio, ");
				}else{
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct  'P'||pessoa.codigo::varchar||'T'||turma.codigo::varchar) qtdeRelatorio, ");
				}
			}else{
				if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct  'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) qtdeRelatorio, ");
				}else{
					sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'T'||turma.codigo::varchar)) as totalPessoa, ");
					sqlStr.append(" count(distinct turma.codigo) qtdeRelatorio, ");
				}
			}
		}else if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D'||disciplina.codigo::varchar)) as totalPessoa, ");
			if(getIsApresentarProfessor(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				sqlStr.append(" count(distinct  'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar) qtdeRelatorio, ");
			}else{
				sqlStr.append(" count(distinct  disciplina.codigo) qtdeRelatorio, ");
			}
		}else{
			sqlStr.append(" select count(distinct (('R'||case when RespostaAvaliacaoInstitucionalDW.tipopessoa = 'AL' then matriculaaluno else respondente.codigo::VARCHAR end)||'D0')) as totalPessoa, ");
			if(getIsApresentarProfessor(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				sqlStr.append(" count(distinct  pessoa.codigo) qtdeRelatorio, ");
			}else if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
				sqlStr.append(" count(distinct  curso.codigo) qtdeRelatorio, ");
			}else{
				sqlStr.append(" count(distinct  UnidadeEnsino.codigo) qtdeRelatorio, ");
			}
		}
		sqlStr.append(" Questionario.codigo as questionario,  ");
		sqlStr.append(" Questionario.escopo as questionario_escopo, UnidadeEnsino.codigo as UnidadeEnsino_codigo,  UnidadeEnsino.nome as UnidadeEnsino_nome, AvaliacaoInstitucional.nome as AvaliacaoInstitucional_nome, ");
		if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" RespostaAvaliacaoInstitucionalDW.curso as curso, curso.nome as curso_nome, ");
		}else{
			sqlStr.append(" 0 as curso, '' as curso_nome, ");
		}
		if(relatorioRespondente){
			sqlStr.append(" disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, cargo.codigo as cargo_codigo, cargo.nome as cargo_nome,");
		}else{
			sqlStr.append(" 0 as disciplina_codigo, '' as disciplina_nome, 0 as cargo_codigo, '' as cargo_nome, ");
		}
		sqlStr.append(" questionario.descricao as questionario_descricao, ");	
		sqlStr.append(" departamento.codigo as departamento_codigo, departamento.nome as departamento_nome,  ");
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" turma.codigo as turma_codigo, ");
			sqlStr.append(" turma.identificadorturma as identificadorturma ");
		}else{
			sqlStr.append(" 0 as turma_codigo, ");
			sqlStr.append(" '' as identificadorturma ");
		}
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW ");
		sqlStr.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino");
		sqlStr.append(" left join Curso on Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional");
		sqlStr.append(" left join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" left join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" left join Pessoa respondente on respondente.codigo = RespostaAvaliacaoInstitucionalDW.pessoa");
		sqlStr.append(" left join Pessoa on RespostaAvaliacaoInstitucionalDW.professor = pessoa.codigo");
		sqlStr.append(" left join pessoa coordenador on coordenador.codigo = RespostaAvaliacaoInstitucionalDW.coordenador ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");
		sqlStr.append(" left join cargo on cargo.codigo = RespostaAvaliacaoInstitucionalDW.cargo ");
		sqlStr.append(" left join departamento on departamento.codigo = RespostaAvaliacaoInstitucionalDW.departamento ");
		sqlStr.append(" left join turma on turma.codigo = RespostaAvaliacaoInstitucionalDW.turma ");
		sqlStr.append(" where  avaliacaoInstitucional = " + avaliacaoInst.getCodigo().intValue());
		
		String andOr = "and ( ";
		for (CursoCoordenadorVO obj : CursoCoordenadorVOs) {
			sqlStr.append(" " + andOr + "  RespostaAvaliacaoInstitucionalDW.curso= " + obj.getCurso().getCodigo());
			andOr = "or";
		}
		if (!CursoCoordenadorVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		

		sqlStr.append(" group by questionario_escopo, ");
		sqlStr.append(" UnidadeEnsino_codigo, UnidadeEnsino_nome, ");
		sqlStr.append(" AvaliacaoInstitucional_nome, questionario_descricao, ");
		sqlStr.append(" Questionario.codigo, questionario_descricao, ");
		sqlStr.append(" departamento.codigo, departamento.nome ");
			sqlStr.append(", RespostaAvaliacaoInstitucionalDW.curso, curso.nome ");
			if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
		}
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(", turma.codigo, identificadorturma ");
		}
		if(relatorioRespondente){
			sqlStr.append(", disciplina.codigo, disciplina.nome, cargo.codigo, cargo.nome");
		}

		sqlStr.append(" union all ");	
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			sqlStr.append(" select count('P'||avaliacaoitem.codigo::VARCHAR||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) as totalPessoa, ");
			sqlStr.append(" count(distinct 'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar||'T'||turma.codigo::varchar) as qtdeRelatorio, ");
		}else if(getIsApresentarDisciplina(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum()) || relatorioRespondente){
			sqlStr.append(" select count('P'||avaliacaoitem.codigo::VARCHAR||'D'||disciplina.codigo::varchar) as totalPessoa, ");
			sqlStr.append(" count(distinct 'P'||pessoa.codigo::varchar||'D'||disciplina.codigo::varchar) as qtdeRelatorio, ");
		}else{
			sqlStr.append(" select count('P'||avaliacaoitem.codigo::VARCHAR||'D0') as totalPessoa, ");
			sqlStr.append(" count(distinct pessoa.codigo) as qtdeRelatorio, ");
		}
		sqlStr.append(" avaliacaoitem.questionario,  ");
		sqlStr.append(" questionario.escopo as questionario_escopo, avaliacao.unidadeensino as unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome as unidadeensino_nome, avaliacaoinstitucional.nome as avaliacaoinstitucional_nome, ");
		if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" avaliacao.curso, curso.nome AS curso_nome, ");
		}else{
			sqlStr.append(" 0 as curso, '' as curso_nome, ");
		}
		if(relatorioRespondente){
			sqlStr.append(" disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, 0 as cargo_codigo, '' as cargo_nome,");
		}else{
			sqlStr.append(" 0 as disciplina_codigo, '' as disciplina_nome, 0 as cargo_codigo, '' as cargo_nome, ");
		}
		sqlStr.append(" questionario.descricao as questionario_descricao, ");
		sqlStr.append(" 0, '', ");
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" turma.codigo as turma_codigo, ");
			sqlStr.append(" turma.identificadorturma as identificadorturma ");
		}else{
			sqlStr.append(" 0 as turma_codigo, ");
			sqlStr.append(" '' as identificadorturma ");
		}
		sqlStr.append(" from avaliacaoinstitucionalpresencialresposta avaliacao ");
		sqlStr.append(" inner join avaliacaoinstitucionalpresencialitemresposta avaliacaoitem on avaliacaoitem.avaliacaoinstitucionalpresencialresposta = avaliacao.codigo ");
		sqlStr.append(" left join respostapergunta on respostapergunta.codigo = avaliacaoitem.respostapergunta ");
		sqlStr.append(" inner join questionario on questionario.codigo = avaliacaoitem.questionario ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = avaliacao.unidadeensino ");
		sqlStr.append(" inner join avaliacaoinstitucional on avaliacaoinstitucional.codigo = avaliacao.avaliacaoinstitucional ");
		sqlStr.append(" inner join pergunta on pergunta.codigo = avaliacaoitem.pergunta ");
		sqlStr.append(" inner join curso on curso.codigo = avaliacao.curso ");
		sqlStr.append(" left join turma on turma.codigo = avaliacao.turma ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = avaliacao.disciplina ");
		sqlStr.append(" left join pessoa pessoa on pessoa.codigo = avaliacao.professor ");
		sqlStr.append(" left join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");

		sqlStr.append(" where avaliacao.avaliacaoinstitucional = " + avaliacaoInst.getCodigo().intValue());
		
		String andOr1 = "and ( ";
		for (CursoCoordenadorVO obj : CursoCoordenadorVOs) {
			sqlStr.append(" " + andOr1 + "  curso.codigo = " + obj.getCurso().getCodigo());
			andOr1 = "or";
		}
		if (!CursoCoordenadorVOs.isEmpty()) {
			sqlStr.append(" ) ");
		}
		
		sqlStr.append(" group by avaliacaoitem.questionario,  questionario.escopo,  ");
		sqlStr.append(" avaliacao.unidadeensino, avaliacaoinstitucional.nome, questionario.descricao,  ");
		if(relatorioRespondente){
			sqlStr.append(" disciplina.codigo, disciplina.nome, ");
		}
		if(getIsApresentarCurso(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" avaliacao.curso, curso.nome, ");
		}
		if(getIsApresentarTurma(nivelDetalhamento, avaliacaoInst.getPublicarAlvoEnum())){
			sqlStr.append(" turma_codigo, identificadorturma, ");
		}
		sqlStr.append("  unidadeensino.nome ) as t ");
		sqlStr.append(" group by questionario, curso, ");
		sqlStr.append(" questionario_escopo, UnidadeEnsino_codigo,  UnidadeEnsino_nome, AvaliacaoInstitucional_nome, ");
		sqlStr.append(" questionario_descricao, disciplina_codigo, disciplina_nome, turma_codigo, identificadorturma, ");
		sqlStr.append(" curso_nome, turma_codigo, identificadorturma, ");				
		sqlStr.append(" departamento_codigo, departamento_nome, cargo_codigo, cargo_nome, turma_codigo, identificadorTurma  ");
		sqlStr.append(" order by UnidadeEnsino_nome, curso_nome, identificadorTurma, departamento_nome, disciplina_nome, cargo_nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaQuantidadeRelatorio(relatorioRespondente, tabelaResultado, unidadeEnsino, avaliacaoInstucionalRelVO);

	}

	@SuppressWarnings("unlikely-arg-type")
	private void validarPerguntasTextuais(List<PerguntaVO> perguntaVOs) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(perguntaVOs) && (perguntaVOs.stream().noneMatch(PerguntaVO::getTipoRespostaTextual))) {
			for (PerguntaVO perguntaVO : perguntaVOs) {
				if (Uteis.isAtributoPreenchido(perguntaVO.getRespostaPerguntaVOs())) {
					if (perguntaVO.getRespostaPerguntaVOs().stream().anyMatch(rep -> rep.getApresentarRespostaAdicional())) {
						break;
					} else if (perguntaVO.getCodigo().equals(perguntaVOs.get(perguntaVOs.size() -1))) {
						throw new ConsistirException("Não existem respostas do tipo TEXTUAL para a(s) pergunta(s) informada(s).");
					}
				} else {
					throw new ConsistirException("Não existem respostas do tipo TEXTUAL para a(s) pergunta(s) informada(s).");
				}
			}
		}
	}

	@Override
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoComboBoxPorAvaliacaoInstitucionalPorQuestionario( Integer avaliacaoInst, Integer questionario, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT	DISTINCT unidadeensino.codigo AS unidadeensino_codigo,	unidadeensino.nome AS unidadeensino_nome ");
		sqlStr.append(" FROM	AvaliacaoInstitucional LEFT JOIN avaliacaoinstitucionalrespondente ON	avaliacaoinstitucionalrespondente.avaliacaoInstitucional = AvaliacaoInstitucional.codigo ");
		sqlStr.append(" left JOIN avaliacaoinstitucionalunidadeensino ON	avaliacaoinstitucionalunidadeensino.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
		sqlStr.append(" LEFT JOIN UnidadeEnsino ON	UnidadeEnsino.codigo = avaliacaoinstitucionalunidadeensino.unidadeEnsino ");
		sqlStr.append(" left join Questionario on Questionario.codigo = AvaliacaoInstitucional.questionario ");
		sqlStr.append(" where  avaliacaoinstitucional.codigo = " + avaliacaoInst.intValue());
		if (questionario.intValue() != 0) {
			sqlStr.append(" and AvaliacaoInstitucional.questionario = " + questionario.intValue());
		}
		sqlStr.append(" group by unidadeensino.codigo , unidadeensino.nome order by unidadeensino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UnidadeEnsinoVO> vetResultado = new ArrayList<UnidadeEnsinoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(tabelaResultado.getInt("unidadeensino_codigo"));
			obj.setNome(tabelaResultado.getInt("unidadeensino_codigo") + " - " + tabelaResultado.getString("unidadeensino_nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<CursoVO> consultarCursosComboBoxPorAvaliacaoInstitucionalPorQuestionario(String tipoConsulta, String valorConsulta, Integer avaliacaoInst, Integer questionario, Integer unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select DISTINCT Curso.codigo as curso_codigo, Curso.nome as curso_nome");
		sqlStr.append(" from avaliacaoinstitucionalrespondente");
		sqlStr.append(" left join AvaliacaoInstitucional on AvaliacaoInstitucional.codigo = avaliacaoinstitucionalrespondente.avaliacaoInstitucional");
		sqlStr.append(" INNER JOIN avaliacaoinstitucionalcurso ON avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucionalrespondente.avaliacaoinstitucional");
		sqlStr.append(" inner JOIN avaliacaoinstitucionalunidadeensino ON avaliacaoinstitucionalunidadeensino.avaliacaoinstitucional = avaliacaoinstitucional.codigo");
		sqlStr.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = avaliacaoinstitucionalunidadeensino.unidadeEnsino");
		sqlStr.append(" inner join Curso on Curso.codigo = avaliacaoinstitucionalcurso.curso");
		sqlStr.append(" left join Disciplina on Disciplina.codigo = avaliacaoinstitucional.disciplina");
		sqlStr.append(" left join Questionario on Questionario.codigo = AvaliacaoInstitucional.questionario");
		sqlStr.append(" left join Pessoa on avaliacaoinstitucionalrespondente.pessoa = pessoa.codigo");
		sqlStr.append(" where  avaliacaoinstitucional.codigo = " + avaliacaoInst.intValue());
		sqlStr.append(" and pessoa.codigo is not null and pessoa.codigo <> 0 ");
		if (questionario.intValue() != 0) {
			sqlStr.append(" and AvaliacaoInstitucional.questionario = " + questionario.intValue());
		}
		if(tipoConsulta.equals("codigo")){
			sqlStr.append(" and Curso.codigo = " + valorConsulta);
		}else{
			sqlStr.append(" and sem_acentos(Curso.nome) ilike sem_acentos('" + valorConsulta+"%') ");
		}
		sqlStr.append(" group by curso.codigo , curso.nome order by UnidadeEnsino.nome, curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<CursoVO> listaCursos = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			CursoVO cursoVO = new CursoVO();
			cursoVO.setCodigo(tabelaResultado.getInt("Curso_codigo"));
			cursoVO.setNome(tabelaResultado.getString("Curso_nome"));
			listaCursos.add(cursoVO);
		}
		return listaCursos;

	}

}