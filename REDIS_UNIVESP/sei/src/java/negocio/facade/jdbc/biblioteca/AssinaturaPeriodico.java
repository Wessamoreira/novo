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
import negocio.comuns.biblioteca.AssinaturaPeriodicoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.AssinaturaPeriodicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AssinaturaPeriodicoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>AssinaturaPeriodicoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see AssinaturaPeriodicoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AssinaturaPeriodico extends ControleAcesso implements AssinaturaPeriodicoInterfaceFacade {

    protected static String idEntidade;

    public AssinaturaPeriodico() throws Exception {
        super();
        setIdEntidade("AssinaturaPeriodico");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>AssinaturaPeriodicoVO</code>.
     */
    public AssinaturaPeriodicoVO novo() throws Exception {
        AssinaturaPeriodico.incluir(getIdEntidade());
        AssinaturaPeriodicoVO obj = new AssinaturaPeriodicoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AssinaturaPeriodicoVO</code>. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>AssinaturaPeriodicoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AssinaturaPeriodicoVO obj, UsuarioVO usuario) throws Exception {
        try {
            AssinaturaPeriodicoVO.validarDados(obj);
            AssinaturaPeriodico.incluir(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO assinaturaperiodico( dataCadastro, dataInicioAssinatura, dataFinalAssinatura, periodicidade, usuarioResponsavel, nome, pha, cdu, isbn, editora, mes, anovolume, nrEdicaoEspecial, situacaoAssinatura, localPublicacao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataCadastro()));
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicioAssinatura()));
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataFinalAssinatura()));
                    sqlInserir.setString(4, obj.getPeriodicidade());
                    if (obj.getUsuarioResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getUsuarioResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (obj.getNome() != null || !obj.getNome().equals("")) {
                        sqlInserir.setString(6, obj.getNome());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setString(7, obj.getPha());
                    sqlInserir.setString(8, obj.getCdu());
                    sqlInserir.setString(9, obj.getIsbn());
                    sqlInserir.setInt(10, obj.getEditora().getCodigo());
                    sqlInserir.setString(11, obj.getMes());
                    sqlInserir.setString(12, obj.getAnovolume());
                    sqlInserir.setString(13, obj.getNrEdicaoEspecial());
                    sqlInserir.setString(14, obj.getSituacaoAssinatura());
                    sqlInserir.setString(15, obj.getLocalPublicacao());
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
            persistirListaExemplares(obj, usuario);

            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void persistirListaExemplares(AssinaturaPeriodicoVO assinaturaPeriodicoVO, UsuarioVO usuario) throws Exception {
        for (ExemplarVO exemplarVO : assinaturaPeriodicoVO.getExemplarVOs()) {
            exemplarVO.setAssinaturaPeriodico(assinaturaPeriodicoVO);
            getFacadeFactory().getExemplarFacade().incluir(exemplarVO, true, false, usuario);
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AssinaturaPeriodicoVO</code>. Sempre utiliza
     * a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os
     * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>AssinaturaPeriodicoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AssinaturaPeriodicoVO obj, UsuarioVO usuario) throws Exception {
        try {

            AssinaturaPeriodicoVO.validarDados(obj);
            AssinaturaPeriodico.alterar(getIdEntidade(), true , usuario);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder("UPDATE assinaturaperiodico set dataCadastro=?, dataInicioAssinatura=?, dataFinalAssinatura=?, periodicidade=?, usuarioResponsavel=?, nome=?, ");
            sql.append(" pha=?, cdu=?, isbn=?, editora=?, mes=?, anovolume=?, nrEdicaoEspecial=?, situacaoAssinatura=?, localPublicacao=? ");
            sql.append("WHERE ((codigo = ?))");
            
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataCadastro()));
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataInicioAssinatura()));
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataFinalAssinatura()));
                    sqlAlterar.setString(4, obj.getPeriodicidade());
                    if (obj.getUsuarioResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getUsuarioResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    if (obj.getNome() != null || !obj.getNome().equals("")) {
                        sqlAlterar.setString(6, obj.getNome());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setString(7, obj.getPha());
                    sqlAlterar.setString(8, obj.getCdu());
                    sqlAlterar.setString(9, obj.getIsbn());
                    sqlAlterar.setInt(10, obj.getEditora().getCodigo());
                    sqlAlterar.setString(11, obj.getMes());
                    sqlAlterar.setString(12, obj.getAnovolume());
                    sqlAlterar.setString(13, obj.getNrEdicaoEspecial());
                    sqlAlterar.setString(14, obj.getSituacaoAssinatura());
                    sqlAlterar.setString(15, obj.getLocalPublicacao());
                    sqlAlterar.setInt(16, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AssinaturaPeriodicoVO</code>. Sempre localiza o registro
     * a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>AssinaturaPeriodicoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AssinaturaPeriodicoVO obj) throws Exception {
        try {
            AssinaturaPeriodico.excluir(getIdEntidade());
            String sql = "DELETE FROM assinaturaperiodico WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>AssinaturaPeriodico</code> através do valor do atributo
     * <code>Integer exemplar</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorExemplar(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico inner join exemplar on exemplar.assinaturaperiodico = assinaturaperiodo.exemplar WHERE exemplar.codigoBarra ilike '" + valorConsulta + "' ORDER BY exemplar.codigobarra";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorCatalogo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico inner join exemplar on exemplar.assinaturaperiodico = assinaturaperiodo.exemplar "
                + "inner join catalogo on exemplar.catalogo = catalogo.codigo "
                + "WHERE sem_acentos(catalogo.titulo) ilike sem_acentos('" + valorConsulta + "%') ORDER BY catalogo.titulo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AssinaturaPeriodico</code> através do valor do atributo <code>nome</code>
     * da classe <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct usuario.nome as usuarioNome, assinaturaperiodico.* FROM assinaturaperiodico");
        sqlStr.append(" INNER JOIN usuario ON usuario.codigo = assinaturaperiodico.usuarioresponsavel");
        sqlStr.append(" WHERE  sem_acentos(Usuario.nome) ilike sem_acentos('").append(valorConsulta).append("%') ORDER BY usuario.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>assinaturaperiodico</code> através do valor do atributo
     * <code>Date dataFinalAssinatura</code>. Retorna os objetos com valores pertecentes ao período informado por
     * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataFinalAssinatura(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico WHERE ((dataFinalAssinatura >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalAssinatura <= '" + Uteis.getDataJDBC(prmFim)
                + "')) ORDER BY dataFinalAssinatura";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AssinaturaPeriodico</code> através do valor do atributo
     * <code>Date dataInicioAssinatura</code>. Retorna os objetos com valores pertecentes ao período informado por
     * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataInicioAssinatura(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico WHERE ((dataInicioAssinatura >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicioAssinatura <= '" + Uteis.getDataJDBC(prmFim)
                + "')) ORDER BY dataInicioAssinatura";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AssinaturaPeriodico</code> através do valor do atributo
     * <code>Date dataCadastro</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico WHERE ((dataCadastro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCadastro <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataCadastro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorNome(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico WHERE sem_acentos(nome) ilike sem_acentos('" + nome + "%') ORDER BY dataCadastro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AssinaturaPeriodico</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>AssinaturaPeriodicoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>AssinaturaPeriodicoVO</code>.
     *
     * @return O objeto da classe <code>AssinaturaPeriodicoVO</code> com os dados devidamente montados.
     */
    public static AssinaturaPeriodicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        AssinaturaPeriodicoVO obj = new AssinaturaPeriodicoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        obj.setDataInicioAssinatura(dadosSQL.getDate("dataInicioAssinatura"));
        obj.setDataFinalAssinatura(dadosSQL.getDate("dataFinalAssinatura"));
        obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
        obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("usuarioResponsavel"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setCdu(dadosSQL.getString("cdu"));
        obj.setPha(dadosSQL.getString("pha"));
        obj.setIsbn(dadosSQL.getString("isbn"));
        obj.setEditora(getFacadeFactory().getEditoraFacade().consultarPorChavePrimaria(dadosSQL.getInt("editora"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        obj.setMes(dadosSQL.getString("mes"));
        obj.setAnovolume(dadosSQL.getString("anovolume"));
        obj.setNrEdicaoEspecial(dadosSQL.getString("nrEdicaoEspecial"));
        obj.setSituacaoAssinatura(dadosSQL.getString("situacaoAssinatura"));
        obj.setLocalPublicacao(dadosSQL.getString("localPublicacao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosUsuarioResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            return obj;
        }
        obj.setExemplarVOs(getFacadeFactory().getExemplarFacade().consultarPorCodigoAssinaturaPeriodico(obj.getCodigo().toString(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosUsuarioResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>AssinaturaPeriodicoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUsuarioResponsavel(AssinaturaPeriodicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuarioResponsavel().getCodigo().intValue() == 0) {
            obj.setUsuarioResponsavel(new UsuarioVO());
            return;
        }
        obj.setUsuarioResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AssinaturaPeriodicoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public AssinaturaPeriodicoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM assinaturaperiodico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Assinatura Periódico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public void adicionarExemplaresAssinaturaPeriodico(AssinaturaPeriodicoVO assinaturaPeriodico, ExemplarVO obj) throws Exception {        
        int index = 0;
        Iterator i = assinaturaPeriodico.getExemplarVOs().iterator();
        while (i.hasNext()) {
            ExemplarVO objExistente = (ExemplarVO) i.next();
            if (objExistente.getNumeroEdicao().equals(obj.getNumeroEdicao())) {
                assinaturaPeriodico.getExemplarVOs().set(index, obj);
                return;
            }
            index++;
        }        
        assinaturaPeriodico.getExemplarVOs().add(obj);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AssinaturaPeriodico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AssinaturaPeriodico.idEntidade = idEntidade;
    }
}
