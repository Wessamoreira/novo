package negocio.facade.jdbc.administrativo;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.PessoaPreInscricaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.interfaces.administrativo.PessoaPreInscricaoCursoInterfaceFacade;
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
import java.util.Iterator;
import java.util.List;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PessoaPreInscricaoCursoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PessoaPreInscricaoCursoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PessoaPreInscricaoCursoVO
 * @see ControleAcesso
 * @see Pessoa
 */
@Repository
@Scope("singleton")
@Lazy 
public class PessoaPreInscricaoCurso extends ControleAcesso implements PessoaPreInscricaoCursoInterfaceFacade{

    protected static String idEntidade;

    public PessoaPreInscricaoCurso() throws Exception {
        super();
        setIdEntidade("Pessoa");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PessoaPreInscricaoCursoVO</code>.
     */
    public PessoaPreInscricaoCursoVO novo() throws Exception {
        PessoaPreInscricaoCurso.incluir(getIdEntidade());
        PessoaPreInscricaoCursoVO obj = new PessoaPreInscricaoCursoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PessoaPreInscricaoCursoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PessoaPreInscricaoCursoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(PessoaPreInscricaoCursoVO obj) throws Exception {
        incluir(obj, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluir(final PessoaPreInscricaoCursoVO obj, boolean verificarAcesso) throws Exception {
        PessoaPreInscricaoCursoVO.validarDados(obj);
        final String sql = "INSERT INTO PessoaPreInscricaoCurso( pessoa, curso ) VALUES (?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getPessoa().intValue());
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getCurso().getCodigo());
                } else {
                    sqlInserir.setNull(2, 0);
                }
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

    public void alterar(PessoaPreInscricaoCursoVO obj) throws Exception {
        alterar(obj, true);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PessoaPreInscricaoCursoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PessoaPreInscricaoCursoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterar(final PessoaPreInscricaoCursoVO obj, boolean verificarAcesso) throws Exception {
        PessoaPreInscricaoCursoVO.validarDados(obj);
        final String sql = "UPDATE PessoaPreInscricaoCurso set pessoa=?, curso=? WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getPessoa().intValue());
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getCurso().getCodigo());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PessoaPreInscricaoCursoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PessoaPreInscricaoCursoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(PessoaPreInscricaoCursoVO obj) throws Exception {
        PessoaPreInscricaoCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM PessoaPreInscricaoCurso WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(sql,new Object[] {obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>PessoaPreInscricaoCurso</code> através do valor do atributo
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PessoaPreInscricaoCursoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public PessoaPreInscricaoCursoVO consultarFuncionarioResponsavel(Integer codCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT  pessoa from pessoapreinscricaocurso  ");
        sqlStr.append(" left join preinscricao on preinscricao.responsavel = pessoapreinscricaocurso.pessoa and pessoapreinscricaocurso.curso = preinscricao.curso ");
        sqlStr.append(" where  pessoapreinscricaocurso.curso = ").append(codCurso);
        sqlStr.append(" group by pessoa order by count (preinscricao.codigo) limit 1 ");        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new PessoaPreInscricaoCursoVO();
        }
        return (montarDados(tabelaResultado, usuario));
    }

    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PessoaPreInscricaoCurso.* FROM PessoaPreInscricaoCurso, Pessoa WHERE PessoaPreInscricaoCurso.pessoa = Pessoa.codigo and Pessoa.nome ilike('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PessoaPreInscricaoCurso.* FROM PessoaPreInscricaoCurso, Pessoa WHERE PessoaPreInscricaoCurso.pessoa = Pessoa.codigo and Pessoa.codigo = " + valorConsulta + " ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PessoaPreInscricaoCurso</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PessoaPreInscricaoCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PessoaPreInscricaoCurso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PessoaPreInscricaoCursoVO</code> resultantes da consulta.
     */
    public List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PessoaPreInscricaoCursoVO</code>.
     * @return  O objeto da classe <code>PessoaPreInscricaoCursoVO</code> com os dados devidamente montados.
     */
    public static PessoaPreInscricaoCursoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        PessoaPreInscricaoCursoVO obj = new PessoaPreInscricaoCursoVO();
        //obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setPessoa(dadosSQL.getInt("pessoa"));
        //obj.getCurso().setCodigo(dadosSQL.getInt("codigo"));
        obj.setNovoObj(false);
        //montarDadosCurso(obj, usuario);
        return obj;
    }

    public void montarDadosCurso(PessoaPreInscricaoCursoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>PessoaPreInscricaoCursoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>PessoaPreInscricaoCurso</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirPessoaPreInscricaoCursos(Integer pessoa, List objetos) throws Exception {
        excluirPessoaPreInscricaoCursos(pessoa, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirPessoaPreInscricaoCursos(Integer pessoa, List objetos, boolean verificarAcesso) throws Exception {
        String sql = "DELETE FROM PessoaPreInscricaoCurso WHERE (pessoa = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            PessoaPreInscricaoCursoVO obj = (PessoaPreInscricaoCursoVO) i.next();
            sql += " and codigo != " + obj.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql, new Object[] {pessoa.intValue()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PessoaPreInscricaoCursoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPessoaPreInscricaoCursos</code> e <code>incluirPessoaPreInscricaoCursos</code> disponíveis na classe <code>PessoaPreInscricaoCurso</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarPessoaPreInscricaoCursos(Integer pessoa, List objetos) throws Exception {
        incluirPessoaPreInscricaoCursos(pessoa, objetos, true);
        excluirPessoaPreInscricaoCursos(pessoa, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarPessoaPreInscricaoCursos(Integer pessoa, List objetos, boolean verificarAcesso) throws Exception {
		if (!objetos.isEmpty()) {
			incluirPessoaPreInscricaoCursos(pessoa, objetos, verificarAcesso);
			excluirPessoaPreInscricaoCursos(pessoa, objetos, verificarAcesso);
		}
    }

    /**
     * Operação responsável por incluir objetos da <code>PessoaPreInscricaoCursoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirPessoaPreInscricaoCursos(Integer pessoaPrm, List objetos) throws Exception {
        incluirPessoaPreInscricaoCursos(pessoaPrm, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirPessoaPreInscricaoCursos(Integer pessoaPrm, List objetos, boolean verificarAcesso) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            PessoaPreInscricaoCursoVO obj = (PessoaPreInscricaoCursoVO) e.next();
            obj.setPessoa(pessoaPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, verificarAcesso);
            } else {
                alterar(obj, verificarAcesso);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>PessoaPreInscricaoCursoVO</code> relacionados a um objeto da classe <code>basico.Pessoa</code>.
     * @param pessoa  Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe <code>PessoaPreInscricaoCursoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PessoaPreInscricaoCursoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarPessoaPreInscricaoCursos(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM PessoaPreInscricaoCurso WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {pessoa.intValue()});
        while (resultado.next()) {
            PessoaPreInscricaoCursoVO novoObj = new PessoaPreInscricaoCursoVO();
            novoObj = PessoaPreInscricaoCurso.montarDados(resultado, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PessoaPreInscricaoCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PessoaPreInscricaoCurso.idEntidade = idEntidade;
    }
    
    /**
     * Operação responsável por localizar um objeto da classe <code>PessoaPreInscricaoCursoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PessoaPreInscricaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PessoaPreInscricaoCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }
}
