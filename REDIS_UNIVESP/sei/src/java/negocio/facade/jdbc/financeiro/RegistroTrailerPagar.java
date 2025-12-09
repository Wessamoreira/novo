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
import negocio.comuns.financeiro.RegistroTrailerPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroTrailerPagarInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy 
public class RegistroTrailerPagar extends ControleAcesso implements RegistroTrailerPagarInterfaceFacade{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8922145812900430307L;
	protected static String idEntidade;

    public RegistroTrailerPagar() throws Exception {
        super();
        setIdEntidade("RegistroTrailerPagar");
    }  
    
    @Override
   	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
   	public void persistir(RegistroTrailerPagarVO obj, UsuarioVO usuarioVO) throws Exception {
       	if (obj.getCodigo() == 0) {
   			incluir(obj, usuarioVO);
   		} else {
   			alterar(obj,  usuarioVO);
   		}
    }
    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroTrailerPagarVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RegistroTrailerPagarVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroTrailerPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroTrailerPagarVO.validarDados(obj);
            final String sql = "INSERT INTO RegistroTrailerPagar( controlecobrancapagar, codigoBanco, loteServico, tipoRegistro, quantidadeLote, quantidadeRegistro) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlInserir.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo());
                    sqlInserir.setString(++i, obj.getCodigoBanco());
                    sqlInserir.setString(++i, obj.getLoteServico());
                    sqlInserir.setInt(++i, obj.getTipoRegistro().intValue());
                    sqlInserir.setInt(++i, obj.getQuantidadeLote().intValue());
                    sqlInserir.setInt(++i, obj.getQuantidadeRegistro().intValue());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroTrailerPagarVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroTrailerPagarVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroTrailerPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroTrailerPagarVO.validarDados(obj);
            final String sql = "UPDATE RegistroTrailerPagar set controlecobrancapagar=?, codigoBanco=?, loteServico=?, tipoRegistro=?, quantidadeLote=?, quantidadeRegistro=?  WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i=0;
                    sqlAlterar.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo());
                    sqlAlterar.setString(++i, obj.getCodigoBanco());
                    sqlAlterar.setString(++i, obj.getLoteServico());
                    sqlAlterar.setInt(++i, obj.getTipoRegistro().intValue());
                    sqlAlterar.setInt(++i, obj.getQuantidadeLote().intValue());
                    sqlAlterar.setInt(++i, obj.getQuantidadeRegistro().intValue());
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroTrailerPagarVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroTrailerPagarVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroTrailerPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroTrailer.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroTrailerPagar WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroTrailer</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroTrailerPagarVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroTrailerPagar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados));
    }
    
    @Override
    public RegistroTrailerPagarVO consultarPorControleCobrancaPagar(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroTrailerPagar WHERE controleCobrancaPagar = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado,nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroTrailerPagarVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>RegistroTrailerPagarVO</code>.
     * @return  O objeto da classe <code>RegistroTrailerPagarVO</code> com os dados devidamente montados.
     */
    public static RegistroTrailerPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RegistroTrailerPagarVO obj = new RegistroTrailerPagarVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));        
        obj.setCodigoBanco((dadosSQL.getString("codigoBanco")));
        obj.setLoteServico((dadosSQL.getString("loteServico")));
        obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
        obj.setQuantidadeLote(new Integer(dadosSQL.getInt("quantidadeLote")));
        obj.setQuantidadeRegistro(new Integer(dadosSQL.getInt("quantidadeRegistro")));
        obj.getControleCobrancaPagarVO().setCodigo(new Integer(dadosSQL.getInt("controlecobrancapagar")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroTrailerPagarVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroTrailerPagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroTrailer WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroTrailer ).");
        }
        return (montarDados(tabelaResultado,nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroTrailer.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroTrailer.idEntidade = idEntidade;
    }

}
