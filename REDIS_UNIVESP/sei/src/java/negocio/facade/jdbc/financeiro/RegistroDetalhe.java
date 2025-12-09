package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.enumerador.SituacaoRegistroDetalheEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroDetalheInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RegistroDetalheVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>RegistroDetalheVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see RegistroDetalheVO
 * @see ControleAcesso
 * @see RegistroArquivo
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroDetalhe extends ControleAcesso implements RegistroDetalheInterfaceFacade {

    protected static String idEntidade;

    public RegistroDetalhe() throws Exception {
        super();
        setIdEntidade("RegistroArquivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>RegistroDetalheVO</code>.
     */
    public RegistroDetalheVO novo() throws Exception {
        RegistroDetalhe.incluir(getIdEntidade());
        RegistroDetalheVO obj = new RegistroDetalheVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroDetalheVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RegistroDetalheVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroDetalheVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 29/10/2014
        	  *  Classe Subordinada
        	  */ 
            // RegistroDetalhe.incluir(getIdEntidade());
            RegistroDetalheVO.validarDados(obj);
            final String sql = "INSERT INTO RegistroDetalhe( registroArquivo, codigoBanco, loteServico, tipoRegistro, "
                    + "numeroSequencialRegistroLote, codigoSeguimentoRegistroDetalhe, codigoMovimentoRemessaRetorno, sacadoTipoInscricao, "
                    + "sacadoNumeroInscricaoCorpo, sacadoNumeroInscricaoFilial, sacadoNumeroInscricaoControle, sacadoNome, sacadoEndereco, "
                    + "sacadoBairro, sacadoCep, sacadoSufixoCep, sacadoCidade, sacadoUnidadeFederacao, sacadoBancoCodigo, sacadoAgenciaCodigo, "
                    + "cedenteTipoInscricaoEmpresa, cedenteNumeroInscricaoEmpresa, cedenteIdentificacaoNoBanco, cedenteNumeroAgencia, "
                    + "cedenteNumeroConta, carteira, codigoCarteira, tipoCarteira, identificacaoOcorrencia, indicadorRateioCredito, "
                    + "numeroIdentificacao1, numeroIdentificacao2, identificaoEmissaoBloqueto, numeroDocumentoCobranca, valorNominalTitulo, "
                    + "especieTitulo, identificacaoAceite, dataEmissaoTitulo, codigoJurosMora, dataJurosMora, jurosMora, codigoDesconto, "
                    + "dataDesconto, desconto, valorAbatimento, identificacaoTituloEmpresa, identificacaoTituloBanco, codigoProtesto, "
                    + "numeroDiasProtesto, codigoMoeda, numeroContrato, dataVencimentoTitulo, numeroContratoCobrancaRegistro, motivoOcorrencia, "
                    + "confirmacaoInstituicaoProtesto, motivoRegeicao, acrescimos, valorDesconto, valorPago, valorLiquido, valorDespesas, "
                    + "valorOutrasDespesas, valorOutrosCreditos, valorIOF, dataOcorrencia, dataCredito, boletoNaoEncontrado, boletoBaixado, codigoContaReceber, "
                    + "tarifaCobranca, situacaoRegistroDetalhe ) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getRegistroArquivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getRegistroArquivo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setString(2, obj.getCodigoBanco());
                    sqlInserir.setInt(3, obj.getLoteServico().intValue());
                    sqlInserir.setInt(4, obj.getTipoRegistro().intValue());
                    sqlInserir.setInt(5, obj.getNumeroSequencialRegistroLote().intValue());
                    sqlInserir.setString(6, obj.getCodigoSeguimentoRegistroDetalhe());
                    sqlInserir.setInt(7, obj.getCodigoMovimentoRemessaRetorno().intValue());
                    sqlInserir.setInt(8, obj.getSacadoTipoInscricao().intValue());
                    sqlInserir.setInt(9, obj.getSacadoNumeroInscricaoCorpo().intValue());
                    sqlInserir.setInt(10, obj.getSacadoNumeroInscricaoFilial().intValue());
                    sqlInserir.setString(11, obj.getSacadoNumeroInscricaoControle());
                    sqlInserir.setString(12, obj.getSacadoNome());
                    sqlInserir.setString(13, obj.getSacadoEndereco());
                    sqlInserir.setString(14, obj.getSacadoBairro());
                    sqlInserir.setInt(15, obj.getSacadoCep().intValue());
                    sqlInserir.setInt(16, obj.getSacadoSufixoCep().intValue());
                    sqlInserir.setString(17, obj.getSacadoCidade());
                    sqlInserir.setString(18, obj.getSacadoUnidadeFederacao());
                    sqlInserir.setString(19, obj.getSacadoBancoCodigo());
                    sqlInserir.setString(20, obj.getSacadoAgenciaCodigo());
                    sqlInserir.setInt(21, obj.getCedenteTipoInscricaoEmpresa().intValue());
                    sqlInserir.setString(22, obj.getCedenteNumeroInscricaoEmpresa());
                    sqlInserir.setString(23, obj.getCedenteIdentificacaoNoBanco());
                    sqlInserir.setString(24, obj.getCedenteNumeroAgencia());
                    sqlInserir.setString(25, obj.getCedenteNumeroConta());
                    sqlInserir.setInt(26, obj.getCarteira().intValue());
                    sqlInserir.setInt(27, obj.getCodigoCarteira().intValue());
                    sqlInserir.setInt(28, obj.getTipoCarteira().intValue());
                    sqlInserir.setInt(29, obj.getIdentificacaoOcorrencia().intValue());
                    sqlInserir.setString(30, obj.getIndicadorRateioCredito());
                    sqlInserir.setDouble(31, obj.getNumeroIdentificacao1().doubleValue());
                    sqlInserir.setDouble(32, obj.getNumeroIdentificacao2().doubleValue());
                    sqlInserir.setInt(33, obj.getIdentificaoEmissaoBloqueto().intValue());
                    sqlInserir.setString(34, obj.getNumeroDocumentoCobranca());
                    sqlInserir.setDouble(35, obj.getValorNominalTitulo().doubleValue());
                    sqlInserir.setInt(36, obj.getEspecieTitulo().intValue());
                    sqlInserir.setString(37, obj.getIdentificacaoAceite());
                    sqlInserir.setInt(38, obj.getDataEmissaoTitulo().intValue());
                    sqlInserir.setInt(39, obj.getCodigoJurosMora().intValue());
                    sqlInserir.setInt(40, obj.getDataJurosMora().intValue());
                    sqlInserir.setDouble(41, obj.getJurosMora().doubleValue());
                    sqlInserir.setInt(42, obj.getCodigoDesconto().intValue());
                    sqlInserir.setInt(43, obj.getDataDesconto().intValue());
                    sqlInserir.setDouble(44, obj.getDesconto().doubleValue());
                    sqlInserir.setDouble(45, obj.getValorAbatimento().doubleValue());
                    sqlInserir.setString(46, obj.getIdentificacaoTituloEmpresa());
                    sqlInserir.setString(47, obj.getIdentificacaoTituloBanco());
                    sqlInserir.setInt(48, obj.getCodigoProtesto().intValue());
                    sqlInserir.setInt(49, obj.getNumeroDiasProtesto().intValue());
                    sqlInserir.setInt(50, obj.getCodigoMoeda().intValue());
                    sqlInserir.setInt(51, obj.getNumeroContrato().intValue());
                    sqlInserir.setDate(52, Uteis.getDataJDBC(obj.getDataVencimentoTitulo()));
                    sqlInserir.setInt(53, obj.getNumeroContratoCobrancaRegistro().intValue());
                    sqlInserir.setInt(54, obj.getMotivoOcorrencia().intValue());
                    sqlInserir.setString(55, obj.getConfirmacaoInstituicaoProtesto());
                    sqlInserir.setString(56, obj.getMotivoRegeicao());
                    sqlInserir.setDouble(57, obj.getAcrescimos().doubleValue());
                    sqlInserir.setDouble(58, obj.getValorDesconto().doubleValue());
                    sqlInserir.setDouble(59, obj.getValorPago().doubleValue());
                    sqlInserir.setDouble(60, obj.getValorLiquido().doubleValue());
                    sqlInserir.setDouble(61, obj.getValorDespesas().doubleValue());
                    sqlInserir.setDouble(62, obj.getValorOutrasDespesas().doubleValue());
                    sqlInserir.setDouble(63, obj.getValorOutrosCreditos().doubleValue());
                    sqlInserir.setDouble(64, obj.getValorIOF().doubleValue());
                    sqlInserir.setDate(65, Uteis.getDataJDBC(obj.getDataOcorrencia()));
                    sqlInserir.setDate(66, Uteis.getDataJDBC(obj.getDataCredito()));
                    sqlInserir.setBoolean(67, obj.isBoletoNaoEncontrado());
                    sqlInserir.setBoolean(68, obj.isBoletoBaixado());
                    if (!obj.getCodigoContaReceber().equals(0)) {
                        sqlInserir.setInt(69, obj.getCodigoContaReceber());
                    } else {
                        sqlInserir.setNull(69, 0);
                    }
                    sqlInserir.setDouble(70, obj.getTarifaCobranca().doubleValue());
                    sqlInserir.setString(71, obj.getSituacaoRegistroDetalheEnum().name());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroDetalheVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroDetalheVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroDetalheVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 29/10/2014
        	  *  Classe Subordinada
        	  */ 
            // RegistroDetalhe.alterar(getIdEntidade());
            RegistroDetalheVO.validarDados(obj);
            final String sql = "UPDATE RegistroDetalhe set registroArquivo=?, codigoBanco=?, loteServico=?, tipoRegistro=?, "
                    + "numeroSequencialRegistroLote=?, codigoSeguimentoRegistroDetalhe=?, codigoMovimentoRemessaRetorno=?, "
                    + "sacadoTipoInscricao=?, sacadoNumeroInscricaoCorpo=?, sacadoNumeroInscricaoFilial=?, sacadoNumeroInscricaoControle=?, "
                    + "sacadoNome=?, sacadoEndereco=?, sacadoBairro=?, sacadoCep=?, sacadoSufixoCep=?, sacadoCidade=?, sacadoUnidadeFederacao=?, "
                    + "sacadoBancoCodigo=?, sacadoAgenciaCodigo=?, cedenteTipoInscricaoEmpresa=?, cedenteNumeroInscricaoEmpresa=?, "
                    + "cedenteIdentificacaoNoBanco=?, cedenteNumeroAgencia=?, cedenteNumeroConta=?, carteira=?, codigoCarteira=?, "
                    + "tipoCarteira=?, identificacaoOcorrencia=?, indicadorRateioCredito=?, numeroIdentificacao1=?, numeroIdentificacao2=?, "
                    + "identificaoEmissaoBloqueto=?, numeroDocumentoCobranca=?, valorNominalTitulo=?, especieTitulo=?, identificacaoAceite=?, "
                    + "dataEmissaoTitulo=?, codigoJurosMora=?, dataJurosMora=?, jurosMora=?, codigoDesconto=?, dataDesconto=?, desconto=?, "
                    + "valorAbatimento=?, identificacaoTituloEmpresa=?, identificacaoTituloBanco=?, codigoProtesto=?, numeroDiasProtesto=?, "
                    + "codigoMoeda=?, numeroContrato=?, dataVencimentoTitulo=?, numeroContratoCobrancaRegistro=?, motivoOcorrencia=?, "
                    + "confirmacaoInstituicaoProtesto=?, motivoRegeicao=?, acrescimos=?, valorDesconto=?, valorPago=?, valorLiquido=?, "
                    + "valorDespesas=?, valorOutrasDespesas=?, valorOutrosCreditos=?, valorIOF=?, dataOcorrencia=?, dataCredito=?, "
                    + "boletonaoencontrado=?, boletobaixado=?, codigoContaReceber=?, tarifaCobranca=?, situacaoRegistroDetalhe=?  "
                    + "WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getRegistroArquivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getRegistroArquivo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getCodigoBanco());
                    sqlAlterar.setInt(3, obj.getLoteServico().intValue());
                    sqlAlterar.setInt(4, obj.getTipoRegistro().intValue());
                    sqlAlterar.setInt(5, obj.getNumeroSequencialRegistroLote().intValue());
                    sqlAlterar.setString(6, obj.getCodigoSeguimentoRegistroDetalhe());
                    sqlAlterar.setInt(7, obj.getCodigoMovimentoRemessaRetorno().intValue());
                    sqlAlterar.setInt(8, obj.getSacadoTipoInscricao().intValue());
                    sqlAlterar.setInt(9, obj.getSacadoNumeroInscricaoCorpo().intValue());
                    sqlAlterar.setInt(10, obj.getSacadoNumeroInscricaoFilial().intValue());
                    sqlAlterar.setString(11, obj.getSacadoNumeroInscricaoControle());
                    sqlAlterar.setString(12, obj.getSacadoNome());
                    sqlAlterar.setString(13, obj.getSacadoEndereco());
                    sqlAlterar.setString(14, obj.getSacadoBairro());
                    sqlAlterar.setInt(15, obj.getSacadoCep().intValue());
                    sqlAlterar.setInt(16, obj.getSacadoSufixoCep().intValue());
                    sqlAlterar.setString(17, obj.getSacadoCidade());
                    sqlAlterar.setString(18, obj.getSacadoUnidadeFederacao());
                    sqlAlterar.setString(19, obj.getSacadoBancoCodigo());
                    sqlAlterar.setString(20, obj.getSacadoAgenciaCodigo());
                    sqlAlterar.setInt(21, obj.getCedenteTipoInscricaoEmpresa().intValue());
                    sqlAlterar.setString(22, obj.getCedenteNumeroInscricaoEmpresa());
                    sqlAlterar.setString(23, obj.getCedenteIdentificacaoNoBanco());
                    sqlAlterar.setString(24, obj.getCedenteNumeroAgencia());
                    sqlAlterar.setString(25, obj.getCedenteNumeroConta());
                    sqlAlterar.setInt(26, obj.getCarteira().intValue());
                    sqlAlterar.setInt(27, obj.getCodigoCarteira().intValue());
                    sqlAlterar.setInt(28, obj.getTipoCarteira().intValue());
                    sqlAlterar.setInt(29, obj.getIdentificacaoOcorrencia().intValue());
                    sqlAlterar.setString(30, obj.getIndicadorRateioCredito());
                    sqlAlterar.setDouble(31, obj.getNumeroIdentificacao1().doubleValue());
                    sqlAlterar.setDouble(32, obj.getNumeroIdentificacao2().doubleValue());
                    sqlAlterar.setInt(33, obj.getIdentificaoEmissaoBloqueto().intValue());
                    sqlAlterar.setString(34, obj.getNumeroDocumentoCobranca());
                    sqlAlterar.setDouble(35, obj.getValorNominalTitulo().doubleValue());
                    sqlAlterar.setInt(36, obj.getEspecieTitulo().intValue());
                    sqlAlterar.setString(37, obj.getIdentificacaoAceite());
                    sqlAlterar.setInt(38, obj.getDataEmissaoTitulo().intValue());
                    sqlAlterar.setInt(39, obj.getCodigoJurosMora().intValue());
                    sqlAlterar.setInt(40, obj.getDataJurosMora().intValue());
                    sqlAlterar.setDouble(41, obj.getJurosMora().doubleValue());
                    sqlAlterar.setInt(42, obj.getCodigoDesconto().intValue());
                    sqlAlterar.setInt(43, obj.getDataDesconto().intValue());
                    sqlAlterar.setDouble(44, obj.getDesconto().doubleValue());
                    sqlAlterar.setDouble(45, obj.getValorAbatimento().doubleValue());
                    sqlAlterar.setString(46, obj.getIdentificacaoTituloEmpresa());
                    sqlAlterar.setString(47, obj.getIdentificacaoTituloBanco());
                    sqlAlterar.setInt(48, obj.getCodigoProtesto().intValue());
                    sqlAlterar.setInt(49, obj.getNumeroDiasProtesto().intValue());
                    sqlAlterar.setInt(50, obj.getCodigoMoeda().intValue());
                    sqlAlterar.setInt(51, obj.getNumeroContrato().intValue());
                    sqlAlterar.setDate(52, Uteis.getDataJDBC(obj.getDataVencimentoTitulo()));
                    sqlAlterar.setInt(53, obj.getNumeroContratoCobrancaRegistro().intValue());
                    sqlAlterar.setInt(54, obj.getMotivoOcorrencia().intValue());
                    sqlAlterar.setString(55, obj.getConfirmacaoInstituicaoProtesto());
                    sqlAlterar.setString(56, obj.getMotivoRegeicao());
                    sqlAlterar.setDouble(57, obj.getAcrescimos().doubleValue());
                    sqlAlterar.setDouble(58, obj.getValorDesconto().doubleValue());
                    sqlAlterar.setDouble(59, obj.getValorPago().doubleValue());
                    sqlAlterar.setDouble(60, obj.getValorLiquido().doubleValue());
                    sqlAlterar.setDouble(61, obj.getValorDespesas().doubleValue());
                    sqlAlterar.setDouble(62, obj.getValorOutrasDespesas().doubleValue());
                    sqlAlterar.setDouble(63, obj.getValorOutrosCreditos().doubleValue());
                    sqlAlterar.setDouble(64, obj.getValorIOF().doubleValue());
                    sqlAlterar.setDate(65, Uteis.getDataJDBC(obj.getDataOcorrencia()));
                    sqlAlterar.setDate(66, Uteis.getDataJDBC(obj.getDataCredito()));
                    sqlAlterar.setBoolean(67, obj.isBoletoNaoEncontrado());
                    sqlAlterar.setBoolean(68, obj.isBoletoBaixado());
                    if (!obj.getCodigoContaReceber().equals(0)) {
                        sqlAlterar.setInt(69, obj.getCodigoContaReceber());
                    } else {
                        sqlAlterar.setNull(69, 0);
                    }
                    sqlAlterar.setDouble(70, obj.getTarifaCobranca().doubleValue());
                    sqlAlterar.setString(71, obj.getSituacaoRegistroDetalheEnum().name());
                    sqlAlterar.setInt(72, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroDetalheVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroDetalheVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroDetalheVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 29/10/2014
        	  *  Classe Subordinada
        	  */ 
            // RegistroDetalhe.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroDetalhe WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroDetalhe</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroDetalheVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroDetalhe WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<RegistroDetalheVO> consultarPorRegistroArquivoIdentificacaoRegistroEmpresa(String valorConsulta, Integer registroArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroDetalhe.*, ContaReceber.parcela FROM RegistroDetalhe "
                + " LEFT JOIN ContaReceber ON (ContaReceber.codigo = RegistroDetalhe.codigoContaReceber) "
                + "WHERE registroArquivo = " + registroArquivo.intValue() + " "
        		+ "and valorpago > 0 ";
        if (valorConsulta != null && !valorConsulta.equals("")) {
            sqlStr += " and identificacaoTituloEmpresa like '%" + valorConsulta + "%'";
        }
        sqlStr += "ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    public List<RegistroDetalheVO> consultarRegistroDetalhesConfirmadosOuRejeitados(Integer registroArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT RegistroDetalhe.*, '' as parcela FROM RegistroDetalhe  WHERE situacaoregistrodetalhe in ('CONFIRMADO', 'REJEITADO') and registroArquivo = " + registroArquivo.intValue() + " ORDER BY codigo";    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroDetalheVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>RegistroDetalheVO</code>.
     * @return  O objeto da classe <code>RegistroDetalheVO</code> com os dados devidamente montados.
     */
    public static RegistroDetalheVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RegistroDetalheVO obj = new RegistroDetalheVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getRegistroArquivo().setCodigo(new Integer(dadosSQL.getInt("registroArquivo")));
        obj.setCodigoBanco(dadosSQL.getString("codigoBanco"));
        obj.setLoteServico(new Integer(dadosSQL.getInt("loteServico")));
        obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
        obj.setNumeroSequencialRegistroLote(new Integer(dadosSQL.getInt("numeroSequencialRegistroLote")));
        obj.setCodigoSeguimentoRegistroDetalhe(dadosSQL.getString("codigoSeguimentoRegistroDetalhe"));
        obj.setCodigoMovimentoRemessaRetorno(new Integer(dadosSQL.getInt("codigoMovimentoRemessaRetorno")));
        obj.setSacadoTipoInscricao(new Integer(dadosSQL.getInt("sacadoTipoInscricao")));
        obj.setSacadoNumeroInscricaoCorpo(new Integer(dadosSQL.getInt("sacadoNumeroInscricaoCorpo")));
        obj.setSacadoNumeroInscricaoFilial(new Integer(dadosSQL.getInt("sacadoNumeroInscricaoFilial")));
        obj.setSacadoNumeroInscricaoControle(dadosSQL.getString("sacadoNumeroInscricaoControle"));
        obj.setSacadoNome(dadosSQL.getString("sacadoNome"));
        obj.setSacadoEndereco(dadosSQL.getString("sacadoEndereco"));
        obj.setSacadoBairro(dadosSQL.getString("sacadoBairro"));
        obj.setSacadoCep(new Integer(dadosSQL.getInt("sacadoCep")));
        obj.setSacadoSufixoCep(new Integer(dadosSQL.getInt("sacadoSufixoCep")));
        obj.setSacadoCidade(dadosSQL.getString("sacadoCidade"));
        obj.setSacadoUnidadeFederacao(dadosSQL.getString("sacadoUnidadeFederacao"));
        obj.setSacadoBancoCodigo((dadosSQL.getString("sacadoBancoCodigo")));
        obj.setSacadoAgenciaCodigo(dadosSQL.getString("sacadoAgenciaCodigo"));
        obj.setCedenteTipoInscricaoEmpresa(new Integer(dadosSQL.getInt("cedenteTipoInscricaoEmpresa")));
        obj.setCedenteNumeroInscricaoEmpresa(dadosSQL.getString("cedenteNumeroInscricaoEmpresa"));
        obj.setCedenteIdentificacaoNoBanco(dadosSQL.getString("cedenteIdentificacaoNoBanco"));
        obj.setCedenteNumeroAgencia(dadosSQL.getString("cedenteNumeroAgencia"));
        obj.setCedenteNumeroConta(dadosSQL.getString("cedenteNumeroConta"));
        obj.setCarteira(new Integer(dadosSQL.getInt("carteira")));
        obj.setCodigoCarteira(new Integer(dadosSQL.getInt("codigoCarteira")));
        obj.setTipoCarteira(new Integer(dadosSQL.getInt("tipoCarteira")));
        obj.setIdentificacaoOcorrencia(new Integer(dadosSQL.getInt("identificacaoOcorrencia")));
        obj.setIndicadorRateioCredito(dadosSQL.getString("indicadorRateioCredito"));
        obj.setNumeroIdentificacao1(new Double(dadosSQL.getDouble("numeroIdentificacao1")));
        obj.setNumeroIdentificacao2(new Double(dadosSQL.getDouble("numeroIdentificacao2")));
        obj.setIdentificaoEmissaoBloqueto(new Integer(dadosSQL.getInt("identificaoEmissaoBloqueto")));
        obj.setNumeroDocumentoCobranca(dadosSQL.getString("numeroDocumentoCobranca"));
        obj.setValorNominalTitulo(new Double(dadosSQL.getDouble("valorNominalTitulo")));
        obj.setEspecieTitulo(new Integer(dadosSQL.getInt("especieTitulo")));
        obj.setIdentificacaoAceite(dadosSQL.getString("identificacaoAceite"));
        obj.setDataEmissaoTitulo(new Integer(dadosSQL.getInt("dataEmissaoTitulo")));
        obj.setCodigoJurosMora(new Integer(dadosSQL.getInt("codigoJurosMora")));
        obj.setDataJurosMora(new Integer(dadosSQL.getInt("dataJurosMora")));
        obj.setJurosMora(new Double(dadosSQL.getDouble("jurosMora")));
        obj.setCodigoDesconto(new Integer(dadosSQL.getInt("codigoDesconto")));
        obj.setDataDesconto(new Integer(dadosSQL.getInt("dataDesconto")));
        obj.setDesconto(new Double(dadosSQL.getDouble("desconto")));
        obj.setValorAbatimento(new Double(dadosSQL.getDouble("valorAbatimento")));
        obj.setIdentificacaoTituloEmpresa(dadosSQL.getString("identificacaoTituloEmpresa"));
        obj.setIdentificacaoTituloBanco(dadosSQL.getString("identificacaoTituloBanco"));
        obj.setCodigoProtesto(new Integer(dadosSQL.getInt("codigoProtesto")));
        obj.setNumeroDiasProtesto(new Integer(dadosSQL.getInt("numeroDiasProtesto")));
        obj.setCodigoMoeda(new Integer(dadosSQL.getInt("codigoMoeda")));
        obj.setNumeroContrato(new Integer(dadosSQL.getInt("numeroContrato")));
        obj.setDataVencimentoTitulo(dadosSQL.getDate("dataVencimentoTitulo"));
        obj.setNumeroContratoCobrancaRegistro(new Integer(dadosSQL.getInt("numeroContratoCobrancaRegistro")));
        obj.setMotivoOcorrencia(new Integer(dadosSQL.getInt("motivoOcorrencia")));
        obj.setConfirmacaoInstituicaoProtesto(dadosSQL.getString("confirmacaoInstituicaoProtesto"));
        obj.setMotivoRegeicao(dadosSQL.getString("motivoRegeicao"));
        obj.setAcrescimos(new Double(dadosSQL.getDouble("acrescimos")));
        obj.setValorDesconto(new Double(dadosSQL.getDouble("valorDesconto")));
        obj.setValorPago(new Double(dadosSQL.getDouble("valorPago")));
        obj.setValorLiquido(new Double(dadosSQL.getDouble("valorLiquido")));
        obj.setValorDespesas(new Double(dadosSQL.getDouble("valorDespesas")));
        obj.setValorOutrasDespesas(new Double(dadosSQL.getDouble("valorOutrasDespesas")));
        obj.setValorOutrosCreditos(new Double(dadosSQL.getDouble("valorOutrosCreditos")));
        obj.setValorIOF(new Double(dadosSQL.getDouble("valorIOF")));
        obj.setDataOcorrencia(dadosSQL.getDate("dataOcorrencia"));
        obj.setDataCredito(dadosSQL.getDate("dataCredito"));
        obj.setBoletoBaixado(dadosSQL.getBoolean("boletobaixado"));
        obj.setTarifaCobranca(dadosSQL.getDouble("tarifaCobranca"));
        obj.setBoletoNaoEncontrado(dadosSQL.getBoolean("boletonaoencontrado"));
        obj.setCodigoContaReceber(dadosSQL.getInt("codigoContaReceber"));
        obj.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.valueOf(dadosSQL.getString("situacaoRegistroDetalhe")));
        obj.setParcela(dadosSQL.getString("parcela"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>RegistroDetalheVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>RegistroDetalhe</code>.
     * @param <code>registroArquivo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirRegistroDetalhes(Integer registroArquivo, UsuarioVO usuario) throws Exception {
        try {
            RegistroDetalhe.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroDetalhe WHERE (registroArquivo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{registroArquivo});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>RegistroDetalheVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirRegistroDetalhes</code> e <code>incluirRegistroDetalhes</code> disponíveis na classe <code>RegistroDetalhe</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarRegistroDetalhes(Integer registroArquivo, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM RegistroDetalhe WHERE registroArquivo = " + registroArquivo;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            RegistroDetalheVO objeto = (RegistroDetalheVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            RegistroDetalheVO objeto = (RegistroDetalheVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }

    }

    /**
     * Operação responsável por incluir objetos da <code>RegistroDetalheVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.RegistroArquivo</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirRegistroDetalhes(Integer registroArquivoPrm, List<RegistroDetalheVO> objetos, UsuarioVO usuario) throws Exception {
        for (RegistroDetalheVO obj : objetos) {
        	if (obj != null) {
	            obj.getRegistroArquivo().setCodigo(registroArquivoPrm);
	            if(obj.getCodigo() == 0) {
	            	incluir(obj, usuario);	
	            }else {
	            	alterar(obj, usuario);
	            }
	            
        	}
        }
    }

    /**
     * Operação responsável por consultar todos os <code>RegistroDetalheVO</code> relacionados a um objeto da classe <code>financeiro.RegistroArquivo</code>.
     * @param registroArquivo  Atributo de <code>financeiro.RegistroArquivo</code> a ser utilizado para localizar os objetos da classe <code>RegistroDetalheVO</code>.
     * @return List  Contendo todos os objetos da classe <code>RegistroDetalheVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarRegistroDetalhes(Integer registroArquivo, int nivelMontarDados) throws Exception {
        RegistroDetalhe.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM RegistroDetalhe WHERE registroArquivo = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        while (resultado.next()) {
            RegistroDetalheVO novoObj = new RegistroDetalheVO();
            novoObj = RegistroDetalhe.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;

    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroDetalheVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroDetalheVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroDetalhe WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroDetalhe ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroDetalhe.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroDetalhe.idEntidade = idEntidade;
    }
    
    public void atualizarCodigoBarraRegistroDetalhe(UsuarioVO usuarioVO) throws Exception {
    	List<RegistroDetalheVO> listaRegistroDetalheVOs = consultarRegistroDetalhe();
    	HashMap<Integer, ContaCorrenteVO> mapContaCorrenteVOs = new HashMap<Integer, ContaCorrenteVO>(0);
    	for (RegistroDetalheVO registroDetalheVO : listaRegistroDetalheVOs) {
			
    		if (!registroDetalheVO.getNumeroContaCorrente().equals(0)) {
				ContaCorrenteVO contaCorrenteVO = null;
				if (mapContaCorrenteVOs.containsKey(registroDetalheVO.getNumeroContaCorrente())) {
					contaCorrenteVO = mapContaCorrenteVOs.get(registroDetalheVO.getNumeroContaCorrente());
				} else {
					String numeroConta = registroDetalheVO.getNumeroContaCorrente().toString().substring(0, 4);
					contaCorrenteVO = obterContaCorrenteAtravesNumeroComDigito(numeroConta, registroDetalheVO.getNumeroAgencia(), usuarioVO);
					mapContaCorrenteVOs.put(registroDetalheVO.getNumeroContaCorrente(), contaCorrenteVO);
				}
				
			}
		}
	}
    
    public ContaCorrenteVO obterContaCorrenteAtravesNumeroComDigito(String numeroContaCorrente, Integer numeroAgencia, UsuarioVO usuarioVO) throws Exception {
    	
    	ContaCorrenteVO contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaNumeroConta(numeroContaCorrente, numeroAgencia.toString(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    	return contaCorrenteVO;
    }
	
	public List<RegistroDetalheVO> consultarRegistroDetalhe() {
		StringBuilder sb = new StringBuilder();
		sb.append("select registroheader.numeroconta, registroheader.numeroagencia, registrodetalhe.* from registroarquivo ");
		sb.append(" inner join registroheader on registroheader.codigo = registroarquivo.registroheader ");
		sb.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
		sb.append(" order by registroarquivo.codigo	");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<RegistroDetalheVO> listaRegistroDetalheVOs = new ArrayList<RegistroDetalheVO>(0);
		while (tabelaResultado.next()) {
			RegistroDetalheVO obj = new RegistroDetalheVO();
			obj.setDataCredito(tabelaResultado.getDate("dataCredito"));
			obj.setDataOcorrencia(tabelaResultado.getDate("dataOcorrencia"));
			obj.setValorNominalTitulo(tabelaResultado.getDouble("valorNominalTitulo"));
			obj.setIdentificacaoTituloEmpresa(tabelaResultado.getString("identificacaoTituloEmpresa"));
			obj.setNumeroContaCorrente(Uteis.getValorInteiro(tabelaResultado.getString("numeroConta")));
			listaRegistroDetalheVOs.add(obj);
		}
		return listaRegistroDetalheVOs;
	}
}
