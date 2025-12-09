package negocio.facade.jdbc.compras;

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
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.DevolucaoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.DevolucaoCompraItemInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DevolucaoCompraItemVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>DevolucaoCompraItemVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see DevolucaoCompraItemVO
 * @see ControleAcesso
 * @see DevolucaoCompra
 */
@Repository
@Scope("singleton")
@Lazy 
public class DevolucaoCompraItem extends ControleAcesso implements DevolucaoCompraItemInterfaceFacade{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6256308446839965077L;
	protected static String idEntidade;

    public DevolucaoCompraItem() throws Exception {
        super();
        setIdEntidade("DevolucaoCompra");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>DevolucaoCompraItemVO</code>.
     */
    public DevolucaoCompraItemVO novo() throws Exception {
        DevolucaoCompraItem.incluir(getIdEntidade());
        DevolucaoCompraItemVO obj = new DevolucaoCompraItemVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>DevolucaoCompraItemVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final DevolucaoCompraItemVO obj, Integer compra, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        DevolucaoCompraItemVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle
         * Comentado 27/10/2014
         */
        // DevolucaoCompraItem.incluir(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "INSERT INTO DevolucaoCompraItem( devolucaoCompra, compraItem, quantidade, motivo ) VALUES ( ?, ?, ?, ?) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getDevolucaoCompra().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getDevolucaoCompra().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setInt(2, obj.getCompraItem().getCodigo().intValue());
                sqlInserir.setInt(3, obj.getQuantidade().intValue());
                sqlInserir.setString(4, obj.getMotivo());
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

        getFacadeFactory().getDevolucaoCompraItemImagemFacade().incluirDevolucaoCompraItemImagems(obj.getCodigo(), obj.getDevolucaoCompraItemImagemVOs());
        subtrairQuantidadeRecebidaItemCompra(obj, usuario);
        retirarProdutoEstoque(obj, compra, unidadeEnsino, usuario);
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void retirarProdutoEstoque(DevolucaoCompraItemVO obj, Integer compra, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        if (obj.getCompraItem().getProduto().getControlarEstoque()) {
            Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.DEVOLUCAO_COMPRA_ITEM, obj.getCompraItem().getProduto().getCodigo(), obj.getQuantidade().doubleValue(), obj.getCompraItem().getPrecoUnitario(), null, unidadeEnsino, OperacaoEstoqueEnum.EXCLUIR, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarItemAoRecebimentoCompra(DevolucaoCompraItemVO obj, Integer compra, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        RecebimentoCompraVO recCompra = getFacadeFactory().getRecebimentoCompraFacade().consultarPorSituacaoPrevisao(compra, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        if (recCompra.getCodigo().intValue() != 0) {
            alterarRecebimentoCompraItem(obj, recCompra, usuario);
        } else {
            throw new Exception("Não existe no sistema nenhum registro de recebimento do item da compra a ser devolvida!");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarRecebimentoCompraItem(DevolucaoCompraItemVO obj, RecebimentoCompraVO recCompra, UsuarioVO usuario) throws Exception {
        for (RecebimentoCompraItemVO item : recCompra.getRecebimentoCompraItemVOs()) {
        	if (item.getCompraItem().getProduto().getCodigo().intValue() == obj.getCompraItem().getProduto().getCodigo()) {
                item.setQuantidadeRecebida(item.getQuantidadeRecebida() + obj.getQuantidade());
                item.setValorTotal(Uteis.arrendondarForcando2CadasDecimais(item.getValorUnitario() * item.getQuantidadeRecebida()));
                getFacadeFactory().getRecebimentoCompraItemFacade().persistir(item,false,usuario);
                return;
            }	
		}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void subtrairQuantidadeRecebidaItemCompra(DevolucaoCompraItemVO obj, UsuarioVO usuario) throws Exception {
        List<CompraItemVO> item = getFacadeFactory().getCompraItemFacade().consultarPorCodigo(obj.getCompraItem().getCodigo().intValue(), true, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        Iterator<CompraItemVO> i = item.iterator();
        while (i.hasNext()) {
            CompraItemVO itemCompra = i.next();
            itemCompra.setQuantidadeRecebida(itemCompra.getQuantidadeRecebida() - obj.getQuantidade().intValue());
            getFacadeFactory().getCompraItemFacade().persistir(itemCompra, usuario);
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final DevolucaoCompraItemVO obj) throws Exception {
        DevolucaoCompraItemVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle
         * Comentado 27/10/2014
         */
        // DevolucaoCompraItem.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "UPDATE DevolucaoCompraItem set devolucaoCompra=?, compraItem=?, quantidade=?, motivo=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getDevolucaoCompra().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getDevolucaoCompra().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setInt(2, obj.getCompraItem().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getQuantidade().intValue());
                sqlAlterar.setString(4, obj.getMotivo());
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });

        getFacadeFactory().getDevolucaoCompraItemImagemFacade().alterarDevolucaoCompraItemImagems(obj.getCodigo(), obj.getDevolucaoCompraItemImagemVOs());
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(DevolucaoCompraItemVO obj) throws Exception {
    	 /**
         * @author Leonardo Riciolle
         * Comentado 27/10/2014
         */
        // DevolucaoCompraItem.excluir(getIdEntidade());
        String sql = "DELETE FROM DevolucaoCompraItem WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        getFacadeFactory().getDevolucaoCompraItemImagemFacade().excluirDevolucaoCompraItemImagems(obj.getCodigo());
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCompraItem</code> através do valor do atributo 
     * <code>Integer compraItem</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCompraItem(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DevolucaoCompraItem WHERE compraItem >= " + valorConsulta.intValue() + " ORDER BY compraItem";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCompraItem</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>DevolucaoCompra</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigoDevolucaoCompra(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT DevolucaoCompraItem.* FROM DevolucaoCompraItem, DevolucaoCompra WHERE DevolucaoCompraItem.devolucaoCompra = DevolucaoCompra.codigo and DevolucaoCompra.codigo >= " + valorConsulta.intValue() + " ORDER BY DevolucaoCompra.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCompraItem</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DevolucaoCompraItem WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * @return  O objeto da classe <code>DevolucaoCompraItemVO</code> com os dados devidamente montados.
     */
    public static DevolucaoCompraItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DevolucaoCompraItemVO obj = new DevolucaoCompraItemVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDevolucaoCompra(new Integer(dadosSQL.getInt("devolucaoCompra")));
        obj.getCompraItem().setCodigo(new Integer(dadosSQL.getInt("compraItem")));
        obj.setQuantidade(new Double(dadosSQL.getDouble("quantidade")));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosCompraItem(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosCompraItem(DevolucaoCompraItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCompraItem().getCodigo().intValue() == 0) {
            obj.setCompraItem(new CompraItemVO());
            return;
        }
        obj.setCompraItem(getFacadeFactory().getCompraItemFacade().consultarPorChavePrimaria(obj.getCompraItem().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>DevolucaoCompraItemVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>DevolucaoCompraItem</code>.
     * @param <code>devolucaoCompra</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluirDevolucaoCompraItems(Integer devolucaoCompra) throws Exception {
        DevolucaoCompraItem.excluir(getIdEntidade());
        String sql = "DELETE FROM DevolucaoCompraItem WHERE (devolucaoCompra = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{devolucaoCompra});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>DevolucaoCompraItemVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirDevolucaoCompraItems</code> e <code>incluirDevolucaoCompraItems</code> disponíveis na classe <code>DevolucaoCompraItem</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterarDevolucaoCompraItems(Integer devolucaoCompra, Integer compra, Integer unidadeEnsino, List objetos, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM DevolucaoCompraItem WHERE devolucaoCompra = " + devolucaoCompra;
        for (DevolucaoCompraItemVO obj : (List<DevolucaoCompraItemVO>) objetos) {
            if (obj.getCodigo().intValue() != 0) {
                sql += " and codigo <> " + obj.getCodigo().intValue();
            }
        }
        getConexao().getJdbcTemplate().update(sql);
        for (DevolucaoCompraItemVO obj : (List<DevolucaoCompraItemVO>) objetos) {
            obj.setDevolucaoCompra(compra);
            if (obj.getCodigo().intValue() != 0) {
                alterar(obj);
            } else {
                incluir(obj, compra, unidadeEnsino, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>DevolucaoCompraItemVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>compra.DevolucaoCompra</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDevolucaoCompraItems(Integer devolucaoCompraPrm, Integer compra, Integer unidadeEnsino, List<DevolucaoCompraItemVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<DevolucaoCompraItemVO> e = objetos.iterator();
        while (e.hasNext()) {
            DevolucaoCompraItemVO obj = e.next();
            obj.setDevolucaoCompra(devolucaoCompraPrm);
            incluir(obj, compra, unidadeEnsino, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>DevolucaoCompraItemVO</code> relacionados a um objeto da classe <code>compra.DevolucaoCompra</code>.
     * @param devolucaoCompra  Atributo de <code>compra.DevolucaoCompra</code> a ser utilizado para localizar os objetos da classe <code>DevolucaoCompraItemVO</code>.
     * @return List  Contendo todos os objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @SuppressWarnings("static-access")
    public static List consultarDevolucaoCompraItems(Integer devolucaoCompra, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DevolucaoCompraItem.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DevolucaoCompraItem WHERE devolucaoCompra = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{devolucaoCompra});
        while (resultado.next()) {
            DevolucaoCompraItemVO novoObj = new DevolucaoCompraItemVO();
            novoObj = DevolucaoCompraItem.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DevolucaoCompraItemVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public DevolucaoCompraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DevolucaoCompraItem WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( DevolucaoCompraItem ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DevolucaoCompraItem.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        DevolucaoCompraItem.idEntidade = idEntidade;
    }

	
}