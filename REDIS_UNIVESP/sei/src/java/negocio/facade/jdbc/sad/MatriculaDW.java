package negocio.facade.jdbc.sad;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.sad.MatriculaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.sad.MatriculaDWInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaDWVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MatriculaDWVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MatriculaDWVO
 * @see ControleAcesso
 */
@Repository @Scope("singleton") @Lazy 
public class MatriculaDW extends ControleAcesso implements MatriculaDWInterfaceFacade {

    protected static String idEntidade;

    public MatriculaDW() throws Exception {
        super();
        setIdEntidade("Matricula");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MatriculaDWVO</code>.
     */
    public MatriculaDWVO novo(UsuarioVO usuario) throws Exception {
        MatriculaDW.incluir(getIdEntidade());
        MatriculaDWVO obj = new MatriculaDWVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MatriculaDWVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MatriculaDWVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MatriculaDWVO obj, UsuarioVO usuario) throws Exception {
        try {
            MatriculaDWVO.validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MatriculaDW.incluir(getIdEntidade());
            final String sql = "INSERT INTO MatriculaDW( data, mes, peso, ano, unidadeEnsino, processoMatricula, curso, turno, areaConhecimento, nivelEducacional, situacao ) VALUES " +
            		"( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
            sqlInserir.setInt(2, obj.getMes().intValue());
            sqlInserir.setInt(3, obj.getPeso().intValue());
            sqlInserir.setInt(4, obj.getAno().intValue());
            if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                sqlInserir.setInt(5, obj.getUnidadeEnsino().getCodigo().intValue());
            } else {
                sqlInserir.setNull(5, 0);
            }
            if (obj.getProcessoMatricula().getCodigo().intValue() != 0) {
                sqlInserir.setInt(6, obj.getProcessoMatricula().getCodigo().intValue());
            } else {
                sqlInserir.setNull(6, 0);
            }
            if (obj.getCurso().getCodigo().intValue() != 0) {
                sqlInserir.setInt(7, obj.getCurso().getCodigo().intValue());
            } else {
                sqlInserir.setNull(7, 0);
            }
            if (obj.getTurno().getCodigo().intValue() != 0) {
                sqlInserir.setInt(8, obj.getTurno().getCodigo().intValue());
            } else {
                sqlInserir.setNull(8, 0);
            }
            if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
                sqlInserir.setInt(9, obj.getAreaConhecimento().getCodigo().intValue());
            } else {
                sqlInserir.setNull(9, 0);
            }
            sqlInserir.setString(10, obj.getNivelEducacional());
            sqlInserir.setString(11, obj.getSituacao());
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
        } catch (Exception e) {
        	obj.setNovoObj(true);
        	throw e;
        }
    }
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaDWVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MatriculaDWVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MatriculaDWVO obj, UsuarioVO usuario) throws Exception {
        try {
            
            MatriculaDWVO.validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
           // MatriculaDW.alterar(getIdEntidade());
           final  String sql = "UPDATE MatriculaDW set data=?, mes=?, peso=?, ano=?, unidadeEnsino=?, processoMatricula=?, curso=?, turno=?, areaConhecimento=?, nivelEducacional=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
            sqlAlterar.setInt(2, obj.getMes().intValue());
            sqlAlterar.setInt(3, obj.getPeso().intValue());
            sqlAlterar.setInt(4, obj.getAno().intValue());
            if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(5, obj.getUnidadeEnsino().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(5, 0);
            }
            if (obj.getProcessoMatricula().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(6, obj.getProcessoMatricula().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(6, 0);
            }
            if (obj.getCurso().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(7, obj.getCurso().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(7, 0);
            }
            if (obj.getTurno().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(8, obj.getTurno().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(8, 0);
            }
            if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(9, obj.getAreaConhecimento().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(9, 0);
            }
            sqlAlterar.setString(10, obj.getNivelEducacional());
            sqlAlterar.setString(11, obj.getSituacao());
            sqlAlterar.setInt(12, obj.getCodigo().intValue());
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MatriculaDWVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MatriculaDWVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MatriculaDWVO obj, UsuarioVO usuario) throws Exception {
        try {
        	  /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MatriculaDW.excluir(getIdEntidade());
            String sql = "DELETE FROM MatriculaDW WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public SqlRowSet consultaGeracaoRelatorioPizzaBarra(MatriculaDWVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "Select sum(peso) as peso, situacao as situacao from MatriculaDW  ";
        String and = "";
        String where = " where ";
        if (obj.getData() != null) {
            if (obj.getDataFim() != null) {
                sql += where + and + "(( data >= '" + Uteis.getDataJDBC(obj.getData()) + "') and (data <= '" + Uteis.getDataJDBC(obj.getDataFim()) + "'))";
            } else {
                sql += where + "( data >= " + Uteis.getDataJDBC(obj.getData()) + ") ";
            }
            and = " and ";
            where = "";
        } else {
            if (obj.getAno().intValue() != 0) {
                sql += where + and + " ano = " + obj.getAno().intValue();
                and = " and ";
                where = "";
            }
            if (obj.getMes().intValue() != 0) {
                sql += where + and + " mes = " + obj.getMes().intValue();
                and = " and ";
                where = "";
            }

        }
        if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
            sql += where + and + " areaConhecimento = " + obj.getAreaConhecimento().getCodigo().intValue();
            and = " and ";
            where = "";
        }
        if (obj.getCurso().getCodigo().intValue() != 0) {
            sql += where + and + " curso = " + obj.getCurso().getCodigo().intValue();
            and = " and ";
            where = "";
        }
        if (!obj.getNivelEducacional().equals("")) {
            sql += where + and + " upper (nivelEducacional) = '" + obj.getNivelEducacional().toUpperCase() + "'";
            and = " and ";
            where = "";
        }
        if (obj.getProcessoMatricula().getCodigo().intValue() != 0) {
            sql += where + and + " processoMatricula = " + obj.getProcessoMatricula().getCodigo().intValue();
            and = " and ";
            where = "";
        }
        if (obj.getTurno().getCodigo().intValue() != 0) {
            sql += where + and + " turno = " + obj.getTurno().getCodigo().intValue();
            and = " and ";
            where = "";
        }
        if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
            sql += where + and + " unidadeEnsino = " + obj.getUnidadeEnsino().getCodigo().intValue();
            and = " and ";
            where = "";
        }
        sql += " group by  situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return (SqlRowSet) (montarDadosConsulta(tabelaResultado, usuario));
    }

    public SqlRowSet consultaGeracaoRelatorioLinhaTempo(MatriculaDWVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "Select sum(peso) as peso, situacao as situacao, data as data from MatriculaDW ";
        String and = "";
        String where = " where ";
        if (obj.getData() != null) {
            if (obj.getDataFim() != null) {
                sql += where + and + "(( data >= '" + Uteis.getDataJDBC(obj.getData()) + "') and (data <= '" + Uteis.getDataJDBC(obj.getDataFim()) + "'))";

            } else {
                sql += where + and + "( data >= " + Uteis.getDataJDBC(obj.getData()) + ") ";

            }
            and = " and ";
            where = " ";
        } else {
            if (obj.getAno().intValue() != 0) {
                sql += where + and + " ano = " + obj.getAno().intValue();
                and = " and ";
                where = " ";
            }
            if (obj.getMes().intValue() != 0) {
                sql += where + and + " mes = " + obj.getMes().intValue();
                and = " and ";
                where = " ";
            }
        }
        if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
            sql += where + and + " areaConhecimento = " + obj.getAreaConhecimento().getCodigo().intValue();
            and = " and ";
            where = " ";
        }
        if (obj.getCurso().getCodigo().intValue() != 0) {
            sql += where + and + " curso = " + obj.getCurso().getCodigo().intValue();
            and = " and ";
            where = " ";
        }
        if (!obj.getNivelEducacional().equals("")) {
            sql += where + and + " upper (nivelEducacional) = '" + obj.getNivelEducacional().toUpperCase() + "'";
            and = " and ";
            where = " ";
        }
        if (obj.getProcessoMatricula().getCodigo().intValue() != 0) {
            sql += where + and + " processoMatricula = " + obj.getProcessoMatricula().getCodigo().intValue();
            and = " and ";
            where = " ";
        }
        if (obj.getTurno().getCodigo().intValue() != 0) {
            sql += where + and + " turno = " + obj.getTurno().getCodigo().intValue();
            and = " and ";
            where = " ";
        }
        if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
            sql += where + and + " unidadeEnsino = " + obj.getUnidadeEnsino().getCodigo().intValue();
            and = " and ";
            where = " ";
        }
        sql += " group by  situacao, data order by data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return (SqlRowSet) (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>AreaConhecimento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaDW.* FROM MatriculaDW, AreaConhecimento WHERE MatriculaDW.areaConhecimento = AreaConhecimento.codigo and upper( AreaConhecimento.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY AreaConhecimento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Turno</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeTurno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaDW.* FROM MatriculaDW, Turno WHERE MatriculaDW.turno = Turno.codigo and upper( Turno.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Turno.nome";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaDW.* FROM MatriculaDW, Curso WHERE MatriculaDW.curso = Curso.codigo and upper( Curso.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Curso.nome";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>PeriodoLetivo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoPeriodoLetivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaDW.* FROM MatriculaDW, PeriodoLetivo WHERE MatriculaDW.periodoLetivo = PeriodoLetivo.codigo and upper( PeriodoLetivo.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PeriodoLetivo.descricao";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaDW.* FROM MatriculaDW, UnidadeEnsino WHERE MatriculaDW.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>Integer ano</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaDW WHERE ano >= " + valorConsulta.intValue() + " ORDER BY ano";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>Integer semestre</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSemestre(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaDW WHERE semestre >= " + valorConsulta.intValue() + " ORDER BY semestre";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>Integer mes</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaDW WHERE mes >= " + valorConsulta.intValue() + " ORDER BY mes";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaDW WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaDW</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaDW WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MatriculaDWVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado,Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MatriculaDWVO</code>.
     * @return  O objeto da classe <code>MatriculaDWVO</code> com os dados devidamente montados.
     */
    public static MatriculaDWVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        MatriculaDWVO obj = new MatriculaDWVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setMes(new Integer(dadosSQL.getInt("mes")));
        obj.setPeso(new Integer(dadosSQL.getInt("peso")));
        obj.setAno(new Integer(dadosSQL.getInt("ano")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getProcessoMatricula().setCodigo(new Integer(dadosSQL.getInt("processoMatricula")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
        obj.getAreaConhecimento().setCodigo(new Integer(dadosSQL.getInt("areaConhecimento")));
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosUnidadeEnsino(obj, nivelMontarDados,usuario);
        montarDadosProcessoMatricula(obj, nivelMontarDados,usuario);
        montarDadosCurso(obj, nivelMontarDados,usuario);
        montarDadosTurno(obj, nivelMontarDados,usuario);
        montarDadosAreaConhecimento(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>MatriculaDWVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosAreaConhecimento(MatriculaDWVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto <code>MatriculaDWVO</code>.
     * Faz uso da chave primária da classe <code>TurnoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurno(MatriculaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getTurno().getCodigo().intValue() == 0) {
            obj.setTurno(new TurnoVO());
            return;
        }
        obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>MatriculaDWVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(MatriculaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PeriodoLetivoVO</code> relacionado ao objeto <code>MatriculaDWVO</code>.
     * Faz uso da chave primária da classe <code>PeriodoLetivoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosProcessoMatricula(MatriculaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getProcessoMatricula().getCodigo().intValue() == 0) {
            obj.setProcessoMatricula(new ProcessoMatriculaVO());
            return;
        }
        obj.setProcessoMatricula(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(obj.getProcessoMatricula().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>MatriculaDWVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(MatriculaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MatriculaDWVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MatriculaDWVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM MatriculaDW WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MatriculaDW ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por obter o último valor gerado para uma chave primária.
     * É utilizada para obter o valor gerado pela SGBD para uma chave primária, 
     * a apresentação do mesmo e a implementação de possíveis relacionamentos. 
     */
    public static Integer obterValorChavePrimariaCodigo() throws Exception {
        String sqlStr = "SELECT MAX(codigo) FROM MatriculaDW";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        return (new Integer(tabelaResultado.getInt(1)));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MatriculaDW.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        MatriculaDW.idEntidade = idEntidade;
    }
}
