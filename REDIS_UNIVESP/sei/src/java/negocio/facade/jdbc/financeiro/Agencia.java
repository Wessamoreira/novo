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
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.AgenciaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AgenciaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>AgenciaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see AgenciaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class Agencia extends ControleAcesso implements AgenciaInterfaceFacade {

    protected static String idEntidade;

    public Agencia() throws Exception {
        super();
        setIdEntidade("Agencia");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>AgenciaVO</code>.
     */
    public AgenciaVO novo() throws Exception {
        Agencia.incluir(getIdEntidade());
        AgenciaVO obj = new AgenciaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AgenciaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>AgenciaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AgenciaVO obj,UsuarioVO usuario) throws Exception {
        try {
            AgenciaVO.validarDados(obj);
            Agencia.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO Agencia(banco, gerente, numeroAgencia, nome, cep, cnpj, complemento, email, endereco, fax, inscestadual, numero, razaosocial, setor, site, telcomercial1, telcomercial2, telcomercial3, cidade, digito  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setString(2, obj.getGerente());
                    sqlInserir.setString(3, obj.getNumeroAgencia());
                    sqlInserir.setString(4, obj.getNome());
                    sqlInserir.setString(5, obj.getCEP());
                    sqlInserir.setString(6, obj.getCNPJ());
                    sqlInserir.setString(7, obj.getComplemento());
                    sqlInserir.setString(8, obj.getEmail());
                    sqlInserir.setString(9, obj.getEndereco());
                    sqlInserir.setString(10, obj.getFax());
                    sqlInserir.setString(11, obj.getInscEstadual());
                    sqlInserir.setString(12, obj.getNumero());
                    sqlInserir.setString(13, obj.getRazaoSocial());
                    sqlInserir.setString(14, obj.getSetor());
                    sqlInserir.setString(15, obj.getSite());
                    sqlInserir.setString(16, obj.getTelComercial1());
                    sqlInserir.setString(17, obj.getTelComercial2());
                    sqlInserir.setString(18, obj.getTelComercial3());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(19, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlInserir.setInt(19, 0);
                    }
                    sqlInserir.setString(20, obj.getDigito());
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
            if (!obj.getCEP().equals("")) {
                getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj,usuario);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AgenciaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>AgenciaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AgenciaVO obj,UsuarioVO usuario) throws Exception {
        try {
            AgenciaVO.validarDados(obj);
            Agencia.alterar(getIdEntidade());
            final String sql = "UPDATE Agencia set numeroAgencia=?, gerente=?, banco=?, nome=?, cep=?, cnpj=?, complemento=?, email=?, endereco=?, fax=?, inscestadual=?, numero=?, razaosocial=?, setor=?, site=?, telcomercial1=?, telcomercial2=?, telcomercial3=?, cidade=?, digito=?   WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNumeroAgencia());
                    sqlAlterar.setString(2, obj.getGerente());
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setString(4, obj.getNome());
                    sqlAlterar.setString(5, obj.getCEP());
                    sqlAlterar.setString(6, obj.getCNPJ());
                    sqlAlterar.setString(7, obj.getComplemento());
                    sqlAlterar.setString(8, obj.getEmail());
                    sqlAlterar.setString(9, obj.getEndereco());
                    sqlAlterar.setString(10, obj.getFax());
                    sqlAlterar.setString(11, obj.getInscEstadual());
                    sqlAlterar.setString(12, obj.getNumero());
                    sqlAlterar.setString(13, obj.getRazaoSocial());
                    sqlAlterar.setString(14, obj.getSetor());
                    sqlAlterar.setString(15, obj.getSite());
                    sqlAlterar.setString(16, obj.getTelComercial1());
                    sqlAlterar.setString(17, obj.getTelComercial2());
                    sqlAlterar.setString(18, obj.getTelComercial3());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(19, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlAlterar.setInt(19, 0);
                    }
                    sqlAlterar.setString(20, obj.getDigito());
                    sqlAlterar.setInt(21, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            if (!obj.getCEP().equals("")) {
                getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj,usuario);
            }
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AgenciaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>AgenciaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AgenciaVO obj,UsuarioVO usuario) throws Exception {
        try {
            Agencia.excluir(getIdEntidade());
            String sql = "DELETE FROM Agencia WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Agencia</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Banco</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeBanco(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT Agencia.* FROM Agencia, Banco WHERE Agencia.banco = Banco.codigo and upper( Banco.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Banco.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Agencia</code> através do valor do atributo 
     * <code>String gerente</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorGerente(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM Agencia WHERE upper( gerente ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY gerente";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Agencia</code> através do valor do atributo 
     * <code>String numeroAgencia</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNumeroAgencia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Agencia WHERE upper( numeroAgencia ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY numeroAgencia";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Agencia</code> através do valor do atributo 
     * <code>String razaoSocial</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorRazaoSocial(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Agencia WHERE upper( razaoSocial ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY razaoSocial";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Agencia</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Agencia WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Agencia</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM Agencia WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @SuppressWarnings("unchecked")
	public List<AgenciaVO> consultarPorBanco(int codigoBanco, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM Agencia WHERE banco = ? ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, codigoBanco);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>AgenciaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>AgenciaVO</code>.
     * @return  O objeto da classe <code>AgenciaVO</code> com os dados devidamente montados.
     */
    public static AgenciaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        AgenciaVO obj = new AgenciaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNumeroAgencia(dadosSQL.getString("numeroAgencia"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setDigito(dadosSQL.getString("digito"));
        obj.setGerente(dadosSQL.getString("gerente"));
        obj.setCEP(dadosSQL.getString("cep"));
        obj.setCNPJ(dadosSQL.getString("cnpj"));
        obj.setComplemento(dadosSQL.getString("complemento"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.setEndereco(dadosSQL.getString("endereco"));
        obj.setFax(dadosSQL.getString("fax"));
        obj.setInscEstadual(dadosSQL.getString("inscestadual"));
        obj.setNome(dadosSQL.getString("Nome"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setRazaoSocial(dadosSQL.getString("razaosocial"));
        obj.setSetor(dadosSQL.getString("setor"));
        obj.setSite(dadosSQL.getString("site"));
        obj.setTelComercial1(dadosSQL.getString("telcomercial1"));
        obj.setTelComercial2(dadosSQL.getString("telcomercial2"));
        obj.setTelComercial3(dadosSQL.getString("telcomercial3"));
        obj.getBanco().setCodigo(new Integer(dadosSQL.getInt("banco")));
        obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
            montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS ,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>BancoVO</code> relacionado ao objeto <code>AgenciaVO</code>.
     * Faz uso da chave primária da classe <code>BancoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosBanco(AgenciaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getBanco().getCodigo().intValue() == 0) {
            obj.setBanco(new BancoVO());
            return;
        }
        obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosCidade(AgenciaVO obj, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AgenciaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public AgenciaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), false, usuario);
    	String sql = "SELECT * FROM Agencia WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Agencia ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Agencia.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Agencia.idEntidade = idEntidade;
    }
}
