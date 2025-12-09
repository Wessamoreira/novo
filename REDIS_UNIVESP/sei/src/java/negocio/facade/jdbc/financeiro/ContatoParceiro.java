package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import negocio.comuns.financeiro.ContatoParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContatoParceiroInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContatoParceiroVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContatoParceiroVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContatoParceiroVO
 * @see ControleAcesso
 * @see Parceiro
 */
@Repository
@Scope("singleton")
@Lazy
public class ContatoParceiro extends ControleAcesso implements ContatoParceiroInterfaceFacade {

    protected static String idEntidade;

    public ContatoParceiro() throws Exception {
        super();
        setIdEntidade("Parceiro");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContatoParceiroVO</code>.
     */
    public ContatoParceiroVO novo() throws Exception {
        ContatoParceiro.incluir(getIdEntidade());
        ContatoParceiroVO obj = new ContatoParceiroVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContatoParceiroVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContatoParceiroVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContatoParceiroVO obj) throws Exception {
        ContatoParceiroVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle
         * Comentado 23/10/2014
         */
		// ContatoParceiro.incluir(getIdEntidade());
        final String sql = "INSERT INTO ContatoParceiro( nome, cargo, telefone, dataNascimento, ramal, celular, email, parceiro, responsavelLegal, cpf, rg, endereco, setor, numero, complemento, cidade, CEP ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                sqlInserir.setString(1, obj.getNome());
                sqlInserir.setString(2, obj.getCargo());
                sqlInserir.setString(3, obj.getTelefone());
                sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataNascimento()));
                sqlInserir.setString(5, obj.getRamal());
                sqlInserir.setString(6, obj.getCelular());
                sqlInserir.setString(7, obj.getEmail());
                if (obj.getParceiro().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(8, obj.getParceiro().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(8, 0);
                }
                
                sqlInserir.setBoolean(9, obj.getResponsavelLegal());
                sqlInserir.setString(10, obj.getCpf());
                sqlInserir.setString(11, obj.getRg());
                sqlInserir.setString(12, obj.getEndereco());
                sqlInserir.setString(13, obj.getSetor());
                sqlInserir.setString(14, obj.getNumero());
                sqlInserir.setString(15, obj.getComplemento());
                if (obj.getCidade().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(16, obj.getCidade().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(16, 0);
                }
                sqlInserir.setString(17, obj.getCep());
				
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContatoParceiroVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContatoParceiroVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContatoParceiroVO obj) throws Exception {
        ContatoParceiroVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle
         * Comentado 23/10/2014
         */
        // ContatoParceiro.alterar(getIdEntidade());
        final String sql = "UPDATE ContatoParceiro set nome=?, cargo=?, telefone=?, dataNascimento=?, ramal=?, celular=?, email=?, parceiro=?, responsavelLegal=?, cpf=?, rg=?, endereco=?, setor=?, numero=?, complemento=?, cidade=?, CEP=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getNome());
                sqlAlterar.setString(2, obj.getCargo());
                sqlAlterar.setString(3, obj.getTelefone());
                sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataNascimento()));
                sqlAlterar.setString(5, obj.getRamal());
                sqlAlterar.setString(6, obj.getCelular());
                sqlAlterar.setString(7, obj.getEmail());
                if (obj.getParceiro().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(8, obj.getParceiro().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(8, 0);
                }
                
                sqlAlterar.setBoolean(9, obj.getResponsavelLegal());
                sqlAlterar.setString(10, obj.getCpf());
                sqlAlterar.setString(11, obj.getRg());
                sqlAlterar.setString(12, obj.getEndereco());
                sqlAlterar.setString(13, obj.getSetor());
                sqlAlterar.setString(14, obj.getNumero());
                sqlAlterar.setString(15, obj.getComplemento());
                if (obj.getCidade().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(16, obj.getCidade().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(16, 0);
                }
                sqlAlterar.setString(17, obj.getCep());
                
                sqlAlterar.setInt(18, obj.getCodigo().intValue());

                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContatoParceiroVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContatoParceiroVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContatoParceiroVO obj) throws Exception {
    	/**
         * @author Leonardo Riciolle
         * Comentado 23/10/2014
         */
        //  ContatoParceiro.excluir(getIdEntidade());
        String sql = "DELETE FROM ContatoParceiro WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ContatoParceiro</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Parceiro</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContatoParceiroVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeParceiro(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ContatoParceiro.* FROM ContatoParceiro, Parceiro WHERE ContatoParceiro.parceiro = Parceiro.codigo and upper( Parceiro.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Parceiro.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContatoParceiro</code> através do valor do atributo 
     * <code>String cargo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContatoParceiroVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContatoParceiro WHERE upper( cargo ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY cargo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List consultarAniversariantesPorMes(Integer valorConsulta, Integer parceiro, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT ContatoParceiro.*, parceiro.nome As nomeParceiro FROM ContatoParceiro INNER JOIN parceiro ON parceiro.codigo = contatoparceiro.parceiro WHERE extract(MONTH FROM dataNascimento) = " + valorConsulta;
        if (parceiro != 0) {
            sqlStr += " AND parceiro.codigo = " + parceiro;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosContatosAniversariantes(tabelaResultado);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContatoParceiro</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContatoParceiroVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContatoParceiro WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContatoParceiro</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContatoParceiroVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContatoParceiro WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContatoParceiroVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ContatoParceiroVO</code>.
     * @return  O objeto da classe <code>ContatoParceiroVO</code> com os dados devidamente montados.
     */
    public static ContatoParceiroVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ContatoParceiroVO obj = new ContatoParceiroVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setCargo(dadosSQL.getString("cargo"));
        obj.setDataNascimento(dadosSQL.getDate("dataNascimento"));
        obj.setTelefone(dadosSQL.getString("telefone"));
        obj.setRamal(dadosSQL.getString("ramal"));
        obj.setCelular(dadosSQL.getString("celular"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
        
        obj.setResponsavelLegal(dadosSQL.getBoolean("responsavelLegal"));
        obj.setCpf(dadosSQL.getString("cpf"));
        obj.setRg(dadosSQL.getString("rg"));
        obj.setEndereco(dadosSQL.getString("endereco"));
        obj.setSetor(dadosSQL.getString("setor"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setComplemento(dadosSQL.getString("complemento"));
        obj.setCep(dadosSQL.getString("cep"));
        
        if (dadosSQL.getInt("cidade") != 0) { 
            try {
                obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(dadosSQL.getInt("cidade"), false, null));
            } catch (Exception e) {
                obj.setCidade(null);
            }
        }
		
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    public static List montarDadosContatosAniversariantes(SqlRowSet dadosSQL) throws Exception {
        List listaObjs = new ArrayList(0);
        while (dadosSQL.next()) {
            ContatoParceiroVO obj = new ContatoParceiroVO();
            obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
            obj.setNome(dadosSQL.getString("nome"));
            obj.setCargo(dadosSQL.getString("cargo"));
            obj.setDataNascimento(dadosSQL.getDate("dataNascimento"));
            obj.setTelefone(dadosSQL.getString("telefone"));
            obj.setCelular(dadosSQL.getString("celular"));
            obj.setEmail(dadosSQL.getString("email"));
            obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
            obj.getParceiro().setNome(dadosSQL.getString("nomeParceiro"));
            listaObjs.add(obj);
        }
        return listaObjs;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ContatoParceiroVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContatoParceiro</code>.
     * @param <code>parceiro</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContatoParceiros(Integer parceiro) throws Exception {
        String sql = "DELETE FROM ContatoParceiro WHERE (parceiro = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{parceiro});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContatoParceiroVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContatoParceiros</code> e <code>incluirContatoParceiros</code> disponíveis na classe <code>ContatoParceiro</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarContatoParceiros(Integer parceiro, List objetos) throws Exception {
        excluirContatoParceiros(parceiro);
        incluirContatoParceiros(parceiro, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>ContatoParceiroVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.Parceiro</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirContatoParceiros(Integer parceiroPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ContatoParceiroVO obj = (ContatoParceiroVO) e.next();
            obj.getParceiro().setCodigo(parceiroPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ContatoParceiroVO</code> relacionados a um objeto da classe <code>financeiro.Parceiro</code>.
     * @param parceiro  Atributo de <code>financeiro.Parceiro</code> a ser utilizado para localizar os objetos da classe <code>ContatoParceiroVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ContatoParceiroVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarContatoParceiros(Integer parceiro, int nivelMontarDados) throws Exception {
        ContatoParceiro.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ContatoParceiro WHERE parceiro = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{parceiro});
        while (resultado.next()) {
            ContatoParceiroVO novoObj = new ContatoParceiroVO();
            novoObj = ContatoParceiro.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContatoParceiroVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContatoParceiroVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContatoParceiro WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContatoParceiro ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContatoParceiro.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContatoParceiro.idEntidade = idEntidade;
    }
}
