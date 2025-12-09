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
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.sad.ReceitaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.sad.ReceitaDWInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ReceitaDWVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ReceitaDWVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ReceitaDWVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class ReceitaDW extends ControleAcesso implements ReceitaDWInterfaceFacade {
	
    protected static String idEntidade;
	
    public ReceitaDW() throws Exception {
        super();
        setIdEntidade("Receita");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>ReceitaDWVO</code>.
    */
    public ReceitaDWVO novo() throws Exception {
        //ReceitaDW.incluir(getIdEntidade());
        ReceitaDWVO obj = new ReceitaDWVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ReceitaDWVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ReceitaDWVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ReceitaDWVO obj) throws Exception {
        try{

            ReceitaDWVO.validarDados(obj);
            //ReceitaDW.incluir(getIdEntidade());
            final String sql = "INSERT INTO ReceitaDW( data, mes, procSeletivo, ano, valor, centroReceita, unidadeEnsino, curso, turno, processoMatricula, areaConhecimento, nivelEducacional ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
           obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setDate( 1, Uteis.getDataJDBC(obj.getData() ));
            sqlInserir.setInt( 2, obj.getMes().intValue() );
            if (obj.getProcSeletivo().getCodigo().intValue() != 0) {
                sqlInserir.setInt(3, obj.getProcSeletivo().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(3, 0);
            }
            sqlInserir.setInt( 4, obj.getAno().intValue() );
            sqlInserir.setDouble( 5, obj.getValor().doubleValue() );
            if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                sqlInserir.setInt(6, obj.getCentroReceita().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(6, 0);
            }
            if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                sqlInserir.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(7, 0);
            }
            if (obj.getCurso().getCodigo().intValue() != 0) {
                sqlInserir.setInt(8, obj.getCurso().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(8, 0);
            }
            if (obj.getTurno().getCodigo().intValue() != 0) {
                sqlInserir.setInt(9, obj.getTurno().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(9, 0);
            }
            if (obj.getProcessoMatricula().getCodigo().intValue() != 0) {
                sqlInserir.setInt(10, obj.getProcessoMatricula().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(10, 0);
            }
            if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
                sqlInserir.setInt(11, obj.getAreaConhecimento().getCodigo().intValue() );
            } else {
                sqlInserir.setNull(11, 0);
            }
            sqlInserir.setString( 12, obj.getNivelEducacional() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ReceitaDWVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ReceitaDWVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ReceitaDWVO obj) throws Exception {
        try{
            //ReceitaDW.alterar(getIdEntidade());
            final String sql = "UPDATE ReceitaDW set data=?, mes=?, procSeletivo=?, ano=?, valor=?, centroReceita=?, unidadeEnsino=?, curso=?, turno=?, processoMatricula=?, areaConhecimento=?, nivelEducacional=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setDate( 1, Uteis.getDataJDBC(obj.getData() ));
            sqlAlterar.setInt( 2, obj.getMes().intValue() );
            if (obj.getProcSeletivo().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(3, obj.getProcSeletivo().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(3, 0);
            }
            sqlAlterar.setInt( 4, obj.getAno().intValue() );
            sqlAlterar.setDouble( 5, obj.getValor().doubleValue() );
            if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(6, obj.getCentroReceita().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(6, 0);
            }
            if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(7, 0);
            }
            if (obj.getCurso().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(8, obj.getCurso().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(8, 0);
            }
            if (obj.getTurno().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(9, obj.getTurno().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(9, 0);
            }
            if (obj.getProcessoMatricula().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(10, obj.getProcessoMatricula().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(10, 0);
            }
            if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(11, obj.getAreaConhecimento().getCodigo().intValue() );
            } else {
                sqlAlterar.setNull(11, 0);
            }
            sqlAlterar.setString( 12, obj.getNivelEducacional() );
            sqlAlterar.setInt( 13, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ReceitaDWVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ReceitaDWVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ReceitaDWVO obj) throws Exception {
        try {
            String sql = "DELETE FROM ReceitaDW WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    public SqlRowSet consultaGeracaoRelatorioPizzaBarra(ReceitaDWVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "Select sum(ReceitaDW.valor) as valor, UnidadeEnsino.nome as unidadeEnsino from ReceitaDW left join UnidadeEnsino  on ReceitaDW.unidadeEnsino = UnidadeEnsino.codigo ";
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
        if(obj.getAreaConhecimento().getCodigo().intValue() !=0){
            sql += where + and + " areaConhecimento = " + obj.getAreaConhecimento().getCodigo().intValue();            
            and = " and ";
            where = "";
        }
        if(obj.getCurso().getCodigo().intValue() !=0){
            sql += where + and + " curso = " + obj.getCurso().getCodigo().intValue();            
            and = " and ";
            where = "";
        }
        if(!obj.getNivelEducacional().equals("")){
            sql += where + and + " upper (nivelEducacional) = '" + obj.getNivelEducacional().toUpperCase() + "'";            
            and = " and ";
            where = "";
        }
        if(obj.getProcessoMatricula().getCodigo().intValue() !=0){
            sql += where + and + " processoMatricula = " + obj.getProcessoMatricula().getCodigo().intValue();            
            and = " and ";
            where = "";
        }
        if(obj.getProcSeletivo().getCodigo().intValue() !=0){
            sql += where + and + " procSeletivoc = " + obj.getProcSeletivo().getCodigo().intValue();            
            and = " and ";
            where = "";
        }
        if(obj.getTurno().getCodigo().intValue() !=0){
            sql += where + and + " turno = " + obj.getTurno().getCodigo().intValue();            
            and = " and ";
            where = "";
        }
        if(obj.getCentroReceita().getCodigo().intValue() !=0){
            sql += where +  and + " centroReceita = " + obj.getCentroReceita().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(obj.getUnidadeEnsino().getCodigo().intValue() !=0){
            sql += where + and + " unidadeEnsino = " + obj.getUnidadeEnsino().getCodigo().intValue();            
            and = " and ";
            where = "";
        }
        sql += " group by  unidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);

        return (SqlRowSet) (montarDadosConsulta(tabelaResultado));
    }
    
    public SqlRowSet consultaGeracaoRelatorioLinhaTempo(ReceitaDWVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "Select sum(ReceitaDW.valor) as valor, UnidadeEnsino.nome as unidadeEnsino, ReceitaDW.ano as ano, ReceitaDW.mes as mes, ReceitaDW.data as data from ReceitaDW left join UnidadeEnsino  on ReceitaDW.unidadeEnsino = UnidadeEnsino.codigo ";
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
        if(obj.getAreaConhecimento().getCodigo().intValue() !=0){
            sql += where +  and + " areaConhecimento = " + obj.getAreaConhecimento().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(obj.getCurso().getCodigo().intValue() !=0){
            sql += where +  and + " curso = " + obj.getCurso().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(obj.getCentroReceita().getCodigo().intValue() !=0){
            sql += where +  and + " centroReceita = " + obj.getCentroReceita().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(!obj.getNivelEducacional().equals("")){
            sql += where +  and + " upper (nivelEducacional) = '" + obj.getNivelEducacional().toUpperCase() + "'";            
            and = " and ";
            where = " ";
        }
        if(obj.getProcessoMatricula().getCodigo().intValue() !=0){
            sql +=  where + and + " processoMatricula = " + obj.getProcessoMatricula().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(obj.getProcSeletivo().getCodigo().intValue() !=0){
            sql +=  where + and + " procSeletivo = " + obj.getProcSeletivo().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(obj.getTurno().getCodigo().intValue() !=0){
            sql +=  where + and + " turno = " + obj.getTurno().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        if(obj.getUnidadeEnsino().getCodigo().intValue() !=0){
            sql +=  where + and + " unidadeEnsino = " + obj.getUnidadeEnsino().getCodigo().intValue();            
            and = " and ";
            where = " ";
        }
        sql += " group by  unidadeEnsino.nome, data, mes, ano  order by data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return (SqlRowSet) (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>AreaConhecimento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ReceitaDW.* FROM ReceitaDW, AreaConhecimento WHERE ReceitaDW.areaConhecimento = AreaConhecimento.codigo and upper( AreaConhecimento.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY AreaConhecimento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>PeriodoLetivo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDescricaoPeriodoLetivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ReceitaDW.* FROM ReceitaDW, PeriodoLetivo WHERE ReceitaDW.periodoLetivo = PeriodoLetivo.codigo and upper( PeriodoLetivo.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PeriodoLetivo.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Turno</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeTurno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ReceitaDW.* FROM ReceitaDW, Turno WHERE ReceitaDW.turno = Turno.codigo and upper( Turno.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Turno.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ReceitaDW.* FROM ReceitaDW, Curso WHERE ReceitaDW.curso = Curso.codigo and upper( Curso.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Curso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ReceitaDW.* FROM ReceitaDW, UnidadeEnsino WHERE ReceitaDW.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>identificadorCentroReceita</code> da classe <code>CentroReceita</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ReceitaDW.* FROM ReceitaDW, CentroReceita WHERE ReceitaDW.centroReceita = CentroReceita.codigo and upper( CentroReceita.identificadorCentroReceita ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CentroReceita.identificadorCentroReceita";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ReceitaDW WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>ReceitaDW</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ReceitaDW WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ReceitaDWVO</code> resultantes da consulta.
    */
     public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado , Uteis.NIVELMONTARDADOS_TODOS));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ReceitaDWVO</code>.
     * @return  O objeto da classe <code>ReceitaDWVO</code> com os dados devidamente montados.
    */
    public static ReceitaDWVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ReceitaDWVO obj = new ReceitaDWVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setData( dadosSQL.getDate("data"));
        obj.setMes( new Integer( dadosSQL.getInt("mes")));
        obj.getProcSeletivo().setCodigo( new Integer( dadosSQL.getInt("procSeletivo")));
        obj.setAno( new Integer( dadosSQL.getInt("ano")));
        obj.setValor( new Double( dadosSQL.getDouble("valor")));
        obj.getCentroReceita().setCodigo( new Integer( dadosSQL.getInt("centroReceita")));
        obj.getUnidadeEnsino().setCodigo( new Integer( dadosSQL.getInt("unidadeEnsino")));
        obj.getCurso().setCodigo( new Integer( dadosSQL.getInt("curso")));
        obj.getTurno().setCodigo( new Integer( dadosSQL.getInt("turno")));
        obj.getProcessoMatricula().setCodigo( new Integer( dadosSQL.getInt("processoMatricula")));
        obj.getAreaConhecimento().setCodigo( new Integer( dadosSQL.getInt("areaConhecimento")));
        obj.setNivelEducacional( dadosSQL.getString("nivelEducacional"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

//        montarDadosCentroReceita(obj, nivelMontarDados);
//        montarDadosUnidadeEnsino(obj, nivelMontarDados);
//        montarDadosCurso(obj, nivelMontarDados);
//        montarDadosTurno(obj, nivelMontarDados);
//        montarDadosPeriodoLetivo(obj, nivelMontarDados);
//        montarDadosAreaConhecimento(obj, nivelMontarDados);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>ReceitaDWVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAreaConhecimento(ReceitaDWVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PeriodoLetivoVO</code> relacionado ao objeto <code>ReceitaDWVO</code>.
     * Faz uso da chave primária da classe <code>PeriodoLetivoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
//    public static void montarDadosPeriodoLetivo(ReceitaDWVO obj, int nivelMontarDados) throws Exception {
//        if (obj.getPeriodoLetivo().getCodigo().intValue() == 0) {
//            obj.setPeriodoLetivo(new PeriodoLetivoVO());
//            return;
//        }
//        obj.setPeriodoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeriodoLetivo().getCodigo()));
//    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto <code>ReceitaDWVO</code>.
     * Faz uso da chave primária da classe <code>TurnoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosTurno(ReceitaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getTurno().getCodigo().intValue() == 0) {
            obj.setTurno(new TurnoVO());
            return;
        }
        obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>ReceitaDWVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosCurso(ReceitaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>ReceitaDWVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosUnidadeEnsino(ReceitaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CentroReceitaVO</code> relacionado ao objeto <code>ReceitaDWVO</code>.
     * Faz uso da chave primária da classe <code>CentroReceitaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosCentroReceita(ReceitaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceita().getCodigo().intValue() == 0) {
            obj.setCentroReceita(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceita(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceita().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ReceitaDWVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public ReceitaDWVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario ) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM ReceitaDW WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ReceitaDW ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return ReceitaDW.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        ReceitaDW.idEntidade = idEntidade;
    }
}