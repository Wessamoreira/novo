/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
/**
 *
 * @author Rogerio
 */
import negocio.facade.jdbc.basico.Pessoa;
import negocio.interfaces.administrativo.FormacaoExtraCurricularInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FormacaoExtraCurricularVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FormacaoExtraCurricularVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FormacaoExtraCurricularVO
 * @see ControleAcesso
 * @see Pessoa
 */
@Repository
@Scope("singleton")
@Lazy
public class FormacaoExtraCurricular extends ControleAcesso implements FormacaoExtraCurricularInterfaceFacade {

    protected static String idEntidade;

    public FormacaoExtraCurricular() throws Exception {
        super();
        setIdEntidade("FormacaoExtraCurricular");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FormacaoExtraCurricularVO</code>.
     */
    public FormacaoExtraCurricularVO novo() throws Exception {
        FormacaoExtraCurricular.incluir(getIdEntidade());
        FormacaoExtraCurricularVO obj = new FormacaoExtraCurricularVO();
        return obj;
    }

    public static void validarDados(FormacaoExtraCurricularVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getInstituicao() == null || obj.getInstituicao().equals("")) {
            throw new ConsistirException("O campo INSTITUIÇÃO (Formação Extra Curricular) deve ser informado.");
        }
        if (obj.getCurso() == null || obj.getCurso().equals("")) {
            throw new ConsistirException("O campo CURSO (Formação Extra Curricular) deve ser informado.");
        }
//        if (obj.getCargoPessoaEmpresa() == null) {
//            throw new ConsistirException("O campo CARGO (Dados Comerciais) deve ser informado.");
//        }
//        if (obj.getDataAdmissao() == null) {
//            throw new ConsistirException("O campo DATA DE ADMISSÃO (Dados Comerciais) deve ser informado.");
//        }
//        if (!obj.getEmpregoAtual()) {
//            if (obj.getDataDemissao() == null) {
//                throw new ConsistirException("O campo DATA DE DEMISSÃO (Dados Comerciais) deve ser informado.");
//            }
//            if (obj.getMotivoDesligamento() == null || obj.getMotivoDesligamento().equals("")) {
//                throw new ConsistirException("O campo MOTIVO DE DESLIGAMENTO (Dados Comerciais) deve ser informado.");
//            }
//        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FormacaoExtraCurricularVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(FormacaoExtraCurricularVO obj) throws Exception {
        incluir(obj, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluir(final FormacaoExtraCurricularVO obj, boolean verificarAcesso) throws Exception {
//        validarDados(obj);
        final String sql = "INSERT INTO FormacaoExtraCurricular( pessoa, instituicao, curso, anoRealizacaoConclusao, cargaHoraria ) VALUES (?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
                    sqlInserir.setInt(1, obj.getPessoa().getCodigo());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getInstituicao());
                sqlInserir.setString(3, obj.getCurso());
                sqlInserir.setInt(4, obj.getAnoRealizacaoConclusao());
                sqlInserir.setString(5, obj.getCargaHoraria());

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

    public void alterar(FormacaoExtraCurricularVO obj) throws Exception {
        alterar(obj, true);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoExtraCurricularVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterar(final FormacaoExtraCurricularVO obj, boolean verificarAcesso) throws Exception {
//        validarDados(obj);
        final String sql = "UPDATE FormacaoExtraCurricular set pessoa=?, instituicao=?, curso=?, anoRealizacaoConclusao=?, cargaHoraria=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
                    sqlAlterar.setInt(1, obj.getPessoa().getCodigo());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getInstituicao());
                sqlAlterar.setString(3, obj.getCurso());
                sqlAlterar.setInt(4, obj.getAnoRealizacaoConclusao());
                sqlAlterar.setString(5, obj.getCargaHoraria());

                sqlAlterar.setInt(6, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoExtraCurricularVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(FormacaoExtraCurricularVO obj) throws Exception {
        FormacaoExtraCurricular.excluir(getIdEntidade());
        String sql = "DELETE FROM FormacaoExtraCurricular WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>FormacaoExtraCurricular</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormacaoExtraCurricularVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FormacaoExtraCurricular WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorCodigoPessoaOrdemNovaAntiga(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FormacaoExtraCurricular WHERE pessoa = " + pessoa.intValue() + " ORDER BY anoRealizacaoConclusao desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FormacaoExtraCurricularVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * @return  O objeto da classe <code>FormacaoExtraCurricularVO</code> com os dados devidamente montados.
     */
    public static FormacaoExtraCurricularVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        FormacaoExtraCurricularVO obj = new FormacaoExtraCurricularVO();
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
        obj.setInstituicao(dadosSQL.getString("instituicao"));
        obj.setCurso(dadosSQL.getString("curso"));
        obj.setAnoRealizacaoConclusao(dadosSQL.getInt("anoRealizacaoConclusao"));
        obj.setCargaHoraria(dadosSQL.getString("cargaHoraria"));

        obj.setNovoObj(false);
//        montarDadosPessoa(obj, usuario);
        return obj;
    }

    public  void montarDadosPessoa(FormacaoExtraCurricularVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>FormacaoExtraCurricularVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FormacaoExtraCurricular</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirFormacaoExtraCurricular(PessoaVO pessoa, List objetos) throws Exception {
        excluirFormacaoExtraCurricular(pessoa, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirFormacaoExtraCurricular(PessoaVO pessoa, List objetos, boolean verificarAcesso) throws Exception {
        String sql = "DELETE FROM FormacaoExtraCurricular WHERE (pessoa = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            FormacaoExtraCurricularVO obj = (FormacaoExtraCurricularVO) i.next();
            sql += " and codigo != " + obj.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{pessoa.getCodigo()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>FormacaoExtraCurricularVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFormacaoExtraCurricular</code> e <code>incluirFormacaoExtraCurricular</code> disponíveis na classe <code>FormacaoExtraCurricular</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarFormacaoExtraCurricular(PessoaVO pessoa, List objetos) throws Exception {
        incluirFormacaoExtraCurricular(pessoa, objetos, true);
        excluirFormacaoExtraCurricular(pessoa, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarFormacaoExtraCurricular(PessoaVO pessoa, List objetos, boolean verificarAcesso) throws Exception {
        incluirFormacaoExtraCurricular(pessoa, objetos, verificarAcesso);
        excluirFormacaoExtraCurricular(pessoa, objetos, verificarAcesso);
    }

    /**
     * Operação responsável por incluir objetos da <code>FormacaoExtraCurricularVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirFormacaoExtraCurricular(PessoaVO pessoaPrm, List objetos) throws Exception {
        incluirFormacaoExtraCurricular(pessoaPrm, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirFormacaoExtraCurricular(PessoaVO pessoaPrm, List objetos, boolean verificarAcesso) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            FormacaoExtraCurricularVO obj = (FormacaoExtraCurricularVO) e.next();
            obj.setPessoa(pessoaPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, verificarAcesso);
            } else {
                alterar(obj, verificarAcesso);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>FormacaoExtraCurricularVO</code> relacionados a um objeto da classe <code>basico.Pessoa</code>.
     * @param pessoa  Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe <code>FormacaoExtraCurricularVO</code>.
     * @return List  Contendo todos os objetos da classe <code>FormacaoExtraCurricularVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public  List consultarFormacaoExtraCurricular(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        return consultarFormacaoExtraCurricular(pessoa, controlarAcesso, false, usuario);
    }

    public  List consultarFormacaoExtraCurricular(Integer pessoa, boolean controlarAcesso, boolean funcionario, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM FormacaoExtraCurricular WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa.intValue()});
        while (resultado.next()) {
            FormacaoExtraCurricularVO novoObj = new FormacaoExtraCurricularVO();
            novoObj = FormacaoExtraCurricular.montarDados(resultado, usuario);
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
        return FormacaoExtraCurricular.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        FormacaoExtraCurricular.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FormacaoExtraCurricularVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormacaoExtraCurricularVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FormacaoExtraCurricular WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FormacaoExtraCurricularVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormacaoExtraCurricularVO consultarEmpregoAtualPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FormacaoExtraCurricular WHERE empregoAtual = TRUE AND pessoa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>FadosComerciaisVO</code>
     * ao List <code>fadosComerciaisVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>FadosComerciaisVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjFormacaoExtraCurricularVOs(FormacaoExtraCurricularVO obj, PessoaVO pessoa) throws Exception {
        validarDados(obj);
        int index = 0;
        Iterator i = pessoa.getFormacaoExtraCurricularVOs().iterator();
        while (i.hasNext()) {
            FormacaoExtraCurricularVO objExistente = (FormacaoExtraCurricularVO) i.next();
            if (objExistente.getCurso().equals(obj.getCurso())) {
                pessoa.getFormacaoExtraCurricularVOs().set(index, obj);
                return;
            }
            index++;
        }
        pessoa.getFormacaoExtraCurricularVOs().add(obj);
        //adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>FadosComerciaisVO</code>
     * no List <code>fadosComerciaisVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do objeto no List.
     * @param curso  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjFormacaoExtraCurricularVOs(FormacaoExtraCurricularVO obj, PessoaVO pessoa) throws Exception {
        int index = 0;
        Iterator i = pessoa.getFormacaoExtraCurricularVOs().iterator();
        while (i.hasNext()) {
            FormacaoExtraCurricularVO objExistente = (FormacaoExtraCurricularVO) i.next();
            if (objExistente.getCurso().equals(obj.getCurso())) {
                pessoa.getFormacaoExtraCurricularVOs().remove(index);
                return;
            }
            index++;
        }
        //excluirObjSubordinadoOC
    }
}
