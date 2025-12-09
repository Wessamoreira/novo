package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.TransferenciaMatrizCurricularMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CidadeVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CidadeVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CidadeVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TransferenciaMatrizCurricularMatricula extends ControleAcesso implements TransferenciaMatrizCurricularMatriculaInterfaceFacade {

    protected static String idEntidade;

    public TransferenciaMatrizCurricularMatricula() throws Exception {
        super();
        setIdEntidade("TransferenciaMatrizCurricular");
    }

    public TransferenciaMatrizCurricularMatriculaVO novo() throws Exception {
        TransferenciaMatrizCurricularMatricula.incluir(getIdEntidade());
        TransferenciaMatrizCurricularMatriculaVO obj = new TransferenciaMatrizCurricularMatriculaVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(final TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCodigo().equals(0)) {
            incluir(obj, usuario);
        } else {
            alterar(obj, usuario);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            this.validarDados(obj);
            final String sql = "INSERT INTO transferenciamatrizcurricularmatricula(situacao, observacoes, resultadoprocessamento, "
                    + "dataprocessamento, matricula, matriculaperiodo, usuario, transferenciaMatrizCurricularAtual, nrAlertasCriticos) " 
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement sqlInserir = conn.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getSituacao());
                    sqlInserir.setString(2, obj.getObservacoes());
                    sqlInserir.setString(3, obj.getResultadoProcessamento());
                    sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
                    sqlInserir.setString(5, obj.getMatriculaVO().getMatricula());
                    sqlInserir.setInt(6, obj.getMatriculaPeriodoUltimoPeriodoVO().getCodigo());
                    if (!obj.getResponsavel().getCodigo().equals(0)) {
                        sqlInserir.setInt(7, obj.getResponsavel().getCodigo());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setInt(8, obj.getTransferenciaMatrizCurricularVO().getCodigo());
                    sqlInserir.setInt(9, obj.getNrAlertasCriticos());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet res) throws SQLException, DataAccessException {
                    if (res.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return res.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            this.validarDados(obj);
            final String sql = "UPDATE transferenciamatrizcurricularmatricula SET situacao=?, observacoes=?, resultadoprocessamento=?, "
                    + "dataprocessamento=?, matricula=?, matriculaperiodo=?, usuario=?, transferenciaMatrizCurricularAtual=?, nrAlertasCriticos=? " 
                    + "WHERE ((codigo = ? ))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement sqlAlterar = conn.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacao());
                    sqlAlterar.setString(2, obj.getObservacoes());
                    sqlAlterar.setString(3, obj.getResultadoProcessamento());
                    sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
                    sqlAlterar.setString(5, obj.getMatriculaVO().getMatricula());
                    sqlAlterar.setInt(6, obj.getMatriculaPeriodoUltimoPeriodoVO().getCodigo());
                    sqlAlterar.setInt(7, obj.getResponsavel().getCodigo());
                    sqlAlterar.setInt(8, obj.getTransferenciaMatrizCurricularVO().getCodigo());
                    sqlAlterar.setInt(9, obj.getNrAlertasCriticos());
                    sqlAlterar.setInt(10, obj.getCodigo());
                    return sqlAlterar;
                }
            });
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            TransferenciaMatrizCurricularMatricula.excluir(getIdEntidade());
            String sql = "DELETE FROM transferenciamatrizcurricularmatricula WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    private StringBuilder getSQLPadraoConsultaBasica() {
        StringBuilder str = new StringBuilder();
        str.append("SELECT TransferenciaMatrizCurricularMatricula.*,  ");
        str.append("       Matricula.matricula as \"Matricula.matricula\", Matricula.gradeCurricularAtual as \"Matricula.gradeCurricularAtual\", ");
        str.append("       Matricula.situacao as \"Matricula.situacao\",  ");
        str.append("       Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", ");
        str.append("       MatriculaPeriodo.codigo as \"MatriculaPeriodo.codigo\", MatriculaPeriodo.ano as \"MatriculaPeriodo.ano\", MatriculaPeriodo.semestre as \"MatriculaPeriodo.semestre\", ");
        str.append("       PeriodoLetivo.codigo as \"PeriodoLetivo.codigo\", PeriodoLetivo.descricao as \"PeriodoLetivo.descricao\", PeriodoLetivo.periodoLetivo as \"PeriodoLetivo.periodoLetivo\", ");
        str.append("       Usuario.codigo as \"Usuario.codigo\", Usuario.nome as \"Usuario.nome\", ");
        
        str.append("       (SELECT COUNT(matriculaperiodoturmadisciplina.codigo) FROM matriculaperiodoturmadisciplina "); 
        str.append("         INNER JOIN matriculaperiodo as mp ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        str.append("         INNER JOIN historico ON historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        str.append("         WHERE (matriculaperiodoturmadisciplina.disciplinaCursandoPorCorrespondenciaAposTransferencia = true) ");
        str.append("               and (matriculaperiodoturmadisciplina.MatriculaPeriodo = matriculaPeriodo.codigo) ");
        str.append("               and (historico.situacao = 'CS') "); 
        str.append("               and (mp.matricula = Matricula.matricula) ");
        str.append("               and (mp.codigo = MatriculaPeriodo.codigo)) as nrDisciplinasCursandoPorCorrespondencia, ");
        
        str.append("       (SELECT DISTINCT COUNT(DISTINCT matriculaperiodoturmadisciplina.codigo) FROM matriculaperiodoturmadisciplina ");
        str.append("         INNER JOIN matriculaperiodo as mp ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        str.append("         INNER JOIN historico ON historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        str.append("         WHERE (matriculaperiodoturmadisciplina.disciplinaequivale) ");
        str.append("         and (matriculaperiodoturmadisciplina.MatriculaPeriodo = matriculaPeriodo.codigo) ");
        str.append("         and (historico.situacao = 'CS') ");
        str.append("         and (mp.matricula = Matricula.matricula) ");
        str.append("         and (mp.codigo = MatriculaPeriodo.codigo)) as nrDisciplinasCursandoPorEquivalencia  ");
        
        str.append(" FROM TransferenciaMatrizCurricularMatricula ");
        str.append("      LEFT JOIN Matricula ON (Matricula.matricula = TransferenciaMatrizCurricularMatricula.matricula) ");
        str.append("      LEFT JOIN Pessoa ON (Matricula.aluno = Pessoa.codigo) ");
        str.append("      LEFT JOIN MatriculaPeriodo ON (MatriculaPeriodo.codigo = TransferenciaMatrizCurricularMatricula.matriculaperiodo) ");
        str.append("      LEFT JOIN PeriodoLetivo ON (PeriodoLetivo.codigo = MatriculaPeriodo.periodoletivomatricula) ");
        str.append("      LEFT JOIN Usuario ON (Usuario.codigo = TransferenciaMatrizCurricularMatricula.usuario) ");
        return str;
    }

    @Override
    public List<TransferenciaMatrizCurricularMatriculaVO> consultaRapidaPorMatricula(String matriculaPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE matricula= '").append(matriculaPrm).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    @Override
    public List<TransferenciaMatrizCurricularMatriculaVO> consultaRapidaPorTransferenciaMatrizCurricular(Integer codigoTransferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE (transferenciaMatrizCurricularAtual = ").append(codigoTransferencia).append(") ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<TransferenciaMatrizCurricularMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<TransferenciaMatrizCurricularMatriculaVO> vetResultado = new ArrayList<TransferenciaMatrizCurricularMatriculaVO>(0);
        while (tabelaResultado.next()) {
            TransferenciaMatrizCurricularMatriculaVO obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    @SuppressWarnings("unchecked")
    private TransferenciaMatrizCurricularMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TransferenciaMatrizCurricularMatriculaVO obj = new TransferenciaMatrizCurricularMatriculaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setObservacoes(dadosSQL.getString("observacoes"));
        obj.setResultadoProcessamento(dadosSQL.getString("resultadoprocessamento"));
        obj.setDataProcessamento(dadosSQL.getTimestamp("dataprocessamento"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario"));
        obj.setNrDisciplinasCursandoPorCorrespondencia(dadosSQL.getInt("nrDisciplinasCursandoPorCorrespondencia"));
        obj.setNrDisciplinasCursandoPorEquivalencia(dadosSQL.getInt("nrDisciplinasCursandoPorEquivalencia"));
        
        obj.getMatriculaVO().setMatricula(dadosSQL.getString(("matricula")));
        obj.getMatriculaVO().setSituacao(dadosSQL.getString("Matricula.situacao"));
        obj.getMatriculaVO().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("Matricula.gradeCurricularAtual"));
        obj.getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
        obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString(("Pessoa.nome")));
        obj.getMatriculaPeriodoUltimoPeriodoVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.codigo"));
        obj.getMatriculaPeriodoUltimoPeriodoVO().setAno(dadosSQL.getString("MatriculaPeriodo.ano"));
        obj.getMatriculaPeriodoUltimoPeriodoVO().setSemestre(dadosSQL.getString("MatriculaPeriodo.semestre"));
        obj.getMatriculaPeriodoUltimoPeriodoVO().getPeriodoLetivo().setCodigo(dadosSQL.getInt("PeriodoLetivo.codigo"));
        obj.getMatriculaPeriodoUltimoPeriodoVO().getPeriodoLetivo().setDescricao(dadosSQL.getString("PeriodoLetivo.descricao"));
        obj.getMatriculaPeriodoUltimoPeriodoVO().getPeriodoLetivo().setPeriodoLetivo(dadosSQL.getInt("PeriodoLetivo.periodoLetivo"));
        return obj;
    }
	
    public TransferenciaMatrizCurricularMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (transferenciaMatrizCurricularMatricula.codigo = ").append(codigoPrm).append(") ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public static String getIdEntidade() {
        return TransferenciaMatrizCurricularMatricula.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        TransferenciaMatrizCurricularMatricula.idEntidade = idEntidade;
    }
    
    public void persistirTransferenciaMatrizCurricularMatriculaVOs(TransferenciaMatrizCurricularVO transferencia, List<TransferenciaMatrizCurricularMatriculaVO> listaTransferenciaMatrizCurricularMatriculaVOs, UsuarioVO usuario) throws Exception {
        for (TransferenciaMatrizCurricularMatriculaVO transferenciaMatricula : listaTransferenciaMatrizCurricularMatriculaVOs) {
            transferenciaMatricula.setTransferenciaMatrizCurricularVO(transferencia);
            transferenciaMatricula.setResponsavel(usuario);
            persistir(transferenciaMatricula, usuario);
        }
    }

    public void validarDados(TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO) throws Exception {
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMatriculaPeriodo(Integer matriculaPeriodo, String periodoMatricula , String matricula, UsuarioVO usuario) throws Exception {
		TransferenciaMatrizCurricularMatricula.excluir(getIdEntidade());
		try {
			String sql = "DELETE FROM transferenciamatrizcurricularmatricula WHERE ((matriculaPeriodo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaPeriodo });
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_historico_transferenciamatrizcurricularmatricula")) {
				throw new ConsistirException("No período " + periodoMatricula + " já existe uuma transferência de matriz para a matrícula " +matricula+ " . Deve-se cancelar a transferência depara prosseguimento da exclusão.");
			}
			throw e;
		}
	}
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorMatricula(String matricula, UsuarioVO usuario) throws Exception {
    	TransferenciaMatrizCurricularMatricula.excluir(getIdEntidade());
    	String sql = "DELETE FROM transferenciamatrizcurricularmatricula WHERE ((matricula = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    	getConexao().getJdbcTemplate().update(sql, new Object[] { matricula });
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final Integer codigo, final String situacao, final String alertas, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE transferenciamatrizcurricularmatricula set situacao=?, resultadoprocessamento=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, situacao);
					sqlAlterar.setString(++i, alertas);
					sqlAlterar.setInt(++i, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
    
	public boolean executarBloqueioTransferenciaMatrizCurricularMatriculaValidandoGradeMigrarGradeDestino(int matriculaPeriodo, int gradeMigrar, int gradeOrigem) throws Exception {
		StringBuilder str = new StringBuilder(" SELECT transferenciamatrizcurricularmatricula.codigo FROM transferenciamatrizcurricularmatricula INNER JOIN transferenciamatrizcurricularatual ON ")
				.append(" transferenciamatrizcurricularmatricula.transferenciamatrizcurricularatual = transferenciamatrizcurricularatual.codigo ")
				.append(" WHERE transferenciamatrizcurricularmatricula.matriculaPeriodo = ")
				.append(matriculaPeriodo)
				.append(" AND transferenciamatrizcurricularatual.grademigrar = ")
				.append(gradeOrigem)
				.append(" AND transferenciamatrizcurricularatual.gradeorigem = ")
				.append(gradeMigrar)
				.append(" AND transferenciamatrizcurricularmatricula.codigo = (")
				.append(" SELECT codigo FROM transferenciamatrizcurricularmatricula tmcm WHERE tmcm.matriculaperiodo = transferenciamatrizcurricularmatricula.matriculaPeriodo ")
				.append(" AND tmcm.situacao = 'RE' ORDER BY tmcm.dataprocessamento DESC LIMIT 1) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return tabelaResultado.next() && Uteis.isAtributoPreenchido(tabelaResultado.getInt("codigo"));
	}
}
