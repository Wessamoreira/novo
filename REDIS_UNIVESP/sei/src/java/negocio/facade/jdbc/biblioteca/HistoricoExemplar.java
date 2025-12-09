package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.HistoricoExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistoricoExemplar;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.HistoricoExemplarInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>HistoricoExemplarVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>HistoricoExemplarVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see HistoricoExemplarVO
 * @see ControleAcesso
 * @see Exemplar
 */
@Repository
@Scope("singleton")
@Lazy
public class HistoricoExemplar extends ControleAcesso implements HistoricoExemplarInterfaceFacade {

    protected static String idEntidade;

    public HistoricoExemplar() throws Exception {
        super();
        setIdEntidade("Exemplar");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>HistoricoExemplarVO</code>.
     */
    public HistoricoExemplarVO novo() throws Exception {
        HistoricoExemplar.incluir(getIdEntidade());
        HistoricoExemplarVO obj = new HistoricoExemplarVO();
        return obj;
    }

    /**
     * Registro um HistoricoExemplar quando um registroSaidaAcervo é feito.
     *
     * @param exemplarVO
     * @param situacao
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void registrarHistoricoExemplarParaRegistroAcervo(ExemplarVO exemplarVO, String situacao, UsuarioVO usuario) throws Exception {
        HistoricoExemplarVO historicoExemplarVO = new HistoricoExemplarVO();
        historicoExemplarVO.setExemplar(exemplarVO.getCodigo());
        historicoExemplarVO.setData(new Date());
        historicoExemplarVO.setResponsavel(usuario);
        historicoExemplarVO.setEstado(exemplarVO.getEstadoExemplar());
        historicoExemplarVO.setSituacao(situacao);
        incluir(historicoExemplarVO);
    }

    /**
     * Registra um HistoricoExemplar quando um Emprestimo é feito. Trata uma inclusão de renovação.
     *
     * @param emprestimoVO
     * @param renovacao
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void registrarHistoricoExemplarParaEmprestimo(EmprestimoVO emprestimoVO, Boolean renovacao) throws Exception {
        HistoricoExemplarVO historicoExemplarVO = new HistoricoExemplarVO();
        for (ItemEmprestimoVO itemEmprestimoVO : emprestimoVO.getItemEmprestimoVOs()) {
            //if (!itemEmprestimoVO.getAlterado()) {
                historicoExemplarVO.setExemplar(itemEmprestimoVO.getExemplar().getCodigo());
                historicoExemplarVO.setData(new Date());
                historicoExemplarVO.setResponsavel(emprestimoVO.getAtendente());
                historicoExemplarVO.setEstado(itemEmprestimoVO.getExemplar().getEstadoExemplar());
                if(itemEmprestimoVO.getEmprestar()){
                    historicoExemplarVO.setSituacao(SituacaoHistoricoExemplar.EMPRESTADO.getValor());
                } else {
                    historicoExemplarVO.setSituacao(SituacaoHistoricoExemplar.RENOVADO.getValor());
                }
                incluir(historicoExemplarVO);
            //}
        }
    }

    /**
     * Registra um <code>HistoricoExemplar</code> quando um <code>Emprestimo</code> é devolvido.
     *
     * @param emprestimoVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void registrarHistoricoExemplarDevolucao(EmprestimoVO emprestimoVO, ItemEmprestimoVO itemEmprestimoVO)
            throws Exception {
        HistoricoExemplarVO historicoExemplarVO = new HistoricoExemplarVO();
        historicoExemplarVO.setExemplar(itemEmprestimoVO.getExemplar().getCodigo());
        historicoExemplarVO.setData(new Date());
        historicoExemplarVO.setResponsavel(emprestimoVO.getAtendente());
        historicoExemplarVO.setSituacao(SituacaoHistoricoExemplar.DEVOLVIDO.getValor());
        historicoExemplarVO.setEstado(itemEmprestimoVO.getExemplar().getEstadoExemplar());
        incluir(historicoExemplarVO);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>HistoricoExemplarVO</code>.
     * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>HistoricoExemplarVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final HistoricoExemplarVO obj) throws Exception {
        try {
            HistoricoExemplarVO.validarDados(obj);
            /**
             * @author Leonardo Riciolle
             * Comentado 28/10/2014
             * Classe Subordinada
             */
            //HistoricoExemplar.incluir(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO HistoricoExemplar( exemplar, data, responsavel, situacao, motivo, estado ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getExemplar().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getExemplar().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getSituacao());
                    sqlInserir.setString(5, obj.getMotivo());
                    sqlInserir.setString(6, obj.getEstado());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>HistoricoExemplarVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>HistoricoExemplarVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final HistoricoExemplarVO obj) throws Exception {
        try {
            HistoricoExemplarVO.validarDados(obj);
            /**
             * @author Leonardo Riciolle
             * Comentado 28/10/2014
             * Classe Subordinada
             */
            // HistoricoExemplar.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE HistoricoExemplar set exemplar=?, data=?, responsavel=?, situacao=?, motivo=?, estado=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getExemplar().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getExemplar().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setString(4, obj.getSituacao());
                    sqlAlterar.setString(5, obj.getMotivo());
                    sqlAlterar.setString(6, obj.getEstado());
                    sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>HistoricoExemplarVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>HistoricoExemplarVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(HistoricoExemplarVO obj) throws Exception {
        try {
        	/**
             * @author Leonardo Riciolle
             * Comentado 28/10/2014
             * Classe Subordinada
             */
			// HistoricoExemplar.excluir(getIdEntidade());
            String sql = "DELETE FROM HistoricoExemplar WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoExemplar</code> através do valor do atributo
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>HistoricoExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM HistoricoExemplar WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoExemplar</code> através do valor do atributo
     * <code>codigo</code> da classe <code>Exemplar</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>HistoricoExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoExemplar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT HistoricoExemplar.* FROM HistoricoExemplar, Exemplar WHERE HistoricoExemplar.exemplar = Exemplar.codigo and Exemplar.codigo >= " + valorConsulta.intValue()
                + " ORDER BY Exemplar.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoExemplar</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>HistoricoExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM HistoricoExemplar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>HistoricoExemplarVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>HistoricoExemplarVO</code>.
     *
     * @return O objeto da classe <code>HistoricoExemplarVO</code> com os dados devidamente montados.
     */
    public static HistoricoExemplarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HistoricoExemplarVO obj = new HistoricoExemplarVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setExemplar(dadosSQL.getInt("exemplar"));
        obj.setData(dadosSQL.getDate("data"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.setEstado(dadosSQL.getString("estado"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto
     * <code>HistoricoExemplarVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a
     * consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(HistoricoExemplarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getResponsavel().getCodigo())) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>HistoricoExemplarVO</code> no BD. Faz uso da operação
     * <code>excluir</code> disponível na classe <code>HistoricoExemplar</code>.
     *
     * @param <code>exemplar</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirHistoricoExemplars(Integer exemplar, UsuarioVO usuario) throws Exception {
        try {
            HistoricoExemplar.excluir(getIdEntidade());
            String sql = "DELETE FROM HistoricoExemplar WHERE (exemplar = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, exemplar);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>HistoricoExemplarVO</code> contidos em um Hashtable no
     * BD. Faz uso da operação <code>excluirHistoricoExemplars</code> e <code>incluirHistoricoExemplars</code>
     * disponíveis na classe <code>HistoricoExemplar</code>.
     *
     * @param objetos
     *            List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarHistoricoExemplars(Integer exemplar, List objetos) throws Exception {
        String str = "DELETE FROM HistoricoExemplar WHERE exemplar = " + exemplar;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            HistoricoExemplarVO objeto = (HistoricoExemplarVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().queryForRowSet(str);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            HistoricoExemplarVO objeto = (HistoricoExemplarVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto);
            } else {
                alterar(objeto);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>HistoricoExemplarVO</code> no BD. Garantindo o relacionamento
     * com a entidade principal <code>biblioteca.Exemplar</code> através do atributo de vínculo.
     *
     * @param objetos
     *            List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirHistoricoExemplars(Integer exemplarPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            HistoricoExemplarVO obj = (HistoricoExemplarVO) e.next();
            obj.setExemplar(exemplarPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>HistoricoExemplarVO</code> relacionados a um objeto da classe
     * <code>biblioteca.Exemplar</code>.
     *
     * @param exemplar
     *            Atributo de <code>biblioteca.Exemplar</code> a ser utilizado para localizar os objetos da classe
     *            <code>HistoricoExemplarVO</code>.
     * @return List Contendo todos os objetos da classe <code>HistoricoExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarHistoricoExemplars(Integer exemplar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HistoricoExemplar.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sqlStr = "SELECT * FROM HistoricoExemplar WHERE exemplar = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{exemplar});
        while (tabelaResultado.next()) {
            objetos.add(HistoricoExemplar.montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>HistoricoExemplarVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public HistoricoExemplarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM HistoricoExemplar WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( HistoricoExemplar ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return HistoricoExemplar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        HistoricoExemplar.idEntidade = idEntidade;
    }
}
