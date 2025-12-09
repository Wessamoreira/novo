package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ContaPagarInterfaceFacade {

	public void persistir(final ContaPagarVO obj, Boolean validarAcesso, boolean validarCentroResultadoAposAlteracao, UsuarioVO usuario) throws Exception;
	
	public void incluir(ContaPagarVO obj, Boolean validarAcesso, boolean validarCentroResultadoAposAlteracao, UsuarioVO usuario) throws Exception;

	public void alterar(ContaPagarVO obj, Boolean validarAcesso, boolean validarCentroResultadoAposAlteracao, UsuarioVO usuario) throws Exception;

	public void excluir(ContaPagarVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public ContaPagarVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorCodigoCompra(Integer valorConsulta, TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorCodigoFornecedor(Integer valorConsulta, Boolean pago, Boolean pagoParcialmente, Boolean apagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorCodigoFuncionario(Integer valorConsulta, Boolean pago, Boolean pagoParcialmente, Boolean apagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ContaPagarVO consultarPorChavePrimariaSituacao(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<ContaPagarVO> consultarVencidosPorTipoSacado(Integer codigoSacado, String tipoSacado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarAVencerPorTipoSacado(Integer codigoSacado, String tipoSacado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarVencidosEmPeriodo(Integer codigoSacado, String tipoSacado, Date dataInicio, Date dataTermino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorOrigemContaPagar(String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarContaPagarPorSituacaoUnidadeEnsino(Integer unidadeEnsino, String situacao, Date dataBase, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, Date dataBase, UsuarioVO usuario) throws Exception;

	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorCodigoGrupoContaPagar(Integer grupoContaPagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultarPorCodigoNomeFavorecidoDataVencimentoEdicaoFuncionario(Integer codigoFavorecido, String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public SqlRowSet consultarQtdeMesesContaPagarGraficoLinhaTempo(boolean filtroFavorecido, ContaPagarVO contaPagarVO, Date dataInicio, Date dataTermino) throws Exception;

	public SqlRowSet consultarValorContaPagarGraficoLinhaTempo(boolean filtroFavorecido, ContaPagarVO contaPagarVO, Date dataInicio, Date dataTermino, Boolean montarGraficoPorDia) throws Exception;

	public void executarCriacaoGraficoLinhaContaPagar(ContaPagarVO contaPagarVO, boolean filtroFavorecido, Date dataInicio, Date dataTermino) throws Exception;

	public List<ContaPagarVO> consultaRapidaContaPagarPorSituacaoUnidadeEnsino(Integer unidadeEnsino, String situacao, Date dataBase, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaRapidaContaPagarPorSituacaoUnidadeEnsinoTotalRegistros(Integer unidadeEnsino, String situacao, Date dataBase) throws Exception;

	public Map<String, Double> consultarPorCodigoTotalPagarTotalPago(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Map<String, Double> consultaRapidaPorNomeSacadoTotalPagarTotalPago(String nomeFavorecido, String situacao, int unidadeEnsino, Date dataInicio, Date dataFim, Boolean filtratDataFatoGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Map<String, Double> consultaRapidaPorIdentificadorCentroReceitaTotalPagarTotalPago(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, Boolean filtratDataFatoGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Map<String, Double> consultaRapidaPorSituacaoUnidadeEnsinoTotalPagarTotalPago(Integer unidadeEnsino, String situacao, Date dataBase, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void realizarVinculoContaReceberComResponsavelFinanceiro(ContaPagarVO contaPagarVO, UsuarioVO usuarioLogado) throws Exception;

	List<ContaPagarVO> consultarContaPagarPorUnidadesCategoriaDespesaPeriodo(String identificadorCategoriaDespesa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean trazerContasPagar, Boolean trazerContasAPagar, Integer limit, Integer offset, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodo(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean trazerContasPagas, Boolean trazerContasAPagar) throws Exception;

	Integer consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodoDepartamento(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer codigoDepartamento) throws Exception;

	List<ContaPagarVO> consultarContaPagarPorUnidadesCategoriaDespesaPorDepartamento(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer codigoDepartamento, Integer limit, Integer offset, UsuarioVO usuarioVO) throws Exception;

	public ContaPagarVO criarContaPagarRestituirValorJaPagoPorAlunoConvenio(Date dataVcto, Double valorRestituir, String parcelaCorrespondente, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, OrigemContaPagar origemContaPagar, String descricaoPagamento, ConvenioVO convenio, CategoriaDespesaVO categoriaDespesaPadraoRestituicaoAluno, UsuarioVO usuario) throws Exception;

	public List<ContaPagarVO> consultaRapidaContaPagarPorMatriculaPeriodoConvenio(String matricula, Integer matriculaPeriodo, Integer convenio, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirContasPagarMatriculaPeriodoConvenio(String matricula, Integer matriculaPeriodo, Integer convenio, UsuarioVO usuario) throws Exception;

	void alterarContaPagarPorCodigoOrigemTipoOrigemSituacaoNumeroContratoDespesas(Double valorNovaParcela, String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Integer consultarPorNumeroChequeTotalRegistros(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, Date dataInicio, Date dataFim, Integer unidadeEnsino, Boolean filtarTodoPeriodo) throws Exception;

	public List<ContaPagarVO> consultarPorNumeroCheque(String valorConsulta, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario, Date dataInicio, Date dataFim, Integer unidadeEnsino, Boolean filtarTodoPeriodo, Integer limite, Integer offset) throws Exception;

	public List<ContaPagarVO> consultarPorCodigoCheque(Integer cheque, UsuarioVO usuarioVO);

	ContaPagarVO consultaRapidaContaPagarRestituicaoValorPorMatriculaPeriodo(String matricula, Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ContaPagarVO> consultarPorNotaFiscalEntrada(NotaFiscalEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaPagarControleRemessaContaPagarVO> consultaRapidaContasArquivoRemessaEntreDatas(ControleRemessaContaPagarVO controleRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<FormaPagamentoVO> formaPagamentoVOs, Boolean outroBanco, Boolean semBanco, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiro, UsuarioVO usuario) throws Exception;

	public void gerarContaPagarPaga(Double valorConta, Integer banco, FormaPagamentoVO formaPagamento, CategoriaDespesaVO categoriaDespesa, UnidadeEnsinoVO unidadeEnsino, ContaCorrenteVO contaCorrente, Boolean utilizaAbatimentoNoRepasseRemessaBanco, UsuarioVO usuario) throws Exception;

	void addLancamentoContabilVO(ContaPagarVO conta, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;

	void removeLancamentoContabilVO(ContaPagarVO conta, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;

	void excluirContaPagarPorTipoOrigemPorCodigoOrigem(String tipoOrigem, String codigoOrigem, UsuarioVO usuario) throws Exception;

	void validarAtualizacaoDoCampoSituacaoFinanceiraCompra(ContaPagarVO contaPagar, UsuarioVO usuario);

	Boolean consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(String codOrigem, String situacao, Integer contaPagarDiferente, OrigemContaPagar origemContaPagar, UsuarioVO usuario) throws Exception;

	void persistirLancamentoContabilVO(ContaPagarVO conta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void persistirLancamentoContabilPadrao(ContaPagarVO conta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void preencherDadosRemessaContaPagar(ContaPagarVO obj, String bancoRecebimento, String agenciaRecebimento, String digitoAgenciaRecebimento, String contaRecebimento, String digitoContaRecebimento, String chaveEnderecamentoPIX , TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum ,  UsuarioVO usuario) throws Exception;

	ContaPagarVO consultarPorNossoNumero(Long codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void montarContaPagarPorRegistroDetalhe(ContaPagarRegistroArquivoVO cpra, UsuarioVO usuarioVO) throws Exception;

	Boolean verificarExistenciaContaPagarRecebidaEmDuplicidade(Integer contaPagar) throws Exception;

	Double consultarValorParaAtualizarContaPagar(Integer codigo) throws Exception;

	void excluirContaFilho(ContaPagarVO obj, UsuarioVO usuario) throws Exception;

	void excluirPorNotaFiscalEntrada(NotaFiscalEntradaVO obj, UsuarioVO usuario);

	void validarSeContaPagarExisteVinculoComArquivoRemessa(ContaPagarVO obj);

	Boolean consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(String codigoOrigem, String tipoOrigem, UsuarioVO usuario);

	Boolean consultarSeExisteContaPagarPorCodOrigemPorTipoOrigem(String codigoOrigem, String tipoOrigem, UsuarioVO usuario);
	
	ContaPagarVO consultarPorPeriodoVencimentoPorUnidadeEnsinoPorValorPorSituacao( Date dataIni, Date dataFim, Integer unidadeEnsino, Double valor, String situacao, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ContaPagarVO> consultarPorNotaFiscalEntradaOutrasOrigem(NotaFiscalEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void preencherCamposRemessaPorTipoLancamento(ContaPagarVO obj);

	void consultar(String situacao, Integer unidadeEnsino, boolean filtrarDataFatorGerador, boolean consultaPainelGestorFinanceiro, DataModelo dataModelo);

	void cancelarContaPagar(ContaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void reativarContaPagarCancelada(ContaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void alterarSituacao(Integer codigo, SituacaoFinanceira situacaoFinanceira, UsuarioVO usuario) throws Exception;
	
	void alterarContaPagarPorGestaoContasPagar(ContaPagarVO obj, boolean isAlterouValorContaPagar, UsuarioVO usuario) throws Exception;

	List<ContaPagarVO> consultarPorPeriodoTipoSacadoCodigoSacadoAPagar(String campoConsulta, String valorConsulta, Date dataInicio, Date dataFim, TipoSacado tipoSacado, Integer codigoSacado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	Map<String, Double> consultarPorNumeroNotaFiscalEntradaTotalPagarTotalPago(Integer valorConsulta, Integer unidadeEnsino,  Date dataIni, Date dataFim, String situacao, Boolean filtrarDataFatorGerador) throws Exception;
	
	void adicionarContaPagarPorCentroResultadoOrigem(List<ContaPagarVO> listaContaPagar, List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, ContaPagarVO contaPagar, UsuarioVO usuario);

	void atualizarCentroResultadoOrigemContaPagar(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, ContaPagarVO contaPagar, UsuarioVO usuario);

	public void alterarDadosRemessa(final ContaPagarVO obj, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	public void verificarPossiveisErrosRemessa(ContaPagarControleRemessaContaPagarVO obj, ControleRemessaContaPagarVO controleRemessa);

	Map<String, Double> consultaRapidaPorNrDocumentoTotalPagarTotalPago(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<ContaPagarVO> consultaRapidaContaPagarAdiantamentoPodemSerUtilizadasParaAbatimentoNegociacaoPagamento(Integer unidadeEnsino, String tipoSacado,  Integer codigoSacado, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void alterarValorUtilizadoAdiantamento(final ContaPagarVO obj,  final Double valorUtilizado, final UsuarioVO usuario,  final boolean isOperacaoInclusao) throws Exception;
	
	public void alterarValorDescontoPorUsoAdiantamento(final ContaPagarVO obj,  final Double valorUtilizado, final UsuarioVO usuario) throws Exception;

	Double realizarDistribuicaoAdiantamentosDisponiveisParaAbaterContaPagar(ContaPagarVO contaPagar, Double valorResidualContaPagarParaAbatimento, List<ContaPagarVO> listaAdiantamentosDisponiveisUsarAbatimento, boolean isConsiderarContaPagarParcialmentePaga);
	
	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, Date dataBase, Boolean validarBloqueioFechamentoMes, UsuarioVO usuario) throws Exception;
	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, Boolean validarBloqueioFechamento, UsuarioVO usuario) throws Exception;
	public void alterarContaPagarPorCodigoOrigemTipoOrigemSituacaoNumeroContratoDespesas(Double valorNovaParcela, String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, Boolean verificarBloqueioCompetenciaFechada, UsuarioVO usuario) throws Exception;
	public void excluirListaContasPagar(String listaCodigoExcluir, Boolean validarBloqueioFechamento, UsuarioVO usuario) throws Exception;

	public void validarDados(ContaPagarVO contaPagar) throws ConsistirException;

	public void excluirArquivoContaPagar(ContaPagarVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;
}
