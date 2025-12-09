package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.academico.InclusaoDisciplinaForaGradeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.InclusaoDisciplinaForaGradeInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class InclusaoDisciplinaForaGrade extends ControleAcesso implements InclusaoDisciplinaForaGradeInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public InclusaoDisciplinaForaGrade() {

	}

	public void validarDados(InclusaoDisciplinaForaGradeVO obj) throws Exception {
		if (obj.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("O campo MATRÍCULA deve ser informado.");
		}
		if (obj.getDisciplinaForaGradeVOs().isEmpty()) {
			throw new Exception("Deve ser informado pelo menos uma disciplina para gravação.");
		}
	}

	public void persistir(InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuario);
		} else {
			alterar(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "INSERT INTO InclusaoDisciplinaForaGrade( matricula ) VALUES ( ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
				return sqlInserir;
			}
		 }, new ResultSetExtractor<Integer>() {

             public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                 if (arg0.next()) {
                     obj.setNovoObj(Boolean.FALSE);
                     return arg0.getInt("codigo");
                 }
                 return null;
             }
         }));
		getFacadeFactory().getDisciplinaForaGradeFacade().incluirDisciplinaForaGrade(obj.getCodigo(), obj.getMatriculaVO().getCurso().getPeriodicidade(), obj.getDisciplinaForaGradeVOs(), usuario);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE InclusaoDisciplinaForaGrade set matricula=?  WHERE (codigo=?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getMatriculaVO().getMatricula());
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
		getFacadeFactory().getDisciplinaForaGradeFacade().alterarDisciplinaForaGrade(obj.getCodigo(), obj.getMatriculaVO().getCurso().getPeriodicidade(), obj.getDisciplinaForaGradeVOs(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		getFacadeFactory().getDisciplinaForaGradeFacade().excluirDisciplinaForaGrade(obj.getCodigo(), usuario);
		String sql = "DELETE FROM InclusaoDisciplinaForaGrade WHERE (codigo=?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct inclusaoDisciplinaForaGrade.codigo AS \"inclusaoDisciplinaForaGrade.codigo\", curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\",  ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" matricula.matricula ");
		sb.append(" from inclusaoDisciplinaForaGrade ");
		sb.append(" inner join matricula on matricula.matricula = inclusaoDisciplinaForaGrade.matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join disciplinaForaGrade on disciplinaForaGrade.inclusaoDisciplinaForaGrade = inclusaoDisciplinaForaGrade.codigo ");
		return sb;
	}

	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder sb = new StringBuilder();
		sb.append("select inclusaoDisciplinaForaGrade.codigo AS \"inclusaoDisciplinaForaGrade.codigo\", curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\",  ");
		sb.append(" curso.nivelEducacional AS \"curso.nivelEducacional\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" matricula.matricula,  ");
		sb.append(" disciplinaForaGrade.codigo AS \"disciplinaForaGrade.codigo\", disciplinaForaGrade.disciplina AS \"disciplinaForaGrade.disciplina\", ");
		sb.append(" disciplinaForaGrade.usarNotaConceito AS \"disciplinaForaGrade.usarNotaConceito\", disciplinaForaGrade.notaConceito AS \"disciplinaForaGrade.notaConceito\", disciplinaForaGrade.nota AS \"disciplinaForaGrade.nota\", disciplinaForaGrade.frequencia AS \"disciplinaForaGrade.frequencia\", disciplinaForaGrade.situacao AS \"disciplinaForaGrade.situacao\", ");
		sb.append(" disciplinaForaGrade.ano AS \"disciplinaForaGrade.ano\", disciplinaForaGrade.semestre AS \"disciplinaForaGrade.semestre\", disciplinaForaGrade.cargaHoraria AS \"disciplinaForaGrade.cargaHoraria\", disciplinaForaGrade.cargaHorariaCursada AS \"disciplinaForaGrade.cargaHorariaCursada\", ");
		sb.append(" disciplinaForaGrade.numeroCredito AS \"disciplinaForaGrade.numeroCredito\", ");
		sb.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.periodoLetivo AS \"periodoLetivo.periodoLetivo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		sb.append(" instituicaoEnsino, cidade.nome AS \"cidade.nome\", cidade.codigo AS \"cidade.codigo\", estado.sigla AS \"estado.sigla\", estado.codigo AS \"estado.codigo\" ");
		sb.append(" from inclusaoDisciplinaForaGrade ");
		sb.append(" inner join matricula on matricula.matricula = inclusaoDisciplinaForaGrade.matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join disciplinaForaGrade on disciplinaForaGrade.inclusaoDisciplinaForaGrade = inclusaoDisciplinaForaGrade.codigo ");
		sb.append(" left join periodoLetivo on disciplinaForaGrade.periodoLetivo = periodoLetivo.codigo ");
		sb.append(" left join cidade on disciplinaForaGrade.cidade = cidade.codigo ");
		sb.append(" left join estado on cidade.estado = estado.codigo ");
		
		return sb;
	}

	public List<InclusaoDisciplinaForaGradeVO> consultaRapidaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sb = getSQLPadraoConsultaBasica();
		sb.append(" where matricula.matricula = ? ");		
		sb.append(" order by curso.nome, matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), matricula);
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public List<InclusaoDisciplinaForaGradeVO> consultaRapidaPorNome(String nome, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sb = getSQLPadraoConsultaBasica();
		sb.append(" where sem_acentos(pessoa.nome) ilike (?) ");
		sb.append(" order by curso.nome, pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), nome + PERCENT);
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public List<InclusaoDisciplinaForaGradeVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) {
		List<InclusaoDisciplinaForaGradeVO> listaInclusao = new ArrayList<InclusaoDisciplinaForaGradeVO>(0);
		while (tabelaResultado.next()) {
			InclusaoDisciplinaForaGradeVO obj = new InclusaoDisciplinaForaGradeVO();
			montarDadosBasico(obj, tabelaResultado);
			listaInclusao.add(obj);
		}
		return listaInclusao;
	}

	public void carregarDados(InclusaoDisciplinaForaGradeVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((InclusaoDisciplinaForaGradeVO) obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((InclusaoDisciplinaForaGradeVO) obj, resultado);
		}
	}

	public void montarDadosBasico(InclusaoDisciplinaForaGradeVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("inclusaoDisciplinaForaGrade.codigo"));
		obj.getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
	}

	public void montarDadosCompleto(InclusaoDisciplinaForaGradeVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("inclusaoDisciplinaForaGrade.codigo"));
		obj.getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getMatriculaVO().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
		obj.getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));

		DisciplinaForaGradeVO disciplinaForaGradeVO = null;
		obj.getDisciplinaForaGradeVOs().clear();
		do {
			disciplinaForaGradeVO = new DisciplinaForaGradeVO();

			disciplinaForaGradeVO.setCodigo(dadosSQL.getInt("disciplinaForaGrade.codigo"));
			disciplinaForaGradeVO.getInclusaoDisciplinaForaGradeVO().setCodigo(dadosSQL.getInt("inclusaoDisciplinaForaGrade.codigo"));
			if (disciplinaForaGradeVO.getInclusaoDisciplinaForaGradeVO().getCodigo() == null || disciplinaForaGradeVO.getInclusaoDisciplinaForaGradeVO().getCodigo().equals(0)) {
				continue;
			}
			disciplinaForaGradeVO.setDisciplina(dadosSQL.getString("disciplinaForaGrade.disciplina"));
			disciplinaForaGradeVO.setNota(dadosSQL.getDouble("disciplinaForaGrade.nota"));
			disciplinaForaGradeVO.setNotaConceito(dadosSQL.getString("disciplinaForaGrade.notaConceito"));
			disciplinaForaGradeVO.setUsarNotaConceito(dadosSQL.getBoolean("disciplinaForaGrade.usarNotaConceito"));
			disciplinaForaGradeVO.setFrequencia(dadosSQL.getDouble("disciplinaForaGrade.frequencia"));
			disciplinaForaGradeVO.setSituacao(dadosSQL.getString("disciplinaForaGrade.situacao"));
			disciplinaForaGradeVO.setAno(dadosSQL.getString("disciplinaForaGrade.ano"));
			disciplinaForaGradeVO.setSemestre(dadosSQL.getString("disciplinaForaGrade.semestre"));
			disciplinaForaGradeVO.setCargaHoraria(dadosSQL.getInt("disciplinaForaGrade.cargaHoraria"));
			disciplinaForaGradeVO.setCargaHorariaCursada(dadosSQL.getInt("disciplinaForaGrade.cargaHorariaCursada"));
			disciplinaForaGradeVO.setNumeroCredito(dadosSQL.getInt("disciplinaForaGrade.numeroCredito"));
			disciplinaForaGradeVO.getPeriodoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
			disciplinaForaGradeVO.getPeriodoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
			disciplinaForaGradeVO.getPeriodoLetivo().setPeriodoLetivo(dadosSQL.getInt("periodoLetivo.periodoLetivo"));
			disciplinaForaGradeVO.setInstituicaoEnsino(dadosSQL.getString("instituicaoEnsino"));
			disciplinaForaGradeVO.getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
			disciplinaForaGradeVO.getCidade().setNome(dadosSQL.getString("cidade.nome"));
			disciplinaForaGradeVO.getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
			disciplinaForaGradeVO.getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
			disciplinaForaGradeVO.setInclusaoDisciplinaForaGradeVO(obj);
			obj.getDisciplinaForaGradeVOs().add(disciplinaForaGradeVO);
			if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("inclusaoDisciplinaForaGrade.codigo")))) {
				return;
			}
		} while (dadosSQL.next());
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codInclusaoDisciplinaForaGrade, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (InclusaoDisciplinaForaGrade.codigo= ").append(codInclusaoDisciplinaForaGrade).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados Disciplina Fora da Grade.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codInclusaoDisciplinaForaGrade, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (InclusaoDisciplinaForaGrade.codigo= ").append(codInclusaoDisciplinaForaGrade).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados Disciplina Fora da Grade.");
		}
		return tabelaResultado;
	}

	public List<InclusaoDisciplinaForaGradeVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) throws Exception {
		if (campoConsulta.equals("nome")) {
			return consultaRapidaPorNome(valorConsulta, usuarioVO);
		} else if (campoConsulta.equals("matricula")) {
			return consultaRapidaPorMatricula(valorConsulta, usuarioVO);
		}
		return new ArrayList<>();
	}

	public List<MatriculaVO> consultarAluno(String campoConsultaAluno, String valorConsultaAluno, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
		if (campoConsultaAluno.equals("")) {
			throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
		}
		if (campoConsultaAluno.equals("matricula")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(valorConsultaAluno, unidadeEnsino, false, usuarioVO);
		}
		if (campoConsultaAluno.equals("nomePessoa")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(valorConsultaAluno, unidadeEnsino, false, usuarioVO);
		}
		if (campoConsultaAluno.equals("nomeCurso")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(valorConsultaAluno, unidadeEnsino, false, usuarioVO);
		}
		return objs;
	}

	public MatriculaVO consultarAlunoPorMatricula(MatriculaVO matriculaVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaVO.getMatricula(), unidadeEnsino, NivelMontarDados.BASICO, usuarioVO);
		if (objAluno.getMatricula().equals("")) {
			throw new Exception("Aluno de matrícula " + matriculaVO.getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
		}
		return objAluno;
	}

	public void adicionarDisciplinaForaGrade(InclusaoDisciplinaForaGradeVO obj, DisciplinaForaGradeVO disciplinaForaGradeVO, UsuarioVO usuarioVO) throws Exception {
		disciplinaForaGradeVO.setInclusaoDisciplinaForaGradeVO(obj);
		getFacadeFactory().getDisciplinaForaGradeFacade().validarDados(disciplinaForaGradeVO, obj.getMatriculaVO().getCurso().getPeriodicidade());
		disciplinaForaGradeVO.setPeriodoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(disciplinaForaGradeVO.getPeriodoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		int index = 0;
		Iterator<DisciplinaForaGradeVO> i = obj.getDisciplinaForaGradeVOs().iterator();
		while (i.hasNext()) {
			DisciplinaForaGradeVO objExistente = (DisciplinaForaGradeVO) i.next();
			if (objExistente.getDisciplina().trim().equals(disciplinaForaGradeVO.getDisciplina().trim())
					&& objExistente.getPeriodoLetivo().getCodigo().equals(disciplinaForaGradeVO.getPeriodoLetivo().getCodigo()) 
					&& objExistente.getAno().equals(disciplinaForaGradeVO.getAno()) 
					&& objExistente.getSemestre().equals(disciplinaForaGradeVO.getSemestre())) {
				obj.getDisciplinaForaGradeVOs().set(index, disciplinaForaGradeVO);
				return;
			}
			index++;
		}
		obj.getDisciplinaForaGradeVOs().add(disciplinaForaGradeVO);
	}

	public void removerDisciplinaForaPrazo(InclusaoDisciplinaForaGradeVO obj, DisciplinaForaGradeVO disciplinaForaGradeVO, UsuarioVO usuarioVO) {
		int index = 0;
		Iterator<DisciplinaForaGradeVO> i = obj.getDisciplinaForaGradeVOs().iterator();
		while (i.hasNext()) {
			DisciplinaForaGradeVO objExistente = (DisciplinaForaGradeVO) i.next();
			if (objExistente.getDisciplina().equals(disciplinaForaGradeVO.getDisciplina()) 
					&& objExistente.getPeriodoLetivo().getCodigo().equals(disciplinaForaGradeVO.getPeriodoLetivo().getCodigo())
					&& objExistente.getAno().equals(disciplinaForaGradeVO.getAno()) 
					&& objExistente.getSemestre().equals(disciplinaForaGradeVO.getSemestre())) {
				obj.getDisciplinaForaGradeVOs().remove(index);
				return;
			}
			index++;
		}
	}

}
