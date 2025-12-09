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
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroHeaderInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RegistroHeaderVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>RegistroHeaderVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see RegistroHeaderVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class RegistroHeader extends ControleAcesso implements RegistroHeaderInterfaceFacade {

    protected static String idEntidade;

    public RegistroHeader() throws Exception {
        super();
        setIdEntidade("RegistroArquivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>RegistroHeaderVO</code>.
     */
    public RegistroHeaderVO novo() throws Exception {
        RegistroHeader.incluir(getIdEntidade());
        RegistroHeaderVO obj = new RegistroHeaderVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroHeaderVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroHeader.incluir(getIdEntidade());
            RegistroHeaderVO.validarDados(obj);
            final String sql = "INSERT INTO RegistroHeader( registroArquivo, codigoBanco, numeroLote, tipoRegistro, identificacaoRegistro, identirifacaoArquivoRetorno, literalRetorno, codigoServico, literalServico, codigoEmpresa, nomeEmpresaExtenso, tipoInscricaoEmpresa, numeroInscricaoEmpresa, codigoConvenioBanco, numeroAgencia, digitoAgencia, numeroConta, digitoConta, digitoAgenciaConta, nomeEmpresa, nomeBanco, codigoRemessaRetorno, dataGeracaoArquivo, dataCredito, numeroSequencialArquivo, numeroVersaoArquivo, densidadeGravacao, reservadoEmpresa, numeroAvisoBancario, numeroSequencialRegistro, linhaheader ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getRegistroArquivo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getRegistroArquivo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setString(2, obj.getCodigoBanco());
                    sqlInserir.setInt(3, obj.getNumeroLote().intValue());
                    sqlInserir.setInt(4, obj.getTipoRegistro().intValue());
                    sqlInserir.setInt(5, obj.getIdentificacaoRegistro().intValue());
                    sqlInserir.setInt(6, obj.getIdentirifacaoArquivoRetorno().intValue());
                    sqlInserir.setString(7, obj.getLiteralRetorno());
                    sqlInserir.setInt(8, obj.getCodigoServico().intValue());
                    sqlInserir.setInt(9, obj.getLiteralServico().intValue());
                    sqlInserir.setInt(10, obj.getCodigoEmpresa().intValue());
                    sqlInserir.setString(11, obj.getNomeEmpresaExtenso());
                    sqlInserir.setInt(12, obj.getTipoInscricaoEmpresa().intValue());
                    sqlInserir.setLong(13, obj.getNumeroInscricaoEmpresa().longValue());
                    sqlInserir.setString(14, obj.getCodigoConvenioBanco());
                    sqlInserir.setInt(15, obj.getNumeroAgencia().intValue());
                    sqlInserir.setString(16, obj.getDigitoAgencia());
                    sqlInserir.setInt(17, obj.getNumeroConta().intValue());
                    sqlInserir.setString(18, obj.getDigitoConta());
                    sqlInserir.setInt(19, obj.getDigitoAgenciaConta().intValue());
                    sqlInserir.setString(20, obj.getNomeEmpresa());
                    sqlInserir.setString(21, obj.getNomeBanco());
                    sqlInserir.setInt(22, obj.getCodigoRemessaRetorno().intValue());
                    sqlInserir.setDate(23, Uteis.getDataJDBC(obj.getDataGeracaoArquivo()));
                    sqlInserir.setDate(24, Uteis.getDataJDBC(obj.getDataCredito()));
                    sqlInserir.setInt(25, obj.getNumeroSequencialArquivo().intValue());
                    sqlInserir.setInt(26, obj.getNumeroVersaoArquivo().intValue());
                    sqlInserir.setInt(27, obj.getDensidadeGravacao().intValue());
                    sqlInserir.setString(28, obj.getReservadoEmpresa());
                    sqlInserir.setInt(29, obj.getNumeroAvisoBancario().intValue());
                    sqlInserir.setInt(30, obj.getNumeroSequencialRegistro().intValue());
					sqlInserir.setString(31, obj.getLinhaHeader());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroHeaderVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroHeaderVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroHeaderVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroHeader.alterar(getIdEntidade());
            RegistroHeaderVO.validarDados(obj);
            final String sql = "UPDATE RegistroHeader set registroArquivo=?, codigoBanco=?, numeroLote=?, tipoRegistro=?, identificacaoRegistro=?, identirifacaoArquivoRetorno=?, literalRetorno=?, codigoServico=?, literalServico=?, codigoEmpresa=?, nomeEmpresaExtenso=?, tipoInscricaoEmpresa=?, numeroInscricaoEmpresa=?, codigoConvenioBanco=?, numeroAgencia=?, digitoAgencia=?, numeroConta=?, digitoConta=?, digitoAgenciaConta=?, nomeEmpresa=?, nomeBanco=?, codigoRemessaRetorno=?, dataGeracaoArquivo=?, dataCredito=?, numeroSequencialArquivo=?, numeroVersaoArquivo=?, densidadeGravacao=?, reservadoEmpresa=?, numeroAvisoBancario=?, numeroSequencialRegistro=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getRegistroArquivo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getRegistroArquivo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getCodigoBanco());
                    sqlAlterar.setInt(3, obj.getNumeroLote().intValue());
                    sqlAlterar.setInt(4, obj.getTipoRegistro().intValue());
                    sqlAlterar.setInt(5, obj.getIdentificacaoRegistro().intValue());
                    sqlAlterar.setInt(6, obj.getIdentirifacaoArquivoRetorno().intValue());
                    sqlAlterar.setString(7, obj.getLiteralRetorno());
                    sqlAlterar.setInt(8, obj.getCodigoServico().intValue());
                    sqlAlterar.setInt(9, obj.getLiteralServico().intValue());
                    sqlAlterar.setInt(10, obj.getCodigoEmpresa().intValue());
                    sqlAlterar.setString(11, obj.getNomeEmpresaExtenso());
                    sqlAlterar.setInt(12, obj.getTipoInscricaoEmpresa().intValue());
                    sqlAlterar.setLong(13, obj.getNumeroInscricaoEmpresa().longValue());
                    sqlAlterar.setString(14, obj.getCodigoConvenioBanco());
                    sqlAlterar.setInt(15, obj.getNumeroAgencia().intValue());
                    sqlAlterar.setString(16, obj.getDigitoAgencia());
                    sqlAlterar.setInt(17, obj.getNumeroConta().intValue());
                    sqlAlterar.setString(18, obj.getDigitoConta());
                    sqlAlterar.setInt(19, obj.getDigitoAgenciaConta().intValue());
                    sqlAlterar.setString(20, obj.getNomeEmpresa());
                    sqlAlterar.setString(21, obj.getNomeBanco());
                    sqlAlterar.setInt(22, obj.getCodigoRemessaRetorno().intValue());
                    sqlAlterar.setDate(23, Uteis.getDataJDBC(obj.getDataGeracaoArquivo()));
                    sqlAlterar.setDate(24, Uteis.getDataJDBC(obj.getDataCredito()));
                    sqlAlterar.setInt(25, obj.getNumeroSequencialArquivo().intValue());
                    sqlAlterar.setInt(26, obj.getNumeroVersaoArquivo().intValue());
                    sqlAlterar.setInt(27, obj.getDensidadeGravacao().intValue());
                    sqlAlterar.setString(28, obj.getReservadoEmpresa());
                    sqlAlterar.setInt(29, obj.getNumeroAvisoBancario().intValue());
                    sqlAlterar.setInt(30, obj.getNumeroSequencialRegistro().intValue());
                    sqlAlterar.setInt(31, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroHeaderVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroHeaderVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroHeaderVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroHeader.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroHeader WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroHeader</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroHeaderVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroHeader WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroHeaderVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>RegistroHeaderVO</code>.
     * @return  O objeto da classe <code>RegistroHeaderVO</code> com os dados devidamente montados.
     */
    public static RegistroHeaderVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RegistroHeaderVO obj = new RegistroHeaderVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setRegistroArquivo(new Integer(dadosSQL.getInt("registroArquivo")));
        obj.setCodigoBanco((dadosSQL.getString("codigoBanco")));
        obj.setNumeroLote(new Integer(dadosSQL.getInt("numeroLote")));
        obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
        obj.setIdentificacaoRegistro(new Integer(dadosSQL.getInt("identificacaoRegistro")));
        obj.setIdentirifacaoArquivoRetorno(new Integer(dadosSQL.getInt("identirifacaoArquivoRetorno")));
        obj.setLiteralRetorno(dadosSQL.getString("literalRetorno"));
        obj.setCodigoServico(new Integer(dadosSQL.getInt("codigoServico")));
        obj.setLiteralServico(new Integer(dadosSQL.getInt("literalServico")));
        obj.setCodigoEmpresa(new Integer(dadosSQL.getInt("codigoEmpresa")));
        obj.setNomeEmpresaExtenso(dadosSQL.getString("nomeEmpresaExtenso"));
        obj.setTipoInscricaoEmpresa(new Integer(dadosSQL.getInt("tipoInscricaoEmpresa")));
        obj.setNumeroInscricaoEmpresa(dadosSQL.getLong("numeroInscricaoEmpresa"));
        obj.setCodigoConvenioBanco(dadosSQL.getString("codigoConvenioBanco"));
        obj.setNumeroAgencia(new Integer(dadosSQL.getInt("numeroAgencia")));
        obj.setDigitoAgencia(dadosSQL.getString("digitoAgencia"));
        obj.setNumeroConta(new Integer(dadosSQL.getInt("numeroConta")));
        obj.setDigitoConta(dadosSQL.getString("digitoConta"));
        obj.setDigitoAgenciaConta(new Integer(dadosSQL.getInt("digitoAgenciaConta")));
        obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
        obj.setNomeBanco(dadosSQL.getString("nomeBanco"));
        obj.setCodigoRemessaRetorno(new Integer(dadosSQL.getInt("codigoRemessaRetorno")));
        obj.setDataGeracaoArquivo(dadosSQL.getDate("dataGeracaoArquivo"));
        obj.setDataCredito(dadosSQL.getDate("dataCredito"));
        obj.setNumeroSequencialArquivo(new Integer(dadosSQL.getInt("numeroSequencialArquivo")));
        obj.setNumeroVersaoArquivo(new Integer(dadosSQL.getInt("numeroVersaoArquivo")));
        obj.setDensidadeGravacao(new Integer(dadosSQL.getInt("densidadeGravacao")));
        obj.setReservadoEmpresa(dadosSQL.getString("reservadoEmpresa"));
        obj.setNumeroAvisoBancario(new Integer(dadosSQL.getInt("numeroAvisoBancario")));
        obj.setNumeroSequencialRegistro(new Integer(dadosSQL.getInt("numeroSequencialRegistro")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroHeaderVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroHeaderVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroHeader WHERE codigo = ?";
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
        return RegistroHeader.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroHeader.idEntidade = idEntidade;
    }
	
    public Integer verificarHeaderArquivoProcessado(String header, Integer codigoHeader) throws Exception {
    	String sql = "select controlecobranca.codigo from controlecobranca inner join registroarquivo on registroarquivo.codigo = controlecobranca.registroarquivo inner join registroheader on registroarquivo.registroheader = registroheader.codigo WHERE linhaHeader = ?";
    	if(Uteis.isAtributoPreenchido(codigoHeader)){
    		sql += " and registroheader.codigo != "+codigoHeader;
    	}
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {header});
    	if (tabelaResultado.next()) {
    		return tabelaResultado.getInt("codigo");
    	} else {
        	return 0;
    	}
    }	
}
