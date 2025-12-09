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
import negocio.comuns.financeiro.RegistroDetalhePagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroDetalhePagarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroDetalhePagar extends ControleAcesso implements RegistroDetalhePagarInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3579484907911991575L;
	protected static String idEntidade;

    public RegistroDetalhePagar() throws Exception {
        super();
        setIdEntidade("RegistroArquivo");
    }
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(List<RegistroDetalhePagarVO> lista, UsuarioVO usuario) throws Exception {
    	for (RegistroDetalhePagarVO objeto : lista) {
    		if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
		}
    }    
    

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroDetalhePagarVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RegistroDetalhePagarVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final RegistroDetalhePagarVO obj, UsuarioVO usuario) throws Exception {
        try {            
            final String sql = "INSERT INTO RegistroDetalhePagar( " +
            		"registroHeaderLotePagar, codigoBanco,loteServico,tipoRegistro,numeroSequencialRegistroLote,codigoSegmentoRegistroLote,tipoMovimento,codigoInstrucaoMovimento,codigoCamaraCompesacao,codigoBancoFavorecido, " +
            		"numeroAgenciaFavorecido,digitoAgenciaFavorecido,numeroContaFavorecido,digitoContaFavorecido,digitoAgenciaContaFavorecido,nomeFavorecido,nossonumero,dataPagamento, " +
            		"valorPagamento,numeroDocumento,dataRealPagamento,valorRealPagamento,informacao2,finalidadeDoc,finalidadeTed,codigoFinalidadeComplementar,tipoInscricaoFavorecido,numeroInscricaoFavorecido, " +
            		"logradouroFavorecido,numeroEnderecoFavorecido,complementoEnderecoFavorecido,bairroFavorecido,cidadeFavorecido,cepFavorecido,estadoFavorecido,dataVencimento,valorDesconto, " +
            		"valorMulta,valorJuro,horarioEnvioTed,codigoHistoricoParaCredito,tedInstituicaoFinanceira,numeroISPB,codigoBarra,codigoReceitaTributo,tipoIdentificacaoContribuinte, " +
            		"identificacaoContribuinte,codigoidentificacaoTributo,mesAnoCompetencia,periodoApuracao,numeroReferencia,valorReceitaBrutaAcumulada,percentualReceitaBrutaAcumulada, " +
            		"numeroInscricaoEstadualFavorecido,numeroInscricaoMunicipalFavorecido,motivoOcorrencia,motivoRegeicao,autenticacaoPagamento,protocoloPagamento ,nossoNumeroContaAgrupada ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + //30
                    " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?  ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    int i=0;
                    
                    sqlInserir.setInt(++i, obj.getRegistroHeaderLotePagarVO().getCodigo().intValue());
					sqlInserir.setString(++i, obj.getCodigoBanco());
					sqlInserir.setString(++i, obj.getLoteServico());
					sqlInserir.setInt(++i, obj.getTipoRegistro());
					sqlInserir.setString(++i, obj.getNumeroSequencialRegistroLote());
					sqlInserir.setString(++i, obj.getCodigoSegmentoRegistroLote());
					sqlInserir.setString(++i, obj.getTipoMovimento());
					sqlInserir.setString(++i, obj.getCodigoInstrucaoMovimento());
					sqlInserir.setString(++i, obj.getCodigoCamaraCompesacao());
					sqlInserir.setInt(++i, obj.getCodigoBancoFavorecido());
					sqlInserir.setInt(++i, obj.getNumeroAgenciaFavorecido());
					sqlInserir.setString(++i, obj.getDigitoAgenciaFavorecido());
					sqlInserir.setString(++i, obj.getNumeroContaFavorecido());
					sqlInserir.setString(++i, obj.getDigitoContaFavorecido());
					sqlInserir.setString(++i, obj.getDigitoAgenciaContaFavorecido());
					sqlInserir.setString(++i, obj.getNomeFavorecido());
					sqlInserir.setLong(++i, obj.getNossoNumero());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPagamento()));
					sqlInserir.setDouble(++i, obj.getValorPagamento());
					sqlInserir.setString(++i, obj.getNumeroDocumento());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRealPagamento()));
					sqlInserir.setDouble(++i, obj.getValorRealPagamento());
					sqlInserir.setString(++i, obj.getInformacao2());
					sqlInserir.setString(++i, obj.getFinalidadeDoc());
					sqlInserir.setString(++i, obj.getFinalidadeTed());
					sqlInserir.setString(++i, obj.getCodigoFinalidadeComplementar());
					sqlInserir.setLong(++i, obj.getTipoInscricaoFavorecido());
					sqlInserir.setLong(++i, obj.getNumeroInscricaoFavorecido());
					sqlInserir.setString(++i, obj.getLogradouroFavorecido());
					sqlInserir.setString(++i, obj.getNumeroEnderecoFavorecido());
					sqlInserir.setString(++i, obj.getComplementoEnderecoFavorecido());
					sqlInserir.setString(++i, obj.getBairroFavorecido());
					sqlInserir.setString(++i, obj.getCidadeFavorecido());
					sqlInserir.setString(++i, obj.getCepFavorecido());
					sqlInserir.setString(++i, obj.getEstadoFavorecido());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setDouble(++i, obj.getValorDesconto());
					sqlInserir.setDouble(++i, obj.getValorMulta());
					sqlInserir.setDouble(++i, obj.getValorJuro());
					sqlInserir.setString(++i, obj.getHorarioEnvioTed());
					sqlInserir.setString(++i, obj.getCodigoHistoricoParaCredito());
					sqlInserir.setString(++i, obj.getTedInstituicaoFinanceira());
					sqlInserir.setString(++i, obj.getNumeroISPB());
					sqlInserir.setString(++i, obj.getCodigoBarra());
					sqlInserir.setString(++i, obj.getCodigoReceitaTributo());
					sqlInserir.setString(++i, obj.getTipoIdentificacaoContribuinte());
					sqlInserir.setString(++i, obj.getIdentificacaoContribuinte());
					sqlInserir.setString(++i, obj.getCodigoidentificacaoTributo());
					sqlInserir.setString(++i, obj.getMesAnoCompetencia());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getPeriodoApuracao()));
					sqlInserir.setString(++i, obj.getNumeroReferencia());
					sqlInserir.setDouble(++i, obj.getValorReceitaBrutaAcumulada());
					sqlInserir.setDouble(++i, obj.getPercentualReceitaBrutaAcumulada());
					sqlInserir.setLong(++i, obj.getNumeroInscricaoEstadualFavorecido());
					sqlInserir.setLong(++i, obj.getNumeroInscricaoMunicipalFavorecido());
					sqlInserir.setInt(++i, obj.getMotivoOcorrencia());
					sqlInserir.setString(++i, obj.getMotivoRegeicao());					
					sqlInserir.setString(++i, obj.getAutenticacaoPagamento());
					sqlInserir.setString(++i, obj.getProtocoloPagamento());
					sqlInserir.setString(++i, obj.getNossoNumeroContaAgrupada());					
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroDetalhePagarVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroDetalhePagarVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final RegistroDetalhePagarVO obj, UsuarioVO usuario) throws Exception {
        try {            
            final String sql = "UPDATE RegistroDetalhePagar set  " +
            		" registroHeaderLotePagar=?, codigoBanco=?,loteServico=?,tipoRegistro=?,numeroSequencialRegistroLote=?,codigoSegmentoRegistroLote=?,tipoMovimento=?,codigoInstrucaoMovimento=?,codigoCamaraCompesacao=?,codigoBancoFavorecido=?, " +
            		" numeroAgenciaFavorecido=?,digitoAgenciaFavorecido=?,numeroContaFavorecido=?,digitoContaFavorecido=?,digitoAgenciaContaFavorecido=?,nomeFavorecido=?,nossonumero=?,dataPagamento=?, " +
            		" valorPagamento=?,numeroDocumento=?,dataRealPagamento=?,valorRealPagamento=?,informacao2=?,finalidadeDoc=?,finalidadeTed=?,codigoFinalidadeComplementar=?,tipoInscricaoFavorecido=?,numeroInscricaoFavorecido=?, " +
            		" logradouroFavorecido=?,numeroEnderecoFavorecido=?,complementoEnderecoFavorecido=?,bairroFavorecido=?,cidadeFavorecido=?,cepFavorecido=?,estadoFavorecido=?,dataVencimento=?,valorDesconto=?, " +
            		" valorMulta=?,valorJuro=?,horarioEnvioTed=?,codigoHistoricoParaCredito=?,tedInstituicaoFinanceira=?,numeroISPB=?,codigoBarra=?,codigoReceitaTributo=?,tipoIdentificacaoContribuinte=?, " +
            		" identificacaoContribuinte=?,codigoidentificacaoTributo=?,mesAnoCompetencia=?,periodoApuracao=?,numeroReferencia=?,valorReceitaBrutaAcumulada=?,percentualReceitaBrutaAcumulada=?, " +
            		" numeroInscricaoEstadualFavorecido=?,numeroInscricaoMunicipalFavorecido=?,motivoOcorrencia=?,motivoRegeicao=?, autenticacaoPagamento=?,protocoloPagamento=?  , nossoNumeroContaAgrupada =? " +
                    " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i=0;
                    sqlAlterar.setInt(++i, obj.getRegistroHeaderLotePagarVO().getCodigo().intValue());
					sqlAlterar.setString(++i, obj.getCodigoBanco());
					sqlAlterar.setString(++i, obj.getLoteServico());
					sqlAlterar.setInt(++i, obj.getTipoRegistro());
					sqlAlterar.setString(++i, obj.getNumeroSequencialRegistroLote());
					sqlAlterar.setString(++i, obj.getCodigoSegmentoRegistroLote());
					sqlAlterar.setString(++i, obj.getTipoMovimento());
					sqlAlterar.setString(++i, obj.getCodigoInstrucaoMovimento());
					sqlAlterar.setString(++i, obj.getCodigoCamaraCompesacao());
					sqlAlterar.setInt(++i, obj.getCodigoBancoFavorecido());
					sqlAlterar.setInt(++i, obj.getNumeroAgenciaFavorecido());
					sqlAlterar.setString(++i, obj.getDigitoAgenciaFavorecido());
					sqlAlterar.setString(++i, obj.getNumeroContaFavorecido());
					sqlAlterar.setString(++i, obj.getDigitoContaFavorecido());
					sqlAlterar.setString(++i, obj.getDigitoAgenciaContaFavorecido());
					sqlAlterar.setString(++i, obj.getNomeFavorecido());
					sqlAlterar.setLong(++i, obj.getNossoNumero());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPagamento()));
					sqlAlterar.setDouble(++i, obj.getValorPagamento());
					sqlAlterar.setString(++i, obj.getNumeroDocumento());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataRealPagamento()));
					sqlAlterar.setDouble(++i, obj.getValorRealPagamento());
					sqlAlterar.setString(++i, obj.getInformacao2());
					sqlAlterar.setString(++i, obj.getFinalidadeDoc());
					sqlAlterar.setString(++i, obj.getFinalidadeTed());
					sqlAlterar.setString(++i, obj.getCodigoFinalidadeComplementar());
					sqlAlterar.setLong(++i, obj.getTipoInscricaoFavorecido());
					sqlAlterar.setLong(++i, obj.getNumeroInscricaoFavorecido());
					sqlAlterar.setString(++i, obj.getLogradouroFavorecido());
					sqlAlterar.setString(++i, obj.getNumeroEnderecoFavorecido());
					sqlAlterar.setString(++i, obj.getComplementoEnderecoFavorecido());
					sqlAlterar.setString(++i, obj.getBairroFavorecido());
					sqlAlterar.setString(++i, obj.getCidadeFavorecido());
					sqlAlterar.setString(++i, obj.getCepFavorecido());
					sqlAlterar.setString(++i, obj.getEstadoFavorecido());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlAlterar.setDouble(++i, obj.getValorDesconto());
					sqlAlterar.setDouble(++i, obj.getValorMulta());
					sqlAlterar.setDouble(++i, obj.getValorJuro());
					sqlAlterar.setString(++i, obj.getHorarioEnvioTed());
					sqlAlterar.setString(++i, obj.getCodigoHistoricoParaCredito());
					sqlAlterar.setString(++i, obj.getTedInstituicaoFinanceira());
					sqlAlterar.setString(++i, obj.getNumeroISPB());
					sqlAlterar.setString(++i, obj.getCodigoBarra());
					sqlAlterar.setString(++i, obj.getCodigoReceitaTributo());
					sqlAlterar.setString(++i, obj.getTipoIdentificacaoContribuinte());
					sqlAlterar.setString(++i, obj.getIdentificacaoContribuinte());
					sqlAlterar.setString(++i, obj.getCodigoidentificacaoTributo());
					sqlAlterar.setString(++i, obj.getMesAnoCompetencia());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getPeriodoApuracao()));
					sqlAlterar.setString(++i, obj.getNumeroReferencia());
					sqlAlterar.setDouble(++i, obj.getValorReceitaBrutaAcumulada());
					sqlAlterar.setDouble(++i, obj.getPercentualReceitaBrutaAcumulada());
					sqlAlterar.setLong(++i, obj.getNumeroInscricaoEstadualFavorecido());
					sqlAlterar.setLong(++i, obj.getNumeroInscricaoMunicipalFavorecido());
					sqlAlterar.setInt(++i, obj.getMotivoOcorrencia());
					sqlAlterar.setString(++i, obj.getMotivoRegeicao());
					sqlAlterar.setString(++i, obj.getAutenticacaoPagamento());
					sqlAlterar.setString(++i, obj.getProtocoloPagamento());
					sqlAlterar.setString(++i, obj.getNossoNumeroContaAgrupada());	
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroDetalhePagarVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroDetalhePagarVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroDetalhePagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM RegistroDetalhePagar WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroDetalhePagar</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroDetalhePagarVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroDetalhePagar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    @Override
    public List<RegistroDetalhePagarVO> consultarPorRegistroHeaderLotePagar(Integer registroHeaderLotePagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroDetalhePagar WHERE registroHeaderLotePagar = " + registroHeaderLotePagar.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

   

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroDetalhePagarVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>RegistroDetalhePagarVO</code>.
     * @return  O objeto da classe <code>RegistroDetalhePagarVO</code> com os dados devidamente montados.
     */
    public static RegistroDetalhePagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RegistroDetalhePagarVO obj = new RegistroDetalhePagarVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setCodigoBanco(dadosSQL.getString("codigoBanco"));
    	obj.setLoteServico(dadosSQL.getString("loteServico"));
    	obj.setTipoRegistro(dadosSQL.getInt("tipoRegistro"));
    	obj.setNumeroSequencialRegistroLote(dadosSQL.getString("numeroSequencialRegistroLote"));
    	obj.setCodigoSegmentoRegistroLote(dadosSQL.getString("codigoSegmentoRegistroLote"));
    	obj.setTipoMovimento(dadosSQL.getString("tipoMovimento"));
    	obj.setCodigoInstrucaoMovimento(dadosSQL.getString("codigoInstrucaoMovimento"));
    	obj.setCodigoCamaraCompesacao(dadosSQL.getString("codigoCamaraCompesacao"));
    	obj.setCodigoBancoFavorecido(dadosSQL.getInt("codigoBancoFavorecido"));
    	obj.setNumeroAgenciaFavorecido(dadosSQL.getInt("numeroAgenciaFavorecido"));
    	obj.setDigitoAgenciaFavorecido(dadosSQL.getString("digitoAgenciaFavorecido"));
    	obj.setNumeroContaFavorecido(dadosSQL.getString("numeroContaFavorecido"));
    	obj.setDigitoContaFavorecido(dadosSQL.getString("digitoContaFavorecido"));
    	obj.setDigitoAgenciaContaFavorecido(dadosSQL.getString("digitoAgenciaContaFavorecido"));
    	obj.setNomeFavorecido(dadosSQL.getString("nomeFavorecido"));
    	obj.setNossoNumero(dadosSQL.getLong("nossoNumero"));
    	obj.setDataPagamento(dadosSQL.getDate("dataPagamento"));
    	obj.setValorPagamento(dadosSQL.getDouble("valorPagamento"));
    	obj.setNumeroDocumento(dadosSQL.getString("numeroDocumento"));
    	obj.setDataRealPagamento(dadosSQL.getDate("dataRealPagamento"));
    	obj.setValorRealPagamento(dadosSQL.getDouble("valorRealPagamento"));
    	obj.setInformacao2(dadosSQL.getString("informacao2"));
    	obj.setFinalidadeDoc(dadosSQL.getString("finalidadeDoc"));
    	obj.setFinalidadeTed(dadosSQL.getString("finalidadeTed"));
    	obj.setCodigoFinalidadeComplementar(dadosSQL.getString("codigoFinalidadeComplementar"));
    	obj.setTipoInscricaoFavorecido(dadosSQL.getInt("tipoInscricaoFavorecido"));
    	obj.setNumeroInscricaoFavorecido(dadosSQL.getLong("numeroInscricaoFavorecido"));
    	obj.setLogradouroFavorecido(dadosSQL.getString("logradouroFavorecido"));
    	obj.setNumeroEnderecoFavorecido(dadosSQL.getString("numeroEnderecoFavorecido"));
    	obj.setComplementoEnderecoFavorecido(dadosSQL.getString("complementoEnderecoFavorecido"));
    	obj.setBairroFavorecido(dadosSQL.getString("bairroFavorecido"));
    	obj.setCidadeFavorecido(dadosSQL.getString("cidadeFavorecido"));
    	obj.setCepFavorecido(dadosSQL.getString("cepFavorecido"));
    	obj.setEstadoFavorecido(dadosSQL.getString("estadoFavorecido"));
    	obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
    	obj.setValorDesconto(dadosSQL.getDouble("valorDesconto"));
    	obj.setValorMulta(dadosSQL.getDouble("valorMulta"));
    	obj.setValorJuro(dadosSQL.getDouble("valorJuro"));
    	obj.setHorarioEnvioTed(dadosSQL.getString("horarioEnvioTed"));
    	obj.setCodigoHistoricoParaCredito(dadosSQL.getString("codigoHistoricoParaCredito"));
    	obj.setTedInstituicaoFinanceira(dadosSQL.getString("tedInstituicaoFinanceira"));
    	obj.setNumeroISPB(dadosSQL.getString("numeroISPB"));
    	obj.setCodigoBarra(dadosSQL.getString("codigoBarra"));
    	obj.setCodigoReceitaTributo(dadosSQL.getString("codigoReceitaTributo"));
    	obj.setTipoIdentificacaoContribuinte(dadosSQL.getString("tipoIdentificacaoContribuinte"));
    	obj.setIdentificacaoContribuinte(dadosSQL.getString("identificacaoContribuinte"));
    	obj.setCodigoidentificacaoTributo(dadosSQL.getString("codigoidentificacaoTributo"));
    	obj.setMesAnoCompetencia(dadosSQL.getString("mesAnoCompetencia"));
    	obj.setPeriodoApuracao(dadosSQL.getDate("periodoApuracao"));
    	obj.setNumeroReferencia(dadosSQL.getString("numeroReferencia"));	
    	obj.setValorReceitaBrutaAcumulada(dadosSQL.getDouble("valorReceitaBrutaAcumulada"));
    	obj.setPercentualReceitaBrutaAcumulada(dadosSQL.getDouble("percentualReceitaBrutaAcumulada"));
    	obj.setNumeroInscricaoEstadualFavorecido(dadosSQL.getLong("numeroInscricaoEstadualFavorecido"));
    	obj.setNumeroInscricaoMunicipalFavorecido(dadosSQL.getLong("numeroInscricaoMunicipalFavorecido"));
    	obj.setMotivoOcorrencia(dadosSQL.getInt("motivoOcorrencia"));
    	obj.setMotivoRegeicao(dadosSQL.getString("motivoRegeicao"));    	
    	obj.setAutenticacaoPagamento(dadosSQL.getString("autenticacaoPagamento"));
    	obj.setProtocoloPagamento(dadosSQL.getString("protocoloPagamento"));
    	obj.setNossoNumeroContaAgrupada(dadosSQL.getString("nossoNumeroContaAgrupada"));
    	
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>RegistroDetalhePagarVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>RegistroDetalhePagar</code>.
     * @param <code>registroArquivo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirRegistroDetalhePagars(Integer registroArquivo, UsuarioVO usuario) throws Exception {
        try {
            RegistroDetalhePagar.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroDetalhePagar WHERE (registroArquivo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{registroArquivo});
        } catch (Exception e) {
            throw e;
        }
    }   

   

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroDetalhePagarVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Override
    public RegistroDetalhePagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroDetalhePagar WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroDetalhePagar ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroDetalhePagar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroDetalhePagar.idEntidade = idEntidade;
    }
	
	

}
