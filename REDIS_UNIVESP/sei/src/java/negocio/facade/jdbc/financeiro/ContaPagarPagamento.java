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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ContaPagarPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContaPagarPagamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContaPagarPagamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContaPagarPagamentoVO
 * @see ControleAcesso
 * @see ContaPagar
 */
@Repository
@Scope("singleton")
@Lazy 
public class ContaPagarPagamento extends ControleAcesso implements ContaPagarPagamentoInterfaceFacade{

    protected static String idEntidade;

    public ContaPagarPagamento() throws Exception {
        super();
        setIdEntidade("ContaPagar");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaPagarPagamentoVO</code>.
     */
    public ContaPagarPagamentoVO novo() throws Exception {
        ContaPagarPagamento.incluir(getIdEntidade());
        ContaPagarPagamentoVO obj = new ContaPagarPagamentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaPagarPagamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaPagarPagamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaPagarPagamentoVO obj, UsuarioVO usuario) throws Exception {
        ContaPagarPagamentoVO.validarDados(obj);
        //  ContaPagarPagamento.incluir(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "INSERT INTO ContaPagarPagamento( contaPagar, data, formaPagamentoNegociacaoPagamento, valorTotalPagamento, formaPagamento"
                + ", tipoPagamento, motivo, negociacaoPagamento, responsavel, "
                + " numeroCheque, bancoCheque, agenciaCheque, contaCorrenteCheque, sacadoCheque, dataPrevisaoCheque, dataEmissaoCheque, valorCheque "
                + ") VALUES ( ?, ?, ?, ? ,? ,? ,? ,? ,?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer)getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                if (obj.getContaPagar().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getContaPagar().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
                if (obj.getPagamento().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getPagamento().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                sqlInserir.setDouble(4, obj.getValorTotalPagamento().doubleValue());
                if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(5, obj.getFormaPagamento().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(5, 0);
                }
                sqlInserir.setString(6, obj.getTipoPagamento());
                sqlInserir.setString(7, obj.getMotivo());
                if (obj.getNegociacaoPagamento().intValue() != 0) {
                    sqlInserir.setInt(8, obj.getNegociacaoPagamento().intValue());
                } else {
                    sqlInserir.setNull(8, 0);
                }
                if (obj.getResponsavel().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(9, obj.getResponsavel().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(9, 0);
                }
              //Dados do Cheque
                sqlInserir.setString(10, obj.getNumero());
                sqlInserir.setString(11, obj.getBanco());
                sqlInserir.setString(12, obj.getAgencia());
                sqlInserir.setString(13, obj.getContaCorrente().getNumero());
                sqlInserir.setString(14, obj.getSacado());
                sqlInserir.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataPrevisao()));
                sqlInserir.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataEmissao()));
                sqlInserir.setDouble(17, obj.getValor());
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

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaPagarPagamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaPagarPagamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaPagarPagamentoVO obj, UsuarioVO usuario) throws Exception {
        ContaPagarPagamentoVO.validarDados(obj);
        //   ContaPagarPagamento.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "UPDATE ContaPagarPagamento set contaPagar=?, data=?, formaPagamentoNegociacaoPagamento = ?, valorTotalPagamento = ?, formaPagamento = ?"
                + ", tipoPagamento = ?, motivo = ?, negociacaoPagamento= ?, responsavel= ?, "
                + " numeroCheque = ?, bancoCheque = ?, agenciaCheque = ?, contaCorrenteCheque = ?, sacadoCheque = ?, dataPrevisaoCheque = ?, dataEmissaoCheque = ?, valorCheque = ? "
                + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (obj.getContaPagar().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getContaPagar().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
                if (obj.getPagamento().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getPagamento().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                sqlAlterar.setDouble(4, obj.getValorTotalPagamento().doubleValue());
                if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(5, obj.getFormaPagamento().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(5, 0);
                }
                sqlAlterar.setString(6, obj.getTipoPagamento());
                sqlAlterar.setString(7, obj.getMotivo());
                if (obj.getNegociacaoPagamento().intValue() != 0) {
                    sqlAlterar.setInt(8, obj.getNegociacaoPagamento().intValue());
                } else {
                    sqlAlterar.setNull(8, 0);
                }
                if (obj.getResponsavel().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(9, obj.getResponsavel().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(9, 0);
                }
              //Dados do Cheque
                sqlAlterar.setString(10, obj.getNumero());
                sqlAlterar.setString(11, obj.getBanco());
                sqlAlterar.setString(12, obj.getAgencia());
                sqlAlterar.setString(13, obj.getContaCorrente().getNumero());
                sqlAlterar.setString(14, obj.getSacado());
                sqlAlterar.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataPrevisao()));
                sqlAlterar.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataEmissao()));
                sqlAlterar.setDouble(17, obj.getValor());
                sqlAlterar.setInt(18, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaPagarPagamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaPagarPagamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaPagarPagamentoVO obj, UsuarioVO usuario) throws Exception {
        //  ContaPagarPagamento.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaPagarPagamento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagarPagamento</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaPagarPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaPagarPagamento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagarPagamento</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>ContaPagar</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContaPagarPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoContaPagar(Integer valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        //   ControleAcesso.consultar(getIdEntidade(), true);
        String sqlStr = "SELECT ContaPagarPagamento.* FROM ContaPagarPagamento, ContaPagar WHERE ContaPagarPagamento.contaPagar = ContaPagar.codigo and ContaPagar.codigo >= " + valorConsulta.intValue() + " ORDER BY ContaPagar.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaPagarPagamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaPagarPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaPagarPagamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorPagamento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaPagarPagamento WHERE formaPagamentoNegociacaoPagamento = " + valorConsulta.intValue() + " ORDER BY formaPagamentoNegociacaoPagamento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<ContaPagarPagamentoVO> consultarPorContaPagarParaGeracaoLancamentoContabil(Integer contaPagar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder(" select contapagarpagamento.valorTotalPagamento, contapagarpagamento.data, ");
		sb.append(" contapagarpagamento.formapagamentonegociacaopagamento,  ");
		sb.append(" formapagamentonegociacaopagamento.contacorrente,   ");
		sb.append(" formapagamentonegociacaopagamento.formapagamento,  ");
		sb.append(" formapagamento.tipo  ");
		sb.append(" from contapagar ");
		sb.append(" inner join contapagarpagamento on contapagarpagamento.contapagar = contapagar.codigo  ");
		sb.append(" inner join formapagamentonegociacaopagamento on contapagarpagamento.formapagamentonegociacaopagamento = formapagamentonegociacaopagamento.codigo ");
		sb.append(" inner join formapagamento on formapagamentonegociacaopagamento.formapagamento = formapagamento.codigo ");
		sb.append(" where contapagar.codigo = ").append(contaPagar);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ContaPagarPagamentoVO> vetResultado = new ArrayList<>(0);
		while (dadosSQL.next()) {
			ContaPagarPagamentoVO obj = new ContaPagarPagamentoVO();
			obj.setValorTotalPagamento((dadosSQL.getDouble("valorTotalPagamento")));
			obj.setData(dadosSQL.getDate("data"));
			obj.getFormaPagamentoNegociacaoPagamentoVO().setCodigo((dadosSQL.getInt("formapagamentonegociacaopagamento")));
			obj.getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente().setCodigo((dadosSQL.getInt("contacorrente")));
			obj.getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().setCodigo((dadosSQL.getInt("formapagamento")));
			obj.getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().setTipo((dadosSQL.getString("tipo")));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
    
    

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContaPagarPagamentoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ContaPagarPagamentoVO</code>.
     * @return  O objeto da classe <code>ContaPagarPagamentoVO</code> com os dados devidamente montados.
     */
    public static ContaPagarPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ContaPagarPagamentoVO obj = new ContaPagarPagamentoVO();
        obj.setData(dadosSQL.getTimestamp("data"));
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setContaPagar(new Integer(dadosSQL.getInt("contaPagar")));
        obj.setValorTotalPagamento(new Double(dadosSQL.getDouble("valorTotalPagamento")));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formaPagamento")));
        obj.setPagamento(new Integer(dadosSQL.getInt("formaPagamentoNegociacaoPagamento")));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.setTipoPagamento(dadosSQL.getString("tipoPagamento"));
        obj.setNegociacaoPagamento(new Integer(dadosSQL.getInt("NegociacaoPagamento")));
        
        //Dados Cheque
        obj.setNumero(dadosSQL.getString("numeroCheque"));
        obj.setBanco(dadosSQL.getString("bancoCheque"));
        obj.setAgencia(dadosSQL.getString("agenciaCheque"));
        obj.getContaCorrente().setNumero(dadosSQL.getString("contaCorrenteCheque"));
        obj.setSacado(dadosSQL.getString("sacadoCheque"));
        obj.setDataPrevisao(dadosSQL.getDate("dataPrevisaoCheque"));
        obj.setDataEmissao(dadosSQL.getDate("dataEmissaoCheque"));
        obj.setValor(dadosSQL.getDouble("valorCheque"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosFormaPagamento(obj, nivelMontarDados, usuario);

        return obj;
    }

    public static void montarDadosResponsavel(ContaPagarPagamentoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel() == null || obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosFormaPagamento(ContaPagarPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFormaPagamento() == null || obj.getFormaPagamento().getCodigo().intValue() == 0) {
            obj.setFormaPagamento(new FormaPagamentoVO());
            return;
        }
        obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaPagarPagamentoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaPagarPagamento</code>.
     * @param <code>contaPagar</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContaPagarPagamentos(Integer contaPagar, UsuarioVO usuario) throws Exception {
        //   ContaPagarPagamento.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaPagarPagamento WHERE (contaPagar = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{contaPagar});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaPagarPagamentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaPagarPagamentos</code> e <code>incluirContaPagarPagamentos</code> disponíveis na classe <code>ContaPagarPagamento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarContaPagarPagamentos(Integer contaPagar, List objetos, UsuarioVO usuario) throws Exception {
//        String str = "DELETE FROM ContaPagarPagamento WHERE contaPagar = " + contaPagar;
//        Iterator i = objetos.iterator();
//        while (i.hasNext()) {
//            ContaPagarPagamentoVO objeto = (ContaPagarPagamentoVO) i.next();
//            str += " AND codigo <> " + objeto.getCodigo().intValue();
//        }
//        PreparedStatement sqlExcluir = con.prepareStatement(str);
//        sqlExcluir.execute();
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ContaPagarPagamentoVO objeto = (ContaPagarPagamentoVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.setContaPagar(contaPagar);
                incluir(objeto, usuario);
            }
//                else {
//                alterar(objeto);
//            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ContaPagarPagamentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ContaPagar</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirContaPagarPagamentos(Integer contaPagarPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ContaPagarPagamentoVO obj = (ContaPagarPagamentoVO) e.next();
            obj.setContaPagar(contaPagarPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, usuario);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ContaPagarPagamentoVO</code> relacionados a um objeto da classe <code>financeiro.ContaPagar</code>.
     * @param contaPagar  Atributo de <code>financeiro.ContaPagar</code> a ser utilizado para localizar os objetos da classe <code>ContaPagarPagamentoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ContaPagarPagamentoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarContaPagarPagamentos(Integer contaPagar, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        //   ContaPagarPagamento.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ContaPagarPagamento WHERE contaPagar = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{contaPagar});
        while (resultado.next()) {
            ContaPagarPagamentoVO novoObj = new ContaPagarPagamentoVO();
            novoObj = ContaPagarPagamento.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaPagarPagamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaPagarPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContaPagarPagamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaPagarPagamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContaPagarPagamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContaPagarPagamento.idEntidade = idEntidade;
    }
}
