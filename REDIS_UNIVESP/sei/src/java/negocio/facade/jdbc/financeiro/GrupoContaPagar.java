package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.GrupoContaPagarVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.GrupoContaPagarInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>GrupoContaPagarVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>GrupoContaPagarVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see GrupoContaPagarVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class GrupoContaPagar extends ControleAcesso implements GrupoContaPagarInterfaceFacade {

    protected static String idEntidade;

    public GrupoContaPagar() throws Exception {
        super();
        setIdEntidade("ContaPagar");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>GrupoContaPagarVO</code>.
     */
    public GrupoContaPagarVO novo() throws Exception {
        GrupoContaPagar.incluir(getIdEntidade());
        GrupoContaPagarVO obj = new GrupoContaPagarVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>GrupoContaPagarVO</code>. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>GrupoContaPagarVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final GrupoContaPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            GrupoContaPagarVO.validarDados(obj);
            GrupoContaPagar.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO GrupoContaPagar( dataEmissao, fornecedor, funcionario, tipoSacado, banco, responsavel, pessoa, parceiro, responsavelFinanceiro ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataEmissao()));
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getTipoSacado());
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getParceiro().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getParceiro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getResponsavelFinanceiro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
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
            alterarContasPagar(obj, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>GrupoContaPagarVO</code>. Sempre utiliza
     * a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os
     * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>GrupoContaPagarVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final GrupoContaPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            GrupoContaPagar.alterar(getIdEntidade(), true, usuario);
            GrupoContaPagarVO.validarDados(obj);
            final String sql = "UPDATE GrupoContaPagar set dataEmissao=?, fornecedor=?, funcionario = ?, tipoSacado = ? , banco=?, responsavel=?, pessoa=?, parceiro=?, responsavelFinanceiro=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataEmissao()));
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setString(++i, obj.getTipoSacado());
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getParceiro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getParceiro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getResponsavelFinanceiro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            alterarContasPagar(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    // public void estornarPagamentoEmDespesaDW(GrupoContaPagarVO obj) throws Exception {
    // new DespesaDW().incluir(obj.getDespesaDWVO(obj.getValorPago() * -1));
    // }
    /**
     * Operação responsável por excluir no BD um objeto da classe <code>GrupoContaPagarVO</code>. Sempre localiza o registro
     * a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>GrupoContaPagarVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(GrupoContaPagarVO obj, UsuarioVO usuarioVO) throws SQLException, Exception {
        GrupoContaPagar.excluir(getIdEntidade(), true, usuarioVO);
        excluirContasPagar(obj.getCodigo());
        String sql = "DELETE FROM GrupoContaPagar WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContasPagar(Integer grupoContaPagar) throws Exception {
        String sql = "DELETE FROM ContaPagar WHERE (grupoContaPagar = ?) ";
        getConexao().getJdbcTemplate().update(sql, new Object[]{grupoContaPagar});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static void alterarContasPagar(GrupoContaPagarVO obj, UsuarioVO usuario) throws Exception {
        Iterator i = obj.getContaPagarVOs().iterator();
        while (i.hasNext()) {
            ContaPagarVO contaPagar = (ContaPagarVO) i.next();
            if (contaPagar.getCodigo().equals(0)) {
                contaPagar.setGrupoContaPagar(obj);
                getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuario);
            } else {
                getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, true, usuario);
            }
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo
     * <code>identificadorCentroDespesa</code> da classe <code>CentroDespesa</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>GrupoContaPagarVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controleAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct GrupoContaPagar.*, categoriaDespesa.identificadorcategoriaDespesa from GrupoContaPagar ");
        sqlStr.append("inner join contaPagar on contapagar.grupoContaPagar = grupoContaPagar.codigo ");
        sqlStr.append("inner join categoriadespesa on contapagar.centrodespesa = categoriadespesa.codigo ");
        sqlStr.append("where upper(categoriadespesa.identificadorcategoriadespesa) like('").append(valorConsulta.toUpperCase()).append("%') ");
        if (unidadeEnsino != 0) {
            sqlStr.append("AND ContaPagar.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        }
        if (!tipoData.equals("semData") && !tipoData.equals("")) {
            if (tipoData.equals("vencimento")) {
                sqlStr.append("and ContaPagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            } else if (tipoData.equals("fatoGerador")) {
                sqlStr.append("and ContaPagar.dataFatoGerador between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            } else if (tipoData.equals("emissão")) {
                sqlStr.append("and GrupoContaPagar.dataEmissao between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            }
        }
        sqlStr.append("ORDER BY categoriaDespesa.identificadorcategoriaDespesa");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorIdentificadorCentroDespesaCentroDespesaResumido(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controleAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct grupoContaPagar.codigo as \"grupoContaPagar.codigo\", GrupoContaPagar.dataemissao, ");
        sqlStr.append("grupoContaPagar.responsavel, usuario.nome as \"responsavel.nome\", fornecedor.codigo as \"fornecedor.codigo\", ");
        sqlStr.append("fornecedor.nome as fornecedor, funcionario.codigo as \"funcionario.codigo\", pessoafuncionario.codigo as \"pessoafuncionario.codigo\", ");
        sqlStr.append("pessoafuncionario.nome as funcionario, pessoa.codigo as \"pessoa.codigo\", pessoa.nome as pessoa, categoriaDespesa.identificadorcategoriaDespesa, ");
        sqlStr.append("ContaPagar.datavencimento, ContaPagar.dataFatoGerador, responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", responsavelFinanceiro.codigo as \"responsavelFinanceiro.codigo\", parceiro.nome as \"parceiro.nome\", parceiro.codigo as \"parceiro.codigo\", grupoContaPagar.tipoSacado as tipoSacado  from GrupoContaPagar ");
        sqlStr.append("inner join contaPagar on contapagar.grupoContaPagar = grupoContaPagar.codigo ");
        sqlStr.append("inner join categoriadespesa on contapagar.centrodespesa = categoriadespesa.codigo ");
        sqlStr.append("left join fornecedor on fornecedor.codigo = GrupoContaPagar.fornecedor ");
        sqlStr.append("left join funcionario on funcionario.codigo = GrupoContaPagar.funcionario ");
        sqlStr.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
        sqlStr.append("left join pessoa on pessoa.codigo = GrupoContaPagar.pessoa ");
        sqlStr.append("left join banco on banco.codigo = GrupoContaPagar.banco ");
        sqlStr.append("left join usuario on usuario.codigo = GrupoContaPagar.responsavel ");
        sqlStr.append("left join parceiro on parceiro.codigo = GrupoContaPagar.parceiro ");
        sqlStr.append("left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = GrupoContaPagar.responsavelFinanceiro ");
        sqlStr.append("where upper(categoriadespesa.identificadorcategoriadespesa) like('").append(valorConsulta.toUpperCase()).append("%') ");
        if (unidadeEnsino != 0) {
            sqlStr.append("AND ContaPagar.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        }
        if (!tipoData.equals("semData") && !tipoData.equals("")) {
            if (tipoData.equals("vencimento")) {
                sqlStr.append("and ContaPagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            } else if (tipoData.equals("fatoGerador")) {
                sqlStr.append("and ContaPagar.dataFatoGerador between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            } else if (tipoData.equals("emissão")) {
                sqlStr.append("and GrupoContaPagar.dataEmissao between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            }
        }
        sqlStr.append("ORDER BY categoriaDespesa.identificadorcategoriaDespesa");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaResumido(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorFuncionarioMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT GrupoContaPagar.* FROM GrupoContaPagar "
                + "inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo "
                + "Inner join funcionario on ContaPagar.funcionario = Funcionario.codigo "
                + "WHERE upper( Funcionario.Matricula ) like('" + valorConsulta.toUpperCase() + "%') ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += "ORDER BY Funcionario.matricula";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorFuncionarioNome(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = " SELECT GrupoContaPagar.* FROM GrupoContaPagar "
                + "inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo "
                + "Inner join funcionario on ContaPagar.funcionario = Funcionario.codigo "
                + "Inner join pessoa on Funcionario.pessoa = pessoa.codigo "
                + "WHERE upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') "
                + "AND ContaPagar.datavencimento between '" + Uteis.getDataJDBC(dataIni) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += "ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo
     * <code>Date dataVencimento</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>GrupoContaPagarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataVencimento(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM GrupoContaPagar "
                + "Inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo"
                + "WHERE ((ContaPagar.datavencimento >= '" + Uteis.getDataJDBC(prmIni) + "') " + "and (ContaPagar.datavencimento <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += "ORDER BY ContaPagar.datavencimento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo
     * <code>Date dataEmissao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>GrupoContaPagarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataEmissao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM GrupoContaPagar "
                + "Inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo"
                + "WHERE ((dataEmissao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataEmissao <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += "ORDER BY dataEmissao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo <code>nome</code>
     * da classe <code>Fornecedor</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>GrupoContaPagarVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeFornecedor(String valorConsulta, Integer unidadeEnsino, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT GrupoContaPagar.* FROM GrupoContaPagar "
                + "inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo "
                + "inner join fornecedor on ContaPagar.fornecedor = Fornecedor.codigo "
                + "WHERE and upper( Fornecedor.nome ) like('" + valorConsulta.toUpperCase() + "%') "
                + " and ContaPagar.datavencimento between '" + Uteis.getDataJDBC(dataIni) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += " ORDER BY ContaPagar.datavencimento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoFornecedor(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT GrupoContaPagar.* FROM GrupoContaPagar "
                + "inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo "
                + "inner join fornecedor on ContaPagar.fornecedor = Fornecedor.codigo "
                + "WHERE Fornecedor.codigo  = " + valorConsulta.intValue() + " ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += "ORDER BY ContaPagar.datavencimento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoFuncionario(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT GrupoContaPagar.* FROM GrupoContaPagar "
                + "inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo "
                + "Inner join funcionario on ContaPagar.funcionario = Funcionario.codigo "
                + "WHERE Funcionario.codigo  = " + valorConsulta.intValue() + " ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        sqlStr += "ORDER BY ContaPagar.datavencimento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>GrupoContaPagarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM GrupoContaPagar WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>GrupoContaPagarVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public GrupoContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM GrupoContaPagar WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( GrupoContaPagar ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigoBanco(Integer codigoSacado, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "select GrupoContaPagar.* from GrupoContaPagar "
                + "inner join contaPagar on contaPagar.grupoContaPagar = grupoContaPagar.codigo"
                + "inner join banco on banco.codigo = GrupoContaPagar.banco "
                + "where banco.codigo = ? and ContaPagar.datavencimento between ? and ? ";
        if (unidadeEnsino != 0) {
            sql += "AND ContaPagar.unidadeEnsino = " + unidadeEnsino + " ";
        }
        SqlRowSet result = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoSacado, dataIni, dataFim});
        return montarDadosConsulta(result, nivelMontarDados, usuario);
    }

    public List consultarPorNomeFavorecidoResumido(String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder("select distinct grupoContaPagar.codigo as \"grupoContaPagar.codigo\", GrupoContaPagar.dataemissao, ");
        sql.append("grupoContaPagar.responsavel, usuario.nome as \"responsavel.nome\", fornecedor.codigo as \"fornecedor.codigo\", ");
        sql.append("fornecedor.nome as fornecedor, funcionario.codigo as \"funcionario.codigo\", pessoafuncionario.codigo as \"pessoafuncionario.codigo\", ");
        sql.append("pessoafuncionario.nome as funcionario, pessoa.codigo as \"pessoa.codigo\", pessoa.nome as pessoa, ContaPagar.datavencimento, ContaPagar.dataFatoGerador, responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", responsavelFinanceiro.codigo as \"responsavelFinanceiro.codigo\", parceiro.nome as \"parceiro.nome\", parceiro.codigo as \"parceiro.codigo\", GrupoContaPagar.tipoSacado as tipoSacado  ");
        sql.append("from GrupoContaPagar ");
        sql.append("left join contaPagar on grupoContaPagar.codigo = ContaPagar.GrupoContaPagar ");
        sql.append("left join fornecedor on fornecedor.codigo = GrupoContaPagar.fornecedor ");
        sql.append("left join funcionario on funcionario.codigo = GrupoContaPagar.funcionario ");
        sql.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
        sql.append("left join pessoa on pessoa.codigo = GrupoContaPagar.pessoa ");
        sql.append("left join banco on banco.codigo = GrupoContaPagar.banco ");
        sql.append("left join usuario on usuario.codigo = GrupoContaPagar.responsavel ");
        sql.append("left join parceiro on parceiro.codigo = GrupoContaPagar.parceiro ");
        sql.append("left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = GrupoContaPagar.responsavelFinanceiro ");
        sql.append(" where (upper(sem_acentos(fornecedor.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%'))");
        sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) or upper(sem_acentos(parceiro.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) or upper(sem_acentos(banco.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) or upper(sem_acentos(pessoa.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) ");
        sql.append(" or upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')))");
        if (unidadeEnsino != 0) {
            sql.append(" AND ContaPagar.unidadeensino = ").append(unidadeEnsino).append(" ");
        }
        if (!tipoData.equals("semData") && !tipoData.equals("")) {
            if (tipoData.equals("vencimento")) {
                sql.append("and ContaPagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
                sql.append("order by ContaPagar.datavencimento");
            } else if (tipoData.equals("fatoGerador")) {
                sql.append("and ContaPagar.dataFatoGerador between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
                sql.append("order by ContaPagar.dataFatoGerador");
            } else if (tipoData.equals("emissão")) {
                sql.append("and GrupoContaPagar.dataEmissao between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
                sql.append("order by GrupoContaPagar.dataEmissao");
            }
        } else {
            sql.append("order by GrupoContaPagar.dataEmissao");
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaResumido(resultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeFavorecido(String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder("select distinct GrupoContaPagar.*, ContaPagar.datavencimento, ContaPagar.dataFatoGerador from GrupoContaPagar ");
        sql.append("left join contaPagar on grupoContaPagar.codigo = ContaPagar.GrupoContaPagar ");
        sql.append("left join fornecedor on fornecedor.codigo = GrupoContaPagar.fornecedor ");
        sql.append("left join funcionario on funcionario.codigo = GrupoContaPagar.funcionario ");
        sql.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
        sql.append("left join pessoa on pessoa.codigo = GrupoContaPagar.pessoa ");
        sql.append("left join banco on banco.codigo = GrupoContaPagar.banco ");
        sql.append("left join parceiro on parceiro.codigo = GrupoContaPagar.parceiro ");
        sql.append("left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = GrupoContaPagar.responsavelFinanceiro ");
        sql.append(" where (upper(sem_acentos(fornecedor.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%'))");
        sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) or upper(sem_acentos(parceiro.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) or upper(sem_acentos(banco.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) or upper(sem_acentos(pessoa.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')) ");
        sql.append(" or upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos('");
        sql.append(nomeFavorecido.toUpperCase());
        sql.append("%')))");
        if (unidadeEnsino != 0) {
            sql.append(" AND ContaPagar.unidadeensino = ").append(unidadeEnsino).append(" ");
        }
        if (!tipoData.equals("semData") && !tipoData.equals("")) {
            if (tipoData.equals("vencimento")) {
                sql.append("and ContaPagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
                sql.append("order by ContaPagar.datavencimento");
            } else if (tipoData.equals("fatoGerador")) {
                sql.append("and ContaPagar.dataFatoGerador between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
                sql.append("order by ContaPagar.dataFatoGerador");
            } else if (tipoData.equals("emissão")) {
                sql.append("and GrupoContaPagar.dataEmissao between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
                sql.append("order by GrupoContaPagar.dataEmissao");
            }
        } else {
            sql.append("order by GrupoContaPagar.dataEmissao");
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsulta(resultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>GrupoContaPagarVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    public static List montarDadosConsultaResumido(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosResumido(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>GrupoContaPagarVO</code>.
     *
     * @return O objeto da classe <code>GrupoContaPagarVO</code> com os dados devidamente montados.
     */
    public static GrupoContaPagarVO montarDadosResumido(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        GrupoContaPagarVO obj = new GrupoContaPagarVO();
        obj.setCodigo(dadosSQL.getInt("grupoContaPagar.codigo"));
        obj.setDataEmissao(dadosSQL.getDate("dataEmissao"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
        obj.getFornecedor().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
        obj.getFornecedor().setNome(dadosSQL.getString("fornecedor"));
        obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario.codigo"));
        obj.getFuncionario().getPessoa().setCodigo(dadosSQL.getInt("pessoafuncionario.codigo"));
        obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("funcionario"));
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getPessoa().setNome(dadosSQL.getString("pessoa"));
        obj.getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("responsavelFinanceiro.codigo"));
        obj.getResponsavelFinanceiro().setNome(dadosSQL.getString("responsavelFinanceiro.nome"));
        obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro.codigo"));
        obj.getParceiro().setNome(dadosSQL.getString("parceiro.nome"));
        obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public static GrupoContaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        GrupoContaPagarVO obj = new GrupoContaPagarVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
        obj.setDataEmissao(dadosSQL.getDate("dataEmissao"));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getFuncionario().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
        obj.getBanco().setCodigo(new Integer(dadosSQL.getInt("banco")));
        obj.getResponsavelFinanceiro().setCodigo(new Integer(dadosSQL.getInt("responsavelFinanceiro")));
        obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorCodigoGrupoContaPagar(obj.getCodigo(), false, nivelMontarDados, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }
    
    public static void montarDadosResponsavelFinanceiro(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelFinanceiro().getCodigo().intValue() == 0) {            
            return;
        }
        getFacadeFactory().getPessoaFacade().carregarDados(obj.getResponsavelFinanceiro(), NivelMontarDados.BASICO, usuario);        
    }

    
    public static void montarDadosParceiro(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getParceiro().getCodigo().intValue() == 0) {
            obj.setParceiro(new ParceiroVO());
            return;
        }
        obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CentroDespesaVO</code> relacionado ao
     * objeto <code>GrupoContaPagarVO</code>. Faz uso da chave primária da classe <code>CentroDespesaVO</code> para realizar
     * a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosBanco(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getBanco().getCodigo().intValue() == 0) {
            obj.setBanco(new BancoVO());
            return;
        }
        obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosResponsavel(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosFornecedor(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFornecedor().getCodigo().intValue() == 0) {
            obj.setFornecedor(new FornecedorVO());
            return;
        }
        obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosFuncionario(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionario().getCodigo().intValue() == 0) {
            obj.setFuncionario(new FuncionarioVO());
            return;
        }
        obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
    }

    public static void montarDadosPessoa(GrupoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
    }

    public void validarDadosAdicionarContaPagar(ContaPagarVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        ContaPagarVO.validarDados(obj);
//        if (obj.getTipoSacado().equals("FO")) {
//            if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {
//                obj.setFuncionario(null);
//                throw new ConsistirException("O campo FORNECEDOR (Conta à Pagar) deve ser informado.");
//            }
//        } else if (obj.getTipoSacado().equals("FU")) {
//            if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
//                obj.setFornecedor(null);
//                throw new ConsistirException("O campo FUNCIONARIO (Conta à Pagar) deve ser informado.");
//            }
//        } else if (obj.getTipoSacado().equals("AL")) {
//            if (obj.getPessoa() == null || obj.getPessoa().getCodigo().equals(0)) {
//                obj.setPessoa(null);
//                throw new ConsistirException("O campo ALUNO(Conta à Pagar) deve ser informado.");
//            }
//        } else if (obj.getTipoSacado().equals("BA")) {
//            if (obj.getBanco() == null || obj.getBanco().getCodigo().equals(0)) {
//                obj.setBanco(null);
//                throw new ConsistirException("O campo BANCO(Conta à Pagar) deve ser informado.");
//            }
//        }
//
//        if (obj.getData() == null) {
//            throw new ConsistirException("O campo DATA (Conta à Pagar) deve ser informado.");
//        }
//        if (obj.getSituacao().equals("")) {
//            throw new ConsistirException("O campo SITUAÇÃO (Conta à Pagar) deve ser informado.");
//        }
//        if (obj.getDataVencimento() == null) {
//            throw new ConsistirException("O campo DATA DE VENCIMENTO (Conta à Pagar) deve ser informado.");
//        }
//        if (obj.getValor().doubleValue() == 0) {
//            throw new ConsistirException("O campo VALOR (Conta à Pagar) deve ser informado.");
//        }
//        // if (obj.getNrDocumento().equals("")) {
//        // if (obj.getTipoSacado().equals("FO")) {
//        // throw new
//        // ConsistirException("O campo NR. DOCUMENTO (Conta à Pagar) deve ser informado.");
//        // }
//        // }
//
//        if ((obj.getCentroDespesa() == null) || (obj.getCentroDespesa().getCodigo().intValue() == 0)) {
//            throw new ConsistirException("O campo CATEGORIA DE DESPESA (Conta à Pagar) deve ser informado.");
//        }
//        // if (!obj.getTipoDesconto().equals("") && obj.getDesconto() == 0.0) {
//        // throw new
//        // ConsistirException("O campo VALOR DO DESCONTO deve ser informado.");
//        // }
//        // if (obj.getTipoDesconto().equals("") && obj.getDesconto() != 0.0) {
//        // throw new
//        // ConsistirException("O campo TIPO DO DESCONTO deve ser informado.");
//        // }
//
//        if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
//            throw new ConsistirException("O campo UNIDADE DE ENSINO (Conta à Pagar) deve ser informado!");
//        }
//
//        if (obj.getTipoOrigem().equals("CP")) { // cadastrado manualmente...
//            if ((obj.getCentroDespesa().getNivelCategoriaDespesa().equals("DE"))
//                    && (obj.getDepartamento() == null || obj.getDepartamento().getCodigo().intValue() == 0)) { // O
//                // departamento
//                // deve
//                // ser
//                // informado..
//                throw new ConsistirException("O campo DEPARTAMENTO (Conta à Pagar) deve ser informado para esta CATEGORIA DE DESPESA.");
//            }
//
//            if ((obj.getCentroDespesa().getNivelCategoriaDespesa().equals("FU"))
//                    && (obj.getFuncionarioCentroCusto() == null || obj.getFuncionarioCentroCusto().getCodigo().intValue() == 0)) { // O
//                // funcionario
//                // deve
//                // ser
//                // informado..
//                throw new ConsistirException("O campo RESPONSÁVEL DESPESA (Conta à Pagar) deve ser informado para esta CATEGORIA DE DESPESA.");
//            }
//
//        }
//        if (obj.getCentroDespesa().getIsNivelAcademicoObrigatorio()) {
////			CU,CT,TU
//            if (obj.getCentroDespesa().getInformarTurma().equals("CU") && (obj.getCursoVO().getCodigo() == null || obj.getCursoVO().getCodigo() == 0)) {
//                throw new ConsistirException("O campo CURSO (Conta à Pagar) deve ser informado.");
//            }
//            if (obj.getCentroDespesa().getInformarTurma().equals("CT") && (obj.getTurnoVO().getCodigo() == null || obj.getTurnoVO().getCodigo() == 0)) {
//                throw new ConsistirException("O campo CURSO/TURNO (Conta à Pagar) deve ser informado.");
//            }
//            if (obj.getCentroDespesa().getInformarTurma().equals("TU") && (obj.getTurma().getCodigo() == null || obj.getTurma().getCodigo() == 0)) {
//                throw new ConsistirException("O campo TURMA (Conta à Pagar) deve ser informado.");
//            }
//        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return GrupoContaPagar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        GrupoContaPagar.idEntidade = idEntidade;
    }
    
    @Override
    public void realizarVinculoContaReceberComResponsavelFinanceiro(GrupoContaPagarVO contaPagarVO, UsuarioVO usuarioLogado) throws Exception {
        if (contaPagarVO.getTipoSacado().equals("AL")) {
            PessoaVO responsavelFinanceiro = getFacadeFactory().getPessoaFacade().consultarResponsavelFinanceiroAluno(contaPagarVO.getPessoa().getCodigo(), usuarioLogado);
            if (responsavelFinanceiro != null) {
                contaPagarVO.setResponsavelFinanceiro(responsavelFinanceiro);
                contaPagarVO.setTipoSacado(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
            }
        }
    }
}
