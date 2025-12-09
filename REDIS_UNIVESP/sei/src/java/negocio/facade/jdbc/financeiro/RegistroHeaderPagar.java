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
import negocio.comuns.financeiro.RegistroHeaderPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroHeaderPagarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroHeaderPagar extends ControleAcesso implements RegistroHeaderPagarInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7334390415745745577L;
	protected static String idEntidade;

    public RegistroHeaderPagar() throws Exception {
        super();
        setIdEntidade("RegistroHeaderPagar");
    }
    
    @Override
   	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
   	public void persistir(RegistroHeaderPagarVO obj, UsuarioVO usuarioVO) throws Exception {
       	if (obj.getCodigo() == 0) {
   			incluir(obj, usuarioVO);
   		} else {
   			alterar(obj,  usuarioVO);
   		}
    }
  

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final RegistroHeaderPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroHeaderPagarVO.validarDados(obj);
            final String sql = "INSERT INTO RegistroHeaderPagar(controleCobrancaPagar, codigoBanco, loteServico, tipoRegistro, tipoInscricaoEmpresa, numeroInscricaoEmpresa, codigoConvenioBanco,numeroAgencia,digitoAgencia,numeroConta,digitoConta,digitoAgenciaConta,nomeEmpresa,nomeBanco,codigoRemessaRetorno,dataGeracaoArquivo,horaGeracaoArquivo,numeroSequencialArquivo,numeroVersaoArquivo,densidadeGravacao,reservadoBanco,reservadoEmpresa,ocorrenciaRetorno ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);        
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                	int i = 0;
                    sqlInserir.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo().intValue());
                    sqlInserir.setString(++i, obj.getCodigoBanco());
                    sqlInserir.setString(++i, obj.getLoteServico());
                    sqlInserir.setInt(++i, obj.getTipoRegistro().intValue());
                    sqlInserir.setInt(++i, obj.getTipoInscricaoEmpresa());
                    sqlInserir.setLong(++i, obj.getNumeroInscricaoEmpresa());
                    sqlInserir.setString(++i, obj.getCodigoConvenioBanco());
                    sqlInserir.setInt(++i, obj.getNumeroAgencia());
                    sqlInserir.setString(++i, obj.getDigitoAgencia());
                    sqlInserir.setString(++i, obj.getNumeroConta());
                    sqlInserir.setString(++i, obj.getDigitoConta());
                    sqlInserir.setInt(++i, obj.getDigitoAgenciaConta());
                    sqlInserir.setString(++i, obj.getNomeEmpresa());
                    sqlInserir.setString(++i, obj.getNomeBanco());
                    sqlInserir.setInt(++i, obj.getCodigoRemessaRetorno());
                    sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataGeracaoArquivo()));
                    sqlInserir.setString(++i, obj.getHoraGeracaoArquivo());
                    sqlInserir.setInt(++i, obj.getNumeroSequencialArquivo());
                    sqlInserir.setInt(++i, obj.getNumeroVersaoArquivo());
                    sqlInserir.setInt(++i, obj.getDensidadeGravacao());
                    sqlInserir.setString(++i, obj.getReservadoBanco());
                    sqlInserir.setString(++i, obj.getReservadoEmpresa());
                    sqlInserir.setString(++i, obj.getOcorrenciaRetorno());
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
    private void alterar(final RegistroHeaderPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroHeaderPagarVO.validarDados(obj);
            final String sql = "UPDATE RegistroHeaderPagar set controlecobrancapagar=?, codigoBanco=?, loteServico=?, tipoRegistro=?, tipoInscricaoEmpresa=?, numeroInscricaoEmpresa=?, codigoConvenioBanco=?,numeroAgencia=?,digitoAgencia=?,numeroConta=?,digitoConta=?,digitoAgenciaConta=?,nomeEmpresa=?,nomeBanco=?,codigoRemessaRetorno=?,dataGeracaoArquivo=?,horaGeracaoArquivo=?,numeroSequencialArquivo=?,numeroVersaoArquivo=?,densidadeGravacao=?,reservadoBanco=?,reservadoEmpresa=?,ocorrenciaRetorno=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlAlterar.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo().intValue());
                    sqlAlterar.setString(++i, obj.getCodigoBanco());
                    sqlAlterar.setString(++i, obj.getLoteServico());
                    sqlAlterar.setInt(++i, obj.getTipoRegistro().intValue());
                    sqlAlterar.setInt(++i, obj.getTipoInscricaoEmpresa());
                    sqlAlterar.setLong(++i, obj.getNumeroInscricaoEmpresa());
                    sqlAlterar.setString(++i, obj.getCodigoConvenioBanco());
                    sqlAlterar.setInt(++i, obj.getNumeroAgencia());
                    sqlAlterar.setString(++i, obj.getDigitoAgencia());
                    sqlAlterar.setString(++i, obj.getNumeroConta());
                    sqlAlterar.setString(++i, obj.getDigitoConta());
                    sqlAlterar.setInt(++i, obj.getDigitoAgenciaConta());
                    sqlAlterar.setString(++i, obj.getNomeEmpresa());
                    sqlAlterar.setString(++i, obj.getNomeBanco());
                    sqlAlterar.setInt(++i, obj.getCodigoRemessaRetorno());
                    sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataGeracaoArquivo()));
                    sqlAlterar.setString(++i, obj.getHoraGeracaoArquivo());
                    sqlAlterar.setInt(++i, obj.getNumeroSequencialArquivo());
                    sqlAlterar.setInt(++i, obj.getNumeroVersaoArquivo());
                    sqlAlterar.setInt(++i, obj.getDensidadeGravacao());
                    sqlAlterar.setString(++i, obj.getReservadoBanco());
                    sqlAlterar.setString(++i, obj.getReservadoEmpresa());
                    sqlAlterar.setString(++i, obj.getOcorrenciaRetorno());
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
    public void excluir(RegistroHeaderPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroHeaderPagar.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroHeaderPagar WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
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
        String sqlStr = "SELECT * FROM RegistroHeaderPagar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    @Override
    public RegistroHeaderPagarVO consultarPorControleCobrancaPagar(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroHeaderPagar WHERE controleCobrancaPagar = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
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
    public static RegistroHeaderPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RegistroHeaderPagarVO obj = new RegistroHeaderPagarVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setCodigoBanco((dadosSQL.getString("codigoBanco")));
        obj.setLoteServico((dadosSQL.getString("loteServico")));
        obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
        obj.setTipoInscricaoEmpresa(new Integer(dadosSQL.getInt("tipoInscricaoEmpresa")));
        obj.setNumeroInscricaoEmpresa(dadosSQL.getLong("numeroInscricaoEmpresa"));
        obj.setCodigoConvenioBanco(dadosSQL.getString("codigoConvenioBanco"));
        obj.setNumeroAgencia(new Integer(dadosSQL.getInt("numeroAgencia")));
        obj.setDigitoAgencia(dadosSQL.getString("digitoAgencia"));
        obj.setNumeroConta(dadosSQL.getString("numeroConta"));
        obj.setDigitoConta(dadosSQL.getString("digitoConta"));
        obj.setDigitoAgenciaConta(new Integer(dadosSQL.getInt("digitoAgenciaConta")));
        obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
        obj.setNomeBanco(dadosSQL.getString("nomeBanco"));
        obj.setCodigoRemessaRetorno(new Integer(dadosSQL.getInt("codigoRemessaRetorno")));
        obj.setDataGeracaoArquivo(dadosSQL.getDate("dataGeracaoArquivo"));
        obj.setHoraGeracaoArquivo(dadosSQL.getString("horaGeracaoArquivo"));
        obj.setNumeroSequencialArquivo(new Integer(dadosSQL.getInt("numeroSequencialArquivo")));
        obj.setNumeroVersaoArquivo(new Integer(dadosSQL.getInt("numeroVersaoArquivo")));
        obj.setDensidadeGravacao(new Integer(dadosSQL.getInt("densidadeGravacao")));
        obj.setReservadoBanco(dadosSQL.getString("reservadoBanco"));
        obj.setReservadoEmpresa(dadosSQL.getString("reservadoEmpresa"));
        obj.setOcorrenciaRetonro(dadosSQL.getString("ocorrenciaRetorno"));
        obj.getControleCobrancaPagarVO().setCodigo(new Integer(dadosSQL.getInt("controlecobrancapagar")));
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
    public RegistroHeaderPagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroHeaderPagar WHERE codigo = ?";
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
        return RegistroHeaderPagar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroHeaderPagar.idEntidade = idEntidade;
    }

}
