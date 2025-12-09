package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ConfiguracaoFinanceiroVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ConfiguracaoFinanceiroVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ConfiguracaoFinanceiroVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class FormaPagamentoNegociacaoRecebimentoCartaoCredito extends ControleAcesso implements FormaPagamentoNegociacaoRecebimentoCartaoCreditoInterfaceFacade {

    protected static String idEntidade;

    public FormaPagamentoNegociacaoRecebimentoCartaoCredito() throws Exception {
        super();
        setIdEntidade("FormaPagamentoNegociacaoRecebimentoCartaoCredito");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj ,UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj, usuario);
            final String sql = "INSERT INTO FormaPagamentoNegociacaoRecebimentoCartaoCredito( numeroParcela, valorParcela, dataEmissao, "
                    + "dataVencimento, dataRecebimento, situacao, responsavelPelaBaixa, tipoFinanciamento, numeroReciboTransacao, numeroCartao, "
                    + "mesValidade, anoValidade, codigoVerificacao, nomeCartaoCredito, situacaoTransacao, configuracaofinanceirocartao, contareceber, "
                    + "ajustarValorLiquido, responsavelAjustarValorLiquido ) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNumeroParcela());
                    sqlInserir.setDouble(2, obj.getValorParcela());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataEmissao()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataVencimento()));
                    if (obj.getDataRecebimento() != null) {
                        sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataRecebimento()));
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setString(6, obj.getSituacao());
                    if (obj.getResponsavelPelaBaixa().getCodigo() != null && !obj.getResponsavelPelaBaixa().getCodigo().equals(0)) {
                        sqlInserir.setInt(7, obj.getResponsavelPelaBaixa().getCodigo());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if(Uteis.isAtributoPreenchido(obj.getTipoFinanciamentoEnum())) {
                    	sqlInserir.setString(8, obj.getTipoFinanciamentoEnum().getName());
                    }
                    sqlInserir.setString(9, obj.getNumeroReciboTransacao());
                    sqlInserir.setString(10, obj.getNumeroCartao());
                    sqlInserir.setInt(11, obj.getMesValidade());
                    sqlInserir.setInt(12, obj.getAnoValidade());
                    sqlInserir.setString(13, obj.getCodigoVerificacao());
                    sqlInserir.setString(14, obj.getNomeCartaoCredito());
                    sqlInserir.setBoolean(15, obj.getSituacaoTransacao());
                    sqlInserir.setInt(16, obj.getConfiguracaoFinanceiroCartaoVO().getCodigo());
                    if(obj.getContaReceberVO().getCodigo().equals(0)) {
                    	sqlInserir.setNull(17, 0);
                    } else {
                    	sqlInserir.setInt(17, obj.getContaReceberVO().getCodigo());                    	
                    }
                    int i = 17;
                    Uteis.setValuePreparedStatement(obj.getAjustarValorLiquido(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getResponsavelAjustarValorLiquido(), ++i, sqlInserir);
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {
    			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
    				if (arg0.next()) {
    					obj.setNovoObj(Boolean.FALSE);
    					return arg0.getInt("codigo");
    				}
    				return null;
    			}
    		}));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj ,UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj, usuario);
            FormaPagamentoNegociacaoRecebimentoCartaoCredito.alterar(getIdEntidade());
            final String sql = "UPDATE FormaPagamentoNegociacaoRecebimentoCartaoCredito set numeroParcela = ?, valorParcela = ?, "
                    + "dataEmissao=?, dataVencimento = ?, dataRecebimento = ?, situacao = ?, responsavelPelaBaixa = ?, tipoFinanciamento = ?, "
                    + "numeroReciboTransacao=?, numeroCartao=?, mesValidade=?, anoValidade=?, codigoVerificacao=?, nomeCartaoCredito=?, "
                    + "situacaoTransacao=?, chavedatransacao=?, FinancialMovementKey=?, configuracaofinanceirocartao=?, splitRealizado=?, "
                    + "contareceberrecebimento=?, ajustarValorLiquido=?, responsavelAjustarValorLiquido=? "
                    + "WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNumeroParcela());
                    sqlAlterar.setDouble(2, obj.getValorParcela());
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataEmissao()));
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataVencimento()));
                    if (obj.getDataRecebimento() != null) {
                        sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataRecebimento()));
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setString(6, obj.getSituacao());
                    if (obj.getResponsavelPelaBaixa().getCodigo() != null && !obj.getResponsavelPelaBaixa().getCodigo().equals(0)) {
                        sqlAlterar.setInt(7, obj.getResponsavelPelaBaixa().getCodigo());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if(Uteis.isAtributoPreenchido(obj.getTipoFinanciamentoEnum())) {
                    	sqlAlterar.setString(8, obj.getTipoFinanciamentoEnum().getName());
                    }
                    sqlAlterar.setString(9, obj.getNumeroReciboTransacao());
                    sqlAlterar.setString(10, obj.getNumeroCartao());
                    sqlAlterar.setInt(11, obj.getMesValidade());
                    sqlAlterar.setInt(12, obj.getAnoValidade());
                    sqlAlterar.setString(13, obj.getCodigoVerificacao());
                    sqlAlterar.setString(14, obj.getNomeCartaoCredito());
                    sqlAlterar.setBoolean(15, obj.getSituacaoTransacao());
                    sqlAlterar.setString(16, obj.getChaveDaTransacao());
                    sqlAlterar.setString(17, obj.getFinancialMovementKey());
                    if(!obj.getConfiguracaoFinanceiroCartaoVO().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(18, obj.getConfiguracaoFinanceiroCartaoVO().getCodigo());                    	
                    } else {
                    	sqlAlterar.setNull(18, 0);
                    }
                    sqlAlterar.setBoolean(19, obj.getSplitRealizado());
                    sqlAlterar.setString(20, obj.getContaReceberRecebimento());
                    int i=20;
                    Uteis.setValuePreparedStatement(obj.getAjustarValorLiquido(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getResponsavelAjustarValorLiquido(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                    return sqlAlterar;
                }
            })== 0){
            	incluir(obj, usuario);
            	return;
            }
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataRecebimentoSituacaoResponsavelPelaBaixa(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj ,UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj, usuario);
            final String sql = "UPDATE FormaPagamentoNegociacaoRecebimentoCartaoCredito set dataRecebimento = ?, situacao = ?, responsavelPelaBaixa = ? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getDataRecebimento() != null) {
                        sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataRecebimento()));
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getSituacao());
                    if (obj.getResponsavelPelaBaixa().getCodigo() != null && !obj.getResponsavelPelaBaixa().getCodigo().equals(0)) {
                        sqlAlterar.setInt(3, obj.getResponsavelPelaBaixa().getCodigo());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setInt(4, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj,UsuarioVO usuario) throws Exception {
        try {
            FormaPagamentoNegociacaoRecebimentoCartaoCredito.excluir(getIdEntidade());
            String sql = "DELETE FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPelaFormaPagamentoNegociacaoRecebimento(Integer codigoFormaPagamentoNegociacaoRecebimento ,UsuarioVO usuario) throws Exception {
        try {
//            FormaPagamentoNegociacaoRecebimentoCartaoCredito.excluir(getIdEntidade());
            String sql = "DELETE FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito WHERE ( (codigo in  ( select FormaPagamentoNegociacaoRecebimentoCartaoCredito from formaPagamentoNegociacaoRecebimento where codigo = ? )) or  formaPagamentoNegociacaoRecebimento = ?) "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{codigoFormaPagamentoNegociacaoRecebimento.intValue(), codigoFormaPagamentoNegociacaoRecebimento});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO,UsuarioVO usuario) throws Exception {
        for (FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj : listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) {
            if (obj.isNovoObj().booleanValue()) {
                incluir(obj, usuario);
            } else {
                alterar(obj, usuario);
            }
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj ,UsuarioVO usuario) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuario);
        } else {
            alterar(obj, usuario);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj ,UsuarioVO usuario) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        ConsistirException consistir = new ConsistirException();
        if (TipoFormaPagamento.CARTAO_DE_DEBITO.getValor().equals(obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo())) {
        	return;
        }
        if (obj.getNumeroParcela() == null || obj.getNumeroParcela().equals("")) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimentoCartaoCredito_numeroParcela"));
        }
        if (obj.getValorParcela() == null || obj.getValorParcela().equals(0.0)) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimentoCartaoCredito_valorParcela"));
        }
        if (obj.getDataEmissao() == null) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimentoCartaoCredito_dataEmissao"));
        }
        if (obj.getDataVencimento() == null) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimentoCartaoCredito_dataVencimento"));
        }
        if (consistir.existeErroListaMensagemErro()) {
            throw consistir;
        }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     */
    public void validarUnicidade(List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> lista, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj) throws ConsistirException {
        for (FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO repetido : lista) {
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimentoCartaoCredito</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito WHERE upper( nome ) like(?) ORDER BY nome";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimentoCartaoCredito</code> através do valor do atributo
     * <code>String tipo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito ");
        sqlStr.append("INNER JOIN operadoraCartao ON operadoraCartao.codigo = FormaPagamentoNegociacaoRecebimentoCartaoCredito.operadoraCartao ");
        sqlStr.append("WHERE upper( tipo ) like(?) ORDER BY nome");
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase()), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimentoCartaoCredito</code> através do valor do atributo
     * <code>String tipo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoOperadoraCartao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE upper( operadoraCartao.tipo ) like(?) ORDER BY operadoraCartao.nome");
        return (montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase())));
    }

    /**
     * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimentoCartaoCredito</code> através do valor do atributo
     * <code>Long codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.numeroParcela as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.numeroParcela\", formaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataEmissao as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.dataEmissao\", formaPagamentoNegociacaoRecebimentoCartaoCredito.dataVencimento as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.dataVencimento\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataRecebimento as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.dataRecebimento\", formaPagamentoNegociacaoRecebimentoCartaoCredito.situacao as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.situacao\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa\" ");

        str.append("FROM formaPagamentoNegociacaoRecebimentoCartaoCredito ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.numeroParcela as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.numeroParcela\", formaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataEmissao as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.dataEmissao\", formaPagamentoNegociacaoRecebimentoCartaoCredito.dataVencimento as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.dataVencimento\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataRecebimento as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.dataRecebimento\", formaPagamentoNegociacaoRecebimentoCartaoCredito.situacao as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.situacao\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido\", ");
        str.append("formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelAjustarValorLiquido as \"formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelAjustarValorLiquido\" ");
        str.append("FROM formaPagamentoNegociacaoRecebimentoCartaoCredito ");
        return str;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (FormaPagamentoNegociacaoRecebimentoCartaoCredito.codigo= " + codigo + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (FormaPagamentoNegociacaoRecebimentoCartaoCredito.codigo= " + codigo + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    public void carregarDados(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) obj, NivelMontarDados.BASICO, usuario);
    }

    public void carregarDados(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if (obj.getIsDadosBasicosDevemSerCarregados(nivelMontarDados)) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) obj, resultado);
        }
        if (obj.getIsDadosCompletosDevemSerCarregados(nivelMontarDados)) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) obj, resultado);
        }
    }

    public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
        obj.setCodigo(codigo);
        carregarDados(obj, nivelMontarDados, usuario);
        return obj;
    }

    public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
        List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> vetResultado = new ArrayList<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO>(0);
        while (tabelaResultado.next()) {
            FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
        List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> vetResultado = new ArrayList<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO>(0);
        while (tabelaResultado.next()) {
            FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
            montarDadosCompleto(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    private void montarDadosBasico(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados da FormaPagamentoNegociacaoRecebimentoCartaoCredito
        obj.setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo"));
        obj.setNumeroParcela(dadosSQL.getString("formaPagamentoNegociacaoRecebimentoCartaoCredito.numeroParcela"));
        obj.setValorParcela(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela"));
        obj.setDataEmissao(dadosSQL.getTimestamp("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataEmissao"));
        obj.setDataVencimento(dadosSQL.getTimestamp("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataVencimento"));
        obj.setDataRecebimento(dadosSQL.getTimestamp("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataRecebimento"));
        obj.setSituacao(dadosSQL.getString("formaPagamentoNegociacaoRecebimentoCartaoCredito.situacao"));
        obj.getResponsavelPelaBaixa().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa"));
        obj.setNivelMontarDados(NivelMontarDados.BASICO);
    }

    private void montarDadosCompleto(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados da FormaPagamentoNegociacaoRecebimentoCartaoCredito
        obj.setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo"));
        obj.setNumeroParcela(dadosSQL.getString("formaPagamentoNegociacaoRecebimentoCartaoCredito.numeroParcela"));
        obj.setValorParcela(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela"));
        obj.setDataEmissao(dadosSQL.getTimestamp("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataEmissao"));
        obj.setDataVencimento(dadosSQL.getTimestamp("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataVencimento"));
        obj.setDataRecebimento(dadosSQL.getTimestamp("formaPagamentoNegociacaoRecebimentoCartaoCredito.dataRecebimento"));
        obj.setSituacao(dadosSQL.getString("formaPagamentoNegociacaoRecebimentoCartaoCredito.situacao"));
        obj.getResponsavelPelaBaixa().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa"));
        obj.setAjustarValorLiquido(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido"));
        obj.getResponsavelAjustarValorLiquido().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito.responsavelAjustarValorLiquido"));
        obj.setNivelMontarDados(NivelMontarDados.TODOS);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> resultantes da consulta.
     */
    public static List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> vetResultado = new ArrayList<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>.
     * @return  O objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code> com os dados devidamente montados.
     */
    public static FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNumeroParcela(dadosSQL.getString("numeroParcela"));
        obj.setValorParcela(dadosSQL.getDouble("valorParcela"));
        obj.setDataEmissao(dadosSQL.getTimestamp("dataEmissao"));
        obj.setDataVencimento(dadosSQL.getTimestamp("dataVencimento"));
        obj.setDataRecebimento(dadosSQL.getTimestamp("dataRecebimento"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.getResponsavelPelaBaixa().setCodigo(dadosSQL.getInt("responsavelPelaBaixa"));
        obj.setAjustarValorLiquido(dadosSQL.getDouble("ajustarValorLiquido"));
        obj.getResponsavelAjustarValorLiquido().setCodigo(dadosSQL.getInt("responsavelAjustarValorLiquido"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipofinanciamento"))) {
        	obj.setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(dadosSQL.getString("tipofinanciamento")));        	
        }
        obj.setNumeroReciboTransacao(dadosSQL.getString("numerorecibotransacao"));
        obj.setNumeroCartao(dadosSQL.getString("numerocartao"));
        obj.setMesValidade(dadosSQL.getInt("mesvalidade"));
        obj.setAnoValidade(dadosSQL.getInt("anovalidade"));
        obj.setNomeCartaoCredito(dadosSQL.getString("nomecartaocredito"));
        obj.setSituacaoTransacao(dadosSQL.getBoolean("situacaotransacao"));
        obj.setChaveDaTransacao(dadosSQL.getString("chavedatransacao"));
        obj.setFinancialMovementKey(dadosSQL.getString("financialmovementkey"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getInt("configuracaofinanceirocartao"))) {
        	obj.getConfiguracaoFinanceiroCartaoVO().setCodigo(dadosSQL.getInt("configuracaofinanceirocartao"));
        	obj.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(obj.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
        }
        obj.setSplitRealizado(dadosSQL.getBoolean("SplitRealizado"));
        obj.setContaReceberRecebimento(dadosSQL.getString("contareceberrecebimento"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getInt("contareceber"))) {
        	obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber"));        	
        	obj.setContaReceberVO(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceberVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO(), null));
        }
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }        
		obj.setListaLancamentoContabeisCredito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, TipoPlanoContaEnum.CREDITO, false, Uteis.NIVELMONTARDADOS_TODOS, null));
		obj.setListaLancamentoContabeisDebito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, TipoPlanoContaEnum.DEBITO, false, Uteis.NIVELMONTARDADOS_TODOS, null));
        return obj;
    }

    /**
     * Operação responsável por consultar todos os <code>FuncionarioCargoVO</code> relacionados a um objeto da classe <code>administrativo.funcionario</code>.
     * @param funcionario  Atributo de <code>administrativo.funcionario</code> a ser utilizado para localizar os objetos da classe <code>FuncionarioCargoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>FuncionarioCargoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarFormaPagamentoNegociacaoRecebimentoCartaoCredito(Integer configuracaoFinanceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FormaPagamentoNegociacaoRecebimentoCartaoCredito.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito WHERE configuracaoFinanceiro = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{configuracaoFinanceiro.intValue()});
        while (resultado.next()) {
            FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO novoObj = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
            novoObj = FormaPagamentoNegociacaoRecebimentoCartaoCredito.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( FormaPagamentoNegociacaoRecebimentoCartaoCredito ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> consultarUnicidade(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FormaPagamentoNegociacaoRecebimentoCartaoCredito.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        FormaPagamentoNegociacaoRecebimentoCartaoCredito.idEntidade = idEntidade;
    }
    
    public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> consultarPorFormaPagamentoNegociacaoRecebimento(Integer codigoFormaPagamentoNegociacaoRecebimento) throws Exception {
    	String sqlStr = "SELECT * FROM formapagamentonegociacaorecebimentocartaocredito WHERE  (codigo in (select formapagamentonegociacaorecebimentocartaocredito from formapagamentonegociacaorecebimento where codigo = ? ) or formapagamentonegociacaorecebimento = ? )";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoFormaPagamentoNegociacaoRecebimento, codigoFormaPagamentoNegociacaoRecebimento), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
    }
    
    @Override
    public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> consultarFormaPagamentoNegociacaoRecebimentoAReceberDCC() throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("select * from formapagamentonegociacaorecebimentocartaocredito");
    	sqlStr.append(" where formapagamentonegociacaorecebimentocartaocredito.situacao = 'AR'");
    	sqlStr.append(" and formapagamentonegociacaorecebimentocartaocredito.datavencimento = NOW()");
    	SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return (montarDadosConsulta(rowSet, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataVencimento(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj , UsuarioVO usuario) throws Exception {
        try {            
            if (obj.getDataVencimento() == null) {
                throw new Exception(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimentoCartaoCredito_dataVencimento"));
            }
            final String sql = "UPDATE FormaPagamentoNegociacaoRecebimentoCartaoCredito set dataVencimento = ? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getDataVencimento() != null) {
                        sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataVencimento()));
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }                   
                    sqlAlterar.setInt(2, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAjusteValorLiquido(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE  FormaPagamentoNegociacaoRecebimentoCartaoCredito  set ajustarValorLiquido=?, responsavelAjustarValorLiquido=? WHERE ((codigo=?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);		
		getConexao().getJdbcTemplate().update(sql, obj.getAjustarValorLiquido(), obj.getResponsavelAjustarValorLiquido().getCodigo(), obj.getCodigo());

	}
}
