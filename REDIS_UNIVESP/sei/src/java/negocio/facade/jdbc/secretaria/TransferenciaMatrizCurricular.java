package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ObservacaoComplementarHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoRegraCargaHorariaEquivalenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.HistoricoGradeMigradaEquivalenteVO;
import negocio.comuns.secretaria.HistoricoGradeVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.TransferenciaMatrizCurricularInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TransferenciaMatrizCurricular extends ControleAcesso implements TransferenciaMatrizCurricularInterfaceFacade {

    protected static String idEntidade;
    private static final long serialVersionUID = 1L;

    public TransferenciaMatrizCurricular() {
        super();
        setIdEntidade("TransferenciaMatrizCurricular");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TransferenciaMatrizCurricularVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 30/10/2014
        	  *  Classe Subordinada
        	  */
	    	// TransferenciaMatrizCurricularMatricula.incluir(getIdEntidade());
	        validarDados(obj);
	        // Foi utilizada uma nova tabela denominada TransferenciaMatrizCurricularAtual, pois a tabela TransferenciaMatrizCurricular
	        // criada inicialmente tinha uma estrutura de dados muito diferente e continha dados que não podiam ser transferidos para esta tabela
	        final String sql = "INSERT INTO TransferenciaMatrizCurricularAtual( "
	                + "unidadeEnsino, unidadeEnsinoCurso, gradeOrigem, gradeMigrar, periodoLetivoInicial, periodoLetivoFinal, observacoes, "
	                + "resultadoProcessamento, responsavel, mapaEquivalenciaUtilizadoGradeMigrar, gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando, "
	                + "dataTransferencia, realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento, utilizarAnoSemestreAtualDisciplinaAprovada, "
	                + "anoDisciplinaAprovada, semestreDisciplinaAprovada, observacaoHistorico  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 1;
					sqlInserir.setInt(i++, obj.getUnidadeEnsino().getCodigo());
					sqlInserir.setInt(i++, obj.getUnidadeEnsinoCurso().getCodigo());
					sqlInserir.setInt(i++, obj.getGradeOrigem().getCodigo());
					sqlInserir.setInt(i++, obj.getGradeMigrar().getCodigo());
					if (obj.getPeriodoLetivoInicial().getCodigo() != 0) {
						sqlInserir.setInt(i++, obj.getPeriodoLetivoInicial().getCodigo());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					if (obj.getPeriodoLetivoFinal().getCodigo() != 0) {
						sqlInserir.setInt(i++, obj.getPeriodoLetivoFinal().getCodigo());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setString(i++, obj.getObservacoes());
					sqlInserir.setString(i++, obj.getResultadoProcessamento());
					if (obj.getResponsavel().getCodigo() != 0) {
						sqlInserir.setInt(i++, obj.getResponsavel().getCodigo());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					if (obj.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo() != 0) {
						sqlInserir.setInt(i++, obj.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setBoolean(i++, obj.getGerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando());
					sqlInserir.setDate(i++, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setBoolean(i++, obj.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
					sqlInserir.setBoolean(i++, obj.getUtilizarAnoSemestreAtualDisciplinaAprovada());
					if (obj.getUtilizarAnoSemestreAtualDisciplinaAprovada()) {
						sqlInserir.setString(i++, obj.getAnoDisciplinaAprovada());
						sqlInserir.setString(i++, obj.getSemestreDisciplinaAprovada());
					} else {
						sqlInserir.setNull(i++, 0);
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setString(i++, obj.getObservacaoHistorico());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistirTransferenciaMatrizCurricularMatriculaVOs(obj, obj.getListaTransferenciaMatrizCurricularMatricula(), usuario);
			obj.setNovoObj(Boolean.FALSE);
        } catch(Exception e) {
        	obj.setNovoObj(true);
        	obj.setCodigo(0);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TransferenciaMatrizCurricularVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 30/10/2014
        	  *  Classe Subordinada
        	  */
            // TransferenciaMatrizCurricularMatricula.alterar(getIdEntidade());
            this.validarDados(obj);
            final String sql = "UPDATE TransferenciaMatrizCurricularAtual SET "
                    + "unidadeEnsino=?, unidadeEnsinoCurso=?, gradeOrigem=?, gradeMigrar=?, periodoLetivoInicial=?, periodoLetivoFinal=?, observacoes=?, "
                    + "resultadoProcessamento=?, responsavel=?, mapaEquivalenciaUtilizadoGradeMigrar=?, gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando=?, "
                    + "dataTransferencia=?, realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento=?, utilizarAnoSemestreAtualDisciplinaAprovada=?, anoDisciplinaAprovada=?, semestreDisciplinaAprovada=?, observacaoHistorico=?  "
                    + "WHERE ((codigo = ? ))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement sqlAlterar = conn.prepareStatement(sql);
                    int i = 1;
                    sqlAlterar.setInt(i++, obj.getUnidadeEnsino().getCodigo());
                    sqlAlterar.setInt(i++, obj.getUnidadeEnsinoCurso().getCodigo());
                    sqlAlterar.setInt(i++, obj.getGradeOrigem().getCodigo());
                    sqlAlterar.setInt(i++, obj.getGradeMigrar().getCodigo());
                    if (obj.getPeriodoLetivoInicial().getCodigo() != 0) {
                        sqlAlterar.setInt(i++, obj.getPeriodoLetivoInicial().getCodigo());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    if (obj.getPeriodoLetivoFinal().getCodigo() != 0) {
                        sqlAlterar.setInt(i++, obj.getPeriodoLetivoFinal().getCodigo());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setString(i++, obj.getObservacoes());
                    sqlAlterar.setString(i++, obj.getResultadoProcessamento());
                    if (obj.getResponsavel().getCodigo() != 0) {
                        sqlAlterar.setInt(i++, obj.getResponsavel().getCodigo());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    if (obj.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo() != 0) {
                        sqlAlterar.setInt(i++, obj.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setBoolean(i++, obj.getGerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando());
                    sqlAlterar.setDate(i++, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setBoolean(i++, obj.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarAnoSemestreAtualDisciplinaAprovada());
					if (obj.getUtilizarAnoSemestreAtualDisciplinaAprovada()) {
						sqlAlterar.setString(i++, obj.getAnoDisciplinaAprovada());
						sqlAlterar.setString(i++, obj.getSemestreDisciplinaAprovada());
					} else {
						sqlAlterar.setNull(i++, 0);
						sqlAlterar.setNull(i++, 0);
					}
					sqlAlterar.setString(i++, obj.getObservacaoHistorico());
                    sqlAlterar.setInt(i++, obj.getCodigo());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistirTransferenciaMatrizCurricularMatriculaVOs(obj, obj.getListaTransferenciaMatrizCurricularMatricula(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 30/10/2014
        	  *  Classe Subordinada
        	  */
            // TransferenciaMatrizCurricularMatricula.excluir(getIdEntidade());
            String sql = "DELETE FROM TransferenciaMatrizCurricularAtual WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }    
    
    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT TransferenciaMatrizCurricularAtual.*,  ");
        str.append("       GradeOrigem.codigo as \"GradeOrigem.codigo\", GradeOrigem.nome as \"GradeOrigem.nome\", ");
        str.append("       GradeMigrar.codigo as \"GradeMigrar.codigo\", GradeMigrar.nome as \"GradeMigrar.nome\", ");
        str.append("       UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", ");
        str.append("       UnidadeEnsinoCurso.codigo as \"UnidadeEnsinoCurso.codigo\",  ");
        str.append("       Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.periodicidade as \"Curso.periodicidade\", ");
        str.append("       Turno.nome as \"Turno.nome\", Turno.codigo as \"Turno.codigo\", ");
        str.append("       PeriodoLetivoInicial.codigo as \"PeriodoLetivoInicial.codigo\", PeriodoLetivoInicial.descricao as \"PeriodoLetivoInicial.descricao\", ");
        str.append("       PeriodoLetivoFinal.codigo as \"PeriodoLetivoFinal.codigo\", PeriodoLetivoFinal.descricao as \"PeriodoLetivoFinal.descricao\", ");
        str.append("       MapaEquivalenciaMatrizCurricular.codigo as \"MapaEquivalenciaMatrizCurricular.codigo\", ");
        str.append("       Usuario.codigo as \"Usuario.codigo\", Usuario.nome as \"Usuario.nome\" ");
        str.append(" FROM TransferenciaMatrizCurricularAtual ");
        str.append("      LEFT JOIN GradeCurricular as GradeOrigem ON (TransferenciaMatrizCurricularAtual.gradeOrigem = GradeOrigem.codigo) ");
        str.append("      LEFT JOIN GradeCurricular as GradeMigrar ON (TransferenciaMatrizCurricularAtual.gradeMigrar = GradeMigrar.codigo) ");
        str.append("      LEFT JOIN UnidadeEnsino ON (TransferenciaMatrizCurricularAtual.unidadeEnsino = UnidadeEnsino.codigo) ");
        str.append("      LEFT JOIN UnidadeEnsinoCurso ON (unidadeEnsinoCurso.codigo = TransferenciaMatrizCurricularAtual.unidadeEnsinoCurso)");
        str.append("      LEFT JOIN Curso ON (unidadeEnsinoCurso.curso = Curso.codigo)");
        str.append("      LEFT JOIN Turno ON (unidadeEnsinoCurso.turno = Turno.codigo)");
        str.append("      LEFT JOIN PeriodoLetivo PeriodoLetivoInicial ON (PeriodoLetivoInicial.codigo = TransferenciaMatrizCurricularAtual.periodoLetivoInicial)");
        str.append("      LEFT JOIN PeriodoLetivo PeriodoLetivoFinal ON (PeriodoLetivoFinal.codigo = TransferenciaMatrizCurricularAtual.periodoLetivoFinal)");
        str.append("      LEFT JOIN MapaEquivalenciaMatrizCurricular ON (MapaEquivalenciaMatrizCurricular.codigo = TransferenciaMatrizCurricularAtual.mapaEquivalenciaUtilizadoGradeMigrar)");
        str.append("      LEFT JOIN Usuario ON (Usuario.codigo = TransferenciaMatrizCurricularAtual.responsavel) ");
        return str;
    }
    
    public void carregarDados(TransferenciaMatrizCurricularVO obj, NivelMontarDados nivelMontar, UsuarioVO usuario) throws Exception {
        obj = consultarPorChavePrimaria(obj.getCodigo(), false, nivelMontar, usuario);
    }

    public TransferenciaMatrizCurricularVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, NivelMontarDados nivelMontar, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (TransferenciaMatrizCurricularAtual.codigo = ").append(codigoPrm).append(") ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontar, usuario));
    }
    
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorCodigoCurso(Integer codigoCurso, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE (Curso.codigo = ").append(codigoCurso).append(") ");
        sqlStr.append(" ORDER BY Curso.codigo, dataTransferencia ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorCodigoTransferencia(Integer codigoTransferencia, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE (TransferenciaMatrizCurricularAtual.codigo = ").append(codigoTransferencia).append(") ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorNomeCurso(String nomeCurso, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE (upper(sem_acentos(Curso.nome)) ilike (upper(sem_acentos('").append(nomeCurso.toUpperCase()).append("%')))) ");
        sqlStr.append(" ORDER BY Curso.nome, transferenciaMatrizCurricularAtual.codigo desc, transferenciaMatrizCurricularAtual.dataTransferencia desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE (TransferenciaMatrizCurricularAtual.codigo IN (select TransferenciaMatrizCurricularAtual from TransferenciaMatrizCurricularMatricula where matricula = '").append(matricula).append("'))");
        sqlStr.append(" ORDER BY Curso.nome, transferenciaMatrizCurricularAtual.codigo desc, transferenciaMatrizCurricularAtual.dataTransferencia desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorNomeAluno(String nomeAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (TransferenciaMatrizCurricularAtual.codigo IN ( ");
        sqlStr.append(" select TransferenciaMatrizCurricularAtual from TransferenciaMatrizCurricularMatricula ");
        sqlStr.append(" inner join matricula on matricula.matricula = TransferenciaMatrizCurricularMatricula.matricula ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" WHERE upper(sem_acentos(pessoa.nome)) ilike (upper(sem_acentos('").append(nomeAluno.toUpperCase()).append("%'))))) ");
        sqlStr.append(" ORDER BY Curso.nome, transferenciaMatrizCurricularAtual.codigo desc, transferenciaMatrizCurricularAtual.dataTransferencia desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    
    @Override
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorRegistroAcademicoAluno(String rgistroAcademico, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuffer sqlStr = getSQLPadraoConsultaBasica();
    	sqlStr.append(" WHERE (TransferenciaMatrizCurricularAtual.codigo IN ( ");
    	sqlStr.append(" select TransferenciaMatrizCurricularAtual from TransferenciaMatrizCurricularMatricula ");
    	sqlStr.append(" inner join matricula on matricula.matricula = TransferenciaMatrizCurricularMatricula.matricula ");
    	sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
    	sqlStr.append(" WHERE upper(sem_acentos(pessoa.registroAcademico)) ilike (upper(sem_acentos('").append(rgistroAcademico.toUpperCase()).append("%'))))) ");
    	sqlStr.append(" ORDER BY Curso.nome, transferenciaMatrizCurricularAtual.codigo desc, transferenciaMatrizCurricularAtual.dataTransferencia desc ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List<TransferenciaMatrizCurricularVO> consultaRapidaPorDataTransferencia(Date dataTransferencia, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (dataTransferencia between '").append(Uteis.getDataJDBC(dataTransferencia)).append("' AND '").append(Uteis.getDataJDBC(dataTransferencia)).append("')");
        sqlStr.append(" ORDER BY dataTransferencia, curso.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<TransferenciaMatrizCurricularVO> montarDadosConsulta(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<TransferenciaMatrizCurricularVO> vetResultado = new ArrayList<TransferenciaMatrizCurricularVO>(0);
        while (tabelaResultado.next()) {
            TransferenciaMatrizCurricularVO obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }
    
    private TransferenciaMatrizCurricularVO montarDados(SqlRowSet dadosSQL, NivelMontarDados nivelMontar, UsuarioVO usuario) throws Exception {
        TransferenciaMatrizCurricularVO obj = new TransferenciaMatrizCurricularVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        //gradeOrigem
        obj.getGradeOrigem().setCodigo(dadosSQL.getInt("GradeOrigem.codigo"));
        obj.getGradeOrigem().setNome(dadosSQL.getString("GradeOrigem.nome"));
        //gradeMigrar
        obj.getGradeMigrar().setCodigo(dadosSQL.getInt("GradeMigrar.codigo"));
        obj.getGradeMigrar().setNome(dadosSQL.getString("GradeMigrar.nome"));
        //unidadeEnsino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
        //unidadeEnsinoCurso
        obj.getUnidadeEnsinoCurso().setCodigo(dadosSQL.getInt("UnidadeEnsinoCurso.codigo"));
        obj.getUnidadeEnsinoCurso().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
        obj.getUnidadeEnsinoCurso().getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
        obj.getUnidadeEnsinoCurso().getCurso().setNome(dadosSQL.getString("Curso.nome"));
        obj.getUnidadeEnsinoCurso().getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
        obj.getUnidadeEnsinoCurso().getTurno().setNome(dadosSQL.getString("Turno.nome"));
        // periodosLetivos
        obj.getPeriodoLetivoInicial().setCodigo(dadosSQL.getInt("PeriodoLetivoInicial.codigo"));
        obj.getPeriodoLetivoInicial().setDescricao(dadosSQL.getString("PeriodoLetivoInicial.descricao"));
        obj.getPeriodoLetivoFinal().setCodigo(dadosSQL.getInt("PeriodoLetivoFinal.codigo"));
        obj.getPeriodoLetivoFinal().setDescricao(dadosSQL.getString("PeriodoLetivoFinal.descricao"));
        // mapaEquivalencia
        obj.getMapaEquivalenciaUtilizadoGradeMigrar().setCodigo(dadosSQL.getInt("MapaEquivalenciaMatrizCurricular.codigo"));
        // outros campos
        obj.setGerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando(dadosSQL.getBoolean("gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando"));
        obj.setObservacoes(dadosSQL.getString("observacoes"));
        obj.setObservacaoHistorico(dadosSQL.getString("observacaoHistorico"));
        obj.setResultadoProcessamento(dadosSQL.getString("resultadoprocessamento"));
        obj.setData(dadosSQL.getDate("dataTransferencia"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.getResponsavel().setNome(dadosSQL.getString("Usuario.nome"));
        obj.setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(dadosSQL.getBoolean("realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento"));
        obj.setUtilizarAnoSemestreAtualDisciplinaAprovada(dadosSQL.getBoolean("utilizarAnoSemestreAtualDisciplinaAprovada"));
        obj.setAnoDisciplinaAprovada(dadosSQL.getString("anoDisciplinaAprovada"));
        obj.setSemestreDisciplinaAprovada(dadosSQL.getString("semestreDisciplinaAprovada"));
        if (nivelMontar.equals(NivelMontarDados.TODOS)) {
            // montar dados das matriculas da transferencia...
            List<TransferenciaMatrizCurricularMatriculaVO> listaTranferenciaMatricula = getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().consultaRapidaPorTransferenciaMatrizCurricular(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
            obj.setListaTransferenciaMatrizCurricularMatricula(listaTranferenciaMatricula);
        }
        return obj;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void validarDados(TransferenciaMatrizCurricularVO obj) throws Exception {
        if (obj.getGradeOrigem().getCodigo().equals(0)) {
            throw new Exception("Não foi informada uma Matriz Curricular de Origem para Transferência de Matriz");
        }
        if (obj.getGradeMigrar().getCodigo().equals(0)) {
            throw new Exception("Não foi informada uma Matriz Curricular de Destino para Transferência de Matriz");
        }
//        if (obj.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo().equals(0)) {
//            throw new Exception("Não foi informado o Mapa de Equivalência da Matriz Destino a ser utilizado no processo de transferência");
//        }
        if (obj.getUnidadeEnsino().getCodigo().equals(0)) {
            throw new Exception("Não foi informado a Unidade de Ensino para Transferência de Matriz");
        }
        if (obj.getUnidadeEnsinoCurso().getCodigo().equals(0)) {
            throw new Exception("Não foi informado o Curso para realizar Transferência de Matriz");
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return TransferenciaMatrizCurricular.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        TransferenciaMatrizCurricular.idEntidade = idEntidade;
    }

//	@Override
//	public void realizarSugestaoMediaEFrequenciaDisciplinaEquivalente(ItemDisciplinaAntigaDisciplinaNovaVO itemDisciplinaAntigaDisciplinaNovaVO, Boolean usarSituacaoAprovadoAproveitamento) {
//		String ano = "";
//		String semestre = "";
//		Double mediaFinal = null;
//		Double frequencia = null;
//		boolean existeSituacaoDiferente = false;
//		boolean existeSituacaoCursando = false;
//		String primeiraSituacao = "";
//		String instituicao = "";
//		CidadeVO cidade = null;
//		Integer cargaHorariaDisciplinaAproveitada = 0;
//		Integer cargaHorariaCursada = 0;
//		
//		for (HistoricoVO historicoVO : itemDisciplinaAntigaDisciplinaNovaVO.getHistoricoDisciplinaEquivalenteVOs()) {
//			if (historicoVO.getMediaFinal() != null) {
//				if (mediaFinal == null) {
//					mediaFinal = 0.0;
//				}
//				mediaFinal += historicoVO.getMediaFinal();
//			}
//			if (historicoVO.getFreguencia() != null) {
//				if (frequencia == null) {
//					frequencia = 0.0;
//				}
//				frequencia += historicoVO.getFreguencia();
//			}
//			if (ano.trim().isEmpty()) {
//				ano = historicoVO.getAnoHistorico();
//				semestre = historicoVO.getSemestreHistorico();
//			} else if ((historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico()).compareTo((ano + "/" + semestre)) < 0) {
//				ano = historicoVO.getAnoHistorico();
//				semestre = historicoVO.getSemestreHistorico();
//			}
//			if (primeiraSituacao.trim().isEmpty()) {
//				primeiraSituacao = historicoVO.getSituacao();
//			}
//			if (!primeiraSituacao.equals(historicoVO.getSituacao())) {
//				existeSituacaoDiferente = true;
//			}
//			if(!historicoVO.getInstituicao().trim().isEmpty() && instituicao.trim().isEmpty()){
//				instituicao = historicoVO.getInstituicao();
//			}
//			if(historicoVO.getCargaHorariaAproveitamentoDisciplina() > 0 && cargaHorariaDisciplinaAproveitada == 0){
//				cargaHorariaDisciplinaAproveitada = historicoVO.getCargaHorariaAproveitamentoDisciplina();
//			}
//			if(historicoVO.getCargaHorariaCursada() > 0 && cargaHorariaCursada == 0){
//				cargaHorariaCursada = historicoVO.getCargaHorariaCursada();
//			}
//			if(historicoVO.getCidadeVO().getCodigo() > 0 && cidade == null){
//				cidade = new CidadeVO();
//				cidade.setCodigo(historicoVO.getCidadeVO().getCodigo());
//				cidade.setNome(historicoVO.getCidadeVO().getNome());
//			}
//			if (historicoVO.getSituacao().equals("CS")) {
//				existeSituacaoCursando = true;
//			}
//		}
//		if (existeSituacaoCursando) {
//			itemDisciplinaAntigaDisciplinaNovaVO.setSituacao("CS");
//			mediaFinal = null;
//			frequencia = null;
//		} else if (usarSituacaoAprovadoAproveitamento) {
//			itemDisciplinaAntigaDisciplinaNovaVO.setSituacao("AA");
//		} else if (existeSituacaoDiferente) {
//			itemDisciplinaAntigaDisciplinaNovaVO.setSituacao("");
//		} else {
//			itemDisciplinaAntigaDisciplinaNovaVO.setSituacao(primeiraSituacao);
//		}
//		itemDisciplinaAntigaDisciplinaNovaVO.setAno(ano);
//		itemDisciplinaAntigaDisciplinaNovaVO.setSemestre(semestre);
//		itemDisciplinaAntigaDisciplinaNovaVO.setInstituicao(instituicao);
//		itemDisciplinaAntigaDisciplinaNovaVO.setCidadeVO(cidade);
//		itemDisciplinaAntigaDisciplinaNovaVO.setCargaHorariaAproveitamentoDisciplina(cargaHorariaDisciplinaAproveitada);
//		
//
//		if (mediaFinal == null) {
//			itemDisciplinaAntigaDisciplinaNovaVO.setMediaFinal(null);
//		} else {
//			itemDisciplinaAntigaDisciplinaNovaVO.setMediaFinal(Uteis.arrendondarForcando2CadasDecimais((mediaFinal / itemDisciplinaAntigaDisciplinaNovaVO.getHistoricoDisciplinaEquivalenteVOs().size())));
//		}
//		if (frequencia == null) {
//			itemDisciplinaAntigaDisciplinaNovaVO.setFrequencia(null);
//		} else {
//			itemDisciplinaAntigaDisciplinaNovaVO.setFrequencia(frequencia / itemDisciplinaAntigaDisciplinaNovaVO.getHistoricoDisciplinaEquivalenteVOs().size());
//		}
//		
//		if(cargaHorariaCursada ==  0 && frequencia != null){
//			cargaHorariaCursada = Double.valueOf(((cargaHorariaDisciplinaAproveitada*frequencia)/100)).intValue();
//		}
//		itemDisciplinaAntigaDisciplinaNovaVO.setCargaHorariaCursada(cargaHorariaCursada);
//	}
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	@Override
//	public void realizarMigracaoGrade(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, CursoVO curso, GradeCurricularVO gradeOrigem, GradeCurricularVO gradeMigrar, List<ItemDisciplinaAntigaDisciplinaNovaVO> listaDisciplinaEquivalente, List<DisciplinaForaGradeVO> listaHistoricoMigrarComoForaGrade, List<HistoricoVO> historicosSemDefinicao, List<HistoricoVO> historicosIguais, List<HistoricoVO> historicoIguaisCursandoComPeriodoDiferente, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
//		List<HistoricoGradeVO> historicoGradeVOs = new ArrayList<HistoricoGradeVO>(0);
//
//		for (ItemDisciplinaAntigaDisciplinaNovaVO item : listaDisciplinaEquivalente) {
//			if (item.getSituacao().trim().isEmpty()) {
//				throw new Exception("O campo SITUAÇÃO da disciplina migrada (" + item.getHistoricoDisciplinaEquivalenteVOs().get(0).getDisciplina().getNome().toUpperCase() + ") deve ser informado.");
//			}
//		}
//		for (DisciplinaForaGradeVO item : listaHistoricoMigrarComoForaGrade) {
//			if (item.getSituacao().trim().isEmpty()) {
//				throw new Exception("O campo SITUAÇÃO da disciplina migrada como fora da grade (" + item.getDisciplina().toUpperCase() + ") deve ser informado.");
//			}
//		}
//
//		historicoGradeVOs = getFacadeFactory().getHistoricoGradeFacade().consultarGradeOrigemAntigaAlunoPorMatricula(matricula.getMatricula(), usuario);
//		Map<Integer, PeriodoLetivoVO> mapNovoPeriodoLetivoTurmaVO = new HashMap<Integer, PeriodoLetivoVO>(0);
//		List<MatriculaPeriodoVO> matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodos(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuario);
//		for (MatriculaPeriodoVO matPer : matriculaPeriodoVOs) {
//			PeriodoLetivoVO periodoLetivoNovo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorPeriodoLetivoGradeCurricular(matPer.getPeridoLetivo().getPeriodoLetivo(), gradeMigrar.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//			if (periodoLetivoNovo.getCodigo().intValue() == 0) {
//				// Caso nao seja encontrado periodo letivo correspondente ao
//				// anterior na nova grade curricular a aplicação irá jogar
//				// o vinculo do perioLetivo da nova grade para o ultimo periodo
//				// Ativo do aluno.
//
//				periodoLetivoNovo = getFacadeFactory().getPeriodoLetivoFacade().consultarUltimoPeriodoLetivoGradeCurricular(gradeMigrar.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//				getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivoGradeCurricular(matPer.getCodigo(), periodoLetivoNovo.getCodigo(), gradeMigrar.getCodigo());
//			} else {
//				// update na matriculaPeriodo alterando o periodo letivo e a
//				// gade curricular;
//				getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivoGradeCurricular(matPer.getCodigo(), periodoLetivoNovo.getCodigo(), gradeMigrar.getCodigo());
//			}
//			mapNovoPeriodoLetivoTurmaVO.put(matPer.getCodigo(), periodoLetivoNovo);
//			if (matPer.getCodigo().intValue() == matriculaPeriodoVO.getCodigo().intValue()) {
//				matriculaPeriodoVO.setGradeCurricular(gradeMigrar);
//				matriculaPeriodoVO.setPeridoLetivo(periodoLetivoNovo);
//			}
//		}
//
//		// Altera a Turma Base da Matricula Periodo
//		if (matriculaPeriodoVO.getSituacaoAtiva() || matriculaPeriodoVO.getSituacaoPreMatricula()) {
//			getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodoVOEspecifico(matriculaPeriodoVO, matricula, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, usuario);
//			getFacadeFactory().getPlanoFinanceiroAlunoLogFacade().realizarCriacaoLogPlanoFinanceiroAluno(matricula, matriculaPeriodoVO, usuario);
//			getFacadeFactory().getPlanoFinanceiroAlunoFacade().persistirLevandoEmContaMatriculaPeriodo(matricula.getPlanoFinanceiroAluno(), matriculaPeriodoVO.getCodigo());
//			getFacadeFactory().getMatriculaFacade().regerarBoletos(matriculaPeriodoVO, configuracaoFinanceiroVO, usuario);
//			getFacadeFactory().getMatriculaFacade().incluirLogMatricula(matricula, matriculaPeriodoVO, usuario);
//			getFacadeFactory().getContaReceberFacade().realizarAtualizacaoNumeroParcelaContaReceberMensalidade(matriculaPeriodoVO.getCodigo(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo());
//		} else {
//			getFacadeFactory().getMatriculaPeriodoFacade().alterarTurmaBaseMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), matriculaPeriodoVO.getTurma().getCodigo());
//		}
//
//		// Criar o registro de migracao com equivalencia
//		List<HistoricoGradeMigradaEquivalenteVO> historicoGradeMigradaEquivalenteVOs = realizarMigracaoGradeEquivalente(matricula, curso, gradeOrigem, gradeMigrar, listaDisciplinaEquivalente, configuracaoFinanceiroVO, usuario);
//		// Criar o registro de inclusão fora da grade
//		realizarInclusaoDisciplinaForaGrade(matricula, listaHistoricoMigrarComoForaGrade, usuario);
//		// Deleta os historicos que não serão migrados para a nova grade
//		for (HistoricoVO historicoVO : historicosSemDefinicao) {
//			getFacadeFactory().getHistoricoFacade().excluir(historicoVO, false, usuario);
//			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluir(historicoVO.getMatriculaPeriodoTurmaDisciplina(), false, usuario);
//		}
//		// Delete os historicos da grade atual com reprovado, trancado e
//		// reprovado por falta
//		// getFacadeFactory().getHistoricoFacade().excluirHistoricoPorMatriculaESituacoes(matricula.getMatricula(),
//		// "'RE', 'RF', 'TR'");
//
//		// Alterar as turmas das matriculas periodos turmas disciplinas da
//		// matricula periodo atual que estão no mesmo periodo letivo
//		for (HistoricoVO historicoVO : historicosIguais) {
//			if (historicoVO.getSituacao().equals("CS")) {
//				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarTurmaMatriculaPeriodoTurmaDisciplina(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), matriculaPeriodoVO.getTurma().getCodigo(), false);
//			}
//		}
//		// Alterar as turmas das matriculas periodos turmas disciplinas que não
//		// estão no mesmo periodo letivo
//		for (HistoricoVO historicoVO : historicoIguaisCursandoComPeriodoDiferente) {
//			if (historicoVO.getSituacao().equals("CS")) {
//				historicoVO.getMatriculaPeriodoTurmaDisciplina().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
//				Boolean disciplinaIncluida = (mapNovoPeriodoLetivoTurmaVO.get(historicoVO.getMatriculaPeriodo().getCodigo()).getPeriodoLetivo().intValue() != historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getPeridoLetivo().getPeriodoLetivo().intValue());
//				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarTurmaMatriculaPeriodoTurmaDisciplina(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), disciplinaIncluida);
//			}
//		}
//		// Grava o registro de Migração da Grade
//		gravarTransferenciaMatrizCurricular(matricula.getMatricula(), historicoGradeVOs, historicoGradeMigradaEquivalenteVOs, gradeOrigem.getCodigo(), gradeMigrar.getCodigo(), usuario);
//	}
//
//	private void realizarInclusaoDisciplinaForaGrade(MatriculaVO matricula, List<DisciplinaForaGradeVO> listaHistoricoMigrarComoForaGrade, UsuarioVO usuario) throws Exception {
//		if (listaHistoricoMigrarComoForaGrade.isEmpty()) {
//			return;
//		}
//		InclusaoDisciplinaForaGradeVO inclusaoDisciplinaForaGradeVO = new InclusaoDisciplinaForaGradeVO();
//		inclusaoDisciplinaForaGradeVO.setDisciplinaForaGradeVOs(listaHistoricoMigrarComoForaGrade);
//		inclusaoDisciplinaForaGradeVO.setMatriculaVO(matricula);
//		getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().incluir(inclusaoDisciplinaForaGradeVO, usuario);
//		for (DisciplinaForaGradeVO disciplinaForaGradeVO : listaHistoricoMigrarComoForaGrade) {
//			getFacadeFactory().getHistoricoFacade().excluir(disciplinaForaGradeVO.getHistoricoVO(), false, usuario);
//			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluir(disciplinaForaGradeVO.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina(), false, usuario);
//		}
//	}
//
//	private List<HistoricoGradeMigradaEquivalenteVO> realizarMigracaoGradeEquivalente(MatriculaVO matricula, CursoVO curso, GradeCurricularVO gradeOrigem, GradeCurricularVO gradeMigrar, List<ItemDisciplinaAntigaDisciplinaNovaVO> listaDisciplinaEquivalente, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
//		List<HistoricoGradeMigradaEquivalenteVO> historicoGradeMigradaEquivalenteVOs = new ArrayList<HistoricoGradeMigradaEquivalenteVO>(0);
//		MatriculaPeriodoVO matriculaPeriodoUsar = null;
//
//		Map<String, MatriculaPeriodoVO> mapMatriculaPeriodoAnoSemestreVOs = new HashMap<String, MatriculaPeriodoVO>();
//		if (!listaDisciplinaEquivalente.isEmpty()) {
//			for (ItemDisciplinaAntigaDisciplinaNovaVO item : listaDisciplinaEquivalente) {
//				matriculaPeriodoUsar = new MatriculaPeriodoVO();
//				for (MatriculaPeriodoTurmaDisciplinaVO matrPerTurDisc : item.getListaDisciplinaGradeMigrarVOs()) {
//
//					if (mapMatriculaPeriodoAnoSemestreVOs.containsKey(item.getAno() + "/" + item.getSemestre())) {
//						matriculaPeriodoUsar = mapMatriculaPeriodoAnoSemestreVOs.get(item.getAno() + "/" + item.getSemestre());
//					} else {
//						matriculaPeriodoUsar = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(matricula.getMatricula(), item.getSemestre(), item.getAno(), false, usuario);
//						if (matriculaPeriodoUsar.getCodigo() == 0) {
//							if (item.getSituacao().equals("CS")) {
//								throw new Exception("Não foi encontrado uma MATRÍCULA no ANO/SEMESTRE definido na disciplina (" + item.getHistoricoDisciplinaEquivalenteVOs().get(0).getDisciplina().getNome().toUpperCase() + ") da grade migrada! ");
//							} else {
//								matriculaPeriodoUsar = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaNrPeriodoLetivo(matricula.getMatricula(), matrPerTurDisc.getGradeDisciplinaVO().getPeriodoLetivoVO().getPeriodoLetivo(), false, usuario);
//								if (matriculaPeriodoUsar.getCodigo() == 0) {
//									matriculaPeriodoUsar = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaPrimeiraMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//								}
//							}
//						}
//						mapMatriculaPeriodoAnoSemestreVOs.put(item.getAno() + "/" + item.getSemestre(), matriculaPeriodoUsar);
//					}
//					matrPerTurDisc.setAno(matriculaPeriodoUsar.getAno());
//					matrPerTurDisc.setSemestre(matriculaPeriodoUsar.getSemestre());
//					matrPerTurDisc.setMatricula(matricula.getMatricula());
//					matrPerTurDisc.setMatriculaPeriodo(matriculaPeriodoUsar.getCodigo());
//					matrPerTurDisc.setIsentarMediaFinal(item.getIsentarMediaFinal());
//					historicoGradeMigradaEquivalenteVOs.add(realizarCriacaoHistoricoGradeMigradaEquivalente(matrPerTurDisc, item, usuario));
//					if(!item.getSituacao().equals("AA") &&  !item.getSituacao().equals("CC")){
//						item.setInstituicao("");
//						item.setCidadeVO(null);
//						item.setCargaHorariaAproveitamentoDisciplina(0);
//					}
//					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirTransferenciaGradeCurricular(matricula, matriculaPeriodoUsar, matrPerTurDisc, item.getMediaFinal(), item.getFrequencia(), item.getMediaFinalNotaConceito().getCodigo(), item.getSituacao(), item.getAno(), item.getSemestre(), item.getInstituicao(), item.getCidadeVO().getCodigo(), item.getCargaHorariaAproveitamentoDisciplina(), item.getCargaHorariaCursada(), configuracaoFinanceiroVO, usuario);
//				}
//				for (HistoricoVO historicoVO : item.getHistoricoDisciplinaEquivalenteVOs()) {
//					getFacadeFactory().getHistoricoFacade().excluir(historicoVO, false, usuario);
//					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluir(historicoVO.getMatriculaPeriodoTurmaDisciplina(), false, usuario);
//				}
//			}
//
//		}
//		return historicoGradeMigradaEquivalenteVOs;
//	}
//
//	public HistoricoGradeMigradaEquivalenteVO realizarCriacaoHistoricoGradeMigradaEquivalente(MatriculaPeriodoTurmaDisciplinaVO mptd, ItemDisciplinaAntigaDisciplinaNovaVO itemDisciplinaAntigaDisciplinaNovaVO, UsuarioVO usuarioVO) throws Exception {
//		HistoricoGradeMigradaEquivalenteVO obj = new HistoricoGradeMigradaEquivalenteVO();
//		obj.setDisciplinaVO(mptd.getDisciplina());
//		obj.getMatriculaPeriodoVO().setCodigo(mptd.getMatriculaPeriodo());
//		obj.setMatriculaPeriodoApresentarHistoricoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(mptd.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//		obj.setSituacao(itemDisciplinaAntigaDisciplinaNovaVO.getSituacao());
//		obj.setMediaFinal(itemDisciplinaAntigaDisciplinaNovaVO.getMediaFinal());
//		obj.setAnoHistorico(itemDisciplinaAntigaDisciplinaNovaVO.getAno());
//		obj.setSemestreHistorico(itemDisciplinaAntigaDisciplinaNovaVO.getSemestre());
//		obj.setMediaFinalNotaConceito(itemDisciplinaAntigaDisciplinaNovaVO.getMediaFinalNotaConceito());
//		obj.setFrequencia(itemDisciplinaAntigaDisciplinaNovaVO.getFrequencia());		
//		return obj;
//	}
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarTransferenciaMatrizCurricular(String matricula, List<HistoricoGradeVO> historicoGradeVOs, List<HistoricoGradeMigradaEquivalenteVO> historicoGradeMigradaEquivalenteVOs, Integer gradeOrigem, Integer gradeMigrar, UsuarioVO usuario) throws Exception {
//		TransferenciaMatrizCurricularVO obj = new TransferenciaMatrizCurricularVO();
//		obj.setMatricula(matricula);
//		obj.setGradeOrigem(gradeOrigem);
//		obj.setGradeMigrar(gradeMigrar);
//		obj.setResponsavel(usuario);
//		obj.setData(new Date());
//		obj.setHistoricoGradeVOs(historicoGradeVOs);
//		obj.setHistoricoGradeMigradaEquivalenteVOs(historicoGradeMigradaEquivalenteVOs);
//		getFacadeFactory().getTransferenciaMatrizCurricularFacade().incluir(obj, usuario);
    }

    public TransferenciaMatrizCurricularVO consultarPorMatriculaCodigoGradeOrigem(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select codigo, matricula from transferenciamatrizcurricular ");
        sb.append(" where gradeOrigem = ").append(gradeCurricular).append(" and matricula = '").append(matricula).append("' ");
        sb.append(" order by codigo limit 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        TransferenciaMatrizCurricularVO obj = null;
        if (tabelaResultado.next()) {
            obj = new TransferenciaMatrizCurricularVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            //obj.setMatricula(tabelaResultado.getString("matricula"));
        }
        return obj;
    }
    
    public TransferenciaMatrizCurricularVO consultarPorMatriculaCodigoGradeAtual(String matricula, Integer gradeCurricularAtual, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select transferenciamatrizcurricularatual.codigo, transferenciamatrizcurricularatual.gradeorigem, gradecurricular.nome from transferenciamatrizcurricularatual ");
        sb.append(" inner join transferenciamatrizcurricularmatricula ");
        sb.append(" on transferenciamatrizcurricularmatricula.transferenciamatrizcurricularatual = transferenciamatrizcurricularatual.codigo ");
        sb.append(" inner join matricula ");       
        sb.append(" on transferenciamatrizcurricularmatricula.matricula = matricula.matricula ");
        sb.append(" inner join gradecurricular ");
        sb.append(" on transferenciamatrizcurricularatual.gradeorigem = gradecurricular.codigo ");
        sb.append(" where transferenciamatrizcurricularatual.grademigrar = ").append(gradeCurricularAtual).append(" and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" order by codigo limit 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        TransferenciaMatrizCurricularVO obj = null;
        if (tabelaResultado.next()) {
            obj = new TransferenciaMatrizCurricularVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.getGradeOrigem().setCodigo(tabelaResultado.getInt("gradeorigem"));
            obj.getGradeOrigem().setNome(tabelaResultado.getString("nome"));
        }
        return obj;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravar(TransferenciaMatrizCurricularVO transferencia, UsuarioVO usuario) throws Exception {
        if (transferencia.getCodigo().equals(0)) {
            incluir(transferencia, usuario);
        } else {
            alterar(transferencia, usuario);
        }
    }

    private boolean verificarComposicaoGradeOrigemFoiMantidaGradeDestino(
            GradeDisciplinaVO gradeDisciplinaCorrespondente,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespodente,
            HistoricoVO historicoAtualAluno,
            UsuarioVO usuario) throws Exception {
        List<GradeDisciplinaCompostaVO> composicaoGradeOrigem = null;
        List<GradeDisciplinaCompostaVO> composicaoGradeDestino = null;
        if (gradeDisciplinaCorrespondente != null) {
            // se gradeDisciplinaCorrespondente é diferente nulo é por que este método precisa verificar
            // a composicao de uma GradeDisciplinaVO
            if (!gradeDisciplinaCorrespondente.getDisciplinaComposta()) {
                // se nao é composta já retornamos false
                return false;
            }
            //carregando dados composicao para verificacao
            GradeDisciplinaVO gradeComComposicao = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaCorrespondente.getCodigo(), usuario);
            composicaoGradeDestino = gradeComComposicao.getGradeDisciplinaCompostaVOs();
            // atualizando esta informacao pois será utilizada adiante
            gradeDisciplinaCorrespondente.setGradeDisciplinaCompostaVOs(composicaoGradeDestino);
        } else {
            if (gradeCurricularGrupoOptativaDisciplinaCorrespodente != null) {
                // se gradeCurricularGrupoOptativaDisciplinaCorrespodente é diferente nulo é por que este método precisa verificar
                // a composicao de uma GradeCurricularGrupoOptativaDisciplinaVO
                if (!gradeCurricularGrupoOptativaDisciplinaCorrespodente.getDisciplinaComposta()) {
                    // se nao é composta já retornamos false
                    return false;
                }
                //carregando dados composicao para verificacao
                GradeCurricularGrupoOptativaDisciplinaVO gradeComComposicao = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(gradeCurricularGrupoOptativaDisciplinaCorrespodente.getCodigo(), usuario);
                composicaoGradeDestino = gradeComComposicao.getGradeDisciplinaCompostaVOs();
                // atualizando esta informacao pois será utilizada adiante
                gradeCurricularGrupoOptativaDisciplinaCorrespodente.setGradeDisciplinaCompostaVOs(composicaoGradeDestino);
            }
        }
        if (composicaoGradeDestino == null) {
            // Se nao foi possivel determinar a composicao da gradeDestino, já retornamos falso
            return false;
        }
        if (historicoAtualAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
            if (!historicoAtualAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaComposta()) {
                return false;
            }
            //carregando dados composicao para verificacao
            GradeCurricularGrupoOptativaDisciplinaVO gradeComComposicao = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(historicoAtualAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuario);
            composicaoGradeOrigem = gradeComComposicao.getGradeDisciplinaCompostaVOs();
            // atualizando esta informacao pois será utilizada adiante
            historicoAtualAluno.getGradeCurricularGrupoOptativaDisciplinaVO().setGradeDisciplinaCompostaVOs(composicaoGradeDestino);
        } else {
            if (!historicoAtualAluno.getGradeDisciplinaVO().getDisciplinaComposta()) {
                return false;
            }
            //carregando dados composicao para verificacao
            GradeDisciplinaVO gradeComComposicao = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(historicoAtualAluno.getGradeDisciplinaVO().getCodigo(), usuario);
            composicaoGradeOrigem = gradeComComposicao.getGradeDisciplinaCompostaVOs();
            // atualizando esta informacao pois será utilizada adiante
            historicoAtualAluno.getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(composicaoGradeDestino);
        }
        if (composicaoGradeOrigem == null) {
            // Se nao foi possivel determinar a composicao da gradeDestino, já retornamos falso
            return false;
        }
        // se chegarmos aqui é por que determinar a composicao da grade origem e destino. Agora temos que verificar
        // se as mesmas são compativeis. Ou seja, se as disciplias da composicao sao as mesmas e com as mesmas
        // cargas horarias
        for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaOrigem : composicaoGradeOrigem) {
            Integer codigoDisciplinaOrigem = gradeDisciplinaCompostaOrigem.getDisciplina().getCodigo();
            Integer cargaHorariaOrigem = gradeDisciplinaCompostaOrigem.getCargaHoraria();
            for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaDestino : composicaoGradeDestino) {
                if ((!gradeDisciplinaCompostaDestino.getDisciplina().getCodigo().equals(codigoDisciplinaOrigem))
                        || (!gradeDisciplinaCompostaDestino.getCargaHoraria().equals(cargaHorariaOrigem))) {
                    // se uma disciplina/carga horaria da composicao nao bate, entao concluimos que a composicao
                    // nao é igual
                    return false;
                }
            }
        }
        // se chegarmos aqui é por que todas as disciplinas da composicaoOrgem é igual
        // as disciplinas da composicaoDestino. Logo, basta garantimos que a composicao
        // destino nao tenha mais disciplinas que a composicao origem.
        if (composicaoGradeOrigem.size() == composicaoGradeDestino.size()) {
            return true;
        }
        return false;
    }

    private void inicializarDadosBasicosNovoHistoricoMigrado(
            HistoricoVO novoHistorico,
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            PeriodoLetivoVO periodoLetivoMatrizAdotar,
            PeriodoLetivoVO periodoLetivoCursarAdotar,
            UsuarioVO usuario) {
        novoHistorico.setHistoricoGeradoAPartirTransferenciaMatrizCurricular(Boolean.TRUE);
        novoHistorico.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(transferencia.getData(), usuario.getNome_Apresentar(), "Histórico Migrado da Matriz Currilar " + transferencia.getGradeOrigem().getNome() + "(" + transferencia.getGradeOrigem().getCodigo() + ") para a Matriz " + transferencia.getGradeMigrar().getNome() + "(" + transferencia.getGradeMigrar().getCodigo() + ")", false);
        novoHistorico.setResponsavel(usuario);
        novoHistorico.setDataRegistro(new Date());
        novoHistorico.setMatrizCurricular(transferencia.getGradeMigrar()); // atualizando a grade
        novoHistorico.setHistoricoDisciplinaForaGrade(Boolean.FALSE);
		if (transferencia.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento() 
				&& (novoHistorico.getSituacao().equals(SituacaoHistorico.APROVADO.getValor())
						|| novoHistorico.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor()))) {
			novoHistorico.setSituacao(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor());
		}
		if (transferencia.getUtilizarAnoSemestreAtualDisciplinaAprovada() 
				&& (novoHistorico.getSituacao().equals(SituacaoHistorico.APROVADO.getValor()) 
						|| novoHistorico.getSituacao().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor())
						|| novoHistorico.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor()))) {
			novoHistorico.setAnoHistorico(transferencia.getAnoDisciplinaAprovada());
			novoHistorico.setSemestreHistorico(transferencia.getSemestreDisciplinaAprovada());
		}

        // matriculaPeriodoTurmaDisciplina precisa ser zerado, para não listar este histórico
        // de forma equivocada, em uma turma, na qual nao tem vinculo real com a grade destino
        // na qual o aluno está sendo migrado. A turma na qual ele estou 
        novoHistorico.setMatriculaPeriodoTurmaDisciplina(null);

        // PeriodoLetivo será atualizado para o periodoLetivo da gradeDisciplina correspodente.
        novoHistorico.setPeriodoLetivoMatrizCurricular(periodoLetivoMatrizAdotar);
        if ((periodoLetivoCursarAdotar == null) || (periodoLetivoCursarAdotar.getCodigo().equals(0))) {
            periodoLetivoCursarAdotar = periodoLetivoMatrizAdotar;
        }
        novoHistorico.setPeriodoLetivoCursada(periodoLetivoCursarAdotar);
    }

    /**
     * Método responsável por gerar um histórico para uma disciplina
     * que faz parte de uma composição GradeDisciplinaCompostaVO 
     * no momento da migração de matriz
     */
    private HistoricoVO gerarHistoricoDisiciplinaFazParteComposicao(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeDisciplinaVO gradeDisciplinaCorrespondente,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente,
            GradeDisciplinaCompostaVO gradeDisciplinaComposta,
            HistoricoVO historicoOrigemAluno,
            Boolean disciplinaMigradaMesmaComposicao,
            UsuarioVO usuario) throws Exception {
        HistoricoVO novoHistorico = (HistoricoVO) historicoOrigemAluno.clone();
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao

        PeriodoLetivoVO periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(historicoOrigemAluno.getPeriodoLetivoCursada().getPeriodoLetivo());
        if (gradeDisciplinaCorrespondente != null) {
            inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, gradeDisciplinaCorrespondente.getPeriodoLetivoVO(), periodoLetivoCursar, usuario);
        } else {
            inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), periodoLetivoCursar, usuario);
        }
        
        novoHistorico.setDisciplina(gradeDisciplinaComposta.getDisciplina());
        novoHistorico.setHistoricoDisciplinaComposta(Boolean.FALSE);
        novoHistorico.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);
        novoHistorico.setGradeDisciplinaComposta(gradeDisciplinaComposta);
        novoHistorico.setGradeDisciplinaVO(gradeDisciplinaComposta.getGradeDisciplina());
        if (!Uteis.isAtributoPreenchido(novoHistorico.getConfiguracaoAcademico().getCodigo())) {
        	Integer codigoCfgAcademicaEspecifica = 0;
        	if (gradeDisciplinaCorrespondente != null) {
        		codigoCfgAcademicaEspecifica = gradeDisciplinaCorrespondente.getConfiguracaoAcademico().getCodigo();
        	} else {
        		codigoCfgAcademicaEspecifica = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getConfiguracaoAcademico().getCodigo();
        	}
        	ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = transferencia.getConfiguracaoAcademicoCursoTransferencia(); 
        	if (!codigoCfgAcademicaEspecifica.equals(0)) {
        		ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
        		configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
        	}
        	novoHistorico.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);
        }
        // Registrando observacao caso o historico da gradeOrigem era uma disciplina composta
        String msgInicial = "Histórico migrado da matriz (" + transferencia.getGradeOrigem().getCodigo() + ") ";
        if (disciplinaMigradaMesmaComposicao) {
        	novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " era de uma disciplina que fazia parte de uma composição. Este histórico foi migrado para nova Matriz para a mesma composição.", true);
        } else {
        	// neste caso já vamos usar a observacao do historico base, que já contem as informaccoes da origem
        }
        
        if (historicoOrigemAluno.getCursando()) {
        	// se partimos de um historico que o aluno ainda estava sendo cursando, significa que iremos gerar um novo historico
        	// cursando por correspodencia e que o este historico base terá que ser marcado para indicar que o mesmo irá pagar
        	// um histórico correspodente (no caso filho de uma composicao).
        	novoHistorico.setSituacao("CO");
        	novoHistorico.setMatriculaPeriodo(historicoOrigemAluno.getMatriculaPeriodo());
        	novoHistorico.setHistoricoGeradoAPartirTransferenciaMatrizCurricular(Boolean.TRUE);
        	novoHistorico.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        	
        	// vamos manter o rastro do historico base para que futuramente ao lancar nota para este historico base,
        	// o resultado final seja automaticamente levado para o historico correspodente.
        	historicoOrigemAluno.setHistoricoCursandoPorCorrespondenciaAposTransferencia(Boolean.TRUE);
        	historicoOrigemAluno.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        	getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(historicoOrigemAluno.getCodigo(), matriculaProcessar.getCodigo(), true, usuario);
        }

        // gravando novo histórico
        getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
        
        return novoHistorico;
    }

    /*
     * Método responsável por gerar históricos para as disciplinas que fazem parte
     * de uma composição. Este método trabalha para: 
     *   a) Gerar históricos com base em históricos já existentes na matriz curricular anterior.
     *       Isto é possível quando a composição existente na gradecurricular origem é identico
     *       ao existente na gradecurricular destino e o aluno já está aprovado nesta disciplina.
     */
    private void gerarHistoricoDisiciplinasFazemParteComposicao(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeDisciplinaVO gradeDisciplinaCorrespondente,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente,
            HistoricoVO historicoOrigemAluno,
            UsuarioVO usuario) throws Exception {
        if (historicoOrigemAluno.getReprovado()) {
            // se estamos tratando de um histórico de reprovação na matriz origem
            // então este histórico de reprovação basta existir para a disciplina mãe (composta).
            // Não há a necessidade de existir para as discilpinas filhas da composição, no caso de reprovação.
            return;
        }
        List<GradeDisciplinaCompostaVO> disciplinasCompostaOrigem = null;
        if (historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
            disciplinasCompostaOrigem = historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs();
        } else {
            disciplinasCompostaOrigem = historicoOrigemAluno.getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs();
        }
        HistoricoVO historicoAproveitarDisciplinaFazParteComposicao = null;
        // para cada disciplina composta acima, iremos obter seu histórico correspondente na matriculaComHistoricoVO
        // e utilizá-lo para geração do novo histórico migrado 
        for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : disciplinasCompostaOrigem) {
            if (historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
                historicoAproveitarDisciplinaFazParteComposicao = matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria());
                if (!historicoAproveitarDisciplinaFazParteComposicao.getCodigo().equals(0)) {
                    gerarHistoricoDisiciplinaFazParteComposicao(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, gradeDisciplinaCompostaVO, historicoAproveitarDisciplinaFazParteComposicao, true, usuario);
                } else {
                    // A priori nao vou gerar a exception, pois este histórico seria interessante somente para título de boletim.
                    // Contudo, como trata-se de uma disciplina já aprovada, geralmente, irá ser necessário somente o histórico da mesma
                    // por isto, nao justifica-se interromper a transferencia de matriz curricular em função deste dado.
                    //throw new Exception("Erro ao tentar gerar o histórico de uma disciplina que faz parte da composição. Histórico não encontrado na Matriz Curricular origem. Código da Disciplina que Faz Parte Composição: " + gradeDisciplinaCompostaVO.getDisciplina().getCodigo() + " - " + gradeDisciplinaCompostaVO.getCargaHoraria() + " horas");
                }
            } else {
                historicoAproveitarDisciplinaFazParteComposicao = matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualGradeDisciplinaVO(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria());
                if (!historicoAproveitarDisciplinaFazParteComposicao.getCodigo().equals(0)) {
                    gerarHistoricoDisiciplinaFazParteComposicao(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, gradeDisciplinaCompostaVO, historicoAproveitarDisciplinaFazParteComposicao, true, usuario);
                } else {
                    // A priori nao vou gerar a exception, pois este histórico seria interessante somente para título de boletim.
                    // Contudo, como trata-se de uma disciplina já aprovada, geralmente, irá ser necessário somente o histórico da mesma
                    // por isto, nao justifica-se interromper a transferencia de matriz curricular em função deste dado.
                    //throw new Exception("Erro ao tentar gerar o histórico de uma disciplina que faz parte da composição. Histórico não encontrado na Matriz Curricular origem. Código da Disciplina que Faz Parte Composição: " + gradeDisciplinaCompostaVO.getDisciplina().getCodigo() + " - " + gradeDisciplinaCompostaVO.getCargaHoraria() + " horas");
                }
            }
        }
    }

    /**
     * Método responsável por gerar um novo historico, na nova grade, com base em um histórico existente na grade anterior.
     * Este método está preparado para trabalhar somente com historicos aprovados na grade origem e que foram mapeados
     * na nova grade (grade destino).
     * Considerações importantes:
     *    a) Cfg academica deve ser mantida, pois trata-se de um historico já aprovado (logo, a cfg utilizada no ato da aprovacao nao pode ser modificada)
     *    b) Se a disciplina na nova grade ou na grade anterior é composta, ...
     *    c) Se a disciplina fazia parte de uma equivalencia na grade Origem, ou seja, a mesma foi aprovada por equivalencia na grade origem,
     *         estas informaçoes NÃO serão mantidas na gradeDestino. Pois o mapa de equivalencia utilizado na gradeOrigem não é aplicável para
     *         a gradeDestino. Desta maneira, este histórico (que já está aprovado e teoricamente) não será mais editado, será gerado como um novo histórico
     *         convencional, mas vinculado a um registro de transferência de matriz de curricular.
     *     d) Se trata-se de uma disciplina vinculada a uma atividade complementar na gradeOrigem este vinculo será mantido na gradeDestino,
     *         caso contrário o histórico sairá errado em função da totalização da carga horária.
     *     e) Novo histórico criado, não será vinculado a nenhuma MatriculaPeriodoTurmaDisciplina, pois a turma na qual o aluno estudou
     *         na grade anterior não é compatível com a grade atual.
     *     f) PeriodoLetivo será atualizado para o periodoLetivo da gradeDisciplina correspodente.
     *         Tanto o que refere-se ao periodo letivo cursado quanto ao periodo letivo da matriz.
     * Este método também é utilizado para gerar os históricos de reprovação nas disciplinas. Pois o comportamento é identico
     * e a situação do histórico não é alterada na clonagem. O booleno historicoParaRefletirCorrespondenciaGradeOrigem será
     * true somente, quando estivermos gerando um histórico para uma disciplina que está sendo cursada por outra disciplina
     * correspondente. Ou seja, o aluno irá ser aprovado no histórico da grade origem e este histórico que está sendo
     * gerado será aprovado automaticamente, pela existencia de correspondencia de disciplinas da gradeOrigem e gradeDestino.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricoGradeDisciplinaCorrespodente(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeDisciplinaVO gradeDisciplinaCorrespondente,
            HistoricoVO historicoOrigemAluno,
            Boolean historicoParaRefletirCorrespondenciaGradeOrigem,
            UsuarioVO usuario) throws Exception {
        HistoricoVO novoHistorico = (HistoricoVO) historicoOrigemAluno.clone();
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao
        if ((historicoParaRefletirCorrespondenciaGradeOrigem) &&
            (historicoOrigemAluno.getCursando() || historicoOrigemAluno.getCursandoPorEquivalencia())) {
            novoHistorico.setSituacao(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor());
        }
        PeriodoLetivoVO periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(historicoOrigemAluno.getPeriodoLetivoCursada().getPeriodoLetivo());
        inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, gradeDisciplinaCorrespondente.getPeriodoLetivoVO(), periodoLetivoCursar, usuario);

        novoHistorico.setDisciplina(gradeDisciplinaCorrespondente.getDisciplina());
        novoHistorico.setGradeDisciplinaVO(gradeDisciplinaCorrespondente); // atualizar o historico para a nova gradeDisciplina

        String msgInicial = "Histórico migrado da matriz (" + transferencia.getGradeOrigem().getCodigo() + ") ";
        if (historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
            // se o historico utilizado como base era de um grupoOptativa, temos que
            // redefinir esta situação, pois o mesmo está sendo vinculado a uma gradeDisciplina
            // na gradeDestino
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " referia-se à Disciplina de um Grupo Optaviva " + novoHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getNome() + "(" + historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo() + ") - " + historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() + "h", true);
            novoHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(null);
            novoHistorico.setDisciplinaReferenteAUmGrupoOptativa(Boolean.FALSE);
        } else {
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " referia-se à Disciplina Regular " + novoHistorico.getGradeDisciplinaVO().getDisciplina().getNome() + "(" + historicoOrigemAluno.getGradeDisciplinaVO().getDisciplina().getCodigo() + ") - " + historicoOrigemAluno.getGradeDisciplinaVO().getCargaHoraria() + "h", true);
        }

        // se a disciplina foi estudada por equivalencia, esta informação não será gerada
        // na nova grade, pois a mesma já está aprovada e portanto a questão da equivalencia
        // não é mais crítica na nova grade e adicionalmente, os mapas de equivalencia 
        // da grade origem não são válidos para a grade destino, por isto precisam ser zerados.
        if (novoHistorico.getHistoricoPorEquivalencia()) {
            // Registrando observacao caso o historico da gradeOrigem tenha sido estudado por equivalencia
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " foi cursado por equivalência na mesma. Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        if (novoHistorico.getHistoricoEquivalente()) {
            // Registrando observacao caso o historico da gradeOrigem era equivalente (ou seja, fora grade)
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "",  msgInicial + " foi cursado como equivalente (ou seja, não pertencia a esta matriz). Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        novoHistorico.setHistoricoEquivalente(Boolean.FALSE);
        novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
        novoHistorico.setMapaEquivalenciaDisciplina(null);
        novoHistorico.setMapaEquivalenciaDisciplinaCursada(null);
        novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(null);

//      Comentado por Edigar A. Diniz Junior  - todas as composicoes passaram a ser tratadas no metodo
//      próprio para este tipo de disciplina.        
//        if (historicoOrigemAluno.getHistoricoDisciplinaComposta()) {
//            boolean disciplinaDestinoEComposta = gradeDisciplinaCorrespondente.getDisciplinaComposta();
//            boolean composicaoMantidaDaGradeOrigemParaGradeDestino = verificarComposicaoGradeOrigemFoiMantidaGradeDestino(gradeDisciplinaCorrespondente, null, historicoOrigemAluno, usuario);
//            String complementoObservacao = "";
//            if ((disciplinaDestinoEComposta) && (composicaoMantidaDaGradeOrigemParaGradeDestino)) {
//                complementoObservacao = "Históricos das disciplinas que fazem parte da composição foram migrados para nova Matriz Curricular";
//                //TODO MIGRAR HISTORICOS DAS DISCIPLINAS QUE FAZEM PARTE DA COMPOSICAO...
//                gerarHistoricoDisiciplinasFazemParteComposicao(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, historicoOrigemAluno, usuario);
//            } else {
//                if (!disciplinaDestinoEComposta) {
//                    complementoObservacao = "Históricos das disciplinas que fazem parte da composição não foram migrados para nova Matriz, pois a disciplina correspondente na Matriz Destino não é composta";
//                } else {
//                    complementoObservacao = "Históricos das disciplinas que fazem parte da composição não foram migrados para nova Matriz, pois a disciplina correspondente na Matriz Destino não possui a mesma composição. Logo o aluno ficará aprovado na disciplina composta, na nova Matriz, contudo, sem históricos para sua composição.";
//                }
//            }
//            // Registrando observacao caso o historico da gradeOrigem era uma disciplina composta
//            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico Migrado da Matriz Curricular " + transferencia.getGradeOrigem().getNome() + "(" + transferencia.getGradeOrigem().getCodigo() + ") era de uma disciplina composta. " + complementoObservacao, true);
//        }

        if (historicoOrigemAluno.getHistoricoDisciplinaFazParteComposicao()) {
            // Se o historico origem fazia parte de uma composicao e está sendo processado
            // neste método, então é por que na matriz nova, o mesmo não será mais integrante
            // de uma composição, mas sim, passará a ser considerado como um histórico
            // convencional. Isto pode ocorrer, pois na nova matriz, pode haver uma disciplina
            // na grade (ou no grupo de optativas) com as mesmas características da disciplina
            // que fazia parte da composição.
            novoHistorico.setGradeDisciplinaComposta(null);
            novoHistorico.setHistoricoDisciplinaFazParteComposicao(Boolean.FALSE);

            // Registrando observacao caso o historico da gradeOrigem fazia parte de uma composição
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico Migrado da Matriz Curricular " + transferencia.getGradeOrigem().getNome() + "(" + transferencia.getGradeOrigem().getCodigo() + ") fazia parte de uma composição, contudo, foi migrado como histórico de uma Disciplina Regular da nova Matriz. ", true);
        }

        // gravando novo histórico
        getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        //if (!novoHistorico.getReprovado()) {
            // vamos adicionar para a lista abaixo somente os históricos de aprovação e cursando que foram aproveitados
        	if(historicoOrigemAluno.getAprovado() || novoHistorico.getReprovado()) {
        		getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoCursandoTransferenciaMatrizCurricularMatricula(historicoOrigemAluno.getCodigo(), matriculaProcessar.getCodigo(), historicoOrigemAluno.getSituacao(), true, usuario);
        	}
            matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
        //} 
    }

    /**
     * Método realiza a geração e migração de históricos de reprovação do aluno
     * em uma gradeDisciplina que foi migrada da gradeOrigem para a destino.
     * Esta migração é importante pois alguns instituição geram o histórico com
     * as reprovações do aluno também. Adicionamente, existem políticas de cobrança
     * por exemplo, que podem ser diferente caso o aluno já tenha feita uma disciplina
     * e reprovado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricosReprovacaoGradeDisciplinaCorrespodente(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO,
            UsuarioVO usuario, GradeDisciplinaVO gradeDisciplinaCorrespondente) throws Exception {
        for (HistoricoVO historicoReprovacao : gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno()) {
            // iremos processar todos os históricos que o aluno está reprovado e que temos
            // que migrar o mesmo para nova grade
            if (historicoReprovacao.getReprovado()) {
            	if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoReprovacao, transferencia, matriculaProcessar, false, usuario) ) {
            		gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, historicoReprovacao, false, usuario);
            	}
            }
        }
    }

    /**
     * Método realiza a geração e migração de históricos de reprovação do aluno
     * em uma gradeDisciplina que foi migrada da gradeOrigem para a destino.
     * Esta migração é importante pois alguns instituição geram o histórico com
     * as reprovações do aluno também. Adicionamente, existem políticas de cobrança
     * por exemplo, que podem ser diferente caso o aluno já tenha feita uma disciplina
     * e reprovado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricosReprovacaoGradeCurricularGrupoOptativaDisciplinaCorrespodente(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondenteVO,
            PeriodoLetivoVO periodoLetivoAdotarVO,
            UsuarioVO usuario) throws Exception {
        for (HistoricoVO historicoReprovacao : gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno()) {
            // iremos processar todos os históricos que o aluno está reprovado e que temos
            // que migrar o mesmo para nova grade
            if (historicoReprovacao.getReprovado()) {
            	if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoReprovacao, transferencia, matriculaProcessar, false, usuario) ) {
	                gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar,
	                        gradeCurricularGrupoOptativaDisciplinaCorrespondenteVO, periodoLetivoAdotarVO, historicoReprovacao, false, usuario);
            	}
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricosReprovacaoGradeCurricularGrupoOptativaDisciplinaCorrespodente(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondenteVO,
            PeriodoLetivoVO periodoLetivoAdotarVO,
            UsuarioVO usuario) throws Exception {
        for (HistoricoVO historicoReprovacao : gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricosAluno()) {
            // iremos processar todos os históricos que o aluno está reprovado e que temos
            // que migrar o mesmo para nova grade
            if (historicoReprovacao.getReprovado()) {
            	if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoReprovacao, transferencia, matriculaProcessar, false, usuario) ) {
	                gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar,
	                        gradeCurricularGrupoOptativaDisciplinaCorrespondenteVO, periodoLetivoAdotarVO, historicoReprovacao, false, usuario);
            	}
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricosReprovacaoGradeCurricularGrupoOptativaDisciplinaCorrespodente(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO,
            GradeDisciplinaVO gradeDisciplinaCorrespondenteVO,
            PeriodoLetivoVO periodoLetivoAdotarVO,
            UsuarioVO usuario) throws Exception {
        for (HistoricoVO historicoReprovacao : gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricosAluno()) {
            // iremos processar todos os históricos que o aluno está reprovado e que temos
            // que migrar o mesmo para nova grade
            if (historicoReprovacao.getReprovado()) {
            	if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoReprovacao, transferencia, matriculaProcessar, false, usuario) ) {
            		gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaCorrespondenteVO, historicoReprovacao, false, usuario);
            	}
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente,
            PeriodoLetivoVO periodoLetivoGerarHistoricoVO,
            HistoricoVO historicoOrigemAluno,
            Boolean historicoParaRefletirCorrespondenciaGradeOrigem,
            UsuarioVO usuario) throws Exception {
        HistoricoVO novoHistorico = (HistoricoVO) historicoOrigemAluno.clone();
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao
        if ((historicoParaRefletirCorrespondenciaGradeOrigem) &&
            (historicoOrigemAluno.getCursando() || historicoOrigemAluno.getCursandoPorEquivalencia())) {
            novoHistorico.setSituacao(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor());
        }
        PeriodoLetivoVO periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(historicoOrigemAluno.getPeriodoLetivoCursada().getPeriodoLetivo());
        inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, periodoLetivoCursar, null, usuario);

        novoHistorico.setDisciplina(gradeCurricularGrupoOptativaDisciplinaCorrespondente.getDisciplina());
        // atualizar o historico para a nova gradeCurricularGrupoOptativaDisciplina
        novoHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaCorrespondente);
        novoHistorico.setDisciplinaReferenteAUmGrupoOptativa(Boolean.TRUE);

        String msgInicial = "Histórico migrado da matriz (" + transferencia.getGradeOrigem().getCodigo() + ") ";
        if (!historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
            // se o historico utilizado como base era de uma GradeDisciplina, temos que
            // redefinir esta situação, pois o mesmo está sendo vinculado a uma gradeDisciplina
            // na gradeDestino
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " referia-se à Disciplina Convencional (Grade Disciplina) " + novoHistorico.getGradeDisciplinaVO().getDisciplina().getNome() + "(" + historicoOrigemAluno.getGradeDisciplinaVO().getDisciplina().getCodigo() + ") - " + historicoOrigemAluno.getGradeDisciplinaVO().getCargaHoraria() + "h", true);
            novoHistorico.setGradeDisciplinaVO(null);
        } else {
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " referia-se à Disciplina Grupo Optativa " + novoHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getNome() + "(" + historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo() + ") - " + historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() + "h", true);
        }

        // se a disciplina foi estudada por equivalencia, esta informação não será gerada
        // na nova grade, pois a mesma já está aprovada e portanto a questão da equivalencia
        // não é mais crítica na nova grade e adicionalmente, os mapas de equivalencia 
        // da grade origem não são válidos para a grade destino, por isto precisam ser zerados.
        if (novoHistorico.getHistoricoPorEquivalencia()) {
            // Registrando observacao caso o historico da gradeOrigem tenha sido estudado por equivalencia
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " foi cursado por equivalência. Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        if (novoHistorico.getHistoricoEquivalente()) {
            // Registrando observacao caso o historico da gradeOrigem era equivalente (ou seja, fora grade)
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " foi cursado como equivalente (ou seja, não pertencia a esta matriz). Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        novoHistorico.setHistoricoEquivalente(Boolean.FALSE);
        novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
        novoHistorico.setMapaEquivalenciaDisciplina(null);
        novoHistorico.setMapaEquivalenciaDisciplinaCursada(null);
        novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(null);

//      Comentado por Edigar A. Diniz Junior - esta operacao sera realizada por metodo especifico para tratar composicoes
//        if (historicoOrigemAluno.getHistoricoDisciplinaComposta()) {
//            boolean disciplinaDestinoEComposta = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getDisciplinaComposta();
//            boolean composicaoMantidaDaGradeOrigemParaGradeDestino = verificarComposicaoGradeOrigemFoiMantidaGradeDestino(null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, historicoOrigemAluno, usuario);
//            String complementoObservacao = "";
//            if ((disciplinaDestinoEComposta) && (composicaoMantidaDaGradeOrigemParaGradeDestino)) {
//                complementoObservacao = "Históricos das disciplinas que fazem parte da composição foram migrados para nova Matriz Curricular";
//                //TODO MIGRAR HISTORICOS DAS DISCIPLINAS QUE FAZEM PARTE DA COMPOSICAO...
//                gerarHistoricoDisiciplinasFazemParteComposicao(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, historicoOrigemAluno, usuario);
//            } else {
//                if (!disciplinaDestinoEComposta) {
//                    complementoObservacao = "Históricos das disciplinas que fazem parte da composição não foram migrados para nova Matriz, pois a disciplina correspondente na Matriz Destino não é composta";
//                } else {
//                    complementoObservacao = "Históricos das disciplinas que fazem parte da composição não foram migrados para nova Matriz, pois a disciplina correspondente na Matriz Destino não possui a mesma composição. Logo o aluno ficará aprovado na disciplina composta, na nova Matriz, contudo, sem históricos para sua composição.";
//                }
//            }
//            // Registrando observacao caso o historico da gradeOrigem era uma disciplina composta
//            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico Migrado da Matriz Curricular " + transferencia.getGradeOrigem().getNome() + "(" + transferencia.getGradeOrigem().getCodigo() + ") era de uma disciplina composta. " + complementoObservacao, true);
//        }
        if (historicoOrigemAluno.getHistoricoDisciplinaFazParteComposicao()) {
            // Se o historico origem fazia parte de uma composicao e está sendo processado
            // neste método, então é por que na matriz nova, o mesmo não será mais integrante
            // de uma composição, mas sim, passará a ser considerado como um histórico
            // convencional. Isto pode ocorrer, pois na nova matriz, pode haver uma disciplina
            // na grade (ou no grupo de optativas) com as mesmas características da disciplina
            // que fazia parte da composição.
            novoHistorico.setGradeDisciplinaComposta(null);
            novoHistorico.setHistoricoDisciplinaFazParteComposicao(Boolean.FALSE);

            // Registrando observacao caso o historico da gradeOrigem fazia parte de uma composição
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico Migrado da Matriz Curricular " + transferencia.getGradeOrigem().getNome() + "(" + transferencia.getGradeOrigem().getCodigo() + ") fazia parte de uma composição, contudo, foi migrado como histórico de uma Disciplina Regular da nova Matriz. ", true);
        }

        // gravando novo histórico
        getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        if(historicoOrigemAluno.getAprovado() || novoHistorico.getReprovado()) {
    		getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoCursandoTransferenciaMatrizCurricularMatricula(historicoOrigemAluno.getCodigo(), matriculaProcessar.getCodigo(), historicoOrigemAluno.getSituacao(), true, usuario);
    	}
        matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
    }

    /**
     * Método responsavel por dado um historico de uma GradeDisciplina no qual o alnuo foi
     * aprovado na gradeOrigem, procurar por uma GradeDisciplina correspondente (priorizando
     * o mesmo periodo letivo) na gradeDestino. Se encontrar o mesmo irá gerar o histórico
     * correspondente na gradeDestino. Caso nao encontre uma GradeDisciplia correspondente
     * este método também irá procurar por uma GradeCurricularGrupoOptativaDisciplinaVO
     * correspondente. Garantindo assim, o máximo de aproveitamento direto no momento da 
     * transferencia. O método também irá buscar por uma GradeCurricularGrupoOptativaDisciplinaVO
     * priorizando o período letivo da GradeDisciplina aprovada na grade origem.
     * @param matriculaProcessar
     * @param gradeDestino
     * @param listaHistoricosAprovadosNaoMapeadosGradeDestino
     * @param usuario 
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void verificandoEGerandoHistoricoGradeDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
            UsuarioVO usuario) throws Exception {
        // Iremos percorrer todos os periodos letivos do aluno na matriz curricular origem.
        // Para cada disciplina encontrada, iremos pegar seu historico atual (que refere-se ao historico
        // que deve ser considerado para uma transferencia - pode haver históricos de reprovação anteriores, que
        // não são úteis no ato da transferencia de matriz) e iremos gerar (caso seja correto) um novo histórico
        // na vinculado a nova matriz curricular (gradeDestino).

        for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()) {
            // Verificando as GradeDisciplina do periodoLetivo que estão aprovadas
            for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeDisciplinaComHistoricoAlunoVOs()) {
                // Iremos agora obter uma disciplina correspodente na grade destino para gerar um novo histórico
                
                GradeDisciplinaVO gradeDisciplinaCorrespondente = gradeDestino.obterGradeDisciplinaCorrespondente(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplina().getCodigo(), gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getCargaHoraria());
                if (gradeDisciplinaCorrespondente != null) {
//                	if (!gradeDisciplinaCorrespondente.getDisciplinaComposta()) {
                		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, usuario)) {
                			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
                			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
                			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
                			boolean historicoReprovadoGeradoMatrizDestino = false;
                			if (gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado() || gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getReprovado()) {
                				// se o histórico atual (que é o que determina a situação real do aluno) está aprovado
                				// então temos que gerar o histórico correspondente de aprovação na grade destino.
                				if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), transferencia, matriculaProcessar, gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado(), usuario) ) {
                					gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), false, usuario);
                					historicoReprovadoGeradoMatrizDestino = true;
                				}
                			}
                			// Independetemenet do aluno estar aprovado ou nao temos que mover todos os históricos de reprovacao do aluno
                			if (!historicoReprovadoGeradoMatrizDestino) {
                				gerarHistoricosReprovacaoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaComHistoricoAlunoVO, usuario, gradeDisciplinaCorrespondente);
                			}
                		}else {
                			// Se entrarmos aqui é por que na matriz destino a disciplina correspodente está aprovada. como não foi gerado um novo Historico
                			// faremos um update para alterar o codigo de transferencia do historico.
                			PeriodoLetivoComHistoricoAlunoVO periodoLetivoDestinoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(gradeDisciplinaCorrespondente.getPeriodoLetivoVO().getCodigo());
                            HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoDestinoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplinaCorrespondente.getCodigo());
                			getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(historicoDisciplinaGradeDestino.getCodigo(), matriculaProcessar.getCodigo(), Boolean.FALSE, usuario);
                			getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getCodigo(), matriculaProcessar.getCodigo(), Boolean.TRUE, usuario);
                		}
//                	} else {
//                		// Caso a disciplina seja composta, entao vamos trata-se no metodo apropriado para composicao. Pois no caso, da composicao
//                		// a resolucao da disciplina mae pode envolver inclusive disciplinas que estao sendo cursadas atualmente pelo aluno.
//                		// Desta maneira, iremos preservar este histórico para ser processado posteriormente no método correto.
//                		matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());                		
//                	}
                } else {
                    // Se nao entramos a gradeDisciplina correspondente, iremos buscar por uma disciplina
                    // de um grupoOptativa que possa ser correspondente ao mesmo
                    GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = gradeDestino.obterGradeCurricularGrupoOptativaCorrespondente(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO());
                    if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
                    	if (!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinaComposta()) {
                    		
                    		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
                    			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
                    			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
                    			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
                    			boolean historicoReprovadoGeradoMatrizDestino = false;
                    			if (gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado() || gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getReprovado()) {
                    				// se o histórico atual (que é o que determina a situação real do aluno) está aprovado
                    				// então temos que gerar o histórico correspondente de aprovação na grade destino.
                    				if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), transferencia, matriculaProcessar, gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado(), usuario) ) {
                    					gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), false, usuario);
                    				}
                    			}
                    			// Independetemenet do aluno estar aprovado ou nao temos que mover todos os históricos de reprovacao do aluno
                    			if (!historicoReprovadoGeradoMatrizDestino) {
                    				gerarHistoricosReprovacaoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaComHistoricoAlunoVO, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), usuario);
                    			}
                    		}
                    	} else {
                    		// Caso a disciplina seja composta, entao vamos trata-se no metodo apropriado para composicao. Pois no caso, da composicao
                    		// a resolucao da disciplina mae pode envolver inclusive disciplinas que estao sendo cursadas atualmente pelo aluno.
                    		// Desta maneira, iremos preservar este histórico para ser processado posteriormente no método correto.
                    		matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());                		
                    	}
                    } else {
                        matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
                    }
                }
            }
        }
    }	

    /**
     * Método responsavel por dado um historico de uma GradeCurricularGrupoOptativaDisciplina no qual o alnuo foi
     * aprovado na gradeOrigem, procurar por uma GradeCurricularGrupoOptativaDisciplina correspondente (priorizando
     * o mesmo periodo letivo) na gradeDestino. Se encontrar o mesmo irá gerar o histórico
     * correspondente na gradeDestino. Caso nao encontre uma GradeCurricularGrupoOptativaDisciplina correspondente
     * este método também irá procurar por uma GradeDisciplina
     * correspondente. Garantindo assim, o máximo de aproveitamento direto no momento da 
     * transferencia. O método também irá buscar priorizando o período letivo do historico Origem
     * @param matriculaProcessar
     * @param gradeDestino
     * @param listaHistoricosAprovadosNaoMapeadosGradeDestino
     * @param usuario 
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void verificandoEGerandoHistoricoGradeCurricularGrupoOptativaDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
            UsuarioVO usuario) throws Exception {
        // Iremos percorrer todos os periodos letivos do aluno na matriz curricular origem.
        // Para cada disciplina encontrada de um GradeCurricularGrupoOptativaDisciplina, iremos pegar seu historico atual 
        // (que refere-se ao historico que deve ser considerado para uma transferencia - pode haver históricos de reprovação anteriores, que
        // não são úteis no ato da transferencia de matriz) e iremos gerar (caso seja correto) um novo histórico
        // na vinculado a nova matriz curricular (gradeDestino).
        for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()) {
            // Verificando as GradeCurricularGrupoOptativaDisciplina do periodoLetivo que estão aprovadas
            for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
                // Obtendo uma disciplina correspodente na grade destino e gerar um novo histórico
                GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = gradeDestino.obterGradeCurricularGrupoOptativaCorrespondente(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaVO(), periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getPeriodoLetivo());
                if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
                	if (!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinaComposta()) {
                		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
                			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
                			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
                			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
                			boolean historicoReprovadoGeradoMatrizDestino = false;
                			if (gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado() || gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getReprovado()) {
                				if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), transferencia, matriculaProcessar, false, usuario) ) {
                					gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), false, usuario);
                				}
                			}
                			// independente se o aluno estava aprovado ou não temos que gerar na grade destino os históricos de reprovacao
                			if (!historicoReprovadoGeradoMatrizDestino) {
                				gerarHistoricosReprovacaoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), usuario);
                			}
                		}
                	} else {
                		// Caso a disciplina seja composta, entao vamos trata-se no metodo apropriado para composicao. Pois no caso, da composicao
                		// a resolucao da disciplina mae pode envolver inclusive disciplinas que estao sendo cursadas atualmente pelo aluno.
                		// Desta maneira, iremos preservar este histórico para ser processado posteriormente no método correto.
                		matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getHistoricoAtualAluno());                		
                	}
                } else {
                    // Se nao entramos a GradeCurricularGrupoOptativaDisciplinaVO correspondente, iremos buscar por uma disciplina
                    // de um gradeDisciplina que possa ser correspondente ao mesmo
                    GradeDisciplinaVO gradeDisciplinaVOCorrespondente = gradeDestino.obterGradeDisciplinaCorrespondente(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaVO(), periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getPeriodoLetivo());
                    if (gradeDisciplinaVOCorrespondente != null) {
                    	if (!gradeDisciplinaVOCorrespondente.getDisciplinaComposta()) {
                    		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaVOCorrespondente, null, usuario)) {
                    			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
                    			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
                    			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
                    			boolean historicoReprovadoGeradoMatrizDestino = false;
                    			if (gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado() || gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getReprovado()) {
                    				if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), transferencia, matriculaProcessar, false, usuario) ) {
                    					gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaVOCorrespondente, gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), false, usuario);
                    				}
                    			}
                    			// independente se o aluno estava aprovado ou não temos que gerar na grade destino os históricos de reprovacao
                    			if (!historicoReprovadoGeradoMatrizDestino) {
                    				gerarHistoricosReprovacaoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO, gradeDisciplinaVOCorrespondente, periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), usuario);
                    			}
                    		}
                    	} else {
                    		// Caso a disciplina seja composta, entao vamos trata-se no metodo apropriado para composicao. Pois no caso, da composicao
                    		// a resolucao da disciplina mae pode envolver inclusive disciplinas que estao sendo cursadas atualmente pelo aluno.
                    		// Desta maneira, iremos preservar este histórico para ser processado posteriormente no método correto.
                    		matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(gradeDisciplinaVOCorrespondente.getHistoricoAtualAluno());                		
                    	}
                    } else {
                        matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
                    }
                }
            }
        }
    }

    /**
     * Método responsavel por percorrer historicos fora da grade origem do aluno, que estão 
     * como situação de aprovação, que podem de alguma forma ser aproveitados na grade
     * destino (para a qual o aluno está sendo migrado). Para cada histórico aprovado e
     * fora da grade Origem, iremos buscar uma correspodencia na grade destino. Caso a 
     * correspondencia direta nao exista, este histórico fica na lista de nao aproveitados
     * diretamente, para serem verificados do ponto de vista de mapa de equivalencia.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void verificandoEGerandoHistoricosForaGradeAprovadosGradeOrigemPorCorrespodenciaDireta(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
            UsuarioVO usuario) throws Exception {
        // Iremos percorrer todos os históricos fora da grade origem para processamento
        // Tendem a ser históricos que o aluno está estudando como equivalente, para pagar outra
        // disciplina de sua matriz (mapa de equivalencia). Porém neste método iremos avaliar se o
        // historico em questõa na gradeDestino não pode ser aproveitado de forma direta - por correspodencia.
        List<HistoricoVO> historicosForaGradeOrigem = matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasForaGradeCurricular();
        for (HistoricoVO historicoForaGradeOrigem : historicosForaGradeOrigem) {
            if (historicoForaGradeOrigem.getAprovado()) {
                // primeiro vamos tentar obter uma gradeDisciplina na grade destino, no qual o histórico possa ser vinculado
                GradeDisciplinaVO gradeDisciplinaCorrespondente = gradeDestino.obterGradeDisciplinaCorrespondente(historicoForaGradeOrigem.getDisciplina().getCodigo(), historicoForaGradeOrigem.getCargaHorariaDisciplina());
                if (gradeDisciplinaCorrespondente != null) {
                	if (!gradeDisciplinaCorrespondente.getDisciplinaComposta()) {
                		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, usuario)) {
                			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
                			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
                			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
                		
                			// se encontramos então vamos gerar o histórico na gradeDestino vinculado a esta gradeDisciplina encontrada.
                			gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, historicoForaGradeOrigem, false, usuario);
                		}
                	} else {
                		// disciplinas compostas serao tratadas no metodo apropriado para este fim
                        matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(historicoForaGradeOrigem);
                	}
                } else {
                    GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = gradeDestino.obterGradeCurricularGrupoOptativaCorrespondente(historicoForaGradeOrigem.getDisciplina().getCodigo(), historicoForaGradeOrigem.getCargaHorariaDisciplina());
                    if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
                    	if (!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinaComposta()) {

                    		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
                    			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
                    			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
                    			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
                    		
                    			// se encontramos então vamos gerar o histórico na gradeDestino vinculado a esta gradeDisciplina encontrada.
                    			gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), historicoForaGradeOrigem, false, usuario);
                    		}
                    	} else {
                    		// disciplinas compostas serao tratadas no metodo apropriado para este fim
                            matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(historicoForaGradeOrigem);
                    	}
                    } else {
                        matriculaProcessar.adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(historicoForaGradeOrigem);
                    }
                }
            }
        }
    }

    /**
     * Método responsável por verificar se um determinado mapa pode ser aplicado para um aluno na
     * gradeDestino (para a qual o mesmo está sendo migrado). Ele verifica se as disciplinas que
     * o mapa referencia da matriz, ainda estão pendentes pelo aluno, na grade destino. Caso contrário, o mapa
     * não pode ser adotado mais para este alnuo.
     */
    private boolean verificarAlunoAptoParaCursarDeterminadoMapaEquivalencia(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaCorrespondente,
            HistoricoVO historicoAproveitarPorEquivalencia,
            UsuarioVO usuario) throws Exception {
        for (MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinasMatriz : mapaEquivalenciaCorrespondente.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
            for (HistoricoVO historivoAproveitado : matriculaProcessar.getHistoricoAproveitadosTransferencia()) {
                // se alguma disciplina da matriz referenciada neste mapa, já está como aprovada (já existe um histórico
                // de aprovação para a mesma), significa que o este mapa não poderá ser utilizado mais para o aluno na grade destino.
                if (historivoAproveitado.getDisciplina().getCodigo().equals(disciplinasMatriz.getDisciplinaVO().getCodigo())) {
                    return false;
                }
            }
            
            GradeDisciplinaVO gradeDisciplinaCorrespondente = transferencia.getGradeMigrar().obterGradeDisciplinaCorrespondente(disciplinasMatriz.getDisciplinaVO().getCodigo(), disciplinasMatriz.getCargaHoraria());
            if (gradeDisciplinaCorrespondente != null) {
                if (!verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, usuario)) {
                	// Caso o histórico já seja aproveitado na matriz destino devemos adicionar o mesmo na lista de históricos aproveitados
                	matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoAproveitarPorEquivalencia);
                	// se a encontramos a gradeDisciplina na gradeDestino e a mesma já tem um historico de aprovacao vinculada a ela, significa que 
                	// podemos mais utiliar este mapa. Este teste é diferente do teste acima. Na validação anterior estamos observando se já foi gerado
                	// um historico durante esta execucao de transferencia que possa impedir o mape de ser utilizado. Aqui já estamos verificando se o 
                	// aluno já nao tinha um historico velho (caso ela tenha cursado esta grade anteriormente, mudado de grade e retornado).
                	return false;
                }            	
            } else {
                GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = transferencia.getGradeMigrar().obterGradeCurricularGrupoOptativaCorrespondente(disciplinasMatriz.getDisciplinaVO().getCodigo(), disciplinasMatriz.getCargaHoraria());
                if (!verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
                	// Caso o histórico já seja aproveitado na matriz destino devemos adicionar o mesmo na lista de históricos aproveitados
                	matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoAproveitarPorEquivalencia);
                	// se a encontramos a gradeDisciplina na gradeDestino e a mesma já tem um historico de aprovacao vinculada a ela, significa que 
                	// podemos mais utiliar este mapa. Este teste é diferente do teste acima. Na validação anterior estamos observando se já foi gerado
                	// um historico durante esta execucao de transferencia que possa impedir o mape de ser utilizado. Aqui já estamos verificando se o 
                	// aluno já nao tinha um historico velho (caso ela tenha cursado esta grade anteriormente, mudado de grade e retornado).
                	return false;
                }            	
            }
            
            // verificando se a disciplina em questao nao é filha de uma composicao. Caso seja, a mesma tem que ser aproveitada
            // dentro do metodo que trata de composicao. Pois, disciplinas compostas tem que ter todas as suas filhas aproveitadas / 
            // aprovadas para que a mae seja atendida. Nao podemos ter a transferencia e/ou aproveitamento de somente parte das filhas
            Boolean disciplinaFazParteComposicao = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorCodigoDisciplinaECargaHorariaDisciplinaFazParteComposicao(disciplinasMatriz.getDisciplinaVO().getCodigo(), transferencia.getGradeMigrar().getCodigo(), usuario);
            if (disciplinaFazParteComposicao) {
            	return false;
            }
        }
        return true;
    }

    private int obterNrDisciplinasCursarMapaEquivalenciaAlunoAprovado(TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, MapaEquivalenciaDisciplinaVO mapaDisponivelEAplicavel) {
        int nrDisciplinasCursarAlunoJaAprovado = 0;
        for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursar : mapaDisponivelEAplicavel.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            for (HistoricoVO historicoVO : matriculaProcessar.getHistoricoAproveitadosTransferencia()) {
                if ((disciplinaCursar.getDisciplinaVO().getCodigo().equals(historicoVO.getDisciplina().getCodigo()))
                        && (disciplinaCursar.getCargaHoraria().equals(historicoVO.getCargaHorariaDisciplina()))) {
                    nrDisciplinasCursarAlunoJaAprovado++;
                }
            }
        }
        return nrDisciplinasCursarAlunoJaAprovado;
    }

    private int obterNrDisciplinasCursarMapaEquivalenciaAlunoEstuandoAtualmente(TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, MapaEquivalenciaDisciplinaVO mapaDisponivelEAplicavel) {
        int nrDisciplinasCursarAlunoEstaEstudando = 0;
        for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursar : mapaDisponivelEAplicavel.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            List<HistoricoVO> disciplinasAlunoCursando = matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular();
            for (HistoricoVO historicoVO : disciplinasAlunoCursando) {
                if ((disciplinaCursar.getDisciplinaVO().getCodigo().equals(historicoVO.getDisciplina().getCodigo()))
                        && (disciplinaCursar.getCargaHoraria().equals(historicoVO.getCargaHorariaDisciplina()))) {
                    nrDisciplinasCursarAlunoEstaEstudando++;
                }
            }
        }
        return nrDisciplinasCursarAlunoEstaEstudando;
    }

    /**
     * Método responsável por obter uma Mapa de Equivalencia que possa ser utilizado de forma a aproveitar
     * um histórico fora da grade que o aluno já foi aprovado na grade origem. Para que uma mapa possa ser
     * utilizado, é necessário que alguma disciplina a ser cursada (do mapa) já tenha sido estuda pelo aluno
     * (iremos utilizar o historicoAproveitarPorEquivalencia para fazer esta verificação) e que todas as disciplinas
     * do mapa da matriz destino (que serão estudas por equivalencia) ainda estejam pendentes para o aluno
     * na matriz destino. Ou seja, o aluno não pode estar aprovado em uma disciplina que o mapa faz referencia
     * que deve ser estuda por equivalencia. Este método já possui uma lógica para determinar qual o melhor mapa
     * a ser aplicado para o aluno em uma determinada disciplina (historicoAproveitarPorEquivalencia). Ele
     * utiliza as seguintes regras de prioridade:
     *    a) primeiro verifica se disciplina em avaliação já não estava sendo estuda em um mapa na grade origem.
     *       Se sim, procura por um mapa identico na grade destino, caso exista, este mapa será utilziado, para 
     *       manter o alnuo com a sensação de que nada foi alterado na sua vida academica.
     *    b) Caso a regra acima nao se aplica o sistema irá buscar pelo mapa que possa ser resolvido por completo
     *       e que tenha mais disciplinas envolvidas.
     *    c) Caso ainda não encontre um mapa resolvido, então o sistema irá buscar pelo mapa que tenha mais disciplinas
     *       resolvidas, dado os históricos aproveitados para o aluno na grade destino.
     */
    private MapaEquivalenciaDisciplinaVO obterMapaEquivalenciaAplicavelDisciplinaAprovadaGradeOrigem(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            HistoricoVO historicoAproveitarPorEquivalencia,
            UsuarioVO usuario) throws Exception { 
        List<MapaEquivalenciaDisciplinaVO> listaMapasValidos =
                getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
                transferencia.getGradeMigrar().getCodigo(), historicoAproveitarPorEquivalencia.getDisciplina().getCodigo(),
                historicoAproveitarPorEquivalencia.getCargaHorariaDisciplina(), transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo(), NivelMontarDados.TODOS);
        if (listaMapasValidos.isEmpty()) {
            return null;
        }

        if ((historicoAproveitarPorEquivalencia.getHistoricoEquivalente())
                && (!historicoAproveitarPorEquivalencia.getMapaEquivalenciaDisciplina().getCodigo().equals(0))) {
            // se o historico a ser aproveitado por equivalencia, tambem faz uso de uma mapa de equivalencia
            // na gradeOrigem então temos que tentar priorizar que este mesmo mapa na grade destino. ou seja,
            // temos que verificar se algum dos mapas disponíveis na gradeDestino é compatível com o mapa
            // em que o aluno já está envolvido (isto é uma tendência, haja vista que mapas de equivalencia e 
            // e matriz curriculares geralmente são clonadas e modificadas pontualmente). Caso sim, então este
            // mapa deve ser priorizado perante aos demais.
            MapaEquivalenciaDisciplinaVO mapaCarregadoVO = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(historicoAproveitarPorEquivalencia.getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS);
            historicoAproveitarPorEquivalencia.setMapaEquivalenciaDisciplina(mapaCarregadoVO);
            // obtendo mapa equivalente comparando codigos das disciplinas e carga horária das mesmas
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaCorrespondente = historicoAproveitarPorEquivalencia.getMapaEquivalenciaDisciplina().obterMapaEquivalenteDisciplina(listaMapasValidos);
            if (mapaEquivalenciaCorrespondente != null) {
                if (verificarAlunoAptoParaCursarDeterminadoMapaEquivalencia(transferencia, matriculaProcessar, mapaEquivalenciaCorrespondente, historicoAproveitarPorEquivalencia, usuario)) {
                    return mapaEquivalenciaCorrespondente;
                }
            }
        }

        List<MapaEquivalenciaDisciplinaVO> listaMapasAplicaveisParaAluno = new ArrayList<MapaEquivalenciaDisciplinaVO>();
        // se chegarmos aqui é por que não foi encontrado nenhum mapa de equivalencia que correspondente
        // na matriz destino (migrar). Logo, que temos que verificar quais dos mapas encontrados pode
        // ser aplicado para o aluno.
        for (MapaEquivalenciaDisciplinaVO mapasDisponiveis : listaMapasValidos) {
            if (verificarAlunoAptoParaCursarDeterminadoMapaEquivalencia(transferencia, matriculaProcessar, mapasDisponiveis, historicoAproveitarPorEquivalencia, usuario)) {
                // se o mapa é aplicável vamos adicionar o mesmo para uma lista. Posteriomente, iremos processar
                // esta lista para determinar qual destes mapas válidos é o melhor a ser adotado 
                // para o aluno na nova grade. Será priorizado o mapa que for resolvido por completo,
                // ou que tiver mais disciplinas resolvidas.
                listaMapasAplicaveisParaAluno.add(mapasDisponiveis);
            }
        }

        MapaEquivalenciaDisciplinaVO melhorMapaAplicar = null;
        Integer nrDisciplinasCursarMelhorMapaAlunoJaAprovado = 0;
        Integer nrDisciplinasCursarMelhorMapaAlunoEstaEstudandoAtualmente = 0;
        boolean melhorMapaResolvido = false;
        // Neste ponto iremos verificar qual é o melhor mapa a ser aplicado para o aluno da
        // lista de mapas aplicaveis
        for (MapaEquivalenciaDisciplinaVO mapaDisponivelEAplicavel : listaMapasAplicaveisParaAluno) {
            if (melhorMapaAplicar == null) {
                // na primeira interacao assumimos o primeiro mapa como sendo o melhor
                // para o aluno
                melhorMapaAplicar = mapaDisponivelEAplicavel;
                // atualizando estatísticas sobre melhor mapa, no caso ja somamos 1 na lista, pois este mapa
                // foi obtido com base em uma disciplina que está fora da grade destino, mas que existe um mapa
                // correspondente para ele. 
                nrDisciplinasCursarMelhorMapaAlunoJaAprovado = 1 + obterNrDisciplinasCursarMapaEquivalenciaAlunoAprovado(matriculaProcessar, mapaDisponivelEAplicavel);
                nrDisciplinasCursarMelhorMapaAlunoEstaEstudandoAtualmente = obterNrDisciplinasCursarMapaEquivalenciaAlunoEstuandoAtualmente(matriculaProcessar, mapaDisponivelEAplicavel);
                melhorMapaResolvido = (nrDisciplinasCursarMelhorMapaAlunoJaAprovado == mapaDisponivelEAplicavel.getMapaEquivalenciaDisciplinaCursadaVOs().size());
            } else {
                // na segunda interacao passamos a comparar o mapa da interacao com o melhor registrado
                // ate o momento. Caso o mapa atual seja melhor, o mesmo passará a ser condirado como 
                // o melhor para o aluno.

                // PRIMEIRO - obtemos informacoes mapaInteracao para compara-lo com o melhor mapa até o momento
                int nrDisciplinasCursarMapaAvaliarAlunoJaAprovado = 1 + obterNrDisciplinasCursarMapaEquivalenciaAlunoAprovado(matriculaProcessar, mapaDisponivelEAplicavel);
                int nrDisciplinasCursarMapaAvaliarAlunoEstaEstudandoAtualmente = obterNrDisciplinasCursarMapaEquivalenciaAlunoEstuandoAtualmente(matriculaProcessar, mapaDisponivelEAplicavel);
                boolean mapaAvaliarResolvido = (nrDisciplinasCursarMapaAvaliarAlunoJaAprovado == mapaDisponivelEAplicavel.getMapaEquivalenciaDisciplinaCursadaVOs().size());
                if (melhorMapaResolvido) {
                    // SEGUNDO - neste caso o mapa da interacao só vai ser considerado melhor se o mesmo
                    // tambem estiver relvido e tiver mais disciplinas aprovados que o primeiro
                    if ((mapaAvaliarResolvido)
                            && (nrDisciplinasCursarMapaAvaliarAlunoJaAprovado > nrDisciplinasCursarMelhorMapaAlunoJaAprovado)) {
                        nrDisciplinasCursarMelhorMapaAlunoJaAprovado = nrDisciplinasCursarMapaAvaliarAlunoJaAprovado;
                        nrDisciplinasCursarMelhorMapaAlunoEstaEstudandoAtualmente = nrDisciplinasCursarMapaAvaliarAlunoEstaEstudandoAtualmente;
                        melhorMapaResolvido = mapaAvaliarResolvido;
                        melhorMapaAplicar = mapaDisponivelEAplicavel;
                    }
                } else {
                    // TERCEIRO - caso o melhor mapa não esteje resolvido, o mapa de interacao será considerado 
                    // melhor somente se o nr de disciplinas aprovadas for igual ou maior e o numero 
                    // de disciplinas que o aluno esta cursando for maior
                    if ((nrDisciplinasCursarMapaAvaliarAlunoJaAprovado >= nrDisciplinasCursarMelhorMapaAlunoJaAprovado)
                            && (nrDisciplinasCursarMapaAvaliarAlunoEstaEstudandoAtualmente > nrDisciplinasCursarMelhorMapaAlunoEstaEstudandoAtualmente)) {
                        nrDisciplinasCursarMelhorMapaAlunoJaAprovado = nrDisciplinasCursarMapaAvaliarAlunoJaAprovado;
                        nrDisciplinasCursarMelhorMapaAlunoEstaEstudandoAtualmente = nrDisciplinasCursarMapaAvaliarAlunoEstaEstudandoAtualmente;
                        melhorMapaResolvido = mapaAvaliarResolvido;
                        melhorMapaAplicar = mapaDisponivelEAplicavel;
                    }
                }
            }
        }
        return melhorMapaAplicar;
    }

    /**
     * Este método varre as disciciplinas do mapa de equivalencia e encontra o primeiro periodo
     * letivo válida da matriz destino, para que este periodo letivo seja adotado para os 
     * históricos das disciplinas que serão aproveitados por equivalencia. Isto por que o periodo
     * letivo do historico a ser aproveitado na gradeDestino pode ser de um periodo letivo
     * que nao existia na gradeOrigem (ou mesmo nao é correspondente).
     */
    private PeriodoLetivoVO obterPeriodoLetivoEncaixarDisciplinaAproveitadaPorMapaEquivalencia(
            TransferenciaMatrizCurricularVO transferencia,
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
    	String disciplinaHistorico = "";
        for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaDisciplinaMatriz : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
        	disciplinaHistorico = mapaDisciplinaMatriz.getDisciplinaVO().getCodigo() +"-"+mapaDisciplinaMatriz.getDisciplinaVO().getNome();
        	GradeDisciplinaVO gradeDisciplina = transferencia.getGradeMigrar().obterGradeDisciplinaCorrespondente(mapaDisciplinaMatriz.getDisciplinaVO().getCodigo(), mapaDisciplinaMatriz.getCargaHoraria());
            if (gradeDisciplina != null) {
                return gradeDisciplina.getPeriodoLetivoVO();
            }
            GradeCurricularGrupoOptativaDisciplinaVO gradeGrupoOptativa = transferencia.getGradeMigrar().obterGradeCurricularGrupoOptativaCorrespondente(mapaDisciplinaMatriz.getDisciplinaVO().getCodigo(), mapaDisciplinaMatriz.getCargaHoraria());
            if (gradeGrupoOptativa != null) {
                return gradeGrupoOptativa.getPeriodoLetivoDisciplinaReferenciada();
            }
        }
        throw new Exception("Não foi possível determinar o período letivo na grade destino, para disciplina "+disciplinaHistorico+" ser aproveitada no histórico por equivalência.");
    }

    /*
     * Método que gerar o histórico para uma disciplina que faz parte de uma mapa de equivalencia, como cursando.
     * Por ter uma variação, quando o atributo mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina true
     * Atributo: mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina - se o mapa de equivalencia tem somente uma disciplina a ser cursada e somente uma disciplina
     * a ser aprovada na matriz destino, então já iremos gerar o histórico de forma direta na
     * gradeDestino. Isto por que não é necessário aplicar as regras do mapaEqualencia (nota por media, ou maior nota)
     * maior frequencia ou médias das frequencias, ...) Pois, temos somente uma disciplina, levando para outra.
     * isto implicará em melhor perfomance e em dados mais limpos na gradeDestino. Como este é o caso comum,
     * para a maioria das transferencia, também, teremos um ganho de performance considerável, pois não teremos
     * que resolver o mapa na matriz destino.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO,
            MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO,
            HistoricoVO historicoOrigemAluno,
            Boolean mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina,
            UsuarioVO usuario) throws Exception {
        HistoricoVO novoHistorico = (HistoricoVO) historicoOrigemAluno.clone();
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao
//        if (historicoOrigemAluno.getDisciplina().getCodigo().equals(418)) {
//            System.out.println("");
//        }
        PeriodoLetivoVO periodoMatrizCurricularVO = obterPeriodoLetivoEncaixarDisciplinaAproveitadaPorMapaEquivalencia(transferencia, mapaEquivalenciaDisciplinaVO);
        //if (historicoOrigemAluno.getHistoricoDisciplinaForaGrade()) {
            // como trata-se de um historico fora da grade, entao o periodoLetivoMatriz nao
            // deve ser modificado, pois trata-se de um periodo letivo que faz referencia
            // a grade que o aluno estudou a disciplina (outra grade diferente da origem).
        //    periodoMatrizCurricularVO = historicoOrigemAluno.getPeriodoLetivoMatrizCurricular();
        //}

        PeriodoLetivoVO periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(historicoOrigemAluno.getPeriodoLetivoCursada().getPeriodoLetivo());
        if ((periodoLetivoCursar == null) || (periodoLetivoCursar.getCodigo().equals(0))) {
            // caso nao encontrou um PeriodoLetivo da Matriz oficial do aluno correspondete, entao
            // adota-se o primeiro periodo letivo da nova matriz do aluno.
            periodoLetivoCursar = transferencia.getGradeMigrar().getPrimeiroPeriodoLetivoGrade();
        }

        inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, periodoMatrizCurricularVO, periodoLetivoCursar, usuario);

        
        if (!mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina) {
            // marcando como fora da grade, pois trata-se de um historico de uma disciplina cursada fora da grade
            // isto somente, quando formos gerar o histórico vinculado ao mapa de equivalencia. O que não é o caso
            // quando a variável mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina for true. Pois, neste caso,
            // geramos um novo históric de forma direta e apesar de usar o mapa de equivalencia para deduzir
            // o novo histórico, este mapa não será utilizado na gradeDestino.
            novoHistorico.setHistoricoDisciplinaForaGrade(Boolean.TRUE);
        } 

        if (mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina) {
            // se trata-se de uma equivalencia direta, então já temos como definir
            // a gradeDisciplina ou gradeCurricularGrupoOptatova com base na disciplina
            // da matrizcurricula da equivalencia. Assim já geramos o histórico 
            // apontando corretamtente para a mesma.
            MapaEquivalenciaDisciplinaMatrizCurricularVO mapaDisciplinaMatriz = mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().get(0);
            // obtendo a gradeDisciplina Corresponde na nova grade
            GradeDisciplinaVO gradeDisciplinaCorrespondente = transferencia.getGradeMigrar().obterGradeDisciplinaCorrespondente(mapaDisciplinaMatriz.getDisciplinaVO().getCodigo(), mapaDisciplinaMatriz.getCargaHoraria());
            if (gradeDisciplinaCorrespondente != null) {
                // se encontramos então vamos gerar o histórico na gradeDestino vinculado a esta gradeDisciplina encontrada.
            	novoHistorico.setDisciplina(gradeDisciplinaCorrespondente.getDisciplina());
                novoHistorico.setGradeDisciplinaVO(gradeDisciplinaCorrespondente);
                novoHistorico.setCargaHorariaDisciplina(gradeDisciplinaCorrespondente.getCargaHoraria());
                if (mapaEquivalenciaDisciplinaVO.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.CARGA_HORARIA_GRADE)) {
                    novoHistorico.setCargaHorariaCursada(gradeDisciplinaCorrespondente.getCargaHoraria());
                } else {
                    novoHistorico.setCargaHorariaCursada(historicoOrigemAluno.getCargaHorariaCursada());
                }
                if (mapaEquivalenciaDisciplinaVO.getEquivalenciaPorIsencao()) {
                    novoHistorico.setIsentarMediaFinal(Boolean.TRUE);
                }
            } else {
                GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = transferencia.getGradeMigrar().obterGradeCurricularGrupoOptativaCorrespondente(mapaDisciplinaMatriz.getDisciplinaVO().getCodigo(), mapaDisciplinaMatriz.getCargaHoraria());
                if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
                	novoHistorico.setDisciplina(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplina());
                    novoHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente);
                    novoHistorico.setCargaHorariaDisciplina(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCargaHoraria());
                    if (mapaEquivalenciaDisciplinaVO.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.CARGA_HORARIA_GRADE)) {
                        novoHistorico.setCargaHorariaCursada(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCargaHoraria());
                    } else {
                        novoHistorico.setCargaHorariaCursada(historicoOrigemAluno.getCargaHorariaCursada());
                    }
                    if (mapaEquivalenciaDisciplinaVO.getEquivalenciaPorIsencao()) {
                        novoHistorico.setIsentarMediaFinal(Boolean.TRUE);
                    }
                } else {
                    throw new Exception("Disciplina referenciada no mapa de equivalência (Matriz Curricular) não foi encontrada na própria matriz curricular.");
                }
            }
            
            novoHistorico.setHistoricoEquivalente(Boolean.FALSE);
            novoHistorico.setHistoricoPorEquivalencia(Boolean.TRUE);
            novoHistorico.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
            novoHistorico.setMapaEquivalenciaDisciplinaCursada(null);
            novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaDisciplinaMatriz);
            novoHistorico.setNumeroAgrupamentoEquivalenciaDisciplina(1);
        } else {
            //no caso contrário todas as regras da equivalencia serão processadas automaticamente
            //no método alterar/incluir do facade de historico.
            // CONSIDERACOES SOBRE GRADEDISCIPLINA / GRADECURRICULARGRUPOOPTATIVADISCIPLINA
            // Não iremos alterar as referencias para a gradeDisciplina/gradeDisciplinaGrupoOptativa
            // da nova Grade. Pois trata-se das seguintes possibilidades:
            //   a) Trata-se de um histórico que já foi estudado pelo aluno fora da grade, logo, a disciplina 
            //        que ele estudou nesta grade externa, deve ser mantida de forma intacta.
            //   b) Trata-se de um histórico que fazia parte da gradeOrigem, contudo, não existe na gradeDestino,
            //        logo o mesmo deverá ir para a gradeDestino como fora da grade. Repare que se na grade origem
            //        a disciplina era de grupo de optativa ou nao, isto continua inalterado.
            
        }
        
        
        String msgInicial = "Histórico migrado da matriz (" + transferencia.getGradeOrigem().getCodigo() + ") ";
        if (novoHistorico.getHistoricoPorEquivalencia()) {
            // Registrando observacao caso o historico da gradeOrigem tenha sido estudado por equivalencia
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " foi cursado por equivalência nesta matriz. Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        if (novoHistorico.getHistoricoEquivalente()) {
            // Registrando observacao caso o historico da gradeOrigem era equivalente (ou seja, fora grade)
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " foi cursado como equivalente (ou seja, não pertencia a esta matriz). Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        } else {
            // Registrando observacao que histórico na gradeOrigem estava dentro da grade 
            // mas que agora passou a ser considerado como fora da grade
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " passou a ser considerado como FORA da matriz curricular destino. Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }

        // Criando vinculos com os mapas de equivalencia.
        if (!mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina) {
            novoHistorico.setHistoricoEquivalente(Boolean.TRUE);
            novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
            novoHistorico.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
            novoHistorico.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);
            novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(null);
        } else {
            //novoHistorico.setHistoricoEquivalente(Boolean.FALSE);
           // novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
        }
        if (historicoOrigemAluno.getHistoricoDisciplinaComposta()) {
            String complementoObservacao = "Históricos das disciplinas que fazem parte da composição não foram migrados para nova Matriz, pois a disciplina correspondente na Matriz Destino não possui a mesma composição. Logo o aluno ficará aprovado na disciplina composta, na nova Matriz, contudo, sem históricos para sua composição.";
            // Registrando observacao caso o historico da gradeOrigem era uma disciplina composta
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " era de uma disciplina composta. " + complementoObservacao, true);
        }else{
        	novoHistorico.setHistoricoDisciplinaComposta(false);        	
        }
        if (historicoOrigemAluno.getHistoricoDisciplinaFazParteComposicao()) {
            // Se o historico origem fazia parte de uma composicao e está sendo processado
            // neste método, então é por que na matriz nova, o mesmo não será mais integrante
            // de uma composição, mas sim, passará a ser considerado como um histórico
            // convencional. Isto pode ocorrer, pois na nova matriz, pode haver uma disciplina
            // na grade (ou no grupo de optativas) com as mesmas características da disciplina
            // que fazia parte da composição.
            novoHistorico.setGradeDisciplinaComposta(null);
            novoHistorico.setHistoricoDisciplinaFazParteComposicao(Boolean.FALSE);
            // Registrando observacao caso o historico da gradeOrigem fazia parte de uma composição
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " fazia parte de uma composição na Matriz Curricular Origem, contudo, foi migrado como histórico de uma Disciplina de um Mapa de Equivalencia que se aplica a Matriz Curricular Destino.", true);
        }
        // Registrando o histórico gerado no mapa, pois este mapa ainda será avaliado
        // e aproveitado ainda mais do ponto de vistas de disciplinas que o aluno está 
        // cursando atualmente.
        mapaEquivalenciaDisciplinaCursadaVO.setHistorico(novoHistorico);
        // gravando novo histórico
        if (mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina) {        	
        	HistoricoVO novoHistorico2 = (HistoricoVO) historicoOrigemAluno.clone();
    		if (transferencia.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento() 
    				&& (novoHistorico2.getSituacao().equals(SituacaoHistorico.APROVADO.getValor())
    						|| novoHistorico2.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor()))) {
    			novoHistorico2.setSituacao(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor());
    		}
    		if (transferencia.getUtilizarAnoSemestreAtualDisciplinaAprovada() 
    				&& (novoHistorico2.getSituacao().equals(SituacaoHistorico.APROVADO.getValor()) 
    						|| novoHistorico2.getSituacao().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor())
    						|| novoHistorico2.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor()))) {
    			novoHistorico2.setAnoHistorico(transferencia.getAnoDisciplinaAprovada());
    			novoHistorico2.setSemestreHistorico(transferencia.getSemestreDisciplinaAprovada());
    		}
        	novoHistorico2.setMatrizCurricular(transferencia.getGradeMigrar());
        	novoHistorico2.setHistoricoEquivalente(Boolean.TRUE);
        	novoHistorico2.setHistoricoPorEquivalencia(Boolean.FALSE);
        	novoHistorico2.setHistoricoDisciplinaForaGrade(Boolean.FALSE);
        	novoHistorico2.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
        	novoHistorico2.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);
        	novoHistorico2.setMapaEquivalenciaDisciplinaMatrizCurricular(null);
        	novoHistorico2.setNumeroAgrupamentoEquivalenciaDisciplina(1);
        	novoHistorico2.setHistoricoGeradoAPartirTransferenciaMatrizCurricular(Boolean.TRUE);
        	novoHistorico2.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        	getFacadeFactory().getHistoricoFacade().incluir(novoHistorico2, usuario);
        	getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(historicoOrigemAluno.getCodigo(), matriculaProcessar.getCodigo(), true, usuario);
        	
        }else {
        	getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        }
        ///
        getFacadeFactory().getHistoricoFacade().realizaExclusaoHistoricoDuplicadoMigracaoMatrizCurricular(novoHistorico.getMatricula().getMatricula(), novoHistorico.getMatrizCurricular().getCodigo(), novoHistorico.getTransferenciaMatrizCurricularMatricula().getCodigo(), mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs(), usuario);
        matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
    }

    /**
     * Dado um mapa de equivalencia já selecionado e um historico que já será utilizado para
     * abater uma das disciplinas a serem cursadas pelo aluno, este método irá gerar o histórico
     * correspondente na gradeDestino deste histórico fornecido como parametro (historicoNaoAproveitadoDiretamente) 
     * e também irá bucar por outros históricos no qual o aluno já tenha aproveitamento (e/ou ainda) está 
     * pendente para ser aproveitado, dentro do mesmo mapa de equivalencia. Para cada histórico aproveitado
     * o mesmo será registrado na lista que controla historicos nesta situação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricosGradeDestinoPorMapaEquivalencia(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO,
            HistoricoVO historicoNaoAproveitadoDiretamente, 
            Boolean mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, UsuarioVO usuario) throws Exception {
        MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaJaProcessado = null;
        if (historicoNaoAproveitadoDiretamente != null) {
            for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                if ((mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().equals(historicoNaoAproveitadoDiretamente.getDisciplina().getCodigo()))
                        && (mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria().equals(historicoNaoAproveitadoDiretamente.getCargaHorariaDisciplina()))) {
                    // primeiro vamos gerar o historico para a historicoNaoAproveitadoDiretamente, pois
                    // o mesmo ja foi identificado como valido e assim podemos remove-lo da lista de pendentes
                    mapaEquivalenciaDisciplinaCursadaJaProcessado = mapaEquivalenciaDisciplinaCursadaVO;
                    gerarHistoricoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(transferencia, matriculaProcessar, mapaEquivalenciaDisciplinaVO, mapaEquivalenciaDisciplinaCursadaVO, historicoNaoAproveitadoDiretamente, mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, usuario);
                }
            }
        }
        if (mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina) {
            // se o mapa é do tipo 1:1 não há necessidade de avaliar novos itens abaixo.
            return;
        }
        for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if ((mapaEquivalenciaDisciplinaCursadaJaProcessado == null)
                    || (!mapaEquivalenciaDisciplinaCursadaJaProcessado.getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVO.getCodigo()))) {
                // Se nenhum mapa foi processado (mapaEquivalenciaDisciplinaCursadaJaProcessado == null) ou
                // se o mapa que pegamos for diferente do mapaEquivalenciaDisciplinaCursadaJaProcessado entao
                // vamos tentar obter e gerar um historico correspondente para o mesmo
                // Neste caso, teremos que buscar um histórico que possa ser utilizado para ABATER
                // esta disciplina que precisa ser cursada pelo aluno. Teremos que buscar este histórico, dentro
                // das seguintes possibilidades:
                //   - dentro dos históricos que ainda não foram aproveitados do aluno. Neste, caso seja encontrado
                //      algum histórico para esta disciplina que precisa ser cursada pelo aluno, teremos que verificar
                //      se este histórico já não foi aproveitado.
                for (HistoricoVO historicoEncontradoVO : matriculaProcessar.getHistoricoNaoAproveitadosTransferencia()) {
                    if ((historicoEncontradoVO.getAprovado())
                        && (historicoEncontradoVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo()))
                        && (historicoEncontradoVO.getCargaHorariaDisciplina().equals(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria()))) {
                        // se entrarmos aqui é por que encontramos um histórico compatível para 
                        // disciplina. Logo, temos que gerar um historico correspondente para o aluno
                        // na matriz destino.
                        gerarHistoricoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(transferencia, matriculaProcessar, mapaEquivalenciaDisciplinaVO, mapaEquivalenciaDisciplinaCursadaVO, historicoEncontradoVO, mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, usuario);
                        //como o histórico foi aproveitado, por meio de um mapa de equivalencia. Então o 
                        //mesmo é removido da lista de historicoNaoAproveitados na gradeDestino. Isto
                        //por que os históricos remanescentes nesta lista, serão gravados para o aluno
                        //na grade destino como históricos fora da grade
                    }
                }
            }
        }
    }
    
    public boolean verificarHistoricoJaFoiAproveitadoOutraInteracao(TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            HistoricoVO historicoVerificar) {
        for (HistoricoVO historicoAproveitado : matriculaProcessar.getHistoricoAproveitadosTransferencia()) {
            if (historicoAproveitado.getCodigo().equals(historicoVerificar.getCodigo())) {
                return true; 
            }
        }
        return false;
    }
	
    public boolean verificarHistoricoParaDisciplinaJaFoiAproveitadoOutraInteracao(TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            Integer codigoDisciplina, Integer cargaHorariaDisciplina) {
        for (HistoricoVO historicoAproveitado : matriculaProcessar.getHistoricoAproveitadosTransferencia()) {
            if ((historicoAproveitado.getDisciplina().getCodigo().equals(codigoDisciplina)) &&
                (historicoAproveitado.getCargaHorariaDisciplina().equals(cargaHorariaDisciplina))){
                return true; 
            }
        }
        return false;
    }    

    /**
     * Método responsavel por percorrer historicos não aproveitados por correspondencia
     * direta (disciplina de mesmo código a carga horária) e verificar se os mesmos não podem
     * ser aproveitados por meio de um mapa de equivalencia. O método irá buscar por um mapa de 
     * equivalencia para o mesmo - caso exista o mapa será aplicado na nova grade, sempre buscando manter
     * a situação identica do aluno, na grade origem. Ou seja, ao migrar se existem mais
     * de um mapa disponível para a disciplina, iremos privilegiar o mapa em que o aluno já
     * está encaixado na grade origem. Assim, a transferência terá menos impacto na vida
     * do aluno. Neste caso iremos avaliar inclusive disciplinas que o aluno esteja cursando
     * atualmente (matriculaPeriodoAtual). Caso não haja mapa equivalente para ser mantido, o sistema
     * irá primeiro buscar por mapas que sejam resolvidos por completo (ou seja, considerando os históricos
     * já aproveitados). Em segundo caso, iremos buscar privilegiar mapas que tenham disciplinas que o aluno
     * esteja cursando atualmente. Em terceiro caso, iremos buscar privilegiar mapas que estejam com maior
     * número de disciplinas realizadas pelo aluno.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void verificandoEGerandoHistoricosNaoAproveitadosGradeDestinoPorMapaEquivalencia(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            UsuarioVO usuario) throws Exception {
        if (transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo().equals(0)) {
            // se nenhum mapa de equivalencia foi fornecido pelo usuário, então é por que esta
            // migração, deverá migrar somente históricos por correspodencia de código/carga horária.
            return;
        }
        
        // Iremos percorrer todos os históricos nao aproveitados de forma direta na grade destino 
        // Tendem a ser históricos que o aluno está estudando como equivalente, para pagar outra
        // disciplina de sua matriz (mapa de equivalencia).
        List<HistoricoVO> historicosNaoAproveitadosDiretamenteGradeDestino = matriculaProcessar.getHistoricoNaoAproveitadosTransferencia();
        int nrHistorico = historicosNaoAproveitadosDiretamenteGradeDestino.size() - 1;
        while (nrHistorico >= 0) {
            HistoricoVO historicoNaoAproveitadoDiretamente = historicosNaoAproveitadosDiretamenteGradeDestino.get(nrHistorico);
            if ((historicoNaoAproveitadoDiretamente.getAprovado() || historicoNaoAproveitadoDiretamente.getReprovado()) &&
                (!verificarHistoricoJaFoiAproveitadoOutraInteracao(matriculaProcessar, historicoNaoAproveitadoDiretamente))) {
                // se chegarmos aqui é por que não encontramos uma correspondencia direta para a disciplina na grade destino (verificada
                // pelos metodos chamados antes de método).
                // logo, iremos buscar por uma mapa de equivalencia que possa ser aplicado no histórico fora da grade,
                // fazendo com que o mesmo seja aproveitado.
                MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = obterMapaEquivalenciaAplicavelDisciplinaAprovadaGradeOrigem(transferencia, matriculaProcessar, historicoNaoAproveitadoDiretamente, usuario);
                if (mapaEquivalenciaDisciplinaVO != null) {
                    boolean mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina = Boolean.FALSE;
                    if ((mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().size() == 1) &&
                        (mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().size() == 1)) {
                        // se o mapa de equivalencia tem somente uma disciplina a ser cursada e somente uma disciplina
                        // a ser aprovada na matriz destino, então já iremos gerar o histórico de forma direta na
                        // gradeDestino. Isto por que não é necessário aplicar as regras do mapaEqualencia (nota por media, ou maior nota)
                        // maior frequencia ou médias das frequencias, ...) Pois, temos somente uma disciplina, levando para outra.
                        // isto implicará em melhor perfomance e em dados mais limpos na gradeDestino. Como este é o caso comum,
                        // para a maioria das transferencia, também, teremos um ganho de performance considerável, pois não teremos
                        // que resolver o mapa na matriz destino.
                        mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina = Boolean.TRUE;
                    }
                    // gerando historicos para o mapa de equivalencia.
                    if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoNaoAproveitadoDiretamente, transferencia, matriculaProcessar, false, usuario) ) {
                    	gerarHistoricosGradeDestinoPorMapaEquivalencia(transferencia, matriculaProcessar, mapaEquivalenciaDisciplinaVO, historicoNaoAproveitadoDiretamente, mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, usuario);
                    }
                    matriculaProcessar.getMapasEquivalenciaAproveitadosTransferenciaComHistoricos().add(mapaEquivalenciaDisciplinaVO);
                }
            }            
            nrHistorico--;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricoComoForaGradeGradeDestino(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            HistoricoVO historicoOrigemAluno,
            UsuarioVO usuario) throws Exception {
        HistoricoVO novoHistorico = (HistoricoVO) historicoOrigemAluno.clone();
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao
        PeriodoLetivoVO periodoMatrizCurricularVO = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(historicoOrigemAluno.getPeriodoLetivoCursada().getPeriodoLetivo());
        if ((periodoMatrizCurricularVO == null) || (periodoMatrizCurricularVO.getCodigo().equals(0))) {
            // caso nao encontrou um PeriodoLetivo da Matriz oficial do aluno correspondete, entao
            // adota-se o primeiro periodo letivo da nova matriz do aluno.
            periodoMatrizCurricularVO = transferencia.getGradeMigrar().getPrimeiroPeriodoLetivoGrade();
        }
        PeriodoLetivoVO periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(historicoOrigemAluno.getPeriodoLetivoCursada().getPeriodoLetivo());
        if ((periodoLetivoCursar == null) || (periodoLetivoCursar.getCodigo().equals(0))) {
            // caso nao encontrou um PeriodoLetivo da Matriz oficial do aluno correspondete, entao
            // adota-se o primeiro periodo letivo da nova matriz do aluno.
            periodoLetivoCursar = transferencia.getGradeMigrar().getPrimeiroPeriodoLetivoGrade();
        }

        inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, periodoMatrizCurricularVO, periodoLetivoCursar, usuario);

        // marcando como fora da grade, pois trata-se de um historico de uma disciplina cursada fora da grade
        novoHistorico.setHistoricoDisciplinaForaGrade(Boolean.TRUE);

        // CONSIDERACOES SOBRE GRADEDISCIPLINA / GRADECURRICULARGRUPOOPTATIVADISCIPLINA
        // iremos limpar a referencia a gradeDisciplina/gradeDisciplinaGrupoOptativa
        // pois aqui temos duas possibilidades:
        //   a) Trata-se de um histórico que já foi estudado pelo aluno fora da grade, logo, a disciplina 
        //        que ele estudou nesta grade externa, deve ser mantida de forma intacta.
        //   b) Trata-se de um histórico que fazia parte da gradeOrigem, contudo, não existe na gradeDestino,
        //        logo o mesmo deverá ir para a gradeDestino como fora da grade. Repare que se na grade origem
        //        a disciplina era de grupo de optativa ou nao, isto continua inalterado.
        novoHistorico.setGradeDisciplinaVO(null);
        novoHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(null);
        
        if (novoHistorico.getHistoricoPorEquivalencia()) {
            // Registrando observacao caso o historico da gradeOrigem tenha sido estudado por equivalencia
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico foi cursado por equivalência. Mas foi migrado como histórico Fora da Grade (Não aproveitado academicamente). Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        if (novoHistorico.getHistoricoEquivalente()) {
            // Registrando observacao caso o historico da gradeOrigem era equivalente (ou seja, fora grade)
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico foi cursado como uma equivalente. Mas foi migrado como histórico Fora da Grade (Não aproveitado academicamente). Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }

        // Criando vinculos com os mapas de equivalencia.
        novoHistorico.setHistoricoEquivalente(Boolean.FALSE);
        novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
        novoHistorico.setMapaEquivalenciaDisciplina(null);
        novoHistorico.setMapaEquivalenciaDisciplinaCursada(null);
        novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(null);

        if (historicoOrigemAluno.getHistoricoDisciplinaComposta()) {
            // Registrando observacao caso o historico da gradeOrigem era uma disciplina composta
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico era de uma disciplina composta. Mas foi migrado como histórico Fora da Grade (Não aproveitado academicamente). Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        if (historicoOrigemAluno.getHistoricoDisciplinaFazParteComposicao()) {
            // Se o historico origem fazia parte de uma composicao e está sendo processado
            // neste método, então é por que na matriz nova, o mesmo não será mais integrante
            // de uma composição, mas sim, passará a ser considerado como um histórico
            // convencional. Isto pode ocorrer, pois na nova matriz, pode haver uma disciplina
            // na grade (ou no grupo de optativas) com as mesmas características da disciplina
            // que fazia parte da composição.
            novoHistorico.setGradeDisciplinaComposta(null);
            novoHistorico.setHistoricoDisciplinaFazParteComposicao(Boolean.FALSE);
            // Registrando observacao caso o historico da gradeOrigem fazia parte de uma composição
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico fazia parte de uma composição. Mas foi migrado como histórico Fora da Grade (Não aproveitado academicamente). Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        // gravando novo histórico
        getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
    }
	
    public boolean verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeDisciplinaVO gradeDisciplinaCorrespondente,
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
            UsuarioVO usuario) throws Exception {
    	if (!matriculaProcessar.getValidarHistoricoJaEstaAprovadoMatrizDestino()) {
    		// se entrar aqui é por que tem historico na grade destino. logo nao temos que preocupar com esta validacao.
    		return true;
    	}
    	if (gradeDisciplinaCorrespondente != null) {
    		// se entrar aqui é por que temos que validar a gradeDisciplina na matriz destino
        	PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(gradeDisciplinaCorrespondente.getPeriodoLetivoVO().getCodigo());
            HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplinaCorrespondente.getCodigo());
            if (historicoDisciplinaGradeDestino.getAprovado()) {
            	// se já existe um histórico para este aluno nesta gradeDisciplina indicando que o aluno
            	// está aprovado na mesma, entao nao podemos migrar este historico para a matriz destino
            	// O que está lá deve ser preservado.
            	return false;
            }
    	} else {
    		// se entrar aqui é por que temos que validar a gradeCurricularGrupoOptativa na matriz destino
        	PeriodoLetivoVO periodoLetivoDestino = gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada();
        	if ((periodoLetivoDestino == null) || (periodoLetivoDestino.getCodigo().equals(0))) {
        		// se periodoLetivoDestino está nulo é por que o mesmo nao estava montado na gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.
        		// Assim vamos ter que percorrer novamente a grade Destino para localizar o periodo letivo deste grupo de optativa e assim
        		// chegar ao seu historicoAtual para as validacoes abaixo.
        		for (PeriodoLetivoVO periodoLetivoVOGradeDestino : transferencia.getGradeMigrar().getPeriodoLetivosVOs()) {
        			if (periodoLetivoVOGradeDestino.getGradeCurricularGrupoOptativa().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getGradeCurricularGrupoOptativa().getCodigo())) {
        				periodoLetivoDestino = periodoLetivoVOGradeDestino;
        				gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.setPeriodoLetivoDisciplinaReferenciada(periodoLetivoVOGradeDestino);
        				break;
        			}
        		}
        	}
        	
        	PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivoDestino.getCodigo());
            HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCodigo());
            if (historicoDisciplinaGradeDestino.getAprovado() || historicoDisciplinaGradeDestino.getReprovado()) {
            	// se já existe um histórico para este aluno nesta gradeCurricularGrupoOptativaDisciplinaVOCorrespondente indicando que o aluno
            	// está aprovada na mesma, entao não podemos permitir que uma nova transferencia para ela 
            	return false;
            }
    	}
    	return true;
    }
    
    private HistoricoVO verificarObterHistoricoReprovadoParaDisciplinaFilhaComposicao(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, 
    		Integer disciplina, Integer cargaHoraria) {
        // Caso a configuracao abaixo esteja marcada, significa que a situacao da disciplina filha nao é determinante.
        // Mas sim, a situação da disciplina mae. Logo, neste caso temos que utilizar tambem historico reprovados como
        // passíveis de serem aproveitados.
        Boolean situacaoDasFilhasDeterminadaPelaMae = matriculaProcessar.getMatriculaVO().getCurso().getConfiguracaoAcademico().getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal();
        if (situacaoDasFilhasDeterminadaPelaMae) {
        	List<HistoricoVO> listaHistoricosReprovados = matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoReprovouGradeCurricular(); 
            int k = listaHistoricosReprovados.size() - 1;
            while (k >= 0) {
            	HistoricoVO historicoReprovadoPassivelDeSerAproveitado = (HistoricoVO)listaHistoricosReprovados.get(k);
            	if ((historicoReprovadoPassivelDeSerAproveitado.getDisciplina().getCodigo().equals(disciplina)) &&
            	    (historicoReprovadoPassivelDeSerAproveitado.getCargaHorariaDisciplina().equals(cargaHoraria))) {
            		return historicoReprovadoPassivelDeSerAproveitado;
            	}
            	k = k - 1;
            }
        }
    	
    	return null;
    }
    
    private void excluirHistoricoDisciplinaListaPendenciaParaTransferencia(
    		TransferenciaMatrizCurricularVO transferencia,
    		TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, 
    		Integer disciplina, Integer cargaHoraria) { 
    	// buscando por historicos no qual o aluno esteja aprovado, para atender a filha da composicao.
        int i = matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().size() - 1;
        while (i >= 0) {
        	HistoricoVO historicoNaoAproveitadoDiretamente = (HistoricoVO)matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().get(i);
        	if ((historicoNaoAproveitadoDiretamente.getDisciplina().getCodigo().equals(disciplina)) &&
        	    (historicoNaoAproveitadoDiretamente.getCargaHorariaDisciplina().equals(cargaHoraria))) {
        		matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().remove(i);
        		return;
        	}
        	i = i - 1;
        }
    	
    }
    
    private HistoricoVO verificarObterHistoricoNaoAproveitadoParaDisciplinaFilhaComposicao(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, 
    		Integer disciplina, Integer cargaHoraria) {
    	List<HistoricoVO> historicosAlunoAprovado = matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAprovadasAlunoGradeCurricular(); 
    	// buscando por historicos no qual o aluno esteja aprovado, para atender a filha da composicao.
        int i = historicosAlunoAprovado.size() - 1;
        while (i >= 0) {
        	HistoricoVO historicoNaoAproveitadoDiretamente = (HistoricoVO)historicosAlunoAprovado.get(i);
        	if ((historicoNaoAproveitadoDiretamente.getDisciplina().getCodigo().equals(disciplina)) &&
        	    (historicoNaoAproveitadoDiretamente.getCargaHorariaDisciplina().equals(cargaHoraria))) {
        		
        		// Se encontramos o historico aprovado, entao vamos verificar se o mesmo já não foi aproveitado 
        		// para outra disciplina da matriz curricular destino. Caso, sim, entao nao podemos considerar
        		// este histórico como válido para novo aproveitamento.
        		if (!verificarHistoricoJaFoiAproveitadoOutraInteracao(matriculaProcessar, historicoNaoAproveitadoDiretamente)) {
        			// se entrar aqui é por que o historico nao foi aproveitado.
        			return historicoNaoAproveitadoDiretamente;
        		}
        	}
        	i = i - 1;
        }
        
        return null;
    } 
    
    public boolean veriricarGradeDisciplinaCompostaJaResolvidaPorOutroMapaEquivalencia(
    		GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO,
    		TransferenciaMatrizCurricularMatriculaVO matriculaProcessar) {
    	for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaAplicar : matriculaProcessar.getMapasEquivalenciaAplicarComposicao()) {
    		for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaEquivalenciaDisciplinaAplicar.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
    			if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo())) &&
    			    (gradeDisciplinaCompostaVO.getCargaHoraria().equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria()))) {
    				// se encontrou disciplina em outro mapa, nao temos que processá-la, pois a mesma será resolvida / enderecada
    				// por este outro mapa
    				return true;
    			}
    		}
    	}
    	return false;
    }    
    
    /**
     * Método responsavel por obter um mapa de equivalencia que possa atender uma determinada disciplina 
     * que faz parte de uma composicao. Este método porem, irá buscar por um mapa que atenda as seguintes
     * características:
     *   1) O mapa tem que ser resolvido por completo. Ou seja, se para pagar o mapa o aluno tem que estudar 
     *      3 disciplinas. Entao deverá ter históricos para pagar por estas 3 disciplinas. Ou seja, nao serao
     *      aceitos aqui mapas parcialmente resolvidos.
     *   2) A mapa deverá ser totalmente voltado para pagar disciplinas da composicao em debate. Ou seja, o mapa
     *      poderá estar pagando mais de uma disciplina. Contudo, todas deverão ser filhas da mesma disciplina mãe
     *      (ou seja, devem fazer parte da mesma composicao).
     *   3) Caso haja mais de uma mapa a rotina irá pegar o primeiro que for totalmente aplicável.
     * @author Otimize - 23 de ago de 2016 
     * @param aproveitamentoDisciplinasEntreMatriculasVO
     * @param gradeDisciplinaCompostaVO
     * @param usuario
     * @return
     * @throws Exception
     */
    private MapaEquivalenciaDisciplinaVO obterMapaEquivalenciaAplicavelDisciplinaFazParteComposicao(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, 
    		GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO,
    		List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs,
            UsuarioVO usuario) throws Exception { 
        List<MapaEquivalenciaDisciplinaVO> listaMapasValidos =
                getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(
                		transferencia.getGradeMigrar().getCodigo(), gradeDisciplinaCompostaVO.getDisciplina().getCodigo(),
                		transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo(), NivelMontarDados.TODOS);
        if (listaMapasValidos.isEmpty()) {
            return null;
        }

        for (MapaEquivalenciaDisciplinaVO mapaDisponivel : listaMapasValidos) {
        	
        	// primerio vamos avaliar se todas as disciplinas a serem estudas pelo aluno podem ser complementamente
        	// atendidas pelo historicos disponiveis para aproveitamento ainda
        	boolean todasDisciplinaACursarSaoAtendidas = true;
        	for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaDisponivel.getMapaEquivalenciaDisciplinaCursadaVOs()) {
        		boolean achouHistoricoParaDisciplinaACursarMapa = false;
        		
        		HistoricoVO historicoNaoAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo(), mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria());
//        		if (historicoNaoAproveitado == null) {
//        			historicoNaoAproveitado = verificarObterHistoricoCompativelSendoCursadoPeloAlunoParaDisciplina(transferencia, matriculaProcessar, mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo(), mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria() );
//        		}
        		if (historicoNaoAproveitado != null) {
    				achouHistoricoParaDisciplinaACursarMapa = true;
    				// se entrarmos aqui é por que temos um historico compativel para ser utilizado no mapa. Contudo, teremos
    				// que verificar se o mesmo já nao foi utilizado em outro mapa que foi processado antes por esta mesma rotina.
    				for (HistoricoVO historicoJaAlocadoOutroMapa : matriculaProcessar.getHistoricoAlocadosMapaEquivalenciaAproveitados()) {
    					if (historicoJaAlocadoOutroMapa.getCodigo().equals(historicoNaoAproveitado.getCodigo())) {
            				achouHistoricoParaDisciplinaACursarMapa = false;
            				break;
    					}
    				}
    				// vamos armazenar nesta variavel transient o historico base para criacao e implementacao da 
    				// equivalencia. Assim, caso o mapa seja de fato aproveitado, ja teremos o historico base do mesmo
    				// setado para processamento.
    				mapaEquivalenciaDisciplinaCursadaVO.setHistorico(historicoNaoAproveitado);
        		}
        		if (!achouHistoricoParaDisciplinaACursarMapa) {
        			todasDisciplinaACursarSaoAtendidas = false;
        			break;
        		}
        		
//        		for (HistoricoVO historicoNaoAproveitado : matriculaProcessar.getHistoricoNaoAproveitadosTransferencia()) {
//        			if ((historicoNaoAproveitado.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo())) && 
//        			    (historicoNaoAproveitado.getCargaHorariaDisciplina().equals(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria()))) {
//        				achouHistoricoParaDisciplinaACursarMapa = true;
//        				// se entrarmos aqui é por que temos um historico compativel para ser utilizado no mapa. Contudo, teremos
//        				// que verificar se o mesmo já nao foi utilizado em outro mapa que foi processado antes por esta mesma rotina.
//        				for (HistoricoVO historicoJaAlocadoOutroMapa : matriculaProcessar.getHistoricoAlocadosMapaEquivalenciaAproveitados()) {
//        					if (historicoJaAlocadoOutroMapa.getCodigo().equals(historicoNaoAproveitado.getCodigo())) {
//                				achouHistoricoParaDisciplinaACursarMapa = false;
//                				break;
//        					}
//        				}
//        				// vamos armazenar nesta variavel transient o historico base para criacao e implementacao da 
//        				// equivalencia. Assim, caso o mapa seja de fato aproveitado, ja teremos o historico base do mesmo
//        				// setado para processamento.
//        				mapaEquivalenciaDisciplinaCursadaVO.setHistorico(historicoNaoAproveitado);
//        			}
//        		}
//        		if (!achouHistoricoParaDisciplinaACursarMapa) {
//        			todasDisciplinaACursarSaoAtendidas = false;
//        			break;
//        		}
        	}
        	
        	if (todasDisciplinaACursarSaoAtendidas) {
	        	// segundo vamos avaliar se todas as disciplinas da matriz que serao pagas pelo mapa de equivalencia estao
	        	// dentro da composicao. Pois a equivalencia deverá estar satisfeita por completo e também deverá estar limitada
	        	// as disciplinas filhas da composição.
	        	boolean encontrouTodasDisciplinasMapaNaComposicao = true;
	        	for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaDisponivel.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
	        		boolean encontrouDisciplanaMapaListaDisciplinasFazParteComposicao = false;
	        		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVOValidar : gradeDisciplinaCompostaVOs) {
	        			if ((mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo().equals(gradeDisciplinaCompostaVOValidar.getDisciplina().getCodigo())) &&
	        				(mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria().equals(gradeDisciplinaCompostaVOValidar.getCargaHoraria()))) {
	        				// Além de encontrar na lista de disciplinas que fazem parte da composicao, temos que ter
	        				// certeza que de que esta disciplina que será paga pelo mapa já nao está como aprovada
	        				// Caso esteja aprovada, entao o mapa nao pode ser aplicado
	        				if (!gradeDisciplinaCompostaVOValidar.getHistoricoAtualAluno().getAprovado()) {
	        					encontrouDisciplanaMapaListaDisciplinasFazParteComposicao = true;
	        				}
	        			}
	        		}
	        		if (!encontrouDisciplanaMapaListaDisciplinasFazParteComposicao) {
	        			encontrouTodasDisciplinasMapaNaComposicao = false;
	        			break;
	        		}
	        	}
	        	if (encontrouTodasDisciplinasMapaNaComposicao) {
	        		return mapaDisponivel;
	        	}
        	}
        }
        return null;
    }    
    
    /**
     * Verificando se alguma disciplina que faz parte da composicao pode ser aproveitada, por meio,
     * do mapa de equivalencia. 
     * @author Otimize - 23 de ago de 2016 
     * @return
     */
    private int verificarObterHistoricoFazParteComposicaoCumpridoPorMeioMapaEquivalencia(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, 
    		List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs,
    		UsuarioVO usuario ) throws Exception {
        if (transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo().equals(0)) {
            // se nenhum mapa de equivalencia foi fornecido pelo usuário, então é por que esta
            // migração, deverá migrar somente históricos por correspodencia de código/carga horária.
            return 0;
        }
        
    	for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaCompostaVOs) {
			// se o historico atual ja esta montado com um historico de aprovado (vindo de um aproveitamento)
			// entao nao temos que tratar mais esta disciplina da composicao, contudo, caso a mesma nao esteja
    		// aprovada é porque a mesma está pendente de solução.
    		if ((!gradeDisciplinaCompostaVO.getHistoricoAtualAluno().getAprovado()) &&
    			(!gradeDisciplinaCompostaVO.getHistoricoAtualAluno().getCursando()) &&
    		    (!veriricarGradeDisciplinaCompostaJaResolvidaPorOutroMapaEquivalencia(gradeDisciplinaCompostaVO, matriculaProcessar))) {
    			
    			// Se o histórico nao está cumprido, vamos atraz de uma mapa de equivalencia que possa resolve-lo
                MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = obterMapaEquivalenciaAplicavelDisciplinaFazParteComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO, gradeDisciplinaCompostaVOs, usuario);
    			
                if (mapaEquivalenciaDisciplinaVO == null) {
                	return 0;
                }
                
    			// Se chegarmos aqui é por que achamos um mapa que valido para aplicacao na composicao. Contudo, o mesmo será
                // armazenado em uma lista para processamento posterior. Isto por que o mapa de fato só será aplicado caso a 
                // composicao esteja sendo resolvida por definitivo. Ou seja, se encontramos um mapa, mas somente parte da composicao
                // foi atendida com ele, entao o mesmo terá que ser descartado.
                matriculaProcessar.getMapasEquivalenciaAplicarComposicao().add(mapaEquivalenciaDisciplinaVO);
                
                // Outro controle que temos que fazer aqui é registrar os historicos que estao envolvidos no mapa adicionado acima,
                // em uma lista de controle que irá evitar que este mesmo historico possa ser utilizado em outro mapa que será avaliado
                // posteriormente por esta rotina. Ou seja, se o historico foi utilizado no mapa acima, vamos adicioná-lo para a lista
                // historicoAlocadosMapaEquivalenciaAproveitados de forma que o mesmo nao seja considerado como valido para outro mapa
                // que virá a ser processado.
                for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                	matriculaProcessar.getHistoricoAlocadosMapaEquivalenciaAproveitados().add(mapaEquivalenciaDisciplinaCursadaVO.getHistorico());
                }

                return mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().size();
    		}
    	}
    	return 0;
    }
    
    private void gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicaoGrupoOptativa(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,    		
    		GradeCurricularGrupoOptativaDisciplinaVO gradeGrupoOptativaDisciplinaMaeComposicaoCorrespondente,
            UsuarioVO usuario) throws Exception {
    	// vamor percorrer os mapas de equivalencia validos para a composicao para processar o aproveitamento de cada um deles
    	for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : matriculaProcessar.getMapasEquivalenciaAplicarComposicao()) {
    		
    		// para cada mapa vamos gerar o historico base da discplina da matriz que será utilizado para a criacao do aproveitamento. 
    		for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatriz : mapaEquivalenciaComposicao.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {

    			GradeDisciplinaCompostaVO gradeDisciplinaCompostaCorrespondeteMapa = null;
    			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeGrupoOptativaDisciplinaMaeComposicaoCorrespondente.getGradeDisciplinaCompostaVOs()) {
    				if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaMatriz.getDisciplinaVO().getCodigo())) &&
    				    (gradeDisciplinaCompostaVO.getCargaHoraria().equals(mapaEquivalenciaDisciplinaMatriz.getCargaHoraria()))) {
    					gradeDisciplinaCompostaCorrespondeteMapa = gradeDisciplinaCompostaVO;
    				}
    			}

    			if (gradeDisciplinaCompostaCorrespondeteMapa == null) {
    				throw new Exception("Ocorreu um erro inesperado! Ao tentar realizar o aproveitamento de uma disciplina filha da composicação, por meio de uma mapa de equivalência.");
    			}
    			
    			HistoricoVO novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz = (HistoricoVO)mapaEquivalenciaDisciplinaMatriz.getHistorico().clone();
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setCodigo(0);
    			
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondeteMapa);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setGradeDisciplinaComposta(gradeDisciplinaCompostaCorrespondeteMapa);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getDisciplina());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setCargaHorariaDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getCargaHoraria());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setCreditoDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getNrCreditos());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMatrizCurricular(gradeDestino);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMatricula(matriculaProcessar.getMatriculaVO());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMatriculaPeriodo(matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
    			
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setResponsavel(usuario);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setDataRegistro(new Date());
    			if ((transferencia.getUtilizarAnoSemestreAtualDisciplinaAprovada()) ||
    			    (novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.getAnoHistorico().equals(""))) {
    				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setAnoHistorico(transferencia.getAnoDisciplinaAprovada());
    				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setSemestreHistorico(transferencia.getSemestreDisciplinaAprovada());
    			} 
    			
    			//novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setHistoricoDisciplinaAproveitada(Boolean.TRUE);
    	        
    	        // Ja teremos que carregar a configuracao academica aqui, pois a mesma sera importante para determinar a media final
    	        // da disciplna mae nos metodos abaixos
    			Integer codigoCfgAcademicaEspecifica = gradeDisciplinaCompostaCorrespondeteMapa.getConfiguracaoAcademico().getCodigo();
    			ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = transferencia.getConfiguracaoAcademicoCursoTransferencia(); 
    			if (!codigoCfgAcademicaEspecifica.equals(0)) {
    				ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
    				configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
    			}
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);

    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMediaFinal(mapaEquivalenciaComposicao.getMediaFinalMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setFreguencia(mapaEquivalenciaComposicao.getFrequenciaMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));
    			
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setSituacao(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatriz);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMapaEquivalenciaDisciplina(mapaEquivalenciaComposicao);
    			
    			gradeDisciplinaCompostaCorrespondeteMapa.setHistoricoAtualAluno(novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz);
    		}
    	}
    	
    	// como os históricos foram aproveitados no sentido de cumprir a disciplina composta (mesmo que por meio de um mapa de equivalencia)
    	// agora temos que removes-los da lista de historicosNaoAproveitados. E, ainda, jogá-los para a lista de historicos aproveitados.
    	
        // removendo histório da lista de nao aproveitados - passo importante para o processamento das proximas disciplinas
        int i = matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().size() - 1;
        while (i >= 0) {
        	HistoricoVO historico = (HistoricoVO)matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().get(i);
        	
        	// vamor percorrer os mapas de equivalencia validos para a composicao para remover os itens aproveitados
        	for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : matriculaProcessar.getMapasEquivalenciaAplicarComposicao()) {
        		// para cada mapa vamos gerar o historico base da discplina da matriz que será utilizado para a criacao do aproveitamento. 
        		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaComposicao.getMapaEquivalenciaDisciplinaCursadaVOs()) {
        			if ((mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getDisciplina().getCodigo().equals(historico.getDisciplina().getCodigo()) &&
        				(mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getCargaHorariaDisciplina().equals(historico.getCargaHorariaDisciplina())))) {
        				// se entrar aqui é por que achamos um historico que foi aproveitado no mapa, logo o mesmo será removido da lista de nao processados.
        				matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().remove(i);
        			}
        		}
        	}
        	i = i - 1;
        }
    	
    }    
    
    private void gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicao(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,    		
    		GradeDisciplinaVO gradeDisciplinaMaeComposicaoCorrespondente,
            UsuarioVO usuario) throws Exception {
    	// vamor percorrer os mapas de equivalencia validos para a composicao para processar o aproveitamento de cada um deles
    	for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : matriculaProcessar.getMapasEquivalenciaAplicarComposicao()) {
    		
    		// para cada mapa vamos gerar o historico base da discplina da matriz que será utilizado para a criacao do aproveitamento. 
    		for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatriz : mapaEquivalenciaComposicao.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {

    			GradeDisciplinaCompostaVO gradeDisciplinaCompostaCorrespondeteMapa = null;
    			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaMaeComposicaoCorrespondente.getGradeDisciplinaCompostaVOs()) {
    				if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaMatriz.getDisciplinaVO().getCodigo())) &&
    				    (gradeDisciplinaCompostaVO.getCargaHoraria().equals(mapaEquivalenciaDisciplinaMatriz.getCargaHoraria()))) {
    					gradeDisciplinaCompostaCorrespondeteMapa = gradeDisciplinaCompostaVO;
    				}
    			}

    			if (gradeDisciplinaCompostaCorrespondeteMapa == null) {
    				throw new Exception("Ocorreu um erro inesperado! Ao tentar realizar o aproveitamento de uma disciplina filha da composicação, por meio de uma mapa de equivalência.");
    			}
    			
    			HistoricoVO novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz = (HistoricoVO)mapaEquivalenciaDisciplinaMatriz.getHistorico().clone();
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setCodigo(0);
    			
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondeteMapa);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setGradeDisciplinaComposta(gradeDisciplinaCompostaCorrespondeteMapa);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getDisciplina());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setCargaHorariaDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getCargaHoraria());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setCreditoDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getNrCreditos());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMatrizCurricular(gradeDestino);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMatricula(matriculaProcessar.getMatriculaVO());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMatriculaPeriodo(matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
    			
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setResponsavel(usuario);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setDataRegistro(new Date());
    			if ((transferencia.getUtilizarAnoSemestreAtualDisciplinaAprovada()) ||
    			    (novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.getAnoHistorico().equals(""))) {
    				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setAnoHistorico(transferencia.getAnoDisciplinaAprovada());
    				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setSemestreHistorico(transferencia.getSemestreDisciplinaAprovada());
    			} 
    			
    			//novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setHistoricoDisciplinaAproveitada(Boolean.TRUE);
    	        
    	        // Ja teremos que carregar a configuracao academica aqui, pois a mesma sera importante para determinar a media final
    	        // da disciplna mae nos metodos abaixos
    			Integer codigoCfgAcademicaEspecifica = gradeDisciplinaCompostaCorrespondeteMapa.getConfiguracaoAcademico().getCodigo();
    			ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = transferencia.getConfiguracaoAcademicoCursoTransferencia(); 
    			if (!codigoCfgAcademicaEspecifica.equals(0)) {
    				ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
    				configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
    			}
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);
    			        
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMediaFinal(mapaEquivalenciaComposicao.getMediaFinalMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setFreguencia(mapaEquivalenciaComposicao.getFrequenciaMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));
    			
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setSituacao(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor());
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatriz);
    			novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMapaEquivalenciaDisciplina(mapaEquivalenciaComposicao);
    			
    			gradeDisciplinaCompostaCorrespondeteMapa.setHistoricoAtualAluno(novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz);
    		}
    	}
    	
    	// como os históricos foram aproveitados no sentido de cumprir a disciplina composta (mesmo que por meio de um mapa de equivalencia)
    	// agora temos que removes-los da lista de historicosNaoAproveitados. E, ainda, jogá-los para a lista de historicos aproveitados.
    	
        // removendo histório da lista de nao aproveitados - passo importante para o processamento das proximas disciplinas
        int i = matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().size() - 1;
        while (i >= 0) {
        	HistoricoVO historico = (HistoricoVO)matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().get(i);
        	
        	// vamor percorrer os mapas de equivalencia validos para a composicao para remover os itens aproveitados
        	for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : matriculaProcessar.getMapasEquivalenciaAplicarComposicao()) {
        		// para cada mapa vamos verificar se o historico base da discplina da matriz que será utilizado para a criacao do aproveitamento. 
        		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaComposicao.getMapaEquivalenciaDisciplinaCursadaVOs()) {
        			if ((mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getDisciplina().getCodigo().equals(historico.getDisciplina().getCodigo()) &&
        				(mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getCargaHorariaDisciplina().equals(historico.getCargaHorariaDisciplina())))) {
        				// se entrar aqui é por que achamos um historico que foi aproveitado no mapa, logo o mesmo será removido da lista de nao processados.
        				matriculaProcessar.getHistoricoNaoAproveitadosTransferencia().remove(i);
        			}
        		}
        	}
        	i = i - 1;
        }
    }
    
    /**
     * Metodo responsavel por gerar os historicos das filhas da composicao, quando essas
     * filhas sao obtidas de historicos diversos aproveitadas da matriz origem
     * @author Otimize - 1 de set de 2016 
     * @param transferencia
     * @param matriculaProcessar
     * @param gradeDestino
     * @param listaGradeDisciplinaCompostaVOs
     * @param gradeDisciplinaCompostaCorrespondente
     * @param gradeCurricularGrupoOptativaCompostaCorrespondente
     * @param usuario
     * @throws Exception
     */
    private List<HistoricoVO> gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
    		List<GradeDisciplinaCompostaVO> listaGradeDisciplinaCompostaVOs,
    		GradeDisciplinaVO gradeDisciplinaCompostaCorrespondenteMae,
    		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaCompostaCorrespondenteMae, 
            UsuarioVO usuario) throws Exception {
    	List<HistoricoVO> listaHistoricosGeradosFilhasComposicao = new ArrayList<HistoricoVO>(0);
    	for (GradeDisciplinaCompostaVO gradeDisciplinaFazParteCompostaVO : listaGradeDisciplinaCompostaVOs) {
    		HistoricoVO historicoBaseGerarAproveitamentoFazParteComposicao = gradeDisciplinaFazParteCompostaVO.getHistoricoAtualAluno();
    		
    		if (Uteis.isAtributoPreenchido(historicoBaseGerarAproveitamentoFazParteComposicao)) {
    			HistoricoVO novoHistoricoDisciplinaFazParteComposicao = null;
    			if (gradeDisciplinaCompostaCorrespondenteMae != null) {
    				novoHistoricoDisciplinaFazParteComposicao = gerarHistoricoDisiciplinaFazParteComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaCorrespondenteMae, null, gradeDisciplinaFazParteCompostaVO, historicoBaseGerarAproveitamentoFazParteComposicao, false, usuario);
    			} else {
    				novoHistoricoDisciplinaFazParteComposicao = gerarHistoricoDisiciplinaFazParteComposicao(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaCompostaCorrespondenteMae, gradeDisciplinaFazParteCompostaVO, historicoBaseGerarAproveitamentoFazParteComposicao, false, usuario);
    			}
    			
    			gradeDisciplinaFazParteCompostaVO.setHistoricoAtualAluno(novoHistoricoDisciplinaFazParteComposicao);
    			listaHistoricosGeradosFilhasComposicao.add(novoHistoricoDisciplinaFazParteComposicao);
    			//getFacadeFactory().getHistoricoFacade().incluir(novoHistoricoDisciplinaFazParteComposicao, usuario);
    		}
    	}
    	return listaHistoricosGeradosFilhasComposicao;
    }
    
    private void gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
    		GradeDisciplinaVO gradeDisciplinaCompostaCorrespondente,
    		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente,
            UsuarioVO usuario) throws Exception {
    	
    	HistoricoVO novoHistorico = null;
    	// como estamos derivando o historico da mae da composicao, com base nos dados das filhas, entao
    	// temos q construir este historico abaixo
    	if (gradeDisciplinaCompostaCorrespondente != null) {
    		novoHistorico = (HistoricoVO) gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno().clone();
    	} else {
    		novoHistorico = (HistoricoVO) gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno().clone();
    	}
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao

        HistoricoVO historicoOrigemAluno = null;
        if (gradeDisciplinaCompostaCorrespondente != null) {
        	historicoOrigemAluno = gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno();
        	PeriodoLetivoVO periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(gradeDisciplinaCompostaCorrespondente.getPeriodoLetivoVO().getPeriodoLetivo());
        	inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, gradeDisciplinaCompostaCorrespondente.getPeriodoLetivoVO(), periodoLetivoCursar, usuario);

        	novoHistorico.setDisciplina(gradeDisciplinaCompostaCorrespondente.getDisciplina());
        	novoHistorico.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondente); // atualizar o historico para a nova gradeDisciplina
        	novoHistorico.setCargaHorariaDisciplina(gradeDisciplinaCompostaCorrespondente.getCargaHoraria());
        	novoHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(null);
    		novoHistorico.setDisciplinaReferenteAUmGrupoOptativa(Boolean.FALSE);
        } else {
        	historicoOrigemAluno = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno();
        	PeriodoLetivoVO periodoLetivoCursar = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getPeriodoLetivoDisciplinaReferenciada();
        	inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), periodoLetivoCursar, usuario);

        	novoHistorico.setDisciplina(gradeCurricularGrupoOptativaDisciplinaCorrespondente.getDisciplina());
        	novoHistorico.setCargaHorariaDisciplina(gradeCurricularGrupoOptativaDisciplinaCorrespondente.getCargaHoraria());
        	novoHistorico.setGradeDisciplinaVO(null); // atualizar o historico para a nova gradeDisciplina
        	novoHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaCorrespondente);
    		novoHistorico.setDisciplinaReferenteAUmGrupoOptativa(Boolean.TRUE);
        }
        novoHistorico.setMatricula(matriculaProcessar.getMatriculaVO());
        novoHistorico.setMatriculaPeriodo(matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO());
        
		novoHistorico.setHistoricoDisciplinaComposta(Boolean.TRUE);

        String msgInicial = "Histórico migrado da matriz (" + transferencia.getGradeOrigem().getCodigo() + ") ";
		novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + ". Histórico da Disciplina Composto foi derivado a partir dos dados dos históricos das filhas da composição.", true);
        
        // limpando estas configuracoes que nao fazem sentido aqui na disciplina mae da composicao
        novoHistorico.setHistoricoEquivalente(Boolean.FALSE);
        novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
        novoHistorico.setMapaEquivalenciaDisciplina(null);
        novoHistorico.setMapaEquivalenciaDisciplinaCursada(null);
        novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(null);
        novoHistorico.setGradeDisciplinaComposta(null);
        novoHistorico.setHistoricoDisciplinaFazParteComposicao(Boolean.FALSE);
        
        // Ja teremos que carregar a configuracao academica aqui, pois a mesma sera importante para determinar a media final
        // da disciplna mae nos metodos abaixos
        Integer codigoCfgAcademicaEspecifica = 0;
        if (gradeDisciplinaCompostaCorrespondente != null) {
        	codigoCfgAcademicaEspecifica = gradeDisciplinaCompostaCorrespondente.getConfiguracaoAcademico().getCodigo();
        } else {
        	codigoCfgAcademicaEspecifica = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getConfiguracaoAcademico().getCodigo();
        }
		ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = transferencia.getConfiguracaoAcademicoCursoTransferencia(); 
		if (!codigoCfgAcademicaEspecifica.equals(0)) {
			ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
			configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
		}
		novoHistorico.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);
		
    	// Antes de gerar o aproveitamento para a disciplina mae (composta), precisamos gerar os aproveitamentos (com seus respectivos
    	// historicos) para as disciplinas que fazem parte da composicao. Isto é importante, pois os dados mais importantes do historico
    	// e aproveitamento da disciplina mae serao obtidos a partir dos dados das filhas.
		List<HistoricoVO> historicoDisciplinasFazemParteAproveitamentoVOs = null;
		if (gradeDisciplinaCompostaCorrespondente != null) {
			historicoDisciplinasFazemParteAproveitamentoVOs = gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(transferencia, matriculaProcessar, gradeDestino, gradeDisciplinaCompostaCorrespondente.getGradeDisciplinaCompostaVOs(), gradeDisciplinaCompostaCorrespondente, null, usuario);
		} else {
			historicoDisciplinasFazemParteAproveitamentoVOs = gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(transferencia, matriculaProcessar, gradeDestino, gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(), null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);
		}

		if (transferencia.getUtilizarAnoSemestreAtualDisciplinaAprovada()) {
			novoHistorico.setAnoHistorico(transferencia.getAnoDisciplinaAprovada());
			novoHistorico.setSemestreHistorico(transferencia.getSemestreDisciplinaAprovada());
		} else {
			HistoricoVO historicoUmDasFilhasObterAnoSemestre = historicoDisciplinasFazemParteAproveitamentoVOs.get(0);
			novoHistorico.setAnoHistorico(historicoUmDasFilhasObterAnoSemestre.getAnoHistorico());
			novoHistorico.setSemestreHistorico(historicoUmDasFilhasObterAnoSemestre.getSemestreHistorico());
		}

		// mantendo atualizado o historico da gradeDisciplinaCorrespondente com o historico que estamos deduzindo.
		if (gradeDisciplinaCompostaCorrespondente != null) {
			gradeDisciplinaCompostaCorrespondente.setHistoricoAtualAluno(novoHistorico);
	        // inferir estes dados com base na rotina do historico que calcula a media e frequencia da disciplina composta
	        // com base nos historicos das disciplinas que fazem parte da composicao
	        getFacadeFactory().getHistoricoFacade().executarSimulacaoAtualizacaoDisciplinaComposta(gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno(), historicoDisciplinasFazemParteAproveitamentoVOs, false, usuario);
		} else {
			gradeCurricularGrupoOptativaDisciplinaCorrespondente.setHistoricoAtualAluno(novoHistorico);
	        // inferir estes dados com base na rotina do historico que calcula a media e frequencia da disciplina composta
	        // com base nos historicos das disciplinas que fazem parte da composicao
	        getFacadeFactory().getHistoricoFacade().executarSimulacaoAtualizacaoDisciplinaComposta(gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno(), historicoDisciplinasFazemParteAproveitamentoVOs, false, usuario);
		}
        
        // gravando novo histórico
        getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        
        if (!novoHistorico.getReprovado()) {
            // vamos adicionar para a lista abaixo somente os históricos de aprovação e cursando que foram aproveitados
            matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
        }        
        
        List<GradeDisciplinaCompostaVO> listaGradeDisciplinaCompostaVOProcessadas = null;
        if (gradeDisciplinaCompostaCorrespondente != null) {
        	listaGradeDisciplinaCompostaVOProcessadas = gradeDisciplinaCompostaCorrespondente.getGradeDisciplinaCompostaVOs();
        } else {
        	listaGradeDisciplinaCompostaVOProcessadas = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs();
        }
        
    	// atualizando lista de historicos nao aproveitados
        
        // removendo historicos referentes a disciplina mae da composicao
        if (gradeDisciplinaCompostaCorrespondente != null) {
        	excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, gradeDisciplinaCompostaCorrespondente.getDisciplina().getCodigo(), gradeDisciplinaCompostaCorrespondente.getCargaHoraria());
        } else {
        	excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaCorrespondente.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaCorrespondente.getCargaHoraria());
        }
        
        // removendo historicos corresponde as disciplinas filhas da composicao
    	for (GradeDisciplinaCompostaVO gradeDisciplinaFazParteCompostaVO : listaGradeDisciplinaCompostaVOProcessadas) {
    		HistoricoVO historicoBaseGerarAproveitamentoFazParteComposicao = gradeDisciplinaFazParteCompostaVO.getHistoricoAtualAluno();
        	excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, historicoBaseGerarAproveitamentoFazParteComposicao.getDisciplina().getCodigo(), historicoBaseGerarAproveitamentoFazParteComposicao.getCargaHorariaDisciplina());
    	}
    }  
    
    
    /**
     * Vamos verificar se existe alguma disciplina composta na grade destino, cuja composicao pode ser atendida por completo (para isto
	 * teremos que observar a regra de resolução da composição - todas as disciplinas ou somente parte delas). Para isto iremos verificar
	 * se os historicos remanescentes podem ser aproveitados nas filhas de composicao de forma a resolve-la por completo. Este método também
	 * é capaz de avaliar se discplinas filhas da composicao pode ser pagos (cumpridos) por meio de mapas de equivalencia. De qualquer forma,
	 * uma mapa de equivalencia só é considerado e ele for resolvido por completo (nao pode ficar pendente) e tambem se a composicao tambem
	 * foi resolvida por completo (nao existe o conceito de composicao resolvida parcialmente no SEI).
     * @author Otimize - 28 de jul de 2016 
     * @param aproveitamentoDisciplinasEntreMatriculasVO
     * @param usuario
     * @throws Exception
     */
    private void verificandoEGerandoHistoricosDisciplinaCompostaNaGradeDestinoCujaComposicaoPodeSerAtendidaPorCompleto(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
            UsuarioVO usuario) throws Exception {    	
    	List<GradeDisciplinaVO> listaGradesDisciplinasCompostasGrade = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeDisciplinaCompostaPorGrade(gradeDestino.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    	for (GradeDisciplinaVO gradeDisciplinaComComposicaoVO : listaGradesDisciplinasCompostasGrade) {
    		// obtendo gradeDisciplina correspondente já montada na gradeDestino
            GradeDisciplinaVO gradeDisciplinaCorrespondente = gradeDestino.obterGradeDisciplinaCorrespondente(gradeDisciplinaComComposicaoVO.getDisciplina().getCodigo(), gradeDisciplinaComComposicaoVO.getCargaHoraria());
            // atualizando a lista de disciplinas que fazem parte da composicao
            gradeDisciplinaCorrespondente.setGradeDisciplinaCompostaVOs(gradeDisciplinaComComposicaoVO.getGradeDisciplinaCompostaVOs());
            
            if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, usuario)) {
            	
                // agora vamos percorrer as filhas e verificar se elas possuem historico que podem ser aproveitados
                // para equalizar essas disciplinas filhas (ou seja, para aproveitar o historico da outra matricula
                // para a filha de uma composicao na gradeDestino.
                int nrDisciplinasCumprir = gradeDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs().size();
                TipoControleComposicaoEnum tipoControle = gradeDisciplinaCorrespondente.getTipoControleComposicao();
                if (tipoControle.equals(TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA)) {
                	nrDisciplinasCumprir = gradeDisciplinaCorrespondente.getNumeroMinimoDisciplinaComposicaoEstudar();
                }
                
                int nrDisciplinasCumpridas = 0;
                for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs()) {
                	HistoricoVO historicoCorrespondeteASerAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
                	if (historicoCorrespondeteASerAproveitado != null) {
                		nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
                		gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
                	} else {
                		// Se entrarmos aqui é porque nao encontramos um historicoAprovado para a filha da composicao. Contudo,
                		// vamos avaliar abaixo se existe uma disciplina que o aluno esteja cursando que pode ter uma correspondencia 
                		// direta para esta filha da composicao.
                		historicoCorrespondeteASerAproveitado = verificarObterHistoricoCompativelSendoCursadoPeloAlunoParaDisciplina(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
                		if (historicoCorrespondeteASerAproveitado != null) {
                			nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
                    		gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
                		} else {
                			// se nao encontramos um historico aprovado e nem um historico que esta sendo cursado, ainda temos que buscar
                			// por um historico que esteja reprovado - isto somente quando o sistema estiver configurado na cfg Academica
                			// para considerar a situacao da mae como determinante, ignorando a situacao das filhas.  Situacao testada no metodo abaixo.
                			historicoCorrespondeteASerAproveitado = verificarObterHistoricoReprovadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
                			if (historicoCorrespondeteASerAproveitado != null) {
                    			nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
                        		gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
                			}
                		}
                	}
                }
                if (nrDisciplinasCumpridas >= nrDisciplinasCumprir) {
                	// se entrarmos aqui é por que o mapa foi cumprido por completo, logo podemos gerar o aproveitamento para a disciplina 
                	// mae da composicao e tambem para as filhas da composicao
            		try {
            			gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(transferencia, matriculaProcessar, gradeDestino, gradeDisciplinaCorrespondente, null, usuario);
            		} catch (Exception e) {
            			System.out.println(e.getMessage());
            		}
                } else {
                	// limpando a lista a abaixo que é utilizada para garantir que um mesmo historico nao seja aproveitado
                	// mais de uma vez.
                	matriculaProcessar.getHistoricoAlocadosMapaEquivalenciaAproveitados().clear();
                	matriculaProcessar.getMapasEquivalenciaAplicarComposicao().clear();

                	// caso a composicao nao tenha sido comprida, vamos verificar se as disciplinas da composicao que nao foram
                	// aproveitadas de forma direta podem ser aproveitas por meio de uma mapa de de equivalencia. Aqui, temos
                	// que considerar, que um mapa que resolver as disciplinas pendentes da composicao podem assim contribuir para
                	// fechar a composicao por completo e, por conseguinte, viabilizar o aproveitamento da mesma.
                	int nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrDisciplinasCumprir - nrDisciplinasCumpridas;
                	while (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa > 0) {
                		int nrDisciplinasAproveitasPorEquivalencia = verificarObterHistoricoFazParteComposicaoCumpridoPorMeioMapaEquivalencia(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(), usuario );
                		if (nrDisciplinasAproveitasPorEquivalencia == 0) {
                			// se nenhuma disciplina filha da composicao pode ser aproveitada por meio do mapa, entao
                			// podemos sair deste método, pois, nao teremos como cumprir a composicao como um todo, logo
                			// ela nao poderá ser aproveita (pois, composicoes so podem ser aproveitadas se cumpridas por 
                			// completo)
                			break;
                		}
                		nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa - nrDisciplinasAproveitasPorEquivalencia;
                	}
                	if (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa == 0) {
                		try {
                			// Uma vez que a composicao foi resolvida de fato. Entao iremos agora processar todos os mapas de equivalencia
                			// fazendo com que os historicos resultantes da resolucao do mapa seja criados e armazendos na respectiva 
                			// disciplina da composicao. Assim, ao chamarmos o método de criacao dos históricos abaixo, o mesmo irá 
                			// processar normalmente. Lembrando que neste caso, o mapa de equavelencia utilizado para geracao ficara
                			// registrado somente no campo observacao. Pois nao iremos considerar mapa que forem totalmente resolvidos.
                			// Nao teremos controles neste caso para que o mapa fique parcialmente resolvido.
                			gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicao(transferencia, matriculaProcessar, gradeDestino, gradeDisciplinaCorrespondente, usuario);

                			// aqui vamos gerar os historicos da disciplina mae da composicao e para suas filhas, com base nos historicos já criados anterioremente.
                			gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(transferencia, matriculaProcessar, gradeDestino, gradeDisciplinaCorrespondente, null, usuario);
                			
                		} catch (Exception e) {
                			System.out.println(e.getMessage());
                		}	
                	}
                }
            	
            }
    	}
    	
    	List<GradeCurricularGrupoOptativaDisciplinaVO> listaGradesCurricularesGrupoOptativaComposta = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorMatrizCurricularGrupoOptativaComposta(gradeDestino.getCodigo(), usuario);
    	for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina : listaGradesCurricularesGrupoOptativaComposta) {
    		// obtendo gradeGrupoOptativaDisciplina correspondente já montada na gradeDestino
    		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente = gradeDestino.obterGradeCurricularGrupoOptativaCorrespondente(gradeCurricularGrupoOptativaDisciplina.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplina.getCargaHoraria());
            // atualizando a lista de disciplinas que fazem parte da composicao
    		gradeCurricularGrupoOptativaDisciplinaCorrespondente.setGradeDisciplinaCompostaVOs(gradeCurricularGrupoOptativaDisciplina.getGradeDisciplinaCompostaVOs());

            if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario)) {
                // agora vamos percorrer as filhas e verificar se elas possuem historico que podem ser aproveitados
                // para equalizar essas disciplinas filhas (ou seja, para aproveitar o historico da outra matricula
                // para a filha de uma composicao na gradeGrupoOptativaDestino.
                int nrDisciplinasCumprir = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs().size();
                //TipoControleComposicaoEnum tipoControle = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getTipoControleComposicao();
                //if (tipoControle.equals(TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA)) {
                //	nrDisciplinasCumprir = gradeCurricularGrupoOptativaDisciplinaCorrespondente.getNumeroMaximoDisciplinaComposicaoEstudar();
                //}
                
                int nrDisciplinasCumpridas = 0;
                for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs()) {
                	HistoricoVO historicoCorrespondeteASerAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
                	if (historicoCorrespondeteASerAproveitado != null) {
                		nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
                		gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
                	} else {
                		// Se entrarmos aqui é porque nao encontramos um historicoAprovado para a filha da composicao. Contudo,
                		// vamos avaliar abaixo se existe uma disciplina que o aluno esteja cursando que pode ter uma correspondencia 
                		// direta para esta filha da composicao.
                		historicoCorrespondeteASerAproveitado = verificarObterHistoricoCompativelSendoCursadoPeloAlunoParaDisciplina(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
                		if (historicoCorrespondeteASerAproveitado != null) {
                			nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
                    		gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
                		} else {
                			// se nao encontramos um historico aprovado e nem um historico que esta sendo cursado, ainda temos que buscar
                			// por um historico que esteja reprovado - isto somente quando o sistema estiver configurado na cfg Academica
                			// para considerar a situacao da mae como determinante, ignorando a situacao das filhas.  Situacao testada no metodo abaixo.
                			historicoCorrespondeteASerAproveitado = verificarObterHistoricoReprovadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
                			if (historicoCorrespondeteASerAproveitado != null) {
                    			nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
                        		gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
                			}
                		}
                	}
                }
                if (nrDisciplinasCumpridas >= nrDisciplinasCumprir) {
                	// se entrarmos aqui é por que o mapa foi cumprido por completo, logo podemos gerar o aproveitamento para a disciplina 
                	// mae da composicao e tambem para as filhas da composicao
            		try {
            			gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(transferencia, matriculaProcessar, gradeDestino, null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);
            		} catch (Exception e) {
            			System.out.println(e.getMessage());
            		}
                } else {
                	// limpando a lista a abaixo que é utilizada para garantir que um mesmo historico nao seja aproveitado
                	// mais de uma vez.
                	matriculaProcessar.getHistoricoAlocadosMapaEquivalenciaAproveitados().clear();
                	matriculaProcessar.getMapasEquivalenciaAplicarComposicao().clear();

                	// caso a composicao nao tenha sido comprida, vamos verificar se as disciplinas da composicao que nao foram
                	// aproveitadas de forma direta podem ser aproveitas por meio de uma mapa de de equivalencia. Aqui, temos
                	// que considerar, que um mapa que resolver as disciplinas pendentes da composicao podem assim contribuir para
                	// fechar a composicao por completo e, por conseguinte, viabilizar o aproveitamento da mesma.
                	int nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrDisciplinasCumprir - nrDisciplinasCumpridas;
                	while (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa > 0) {
                		int nrDisciplinasAproveitasPorEquivalencia = verificarObterHistoricoFazParteComposicaoCumpridoPorMeioMapaEquivalencia(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(), usuario );
                		if (nrDisciplinasAproveitasPorEquivalencia == 0) {
                			// se nenhuma disciplina filha da composicao pode ser aproveitada por meio do mapa, entao
                			// podemos sair deste método, pois, nao teremos como cumprir a composicao como um todo, logo
                			// ela nao poderá ser aproveita (pois, composicoes so podem ser aproveitadas se cumpridas por 
                			// completo)
                			break;
                		}
                		nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa - nrDisciplinasAproveitasPorEquivalencia;
                	}
                	if (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa == 0) {
                		try {
                			// Uma vez que a composicao foi resolvida de fato. Entao iremos agora processar todos os mapas de equivalencia
                			// fazendo com que os historicos resultantes da resolucao do mapa seja criados e armazendos na respectiva 
                			// disciplina da composicao. Assim, ao chamarmos o método de criacao dos históricos abaixo, o mesmo irá 
                			// processar normalmente. Lembrando que neste caso, o mapa de equavelencia utilizado para geracao ficara
                			// registrado somente no campo observacao. Pois nao iremos considerar mapa que forem totalmente resolvidos.
                			// Nao teremos controles neste caso para que o mapa fique parcialmente resolvido.
                			gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicaoGrupoOptativa(transferencia, matriculaProcessar, gradeDestino, gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);

                			// aqui vamos gerar os historicos da disciplina mae da composicao e para suas filhas, com base nos historicos já criados anterioremente.
                			gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(transferencia, matriculaProcessar, gradeDestino, null, gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);
                			
                		} catch (Exception e) {
                			System.out.println(e.getMessage());
                		}	
                	}
                }
            	
            }
    	}
    }	        

    /**
     * Método responsável por gerar históricos na matriz curricular destino, que não 
     * foram aproveitados de nenhuma forma do ponto de vista academico (nem por correspondencia,
     * nem por mapa de equivalencia da matriz)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerandoHistoricosCorrespondetesNaoAproveitadosGradeDestinoComoForaGrade(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            UsuarioVO usuario) throws Exception {
        for (HistoricoVO historicoVO : matriculaProcessar.getHistoricoNaoAproveitadosTransferencia()) {
        	// em caso de historico 'cursando' verifica se há um mapa de equivalencia que resolverá o caso da disciplina cursada e se não houver, será incluido como historico fora da grade 
        	if ((historicoVO.getAprovado() || historicoVO.getReprovado() || (historicoVO.getCursando() && !existeMapaEquivalenciaParaDisciplinaAlunoCursando(transferencia, matriculaProcessar, historicoVO))) &&
                (!verificarHistoricoJaFoiAproveitadoOutraInteracao(matriculaProcessar, historicoVO))) {
        		// precisamos verificar a existencia de um histórico já incluido na matriz de destino
            	// caso haja não poderemos realizar a transferencia evitando a duplicidade dos registros
            	boolean existeHistoricoForaGradeIncluidoOutrasTransferencias = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().verificarExistenciaHistoricoForaGradeMatrizDestino(historicoVO.getDisciplina().getCodigo(), historicoVO.getCargaHorariaDisciplina());
            	if (!existeHistoricoForaGradeIncluidoOutrasTransferencias) {
            		// Só iremos gerar históricos como fora da grade que foram aprovados ou reprovados
            		// historicos cancelados, trancados, nao cursados, e afins não precisam ser trasnfereridos.
            		// Históricos de disciplinas que estão sendo cursados são tratados em método específico mais 
            		// a frente. É preciso verificar se o histórico nao está como aproveitado, o que pode ocorrer
            		// em funcao de mapas de equivalencia que envolvem mais de uma disciplina de forma automatica
            		if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoVO, transferencia, matriculaProcessar, false, usuario) ) {
            			gerarHistoricoComoForaGradeGradeDestino(transferencia, matriculaProcessar, historicoVO, usuario);
            		}
            		if (historicoVO.getAprovado() || (historicoVO.getCursando())) {
            			// se trata-se de um histórico de aprovação temos que gerar um alerta crítico para o usuário
            			matriculaProcessar.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "ALERTA IMOPRTANTE: Histórico migrado como fora da grade. Disciplina: " +  historicoVO.getDisciplina().getCodigo() + " " + historicoVO.getDisciplina().getNome() + " CH: " + historicoVO.getCargaHorariaDisciplina() + " Situação: " + historicoVO.getSituacao_Apresentar() + " Média: " + historicoVO.getMediaFinal_Apresentar(), Boolean.TRUE);
            			matriculaProcessar.incremetarNrAlertasCriticos();
            		}
            	}
            }
        }
    }

    /**
     * Método responsavel por verificar e obter um MapaDeEquivalencia corrrespondente para um histórico, 
     * dentro de uma lista de históricos que já foram utilizados na transferenciaMatrizCurricular
     */
    private MapaEquivalenciaDisciplinaCursadaVO obterMapaEquivalenciaCorrenpodenteHistoricoListaMapaEquivalenciaUtilizadoTransferencia(
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            HistoricoVO historicoCursandoVO) {
        for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO : matriculaProcessar.getMapasEquivalenciaAproveitadosTransferenciaComHistoricos()) {
            for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                if ((mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().equals(historicoCursandoVO.getDisciplina().getCodigo()))
                        && (mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria().equals(historicoCursandoVO.getCargaHorariaDisciplina()))) {
                    // forcando para que todos os dados do mapa estejam carregados
                    mapaEquivalenciaDisciplinaCursadaVO.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
                    return mapaEquivalenciaDisciplinaCursadaVO;
                }
            }
        }
        return null;
    }

    /**
     * Altera o vinculado de uma MatriculaPeriodoTurmaDisciplina que está sendo cursado de forma que 
     * o mesmo passa a fazer uso de um mapa de equivalencia, que conduz o estudo do aluno em disciplina
     * X da gradeOrigem (anterior) para uma ou mais disciplinas da gradeDestino (por meio do mapa)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferenciaMatriz(
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAtualizar,
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO,
            MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO,
            UsuarioVO usuario) throws Exception {
        matriculaPeriodoTurmaDisciplinaAtualizar.setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        matriculaPeriodoTurmaDisciplinaAtualizar.setDisciplinaEquivale(Boolean.TRUE);
        matriculaPeriodoTurmaDisciplinaAtualizar.setMapaEquivalenciaDisciplinaVOIncluir(mapaEquivalenciaDisciplinaVO);
        matriculaPeriodoTurmaDisciplinaAtualizar.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);
        matriculaPeriodoTurmaDisciplinaAtualizar.setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean.FALSE);
        getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(matriculaPeriodoTurmaDisciplinaAtualizar, usuario);
    }

    /**
     * Desfaz o que o método alterarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferenciaMatriz
     * retornando a matriculaPeriodoTurmaDisciplinaAtualizar a situação antiga, antes da transferência de matriz
     * curricular. Para isto utiliza-se como base o histórico anterior da matrizCurricula origem da transferencia
     * que nunca é modificado durante a transferencia
     */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void cancelarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferenciaMatriz(TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAtualizar, UsuarioVO usuario) throws Exception {
		HistoricoVO historicoAnterior = getFacadeFactory().getHistoricoFacade().consultaRapidaPorCodigoMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaAtualizar.getCodigo(), transferencia.getGradeOrigem().getCodigo(), true, usuario);
		matriculaPeriodoTurmaDisciplinaAtualizar.setTransferenciaMatrizCurricularMatricula(null);
		if (Uteis.isAtributoPreenchido(historicoAnterior)) {
			if (!matriculaPeriodoTurmaDisciplinaAtualizar.getDisciplinaCursandoPorCorrespondenciaAposTransferencia()) {
				// Se a matriculaPeriodoTurmaDisciplina nao foi cursado por
				// correspondencia, então é por que
				// a mesma foi envolvida em um mapa de equivalencia da
				// gradeDestino da migração, logo os dados
				// referentes a equivalencia precisam ser recuperados do
				// histórico origem (historico da grade Origem)
				// que nunca sofre alterações.
				matriculaPeriodoTurmaDisciplinaAtualizar.setDisciplinaEquivale(historicoAnterior.getHistoricoEquivalente());
				matriculaPeriodoTurmaDisciplinaAtualizar.setMapaEquivalenciaDisciplinaVOIncluir(historicoAnterior.getMapaEquivalenciaDisciplina());
				matriculaPeriodoTurmaDisciplinaAtualizar.setMapaEquivalenciaDisciplinaCursada(historicoAnterior.getMapaEquivalenciaDisciplinaCursada());
				matriculaPeriodoTurmaDisciplinaAtualizar.setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean.FALSE);
			}
			// anulando vinculo do histórico que estava sendo cursado por
			// correspondencia com a transferencia de matriz curricular
			getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(historicoAnterior.getCodigo(), null, Boolean.FALSE, usuario);
		}
		matriculaPeriodoTurmaDisciplinaAtualizar.setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean.FALSE);
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(matriculaPeriodoTurmaDisciplinaAtualizar, usuario);
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void cancelarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferencia(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            UsuarioVO usuario) throws Exception {
        List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO = 
                getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorTransferenciaMatrizCurricularMatricula(matriculaProcessar.getCodigo(), usuario);
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVORetornar : listaMatriculaPeriodoTurmaDisciplinaVO) {
            cancelarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferenciaMatriz( transferencia,
                    matriculaProcessar, matriculaPeriodoTurmaDisciplinaVORetornar, usuario);
        }
    }

    /**
     * Gera um novo historico na grade destino (cursando) mas que trata-se de um historico de uma disciplina equivalente.
     * Ou seja, irá gerar este histórico vinculando o mesmo a um novo mapa de equivalencia, que foi mapeado
     * na gradeDestino. E iremos reprogramar a matriculaPeriodoTurmaDisciplina para fazer referencia a este histórico
     * criado, assim como iremos fazer a matriculaPeriodoTurmaDisciplina adotar o novo mapa de equivalencia em suas 
     * referencias.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarHistoricoCursandoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            PeriodoLetivoVO periodoLetivoCursar,
            MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO,
            MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO,
            HistoricoVO historicoOrigemAluno,
            UsuarioVO usuario) throws Exception {
        HistoricoVO novoHistorico = (HistoricoVO) historicoOrigemAluno.clone();
        novoHistorico.setCodigo(0); // zerando o codigo para forcar uma nova inclusao

        // nao temos que alterar em nada o periodoletivodamatriz que mantem vinculo com o periodo da matriz
        // que o aluno está estuando (no caso gradeOrigem) que nao será alterada.
        PeriodoLetivoVO periodoMatrizCurricularVO = historicoOrigemAluno.getPeriodoLetivoMatrizCurricular();

        // Mantendo o matriculaPeriodoTurmaDisciplinaAtualizar do historico, pois o mesmo é limpo no método
        // inicializarDadosBasicosNovoHistoricoMigrado e adicionalmente iremos precisar dele para podermos atualiza-lo posteriormente neste método
        MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAtualizar = novoHistorico.getMatriculaPeriodoTurmaDisciplina();
        inicializarDadosBasicosNovoHistoricoMigrado(novoHistorico, transferencia, matriculaProcessar, periodoMatrizCurricularVO, periodoLetivoCursar, usuario);

        // marcando como fora da grade, pois trata-se de um historico de uma disciplina cursada fora da grade
        novoHistorico.setHistoricoDisciplinaForaGrade(Boolean.TRUE);

        // -------------------------------------------------------------------------------
        // CONSIDERACOES SOBRE GRADEDISCIPLINA / GRADECURRICULARGRUPOOPTATIVADISCIPLINA
        // nao iremos alterar nada com relação a gradeDisciplina/gradeDisciplinaGrupoOptativa
        // pois aqui temos que a gradeDisicplina e/ou gradeDisciplinaGrupoOptativa que estão 
        // vinculadas no histórico vão ser mantidas inalteradas. O que vai mudar é o vínculo
        // deste histórico com um novo mapa de equivalencia, que irá fazer a ponte (ligação)
        // entre a matriz origem e a destino
        // Com relação a disciplina composta, como alteramos a gradeDisciplina/GrupoOptativa na 
        // qual o aluno estudava, então não temos impactos do ponto de vista de disciplinas compostas.
        // -------------------------------------------------------------------------------
        String msgInicial = "Histórico migrado da matriz (" + transferencia.getGradeOrigem().getCodigo() + ") ";
        if (historicoOrigemAluno.getHistoricoEquivalente()) {
            // Registrando observacao caso o historico da gradeOrigem era equivalente (ou seja, fora grade)
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", "Histórico estava sendo cursado como equivalente (ou seja, não pertencia a esta matriz). Mapa de equivalência de código " + historicoOrigemAluno.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }
        if (novoHistorico.getHistoricoEquivalente()) {
            // Registrando observacao caso o historico da gradeOrigem era equivalente (ou seja, fora grade)
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " foi cursado como equivalente (ou seja, não pertencia a esta matriz). Mapa de equivalência de código " + novoHistorico.getMapaEquivalenciaDisciplina().getCodigo() + ". Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        } else {
            // Registrando observacao que histórico na gradeOrigem estava dentro da grade 
            // mas que agora passou a ser considerado como fora da grade
            novoHistorico.adicionarObservacaoTransferenciaMatrizCurricular(null, "", msgInicial + " passou a ser considerado como FORA da matriz curricular destino. Histórico origem de código " + historicoOrigemAluno.getCodigo(), true);
        }

        // Alterando as referencia do novo histórico de forma que o mesmo passe a referenciar
        // a mapa de equivalencia fornecido como parametro
        novoHistorico.setHistoricoEquivalente(Boolean.TRUE);
        novoHistorico.setHistoricoPorEquivalencia(Boolean.FALSE);
        novoHistorico.setHistoricoDisciplinaComposta(false);
        novoHistorico.setHistoricoDisciplinaFazParteComposicao(false);
        novoHistorico.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
        novoHistorico.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);
        novoHistorico.setMapaEquivalenciaDisciplinaMatrizCurricular(null);
        
        // Alterando os dados da matriculaPeriodoTurmaDisciplina para nova situação de equivalencia, 
        // dada a transferencia de matrizCurricular do aluno.
        alterarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferenciaMatriz(matriculaProcessar, matriculaPeriodoTurmaDisciplinaAtualizar, mapaEquivalenciaDisciplinaVO, mapaEquivalenciaDisciplinaCursadaVO, usuario);

        // Vinculando o novoHistorico gerado a matriculaPeriodoTurmaDisciplina já atualizada com os
        // dados do mapa de equivalencia necessário
        novoHistorico.setMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaAtualizar);

        // Registrando o histórico gerado no mapa, pois este mapa ainda será avaliado
        // e aproveitado ainda mais do ponto de vistas de disciplinas que o aluno está 
        // cursando atualmente.
        mapaEquivalenciaDisciplinaCursadaVO.setHistorico(novoHistorico);
        // migrar os dados acadêmicos para o novo histórico
        getFacadeFactory().getHistoricoFacade().migrarNotasEDadosAcademicosEntreHistoricos(historicoOrigemAluno, novoHistorico);
        // gravando novo histórico
        getFacadeFactory().getHistoricoFacade().incluir(novoHistorico, usuario);
        getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(historicoOrigemAluno.getCodigo(), matriculaProcessar.getCodigo(), true, usuario);

        matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoOrigemAluno);
    }

    /**
     * Este método vida obter uma mapa de equivalencia para uma disciplina que o aluno esteja cursando.
     * Este método já considera que todos os possíveis mapas para disciplinas nos quais o aluno esteja
     * aprovado, já foram processados. Por isto, o mesmo trabalha com considerando que o mapa a ser
     * obtido será aplicável somente para disciplinas que estão sendo cursada.
     * @return 
     */
    private MapaEquivalenciaDisciplinaCursadaVO verificarEObterMelhorMapaEquivalenciaParaDisciplinaAlunoCursando(
            TransferenciaMatrizCurricularVO transferencia, HistoricoVO historicoCursandoVO) throws Exception {
        // Obtendo lista de mapas que podem ser aplicados para a disciplina cursada.
        List<MapaEquivalenciaDisciplinaVO> listaMapasValidos = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
                transferencia.getGradeMigrar().getCodigo(), historicoCursandoVO.getDisciplina().getCodigo(),
                historicoCursandoVO.getCargaHorariaDisciplina(), NivelMontarDados.TODOS);

        if (listaMapasValidos.isEmpty()) {
            return null;
        }

        MapaEquivalenciaDisciplinaVO melhorMapa = null;
        // Determinando o melhor mapa a ser utilizado para o aluno
        for (MapaEquivalenciaDisciplinaVO mapaAvaliar : listaMapasValidos) {
            if (melhorMapa == null) {
                // na primeira interação assumi-se o primeiro mapa como o melhor
                melhorMapa = mapaAvaliar;
            } else {
                if (mapaAvaliar.getMapaEquivalenciaDisciplinaCursadaVOs().size() < melhorMapa.getMapaEquivalenciaDisciplinaCursadaVOs().size()) {
                    // se o mapa que estamos avaliando tem um número menor de disciplinas a ser cursada
                    // então iremos assumir este mapa como sendo o melho
                    melhorMapa = mapaAvaliar;
                }
            }
        }
        for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursaMelhorMapa : melhorMapa.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if ((disciplinaCursaMelhorMapa.getDisciplinaVO().getCodigo().equals(historicoCursandoVO.getDisciplina().getCodigo()))
                    && (disciplinaCursaMelhorMapa.getCargaHoraria().equals(historicoCursandoVO.getCargaHorariaDisciplina()))) {
                // carregando os dados do mapa de equivalencia
                disciplinaCursaMelhorMapa.setMapaEquivalenciaDisciplina(melhorMapa);
                return disciplinaCursaMelhorMapa;
            }
        }
        return null;
    }

    /**
     * Método responsável por criar uma mapa de equivalencia automaticamente, vinculando a disciplina que o 
     * alnuo está cursando atualmente a uma disciplina correspondente (mesmo código de disciplina e carga horária)
     * na grade destino. De forma que quando o aluno concluir os estudos na gradeAtual, então o SEI irá automaticamente
     * aprovar o aluno na matriz Destino.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private boolean verificarExisteDisciplinaCorrespondenteMatrizMigrarEGerarHistoricoCorrespondenteMatrizDestino(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            HistoricoVO historicoCursandoVO, UsuarioVO usuario) throws Exception {
        GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = null;
        GradeDisciplinaVO gradeDisciplinaVO = transferencia.getGradeMigrar().obterGradeDisciplinaCorrespondente(historicoCursandoVO.getDisciplina().getCodigo(), historicoCursandoVO.getCargaHorariaDisciplina());
        if (gradeDisciplinaVO == null) {
            gradeCurricularGrupoOptativaDisciplinaVO = transferencia.getGradeMigrar().obterGradeCurricularGrupoOptativaCorrespondente(historicoCursandoVO.getDisciplina().getCodigo(), historicoCursandoVO.getCargaHorariaDisciplina());
            if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
                // Se chegar aqui é por que não existem disciplinas correspondentes na matriz destino,
                // com relação as disciplinas que o aluno está cursando agora.
                return false;
            }
        }
        if (gradeDisciplinaVO != null) {
        	// deve ser verificado a existência do histórico na matriz destino
        	// caso haja não pode ser gerado mais um histórico afim de evitar a duplicidade
        	PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(gradeDisciplinaVO.getPeriodoLetivoVO().getCodigo());
        	// se for nulo significa que não existe a disciplina correpondente na matriz destino
        	// por isso deverá ser gerado o histórico correspondente
        	if (periodoLetivoComHistoricoAlunoVO == null) {
				return true;
			}
            HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplinaVO.getCodigo());
            if (historicoDisciplinaGradeDestino != null && !historicoDisciplinaGradeDestino.getCodigo().equals(0)) {
            	
            	if (historicoDisciplinaGradeDestino.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor())) {
            		getFacadeFactory().getHistoricoFacade().migrarNotasEDadosAcademicosEntreHistoricos(historicoCursandoVO, historicoDisciplinaGradeDestino);
            		getFacadeFactory().getHistoricoFacade().alterar(historicoDisciplinaGradeDestino, usuario);
            	}
            	return false;
            } else {
            	gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaVO, historicoCursandoVO, true, usuario);
            }
        } else {
        	// deve ser verificado a existência do histórico na matriz destino
        	// caso haja não pode ser gerado mais um histórico afim de evitar a duplicidade
        	PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(gradeCurricularGrupoOptativaDisciplinaVO.getPeriodoLetivoDisciplinaReferenciada().getCodigo());
        	// se for nulo significa que não existe a disciplina correpondente na matriz destino
        	// por isso deverá ser gerado o histórico correspondente
        	if (periodoLetivoComHistoricoAlunoVO == null) {
				return true;
			}
        	HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo());
        	if (historicoDisciplinaGradeDestino != null && !historicoDisciplinaGradeDestino.getCodigo().equals(0)) {
        		if (historicoDisciplinaGradeDestino.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor())) {
            		getFacadeFactory().getHistoricoFacade().migrarNotasEDadosAcademicosEntreHistoricos(historicoCursandoVO, historicoDisciplinaGradeDestino);
            		getFacadeFactory().getHistoricoFacade().alterar(historicoDisciplinaGradeDestino, usuario);
            	}
            	return false;
        	} else {
        		gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaVO, gradeCurricularGrupoOptativaDisciplinaVO.getPeriodoLetivoDisciplinaReferenciada(), historicoCursandoVO, true, usuario);
        	}
        }
        return true;
    }

    /**
     * Método responsável por redirecionar as matriculasPeriodosTurmaDisciplina que o aluno está atualmente cursando
     * de forma que as disciplinas que o aluno estiver atualmente cursando sejam mantidas. Ou seja, se o aluno estiver
     * estudando uma disciplina X, após a transferência ele continua estudando a mesma disciplina, na mesma turma, na mesma
     * grade origem (grade antes da transferencia). Isto pode ser feito de duas formas. Primeiro, por meio, de uma mapa
     * de equivalencia, caso exista algum compatível com a disciplnia que está sendo cursada. Caso não haja, iremos verificar
     * se a disciplina que está sendo cursada existe de forma identica (mesmo código e carga horária) na grade destino.
     * Neste caso iremos utilizar um recurso denominado historicoPorCorrespondencia
     * que indica que quando o usuário for aprovado neste histórico da grade anterior, ele automaticamente será aprovado
     * no histórico correspondente na grade destino. Este controle será feito por meio dos atributos
     * historicoCursandoPorCorrespondenciaAposTransferencia no historicoVO e na MatriculaPeriodoTurmaDisciplinaVO
     * Um aspecto importante, é que devemos ser capazes de desfazer
     * uma transferência de matriz curricular. Para garantirmos este recurso, teremos que gravar na matriculaPeriodoTurmaDisciplina
     * qual transferenciaMatrizCurricular que modificou o mesmo e se foi utilizado de uma mapaEquivalencia para fazer a inferencia
     * a nova grade. Se isto ocorreu, temos que ser capazes de ao usuário cancelar a transferencia, retornar o matricularPeriodoTurmaDisciplina
     * exatamente como estava antes.
     * Este método precisa trabalhar com as seguintes possibilidades.
     *    a) Existe um mapa de equivalencia que já está sendo utilizado na matriz, então vamos utilizá-lo para que o aluno continue
     *         estudando a disciplina e este estudo seja útil na gradeDestino.
     *    b) O histórico em questão era de uma disciplina regular na matriz anterior e HÁ uma correspondencia (disciplina similar)
     *         na matriz destino (caso comum). Neste caso iremos verificar se há uma mapa de equivalencia que determina este mapeamento
     *         entre as disciplinas correspondentes da gradeOrigem e destino. Caso não exista este mapa então iremos marcar o
     *         historico e a matriculaPeriodoTurmaDisciplina para indicar que os mesmos estão sendo cursados por correspencia na
     *         matriz anterior (origem).
     *    c) O histórico em questão era de uma disciplina regular na matriz anterior e NÃO há uma correspondencia. Logo iremos
     *         verificar se o mesmo pode ser aproveitado em alguma mapa já utilizado da GradeDestino. Caso contrário, iremos ver se
     *         há algum mapa criado especificamente para ele. Caso não seja encontrado nenhum mapa correspondente, então a disciplina
     *         será programada como sendo fora da grade (estudando fora da grade). Caso seja localizado um mapa o historico (novoHistorico 
     *         criado) e a MatriculaPeriodoTurmaDisciplina serão reprogramados para este novo mapa. Ficará registrado que foi alterado o 
     *         mapaEquivalencia da MatriculaPeriodoTurmaDisciplina de forma que caso o usuário cancele a transferenciaMatriCurricular, esta 
     *         MatriculaPeriodoTurmaDisciplina pode ser redirecionada novamente para o mapaAntigo. Este mapa antigo pode ser obtido pelo
     *         historico da matrizOrigem (antiga) que replica estas informações existentes na MatriculaPeriodoTurmaDisciplina.
     *     d) Caso a disciplina em questão seja composta na gradeOrigem, isto não implicará em mudanças na nossa regra. Pois, quando o aluno
     *         for aprovado nas composições, que por sua vez irá gerar a aprovação na mãe (da composição) então o mapa de equivalencia tambem
     *         irá atuar. Fazendo que a(s) disciplina(s) na gradedestino sejam aprovadas por equivalencia.
     * 
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void verificarERedirecionarMatriculaPeriodoTurmaDisciplinaParaEquivalenciaComCorrespondenciaGradeDestino(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            UsuarioVO usuario) throws Exception {
        List<HistoricoVO> disciplinasAlunoCursando = matriculaProcessar.getDisciplinasAlunoCursando();
        
        PeriodoLetivoVO periodoLetivoCursar = matriculaProcessar.getPeriodoLetivoUtilizarDisciplinasCursar();
        Integer codigoMapaEquivalenciaUtilizar = transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo();
        int index = disciplinasAlunoCursando.size() - 1;
        while (index >= 0) {
        	HistoricoVO historicoCursandoVO = disciplinasAlunoCursando.get(index);
        	
        	if (historicoCursandoVO.getMatriculaPeriodo().getCodigo().equals(matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO().getCodigo())) {

        		boolean alterarHistoricoParaRefletirCorrespodencia = Boolean.TRUE;
        		if (historicoCursandoVO.getHistoricoDisciplinaComposta()) {
        			// Se a disciplina é composta, temos duas situação para considerar:
        			// a) primeiro, se as filhas dela foram aproveitas para pagar disciplinas filhas da composicao na
        			//  na grade destino. Entao nao teremos que processar este historico / matriculaPeriodoTurmaDisciplina.
        			//  Pois quando o usuario terminar de estudar as disciplinas filhas da matriz origem, as filhas da matriz destino
        			//  já serão atualizadas. Tanto da matriz origem quanto matriz destino, a mae será atualizada automaticamente, com
        			//  com base no resultado das filhas.
        			// b) segundo, as filhas nao foram aproveitadas e a mae também nao foi aproveitada. Ou seja, a mae ainda consta na lista
        			//  de disciplinas pendentes para processamento. Neste caso iremos processar sim o historico  matrizPeriodoTurmaDisciplina
        			//  pois pode ocorrer da mesma, ao ser finalizada (aprovada) ela sirva para pagar uma disciplina comum (nao composta)
        			//  na matrizDestino.

        			List<GradeDisciplinaCompostaVO> listaDisciplinasComposicao = null;
        			if (historicoCursandoVO.getDisciplinaReferenteAUmGrupoOptativa()) {
        				listaDisciplinasComposicao = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGradeCurricular(historicoCursandoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario); 
        			} else {
        				listaDisciplinasComposicao = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGradeDisciplina(historicoCursandoVO.getGradeDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
        			}

        			boolean filhaAproveitadaTransferencia = Boolean.FALSE;
        			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : listaDisciplinasComposicao) {
        				for (HistoricoVO historicoAproveitado : matriculaProcessar.getHistoricoAproveitadosTransferencia()) {
        					if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(historicoAproveitado.getDisciplina().getCodigo())) &&
        							(gradeDisciplinaCompostaVO.getCargaHoraria().equals(historicoAproveitado.getCargaHorariaDisciplina()))) {
        						filhaAproveitadaTransferencia = Boolean.TRUE;
        					}
        				}
        			}

        			if (filhaAproveitadaTransferencia) {
        				alterarHistoricoParaRefletirCorrespodencia = Boolean.FALSE;
        			}
        		} 
        		if (historicoCursandoVO.getHistoricoDisciplinaFazParteComposicao()) {
        			// Se a disciplina faz parte de uma composicao, temos duas possibilidades para considerar:
        			// a) primeiro, que esta filha de composicao já foi endereçada para outra disciplina filha de composicao
        			//   na matriz destino. Neste caso nao poderemos processar este historico / matriculaPeriodoTurmaDisciplina
        			//   Pois ao aluno ser aprovado na filha da composicao, o historico já está alterado e preparado para refletir
        			//   na filha da composicao da matriz destino. A resolucao da mae ocorrerá automaticamente depois de resolvida
        			//   esta correspodencia.
        			// b) segundo, esta filha de compoiscao nao pode ser enderecada a nenhum disciplina filha de composicao
        			//   na matriz destino. Neste caso, tambem nao teremos que processar este historico / matriculaPeriodoTurmaDisciplina
        			//   pois quando o usuario terminar de estudá-la na verdade irá quitar a disciplina mae. Esta mae, sim, poderá ter 
        			// ou nao impactos na matriz destino (caso a mesma esteja sendo cursada por correspodencia).
        			alterarHistoricoParaRefletirCorrespodencia = Boolean.FALSE;

        			boolean filhaAproveitadaTransferencia = Boolean.FALSE;
        			for (HistoricoVO historicoAproveitado : matriculaProcessar.getHistoricoAproveitadosTransferencia()) {
        				if ((historicoCursandoVO.getDisciplina().getCodigo().equals(historicoAproveitado.getDisciplina().getCodigo())) &&
        						(historicoCursandoVO.getCargaHorariaDisciplina().equals(historicoAproveitado.getCargaHorariaDisciplina()))) {
        					filhaAproveitadaTransferencia = Boolean.TRUE;
        				}
        			}        		 
        			if (filhaAproveitadaTransferencia) {
        				// como a filha foi aproveita, vamos modificar a matriculaPeriodoTurmaDisciplina para ficar evidenciado que a mesma
        				// esta sendo cursada por correspodencia para pagar uma disciplina na outra matriz.
        				historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        				historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean.TRUE);
        				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina(), usuario);
        			}

        		}

        		if (alterarHistoricoParaRefletirCorrespodencia) {
        			// Determinando um mapa de equivalencia que possa ser utilizado para disciplina que está sendo cursada, dentre
        			// os mapas que já foram utilizados no aproveitamento
        			MapaEquivalenciaDisciplinaCursadaVO mapaAdotar = obterMapaEquivalenciaCorrenpodenteHistoricoListaMapaEquivalenciaUtilizadoTransferencia(matriculaProcessar, historicoCursandoVO);
        			// O codigo mapa equivalencia utilizar precisa ser maior que zero
        			// pois se o mesmo estiver zero significa que não foi selecionado nenhum
        			// mapa de equivalencia na tela de transferência. nesse caso deverá realizar a transferência sem mapa de equivalencia
        			// regra definida conversada com o Edgar por telefone
        			// Autor Carlos 15/08/2014
        			if (mapaAdotar == null && !codigoMapaEquivalenciaUtilizar.equals(0)) {
        				// Se nao encontramos um mapa já utilizado adequado para o histórico, iremos verificar se existe 
        				// uma mapa de equivalencia que pode ser aplicado para a disciplina
        				// que está sendo cursada. Haja vista que não encontramos o mapa acima.
        				mapaAdotar = verificarEObterMelhorMapaEquivalenciaParaDisciplinaAlunoCursando(transferencia, historicoCursandoVO);
        			}

        			// Carregando os dados da MatriculaPeriodoTurmaDisciplina corresponde ao histórico que está sendo cursado.
        			if (historicoCursandoVO.getSituacao().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor())) {
        				if (Uteis.isAtributoPreenchido(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina())) {
        					MatriculaPeriodoTurmaDisciplinaVO dadosMatriculaPeriodoTurmaDisciplinaAtualizados = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
        					historicoCursandoVO.setMatriculaPeriodoTurmaDisciplina(dadosMatriculaPeriodoTurmaDisciplinaAtualizados);
        				}
        			} else {
        				if (Uteis.isAtributoPreenchido(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina())) {
        					MatriculaPeriodoTurmaDisciplinaVO dadosMatriculaPeriodoTurmaDisciplinaAtualizados = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
        					historicoCursandoVO.setMatriculaPeriodoTurmaDisciplina(dadosMatriculaPeriodoTurmaDisciplinaAtualizados);
        				}
        			}
        			
        			// Eliminando os vinculos de equivalência e correspondência a fim de converter o histórico como uma disciplina normal
        			realizarEliminacaoEquivalenciaECorrespondenciaAfimDeTransferirComoDisciplinaNormalHistorico(historicoCursandoVO, transferencia, matriculaProcessar, usuario);
        			if (historicoCursandoVO.getHistoricoEquivalente() || historicoCursandoVO.getHistoricoPorEquivalencia()) {
        				index = index - 1;
        				continue;
        			}

        			if (mapaAdotar != null) {
        				// Método responsável por verificar se existe um histórico correspondente na matriz de destino
        				// caso exista não pode ser gerado novo histórico senão gerará duplicidade dos históricos
        				// deve ser migrado as notas e atualizado para o novo histórico.
        				if (!verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoCursandoVO, transferencia, matriculaProcessar, true, usuario)) {
        					// Esse boolean irá verificar a existência de um histórico no momento de incluir o histórico por equivalência 
        					// caso haja será feita a exclusão do mesmo afim de evitar duplicidade.
        					historicoCursandoVO.setVerificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade(true);
        					// Se encontramos o mapa então vamos gerar o histórico e vincular a matricula periodo turma disciplina 
        					// a este novo mapa;
        					gerarHistoricoCursandoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(transferencia, matriculaProcessar, periodoLetivoCursar, mapaAdotar.getMapaEquivalenciaDisciplina(), mapaAdotar, historicoCursandoVO, usuario);
        					index = index - 1;
        					continue;
        				}
        				matriculaProcessar.getMapasEquivalenciaAproveitadosTransferenciaComHistoricos().add(mapaAdotar.getMapaEquivalenciaDisciplina());

        				// como foi gerado um histórico na gradeDestino do tipo CURSANDO_POR_EQUIVALÊNCIA
        				// (cuja situacao é SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA) agora temos que alterar o historicoOrigem
        				// para indicar que o mesmo está sendo cursado na gradeAntigo, com intuito de que quando o alnuo finalizar
        				// seus estudos (na grade antiga) automaticamente o SEI finalize também o histórico correspondente 
        				// (DO TIPO: CURSANDO_POR_EQUIVALENCIA) na gradeDestino. Assim, a tranferencia de matriz pode ser realizada
        				// durante o semestre sem prejuízo para os alunos que estão estudando.
        				getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(historicoCursandoVO.getCodigo(), matriculaProcessar.getCodigo(), true, usuario);

        				historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
//        				historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean.TRUE);
        				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina(), usuario);
        			} else {
        				// Caso ainda não o histórico não possua o vinculo com a MatriculaPeriodoTurmaDisciplina será feita uma consulta 
        				// buscando uma correspondente para incluir no histórico. Essa perca acontece devido a várias mudanças de matriz curricular para a mesma matriz
        				if (!Uteis.isAtributoPreenchido(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina())) {
    						MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAtualizadaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaMatriculaPeriodoDisciplinaCargaHoraria(historicoCursandoVO.getMatricula().getMatricula(), historicoCursandoVO.getMatriculaPeriodo().getCodigo(), historicoCursandoVO.getDisciplina().getCodigo(), historicoCursandoVO.getCargaHorariaDisciplina(), usuario);
    						if (!matriculaPeriodoTurmaDisciplinaAtualizadaVO.getCodigo().equals(0)) {
    							historicoCursandoVO.setMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaAtualizadaVO);
    							getFacadeFactory().getHistoricoFacade().alterarMatriculaPeriodoTurmaDisciplina(historicoCursandoVO.getCodigo(), matriculaPeriodoTurmaDisciplinaAtualizadaVO.getCodigo(), usuario);
    						}
    					}
        				// Caso contrário verificar se existe uma disciplina correspondete. Caso sim, iremos deixar
        				// o aluno estudando a disciplina atual, mais iremos marcá-la determinando que a mesma está
        				// sendo estuda na grade/turma antiga (grade origem) por correspodencia. Ou seja, quando o aluno
        				// for aprovado (ou reprovado) na mesma, o SEI irá automaticamente aprová-lo na disciplina correspondente
        				// na gradeDestino.
        				boolean gerouCorrespodencia = verificarExisteDisciplinaCorrespondenteMatrizMigrarEGerarHistoricoCorrespondenteMatrizDestino(transferencia, matriculaProcessar, historicoCursandoVO, usuario);
        				if (gerouCorrespodencia) {
        					// como foi gerado um histórico na gradeDestino do tipo CURSANDO_POR_CORRESPONCIA
        					// (cuja situacao é SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA) agora temos que alterar o historicoOrigem
        					// para indicar que o mesmo está sendo cursado na gradeAntigo, com intuito de que quando o alnuo finalizar
        					// seus estudos (na grade antiga) automaticamente o SEI finalize também o histórico correspondente 
        					// (DO TIPO: CURSANDO_POR_CORRESPONCIA) na gradeDestino. Assim, a tranferencia de matriz pode ser realizada
        					// durante o semestre sem prejuízo para os alunos que estão estudando.
        					getFacadeFactory().getHistoricoFacade().alterarVinculoHistoricoCursandoTransferenciaMatrizCurricularMatricula(historicoCursandoVO.getCodigo(), matriculaProcessar.getCodigo(), SituacaoHistorico.CURSANDO.getValor(), true, usuario);

        					// No caso da matrícula Período, a única modificação que faremos é vinculá-lo a transferenciaMatrizCurricularMatricula
        					// para que possá-los localizá-lo no caso do cancelamento da transferencia. E também iremos sinalizar que o aluno
        					// está estudando esta disciplina por correspondencia. Os demais atributos, relativos a mapa de equivalencia, permanecem
        					// identicos aos anteriores
        					historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().setTransferenciaMatrizCurricularMatricula(matriculaProcessar);
        					historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina().setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean.TRUE);
        					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(historicoCursandoVO.getMatriculaPeriodoTurmaDisciplina(), usuario);
        				}
        			}
        		}
        	}
        	index = index - 1;
        }
    }
    
    public void realizarEliminacaoEquivalenciaECorrespondenciaAfimDeTransferirComoDisciplinaNormalHistorico(HistoricoVO historicoCursandoVO, TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, UsuarioVO usuarioVO) throws Exception {
    	if (historicoCursandoVO.getHistoricoEquivalente()) {

    		verificarExistenciaHistoricoSendoCursadoMatrizDestino(historicoCursandoVO, transferencia, matriculaProcessar, true, usuarioVO);
    		historicoCursandoVO.setMatriculaPeriodoTurmaDisciplina(null);
    		getFacadeFactory().getHistoricoFacade().excluir(historicoCursandoVO, false, usuarioVO);
			
		} else if (historicoCursandoVO.getHistoricoPorEquivalencia()) {
			historicoCursandoVO.setMatriculaPeriodoTurmaDisciplina(null);
			getFacadeFactory().getHistoricoFacade().excluir(historicoCursandoVO, false, usuarioVO);
			
		} else if (historicoCursandoVO.getHistoricoCursandoPorCorrespondenciaAposTransferencia()) {
			historicoCursandoVO.setHistoricoCursandoPorCorrespondenciaAposTransferencia(false);
			getFacadeFactory().getHistoricoFacade().alterarEliminandoVinculoHistoricoCursandoPorCorrespondencia(historicoCursandoVO.getCodigo(), usuarioVO);
			
		}
    }
    
    
    
	public Boolean verificarExistenciaHistoricoSendoCursadoMatrizDestino(HistoricoVO historicoCursandoVO, TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, Boolean migrarNotasEDadosAcademicos, UsuarioVO usuarioVO) throws Exception {
		GradeDisciplinaVO gradeDisciplinaVO = null;
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = null;
		
		gradeDisciplinaVO = transferencia.getGradeMigrar().obterGradeDisciplinaCorrespondente(historicoCursandoVO.getDisciplina().getCodigo(), historicoCursandoVO.getCargaHorariaDisciplina());
		if (gradeDisciplinaVO == null) {
			gradeCurricularGrupoOptativaDisciplinaVO = transferencia.getGradeMigrar().obterGradeCurricularGrupoOptativaCorrespondente(historicoCursandoVO.getDisciplina().getCodigo(), historicoCursandoVO.getCargaHorariaDisciplina());
			if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
				// Se chegar aqui é por que não existem disciplinas
				// correspondentes na matriz destino,
				// com relação as disciplinas que o aluno está cursando
				// agora.
				return false;
			}
		}
		
		if (gradeDisciplinaVO != null) {
			PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(gradeDisciplinaVO.getPeriodoLetivoVO().getCodigo());
			if (periodoLetivoComHistoricoAlunoVO == null) {
				return false;
			}
			HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplinaVO.getCodigo());
			if (historicoDisciplinaGradeDestino != null && !historicoDisciplinaGradeDestino.getCodigo().equals(0)) {
				
				if (historicoDisciplinaGradeDestino.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor()) && migrarNotasEDadosAcademicos) {
					getFacadeFactory().getHistoricoFacade().migrarNotasEDadosAcademicosEntreHistoricos(historicoCursandoVO, historicoDisciplinaGradeDestino);					
					inicializarDadosBasicosNovoHistoricoMigrado(historicoDisciplinaGradeDestino, transferencia, matriculaProcessar, historicoDisciplinaGradeDestino.getPeriodoLetivoMatrizCurricular(), historicoDisciplinaGradeDestino.getPeriodoLetivoCursada(), usuarioVO);
					historicoDisciplinaGradeDestino.setHistoricoEquivalente(Boolean.FALSE);
					historicoDisciplinaGradeDestino.setHistoricoPorEquivalencia(Boolean.FALSE);
					historicoDisciplinaGradeDestino.setMapaEquivalenciaDisciplina(null);
					historicoDisciplinaGradeDestino.setMapaEquivalenciaDisciplinaCursada(null);
					historicoDisciplinaGradeDestino.setMapaEquivalenciaDisciplinaMatrizCurricular(null);					
					getFacadeFactory().getHistoricoFacade().alterar(historicoDisciplinaGradeDestino, usuarioVO);
					if (!historicoDisciplinaGradeDestino.getReprovado()) {
				         // vamos adicionar para a lista abaixo somente os históricos de aprovação e cursando que foram aproveitados
				         matriculaProcessar.getHistoricoAproveitadosTransferencia().add(historicoCursandoVO);
				    }
				}
				return true;
			}
		} else {
			PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaProcessar.getMatriculaDestinoComHistoricoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(gradeCurricularGrupoOptativaDisciplinaVO.getPeriodoLetivoDisciplinaReferenciada().getCodigo());
			if (periodoLetivoComHistoricoAlunoVO == null) {
				return false;
			}
			HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo());
			if (historicoDisciplinaGradeDestino != null && !historicoDisciplinaGradeDestino.getCodigo().equals(0)) {
				
				if (historicoDisciplinaGradeDestino.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor()) && migrarNotasEDadosAcademicos) {
					getFacadeFactory().getHistoricoFacade().migrarNotasEDadosAcademicosEntreHistoricos(historicoCursandoVO, historicoDisciplinaGradeDestino);
					inicializarDadosBasicosNovoHistoricoMigrado(historicoDisciplinaGradeDestino, transferencia, matriculaProcessar, historicoDisciplinaGradeDestino.getPeriodoLetivoMatrizCurricular(), historicoDisciplinaGradeDestino.getPeriodoLetivoCursada(), usuarioVO);
					historicoDisciplinaGradeDestino.setHistoricoEquivalente(Boolean.FALSE);
					historicoDisciplinaGradeDestino.setHistoricoPorEquivalencia(Boolean.FALSE);
					historicoDisciplinaGradeDestino.setMapaEquivalenciaDisciplina(null);
					historicoDisciplinaGradeDestino.setMapaEquivalenciaDisciplinaCursada(null);
					historicoDisciplinaGradeDestino.setMapaEquivalenciaDisciplinaMatrizCurricular(null);
					getFacadeFactory().getHistoricoFacade().alterar(historicoDisciplinaGradeDestino, usuarioVO);
				}

				return true;
			}
		}
		return false;
	}
	

    /**
     * Método responsável por migrar todos os históricos da grade origem para a grade
     * destino, que referem-se a mesma disciclina e carga horária. Ou seja, são os históricos
     * das disciplinas que foram mantidas sem nenhuma alteracao na grade destino.
     * Adicionalmente, este método irá gerar uma lista de históricos pendentes. Ou seja,
     * históricos de disciplinas que nao foram encontrados na grade destino e, por isto,
     * terão que ser tratados por meio de outros recursos, como o mapa de equivalencia.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarMigracaoMatriculaGradeOrigemParaGradeDestino(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            UsuarioVO usuario) throws Exception {
        GradeCurricularVO gradeDestino = transferencia.getGradeMigrar();

        matriculaProcessar.getDisciplinasAlunoCursando().clear();
        
        // montando lista de historico que o aluno esta cursando para controlar quais já foram transferidos e quais ainda estao pendentes
        matriculaProcessar.getDisciplinasAlunoCursando().addAll(matriculaProcessar.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular());

        // Migrando todas as gradeDisciplinasVO´s da gradeOrigem (aprovadas, reprovadas e cursando) que possuem correspondencia na gradeDestino
        verificandoEGerandoHistoricoGradeDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(transferencia, matriculaProcessar, gradeDestino, usuario);

        // Migrando todas as GradeCurricularGrupoOptativaDisciplinaVO´s da gradeOrigem (aprovadas, reprovadas e cursando) que possuem correspondencia na gradeDestino
        verificandoEGerandoHistoricoGradeCurricularGrupoOptativaDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(transferencia, matriculaProcessar, gradeDestino, usuario);

        // Os dois métodos acima irão processar todos os históricos que na gradeOrigem estavam vinculados
        // a gradeDisciplina e ou GradeCurricularGrupoOptativaDisciplina. Abaixo iremos processar os históricos
        // que o aluno está aprovado mas estão fora da grade origem do mesmo. Isto para verificar se estes históricos
        // que eram foram da grade origem, não podem ser aproveitados por correspodencia direta na grade destino
        verificandoEGerandoHistoricosForaGradeAprovadosGradeOrigemPorCorrespodenciaDireta(transferencia, matriculaProcessar, gradeDestino, usuario);
        
        // vamos verificar se existe alguma disciplina composta na grade destino, cuja composicao pode ser atendida por completo (para isto
        // teremos que observar a regra de resolução da composição - todas as disciplinas ou somente parte delas). Para isto iremos verificar
        // se os historicos remanescentes podem ser aproveitados nas filhas de composicao de forma a resolve-la por completo. Para o caso 
        // das filhas da composicao, também, iremos considerar os historicos que aluno está cursando. Pois, pode ocorrer do aluno ficar cursando
        // por correspodencia uma filha da composicao. De forma que, quando a correspodencia for resolvida, a mae também será resolvida automaticamente
        // na matriz destino.
        verificandoEGerandoHistoricosDisciplinaCompostaNaGradeDestinoCujaComposicaoPodeSerAtendidaPorCompleto(transferencia, matriculaProcessar, gradeDestino, usuario);	        
        
        // vamos buscar por históricos de disciplinas filhas da composicao, na matriz origem, que não
        // foram até este momento aproveitadas pela rotina de transferencia. Isto é importante, pois a rotina anterior
        // trabalha com foco nas composicao na matriz destino. Porém, podem ocorrer de existir disciplinas compostas na 
        // matriz origem, cusa a diciplina mae nao foi aproveitada (pois nao há correspodencia com ninguem). Contudo, a
        // disciplina filha pode ser correspendente à alguma disciplina da matriz destino. Este método visa identificar
        // esta situação e dar o devido tratamento à ele. Sempre que a disciplina filha for aproveitada, nao poderemos
        // mais aproveitar a disciplina mae (nem mesmo fora da grade), pois se isto ocorre-se teríamos uma mesma carga horária
        // sendo aproveita mais de uma vez.        
        verificandoEGerandoHistoricosDisciplinaFilhasComposicaoAindaNaoAproveitados(transferencia, matriculaProcessar, gradeDestino, usuario);
        
        // Neste ponto, temos que todas as correspondencias diretas já foram mapeadas e realizadas pelos métodos acima
        // Todos os históricos que não foram aproveitados por correspondencia direta serão tratados no método abaixo,
        // que buscará pelo melhor mapa de equivalencia a ser aplicado no sentido de aproveitar estes tipo de histórico.
        verificandoEGerandoHistoricosNaoAproveitadosGradeDestinoPorMapaEquivalencia(transferencia, matriculaProcessar, usuario);

        // Neste ponto, todos os históricos aprovados que ainda não foram aproveitados, não poderão mais ser aproveitados.
        // Logo iremos gerá-los na gradeDestino como históricos fora da grade. 
        gerandoHistoricosCorrespondetesNaoAproveitadosGradeDestinoComoForaGrade(transferencia, matriculaProcessar, usuario);

        // Neste ponto, iremos redirecionar as matriculasPeriodosTurmaDisciplina que o aluno está atualmente cursando
        // de forma que as disciplinas que o aluno estiver atualmente cursando sejam mantidas. Ou seja, se o aluno estiver
        // estudando uma disciplina X, após a transferência ele continua estudando a mesma disciplina, na mesma turma, na mesma
        // grade origem (grade antes da transferencia). Mas iremos utilizar uma mapa de equivalencia de forma que quando o 
        // aluno terminar de estudar esta disciplina na gradeOrigem, isto tem replexo em outra (ou outras disciplinas) da grade 
        // destino. Também iremos utilizar o conceito de cursar disciplina por correspodencia, para disciplina identicas nas duas grades
        verificarERedirecionarMatriculaPeriodoTurmaDisciplinaParaEquivalenciaComCorrespondenciaGradeDestino(transferencia, matriculaProcessar, usuario);
    }

    /**
     * Método responsável por realizar a migracao de uma matricula
     * de uma matriz origem para uma destino. A chamar este métodos
     * as matrizes curriculares (assim como os mapas de equivalencia)
     * origem e destino já deveme estar com todos os dados montados.
     * @param transferencia
     * @param matriculaProcessar
     * @param usuario
     * @throws Exception 
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean realizarMigracaoMatrizCurricularMatricula(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            UsuarioVO usuario) throws Exception {
        try {
            if (!matriculaProcessar.getPodeSerProcessada()) {
                return false;
            }

            // É necessário atualizar a situação da matriucla, pois um processamento anterior pode ter alterado a grade curricular
            // da mesma por exemplo.
            matriculaProcessar.getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);           
            getFacadeFactory().getMatriculaFacade().carregarDados(matriculaProcessar.getMatriculaVO(), NivelMontarDados.BASICO, usuario);
            
            ConfiguracaoAcademicoVO cfgAcademicaCarregadaVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(matriculaProcessar.getMatriculaVO().getCurso().getConfiguracaoAcademico().getCodigo(), usuario);
            matriculaProcessar.getMatriculaVO().getCurso().setConfiguracaoAcademico(cfgAcademicaCarregadaVO);
            
            if (!matriculaProcessar.getMatriculaVO().getGradeCurricularAtual().getCodigo().equals(transferencia.getGradeOrigem().getCodigo())) {
                throw new Exception("Esta matrícula não está mais vinculada a Matriz Curricular Origem " + transferencia.getGradeOrigem().getNome() + ". Por isto não pode ser processada nesta transferência de matriz.");
            }
            if (getFacadeFactory().getHistoricoFacade().verificarExisteHistoricoMatriculaVinculadoGradeDestino(matriculaProcessar.getMatriculaVO().getMatricula(), transferencia.getGradeMigrar().getCodigo())) {
            	// Caso existam historicos na matriz destino, significa que o usuario poderá migrar para a mesma, desde que o mesmo nao esteja cursando
            	// nenhuma disciplina por correspodencia. Como existe atualmente. a possibilidade do usuário migrar as disciplinas que estao sendo cursadas
            	// por correspodencia para a nova turma / nova matriz, significa que poderá ser possível ao usuário realizar uma nova transferencia do aluno
            	// para outra matriz (mesmo que seja uma matriz na qual o aluno já tenha cursado uma vez no passado).
            	if (matriculaProcessar.getNrDisciplinasCursandoPorCorrespondencia() > 0) {
                    throw new Exception("Esta matrícula ainda possui disciplinas sendo cursadas por correspodência. Uma nova transferência só poderá ser realizada quando todas estas disciplinas forem migradas para a NOVA MATRIZ / TURMAS DA NOVA MATRIZ.");
            	}
            	matriculaProcessar.setValidarHistoricoJaEstaAprovadoMatrizDestino(Boolean.TRUE); 
            }
            MatriculaComHistoricoAlunoVO matriculaDestinoComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(matriculaProcessar.getMatriculaVO(), transferencia.getGradeMigrar(), false, false, transferencia.getConfiguracaoAcademicoCursoTransferencia(), usuario);
            matriculaProcessar.setMatriculaDestinoComHistoricoVO(matriculaDestinoComHistoricoAlunoVO);
            
            // Verificando se ainda existem disciplinas que estao sendo cursadas por correspondencia (em consequencia de outras trasnferencias, realizadas
            // anteriormente. Neste caso, antes que uma nova transferencia possa ser realizada é necessário remover todas estas cursando por correspodencia.
            if (matriculaProcessar.getNrDisciplinasCursandoPorCorrespondencia() > 0) {
                throw new Exception("Não é possível realizar esta transferência pois esta matrícula possui disciplinas sendo cursadas por correspondência. Estas disciplinas devem ser migradas para turmas da matriz atual do aluno, antes que uma nova transferência possa ser realizada.");
            }
            
            // Verificar se a ultimaMatriculaPeriodo ainda é a mesma vinculada a transferencia
            MatriculaPeriodoVO ultimoMatriculaPeriodoAtual = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(matriculaProcessar.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            if (!ultimoMatriculaPeriodoAtual.getCodigo().equals(matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO().getCodigo())) {
                throw new Exception("Esta matrícula já foi renovada depois do registro desta transferência. Logo esta transferência não pode ser realizada mais.");
            }
            
            // atualizando dados da matriculaPeriodo
            matriculaProcessar.setMatriculaPeriodoUltimoPeriodoVO(ultimoMatriculaPeriodoAtual);
            
            if (getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().executarBloqueioTransferenciaMatrizCurricularMatriculaValidandoGradeMigrarGradeDestino(matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO().getCodigo(), transferencia.getGradeMigrar().getCodigo(), transferencia.getGradeOrigem().getCodigo())) {
            	throw new Exception("Existe uma transferência em vigência para esta matrícula neste período, da grade:  " + transferencia.getGradeMigrar().getCodigo() + " para a grade: " + transferencia.getGradeOrigem().getCodigo() + ". Ela deve ser cancelada em vez de processar essa nova transferência.");
            }
            // Registrando o responsavel, data e inicio da atividade que está sendor realizada.
            matriculaProcessar.setResponsavel(usuario);
            matriculaProcessar.setDataProcessamento(new Date());
            matriculaProcessar.setNrAlertasCriticos(0);
            matriculaProcessar.adicionarHistoricoResultadoProcessamento(matriculaProcessar.getDataProcessamento(), usuario.getNome_Apresentar(), "Iniciando processo de transferência de matriz curricular para esta matrícula.");
            getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaProcessar, usuario);

            // Carregando todos os atuais históricos do aluno que iremos realizar a transferência de matriz curricular
            MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(matriculaProcessar.getMatriculaVO(), transferencia.getGradeOrigem(), true, false, transferencia.getConfiguracaoAcademicoCursoTransferencia(), usuario);
            matriculaProcessar.getMatriculaVO().setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
            
            // Atualizar periodoLetivo ultima matriculaPeriodo
			Integer periodoLetivo = matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO().getPeriodoLetivo().getPeriodoLetivo();
			PeriodoLetivoVO periodoLetivoCursar = new PeriodoLetivoVO();
			while (!Uteis.isAtributoPreenchido(periodoLetivoCursar) && periodoLetivo > 0) {
				periodoLetivoCursar = transferencia.getGradeMigrar().obterPeriodoLetivoPorPeriodoLetivo(periodoLetivo);
				if (!Uteis.isAtributoPreenchido(periodoLetivoCursar)) {
					periodoLetivo--;
				}
			}
            if ((periodoLetivoCursar == null) || (periodoLetivoCursar.getCodigo().equals(0))) {
                matriculaProcessar.adicionarHistoricoResultadoProcessamento(null, "", "ALERTA IMPORTANTE: Não foi possível realizar a migração dos históricos das disciplinas que estavam sendo cursadas, pois não foi possível determinar o período letivo correspondente na nova Matriz Curricular.");
                matriculaProcessar.incremetarNrAlertasCriticos();
                throw new Exception("Não foi possível realizar a migração dos históricos das disciplinas que estavam sendo cursadas, pois não foi possível determinar o período letivo correspondente na nova Matriz");
            }
            matriculaProcessar.setPeriodoLetivoUtilizarDisciplinasCursar(periodoLetivoCursar);
            //getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivo(periodoLetivoCursar.getCodigo(), matriculaProcessar.getMatriculaPeriodoUltimoPeriodoVO().getCodigo());

            // Realizando a migração da grade
            realizarMigracaoMatriculaGradeOrigemParaGradeDestino(transferencia, matriculaProcessar, usuario);

            //Alterar a gradeCurricularAtual do aluno, para a grade migrada. De forma que na próxima renovação
            //e na emissão de histórico, esta seja a grade do aluno.
            getFacadeFactory().getMatriculaFacade().alterarGradeCurricularAtual(matriculaProcessar.getMatriculaVO().getMatricula(), transferencia.getGradeMigrar().getCodigo());

            // Registrando situação final da transferencia da matriz curricular
            matriculaProcessar.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "Finalizada transferência de matriz curricular", true);
            matriculaProcessar.setSituacao("RE");
            getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaProcessar, usuario);
            realizarInclusaoDadosObservacaoHistoricoAluno(matriculaProcessar.getMatriculaVO(), transferencia.getGradeMigrar().getCodigo(), transferencia.getObservacaoHistorico(), usuario);
            inicializarDadosMatriculaProcessar(matriculaProcessar);
            return true;
        } catch (Exception e) {
            // Registrando erro da transferencia da matriz curricular
            matriculaProcessar.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "ERRO: " + e.getMessage());
            matriculaProcessar.setSituacao("ER");
//            getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaProcessar, usuario);
//            inicializarDadosMatriculaProcessar(matriculaProcessar);
//            return false;
            throw e;
        }
    }
    
    public void realizarInclusaoDadosObservacaoHistoricoAluno(MatriculaVO matriculaVO, Integer gradeCurricular, String observacaoHistorico, UsuarioVO usuario) throws Exception {
    	if (!observacaoHistorico.equals("")) {
    		ObservacaoComplementarHistoricoAlunoVO obj = new ObservacaoComplementarHistoricoAlunoVO();
    		obj.setMatricula(matriculaVO.getMatricula());
    		obj.setGradeCurricular(gradeCurricular);
    		obj.setObservacaoTransferenciaMatrizCurricular(observacaoHistorico);
    		getFacadeFactory().getMatriculaFacade().incluirObservacaoComplementarHistoricoAluno(obj, usuario);
    	}
    }
	
    
    /**
     * Método responsável por cancelar uma transferencia de matriz curricular
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean realizarCancelamentoMatrizCurricularMatricula(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaCancelar,
            UsuarioVO usuario) throws Exception {
        try {            
            // É necessário atualizar a situação da matriucla, pois um processamento anterior pode ter alterado a grade curricular
            // da mesma por exemplo.
            matriculaCancelar.getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
            getFacadeFactory().getMatriculaFacade().carregarDados(matriculaCancelar.getMatriculaVO(), NivelMontarDados.BASICO, usuario);
            matriculaCancelar.setDataProcessamento(new Date());
            matriculaCancelar.setResponsavel(usuario);
            matriculaCancelar.adicionarHistoricoResultadoProcessamento(matriculaCancelar.getDataProcessamento(), usuario.getNome_Apresentar(), "Transferência Cancelada pelo Usuário");

            // Verificar se a matricula ainda está vinculada a gradeDestino da transferencia
            if (!matriculaCancelar.getMatriculaVO().getGradeCurricularAtual().getCodigo().equals(transferencia.getGradeMigrar().getCodigo())) {
            	matriculaCancelar.incremetarNrAlertasCriticos();
                throw new Exception("Esta matrícula não está mais vinculada a Matriz Curricular Destino desta transferência. Logo esta transferência não pode ser cancelada.");
            }

            // Verificar se a ultimaMatriculaPeriodo ainda é a mesma vinculada a transferencia
            MatriculaPeriodoVO ultimoMatriculaPeriodoAtual = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(matriculaCancelar.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            if (!ultimoMatriculaPeriodoAtual.getCodigo().equals(matriculaCancelar.getMatriculaPeriodoUltimoPeriodoVO().getCodigo())) {
            	matriculaCancelar.incremetarNrAlertasCriticos();
                throw new Exception("Esta matrícula já foi renovada depois do processamento desta transferência. Logo esta transferência não pode ser cancelada.");
            }
            // atualizando dados ultimo matriculaPeriodo
            matriculaCancelar.setMatriculaPeriodoUltimoPeriodoVO(ultimoMatriculaPeriodoAtual);
                    
            // Verificando se não existem novos históricos adicionados, que não vieram da
            // transferência de matriz curricular. Caso exista, então o sistema não irá mais
            // permitir o cancelamento da transferencia, pois poderá impactar em perca de notas
            // do aluno.
			List<HistoricoVO> historicoCriadoAposTransferenciaMatrizCurricularVOs = getFacadeFactory().getHistoricoFacade().verificarExisteHistoricoMatrizNaoCriadoPorTrasnferenciaMatriz(matriculaCancelar.getMatriculaVO().getMatricula(), matriculaCancelar.getMatriculaVO().getGradeCurricularAtual().getCodigo(), matriculaCancelar.getCodigo());
			if (Uteis.isAtributoPreenchido(historicoCriadoAposTransferenciaMatrizCurricularVOs)) {
				StringBuilder descricaoDisciplinaCriadoAposTransferenciaMatrizCurricular = new StringBuilder();
				for (HistoricoVO obj : historicoCriadoAposTransferenciaMatrizCurricularVOs) {
					descricaoDisciplinaCriadoAposTransferenciaMatrizCurricular.append("Disciplina: ").append(obj.getDisciplina().getCodigo()).append(", Nome: ").append(obj.getDisciplina().getNome()).append("; ");
				}
				matriculaCancelar.incremetarNrAlertasCriticos();
				throw new Exception(UteisJSF.internacionalizar("msg_TransferenciaMatrizCurricular_historicosAdicionadoAposTransferencia").replace("{0}", descricaoDisciplinaCriadoAposTransferenciaMatrizCurricular));
			}
            
            // Atualizar periodoLetivo ultima matriculaPeriodo 
            PeriodoLetivoVO periodoLetivoVoltar = matriculaCancelar.getMatriculaPeriodoUltimoPeriodoVO().getPeriodoLetivo();
            periodoLetivoVoltar = transferencia.getGradeOrigem().obterPeriodoLetivoPorPeriodoLetivo(periodoLetivoVoltar.getPeriodoLetivo());
            if ((periodoLetivoVoltar == null) || (periodoLetivoVoltar.getCodigo().equals(0))) {
                matriculaCancelar.adicionarHistoricoResultadoProcessamento(null, "", "ALERTA IMPORTANTE: Não foi possível realizar a migração dos históricos das disciplinas que estavam sendo cursadas, pois não foi possível determinar o período letivo correspondente na nova Matriz Curricular.");
                matriculaCancelar.incremetarNrAlertasCriticos();
                throw new Exception("Não foi possível realizar o cancelamento da transferência, pois não foi possível determinar o período letivo correspondente na nova Matriz");
            }
            matriculaCancelar.setPeriodoLetivoUtilizarDisciplinasCursar(periodoLetivoVoltar);
            //getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivo(periodoLetivoVoltar.getCodigo(), matriculaCancelar.getMatriculaPeriodoUltimoPeriodoVO().getCodigo());

            //Removendo todos os históricos que foram criados pela transferencia
            getFacadeFactory().getHistoricoFacade().excluirPorTransferenciaMatrizCurricularMatricula(matriculaCancelar.getCodigo(), transferencia.getGradeMigrar().getCodigo(), usuario);

            //Redirecionando MatriculaPeriodoTurmaDisciplina para os históricos antigos, de forma que o aluno fique na mesma
            //situação - igual a antes da migração de matriz curricular
            cancelarVinculoMatriculaPeriodoTurmaDisciplinaParaMapaEquivalenciaTransferencia(transferencia, matriculaCancelar, usuario);
            
            getFacadeFactory().getHistoricoFacade().removerVinculoTransferenciaMatrizCurricularMatriculaHistorico(matriculaCancelar.getCodigo(), usuario);
            
            //Alterar a gradeCurricularAtual do aluno, para a grade migrada. De forma que na próxima renovação
            //e na emissão de histórico, esta seja a grade do aluno.
            getFacadeFactory().getMatriculaFacade().alterarGradeCurricularAtual(matriculaCancelar.getMatriculaVO().getMatricula(), transferencia.getGradeOrigem().getCodigo());

            matriculaCancelar.setSituacao("CA");
            getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaCancelar, usuario);
            return true;
        } catch (Exception e) {
            // Registrando erro da transferencia da matriz curricular
            matriculaCancelar.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "ERRO: " + e.getMessage());
            getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaCancelar, usuario);
            return false;
        }
    }
    
    /**
     * Método responsável por cancelar uma transferencia de matriz curricular
     */
    public void cancelarMigracaoMatrizCurricular(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaEspecificaProcessar,
            UsuarioVO usuario) throws Exception {
        try {
			if ((matriculaEspecificaProcessar != null) &&
			    (!matriculaEspecificaProcessar.getPodeSerCancelada())) {
			    throw new Exception("Esta transferência de matrícula não pode ser cancelada");
			}
			
			transferencia.setResponsavel(usuario);
			transferencia.setData(new Date());
			if (matriculaEspecificaProcessar != null) {
			    transferencia.adicionarHistoricoResultadoProcessamento(transferencia.getData(), usuario.getNome_Apresentar(), "Iniciou cancelamento da transferência de matriz curricular da matrícula: " + matriculaEspecificaProcessar.getMatriculaVO().getMatricula());
			} else {
			    transferencia.adicionarHistoricoResultadoProcessamento(transferencia.getData(), usuario.getNome_Apresentar(), "Iniciou cancelamento da transferência de matriz curricular");
			}
			gravar(transferencia, usuario);

			// carregando grade origem que será útil
			GradeCurricularVO gradeCurricularOrigem = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(transferencia.getGradeOrigem().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
			transferencia.setGradeOrigem(gradeCurricularOrigem);
			
			int contador = 0;
			boolean transferenciaRealizada = false;
			// Realizando as transferencias de matrizes curriculares
			if (matriculaEspecificaProcessar != null) {
			    // Se diferente de nulo é por que o usuário quer processar uma única Matricula
			    transferenciaRealizada = realizarCancelamentoMatrizCurricularMatricula(transferencia, matriculaEspecificaProcessar, usuario);
			    if (transferenciaRealizada) {
			        contador++;
			        getFacadeFactory().getMatriculaFacade().excluirObservacaoComplementarHistoricoAluno(matriculaEspecificaProcessar.getMatriculaVO().getMatricula(), transferencia.getGradeMigrar().getCodigo(), usuario);
			    }
			} else {
			    for (TransferenciaMatrizCurricularMatriculaVO matriculaProcessar : transferencia.getListaTransferenciaMatrizCurricularMatricula()) {
			        if (matriculaProcessar.getPodeSerCancelada()) {
			            transferenciaRealizada = realizarCancelamentoMatrizCurricularMatricula(transferencia, matriculaProcessar, usuario);
			            if (transferenciaRealizada) {
			                contador++;
			                getFacadeFactory().getMatriculaFacade().excluirObservacaoComplementarHistoricoAluno(matriculaEspecificaProcessar.getMatriculaVO().getMatricula(), transferencia.getGradeMigrar().getCodigo(), usuario);
			            }
			        }
			    }
			}

			// gravando o data/hora que foi finalizado o processamento da tranferencia e quantas matrículas foram processadas.
			transferencia.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "   -> Finalizado cancelamento de transferência de matriz curricular. " + contador + " matrícula(s) canceladas (s).");			
			gravar(transferencia, usuario);
			transferencia.atualizarEstatisticasTransferenciaMatrizCurricular();
		} catch (Exception e) {
			matriculaEspecificaProcessar.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "ERRO: " + e.getMessage());
			throw e;
		}
    }    

    /**
     * Método responsavel por realizar a transferencia de matriz curricular
     * de todas as matriculas adicionadas para o objeto TransferenciaMatrizCurricularVO
     * O mesmo carrega os dados grades curriculares Origem e Destino, de forma que
     * estes dados sejam carregados somente uma vez. Se for fornecido uma matrícula
     * específica em matriculaEspecificaProcessar, somente ela será processada. Caso
     * contrário todas as matrículas adicionadas para a trasnferencia serão processadas
     * versão 5.0
     * @param transferencia
     * @param gradeOrigem
     * @param gradeDestino
     * @param usuario
     * @throws Exception 
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarMigracaoMatrizCurricular(
            TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaEspecificaProcessar,
            UsuarioVO usuario) throws Exception {
        validarDados(transferencia);
        if (transferencia.getListaTransferenciaMatrizCurricularMatricula().size()<=0) {
            throw new Exception("Não existe nenhuma matrícula adicionada para transferência de matriz curricular.");
        }

        //Gravando os dados da transferencia, para que mesmo que o processamento seja interrompido
        //os dados das matriculas já processadas sejam persistidos e mantidos atualizados pelo 
        //sistema. Atualizando os dados do usuário e operação que está sendo realizada.
        transferencia.setResponsavel(usuario);
        transferencia.setData(new Date());
        transferencia.adicionarHistoricoResultadoProcessamento(transferencia.getData(), usuario.getNome_Apresentar(), "Iniciou processamento de transferência de matriz curricular");
        gravar(transferencia, usuario);

        // Carregandos as matrizes que serão utilizadas no processamento
        GradeCurricularVO gradeCurricularOrigem = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(transferencia.getGradeOrigem().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
        transferencia.setGradeOrigem(gradeCurricularOrigem);
        GradeCurricularVO gradeCurricularDestino = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(transferencia.getGradeMigrar().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
        transferencia.setGradeMigrar(gradeCurricularDestino);

        // Carregando o mapa de equivalencia que serão utilizados no processamento
        if (!transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo().equals(0)) {
            MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAtivaDestino = getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorChavePrimaria(transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo(), NivelMontarDados.TODOS, true, usuario);
            transferencia.setMapaEquivalenciaAtivaOrigem(mapaEquivalenciaAtivaDestino);
        }

        // Carregando configuracao academica padrao para o curso que está sendo realizada a transferencia
        transferencia.getUnidadeEnsinoCurso().getCurso().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
        getFacadeFactory().getCursoFacade().carregarDados(transferencia.getUnidadeEnsinoCurso().getCurso(), NivelMontarDados.BASICO, usuario);
        ConfiguracaoAcademicoVO cfg = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(transferencia.getUnidadeEnsinoCurso().getCurso().getConfiguracaoAcademico().getCodigo(), usuario);
        transferencia.setConfiguracaoAcademicoCursoTransferencia(cfg);

        int contador = 0;
        boolean transferenciaRealizada = false;
        // Realizando as transferencias de matrizes curriculares
        if (matriculaEspecificaProcessar != null) {
            // Se diferente de nulo é por que o usuário quer processar uma única Matricula
            transferenciaRealizada = realizarMigracaoMatrizCurricularMatricula(transferencia, matriculaEspecificaProcessar, usuario);
            if (transferenciaRealizada) {
                contador++;
            } else {
            	throw new Exception("Erro no Processamento");
            }
        } else {
            for (TransferenciaMatrizCurricularMatriculaVO matriculaProcessar : transferencia.getListaTransferenciaMatrizCurricularMatricula()) {
                if (matriculaProcessar.getPodeSerProcessada()) {
                    transferenciaRealizada = realizarMigracaoMatrizCurricularMatricula(transferencia, matriculaProcessar, usuario);
                    if (transferenciaRealizada) {
                        contador++;
                    } else {
                    	throw new Exception("Erro no Processamento");
                    }
                }
            }
        }

        // gravando o data/hora que foi finalizado o processamento da tranferencia e quantas matrículas foram processadas.
        transferencia.adicionarHistoricoResultadoProcessamento(new Date(), usuario.getNome_Apresentar(), "   -> Finalizado processamento de transferência de matriz curricular. " + contador + " matrícula(s) transferida(s).");
        gravar(transferencia, usuario);

        transferencia.atualizarEstatisticasTransferenciaMatrizCurricular();
    }
    
    /**
     * Método responsavel por realizar a transferencia de matriz curricular
     * de todas as matriculas adicionadas para o objeto TransferenciaMatrizCurricularVO
     * O mesmo carrega os dados grades curriculares Origem e Destino, de forma que
     * estes dados sejam carregados somente uma vez. Se for fornecido uma matrícula
     * específica em matriculaEspecificaProcessar, somente ela será processada. Caso
     * contrário todas as matrículas adicionadas para a trasnferencia serão processadas
     * versão 5.0
     * @param transferencia
     * @param gradeOrigem
     * @param gradeDestino
     * @param usuario
     * @throws Exception 
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarMigracaoMatrizCurricular(
    		TransferenciaMatrizCurricularVO transferencia,    		
    		UsuarioVO usuario) throws Exception {
    	validarDados(transferencia);
    	if (transferencia.getListaTransferenciaMatrizCurricularMatricula().size()<=0) {
    		throw new Exception("Não existe nenhuma matrícula adicionada para transferência de matriz curricular.");
    	}
    	
    	//Gravando os dados da transferencia, para que mesmo que o processamento seja interrompido
    	//os dados das matriculas já processadas sejam persistidos e mantidos atualizados pelo 
    	//sistema. Atualizando os dados do usuário e operação que está sendo realizada.
    	transferencia.setResponsavel(usuario);
    	transferencia.setData(new Date());
    	transferencia.adicionarHistoricoResultadoProcessamento(transferencia.getData(), usuario.getNome_Apresentar(), "Iniciou processamento de transferência de matriz curricular");
    	gravar(transferencia, usuario);
    	
    	// Carregandos as matrizes que serão utilizadas no processamento
    	GradeCurricularVO gradeCurricularOrigem = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(transferencia.getGradeOrigem().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
    	transferencia.setGradeOrigem(gradeCurricularOrigem);
    	GradeCurricularVO gradeCurricularDestino = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(transferencia.getGradeMigrar().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
    	transferencia.setGradeMigrar(gradeCurricularDestino);
    	
    	// Carregando o mapa de equivalencia que serão utilizados no processamento
    	if (!transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo().equals(0)) {
    		MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAtivaDestino = getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorChavePrimaria(transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo(), NivelMontarDados.TODOS, true, usuario);
    		transferencia.setMapaEquivalenciaAtivaOrigem(mapaEquivalenciaAtivaDestino);
    	}
    	
    	// Carregando configuracao academica padrao para o curso que está sendo realizada a transferencia
    	transferencia.getUnidadeEnsinoCurso().getCurso().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
    	getFacadeFactory().getCursoFacade().carregarDados(transferencia.getUnidadeEnsinoCurso().getCurso(), NivelMontarDados.BASICO, usuario);
    	ConfiguracaoAcademicoVO cfg = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(transferencia.getUnidadeEnsinoCurso().getCurso().getConfiguracaoAcademico().getCodigo(), usuario);
    	transferencia.setConfiguracaoAcademicoCursoTransferencia(cfg);
    	
    	
    }

    @Override
    public void inicializarDadosMatriculaProcessar(TransferenciaMatrizCurricularMatriculaVO matriculaProcessar) {
        matriculaProcessar.getMatriculaVO().setMatriculaComHistoricoAlunoVO(null);
        matriculaProcessar.setHistoricoAproveitadosTransferencia(null);
        matriculaProcessar.setHistoricoNaoAproveitadosTransferencia(null);
        //matriculaProcessar.setNrAlertasCriticos(0);
        matriculaProcessar.setMapasEquivalenciaAproveitadosTransferenciaComHistoricos(null);
    }
    
	@Override
	public boolean executarVerificarMatriculaPeriodoPossuiTransferenciaMatrizCurricularExclusaoPreMatricula(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		TransferenciaMatrizCurricular.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT tmca.codigo ");
		sqlStr.append("FROM TransferenciaMatrizCurricularAtual tmca ");
		sqlStr.append("INNER JOIN TransferenciaMatrizCurricularMatricula tmcm on tmcm.TransferenciaMatrizCurricularAtual = tmca.codigo ");
		sqlStr.append("WHERE tmcm.matriculaPeriodo = ").append(matriculaPeriodo);
		sqlStr.append(" and tmcm.situacao = 'RE' ");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}
	
	public Integer processarTransferenciaTodasMatriculasParalelismo(final TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO, final List<TransferenciaMatrizCurricularMatriculaVO> listaTransferenciaMatrizCurricularMatriculaVOs, final UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getTransferenciaMatrizCurricularFacade().realizarMigracaoMatrizCurricular(transferenciaMatrizCurricularVO, usuarioVO);
		int contador = 0;
		final List<TransferenciaMatrizCurricularMatriculaVO> processadas = new ArrayList<TransferenciaMatrizCurricularMatriculaVO>(0);
		final ConsistirException consistirException = new ConsistirException();
		ProcessarParalelismo.executar(0, listaTransferenciaMatrizCurricularMatriculaVOs.size(), consistirException, new ProcessarParalelismo.Processo() {
			@Override
			public void run(int i) {
				TransferenciaMatrizCurricularMatriculaVO matriculaProcessar = listaTransferenciaMatrizCurricularMatriculaVOs.get(i);
				if (matriculaProcessar.getPodeSerProcessada()) {
					try {
						getFacadeFactory().getTransferenciaMatrizCurricularFacade().realizarMigracaoMatrizCurricularMatricula(transferenciaMatrizCurricularVO, matriculaProcessar, usuarioVO);
						processadas.add(matriculaProcessar);
					} catch (Exception e) {
						try {
							getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaProcessar, usuarioVO);
							getFacadeFactory().getTransferenciaMatrizCurricularFacade().inicializarDadosMatriculaProcessar(matriculaProcessar);
						} catch (Exception e1) {
							consistirException.adicionarListaMensagemErro(e1.getMessage());
						}
					}
				}

			}
		});
		contador = processadas.size();
		Uteis.liberarListaMemoria(processadas);
		return contador;
	}

    private HistoricoVO verificarObterHistoricoCompativelSendoCursadoPeloAlunoParaDisciplina(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, 
    		Integer disciplina, Integer cargaHoraria) {
        int i = matriculaProcessar.getDisciplinasAlunoCursando().size() - 1;
        while (i >= 0) {
        	HistoricoVO historicoSendoCursado = (HistoricoVO)matriculaProcessar.getDisciplinasAlunoCursando().get(i);
        	if ((historicoSendoCursado.getDisciplina().getCodigo().equals(disciplina)) &&
        	    (historicoSendoCursado.getCargaHorariaDisciplina().equals(cargaHoraria))) {
        		return historicoSendoCursado;
        	}
        	i = i - 1;
        }
        return null;
    } 

    /**
     * Este método é resposável por varrer históricos de disciplinas filhas da composicao, na matriz origem, que não
     * foram até este momento aproveitadas pela rotina de transferencia. Isto é importante, pois a rotina anterior
     * trabalha com foco nas composicao na matriz destino. Porém, podem ocorrer de existir disciplinas compostas na 
     * matriz origem, cusa a diciplina mae nao foi aproveitada (pois nao há correspodencia com ninguem). Contudo, a
     * disciplina filha pode ser correspendente à alguma disciplina da matriz destino. Este método visa identificar
     * esta situação e dar o devido tratamento à ele. Sempre que a disciplina filha for aproveitada, nao poderemos
     * mais aproveitar a disciplina mae (nem mesmo fora da grade), pois se isto ocorre-se teríamos uma mesma carga horária
     * sendo aproveita mais de uma vez.
     * @author Otimize - 13 de out de 2016 
     * @param transferencia
     * @param matriculaProcessar
     * @param gradeDestino
     * @param usuario
     * @throws Exception
     */
    private void verificandoEGerandoHistoricosDisciplinaFilhasComposicaoAindaNaoAproveitados(
    		TransferenciaMatrizCurricularVO transferencia,
            TransferenciaMatrizCurricularMatriculaVO matriculaProcessar,
            GradeCurricularVO gradeDestino,
            UsuarioVO usuario) throws Exception {    	
    	List<GradeDisciplinaVO> listaGradesDisciplinasCompostasGrade = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeDisciplinaCompostaPorGrade(transferencia.getGradeOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    	for (GradeDisciplinaVO gradeDisciplinaComComposicaoVO : listaGradesDisciplinasCompostasGrade) {
    		
    		// Primeiramente temos que verificar se a disciplina mae já não foi aproveitada. O que pode ter ocorrido pelo caso a disciplina na 
    		// matriz destino (com mesmo codigo e carga horaria) nao seja composta
    		if (!verificarHistoricoParaDisciplinaJaFoiAproveitadoOutraInteracao(matriculaProcessar, gradeDisciplinaComComposicaoVO.getDisciplina().getCodigo(), gradeDisciplinaComComposicaoVO.getCodigo())) {
    			
    			// Se entrarmos aqui é por que a mãe nao foi aproveitada, logo, podemos processar as filhas para verificar se alguma delas pode
    			// ser transferida, matando alguma disciplina na grade destino.
    			
    			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaComComposicaoVO.getGradeDisciplinaCompostaVOs()) {
    				// Percorrendoa s filhas da composicao, para verificarmos se há histórico de aprovacao para esta filha e se o mesmo
    				// pode ser aproveitado por correspodencia na matriz destino.
    				
    	        	HistoricoVO historicoFilhaCompostaASerAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
    	        	if (historicoFilhaCompostaASerAproveitado != null) {
    	        		// Se entramos aqui é por que existe um histórico aprovado, que ainda nao foi transferido.
    	        		
    	                GradeDisciplinaVO gradeDisciplinaCorrespondente = gradeDestino.obterGradeDisciplinaCorrespondente(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria());
    	                
    	                if (gradeDisciplinaCorrespondente != null) {
    	                	if (!gradeDisciplinaCorrespondente.getDisciplinaComposta()) {
    	                		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, usuario)) {
    	                			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
    	                			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
    	                			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
  	                				gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, historicoFilhaCompostaASerAproveitado, false, usuario);
  	                				
  	                				// Como aproveitamos a filha, entao temos que remover a mae da lista de disciplinas pendentes, caso contrario, poderíamos correr
  	                				// o risco de aproveitar a mae e filha ao mesmo tempo. O que implicaria em aproveitar duas vezes a mesma carga horaria.
  	                			    excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, gradeDisciplinaComComposicaoVO.getDisciplina().getCodigo(), gradeDisciplinaComComposicaoVO.getCargaHoraria());
    	                		}
    	                	} 
    	                } else {
    	                    // Se nao entramos a gradeDisciplina correspondente, iremos buscar por uma disciplina
    	                    // de um grupoOptativa que possa ser correspondente ao mesmo
    	                    GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = gradeDestino.obterGradeCurricularGrupoOptativaCorrespondente(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria());
    	                    if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
    	                    	if (!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinaComposta()) {
    	                    		
    	                    		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
    	                    			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
    	                    			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
    	                    			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
    	                    		
    	                    			gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), historicoFilhaCompostaASerAproveitado, false, usuario);

      	                				// Como aproveitamos a filha, entao temos que remover a mae da lista de disciplinas pendentes, caso contrario, poderíamos correr
      	                				// o risco de aproveitar a mae e filha ao mesmo tempo. O que implicaria em aproveitar duas vezes a mesma carga horaria.
      	                			    excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, gradeDisciplinaComComposicaoVO.getDisciplina().getCodigo(), gradeDisciplinaComComposicaoVO.getCargaHoraria());
    	                    		}
    	                    	}
    	                    }
    	                }
    	        	}
    			}
    		}
    	}    		
    	
    	List<GradeCurricularGrupoOptativaDisciplinaVO> listaGradesCurricularesGrupoOptativaComposta = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorMatrizCurricularGrupoOptativaComposta(transferencia.getGradeOrigem().getCodigo(), usuario);
    	for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina : listaGradesCurricularesGrupoOptativaComposta) {

    		// Primeiramente temos que verificar se a disciplina mae já não foi aproveitada. O que pode ter ocorrido pelo caso a disciplina na 
    		// matriz destino (com mesmo codigo e carga horaria) nao seja composta
    		if (!verificarHistoricoParaDisciplinaJaFoiAproveitadoOutraInteracao(matriculaProcessar, gradeCurricularGrupoOptativaDisciplina.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplina.getCodigo())) {
    			
    			// Se entrarmos aqui é por que a mãe nao foi aproveitada, logo, podemos processar as filhas para verificar se alguma delas pode
    			// ser transferida, matando alguma disciplina na grade destino.
    			
    			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplina.getGradeDisciplinaCompostaVOs()) {
    				// Percorrendoa s filhas da composicao, para verificarmos se há histórico de aprovacao para esta filha e se o mesmo
    				// pode ser aproveitado por correspodencia na matriz destino.
    				
    	        	HistoricoVO historicoFilhaCompostaASerAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplinaFilhaComposicao(transferencia, matriculaProcessar, gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria() );
    	        	if (historicoFilhaCompostaASerAproveitado != null) {
    	        		// Se entramos aqui é por que existe um histórico aprovado, que ainda nao foi transferido.
    	        		
    	                GradeDisciplinaVO gradeDisciplinaCorrespondente = gradeDestino.obterGradeDisciplinaCorrespondente(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria());
    	                
    	                if (gradeDisciplinaCorrespondente != null) {
    	                	if (!gradeDisciplinaCorrespondente.getDisciplinaComposta()) {
    	                		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, null, usuario)) {
    	                			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
    	                			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
    	                			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
  	                				gerarHistoricoGradeDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeDisciplinaCorrespondente, historicoFilhaCompostaASerAproveitado, false, usuario);
  	                				
  	                				// Como aproveitamos a filha, entao temos que remover a mae da lista de disciplinas pendentes, caso contrario, poderíamos correr
  	                				// o risco de aproveitar a mae e filha ao mesmo tempo. O que implicaria em aproveitar duas vezes a mesma carga horaria.
  	                			    excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplina.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplina.getCargaHoraria());
    	                		}
    	                	} 
    	                } else {
    	                    // Se nao entramos a gradeDisciplina correspondente, iremos buscar por uma disciplina
    	                    // de um grupoOptativa que possa ser correspondente ao mesmo
    	                    GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = gradeDestino.obterGradeCurricularGrupoOptativaCorrespondente(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getCargaHoraria());
    	                    if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
    	                    	if (!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinaComposta()) {
    	                    		
    	                    		if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(transferencia, matriculaProcessar, null, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
    	                    			// se entrarmos aqui é por que na matriz destino a disciplina correspodente não está aprovada. Entao podemos gerar a transerencia
    	                    			// normalemnte. Caso a mesma já esteja aprovada, o historico da matriz destino deve ser mantido, para nao corrermos o risco de gerar
    	                    			// um histórico que sobreponha uma informaçao de aprovação já existente do aluno.
    	                    		
    	                    			gerarHistoricoGradeCurricularGrupoOptativaDisciplinaCorrespodente(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), historicoFilhaCompostaASerAproveitado, false, usuario);

      	                				// Como aproveitamos a filha, entao temos que remover a mae da lista de disciplinas pendentes, caso contrario, poderíamos correr
      	                				// o risco de aproveitar a mae e filha ao mesmo tempo. O que implicaria em aproveitar duas vezes a mesma carga horaria.
      	                			    excluirHistoricoDisciplinaListaPendenciaParaTransferencia(transferencia, matriculaProcessar, gradeCurricularGrupoOptativaDisciplina.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplina.getCargaHoraria());
    	                    		}
    	                    	}
    	                    }
    	                }
    	        	}
    			}
    		}
    	}
    }	            
    
	private boolean existeMapaEquivalenciaParaDisciplinaAlunoCursando(TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, HistoricoVO historicoVO) throws Exception {
		Integer codigoMapaEquivalenciaUtilizar = transferencia.getMapaEquivalenciaUtilizadoGradeMigrar().getCodigo();
		MapaEquivalenciaDisciplinaCursadaVO mapaAdotar = null;
		if (historicoVO.getCursando()) {
			mapaAdotar = obterMapaEquivalenciaCorrenpodenteHistoricoListaMapaEquivalenciaUtilizadoTransferencia(matriculaProcessar, historicoVO);
			if (mapaAdotar == null && !codigoMapaEquivalenciaUtilizar.equals(0)) {
				mapaAdotar = verificarEObterMelhorMapaEquivalenciaParaDisciplinaAlunoCursando(transferencia, historicoVO);
			}
		}
		return Uteis.isAtributoPreenchido(mapaAdotar);
    }
}
