package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.TipoCatalogoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TipoCatalogoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TipoCatalogoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TipoCatalogoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TipoCatalogo extends ControleAcesso implements TipoCatalogoInterfaceFacade {

    protected static String idEntidade;

    public TipoCatalogo() throws Exception {
        super();
        setIdEntidade("TipoCatalogo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>TipoCatalogoVO</code>.
     */
    public TipoCatalogoVO novo() throws Exception {
        TipoCatalogo.incluir(getIdEntidade());
        TipoCatalogoVO obj = new TipoCatalogoVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TipoCatalogoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TipoCatalogoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Tipo Obra) deve ser informado.");
        }
        if(!obj.getCodigo().equals(0) && obj.getCodigo().equals(obj.getSubdivisao())) {
        	throw new ConsistirException("O campo SUBDIVISÃO deve ser diferente do Tipo Catálogo.");
        }
        if(validarLoopTipoCatalogoPrincipal(obj)) {
        	throw new ConsistirException("Não é possível realizar essa operação, pois o Tipo de Catálo Superior irá gerar um loop de Hierarquia.");
        }
    }
    
    public void validarDadosUnicidadeSigla(TipoCatalogoVO obj) throws Exception {
    	if (!obj.getSigla().equals("")) {
    		boolean existeSigla = consultarTipoCatalogoPorSigla(obj.getCodigo(), obj.getSigla());
    		if (existeSigla) {
    			throw new Exception("A sigla informada já está sendo utilizada para outro tipo de catálogo.");
    		}
    	}
    }
    
    public boolean consultarTipoCatalogoPorSigla(Integer codigo, String sigla) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select codigo from tipoCatalogo where sigla ilike ('").append(sigla).append("') ").append(" and codigo != ").append(codigo);
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (rs.next()) {
    		return true;
    	}
    	return false;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>TipoCatalogoVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TipoCatalogoVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoCatalogoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            validarDadosUnicidadeSigla(obj);
            TipoCatalogo.incluir(getIdEntidade(), true, usuarioVO);

            final String sql = "INSERT INTO TipoCatalogo( nome, sigla, subdivisao ) VALUES ( ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getSigla());
                    sqlInserir.setInt(3, obj.getSubdivisao());
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
        } catch (DuplicateKeyException e) {
            obj.setNovoObj(true);
            throw new Exception("Um registro com esse nome já está cadastrado no sistema.");
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>TipoCatalogoVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TipoCatalogoVO</code> que será alterada
     *            no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TipoCatalogoVO obj,UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            validarDadosUnicidadeSigla(obj);
            TipoCatalogo.alterar(getIdEntidade(), true, usuarioVO);

            final String sql = "UPDATE TipoCatalogo set nome=?, sigla=?, subdivisao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getSigla());
                    sqlAlterar.setInt(3, obj.getSubdivisao());
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (DuplicateKeyException e) {
            obj.setNovoObj(true);
            throw new Exception("Um registro com esse nome já está cadastrado no sistema.");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>TipoCatalogoVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TipoCatalogoVO</code> que será removido
     *            no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TipoCatalogoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoCatalogo.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM TipoCatalogo WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoCatalogo</code>
     * através do valor do atributo <code>String nome</code>. Retorna os
     * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
     * trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>TipoCatalogoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoCatalogo WHERE upper( nome ) like('" + valorConsulta.toUpperCase()
                + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    
    /**
     * Responsável por realizar consulta de <code>TipoCatalogo</code>
     * por meio do valor do atributo <code>Integer codigo</code>.
     * Retorna os objetos que não possuam o codigo ou subdivisão 
     * com valores iguais ao atributo fornecido. 
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.     
     * @return List Contendo objetos da classe 
     *         <code>TipoCatalogoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso. 
     */
    public List<TipoCatalogoVO> consultarSubdivisaoSuperiorPorIdentificador(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), true, usuario);
    	StringBuilder sqlString = getSqlConsultaCompleta();
    	SqlRowSet tabelaResultado;
    	
    	if(codigo != 0) {
    		sqlString.append(" WHERE tipocatalogo.codigo <> (?)");
        	sqlString.append(" AND tipocatalogo.subdivisao <> (?) ");
        	sqlString.append(" ORDER BY tipocatalogo.nome");
        	tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlString.toString(), codigo, codigo);
    	}else {
    		sqlString.append(" ORDER BY tipocatalogo.nome");
    		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlString.toString());
    	}
    	
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    

    /**
     * Responsável por realizar uma consulta de <code>TipoCatalogo</code>
     * através do valor do atributo <code>Integer codigo</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>TipoCatalogoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoCatalogo WHERE codigo = " + valorConsulta.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<TipoCatalogoVO> consultarTipoCatalogoComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT codigo, nome, sigla FROM TipoCatalogo ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            TipoCatalogoVO obj = new TipoCatalogoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            obj.setSigla(tabelaResultado.getString("sigla"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>TipoCatalogoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>TipoCatalogoVO</code>.
     *
     * @return O objeto da classe <code>TipoCatalogoVO</code> com os dados
     *         devidamente montados.
     */
    public static TipoCatalogoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        TipoCatalogoVO obj = new TipoCatalogoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setSigla(dadosSQL.getString("sigla"));
        obj.setSubdivisao(dadosSQL.getInt("subdivisao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>TipoCatalogoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public TipoCatalogoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TipoCatalogo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }
    
    public TipoCatalogoVO consultarPorNomeRegistroUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT * FROM TipoCatalogo WHERE sem_acentos(nome) ilike ('%" + Uteis.removerAcentos(valorConsulta.toUpperCase()) +"%') LIMIT 1 ";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!rs.next()) {
        	return new TipoCatalogoVO();
        }
        return montarDados(rs, nivelMontarDados);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return TipoCatalogo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        TipoCatalogo.idEntidade = idEntidade;
    }
    
    
    public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT ");
		sql.append(" tipocatalogo.codigo,  ");
		sql.append(" tipocatalogo.nome, ");
		sql.append(" tipocatalogo.sigla, ");
		sql.append(" CASE WHEN tipocatalogo.subdivisao <> 0 THEN tipocatalogo.subdivisao ELSE NULL END AS subdivisao, ");
		sql.append(" tipocatalogoprincipal.codigo AS codigosuperior, ");
		sql.append(" tipocatalogoprincipal.nome AS nomesuperior, ");
		sql.append(" tipocatalogoprincipal.subdivisao AS subdivisaosuperior ");
		sql.append(" FROM tipocatalogo ");
		sql.append(" LEFT JOIN tipocatalogo AS tipocatalogoprincipal ON tipocatalogoprincipal.codigo = tipocatalogo.subdivisao ");
		return sql;
	}
    
     
    @Override
    public TreeNodeCustomizado consultarArvoreTipoCatalogoSuperior(TipoCatalogoVO tipoCatalogoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" WITH RECURSIVE tipocatalogosuperior( ");
		sql.append(" codigo, nome, sigla, subdivisao, codigotipocatalogoprincipal, nometipocatalogoprincipal, subdivisaotipocatalogoprincipal ");
		sql.append(" ) AS ( ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" WHERE tipocatalogo.codigo = ").append(tipoCatalogoVO.getCodigo());
		sql.append(" union ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" INNER JOIN tipocatalogosuperior ON tipocatalogosuperior.codigotipocatalogoprincipal  = tipocatalogo.codigo ");
		sql.append(" ) select * from tipocatalogosuperior order by tipocatalogosuperior.codigo ");
				
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		TreeNodeCustomizado TreeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<Integer, TreeNodeCustomizado>(0);
		while (rs.next()) {
			TipoCatalogoVO obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj);
			if(!nodes.containsKey(obj.getSubdivisao())) {
				nodes.put(obj.getSubdivisao(), TreeNodeCustomizadoRaiz);
			}
			nodeImpl.setData(obj);
			nodeImpl.setMaximizarTree(true);
			nodes.get(obj.getSubdivisao()).addChild(obj, nodeImpl);
			nodes.put(obj.getCodigo(), nodeImpl);
		}
		return TreeNodeCustomizadoRaiz;  
    }
    
    
    @Override
    public TreeNodeCustomizado consultarArvoreTipoCatalogoInferior(TipoCatalogoVO tipoCatalogoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sql = new StringBuilder();		
		sql.append(" WITH RECURSIVE tipocatalogoinferior( ");
		sql.append(" codigo, nome, sigla, subdivisao,    ");
		sql.append(" codigotipocatalogoprincipal, nometipocatalogoprincipal, subdivisaotipocatalogoprincipal ");
		sql.append(" ) AS ( ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" where tipocatalogo.codigo = ").append(tipoCatalogoVO.getCodigo());
		sql.append(" union ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" INNER JOIN tipocatalogoinferior ON tipocatalogoinferior.codigo = tipocatalogo.subdivisao ");
		sql.append(" ) select * from tipocatalogoinferior ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		TreeNodeCustomizado TreeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<Integer, TreeNodeCustomizado>(0);
		while (rs.next()) {
			TipoCatalogoVO obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj);	
			if(!nodes.containsKey(obj.getSubdivisao())) {
				nodes.put(obj.getSubdivisao(), TreeNodeCustomizadoRaiz);
			}			
			nodeImpl.setData(obj);			
			nodeImpl.setMaximizarTree(true);
			nodes.get(obj.getSubdivisao()).addChild(obj, nodeImpl);			
			nodes.put(obj.getCodigo(), nodeImpl);
		}
		return TreeNodeCustomizadoRaiz;
    }
    
    public static boolean validarLoopTipoCatalogoPrincipal(TipoCatalogoVO obj) {
    	if(!Uteis.isAtributoPreenchido(obj.getSubdivisao())){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select 1 as qtde from (");
		sql.append(" with recursive tpSuperior (\"tipoCatalogo.codigo\", \"tipoCatalogo.subdivisao\") as (");
		sql.append(" select ");
		sql.append(" tipocatalogo.codigo as \"tipoCatalogo.codigo\",");
		sql.append(" tipocatalogo.subdivisao as \"tipoCatalogo.subdivisao\"");
		sql.append(" from tipocatalogo ");
		sql.append(" where tipocatalogo.codigo = ").append(obj.getSubdivisao());
		sql.append(" union ");
		sql.append(" select ");
		sql.append(" tipocatalogo.codigo as \"tipoCatalogo.codigo\",");
		sql.append(" tipocatalogo.subdivisao as \"tipoCatalogo.subdivisao\"");
		sql.append(" from tipocatalogo ");
		sql.append(" inner join tpSuperior on tipocatalogo.codigo = tpSuperior.\"tipoCatalogo.subdivisao\"");
		sql.append(" )");
		sql.append(" select * from tpSuperior order by ");
		sql.append(" case ");
		sql.append(" when tpSuperior.\"tipoCatalogo.subdivisao\" is null then 0 ");
		sql.append(" when tpSuperior.\"tipoCatalogo.subdivisao\" > tpSuperior.\"tipoCatalogo.codigo\" then tpSuperior.\"tipoCatalogo.codigo\"");
		sql.append(" else tpSuperior.\"tipoCatalogo.subdivisao\"");
		sql.append(" end, tpSuperior.\"tipoCatalogo.codigo\"");
		sql.append(" ) as t ");
		sql.append(" where \"tipoCatalogo.codigo\" in (").append(obj.getCodigo()).append(")");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Integer existeCentroResultadoPai = (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		return Uteis.isAtributoPreenchido(existeCentroResultadoPai);
    	
    }
    
}
