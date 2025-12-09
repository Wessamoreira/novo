/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.AreaProfissionalInteresseContratacaoInterfaceFacade;

/**
 *
 * @author Rogerio
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class AreaProfissionalInteresseContratacao extends ControleAcesso implements AreaProfissionalInteresseContratacaoInterfaceFacade {

    protected static String idEntidade;

    public AreaProfissionalInteresseContratacao() throws Exception {
        super();
        setIdEntidade("AreaProfissionalInteresseContratacao");
    }

    public AreaProfissionalInteresseContratacaoVO novo() throws Exception {
        AreaProfissionalInteresseContratacao.incluir(getIdEntidade());
        AreaProfissionalInteresseContratacaoVO obj = new AreaProfissionalInteresseContratacaoVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception {
        final String sql = "INSERT INTO AreaProfissionalInteresseContratacao( areaProfissional, pessoa ) VALUES ( ?, ? )";

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getAreaProfissional().getCodigo().intValue());
                sqlInserir.setInt(2, obj.getPessoa().getCodigo().intValue());
                return sqlInserir;
            }
        });

        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE AreaProfissionalInteresseContratacao set areaProfissional = ?, pessoa = ? WHERE (codigo = ?)";

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getAreaProfissional().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getPessoa().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception {
        AreaProfissionalInteresseContratacao.excluir(getIdEntidade());
        String sql = "DELETE FROM AreaProfissionalInteresseContratacao WHERE ((areaProfissional = ?) and (pessoa = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getAreaProfissional(), obj.getPessoa().getCodigo()});
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>AreaProfissionalInteresseContratacaoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code>.
     *
     * @return O objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code> com os dados devidamente montados.
     */
    public static AreaProfissionalInteresseContratacaoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        AreaProfissionalInteresseContratacaoVO obj = new AreaProfissionalInteresseContratacaoVO();
        obj.getAreaProfissional().setCodigo(new Integer(dadosSQL.getInt("areaProfissional")));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosAreaProfissional(obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>AreaProfissionalInteresseContratacaoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar
     * a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosAreaProfissional(AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setAreaProfissional(getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(obj.getAreaProfissional().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirAreaProfissionalInteresseContratacao(Integer areaProfissional, UsuarioVO usuario) throws Exception {
        excluirAreaProfissionalInteresseContratacao(areaProfissional, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirAreaProfissionalInteresseContratacao(Integer areaProfissional, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        AreaProfissionalInteresseContratacao.excluir(getIdEntidade(), verificarAcesso, usuario);
        String sql = "DELETE FROM AreaProfissionalInteresseContratacao WHERE (areaProfissional = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{areaProfissional});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirAreaProfissionalInteresseContratacao(Integer pessoa, List objetos, boolean verificarAcesso) throws Exception {
        String sql = "DELETE FROM AreaProfissionalInteresseContratacao WHERE (pessoa = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            AreaProfissionalInteresseContratacaoVO obj = (AreaProfissionalInteresseContratacaoVO) i.next();
            sql += " and codigo != " + obj.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{pessoa.intValue()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarAreaProfissionalInteresseContratacao(Integer areaProfissional, List objetos, UsuarioVO usuario) throws Exception {
        alterarAreaProfissionalInteresseContratacao(areaProfissional, objetos, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarAreaProfissionalInteresseContratacao(Integer pessoa, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        excluirAreaProfissionalInteresseContratacao(pessoa, objetos, verificarAcesso);
        incluirAreaProfissionalInteresseContratacao(pessoa, objetos, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirAreaProfissionalInteresseContratacao(Integer areaProfissionalPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            AreaProfissionalInteresseContratacaoVO obj = (AreaProfissionalInteresseContratacaoVO) e.next();
//            obj.getAreaProfissional().setCodigo(areaProfissionalPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, usuario);
            } else {
                alterar(obj, usuario);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>AreaProfissionalInteresseContratacaoVO</code> relacionados a um objeto da
     * classe <code>basico.Pessoa</code>.
     *
     * @param areaProfissional
     *            Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe
     *            <code>AreaProfissionalInteresseContratacaoVO</code>.
     * @return List Contendo todos os objetos da classe <code>AreaProfissionalInteresseContratacaoVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarAreaProfissionalInteresseContratacao(Integer areaProfissional, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM AreaProfissionalInteresseContratacao WHERE areaProfissional = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{areaProfissional});
        while (resultado.next()) {
            objetos.add(AreaProfissionalInteresseContratacao.montarDados(resultado, usuario));
        }
        return objetos;
    }

    public List consultarAreaProfissionalInteresseContratacaoPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM AreaProfissionalInteresseContratacao WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa});
        while (resultado.next()) {
            objetos.add(AreaProfissionalInteresseContratacao.montarDados(resultado, usuario));
        }
        return objetos;
    }

    public AreaProfissionalInteresseContratacaoVO consultarPorChavePrimaria(Integer areaProfissionalPrm, Integer pessoaPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM AreaProfissionalInteresseContratacao WHERE areaProfissional = ?, pessoa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{areaProfissionalPrm, pessoaPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    public static void validarDados(AreaProfissionalInteresseContratacaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getAreaProfissional().getDescricaoAreaProfissional() == null || obj.getAreaProfissional().getDescricaoAreaProfissional().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO deve ser informado.");
        }
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>FadosComerciaisVO</code>
     * ao List <code>fadosComerciaisVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>FadosComerciaisVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjAreaProfissionalVOs(AreaProfissionalVO obj, PessoaVO pessoa) throws Exception {
        int index = 0;
        AreaProfissionalInteresseContratacaoVO apic = new AreaProfissionalInteresseContratacaoVO();
        apic.setPessoa(pessoa);
        apic.setAreaProfissional(obj);
        validarDados(apic);
        Iterator i = pessoa.getAreaProfissionalInteresseContratacaoVOs().iterator();
        while (i.hasNext()) {
            AreaProfissionalInteresseContratacaoVO objExistente = (AreaProfissionalInteresseContratacaoVO) i.next();
            if (objExistente.getAreaProfissional().getDescricaoAreaProfissional().equals(apic.getAreaProfissional().getDescricaoAreaProfissional())) {
                pessoa.getAreaProfissionalInteresseContratacaoVOs().set(index, apic);
                return ;
            }
            index++;
        }
        pessoa.getAreaProfissionalInteresseContratacaoVOs().add(apic);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>FadosComerciaisVO</code>
     * no List <code>fadosComerciaisVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do objeto no List.
     * @param curso  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjAreaProfissionalVOs(AreaProfissionalInteresseContratacaoVO obj, PessoaVO pessoa) throws Exception {
        int index = 0;
        Iterator i = pessoa.getAreaProfissionalInteresseContratacaoVOs().iterator();
        while (i.hasNext()) {
            AreaProfissionalInteresseContratacaoVO objExistente = (AreaProfissionalInteresseContratacaoVO) i.next();
            if (objExistente.getAreaProfissional().getCodigo().equals(obj.getAreaProfissional().getCodigo())) {
                pessoa.getAreaProfissionalInteresseContratacaoVOs().remove(index);
                return;
            }
            index++;
        }
        //excluirObjSubordinadoOC
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AreaProfissionalInteresseContratacao.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        AreaProfissionalInteresseContratacao.idEntidade = idEntidade;
    }
}
