package negocio.facade.jdbc.basico;

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
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.enumeradores.RegiaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.EstadoInterfaceFacade;


/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>EstadoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>EstadoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see EstadoVO
 * @see ControleAcesso
 */

@Repository @Scope("singleton") @Lazy 
public class Estado extends ControleAcesso implements EstadoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2147711325199635532L;
	protected static String idEntidade;

    public Estado() throws Exception {
        super();
        setIdEntidade("Estado");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>EstadoVO</code>.
     */
    public EstadoVO novo() throws Exception {
        Estado.incluir(getIdEntidade());
        EstadoVO obj = new EstadoVO();
        return obj;
    }
    
    @Override
    public void persistir(EstadoVO estadoVO, UsuarioVO usuarioVO) throws Exception {
    	if(estadoVO.getCodigo().equals(0)) {
    		incluir(estadoVO, usuarioVO);
    	} else {
    		alterar(estadoVO, usuarioVO);
    	}
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>EstadoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>EstadoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final EstadoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            EstadoVO.validarDados(obj);
            Estado.incluir(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Estado( sigla, nome, paiz, codigoinep, regiao ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
             obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
     		  PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                  sqlInserir.setString(1, obj.getSigla());
                  sqlInserir.setString(2, obj.getNome());
                  if (obj.getPaiz().getCodigo().intValue() != 0) {
                      sqlInserir.setInt(3, obj.getPaiz().getCodigo().intValue());
                  } else {
                      sqlInserir.setNull(3, 0);
                  }
                  sqlInserir.setInt(4, obj.getCodigoInep());
                  if(obj.getRegiao() != null){
                	  sqlInserir.setString(5, obj.getRegiao().name());
                  }else{
                	  sqlInserir.setNull(5, 0);
                  }
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
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>EstadoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>EstadoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final EstadoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            EstadoVO.validarDados(obj);
            Estado.alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Estado set sigla=?, nome=?, paiz=?, codigoinep=?, regiao = ? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                        sqlAlterar.setString(1, obj.getSigla());
                        sqlAlterar.setString(2, obj.getNome());
                        if (obj.getPaiz().getCodigo().intValue() != 0) {
                            sqlAlterar.setInt(3, obj.getPaiz().getCodigo().intValue());
                        } else {
                            sqlAlterar.setNull(3, 0);
                        }
                        sqlAlterar.setInt(4, obj.getCodigoInep());                        
                        if(obj.getRegiao() != null){
                      	  sqlAlterar.setString(5, obj.getRegiao().name());
                        }else{
                        	sqlAlterar.setNull(5, 0);
                        }
                        sqlAlterar.setInt(6, obj.getCodigo().intValue());
                        return sqlAlterar;
                }

            });

        } catch (Exception e) {
            throw e;
       }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>EstadoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>EstadoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(EstadoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            Estado.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Estado WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Estado</code> através do valor do atributo
     * <code>nome</code> da classe <code>Paiz</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>EstadoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    
    
    public List<EstadoVO> consultarPorCodigoPaiz(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT Estado.* FROM Estado, Paiz WHERE Estado.paiz = Paiz.codigo  ");
        if(valorConsulta != null && valorConsulta > 0){
        	sqlStr.append(" and Paiz.codigo = " + valorConsulta + "");
        }
        sqlStr.append(" ORDER BY Paiz.nome, Estado.sigla");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);

    }

    /**
     * Responsável por realizar uma consulta de <code>Estado</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Paiz</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>EstadoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<EstadoVO> consultarPorNomePaiz(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Estado.* FROM Estado, Paiz WHERE Estado.paiz = Paiz.codigo and upper( Paiz.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Paiz.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Estado</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EstadoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<EstadoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM estado WHERE sem_acentos(estado.nome) ILIKE '").append(Uteis.removerAcentos(valorConsulta));
        sqlStr.append("%' ORDER BY estado.nome;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Estado</code> através do valor do atributo 
     * <code>String sigla</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EstadoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<EstadoVO> consultarPorSigla(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Estado WHERE upper( sigla ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY sigla";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    public EstadoVO consultarPorSigla(String valorConsulta, UsuarioVO usuario) throws Exception {
    	String sqlStr = "SELECT * FROM Estado WHERE upper( sigla ) like(?) ORDER BY sigla limit 1";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
    	if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
        }
    	return null;
    }
    
    /**
     * Responsável por realizar uma consulta de <code>Estado</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EstadoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<EstadoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Estado WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }
    
    /**
     * Operação responsável por localizar um objeto da classe <code>EstadoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EstadoVO consultarPorCodigoCidade(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM estado, cidade WHERE cidade.estado = estado.codigo AND cidade.codigo = " + valorConsulta;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Estado ).");
        }
        return montarDados(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>EstadoVO</code> resultantes da consulta.
     */
    public static List<EstadoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<EstadoVO> vetResultado = new ArrayList<EstadoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>EstadoVO</code>.
     * @return  O objeto da classe <code>EstadoVO</code> com os dados devidamente montados.
     */
    public static EstadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        EstadoVO obj = new EstadoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setSigla(dadosSQL.getString("sigla"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
        	return obj;
        }
        obj.setNome(dadosSQL.getString("nome"));
        obj.getPaiz().setCodigo(new Integer(dadosSQL.getInt("paiz")));
        String regiao = dadosSQL.getString("regiao");
        if ((regiao != null) && 
            (!regiao.trim().isEmpty()) &&
            (!obj.getSigla().trim().isEmpty())) {
            try {
                obj.setRegiao(RegiaoEnum.valueOf(regiao));
            } catch (Exception e) {
            }
        }
        obj.setCodigoInep(dadosSQL.getInt("codigoinep"));
        obj.setCodigoIBGE(dadosSQL.getString("codigoibge"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosPaiz(obj,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PaizVO</code> relacionado ao objeto <code>EstadoVO</code>.
     * Faz uso da chave primária da classe <code>PaizVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPaiz(EstadoVO obj,UsuarioVO usuario) throws Exception {
        if (obj.getPaiz().getCodigo().intValue() == 0) {
            obj.setPaiz(new PaizVO());
            return;
        }
        obj.setPaiz(getFacadeFactory().getPaizFacade().consultarPorChavePrimaria(obj.getPaiz().getCodigo(), false,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>EstadoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EstadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
       return getAplicacaoControle().getEstadoVO(codigoPrm);
    }
    
    @Override
    public EstadoVO consultarPorChavePrimariaUnico(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), false,usuario);
    	String sql = "SELECT * FROM Estado WHERE codigo = " + codigoPrm;
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados ( Estado ).");
    	}
    	return montarDados(tabelaResultado, nivelMontarDados,usuario);
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Estado.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Estado.idEntidade = idEntidade;
    }
    
    @Override
    public List<EstadoVO> consultarEstadosMinimo() throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT codigo, sigla FROM estado ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
    }
}