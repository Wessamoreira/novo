package negocio.facade.jdbc.academico;

import static java.util.stream.Collectors.joining;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

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

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LogImpactoMatrizCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TituloImpactoMatrizCurricularEnum;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.GradeDisciplinaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>GradeDisciplinaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>GradeDisciplinaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see GradeDisciplinaVO
 * @see ControleAcesso
 * @see PeriodoLetivo
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class GradeDisciplina extends ControleAcesso implements GradeDisciplinaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public GradeDisciplina() throws Exception {
		super();
		setIdEntidade("Curso");
	}

	public GradeDisciplinaVO novo() throws Exception {
		GradeDisciplina.incluir(getIdEntidade());
		GradeDisciplinaVO obj = new GradeDisciplinaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GradeDisciplinaVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		try{ 
		GradeDisciplinaVO.validarDados(obj, "", situacaoGradeCurricular);
		validarDadosFormulaCalculoComposicao(obj);
		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO GradeDisciplina(periodoLetivo, disciplina, modalidadeDisciplina, disciplinaTCC, disciplinaEixoTematico, cargahoraria, cargahorariapratica, nrcreditos, ");
		sql.append("tipodisciplina, configuracaoacademico, diversificada, nrcreditofinanceiro, disciplinaComposta, formulaCalculoNota, formulaCalculo, areaConhecimento, horaAula, nomeChancela, ");
		sql.append("tipoControleComposicao, numeroMaximoDisciplinaComposicaoEstudar, validarPreRequisitoDisciplinaFazParteComposicao, disciplinaEstagio, controlarRecuperacaoPelaDisciplinaPrincipal, condicaoUsoRecuperacao, variavelNotaCondicaoUsoRecuperacao, formulaCalculoNotaRecuperada, variavelNotaFormulaCalculoNotaRecuperada, variavelNotaRecuperacao, formulaCalculoNotaRecuperacao, numeroMinimoDisciplinaComposicaoEstudar, ");
		sql.append(" ordem, bimestre, utilizarEmissaoXmlDiploma ) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setInt(1, obj.getPeriodoLetivo().intValue());
				sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
				sqlInserir.setString(3, obj.getModalidadeDisciplina().name());
				sqlInserir.setBoolean(4, obj.getDisciplinaTCC());
				if (obj.getDisciplinaEixoTematico().getCodigo() > 0) {
					sqlInserir.setInt(5, obj.getDisciplinaEixoTematico().getCodigo().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}
				sqlInserir.setInt(6, obj.getCargaHoraria());
				sqlInserir.setInt(7, obj.getCargaHorariaPratica());
				sqlInserir.setInt(8, obj.getNrCreditos());
				sqlInserir.setString(9, obj.getTipoDisciplina());
				if (obj.getConfiguracaoAcademico() != null && obj.getConfiguracaoAcademico().getCodigo() > 0) {
					sqlInserir.setInt(10, obj.getConfiguracaoAcademico().getCodigo().intValue());
				} else {
					sqlInserir.setNull(10, 0);
				}
				sqlInserir.setBoolean(11, obj.getDiversificada());
				sqlInserir.setDouble(12, obj.getNrCreditoFinanceiro());
				sqlInserir.setBoolean(13, obj.getDisciplinaComposta());
				sqlInserir.setString(14, obj.getFormulaCalculoNota().name());
				sqlInserir.setString(15, obj.getFormulaCalculo());
//				if (obj.getAreaConhecimentoVO() != null && obj.getAreaConhecimentoVO().getCodigo() > 0) {
//					sqlInserir.setInt(16, obj.getAreaConhecimentoVO().getCodigo().intValue());
//				} else {
//					sqlInserir.setNull(16, 0);
//				}
				sqlInserir.setInt(17, obj.getHoraAula());
				sqlInserir.setString(18, obj.getNomeChancela());
				sqlInserir.setString(19, obj.getTipoControleComposicao().getValor());
				sqlInserir.setInt(20, obj.getNumeroMaximoDisciplinaComposicaoEstudar());
				sqlInserir.setBoolean(21, obj.getValidarPreRequisitoDisciplinaFazParteComposicao());
				sqlInserir.setBoolean(22, obj.getDisciplinaEstagio());
				sqlInserir.setBoolean(23, obj.getControlarRecuperacaoPelaDisciplinaPrincipal());
				sqlInserir.setString(24, obj.getCondicaoUsoRecuperacao());
				sqlInserir.setString(25, obj.getVariavelNotaCondicaoUsoRecuperacao());
				sqlInserir.setString(26, obj.getFormulaCalculoNotaRecuperada());
				sqlInserir.setString(27, obj.getVariavelNotaFormulaCalculoNotaRecuperada());
				sqlInserir.setString(28, obj.getVariavelNotaRecuperacao());
				sqlInserir.setString(29, obj.getFormulaCalculoNotaRecuperacao());
				sqlInserir.setInt(30, obj.getNumeroMinimoDisciplinaComposicaoEstudar());
				sqlInserir.setInt(31, obj.getOrdem());				
				sqlInserir.setInt(32, obj.getBimestre());
				sqlInserir.setBoolean(33, obj.getUtilizarEmissaoXmlDiploma());
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

		obj.setNovoObj(Boolean.FALSE);
		getFacadeFactory().getDisciplinaPreRequisitoFacade().incluirDisciplinaPreRequisitos(obj.getCodigo(), obj.getDisciplinaRequisitoVOs(), usuario);
		getFacadeFactory().getGradeDisciplinaCompostaFacade().incluirGradeDisciplinaCompostaVOsPorGradeDisciplina(obj, situacaoGradeCurricular, usuario);
		} catch (Exception e) {
			obj.setCodigo(0);
            throw e;
        }
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GradeDisciplinaVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		try {
			GradeDisciplinaVO.validarDados(obj, "", situacaoGradeCurricular);
			validarDadosFormulaCalculoComposicao(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE GradeDisciplina set periodoLetivo = ?, disciplina = ?, modalidadeDisciplina = ?, disciplinaTCC = ?, disciplinaEixoTematico=?, ");
			sql.append("cargahoraria=?, cargahorariapratica=?, nrcreditos=?, tipodisciplina=?, configuracaoacademico=?, diversificada=?, nrcreditofinanceiro = ?, disciplinaComposta = ?, formulaCalculoNota = ?, formulaCalculo = ?, areaConhecimento = ?, horaAula = ?, nomeChancela = ?, ");
			sql.append("tipoControleComposicao=?, numeroMaximoDisciplinaComposicaoEstudar=?, validarPreRequisitoDisciplinaFazParteComposicao=?, disciplinaEstagio=?, controlarRecuperacaoPelaDisciplinaPrincipal=?, condicaoUsoRecuperacao=?, variavelNotaCondicaoUsoRecuperacao=?, formulaCalculoNotaRecuperada=?, variavelNotaFormulaCalculoNotaRecuperada=?, variavelNotaRecuperacao = ?, formulaCalculoNotaRecuperacao = ?, numeroMinimoDisciplinaComposicaoEstudar = ?, ");
			sql.append("ordem = ?, bimestre = ?, utilizarEmissaoXmlDiploma=? ");
			sql.append("WHERE (codigo = ?) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getPeriodoLetivo().intValue());
					sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlAlterar.setString(3, obj.getModalidadeDisciplina().name());
					sqlAlterar.setBoolean(4, obj.getDisciplinaTCC());
					if (obj.getDisciplinaEixoTematico().getCodigo() > 0) {
						sqlAlterar.setInt(5, obj.getDisciplinaEixoTematico().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setInt(6, obj.getCargaHoraria());
					sqlAlterar.setInt(7, obj.getCargaHorariaPratica());
					sqlAlterar.setInt(8, obj.getNrCreditos());
					sqlAlterar.setString(9, obj.getTipoDisciplina());
					if (obj.getConfiguracaoAcademico() != null && obj.getConfiguracaoAcademico().getCodigo() > 0) {
						sqlAlterar.setInt(10, obj.getConfiguracaoAcademico().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setBoolean(11, obj.getDiversificada());
					sqlAlterar.setDouble(12, obj.getNrCreditoFinanceiro());
					sqlAlterar.setBoolean(13, obj.getDisciplinaComposta());
					sqlAlterar.setString(14, obj.getFormulaCalculoNota().name());
					sqlAlterar.setString(15, obj.getFormulaCalculo());
//					if (obj.getAreaConhecimentoVO() != null && obj.getAreaConhecimentoVO().getCodigo() > 0) {
//						sqlAlterar.setInt(16, obj.getAreaConhecimentoVO().getCodigo().intValue());
//					} else {
//						sqlAlterar.setNull(16, 0);
//					}
					sqlAlterar.setInt(17, obj.getHoraAula());
					sqlAlterar.setString(18, obj.getNomeChancela());
					sqlAlterar.setString(19, obj.getTipoControleComposicao().getValor());
					sqlAlterar.setInt(20, obj.getNumeroMaximoDisciplinaComposicaoEstudar());
					sqlAlterar.setBoolean(21, obj.getValidarPreRequisitoDisciplinaFazParteComposicao());
					sqlAlterar.setBoolean(22, obj.getDisciplinaEstagio());
					sqlAlterar.setBoolean(23, obj.getControlarRecuperacaoPelaDisciplinaPrincipal());
					sqlAlterar.setString(24, obj.getCondicaoUsoRecuperacao());
					sqlAlterar.setString(25, obj.getVariavelNotaCondicaoUsoRecuperacao());
					sqlAlterar.setString(26, obj.getFormulaCalculoNotaRecuperada());
					sqlAlterar.setString(27, obj.getVariavelNotaFormulaCalculoNotaRecuperada());
					sqlAlterar.setString(28, obj.getVariavelNotaRecuperacao());
					sqlAlterar.setString(29, obj.getFormulaCalculoNotaRecuperacao());
					sqlAlterar.setInt(30, obj.getNumeroMinimoDisciplinaComposicaoEstudar().intValue());
					sqlAlterar.setInt(31, obj.getOrdem());
					sqlAlterar.setInt(32, obj.getBimestre());
					sqlAlterar.setBoolean(33, obj.getUtilizarEmissaoXmlDiploma());
					sqlAlterar.setInt(34, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, situacaoGradeCurricular, usuario);
				return;
			}
			getFacadeFactory().getDisciplinaPreRequisitoFacade().alterarDisciplinaPreRequisitos(obj.getCodigo(), obj.getDisciplinaRequisitoVOs(), usuario);
			getFacadeFactory().getGradeDisciplinaCompostaFacade().alterarGradeDisciplinaCompostaVOsPorGradeDisciplina(obj, situacaoGradeCurricular, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarDefinirDisciplinaUtilizaTCC(final Integer gradeDisciplina, UsuarioVO usuario) throws Exception {

		final String sql = "UPDATE GradeDisciplina set disciplinaTCC = true WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, gradeDisciplina);
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GradeDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		try {
			GradeDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM GradeDisciplina WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<GradeDisciplinaVO> consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* FROM GradeDisciplina inner join disciplina on GradeDisciplina.disciplina = Disciplina.codigo  inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo  WHERE  lower (Disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	public GradeDisciplinaVO consultarPorNomeDisciplina(String valorConsulta, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito,periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,GradeDisciplina.* , PeriodoLetivo.* FROM GradeDisciplina, Disciplina , PeriodoLetivo, GradeCurricular WHERE GradeDisciplina.disciplina = Disciplina.codigo " + "and PeriodoLetivo.codigo = GradeDisciplina.periodoLetivo and GradeCurricular.curso = " + curso + " and  lower (Disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * consultarPorCodigoPeriodoLetivo(java.lang.Integer, boolean)
	 */
	public List<GradeDisciplinaVO> consultarPorCodigoPeriodoLetivo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.*  FROM GradeDisciplina, PeriodoLetivo WHERE GradeDisciplina.periodoLetivo = PeriodoLetivo.codigo and PeriodoLetivo.codigo >= " + valorConsulta.intValue() + " ORDER BY PeriodoLetivo.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	@Override
	public List<GradeDisciplinaVO> consultarDadosParaMigracaoGradePorGradeCurricular(Integer gradeCurricular) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT gradeDisciplina.codigo, gradeDisciplina.periodoLetivo, periodoLetivo.descricao as \"periodoLetivo.descricao\",periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\",  ");
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", gradeDisciplina.cargaHoraria as \"gradeDisciplina.cargaHoraria\", ");
		sql.append(" modalidadeDisciplina , disciplinaTCC, disciplinaEixoTematico ");
		sql.append(" FROM GradeDisciplina ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = GradeDisciplina.periodoletivo ");
		sql.append(" inner join disciplina on disciplina.codigo = GradeDisciplina.disciplina ");
		sql.append(" where periodoletivo.gradecurricular =  ").append(gradeCurricular);
		List<GradeDisciplinaVO> gradeDisciplinaVOs = new ArrayList<GradeDisciplinaVO>(0);
		GradeDisciplinaVO gradeDisciplinaVO = null;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
			gradeDisciplinaVO.setCodigo(rs.getInt("codigo"));
			gradeDisciplinaVO.setCargaHoraria(rs.getInt("gradeDisciplina.cargaHoraria"));
			gradeDisciplinaVO.setDisciplinaTCC(rs.getBoolean("disciplinaTCC"));
			gradeDisciplinaVO.getDisciplinaEixoTematico().setCodigo(new Integer(rs.getInt("disciplinaEixoTematico")));
			gradeDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(rs.getString("modalidadeDisciplina")));
			gradeDisciplinaVO.setPeriodoLetivo(rs.getInt("periodoLetivo"));
			gradeDisciplinaVO.getPeriodoLetivoVO().setCodigo(rs.getInt("periodoLetivo"));
			gradeDisciplinaVO.getPeriodoLetivoVO().setDescricao(rs.getString("periodoLetivo.descricao"));
			gradeDisciplinaVO.getPeriodoLetivoVO().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
			gradeDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
			gradeDisciplinaVO.getDisciplina().setNome(rs.getString("disciplina.nome"));

			gradeDisciplinaVO.setNovoObj(false);
			gradeDisciplinaVOs.add(gradeDisciplinaVO);
		}
		return gradeDisciplinaVOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * consultarPorCodigoPeriodoLetivoCodigoDisciplina( java.lang.Integer,
	 * java.lang.Integer, boolean)
	 */
	public GradeDisciplinaVO consultarPorCodigoPeriodoLetivoCodigoDisciplina(Integer periodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT  ( SELECT COUNT(*) FROM disciplinaprerequisito dr WHERE dr.gradedisciplina = gradedisciplina.codigo) AS totalprerequisito, ");
		sqlStr.append(" periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* ");
		sqlStr.append(" from gradedisciplina inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo  ");
		sqlStr.append(" WHERE gradedisciplina.periodoLetivo = ? AND gradedisciplina.disciplina = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { periodo, disciplina });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return new GradeDisciplinaVO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * consultarPorCodigo(java.lang.Integer, boolean)
	 */
	public List<GradeDisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* FROM GradeDisciplina inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo WHERE GradeDisciplina.codigo >= " + valorConsulta.intValue() + " ORDER BY GradeDisciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>GradeDisciplinaVO</code> resultantes da consulta.
	 */
	public List<GradeDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GradeDisciplinaVO> vetResultado = new ArrayList<GradeDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>GradeDisciplinaVO</code>.
	 * 
	 * @return O objeto da classe <code>GradeDisciplinaVO</code> com os dados
	 *         devidamente montados.
	 */
	public GradeDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GradeDisciplinaVO obj = new GradeDisciplinaVO();
		obj.setPeriodoLetivo(new Integer(dadosSQL.getInt("periodoLetivo")));
		obj.getPeriodoLetivoVO().setCodigo(new Integer(dadosSQL.getInt("periodoLetivo")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setDisciplinaTCC(dadosSQL.getBoolean("disciplinaTCC"));
		obj.getDisciplinaEixoTematico().setCodigo(new Integer(dadosSQL.getInt("disciplinaEixoTematico")));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeDisciplina")));
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargahoraria")));
		obj.setCargaHorariaPratica(new Integer(dadosSQL.getInt("cargahorariapratica")));
		obj.setCargaHorariaTeorica(obj.getCargaHoraria() - obj.getCargaHorariaPratica());
		obj.setNrCreditos(new Integer(dadosSQL.getInt("nrcreditos")));
		obj.setTipoControleComposicao(TipoControleComposicaoEnum.valueOf(dadosSQL.getString("tipoControleComposicao")));
		obj.setNumeroMaximoDisciplinaComposicaoEstudar(dadosSQL.getInt("numeroMaximoDisciplinaComposicaoEstudar"));
		obj.setNumeroMinimoDisciplinaComposicaoEstudar(dadosSQL.getInt("numeroMinimoDisciplinaComposicaoEstudar"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.setBimestre(dadosSQL.getInt("bimestre"));
		obj.setValidarPreRequisitoDisciplinaFazParteComposicao(dadosSQL.getBoolean("validarPreRequisitoDisciplinaFazParteComposicao"));
		Integer nrCreditosSemSingleton = (Integer) dadosSQL.getObject("nrcreditos");
		if (nrCreditosSemSingleton == null) {
			obj.setNrCreditosSemSingleton("");
		} else {
			obj.setNrCreditosSemSingleton(nrCreditosSemSingleton.toString());
		}
		obj.setNrCreditoFinanceiro(dadosSQL.getDouble("nrcreditofinanceiro"));
		obj.setTipoDisciplina(dadosSQL.getString("tipodisciplina"));
		obj.setDiversificada(dadosSQL.getBoolean("diversificada"));
		obj.setDisciplinaComposta(dadosSQL.getBoolean("disciplinaComposta"));		
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoacademico"));
//		obj.getAreaConhecimentoVO().setCodigo(dadosSQL.getInt("areaConhecimento"));
		obj.setDisciplinaEstagio(dadosSQL.getBoolean("disciplinaEstagio"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("formulaCalculoNota"))) {
			obj.setFormulaCalculoNota(FormulaCalculoNotaEnum.getEnum(dadosSQL.getString("formulaCalculoNota")));
		}
		obj.setHoraAula(dadosSQL.getInt("horaAula"));
		obj.setFormulaCalculo(dadosSQL.getString("formulaCalculo"));
		obj.setNomeChancela(dadosSQL.getString("nomeChancela"));
		obj.setControlarRecuperacaoPelaDisciplinaPrincipal(dadosSQL.getBoolean("controlarRecuperacaoPelaDisciplinaPrincipal"));
		obj.setCondicaoUsoRecuperacao(dadosSQL.getString("condicaoUsoRecuperacao"));
		obj.setVariavelNotaCondicaoUsoRecuperacao(dadosSQL.getString("variavelNotaCondicaoUsoRecuperacao"));
		obj.setFormulaCalculoNotaRecuperada(dadosSQL.getString("formulaCalculoNotaRecuperada"));
		obj.setVariavelNotaFormulaCalculoNotaRecuperada(dadosSQL.getString("variavelNotaFormulaCalculoNotaRecuperada"));
		obj.setVariavelNotaRecuperacao(dadosSQL.getString("variavelNotaRecuperacao"));
		obj.setFormulaCalculoNotaRecuperacao(dadosSQL.getString("formulaCalculoNotaRecuperacao"));
		obj.setNovoObj(Boolean.FALSE);
		obj.getPeriodoLetivoVO().setCodigo(new Integer(dadosSQL.getInt("codigoperiodoletivo")));
		obj.getPeriodoLetivoVO().setDescricao(dadosSQL.getString("descricaoperiodoletivo"));
		obj.getPeriodoLetivoVO().setPeriodoLetivo(new Integer(dadosSQL.getInt("periodoletivoperiodoletivo")));
		obj.setUtilizarEmissaoXmlDiploma(dadosSQL.getBoolean("utilizarEmissaoXmlDiploma"));
		montarDadosDisciplina(obj, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("totalprerequisito")) && dadosSQL.getInt("totalprerequisito") > 0) {
			obj.setDisciplinaRequisitoVOs(getFacadeFactory().getDisciplinaPreRequisitoFacade().consultarDisciplinaPreRequisitos(obj.getCodigo(), false, usuario));
			Ordenacao.ordenarLista(obj.getDisciplinaRequisitoVOs(), "ordenacao");
		}
		
		
		if (Uteis.isAtributoPreenchido(dadosSQL.getBoolean("disciplinacomposta")) && dadosSQL.getBoolean("disciplinacomposta")) {
			obj.setGradeDisciplinaCompostaVOs(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGradeDisciplina(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		
		
		//obj.setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeriodoLetivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		// Montar Dados NIVELMONTARDADOS_COMBOBOX Perido Letivo
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (obj.getDisciplinaEixoTematico().getCodigo() > 0) {
			obj.setDisciplinaEixoTematico(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaEixoTematico().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}

		// if (obj.getConfiguracaoAcademico() != null &&
		// obj.getConfiguracaoAcademico().getCodigo() > 0) {
		// obj.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(),
		// usuario));
		// }

		return obj;
	}

	public  GradeDisciplinaVO montarDadosResumido(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		GradeDisciplinaVO obj = new GradeDisciplinaVO();
		// obj.setPeriodoLetivo(new Integer(dadosSQL.getInt("periodoLetivo")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		// obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNovoObj(Boolean.FALSE);
		// obj.setDisciplinaRequisitoVOs(DisciplinaPreRequisito.consultarDisciplinaPreRequisitos(obj.getCodigo(),
		// false, usuario));
		Ordenacao.ordenarLista(obj.getDisciplinaRequisitoVOs(), "ordenacao");
		montarDadosDisciplina(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>DisciplinaVO</code> relacionado ao objeto
	 * <code>GradeDisciplinaVO</code>. Faz uso da chave primária da classe
	 * <code>DisciplinaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosDisciplina(GradeDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	public static void montarDadosAreaConhecimento(GradeDisciplinaVO obj, UsuarioVO usuario) throws Exception {
//		if (obj.getAreaConhecimentoVO().getCodigo().intValue() == 0) {
//			obj.setAreaConhecimentoVO(new AreaConhecimentoVO());
//			return;
//		}
//		obj.setAreaConhecimentoVO(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimentoVO().getCodigo(), usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * excluirGradeDisciplinas(negocio.comuns.academico .PeriodoLetivoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGradeDisciplinas(PeriodoLetivoVO periodoLetivo, UsuarioVO usuario) throws Exception {
//		Iterator<GradeDisciplinaVO> i = periodoLetivo.getGradeDisciplinaVOs().iterator();
//		while (i.hasNext()) {
//			GradeDisciplinaVO grade = (GradeDisciplinaVO) i.next();
//			getFacadeFactory().getDisciplinaPreRequisitoFacade().excluirDisciplinaPreRequisitos(grade.getCodigo(), usuario);
//		}
		GradeDisciplina.excluir(getIdEntidade());
		String sql = "DELETE FROM GradeDisciplina WHERE (periodoLetivo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { periodoLetivo.getCodigo() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * alterarGradeDisciplinas(java.lang.Integer, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGradeDisciplinas(Integer periodoLetivo, List<GradeDisciplinaVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		incluirGradeDisciplinas(periodoLetivo, objetos, situacaoGradeCurricular, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * incluirGradeDisciplinas(java.lang.Integer, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirGradeDisciplinas(Integer periodoLetivo, List<GradeDisciplinaVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		Iterator<GradeDisciplinaVO> e = objetos.iterator();
		while (e.hasNext()) {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) e.next();
			obj.setPeriodoLetivo(periodoLetivo);
			if (obj.getCodigo().equals(0)) {
				incluir(obj, situacaoGradeCurricular, usuario);
			} else {
				alterar(obj, situacaoGradeCurricular, usuario);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>GradeDisciplinaVO</code> relacionados a um objeto da classe
	 * <code>academico.PeriodoLetivo</code>.
	 * 
	 * @param gradeCurricular
	 *            Atributo de <code>academico.PeriodoLetivo</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>GradeDisciplinaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>GradeDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List<GradeDisciplinaVO> consultarGradeDisciplinas(Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT ( SELECT COUNT(*) FROM disciplinaprerequisito dr WHERE dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* FROM GradeDisciplina inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo  WHERE GradeDisciplina.periodoLetivo = "+periodoLetivo+" order by GradeDisciplina.ordem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	public List<GradeDisciplinaVO> consultarGradeDisciplinasEquivalentes(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* FROM GradeDisciplina inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo  ";
		sqlStr += "LEFT JOIN disciplinaequivalente ON disciplinaequivalente.equivalente = gradedisciplina.disciplina ";
		sqlStr += "WHERE gradedisciplina.disciplina IN ( ";
		sqlStr += "SELECT equivalente from disciplinaequivalente WHERE disciplina = ? )";
		// sqlStr +=
		// " left join disciplina ON disciplina.codigo = gradedisciplina.disciplina";
		// sqlStr +=
		// " left JOIN disciplinaequivalente ON disciplinaequivalente.disciplina = disciplina.codigo";
		// sqlStr +=
		// " WHERE gradeDisciplina.disciplina = disciplinaequivalente.disciplina and disciplinaequivalente.equivalente = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { disciplina });
		return montarDadosConsulta(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	public List<GradeDisciplinaVO> consultarGradeDisciplinasEquivalenteGradeAtiva(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* FROM GradeDisciplina ";
		sqlStr += "LEFT JOIN disciplinaequivalente ON disciplinaequivalente.equivalente = gradedisciplina.disciplina ";
		sqlStr += "LEFT JOIN periodoLetivo ON periodoLetivo.codigo = gradedisciplina.periodoLetivo ";
		sqlStr += "LEFT JOIN gradecurricular ON periodoLetivo.gradecurricular = gradecurricular.codigo ";
		sqlStr += "WHERE gradedisciplina.disciplina IN ( ";
		sqlStr += "SELECT equivalente from disciplinaequivalente WHERE disciplina = ? ) ";
		sqlStr += "and gradecurricular.situacao = 'AT'";
		// sqlStr +=
		// " left join disciplina ON disciplina.codigo = gradedisciplina.disciplina";
		// sqlStr +=
		// " left JOIN disciplinaequivalente ON disciplinaequivalente.disciplina = disciplina.codigo";
		// sqlStr +=
		// " WHERE gradeDisciplina.disciplina = disciplinaequivalente.disciplina and disciplinaequivalente.equivalente = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { disciplina });
		return montarDadosConsulta(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	public List<GradeDisciplinaVO> consultarGradeDisciplinasEquivalentesPorGradeDisciplina(Integer gradeDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT distinct ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* FROM GradeDisciplina ");
		sqlStr.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo  ");
		sqlStr.append("LEFT JOIN disciplinaequivalente ON disciplinaequivalente.equivalente = gradedisciplina.disciplina ");
		sqlStr.append("WHERE gradedisciplina.disciplina IN ( ");
		sqlStr.append("SELECT de.equivalente from disciplinaequivalente de ");
		sqlStr.append("LEFT JOIN gradedisciplina gd ON de.disciplina = gd.disciplina ");
		sqlStr.append("WHERE gd.codigo = ? )");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { gradeDisciplina });
		return montarDadosConsulta(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer)
	 */
	public GradeDisciplinaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT  ( SELECT COUNT(*) FROM disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, ");
		sql.append(" periodoletivo.codigo AS codigoperiodoletivo,periodoletivo.descricao AS descricaoperiodoletivo,periodoletivo.periodoletivo AS periodoletivoperiodoletivo,GradeDisciplina.* ");
		sql.append(" FROM GradeDisciplina INNER JOIN periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append("  WHERE GradeDisciplina.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return GradeDisciplina.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.GradeDisciplinaInterfaceFacade#setIdEntidade
	 * (java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		GradeDisciplina.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>DisciplinaPreRequisitoVO</code> no List
	 * <code>disciplinaPreRequisitoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinaPreRequisito</code> -
	 * getPreRequisito().getCodigo() - como identificador (key) do objeto no
	 * List.
	 * 
	 * @param preRequisito
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjDisciplinaPreRequisitoVOs(Integer preRequisito, GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception {
		int index = 0;
		Iterator<DisciplinaPreRequisitoVO> i = gradeDisciplinaVO.getDisciplinaRequisitoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(preRequisito)) {
//				if (!objExistente.getCodigo().equals(0)) {
//					getFacadeFactory().getDisciplinaPreRequisitoFacade().excluir(objExistente, usuario);
//				}
				gradeDisciplinaVO.getDisciplinaRequisitoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void excluirPorCodigoDisciplinaPeriodoLetivo(Integer periodoLetivo, Integer disciplina, UsuarioVO usuario) throws Exception {
		try {
			TurmaDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM GradeDisciplina WHERE (periodoLetivo = ?) AND (disciplina = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { periodoLetivo, disciplina });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public GradeDisciplinaVO consultarGradeDisciplinaVOTCC(int gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT ( SELECT COUNT(*) FROM disciplinaprerequisito dr WHERE dr.gradedisciplina = gradedisciplina.codigo) AS totalprerequisito,  ");
		sql.append(" periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,GradeDisciplina.* ");
		sql.append(" FROM GradeDisciplina");
		sql.append(" inner join periodoletivo on GradeDisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append(" inner join disciplina on disciplina.codigo = GradeDisciplina.disciplina ");
		sql.append(" WHERE periodoletivo.gradecurricular = ? ");
		sql.append(" and (gradedisciplina.disciplinatcc = true or disciplina.classificacaoDisciplina = 'TCC') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { gradeCurricular });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return new GradeDisciplinaVO();
	}
	
	@Override
	public Boolean consultarDisciplinaAplicaTCC(int gradeCurricular, int disciplina) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT GradeDisciplina.codigo FROM GradeDisciplina ");
		sql.append(" inner join periodoletivo on GradeDisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append(" WHERE gradecurricular = ").append(gradeCurricular);
		sql.append(" and disciplina = ").append(disciplina);
		sql.append(" and disciplinatcc = true ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	public GradeDisciplinaVO consultarPorMatriculaDisciplina(String matricula, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct gradedisciplina.* from gradedisciplina ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = gradecurricular.codigo ");
		sql.append(" where matricula = '").append(matricula).append("'");
		if (disciplina != null && !disciplina.equals(0)) {
			sql.append(" and gradedisciplina.disciplina = ").append(disciplina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
			gradeDisciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
			gradeDisciplinaVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			gradeDisciplinaVO.getPeriodoLetivoVO().setCodigo(tabelaResultado.getInt("periodoletivo"));
			gradeDisciplinaVO.setDisciplinaTCC(tabelaResultado.getBoolean("disciplinatcc"));
			gradeDisciplinaVO.getDisciplinaEixoTematico().setCodigo(tabelaResultado.getInt("disciplinaeixotematico"));
			gradeDisciplinaVO.setCargaHoraria(tabelaResultado.getInt("cargahoraria"));
			gradeDisciplinaVO.setCargaHorariaPratica(tabelaResultado.getInt("cargahorariapratica"));
			gradeDisciplinaVO.setNrCreditos(tabelaResultado.getInt("nrcreditos"));

			gradeDisciplinaVO.setNrCreditoFinanceiro(tabelaResultado.getDouble("nrCreditoFinanceiro"));
			gradeDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidadeDisciplina")));

			gradeDisciplinaVO.setTipoDisciplina(tabelaResultado.getString("tipodisciplina"));
			gradeDisciplinaVO.getConfiguracaoAcademico().setCodigo(tabelaResultado.getInt("configuracaoacademico"));
			gradeDisciplinaVO.setDiversificada(tabelaResultado.getBoolean("diversificada"));
			return gradeDisciplinaVO;
		}
		return new GradeDisciplinaVO();
	}

	public GradeDisciplinaVO consultarPorMatriculaDisciplinaMatriculaPeriodo(String matricula, Integer matriculaPeriodo, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct gradedisciplina.* from gradedisciplina ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = gradecurricular.codigo ");
		sql.append(" where matricula = '").append(matricula).append("'");
		if (matriculaPeriodo != null && !matriculaPeriodo.equals(0)) {
			sql.append(" and matriculaperiodo.codigo = ").append(matriculaPeriodo);
		}
		if (disciplina != null && !disciplina.equals(0)) {
			sql.append(" and (gradedisciplina.disciplina = ").append(disciplina);
			sql.append(" or gradedisciplina.disciplina in(select disciplina from disciplinaequivalente where equivalente = ").append(disciplina);
			sql.append(")) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
			gradeDisciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
			gradeDisciplinaVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			gradeDisciplinaVO.getPeriodoLetivoVO().setCodigo(tabelaResultado.getInt("periodoletivo"));
			gradeDisciplinaVO.setDisciplinaTCC(tabelaResultado.getBoolean("disciplinatcc"));
			gradeDisciplinaVO.getDisciplinaEixoTematico().setCodigo(tabelaResultado.getInt("disciplinaeixotematico"));
			gradeDisciplinaVO.setCargaHoraria(tabelaResultado.getInt("cargahoraria"));
			gradeDisciplinaVO.setCargaHorariaPratica(tabelaResultado.getInt("cargahorariapratica"));
			gradeDisciplinaVO.setNrCreditos(tabelaResultado.getInt("nrcreditos"));

			gradeDisciplinaVO.setNrCreditoFinanceiro(tabelaResultado.getDouble("nrcreditoFinanceiro"));
			gradeDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidadeDisciplina")));

			gradeDisciplinaVO.setTipoDisciplina(tabelaResultado.getString("tipodisciplina"));
			gradeDisciplinaVO.getConfiguracaoAcademico().setCodigo(tabelaResultado.getInt("configuracaoacademico"));
			gradeDisciplinaVO.setDiversificada(tabelaResultado.getBoolean("diversificada"));
			return gradeDisciplinaVO;
		}
		return new GradeDisciplinaVO();
	}

	public GradeDisciplinaVO consultarPorGradeCurricularEDisciplina(Integer gradeCurricular, Integer disciplina, UsuarioVO usuarioVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO ) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct gradedisciplina.* from gradedisciplina ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" where periodoletivo.gradecurricular = ").append(gradeCurricular);
		if (disciplina != null && !disciplina.equals(0)) {
			sql.append(" and (gradedisciplina.disciplina = ").append(disciplina).append(")");
			// append(" or gradedisciplina.disciplina in(select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append("))");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
			gradeDisciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
			gradeDisciplinaVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			gradeDisciplinaVO.getPeriodoLetivoVO().setCodigo(tabelaResultado.getInt("periodoletivo"));
			gradeDisciplinaVO.setDisciplinaTCC(tabelaResultado.getBoolean("disciplinatcc"));
			gradeDisciplinaVO.getDisciplinaEixoTematico().setCodigo(tabelaResultado.getInt("disciplinaeixotematico"));
			gradeDisciplinaVO.setCargaHoraria(tabelaResultado.getInt("cargahoraria"));
			gradeDisciplinaVO.setCargaHorariaPratica(tabelaResultado.getInt("cargahorariapratica"));
			gradeDisciplinaVO.setNrCreditos(tabelaResultado.getInt("nrcreditos"));
			gradeDisciplinaVO.setNrCreditoFinanceiro(tabelaResultado.getDouble("nrcreditofinanceiro"));
			gradeDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidadeDisciplina")));
			gradeDisciplinaVO.setTipoDisciplina(tabelaResultado.getString("tipodisciplina"));
			gradeDisciplinaVO.getConfiguracaoAcademico().setCodigo(tabelaResultado.getInt("configuracaoacademico"));
			gradeDisciplinaVO.setDiversificada(tabelaResultado.getBoolean("diversificada"));
			return gradeDisciplinaVO;
		}
		return new GradeDisciplinaVO();
	}

	public List<GradeDisciplinaVO> consultarDisciplinaMinistrouHorarioProfessorPorCodigo(Integer pessoa, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct ");
		sqlStr.append(" ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, ");
		sqlStr.append(" periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* ");
		sqlStr.append(" from disciplina ");
		sqlStr.append(" inner join horarioTurmaProfessorDisciplina htpd on htpd.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append(" inner join horarioTurma on horarioturma.codigo = htpd.horarioTurma ");
		sqlStr.append(" inner join horarioTurmadia on horarioturma.codigo = horarioturmadia.horarioTurma  ");
		sqlStr.append(" inner join turma on turma.codigo = horarioTurma.turma ");
		sqlStr.append(" inner join gradedisciplina  on gradedisciplina.periodoletivo = turma.periodoletivo and gradedisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join periodoletivo 	on gradedisciplina.periodoletivo = periodoletivo.codigo  ");
		sqlStr.append(" where pessoa.codigo = ");
		sqlStr.append(pessoa.intValue());
		sqlStr.append(" and ((horarioTurma.anoVigente = '");
		sqlStr.append(Uteis.getAnoDataAtual4Digitos());
		sqlStr.append("' and horarioturma.semestreVigente = '");
		sqlStr.append(Uteis.getSemestreAtual());
		sqlStr.append("') or (horarioTurma.anoVigente = '' and horarioTurma.semestreVigente = '')) ");
		sqlStr.append(" and horarioturmadia.data <= '");
		sqlStr.append(Uteis.getDataJDBCTimestamp(new Date()));
		sqlStr.append("'");
		sqlStr.append(" order by gradedisciplina.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public Integer consultarCargaHorariaDisciplinaPorDisciplinaETurma(Integer disciplina, String matricula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct gradedisciplina.cargahoraria from gradedisciplina ");
		sb.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = periodoletivo.gradecurricular ");
		sb.append(" where gradedisciplina.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradedisciplinacomposta.cargahoraria from gradedisciplina ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = gradedisciplina.periodoletivo ");
		sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo ");
		sb.append(" where gradedisciplinacomposta.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradedisciplinacomposta.cargahoraria from gradecurriculargrupooptativadisciplina ");
		sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = gradecurriculargrupooptativa.gradecurricular ");		
		sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sb.append(" where gradedisciplinacomposta.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradecurriculargrupooptativadisciplina.cargahoraria from gradecurriculargrupooptativadisciplina ");
		sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = gradecurriculargrupooptativa.gradecurricular ");				
		sb.append(" where gradecurriculargrupooptativadisciplina.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' limit 1 ");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("cargahoraria");
			}
			return 0;
		} finally {
			sb = null;
		}
	}

	public List<GradeDisciplinaVO> consultarDisciplinasObrigatoriasNaoCumpridasDaGrade(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		if (Uteis.isAtributoPreenchido(gradeCurricular) && Uteis.isAtributoPreenchido(matricula)) {
			StringBuilder sqlStr = new StringBuilder(" "); 
			sqlStr.append("	select ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito,  ");
			sqlStr.append(" periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* ");
			sqlStr.append(" from gradedisciplina ");
			sqlStr.append("	inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo");
			sqlStr.append("	where gradecurricular = ? and gradedisciplina.tipodisciplina in ('OB', 'LG') and gradedisciplina.codigo not in (");
			sqlStr.append("		select gradedisciplina from historico  ");
			sqlStr.append("		inner join gradedisciplina gd on gd.codigo = historico.gradedisciplina");
			sqlStr.append("		where matricula  = ? and matrizcurricular = ? and gradedisciplina is not null");
			sqlStr.append("		and gd.tipodisciplina in ('OB', 'LG')");
			List<SituacaoHistorico> situacoesDeAprovacao = SituacaoHistorico.getSituacoesDeAprovacao();
			if (Uteis.isAtributoPreenchido(situacoesDeAprovacao)) {
				sqlStr.append(situacoesDeAprovacao.stream().map(SituacaoHistorico::getValor).collect(joining("', '", " and situacao in ('", "') ")));
			}
			sqlStr.append("	)");	
			if(Uteis.isAtributoPreenchido(limite)){
				sqlStr.append("	limit ").append(limite);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { gradeCurricular, matricula, gradeCurricular });
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} else {
			throw new ConsistirException("É necessário informar a Matricula e a Grade Curricular do aluno para listar as Disciplinas Pendentes.");
		}
	}

	@Override
	public void adicionarGradeDisciplinaCompostaVOs(GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular, GradeDisciplinaVO gradeTipo) throws Exception {
		getFacadeFactory().getGradeDisciplinaCompostaFacade().validarDados(gradeDisciplinaCompostaVO, situacaoGradeCurricular, gradeDisciplinaVO.getFormulaCalculoNota());
		Integer cargaHorariaTotal = 0;
		Integer creditos = 0;
		int x = 0;
		boolean disciplinaAdd = false;
		for (GradeDisciplinaCompostaVO objExistente : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			if (objExistente.getDisciplina().getCodigo().intValue() == gradeDisciplinaCompostaVO.getDisciplina().getCodigo()) {
				gradeDisciplinaVO.getGradeDisciplinaCompostaVOs().set(x, gradeDisciplinaCompostaVO);
				disciplinaAdd = true;
			}
			x++;
			cargaHorariaTotal += objExistente.getCargaHoraria();
			creditos += objExistente.getNrCreditos();
		}
		if (!disciplinaAdd) {
			cargaHorariaTotal += gradeDisciplinaCompostaVO.getCargaHoraria();
			creditos += gradeDisciplinaCompostaVO.getNrCreditos();
		}
		if (gradeTipo.getTipoControleComposicao().equals(TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS)) {
			if (gradeDisciplinaVO.getCargaHoraria() < cargaHorariaTotal) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_cargaHorariaMaiorCargaHorariaDisciplinaPrincipal"));
			}
			if (gradeDisciplinaVO.getNrCreditos() < creditos) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_creditosMaiorCreditosDisciplinaPrincipal"));
			}
		}
		if (!disciplinaAdd) {
			gradeDisciplinaCompostaVO.setOrdem(gradeDisciplinaVO.getGradeDisciplinaCompostaVOs().size());
			gradeDisciplinaVO.getGradeDisciplinaCompostaVOs().add(gradeDisciplinaCompostaVO);
		}
	}

	@Override
	public void removerGradeDisciplinaCompostaVOs(GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) throws Exception {
		int x = 0;

		for (GradeDisciplinaCompostaVO objExistente : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			if (objExistente.getDisciplina().getCodigo().intValue() == gradeDisciplinaCompostaVO.getDisciplina().getCodigo()) {
				gradeDisciplinaVO.getGradeDisciplinaCompostaVOs().remove(x);
				break;
			}
			x++;
		}
		x = 0;
		for (GradeDisciplinaCompostaVO objExistente : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			objExistente.setOrdem(x);
			x++;
		}

	}

	public Integer consultarCargaHorariaPorTurmaDisciplina(Integer turma, Integer disciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select gradedisciplina.cargahoraria ");
		sb.append(" from gradedisciplina ");
		sb.append(" inner join turmadisciplina on turmadisciplina.gradedisciplina = gradedisciplina.codigo ");
		sb.append(" where turma = ").append(turma);
		sb.append(" and turmadisciplina.disciplina = ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("cargahoraria");
		}
		return 0;
	}

	public GradeDisciplinaVO consultarGradeDisciplinaPorChavePrimariaDadosCargaHorariaNrCreditos(Integer codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("select gradedisciplina.codigo, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, gradedisciplina.disciplina, disciplina.nome as \"disciplina.nome\" from gradedisciplina inner join disciplina on disciplina.codigo = gradedisciplina.disciplina  where gradedisciplina.codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		GradeDisciplinaVO obj = null;
		if (tabelaResultado.next()) {
			obj = new GradeDisciplinaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setCargaHoraria(tabelaResultado.getInt("cargaHoraria"));
			obj.setNrCreditos(tabelaResultado.getInt("nrCreditos"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			obj.getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
			return obj;
		}
		return obj;
	}

	public Integer consultarCargaHorariaPorChavePrimaria(Integer codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("select cargahoraria from gradedisciplina where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("cargahoraria");
		}
		return 0;
	}

    
    public StringBuilder getSqlConsultaRapida(){
    	StringBuilder sqlConsultaRapida = new StringBuilder();
    	sqlConsultaRapida.append(" select ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.*, ");
    	sqlConsultaRapida.append(" disciplina.nome as \"disciplina.nome\"  ");
    	sqlConsultaRapida.append(" from gradedisciplina  ");
    	sqlConsultaRapida.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo ");
    	sqlConsultaRapida.append(" inner join disciplina 	on disciplina.codigo =  gradedisciplina.disciplina  ");    	
    	return sqlConsultaRapida;
    }
    
    @Override
    public List<GradeDisciplinaVO> consultaRapidaGradeDisciplinaPorPeriodoLetivo(Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception{
    	StringBuilder sql = getSqlConsultaRapida();
    	sql.append(" where periodoletivo.codigo = ? order by disciplina.nome ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), periodoLetivo);
    	return montarDadosConsultaRapida(rs, usuario);
    }
    
    public List<GradeDisciplinaVO> montarDadosConsultaRapida(SqlRowSet rs, UsuarioVO usuario) throws Exception{
    	List<GradeDisciplinaVO> vetResultado = new ArrayList<GradeDisciplinaVO>(0);
		while (rs.next()) {
			GradeDisciplinaVO obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer consultarCargaHorariaPorGradeCurricularPeriodoLetivoDeDisciplinaQueNaoEstaNoHistorico(String matricula, Integer gradeCurricular, Integer periodoLetivo, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select sum(cargahoraria) AS cargahoraria from (");
    	sb.append(" select distinct gradedisciplina.codigo, gradedisciplina.cargahoraria from periodoletivo ");
    	sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
    	sb.append(" where periodoletivo.gradecurricular = ").append(gradeCurricular);
    	if (!periodoLetivo.equals(0)) {
    		sb.append(" and periodoletivo.periodoletivo = ").append(periodoLetivo);
    	}
    	sb.append(" and gradedisciplina.tipodisciplina = 'OB' ");
    	sb.append(" and gradedisciplina.disciplina not in(");
    	sb.append(" select historico.disciplina from historico where matricula = '").append(matricula).append("' ");
    	sb.append(" and historico.matrizcurricular = ").append(gradeCurricular);
    	sb.append(" ) ");
    	sb.append(" ) as t ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (tabelaResultado.next()) {
    		return tabelaResultado.getInt("cargahoraria");
    	}
    	return 0;
    }
    
    @Override
	public void validarDadosFormulaCalculoComposicao(GradeDisciplinaVO obj) throws ConsistirException {
		if (obj.getDisciplinaComposta() && (obj.getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO) || obj.getControlarRecuperacaoPelaDisciplinaPrincipal())) {
			Map<String, GradeDisciplinaCompostaVO> variaveis = new HashMap<String, GradeDisciplinaCompostaVO>();
			if (obj.getFormulaCalculo().trim().isEmpty() && obj.getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO)) {
				throw new ConsistirException("O campo FÓRMULA CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " deve ser informado.");
			}
			obj.setVariavelNotaFormulaCalculoNotaRecuperada(obj.getVariavelNotaFormulaCalculoNotaRecuperada().trim());
			obj.setVariavelNotaCondicaoUsoRecuperacao(obj.getVariavelNotaCondicaoUsoRecuperacao().trim());
			obj.setVariavelNotaRecuperacao(obj.getVariavelNotaRecuperacao().trim());
			obj.setFormulaCalculo(obj.getFormulaCalculo().replace(",", "."));
			obj.setCondicaoUsoRecuperacao(obj.getCondicaoUsoRecuperacao().replace(",", "."));
			obj.setFormulaCalculoNotaRecuperada(obj.getFormulaCalculoNotaRecuperada().replace(",", "."));
			obj.setFormulaCalculoNotaRecuperacao(obj.getFormulaCalculoNotaRecuperacao().replace(",", "."));
			String formula = obj.getFormulaCalculo();
			String formulaRec = obj.getFormulaCalculoNotaRecuperada();
			String condicaoRec = obj.getCondicaoUsoRecuperacao();
			String formulaCalculoRec = obj.getFormulaCalculoNotaRecuperacao();
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : obj.getGradeDisciplinaCompostaVOs()) {
				if (gradeDisciplinaCompostaVO.getVariavelNota().trim().isEmpty()) {
					throw new ConsistirException("O campo VARIAVEL NOTA da disciplina " + gradeDisciplinaCompostaVO.getDisciplina().getNome() + " que faz parte da composição da disciplina " + obj.getDisciplina().getNome() + " deve ser informado.");
				}
				for (String key : variaveis.keySet()) {
					if (key.contains(gradeDisciplinaCompostaVO.getVariavelNota().trim())) {
						throw new ConsistirException("O campo VARIAVEL NOTA da disciplina " + gradeDisciplinaCompostaVO.getDisciplina().getNome() + " que faz parte da composição da disciplina " + obj.getDisciplina().getNome() + " possui a mesma sequencia de caracteres da disciplina " + variaveis.get(key).getDisciplina().getNome() + " inviabilizando o uso desta variável.");
					}
				}
				gradeDisciplinaCompostaVO.setVariavelNota(gradeDisciplinaCompostaVO.getVariavelNota().trim());
				variaveis.put(gradeDisciplinaCompostaVO.getVariavelNota().trim(), gradeDisciplinaCompostaVO);
				formula = formula.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
				formulaRec = formulaRec.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
				condicaoRec = condicaoRec.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
				formulaCalculoRec = formulaCalculoRec.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
			}

			if(obj.getControlarRecuperacaoPelaDisciplinaPrincipal()){
				Boolean resultado = Uteis.realizarFormulaCondicaoUso(condicaoRec);
				if (resultado == null) {
					throw new ConsistirException("A CONDIÇÃO DE USO NOTA RECUPERAÇÃO da disciplina " + obj.getDisciplina().getNome() + " informado está incorreta.");
				}
				resultado = Uteis.realizarFormulaCondicaoUso(formulaRec);
				if (resultado == null) {
					throw new ConsistirException("A CONDIÇÃO DE CÁLCULO NOTA RECUPERADA da disciplina " + obj.getDisciplina().getNome() + " informado está incorreta.");
				}
				if(!formulaCalculoRec.trim().isEmpty()){
					Double nota = Uteis.realizarCalculoFormula(formulaCalculoRec);
					if (nota == null) {
						throw new ConsistirException("A FORMULA DE CÁLCULO NOTA RECUPERAÇÃO da disciplina " + obj.getDisciplina().getNome() + " informado está incorreta.");
					}
				}
			}
			if(obj.getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO)){
			try {			
				Double resultado = Uteis.realizarCalculoFormulaCalculo(formula);
				if (resultado == null) {
					throw new ConsistirException("A FORMULA DE CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " informado está incorreta.");
				}
			} catch (ScriptException e) {
				throw new ConsistirException("A FORMULA DE CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " informado está incorreta.");
			} catch (Exception ex) {
				throw new ConsistirException("A FORMULA DE CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " informado está incorreta.");
			}
			}
		}
	}
    
    @Override
	public List<GradeDisciplinaVO> consultarPorTurmaDisciplinaCompostaEstudarQuantidadeComposta(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select  ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* from gradedisciplina ");
		sqlStr.append("  inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo  ");
		sqlStr.append("inner join turmadisciplina on turmadisciplina.gradedisciplina = gradedisciplina.codigo ");
		sqlStr.append("where turmadisciplina.turma = ").append(turma);
		sqlStr.append(" and disciplinaComposta ");
		sqlStr.append(" and tipoControleComposicao = 'ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}
    
	public List<GradeDisciplinaVO> consultarPorGradeDisciplinaCompostaPorGrade(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" "); 
		sqlStr.append("	select ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* from gradedisciplina");
		sqlStr.append("	inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo");
		sqlStr.append("	where (gradecurricular = ?) and (gradedisciplina.disciplinacomposta = true)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { gradeCurricular });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
    @Override
	public Integer consultarHoraAulaDisciplinaPorDisciplinaEMatricula(Integer disciplina, String matricula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct gradedisciplina.horaAula from gradedisciplina ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = gradedisciplina.periodoletivo ");
		sb.append(" where gradedisciplina.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradedisciplinacomposta.horaAula from gradedisciplina ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = gradedisciplina.periodoletivo ");
		sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo ");
		sb.append(" where gradedisciplinacomposta.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradedisciplinacomposta.horaAula from gradecurriculargrupooptativadisciplina ");
		sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = gradecurriculargrupooptativa.gradecurricular ");		
		sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sb.append(" where gradedisciplinacomposta.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradecurriculargrupooptativadisciplina.horaAula from gradecurriculargrupooptativadisciplina ");
		sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.gradecurricular = gradecurriculargrupooptativa.gradecurricular ");				
		sb.append(" where gradecurriculargrupooptativadisciplina.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.matricula = '").append(matricula).append("' limit 1 ");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("horaAula");
			}
			return 0;
		} finally {
			sb = null;
		}
	}	

    public GradeDisciplinaVO consultarPorChavePrimariaSemExcecao(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select  ( SELECT COUNT(*) FROM disciplinaprerequisito dr WHERE dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, ");
		sql.append(" periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* ");
		sql.append(" FROM gradedisciplina INNER JOIN periodoletivo ON gradedisciplina.periodoletivo = periodoletivo.codigo  ");
		sql.append(" WHERE gradedisciplina.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		return !tabelaResultado.next() ? null : montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
    
    @Override
    public List<LogImpactoMatrizCurricularVO> validarDadosImpactoMatriculaFormadaInclusaoGradeDisciplinaMatrizCurricular(GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaIncluirVO, StringBuilder msgAvisoAlteracaoGrade, UsuarioVO usuarioVO) {
    	List<MatriculaVO> listaMatriculaVOs = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorSituacaoMatrizCurricular(SituacaoVinculoMatricula.FORMADO.getValor(), gradeCurricularVO.getCodigo(), usuarioVO);
    	if (listaMatriculaVOs.isEmpty()) {
    		return new ArrayList<LogImpactoMatrizCurricularVO>(0);
    	}
		msgAvisoAlteracaoGrade.append("Existem ").append(listaMatriculaVOs.size()).append(" alunos que estão com a situação FORMADO, com esta operação o histórico deste alunos não estarão mais INTEGRALIZADOS <br/>");
		
		List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs = new ArrayList<LogImpactoMatrizCurricularVO>(0);
		
		LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
		logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.INCLUSAO_GRADE_DISCIPLINA);
		logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
		logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo com a inclusão na Matriz Curricular mesmo possuindo Aluno Formado.");
		logImpactoGradeDisciplinaVO.setListaMatriculaVOs(listaMatriculaVOs);
		listaLogImpactoGradeDisciplinaVOs.add(logImpactoGradeDisciplinaVO);
		gradeDisciplinaIncluirVO.setListaLogImpactoGradeDisciplinaVOs(listaLogImpactoGradeDisciplinaVOs);
		return listaLogImpactoGradeDisciplinaVOs;
    }
    
    @Override
    public void validarDadosImpactoAlteracaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaEditadaVO, StringBuilder msgAvisoAlteracaoGrade, UsuarioVO usuarioVO) throws Exception {
    	if (Uteis.isAtributoPreenchido(gradeDisciplinaEditadaVO.getCodigo())) {
    		GradeDisciplinaVO gradeDisciplinaOriginalVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaEditadaVO.getCodigo(), usuarioVO);
        	String alteracao = gradeDisciplinaEditadaVO.validarAlteracaoMatrizCurricularAtivaInativa(gradeDisciplinaOriginalVO);
        	if (!alteracao.equals("")) {
        		
        		gradeDisciplinaEditadaVO.setListaLogImpactoGradeDisciplinaVOs(new ArrayList<LogImpactoMatrizCurricularVO>(0));
        		
        		List<HistoricoVO> listaHistoricoVOs = getFacadeFactory().getHistoricoFacade().consultarPorGradeDisciplinaVinculoHistoricoAlteracaoMatrizCurricular(gradeDisciplinaEditadaVO, usuarioVO);

         		if (!listaHistoricoVOs.isEmpty()) {
         			
         			msgAvisoAlteracaoGrade.append("Existem ").append(listaHistoricoVOs.size()).append(" registro(s) em historico que estão vinculado(s) à esta disciplina. Alterações realizadas serão atualizadas nos históricos deste(s) aluno(s). ");
     				msgAvisoAlteracaoGrade.append("<br/><br/>").append(getFacadeFactory().getHistoricoFacade().consultarMensagemImpactoHistoricoPorGradeDisciplina(gradeDisciplinaOriginalVO, usuarioVO));
         			
         			if(!gradeDisciplinaOriginalVO.getDisciplinaComposta() && gradeDisciplinaEditadaVO.getDisciplinaComposta()) {
     					msgAvisoAlteracaoGrade.append("<br/><br/> O sistema irá incluir para esses alunos, um novo registro em historico com a situacão cursando para cada disciplina filha adicionada.");	
     				}

         			LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
     				logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.ALTERACAO_HISTORICO);
     				gradeDisciplinaEditadaVO.setListaHistoricoImpactoAlteracaoVOs(listaHistoricoVOs);
     				logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
     				logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo com as alterações no histórico.");
     				logImpactoGradeDisciplinaVO.setListaHistoricoImpactoAlteracaoVOs(listaHistoricoVOs);
     				gradeDisciplinaEditadaVO.getListaLogImpactoGradeDisciplinaVOs().add(logImpactoGradeDisciplinaVO);
        		} 
        	}
    	}
    	
    }
    
	@Override
	public void validarDadosImpactoExclusaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaExcluirVO, UsuarioVO usuarioVO) throws Exception {
		gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().clear();
//		VERIFICA SE POSSUI HISTÓRICOS VINCULADOS
		List<HistoricoVO> listaHistoricoVOs = getFacadeFactory().getHistoricoFacade().consultarPorGradeDisciplinaVinculoHistoricoAlteracaoMatrizCurricular(gradeDisciplinaExcluirVO, usuarioVO);
		if (!listaHistoricoVOs.isEmpty()) {
			StringBuilder msgAvisoAlteracaoGrade = new StringBuilder();
			gradeDisciplinaExcluirVO.setSofreuAlteracaoMatrizAtivaInativa(true);
			gradeDisciplinaExcluirVO.setListaHistoricoImpactoAlteracaoVOs(listaHistoricoVOs);

			msgAvisoAlteracaoGrade.append("Existem ").append(listaHistoricoVOs.size()).append(" registros em historico vinculados à essa disciplina. Caso seja removida, o sistema poderá excluir o histórico ou deixa o registro como uma disciplina fora da grade.");
			msgAvisoAlteracaoGrade.append("<br/>").append(getFacadeFactory().getHistoricoFacade().consultarMensagemImpactoHistoricoPorGradeDisciplina(gradeDisciplinaExcluirVO, usuarioVO));
			msgAvisoAlteracaoGrade.append("<br/><br/> Obs: Se optar pela exclusão dos históricos os dados não poderão mais ser recuperados.");
			msgAvisoAlteracaoGrade.append("<br/><br/> Selecione o que deseja que seja feito com o Histórico:");
			
			LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
			logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.EXCLUSAO_HISTORICO);
			logImpactoGradeDisciplinaVO.setLogReferenteExclusaoHistorico(true);
			logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
			logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo com as alterações e/ou exclusões dos históricos.");
			logImpactoGradeDisciplinaVO.setListaHistoricoImpactoAlteracaoVOs(listaHistoricoVOs);
			gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().add(logImpactoGradeDisciplinaVO);

		}
//		VERIFICA SE POSSUI TURMAS VINCULDAS
   		List<TurmaVO> listaTurmaVOs = getFacadeFactory().getTurmaFacade().consultarTurmaPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
   		if (!listaTurmaVOs.isEmpty()) {
   			StringBuilder msgAvisoAlteracaoGrade = new StringBuilder();
   			msgAvisoAlteracaoGrade.append("Existem ").append(listaTurmaVOs.size()).append(" registros em Turmas vinculados à essa disciplina. Caso seja removida será automaticamente excluída a Disciplina da Turma e não será mais possível desfazer a operação.");
   			
   			LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
			logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.EXCLUSAO_DISCIPLINA_TURMA);
			logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
			logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo com a exclusão da disciplina na Turma.");
			logImpactoGradeDisciplinaVO.setListaTurmaVOs(listaTurmaVOs);
			gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().add(logImpactoGradeDisciplinaVO);
   		}
//   	VERIFICA PROGRAMAÇÃO DE AULA
   		Integer qtdeAulaProgramadaDisciplina = getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarQtdeAulaPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
   		if (qtdeAulaProgramadaDisciplina > 0) {
   			StringBuilder msgAvisoAlteracaoGrade = new StringBuilder();
   			msgAvisoAlteracaoGrade.append("Existem ").append(qtdeAulaProgramadaDisciplina).append(" aulas programadas vinculados a essa disciplina. Caso seja removida esses registros ficaram orfãos.");
   			
   			LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
			logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.PROGRAMACAO_AULA_REGISTRO_ORFAO);
			logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
			logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo em deixar os registros orfãos na Programação de Aula.");
			logImpactoGradeDisciplinaVO.setQtdeAulaProgramadaDisciplina(qtdeAulaProgramadaDisciplina);
			gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().add(logImpactoGradeDisciplinaVO);
   		}
//   	VERIFICA REGISTRO DE AULA
   		Integer qtdeRegistroAulaDisciplina = getFacadeFactory().getRegistroAulaFacade().consultarQtdeRegistroAulaPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
   		if (qtdeRegistroAulaDisciplina > 0) {
   			StringBuilder msgAvisoAlteracaoGrade = new StringBuilder();
   			msgAvisoAlteracaoGrade.append("Existem ").append(qtdeRegistroAulaDisciplina).append(" aulas registradas vinculados a essa disciplina. Caso seja removida esses registros ficaram orfãos.");
   			
   			LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
			logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.REGISTRO_AUAL_REGISTRO_ORFAO);
			logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
			logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo em deixar os registros orfãos em Registros de Aula.");
			logImpactoGradeDisciplinaVO.setQtdeRegistroAulaDisciplina(qtdeRegistroAulaDisciplina);
			gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().add(logImpactoGradeDisciplinaVO);
   		}
   		
//   		Integer qtdeVagaTurmaDisciplina = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultarQtdeVagaDisciplinaPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
//   		if (qtdeVagaTurmaDisciplina > 0 ) {
//   			StringBuilder msgAvisoAlteracaoGrade = new StringBuilder();
//   			msgAvisoAlteracaoGrade.append("Existem ").append(qtdeVagaTurmaDisciplina).append(" controle de vagas registradas vinculados a essa disciplina. Caso seja removida esses registros ficaram orfãos.");
//   			
//   			LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO = new LogImpactoMatrizCurricularVO();
//			logImpactoGradeDisciplinaVO.setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum.CONTROLE_VAGAS_DISCIPLINA_REGISTRO_ORFAO);
//			logImpactoGradeDisciplinaVO.setMsgAvisoAlteracaoGrade(msgAvisoAlteracaoGrade);
//			logImpactoGradeDisciplinaVO.setMensagemLiEConcordoComOsTermos("Li e concordo em deixar os registros orfãos em Registros de Aula.");
//			logImpactoGradeDisciplinaVO.setQtdeVagaTurmaDisciplina(qtdeVagaTurmaDisciplina);
//			gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().add(logImpactoGradeDisciplinaVO);
//   		}
	}
	
	@Override
	public void validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina(GradeDisciplinaVO gradeDisciplinaVO) throws Exception {
		for (LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO : gradeDisciplinaVO.getListaLogImpactoGradeDisciplinaVOs()) {
			if (!logImpactoGradeDisciplinaVO.getLiEConcordoComOsTermos()) {
				throw new Exception("Para prosseguir com a operação é necessário marcar a opção que concorda com as alterações "+logImpactoGradeDisciplinaVO.getTituloImpactoMatrizCurricularEnum().getTitulo().toUpperCase()+"");
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirImpactoExclusaoGradeDisciplina(GradeDisciplinaVO gradeDisciplinaExcluirVO, UsuarioVO usuarioVO) throws Exception {
//		validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina(gradeDisciplinaExcluirVO);
		for (LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO : gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs()) {
			
			if (logImpactoGradeDisciplinaVO.getLogReferenteExclusaoHistorico()) {
				if (logImpactoGradeDisciplinaVO.getTipoExclusaoHistorico().equals("FORA_GRADE")) {
					getFacadeFactory().getHistoricoFacade().alterarHistoricoParaForaDaGradePorGradeDisciplina(gradeDisciplinaExcluirVO, usuarioVO);
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarParaForaDaGradePorGradeDisciplina(gradeDisciplinaExcluirVO, usuarioVO);
				} else {
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluirPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
					getFacadeFactory().getHistoricoFacade().excluirPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
				}
			}
			
			if (!logImpactoGradeDisciplinaVO.getListaTurmaVOs().isEmpty()) {
				getFacadeFactory().getTurmaDisciplinaFacade().excluirPorGradeDisciplina(gradeDisciplinaExcluirVO.getCodigo(), usuarioVO);
			}
		}
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCorrecaoImpactosAlteracaoGradeDisciplina(GradeDisciplinaVO gradeDisciplinaEdicaoVO, List<HistoricoVO> historicoVOs, TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricularEnum, UsuarioVO usuarioVO, GradeCurricularVO gradeCurricularVO) throws Exception {

		if (tipoAlteracaoMatrizCurricularEnum.equals(TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA) && !historicoVOs.isEmpty()) {
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorGradeDisciplinaAlteracaoMatrizCurricularAtivaInativa(gradeDisciplinaEdicaoVO, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarPorGradeDisciplinaAlteracaoMatrizAtivaInativa(gradeDisciplinaEdicaoVO);
		} 
	}
	
	public List<GradeDisciplinaVO> consultarPorGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" "); 
		sqlStr.append("	select ( select count(*) from disciplinaprerequisito dr where dr.gradedisciplina = gradedisciplina.codigo) as totalprerequisito, periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,gradedisciplina.* from gradedisciplina");
		sqlStr.append("	inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo");
		sqlStr.append("	where gradecurricular = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { gradeCurricular });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	

	@Override
	public List<GradeDisciplinaVO> consultarGradeDisciplinaVOTCCsNaoAprovadoAluno(String matricula,  int gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT ( SELECT COUNT(*) FROM disciplinaprerequisito dr WHERE dr.gradedisciplina = gradedisciplina.codigo) AS totalprerequisito,  ");
		sql.append(" periodoletivo.codigo as codigoperiodoletivo,periodoletivo.descricao as descricaoperiodoletivo,periodoletivo.periodoletivo as periodoletivoperiodoletivo,GradeDisciplina.* ");
		sql.append(" FROM GradeDisciplina");
		sql.append(" inner join periodoletivo on GradeDisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append(" inner join disciplina on disciplina.codigo = GradeDisciplina.disciplina ");
		sql.append(" WHERE periodoletivo.gradecurricular = ? ");
		sql.append(" and (gradedisciplina.disciplinatcc = true or disciplina.classificacaoDisciplina = 'TCC') ");
		sql.append(" and not exists (select historico.codigo from historico where historico.matricula = ? and historico.matrizcurricular = periodoletivo.gradecurricular and historico.gradedisciplina = GradeDisciplina.codigo and historico.situacao in ('AA', 'AP', 'AE', 'CC', 'CH', 'IS', 'AB') ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { gradeCurricular, matricula });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
}
