package relatorio.negocio.jdbc.avaliacaoInst;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jobs.JobNotificarRespondenteAvaliacaoInstitucional;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.avaliacaoinst.AvaliacaoInstitucional;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalPorSinteticoPorCursoVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalPorTurmaSinteticoVO;
import relatorio.negocio.interfaces.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelInterfaceFacade;

@Repository
@Lazy
public class AvaliacaoInstitucionalAnaliticoRel extends ControleAcesso implements AvaliacaoInstitucionalAnaliticoRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3406730027833297624L;

	@Override
	public List<AvaliacaoInstitucionalAnaliticoRelVO> realizarGeracaoRelatorioAnalitico(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO, Boolean considerarTurmaAvaliacaoInstitucional) throws Exception {
		if (avaliacaoInstitucionalVO.getCodigo() == null || avaliacaoInstitucionalVO.getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoInstitucionalRel_avaliacaoInstitucional"));
		}
		SqlRowSet rs = realizarConsultarRespondentePorPublicoAlvo(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO, considerarTurmaAvaliacaoInstitucional);

		List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs = new ArrayList<AvaliacaoInstitucionalAnaliticoRelVO>(0);
		if (rs != null) {
			AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO = null;
			while (rs.next()) {
				avaliacaoInstitucionalAnaliticoRelVO = new AvaliacaoInstitucionalAnaliticoRelVO();
				avaliacaoInstitucionalAnaliticoRelVO.setAluno(rs.getInt("codigo"));
				avaliacaoInstitucionalAnaliticoRelVO.setNome(rs.getString("nome"));
				avaliacaoInstitucionalAnaliticoRelVO.setEmail(rs.getString("email"));
				avaliacaoInstitucionalAnaliticoRelVO.setTelefone(rs.getString("telefone"));
				avaliacaoInstitucionalAnaliticoRelVO.setMatricula(rs.getString("matricula"));
				avaliacaoInstitucionalAnaliticoRelVO.setCurso(rs.getString("curso"));
				avaliacaoInstitucionalAnaliticoRelVO.setTurno(rs.getString("turno"));
				avaliacaoInstitucionalAnaliticoRelVO.setTurma(rs.getString("identificadorTurma"));
				// usado para avaliação de ultimo modulo.
				avaliacaoInstitucionalAnaliticoRelVO.setIdentificadorTurma(rs.getString("identificadorTurma"));
				avaliacaoInstitucionalAnaliticoRelVO.setDisciplina(rs.getString("disciplina"));

				avaliacaoInstitucionalAnaliticoRelVO.setJaRespondeu(rs.getBoolean("jaRespondido"));
				avaliacaoInstitucionalAnaliticoRelVO.getUsuarioVO().setCodigo(rs.getInt("usuario"));
				avaliacaoInstitucionalAnaliticoRelVO.getUsuarioVO().getPessoa().setCodigo(rs.getInt("codigo"));
				avaliacaoInstitucionalAnaliticoRelVO.setNomeUnidadeEnsino(rs.getString("unidadeensino"));
				avaliacaoInstitucionalAnaliticoRelVOs.add(avaliacaoInstitucionalAnaliticoRelVO);
			}
		}
		return avaliacaoInstitucionalAnaliticoRelVOs;
	}

	private SqlRowSet realizarConsultarRespondentePorPublicoAlvo(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO, Boolean considerarTurmaAvaliacaoInstitucional) throws Exception {
		PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional = PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo());
		switch (publicoAlvoAvaliacaoInstitucional) {
		case CARGO_CARGO:
			return consultarDadosRelatorioAnaliticoCargoAvaliandoCargo(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case CARGO_COORDENADORES:
			return consultarDadosRelatorioAnaliticoCargoAvaliandoCoordenador(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case CARGO_DEPARTAMENTO:
			return consultarDadosRelatorioAnaliticoCargoAvaliandoDepartamento(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case COLABORADORES_INSTITUICAO:
			return consultarDadosRelatorioAnaliticoColaboradorAvaliandoInstituicao(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case COORDENADORES:
			return consultarDadosRelatorioAnaliticoCoordenador(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case COORDENADORES_CARGO:
			return consultarDadosRelatorioAnaliticoCoodenadorAvaliandoCargo(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case COORDENADORES_CURSO:
			return consultarDadosRelatorioAnaliticoCoodenadorAvaliandoCurso(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case COORDENADORES_DEPARTAMENTO:
			return consultarDadosRelatorioAnaliticoCoodenadorAvaliandoDepartamento(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case COORDENADORES_PROFESSOR:
			return consultarDadosRelatorioAnaliticoCoodenadorAvaliandoProfessor(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case CURSO:
			return consultarDadosRelatorioAnaliticoAluno(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO, considerarTurmaAvaliacaoInstitucional);
		case DEPARTAMENTO_CARGO:
			return consultarDadosRelatorioAnaliticoDepartamentoAvaliandoCargo(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case DEPARTAMENTO_COORDENADORES:
			return consultarDadosRelatorioAnaliticoDepartamentoAvaliandoCoordenador(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case DEPARTAMENTO_DEPARTAMENTO:
			return consultarDadosRelatorioAnaliticoDepartamentoAvaliandoDepartamento(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case FUNCIONARIO_GESTOR:
			return consultarDadosRelatorioAnaliticoFuncionario(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case PROFESSORES:
			return consultarDadosRelatorioAnaliticoProfessor(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case PROFESSORES_COORDENADORES:
			return consultarDadosRelatorioAnaliticoProfessorAvaliandoCoodenador(unidadeEnsino, avaliacaoInstitucionalVO, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case PROFESSOR_TURMA:
			return consultarDadosRelatorioAnaliticoProfessorAvaliaTurma(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case PROFESSOR_CURSO:
			return consultarDadosRelatorioAnaliticoProfessorAvaliaCurso(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case TODOS_CURSOS:
			return consultarDadosRelatorioAnaliticoAluno(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO,considerarTurmaAvaliacaoInstitucional);
		case ALUNO_COORDENADOR:
			return consultarDadosRelatorioAnaliticoAlunoAvaliaCoordenador(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO);
		case TURMA:
			return consultarDadosRelatorioAnaliticoAluno(unidadeEnsino, avaliacaoInstitucionalVO, curso, turno, turma, situacaoResposta, ordenarPor, dataInicio, dataFim, utilizarListagemRespondente, usuarioVO, considerarTurmaAvaliacaoInstitucional);		
		default:
			return null;
		}

	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCoodenadorAvaliandoProfessor(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join cursocoordenador on funcionario.codigo = cursocoordenador.funcionario ");

		sql.append(" where pessoa.ativo = true ");
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
		//sql.append(" and cursocoordenador.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if (!situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and cursocoordenador.curso in ( ");
			sql.append(" 	select distinct curso.codigo from horarioturma ");
			sql.append(" 	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
			sql.append(" 	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
			sql.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
			sql.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
			sql.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
			sql.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
			sql.append("	inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor  ");
			//sql.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			sql.append(" 	where case when cursocoordenador.turma is not null then cursocoordenador.turma = turma.codigo else true end ");
			if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
				sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
			}
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino ", "and"));
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
			sql.append(" 	and pessoa.ativo = true ");
			sql.append(" 	and cursocoordenador.curso = curso.codigo ");
			if (avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" and	horarioturmadia.data::DATE >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data::DATE <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
			} else if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && !avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
			} else {
				sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
			}
			sql.append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by  pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	
	public SqlRowSet consultarDadosRelatorioAnaliticoCoodenadorAvaliandoCurso(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (exists (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join cursocoordenador on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
		sql.append(" where pessoa.ativo = true ");
		//sql.append(" and cursocoordenador.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if (curso != null && curso > 0) {
			sql.append(" and cursocoordenador.curso = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and cursocoordenador.curso in (select distinct curso from unidadeensinocurso where unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" and turno = ").append(turno).append(") ");
		}
		if (turma != null && turma > 0) {
			sql.append(" and cursocoordenador.curso in (select distinct curso from turma where codigo = ").append(turma).append(") ");
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())) {
			sql.append(" and curso.nivelEducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma())) {
			sql.append(" and (cursocoordenador.turma is null or cursocoordenador.turma = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo()).append(") ");
		}
		
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and exists (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and not exists (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by  pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoProfessorAvaliandoCoodenador(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (exists (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		if (!situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" inner join (");	
			sql.append("      SELECT distinct horarioturmadiaitem.professor");	
			sql.append("      FROM horarioturma");	
			sql.append("      INNER JOIN horarioturmadia ON horarioturma.codigo = horarioturmadia.horarioturma");	
			sql.append("      INNER JOIN horarioturmadiaitem ON horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");	
			sql.append("      INNER JOIN turma ON turma.codigo = horarioturma.turma");	
			sql.append("      inner join curso on curso.codigo = turma.curso");	
			sql.append("     ");	
			
			//sql.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			if (avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" horarioturmadia.data::DATE >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data::DATE <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
			} else if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && !avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
			} else {
				sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
			}
			if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
				sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
			}
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino", "and"));
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
			sql.append(" AND EXISTS");
			sql.append("           (SELECT cursocoordenador.codigo");	
			sql.append("            FROM cursocoordenador");	
			sql.append("            INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario");	
			sql.append("            INNER JOIN pessoa AS coordenador ON funcionario.pessoa = coordenador.codigo");	
			sql.append("            WHERE curso.codigo = cursocoordenador.curso		           ");	
			//sql.append("            AND cursocoordenador.unidadeensino =  ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
			sql.append("            AND coordenador.ativo = TRUE");
			sql.append("            AND (cursocoordenador.turma IS NULL OR (cursocoordenador.turma IS NOT NULL AND cursocoordenador.turma = turma.codigo))");	
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
				sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
			}			
			sql.append("            LIMIT 1)");						
			sql.append("        union ");	
			sql.append("        SELECT distinct horarioturmadiaitem.professor");	
			sql.append("        FROM horarioturma");	
			sql.append("        INNER JOIN horarioturmadia ON horarioturma.codigo = horarioturmadia.horarioturma");	
			sql.append("        INNER JOIN horarioturmadiaitem ON horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");	
			sql.append("        INNER JOIN turma ON turma.codigo = horarioturma.turma");	
			sql.append("        INNER JOIN turmaagrupada ON turma.codigo = turmaagrupada.turmaorigem");	
			sql.append("        INNER JOIN turma as turmabase ON turmabase.codigo = turmaagrupada.turma");	
			sql.append("        inner join curso on curso.codigo = turmabase.curso");	
			//sql.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			if (avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" where horarioturmadia.data::DATE >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data::DATE <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
			} else if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && !avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
			} else {
				sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
			}
			if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
				sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
			}
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino", "and"));
			sql.append("");	
			sql.append(" AND EXISTS");	
			sql.append("           (SELECT cursocoordenador.codigo");	
			sql.append("            FROM cursocoordenador");	
			sql.append("            INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario");	
			sql.append("            INNER JOIN pessoa AS coordenador ON funcionario.pessoa = coordenador.codigo");	
			sql.append("            WHERE curso.codigo = cursocoordenador.curso		           ");	
			//sql.append("            AND cursocoordenador.unidadeensino =  ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
			sql.append("            AND coordenador.ativo = TRUE");
			sql.append("            AND (cursocoordenador.turma IS NULL OR (cursocoordenador.turma IS NOT NULL AND cursocoordenador.turma = turmabase.codigo))");	
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
				sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
			}			
			sql.append("            LIMIT 1)");	
			sql.append("        ) as cursoprofessor on cursoprofessor.professor =  pessoa.codigo");	
			
			
		}
		sql.append(" where pessoa.ativo = true and pessoa.professor = true ");
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if (!situacaoResposta.equals("RESPONDIDO")) {			
			sql.append(" and exists ( ");
			sql.append(" 	select horarioturmadia.codigo from horarioturma ");
			sql.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
			sql.append(" 	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
			sql.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
			sql.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
			sql.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
			sql.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");			
			sql.append(" 	where horarioturmadiaitem.professor = pessoa.codigo ");
			if (avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" and	horarioturmadia.data::DATE >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data::DATE <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
			} else if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && !avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
				sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
			} else {
				sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
			}
			//sql.append(" 	and turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino", "and"));
			if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
				sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
			}
			sql.append("  and exists (select cursocoordenador.codigo from cursocoordenador");
			sql.append(" 	INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario");
			sql.append(" 	INNER JOIN pessoa AS coordenador ON funcionario.pessoa = coordenador.codigo");
			sql.append(" 	where curso.codigo = cursocoordenador.curso");
			//sql.append(" 	AND cursocoordenador.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
			sql.append("    AND coordenador.ativo = TRUE");
			sql.append(" 	and (cursocoordenador.turma is null or (cursocoordenador.turma IS NOT NULL and cursocoordenador.turma = turma.codigo))");			
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
				sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
			}
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "cursocoordenador.curso", "and"));
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
			sql.append(" limit 1)");
			sql.append(" limit 1)");
		}
		 
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCoodenadorAvaliandoCargo(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join cursocoordenador on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
		sql.append(" where pessoa.ativo = true ");
		//sql.append(" and cursocoordenador.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCoodenadorAvaliandoDepartamento(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join cursocoordenador on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
		sql.append(" where pessoa.ativo = true ");
		//sql.append(" and cursocoordenador.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"cursocoordenador.unidadeensino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoColaboradorAvaliandoInstituicao(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCargoAvaliandoCargo(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		sql.append(" and funcionariocargo.cargo = ").append(avaliacaoInstitucionalVO.getCargo().getCodigo());
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCargoAvaliandoCoordenador(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		sql.append(" and funcionariocargo.cargo = ").append(avaliacaoInstitucionalVO.getCargo().getCodigo());
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCargoAvaliandoDepartamento(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		sql.append(" and funcionariocargo.cargo = ").append(avaliacaoInstitucionalVO.getCargo().getCodigo());
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoDepartamentoAvaliandoDepartamento(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		sql.append(" and cargo.departamento = ").append(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoDepartamentoAvaliandoCargo(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma , ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		sql.append(" and cargo.departamento = ").append(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoDepartamentoAvaliandoCoordenador(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
		sql.append(" where pessoa.ativo = true and funcionariocargo.ativo  ");
		sql.append(" and cargo.departamento = ").append(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		//sql.append(" and funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeensino", "and"));
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		}
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	@Override
	public List<AvaliacaoInstitucionalPorTurmaSinteticoVO> consultarRelatorioSintetico(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct  turma.identificadorTurma as turma, curso.nome as curso, ");
		sql.append(" sum(case when (pessoa.codigo in(select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(")) then 1 else 0 end) as qtdeRespondeu, ");
		sql.append(" sum(case when (pessoa.codigo not in(select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(")) then 1 else 0 end) as qtdeNaoRespondeu ");
		sql.append(" from pessoa ");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join turno on matricula.turno = turno.codigo ");
		sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sql.append(" and mp.situacaomatriculaperiodo != 'PC' ");
		if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty()) {
			sql.append(" and mp.ano = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" and mp.semestre = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador() || (avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador() && Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina().getCodigo()))){
		sql.append("     and mp.codigo in ( ");
		sql.append("     select historico.matriculaperiodo from historico ");
		sql.append("		inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sql.append("		where historico.matriculaperiodo = mp.codigo ");
		sql.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
		sql.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
		sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
		sql.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
		sql.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
		sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
		sql.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
		sql.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
		sql.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina().getCodigo())){
			sql.append("	and historico.disciplina = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		if(!avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador()){
			sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(") ");
			sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
			sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ").append(AvaliacaoInstitucional.getSqlHorarioAulaAluno()).append(" )) ");
		}
		sql.append("		limit 1 ) ");
		}
		
		sql.append(" order by mp.ano||mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1)"); 
		
		sql.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");		
		sql.append(" where  matriculaperiodo.situacaomatriculaperiodo != 'PC'  ");
		if (!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()) {
			sql.append(" and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		if (avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0) {
			//sql.append(" and matricula.unidadeEnsino =  ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"matricula.unidadeEnsino", "and"));
		}
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			//sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"unidadeensino.codigo", "and"));
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turno.codigo = ").append(turno);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}		
		if ((avaliacaoInstitucionalVO.getPublicoAlvo_Curso() || avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador())) {
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));			
		}
		if ((avaliacaoInstitucionalVO.getPublicoAlvo_Turma() || avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador()) && avaliacaoInstitucionalVO.getTurma().getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if(avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador()){
			sql.append("   	and exists ( select cursocoordenador.codigo from cursocoordenador  ");
			sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sql.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
			sql.append("   	where (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
			sql.append("   	and coord.ativo ");
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
				sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
			}
			sql.append("   	and cursocoordenador.curso = matricula.curso ");
			sql.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
			sql.append("   	limit 1 )");
			
			sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
			sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");			
			sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			sql.append("   	and exists (select cursocoordenador.codigo from cursocoordenador  ");
			sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sql.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
			sql.append("   	where (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
			sql.append("   	and coord.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
			sql.append("   	and coord.ativo ");
			sql.append("   	and cursocoordenador.curso = matricula.curso ");
			sql.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
			sql.append("   	limit 1) ) ) ");			
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		} else {
			sql.append(" and (pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
			sql.append(" or matricula.situacao  = 'AT') ");
		}
		sql.append(" group by turma.identificadorTurma,  curso.nome  ");
		sql.append(" order by  curso.nome, turma.identificadorTurma ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<AvaliacaoInstitucionalPorTurmaSinteticoVO> avaliacaoInstitucionalPorTurmaSinteticoVOs = new ArrayList<AvaliacaoInstitucionalPorTurmaSinteticoVO>(0);
		AvaliacaoInstitucionalPorTurmaSinteticoVO avaliacaoInstitucionalPorTurmaSinteticoVO = null;
		while (rs.next()) {
			avaliacaoInstitucionalPorTurmaSinteticoVO = new AvaliacaoInstitucionalPorTurmaSinteticoVO();
			avaliacaoInstitucionalPorTurmaSinteticoVO.setTurma(rs.getString("turma"));
			avaliacaoInstitucionalPorTurmaSinteticoVO.setCurso(rs.getString("curso"));
			avaliacaoInstitucionalPorTurmaSinteticoVO.setQtdeNaoRespondeu(rs.getInt("qtdeNaoRespondeu"));
			avaliacaoInstitucionalPorTurmaSinteticoVO.setQtdeRespondeu(rs.getInt("qtdeRespondeu"));
			avaliacaoInstitucionalPorTurmaSinteticoVOs.add(avaliacaoInstitucionalPorTurmaSinteticoVO);
		}
		return avaliacaoInstitucionalPorTurmaSinteticoVOs;
	}

	public List<AvaliacaoInstitucionalPorSinteticoPorCursoVO> consultarRelatorioSinteticoPorCurso(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct  curso.nome as curso, ");
		sql.append(" sum(case when (pessoa.codigo in(select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(")) then 1 else 0 end) as qtdeRespondeu, ");
		sql.append(" sum(case when (pessoa.codigo not in(select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(")) then 1 else 0 end) as qtdeNaoRespondeu ");
		sql.append(" from pessoa ");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join turno on matricula.turno = turno.codigo ");
		sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sql.append(" and mp.situacaomatriculaperiodo != 'PC' ");
		if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty()) {
			sql.append(" and mp.ano = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" and mp.semestre = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador() || (avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador() && Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina().getCodigo()))){
		sql.append("     and mp.codigo in ( ");
		sql.append("     select historico.matriculaperiodo from historico ");
		sql.append("		inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sql.append("		where historico.matriculaperiodo = mp.codigo ");
		sql.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
		sql.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
		sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
		sql.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
		sql.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
		sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
		sql.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
		sql.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
		sql.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina().getCodigo())){
			sql.append("		and historico.disciplina  = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		if(!avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador()){
			sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(") ");
			sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");		
			sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ").append(AvaliacaoInstitucional.getSqlHorarioAulaAluno()).append(" ))");
		}
		sql.append("		limit 1 ) ");
		}
		sql.append(" order by mp.ano||mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1)"); 
		
		sql.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sql.append(" where  matriculaperiodo.situacaomatriculaperiodo != 'PC'  ");
		if (!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()) {
			sql.append(" and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		if (avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0) {
			//sql.append(" and matricula.unidadeEnsino =  ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		}
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			//sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"matricula.unidadeEnsino", "and"));
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turno.codigo = ").append(turno);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if ((avaliacaoInstitucionalVO.getPublicoAlvo_Curso() || avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador() )) {
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		}
		if ((avaliacaoInstitucionalVO.getPublicoAlvo_Turma() || avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador() ) && avaliacaoInstitucionalVO.getTurma().getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if(avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador()){
			sql.append("   	and exists ( select cursocoordenador.codigo from cursocoordenador  ");
			sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sql.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
			sql.append("   	where (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
			sql.append("   	and coord.ativo ");
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
				sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
			}
			sql.append("   	and cursocoordenador.curso = matricula.curso ");
			sql.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
			sql.append("   	limit 1 )");
			
			sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
			sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");			
			sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			sql.append("   	and exists (select cursocoordenador.codigo from cursocoordenador  ");
			sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sql.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
			sql.append("   	where (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
			sql.append("   	and coord.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
			sql.append("   	and coord.ativo ");
			sql.append("   	and cursocoordenador.curso = matricula.curso ");
			sql.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
			sql.append("   	limit 1) ) ) ");
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		} else {
			sql.append(" and (pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
			sql.append("  or matricula.situacao = 'AT' ) ");
		}
		sql.append(" group by curso.nome ");
		sql.append(" order by curso.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<AvaliacaoInstitucionalPorSinteticoPorCursoVO> avaliacaoInstitucionalPorSinteticoPorCursoVOs = new ArrayList<AvaliacaoInstitucionalPorSinteticoPorCursoVO>(0);
		AvaliacaoInstitucionalPorSinteticoPorCursoVO avaliacaoInstitucionalPorSinteticoPorCursoVO = null;
		while (rs.next()) {
			avaliacaoInstitucionalPorSinteticoPorCursoVO = new AvaliacaoInstitucionalPorSinteticoPorCursoVO();
			avaliacaoInstitucionalPorSinteticoPorCursoVO.setCurso(rs.getString("curso"));
			//avaliacaoInstitucionalPorSinteticoPorCursoVO.setCodCurso(rs.getInt("codCurso"));
			avaliacaoInstitucionalPorSinteticoPorCursoVO.setQtdeNaoRespondeu(rs.getInt("qtdeNaoRespondeu"));
			avaliacaoInstitucionalPorSinteticoPorCursoVO.setQtdeRespondeu(rs.getInt("qtdeRespondeu"));
			avaliacaoInstitucionalPorSinteticoPorCursoVOs.add(avaliacaoInstitucionalPorSinteticoPorCursoVO);
		}
		return avaliacaoInstitucionalPorSinteticoPorCursoVOs;
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoAluno(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente,UsuarioVO usuarioVO, Boolean considerarTurmaAvaliacaoInstitucional) throws Exception {

		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone,");
		sql.append(" matricula.matricula, curso.nome as curso, turno.nome as turno, unidadeensino.nome as unidadeensino, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (exists (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1)) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		
			sql.append(" '' as disciplina, ");
			sql.append(" turma.identificadorturma, ");

		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join turno on matricula.turno = turno.codigo ");
		sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sql.append(" and mp.situacaomatriculaperiodo != 'PC'  ");
		if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty()) {
			sql.append(" and mp.ano = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" and mp.semestre = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		
		sql.append(" order by mp.ano||mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1) ");
		sql.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		
		sql.append(" left join historico on matriculaperiodo.codigo = historico.matriculaperiodo");

		sql.append(" where matriculaperiodo.situacaomatriculaperiodo != 'PC'  ");
		sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
		if (considerarTurmaAvaliacaoInstitucional) {
			sql.append(" and turma.considerarturmaavaliacaoinstitucional  ");
		}
		if (!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()) {
			sql.append(" and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		/*if (avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" and unidadeEnsino.codigo =  ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		}
		*/
		if (unidadeEnsino != null && unidadeEnsino > 0 && !avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turno.codigo = ").append(turno);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}

		if (!avaliacaoInstitucionalVO.getAvaliarDisciplinasReposicao() && avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("DI")) {
			sql.append(" and historico.tipohistorico not in ('AD', 'DE') ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"unidadeensino.codigo", "and"));
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if (avaliacaoInstitucionalVO.getPublicoAlvo_Turma() && avaliacaoInstitucionalVO.getTurma().getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if(!avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and exists (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			if (dataInicio != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			}
			if (dataFim != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
			}
			if (curso != 0) {
				sql.append(" and RespostaAvaliacaoInstitucionalDW.curso = ").append(curso);
			}
			sql.append(" limit 1 ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and matricula.situacao = 'AT' "); 
			sql.append(" and not exists (select respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			if (dataInicio != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			}
			if (dataFim != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
			}
			if (curso != 0) {
				sql.append(" and RespostaAvaliacaoInstitucionalDW.curso = ").append(curso);
			}
			sql.append(" limit 1 ) ");
		}else{
			
			sql.append(" and (exists (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
			sql.append(" or matricula.situacao = 'AT' ) ");
			
		}
		}
		
		sql.append("     and exists ( ");
		if(!avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
		sql.append("     select historico.matriculaperiodo from matriculaperiodoturmadisciplina ");
		sql.append("		inner join historico on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		}else if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
			sql.append("  select historico.matriculaperiodo  from (select turma.codigo as turma, disciplina.codigo as disciplina, pessoa.codigo as professor, min(horarioturmadiaitem.data) as datainicio, ");		
			sql.append("  (select max(htdi.data) from horarioturmadia htd");		
			sql.append("  inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");		
			sql.append("  where htd.horarioturma =  horarioturma.codigo");		
			sql.append("  and htdi.disciplina = disciplina.codigo");		
			sql.append("  and htdi.professor = pessoa.codigo)  as datatermino  ");		
			sql.append("  from turma");		
			sql.append("  inner join curso as c on (((turma.turmaagrupada is null or turma.turmaagrupada =  false) and turma.curso = c.codigo)");		
			sql.append("  or (turma.turmaagrupada =  true and c.codigo = (select t.curso from turmaagrupada ta inner join turma as t on t.codigo = ta.turma ");		
			sql.append("  where ta.turmaorigem = turma.codigo limit 1 )))");		
			sql.append("  inner join horarioturma on horarioturma.turma = turma.codigo");		
			sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");		
			sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");		
			sql.append("  inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo");		
			sql.append("  inner join pessoa on horarioturmadiaitem.professor = pessoa.codigo");		
			sql.append("  where 1 = 1 ");
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getUnidadeEnsino())) {
				//sql.append(" and  turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
			}
			if (unidadeEnsino != null && unidadeEnsino > 0 && !avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
				//sql.append(" and turma.unidadeensino = ").append(unidadeEnsino);
			}
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino", "and"));
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())) {
				sql.append("  and c.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
			}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())){
				sql.append("  and disciplina.codigo = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
			}
			if (curso != null && curso > 0) {
				sql.append(" and c.codigo = ").append(curso);
			}
			if (turno != null && turno > 0) {
				sql.append(" and turma.turno = ").append(turno);
			}
			if (turma != null && turma > 0) {
				sql.append(" and turma.codigo = ").append(turma);
			}
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"unidadeensino.codigo", "and"));
			sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "c.codigo", "and"));
			sql.append("  and horarioturmadiaitem.data BETWEEN '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			sql.append("  group by turma.codigo, disciplina.codigo, pessoa.codigo, horarioturma.codigo");		
			sql.append("  having (select max(htdi.data) from horarioturmadia htd");		
			sql.append("  inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");		
			sql.append("  where htd.horarioturma =  horarioturma.codigo");		
			sql.append("  and htdi.disciplina = disciplina.codigo");		
			sql.append("  and htdi.professor = pessoa.codigo) BETWEEN '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");		
			sql.append("  ");		
			sql.append("       ) as horarioturma ");		
			sql.append("  ");		
			sql.append("       INNER JOIN matriculaperiodoturmadisciplina on horarioturma.turma = matriculaperiodoturmadisciplina.turma and horarioturma.disciplina = matriculaperiodoturmadisciplina.disciplina");		
			sql.append("       INNER JOIN historico ON historico.matricula = matricula.matricula     AND matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");										
		}
		sql.append("		where historico.matricula = matricula.matricula and  historico.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())){
			sql.append("   	and historico.disciplina  = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		sql.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
		sql.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
			if (situacaoResposta.equals("RESPONDIDO")) {
				sql.append("     and exists (");
				sql.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = ").append(avaliacaoInstitucionalVO.getCodigo());
				sql.append(" 	and respostaavaliacaoinstitucionaldw.turma = matriculaperiodoturmadisciplina.turma	");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.matriculaaluno = matricula.matricula");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = matriculaperiodoturmadisciplina.disciplina limit 1");
				sql.append("    )");	
			}else if(situacaoResposta.equals("NAO_RESPONDIDO")) {
				sql.append(" and matricula.situacao = 'AT' "); 
				sql.append("     and not exists (");
				sql.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = ").append(avaliacaoInstitucionalVO.getCodigo());
				sql.append(" 	and respostaavaliacaoinstitucionaldw.turma = matriculaperiodoturmadisciplina.turma	");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.matriculaaluno = matricula.matricula");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = matriculaperiodoturmadisciplina.disciplina limit 1");
				sql.append("    )");	
			}else {
				sql.append("  and (exists (");
				sql.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = ").append(avaliacaoInstitucionalVO.getCodigo());
				sql.append(" 	and respostaavaliacaoinstitucionaldw.turma = matriculaperiodoturmadisciplina.turma	");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.matriculaaluno = matricula.matricula");
				sql.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = matriculaperiodoturmadisciplina.disciplina limit 1");
				sql.append("    ) ");	
				sql.append(" or matricula.situacao = 'AT' ) ");
			}
		}
		
		sql.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
		sql.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
		sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
		sql.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
		sql.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
		sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
		sql.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
		if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
		sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(") ");
		sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");		
		sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			sql.append("   	and horarioturma.professor = avaliacaoinstitucionalpessoaavaliada.pessoa ");
		}else {
			sql.append(" and ( (not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
			if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("DI")){
				sql.append(" and exists (select horario.professor_codigo from periodoauladisciplinaaluno(historico.codigo) as horario ");
				sql.append("	where horario.professor_codigo is not null) ");	
			}			
			sql.append(") ");
			sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");		
			sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			sql.append(AvaliacaoInstitucional.getSqlHorarioAulaAluno()).append(" ");
		}
		sql.append("		)) ");
		sql.append("		limit 1 ) ");	
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by ").append(ordenarPor).append(", pessoa.nome ");		
		//	System.out.println(sql.toString());
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoProfessor(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone,");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "')) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido ,");
		}
		sql.append(" (select disciplina.nome from respostaavaliacaoinstitucionaldw left join disciplina on disciplina.codigo = respostaavaliacaoinstitucionaldw.disciplina where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as disciplina, ");
		sql.append(" (select turma.identificadorturma from respostaavaliacaoinstitucionaldw left join turma on turma.codigo = respostaavaliacaoinstitucionaldw.turma where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			//sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino", "and"));
		if (curso != null && curso > 0) {
			sql.append(" and turma.curso = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turma.turno = ").append(turno);
		}
		if (avaliacaoInstitucionalVO.getPublicoAlvo_Curso() && avaliacaoInstitucionalVO.getCurso().getCodigo() > 0) {
			sql.append(" and turma.curso  = ").append(avaliacaoInstitucionalVO.getCurso().getCodigo());
		}
		if (avaliacaoInstitucionalVO.getPublicoAlvo_Turma() && avaliacaoInstitucionalVO.getTurma().getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		sql.append(" order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" where pessoa.professor = true ");
		sql.append(" and pessoa.codigo in (select distinct professor from horarioturma ");
		sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sql.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sql.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sql.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
		sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sql.append(" where horarioturmadiaitem.professor = pessoa.codigo ");
	/*	if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		}*/
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turma.turno = ").append(turno);
		}
		if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())) {
			sql.append(" and curso.nivelEducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeensino", "and"));
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if (avaliacaoInstitucionalVO.getTurma().getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
/*		if (avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" and turma.unidadeEnsino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		}*/
		if (!avaliacaoInstitucionalVO.getAno().equals("")) {
			sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().equals("")) {
			sql.append(" and horarioturma.semestreVigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(avaliacaoInstitucionalVO.getAno().equals("") && avaliacaoInstitucionalVO.getSemestre().equals("")){
			if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
				sql.append(" and horarioturmadia.data::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and horarioturmadia.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
				sql.append(" and exists (select max(htdi.data) from  horarioturmadia htd ");
				sql.append("      inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");
				sql.append("      and htd.horarioturma = horarioturma.codigo");
				sql.append("      and htdi.disciplina = horarioturmadiaitem.disciplina   ");
				sql.append("      and htdi.professor = horarioturmadiaitem.professor   ");
				sql.append(" having max(htdi.data) <= '").append(Uteis.getDataJDBC(dataFim)).append("'    )   ");
			}else {
				sql.append(" and horarioturmadia.data::date >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
		}
		}
		sql.append(" ) ");
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "') ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "') ");
//			if (avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
//				sql.append(" and pessoa.codigo in (select distinct t.professor from horarioturmadetalhado(null, null, null, null) as t  inner join pessoa on pessoa.codigo = t.professor where 1 = 1  and data >= '" + Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula()) + "' and data <= '" + Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula()) + "')");
//			}
			}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoCoordenador(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" (select disciplina.nome from respostaavaliacaoinstitucionaldw left join disciplina on disciplina.codigo = respostaavaliacaoinstitucionaldw.disciplina where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as disciplina, ");
		sql.append(" (select turma.identificadorturma from respostaavaliacaoinstitucionaldw left join turma on turma.codigo = respostaavaliacaoinstitucionaldw.turma where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as identificadorturma , ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");		
		sql.append(" where ");
		sql.append(" pessoa.codigo in (select distinct funcionario.pessoa from cursocoordenador ");
		sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append(" where 1 = 1 ");
		/*if (avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" and cursocoordenador.unidadeEnsino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		}*/
		if (curso != null && curso > 0) {
			sql.append(" and cursocoordenador.curso = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and cursocoordenador.curso in (select distinct curso from unidadeensinocurso where unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" and turno = ").append(turno).append(") ");
		}
		if (turma != null && turma > 0) {
			sql.append(" and cursocoordenador.curso in (select distinct curso from turma where codigo = ").append(turma).append(") ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO, "cursocoordenador.unidadeEnsino", "and")).append(" OR cursocoordenador.unidadeEnsino IS null ");
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "cursocoordenador.curso", "and"));
		if (avaliacaoInstitucionalVO.getPublicoAlvo_Turma() && avaliacaoInstitucionalVO.getTurma().getCodigo() > 0) {
			sql.append(" and cursocoordenador.curso in (select distinct curso from turma where codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo()).append(") ");
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}

		sql.append(" ) ");
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public SqlRowSet consultarDadosRelatorioAnaliticoFuncionario(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		sql.append(" (select disciplina.nome from respostaavaliacaoinstitucionaldw left join disciplina on disciplina.codigo = respostaavaliacaoinstitucionaldw.disciplina where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as disciplina, ");
		sql.append(" (select turma.identificadorturma from respostaavaliacaoinstitucionaldw left join turma on turma.codigo = respostaavaliacaoinstitucionaldw.turma where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append(" where pessoa.ativo ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getUnidadeEnsino())){
			sql.append(" and exists (select funcionariocargo.codigo from funcionariocargo where funcionariocargo.funcionario = funcionario.codigo and funcionariocargo.ativo  ");
			sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"funcionariocargo.unidadeEnsino", "and")).append(" ) ");
		}else {
			sql.append(" and exists (select funcionariocargo.codigo from funcionariocargo where funcionariocargo.funcionario = funcionario.codigo and funcionariocargo.ativo ) ");
		}
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" and funcionario.exercecargoadministrativo ");

		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	@Override
	public void realizarEnvioEmail(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs, PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		if (personalizacaoMensagemAutomaticaVO.getAssunto().trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_DataComemorativa_assunto"));
		}
		if (Uteis.retiraTags(personalizacaoMensagemAutomaticaVO.getMensagem()).trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_DataComemorativa_mensagem"));
		}
		Boolean existeDest = false;
		List<AvaliacaoInstitucionalAnaliticoRelVO> respondentesNotificar = new ArrayList<AvaliacaoInstitucionalAnaliticoRelVO>(0);
		for (AvaliacaoInstitucionalAnaliticoRelVO av : avaliacaoInstitucionalAnaliticoRelVOs) {
			if (av.getEnviarEmail() && !av.getJaRespondeu()) {
				respondentesNotificar.add(av);
				existeDest = true;				
			}
		}
		if (!existeDest) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoInstitucional_destinatario"));
		}
		personalizacaoMensagemAutomaticaVO.setMensagem(personalizacaoMensagemAutomaticaVO.getMensagem().replace("src=\"../../resources/", "src=\"../resources/"));
		JobNotificarRespondenteAvaliacaoInstitucional job = new JobNotificarRespondenteAvaliacaoInstitucional(avaliacaoInstitucionalVO, respondentesNotificar, personalizacaoMensagemAutomaticaVO, usuarioVO);		
		Thread jobNotificarRespondente = new Thread(job);
		jobNotificarRespondente.start();

	}

	public String realizarSubstituicaoTagsMensagem(AvaliacaoInstitucionalAnaliticoRelVO av, String mensagem) {
		mensagem = mensagem.replaceAll("../imagens", "./imagens");
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), av.getNome());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), av.getNome());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), av.getCurso());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(), av.getTurno());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), av.getMatricula());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), av.getTurma());
		return mensagem;
	}
	
	
	public SqlRowSet consultarDadosRelatorioAnaliticoProfessorAvaliaTurma(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone,");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "')) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido ,");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" where pessoa.professor = true and pessoa.ativo = true ");
		sql.append(" and exists (select distinct professor from horarioturma ");
		sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sql.append(" inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sql.append(" inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sql.append(" or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
		sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sql.append(" where horarioturmadiaitem.professor = pessoa.codigo ");
	/*	if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		}*/
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turma.turno = ").append(turno);
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())){
			sql.append(" and curso.niveleducacional  = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeEnsino", "and"));
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma())) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if (avaliacaoInstitucionalVO.getDisciplina().getCodigo() > 0) {
			sql.append(" and horarioturmadiaitem.disciplina = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		if (!avaliacaoInstitucionalVO.getAno().equals("")) {
			sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().equals("")) {
			sql.append(" and horarioturma.semestreVigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(avaliacaoInstitucionalVO.getAno().equals("") && avaliacaoInstitucionalVO.getSemestre().equals("")){
			sql.append(" and horarioturmadia.data >= '").append(Uteis.getData(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data <= '").append(avaliacaoInstitucionalVO.getDataTerminoAula()).append("' "); 
		}
		sql.append(" limit 1 ) ");
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "') ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "') ");
		}
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	public SqlRowSet consultarDadosRelatorioAnaliticoProfessorAvaliaCurso(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone,");
		sql.append(" '' as matricula, '' as curso, '' as turno, '' as unidadeensino, '' as turma, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (pessoa.codigo in (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "')) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido ,");
		}
		sql.append(" '' as disciplina, ");
		sql.append(" '' as identificadorturma, ");
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" where pessoa.professor = true and pessoa.ativo = true ");
		sql.append(" and exists (select distinct professor from horarioturma ");
		sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sql.append(" inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sql.append(" inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sql.append(" or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
		sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sql.append(" where horarioturmadiaitem.professor = pessoa.codigo ");
/*		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		}*/
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turma.turno = ").append(turno);
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())){
			sql.append(" and curso.niveleducacional  = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"turma.unidadeEnsino", "and"));
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma())) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}
		if (avaliacaoInstitucionalVO.getDisciplina().getCodigo() > 0) {
			sql.append(" and horarioturmadiaitem.disciplina = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		if (!avaliacaoInstitucionalVO.getAno().equals("")) {
			sql.append(" and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().equals("")) {
			sql.append(" and horarioturma.semestreVigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(avaliacaoInstitucionalVO.getAno().equals("") && avaliacaoInstitucionalVO.getSemestre().equals("")){
			sql.append(" and horarioturmadia.data >= '").append(Uteis.getData(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data <= '").append(avaliacaoInstitucionalVO.getDataTerminoAula()).append("' "); 
		}
		sql.append(" limit 1 ) ");
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo  and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "') ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "') ");
		}
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	
	public SqlRowSet consultarDadosRelatorioAnaliticoAlunoAvaliaCoordenador(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFim, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome, pessoa.email, ");
		sql.append(" agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone,");
		sql.append(" matricula.matricula, curso.nome as curso, turno.nome as turno, unidadeensino.nome as unidadeensino, ");
		if (situacaoResposta.equals("TODAS")) {
			sql.append(" (exists (select pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" )) as jaRespondido, ");
		} else if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" true as jaRespondido, ");
		} else {
			sql.append(" false as jaRespondido, ");
		}
		
		if(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("UM")){
			sql.append(" (select disciplina.nome from respostaavaliacaoinstitucionaldw left join disciplina on disciplina.codigo = respostaavaliacaoinstitucionaldw.disciplina where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as disciplina, ");
	    	sql.append(" (select turma.identificadorturma from respostaavaliacaoinstitucionaldw left join turma on turma.codigo = respostaavaliacaoinstitucionaldw.turma where pessoa.codigo = respostaavaliacaoinstitucionaldw.pessoa and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and dataresposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' and dataresposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' order by respostaavaliacaoinstitucionaldw.codigo desc limit 1) as identificadorturma, ");
		}else{
			sql.append(" '' as disciplina, ");
			sql.append(" turma.identificadorturma, ");
		}		
		sql.append(" usuario.codigo as usuario ");
		sql.append(" from pessoa ");
		sql.append(" inner join usuario on usuario.pessoa = pessoa.codigo and usuario.codigo = (select us.codigo from usuario as us where us.pessoa = pessoa.codigo order by us.codigo desc limit 1)");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join turno on matricula.turno = turno.codigo ");
		sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sql.append(" and mp.situacaomatriculaperiodo != 'PC'  ");
		if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty()) {
			sql.append(" and mp.ano = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if (!avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" and mp.semestre = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		sql.append(" order by mp.ano||mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1)"); 		
		sql.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sql.append(" where matriculaperiodo.situacaomatriculaperiodo != 'PC'  ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())){
			sql.append("     and exists ( ");
			sql.append("     	select historico.matriculaperiodo from matriculaperiodoturmadisciplina ");
			sql.append("		inner join historico  on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sql.append("		where historico.matriculaperiodo = matriculaperiodo.codigo ");
			sql.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
			sql.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");			
			sql.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
			sql.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
			sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
			sql.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
			sql.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
			sql.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
			sql.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
			sql.append("   	    and historico.disciplina  = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
			sql.append("		limit 1 ) ");						
		}
		
		if (!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()) {
			sql.append(" and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
/*		if (avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" and unidadeEnsino.codigo =  ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		}
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}*/
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turno != null && turno > 0) {
			sql.append(" and turno.codigo = ").append(turno);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		sql.append(getSqlCondicaoWhereUnidadeEnsino(avaliacaoInstitucionalVO,"unidadeensino.codigo", "and"));
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma().getCodigo())) {
			sql.append(" and turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
		}	
		
		sql.append("   	and exists ( select cursocoordenador.codigo from cursocoordenador  ");
		sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
		sql.append("   	where (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
		sql.append("   	and coord.ativo ");
		sql.append("   	and cursocoordenador.curso = matricula.curso ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipoCoordenadorCurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		sql.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
		sql.append("   	limit 1 )");
		
		sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");			
		sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
		sql.append("   	and exists (select cursocoordenador.codigo from cursocoordenador  ");
		sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
		sql.append("   	where (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
		sql.append("   	and coord.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
		sql.append("   	and coord.ativo ");
		sql.append("   	and cursocoordenador.curso = matricula.curso ");
		sql.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
		sql.append("   	limit 1) ) ) ");
		
		
		
		if (situacaoResposta.equals("RESPONDIDO")) {
			sql.append(" and matricula.situacao in ('AT') "); 
			sql.append(" and pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			if (dataInicio != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			}
			if (dataFim != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
			}
			if (curso != 0) {
				sql.append(" and RespostaAvaliacaoInstitucionalDW.curso = ").append(curso);
			}
			sql.append(" ) ");
		} else if (situacaoResposta.equals("NAO_RESPONDIDO")) {
			sql.append(" and pessoa.codigo not in (select distinct respostaavaliacaoinstitucionaldw.pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			if (dataInicio != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date >= '" + Uteis.getDataJDBC(dataInicio) + "' ");
			}
			if (dataFim != null) {
				sql.append(" AND RespostaAvaliacaoInstitucionalDW.dataResposta::date <= '" + Uteis.getDataJDBC(dataFim) + "' ");
			}
			if (curso != 0) {
				sql.append(" and RespostaAvaliacaoInstitucionalDW.curso = ").append(curso);
			}
			sql.append(" ) ");
		}else{
			sql.append(" and (pessoa.codigo in (select distinct pessoa from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.pessoa = pessoa.codigo and matricula.matricula = respostaavaliacaoinstitucionaldw.matriculaaluno and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(") ");
			sql.append(" or matricula.situacao in ('AT') ) ");
		}
		
		if(utilizarListagemRespondente) {
			sql.append(" and pessoa.codigo  in (select pessoa from avaliacaoinstitucionalrespondente  where pessoa = pessoa.codigo and avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ) ");
		}
		sql.append(" order by ").append(ordenarPor).append(", pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	@Override
	public StringBuilder getSqlCondicaoWhereCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String campoComparacao, String andOr) {
		StringBuilder sql = new StringBuilder();
		if(avaliacaoInstitucionalVO.getCurso().getCodigo() > 0 && avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().isEmpty()){
			sql.append(" ").append(andOr).append(" ").append(campoComparacao).append(" = ").append(avaliacaoInstitucionalVO.getCurso().getCodigo()).append(" ");
		}else if(!avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
			sql.append(" ").append(andOr).append(" exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and avaliacaoinstitucionalcurso.curso = ").append(campoComparacao).append(" limit 1) ");
		}
		return sql;
	}

	@Override
	public StringBuilder getSqlCondicaoWhereUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String campoComparacao, String andOr) {
		StringBuilder sql = new StringBuilder();
		if(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo() > 0 && avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty()){
			sql.append(" ").append(andOr).append(" ").append(campoComparacao).append(" = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
		}else if(!avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty()) {
			sql.append(" ").append(andOr).append(" exists(select avaliacaoinstitucionalunidadeensino.codigo from avaliacaoinstitucionalunidadeensino where avaliacaoinstitucionalunidadeensino.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and avaliacaoinstitucionalunidadeensino.unidadeensino = ").append(campoComparacao).append(" limit 1) ");
		}
		return sql;
	}
}
