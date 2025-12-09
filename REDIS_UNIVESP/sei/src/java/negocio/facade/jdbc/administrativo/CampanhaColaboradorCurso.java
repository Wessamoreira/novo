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

import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaColaboradorCursoInterfaceFacade;

/**
 *
 * @author PEDRO
 */
@Repository
@Scope("singleton")
@Lazy
public class CampanhaColaboradorCurso extends ControleAcesso implements CampanhaColaboradorCursoInterfaceFacade {

    protected static String idEntidade;

    public CampanhaColaboradorCurso() throws Exception {
        super();
        setIdEntidade("CampanhaColaborador");
    }

    public static void validarDados(CampanhaColaboradorCursoVO obj) throws ConsistirException {
        if (obj.getCursoVO() == null || obj.getCursoVO().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CURSO (Formação Acadêmica) deve ser informado.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> que será gravado no banco de
     * dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    public void incluir(CampanhaColaboradorCursoVO obj) throws Exception {
        incluir(obj, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    @Override
    public void incluir(final CampanhaColaboradorCursoVO obj, boolean verificarAcesso) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO CampanhaColaboradorCurso( campanhaColaborador, curso, dataUltimoCompromissoGerado ) VALUES (?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getCampanhaColaboradorVO().getCodigo().intValue());
                if (obj.getCursoVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getCursoVO().getCodigo());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataUltimoCompromissoGerado()));
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

    public void alterar(CampanhaColaboradorCursoVO obj) throws Exception {
        alterar(obj, true);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code>. Sempre utiliza a chave primária
     * da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>alterar</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> que será alterada no banco de
     * dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    @Override
    public void alterar(final CampanhaColaboradorCursoVO obj, boolean verificarAcesso) throws Exception {
        validarDados(obj);
        final String sql = "UPDATE CampanhaColaboradorCurso set campanhaColaborador=?, curso=?, dataUltimoCompromissoGerado=?  WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getCampanhaColaboradorVO().getCodigo().intValue());
                if (obj.getCursoVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getCursoVO().getCodigo());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataUltimoCompromissoGerado()));
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code>. Sempre localiza o registro a ser
     * excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> que será removido no banco de
     * dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    @Override
    public void excluir(CampanhaColaboradorCursoVO obj) throws Exception {
        CampanhaColaboradorCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM CampanhaColaboradorCurso WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>CampanhaColaboradorCurso</code> através do valor do atributo
     * <code>nome</code> da classe
     * <code>Pessoa</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>CampanhaColaboradorCursoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Override
    public CampanhaColaboradorCursoVO consultarCampanhaAndResponsavel(Integer codCurso, Integer codUnidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT funcionario.codigo as funcionario, funcionario.pessoa, campanha.codigo as campanha from campanhacolaboradorcurso   ");
        sqlStr.append(" inner join campanhacolaborador on campanhacolaborador.codigo = campanhacolaboradorcurso.campanhacolaborador ");
        sqlStr.append(" inner join campanha on campanha.codigo = campanhacolaborador.campanha ");
        sqlStr.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo ");
        sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
        sqlStr.append(" left join preinscricao on preinscricao.responsavel = funcionario.pessoa and campanhacolaboradorcurso.curso = preinscricao.curso  ");
        sqlStr.append(" where  campanhacolaboradorcurso.curso = ").append(codCurso);
        sqlStr.append(" and campanha.tipocampanha = '").append(tipoCampanhaEnum).append("' ");
        sqlStr.append(" and funcionariocargo.ativo ");
        if (!situacao.isEmpty()) {
            sqlStr.append(" and campanha.situacao = '").append(situacao).append("' ");
        }
        if (codUnidadeEnsino != 0) {
            sqlStr.append(" and campanha.unidadeensino =  ").append(codUnidadeEnsino);
        }
        sqlStr.append(" group by funcionario.codigo, funcionario.pessoa, campanha.codigo order by count (preinscricao.codigo) limit 1  ");
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (dadosSQL.next()) {
            CampanhaColaboradorCursoVO obj = new CampanhaColaboradorCursoVO();
            obj.getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario"));
            obj.getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
            obj.getCampanhaColaboradorVO().getCampanha().setCodigo(dadosSQL.getInt("campanha"));
            return obj;

        }
        return new CampanhaColaboradorCursoVO();

    }

    @Override
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CampanhaColaboradorCurso.* FROM CampanhaColaboradorCurso, Pessoa WHERE CampanhaColaboradorCurso.campanhaColaborador = Pessoa.codigo and Pessoa.nome ilike('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    @Override
    public List consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CampanhaColaboradorCurso.* FROM CampanhaColaboradorCurso, Pessoa WHERE CampanhaColaboradorCurso.campanhaColaborador = Pessoa.codigo and Pessoa.codigo = " + valorConsulta + " ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>CampanhaColaboradorCurso</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>CampanhaColaboradorCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Override
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaColaboradorCurso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     * <code>CampanhaColaboradorCursoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code>.
     *
     * @return O objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> com os dados devidamente
     * montados.
     */
    public static CampanhaColaboradorCursoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        CampanhaColaboradorCursoVO obj = new CampanhaColaboradorCursoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCampanhaColaboradorVO().setCodigo(dadosSQL.getInt("campanhaColaborador"));
        obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
        obj.setNovoObj(false);
        obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da
     * <code>CampanhaColaboradorCursoVO</code> no BD. Faz uso da operação
     * <code>excluir</code> disponível na classe
     * <code>CampanhaColaboradorCurso</code>.
     *
     * @param <code>campanhaColaborador</code> campo chave para exclusão dos
     * objetos no BD.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    @Override
    public void excluirCampanhaColaboradorCursos(Integer campanhaColaborador, List objetos) throws Exception {
        excluirCampanhaColaboradorCursos(campanhaColaborador, objetos, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    @Override
    public void excluirCampanhaColaboradorCursos(Integer campanhaColaborador, List objetos, boolean verificarAcesso) throws Exception {
        String sql = "DELETE FROM CampanhaColaboradorCurso WHERE (campanhaColaborador = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CampanhaColaboradorCursoVO obj = (CampanhaColaboradorCursoVO) i.next();
            sql += " and codigo != " + obj.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{campanhaColaborador.intValue()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void alterarCampanhaColaboradorCurso(Integer campanhaColaborador, List objetos, boolean verificarAcesso) throws Exception {
        String str = "DELETE FROM CampanhaColaboradorCurso WHERE campanhaColaborador = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CampanhaColaboradorCursoVO objeto = (CampanhaColaboradorCursoVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str, new Object[]{campanhaColaborador});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaColaboradorCursoVO obj = (CampanhaColaboradorCursoVO) e.next();
            obj.getCampanhaColaboradorVO().setCodigo(campanhaColaborador);
            if (obj.getCodigo().equals(0)) {
                incluir(obj, verificarAcesso);
            } else {
                alterar(obj, verificarAcesso);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    @Override
    public void incluirCampanhaColaboradorCursos(Integer campanhaColaboradorPrm, List objetos, boolean verificarAcesso) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaColaboradorCursoVO obj = (CampanhaColaboradorCursoVO) e.next();
            obj.getCampanhaColaboradorVO().setCodigo(campanhaColaboradorPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, verificarAcesso);
            } else {
                alterar(obj, verificarAcesso);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os
     * <code>CampanhaColaboradorCursoVO</code> relacionados a um objeto da
     * classe
     * <code>basico.Pessoa</code>.
     *
     * @param campanhaColaborador Atributo de
     * <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da
     * classe
     * <code>CampanhaColaboradorCursoVO</code>.
     * @return List Contendo todos os objetos da classe
     * <code>CampanhaColaboradorCursoVO</code> resultantes da consulta.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    public List consultarCampanhaColaboradorCursos(Integer campanhaColaborador, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        CampanhaColaboradorCurso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM CampanhaColaboradorCurso WHERE campanhaColaborador = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{campanhaColaborador.intValue()});
        while (resultado.next()) {
            CampanhaColaboradorCursoVO novoObj = new CampanhaColaboradorCursoVO();
            novoObj = CampanhaColaboradorCurso.montarDados(resultado, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return CampanhaColaboradorCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CampanhaColaboradorCurso.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> através de sua chave primária.
     *
     * @exception Exception Caso haja problemas de conexão ou localização do
     * objeto procurado.
     */
    @Override
    public CampanhaColaboradorCursoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CampanhaColaboradorCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }
    
    
}
