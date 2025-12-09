package negocio.interfaces.financeiro;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroExcelVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.financeiro.ContaReceberCobrancaRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface ContaReceberInterfaceFacade {

    public void executarAjusteNossoNumeroMatriculaCancelada(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void validarUsuarioConsultarMinhasContasReceber(UsuarioVO usuario) throws Exception;

    public ContaReceberVO novo(UsuarioVO usuario) throws Exception;

    public void validarDadosParaImpressaoBoletos(TipoBoletoBancario tipoBoletoBancario, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    public void incluir(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void gerarNumeroDoc(final ContaReceberVO obj, String numeroBanco, UsuarioVO usuario) throws Exception;
    
    public void executarGeracaoNossoNumeroContaReceber(final ContaReceberVO obj, String numeroBanco, Boolean forcarRegerarNossoNumero, UsuarioVO usuario) throws Exception;

    public String gerarNumeroDocumento(UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuario) throws Exception;

    public ContaReceberVO criarContaReceber(DevolucaoChequeVO devolucaoChequeVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public ContaReceberVO criarContaReceber(MatriculaVO matricula, PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, ContaCorrenteVO contaCorrenteVO, int codigoOrigem, String tipoOrigem, Date dataVencimento, Date dataCompetencia, double valor, int centroReceita, String tipoBoleto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FornecedorVO fornecedorVO) throws Exception;

    public ContaReceberVO criarContaReceber(MatriculaVO matricula, ParceiroVO parceiro, PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, ContaCorrenteVO contaCorrenteVO, int codigoOrigem, String tipoOrigem, Date dataVencimento, Date dataCompetencia, double valor, int centroReceita, int numParcela, int totalParcelas, String tipoBoleto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FornecedorVO fornecedorVO, String observacao) throws Exception;

    public void alterar(ContaReceberVO obj,  ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean validarAcesso, UsuarioVO usuario) throws Exception;

    public void gravarContaReceberNegociacao(List listaContaReceberVencidas, List novasContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void alterar(ContaReceberVO obj, boolean modificarSituacaoContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void alterar(final ContaReceberVO obj, boolean modificarSituacaoContaReceber, boolean modificarMatriculaPeriodoFinanceiroParaManual, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void processarConfirmacaoPagamentoMatricula(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void modificarSituacaoContaReceber(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoOrigemContaReceber(ContaReceberVO obj, UsuarioVO usuario) throws Exception;

    public void estornarSituacaoOrigemContaReceber(ContaReceberVO obj, UsuarioVO usuario) throws Exception;

    public void alterarStatusInscricaoProcessoSeletivo(Integer codigo, String situacao, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoMatricula(Integer matricula, String situacao, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoRequerimento(Integer codigo, Boolean realizandoRecebimento, String sitFinan, String sitExecucao, boolean realizandoBaixaAutomatica, UsuarioVO usuario) throws Exception;

    public void incluirRecebimentoEmReceitaDW(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void estornarRecebimentoEmReceitaDW(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void alterarSituacao(final Integer codigo, final String situacao, UsuarioVO usuario) throws Exception;

    public void excluir(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro,Boolean verificarPermissao, UsuarioVO usuario) throws Exception;

    public void excluir(ContaReceberVO obj, MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, ConfiguracaoFinanceiroVO configuracaoFinanceiro, Boolean verificarPermissao,UsuarioVO usuario) throws Exception;

    public void excluirContasReceberTipoOrigemCodigoOrigem(String tipoOrigem, Integer codigoOrigem, String situacao, UsuarioVO usuario) throws Exception;

    public void excluirContasReceberTipoOrigemCodigoOrigem(String tipoOrigem, Integer codigoOrigem, String situacao, String parcela, UsuarioVO usuario) throws Exception;

    public void excluir(ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorCentroReceita(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    List<ContaReceberVO> consultaRapidaPorIdentificadorCentroReceita(String nomeSacado, String centroReceita, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> ordenador) throws Exception;

    public Integer consultaRapidaPorIdentificadorCentroReceitaTotalRegistros(String nomeSacado, String centroReceita, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorIdentificadorCentroReceitaSituacaoPessoa(String valorConsulta, Integer pessoa, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public ContaReceberVO consultarNomeUltimaParcela(Integer pessoa) throws Exception;

    public List consultarPorPendeciaFinanceiraAluno(Integer pessoa, Integer unidadeEnsino, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorMatriculaAlunoPorTipoOrigem(String matricula, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public Boolean consultarExistenciaPendenciaFinanceiraMatricula(String matricula, UsuarioVO usuario) throws Exception;

    public List consultarPorParceiroPorTipoOrigem(String valorConsulta, Integer codigoParceiro, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorOrigemContaReceber(String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorNrDocumento(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorNrDocumento(String valorConsulta, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public Integer consultaRapidaPorNrDocumentoTotalRegistros(String valorConsulta, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorCodOrigemTipoOrigem(String tipoOrigem, Integer codigoOrigem, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoOrigem(String tipoOrigem, Integer codigoOrigem, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorNrDocumentoSituacaoPessoa(TipoPessoa tipoPessoa, String valorConsulta, Integer pessoa, Integer parceiro, Integer fornecedor, String matricula, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoBarra(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorCodigoBarra(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public Integer consultaRapidaPorCodigoBarraTotalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorCodigoBarraSituacaoPessoa(TipoPessoa tipoPessoa, String valorConsulta, Integer pessoa, Integer parceiro, Integer fornecedor, String matricula, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCandidato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCPF(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorCPF(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public Integer consultaRapidaPorCPFTotalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorAluno(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorAlunoLimiteOffset(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,List<String> ordenador) throws Exception;

    public Integer consultaRapidaPorAlunoTotalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorAluno(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataSituacaoPessoa(TipoPessoa tipoPessoa, Date prmIni, Integer pessoa, Integer parceiro, Integer fornecedor, String matricula, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorFuncionario(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    List<ContaReceberVO> consultaRapidaPorFuncionario(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> ordenador) throws Exception;

    public Integer consultaRapidaPorFuncionarioToalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorConvenio(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorConvenio(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> ordenador) throws Exception;

    public Integer consultaRapidaPorConvenioTotalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorTipoEUnidadeEnsino(String valorConsulta, Integer pessoa, Integer unidadeEnsino, String tipoOrigem, List<String> listaTipoOrigemContaCaixa, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoEConvenioEUnidadeEnsino(String valorConsulta, Integer convenio, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorNrDocumentoPessoa(String valorConsulta, Integer pessoa, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorAlunoEMatriculaContasReceberVencidas(Integer pessoa, String matricula, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultarPorAlunoEMatriculaContasReceberMensalidadeEMatricula(Integer pessoa, String matricula, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultarPorAlunoEMatriculaContasReceberBiblioteca(Integer pessoa, String matricula, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public Boolean verificarContaReceberPessoaTipoBiblioteca(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ContaReceberVO consultarPorChavePrimariaSituacao(Integer codigoPrm, String situacao, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorCodigo(Integer valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception; 
    
    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, String tipoOrigem, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public Integer consultarPorCodigoTotalRegistros(Integer valorConsulta, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarContaReceberPorSituacaoUnidadeEnsino(Integer unidadeEnsino, String situacao, Date dataBase, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorNomeSacado(String nomeSacado, List<String> situacao, int unidadeEnsino, String tipoOrigem, Boolean consDataCompetencia, Date dataInicio, Date dataFim, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> ordenador) throws Exception;

    public Integer consultaRapidaPorNomeSacadoTotalRegistros(String nomeSacado, List<String> situacao, int unidadeEnsino, String tipoOrigem, Boolean consDataCompetencia, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List consultarPorMatriculaTipoPessoaSituacaoDiferenteDe(String matricula, String situacao, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorMatriculaSituacaoTipoOrigens(String matricula, String situacao, String tipoOrigens, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public ContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);

    public void baixarContaReceberConcedendoDescontoTotalAMesma(Integer codigoContaReceber, Date dataBaseBaixa, String justificativa, Boolean liberacaoCancelamentoTrancamento, Boolean realizandoRecebimentoAutomaticoMatricula, UsuarioVO responsavel, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
    
    public void alterarMatriculaPeriodoFinanceiroManualPelaContaReceber(Integer codigoMatriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void alterarMatriculaPeriodoVencimentoDaContaReceber(ContaReceberVO contaReceberVo, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public ContaReceberVO alterarContaReceberPelaMatriculaPeriodoVencimento(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> executarCalculoValorFinalASerPago(List<ContaReceberVO> listaContaReceber, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Date dataBaseCalcular) throws Exception;

    void carregarDados(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    void carregarDados(ContaReceberVO obj, NivelMontarDados nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    List<ContaReceberVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception;

    //List<ContaReceberVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorAlunoEMatriculaContasReceberVencidas(Integer pessoa, Boolean possuiIntegracaoFinanceira, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public Boolean executarVerificarContaReceberImportadaSistemaLegada(Integer codigoConta, UsuarioVO usuario) throws Exception;

    public void gerarDadosBoleto(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean validarPermissao, UsuarioVO usuario) throws Exception;

    public ContaReceberVO consultarPorNossoNumero(String string, boolean b, int nivelmontardadosDadosbasicos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorMatricula(String matricula) throws Exception;

    public Boolean consultarPendenciaFinanceiraPorCodPessoaFichaAluno(Integer codPessoa, UsuarioVO usuario) throws Exception;

    public void executarAjusteNossoNumeroRepetido(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<PlanoFinanceiroAlunoDescricaoDescontosVO> executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(
            Boolean parcelaReferenteMatricula,
            Double valorBase,
            String tipoDescontoAluno, // valor ("VA") ou percentual ("PO")
            Double percDescontoAluno,
            Double valorDescontoAluno,
            Boolean descontoAlunoValidoAteVencimento,
            Date dataBaseVencimento,
            Date dataOriginalVencimento,
            List<OrdemDescontoVO> ordemAplicacaoDescontos,
            DescontoProgressivoVO descontoProgressivoVO,
            List<PlanoDescontoVO> listaPlanosDescontosAluno,
            List<ConvenioVO> listaConveniosAluno,
            Integer diasVariacaoDataVencimento,
            Boolean usaDescontoCompostoPlanoDesconto,
            ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,
                        Boolean realizandoRecebimento, Date dataReferenciaConsiderarBaixaTitulo, String matricula, Boolean aplicarCalculoComBaseDescontosCalculados, Integer cidade) throws Exception;

    public ContaReceberVO consultarPorNossoNumeroBB(String string, int nivelmontardadosDadosbasicos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void incluir(final ContaReceberVO obj, final boolean verificarPermissao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public Boolean consultarPendenciaFinanceiraPorCodMatriculaPeriodoFichaAluno(Integer codMatriculaPeriodo, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorMatriculaParaSerasa(String matriculaAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    public void atualizarDataVencimentoParaRegerarBoleto(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean validarPermissao, UsuarioVO usuario, ControleRemessaContaReceberVO crcr) throws Exception;

	//public void atualizarVencimentoTurma(List<MatriculaVO> matriculaVOs, List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs, Date novaDataVencimentoContasTurma, Boolean atualizarContaPorParcela, String parcela, String periodicidade, String ano, String semestre, Boolean contaVencida, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, int contadorMesesContasRecebidas) throws Exception;

    public void atualizarVencimentoTurma(List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs, Date novaDataVencimentoContasAluno, Boolean atualizarContaPorParcela,  ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void processarRotinaRemoverContaReceberDuplicada(String mesReferencia, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    public Integer consultarQtdeContasPorMatriculaPeriodo(Integer codMatriculaPeriodo, String tipoOrigem1, String tipoOrigem2, UsuarioVO usuario) throws Exception;

    
    public List consultarUnidadeEnsinoFinanceiraDaContaReceberPorMatriculaOuResponsavelFinanceiro(String valorConsulta, String matricula, Integer responsavelFinanceiro, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
    
    public List consultarPorMatriculaEUnidadeEnsino(String valorConsulta, String matricula, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultarPorTipoCursoEUnidadeEnsino(String valorConsulta, Integer pessoa, Integer curso, Integer unidadeEnsino, List<String> listaTipoOrigemDesconsiderar, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, Integer limite, Integer offset, DataModelo dataModelo,boolean permiteVisualizarParcelasPeriodoContratosEletronico , boolean permitePagamentoParcelasPeriodoContratosEletronico , List<Integer> matriculaPeriodosContratosPendenteRejeitado) throws Exception;

    public void excluir(ContaReceberVO contaReceber, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorNossoNumero(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultaRapidaPorAlunoTipoPessoaContasReceberVencidas(Integer pessoa, String tipoPessoa, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorMatriculaContaReceberMatriculaMensalidadeImpressaoBoletoTrue(String matricula, Integer codMatriculaPeriodo) throws Exception;

    public void alterarBooleanEmissaoBoletoRealizada(final Integer codContaReceber, final boolean impressaoBoletoRealizada, UsuarioVO usuario) throws Exception;

    public Integer consultarQuantidadeContasRecebidasPorMatricula(String matricula);

    public List<ContaReceberVO> consultarContasVinculadasNegociacao(Integer codigoNegociacaoContaReceber, String matricula, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public ContaReceberVO consultarContaReceberReferentePreMatriculaPorMatriculaPerido(Integer matriculaPeriodo) throws Exception;

    public List<ContaReceberVO> consultarContasQueNaoForamAdicionadasAoArquivoRemessaEntreDatas(Date dataInicio, Date dataFim, Integer codigoUnidadeEnsino, int nivelMontarDados,
            ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public Boolean consultarExistenciaContaReceberRecebidaOuNegociadaPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorNossoNumero(String valorConsulta, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public Integer consultaRapidaPorNossoNumeroTotalRegistros(String valorConsulta, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorMatriculaSomentePessoaESituacaoAReceber(String matricula) throws Exception;    

    public List<ContaReceberVO> consultaRapidaPorMatriculaESituacaoAReceber(String matricula, Boolean consultarContaVencida,  Optional<FiltroRelatorioFinanceiroVO> filtroRelatorioFinanceiroVO) throws Exception;

    public ContaReceberVO consultarPorNossoNumeroTelaControleCobranca(String nossoNumero) throws Exception;

    public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;    

    public void realizarAlteracaoContaReceberConformeMapaPendenciaControleCobranca(List<ContaReceberVO> contaReceberVOs, PlanoDescontoVO planoDesconto, DescontoProgressivoVO descontoProgressivo, UsuarioVO usuarioVO) throws Exception;

    public List<ContaReceberVO> consultaCompletaPorMatriculaPeriodoTipoMensalidade(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorMatriculaEUnidadeEnsino(String matricula, String situacao, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorCursoEUnidadeEnsinoSituacao(Integer pessoa, String situacao, Integer curso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultarQtdeContasComBaseNaDataVencimento(String matricula, String tipoPessoa, String parcelaMatricula, Boolean somenteMensalidade) throws Exception;

    public void excluirComBaseNaDataVencimentoSituacaoMatricula(String matricula, String situacao, UsuarioVO usuarioLogado) throws Exception;

    public Boolean consultarAlunoPossuiContasRenegociadas(String matricula, String situacao, UsuarioVO usuarioLogado) throws Exception;

    public String consultarUltimaParcelaInclusaoDisciplinaForaPrazo(String matricula, Integer matriculaPeriodo, UsuarioVO usuarioLogado) throws Exception;

    public ContaReceberVO criarContaReceberInclusaoForaPrazo(MatriculaVO matricula, ParceiroVO parceiro, PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, ContaCorrenteVO contaCorrenteVO, int codigoOrigem, String tipoOrigem,
            Date dataVencimento, Date dataCompetencia, double valor, int centroReceita, int numParcela, int totalParcelas, String tipoBoleto, String partePrefixoParcela, TurmaVO turma, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, Integer matriculaPeriodo) throws Exception;

    public ContaReceberVO consultarPorNossoNumeroControleCobranca(String nossoNumero, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public String consultarUltimaParcelaReposicaoDisciplina(String matricula, Integer matriculaPeriodo, UsuarioVO usuarioLogado) throws Exception;

    public void baixarContaReceberConcedendoDescontoTotalAMesmaPorNegociacaoRecebimento(NegociacaoRecebimentoVO obj, String justificativa, UsuarioVO responsavel, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void alterarComDescontoCemPorCento(final ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception;

    public Date consultarDataVencimento(Integer codigoContaReceber) throws Exception;

    public void excluirPorMatriculaPeriodoNaoRecebidaNaoNegociada(Integer codigoMatriculaPeriodo, UsuarioVO usuario) throws Exception;
    
    public List<ContaReceberVO> consultarPorCursoTurmaMatriculaSituacaoDataVencimento(String matricula, Integer turma, Integer curso, Integer unidadeEnsino, Integer unidadeEnsinoAcademica, String situacao, Date dataIni, Date dataFim, Integer contaCorrenteDestino, boolean regerarCarteiraContaCorrenteDestino, Integer contaCorrenteOrigem, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, boolean trazerContasRegistradas, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void alterarCarteira(final ContaReceberVO obj, UsuarioVO usuario) throws Exception;

    public void regerarDadosBoletoMudancaCarteira(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public Map<BigInteger, ContaReceberVO> consultarContaBaixarPix(PixContaCorrenteVO pixoVO, boolean bancoBrasil) throws Exception;
    public Map<BigInteger, ContaReceberVO> consultarPorNossoNumeroControleCobranca(RegistroArquivoVO registroArquivoVO, ProgressBarVO progressBarVO, Boolean bancoBrasil) throws Exception;
    public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaEntreDatas(Date dataInicio, Date dataFim, Integer codigoUnidadeEnsino, ControleRemessaVO controleRemessaVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void alterarDataArquivoRemessa(List<ControleRemessaContaReceberVO> dadosRemessaVOs, Date dataArquivoRemessa, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorSituacaoUnidadeEnsinoTotalRegistros(Integer unidadeEnsino, String situacao, Date dataBase, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario,List<String> listaStringFiltroSelecionado) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorSituacaoUnidadeEnsino(Integer unidadeEnsino, String situacao, Date dataBase, Integer limite, Integer offset, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario,List<String> listaDeStringFiltroSelecionado) throws Exception;

    public void executarAjusteNossoNumeroParceiro(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void executarAjusteNossoNumeroPorCursoTurmaSituacaoAreceberAlunosComContasPagas(Integer curso, Integer turma, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public boolean consultarExistenciaContaGeradaVinculadaOutraContaCorrente(Integer turma, Integer contaCorrente, UsuarioVO usuarioVO);

    public void alterarDataArquivoRemessaContasRejeitadas(final Integer codContaReceber, UsuarioVO usuario) throws Exception;

    public void alterarNossoNumeroBancoContasValidadas(final Integer codContaReceber, final String nossoNumeroBanco, UsuarioVO usuario) throws Exception;

    Double consultarValorDevedorBibliotecaPorPessoa(Integer codigoPessoa);

    public void realizarCriacaoContaReceberMultaBiblioteca(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItensEmprestimo, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;
    

    Boolean consultarSeExistePendenciaFinanceiraPorRestricoesRenovacaoMatriculaVisaoAluno(String codMatricula, UsuarioVO usuario) throws Exception;

    void realizarVinculoContaReceberComResponsavelFinanceiro(ContaReceberVO contaReceberVO, UsuarioVO usuarioLogado) throws Exception;

    List<ContaReceberVO> consultaRapidaPorResponsavelFinanceiroLimiteOffset(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,List<String> ordenador) throws Exception;

    Integer consultaRapidaPorResponsavelFinanceiroTotalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    List<ContaReceberVO> consultarPorResponsavelFinanceiroVisaoPais(String situacao, Integer pais, Integer filho, String matriculaAluno, Integer unidadeEnsino, List<String> listaTipoOrigemDesconsiderar, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, Integer limite, Integer offset, DataModelo dataModelo , boolean permiteVisualizarParcelasPeriodoContratosEletronico , List<Integer> matriculaPeriodosContratosPendenteRejeitado) throws Exception;

    List consultarContaReceberRenegociacaoResponsavelFinanceiro(String valorConsulta, Integer responsavelFinanceiro, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    String validarDadosExisteContaReceberRenegociadadaQueNaoCumpriuAcordo(String matriculaAluno, String nomeAluno);

    List<ContaReceberVO> consultaRapidaPorMatriculaEUnidadeEnsinoVisaoPais(Integer pais, Integer filho, String matricula, String situacao, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset, DataModelo dataModelo) throws Exception;

    List<ContaReceberVO> consultarContaReceberNotificarVencimentoConta() throws Exception;

    public List<ContaReceberVO> consultarContaReceberMatriculaNaoPaga(Integer codUnidadeEnsino) throws Exception;

    public List consultaRapidaPorCodigoFormaPagamentoNegociacaoRecebimento(Integer codigoFormaPgtoNegRec, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public Date obterDataVerificandoDiaUtil(Date dataBaseVencimento, Integer cidade, UsuarioVO usuario) throws Exception;
    
    public CursoVO obterCursoVerificandoTurmaOrMatricula(ContaReceberVO obj, UsuarioVO usuario) throws Exception;
    
    public TurnoVO obterTurnoVerificandoTurmaOrMatricula(ContaReceberVO obj, UsuarioVO usuario) throws Exception;

    public void alterarDataVencimentoContaReceber(final Integer codContaReceber, final Date dataVencimento, UsuarioVO usuario) throws Exception;

    public void excluirComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

    public List<ContaReceberVO> consultarPorTipoUnidadeEnsinoMatricula(String valorConsulta, Integer aluno, String matricula, Integer unidadeEnsino, String tipoOrigem, List<String> listaTipoOrigemContaCaixa, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void excluirPorChequeDevolvido(Integer codigoCheque, UsuarioVO usuario) throws Exception ;

    public void gerarLinhaDigitavelSemGerarNossoNumero(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public List<String> consultarParcelaPorTurma(TurmaVO turma, UsuarioVO usuarioVO) throws Exception;

    public List<ContaReceberVO> consultaRapidaPorNomeSacadoAnoSemestre(String nomeSacado, String ano, String semestre, List<String> situacao, int unidadeEnsino, String tipoOrigem, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    public Integer consultaRapidaPorNomeSacadoAnoSemestreTotalRegistros(String nomeSacado, String ano, String semestre, List<String> situacao, int unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentes(Integer codigoUnidade) throws Exception;
    List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentes(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    void incluirValorTaxaBoleto(Integer codContaReceber, Double taxaBoleto, UsuarioVO usuario) throws Exception;

    public List<ContaReceberVO> consultarAlunoNotificarBoleto() throws Exception;
    
    public List consultarAlunoNotificarBoleto(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO) throws Exception;

    public void registrarAlunoRecebeuNotificacaoBoleto(Integer codigo, UsuarioVO usuario) throws Exception;

    public ContaReceberVO consultarPrimeiraMensalidade(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

    public void alterarSituacaoSuplicidadeTratada(final Integer codContaReceber, final Boolean tratada, UsuarioVO usuario) throws Exception;

    List<ContaReceberVO> consultaRapidaPorFornecedorLimiteOffset(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> ordenador) throws Exception;

    Integer consultaRapidaPorFornecedorTotalRegistros(String valorConsulta, Boolean dataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

    List<ContaReceberVO> consultarPorTipoUnidadeEnsinoFornecedor(String valorConsulta, Integer fornecedor, Integer unidadeEnsino, String tipoOrigem, List<String> listaTipoOrigemContaCaixa, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<String> consultarParcelaPorTurma(TurmaVO turma, Optional<FiltroRelatorioFinanceiroVO> filtroRelatorioFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public List<ContaReceberVO> consultaContaReceberRecebidaEmDuplicidadePorNossoNumero(String valorConsulta) throws Exception;

    public List<ContaReceberVO> consultaContaReceberRecebidaEmDuplicidadePorPeriodoDataVcto(Date dataInicio, Date dataFim) throws Exception;

    public List<ContaReceberVO> consultaContaReceberRecebidaEmDuplicidadePorTratada(Boolean tratada) throws Exception;

	void realizarProcessamentoCalculoValorPrimeiraFaixaDesconto(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	List<ContaReceberVO> consultaRapidaPorNomeAlunoLimiteOffset(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,List<String> ordenador) throws Exception;

	Integer consultaRapidaPorNomeAlunoTotalRegistros(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

	void realizarAtualizacaoNumeroParcelaContaReceberMensalidade(Integer matriculaPeriodo, Integer totalParcela, UsuarioVO usuario) throws Exception;

	void alterarValor(ContaReceberVO obj, UsuarioVO usuario) throws Exception;
	
	public List<ContaReceberVO> consultaRapidaPorMatriculaEUnidadeEnsinoVisaoAluno(String matricula, String situacao, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset, DataModelo dataModelo) throws Exception;
	
    public void realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(Integer nrAnosProcessar, List<ContaReceberVO> contaReceberProcessar, Boolean validarRealizadoProcessamento, String competencia, Boolean considerarDataFutura, UsuarioVO usuario, Boolean lancarExcecao , Boolean considerarPeriodoVencimento,Date dataInicioPeriodoVencimento , Date dataFimPeriodoVencimento, Boolean verificarCompetencia ) throws Exception;
	
	public List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesPrimeiraMsg(Integer codigoUnidade) throws Exception;
	public List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesSegundaMsg(Integer codigoUnidade) throws Exception;
	public List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesTerceiraMsg(Integer codigoUnidade) throws Exception;

	void realizarCorrecaoValorRecebidoPlanoDescontoEConvenioContaReceber(UsuarioVO usuario) throws Exception;

	String executarVerificacaoQuantidadeDigitosNossNumeroPorBanco(String baseNossoNumero, String numeroBanco, String tipoOrigem, String tipoNumeracaoGeracao, String numeroParcela);

	List<ContaReceberVO> consultarPorCodigoAgrupamento(Integer codigoAgrupamento, NivelMontarDados nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	List<ContaReceberVO> consultarContaReceberPorAptaAdicionarAgrupamento(Integer codigoUnidadeEnsino, Date dataVencimento, TipoPessoa tipoPessoa, String matricula, Integer responsavelFinanceiro, UsuarioVO usuarioVO) throws Exception;
	
	public List<Integer> consultarContaReceberPorContaAgrupada(Integer contaReceberAgrupada);

	public List consultaRapidaPorCodigoContaReceberAgrupada(Integer codigoContaReceberAgrupada, UsuarioVO usuario) throws Exception;
	public List<ContaReceberVO> consultarContaReceberPorNossoNumeroContaAgrupada(String nossoNumero);
	
	public void executarCorrecaoBaseDadosDistribuicaoDescontosQuandoDescontoForMaiorQueValorContaReceber(Date dataInicioAtualizacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
	
	public void executarCalculoValorBoletoDescontoSemValidade(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public Boolean validaContaCorrenteBloqueadoEmissaoBoleto(Integer codigoContaReceber);

	ContaReceberVO consultarDadosGerarNotaFiscalSaida(Integer codigoPrm, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	public List<ContaReceberVO> consultaRapidaPorMatriculaESituacaoAReceberCompleto(String matricula, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Boolean consultaContaVencida, Date dataInicial, Date dataFinal, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesQuartaMensagem(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesQuartaMsgPrimeiraConsulta(Integer codigoUnidade) throws Exception;

	ContaReceberVO criarContaReceberSemInclusao(MatriculaVO matricula, ParceiroVO parceiro, PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoVO unidadeEnsinoVOAluno, ContaCorrenteVO contaCorrenteVO, int codigoOrigem, String tipoOrigem, Date dataVencimento, Date dataCompetencia, double valor, int centroReceita, int numParcela, int totalParcelas, String tipoBoleto, String partePrefixoParcela, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FornecedorVO fornecedorVO) throws Exception;
	
	public List<ContaReceberVO> consultarTodasContaAReceberTurma(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesPrimeiraMsg(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesSegundaMsg(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	List<ContaReceberCobrancaRelVO> consultarContaReceberAlunosInadimentesTerceiraMsg(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
	
	public List<ContaReceberCobrancaRelVO> consultarContaReceberAvisoDesconto(Integer codigoUnidadeEnsino, ConfiguracaoFinanceiroVO configFinaneiro, UsuarioVO usuario) throws Exception;

	List<ContaReceberVO> consultarContaReceberInscricaoCandidatoInadimplente(Integer qtdeDiasAposDataProvaRemoverContaReceberCandidato) throws Exception;

	Boolean consultarExistenciaPendenciaFinanceiraMatriculaEmAtraso(String matricula, UsuarioVO usuario) throws Exception;
		
	public void alterarValorBaseContaReceber(Integer codigo, Double valor, UsuarioVO usuario) throws Exception;

	void executarRegerarBoletoVencidoInscricaoCandidato(InscricaoVO inscricaoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
        
    public void baixarContaReceberVOConcedendoDescontoTotalAMesma(ContaReceberVO contaBaixar,  Date dataBaseBaixa, String justificativa, Boolean liberacaoCancelamentoTrancamento, UsuarioVO responsavel, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
        
	Boolean executarVerificarContaReceberGerada(String codOrigem, String tipoOrigem, String parcela, Integer pessoa, UsuarioVO usuarioVO) throws Exception;

	public List<ContaReceberVO> consultarPorMatriculaContasReceberMensalidadeEMatriculaEmAberto(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	
	public List<ContaReceberVO> consultarPorTipoEConvenioEUnidadeEnsinoDadosApresentacaoModalNegociacaoRecebimento(String nomeParceiro, String valorConsulta, Integer unidadeEnsino, String tipoOrigem, List<String> listaTipoOrigemContaCaixa, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	
	public void validarDadosAtualizarVencimentoTurma(String parcela, String periodicidade, String ano, String semestre, Boolean atualizarContaPorParcela, UsuarioVO usuarioVO) throws Exception;
        
        //public void excluirContasReceberTipoOrigemCodigoOrigemConvenioEspecifico(Integer convenioRemover, String tipoOrigem, Integer codigoOrigem, String situacao, String parcela, UsuarioVO usuario) throws Exception;
        
	public void cancelarContaReceber(ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception;

	public void reativarContaReceberCancelada(ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception;
	
	public ContaReceberVO criarContaReceber(MatriculaVO matricula, ParceiroVO parceiro, PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoVO unidadeEnsinoVOAluno, ContaCorrenteVO contaCorrenteVO, int codigoOrigem, String tipoOrigem, Date dataVencimento, Date dataCompetencia, double valor, int centroReceita, int numParcela, int totalParcelas, String tipoBoleto, String partePrefixoParcela, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FornecedorVO fornecedorVO, Integer matriculaPeriodo, String observacao, DepartamentoVO departamentoVO) throws Exception;

	Boolean consultarExistenciaContasReceberVencidasAluno(Integer pessoa, UsuarioVO usuario) throws Exception;
	
	void realizarExecucaoJobCalculoValorTemporarioContaReceber(List<ContaReceberVO> contaReceberVOs, UsuarioVO usuarioVO);
	
	public List<ContaReceberVO> consultarContaReceberAlterarResponsavelFinanceiro(String valorConsulta, Boolean consDataCompetencia, Date dataIni, Date dataFim, String ano, String semestre, String situacao, Integer unidadeEnsino, Integer unidadeEnsinoFinanceira, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void alterarPessoaContaReceberSituacaoAReceber(final ContaReceberVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarPessoaContaReceberSituacaoRecebido(final ContaReceberVO obj, UsuarioVO usuario) throws Exception;

	public ContaReceberVO criarContaReceber(MatriculaVO matricula, ParceiroVO parceiro, PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoVO unidadeEnsinoVOAluno, ContaCorrenteVO contaCorrenteVO, int codigoOrigem, String tipoOrigem, Date dataVencimento, Date dataCompetencia, double valor, int centroReceita, int numParcela, int totalParcelas, String tipoBoleto, String partePrefixoParcela, TurmaVO turma, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FornecedorVO fornecedorVO, Integer matriculaPeriodo, String observacao, DepartamentoVO departamentoVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 20/03/2015
	 * @param obj
	 * @param configuracaoFinanceiro
	 * @param usuario
	 * @throws Exception
	 */
	void registrarLinhaDigitavelCodigoBarrasBoletoNossoNumeroContaReceber(final Integer codigo, final String nossoNumero, final String numeroDocumento, final String codigoBarra, final String linhaDigitavel, UsuarioVO usuario) throws Exception;

	List<PlanoFinanceiroAlunoDescricaoDescontosVO> executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(Boolean parcelaReferenteMatricula, Double valorBase, String tipoDescontoAluno, Double percDescontoAluno, Double valorDescontoAluno, Boolean descontoAlunoValidoAteVencimento, Date dataBaseVencimento, Date dataOriginalVencimento, List<OrdemDescontoVO> ordemAplicacaoDescontos, DescontoProgressivoVO descontoProgressivoVO, List<PlanoDescontoVO> listaPlanosDescontosAluno, List<ConvenioVO> listaConveniosAluno, Integer diasVariacaoDataVencimento, Boolean usaDescontoCompostoPlanoDesconto, Boolean vencimentoDescontoProgressivoDiaUtil, Boolean realizandoRecebimento, Date dataReferenciaConsiderarBaixaTitulo, String matricula, Boolean aplicarCalculoComBaseDescontosCalculados, Boolean gerarDescontosAplicaveisAposVencimento, Integer cidade) throws Exception;

	List<PlanoFinanceiroAlunoDescricaoDescontosVO> executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(Boolean parcelaReferenteMatricula, Double valorBase, String tipoDescontoAluno, Double percDescontoAluno, Double valorDescontoAluno, Boolean descontoAlunoValidoAteVencimento, Date dataBaseVencimento, Date dataOriginalVencimento, List<OrdemDescontoVO> ordemAplicacaoDescontos, DescontoProgressivoVO descontoProgressivoVO, List<PlanoDescontoVO> listaPlanosDescontosAluno, List<ConvenioVO> listaConveniosAluno, Integer diasVariacaoDataVencimento, Boolean usaDescontoCompostoPlanoDesconto, Boolean vencimentoDescontoProgressivoDiaUtil, Boolean realizandoRecebimento, Date dataReferenciaConsiderarBaixaTitulo, String matricula, Boolean aplicarCalculoComBaseDescontosCalculados, Integer cidade) throws Exception;
	
	void excluirContasReceberIntegracaoFinanceiro(UsuarioVO usuario) throws Exception;

	boolean incluirContaReceberIntegracaoFinanceiro(ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaContaReceberUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

	List<ContaReceberVO> consultaCompletaPorMatriculaPeriodoTipoMatriculaEMensalidadeAReceber(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public Boolean verificaSituacaoContaReceberPorCodigoSituacao(Integer codigoPrm, String situacao, UsuarioVO usuario) throws Exception;
	
	/** 
	 * @author Wellington - 24 de set de 2015 
	 * @param matriculaPeriodo
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	Double consultarValorParcelasInclusaoPorMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioLogado) throws Exception;
	
	public Boolean consultarSituacaoRecebidaContaReceberTipoOrigemRequerimento(String codOrigem, Double valor,Integer codigo) throws Exception;

    public void realizarVerificacaoPermiteRecebimentoOnlineVisaoAlunoBoletoMatriculaCalouro(MatriculaVO matricula, List<ContaReceberVO> contaReceberVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
    
    public void realizarVerificacaoPermiteRecebimentoOnlineVisaoAlunoBoletoMatriculaInadimplente(MatriculaVO matricula,List<ContaReceberVO> contaReceberVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso,  UsuarioVO usuarioVOCorrente,  int nivelMontarDados, UsuarioVO usuarioVO, Boolean origemAplicativo) throws Exception;

	public List<ContaReceberVO> consultaRapidaPorCodigoFinanceiroMatricula(String codigoFinanceiroMatricula, List<String> situacao, int unidadeEnsino, String tipoOrigem, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;
	
	public Integer consultaRapidaPorCodigoFinanceiroMatriculaTotalRegistros(String codigoFinanceiroMatricula, List<String> situacao, int unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;
	
		/**
		 * @author Rodrigo Wind - 23/11/2015
		 * @param obj
		 * @param usuario
		 * @throws Exception
		 */
		void gravarValorFinalContaReceberAtualizadoComAcrescimosEDescontos(ContaReceberVO obj, UsuarioVO usuario, Boolean sofreuAlteracao, Boolean verificarCompetencia) throws Exception;

		public List<ContaReceberVO> consultaRapidaPorMatriculaDataVencimentoEDiaSemenaESituacaoAReceberCompleto(String matricula, Date dataVencimentoInicio, Date dataVencimentoFinal, DiaSemana diaSemana, Boolean consultaContaVencida, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Optional<FiltroRelatorioFinanceiroVO> filtroRelatorioFinanceiroVO, String parcela, UsuarioVO usuario) throws Exception;
		
		public void realizarSimulacaoDataVencimentoAtualizar(Date novaDataVencimento, List<ContaReceberVO> listaContaReceberVOs, UsuarioVO usuarioVO) throws Exception;
		
		public void realizarAtualizacaoVencimentoPorPeriodo(List<MatriculaVO> listaMatriculaVOs, Date novaDataVencimento, ProgressBarVO progressBarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
		
		//public void realizarAtualizacaoDataVencimento(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
		
		public void validarDadosAtualizacaoVencimentoPorPeriodo(Date dataVencimentoInicio, Date dataVencimentoFinal) throws Exception;
		Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> realizarVerificacaoPermiteRecebimentoOnlineUsarMinhasContasVisaoAluno(String matriculaAluno, List<ContaReceberVO> contaReceberVOs, UsuarioVO usuarioVO) throws Exception;
		public Boolean consultarMatriculaComPendenciaFinanceiraExterna(String matriculaAluno) throws Exception;
		
	/** 
	 * @author Wellington - 15 de abr de 2016 
	 * @param codigoOrigem
	 * @param tipoOrigem
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	boolean executarVerificacaoContaReceberRecebidaOuNegociada(Integer codigoOrigem, String tipoOrigem, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 28/04/2016
	 * @param matricula
	 * @param matriculaPeriodo
	 * @param convenio
	 * @return
	 * @throws Exception
	 */
	Boolean consultarExistenciaContaReceberConvenioRecebidaNegociadaPorMatriculaPeriodoEConvenio(String matricula, Integer matriculaPeriodo, Integer convenio) throws Exception;
	
	boolean consultarExistenciaContaReceberRecebidaNegociadaPorCodigo(Integer contaReceber) throws Exception;

	/**
	 * @author Rodrigo Wind - 28/04/2016
	 * @param matricula
	 * @param matriculaPeriodo
	 * @param convenio
	 * @param parcela
	 * @return
	 * @throws Exception
	 */
	Boolean consultarExistenciaContaReceberAlunoVinculadoContaConvenio(String matricula, Integer matriculaPeriodo, Integer convenio, String parcela) throws Exception;
	

	/** 
	 * @author Victor Hugo de Paula Costa - 29 de abr de 2016 
	 * @param consulta
	 * @param consDataCompetencia
	 * @param codigoUnidadeEnsino
	 * @param ano
	 * @param semestre
	 * @param configuracaoFinanceiroVO
	 * @param contaReceberVO
	 * @param tipoOrigem
	 * @param limite
	 * @param offset
	 * @param nivelMontarDados
	 * @param valorConsultaReceberRecebidoNegociado
	 * @param usuarioVO
	 * @param filtroRelatorioFinanceiroVO 
	 * @return
	 * @throws Exception 
	 */
	List<ContaReceberVO> consultar(ControleConsulta consulta, Boolean consDataCompetencia, int codigoUnidadeEnsino, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaReceberVO contaReceberVO, String tipoOrigem, int limite, int offset, int nivelMontarDados, List<String> valorConsultaReceberRecebidoNegociado, UsuarioVO usuarioVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,List<String> listaDeStringFiltroSelecionado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 29 de abr de 2016 
	 * @param consulta
	 * @param consDataCompetencia
	 * @param codigoUnidadeEnsino
	 * @param ano
	 * @param semestre
	 * @param configuracaoFinanceiroVO
	 * @param contaReceberVO
	 * @param tipoOrigem
	 * @param limite
	 * @param offset
	 * @param nivelMontarDados
	 * @param valorConsultaReceberRecebidoNegociado
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	Integer consultarTotalRegistros(ControleConsulta consulta, Boolean consDataCompetencia, int codigoUnidadeEnsino, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaReceberVO contaReceberVO, String tipoOrigem, int limite, int offset, int nivelMontarDados, List<String> valorConsultaReceberRecebidoNegociado, UsuarioVO usuarioVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> listaDeStringFiltroSelecionado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 17 de jun de 2016 
	 * @param codigoContaReceber
	 * @throws Exception 
	 */
	void alterarSituacaoPagoDCCFalso(Integer codigoContaReceber) throws Exception;
	
	/** 
	 * @author Victor Hugo de Paula Costa - 22 de jun de 2016 
	 * @param codigoContaReceber
	 * @throws Exception 
	 */
	void alterarSituacaoPagoDCCTrue(Integer codigoContaReceber) throws Exception;
	
	public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaEntreDatasUtilizandoIntegracaoFinanceira(Date dataInicio, Date dataFim, Integer codigoUnidadeEnsino, ControleRemessaVO controleRemessaVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 27/09/2016
	 * @param codigo
	 * @param motivoCancelamento
	 * @param dataCancelamento
	 * @param responsavelCancelamento
	 * @param usuario
	 * @throws Exception
	 */
	void alterarDadosCancelamento(Integer codigo, String motivoCancelamento, Date dataCancelamento, Integer responsavelCancelamento, UsuarioVO usuario) throws Exception;				
	
	public List<ContaReceberVO> consultarPorAlunoEMatriculaContasReceberMensalidadeEMatriculaCancelado(Integer pessoa, String matricula, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param codigoCurso
	 * @param matricula
	 * @param tipoContasAPagar
	 * @param usuarioVOCorrente
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<ContaReceberVO> consultarContasAPagarVisaoAluno(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, String matricula, String tipoContasAPagar, UsuarioVO usuarioVOCorrente, Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> ConfiguracaoRecebimentoCartaoOnlineVOs, int nivelMontarDados, UsuarioVO usuarioVO, Boolean origemAplicativo, Integer limite, Integer offset, DataModelo dataModelo) throws Exception;
	
	public List<ContaReceberVO> consultarConvenioFinanciamentoProprioVisaoAluno(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String matricula, UsuarioVO usuarioVOCorrente, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void alterarNotaFiscalSaidaServico(ContaReceberVO contaReceberVO, Integer codigoNotaFiscalServicoSaida, UsuarioVO usuario) throws Exception;

	Integer consultarNumeroNotaFiscalSaidaServicoPorContaReceber(Integer contaReceber) throws Exception;

	void realizarVinculoNotaFiscalSaidaContaReceberPorNossoNumero(List<ContaReceberVO> mapNossoNumeroNotaFiscal, UsuarioVO usuario) throws Exception;

	void consultarContasReceberTipoOrigemCodigoOrigemSituacaoParcela(String parcela, MatriculaPeriodoVO matPeriodoVO, UsuarioVO usuario) throws Exception;

	void removerVinculoNotaFiscalSaidaServicoContaReceber(Integer notaFiscalSaidaServico, UsuarioVO usuarioVO) throws Exception;

	void adicionarDescontoRateioEmPlanoFinanceiroAlunoDescricaoDescontosVO(
			List<PlanoFinanceiroAlunoDescricaoDescontosVO> planoFinanceiroAlunoDescricaoDescontosVOs, Double valorBase, Double valorCusteadoContaReceber,
			Double valorDescontoRateio, Date dataVencimento, Date dataReferenciaConsiderarBaixaTitulo) throws Exception;

	public void alterarIdentificacaoTituloBanco(final String nossoNumero, final String identificacaoTituloBanco, UsuarioVO usuario) throws Exception;
	
	List<ContaReceberVO> consultarPorMatriculaTipoOrigemSituacaoMesAnoFichaAluno(String matricula, TipoOrigemContaReceber tipoOrigemContaReceber, SituacaoContaReceber situacaoContaReceber, String mesAno, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> consultarMesAnoContaReceberPorAluno(Integer aluno, UsuarioVO usuarioVO);

	List<ContaReceberVO> consultarPorMatriculaPeriodoFichaAluno(Integer matriculaPeriodo, UsuarioVO usuarioVO);

	/**
	 * @author Kennedy Souza
	 * @param pessoa
	 * @return
	 * @throws Exception
	 */
	public Boolean verificarExistenciaDeContaParaResponsavelFinanceiroDiferenteDoAtual(PessoaVO pessoa) throws Exception;

	void validarTipoImpressao(List<ContaReceberVO> contaReceberVOs, UsuarioVO usuarioVO) throws Exception;
	
	ContaReceberVO consultarPorContaReceberRecebimento(Integer contaReceberRecebimento, UsuarioVO usuarioVO);
	public ContaReceberVO consultarContaReceberPorFormaPagamentoRecebimentoCartaoCredito(Integer formaPagamentoRecebimentoCartaoCredito, UsuarioVO usuarioLogado) throws Exception;
	void validarContaReceberEmMemoriaAntesAlteracao(ContaReceberVO contaReceber) throws Exception;

	void atualizarAtributoUpdatedContaReceberAposAlteracaoPeloSistemaGarantindoAssimIntegridade(
			ContaReceberVO contaReceber) throws Exception;

	

	void verificarContasRemetidasQueTiveramAlteracao(Integer matriculaPeriodo, MatriculaVO matricula,
			List<ContaReceberVO> listaContaRemetida, List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimento,
			List<ControleRemessaContaReceberVO> listaControleRemessaContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void realizaVinculoContaReceberComControleRemessaContaReceber(
			List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimento, UsuarioVO usuario) throws Exception;

	void executarCalculoValorDiferencaContaDescontos(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO)
			throws Exception;

	List<ContaReceberVO> consultaContasAReceberQuePossuiRemessaGerada(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ContaReceberVO consultarContaReceberProcessamentoArquivoParceiroExcel( ProcessamentoArquivoRetornoParceiroExcelVO processamentoExcelVO, UsuarioVO usuarioVO) throws Exception;
	
	StringBuilder getSQLPadraoConsultaProcessamentoArquivoRetornoParceiro();

	
			
		
		public void verificarAlteracoesContaRemessa(ControleRemessaContaReceberVO crcr, ContaReceberVO contaReceberMpv, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaReceberVO c) throws Exception;
		
		public void alterarContaReceberRemetidaComAlteracao(final Integer codContaReceber, final ContaReceberVO obj) throws Exception;
		
		void montarDadosBasico(ContaReceberVO obj, SqlRowSet dadosSQL) throws Exception;		

		public boolean verificaBloqueioEmissaoBoleto(ContaReceberVO obj, UsuarioVO usuario);
		
		public void atualizarDataCompetenciaDeAcordoDataVencimento(ContaReceberVO obj, UsuarioVO usuarioVO) throws Exception;

		void validarTipoImpressaoPorContaReceber(ContaReceberVO contaReceberVO, Boolean possuiPermissaoEmitirBoletoVencido, UsuarioVO usuarioVO) throws Exception;
		
		public Boolean consultarExistenciaPendenciaFinanceiraMatricula(Integer pessoa, String matricula, UsuarioVO usuario) throws Exception;
		
		public List<ContaReceberVO> consultaRapidaPorPeriodo(int unidadeEnsino, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public Boolean consultarExistenciaPendenciaFinanceiraMatriculaEmAtraso(Integer pessoa, String matricula, UsuarioVO usuario) throws Exception;		
		
		public ContaReceberVO consultarPrimeiraMensalidade(MatriculaPeriodoVO matriculaPeriodoVO, String tipoOrigem, Boolean considerarAnoSemestre) throws Exception;
		
		public void realizarCorrecaoLancamentoContabilPorSql(String sql, ConfiguracaoFinanceiroVO confFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

		public void persistirLancamentoContabilPadrao(ContaReceberVO conta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
		
		void persistirLancamentoContabilVO(ContaReceberVO conta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

		void addLancamentoContabilVO(ContaReceberVO conta, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;

		void removeLancamentoContabilVO(ContaReceberVO conta, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;
		
		public void excluirNossoNumeroContaReceberComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;
		
		public void excluirNossoNumeroContaReceberComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;
		
		void executarBloqueioContaReceber(final Integer codigoContaReceber, final Integer codigoRegistroCobranca, UsuarioVO usuario) throws Exception;
		
		void executarDesbloqueioContaReceber(final Integer codigoContaReceber, UsuarioVO usuario) throws Exception;
		
		void alterarValorIndiceReajusteContaReceber(Integer codigo, BigDecimal valorIndiceReajuste, BigDecimal valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa, UsuarioVO usuario) throws Exception;

		List<ContaReceberVO> consultarContaReceberProcessadaParaCancelamentoPorIndiceReajustePeriodo(Integer indiceReajustePeriodo, UsuarioVO usuarioVO);

		List<ContaReceberVO> consultarContaReceberRecebidasOuEnviadasParaRemessaParaAplicacaoReajustePosteriorEmOutraConta(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Boolean considerarDescontoSemValidadeCalculoIndiceReajuste, UsuarioVO usuarioVO) throws Exception;
		
		List<ContaReceberVO> consultarContaReceberRecebidasOuEnviadasParaRemessaParaAplicacaoReajustePosteriorEmOutraContaBolsaCusteadaConvenio(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, UsuarioVO usuarioVO) throws Exception;

		ContaReceberVO consultarContaReceberProcessadaParaCancelamentoPorIndiceReajustePeriodoMatriculaPeriodoVencimento(Integer indiceReajustePeriodoMatriculaPeriodoVencimento, UsuarioVO usuarioVO);
		
		public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaEntreDatas(String nossoNumero, Integer codigoContaReceber, Date dataInicio, Date dataFim, Integer codigoUnidadeEnsino, ControleRemessaVO controleRemessaVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,  boolean isGeracaoPix, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

		public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaEntreDatasUtilizandoIntegracaoFinanceira(String nossonumero, Date dataInicio, Date dataFim, Integer codigoUnidadeEnsino, ControleRemessaVO controleRemessaVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

		List<ContaReceberVO> consultaCompletaPorMatriculaPeriodoValindadoOrigemPorConfiguracaoRecebimentoCartaoOnline(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioVO) throws Exception;
		
		public List<ContaReceberVO> consultaRapidaPorMatriculaContaReceberMatriculaMensalidadeEditadaManualmente(String matricula, Integer codMatriculaPeriodo) throws Exception;
		
		public void validarRegrasParaFinanciamentoProprioConvenioImprimirBoleto(List<PlanoDescontoContaReceberVO> planoDescontoContaReceberVOs, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos ,  Integer codigoContaReceber, String matricula, String tipoOrigem, Date dataReferenciaQuitacao) throws Exception;
		
		void realizarLiberacaoSuspensaoConvenioPorOperacaoFuncionalidade(ContaReceberVO contaReceber, PlanoDescontoContaReceberVO pdcr, UsuarioVO usuarioVerif, ConfiguracaoFinanceiroVO config, UsuarioVO usuario) throws Exception;
		
		void realizarProcessamentoLiberacaoSuspensaoConvenioFinanciamentoProprio(ContaReceberVO obj, PlanoDescontoContaReceberVO pdcr, UsuarioVO usuario) throws Exception;
		
		void processarLiberacaoSuspensaoConvenioFinanciamentoProprio(String matricula, Integer convenio, Integer parceiro, boolean estorno, UsuarioVO usuario) throws Exception;
		
		void regerarBolsaCusteadaConvenio(ContaReceberVO contaReceber, ConfiguracaoFinanceiroVO configFinan, UsuarioVO usuario) throws Exception;
		
		void atualizarLiberacaoIndiceReajustePorAtraso(ContaReceberVO contaReceber,  UsuarioVO usuarioVerif, ConfiguracaoFinanceiroVO config,UsuarioVO usuarioVO) throws Exception;
		
		public void alterarContasReceberMatriculaPeriodoRemovendoEditadaManualmente(String matricula, Integer codMatriculaPeriodo) throws Exception;
		
		public void alterarContasReplicandoPlanoDescontoManual(List<ContaReceberVO> listaContasReceberReplicarDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
		
		ContaReceberVO realizarCalculoValorOriginalContaReceberUtilizandoMetodoEspirita(Integer codigoCcontaReceber) throws Exception;
		
		ContaReceberVO consultarPorPeriodoVencimentoPorUnidadeEnsinoPorValorPorSituacao( Date dataIni, Date dataFim, Integer unidadeEnsino, Double valor, String situacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,  Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
			
		public HashMap<String, Double> consultaTotalContaReceber(String campoConsulta, String valorConsulta, String ano, String semestre, Boolean consDataCompetencia, Date dataIni, Date dataFim, List<String> situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

		void realizarAtualizacaoValorDescontoFaixaContaReceber(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, boolean forcarCalculoDescontos) throws Exception;

		public void alterarParceiro(Integer codParceiroManter, Integer codParceiroRemover, UsuarioVO usuarioVO) throws Exception;

		Boolean realizarVerificacaoAlunoPossuiContaAReceberVinculadoUnidadeEnsinoFinanceira(String matricula,
				Integer unidadeEnsino);

		void realizarGeracaoDetalhamentoValorContaRecebidaNegociacadaCancelada(Integer nrAnosProcessar,
				List<ContaReceberVO> contaReceberProcessar, String competencia) throws Exception;

		void alterarValoresDaContaReceberPorEstorno(Integer codigoContaReceber, String situacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean bloqueioPorFechamentoMesLiberado, UsuarioVO usuario) throws Exception;
		
		ContaReceberVO realizarRenegociacaoContaReceberAutomaticamente(Integer contaReceber, UsuarioVO usuarioVO)
				throws Exception;

		
		public void verificarPermissaoNegociacaoContaReceber(List<ContaReceberVO> listaTemp, UsuarioVO usuarioLogado) throws Exception;
		
		public void excluirContasReceberTipoOrigemCodigoOrigem(String tipoOrigem, Integer codigoOrigem, String situacao, Boolean validarCompetenciaFechada, UsuarioVO usuario) throws Exception;
		public void excluirContasReceberTipoOrigemCodigoOrigem(String tipoOrigem, Integer codigoOrigem, String situacao, String parcela, Boolean validarCompetenciaFechada, UsuarioVO usuario) throws Exception;
		public void excluirContasReceberTipoOrigemCodigoOrigemConvenioEspecifico(Integer convenioRemover, String tipoOrigem, Integer codigoOrigem, String situacao, String parcela, Boolean validarCompetenciaFechada, UsuarioVO usuario) throws Exception;

		Boolean excluirContaReceberRecebidaSemRegistroRecebimento(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, Boolean verificarPermissao, UsuarioVO usuario) throws Exception;
		
		public void processarRegerarNossoNumeroContaReceber(String listaNossoNumero, UsuarioVO usuario) throws Exception;		
		
		public void processarRegerarNossoNumeroContaReceber(ContaReceberVO obj, ContaCorrenteVO contaCorrenteVO, UsuarioVO usuario) throws Exception;
		
		public ContaReceberVO consultarNossoNumeroContaReceberPorNossoNumero(String nossoNumero, UsuarioVO usuario) throws Exception;
		
		public String adicionarValidacaoParaContaReceberConvenioSeExisteContaAlunoRecebida();
		
		public Boolean validarSeExisteContaParaUsuarioComCodigoContaReceber(Integer usuario, Integer contareceber) throws Exception;


		Map<String, Object> consultarPorSacado(ControleConsulta consulta, Boolean consDataCompetencia,
				int unidadeEnsino, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,
				ContaReceberVO contaReceberVO, String tipoOrigem, int limite, int offset, int nivelMontarDados,
				List<String> situacoes, UsuarioVO usuarioVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO)
				throws Exception;
		
		public Boolean permiteEmitirBoletoAlunoVinculadoParceiro(Integer codigoContaReceber);
		public Boolean permiteRemessaBoletoAlunoVinculadoParceiro(Integer codigoContaReceber);
		
		public void gerarDadosBoleto(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean validarPermissao, boolean forcarRegerarNossoNumero, UsuarioVO usuario) throws Exception;

		void alterarNossoNumeroLinhaDigitavel(Integer codContaReceber, String nossoNumero, String linhaDigitavelCodigoBarras, String codigoBarra, UsuarioVO usuario) throws Exception;

		String consultarNomesParceiroPorContaReceber(List<NotaFiscalSaidaServicoVO> servicos) throws Exception;


		List<Integer> validarExisteContaReceberRenegociadadaQueNaoCumpriuAcordo(Integer codigoPessoa) throws Exception;

		public List<ContaReceberVO> consultarBolsaCusteadaConveioParcelaReajusteAReceberAptasParaAplicacaoReajuste (Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Integer matriculaPeriodo, Boolean considerarDescontoSemValidadeCalculoIndiceReajuste,  Boolean limit1, UsuarioVO usuarioVO) throws Exception;
		
		public List<ContaReceberVO> consultarMatriculaPeriodoVencimentoParcelaReajusteARecebidaAptasParaAplicacaoReajusteBolsaCusteadaConvenio(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Integer matriculaPeriodo,Boolean considerarDescontoSemValidadeCalculoIndiceReajuste, Boolean limit1, UsuarioVO usuarioVO) throws Exception;


		public void realizaVinculoContaReceberConvenioComControleRemessaContaReceber(Integer codigo, UsuarioVO usuario) throws Exception;
		
		public Boolean verificarExisteContaReceberPorCodOrigemTipoOrigemSituacaoNegociacaoContaReceber(String codOrigem, String tipoOrigem, String situacao) throws Exception;

		public void realizarVerificacaoRegraImpressaoBoleto(List<ContaReceberVO> listaContasPagarVisaoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroPrevilegiandoUnidadeEnsino, UsuarioVO usuarioLogado) throws Exception;
		
		public ContaReceberVO alterarVencimentoContaReceberWS(String paramObject) throws Exception;
		
		List<ContaReceberVO> consultarContaReceberRecorrenciaAptasPagamentoPorMatriculaFormaPadraoPagamento(String matricula, FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente, HashMap<Integer, ContaReceberVO> mapContaReceberVOs);
		
		Boolean consultarContaReceberRecebidaPorCodigo(Integer codigo);
		
		public Integer consultarQuantidadeContasAPagarVisaoAluno(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, String matricula, String tipoContasAPagar, UsuarioVO usuarioVOCorrente, Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> configuracaoRecebimentoCartaoOnlineVOs, int nivelMontarDados, UsuarioVO usuarioVO, Boolean origemAplicativo, Integer limite, Integer offset) throws Exception;
		
		public void alterarUnidadeEnsino(TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
		
		
		public Integer consultarQuantidadePorResponsavelFinanceiroVisaoPais(String situacao, Integer pais, Integer filho, String matriculaAluno, Integer unidadeEnsino, List<String> listaTipoOrigemDesconsiderar, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
		
		public Integer consultaQuantidadeRapidaPorMatriculaEUnidadeEnsinoVisaoPais(Integer pais, Integer filho, String matricula, String situacao, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		public boolean consultarExistenciaContaReceber(Integer contaReceber);
		
		public List<ContaReceberVO> consultarContaReceberMatriculaMensalidadeNegociacao(Integer negociacaoContaReceber, UsuarioVO usuario) throws Exception;
		
		public void removerVinculoPeriodoContasBibOut(Integer matriculaAluno, UsuarioVO usuarioVO) throws Exception;
}