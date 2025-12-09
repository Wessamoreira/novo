package negocio.facade.jdbc.basico;

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

import controle.arquitetura.ConfiguracaoControleInterface;
import controle.basico.ConfiguracoesControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.ConfiguracoesInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ConfiguracoesVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ConfiguracoesVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ConfiguracoesVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Configuracoes extends ControleAcesso implements ConfiguracoesInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = 4803705434417013544L;
    protected static String idEntidade;

    
    public Configuracoes() throws Exception {
        super();
        setIdEntidade("Configuracoes");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ConfiguracoesVO</code>.
     */
    public ConfiguracoesVO novo() throws Exception {
        Configuracoes.incluir(getIdEntidade());
        ConfiguracoesVO obj = new ConfiguracoesVO();
        return obj;
    }

    public void verificarUnicidadeDePadrao(ConfiguracoesVO configuracoesVO) throws Exception {
        boolean valorSugeridoPeloBD;
        String sqlStr = "select count(*) = 0 from configuracoes where padrao = true";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        valorSugeridoPeloBD = tabelaResultado.getBoolean(1);
        if (valorSugeridoPeloBD && !configuracoesVO.getPadrao()) {
            throw new Exception("Não há configuração padrão salva no sistema, selecione a opção (PADRÃO)");
        }
        if (!valorSugeridoPeloBD && configuracoesVO.getPadrao()) {
            sqlStr += " and codigo = " + configuracoesVO.getCodigo().intValue();
            SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

            resultado.next();
            valorSugeridoPeloBD = resultado.getBoolean(1);
            if (valorSugeridoPeloBD) {
                throw new Exception("O sistema pode ter apenas uma configuração padrão, desmarque a opção (PADRAO)");
            }
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ConfiguracoesVO</code>.
     * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ConfiguracoesVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ConfiguracoesVO obj, List<ConfiguracaoControleInterface> configs, UsuarioVO usuarioVO) throws Exception {
        try {
            incluir(getIdEntidade(), true, usuarioVO);
            ConfiguracoesVO.validarDados(obj);
            verificarUnicidadeDePadrao(obj);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder("INSERT INTO Configuracoes( nome, padrao, mensagemPlanoDeEstudo, mensagemBoletimAcademico, ");
            		sql.append(" controlarSuspensaoMatriculaPendenciaDocumentos, nrDiasPrimeiroAvisoRiscoSuspensao, ");
            		sql.append(" nrDiasSegundoAvisoRiscoSuspensao, nrDiasTerceiroAvisoRiscoSuspensao, nrDiasSuspenderMatriculaPendenciaDocumentos, "); 
            		sql.append(" nrDiasListarMatriculaPendenciaDocumentosParaCancelamento, layoutPadraoComprovanteMatricula, ");
            		sql.append(" enviarMensagemNotificacaoFrequenciaAula, numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula, porcentagemConsiderarFrequenciaAulaBaixa, ");
            		sql.append(" periodicidadeNotificacaoFrequenciaAula, controlarNotificacaoPendenciaDocumentos, nrDiaAposInicioAulaNotificarPendenciaDocumento, periodicidadeNotificarPendenciaDocumento, ");
            		sql.append(" primeiraNotificacaoNaoLancamentoRegistroAula, segundaNotificacaoNaoLancamentoRegistroAula,nrDiasQuartoAvisoRiscoSuspensao,periodicidadeQuartoAvisoRiscoSuspensao, ");
            		sql.append(" terceiraNotificacaoNaoLancamentoRegistroAula, quartaNotificacaoNaoLancamentoRegistroAula ");
            		sql.append(" ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo");
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setBoolean(2, obj.isPadrao().booleanValue());
                    sqlInserir.setString(3, obj.getMensagemPlanoDeEstudo());
                    sqlInserir.setString(4, obj.getMensagemBoletimAcademico());
                    sqlInserir.setBoolean(5, obj.getControlarSuspensaoMatriculaPendenciaDocumentos());
                    sqlInserir.setInt(6, obj.getNrDiasPrimeiroAvisoRiscoSuspensao());
                    sqlInserir.setInt(7, obj.getNrDiasSegundoAvisoRiscoSuspensao());
                    sqlInserir.setInt(8, obj.getNrDiasTerceiroAvisoRiscoSuspensao());
                    sqlInserir.setInt(9, obj.getNrDiasSuspenderMatriculaPendenciaDocumentos());
                    sqlInserir.setInt(10, obj.getNrDiasListarMatriculaPendenciaDocumentosParaCancelamento());
                    sqlInserir.setString(11, obj.getLayoutPadraoComprovanteMatricula().toString());
                    sqlInserir.setBoolean(12, obj.getEnviarMensagemNotificacaoFrequenciaAula());
                    sqlInserir.setInt(13, obj.getNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula());
                    sqlInserir.setInt(14, obj.getPorcentagemConsiderarFrequenciaAulaBaixa());
                    sqlInserir.setInt(15, obj.getPeriodicidadeNotificacaoFrequenciaAula());
                    sqlInserir.setBoolean(16, obj.getControlarNotificacaoPendenciaDocumentos());
                    sqlInserir.setInt(17, obj.getNrDiaAposInicioAulaNotificarPendenciaDocumento());
                    sqlInserir.setInt(18, obj.getPeriodicidadeNotificarPendenciaDocumento());
                    sqlInserir.setInt(19, obj.getPrimeiraNotificacaoNaoLancamentoRegistroAula());
                    sqlInserir.setInt(20, obj.getSegundaNotificacaoNaoLancamentoRegistroAula());
                    sqlInserir.setInt(21, obj.getNrDiasQuartoAvisoRiscoSuspensao());
                    sqlInserir.setInt(22, obj.getPeriodicidadeQuartoAvisoRiscoSuspensao());                    
                    sqlInserir.setInt(23, obj.getTerceiraNotificacaoNaoLancamentoRegistroAula());
                    sqlInserir.setInt(24, obj.getQuartaNotificacaoNaoLancamentoRegistroAula());
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

            salvarConfiguracoesRelacionadas(configs, obj);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            limparCamposParaClone(configs, obj);
            throw e;
        }
    }

    public void salvarConfiguracoesRelacionadas(List<ConfiguracaoControleInterface> configs, ConfiguracoesVO configuracoesVO) throws Exception {
        for (ConfiguracaoControleInterface config : configs) {
            config.gravar(configuracoesVO);
        }
    }

    private void limparCamposParaClone(List<ConfiguracaoControleInterface> configs, ConfiguracoesVO configuracoesVO) {
        for (ConfiguracaoControleInterface config : configs) {
            config.limparCamposParaClone();
        }
        configuracoesVO.setCodigo(0);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConfiguracoesVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ConfiguracoesVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ConfiguracoesVO obj, List<ConfiguracaoControleInterface> configs, ConfiguracoesControle configuracoesControle, UsuarioVO usuarioVO) throws Exception {
        try {
            alterar(getIdEntidade(), true, usuarioVO);
            ConfiguracoesVO.validarDados(obj);
            verificarUnicidadeDePadrao(obj);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder("UPDATE Configuracoes set nome=?, padrao=?, mensagemPlanoDeEstudo=?, mensagemBoletimAcademico=?,");
            sql.append(" controlarSuspensaoMatriculaPendenciaDocumentos=?, nrDiasPrimeiroAvisoRiscoSuspensao=?, nrDiasSegundoAvisoRiscoSuspensao=?, ");
            sql.append(" nrDiasTerceiroAvisoRiscoSuspensao=?, nrDiasSuspenderMatriculaPendenciaDocumentos=?, nrDiasListarMatriculaPendenciaDocumentosParaCancelamento=?, ");
            sql.append(" layoutPadraoComprovanteMatricula = ?, enviarMensagemNotificacaoFrequenciaAula = ?, numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula = ?, ");
            sql.append(" porcentagemConsiderarFrequenciaAulaBaixa = ?, periodicidadeNotificacaoFrequenciaAula=?, controlarNotificacaoPendenciaDocumentos=?, ");
            sql.append(" nrDiaAposInicioAulaNotificarPendenciaDocumento=?, periodicidadeNotificarPendenciaDocumento=?, primeiraNotificacaoNaoLancamentoRegistroAula=?, ");
            sql.append(" segundaNotificacaoNaoLancamentoRegistroAula = ?,nrDiasQuartoAvisoRiscoSuspensao=?,periodicidadeQuartoAvisoRiscoSuspensao=?, ");
            sql.append(" terceiraNotificacaoNaoLancamentoRegistroAula=?, quartaNotificacaoNaoLancamentoRegistroAula=? ");
            sql.append("  WHERE ((codigo = ?))");
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setBoolean(2, obj.isPadrao().booleanValue());
                    sqlAlterar.setString(3, obj.getMensagemPlanoDeEstudo());
                    sqlAlterar.setString(4, obj.getMensagemBoletimAcademico());
                    sqlAlterar.setBoolean(5, obj.getControlarSuspensaoMatriculaPendenciaDocumentos());
                    sqlAlterar.setInt(6, obj.getNrDiasPrimeiroAvisoRiscoSuspensao());
                    sqlAlterar.setInt(7, obj.getNrDiasSegundoAvisoRiscoSuspensao());
                    sqlAlterar.setInt(8, obj.getNrDiasTerceiroAvisoRiscoSuspensao());
                    sqlAlterar.setInt(9, obj.getNrDiasSuspenderMatriculaPendenciaDocumentos());
                    sqlAlterar.setInt(10, obj.getNrDiasListarMatriculaPendenciaDocumentosParaCancelamento());
                    sqlAlterar.setString(11, obj.getLayoutPadraoComprovanteMatricula().toString());
                    sqlAlterar.setBoolean(12, obj.getEnviarMensagemNotificacaoFrequenciaAula());
                    sqlAlterar.setInt(13, obj.getNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula());
                    sqlAlterar.setInt(14, obj.getPorcentagemConsiderarFrequenciaAulaBaixa());
                    sqlAlterar.setInt(15, obj.getPeriodicidadeNotificacaoFrequenciaAula());
                    sqlAlterar.setBoolean(16, obj.getControlarNotificacaoPendenciaDocumentos());
                    sqlAlterar.setInt(17, obj.getNrDiaAposInicioAulaNotificarPendenciaDocumento());
                    sqlAlterar.setInt(18, obj.getPeriodicidadeNotificarPendenciaDocumento());
                    sqlAlterar.setInt(19, obj.getPrimeiraNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(20, obj.getSegundaNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(21, obj.getNrDiasQuartoAvisoRiscoSuspensao());
                    sqlAlterar.setInt(22, obj.getPeriodicidadeQuartoAvisoRiscoSuspensao());
                    sqlAlterar.setInt(23, obj.getTerceiraNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(24, obj.getQuartaNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(25, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            //salvarConfiguracoesRelacionadas(configs, obj);
        } catch (Exception e) {
            carregarNovamenteConfiguracoes(configs, obj, configuracoesControle);
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSomenteConfiguracores(final ConfiguracoesVO obj) throws Exception {
        try {
            Configuracoes.alterar(getIdEntidade());
            ConfiguracoesVO.validarDados(obj);
            verificarUnicidadeDePadrao(obj);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder("UPDATE Configuracoes set nome=?, padrao=?, mensagemPlanoDeEstudo=?, mensagemBoletimAcademico=?,");
            sql.append(" controlarSuspensaoMatriculaPendenciaDocumentos=?, nrDiasPrimeiroAvisoRiscoSuspensao=?, nrDiasSegundoAvisoRiscoSuspensao=?, ");
            sql.append(" nrDiasTerceiroAvisoRiscoSuspensao=?, nrDiasSuspenderMatriculaPendenciaDocumentos=?, nrDiasListarMatriculaPendenciaDocumentosParaCancelamento=?, ");
            sql.append(" layoutPadraoComprovanteMatricula = ?, enviarMensagemNotificacaoFrequenciaAula = ?, numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula = ?, ");
            sql.append(" porcentagemConsiderarFrequenciaAulaBaixa = ?, periodicidadeNotificacaoFrequenciaAula=?, controlarNotificacaoPendenciaDocumentos=?, ");
            sql.append(" nrDiaAposInicioAulaNotificarPendenciaDocumento=?, periodicidadeNotificarPendenciaDocumento=?, primeiraNotificacaoNaoLancamentoRegistroAula=?, ");
            sql.append(" segundaNotificacaoNaoLancamentoRegistroAula = ?,nrDiasQuartoAvisoRiscoSuspensao=?,periodicidadeQuartoAvisoRiscoSuspensao=?, ");
            sql.append(" terceiraNotificacaoNaoLancamentoRegistroAula=?, quartaNotificacaoNaoLancamentoRegistroAula=? ");
            sql.append("  WHERE ((codigo = ?))");
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setBoolean(2, obj.isPadrao().booleanValue());
                    sqlAlterar.setString(3, obj.getMensagemPlanoDeEstudo());
                    sqlAlterar.setString(4, obj.getMensagemBoletimAcademico());
                    sqlAlterar.setBoolean(5, obj.getControlarSuspensaoMatriculaPendenciaDocumentos());
                    sqlAlterar.setInt(6, obj.getNrDiasPrimeiroAvisoRiscoSuspensao());
                    sqlAlterar.setInt(7, obj.getNrDiasSegundoAvisoRiscoSuspensao());
                    sqlAlterar.setInt(8, obj.getNrDiasTerceiroAvisoRiscoSuspensao());
                    sqlAlterar.setInt(9, obj.getNrDiasSuspenderMatriculaPendenciaDocumentos());
                    sqlAlterar.setInt(10, obj.getNrDiasListarMatriculaPendenciaDocumentosParaCancelamento());
                    sqlAlterar.setString(11, obj.getLayoutPadraoComprovanteMatricula().toString());
                    sqlAlterar.setBoolean(12, obj.getEnviarMensagemNotificacaoFrequenciaAula());
                    sqlAlterar.setInt(13, obj.getNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula());
                    sqlAlterar.setInt(14, obj.getPorcentagemConsiderarFrequenciaAulaBaixa());
                    sqlAlterar.setInt(15, obj.getPeriodicidadeNotificacaoFrequenciaAula());
                    sqlAlterar.setBoolean(16, obj.getControlarNotificacaoPendenciaDocumentos());
                    sqlAlterar.setInt(17, obj.getNrDiaAposInicioAulaNotificarPendenciaDocumento());
                    sqlAlterar.setInt(18, obj.getPeriodicidadeNotificarPendenciaDocumento());
                    sqlAlterar.setInt(19, obj.getPrimeiraNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(20, obj.getSegundaNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(21, obj.getNrDiasQuartoAvisoRiscoSuspensao());
                    sqlAlterar.setInt(22, obj.getPeriodicidadeQuartoAvisoRiscoSuspensao());
                    sqlAlterar.setInt(23, obj.getTerceiraNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(24, obj.getQuartaNotificacaoNaoLancamentoRegistroAula());
                    sqlAlterar.setInt(25, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }    

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracoesVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ConfiguracoesVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ConfiguracoesVO obj, List<ConfiguracaoControleInterface> configs, ConfiguracoesControle configuracoesControle, UsuarioVO usuarioVO) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuarioVO);
            excluirConfiguracoesRelacionadas(configs, obj, configuracoesControle);
            String sql = "DELETE FROM Configuracoes WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirConfiguracoesRelacionadas(List<ConfiguracaoControleInterface> configs, ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) throws Exception {
        try {
            for (ConfiguracaoControleInterface config : configs) {
                config.excluir();
            }
        } catch (Exception e) {
            carregarNovamenteConfiguracoes(configs, configuracoesVO, configuracoesControle);
            throw e;
        }
    }

    public void carregarNovamenteConfiguracoes(List<ConfiguracaoControleInterface> configs, ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) throws Exception {
        for (ConfiguracaoControleInterface config : configs) {
            config.iniciarControleConfiguracao(configuracoesVO, configuracoesControle);
        }
    }

    public ConfiguracoesVO consultarConfiguracaoPadrao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	return consultarConfiguracaoPadraoSemControleAcesso();
    }

    public ConfiguracoesVO consultarConfiguracaoPadraoSemControleAcesso() throws Exception {
    	return consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_TODOS, null, null);
    }

    public ConfiguracoesVO consultarConfiguracaoUnidadeEnsino(Integer unidadeEnsinoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
       return consultarConfiguracaoASerUsada(controlarAcesso, nivelMontarDados, usuario, unidadeEnsinoMatricula);
    }
    
    public ConfiguracoesVO consultarConfiguracaoASerUsada(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer unidadeEnsino) throws Exception {
    	if(!Uteis.isAtributoPreenchido(unidadeEnsino) && usuario != null && usuario.getUnidadeEnsinoLogado() != null && usuario.getUnidadeEnsinoLogado().getCodigo() != null && usuario.getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
    		unidadeEnsino = usuario.getUnidadeEnsinoLogado().getCodigo();
    	}
    	return getAplicacaoControle().getConfiguracaoGeralSistemaVO(unidadeEnsino, usuario).getConfiguracoesVO();        
    }

    @Override
    public List<ConfiguracoesVO> listarTodasConfiguracoes(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM configuracoes";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de <code>Configuracoes</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ConfiguracoesVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ConfiguracoesVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Configuracoes WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de <code>Configuracoes</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ConfiguracoesVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ConfiguracoesVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Configuracoes WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ConfiguracoesVO</code> resultantes da consulta.
     */
    public static List<ConfiguracoesVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<ConfiguracoesVO> vetResultado = new ArrayList<ConfiguracoesVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ConfiguracoesVO</code>.
     *
     * @return O objeto da classe <code>ConfiguracoesVO</code> com os dados devidamente montados.
     */
    public static ConfiguracoesVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ////System.out.println(">> Montar dados(Configuracao) - " + new Date());
        ConfiguracoesVO obj = new ConfiguracoesVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setPadrao(dadosSQL.getBoolean("padrao"));
        obj.setMensagemPlanoDeEstudo(dadosSQL.getString("mensagemPlanoDeEstudo"));
        obj.setMensagemBoletimAcademico(dadosSQL.getString("mensagemBoletimAcademico"));
        obj.setNrDiasPrimeiroAvisoRiscoSuspensao(dadosSQL.getInt("nrDiasPrimeiroAvisoRiscoSuspensao"));
        obj.setNrDiasSegundoAvisoRiscoSuspensao(dadosSQL.getInt("nrDiasSegundoAvisoRiscoSuspensao"));
        obj.setNrDiasTerceiroAvisoRiscoSuspensao(dadosSQL.getInt("nrDiasTerceiroAvisoRiscoSuspensao"));
        obj.setNrDiasSuspenderMatriculaPendenciaDocumentos(dadosSQL.getInt("nrDiasSuspenderMatriculaPendenciaDocumentos"));
        obj.setNrDiasListarMatriculaPendenciaDocumentosParaCancelamento(dadosSQL.getInt("nrDiasListarMatriculaPendenciaDocumentosParaCancelamento"));
        obj.setControlarSuspensaoMatriculaPendenciaDocumentos(dadosSQL.getBoolean("controlarSuspensaoMatriculaPendenciaDocumentos"));
        obj.setLayoutPadraoComprovanteMatricula(LayoutComprovanteMatriculaEnum.valueOf(dadosSQL.getString("layoutPadraoComprovanteMatricula")));
        obj.setEnviarMensagemNotificacaoFrequenciaAula(dadosSQL.getBoolean("enviarMensagemNotificacaoFrequenciaAula"));
        obj.setNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula(dadosSQL.getInt("numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula"));
        obj.setPorcentagemConsiderarFrequenciaAulaBaixa(dadosSQL.getInt("porcentagemConsiderarFrequenciaAulaBaixa"));
        obj.setPeriodicidadeNotificacaoFrequenciaAula(dadosSQL.getInt("periodicidadeNotificacaoFrequenciaAula"));
        obj.setControlarNotificacaoPendenciaDocumentos(dadosSQL.getBoolean("controlarNotificacaoPendenciaDocumentos"));
        obj.setNrDiaAposInicioAulaNotificarPendenciaDocumento(dadosSQL.getInt("nrDiaAposInicioAulaNotificarPendenciaDocumento"));
        obj.setPeriodicidadeNotificarPendenciaDocumento(dadosSQL.getInt("periodicidadeNotificarPendenciaDocumento"));
        obj.setPrimeiraNotificacaoNaoLancamentoRegistroAula(dadosSQL.getInt("primeiraNotificacaoNaoLancamentoRegistroAula"));
        obj.setSegundaNotificacaoNaoLancamentoRegistroAula(dadosSQL.getInt("segundaNotificacaoNaoLancamentoRegistroAula"));
        obj.setTerceiraNotificacaoNaoLancamentoRegistroAula(dadosSQL.getInt("terceiraNotificacaoNaoLancamentoRegistroAula"));
        obj.setQuartaNotificacaoNaoLancamentoRegistroAula(dadosSQL.getInt("quartaNotificacaoNaoLancamentoRegistroAula"));
        obj.setNrDiasQuartoAvisoRiscoSuspensao(dadosSQL.getInt("nrDiasQuartoAvisoRiscoSuspensao"));
        obj.setPeriodicidadeQuartoAvisoRiscoSuspensao(dadosSQL.getInt("periodicidadeQuartoAvisoRiscoSuspensao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ConfiguracoesVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ConfiguracoesVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Configuracoes WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Configuracoes ).");
        }
        return montarDados(tabelaResultado, nivelMontarDados);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Configuracoes.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Configuracoes.idEntidade = idEntidade;
    }
}
