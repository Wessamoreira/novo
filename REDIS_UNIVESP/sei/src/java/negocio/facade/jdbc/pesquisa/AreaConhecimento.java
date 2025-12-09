package negocio.facade.jdbc.pesquisa;
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
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.AreaConhecimentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AreaConhecimentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>AreaConhecimentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see AreaConhecimentoVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class AreaConhecimento extends ControleAcesso implements AreaConhecimentoInterfaceFacade {
	
    protected static String idEntidade;
	
    public AreaConhecimento() throws Exception {
        super();
        setIdEntidade("AreaConhecimento");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>AreaConhecimentoVO</code>.
    */
    public AreaConhecimentoVO novo() throws Exception {
        AreaConhecimento.incluir(getIdEntidade());
        AreaConhecimentoVO obj = new AreaConhecimentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AreaConhecimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>AreaConhecimentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AreaConhecimentoVO obj, final Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            AreaConhecimentoVO.validarDados(obj);
            AreaConhecimento.incluir(getIdEntidade(), controlarAcesso, usuarioVO);
            final String sql = "INSERT INTO AreaConhecimento( nome, descricao, areaConhecimentoPrincipal ) VALUES ( ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setString( 1, obj.getNome() );
            sqlInserir.setString( 2, obj.getDescricao() );
            Uteis.setValuePreparedStatement(obj.getAreaConhecimentoPrincipal(), 3, sqlInserir);
            
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AreaConhecimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>AreaConhecimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AreaConhecimentoVO obj, final Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            AreaConhecimentoVO.validarDados(obj);
			AreaConhecimento.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
            final String sql = "UPDATE AreaConhecimento set nome=?, descricao=?, areaConhecimentoPrincipal=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setString( 1, obj.getNome() );
            sqlAlterar.setString( 2, obj.getDescricao() );            
            Uteis.setValuePreparedStatement(obj.getAreaConhecimentoPrincipal(), 3, sqlAlterar);
            sqlAlterar.setInt( 4, obj.getCodigo().intValue() );
           return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AreaConhecimentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>AreaConhecimentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
     @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AreaConhecimentoVO obj,final Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
			AreaConhecimento.excluir(getIdEntidade(), controlarAcesso, usuarioVO);
            String sql = "DELETE FROM AreaConhecimento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);;
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
     
     public List<AreaConhecimentoVO> consultarSecaoNivelComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
         ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
         String sqlStr = "SELECT DISTINCT codigo, nome FROM AreaConhecimento ORDER BY nome";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
         List vetResultado = new ArrayList(0);
         while (tabelaResultado.next()) {
        	 AreaConhecimentoVO obj = new AreaConhecimentoVO();
             obj.setCodigo(tabelaResultado.getInt("codigo"));
             obj.setNome(tabelaResultado.getString("nome"));
             vetResultado.add(obj);
         }
         return vetResultado;
     }

    /**
     * Responsável por realizar uma consulta de <code>AreaConhecimento</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AreaConhecimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AreaConhecimento WHERE sem_acentos(nome) ilike sem_acentos(?) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AreaConhecimento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AreaConhecimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM AreaConhecimento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>AreaConhecimentoVO</code> resultantes da consulta.
    */
      public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>AreaConhecimentoVO</code>.
     * @return  O objeto da classe <code>AreaConhecimentoVO</code> com os dados devidamente montados.
    */
    public static AreaConhecimentoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        AreaConhecimentoVO obj = new AreaConhecimentoVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setNome( dadosSQL.getString("nome"));
        obj.setDescricao( dadosSQL.getString("descricao"));
        obj.getAreaConhecimentoPrincipal().setCodigo( new Integer( dadosSQL.getInt("areaConhecimentoPrincipal")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosAreaConhecimentoPrincipal(obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>AreaConhecimentoVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAreaConhecimentoPrincipal(AreaConhecimentoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimentoPrincipal().getCodigo().intValue() == 0) {
            //obj.setAreaConhecimentoPrincipal(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimentoPrincipal(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria( obj.getAreaConhecimentoPrincipal().getCodigo(), usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AreaConhecimentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public AreaConhecimentoVO consultarPorChavePrimaria( Integer codigoPrm, UsuarioVO usuario) throws Exception {
        return getAplicacaoControle().getAreaConhecimentoVO(codigoPrm);
    }
    
    @Override
    public AreaConhecimentoVO consultarPorChavePrimariaUnico( Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM AreaConhecimento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Área Conhecimento).");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação responsável por obter o último valor gerado para uma chave primária.
     * É utilizada para obter o valor gerado pela SGBD para uma chave primária, 
     * a apresentação do mesmo e a implementação de possíveis relacionamentos. 
     */

	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return AreaConhecimento.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        AreaConhecimento.idEntidade = idEntidade;
    }
    
	public List<AreaConhecimentoVO> consultarDisciplinasPorGradeCurricularPeriodoLetivo(Integer gradeCurricular, Integer periodoLetivo, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select distinct areaconhecimento.codigo, areaconhecimento.nome from areaconhecimento ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.areaconhecimento = areaconhecimento.codigo ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.codigo = gradedisciplina.periodoletivo ");
		sqlStr.append(" where gradeCurricular = ").append(gradeCurricular);
		if (!periodoLetivo.equals(0)) {
			sqlStr.append(" and periodoletivo.codigo = ").append(periodoLetivo);
		}
		sqlStr.append(" union ");
		sqlStr.append(" select distinct areaconhecimento.codigo, areaconhecimento.nome from areaconhecimento ");
		sqlStr.append(" inner join curso on curso.areaconhecimento = areaconhecimento.codigo ");
		sqlStr.append(" inner join gradecurricular on gradecurricular.curso = curso.codigo ");
		sqlStr.append(" where gradeCurricular.codigo = ").append(gradeCurricular);		
		sqlStr.append(" order by nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		AreaConhecimentoVO obj = null;
		List<AreaConhecimentoVO> listaAreaConhecimentoVOs = new ArrayList<AreaConhecimentoVO>();
		while (tabelaResultado.next()) {
			obj = new AreaConhecimentoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaAreaConhecimentoVOs.add(obj);
		}
		return listaAreaConhecimentoVOs;
	}

    
   
}