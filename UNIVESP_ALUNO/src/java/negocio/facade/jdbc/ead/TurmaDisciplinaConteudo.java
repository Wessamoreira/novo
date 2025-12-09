package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.TurmaDisciplinaConteudoVO;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.TurmaDisciplinaConteudoFacade;

/**
 * @author Victor Hugo 08/01/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class TurmaDisciplinaConteudo extends ControleAcesso implements TurmaDisciplinaConteudoFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TurmaDisciplinaConteudo.idEntidade = idEntidade;
	}

	public TurmaDisciplinaConteudo() throws Exception {
		super();
		setIdEntidade("ConfiguracaoConteudoTurma");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(turmaDisciplinaConteudoVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO TurmaDisciplinaConteudo (" + "turma, disciplina, ano, semestre, conteudo, avaliacaoonline, usuario, datainclusao )" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			turmaDisciplinaConteudoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);

					sqlInserir.setInt(1, turmaDisciplinaConteudoVO.getTurmaVO().getCodigo());
					sqlInserir.setInt(2, turmaDisciplinaConteudoVO.getDisciplinaVO().getCodigo());
					sqlInserir.setString(3, turmaDisciplinaConteudoVO.getAno());
					sqlInserir.setString(4, turmaDisciplinaConteudoVO.getSemestre());
					sqlInserir.setInt(5, turmaDisciplinaConteudoVO.getConteudoVO().getCodigo());
					if(!turmaDisciplinaConteudoVO.getAvaliacaoOnlineVO().getCodigo().equals(0)) {
						sqlInserir.setInt(6, turmaDisciplinaConteudoVO.getAvaliacaoOnlineVO().getCodigo());						
					} else {
						sqlInserir.setNull(6, 0);
					}
					
					sqlInserir.setInt(7, usuarioVO.getCodigo());
					sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(new Date()));
				
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						turmaDisciplinaConteudoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getConexao().getJdbcTemplate().update("update matriculaperiodoturmadisciplina set conteudo = ? where conteudo is null and turma = ? and disciplina = ? and ano = ? and semestre = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), turmaDisciplinaConteudoVO.getConteudoVO().getCodigo(), turmaDisciplinaConteudoVO.getTurmaVO().getCodigo(), turmaDisciplinaConteudoVO.getDisciplinaVO().getCodigo(), turmaDisciplinaConteudoVO.getAno(), turmaDisciplinaConteudoVO.getSemestre());
		} catch (Exception e) {
			turmaDisciplinaConteudoVO.setNovoObj(Boolean.TRUE);
			turmaDisciplinaConteudoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO) throws Exception {
		if (turmaDisciplinaConteudoVO.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("Informe Uma Disciplina");
		}
		
		if(turmaDisciplinaConteudoVO.getTurmaVO().getAnual()) {
			if (turmaDisciplinaConteudoVO.getAno().equals("")) {
				throw new Exception("Informe o Ano");
			}			
		}
		
		if(turmaDisciplinaConteudoVO.getTurmaVO().getSemestral()) {
			if (turmaDisciplinaConteudoVO.getSemestre().equals("")) {
				throw new Exception("Informe o Semestre");
			}	
		}
		
		if(turmaDisciplinaConteudoVO.getConteudoVO().getCodigo().equals(0)) {
			throw new Exception("Informe um Conteúdo");
		}
		
		if (!turmaDisciplinaConteudoVO.getTurmaVO().getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE)) {
			if (turmaDisciplinaConteudoVO.getAvaliacaoOnlineVO().getCodigo().equals(0)) {
				throw new Exception("Informe uma Avaliação On-line");
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE TurmaDisciplinaConteudo" + " SET turma = ?, disciplina = ?, ano = ?, semestre = ?, conteudo = ?, avaliacaoonline = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setInt(1, turmaDisciplinaConteudoVO.getTurmaVO().getCodigo());
					sqlAlterar.setInt(2, turmaDisciplinaConteudoVO.getDisciplinaVO().getCodigo());
					sqlAlterar.setString(3, turmaDisciplinaConteudoVO.getAno());
					sqlAlterar.setString(4, turmaDisciplinaConteudoVO.getSemestre());
					sqlAlterar.setInt(5, turmaDisciplinaConteudoVO.getConteudoVO().getCodigo());
					if(!turmaDisciplinaConteudoVO.getAvaliacaoOnlineVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(6, turmaDisciplinaConteudoVO.getAvaliacaoOnlineVO().getCodigo());						
					} else {
						sqlAlterar.setNull(6, 0);
					}					
					sqlAlterar.setInt(7, turmaDisciplinaConteudoVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public TurmaDisciplinaConteudoVO consultarTurmaDisciplinConteudoPorConteudo(Integer conteudoSugerido, boolean b, UsuarioVO usuarioLogadoClone) throws Exception {			
					
			String sql = "SELECT * FROM TurmaDisciplinaConteudo WHERE conteudo = ?";
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, conteudoSugerido);
			if (rs.next()) {
				return (montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS , usuarioLogadoClone));
			}
			return new TurmaDisciplinaConteudoVO();		
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaDisciplinaConteudoUsuarioDataInclusao(final TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE TurmaDisciplinaConteudo" + " SET usuario = ?, dataInclusao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, usuarioVO.getCodigo());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));						
					sqlAlterar.setInt(3, turmaDisciplinaConteudoVO.getCodigo());				

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM turmaDisciplinaConteudo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, turmaDisciplinaConteudoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public TurmaDisciplinaConteudoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		TurmaDisciplinaConteudoVO obj = new TurmaDisciplinaConteudoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.getConteudoVO().setCodigo(tabelaResultado.getInt("conteudo"));
		obj.getAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("avaliacaoonline"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			return obj;
		}
		return obj;
	}

	@Override
	public List<TurmaDisciplinaConteudoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList<TurmaDisciplinaConteudoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public TurmaDisciplinaConteudoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM TurmaDisciplinaConteudo WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new TurmaDisciplinaConteudoVO();
	}

	@Override
	public List<TurmaDisciplinaConteudoVO> consultarConfiguracoesConteudoTurma(Integer codigoTurma, UsuarioVO usuarioLogado) throws Exception {	
		if(codigoTurma == 0) {
			throw new Exception("Informe Uma Turma");
		} 
		StringBuilder sqlStr = new StringBuilder();


		sqlStr.append(" select distinct disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, turma.codigo as codigoturma, turmadisciplinaconteudo.codigo,");
		sqlStr.append(" turmadisciplinaconteudo.ano as anoturmadisciplina , turmadisciplinaconteudo.semestre as semestreturmadisciplina, conteudo.codigo as codigoconteudo, conteudo.descricao as descricao,");
		sqlStr.append(" turmadisciplinaconteudo.usuario as usuariocodigo, turmadisciplinaconteudo.dataInclusao as dataInclusao , ");
		sqlStr.append(" avaliacaoonline.nome as nomeavaliacaoonline, avaliacaoonline.codigo as codigoavaliacaoonline  ,");
		sqlStr.append(" (select count(matriculaperiodoturmadisciplina.codigo) ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(sqlStr);
		sqlStr.append(" where ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		sqlStr.append(" ");
		sqlStr.append(" and (((matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre) and matriculaperiodoturmadisciplina.conteudo = turmadisciplinaconteudo.conteudo) ");
		sqlStr.append(" or ((matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) = (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)))");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) <= (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )) as qtdeAluno,");
		sqlStr.append(" ");
		sqlStr.append(" (select count(matriculaperiodoturmadisciplina.codigo) ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(sqlStr);
		sqlStr.append(" where ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		sqlStr.append(" and matriculaperiodoturmadisciplina.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" and (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) >= (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) <= (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo != turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) = (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )");
		sqlStr.append(" ) as qtdeAlunoConteudoCorreto,");
		sqlStr.append(" ");
		sqlStr.append(" (select array_to_string(array_agg(matriculaperiodoturmadisciplina.codigo), ',') ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(sqlStr);
		sqlStr.append(" where ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		sqlStr.append(" and matriculaperiodoturmadisciplina.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" and (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) >= (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) <= (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo != turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) = (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )");
		sqlStr.append(" ) as codigoMatPerTurmaDiscAlunoConteudoCorreto,");
		sqlStr.append(" ");
		sqlStr.append(" ");
		sqlStr.append(" (select count(matriculaperiodoturmadisciplina.codigo) ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(sqlStr);
		sqlStr.append(" where ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		sqlStr.append(" and ( (matriculaperiodoturmadisciplina.conteudo != turmadisciplinaconteudo.conteudo and (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) = (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)) ");
		sqlStr.append(" or (matriculaperiodoturmadisciplina.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" and (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) <= (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )");
		sqlStr.append(" and exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo != turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) = (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");		
		
		sqlStr.append(" )))");
		sqlStr.append(" ) as qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo, ");
		sqlStr.append(" ");
		sqlStr.append(" ");
		sqlStr.append(" (select array_to_string(array_agg(matriculaperiodoturmadisciplina.codigo), ',')");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(sqlStr);
		sqlStr.append(" where ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		sqlStr.append(" and ( (matriculaperiodoturmadisciplina.conteudo != turmadisciplinaconteudo.conteudo and (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) = (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)) ");
		sqlStr.append(" or (matriculaperiodoturmadisciplina.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" and (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre) >= (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" and not exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) <= (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )");
		sqlStr.append(" and exists (");
		sqlStr.append(" 	select tdc.codigo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = turmadisciplinaconteudo.turma");
		sqlStr.append(" 	and tdc.disciplina = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" 	and tdc.conteudo != turmadisciplinaconteudo.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > (turmadisciplinaconteudo.ano||turmadisciplinaconteudo.semestre)");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) = (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" )))");
		sqlStr.append(" ) as codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo ");
		
		
		sqlStr.append(" from turmadisciplinaconteudo");
		sqlStr.append(" inner join conteudo on conteudo.codigo = turmadisciplinaconteudo.conteudo");
		sqlStr.append(" inner join turma on turmadisciplinaconteudo.turma = turma.codigo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = turmadisciplinaconteudo.disciplina");
		sqlStr.append(" left join avaliacaoonline on avaliacaoonline.codigo = turmadisciplinaconteudo.avaliacaoonline");
		sqlStr.append(" where turma.codigo = ").append(codigoTurma);
		sqlStr.append(" order by anoturmadisciplina desc,");
		sqlStr.append(" semestreturmadisciplina desc");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		

		List<TurmaDisciplinaConteudoVO> lista = new ArrayList<TurmaDisciplinaConteudoVO>();
		TurmaDisciplinaConteudoVO obj = new TurmaDisciplinaConteudoVO();

		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigodisciplina"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("nomedisciplina"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("codigoturma"));
			obj.setAno(tabelaResultado.getString("anoturmadisciplina"));
			obj.setSemestre(tabelaResultado.getString("semestreturmadisciplina"));
			obj.getConteudoVO().setCodigo(tabelaResultado.getInt("codigoconteudo"));
			obj.getConteudoVO().setDescricao(tabelaResultado.getString("descricao"));
			obj.getAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("codigoavaliacaoonline"));
			obj.getAvaliacaoOnlineVO().setNome(tabelaResultado.getString("nomeavaliacaoonline"));
			obj.getUsuarioVO().setCodigo(tabelaResultado.getInt("usuariocodigo"));
			obj.setDataInclusao(tabelaResultado.getDate("dataInclusao"));
			obj.setQtdeAlunoConteudoCorreto(tabelaResultado.getString("qtdeAlunoConteudoCorreto"));
            obj.setCodigoMatPerTurmaDiscAlunoConteudoCorreto(tabelaResultado.getString("codigoMatPerTurmaDiscAlunoConteudoCorreto"));
            obj.setQtdeAlunoOutroAnoSemetreUsandoConteudoAntigo(tabelaResultado.getString("qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo"));
            obj.setCodigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo(tabelaResultado.getString("codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo"));
            obj.setQtdeAluno(tabelaResultado.getString("qtdeAluno"));
			lista.add(obj);
			obj = new TurmaDisciplinaConteudoVO();
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarConteudoDaDisciplinaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select conteudo from turmadisciplinaconteudo ");
		sqlStr.append(" inner join turma on	turmadisciplinaconteudo.turma = turma.codigo ");
		sqlStr.append(" where ((turma.codigo = ").append(codigoTurma).append(") ");
		sqlStr.append(" or (turma.turmaagrupada and turma.codigo in (select turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turma = ").append(codigoTurma).append("))) ");
		sqlStr.append(" and ((turmadisciplinaconteudo.disciplina = ").append(codigoDisciplina).append(") ");
		sqlStr.append(" or (turma.turmaagrupada and turmadisciplinaconteudo.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina);
		sqlStr.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina).append("))) ");
		sqlStr.append(" and turmadisciplinaconteudo.ano = '").append(ano).append("'");
		sqlStr.append(" and turmadisciplinaconteudo.semestre = '").append(semestre).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getInt("conteudo");
		}
		return 0;
	}

	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplina(String codigoMatPerTurmaDiscAlunoConteudoCorreto,UsuarioVO usuarioLogado) {
		List<MatriculaPeriodoTurmaDisciplinaVO> lista = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append(" select matricula.matricula as matricula , pessoa.nome as aluno,  matriculaperiodo.situacaomatriculaperiodo as situacaomatriculaperiodo, matriculaperiodo.data  as dataMatriculaPeriodo , matriculaperiodoturmadisciplina.ano  , matriculaperiodoturmadisciplina.semestre  , matriculaperiodoturmadisciplina.codigo , matriculaperiodoturmadisciplina.logAlteracao ,");
		sqlStr.append(" exists (select conteudoregistroacesso.codigo from conteudoregistroacesso where conteudoregistroacesso.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and conteudoregistroacesso.conteudo = matriculaperiodoturmadisciplina.conteudo limit 1) as jaAcessouConteudo,");
		sqlStr.append(" exists (select avaliacaoonlinematricula.codigo from avaliacaoonlinematricula ");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonlinematricula.avaliacaoonline = avaliacaoonline.codigo");
		sqlStr.append(" inner join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.avaliacaoonline = avaliacaoonline.codigo");
		sqlStr.append(" inner join conteudounidadepagina on conteudounidadepaginarecursoeducacional.conteudounidadepagina = conteudounidadepagina.codigo");
		sqlStr.append(" inner join unidadeconteudo on conteudounidadepagina.unidadeconteudo = unidadeconteudo.codigo");
		sqlStr.append(" where avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo limit 1");
		sqlStr.append(" ) as jaPossuiAvaliacaoOnline, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.conteudo as codigoConteudo,");
		sqlStr.append(" (");
		sqlStr.append(" 	select tdc.conteudo from turmadisciplinaconteudo tdc");
		sqlStr.append(" 	where tdc.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" 	and tdc.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" 	and tdc.conteudo != matriculaperiodoturmadisciplina.conteudo");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) > ('20182')");
		sqlStr.append(" 	and (tdc.ano||tdc.semestre) = (matriculaperiodoturmadisciplina.ano||matriculaperiodoturmadisciplina.semestre)");
		sqlStr.append(" 	limit 1");
		sqlStr.append(" ) as conteudoSugerido");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" inner join matricula on matriculaperiodo.matricula = matricula.matricula");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" ");
		sqlStr.append(" ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo in (");
		sqlStr.append(codigoMatPerTurmaDiscAlunoConteudoCorreto);		
		sqlStr.append(" )");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO  matriculaPeriodoTD = new MatriculaPeriodoTurmaDisciplinaVO();
		  
		    matriculaPeriodoTD.setMatricula(tabelaResultado.getString("matricula"));
		    matriculaPeriodoTD.getMatriculaPeriodoObjetoVO().setSituacaoMatriculaPeriodo(tabelaResultado.getString("situacaomatriculaperiodo"));
		    matriculaPeriodoTD.getMatriculaPeriodoObjetoVO().setData(tabelaResultado.getDate("dataMatriculaPeriodo"));
		    matriculaPeriodoTD.getMatriculaPeriodoObjetoVO().setAluno(tabelaResultado.getString("aluno"));
		    matriculaPeriodoTD.setAno(tabelaResultado.getString("ano"));		 
		    matriculaPeriodoTD.setSemestre(tabelaResultado.getString("semestre"));
		    matriculaPeriodoTD.setCodigo(tabelaResultado.getInt("codigo"));
		    matriculaPeriodoTD.getConteudo().setCodigo(tabelaResultado.getInt("codigoConteudo"));
		    matriculaPeriodoTD.setJaAcessouConteudo(tabelaResultado.getString("jaAcessouConteudo"));
		    matriculaPeriodoTD.setJaPossuiAvaliacaoOnline(tabelaResultado.getString("jaPossuiAvaliacaoOnline"));
		    matriculaPeriodoTD.setConteudoSugerido(tabelaResultado.getInt("conteudoSugerido"));	
		    matriculaPeriodoTD.setLogAlteracao(tabelaResultado.getString("logAlteracao"));
			lista.add(matriculaPeriodoTD);	
		}
		return lista;
	}

	@Override
	public String alterarALunoConteudoDisciplinaDiferente(List listaConsulta3, TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO ,String fecharModalAlunoConteudoDiferente , UsuarioVO usuariologado , Integer conteudoSugeridoAlteracao) throws Exception {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO = listaConsulta3;
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOSelecionada = new ArrayList<>();
			listaMatriculaPeriodoTurmaDisciplinaVO.stream().filter(a -> a.getSelecionarAlunoConteudoDiferente()).forEach(a-> listaMatriculaPeriodoTurmaDisciplinaVOSelecionada.add(a));
			if (listaMatriculaPeriodoTurmaDisciplinaVOSelecionada.isEmpty()) {
				throw new Exception("Necessario Selecionar Matricula Para Alteração.");
			}
			for (MatriculaPeriodoTurmaDisciplinaVO obj : listaMatriculaPeriodoTurmaDisciplinaVOSelecionada) {
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarConteudoMatriculaPeriodoTurmaDisciplinaComConteudo(conteudoSugeridoAlteracao,obj.getCodigo(), usuariologado);
			}
			return "PF('panelAlunosConteudoDiferente').hide()";
	}
}
