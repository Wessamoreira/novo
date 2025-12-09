/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.basico;

/**
 *
 * @author Rogerio
 */
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
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.DadosComerciaisInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DadosComerciaisVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>DadosComerciaisVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see DadosComerciaisVO
 * @see ControleAcesso
 * @see Pessoa
 */
@Repository
@Scope("singleton")
@Lazy
public class DadosComerciais extends ControleAcesso implements DadosComerciaisInterfaceFacade {

    protected static String idEntidade;

    public DadosComerciais() throws Exception {
        super();
        setIdEntidade("DadosComerciais");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>DadosComerciaisVO</code>.
     */
    public DadosComerciaisVO novo() throws Exception {
        DadosComerciais.incluir(getIdEntidade());
        DadosComerciaisVO obj = new DadosComerciaisVO();
        return obj;
    }

    public static void validarDados(DadosComerciaisVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNomeEmpresa() == null || obj.getNomeEmpresa().equals("")) {
            throw new ConsistirException("O campo EMPRESA (Dados Comerciais) deve ser informado.");
        }
        if (obj.getCargoPessoaEmpresa() == null || obj.getCargoPessoaEmpresa().equals("")) {
            throw new ConsistirException("O campo CARGO (Dados Comerciais) deve ser informado.");
        }
//        if (obj.getDataAdmissao() == null) {
//            throw new ConsistirException("O campo DATA DE ADMISSÃO (Dados Comerciais) deve ser informado.");
//        }
//        if (obj.getPrincipaisAtividades() == null || obj.getPrincipaisAtividades().equals("")) {
//            throw new ConsistirException("O campo PRINCIPAIS ATIVIDADES (Dados Comerciais) deve ser informado.");
//        }
//        if (!obj.getEmpregoAtual()) {
//            if (obj.getDataDemissao() == null) {
//                throw new ConsistirException("O campo DATA CONCLUSÃO (Dados Comerciais) deve ser informado.");
//            }
//            if (obj.getMotivoDesligamento() == null || obj.getMotivoDesligamento().equals("")) {
//                throw new ConsistirException("O campo MOTIVO DE DESLIGAMENTO (Dados Comerciais) deve ser informado.");
//            }
//        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DadosComerciaisVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>DadosComerciaisVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(DadosComerciaisVO obj) throws Exception {
        incluir(obj, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluir(final DadosComerciaisVO obj, boolean verificarAcesso) throws Exception {
//        validarDados(obj);
        final String sql = "INSERT INTO DadosComerciais( pessoa, nomeEmpresa, enderecoEmpresa, cargoPessoaEmpresa, cepEmpresa, complementoEmpresa , cidadeEmpresa, setorEmpresa , telefoneComer, empregoAtual, principaisAtividades, salario, motivoDesligamento, dataAdmissao, dataDemissao ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
                    sqlInserir.setInt(1, obj.getPessoa().getCodigo());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getNomeEmpresa());
                sqlInserir.setString(3, obj.getEnderecoEmpresa());
                sqlInserir.setString(4, obj.getCargoPessoaEmpresa());
                sqlInserir.setString(5, obj.getCepEmpresa());
                sqlInserir.setString(6, obj.getComplementoEmpresa());
                if (Uteis.isAtributoPreenchido(obj.getCidadeEmpresa().getCodigo())) {
                    sqlInserir.setInt(7, obj.getCidadeEmpresa().getCodigo());
                } else {
                    sqlInserir.setNull(7, 0);
                }
                sqlInserir.setString(8, obj.getSetorEmpresa());
                sqlInserir.setString(9, obj.getTelefoneComer());
                sqlInserir.setBoolean(10, obj.getEmpregoAtual());
                sqlInserir.setString(11, obj.getPrincipaisAtividades());
                sqlInserir.setString(12, obj.getSalario());
                sqlInserir.setString(13, obj.getMotivoDesligamento());
                sqlInserir.setDate(14, Uteis.getDataJDBC(obj.getDataAdmissao()));
                sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataDemissao()));
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

    public void alterar(DadosComerciaisVO obj) throws Exception {
        alterar(obj, true);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DadosComerciaisVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>DadosComerciaisVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterar(final DadosComerciaisVO obj, boolean verificarAcesso) throws Exception {
//        validarDados(obj);
        final String sql = "UPDATE DadosComerciais set pessoa=?, nomeEmpresa=?, enderecoEmpresa=?, cargoPessoaEmpresa=?, cepEmpresa=?, complementoEmpresa=?, cidadeEmpresa=?, setorEmpresa=?, telefoneComer=?, empregoAtual=?, principaisAtividades=?, salario=?, motivoDesligamento=?, dataAdmissao=?, dataDemissao=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
                    sqlAlterar.setInt(1, obj.getPessoa().getCodigo());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getNomeEmpresa());
                sqlAlterar.setString(3, obj.getEnderecoEmpresa());
                sqlAlterar.setString(4, obj.getCargoPessoaEmpresa());
                sqlAlterar.setString(5, obj.getCepEmpresa());
                sqlAlterar.setString(6, obj.getComplementoEmpresa());
                if (Uteis.isAtributoPreenchido(obj.getCidadeEmpresa().getCodigo())) {
                    sqlAlterar.setInt(7, obj.getCidadeEmpresa().getCodigo());
                } else {
                    sqlAlterar.setNull(7, 0);
                }
                sqlAlterar.setString(8, obj.getSetorEmpresa());
                sqlAlterar.setString(9, obj.getTelefoneComer());
                sqlAlterar.setBoolean(10, obj.getEmpregoAtual());
                sqlAlterar.setString(11, obj.getPrincipaisAtividades());
                sqlAlterar.setString(12, obj.getSalario());
                sqlAlterar.setString(13, obj.getMotivoDesligamento());
                sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataAdmissao()));
                sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataDemissao()));

                sqlAlterar.setInt(16, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DadosComerciaisVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>DadosComerciaisVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(DadosComerciaisVO obj) throws Exception {
        DadosComerciais.excluir(getIdEntidade());
        String sql = "DELETE FROM DadosComerciais WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>DadosComerciais</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DadosComerciaisVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DadosComerciais WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorCodigoPessoaOrdemNovaAntiga(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DadosComerciais WHERE pessoa = " + pessoa.intValue() + " ORDER BY dataAdmissao desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>DadosComerciaisVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>DadosComerciaisVO</code>.
     * @return  O objeto da classe <code>DadosComerciaisVO</code> com os dados devidamente montados.
     */
    public static DadosComerciaisVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        DadosComerciaisVO obj = new DadosComerciaisVO();
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
        obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
        obj.setEnderecoEmpresa(dadosSQL.getString("enderecoEmpresa"));
        obj.setCargoPessoaEmpresa(dadosSQL.getString("cargoPessoaEmpresa"));
        obj.setCepEmpresa(dadosSQL.getString("cepEmpresa"));
        obj.setComplementoEmpresa(dadosSQL.getString("complementoEmpresa"));
        obj.getCidadeEmpresa().setCodigo(dadosSQL.getInt("cidadeEmpresa"));
        obj.setSetorEmpresa(dadosSQL.getString("setorEmpresa"));
        obj.setTelefoneComer(dadosSQL.getString("telefoneComer"));
        obj.setEmpregoAtual(dadosSQL.getBoolean("empregoAtual"));
        obj.setPrincipaisAtividades(dadosSQL.getString("principaisAtividades"));
        obj.setSalario(dadosSQL.getString("salario"));
        obj.setMotivoDesligamento(dadosSQL.getString("motivoDesligamento"));
        obj.setDataAdmissao(dadosSQL.getDate("dataAdmissao"));
        obj.setDataDemissao(dadosSQL.getDate("dataDemissao"));

        obj.setNovoObj(false);
        montarDadosCidade(obj, usuario);
        //montarDadosPessoa(obj, usuario);
        return obj;
    }

    public static void montarDadosCidade(DadosComerciaisVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCidadeEmpresa().getCodigo().intValue() == 0) {
            obj.setCidadeEmpresa(new CidadeVO());
            return;
        }
        obj.setCidadeEmpresa(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidadeEmpresa().getCodigo(), false, usuario));
    }

    public static void montarDadosPessoa(DadosComerciaisVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>DadosComerciaisVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>DadosComerciais</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirDadosComerciais(PessoaVO pessoa, List objetos) throws Exception {
        excluirDadosComerciais(pessoa, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirDadosComerciais(PessoaVO pessoa, List objetos, boolean verificarAcesso) throws Exception {
        String sql = "DELETE FROM DadosComerciais WHERE (pessoa = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            DadosComerciaisVO obj = (DadosComerciaisVO) i.next();
            sql += " and codigo != " + obj.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{pessoa.getCodigo()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>DadosComerciaisVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirDadosComerciais</code> e <code>incluirDadosComerciais</code> disponíveis na classe <code>DadosComerciais</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarDadosComerciais(PessoaVO pessoa, List objetos) throws Exception {
        incluirDadosComerciais(pessoa, objetos, true);
        excluirDadosComerciais(pessoa, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarDadosComerciais(PessoaVO pessoa, List objetos, boolean verificarAcesso) throws Exception {
        incluirDadosComerciais(pessoa, objetos, verificarAcesso);
        excluirDadosComerciais(pessoa, objetos, verificarAcesso);
    }

    /**
     * Operação responsável por incluir objetos da <code>DadosComerciaisVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirDadosComerciais(PessoaVO pessoaPrm, List objetos) throws Exception {
        incluirDadosComerciais(pessoaPrm, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirDadosComerciais(PessoaVO pessoaPrm, List objetos, boolean verificarAcesso) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            DadosComerciaisVO obj = (DadosComerciaisVO) e.next();
            obj.setPessoa(pessoaPrm);
            if (obj.getEmpregoAtual()) {
                obj.setMotivoDesligamento("");
                obj.setDataDemissao(null);
            }
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, verificarAcesso);
            } else {
                alterar(obj, verificarAcesso);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>DadosComerciaisVO</code> relacionados a um objeto da classe <code>basico.Pessoa</code>.
     * @param pessoa  Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe <code>DadosComerciaisVO</code>.
     * @return List  Contendo todos os objetos da classe <code>DadosComerciaisVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarDadosComerciais(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        return consultarDadosComerciais(pessoa, controlarAcesso, false, usuario);
    }

    public static List consultarDadosComerciais(Integer pessoa, boolean controlarAcesso, boolean funcionario, UsuarioVO usuario) throws Exception {
        DadosComerciais.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM DadosComerciais WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa.intValue()});
        while (resultado.next()) {
            DadosComerciaisVO novoObj = new DadosComerciaisVO();
            novoObj = DadosComerciais.montarDados(resultado, usuario);
//            novoObj.setFuncionario(funcionario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DadosComerciais.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        DadosComerciais.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DadosComerciaisVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DadosComerciaisVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DadosComerciais WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DadosComerciaisVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DadosComerciaisVO consultarEmpregoAtualPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DadosComerciais WHERE empregoAtual = TRUE AND pessoa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa.intValue()});
        if (!tabelaResultado.next()) {
            sql = "SELECT * FROM DadosComerciais WHERE pessoa = ? order by codigo desc limit 1";
            tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa.intValue()});
            if (!tabelaResultado.next()) {
                return new DadosComerciaisVO();
                //throw new ConsistirException("Dados Não Encontrados.");
            }
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>FadosComerciaisVO</code>
     * ao List <code>fadosComerciaisVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>FadosComerciaisVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjDadosComerciaisVOs(DadosComerciaisVO obj, PessoaVO pessoa) throws Exception {
        validarDados(obj);
        realizarValidacaoEmpregoAtual(obj, pessoa);
        int index = 0;
        Iterator i = pessoa.getDadosComerciaisVOs().iterator();
        while (i.hasNext()) {
            DadosComerciaisVO objExistente = (DadosComerciaisVO) i.next();
            if (objExistente.getNomeEmpresa().equals(obj.getNomeEmpresa())
                    && objExistente.getCargoPessoaEmpresa().equals(obj.getCargoPessoaEmpresa())) {
                pessoa.getDadosComerciaisVOs().set(index, obj);
                return;
            }
            index++;
        }
        pessoa.getDadosComerciaisVOs().add(obj);
        //adicionarObjSubordinadoOC
    }

    public void realizarValidacaoEmpregoAtual(DadosComerciaisVO obj, PessoaVO pessoa) throws Exception {
        Iterator i = pessoa.getDadosComerciaisVOs().iterator();
        while (i.hasNext()) {
            DadosComerciaisVO objExistente = (DadosComerciaisVO) i.next();
            if ((!objExistente.getNomeEmpresa().equals(obj.getNomeEmpresa())
                    && !objExistente.getCargoPessoaEmpresa().equals(obj.getCargoPessoaEmpresa()))
                    || (objExistente.getNomeEmpresa().equals(obj.getNomeEmpresa())
                    && !objExistente.getCargoPessoaEmpresa().equals(obj.getCargoPessoaEmpresa()))) {
                if (objExistente.getEmpregoAtual() && obj.getEmpregoAtual()) {
                    obj.setEmpregoAtual(false);
                    throw new Exception("Já existe um Emprego Atual cadastrado. Desmarque a opção EMPREGO ATUAL e tente adicionar novamente!");
                }
            }
        }
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>FadosComerciaisVO</code>
     * no List <code>fadosComerciaisVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do objeto no List.
     * @param curso  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjDadosComerciaisVOs(DadosComerciaisVO obj, PessoaVO pessoa) throws Exception {
        int index = 0;
        Iterator i = pessoa.getDadosComerciaisVOs().iterator();
        while (i.hasNext()) {
            DadosComerciaisVO objExistente = (DadosComerciaisVO) i.next();
            if (objExistente.getNomeEmpresa().equals(obj.getNomeEmpresa())
                    && objExistente.getCargoPessoaEmpresa().equals(obj.getCargoPessoaEmpresa())) {
                pessoa.getDadosComerciaisVOs().remove(index);
                return;
            }
            index++;
        }
        //excluirObjSubordinadoOC
    }
}
