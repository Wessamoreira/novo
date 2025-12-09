package negocio.facade.jdbc.academico;

import negocio.comuns.academico.CategoriaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CategoriaTurmaInterfaceFacade;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CategoriaTurmaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CategoriaTurmaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CategoriaTurmaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class CategoriaTurma extends ControleAcesso implements CategoriaTurmaInterfaceFacade {

    protected static String idEntidade;

    public CategoriaTurma() throws Exception {
        super();
        setIdEntidade("CategoriaTurma");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CategoriaTurmaVO</code>.
     */
    public CategoriaTurmaVO novo() throws Exception {
        CategoriaTurma.incluir(getIdEntidade());
        CategoriaTurmaVO obj = new CategoriaTurmaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CategoriaTurmaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CategoriaTurmaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CategoriaTurmaVO obj,UsuarioVO usuario) throws Exception {
        try {
            validarUnicidade(obj,usuario);
            CategoriaTurmaVO.validarDados(obj);
            incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO CategoriaTurma( turma, tipoCategoria ) VALUES ( ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getTurma().getCodigo());
                    sqlInserir.setInt(2, obj.getTipoCategoria().getCodigo());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CategoriaTurmaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CategoriaTurmaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CategoriaTurmaVO obj,UsuarioVO usuario) throws Exception {
        try {
            //validarUnicidade(obj,usuario);
            CategoriaTurmaVO.validarDados(obj);
            alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE CategoriaTurma set turma=?, tipoCategoria=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getTurma().getCodigo());
                    sqlAlterar.setInt(2, obj.getTipoCategoria().getCodigo());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CategoriaTurmaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CategoriaTurmaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CategoriaTurmaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM CategoriaTurma WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CategoriaTurma</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CategoriaTurmaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CategoriaTurma inner join turma on turma.codigo = categoriaturma.turma WHERE turma.codigo = " + turma + " ORDER BY turma.identificadorTurma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorTurma(String turma, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM CategoriaTurma inner join turma on turma.codigo = categoriaturma.turma WHERE sem_acentos(turma.identificadorTurma) ilike sem_acentos(?) ORDER BY turma.identificadorTurma";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, turma+"%");
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List consultarPorTipoCategoria(String tipoCategoria, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM CategoriaTurma inner join tipoCategoria on tipoCategoria.codigo = categoriaTurma.tipoCategoria WHERE sem_acentos(tipoCategoria.nome) ilike sem_acentos(?) ORDER BY tipoCategoria.nome ";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, tipoCategoria+"%");
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    /**
     * Responsável por realizar uma consulta de <code>CategoriaTurma</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CategoriaTurmaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CategoriaTurma WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CategoriaTurmaVO</code> resultantes da consulta.
     */
    public List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CategoriaTurmaVO</code>.
     * @return  O objeto da classe <code>CategoriaTurmaVO</code> com os dados devidamente montados.
     */
    public CategoriaTurmaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CategoriaTurmaVO obj = new CategoriaTurmaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
        obj.getTipoCategoria().setCodigo(dadosSQL.getInt("tipoCategoria"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosTurma(obj, nivelMontarDados, usuario);
        montarDadosTipoCategoria(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void montarDadosTurma(CategoriaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void montarDadosTipoCategoria(CategoriaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getTipoCategoria().getCodigo().intValue() == 0) {
    		return;
    	}
    	obj.setTipoCategoria(getFacadeFactory().getTipoCategoriaFacade().consultarPorChavePrimaria(obj.getTipoCategoria().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
    }
    
    /**
     * Operação responsável por localizar um objeto da classe <code>CategoriaTurmaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CategoriaTurmaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM CategoriaTurma WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public void validarUnicidade(CategoriaTurmaVO obj,UsuarioVO usuario) throws ConsistirException, Exception {
        List<CategoriaTurmaVO> lista = consultarPorCodigoTurma(obj.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        for (CategoriaTurmaVO repetido : lista) {
            if (repetido.getTurma().getCodigo().intValue() == obj.getTurma().getCodigo().intValue()) {
                throw new ConsistirException("Já existe uma categorização definida para essa turma!");
            }
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CategoriaTurma.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CategoriaTurma.idEntidade = idEntidade;
    }

}
