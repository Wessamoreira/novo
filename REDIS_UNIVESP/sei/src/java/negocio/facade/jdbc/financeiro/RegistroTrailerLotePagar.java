package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.RegistroTrailerLotePagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroTrailerLotePagarInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class RegistroTrailerLotePagar extends ControleAcesso implements RegistroTrailerLotePagarInterfaceFacade{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 500924651223106407L;
	protected static String idEntidade;
	
	public RegistroTrailerLotePagar() throws Exception {
        super();
        setIdEntidade("RegistroTrailerLotePagar");
    }
    
    @Override
   	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
   	public void persistir(RegistroTrailerLotePagarVO obj, UsuarioVO usuarioVO) throws Exception {    	
    	if (obj.getCodigo() == 0) {
       		incluir(obj, usuarioVO);
       	} else {
       		alterar(obj,  usuarioVO);
       	}
    }
  

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final RegistroTrailerLotePagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO RegistroTrailerLotePagar (registroHeaderLotePagar, codigoBanco, loteServico, tipoRegistro, quantidadeRegistroLote, somatoraRegistroLote, numeroAvisoDebito   ) VALUES ( ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);        
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                	int i = 0;
                	sqlInserir.setInt(++i, obj.getRegistroHeaderLotePagarVO().getCodigo().intValue());
                    sqlInserir.setString(++i, obj.getCodigoBanco());
                    sqlInserir.setString(++i, obj.getLoteServico());
                    sqlInserir.setInt(++i, obj.getTipoRegistro().intValue());
                    sqlInserir.setInt(++i, obj.getQuantidadeRegistroLote().intValue());
                    sqlInserir.setDouble(++i, obj.getSomatoraRegistroLote().intValue());
                    sqlInserir.setString(++i, obj.getNumeroAvisoDebito());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroHeaderPagarVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroHeaderPagarVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final RegistroTrailerLotePagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE RegistroTrailerLotePagar set registroHeaderLotePagar=?, codigoBanco=?, loteServico=?, tipoRegistro=?, quantidadeRegistroLote=?, somatoraRegistroLote=?, numeroAvisoDebito=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    
                    sqlAlterar.setInt(++i, obj.getRegistroHeaderLotePagarVO().getCodigo().intValue());
                    sqlAlterar.setString(++i, obj.getCodigoBanco());
                    sqlAlterar.setString(++i, obj.getLoteServico());
                    sqlAlterar.setInt(++i, obj.getTipoRegistro().intValue());
                    sqlAlterar.setInt(++i, obj.getQuantidadeRegistroLote().intValue());
                    sqlAlterar.setDouble(++i, obj.getSomatoraRegistroLote().intValue());
                    sqlAlterar.setString(++i, obj.getNumeroAvisoDebito());
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroHeaderPagarVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroHeaderPagarVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroTrailerLotePagarVO obj, UsuarioVO usuario) throws Exception {
        try {
        	RegistroTrailerLotePagar.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroTrailerLotePagar WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroHeaderPagar</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroHeaderPagarVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroTrailerLotePagar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    
    @Override
    public RegistroTrailerLotePagarVO consultarPorRegistroHeaderLotePagar(Integer registroHeaderLotePagar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroTrailerLotePagar WHERE registroHeaderLotePagar = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {registroHeaderLotePagar});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado,nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroHeaderPagarVO</code> resultantes da consulta.
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
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>RegistroHeaderPagarVO</code>.
     * @return  O objeto da classe <code>RegistroHeaderPagarVO</code> com os dados devidamente montados.
     */
    public static RegistroTrailerLotePagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
    	RegistroTrailerLotePagarVO obj = new RegistroTrailerLotePagarVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setCodigoBanco((dadosSQL.getString("codigoBanco")));
        obj.setLoteServico((dadosSQL.getString("loteServico")));
        obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
        obj.setQuantidadeRegistroLote(dadosSQL.getInt("quantidadeRegistroLote"));
        obj.setSomatoraRegistroLote(dadosSQL.getDouble("somatoraRegistroLote"));
        obj.setNumeroAvisoDebito(dadosSQL.getString("numeroAvisoDebito"));
        obj.getRegistroHeaderLotePagarVO().setCodigo(dadosSQL.getInt("registroHeaderLotePagar"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroHeaderPagarVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroTrailerLotePagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroTrailerLotePagar WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado,nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroTrailerLotePagar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
    	RegistroTrailerLotePagar.idEntidade = idEntidade;
    }

}
