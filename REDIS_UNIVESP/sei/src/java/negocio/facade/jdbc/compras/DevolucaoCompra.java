package negocio.facade.jdbc.compras;
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.CreditoFornecedorVO;
import negocio.comuns.compras.DevolucaoCompraItemVO;
import negocio.comuns.compras.DevolucaoCompraVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemCreditoFornecedor;
import negocio.comuns.utilitarias.dominios.SituacaoCreditoFornecedor;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.DevolucaoCompraInterfaceFacade;
import negocio.interfaces.financeiro.ContaPagarInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DevolucaoCompraVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>DevolucaoCompraVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see DevolucaoCompraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class DevolucaoCompra extends ControleAcesso implements DevolucaoCompraInterfaceFacade {

    protected static String idEntidade;
    protected ContaPagarInterfaceFacade contaPagarFacade;
    protected RecebimentoCompra recebimentoCompraFacade;
    protected RecebimentoCompraItem recebimentoCompraItemFacade;
    protected CreditoFornecedor creditoFornecedorFacade;
    

    public DevolucaoCompra() throws Exception {
        super();
        setIdEntidade("DevolucaoCompra");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>DevolucaoCompraVO</code>.
     */
    public DevolucaoCompraVO novo() throws Exception {
        DevolucaoCompra.incluir(getIdEntidade());
        DevolucaoCompraVO obj = new DevolucaoCompraVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DevolucaoCompraVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>DevolucaoCompraVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final DevolucaoCompraVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
        try {
            DevolucaoCompraVO.validarDados(obj);
            DevolucaoCompra.incluir(getIdEntidade(), controlarAcesso, usuario);
            obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO DevolucaoCompra( responsavel, data, compra, unidadeEnsino,totalpago,totalpagar,totalexcluir,totalnaoabatido,valortotalcredito ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? , ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getCompra().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getCompra().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setDouble(5, obj.getTotalPago());
                    sqlInserir.setDouble(6, obj.getTotalPagar());
                    sqlInserir.setDouble(7, obj.getTotalExcluir());
                    sqlInserir.setDouble(8, obj.getTotalNaoAbatido());
                    sqlInserir.setDouble(9, obj.getValorTotalCredito());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));

            getFacadeFactory().getDevolucaoCompraItemFacade().incluirDevolucaoCompraItems(obj.getCodigo(), obj.getCompra().getCodigo(), obj.getUnidadeEnsino().getCodigo(), obj.getDevolucaoCompraItemVOs(), usuario);
            if (obj.getFormaReceber().equals("alterar")) {
                Iterator<ContaPagarVO> i = obj.getConsultaContaPagar().iterator();
                while (i.hasNext()) {
                    ContaPagarVO contaPagar = i.next();
                    if (contaPagar.isExcluirContaPagar()) {
                        getFacadeFactory().getContaPagarFacade().excluir(contaPagar, false, usuario);
                    } else {
                    	contaPagar.setValor(contaPagar.getValorAlterar());
            			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(contaPagar.getListaCentroResultadoOrigemVOs(), contaPagar.getPrevisaoValorPago(), usuario);
                        getFacadeFactory().getContaPagarFacade().alterar(contaPagar, true, true, usuario);
                    }
                }
            } else if (obj.getFormaReceber().equals("mercadoria")) {
                gerarRecebimentoCompraPorDevolucaoMercadoria(obj, usuario, controlarAcesso);
            } else if (obj.getFormaReceber().equals("credito")) {
                gerarCreditoFornecedorPorDevolucaoMercadoria(obj);
            }
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

	private void gerarCreditoFornecedorPorDevolucaoMercadoria(final DevolucaoCompraVO obj) throws Exception {
		CreditoFornecedorVO creditoFornecedor = new CreditoFornecedorVO();
		creditoFornecedor.setData(obj.getData());
		creditoFornecedor.setSituacao(SituacaoCreditoFornecedor.ABERTO.getValor());
		creditoFornecedor.setUnidadeEnsino(obj.getUnidadeEnsino());
		creditoFornecedor.setFornecedor(obj.getCompra().getFornecedor());
		creditoFornecedor.setOrigem(OrigemCreditoFornecedor.DEVOLUCAO_COMPRA.getValor());
		creditoFornecedor.setCodigoOrigem(obj.getCodigo());
		creditoFornecedor.setResponsavelCadastro(obj.getResponsavel());
		Iterator j = obj.getDevolucaoCompraItemVOs().iterator();
		while (j.hasNext()) {
		    DevolucaoCompraItemVO devCompraItem = (DevolucaoCompraItemVO) j.next();
		    creditoFornecedor.setValor(creditoFornecedor.getValor() + (devCompraItem.getQuantidade() * devCompraItem.getCompraItem().getPrecoUnitario()));
		}
		creditoFornecedor.setSaldo(creditoFornecedor.getValor());
		getFacadeFactory().getCreditoFornecedorFacade().incluir(creditoFornecedor);
	}

	private void gerarRecebimentoCompraPorDevolucaoMercadoria(final DevolucaoCompraVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		RecebimentoCompraVO recCompra = new RecebimentoCompraVO("PR", obj.getCompra());
		recCompra.setResponsavel(obj.getResponsavel());
		recCompra.setData(obj.getData());
		recCompra.setUnidadeEnsino(obj.getUnidadeEnsino());
		for (DevolucaoCompraItemVO devCompraItem : obj.getDevolucaoCompraItemVOs()) {
			RecebimentoCompraItemVO recCompraItem = new RecebimentoCompraItemVO();
		    recCompraItem.setRecebimentoCompraVO(recCompra);
		    recCompraItem.setQuantidadeRecebida(devCompraItem.getQuantidade());
		    recCompraItem.setCompraItem(devCompraItem.getCompraItem());
		    recCompraItem.setValorUnitario(devCompraItem.getCompraItem().getPrecoUnitario());
		    recCompraItem.setValorTotal(devCompraItem.getCompraItem().getPrecoUnitario() * devCompraItem.getQuantidade());
		    recCompra.setValorTotal(recCompra.getValorTotal() + recCompraItem.getValorTotal());
		    recCompra.getRecebimentoCompraItemVOs().add(recCompraItem);
		}
		getFacadeFactory().getRecebimentoCompraFacade().persistir(recCompra,controlarAcesso, usuario);
	}

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DevolucaoCompraVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>DevolucaoCompraVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final DevolucaoCompraVO obj, UsuarioVO usuario) throws Exception {
        try {
            DevolucaoCompraVO.validarDados(obj);
            DevolucaoCompra.alterar(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE DevolucaoCompra set responsavel=?, data=?, compra=?, unidadeEnsino=?, totalpago =? , totalpagar =? ,totalexcluir =? ,totalnaoabatido =? ,valortotalcredito =?  WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getCompra().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getCompra().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setDouble(5, obj.getTotalPago());
                    sqlAlterar.setDouble(6, obj.getTotalPagar());
                    sqlAlterar.setDouble(7, obj.getTotalExcluir());
                    sqlAlterar.setDouble(8, obj.getTotalNaoAbatido());
                    sqlAlterar.setDouble(9, obj.getValorTotalCredito());
                    sqlAlterar.setInt(10, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

            getFacadeFactory().getDevolucaoCompraItemFacade().alterarDevolucaoCompraItems(obj.getCodigo(), obj.getCompra().getCodigo(), obj.getUnidadeEnsino().getCodigo(), obj.getDevolucaoCompraItemVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DevolucaoCompraVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>DevolucaoCompraVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(DevolucaoCompraVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            DevolucaoCompra.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM DevolucaoCompra WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getDevolucaoCompraItemFacade().excluirDevolucaoCompraItems(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<DevolucaoCompraVO> consultarPorNomeFornecedor(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DevolucaoCompra.* " +
                "from devolucaocompra " +
                "inner join compra on compra.codigo = devolucaocompra.compra " +
                "inner join fornecedor on compra.fornecedor = fornecedor.codigo " +
                "where upper(sem_acentos(fornecedor.nome)) like upper(sem_acentos(?)) " +
                "and devolucaocompra.data between '" + Uteis.getDataJDBC(dataIni) + "' and '" + Uteis.getDataJDBC(dataFim) + "'";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += "and devolucaocompra.unidadeensino = " + unidadeEnsino.intValue();
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return montarDadosConsulta(resultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCompra</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>Compra</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoCompraVO> consultarPorCodigoCompra(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DevolucaoCompra.* FROM DevolucaoCompra, Compra WHERE DevolucaoCompra.compra = Compra.codigo and Compra.codigo >= " + valorConsulta.intValue();
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT DevolucaoCompra.* FROM DevolucaoCompra, Compra WHERE DevolucaoCompra.compra = Compra.codigo and Compra.codigo >= " + valorConsulta.intValue() + " and DevolucaoCompra.unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND DevolucaoCompra.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND DevolucaoCompra.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY Compra.codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCompra</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    /*public List consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados) throws Exception {
    ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    String sqlStr = "SELECT * FROM DevolucaoCompra WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
    if (unidadeEnsino.intValue() != 0) {
    sqlStr = "SELECT * FROM DevolucaoCompra WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY data";
    }
    Statement stm = con.createStatement();
    ResultSet tabelaResultado = stm.executeQuery(sqlStr);
    return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCompra</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoCompraVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DevolucaoCompra WHERE codigo >= " + valorConsulta.intValue();
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM DevolucaoCompra WHERE codigo >= " + valorConsulta.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND DevolucaoCompra.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND DevolucaoCompra.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraVO</code> resultantes da consulta.
     */
    public static List<DevolucaoCompraVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DevolucaoCompraVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>DevolucaoCompraVO</code>.
     * @return  O objeto da classe <code>DevolucaoCompraVO</code> com os dados devidamente montados.
     */
    public static DevolucaoCompraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DevolucaoCompraVO obj = new DevolucaoCompraVO();
        obj.setNovoObj(false);
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCompra().setCodigo(new Integer(dadosSQL.getInt("compra")));
        obj.setData(dadosSQL.getTimestamp("data"));
        montarDadosCompra(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
        	return obj;
        }
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setTotalExcluir(dadosSQL.getDouble("totalexcluir"));
        obj.setTotalPagar(dadosSQL.getDouble("totalpagar"));
        obj.setTotalPago(dadosSQL.getDouble("totalpago"));
        obj.setTotalNaoAbatido(dadosSQL.getDouble("totalNaoAbatido"));
        obj.setValorTotalCredito(dadosSQL.getDouble("valortotalcredito"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setDevolucaoCompraItemVOs(DevolucaoCompraItem.consultarDevolucaoCompraItems(obj.getCodigo(), nivelMontarDados, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CompraVO</code> relacionado ao objeto <code>DevolucaoCompraVO</code>.
     * Faz uso da chave primária da classe <code>CompraVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCompra(DevolucaoCompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCompra().getCodigo().intValue() == 0) {
            obj.setCompra(new CompraVO());
            return;
        }
        obj.setCompra(getFacadeFactory().getCompraFacade().consultarPorChavePrimaria(obj.getCompra().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosUnidadeEnsino(DevolucaoCompraVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>DevolucaoCompraVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(DevolucaoCompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DevolucaoCompraVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public DevolucaoCompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM DevolucaoCompra WHERE codigo = ?";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( DevolucaoCompra ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DevolucaoCompra.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        DevolucaoCompra.idEntidade = idEntidade;
    }
    
    /**
	 * Operação que sugere o valor das contas a pagar após as alterações nas contas. Está operação mantém o valor total de credito que no caso na tela de compra e o valor do Preço total que varia de acordo com os itens selecionados para a devolução, com isso o valorCredito aumenta. A sugestão de parcelas ocorre a partir do valorCredito que sempre e dividido pelo numero de parcelas, caso o usuário opte por não ter nenhuma parcela, então o valorCredito passa a ser uma única parcela.
	 */
	public String sugerirValorParcelas(DevolucaoCompraVO devolucaoCompraVO, CreditoFornecedorVO creditoFornecedorVO) throws Exception {
		calcularValorCredito(devolucaoCompraVO,creditoFornecedorVO);
		Integer numParcelas = devolucaoCompraVO.getConsultaContaPagar().size();
		Double valorCredito = devolucaoCompraVO.getValorTotalCredito();
		Double valorParcela = 0.0;
		Double valorParcelaAbater = 0.0;
		Double valorParcelaTotal = 0.0;
		Double valorPago = 0.0;
		Double valorDiferencaPagarAlterado = 0.0;
		devolucaoCompraVO.setTotalPagar(0.0);
		devolucaoCompraVO.setTotalNaoAbatido(0.0);
		if (!Uteis.isAtributoPreenchido(devolucaoCompraVO.getDevolucaoCompraItemVOs())) {
			throw new Exception(UteisJSF.internacionalizar("msg_DevolucaoCompra"));
		}
		Iterator i = devolucaoCompraVO.getConsultaContaPagar().iterator();
		while (i.hasNext()) {
			ContaPagarVO contaPagar = (ContaPagarVO) i.next();
			if (contaPagar.isExcluirContaPagar() || contaPagar.getSituacao().equals("PA")) {
				valorParcelaAbater += contaPagar.getValor();
				numParcelas--;
			} else if (!contaPagar.isExcluirContaPagar() && contaPagar.getSituacao().equals("AP")) {
				devolucaoCompraVO.setTotalPagar(devolucaoCompraVO.getTotalPagar() + contaPagar.getValorAlterar());
				if (contaPagar.getValorAlterar() < contaPagar.getValor()) {
					valorDiferencaPagarAlterado += contaPagar.getValor() - contaPagar.getValorAlterar();
				}
			}
			if (contaPagar.getSituacao().equals("PA")) {
				valorPago += contaPagar.getValor();
			}
			valorParcelaTotal += contaPagar.getValor();
		}
		if (numParcelas == 0 && valorParcelaAbater < valorParcelaTotal) {
			numParcelas = 1;
		}
		devolucaoCompraVO.setTotalExcluir(valorParcelaAbater);
		devolucaoCompraVO.setTotalPago(valorPago);
		creditoFornecedorVO.setValor(valorCredito);
		if (valorCredito - devolucaoCompraVO.getTotalExcluir() - valorDiferencaPagarAlterado >= 0) {
			devolucaoCompraVO.setTotalNaoAbatido(valorCredito - devolucaoCompraVO.getTotalExcluir() - valorDiferencaPagarAlterado);
		}
		if (numParcelas > 0 && devolucaoCompraVO.getTotalPagar() > 0) {
			if (devolucaoCompraVO.getTotalPagar() > devolucaoCompraVO.getTotalNaoAbatido() && devolucaoCompraVO.getTotalNaoAbatido() > 0) {
				valorParcela = (devolucaoCompraVO.getTotalPagar() - devolucaoCompraVO.getTotalNaoAbatido()) / numParcelas;
			} else {
				valorParcela = (valorParcelaTotal - devolucaoCompraVO.getValorTotalCredito()) / numParcelas;
			}
			if (valorParcela <= 0.0) {
				return ("Excluir todas a pagar");
			} else {
				return (numParcelas + "x " + Uteis.formatarDecimalDuasCasas(valorParcela));
			}
		} else {
			return ("");
		}
	}
	/**
	 * Operação que calcula o valor do Crédito aquirido com a devolução de itens de compra.
	 */
	public void calcularValorCredito(DevolucaoCompraVO devolucaoCompraVO, CreditoFornecedorVO creditoFornecedorVO) {
		Iterator i = devolucaoCompraVO.getDevolucaoCompraItemVOs().iterator();
		creditoFornecedorVO.setValor(0.0);
		devolucaoCompraVO.setValorTotalCredito(0.0);
		while (i.hasNext()) {
			DevolucaoCompraItemVO devCompraItem = (DevolucaoCompraItemVO) i.next();
			creditoFornecedorVO.setValor(creditoFornecedorVO.getValor() + (devCompraItem.getQuantidade() * devCompraItem.getCompraItem().getPrecoUnitario()));
			devolucaoCompraVO.setValorTotalCredito(devolucaoCompraVO.getValorTotalCredito() + (devCompraItem.getQuantidade() * devCompraItem.getCompraItem().getPrecoUnitario()));
		}
	}
}