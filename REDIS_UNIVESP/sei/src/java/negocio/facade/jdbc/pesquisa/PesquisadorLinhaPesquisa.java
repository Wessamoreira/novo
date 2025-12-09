package negocio.facade.jdbc.pesquisa;
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

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.LinhaPesquisaVO;
import negocio.comuns.pesquisa.PesquisadorConvidadoVO;
import negocio.comuns.pesquisa.PesquisadorLinhaPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.pesquisa.PesquisadorLinhaPesquisaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PesquisadorLinhaPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PesquisadorLinhaPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PesquisadorLinhaPesquisaVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class PesquisadorLinhaPesquisa extends ControleAcesso implements PesquisadorLinhaPesquisaInterfaceFacade {
	
    protected static String idEntidade;
	
    public PesquisadorLinhaPesquisa() throws Exception {
        super();
        setIdEntidade("PesquisadorLinhaPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>PesquisadorLinhaPesquisaVO</code>.
    */
    public PesquisadorLinhaPesquisaVO novo() throws Exception {
        PesquisadorLinhaPesquisa.incluir(getIdEntidade());
        PesquisadorLinhaPesquisaVO obj = new PesquisadorLinhaPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PesquisadorLinhaPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PesquisadorLinhaPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PesquisadorLinhaPesquisaVO obj) throws Exception {
        try {
            
            PesquisadorLinhaPesquisaVO.validarDados(obj);
            PesquisadorLinhaPesquisa.incluir(getIdEntidade());
            final String sql = "INSERT INTO PesquisadorLinhaPesquisa( tipoPesquisador, pesquisadorProfessor, pesquisadorAluno, pesquisadorConvidado, dataFiliacao, situacao, linhaPesquisa ) VALUES ( ?, ?, ?, ?, ?, ?, ? )returning codigo";
           obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setString( 1, obj.getTipoPesquisador() );
            sqlInserir.setInt( 2, obj.getPesquisadorProfessor().getCodigo().intValue() );
            sqlInserir.setString( 3, obj.getPesquisadorAluno().getMatricula() );
            sqlInserir.setInt( 4, obj.getPesquisadorConvidado().getCodigo().intValue() );
            sqlInserir.setDate( 5, Uteis.getDataJDBC(obj.getDataFiliacao() ));
            sqlInserir.setString( 6, obj.getSituacao() );
            sqlInserir.setInt( 7, obj.getLinhaPesquisa().getCodigo().intValue() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PesquisadorLinhaPesquisaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PesquisadorLinhaPesquisaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PesquisadorLinhaPesquisaVO obj) throws Exception {
        try {
          
            PesquisadorLinhaPesquisaVO.validarDados(obj);
            PesquisadorLinhaPesquisa.alterar(getIdEntidade());
            final String sql = "UPDATE PesquisadorLinhaPesquisa set tipoPesquisador=?, pesquisadorProfessor=?, pesquisadorAluno=?, pesquisadorConvidado=?, dataFiliacao=?, situacao=?, linhaPesquisa=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setString( 1, obj.getTipoPesquisador() );
            sqlAlterar.setInt( 2, obj.getPesquisadorProfessor().getCodigo().intValue() );
            sqlAlterar.setString( 3, obj.getPesquisadorAluno().getMatricula() );
            sqlAlterar.setInt( 4, obj.getPesquisadorConvidado().getCodigo().intValue() );
            sqlAlterar.setDate( 5, Uteis.getDataJDBC(obj.getDataFiliacao() ));
            sqlAlterar.setString( 6, obj.getSituacao() );
            sqlAlterar.setInt( 7, obj.getLinhaPesquisa().getCodigo().intValue() );
            sqlAlterar.setInt( 8, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PesquisadorLinhaPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PesquisadorLinhaPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PesquisadorLinhaPesquisaVO obj) throws Exception {
        try {
           
            PesquisadorLinhaPesquisa.excluir(getIdEntidade());
            String sql = "DELETE FROM PesquisadorLinhaPesquisa WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>LinhaPesquisa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeLinhaPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PesquisadorLinhaPesquisa.* FROM PesquisadorLinhaPesquisa, LinhaPesquisa WHERE PesquisadorLinhaPesquisa.linhaPesquisa = LinhaPesquisa.codigo and LinhaPesquisa.nome like('" + valorConsulta + "%') ORDER BY LinhaPesquisa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PesquisadorLinhaPesquisa WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>Date dataFiliacao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDataFiliacao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PesquisadorLinhaPesquisa WHERE ((dataFiliacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFiliacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFiliacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>PesquisadorConvidado</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorCodigoPesquisadorConvidado(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PesquisadorLinhaPesquisa.* FROM PesquisadorLinhaPesquisa, PesquisadorConvidado WHERE PesquisadorLinhaPesquisa.pesquisadorConvidado = PesquisadorConvidado.codigo and PesquisadorConvidado.codigo >= " + valorConsulta.intValue() + " ORDER BY PesquisadorConvidado.codigo";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PesquisadorLinhaPesquisa.* FROM PesquisadorLinhaPesquisa, Matricula WHERE PesquisadorLinhaPesquisa.pesquisadorAluno = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') ORDER BY Matricula.matricula";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PesquisadorLinhaPesquisa.* FROM PesquisadorLinhaPesquisa, Pessoa WHERE PesquisadorLinhaPesquisa.pesquisadorProfessor = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>String tipoPesquisador</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoPesquisador(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PesquisadorLinhaPesquisa WHERE tipoPesquisador like('" + valorConsulta + "%') ORDER BY tipoPesquisador";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorLinhaPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PesquisadorLinhaPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PesquisadorLinhaPesquisaVO</code> resultantes da consulta.
    */
      public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado,Uteis.NIVELMONTARDADOS_TODOS,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PesquisadorLinhaPesquisaVO</code>.
     * @return  O objeto da classe <code>PesquisadorLinhaPesquisaVO</code> com os dados devidamente montados.
    */
    public static PesquisadorLinhaPesquisaVO montarDados(SqlRowSet dadosSQL,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        PesquisadorLinhaPesquisaVO obj = new PesquisadorLinhaPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setTipoPesquisador( dadosSQL.getString("tipoPesquisador"));
        obj.getPesquisadorProfessor().setCodigo( new Integer( dadosSQL.getInt("pesquisadorProfessor")));
        obj.getPesquisadorAluno().setMatricula( dadosSQL.getString("pesquisadorAluno"));
        obj.getPesquisadorConvidado().setCodigo( new Integer( dadosSQL.getInt("pesquisadorConvidado")));
        obj.setDataFiliacao( dadosSQL.getDate("dataFiliacao"));
        obj.setSituacao( dadosSQL.getString("situacao"));
        obj.getLinhaPesquisa().setCodigo( new Integer( dadosSQL.getInt("linhaPesquisa")));
        obj.setNovoObj(Boolean.FALSE);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
            return  obj;
        }
        montarDadosPesquisadorProfessor(obj, nivelMontarDados,usuario);
        montarDadosPesquisadorAluno(obj, nivelMontarDados,usuario);
        montarDadosPesquisadorConvidado(obj, nivelMontarDados,usuario);
        montarDadosLinhaPesquisa(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>LinhaPesquisaVO</code> relacionado ao objeto <code>PesquisadorLinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>LinhaPesquisaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosLinhaPesquisa(PesquisadorLinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getLinhaPesquisa().getCodigo().intValue() == 0) {
            obj.setLinhaPesquisa(new LinhaPesquisaVO());
            return;
        }
        obj.setLinhaPesquisa(getFacadeFactory().getLinhaPesquisaFacade().consultarPorChavePrimaria( obj.getLinhaPesquisa().getCodigo(), nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PesquisadorConvidadoVO</code> relacionado ao objeto <code>PesquisadorLinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>PesquisadorConvidadoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosPesquisadorConvidado(PesquisadorLinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPesquisadorConvidado().getCodigo().intValue() == 0) {
            obj.setPesquisadorConvidado(new PesquisadorConvidadoVO());
            return;
        }
        obj.setPesquisadorConvidado(getFacadeFactory().getPesquisadorConvidadoFacade().consultarPorChavePrimaria( obj.getPesquisadorConvidado().getCodigo(),usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>PesquisadorLinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosPesquisadorAluno(PesquisadorLinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if ((obj.getPesquisadorAluno().getMatricula() == null) || (obj.getPesquisadorAluno().getMatricula().equals(""))) {
            obj.setPesquisadorAluno(new MatriculaVO());
            return;
        }
        obj.setPesquisadorAluno(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getPesquisadorAluno().getMatricula(),0, NivelMontarDados.getEnum(nivelMontarDados),usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PesquisadorLinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosPesquisadorProfessor(PesquisadorLinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPesquisadorProfessor().getCodigo().intValue() == 0) {
            obj.setPesquisadorProfessor(new PessoaVO());
            return;
        }
        obj.setPesquisadorProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria( obj.getPesquisadorProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PesquisadorLinhaPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public PesquisadorLinhaPesquisaVO consultarPorChavePrimaria( Integer codigoPrm,int nivelMontarDados,UsuarioVO usuario ) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM PesquisadorLinhaPesquisa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return PesquisadorLinhaPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        PesquisadorLinhaPesquisa.idEntidade = idEntidade;
    }
}