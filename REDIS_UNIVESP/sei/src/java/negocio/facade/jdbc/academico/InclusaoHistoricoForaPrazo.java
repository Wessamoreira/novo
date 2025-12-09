package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.InclusaoDisciplinasHistoricoForaPrazoVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.InclusaoHistoricoForaPrazoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaPeriodoTurmaDisciplinaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class InclusaoHistoricoForaPrazo extends ControleAcesso implements InclusaoHistoricoForaPrazoInterfaceFacade {

	protected static String idEntidade;

	public InclusaoHistoricoForaPrazo() throws Exception {
		super();
		setIdEntidade("InclusaoHistoricoForaPrazo");
	}

	public static String getIdEntidade() {
		return InclusaoDisciplinasHistoricoForaPrazo.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		InclusaoDisciplinasHistoricoForaPrazo.idEntidade = idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #incluir(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InclusaoHistoricoForaPrazoVO obj, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO) throws Exception {
		preencherInclusaoDisciplinasHistoricoForaPrazoVO(obj, listaMatriculaPeriodoVO);
		incluir(obj);
	}

	public void preencherInclusaoDisciplinasHistoricoForaPrazoVO(final InclusaoHistoricoForaPrazoVO obj, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO) throws Exception {
		for (MatriculaPeriodoVO matriculaPeriodoVO : listaMatriculaPeriodoVO) {
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs()) {
				InclusaoDisciplinasHistoricoForaPrazoVO inclusaoDisciplinasHistoricoForaPrazoVO = new InclusaoDisciplinasHistoricoForaPrazoVO();
				inclusaoDisciplinasHistoricoForaPrazoVO.setTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma());
				inclusaoDisciplinasHistoricoForaPrazoVO.setDisciplina(matriculaPeriodoTurmaDisciplinaVO.getDisciplina());
				inclusaoDisciplinasHistoricoForaPrazoVO.setSemestre(matriculaPeriodoTurmaDisciplinaVO.getSemestre());
				inclusaoDisciplinasHistoricoForaPrazoVO.setAno(matriculaPeriodoTurmaDisciplinaVO.getAno());
				obj.getListaInclusaoDisciplinasHistoricoForaPrazoVO().add(inclusaoDisciplinasHistoricoForaPrazoVO);
			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InclusaoHistoricoForaPrazoVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO inclusaohistoricoforaprazo( matriculaPeriodo, reposicao, textoPadraoContrato, justificativa, observacao, planoFinanceiroReposicao, nrParcelas, valorTotalParcela, desconto, dataVencimento, responsavel, dataInclusao, requerimento ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getMatriculaPeriodoVO().getCodigo().intValue());
					sqlInserir.setBoolean(2, obj.getReposicao());
					if (obj.getTextoPadraoContrato().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getTextoPadraoContrato().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getJustificativa());
					sqlInserir.setString(5, obj.getObservacao());
					if (obj.getPlanoFinanceiroReposicaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getPlanoFinanceiroReposicaoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setInt(7, obj.getNrParcelas().intValue());
					sqlInserir.setDouble(8, obj.getValorTotalParcela().doubleValue());
					sqlInserir.setDouble(9, obj.getDesconto().doubleValue());
					sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setInt(11, obj.getResponsavel().getCodigo().intValue());
					sqlInserir.setDate(12, Uteis.getDataJDBC(obj.getDataInclusao()));
					if (obj.getRequerimentoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getRequerimentoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().incluirListaInclusaoDisciplinasHistoricoForaPrazoVO(obj);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #alterar(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InclusaoHistoricoForaPrazoVO obj) throws Exception {
		final String sql = "UPDATE inclusaohistoricoforaprazo( matriculaPeriodo=?, reposicao=?, textoPadraoContrato=?, justificativa=?, observacao=?, planoFinanceiroReposicao=?, nrParcelas=?, valorTotalParcela=?, desconto=?, dataVencimento=?, responsavel=?, dataInclusao=?, requerimento=? WHERE (codigo = ?)";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getMatriculaPeriodoVO().getCodigo().intValue());
				sqlAlterar.setBoolean(2, obj.getReposicao());
				if (obj.getTextoPadraoContrato().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(3, obj.getTextoPadraoContrato().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setString(4, obj.getJustificativa());
				sqlAlterar.setString(5, obj.getObservacao());
				if (obj.getPlanoFinanceiroReposicaoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(6, obj.getPlanoFinanceiroReposicaoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(6, 0);
				}
				sqlAlterar.setInt(7, obj.getNrParcelas().intValue());
				sqlAlterar.setDouble(8, obj.getValorTotalParcela().doubleValue());
				sqlAlterar.setDouble(9, obj.getDesconto().doubleValue());
				sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataVencimento()));
				sqlAlterar.setInt(11, obj.getResponsavel().getCodigo().intValue());
				sqlAlterar.setDate(12, Uteis.getDataJDBC(obj.getDataInclusao()));
				if (obj.getRequerimentoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(13, obj.getRequerimentoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(13, 0);
				}
				sqlAlterar.setInt(14, obj.getCodigo().intValue());

				return sqlAlterar;
			}
		});
		getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().incluirListaInclusaoDisciplinasHistoricoForaPrazoVO(obj);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InclusaoHistoricoForaPrazoVO obj) throws Exception {
		getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().excluirPorInclusaoHistoricoForaPrazo(obj);
		String sql = "DELETE FROM inclusaohistoricoforaprazo WHERE (codigo = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT inclusaoHistoricoForaPrazo.codigo, inclusaoHistoricoForaPrazo.matriculaPeriodo, inclusaoHistoricoForaPrazo.reposicao, ");
		sql.append("inclusaoHistoricoForaPrazo.textoPadraoContrato, inclusaoHistoricoForaPrazo.justificativa ,inclusaoHistoricoForaPrazo.observacao, ");
		sql.append("inclusaoHistoricoForaPrazo.planoFinanceiroReposicao, ");
		sql.append("case when planoFinanceiroReposicao.codigo is null then inclusaoHistoricoForaPrazo.nrParcelas else planoFinanceiroReposicao.qtdeparcela end as nrParcelas,  ");
		sql.append("case when planoFinanceiroReposicao.codigo is null then inclusaoHistoricoForaPrazo.valorTotalParcela else planoFinanceiroReposicao.valor  end as valorTotalParcela,  ");
		sql.append("case when planoFinanceiroReposicao.codigo is null then inclusaoHistoricoForaPrazo.desconto else 0.0 end as desconto, ");
		sql.append("inclusaoHistoricoForaPrazo.dataVencimento, inclusaoHistoricoForaPrazo.responsavel, ");
		sql.append("inclusaoHistoricoForaPrazo.dataInclusao, textoPadrao.codigo AS \"textoPadrao.codigo\", textoPadrao.descricao AS \"textoPadrao.descricao\", ");
		sql.append(" requerimento.codigo AS \"requerimento.codigo\", ");
		
		sql.append("planoFinanceiroReposicao.codigo AS \"planoFinanceiroReposicao.codigo\", planoFinanceiroReposicao.descricao AS \"planoFinanceiroReposicao.descricao\", ");
		sql.append("matricula.matricula AS \"matricula.matricula\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.registroAcademico AS \"pessoa.registroAcademico\", ");
		sql.append("usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\", periodoletivo.codigo AS \"periodoletivo.codigo\", ");
		sql.append("periodoletivo.periodoletivo AS \"periodoletivo.periodoletivo\", periodoletivo.descricao AS \"periodoletivo.descricao\" ");
		sql.append("FROM inclusaoHistoricoForaPrazo ");
		sql.append("INNER JOIN matriculaPeriodo ON matriculaPeriodo.codigo = inclusaoHistoricoForaPrazo.matriculaPeriodo ");
		sql.append("INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sql.append("INNER JOIN periodoletivo ON periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sql.append("LEFT JOIN textoPadrao ON textoPadrao.codigo = inclusaoHistoricoForaPrazo.textoPadraoContrato ");
		sql.append("LEFT JOIN planoFinanceiroReposicao ON planoFinanceiroReposicao.codigo = inclusaoHistoricoForaPrazo.planoFinanceiroReposicao ");
		sql.append("LEFT JOIN usuario ON usuario.codigo = inclusaoHistoricoForaPrazo.responsavel ");
		sql.append("LEFT JOIN requerimento ON requerimento.codigo = inclusaoHistoricoForaPrazo.requerimento ");
		return sql;
	}

	private StringBuffer getSQLPadraoConsultaBasicaTotalRegistros() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(distinct inclusaoHistoricoForaPrazo.codigo) FROM inclusaoHistoricoForaPrazo ");
		sql.append("INNER JOIN matriculaPeriodo ON matriculaPeriodo.codigo = inclusaoHistoricoForaPrazo.matriculaPeriodo ");
		sql.append("INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sql.append("LEFT JOIN textoPadrao ON textoPadrao.codigo = inclusaoHistoricoForaPrazo.textoPadraoContrato ");
		sql.append("LEFT JOIN planoFinanceiroReposicao ON planoFinanceiroReposicao.codigo = inclusaoHistoricoForaPrazo.planoFinanceiroReposicao ");
		sql.append("LEFT JOIN usuario ON usuario.codigo = inclusaoHistoricoForaPrazo.responsavel ");
		return sql;
	}

	public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE upper(matricula.matricula) like '").append(matricula.toUpperCase()).append("%' ");
		sql.append("ORDER BY inclusaoHistoricoForaPrazo.dataInclusao desc ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaRapidaPorMatriculaTotalRegistros(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		try {
			StringBuffer sql = getSQLPadraoConsultaBasicaTotalRegistros();
			sql.append("WHERE upper(matricula.matricula) like '").append(matricula.toUpperCase()).append("%' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorRegistroAcademico(String registroAcademico, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE upper(pessoa.registroAcademico) like '").append(registroAcademico.toUpperCase()).append("%' ");
		sql.append("ORDER BY inclusaoHistoricoForaPrazo.dataInclusao desc ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public Integer consultaRapidaPorRegistroAcademicoTotalRegistros(String registroAcademico, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		try {
			StringBuffer sql = getSQLPadraoConsultaBasicaTotalRegistros();
			sql.append("WHERE  upper(pessoa.registroAcademico) like '").append(registroAcademico.toUpperCase()).append("%' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorAluno(String nomeAluno, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE lower(sem_acentos(pessoa.nome)) like '").append(Uteis.removerAcentuacao(nomeAluno.toLowerCase())).append("%' ");
		sql.append("ORDER BY pessoa.nome");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaRapidaPorAlunoTotalRegistros(String nomeAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		try {
			StringBuffer sql = getSQLPadraoConsultaBasicaTotalRegistros();
			sql.append("WHERE lower(sem_acentos(pessoa.nome)) like '").append(Uteis.removerAcentuacao(nomeAluno.toLowerCase())).append("%' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorResponsavel(String nomeResponsavel, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE lower(sem_acentos(usuario.nome)) like '").append(Uteis.removerAcentuacao(nomeResponsavel.toLowerCase())).append("%' ");
		sql.append("ORDER BY usuario.nome");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaRapidaPorResponsavelTotalRegistros(String nomeResponsavel, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		try {
			StringBuffer sql = getSQLPadraoConsultaBasicaTotalRegistros();
			sql.append("WHERE lower(sem_acentos(usuario.nome)) like '").append(Uteis.removerAcentuacao(nomeResponsavel.toLowerCase())).append("%' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorDataInclusao(Date dataIni, Date dataFim, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE dataInclusao >= '").append(Uteis.getDataJDBC(dataIni)).append("' ");
		sql.append("AND dataInclusao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sql.append("ORDER BY inclusaoHistoricoForaPrazo.dataInclusao ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaRapidaPorDataInclusaoTotalRegistros(Date dataIni, Date dataFim, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		try {
			StringBuffer sql = getSQLPadraoConsultaBasicaTotalRegistros();
			sql.append("WHERE dataInclusao >= '").append(Uteis.getDataJDBC(dataIni)).append("' ");
			sql.append("AND dataInclusao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<InclusaoHistoricoForaPrazoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<InclusaoHistoricoForaPrazoVO> vetResultado = new ArrayList<InclusaoHistoricoForaPrazoVO>(0);
		while (tabelaResultado.next()) {
			InclusaoHistoricoForaPrazoVO obj = new InclusaoHistoricoForaPrazoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(InclusaoHistoricoForaPrazoVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo"));
		obj.setReposicao(dadosSQL.getBoolean("reposicao"));
		obj.getTextoPadraoContrato().setCodigo(dadosSQL.getInt("textoPadraoContrato"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.getPlanoFinanceiroReposicaoVO().setCodigo(dadosSQL.getInt("planoFinanceiroReposicao"));
		obj.setNrParcelas(dadosSQL.getInt("nrParcelas"));
		obj.setValorTotalParcela(dadosSQL.getDouble("valorTotalParcela"));
		obj.setDesconto(dadosSQL.getDouble("desconto"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.setDataInclusao(dadosSQL.getDate("dataInclusao"));
		// TextoPadrao
		obj.getTextoPadraoContrato().setCodigo(dadosSQL.getInt("textoPadrao.codigo"));
		obj.getTextoPadraoContrato().setDescricao(dadosSQL.getString("textoPadrao.descricao"));
		// PlanoFinanceiroReposicao
		obj.getPlanoFinanceiroReposicaoVO().setCodigo(dadosSQL.getInt("planoFinanceiroReposicao.codigo"));
		obj.getPlanoFinanceiroReposicaoVO().setDescricao(dadosSQL.getString("planoFinanceiroReposicao.descricao"));
		// Matricula
		obj.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(dadosSQL.getString("matricula.matricula"));
		// Aluno
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setRegistroAcademico(dadosSQL.getString("pessoa.registroAcademico"));
		// PeriodoLetivo
		obj.getMatriculaPeriodoVO().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoletivo.codigo"));
		obj.getMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("periodoletivo.periodoletivo"));
		obj.getMatriculaPeriodoVO().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoletivo.descricao"));
		// Responsavel
		obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		// Requerimento
		obj.getRequerimentoVO().setCodigo(dadosSQL.getInt("requerimento.codigo"));

	}

	// public List<MatriculaPeriodoTurmaDisciplinaVO>
	// montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
	// List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new
	// ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
	// while (tabelaResultado.next()) {
	// MatriculaPeriodoTurmaDisciplinaVO obj = new
	// MatriculaPeriodoTurmaDisciplinaVO();
	// montarDadosCompleto(obj, tabelaResultado);
	// vetResultado.add(obj);
	// }
	// return vetResultado;
	// }
	//
	// private void montarDadosCompleto(MatriculaPeriodoTurmaDisciplinaVO obj,
	// SqlRowSet dadosSQL) throws Exception {
	// obj.setNivelMontarDados(NivelMontarDados.TODOS);
	// obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
	// obj.setMatriculaPeriodo(new
	// Integer(dadosSQL.getInt("matriculaPeriodo")));
	// obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
	// obj.getDisciplina().setCodigo(new
	// Integer(dadosSQL.getInt("disciplina")));
	// obj.setAno(dadosSQL.getString("ano"));
	// obj.setSemestre(dadosSQL.getString("semestre"));
	// obj.setMatricula(dadosSQL.getString("matricula"));
	// obj.setDisciplinaIncluida(dadosSQL.getBoolean("disciplinaIncluida"));
	// obj.setDisciplinaEquivale(dadosSQL.getBoolean("disciplinaEquivale"));
	// obj.setDisciplinaComposta(dadosSQL.getBoolean("disciplinaComposta"));
	// obj.getDisciplinaEquivalente().setCodigo(dadosSQL.getInt("disciplinaEquivalente"));
	// // Turma
	// obj.getTurma().setCodigo((dadosSQL.getInt("Turma.codigo")));
	// obj.getTurma().setIdentificadorTurma((dadosSQL.getString("Turma.identificadorTurma")));
	// // Disciplina
	// obj.getDisciplina().setCodigo(dadosSQL.getInt("Disciplina.codigo"));
	// obj.getDisciplina().setNome(dadosSQL.getString("Disciplina.nome"));
	// // DisciplinaEquivalente
	// obj.getDisciplinaEquivalente().setCodigo((dadosSQL.getInt("Disciplinaequivalente.codigo")));
	// obj.getDisciplinaEquivalente().setNome((dadosSQL.getString("Disciplinaequivalente.nome")));
	//
	// }
	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TurmaVO</code> relacionado ao objeto
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Faz uso da chave primária
	 * da classe <code>TurmaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 * @throws Exception
	 */
	// public static void montarDadosTurma(MatriculaPeriodoTurmaDisciplinaVO
	// obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	// if (obj.getTurma().getCodigo().intValue() == 0) {
	// return;
	// }
	// getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(),
	// usuario);
	// }

	public GradeDisciplinaVO inicializarDadosGradeDisciplinaDeAcordoInclusaoHistoricoDisciplinaEdicao(InclusaoDisciplinasHistoricoForaPrazoVO inclusaoDisciplinasHistoricoForaPrazoVO, MatriculaVO matriculaVO, HistoricoVO historico, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer cargaHoraria, Integer nrCreditos, UsuarioVO usuarioVO) throws Exception {
		PeriodoLetivoVO periodoLetivoCursandoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorAnoSemestreMatriculaPeriodoEMatricula(matriculaVO.getMatricula(), inclusaoDisciplinasHistoricoForaPrazoVO.getAno(), inclusaoDisciplinasHistoricoForaPrazoVO.getSemestre(), matriculaVO.getCurso().getPeriodicidade(), usuarioVO);
		if (historico == null && !matriculaPeriodoTurmaDisciplinaVO.getCodigo().equals(0)) {
			historico = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), false, false, usuarioVO);
		}
		GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
		gradeDisciplinaVO.setHistoricoAtualAluno(historico);
		gradeDisciplinaVO.setSelecionado(Boolean.TRUE);
		gradeDisciplinaVO.setDisciplina(inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina());
		gradeDisciplinaVO.setCargaHoraria(cargaHoraria);
		gradeDisciplinaVO.setNrCreditos(nrCreditos);
		gradeDisciplinaVO.setPeriodoLetivoVO(periodoLetivoCursandoVO);
		gradeDisciplinaVO.setPeriodoLetivo(periodoLetivoCursandoVO.getPeriodoLetivo());
		if (historico != null) {
			if (historico.getHistoricoDisciplinaComposta()) {
				if (matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getCodigo().equals(0)) {
					matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().setCodigo(historico.getGradeDisciplinaVO().getCodigo());
				}
				matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().setDisciplinaComposta(Boolean.TRUE);
			}
		}
		gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
		return gradeDisciplinaVO;
	}

	/**
	 * Realiza a montagem das disciplinas no momento de editar o obj. Autor
	 * Carlos
	 */
	public void realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception {
		gradeCurricularVO.setGradeCurricularGrupoOptativaVOs(new ArrayList<GradeCurricularGrupoOptativaVO>());
		if (inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().isEmpty()) {
			Map<Integer, GradeCurricularGrupoOptativaVO> hashGradeCurricularGrupoOptativaVO = new HashMap<Integer, GradeCurricularGrupoOptativaVO>(0);
			for (PeriodoLetivoVO periodoLetivo : gradeCurricularVO.getPeriodoLetivosVOs()) {
				for (GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
					PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivo.getCodigo());
					HistoricoVO historico = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplina.getCodigo());
					gradeDisciplina.setHistoricoAtualAluno(historico);
					gradeDisciplina.setPeriodoLetivoVO(periodoLetivo);
					gradeDisciplina.setHistoricosDuplicadosAluno(periodoLetivoComHistoricoAlunoVO.obterHistoricosDuplicadosGradeDisciplinaVO(gradeDisciplina.getCodigo()));
					if (Uteis.isAtributoPreenchido(historico.getMatriculaPeriodoTurmaDisciplina())) {
						gradeDisciplina.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(historico.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
					}
					if (historico.getHistoricoDisciplinaComposta()) {
						gradeDisciplina.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().setDisciplinaComposta(Boolean.TRUE);
					}
				}
				if (periodoLetivo.getControleOptativaGrupo()) {
					if (periodoLetivo.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
						periodoLetivo.setGradeCurricularGrupoOptativa(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(periodoLetivo.getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, usuarioVO));
					}
					if (!hashGradeCurricularGrupoOptativaVO.containsKey(periodoLetivo.getGradeCurricularGrupoOptativa().getCodigo())) {
						hashGradeCurricularGrupoOptativaVO.put(periodoLetivo.getGradeCurricularGrupoOptativa().getCodigo(), periodoLetivo.getGradeCurricularGrupoOptativa());
					}
					for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : hashGradeCurricularGrupoOptativaVO.get(periodoLetivo.getGradeCurricularGrupoOptativa().getCodigo()).getGradeCurricularGrupoOptativaDisciplinaVOs()) {
						HistoricoVO historico = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
						gradeCurricularGrupoOptativaDisciplinaVO.setHistoricoAtualAluno(historico);
						gradeCurricularGrupoOptativaDisciplinaVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivo);
						if (Uteis.isAtributoPreenchido(historico.getMatriculaPeriodoTurmaDisciplina())) {
							gradeCurricularGrupoOptativaDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(historico.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
						}
						if (historico.getHistoricoDisciplinaComposta()) {
							gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().setDisciplinaComposta(Boolean.TRUE);
						}
					}
				}
			}
			gradeCurricularVO.getGradeCurricularGrupoOptativaVOs().addAll(hashGradeCurricularGrupoOptativaVO.values());
		} else {
			// PARTE SERÁ USADA APENAS NA EDIÇÃO
			for (PeriodoLetivoVO periodoLetivo : gradeCurricularVO.getPeriodoLetivosVOs()) {
				// PRECISO PERCORRER TODAS AS DISCIPLINAS DA GRADE
				// PARA SETAR A SITUAÇÃO DO HISTORICO DAS MESMAS
				for (GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
					PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivo.getCodigo());
					HistoricoVO historico = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplina.getCodigo());
					gradeDisciplina.setHistoricoAtualAluno(historico);
					gradeDisciplina.setPeriodoLetivoVO(periodoLetivo);
					gradeDisciplina.setHistoricosDuplicadosAluno(periodoLetivoComHistoricoAlunoVO.obterHistoricosDuplicadosGradeDisciplinaVO(gradeDisciplina.getCodigo()));
					for (InclusaoDisciplinasHistoricoForaPrazoVO inclusaoDisciplinasHistoricoForaPrazoVO : inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO()) {
						if (inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina().getCodigo().equals(gradeDisciplina.getDisciplina().getCodigo())) {
							// MONTANDO DISCIPLINA COMPOSTA
							if (historico.getHistoricoDisciplinaComposta()) {
								MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(matriculaVO.getMatricula(), inclusaoDisciplinasHistoricoForaPrazoVO.getTurma(), inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina().getCodigo(), inclusaoDisciplinasHistoricoForaPrazoVO.getAno(), inclusaoDisciplinasHistoricoForaPrazoVO.getSemestre(), 0, false, false, usuarioVO);
								matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().setDisciplinaComposta(Boolean.TRUE);
								gradeDisciplina.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
							}
						}
					}
					if (Uteis.isAtributoPreenchido(historico.getMatriculaPeriodoTurmaDisciplina())) {
						gradeDisciplina.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(historico.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
					}
					if (historico.getHistoricoDisciplinaComposta()) {
						gradeDisciplina.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().setDisciplinaComposta(Boolean.TRUE);
					}
				}

				// MONTANDO DISCIPLINAS GRUPO OPTATIVA
				if (periodoLetivo.getControleOptativaGrupo()) {
					if (periodoLetivo.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
						periodoLetivo.setGradeCurricularGrupoOptativa(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(periodoLetivo.getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, usuarioVO));
					}
					/*
					 * Alterado por Victor Hugo de Paula 19/08/2015 09:08
					 */
					for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivo.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
						PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivo.getCodigo());
						HistoricoVO historico = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo());
						gradeCurricularGrupoOptativaDisciplinaVO.setHistoricoAtualAluno(historico);
						gradeCurricularGrupoOptativaDisciplinaVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivo);
						if (Uteis.isAtributoPreenchido(historico.getMatriculaPeriodoTurmaDisciplina())) {
							gradeCurricularGrupoOptativaDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(historico.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
						}
						if (historico.getHistoricoDisciplinaComposta()) {
							gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().setDisciplinaComposta(Boolean.TRUE);
						}
					}
					gradeCurricularVO.getGradeCurricularGrupoOptativaVOs().add(periodoLetivo.getGradeCurricularGrupoOptativa());
				}
			}
			// AQUI SERÁ MONTADA AS DISCIPLINAS QUE PORVENTURA NÃO FAZEM PARTE
			// DA GRADE
			// POR ISSO É NECESSÁRIO CRIAR UMA GRADE DISCIPLINA
			for (InclusaoDisciplinasHistoricoForaPrazoVO inclusaoDisciplinasHistoricoForaPrazoVO : inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO()) {
				// MONTANDO DISCIPLINA FORA GRADE
				if (inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplinaForaGrade()) {
					Integer cargaHoraria = 0;
					Integer nrCreditos = 0;
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(matriculaVO.getMatricula(), inclusaoDisciplinasHistoricoForaPrazoVO.getTurma(), inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina().getCodigo(), inclusaoDisciplinasHistoricoForaPrazoVO.getAno(), inclusaoDisciplinasHistoricoForaPrazoVO.getSemestre(), 0, false, false, usuarioVO);
					GradeDisciplinaVO gradeDisciplinaCargaHorariaNrCreditoVO = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinaPorChavePrimariaDadosCargaHorariaNrCreditos(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getCodigo());
					if (gradeDisciplinaCargaHorariaNrCreditoVO != null) {
						cargaHoraria = gradeDisciplinaCargaHorariaNrCreditoVO.getCargaHoraria();
						nrCreditos = gradeDisciplinaCargaHorariaNrCreditoVO.getNrCreditos();
					} else {
						cargaHoraria = matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
						nrCreditos = matriculaPeriodoTurmaDisciplinaVO.getCreditosDisciplina();
					}
					GradeDisciplinaVO gradeDisciplinaCriadaVO = inicializarDadosGradeDisciplinaDeAcordoInclusaoHistoricoDisciplinaEdicao(inclusaoDisciplinasHistoricoForaPrazoVO, matriculaVO, null, matriculaPeriodoTurmaDisciplinaVO, cargaHoraria, nrCreditos, usuarioVO);
					adicionarGradeDisciplinaPeriodoLetivoCorrespondente(gradeCurricularVO, gradeDisciplinaCriadaVO.getPeriodoLetivo(), gradeDisciplinaCriadaVO);
					break;

				}
				// MONTANDO DISCIPLINAS EQUIVALENTES
//				if (!inclusaoDisciplinasHistoricoForaPrazoVO.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(0)) {
//					Integer codigoMapaVisualisar = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarCodigoPorMapaEquivalenciaCursada(inclusaoDisciplinasHistoricoForaPrazoVO.getMapaEquivalenciaDisciplinaCursada().getCodigo(), NivelMontarDados.TODOS);
//					MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorMapaEquivalenciaDisciplinaEDisciplina(codigoMapaVisualisar, inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina().getCodigo());
//					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(matriculaVO.getMatricula(), inclusaoDisciplinasHistoricoForaPrazoVO.getTurma().getCodigo(), inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina().getCodigo(), inclusaoDisciplinasHistoricoForaPrazoVO.getAno(), inclusaoDisciplinasHistoricoForaPrazoVO.getSemestre(), false, usuarioVO);
//					HistoricoVO historico = (getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null, usuarioVO));
//					historico.getMapaEquivalenciaDisciplina().setCodigo(codigoMapaVisualisar);
//					GradeDisciplinaVO gradeDisciplinaCriadaVO = inicializarDadosGradeDisciplinaDeAcordoInclusaoHistoricoDisciplinaEdicao(inclusaoDisciplinasHistoricoForaPrazoVO, matriculaVO, historico, matriculaPeriodoTurmaDisciplinaVO, mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria(), mapaEquivalenciaDisciplinaCursadaVO.getNumeroCreditos(), usuarioVO);
//					adicionarGradeDisciplinaPeriodoLetivoCorrespondente(gradeCurricularVO, gradeDisciplinaCriadaVO.getPeriodoLetivo(), gradeDisciplinaCriadaVO);
//				}
			}
		}
	}

	public void realizarMontagemListaDisciplinasAproveitadas(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().clear();
		for (PeriodoLetivoVO periodoLetivoVO : matriculaPeriodoVO.getGradeCurricular().getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gradeDisciplinaVO.getSelecionadoAproveitamento()) {
					// DisciplinasAproveitadasVO.validarDados(gradeDisciplinaVO.getDisciplinasAproveitadasVO(),
					// aproveitamentoDisciplinaVO.getCurso().getPeriodicidade(),
					// aproveitamentoDisciplinaVO.getCurso().getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
					// DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(gradeDisciplinaVO.getDisciplinasAproveitadasVO(),
					// gradeDisciplinaVO.getHistoricoAtualAluno());
					// inicializarDadosDisciplinasAproveitadas(aproveitamentoDisciplinaVO,
					// gradeDisciplinaVO,
					// gradeDisciplinaVO.getDisciplinasAproveitadasVO(),
					// instituicao, cidadeVO, usuarioVO);
					// adicionarObjDisciplinasVOs(inclusaoHistoricoForaPrazoVO,
					// obj);
				}
			}
			if (periodoLetivoVO.getControleOptativaGrupo()) {
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					if ((gradeCurricularGrupoOptativaDisciplinaVO.getSelecionadoAproveitamento()) && (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO().getPeriodoletivoGrupoOptativaVO().getCodigo().equals(periodoLetivoVO.getCodigo()))) {
						// DisciplinasAproveitadasVO.validarDados(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO(),
						// aproveitamentoDisciplinaVO.getCurso().getPeriodicidade(),
						// aproveitamentoDisciplinaVO.getCurso().getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
						// DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO(),
						// gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno());
						// inicializarDadosDisciplinasAproveitadasGrupoOptativa(aproveitamentoDisciplinaVO,
						// periodoLetivoVO,
						// gradeCurricularGrupoOptativaDisciplinaVO,
						// instituicao, cidadeVO, usuarioVO);
						// aproveitamentoDisciplinaVO.adicionarObjDisciplinasAproveitadasVOs(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO());
					}
				}
			}
		}

		// for (MapaEquivalenciaDisciplinaCursadaVO mapaCursadaForaGrade :
		// aproveitamentoDisciplinaVO.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia())
		// {
		// DisciplinasAproveitadasVO.validarDados(mapaCursadaForaGrade.getDisciplinasAproveitadasVO(),
		// aproveitamentoDisciplinaVO.getCurso().getPeriodicidade(),
		// aproveitamentoDisciplinaVO.getCurso().getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
		// DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(mapaCursadaForaGrade.getDisciplinasAproveitadasVO(),
		// mapaCursadaForaGrade.getHistorico());
		// inicializarDadosDisciplinasAproveitadasForaGrade(aproveitamentoDisciplinaVO,
		// mapaCursadaForaGrade, instituicao, cidadeVO, usuarioVO);
		// aproveitamentoDisciplinaVO.adicionarObjDisciplinasAproveitadasVOs(mapaCursadaForaGrade.getDisciplinasAproveitadasVO());
		// }
	}

	public void inicializarDadosDisciplinasAproveitadas(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, GradeDisciplinaVO gradeDisciplinaVO, DisciplinasAproveitadasVO disciplinasAproveitadasVO, String instituicao, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		// if (!aproveitamentoDisciplinaVO.getCodigo().equals(0)) {
		// disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
		// }
		if (disciplinasAproveitadasVO.getFrequencia() == null || disciplinasAproveitadasVO.getFrequencia().equals(0.0)) {
			disciplinasAproveitadasVO.setFrequencia(100.00);
		}
//		if (disciplinasAproveitadasVO.getUtilizaNotaConceito() && disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo() > 0) {
//			disciplinasAproveitadasVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo()));
//		}
		if (!gradeDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setGradeDisciplinaVO(gradeDisciplinaVO);
			disciplinasAproveitadasVO.setPeriodoletivoGrupoOptativaVO(null);
		}
		if (gradeDisciplinaVO.getHistoricoAtualAluno().getCursando()) {
			// se está cursando é por que já existe um histórico gerado para a
			// disciplina
			// logo o mesmo deverá ser excluído pelo sistema, para que seja
			// gerado outro histórico
			disciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
		}
		disciplinasAproveitadasVO.setHistoricoAtual(gradeDisciplinaVO.getHistoricoAtualAluno());
	}

	public void inicializarDadosDisciplinasAproveitadasGrupoOptativa(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, PeriodoLetivoVO periodoLetivoGrupoOptativaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String instituicao, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		DisciplinasAproveitadasVO disciplinasAproveitadasVO = gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO();
		if (!aproveitamentoDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
		}
		if (disciplinasAproveitadasVO.getFrequencia() == null || disciplinasAproveitadasVO.getFrequencia().equals(0.0)) {
			disciplinasAproveitadasVO.setFrequencia(100.00);
		}

//		if (disciplinasAproveitadasVO.getUtilizaNotaConceito() && disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo() > 0) {
//			disciplinasAproveitadasVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo()));
//		}
		if (!periodoLetivoGrupoOptativaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setPeriodoletivoGrupoOptativaVO(periodoLetivoGrupoOptativaVO);
			disciplinasAproveitadasVO.setGradeDisciplinaVO(null);
			disciplinasAproveitadasVO.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO);
		}
		if (gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getCursando()) {
			// se está cursando é por que já existe um histórico gerado para a
			// disciplina
			// logo o mesmo deverá ser excluído pelo sistema, para que seja
			// gerado outro histórico
			disciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
		}
		disciplinasAproveitadasVO.setHistoricoAtual(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno());
	}

	public void inicializarDadosDisciplinasAproveitadasForaGrade(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO, String instituicao, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		DisciplinasAproveitadasVO disciplinasAproveitadasVO = mapaEquivalenciaDisciplinaCursadaVO.getDisciplinasAproveitadasVO();
		if (!aproveitamentoDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
		}
		if (disciplinasAproveitadasVO.getFrequencia() == null || disciplinasAproveitadasVO.getFrequencia().equals(0.0)) {
			disciplinasAproveitadasVO.setFrequencia(100.00);
		}
//		if (disciplinasAproveitadasVO.getUtilizaNotaConceito() && disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo() > 0) {
//			disciplinasAproveitadasVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo()));
//		}
		if (mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getCursando()) {
			// se está cursando é por que já existe um histórico gerado para a
			// disciplina
			// logo o mesmo deverá ser excluído pelo sistema, para que seja
			// gerado outro histórico
			disciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
		}
		// para persitir no aproveitamento o mapa
		mapaEquivalenciaDisciplinaCursadaVO.getDisciplinasAproveitadasVO().setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);

		disciplinasAproveitadasVO.setHistoricoAtual(mapaEquivalenciaDisciplinaCursadaVO.getHistorico());
	}

	public void adicionarObjDisciplinasVOs(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, InclusaoDisciplinasHistoricoForaPrazoVO obj) throws Exception {
		getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().validarDados(obj);
		obj.setInclusaoHistoricoForaPrazoVO(inclusaoHistoricoForaPrazoVO);
		int index = 0;
		Iterator i = inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().iterator();
		while (i.hasNext()) {
			InclusaoDisciplinasHistoricoForaPrazoVO objExistente = (InclusaoDisciplinasHistoricoForaPrazoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().set(index, obj);
				return;
			}
			index++;
		}
		inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().add(obj);
	}

	public void realizarMontagemPainelMapaEquivalenciaDisciplinasAproveitadas(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, MapaEquivalenciaDisciplinaVO mapaEquivalencia, UsuarioVO usuarioVO) throws Exception {
		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : aproveitamentoDisciplinaVO.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia()) {
			DisciplinasAproveitadasVO disciplinasAproveitadasVO = mapaEquivalenciaDisciplinaCursadaVO.getDisciplinasAproveitadasVO();
			if (disciplinasAproveitadasVO.getDisciplinaForaGrade()) {

				// Para cada disciplina aproveitada fora da grade, temos que
				// verificar se a mesma trata-se de uma disciplina
				// cursada do mapa de equivalencia. Caso seja, iremos vincular o
				// aproveitamento a disciplina cursada para que
				// o mesmo possa ser visto e editado pelo usuário
				for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaAtualizarDisciplinaCursadaVO : mapaEquivalencia.getMapaEquivalenciaDisciplinaCursadaVOs()) {
					if ((disciplinasAproveitadasVO.getDisciplina().getCodigo().equals(mapaEquivalenciaAtualizarDisciplinaCursadaVO.getDisciplinaVO().getCodigo())) && (disciplinasAproveitadasVO.getCargaHoraria().equals(mapaEquivalenciaAtualizarDisciplinaCursadaVO.getCargaHoraria()))) {
						// temos que testar tambem a carga horaria, pois pode
						// existir aproveitamento da mesma disciplina (mesmo
						// codigo)
						// de cursos diferentes.
						mapaEquivalenciaAtualizarDisciplinaCursadaVO.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
						mapaEquivalenciaAtualizarDisciplinaCursadaVO.setSelecionadoAproveitamento(Boolean.TRUE);
						break;
					}
				}
			}
		}
	}

	public void validarDadosAnoSemestreInclusao(CursoVO cursoVO, String ano, String semestre) throws Exception {
		if (cursoVO.getSemestral()) {
			if (ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			if (semestre.equals("")) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}
		} else if (cursoVO.getAnual()) {
			if (ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
		}

	}

	public MatriculaPeriodoVO verificarQualMatriculaPeriodoCarregarDadosParaChoqueHorario(MatriculaVO matriculaVO, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, HashMap<String, MatriculaPeriodoVO> mapMatriculaPeriodoVOs, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVO matriculaPeriodoVerificarVO = null;
		if (matriculaVO.getCurso().getIntegral()) {
			if (mapMatriculaPeriodoVOs.containsKey("INTEGRAL")) {
				matriculaPeriodoVerificarVO = mapMatriculaPeriodoVOs.get("INTEGRAL");
			} else {
				matriculaPeriodoVerificarVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarDadosCompletosMatriculaPeriodoPorMatriculaAnoSemestre(matriculaVO.getMatricula(), "", "", configuracaoFinanceiroVO, usuario);
				mapMatriculaPeriodoVOs.put("INTEGRAL", matriculaPeriodoVerificarVO);
			}
		} else if (matriculaVO.getCurso().getSemestral()) {
			if (mapMatriculaPeriodoVOs.containsKey(matricaPeriodoTurmaDisciplina.getAno() + "_" + matricaPeriodoTurmaDisciplina.getSemestre())) {
				matriculaPeriodoVerificarVO = mapMatriculaPeriodoVOs.get(matricaPeriodoTurmaDisciplina.getAno() + "_" + matricaPeriodoTurmaDisciplina.getSemestre());
			} else {
				matriculaPeriodoVerificarVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarDadosCompletosMatriculaPeriodoPorMatriculaAnoSemestre(matriculaVO.getMatricula(), matricaPeriodoTurmaDisciplina.getAno(), matricaPeriodoTurmaDisciplina.getSemestre(), configuracaoFinanceiroVO, usuario);
				mapMatriculaPeriodoVOs.put(matricaPeriodoTurmaDisciplina.getAno() + "_" + matricaPeriodoTurmaDisciplina.getSemestre(), matriculaPeriodoVerificarVO);
			}
		} else {
			if (mapMatriculaPeriodoVOs.containsKey(matricaPeriodoTurmaDisciplina.getAno())) {
				matriculaPeriodoVerificarVO = mapMatriculaPeriodoVOs.get(matricaPeriodoTurmaDisciplina.getAno());
			} else {
				matriculaPeriodoVerificarVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarDadosCompletosMatriculaPeriodoPorMatriculaAnoSemestre(matriculaVO.getMatricula(), matricaPeriodoTurmaDisciplina.getAno(), "", configuracaoFinanceiroVO, usuario);
				mapMatriculaPeriodoVOs.put(matricaPeriodoTurmaDisciplina.getAno(), matriculaPeriodoVerificarVO);
			}
		}
		return matriculaPeriodoVerificarVO;
	}

	/*
	 * Adicionar MatriculaPeriodoTurmaDisciplinaVO recebendo este com os dados
	 * já montados. Este método considera que os dados da Turma estão carregados
	 * (inclusive com os dados da TurmaDisciplina - fk´s para configuracao,
	 * gradeDisciplina, gradeGrupoOptativadisciplina) Este método considera que
	 * MatriculaComHistoricoAlunoVO tambem deve estar montado. Dados da
	 * MatriculaPeriodoTurmaDisciplinaVO também precisam estar preenchidos,
	 * considerando, possíveis variacoes como: disciplina equivalente,
	 * disciplina composta, disciplina grupo optativas ... O mesmo irá adicionar
	 * o obj matricaPeriodoTurmaDisciplina e fazer todas as validacoes
	 * necessarias Desenvolvido conforme Versão 5.0
	 */
	@Override
	public void adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO,Boolean liberarRealizarMatriculaDisciplinaPreRequisito, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, Boolean liberarChoqueHorario, UsuarioVO usuario, GradeCurricularVO gradeCurricularSelecionadaVO, Boolean considerarVagaReposicao, Boolean realizandoRecebimento) throws Exception {
		validarDadosAnoSemestreInclusao(matriculaVO.getCurso(), matricaPeriodoTurmaDisciplina.getAno(), matricaPeriodoTurmaDisciplina.getSemestre());
	    if (!liberarChoqueHorario) { 
			if (matricaPeriodoTurmaDisciplina.getDisciplinaEmRegimeEspecial()) {
				getFacadeFactory().getMatriculaPeriodoFacade().verificarDisciplinaPodeSerEstudaEmRegimeEspecial(matriculaPeriodoVO, matriculaVO, matricaPeriodoTurmaDisciplina, usuario);
			} else {
				getFacadeFactory().getMatriculaPeriodoFacade().executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(matriculaPeriodoVO, matricaPeriodoTurmaDisciplina, horarioAlunoTurnoVOs, matriculaVO.getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), usuario);
			}
		}
		// se chegarmos aqui aqui é por que o aluno não está aprovado nem
		// cursando a disciplina em outros periodos, logo a
		// mesma pode ser adicionada. Para isto vamos gerar uma nova versão da
		// MatriculaPeriodoTurmaDisciplina com os dados
		// já validados e organizados - do ponto de vista de disciplinas
		// equivalentes, disciplinas de grupo de optativas e regulares.
		// por default geramos a MatriculaPeriodoTurmaDisciplina para a
		// disciplina principal da composicao. Logo abaixo, iremos tratar o
		// aspecto da composicao
		MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoFacade().gerarMatriculaPeriodoTurmaDisciplinaVOValidado(matricaPeriodoTurmaDisciplina.getGradeDisciplinaVO(), matricaPeriodoTurmaDisciplina.getDisciplinaPorEquivalencia(), matricaPeriodoTurmaDisciplina.getMapaEquivalenciaDisciplinaVOIncluir(), matricaPeriodoTurmaDisciplina.getMapaEquivalenciaDisciplinaCursada(), matricaPeriodoTurmaDisciplina.getDisciplinaReferenteAUmGrupoOptativa(), matricaPeriodoTurmaDisciplina.getGradeCurricularGrupoOptativaDisciplinaVO(), matricaPeriodoTurmaDisciplina.getDisciplinaEmRegimeEspecial(), matricaPeriodoTurmaDisciplina.getDisciplinaFazParteComposicao(), matricaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO(), matricaPeriodoTurmaDisciplina.getModalidadeDisciplina(), matriculaPeriodoVO, matriculaVO, matricaPeriodoTurmaDisciplina.getTurma(), matricaPeriodoTurmaDisciplina.getTurmaPratica(), matricaPeriodoTurmaDisciplina.getTurmaTeorica(), usuario);
		novaMatriculaPeriodoTurmaDisciplina.setDataRegistroHistorico(matricaPeriodoTurmaDisciplina.getDataRegistroHistorico());
		novaMatriculaPeriodoTurmaDisciplina.setDataInicioAula(matricaPeriodoTurmaDisciplina.getDataInicioAula());
		novaMatriculaPeriodoTurmaDisciplina.setDataFimAula(matricaPeriodoTurmaDisciplina.getDataFimAula());
		if (liberarRealizarMatriculaDisciplinaPreRequisito) {
			novaMatriculaPeriodoTurmaDisciplina.setLiberarInclusaoDisciplinaPreRequisito(true);
		}
		if (liberarChoqueHorario) {
			novaMatriculaPeriodoTurmaDisciplina.setUsuarioLiberacaoChoqueHorario(matricaPeriodoTurmaDisciplina.getUsuarioLiberacaoChoqueHorario());
			matriculaPeriodoVO.getMensagemAvisoUsuario().setListaMensagemErro(null);
		}
		if (matricaPeriodoTurmaDisciplina.getDisciplinaForaGrade()) {
			// Quando a disciplina é fora da Grade é preciso buscar a
			// gradeDisciplina q está na TurmaDisciplina da turma selecionada
			inicializarDadosGradeDisciplinaDisciplinaForaGradeDeAcordoTurmaSelecionada(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina);
		}

		inicializarDadosInclusaoForaPrazoParaMatriculaPeriodoTurmaDisciplina(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, usuario);
		getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoTurmaDisciplinaVOParaAluno(matriculaPeriodoVO, matriculaVO, liberarRealizarMatriculaDisciplinaPreRequisito, novaMatriculaPeriodoTurmaDisciplina, usuario, realizandoRecebimento);
		
		/**
		 * Adicionado regra para que seja possível incluir uma disciplina que faz parte da composição conforme configuração do
		 * <code>TipoControleComposicaoEnum</code> ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA, no qual, torna possível selecionar quais disciplinas de uma
		 * composição será cursada de acordo com o que foi ofertado.
		 */
		if (matricaPeriodoTurmaDisciplina.getDisciplinaFazParteComposicao()) {
			adicionarMatriculaPeriodoTurmaDisciplinaFazParteComposicao(matriculaPeriodoVO, matriculaVO, gradeDisciplinaVO, gradeDisciplinaCompostaVO, gradeCurricularGrupoOptativaDisciplinaVO, liberarRealizarMatriculaDisciplinaPreRequisito, matricaPeriodoTurmaDisciplina, novaMatriculaPeriodoTurmaDisciplina, horarioAlunoTurnoVOs, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, liberarChoqueHorario, usuario, gradeCurricularSelecionadaVO, considerarVagaReposicao);
			return;
			
		}
		if(Uteis.isAtributoPreenchido(gradeDisciplinaVO) && novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()){
			gradeDisciplinaVO.getGradeDisciplinaCompostaVOs().clear();
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO) && novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()){
			gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs().clear();
		}
		// Verifica se a disciplina equivale
		// Nesse caso será incluída a disciplina filha
		if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaEquivale()) {
			if(matricaPeriodoTurmaDisciplina.getDisciplinaReferenteAUmGrupoOptativa()) {
				adicionarDisciplinaGrupoOptativaEquivalente(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeCurricularGrupoOptativaDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, usuario);
			} else {				
				adicionarDisciplinaEquivalente(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, usuario);
			}
			// Verifica se a disciplina é referente a um Grupo de Optativa
			// Nesse caso a disciplina está sendo adicionada da opção Adicionar
			// Optativas
		} else if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaReferenteAUmGrupoOptativa()) {
			adicionarDisciplinaGrupoOptativa(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, usuario);			
			// Verifica se a disciplina é composta
			// Caso seja composta será criado as disciplinas que fazem
			// parte da composição
		} else if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()) {
			if(matricaPeriodoTurmaDisciplina.getDisciplinaReferenteAUmGrupoOptativa()) {
				adicionarDisciplinaGrupoOptativaComposta(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeCurricularGrupoOptativaDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, Boolean.TRUE, usuario);
			} else {
				adicionarDisciplinaComposta(novaMatriculaPeriodoTurmaDisciplina, matricaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, Boolean.FALSE, usuario);				
			}
			adicionarDisciplinaNormal(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, usuario);
		} else if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaForaGrade()) {
			adicionarDisciplinaForaGrade(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, usuario);
			// Esse é o caso mais normal de acontecer, caso esteja adicionando
			// uma disciplina normal.
		} else {
			adicionarDisciplinaNormal(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, gradeCurricularSelecionadaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, usuario);
		}
		getFacadeFactory().getMatriculaPeriodoFacade().realizarSugestaoTurmaPraticaTeorica(matriculaPeriodoVO, matriculaVO.getCurso().getConfiguracaoAcademico(), horarioAlunoTurnoVOs, considerarVagaReposicao, usuario);
		if (!matriculaPeriodoVO.getMensagemAvisoUsuario().getListaMensagemErro().isEmpty() && !liberarChoqueHorario) {
			gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(null);
			gradeDisciplinaVO.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
			gradeDisciplinaVO.setSelecionado(Boolean.FALSE);
			for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().iterator(); iterator.hasNext();) {
				MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = (MatriculaPeriodoTurmaDisciplinaVO) iterator.next();
				if(matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao() && matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().getGradeDisciplina().equals(gradeDisciplinaVO.getCodigo())){
					iterator.remove();
				}
			}
			throw matriculaPeriodoVO.getMensagemAvisoUsuario();
		}
		matriculaPeriodoVO.adicionarObjMatriculaPeriodoTurmaDisciplinaVOs(novaMatriculaPeriodoTurmaDisciplina);
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(matriculaPeriodoVO, novaMatriculaPeriodoTurmaDisciplina, novaMatriculaPeriodoTurmaDisciplina.getDisciplina(),  novaMatriculaPeriodoTurmaDisciplina.getAno(), novaMatriculaPeriodoTurmaDisciplina.getSemestre(), true, considerarVagaReposicao);
	}

	public void inicializarDadosGradeDisciplinaDisciplinaForaGradeDeAcordoTurmaSelecionada(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina) throws Exception {
		novaMatriculaPeriodoTurmaDisciplina.setDisciplinaForaGrade(Boolean.TRUE);
		novaMatriculaPeriodoTurmaDisciplina.setDisciplina(matricaPeriodoTurmaDisciplina.getDisciplina());
		TurmaDisciplinaVO turmaDisciplinaVO = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaDisciplina(matricaPeriodoTurmaDisciplina.getTurma().getCodigo(), matricaPeriodoTurmaDisciplina.getDisciplina().getCodigo(), false, null);
		// Como o aluno está estudando por equivalencia, temos que adotar as
		// regras (configuracao)
		// e o tipo de disciplina (GradeDisciplinVO ou
		// GradeCurricularGrupoOptativaDisciplinaVO) desta turma alvo.
		if (turmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa()) {
			novaMatriculaPeriodoTurmaDisciplina.setDisciplinaReferenteAUmGrupoOptativa(true);
			if (turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo() > 0) {
				novaMatriculaPeriodoTurmaDisciplina.setGradeCurricularGrupoOptativaDisciplinaVO(getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), null));
			}
		} else {
			novaMatriculaPeriodoTurmaDisciplina.setDisciplinaReferenteAUmGrupoOptativa(false);
			if (turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo() > 0) {
				novaMatriculaPeriodoTurmaDisciplina.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo(), null));
			}
		}
	}

	public void adicionarDisciplinaGrupoOptativa(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, UsuarioVO usuario) throws Exception {
		// Cria a GradeDisciplina que será incluída na lista de PeriodoLetivo
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = realizarCriacaoGradeCurricularGrupoOptativaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), matriculaPeriodoAdicionadaVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
		// Adiciona a GradeDisciplina criada na lista
		PeriodoLetivoVO periodoLetivoUtilizarVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorAnoSemestreMatriculaPeriodoEMatricula(matriculaVO.getMatricula(), matriculaPeriodoAdicionadaVO.getAno(), matriculaPeriodoAdicionadaVO.getSemestre(), matriculaVO.getCurso().getPeriodicidade(), usuario);
		if (periodoLetivoUtilizarVO == null) {
			adicionarGradeCurricularGrupoOptativaDisciplinaVOPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getPeriodoLetivo(), gradeCurricularGrupoOptativaDisciplinaVO);
		} else {
			adicionarGradeCurricularGrupoOptativaDisciplinaVOPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoUtilizarVO.getPeriodoLetivo(), gradeCurricularGrupoOptativaDisciplinaVO);
		}
		for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO:gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()){			
			for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO2: gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
				if(novaMatriculaPeriodoTurmaDisciplina.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVO2.getCodigo())){
					gradeCurricularGrupoOptativaDisciplinaVO2.setHistoricoAtualAluno(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno());
					gradeCurricularGrupoOptativaDisciplinaVO2.setMatriculaPeriodoTurmaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO());
					gradeCurricularGrupoOptativaDisciplinaVO2.setSelecionado(true);
					return;
				}
			
			}
		}
		// Verifica se é composta para realizar a criação das GradesDisciplinas
		// que serão incluídas posteriormente.
//		if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()) {
//			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs = gerarEAdicionarMatriculaPeriodoTurmaDisciplinaVOParaDisciplinasComposicao(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoAdicionadaVO, matriculaVO, usuario);
//			realizarCriacaoGradeDisciplinaDisciplinaComposta(listaMatriculaPeriodoTurmaDisciplinaVOs, gradeCurricularVO, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoAdicionadaVO, matriculaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, usuario);
//		}
	}

	public void adicionarDisciplinaComposta(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, Boolean equivalenciaComposta, UsuarioVO usuario) throws Exception {
		if (TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS.equals(novaMatriculaPeriodoTurmaDisciplina.getGradeDisciplinaVO().getTipoControleComposicao())) {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs = gerarEAdicionarMatriculaPeriodoTurmaDisciplinaVOParaDisciplinasComposicao(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoAdicionadaVO, matriculaVO, usuario);
			realizarCriacaoGradeDisciplinaDisciplinaComposta(listaMatriculaPeriodoTurmaDisciplinaVOs, gradeCurricularVO, gradeDisciplinaVO.getPeriodoLetivoVO(), matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoAdicionadaVO, matriculaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, usuario);
			for(MatriculaPeriodoTurmaDisciplinaVO mptd: listaMatriculaPeriodoTurmaDisciplinaVOs){
				mptd.setDataRegistroHistorico(matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO.getDataRegistroHistorico());
				matriculaPeriodoAdicionadaVO.adicionarObjMatriculaPeriodoTurmaDisciplinaVOs(mptd);
			}
		}
		if (!equivalenciaComposta) {
			gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
			gradeDisciplinaVO.setSelecionado(Boolean.TRUE);
		}
	}

	public void adicionarDisciplinaEquivalente(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, UsuarioVO usuario) throws Exception {
		GradeDisciplinaVO gradeDisciplinaIncluirVO = null;
		PeriodoLetivoVO periodoLetivoUtilizarVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorAnoSemestreMatriculaPeriodoEMatricula(matriculaVO.getMatricula(), matriculaPeriodoAdicionadaVO.getAno(), matriculaPeriodoAdicionadaVO.getSemestre(), matriculaVO.getCurso().getPeriodicidade(), usuario);
		if (periodoLetivoUtilizarVO == null) {
			gradeDisciplinaIncluirVO = realizarCriacaoGradeDisciplina(novaMatriculaPeriodoTurmaDisciplina, gradeDisciplinaVO.getPeriodoLetivoVO(), matriculaPeriodoAdicionadaVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			adicionarGradeDisciplinaPeriodoLetivoCorrespondente(gradeCurricularVO, gradeDisciplinaVO.getPeriodoLetivoVO().getPeriodoLetivo(), gradeDisciplinaIncluirVO);		
		} else {
			gradeDisciplinaIncluirVO = realizarCriacaoGradeDisciplina(novaMatriculaPeriodoTurmaDisciplina, periodoLetivoUtilizarVO, matriculaPeriodoAdicionadaVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
//			adicionarGradeDisciplinaPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoUtilizarVO.getPeriodoLetivo(), gradeDisciplinaIncluirVO);
			gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(gradeDisciplinaIncluirVO.getMatriculaPeriodoTurmaDisciplinaVO());
			HistoricoVO historicoMapaEquivalenciaDisciplinaCursada = (HistoricoVO) gradeDisciplinaIncluirVO.getHistoricoAtualAluno().clone();
			gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMapaEquivalenciaDisciplinaCursada().setHistorico(historicoMapaEquivalenciaDisciplinaCursada);
			gradeDisciplinaVO.setHistoricoAtualAluno(gradeDisciplinaIncluirVO.getHistoricoAtualAluno());
			gradeDisciplinaVO.setSelecionado(true);
			matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular().add(historicoMapaEquivalenciaDisciplinaCursada);
		}
		gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().setDisciplinaPorEquivalencia(Boolean.TRUE);
		gradeDisciplinaVO.getHistoricoAtualAluno().setHistoricoPorEquivalencia(true);
		gradeDisciplinaVO.getHistoricoAtualAluno().setHistoricoEquivalente(false);
		gradeDisciplinaVO.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
		matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs().add(novaMatriculaPeriodoTurmaDisciplina);
		if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()) {
			adicionarDisciplinaComposta(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoAdicionadaVO, matriculaVO, gradeCurricularVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, Boolean.TRUE, usuario);
		}
	}

	public void adicionarDisciplinaNormal(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, UsuarioVO usuario) throws Exception {
		gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
		gradeDisciplinaVO.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.CURSANDO.getValor());
		gradeDisciplinaVO.setSelecionado(Boolean.TRUE);
	}

	public void adicionarDisciplinaForaGrade(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, UsuarioVO usuario) throws Exception {
		PeriodoLetivoVO periodoLetivoUtilizarVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorAnoSemestreMatriculaPeriodoEMatricula(matriculaVO.getMatricula(), matriculaPeriodoAdicionadaVO.getAno(), matriculaPeriodoAdicionadaVO.getSemestre(), matriculaVO.getCurso().getPeriodicidade(), usuario);
		validarDadosPeridoLetivo(periodoLetivoUtilizarVO, matriculaVO.getCurso());
		GradeDisciplinaVO gradeDisciplinaIncluirVO = realizarCriacaoGradeDisciplina(novaMatriculaPeriodoTurmaDisciplina, periodoLetivoUtilizarVO, matriculaPeriodoAdicionadaVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
		adicionarGradeDisciplinaPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoUtilizarVO.getPeriodoLetivo(), gradeDisciplinaIncluirVO);
	}

	public void validarDadosPeridoLetivo(PeriodoLetivoVO periodoLetivoVO, CursoVO cursoVO) throws Exception {
		if (periodoLetivoVO == null) {
			if (cursoVO.getIntegral()) {
				throw new Exception("Não foi encontrado o PERÍODO LETIVO para a inclusão da DISCIPLINA FORA DA GRADE.");
			}
			if (cursoVO.getSemestral()) {
				throw new Exception("Não foi encontrado um PERÍODO LETIVO de acordo o ano e semestre informado.");
			}
			if (cursoVO.getAnual()) {
				throw new Exception("Não foi encontrado um PERÍODO LETIVO de acordo com o ano informado. ");
			}
		}
	}

	/**
	 * Método responsável por gerar as MatriculaPeriodoTurmaDisciplinaVO para as
	 * disciplinas de uma disciplina composta. Versão 5.0
	 * 
	 * @param novaMatriculaPeriodoTurmaDisciplina
	 * @param matriculaPeriodoVO
	 * @return
	 * @throws Exception
	 */
	public List<MatriculaPeriodoTurmaDisciplinaVO> gerarEAdicionarMatriculaPeriodoTurmaDisciplinaVOParaDisciplinasComposicao(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		if (!novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()) {
			return new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		// obtendo os detalhes das disciplinas que fazem parte da composicao.
		// abaixo vamos obter a lista de disciplinas que fazer parte da
		// composticao. Vale ressaltar que o código abaixo já está
		// observando se a disciplina em questão vem de um grupo de optativa ou
		// é de uma gradedisciplina recular.
		// Com relação a equivalencia isto indifere, pois ao cursar uma
		// disciplina por equivalencia, as regras da disciplina equivalente
		// já são refletidas automaticamente pelo método abaixo. Ou seja, mesma
		// que seja uma disciplina equivalente (de outro curso)
		// se ela for de um grupo de optativa, isto estará definido nos
		// atributos referentes a grupo de optativa e o método abaixo irá
		// perceber isto.
		List<GradeDisciplinaCompostaVO> listaGradesDisciplinaCompostaVO = null;
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaReferenteAUmGrupoOptativa()) {
			listaGradesDisciplinaCompostaVO = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGrupoOptativaDisciplina(novaMatriculaPeriodoTurmaDisciplina.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
		} else {
			listaGradesDisciplinaCompostaVO = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGradeDisciplina(novaMatriculaPeriodoTurmaDisciplina.getGradeDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}

		if (listaGradesDisciplinaCompostaVO.isEmpty()) {
			throw new Exception("Está sendo incluída uma disciplina composta (" + novaMatriculaPeriodoTurmaDisciplina.getDisciplina().getCodigo() + " - " + novaMatriculaPeriodoTurmaDisciplina.getDisciplina().getNome() + "), contudo a composição da mesma não foi defenida na Matriz Curricular correspondente da turma: " + novaMatriculaPeriodoTurmaDisciplina.getTurma().getIdentificadorTurma());
		}
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : listaGradesDisciplinaCompostaVO) {
			MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplinaComposicao = getFacadeFactory().getMatriculaPeriodoFacade().gerarMatriculaPeriodoTurmaDisciplinaVOValidado(null, false, // indica
					// se
					// a
					// disciplina
					// será
					// cursada
					// por
					// meio
					// de
					// um
					// mapa
					// de
					// equivalencia
					null, // nao podem ser cursadas por equivalencia, pois sao
							// composicoes de uma disciplina principal
					null, // nao podem ser cursadas por equivalencia, pois sao
							// composicoes de uma disciplina principal
					false, // nao tratamos a mesma como um GrupoOptativa, pois
							// trata-se de uma composticao
					null, // nao tratamos a mesma como um GrupoOptativa, pois
							// trata-se de uma composticao
					novaMatriculaPeriodoTurmaDisciplina.getDisciplinaEmRegimeEspecial(), true, // true
																								// para
																								// indicar
																								// que
																								// é
																								// uma
																								// disciplina
																								// de
																								// uma
																								// composicao
					gradeDisciplinaCompostaVO, // dados da disciplina da
												// composicao (codigo da
												// disciplina, carga horaria,
												// cfg e agins)
					gradeDisciplinaCompostaVO.getModalidadeDisciplina(), matriculaPeriodoVO, matriculaVO, novaMatriculaPeriodoTurmaDisciplina.getTurma(), novaMatriculaPeriodoTurmaDisciplina.getTurmaPratica(), novaMatriculaPeriodoTurmaDisciplina.getTurmaTeorica(), usuario);

			listaMatriculaPeriodoTurmaDisciplinaVOs.add(novaMatriculaPeriodoTurmaDisciplinaComposicao);
		}
		return listaMatriculaPeriodoTurmaDisciplinaVOs;
	}

	public void realizarCriacaoGradeDisciplinaDisciplinaComposta(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs, GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, GradeDisciplinaVO gradeDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina : listaMatriculaPeriodoTurmaDisciplinaVOs) {
			inicializarDadosInclusaoForaPrazoParaMatriculaPeriodoTurmaDisciplina(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoVO, matriculaVO, gradeDisciplinaVO, configuracaoGeralSistemaVO, usuarioVO);
			GradeDisciplinaVO gradeDisciplinaIncluirVO = new GradeDisciplinaVO();
			gradeDisciplinaIncluirVO.setSelecionado(Boolean.TRUE);
			gradeDisciplinaIncluirVO.setDisciplina(novaMatriculaPeriodoTurmaDisciplina.getDisciplina());
			gradeDisciplinaIncluirVO.setCargaHoraria(novaMatriculaPeriodoTurmaDisciplina.getCargaHorariaDisciplina());
			gradeDisciplinaIncluirVO.setNrCreditos(novaMatriculaPeriodoTurmaDisciplina.getCreditosDisciplina());
			gradeDisciplinaIncluirVO.setPeriodoLetivoVO(periodoLetivoVO);
			gradeDisciplinaIncluirVO.setPeriodoLetivo(periodoLetivoVO.getPeriodoLetivo());
			gradeDisciplinaIncluirVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
			removerGradeDisciplinaEquivalente(gradeCurricularVO.getPeriodoLetivosVOs(), gradeDisciplinaIncluirVO);
			adicionarGradeDisciplinaPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoVO.getPeriodoLetivo(), gradeDisciplinaIncluirVO);
		}

	}

	public GradeDisciplinaVO realizarCriacaoGradeDisciplina(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, PeriodoLetivoVO periodoLetivoVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception {
		GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
		gradeDisciplinaVO.setSelecionado(Boolean.TRUE);
		gradeDisciplinaVO.setDisciplina(novaMatriculaPeriodoTurmaDisciplina.getDisciplina());
		gradeDisciplinaVO.setCargaHoraria(novaMatriculaPeriodoTurmaDisciplina.getCargaHorariaDisciplina());
		gradeDisciplinaVO.setNrCreditos(novaMatriculaPeriodoTurmaDisciplina.getCreditosDisciplina());
		gradeDisciplinaVO.setPeriodoLetivoVO(periodoLetivoVO);
		gradeDisciplinaVO.setPeriodoLetivo(periodoLetivoVO.getPeriodoLetivo());
		gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
		gradeDisciplinaVO.setHistoricoAtualAluno(getFacadeFactory().getHistoricoFacade().gerarHistoricoVOComBaseMatriculaPeriodoTurmaDisciplina(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuarioVO));
		return gradeDisciplinaVO;
	}

	public void adicionarGradeDisciplinaPeriodoLetivoCorrespondente(GradeCurricularVO gradeCurricularVO, Integer periodoLetivoCorrespondente, GradeDisciplinaVO gradeDisciplinaIncluirVO) {
		for (PeriodoLetivoVO periodoLetivoVO : gradeCurricularVO.getPeriodoLetivosVOs()) {
			int index = 0;
			if (periodoLetivoCorrespondente.equals(periodoLetivoVO.getPeriodoLetivo())) {
				Boolean localizouDisciplina = Boolean.FALSE;
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
					if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(gradeDisciplinaIncluirVO.getDisciplina().getCodigo())) {
						periodoLetivoVO.getGradeDisciplinaVOs().set(index, gradeDisciplinaIncluirVO);
						localizouDisciplina = Boolean.TRUE;
						break;
					}
					index++;
				}
				if (!localizouDisciplina) {
					periodoLetivoVO.getGradeDisciplinaVOs().add(gradeDisciplinaIncluirVO);
					break;
				}
				break;
			}
		}

	}

	public void removerGradeDisciplinaEquivalente(List<PeriodoLetivoVO> listaPeriodoLetivoVOs, GradeDisciplinaVO gradeDisciplinaExcluirVO) {
		for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {		
			Boolean localizouDisciplina = Boolean.FALSE;
			for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(gradeDisciplinaExcluirVO.getDisciplina().getCodigo())) {
					gradeDisciplinaVO.setSelecionado(false);					
					localizouDisciplina = Boolean.TRUE;
					break;
				}				
			}
			if (localizouDisciplina) {
				break;
			}
		}
	}

	public void removerMatriculaPeriodoTurmaDisciplinaEquivalenteCursada(MatriculaPeriodoVO obj, Integer codigoDisciplina) {
		int index = 0;
		Iterator i = obj.getMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().intValue() == codigoDisciplina) {
				obj.getMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Método responsável por alterar a situação da disciplina da Matriz
	 * curricular do mapa de equivalência, caso a disciplina que equivale seja
	 * removida da lista. Para isso é verificado se todas disciplinas a cursar
	 * do mapa já estão fora da lista.
	 */
	public void alterarSituacaoDisciplinaCursandoPorEquivalenciaAposRemocaoDisciplinaEquivale(List<PeriodoLetivoVO> listaPeriodoLetivoVOs, GradeDisciplinaVO gradeDisciplinaEquivaleVO, MapaEquivalenciaDisciplinaVO mapaVisualizar) {
		Boolean existeDisciplinaEquivaleLista = Boolean.FALSE;
		for (MapaEquivalenciaDisciplinaCursadaVO mapaCursadaVO : mapaVisualizar.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if (!mapaCursadaVO.getDisciplinaVO().getCodigo().equals(gradeDisciplinaEquivaleVO.getDisciplina().getCodigo())) {

				for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {
					for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
						if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(mapaCursadaVO.getDisciplinaVO().getCodigo())) {
							existeDisciplinaEquivaleLista = Boolean.TRUE;
							break;
						}
					}
					if (!existeDisciplinaEquivaleLista) {
						break;
					}
				}

			}
		}
		if (!existeDisciplinaEquivaleLista) {
			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaMatrizCurricularVO : mapaVisualizar.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
				for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {
					for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
						if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(mapaMatrizCurricularVO.getDisciplinaVO().getCodigo())) {
							gradeDisciplinaVO.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
							gradeDisciplinaVO.getHistoricoAtualAluno().setHistoricoPorEquivalencia(false);
						}
					}
				}
			}

		}

	}

	public void inicializarDadosInclusaoForaPrazoParaMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, GradeDisciplinaVO gradeDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina(configuracaoGeralSistemaVO, novaMatriculaPeriodoTurmaDisciplinaVO);
		novaMatriculaPeriodoTurmaDisciplinaVO.setInclusaoForaPrazo(true);
		novaMatriculaPeriodoTurmaDisciplinaVO.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
		novaMatriculaPeriodoTurmaDisciplinaVO.setMatriculaPeriodoObjetoVO(matriculaPeriodoVO);
		novaMatriculaPeriodoTurmaDisciplinaVO.setMatricula(matriculaVO.getMatricula());
		novaMatriculaPeriodoTurmaDisciplinaVO.setMatriculaObjetoVO(matriculaVO);
		if (matriculaVO.getCurso().getIntegral()) {
			novaMatriculaPeriodoTurmaDisciplinaVO.setAno(matriculaPeriodoVO.getAno());
			novaMatriculaPeriodoTurmaDisciplinaVO.setSemestre(matriculaPeriodoVO.getSemestre());
		} else if (matriculaVO.getCurso().getSemestral()) {
			novaMatriculaPeriodoTurmaDisciplinaVO.setAno(matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO.getAno());
			novaMatriculaPeriodoTurmaDisciplinaVO.setSemestre(matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO.getSemestre());
		} else {
			novaMatriculaPeriodoTurmaDisciplinaVO.setAno(matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO.getAno());
			novaMatriculaPeriodoTurmaDisciplinaVO.setSemestre(matriculaPeriodoVO.getSemestre());
		}
		novaMatriculaPeriodoTurmaDisciplinaVO.setDisciplinaIncluida(Boolean.TRUE);
	}

	public void validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		if (configuracaoGeralSistemaVO.getValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina().booleanValue()) {
			Date data = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre());
			if (data != null) {
				if (data.after(new Date())) {
					List listaTurmaDisciplina = getFacadeFactory().getTurmaDisciplinaFacade().consultarMapaLocalAulaTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getUnidadeEnsino().getCodigo(), new Date(), Uteis.obterDataFutura(new Date(), 3650), 0, matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
					if (listaTurmaDisciplina.isEmpty()) {
						throw new Exception("Não existe uma local de aula/sala definido para a turma e disciplina. Para realizar a reposição é necessário que haja essa definição, posteriormente será permitido realizar a reposição.");
					} else if (listaTurmaDisciplina.size() > 1) {
						throw new Exception("Existe dois locais de aula/sala definido para a turma e disciplina. Para realizar a reposição é necessário que exista apenas um local de aula/sala cadastrado.");
					} else {
						MapaLocalAulaTurmaVO t = (MapaLocalAulaTurmaVO) listaTurmaDisciplina.get(0);
						if (t.getTurmaDisciplina().getLocalAula().getCodigo().intValue() == 0 || t.getTurmaDisciplina().getSalaLocalAula().getCodigo().intValue() == 0) {
							throw new Exception("Não existe uma local de aula/sala definido para a turma e disciplina. Para realizar a reposição é necessário que haja essa definição, posteriormente será permitido realizar a reposição.");
						} else {
							Integer qtd = t.getQtdeAluno().intValue() + t.getQtdeAlunoExtRep().intValue();
							SalaLocalAulaVO sala = (SalaLocalAulaVO) getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(t.getTurmaDisciplina().getSalaLocalAula().getCodigo());
							if (qtd >= sala.getCapacidade().intValue()) {
								throw new Exception("A sala de aula encontra-se lotada. Capacidade = " + sala.getCapacidade().intValue() + "; Alunos alocados a sala = " + qtd + ";");
							}
						}
					}
				}
			}
		}
	}

	public void validarDadosDisciplinaIncluida(List<PeriodoLetivoVO> listaPeriodoLetivoVOs) throws Exception {
		Boolean selecionado = false;
		for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {
			for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gradeDisciplinaVO.getSelecionado()) {
					selecionado = true;
					break;
				}
			}
			for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
				if (gradeCurricularGrupoOptativaDisciplinaVO.getSelecionado()) {
					selecionado = true;
					break;
				}
			}
		}
		if (!selecionado) {
			throw new Exception("Nenhuma disciplina foi selecionada para a Inclusão. Por favor selecione para prosseguir a operação.");
		}
	}

	public void validarDadosReposicao(boolean incluindaPeloPagamentoRequerimento, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		if (matriculaPeriodoVO.getReposicao() && configuracaoFinanceiroVO.getUtilizaPlanoFinanceiroReposicao() && !incluindaPeloPagamentoRequerimento) {
			if (matriculaPeriodoVO.getPlanoFinanceiroReposicaoVO() == null || matriculaPeriodoVO.getPlanoFinanceiroReposicaoVO().getCodigo() == 0) {
				throw new Exception("O campo PLANO FINANCEIRO REPOSIÇÃO é obrigatório");
			}
		}
	}

	public MatriculaPeriodoVO consultarMatriculaPeriodoPorMatriculaAnoSemestre(MatriculaVO matriculaVO, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		MatriculaPeriodoVO matriculaP = new MatriculaPeriodoVO();
		if (matriculaVO.getCurso().getIntegral()) {
			matriculaP = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(matriculaVO.getMatricula(), "", "", false, usuarioVO);
		} else {
			matriculaP = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(matriculaVO.getMatricula(), semestre, ano, false, usuarioVO);
		}
		return matriculaP;
	}

	public void validarDados(RequerimentoVO requerimentoVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO) throws Exception {
		if (!matriculaPeriodoVO.getInclusaoForaPrazo() && !matriculaPeriodoVO.getReposicao()) {
			throw new Exception("Deve ser selecionado o tipo desejado para realizar a gravação (INCLUSÃO/REPOSIÇÃO) aba Dados Gerais ");
		}
		if (matriculaVO.getMatricula().equals("")) {
			throw new Exception("Matrícula não foi encontrada, por favor consulte o aluno e posteriormente tente novamente.");
		}
		if (!requerimentoVO.getCodigo().equals(0)) {
			if (consultarRequerimentoJaUtilizadoInclusao(requerimentoVO.getCodigo())) {
				throw new Exception("O REQUERIMENTO informado já foi utilizado para a inclusão de Disciplina.");
			}
		}
	}
	
	public Boolean consultarRequerimentoJaUtilizadoInclusao(Integer requerimento) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo from inclusaohistoricoforaprazo  where requerimento = ").append(requerimento);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public void persistir(boolean incluindaPeloPagamentoRequerimento, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, List<PeriodoLetivoVO> listaPeriodoLetivoVOs, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, String justificativa, String observacaoJustificativa, TextoPadraoVO textoPadraoContratoInclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, RequerimentoVO requerimentoVO, GradeCurricularVO gradeCurricularVO, HashMap<String, MatriculaPeriodoVO> mapMatriculaPeriodoVOs, boolean realizandoBaixaAutomatica, UsuarioVO usuarioVO) throws Exception {
		validarDados(requerimentoVO, matriculaPeriodoVO, matriculaVO);
		validarDadosDisciplinaIncluida(listaPeriodoLetivoVOs);
		if (matriculaPeriodoVO.getValorTotalParcelaInclusaoForaPrazo() > 0 && matriculaPeriodoVO.getNumParcelasInclusaoForaPrazo() == 0) {
			throw new Exception("Para gerar as contas dessa inclusão/reposição é necessário informar o número de parcelas.");
		}
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {
			executarGeracaoInclusaoHistoricoForaPrazoGradeDisciplina(incluindaPeloPagamentoRequerimento, periodoLetivoVO, inclusaoHistoricoForaPrazoVO, listaPeriodoLetivoVOs, matriculaPeriodoVO, matriculaVO, justificativa, observacaoJustificativa, textoPadraoContratoInclusao, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO, listaMatriculaPeriodoTurmaDisciplinaVOs);
			executarGeracaoInclusaoHistoricoForaPrazoGradeCurricularGrupoOptativaDisciplina(incluindaPeloPagamentoRequerimento, periodoLetivoVO, inclusaoHistoricoForaPrazoVO, listaPeriodoLetivoVOs, matriculaPeriodoVO, matriculaVO, justificativa, observacaoJustificativa, textoPadraoContratoInclusao, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO, listaMatriculaPeriodoTurmaDisciplinaVOs);
		}
		if (Uteis.isAtributoPreenchido(mapMatriculaPeriodoVOs)) {
			List<Integer> codigoMatriculaPeriodoVerificado = new ArrayList<>();
			for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaMatriculaPeriodoTurmaDisciplinaVOs) {
				MatriculaPeriodoVO mpvo = verificarQualMatriculaPeriodoCarregarDadosParaChoqueHorario(matriculaVO, mptd, configuracaoFinanceiroVO, mapMatriculaPeriodoVOs, usuarioVO);
				if (codigoMatriculaPeriodoVerificado.stream().noneMatch(mpvo.getCodigo()::equals)) {
					liberarValidacaoPreRequisitoDisciplinasJaIncluidas(mpvo.getMatriculaPeriodoTumaDisciplinaVOs());
					getFacadeFactory().getMatriculaPeriodoFacade().verificarPreRequisitoDisciplina(mpvo, matriculaVO, false, usuarioVO);
					codigoMatriculaPeriodoVerificado.add(mpvo.getCodigo());
				}
			}
		}
		MatriculaPeriodoVO matPer= new MatriculaPeriodoVO();
		matPer.setMatriculaPeriodoTumaDisciplinaVOs(listaMatriculaPeriodoTurmaDisciplinaVOs);
		if (!realizandoBaixaAutomatica) {
			getFacadeFactory().getMatriculaPeriodoFacade().validarDisciplinaSemVaga(matPer, matriculaVO.getCurso().getConfiguracaoAcademico(), usuarioVO);		
		}
		preencherDadosInclusaoHistoricoForaPrazo(inclusaoHistoricoForaPrazoVO, matriculaPeriodoVO, justificativa, observacaoJustificativa, textoPadraoContratoInclusao, requerimentoVO, usuarioVO);
		matriculaVO.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimariaTipoPessoa(matriculaVO.getAluno().getCodigo(), usuarioVO));
		getFacadeFactory().getMatriculaPeriodoFacade().gerarInclusaoForaPrazo(incluindaPeloPagamentoRequerimento, inclusaoHistoricoForaPrazoVO, listaMatriculaPeriodoTurmaDisciplinaVOs, matriculaVO, matriculaPeriodoVO, configuracaoFinanceiroVO, gradeCurricularVO, usuarioVO);
		if (!requerimentoVO.getCodigo().equals(0) && !requerimentoVO.getTipoRequerimento().getDeferirAutomaticamente()) {
			if (!requerimentoVO.getSituacao().equals(SituacaoRequerimento.EM_EXECUCAO.getValor())) {
				throw new Exception("A situação do Requerimento deve estar em Execução.");
			}
			getFacadeFactory().getRequerimentoFacade().alterarSituacaoRequerimento(requerimentoVO.getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), usuarioVO);
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinaApartirDoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO, Boolean realizandoRecebimento, boolean realizandoBaixaAutomatica) throws Exception {
		if(!requerimentoVO.getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
			getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoVO, usuarioVO);
		}
		ConfiguracaoFinanceiroVO configFin = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo(), usuarioVO);
		ConfiguracaoGeralSistemaVO configGeral = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo());
		MatriculaPeriodoTurmaDisciplinaVO mptd = realizarPreenchimentoDadosMatriculaPeriodoTurmaDisciplinaAdicionarPorRequerimento(requerimentoVO, false, true, configFin, usuarioVO);	
		getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(requerimentoVO.getMatriculaPeriodoVO(), requerimentoVO.getMatricula(), mptd.getGradeDisciplinaVO(), new GradeDisciplinaCompostaVO(), mptd.getGradeCurricularGrupoOptativaDisciplinaVO(), false, mptd, 
				requerimentoVO.getHorarioAlunoTurnoVOs(), configGeral, configFin, new PeriodoLetivoComHistoricoAlunoVO(), true, usuarioVO, 
				requerimentoVO.getMatricula().getGradeCurricularAtual(), true, realizandoRecebimento);
		InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO = new InclusaoHistoricoForaPrazoVO();
		inclusaoHistoricoForaPrazoVO.setRequerimentoVO(requerimentoVO);
		getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().persistir(true, inclusaoHistoricoForaPrazoVO, requerimentoVO.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs(), requerimentoVO.getMatriculaPeriodoVO(), requerimentoVO.getMatricula(), "", "", new TextoPadraoVO(), configGeral, configFin, requerimentoVO, requerimentoVO.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO(), null, realizandoBaixaAutomatica, usuarioVO);
	}
	
	@Override
	public MatriculaPeriodoTurmaDisciplinaVO realizarPreenchimentoDadosMatriculaPeriodoTurmaDisciplinaAdicionarPorRequerimento(RequerimentoVO requerimentoVO, boolean validarChoqueHorario, boolean validarVagasReposicao, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuarioVO ) throws Exception {
		if(!requerimentoVO.getMatricula().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
			getFacadeFactory().getMatriculaFacade().carregarDados(requerimentoVO.getMatricula(), usuarioVO);
			requerimentoVO.getMatricula().setNivelMontarDados(NivelMontarDados.TODOS);
			getFacadeFactory().getPessoaFacade().carregarDados(requerimentoVO.getMatricula().getAluno(), NivelMontarDados.BASICO, usuarioVO);
		}
		if(!requerimentoVO.getMatricula().getMatriculaComHistoricoAlunoVO().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
			MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(requerimentoVO.getMatricula(), requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), false, requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico(), usuarioVO);
			requerimentoVO.getMatricula().setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
			requerimentoVO.getMatricula().getMatriculaComHistoricoAlunoVO().setNivelMontarDados(NivelMontarDados.TODOS);			
		}
		if(!requerimentoVO.getMatriculaPeriodoVO().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
			if(Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO())) {
				getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(requerimentoVO.getMatriculaPeriodoVO(), configFin, usuarioVO);
			}else {
				requerimentoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(requerimentoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configFin, usuarioVO));
			}
			requerimentoVO.getMatriculaPeriodoVO().setNivelMontarDados(NivelMontarDados.TODOS);
		}
		if(!requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS) && Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico())) {
			requerimentoVO.getMatricula().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico().getCodigo(), usuarioVO));
			requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico().setNivelMontarDados(NivelMontarDados.TODOS);
		}
		requerimentoVO.getMatriculaPeriodoVO().setReposicao(Boolean.TRUE);
		MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
		if(requerimentoVO.getDisciplinaPorEquivalencia()) {
			mptd.setDisciplinaPorEquivalencia(true);
			mptd.setMapaEquivalenciaDisciplinaCursada(requerimentoVO.getMapaEquivalenciaDisciplinaCursadaVO());
			mptd.setMapaEquivalenciaDisciplinaVOIncluir(requerimentoVO.getMapaEquivalenciaDisciplinaVO());
			mptd.setDisciplina(requerimentoVO.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO());
		}else {
			mptd.setDisciplina(requerimentoVO.getDisciplina());
		}
		mptd.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(requerimentoVO.getTurmaReposicao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		mptd.setAno(requerimentoVO.getMatriculaPeriodoVO().getAno());
		mptd.setSemestre(requerimentoVO.getMatriculaPeriodoVO().getSemestre());
		mptd.setSugestaoTurmaPraticaTeoricaRealizada(false);
		mptd.setInclusaoForaPrazo(true);
		mptd.setNrVagasDisponiveis(1);
		mptd.setMatricula(requerimentoVO.getMatricula().getMatricula());
		mptd.setMatriculaObjetoVO(requerimentoVO.getMatricula());
		mptd.setMatriculaPeriodoObjetoVO(requerimentoVO.getMatriculaPeriodoVO());
		mptd.setMatriculaPeriodo(requerimentoVO.getMatriculaPeriodoVO().getCodigo());
		mptd.setDataRegistroHistorico(new Date());		
		for(PeriodoLetivoVO periodoLetivoVO : requerimentoVO.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs()) {
			boolean selecionado = false;
			for(GradeDisciplinaVO gradeDisciplinaVO: periodoLetivoVO.getGradeDisciplinaVOs()) {
				if(gradeDisciplinaVO.getDisciplina().getCodigo().equals(requerimentoVO.getDisciplina().getCodigo())) {
					gradeDisciplinaVO.setSelecionado(true);
					mptd.setGradeDisciplinaVO(gradeDisciplinaVO);
					selecionado = true;	
					break;
				}else {
					gradeDisciplinaVO.setSelecionado(false);
				}
			}
			if(selecionado) {
				break;
			}
			if(Uteis.isAtributoPreenchido(periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo())) {				
				if(periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
					for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO:requerimentoVO.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs()) {
						if(periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo().equals(gradeCurricularGrupoOptativaVO.getCodigo())) {
							periodoLetivoVO.getGradeCurricularGrupoOptativa().setGradeCurricularGrupoOptativaDisciplinaVOs(gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs());
							break;
						}
					}
				}
				for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO: periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					if(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo().equals(requerimentoVO.getDisciplina().getCodigo())){
						selecionado = true;	
						gradeCurricularGrupoOptativaDisciplinaVO.setSelecionado(true);
						mptd.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO);
						mptd.setDisciplinaReferenteAUmGrupoOptativa(true);
					}
				}
			}
			if(selecionado) {
				break;
			}
		}
		
		TurmaDisciplinaVO turDis = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaDisciplina(mptd.getTurma().getCodigo(), mptd.getDisciplina().getCodigo(), false, usuarioVO);
		if (turDis.getCodigo().intValue() > 0) {
			turDis.setModalidadeDisciplina(turDis.getModalidadeDisciplina());				
		}
				
		mptd.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarDefinicaoConfiguracaoAcademicaVincularHistoricoAluno(requerimentoVO.getMatricula().getCurso(), 
				mptd.getGradeDisciplinaVO(), mptd.getGradeCurricularGrupoOptativaDisciplinaVO(), 
				mptd.getGradeDisciplinaCompostaVO(), mptd.getTurma(), mptd.getAno(), mptd.getSemestre(), usuarioVO));
		if (requerimentoVO.getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
			requerimentoVO.getMatriculaPeriodoVO().setMatriculaPeriodoTumaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestre(requerimentoVO.getMatricula().getMatricula(), 0, 0, mptd.getAno(), mptd.getSemestre(), false, false, false, false, false, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}		
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarSugestaoTurmaPraticaTeorica(requerimentoVO.getMatriculaPeriodoVO(), mptd, mptd.getDisciplina(), requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico(), requerimentoVO.getHorarioAlunoTurnoVOs(), true, usuarioVO);
		if (validarChoqueHorario) {
			if (!Uteis.isAtributoPreenchido(mptd.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mptd.getTurmaTeorica().getCodigo())) {
				if(getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(requerimentoVO.getMatriculaPeriodoVO(), requerimentoVO.getHorarioAlunoTurnoVOs(), requerimentoVO.getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), requerimentoVO.getTurmaReposicao().getCodigo(), mptd.getTurmaPratica().getCodigo(), mptd.getTurmaTeorica().getCodigo(), mptd.getDisciplina().getCodigo(), usuarioVO, !requerimentoVO.getTipoRequerimento().getPermitirReposicaoComChoqueHorario(), requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno())) {
					requerimentoVO.setMensagemChoqueHorario(requerimentoVO.getMatriculaPeriodoVO().getMensagemErro());
				}else {
					requerimentoVO.setMensagemChoqueHorario("");
				}
			}
		}
		return mptd;
	}
	
	public void preencherDadosInclusaoHistoricoForaPrazo(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaPeriodoVO matriculaPeriodoVO, String justificativa, String observacaoJustificativa, TextoPadraoVO textoPadraoContratoInclusao, RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		if (inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().getCodigo().equals(0)) {
			inclusaoHistoricoForaPrazoVO.setMatriculaPeriodoVO(matriculaPeriodoVO);
			inclusaoHistoricoForaPrazoVO.setReposicao(matriculaPeriodoVO.getReposicao());
			inclusaoHistoricoForaPrazoVO.setTextoPadraoContrato(textoPadraoContratoInclusao);
			inclusaoHistoricoForaPrazoVO.setJustificativa(justificativa);
			inclusaoHistoricoForaPrazoVO.setObservacao(observacaoJustificativa);
			inclusaoHistoricoForaPrazoVO.setPlanoFinanceiroReposicaoVO(matriculaPeriodoVO.getPlanoFinanceiroReposicaoVO());
			inclusaoHistoricoForaPrazoVO.setNrParcelas(matriculaPeriodoVO.getNumParcelasInclusaoForaPrazo());
			inclusaoHistoricoForaPrazoVO.setValorTotalParcela(matriculaPeriodoVO.getValorTotalParcelaInclusaoForaPrazo());
			inclusaoHistoricoForaPrazoVO.setDesconto(matriculaPeriodoVO.getDescontoReposicao());
			inclusaoHistoricoForaPrazoVO.setDataVencimento(matriculaPeriodoVO.getDiaVencimentoInclusaoForaPrazo());
			inclusaoHistoricoForaPrazoVO.setResponsavel(usuarioVO);
			inclusaoHistoricoForaPrazoVO.setDataInclusao(new Date());
			inclusaoHistoricoForaPrazoVO.setRequerimentoVO(requerimentoVO);
		}
	}

	public void realizarCriacaoInclusaoDisciplinaForaPrazo(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, PeriodoLetivoVO periodoLetivoVO, UsuarioVO usuarioVO) {
		InclusaoDisciplinasHistoricoForaPrazoVO inclusaoDisciplinasHistoricoForaPrazoVO = new InclusaoDisciplinasHistoricoForaPrazoVO();
		inclusaoDisciplinasHistoricoForaPrazoVO.setTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma());
		inclusaoDisciplinasHistoricoForaPrazoVO.setDisciplina(matriculaPeriodoTurmaDisciplinaVO.getDisciplina());
		inclusaoDisciplinasHistoricoForaPrazoVO.setSemestre(matriculaPeriodoTurmaDisciplinaVO.getSemestre());
		inclusaoDisciplinasHistoricoForaPrazoVO.setAno(matriculaPeriodoTurmaDisciplinaVO.getAno());
		inclusaoDisciplinasHistoricoForaPrazoVO.setUsuarioLibercaoChoqueHorario(matriculaPeriodoTurmaDisciplinaVO.getUsuarioLiberacaoChoqueHorario());
		inclusaoDisciplinasHistoricoForaPrazoVO.setDataUsuarioLibercaoChoqueHorario(new Date());
		if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaForaGrade()) {
			inclusaoDisciplinasHistoricoForaPrazoVO.setDisciplinaForaGrade(Boolean.TRUE);
			inclusaoDisciplinasHistoricoForaPrazoVO.getPeriodoLetivoDisciplinaIncluidaVO().setCodigo(periodoLetivoVO.getCodigo());
		}
		if (!matriculaPeriodoTurmaDisciplinaVO.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(0)) {
			inclusaoDisciplinasHistoricoForaPrazoVO.getMapaEquivalenciaDisciplinaCursada().setCodigo(matriculaPeriodoTurmaDisciplinaVO.getMapaEquivalenciaDisciplinaCursada().getCodigo());
		}
		if (matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
			inclusaoDisciplinasHistoricoForaPrazoVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
		}
		inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().add(inclusaoDisciplinasHistoricoForaPrazoVO);
	}
	
	public GradeCurricularGrupoOptativaDisciplinaVO realizarCriacaoGradeCurricularGrupoOptativaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, PeriodoLetivoVO periodoLetivoVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception {
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		gradeCurricularGrupoOptativaDisciplinaVO.setDisciplina(novaMatriculaPeriodoTurmaDisciplina.getDisciplina());
		gradeCurricularGrupoOptativaDisciplinaVO.setCargaHoraria(novaMatriculaPeriodoTurmaDisciplina.getCargaHorariaDisciplina());
		gradeCurricularGrupoOptativaDisciplinaVO.setNrCreditos(novaMatriculaPeriodoTurmaDisciplina.getCreditosDisciplina());
		gradeCurricularGrupoOptativaDisciplinaVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivoVO);
		gradeCurricularGrupoOptativaDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
		gradeCurricularGrupoOptativaDisciplinaVO.setSelecionado(true);
		gradeCurricularGrupoOptativaDisciplinaVO.setHistoricoAtualAluno(getFacadeFactory().getHistoricoFacade().gerarHistoricoVOComBaseMatriculaPeriodoTurmaDisciplina(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuarioVO));
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}
	
	public void adicionarGradeCurricularGrupoOptativaDisciplinaVOPeriodoLetivoCorrespondente(GradeCurricularVO gradeCurricularVO, Integer periodoLetivoCorrespondente, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOIncluir) {
		for (PeriodoLetivoVO periodoLetivoVO : gradeCurricularVO.getPeriodoLetivosVOs()) {
			int index = 0;
			if (periodoLetivoCorrespondente.equals(periodoLetivoVO.getPeriodoLetivo())) {
				Boolean localizouDisciplina = Boolean.FALSE;
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVOIncluir.getDisciplina().getCodigo())) {
						periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().set(index, gradeCurricularGrupoOptativaDisciplinaVOIncluir);
						localizouDisciplina = Boolean.TRUE;
						break;
					}
					index++;
				}
				if (!localizouDisciplina) {
					periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().add(gradeCurricularGrupoOptativaDisciplinaVOIncluir);
					break;
				}
				break;
			}
		}

	}

		public void adicionarDisciplinaGrupoOptativaEquivalente(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, UsuarioVO usuario) throws Exception {
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaIncluirVO = null;
		PeriodoLetivoVO periodoLetivoUtilizarVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorAnoSemestreMatriculaPeriodoEMatricula(matriculaVO.getMatricula(), matriculaPeriodoAdicionadaVO.getAno(), matriculaPeriodoAdicionadaVO.getSemestre(), matriculaVO.getCurso().getPeriodicidade(), usuario);
		if (periodoLetivoUtilizarVO == null) {
			gradeCurricularGrupoOptativaDisciplinaIncluirVO = realizarCriacaoGradeCurricularGrupoOptativaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), matriculaPeriodoAdicionadaVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			adicionarGradeCurricularGrupoOptativaDisciplinaVOPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getPeriodoLetivo(), gradeCurricularGrupoOptativaDisciplinaIncluirVO);		
		} else {
			gradeCurricularGrupoOptativaDisciplinaIncluirVO = realizarCriacaoGradeCurricularGrupoOptativaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina, periodoLetivoUtilizarVO, matriculaPeriodoAdicionadaVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			gradeCurricularGrupoOptativaDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaIncluirVO.getMatriculaPeriodoTurmaDisciplinaVO());
			HistoricoVO historicoMapaEquivalenciaDisciplinaCursada = (HistoricoVO) gradeCurricularGrupoOptativaDisciplinaIncluirVO.getHistoricoAtualAluno().clone();
			gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMapaEquivalenciaDisciplinaCursada().setHistorico(historicoMapaEquivalenciaDisciplinaCursada);
			gradeCurricularGrupoOptativaDisciplinaVO.setHistoricoAtualAluno(gradeCurricularGrupoOptativaDisciplinaIncluirVO.getHistoricoAtualAluno());
			gradeCurricularGrupoOptativaDisciplinaVO.setSelecionado(true);
			matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular().add(historicoMapaEquivalenciaDisciplinaCursada);
		}
		gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().setDisciplinaPorEquivalencia(Boolean.TRUE);
		gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().setHistoricoPorEquivalencia(true);
		gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().setHistoricoEquivalente(false);
		gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
		matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs().add(novaMatriculaPeriodoTurmaDisciplina);
		if (novaMatriculaPeriodoTurmaDisciplina.getDisciplinaComposta()) {
			adicionarDisciplinaGrupoOptativaComposta(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoAdicionadaVO, matriculaVO, gradeCurricularVO, gradeCurricularGrupoOptativaDisciplinaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, periodoLetivoComHistoricoAlunoVO, Boolean.TRUE, usuario);
		}
	}

	public void adicionarDisciplinaGrupoOptativaComposta(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoAdicionadaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, Boolean equivalenciaComposta, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs = gerarEAdicionarMatriculaPeriodoTurmaDisciplinaVOParaDisciplinasComposicao(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoAdicionadaVO, matriculaVO, usuario);
		realizarCriacaoGradeDisciplinaDisciplinaCompostaGrupoOptativa(listaMatriculaPeriodoTurmaDisciplinaVOs, gradeCurricularVO, gradeCurricularGrupoOptativaDisciplinaVO.getPeriodoLetivoDisciplinaReferenciada(), matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoAdicionadaVO, matriculaVO, gradeCurricularGrupoOptativaDisciplinaVO, configuracaoGeralSistemaVO, usuario);
		for(MatriculaPeriodoTurmaDisciplinaVO mptd: listaMatriculaPeriodoTurmaDisciplinaVOs){
			mptd.setDataRegistroHistorico(matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO.getDataRegistroHistorico());
			matriculaPeriodoAdicionadaVO.adicionarObjMatriculaPeriodoTurmaDisciplinaVOs(mptd);
		}
		if (!equivalenciaComposta) {
			gradeCurricularGrupoOptativaDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
			gradeCurricularGrupoOptativaDisciplinaVO.setSelecionado(Boolean.TRUE);
		}
	}
	
	public void realizarCriacaoGradeDisciplinaDisciplinaCompostaGrupoOptativa(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs, GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina : listaMatriculaPeriodoTurmaDisciplinaVOs) {
			inicializarDadosInclusaoForaPrazoParaMatriculaPeriodoTurmaDisciplina(novaMatriculaPeriodoTurmaDisciplina, matriculaPeriodoPeriodoTurmaDisciplinaTemporariaVO, matriculaPeriodoVO, matriculaVO, null, configuracaoGeralSistemaVO, usuarioVO);
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaIncluirVO = new GradeCurricularGrupoOptativaDisciplinaVO();
			gradeCurricularGrupoOptativaDisciplinaIncluirVO.setSelecionado(Boolean.TRUE);
			gradeCurricularGrupoOptativaDisciplinaIncluirVO.setDisciplina(novaMatriculaPeriodoTurmaDisciplina.getDisciplina());
			gradeCurricularGrupoOptativaDisciplinaIncluirVO.setCargaHoraria(novaMatriculaPeriodoTurmaDisciplina.getCargaHorariaDisciplina());
			gradeCurricularGrupoOptativaDisciplinaIncluirVO.setNrCreditos(novaMatriculaPeriodoTurmaDisciplina.getCreditosDisciplina());
			gradeCurricularGrupoOptativaDisciplinaIncluirVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivoVO);
			gradeCurricularGrupoOptativaDisciplinaIncluirVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
			removerGradeDisciplinaEquivalenteGrupoOptativa(gradeCurricularVO.getPeriodoLetivosVOs(), gradeCurricularGrupoOptativaDisciplinaIncluirVO);
			adicionarGradeCurricularGrupoOptativaDisciplinaVOPeriodoLetivoCorrespondente(gradeCurricularVO, periodoLetivoVO.getPeriodoLetivo(), gradeCurricularGrupoOptativaDisciplinaIncluirVO);
		}
	}
	
	public void removerGradeDisciplinaEquivalenteGrupoOptativa(List<PeriodoLetivoVO> listaPeriodoLetivoVOs, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaExcluirVO) {
		for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {
			int index = 0;
			Boolean localizouDisciplina = Boolean.FALSE;
			for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
				if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaExcluirVO.getDisciplina().getCodigo())) {
					periodoLetivoVO.getGradeDisciplinaVOs().remove(index);
					localizouDisciplina = Boolean.TRUE;
					break;
				}
				index++;
			}
			if (localizouDisciplina) {
				break;
			}
		}
	}
	
	private void executarGeracaoInclusaoHistoricoForaPrazo(boolean incluindaPeloPagamentoRequerimento, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, PeriodoLetivoVO periodoLetivoVO, UsuarioVO usuarioVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, MatriculaVO matriculaVO, String observacaoJustificativa, String justificativa) throws Exception {
		MatriculaPeriodoVO matriculaP = matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO();
		matriculaPeriodoTurmaDisciplinaVO.setJustificativa(justificativa);
		matriculaPeriodoTurmaDisciplinaVO.setObservacaoJustificativa(observacaoJustificativa);
		if (matriculaPeriodoVO.getReposicao()) {
			matriculaPeriodoTurmaDisciplinaVO.setReposicao(Boolean.TRUE);
		}
		if (!configuracaoGeralSistemaVO.getPermiteInclusaoForaPrazoMatriculaPeriodoAtiva()) {
			if (!matriculaVO.getCurso().getIntegral() && matriculaVO.getGradeCurricularAtual().getCodigo().equals(matriculaP.getGradeCurricular().getCodigo())) {
				if (matriculaP.getSituacaoMatriculaPeriodo().equals("AT") || matriculaP.getSituacaoMatriculaPeriodo().equals("PR")) {
					throw new Exception("Uma das matrículas Períodos encontra-se ativa, por isso a inclusão da disciplina deverá ser feita através da tela de matrícula.");
				}
			}
		}
		validarDadosReposicao(incluindaPeloPagamentoRequerimento, matriculaPeriodoVO, configuracaoFinanceiroVO);
		boolean encontrouDisciplina = false;
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			Iterator i = matriculaPeriodoTurmaDisciplinaVOs.iterator();
			while (i.hasNext()) {
				MatriculaPeriodoTurmaDisciplinaVO matPer = (MatriculaPeriodoTurmaDisciplinaVO)i.next();
				if (matPer.getDisciplina().getCodigo().intValue() == matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo().intValue() &&
						matPer.getAno().equals(matriculaPeriodoTurmaDisciplinaVO.getAno()) &&
						matPer.getSemestre().equals(matriculaPeriodoTurmaDisciplinaVO.getSemestre())) {
					encontrouDisciplina = true;
				}
			}
		}
		if (!encontrouDisciplina) {
			realizarCriacaoInclusaoDisciplinaForaPrazo(inclusaoHistoricoForaPrazoVO, matriculaPeriodoTurmaDisciplinaVO, periodoLetivoVO, usuarioVO);
			matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);
		}
	}
	
	private void executarGeracaoInclusaoHistoricoForaPrazoGradeDisciplina(boolean incluindaPeloPagamentoRequerimento, PeriodoLetivoVO periodoLetivoVO, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, List<PeriodoLetivoVO> listaPeriodoLetivoVOs, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, String justificativa, String observacaoJustificativa, TextoPadraoVO textoPadraoContratoInclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs) throws Exception {
		for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getDisciplinaComposta()) {
				for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
					if (!gradeDisciplinaCompostaVO.getSelecionado() || !Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo())) {
						continue;
					}
					executarGeracaoInclusaoHistoricoForaPrazo(incluindaPeloPagamentoRequerimento, gradeDisciplinaCompostaVO.getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, inclusaoHistoricoForaPrazoVO, periodoLetivoVO, usuarioVO, listaMatriculaPeriodoTurmaDisciplinaVOs, matriculaVO, observacaoJustificativa, justificativa);
				}
			}
			if (!gradeDisciplinaVO.getSelecionado()) {
				continue;
			}
			if (gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().isNovoObj()) {
				executarGeracaoInclusaoHistoricoForaPrazo(incluindaPeloPagamentoRequerimento, gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, inclusaoHistoricoForaPrazoVO, periodoLetivoVO, usuarioVO, listaMatriculaPeriodoTurmaDisciplinaVOs, matriculaVO, observacaoJustificativa, justificativa);
			}
		}
	}
	
	private void executarGeracaoInclusaoHistoricoForaPrazoGradeCurricularGrupoOptativaDisciplina(boolean incluindaPeloPagamentoRequerimento, PeriodoLetivoVO periodoLetivoVO, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, List<PeriodoLetivoVO> listaPeriodoLetivoVOs, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, String justificativa, String observacaoJustificativa, TextoPadraoVO textoPadraoContratoInclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs) throws Exception {
		for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
			if (!gradeCurricularGrupoOptativaDisciplinaVO.getSelecionado()) {
				continue;
			}
			if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaComposta()) {
				for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
					if (!gradeDisciplinaCompostaVO.getSelecionado()) {
						continue;
					}
					executarGeracaoInclusaoHistoricoForaPrazo(incluindaPeloPagamentoRequerimento, gradeDisciplinaCompostaVO.getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, inclusaoHistoricoForaPrazoVO, periodoLetivoVO, usuarioVO, listaMatriculaPeriodoTurmaDisciplinaVOs, matriculaVO, observacaoJustificativa,justificativa);
				}
			}
			if (gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().isNovoObj()) {
				executarGeracaoInclusaoHistoricoForaPrazo(incluindaPeloPagamentoRequerimento, gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, inclusaoHistoricoForaPrazoVO, periodoLetivoVO, usuarioVO, listaMatriculaPeriodoTurmaDisciplinaVOs, matriculaVO, observacaoJustificativa, justificativa);
			}
		}
	}
	
	private void adicionarMatriculaPeriodoTurmaDisciplinaFazParteComposicao(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO,Boolean liberarRealizarMatriculaDisciplinaPreRequisito, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina, MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, Boolean liberarChoqueHorario, UsuarioVO usuario, GradeCurricularVO gradeCurricularSelecionadaVO, Boolean considerarVagaReposicao) throws Exception{
		gradeDisciplinaCompostaVO.setMatriculaPeriodoTurmaDisciplinaVO(novaMatriculaPeriodoTurmaDisciplina);
		getFacadeFactory().getMatriculaPeriodoFacade().realizarSugestaoTurmaPraticaTeorica(matriculaPeriodoVO, matriculaVO.getCurso().getConfiguracaoAcademico(), horarioAlunoTurnoVOs, considerarVagaReposicao, usuario);
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(matriculaPeriodoVO, novaMatriculaPeriodoTurmaDisciplina,  novaMatriculaPeriodoTurmaDisciplina.getDisciplina(), novaMatriculaPeriodoTurmaDisciplina.getAno(), novaMatriculaPeriodoTurmaDisciplina.getSemestre(), true, considerarVagaReposicao);
		if(Uteis.isAtributoPreenchido(novaMatriculaPeriodoTurmaDisciplina.getTurmaPratica().getCodigo())){
			matricaPeriodoTurmaDisciplina.getTurmaPratica().setNrVagas(novaMatriculaPeriodoTurmaDisciplina.getTurmaPratica().getNrVagas());
			matricaPeriodoTurmaDisciplina.getTurmaPratica().setNrMaximoMatricula(novaMatriculaPeriodoTurmaDisciplina.getTurmaPratica().getNrMaximoMatricula());
			matricaPeriodoTurmaDisciplina.setNrAlunosMatriculadosTurmaPratica(novaMatriculaPeriodoTurmaDisciplina.getNrAlunosMatriculadosTurmaPratica());
			matricaPeriodoTurmaDisciplina.setNrVagasDisponiveisTurmaPratica(novaMatriculaPeriodoTurmaDisciplina.getNrVagasDisponiveisTurmaPratica());
			matricaPeriodoTurmaDisciplina.setTurmaPratica(novaMatriculaPeriodoTurmaDisciplina.getTurmaPratica());
		}
		if(Uteis.isAtributoPreenchido(novaMatriculaPeriodoTurmaDisciplina.getTurmaTeorica().getCodigo())){    			
			matricaPeriodoTurmaDisciplina.getTurmaTeorica().setNrVagas(novaMatriculaPeriodoTurmaDisciplina.getTurmaTeorica().getNrVagas());
			matricaPeriodoTurmaDisciplina.getTurmaTeorica().setNrMaximoMatricula(novaMatriculaPeriodoTurmaDisciplina.getTurmaTeorica().getNrMaximoMatricula());
			matricaPeriodoTurmaDisciplina.setNrAlunosMatriculadosTurmaTeorica(novaMatriculaPeriodoTurmaDisciplina.getNrAlunosMatriculadosTurmaTeorica());
			matricaPeriodoTurmaDisciplina.setNrVagasDisponiveisTurmaTeorica(novaMatriculaPeriodoTurmaDisciplina.getNrVagasDisponiveisTurmaTeorica());
			matricaPeriodoTurmaDisciplina.setTurmaTeorica(novaMatriculaPeriodoTurmaDisciplina.getTurmaTeorica());
		}
		matricaPeriodoTurmaDisciplina.getTurma().setNrVagas(novaMatriculaPeriodoTurmaDisciplina.getTurma().getNrVagas());
		matricaPeriodoTurmaDisciplina.getTurma().setNrMaximoMatricula(novaMatriculaPeriodoTurmaDisciplina.getTurma().getNrMaximoMatricula());
		matricaPeriodoTurmaDisciplina.setNrAlunosMatriculados(novaMatriculaPeriodoTurmaDisciplina.getNrAlunosMatriculados());
		matricaPeriodoTurmaDisciplina.setNrVagasDisponiveis(novaMatriculaPeriodoTurmaDisciplina.getNrVagasDisponiveis());
		realizarDefinicaoNumeroVagasDisciplinaCompostaPorEscolha(gradeDisciplinaVO);		
	}
	
	@Override
	public void realizarDefinicaoNumeroVagasDisciplinaCompostaPorEscolha(GradeDisciplinaVO gradeDisciplinaVO) throws Exception{
		if(Uteis.isAtributoPreenchido(gradeDisciplinaVO)){
			Integer nrVagasDisponiveis = null;
			Integer nrAlunosMatriculados = null;			
			for(GradeDisciplinaCompostaVO gdc: gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()){
					if(Uteis.isAtributoPreenchido(gdc.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo())){
					if(Uteis.isAtributoPreenchido(gdc.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo())){
						if(nrVagasDisponiveis == null || nrVagasDisponiveis > gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrVagasDisponiveisTurmaPratica()){
							nrVagasDisponiveis = gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrVagasDisponiveisTurmaPratica();
							nrAlunosMatriculados = gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrAlunosMatriculadosTurmaPratica();
						}
					}
					if(Uteis.isAtributoPreenchido(gdc.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo())){
						if(nrVagasDisponiveis == null || nrVagasDisponiveis > gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrVagasDisponiveisTurmaTeorica()){
							nrVagasDisponiveis = gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrVagasDisponiveisTurmaTeorica();
							nrAlunosMatriculados = gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrAlunosMatriculadosTurmaTeorica();
						}
					}
					if(nrVagasDisponiveis == null || (nrVagasDisponiveis >  gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrVagasDisponiveis() 
							&& !Uteis.isAtributoPreenchido(gdc.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo())
							&& !Uteis.isAtributoPreenchido(gdc.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo()))){
						nrVagasDisponiveis = gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrVagasDisponiveis();
						nrAlunosMatriculados = gdc.getMatriculaPeriodoTurmaDisciplinaVO().getNrAlunosMatriculados();
					}
				}
			}										
			gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().setNrVagasDisponiveis(nrVagasDisponiveis);
			gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().setNrAlunosMatriculados(nrAlunosMatriculados);						
			}
	}
		
	private void liberarValidacaoPreRequisitoDisciplinasJaIncluidas(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVOs)) {
			matriculaPeriodoTurmaDisciplinaVOs.stream().filter(Uteis::isAtributoPreenchido).forEach(mptdvo -> mptdvo.setLiberarInclusaoDisciplinaPreRequisito(true));
		}
	}
}
