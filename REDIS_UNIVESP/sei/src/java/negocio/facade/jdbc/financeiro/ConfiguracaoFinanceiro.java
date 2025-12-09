package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.HistoricoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.financeiro.enumerador.AmbienteCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.EmpresaOperadoraCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoParcelaNegociarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ConfiguracaoFinanceiroInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ConfiguracaoFinanceiroVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ConfiguracaoFinanceiroVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see ConfiguracaoFinanceiroVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoFinanceiro extends ControleAcesso implements ConfiguracaoFinanceiroInterfaceFacade {

	private static final long serialVersionUID = 7368828251970401730L;

	protected static String idEntidade;

    public ConfiguracaoFinanceiro() throws Exception {
        super();
        setIdEntidade("ConfiguracaoFinanceiro");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>ConfiguracaoFinanceiroVO</code>.
     */
    public ConfiguracaoFinanceiroVO novo() throws Exception {
        ConfiguracaoFinanceiro.incluir(getIdEntidade());
        ConfiguracaoFinanceiroVO obj = new ConfiguracaoFinanceiroVO();
        return obj;
    }

    public void validarMascaraPadraoPlanoConta(ConfiguracaoFinanceiroVO obj) throws Exception {
        String mascara = obj.getMascaraPlanoConta();
        if (mascara != null) {
            int niveis = obj.getNrNiveisPlanoConta().intValue();
            int posicao = mascara.indexOf(".");
            int quantidadePonto = 0;
            while (posicao != -1) {
                quantidadePonto++;
                mascara = mascara.substring(posicao + 1);
                posicao = mascara.indexOf(".");
            }
            if (quantidadePonto != (niveis - 1)) {
                throw new Exception("A quantidade de níveis informado não corresponde a máscara do plano de conta.");
            }
        }
    }

    public void validarMascaraPadraoCategoriaDespesa(ConfiguracaoFinanceiroVO obj) throws Exception {
        String mascara = obj.getMascaraCategoriaDespesa();
        if (mascara != null) {
            int niveis = obj.getNrNiveisCategoriaDespesa().intValue();
            int posicao = mascara.indexOf(".");
            int quantidadePonto = 0;
            while (posicao != -1) {
                quantidadePonto++;
                mascara = mascara.substring(posicao + 1);
                posicao = mascara.indexOf(".");
            }
            if (quantidadePonto != (niveis - 1)) {
                throw new Exception("A quantidade de níveis informado não corresponde a máscara do centro despesa.");
            }
        }
    }

    public void validarSeMascaraDeCategoriaDespesaFoiAlterada(ConfiguracaoFinanceiroVO obj) throws Exception {
        if (getFacadeFactory().getCategoriaDespesaFacade().consultarSeExisteCategoriaDespesa()) {
            String sql = "select mascaracategoriadespesa = ? and nrniveiscategoriadespesa = ? as integridade from configuracaofinanceiro";
            SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{obj.getMascaraCategoriaDespesa(), obj.getNrNiveisCategoriaDespesa()});
            boolean integridade = true;
            if (resultado.next()) {
                integridade = resultado.getBoolean("integridade");
            }
            if (!integridade) {
//                throw new Exception("A Configuração referente a Categoria de Despesas não pode ser alterada.");
            }
        }
    }

    public void validarMascaraPadraoCentroReceita(ConfiguracaoFinanceiroVO obj) throws Exception {
        String mascara = obj.getMascaraCentroReceita();
        if (mascara != null) {
            int niveis = obj.getNrNiveisCentroReceita().intValue();
            int posicao = mascara.indexOf(".");
            int quantidadePonto = 0;
            while (posicao != -1) {
                quantidadePonto++;
                mascara = mascara.substring(posicao + 1);
                posicao = mascara.indexOf(".");
            }
            if (quantidadePonto != (niveis - 1)) {
                throw new Exception("A quantidade de níveis informado não corresponde a máscara do centro receita.");
            }
        }
    }
    
    public void validarMensagemNotificacaoMatriculaNaoPagaConsultor(ConfiguracaoFinanceiroVO obj) throws Exception {
        if (obj.getEnviarNotificacaoConsultorMatricula() && obj.getNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor() == null) { 
        	throw new Exception("A quantidade de dias para notificação sobre a matrícula não paga deve ser informada.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>ConfiguracaoFinanceiroVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>ConfiguracaoFinanceiroVO</code> que
     *            será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ConfiguracaoFinanceiroVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            validarMascaraPadraoCategoriaDespesa(obj);
            validarMascaraPadraoCentroReceita(obj);
            validarMascaraPadraoPlanoConta(obj);
            validarSeMascaraDeCategoriaDespesaFoiAlterada(obj);
            validarMensagemNotificacaoMatriculaNaoPagaConsultor(obj);
            validarBloqueioEmissaoBoletoPagamentoVencidoVisaoAluno(obj);
            if(!obj.getArquivoIreportMovFin().getNome().equals("")){
				obj.getArquivoIreportMovFin().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoIreportMovFin(), false, usuario, configuracaoGeralSistemaVO);
			}
            final String sql = "INSERT INTO ConfiguracaoFinanceiro( percentualJuroPadrao, percentualMultaPadrao, tipoCalculoJuro, descontoProgressivoPadrao, diaVencimentoParcelasPadrao, " //1-5
                    + "diaVencimentoMatriculaPadrao, nrNiveisPlanoConta, mascaraPlanoConta, nrNiveisCategoriaDespesa, mascaraCategoriaDespesa, " //5-10
                    + "nrNiveisCentroReceita, mascaraCentroReceita, centroReceitaMensalidadePadrao, centroReceitaMatriculaPadrao, centroReceitaRequerimentoPadrao, " //11-15
                    + "centroReceitaBibliotecaPadrao, centroReceitaInscricaoProcessoSeletivoPadrao, contaCorrentePadraoBiblioteca, contaCorrentePadraoMatricula, contaCorrentePadraoMensalidade, " //16-20
                    + "contaCorrentePadraoRequerimento, contaCorrentePadraoProcessoSeletivo, contaCorrentePadraoNegociacao, centroReceitaNegociacaoPadrao, formaPagamentoPadraoCheque, " //21-25
                    + "categoriaDespesaPadraoAntecipacaoCheque, departamentoPadraoAntecipacaoCheque, formaPagamentoPadraoProvisaoCusto, mensagempadraonotificacao, assuntopadraonotificacao,  " //26-30
                    + "configuracoes, modeloboletomatricula, modeloboletomensalidade, modeloboletoprocessoseletivo, modeloboletorequerimento, " //31-35                    
                    + "modeloboletooutros, formapagamentocontrolecobranca, ContaCorrentePadraoControleCobranca, planoContaPagarPadraoJuro, planoContaPagarPadraoDesconto, " //36-40
                    + "planoContaPagarPadraoCredito, planoContaPagarPadraoDebito, planoContaReceberPadraoJuro, planoContaReceberPadraoDesconto, historicoContaReceberPadraoJuro, " //41-45
                    + "planoContaReceberPadraoCredito, planoContaReceberPadraoDebito, historicoContaPagarPadraoJuro, historicoContaPagarPadraoDesconto,  historicoContaReceberPadraoDesconto, " //46- 50
                    + "ordemConvenio, ordemConvenioValorCheio, ordemDescontoAluno, ordemDescontoAlunoValorCheio, ordemDescontoProgressivo, " //51-55
                    + "ordemDescontoProgressivoValorCheio, ordemPlanoDesconto, ordemPlanoDescontoValorCheio, qtdeParcelasNegativacaoSerasa, qtdeMinimaDiasAntesNegativacaoSerasa,  " //56-60
                    + "usaChancela, cobrarReimpressaoBoletos, modeloBoletoReimpressao, contaCorrenteReimpressaoBoletos, centroReceitaReimpressaoBoletos, " //61-65                    
                    + "valorCobrarReimpressaoBoletos, usaDescontoCompostoPlanoDesconto, usaPlanoOrcamentario, gerarBoletoComDescontoSemValidade, vencimentoDescontoProgressivoDiaUtil, " //66-70
                    + "obrigatorioSelecionarUnidadeEnsinoControleCobranca, utilizaPlanoFinanceiroReposicao, utilizaPlanoFinanceiroInclusao, imprimirBoletoComLogoBanco, usarContaCorrenteTurmaIncluida, " //71-75
                    + "recomendacaoRenegociacaoVisaoAluno, numeroDiasNotificarVencimentoContaReceber, enviarNotificacaoConsultorMatricula, numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor, vencimentoParcelaDiaUtil, " // 76-80
                    + "ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula, textoPadraoCartaCobranca, quantidadeDiasEnviarMensagemCobrancaInadimplente, periodicidadeDiasEnviarMensagemCobrancaInadimplente, grupoDestinatarioMensagemCobrancaInadimplente," //81-85
                    + "modeloBoletoRenegociacao, permitirGerarParcelaPreMatricula, emailEnviarNotificacaoConsultorMatricula, numeroDiasBloquearAcessoAlunoInadimplente, tipoEnvioInadimplencia,  " //86-90
                    + "quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente, quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente, quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente, quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno, naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor, " //91-95
                    + "naoApresAlunoInadimplenteDiarioEspelhoNota, qtdDiasAntesVencimentoApresentarContaReceberMatVisaoAluno, centroReceitaReposicaoPadrao , quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente, centroReceitaParcelaAvulsaControleCobranca, "//96-100                                       
                    + "cancelarContaReceberCandidatoInadimplenteAposDataProva, qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente, categoriaDespesaOperadoraCartao,excluirNegociacaoRecebimentoVencida, permitiVisualizarContaReceberVisaoAlunoPreMatricula,  "// 101-105
                    + "cobrarJuroMultaSobreValorCheioConta, realizarMatriculaComFinanceiroManualAtivo, utilizarIntegracaoFinanceira, idClienteMundiPag, confirmarMatricPendFinanCasoNaoControleMatricula, " //106-110
                    + "contaCorrentePadraoDevolucaoCheque, ambienteCartaoCredito, imprimirBoletoComImagemLinhaDigitavel, chaveContaMundipagg, bloquearCalouroPagarMatriculaVisaoAluno,  " //111-115
                    + "bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente, permiteNegociarParcelaMatricula, permiteNegociarParcelaMensalidade,  permiteNegociarParcelaBiblioteca, permiteNegociarParcelaOutras,  " //116-120
                    + "permiteNegociarParcelaContratoReceita, permiteNegociarParcelaDevolucaoCheque, permiteNegociarParcelaNegociacao, permiteNegociarParcelaInclusaoReposicao, modeloBoletoBiblioteca, " //121-123                                                           
                    + "apresentarFormaRecebimentoContaReceberVisaoAluno, criarContaReceberPendenciaArquivoRetornoAutomaticamente, categoriaDespesaPadraoRestituicaoAluno, arquivoIreportMovFin, alterarDataVencimentoParcelaDiaUtil, merchantIdCielo, merchantKeyCielo, observacaoComprovanteRecebimento, " //125
                    + "modeloBoletoMaterialDidatico, centroReceitaMaterialDidaticoPadrao, contaCorrentePadraoMaterialDidatico, permiteNegociarParcelaMaterialDidatico, valorMinimoGerarPendenciaControleCobranca, valorMaximoCompraDiretaRequisicao, "
                    + "quantidadeDiasEnviarAvisoDesconto, indiceReajustePadraoContasPorAtraso, qtdDiasAplicarIndireReajustePorAtrasoContaReceber , qtdeDiasExcluirNegociacaoContaReceberVencida, categoriaDespesa, bancoPadraoRemessa, formaPagamentoPadrao, tokenRede, pvRede, operadora, "
                    + "tipoOrigemMatriculaRotinaInadimplencia, tipoOrigemBibliotecaRotinaInadimplencia, tipoOrigemMensalidadeRotinaInadimplencia, tipoOrigemDevolucaoChequeRotinaInadimplencia, "
                    + "tipoOrigemNegociacaoRotinaInadimplencia, tipoOrigemContratoReceitaRotinaInadimplencia, tipoOrigemOutrosRotinaInadimplencia, tipoOrigemMaterialDidaticoRotinaInadimplencia, "
                    + "tipoOrigemInclusaoReposicaoRotinaInadimplencia, nomeParcelaMatriculaApresentarAluno, nomeParcelaMaterialDidaticoApresentarAluno, "
                    + "siglaParcelaMatriculaApresentarAluno, siglaParcelaMaterialDidaticoApresentarAluno, bloquearEmissaoBoletoPagamentoVencidoVisaoAluno, bloquearDemaisParcelasVencidas , quantidadeDiasAtrasos, "
                    + "bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco, tipoparcelanegociar, filtroPadraoContaReceberVisaoAluno ) "
                    
                    + "VALUES ( "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //1-10
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //11-20
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //21-30
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //31-40
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //41-50
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //51-60
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //61-70
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //71-80
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //81-90
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //91-100
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //101-110
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //111-120
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //121-130
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //131-133
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ? "
                    +") returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario); //37-86
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlInserir.setDouble(++i, obj.getPercentualJuroPadrao().doubleValue()); //1
                    sqlInserir.setDouble(++i, obj.getPercentualMultaPadrao().doubleValue()); //2
                    sqlInserir.setString(++i, obj.getTipoCalculoJuro()); //3
                    if (obj.getDescontoProgressivoPadrao().getCodigo().intValue() != 0) { //4
                        sqlInserir.setInt(++i, obj.getDescontoProgressivoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setInt(++i, obj.getDiaVencimentoParcelasPadrao().intValue());//5
                    sqlInserir.setInt(++i, obj.getDiaVencimentoMatriculaPadrao().intValue());//6
                    sqlInserir.setInt(++i, obj.getNrNiveisPlanoConta().intValue());//7
                    sqlInserir.setString(++i, obj.getMascaraPlanoConta());//8
                    sqlInserir.setInt(++i, obj.getNrNiveisCategoriaDespesa().intValue());//9
                    sqlInserir.setString(++i, obj.getMascaraCategoriaDespesa());//10
                    sqlInserir.setInt(++i, obj.getNrNiveisCentroReceita().intValue());//11
                    sqlInserir.setString(++i, obj.getMascaraCentroReceita());//12
                    if (obj.getCentroReceitaMensalidadePadrao().getCodigo().intValue() != 0) {//13
                        sqlInserir.setInt(++i, obj.getCentroReceitaMensalidadePadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaMatriculaPadrao().getCodigo().intValue() != 0) {//14
                        sqlInserir.setInt(++i, obj.getCentroReceitaMatriculaPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue() != 0) {//15
                        sqlInserir.setInt(++i, obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaBibliotecaPadrao().getCodigo().intValue() != 0) {//16
                        sqlInserir.setInt(++i, obj.getCentroReceitaBibliotecaPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo().intValue() != 0) {//17
                        sqlInserir.setInt(++i, obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoBiblioteca().intValue() != 0) {//18
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoBiblioteca().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoMatricula().intValue() != 0) {//19
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoMatricula().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoMensalidade().intValue() != 0) {//20
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoMensalidade().intValue());                         
                    } else {                                                                                                
                        sqlInserir.setNull(++i, 0);                                                                         
                    }                                                                                                       
                    if (obj.getContaCorrentePadraoRequerimento().intValue() != 0) {//21
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoRequerimento().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoProcessoSeletivo().intValue() != 0) {//22
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoProcessoSeletivo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoNegociacao().intValue() != 0) {//23
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoNegociacao().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaNegociacaoPadrao().getCodigo().intValue() != 0) {//24
                        sqlInserir.setInt(++i, obj.getCentroReceitaNegociacaoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getFormaPagamentoPadraoCheque().getCodigo().intValue() != 0) {//25
                        sqlInserir.setInt(++i, obj.getFormaPagamentoPadraoCheque().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo().intValue() != 0) {//26
                        sqlInserir.setInt(++i, obj.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getDepartamentoPadraoAntecipacaoCheque().getCodigo().intValue() != 0) {//27
                        sqlInserir.setInt(++i, obj.getDepartamentoPadraoAntecipacaoCheque().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getFormaPagamentoPadraoProvisaoCusto().getCodigo().intValue() != 0) {//28
                        sqlInserir.setInt(++i, obj.getFormaPagamentoPadraoProvisaoCusto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getMensagemPadraoNotificacao());//29
                    sqlInserir.setString(++i, obj.getAssuntoPadraoNotificacao());//30
                    if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {//31
                        sqlInserir.setInt(++i, obj.getConfiguracoesVO().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoMatricula().getCodigo().intValue() != 0) {//32
                        sqlInserir.setInt(++i, obj.getModeloBoletoMatricula().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoMensalidade().getCodigo().intValue() != 0) {//33
                        sqlInserir.setInt(++i, obj.getModeloBoletoMensalidade().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoProcessoSeletivo().getCodigo().intValue() != 0) {//34
                        sqlInserir.setInt(++i, obj.getModeloBoletoProcessoSeletivo().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoRequerimento().getCodigo().intValue() != 0) {//35
                        sqlInserir.setInt(++i, obj.getModeloBoletoRequerimento().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoOutros().getCodigo().intValue() != 0) {//36
                        sqlInserir.setInt(++i, obj.getModeloBoletoOutros().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getFormaPagamentoPadraoControleCobranca().getCodigo().intValue() != 0) {//37
                        sqlInserir.setInt(++i, obj.getFormaPagamentoPadraoControleCobranca().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoControleCobranca().getCodigo().intValue() != 0) {//38
                        sqlInserir.setInt(++i, obj.getContaCorrentePadraoControleCobranca().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaPagarPadraoJuro().getCodigo().intValue() != 0) {//39
                        sqlInserir.setInt(++i, obj.getPlanoContaPagarPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaPagarPadraoDesconto().getCodigo().intValue() != 0) {//40
                        sqlInserir.setInt(++i, obj.getPlanoContaPagarPadraoDesconto().getCodigo().intValue());
                    } else {                                                                                                
                        sqlInserir.setNull(++i, 0);                                                                         
                    }                                                                                                       
                    if (obj.getPlanoContaPagarPadraoCredito().getCodigo().intValue() != 0) {//41
                        sqlInserir.setInt(++i, obj.getPlanoContaPagarPadraoCredito().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaPagarPadraoDebito().getCodigo().intValue() != 0) {//42
                        sqlInserir.setInt(++i, obj.getPlanoContaPagarPadraoDebito().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoJuro().getCodigo().intValue() != 0) {//43
                        sqlInserir.setInt(++i, obj.getPlanoContaReceberPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoDesconto().getCodigo().intValue() != 0) {//44
                        sqlInserir.setInt(++i, obj.getPlanoContaReceberPadraoDesconto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoCredito().getCodigo().intValue() != 0) {//45
                        sqlInserir.setInt(++i, obj.getPlanoContaReceberPadraoCredito().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoDebito().getCodigo().intValue() != 0) {//46
                        sqlInserir.setInt(++i, obj.getPlanoContaReceberPadraoDebito().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaPagarPadraoJuro().getCodigo().intValue() != 0) {//47
                        sqlInserir.setInt(++i, obj.getHistoricoContaPagarPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaPagarPadraoDesconto().getCodigo().intValue() != 0) {//48
                        sqlInserir.setInt(++i, obj.getHistoricoContaPagarPadraoDesconto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaReceberPadraoJuro().getCodigo().intValue() != 0) {//49
                        sqlInserir.setInt(++i, obj.getHistoricoContaReceberPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaReceberPadraoDesconto().getCodigo().intValue() != 0) {//50
                        sqlInserir.setInt(++i, obj.getHistoricoContaReceberPadraoDesconto().getCodigo().intValue());        
                    } else {                                                                                                
                        sqlInserir.setNull(++i, 0);                                                                         
                    }                                                                                                       
                    sqlInserir.setInt(++i, obj.getOrdemConvenio());//51
                    sqlInserir.setBoolean(++i, obj.getOrdemConvenioValorCheio());//52
                    sqlInserir.setInt(++i, obj.getOrdemDescontoAluno());//53
                    sqlInserir.setBoolean(++i, obj.getOrdemDescontoAlunoValorCheio());//54
                    sqlInserir.setInt(++i, obj.getOrdemDescontoProgressivo());//55
                    sqlInserir.setBoolean(++i, obj.getOrdemDescontoProgressivoValorCheio());//56
                    sqlInserir.setInt(++i, obj.getOrdemPlanoDesconto());//57
                    sqlInserir.setBoolean(++i, obj.getOrdemPlanoDescontoValorCheio());//58
                    sqlInserir.setInt(++i, obj.getQtdeParcelasNegativacaoSerasa());//59
                    sqlInserir.setInt(++i, obj.getQtdeMinimaDiasAntesNegativacaoSerasa());//60
                    sqlInserir.setBoolean(++i, obj.getUsaChancela());//61
                    sqlInserir.setBoolean(++i, obj.getCobrarReimpressaoBoletos());//62
                    if (obj.getModeloBoletoReimpressao().getCodigo().intValue() != 0) {//63
                        sqlInserir.setInt(++i, obj.getModeloBoletoReimpressao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getContaCorrenteReimpressaoBoletos().getCodigo().intValue() != 0) {//64
                        sqlInserir.setInt(++i, obj.getContaCorrenteReimpressaoBoletos().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaReimpressaoBoletos().getCodigo().intValue() != 0) {//65
                        sqlInserir.setInt(++i, obj.getCentroReceitaReimpressaoBoletos().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setDouble(++i, obj.getValorCobrarReimpressaoBoletos());//66
                    sqlInserir.setBoolean(++i, obj.getUsaDescontoCompostoPlanoDesconto());//67
                    sqlInserir.setBoolean(++i, obj.getUsaPlanoOrcamentario());//68
                    sqlInserir.setBoolean(++i, obj.getGerarBoletoComDescontoSemValidade());//69
                    sqlInserir.setBoolean(++i, obj.getVencimentoDescontoProgressivoDiaUtil());//70
                    sqlInserir.setBoolean(++i, obj.getObrigatorioSelecionarUnidadeEnsinoControleCobranca());//71
                    sqlInserir.setBoolean(++i, obj.getUtilizaPlanoFinanceiroReposicao());//72
                    sqlInserir.setBoolean(++i, obj.getUtilizaPlanoFinanceiroInclusao());//73
                    sqlInserir.setBoolean(++i, obj.getImprimirBoletoComLogoBanco());//74
                    sqlInserir.setBoolean(++i, obj.getUsarContaCorrenteTurmaIncluida());//75
                    sqlInserir.setString(++i, obj.getRecomendacaoRenegociacaoVisaoAluno().trim());//76
                    sqlInserir.setInt(++i, obj.getNumeroDiasNotificarVencimentoContaReceber());//77
                    sqlInserir.setBoolean(++i, obj.getEnviarNotificacaoConsultorMatricula());//78
                    sqlInserir.setInt(++i, obj.getNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor());//79
                    sqlInserir.setBoolean(++i, obj.getVencimentoParcelaDiaUtil());//80
                    sqlInserir.setBoolean(++i, obj.getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula());//81
                    sqlInserir.setString(++i, obj.getTextoPadraoCartaCobranca());//82
                    sqlInserir.setInt(++i, obj.getQuantidadeDiasEnviarMensagemCobrancaInadimplente());//83
                    sqlInserir.setInt(++i, obj.getPeriodicidadeDiasEnviarMensagemCobrancaInadimplente());//84
                    if(obj.getGrupoDestinatarioMensagemCobrancaInadimplente().getCodigo() != null && obj.getGrupoDestinatarioMensagemCobrancaInadimplente().getCodigo() >0){//85
                        sqlInserir.setInt(++i, obj.getGrupoDestinatarioMensagemCobrancaInadimplente().getCodigo());
                    }else{
                        sqlInserir.setNull(++i, 0);
                    }
                    if(obj.getModeloBoletoRenegociacao().getCodigo() != null && obj.getModeloBoletoRenegociacao().getCodigo() >0){//86
                    	sqlInserir.setInt(++i, obj.getModeloBoletoRenegociacao().getCodigo());
                    }else{
                    	sqlInserir.setNull(++i, 0);
                    }
                	sqlInserir.setBoolean(++i, obj.getPermitirGerarParcelaPreMatricula());//87
                	sqlInserir.setString(++i, obj.getEmailEnviarNotificacaoConsultorMatricula());//88
                	sqlInserir.setInt(++i, obj.getNumeroDiasBloquearAcessoAlunoInadimplente());//89
                	sqlInserir.setString(++i, obj.getTipoEnvioInadimplencia());//90
                	sqlInserir.setInt(++i, obj.getQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente());//91
                	sqlInserir.setInt(++i, obj.getQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente());//92
                	sqlInserir.setInt(++i, obj.getQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente());//93
                	sqlInserir.setInt(++i, obj.getQuantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno());//94                	
                	sqlInserir.setBoolean(++i, obj.getNaoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor());//95
                	sqlInserir.setBoolean(++i, obj.getNaoApresAlunoInadimplenteDiarioEspelhoNota());//96
                	sqlInserir.setInt(++i, obj.getQuantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno());//97
                    if (obj.getCentroReceitaReposicaoPadrao().getCodigo().intValue() != 0) {//98
                        sqlInserir.setInt(++i, obj.getCentroReceitaReposicaoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setInt(++i, obj.getQuantidadeDiasEnviarQuartaMensagemCobrancaInadimplente());//99
                    if (obj.getCentroReceitaParcelaAvulsaControleCobranca().getCodigo().intValue() != 0) {//100
                        sqlInserir.setInt(++i, obj.getCentroReceitaParcelaAvulsaControleCobranca().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getCancelarContaReceberCandidatoInadimplenteAposDataProva());//101
                    sqlInserir.setInt(++i, obj.getQtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente());//102
                    if (obj.getCategoriaDespesaOperadoraCartao().getCodigo().intValue() != 0) {//103
                        sqlInserir.setInt(++i, obj.getCategoriaDespesaOperadoraCartao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getExcluirNegociacaoRecebimentoVencida());//104
                    sqlInserir.setBoolean(++i, obj.getPermitiVisualizarContaReceberVisaoAlunoPreMatricula());//105
                    sqlInserir.setBoolean(++i, obj.getCobrarJuroMultaSobreValorCheioConta());//106
                    sqlInserir.setBoolean(++i, obj.getRealizarMatriculaComFinanceiroManualAtivo());//107
                    sqlInserir.setBoolean(++i, obj.getUtilizarIntegracaoFinanceira());//108
                    sqlInserir.setString(++i, obj.getIdClienteMundiPag());//109
                    sqlInserir.setBoolean(++i, obj.getConfirmarMatricPendFinanCasoNaoControleMatricula());//110
                    if(Uteis.isAtributoPreenchido(obj.getContaCorrentePadraoDevolucaoCheque())){//111
                    	sqlInserir.setInt(++i, obj.getContaCorrentePadraoDevolucaoCheque());
                    }else{
                    	sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getAmbienteCartaoCreditoEnum().getName());//112
                    sqlInserir.setBoolean(++i, obj.getImprimirBoletoComImagemLinhaDigitavel());//112
                    sqlInserir.setString(++i, obj.getChaveContaMundipagg());//114
                    sqlInserir.setBoolean(++i, obj.getBloquearCalouroPagarMatriculaVisaoAluno());//115
                    sqlInserir.setBoolean(++i, obj.getBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente());//116
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaMatricula());//117
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaMensalidade());//118
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaBiblioteca());//119
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaOutras());//120
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaContratoReceita());//121
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaDevolucaoCheque());//122
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaNegociacao());//123
                    sqlInserir.setBoolean(++i, obj.getPermiteNegociarParcelaInclusaoReposicao());//124
                    if(obj.getModeloBoletoBiblioteca().getCodigo() != null && obj.getModeloBoletoBiblioteca().getCodigo() >0){
                    	sqlInserir.setInt(++i, obj.getModeloBoletoBiblioteca().getCodigo());
                    }else{
                    	sqlInserir.setNull(++i, 0);
                    }
					sqlInserir.setBoolean(++i, obj.isApresentarFormaRecebimentoContaReceberVisaoAluno());//125
					sqlInserir.setBoolean(++i, obj.getCriarContaReceberPendenciaArquivoRetornoAutomaticamente());//126
					if(obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo() != null && obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo() >0){
	                   	sqlInserir.setInt(++i, obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo());
	                }else{
	                   	sqlInserir.setNull(++i, 0);
	                }          
					if(obj.getArquivoIreportMovFin().getCodigo() >0){
						sqlInserir.setInt(++i, obj.getArquivoIreportMovFin().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getAlterarDataVencimentoParcelaDiaUtil().booleanValue());
					sqlInserir.setString(++i, obj.getMerchantIdCielo());
					sqlInserir.setString(++i, obj.getMerchantKeyCielo());
					sqlInserir.setString(++i, obj.getObservacaoComprovanteRecebimento());					
					if(Uteis.isAtributoPreenchido(obj.getModeloBoletoMaterialDidatico())) {
						Uteis.setValuePreparedStatement(obj.getModeloBoletoMaterialDidatico(), ++i, sqlInserir);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlInserir);
					}
					if(Uteis.isAtributoPreenchido(obj.getCentroReceitaMaterialDidaticoPadrao())) {
						Uteis.setValuePreparedStatement(obj.getCentroReceitaMaterialDidaticoPadrao(), ++i, sqlInserir);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlInserir);
					}
					if(Uteis.isAtributoPreenchido(obj.getContaCorrentePadraoMaterialDidatico())) {
						Uteis.setValuePreparedStatement(obj.getContaCorrentePadraoMaterialDidatico(), ++i, sqlInserir);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlInserir);
					}
					
					Uteis.setValuePreparedStatement(obj.isPermiteNegociarParcelaMaterialDidatico(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorMinimoGerarPendenciaControleCobranca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorMaximoCompraDiretaRequisicao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidadeDiasEnviarAvisoDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIndiceReajustePadraoContasPorAtrasoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQtdeDiasExcluirNegociacaoContaReceberVencida(), ++i, sqlInserir);
					
					if(Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())) {
						Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlInserir);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlInserir);
					}

					if(Uteis.isAtributoPreenchido(obj.getBancoPadraoRemessa())) {
						Uteis.setValuePreparedStatement(obj.getBancoPadraoRemessa(), ++i, sqlInserir);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlInserir);
					}

					if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoPadrao())) {
						Uteis.setValuePreparedStatement(obj.getFormaPagamentoPadrao(), ++i, sqlInserir);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlInserir);
					}
					Uteis.setValuePreparedStatement(obj.getTokenRede(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPvRede(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOperadora(), ++i, sqlInserir);
					sqlInserir.setBoolean(++i, obj.getTipoOrigemMatriculaRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemBibliotecaRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemMensalidadeRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemDevolucaoChequeRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemNegociacaoRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemContratoReceitaRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemOutrosRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemMaterialDidaticoRotinaInadimplencia().booleanValue());
					sqlInserir.setBoolean(++i, obj.getTipoOrigemInclusaoReposicaoRotinaInadimplencia().booleanValue());

					Uteis.setValuePreparedStatement(obj.getNomeParcelaMatriculaApresentarAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNomeParcelaMaterialDidaticoApresentarAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSiglaParcelaMatriculaApresentarAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSiglaParcelaMaterialDidaticoApresentarAluno(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getBloquearEmissaoBoletoPagamentoVencidoVisaoAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIsBloquearDemaisParcelasVencidas(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidadeDiasAtrasos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoParcelaNegociar(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFiltroPadraoContaReceberVisaoAluno(), ++i, sqlInserir);
					
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
            persistirConfiguracaoFinanceiroCartaoVOs(obj, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }
    
    private void validarBloqueioEmissaoBoletoPagamentoVencidoVisaoAluno(ConfiguracaoFinanceiroVO obj) throws Exception {
   	 if (obj.getBloquearEmissaoBoletoPagamentoVencidoVisaoAluno() && !Uteis.isAtributoPreenchido(obj.getQuantidadeDiasAtrasos())) { 
        	throw new Exception("A quantidade de dias de atraso Permitido para Pagamento deve ser informado.");
        }
		
	}

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>ConfiguracaoFinanceiroVO</code>. Sempre utiliza a chave primária da
     * classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
     * Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação
     * <code>alterar</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>ConfiguracaoFinanceiroVO</code> que
     *            será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ConfiguracaoFinanceiroVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            ConfiguracaoFinanceiroVO.validarDados(obj);
            validarMascaraPadraoCategoriaDespesa(obj);
            validarMascaraPadraoCentroReceita(obj);
            validarMascaraPadraoPlanoConta(obj);
            validarSeMascaraDeCategoriaDespesaFoiAlterada(obj);
            validarMensagemNotificacaoMatriculaNaoPagaConsultor(obj);
            validarBloqueioEmissaoBoletoPagamentoVencidoVisaoAluno(obj);
            if(!obj.getArquivoIreportMovFin().getNome().equals("")){
				obj.getArquivoIreportMovFin().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoIreportMovFin(), false, usuario, configuracaoGeralSistemaVO);
			}
            final String sql = "UPDATE ConfiguracaoFinanceiro set percentualJuroPadrao=?, percentualMultaPadrao=?, tipoCalculoJuro=?, descontoProgressivoPadrao=?, " //1-4
                    + "diaVencimentoParcelasPadrao=?, diaVencimentoMatriculaPadrao=?, nrNiveisPlanoConta=?, mascaraPlanoConta=?, nrNiveisCategoriaDespesa=?, " //5-9
                    + "mascaraCategoriaDespesa=?, nrNiveisCentroReceita=?, mascaraCentroReceita=?, centroReceitaMensalidadePadrao=?, centroReceitaMatriculaPadrao=?, " //10-14
                    + "centroReceitaRequerimentoPadrao=?, centroReceitaBibliotecaPadrao=?, centroReceitaInscricaoProcessoSeletivoPadrao=?, contacorrentepadraobiblioteca=?, "//15-18
                    + "contaCorrentePadraoMatricula=?, contaCorrentePadraoMensalidade=?, contaCorrentePadraoRequerimento=?, contaCorrentePadraoProcessoSeletivo=?, " //19-22
                    + "contaCorrentePadraoNegociacao=?, centroReceitaNegociacaoPadrao =?,  formaPagamentoPadraoCheque=?, CategoriaDespesaPadraoAntecipacaoCheque=?, " //23-26
                    + "departamentoPadraoAntecipacaoCheque=?, formaPagamentoPadraoProvisaoCusto=?, mensagempadraonotificacao=?, assuntopadraonotificacao=?, configuracoes=?, " //27-31
                    + "modeloboletomatricula=?, modeloboletomensalidade=?, modeloboletoprocessoseletivo=?, modeloboletorequerimento=?, modeloboletooutros=?, " //32-36
                    + "formapagamentocontrolecobranca=?, contacorrentepadraocontrolecobranca=?, planoContaPagarPadraoJuro=?, planoContaPagarPadraoDesconto=?, " //37-40
                    + "planoContaPagarPadraoCredito=?, planoContaPagarPadraoDebito=?, planoContaReceberPadraoJuro=?, planoContaReceberPadraoDesconto=?, " //41-44
                    + "planoContaReceberPadraoCredito=?, planoContaReceberPadraoDebito=?, historicoContaPagarPadraoJuro=?, historicoContaPagarPadraoDesconto=?, " //45-48
                    + "historicoContaReceberPadraoJuro=?, historicoContaReceberPadraoDesconto=?, ordemConvenio=?, ordemConvenioValorCheio=?, ordemDescontoAluno=?, " //49-53
                    + "ordemDescontoAlunoValorCheio=?, ordemDescontoProgressivo=?, ordemDescontoProgressivoValorCheio=?, ordemPlanoDesconto=?, ordemPlanoDescontoValorCheio=?, " //54-58
                    + "qtdeParcelasNegativacaoSerasa=?, qtdeMinimaDiasAntesNegativacaoSerasa=?, usaChancela=?, cobrarReimpressaoBoletos=?, modeloBoletoReimpressao=?, " //59-63
                    + "contaCorrenteReimpressaoBoletos=?, centroReceitaReimpressaoBoletos=?, valorCobrarReimpressaoBoletos=?, usaDescontoCompostoPlanoDesconto=?, " //64-67
                    + "usaPlanoOrcamentario=?, gerarBoletoComDescontoSemValidade=?, vencimentoDescontoProgressivoDiaUtil=?, obrigatorioSelecionarUnidadeEnsinoControleCobranca=?, utilizaPlanoFinanceiroReposicao=?, utilizaPlanoFinanceiroInclusao=?, "//73
                    + "imprimirBoletoComLogoBanco=?, usarContaCorrenteTurmaIncluida=?, recomendacaoRenegociacaoVisaoAluno=?, numeroDiasNotificarVencimentoContaReceber=?, enviarNotificacaoConsultorMatricula=?, numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor=?, vencimentoParcelaDiaUtil=?, ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula=?, textoPadraoCartaCobranca=?,  "//82
                    + " quantidadeDiasEnviarMensagemCobrancaInadimplente = ?, periodicidadeDiasEnviarMensagemCobrancaInadimplente = ?, grupoDestinatarioMensagemCobrancaInadimplente = ?, modeloBoletoRenegociacao=?, permitirGerarParcelaPreMatricula=?,  emailEnviarNotificacaoConsultorMatricula=?, numeroDiasBloquearAcessoAlunoInadimplente=?, "
                    + " tipoEnvioInadimplencia=?, quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente=?, quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente=?, quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente=?, quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno=?, naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor=?, naoApresAlunoInadimplenteDiarioEspelhoNota=?, qtdDiasAntesVencimentoApresentarContaReceberMatVisaoAluno=?, centroReceitaReposicaoPadrao=?,quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente=?, "
                    + " centroReceitaParcelaAvulsaControleCobranca=?, cancelarContaReceberCandidatoInadimplenteAposDataProva=?, qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente=?,categoriaDespesaOperadoraCartao=?,excluirNegociacaoRecebimentoVencida=?, permitiVisualizarContaReceberVisaoAlunoPreMatricula=?, cobrarJuroMultaSobreValorCheioConta=?, realizarMatriculaComFinanceiroManualAtivo=?, utilizarIntegracaoFinanceira=?, "
                    + " idClienteMundiPag=?, confirmarMatricPendFinanCasoNaoControleMatricula=?, contaCorrentePadraoDevolucaoCheque = ?, ambienteCartaoCredito=?, imprimirBoletoComImagemLinhaDigitavel=?, chaveContaMundipagg=?, bloquearCalouroPagarMatriculaVisaoAluno=?, bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente=?, "
                    + " permiteNegociarParcelaMatricula=?, permiteNegociarParcelaMensalidade=?, permiteNegociarParcelaBiblioteca=?, permiteNegociarParcelaOutras=?," //82
                    + " permiteNegociarParcelaContratoReceita=?, permiteNegociarParcelaDevolucaoCheque=?, permiteNegociarParcelaNegociacao=?, permiteNegociarParcelaInclusaoReposicao=?, modeloBoletoBiblioteca=? , " //86                                                            
                    + " apresentarFormaRecebimentoContaReceberVisaoAluno = ?, criarContaReceberPendenciaArquivoRetornoAutomaticamente=?, categoriaDespesaPadraoRestituicaoAluno = ? , arquivoIreportMovFin=?, alterarDataVencimentoParcelaDiaUtil=?, merchantIdCielo=?, merchantKeyCielo=?, observacaoComprovanteRecebimento = ? , modeloboletomaterialdidatico=?, " //90
                    + " centroReceitaMaterialDidaticoPadrao = ?, contaCorrentePadraoMaterialDidatico = ?, permiteNegociarParcelaMaterialDidatico=?, valorMinimoGerarPendenciaControleCobranca=?, valorMaximoCompraDiretaRequisicao=?, quantidadeDiasEnviarAvisoDesconto=?, " //
                    + " indiceReajustePadraoContasPorAtraso = ?, qtdDiasAplicarIndireReajustePorAtrasoContaReceber = ?,  qtdeDiasExcluirNegociacaoContaReceberVencida = ?, categoriaDespesa =?, bancoPadraoRemessa=?, formaPagamentoPadrao=?, tokenRede=?, pvRede=?, operadora=?, "
                    + "tipoOrigemMatriculaRotinaInadimplencia=?, tipoOrigemBibliotecaRotinaInadimplencia=?, tipoOrigemMensalidadeRotinaInadimplencia=?, tipoOrigemDevolucaoChequeRotinaInadimplencia=?, "
                    + "tipoOrigemNegociacaoRotinaInadimplencia=?, tipoOrigemContratoReceitaRotinaInadimplencia=?, tipoOrigemOutrosRotinaInadimplencia=?, tipoOrigemMaterialDidaticoRotinaInadimplencia=?, "
                    + "tipoOrigemInclusaoReposicaoRotinaInadimplencia=?, nomeParcelaMatriculaApresentarAluno=?, nomeParcelaMaterialDidaticoApresentarAluno=?, "
                    + " siglaParcelaMatriculaApresentarAluno=?, siglaParcelaMaterialDidaticoApresentarAluno=?, bloquearEmissaoBoletoPagamentoVencidoVisaoAluno=?, bloquearDemaisParcelasVencidas=?, quantidadeDiasAtrasos=?, bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco = ?, tipoparcelanegociar = ?, filtroPadraoContaReceberVisaoAluno = ? "
                    + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlAlterar.setDouble(++i, obj.getPercentualJuroPadrao().doubleValue());
                    sqlAlterar.setDouble(++i, obj.getPercentualMultaPadrao().doubleValue());
                    sqlAlterar.setString(++i, obj.getTipoCalculoJuro());
                    if (obj.getDescontoProgressivoPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getDescontoProgressivoPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setInt(++i, obj.getDiaVencimentoParcelasPadrao().intValue());
                    sqlAlterar.setInt(++i, obj.getDiaVencimentoMatriculaPadrao().intValue());
                    sqlAlterar.setInt(++i, obj.getNrNiveisPlanoConta().intValue());
                    sqlAlterar.setString(++i, obj.getMascaraPlanoConta());
                    sqlAlterar.setInt(++i, obj.getNrNiveisCategoriaDespesa().intValue());
                    sqlAlterar.setString(++i, obj.getMascaraCategoriaDespesa());                                                        //10
                    sqlAlterar.setInt(++i, obj.getNrNiveisCentroReceita().intValue());
                    sqlAlterar.setString(++i, obj.getMascaraCentroReceita());
                    if (obj.getCentroReceitaMensalidadePadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaMensalidadePadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaMatriculaPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaMatriculaPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaBibliotecaPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaBibliotecaPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoBiblioteca().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoBiblioteca().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoMatricula().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoMatricula().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoMensalidade().intValue() != 0) {                                  //20
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoMensalidade().intValue());                 //20
                    } else {                                                                                        //20
                        sqlAlterar.setNull(++i, 0);                                                                 //20
                    }                                                                                               //20
                    if (obj.getContaCorrentePadraoRequerimento().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoRequerimento().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoProcessoSeletivo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoProcessoSeletivo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoNegociacao().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoNegociacao().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaNegociacaoPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaNegociacaoPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getFormaPagamentoPadraoCheque().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getFormaPagamentoPadraoCheque().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getDepartamentoPadraoAntecipacaoCheque().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getDepartamentoPadraoAntecipacaoCheque().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getFormaPagamentoPadraoProvisaoCusto().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getFormaPagamentoPadraoProvisaoCusto().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setString(++i, obj.getMensagemPadraoNotificacao());
                    sqlAlterar.setString(++i, obj.getAssuntoPadraoNotificacao());                                       //30
                    if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getConfiguracoesVO().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoMatricula().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getModeloBoletoMatricula().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoMensalidade().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getModeloBoletoMensalidade().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoProcessoSeletivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getModeloBoletoProcessoSeletivo().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoRequerimento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getModeloBoletoRequerimento().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getModeloBoletoOutros().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getModeloBoletoOutros().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getFormaPagamentoPadraoControleCobranca().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getFormaPagamentoPadraoControleCobranca().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrentePadraoControleCobranca().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrentePadraoControleCobranca().getCodigo());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaPagarPadraoJuro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaPagarPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaPagarPadraoDesconto().getCodigo().intValue() != 0) {                           //40
                        sqlAlterar.setInt(++i, obj.getPlanoContaPagarPadraoDesconto().getCodigo().intValue());          //40
                    } else {                                                                                            //40
                        sqlAlterar.setNull(++i, 0);                                                                     //40
                    }                                                                                                   //40
                    if (obj.getPlanoContaPagarPadraoCredito().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaPagarPadraoCredito().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaPagarPadraoDebito().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaPagarPadraoDebito().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoJuro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaReceberPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoDesconto().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaReceberPadraoDesconto().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoCredito().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaReceberPadraoCredito().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getPlanoContaReceberPadraoDebito().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getPlanoContaReceberPadraoDebito().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaPagarPadraoJuro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getHistoricoContaPagarPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaPagarPadraoDesconto().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getHistoricoContaPagarPadraoDesconto().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaReceberPadraoJuro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getHistoricoContaReceberPadraoJuro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getHistoricoContaReceberPadraoDesconto().getCodigo().intValue() != 0) {                         //50
                        sqlAlterar.setInt(++i, obj.getHistoricoContaReceberPadraoDesconto().getCodigo().intValue());        //50
                    } else {                                                                                                //50
                        sqlAlterar.setNull(++i, 0);                                                                         //50
                    }                                                                                                       //50
                    sqlAlterar.setInt(++i, obj.getOrdemConvenio());
                    sqlAlterar.setBoolean(++i, obj.getOrdemConvenioValorCheio());
                    sqlAlterar.setInt(++i, obj.getOrdemDescontoAluno());
                    sqlAlterar.setBoolean(++i, obj.getOrdemDescontoAlunoValorCheio());
                    sqlAlterar.setInt(++i, obj.getOrdemDescontoProgressivo());
                    sqlAlterar.setBoolean(++i, obj.getOrdemDescontoProgressivoValorCheio());
                    sqlAlterar.setInt(++i, obj.getOrdemPlanoDesconto());
                    sqlAlterar.setBoolean(++i, obj.getOrdemPlanoDescontoValorCheio());
                    sqlAlterar.setInt(++i, obj.getQtdeParcelasNegativacaoSerasa());
                    sqlAlterar.setInt(++i, obj.getQtdeMinimaDiasAntesNegativacaoSerasa());                                  //60
                    sqlAlterar.setBoolean(++i, obj.getUsaChancela());

                    sqlAlterar.setBoolean(++i, obj.getCobrarReimpressaoBoletos());
                    if (obj.getModeloBoletoReimpressao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getModeloBoletoReimpressao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getContaCorrenteReimpressaoBoletos().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getContaCorrenteReimpressaoBoletos().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCentroReceitaReimpressaoBoletos().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaReimpressaoBoletos().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setDouble(++i, obj.getValorCobrarReimpressaoBoletos());
                    sqlAlterar.setBoolean(++i, obj.getUsaDescontoCompostoPlanoDesconto());
                    sqlAlterar.setBoolean(++i, obj.getUsaPlanoOrcamentario());
                    sqlAlterar.setBoolean(++i, obj.getGerarBoletoComDescontoSemValidade());
                    sqlAlterar.setBoolean(++i, obj.getVencimentoDescontoProgressivoDiaUtil());                                     //70
                    sqlAlterar.setBoolean(++i, obj.getObrigatorioSelecionarUnidadeEnsinoControleCobranca());
                    sqlAlterar.setBoolean(++i, obj.getUtilizaPlanoFinanceiroReposicao());
                    sqlAlterar.setBoolean(++i, obj.getUtilizaPlanoFinanceiroInclusao());
                    sqlAlterar.setBoolean(++i, obj.getImprimirBoletoComLogoBanco());
                    sqlAlterar.setBoolean(++i, obj.getUsarContaCorrenteTurmaIncluida());
                    sqlAlterar.setString(++i, obj.getRecomendacaoRenegociacaoVisaoAluno());
                    sqlAlterar.setInt(++i, obj.getNumeroDiasNotificarVencimentoContaReceber());
                    sqlAlterar.setBoolean(++i, obj.getEnviarNotificacaoConsultorMatricula()); 
                    sqlAlterar.setInt(++i, obj.getNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor());
                    sqlAlterar.setBoolean(++i, obj.getVencimentoParcelaDiaUtil());                                                  
                    sqlAlterar.setBoolean(++i, obj.getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula());               //81
                    sqlAlterar.setString(++i, obj.getTextoPadraoCartaCobranca());
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasEnviarMensagemCobrancaInadimplente());
                    sqlAlterar.setInt(++i, obj.getPeriodicidadeDiasEnviarMensagemCobrancaInadimplente());
                    if(obj.getGrupoDestinatarioMensagemCobrancaInadimplente().getCodigo() != null && obj.getGrupoDestinatarioMensagemCobrancaInadimplente().getCodigo() >0){
                        sqlAlterar.setInt(++i, obj.getGrupoDestinatarioMensagemCobrancaInadimplente().getCodigo());
                    }else{
                        sqlAlterar.setNull(++i, 0);
                    }
                    if(obj.getModeloBoletoRenegociacao().getCodigo() != null && obj.getModeloBoletoRenegociacao().getCodigo() >0){
                    	sqlAlterar.setInt(++i, obj.getModeloBoletoRenegociacao().getCodigo());
                    }else{
                    	sqlAlterar.setNull(++i, 0);
                    }                    
                    sqlAlterar.setBoolean(++i, obj.getPermitirGerarParcelaPreMatricula());
                    sqlAlterar.setString(++i, obj.getEmailEnviarNotificacaoConsultorMatricula());
                    sqlAlterar.setInt(++i, obj.getNumeroDiasBloquearAcessoAlunoInadimplente());
                    sqlAlterar.setString(++i, obj.getTipoEnvioInadimplencia());
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente());
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente());
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente());
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno());
                    sqlAlterar.setBoolean(++i, obj.getNaoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor());
                    sqlAlterar.setBoolean(++i, obj.getNaoApresAlunoInadimplenteDiarioEspelhoNota());
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno());
                    if (obj.getCentroReceitaReposicaoPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaReposicaoPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setInt(++i, obj.getQuantidadeDiasEnviarQuartaMensagemCobrancaInadimplente());
                    if (obj.getCentroReceitaParcelaAvulsaControleCobranca().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCentroReceitaParcelaAvulsaControleCobranca().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setBoolean(++i, obj.getCancelarContaReceberCandidatoInadimplenteAposDataProva());
                    sqlAlterar.setInt(++i, obj.getQtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente());
                    if (obj.getCategoriaDespesaOperadoraCartao().getCodigo().intValue() != 0) {
                    	sqlAlterar.setInt(++i, obj.getCategoriaDespesaOperadoraCartao().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setBoolean(++i, obj.getExcluirNegociacaoRecebimentoVencida());
                    sqlAlterar.setBoolean(++i, obj.getPermitiVisualizarContaReceberVisaoAlunoPreMatricula());
                    sqlAlterar.setBoolean(++i, obj.getCobrarJuroMultaSobreValorCheioConta());
                    sqlAlterar.setBoolean(++i, obj.getRealizarMatriculaComFinanceiroManualAtivo());
                    sqlAlterar.setBoolean(++i, obj.getUtilizarIntegracaoFinanceira());
                    sqlAlterar.setString(++i, obj.getIdClienteMundiPag());
                    sqlAlterar.setBoolean(++i, obj.getConfirmarMatricPendFinanCasoNaoControleMatricula());
                    if(Uteis.isAtributoPreenchido(obj.getContaCorrentePadraoDevolucaoCheque())){
                    	sqlAlterar.setInt(++i, obj.getContaCorrentePadraoDevolucaoCheque());
                    }else{
                    	sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setString(++i, obj.getAmbienteCartaoCreditoEnum().getName());
                    sqlAlterar.setBoolean(++i, obj.getImprimirBoletoComImagemLinhaDigitavel());
                    sqlAlterar.setString(++i, obj.getChaveContaMundipagg());
                    sqlAlterar.setBoolean(++i, obj.getBloquearCalouroPagarMatriculaVisaoAluno());
                    sqlAlterar.setBoolean(++i, obj.getBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaMatricula());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaMensalidade());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaBiblioteca());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaOutras());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaContratoReceita());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaDevolucaoCheque());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaNegociacao());
                    sqlAlterar.setBoolean(++i, obj.getPermiteNegociarParcelaInclusaoReposicao());                    
                    if(obj.getModeloBoletoBiblioteca().getCodigo() != null && obj.getModeloBoletoBiblioteca().getCodigo() >0){
                    	sqlAlterar.setInt(++i, obj.getModeloBoletoBiblioteca().getCodigo());
                    }else{
                    	sqlAlterar.setNull(++i, 0);
                    }                    
                    sqlAlterar.setBoolean(++i, obj.isApresentarFormaRecebimentoContaReceberVisaoAluno());
                    sqlAlterar.setBoolean(++i, obj.getCriarContaReceberPendenciaArquivoRetornoAutomaticamente());
                    if(obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo() != null && obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo() >0){
                    	sqlAlterar.setInt(++i, obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo());
                    }else{
                    	sqlAlterar.setNull(++i, 0);
                    }
                    if(obj.getArquivoIreportMovFin().getCodigo().intValue() > 0){
                    	sqlAlterar.setInt(++i, obj.getArquivoIreportMovFin().getCodigo());
                    }else{
                    	sqlAlterar.setNull(++i, 0);
                    }
					if (!obj.getVencimentoParcelaDiaUtil().booleanValue()) {
                    	obj.setAlterarDataVencimentoParcelaDiaUtil(Boolean.FALSE);
                    }					
                    sqlAlterar.setBoolean(++i, obj.getAlterarDataVencimentoParcelaDiaUtil());
                    sqlAlterar.setString(++i, obj.getMerchantIdCielo());
                    sqlAlterar.setString(++i, obj.getMerchantKeyCielo()); //90
                    sqlAlterar.setString(++i, obj.getObservacaoComprovanteRecebimento());
                    if(Uteis.isAtributoPreenchido(obj.getModeloBoletoMaterialDidatico())) {
						Uteis.setValuePreparedStatement(obj.getModeloBoletoMaterialDidatico(), ++i, sqlAlterar);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlAlterar);
					}                    
                    if(Uteis.isAtributoPreenchido(obj.getCentroReceitaMaterialDidaticoPadrao())) {
						Uteis.setValuePreparedStatement(obj.getCentroReceitaMaterialDidaticoPadrao(), ++i, sqlAlterar);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlAlterar);
					}                    
					if(Uteis.isAtributoPreenchido(obj.getContaCorrentePadraoMaterialDidatico())) {
						Uteis.setValuePreparedStatement(obj.getContaCorrentePadraoMaterialDidatico(), ++i, sqlAlterar);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlAlterar);
					}
					Uteis.setValuePreparedStatement(obj.isPermiteNegociarParcelaMaterialDidatico(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorMinimoGerarPendenciaControleCobranca(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorMaximoCompraDiretaRequisicao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQuantidadeDiasEnviarAvisoDesconto(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIndiceReajustePadraoContasPorAtrasoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQtdeDiasExcluirNegociacaoContaReceberVencida(), ++i, sqlAlterar);
					if(Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())) {
						Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlAlterar);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlAlterar);
					}

					if(Uteis.isAtributoPreenchido(obj.getBancoPadraoRemessa())) {
						Uteis.setValuePreparedStatement(obj.getBancoPadraoRemessa(), ++i, sqlAlterar);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlAlterar);
					}

					if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoPadrao())) {
						Uteis.setValuePreparedStatement(obj.getFormaPagamentoPadrao(), ++i, sqlAlterar);
					} else {
						Uteis.setValuePreparedStatement(null, ++i, sqlAlterar);
					}
					Uteis.setValuePreparedStatement(obj.getTokenRede(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPvRede(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getOperadora(), ++i, sqlAlterar);
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemMatriculaRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemBibliotecaRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemMensalidadeRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemDevolucaoChequeRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemNegociacaoRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemContratoReceitaRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemOutrosRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemMaterialDidaticoRotinaInadimplencia().booleanValue());
					sqlAlterar.setBoolean(++i, obj.getTipoOrigemInclusaoReposicaoRotinaInadimplencia().booleanValue());
					Uteis.setValuePreparedStatement(obj.getNomeParcelaMatriculaApresentarAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNomeParcelaMaterialDidaticoApresentarAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSiglaParcelaMatriculaApresentarAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSiglaParcelaMaterialDidaticoApresentarAluno(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getBloquearEmissaoBoletoPagamentoVencidoVisaoAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIsBloquearDemaisParcelasVencidas(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQuantidadeDiasAtrasos(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getBloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoParcelaNegociar(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFiltroPadraoContaReceberVisaoAluno(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);				
                    return sqlAlterar;
                }
            });
            persistirConfiguracaoFinanceiroCartaoVOs(obj, usuario);

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistirConfiguracaoFinanceiroCartaoVOs(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        try {
            getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().excluirPelaConfiguracaoFinanceiro(configuracaoFinanceiroVO.getCodigo(), configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO());
            for (ConfiguracaoFinanceiroCartaoVO obj : configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO()) {
                if (obj.getConfiguracaoFinanceiroVO().getCodigo() == null || obj.getConfiguracaoFinanceiroVO().getCodigo().equals(0)) {
                    obj.getConfiguracaoFinanceiroVO().setCodigo(configuracaoFinanceiroVO.getCodigo());
                }
                getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().persistir(obj);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>ConfiguracaoFinanceiroVO</code>. Sempre localiza o registro a ser
     * excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>ConfiguracaoFinanceiroVO</code> que
     *            será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ConfiguracaoFinanceiroVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM ConfiguracaoFinanceiro WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getAplicacaoControle().removerConfiguracaoFinanceiraEmNivelAplicacao(obj);
        } catch (Exception e) {
            throw e;
        }
    } 

    public ConfiguracaoFinanceiroVO consultarConfiguracaoASerUsada(int nivelMontarDados, Integer unidadeEnsinoMatricula, UsuarioVO usuario) throws Exception {
    	return consultarConfiguracaoASerUsada(nivelMontarDados, usuario, unidadeEnsinoMatricula);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ConfiguracaoFinanceiroVO consultarConfiguracaoASerUsada(int nivelMontarDados, UsuarioVO usuario, Integer codigoUnidadeEnsino) throws Exception {
    	if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino) && (Uteis.isAtributoPreenchido(usuario) && Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado()))) {
    		codigoUnidadeEnsino = usuario.getUnidadeEnsinoLogado().getCodigo();
    	}
        return getAplicacaoControle().getConfiguracaoFinanceiroVO(codigoUnidadeEnsino);    	
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ConfiguracaoFinanceiroVO consultarConfiguracaoNivelAplicacaoSerUsada(Integer codigoUnidadeEnsino) throws Exception {
    	StringBuilder sql  =  new StringBuilder("select ConfiguracaoFinanceiro.codigo from ConfiguracaoFinanceiro ");
    	if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
    		sql.append(" inner join configuracoes on configuracoes.codigo = ConfiguracaoFinanceiro.configuracoes ");
    		sql.append(" inner join unidadeensino on configuracoes.codigo = unidadeensino.configuracoes ");
    		sql.append(" where unidadeensino.codigo = ").append(codigoUnidadeEnsino);
    	}else {
    		sql.append(" inner join configuracoes on configuracoes.codigo = ConfiguracaoFinanceiro.configuracoes ");
    		sql.append(" where configuracoes.padrao ");    		
    	}
    	SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	if(dadosSQL.next()) {    		
    		return getAplicacaoControle().getConfiguracaoFinanceiroPorCodigoVO(dadosSQL.getInt("codigo"));    		
    	}else if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
    		return consultarConfiguracaoNivelAplicacaoSerUsada(null);
    	}
    	if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
    		throw new Exception("Não foi possível carregar uma CONFIGURAÇÃO FINANCEIRA para a unidade de ensino de código "+codigoUnidadeEnsino+". ");
    	}else {
    		throw new Exception("Não foi possível carregar uma CONFIGURAÇÃO FINANCEIRA padrão do sistema. ");
    	}
    }
    /**
     * Responsável por realizar uma consulta de
     * <code>ConfiguracaoFinanceiro</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>ConfiguracaoFinanceiroVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM ConfiguracaoFinanceiro WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public ConfiguracaoFinanceiroVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM configuracaofinanceiro where configuracaofinanceiro.configuracoes = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoConfiguracoes});
        if (resultado.next()) {
            return montarDados(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ConfiguracaoFinanceiroVO();
    }    

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe
     *         <code>ConfiguracaoFinanceiroVO</code> resultantes da consulta.
     */
    public static List<ConfiguracaoFinanceiroVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ConfiguracaoFinanceiroVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>ConfiguracaoFinanceiroVO</code>.
     * 
     * @return O objeto da classe <code>ConfiguracaoFinanceiroVO</code> com os
     *         dados devidamente montados.
     */
    @SuppressWarnings("unchecked")
	public static ConfiguracaoFinanceiroVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        ConfiguracaoFinanceiroVO obj = new ConfiguracaoFinanceiroVO();
        try {
            obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
                return obj;
            }
            obj.setPercentualJuroPadrao(new Double(dadosSQL.getDouble("percentualJuroPadrao")));
            obj.setPercentualMultaPadrao(new Double(dadosSQL.getDouble("percentualMultaPadrao")));
            obj.setTipoCalculoJuro(dadosSQL.getString("tipoCalculoJuro"));
            obj.getDescontoProgressivoPadrao().setCodigo(new Integer(dadosSQL.getInt("descontoProgressivoPadrao")));
            obj.setDiaVencimentoParcelasPadrao(new Integer(dadosSQL.getInt("diaVencimentoParcelasPadrao")));
            obj.setAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula(dadosSQL.getBoolean("ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula"));
            obj.setConfirmarMatricPendFinanCasoNaoControleMatricula(dadosSQL.getBoolean("confirmarMatricPendFinanCasoNaoControleMatricula"));
            obj.setDiaVencimentoMatriculaPadrao(new Integer(dadosSQL.getInt("diaVencimentoMatriculaPadrao")));
            obj.setNrNiveisPlanoConta(new Integer(dadosSQL.getInt("nrNiveisPlanoConta")));
            obj.setMascaraPlanoConta(dadosSQL.getString("mascaraPlanoConta"));
            obj.setNrNiveisCategoriaDespesa(new Integer(dadosSQL.getInt("nrNiveisCategoriaDespesa")));
            obj.setMascaraCategoriaDespesa(dadosSQL.getString("mascaraCategoriaDespesa"));
            obj.setNrNiveisCentroReceita(new Integer(dadosSQL.getInt("nrNiveisCentroReceita")));
            obj.setMascaraCentroReceita(dadosSQL.getString("mascaraCentroReceita"));
            obj.getCentroReceitaBibliotecaPadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaBibliotecaPadrao")));
            obj.getCentroReceitaMensalidadePadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaMensalidadePadrao")));
            obj.getCentroReceitaMaterialDidaticoPadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaMaterialDidaticoPadrao")));
            obj.getCentroReceitaMatriculaPadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaMatriculaPadrao")));
            obj.getCentroReceitaRequerimentoPadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaRequerimentoPadrao")));
            obj.getCentroReceitaReposicaoPadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaReposicaoPadrao")));
            obj.getCentroReceitaParcelaAvulsaControleCobranca().setCodigo(new Integer(dadosSQL.getInt("centroReceitaParcelaAvulsaControleCobranca")));
            obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().setCodigo(new Integer(dadosSQL.getInt("CentroReceitaInscricaoProcessoSeletivoPadrao")));
            obj.setContaCorrentePadraoBiblioteca(new Integer(dadosSQL.getInt("contaCorrentePadraoBiblioteca")));
            obj.setContaCorrentePadraoMatricula(new Integer(dadosSQL.getInt("contaCorrentePadraoMatricula")));
            obj.setContaCorrentePadraoMensalidade(new Integer(dadosSQL.getInt("contaCorrentePadraoMensalidade")));
            obj.setContaCorrentePadraoMaterialDidatico(new Integer(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico")));
            obj.setContaCorrentePadraoRequerimento(new Integer(dadosSQL.getInt("contaCorrentePadraoRequerimento")));
            obj.setContaCorrentePadraoProcessoSeletivo(new Integer(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo")));
            obj.setContaCorrentePadraoNegociacao(new Integer(dadosSQL.getInt("contaCorrentePadraoNegociacao")));
            obj.setContaCorrentePadraoDevolucaoCheque(new Integer(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque")));
            obj.getCentroReceitaNegociacaoPadrao().setCodigo(new Integer(dadosSQL.getInt("centroReceitaNegociacaoPadrao")));
            obj.getFormaPagamentoPadraoCheque().setCodigo(new Integer(dadosSQL.getInt("formaPagamentoPadraoCheque")));
            obj.getFormaPagamentoPadraoProvisaoCusto().setCodigo(new Integer(dadosSQL.getInt("formaPagamentoPadraoProvisaoCusto")));
            obj.getCategoriaDespesaPadraoAntecipacaoCheque().setCodigo(new Integer(dadosSQL.getInt("categoriaDespesaPadraoAntecipacaoCheque")));
            obj.getCategoriaDespesaOperadoraCartao().setCodigo(dadosSQL.getInt("categoriaDespesaOperadoraCartao"));
            obj.getDepartamentoPadraoAntecipacaoCheque().setCodigo(new Integer(dadosSQL.getInt("departamentoPadraoAntecipacaoCheque")));
            obj.setMensagemPadraoNotificacao(dadosSQL.getString("mensagempadraonotificacao"));
            obj.setTextoPadraoCartaCobranca(dadosSQL.getString("textoPadraoCartaCobranca"));
            obj.setAssuntoPadraoNotificacao(dadosSQL.getString("assuntopadraonotificacao"));
            obj.getConfiguracoesVO().setCodigo(dadosSQL.getInt("configuracoes"));
            obj.getModeloBoletoMatricula().setCodigo(dadosSQL.getInt("modeloboletomatricula"));
            obj.getModeloBoletoMensalidade().setCodigo(dadosSQL.getInt("modeloboletomensalidade"));
            obj.getModeloBoletoMaterialDidatico().setCodigo(dadosSQL.getInt("modeloBoletoMaterialDidatico"));
            obj.getModeloBoletoProcessoSeletivo().setCodigo(dadosSQL.getInt("modeloboletoprocessoseletivo"));
            obj.getModeloBoletoRequerimento().setCodigo(dadosSQL.getInt("modeloboletorequerimento"));
            obj.getModeloBoletoBiblioteca().setCodigo(dadosSQL.getInt("modeloboletoBiblioteca"));           
			obj.getModeloBoletoRenegociacao().setCodigo(dadosSQL.getInt("modeloboletoRenegociacao"));
            obj.setPermitirGerarParcelaPreMatricula(dadosSQL.getBoolean("permitirGerarParcelaPreMatricula"));
            obj.getModeloBoletoOutros().setCodigo(dadosSQL.getInt("modeloboletooutros"));
            obj.getFormaPagamentoPadraoControleCobranca().setCodigo(dadosSQL.getInt("formapagamentocontrolecobranca"));
            obj.getContaCorrentePadraoControleCobranca().setCodigo(dadosSQL.getInt("contacorrentepadraocontrolecobranca"));
            obj.getPlanoContaPagarPadraoJuro().setCodigo(dadosSQL.getInt("planoContaPagarPadraoJuro"));
            obj.getPlanoContaPagarPadraoDesconto().setCodigo(dadosSQL.getInt("planoContaPagarPadraoDesconto"));
            obj.getPlanoContaPagarPadraoCredito().setCodigo(dadosSQL.getInt("planoContaPagarPadraoCredito"));
            obj.getPlanoContaPagarPadraoDebito().setCodigo(dadosSQL.getInt("planoContaPagarPadraoDebito"));
            obj.getPlanoContaReceberPadraoJuro().setCodigo(dadosSQL.getInt("planoContaReceberPadraoJuro"));
            obj.getPlanoContaReceberPadraoDesconto().setCodigo(dadosSQL.getInt("planoContaReceberPadraoDesconto"));
            obj.getPlanoContaReceberPadraoCredito().setCodigo(dadosSQL.getInt("planoContaReceberPadraoCredito"));
            obj.getPlanoContaReceberPadraoDebito().setCodigo(dadosSQL.getInt("planoContaReceberPadraoDebito"));
            obj.getHistoricoContaPagarPadraoJuro().setCodigo(dadosSQL.getInt("historicoContaPagarPadraoJuro"));
            obj.getHistoricoContaPagarPadraoDesconto().setCodigo(dadosSQL.getInt("historicoContaPagarPadraoDesconto"));
            
            obj.setBloquearEmissaoBoletoPagamentoVencidoVisaoAluno(dadosSQL.getBoolean("bloquearEmissaoBoletoPagamentoVencidoVisaoAluno"));
            obj.setBloquearDemaisParcelasVencidas(dadosSQL.getBoolean("bloquearDemaisParcelasVencidas"));
            obj.setQuantidadeDiasAtrasos(dadosSQL.getInt("quantidadeDiasAtrasos"));
            obj.setBloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco(dadosSQL.getBoolean("bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco"));
            
            obj.getHistoricoContaReceberPadraoJuro().setCodigo(dadosSQL.getInt("historicoContaReceberPadraoJuro"));
            obj.getHistoricoContaReceberPadraoDesconto().setCodigo(dadosSQL.getInt("historicoContaReceberPadraoDesconto"));
            obj.setUsaChancela(dadosSQL.getBoolean("usaChancela"));
            obj.setUsaPlanoOrcamentario(dadosSQL.getBoolean("usaPlanoOrcamentario"));
            obj.setGerarBoletoComDescontoSemValidade(dadosSQL.getBoolean("gerarBoletoComDescontoSemValidade"));
            obj.setUsaDescontoCompostoPlanoDesconto(dadosSQL.getBoolean("usaDescontoCompostoPlanoDesconto"));
            obj.setOrdemConvenio(dadosSQL.getInt("ordemConvenio"));
            obj.setOrdemConvenioValorCheio(dadosSQL.getBoolean("OrdemConvenioValorCheio"));
            obj.setOrdemDescontoAluno(dadosSQL.getInt("OrdemDescontoAluno"));
            obj.setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("OrdemDescontoAlunoValorCheio"));
            obj.setOrdemDescontoProgressivo(dadosSQL.getInt("OrdemDescontoProgressivo"));
            obj.setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("OrdemDescontoProgressivoValorCheio"));
            obj.setOrdemPlanoDesconto(dadosSQL.getInt("OrdemPlanoDesconto"));
            obj.setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("OrdemPlanoDescontoValorCheio"));
            obj.setQtdeParcelasNegativacaoSerasa(dadosSQL.getInt("qtdeParcelasNegativacaoSerasa"));
            obj.setQtdeMinimaDiasAntesNegativacaoSerasa(dadosSQL.getInt("qtdeMinimaDiasAntesNegativacaoSerasa"));
            obj.setCobrarReimpressaoBoletos(dadosSQL.getBoolean("cobrarReimpressaoBoletos"));
            obj.getModeloBoletoReimpressao().setCodigo(dadosSQL.getInt("modeloBoletoReimpressao"));
            obj.getContaCorrenteReimpressaoBoletos().setCodigo(dadosSQL.getInt("contaCorrenteReimpressaoBoletos"));
            obj.getCentroReceitaReimpressaoBoletos().setCodigo(dadosSQL.getInt("centroReceitaReimpressaoBoletos"));
            obj.setValorCobrarReimpressaoBoletos(dadosSQL.getDouble("valorCobrarReimpressaoBoletos"));
            obj.setVencimentoDescontoProgressivoDiaUtil(dadosSQL.getBoolean("vencimentoDescontoProgressivoDiaUtil"));
            obj.setUtilizaPlanoFinanceiroReposicao(dadosSQL.getBoolean("utilizaPlanoFinanceiroReposicao"));
            obj.setUtilizaPlanoFinanceiroInclusao(dadosSQL.getBoolean("utilizaPlanoFinanceiroInclusao"));
            obj.setImprimirBoletoComLogoBanco(dadosSQL.getBoolean("imprimirBoletoComLogoBanco"));
            obj.setImprimirBoletoComImagemLinhaDigitavel(dadosSQL.getBoolean("imprimirBoletoComImagemLinhaDigitavel"));
            obj.setUsarContaCorrenteTurmaIncluida(dadosSQL.getBoolean("usarContaCorrenteTurmaIncluida"));
            obj.setRecomendacaoRenegociacaoVisaoAluno(dadosSQL.getString("recomendacaoRenegociacaoVisaoAluno"));
            obj.setNumeroDiasNotificarVencimentoContaReceber(dadosSQL.getInt("numeroDiasNotificarVencimentoContaReceber"));
            obj.setEnviarNotificacaoConsultorMatricula(dadosSQL.getBoolean("enviarNotificacaoConsultorMatricula"));
            obj.setEmailEnviarNotificacaoConsultorMatricula(dadosSQL.getString("emailEnviarNotificacaoConsultorMatricula"));
            obj.setNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor(dadosSQL.getInt("numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor"));
            obj.setVencimentoParcelaDiaUtil(dadosSQL.getBoolean("vencimentoParcelaDiaUtil"));
            obj.setQuantidadeDiasEnviarMensagemCobrancaInadimplente(dadosSQL.getInt("quantidadeDiasEnviarMensagemCobrancaInadimplente"));
            obj.setPeriodicidadeDiasEnviarMensagemCobrancaInadimplente(dadosSQL.getInt("periodicidadeDiasEnviarMensagemCobrancaInadimplente"));
            obj.getGrupoDestinatarioMensagemCobrancaInadimplente().setCodigo(dadosSQL.getInt("grupoDestinatarioMensagemCobrancaInadimplente"));
            obj.setNumeroDiasBloquearAcessoAlunoInadimplente(dadosSQL.getInt("numeroDiasBloquearAcessoAlunoInadimplente"));            
            obj.setTipoEnvioInadimplencia(dadosSQL.getString("tipoEnvioInadimplencia"));
            obj.setQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente(dadosSQL.getInt("quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente"));
            obj.setQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente(dadosSQL.getInt("quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente"));
            obj.setAlterarDataVencimentoParcelaDiaUtil(dadosSQL.getBoolean("alterarDataVencimentoParcelaDiaUtil"));			
            obj.setQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente(dadosSQL.getInt("quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente"));
            obj.setQuantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno(dadosSQL.getInt("quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno"));
            obj.setQuantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno(dadosSQL.getInt("qtdDiasAntesVencimentoApresentarContaReceberMatVisaoAluno"));
            obj.setPermitiVisualizarContaReceberVisaoAlunoPreMatricula(dadosSQL.getBoolean("permitiVisualizarContaReceberVisaoAlunoPreMatricula"));
            obj.setNaoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor(dadosSQL.getBoolean("naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor"));
            obj.setNaoApresAlunoInadimplenteDiarioEspelhoNota(dadosSQL.getBoolean("naoApresAlunoInadimplenteDiarioEspelhoNota"));
            obj.setQuantidadeDiasEnviarQuartaMensagemCobrancaInadimplente(dadosSQL.getInt("quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente"));
            obj.setQuantidadeDiasEnviarAvisoDesconto(dadosSQL.getInt("quantidadeDiasEnviarAvisoDesconto"));
            obj.setCancelarContaReceberCandidatoInadimplenteAposDataProva(dadosSQL.getBoolean("cancelarContaReceberCandidatoInadimplenteAposDataProva"));
            obj.setQtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente(dadosSQL.getInt("qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente"));
            obj.setExcluirNegociacaoRecebimentoVencida(dadosSQL.getBoolean("excluirNegociacaoRecebimentoVencida"));
            obj.setCobrarJuroMultaSobreValorCheioConta(dadosSQL.getBoolean("cobrarJuroMultaSobreValorCheioConta"));
            obj.setRealizarMatriculaComFinanceiroManualAtivo(dadosSQL.getBoolean("realizarMatriculaComFinanceiroManualAtivo"));
            obj.setUtilizarIntegracaoFinanceira(dadosSQL.getBoolean("utilizarIntegracaoFinanceira"));
            obj.getIndiceReajustePadraoContasPorAtrasoVO().setCodigo(dadosSQL.getInt("indiceReajustePadraoContasPorAtraso"));
            obj.setQtdDiasAplicarIndireReajustePorAtrasoContaReceber(dadosSQL.getInt("qtdDiasAplicarIndireReajustePorAtrasoContaReceber"));
            obj.setIdClienteMundiPag(dadosSQL.getString("idClienteMundiPag"));
            obj.setAmbienteCartaoCreditoEnum(AmbienteCartaoCreditoEnum.valueOf(dadosSQL.getString("ambienteCartaoCredito")));
            obj.setChaveContaMundipagg(dadosSQL.getString("chaveContaMundipagg"));
            obj.setUtilizarv1(dadosSQL.getBoolean("utilizarv1"));
            obj.setBloquearCalouroPagarMatriculaVisaoAluno(dadosSQL.getBoolean("bloquearCalouroPagarMatriculaVisaoAluno"));
            obj.setBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente(dadosSQL.getBoolean("bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente"));
            obj.setPermiteNegociarParcelaMatricula(dadosSQL.getBoolean("permiteNegociarParcelaMatricula"));
            obj.setValorMinimoGerarPendenciaControleCobranca(dadosSQL.getDouble("valorMinimoGerarPendenciaControleCobranca"));
            obj.setPermiteNegociarParcelaMensalidade(dadosSQL.getBoolean("permiteNegociarParcelaMensalidade"));
            obj.setPermiteNegociarParcelaMaterialDidatico(dadosSQL.getBoolean("permiteNegociarParcelaMaterialDidatico"));
            obj.setPermiteNegociarParcelaBiblioteca(dadosSQL.getBoolean("permiteNegociarParcelaBiblioteca"));
            obj.setPermiteNegociarParcelaOutras(dadosSQL.getBoolean("permiteNegociarParcelaOutras"));
            obj.setPermiteNegociarParcelaContratoReceita(dadosSQL.getBoolean("permiteNegociarParcelaContratoReceita"));
            obj.setPermiteNegociarParcelaDevolucaoCheque(dadosSQL.getBoolean("permiteNegociarParcelaDevolucaoCheque"));
            obj.setPermiteNegociarParcelaNegociacao(dadosSQL.getBoolean("permiteNegociarParcelaNegociacao"));
            obj.setPermiteNegociarParcelaInclusaoReposicao(dadosSQL.getBoolean("permiteNegociarParcelaInclusaoReposicao"));
            obj.setApresentarFormaRecebimentoContaReceberVisaoAluno(dadosSQL.getBoolean("apresentarFormaRecebimentoContaReceberVisaoAluno"));
            obj.setCriarContaReceberPendenciaArquivoRetornoAutomaticamente(dadosSQL.getBoolean("criarContaReceberPendenciaArquivoRetornoAutomaticamente"));
            obj.getCategoriaDespesaPadraoRestituicaoAluno().setCodigo(dadosSQL.getInt("categoriaDespesaPadraoRestituicaoAluno"));
            obj.getArquivoIreportMovFin().setCodigo(dadosSQL.getInt("arquivoIreportMovFin"));
            if (dadosSQL.getString("operadora") == null) {
            	obj.setOperadora(EmpresaOperadoraCartaoEnum.NENHUM);
            } else {
            	obj.setOperadora(EmpresaOperadoraCartaoEnum.valueOf(dadosSQL.getString("operadora")));
            }
            obj.setMerchantIdCielo(dadosSQL.getString("merchantidcielo"));
            obj.setMerchantKeyCielo(dadosSQL.getString("merchantkeycielo"));
            obj.setTokenRede(dadosSQL.getString("tokenrede"));
            obj.setPvRede(dadosSQL.getString("pvrede"));
            obj.setObservacaoComprovanteRecebimento(dadosSQL.getString("observacaoComprovanteRecebimento"));
            obj.setNomeParcelaMatriculaApresentarAluno(dadosSQL.getString("nomeParcelaMatriculaApresentarAluno"));
            obj.setNomeParcelaMaterialDidaticoApresentarAluno(dadosSQL.getString("nomeParcelaMaterialDidaticoApresentarAluno"));
            obj.setSiglaParcelaMatriculaApresentarAluno(dadosSQL.getString("siglaParcelaMatriculaApresentarAluno"));
            obj.setSiglaParcelaMaterialDidaticoApresentarAluno(dadosSQL.getString("siglaParcelaMaterialDidaticoApresentarAluno"));
            obj.setValorMaximoCompraDiretaRequisicao(dadosSQL.getDouble("valorMaximoCompraDiretaRequisicao"));
            obj.setQtdeDiasExcluirNegociacaoContaReceberVencida(dadosSQL.getInt("qtdeDiasExcluirNegociacaoContaReceberVencida"));
            obj.setNovoObj(Boolean.FALSE);
            
            if(Uteis.isAtributoPreenchido(dadosSQL.getInt("categoriaDespesa"))) {
    			obj.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(dadosSQL.getInt("categoriaDespesa"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
    		}

            if(Uteis.isAtributoPreenchido(dadosSQL.getInt("bancoPadraoRemessa"))) {
            	obj.setBancoPadraoRemessa(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(dadosSQL.getInt("bancoPadraoRemessa"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
            }

            if(Uteis.isAtributoPreenchido(dadosSQL.getInt("formaPagamentoPadrao"))) {
            	obj.setFormaPagamentoPadrao(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("formaPagamentoPadrao"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
            }
            
            if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoparcelanegociar"))) {
            	obj.setTipoParcelaNegociar(TipoParcelaNegociarEnum.valueOf(dadosSQL.getString("tipoparcelanegociar")));
            }
            if (Uteis.isAtributoPreenchido(dadosSQL.getString("filtroPadraoContaReceberVisaoAluno"))) {
            	obj.setFiltroPadraoContaReceberVisaoAluno(dadosSQL.getString("filtroPadraoContaReceberVisaoAluno"));
            }
            obj.setTipoOrigemMatriculaRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemMatriculaRotinaInadimplencia"));
            obj.setTipoOrigemBibliotecaRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemBibliotecaRotinaInadimplencia"));
            obj.setTipoOrigemMensalidadeRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemMensalidadeRotinaInadimplencia"));
            obj.setTipoOrigemDevolucaoChequeRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemDevolucaoChequeRotinaInadimplencia"));
            obj.setTipoOrigemNegociacaoRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemNegociacaoRotinaInadimplencia"));
            obj.setTipoOrigemContratoReceitaRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemContratoReceitaRotinaInadimplencia"));
            obj.setTipoOrigemOutrosRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemOutrosRotinaInadimplencia"));
            obj.setTipoOrigemMaterialDidaticoRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemMaterialDidaticoRotinaInadimplencia"));
            obj.setTipoOrigemInclusaoReposicaoRotinaInadimplencia(dadosSQL.getBoolean("tipoOrigemInclusaoReposicaoRotinaInadimplencia"));

            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
                obj.setListaConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartao(obj.getCodigo(), false, nivelMontarDados, usuario));
                return obj;
            }
            obj.setListaConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartao(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
            montarDadosContaCorrentePadraoControleCobranca(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosFormaPagamentoPadraoControleCobranca(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosConfiguracoes(obj, nivelMontarDados, usuario);
            montarDadosDescontoProgressivoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaMensalidadePadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            obj.setCentroReceitaMaterialDidaticoPadrao(Uteis.montarDadosVO(dadosSQL.getInt("CentroReceitaMaterialDidaticoPadrao"), CentroReceitaVO.class, p -> getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
            montarDadosCentroReceitaInscricaoProcessoSeletivoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaMatriculaPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaRequerimentoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaReposicaoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaBibliotecaPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaNegociacaoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCentroReceitaParcelaAvulsaControleCobranca(obj,Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
            montarDadosFormaPagamentoPadraoCheque(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosFormaPagamentoPadraoProvisaoCusto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCategoriaDespesaPadraoAntecipacaoCheque(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosDepartamentoPadraoAntecipacaoCheque(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoMensalidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoMaterialDidatico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoProcessoSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoBiblioteca(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoRequerimento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoRenegociacao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosModeloBoletoOutros(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaPagarPadraoJuro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaPagarPadraoDesconto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaPagarPadraoCredito(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaPagarPadraoDebito(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaReceberPadraoJuro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaReceberPadraoDesconto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaReceberPadraoCredito(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPlanoContaReceberPadraoDebito(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosHistoricoContaPagarPadraoJuro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosHistoricoContaPagarPadraoDesconto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosHistoricoContaReceberPadraoJuro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosHistoricoContaReceberPadraoDesconto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCategoriaDespesaPadraoRestituicaoAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosArquivoIreportMovFin(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);			
            obj.setNivelMontarDados(NivelMontarDados.TODOS);
            return obj;

        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
    }

    public static void montarDadosContaCorrentePadraoControleCobranca(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrentePadraoControleCobranca().getCodigo().intValue() == 0) {
            obj.setContaCorrentePadraoControleCobranca(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrentePadraoControleCobranca(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrentePadraoControleCobranca().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoOutros(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoOutros().getCodigo().intValue() == 0) {
            obj.setModeloBoletoOutros(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoOutros(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoOutros().getCodigo(), nivelMontarDados, usuario));
    }
	
    public static void montarDadosModeloBoletoBiblioteca(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getModeloBoletoBiblioteca().getCodigo().intValue() == 0) {
    		obj.setModeloBoletoBiblioteca(new ModeloBoletoVO());
    		return;
    	}
    	obj.setModeloBoletoBiblioteca(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoBiblioteca().getCodigo(), nivelMontarDados, usuario));
    }
	
    public static void montarDadosModeloBoletoRenegociacao(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getModeloBoletoRenegociacao().getCodigo().intValue() == 0) {
    		obj.setModeloBoletoRenegociacao(new ModeloBoletoVO());
    		return;
    	}
    	obj.setModeloBoletoRenegociacao(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoRenegociacao().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoRequerimento(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoRequerimento().getCodigo().intValue() == 0) {
            obj.setModeloBoletoRequerimento(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoRequerimento(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoRequerimento().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoProcessoSeletivo(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoProcessoSeletivo().getCodigo().intValue() == 0) {
            obj.setModeloBoletoProcessoSeletivo(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoProcessoSeletivo(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoProcessoSeletivo().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoMensalidade(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoMensalidade().getCodigo().intValue() == 0) {
            obj.setModeloBoletoMensalidade(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoMensalidade(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoMensalidade().getCodigo(), nivelMontarDados, usuario));
    }
    
    public static void montarDadosModeloBoletoMaterialDidatico(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getModeloBoletoMaterialDidatico().getCodigo().intValue() == 0) {
    		obj.setModeloBoletoMaterialDidatico(new ModeloBoletoVO());
    		return;
    	}
    	obj.setModeloBoletoMaterialDidatico(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoMaterialDidatico().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoMatricula(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoMatricula().getCodigo().intValue() == 0) {
            obj.setModeloBoletoMatricula(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoMatricula(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoMatricula().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosConfiguracoes(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getConfiguracoesVO().getCodigo().intValue() == 0) {
            obj.setConfiguracoesVO(new ConfiguracoesVO());
            return;
        }
        obj.setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(obj.getConfiguracoesVO().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosDepartamentoPadraoAntecipacaoCheque(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDepartamentoPadraoAntecipacaoCheque().getCodigo().intValue() == 0) {
            obj.setDepartamentoPadraoAntecipacaoCheque(new DepartamentoVO());
            return;
        }
        obj.setDepartamentoPadraoAntecipacaoCheque(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamentoPadraoAntecipacaoCheque().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosFormaPagamentoPadraoControleCobranca(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFormaPagamentoPadraoControleCobranca().getCodigo().intValue() == 0) {
            obj.setFormaPagamentoPadraoControleCobranca(new FormaPagamentoVO());
            return;
        }
        obj.setFormaPagamentoPadraoControleCobranca(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoPadraoControleCobranca().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosFormaPagamentoPadraoCheque(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFormaPagamentoPadraoCheque().getCodigo().intValue() == 0) {
            obj.setFormaPagamentoPadraoCheque(new FormaPagamentoVO());
            return;
        }
        obj.setFormaPagamentoPadraoCheque(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoPadraoCheque().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosFormaPagamentoPadraoProvisaoCusto(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFormaPagamentoPadraoProvisaoCusto().getCodigo().intValue() == 0) {
            obj.setFormaPagamentoPadraoProvisaoCusto(new FormaPagamentoVO());
            return;
        }
        obj.setFormaPagamentoPadraoProvisaoCusto(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoPadraoProvisaoCusto().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCategoriaDespesaPadraoAntecipacaoCheque(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo().intValue() == 0) {
            obj.setCategoriaDespesaPadraoAntecipacaoCheque(new CategoriaDespesaVO());
            return;
        }
        obj.setCategoriaDespesaPadraoAntecipacaoCheque(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaMensalidadePadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaMensalidadePadrao().getCodigo().intValue() == 0) {
            obj.setCentroReceitaMensalidadePadrao(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaMensalidadePadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaMensalidadePadrao().getCodigo(), false, niveMontarDados, usuario));
    }
    
    
    public static void montarDadosCentroReceitaParcelaAvulsaControleCobranca(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaParcelaAvulsaControleCobranca().getCodigo().intValue() == 0) {
            obj.setCentroReceitaParcelaAvulsaControleCobranca(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaParcelaAvulsaControleCobranca(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaParcelaAvulsaControleCobranca().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaNegociacaoPadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaNegociacaoPadrao().getCodigo().intValue() == 0) {
            obj.setCentroReceitaNegociacaoPadrao(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaNegociacaoPadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaNegociacaoPadrao().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaInscricaoProcessoSeletivoPadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo().intValue() == 0) {
            obj.setCentroReceitaInscricaoProcessoSeletivoPadrao(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaInscricaoProcessoSeletivoPadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaInscricaoProcessoSeletivoPadrao().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaMatriculaPadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaMatriculaPadrao().getCodigo().intValue() == 0) {
            obj.setCentroReceitaMatriculaPadrao(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaMatriculaPadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaMatriculaPadrao().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaBibliotecaPadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaBibliotecaPadrao().getCodigo().intValue() == 0) {
            obj.setCentroReceitaBibliotecaPadrao(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaBibliotecaPadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaBibliotecaPadrao().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaRequerimentoPadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue() == 0) {
            obj.setCentroReceitaRequerimentoPadrao(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaRequerimentoPadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaRequerimentoPadrao().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosCentroReceitaReposicaoPadrao(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getCentroReceitaReposicaoPadrao().getCodigo().intValue() == 0) {
    		obj.setCentroReceitaReposicaoPadrao(new CentroReceitaVO());
    		return;
    	}
    	obj.setCentroReceitaReposicaoPadrao(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaReposicaoPadrao().getCodigo(), false, niveMontarDados, usuario));
    }
    
    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>DescontoProgressivoVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>DescontoProgressivoVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDescontoProgressivoPadrao(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivoPadrao().getCodigo().intValue() == 0) {
            obj.setDescontoProgressivoPadrao(new DescontoProgressivoVO());
            return;
        }
        obj.setDescontoProgressivoPadrao(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoPadrao().getCodigo(), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaPagarPadraoJuro(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaPagarPadraoJuro().getCodigo().intValue() == 0) {
            obj.setPlanoContaPagarPadraoJuro(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaPagarPadraoJuro(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaPagarPadraoJuro().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaPagarPadraoDesconto(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaPagarPadraoDesconto().getCodigo().intValue() == 0) {
            obj.setPlanoContaPagarPadraoDesconto(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaPagarPadraoDesconto(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaPagarPadraoDesconto().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaPagarPadraoCredito(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaPagarPadraoCredito().getCodigo().intValue() == 0) {
            obj.setPlanoContaPagarPadraoCredito(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaPagarPadraoCredito(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaPagarPadraoCredito().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaPagarPadraoDebito(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaPagarPadraoDebito().getCodigo().intValue() == 0) {
            obj.setPlanoContaPagarPadraoDebito(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaPagarPadraoDebito(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaPagarPadraoDebito().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaReceberPadraoJuro(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaReceberPadraoJuro().getCodigo().intValue() == 0) {
            obj.setPlanoContaReceberPadraoJuro(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaReceberPadraoJuro(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaReceberPadraoJuro().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaReceberPadraoDesconto(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaReceberPadraoDesconto().getCodigo().intValue() == 0) {
            obj.setPlanoContaReceberPadraoDesconto(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaReceberPadraoDesconto(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaReceberPadraoDesconto().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaReceberPadraoCredito(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaReceberPadraoCredito().getCodigo().intValue() == 0) {
            obj.setPlanoContaReceberPadraoCredito(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaReceberPadraoCredito(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaReceberPadraoCredito().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>PlanoContaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosPlanoContaReceberPadraoDebito(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoContaReceberPadraoDebito().getCodigo().intValue() == 0) {
            obj.setPlanoContaReceberPadraoDebito(new PlanoContaVO());
            return;
        }
        obj.setPlanoContaReceberPadraoDebito(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaReceberPadraoDebito().getCodigo(), 0, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>HistoricoContabilVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>HistoricoContaBilVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosHistoricoContaReceberPadraoDesconto(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getHistoricoContaReceberPadraoDesconto().getCodigo().intValue() == 0) {
            obj.setHistoricoContaReceberPadraoDesconto(new HistoricoContabilVO());
            return;
        }
        obj.setHistoricoContaReceberPadraoDesconto(getFacadeFactory().getHistoricoContabilFacade().consultarPorChavePrimaria(obj.getHistoricoContaReceberPadraoDesconto().getCodigo(), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>HistoricoContabilVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>HistoricoContaBilVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosHistoricoContaReceberPadraoJuro(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getHistoricoContaReceberPadraoJuro().getCodigo().intValue() == 0) {
            obj.setHistoricoContaReceberPadraoJuro(new HistoricoContabilVO());
            return;
        }
        obj.setHistoricoContaReceberPadraoJuro(getFacadeFactory().getHistoricoContabilFacade().consultarPorChavePrimaria(obj.getHistoricoContaReceberPadraoJuro().getCodigo(), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>HistoricoContabilVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>HistoricoContaBilVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosHistoricoContaPagarPadraoDesconto(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getHistoricoContaPagarPadraoDesconto().getCodigo().intValue() == 0) {
            obj.setHistoricoContaPagarPadraoDesconto(new HistoricoContabilVO());
            return;
        }
        obj.setHistoricoContaPagarPadraoDesconto(getFacadeFactory().getHistoricoContabilFacade().consultarPorChavePrimaria(obj.getHistoricoContaPagarPadraoDesconto().getCodigo(), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>HistoricoContabilVO</code> relacionado ao objeto
     * <code>ConfiguracaoFinanceiroVO</code>. Faz uso da chave primária da
     * classe <code>HistoricoContaBilVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     * @throws Exception
     */
    public static void montarDadosHistoricoContaPagarPadraoJuro(ConfiguracaoFinanceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getHistoricoContaPagarPadraoJuro().getCodigo().intValue() == 0) {
            obj.setHistoricoContaPagarPadraoJuro(new HistoricoContabilVO());
            return;
        }
        obj.setHistoricoContaPagarPadraoJuro(getFacadeFactory().getHistoricoContabilFacade().consultarPorChavePrimaria(obj.getHistoricoContaPagarPadraoJuro().getCodigo(), usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>ConfiguracaoFinanceiroVO</code> através de sua chave primária.
     * 
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public ConfiguracaoFinanceiroVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return getAplicacaoControle().getConfiguracaoFinanceiroPorCodigoVO(codigoPrm);
    }
    
    @Override
    public ConfiguracaoFinanceiroVO consultarPorChavePrimariaUnica(Integer codigoPrm, UsuarioVO usuario) throws Exception {        
        String sql = "SELECT * FROM ConfiguracaoFinanceiro WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ConfiguracaoFinanceiro ).");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return ConfiguracaoFinanceiro.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ConfiguracaoFinanceiro.idEntidade = idEntidade;
    }
 

    public void adicionarObjConfiguracaoFinanceiroCartaoVOs(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().validarDados(obj);
        int index = 0;
        Iterator i = configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().iterator();
        while (i.hasNext()) {
            ConfiguracaoFinanceiroCartaoVO objExistente = (ConfiguracaoFinanceiroCartaoVO) i.next();
            if ((objExistente.getOperadoraCartaoVO().getCodigo().intValue() == obj.getOperadoraCartaoVO().getCodigo().intValue()) && (objExistente.getContaCorrenteVO().getCodigo().intValue() == obj.getContaCorrenteVO().getCodigo().intValue())) {
                configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().set(index, obj);
                return;
            }
            index++;
        }
        configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().add(obj);
    }

    public void excluirObjConfiguracaoFinanceiroCartaoVOs(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception {
        int index = 0;
        Iterator i = configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().iterator();
        while (i.hasNext()) {
            ConfiguracaoFinanceiroCartaoVO objExistente = (ConfiguracaoFinanceiroCartaoVO) i.next();
            if (objExistente.getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()) && objExistente.getContaCorrenteVO().getCodigo().equals(obj.getContaCorrenteVO().getCodigo())) {
                configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().remove(index);
                return;
            }
            index++;
        }
    }

    public ConfiguracaoFinanceiroCartaoVO consultarObjConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception {
        Iterator i = configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().iterator();
        while (i.hasNext()) {
            ConfiguracaoFinanceiroCartaoVO objExistente = (ConfiguracaoFinanceiroCartaoVO) i.next();
            if (objExistente.getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()) && objExistente.getContaCorrenteVO().getCodigo().equals(obj.getContaCorrenteVO().getCodigo())) {
                return objExistente;
            }
        }
        return null;
    }

    public ConfiguracaoFinanceiroVO consultarPorCodigoConfiguracoes(
            Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM configuracaofinanceiro WHERE configuracoes = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoConfiguracoes});
        if (resultado.next()) {
            return montarDados(resultado, nivelMontarDados, usuario);
        }
        return new ConfiguracaoFinanceiroVO();
    }

    public ConfiguracaoFinanceiroVO verificarConfiguracaoUtilizarGeracaoParcela(UnidadeEnsinoVO unidadeEnsinoAlunoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroPadraoVO, UsuarioVO usuarioVO) throws Exception {
    	return getAplicacaoControle().getConfiguracaoFinanceiroVO(unidadeEnsinoAlunoVO.getCodigo());
    }
    
    public ConfiguracaoFinanceiroVO consultarPorUnidadeEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	return getAplicacaoControle().getConfiguracaoFinanceiroVO(unidadeEnsino);    	
    }
    
    public static void montarDadosCategoriaDespesaPadraoRestituicaoAluno(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo().intValue() == 0) {
            obj.setCategoriaDespesaPadraoRestituicaoAluno(new CategoriaDespesaVO());
            return;
        }
        obj.setCategoriaDespesaPadraoRestituicaoAluno(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesaPadraoRestituicaoAluno().getCodigo(), false, niveMontarDados, usuario));
    }

    public static void montarDadosArquivoIreportMovFin(ConfiguracaoFinanceiroVO obj, int niveMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getArquivoIreportMovFin().getCodigo().intValue() == 0) {
    		obj.setArquivoIreportMovFin(new ArquivoVO());
    		return;
    	}
    	obj.setArquivoIreportMovFin(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoIreportMovFin().getCodigo(), niveMontarDados, usuario));
    }
    
    public ConfiguracaoFinanceiroVO consultarPorMatriculaAluno(String matricula, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" SELECT unidadeensino.codigo as unidadeensino from matricula ");
    	sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
    	sqlStr.append(" where matricula.matricula = ? ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
    	if(rs.next()) {
			return getAplicacaoControle().getConfiguracaoFinanceiroVO(rs.getInt("unidadeensino"));
		}
		return getAplicacaoControle().getConfiguracaoFinanceiroVO(0);
    	
    }

	@Override
	public ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiraComBaseContaReceber(Integer contaReceber)
			throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select unidadeensinofinanceira from contareceber where codigo = ? ", contaReceber);
		if(rs.next()) {
			return getAplicacaoControle().getConfiguracaoFinanceiroVO(rs.getInt("unidadeensinofinanceira"));
		}
		return getAplicacaoControle().getConfiguracaoFinanceiroVO(0);
	}
	
	@Override
	public ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiraComBaseContaReceberCasoExista(Integer contaReceber) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select unidadeensinofinanceira from contareceber where codigo = ? ", contaReceber);
		if(rs.next()) {
			return getAplicacaoControle().getConfiguracaoFinanceiroVO(rs.getInt("unidadeensinofinanceira"));
		}
		return null;
	}

}
