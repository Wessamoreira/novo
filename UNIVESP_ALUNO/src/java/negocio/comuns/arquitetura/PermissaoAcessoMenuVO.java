package negocio.comuns.arquitetura;

/**
 * 
 * @author Otimize-TI
 */
import java.io.Serializable;

public class PermissaoAcessoMenuVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8620347103202529294L;
	// Menu Processo Seletivo
	protected Boolean candidato = false;
	private Boolean candidatoFavorito = false;
	protected Boolean inscricao = false;
	private Boolean inscricaoFavorito = false;
	protected Boolean perfilSocioEconomico = false;
	private Boolean perfilSocioEconomicoFavorito = false;
	protected Boolean disciplinasProcSeletivo = false;
	private Boolean disciplinasProcSeletivoFavorito = false;
	protected Boolean procSeletivo = false;
	private Boolean procSeletivoFavorito = false;
	protected Boolean grupoDisciplinaProcSeletivo = false;
	private Boolean grupoDisciplinaProcSeletivoFavorito = false;
	private Boolean inclusaoDisciplinaForaGradeFavorito = false;
	protected Boolean resultadoProcessoSeletivo = false;
	private Boolean resultadoProcessoSeletivoFavorito = false;
	protected Boolean processarResultadoProcessoSeletivo = false;
	private Boolean processarResultadoProcessoSeletivoFavorito = false;
	private Boolean resultadoProcessoSeletivoRel = false;
	private Boolean resultadoProcessoSeletivoRelFavorito = false;
	protected Boolean comprovanteInscricao = false;
	private Boolean comprovanteInscricaoFavorito = false;
	private Boolean inscricaoCandidato = false;
	private Boolean inscricaoCandidatoFavorito = false;
	private Boolean resultadoCandidato = false;
	private Boolean resultadoCandidatoFavorito = false;
	private Boolean calendarioCandidato = false;
	private Boolean calendarioCandidatoFavorito = false;
	private Boolean boletoCandidato = false;
	private Boolean boletoCandidatoFavorito = false;
	private Boolean cursosCandidato = false;
	private Boolean cursosCandidatoFavorito = false;
	private Boolean comprovanteInscricaoCandidato = false;
	private Boolean comprovanteInscricaoCandidatoFavorito = false;
	protected Boolean controleCorrespondencia = false;
	private Boolean controleCorrespondenciaFavorito = false;
	protected Boolean requerimento = false;
	private Boolean requerimentoFavorito = false;
	private Boolean requerimentoOperacaoLoteFavorito = false;
	protected Boolean tipoRequerimento = false;
	private Boolean tipoRequerimentoFavorito = false;
	private Boolean alterarResponsavelRequerimento = false;
	private Boolean alterarResponsavelRequerimentoFavorito = false;
	// Menu Financeiro
	protected Boolean agencia = false;
	private Boolean agenciaFavorito = false;
	protected Boolean banco = false;
	private Boolean bancoFavorito = false;
	protected Boolean categoriaDespesa = false;
	private Boolean categoriaDespesaFavorito = false;

	private Boolean categoriaDesconto = false;
	private Boolean categoriaDescontoFavorito = false;

	protected Boolean centroReceita = false;
	private Boolean centroReceitaFavorito = false;
	protected Boolean descontoChancela = false;
	private Boolean descontoChancelaFavorito = false;
	protected Boolean contaPagar = false;
	private Boolean contaPagarFavorito = false;
	protected Boolean grupoContaPagar = false;
	private Boolean grupoContaPagarFavorito = false;
	protected Boolean contaCorrente = false;
	private Boolean contaCorrenteFavorito = false;
	protected Boolean parceiro = false;
	private Boolean parceiroFavorito = false;
	protected Boolean contratosDespesas = false;
	private Boolean contratosDespesasFavorito = false;
	protected Boolean contratosReceitas = false;
	private Boolean contratosReceitasFavorito = false;
	protected Boolean convenio = false;
	private Boolean convenioFavorito = false;
	protected Boolean recebimento = false;
	private Boolean recebimentoFavorito = false;
	protected Boolean pagamento = false;
	private Boolean pagamentoFavorito = false;
	protected Boolean contaReceber = false;
	private Boolean contaReceberFavorito = false;
	protected Boolean consultaLogContaReceber = false;
	private Boolean consultaLogContaReceberFavorito = false;
	protected Boolean negociacaoContaReceber = false;
	private Boolean negociacaoContaReceberFavorito = false;
	protected Boolean condicaoNegociacao = false;
	private Boolean condicaoNegociacaoFavorito = false;
	protected Boolean perfilEconomico = false;
	private Boolean perfilEconomicoFavorito = false;
	protected Boolean negociacaoRecebimento = false;
	private Boolean negociacaoRecebimentoFavorito = false;
	protected Boolean fluxoCaixa = false;
	private Boolean fluxoCaixaFavorito = false;
	protected Boolean conciliacaoContaCorrente = false;
	private Boolean conciliacaoContaCorrenteFavorito = false;
	protected Boolean pixContaCorrente = false;
	private Boolean pixContaCorrenteFavorito = false;
	protected Boolean parametrizarOperacoesAutomaticasConciliacaoItem = false;
	private Boolean parametrizarOperacoesAutomaticasConciliacaoItemFavorito = false;
	protected Boolean planoOrcamentario = false;
	private Boolean planoOrcamentarioFavorito = false;
	protected Boolean contaReceberNaoLocalizadaArquivoRetorno = false;
	private Boolean contaReceberNaoLocalizadaArquivoRetornoFavorito = false;
	protected Boolean solicitacaoOrcamentoPlanoOrcamentario = false;
	private Boolean solicitacaoOrcamentoPlanoOrcamentarioFavorito = false;
	protected Boolean geracaoManualParcelas = false;
	private Boolean geracaoManualParcelasFavorito = false;
	protected Boolean geracaoManualParcelasAluno = false;
	private Boolean geracaoManualParcelasAlunoFavorito = false;
	protected Boolean mudancaCarteira = false;
	private Boolean mudancaCarteiraFavorito = false;
	protected Boolean movimentacaoFinanceira = false;
	private Boolean movimentacaoFinanceiraFavorito = false;
	protected Boolean estornarMovimentacaoFinanceira = false;
	private Boolean estornarMovimentacaoFinanceiraFavorito = false;
	protected Boolean alterarDataMovimentacaoFinanceira = false;
	private Boolean alterarDataMovimentacaoFinanceiraFavorito = false;
	protected Boolean devolucaoCheque = false;
	private Boolean devolucaoChequeFavorito = false;
	protected Boolean condicaoRenegociacao = false;
	private Boolean condicaoRenegociacaoFavorito = false;
	protected Boolean cheque = false;
	private Boolean chequeFavorito = false;
	protected Boolean mapaLancamentoFuturo = false;
	private Boolean mapaLancamentoFuturoFavorito = false;
	protected Boolean mapaReposicao = false;
	private Boolean mapaReposicaoFavorito = false;
	private Boolean mapaSuspensaoMatricula = false;
	private Boolean mapaSuspensaoMatriculaFavorito = false;
	private Boolean mapaControleEntregaDocumentoUpload = false;
	private Boolean mapaControleEntregaDocumentoUploadFavorito = false;
	private Boolean assinarDocumentoAlunoEntregue = false;
	private Boolean assinarDocumentoAlunoEntregueFavorito = false;
	private Boolean matriculaSerasa = false;
	private Boolean matriculaSerasaFavorito = false;
	private Boolean matriculaLiberacao = false;
	private Boolean matriculaLiberacaoFavorito = false;
	protected Boolean provisaoCusto = false;
	private Boolean provisaoCustoFavorito = false;
	protected Boolean contaPagarRel = false;
	private Boolean contaPagarRelFavorito = false;
	private Boolean contaPagarPorCategoriaDespesaRel = false;
	private Boolean contaPagarPorCategoriaDespesaRelFavorito = false;
	protected Boolean adiantamentoRel = false;
	private Boolean adiantamentoRelFavorito = false;
	private Boolean contaPagarPorTurmaRel = false;
	private Boolean contaPagarPorTurmaRelFavorito = false;
	protected Boolean modeloBoleto = false;
	private Boolean modeloBoletoFavorito = false;
	protected Boolean controleCobranca = false;
	private Boolean controleCobrancaFavorito = false;
	protected Boolean controleMovimentacaoRemessa = false;
	private Boolean controleMovimentacaoRemessaFavorito = false;
	protected Boolean cartaCobrancaRel = false;
	private Boolean cartaCobrancaRelFavorito = false;
	protected Boolean liberacaoFinanceiroCancelamentoTrancamento = false;
	private Boolean liberacaoFinanceiroCancelamentoTrancamentoFavorito = false;
	private Boolean serasa = false;
	private Boolean serasaFavorito = false;
	private Boolean atualizacaoVencimentos = false;
	private Boolean atualizacaoVencimentosFavorito = false;
	protected Boolean mapaPendenciasControleCobranca = false;
	private Boolean mapaPendenciasControleCobrancaFavorito = false;
	protected Boolean alterarDescontoContaReceber = false;
	private Boolean alterarDescontoContaReceberFavorito = false;
	protected Boolean operadoraCartao = false;
	private Boolean operadoraCartaoFavorito = false;
	protected Boolean mapaPendenciaCartaoCredito = false;
	private Boolean mapaPendenciaCartaoCreditoFavorito = false;
	private Boolean cartaoRespostaRel = false;
	private Boolean cartaoRespostaRelFavorito = false;
	private Boolean tipoDescontoRel = false;
	private Boolean tipoDescontoRelFavorito = false;
	protected Boolean mapaPendenciaMovimentacaoFinanceira = false;
	private Boolean mapaPendenciaMovimentacaoFinanceiraFavorito = false;
	private Boolean controleRemessa = false;
	private Boolean controleRemessaFavorito = false;
	private Boolean controleRemessaContaPagar = false;
	private Boolean controleRemessaContaPagarFavorito = false;
	private Boolean controleCobrancaPagar = false;
	private Boolean controleCobrancaPagarFavorito = false;

	private Boolean processamentoArquivoRetornoParceiro = false;
	private Boolean processamentoArquivoRetornoParceiroFavorito = false;

	private Boolean chancela = false;
	private Boolean chancelaFavorito = false;
	private Boolean descontoInclusaoReposicaoForaPrazo = false;
	private Boolean descontoInclusaoReposicaoForaPrazoFavorito = false;
	private Boolean integracaoFinanceiro = false;
	private Boolean integracaoFinanceiroFavorito = false;
	private Boolean agenteNegativacaoCobrancaContaReceber = false;
	private Boolean agenteNegativacaoCobrancaContaReceberFavorito = false;
	private Boolean registroNegativacaoCobrancaContaReceber = false;
	private Boolean registroNegativacaoCobrancaContaReceberFavorito = false;
	private Boolean mapaNegativacaoCobrancaContaReceber = false;
	private Boolean mapaNegativacaoCobrancaContaReceberFavorito = false;
	private Boolean indiceReajuste = false;
	private Boolean indiceReajusteFavorito = false;
	private Boolean gestaoContasPagar = false;
	private Boolean gestaoContasPagarFavorito = false;
	// Menu Eventos
	protected Boolean membroComunidade = false;
	private Boolean membroComunidadeFavorito = false;
	protected Boolean trabalhoSubmetido = false;
	private Boolean trabalhoSubmetidoFavorito = false;
	// Menu Compras
	protected Boolean categoriaProduto = false;
	private Boolean categoriaProdutoFavorito = false;
	protected Boolean classificaoCustos = false;
	private Boolean classificaoCustosFavorito = false;
	protected Boolean compra = false;
	private Boolean compraFavorito = false;
	protected Boolean cotacao = false;
	private Boolean cotacaoFavorito = false;
	protected Boolean tramiteCotacaoCompra = false;
	private Boolean tramiteCotacaoCompraFavorito = false;
	protected Boolean mapaCotacao = false;
	private Boolean mapaCotacaoFavorito = false;
	protected Boolean estoque = false;
	private Boolean estoqueFavorito = false;
	protected Boolean movimentacaoEstoque = false;
	private Boolean movimentacaoEstoqueFavorito = false;
	protected Boolean formaPagamento = false;
	private Boolean formaPagamentoFavorito = false;
	protected Boolean pgtoServicoAcademico = false;
	private Boolean pgtoServicoAcademicoFavorito = false;
	protected Boolean previsaoCustos = false;
	private Boolean previsaoCustosFavorito = false;
	protected Boolean produtoServico = false;
	private Boolean produtoServicoFavorito = false;
	protected Boolean solicitacaoPgtoServicoAcademico = false;
	private Boolean solicitacaoPgtoServicoAcademicoFavorito = false;
	protected Boolean fornecedor = false;
	private Boolean fornecedorFavorito = false;
	protected Boolean requisicao = false;
	private Boolean requisicaoFavorito = false;
	protected Boolean mapaRequisicao = false;
	private Boolean mapaRequisicaoFavorito = false;
	protected Boolean recebimentoCompra = false;
	private Boolean recebimentoCompraFavorito = false;
	protected Boolean devolucaoCompra = false;
	private Boolean devolucaoCompraFavorito = false;
	protected Boolean entregaRequisicao = false;
	private Boolean entregaRequisicaoFavorito = false;
	protected Boolean condicaoPagamento = false;
	private Boolean condicaoPagamentoFavorito = false;
	// Menu Biblioteca
	protected Boolean classificacaoBibliografica = false;
	private Boolean classificacaoBibliograficaFavorito = false;
	protected Boolean exemplar = false;
	private Boolean exemplarFavorito = false;
	protected Boolean publicacao = false;
	private Boolean publicacaoFavorito = false;
	protected Boolean editora = false;
	private Boolean editoraFavorito = false;
	protected Boolean biblioteca = false;
	private Boolean bibliotecaFavorito = false;
	protected Boolean recursosAudioVisuais = false;
	private Boolean recursosAudioVisuaisFavorito = false;
	protected Boolean registroSaidaAcervo = false;
	private Boolean registroSaidaAcervoFavorito = false;
	protected Boolean autor = false;
	private Boolean autorFavorito = false;
	protected Boolean itemPublicacaoAutor = false;
	private Boolean itemPublicacaoAutorFavorito = false;
	protected Boolean emprestimo = false;
	private Boolean emprestimoFavorito = false;
	protected Boolean catalogo = false;
	private Boolean catalogoFavorito = false;
	protected Boolean titulo = false;
	private Boolean tituloFavorito = false;
	protected Boolean registroEntradaAcervo = false;
	private Boolean registroEntradaAcervoFavorito = false;
	protected Boolean secao = false;
	private Boolean secaoFavorito = false;
	protected Boolean assinaturaPeriodico = false;
	private Boolean assinaturaPeriodicoFavorito = false;
	protected Boolean configuracaoBiblioteca = false;
	private Boolean configuracaoBibliotecaFavorito = false;
	protected Boolean tipoAutoria = false;
	private Boolean tipoAutoriaFavorito = false;
	protected Boolean tipoCatalogo = false;
	private Boolean tipoCatalogoFavorito = false;
	protected Boolean bloqueioBiblioteca = false;
	private Boolean bloqueioBibliotecaFavorito = false;
	protected Boolean buscaCatalogo = false;
	private Boolean buscaCatalogoFavorito = false;
	private Boolean minhaBiblioteca = false;
	private Boolean minhaBibliotecaFavorito = false;
	protected Boolean acervoRel = false;
	private Boolean acervoRelFavorito = false;
	private Boolean etiquetaCodigoBarra = false;
	private Boolean etiquetaCodigoBarraFavorito = false;
	private Boolean etiquetaLivroRel = false;
	private Boolean etiquetaLivroRelFavorito = false;
	protected Boolean exemplaresPorCursoRel = false;
	private Boolean exemplaresPorCursoRelFavorito = false;
	protected Boolean permiteIsentarMultaBiblioteca = false;
	private Boolean permiteIsentarMultaBibliotecaFavorito = false;
	private Boolean bibliotecaLexMagister = false;
	private Boolean bibliotecaLexMagisterFavorito = false;
	// Menu Basico
	protected Boolean artefatoAjuda = false;
	private Boolean artefatoAjudaFavorito = false;
	protected Boolean bairro = false;
	private Boolean bairroFavorito = false;
	protected Boolean endereco = false;
	private Boolean enderecoFavorito = false;
	protected Boolean cidade = false;
	private Boolean cidadeFavorito = false;
	protected Boolean linksUteis = false;
	private Boolean linksUteisFavorito = false;
	protected Boolean estado = false;
	private Boolean estadoFavorito = false;
	protected Boolean pais = false;
	private Boolean paisFavorito = false;
	private Boolean configuracoes = false;
	private Boolean configuracoesFavorito = false;
	private Boolean feriado = false;
	private Boolean feriadoFavorito = false;
	private Boolean alunosAtivosRel = false;
	private Boolean alunosAtivosRelFavorito = false;
	private Boolean dataComemorativa = false;
	private Boolean dataComemorativaFavorito = false;
	private Boolean configuracaoTCC = false;
	private Boolean configuracaoTCCFavorito = false;
	private Boolean retornarTCCFaseAnterior = false;
	private Boolean extenderPrazoExecucaoTCC = false;
	private Boolean lancarNotasTCC = false;
	private Boolean aprovarPlanoTCC = false;
	private Boolean aprovarElaboracaoTCC = false;
	private Boolean definirOrientadorTCC = false;
	private Boolean encaminharTCC = false;
	private Boolean solicitarNovoArquivoTCC = false;
	private Boolean reprovarTCC = false;
	private Boolean registrarArtefatoTCC = false;
	private Boolean membroBancaTCC = false;
	private Boolean revisarPlanoTCC = false;
	private Boolean postarArquivoTCC = false;
	private Boolean calendarioAgrupamentoTcc = false;
	private Boolean calendarioAgrupamentoTccFavorito = false;
	                

	// Menu Arquitetura
	protected Boolean perfilAcesso = false;
	private Boolean perfilAcessoFavorito = false;
	protected Boolean usuario = false;
	private Boolean usuarioFavorito = false;
	protected Boolean log = false;
	private Boolean logFavorito = false;
	protected Boolean mapaControleAtividadesUsuarios = false;
	private Boolean mapaControleAtividadesUsuariosFavorito = false;
	// Menu Administrativo() {
	protected Boolean ouvidoria = false;
	private Boolean ouvidoriaFavorito = false;
	protected Boolean tipagemOuvidoria = false;
	private Boolean tipagemOuvidoriaFavorito = false;
	protected Boolean configuracaoAtendimento = false;
	private Boolean configuracaoAtendimentoFavorito = false;
	protected Boolean unidadeEnsino = false;
	private Boolean unidadeEnsinoFavorito = false;
	protected Boolean agrupamentoUnidadeEnsino = false;
	protected Boolean agrupamentoUnidadeEnsinoFavorito = false;
	protected Boolean mapaEmail = false;
	private Boolean mapaEmailFavorito = false;
	protected Boolean departamento = false;
	private Boolean departamentoFavorito = false;
	protected Boolean cargo = false;
	private Boolean cargoFavorito = false;
	protected Boolean fraseInspiracao = false;
	private Boolean fraseInspiracaoFavorito = false;
	protected Boolean comunicacaoInterna = false;
	private Boolean comunicacaoInternaFavorito = false;
	protected Boolean funcionario = false;
	private Boolean funcionarioFavorito = false;
	protected Boolean campanhaMarketing = false;
	private Boolean campanhaMarketingFavorito = false;
	protected Boolean campanha = false;
	private Boolean campanhaFavorito = false;
	protected Boolean tipoMidiaCaptacao = false;
	private Boolean tipoMidiaCaptacaoFavorito = false;
	protected Boolean visao = false;
	private Boolean visaoFavorito = false;
	protected Boolean grupoDestinatarios = false;
	private Boolean grupoDestinatariosFavorito = false;
	private Boolean simularAcessoUsuario = false;
	private Boolean unificacaoCadastroUsuario = false;
	private Boolean unificacaoCadastroUsuarioFavorito = false;
	// Menu Academico
	protected Boolean aluno = false;
	private Boolean alunoFavorito = false;
	protected Boolean coordenador = false;
	private Boolean coordenadorFavorito = false;
	protected Boolean ataProva = false;
	private Boolean ataProvaFavorito = false;
	protected Boolean arquivoAssinado = false;
	private Boolean arquivoAssinadoFavorito = false;
	protected Boolean professor = false;
	private Boolean professorFavorito = false;
	private Boolean mapaAtualizacaoMatriculaFormada = false;
	private Boolean mapaAtualizacaoMatriculaFormadaFavorito = false;
	protected Boolean curso = false;
	private Boolean cursoFavorito = false;
	protected Boolean titulacaoProfessorCurso = false;
	private Boolean titulacaoProfessorCursoFavorito = false;
	protected Boolean disciplina = false;
	protected Boolean disciplinaFavorito = false;
	protected Boolean navegarAbaDocumentacao = false;
	private Boolean navegarAbaDocumentacaoFavorito = false;
	protected Boolean navegarAbaDisciplinas = false;
	private Boolean navegarAbaDisciplinasFavorito = false;
	protected Boolean navegarAbaPlanoFinanceiroAluno = false;
	private Boolean navegarAbaPlanoFinanceiroAlunoFavorito = false;
	protected Boolean navegarAbaPendenciaFinanceira = false;
	private Boolean navegarAbaPendenciaFinanceiraFavorito = false;
	private Boolean enade = false;
	private Boolean enadeFavorito = false;
	protected Boolean turno = false;
	protected Boolean turma = false;
	protected Boolean integracaoMestreGR = false;
	protected Boolean integracaoMestreGRFavorito = false;
	protected Boolean vagaTurma = false;
	protected Boolean tipoCategoria = false;
	protected Boolean categoriaTurma = false;
	private Boolean tipoCategoriaFavorito = false;
	private Boolean categoriaTurmaFavorito = false;
	private Boolean turnoFavorito = false;
	private Boolean turmaFavorito = false;
	private Boolean vagaTurmaFavorito = false;
	protected Boolean upload = false;
	private Boolean uploadArquivosComuns = false;
	protected Boolean uploadFavorito = false;
	private Boolean uploadArquivosComunsFavorito = false;
	protected Boolean programacaoAula = false;
	private Boolean programacaoAulaFavorito = false;
	protected Boolean RegistroAula = false;
	private Boolean RegistroAulaFavorito = false;
	protected Boolean professorMinistrouAulaTurma = false;
	private Boolean professorMinistrouAulaTurmaFavorito = false;
	private Boolean observacaoComplementar = false;
	private Boolean observacaoComplementarFavorito = false;
	protected Boolean ControleLivroRegistroDiploma = false;
	private Boolean ControleLivroRegistroDiplomaFavorito = false;
	protected Boolean frequenciaAula = false;
	private Boolean frequenciaAulaFavorito = false;
	protected Boolean processoMatricula = false;
	private Boolean processoMatriculaFavorito = false;
	protected Boolean mudarDataFimPeriodoLetivo = false;
	private Boolean mudarDataFimPeriodoLetivoFavorito = false;
	protected Boolean matricula = false;
	private Boolean matriculaFavorito = false;
	private Boolean logmatricula = false;
	private Boolean logmatriculaFavorito = false;
	protected Boolean renovarMatriculaPorTurma = false;
	private Boolean renovarMatriculaPorTurmaFavorito = false;
	protected Boolean inclusaoForaPrazo = false;
	private Boolean inclusaoForaPrazoFavorito = false;
	protected Boolean exclusaoForaPrazo = false;
	private Boolean exclusaoForaPrazoFavorito = false;
	protected Boolean alteracoesCadastraisMatricula = false;
	private Boolean alteracoesCadastraisMatriculaFavorito = false;
	protected Boolean matriculaEnade = false;
	private Boolean matriculaEnadeFavorito = false;
	protected Boolean historico = false;
	private Boolean historicoFavorito = false;
	protected Boolean trancamento = false;
	private Boolean trancamentoFavorito = false;
	protected Boolean logTurma = false;
	private Boolean logTurmaFavorito = false;
	protected Boolean cancelamento = false;
	private Boolean cancelamentoFavorito = false;
	protected Boolean transferenciaEntrada = false;
	private Boolean transferenciaEntradaFavorito = false;
	protected Boolean aproveitamentoDisciplina = false;
	private Boolean aproveitamentoDisciplinaFavorito = false;
	protected Boolean artefatoEntregaAluno = false;
	private Boolean artefatoEntregaAlunoFavorito = false;
	protected Boolean registroEntregaArtefatoAluno = false;
	private Boolean registroEntregaArtefatoAlunoFavorito = false;
	protected Boolean alunoArtefatosEntregue = false;
	protected Boolean permitirCriarScriptArtefatoEntregaAluno = false;
	protected Boolean transferenciaInterna = false;
	private Boolean transferenciaInternaFavorito = false;
	protected Boolean transferenciaSaida = false;
	private Boolean transferenciaSaidaFavorito = false;
	protected Boolean planoDesconto = false;
	private Boolean planoDescontoFavorito = false;
	protected Boolean planoFinanceiroCurso = false;
	private Boolean planoFinanceiroCursoFavorito = false;
	private Boolean planoFinanceiroReposicao = false;
	private Boolean planoFinanceiroReposicaoFavorito = false;
	protected Boolean textoPadrao = false;
	private Boolean textoPadraoFavorito = false;
	protected Boolean centroResultado = false;
	private Boolean centroResultadoFavorito = false;
	protected Boolean planoFinanceiroAluno = false;
	private Boolean planoFinanceiroAlunoFavorito = false;
	private Boolean controleGeracaoParcelaTurma = false;
	private Boolean controleGeracaoParcelaTurmaFavorito = false;
	protected Boolean descontoProgressivo = false;
	private Boolean descontoProgressivoFavorito = false;
	protected Boolean areaConhecimento = false;
	protected Boolean areaConhecimentoFavorito = false;
	protected Boolean portadorDiploma = false;
	private Boolean portadorDiplomaFavorito = false;
	protected Boolean periodoLetivoAtivoUnidadeEnsinoCurso = false;
	private Boolean periodoLetivoAtivoUnidadeEnsinoCursoFavorito = false;
	protected Boolean abonoFalta = false;
	private Boolean abonoFaltaFavorito = false;
	protected Boolean tipoDocumento = false;
	private Boolean tipoDocumentoFavorito = false;
	protected Boolean entregaDocumento = false;
	protected Boolean estagio = false;
	protected Boolean estagioFavorito = false;
	protected Boolean estagioObrigatorio = false;
	protected Boolean estagioObrigatorioFavorito = false;
	protected Boolean configuracaoEstagioObrigatorio = false;
	protected Boolean configuracaoEstagioObrigatorioFavorito = false;
	protected Boolean motivosPadroesEstagio = false;
	protected Boolean motivosPadroesEstagioFavorito = false;
	protected Boolean tipoConcedente = false;
	protected Boolean tipoConcedenteFavorito = false;
	protected Boolean concedente = false;
	protected Boolean concedenteFavorito = false;
	protected Boolean grupoPessoa = false;
	protected Boolean grupoPessoaFavorito = false;
	protected Boolean camposEstagio = false;
	protected Boolean camposEstagioFavorito = false;
	protected Boolean formularioEstagio = false;
	protected Boolean formularioEstagioFavorito = false;
	protected Boolean modeloTermoEstagio = false;
	protected Boolean modeloTermoEstagioFavorito = false;
	private Boolean entregaDocumentoFavorito = false;
	protected Boolean permiteLeituraArquivoScanner = false;
	private Boolean permiteLeituraArquivoScannerFavorito = false;
	protected Boolean permiteLeituraArquivoScannerMatricula = false;
	private Boolean permiteLeituraArquivoScannerMatriculaFavorito = false;
	protected Boolean confirmacaoPreMatricula = false;
	private Boolean confirmacaoPreMatriculaFavorito = false;
	protected Boolean colacaoGrau = false;
	private Boolean colacaoGrauFavorito = false;
	protected Boolean expedicaoDiploma = false;
	private Boolean expedicaoDiplomaFavorito = false;
	protected Boolean programacaoFormatura = false;
	private Boolean programacaoFormaturaFavorito = false;
	private Boolean uploadProfessorRel = false;
	private Boolean uploadProfessorRelFavorito = false;
	protected Boolean registroPresencaColacaoGrau = false;
	private Boolean registroPresencaColacaoGrauFavorito = false;
	protected Boolean subMenuSecretaria = false;
	protected Boolean transferenciaMatrizCurricular = false;
	private Boolean transferenciaMatrizCurricularFavorito = false;
	protected Boolean transferenciaTurma = false;
	private Boolean transferenciaTurmaFavorito = false;
	private Boolean atividadeComplementar = false;
	private Boolean atividadeComplementarFavorito = false;
	protected Boolean lancamentoNota = false;
	protected Boolean liberacaoTurmaEADIPOG = false;
	private Boolean liberacaoTurmaEADIPOGFavorito = false;
	private Boolean lancamentoNotaFavorito = false;
	private Boolean textoPadraoDeclaracao = false;
	private Boolean textoPadraoDeclaracaoFavorito = false;
	protected Boolean reativacaoMatricula = false;
	private Boolean reativacaoMatriculaFavorito = false;
	protected Boolean mapaNotaAlunoPorTurma = false;
	private Boolean mapaNotaAlunoPorTurmaFavorito = false;
	protected Boolean mapaNotasDisciplinaAlunos = false;
	private Boolean mapaNotasDisciplinaAlunosFavorito = false;
	protected Boolean inclusaoExclusaoDisciplina = false;
	private Boolean inclusaoExclusaoDisciplinaFavorito = false;
	protected Boolean transferenciaTurno = false;
	private Boolean transferenciaTurnoFavorito = false;
	protected Boolean identificacaoEstudantil = false;
	private Boolean identificacaoEstudantilFavorito = false;
	protected Boolean informacaoProfessorRel = false;
	private Boolean informacaoProfessorRelFavorito = false;
	private Boolean configuracaoAcademicaHistorico = false;
	private Boolean configuracaoAcademicaHistoricoFavorito = false;
	private Boolean consultorMatricula = false;
	private Boolean consultorMatriculaFavorito = false;
	private Boolean planoDescontoInclusaoDisciplina = false;
	private Boolean planoDescontoInclusaoDisciplinaFavorito = false;
	private Boolean impressaoContrato = false;
	private Boolean impressaoContratoFavorito = false;
	private Boolean impressaoDeclaracao = false;
	private Boolean impressaoDeclaracaoFavorito = false;
	private Boolean motivoCancelamentoTrancamento = false;
	private Boolean motivoCancelamentoTrancamentoFavorito = false;
	protected Boolean registroAulaNota = false;
	private Boolean registroAulaNotaFavorito = false;
	protected Boolean permitirProfessorExcluirArquivoInstituicao = false;
	private Boolean permitirProfessorExcluirArquivoInstituicaoFavorito = false;
	protected Boolean exclusaoMatricula = false;
	private Boolean exclusaoMatriculaFavorito = false;
	protected Boolean transferenciaUnidade = false;
	private Boolean transferenciaUnidadeFavorito = false;
	private Boolean alterarUserNameSenhaAlunos = false;
	private Boolean alterarUserNameSenhaAlunosFavorito = false;
	private Boolean mapaNotaAlunoPorTurmaRel = false;
	private Boolean mapaNotaAlunoPorTurmaRelFavorito = false;
	private Boolean mapaNotasDisciplinaAlunosRel = false;
	private Boolean mapaNotasDisciplinaAlunosRelFavorito = false;
	private Boolean mapaDeTurmasEncerradasRel = false;
	private Boolean mapaDeTurmasEncerradasRelFavorito = false;
	private Boolean solicitarAberturaTurma = false;
	private Boolean solicitarAberturaTurmaFavorito = false;
	private Boolean autorizarSolicitacaoAberturaTurma = false;
	private Boolean autorizarSolicitacaoAberturaTurmaFavorito = false;
	private Boolean naoAutorizarSolicitacaoAberturaTurma = false;
	private Boolean naoAutorizarSolicitacaoAberturaTurmaFavorito = false;
	private Boolean revisarSolicitacaoAberturaTurma = false;
	private Boolean revisarSolicitacaoAberturaTurmaFavorito = false;
	private Boolean finalizarSolicitacaoAberturaTurma = false;
	private Boolean finalizarSolicitacaoAberturaTurmaFavorito = false;
	private Boolean permiteRevisarSolicitacaoAberturaTurma = false;
	private Boolean permiteRevisarSolicitacaoAberturaTurmaFavorito = false;
	private Boolean chamadaCandidatoAprovadoRel = false;
	private Boolean chamadaCandidatoAprovadoRelFavorito = false;
	private Boolean localAula = false;
	private Boolean localAulaFavorito = false;
	private Boolean mapaLocalAulaTurma = false;
	private Boolean mapaLocalAulaTurmaFavorito = false;
	private Boolean mapaAlunoAptoFormar = false;
	private Boolean mapaAlunoAptoFormarFavorito = false;
	private Boolean trabalhoConclusaoCurso = false;
	private Boolean trabalhoConclusaoCursoFavorito = false;
	private Boolean coordenadorTCC = false;
	private Boolean coordenadorTCCFavorito = false;
	private Boolean permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica;
	protected Boolean eixoCurso = false;
	private Boolean eixoCursoFavorito = false;
	private Boolean configuracaoDiplomaDigital;
	private Boolean configuracaoDiplomaDigitalFavorito;
	private Boolean gestaoXmlGradeCurricular;
	private Boolean gestaoXmlGradeCurricularFavorito;
	protected Boolean configuracaoEstagio = false;	
	protected Boolean configuracaoEstagioFavorito = false;
	protected Boolean registrarNotaAutomaticamente = false;
	private Boolean motivoIndeferimentoDocumentoAluno = false;
	private Boolean motivoIndeferimentoDocumentoAlunoFavorito = false;
	protected Boolean formularioRelatorioFacilitador = false;
	protected Boolean formularioRelatorioFacilitadorFavorito = false;
	protected Boolean camposFormularioRelatorioFacilitador = false;
	protected Boolean camposFormularioRelatorioFacilitadorFavorito = false;
	protected Boolean calendarioRelatorioFinalFacilitador = false;
	protected Boolean calendarioRelatorioFinalFacilitadorFavorito = false;
	protected Boolean mapaRelatorioFacilitador = false;
	protected Boolean mapaRelatorioFacilitadorFavorito = false;
	// Menu avaliacao Institucional
	protected Boolean avaliacaoInstitucional = false;
	private Boolean avaliacaoInstitucionalFavorito = false;
	protected Boolean questionarioAvaliacaoInstitucional = false;
	private Boolean questionarioAvaliacaoInstitucionalFavorito = false;
	protected Boolean perguntaAvaliacaoInstitucional = false;
	private Boolean perguntaAvaliacaoInstitucionalFavorito = false;
	protected Boolean questionarioProcessoSeletivo = false;
	private Boolean questionarioProcessoSeletivoFavorito = false;
	protected Boolean perguntaProcessoSeletivo = false;
	private Boolean perguntaProcessoSeletivoFavorito = false;
	protected Boolean avaliacaoInstitucionalPresencialResposta = false;
	private Boolean avaliacaoInstitucionalPresencialRespostaFavorito = false;
	// Menu Relatorios
	protected Boolean unidadeEnsinoRel = false;
	private Boolean unidadeEnsinoRelFavorito = false;
	protected Boolean campanhaMarketingRel = false;
	private Boolean campanhaMarketingRelFavorito = false;
	protected Boolean disciplinaRel = false;
	private Boolean disciplinaRelFavorito = false;
	protected Boolean processoMatriculaRel = false;
	private Boolean processoMatriculaRelFavorito = false;
	protected Boolean procSeletivoRel = false;
	private Boolean procSeletivoRelFavorito = false;
	protected Boolean contaReceberRel = false;
	private Boolean contaReceberRelFavorito = false;
	protected Boolean declaracaoImpostoRendaRel = false;
	private Boolean declaracaoImpostoRendaRelFavorito = false;
	protected Boolean boletosRel = false;
	private Boolean boletosRelFavorito = false;
	protected Boolean termoReconhecimentoDividaRel = false;
	private Boolean termoReconhecimentoDividaRelFavorito = false;
	private Boolean renegociacaoRel = false;
	private Boolean renegociacaoRelFavorito = false;
	protected Boolean fluxoCaixaRel = false;
	private Boolean fluxoCaixaRelFavorito = false;
	protected Boolean faturamentoRecebimentoRel = false;
	private Boolean faturamentoRecebimentoRelFavorito = false;
	protected Boolean recebimentoRel = false;
	private Boolean recebimentoRelFavorito = false;
	protected Boolean contaPagarResumidaPorDataRel = false;
	private Boolean contaPagarResumidaPorDataRelFavorito = false;
	protected Boolean contaPagarResumidaPorFornecedorRel = false;
	private Boolean contaPagarResumidaPorFornecedorRelFavorito = false;
	protected Boolean pagamentoRel = false;
	private Boolean pagamentoRelFavorito = false;
	protected Boolean pagamentoResumidoRel = false;
	private Boolean pagamentoResumidoRelFavorito = false;
	protected Boolean historicoTurmaRel = false;
	private Boolean historicoTurmaRelFavorito = false;
	protected Boolean historicoAlunoRel = false;
	private Boolean historicoAlunoRelFavorito = false;
	protected Boolean gradeCurricularAlunoRel = false;
	private Boolean gradeCurricularAlunoRelFavorito = false;
	protected Boolean diarioRel = false;
	private Boolean diarioRelFavorito = false;
	protected Boolean espelhoRel = false;
	private Boolean espelhoRelFavorito = false;
	protected Boolean perfilSocioEconomicoRel = false;
	private Boolean perfilSocioEconomicoRelFavorito = false;
	protected Boolean procSeletivoInscricoesRel = false;
	private Boolean procSeletivoInscricoesRelFavorito = false;
	protected Boolean avaliacaoInstitucionalRel = false;
	private Boolean avaliacaoInstitucionalRelFavorito = false;
	protected Boolean declaracaoSetranspRel = false;
	private Boolean declaracaoSetranspRelFavorito = false;
	private Boolean declaracaoPasseEstudantilRel = false;
	private Boolean declaracaoPasseEstudantilRelFavorito = false;
	protected Boolean comunicadoDebitoDocumentosAlunoRel = false;
	private Boolean comunicadoDebitoDocumentosAlunoRelFavorito = false;
	protected Boolean declaracaoFrequenciaRel = false;
	private Boolean declaracaoFrequenciaRelFavorito = false;
	protected Boolean declaracaoAbandonoCursoRel = false;
	private Boolean declaracaoAbandonoCursoRelFavorito = false;
	protected Boolean declaracaoConclusaoCursoRel = false;
	private Boolean declaracaoConclusaoCursoRelFavorito = false;
	protected Boolean declaracaoCancelamentoMatriculaRel = false;
	private Boolean declaracaoCancelamentoMatriculaRelFavorito = false;
	protected Boolean declaracaoAprovacaoVestRel = false;
	private Boolean declaracaoAprovacaoVestRelFavorito = false;
	protected Boolean cartaAlunoRel = false;
	private Boolean cartaAlunoRelFavorito = false;
	protected Boolean alunosCursoRel = false;
	private Boolean alunosCursoRelFavorito = false;
	protected Boolean alunosPorUnidadeCursoTurmaRel = false;
	private Boolean alunosPorUnidadeCursoTurmaRelFavorito = false;
	protected Boolean alunosPorDisciplinasRel = false;
	private Boolean alunosPorDisciplinasRelFavorito = false;
	protected Boolean reposicaoRel = false;
	private Boolean reposicaoRelFavorito = false;
	protected Boolean professorRel = false;
	private Boolean professorRelFavorito = false;
	protected Boolean alunosMatriculadosGeralRel = false;
	private Boolean alunosMatriculadosGeralRelFavorito = false;
	protected Boolean alunosNaoRenovaramRel = false;
	private Boolean alunosNaoRenovaramRelFavorito = false;
	protected Boolean processoSeletivoAprovadoReprovadoRel = false;
	private Boolean processoSeletivoAprovadoReprovadoRelFavorito = false;
	protected Boolean declaracaoTransferenciaRel = false;
	private Boolean declaracaoTransferenciaRelFavorito = false;
	protected Boolean debitoDocumentosPosRel = false;
	private Boolean debitoDocumentosPosRelFavorito = false;
	protected Boolean censo = false;
	private Boolean censoFavorito = false;
	protected Boolean recebimentoPorTurmaRel = false;
	private Boolean recebimentoPorTurmaRelFavorito = false;
	protected Boolean recebimentoPorUnidadeCursoTurmaRel = false;
	private Boolean recebimentoPorUnidadeCursoTurmaRelFavorito = false;
	protected Boolean prestacaoContaRel = false;
	private Boolean prestacaoContaRelFavorito = false;
	protected Boolean controleDocumentacaoAlunoRel = false;
	private Boolean controleDocumentacaoAlunoRelFavorito = false;
	protected Boolean contaReceberAlunosRel = false;
	private Boolean contaReceberAlunosRelFavorito = false;
	protected Boolean situacaoFinanceiraAlunoRel = false;
	private Boolean situacaoFinanceiraAlunoRelFavorito = false;
	protected Boolean mediaDescontoAlunoRel = false;
	private Boolean mediaDescontoAlunoRelFavorito = false;
	protected Boolean mediaDescontoTurmaRel = false;
	private Boolean mediaDescontoTurmaRelFavorito = false;
	protected Boolean balanceteRel = false;
	private Boolean balanceteRelFavorito = false;
	protected Boolean chequesRel = false;
	private Boolean chequesRelFavorito = false;
	protected Boolean registroArquivo = false;
	private Boolean registroArquivoFavorito = false;
	protected Boolean setransp = false;
	private Boolean setranspFavorito = false;
	protected Boolean inadimplenciaRel = false;
	private Boolean inadimplenciaRelFavorito = false;
	protected Boolean cartaoCreditoDebitoRel = false;
	private Boolean cartaoCreditoDebitoRelFavorito = false;
	protected Boolean mapaFinanceiroAlunoRel = false;
	private Boolean mapaFinanceiroAlunoRelFavorito = false;
	protected Boolean boletimAcademicoRel = false;
	private Boolean boletimAcademicoRelFavorito = false;
	protected Boolean quadroMatriculaRel = false;
	private Boolean quadroMatriculaRelFavorito = false;
	protected Boolean ocorrenciasAlunosRel = false;
	private Boolean ocorrenciasAlunosRelFavorito = false;
	protected Boolean estatisticaMatriculaRel = false;
	private Boolean estatisticaMatriculaRelFavorito = false;
	protected Boolean quadroAlunosAtivosInativosRel = false;
	private Boolean quadroAlunosAtivosInativosRelFavorito = false;
	protected Boolean senhaAlunoProfessorRel = false;
	private Boolean senhaAlunoProfessorRelFavorito = false;
	protected Boolean alunoNaoCursouDisciplinaRel = false;
	private Boolean alunoNaoCursouDisciplinaRelFavorito = false;
	protected Boolean certificadoCursoExtensaoRel = false;
	private Boolean certificadoCursoExtensaoRelFavorito = false;
	protected Boolean disciplinasGradeRel = false;
	private Boolean disciplinasGradeRelFavorito = false;
	protected Boolean termoCompromissoDocumentacaoPendenteRel = false;
	private Boolean termoCompromissoDocumentacaoPendenteRelFavorito = false;
	protected Boolean entregaBoletosRel = false;
	private Boolean entregaBoletosRelFavorito = false;
	protected Boolean entregaBoletosAlunoRel = false;
	private Boolean entregaBoletosAlunoRelFavorito = false;
	protected Boolean alunoDescontoDesempenhoRel = false;
	private Boolean alunoDescontoDesempenhoRelFavorito = false;
	protected Boolean relacaoEnderecoAlunoRel = false;
	private Boolean relacaoEnderecoAlunoRelFavorito = false;
	private Boolean alunoBaixaFrequenciaRel = false;
	private Boolean alunoBaixaFrequenciaRelFavorito = false;
	protected Boolean livroRegistroRel = false;
	private Boolean livroRegistroRelFavorito = false;
	protected Boolean fichaAtualizacaoCadastralRel = false;
	private Boolean fichaAtualizacaoCadastralRelFavorito = false;
	protected Boolean alunosMatriculadosPorProcessoSeletivoRel = false;
	private Boolean alunosMatriculadosPorProcessoSeletivoRelFavorito = false;
	protected Boolean alunosProcessoSeletivoRel = false;
	private Boolean alunosProcessoSeletivoRelFavorito = false;
	protected Boolean meritoAcademicoRel = false;
	private Boolean meritoAcademicoRelFavorito = false;
	protected Boolean possiveisFormandosRel = false;
	private Boolean possiveisFormandosRelFavorito = false;
	protected Boolean controleVagaRel = false;
	private Boolean controleVagaRelFavorito = false;
	protected Boolean perfilTurmaRel = false;
	private Boolean perfilTurmaRelFavorito = false;
	private Boolean emailTurmaRel = false;
	private Boolean emailTurmaRelFavorito = false;
	protected Boolean alunosProUniRel = false;
	private Boolean alunosProUniRelFavorito = false;
	protected Boolean condicoesPagamentoRel = false;
	private Boolean condicoesPagamentoRelFavorito = false;
	protected Boolean listagemDescontosAlunosRel = false;
	private Boolean listagemDescontosAlunosRelFavorito = false;
	protected Boolean AlunosReprovadosRel = false;
	private Boolean AlunosReprovadosRelFavorito = false;
	protected Boolean MapaSituacaoAlunoRel = false;
	private Boolean MapaSituacaoAlunoRelFavorito = false;
	protected Boolean exemplaresRel = false;
	private Boolean exemplaresRelFavorito = false;
	protected Boolean situacaoExemplaresRel = false;
	private Boolean situacaoExemplaresRelFavorito = false;
	protected Boolean horarioDaTurmaRel = false;
	private Boolean horarioDaTurmaRelFavorito = false;
	protected Boolean horarioAlunoRel = false;
	private Boolean horarioAlunoRelFavorito = false;
	protected Boolean faltasAlunosRel = false;
	protected Boolean registroAulaLancadaNaoLancadaRel = false;
	private Boolean faltasAlunosRelFavorito = false;
	private Boolean registroAulaLancadaNaoLancadaRelFavorito = false;
	protected Boolean emprestimoRel = false;
	private Boolean emprestimoRelFavorito = false;
	private Boolean agendaProfessorRel = false;
	private Boolean agendaProfessorRelFavorito = false;
	protected Boolean aniversariantesDoMesRel = false;
	private Boolean aniversariantesDoMesRelFavorito = false;
	protected Boolean cronogramaDeAulasRel = false;
	private Boolean cronogramaDeAulasRelFavorito = false;
	private Boolean previsaoFaturamentoRel = false;
	private Boolean previsaoFaturamentoRelFavorito = false;
	private Boolean quadroComissaoConsultoresRel = false;
	private Boolean quadroComissaoConsultoresRelFavorito = false;
	private Boolean posVendaRel = false;
	private Boolean posVendaRelFavorito = false;
	private Boolean envelopeRel = false;
	private Boolean envelopeRelFavorito = false;
	private Boolean envelopeRequerimentoRel = false;
	private Boolean envelopeRequerimentoRelFavorito = false;
	private Boolean aberturaTurmaRel = false;
	private Boolean aberturaTurmaRelFavorito = false;
	private Boolean mapaAberturaTurma = false;
	private Boolean mapaAberturaTurmaFavorito = false;
	private Boolean extratoContaPagarRel = false;
	private Boolean extratoContaPagarRelFavorito = false;
	private Boolean requerimentoRel = false;
	private Boolean requerimentoRelFavorito = false;
	private Boolean requerimentoTCCRel = false;
	private Boolean requerimentoTCCRelFavorito = false;
	private Boolean candidatosParaVagaRel = false;
	private Boolean candidatosParaVagaRelFavorito = false;
	private Boolean empresaVagaBancoTalentoRel = false;
	private Boolean empresaVagaBancoTalentoRelFavorito = false;
	private Boolean empresaPorVagasRel = false;
	private Boolean empresaPorVagasRelFavorito = false;
	private Boolean empresasRel = false;
	private Boolean empresasRelFavorito = false;
	private Boolean alunosCandidatadosVagaRel = false;
	private Boolean alunosCandidatadosVagaRelFavorito = false;
	private Boolean etiquetaProvaRel = false;
	private Boolean etiquetaProvaRelFavorito = false;
	private Boolean reciboComissoesRel = false;
	private Boolean reciboComissoesRelFavorito = false;
	private Boolean impostosRetidosContaReceberRel = false;
	private Boolean impostosRetidosContaReceberRelFavorito = false;

	// Banco curriculo
	private Boolean areaProfissional = false;
	private Boolean areaProfissionalFavorito = false;
	private Boolean vagas = false;
	private Boolean vagasFavorito = false;
	private Boolean painelAluno = false;
	private Boolean painelAlunoFavorito = false;
	private Boolean link = false;
	private Boolean linkFavorito = false;
	private Boolean textoPadraoBancoCurriculum = false;
	private Boolean textoPadraoBancoCurriculumFavorito = false;
	private Boolean painelGestorBancoCurriculo = false;
	private Boolean painelGestorBancoCurriculoFavorito = false;
	private Boolean contatoAniversariante = false;
	private Boolean contatoAniversarianteFavorito = false;
	// Academico - Visa Professor
	private Boolean configuracoesVisaoProfessor = false;
	private Boolean configuracoesVisaoProfessorFavorito = false;
	private Boolean configuracoesLiberarAlteracaoSenhaProfessor = false;
	private Boolean configuracoesLiberarAlteracaoSenhaProfessorFavorito = false;
	private Boolean configuracoesLiberarAlteracaoFotoProfessor = false;
	private Boolean configuracoesLiberarAlteracaoFotoProfessorFavorito = false;
	private Boolean configuracoesLiberarAlteracaoCorTelaProfessor = false;
	private Boolean configuracoesLiberarAlteracaoCorTelaProfessorFavorito = false;
	// Academico - Visao do Aluno
	protected Boolean minhasNotas = false;
	private Boolean minhasNotasFavorito = false;
	protected Boolean meusAmigos = false;
	private Boolean meusAmigosFavorito = false;
	protected Boolean meusHorarios = false;
	private Boolean meusHorariosFavorito = false;
	protected Boolean meusProfessores = false;
	private Boolean meusProfessoresFavorito = false;
	private Boolean minhasFaltas = false;
	private Boolean minhasFaltasFavorito = false;
	protected Boolean downloadArquivo = false;
	private Boolean downloadArquivoFavorito = false;
	private Boolean documentosDigitais = false;
	private Boolean documentosDigitaisFavorito = false;
	protected Boolean planoEstudo = false;
	private Boolean planoEstudoFavorito = false;
	protected Boolean minhasContasPagar = false;
	private Boolean minhasContasPagarFavorito = false;
	protected Boolean meusContratos = false;
	private Boolean meusContratosFavorito = false;
	private Boolean linkVisoesMoodle = false;
	private Boolean configuracoesVisao = false;
	private Boolean configuracoesVisaoFavorito = false;
	private Boolean configuracoesAlteracaoSenha = false;
	private Boolean configuracoesAlteracaoSenhaFavorito = false;
	private Boolean configuracoesAlteracaoFoto = false;
	private Boolean configuracoesAlteracaoFotoFavorito = false;
	private Boolean configuracoesAlteracaoCorTela = false;
	private Boolean configuracoesAlteracaoCorTelaFavorito = false;
	// Menu - Contabil'
	protected Boolean planoConta = false;
	private Boolean planoContaFavorito = false;
	protected Boolean configuracaoContabil = false;
	private Boolean configuracaoContabilFavorito = false;
	protected Boolean layoutIntegracao = false;
	private Boolean layoutIntegracaoFavorito = false;
	protected Boolean integracaoContabil = false;
	private Boolean integracaoContabilFavorito = false;
	private Boolean dRE = false;
	private Boolean dREFavorito = false;
	protected Boolean tipoEventoContabil = false;
	private Boolean tipoEventoContabilFavorito = false;
	protected Boolean historicoContabil = false;
	private Boolean historicoContabilFavorito = false;
	protected Boolean contabil = false;
	private Boolean contabilFavorito = false;
	protected Boolean calculoMes = false;
	private Boolean calculoMesFavorito = false;
	protected Boolean fechamentoMes = false;
	private Boolean fechamentoMesFavorito = false;
	protected Boolean painelGestorRequerimentosAcademico = false;
	private Boolean painelGestorRequerimentosAcademicoFavorito = false;
	protected Boolean painelGestorComunicacaoInternaAcademico = false;
	protected Boolean painelGestorConsultorPorMatricula = false;
	private Boolean painelGestorComunicacaoInternaAcademicoFavorito = false;
	protected Boolean painelGestorRequerimentosFinanceiro = false;
	private Boolean painelGestorRequerimentosFinanceiroFavorito = false;
	protected Boolean painelGestorComunicacaoInternaFinanceiro = false;
	private Boolean painelGestorComunicacaoInternaFinanceiroFavorito = false;
	private Boolean controleConcorrenciaHorarioTurma = false;
	private Boolean controleConcorrenciaHorarioTurmaFavorito = false;
	// Menu - CRM
	private Boolean meta = false;
	private Boolean segmentacaoProspect = false;
	private Boolean segmentacaoProspectFavorito = false;
	private Boolean metaFavorito = false;
	private Boolean prospects = false;
	private Boolean prospectsFavorito = false;
	private Boolean buscaProspect = false;
	private Boolean buscaProspectFavorito = false;
	private Boolean novoProspect = false;
	private Boolean novoProspectFavorito = false;
	private Boolean workflow = false;
	private Boolean workflowFavorito = false;
	private Boolean voltarEtapaAnterior = false;
	private Boolean voltarEtapaAnteriorFavorito = false;
	private Boolean situacaoProspectPipeline = false;
	private Boolean situacaoProspectPipelineFavorito = false;
	private Boolean buscaCandidatoVaga = false;
	private Boolean buscaCandidatoVagaFavorito = false;
	private Boolean registroEntrada = false;
	private Boolean registroEntradaFavorito = false;
	private Boolean buscaVagas = false;
	private Boolean buscaVagasFavorito = false;
	private Boolean agendaPessoa = false;
	private Boolean agendaPessoaFavorito = false;
	private Boolean visaoAdministradorAgendaPessoa = false;
	private Boolean visaoAdministradorAgendaPessoaFavorito = false;
	private Boolean cronogramaAula = false;
	private Boolean cronogramaAulaFavorito = false;
	private Boolean followUpProspect = false;
	private Boolean followUpProspectFavorito = false;
	private Boolean alterarObservacaoInteracaoFollowUp = false;
	private Boolean alterarObservacaoInteracaoFollowUpFavorito = false;
	private Boolean gravarInteracaoFollowUp = false;
	private Boolean gravarInteracaoFollowUpFavorito = false;
	private Boolean interacaoWorkflow = false;
	private Boolean interacaoWorkflowFavorito = false;
	private Boolean motivoInsucesso = false;
	private Boolean motivoInsucessoFavorito = false;
	private Boolean comissionamentoTurma = false;
	private Boolean comissionamentoTurmaFavorito = false;
	private Boolean ranking = false;
	private Boolean rankingFavorito = false;
	private Boolean titulacaoCurso = false;
	private Boolean titulacaoCursoFavorito = false;
	private Boolean configuracaoRanking = false;
	private Boolean configuracaoRankingFavorito = false;
	private Boolean percentualConfiguracaoRanking = false;
	private Boolean percentualConfiguracaoRankingFavorito = false;
	private Boolean extratoComissaoRel = false;
	private Boolean extratoComissaoRelFavorito = false;
	private Boolean produtividadeConsultorRel = false;
	private Boolean produtividadeConsultorRelFavorito = false;
	private Boolean consultorPorMatriculaRel = false;
	private Boolean consultorPorMatriculaRelFavorito = false;
	private Boolean painelGestorSupervisaoVenda = false;
	private Boolean painelGestorSupervisaoVendaFavorito = false;
	private Boolean painelGestorVendedor = false;
	private Boolean painelGestorVendedorFavorito = false;
	// Compras
	private Boolean compraRel = false;
	private Boolean compraRelFavorito = false;
	private Boolean produtoServicoRel = false;
	private Boolean produtoServicoRelFavorito = false;
	private Boolean requisicaoRel = false;
	private Boolean requisicaoRelFavorito = false;
	protected Boolean personalizacaoMensagemAutomatica = false;
	private Boolean personalizacaoMensagemAutomaticaFavorito = false;
	private Boolean prestacaoContaTurma = false;
	private Boolean prestacaoContaTurmaFavorito = false;
	private Boolean prestacaoContaUnidadeEnsino = false;
	private Boolean prestacaoContaUnidadeEnsinoFavorito = false;
	private Boolean documentacaoPendenteProfessorRel = false;
	private Boolean documentacaoPendenteProfessorRelFavorito = false;
	private Boolean mapaAlunoRel = false;
	private Boolean mapaAlunoRelFavorito = false;
	private Boolean extratoContaCorrenteRel = false;
	private Boolean extratoContaCorrenteRelFavorito = false;
	private Boolean categoriaDespesaRel = false;
	private Boolean categoriaDespesaRelFavorito = false;
	private Boolean conteudo = false;
	private Boolean conteudoFavorito = false;
	private Boolean forum = false;
	private Boolean forumFavorito = false;
	private Boolean forumProfessor = false;
	private Boolean forumProfessorFavorito = false;
	private Boolean forumAluno = false;
	private Boolean forumAlunoFavorito = false;
	private Boolean alterarTemaForum = false;
	private Boolean alterarTemaForumFavorito = false;
	private Boolean alterarTemaForumProfessor = false;
	private Boolean alterarTemaForumProfessorFavorito = false;
	private Boolean contaReceber_PermitirAlterarDataVencimento = false;
	private Boolean contaReceber_PermitirEditarManualmenteConta = false;

	private Boolean contaReceber_PermitirAlterarDataVencimentoFavorito = false;
	private Boolean permitirCancelarContaReceber = false;
	private Boolean followMe = false;
	private Boolean followMeFavorito = false;
	private Boolean questaoExercicio = false;
	private Boolean questaoExercicioFavorito = false;
	private Boolean alterarExercicioOutroProfessor = false;
	private Boolean inativarExercicio = false;
	private Boolean questaoOnline = false;
	private Boolean questaoOnlineFavorito = false;
	private Boolean alterarQuestaoOnlineOutroProfessor = false;
	private Boolean inativarQuestaoOnline = false;
	private Boolean cancelarQuestaoOnline = false;
	private Boolean questaoPresencial = false;
	private Boolean alterarQuestaoPresencialOutroProfessor = false;
	private Boolean questaoPresencialFavorito = false;
	private Boolean inativarQuestaoPresencial = false;
	private Boolean cancelarQuestaoPresencial = false;
	private Boolean alterarAtividadeDiscursivaOutroProfessor = false;
	private Boolean inativarAtividadeDiscursiva = false;
	private Boolean listaExercicio = false;
	private Boolean listaExercicioFavorito = false;
	private Boolean listaExercicioProfessor = false;
	private Boolean listaExercicioProfessorFavorito = false;
	private Boolean listaExercicioAluno = false;
	private Boolean listaExercicioAlunoFavorito = false;
	private Boolean alterarListaExercicioOutroProfessor = false;
	private Boolean alterarListaExercicioOutroProfessorFavorito = false;
	private Boolean alterarListaExercicioOutroProfessorProfessor = false;
	private Boolean alterarListaExercicioOutroProfessorProfessorFavorito = false;
	private Boolean inativarListaExercicio = false;
	private Boolean inativarListaExercicioFavorito = false;
	private Boolean inativarListaExercicioProfessor = false;
	private Boolean inativarListaExercicioProfessorFavorito = false;
	private Boolean contaReceber_PermitirEnvioEmailCobranca = false;
	private Boolean contaReceber_PermitirEnvioEmailCobrancaFavorito = false;
	private Boolean duvidaProfessor = false;
	private Boolean duvidaProfessorFavorito = false;
	private Boolean duvidaProfessorAluno = false;
	private Boolean duvidaProfessorAlunoFavorito = false;
	private Boolean painelGestorCrm = false;
	private Boolean painelGestorCrmFavorito = false;
	private Boolean calendarioLancamentoNota = false;
	private Boolean calendarioLancamentoNotaFavorito = false;
	private Boolean calendarioRegistroAula = false;
	private Boolean calendarioRegistroAulaFavorito = false;
	private Boolean layoutEtiqueta = false;
	private Boolean layoutEtiquetaFavorito = false;
	private Boolean distribuirSalaProcessoSeletivo = false;
	private Boolean distribuirSalaProcessoSeletivoFavorito = false;
	private Boolean provaProcessoSeletivo = false;
	private Boolean provaProcessoSeletivoFavorito = false;
	private Boolean questaoProcessoSeletivo = false;
	private Boolean questaoProcessoSeletivoFavorito = false;
	private Boolean cancelarQuestaoProcessoSeletivo = false;
	private Boolean cancelarQuestaoProcessoSeletivoFavorito = false;
	private Boolean inativarQuestaoProcessoSeletivo = false;
	private Boolean inativarQuestaoProcessoSeletivoFavorito = false;
	private Boolean ativarQuestaoProcessoSeletivo = false;
	private Boolean ativarQuestaoProcessoSeletivoFavorito = false;
	private Boolean inativarProvaProcessoSeletivo = false;
	private Boolean inativarProvaProcessoSeletivoFavorito = false;
	private Boolean ativarProvaProcessoSeletivo = false;
	private Boolean ativarProvaProcessoSeletivoFavorito = false;
	private Boolean estatisticaProcessoSeletivo = false;
	private Boolean estatisticaProcessoSeletivoFavorito = false;
	private Boolean gabarito = false;
	private Boolean gabaritoFavorito = false;
	private Boolean contaReceber_SimularAlteracao = false;
	private Boolean contaReceber_SimularAlteracaoFavorito = false;
	private Boolean contaReceber_SimularAlteracaoFavoritoFavorito = false;
	private Boolean mapaRecebimentoContaReceberDuplicidade = false;
	private Boolean mapaRecebimentoContaReceberDuplicidadeFavorito = false;
	private Boolean demonstrativoResultadoFinanceiro = false;
	private Boolean demonstrativoResultadoFinanceiroFavorito = false;
	private Boolean planoDisciplinaRel = false;
	private Boolean planoDisciplinaRelFavorito = false;
	private Boolean etiquetaAlunoRel = false;
	private Boolean etiquetaAlunoRelFavorito = false;
	private Boolean estagioRel = false;
	private Boolean estagioRelFavorito = false;
	private Boolean tipoContato = false;
	private Boolean tipoContatoFavorito = false;
	private Boolean questionarioRel = false;
	private Boolean questionarioRelFavorito = false;
	private Boolean contaRecebidaVersoContaReceberRel = false;
	private Boolean contaRecebidaVersoContaReceberRelFavorito = false;
	private Boolean declaracaoTransferenciaInterna = false;
	private Boolean declaracaoTransferenciaInternaFavorito = false;
	private Boolean avaliacaoInstitucionalAnaliticoRel = false;
	private Boolean avaliacaoInstitucionalAnaliticoRelFavorito = false;
	private Boolean planoEnsino = false;
	private Boolean planoEnsinoFavorito = false;
	private Boolean perguntaPlanoEnsino = false;
	private Boolean perguntaPlanoEnsinoFavorito = false;
	private Boolean formularioPlanoEnsino = false;
	private Boolean formularioPlanoEnsinoFavorito = false;
	private Boolean permiteAlterarUnidadeEnsinoCertificadora = false;
	private Boolean inclusaoDisciplinaForaGrade = false;
	private Boolean cartaoResposta = false;
	private Boolean cartaoRespostaFavorito = false;
	private Boolean permitirApenasContasDaBiblioteca = false;

	private Boolean regraComissionamentoRanking = false;
	private Boolean regraComissionamentoRankingFavorito = false;

	private Boolean resultadoQuestionarioProcessoSeletivoRelFavorito = false;
	private Boolean resultadoQuestionarioProcessoSeletivoRel = false;

	private Boolean procSeletivoProvasRel = false;
	private Boolean procSeletivoProvasRelFavorito = false;

	private Boolean estatisticaProcessoSeletivoRelDadosCandidato = false;
	private Boolean estatisticaProcessoSeletivoRelInscritoBairro = false;
	private Boolean estatisticaProcessoSeletivoRelInscritoCurso = false;
	private Boolean estatisticaProcessoSeletivoRelListaFrequencia = false;
	private Boolean estatisticaProcessoSeletivoRelListaAprovado = false;
	private Boolean estatisticaProcessoSeletivoRelListaReprovado = false;
	private Boolean estatisticaProcessoSeletivoRelListaNaoMatriculado = false;
	private Boolean estatisticaProcessoSeletivoRelListaMatriculado = false;
	private Boolean estatisticaProcessoSeletivoRelListaAusente = false;
	private Boolean estatisticaProcessoSeletivoRelListaClassificado = false;
	private Boolean estatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno = false;

	private Boolean estatisticaProcessoSeletivoRelListaMuralCandidato = false;

	private Boolean alterarTodosPlanoEnsino = false;
	private Boolean alterarApenasUltimoPlanoEnsino = false;
	private Boolean criarNovoPlanoEnsino = false;

	private Boolean notificacaoProcessoSeletivo = false;
	private Boolean notificacaoProcessoSeletivoFavorito = false;

	private Boolean permitirAlterarValorContaReceber = false;
	private Boolean processoSeletivoOcorrenciaRel = false;
	private Boolean processoSeletivoOcorrenciaRelFavorito = false;
	private Boolean processoSeletivoRedacaoRel = false;
	private Boolean processoSeletivoRedacaoRelFavorito = false;

	private Boolean imprimirBoletoVencidoVisaoAluno = false;
	private Boolean permiteAlterarFuncionarioResponsavel = false;
	private Boolean permitirApenasRelatorioRecebimentoBiblioteca = false;
	private Boolean permitirApenasRelatorioContaReceberBiblioteca = false;

	private Boolean perguntaRequerimento = false;
	private Boolean questionarioRequerimento = false;
	private Boolean perguntaRequerimentoFavorito = false;
	private Boolean questionarioRequerimentoFavorito = false;

	private Boolean permitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina = false;

	private Boolean casosEspeciaisRel = false;
	private Boolean casosEspeciaisRelFavorito = false;

	private Boolean liberarInscricaoForaPrazo = false;
	private Boolean alterarAproveitamentoDisciplina = false;
	private Boolean alterarAproveitamentoDisciplinaFavorito = false;
	private Boolean tipoAdvertencia = false;
	private Boolean tipoAdvertenciaFavorito = false;
	private Boolean advertencia = false;
	private Boolean advertenciaFavorito = false;
	private Boolean permiteLancamentoAulaFutura = false;
	private Boolean permiteLancamentoAulaFuturaProfessor = false;
	private Boolean permiteLancamentoAulaFuturaCoordenador = false;
	private Boolean permiteLancamentoAtividadeComplementarFutura = false;
	private Boolean alterarHistoricoGradeAnterior;
	private Boolean alterarHistoricoGradeAnteriorFavorito;
	private Boolean contaReceberAgrupada = false;
	private Boolean contaReceberAgrupadaFavorito = false;
	private Boolean contaReceberAgrupada_PermitirProcessarAgrupamento = false;
	private Boolean tipoAtividadeComplementar = false;
	private Boolean tipoAtividadeComplementarFavorito = false;
	private Boolean registroAtividadeComplementar = false;
	private Boolean registroAtividadeComplementarFavorito = false;
	private Boolean acompanhamentoAtividadeComplementar = false;
	private Boolean acompanhamentoAtividadeComplementarFavorito = false;
	private Boolean atividadeComplementarAluno = false;
	private Boolean estagioAluno = false;
	private Boolean registroAtividadeComplementarCoordenador = false;
	private Boolean acompanhamentoAtividadeComplementarCoordenador = false;
	private Boolean recebimentoCompraRel = false;
	private Boolean recebimentoCompraRelFavorito = false;
	private Boolean ataResultadosFinais = false;
	private Boolean ataResultadosFinaisFavorito = false;
	private Boolean permitirLancarNotaRetroativo = false;
	private Boolean permitirRegistrarAulaRetroativo = false;
	private Boolean permitirGerarAtaProvaRetroativo = false;
	private Boolean permitirConsultarPlanoEnsinoAnterior = false;
	private Boolean distribuicaoSubturma = false;
	private Boolean distribuicaoSubturmaFavorito = false;
	private Boolean reservaCatalogoRel = false;
	private Boolean reservaCatalogoRelFavorito = false;
	private Boolean mapaEquivalenciaMatrizCurricular = false;
	private Boolean mapaEquivalenciaMatrizCurricularFavorito = false;
	private Boolean permitirGerarRelatorioDiarioRetroativo = false;
	private Boolean permitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular = false;
	private Boolean permitirAlterarMatrizCurricularConstrucao = false;
	private Boolean atividadeComplementarRel = false;
	private Boolean atividadeComplementarRelFavorito = false;
	private Boolean configuracaoNotaFiscal = false;
	private Boolean configuracaoNotaFiscalFavorito = false;
	private Boolean notaFiscalSaida = false;
	private Boolean notaFiscalSaidaFavorito = false;
	private Boolean valoresCursoGinfes = false;
	private Boolean valoresCursoGinfesFavorito = false;
	private Boolean integracaoGinfesCurso = false;
	private Boolean integracaoGinfesCursoFavorito = false;
	private Boolean integracaoGinfesAluno = false;
	private Boolean integracaoGinfesAlunoFavorito = false;

	private Boolean naturezaOperacao = false;
	private Boolean naturezaOperacaoFavorito = false;
	private Boolean imposto = false;
	private Boolean impostoFavorito = false;
	private Boolean notaFiscalEntrada = false;
	private Boolean notaFiscalEntradaFavorito = false;
	private Boolean impostosRetidosNotaFiscalEntradaRel = false;
	private Boolean impostosRetidosNotaFiscalEntradaRelFavorito = false;

	private Boolean mapaConvocacaoEnade = false;
	private Boolean mapaConvocacaoEnadeFavorito = false;
	private Boolean notaConceitoIndicadorAvaliacao = false;
	private Boolean notaConceitoIndicadorAvaliacaoFavorito = false;
	private Boolean criterioAvaliacao = false;
	private Boolean criterioAvaliacaoFavorito = false;

	private Boolean permitirComunicacaoInternaEnviarCopiaPorEmail = false;

	private Boolean criterioAvaliacaoAluno = false;
	private Boolean criterioAvaliacaoAlunoFavorito = false;
	private Boolean criterioAvaliacaoAlunoVisaoProfessor = false;
	private Boolean criterioAvaliacaoAlunoVisaoCoordenador = false;
	private Boolean criterioAvaliacaoAlunoVisaoPais = false;
	private Boolean criterioAvaliacaoAlunoRelVisaoProfessor = false;
	private Boolean criterioAvaliacaoAlunoRelVisaoCoordenador = false;
	private Boolean criterioAvaliacaoAlunoRel = false;
	private Boolean criterioAvaliacaoAlunoRelFavorito = false;

	private Boolean estoqueRel = false;
	private Boolean estoqueRelFavorito = false;
	private Boolean taxaRequerimento = false;
	private Boolean taxaRequerimentoFavorito = false;

	protected Boolean uploadBackUp = false;
	protected Boolean uploadBackUpFavorito = false;

	private Boolean minhasNotasAdminsitrativo = false;
	private Boolean minhasNotasAdminsitrativoFavorito = false;
	private Boolean autorizarPublicarPlanoEnsino = false;
	private Boolean autorizarPublicarPlanoEnsinoVisaoProfessorCoordenador = false;

	private Boolean permitirVisualizarCampanhaTodasUnidades = false;
	private Boolean permitirVisualizarCampanhaCobranca = false;
	private Boolean permitirVisualizarCampanhaVendas = false;
	private Boolean estagioCoordenador = false;
	private Boolean permitirGerarRelatorioDocumentoIntegralizacaoCurricular = false;
	private Boolean permitirImprimirMatrizCurricularAluno = false;
	private Boolean politicaDivulgacaoMatriculaOnline = false;
	private Boolean politicaDivulgacaoMatriculaOnlineFavorito = false;
	private Boolean permitirExcluirPreMatricula = false;
	private Boolean tipoPatrimonio = false;
	private Boolean tipoPatrimonioFavorito = false;
	private Boolean anotacaoDisciplina = false;
	private Boolean anotacaoDisciplinaFavorito = false;
	private Boolean patrimonio = false;
	private Boolean patrimonioFavorito = false;
	private Boolean questaoProfessor = false;
	private Boolean questaoProfessorFavorito = false;
	private Boolean configuracaoEAD = false;
	private Boolean configuracaoEADFavorito = false;
	private Boolean atividadeDiscursivaProfessor = false;
	private Boolean atividadeDiscursivaProfessorFavorito = false;
	private Boolean AtividadeDiscursivaAluno = false;
	private Boolean calendarioAtividadeMatricula = false;
	private Boolean calendarioAtividadeMatriculaFavorito = false;
	private Boolean avaliacaoOnline = false;
	private Boolean avaliacaoOnlineFavorito = false;
	private Boolean registrarFalta = false;
	private Boolean registrarFaltaFavorito = false;
	private Boolean configuracaoAvaliacaoOnlineTurmaProfessor = false;
	private Boolean permitirExcluirPreMatriculaCancelada = false;
	private Boolean gestaoAvaliacaoOnline = false;
	private Boolean gestaoAvaliacaoOnlineFavorito = false;
	private Boolean relatorioSEIDecidirCrm = false;
	private Boolean relatorioSEIDecidirCrmFavorito = false;
	private Boolean relatorioSEIDecidirReceita = false;
	private Boolean relatorioSEIDecidirReceitaFavorito = false;
	private Boolean relatorioSEIDecidirCentroResultadoReceitaFavorito = false;
	private Boolean relatorioSEIDecidirDespesa = false;
	private Boolean relatorioSEIDecidirDespesaFavorito = false;
	private Boolean relatorioSEIDecidirCentroResultadoDespesaFavorito = false;
	private Boolean relatorioSEIDecidirAcademico = false;
	private Boolean relatorioSEIDecidirAcademicoFavorito = false;
	private Boolean programacaoTutoriaOnline = false;
	private Boolean programacaoTutoriaOnlineFavorito = false;
	private Boolean layoutRelatorioSEIDecidir = false;
	private Boolean layoutRelatorioSEIDecidirFavorito = false;
	private Boolean vincularNotaEspecifica = false;
	private Boolean monitoramentoAlunosEADVisaoProfessor = false;
	private Boolean configuracaoConteudoTurma = false;
	private Boolean configuracaoConteudoTurmaFavorito = false;
	private Boolean importarArquivoMarc21 = false;
	private Boolean importarArquivoMarc21Favorito = false;
	private Boolean comunicacaoInternaRel = false;
	private Boolean comunicacaoInternaRelFavorito = false;
	private Boolean conteudoVisaoProfessor = false;
	private Boolean permitirProfessorCadastrarConteudoQualquerDisciplina = false;
	private Boolean permitirProfessorCadastrarConteudoApenasAulasProgramadas = false;
	private Boolean permitirProfessorCadastrarApenasConteudosExclusivos = false;
	private Boolean permitirProfessorReabrirAvaliacaoPBL = false;
	private Boolean permitirProfessorReabrirAtaPBL = false;
	private Boolean permitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline = false;
	private Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoOnline = false;
	private Boolean avaliacaoOnlineProfessor = false;
	private Boolean funcionario_permitirCriarUsuario = false;
	private Boolean alunoIniciarMatricula = false;
	private Boolean configuracaoConteudoTurmaVisaoProfessor;
	private Boolean definirResponsavelFinanceiro = false;
	private Boolean definirResponsavelFinanceiroFavorito = false;
	private Boolean monitoramentoAlunosEAD = false;
	private Boolean monitoramentoAlunosEADFavorito = false;
	private Boolean solicitarAlterarSenha = false;
	private Boolean solicitarAlterarSenhaFavorito = false;
	private Boolean monitoramentoAlunosEADVisaoCoodernador = false;
	private Boolean temaAssunto = false;
	private Boolean temaAssuntoFavorito = false;
	private Boolean permitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial = false;
	private Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoPresencial = false;
	private Boolean permitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio = false;
	private Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoExercicio = false;
	private Boolean estatisticaAcademicaPorConvenioRel = false;
	private Boolean estatisticaAcademicaPorConvenioRelFavorito = false;
	private Boolean parametrosMonitoramentoAvaliacaoOnline = false;
	private Boolean parametrosMonitoramentoAvaliacaoOnlineFavorito = false;
	private Boolean localArmazenamentoFavorito = false;
	private Boolean localArmazenamento = false;
	private Boolean biometria = false;
	private Boolean biometriaFavorito = false;
	private Boolean matriculaOnlineVisaoAluno = false;
	private Boolean painelGestorMonitorarPorSegmentacao = false;
	private Boolean textoPadraoPatrimonio = false;
	private Boolean textoPadraoPatrimonioFavorito = false;
	private Boolean processarResultadoProvaPresencial = false;
	private Boolean processarResultadoProvaPresencialFavorito = false;
	private Boolean catraca = false;
	private Boolean catracaFavorito = false;
	private Boolean ocorrenciaPatrimonio = false;
	private Boolean ocorrenciaPatrimonioFavorito = false;
	private Boolean permitirCadastrarOcorrenciaManutencao = false;
	private Boolean permitirCadastrarOcorrenciaEmprestimo = false;
	private Boolean permitirCadastrarOcorrenciaDescarte = false;
	private Boolean permitirCadastrarOcorrenciaTrocaLocal = false;
	private Boolean permitirCadastrarOcorrenciaSeparacaoDescarte = false;
	private Boolean patrimonioRel = false;
	private Boolean patrimonioRelFavorito = false;
	private Boolean ocorrenciaPatrimonioRel = false;
	private Boolean ocorrenciaPatrimonioRelFavorito = false;
	private Boolean mapaPatrimonioSeparadoDescarte = false;
	private Boolean mapaPatrimonioSeparadoDescarteFavorito = false;
	private Boolean liberarAlteracaoTaxaOperacaoCartaoCredito = false;
	private Boolean liberarAlteracaoContaCorrenteCartaoCredito = false;
	private Boolean mapaNotaPendenciaAlunoRel = false;
	private Boolean mapaNotaPendenciaAlunoRelFavorito = false;
	private Boolean permitirAlterarDataMatricula = true;
	private Boolean permiteAlterarPeriodoCalendario = true;
	private Boolean permiteLiberarProgramacaoAulaProfessorAcimaPermitido = false;
	private Boolean frequenciaAlunoRel = false;
	private Boolean frequenciaAlunoRelFavorito = false;
	private Boolean alteracaoPlanoFinanceiroAluno = false;
	private Boolean alteracaoPlanoFinanceiroAlunoFavorito = false;
	private Boolean mapaRegistroEvasaoCurso = false;
	private Boolean mapaRegistroEvasaoCursoFavorito = false;
//	private Boolean mapaRegistroAbandonoCursoTrancamento = false;
//	private Boolean mapaRegistroAbandonoCursoTrancamentoFavorito = false;
	private Boolean notaNaoLancadaProfessorRel = false;
	private Boolean notaNaoLancadaProfessorRelFavorito = false;
	private Boolean configuracaoRecebimentoCartaoOnline = false;
	private Boolean configuracaoRecebimentoCartaoOnlineFavorito = false;
	private Boolean controleAcessoAlunoCatracaRel = false;
	private Boolean controleAcessoAlunoCatracaRelFavorito = false;
	private Boolean permitirCancelarDCCPrevisto = false;
	private Boolean permitirApresentarValorRecursoPatrimonioRel = false;
	private Boolean permitirSimularAcessoVisaoAluno = false;
	private Boolean permitirCadastrarConvenioMatricula = false;
	private Boolean permiteAlterarInformacoesAdicionaisConvenio = false;
	private Boolean liberarValidacaoDadosEnadeCenso = false;

	private Boolean permiteRegistrarEstagioSeguradora = false;

	private Boolean motivoEmprestimoPorHora = false;
	private Boolean motivoEmprestimoPorHoraFavorito = false;

	private Boolean permiteExcluirAulaProgramadaGravarFeriado = false;
	private Boolean inclusaoHistoricoAluno = false;
	private Boolean inclusaoHistoricoAlunoFavorito = false;
	private Boolean gestaoEventoConteudoTurmaVisaoProfessor = false;
	private Boolean gestaoEventoConteudoTurmaVisaoProfessorFavorito = false;
	private Boolean permiteAlterarNotaAlunoAvaliacaoPBL = false;

	private Boolean calendarioAberturaRequerimento = false;
	private Boolean calendarioAberturaRequerimentoFavorito = false;

	private Boolean livroMatriculaRel = false;
	private Boolean livroMatriculaRelFavorito = false;
	private Boolean inutilizacaoNotaFiscal = false;
	private Boolean inutilizacaoNotaFiscalFavorito = false;
	private Boolean permiteLancarNotaAlunoTransferenciaMatriz = false;
	private Boolean permiteRegistrarAulaAlunoTransferenciaMatriz = false;
	private Boolean tipoJustificativaFalta = false;
	private Boolean tipoJustificativaFaltaFavorito = false;
	private Boolean permiteEstornarTransferenciaSaida = false;
	private Boolean permiteEstornarTransferenciaInterna = false;
	private Boolean configuracaoMobile = false;
	private Boolean configuracaoMobileFavorito = false;
	private Boolean configuracaoSeiGsuite = false;
	private Boolean configuracaoSeiGsuiteFavorito = false;
	private Boolean configuracaoSeiBlackboard = false;
	private Boolean configuracaoSeiBlackboardFavorito = false;
	private Boolean gestaoTurmaRel = false;
	private Boolean gestaoTurmaRelFavorito = false;

	private Boolean preInscricaoLog = false;
	private Boolean preInscricaoLogFavorito = false;
	private Boolean impressora = false;
	private Boolean impressoraFavorito = false;

	protected Boolean impressaoTextoPadraoProcessoSeletivo = false;
	private Boolean impressaoTextoPadraoProcessoSeletivoFavorito = false;
	protected Boolean textoPadraoProcessoSeletivo = false;
	private Boolean textoPadraoProcessoSeletivoFavorito = false;
	private Boolean permitirCadastrarOcorrenciaReservaUnidade = false;
	private Boolean permitirCadastrarOcorrenciaReservaLocal = false;
	private Boolean gestaoReservaPatrimonio = false;
	private Boolean gestaoReservaPatrimonioFavorito = false;
	private Boolean permiteExcluirPlanoEnsino = false;
	private Boolean permiteReplicarNotaOutraDisciplina = false;

	private Boolean categoriaGED = false;
	private Boolean categoriaGEDFavorito = false;

	private Boolean mapaDocumentacaoDigitalizadoGED = false;
	private Boolean mapaDocumentacaoDigitalizadoGEDFavorito = false;
	private Boolean negociacaoContaPagar;
	private Boolean negociacaoContaPagarFavorito;
	private Boolean condicaoDescontoRenegociacao = false;
	private Boolean condicaoDescontoRenegociacaoFavorito = false;

	private Boolean centralAluno = false;
	private Boolean permitirVisualizarDadosMatricula = false;
	private Boolean permitirVisualizarDadosFinanceiro = false;
	private Boolean permitirVisualizarDadosRequerimento = false;
	private Boolean permitirVisualizarDadosBiblioteca = false;
	private Boolean permitirVisualizarDadosCRM = false;
	private Boolean permitirVisualizarDadosProcessoSeletivo = false;
	private Boolean permitirVisualizarDadosComunicacaoInterna = false;

	// Menu RH - Folha de Pagamento
	private Boolean valorReferenciaFolhaPagamento = false;
	private Boolean valorReferenciaFolhaPagamentoFavorito = false;
//		private Boolean incidenciaFolhaPagamento = false;
//		private Boolean incidenciaFolhaPagamentoFavorito = false;
	private Boolean formulaFolhaPagamento = false;
	private Boolean formulaFolhaPagamentoFavorito = false;

	private Boolean eventoFolhaPagamento = false;
	private Boolean eventoFolhaPagamentoFavorito = false;

	private Boolean grupoLancamentoFolhaPagamento = false;
	private Boolean grupoLancamentoFolhaPagamentoFavorito = false;

	private Boolean lancamentoFolhaPagamento = false;
	private Boolean lancamentoFolhaPagamentoFavorito = false;

	private Boolean eventoFixoCargoFuncionario = false;
	private Boolean eventoFixoCargoFuncionarioFavorito = false;

	private Boolean secaoFolhaPagamento = false;
	private Boolean secaoFolhaPagamentoFavorito = false;

	private Boolean nivelSalarial = false;
	private Boolean nivelSalarialFavorito = false;

	private Boolean faixaSalarial = false;
	private Boolean faixaSalarialFavorito = false;

	private Boolean progressaoSalarial = false;
	private Boolean progressaoSalarialFavorito = false;

	private Boolean competenciaFolhaPagamento = false;
	private Boolean competenciaFolhaPagamentoFavorito = false;

	private Boolean fichaFinanceira = false;
	private Boolean fichaFinanceiraFavorito = false;

	private Boolean tipoEmprestimo = false;
	private Boolean tipoEmprestimoFavorito = false;

	private Boolean tipoTransporte = false;
	private Boolean tipoTransporteFavorito = false;

	private Boolean fichaFinanceiraRel = false;
	private Boolean fichaFinanceiraRelFavorito = false;

	private Boolean parametroValeTransporte = false;
	private Boolean parametroValeTransporteFavorito = false;

	private Boolean salarioComposto = false;
	private Boolean salarioCompostoFavorito = false;

	private Boolean eventoValeTransporteFuncionarioCargo = false;
	private Boolean eventoValeTransporteFuncionarioCargoFavorito = false;

	private Boolean eventoEmprestimoCargoFuncionario;
	private Boolean eventoEmprestimoCargoFuncionarioFavorito;

	private Boolean faltasFuncionario = false;
	private Boolean faltasFuncionarioFavorito = false;

	private Boolean periodoAquisitivoFerias = false;
	private Boolean periodoAquisitivoFeriasFavorito = false;

	private Boolean sindicato = false;
	private Boolean sindicatoFavorito = false;

	private Boolean marcacaoFerias = false;
	private Boolean marcacaoFeriasFavorito = false;

	private Boolean marcacaoFeriasColetivas = false;
	private Boolean marcacaoFeriasColetivasFavorito = false;

	private Boolean historicoFuncionario = false;
	private Boolean historicoFuncionarioFavorito = false;

	private Boolean afastamentoFuncionario = false;
	private Boolean afastamentoFuncionarioFavorito = false;

	private Boolean rescisao = false;
	private Boolean rescisaoFavorito = false;

	private Boolean linkVisoesMoodleVisaoCoordenador = false;

	private Boolean funcionarioRel = false;
	private Boolean funcionarioRelFavorito = false;

	private Boolean historicoSituacaofuncionarioRel = false;
	private Boolean historicoSituacaofuncionarioRelFavorito = false;

	private Boolean atualizarValeTrasnporte = false;
	private Boolean atualizarValeTrasnporteFavorito = false;

	private Boolean relatorioSEIDecidirRecursosHumanos = false;
	private Boolean relatorioSEIDecidirRecursosHumanosFavorito = false;

	private Boolean relatorioSEIDecidirPlanoOrcamentario = false;
	private Boolean relatorioSEIDecidirPlanoOrcamentarioFavorito = false;

	private Boolean downloadRel = false;
	private Boolean downloadRelFavorito = false;

	// Fim Menu RH - Folha de Pagamento

	protected Boolean entregaDocumentoPermiteAnexarArquivo = false;
	private Boolean entregaDocumentoPermiteAnexarArquivoFavorito = false;

	private Boolean unificacaoCadastroPessoa = false;
	private Boolean unificacaoCadastroPessoaFavorito = false;

	private Boolean configuracaoGED = false;
	private Boolean configuracaoGEDFavorito = false;
	private Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio = false;

	private Boolean atividadeExtraClasseProfessor = false;
	private Boolean atividadeExtraClasseProfessorFavorito = false;

	private Boolean atividadeExtraClasseProfessorPostado = false;
	private Boolean atividadeExtraClasseProfessorPostadoFavorito = false;

	private Boolean mapaAtividadeExtraClasseProfessor = false;
	private Boolean mapaAtividadeExtraClasseProfessorFavorito = false;

	private Boolean mapaAtividadeExtraClasseProfessorVisaoCoordenador = false;
	private Boolean mapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador = false;

	private Boolean questionarioRequisicao = false;
	private Boolean questionarioRequisicaoFavorito = false;

	private Boolean perguntaRequisicao = false;
	private Boolean perguntaRequisicaoFavorito = false;

	private Boolean definirAtividadeExtraClasseProfessorVisaoCoordenador = false;
	private Boolean definirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito = false;
	private Boolean anularQuestaoOnline = false;

	private Boolean calendarioLancamentoPlanoEnsino = false;
	private Boolean calendarioLancamentoPlanoEnsinoFavorito = false;

	private Boolean planoDisciplinaCoordenadorRel = false;
	private Boolean planoDisciplinaCoordenadorRelFavorito = false;

	private Boolean relatorioSEIDecidirRequerimento = false;
	private Boolean relatorioSEIDecidirRequerimentoFavorito = false;
	private Boolean bibliotecaBvPearson = false;
	private Boolean bibliotecaPearsonFavorito = false;
	private Boolean relatorioSEIDecidirProcessoSeletivo = false;
	private Boolean relatorioSEIDecidirProcessoSeletivoFavorito = false;

	private Boolean importarCandidatoInscricaoProcessoSeletivo = false;
	private Boolean importarCandidatoInscricaoProcessoSeletivoFavorito = false;

	private Boolean registroLdap = false;
	private Boolean registroLdapFavorito = false;

	private Boolean gestaoSalasNotasBlackboard = false;
	private Boolean gestaoSalasNotasBlackboardFavorito = false;
	private Boolean permiteApurarNotasBlackboard= false;
	private Boolean permiteCopiarConteudoBlackboard = false;
	private Boolean ApresentarDocumentacaoGED = false;
	private Boolean permitirGerarRelatorioSeiDecidirRequerimentoApenasDados =  false;
	private Boolean permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados =  false;
	private Boolean permitirGerarRelatorioSeiDecidirRHApenasDados =  false;
	private Boolean permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados =  false;
	
	private Boolean logHistorico = false;
	private Boolean logHistoricoFavorito = false;

	protected Boolean modalidadeTurma = false;
	protected Boolean modalidadeTurmaFavorito = false;
	private Boolean permitirAlterarMatrizAlunosCalourosSemAproveitamentoDisciplinas;
	
	private Boolean permitirAlterarFaltasProvenienteTransferencia = false;

	private Boolean permitirVisualizarScriptSqlRelatorioSeiDecidirAcademico =  false;
	private Boolean permitirVisualizarScriptSqlRelatorioSeiDecidirDespesa =  false;
	private Boolean permitirVisualizarScriptSqlRelatorioSeiDecidirReceita =  false;
	private Boolean permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca =  false;
	
	private Boolean relatorioSEIDecidirAdministrativo = false;
	private Boolean relatorioSEIDecidirAdministrativoFavorito = false;
	private Boolean relatorioSEIDecidirAvaliacaoInstitucional = false;
	private Boolean relatorioSEIDecidirAvaliacaoInstitucionalFavorito = false;
	private Boolean relatorioSEIDecidirCompra = false;
	private Boolean relatorioSEIDecidirCompraFavorito = false;
	private Boolean relatorioSEIDecidirEAD = false;
	private Boolean relatorioSEIDecidirEADFavorito = false;
	private Boolean relatorioSEIDecidirEstagio = false;
	private Boolean relatorioSEIDecidirEstagioFavorito = false;
	private Boolean relatorioSEIDecidirNotaFiscal = false;
	private Boolean relatorioSEIDecidirNotaFiscalFavorito = false;
	private Boolean relatorioSEIDecidirPatrimonio = false;
	private Boolean relatorioSEIDecidirPatrimonioFavorito = false;
	private Boolean permitirFechamentoNotaBlackBoard = false;
	private Boolean permitirUploadDeferimentoIndeferimentoDocumento = false;
	private Boolean arquivoAssinadoDiploma;
	private Boolean arquivoAssinadoHistorico;
	private Boolean arquivoAssinadoHistoricoFavorito;
	private Boolean arquivoAssinadoDiplomaFavorito;
	private Boolean permiteConfigurarIntegracaoMestreGR = false;

	public Boolean getPainelGestorCrm() {
		return painelGestorCrm;
	}

	public void setPainelGestorCrm(Boolean painelGestorCrm) {
		this.painelGestorCrm = painelGestorCrm;
	}

	public Boolean getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Boolean workflow) {
		this.workflow = workflow;
	}

	public Boolean getPainelGestorRequerimentosAcademico() {
		return painelGestorRequerimentosAcademico;
	}

	public void setPainelGestorRequerimentosAcademico(Boolean painelGestorRequerimentosAcademico) {
		this.painelGestorRequerimentosAcademico = painelGestorRequerimentosAcademico;
	}

	public Boolean getPainelGestorComunicacaoInternaAcademico() {
		return painelGestorComunicacaoInternaAcademico;
	}

	public void setPainelGestorComunicacaoInternaAcademico(Boolean painelGestorComunicacaoInternaAcademico) {
		this.painelGestorComunicacaoInternaAcademico = painelGestorComunicacaoInternaAcademico;
	}

	public Boolean getPainelGestorRequerimentosFinanceiro() {
		return painelGestorRequerimentosFinanceiro;
	}

	public void setPainelGestorRequerimentosFinanceiro(Boolean painelGestorRequerimentosFinanceiro) {
		this.painelGestorRequerimentosFinanceiro = painelGestorRequerimentosFinanceiro;
	}

	public Boolean getPainelGestorComunicacaoInternaFinanceiro() {
		return painelGestorComunicacaoInternaFinanceiro;
	}

	public void setPainelGestorComunicacaoInternaFinanceiro(Boolean painelGestorComunicacaoInternaFinanceiro) {
		this.painelGestorComunicacaoInternaFinanceiro = painelGestorComunicacaoInternaFinanceiro;
	}

	// Menu Processo Seletivo
	public Boolean getMenuProcSeletivo() {
		if (getCandidato() || getDisciplinasProcSeletivo() || getInscricao() || getPerfilSocioEconomico() || getProcSeletivo() || getResultadoProcessoSeletivo() || getTextoPadraoProcessoSeletivo()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getLog() {
		if (log == null) {
			log = false;
		}
		return log;
	}

	public void setLog(Boolean log) {
		this.log = log;
	}

	public Boolean getMapaControleAtividadesUsuarios() {
		if (mapaControleAtividadesUsuarios == null) {
			mapaControleAtividadesUsuarios = false;
		}
		return mapaControleAtividadesUsuarios;
	}

	public void setMapaControleAtividadesUsuarios(Boolean mapaControleAtividadesUsuarios) {
		this.mapaControleAtividadesUsuarios = mapaControleAtividadesUsuarios;
	}

	public Boolean getMenuProcSeletivoRel() {
		if (getComprovanteInscricao() || getAlunosProcessoSeletivoRel() || getProcSeletivoRel() || getAlunosMatriculadosPorProcessoSeletivoRel() || getPerfilSocioEconomicoRel() || getProcSeletivoInscricoesRel() || getAlunosMatriculadosGeralRel() || getProcessoSeletivoAprovadoReprovadoRel() || getResultadoProcessoSeletivoRel() || getDeclaracaoAprovacaoVestRel() || getCartaoRespostaRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMenuBasicoBancoCurriculo() {
		if (getAreaProfissional() || getPainelAluno() || getVagas() || getLink() || getParceiro() || getTextoPadraoBancoCurriculum()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMenuRelatorioBancoCurriculo() {
		if (getCandidatosParaVagaRel() || getEmpresaVagaBancoTalentoRel() || getEmpresasRel() || getAlunosCandidatadosVagaRel() || getEmpresaPorVagasRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getCandidato() {
		return candidato;
	}

	public void setCandidato(Boolean candidato) {
		this.candidato = candidato;
	}

	public Boolean getDisciplinasProcSeletivo() {
		return disciplinasProcSeletivo;
	}

	public void setDisciplinasProcSeletivo(Boolean disciplinasProcSeletivo) {
		this.disciplinasProcSeletivo = disciplinasProcSeletivo;
	}

	public Boolean getInscricao() {
		return inscricao;
	}

	public void setInscricao(Boolean inscricao) {
		this.inscricao = inscricao;
	}

	public Boolean getPerfilSocioEconomico() {
		return perfilSocioEconomico;
	}

	public void setPerfilSocioEconomico(Boolean perfilSocioEconomico) {
		this.perfilSocioEconomico = perfilSocioEconomico;
	}

	public Boolean getProcSeletivo() {
		return procSeletivo;
	}

	public void setProcSeletivo(Boolean procSeletivo) {
		this.procSeletivo = procSeletivo;
	}

	public Boolean getResultadoProcessoSeletivo() {
		return resultadoProcessoSeletivo;
	}

	public void setResultadoProcessoSeletivo(Boolean resultadoProcessoSeletivo) {
		this.resultadoProcessoSeletivo = resultadoProcessoSeletivo;
	}

	// Menu Protocolo
	public Boolean getMenuProtocolo() {
		if (getControleCorrespondencia() || getRequerimento() || getTipoRequerimento() || getTransferenciaEntrada() || getTransferenciaSaida() || getTransferenciaInterna() || getTransferenciaUnidade() || getTransferenciaTurno() || getTransferenciaMatrizCurricular() || getControleCorrespondencia() || getRequerimento() || getTipoRequerimento() || getAlterarResponsavelRequerimento()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getControleCorrespondencia() {
		return controleCorrespondencia;
	}

	public void setControleCorrespondencia(Boolean controleCorrespondencia) {
		this.controleCorrespondencia = controleCorrespondencia;
	}

	public Boolean getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Boolean requerimento) {
		this.requerimento = requerimento;
	}

	public Boolean getTipoRequerimento() {
		return tipoRequerimento;
	}

	public void setTipoRequerimento(Boolean tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	private Boolean permitirAlterarMatrizCurricularAtivaInativa = false;

	// Menu Financeiro
	// public Boolean getSubMenuAcesso() {
	// if (getPerfilAcesso() || getUsuario() ||
	// getMapaControleAtividadesUsuarios() || getAlunosAtivosRel()) {
	// return Boolean.TRUE;
	// }
	// return Boolean.FALSE;
	// }
	public Boolean getSubMenuLiberacaoFinanceiro() {
		if (getLiberacaoFinanceiroCancelamentoTrancamento()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuCRM() {
		if (getMeta() || getSituacaoProspectPipeline() || getWorkflow() || getRegistroEntrada() || getCampanha() || getProspects() || getMotivoInsucesso() || getComissionamentoTurma() || getRanking() || getConfiguracaoRanking() || getPainelGestorSupervisaoVenda() || getPainelGestorVendedor()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuAcesso() {
		if (getPerfilAcesso() || getUsuario() || getMapaControleAtividadesUsuarios()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuBancoParceiro() {
		if (getAgencia() || getBanco() || getContaCorrente() || getConvenio() || getParceiro() || getFormaPagamento() || getCondicaoPagamento() || getModeloBoleto() || getControleCobranca() || getControleMovimentacaoRemessa() || getMapaPendenciasControleCobranca() || getOperadoraCartao() || getControleRemessa() || getContaReceberNaoLocalizadaArquivoRetorno() || getProcessamentoArquivoRetornoParceiro()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMenuBancoCurriculum() {
		if (getAreaProfissional() || getVagas() || getLink() || getParceiro() || getTextoPadraoBancoCurriculum()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuContaPagar() {
		if (getContaPagar() || getCategoriaDespesa() || getContratosDespesas() || getContratosReceitas() || getPagamento() || getProvisaoCusto() || getGrupoContaPagar() || getControleCobrancaPagar() || getControleRemessaContaPagar()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuContaReceber() {
		if (getContaReceber() || getRecebimento() || getNegociacaoContaReceber() || getCentroReceita() || getDescontoChancela() || getGeracaoManualParcelas() || getGeracaoManualParcelasAluno() || getMudancaCarteira() || getConsultaLogContaReceber() || getNegociacaoRecebimento() || getContratosReceitas() || getCentroReceita() || getDescontoChancela() || getMatriculaSerasa() || getMatriculaLiberacao()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuCaixa() {
		if (getFluxoCaixa() || getMovimentacaoFinanceira() || getDevolucaoCheque() || getCheque() || getMapaPendenciaCartaoCredito() || getMapaPendenciaMovimentacaoFinanceira() || getMapaLancamentoFuturo() || getConciliacaoContaCorrente() || getPixContaCorrente()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuPlanoOrcamentario() {
		if (getPlanoOrcamentario() || getSolicitacaoOrcamentoPlanoOrcamentario()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuPerfilEconomico() {
		if (getCondicaoNegociacao() || getPerfilEconomico()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuGeracaoArquivosFinanceiro() {
		if (getSerasa()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuRelatorioFinanceiro() {
		if (getFluxoCaixaRel() || getContaPagarResumidaPorDataRel() || getContaPagarResumidaPorFornecedorRel() || getContaReceberRel() || getTermoReconhecimentoDividaRel() || getPagamentoRel() || getPagamentoResumidoRel() || getRecebimentoRel() || getFaturamentoRecebimentoRel() || getRecebimentoPorTurmaRel() || getPrestacaoContaRel() || getChequesRel() || getInadimplenciaRel() || getCartaoCreditoDebitoRel() || getMapaFinanceiroAlunoRel() || getSituacaoFinanceiraAlunoRel() || getEntregaBoletosRel() || getListagemDescontosAlunosRel() || getExtratoContaPagarRel() || getRecebimentoRel() || getRenegociacaoRel() || getPrevisaoFaturamentoRel() || getMapaFinanceiroAlunoRel() || getCondicoesPagamentoRel() || getContaPagarPorCategoriaDespesaRel() || getContaPagarPorTurmaRel() || getPagamentoRel() || getPrestacaoContaRel() || getExtratoContaPagarRel() || getListagemDescontosAlunosRel() || getMediaDescontoAlunoRel() || getMediaDescontoTurmaRel() || getTipoDescontoRel() || getQuadroComissaoConsultoresRel()
				|| getChequesRel() || getDeclaracaoImpostoRendaRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMenuFinanceiro() {
		if (getSubMenuBancoParceiro() || getSubMenuPerfilEconomico() || getSubMenuContaPagar() || getSubMenuContaReceber() || getSubMenuRelatorioFinanceiro() || getSubMenuPrestacaoConta()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getAgencia() {
		return agencia;
	}

	public void setAgencia(Boolean agencia) {
		this.agencia = agencia;
	}

	public Boolean getBanco() {
		return banco;
	}

	public void setBanco(Boolean banco) {
		this.banco = banco;
	}

	public Boolean getCategoriaDespesa() {
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(Boolean categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public Boolean getCentroReceita() {
		return centroReceita;
	}

	public void setCentroReceita(Boolean centroReceita) {
		this.centroReceita = centroReceita;
	}

	public Boolean getDescontoChancela() {
		return descontoChancela;
	}

	public void setDescontoChancela(Boolean descontoChancela) {
		this.descontoChancela = descontoChancela;
	}

	public Boolean getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(Boolean contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public Boolean getContaPagar() {
		return contaPagar;
	}

	public void setContaPagar(Boolean contaPagar) {
		this.contaPagar = contaPagar;
	}

	public Boolean getGrupoContaPagar() {
		return grupoContaPagar;
	}

	public void setGrupoContaPagar(Boolean grupoContaPagar) {
		this.grupoContaPagar = grupoContaPagar;
	}

	public Boolean getContaReceber() {
		return contaReceber;
	}

	public void setContaReceber(Boolean contaReceber) {
		this.contaReceber = contaReceber;
	}

	public Boolean getContratosDespesas() {
		return contratosDespesas;
	}

	public void setContratosDespesas(Boolean contratosDespesas) {
		this.contratosDespesas = contratosDespesas;
	}

	public Boolean getContratosReceitas() {
		return contratosReceitas;
	}

	public void setContratosReceitas(Boolean contratosReceitas) {
		this.contratosReceitas = contratosReceitas;
	}

	public Boolean getConvenio() {
		return convenio;
	}

	public void setConvenio(Boolean convenio) {
		this.convenio = convenio;
	}

	public Boolean getPagamento() {
		return pagamento;
	}

	public void setPagamento(Boolean pagamento) {
		this.pagamento = pagamento;
	}

	public Boolean getParceiro() {
		return parceiro;
	}

	public void setParceiro(Boolean parceiro) {
		this.parceiro = parceiro;
	}

	public Boolean getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(Boolean planoConta) {
		this.planoConta = planoConta;
	}

	public Boolean getConfiguracaoContabil() {
		return configuracaoContabil;
	}

	public void setConfiguracaoContabil(Boolean configuracaoContabil) {
		this.configuracaoContabil = configuracaoContabil;
	}

	public Boolean getConfiguracaoContabilFavorito() {
		return configuracaoContabilFavorito;
	}

	public void setConfiguracaoContabilFavorito(Boolean configuracaoContabilFavorito) {
		this.configuracaoContabilFavorito = configuracaoContabilFavorito;
	}

	public Boolean getLayoutIntegracao() {
		return layoutIntegracao;
	}

	public void setLayoutIntegracao(Boolean layoutIntegracaoXml) {
		this.layoutIntegracao = layoutIntegracaoXml;
	}

	public Boolean getLayoutIntegracaoFavorito() {
		return layoutIntegracaoFavorito;
	}

	public Boolean getIntegracaoContabil() {
		return integracaoContabil;
	}

	public void setIntegracaoContabil(Boolean integracaoContabil) {
		this.integracaoContabil = integracaoContabil;
	}

	public Boolean getIntegracaoContabilFavorito() {
		return integracaoContabilFavorito;
	}

	public void setIntegracaoContabilFavorito(Boolean integracaoContabilFavorito) {
		this.integracaoContabilFavorito = integracaoContabilFavorito;
	}

	public void setLayoutIntegracaoFavorito(Boolean layougetIntegracaoXmlFavorito) {
		this.layoutIntegracaoFavorito = layougetIntegracaoXmlFavorito;
	}

	public Boolean getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Boolean recebimento) {
		this.recebimento = recebimento;
	}

	public Boolean getOperadoraCartao() {
		return operadoraCartao;
	}

	public void setOperadoraCartao(Boolean operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
	}

	// Menu Eventos
	public Boolean getMenuEventos() {
		if (getTrabalhoSubmetido() || getMembroComunidade()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getTrabalhoSubmetido() {
		return trabalhoSubmetido;
	}

	public void setTrabalhoSubmetido(Boolean trabalhoSubmetido) {
		this.trabalhoSubmetido = trabalhoSubmetido;
	}

	public Boolean getMembroComunidade() {
		return membroComunidade;
	}

	public void setMembroComunidade(Boolean membroComunidade) {
		this.membroComunidade = membroComunidade;
	}

	public Boolean getEmpresaPorVagasRel() {
		return empresaPorVagasRel;
	}

	public void setEmpresaPorVagasRel(Boolean empresaPorVagasRel) {
		this.empresaPorVagasRel = empresaPorVagasRel;
	}

	// Menu Compras
	public Boolean getMenuCompras() {
		if (getSubMenuCompraBasico() || getSubMenuCompraCotacao() || getSubMenuCompraCusto() || getSubMenuCompraRequisicao()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	// Menu Contabil
	public Boolean getMenuContabil() {
		if (getSubMenuContabil()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuCompraBasico() {
		if (getFornecedor() || getProdutoServico() || getCategoriaProduto() || getEstoque() || getMovimentacaoEstoque() || getFormaPagamento() || getCondicaoPagamento()) {
			return true;
		}
		return false;
	}

	public Boolean getSubMenuCompraRequisicao() {
		if (getRequisicao() || getEntregaRequisicao() || getMapaRequisicao()) {
			return true;
		}
		return false;
	}

	public Boolean getSubMenuCompraCotacao() {
		if (getCotacao() || getCompra() || getRecebimentoCompra() || getDevolucaoCompra() || getMapaCotacao()) {
			return true;
		}
		return false;
	}

	public Boolean getSubMenuRelatorioCompras() {
		if (getRequisicaoRel() || getCompraRel() || getProdutoServicoRel()) {
			return true;
		}
		return false;
	}

	public Boolean getSubMenuCompraCusto() {
		if (getClassificaoCustos() || getPrevisaoCustos() || getPgtoServicoAcademico() || getSolicitacaoPgtoServicoAcademico()) {
			return true;
		}
		return false;
	}

	public Boolean getCategoriaProduto() {
		return categoriaProduto;
	}

	public void setCategoriaProduto(Boolean categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public Boolean getClassificaoCustos() {
		return classificaoCustos;
	}

	public void setClassificaoCustos(Boolean classificaoCustos) {
		this.classificaoCustos = classificaoCustos;
	}

	public Boolean getCompra() {
		return compra;
	}

	public void setCompra(Boolean compra) {
		this.compra = compra;
	}

	public Boolean getEstoque() {
		return estoque;
	}

	public void setEstoque(Boolean estoque) {
		this.estoque = estoque;
	}

	public Boolean getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(Boolean formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Boolean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Boolean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Boolean getPgtoServicoAcademico() {
		return pgtoServicoAcademico;
	}

	public void setPgtoServicoAcademico(Boolean pgtoServicoAcademico) {
		this.pgtoServicoAcademico = pgtoServicoAcademico;
	}

	public Boolean getPrevisaoCustos() {
		return previsaoCustos;
	}

	public void setPrevisaoCustos(Boolean previsaoCustos) {
		this.previsaoCustos = previsaoCustos;
	}

	public Boolean getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(Boolean produtoServico) {
		this.produtoServico = produtoServico;
	}

	public Boolean getSolicitacaoPgtoServicoAcademico() {
		return solicitacaoPgtoServicoAcademico;
	}

	public void setSolicitacaoPgtoServicoAcademico(Boolean solicitacaoPgtoServicoAcademico) {
		this.solicitacaoPgtoServicoAcademico = solicitacaoPgtoServicoAcademico;
	}

	// Menu Biblioteca
	public Boolean getMenuBiblioteca() {
		if (getAutor() || getBiblioteca() || getClassificacaoBibliografica() || getEditora() || getExemplar() || getPublicacao() || getItemPublicacaoAutor() || getRecursosAudioVisuais() || getRegistroSaidaAcervo() || getEmprestimo() || getCatalogo() || getTitulo() || getRegistroEntradaAcervo() || getSecao() || getAssinaturaPeriodico() || getConfiguracaoBiblioteca() || getTipoAutoria() || getTipoCatalogo() || getBloqueioBiblioteca() || getSubMenuRelatorioBiblioteca() || getBuscaCatalogo() || getMinhaBiblioteca()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getAutor() {
		return autor;
	}

	public void setAutor(Boolean autor) {
		this.autor = autor;
	}

	public Boolean getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(Boolean biblioteca) {
		this.biblioteca = biblioteca;
	}

	public Boolean getClassificacaoBibliografica() {
		return classificacaoBibliografica;
	}

	public void setClassificacaoBibliografica(Boolean classificacaoBibliografica) {
		this.classificacaoBibliografica = classificacaoBibliografica;
	}

	public Boolean getEditora() {
		return editora;
	}

	public void setEditora(Boolean editora) {
		this.editora = editora;
	}

	public Boolean getExemplar() {
		return exemplar;
	}

	public void setExemplar(Boolean exemplar) {
		this.exemplar = exemplar;
	}

	public Boolean getItemPublicacaoAutor() {
		return itemPublicacaoAutor;
	}

	public void setItemPublicacaoAutor(Boolean itemPublicacaoAutor) {
		this.itemPublicacaoAutor = itemPublicacaoAutor;
	}

	public Boolean getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Boolean publicacao) {
		this.publicacao = publicacao;
	}

	public Boolean getRecursosAudioVisuais() {
		return recursosAudioVisuais;
	}

	public void setRecursosAudioVisuais(Boolean recursosAudioVisuais) {
		this.recursosAudioVisuais = recursosAudioVisuais;
	}

	public Boolean getRegistroSaidaAcervo() {
		return registroSaidaAcervo;
	}

	public void setRegistroSaidaAcervo(Boolean registroSaidaAcervo) {
		this.registroSaidaAcervo = registroSaidaAcervo;
	}

	// Menu Basico
	public Boolean getMenuBasico() {
		if (getArtefatoAjuda() || getCidade() || getPais() || getEstado() || getConfiguracoes() || getTipoDocumento() || getFeriado() || getTipoDocumento() || getControleConcorrenciaHorarioTurma() || getConfiguracaoSeiGsuite() || getConfiguracaoSeiBlackboard()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getArtefatoAjuda() {
		return artefatoAjuda;
	}

	public Boolean getCidade() {
		return cidade;
	}

	public void setCidade(Boolean cidade) {
		this.cidade = cidade;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Boolean getPais() {
		return pais;
	}

	public void setPais(Boolean pais) {
		this.pais = pais;
	}

	public void setArtefatoAjuda(Boolean artefatoAjuda) {
		this.artefatoAjuda = artefatoAjuda;
	}

	// Menu Arquitetura
	public Boolean getMenuArquitetura() {
		if (getPerfilAcesso() || getUsuario()) {
			return Boolean.TRUE;

		}
		return Boolean.FALSE;
	}

	public Boolean getPerfilAcesso() {
		return perfilAcesso;
	}

	public void setPerfilAcesso(Boolean perfilAcesso) {
		this.perfilAcesso = perfilAcesso;
	}

	public Boolean getUsuario() {
		return usuario;
	}

	public void setUsuario(Boolean usuario) {
		this.usuario = usuario;
	}

	// Menu Administrativo
	public Boolean getSubMenuConfiguracaoAdministrativo() {
		if (getVisao()) {
			return true;
		}
		return false;
	}

	public Boolean getSubMenuAdministrativo() {
		if (getUnidadeEnsino() || getDepartamento() || getCargo() || getFraseInspiracao() || getFuncionario() || getComunicacaoInterna() || getTipoMidiaCaptacao() || getGrupoDestinatarios() || getTipoMidiaCaptacao() || getCampanha()
				|| getAgrupamentoUnidadeEnsino()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMenuAdministrativo() {
		if (getSubMenuAdministrativo()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuRelatorioAdministrativo() {
		if (getSenhaAlunoProfessorRel()) {
			return Boolean.TRUE;

		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuRelatorioBancoCurriculum() {
		if (getCandidatosParaVagaRel() || getEmpresaVagaBancoTalentoRel() || getEmpresaPorVagasRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getCampanhaMarketing() {
		return campanhaMarketing;
	}

	public void setCampanhaMarketing(Boolean campanhaMarketing) {
		this.campanhaMarketing = campanhaMarketing;
	}

	public Boolean getCargo() {
		return cargo;
	}

	public void setCargo(Boolean cargo) {
		this.cargo = cargo;
	}

	public Boolean getFraseInspiracao() {
		return fraseInspiracao;
	}

	public void setFraseInspiracao(Boolean fraseInspiracao) {
		this.fraseInspiracao = fraseInspiracao;
	}

	public Boolean getComunicacaoInterna() {
		return comunicacaoInterna;
	}

	public void setComunicacaoInterna(Boolean comunicacaoInterna) {
		this.comunicacaoInterna = comunicacaoInterna;
	}

	public Boolean getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Boolean departamento) {
		this.departamento = departamento;
	}

	public Boolean getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getTipoMidiaCaptacao() {
		return tipoMidiaCaptacao;
	}

	public void setTipoMidiaCaptacao(Boolean tipoMidiaCaptacao) {
		this.tipoMidiaCaptacao = tipoMidiaCaptacao;
	}

	public Boolean getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Boolean unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	public Boolean getAgrupamentoUnidadeEnsino() {
		return agrupamentoUnidadeEnsino;
	}
	
	public void setAgrupamentoUnidadeEnsino(Boolean agrupamentoUnidadeEnsino) {
		this.agrupamentoUnidadeEnsino = agrupamentoUnidadeEnsino;
	}
	
	public Boolean getAgrupamentoUnidadeEnsinoFavorito() {
		return agrupamentoUnidadeEnsinoFavorito;
	}
	
	public void setAgrupamentoUnidadeEnsinoFavorito(Boolean agrupamentoUnidadeEnsinoFavorito) {
		this.agrupamentoUnidadeEnsinoFavorito = agrupamentoUnidadeEnsinoFavorito;
	}

	public Boolean getVisao() {
		return visao;
	}

	public void setVisao(Boolean visao) {
		this.visao = visao;
	}

	// Menu Academico
	public Boolean getMenuAcademico() {
		if (getSubMenuAluno() || getSubMenuCurso() || getSubMenuMatricula() || getSubMenuFormatura() || getMenuProtocolo() || getSubMenuFinanceiroAcademico() || getSubMenuSecretaria() || getSubMenuMonografia()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuFormatura() {
		if (getExpedicaoDiploma() || getColacaoGrau() || getProgramacaoFormatura() || getRegistroPresencaColacaoGrau() || getControleLivroRegistroDiploma()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuMonografia() {
		if (getTrabalhoConclusaoCurso() || getCoordenadorTCC()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuAluno() {
		if (getAluno()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuCurso() {
		if (getTurno() || getDisciplina() || getTurma() || getVagaTurma() || getCurso() || getAreaConhecimento() || getProgramacaoAula() || getTitulacaoCurso() || getUploadArquivosComuns() || getUpload() || getLogTurma()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuContabil() {
		if (getPlanoConta() || getConfiguracaoContabil() || getIntegracaoContabil() || getLayoutIntegracao() || getDRE() || getTipoEventoContabil() || getHistoricoContabil()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuNotaFiscalBasico() {
		if (getNaturezaOperacao()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuNotaFiscalSaida() {
		if (getNotaFiscalSaida() || getInutilizacaoNotaFiscal() || getConfiguracaoNotaFiscal()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuNotaFiscalGinfes() {
		if (getIntegracaoGinfesAluno() || getIntegracaoGinfesCurso() || getValoresCursoGinfes()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuNotaFiscalEntrada() {
		if (getImposto() || getNotaFiscalEntrada()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuNotaFiscalRelatoriosEntrada() {
		if (getImpostosRetidosNotaFiscalEntradaRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuMatricula() {
		if (getConsultorMatricula() || getProcessoMatricula() || getMatricula() || getLogMatricula() || getPortadorDiploma() || getPeriodoLetivoAtivoUnidadeEnsinoCurso() || getInclusaoExclusaoDisciplina() || getConfirmacaoPreMatricula() || getImpressaoContrato() || getMapaReposicao()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuFinanceiroAcademico() {
		if (getCategoriaDesconto() || getAtualizacaoVencimentos() || getPlanoDesconto() || getDescontoProgressivo() || getPlanoFinanceiroCurso() || getTextoPadrao() || getChancela() || getPlanoDescontoInclusaoDisciplina() || getPlanoFinanceiroReposicao() || getCentroResultado()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean getSubMenuGeracaoArquivos() {
		if (getCenso() || getSetransp()) {
			return true;
		}
		return false;
	}

	public Boolean getSubMenuRelatorioAcademico() {

		if (getProcessoMatriculaRel() || getDisciplinaRel() || getHistoricoAlunoRel() || getGradeCurricularAlunoRel() || getHistoricoTurmaRel() || getDiarioRel() || getEspelhoRel() || getDeclaracaoSetranspRel() || getAtaProva() || getComunicadoDebitoDocumentosAlunoRel() || getDeclaracaoAbandonoCursoRel() || getEtiquetaProvaRel() || getDeclaracaoConclusaoCursoRel() || getDeclaracaoCancelamentoMatriculaRel() || getCartaAlunoRel() || getDeclaracaoAprovacaoVestRel() || getDeclaracaoTransferenciaRel() || getControleDocumentacaoAlunoRel() || getAlunosCursoRel() || getAlunosPorUnidadeCursoTurmaRel() || getReposicaoRel() || getProfessorRel() || getAlunosMatriculadosGeralRel() || getAlunosNaoRenovaramRel() || getMediaDescontoAlunoRel() || getMediaDescontoTurmaRel() || getDeclaracaoFrequenciaRel() || getBoletimAcademicoRel() || getQuadroMatriculaRel() || getOcorrenciasAlunosRel() || getQuadroAlunosAtivosInativosRel() || getEstatisticaMatriculaRel() || getAlunoNaoCursouDisciplinaRel() || getCertificadoCursoExtensaoRel()
				|| getMapaNotaAlunoPorTurma() || getMapaNotasDisciplinaAlunos() || getAlunoDescontoDesempenhoRel() || getAgendaProfessorRel() || getDisciplinasGradeRel() || getTermoCompromissoDocumentacaoPendenteRel() || getRelacaoEnderecoAlunoRel() || getLivroRegistroRel() || getFichaAtualizacaoCadastralRel() || getMeritoAcademicoRel() || getPossiveisFormandosRel() || getAlunosProUniRel() || getAlunosNaoRenovaramRel() || getAlunosReprovadosRel() || getMapaSituacaoAlunoRel() || getHorarioDaTurmaRel() || getHorarioAlunoRel() || getPerfilTurmaRel() || getEmailTurmaRel() || getAniversariantesDoMesRel() || getRequerimentoRel() || getFaltasAlunosRel() || getRegistroAulaLancadaNaoLancadaRel() || getEntregaBoletosRel() || getMapaNotaAlunoPorTurmaRel() || getMapaNotasDisciplinaAlunosRel() || getDisciplinaRel() || getDisciplinasGradeRel() || getEntregaBoletosRel() || getFaltasAlunosRel() || getAgendaProfessorRel() || getCronogramaDeAulasRel() || getHorarioDaTurmaRel() || getHorarioAlunoRel()
				|| getCertificadoCursoExtensaoRel() || getDeclaracaoPasseEstudantilRel() || getDeclaracaoSetranspRel() || getDeclaracaoFrequenciaRel() || getDeclaracaoAbandonoCursoRel() || getDeclaracaoConclusaoCursoRel() || getDeclaracaoConclusaoCursoRel() || getDeclaracaoTransferenciaRel() || getTermoCompromissoDocumentacaoPendenteRel() || getCartaAlunoRel() || getBoletimAcademicoRel() || getComunicadoDebitoDocumentosAlunoRel() || getControleDocumentacaoAlunoRel() || getAniversariantesDoMesRel() || getPerfilTurmaRel() || getEmailTurmaRel() || getRelacaoEnderecoAlunoRel() || getLivroRegistroRel() || getFichaAtualizacaoCadastralRel() || getGradeCurricularAlunoRel() || getEnvelopeRel() || getEnvelopeRequerimentoRel() || getEntregaBoletosRel() || getAberturaTurmaRel() || getAlunoNaoCursouDisciplinaRel() || getAlunosMatriculadosGeralRel() || getAlunosNaoRenovaramRel() || getUploadProfessorRel() || getAlunosPorUnidadeCursoTurmaRel() || getReposicaoRel() || getProfessorRel() || getAlunosProUniRel()
				|| getAlunosReprovadosRel() || getRequerimentoRel() || getMapaSituacaoAlunoRel() || getOcorrenciasAlunosRel() || getEstatisticaMatriculaRel() || getMeritoAcademicoRel() || getProcessoMatriculaRel() || getQuadroAlunosAtivosInativosRel() || getQuadroMatriculaRel() || getAlunosPorUnidadeCursoTurmaRel() || getProfessorRel() || getPossiveisFormandosRel() || getControleVagaRel()) {

			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuRelatorioProfessor() {
		if (getDiarioRel() || getEspelhoRel() || getHistoricoTurmaRel() || getHistoricoAlunoRel() || getPerfilTurmaRel() || getAtaProva()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getSubMenuRelatorioCoordenador() {
		if (getDiarioRel() || getEspelhoRel() || getHistoricoTurmaRel() || getAtaProva() || getCronogramaDeAulasRel() || getHorarioDaTurmaRel() || getFrequenciaAlunoRel() || getPerfilTurmaRel() || getDownloadRel() || getRelatorioAtividadeExtraClasseProfessorVisaoCoordenador() || getMeritoAcademicoRel() || getAlunoBaixaFrequenciaRel() || getUploadProfessorRel() || getRegistroAulaLancadaNaoLancadaRel()) {
			return Boolean.TRUE;
		}
		return Boolean.TRUE;
	}

	public Boolean getSubMenuProfessor() {
		if (getHistorico() || getRegistroAula() || getAtaProva()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMeritoAcademicoRel() {
		return meritoAcademicoRel;
	}

	public void setMeritoAcademicoRel(Boolean meritoAcademicoRel) {
		this.meritoAcademicoRel = meritoAcademicoRel;
	}

	public Boolean getPossiveisFormandosRel() {
		return possiveisFormandosRel;
	}

	public void setPossiveisFormandosRel(Boolean possiveisFormandosRel) {
		this.possiveisFormandosRel = possiveisFormandosRel;
	}

	public Boolean getControleVagaRel() {
		return controleVagaRel;
	}

	public void setControleVagaRel(Boolean controleVagaRel) {
		this.controleVagaRel = controleVagaRel;
	}

	public Boolean getPerfilTurmaRel() {
		return perfilTurmaRel;
	}

	public void setPerfilTurmaRel(Boolean perfilTurmaRel) {
		this.perfilTurmaRel = perfilTurmaRel;
	}

	public Boolean getControleLivroRegistroDiploma() {
		return ControleLivroRegistroDiploma;
	}

	public void setControleLivroRegistroDiploma(Boolean ControleLivroRegistroDiploma) {
		this.ControleLivroRegistroDiploma = ControleLivroRegistroDiploma;
	}

	public Boolean getRegistroAula() {
		return RegistroAula;
	}

	public void setRegistroAula(Boolean RegistroAula) {
		this.RegistroAula = RegistroAula;
	}

	public Boolean getProfessorMinistrouAulaTurma() {
		return professorMinistrouAulaTurma;
	}

	public void setProfessorMinistrouAulaTurma(Boolean professorMinistrouAulaTurma) {
		this.professorMinistrouAulaTurma = professorMinistrouAulaTurma;
	}

	public Boolean getAluno() {
		return aluno;
	}

	public void setAluno(Boolean aluno) {
		this.aluno = aluno;
	}

	public Boolean getCancelamento() {
		return cancelamento;
	}

	public void setCancelamento(Boolean cancelamento) {
		this.cancelamento = cancelamento;
	}

	public Boolean getCurso() {
		return curso;
	}

	public void setCurso(Boolean curso) {
		this.curso = curso;
	}

	public Boolean getDescontoProgressivo() {
		return descontoProgressivo;
	}

	public void setDescontoProgressivo(Boolean descontoProgressivo) {
		this.descontoProgressivo = descontoProgressivo;
	}

	public Boolean getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Boolean disciplina) {
		this.disciplina = disciplina;
	}

	public Boolean getDisciplinaFavorito() {
		return disciplinaFavorito;
	}

	public void setDisciplinaFavorito(Boolean disciplinaFavorito) {
		this.disciplinaFavorito = disciplinaFavorito;
	}

	public Boolean getFrequenciaAula() {
		return frequenciaAula;
	}

	public void setFrequenciaAula(Boolean frequenciaAula) {
		this.frequenciaAula = frequenciaAula;
	}

	public Boolean getHistorico() {
		return historico;
	}

	public void setHistorico(Boolean historico) {
		this.historico = historico;
	}

	public Boolean getMatricula() {
		return matricula;
	}

	public void setMatricula(Boolean matricula) {
		this.matricula = matricula;
	}

	public Boolean getLogMatricula() {
		return getLogmatricula();
	}

	public void setLogMatricula(Boolean logmatricula) {
		this.setLogmatricula(logmatricula);
	}

	public Boolean getPlanoDesconto() {
		return planoDesconto;
	}

	public void setPlanoDesconto(Boolean planoDesconto) {
		this.planoDesconto = planoDesconto;
	}

	public Boolean getPlanoFinanceiroAluno() {
		return planoFinanceiroAluno;
	}

	public void setPlanoFinanceiroAluno(Boolean planoFinanceiroAluno) {
		this.planoFinanceiroAluno = planoFinanceiroAluno;
	}

	public Boolean getPlanoFinanceiroCurso() {
		return planoFinanceiroCurso;
	}

	public void setPlanoFinanceiroCurso(Boolean planoFinanceiroCurso) {
		this.planoFinanceiroCurso = planoFinanceiroCurso;
	}

	public Boolean getCentroResultado() {
		return centroResultado;
	}

	public void setCentroResultado(Boolean centroResultado) {
		this.centroResultado = centroResultado;
	}

	public Boolean getCentroResultadoFavorito() {
		return centroResultadoFavorito;
	}

	public void setCentroResultadoFavorito(Boolean centroResultadoFavorito) {
		this.centroResultadoFavorito = centroResultadoFavorito;
	}

	public Boolean getTextoPadrao() {
		return textoPadrao;
	}

	public void setTextoPadrao(Boolean textoPadrao) {
		this.textoPadrao = textoPadrao;
	}

	public Boolean getProcessoMatricula() {
		return processoMatricula;
	}

	public void setProcessoMatricula(Boolean processoMatricula) {
		this.processoMatricula = processoMatricula;
	}

	public Boolean getProfessor() {
		return professor;
	}

	public void setProfessor(Boolean professor) {
		this.professor = professor;
	}

	public Boolean getProgramacaoAula() {
		return programacaoAula;
	}

	public void setProgramacaoAula(Boolean programacaoAula) {
		this.programacaoAula = programacaoAula;
	}

	public Boolean getTrancamento() {
		return trancamento;
	}

	public void setTrancamento(Boolean trancamento) {
		this.trancamento = trancamento;
	}

	public Boolean getTransferenciaEntrada() {
		return transferenciaEntrada;
	}

	public void setTransferenciaEntrada(Boolean transferenciaEntrada) {
		this.transferenciaEntrada = transferenciaEntrada;
	}

	public Boolean getAproveitamentoDisciplina() {
		return aproveitamentoDisciplina;
	}

	public void setAproveitamentoDisciplina(Boolean aproveitamentoDisciplina) {
		this.aproveitamentoDisciplina = aproveitamentoDisciplina;
	}

	public Boolean getTransferenciaSaida() {
		return transferenciaSaida;
	}

	public void setTransferenciaSaida(Boolean transferenciaSaida) {
		this.transferenciaSaida = transferenciaSaida;
	}

	public Boolean getUpload() {
		return upload;
	}

	public void setUpload(Boolean upload) {
		this.upload = upload;
	}

	public Boolean getUploadFavorito() {
		return uploadFavorito;
	}

	public void setUploadFavorito(Boolean uploadFavorito) {
		this.uploadFavorito = uploadFavorito;
	}

	public Boolean getTurma() {
		return turma;
	}

	public void setTurma(Boolean turma) {
		this.turma = turma;
	}

	public Boolean getVagaTurma() {
		return vagaTurma;
	}

	public Boolean getIntegracaoMestreGR() {
		return integracaoMestreGR;
	}
	
	public void setIntegracaoMestreGR(Boolean integracaoMestreGR) {
		this.integracaoMestreGR = integracaoMestreGR;
	}

	public Boolean getIntegracaoMestreGRFavorito() {
		return integracaoMestreGRFavorito;
	}
	
	public void setIntegracaoMestreGRFavorito(Boolean integracaoMestreGRFavorito) {
		this.integracaoMestreGRFavorito = integracaoMestreGRFavorito;
	}

	public void setVagaTurma(Boolean vagaTurma) {
		this.vagaTurma = vagaTurma;
	}

	public Boolean getTurno() {
		return turno;
	}

	public void setTurno(Boolean turno) {
		this.turno = turno;
	}

	public Boolean getAreaConhecimento() {
		return areaConhecimento;
	}

	public Boolean getAreaConhecimentoFavorito() {
		return areaConhecimentoFavorito;
	}

	public Boolean getPortadorDiploma() {
		return portadorDiploma;
	}

	public void setPortadorDiploma(Boolean portadorDiploma) {
		this.portadorDiploma = portadorDiploma;
	}

	public void setAreaConhecimento(Boolean areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}

	public void setAreaConhecimentoFavorito(Boolean areaConhecimentoFavorito) {
		this.areaConhecimentoFavorito = areaConhecimentoFavorito;
	}

	public Boolean getCampanhaMarketingRel() {
		return campanhaMarketingRel;
	}

	public void setCampanhaMarketingRel(Boolean campanhaMarketingRel) {
		this.campanhaMarketingRel = campanhaMarketingRel;
	}

	public Boolean getContaPagarResumidaPorDataRel() {
		return contaPagarResumidaPorDataRel;
	}

	public void setContaPagarResumidaPorDataRel(Boolean contaPagarResumidaPorDataRel) {
		this.contaPagarResumidaPorDataRel = contaPagarResumidaPorDataRel;
	}

	public Boolean getContaReceberRel() {
		return contaReceberRel;
	}

	public void setContaReceberRel(Boolean contaReceberRel) {
		this.contaReceberRel = contaReceberRel;
	}

	public Boolean getTermoReconhecimentoDividaRel() {
		return termoReconhecimentoDividaRel;
	}

	public void setTermoReconhecimentoDividaRel(Boolean termoReconhecimentoDividaRel) {
		this.termoReconhecimentoDividaRel = termoReconhecimentoDividaRel;
	}

	public Boolean getDiarioRel() {
		return diarioRel;
	}

	public void setDiarioRel(Boolean diarioRel) {
		this.diarioRel = diarioRel;
	}

	public Boolean getEspelhoRel() {
		return espelhoRel;
	}

	public void setEspelhoRel(Boolean espelhoRel) {
		this.espelhoRel = espelhoRel;
	}

	public Boolean getDisciplinaRel() {
		return disciplinaRel;
	}

	public void setDisciplinaRel(Boolean disciplinaRel) {
		this.disciplinaRel = disciplinaRel;
	}

	public Boolean getHistoricoTurmaRel() {
		return historicoTurmaRel;
	}

	public void setHistoricoTurmaRel(Boolean historicoTurmaRel) {
		this.historicoTurmaRel = historicoTurmaRel;
	}

	public Boolean getPagamentoRel() {
		return pagamentoRel;
	}

	public void setPagamentoRel(Boolean pagamentoRel) {
		this.pagamentoRel = pagamentoRel;
	}

	public Boolean getPerfilSocioEconomicoRel() {
		return perfilSocioEconomicoRel;
	}

	public void setPerfilSocioEconomicoRel(Boolean perfilSocioEconomicoRel) {
		this.perfilSocioEconomicoRel = perfilSocioEconomicoRel;
	}

	public Boolean getProcSeletivoInscricoesRel() {
		return procSeletivoInscricoesRel;
	}

	public void setProcSeletivoInscricoesRel(Boolean procSeletivoInscricoesRel) {
		this.procSeletivoInscricoesRel = procSeletivoInscricoesRel;
	}

	public Boolean getProcSeletivoRel() {
		return procSeletivoRel;
	}

	public void setProcSeletivoRel(Boolean procSeletivoRel) {
		this.procSeletivoRel = procSeletivoRel;
	}

	public Boolean getProcessoMatriculaRel() {
		return processoMatriculaRel;
	}

	public void setProcessoMatriculaRel(Boolean processoMatriculaRel) {
		this.processoMatriculaRel = processoMatriculaRel;
	}

	public Boolean getRecebimentoRel() {
		return recebimentoRel;
	}

	public void setRecebimentoRel(Boolean recebimentoRel) {
		this.recebimentoRel = recebimentoRel;
	}

	public Boolean getUnidadeEnsinoRel() {
		return unidadeEnsinoRel;
	}

	public void setUnidadeEnsinoRel(Boolean unidadeEnsinoRel) {
		this.unidadeEnsinoRel = unidadeEnsinoRel;
	}

	public Boolean getMenuRelatorio() {
		if (getSubMenuRelatorioAcademico() || getSubMenuRelatorioAdministrativo() || getSubMenuRelatorioFinanceiro() || getSubMenuRelatorioProfessor() || getMenuProcSeletivoRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getPeriodoLetivoAtivoUnidadeEnsinoCurso() {
		return periodoLetivoAtivoUnidadeEnsinoCurso;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCurso(Boolean periodoLetivoAtivoUnidadeEnsinoCurso) {
		this.periodoLetivoAtivoUnidadeEnsinoCurso = periodoLetivoAtivoUnidadeEnsinoCurso;
	}

	public Boolean getCotacao() {
		return cotacao;
	}

	public void setCotacao(Boolean cotacao) {
		this.cotacao = cotacao;
	}

	public Boolean getTramiteCotacaoCompra() {
		return tramiteCotacaoCompra;
	}

	public void setTramiteCotacaoCompra(Boolean tramiteCotacaoCompra) {
		this.tramiteCotacaoCompra = tramiteCotacaoCompra;
	}

	public Boolean getTramiteCotacaoCompraFavorito() {
		return tramiteCotacaoCompraFavorito;
	}

	public void setTramiteCotacaoCompraFavorito(Boolean tramiteCotacaoCompraFavorito) {
		this.tramiteCotacaoCompraFavorito = tramiteCotacaoCompraFavorito;
	}

	public Boolean getDevolucaoCompra() {
		return devolucaoCompra;
	}

	public void setDevolucaoCompra(Boolean devolucaoCompra) {
		this.devolucaoCompra = devolucaoCompra;
	}

	public Boolean getEntregaRequisicao() {
		return entregaRequisicao;
	}

	public void setEntregaRequisicao(Boolean entregaRequisicao) {
		this.entregaRequisicao = entregaRequisicao;
	}

	public Boolean getRecebimentoCompra() {
		return recebimentoCompra;
	}

	public void setRecebimentoCompra(Boolean recebimentoCompra) {
		this.recebimentoCompra = recebimentoCompra;
	}

	public Boolean getRequisicao() {
		return requisicao;
	}

	public void setRequisicao(Boolean requisicao) {
		this.requisicao = requisicao;
	}

	public Boolean getMapaRequisicao() {
		return mapaRequisicao;
	}

	public void setMapaRequisicao(Boolean mapaRequisicao) {
		this.mapaRequisicao = mapaRequisicao;
	}

	public Boolean getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(Boolean condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public Boolean getMovimentacaoEstoque() {
		return movimentacaoEstoque;
	}

	public void setMovimentacaoEstoque(Boolean movimentacaoEstoque) {
		this.movimentacaoEstoque = movimentacaoEstoque;
	}

	public Boolean getNegociacaoContaReceber() {
		return negociacaoContaReceber;
	}

	public void setNegociacaoContaReceber(Boolean negociacaoContaReceber) {
		this.negociacaoContaReceber = negociacaoContaReceber;
	}

	public Boolean getCondicaoNegociacao() {
		return condicaoNegociacao;
	}

	public void setCondicaoNegociacao(Boolean condicaoNegociacao) {
		this.condicaoNegociacao = condicaoNegociacao;
	}

	public Boolean getPerfilEconomico() {
		return perfilEconomico;
	}

	public void setPerfilEconomico(Boolean perfilEconomico) {
		this.perfilEconomico = perfilEconomico;
	}

	public Boolean getHistoricoAlunoRel() {
		return historicoAlunoRel;
	}

	public void setHistoricoAlunoRel(Boolean historicoAlunoRel) {
		this.historicoAlunoRel = historicoAlunoRel;
	}

	public Boolean getMenuAvaliacaoInstitucionalRelatorio() {
		if (getAvaliacaoInstitucionalRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getMenuAvaliacaoInstitucional() {
		if (getAvaliacaoInstitucional() || getPerguntaAvaliacaoInstitucional() || getQuestionarioAvaliacaoInstitucional() || getAvaliacaoInstitucionalPresencialResposta()) {
			return true;
		}
		return false;
	}

	public Boolean getMenuAvaliacaoInstitucionalRel() {
		if (getAvaliacaoInstitucionalRel()) {
			return true;
		}
		return false;
	}

	public Boolean getAvaliacaoInstitucional() {
		return avaliacaoInstitucional;
	}

	public void setAvaliacaoInstitucional(Boolean avaliacaoInstitucional) {
		this.avaliacaoInstitucional = avaliacaoInstitucional;
	}

	public Boolean getAvaliacaoInstitucionalRel() {
		return avaliacaoInstitucionalRel;
	}

	public void setAvaliacaoInstitucionalRel(Boolean avaliacaoInstitucionalRel) {
		this.avaliacaoInstitucionalRel = avaliacaoInstitucionalRel;
	}

	/**
	 * @return the negociacaoRecebimento
	 */
	public Boolean getNegociacaoRecebimento() {
		return negociacaoRecebimento;
	}

	/**
	 * @param negociacaoRecebimento the negociacaoRecebimento to set
	 */
	public void setNegociacaoRecebimento(Boolean negociacaoRecebimento) {
		this.negociacaoRecebimento = negociacaoRecebimento;
	}

	public Boolean getCheque() {
		return cheque;
	}

	public void setCheque(Boolean cheque) {
		this.cheque = cheque;
	}

	public Boolean getDevolucaoCheque() {
		return devolucaoCheque;
	}

	public void setDevolucaoCheque(Boolean devolucaoCheque) {
		this.devolucaoCheque = devolucaoCheque;
	}

	public Boolean getFluxoCaixa() {
		return fluxoCaixa;
	}

	public void setFluxoCaixa(Boolean fluxoCaixa) {
		this.fluxoCaixa = fluxoCaixa;
	}

	public Boolean getConciliacaoContaCorrente() {
		return conciliacaoContaCorrente;
	}

	public void setConciliacaoContaCorrente(Boolean conciliacaoContaCorrente) {
		this.conciliacaoContaCorrente = conciliacaoContaCorrente;
	}

	public Boolean getConciliacaoContaCorrenteFavorito() {
		return conciliacaoContaCorrenteFavorito;
	}

	public void setConciliacaoContaCorrenteFavorito(Boolean conciliacaoContaCorrenteFavorito) {
		this.conciliacaoContaCorrenteFavorito = conciliacaoContaCorrenteFavorito;
	}

	public Boolean getPixContaCorrente() {
		return pixContaCorrente;
	}

	public void setPixContaCorrente(Boolean pixContaCorrente) {
		this.pixContaCorrente = pixContaCorrente;
	}

	public Boolean getPixContaCorrenteFavorito() {
		return pixContaCorrenteFavorito;
	}

	public void setPixContaCorrenteFavorito(Boolean pixContaCorrenteFavorito) {
		this.pixContaCorrenteFavorito = pixContaCorrenteFavorito;
	}

	public Boolean getParametrizarOperacoesAutomaticasConciliacaoItem() {
		return parametrizarOperacoesAutomaticasConciliacaoItem;
	}

	public void setParametrizarOperacoesAutomaticasConciliacaoItem(Boolean parametrizarOperacoesAutomaticasConciliacaoItem) {
		this.parametrizarOperacoesAutomaticasConciliacaoItem = parametrizarOperacoesAutomaticasConciliacaoItem;
	}

	public Boolean getParametrizarOperacoesAutomaticasConciliacaoItemFavorito() {
		return parametrizarOperacoesAutomaticasConciliacaoItemFavorito;
	}

	public void setParametrizarOperacoesAutomaticasConciliacaoItemFavorito(Boolean parametrizarOperacoesAutomaticasConciliacaoItemFavorito) {
		this.parametrizarOperacoesAutomaticasConciliacaoItemFavorito = parametrizarOperacoesAutomaticasConciliacaoItemFavorito;
	}

	public Boolean getPlanoOrcamentario() {
		return planoOrcamentario;
	}

	public void setPlanoOrcamentario(Boolean planoOrcamentario) {
		this.planoOrcamentario = planoOrcamentario;
	}

	public Boolean getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
	}

	public void setMovimentacaoFinanceira(Boolean movimentacaoFinanceira) {
		this.movimentacaoFinanceira = movimentacaoFinanceira;
	}

	public Boolean getMapaLancamentoFuturo() {
		return mapaLancamentoFuturo;
	}

	public void setMapaLancamentoFuturo(Boolean mapaLancamentoFuturo) {
		this.mapaLancamentoFuturo = mapaLancamentoFuturo;
	}

	public Boolean getMapaReposicao() {
		return mapaReposicao;
	}

	public void setMapaReposicao(Boolean mapaReposicao) {
		this.mapaReposicao = mapaReposicao;
	}

	public Boolean getMapaCotacao() {
		return mapaCotacao;
	}

	public void setMapaCotacao(Boolean mapaCotacao) {
		this.mapaCotacao = mapaCotacao;
	}

	/**
	 * @return the provisaoCusto
	 */
	public Boolean getProvisaoCusto() {
		return provisaoCusto;
	}

	/**
	 * @param provisaoCusto the provisaoCusto to set
	 */
	public void setProvisaoCusto(Boolean provisaoCusto) {
		this.provisaoCusto = provisaoCusto;
	}

	/**
	 * @return the pagamentoResumidoRel
	 */
	public Boolean getPagamentoResumidoRel() {
		return pagamentoResumidoRel;
	}

	/**
	 * @param pagamentoResumidoRel the pagamentoResumidoRel to set
	 */
	public void setPagamentoResumidoRel(Boolean pagamentoResumidoRel) {
		this.pagamentoResumidoRel = pagamentoResumidoRel;
	}

	/**
	 * @return the fluxoCaixaRel
	 */
	public Boolean getFluxoCaixaRel() {
		return fluxoCaixaRel;
	}

	/**
	 * @param fluxoCaixaRel the fluxoCaixaRel to set
	 */
	public void setFluxoCaixaRel(Boolean fluxoCaixaRel) {
		this.fluxoCaixaRel = fluxoCaixaRel;
	}

	/**
	 * @return the contaPagarResumidaPorFornecedorRel
	 */
	public Boolean getContaPagarResumidaPorFornecedorRel() {
		return contaPagarResumidaPorFornecedorRel;
	}

	/**
	 * @param contaPagarResumidaPorFornecedorRel the contaPagarResumidaPorFornecedorRel to set
	 */
	public void setContaPagarResumidaPorFornecedorRel(Boolean contaPagarResumidaPorFornecedorRel) {
		this.contaPagarResumidaPorFornecedorRel = contaPagarResumidaPorFornecedorRel;
	}

	/**
	 * @return the endereco
	 */
	public Boolean getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(Boolean endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the bairro
	 */
	public Boolean getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(Boolean bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the contaPagarRel
	 */
	public Boolean getContaPagarRel() {
		return contaPagarRel;
	}

	/**
	 * @param contaPagarRel the contaPagarRel to set
	 */
	public void setContaPagarRel(Boolean contaPagarRel) {
		this.contaPagarRel = contaPagarRel;
	}

	/**
	 * @return the modeloBoleto
	 */
	public Boolean getModeloBoleto() {
		return modeloBoleto;
	}

	/**
	 * @param modeloBoleto the modeloBoleto to set
	 */
	public void setModeloBoleto(Boolean modeloBoleto) {
		this.modeloBoleto = modeloBoleto;
	}

	/**
	 * @return the configuracoes
	 */
	public Boolean getConfiguracoes() {
		return configuracoes;
	}

	/**
	 * @param configuracoes the configuracoes to set
	 */
	public void setConfiguracoes(Boolean configuracoes) {
		this.configuracoes = configuracoes;
	}

	public Boolean getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Boolean emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Boolean getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Boolean catalogo) {
		this.catalogo = catalogo;
	}

	public Boolean getTitulo() {
		return titulo;
	}

	public void setTitulo(Boolean titulo) {
		this.titulo = titulo;
	}

	public Boolean getRegistroEntradaAcervo() {
		return registroEntradaAcervo;
	}

	public void setRegistroEntradaAcervo(Boolean registroEntradaAcervo) {
		this.registroEntradaAcervo = registroEntradaAcervo;
	}

	public Boolean getSecao() {
		return secao;
	}

	public void setSecao(Boolean secao) {
		this.secao = secao;
	}

	public Boolean getAssinaturaPeriodico() {
		return assinaturaPeriodico;
	}

	public void setAssinaturaPeriodico(Boolean assinaturaPeriodico) {
		this.assinaturaPeriodico = assinaturaPeriodico;
	}

	public Boolean getConfiguracaoBiblioteca() {
		return configuracaoBiblioteca;
	}

	public void setConfiguracaoBiblioteca(Boolean configuracaoBiblioteca) {
		this.configuracaoBiblioteca = configuracaoBiblioteca;
	}

	public Boolean getTipoAutoria() {
		return tipoAutoria;
	}

	public void setTipoAutoria(Boolean tipoAutoria) {
		this.tipoAutoria = tipoAutoria;
	}

	public Boolean getTipoCatalogo() {
		return tipoCatalogo;
	}

	public void setTipoCatalogo(Boolean tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}

	/**
	 * @return the abonoFalta
	 */
	public Boolean getAbonoFalta() {
		return abonoFalta;
	}

	/**
	 * @param abonoFalta the abonoFalta to set
	 */
	public void setAbonoFalta(Boolean abonoFalta) {
		this.abonoFalta = abonoFalta;
	}

	/**
	 * @param declaracaoSetranspRel the declaracaoSetranspRel to set
	 */
	public void setDeclaracaoSetranspRel(Boolean declaracaoSetranspRel) {
		this.declaracaoSetranspRel = declaracaoSetranspRel;
	}

	/**
	 * @return the declaracaoSetranspRel
	 */
	public Boolean getDeclaracaoSetranspRel() {
		return declaracaoSetranspRel;
	}

	/**
	 * @return the declaracaoAbandonoCursoRel
	 */
	public Boolean getDeclaracaoAbandonoCursoRel() {
		return declaracaoAbandonoCursoRel;
	}

	/**
	 * @param declaracaoAbandonoCursoRel the declaracaoAbandonoCursoRel to set
	 */
	public void setDeclaracaoAbandonoCursoRel(Boolean declaracaoAbandonoCursoRel) {
		this.declaracaoAbandonoCursoRel = declaracaoAbandonoCursoRel;
	}

	public Boolean getDeclaracaoConclusaoCursoRel() {
		return declaracaoConclusaoCursoRel;
	}

	public void setDeclaracaoConclusaoCursoRel(Boolean declaracaoConclusaoCursoRel) {
		this.declaracaoConclusaoCursoRel = declaracaoConclusaoCursoRel;
	}

	public Boolean getComunicadoDebitoDocumentosAlunoRel() {
		return comunicadoDebitoDocumentosAlunoRel;
	}

	public void setComunicadoDebitoDocumentosAlunoRel(Boolean comunicadoDebitoDocumentosAlunoRel) {
		this.comunicadoDebitoDocumentosAlunoRel = comunicadoDebitoDocumentosAlunoRel;
	}

	/**
	 * @return the declaracaoCancelamentoMatriculaRel
	 */
	public Boolean getDeclaracaoCancelamentoMatriculaRel() {
		return declaracaoCancelamentoMatriculaRel;
	}

	/**
	 * @param declaracaoCancelamentoMatriculaRel the declaracaoCancelamentoMatriculaRel to set
	 */
	public void setDeclaracaoCancelamentoMatriculaRel(Boolean declaracaoCancelamentoMatriculaRel) {
		this.declaracaoCancelamentoMatriculaRel = declaracaoCancelamentoMatriculaRel;
	}

	/**
	 * @return the cartaAlunoRel
	 */
	public Boolean getCartaAlunoRel() {
		return cartaAlunoRel;
	}

	/**
	 * @param cartaAlunoRel the cartaAlunoRel to set
	 */
	public void setCartaAlunoRel(Boolean cartaAlunoRel) {
		this.cartaAlunoRel = cartaAlunoRel;
	}

	/**
	 * @return the declaracaoTransferenciaRel
	 */
	public Boolean getDeclaracaoTransferenciaRel() {
		return declaracaoTransferenciaRel;
	}

	/**
	 * @param declaracaoTransferenciaRel the declaracaoTransferenciaRel to set
	 */
	public void setDeclaracaoTransferenciaRel(Boolean declaracaoTransferenciaRel) {
		this.declaracaoTransferenciaRel = declaracaoTransferenciaRel;
	}

	public Boolean getDeclaracaoAprovacaoVestRel() {
		return declaracaoAprovacaoVestRel;
	}

	public void setDeclaracaoAprovacaoVestRel(Boolean declaracaoAprovacaoVestRel) {
		this.declaracaoAprovacaoVestRel = declaracaoAprovacaoVestRel;
	}

	public Boolean getDebitoDocumentosPosRel() {
		return debitoDocumentosPosRel;
	}

	public void setDebitoDocumentosPosRel(Boolean debitoDocumentosPosRel) {
		this.debitoDocumentosPosRel = debitoDocumentosPosRel;
	}

	public Boolean getCenso() {
		return censo;
	}

	public void setCenso(Boolean censo) {
		this.censo = censo;
	}

	/**
	 * @return the recebimentoPorTurmaRel
	 */
	public Boolean getRecebimentoPorTurmaRel() {
		return recebimentoPorTurmaRel;
	}

	/**
	 * @param recebimentoPorTurmaRel the recebimentoPorTurmaRel to set
	 */
	public void setRecebimentoPorTurmaRel(Boolean recebimentoPorTurmaRel) {
		this.recebimentoPorTurmaRel = recebimentoPorTurmaRel;
	}

	public Boolean getPrestacaoContaRel() {
		return prestacaoContaRel;
	}

	/**
	 * @param recebimentoPorTurmaRel the recebimentoPorTurmaRel to set
	 */
	public void setPrestacaoContaRel(Boolean prestacaoContaRel) {
		this.prestacaoContaRel = prestacaoContaRel;
	}

	public Boolean getAlunosCursoRel() {
		return alunosCursoRel;
	}

	public void setAlunosCursoRel(Boolean alunosCursoRel) {
		this.alunosCursoRel = alunosCursoRel;
	}

	public Boolean getAlunosPorUnidadeCursoTurmaRel() {
		return alunosPorUnidadeCursoTurmaRel;
	}

	public void setAlunosPorUnidadeCursoTurmaRel(Boolean alunosPorUnidadeCursoTurmaRel) {
		this.alunosPorUnidadeCursoTurmaRel = alunosPorUnidadeCursoTurmaRel;
	}

	public Boolean getReposicaoRel() {
		return reposicaoRel;
	}

	public void setReposicaoRel(Boolean reposicaoRel) {
		this.reposicaoRel = reposicaoRel;
	}

	public Boolean getProfessorRel() {
		return professorRel;
	}

	public void setProfessorRel(Boolean professorRel) {
		this.professorRel = professorRel;
	}

	/**
	 * @return the controleDocumentacaoAlunoRel
	 */
	public Boolean getControleDocumentacaoAlunoRel() {
		return controleDocumentacaoAlunoRel;
	}

	/**
	 * @param controleDocumentacaoAlunoRel the controleDocumentacaoAlunoRel to set
	 */
	public void setControleDocumentacaoAlunoRel(Boolean controleDocumentacaoAlunoRel) {
		this.controleDocumentacaoAlunoRel = controleDocumentacaoAlunoRel;
	}

	public Boolean getContaReceberAlunosRel() {
		return contaReceberAlunosRel;
	}

	public void setContaReceberAlunosRel(Boolean contaReceberAlunosRel) {
		this.contaReceberAlunosRel = contaReceberAlunosRel;
	}

	public Boolean getMediaDescontoAlunoRel() {
		return mediaDescontoAlunoRel;
	}

	public void setMediaDescontoAlunoRel(Boolean mediaDescontoAlunoRel) {
		this.mediaDescontoAlunoRel = mediaDescontoAlunoRel;
	}

	public Boolean getMediaDescontoTurmaRel() {
		return mediaDescontoTurmaRel;
	}

	public void setMediaDescontoTurmaRel(Boolean mediaDescontoTurmaRel) {
		this.mediaDescontoTurmaRel = mediaDescontoTurmaRel;
	}

	public Boolean getControleCobranca() {
		return controleCobranca;
	}

	public void setControleCobranca(Boolean controleCobranca) {
		this.controleCobranca = controleCobranca;
	}

	public Boolean getControleMovimentacaoRemessa() {
		return controleMovimentacaoRemessa;
	}

	public void setControleMovimentacaoRemessa(Boolean controleMovimentacaoRemessa) {
		this.controleMovimentacaoRemessa = controleMovimentacaoRemessa;
	}

	/**
	 * @return the balanceteRel
	 */
	public Boolean getBalanceteRel() {
		return balanceteRel;
	}

	/**
	 * @param balanceteRel the balanceteRel to set
	 */
	public void setBalanceteRel(Boolean balanceteRel) {
		this.balanceteRel = balanceteRel;
	}

	/**
	 * @return the chequesRel
	 */
	public Boolean getChequesRel() {
		return chequesRel;
	}

	/**
	 * @param chequesRel the chequesRel to set
	 */
	public void setChequesRel(Boolean chequesRel) {
		this.chequesRel = chequesRel;
	}

	public Boolean getRegistroArquivo() {
		return registroArquivo;
	}

	public void setRegistroArquivo(Boolean registroArquivo) {
		this.registroArquivo = registroArquivo;
	}

	/**
	 * @return the tipoDocumento
	 */
	public Boolean getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(Boolean tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the declaracaoFrequenciaRel
	 */
	public Boolean getDeclaracaoFrequenciaRel() {
		return declaracaoFrequenciaRel;
	}

	/**
	 * @param declaracaoFrequenciaRel the declaracaoFrequenciaRel to set
	 */
	public void setDeclaracaoFrequenciaRel(Boolean declaracaoFrequenciaRel) {
		this.declaracaoFrequenciaRel = declaracaoFrequenciaRel;
	}

	/**
	 * @return the entregaDocumento
	 */
	public Boolean getEntregaDocumento() {
		return entregaDocumento;
	}

	/**
	 * @param entregaDocumento the entregaDocumento to set
	 */
	public void setEntregaDocumento(Boolean entregaDocumento) {
		this.entregaDocumento = entregaDocumento;
	}

	public Boolean getConfirmacaoPreMatricula() {
		return confirmacaoPreMatricula;
	}

	public void setConfirmacaoPreMatricula(Boolean confirmacaoPreMatricula) {
		this.confirmacaoPreMatricula = confirmacaoPreMatricula;
	}

	public Boolean getSetransp() {
		return setransp;
	}

	public void setSetransp(Boolean setransp) {
		this.setransp = setransp;
	}

	/**
	 * @return the colacaoGrau
	 */
	public Boolean getColacaoGrau() {
		return colacaoGrau;
	}

	public Boolean getFeriado() {
		return feriado;
	}

	public void setFeriado(Boolean feriado) {
		this.feriado = feriado;
	}

	/**
	 * @param colacaoGrau the colacaoGrau to set
	 */
	public void setColacaoGrau(Boolean colacaoGrau) {
		this.colacaoGrau = colacaoGrau;
	}

	/**
	 * @return the expedicaoDiploma
	 */
	public Boolean getExpedicaoDiploma() {
		return expedicaoDiploma;
	}

	/**
	 * @param expedicaoDiploma the expedicaoDiploma to set
	 */
	public void setExpedicaoDiploma(Boolean expedicaoDiploma) {
		this.expedicaoDiploma = expedicaoDiploma;
	}

	/**
	 * @return the programacaoFormatura
	 */
	public Boolean getProgramacaoFormatura() {
		return programacaoFormatura;
	}

	/**
	 * @param programacaoFormatura the programacaoFormatura to set
	 */
	public void setProgramacaoFormatura(Boolean programacaoFormatura) {
		this.programacaoFormatura = programacaoFormatura;
	}

	public Boolean getCartaoCreditoDebitoRel() {
		return cartaoCreditoDebitoRel;
	}

	public void setCartaoCreditoDebitoRel(Boolean cartaoCreditoDebitoRel) {
		this.cartaoCreditoDebitoRel = cartaoCreditoDebitoRel;
	}

	public Boolean getInadimplenciaRel() {
		return inadimplenciaRel;
	}

	public void setInadimplenciaRel(Boolean inadimplenciaRel) {
		this.inadimplenciaRel = inadimplenciaRel;
	}

	public Boolean getCartaoCreditoDebitoRelFavorito() {
		return cartaoCreditoDebitoRelFavorito;
	}

	public void setCartaoCreditoDebitoRelFavorito(Boolean cartaoCreditoDebitoRelFavorito) {
		this.cartaoCreditoDebitoRelFavorito = cartaoCreditoDebitoRelFavorito;
	}

	public Boolean getMapaFinanceiroAlunoRel() {
		return mapaFinanceiroAlunoRel;
	}

	public void setMapaFinanceiroAlunoRel(Boolean mapaFinanceiroAlunoRel) {
		this.mapaFinanceiroAlunoRel = mapaFinanceiroAlunoRel;
	}

	public Boolean getGradeCurricularAlunoRel() {
		return gradeCurricularAlunoRel;
	}

	public void setGradeCurricularAlunoRel(Boolean gradeCurricularAlunoRel) {
		this.gradeCurricularAlunoRel = gradeCurricularAlunoRel;
	}

	public Boolean getAtaProva() {
		return ataProva;
	}

	public void setAtaProva(Boolean ataProva) {
		this.ataProva = ataProva;
	}

	public Boolean getLiberacaoFinanceiroCancelamentoTrancamento() {
		return liberacaoFinanceiroCancelamentoTrancamento;
	}

	public void setLiberacaoFinanceiroCancelamentoTrancamento(Boolean liberacaoFinanceiroCancelamentoTrancamento) {
		this.liberacaoFinanceiroCancelamentoTrancamento = liberacaoFinanceiroCancelamentoTrancamento;
	}

	public Boolean getSubMenuSecretaria() {
		if (getAluno() || getAbonoFalta() || getAlterarUserNameSenhaAlunos() || getEntregaDocumento() || getLancamentoNota() || getEnade() || getAproveitamentoDisciplina() || getExclusaoMatricula() || getTextoPadraoDeclaracao() || getMapaAberturaTurma() || getAtividadeComplementar() || getTitulacaoProfessorCurso() || getRegistroAula() || getRegistroAulaNota() || getProfessorMinistrouAulaTurma() || getTrancamento() || getReativacaoMatricula() || getCancelamento() || getMapaSuspensaoMatricula() || getIdentificacaoEstudantil() || getMapaAberturaTurma() || getImpressaoDeclaracao() || getMapaAtualizacaoMatriculaFormada() || getMotivoCancelamentoTrancamento() || getEstagio() || getArtefatoEntregaAluno() || getRegistroEntregaArtefatoAluno()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void setSubMenuSecretaria(Boolean subMenuSecretaria) {
		this.subMenuSecretaria = subMenuSecretaria;
	}

	public Boolean getLancamentoNota() {
		return lancamentoNota;
	}

	public void setLancamentoNota(Boolean lancamentoNota) {
		this.lancamentoNota = lancamentoNota;
	}

	public Boolean getTransferenciaMatrizCurricular() {
		return transferenciaMatrizCurricular;
	}

	public void setTransferenciaMatrizCurricular(Boolean transferenciaMatrizCurricular) {
		this.transferenciaMatrizCurricular = transferenciaMatrizCurricular;
	}

	public Boolean getBoletimAcademicoRel() {
		return boletimAcademicoRel;
	}

	public void setBoletimAcademicoRel(Boolean boletimAcademicoRel) {
		this.boletimAcademicoRel = boletimAcademicoRel;
	}

	/**
	 * @return the registroPresencaColacaoGrau
	 */
	public Boolean getRegistroPresencaColacaoGrau() {
		return registroPresencaColacaoGrau;
	}

	/**
	 * @param registroPresencaColacaoGrau the registroPresencaColacaoGrau to set
	 */
	public void setRegistroPresencaColacaoGrau(Boolean registroPresencaColacaoGrau) {
		this.registroPresencaColacaoGrau = registroPresencaColacaoGrau;
	}

	public Boolean getTransferenciaInterna() {
		return transferenciaInterna;
	}

	public void setTransferenciaInterna(Boolean transferenciaInterna) {
		this.transferenciaInterna = transferenciaInterna;
	}

	/**
	 * @return the quadroMatriculaRel
	 */
	public Boolean getQuadroMatriculaRel() {
		return quadroMatriculaRel;
	}

	/**
	 * @param quadroMatriculaRel the quadroMatriculaRel to set
	 */
	public void setQuadroMatriculaRel(Boolean quadroMatriculaRel) {
		this.quadroMatriculaRel = quadroMatriculaRel;
	}

	public Boolean getOcorrenciasAlunosRel() {
		return ocorrenciasAlunosRel;
	}

	public void setOcorrenciasAlunosRel(Boolean ocorrenciasAlunosRel) {
		this.ocorrenciasAlunosRel = ocorrenciasAlunosRel;
	}

	public Boolean getEstatisticaMatriculaRel() {
		return estatisticaMatriculaRel;
	}

	public void setEstatisticaMatriculaRel(Boolean estatisticaMatriculaRel) {
		this.estatisticaMatriculaRel = estatisticaMatriculaRel;
	}

	public void setEnade(Boolean enade) {
		this.enade = enade;
	}

	public Boolean getEnade() {
		return enade;
	}

	public Boolean getQuadroAlunosAtivosInativosRel() {
		return quadroAlunosAtivosInativosRel;
	}

	public void setQuadroAlunosAtivosInativosRel(Boolean quadroAlunosAtivosInativosRel) {
		this.quadroAlunosAtivosInativosRel = quadroAlunosAtivosInativosRel;
	}

	public Boolean getSituacaoFinanceiraAlunoRel() {
		return situacaoFinanceiraAlunoRel;
	}

	public void setSituacaoFinanceiraAlunoRel(Boolean situacaoFinanceiraAlunoRel) {
		this.situacaoFinanceiraAlunoRel = situacaoFinanceiraAlunoRel;
	}

	public Boolean getReativacaoMatricula() {
		return reativacaoMatricula;
	}

	public void setReativacaoMatricula(Boolean reativacaoMatricula) {
		this.reativacaoMatricula = reativacaoMatricula;
	}

	public Boolean getMeusHorarios() {
		return meusHorarios;
	}

	public void setMeusHorarios(Boolean meusHorarios) {
		this.meusHorarios = meusHorarios;
	}

	public Boolean getMinhasNotas() {
		return minhasNotas;
	}

	public void setMinhasNotas(Boolean minhasNotas) {
		this.minhasNotas = minhasNotas;
	}

	public Boolean getMeusAmigos() {
		return meusAmigos;
	}

	public void setMeusAmigos(Boolean meusAmigos) {
		this.meusAmigos = meusAmigos;
	}

	public Boolean getMeusProfessores() {
		return meusProfessores;
	}

	public void setMeusProfessores(Boolean meusProfessores) {
		this.meusProfessores = meusProfessores;
	}

	public Boolean getDownloadArquivo() {
		return downloadArquivo;
	}

	public void setDownloadArquivo(Boolean downloadArquivo) {
		this.downloadArquivo = downloadArquivo;
	}

	public Boolean getDocumentosDigitais() {
		return documentosDigitais;
	}

	public void setDocumentosDigitais(Boolean documentosDigitais) {
		this.documentosDigitais = documentosDigitais;
	}

	public Boolean getDocumentosDigitaisFavorito() {
		return documentosDigitaisFavorito;
	}

	public void setDocumentosDigitaisFavorito(Boolean documentosDigitaisFavorito) {
		this.documentosDigitaisFavorito = documentosDigitaisFavorito;
	}

	public Boolean getPlanoEstudo() {
		return planoEstudo;
	}

	public void setPlanoEstudo(Boolean planoEstudo) {
		this.planoEstudo = planoEstudo;
	}

	public Boolean getMinhasContasPagar() {
		return minhasContasPagar;
	}

	public void setMinhasContasPagar(Boolean minhasContasPagar) {
		this.minhasContasPagar = minhasContasPagar;
	}

	public Boolean getMeusContratos() {
		return meusContratos;
	}

	public void setMeusContratos(Boolean meusContratos) {
		this.meusContratos = meusContratos;
	}

	public Boolean getBloqueioBiblioteca() {
		return bloqueioBiblioteca;
	}

	public void setBloqueioBiblioteca(Boolean bloqueioBiblioteca) {
		this.bloqueioBiblioteca = bloqueioBiblioteca;
	}

	public Boolean getDRE() {
		return getdRE();
	}

	public void setDRE(Boolean dRE) {
		this.setdRE(dRE);
	}

	public Boolean getTipoEventoContabil() {
		return tipoEventoContabil;
	}

	public void setTipoEventoContabil(Boolean tipoEventoContabil) {
		this.tipoEventoContabil = tipoEventoContabil;
	}

	public Boolean getHistoricoContabil() {
		return historicoContabil;
	}

	public void setHistoricoContabil(Boolean historicoContabil) {
		this.historicoContabil = historicoContabil;
	}

	public Boolean getSenhaAlunoProfessorRel() {
		return senhaAlunoProfessorRel;
	}

	public void setSenhaAlunoProfessorRel(Boolean senhaAlunoProfessorRel) {
		this.senhaAlunoProfessorRel = senhaAlunoProfessorRel;
	}

	public Boolean getContabil() {
		return contabil;
	}

	public void setContabil(Boolean contabil) {
		this.contabil = contabil;
	}

	public Boolean getCalculoMes() {
		return calculoMes;
	}

	public void setCalculoMes(Boolean calculoMes) {
		this.calculoMes = calculoMes;
	}

	public Boolean getFechamentoMes() {
		return fechamentoMes;
	}

	public void setFechamentoMes(Boolean fechamentoMes) {
		this.fechamentoMes = fechamentoMes;
	}

	public Boolean getAlunoNaoCursouDisciplinaRel() {
		return alunoNaoCursouDisciplinaRel;
	}

	public void setAlunoNaoCursouDisciplinaRel(Boolean alunoNaoCursouDisciplinaRel) {
		this.alunoNaoCursouDisciplinaRel = alunoNaoCursouDisciplinaRel;
	}

	public Boolean getMapaNotaAlunoPorTurma() {
		return mapaNotaAlunoPorTurma;
	}

	public void setMapaNotaAlunoPorTurma(Boolean mapaNotaAlunoPorTurma) {
		this.mapaNotaAlunoPorTurma = mapaNotaAlunoPorTurma;
	}

	public Boolean getCertificadoCursoExtensaoRel() {
		return certificadoCursoExtensaoRel;
	}

	public void setCertificadoCursoExtensaoRel(Boolean certificadoCursoExtensaoRel) {
		this.certificadoCursoExtensaoRel = certificadoCursoExtensaoRel;
	}

	public void setGeracaoManualParcelas(Boolean geracaoManualParcelas) {
		this.geracaoManualParcelas = geracaoManualParcelas;
	}

	public Boolean getGeracaoManualParcelas() {
		return geracaoManualParcelas;
	}

	public void setDisciplinasGradeRel(Boolean disciplinasGradeRel) {
		this.disciplinasGradeRel = disciplinasGradeRel;
	}

	public Boolean getDisciplinasGradeRel() {
		return disciplinasGradeRel;
	}

	public void setTermoCompromissoDocumentacaoPendenteRel(Boolean termoCompromissoDocumentacaoPendenteRel) {
		this.termoCompromissoDocumentacaoPendenteRel = termoCompromissoDocumentacaoPendenteRel;
	}

	public Boolean getTermoCompromissoDocumentacaoPendenteRel() {
		return termoCompromissoDocumentacaoPendenteRel;
	}

	public void setSerasa(Boolean serasa) {
		this.serasa = serasa;
	}

	public Boolean getSerasa() {
		return serasa;
	}

	/**
	 * @return the transferenciaTurma
	 */
	public Boolean getTransferenciaTurma() {
		if (transferenciaTurma == null) {
			return false;
		}
		return transferenciaTurma;
	}

	/**
	 * @param transferenciaTurma the transferenciaTurma to set
	 */
	public void setTransferenciaTurma(Boolean transferenciaTurma) {
		this.transferenciaTurma = transferenciaTurma;
	}

	public void setAtualizacaoVencimentos(Boolean atualizacaoVencimentos) {
		this.atualizacaoVencimentos = atualizacaoVencimentos;
	}

	public Boolean getAtualizacaoVencimentos() {
		return atualizacaoVencimentos;
	}

	public Boolean getEntregaBoletosRel() {
		return entregaBoletosRel;
	}

	public void setEntregaBoletosRel(Boolean entregaBoletosRel) {
		this.entregaBoletosRel = entregaBoletosRel;
	}

	/**
	 * @return the inclusaoExclusaoDisciplina
	 */
	public Boolean getInclusaoExclusaoDisciplina() {
		return inclusaoExclusaoDisciplina;
	}

	/**
	 * @param inclusaoExclusaoDisciplina the inclusaoExclusaoDisciplina to set
	 */
	public void setInclusaoExclusaoDisciplina(Boolean inclusaoExclusaoDisciplina) {
		this.inclusaoExclusaoDisciplina = inclusaoExclusaoDisciplina;
	}

	/**
	 * @return the alunoDescontoDesempenhoRel
	 */
	public Boolean getAlunoDescontoDesempenhoRel() {
		return alunoDescontoDesempenhoRel;
	}

	public Boolean getTransferenciaTurno() {
		return transferenciaTurno;
	}

	/**
	 * @param alunoDescontoDesempenhoRel the alunoDescontoDesempenhoRel to set
	 */
	public void setAlunoDescontoDesempenhoRel(Boolean alunoDescontoDesempenhoRel) {
		this.alunoDescontoDesempenhoRel = alunoDescontoDesempenhoRel;
	}

	public void setTransferenciaTurno(Boolean transferenciaTurno) {
		this.transferenciaTurno = transferenciaTurno;
	}

	/**
	 * @return the alunosMatriculadosGeralRel
	 */
	public Boolean getAlunosMatriculadosGeralRel() {
		return alunosMatriculadosGeralRel;
	}

	/**
	 * @param alunosMatriculadosGeralRel the alunosMatriculadosGeralRel to set
	 */
	public void setAlunosMatriculadosGeralRel(Boolean alunosMatriculadosGeralRel) {
		this.alunosMatriculadosGeralRel = alunosMatriculadosGeralRel;
	}

	public Boolean getRelacaoEnderecoAlunoRel() {
		return relacaoEnderecoAlunoRel;
	}

	public void setRelacaoEnderecoAlunoRel(Boolean relacaoEnderecoAlunoRel) {
		this.relacaoEnderecoAlunoRel = relacaoEnderecoAlunoRel;
	}

	public Boolean getLivroRegistroRel() {
		return livroRegistroRel;
	}

	public void setLivroRegistroRel(Boolean livroRegistroRel) {
		this.livroRegistroRel = livroRegistroRel;
	}

	public void setFichaAtualizacaoCadastralRel(Boolean fichaAtualizacaoCadastralRel) {
		this.fichaAtualizacaoCadastralRel = fichaAtualizacaoCadastralRel;
	}

	public Boolean getFichaAtualizacaoCadastralRel() {
		return fichaAtualizacaoCadastralRel;
	}

	public void setAlunosMatriculadosPorProcessoSeletivoRel(Boolean alunosMatriculadosPorProcessoSeletivoRel) {
		this.alunosMatriculadosPorProcessoSeletivoRel = alunosMatriculadosPorProcessoSeletivoRel;
	}

	public Boolean getAlunosMatriculadosPorProcessoSeletivoRel() {
		return alunosMatriculadosPorProcessoSeletivoRel;
	}

	public Boolean getFaturamentoRecebimentoRel() {
		return faturamentoRecebimentoRel;
	}

	public void setFaturamentoRecebimentoRel(Boolean faturamentoRecebimentoRel) {
		this.faturamentoRecebimentoRel = faturamentoRecebimentoRel;
	}

	/**
	 * @return the alunosProcessoSeletivoRel
	 */
	public Boolean getAlunosProcessoSeletivoRel() {
		return alunosProcessoSeletivoRel;
	}

	/**
	 * @param alunosProcessoSeletivoRel the alunosProcessoSeletivoRel to set
	 */
	public void setAlunosProcessoSeletivoRel(Boolean alunosProcessoSeletivoRel) {
		this.alunosProcessoSeletivoRel = alunosProcessoSeletivoRel;
	}

	public Boolean getMapaPendenciasControleCobranca() {
		return mapaPendenciasControleCobranca;
	}

	public void setMapaPendenciasControleCobranca(Boolean mapaPendenciasControleCobranca) {
		this.mapaPendenciasControleCobranca = mapaPendenciasControleCobranca;
	}

	public Boolean getAlunosNaoRenovaramRel() {
		return alunosNaoRenovaramRel;
	}

	public Boolean getAcervoRel() {
		return acervoRel;
	}

	public void setAcervoRel(Boolean acervoRel) {
		this.acervoRel = acervoRel;
	}

	public void setAlunosNaoRenovaramRel(Boolean alunosNaoRenovaramRel) {
		this.alunosNaoRenovaramRel = alunosNaoRenovaramRel;
	}

	public Boolean getProcessoSeletivoAprovadoReprovadoRel() {
		return processoSeletivoAprovadoReprovadoRel;
	}

	public void setProcessoSeletivoAprovadoReprovadoRel(Boolean processoSeletivoAprovadoReprovadoRel) {
		this.processoSeletivoAprovadoReprovadoRel = processoSeletivoAprovadoReprovadoRel;
	}

	public Boolean getAlunosProUniRel() {
		return alunosProUniRel;
	}

	public void setAlunosProUniRel(Boolean alunosProUniRel) {
		this.alunosProUniRel = alunosProUniRel;
	}

	public Boolean getCondicoesPagamentoRel() {
		return condicoesPagamentoRel;
	}

	public void setCondicoesPagamentoRel(Boolean condicoesPagamentoRel) {
		this.condicoesPagamentoRel = condicoesPagamentoRel;
	}

	public Boolean getAlunosAtivosRel() {
		return alunosAtivosRel;
	}

	public void setAlunosAtivosRel(Boolean alunosAtivosRel) {
		this.alunosAtivosRel = alunosAtivosRel;
	}

	public Boolean getMapaNotasDisciplinaAlunos() {
		return mapaNotasDisciplinaAlunos;
	}

	public void setMapaNotasDisciplinaAlunos(Boolean mapaNotasDisciplinaAlunos) {
		this.mapaNotasDisciplinaAlunos = mapaNotasDisciplinaAlunos;
	}

	public Boolean getListagemDescontosAlunosRel() {
		return listagemDescontosAlunosRel;
	}

	public void setListagemDescontosAlunosRel(Boolean listagemDescontosAlunosRel) {
		this.listagemDescontosAlunosRel = listagemDescontosAlunosRel;
	}

	public Boolean getGeracaoManualParcelasAluno() {
		return geracaoManualParcelasAluno;
	}

	public void setGeracaoManualParcelasAluno(Boolean geracaoManualParcelasAluno) {
		this.geracaoManualParcelasAluno = geracaoManualParcelasAluno;
	}

	public Boolean getAlunosReprovadosRel() {
		return AlunosReprovadosRel;
	}

	public void setAlunosReprovadosRel(Boolean alunosReprovadosRel) {
		AlunosReprovadosRel = alunosReprovadosRel;
	}

	/**
	 * @return the MapaSituacaoAlunoRel
	 */
	public Boolean getMapaSituacaoAlunoRel() {
		return MapaSituacaoAlunoRel;
	}

	/**
	 * @param MapaSituacaoAlunoRel the MapaSituacaoAlunoRel to set
	 */
	public void setMapaSituacaoAlunoRel(Boolean MapaSituacaoAlunoRel) {
		this.MapaSituacaoAlunoRel = MapaSituacaoAlunoRel;
	}

	public Boolean getSubMenuRelatorioBiblioteca() {
		return getExemplaresRel() || getSituacaoExemplaresRel() || getAcervoRel() || getEmprestimoRel() || getEtiquetaLivroRel();
	}

	public Boolean getExemplaresRel() {
		return exemplaresRel;
	}

	public void setExemplaresRel(Boolean exemplaresRel) {
		this.exemplaresRel = exemplaresRel;
	}

	private Boolean painelGestor = false;
	private Boolean painelGestorFinanceiro = false;
	private Boolean painelGestorMonitorarConsultor = false;
	private Boolean painelGestorPlanoOrcamentario = false;
	private Boolean painelGestorAcademico = false;
	private Boolean painelGestorConsumo = false;
	private Boolean painelGestorDespesaCategoria = false;

	public Boolean getPainelGestor() {
		return painelGestor;
	}

	public void setPainelGestor(Boolean painelGestor) {
		this.painelGestor = painelGestor;
	}

	public Boolean getPainelGestorAcademico() {
		return painelGestorAcademico;
	}

	public void setPainelGestorAcademico(Boolean painelGestorAcademico) {
		this.painelGestorAcademico = painelGestorAcademico;
	}

	public Boolean getPainelGestorFinanceiro() {
		return painelGestorFinanceiro;
	}

	public void setPainelGestorFinanceiro(Boolean painelGestorFinanceiro) {
		this.painelGestorFinanceiro = painelGestorFinanceiro;
	}

	public Boolean getMapaPendenciaCartaoCredito() {
		return mapaPendenciaCartaoCredito;
	}

	public void setMapaPendenciaCartaoCredito(Boolean mapaPendenciaCartaoCredito) {
		this.mapaPendenciaCartaoCredito = mapaPendenciaCartaoCredito;
	}

	public Boolean getMapaPendenciaMovimentacaoFinanceira() {
		return mapaPendenciaMovimentacaoFinanceira;
	}

	public void setMapaPendenciaMovimentacaoFinanceira(Boolean mapaPendenciaMovimentacaoFinanceira) {
		this.mapaPendenciaMovimentacaoFinanceira = mapaPendenciaMovimentacaoFinanceira;
	}

	public Boolean getBuscaCatalogo() {
		return buscaCatalogo;
	}

	public void setBuscaCatalogo(Boolean buscaCatalogo) {
		this.buscaCatalogo = buscaCatalogo;
	}

	public Boolean getMinhaBiblioteca() {
		return minhaBiblioteca;
	}

	public void setMinhaBiblioteca(Boolean minhaBiblioteca) {
		this.minhaBiblioteca = minhaBiblioteca;
	}

	public Boolean getSituacaoExemplaresRel() {
		return situacaoExemplaresRel;
	}

	public void setSituacaoExemplaresRel(Boolean situacaoExemplaresRel) {
		this.situacaoExemplaresRel = situacaoExemplaresRel;
	}

	public Boolean getHorarioDaTurmaRel() {
		return horarioDaTurmaRel;
	}

	public void setHorarioDaTurmaRel(Boolean horarioDaTurma) {
		this.horarioDaTurmaRel = horarioDaTurma;
	}

	public Boolean getHorarioAlunoRel() {
		return horarioAlunoRel;
	}

	public void setHorarioAlunoRel(Boolean horarioAluno) {
		this.horarioAlunoRel = horarioAluno;
	}

	public void setControleRemessa(Boolean controleRemessa) {
		this.controleRemessa = controleRemessa;
	}

	public Boolean getControleRemessa() {
		return controleRemessa;
	}

	public Boolean getEmprestimoRel() {
		return emprestimoRel;
	}

	public void setEmprestimoRel(Boolean emprestimoRel) {
		this.emprestimoRel = emprestimoRel;
	}

	public Boolean getIdentificacaoEstudantil() {
		return identificacaoEstudantil;
	}

	public void setIdentificacaoEstudantil(Boolean identificacaoEstudantil) {
		this.identificacaoEstudantil = identificacaoEstudantil;
	}

	public void setRegistroAulaNota(Boolean registroAulaNota) {
		this.registroAulaNota = registroAulaNota;
	}

	public Boolean getRegistroAulaNota() {
		return registroAulaNota;
	}

	public Boolean getExclusaoMatricula() {
		return exclusaoMatricula;
	}

	public void setExclusaoMatricula(Boolean exclusaoMatricula) {
		this.exclusaoMatricula = exclusaoMatricula;
	}

	/**
	 * @return the agendaProfessorRel
	 */
	public Boolean getAgendaProfessorRel() {
		return agendaProfessorRel;
	}

	/**
	 * @param agendaProfessorRel the agendaProfessorRel to set
	 */
	public void setAgendaProfessorRel(Boolean agendaProfessorRel) {
		this.agendaProfessorRel = agendaProfessorRel;
	}

	public Boolean getGrupoDestinatarios() {
		return grupoDestinatarios;
	}

	public void setGrupoDestinatarios(Boolean grupoDestinatarios) {
		this.grupoDestinatarios = grupoDestinatarios;
	}

	public Boolean getChancela() {
		return chancela;
	}

	public void setChancela(Boolean chancela) {
		this.chancela = chancela;
	}

	/**
	 * @return the configuracoesVisao
	 */
	public Boolean getConfiguracoesVisao() {
		return configuracoesVisao;
	}

	/**
	 * @param configuracoesVisao the configuracoesVisao to set
	 */
	public void setConfiguracoesVisao(Boolean configuracoesVisao) {
		this.configuracoesVisao = configuracoesVisao;
	}

	/**
	 * @return the configuracoesAlteracaoSenha
	 */
	public Boolean getConfiguracoesAlteracaoSenha() {
		return configuracoesAlteracaoSenha;
	}

	/**
	 * @param configuracoesAlteracaoSenha the configuracoesAlteracaoSenha to set
	 */
	public void setConfiguracoesAlteracaoSenha(Boolean configuracoesAlteracaoSenha) {
		this.configuracoesAlteracaoSenha = configuracoesAlteracaoSenha;
	}

	/**
	 * @return the configuracoesAlteracaoFoto
	 */
	public Boolean getConfiguracoesAlteracaoFoto() {
		return configuracoesAlteracaoFoto;
	}

	/**
	 * @param configuracoesAlteracaoFoto the configuracoesAlteracaoFoto to set
	 */
	public void setConfiguracoesAlteracaoFoto(Boolean configuracoesAlteracaoFoto) {
		this.configuracoesAlteracaoFoto = configuracoesAlteracaoFoto;
	}

	/**
	 * @return the configuracoesAlteracaoCorTela
	 */
	public Boolean getConfiguracoesAlteracaoCorTela() {
		return configuracoesAlteracaoCorTela;
	}

	/**
	 * @param configuracoesAlteracaoCorTela the configuracoesAlteracaoCorTela to set
	 */
	public void setConfiguracoesAlteracaoCorTela(Boolean configuracoesAlteracaoCorTela) {
		this.configuracoesAlteracaoCorTela = configuracoesAlteracaoCorTela;
	}

	/**
	 * @return the configuracoesVisaoProfessor
	 */
	public Boolean getConfiguracoesVisaoProfessor() {
		return configuracoesVisaoProfessor;
	}

	/**
	 * @param configuracoesVisaoProfessor the configuracoesVisaoProfessor to set
	 */
	public void setConfiguracoesVisaoProfessor(Boolean configuracoesVisaoProfessor) {
		this.configuracoesVisaoProfessor = configuracoesVisaoProfessor;
	}

	/**
	 * @return the configuracoesLiberarAlteracaoSenhaProfessor
	 */
	public Boolean getConfiguracoesLiberarAlteracaoSenhaProfessor() {
		return configuracoesLiberarAlteracaoSenhaProfessor;
	}

	/**
	 * @param configuracoesLiberarAlteracaoSenhaProfessor the configuracoesLiberarAlteracaoSenhaProfessor to set
	 */
	public void setConfiguracoesLiberarAlteracaoSenhaProfessor(Boolean configuracoesLiberarAlteracaoSenhaProfessor) {
		this.configuracoesLiberarAlteracaoSenhaProfessor = configuracoesLiberarAlteracaoSenhaProfessor;
	}

	/**
	 * @return the configuracoesLiberarAlteracaoFotoProfessor
	 */
	public Boolean getConfiguracoesLiberarAlteracaoFotoProfessor() {
		return configuracoesLiberarAlteracaoFotoProfessor;
	}

	/**
	 * @param configuracoesLiberarAlteracaoFotoProfessor the configuracoesLiberarAlteracaoFotoProfessor to set
	 */
	public void setConfiguracoesLiberarAlteracaoFotoProfessor(Boolean configuracoesLiberarAlteracaoFotoProfessor) {
		this.configuracoesLiberarAlteracaoFotoProfessor = configuracoesLiberarAlteracaoFotoProfessor;
	}

	/**
	 * @return the configuracoesLiberarAlteracaoCorTelaProfessor
	 */
	public Boolean getConfiguracoesLiberarAlteracaoCorTelaProfessor() {
		return configuracoesLiberarAlteracaoCorTelaProfessor;
	}

	/**
	 * @param configuracoesLiberarAlteracaoCorTelaProfessor the configuracoesLiberarAlteracaoCorTelaProfessor to set
	 */
	public void setConfiguracoesLiberarAlteracaoCorTelaProfessor(Boolean configuracoesLiberarAlteracaoCorTelaProfessor) {
		this.configuracoesLiberarAlteracaoCorTelaProfessor = configuracoesLiberarAlteracaoCorTelaProfessor;
	}

	/**
	 * @return the controleConcorrenciaHorarioTurma
	 */
	public Boolean getControleConcorrenciaHorarioTurma() {
		return controleConcorrenciaHorarioTurma;
	}

	/**
	 * @param controleConcorrenciaHorarioTurma the controleConcorrenciaHorarioTurma to set
	 */
	public void setControleConcorrenciaHorarioTurma(Boolean controleConcorrenciaHorarioTurma) {
		this.controleConcorrenciaHorarioTurma = controleConcorrenciaHorarioTurma;
	}

	/**
	 * @return the emailTurmaRel
	 */
	public Boolean getEmailTurmaRel() {
		return emailTurmaRel;
	}

	/**
	 * @param emailTurmaRel the emailTurmaRel to set
	 */
	public void setEmailTurmaRel(Boolean emailTurmaRel) {
		this.emailTurmaRel = emailTurmaRel;
	}

	public Boolean getPermitirProfessorExcluirArquivoInstituicao() {
		return permitirProfessorExcluirArquivoInstituicao;
	}

	public void setPermitirProfessorExcluirArquivoInstituicao(Boolean permitirProfessorExcluirArquivoInstituicao) {
		this.permitirProfessorExcluirArquivoInstituicao = permitirProfessorExcluirArquivoInstituicao;
	}

	public Boolean getAniversariantesDoMesRel() {
		return aniversariantesDoMesRel;
	}

	public void setAniversariantesDoMesRel(Boolean aniversariantesDoMesRel) {
		this.aniversariantesDoMesRel = aniversariantesDoMesRel;
	}

	public Boolean getAlterarDescontoContaReceber() {
		if (alterarDescontoContaReceber == null) {
			alterarDescontoContaReceber = Boolean.FALSE;
		}
		return alterarDescontoContaReceber;
	}

	public void setAlterarDescontoContaReceber(Boolean alterarDescontoContaReceber) {
		this.alterarDescontoContaReceber = alterarDescontoContaReceber;
	}

	public Boolean getCronogramaDeAulasRel() {
		return cronogramaDeAulasRel;
	}

	public void setCronogramaDeAulasRel(Boolean cronogramaDeAulasRel) {
		this.cronogramaDeAulasRel = cronogramaDeAulasRel;
	}

	/**
	 * @return the previsaoFaturamentoRel
	 */
	public Boolean getPrevisaoFaturamentoRel() {
		return previsaoFaturamentoRel;
	}

	/**
	 * @param previsaoFaturamentoRel the previsaoFaturamentoRel to set
	 */
	public void setPrevisaoFaturamentoRel(Boolean previsaoFaturamentoRel) {
		this.previsaoFaturamentoRel = previsaoFaturamentoRel;
	}

	/**
	 * @return the consultorMatricula
	 */
	public Boolean getConsultorMatricula() {
		return consultorMatricula;
	}

	/**
	 * @param consultorMatricula the consultorMatricula to set
	 */
	public void setConsultorMatricula(Boolean consultorMatricula) {
		this.consultorMatricula = consultorMatricula;
	}

	/**
	 * @return the quadroComissaoConsultoresRel
	 */
	public Boolean getQuadroComissaoConsultoresRel() {
		return quadroComissaoConsultoresRel;
	}

	/**
	 * @param quadroComissaoConsultoresRel the quadroComissaoConsultoresRel to set
	 */
	public void setQuadroComissaoConsultoresRel(Boolean quadroComissaoConsultoresRel) {
		this.quadroComissaoConsultoresRel = quadroComissaoConsultoresRel;
	}

	public Boolean getPosVendaRel() {
		return posVendaRel;
	}

	public void setPosVendaRel(Boolean posVendaRel) {
		this.posVendaRel = posVendaRel;
	}

	public Boolean getPosVendaRelFavorito() {
		return posVendaRelFavorito;
	}

	public void setPosVendaRelFavorito(Boolean posVendaRelFavorito) {
		this.posVendaRelFavorito = posVendaRelFavorito;
	}

	public Boolean getTransferenciaUnidade() {
		return transferenciaUnidade;
	}

	public void setTransferenciaUnidade(Boolean transferenciaUnidade) {
		this.transferenciaUnidade = transferenciaUnidade;
	}

	public Boolean getEnvelopeRel() {
		return envelopeRel;
	}

	public void setEnvelopeRel(Boolean envelopeRel) {
		this.envelopeRel = envelopeRel;
	}

	public Boolean getEnvelopeRequerimentoRel() {
		return envelopeRequerimentoRel;
	}

	public void setEnvelopeRequerimentoRel(Boolean envelopeRequerimentoRel) {
		this.envelopeRequerimentoRel = envelopeRequerimentoRel;
	}

	public Boolean getUploadArquivosComuns() {
		return uploadArquivosComuns;
	}

	public void setUploadArquivosComuns(Boolean uploadArquivosComuns) {
		this.uploadArquivosComuns = uploadArquivosComuns;
	}

	public Boolean getUploadArquivosComunsFavorito() {
		return uploadArquivosComunsFavorito;
	}

	public void setUploadArquivosComunsFavorito(Boolean uploadArquivosComunsFavorito) {
		this.uploadArquivosComunsFavorito = uploadArquivosComunsFavorito;
	}

	public Boolean getPlanoDescontoInclusaoDisciplina() {
		return planoDescontoInclusaoDisciplina;
	}

	public void setPlanoDescontoInclusaoDisciplina(Boolean planoDescontoInclusaoDisciplina) {
		this.planoDescontoInclusaoDisciplina = planoDescontoInclusaoDisciplina;
	}

	public Boolean getImpressaoContrato() {
		return impressaoContrato;
	}

	public void setImpressaoContrato(Boolean impressaoContrato) {
		this.impressaoContrato = impressaoContrato;
	}

	public Boolean getMotivoCancelamentoTrancamento() {
		return motivoCancelamentoTrancamento;
	}

	public void setMotivoCancelamentoTrancamento(Boolean motivoCancelamentoTrancamento) {
		this.motivoCancelamentoTrancamento = motivoCancelamentoTrancamento;
	}

	public Boolean getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Boolean coordenador) {
		this.coordenador = coordenador;
	}

	public Boolean getAberturaTurmaRel() {
		return aberturaTurmaRel;
	}

	public void setAberturaTurmaRel(Boolean aberturaTurmaRel) {
		this.aberturaTurmaRel = aberturaTurmaRel;
	}

	public Boolean getContaPagarPorCategoriaDespesaRel() {
		return contaPagarPorCategoriaDespesaRel;
	}

	public void setContaPagarPorCategoriaDespesaRel(Boolean contaPagarPorCategoriaDespesaRel) {
		this.contaPagarPorCategoriaDespesaRel = contaPagarPorCategoriaDespesaRel;
	}

	/**
	 * @return the contaPagarPorTurmaRel
	 */
	public Boolean getContaPagarPorTurmaRel() {
		return contaPagarPorTurmaRel;
	}

	/**
	 * @param contaPagarPorTurmaRel the contaPagarPorTurmaRel to set
	 */
	public void setContaPagarPorTurmaRel(Boolean contaPagarPorTurmaRel) {
		this.contaPagarPorTurmaRel = contaPagarPorTurmaRel;
	}

	public Boolean getExtratoContaPagarRel() {
		return extratoContaPagarRel;
	}

	public void setExtratoContaPagarRel(Boolean extratoContaPagarRel) {
		this.extratoContaPagarRel = extratoContaPagarRel;
	}

	public Boolean getRequerimentoRel() {
		return requerimentoRel;
	}

	public void setRequerimentoRel(Boolean requerimentoRel) {
		this.requerimentoRel = requerimentoRel;
	}

	public Boolean getMapaAberturaTurma() {
		return mapaAberturaTurma;
	}

	public void setMapaAberturaTurma(Boolean mapaAberturaTurma) {
		this.mapaAberturaTurma = mapaAberturaTurma;
	}

	public Boolean getSolicitacaoOrcamentoPlanoOrcamentario() {
		return solicitacaoOrcamentoPlanoOrcamentario;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentario(Boolean solicitacaoOrcamentoPlanoOrcamentario) {
		this.solicitacaoOrcamentoPlanoOrcamentario = solicitacaoOrcamentoPlanoOrcamentario;
	}

	public Boolean getAvaliacaoInstitucionalPresencialResposta() {
		return avaliacaoInstitucionalPresencialResposta;
	}

	public void setAvaliacaoInstitucionalPresencialResposta(Boolean avaliacaoInstitucionalPresencialResposta) {
		this.avaliacaoInstitucionalPresencialResposta = avaliacaoInstitucionalPresencialResposta;
	}

	public Boolean getMudancaCarteira() {
		return mudancaCarteira;
	}

	public void setMudancaCarteira(Boolean mudancaCarteira) {
		this.mudancaCarteira = mudancaCarteira;
	}

	public Boolean getAreaProfissional() {
		return areaProfissional;
	}

	public void setAreaProfissional(Boolean areaProfissional) {
		this.areaProfissional = areaProfissional;
	}

	public Boolean getVagas() {
		return vagas;
	}

	public void setVagas(Boolean vagas) {
		this.vagas = vagas;
	}

	public Boolean getLink() {
		return link;
	}

	public void setLink(Boolean link) {
		this.link = link;
	}

	/**
	 * @return the declaracaoPasseEstudantilRel
	 */
	public Boolean getDeclaracaoPasseEstudantilRel() {
		return declaracaoPasseEstudantilRel;
	}

	/**
	 * @param declaracaoPasseEstudantilRel the declaracaoPasseEstudantilRel to set
	 */
	public void setDeclaracaoPasseEstudantilRel(Boolean declaracaoPasseEstudantilRel) {
		this.declaracaoPasseEstudantilRel = declaracaoPasseEstudantilRel;
	}

	public Boolean getTextoPadraoBancoCurriculum() {
		return textoPadraoBancoCurriculum;
	}

	public void setTextoPadraoBancoCurriculum(Boolean textoPadraoBancoCurriculum) {
		this.textoPadraoBancoCurriculum = textoPadraoBancoCurriculum;
	}

	public Boolean getCandidatosParaVagaRel() {
		return candidatosParaVagaRel;
	}

	public void setCandidatosParaVagaRel(Boolean candidatosParaVagaRel) {
		this.candidatosParaVagaRel = candidatosParaVagaRel;
	}

	public Boolean getCampanha() {
		return campanha;
	}

	public void setCampanha(Boolean campanha) {
		this.campanha = campanha;
	}

	public Boolean getMeta() {
		return meta;
	}

	public void setMeta(Boolean meta) {
		this.meta = meta;
	}

	public Boolean getProspects() {
		return prospects;
	}

	public void setProspects(Boolean prospects) {
		this.prospects = prospects;
	}

	public Boolean getEtiquetaCodigoBarra() {
		return etiquetaCodigoBarra;
	}

	public void setEtiquetaCodigoBarra(Boolean etiquetaCodigoBarra) {
		this.etiquetaCodigoBarra = etiquetaCodigoBarra;
	}

	public Boolean getSituacaoProspectPipeline() {
		return situacaoProspectPipeline;
	}

	public void setSituacaoProspectPipeline(Boolean situacaoProspectPipeline) {
		this.situacaoProspectPipeline = situacaoProspectPipeline;
	}

	public Boolean getFollowUpProspect() {
		return followUpProspect;
	}

	public void setFollowUpProspect(Boolean followUpProspect) {
		this.followUpProspect = followUpProspect;
	}

	public Boolean getEmpresaVagaBancoTalentoRel() {
		return empresaVagaBancoTalentoRel;
	}

	public void setEmpresaVagaBancoTalentoRel(Boolean empresaVagaBancoTalentoRel) {
		this.empresaVagaBancoTalentoRel = empresaVagaBancoTalentoRel;
	}

	public Boolean getBuscaCandidatoVaga() {
		return buscaCandidatoVaga;
	}

	public void setBuscaCandidatoVaga(Boolean buscaCandidatoVaga) {
		this.buscaCandidatoVaga = buscaCandidatoVaga;
	}

	public Boolean getRegistroEntrada() {
		return registroEntrada;
	}

	public void setRegistroEntrada(Boolean registroEntrada) {
		this.registroEntrada = registroEntrada;
	}

	public Boolean getBuscaVagas() {
		return buscaVagas;
	}

	public void setBuscaVagas(Boolean buscaVagas) {
		this.buscaVagas = buscaVagas;
	}

	public Boolean getEtiquetaProvaRel() {
		return etiquetaProvaRel;
	}

	public void setEtiquetaProvaRel(Boolean etiquetaProvaRel) {
		this.etiquetaProvaRel = etiquetaProvaRel;
	}

	public Boolean getAgendaPessoa() {
		return agendaPessoa;
	}

	public void setAgendaPessoa(Boolean agendaPessoa) {
		this.agendaPessoa = agendaPessoa;
	}

	public Boolean getEtiquetaLivroRel() {
		return etiquetaLivroRel;
	}

	public void setEtiquetaLivroRel(Boolean etiquetaLivroRel) {
		this.etiquetaLivroRel = etiquetaLivroRel;
	}

	public Boolean getTitulacaoCurso() {
		return titulacaoCurso;
	}

	public void setTitulacaoCurso(Boolean titulacaoCurso) {
		this.titulacaoCurso = titulacaoCurso;
	}

	public Boolean getCronogramaAula() {
		return cronogramaAula;
	}

	public void setCronogramaAula(Boolean cronogramaAula) {
		this.cronogramaAula = cronogramaAula;
	}

	public Boolean getMotivoInsucesso() {
		return motivoInsucesso;
	}

	public void setMotivoInsucesso(Boolean motivoInsucesso) {
		this.motivoInsucesso = motivoInsucesso;
	}

	public Boolean getInteracaoWorkflow() {
		return interacaoWorkflow;
	}

	public void setInteracaoWorkflow(Boolean interacaoWorkflow) {
		this.interacaoWorkflow = interacaoWorkflow;
	}

	public Boolean getTitulacaoProfessorCurso() {
		return titulacaoProfessorCurso;
	}

	public void setTitulacaoProfessorCurso(Boolean titulacaoProfessorCurso) {
		this.titulacaoProfessorCurso = titulacaoProfessorCurso;
	}

	public Boolean getAlterarUserNameSenhaAlunos() {
		return alterarUserNameSenhaAlunos;
	}

	public void setAlterarUserNameSenhaAlunos(Boolean alterarUserNameSenhaAlunos) {
		this.alterarUserNameSenhaAlunos = alterarUserNameSenhaAlunos;
	}

	public Boolean getComissionamentoTurma() {
		return comissionamentoTurma;
	}

	public void setComissionamentoTurma(Boolean comissionamentoTurma) {
		this.comissionamentoTurma = comissionamentoTurma;
	}

	public Boolean getPainelGestorBancoCurriculo() {
		return painelGestorBancoCurriculo;
	}

	public void setPainelGestorBancoCurriculo(Boolean painelGestorBancoCurriculo) {
		this.painelGestorBancoCurriculo = painelGestorBancoCurriculo;
	}

	public Boolean getEmpresasRel() {
		return empresasRel;
	}

	public void setEmpresasRel(Boolean empresasRel) {
		this.empresasRel = empresasRel;
	}

	public Boolean getAlunosCandidatadosVagaRel() {
		return alunosCandidatadosVagaRel;
	}

	public void setAlunosCandidatadosVagaRel(Boolean alunosCandidatadosVagaRel) {
		this.alunosCandidatadosVagaRel = alunosCandidatadosVagaRel;
	}

	public Boolean getConfiguracaoRanking() {
		return configuracaoRanking;
	}

	public void setConfiguracaoRanking(Boolean configuracaoRanking) {
		this.configuracaoRanking = configuracaoRanking;
	}

	public Boolean getPercentualConfiguracaoRanking() {
		return percentualConfiguracaoRanking;
	}

	public void setPercentualConfiguracaoRanking(Boolean percentualConfiguracaoRanking) {
		this.percentualConfiguracaoRanking = percentualConfiguracaoRanking;
	}

	public Boolean getRanking() {
		return ranking;
	}

	public void setRanking(Boolean ranking) {
		this.ranking = ranking;
	}

	public Boolean getReciboComissoesRel() {
		return reciboComissoesRel;
	}

	public void setReciboComissoesRel(Boolean reciboComissoes) {
		this.reciboComissoesRel = reciboComissoes;
	}

	public Boolean getSubMenuRelatorioCRM() {
		if (getReciboComissoesRel() || getExtratoComissaoRel() || getConsultorPorMatriculaRel() || getProdutividadeConsultorRel()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getVisaoAdministradorAgendaPessoa() {
		return visaoAdministradorAgendaPessoa;
	}

	public void setVisaoAdministradorAgendaPessoa(Boolean visaoAdministradorAgendaPessoa) {
		this.visaoAdministradorAgendaPessoa = visaoAdministradorAgendaPessoa;
	}

	public Boolean getExtratoComissaoRel() {
		return extratoComissaoRel;
	}

	public void setExtratoComissaoRel(Boolean extratoComissaoRel) {
		this.extratoComissaoRel = extratoComissaoRel;
	}

	public Boolean getPainelGestorSupervisaoVenda() {
		return painelGestorSupervisaoVenda;
	}

	public void setPainelGestorSupervisaoVenda(Boolean painelGestorSupervisaoVenda) {
		this.painelGestorSupervisaoVenda = painelGestorSupervisaoVenda;
	}

	public Boolean getPainelGestorVendedor() {
		return painelGestorVendedor;
	}

	public void setPainelGestorVendedor(Boolean painelGestorVendedor) {
		this.painelGestorVendedor = painelGestorVendedor;
	}

	public Boolean getRenegociacaoRel() {
		return renegociacaoRel;
	}

	public void setRenegociacaoRel(Boolean renegociacaoRel) {
		this.renegociacaoRel = renegociacaoRel;
	}

	/**
	 * @return the textoPadraDeclaracao
	 */
	public Boolean getTextoPadraoDeclaracao() {
		return textoPadraoDeclaracao;
	}

	/**
	 * @param textoPadraDeclaracao the textoPadraDeclaracao to set
	 */
	public void setTextoPadraoDeclaracao(Boolean textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	/**
	 * @return the impressaoDeclaracao
	 */
	public Boolean getImpressaoDeclaracao() {
		return impressaoDeclaracao;
	}

	/**
	 * @param impressaoDeclaracao the impressaoDeclaracao to set
	 */
	public void setImpressaoDeclaracao(Boolean impressaoDeclaracao) {
		this.impressaoDeclaracao = impressaoDeclaracao;
	}

	/**
	 * @return the minhasFaltas
	 */
	public Boolean getMinhasFaltas() {
		return minhasFaltas;
	}

	/**
	 * @param minhasFaltas the minhasFaltas to set
	 */
	public void setMinhasFaltas(Boolean minhasFaltas) {
		this.minhasFaltas = minhasFaltas;
	}

	public Boolean getAtividadeComplementar() {
		return atividadeComplementar;
	}

	public void setAtividadeComplementar(Boolean atividadeComplementar) {
		this.atividadeComplementar = atividadeComplementar;
	}

	/**
	 * @return the cartaoRespostaRel
	 */
	public Boolean getCartaoRespostaRel() {
		return cartaoRespostaRel;
	}

	/**
	 * @param cartaoRespostaRel the cartaoRespostaRel to set
	 */
	public void setCartaoRespostaRel(Boolean cartaoRespostaRel) {
		this.cartaoRespostaRel = cartaoRespostaRel;
	}

	public Boolean getTipoDescontoRel() {
		return tipoDescontoRel;
	}

	public void setTipoDescontoRel(Boolean tipoDescontoRel) {
		this.tipoDescontoRel = tipoDescontoRel;
	}

	public Boolean getConsultaLogContaReceber() {
		return consultaLogContaReceber;
	}

	public void setConsultaLogContaReceber(Boolean consultaLogContaReceber) {
		this.consultaLogContaReceber = consultaLogContaReceber;
	}

	/**
	 * @return the mapaSuspensaoMatricula
	 */
	public Boolean getMapaSuspensaoMatricula() {
		return mapaSuspensaoMatricula;
	}

	/**
	 * @param mapaSuspensaoMatricula the mapaSuspensaoMatricula to set
	 */
	public void setMapaSuspensaoMatricula(Boolean mapaSuspensaoMatricula) {
		this.mapaSuspensaoMatricula = mapaSuspensaoMatricula;
	}

	/**
	 * @return the matriculaSerasa
	 */
	public Boolean getMatriculaSerasa() {
		return matriculaSerasa;
	}

	/**
	 * @param matriculaSerasa the matriculaSerasa to set
	 */
	public void setMatriculaSerasa(Boolean matriculaSerasa) {
		this.matriculaSerasa = matriculaSerasa;
	}

	public Boolean getMatriculaLiberacao() {
		return matriculaLiberacao;
	}

	public void setMatriculaLiberacao(Boolean matriculaLiberacao) {
		this.matriculaLiberacao = matriculaLiberacao;
	}

	public Boolean getPainelAluno() {
		return painelAluno;
	}

	public void setPainelAluno(Boolean painelAluno) {
		this.painelAluno = painelAluno;
	}

	public Boolean getComprovanteInscricao() {
		return comprovanteInscricao;
	}

	public void setComprovanteInscricao(Boolean comprovanteInscricao) {
		this.comprovanteInscricao = comprovanteInscricao;
	}

	public Boolean getResultadoProcessoSeletivoRel() {
		return resultadoProcessoSeletivoRel;
	}

	public void setResultadoProcessoSeletivoRel(Boolean resultadoProcessoSeletivoRel) {
		this.resultadoProcessoSeletivoRel = resultadoProcessoSeletivoRel;
	}

	public Boolean getUploadProfessorRel() {
		return uploadProfessorRel;
	}

	public void setUploadProfessorRel(Boolean uploadProfessorRel) {
		this.uploadProfessorRel = uploadProfessorRel;
	}

	public Boolean getFaltasAlunosRel() {
		return faltasAlunosRel;
	}

	public void setFaltasAlunosRel(Boolean faltasAlunosRel) {
		this.faltasAlunosRel = faltasAlunosRel;
	}

	public Boolean getPlanoFinanceiroReposicao() {
		return planoFinanceiroReposicao;
	}

	public void setPlanoFinanceiroReposicao(Boolean planoFinanceiroReposicao) {
		this.planoFinanceiroReposicao = planoFinanceiroReposicao;
	}

	public Boolean getRequisicaoRel() {
		return requisicaoRel;
	}

	public void setRequisicaoRel(Boolean requisicaoRel) {
		this.requisicaoRel = requisicaoRel;
	}

	/**
	 * @return the mapaNotaAlunoPorTurmaRel
	 */
	public Boolean getMapaNotaAlunoPorTurmaRel() {
		return mapaNotaAlunoPorTurmaRel;
	}

	/**
	 * @param mapaNotaAlunoPorTurmaRel the mapaNotaAlunoPorTurmaRel to set
	 */
	public void setMapaNotaAlunoPorTurmaRel(Boolean mapaNotaAlunoPorTurmaRel) {
		this.mapaNotaAlunoPorTurmaRel = mapaNotaAlunoPorTurmaRel;
	}

	/**
	 * @return the mapaNotasDisciplinaAlunosRel
	 */
	public Boolean getMapaNotasDisciplinaAlunosRel() {
		return mapaNotasDisciplinaAlunosRel;
	}

	/**
	 * @param mapaNotasDisciplinaAlunosRel the mapaNotasDisciplinaAlunosRel to set
	 */
	public void setMapaNotasDisciplinaAlunosRel(Boolean mapaNotasDisciplinaAlunosRel) {
		this.mapaNotasDisciplinaAlunosRel = mapaNotasDisciplinaAlunosRel;
	}

	public Boolean getMapaAtualizacaoMatriculaFormada() {
		return mapaAtualizacaoMatriculaFormada;
	}

	public void setMapaAtualizacaoMatriculaFormada(Boolean mapaAtualizacaoMatriculaFormada) {
		this.mapaAtualizacaoMatriculaFormada = mapaAtualizacaoMatriculaFormada;
	}

	public Boolean getVoltarEtapaAnterior() {
		return voltarEtapaAnterior;
	}

	public void setVoltarEtapaAnterior(Boolean voltarEtapaAnterior) {
		this.voltarEtapaAnterior = voltarEtapaAnterior;
	}

	public Boolean getContatoAniversariante() {
		return contatoAniversariante;
	}

	public void setContatoAniversariante(Boolean contatoAniversariante) {
		this.contatoAniversariante = contatoAniversariante;
	}

	public Boolean getDescontoInclusaoReposicaoForaPrazo() {
		return descontoInclusaoReposicaoForaPrazo;
	}

	public void setDescontoInclusaoReposicaoForaPrazo(Boolean descontoInclusaoReposicaoForaPrazo) {
		this.descontoInclusaoReposicaoForaPrazo = descontoInclusaoReposicaoForaPrazo;
	}

	public Boolean getLogTurma() {
		return logTurma;
	}

	public void setLogTurma(Boolean logTurma) {
		this.logTurma = logTurma;
	}

	public Boolean getContaReceberNaoLocalizadaArquivoRetorno() {
		return contaReceberNaoLocalizadaArquivoRetorno;
	}

	public void setContaReceberNaoLocalizadaArquivoRetorno(Boolean contaReceberNaoLocalizadaArquivoRetorno) {
		this.contaReceberNaoLocalizadaArquivoRetorno = contaReceberNaoLocalizadaArquivoRetorno;
	}

	public Boolean getBuscaProspect() {
		return buscaProspect;
	}

	public void setBuscaProspect(Boolean buscaProspect) {
		this.buscaProspect = buscaProspect;
	}

	public Boolean getExemplaresPorCursoRel() {
		if (exemplaresPorCursoRel == null) {
			exemplaresPorCursoRel = Boolean.FALSE;
		}
		return exemplaresPorCursoRel;
	}

	public void setExemplaresPorCursoRel(Boolean exemplaresPorCursoRel) {
		this.exemplaresPorCursoRel = exemplaresPorCursoRel;
	}

	public Boolean getCondicaoRenegociacao() {
		return condicaoRenegociacao;
	}

	public void setCondicaoRenegociacao(Boolean condicaoRenegociacao) {
		this.condicaoRenegociacao = condicaoRenegociacao;
	}

	public Boolean getCartaCobrancaRel() {
		return cartaCobrancaRel;
	}

	public void setCartaCobrancaRel(Boolean cartaCobrancaRel) {
		this.cartaCobrancaRel = cartaCobrancaRel;
	}

	public Boolean getNovoProspect() {
		return novoProspect;
	}

	public void setNovoProspect(Boolean novoProspect) {
		this.novoProspect = novoProspect;
	}

	public Boolean getPrestacaoContaTurma() {
		if (prestacaoContaTurma == null) {
			prestacaoContaTurma = false;
		}
		return prestacaoContaTurma;
	}

	public void setPrestacaoContaTurma(Boolean prestacaoContaTurma) {
		this.prestacaoContaTurma = prestacaoContaTurma;
	}

	public Boolean getSubMenuPrestacaoConta() {
		return getPrestacaoContaUnidadeEnsino() || getPrestacaoContaTurma();
	}

	public Boolean getPrestacaoContaUnidadeEnsino() {
		if (prestacaoContaUnidadeEnsino == null) {
			prestacaoContaUnidadeEnsino = false;
		}
		return prestacaoContaUnidadeEnsino;
	}

	public void setPrestacaoContaUnidadeEnsino(Boolean prestacaoContaUnidadeEnsino) {
		this.prestacaoContaUnidadeEnsino = prestacaoContaUnidadeEnsino;
	}

	public Boolean getRenovarMatriculaPorTurma() {
		return renovarMatriculaPorTurma;
	}

	public void setRenovarMatriculaPorTurma(Boolean renovarMatriculaPorTurma) {
		this.renovarMatriculaPorTurma = renovarMatriculaPorTurma;
	}

	public Boolean getInclusaoForaPrazo() {
		return inclusaoForaPrazo;
	}

	public void setInclusaoForaPrazo(Boolean inclusaoForaPrazo) {
		this.inclusaoForaPrazo = inclusaoForaPrazo;
	}

	public Boolean getExclusaoForaPrazo() {
		return exclusaoForaPrazo;
	}

	public void setExclusaoForaPrazo(Boolean exclusaoForaPrazo) {
		this.exclusaoForaPrazo = exclusaoForaPrazo;
	}

	public Boolean getAlteracoesCadastraisMatricula() {
		return alteracoesCadastraisMatricula;
	}

	public void setAlteracoesCadastraisMatricula(Boolean alteracoesCadastraisMatricula) {
		this.alteracoesCadastraisMatricula = alteracoesCadastraisMatricula;
	}

	public Boolean getDocumentacaoPendenteProfessorRel() {
		return documentacaoPendenteProfessorRel;
	}

	public void setDocumentacaoPendenteProfessorRel(Boolean documentacaoPendenteProfessorRel) {
		this.documentacaoPendenteProfessorRel = documentacaoPendenteProfessorRel;
	}

	public Boolean getMapaAlunoRel() {
		return mapaAlunoRel;
	}

	public void setMapaAlunoRel(Boolean mapaAlunoRel) {
		this.mapaAlunoRel = mapaAlunoRel;
	}

	public Boolean getGravarInteracaoFollowUp() {
		return gravarInteracaoFollowUp;
	}

	public void setGravarInteracaoFollowUp(Boolean gravarInteracaoFollowUp) {
		this.gravarInteracaoFollowUp = gravarInteracaoFollowUp;
	}

	public Boolean getBoletosRel() {
		return boletosRel;
	}

	public void setBoletosRel(Boolean boletosRel) {
		this.boletosRel = boletosRel;
	}

	public Boolean getNavegarAbaDocumentacao() {
		return navegarAbaDocumentacao;
	}

	public void setNavegarAbaDocumentacao(Boolean navegarAbaDocumentacao) {
		this.navegarAbaDocumentacao = navegarAbaDocumentacao;
	}

	public Boolean getNavegarAbaDisciplinas() {
		return navegarAbaDisciplinas;
	}

	public void setNavegarAbaDisciplinas(Boolean navegarAbaDisciplinas) {
		this.navegarAbaDisciplinas = navegarAbaDisciplinas;
	}

	public Boolean getNavegarAbaPlanoFinanceiroAluno() {
		// Correção de situação onde sem dar permissão para essa funcionalidade o bota gravar não é apresentando, ocasionando o problema de nao salvar a matricula.
		// em conversa com o lucas definimos que poderá sempre ir para a tela do financeiro, porem sem editar nada, e assim sempre gravando a matricula.
		// return navegarAbaPlanoFinanceiroAluno;
		return true;
	}

	public void setNavegarAbaPlanoFinanceiroAluno(Boolean navegarAbaPlanoFinanceiroAluno) {
		this.navegarAbaPlanoFinanceiroAluno = navegarAbaPlanoFinanceiroAluno;
	}

	public Boolean getExtratoContaCorrenteRel() {
		return extratoContaCorrenteRel;
	}

	public void setExtratoContaCorrenteRel(Boolean extratoContaCorrenteRel) {
		this.extratoContaCorrenteRel = extratoContaCorrenteRel;
	}

	public Boolean getCategoriaDespesaRel() {
		return categoriaDespesaRel;
	}

	public void setCategoriaDespesaRel(Boolean categoriaDespesaRel) {
		this.categoriaDespesaRel = categoriaDespesaRel;
	}

	public Boolean getDataComemorativa() {
		return dataComemorativa;
	}

	public void setDataComemorativa(Boolean dataComemorativa) {
		this.dataComemorativa = dataComemorativa;
	}

	public Boolean getNavegarAbaPendenciaFinanceira() {
		return navegarAbaPendenciaFinanceira;
	}

	public void setNavegarAbaPendenciaFinanceira(Boolean navegarAbaPendenciaFinanceira) {
		this.navegarAbaPendenciaFinanceira = navegarAbaPendenciaFinanceira;
	}

	/**
	 * @return the inscricaoCandidato
	 */
	public Boolean getInscricaoCandidato() {
		return inscricaoCandidato;
	}

	/**
	 * @param inscricaoCandidato the inscricaoCandidato to set
	 */
	public void setInscricaoCandidato(Boolean inscricaoCandidato) {
		this.inscricaoCandidato = inscricaoCandidato;
	}

	/**
	 * @return the resultadoCandidato
	 */
	public Boolean getResultadoCandidato() {
		return resultadoCandidato;
	}

	/**
	 * @param resultadoCandidato the resultadoCandidato to set
	 */
	public void setResultadoCandidato(Boolean resultadoCandidato) {
		this.resultadoCandidato = resultadoCandidato;
	}

	/**
	 * @return the calendarioCandidato
	 */
	public Boolean getCalendarioCandidato() {
		return calendarioCandidato;
	}

	/**
	 * @param calendarioCandidato the calendarioCandidato to set
	 */
	public void setCalendarioCandidato(Boolean calendarioCandidato) {
		this.calendarioCandidato = calendarioCandidato;
	}

	/**
	 * @return the boletoCandidato
	 */
	public Boolean getBoletoCandidato() {
		return boletoCandidato;
	}

	/**
	 * @param boletoCandidato the boletoCandidato to set
	 */
	public void setBoletoCandidato(Boolean boletoCandidato) {
		this.boletoCandidato = boletoCandidato;
	}

	/**
	 * @return the cursosCandidato
	 */
	public Boolean getCursosCandidato() {
		return cursosCandidato;
	}

	/**
	 * @param cursosCandidato the cursosCandidato to set
	 */
	public void setCursosCandidato(Boolean cursosCandidato) {
		this.cursosCandidato = cursosCandidato;
	}

	/**
	 * @return the comprovanteInscricaoCandidato
	 */
	public Boolean getComprovanteInscricaoCandidato() {
		return comprovanteInscricaoCandidato;
	}

	/**
	 * @param comprovanteInscricaoCandidato the comprovanteInscricaoCandidato to set
	 */
	public void setComprovanteInscricaoCandidato(Boolean comprovanteInscricaoCandidato) {
		this.comprovanteInscricaoCandidato = comprovanteInscricaoCandidato;
	}

	public Boolean getDeclaracaoImpostoRendaRel() {
		return declaracaoImpostoRendaRel;
	}

	public void setDeclaracaoImpostoRendaRel(Boolean declaracaoImpostoRendaRel) {
		this.declaracaoImpostoRendaRel = declaracaoImpostoRendaRel;
	}

	public Boolean getConfiguracaoAtendimento() {
		return configuracaoAtendimento;
	}

	public void setConfiguracaoAtendimento(Boolean configuracaoAtendimento) {
		this.configuracaoAtendimento = configuracaoAtendimento;
	}

	public Boolean getOuvidoria() {
		return ouvidoria;
	}

	public void setOuvidoria(Boolean ouvidoria) {
		this.ouvidoria = ouvidoria;
	}

	public Boolean getTipagemOuvidoria() {
		return tipagemOuvidoria;
	}

	public void setTipagemOuvidoria(Boolean tipagemOuvidoria) {
		this.tipagemOuvidoria = tipagemOuvidoria;
	}

	public Boolean getAlterarDataMovimentacaoFinanceira() {
		return alterarDataMovimentacaoFinanceira;
	}

	public void setAlterarDataMovimentacaoFinanceira(Boolean alterarDataMovimentacaoFinanceira) {
		this.alterarDataMovimentacaoFinanceira = alterarDataMovimentacaoFinanceira;
	}

	public Boolean getMudarDataFimPeriodoLetivo() {
		return mudarDataFimPeriodoLetivo;
	}

	public void setMudarDataFimPeriodoLetivo(Boolean mudarDataFimPeriodoLetivo) {
		this.mudarDataFimPeriodoLetivo = mudarDataFimPeriodoLetivo;
	}

	public Boolean getConteudo() {

		return conteudo;
	}

	public void setConteudo(Boolean conteudo) {
		this.conteudo = conteudo;
	}

	public Boolean getContaReceber_PermitirEditarManualmenteConta() {
		return contaReceber_PermitirEditarManualmenteConta;
	}

	public void setContaReceber_PermitirEditarManualmenteConta(Boolean contaReceber_PermitirEditarManualmenteConta) {
		this.contaReceber_PermitirEditarManualmenteConta = contaReceber_PermitirEditarManualmenteConta;
	}

	public Boolean getContaReceber_PermitirAlterarDataVencimento() {
		return contaReceber_PermitirAlterarDataVencimento;
	}

	public void setContaReceber_PermitirAlterarDataVencimento(Boolean contaReceber_PermitirAlterarDataVencimento) {
		this.contaReceber_PermitirAlterarDataVencimento = contaReceber_PermitirAlterarDataVencimento;
	}

	public Boolean getEstornarMovimentacaoFinanceira() {
		return estornarMovimentacaoFinanceira;
	}

	public void setEstornarMovimentacaoFinanceira(Boolean estornarMovimentacaoFinanceira) {
		this.estornarMovimentacaoFinanceira = estornarMovimentacaoFinanceira;
	}

	public Boolean getAlterarObservacaoInteracaoFollowUp() {
		return alterarObservacaoInteracaoFollowUp;
	}

	public void setAlterarObservacaoInteracaoFollowUp(Boolean alterarObservacaoInteracaoFollowUp) {
		this.alterarObservacaoInteracaoFollowUp = alterarObservacaoInteracaoFollowUp;
	}

	public Boolean getForum() {
		return forum;
	}

	public void setForum(Boolean forum) {
		this.forum = forum;
	}

	public Boolean getForumProfessor() {
		return forumProfessor;
	}

	public void setForumProfessor(Boolean forumProfessor) {
		this.forumProfessor = forumProfessor;
	}

	public Boolean getForumAluno() {
		return forumAluno;
	}

	public void setForumAluno(Boolean forumAluno) {
		this.forumAluno = forumAluno;
	}

	public Boolean getAlterarTemaForum() {
		return alterarTemaForum;
	}

	public void setAlterarTemaForum(Boolean alterarTemaForum) {
		this.alterarTemaForum = alterarTemaForum;
	}

	public Boolean getAlterarTemaForumProfessor() {
		return alterarTemaForumProfessor;
	}

	public void setAlterarTemaForumProfessor(Boolean alterarTemaForumProfessor) {
		this.alterarTemaForumProfessor = alterarTemaForumProfessor;
	}

	public Boolean getFollowMe() {
		return followMe;
	}

	public void setFollowMe(Boolean followMe) {
		this.followMe = followMe;
	}

	public Boolean getInformacaoProfessorRel() {
		return informacaoProfessorRel;
	}

	public void setInformacaoProfessorRel(Boolean informacaoProfessorRel) {
		this.informacaoProfessorRel = informacaoProfessorRel;
	}

	public Boolean getQuestaoExercicio() {
		return questaoExercicio;
	}

	public void setQuestaoExercicio(Boolean questaoExercicio) {
		this.questaoExercicio = questaoExercicio;
	}

	public Boolean getQuestaoOnline() {
		return questaoOnline;
	}

	public void setQuestaoOnline(Boolean questaoOnline) {
		this.questaoOnline = questaoOnline;
	}

	public Boolean getQuestaoPresencial() {
		return questaoPresencial;
	}

	public void setQuestaoPresencial(Boolean questaoPresencial) {
		this.questaoPresencial = questaoPresencial;
	}

	public Boolean getAlterarExercicioOutroProfessor() {
		return alterarExercicioOutroProfessor;
	}

	public void setAlterarExercicioOutroProfessor(Boolean alterarExercicioOutroProfessor) {
		this.alterarExercicioOutroProfessor = alterarExercicioOutroProfessor;
	}

	public Boolean getInativarExercicio() {
		return inativarExercicio;
	}

	public void setInativarExercicio(Boolean inativarExercicio) {
		this.inativarExercicio = inativarExercicio;
	}

	public Boolean getAlterarAtividadeDiscursivaOutroProfessor() {
		return alterarAtividadeDiscursivaOutroProfessor;
	}

	public void setAlterarAtividadeDiscursivaOutroProfessor(Boolean alterarAtividadeDiscursivaOutroProfessor) {
		this.alterarAtividadeDiscursivaOutroProfessor = alterarAtividadeDiscursivaOutroProfessor;
	}

	public Boolean getInativarAtividadeDiscursiva() {
		return inativarAtividadeDiscursiva;
	}

	public void setInativarAtividadeDiscursiva(Boolean inativarAtividadeDiscursiva) {
		this.inativarAtividadeDiscursiva = inativarAtividadeDiscursiva;
	}

	public Boolean getAlterarQuestaoOnlineOutroProfessor() {
		return alterarQuestaoOnlineOutroProfessor;
	}

	public void setAlterarQuestaoOnlineOutroProfessor(Boolean alterarQuestaoOnlineOutroProfessor) {
		this.alterarQuestaoOnlineOutroProfessor = alterarQuestaoOnlineOutroProfessor;
	}

	public Boolean getAlterarQuestaoPresencialOutroProfessor() {
		return alterarQuestaoPresencialOutroProfessor;
	}

	public void setAlterarQuestaoPresencialOutroProfessor(Boolean alterarQuestaoPresencialOutroProfessor) {
		this.alterarQuestaoPresencialOutroProfessor = alterarQuestaoPresencialOutroProfessor;
	}

	public Boolean getInativarQuestaoOnline() {
		return inativarQuestaoOnline;
	}

	public void setInativarQuestaoOnline(Boolean inativarQuestaoOnline) {
		this.inativarQuestaoOnline = inativarQuestaoOnline;
	}

	public Boolean getCancelarQuestaoOnline() {
		return cancelarQuestaoOnline;
	}

	public void setCancelarQuestaoOnline(Boolean cancelarQuestaoOnline) {
		this.cancelarQuestaoOnline = cancelarQuestaoOnline;
	}

	public Boolean getInativarQuestaoPresencial() {
		return inativarQuestaoPresencial;
	}

	public void setInativarQuestaoPresencial(Boolean inativarQuestaoPresencial) {
		this.inativarQuestaoPresencial = inativarQuestaoPresencial;
	}

	public Boolean getCancelarQuestaoPresencial() {
		return cancelarQuestaoPresencial;
	}

	public void setCancelarQuestaoPresencial(Boolean cancelarQuestaoPresencial) {
		this.cancelarQuestaoPresencial = cancelarQuestaoPresencial;
	}

	public Boolean getListaExercicio() {
		return listaExercicio;
	}

	public void setListaExercicio(Boolean listaExercicio) {
		this.listaExercicio = listaExercicio;
	}

	public Boolean getListaExercicioProfessor() {
		return listaExercicioProfessor;
	}

	public void setListaExercicioProfessor(Boolean listaExercicioProfessor) {
		this.listaExercicioProfessor = listaExercicioProfessor;
	}

	public Boolean getListaExercicioAluno() {
		return listaExercicioAluno;
	}

	public void setListaExercicioAluno(Boolean listaExercicioAluno) {
		this.listaExercicioAluno = listaExercicioAluno;
	}

	public Boolean getAlterarListaExercicioOutroProfessor() {
		return alterarListaExercicioOutroProfessor;
	}

	public void setAlterarListaExercicioOutroProfessor(Boolean alterarListaExercicioOutroProfessor) {
		this.alterarListaExercicioOutroProfessor = alterarListaExercicioOutroProfessor;
	}

	public Boolean getAlterarListaExercicioOutroProfessorProfessor() {
		return alterarListaExercicioOutroProfessorProfessor;
	}

	public void setAlterarListaExercicioOutroProfessorProfessor(Boolean alterarListaExercicioOutroProfessorProfessor) {
		this.alterarListaExercicioOutroProfessorProfessor = alterarListaExercicioOutroProfessorProfessor;
	}

	public Boolean getInativarListaExercicio() {
		return inativarListaExercicio;
	}

	public void setInativarListaExercicio(Boolean inativarListaExercicio) {
		this.inativarListaExercicio = inativarListaExercicio;
	}

	public Boolean getInativarListaExercicioProfessor() {
		return inativarListaExercicioProfessor;
	}

	public void setInativarListaExercicioProfessor(Boolean inativarListaExercicioProfessor) {
		this.inativarListaExercicioProfessor = inativarListaExercicioProfessor;
	}

	public Boolean getPainelGestorConsumo() {
		return painelGestorConsumo;
	}

	public void setPainelGestorConsumo(Boolean painelGestorConsumo) {
		this.painelGestorConsumo = painelGestorConsumo;
	}

	public Boolean getPainelGestorDespesaCategoria() {
		return painelGestorDespesaCategoria;
	}

	public void setPainelGestorDespesaCategoria(Boolean painelGestorDespesaCategoria) {
		this.painelGestorDespesaCategoria = painelGestorDespesaCategoria;
	}

	public Boolean getPainelGestorPlanoOrcamentario() {
		return painelGestorPlanoOrcamentario;
	}

	public void setPainelGestorPlanoOrcamentario(Boolean painelGestorPlanoOrcamentario) {
		this.painelGestorPlanoOrcamentario = painelGestorPlanoOrcamentario;
	}

	public Boolean getContaReceber_PermitirEnvioEmailCobranca() {
		return contaReceber_PermitirEnvioEmailCobranca;
	}

	public void setContaReceber_PermitirEnvioEmailCobranca(Boolean contaReceber_PermitirEnvioEmailCobranca) {
		this.contaReceber_PermitirEnvioEmailCobranca = contaReceber_PermitirEnvioEmailCobranca;
	}

	public Boolean getDuvidaProfessor() {
		return duvidaProfessor;
	}

	public void setDuvidaProfessor(Boolean duvidaProfessor) {
		this.duvidaProfessor = duvidaProfessor;
	}

	public Boolean getDuvidaProfessorAluno() {
		return duvidaProfessorAluno;
	}

	public void setDuvidaProfessorAluno(Boolean duvidaProfessorAluno) {
		this.duvidaProfessorAluno = duvidaProfessorAluno;
	}

	public Boolean getCalendarioLancamentoNota() {
		return calendarioLancamentoNota;
	}

	public void setCalendarioLancamentoNota(Boolean calendarioLancamentoNota) {
		this.calendarioLancamentoNota = calendarioLancamentoNota;
	}

	public Boolean getLayoutEtiqueta() {
		return layoutEtiqueta;
	}

	public void setLayoutEtiqueta(Boolean layoutEtiqueta) {
		this.layoutEtiqueta = layoutEtiqueta;
	}

	public Boolean getMatriculaEnade() {
		return matriculaEnade;
	}

	public void setMatriculaEnade(Boolean matriculaEnade) {
		this.matriculaEnade = matriculaEnade;
	}

	public Boolean getDistribuirSalaProcessoSeletivo() {
		return distribuirSalaProcessoSeletivo;
	}

	public void setDistribuirSalaProcessoSeletivo(Boolean distribuirSalaProcessoSeletivo) {
		this.distribuirSalaProcessoSeletivo = distribuirSalaProcessoSeletivo;
	}

	public Boolean getCancelarQuestaoProcessoSeletivo() {
		return cancelarQuestaoProcessoSeletivo;
	}

	public void setCancelarQuestaoProcessoSeletivo(Boolean cancelarQuestaoProcessoSeletivo) {
		this.cancelarQuestaoProcessoSeletivo = cancelarQuestaoProcessoSeletivo;
	}

	public Boolean getInativarQuestaoProcessoSeletivo() {
		return inativarQuestaoProcessoSeletivo;
	}

	public void setInativarQuestaoProcessoSeletivo(Boolean inativarQuestaoProcessoSeletivo) {
		this.inativarQuestaoProcessoSeletivo = inativarQuestaoProcessoSeletivo;
	}

	public Boolean getQuestaoProcessoSeletivo() {
		return questaoProcessoSeletivo;
	}

	public void setQuestaoProcessoSeletivo(Boolean questaoProcessoSeletivo) {
		this.questaoProcessoSeletivo = questaoProcessoSeletivo;
	}

	public Boolean getAtivarQuestaoProcessoSeletivo() {
		return ativarQuestaoProcessoSeletivo;
	}

	public void setAtivarQuestaoProcessoSeletivo(Boolean ativarQuestaoProcessoSeletivo) {
		this.ativarQuestaoProcessoSeletivo = ativarQuestaoProcessoSeletivo;
	}

	public Boolean getProvaProcessoSeletivo() {
		return provaProcessoSeletivo;
	}

	public void setProvaProcessoSeletivo(Boolean provaProcessoSeletivo) {
		this.provaProcessoSeletivo = provaProcessoSeletivo;
	}

	public Boolean getInativarProvaProcessoSeletivo() {
		return inativarProvaProcessoSeletivo;
	}

	public void setInativarProvaProcessoSeletivo(Boolean inativarProvaProcessoSeletivo) {
		this.inativarProvaProcessoSeletivo = inativarProvaProcessoSeletivo;
	}

	public Boolean getAtivarProvaProcessoSeletivo() {
		return ativarProvaProcessoSeletivo;
	}

	public void setAtivarProvaProcessoSeletivo(Boolean ativarProvaProcessoSeletivo) {
		this.ativarProvaProcessoSeletivo = ativarProvaProcessoSeletivo;
	}

	public Boolean getEstatisticaProcessoSeletivo() {
		return estatisticaProcessoSeletivo;
	}

	public void setEstatisticaProcessoSeletivo(Boolean estatisticaProcessoSeletivo) {
		this.estatisticaProcessoSeletivo = estatisticaProcessoSeletivo;
	}

	public Boolean getGabarito() {
		return gabarito;
	}

	public void setGabarito(Boolean gabarito) {
		this.gabarito = gabarito;
	}

	public Boolean getContaReceber_SimularAlteracao() {
		return contaReceber_SimularAlteracao;
	}

	public void setContaReceber_SimularAlteracao(Boolean contaReceber_SimularAlteracao) {
		this.contaReceber_SimularAlteracao = contaReceber_SimularAlteracao;
	}

	public Boolean getMapaRecebimentoContaReceberDuplicidade() {
		return mapaRecebimentoContaReceberDuplicidade;
	}

	public void setMapaRecebimentoContaReceberDuplicidade(Boolean mapaRecebimentoContaReceberDuplicidade) {
		this.mapaRecebimentoContaReceberDuplicidade = mapaRecebimentoContaReceberDuplicidade;
	}

	public Boolean getDemonstrativoResultadoFinanceiro() {
		return demonstrativoResultadoFinanceiro;
	}

	public void setDemonstrativoResultadoFinanceiro(Boolean demonstrativoResultadoFinanceiro) {
		this.demonstrativoResultadoFinanceiro = demonstrativoResultadoFinanceiro;
	}

	public Boolean getMapaDeTurmasEncerradasRel() {
		return mapaDeTurmasEncerradasRel;
	}

	public void setMapaDeTurmasEncerradasRel(Boolean mapaDeTurmasEncerradasRel) {
		this.mapaDeTurmasEncerradasRel = mapaDeTurmasEncerradasRel;
	}

	public Boolean getSolicitarAberturaTurma() {
		return solicitarAberturaTurma;
	}

	public void setSolicitarAberturaTurma(Boolean solicitarAberturaTurma) {
		this.solicitarAberturaTurma = solicitarAberturaTurma;
	}

	public Boolean getAutorizarSolicitacaoAberturaTurma() {
		return autorizarSolicitacaoAberturaTurma;
	}

	public void setAutorizarSolicitacaoAberturaTurma(Boolean autorizarSolicitacaoAberturaTurma) {
		this.autorizarSolicitacaoAberturaTurma = autorizarSolicitacaoAberturaTurma;
	}

	public Boolean getNaoAutorizarSolicitacaoAberturaTurma() {
		return naoAutorizarSolicitacaoAberturaTurma;
	}

	public void setNaoAutorizarSolicitacaoAberturaTurma(Boolean naoAutorizarSolicitacaoAberturaTurma) {
		this.naoAutorizarSolicitacaoAberturaTurma = naoAutorizarSolicitacaoAberturaTurma;
	}

	public Boolean getRevisarSolicitacaoAberturaTurma() {
		return revisarSolicitacaoAberturaTurma;
	}

	public void setRevisarSolicitacaoAberturaTurma(Boolean revisarSolicitacaoAberturaTurma) {
		this.revisarSolicitacaoAberturaTurma = revisarSolicitacaoAberturaTurma;
	}

	public Boolean getChamadaCandidatoAprovadoRel() {
		return chamadaCandidatoAprovadoRel;
	}

	public void setChamadaCandidatoAprovadoRel(Boolean chamadaCandidatoAprovadoRel) {
		this.chamadaCandidatoAprovadoRel = chamadaCandidatoAprovadoRel;
	}

	public Boolean getFinalizarSolicitacaoAberturaTurma() {
		return finalizarSolicitacaoAberturaTurma;
	}

	public void setFinalizarSolicitacaoAberturaTurma(Boolean finalizarSolicitacaoAberturaTurma) {
		this.finalizarSolicitacaoAberturaTurma = finalizarSolicitacaoAberturaTurma;
	}

	public Boolean getPermiteRevisarSolicitacaoAberturaTurma() {
		return permiteRevisarSolicitacaoAberturaTurma;
	}

	public void setPermiteRevisarSolicitacaoAberturaTurma(Boolean permiteRevisarSolicitacaoAberturaTurma) {
		this.permiteRevisarSolicitacaoAberturaTurma = permiteRevisarSolicitacaoAberturaTurma;
	}

	/**
	 * @return the controleGeracaoParcelaTurma
	 */
	public Boolean getControleGeracaoParcelaTurma() {
		return controleGeracaoParcelaTurma;
	}

	/**
	 * @param controleGeracaoParcelaTurma the controleGeracaoParcelaTurma to set
	 */
	public void setControleGeracaoParcelaTurma(Boolean controleGeracaoParcelaTurma) {
		this.controleGeracaoParcelaTurma = controleGeracaoParcelaTurma;
	}

	public Boolean getLocalAula() {
		return localAula;
	}

	public void setLocalAula(Boolean localAula) {
		this.localAula = localAula;
	}

	public Boolean getPlanoDisciplinaRel() {
		return planoDisciplinaRel;
	}

	public void setPlanoDisciplinaRel(Boolean planoDisciplinaRel) {
		this.planoDisciplinaRel = planoDisciplinaRel;
	}

	public Boolean getEtiquetaAlunoRel() {
		return etiquetaAlunoRel;
	}

	public void setEtiquetaAlunoRel(Boolean etiquetaAlunoRel) {
		this.etiquetaAlunoRel = etiquetaAlunoRel;
	}

	public Boolean getMapaLocalAulaTurma() {
		return mapaLocalAulaTurma;
	}

	public void setMapaLocalAulaTurma(Boolean mapaLocalAulaTurma) {
		this.mapaLocalAulaTurma = mapaLocalAulaTurma;
	}

	public Boolean getMapaAlunoAptoFormar() {
		return mapaAlunoAptoFormar;
	}

	public void setMapaAlunoAptoFormar(Boolean mapaAlunoAptoFormar) {
		this.mapaAlunoAptoFormar = mapaAlunoAptoFormar;
	}

	public Boolean getTipoContato() {
		return tipoContato;
	}

	public void setTipoContato(Boolean tipoContato) {
		this.tipoContato = tipoContato;
	}

	public Boolean getQuestionarioRel() {
		return questionarioRel;
	}

	public void setQuestionarioRel(Boolean questionarioRel) {
		this.questionarioRel = questionarioRel;
	}

	public Boolean getContaRecebidaVersoContaReceberRel() {
		return contaRecebidaVersoContaReceberRel;
	}

	public void setContaRecebidaVersoContaReceberRel(Boolean contaRecebidaVersoContaReceberRel) {
		this.contaRecebidaVersoContaReceberRel = contaRecebidaVersoContaReceberRel;
	}

	public Boolean getDeclaracaoTransferenciaInterna() {
		return declaracaoTransferenciaInterna;
	}

	public void setDeclaracaoTransferenciaInterna(Boolean declaracaoTransferenciaInterna) {
		this.declaracaoTransferenciaInterna = declaracaoTransferenciaInterna;
	}

	public Boolean getAvaliacaoInstitucionalAnaliticoRel() {
		return avaliacaoInstitucionalAnaliticoRel;
	}

	public void setAvaliacaoInstitucionalAnaliticoRel(Boolean avaliacaoInstitucionalAnaliticoRel) {
		this.avaliacaoInstitucionalAnaliticoRel = avaliacaoInstitucionalAnaliticoRel;
	}

	public Boolean getPlanoEnsino() {
		return planoEnsino;
	}

	public void setPlanoEnsino(Boolean planoEnsino) {
		this.planoEnsino = planoEnsino;
	}

	public Boolean getPermiteIsentarMultaBiblioteca() {
		return permiteIsentarMultaBiblioteca;
	}

	public void setPermiteIsentarMultaBiblioteca(Boolean permiteIsentarMultaBiblioteca) {
		this.permiteIsentarMultaBiblioteca = permiteIsentarMultaBiblioteca;
	}

	public Boolean getCalendarioRegistroAula() {
		return calendarioRegistroAula;
	}

	public void setCalendarioRegistroAula(Boolean calendarioRegistroAula) {
		this.calendarioRegistroAula = calendarioRegistroAula;
	}

	/**
	 * @return the cursoFavorito
	 */
	public Boolean getCursoFavorito() {
		return cursoFavorito;
	}

	/**
	 * @param cursoFavorito the cursoFavorito to set
	 */
	public void setCursoFavorito(Boolean cursoFavorito) {
		this.cursoFavorito = cursoFavorito;
	}

	/**
	 * @return the conteudoFavorito
	 */
	public Boolean getConteudoFavorito() {
		return conteudoFavorito;
	}

	/**
	 * @param conteudoFavorito the conteudoFavorito to set
	 */
	public void setConteudoFavorito(Boolean conteudoFavorito) {
		this.conteudoFavorito = conteudoFavorito;
	}

	/**
	 * @return the alunoFavorito
	 */
	public Boolean getAlunoFavorito() {
		return alunoFavorito;
	}

	/**
	 * @param alunoFavorito the alunoFavorito to set
	 */
	public void setAlunoFavorito(Boolean alunoFavorito) {
		this.alunoFavorito = alunoFavorito;
	}

	/**
	 * @return the forumFavorito
	 */
	public Boolean getForumFavorito() {
		return forumFavorito;
	}

	/**
	 * @param forumFavorito the forumFavorito to set
	 */
	public void setForumFavorito(Boolean forumFavorito) {
		this.forumFavorito = forumFavorito;
	}

	/**
	 * @return the localAulaFavorito
	 */
	public Boolean getLocalAulaFavorito() {
		return localAulaFavorito;
	}

	/**
	 * @param localAulaFavorito the localAulaFavorito to set
	 */
	public void setLocalAulaFavorito(Boolean localAulaFavorito) {
		this.localAulaFavorito = localAulaFavorito;
	}

	/**
	 * @return the logTurmaFavorito
	 */
	public Boolean getLogTurmaFavorito() {
		return logTurmaFavorito;
	}

	/**
	 * @param logTurmaFavorito the logTurmaFavorito to set
	 */
	public void setLogTurmaFavorito(Boolean logTurmaFavorito) {
		this.logTurmaFavorito = logTurmaFavorito;
	}

	/**
	 * @return the mapaDeTurmasEncerradasRelFavorito
	 */
	public Boolean getMapaDeTurmasEncerradasRelFavorito() {
		return mapaDeTurmasEncerradasRelFavorito;
	}

	/**
	 * @param mapaDeTurmasEncerradasRelFavorito the mapaDeTurmasEncerradasRelFavorito to set
	 */
	public void setMapaDeTurmasEncerradasRelFavorito(Boolean mapaDeTurmasEncerradasRelFavorito) {
		this.mapaDeTurmasEncerradasRelFavorito = mapaDeTurmasEncerradasRelFavorito;
	}

	/**
	 * @return the mapaLocalAulaTurmaFavorito
	 */
	public Boolean getMapaLocalAulaTurmaFavorito() {
		return mapaLocalAulaTurmaFavorito;
	}

	/**
	 * @param mapaLocalAulaTurmaFavorito the mapaLocalAulaTurmaFavorito to set
	 */
	public void setMapaLocalAulaTurmaFavorito(Boolean mapaLocalAulaTurmaFavorito) {
		this.mapaLocalAulaTurmaFavorito = mapaLocalAulaTurmaFavorito;
	}

	public Boolean getMapaAlunoAptoFormarFavorito() {
		return mapaAlunoAptoFormarFavorito;
	}

	public void setMapaAlunoAptoFormarFavorito(Boolean mapaAlunoAptoFormarFavorito) {
		this.mapaAlunoAptoFormarFavorito = mapaAlunoAptoFormarFavorito;
	}

	/**
	 * @return the planoEnsinoFavorito
	 */
	public Boolean getPlanoEnsinoFavorito() {
		return planoEnsinoFavorito;
	}

	/**
	 * @param planoEnsinoFavorito the planoEnsinoFavorito to set
	 */
	public void setPlanoEnsinoFavorito(Boolean planoEnsinoFavorito) {
		this.planoEnsinoFavorito = planoEnsinoFavorito;
	}

	/**
	 * @return the solicitarAberturaTurmaFavorito
	 */
	public Boolean getSolicitarAberturaTurmaFavorito() {
		return solicitarAberturaTurmaFavorito;
	}

	/**
	 * @param solicitarAberturaTurmaFavorito the solicitarAberturaTurmaFavorito to set
	 */
	public void setSolicitarAberturaTurmaFavorito(Boolean solicitarAberturaTurmaFavorito) {
		this.solicitarAberturaTurmaFavorito = solicitarAberturaTurmaFavorito;
	}

	/**
	 * @return the titulacaoCursoFavorito
	 */
	public Boolean getTitulacaoCursoFavorito() {
		return titulacaoCursoFavorito;
	}

	/**
	 * @param titulacaoCursoFavorito the titulacaoCursoFavorito to set
	 */
	public void setTitulacaoCursoFavorito(Boolean titulacaoCursoFavorito) {
		this.titulacaoCursoFavorito = titulacaoCursoFavorito;
	}

	/**
	 * @return the turnoFavorito
	 */
	public Boolean getTurnoFavorito() {
		return turnoFavorito;
	}

	/**
	 * @param turnoFavorito the turnoFavorito to set
	 */
	public void setTurnoFavorito(Boolean turnoFavorito) {
		this.turnoFavorito = turnoFavorito;
	}

	/**
	 * @return the turmaFavorito
	 */
	public Boolean getTurmaFavorito() {
		return turmaFavorito;
	}

	/**
	 * @param turmaFavorito the turmaFavorito to set
	 */
	public void setTurmaFavorito(Boolean turmaFavorito) {
		this.turmaFavorito = turmaFavorito;
	}

	public Boolean getVagaTurmaFavorito() {
		return vagaTurmaFavorito;
	}

	public void setVagaTurmaFavorito(Boolean vagaTurmaFavorito) {
		this.vagaTurmaFavorito = vagaTurmaFavorito;
	}

	/**
	 * @return the programacaoAulaFavorito
	 */
	public Boolean getProgramacaoAulaFavorito() {
		return programacaoAulaFavorito;
	}

	/**
	 * @param programacaoAulaFavorito the programacaoAulaFavorito to set
	 */
	public void setProgramacaoAulaFavorito(Boolean programacaoAulaFavorito) {
		this.programacaoAulaFavorito = programacaoAulaFavorito;
	}

	public Boolean getPermiteLeituraArquivoScanner() {
		return permiteLeituraArquivoScanner;
	}

	public void setPermiteLeituraArquivoScanner(Boolean permiteLeituraArquivoScanner) {
		this.permiteLeituraArquivoScanner = permiteLeituraArquivoScanner;
	}

	public Boolean getPermiteLeituraArquivoScannerMatricula() {
		return permiteLeituraArquivoScannerMatricula;
	}

	public void setPermiteLeituraArquivoScannerMatricula(Boolean permiteLeituraArquivoScannerMatricula) {
		this.permiteLeituraArquivoScannerMatricula = permiteLeituraArquivoScannerMatricula;
	}

	public Boolean getConfiguracaoTCC() {
		return configuracaoTCC;
	}

	public void setConfiguracaoTCC(Boolean configuracaoTCC) {
		this.configuracaoTCC = configuracaoTCC;
	}

	/**
	 * @return the candidatoFavorito
	 */
	public Boolean getCandidatoFavorito() {
		return candidatoFavorito;
	}

	/**
	 * @param candidatoFavorito the candidatoFavorito to set
	 */
	public void setCandidatoFavorito(Boolean candidatoFavorito) {
		this.candidatoFavorito = candidatoFavorito;
	}

	/**
	 * @return the inscricaoFavorito
	 */
	public Boolean getInscricaoFavorito() {
		return inscricaoFavorito;
	}

	/**
	 * @param inscricaoFavorito the inscricaoFavorito to set
	 */
	public void setInscricaoFavorito(Boolean inscricaoFavorito) {
		this.inscricaoFavorito = inscricaoFavorito;
	}

	/**
	 * @return the perfilSocioEconomicoFavorito
	 */
	public Boolean getPerfilSocioEconomicoFavorito() {
		return perfilSocioEconomicoFavorito;
	}

	/**
	 * @param perfilSocioEconomicoFavorito the perfilSocioEconomicoFavorito to set
	 */
	public void setPerfilSocioEconomicoFavorito(Boolean perfilSocioEconomicoFavorito) {
		this.perfilSocioEconomicoFavorito = perfilSocioEconomicoFavorito;
	}

	/**
	 * @return the disciplinasProcSeletivoFavorito
	 */
	public Boolean getDisciplinasProcSeletivoFavorito() {
		return disciplinasProcSeletivoFavorito;
	}

	/**
	 * @param disciplinasProcSeletivoFavorito the disciplinasProcSeletivoFavorito to set
	 */
	public void setDisciplinasProcSeletivoFavorito(Boolean disciplinasProcSeletivoFavorito) {
		this.disciplinasProcSeletivoFavorito = disciplinasProcSeletivoFavorito;
	}

	/**
	 * @return the procSeletivoFavorito
	 */
	public Boolean getProcSeletivoFavorito() {
		return procSeletivoFavorito;
	}

	/**
	 * @param procSeletivoFavorito the procSeletivoFavorito to set
	 */
	public void setProcSeletivoFavorito(Boolean procSeletivoFavorito) {
		this.procSeletivoFavorito = procSeletivoFavorito;
	}

	/**
	 * @return the resultadoProcessoSeletivoFavorito
	 */
	public Boolean getResultadoProcessoSeletivoFavorito() {
		return resultadoProcessoSeletivoFavorito;
	}

	/**
	 * @param resultadoProcessoSeletivoFavorito the resultadoProcessoSeletivoFavorito to set
	 */
	public void setResultadoProcessoSeletivoFavorito(Boolean resultadoProcessoSeletivoFavorito) {
		this.resultadoProcessoSeletivoFavorito = resultadoProcessoSeletivoFavorito;
	}

	/**
	 * @return the resultadoProcessoSeletivoRelFavorito
	 */
	public Boolean getResultadoProcessoSeletivoRelFavorito() {
		return resultadoProcessoSeletivoRelFavorito;
	}

	/**
	 * @param resultadoProcessoSeletivoRelFavorito the resultadoProcessoSeletivoRelFavorito to set
	 */
	public void setResultadoProcessoSeletivoRelFavorito(Boolean resultadoProcessoSeletivoRelFavorito) {
		this.resultadoProcessoSeletivoRelFavorito = resultadoProcessoSeletivoRelFavorito;
	}

	/**
	 * @return the comprovanteInscricaoFavorito
	 */
	public Boolean getComprovanteInscricaoFavorito() {
		return comprovanteInscricaoFavorito;
	}

	/**
	 * @param comprovanteInscricaoFavorito the comprovanteInscricaoFavorito to set
	 */
	public void setComprovanteInscricaoFavorito(Boolean comprovanteInscricaoFavorito) {
		this.comprovanteInscricaoFavorito = comprovanteInscricaoFavorito;
	}

	/**
	 * @return the inscricaoCandidatoFavorito
	 */
	public Boolean getInscricaoCandidatoFavorito() {
		return inscricaoCandidatoFavorito;
	}

	/**
	 * @param inscricaoCandidatoFavorito the inscricaoCandidatoFavorito to set
	 */
	public void setInscricaoCandidatoFavorito(Boolean inscricaoCandidatoFavorito) {
		this.inscricaoCandidatoFavorito = inscricaoCandidatoFavorito;
	}

	/**
	 * @return the resultadoCandidatoFavorito
	 */
	public Boolean getResultadoCandidatoFavorito() {
		return resultadoCandidatoFavorito;
	}

	/**
	 * @param resultadoCandidatoFavorito the resultadoCandidatoFavorito to set
	 */
	public void setResultadoCandidatoFavorito(Boolean resultadoCandidatoFavorito) {
		this.resultadoCandidatoFavorito = resultadoCandidatoFavorito;
	}

	/**
	 * @return the calendarioCandidatoFavorito
	 */
	public Boolean getCalendarioCandidatoFavorito() {
		return calendarioCandidatoFavorito;
	}

	/**
	 * @param calendarioCandidatoFavorito the calendarioCandidatoFavorito to set
	 */
	public void setCalendarioCandidatoFavorito(Boolean calendarioCandidatoFavorito) {
		this.calendarioCandidatoFavorito = calendarioCandidatoFavorito;
	}

	/**
	 * @return the boletoCandidatoFavorito
	 */
	public Boolean getBoletoCandidatoFavorito() {
		return boletoCandidatoFavorito;
	}

	/**
	 * @param boletoCandidatoFavorito the boletoCandidatoFavorito to set
	 */
	public void setBoletoCandidatoFavorito(Boolean boletoCandidatoFavorito) {
		this.boletoCandidatoFavorito = boletoCandidatoFavorito;
	}

	/**
	 * @return the cursosCandidatoFavorito
	 */
	public Boolean getCursosCandidatoFavorito() {
		return cursosCandidatoFavorito;
	}

	/**
	 * @param cursosCandidatoFavorito the cursosCandidatoFavorito to set
	 */
	public void setCursosCandidatoFavorito(Boolean cursosCandidatoFavorito) {
		this.cursosCandidatoFavorito = cursosCandidatoFavorito;
	}

	/**
	 * @return the comprovanteInscricaoCandidatoFavorito
	 */
	public Boolean getComprovanteInscricaoCandidatoFavorito() {
		return comprovanteInscricaoCandidatoFavorito;
	}

	/**
	 * @param comprovanteInscricaoCandidatoFavorito the comprovanteInscricaoCandidatoFavorito to set
	 */
	public void setComprovanteInscricaoCandidatoFavorito(Boolean comprovanteInscricaoCandidatoFavorito) {
		this.comprovanteInscricaoCandidatoFavorito = comprovanteInscricaoCandidatoFavorito;
	}

	/**
	 * @return the controleCorrespondenciaFavorito
	 */
	public Boolean getControleCorrespondenciaFavorito() {
		return controleCorrespondenciaFavorito;
	}

	/**
	 * @param controleCorrespondenciaFavorito the controleCorrespondenciaFavorito to set
	 */
	public void setControleCorrespondenciaFavorito(Boolean controleCorrespondenciaFavorito) {
		this.controleCorrespondenciaFavorito = controleCorrespondenciaFavorito;
	}

	/**
	 * @return the requerimentoFavorito
	 */
	public Boolean getRequerimentoFavorito() {
		return requerimentoFavorito;
	}

	/**
	 * @param requerimentoFavorito the requerimentoFavorito to set
	 */
	public void setRequerimentoFavorito(Boolean requerimentoFavorito) {
		this.requerimentoFavorito = requerimentoFavorito;
	}

	/**
	 * @return the tipoRequerimentoFavorito
	 */
	public Boolean getTipoRequerimentoFavorito() {
		return tipoRequerimentoFavorito;
	}

	/**
	 * @param tipoRequerimentoFavorito the tipoRequerimentoFavorito to set
	 */
	public void setTipoRequerimentoFavorito(Boolean tipoRequerimentoFavorito) {
		this.tipoRequerimentoFavorito = tipoRequerimentoFavorito;
	}

	/**
	 * @return the agenciaFavorito
	 */
	public Boolean getAgenciaFavorito() {
		return agenciaFavorito;
	}

	/**
	 * @param agenciaFavorito the agenciaFavorito to set
	 */
	public void setAgenciaFavorito(Boolean agenciaFavorito) {
		this.agenciaFavorito = agenciaFavorito;
	}

	/**
	 * @return the bancoFavorito
	 */
	public Boolean getBancoFavorito() {
		return bancoFavorito;
	}

	/**
	 * @param bancoFavorito the bancoFavorito to set
	 */
	public void setBancoFavorito(Boolean bancoFavorito) {
		this.bancoFavorito = bancoFavorito;
	}

	/**
	 * @return the categoriaDespesaFavorito
	 */
	public Boolean getCategoriaDespesaFavorito() {
		return categoriaDespesaFavorito;
	}

	/**
	 * @param categoriaDespesaFavorito the categoriaDespesaFavorito to set
	 */
	public void setCategoriaDespesaFavorito(Boolean categoriaDespesaFavorito) {
		this.categoriaDespesaFavorito = categoriaDespesaFavorito;
	}

	/**
	 * @return the centroReceitaFavorito
	 */
	public Boolean getCentroReceitaFavorito() {
		return centroReceitaFavorito;
	}

	/**
	 * @param centroReceitaFavorito the centroReceitaFavorito to set
	 */
	public void setCentroReceitaFavorito(Boolean centroReceitaFavorito) {
		this.centroReceitaFavorito = centroReceitaFavorito;
	}

	/**
	 * @return the descontoChancelaFavorito
	 */
	public Boolean getDescontoChancelaFavorito() {
		return descontoChancelaFavorito;
	}

	/**
	 * @param descontoChancelaFavorito the descontoChancelaFavorito to set
	 */
	public void setDescontoChancelaFavorito(Boolean descontoChancelaFavorito) {
		this.descontoChancelaFavorito = descontoChancelaFavorito;
	}

	/**
	 * @return the contaPagarFavorito
	 */
	public Boolean getContaPagarFavorito() {
		return contaPagarFavorito;
	}

	/**
	 * @param contaPagarFavorito the contaPagarFavorito to set
	 */
	public void setContaPagarFavorito(Boolean contaPagarFavorito) {
		this.contaPagarFavorito = contaPagarFavorito;
	}

	/**
	 * @return the grupoContaPagarFavorito
	 */
	public Boolean getGrupoContaPagarFavorito() {
		return grupoContaPagarFavorito;
	}

	/**
	 * @param grupoContaPagarFavorito the grupoContaPagarFavorito to set
	 */
	public void setGrupoContaPagarFavorito(Boolean grupoContaPagarFavorito) {
		this.grupoContaPagarFavorito = grupoContaPagarFavorito;
	}

	/**
	 * @return the contaCorrenteFavorito
	 */
	public Boolean getContaCorrenteFavorito() {
		return contaCorrenteFavorito;
	}

	/**
	 * @param contaCorrenteFavorito the contaCorrenteFavorito to set
	 */
	public void setContaCorrenteFavorito(Boolean contaCorrenteFavorito) {
		this.contaCorrenteFavorito = contaCorrenteFavorito;
	}

	/**
	 * @return the parceiroFavorito
	 */
	public Boolean getParceiroFavorito() {
		return parceiroFavorito;
	}

	/**
	 * @param parceiroFavorito the parceiroFavorito to set
	 */
	public void setParceiroFavorito(Boolean parceiroFavorito) {
		this.parceiroFavorito = parceiroFavorito;
	}

	/**
	 * @return the contratosDespesasFavorito
	 */
	public Boolean getContratosDespesasFavorito() {
		return contratosDespesasFavorito;
	}

	/**
	 * @param contratosDespesasFavorito the contratosDespesasFavorito to set
	 */
	public void setContratosDespesasFavorito(Boolean contratosDespesasFavorito) {
		this.contratosDespesasFavorito = contratosDespesasFavorito;
	}

	/**
	 * @return the contratosReceitasFavorito
	 */
	public Boolean getContratosReceitasFavorito() {
		return contratosReceitasFavorito;
	}

	/**
	 * @param contratosReceitasFavorito the contratosReceitasFavorito to set
	 */
	public void setContratosReceitasFavorito(Boolean contratosReceitasFavorito) {
		this.contratosReceitasFavorito = contratosReceitasFavorito;
	}

	/**
	 * @return the convenioFavorito
	 */
	public Boolean getConvenioFavorito() {
		return convenioFavorito;
	}

	/**
	 * @param convenioFavorito the convenioFavorito to set
	 */
	public void setConvenioFavorito(Boolean convenioFavorito) {
		this.convenioFavorito = convenioFavorito;
	}

	/**
	 * @return the recebimentoFavorito
	 */
	public Boolean getRecebimentoFavorito() {
		return recebimentoFavorito;
	}

	/**
	 * @param recebimentoFavorito the recebimentoFavorito to set
	 */
	public void setRecebimentoFavorito(Boolean recebimentoFavorito) {
		this.recebimentoFavorito = recebimentoFavorito;
	}

	/**
	 * @return the pagamentoFavorito
	 */
	public Boolean getPagamentoFavorito() {
		return pagamentoFavorito;
	}

	/**
	 * @param pagamentoFavorito the pagamentoFavorito to set
	 */
	public void setPagamentoFavorito(Boolean pagamentoFavorito) {
		this.pagamentoFavorito = pagamentoFavorito;
	}

	/**
	 * @return the contaReceberFavorito
	 */
	public Boolean getContaReceberFavorito() {
		return contaReceberFavorito;
	}

	/**
	 * @param contaReceberFavorito the contaReceberFavorito to set
	 */
	public void setContaReceberFavorito(Boolean contaReceberFavorito) {
		this.contaReceberFavorito = contaReceberFavorito;
	}

	/**
	 * @return the consultaLogContaReceberFavorito
	 */
	public Boolean getConsultaLogContaReceberFavorito() {
		return consultaLogContaReceberFavorito;
	}

	/**
	 * @param consultaLogContaReceberFavorito the consultaLogContaReceberFavorito to set
	 */
	public void setConsultaLogContaReceberFavorito(Boolean consultaLogContaReceberFavorito) {
		this.consultaLogContaReceberFavorito = consultaLogContaReceberFavorito;
	}

	/**
	 * @return the negociacaoContaReceberFavorito
	 */
	public Boolean getNegociacaoContaReceberFavorito() {
		return negociacaoContaReceberFavorito;
	}

	/**
	 * @param negociacaoContaReceberFavorito the negociacaoContaReceberFavorito to set
	 */
	public void setNegociacaoContaReceberFavorito(Boolean negociacaoContaReceberFavorito) {
		this.negociacaoContaReceberFavorito = negociacaoContaReceberFavorito;
	}

	/**
	 * @return the condicaoNegociacaoFavorito
	 */
	public Boolean getCondicaoNegociacaoFavorito() {
		return condicaoNegociacaoFavorito;
	}

	/**
	 * @param condicaoNegociacaoFavorito the condicaoNegociacaoFavorito to set
	 */
	public void setCondicaoNegociacaoFavorito(Boolean condicaoNegociacaoFavorito) {
		this.condicaoNegociacaoFavorito = condicaoNegociacaoFavorito;
	}

	/**
	 * @return the perfilEconomicoFavorito
	 */
	public Boolean getPerfilEconomicoFavorito() {
		return perfilEconomicoFavorito;
	}

	/**
	 * @param perfilEconomicoFavorito the perfilEconomicoFavorito to set
	 */
	public void setPerfilEconomicoFavorito(Boolean perfilEconomicoFavorito) {
		this.perfilEconomicoFavorito = perfilEconomicoFavorito;
	}

	/**
	 * @return the negociacaoRecebimentoFavorito
	 */
	public Boolean getNegociacaoRecebimentoFavorito() {
		return negociacaoRecebimentoFavorito;
	}

	/**
	 * @param negociacaoRecebimentoFavorito the negociacaoRecebimentoFavorito to set
	 */
	public void setNegociacaoRecebimentoFavorito(Boolean negociacaoRecebimentoFavorito) {
		this.negociacaoRecebimentoFavorito = negociacaoRecebimentoFavorito;
	}

	/**
	 * @return the fluxoCaixaFavorito
	 */
	public Boolean getFluxoCaixaFavorito() {
		return fluxoCaixaFavorito;
	}

	/**
	 * @param fluxoCaixaFavorito the fluxoCaixaFavorito to set
	 */
	public void setFluxoCaixaFavorito(Boolean fluxoCaixaFavorito) {
		this.fluxoCaixaFavorito = fluxoCaixaFavorito;
	}

	/**
	 * @return the planoOrcamentarioFavorito
	 */
	public Boolean getPlanoOrcamentarioFavorito() {
		return planoOrcamentarioFavorito;
	}

	/**
	 * @param planoOrcamentarioFavorito the planoOrcamentarioFavorito to set
	 */
	public void setPlanoOrcamentarioFavorito(Boolean planoOrcamentarioFavorito) {
		this.planoOrcamentarioFavorito = planoOrcamentarioFavorito;
	}

	/**
	 * @return the contaReceberNaoLocalizadaArquivoRetornoFavorito
	 */
	public Boolean getContaReceberNaoLocalizadaArquivoRetornoFavorito() {
		return contaReceberNaoLocalizadaArquivoRetornoFavorito;
	}

	/**
	 * @param contaReceberNaoLocalizadaArquivoRetornoFavorito the contaReceberNaoLocalizadaArquivoRetornoFavorito to set
	 */
	public void setContaReceberNaoLocalizadaArquivoRetornoFavorito(Boolean contaReceberNaoLocalizadaArquivoRetornoFavorito) {
		this.contaReceberNaoLocalizadaArquivoRetornoFavorito = contaReceberNaoLocalizadaArquivoRetornoFavorito;
	}

	/**
	 * @return the solicitacaoOrcamentoPlanoOrcamentarioFavorito
	 */
	public Boolean getSolicitacaoOrcamentoPlanoOrcamentarioFavorito() {
		return solicitacaoOrcamentoPlanoOrcamentarioFavorito;
	}

	/**
	 * @param solicitacaoOrcamentoPlanoOrcamentarioFavorito the solicitacaoOrcamentoPlanoOrcamentarioFavorito to set
	 */
	public void setSolicitacaoOrcamentoPlanoOrcamentarioFavorito(Boolean solicitacaoOrcamentoPlanoOrcamentarioFavorito) {
		this.solicitacaoOrcamentoPlanoOrcamentarioFavorito = solicitacaoOrcamentoPlanoOrcamentarioFavorito;
	}

	/**
	 * @return the geracaoManualParcelasFavorito
	 */
	public Boolean getGeracaoManualParcelasFavorito() {
		return geracaoManualParcelasFavorito;
	}

	/**
	 * @param geracaoManualParcelasFavorito the geracaoManualParcelasFavorito to set
	 */
	public void setGeracaoManualParcelasFavorito(Boolean geracaoManualParcelasFavorito) {
		this.geracaoManualParcelasFavorito = geracaoManualParcelasFavorito;
	}

	/**
	 * @return the geracaoManualParcelasAlunoFavorito
	 */
	public Boolean getGeracaoManualParcelasAlunoFavorito() {
		return geracaoManualParcelasAlunoFavorito;
	}

	/**
	 * @param geracaoManualParcelasAlunoFavorito the geracaoManualParcelasAlunoFavorito to set
	 */
	public void setGeracaoManualParcelasAlunoFavorito(Boolean geracaoManualParcelasAlunoFavorito) {
		this.geracaoManualParcelasAlunoFavorito = geracaoManualParcelasAlunoFavorito;
	}

	/**
	 * @return the mudancaCarteiraFavorito
	 */
	public Boolean getMudancaCarteiraFavorito() {
		return mudancaCarteiraFavorito;
	}

	/**
	 * @param mudancaCarteiraFavorito the mudancaCarteiraFavorito to set
	 */
	public void setMudancaCarteiraFavorito(Boolean mudancaCarteiraFavorito) {
		this.mudancaCarteiraFavorito = mudancaCarteiraFavorito;
	}

	/**
	 * @return the movimentacaoFinanceiraFavorito
	 */
	public Boolean getMovimentacaoFinanceiraFavorito() {
		return movimentacaoFinanceiraFavorito;
	}

	/**
	 * @param movimentacaoFinanceiraFavorito the movimentacaoFinanceiraFavorito to set
	 */
	public void setMovimentacaoFinanceiraFavorito(Boolean movimentacaoFinanceiraFavorito) {
		this.movimentacaoFinanceiraFavorito = movimentacaoFinanceiraFavorito;
	}

	/**
	 * @return the estornarMovimentacaoFinanceiraFavorito
	 */
	public Boolean getEstornarMovimentacaoFinanceiraFavorito() {
		return estornarMovimentacaoFinanceiraFavorito;
	}

	/**
	 * @param estornarMovimentacaoFinanceiraFavorito the estornarMovimentacaoFinanceiraFavorito to set
	 */
	public void setEstornarMovimentacaoFinanceiraFavorito(Boolean estornarMovimentacaoFinanceiraFavorito) {
		this.estornarMovimentacaoFinanceiraFavorito = estornarMovimentacaoFinanceiraFavorito;
	}

	/**
	 * @return the alterarDataMovimentacaoFinanceiraFavorito
	 */
	public Boolean getAlterarDataMovimentacaoFinanceiraFavorito() {
		return alterarDataMovimentacaoFinanceiraFavorito;
	}

	/**
	 * @param alterarDataMovimentacaoFinanceiraFavorito the alterarDataMovimentacaoFinanceiraFavorito to set
	 */
	public void setAlterarDataMovimentacaoFinanceiraFavorito(Boolean alterarDataMovimentacaoFinanceiraFavorito) {
		this.alterarDataMovimentacaoFinanceiraFavorito = alterarDataMovimentacaoFinanceiraFavorito;
	}

	/**
	 * @return the devolucaoChequeFavorito
	 */
	public Boolean getDevolucaoChequeFavorito() {
		return devolucaoChequeFavorito;
	}

	/**
	 * @param devolucaoChequeFavorito the devolucaoChequeFavorito to set
	 */
	public void setDevolucaoChequeFavorito(Boolean devolucaoChequeFavorito) {
		this.devolucaoChequeFavorito = devolucaoChequeFavorito;
	}

	/**
	 * @return the condicaoRenegociacaoFavorito
	 */
	public Boolean getCondicaoRenegociacaoFavorito() {
		return condicaoRenegociacaoFavorito;
	}

	/**
	 * @param condicaoRenegociacaoFavorito the condicaoRenegociacaoFavorito to set
	 */
	public void setCondicaoRenegociacaoFavorito(Boolean condicaoRenegociacaoFavorito) {
		this.condicaoRenegociacaoFavorito = condicaoRenegociacaoFavorito;
	}

	/**
	 * @return the chequeFavorito
	 */
	public Boolean getChequeFavorito() {
		return chequeFavorito;
	}

	/**
	 * @param chequeFavorito the chequeFavorito to set
	 */
	public void setChequeFavorito(Boolean chequeFavorito) {
		this.chequeFavorito = chequeFavorito;
	}

	/**
	 * @return the mapaLancamentoFuturoFavorito
	 */
	public Boolean getMapaLancamentoFuturoFavorito() {
		return mapaLancamentoFuturoFavorito;
	}

	/**
	 * @param mapaLancamentoFuturoFavorito the mapaLancamentoFuturoFavorito to set
	 */
	public void setMapaLancamentoFuturoFavorito(Boolean mapaLancamentoFuturoFavorito) {
		this.mapaLancamentoFuturoFavorito = mapaLancamentoFuturoFavorito;
	}

	/**
	 * @return the mapaReposicaoFavorito
	 */
	public Boolean getMapaReposicaoFavorito() {
		return mapaReposicaoFavorito;
	}

	/**
	 * @param mapaReposicaoFavorito the mapaReposicaoFavorito to set
	 */
	public void setMapaReposicaoFavorito(Boolean mapaReposicaoFavorito) {
		this.mapaReposicaoFavorito = mapaReposicaoFavorito;
	}

	/**
	 * @return the mapaSuspensaoMatriculaFavorito
	 */
	public Boolean getMapaSuspensaoMatriculaFavorito() {
		return mapaSuspensaoMatriculaFavorito;
	}

	/**
	 * @param mapaSuspensaoMatriculaFavorito the mapaSuspensaoMatriculaFavorito to set
	 */
	public void setMapaSuspensaoMatriculaFavorito(Boolean mapaSuspensaoMatriculaFavorito) {
		this.mapaSuspensaoMatriculaFavorito = mapaSuspensaoMatriculaFavorito;
	}

	/**
	 * @return the matriculaSerasaFavorito
	 */
	public Boolean getMatriculaSerasaFavorito() {
		return matriculaSerasaFavorito;
	}

	/**
	 * @param matriculaSerasaFavorito the matriculaSerasaFavorito to set
	 */
	public void setMatriculaSerasaFavorito(Boolean matriculaSerasaFavorito) {
		this.matriculaSerasaFavorito = matriculaSerasaFavorito;
	}

	/**
	 * @return the matriculaLiberacaoFavorito
	 */
	public Boolean getMatriculaLiberacaoFavorito() {
		return matriculaLiberacaoFavorito;
	}

	/**
	 * @param matriculaLiberacaoFavorito the matriculaLiberacaoFavorito to set
	 */
	public void setMatriculaLiberacaoFavorito(Boolean matriculaLiberacaoFavorito) {
		this.matriculaLiberacaoFavorito = matriculaLiberacaoFavorito;
	}

	/**
	 * @return the provisaoCustoFavorito
	 */
	public Boolean getProvisaoCustoFavorito() {
		return provisaoCustoFavorito;
	}

	/**
	 * @param provisaoCustoFavorito the provisaoCustoFavorito to set
	 */
	public void setProvisaoCustoFavorito(Boolean provisaoCustoFavorito) {
		this.provisaoCustoFavorito = provisaoCustoFavorito;
	}

	/**
	 * @return the contaPagarRelFavorito
	 */
	public Boolean getContaPagarRelFavorito() {
		return contaPagarRelFavorito;
	}

	/**
	 * @param contaPagarRelFavorito the contaPagarRelFavorito to set
	 */
	public void setContaPagarRelFavorito(Boolean contaPagarRelFavorito) {
		this.contaPagarRelFavorito = contaPagarRelFavorito;
	}

	/**
	 * @return the contaPagarPorCategoriaDespesaRelFavorito
	 */
	public Boolean getContaPagarPorCategoriaDespesaRelFavorito() {
		return contaPagarPorCategoriaDespesaRelFavorito;
	}

	/**
	 * @param contaPagarPorCategoriaDespesaRelFavorito the contaPagarPorCategoriaDespesaRelFavorito to set
	 */
	public void setContaPagarPorCategoriaDespesaRelFavorito(Boolean contaPagarPorCategoriaDespesaRelFavorito) {
		this.contaPagarPorCategoriaDespesaRelFavorito = contaPagarPorCategoriaDespesaRelFavorito;
	}

	/**
	 * @return the contaPagarPorTurmaRelFavorito
	 */
	public Boolean getContaPagarPorTurmaRelFavorito() {
		return contaPagarPorTurmaRelFavorito;
	}

	/**
	 * @param contaPagarPorTurmaRelFavorito the contaPagarPorTurmaRelFavorito to set
	 */
	public void setContaPagarPorTurmaRelFavorito(Boolean contaPagarPorTurmaRelFavorito) {
		this.contaPagarPorTurmaRelFavorito = contaPagarPorTurmaRelFavorito;
	}

	/**
	 * @return the modeloBoletoFavorito
	 */
	public Boolean getModeloBoletoFavorito() {
		return modeloBoletoFavorito;
	}

	/**
	 * @param modeloBoletoFavorito the modeloBoletoFavorito to set
	 */
	public void setModeloBoletoFavorito(Boolean modeloBoletoFavorito) {
		this.modeloBoletoFavorito = modeloBoletoFavorito;
	}

	/**
	 * @return the controleCobrancaFavorito
	 */
	public Boolean getControleCobrancaFavorito() {
		return controleCobrancaFavorito;
	}

	/**
	 * @param controleCobrancaFavorito the controleCobrancaFavorito to set
	 */
	public void setControleCobrancaFavorito(Boolean controleCobrancaFavorito) {
		this.controleCobrancaFavorito = controleCobrancaFavorito;
	}

	/**
	 * @return the controleMovimentacaoRemessaFavorito
	 */
	public Boolean getControleMovimentacaoRemessaFavorito() {
		return controleMovimentacaoRemessaFavorito;
	}

	/**
	 * @param controleMovimentacaoRemessaFavorito the controleMovimentacaoRemessaFavorito to set
	 */
	public void setControleMovimentacaoRemessaFavorito(Boolean controleMovimentacaoRemessaFavorito) {
		this.controleMovimentacaoRemessaFavorito = controleMovimentacaoRemessaFavorito;
	}

	/**
	 * @return the cartaCobrancaRelFavorito
	 */
	public Boolean getCartaCobrancaRelFavorito() {
		return cartaCobrancaRelFavorito;
	}

	/**
	 * @param cartaCobrancaRelFavorito the cartaCobrancaRelFavorito to set
	 */
	public void setCartaCobrancaRelFavorito(Boolean cartaCobrancaRelFavorito) {
		this.cartaCobrancaRelFavorito = cartaCobrancaRelFavorito;
	}

	/**
	 * @return the liberacaoFinanceiroCancelamentoTrancamentoFavorito
	 */
	public Boolean getLiberacaoFinanceiroCancelamentoTrancamentoFavorito() {
		return liberacaoFinanceiroCancelamentoTrancamentoFavorito;
	}

	/**
	 * @param liberacaoFinanceiroCancelamentoTrancamentoFavorito the liberacaoFinanceiroCancelamentoTrancamentoFavorito to set
	 */
	public void setLiberacaoFinanceiroCancelamentoTrancamentoFavorito(Boolean liberacaoFinanceiroCancelamentoTrancamentoFavorito) {
		this.liberacaoFinanceiroCancelamentoTrancamentoFavorito = liberacaoFinanceiroCancelamentoTrancamentoFavorito;
	}

	/**
	 * @return the serasaFavorito
	 */
	public Boolean getSerasaFavorito() {
		return serasaFavorito;
	}

	/**
	 * @param serasaFavorito the serasaFavorito to set
	 */
	public void setSerasaFavorito(Boolean serasaFavorito) {
		this.serasaFavorito = serasaFavorito;
	}

	/**
	 * @return the atualizacaoVencimentosFavorito
	 */
	public Boolean getAtualizacaoVencimentosFavorito() {
		return atualizacaoVencimentosFavorito;
	}

	/**
	 * @param atualizacaoVencimentosFavorito the atualizacaoVencimentosFavorito to set
	 */
	public void setAtualizacaoVencimentosFavorito(Boolean atualizacaoVencimentosFavorito) {
		this.atualizacaoVencimentosFavorito = atualizacaoVencimentosFavorito;
	}

	/**
	 * @return the mapaPendenciasControleCobrancaFavorito
	 */
	public Boolean getMapaPendenciasControleCobrancaFavorito() {
		return mapaPendenciasControleCobrancaFavorito;
	}

	/**
	 * @param mapaPendenciasControleCobrancaFavorito the mapaPendenciasControleCobrancaFavorito to set
	 */
	public void setMapaPendenciasControleCobrancaFavorito(Boolean mapaPendenciasControleCobrancaFavorito) {
		this.mapaPendenciasControleCobrancaFavorito = mapaPendenciasControleCobrancaFavorito;
	}

	/**
	 * @return the alterarDescontoContaReceberFavorito
	 */
	public Boolean getAlterarDescontoContaReceberFavorito() {
		return alterarDescontoContaReceberFavorito;
	}

	/**
	 * @param alterarDescontoContaReceberFavorito the alterarDescontoContaReceberFavorito to set
	 */
	public void setAlterarDescontoContaReceberFavorito(Boolean alterarDescontoContaReceberFavorito) {
		this.alterarDescontoContaReceberFavorito = alterarDescontoContaReceberFavorito;
	}

	/**
	 * @return the operadoraCartaoFavorito
	 */
	public Boolean getOperadoraCartaoFavorito() {
		return operadoraCartaoFavorito;
	}

	/**
	 * @param operadoraCartaoFavorito the operadoraCartaoFavorito to set
	 */
	public void setOperadoraCartaoFavorito(Boolean operadoraCartaoFavorito) {
		this.operadoraCartaoFavorito = operadoraCartaoFavorito;
	}

	/**
	 * @return the mapaPendenciaCartaoCreditoFavorito
	 */
	public Boolean getMapaPendenciaCartaoCreditoFavorito() {
		return mapaPendenciaCartaoCreditoFavorito;
	}

	/**
	 * @param mapaPendenciaCartaoCreditoFavorito the mapaPendenciaCartaoCreditoFavorito to set
	 */
	public void setMapaPendenciaCartaoCreditoFavorito(Boolean mapaPendenciaCartaoCreditoFavorito) {
		this.mapaPendenciaCartaoCreditoFavorito = mapaPendenciaCartaoCreditoFavorito;
	}

	/**
	 * @return the cartaoRespostaRelFavorito
	 */
	public Boolean getCartaoRespostaRelFavorito() {
		return cartaoRespostaRelFavorito;
	}

	/**
	 * @param cartaoRespostaRelFavorito the cartaoRespostaRelFavorito to set
	 */
	public void setCartaoRespostaRelFavorito(Boolean cartaoRespostaRelFavorito) {
		this.cartaoRespostaRelFavorito = cartaoRespostaRelFavorito;
	}

	/**
	 * @return the tipoDescontoRelFavorito
	 */
	public Boolean getTipoDescontoRelFavorito() {
		return tipoDescontoRelFavorito;
	}

	/**
	 * @param tipoDescontoRelFavorito the tipoDescontoRelFavorito to set
	 */
	public void setTipoDescontoRelFavorito(Boolean tipoDescontoRelFavorito) {
		this.tipoDescontoRelFavorito = tipoDescontoRelFavorito;
	}

	/**
	 * @return the mapaPendenciaMovimentacaoFinanceiraFavorito
	 */
	public Boolean getMapaPendenciaMovimentacaoFinanceiraFavorito() {
		return mapaPendenciaMovimentacaoFinanceiraFavorito;
	}

	/**
	 * @param mapaPendenciaMovimentacaoFinanceiraFavorito the mapaPendenciaMovimentacaoFinanceiraFavorito to set
	 */
	public void setMapaPendenciaMovimentacaoFinanceiraFavorito(Boolean mapaPendenciaMovimentacaoFinanceiraFavorito) {
		this.mapaPendenciaMovimentacaoFinanceiraFavorito = mapaPendenciaMovimentacaoFinanceiraFavorito;
	}

	/**
	 * @return the controleRemessaFavorito
	 */
	public Boolean getControleRemessaFavorito() {
		return controleRemessaFavorito;
	}

	/**
	 * @param controleRemessaFavorito the controleRemessaFavorito to set
	 */
	public void setControleRemessaFavorito(Boolean controleRemessaFavorito) {
		this.controleRemessaFavorito = controleRemessaFavorito;
	}

	public Boolean getProcessamentoArquivoRetornoParceiro() {
		return processamentoArquivoRetornoParceiro;
	}

	public void setProcessamentoArquivoRetornoParceiro(Boolean processamentoArquivoRetornoParceiro) {
		this.processamentoArquivoRetornoParceiro = processamentoArquivoRetornoParceiro;
	}

	public Boolean getProcessamentoArquivoRetornoParceiroFavorito() {
		return processamentoArquivoRetornoParceiroFavorito;
	}

	public void setProcessamentoArquivoRetornoParceiroFavorito(Boolean processamentoArquivoRetornoParceiroFavorito) {
		this.processamentoArquivoRetornoParceiroFavorito = processamentoArquivoRetornoParceiroFavorito;
	}

	/**
	 * @return the chancelaFavorito
	 */
	public Boolean getChancelaFavorito() {
		return chancelaFavorito;
	}

	/**
	 * @param chancelaFavorito the chancelaFavorito to set
	 */
	public void setChancelaFavorito(Boolean chancelaFavorito) {
		this.chancelaFavorito = chancelaFavorito;
	}

	/**
	 * @return the descontoInclusaoReposicaoForaPrazoFavorito
	 */
	public Boolean getDescontoInclusaoReposicaoForaPrazoFavorito() {
		return descontoInclusaoReposicaoForaPrazoFavorito;
	}

	/**
	 * @param descontoInclusaoReposicaoForaPrazoFavorito the descontoInclusaoReposicaoForaPrazoFavorito to set
	 */
	public void setDescontoInclusaoReposicaoForaPrazoFavorito(Boolean descontoInclusaoReposicaoForaPrazoFavorito) {
		this.descontoInclusaoReposicaoForaPrazoFavorito = descontoInclusaoReposicaoForaPrazoFavorito;
	}

	/**
	 * @return the membroComunidadeFavorito
	 */
	public Boolean getMembroComunidadeFavorito() {
		return membroComunidadeFavorito;
	}

	/**
	 * @param membroComunidadeFavorito the membroComunidadeFavorito to set
	 */
	public void setMembroComunidadeFavorito(Boolean membroComunidadeFavorito) {
		this.membroComunidadeFavorito = membroComunidadeFavorito;
	}

	/**
	 * @return the trabalhoSubmetidoFavorito
	 */
	public Boolean getTrabalhoSubmetidoFavorito() {
		return trabalhoSubmetidoFavorito;
	}

	/**
	 * @param trabalhoSubmetidoFavorito the trabalhoSubmetidoFavorito to set
	 */
	public void setTrabalhoSubmetidoFavorito(Boolean trabalhoSubmetidoFavorito) {
		this.trabalhoSubmetidoFavorito = trabalhoSubmetidoFavorito;
	}

	/**
	 * @return the categoriaProdutoFavorito
	 */
	public Boolean getCategoriaProdutoFavorito() {
		return categoriaProdutoFavorito;
	}

	/**
	 * @param categoriaProdutoFavorito the categoriaProdutoFavorito to set
	 */
	public void setCategoriaProdutoFavorito(Boolean categoriaProdutoFavorito) {
		this.categoriaProdutoFavorito = categoriaProdutoFavorito;
	}

	/**
	 * @return the grupoDestinatariosFavorito
	 */
	public Boolean getGrupoDestinatariosFavorito() {
		return grupoDestinatariosFavorito;
	}

	/**
	 * @param grupoDestinatariosFavorito the grupoDestinatariosFavorito to set
	 */
	public void setGrupoDestinatariosFavorito(Boolean grupoDestinatariosFavorito) {
		this.grupoDestinatariosFavorito = grupoDestinatariosFavorito;
	}

	/**
	 * @return the classificaoCustosFavorito
	 */
	public Boolean getClassificaoCustosFavorito() {
		return classificaoCustosFavorito;
	}

	/**
	 * @param classificaoCustosFavorito the classificaoCustosFavorito to set
	 */
	public void setClassificaoCustosFavorito(Boolean classificaoCustosFavorito) {
		this.classificaoCustosFavorito = classificaoCustosFavorito;
	}

	/**
	 * @return the compraFavorito
	 */
	public Boolean getCompraFavorito() {
		return compraFavorito;
	}

	/**
	 * @param compraFavorito the compraFavorito to set
	 */
	public void setCompraFavorito(Boolean compraFavorito) {
		this.compraFavorito = compraFavorito;
	}

	/**
	 * @return the cotacaoFavorito
	 */
	public Boolean getCotacaoFavorito() {
		return cotacaoFavorito;
	}

	/**
	 * @param cotacaoFavorito the cotacaoFavorito to set
	 */
	public void setCotacaoFavorito(Boolean cotacaoFavorito) {
		this.cotacaoFavorito = cotacaoFavorito;
	}

	/**
	 * @return the mapaCotacaoFavorito
	 */
	public Boolean getMapaCotacaoFavorito() {
		return mapaCotacaoFavorito;
	}

	/**
	 * @param mapaCotacaoFavorito the mapaCotacaoFavorito to set
	 */
	public void setMapaCotacaoFavorito(Boolean mapaCotacaoFavorito) {
		this.mapaCotacaoFavorito = mapaCotacaoFavorito;
	}

	/**
	 * @return the estoqueFavorito
	 */
	public Boolean getEstoqueFavorito() {
		return estoqueFavorito;
	}

	/**
	 * @param estoqueFavorito the estoqueFavorito to set
	 */
	public void setEstoqueFavorito(Boolean estoqueFavorito) {
		this.estoqueFavorito = estoqueFavorito;
	}

	/**
	 * @return the movimentacaoEstoqueFavorito
	 */
	public Boolean getMovimentacaoEstoqueFavorito() {
		return movimentacaoEstoqueFavorito;
	}

	/**
	 * @param movimentacaoEstoqueFavorito the movimentacaoEstoqueFavorito to set
	 */
	public void setMovimentacaoEstoqueFavorito(Boolean movimentacaoEstoqueFavorito) {
		this.movimentacaoEstoqueFavorito = movimentacaoEstoqueFavorito;
	}

	/**
	 * @return the formaPagamentoFavorito
	 */
	public Boolean getFormaPagamentoFavorito() {
		return formaPagamentoFavorito;
	}

	/**
	 * @param formaPagamentoFavorito the formaPagamentoFavorito to set
	 */
	public void setFormaPagamentoFavorito(Boolean formaPagamentoFavorito) {
		this.formaPagamentoFavorito = formaPagamentoFavorito;
	}

	/**
	 * @return the pgtoServicoAcademicoFavorito
	 */
	public Boolean getPgtoServicoAcademicoFavorito() {
		return pgtoServicoAcademicoFavorito;
	}

	/**
	 * @param pgtoServicoAcademicoFavorito the pgtoServicoAcademicoFavorito to set
	 */
	public void setPgtoServicoAcademicoFavorito(Boolean pgtoServicoAcademicoFavorito) {
		this.pgtoServicoAcademicoFavorito = pgtoServicoAcademicoFavorito;
	}

	/**
	 * @return the previsaoCustosFavorito
	 */
	public Boolean getPrevisaoCustosFavorito() {
		return previsaoCustosFavorito;
	}

	/**
	 * @param previsaoCustosFavorito the previsaoCustosFavorito to set
	 */
	public void setPrevisaoCustosFavorito(Boolean previsaoCustosFavorito) {
		this.previsaoCustosFavorito = previsaoCustosFavorito;
	}

	/**
	 * @return the produtoServicoFavorito
	 */
	public Boolean getProdutoServicoFavorito() {
		return produtoServicoFavorito;
	}

	/**
	 * @param produtoServicoFavorito the produtoServicoFavorito to set
	 */
	public void setProdutoServicoFavorito(Boolean produtoServicoFavorito) {
		this.produtoServicoFavorito = produtoServicoFavorito;
	}

	/**
	 * @return the solicitacaoPgtoServicoAcademicoFavorito
	 */
	public Boolean getSolicitacaoPgtoServicoAcademicoFavorito() {
		return solicitacaoPgtoServicoAcademicoFavorito;
	}

	/**
	 * @param solicitacaoPgtoServicoAcademicoFavorito the solicitacaoPgtoServicoAcademicoFavorito to set
	 */
	public void setSolicitacaoPgtoServicoAcademicoFavorito(Boolean solicitacaoPgtoServicoAcademicoFavorito) {
		this.solicitacaoPgtoServicoAcademicoFavorito = solicitacaoPgtoServicoAcademicoFavorito;
	}

	/**
	 * @return the fornecedorFavorito
	 */
	public Boolean getFornecedorFavorito() {
		return fornecedorFavorito;
	}

	/**
	 * @param fornecedorFavorito the fornecedorFavorito to set
	 */
	public void setFornecedorFavorito(Boolean fornecedorFavorito) {
		this.fornecedorFavorito = fornecedorFavorito;
	}

	/**
	 * @return the requisicaoFavorito
	 */
	public Boolean getRequisicaoFavorito() {
		return requisicaoFavorito;
	}

	/**
	 * @param requisicaoFavorito the requisicaoFavorito to set
	 */
	public void setRequisicaoFavorito(Boolean requisicaoFavorito) {
		this.requisicaoFavorito = requisicaoFavorito;
	}

	/**
	 * @return the mapaRequisicaoFavorito
	 */
	public Boolean getMapaRequisicaoFavorito() {
		return mapaRequisicaoFavorito;
	}

	/**
	 * @param mapaRequisicaoFavorito the mapaRequisicaoFavorito to set
	 */
	public void setMapaRequisicaoFavorito(Boolean mapaRequisicaoFavorito) {
		this.mapaRequisicaoFavorito = mapaRequisicaoFavorito;
	}

	/**
	 * @return the recebimentoCompraFavorito
	 */
	public Boolean getRecebimentoCompraFavorito() {
		return recebimentoCompraFavorito;
	}

	/**
	 * @param recebimentoCompraFavorito the recebimentoCompraFavorito to set
	 */
	public void setRecebimentoCompraFavorito(Boolean recebimentoCompraFavorito) {
		this.recebimentoCompraFavorito = recebimentoCompraFavorito;
	}

	/**
	 * @return the devolucaoCompraFavorito
	 */
	public Boolean getDevolucaoCompraFavorito() {
		return devolucaoCompraFavorito;
	}

	/**
	 * @param devolucaoCompraFavorito the devolucaoCompraFavorito to set
	 */
	public void setDevolucaoCompraFavorito(Boolean devolucaoCompraFavorito) {
		this.devolucaoCompraFavorito = devolucaoCompraFavorito;
	}

	/**
	 * @return the entregaRequisicaoFavorito
	 */
	public Boolean getEntregaRequisicaoFavorito() {
		return entregaRequisicaoFavorito;
	}

	/**
	 * @param entregaRequisicaoFavorito the entregaRequisicaoFavorito to set
	 */
	public void setEntregaRequisicaoFavorito(Boolean entregaRequisicaoFavorito) {
		this.entregaRequisicaoFavorito = entregaRequisicaoFavorito;
	}

	/**
	 * @return the condicaoPagamentoFavorito
	 */
	public Boolean getCondicaoPagamentoFavorito() {
		return condicaoPagamentoFavorito;
	}

	/**
	 * @param condicaoPagamentoFavorito the condicaoPagamentoFavorito to set
	 */
	public void setCondicaoPagamentoFavorito(Boolean condicaoPagamentoFavorito) {
		this.condicaoPagamentoFavorito = condicaoPagamentoFavorito;
	}

	/**
	 * @return the classificacaoBibliograficaFavorito
	 */
	public Boolean getClassificacaoBibliograficaFavorito() {
		return classificacaoBibliograficaFavorito;
	}

	/**
	 * @param classificacaoBibliograficaFavorito the classificacaoBibliograficaFavorito to set
	 */
	public void setClassificacaoBibliograficaFavorito(Boolean classificacaoBibliograficaFavorito) {
		this.classificacaoBibliograficaFavorito = classificacaoBibliograficaFavorito;
	}

	/**
	 * @return the exemplarFavorito
	 */
	public Boolean getExemplarFavorito() {
		return exemplarFavorito;
	}

	/**
	 * @param exemplarFavorito the exemplarFavorito to set
	 */
	public void setExemplarFavorito(Boolean exemplarFavorito) {
		this.exemplarFavorito = exemplarFavorito;
	}

	/**
	 * @return the publicacaoFavorito
	 */
	public Boolean getPublicacaoFavorito() {
		return publicacaoFavorito;
	}

	/**
	 * @param publicacaoFavorito the publicacaoFavorito to set
	 */
	public void setPublicacaoFavorito(Boolean publicacaoFavorito) {
		this.publicacaoFavorito = publicacaoFavorito;
	}

	/**
	 * @return the editoraFavorito
	 */
	public Boolean getEditoraFavorito() {
		return editoraFavorito;
	}

	/**
	 * @param editoraFavorito the editoraFavorito to set
	 */
	public void setEditoraFavorito(Boolean editoraFavorito) {
		this.editoraFavorito = editoraFavorito;
	}

	/**
	 * @return the bibliotecaFavorito
	 */
	public Boolean getBibliotecaFavorito() {
		return bibliotecaFavorito;
	}

	/**
	 * @param bibliotecaFavorito the bibliotecaFavorito to set
	 */
	public void setBibliotecaFavorito(Boolean bibliotecaFavorito) {
		this.bibliotecaFavorito = bibliotecaFavorito;
	}

	/**
	 * @return the recursosAudioVisuaisFavorito
	 */
	public Boolean getRecursosAudioVisuaisFavorito() {
		return recursosAudioVisuaisFavorito;
	}

	/**
	 * @param recursosAudioVisuaisFavorito the recursosAudioVisuaisFavorito to set
	 */
	public void setRecursosAudioVisuaisFavorito(Boolean recursosAudioVisuaisFavorito) {
		this.recursosAudioVisuaisFavorito = recursosAudioVisuaisFavorito;
	}

	/**
	 * @return the registroSaidaAcervoFavorito
	 */
	public Boolean getRegistroSaidaAcervoFavorito() {
		return registroSaidaAcervoFavorito;
	}

	/**
	 * @param registroSaidaAcervoFavorito the registroSaidaAcervoFavorito to set
	 */
	public void setRegistroSaidaAcervoFavorito(Boolean registroSaidaAcervoFavorito) {
		this.registroSaidaAcervoFavorito = registroSaidaAcervoFavorito;
	}

	/**
	 * @return the autorFavorito
	 */
	public Boolean getAutorFavorito() {
		return autorFavorito;
	}

	/**
	 * @param autorFavorito the autorFavorito to set
	 */
	public void setAutorFavorito(Boolean autorFavorito) {
		this.autorFavorito = autorFavorito;
	}

	/**
	 * @return the itemPublicacaoAutorFavorito
	 */
	public Boolean getItemPublicacaoAutorFavorito() {
		return itemPublicacaoAutorFavorito;
	}

	/**
	 * @param itemPublicacaoAutorFavorito the itemPublicacaoAutorFavorito to set
	 */
	public void setItemPublicacaoAutorFavorito(Boolean itemPublicacaoAutorFavorito) {
		this.itemPublicacaoAutorFavorito = itemPublicacaoAutorFavorito;
	}

	/**
	 * @return the emprestimoFavorito
	 */
	public Boolean getEmprestimoFavorito() {
		return emprestimoFavorito;
	}

	/**
	 * @param emprestimoFavorito the emprestimoFavorito to set
	 */
	public void setEmprestimoFavorito(Boolean emprestimoFavorito) {
		this.emprestimoFavorito = emprestimoFavorito;
	}

	/**
	 * @return the catalogoFavorito
	 */
	public Boolean getCatalogoFavorito() {
		return catalogoFavorito;
	}

	/**
	 * @param catalogoFavorito the catalogoFavorito to set
	 */
	public void setCatalogoFavorito(Boolean catalogoFavorito) {
		this.catalogoFavorito = catalogoFavorito;
	}

	/**
	 * @return the tituloFavorito
	 */
	public Boolean getTituloFavorito() {
		return tituloFavorito;
	}

	/**
	 * @param tituloFavorito the tituloFavorito to set
	 */
	public void setTituloFavorito(Boolean tituloFavorito) {
		this.tituloFavorito = tituloFavorito;
	}

	/**
	 * @return the registroEntradaAcervoFavorito
	 */
	public Boolean getRegistroEntradaAcervoFavorito() {
		return registroEntradaAcervoFavorito;
	}

	/**
	 * @param registroEntradaAcervoFavorito the registroEntradaAcervoFavorito to set
	 */
	public void setRegistroEntradaAcervoFavorito(Boolean registroEntradaAcervoFavorito) {
		this.registroEntradaAcervoFavorito = registroEntradaAcervoFavorito;
	}

	/**
	 * @return the secaoFavorito
	 */
	public Boolean getSecaoFavorito() {
		return secaoFavorito;
	}

	/**
	 * @param secaoFavorito the secaoFavorito to set
	 */
	public void setSecaoFavorito(Boolean secaoFavorito) {
		this.secaoFavorito = secaoFavorito;
	}

	/**
	 * @return the assinaturaPeriodicoFavorito
	 */
	public Boolean getAssinaturaPeriodicoFavorito() {
		return assinaturaPeriodicoFavorito;
	}

	/**
	 * @param assinaturaPeriodicoFavorito the assinaturaPeriodicoFavorito to set
	 */
	public void setAssinaturaPeriodicoFavorito(Boolean assinaturaPeriodicoFavorito) {
		this.assinaturaPeriodicoFavorito = assinaturaPeriodicoFavorito;
	}

	/**
	 * @return the configuracaoBibliotecaFavorito
	 */
	public Boolean getConfiguracaoBibliotecaFavorito() {
		return configuracaoBibliotecaFavorito;
	}

	/**
	 * @param configuracaoBibliotecaFavorito the configuracaoBibliotecaFavorito to set
	 */
	public void setConfiguracaoBibliotecaFavorito(Boolean configuracaoBibliotecaFavorito) {
		this.configuracaoBibliotecaFavorito = configuracaoBibliotecaFavorito;
	}

	/**
	 * @return the tipoAutoriaFavorito
	 */
	public Boolean getTipoAutoriaFavorito() {
		return tipoAutoriaFavorito;
	}

	/**
	 * @param tipoAutoriaFavorito the tipoAutoriaFavorito to set
	 */
	public void setTipoAutoriaFavorito(Boolean tipoAutoriaFavorito) {
		this.tipoAutoriaFavorito = tipoAutoriaFavorito;
	}

	/**
	 * @return the tipoCatalogoFavorito
	 */
	public Boolean getTipoCatalogoFavorito() {
		return tipoCatalogoFavorito;
	}

	/**
	 * @param tipoCatalogoFavorito the tipoCatalogoFavorito to set
	 */
	public void setTipoCatalogoFavorito(Boolean tipoCatalogoFavorito) {
		this.tipoCatalogoFavorito = tipoCatalogoFavorito;
	}

	/**
	 * @return the bloqueioBibliotecaFavorito
	 */
	public Boolean getBloqueioBibliotecaFavorito() {
		return bloqueioBibliotecaFavorito;
	}

	/**
	 * @param bloqueioBibliotecaFavorito the bloqueioBibliotecaFavorito to set
	 */
	public void setBloqueioBibliotecaFavorito(Boolean bloqueioBibliotecaFavorito) {
		this.bloqueioBibliotecaFavorito = bloqueioBibliotecaFavorito;
	}

	/**
	 * @return the buscaCatalogoFavorito
	 */
	public Boolean getBuscaCatalogoFavorito() {
		return buscaCatalogoFavorito;
	}

	/**
	 * @param buscaCatalogoFavorito the buscaCatalogoFavorito to set
	 */
	public void setBuscaCatalogoFavorito(Boolean buscaCatalogoFavorito) {
		this.buscaCatalogoFavorito = buscaCatalogoFavorito;
	}

	/**
	 * @return the acervoRelFavorito
	 */
	public Boolean getAcervoRelFavorito() {
		return acervoRelFavorito;
	}

	/**
	 * @param acervoRelFavorito the acervoRelFavorito to set
	 */
	public void setAcervoRelFavorito(Boolean acervoRelFavorito) {
		this.acervoRelFavorito = acervoRelFavorito;
	}

	/**
	 * @return the etiquetaCodigoBarraFavorito
	 */
	public Boolean getEtiquetaCodigoBarraFavorito() {
		return etiquetaCodigoBarraFavorito;
	}

	/**
	 * @param etiquetaCodigoBarraFavorito the etiquetaCodigoBarraFavorito to set
	 */
	public void setEtiquetaCodigoBarraFavorito(Boolean etiquetaCodigoBarraFavorito) {
		this.etiquetaCodigoBarraFavorito = etiquetaCodigoBarraFavorito;
	}

	/**
	 * @return the etiquetaLivroRelFavorito
	 */
	public Boolean getEtiquetaLivroRelFavorito() {
		return etiquetaLivroRelFavorito;
	}

	/**
	 * @param etiquetaLivroRelFavorito the etiquetaLivroRelFavorito to set
	 */
	public void setEtiquetaLivroRelFavorito(Boolean etiquetaLivroRelFavorito) {
		this.etiquetaLivroRelFavorito = etiquetaLivroRelFavorito;
	}

	/**
	 * @return the exemplaresPorCursoRelFavorito
	 */
	public Boolean getExemplaresPorCursoRelFavorito() {
		return exemplaresPorCursoRelFavorito;
	}

	/**
	 * @param exemplaresPorCursoRelFavorito the exemplaresPorCursoRelFavorito to set
	 */
	public void setExemplaresPorCursoRelFavorito(Boolean exemplaresPorCursoRelFavorito) {
		this.exemplaresPorCursoRelFavorito = exemplaresPorCursoRelFavorito;
	}

	/**
	 * @return the permiteIsentarMultaBibliotecaFavorito
	 */
	public Boolean getPermiteIsentarMultaBibliotecaFavorito() {
		return permiteIsentarMultaBibliotecaFavorito;
	}

	/**
	 * @param permiteIsentarMultaBibliotecaFavorito the permiteIsentarMultaBibliotecaFavorito to set
	 */
	public void setPermiteIsentarMultaBibliotecaFavorito(Boolean permiteIsentarMultaBibliotecaFavorito) {
		this.permiteIsentarMultaBibliotecaFavorito = permiteIsentarMultaBibliotecaFavorito;
	}

	/**
	 * @return the artefatoAjudaFavorito
	 */
	public Boolean getArtefatoAjudaFavorito() {
		return artefatoAjudaFavorito;
	}

	/**
	 * @param artefatoAjudaFavorito the artefatoAjudaFavorito to set
	 */
	public void setArtefatoAjudaFavorito(Boolean artefatoAjudaFavorito) {
		this.artefatoAjudaFavorito = artefatoAjudaFavorito;
	}

	/**
	 * @return the bairroFavorito
	 */
	public Boolean getBairroFavorito() {
		return bairroFavorito;
	}

	/**
	 * @param bairroFavorito the bairroFavorito to set
	 */
	public void setBairroFavorito(Boolean bairroFavorito) {
		this.bairroFavorito = bairroFavorito;
	}

	/**
	 * @return the enderecoFavorito
	 */
	public Boolean getEnderecoFavorito() {
		return enderecoFavorito;
	}

	/**
	 * @param enderecoFavorito the enderecoFavorito to set
	 */
	public void setEnderecoFavorito(Boolean enderecoFavorito) {
		this.enderecoFavorito = enderecoFavorito;
	}

	/**
	 * @return the cidadeFavorito
	 */
	public Boolean getCidadeFavorito() {
		return cidadeFavorito;
	}

	/**
	 * @param cidadeFavorito the cidadeFavorito to set
	 */
	public void setCidadeFavorito(Boolean cidadeFavorito) {
		this.cidadeFavorito = cidadeFavorito;
	}

	/**
	 * @return the estadoFavorito
	 */
	public Boolean getEstadoFavorito() {
		return estadoFavorito;
	}

	/**
	 * @param estadoFavorito the estadoFavorito to set
	 */
	public void setEstadoFavorito(Boolean estadoFavorito) {
		this.estadoFavorito = estadoFavorito;
	}

	/**
	 * @return the paisFavorito
	 */
	public Boolean getPaisFavorito() {
		return paisFavorito;
	}

	/**
	 * @param paisFavorito the paisFavorito to set
	 */
	public void setPaisFavorito(Boolean paisFavorito) {
		this.paisFavorito = paisFavorito;
	}

	/**
	 * @return the configuracoesFavorito
	 */
	public Boolean getConfiguracoesFavorito() {
		return configuracoesFavorito;
	}

	/**
	 * @param configuracoesFavorito the configuracoesFavorito to set
	 */
	public void setConfiguracoesFavorito(Boolean configuracoesFavorito) {
		this.configuracoesFavorito = configuracoesFavorito;
	}

	/**
	 * @return the feriadoFavorito
	 */
	public Boolean getFeriadoFavorito() {
		return feriadoFavorito;
	}

	/**
	 * @param feriadoFavorito the feriadoFavorito to set
	 */
	public void setFeriadoFavorito(Boolean feriadoFavorito) {
		this.feriadoFavorito = feriadoFavorito;
	}

	/**
	 * @return the alunosAtivosRelFavorito
	 */
	public Boolean getAlunosAtivosRelFavorito() {
		return alunosAtivosRelFavorito;
	}

	/**
	 * @param alunosAtivosRelFavorito the alunosAtivosRelFavorito to set
	 */
	public void setAlunosAtivosRelFavorito(Boolean alunosAtivosRelFavorito) {
		this.alunosAtivosRelFavorito = alunosAtivosRelFavorito;
	}

	/**
	 * @return the dataComemorativaFavorito
	 */
	public Boolean getDataComemorativaFavorito() {
		return dataComemorativaFavorito;
	}

	/**
	 * @param dataComemorativaFavorito the dataComemorativaFavorito to set
	 */
	public void setDataComemorativaFavorito(Boolean dataComemorativaFavorito) {
		this.dataComemorativaFavorito = dataComemorativaFavorito;
	}

	/**
	 * @return the configuracaoTCCFavorito
	 */
	public Boolean getConfiguracaoTCCFavorito() {
		return configuracaoTCCFavorito;
	}

	/**
	 * @param configuracaoTCCFavorito the configuracaoTCCFavorito to set
	 */
	public void setConfiguracaoTCCFavorito(Boolean configuracaoTCCFavorito) {
		this.configuracaoTCCFavorito = configuracaoTCCFavorito;
	}

	/**
	 * @return the perfilAcessoFavorito
	 */
	public Boolean getPerfilAcessoFavorito() {
		return perfilAcessoFavorito;
	}

	/**
	 * @param perfilAcessoFavorito the perfilAcessoFavorito to set
	 */
	public void setPerfilAcessoFavorito(Boolean perfilAcessoFavorito) {
		this.perfilAcessoFavorito = perfilAcessoFavorito;
	}

	/**
	 * @return the usuarioFavorito
	 */
	public Boolean getUsuarioFavorito() {
		return usuarioFavorito;
	}

	/**
	 * @param usuarioFavorito the usuarioFavorito to set
	 */
	public void setUsuarioFavorito(Boolean usuarioFavorito) {
		this.usuarioFavorito = usuarioFavorito;
	}

	/**
	 * @return the logFavorito
	 */
	public Boolean getLogFavorito() {
		return logFavorito;
	}

	/**
	 * @param logFavorito the logFavorito to set
	 */
	public void setLogFavorito(Boolean logFavorito) {
		this.logFavorito = logFavorito;
	}

	/**
	 * @return the mapaControleAtividadesUsuariosFavorito
	 */
	public Boolean getMapaControleAtividadesUsuariosFavorito() {
		return mapaControleAtividadesUsuariosFavorito;
	}

	/**
	 * @param mapaControleAtividadesUsuariosFavorito the mapaControleAtividadesUsuariosFavorito to set
	 */
	public void setMapaControleAtividadesUsuariosFavorito(Boolean mapaControleAtividadesUsuariosFavorito) {
		this.mapaControleAtividadesUsuariosFavorito = mapaControleAtividadesUsuariosFavorito;
	}

	/**
	 * @return the ouvidoriaFavorito
	 */
	public Boolean getOuvidoriaFavorito() {
		return ouvidoriaFavorito;
	}

	/**
	 * @param ouvidoriaFavorito the ouvidoriaFavorito to set
	 */
	public void setOuvidoriaFavorito(Boolean ouvidoriaFavorito) {
		this.ouvidoriaFavorito = ouvidoriaFavorito;
	}

	/**
	 * @return the tipagemOuvidoriaFavorito
	 */
	public Boolean getTipagemOuvidoriaFavorito() {
		return tipagemOuvidoriaFavorito;
	}

	/**
	 * @param tipagemOuvidoriaFavorito the tipagemOuvidoriaFavorito to set
	 */
	public void setTipagemOuvidoriaFavorito(Boolean tipagemOuvidoriaFavorito) {
		this.tipagemOuvidoriaFavorito = tipagemOuvidoriaFavorito;
	}

	/**
	 * @return the configuracaoAtendimentoFavorito
	 */
	public Boolean getConfiguracaoAtendimentoFavorito() {
		return configuracaoAtendimentoFavorito;
	}

	/**
	 * @param configuracaoAtendimentoFavorito the configuracaoAtendimentoFavorito to set
	 */
	public void setConfiguracaoAtendimentoFavorito(Boolean configuracaoAtendimentoFavorito) {
		this.configuracaoAtendimentoFavorito = configuracaoAtendimentoFavorito;
	}

	/**
	 * @return the unidadeEnsinoFavorito
	 */
	public Boolean getUnidadeEnsinoFavorito() {
		return unidadeEnsinoFavorito;
	}

	/**
	 * @param unidadeEnsinoFavorito the unidadeEnsinoFavorito to set
	 */
	public void setUnidadeEnsinoFavorito(Boolean unidadeEnsinoFavorito) {
		this.unidadeEnsinoFavorito = unidadeEnsinoFavorito;
	}

	/**
	 * @return the departamentoFavorito
	 */
	public Boolean getDepartamentoFavorito() {
		return departamentoFavorito;
	}

	/**
	 * @param departamentoFavorito the departamentoFavorito to set
	 */
	public void setDepartamentoFavorito(Boolean departamentoFavorito) {
		this.departamentoFavorito = departamentoFavorito;
	}

	/**
	 * @return the cargoFavorito
	 */
	public Boolean getCargoFavorito() {
		return cargoFavorito;
	}

	/**
	 * @param cargoFavorito the cargoFavorito to set
	 */
	public void setCargoFavorito(Boolean cargoFavorito) {
		this.cargoFavorito = cargoFavorito;
	}

	/**
	 * @return the fraseInspiracaoFavorito
	 */
	public Boolean getFraseInspiracaoFavorito() {
		return fraseInspiracaoFavorito;
	}

	/**
	 * @param fraseInspiracaoFavorito the fraseInspiracaoFavorito to set
	 */
	public void setFraseInspiracaoFavorito(Boolean fraseInspiracaoFavorito) {
		this.fraseInspiracaoFavorito = fraseInspiracaoFavorito;
	}

	/**
	 * @return the comunicacaoInternaFavorito
	 */
	public Boolean getComunicacaoInternaFavorito() {
		return comunicacaoInternaFavorito;
	}

	/**
	 * @param comunicacaoInternaFavorito the comunicacaoInternaFavorito to set
	 */
	public void setComunicacaoInternaFavorito(Boolean comunicacaoInternaFavorito) {
		this.comunicacaoInternaFavorito = comunicacaoInternaFavorito;
	}

	/**
	 * @return the funcionarioFavorito
	 */
	public Boolean getFuncionarioFavorito() {
		return funcionarioFavorito;
	}

	/**
	 * @param funcionarioFavorito the funcionarioFavorito to set
	 */
	public void setFuncionarioFavorito(Boolean funcionarioFavorito) {
		this.funcionarioFavorito = funcionarioFavorito;
	}

	/**
	 * @return the campanhaMarketingFavorito
	 */
	public Boolean getCampanhaMarketingFavorito() {
		return campanhaMarketingFavorito;
	}

	/**
	 * @param campanhaMarketingFavorito the campanhaMarketingFavorito to set
	 */
	public void setCampanhaMarketingFavorito(Boolean campanhaMarketingFavorito) {
		this.campanhaMarketingFavorito = campanhaMarketingFavorito;
	}

	/**
	 * @return the campanhaFavorito
	 */
	public Boolean getCampanhaFavorito() {
		return campanhaFavorito;
	}

	/**
	 * @param campanhaFavorito the campanhaFavorito to set
	 */
	public void setCampanhaFavorito(Boolean campanhaFavorito) {
		this.campanhaFavorito = campanhaFavorito;
	}

	/**
	 * @return the tipoMidiaCaptacaoFavorito
	 */
	public Boolean getTipoMidiaCaptacaoFavorito() {
		return tipoMidiaCaptacaoFavorito;
	}

	/**
	 * @param tipoMidiaCaptacaoFavorito the tipoMidiaCaptacaoFavorito to set
	 */
	public void setTipoMidiaCaptacaoFavorito(Boolean tipoMidiaCaptacaoFavorito) {
		this.tipoMidiaCaptacaoFavorito = tipoMidiaCaptacaoFavorito;
	}

	/**
	 * @return the visaoFavorito
	 */
	public Boolean getVisaoFavorito() {
		return visaoFavorito;
	}

	/**
	 * @param visaoFavorito the visaoFavorito to set
	 */
	public void setVisaoFavorito(Boolean visaoFavorito) {
		this.visaoFavorito = visaoFavorito;
	}

	/**
	 * @return the coordenadorFavorito
	 */
	public Boolean getCoordenadorFavorito() {
		return coordenadorFavorito;
	}

	/**
	 * @param coordenadorFavorito the coordenadorFavorito to set
	 */
	public void setCoordenadorFavorito(Boolean coordenadorFavorito) {
		this.coordenadorFavorito = coordenadorFavorito;
	}

	/**
	 * @return the ataProvaFavorito
	 */
	public Boolean getAtaProvaFavorito() {
		return ataProvaFavorito;
	}

	/**
	 * @param ataProvaFavorito the ataProvaFavorito to set
	 */
	public void setAtaProvaFavorito(Boolean ataProvaFavorito) {
		this.ataProvaFavorito = ataProvaFavorito;
	}

	/**
	 * @return the professorFavorito
	 */
	public Boolean getProfessorFavorito() {
		return professorFavorito;
	}

	/**
	 * @param professorFavorito the professorFavorito to set
	 */
	public void setProfessorFavorito(Boolean professorFavorito) {
		this.professorFavorito = professorFavorito;
	}

	/**
	 * @return the mapaAtualizacaoMatriculaFormadaFavorito
	 */
	public Boolean getMapaAtualizacaoMatriculaFormadaFavorito() {
		return mapaAtualizacaoMatriculaFormadaFavorito;
	}

	/**
	 * @param mapaAtualizacaoMatriculaFormadaFavorito the mapaAtualizacaoMatriculaFormadaFavorito to set
	 */
	public void setMapaAtualizacaoMatriculaFormadaFavorito(Boolean mapaAtualizacaoMatriculaFormadaFavorito) {
		this.mapaAtualizacaoMatriculaFormadaFavorito = mapaAtualizacaoMatriculaFormadaFavorito;
	}

	/**
	 * @return the titulacaoProfessorCursoFavorito
	 */
	public Boolean getTitulacaoProfessorCursoFavorito() {
		return titulacaoProfessorCursoFavorito;
	}

	/**
	 * @param titulacaoProfessorCursoFavorito the titulacaoProfessorCursoFavorito to set
	 */
	public void setTitulacaoProfessorCursoFavorito(Boolean titulacaoProfessorCursoFavorito) {
		this.titulacaoProfessorCursoFavorito = titulacaoProfessorCursoFavorito;
	}

	/**
	 * @return the navegarAbaDocumentacaoFavorito
	 */
	public Boolean getNavegarAbaDocumentacaoFavorito() {
		return navegarAbaDocumentacaoFavorito;
	}

	/**
	 * @param navegarAbaDocumentacaoFavorito the navegarAbaDocumentacaoFavorito to set
	 */
	public void setNavegarAbaDocumentacaoFavorito(Boolean navegarAbaDocumentacaoFavorito) {
		this.navegarAbaDocumentacaoFavorito = navegarAbaDocumentacaoFavorito;
	}

	/**
	 * @return the navegarAbaDisciplinasFavorito
	 */
	public Boolean getNavegarAbaDisciplinasFavorito() {
		return navegarAbaDisciplinasFavorito;
	}

	/**
	 * @param navegarAbaDisciplinasFavorito the navegarAbaDisciplinasFavorito to set
	 */
	public void setNavegarAbaDisciplinasFavorito(Boolean navegarAbaDisciplinasFavorito) {
		this.navegarAbaDisciplinasFavorito = navegarAbaDisciplinasFavorito;
	}

	/**
	 * @return the navegarAbaPlanoFinanceiroAlunoFavorito
	 */
	public Boolean getNavegarAbaPlanoFinanceiroAlunoFavorito() {
		return navegarAbaPlanoFinanceiroAlunoFavorito;
	}

	/**
	 * @param navegarAbaPlanoFinanceiroAlunoFavorito the navegarAbaPlanoFinanceiroAlunoFavorito to set
	 */
	public void setNavegarAbaPlanoFinanceiroAlunoFavorito(Boolean navegarAbaPlanoFinanceiroAlunoFavorito) {
		this.navegarAbaPlanoFinanceiroAlunoFavorito = navegarAbaPlanoFinanceiroAlunoFavorito;
	}

	/**
	 * @return the navegarAbaPendenciaFinanceiraFavorito
	 */
	public Boolean getNavegarAbaPendenciaFinanceiraFavorito() {
		return navegarAbaPendenciaFinanceiraFavorito;
	}

	/**
	 * @param navegarAbaPendenciaFinanceiraFavorito the navegarAbaPendenciaFinanceiraFavorito to set
	 */
	public void setNavegarAbaPendenciaFinanceiraFavorito(Boolean navegarAbaPendenciaFinanceiraFavorito) {
		this.navegarAbaPendenciaFinanceiraFavorito = navegarAbaPendenciaFinanceiraFavorito;
	}

	/**
	 * @return the enadeFavorito
	 */
	public Boolean getEnadeFavorito() {
		return enadeFavorito;
	}

	/**
	 * @param enadeFavorito the enadeFavorito to set
	 */
	public void setEnadeFavorito(Boolean enadeFavorito) {
		this.enadeFavorito = enadeFavorito;
	}

	/**
	 * @return the RegistroAulaFavorito
	 */
	public Boolean getRegistroAulaFavorito() {
		return RegistroAulaFavorito;
	}

	/**
	 * @param RegistroAulaFavorito the RegistroAulaFavorito to set
	 */
	public void setRegistroAulaFavorito(Boolean RegistroAulaFavorito) {
		this.RegistroAulaFavorito = RegistroAulaFavorito;
	}

	/**
	 * @return the professorMinistrouAulaTurmaFavorito
	 */
	public Boolean getProfessorMinistrouAulaTurmaFavorito() {
		return professorMinistrouAulaTurmaFavorito;
	}

	/**
	 * @param professorMinistrouAulaTurmaFavorito the professorMinistrouAulaTurmaFavorito to set
	 */
	public void setProfessorMinistrouAulaTurmaFavorito(Boolean professorMinistrouAulaTurmaFavorito) {
		this.professorMinistrouAulaTurmaFavorito = professorMinistrouAulaTurmaFavorito;
	}

	/**
	 * @return the ControleLivroRegistroDiplomaFavorito
	 */
	public Boolean getControleLivroRegistroDiplomaFavorito() {
		return ControleLivroRegistroDiplomaFavorito;
	}

	/**
	 * @param ControleLivroRegistroDiplomaFavorito the ControleLivroRegistroDiplomaFavorito to set
	 */
	public void setControleLivroRegistroDiplomaFavorito(Boolean ControleLivroRegistroDiplomaFavorito) {
		this.ControleLivroRegistroDiplomaFavorito = ControleLivroRegistroDiplomaFavorito;
	}

	/**
	 * @return the frequenciaAulaFavorito
	 */
	public Boolean getFrequenciaAulaFavorito() {
		return frequenciaAulaFavorito;
	}

	/**
	 * @param frequenciaAulaFavorito the frequenciaAulaFavorito to set
	 */
	public void setFrequenciaAulaFavorito(Boolean frequenciaAulaFavorito) {
		this.frequenciaAulaFavorito = frequenciaAulaFavorito;
	}

	/**
	 * @return the processoMatriculaFavorito
	 */
	public Boolean getProcessoMatriculaFavorito() {
		return processoMatriculaFavorito;
	}

	/**
	 * @param processoMatriculaFavorito the processoMatriculaFavorito to set
	 */
	public void setProcessoMatriculaFavorito(Boolean processoMatriculaFavorito) {
		this.processoMatriculaFavorito = processoMatriculaFavorito;
	}

	/**
	 * @return the mudarDataFimPeriodoLetivoFavorito
	 */
	public Boolean getMudarDataFimPeriodoLetivoFavorito() {
		return mudarDataFimPeriodoLetivoFavorito;
	}

	/**
	 * @param mudarDataFimPeriodoLetivoFavorito the mudarDataFimPeriodoLetivoFavorito to set
	 */
	public void setMudarDataFimPeriodoLetivoFavorito(Boolean mudarDataFimPeriodoLetivoFavorito) {
		this.mudarDataFimPeriodoLetivoFavorito = mudarDataFimPeriodoLetivoFavorito;
	}

	/**
	 * @return the matriculaFavorito
	 */
	public Boolean getMatriculaFavorito() {
		return matriculaFavorito;
	}

	/**
	 * @param matriculaFavorito the matriculaFavorito to set
	 */
	public void setMatriculaFavorito(Boolean matriculaFavorito) {
		this.matriculaFavorito = matriculaFavorito;
	}

	/**
	 * @return the logmatricula
	 */
	public Boolean getLogmatricula() {
		return logmatricula;
	}

	/**
	 * @param logmatricula the logmatricula to set
	 */
	public void setLogmatricula(Boolean logmatricula) {
		this.logmatricula = logmatricula;
	}

	/**
	 * @return the logmatriculaFavorito
	 */
	public Boolean getLogmatriculaFavorito() {
		return logmatriculaFavorito;
	}

	/**
	 * @param logmatriculaFavorito the logmatriculaFavorito to set
	 */
	public void setLogmatriculaFavorito(Boolean logmatriculaFavorito) {
		this.logmatriculaFavorito = logmatriculaFavorito;
	}

	/**
	 * @return the renovarMatriculaPorTurmaFavorito
	 */
	public Boolean getRenovarMatriculaPorTurmaFavorito() {
		return renovarMatriculaPorTurmaFavorito;
	}

	/**
	 * @param renovarMatriculaPorTurmaFavorito the renovarMatriculaPorTurmaFavorito to set
	 */
	public void setRenovarMatriculaPorTurmaFavorito(Boolean renovarMatriculaPorTurmaFavorito) {
		this.renovarMatriculaPorTurmaFavorito = renovarMatriculaPorTurmaFavorito;
	}

	/**
	 * @return the inclusaoForaPrazoFavorito
	 */
	public Boolean getInclusaoForaPrazoFavorito() {
		return inclusaoForaPrazoFavorito;
	}

	/**
	 * @param inclusaoForaPrazoFavorito the inclusaoForaPrazoFavorito to set
	 */
	public void setInclusaoForaPrazoFavorito(Boolean inclusaoForaPrazoFavorito) {
		this.inclusaoForaPrazoFavorito = inclusaoForaPrazoFavorito;
	}

	/**
	 * @return the exclusaoForaPrazoFavorito
	 */
	public Boolean getExclusaoForaPrazoFavorito() {
		return exclusaoForaPrazoFavorito;
	}

	/**
	 * @param exclusaoForaPrazoFavorito the exclusaoForaPrazoFavorito to set
	 */
	public void setExclusaoForaPrazoFavorito(Boolean exclusaoForaPrazoFavorito) {
		this.exclusaoForaPrazoFavorito = exclusaoForaPrazoFavorito;
	}

	/**
	 * @return the alteracoesCadastraisMatriculaFavorito
	 */
	public Boolean getAlteracoesCadastraisMatriculaFavorito() {
		return alteracoesCadastraisMatriculaFavorito;
	}

	/**
	 * @param alteracoesCadastraisMatriculaFavorito the alteracoesCadastraisMatriculaFavorito to set
	 */
	public void setAlteracoesCadastraisMatriculaFavorito(Boolean alteracoesCadastraisMatriculaFavorito) {
		this.alteracoesCadastraisMatriculaFavorito = alteracoesCadastraisMatriculaFavorito;
	}

	/**
	 * @return the matriculaEnadeFavorito
	 */
	public Boolean getMatriculaEnadeFavorito() {
		return matriculaEnadeFavorito;
	}

	/**
	 * @param matriculaEnadeFavorito the matriculaEnadeFavorito to set
	 */
	public void setMatriculaEnadeFavorito(Boolean matriculaEnadeFavorito) {
		this.matriculaEnadeFavorito = matriculaEnadeFavorito;
	}

	/**
	 * @return the historicoFavorito
	 */
	public Boolean getHistoricoFavorito() {
		return historicoFavorito;
	}

	/**
	 * @param historicoFavorito the historicoFavorito to set
	 */
	public void setHistoricoFavorito(Boolean historicoFavorito) {
		this.historicoFavorito = historicoFavorito;
	}

	/**
	 * @return the trancamentoFavorito
	 */
	public Boolean getTrancamentoFavorito() {
		return trancamentoFavorito;
	}

	/**
	 * @param trancamentoFavorito the trancamentoFavorito to set
	 */
	public void setTrancamentoFavorito(Boolean trancamentoFavorito) {
		this.trancamentoFavorito = trancamentoFavorito;
	}

	/**
	 * @return the cancelamentoFavorito
	 */
	public Boolean getCancelamentoFavorito() {
		return cancelamentoFavorito;
	}

	/**
	 * @param cancelamentoFavorito the cancelamentoFavorito to set
	 */
	public void setCancelamentoFavorito(Boolean cancelamentoFavorito) {
		this.cancelamentoFavorito = cancelamentoFavorito;
	}

	/**
	 * @return the transferenciaEntradaFavorito
	 */
	public Boolean getTransferenciaEntradaFavorito() {
		return transferenciaEntradaFavorito;
	}

	/**
	 * @param transferenciaEntradaFavorito the transferenciaEntradaFavorito to set
	 */
	public void setTransferenciaEntradaFavorito(Boolean transferenciaEntradaFavorito) {
		this.transferenciaEntradaFavorito = transferenciaEntradaFavorito;
	}

	/**
	 * @return the aproveitamentoDisciplinaFavorito
	 */
	public Boolean getAproveitamentoDisciplinaFavorito() {
		return aproveitamentoDisciplinaFavorito;
	}

	/**
	 * @param aproveitamentoDisciplinaFavorito the aproveitamentoDisciplinaFavorito to set
	 */
	public void setAproveitamentoDisciplinaFavorito(Boolean aproveitamentoDisciplinaFavorito) {
		this.aproveitamentoDisciplinaFavorito = aproveitamentoDisciplinaFavorito;
	}

	/**
	 * @return the transferenciaInternaFavorito
	 */
	public Boolean getTransferenciaInternaFavorito() {
		return transferenciaInternaFavorito;
	}

	/**
	 * @param transferenciaInternaFavorito the transferenciaInternaFavorito to set
	 */
	public void setTransferenciaInternaFavorito(Boolean transferenciaInternaFavorito) {
		this.transferenciaInternaFavorito = transferenciaInternaFavorito;
	}

	/**
	 * @return the transferenciaSaidaFavorito
	 */
	public Boolean getTransferenciaSaidaFavorito() {
		return transferenciaSaidaFavorito;
	}

	/**
	 * @param transferenciaSaidaFavorito the transferenciaSaidaFavorito to set
	 */
	public void setTransferenciaSaidaFavorito(Boolean transferenciaSaidaFavorito) {
		this.transferenciaSaidaFavorito = transferenciaSaidaFavorito;
	}

	/**
	 * @return the planoDescontoFavorito
	 */
	public Boolean getPlanoDescontoFavorito() {
		return planoDescontoFavorito;
	}

	/**
	 * @param planoDescontoFavorito the planoDescontoFavorito to set
	 */
	public void setPlanoDescontoFavorito(Boolean planoDescontoFavorito) {
		this.planoDescontoFavorito = planoDescontoFavorito;
	}

	/**
	 * @return the planoFinanceiroCursoFavorito
	 */
	public Boolean getPlanoFinanceiroCursoFavorito() {
		return planoFinanceiroCursoFavorito;
	}

	/**
	 * @param planoFinanceiroCursoFavorito the planoFinanceiroCursoFavorito to set
	 */
	public void setPlanoFinanceiroCursoFavorito(Boolean planoFinanceiroCursoFavorito) {
		this.planoFinanceiroCursoFavorito = planoFinanceiroCursoFavorito;
	}

	/**
	 * @return the planoFinanceiroReposicaoFavorito
	 */
	public Boolean getPlanoFinanceiroReposicaoFavorito() {
		return planoFinanceiroReposicaoFavorito;
	}

	/**
	 * @param planoFinanceiroReposicaoFavorito the planoFinanceiroReposicaoFavorito to set
	 */
	public void setPlanoFinanceiroReposicaoFavorito(Boolean planoFinanceiroReposicaoFavorito) {
		this.planoFinanceiroReposicaoFavorito = planoFinanceiroReposicaoFavorito;
	}

	/**
	 * @return the textoPadraoFavorito
	 */
	public Boolean getTextoPadraoFavorito() {
		return textoPadraoFavorito;
	}

	/**
	 * @param textoPadraoFavorito the textoPadraoFavorito to set
	 */
	public void setTextoPadraoFavorito(Boolean textoPadraoFavorito) {
		this.textoPadraoFavorito = textoPadraoFavorito;
	}

	/**
	 * @return the planoFinanceiroAlunoFavorito
	 */
	public Boolean getPlanoFinanceiroAlunoFavorito() {
		return planoFinanceiroAlunoFavorito;
	}

	/**
	 * @param planoFinanceiroAlunoFavorito the planoFinanceiroAlunoFavorito to set
	 */
	public void setPlanoFinanceiroAlunoFavorito(Boolean planoFinanceiroAlunoFavorito) {
		this.planoFinanceiroAlunoFavorito = planoFinanceiroAlunoFavorito;
	}

	/**
	 * @return the controleGeracaoParcelaTurmaFavorito
	 */
	public Boolean getControleGeracaoParcelaTurmaFavorito() {
		return controleGeracaoParcelaTurmaFavorito;
	}

	/**
	 * @param controleGeracaoParcelaTurmaFavorito the controleGeracaoParcelaTurmaFavorito to set
	 */
	public void setControleGeracaoParcelaTurmaFavorito(Boolean controleGeracaoParcelaTurmaFavorito) {
		this.controleGeracaoParcelaTurmaFavorito = controleGeracaoParcelaTurmaFavorito;
	}

	/**
	 * @return the descontoProgressivoFavorito
	 */
	public Boolean getDescontoProgressivoFavorito() {
		return descontoProgressivoFavorito;
	}

	/**
	 * @param descontoProgressivoFavorito the descontoProgressivoFavorito to set
	 */
	public void setDescontoProgressivoFavorito(Boolean descontoProgressivoFavorito) {
		this.descontoProgressivoFavorito = descontoProgressivoFavorito;
	}

	/**
	 * @return the portadorDiplomaFavorito
	 */
	public Boolean getPortadorDiplomaFavorito() {
		return portadorDiplomaFavorito;
	}

	/**
	 * @param portadorDiplomaFavorito the portadorDiplomaFavorito to set
	 */
	public void setPortadorDiplomaFavorito(Boolean portadorDiplomaFavorito) {
		this.portadorDiplomaFavorito = portadorDiplomaFavorito;
	}

	/**
	 * @return the periodoLetivoAtivoUnidadeEnsinoCursoFavorito
	 */
	public Boolean getPeriodoLetivoAtivoUnidadeEnsinoCursoFavorito() {
		return periodoLetivoAtivoUnidadeEnsinoCursoFavorito;
	}

	/**
	 * @param periodoLetivoAtivoUnidadeEnsinoCursoFavorito the periodoLetivoAtivoUnidadeEnsinoCursoFavorito to set
	 */
	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoFavorito(Boolean periodoLetivoAtivoUnidadeEnsinoCursoFavorito) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoFavorito = periodoLetivoAtivoUnidadeEnsinoCursoFavorito;
	}

	/**
	 * @return the abonoFaltaFavorito
	 */
	public Boolean getAbonoFaltaFavorito() {
		return abonoFaltaFavorito;
	}

	/**
	 * @param abonoFaltaFavorito the abonoFaltaFavorito to set
	 */
	public void setAbonoFaltaFavorito(Boolean abonoFaltaFavorito) {
		this.abonoFaltaFavorito = abonoFaltaFavorito;
	}

	/**
	 * @return the tipoDocumentoFavorito
	 */
	public Boolean getTipoDocumentoFavorito() {
		return tipoDocumentoFavorito;
	}

	/**
	 * @param tipoDocumentoFavorito the tipoDocumentoFavorito to set
	 */
	public void setTipoDocumentoFavorito(Boolean tipoDocumentoFavorito) {
		this.tipoDocumentoFavorito = tipoDocumentoFavorito;
	}

	/**
	 * @return the entregaDocumentoFavorito
	 */
	public Boolean getEntregaDocumentoFavorito() {
		return entregaDocumentoFavorito;
	}

	/**
	 * @param entregaDocumentoFavorito the entregaDocumentoFavorito to set
	 */
	public void setEntregaDocumentoFavorito(Boolean entregaDocumentoFavorito) {
		this.entregaDocumentoFavorito = entregaDocumentoFavorito;
	}

	/**
	 * @return the permiteLeituraArquivoScannerFavorito
	 */
	public Boolean getPermiteLeituraArquivoScannerFavorito() {
		return permiteLeituraArquivoScannerFavorito;
	}

	/**
	 * @param permiteLeituraArquivoScannerFavorito the permiteLeituraArquivoScannerFavorito to set
	 */
	public void setPermiteLeituraArquivoScannerFavorito(Boolean permiteLeituraArquivoScannerFavorito) {
		this.permiteLeituraArquivoScannerFavorito = permiteLeituraArquivoScannerFavorito;
	}

	/**
	 * @return the permiteLeituraArquivoScannerMatriculaFavorito
	 */
	public Boolean getPermiteLeituraArquivoScannerMatriculaFavorito() {
		return permiteLeituraArquivoScannerMatriculaFavorito;
	}

	/**
	 * @param permiteLeituraArquivoScannerMatriculaFavorito the permiteLeituraArquivoScannerMatriculaFavorito to set
	 */
	public void setPermiteLeituraArquivoScannerMatriculaFavorito(Boolean permiteLeituraArquivoScannerMatriculaFavorito) {
		this.permiteLeituraArquivoScannerMatriculaFavorito = permiteLeituraArquivoScannerMatriculaFavorito;
	}

	/**
	 * @return the confirmacaoPreMatriculaFavorito
	 */
	public Boolean getConfirmacaoPreMatriculaFavorito() {
		return confirmacaoPreMatriculaFavorito;
	}

	/**
	 * @param confirmacaoPreMatriculaFavorito the confirmacaoPreMatriculaFavorito to set
	 */
	public void setConfirmacaoPreMatriculaFavorito(Boolean confirmacaoPreMatriculaFavorito) {
		this.confirmacaoPreMatriculaFavorito = confirmacaoPreMatriculaFavorito;
	}

	/**
	 * @return the colacaoGrauFavorito
	 */
	public Boolean getColacaoGrauFavorito() {
		return colacaoGrauFavorito;
	}

	/**
	 * @param colacaoGrauFavorito the colacaoGrauFavorito to set
	 */
	public void setColacaoGrauFavorito(Boolean colacaoGrauFavorito) {
		this.colacaoGrauFavorito = colacaoGrauFavorito;
	}

	/**
	 * @return the expedicaoDiplomaFavorito
	 */
	public Boolean getExpedicaoDiplomaFavorito() {
		return expedicaoDiplomaFavorito;
	}

	/**
	 * @param expedicaoDiplomaFavorito the expedicaoDiplomaFavorito to set
	 */
	public void setExpedicaoDiplomaFavorito(Boolean expedicaoDiplomaFavorito) {
		this.expedicaoDiplomaFavorito = expedicaoDiplomaFavorito;
	}

	/**
	 * @return the programacaoFormaturaFavorito
	 */
	public Boolean getProgramacaoFormaturaFavorito() {
		return programacaoFormaturaFavorito;
	}

	/**
	 * @param programacaoFormaturaFavorito the programacaoFormaturaFavorito to set
	 */
	public void setProgramacaoFormaturaFavorito(Boolean programacaoFormaturaFavorito) {
		this.programacaoFormaturaFavorito = programacaoFormaturaFavorito;
	}

	/**
	 * @return the uploadProfessorRelFavorito
	 */
	public Boolean getUploadProfessorRelFavorito() {
		return uploadProfessorRelFavorito;
	}

	/**
	 * @param uploadProfessorRelFavorito the uploadProfessorRelFavorito to set
	 */
	public void setUploadProfessorRelFavorito(Boolean uploadProfessorRelFavorito) {
		this.uploadProfessorRelFavorito = uploadProfessorRelFavorito;
	}

	/**
	 * @return the registroPresencaColacaoGrauFavorito
	 */
	public Boolean getRegistroPresencaColacaoGrauFavorito() {
		return registroPresencaColacaoGrauFavorito;
	}

	/**
	 * @param registroPresencaColacaoGrauFavorito the registroPresencaColacaoGrauFavorito to set
	 */
	public void setRegistroPresencaColacaoGrauFavorito(Boolean registroPresencaColacaoGrauFavorito) {
		this.registroPresencaColacaoGrauFavorito = registroPresencaColacaoGrauFavorito;
	}

	/**
	 * @return the transferenciaMatrizCurricularFavorito
	 */
	public Boolean getTransferenciaMatrizCurricularFavorito() {
		return transferenciaMatrizCurricularFavorito;
	}

	/**
	 * @param transferenciaMatrizCurricularFavorito the transferenciaMatrizCurricularFavorito to set
	 */
	public void setTransferenciaMatrizCurricularFavorito(Boolean transferenciaMatrizCurricularFavorito) {
		this.transferenciaMatrizCurricularFavorito = transferenciaMatrizCurricularFavorito;
	}

	/**
	 * @return the transferenciaTurmaFavorito
	 */
	public Boolean getTransferenciaTurmaFavorito() {
		return transferenciaTurmaFavorito;
	}

	/**
	 * @param transferenciaTurmaFavorito the transferenciaTurmaFavorito to set
	 */
	public void setTransferenciaTurmaFavorito(Boolean transferenciaTurmaFavorito) {
		this.transferenciaTurmaFavorito = transferenciaTurmaFavorito;
	}

	/**
	 * @return the atividadeComplementarFavorito
	 */
	public Boolean getAtividadeComplementarFavorito() {
		return atividadeComplementarFavorito;
	}

	/**
	 * @param atividadeComplementarFavorito the atividadeComplementarFavorito to set
	 */
	public void setAtividadeComplementarFavorito(Boolean atividadeComplementarFavorito) {
		this.atividadeComplementarFavorito = atividadeComplementarFavorito;
	}

	/**
	 * @return the lancamentoNotaFavorito
	 */
	public Boolean getLancamentoNotaFavorito() {
		return lancamentoNotaFavorito;
	}

	/**
	 * @param lancamentoNotaFavorito the lancamentoNotaFavorito to set
	 */
	public void setLancamentoNotaFavorito(Boolean lancamentoNotaFavorito) {
		this.lancamentoNotaFavorito = lancamentoNotaFavorito;
	}

	/**
	 * @return the textoPadraoDeclaracaoFavorito
	 */
	public Boolean getTextoPadraoDeclaracaoFavorito() {
		return textoPadraoDeclaracaoFavorito;
	}

	/**
	 * @param textoPadraoDeclaracaoFavorito the textoPadraoDeclaracaoFavorito to set
	 */
	public void setTextoPadraoDeclaracaoFavorito(Boolean textoPadraoDeclaracaoFavorito) {
		this.textoPadraoDeclaracaoFavorito = textoPadraoDeclaracaoFavorito;
	}

	/**
	 * @return the reativacaoMatriculaFavorito
	 */
	public Boolean getReativacaoMatriculaFavorito() {
		return reativacaoMatriculaFavorito;
	}

	/**
	 * @param reativacaoMatriculaFavorito the reativacaoMatriculaFavorito to set
	 */
	public void setReativacaoMatriculaFavorito(Boolean reativacaoMatriculaFavorito) {
		this.reativacaoMatriculaFavorito = reativacaoMatriculaFavorito;
	}

	/**
	 * @return the mapaNotaAlunoPorTurmaFavorito
	 */
	public Boolean getMapaNotaAlunoPorTurmaFavorito() {
		return mapaNotaAlunoPorTurmaFavorito;
	}

	/**
	 * @param mapaNotaAlunoPorTurmaFavorito the mapaNotaAlunoPorTurmaFavorito to set
	 */
	public void setMapaNotaAlunoPorTurmaFavorito(Boolean mapaNotaAlunoPorTurmaFavorito) {
		this.mapaNotaAlunoPorTurmaFavorito = mapaNotaAlunoPorTurmaFavorito;
	}

	/**
	 * @return the mapaNotasDisciplinaAlunosFavorito
	 */
	public Boolean getMapaNotasDisciplinaAlunosFavorito() {
		return mapaNotasDisciplinaAlunosFavorito;
	}

	/**
	 * @param mapaNotasDisciplinaAlunosFavorito the mapaNotasDisciplinaAlunosFavorito to set
	 */
	public void setMapaNotasDisciplinaAlunosFavorito(Boolean mapaNotasDisciplinaAlunosFavorito) {
		this.mapaNotasDisciplinaAlunosFavorito = mapaNotasDisciplinaAlunosFavorito;
	}

	/**
	 * @return the inclusaoExclusaoDisciplinaFavorito
	 */
	public Boolean getInclusaoExclusaoDisciplinaFavorito() {
		return inclusaoExclusaoDisciplinaFavorito;
	}

	/**
	 * @param inclusaoExclusaoDisciplinaFavorito the inclusaoExclusaoDisciplinaFavorito to set
	 */
	public void setInclusaoExclusaoDisciplinaFavorito(Boolean inclusaoExclusaoDisciplinaFavorito) {
		this.inclusaoExclusaoDisciplinaFavorito = inclusaoExclusaoDisciplinaFavorito;
	}

	/**
	 * @return the transferenciaTurnoFavorito
	 */
	public Boolean getTransferenciaTurnoFavorito() {
		return transferenciaTurnoFavorito;
	}

	/**
	 * @param transferenciaTurnoFavorito the transferenciaTurnoFavorito to set
	 */
	public void setTransferenciaTurnoFavorito(Boolean transferenciaTurnoFavorito) {
		this.transferenciaTurnoFavorito = transferenciaTurnoFavorito;
	}

	/**
	 * @return the identificacaoEstudantilFavorito
	 */
	public Boolean getIdentificacaoEstudantilFavorito() {
		return identificacaoEstudantilFavorito;
	}

	/**
	 * @param identificacaoEstudantilFavorito the identificacaoEstudantilFavorito to set
	 */
	public void setIdentificacaoEstudantilFavorito(Boolean identificacaoEstudantilFavorito) {
		this.identificacaoEstudantilFavorito = identificacaoEstudantilFavorito;
	}

	/**
	 * @return the informacaoProfessorRelFavorito
	 */
	public Boolean getInformacaoProfessorRelFavorito() {
		return informacaoProfessorRelFavorito;
	}

	/**
	 * @param informacaoProfessorRelFavorito the informacaoProfessorRelFavorito to set
	 */
	public void setInformacaoProfessorRelFavorito(Boolean informacaoProfessorRelFavorito) {
		this.informacaoProfessorRelFavorito = informacaoProfessorRelFavorito;
	}

	/**
	 * @return the consultorMatriculaFavorito
	 */
	public Boolean getConsultorMatriculaFavorito() {
		return consultorMatriculaFavorito;
	}

	/**
	 * @param consultorMatriculaFavorito the consultorMatriculaFavorito to set
	 */
	public void setConsultorMatriculaFavorito(Boolean consultorMatriculaFavorito) {
		this.consultorMatriculaFavorito = consultorMatriculaFavorito;
	}

	/**
	 * @return the planoDescontoInclusaoDisciplinaFavorito
	 */
	public Boolean getPlanoDescontoInclusaoDisciplinaFavorito() {
		return planoDescontoInclusaoDisciplinaFavorito;
	}

	/**
	 * @param planoDescontoInclusaoDisciplinaFavorito the planoDescontoInclusaoDisciplinaFavorito to set
	 */
	public void setPlanoDescontoInclusaoDisciplinaFavorito(Boolean planoDescontoInclusaoDisciplinaFavorito) {
		this.planoDescontoInclusaoDisciplinaFavorito = planoDescontoInclusaoDisciplinaFavorito;
	}

	/**
	 * @return the impressaoContratoFavorito
	 */
	public Boolean getImpressaoContratoFavorito() {
		return impressaoContratoFavorito;
	}

	/**
	 * @param impressaoContratoFavorito the impressaoContratoFavorito to set
	 */
	public void setImpressaoContratoFavorito(Boolean impressaoContratoFavorito) {
		this.impressaoContratoFavorito = impressaoContratoFavorito;
	}

	/**
	 * @return the impressaoDeclaracaoFavorito
	 */
	public Boolean getImpressaoDeclaracaoFavorito() {
		return impressaoDeclaracaoFavorito;
	}

	/**
	 * @param impressaoDeclaracaoFavorito the impressaoDeclaracaoFavorito to set
	 */
	public void setImpressaoDeclaracaoFavorito(Boolean impressaoDeclaracaoFavorito) {
		this.impressaoDeclaracaoFavorito = impressaoDeclaracaoFavorito;
	}

	/**
	 * @return the motivoCancelamentoTrancamentoFavorito
	 */
	public Boolean getMotivoCancelamentoTrancamentoFavorito() {
		return motivoCancelamentoTrancamentoFavorito;
	}

	/**
	 * @param motivoCancelamentoTrancamentoFavorito the motivoCancelamentoTrancamentoFavorito to set
	 */
	public void setMotivoCancelamentoTrancamentoFavorito(Boolean motivoCancelamentoTrancamentoFavorito) {
		this.motivoCancelamentoTrancamentoFavorito = motivoCancelamentoTrancamentoFavorito;
	}

	/**
	 * @return the registroAulaNotaFavorito
	 */
	public Boolean getRegistroAulaNotaFavorito() {
		return registroAulaNotaFavorito;
	}

	/**
	 * @param registroAulaNotaFavorito the registroAulaNotaFavorito to set
	 */
	public void setRegistroAulaNotaFavorito(Boolean registroAulaNotaFavorito) {
		this.registroAulaNotaFavorito = registroAulaNotaFavorito;
	}

	/**
	 * @return the permitirProfessorExcluirArquivoInstituicaoFavorito
	 */
	public Boolean getPermitirProfessorExcluirArquivoInstituicaoFavorito() {
		return permitirProfessorExcluirArquivoInstituicaoFavorito;
	}

	/**
	 * @param permitirProfessorExcluirArquivoInstituicaoFavorito the permitirProfessorExcluirArquivoInstituicaoFavorito to set
	 */
	public void setPermitirProfessorExcluirArquivoInstituicaoFavorito(Boolean permitirProfessorExcluirArquivoInstituicaoFavorito) {
		this.permitirProfessorExcluirArquivoInstituicaoFavorito = permitirProfessorExcluirArquivoInstituicaoFavorito;
	}

	/**
	 * @return the exclusaoMatriculaFavorito
	 */
	public Boolean getExclusaoMatriculaFavorito() {
		return exclusaoMatriculaFavorito;
	}

	/**
	 * @param exclusaoMatriculaFavorito the exclusaoMatriculaFavorito to set
	 */
	public void setExclusaoMatriculaFavorito(Boolean exclusaoMatriculaFavorito) {
		this.exclusaoMatriculaFavorito = exclusaoMatriculaFavorito;
	}

	/**
	 * @return the transferenciaUnidadeFavorito
	 */
	public Boolean getTransferenciaUnidadeFavorito() {
		return transferenciaUnidadeFavorito;
	}

	/**
	 * @param transferenciaUnidadeFavorito the transferenciaUnidadeFavorito to set
	 */
	public void setTransferenciaUnidadeFavorito(Boolean transferenciaUnidadeFavorito) {
		this.transferenciaUnidadeFavorito = transferenciaUnidadeFavorito;
	}

	/**
	 * @return the alterarUserNameSenhaAlunosFavorito
	 */
	public Boolean getAlterarUserNameSenhaAlunosFavorito() {
		return alterarUserNameSenhaAlunosFavorito;
	}

	/**
	 * @param alterarUserNameSenhaAlunosFavorito the alterarUserNameSenhaAlunosFavorito to set
	 */
	public void setAlterarUserNameSenhaAlunosFavorito(Boolean alterarUserNameSenhaAlunosFavorito) {
		this.alterarUserNameSenhaAlunosFavorito = alterarUserNameSenhaAlunosFavorito;
	}

	/**
	 * @return the mapaNotaAlunoPorTurmaRelFavorito
	 */
	public Boolean getMapaNotaAlunoPorTurmaRelFavorito() {
		return mapaNotaAlunoPorTurmaRelFavorito;
	}

	/**
	 * @param mapaNotaAlunoPorTurmaRelFavorito the mapaNotaAlunoPorTurmaRelFavorito to set
	 */
	public void setMapaNotaAlunoPorTurmaRelFavorito(Boolean mapaNotaAlunoPorTurmaRelFavorito) {
		this.mapaNotaAlunoPorTurmaRelFavorito = mapaNotaAlunoPorTurmaRelFavorito;
	}

	/**
	 * @return the mapaNotasDisciplinaAlunosRelFavorito
	 */
	public Boolean getMapaNotasDisciplinaAlunosRelFavorito() {
		return mapaNotasDisciplinaAlunosRelFavorito;
	}

	/**
	 * @param mapaNotasDisciplinaAlunosRelFavorito the mapaNotasDisciplinaAlunosRelFavorito to set
	 */
	public void setMapaNotasDisciplinaAlunosRelFavorito(Boolean mapaNotasDisciplinaAlunosRelFavorito) {
		this.mapaNotasDisciplinaAlunosRelFavorito = mapaNotasDisciplinaAlunosRelFavorito;
	}

	/**
	 * @return the autorizarSolicitacaoAberturaTurmaFavorito
	 */
	public Boolean getAutorizarSolicitacaoAberturaTurmaFavorito() {
		return autorizarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @param autorizarSolicitacaoAberturaTurmaFavorito the autorizarSolicitacaoAberturaTurmaFavorito to set
	 */
	public void setAutorizarSolicitacaoAberturaTurmaFavorito(Boolean autorizarSolicitacaoAberturaTurmaFavorito) {
		this.autorizarSolicitacaoAberturaTurmaFavorito = autorizarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @return the naoAutorizarSolicitacaoAberturaTurmaFavorito
	 */
	public Boolean getNaoAutorizarSolicitacaoAberturaTurmaFavorito() {
		return naoAutorizarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @param naoAutorizarSolicitacaoAberturaTurmaFavorito the naoAutorizarSolicitacaoAberturaTurmaFavorito to set
	 */
	public void setNaoAutorizarSolicitacaoAberturaTurmaFavorito(Boolean naoAutorizarSolicitacaoAberturaTurmaFavorito) {
		this.naoAutorizarSolicitacaoAberturaTurmaFavorito = naoAutorizarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @return the revisarSolicitacaoAberturaTurmaFavorito
	 */
	public Boolean getRevisarSolicitacaoAberturaTurmaFavorito() {
		return revisarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @param revisarSolicitacaoAberturaTurmaFavorito the revisarSolicitacaoAberturaTurmaFavorito to set
	 */
	public void setRevisarSolicitacaoAberturaTurmaFavorito(Boolean revisarSolicitacaoAberturaTurmaFavorito) {
		this.revisarSolicitacaoAberturaTurmaFavorito = revisarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @return the finalizarSolicitacaoAberturaTurmaFavorito
	 */
	public Boolean getFinalizarSolicitacaoAberturaTurmaFavorito() {
		return finalizarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @param finalizarSolicitacaoAberturaTurmaFavorito the finalizarSolicitacaoAberturaTurmaFavorito to set
	 */
	public void setFinalizarSolicitacaoAberturaTurmaFavorito(Boolean finalizarSolicitacaoAberturaTurmaFavorito) {
		this.finalizarSolicitacaoAberturaTurmaFavorito = finalizarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @return the permiteRevisarSolicitacaoAberturaTurmaFavorito
	 */
	public Boolean getPermiteRevisarSolicitacaoAberturaTurmaFavorito() {
		return permiteRevisarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @param permiteRevisarSolicitacaoAberturaTurmaFavorito the permiteRevisarSolicitacaoAberturaTurmaFavorito to set
	 */
	public void setPermiteRevisarSolicitacaoAberturaTurmaFavorito(Boolean permiteRevisarSolicitacaoAberturaTurmaFavorito) {
		this.permiteRevisarSolicitacaoAberturaTurmaFavorito = permiteRevisarSolicitacaoAberturaTurmaFavorito;
	}

	/**
	 * @return the chamadaCandidatoAprovadoRelFavorito
	 */
	public Boolean getChamadaCandidatoAprovadoRelFavorito() {
		return chamadaCandidatoAprovadoRelFavorito;
	}

	/**
	 * @param chamadaCandidatoAprovadoRelFavorito the chamadaCandidatoAprovadoRelFavorito to set
	 */
	public void setChamadaCandidatoAprovadoRelFavorito(Boolean chamadaCandidatoAprovadoRelFavorito) {
		this.chamadaCandidatoAprovadoRelFavorito = chamadaCandidatoAprovadoRelFavorito;
	}

	/**
	 * @return the avaliacaoInstitucionalFavorito
	 */
	public Boolean getAvaliacaoInstitucionalFavorito() {
		return avaliacaoInstitucionalFavorito;
	}

	/**
	 * @param avaliacaoInstitucionalFavorito the avaliacaoInstitucionalFavorito to set
	 */
	public void setAvaliacaoInstitucionalFavorito(Boolean avaliacaoInstitucionalFavorito) {
		this.avaliacaoInstitucionalFavorito = avaliacaoInstitucionalFavorito;
	}

	/**
	 * @return the avaliacaoInstitucionalPresencialRespostaFavorito
	 */
	public Boolean getAvaliacaoInstitucionalPresencialRespostaFavorito() {
		return avaliacaoInstitucionalPresencialRespostaFavorito;
	}

	/**
	 * @param avaliacaoInstitucionalPresencialRespostaFavorito the avaliacaoInstitucionalPresencialRespostaFavorito to set
	 */
	public void setAvaliacaoInstitucionalPresencialRespostaFavorito(Boolean avaliacaoInstitucionalPresencialRespostaFavorito) {
		this.avaliacaoInstitucionalPresencialRespostaFavorito = avaliacaoInstitucionalPresencialRespostaFavorito;
	}

	/**
	 * @return the unidadeEnsinoRelFavorito
	 */
	public Boolean getUnidadeEnsinoRelFavorito() {
		return unidadeEnsinoRelFavorito;
	}

	/**
	 * @param unidadeEnsinoRelFavorito the unidadeEnsinoRelFavorito to set
	 */
	public void setUnidadeEnsinoRelFavorito(Boolean unidadeEnsinoRelFavorito) {
		this.unidadeEnsinoRelFavorito = unidadeEnsinoRelFavorito;
	}

	/**
	 * @return the campanhaMarketingRelFavorito
	 */
	public Boolean getCampanhaMarketingRelFavorito() {
		return campanhaMarketingRelFavorito;
	}

	/**
	 * @param campanhaMarketingRelFavorito the campanhaMarketingRelFavorito to set
	 */
	public void setCampanhaMarketingRelFavorito(Boolean campanhaMarketingRelFavorito) {
		this.campanhaMarketingRelFavorito = campanhaMarketingRelFavorito;
	}

	/**
	 * @return the disciplinaRelFavorito
	 */
	public Boolean getDisciplinaRelFavorito() {
		return disciplinaRelFavorito;
	}

	/**
	 * @param disciplinaRelFavorito the disciplinaRelFavorito to set
	 */
	public void setDisciplinaRelFavorito(Boolean disciplinaRelFavorito) {
		this.disciplinaRelFavorito = disciplinaRelFavorito;
	}

	/**
	 * @return the processoMatriculaRelFavorito
	 */
	public Boolean getProcessoMatriculaRelFavorito() {
		return processoMatriculaRelFavorito;
	}

	/**
	 * @param processoMatriculaRelFavorito the processoMatriculaRelFavorito to set
	 */
	public void setProcessoMatriculaRelFavorito(Boolean processoMatriculaRelFavorito) {
		this.processoMatriculaRelFavorito = processoMatriculaRelFavorito;
	}

	/**
	 * @return the procSeletivoRelFavorito
	 */
	public Boolean getProcSeletivoRelFavorito() {
		return procSeletivoRelFavorito;
	}

	/**
	 * @param procSeletivoRelFavorito the procSeletivoRelFavorito to set
	 */
	public void setProcSeletivoRelFavorito(Boolean procSeletivoRelFavorito) {
		this.procSeletivoRelFavorito = procSeletivoRelFavorito;
	}

	/**
	 * @return the contaReceberRelFavorito
	 */
	public Boolean getContaReceberRelFavorito() {
		return contaReceberRelFavorito;
	}

	/**
	 * @param contaReceberRelFavorito the contaReceberRelFavorito to set
	 */
	public void setContaReceberRelFavorito(Boolean contaReceberRelFavorito) {
		this.contaReceberRelFavorito = contaReceberRelFavorito;
	}

	/**
	 * @return the declaracaoImpostoRendaRelFavorito
	 */
	public Boolean getDeclaracaoImpostoRendaRelFavorito() {
		return declaracaoImpostoRendaRelFavorito;
	}

	/**
	 * @param declaracaoImpostoRendaRelFavorito the declaracaoImpostoRendaRelFavorito to set
	 */
	public void setDeclaracaoImpostoRendaRelFavorito(Boolean declaracaoImpostoRendaRelFavorito) {
		this.declaracaoImpostoRendaRelFavorito = declaracaoImpostoRendaRelFavorito;
	}

	/**
	 * @return the boletosRelFavorito
	 */
	public Boolean getBoletosRelFavorito() {
		return boletosRelFavorito;
	}

	/**
	 * @param boletosRelFavorito the boletosRelFavorito to set
	 */
	public void setBoletosRelFavorito(Boolean boletosRelFavorito) {
		this.boletosRelFavorito = boletosRelFavorito;
	}

	/**
	 * @return the termoReconhecimentoDividaRelFavorito
	 */
	public Boolean getTermoReconhecimentoDividaRelFavorito() {
		return termoReconhecimentoDividaRelFavorito;
	}

	/**
	 * @param termoReconhecimentoDividaRelFavorito the termoReconhecimentoDividaRelFavorito to set
	 */
	public void setTermoReconhecimentoDividaRelFavorito(Boolean termoReconhecimentoDividaRelFavorito) {
		this.termoReconhecimentoDividaRelFavorito = termoReconhecimentoDividaRelFavorito;
	}

	/**
	 * @return the renegociacaoRelFavorito
	 */
	public Boolean getRenegociacaoRelFavorito() {
		return renegociacaoRelFavorito;
	}

	/**
	 * @param renegociacaoRelFavorito the renegociacaoRelFavorito to set
	 */
	public void setRenegociacaoRelFavorito(Boolean renegociacaoRelFavorito) {
		this.renegociacaoRelFavorito = renegociacaoRelFavorito;
	}

	/**
	 * @return the fluxoCaixaRelFavorito
	 */
	public Boolean getFluxoCaixaRelFavorito() {
		return fluxoCaixaRelFavorito;
	}

	/**
	 * @param fluxoCaixaRelFavorito the fluxoCaixaRelFavorito to set
	 */
	public void setFluxoCaixaRelFavorito(Boolean fluxoCaixaRelFavorito) {
		this.fluxoCaixaRelFavorito = fluxoCaixaRelFavorito;
	}

	/**
	 * @return the faturamentoRecebimentoRelFavorito
	 */
	public Boolean getFaturamentoRecebimentoRelFavorito() {
		return faturamentoRecebimentoRelFavorito;
	}

	/**
	 * @param faturamentoRecebimentoRelFavorito the faturamentoRecebimentoRelFavorito to set
	 */
	public void setFaturamentoRecebimentoRelFavorito(Boolean faturamentoRecebimentoRelFavorito) {
		this.faturamentoRecebimentoRelFavorito = faturamentoRecebimentoRelFavorito;
	}

	/**
	 * @return the recebimentoRelFavorito
	 */
	public Boolean getRecebimentoRelFavorito() {
		return recebimentoRelFavorito;
	}

	/**
	 * @param recebimentoRelFavorito the recebimentoRelFavorito to set
	 */
	public void setRecebimentoRelFavorito(Boolean recebimentoRelFavorito) {
		this.recebimentoRelFavorito = recebimentoRelFavorito;
	}

	/**
	 * @return the contaPagarResumidaPorDataRelFavorito
	 */
	public Boolean getContaPagarResumidaPorDataRelFavorito() {
		return contaPagarResumidaPorDataRelFavorito;
	}

	/**
	 * @param contaPagarResumidaPorDataRelFavorito the contaPagarResumidaPorDataRelFavorito to set
	 */
	public void setContaPagarResumidaPorDataRelFavorito(Boolean contaPagarResumidaPorDataRelFavorito) {
		this.contaPagarResumidaPorDataRelFavorito = contaPagarResumidaPorDataRelFavorito;
	}

	/**
	 * @return the contaPagarResumidaPorFornecedorRelFavorito
	 */
	public Boolean getContaPagarResumidaPorFornecedorRelFavorito() {
		return contaPagarResumidaPorFornecedorRelFavorito;
	}

	/**
	 * @param contaPagarResumidaPorFornecedorRelFavorito the contaPagarResumidaPorFornecedorRelFavorito to set
	 */
	public void setContaPagarResumidaPorFornecedorRelFavorito(Boolean contaPagarResumidaPorFornecedorRelFavorito) {
		this.contaPagarResumidaPorFornecedorRelFavorito = contaPagarResumidaPorFornecedorRelFavorito;
	}

	/**
	 * @return the pagamentoRelFavorito
	 */
	public Boolean getPagamentoRelFavorito() {
		return pagamentoRelFavorito;
	}

	/**
	 * @param pagamentoRelFavorito the pagamentoRelFavorito to set
	 */
	public void setPagamentoRelFavorito(Boolean pagamentoRelFavorito) {
		this.pagamentoRelFavorito = pagamentoRelFavorito;
	}

	/**
	 * @return the pagamentoResumidoRelFavorito
	 */
	public Boolean getPagamentoResumidoRelFavorito() {
		return pagamentoResumidoRelFavorito;
	}

	/**
	 * @param pagamentoResumidoRelFavorito the pagamentoResumidoRelFavorito to set
	 */
	public void setPagamentoResumidoRelFavorito(Boolean pagamentoResumidoRelFavorito) {
		this.pagamentoResumidoRelFavorito = pagamentoResumidoRelFavorito;
	}

	/**
	 * @return the historicoTurmaRelFavorito
	 */
	public Boolean getHistoricoTurmaRelFavorito() {
		return historicoTurmaRelFavorito;
	}

	/**
	 * @param historicoTurmaRelFavorito the historicoTurmaRelFavorito to set
	 */
	public void setHistoricoTurmaRelFavorito(Boolean historicoTurmaRelFavorito) {
		this.historicoTurmaRelFavorito = historicoTurmaRelFavorito;
	}

	/**
	 * @return the historicoAlunoRelFavorito
	 */
	public Boolean getHistoricoAlunoRelFavorito() {
		return historicoAlunoRelFavorito;
	}

	/**
	 * @param historicoAlunoRelFavorito the historicoAlunoRelFavorito to set
	 */
	public void setHistoricoAlunoRelFavorito(Boolean historicoAlunoRelFavorito) {
		this.historicoAlunoRelFavorito = historicoAlunoRelFavorito;
	}

	/**
	 * @return the gradeCurricularAlunoRelFavorito
	 */
	public Boolean getGradeCurricularAlunoRelFavorito() {
		return gradeCurricularAlunoRelFavorito;
	}

	/**
	 * @param gradeCurricularAlunoRelFavorito the gradeCurricularAlunoRelFavorito to set
	 */
	public void setGradeCurricularAlunoRelFavorito(Boolean gradeCurricularAlunoRelFavorito) {
		this.gradeCurricularAlunoRelFavorito = gradeCurricularAlunoRelFavorito;
	}

	/**
	 * @return the diarioRelFavorito
	 */
	public Boolean getDiarioRelFavorito() {
		return diarioRelFavorito;
	}

	/**
	 * @param diarioRelFavorito the diarioRelFavorito to set
	 */
	public void setDiarioRelFavorito(Boolean diarioRelFavorito) {
		this.diarioRelFavorito = diarioRelFavorito;
	}

	/**
	 * @return the espelhoRelFavorito
	 */
	public Boolean getEspelhoRelFavorito() {
		return espelhoRelFavorito;
	}

	/**
	 * @param espelhoRelFavorito the espelhoRelFavorito to set
	 */
	public void setEspelhoRelFavorito(Boolean espelhoRelFavorito) {
		this.espelhoRelFavorito = espelhoRelFavorito;
	}

	/**
	 * @return the perfilSocioEconomicoRelFavorito
	 */
	public Boolean getPerfilSocioEconomicoRelFavorito() {
		return perfilSocioEconomicoRelFavorito;
	}

	/**
	 * @param perfilSocioEconomicoRelFavorito the perfilSocioEconomicoRelFavorito to set
	 */
	public void setPerfilSocioEconomicoRelFavorito(Boolean perfilSocioEconomicoRelFavorito) {
		this.perfilSocioEconomicoRelFavorito = perfilSocioEconomicoRelFavorito;
	}

	/**
	 * @return the procSeletivoInscricoesRelFavorito
	 */
	public Boolean getProcSeletivoInscricoesRelFavorito() {
		return procSeletivoInscricoesRelFavorito;
	}

	/**
	 * @param procSeletivoInscricoesRelFavorito the procSeletivoInscricoesRelFavorito to set
	 */
	public void setProcSeletivoInscricoesRelFavorito(Boolean procSeletivoInscricoesRelFavorito) {
		this.procSeletivoInscricoesRelFavorito = procSeletivoInscricoesRelFavorito;
	}

	/**
	 * @return the avaliacaoInstitucionalRelFavorito
	 */
	public Boolean getAvaliacaoInstitucionalRelFavorito() {
		return avaliacaoInstitucionalRelFavorito;
	}

	/**
	 * @param avaliacaoInstitucionalRelFavorito the avaliacaoInstitucionalRelFavorito to set
	 */
	public void setAvaliacaoInstitucionalRelFavorito(Boolean avaliacaoInstitucionalRelFavorito) {
		this.avaliacaoInstitucionalRelFavorito = avaliacaoInstitucionalRelFavorito;
	}

	/**
	 * @return the declaracaoSetranspRelFavorito
	 */
	public Boolean getDeclaracaoSetranspRelFavorito() {
		return declaracaoSetranspRelFavorito;
	}

	/**
	 * @param declaracaoSetranspRelFavorito the declaracaoSetranspRelFavorito to set
	 */
	public void setDeclaracaoSetranspRelFavorito(Boolean declaracaoSetranspRelFavorito) {
		this.declaracaoSetranspRelFavorito = declaracaoSetranspRelFavorito;
	}

	/**
	 * @return the declaracaoPasseEstudantilRelFavorito
	 */
	public Boolean getDeclaracaoPasseEstudantilRelFavorito() {
		return declaracaoPasseEstudantilRelFavorito;
	}

	/**
	 * @param declaracaoPasseEstudantilRelFavorito the declaracaoPasseEstudantilRelFavorito to set
	 */
	public void setDeclaracaoPasseEstudantilRelFavorito(Boolean declaracaoPasseEstudantilRelFavorito) {
		this.declaracaoPasseEstudantilRelFavorito = declaracaoPasseEstudantilRelFavorito;
	}

	/**
	 * @return the comunicadoDebitoDocumentosAlunoRelFavorito
	 */
	public Boolean getComunicadoDebitoDocumentosAlunoRelFavorito() {
		return comunicadoDebitoDocumentosAlunoRelFavorito;
	}

	/**
	 * @param comunicadoDebitoDocumentosAlunoRelFavorito the comunicadoDebitoDocumentosAlunoRelFavorito to set
	 */
	public void setComunicadoDebitoDocumentosAlunoRelFavorito(Boolean comunicadoDebitoDocumentosAlunoRelFavorito) {
		this.comunicadoDebitoDocumentosAlunoRelFavorito = comunicadoDebitoDocumentosAlunoRelFavorito;
	}

	/**
	 * @return the declaracaoFrequenciaRelFavorito
	 */
	public Boolean getDeclaracaoFrequenciaRelFavorito() {
		return declaracaoFrequenciaRelFavorito;
	}

	/**
	 * @param declaracaoFrequenciaRelFavorito the declaracaoFrequenciaRelFavorito to set
	 */
	public void setDeclaracaoFrequenciaRelFavorito(Boolean declaracaoFrequenciaRelFavorito) {
		this.declaracaoFrequenciaRelFavorito = declaracaoFrequenciaRelFavorito;
	}

	/**
	 * @return the declaracaoAbandonoCursoRelFavorito
	 */
	public Boolean getDeclaracaoAbandonoCursoRelFavorito() {
		return declaracaoAbandonoCursoRelFavorito;
	}

	/**
	 * @param declaracaoAbandonoCursoRelFavorito the declaracaoAbandonoCursoRelFavorito to set
	 */
	public void setDeclaracaoAbandonoCursoRelFavorito(Boolean declaracaoAbandonoCursoRelFavorito) {
		this.declaracaoAbandonoCursoRelFavorito = declaracaoAbandonoCursoRelFavorito;
	}

	/**
	 * @return the declaracaoConclusaoCursoRelFavorito
	 */
	public Boolean getDeclaracaoConclusaoCursoRelFavorito() {
		return declaracaoConclusaoCursoRelFavorito;
	}

	/**
	 * @param declaracaoConclusaoCursoRelFavorito the declaracaoConclusaoCursoRelFavorito to set
	 */
	public void setDeclaracaoConclusaoCursoRelFavorito(Boolean declaracaoConclusaoCursoRelFavorito) {
		this.declaracaoConclusaoCursoRelFavorito = declaracaoConclusaoCursoRelFavorito;
	}

	/**
	 * @return the declaracaoCancelamentoMatriculaRelFavorito
	 */
	public Boolean getDeclaracaoCancelamentoMatriculaRelFavorito() {
		return declaracaoCancelamentoMatriculaRelFavorito;
	}

	/**
	 * @param declaracaoCancelamentoMatriculaRelFavorito the declaracaoCancelamentoMatriculaRelFavorito to set
	 */
	public void setDeclaracaoCancelamentoMatriculaRelFavorito(Boolean declaracaoCancelamentoMatriculaRelFavorito) {
		this.declaracaoCancelamentoMatriculaRelFavorito = declaracaoCancelamentoMatriculaRelFavorito;
	}

	/**
	 * @return the declaracaoAprovacaoVestRelFavorito
	 */
	public Boolean getDeclaracaoAprovacaoVestRelFavorito() {
		return declaracaoAprovacaoVestRelFavorito;
	}

	/**
	 * @param declaracaoAprovacaoVestRelFavorito the declaracaoAprovacaoVestRelFavorito to set
	 */
	public void setDeclaracaoAprovacaoVestRelFavorito(Boolean declaracaoAprovacaoVestRelFavorito) {
		this.declaracaoAprovacaoVestRelFavorito = declaracaoAprovacaoVestRelFavorito;
	}

	/**
	 * @return the cartaAlunoRelFavorito
	 */
	public Boolean getCartaAlunoRelFavorito() {
		return cartaAlunoRelFavorito;
	}

	/**
	 * @param cartaAlunoRelFavorito the cartaAlunoRelFavorito to set
	 */
	public void setCartaAlunoRelFavorito(Boolean cartaAlunoRelFavorito) {
		this.cartaAlunoRelFavorito = cartaAlunoRelFavorito;
	}

	/**
	 * @return the alunosCursoRelFavorito
	 */
	public Boolean getAlunosCursoRelFavorito() {
		return alunosCursoRelFavorito;
	}

	/**
	 * @param alunosCursoRelFavorito the alunosCursoRelFavorito to set
	 */
	public void setAlunosCursoRelFavorito(Boolean alunosCursoRelFavorito) {
		this.alunosCursoRelFavorito = alunosCursoRelFavorito;
	}

	/**
	 * @return the alunosPorUnidadeCursoTurmaRelFavorito
	 */
	public Boolean getAlunosPorUnidadeCursoTurmaRelFavorito() {
		return alunosPorUnidadeCursoTurmaRelFavorito;
	}

	/**
	 * @param alunosPorUnidadeCursoTurmaRelFavorito the alunosPorUnidadeCursoTurmaRelFavorito to set
	 */
	public void setAlunosPorUnidadeCursoTurmaRelFavorito(Boolean alunosPorUnidadeCursoTurmaRelFavorito) {
		this.alunosPorUnidadeCursoTurmaRelFavorito = alunosPorUnidadeCursoTurmaRelFavorito;
	}

	/**
	 * @return the reposicaoRelFavorito
	 */
	public Boolean getReposicaoRelFavorito() {
		return reposicaoRelFavorito;
	}

	/**
	 * @param reposicaoRelFavorito the reposicaoRelFavorito to set
	 */
	public void setReposicaoRelFavorito(Boolean reposicaoRelFavorito) {
		this.reposicaoRelFavorito = reposicaoRelFavorito;
	}

	/**
	 * @return the professorRelFavorito
	 */
	public Boolean getProfessorRelFavorito() {
		return professorRelFavorito;
	}

	/**
	 * @param professorRelFavorito the professorRelFavorito to set
	 */
	public void setProfessorRelFavorito(Boolean professorRelFavorito) {
		this.professorRelFavorito = professorRelFavorito;
	}

	/**
	 * @return the alunosMatriculadosGeralRelFavorito
	 */
	public Boolean getAlunosMatriculadosGeralRelFavorito() {
		return alunosMatriculadosGeralRelFavorito;
	}

	/**
	 * @param alunosMatriculadosGeralRelFavorito the alunosMatriculadosGeralRelFavorito to set
	 */
	public void setAlunosMatriculadosGeralRelFavorito(Boolean alunosMatriculadosGeralRelFavorito) {
		this.alunosMatriculadosGeralRelFavorito = alunosMatriculadosGeralRelFavorito;
	}

	/**
	 * @return the alunosNaoRenovaramRelFavorito
	 */
	public Boolean getAlunosNaoRenovaramRelFavorito() {
		return alunosNaoRenovaramRelFavorito;
	}

	/**
	 * @param alunosNaoRenovaramRelFavorito the alunosNaoRenovaramRelFavorito to set
	 */
	public void setAlunosNaoRenovaramRelFavorito(Boolean alunosNaoRenovaramRelFavorito) {
		this.alunosNaoRenovaramRelFavorito = alunosNaoRenovaramRelFavorito;
	}

	/**
	 * @return the processoSeletivoAprovadoReprovadoRelFavorito
	 */
	public Boolean getProcessoSeletivoAprovadoReprovadoRelFavorito() {
		return processoSeletivoAprovadoReprovadoRelFavorito;
	}

	/**
	 * @param processoSeletivoAprovadoReprovadoRelFavorito the processoSeletivoAprovadoReprovadoRelFavorito to set
	 */
	public void setProcessoSeletivoAprovadoReprovadoRelFavorito(Boolean processoSeletivoAprovadoReprovadoRelFavorito) {
		this.processoSeletivoAprovadoReprovadoRelFavorito = processoSeletivoAprovadoReprovadoRelFavorito;
	}

	/**
	 * @return the declaracaoTransferenciaRelFavorito
	 */
	public Boolean getDeclaracaoTransferenciaRelFavorito() {
		return declaracaoTransferenciaRelFavorito;
	}

	/**
	 * @param declaracaoTransferenciaRelFavorito the declaracaoTransferenciaRelFavorito to set
	 */
	public void setDeclaracaoTransferenciaRelFavorito(Boolean declaracaoTransferenciaRelFavorito) {
		this.declaracaoTransferenciaRelFavorito = declaracaoTransferenciaRelFavorito;
	}

	/**
	 * @return the debitoDocumentosPosRelFavorito
	 */
	public Boolean getDebitoDocumentosPosRelFavorito() {
		return debitoDocumentosPosRelFavorito;
	}

	/**
	 * @param debitoDocumentosPosRelFavorito the debitoDocumentosPosRelFavorito to set
	 */
	public void setDebitoDocumentosPosRelFavorito(Boolean debitoDocumentosPosRelFavorito) {
		this.debitoDocumentosPosRelFavorito = debitoDocumentosPosRelFavorito;
	}

	/**
	 * @return the censoFavorito
	 */
	public Boolean getCensoFavorito() {
		return censoFavorito;
	}

	/**
	 * @param censoFavorito the censoFavorito to set
	 */
	public void setCensoFavorito(Boolean censoFavorito) {
		this.censoFavorito = censoFavorito;
	}

	/**
	 * @return the recebimentoPorTurmaRelFavorito
	 */
	public Boolean getRecebimentoPorTurmaRelFavorito() {
		return recebimentoPorTurmaRelFavorito;
	}

	/**
	 * @param recebimentoPorTurmaRelFavorito the recebimentoPorTurmaRelFavorito to set
	 */
	public void setRecebimentoPorTurmaRelFavorito(Boolean recebimentoPorTurmaRelFavorito) {
		this.recebimentoPorTurmaRelFavorito = recebimentoPorTurmaRelFavorito;
	}

	/**
	 * @return the prestacaoContaRelFavorito
	 */
	public Boolean getPrestacaoContaRelFavorito() {
		return prestacaoContaRelFavorito;
	}

	/**
	 * @param prestacaoContaRelFavorito the prestacaoContaRelFavorito to set
	 */
	public void setPrestacaoContaRelFavorito(Boolean prestacaoContaRelFavorito) {
		this.prestacaoContaRelFavorito = prestacaoContaRelFavorito;
	}

	/**
	 * @return the controleDocumentacaoAlunoRelFavorito
	 */
	public Boolean getControleDocumentacaoAlunoRelFavorito() {
		return controleDocumentacaoAlunoRelFavorito;
	}

	/**
	 * @param controleDocumentacaoAlunoRelFavorito the controleDocumentacaoAlunoRelFavorito to set
	 */
	public void setControleDocumentacaoAlunoRelFavorito(Boolean controleDocumentacaoAlunoRelFavorito) {
		this.controleDocumentacaoAlunoRelFavorito = controleDocumentacaoAlunoRelFavorito;
	}

	/**
	 * @return the contaReceberAlunosRelFavorito
	 */
	public Boolean getContaReceberAlunosRelFavorito() {
		return contaReceberAlunosRelFavorito;
	}

	/**
	 * @param contaReceberAlunosRelFavorito the contaReceberAlunosRelFavorito to set
	 */
	public void setContaReceberAlunosRelFavorito(Boolean contaReceberAlunosRelFavorito) {
		this.contaReceberAlunosRelFavorito = contaReceberAlunosRelFavorito;
	}

	/**
	 * @return the situacaoFinanceiraAlunoRelFavorito
	 */
	public Boolean getSituacaoFinanceiraAlunoRelFavorito() {
		return situacaoFinanceiraAlunoRelFavorito;
	}

	/**
	 * @param situacaoFinanceiraAlunoRelFavorito the situacaoFinanceiraAlunoRelFavorito to set
	 */
	public void setSituacaoFinanceiraAlunoRelFavorito(Boolean situacaoFinanceiraAlunoRelFavorito) {
		this.situacaoFinanceiraAlunoRelFavorito = situacaoFinanceiraAlunoRelFavorito;
	}

	/**
	 * @return the mediaDescontoAlunoRelFavorito
	 */
	public Boolean getMediaDescontoAlunoRelFavorito() {
		return mediaDescontoAlunoRelFavorito;
	}

	/**
	 * @param mediaDescontoAlunoRelFavorito the mediaDescontoAlunoRelFavorito to set
	 */
	public void setMediaDescontoAlunoRelFavorito(Boolean mediaDescontoAlunoRelFavorito) {
		this.mediaDescontoAlunoRelFavorito = mediaDescontoAlunoRelFavorito;
	}

	/**
	 * @return the mediaDescontoTurmaRelFavorito
	 */
	public Boolean getMediaDescontoTurmaRelFavorito() {
		return mediaDescontoTurmaRelFavorito;
	}

	/**
	 * @param mediaDescontoTurmaRelFavorito the mediaDescontoTurmaRelFavorito to set
	 */
	public void setMediaDescontoTurmaRelFavorito(Boolean mediaDescontoTurmaRelFavorito) {
		this.mediaDescontoTurmaRelFavorito = mediaDescontoTurmaRelFavorito;
	}

	/**
	 * @return the balanceteRelFavorito
	 */
	public Boolean getBalanceteRelFavorito() {
		return balanceteRelFavorito;
	}

	/**
	 * @param balanceteRelFavorito the balanceteRelFavorito to set
	 */
	public void setBalanceteRelFavorito(Boolean balanceteRelFavorito) {
		this.balanceteRelFavorito = balanceteRelFavorito;
	}

	/**
	 * @return the chequesRelFavorito
	 */
	public Boolean getChequesRelFavorito() {
		return chequesRelFavorito;
	}

	/**
	 * @param chequesRelFavorito the chequesRelFavorito to set
	 */
	public void setChequesRelFavorito(Boolean chequesRelFavorito) {
		this.chequesRelFavorito = chequesRelFavorito;
	}

	/**
	 * @return the registroArquivoFavorito
	 */
	public Boolean getRegistroArquivoFavorito() {
		return registroArquivoFavorito;
	}

	/**
	 * @param registroArquivoFavorito the registroArquivoFavorito to set
	 */
	public void setRegistroArquivoFavorito(Boolean registroArquivoFavorito) {
		this.registroArquivoFavorito = registroArquivoFavorito;
	}

	/**
	 * @return the setranspFavorito
	 */
	public Boolean getSetranspFavorito() {
		return setranspFavorito;
	}

	/**
	 * @param setranspFavorito the setranspFavorito to set
	 */
	public void setSetranspFavorito(Boolean setranspFavorito) {
		this.setranspFavorito = setranspFavorito;
	}

	/**
	 * @return the inadimplenciaRelFavorito
	 */
	public Boolean getInadimplenciaRelFavorito() {
		return inadimplenciaRelFavorito;
	}

	/**
	 * @param inadimplenciaRelFavorito the inadimplenciaRelFavorito to set
	 */
	public void setInadimplenciaRelFavorito(Boolean inadimplenciaRelFavorito) {
		this.inadimplenciaRelFavorito = inadimplenciaRelFavorito;
	}

	/**
	 * @return the mapaFinanceiroAlunoRelFavorito
	 */
	public Boolean getMapaFinanceiroAlunoRelFavorito() {
		return mapaFinanceiroAlunoRelFavorito;
	}

	/**
	 * @param mapaFinanceiroAlunoRelFavorito the mapaFinanceiroAlunoRelFavorito to set
	 */
	public void setMapaFinanceiroAlunoRelFavorito(Boolean mapaFinanceiroAlunoRelFavorito) {
		this.mapaFinanceiroAlunoRelFavorito = mapaFinanceiroAlunoRelFavorito;
	}

	/**
	 * @return the boletimAcademicoRelFavorito
	 */
	public Boolean getBoletimAcademicoRelFavorito() {
		return boletimAcademicoRelFavorito;
	}

	/**
	 * @param boletimAcademicoRelFavorito the boletimAcademicoRelFavorito to set
	 */
	public void setBoletimAcademicoRelFavorito(Boolean boletimAcademicoRelFavorito) {
		this.boletimAcademicoRelFavorito = boletimAcademicoRelFavorito;
	}

	/**
	 * @return the quadroMatriculaRelFavorito
	 */
	public Boolean getQuadroMatriculaRelFavorito() {
		return quadroMatriculaRelFavorito;
	}

	/**
	 * @param quadroMatriculaRelFavorito the quadroMatriculaRelFavorito to set
	 */
	public void setQuadroMatriculaRelFavorito(Boolean quadroMatriculaRelFavorito) {
		this.quadroMatriculaRelFavorito = quadroMatriculaRelFavorito;
	}

	/**
	 * @return the ocorrenciasAlunosRelFavorito
	 */
	public Boolean getOcorrenciasAlunosRelFavorito() {
		return ocorrenciasAlunosRelFavorito;
	}

	/**
	 * @param ocorrenciasAlunosRelFavorito the ocorrenciasAlunosRelFavorito to set
	 */
	public void setOcorrenciasAlunosRelFavorito(Boolean ocorrenciasAlunosRelFavorito) {
		this.ocorrenciasAlunosRelFavorito = ocorrenciasAlunosRelFavorito;
	}

	/**
	 * @return the estatisticaMatriculaRelFavorito
	 */
	public Boolean getEstatisticaMatriculaRelFavorito() {
		return estatisticaMatriculaRelFavorito;
	}

	/**
	 * @param estatisticaMatriculaRelFavorito the estatisticaMatriculaRelFavorito to set
	 */
	public void setEstatisticaMatriculaRelFavorito(Boolean estatisticaMatriculaRelFavorito) {
		this.estatisticaMatriculaRelFavorito = estatisticaMatriculaRelFavorito;
	}

	/**
	 * @return the quadroAlunosAtivosInativosRelFavorito
	 */
	public Boolean getQuadroAlunosAtivosInativosRelFavorito() {
		return quadroAlunosAtivosInativosRelFavorito;
	}

	/**
	 * @param quadroAlunosAtivosInativosRelFavorito the quadroAlunosAtivosInativosRelFavorito to set
	 */
	public void setQuadroAlunosAtivosInativosRelFavorito(Boolean quadroAlunosAtivosInativosRelFavorito) {
		this.quadroAlunosAtivosInativosRelFavorito = quadroAlunosAtivosInativosRelFavorito;
	}

	/**
	 * @return the senhaAlunoProfessorRelFavorito
	 */
	public Boolean getSenhaAlunoProfessorRelFavorito() {
		return senhaAlunoProfessorRelFavorito;
	}

	/**
	 * @param senhaAlunoProfessorRelFavorito the senhaAlunoProfessorRelFavorito to set
	 */
	public void setSenhaAlunoProfessorRelFavorito(Boolean senhaAlunoProfessorRelFavorito) {
		this.senhaAlunoProfessorRelFavorito = senhaAlunoProfessorRelFavorito;
	}

	/**
	 * @return the alunoNaoCursouDisciplinaRelFavorito
	 */
	public Boolean getAlunoNaoCursouDisciplinaRelFavorito() {
		return alunoNaoCursouDisciplinaRelFavorito;
	}

	/**
	 * @param alunoNaoCursouDisciplinaRelFavorito the alunoNaoCursouDisciplinaRelFavorito to set
	 */
	public void setAlunoNaoCursouDisciplinaRelFavorito(Boolean alunoNaoCursouDisciplinaRelFavorito) {
		this.alunoNaoCursouDisciplinaRelFavorito = alunoNaoCursouDisciplinaRelFavorito;
	}

	/**
	 * @return the certificadoCursoExtensaoRelFavorito
	 */
	public Boolean getCertificadoCursoExtensaoRelFavorito() {
		return certificadoCursoExtensaoRelFavorito;
	}

	/**
	 * @param certificadoCursoExtensaoRelFavorito the certificadoCursoExtensaoRelFavorito to set
	 */
	public void setCertificadoCursoExtensaoRelFavorito(Boolean certificadoCursoExtensaoRelFavorito) {
		this.certificadoCursoExtensaoRelFavorito = certificadoCursoExtensaoRelFavorito;
	}

	/**
	 * @return the disciplinasGradeRelFavorito
	 */
	public Boolean getDisciplinasGradeRelFavorito() {
		return disciplinasGradeRelFavorito;
	}

	/**
	 * @param disciplinasGradeRelFavorito the disciplinasGradeRelFavorito to set
	 */
	public void setDisciplinasGradeRelFavorito(Boolean disciplinasGradeRelFavorito) {
		this.disciplinasGradeRelFavorito = disciplinasGradeRelFavorito;
	}

	/**
	 * @return the termoCompromissoDocumentacaoPendenteRelFavorito
	 */
	public Boolean getTermoCompromissoDocumentacaoPendenteRelFavorito() {
		return termoCompromissoDocumentacaoPendenteRelFavorito;
	}

	/**
	 * @param termoCompromissoDocumentacaoPendenteRelFavorito the termoCompromissoDocumentacaoPendenteRelFavorito to set
	 */
	public void setTermoCompromissoDocumentacaoPendenteRelFavorito(Boolean termoCompromissoDocumentacaoPendenteRelFavorito) {
		this.termoCompromissoDocumentacaoPendenteRelFavorito = termoCompromissoDocumentacaoPendenteRelFavorito;
	}

	/**
	 * @return the entregaBoletosRelFavorito
	 */
	public Boolean getEntregaBoletosRelFavorito() {
		return entregaBoletosRelFavorito;
	}

	/**
	 * @param entregaBoletosRelFavorito the entregaBoletosRelFavorito to set
	 */
	public void setEntregaBoletosRelFavorito(Boolean entregaBoletosRelFavorito) {
		this.entregaBoletosRelFavorito = entregaBoletosRelFavorito;
	}

	/**
	 * @return the alunoDescontoDesempenhoRelFavorito
	 */
	public Boolean getAlunoDescontoDesempenhoRelFavorito() {
		return alunoDescontoDesempenhoRelFavorito;
	}

	/**
	 * @param alunoDescontoDesempenhoRelFavorito the alunoDescontoDesempenhoRelFavorito to set
	 */
	public void setAlunoDescontoDesempenhoRelFavorito(Boolean alunoDescontoDesempenhoRelFavorito) {
		this.alunoDescontoDesempenhoRelFavorito = alunoDescontoDesempenhoRelFavorito;
	}

	/**
	 * @return the relacaoEnderecoAlunoRelFavorito
	 */
	public Boolean getRelacaoEnderecoAlunoRelFavorito() {
		return relacaoEnderecoAlunoRelFavorito;
	}

	/**
	 * @param relacaoEnderecoAlunoRelFavorito the relacaoEnderecoAlunoRelFavorito to set
	 */
	public void setRelacaoEnderecoAlunoRelFavorito(Boolean relacaoEnderecoAlunoRelFavorito) {
		this.relacaoEnderecoAlunoRelFavorito = relacaoEnderecoAlunoRelFavorito;
	}

	/**
	 * @return alunoBaixaFrequenciaRel
	 */
	public Boolean getAlunoBaixaFrequenciaRel() {
		return alunoBaixaFrequenciaRel;
	}

	/**
	 * @param alunoBaixaFrequenciaRel
	 */
	public void setAlunoBaixaFrequenciaRel(Boolean alunoBaixaFrequenciaRel) {
		this.alunoBaixaFrequenciaRel = alunoBaixaFrequenciaRel;
	}

	public Boolean getAlunoBaixaFrequenciaRelFavorito() {
		return alunoBaixaFrequenciaRelFavorito;
	}

	public void setAlunoBaixaFrequenciaRelFavorito(Boolean alunoBaixaFrequenciaRelFavorito) {
		this.alunoBaixaFrequenciaRelFavorito = alunoBaixaFrequenciaRelFavorito;
	}

	/**
	 * @return the livroRegistroRelFavorito
	 */
	public Boolean getLivroRegistroRelFavorito() {
		return livroRegistroRelFavorito;
	}

	/**
	 * @param livroRegistroRelFavorito the livroRegistroRelFavorito to set
	 */
	public void setLivroRegistroRelFavorito(Boolean livroRegistroRelFavorito) {
		this.livroRegistroRelFavorito = livroRegistroRelFavorito;
	}

	/**
	 * @return the fichaAtualizacaoCadastralRelFavorito
	 */
	public Boolean getFichaAtualizacaoCadastralRelFavorito() {
		return fichaAtualizacaoCadastralRelFavorito;
	}

	/**
	 * @param fichaAtualizacaoCadastralRelFavorito the fichaAtualizacaoCadastralRelFavorito to set
	 */
	public void setFichaAtualizacaoCadastralRelFavorito(Boolean fichaAtualizacaoCadastralRelFavorito) {
		this.fichaAtualizacaoCadastralRelFavorito = fichaAtualizacaoCadastralRelFavorito;
	}

	/**
	 * @return the alunosMatriculadosPorProcessoSeletivoRelFavorito
	 */
	public Boolean getAlunosMatriculadosPorProcessoSeletivoRelFavorito() {
		return alunosMatriculadosPorProcessoSeletivoRelFavorito;
	}

	/**
	 * @param alunosMatriculadosPorProcessoSeletivoRelFavorito the alunosMatriculadosPorProcessoSeletivoRelFavorito to set
	 */
	public void setAlunosMatriculadosPorProcessoSeletivoRelFavorito(Boolean alunosMatriculadosPorProcessoSeletivoRelFavorito) {
		this.alunosMatriculadosPorProcessoSeletivoRelFavorito = alunosMatriculadosPorProcessoSeletivoRelFavorito;
	}

	/**
	 * @return the alunosProcessoSeletivoRelFavorito
	 */
	public Boolean getAlunosProcessoSeletivoRelFavorito() {
		return alunosProcessoSeletivoRelFavorito;
	}

	/**
	 * @param alunosProcessoSeletivoRelFavorito the alunosProcessoSeletivoRelFavorito to set
	 */
	public void setAlunosProcessoSeletivoRelFavorito(Boolean alunosProcessoSeletivoRelFavorito) {
		this.alunosProcessoSeletivoRelFavorito = alunosProcessoSeletivoRelFavorito;
	}

	/**
	 * @return the meritoAcademicoRelFavorito
	 */
	public Boolean getMeritoAcademicoRelFavorito() {
		return meritoAcademicoRelFavorito;
	}

	/**
	 * @param meritoAcademicoRelFavorito the meritoAcademicoRelFavorito to set
	 */
	public void setMeritoAcademicoRelFavorito(Boolean meritoAcademicoRelFavorito) {
		this.meritoAcademicoRelFavorito = meritoAcademicoRelFavorito;
	}

	/**
	 * @return the possiveisFormandosRelFavorito
	 */
	public Boolean getPossiveisFormandosRelFavorito() {
		return possiveisFormandosRelFavorito;
	}

	/**
	 * @param possiveisFormandosRelFavorito the possiveisFormandosRelFavorito to set
	 */
	public void setPossiveisFormandosRelFavorito(Boolean possiveisFormandosRelFavorito) {
		this.possiveisFormandosRelFavorito = possiveisFormandosRelFavorito;
	}

	/**
	 * @param possiveisFormandosRelFavorito the possiveisFormandosRelFavorito to set
	 */
	public void setControleVagaRelFavorito(Boolean controleVagaRelFavorito) {
		this.controleVagaRelFavorito = controleVagaRelFavorito;
	}

	/**
	 * @return the possiveisFormandosRelFavorito
	 */
	public Boolean getControleVagaRelFavorito() {
		return controleVagaRelFavorito;
	}

	/**
	 * @return the perfilTurmaRelFavorito
	 */
	public Boolean getPerfilTurmaRelFavorito() {
		return perfilTurmaRelFavorito;
	}

	/**
	 * @param perfilTurmaRelFavorito the perfilTurmaRelFavorito to set
	 */
	public void setPerfilTurmaRelFavorito(Boolean perfilTurmaRelFavorito) {
		this.perfilTurmaRelFavorito = perfilTurmaRelFavorito;
	}

	/**
	 * @return the emailTurmaRelFavorito
	 */
	public Boolean getEmailTurmaRelFavorito() {
		return emailTurmaRelFavorito;
	}

	/**
	 * @param emailTurmaRelFavorito the emailTurmaRelFavorito to set
	 */
	public void setEmailTurmaRelFavorito(Boolean emailTurmaRelFavorito) {
		this.emailTurmaRelFavorito = emailTurmaRelFavorito;
	}

	/**
	 * @return the alunosProUniRelFavorito
	 */
	public Boolean getAlunosProUniRelFavorito() {
		return alunosProUniRelFavorito;
	}

	/**
	 * @param alunosProUniRelFavorito the alunosProUniRelFavorito to set
	 */
	public void setAlunosProUniRelFavorito(Boolean alunosProUniRelFavorito) {
		this.alunosProUniRelFavorito = alunosProUniRelFavorito;
	}

	/**
	 * @return the condicoesPagamentoRelFavorito
	 */
	public Boolean getCondicoesPagamentoRelFavorito() {
		return condicoesPagamentoRelFavorito;
	}

	/**
	 * @param condicoesPagamentoRelFavorito the condicoesPagamentoRelFavorito to set
	 */
	public void setCondicoesPagamentoRelFavorito(Boolean condicoesPagamentoRelFavorito) {
		this.condicoesPagamentoRelFavorito = condicoesPagamentoRelFavorito;
	}

	/**
	 * @return the listagemDescontosAlunosRelFavorito
	 */
	public Boolean getListagemDescontosAlunosRelFavorito() {
		return listagemDescontosAlunosRelFavorito;
	}

	/**
	 * @param listagemDescontosAlunosRelFavorito the listagemDescontosAlunosRelFavorito to set
	 */
	public void setListagemDescontosAlunosRelFavorito(Boolean listagemDescontosAlunosRelFavorito) {
		this.listagemDescontosAlunosRelFavorito = listagemDescontosAlunosRelFavorito;
	}

	/**
	 * @return the AlunosReprovadosRelFavorito
	 */
	public Boolean getAlunosReprovadosRelFavorito() {
		return AlunosReprovadosRelFavorito;
	}

	/**
	 * @param AlunosReprovadosRelFavorito the AlunosReprovadosRelFavorito to set
	 */
	public void setAlunosReprovadosRelFavorito(Boolean AlunosReprovadosRelFavorito) {
		this.AlunosReprovadosRelFavorito = AlunosReprovadosRelFavorito;
	}

	/**
	 * @return the MapaSituacaoAlunoRelFavorito
	 */
	public Boolean getMapaSituacaoAlunoRelFavorito() {
		return MapaSituacaoAlunoRelFavorito;
	}

	/**
	 * @param MapaSituacaoAlunoRelFavorito the MapaSituacaoAlunoRelFavorito to set
	 */
	public void setMapaSituacaoAlunoRelFavorito(Boolean MapaSituacaoAlunoRelFavorito) {
		this.MapaSituacaoAlunoRelFavorito = MapaSituacaoAlunoRelFavorito;
	}

	/**
	 * @return the exemplaresRelFavorito
	 */
	public Boolean getExemplaresRelFavorito() {
		return exemplaresRelFavorito;
	}

	/**
	 * @param exemplaresRelFavorito the exemplaresRelFavorito to set
	 */
	public void setExemplaresRelFavorito(Boolean exemplaresRelFavorito) {
		this.exemplaresRelFavorito = exemplaresRelFavorito;
	}

	/**
	 * @return the situacaoExemplaresRelFavorito
	 */
	public Boolean getSituacaoExemplaresRelFavorito() {
		return situacaoExemplaresRelFavorito;
	}

	/**
	 * @param situacaoExemplaresRelFavorito the situacaoExemplaresRelFavorito to set
	 */
	public void setSituacaoExemplaresRelFavorito(Boolean situacaoExemplaresRelFavorito) {
		this.situacaoExemplaresRelFavorito = situacaoExemplaresRelFavorito;
	}

	/**
	 * @return the horarioDaTurmaRelFavorito
	 */
	public Boolean getHorarioDaTurmaRelFavorito() {
		return horarioDaTurmaRelFavorito;
	}

	/**
	 * @param horarioDaTurmaRelFavorito the horarioDaTurmaRelFavorito to set
	 */
	public void setHorarioDaTurmaRelFavorito(Boolean horarioDaTurmaRelFavorito) {
		this.horarioDaTurmaRelFavorito = horarioDaTurmaRelFavorito;
	}

	/**
	 * @return the faltasAlunosRelFavorito
	 */
	public Boolean getFaltasAlunosRelFavorito() {
		return faltasAlunosRelFavorito;
	}

	/**
	 * @param faltasAlunosRelFavorito the faltasAlunosRelFavorito to set
	 */
	public void setFaltasAlunosRelFavorito(Boolean faltasAlunosRelFavorito) {
		this.faltasAlunosRelFavorito = faltasAlunosRelFavorito;
	}

	/**
	 * @return the emprestimoRelFavorito
	 */
	public Boolean getEmprestimoRelFavorito() {
		return emprestimoRelFavorito;
	}

	/**
	 * @param emprestimoRelFavorito the emprestimoRelFavorito to set
	 */
	public void setEmprestimoRelFavorito(Boolean emprestimoRelFavorito) {
		this.emprestimoRelFavorito = emprestimoRelFavorito;
	}

	/**
	 * @return the agendaProfessorRelFavorito
	 */
	public Boolean getAgendaProfessorRelFavorito() {
		return agendaProfessorRelFavorito;
	}

	/**
	 * @param agendaProfessorRelFavorito the agendaProfessorRelFavorito to set
	 */
	public void setAgendaProfessorRelFavorito(Boolean agendaProfessorRelFavorito) {
		this.agendaProfessorRelFavorito = agendaProfessorRelFavorito;
	}

	/**
	 * @return the aniversariantesDoMesRelFavorito
	 */
	public Boolean getAniversariantesDoMesRelFavorito() {
		return aniversariantesDoMesRelFavorito;
	}

	/**
	 * @param aniversariantesDoMesRelFavorito the aniversariantesDoMesRelFavorito to set
	 */
	public void setAniversariantesDoMesRelFavorito(Boolean aniversariantesDoMesRelFavorito) {
		this.aniversariantesDoMesRelFavorito = aniversariantesDoMesRelFavorito;
	}

	/**
	 * @return the cronogramaDeAulasRelFavorito
	 */
	public Boolean getCronogramaDeAulasRelFavorito() {
		return cronogramaDeAulasRelFavorito;
	}

	/**
	 * @param cronogramaDeAulasRelFavorito the cronogramaDeAulasRelFavorito to set
	 */
	public void setCronogramaDeAulasRelFavorito(Boolean cronogramaDeAulasRelFavorito) {
		this.cronogramaDeAulasRelFavorito = cronogramaDeAulasRelFavorito;
	}

	/**
	 * @return the previsaoFaturamentoRelFavorito
	 */
	public Boolean getPrevisaoFaturamentoRelFavorito() {
		return previsaoFaturamentoRelFavorito;
	}

	/**
	 * @param previsaoFaturamentoRelFavorito the previsaoFaturamentoRelFavorito to set
	 */
	public void setPrevisaoFaturamentoRelFavorito(Boolean previsaoFaturamentoRelFavorito) {
		this.previsaoFaturamentoRelFavorito = previsaoFaturamentoRelFavorito;
	}

	/**
	 * @return the quadroComissaoConsultoresRelFavorito
	 */
	public Boolean getQuadroComissaoConsultoresRelFavorito() {
		return quadroComissaoConsultoresRelFavorito;
	}

	/**
	 * @param quadroComissaoConsultoresRelFavorito the quadroComissaoConsultoresRelFavorito to set
	 */
	public void setQuadroComissaoConsultoresRelFavorito(Boolean quadroComissaoConsultoresRelFavorito) {
		this.quadroComissaoConsultoresRelFavorito = quadroComissaoConsultoresRelFavorito;
	}

	/**
	 * @return the envelopeRelFavorito
	 */
	public Boolean getEnvelopeRelFavorito() {
		return envelopeRelFavorito;
	}

	/**
	 * @param envelopeRelFavorito the envelopeRelFavorito to set
	 */
	public void setEnvelopeRelFavorito(Boolean envelopeRelFavorito) {
		this.envelopeRelFavorito = envelopeRelFavorito;
	}

	/**
	 * @return the envelopeRequerimentoRelFavorito
	 */
	public Boolean getEnvelopeRequerimentoRelFavorito() {
		return envelopeRequerimentoRelFavorito;
	}

	/**
	 * @param envelopeRequerimentoRelFavorito the envelopeRequerimentoRelFavorito to set
	 */
	public void setEnvelopeRequerimentoRelFavorito(Boolean envelopeRequerimentoRelFavorito) {
		this.envelopeRequerimentoRelFavorito = envelopeRequerimentoRelFavorito;
	}

	/**
	 * @return the aberturaTurmaRelFavorito
	 */
	public Boolean getAberturaTurmaRelFavorito() {
		return aberturaTurmaRelFavorito;
	}

	/**
	 * @param aberturaTurmaRelFavorito the aberturaTurmaRelFavorito to set
	 */
	public void setAberturaTurmaRelFavorito(Boolean aberturaTurmaRelFavorito) {
		this.aberturaTurmaRelFavorito = aberturaTurmaRelFavorito;
	}

	/**
	 * @return the mapaAberturaTurmaFavorito
	 */
	public Boolean getMapaAberturaTurmaFavorito() {
		return mapaAberturaTurmaFavorito;
	}

	/**
	 * @param mapaAberturaTurmaFavorito the mapaAberturaTurmaFavorito to set
	 */
	public void setMapaAberturaTurmaFavorito(Boolean mapaAberturaTurmaFavorito) {
		this.mapaAberturaTurmaFavorito = mapaAberturaTurmaFavorito;
	}

	/**
	 * @return the extratoContaPagarRelFavorito
	 */
	public Boolean getExtratoContaPagarRelFavorito() {
		return extratoContaPagarRelFavorito;
	}

	/**
	 * @param extratoContaPagarRelFavorito the extratoContaPagarRelFavorito to set
	 */
	public void setExtratoContaPagarRelFavorito(Boolean extratoContaPagarRelFavorito) {
		this.extratoContaPagarRelFavorito = extratoContaPagarRelFavorito;
	}

	/**
	 * @return the requerimentoRelFavorito
	 */
	public Boolean getRequerimentoRelFavorito() {
		return requerimentoRelFavorito;
	}

	/**
	 * @param requerimentoRelFavorito the requerimentoRelFavorito to set
	 */
	public void setRequerimentoRelFavorito(Boolean requerimentoRelFavorito) {
		this.requerimentoRelFavorito = requerimentoRelFavorito;
	}

	/**
	 * @return
	 */
	public Boolean getRequerimentoTCCRel() {
		return requerimentoTCCRel;
	}

	/**
	 * @param requerimentoTCCRel
	 */
	public void setRequerimentoTCCRel(Boolean requerimentoTCCRel) {
		this.requerimentoTCCRel = requerimentoTCCRel;
	}

	public Boolean getRequerimentoTCCRelFavorito() {
		return requerimentoTCCRelFavorito;
	}

	public void setRequerimentoTCCRelFavorito(Boolean requerimentoTCCRelFavorito) {
		this.requerimentoTCCRelFavorito = requerimentoTCCRelFavorito;
	}

	/**
	 * @return the candidatosParaVagaRelFavorito
	 */
	public Boolean getCandidatosParaVagaRelFavorito() {
		return candidatosParaVagaRelFavorito;
	}

	/**
	 * @param candidatosParaVagaRelFavorito the candidatosParaVagaRelFavorito to set
	 */
	public void setCandidatosParaVagaRelFavorito(Boolean candidatosParaVagaRelFavorito) {
		this.candidatosParaVagaRelFavorito = candidatosParaVagaRelFavorito;
	}

	/**
	 * @return the empresaVagaBancoTalentoRelFavorito
	 */
	public Boolean getEmpresaVagaBancoTalentoRelFavorito() {
		return empresaVagaBancoTalentoRelFavorito;
	}

	/**
	 * @param empresaVagaBancoTalentoRelFavorito the empresaVagaBancoTalentoRelFavorito to set
	 */
	public void setEmpresaVagaBancoTalentoRelFavorito(Boolean empresaVagaBancoTalentoRelFavorito) {
		this.empresaVagaBancoTalentoRelFavorito = empresaVagaBancoTalentoRelFavorito;
	}

	/**
	 * @return the empresaPorVagasRelFavorito
	 */
	public Boolean getEmpresaPorVagasRelFavorito() {
		return empresaPorVagasRelFavorito;
	}

	/**
	 * @param empresaPorVagasRelFavorito the empresaPorVagasRelFavorito to set
	 */
	public void setEmpresaPorVagasRelFavorito(Boolean empresaPorVagasRelFavorito) {
		this.empresaPorVagasRelFavorito = empresaPorVagasRelFavorito;
	}

	/**
	 * @return the empresasRelFavorito
	 */
	public Boolean getEmpresasRelFavorito() {
		return empresasRelFavorito;
	}

	/**
	 * @param empresasRelFavorito the empresasRelFavorito to set
	 */
	public void setEmpresasRelFavorito(Boolean empresasRelFavorito) {
		this.empresasRelFavorito = empresasRelFavorito;
	}

	/**
	 * @return the alunosCandidatadosVagaRelFavorito
	 */
	public Boolean getAlunosCandidatadosVagaRelFavorito() {
		return alunosCandidatadosVagaRelFavorito;
	}

	/**
	 * @param alunosCandidatadosVagaRelFavorito the alunosCandidatadosVagaRelFavorito to set
	 */
	public void setAlunosCandidatadosVagaRelFavorito(Boolean alunosCandidatadosVagaRelFavorito) {
		this.alunosCandidatadosVagaRelFavorito = alunosCandidatadosVagaRelFavorito;
	}

	/**
	 * @return the etiquetaProvaRelFavorito
	 */
	public Boolean getEtiquetaProvaRelFavorito() {
		return etiquetaProvaRelFavorito;
	}

	/**
	 * @param etiquetaProvaRelFavorito the etiquetaProvaRelFavorito to set
	 */
	public void setEtiquetaProvaRelFavorito(Boolean etiquetaProvaRelFavorito) {
		this.etiquetaProvaRelFavorito = etiquetaProvaRelFavorito;
	}

	/**
	 * @return the reciboComissoesRelFavorito
	 */
	public Boolean getReciboComissoesRelFavorito() {
		return reciboComissoesRelFavorito;
	}

	/**
	 * @param reciboComissoesRelFavorito the reciboComissoesRelFavorito to set
	 */
	public void setReciboComissoesRelFavorito(Boolean reciboComissoesRelFavorito) {
		this.reciboComissoesRelFavorito = reciboComissoesRelFavorito;
	}

	/**
	 * @return the areaProfissionalFavorito
	 */
	public Boolean getAreaProfissionalFavorito() {
		return areaProfissionalFavorito;
	}

	/**
	 * @param areaProfissionalFavorito the areaProfissionalFavorito to set
	 */
	public void setAreaProfissionalFavorito(Boolean areaProfissionalFavorito) {
		this.areaProfissionalFavorito = areaProfissionalFavorito;
	}

	/**
	 * @return the vagasFavorito
	 */
	public Boolean getVagasFavorito() {
		return vagasFavorito;
	}

	/**
	 * @param vagasFavorito the vagasFavorito to set
	 */
	public void setVagasFavorito(Boolean vagasFavorito) {
		this.vagasFavorito = vagasFavorito;
	}

	/**
	 * @return the painelAlunoFavorito
	 */
	public Boolean getPainelAlunoFavorito() {
		return painelAlunoFavorito;
	}

	/**
	 * @param painelAlunoFavorito the painelAlunoFavorito to set
	 */
	public void setPainelAlunoFavorito(Boolean painelAlunoFavorito) {
		this.painelAlunoFavorito = painelAlunoFavorito;
	}

	/**
	 * @return the linkFavorito
	 */
	public Boolean getLinkFavorito() {
		return linkFavorito;
	}

	/**
	 * @param linkFavorito the linkFavorito to set
	 */
	public void setLinkFavorito(Boolean linkFavorito) {
		this.linkFavorito = linkFavorito;
	}

	/**
	 * @return the textoPadraoBancoCurriculumFavorito
	 */
	public Boolean getTextoPadraoBancoCurriculumFavorito() {
		return textoPadraoBancoCurriculumFavorito;
	}

	/**
	 * @param textoPadraoBancoCurriculumFavorito the textoPadraoBancoCurriculumFavorito to set
	 */
	public void setTextoPadraoBancoCurriculumFavorito(Boolean textoPadraoBancoCurriculumFavorito) {
		this.textoPadraoBancoCurriculumFavorito = textoPadraoBancoCurriculumFavorito;
	}

	/**
	 * @return the painelGestorBancoCurriculoFavorito
	 */
	public Boolean getPainelGestorBancoCurriculoFavorito() {
		return painelGestorBancoCurriculoFavorito;
	}

	/**
	 * @param painelGestorBancoCurriculoFavorito the painelGestorBancoCurriculoFavorito to set
	 */
	public void setPainelGestorBancoCurriculoFavorito(Boolean painelGestorBancoCurriculoFavorito) {
		this.painelGestorBancoCurriculoFavorito = painelGestorBancoCurriculoFavorito;
	}

	/**
	 * @return the contatoAniversarianteFavorito
	 */
	public Boolean getContatoAniversarianteFavorito() {
		return contatoAniversarianteFavorito;
	}

	/**
	 * @param contatoAniversarianteFavorito the contatoAniversarianteFavorito to set
	 */
	public void setContatoAniversarianteFavorito(Boolean contatoAniversarianteFavorito) {
		this.contatoAniversarianteFavorito = contatoAniversarianteFavorito;
	}

	/**
	 * @return the configuracoesVisaoProfessorFavorito
	 */
	public Boolean getConfiguracoesVisaoProfessorFavorito() {
		return configuracoesVisaoProfessorFavorito;
	}

	/**
	 * @param configuracoesVisaoProfessorFavorito the configuracoesVisaoProfessorFavorito to set
	 */
	public void setConfiguracoesVisaoProfessorFavorito(Boolean configuracoesVisaoProfessorFavorito) {
		this.configuracoesVisaoProfessorFavorito = configuracoesVisaoProfessorFavorito;
	}

	/**
	 * @return the configuracoesLiberarAlteracaoSenhaProfessorFavorito
	 */
	public Boolean getConfiguracoesLiberarAlteracaoSenhaProfessorFavorito() {
		return configuracoesLiberarAlteracaoSenhaProfessorFavorito;
	}

	/**
	 * @param configuracoesLiberarAlteracaoSenhaProfessorFavorito the configuracoesLiberarAlteracaoSenhaProfessorFavorito to set
	 */
	public void setConfiguracoesLiberarAlteracaoSenhaProfessorFavorito(Boolean configuracoesLiberarAlteracaoSenhaProfessorFavorito) {
		this.configuracoesLiberarAlteracaoSenhaProfessorFavorito = configuracoesLiberarAlteracaoSenhaProfessorFavorito;
	}

	/**
	 * @return the configuracoesLiberarAlteracaoFotoProfessorFavorito
	 */
	public Boolean getConfiguracoesLiberarAlteracaoFotoProfessorFavorito() {
		return configuracoesLiberarAlteracaoFotoProfessorFavorito;
	}

	/**
	 * @param configuracoesLiberarAlteracaoFotoProfessorFavorito the configuracoesLiberarAlteracaoFotoProfessorFavorito to set
	 */
	public void setConfiguracoesLiberarAlteracaoFotoProfessorFavorito(Boolean configuracoesLiberarAlteracaoFotoProfessorFavorito) {
		this.configuracoesLiberarAlteracaoFotoProfessorFavorito = configuracoesLiberarAlteracaoFotoProfessorFavorito;
	}

	/**
	 * @return the configuracoesLiberarAlteracaoCorTelaProfessorFavorito
	 */
	public Boolean getConfiguracoesLiberarAlteracaoCorTelaProfessorFavorito() {
		return configuracoesLiberarAlteracaoCorTelaProfessorFavorito;
	}

	/**
	 * @param configuracoesLiberarAlteracaoCorTelaProfessorFavorito the configuracoesLiberarAlteracaoCorTelaProfessorFavorito to set
	 */
	public void setConfiguracoesLiberarAlteracaoCorTelaProfessorFavorito(Boolean configuracoesLiberarAlteracaoCorTelaProfessorFavorito) {
		this.configuracoesLiberarAlteracaoCorTelaProfessorFavorito = configuracoesLiberarAlteracaoCorTelaProfessorFavorito;
	}

	/**
	 * @return the minhasNotasFavorito
	 */
	public Boolean getMinhasNotasFavorito() {
		return minhasNotasFavorito;
	}

	/**
	 * @param minhasNotasFavorito the minhasNotasFavorito to set
	 */
	public void setMinhasNotasFavorito(Boolean minhasNotasFavorito) {
		this.minhasNotasFavorito = minhasNotasFavorito;
	}

	/**
	 * @return the meusAmigosFavorito
	 */
	public Boolean getMeusAmigosFavorito() {
		return meusAmigosFavorito;
	}

	/**
	 * @param meusAmigosFavorito the meusAmigosFavorito to set
	 */
	public void setMeusAmigosFavorito(Boolean meusAmigosFavorito) {
		this.meusAmigosFavorito = meusAmigosFavorito;
	}

	/**
	 * @return the meusHorariosFavorito
	 */
	public Boolean getMeusHorariosFavorito() {
		return meusHorariosFavorito;
	}

	/**
	 * @param meusHorariosFavorito the meusHorariosFavorito to set
	 */
	public void setMeusHorariosFavorito(Boolean meusHorariosFavorito) {
		this.meusHorariosFavorito = meusHorariosFavorito;
	}

	/**
	 * @return the meusProfessoresFavorito
	 */
	public Boolean getMeusProfessoresFavorito() {
		return meusProfessoresFavorito;
	}

	/**
	 * @param meusProfessoresFavorito the meusProfessoresFavorito to set
	 */
	public void setMeusProfessoresFavorito(Boolean meusProfessoresFavorito) {
		this.meusProfessoresFavorito = meusProfessoresFavorito;
	}

	/**
	 * @return the minhasFaltasFavorito
	 */
	public Boolean getMinhasFaltasFavorito() {
		return minhasFaltasFavorito;
	}

	/**
	 * @param minhasFaltasFavorito the minhasFaltasFavorito to set
	 */
	public void setMinhasFaltasFavorito(Boolean minhasFaltasFavorito) {
		this.minhasFaltasFavorito = minhasFaltasFavorito;
	}

	/**
	 * @return the downloadArquivoFavorito
	 */
	public Boolean getDownloadArquivoFavorito() {
		return downloadArquivoFavorito;
	}

	/**
	 * @param downloadArquivoFavorito the downloadArquivoFavorito to set
	 */
	public void setDownloadArquivoFavorito(Boolean downloadArquivoFavorito) {
		this.downloadArquivoFavorito = downloadArquivoFavorito;
	}

	/**
	 * @return the planoEstudoFavorito
	 */
	public Boolean getPlanoEstudoFavorito() {
		return planoEstudoFavorito;
	}

	/**
	 * @param planoEstudoFavorito the planoEstudoFavorito to set
	 */
	public void setPlanoEstudoFavorito(Boolean planoEstudoFavorito) {
		this.planoEstudoFavorito = planoEstudoFavorito;
	}

	/**
	 * @return the minhasContasPagarFavorito
	 */
	public Boolean getMinhasContasPagarFavorito() {
		return minhasContasPagarFavorito;
	}

	/**
	 * @param minhasContasPagarFavorito the minhasContasPagarFavorito to set
	 */
	public void setMinhasContasPagarFavorito(Boolean minhasContasPagarFavorito) {
		this.minhasContasPagarFavorito = minhasContasPagarFavorito;
	}

	/**
	 * @return the meusContratosFavorito
	 */
	public Boolean getMeusContratosFavorito() {
		return meusContratosFavorito;
	}

	/**
	 * @param meusContratosFavorito the meusContratosFavorito to set
	 */
	public void setMeusContratosFavorito(Boolean meusContratosFavorito) {
		this.meusContratosFavorito = meusContratosFavorito;
	}

	/**
	 * @return the configuracoesVisaoFavorito
	 */
	public Boolean getConfiguracoesVisaoFavorito() {
		return configuracoesVisaoFavorito;
	}

	/**
	 * @param configuracoesVisaoFavorito the configuracoesVisaoFavorito to set
	 */
	public void setConfiguracoesVisaoFavorito(Boolean configuracoesVisaoFavorito) {
		this.configuracoesVisaoFavorito = configuracoesVisaoFavorito;
	}

	/**
	 * @return the configuracoesAlteracaoSenhaFavorito
	 */
	public Boolean getConfiguracoesAlteracaoSenhaFavorito() {
		return configuracoesAlteracaoSenhaFavorito;
	}

	/**
	 * @param configuracoesAlteracaoSenhaFavorito the configuracoesAlteracaoSenhaFavorito to set
	 */
	public void setConfiguracoesAlteracaoSenhaFavorito(Boolean configuracoesAlteracaoSenhaFavorito) {
		this.configuracoesAlteracaoSenhaFavorito = configuracoesAlteracaoSenhaFavorito;
	}

	/**
	 * @return the configuracoesAlteracaoFotoFavorito
	 */
	public Boolean getConfiguracoesAlteracaoFotoFavorito() {
		return configuracoesAlteracaoFotoFavorito;
	}

	/**
	 * @param configuracoesAlteracaoFotoFavorito the configuracoesAlteracaoFotoFavorito to set
	 */
	public void setConfiguracoesAlteracaoFotoFavorito(Boolean configuracoesAlteracaoFotoFavorito) {
		this.configuracoesAlteracaoFotoFavorito = configuracoesAlteracaoFotoFavorito;
	}

	/**
	 * @return the configuracoesAlteracaoCorTelaFavorito
	 */
	public Boolean getConfiguracoesAlteracaoCorTelaFavorito() {
		return configuracoesAlteracaoCorTelaFavorito;
	}

	/**
	 * @param configuracoesAlteracaoCorTelaFavorito the configuracoesAlteracaoCorTelaFavorito to set
	 */
	public void setConfiguracoesAlteracaoCorTelaFavorito(Boolean configuracoesAlteracaoCorTelaFavorito) {
		this.configuracoesAlteracaoCorTelaFavorito = configuracoesAlteracaoCorTelaFavorito;
	}

	/**
	 * @return the planoContaFavorito
	 */
	public Boolean getPlanoContaFavorito() {
		return planoContaFavorito;
	}

	/**
	 * @param planoContaFavorito the planoContaFavorito to set
	 */
	public void setPlanoContaFavorito(Boolean planoContaFavorito) {
		this.planoContaFavorito = planoContaFavorito;
	}

	/**
	 * @return the dRE
	 */
	public Boolean getdRE() {
		return dRE;
	}

	/**
	 * @param dRE the dRE to set
	 */
	public void setdRE(Boolean dRE) {
		this.dRE = dRE;
	}

	/**
	 * @return the dREFavorito
	 */
	public Boolean getdREFavorito() {
		return dREFavorito;
	}

	/**
	 * @param dREFavorito the dREFavorito to set
	 */
	public void setdREFavorito(Boolean dREFavorito) {
		this.dREFavorito = dREFavorito;
	}

	/**
	 * @return the tipoEventoContabilFavorito
	 */
	public Boolean getTipoEventoContabilFavorito() {
		return tipoEventoContabilFavorito;
	}

	/**
	 * @param tipoEventoContabilFavorito the tipoEventoContabilFavorito to set
	 */
	public void setTipoEventoContabilFavorito(Boolean tipoEventoContabilFavorito) {
		this.tipoEventoContabilFavorito = tipoEventoContabilFavorito;
	}

	/**
	 * @return the historicoContabilFavorito
	 */
	public Boolean getHistoricoContabilFavorito() {
		return historicoContabilFavorito;
	}

	/**
	 * @param historicoContabilFavorito the historicoContabilFavorito to set
	 */
	public void setHistoricoContabilFavorito(Boolean historicoContabilFavorito) {
		this.historicoContabilFavorito = historicoContabilFavorito;
	}

	/**
	 * @return the contabilFavorito
	 */
	public Boolean getContabilFavorito() {
		return contabilFavorito;
	}

	/**
	 * @param contabilFavorito the contabilFavorito to set
	 */
	public void setContabilFavorito(Boolean contabilFavorito) {
		this.contabilFavorito = contabilFavorito;
	}

	/**
	 * @return the calculoMesFavorito
	 */
	public Boolean getCalculoMesFavorito() {
		return calculoMesFavorito;
	}

	/**
	 * @param calculoMesFavorito the calculoMesFavorito to set
	 */
	public void setCalculoMesFavorito(Boolean calculoMesFavorito) {
		this.calculoMesFavorito = calculoMesFavorito;
	}

	/**
	 * @return the fechamentoMesFavorito
	 */
	public Boolean getFechamentoMesFavorito() {
		return fechamentoMesFavorito;
	}

	/**
	 * @param fechamentoMesFavorito the fechamentoMesFavorito to set
	 */
	public void setFechamentoMesFavorito(Boolean fechamentoMesFavorito) {
		this.fechamentoMesFavorito = fechamentoMesFavorito;
	}

	/**
	 * @return the painelGestorRequerimentosAcademicoFavorito
	 */
	public Boolean getPainelGestorRequerimentosAcademicoFavorito() {
		return painelGestorRequerimentosAcademicoFavorito;
	}

	/**
	 * @param painelGestorRequerimentosAcademicoFavorito the painelGestorRequerimentosAcademicoFavorito to set
	 */
	public void setPainelGestorRequerimentosAcademicoFavorito(Boolean painelGestorRequerimentosAcademicoFavorito) {
		this.painelGestorRequerimentosAcademicoFavorito = painelGestorRequerimentosAcademicoFavorito;
	}

	/**
	 * @return the painelGestorComunicacaoInternaAcademicoFavorito
	 */
	public Boolean getPainelGestorComunicacaoInternaAcademicoFavorito() {
		return painelGestorComunicacaoInternaAcademicoFavorito;
	}

	/**
	 * @param painelGestorComunicacaoInternaAcademicoFavorito the painelGestorComunicacaoInternaAcademicoFavorito to set
	 */
	public void setPainelGestorComunicacaoInternaAcademicoFavorito(Boolean painelGestorComunicacaoInternaAcademicoFavorito) {
		this.painelGestorComunicacaoInternaAcademicoFavorito = painelGestorComunicacaoInternaAcademicoFavorito;
	}

	/**
	 * @return the painelGestorRequerimentosFinanceiroFavorito
	 */
	public Boolean getPainelGestorRequerimentosFinanceiroFavorito() {
		return painelGestorRequerimentosFinanceiroFavorito;
	}

	/**
	 * @param painelGestorRequerimentosFinanceiroFavorito the painelGestorRequerimentosFinanceiroFavorito to set
	 */
	public void setPainelGestorRequerimentosFinanceiroFavorito(Boolean painelGestorRequerimentosFinanceiroFavorito) {
		this.painelGestorRequerimentosFinanceiroFavorito = painelGestorRequerimentosFinanceiroFavorito;
	}

	/**
	 * @return the painelGestorComunicacaoInternaFinanceiroFavorito
	 */
	public Boolean getPainelGestorComunicacaoInternaFinanceiroFavorito() {
		return painelGestorComunicacaoInternaFinanceiroFavorito;
	}

	/**
	 * @param painelGestorComunicacaoInternaFinanceiroFavorito the painelGestorComunicacaoInternaFinanceiroFavorito to set
	 */
	public void setPainelGestorComunicacaoInternaFinanceiroFavorito(Boolean painelGestorComunicacaoInternaFinanceiroFavorito) {
		this.painelGestorComunicacaoInternaFinanceiroFavorito = painelGestorComunicacaoInternaFinanceiroFavorito;
	}

	/**
	 * @return the controleConcorrenciaHorarioTurmaFavorito
	 */
	public Boolean getControleConcorrenciaHorarioTurmaFavorito() {
		return controleConcorrenciaHorarioTurmaFavorito;
	}

	/**
	 * @param controleConcorrenciaHorarioTurmaFavorito the controleConcorrenciaHorarioTurmaFavorito to set
	 */
	public void setControleConcorrenciaHorarioTurmaFavorito(Boolean controleConcorrenciaHorarioTurmaFavorito) {
		this.controleConcorrenciaHorarioTurmaFavorito = controleConcorrenciaHorarioTurmaFavorito;
	}

	/**
	 * @return the metaFavorito
	 */
	public Boolean getMetaFavorito() {
		return metaFavorito;
	}

	/**
	 * @param metaFavorito the metaFavorito to set
	 */
	public void setMetaFavorito(Boolean metaFavorito) {
		this.metaFavorito = metaFavorito;
	}

	/**
	 * @return the prospectsFavorito
	 */
	public Boolean getProspectsFavorito() {
		return prospectsFavorito;
	}

	/**
	 * @param prospectsFavorito the prospectsFavorito to set
	 */
	public void setProspectsFavorito(Boolean prospectsFavorito) {
		this.prospectsFavorito = prospectsFavorito;
	}

	/**
	 * @return the buscaProspectFavorito
	 */
	public Boolean getBuscaProspectFavorito() {
		return buscaProspectFavorito;
	}

	/**
	 * @param buscaProspectFavorito the buscaProspectFavorito to set
	 */
	public void setBuscaProspectFavorito(Boolean buscaProspectFavorito) {
		this.buscaProspectFavorito = buscaProspectFavorito;
	}

	/**
	 * @return the novoProspectFavorito
	 */
	public Boolean getNovoProspectFavorito() {
		return novoProspectFavorito;
	}

	/**
	 * @param novoProspectFavorito the novoProspectFavorito to set
	 */
	public void setNovoProspectFavorito(Boolean novoProspectFavorito) {
		this.novoProspectFavorito = novoProspectFavorito;
	}

	/**
	 * @return the workflowFavorito
	 */
	public Boolean getWorkflowFavorito() {
		return workflowFavorito;
	}

	/**
	 * @param workflowFavorito the workflowFavorito to set
	 */
	public void setWorkflowFavorito(Boolean workflowFavorito) {
		this.workflowFavorito = workflowFavorito;
	}

	/**
	 * @return the voltarEtapaAnteriorFavorito
	 */
	public Boolean getVoltarEtapaAnteriorFavorito() {
		return voltarEtapaAnteriorFavorito;
	}

	/**
	 * @param voltarEtapaAnteriorFavorito the voltarEtapaAnteriorFavorito to set
	 */
	public void setVoltarEtapaAnteriorFavorito(Boolean voltarEtapaAnteriorFavorito) {
		this.voltarEtapaAnteriorFavorito = voltarEtapaAnteriorFavorito;
	}

	/**
	 * @return the situacaoProspectPipelineFavorito
	 */
	public Boolean getSituacaoProspectPipelineFavorito() {
		return situacaoProspectPipelineFavorito;
	}

	/**
	 * @param situacaoProspectPipelineFavorito the situacaoProspectPipelineFavorito to set
	 */
	public void setSituacaoProspectPipelineFavorito(Boolean situacaoProspectPipelineFavorito) {
		this.situacaoProspectPipelineFavorito = situacaoProspectPipelineFavorito;
	}

	/**
	 * @return the buscaCandidatoVagaFavorito
	 */
	public Boolean getBuscaCandidatoVagaFavorito() {
		return buscaCandidatoVagaFavorito;
	}

	/**
	 * @param buscaCandidatoVagaFavorito the buscaCandidatoVagaFavorito to set
	 */
	public void setBuscaCandidatoVagaFavorito(Boolean buscaCandidatoVagaFavorito) {
		this.buscaCandidatoVagaFavorito = buscaCandidatoVagaFavorito;
	}

	/**
	 * @return the registroEntradaFavorito
	 */
	public Boolean getRegistroEntradaFavorito() {
		return registroEntradaFavorito;
	}

	/**
	 * @param registroEntradaFavorito the registroEntradaFavorito to set
	 */
	public void setRegistroEntradaFavorito(Boolean registroEntradaFavorito) {
		this.registroEntradaFavorito = registroEntradaFavorito;
	}

	/**
	 * @return the buscaVagasFavorito
	 */
	public Boolean getBuscaVagasFavorito() {
		return buscaVagasFavorito;
	}

	/**
	 * @param buscaVagasFavorito the buscaVagasFavorito to set
	 */
	public void setBuscaVagasFavorito(Boolean buscaVagasFavorito) {
		this.buscaVagasFavorito = buscaVagasFavorito;
	}

	/**
	 * @return the agendaPessoaFavorito
	 */
	public Boolean getAgendaPessoaFavorito() {
		return agendaPessoaFavorito;
	}

	/**
	 * @param agendaPessoaFavorito the agendaPessoaFavorito to set
	 */
	public void setAgendaPessoaFavorito(Boolean agendaPessoaFavorito) {
		this.agendaPessoaFavorito = agendaPessoaFavorito;
	}

	/**
	 * @return the visaoAdministradorAgendaPessoaFavorito
	 */
	public Boolean getVisaoAdministradorAgendaPessoaFavorito() {
		return visaoAdministradorAgendaPessoaFavorito;
	}

	/**
	 * @param visaoAdministradorAgendaPessoaFavorito the visaoAdministradorAgendaPessoaFavorito to set
	 */
	public void setVisaoAdministradorAgendaPessoaFavorito(Boolean visaoAdministradorAgendaPessoaFavorito) {
		this.visaoAdministradorAgendaPessoaFavorito = visaoAdministradorAgendaPessoaFavorito;
	}

	/**
	 * @return the cronogramaAulaFavorito
	 */
	public Boolean getCronogramaAulaFavorito() {
		return cronogramaAulaFavorito;
	}

	/**
	 * @param cronogramaAulaFavorito the cronogramaAulaFavorito to set
	 */
	public void setCronogramaAulaFavorito(Boolean cronogramaAulaFavorito) {
		this.cronogramaAulaFavorito = cronogramaAulaFavorito;
	}

	/**
	 * @return the followUpProspectFavorito
	 */
	public Boolean getFollowUpProspectFavorito() {
		return followUpProspectFavorito;
	}

	/**
	 * @param followUpProspectFavorito the followUpProspectFavorito to set
	 */
	public void setFollowUpProspectFavorito(Boolean followUpProspectFavorito) {
		this.followUpProspectFavorito = followUpProspectFavorito;
	}

	/**
	 * @return the alterarObservacaoInteracaoFollowUpFavorito
	 */
	public Boolean getAlterarObservacaoInteracaoFollowUpFavorito() {
		return alterarObservacaoInteracaoFollowUpFavorito;
	}

	/**
	 * @param alterarObservacaoInteracaoFollowUpFavorito the alterarObservacaoInteracaoFollowUpFavorito to set
	 */
	public void setAlterarObservacaoInteracaoFollowUpFavorito(Boolean alterarObservacaoInteracaoFollowUpFavorito) {
		this.alterarObservacaoInteracaoFollowUpFavorito = alterarObservacaoInteracaoFollowUpFavorito;
	}

	/**
	 * @return the gravarInteracaoFollowUpFavorito
	 */
	public Boolean getGravarInteracaoFollowUpFavorito() {
		return gravarInteracaoFollowUpFavorito;
	}

	/**
	 * @param gravarInteracaoFollowUpFavorito the gravarInteracaoFollowUpFavorito to set
	 */
	public void setGravarInteracaoFollowUpFavorito(Boolean gravarInteracaoFollowUpFavorito) {
		this.gravarInteracaoFollowUpFavorito = gravarInteracaoFollowUpFavorito;
	}

	/**
	 * @return the interacaoWorkflowFavorito
	 */
	public Boolean getInteracaoWorkflowFavorito() {
		return interacaoWorkflowFavorito;
	}

	/**
	 * @param interacaoWorkflowFavorito the interacaoWorkflowFavorito to set
	 */
	public void setInteracaoWorkflowFavorito(Boolean interacaoWorkflowFavorito) {
		this.interacaoWorkflowFavorito = interacaoWorkflowFavorito;
	}

	/**
	 * @return the motivoInsucessoFavorito
	 */
	public Boolean getMotivoInsucessoFavorito() {
		return motivoInsucessoFavorito;
	}

	/**
	 * @param motivoInsucessoFavorito the motivoInsucessoFavorito to set
	 */
	public void setMotivoInsucessoFavorito(Boolean motivoInsucessoFavorito) {
		this.motivoInsucessoFavorito = motivoInsucessoFavorito;
	}

	/**
	 * @return the comissionamentoTurmaFavorito
	 */
	public Boolean getComissionamentoTurmaFavorito() {
		return comissionamentoTurmaFavorito;
	}

	/**
	 * @param comissionamentoTurmaFavorito the comissionamentoTurmaFavorito to set
	 */
	public void setComissionamentoTurmaFavorito(Boolean comissionamentoTurmaFavorito) {
		this.comissionamentoTurmaFavorito = comissionamentoTurmaFavorito;
	}

	/**
	 * @return the rankingFavorito
	 */
	public Boolean getRankingFavorito() {
		return rankingFavorito;
	}

	/**
	 * @param rankingFavorito the rankingFavorito to set
	 */
	public void setRankingFavorito(Boolean rankingFavorito) {
		this.rankingFavorito = rankingFavorito;
	}

	/**
	 * @return the configuracaoRankingFavorito
	 */
	public Boolean getConfiguracaoRankingFavorito() {
		return configuracaoRankingFavorito;
	}

	/**
	 * @param configuracaoRankingFavorito the configuracaoRankingFavorito to set
	 */
	public void setConfiguracaoRankingFavorito(Boolean configuracaoRankingFavorito) {
		this.configuracaoRankingFavorito = configuracaoRankingFavorito;
	}

	/**
	 * @return the percentualConfiguracaoRankingFavorito
	 */
	public Boolean getPercentualConfiguracaoRankingFavorito() {
		return percentualConfiguracaoRankingFavorito;
	}

	/**
	 * @param percentualConfiguracaoRankingFavorito the percentualConfiguracaoRankingFavorito to set
	 */
	public void setPercentualConfiguracaoRankingFavorito(Boolean percentualConfiguracaoRankingFavorito) {
		this.percentualConfiguracaoRankingFavorito = percentualConfiguracaoRankingFavorito;
	}

	/**
	 * @return the extratoComissaoRelFavorito
	 */
	public Boolean getExtratoComissaoRelFavorito() {
		return extratoComissaoRelFavorito;
	}

	/**
	 * @param extratoComissaoRelFavorito the extratoComissaoRelFavorito to set
	 */
	public void setExtratoComissaoRelFavorito(Boolean extratoComissaoRelFavorito) {
		this.extratoComissaoRelFavorito = extratoComissaoRelFavorito;
	}

	public Boolean getProdutividadeConsultorRel() {
		return produtividadeConsultorRel;
	}

	public void setProdutividadeConsultorRel(Boolean produtividadeConsultorRel) {
		this.produtividadeConsultorRel = produtividadeConsultorRel;
	}

	public Boolean getProdutividadeConsultorRelFavorito() {
		return produtividadeConsultorRelFavorito;
	}

	public void setProdutividadeConsultorRelFavorito(Boolean produtividadeConsultorRelFavorito) {
		this.produtividadeConsultorRelFavorito = produtividadeConsultorRelFavorito;
	}

	/**
	 * @return the painelGestorSupervisaoVendaFavorito
	 */
	public Boolean getPainelGestorSupervisaoVendaFavorito() {
		return painelGestorSupervisaoVendaFavorito;
	}

	/**
	 * @param painelGestorSupervisaoVendaFavorito the painelGestorSupervisaoVendaFavorito to set
	 */
	public void setPainelGestorSupervisaoVendaFavorito(Boolean painelGestorSupervisaoVendaFavorito) {
		this.painelGestorSupervisaoVendaFavorito = painelGestorSupervisaoVendaFavorito;
	}

	/**
	 * @return the painelGestorVendedorFavorito
	 */
	public Boolean getPainelGestorVendedorFavorito() {
		return painelGestorVendedorFavorito;
	}

	/**
	 * @param painelGestorVendedorFavorito the painelGestorVendedorFavorito to set
	 */
	public void setPainelGestorVendedorFavorito(Boolean painelGestorVendedorFavorito) {
		this.painelGestorVendedorFavorito = painelGestorVendedorFavorito;
	}

	/**
	 * @return the requisicaoRelFavorito
	 */
	public Boolean getRequisicaoRelFavorito() {
		return requisicaoRelFavorito;
	}

	/**
	 * @param requisicaoRelFavorito the requisicaoRelFavorito to set
	 */
	public void setRequisicaoRelFavorito(Boolean requisicaoRelFavorito) {
		this.requisicaoRelFavorito = requisicaoRelFavorito;
	}

	/**
	 * @return the prestacaoContaTurmaFavorito
	 */
	public Boolean getPrestacaoContaTurmaFavorito() {
		return prestacaoContaTurmaFavorito;
	}

	/**
	 * @param prestacaoContaTurmaFavorito the prestacaoContaTurmaFavorito to set
	 */
	public void setPrestacaoContaTurmaFavorito(Boolean prestacaoContaTurmaFavorito) {
		this.prestacaoContaTurmaFavorito = prestacaoContaTurmaFavorito;
	}

	/**
	 * @return the prestacaoContaUnidadeEnsinoFavorito
	 */
	public Boolean getPrestacaoContaUnidadeEnsinoFavorito() {
		return prestacaoContaUnidadeEnsinoFavorito;
	}

	/**
	 * @param prestacaoContaUnidadeEnsinoFavorito the prestacaoContaUnidadeEnsinoFavorito to set
	 */
	public void setPrestacaoContaUnidadeEnsinoFavorito(Boolean prestacaoContaUnidadeEnsinoFavorito) {
		this.prestacaoContaUnidadeEnsinoFavorito = prestacaoContaUnidadeEnsinoFavorito;
	}

	/**
	 * @return the documentacaoPendenteProfessorRelFavorito
	 */
	public Boolean getDocumentacaoPendenteProfessorRelFavorito() {
		return documentacaoPendenteProfessorRelFavorito;
	}

	/**
	 * @param documentacaoPendenteProfessorRelFavorito the documentacaoPendenteProfessorRelFavorito to set
	 */
	public void setDocumentacaoPendenteProfessorRelFavorito(Boolean documentacaoPendenteProfessorRelFavorito) {
		this.documentacaoPendenteProfessorRelFavorito = documentacaoPendenteProfessorRelFavorito;
	}

	/**
	 * @return the mapaAlunoRelFavorito
	 */
	public Boolean getMapaAlunoRelFavorito() {
		return mapaAlunoRelFavorito;
	}

	/**
	 * @param mapaAlunoRelFavorito the mapaAlunoRelFavorito to set
	 */
	public void setMapaAlunoRelFavorito(Boolean mapaAlunoRelFavorito) {
		this.mapaAlunoRelFavorito = mapaAlunoRelFavorito;
	}

	/**
	 * @return the extratoContaCorrenteRelFavorito
	 */
	public Boolean getExtratoContaCorrenteRelFavorito() {
		return extratoContaCorrenteRelFavorito;
	}

	/**
	 * @param extratoContaCorrenteRelFavorito the extratoContaCorrenteRelFavorito to set
	 */
	public void setExtratoContaCorrenteRelFavorito(Boolean extratoContaCorrenteRelFavorito) {
		this.extratoContaCorrenteRelFavorito = extratoContaCorrenteRelFavorito;
	}

	/**
	 * @return the categoriaDespesaRelFavorito
	 */
	public Boolean getCategoriaDespesaRelFavorito() {
		return categoriaDespesaRelFavorito;
	}

	/**
	 * @param categoriaDespesaRelFavorito the categoriaDespesaRelFavorito to set
	 */
	public void setCategoriaDespesaRelFavorito(Boolean categoriaDespesaRelFavorito) {
		this.categoriaDespesaRelFavorito = categoriaDespesaRelFavorito;
	}

	/**
	 * @return the forumProfessorFavorito
	 */
	public Boolean getForumProfessorFavorito() {
		return forumProfessorFavorito;
	}

	/**
	 * @param forumProfessorFavorito the forumProfessorFavorito to set
	 */
	public void setForumProfessorFavorito(Boolean forumProfessorFavorito) {
		this.forumProfessorFavorito = forumProfessorFavorito;
	}

	/**
	 * @return the forumAlunoFavorito
	 */
	public Boolean getForumAlunoFavorito() {
		return forumAlunoFavorito;
	}

	/**
	 * @param forumAlunoFavorito the forumAlunoFavorito to set
	 */
	public void setForumAlunoFavorito(Boolean forumAlunoFavorito) {
		this.forumAlunoFavorito = forumAlunoFavorito;
	}

	/**
	 * @return the alterarTemaForumFavorito
	 */
	public Boolean getAlterarTemaForumFavorito() {
		return alterarTemaForumFavorito;
	}

	/**
	 * @param alterarTemaForumFavorito the alterarTemaForumFavorito to set
	 */
	public void setAlterarTemaForumFavorito(Boolean alterarTemaForumFavorito) {
		this.alterarTemaForumFavorito = alterarTemaForumFavorito;
	}

	/**
	 * @return the alterarTemaForumProfessorFavorito
	 */
	public Boolean getAlterarTemaForumProfessorFavorito() {
		return alterarTemaForumProfessorFavorito;
	}

	/**
	 * @param alterarTemaForumProfessorFavorito the alterarTemaForumProfessorFavorito to set
	 */
	public void setAlterarTemaForumProfessorFavorito(Boolean alterarTemaForumProfessorFavorito) {
		this.alterarTemaForumProfessorFavorito = alterarTemaForumProfessorFavorito;
	}

	/**
	 * @return the contaReceber_PermitirAlterarDataVencimentoFavorito
	 */
	public Boolean getContaReceber_PermitirAlterarDataVencimentoFavorito() {
		return contaReceber_PermitirAlterarDataVencimentoFavorito;
	}

	/**
	 * @param contaReceber_PermitirAlterarDataVencimentoFavorito the contaReceber_PermitirAlterarDataVencimentoFavorito to set
	 */
	public void setContaReceber_PermitirAlterarDataVencimentoFavorito(Boolean contaReceber_PermitirAlterarDataVencimentoFavorito) {
		this.contaReceber_PermitirAlterarDataVencimentoFavorito = contaReceber_PermitirAlterarDataVencimentoFavorito;
	}

	/**
	 * @return the followMeFavorito
	 */
	public Boolean getFollowMeFavorito() {
		return followMeFavorito;
	}

	/**
	 * @param followMeFavorito the followMeFavorito to set
	 */
	public void setFollowMeFavorito(Boolean followMeFavorito) {
		this.followMeFavorito = followMeFavorito;
	}

	/**
	 * @return the questaoExercicioFavorito
	 */
	public Boolean getQuestaoExercicioFavorito() {
		return questaoExercicioFavorito;
	}

	/**
	 * @param questaoExercicioFavorito the questaoExercicioFavorito to set
	 */
	public void setQuestaoExercicioFavorito(Boolean questaoExercicioFavorito) {
		this.questaoExercicioFavorito = questaoExercicioFavorito;
	}

	/**
	 * @return the questaoOnlineFavorito
	 */
	public Boolean getQuestaoOnlineFavorito() {
		return questaoOnlineFavorito;
	}

	/**
	 * @param questaoOnlineFavorito the questaoOnlineFavorito to set
	 */
	public void setQuestaoOnlineFavorito(Boolean questaoOnlineFavorito) {
		this.questaoOnlineFavorito = questaoOnlineFavorito;
	}

	/**
	 * @return the questaoPresencialFavorito
	 */
	public Boolean getQuestaoPresencialFavorito() {
		return questaoPresencialFavorito;
	}

	/**
	 * @param questaoPresencialFavorito the questaoPresencialFavorito to set
	 */
	public void setQuestaoPresencialFavorito(Boolean questaoPresencialFavorito) {
		this.questaoPresencialFavorito = questaoPresencialFavorito;
	}

	/**
	 * @return the listaExercicioFavorito
	 */
	public Boolean getListaExercicioFavorito() {
		return listaExercicioFavorito;
	}

	/**
	 * @param listaExercicioFavorito the listaExercicioFavorito to set
	 */
	public void setListaExercicioFavorito(Boolean listaExercicioFavorito) {
		this.listaExercicioFavorito = listaExercicioFavorito;
	}

	/**
	 * @return the listaExercicioProfessorFavorito
	 */
	public Boolean getListaExercicioProfessorFavorito() {
		return listaExercicioProfessorFavorito;
	}

	/**
	 * @param listaExercicioProfessorFavorito the listaExercicioProfessorFavorito to set
	 */
	public void setListaExercicioProfessorFavorito(Boolean listaExercicioProfessorFavorito) {
		this.listaExercicioProfessorFavorito = listaExercicioProfessorFavorito;
	}

	/**
	 * @return the listaExercicioAlunoFavorito
	 */
	public Boolean getListaExercicioAlunoFavorito() {
		return listaExercicioAlunoFavorito;
	}

	/**
	 * @param listaExercicioAlunoFavorito the listaExercicioAlunoFavorito to set
	 */
	public void setListaExercicioAlunoFavorito(Boolean listaExercicioAlunoFavorito) {
		this.listaExercicioAlunoFavorito = listaExercicioAlunoFavorito;
	}

	/**
	 * @return the alterarListaExercicioOutroProfessorFavorito
	 */
	public Boolean getAlterarListaExercicioOutroProfessorFavorito() {
		return alterarListaExercicioOutroProfessorFavorito;
	}

	/**
	 * @param alterarListaExercicioOutroProfessorFavorito the alterarListaExercicioOutroProfessorFavorito to set
	 */
	public void setAlterarListaExercicioOutroProfessorFavorito(Boolean alterarListaExercicioOutroProfessorFavorito) {
		this.alterarListaExercicioOutroProfessorFavorito = alterarListaExercicioOutroProfessorFavorito;
	}

	/**
	 * @return the alterarListaExercicioOutroProfessorProfessorFavorito
	 */
	public Boolean getAlterarListaExercicioOutroProfessorProfessorFavorito() {
		return alterarListaExercicioOutroProfessorProfessorFavorito;
	}

	/**
	 * @param alterarListaExercicioOutroProfessorProfessorFavorito the alterarListaExercicioOutroProfessorProfessorFavorito to set
	 */
	public void setAlterarListaExercicioOutroProfessorProfessorFavorito(Boolean alterarListaExercicioOutroProfessorProfessorFavorito) {
		this.alterarListaExercicioOutroProfessorProfessorFavorito = alterarListaExercicioOutroProfessorProfessorFavorito;
	}

	/**
	 * @return the inativarListaExercicioFavorito
	 */
	public Boolean getInativarListaExercicioFavorito() {
		return inativarListaExercicioFavorito;
	}

	/**
	 * @param inativarListaExercicioFavorito the inativarListaExercicioFavorito to set
	 */
	public void setInativarListaExercicioFavorito(Boolean inativarListaExercicioFavorito) {
		this.inativarListaExercicioFavorito = inativarListaExercicioFavorito;
	}

	/**
	 * @return the inativarListaExercicioProfessorFavorito
	 */
	public Boolean getInativarListaExercicioProfessorFavorito() {
		return inativarListaExercicioProfessorFavorito;
	}

	/**
	 * @param inativarListaExercicioProfessorFavorito the inativarListaExercicioProfessorFavorito to set
	 */
	public void setInativarListaExercicioProfessorFavorito(Boolean inativarListaExercicioProfessorFavorito) {
		this.inativarListaExercicioProfessorFavorito = inativarListaExercicioProfessorFavorito;
	}

	/**
	 * @return the contaReceber_PermitirEnvioEmailCobrancaFavorito
	 */
	public Boolean getContaReceber_PermitirEnvioEmailCobrancaFavorito() {
		return contaReceber_PermitirEnvioEmailCobrancaFavorito;
	}

	/**
	 * @param contaReceber_PermitirEnvioEmailCobrancaFavorito the contaReceber_PermitirEnvioEmailCobrancaFavorito to set
	 */
	public void setContaReceber_PermitirEnvioEmailCobrancaFavorito(Boolean contaReceber_PermitirEnvioEmailCobrancaFavorito) {
		this.contaReceber_PermitirEnvioEmailCobrancaFavorito = contaReceber_PermitirEnvioEmailCobrancaFavorito;
	}

	/**
	 * @return the duvidaProfessorFavorito
	 */
	public Boolean getDuvidaProfessorFavorito() {
		return duvidaProfessorFavorito;
	}

	/**
	 * @param duvidaProfessorFavorito the duvidaProfessorFavorito to set
	 */
	public void setDuvidaProfessorFavorito(Boolean duvidaProfessorFavorito) {
		this.duvidaProfessorFavorito = duvidaProfessorFavorito;
	}

	/**
	 * @return the duvidaProfessorAlunoFavorito
	 */
	public Boolean getDuvidaProfessorAlunoFavorito() {
		return duvidaProfessorAlunoFavorito;
	}

	/**
	 * @param duvidaProfessorAlunoFavorito the duvidaProfessorAlunoFavorito to set
	 */
	public void setDuvidaProfessorAlunoFavorito(Boolean duvidaProfessorAlunoFavorito) {
		this.duvidaProfessorAlunoFavorito = duvidaProfessorAlunoFavorito;
	}

	/**
	 * @return the painelGestorCrmFavorito
	 */
	public Boolean getPainelGestorCrmFavorito() {
		return painelGestorCrmFavorito;
	}

	/**
	 * @param painelGestorCrmFavorito the painelGestorCrmFavorito to set
	 */
	public void setPainelGestorCrmFavorito(Boolean painelGestorCrmFavorito) {
		this.painelGestorCrmFavorito = painelGestorCrmFavorito;
	}

	/**
	 * @return the calendarioLancamentoNotaFavorito
	 */
	public Boolean getCalendarioLancamentoNotaFavorito() {
		return calendarioLancamentoNotaFavorito;
	}

	/**
	 * @param calendarioLancamentoNotaFavorito the calendarioLancamentoNotaFavorito to set
	 */
	public void setCalendarioLancamentoNotaFavorito(Boolean calendarioLancamentoNotaFavorito) {
		this.calendarioLancamentoNotaFavorito = calendarioLancamentoNotaFavorito;
	}

	/**
	 * @return the calendarioRegistroAulaFavorito
	 */
	public Boolean getCalendarioRegistroAulaFavorito() {
		return calendarioRegistroAulaFavorito;
	}

	/**
	 * @param calendarioRegistroAulaFavorito the calendarioRegistroAulaFavorito to set
	 */
	public void setCalendarioRegistroAulaFavorito(Boolean calendarioRegistroAulaFavorito) {
		this.calendarioRegistroAulaFavorito = calendarioRegistroAulaFavorito;
	}

	/**
	 * @return the layoutEtiquetaFavorito
	 */
	public Boolean getLayoutEtiquetaFavorito() {
		return layoutEtiquetaFavorito;
	}

	/**
	 * @param layoutEtiquetaFavorito the layoutEtiquetaFavorito to set
	 */
	public void setLayoutEtiquetaFavorito(Boolean layoutEtiquetaFavorito) {
		this.layoutEtiquetaFavorito = layoutEtiquetaFavorito;
	}

	/**
	 * @return the distribuirSalaProcessoSeletivoFavorito
	 */
	public Boolean getDistribuirSalaProcessoSeletivoFavorito() {
		return distribuirSalaProcessoSeletivoFavorito;
	}

	/**
	 * @param distribuirSalaProcessoSeletivoFavorito the distribuirSalaProcessoSeletivoFavorito to set
	 */
	public void setDistribuirSalaProcessoSeletivoFavorito(Boolean distribuirSalaProcessoSeletivoFavorito) {
		this.distribuirSalaProcessoSeletivoFavorito = distribuirSalaProcessoSeletivoFavorito;
	}

	/**
	 * @return the provaProcessoSeletivoFavorito
	 */
	public Boolean getProvaProcessoSeletivoFavorito() {
		return provaProcessoSeletivoFavorito;
	}

	/**
	 * @param provaProcessoSeletivoFavorito the provaProcessoSeletivoFavorito to set
	 */
	public void setProvaProcessoSeletivoFavorito(Boolean provaProcessoSeletivoFavorito) {
		this.provaProcessoSeletivoFavorito = provaProcessoSeletivoFavorito;
	}

	/**
	 * @return the questaoProcessoSeletivoFavorito
	 */
	public Boolean getQuestaoProcessoSeletivoFavorito() {
		return questaoProcessoSeletivoFavorito;
	}

	/**
	 * @param questaoProcessoSeletivoFavorito the questaoProcessoSeletivoFavorito to set
	 */
	public void setQuestaoProcessoSeletivoFavorito(Boolean questaoProcessoSeletivoFavorito) {
		this.questaoProcessoSeletivoFavorito = questaoProcessoSeletivoFavorito;
	}

	/**
	 * @return the cancelarQuestaoProcessoSeletivoFavorito
	 */
	public Boolean getCancelarQuestaoProcessoSeletivoFavorito() {
		return cancelarQuestaoProcessoSeletivoFavorito;
	}

	/**
	 * @param cancelarQuestaoProcessoSeletivoFavorito the cancelarQuestaoProcessoSeletivoFavorito to set
	 */
	public void setCancelarQuestaoProcessoSeletivoFavorito(Boolean cancelarQuestaoProcessoSeletivoFavorito) {
		this.cancelarQuestaoProcessoSeletivoFavorito = cancelarQuestaoProcessoSeletivoFavorito;
	}

	/**
	 * @return the inativarQuestaoProcessoSeletivoFavorito
	 */
	public Boolean getInativarQuestaoProcessoSeletivoFavorito() {
		return inativarQuestaoProcessoSeletivoFavorito;
	}

	/**
	 * @param inativarQuestaoProcessoSeletivoFavorito the inativarQuestaoProcessoSeletivoFavorito to set
	 */
	public void setInativarQuestaoProcessoSeletivoFavorito(Boolean inativarQuestaoProcessoSeletivoFavorito) {
		this.inativarQuestaoProcessoSeletivoFavorito = inativarQuestaoProcessoSeletivoFavorito;
	}

	/**
	 * @return the ativarQuestaoProcessoSeletivoFavorito
	 */
	public Boolean getAtivarQuestaoProcessoSeletivoFavorito() {
		return ativarQuestaoProcessoSeletivoFavorito;
	}

	/**
	 * @param ativarQuestaoProcessoSeletivoFavorito the ativarQuestaoProcessoSeletivoFavorito to set
	 */
	public void setAtivarQuestaoProcessoSeletivoFavorito(Boolean ativarQuestaoProcessoSeletivoFavorito) {
		this.ativarQuestaoProcessoSeletivoFavorito = ativarQuestaoProcessoSeletivoFavorito;
	}

	/**
	 * @return the inativarProvaProcessoSeletivoFavorito
	 */
	public Boolean getInativarProvaProcessoSeletivoFavorito() {
		return inativarProvaProcessoSeletivoFavorito;
	}

	/**
	 * @param inativarProvaProcessoSeletivoFavorito the inativarProvaProcessoSeletivoFavorito to set
	 */
	public void setInativarProvaProcessoSeletivoFavorito(Boolean inativarProvaProcessoSeletivoFavorito) {
		this.inativarProvaProcessoSeletivoFavorito = inativarProvaProcessoSeletivoFavorito;
	}

	/**
	 * @return the ativarProvaProcessoSeletivoFavorito
	 */
	public Boolean getAtivarProvaProcessoSeletivoFavorito() {
		return ativarProvaProcessoSeletivoFavorito;
	}

	/**
	 * @param ativarProvaProcessoSeletivoFavorito the ativarProvaProcessoSeletivoFavorito to set
	 */
	public void setAtivarProvaProcessoSeletivoFavorito(Boolean ativarProvaProcessoSeletivoFavorito) {
		this.ativarProvaProcessoSeletivoFavorito = ativarProvaProcessoSeletivoFavorito;
	}

	/**
	 * @return the estatisticaProcessoSeletivoFavorito
	 */
	public Boolean getEstatisticaProcessoSeletivoFavorito() {
		return estatisticaProcessoSeletivoFavorito;
	}

	/**
	 * @param estatisticaProcessoSeletivoFavorito the estatisticaProcessoSeletivoFavorito to set
	 */
	public void setEstatisticaProcessoSeletivoFavorito(Boolean estatisticaProcessoSeletivoFavorito) {
		this.estatisticaProcessoSeletivoFavorito = estatisticaProcessoSeletivoFavorito;
	}

	/**
	 * @return the gabaritoFavorito
	 */
	public Boolean getGabaritoFavorito() {
		return gabaritoFavorito;
	}

	/**
	 * @param gabaritoFavorito the gabaritoFavorito to set
	 */
	public void setGabaritoFavorito(Boolean gabaritoFavorito) {
		this.gabaritoFavorito = gabaritoFavorito;
	}

	/**
	 * @return the contaReceber_SimularAlteracaoFavorito
	 */
	public Boolean getContaReceber_SimularAlteracaoFavorito() {
		return contaReceber_SimularAlteracaoFavorito;
	}

	/**
	 * @param contaReceber_SimularAlteracaoFavorito the contaReceber_SimularAlteracaoFavorito to set
	 */
	public void setContaReceber_SimularAlteracaoFavorito(Boolean contaReceber_SimularAlteracaoFavorito) {
		this.contaReceber_SimularAlteracaoFavorito = contaReceber_SimularAlteracaoFavorito;
	}

	/**
	 * @return the contaReceber_SimularAlteracaoFavoritoFavorito
	 */
	public Boolean getContaReceber_SimularAlteracaoFavoritoFavorito() {
		return contaReceber_SimularAlteracaoFavoritoFavorito;
	}

	/**
	 * @param contaReceber_SimularAlteracaoFavoritoFavorito the contaReceber_SimularAlteracaoFavoritoFavorito to set
	 */
	public void setContaReceber_SimularAlteracaoFavoritoFavorito(Boolean contaReceber_SimularAlteracaoFavoritoFavorito) {
		this.contaReceber_SimularAlteracaoFavoritoFavorito = contaReceber_SimularAlteracaoFavoritoFavorito;
	}

	/**
	 * @return the mapaRecebimentoContaReceberDuplicidadeFavorito
	 */
	public Boolean getMapaRecebimentoContaReceberDuplicidadeFavorito() {
		return mapaRecebimentoContaReceberDuplicidadeFavorito;
	}

	/**
	 * @param mapaRecebimentoContaReceberDuplicidadeFavorito the mapaRecebimentoContaReceberDuplicidadeFavorito to set
	 */
	public void setMapaRecebimentoContaReceberDuplicidadeFavorito(Boolean mapaRecebimentoContaReceberDuplicidadeFavorito) {
		this.mapaRecebimentoContaReceberDuplicidadeFavorito = mapaRecebimentoContaReceberDuplicidadeFavorito;
	}

	/**
	 * @return the demonstrativoResultadoFinanceiroFavorito
	 */
	public Boolean getDemonstrativoResultadoFinanceiroFavorito() {
		return demonstrativoResultadoFinanceiroFavorito;
	}

	/**
	 * @param demonstrativoResultadoFinanceiroFavorito the demonstrativoResultadoFinanceiroFavorito to set
	 */
	public void setDemonstrativoResultadoFinanceiroFavorito(Boolean demonstrativoResultadoFinanceiroFavorito) {
		this.demonstrativoResultadoFinanceiroFavorito = demonstrativoResultadoFinanceiroFavorito;
	}

	/**
	 * @return the planoDisciplinaRelFavorito
	 */
	public Boolean getPlanoDisciplinaRelFavorito() {
		return planoDisciplinaRelFavorito;
	}

	/**
	 * @param planoDisciplinaRelFavorito the planoDisciplinaRelFavorito to set
	 */
	public void setPlanoDisciplinaRelFavorito(Boolean planoDisciplinaRelFavorito) {
		this.planoDisciplinaRelFavorito = planoDisciplinaRelFavorito;
	}

	/**
	 * @return the etiquetaAlunoRelFavorito
	 */
	public Boolean getEtiquetaAlunoRelFavorito() {
		return etiquetaAlunoRelFavorito;
	}

	/**
	 * @param etiquetaAlunoRelFavorito the etiquetaAlunoRelFavorito to set
	 */
	public void setEtiquetaAlunoRelFavorito(Boolean etiquetaAlunoRelFavorito) {
		this.etiquetaAlunoRelFavorito = etiquetaAlunoRelFavorito;
	}

	/**
	 * @return the tipoContatoFavorito
	 */
	public Boolean getTipoContatoFavorito() {
		return tipoContatoFavorito;
	}

	/**
	 * @param tipoContatoFavorito the tipoContatoFavorito to set
	 */
	public void setTipoContatoFavorito(Boolean tipoContatoFavorito) {
		this.tipoContatoFavorito = tipoContatoFavorito;
	}

	/**
	 * @return the questionarioRelFavorito
	 */
	public Boolean getQuestionarioRelFavorito() {
		return questionarioRelFavorito;
	}

	/**
	 * @param questionarioRelFavorito the questionarioRelFavorito to set
	 */
	public void setQuestionarioRelFavorito(Boolean questionarioRelFavorito) {
		this.questionarioRelFavorito = questionarioRelFavorito;
	}

	/**
	 * @return the contaRecebidaVersoContaReceberRelFavorito
	 */
	public Boolean getContaRecebidaVersoContaReceberRelFavorito() {
		return contaRecebidaVersoContaReceberRelFavorito;
	}

	/**
	 * @param contaRecebidaVersoContaReceberRelFavorito the contaRecebidaVersoContaReceberRelFavorito to set
	 */
	public void setContaRecebidaVersoContaReceberRelFavorito(Boolean contaRecebidaVersoContaReceberRelFavorito) {
		this.contaRecebidaVersoContaReceberRelFavorito = contaRecebidaVersoContaReceberRelFavorito;
	}

	/**
	 * @return the declaracaoTransferenciaInternaFavorito
	 */
	public Boolean getDeclaracaoTransferenciaInternaFavorito() {
		return declaracaoTransferenciaInternaFavorito;
	}

	/**
	 * @param declaracaoTransferenciaInternaFavorito the declaracaoTransferenciaInternaFavorito to set
	 */
	public void setDeclaracaoTransferenciaInternaFavorito(Boolean declaracaoTransferenciaInternaFavorito) {
		this.declaracaoTransferenciaInternaFavorito = declaracaoTransferenciaInternaFavorito;
	}

	/**
	 * @return the avaliacaoInstitucionalAnaliticoRelFavorito
	 */
	public Boolean getAvaliacaoInstitucionalAnaliticoRelFavorito() {
		return avaliacaoInstitucionalAnaliticoRelFavorito;
	}

	/**
	 * @param avaliacaoInstitucionalAnaliticoRelFavorito the avaliacaoInstitucionalAnaliticoRelFavorito to set
	 */
	public void setAvaliacaoInstitucionalAnaliticoRelFavorito(Boolean avaliacaoInstitucionalAnaliticoRelFavorito) {
		this.avaliacaoInstitucionalAnaliticoRelFavorito = avaliacaoInstitucionalAnaliticoRelFavorito;
	}

	public Boolean getSegmentacaoProspect() {
		return segmentacaoProspect;
	}

	public void setSegmentacaoProspect(Boolean segmentacaoProspect) {
		this.segmentacaoProspect = segmentacaoProspect;
	}

	public Boolean getProcessarResultadoProcessoSeletivo() {
		return processarResultadoProcessoSeletivo;
	}

	public void setProcessarResultadoProcessoSeletivo(Boolean processarResultadoProcessoSeletivo) {
		this.processarResultadoProcessoSeletivo = processarResultadoProcessoSeletivo;
	}

	public Boolean getProcessarResultadoProcessoSeletivoFavorito() {
		return processarResultadoProcessoSeletivoFavorito;
	}

	public void setProcessarResultadoProcessoSeletivoFavorito(Boolean processarResultadoProcessoSeletivoFavorito) {
		this.processarResultadoProcessoSeletivoFavorito = processarResultadoProcessoSeletivoFavorito;
	}

	public Boolean getPermiteAlterarUnidadeEnsinoCertificadora() {
		return permiteAlterarUnidadeEnsinoCertificadora;
	}

	public void setPermiteAlterarUnidadeEnsinoCertificadora(Boolean permiteAlterarUnidadeEnsinoCertificadora) {
		this.permiteAlterarUnidadeEnsinoCertificadora = permiteAlterarUnidadeEnsinoCertificadora;
	}

	public Boolean getEntregaBoletosAlunoRel() {
		return entregaBoletosAlunoRel;
	}

	public void setEntregaBoletosAlunoRel(Boolean entregaBoletosAlunoRel) {
		this.entregaBoletosAlunoRel = entregaBoletosAlunoRel;
	}

	public Boolean getEntregaBoletosAlunoRelFavorito() {
		return entregaBoletosAlunoRelFavorito;
	}

	public void setEntregaBoletosAlunoRelFavorito(Boolean entregaBoletosAlunoRelFavorito) {
		this.entregaBoletosAlunoRelFavorito = entregaBoletosAlunoRelFavorito;
	}

	public Boolean getAlunosPorDisciplinasRel() {
		return alunosPorDisciplinasRel;
	}

	public void setAlunosPorDisciplinasRel(Boolean alunosPorDisciplinasRel) {
		this.alunosPorDisciplinasRel = alunosPorDisciplinasRel;
	}

	public Boolean getAlunosPorDisciplinasRelFavorito() {
		return alunosPorDisciplinasRelFavorito;
	}

	public void setAlunosPorDisciplinasRelFavorito(Boolean alunosPorDisciplinasRelFavorito) {
		this.alunosPorDisciplinasRelFavorito = alunosPorDisciplinasRelFavorito;
	}

	/**
	 * @return the mapaControleEntregaDocumentoUpload
	 */
	public Boolean getMapaControleEntregaDocumentoUpload() {
		return mapaControleEntregaDocumentoUpload;
	}

	/**
	 * @param mapaControleEntregaDocumentoUpload the mapaControleEntregaDocumentoUpload to set
	 */
	public void setMapaControleEntregaDocumentoUpload(Boolean mapaControleEntregaDocumentoUpload) {
		this.mapaControleEntregaDocumentoUpload = mapaControleEntregaDocumentoUpload;
	}

	/**
	 * @return
	 */
	public Boolean getAssinarDocumentoAlunoEntregue() {
		return assinarDocumentoAlunoEntregue;
	}

	/**
	 * @param assinarDocumentoAlunoEntregue
	 */
	public void setAssinarDocumentoAlunoEntregue(Boolean assinarDocumentoAlunoEntregue) {
		this.assinarDocumentoAlunoEntregue = assinarDocumentoAlunoEntregue;
	}

	/**
	 * @return
	 */
	public Boolean getAssinarDocumentoAlunoEntregueFavorito() {
		return assinarDocumentoAlunoEntregueFavorito;
	}

	/**
	 * @param assinarDocumentoAlunoEntregueFavorito
	 */
	public void setAssinarDocumentoAlunoEntregueFavorito(Boolean assinarDocumentoAlunoEntregueFavorito) {
		this.assinarDocumentoAlunoEntregueFavorito = assinarDocumentoAlunoEntregueFavorito;
	}

	/**
	 * @return the mapaControleEntregaDocumentoUploadFavorito
	 */
	public Boolean getMapaControleEntregaDocumentoUploadFavorito() {
		return mapaControleEntregaDocumentoUploadFavorito;
	}

	/**
	 * @param mapaControleEntregaDocumentoUploadFavorito the mapaControleEntregaDocumentoUploadFavorito to set
	 */
	public void setMapaControleEntregaDocumentoUploadFavorito(Boolean mapaControleEntregaDocumentoUploadFavorito) {
		this.mapaControleEntregaDocumentoUploadFavorito = mapaControleEntregaDocumentoUploadFavorito;
	}

	public Boolean getSegmentacaoProspectFavorito() {
		return segmentacaoProspectFavorito;
	}

	public void setSegmentacaoProspectFavorito(Boolean segmentacaoProspectFavorito) {
		this.segmentacaoProspectFavorito = segmentacaoProspectFavorito;
	}

	public Boolean getInclusaoDisciplinaForaGrade() {
		return inclusaoDisciplinaForaGrade;
	}

	public void setInclusaoDisciplinaForaGrade(Boolean inclusaoDisciplinaForaGrade) {
		this.inclusaoDisciplinaForaGrade = inclusaoDisciplinaForaGrade;
	}

	public Boolean getInclusaoDisciplinaForaGradeFavorito() {
		return inclusaoDisciplinaForaGradeFavorito;
	}

	public void setInclusaoDisciplinaForaGradeFavorito(Boolean inclusaoDisciplinaForaGradeFavorito) {
		this.inclusaoDisciplinaForaGradeFavorito = inclusaoDisciplinaForaGradeFavorito;
	}

	public Boolean getCartaoResposta() {
		return cartaoResposta;
	}

	public void setCartaoResposta(Boolean cartaoResposta) {
		this.cartaoResposta = cartaoResposta;
	}

	public Boolean getCartaoRespostaFavorito() {
		return cartaoRespostaFavorito;
	}

	public void setCartaoRespostaFavorito(Boolean cartaoRespostaFavorito) {
		this.cartaoRespostaFavorito = cartaoRespostaFavorito;
	}

	public Boolean getPermitirApenasContasDaBiblioteca() {
		return permitirApenasContasDaBiblioteca;
	}

	public void setPermitirApenasContasDaBiblioteca(Boolean permitirApenasContasDaBiblioteca) {
		this.permitirApenasContasDaBiblioteca = permitirApenasContasDaBiblioteca;
	}

	public Boolean getTrabalhoConclusaoCurso() {
		return trabalhoConclusaoCurso;
	}

	public void setTrabalhoConclusaoCurso(Boolean trabalhoConclusaoCurso) {
		this.trabalhoConclusaoCurso = trabalhoConclusaoCurso;
	}

	public Boolean getTrabalhoConclusaoCursoFavorito() {
		return trabalhoConclusaoCursoFavorito;
	}

	public void setTrabalhoConclusaoCursoFavorito(Boolean trabalhoConclusaoCursoFavorito) {
		this.trabalhoConclusaoCursoFavorito = trabalhoConclusaoCursoFavorito;
	}

	public Boolean getRegraComissionamentoRanking() {
		return regraComissionamentoRanking;
	}

	public void setRegraComissionamentoRanking(Boolean regraComissionamentoRanking) {
		this.regraComissionamentoRanking = regraComissionamentoRanking;
	}

	public Boolean getRegraComissionamentoRankingFavorito() {
		if (regraComissionamentoRankingFavorito == null) {
			regraComissionamentoRankingFavorito = false;
		}
		return regraComissionamentoRankingFavorito;
	}

	public void setRegraComissionamentoRankingFavorito(Boolean regraComissionamentoRankingFavorito) {
		this.regraComissionamentoRankingFavorito = regraComissionamentoRankingFavorito;
	}

	public Boolean getCoordenadorTCC() {
		return coordenadorTCC;
	}

	public void setCoordenadorTCC(Boolean coordenadorTCC) {
		this.coordenadorTCC = coordenadorTCC;
	}

	public Boolean getCoordenadorTCCFavorito() {
		return coordenadorTCCFavorito;
	}

	public void setCoordenadorTCCFavorito(Boolean coordenadorTCCFavorito) {
		this.coordenadorTCCFavorito = coordenadorTCCFavorito;
	}

	public Boolean getRetornarTCCFaseAnterior() {
		return retornarTCCFaseAnterior;
	}

	public void setRetornarTCCFaseAnterior(Boolean retornarTCCFaseAnterior) {
		this.retornarTCCFaseAnterior = retornarTCCFaseAnterior;
	}

	public Boolean getExtenderPrazoExecucaoTCC() {
		return extenderPrazoExecucaoTCC;
	}

	public void setExtenderPrazoExecucaoTCC(Boolean extenderPrazoExecucaoTCC) {
		this.extenderPrazoExecucaoTCC = extenderPrazoExecucaoTCC;
	}

	public Boolean getLancarNotasTCC() {
		return lancarNotasTCC;
	}

	public void setLancarNotasTCC(Boolean lancarNotasTCC) {
		this.lancarNotasTCC = lancarNotasTCC;
	}

	public Boolean getAprovarPlanoTCC() {
		return aprovarPlanoTCC;
	}

	public void setAprovarPlanoTCC(Boolean aprovarPlanoTCC) {
		this.aprovarPlanoTCC = aprovarPlanoTCC;
	}

	public Boolean getAprovarElaboracaoTCC() {
		return aprovarElaboracaoTCC;
	}

	public void setAprovarElaboracaoTCC(Boolean aprovarElaboracaoTCC) {
		this.aprovarElaboracaoTCC = aprovarElaboracaoTCC;
	}

	public Boolean getEncaminharTCC() {
		return encaminharTCC;
	}

	public void setEncaminharTCC(Boolean encaminharTCC) {
		this.encaminharTCC = encaminharTCC;
	}

	public Boolean getDefinirOrientadorTCC() {
		return definirOrientadorTCC;
	}

	public void setDefinirOrientadorTCC(Boolean definirOrientadorTCC) {
		this.definirOrientadorTCC = definirOrientadorTCC;
	}

	public Boolean getSolicitarNovoArquivoTCC() {
		return solicitarNovoArquivoTCC;
	}

	public void setSolicitarNovoArquivoTCC(Boolean solicitarNovoArquivoTCC) {
		this.solicitarNovoArquivoTCC = solicitarNovoArquivoTCC;
	}

	public Boolean getReprovarTCC() {
		return reprovarTCC;
	}

	public void setReprovarTCC(Boolean reprovarTCC) {
		this.reprovarTCC = reprovarTCC;
	}

	public Boolean getRegistrarArtefatoTCC() {
		return registrarArtefatoTCC;
	}

	public void setRegistrarArtefatoTCC(Boolean registrarArtefatoTCC) {
		this.registrarArtefatoTCC = registrarArtefatoTCC;
	}

	public Boolean getMembroBancaTCC() {
		return membroBancaTCC;
	}

	public void setMembroBancaTCC(Boolean membroBancaTCC) {
		this.membroBancaTCC = membroBancaTCC;
	}

	public Boolean getRevisarPlanoTCC() {
		return revisarPlanoTCC;
	}

	public void setRevisarPlanoTCC(Boolean revisarPlanoTCC) {
		this.revisarPlanoTCC = revisarPlanoTCC;
	}

	public Boolean getPostarArquivoTCC() {
		return postarArquivoTCC;
	}

	public void setPostarArquivoTCC(Boolean postarArquivoTCC) {
		this.postarArquivoTCC = postarArquivoTCC;
	}

	public Boolean getCalendarioAgrupamentoTcc() {
		return calendarioAgrupamentoTcc;
	}

	public void setCalendarioAgrupamentoTcc(Boolean calendarioAgrupamentoTcc) {
		this.calendarioAgrupamentoTcc = calendarioAgrupamentoTcc;
	}

	public Boolean getCalendarioAgrupamentoTccFavorito() {
		return calendarioAgrupamentoTccFavorito;
	}

	public void setCalendarioAgrupamentoTccFavorito(Boolean calendarioAgrupamentoTccFavorito) {
		this.calendarioAgrupamentoTccFavorito = calendarioAgrupamentoTccFavorito;
	}

	public Boolean getPainelGestorMonitorarConsultor() {
		if (painelGestorMonitorarConsultor == null) {
			painelGestorMonitorarConsultor = Boolean.FALSE;
		}
		return painelGestorMonitorarConsultor;
	}

	public void setPainelGestorMonitorarConsultor(Boolean painelGestorMonitorarConsultor) {
		this.painelGestorMonitorarConsultor = painelGestorMonitorarConsultor;
	}

	public Boolean getResultadoQuestionarioProcessoSeletivoRelFavorito() {
		return resultadoQuestionarioProcessoSeletivoRelFavorito;
	}

	public void setResultadoQuestionarioProcessoSeletivoRelFavorito(Boolean resultadoQuestionarioProcessoSeletivoRelFavorito) {
		this.resultadoQuestionarioProcessoSeletivoRelFavorito = resultadoQuestionarioProcessoSeletivoRelFavorito;
	}

	public Boolean getResultadoQuestionarioProcessoSeletivoRel() {
		return resultadoQuestionarioProcessoSeletivoRel;
	}

	public void setResultadoQuestionarioProcessoSeletivoRel(Boolean resultadoQuestionarioProcessoSeletivoRel) {
		this.resultadoQuestionarioProcessoSeletivoRel = resultadoQuestionarioProcessoSeletivoRel;
	}

	public Boolean getEstatisticaProcessoSeletivoRelDadosCandidato() {
		return estatisticaProcessoSeletivoRelDadosCandidato;
	}

	public void setEstatisticaProcessoSeletivoRelDadosCandidato(Boolean estatisticaProcessoSeletivoRelDadosCandidato) {
		this.estatisticaProcessoSeletivoRelDadosCandidato = estatisticaProcessoSeletivoRelDadosCandidato;
	}

	public Boolean getEstatisticaProcessoSeletivoRelInscritoBairro() {
		return estatisticaProcessoSeletivoRelInscritoBairro;
	}

	public void setEstatisticaProcessoSeletivoRelInscritoBairro(Boolean estatisticaProcessoSeletivoRelInscritoBairro) {
		this.estatisticaProcessoSeletivoRelInscritoBairro = estatisticaProcessoSeletivoRelInscritoBairro;
	}

	public Boolean getEstatisticaProcessoSeletivoRelInscritoCurso() {
		return estatisticaProcessoSeletivoRelInscritoCurso;
	}

	public void setEstatisticaProcessoSeletivoRelInscritoCurso(Boolean estatisticaProcessoSeletivoRelInscritoCurso) {
		this.estatisticaProcessoSeletivoRelInscritoCurso = estatisticaProcessoSeletivoRelInscritoCurso;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaFrequencia() {
		return estatisticaProcessoSeletivoRelListaFrequencia;
	}

	public void setEstatisticaProcessoSeletivoRelListaFrequencia(Boolean estatisticaProcessoSeletivoRelListaFrequencia) {
		this.estatisticaProcessoSeletivoRelListaFrequencia = estatisticaProcessoSeletivoRelListaFrequencia;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaAprovado() {
		return estatisticaProcessoSeletivoRelListaAprovado;
	}

	public void setEstatisticaProcessoSeletivoRelListaAprovado(Boolean estatisticaProcessoSeletivoRelListaAprovado) {
		this.estatisticaProcessoSeletivoRelListaAprovado = estatisticaProcessoSeletivoRelListaAprovado;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaReprovado() {
		return estatisticaProcessoSeletivoRelListaReprovado;
	}

	public void setEstatisticaProcessoSeletivoRelListaReprovado(Boolean estatisticaProcessoSeletivoRelListaReprovado) {
		this.estatisticaProcessoSeletivoRelListaReprovado = estatisticaProcessoSeletivoRelListaReprovado;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaNaoMatriculado() {
		return estatisticaProcessoSeletivoRelListaNaoMatriculado;
	}

	public void setEstatisticaProcessoSeletivoRelListaNaoMatriculado(Boolean estatisticaProcessoSeletivoRelListaNaoMatriculado) {
		this.estatisticaProcessoSeletivoRelListaNaoMatriculado = estatisticaProcessoSeletivoRelListaNaoMatriculado;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaMatriculado() {
		return estatisticaProcessoSeletivoRelListaMatriculado;
	}

	public void setEstatisticaProcessoSeletivoRelListaMatriculado(Boolean estatisticaProcessoSeletivoRelListaMatriculado) {
		this.estatisticaProcessoSeletivoRelListaMatriculado = estatisticaProcessoSeletivoRelListaMatriculado;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaAusente() {
		return estatisticaProcessoSeletivoRelListaAusente;
	}

	public void setEstatisticaProcessoSeletivoRelListaAusente(Boolean estatisticaProcessoSeletivoRelListaAusente) {
		this.estatisticaProcessoSeletivoRelListaAusente = estatisticaProcessoSeletivoRelListaAusente;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaClassificado() {
		return estatisticaProcessoSeletivoRelListaClassificado;
	}

	public void setEstatisticaProcessoSeletivoRelListaClassificado(Boolean estatisticaProcessoSeletivoRelListaClassificado) {
		this.estatisticaProcessoSeletivoRelListaClassificado = estatisticaProcessoSeletivoRelListaClassificado;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno() {
		return estatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno;
	}

	public void setEstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno(Boolean estatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno) {
		this.estatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno = estatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno;
	}

	public Boolean getProcSeletivoProvasRel() {
		return procSeletivoProvasRel;
	}

	public void setProcSeletivoProvasRel(Boolean procSeletivoProvasRel) {
		this.procSeletivoProvasRel = procSeletivoProvasRel;
	}

	public Boolean getProcSeletivoProvasRelFavorito() {
		return procSeletivoProvasRelFavorito;
	}

	public void setProcSeletivoProvasRelFavorito(Boolean procSeletivoProvasRelFavorito) {
		this.procSeletivoProvasRelFavorito = procSeletivoProvasRelFavorito;
	}

	public Boolean getEstatisticaProcessoSeletivoRelListaMuralCandidato() {
		return estatisticaProcessoSeletivoRelListaMuralCandidato;
	}

	public void setEstatisticaProcessoSeletivoRelListaMuralCandidato(Boolean estatisticaProcessoSeletivoRelListaMuralCandidato) {
		this.estatisticaProcessoSeletivoRelListaMuralCandidato = estatisticaProcessoSeletivoRelListaMuralCandidato;
	}

	public Boolean getAlterarTodosPlanoEnsino() {
		return alterarTodosPlanoEnsino;
	}

	public void setAlterarTodosPlanoEnsino(Boolean alterarTodosPlanoEnsino) {
		this.alterarTodosPlanoEnsino = alterarTodosPlanoEnsino;
	}

	public Boolean getAlterarApenasUltimoPlanoEnsino() {
		return alterarApenasUltimoPlanoEnsino;
	}

	public void setAlterarApenasUltimoPlanoEnsino(Boolean alterarApenasUltimoPlanoEnsino) {
		this.alterarApenasUltimoPlanoEnsino = alterarApenasUltimoPlanoEnsino;
	}

	public Boolean getCriarNovoPlanoEnsino() {
		return criarNovoPlanoEnsino;
	}

	public void setCriarNovoPlanoEnsino(Boolean criarNovoPlanoEnsino) {
		this.criarNovoPlanoEnsino = criarNovoPlanoEnsino;
	}

	public Boolean getNotificacaoProcessoSeletivo() {
		return notificacaoProcessoSeletivo;
	}

	public void setNotificacaoProcessoSeletivo(Boolean notificacaoProcessoSeletivo) {
		this.notificacaoProcessoSeletivo = notificacaoProcessoSeletivo;
	}

	public Boolean getNotificacaoProcessoSeletivoFavorito() {
		return notificacaoProcessoSeletivoFavorito;
	}

	public void setNotificacaoProcessoSeletivoFavorito(Boolean notificacaoProcessoSeletivoFavorito) {
		this.notificacaoProcessoSeletivoFavorito = notificacaoProcessoSeletivoFavorito;
	}

	public Boolean getRecebimentoPorUnidadeCursoTurmaRel() {
		return recebimentoPorUnidadeCursoTurmaRel;
	}

	public void setRecebimentoPorUnidadeCursoTurmaRel(Boolean recebimentoPorUnidadeCursoTurmaRel) {
		this.recebimentoPorUnidadeCursoTurmaRel = recebimentoPorUnidadeCursoTurmaRel;
	}

	public Boolean getRecebimentoPorUnidadeCursoTurmaRelFavorito() {
		return recebimentoPorUnidadeCursoTurmaRelFavorito;
	}

	public void setRecebimentoPorUnidadeCursoTurmaRelFavorito(Boolean recebimentoPorUnidadeCursoTurmaRelFavorito) {
		this.recebimentoPorUnidadeCursoTurmaRelFavorito = recebimentoPorUnidadeCursoTurmaRelFavorito;
	}

	public Boolean getPermitirAlterarValorContaReceber() {
		return permitirAlterarValorContaReceber;
	}

	public void setPermitirAlterarValorContaReceber(Boolean permitirAlterarValorContaReceber) {
		this.permitirAlterarValorContaReceber = permitirAlterarValorContaReceber;
	}

	public Boolean getProcessoSeletivoOcorrenciaRel() {
		return processoSeletivoOcorrenciaRel;
	}

	public void setProcessoSeletivoOcorrenciaRel(Boolean processoSeletivoOcorrenciaRel) {
		this.processoSeletivoOcorrenciaRel = processoSeletivoOcorrenciaRel;
	}

	public Boolean getProcessoSeletivoOcorrenciaRelFavorito() {
		return processoSeletivoOcorrenciaRelFavorito;
	}

	public void setProcessoSeletivoOcorrenciaRelFavorito(Boolean processoSeletivoOcorrenciaRelFavorito) {
		this.processoSeletivoOcorrenciaRelFavorito = processoSeletivoOcorrenciaRelFavorito;
	}

	public Boolean getImprimirBoletoVencidoVisaoAluno() {
		return imprimirBoletoVencidoVisaoAluno;
	}

	public void setImprimirBoletoVencidoVisaoAluno(Boolean imprimirBoletoVencidoVisaoAluno) {
		this.imprimirBoletoVencidoVisaoAluno = imprimirBoletoVencidoVisaoAluno;
	}

	public Boolean getPermitirApenasRelatorioRecebimentoBiblioteca() {
		return permitirApenasRelatorioRecebimentoBiblioteca;
	}

	public void setPermitirApenasRelatorioRecebimentoBiblioteca(Boolean permitirApenasRelatorioRecebimentoBiblioteca) {
		this.permitirApenasRelatorioRecebimentoBiblioteca = permitirApenasRelatorioRecebimentoBiblioteca;
	}

	public Boolean getPermitirApenasRelatorioContaReceberBiblioteca() {
		return permitirApenasRelatorioContaReceberBiblioteca;
	}

	public void setPermitirApenasRelatorioContaReceberBiblioteca(Boolean permitirApenasRelatorioContaReceberBiblioteca) {
		this.permitirApenasRelatorioContaReceberBiblioteca = permitirApenasRelatorioContaReceberBiblioteca;
	}

	public Boolean getPermiteAlterarFuncionarioResponsavel() {

		return permiteAlterarFuncionarioResponsavel;
	}

	public void setPermiteAlterarFuncionarioResponsavel(Boolean permiteAlterarFuncionarioResponsavel) {
		this.permiteAlterarFuncionarioResponsavel = permiteAlterarFuncionarioResponsavel;
	}

	public Boolean getPerguntaRequerimento() {
		return perguntaRequerimento;
	}

	public void setPerguntaRequerimento(Boolean perguntaRequerimento) {
		this.perguntaRequerimento = perguntaRequerimento;
	}

	public Boolean getQuestionarioRequerimento() {
		return questionarioRequerimento;
	}

	public void setQuestionarioRequerimento(Boolean questionarioRequerimento) {
		this.questionarioRequerimento = questionarioRequerimento;
	}

	public Boolean getPerguntaRequerimentoFavorito() {
		return perguntaRequerimentoFavorito;
	}

	public void setPerguntaRequerimentoFavorito(Boolean perguntaRequerimentoFavorito) {
		this.perguntaRequerimentoFavorito = perguntaRequerimentoFavorito;
	}

	public Boolean getQuestionarioRequerimentoFavorito() {
		return questionarioRequerimentoFavorito;
	}

	public void setQuestionarioRequerimentoFavorito(Boolean questionarioRequerimentoFavorito) {
		this.questionarioRequerimentoFavorito = questionarioRequerimentoFavorito;
	}

	public Boolean getQuestionarioAvaliacaoInstitucional() {
		return questionarioAvaliacaoInstitucional;
	}

	public void setQuestionarioAvaliacaoInstitucional(Boolean questionarioAvaliacaoInstitucional) {
		this.questionarioAvaliacaoInstitucional = questionarioAvaliacaoInstitucional;
	}

	public Boolean getQuestionarioAvaliacaoInstitucionalFavorito() {
		return questionarioAvaliacaoInstitucionalFavorito;
	}

	public void setQuestionarioAvaliacaoInstitucionalFavorito(Boolean questionarioAvaliacaoInstitucionalFavorito) {
		this.questionarioAvaliacaoInstitucionalFavorito = questionarioAvaliacaoInstitucionalFavorito;
	}

	public Boolean getPerguntaAvaliacaoInstitucional() {
		return perguntaAvaliacaoInstitucional;
	}

	public void setPerguntaAvaliacaoInstitucional(Boolean perguntaAvaliacaoInstitucional) {
		this.perguntaAvaliacaoInstitucional = perguntaAvaliacaoInstitucional;
	}

	public Boolean getPerguntaAvaliacaoInstitucionalFavorito() {
		return perguntaAvaliacaoInstitucionalFavorito;
	}

	public void setPerguntaAvaliacaoInstitucionalFavorito(Boolean perguntaAvaliacaoInstitucionalFavorito) {
		this.perguntaAvaliacaoInstitucionalFavorito = perguntaAvaliacaoInstitucionalFavorito;
	}

	public Boolean getQuestionarioProcessoSeletivo() {
		return questionarioProcessoSeletivo;
	}

	public void setQuestionarioProcessoSeletivo(Boolean questionarioProcessoSeletivo) {
		this.questionarioProcessoSeletivo = questionarioProcessoSeletivo;
	}

	public Boolean getQuestionarioProcessoSeletivoFavorito() {
		return questionarioProcessoSeletivoFavorito;
	}

	public void setQuestionarioProcessoSeletivoFavorito(Boolean questionarioProcessoSeletivoFavorito) {
		this.questionarioProcessoSeletivoFavorito = questionarioProcessoSeletivoFavorito;
	}

	public Boolean getPerguntaProcessoSeletivo() {
		return perguntaProcessoSeletivo;
	}

	public void setPerguntaProcessoSeletivo(Boolean perguntaProcessoSeletivo) {
		this.perguntaProcessoSeletivo = perguntaProcessoSeletivo;
	}

	public Boolean getPerguntaProcessoSeletivoFavorito() {
		return perguntaProcessoSeletivoFavorito;
	}

	public void setPerguntaProcessoSeletivoFavorito(Boolean perguntaProcessoSeletivoFavorito) {
		this.perguntaProcessoSeletivoFavorito = perguntaProcessoSeletivoFavorito;
	}

	public Boolean getPermitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina() {
		return permitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina;
	}

	public void setPermitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina(Boolean permitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina) {
		this.permitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina = permitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina;
	}

	public Boolean getCasosEspeciaisRel() {
		return casosEspeciaisRel;
	}

	public void setCasosEspeciaisRel(Boolean casosEspeciaisRel) {
		this.casosEspeciaisRel = casosEspeciaisRel;
	}

	public Boolean getCasosEspeciaisRelFavorito() {
		return casosEspeciaisRelFavorito;
	}

	public void setCasosEspeciaisRelFavorito(Boolean casosEspeciaisRelFavorito) {
		this.casosEspeciaisRelFavorito = casosEspeciaisRelFavorito;
	}

	public Boolean getLiberarInscricaoForaPrazo() {
		return liberarInscricaoForaPrazo;
	}

	public void setLiberarInscricaoForaPrazo(Boolean liberarInscricaoForaPrazo) {
		this.liberarInscricaoForaPrazo = liberarInscricaoForaPrazo;
	}

	public Boolean getAlterarAproveitamentoDisciplina() {
		return alterarAproveitamentoDisciplina;
	}

	public void setAlterarAproveitamentoDisciplina(Boolean alterarAproveitamentoDisciplina) {
		this.alterarAproveitamentoDisciplina = alterarAproveitamentoDisciplina;
	}

	public Boolean getAlterarAproveitamentoDisciplinaFavorito() {
		return alterarAproveitamentoDisciplinaFavorito;
	}

	public void setAlterarAproveitamentoDisciplinaFavorito(Boolean alterarAproveitamentoDisciplinaFavorito) {
		this.alterarAproveitamentoDisciplinaFavorito = alterarAproveitamentoDisciplinaFavorito;
	}

	public Boolean getTipoAdvertencia() {
		return tipoAdvertencia;
	}

	public void setTipoAdvertencia(Boolean tipoAdvertencia) {
		this.tipoAdvertencia = tipoAdvertencia;
	}

	public Boolean getAdvertencia() {
		return advertencia;
	}

	public void setAdvertencia(Boolean advertencia) {
		this.advertencia = advertencia;
	}

	public Boolean getTipoAdvertenciaFavorito() {
		return tipoAdvertenciaFavorito;
	}

	public void setTipoAdvertenciaFavorito(Boolean tipoAdvertenciaFavorito) {
		this.tipoAdvertenciaFavorito = tipoAdvertenciaFavorito;
	}

	public Boolean getAdvertenciaFavorito() {
		return advertenciaFavorito;
	}

	public void setAdvertenciaFavorito(Boolean advertenciaFavorito) {
		this.advertenciaFavorito = advertenciaFavorito;
	}

	public Boolean getPermiteLancamentoAulaFutura() {
		return permiteLancamentoAulaFutura;
	}

	public void setPermiteLancamentoAulaFutura(Boolean permiteLancamentoAulaFutura) {
		this.permiteLancamentoAulaFutura = permiteLancamentoAulaFutura;
	}

	public Boolean getPermiteLancamentoAulaFuturaProfessor() {
		return permiteLancamentoAulaFuturaProfessor;
	}

	public void setPermiteLancamentoAulaFuturaProfessor(Boolean permiteLancamentoAulaFuturaProfessor) {
		this.permiteLancamentoAulaFuturaProfessor = permiteLancamentoAulaFuturaProfessor;
	}

	public Boolean getPermiteLancamentoAulaFuturaCoordenador() {
		return permiteLancamentoAulaFuturaCoordenador;
	}

	public void setPermiteLancamentoAulaFuturaCoordenador(Boolean permiteLancamentoAulaFuturaCoordenador) {
		this.permiteLancamentoAulaFuturaCoordenador = permiteLancamentoAulaFuturaCoordenador;
	}

	public Boolean getPermiteLancamentoAtividadeComplementarFutura() {

		return permiteLancamentoAtividadeComplementarFutura;
	}

	public void setPermiteLancamentoAtividadeComplementarFutura(Boolean permiteLancamentoAtividadeComplementarFutura) {
		this.permiteLancamentoAtividadeComplementarFutura = permiteLancamentoAtividadeComplementarFutura;
	}

	public Boolean getAlterarHistoricoGradeAnterior() {
		return alterarHistoricoGradeAnterior;
	}

	public void setAlterarHistoricoGradeAnterior(Boolean alterarHistoricoGradeAnterior) {
		this.alterarHistoricoGradeAnterior = alterarHistoricoGradeAnterior;
	}

	public Boolean getAlterarHistoricoGradeAnteriorFavorito() {
		return alterarHistoricoGradeAnteriorFavorito;
	}

	public void setAlterarHistoricoGradeAnteriorFavorito(Boolean alterarHistoricoGradeAnteriorFavorito) {
		this.alterarHistoricoGradeAnteriorFavorito = alterarHistoricoGradeAnteriorFavorito;
	}

	public Boolean getContaReceberAgrupada() {
		return contaReceberAgrupada;
	}

	public void setContaReceberAgrupada(Boolean contaReceberAgrupada) {
		this.contaReceberAgrupada = contaReceberAgrupada;
	}

	public Boolean getContaReceberAgrupadaFavorito() {
		return contaReceberAgrupadaFavorito;
	}

	public void setContaReceberAgrupadaFavorito(Boolean contaReceberAgrupadaFavorito) {
		this.contaReceberAgrupadaFavorito = contaReceberAgrupadaFavorito;
	}

	public Boolean getContaReceberAgrupada_PermitirProcessarAgrupamento() {
		return contaReceberAgrupada_PermitirProcessarAgrupamento;
	}

	public void setContaReceberAgrupada_PermitirProcessarAgrupamento(Boolean contaReceberAgrupada_PermitirProcessarAgrupamento) {
		this.contaReceberAgrupada_PermitirProcessarAgrupamento = contaReceberAgrupada_PermitirProcessarAgrupamento;
	}

	public Boolean getTipoAtividadeComplementar() {
		return tipoAtividadeComplementar;
	}

	public void setTipoAtividadeComplementar(Boolean tipoAtividadeComplementar) {
		this.tipoAtividadeComplementar = tipoAtividadeComplementar;
	}

	public Boolean getTipoAtividadeComplementarFavorito() {
		return tipoAtividadeComplementarFavorito;
	}

	public void setTipoAtividadeComplementarFavorito(Boolean tipoAtividadeComplementarFavorito) {
		this.tipoAtividadeComplementarFavorito = tipoAtividadeComplementarFavorito;
	}

	public Boolean getRegistroAtividadeComplementar() {
		return registroAtividadeComplementar;
	}

	public void setRegistroAtividadeComplementar(Boolean registroAtividadeComplementar) {
		this.registroAtividadeComplementar = registroAtividadeComplementar;
	}

	public Boolean getRegistroAtividadeComplementarFavorito() {
		return registroAtividadeComplementarFavorito;
	}

	public void setRegistroAtividadeComplementarFavorito(Boolean registroAtividadeComplementarFavorito) {
		this.registroAtividadeComplementarFavorito = registroAtividadeComplementarFavorito;
	}

	public Boolean getAcompanhamentoAtividadeComplementar() {
		return acompanhamentoAtividadeComplementar;
	}

	public void setAcompanhamentoAtividadeComplementar(Boolean acompanhamentoAtividadeComplementar) {
		this.acompanhamentoAtividadeComplementar = acompanhamentoAtividadeComplementar;
	}

	public Boolean getAcompanhamentoAtividadeComplementarFavorito() {
		return acompanhamentoAtividadeComplementarFavorito;
	}

	public void setAcompanhamentoAtividadeComplementarFavorito(Boolean acompanhamentoAtividadeComplementarFavorito) {
		this.acompanhamentoAtividadeComplementarFavorito = acompanhamentoAtividadeComplementarFavorito;
	}

	public Boolean getAtividadeComplementarAluno() {
		return atividadeComplementarAluno;
	}

	public void setAtividadeComplementarAluno(Boolean atividadeComplementarAluno) {
		this.atividadeComplementarAluno = atividadeComplementarAluno;
	}

	public Boolean getRegistroAtividadeComplementarCoordenador() {
		return registroAtividadeComplementarCoordenador;
	}

	public void setRegistroAtividadeComplementarCoordenador(Boolean registroAtividadeComplementarCoordenador) {
		this.registroAtividadeComplementarCoordenador = registroAtividadeComplementarCoordenador;
	}

	public Boolean getAcompanhamentoAtividadeComplementarCoordenador() {
		return acompanhamentoAtividadeComplementarCoordenador;
	}

	public void setAcompanhamentoAtividadeComplementarCoordenador(Boolean acompanhamentoAtividadeComplementarCoordenador) {
		this.acompanhamentoAtividadeComplementarCoordenador = acompanhamentoAtividadeComplementarCoordenador;
	}

	public Boolean getRecebimentoCompraRel() {
		return recebimentoCompraRel;
	}

	public void setRecebimentoCompraRel(Boolean recebimentoCompraRel) {
		this.recebimentoCompraRel = recebimentoCompraRel;
	}

	public Boolean getRecebimentoCompraRelFavorito() {
		return recebimentoCompraRelFavorito;
	}

	public void setRecebimentoCompraRelFavorito(Boolean recebimentoCompraRelFavorito) {
		this.recebimentoCompraRelFavorito = recebimentoCompraRelFavorito;
	}

	public Boolean getAtaResultadosFinais() {
		return ataResultadosFinais;
	}

	public void setAtaResultadosFinais(Boolean ataResultadosFinais) {
		this.ataResultadosFinais = ataResultadosFinais;
	}

	public Boolean getAtaResultadosFinaisFavorito() {
		return ataResultadosFinaisFavorito;
	}

	public void setAtaResultadosFinaisFavorito(Boolean ataResultadosFinaisFavorito) {
		this.ataResultadosFinaisFavorito = ataResultadosFinaisFavorito;
	}

	public Boolean getPermitirRegistrarAulaRetroativo() {
		return permitirRegistrarAulaRetroativo;
	}

	public void setPermitirRegistrarAulaRetroativo(Boolean permitirRegistrarAulaRetroativo) {
		this.permitirRegistrarAulaRetroativo = permitirRegistrarAulaRetroativo;
	}

	public Boolean getPermitirGerarAtaProvaRetroativo() {
		return permitirGerarAtaProvaRetroativo;
	}

	public void setPermitirGerarAtaProvaRetroativo(Boolean permitirGerarAtaProvaRetroativo) {
		this.permitirGerarAtaProvaRetroativo = permitirGerarAtaProvaRetroativo;
	}

	public Boolean getPermitirConsultarPlanoEnsinoAnterior() {
		return permitirConsultarPlanoEnsinoAnterior;
	}

	public void setPermitirConsultarPlanoEnsinoAnterior(Boolean permitirConsultarPlanoEnsinoAnterior) {
		this.permitirConsultarPlanoEnsinoAnterior = permitirConsultarPlanoEnsinoAnterior;
	}

	public Boolean getPermitirLancarNotaRetroativo() {
		return permitirLancarNotaRetroativo;
	}

	public void setPermitirLancarNotaRetroativo(Boolean permitirLancarNotaRetroativo) {
		this.permitirLancarNotaRetroativo = permitirLancarNotaRetroativo;
	}

	public Boolean getDistribuicaoSubturma() {
		return distribuicaoSubturma;
	}

	public void setDistribuicaoSubturma(Boolean distribuicaoSubturma) {
		this.distribuicaoSubturma = distribuicaoSubturma;
	}

	public Boolean getDistribuicaoSubturmaFavorito() {
		return distribuicaoSubturmaFavorito;
	}

	public void setDistribuicaoSubturmaFavorito(Boolean distribuicaoSubturmaFavorito) {
		this.distribuicaoSubturmaFavorito = distribuicaoSubturmaFavorito;
	}

	public Boolean getPainelGestorConsultorPorMatricula() {
		return painelGestorConsultorPorMatricula;
	}

	public void setPainelGestorConsultorPorMatricula(Boolean painelGestorConsultorPorMatricula) {
		this.painelGestorConsultorPorMatricula = painelGestorConsultorPorMatricula;
	}

	public Boolean getConsultorPorMatriculaRel() {
		return consultorPorMatriculaRel;
	}

	public void setConsultorPorMatriculaRel(Boolean consultorPorMatriculaRel) {
		this.consultorPorMatriculaRel = consultorPorMatriculaRel;
	}

	public Boolean getConsultorPorMatriculaRelFavorito() {
		return consultorPorMatriculaRelFavorito;
	}

	public void setConsultorPorMatriculaRelFavorito(Boolean consultorPorMatriculaRelFavorito) {
		this.consultorPorMatriculaRelFavorito = consultorPorMatriculaRelFavorito;
	}

	public Boolean getReservaCatalogoRel() {
		return reservaCatalogoRel;
	}

	public void setReservaCatalogoRel(Boolean reservaCatalogoRel) {
		this.reservaCatalogoRel = reservaCatalogoRel;
	}

	public Boolean getReservaCatalogoRelFavorito() {
		return reservaCatalogoRelFavorito;
	}

	public void setReservaCatalogoRelFavorito(Boolean reservaCatalogoRelFavorito) {
		this.reservaCatalogoRelFavorito = reservaCatalogoRelFavorito;
	}

	/**
	 * @return the categoriaDesconto
	 */
	public Boolean getCategoriaDesconto() {
		return categoriaDesconto;
	}

	/**
	 * @param categoriaDesconto the categoriaDesconto to set
	 */
	public void setCategoriaDesconto(Boolean categoriaDesconto) {
		this.categoriaDesconto = categoriaDesconto;
	}

	/**
	 * @return the categoriaDescontoFavorito
	 */
	public Boolean getCategoriaDescontoFavorito() {
		return categoriaDescontoFavorito;
	}

	/**
	 * @param categoriaDescontoFavorito the categoriaDescontoFavorito to set
	 */
	public void setCategoriaDescontoFavorito(Boolean categoriaDescontoFavorito) {
		this.categoriaDescontoFavorito = categoriaDescontoFavorito;
	}

	public Boolean getMapaEquivalenciaMatrizCurricular() {
		return mapaEquivalenciaMatrizCurricular;
	}

	public void setMapaEquivalenciaMatrizCurricular(Boolean mapaEquivalenciaMatrizCurricular) {
		this.mapaEquivalenciaMatrizCurricular = mapaEquivalenciaMatrizCurricular;
	}

	public Boolean getMapaEquivalenciaMatrizCurricularFavorito() {
		return mapaEquivalenciaMatrizCurricularFavorito;
	}

	public void setMapaEquivalenciaMatrizCurricularFavorito(Boolean mapaEquivalenciaMatrizCurricularFavorito) {
		this.mapaEquivalenciaMatrizCurricularFavorito = mapaEquivalenciaMatrizCurricularFavorito;
	}

	public Boolean getPermitirGerarRelatorioDiarioRetroativo() {
		return permitirGerarRelatorioDiarioRetroativo;
	}

	public void setPermitirGerarRelatorioDiarioRetroativo(Boolean permitirGerarRelatorioDiarioRetroativo) {
		this.permitirGerarRelatorioDiarioRetroativo = permitirGerarRelatorioDiarioRetroativo;
	}

	public Boolean getTipoConcedente() {
		return tipoConcedente;
	}

	public void setTipoConcedente(Boolean tipoConcedente) {
		this.tipoConcedente = tipoConcedente;
	}

	public Boolean getTipoConcedenteFavorito() {
		return tipoConcedenteFavorito;
	}

	public void setTipoConcedenteFavorito(Boolean tipoConcedenteFavorito) {
		this.tipoConcedenteFavorito = tipoConcedenteFavorito;
	}

	public Boolean getConcedente() {
		return concedente;
	}

	public void setConcedente(Boolean concedente) {
		this.concedente = concedente;
	}

	public Boolean getConcedenteFavorito() {
		return concedenteFavorito;
	}

	public void setConcedenteFavorito(Boolean concedenteFavorito) {
		this.concedenteFavorito = concedenteFavorito;
	}

	public Boolean getGrupoPessoa() {
		return grupoPessoa;
	}

	public void setGrupoPessoa(Boolean grupoPessoa) {
		this.grupoPessoa = grupoPessoa;
	}

	public Boolean getGrupoPessoaFavorito() {
		return grupoPessoaFavorito;
	}

	public void setGrupoPessoaFavorito(Boolean grupoPessoaFavorito) {
		this.grupoPessoaFavorito = grupoPessoaFavorito;
	}

	public Boolean getCamposEstagio() {
		return camposEstagio;
	}

	public void setCamposEstagio(Boolean camposEstagio) {
		this.camposEstagio = camposEstagio;
	}

	public Boolean getCamposEstagioFavorito() {
		return camposEstagioFavorito;
	}

	public void setCamposEstagioFavorito(Boolean camposEstagioFavorito) {
		this.camposEstagioFavorito = camposEstagioFavorito;
	}

	public Boolean getFormularioEstagio() {
		return formularioEstagio;
	}

	public void setFormularioEstagio(Boolean formularioEstagio) {
		this.formularioEstagio = formularioEstagio;
	}

	public Boolean getFormularioEstagioFavorito() {
		return formularioEstagioFavorito;
	}

	public void setFormularioEstagioFavorito(Boolean formularioEstagioFavorito) {
		this.formularioEstagioFavorito = formularioEstagioFavorito;
	}

	public Boolean getModeloTermoEstagio() {
		return modeloTermoEstagio;
	}

	public void setModeloTermoEstagio(Boolean modeloTermoEstagio) {
		this.modeloTermoEstagio = modeloTermoEstagio;
	}

	public Boolean getModeloTermoEstagioFavorito() {
		return modeloTermoEstagioFavorito;
	}

	public void setModeloTermoEstagioFavorito(Boolean modeloTermoEstagioFavorito) {
		this.modeloTermoEstagioFavorito = modeloTermoEstagioFavorito;
	}

	public Boolean getMotivosPadroesEstagio() {
		return motivosPadroesEstagio;
	}

	public void setMotivosPadroesEstagio(Boolean motivosPadroesEstagio) {
		this.motivosPadroesEstagio = motivosPadroesEstagio;
	}

	public Boolean getMotivosPadroesEstagioFavorito() {
		return motivosPadroesEstagioFavorito;
	}

	public void setMotivosPadroesEstagioFavorito(Boolean motivosPadroesEstagioFavorito) {
		this.motivosPadroesEstagioFavorito = motivosPadroesEstagioFavorito;
	}

	public Boolean getConfiguracaoEstagioObrigatorio() {
		return configuracaoEstagioObrigatorio;
	}

	public void setConfiguracaoEstagioObrigatorio(Boolean configuracaoEstagioObrigatorio) {
		this.configuracaoEstagioObrigatorio = configuracaoEstagioObrigatorio;
	}

	public Boolean getConfiguracaoEstagioObrigatorioFavorito() {
		return configuracaoEstagioObrigatorioFavorito;
	}

	public void setConfiguracaoEstagioObrigatorioFavorito(Boolean configuracaoEstagioObrigatorioFavorito) {
		this.configuracaoEstagioObrigatorioFavorito = configuracaoEstagioObrigatorioFavorito;
	}

	public Boolean getEstagio() {
		return estagio;
	}

	public void setEstagio(Boolean estagio) {
		this.estagio = estagio;
	}

	public Boolean getEstagioFavorito() {
		return estagioFavorito;
	}

	public void setEstagioFavorito(Boolean estagioFavorito) {
		this.estagioFavorito = estagioFavorito;
	}

	public Boolean getEstagioObrigatorio() {
		return estagioObrigatorio;
	}

	public void setEstagioObrigatorio(Boolean estagioObrigatorio) {
		this.estagioObrigatorio = estagioObrigatorio;
	}

	public Boolean getEstagioObrigatorioFavorito() {
		return estagioObrigatorioFavorito;
	}

	public void setEstagioObrigatorioFavorito(Boolean estagioObrigatorioFavorito) {
		this.estagioObrigatorioFavorito = estagioObrigatorioFavorito;
	}

	public Boolean getEstagioAluno() {
		return estagioAluno;
	}

	public void setEstagioAluno(Boolean estagioAluno) {
		this.estagioAluno = estagioAluno;
	}

	public Boolean getEstagioRel() {
		return estagioRel;
	}

	public void setEstagioRel(Boolean estagioRel) {
		this.estagioRel = estagioRel;
	}

	public Boolean getEstagioRelFavorito() {
		return estagioRelFavorito;
	}

	public void setEstagioRelFavorito(Boolean estagioRelFavorito) {
		this.estagioRelFavorito = estagioRelFavorito;
	}

	public Boolean getConfiguracaoNotaFiscal() {
		return configuracaoNotaFiscal;
	}

	public void setConfiguracaoNotaFiscal(Boolean configuracaoNotaFiscal) {
		this.configuracaoNotaFiscal = configuracaoNotaFiscal;
	}

	public Boolean getConfiguracaoNotaFiscalFavorito() {
		return configuracaoNotaFiscalFavorito;
	}

	public void setConfiguracaoNotaFiscalFavorito(Boolean configuracaoNotaFiscalFavorito) {
		this.configuracaoNotaFiscalFavorito = configuracaoNotaFiscalFavorito;
	}

	public Boolean getNotaFiscalSaida() {
		return notaFiscalSaida;
	}

	public void setNotaFiscalSaida(Boolean notaFiscalSaida) {
		this.notaFiscalSaida = notaFiscalSaida;
	}

	public Boolean getNotaFiscalSaidaFavorito() {
		return notaFiscalSaidaFavorito;
	}

	public void setNotaFiscalSaidaFavorito(Boolean notaFiscalSaidaFavorito) {
		this.notaFiscalSaidaFavorito = notaFiscalSaidaFavorito;
	}

	public Boolean getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(Boolean naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public Boolean getNaturezaOperacaoFavorito() {
		return naturezaOperacaoFavorito;
	}

	public void setNaturezaOperacaoFavorito(Boolean naturezaOperacaoFavorito) {
		this.naturezaOperacaoFavorito = naturezaOperacaoFavorito;
	}

	public Boolean getImposto() {
		return imposto;
	}

	public void setImposto(Boolean imposto) {
		this.imposto = imposto;
	}

	public Boolean getImpostoFavorito() {
		return impostoFavorito;
	}

	public Boolean getNotaFiscalEntrada() {
		return notaFiscalEntrada;
	}

	public void setNotaFiscalEntrada(Boolean notaFiscalEntrada) {
		this.notaFiscalEntrada = notaFiscalEntrada;
	}

	public Boolean getNotaFiscalEntradaFavorito() {
		return notaFiscalEntradaFavorito;
	}

	public void setNotaFiscalEntradaFavorito(Boolean notaFiscalEntradaFavorito) {
		this.notaFiscalEntradaFavorito = notaFiscalEntradaFavorito;
	}

	public void setImpostoFavorito(Boolean impostoFavorito) {
		this.impostoFavorito = impostoFavorito;
	}

	public Boolean getPermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular() {
		return permitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular;
	}

	public void setPermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular(Boolean permitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular) {
		this.permitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular = permitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular;
	}

	public Boolean getAtividadeComplementarRel() {
		return atividadeComplementarRel;
	}

	public void setAtividadeComplementarRel(Boolean atividadeComplementarRel) {
		this.atividadeComplementarRel = atividadeComplementarRel;
	}

	public Boolean getAtividadeComplementarRelFavorito() {
		return atividadeComplementarRelFavorito;
	}

	public void setAtividadeComplementarRelFavorito(Boolean atividadeComplementarRelFavorito) {
		this.atividadeComplementarRelFavorito = atividadeComplementarRelFavorito;
	}

	public Boolean getMapaConvocacaoEnade() {
		return mapaConvocacaoEnade;
	}

	public void setMapaConvocacaoEnade(Boolean mapaConvocacaoEnade) {
		this.mapaConvocacaoEnade = mapaConvocacaoEnade;
	}

	public Boolean getMapaConvocacaoEnadeFavorito() {
		return mapaConvocacaoEnadeFavorito;
	}

	public void setMapaConvocacaoEnadeFavorito(Boolean mapaConvocacaoEnadeFavorito) {
		this.mapaConvocacaoEnadeFavorito = mapaConvocacaoEnadeFavorito;
	}

	public Boolean getNotaConceitoIndicadorAvaliacao() {
		if (notaConceitoIndicadorAvaliacao == null) {
			notaConceitoIndicadorAvaliacao = false;
		}
		return notaConceitoIndicadorAvaliacao;
	}

	public void setNotaConceitoIndicadorAvaliacao(Boolean notaConceitoIndicadorAvaliacao) {
		this.notaConceitoIndicadorAvaliacao = notaConceitoIndicadorAvaliacao;
	}

	public Boolean getNotaConceitoIndicadorAvaliacaoFavorito() {
		if (notaConceitoIndicadorAvaliacaoFavorito == null) {
			notaConceitoIndicadorAvaliacaoFavorito = false;
		}
		return notaConceitoIndicadorAvaliacaoFavorito;
	}

	public void setNotaConceitoIndicadorAvaliacaoFavorito(Boolean notaConceitoIndicadorAvaliacaoFavorito) {
		this.notaConceitoIndicadorAvaliacaoFavorito = notaConceitoIndicadorAvaliacaoFavorito;
	}

	public Boolean getCriterioAvaliacao() {
		if (criterioAvaliacao == null) {
			criterioAvaliacao = false;
		}
		return criterioAvaliacao;
	}

	public void setCriterioAvaliacao(Boolean criterioAvaliacao) {
		this.criterioAvaliacao = criterioAvaliacao;
	}

	public Boolean getCriterioAvaliacaoFavorito() {
		if (criterioAvaliacaoFavorito == null) {
			criterioAvaliacaoFavorito = false;
		}
		return criterioAvaliacaoFavorito;
	}

	public void setCriterioAvaliacaoFavorito(Boolean criterioAvaliacaoFavorito) {
		this.criterioAvaliacaoFavorito = criterioAvaliacaoFavorito;
	}

	public Boolean getPermitirComunicacaoInternaEnviarCopiaPorEmail() {
		return permitirComunicacaoInternaEnviarCopiaPorEmail;
	}

	public void setPermitirComunicacaoInternaEnviarCopiaPorEmail(Boolean permitirComunicacaoInternaEnviarCopiaPorEmail) {
		this.permitirComunicacaoInternaEnviarCopiaPorEmail = permitirComunicacaoInternaEnviarCopiaPorEmail;
	}

	public Boolean getCriterioAvaliacaoAluno() {
		if (criterioAvaliacaoAluno == null) {
			criterioAvaliacaoAluno = false;
		}
		return criterioAvaliacaoAluno;
	}

	public void setCriterioAvaliacaoAluno(Boolean criterioAvaliacaoAluno) {
		this.criterioAvaliacaoAluno = criterioAvaliacaoAluno;
	}

	public Boolean getCriterioAvaliacaoAlunoFavorito() {
		if (criterioAvaliacaoAlunoFavorito == null) {
			criterioAvaliacaoAlunoFavorito = false;
		}
		return criterioAvaliacaoAlunoFavorito;
	}

	public void setCriterioAvaliacaoAlunoFavorito(Boolean criterioAvaliacaoAlunoFavorito) {
		this.criterioAvaliacaoAlunoFavorito = criterioAvaliacaoAlunoFavorito;
	}

	public Boolean getCriterioAvaliacaoAlunoVisaoProfessor() {
		if (criterioAvaliacaoAlunoVisaoProfessor == null) {
			criterioAvaliacaoAlunoVisaoProfessor = false;
		}
		return criterioAvaliacaoAlunoVisaoProfessor;
	}

	public void setCriterioAvaliacaoAlunoVisaoProfessor(Boolean criterioAvaliacaoAlunoVisaoProfessor) {
		this.criterioAvaliacaoAlunoVisaoProfessor = criterioAvaliacaoAlunoVisaoProfessor;
	}

	public Boolean getCriterioAvaliacaoAlunoVisaoCoordenador() {
		return criterioAvaliacaoAlunoVisaoCoordenador;
	}

	public void setCriterioAvaliacaoAlunoVisaoCoordenador(Boolean criterioAvaliacaoAlunoVisaoCoordenador) {
		this.criterioAvaliacaoAlunoVisaoCoordenador = criterioAvaliacaoAlunoVisaoCoordenador;
	}

	public Boolean getCriterioAvaliacaoAlunoRel() {
		if (criterioAvaliacaoAlunoRel == null) {
			criterioAvaliacaoAlunoRel = false;
		}
		return criterioAvaliacaoAlunoRel;
	}

	public void setCriterioAvaliacaoAlunoRel(Boolean criterioAvaliacaoAlunoRel) {
		this.criterioAvaliacaoAlunoRel = criterioAvaliacaoAlunoRel;
	}

	public Boolean getCriterioAvaliacaoAlunoRelVisaoProfessor() {
		if (criterioAvaliacaoAlunoRelVisaoProfessor == null) {
			criterioAvaliacaoAlunoRelVisaoProfessor = false;
		}
		return criterioAvaliacaoAlunoRelVisaoProfessor;
	}

	public void setCriterioAvaliacaoAlunoRelVisaoProfessor(Boolean criterioAvaliacaoAlunoRelVisaoProfessor) {
		this.criterioAvaliacaoAlunoRelVisaoProfessor = criterioAvaliacaoAlunoRelVisaoProfessor;
	}

	public Boolean getCriterioAvaliacaoAlunoRelVisaoCoordenador() {
		if (criterioAvaliacaoAlunoRelVisaoCoordenador == null) {
			criterioAvaliacaoAlunoRelVisaoCoordenador = false;
		}
		return criterioAvaliacaoAlunoRelVisaoCoordenador;
	}

	public void setCriterioAvaliacaoAlunoRelVisaoCoordenador(Boolean criterioAvaliacaoAlunoRelVisaoCoordenador) {
		this.criterioAvaliacaoAlunoRelVisaoCoordenador = criterioAvaliacaoAlunoRelVisaoCoordenador;
	}

	public Boolean getCriterioAvaliacaoAlunoRelFavorito() {
		if (criterioAvaliacaoAlunoRelFavorito == null) {
			criterioAvaliacaoAlunoRelFavorito = false;
		}
		return criterioAvaliacaoAlunoRelFavorito;
	}

	public void setCriterioAvaliacaoAlunoRelFavorito(Boolean criterioAvaliacaoAlunoRelFavorito) {
		this.criterioAvaliacaoAlunoRelFavorito = criterioAvaliacaoAlunoRelFavorito;
	}

	public Boolean getEstoqueRel() {
		return estoqueRel;
	}

	public void setEstoqueRel(Boolean estoqueRel) {
		this.estoqueRel = estoqueRel;
	}

	public Boolean getEstoqueRelFavorito() {
		return estoqueRelFavorito;
	}

	public void setEstoqueRelFavorito(Boolean estoqueRelFavorito) {
		this.estoqueRelFavorito = estoqueRelFavorito;
	}

	public Boolean getTaxaRequerimento() {
		return taxaRequerimento;
	}

	public void setTaxaRequerimento(Boolean taxaRequerimento) {
		this.taxaRequerimento = taxaRequerimento;
	}

	public Boolean getTaxaRequerimentoFavorito() {
		return taxaRequerimentoFavorito;
	}

	public void setTaxaRequerimentoFavorito(Boolean taxaRequerimentoFavorito) {
		this.taxaRequerimentoFavorito = taxaRequerimentoFavorito;
	}

	public Boolean getUploadBackUp() {
		return uploadBackUp;
	}

	public void setUploadBackUp(Boolean uploadBackUp) {
		this.uploadBackUp = uploadBackUp;
	}

	public Boolean getUploadBackUpFavorito() {
		return uploadBackUpFavorito;
	}

	public void setUploadBackUpFavorito(Boolean uploadBackUpFavorito) {
		this.uploadBackUpFavorito = uploadBackUpFavorito;
	}

	public Boolean getMinhasNotasAdminsitrativo() {
		return minhasNotasAdminsitrativo;
	}

	public void setMinhasNotasAdminsitrativo(Boolean minhasNotasAdminsitrativo) {
		this.minhasNotasAdminsitrativo = minhasNotasAdminsitrativo;
	}

	public Boolean getMinhasNotasAdminsitrativoFavorito() {
		return minhasNotasAdminsitrativoFavorito;
	}

	public void setMinhasNotasAdminsitrativoFavorito(Boolean minhasNotasAdminsitrativoFavorito) {
		this.minhasNotasAdminsitrativoFavorito = minhasNotasAdminsitrativoFavorito;
	}

	public Boolean getAutorizarPublicarPlanoEnsino() {
		return autorizarPublicarPlanoEnsino;
	}

	public void setAutorizarPublicarPlanoEnsino(Boolean autorizarPublicarPlanoEnsino) {
		this.autorizarPublicarPlanoEnsino = autorizarPublicarPlanoEnsino;
	}

	public Boolean getAutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador() {
		return autorizarPublicarPlanoEnsinoVisaoProfessorCoordenador;
	}

	public void setAutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador(Boolean autorizarPublicarPlanoEnsinoVisaoProfessorCoordenador) {
		this.autorizarPublicarPlanoEnsinoVisaoProfessorCoordenador = autorizarPublicarPlanoEnsinoVisaoProfessorCoordenador;
	}

	public Boolean getPermitirVisualizarCampanhaTodasUnidades() {
		return permitirVisualizarCampanhaTodasUnidades;
	}

	public void setPermitirVisualizarCampanhaTodasUnidades(Boolean permitirVisualizarCampanhaTodasUnidades) {
		this.permitirVisualizarCampanhaTodasUnidades = permitirVisualizarCampanhaTodasUnidades;
	}

	public Boolean getPermitirVisualizarCampanhaCobranca() {
		return permitirVisualizarCampanhaCobranca;
	}

	public void setPermitirVisualizarCampanhaCobranca(Boolean permitirVisualizarCampanhaCobranca) {
		this.permitirVisualizarCampanhaCobranca = permitirVisualizarCampanhaCobranca;
	}

	public Boolean getPermitirVisualizarCampanhaVendas() {
		return permitirVisualizarCampanhaVendas;
	}

	public void setPermitirVisualizarCampanhaVendas(Boolean permitirVisualizarCampanhaVendas) {
		this.permitirVisualizarCampanhaVendas = permitirVisualizarCampanhaVendas;
	}

	public Boolean getEstagioCoordenador() {
		return estagioCoordenador;
	}

	public void setEstagioCoordenador(Boolean estagioCoordenador) {
		this.estagioCoordenador = estagioCoordenador;
	}

	public Boolean getPermitirGerarRelatorioDocumentoIntegralizacaoCurricular() {
		return permitirGerarRelatorioDocumentoIntegralizacaoCurricular;
	}

	public void setPermitirGerarRelatorioDocumentoIntegralizacaoCurricular(Boolean permitirGerarRelatorioDocumentoIntegralizacaoCurricular) {
		this.permitirGerarRelatorioDocumentoIntegralizacaoCurricular = permitirGerarRelatorioDocumentoIntegralizacaoCurricular;
	}

	public Boolean getPoliticaDivulgacaoMatriculaOnline() {
		return politicaDivulgacaoMatriculaOnline;
	}

	public void setPoliticaDivulgacaoMatriculaOnline(Boolean politicaDivulgacaoMatriculaOnline) {
		this.politicaDivulgacaoMatriculaOnline = politicaDivulgacaoMatriculaOnline;
	}

	public Boolean getPoliticaDivulgacaoMatriculaOnlineFavorito() {
		return politicaDivulgacaoMatriculaOnlineFavorito;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineFavorito(Boolean politicaDivulgacaoMatriculaOnlineFavorito) {
		this.politicaDivulgacaoMatriculaOnlineFavorito = politicaDivulgacaoMatriculaOnlineFavorito;
	}

	public Boolean getLiberacaoTurmaEADIPOG() {
		return liberacaoTurmaEADIPOG;
	}

	public void setLiberacaoTurmaEADIPOG(Boolean liberacaoTurmaEADIPOG) {
		this.liberacaoTurmaEADIPOG = liberacaoTurmaEADIPOG;
	}

	public Boolean getLiberacaoTurmaEADIPOGFavorito() {
		return liberacaoTurmaEADIPOGFavorito;
	}

	public void setLiberacaoTurmaEADIPOGFavorito(Boolean liberacaoTurmaEADIPOGFavorito) {
		this.liberacaoTurmaEADIPOGFavorito = liberacaoTurmaEADIPOGFavorito;
	}

	public Boolean getPermitirExcluirPreMatricula() {
		return permitirExcluirPreMatricula;
	}

	public void setPermitirExcluirPreMatricula(Boolean permitirExcluirPreMatricula) {
		this.permitirExcluirPreMatricula = permitirExcluirPreMatricula;
	}

	public Boolean getTipoPatrimonio() {
		return tipoPatrimonio;
	}

	public void setTipoPatrimonio(Boolean tipoPatrimonio) {
		this.tipoPatrimonio = tipoPatrimonio;
	}

	public Boolean getTipoPatrimonioFavorito() {
		return tipoPatrimonioFavorito;
	}

	public void setTipoPatrimonioFavorito(Boolean tipoRecursoControleReservaFavorito) {
		this.tipoPatrimonioFavorito = tipoRecursoControleReservaFavorito;
	}

	public Boolean getAnotacaoDisciplina() {
		return anotacaoDisciplina;
	}

	public void setAnotacaoDisciplina(Boolean anotacaoDisciplina) {
		this.anotacaoDisciplina = anotacaoDisciplina;
	}

	public Boolean getAnotacaoDisciplinaFavorito() {
		return anotacaoDisciplinaFavorito;
	}

	public void setAnotacaoDisciplinaFavorito(Boolean anotacaoDisciplinaFavorito) {
		this.anotacaoDisciplinaFavorito = anotacaoDisciplinaFavorito;
	}

	public Boolean getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(Boolean patrimonio) {
		this.patrimonio = patrimonio;
	}

	public Boolean getPatrimonioFavorito() {
		return patrimonioFavorito;
	}

	public void setPatrimonioFavorito(Boolean patrimonioFavorito) {
		this.patrimonioFavorito = patrimonioFavorito;
	}

	public Boolean getQuestaoProfessor() {
		if (questaoProfessor == null) {
			questaoProfessor = false;
		}
		return questaoProfessor;
	}

	public void setQuestaoProfessor(Boolean questaoProfessor) {
		this.questaoProfessor = questaoProfessor;
	}

	public Boolean getQuestaoProfessorFavorito() {
		if (questaoProfessorFavorito == null) {
			questaoProfessorFavorito = false;
		}
		return questaoProfessorFavorito;
	}

	public void setQuestaoProfessorFavorito(Boolean questaoProfessorFavorito) {
		this.questaoProfessorFavorito = questaoProfessorFavorito;
	}

	public Boolean getConfiguracoesEAD() {
		if (configuracaoEAD == null) {
			configuracaoEAD = false;
		}
		return configuracaoEAD;
	}

	public void setConfiguracoesEAD(Boolean configuracoesEAD) {
		this.configuracaoEAD = configuracoesEAD;
	}

	public Boolean getConfiguracaoEAD() {
		if (configuracaoEAD == null) {
			configuracaoEAD = false;
		}
		return configuracaoEAD;
	}

	public void setConfiguracaoEAD(Boolean configuracaoEAD) {
		this.configuracaoEAD = configuracaoEAD;
	}

	public Boolean getAtividadeDiscursivaProfessor() {
		if (atividadeDiscursivaProfessor == null) {
			atividadeDiscursivaProfessor = false;
		}
		return atividadeDiscursivaProfessor;
	}

	public void setAtividadeDiscursivaProfessor(Boolean atividadeDiscursivaProfessor) {
		this.atividadeDiscursivaProfessor = atividadeDiscursivaProfessor;
	}

	public Boolean getAtividadeDiscursivaAluno() {
		if (AtividadeDiscursivaAluno == null) {
			AtividadeDiscursivaAluno = false;
		}
		return AtividadeDiscursivaAluno;
	}

	public void setAtividadeDiscursivaAluno(Boolean atividadeDiscursivaAluno) {
		AtividadeDiscursivaAluno = atividadeDiscursivaAluno;
	}

	public Boolean getConfiguracaoEADFavorito() {
		if (configuracaoEADFavorito == null) {
			configuracaoEADFavorito = false;
		}
		return configuracaoEADFavorito;
	}

	public void setConfiguracaoEADFavorito(Boolean configuracaoEADFavorito) {
		this.configuracaoEADFavorito = configuracaoEADFavorito;
	}

	public Boolean getCalendarioAtividadeMatricula() {
		if (calendarioAtividadeMatricula == null) {
			calendarioAtividadeMatricula = false;
		}
		return calendarioAtividadeMatricula;
	}

	public void setCalendarioAtividadeMatricula(Boolean calendarioAtividadeMatricula) {
		this.calendarioAtividadeMatricula = calendarioAtividadeMatricula;
	}

	public Boolean getCalendarioAtividadeMatriculaFavorito() {
		if (calendarioAtividadeMatriculaFavorito == null) {
			calendarioAtividadeMatriculaFavorito = false;
		}
		return calendarioAtividadeMatriculaFavorito;
	}

	public void setCalendarioAtividadeMatriculaFavorito(Boolean calendarioAtividadeMatriculaFavorito) {
		this.calendarioAtividadeMatriculaFavorito = calendarioAtividadeMatriculaFavorito;
	}

	public Boolean getAvaliacaoOnline() {
		if (avaliacaoOnline == null) {
			avaliacaoOnline = false;
		}
		return avaliacaoOnline;
	}

	public void setAvaliacaoOnline(Boolean avaliacaoOnline) {
		this.avaliacaoOnline = avaliacaoOnline;
	}

	public Boolean getAvaliacaoOnlineFavorito() {
		if (avaliacaoOnlineFavorito == null) {
			avaliacaoOnlineFavorito = false;
		}
		return avaliacaoOnlineFavorito;
	}

	public void setAvaliacaoOnlineFavorito(Boolean avaliacaoOnlineFavorito) {
		this.avaliacaoOnlineFavorito = avaliacaoOnlineFavorito;
	}

	public Boolean getRegistrarFalta() {
		if (registrarFalta == null) {
			registrarFalta = false;
		}
		return registrarFalta;
	}

	public void setRegistrarFalta(Boolean registrarFalta) {
		this.registrarFalta = registrarFalta;
	}

	public Boolean getRegistrarFaltaFavorito() {
		if (registrarFaltaFavorito == null) {
			registrarFaltaFavorito = false;
		}
		return registrarFaltaFavorito;
	}

	public void setRegistrarFaltaFavorito(Boolean registrarFaltaFavorito) {
		this.registrarFaltaFavorito = registrarFaltaFavorito;
	}

	public Boolean getConfiguracaoAcademicaHistorico() {
		if (configuracaoAcademicaHistorico == null) {
			configuracaoAcademicaHistorico = false;
		}
		return configuracaoAcademicaHistorico;
	}

	public void setConfiguracaoAcademicaHistorico(Boolean configuracaoAcademicaHistorico) {
		this.configuracaoAcademicaHistorico = configuracaoAcademicaHistorico;
	}

	public Boolean getConfiguracaoAcademicaHistoricoFavorito() {
		if (configuracaoAcademicaHistoricoFavorito == null) {
			configuracaoAcademicaHistoricoFavorito = false;
		}
		return configuracaoAcademicaHistoricoFavorito;
	}

	public void setConfiguracaoAcademicaHistoricoFavorito(Boolean configuracaoAcademicaHistoricoFavorito) {
		this.configuracaoAcademicaHistoricoFavorito = configuracaoAcademicaHistoricoFavorito;
	}

	public Boolean getConfiguracaoAvaliacaoOnlineTurmaProfessor() {
		if (configuracaoAvaliacaoOnlineTurmaProfessor == null) {
			configuracaoAvaliacaoOnlineTurmaProfessor = false;
		}
		return configuracaoAvaliacaoOnlineTurmaProfessor;
	}

	public void setConfiguracaoAvaliacaoOnlineTurmaProfessor(Boolean configuracaoAvaliacaoOnlineTurmaProfessor) {
		this.configuracaoAvaliacaoOnlineTurmaProfessor = configuracaoAvaliacaoOnlineTurmaProfessor;
	}

	public Boolean getPermitirExcluirPreMatriculaCancelada() {
		return permitirExcluirPreMatriculaCancelada;
	}

	public void setPermitirExcluirPreMatriculaCancelada(Boolean permitirExcluirPreMatriculaCancelada) {
		this.permitirExcluirPreMatriculaCancelada = permitirExcluirPreMatriculaCancelada;
	}

	public Boolean getGestaoAvaliacaoOnline() {
		if (gestaoAvaliacaoOnline == null) {
			gestaoAvaliacaoOnline = false;
		}
		return gestaoAvaliacaoOnline;
	}

	public void setGestaoAvaliacaoOnline(Boolean gestaoAvaliacaoOnline) {
		this.gestaoAvaliacaoOnline = gestaoAvaliacaoOnline;
	}

	public Boolean getGestaoAvaliacaoOnlineFavorito() {
		if (gestaoAvaliacaoOnlineFavorito == null) {
			gestaoAvaliacaoOnlineFavorito = false;
		}
		return gestaoAvaliacaoOnlineFavorito;
	}

	public void setGestaoAvaliacaoOnlineFavorito(Boolean gestaoAvaliacaoOnlineFavorito) {
		this.gestaoAvaliacaoOnlineFavorito = gestaoAvaliacaoOnlineFavorito;
	}
	
	public Boolean getRelatorioSEIDecidirEstagio() {
		if (relatorioSEIDecidirEstagio == null) {
			relatorioSEIDecidirEstagio = false;
		}
		return relatorioSEIDecidirEstagio;
	}

	public void setRelatorioSEIDecidirEstagio(Boolean relatorioSEIDecidirEstagio) {
		this.relatorioSEIDecidirEstagio = relatorioSEIDecidirEstagio;
	}

	public Boolean getRelatorioSEIDecidirEstagioFavorito() {
		if (relatorioSEIDecidirEstagioFavorito == null) {
			relatorioSEIDecidirEstagioFavorito = false;
		}
		return relatorioSEIDecidirEstagioFavorito;
	}

	public void setRelatorioSEIDecidirEstagioFavorito(Boolean relatorioSEIDecidirEstagioFavorito) {
		this.relatorioSEIDecidirEstagioFavorito = relatorioSEIDecidirEstagioFavorito;
	}
	
	public Boolean getRelatorioSEIDecidirCrm() {
		if (relatorioSEIDecidirCrm == null) {
			relatorioSEIDecidirCrm = false;
		}
		return relatorioSEIDecidirCrm;
	}
	
	public void setRelatorioSEIDecidirCrm(Boolean relatorioSEIDecidirCrm) {
		this.relatorioSEIDecidirCrm = relatorioSEIDecidirCrm;
	}
	
	public Boolean getRelatorioSEIDecidirCrmFavorito() {
		if (relatorioSEIDecidirCrmFavorito == null) {
			relatorioSEIDecidirCrmFavorito = false;
		}
		return relatorioSEIDecidirCrmFavorito;
	}
	
	public void setRelatorioSEIDecidirCrmFavorito(Boolean relatorioSEIDecidirCrmFavorito) {
		this.relatorioSEIDecidirCrmFavorito = relatorioSEIDecidirCrmFavorito;
	}

	public Boolean getRelatorioSEIDecidirReceita() {
		if (relatorioSEIDecidirReceita == null) {
			relatorioSEIDecidirReceita = false;
		}
		return relatorioSEIDecidirReceita;
	}

	public void setRelatorioSEIDecidirReceita(Boolean relatorioSEIDecidirReceita) {
		this.relatorioSEIDecidirReceita = relatorioSEIDecidirReceita;
	}

	public Boolean getRelatorioSEIDecidirReceitaFavorito() {
		if (relatorioSEIDecidirReceitaFavorito == null) {
			relatorioSEIDecidirReceitaFavorito = false;
		}
		return relatorioSEIDecidirReceitaFavorito;
	}

	public void setRelatorioSEIDecidirReceitaFavorito(Boolean relatorioSEIDecidirReceitaFavorito) {
		this.relatorioSEIDecidirReceitaFavorito = relatorioSEIDecidirReceitaFavorito;
	}

	public Boolean getRelatorioSEIDecidirCentroResultadoReceitaFavorito() {
		if (relatorioSEIDecidirCentroResultadoReceitaFavorito == null) {
			relatorioSEIDecidirCentroResultadoReceitaFavorito = false;
		}
		return relatorioSEIDecidirCentroResultadoReceitaFavorito;
	}

	public void setRelatorioSEIDecidirCentroResultadoReceitaFavorito(Boolean relatorioSEIDecidirCentroResultadoReceitaFavorito) {
		this.relatorioSEIDecidirCentroResultadoReceitaFavorito = relatorioSEIDecidirCentroResultadoReceitaFavorito;
	}

	public Boolean getRelatorioSEIDecidirDespesa() {
		if (relatorioSEIDecidirDespesa == null) {
			relatorioSEIDecidirDespesa = false;
		}
		return relatorioSEIDecidirDespesa;
	}

	public void setRelatorioSEIDecidirDespesa(Boolean relatorioSEIDecidirDespesa) {
		this.relatorioSEIDecidirDespesa = relatorioSEIDecidirDespesa;
	}

	public Boolean getRelatorioSEIDecidirCentroResultadoDespesaFavorito() {
		if (relatorioSEIDecidirCentroResultadoDespesaFavorito == null) {
			relatorioSEIDecidirCentroResultadoDespesaFavorito = false;
		}
		return relatorioSEIDecidirCentroResultadoDespesaFavorito;
	}

	public void setRelatorioSEIDecidirCentroResultadoDespesaFavorito(Boolean relatorioSEIDecidirCentroResultadoDespesaFavorito) {
		this.relatorioSEIDecidirCentroResultadoDespesaFavorito = relatorioSEIDecidirCentroResultadoDespesaFavorito;
	}

	public Boolean getRelatorioSEIDecidirDespesaFavorito() {
		if (relatorioSEIDecidirDespesaFavorito == null) {
			relatorioSEIDecidirDespesaFavorito = false;
		}
		return relatorioSEIDecidirDespesaFavorito;
	}

	public void setRelatorioSEIDecidirDespesaFavorito(Boolean relatorioSEIDecidirDespesaFavorito) {
		this.relatorioSEIDecidirDespesaFavorito = relatorioSEIDecidirDespesaFavorito;
	}

	public Boolean getRelatorioSEIDecidirAcademico() {
		if (relatorioSEIDecidirAcademico == null) {
			relatorioSEIDecidirAcademico = false;
		}
		return relatorioSEIDecidirAcademico;
	}

	public void setRelatorioSEIDecidirAcademico(Boolean relatorioSEIDecidirAcademico) {
		this.relatorioSEIDecidirAcademico = relatorioSEIDecidirAcademico;
	}

	public Boolean getRelatorioSEIDecidirAcademicoFavorito() {
		if (relatorioSEIDecidirAcademicoFavorito == null) {
			relatorioSEIDecidirAcademicoFavorito = false;
		}
		return relatorioSEIDecidirAcademicoFavorito;
	}

	public void setRelatorioSEIDecidirAcademicoFavorito(Boolean relatorioSEIDecidirAcademicoFavorito) {
		this.relatorioSEIDecidirAcademicoFavorito = relatorioSEIDecidirAcademicoFavorito;
	}

	public Boolean getProgramacaoTutoriaOnline() {
		if (programacaoTutoriaOnline == null) {
			programacaoTutoriaOnline = false;
		}
		return programacaoTutoriaOnline;
	}

	public void setProgramacaoTutoriaOnline(Boolean programacaoTutoriaOnline) {
		this.programacaoTutoriaOnline = programacaoTutoriaOnline;
	}

	public Boolean getProgramacaoTutoriaOnlineFavorito() {
		if (programacaoTutoriaOnlineFavorito == null) {
			programacaoTutoriaOnlineFavorito = false;
		}
		return programacaoTutoriaOnlineFavorito;
	}

	public void setProgramacaoTutoriaOnlineFavorito(Boolean programacaoTutoriaOnlineFavorito) {
		this.programacaoTutoriaOnlineFavorito = programacaoTutoriaOnlineFavorito;
	}

	public Boolean getLayoutRelatorioSEIDecidirFavorito() {
		if (layoutRelatorioSEIDecidirFavorito == null) {
			layoutRelatorioSEIDecidirFavorito = false;
		}
		return layoutRelatorioSEIDecidirFavorito;
	}

	public void setLayoutRelatorioSEIDecidirFavorito(Boolean layoutRelatorioSEIDecidirFavorito) {
		this.layoutRelatorioSEIDecidirFavorito = layoutRelatorioSEIDecidirFavorito;
	}

	public Boolean getLayoutRelatorioSEIDecidir() {
		if (layoutRelatorioSEIDecidir == null) {
			layoutRelatorioSEIDecidir = false;
		}
		return layoutRelatorioSEIDecidir;
	}

	public void setLayoutRelatorioSEIDecidir(Boolean layoutRelatorioSEIDecidir) {
		this.layoutRelatorioSEIDecidir = layoutRelatorioSEIDecidir;
	}

	public Boolean getVincularNotaEspecifica() {
		if (vincularNotaEspecifica == null) {
			vincularNotaEspecifica = false;
		}
		return vincularNotaEspecifica;
	}

	public void setVincularNotaEspecifica(Boolean vincularNotaEspecifica) {
		this.vincularNotaEspecifica = vincularNotaEspecifica;
	}

	public Boolean getPermitirCancelarContaReceber() {
		if (permitirCancelarContaReceber == null) {
			permitirCancelarContaReceber = false;
		}
		return permitirCancelarContaReceber;
	}

	public void setPermitirCancelarContaReceber(Boolean permitirCancelarContaReceber) {
		this.permitirCancelarContaReceber = permitirCancelarContaReceber;
	}

	public Boolean getMonitoramentoAlunosEAD() {
		if (monitoramentoAlunosEAD == null) {
			monitoramentoAlunosEAD = false;
		}
		return monitoramentoAlunosEAD;
	}

	public void setMonitoramentoAlunosEAD(Boolean monitoramentoAlunosEAD) {
		this.monitoramentoAlunosEAD = monitoramentoAlunosEAD;
	}

	public Boolean getCriterioAvaliacaoAlunoVisaoPais() {
		return criterioAvaliacaoAlunoVisaoPais;
	}

	public void setCriterioAvaliacaoAlunoVisaoPais(Boolean criterioAvaliacaoAlunoVisaoPais) {
		this.criterioAvaliacaoAlunoVisaoPais = criterioAvaliacaoAlunoVisaoPais;
	}

	public Boolean getConfiguracaoConteudoTurma() {
		if (configuracaoConteudoTurma == null) {
			configuracaoConteudoTurma = false;
		}
		return configuracaoConteudoTurma;
	}

	public void setConfiguracaoConteudoTurma(Boolean configuracaoConteudoTurma) {
		this.configuracaoConteudoTurma = configuracaoConteudoTurma;
	}

	public Boolean getConfiguracaoConteudoTurmaFavorito() {
		if (configuracaoConteudoTurmaFavorito == null) {
			configuracaoConteudoTurmaFavorito = false;
		}
		return configuracaoConteudoTurmaFavorito;
	}

	public void setConfiguracaoConteudoTurmaFavorito(Boolean configuracaoConteudoTurmaFavorito) {
		this.configuracaoConteudoTurmaFavorito = configuracaoConteudoTurmaFavorito;
	}

	public Boolean getImportarArquivoMarc21() {
		return importarArquivoMarc21;
	}

	public void setImportarArquivoMarc21(Boolean importarArquivoMarc21) {
		this.importarArquivoMarc21 = importarArquivoMarc21;
	}

	public Boolean getImportarArquivoMarc21Favorito() {
		return importarArquivoMarc21Favorito;
	}

	public void setImportarArquivoMarc21Favorito(Boolean importarArquivoMarc21Favorito) {
		this.importarArquivoMarc21Favorito = importarArquivoMarc21Favorito;
	}

	public Boolean getComunicacaoInternaRel() {
		return comunicacaoInternaRel;
	}

	public void setComunicacaoInternaRel(Boolean comunicacaoInternaRel) {
		this.comunicacaoInternaRel = comunicacaoInternaRel;
	}

	public Boolean getComunicacaoInternaRelFavorito() {
		return comunicacaoInternaRelFavorito;
	}

	public void setComunicacaoInternaRelFavorito(Boolean comunicacaoInternaRelFavorito) {
		this.comunicacaoInternaRelFavorito = comunicacaoInternaRelFavorito;
	}

	public Boolean getConteudoVisaoProfessor() {
		return conteudoVisaoProfessor;
	}

	public void setConteudoVisaoProfessor(Boolean conteudoVisaoProfessor) {
		this.conteudoVisaoProfessor = conteudoVisaoProfessor;
	}

	public Boolean getPermitirProfessorCadastrarConteudoQualquerDisciplina() {
		return permitirProfessorCadastrarConteudoQualquerDisciplina;
	}

	public void setPermitirProfessorCadastrarConteudoQualquerDisciplina(Boolean permitirProfessorCadastrarConteudoQualquerDisciplina) {
		this.permitirProfessorCadastrarConteudoQualquerDisciplina = permitirProfessorCadastrarConteudoQualquerDisciplina;
	}

	public Boolean getPermitirProfessorCadastrarConteudoApenasAulasProgramadas() {
		return permitirProfessorCadastrarConteudoApenasAulasProgramadas;
	}

	public void setPermitirProfessorCadastrarConteudoApenasAulasProgramadas(Boolean permitirProfessorCadastrarConteudoApenasAulasProgramadas) {
		this.permitirProfessorCadastrarConteudoApenasAulasProgramadas = permitirProfessorCadastrarConteudoApenasAulasProgramadas;
	}

	public Boolean getPermitirProfessorCadastrarApenasConteudosExclusivos() {
		return permitirProfessorCadastrarApenasConteudosExclusivos;
	}

	public void setPermitirProfessorCadastrarApenasConteudosExclusivos(Boolean permitirProfessorCadastrarApenasConteudosExclusivos) {
		this.permitirProfessorCadastrarApenasConteudosExclusivos = permitirProfessorCadastrarApenasConteudosExclusivos;
	}

	public Boolean getPermitirProfessorReabrirAvaliacaoPBL() {
		return permitirProfessorReabrirAvaliacaoPBL;
	}

	public void setPermitirProfessorReabrirAvaliacaoPBL(Boolean permitirProfessorReabrirAvaliacaoPBL) {
		this.permitirProfessorReabrirAvaliacaoPBL = permitirProfessorReabrirAvaliacaoPBL;
	}

	public Boolean getPermitirProfessorReabrirAtaPBL() {
		return permitirProfessorReabrirAtaPBL;
	}

	public void setPermitirProfessorReabrirAtaPBL(Boolean permitirProfessorReabrirAtaPBL) {
		this.permitirProfessorReabrirAtaPBL = permitirProfessorReabrirAtaPBL;
	}

	public Boolean getAvaliacaoOnlineProfessor() {
		return avaliacaoOnlineProfessor;
	}

	public void setAvaliacaoOnlineProfessor(Boolean avaliacaoOnlineProfessor) {
		this.avaliacaoOnlineProfessor = avaliacaoOnlineProfessor;
	}

	public Boolean getFuncionario_permitirCriarUsuario() {
		return funcionario_permitirCriarUsuario;
	}

	public void setFuncionario_permitirCriarUsuario(Boolean funcionario_permitirCriarUsuario) {
		this.funcionario_permitirCriarUsuario = funcionario_permitirCriarUsuario;
	}

	public Boolean getAlunoIniciarMatricula() {
		return alunoIniciarMatricula;
	}

	public void setAlunoIniciarMatricula(Boolean alunoIniciarMatricula) {
		this.alunoIniciarMatricula = alunoIniciarMatricula;
	}

	public Boolean getConfiguracaoConteudoTurmaVisaoProfessor() {
		return configuracaoConteudoTurmaVisaoProfessor;
	}

	public void setConfiguracaoConteudoTurmaVisaoProfessor(Boolean configuracaoConteudoTurmaVisaoProfessor) {
		this.configuracaoConteudoTurmaVisaoProfessor = configuracaoConteudoTurmaVisaoProfessor;
	}

	public Boolean getMonitoramentoAlunosEADVisaoProfessor() {
		return monitoramentoAlunosEADVisaoProfessor;
	}

	public void setMonitoramentoAlunosEADVisaoProfessor(Boolean monitoramentoAlunosEADVisaoProfessor) {
		this.monitoramentoAlunosEADVisaoProfessor = monitoramentoAlunosEADVisaoProfessor;
	}

	public Boolean getMonitoramentoAlunosEADFavorito() {
		return monitoramentoAlunosEADFavorito;
	}

	public void setMonitoramentoAlunosEADFavorito(Boolean monitoramentoAlunosEADFavorito) {
		this.monitoramentoAlunosEADFavorito = monitoramentoAlunosEADFavorito;
	}

	public Boolean getSolicitarAlterarSenha() {
		return solicitarAlterarSenha;
	}

	public void setSolicitarAlterarSenha(Boolean solicitarAlterarSenha) {
		this.solicitarAlterarSenha = solicitarAlterarSenha;
	}

	public Boolean getSolicitarAlterarSenhaFavorito() {
		return solicitarAlterarSenhaFavorito;
	}

	public void setSolicitarAlterarSenhaFavorito(Boolean solicitarAlterarSenhaFavorito) {
		this.solicitarAlterarSenhaFavorito = solicitarAlterarSenhaFavorito;
	}

	public Boolean getMonitoramentoAlunosEADVisaoCoodernador() {
		return monitoramentoAlunosEADVisaoCoodernador;
	}

	public void setMonitoramentoAlunosEADVisaoCoodernador(Boolean monitoramentoAlunosEADVisaoCoodernador) {
		this.monitoramentoAlunosEADVisaoCoodernador = monitoramentoAlunosEADVisaoCoodernador;
	}

	public Boolean getTemaAssunto() {
		return temaAssunto;
	}

	public void setTemaAssunto(Boolean temaAssunto) {
		this.temaAssunto = temaAssunto;
	}

	public Boolean getTemaAssuntoFavorito() {
		return temaAssuntoFavorito;
	}

	public void setTemaAssuntoFavorito(Boolean temaAssuntoFavorito) {
		this.temaAssuntoFavorito = temaAssuntoFavorito;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline() {
		return permitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline;
	}

	public void setPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline(Boolean permitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline) {
		this.permitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline = permitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoSemInformarConteudoOnline() {
		return permitirProfessorCadastrarQuestaoSemInformarConteudoOnline;
	}

	public void setPermitirProfessorCadastrarQuestaoSemInformarConteudoOnline(Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoOnline) {
		this.permitirProfessorCadastrarQuestaoSemInformarConteudoOnline = permitirProfessorCadastrarQuestaoSemInformarConteudoOnline;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial() {
		return permitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial;
	}

	public void setPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial(Boolean permitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial) {
		this.permitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial = permitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial() {
		return permitirProfessorCadastrarQuestaoSemInformarConteudoPresencial;
	}

	public void setPermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial(Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoPresencial) {
		this.permitirProfessorCadastrarQuestaoSemInformarConteudoPresencial = permitirProfessorCadastrarQuestaoSemInformarConteudoPresencial;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio() {
		return permitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio;
	}

	public void setPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio(Boolean permitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio) {
		this.permitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio = permitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio() {
		return permitirProfessorCadastrarQuestaoSemInformarConteudoExercicio;
	}

	public void setPermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio(Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoExercicio) {
		this.permitirProfessorCadastrarQuestaoSemInformarConteudoExercicio = permitirProfessorCadastrarQuestaoSemInformarConteudoExercicio;
	}

	public Boolean getEstatisticaAcademicaPorConvenioRel() {
		return estatisticaAcademicaPorConvenioRel;
	}

	public void setEstatisticaAcademicaPorConvenioRel(Boolean estatisticaAcademicaPorConvenioRel) {
		this.estatisticaAcademicaPorConvenioRel = estatisticaAcademicaPorConvenioRel;
	}

	public Boolean getEstatisticaAcademicaPorConvenioRelFavorito() {
		return estatisticaAcademicaPorConvenioRelFavorito;
	}

	public void setEstatisticaAcademicaPorConvenioRelFavorito(Boolean estatisticaAcademicaPorConvenioRelFavorito) {
		this.estatisticaAcademicaPorConvenioRelFavorito = estatisticaAcademicaPorConvenioRelFavorito;
	}

	public Boolean getParametrosMonitoramentoAvaliacaoOnline() {
		return parametrosMonitoramentoAvaliacaoOnline;
	}

	public void setParametrosMonitoramentoAvaliacaoOnline(Boolean parametrosMonitoramentoAvaliacaoOnline) {
		this.parametrosMonitoramentoAvaliacaoOnline = parametrosMonitoramentoAvaliacaoOnline;
	}

	public Boolean getParametrosMonitoramentoAvaliacaoOnlineFavorito() {
		return parametrosMonitoramentoAvaliacaoOnlineFavorito;
	}

	public void setParametrosMonitoramentoAvaliacaoOnlineFavorito(Boolean parametrosMonitoramentoAvaliacaoOnlineFavorito) {
		this.parametrosMonitoramentoAvaliacaoOnlineFavorito = parametrosMonitoramentoAvaliacaoOnlineFavorito;
	}

	public Boolean getLocalArmazenamentoFavorito() {

		return localArmazenamentoFavorito;
	}

	public void setLocalArmazenamentoFavorito(Boolean localArmazenamentoFavorito) {
		this.localArmazenamentoFavorito = localArmazenamentoFavorito;
	}

	public Boolean getLocalArmazenamento() {

		return localArmazenamento;
	}

	public void setLocalArmazenamento(Boolean localArmazenamento) {
		this.localArmazenamento = localArmazenamento;
	}

	public Boolean getMatriculaOnlineVisaoAluno() {
		return matriculaOnlineVisaoAluno;
	}

	public void setMatriculaOnlineVisaoAluno(Boolean matriculaOnlineVisaoAluno) {
		this.matriculaOnlineVisaoAluno = matriculaOnlineVisaoAluno;
	}

	public Boolean getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(Boolean tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	public Boolean getCategoriaTurma() {
		return categoriaTurma;
	}

	public void setCategoriaTurma(Boolean categoriaTurma) {
		this.categoriaTurma = categoriaTurma;
	}

	public Boolean getTipoCategoriaFavorito() {
		return tipoCategoriaFavorito;
	}

	public void setTipoCategoriaFavorito(Boolean tipoCategoriaFavorito) {
		this.tipoCategoriaFavorito = tipoCategoriaFavorito;
	}

	public Boolean getCategoriaTurmaFavorito() {
		return categoriaTurmaFavorito;
	}

	public void setCategoriaTurmaFavorito(Boolean categoriaTurmaFavorito) {
		this.categoriaTurmaFavorito = categoriaTurmaFavorito;
	}

	public Boolean getPainelGestorMonitorarPorSegmentacao() {
		return painelGestorMonitorarPorSegmentacao;
	}

	public void setPainelGestorMonitorarPorSegmentacao(Boolean painelGestorMonitorarPorSegmentacao) {
		this.painelGestorMonitorarPorSegmentacao = painelGestorMonitorarPorSegmentacao;
	}

	public Boolean getTextoPadraoPatrimonio() {
		return textoPadraoPatrimonio;
	}

	public void setTextoPadraoPatrimonio(Boolean textoPadraoPatrimonio) {
		this.textoPadraoPatrimonio = textoPadraoPatrimonio;
	}

	public Boolean getTextoPadraoPatrimonioFavorito() {
		return textoPadraoPatrimonioFavorito;
	}

	public void setTextoPadraoPatrimonioFavorito(Boolean textoPadraoPatrimonioFavorito) {
		this.textoPadraoPatrimonioFavorito = textoPadraoPatrimonioFavorito;
	}

	public Boolean getIntegracaoFinanceiro() {
		return integracaoFinanceiro;
	}

	public void setIntegracaoFinanceiro(Boolean integracaoFinanceiro) {
		this.integracaoFinanceiro = integracaoFinanceiro;
	}

	public Boolean getIntegracaoFinanceiroFavorito() {
		return integracaoFinanceiroFavorito;
	}

	public void setIntegracaoFinanceiroFavorito(Boolean integracaoFinanceiroFavorito) {
		this.integracaoFinanceiroFavorito = integracaoFinanceiroFavorito;
	}

	public Boolean getDefinirResponsavelFinanceiro() {
		return definirResponsavelFinanceiro;
	}

	public void setDefinirResponsavelFinanceiro(Boolean definirResponsavelFinanceiro) {
		this.definirResponsavelFinanceiro = definirResponsavelFinanceiro;
	}

	public Boolean getDefinirResponsavelFinanceiroFavorito() {
		return definirResponsavelFinanceiroFavorito;
	}

	public void setDefinirResponsavelFinanceiroFavorito(Boolean definirResponsavelFinanceiroFavorito) {
		this.definirResponsavelFinanceiroFavorito = definirResponsavelFinanceiroFavorito;
	}

	public Boolean getPersonalizacaoMensagemAutomaticaFavorito() {
		if (personalizacaoMensagemAutomaticaFavorito == null) {
			personalizacaoMensagemAutomaticaFavorito = Boolean.FALSE;
		}
		return personalizacaoMensagemAutomaticaFavorito;
	}

	public void setPersonalizacaoMensagemAutomaticaFavorito(Boolean personalizacaoMensagemAutomaticaFavorito) {
		this.personalizacaoMensagemAutomaticaFavorito = personalizacaoMensagemAutomaticaFavorito;
	}

	public Boolean getPersonalizacaoMensagemAutomatica() {
		if (personalizacaoMensagemAutomatica == null) {
			personalizacaoMensagemAutomatica = false;
		}
		return personalizacaoMensagemAutomatica;
	}

	public void setPersonalizacaoMensagemAutomatica(Boolean personalizacaoMensagemAutomatica) {
		this.personalizacaoMensagemAutomatica = personalizacaoMensagemAutomatica;
	}

	public Boolean getBiometria() {
		return biometria;
	}

	public void setBiometria(Boolean biometria) {
		this.biometria = biometria;
	}

	public Boolean getBiometriaFavorito() {
		return biometriaFavorito;
	}

	public void setBiometriaFavorito(Boolean biometriaFavorito) {
		this.biometriaFavorito = biometriaFavorito;
	}

	public Boolean getProcessarResultadoProvaPresencial() {
		return processarResultadoProvaPresencial;
	}

	public void setProcessarResultadoProvaPresencial(Boolean processarResultadoProvaPresencial) {
		this.processarResultadoProvaPresencial = processarResultadoProvaPresencial;
	}

	public Boolean getCatraca() {
		return catraca;
	}

	public void setCatraca(Boolean catraca) {
		this.catraca = catraca;
	}

	public Boolean getCatracaFavorito() {
		return catracaFavorito;
	}

	public void setCatracaFavorito(Boolean catracaFavorito) {
		this.catracaFavorito = catracaFavorito;
	}

	public Boolean getProcessarResultadoProvaPresencialFavorito() {
		return processarResultadoProvaPresencialFavorito;
	}

	public void setProcessarResultadoProvaPresencialFavorito(Boolean processarResultadoProvaPresencialFavorito) {
		this.processarResultadoProvaPresencialFavorito = processarResultadoProvaPresencialFavorito;
	}

	/**
	 * @return the ocorrenciaPatrimonio
	 */
	public Boolean getOcorrenciaPatrimonio() {
		if (ocorrenciaPatrimonio == null) {
			ocorrenciaPatrimonio = false;
		}
		return ocorrenciaPatrimonio;
	}

	/**
	 * @param ocorrenciaPatrimonio the ocorrenciaPatrimonio to set
	 */
	public void setOcorrenciaPatrimonio(Boolean ocorrenciaPatrimonio) {
		this.ocorrenciaPatrimonio = ocorrenciaPatrimonio;
	}

	/**
	 * @return the ocorrenciaPatrimonioFavorito
	 */
	public Boolean getOcorrenciaPatrimonioFavorito() {
		if (ocorrenciaPatrimonioFavorito == null) {
			ocorrenciaPatrimonioFavorito = false;
		}
		return ocorrenciaPatrimonioFavorito;
	}

	/**
	 * @param ocorrenciaPatrimonioFavorito the ocorrenciaPatrimonioFavorito to set
	 */
	public void setOcorrenciaPatrimonioFavorito(Boolean ocorrenciaPatrimonioFavorito) {
		this.ocorrenciaPatrimonioFavorito = ocorrenciaPatrimonioFavorito;
	}

	/**
	 * @return the permitirCadastrarOcorrenciaManutencao
	 */
	public Boolean getPermitirCadastrarOcorrenciaManutencao() {
		if (permitirCadastrarOcorrenciaManutencao == null) {
			permitirCadastrarOcorrenciaManutencao = false;
		}
		return permitirCadastrarOcorrenciaManutencao;
	}

	/**
	 * @param permitirCadastrarOcorrenciaManutencao the permitirCadastrarOcorrenciaManutencao to set
	 */
	public void setPermitirCadastrarOcorrenciaManutencao(Boolean permitirCadastrarOcorrenciaManutencao) {
		this.permitirCadastrarOcorrenciaManutencao = permitirCadastrarOcorrenciaManutencao;
	}

	/**
	 * @return the permitirCadastrarOcorrenciaEmprestimo
	 */
	public Boolean getPermitirCadastrarOcorrenciaEmprestimo() {
		if (permitirCadastrarOcorrenciaEmprestimo == null) {
			permitirCadastrarOcorrenciaEmprestimo = false;
		}
		return permitirCadastrarOcorrenciaEmprestimo;
	}

	/**
	 * @param permitirCadastrarOcorrenciaEmprestimo the permitirCadastrarOcorrenciaEmprestimo to set
	 */
	public void setPermitirCadastrarOcorrenciaEmprestimo(Boolean permitirCadastrarOcorrenciaEmprestimo) {
		this.permitirCadastrarOcorrenciaEmprestimo = permitirCadastrarOcorrenciaEmprestimo;
	}

	/**
	 * @return the permitirCadastrarOcorrenciaDescarte
	 */
	public Boolean getPermitirCadastrarOcorrenciaDescarte() {
		if (permitirCadastrarOcorrenciaDescarte == null) {
			permitirCadastrarOcorrenciaDescarte = false;
		}
		return permitirCadastrarOcorrenciaDescarte;
	}

	/**
	 * @param permitirCadastrarOcorrenciaDescarte the permitirCadastrarOcorrenciaDescarte to set
	 */
	public void setPermitirCadastrarOcorrenciaDescarte(Boolean permitirCadastrarOcorrenciaDescarte) {
		this.permitirCadastrarOcorrenciaDescarte = permitirCadastrarOcorrenciaDescarte;
	}

	/**
	 * @return the permitirCadastrarOcorrenciaTrocaLocal
	 */
	public Boolean getPermitirCadastrarOcorrenciaTrocaLocal() {
		if (permitirCadastrarOcorrenciaTrocaLocal == null) {
			permitirCadastrarOcorrenciaTrocaLocal = false;
		}
		return permitirCadastrarOcorrenciaTrocaLocal;
	}

	/**
	 * @param permitirCadastrarOcorrenciaTrocaLocal the permitirCadastrarOcorrenciaTrocaLocal to set
	 */
	public void setPermitirCadastrarOcorrenciaTrocaLocal(Boolean permitirCadastrarOcorrenciaTrocaLocal) {
		this.permitirCadastrarOcorrenciaTrocaLocal = permitirCadastrarOcorrenciaTrocaLocal;
	}

	/**
	 * @return the permitirCadastrarOcorrenciaSeparacaoDescarte
	 */
	public Boolean getPermitirCadastrarOcorrenciaSeparacaoDescarte() {
		if (permitirCadastrarOcorrenciaSeparacaoDescarte == null) {
			permitirCadastrarOcorrenciaSeparacaoDescarte = false;
		}
		return permitirCadastrarOcorrenciaSeparacaoDescarte;
	}

	/**
	 * @param permitirCadastrarOcorrenciaSeparacaoDescarte the permitirCadastrarOcorrenciaSeparacaoDescarte to set
	 */
	public void setPermitirCadastrarOcorrenciaSeparacaoDescarte(Boolean permitirCadastrarOcorrenciaSeparacaoDescarte) {
		this.permitirCadastrarOcorrenciaSeparacaoDescarte = permitirCadastrarOcorrenciaSeparacaoDescarte;
	}

	/**
	 * @return the patrimonioRel
	 */
	public Boolean getPatrimonioRel() {
		if (patrimonioRel == null) {
			patrimonioRel = false;
		}
		return patrimonioRel;
	}

	/**
	 * @param patrimonioRel the patrimonioRel to set
	 */
	public void setPatrimonioRel(Boolean patrimonioRel) {
		this.patrimonioRel = patrimonioRel;
	}

	/**
	 * @return the patrimonioRelFavorito
	 */
	public Boolean getPatrimonioRelFavorito() {
		if (patrimonioRelFavorito == null) {
			patrimonioRelFavorito = false;
		}
		return patrimonioRelFavorito;
	}

	/**
	 * @param patrimonioRelFavorito the patrimonioRelFavorito to set
	 */
	public void setPatrimonioRelFavorito(Boolean patrimonioRelFavorito) {
		this.patrimonioRelFavorito = patrimonioRelFavorito;
	}

	/**
	 * @return the mapaPatrimonioSeparadoDescarte
	 */
	public Boolean getMapaPatrimonioSeparadoDescarte() {
		if (mapaPatrimonioSeparadoDescarte == null) {
			mapaPatrimonioSeparadoDescarte = false;
		}
		return mapaPatrimonioSeparadoDescarte;
	}

	/**
	 * @param mapaPatrimonioSeparadoDescarte the mapaPatrimonioSeparadoDescarte to set
	 */
	public void setMapaPatrimonioSeparadoDescarte(Boolean mapaPatrimonioSeparadoDescarte) {
		this.mapaPatrimonioSeparadoDescarte = mapaPatrimonioSeparadoDescarte;
	}

	/**
	 * @return the mapaPatrimonioSeparadoDescarteFavorito
	 */
	public Boolean getMapaPatrimonioSeparadoDescarteFavorito() {
		if (mapaPatrimonioSeparadoDescarteFavorito == null) {
			mapaPatrimonioSeparadoDescarteFavorito = false;
		}
		return mapaPatrimonioSeparadoDescarteFavorito;
	}

	/**
	 * @param mapaPatrimonioSeparadoDescarteFavorito the mapaPatrimonioSeparadoDescarteFavorito to set
	 */
	public void setMapaPatrimonioSeparadoDescarteFavorito(Boolean mapaPatrimonioSeparadoDescarteFavorito) {
		this.mapaPatrimonioSeparadoDescarteFavorito = mapaPatrimonioSeparadoDescarteFavorito;
	}

	public Boolean getOcorrenciaPatrimonioRel() {
		if (ocorrenciaPatrimonioRel == null) {
			ocorrenciaPatrimonioRel = false;
		}
		return ocorrenciaPatrimonioRel;
	}

	public void setOcorrenciaPatrimonioRel(Boolean ocorrenciaPatrimonioRel) {
		this.ocorrenciaPatrimonioRel = ocorrenciaPatrimonioRel;
	}

	public Boolean getOcorrenciaPatrimonioRelFavorito() {
		if (ocorrenciaPatrimonioRelFavorito == null) {
			ocorrenciaPatrimonioRelFavorito = false;
		}
		return ocorrenciaPatrimonioRelFavorito;
	}

	public void setOcorrenciaPatrimonioRelFavorito(Boolean ocorrenciaPatrimonioRelFavorito) {
		this.ocorrenciaPatrimonioRelFavorito = ocorrenciaPatrimonioRelFavorito;
	}

	public Boolean getGrupoDisciplinaProcSeletivo() {
		return grupoDisciplinaProcSeletivo;
	}

	public void setGrupoDisciplinaProcSeletivo(Boolean grupoDisciplinaProcSeletivo) {
		this.grupoDisciplinaProcSeletivo = grupoDisciplinaProcSeletivo;
	}

	public Boolean getGrupoDisciplinaProcSeletivoFavorito() {
		return grupoDisciplinaProcSeletivoFavorito;
	}

	public void setGrupoDisciplinaProcSeletivoFavorito(Boolean grupoDisciplinaProcSeletivoFavorito) {
		this.grupoDisciplinaProcSeletivoFavorito = grupoDisciplinaProcSeletivoFavorito;
	}

	public Boolean getLiberarAlteracaoTaxaOperacaoCartaoCredito() {
		if (liberarAlteracaoTaxaOperacaoCartaoCredito == null) {
			liberarAlteracaoTaxaOperacaoCartaoCredito = false;
		}
		return liberarAlteracaoTaxaOperacaoCartaoCredito;
	}

	public void setLiberarAlteracaoTaxaOperacaoCartaoCredito(Boolean liberarAlteracaoTaxaOperacaoCartaoCredito) {
		this.liberarAlteracaoTaxaOperacaoCartaoCredito = liberarAlteracaoTaxaOperacaoCartaoCredito;
	}

	public Boolean getLiberarAlteracaoContaCorrenteCartaoCredito() {
		if (liberarAlteracaoContaCorrenteCartaoCredito == null) {
			liberarAlteracaoContaCorrenteCartaoCredito = false;
		}
		return liberarAlteracaoContaCorrenteCartaoCredito;
	}

	public void setLiberarAlteracaoContaCorrenteCartaoCredito(Boolean liberarAlteracaoContaCorrenteCartaoCredito) {
		this.liberarAlteracaoContaCorrenteCartaoCredito = liberarAlteracaoContaCorrenteCartaoCredito;
	}

	public Boolean getMapaNotaPendenciaAlunoRel() {
		return mapaNotaPendenciaAlunoRel;
	}

	public void setMapaNotaPendenciaAlunoRel(Boolean mapaNotaPendenciaAlunoRel) {
		this.mapaNotaPendenciaAlunoRel = mapaNotaPendenciaAlunoRel;
	}

	public Boolean getMapaNotaPendenciaAlunoRelFavorito() {
		return mapaNotaPendenciaAlunoRelFavorito;
	}

	public void setMapaNotaPendenciaAlunoRelFavorito(Boolean mapaNotaPendenciaAlunoRelFavorito) {
		this.mapaNotaPendenciaAlunoRelFavorito = mapaNotaPendenciaAlunoRelFavorito;
	}

	public Boolean getPermitirAlterarDataMatricula() {
		return permitirAlterarDataMatricula;
	}

	public void setPermitirAlterarDataMatricula(Boolean permitirAlterarDataMatricula) {
		this.permitirAlterarDataMatricula = permitirAlterarDataMatricula;
	}

	public Boolean getPermiteAlterarPeriodoCalendario() {
		return permiteAlterarPeriodoCalendario;
	}

	public void setPermiteAlterarPeriodoCalendario(Boolean permiteAlterarPeriodoCalendario) {
		this.permiteAlterarPeriodoCalendario = permiteAlterarPeriodoCalendario;
	}

	/**
	 * @return the permiteLiberarProgramacaoAulaProfessorAcimaPermitido
	 */
	public Boolean getPermiteLiberarProgramacaoAulaProfessorAcimaPermitido() {
		if (permiteLiberarProgramacaoAulaProfessorAcimaPermitido == null) {
			permiteLiberarProgramacaoAulaProfessorAcimaPermitido = false;
		}
		return permiteLiberarProgramacaoAulaProfessorAcimaPermitido;
	}

	/**
	 * @param permiteLiberarProgramacaoAulaProfessorAcimaPermitido the permiteLiberarProgramacaoAulaProfessorAcimaPermitido to set
	 */
	public void setPermiteLiberarProgramacaoAulaProfessorAcimaPermitido(Boolean permiteLiberarProgramacaoAulaProfessorAcimaPermitido) {
		this.permiteLiberarProgramacaoAulaProfessorAcimaPermitido = permiteLiberarProgramacaoAulaProfessorAcimaPermitido;
	}

	public Boolean getRegistroAulaLancadaNaoLancadaRel() {
		return registroAulaLancadaNaoLancadaRel;
	}

	public void setRegistroAulaLancadaNaoLancadaRel(Boolean registroAulaLancadaNaoLancadaRel) {
		this.registroAulaLancadaNaoLancadaRel = registroAulaLancadaNaoLancadaRel;
	}

	public Boolean getRegistroAulaLancadaNaoLancadaRelFavorito() {
		return registroAulaLancadaNaoLancadaRelFavorito;
	}

	public void setRegistroAulaLancadaNaoLancadaRelFavorito(Boolean registroAulaLancadaNaoLancadaRelFavorito) {
		this.registroAulaLancadaNaoLancadaRelFavorito = registroAulaLancadaNaoLancadaRelFavorito;
	}

	public Boolean getObservacaoComplementar() {
		return observacaoComplementar;
	}

	public void setObservacaoComplementar(Boolean observacaoComplementar) {
		this.observacaoComplementar = observacaoComplementar;
	}

	public Boolean getObservacaoComplementarFavorito() {
		return observacaoComplementarFavorito;
	}

	public void setObservacaoComplementarFavorito(Boolean observacaoComplementarFavorito) {
		this.observacaoComplementarFavorito = observacaoComplementarFavorito;
	}

	public Boolean getHorarioAlunoRelFavorito() {
		return horarioAlunoRelFavorito;
	}

	public void setHorarioAlunoRelFavorito(Boolean horarioAlunoRelFavorito) {
		this.horarioAlunoRelFavorito = horarioAlunoRelFavorito;
	}

	public Boolean getFrequenciaAlunoRel() {
		return frequenciaAlunoRel;
	}

	public void setFrequenciaAlunoRel(Boolean frequenciaAlunoRel) {
		this.frequenciaAlunoRel = frequenciaAlunoRel;
	}

	public Boolean getFrequenciaAlunoRelFavorito() {
		return frequenciaAlunoRelFavorito;
	}

	public void setFrequenciaAlunoRelFavorito(Boolean frequenciaAlunoRelFavorito) {
		this.frequenciaAlunoRelFavorito = frequenciaAlunoRelFavorito;
	}

	private Boolean operacaoFinanceiraCaixaRel = false;
	private Boolean operacaoFinanceiraCaixaRelFavorito = false;

	/**
	 * @return the operacaoFinanceiraCaixaRel
	 */
	public Boolean getOperacaoFinanceiraCaixaRel() {
		if (operacaoFinanceiraCaixaRel == null) {
			operacaoFinanceiraCaixaRel = false;
		}
		return operacaoFinanceiraCaixaRel;
	}

	/**
	 * @param operacaoFinanceiraCaixaRel the operacaoFinanceiraCaixaRel to set
	 */
	public void setOperacaoFinanceiraCaixaRel(Boolean operacaoFinanceiraCaixaRel) {
		this.operacaoFinanceiraCaixaRel = operacaoFinanceiraCaixaRel;
	}

	/**
	 * @return the operacaoFinanceiraCaixaRelFavorito
	 */
	public Boolean getOperacaoFinanceiraCaixaRelFavorito() {
		if (operacaoFinanceiraCaixaRelFavorito == null) {
			operacaoFinanceiraCaixaRelFavorito = false;
		}
		return operacaoFinanceiraCaixaRelFavorito;
	}

	/**
	 * @param operacaoFinanceiraCaixaRelFavorito the operacaoFinanceiraCaixaRelFavorito to set
	 */
	public void setOperacaoFinanceiraCaixaRelFavorito(Boolean operacaoFinanceiraCaixaRelFavorito) {
		this.operacaoFinanceiraCaixaRelFavorito = operacaoFinanceiraCaixaRelFavorito;
	}

	public Boolean getAlteracaoPlanoFinanceiroAlunoFavorito() {
		return alteracaoPlanoFinanceiroAlunoFavorito;
	}

	public void setAlteracaoPlanoFinanceiroAlunoFavorito(Boolean alteracaoPlanoFinanceiroAlunoFavorito) {
		this.alteracaoPlanoFinanceiroAlunoFavorito = alteracaoPlanoFinanceiroAlunoFavorito;
	}

	public Boolean getAlteracaoPlanoFinanceiroAluno() {
		return alteracaoPlanoFinanceiroAluno;
	}

	public void setAlteracaoPlanoFinanceiroAluno(Boolean alteracaoPlanoFinanceiroAluno) {
		this.alteracaoPlanoFinanceiroAluno = alteracaoPlanoFinanceiroAluno;
	}

//	public Boolean getMapaRegistroAbandonoCursoTrancamento() {
//		return mapaRegistroAbandonoCursoTrancamento;
//	}
//
//	public void setMapaRegistroAbandonoCursoTrancamento(Boolean mapaRegistroAbandonoCursoTrancamento) {
//		this.mapaRegistroAbandonoCursoTrancamento = mapaRegistroAbandonoCursoTrancamento;
//	}
//
//	public Boolean getMapaRegistroAbandonoCursoTrancamentoFavorito() {
//		return mapaRegistroAbandonoCursoTrancamentoFavorito;
//	}
//
//	public void setMapaRegistroAbandonoCursoTrancamentoFavorito(Boolean mapaRegistroAbandonoCursoTrancamentoFavorito) {
//		this.mapaRegistroAbandonoCursoTrancamentoFavorito = mapaRegistroAbandonoCursoTrancamentoFavorito;
//	}
	
	

	public Boolean getMapaRegistroEvasaoCurso() {
		return mapaRegistroEvasaoCurso;
	}
	
	public void setMapaRegistroEvasaoCurso(Boolean mapaRegistroEvasaoCurso) {
		this.mapaRegistroEvasaoCurso = mapaRegistroEvasaoCurso;
	}
	
	public Boolean getMapaRegistroEvasaoCursoFavorito() {
		return mapaRegistroEvasaoCursoFavorito;
	}
	
	public void setMapaRegistroEvasaoCursoFavorito(Boolean mapaRegistroEvasaoCursoFavorito) {
		this.mapaRegistroEvasaoCursoFavorito = mapaRegistroEvasaoCursoFavorito;
	}
	
	
	public Boolean getAlterarResponsavelRequerimento() {
		return alterarResponsavelRequerimento;
	}

	public void setAlterarResponsavelRequerimento(Boolean alterarResponsavelRequerimento) {
		this.alterarResponsavelRequerimento = alterarResponsavelRequerimento;
	}

	public Boolean getAlterarResponsavelRequerimentoFavorito() {
		return alterarResponsavelRequerimentoFavorito;
	}

	public void setAlterarResponsavelRequerimentoFavorito(Boolean alterarResponsavelRequerimentoFavorito) {
		this.alterarResponsavelRequerimentoFavorito = alterarResponsavelRequerimentoFavorito;
	}

	private Boolean permitirIncluirDisciplinaPorEquivalencia;
	private Boolean permitirIncluirDisciplinaOptativa;
	private Boolean incluirDisciplinaApenasTurmaProprioUnidadeEnsino;
	private Boolean incluirDisciplinaApenasTurmaProprioCurso;
	private Boolean incluirDisciplinaApenasTurmaProprioMatrizCurricular;

	/**
	 * @return the permitirIncluirDisciplinaPorEquivalencia
	 */
	public Boolean getPermitirIncluirDisciplinaPorEquivalencia() {
		if (permitirIncluirDisciplinaPorEquivalencia == null) {
			permitirIncluirDisciplinaPorEquivalencia = false;
		}
		return permitirIncluirDisciplinaPorEquivalencia;
	}

	/**
	 * @param permitirIncluirDisciplinaPorEquivalencia the permitirIncluirDisciplinaPorEquivalencia to set
	 */
	public void setPermitirIncluirDisciplinaPorEquivalencia(Boolean permitirIncluirDisciplinaPorEquivalencia) {
		this.permitirIncluirDisciplinaPorEquivalencia = permitirIncluirDisciplinaPorEquivalencia;
	}

	/**
	 * @return the permitirIncluirDisciplinaOptativa
	 */
	public Boolean getPermitirIncluirDisciplinaOptativa() {
		if (permitirIncluirDisciplinaOptativa == null) {
			permitirIncluirDisciplinaOptativa = false;
		}
		return permitirIncluirDisciplinaOptativa;
	}

	/**
	 * @param permitirIncluirDisciplinaOptativa the permitirIncluirDisciplinaOptativa to set
	 */
	public void setPermitirIncluirDisciplinaOptativa(Boolean permitirIncluirDisciplinaOptativa) {
		this.permitirIncluirDisciplinaOptativa = permitirIncluirDisciplinaOptativa;
	}

	/**
	 * @return the incluirDisciplinaApenasTurmaProprioCurso
	 */
	public Boolean getIncluirDisciplinaApenasTurmaProprioCurso() {
		if (incluirDisciplinaApenasTurmaProprioCurso == null) {
			incluirDisciplinaApenasTurmaProprioCurso = false;
		}
		return incluirDisciplinaApenasTurmaProprioCurso;
	}

	/**
	 * @param incluirDisciplinaApenasTurmaProprioCurso the incluirDisciplinaApenasTurmaProprioCurso to set
	 */
	public void setIncluirDisciplinaApenasTurmaProprioCurso(Boolean incluirDisciplinaApenasTurmaProprioCurso) {
		this.incluirDisciplinaApenasTurmaProprioCurso = incluirDisciplinaApenasTurmaProprioCurso;
	}
	
	

	public Boolean getIncluirDisciplinaApenasTurmaProprioUnidadeEnsino() {
		if (incluirDisciplinaApenasTurmaProprioUnidadeEnsino == null) {
			incluirDisciplinaApenasTurmaProprioUnidadeEnsino = false;
		}
		return incluirDisciplinaApenasTurmaProprioUnidadeEnsino;
	}

	public void setIncluirDisciplinaApenasTurmaProprioUnidadeEnsino(Boolean incluirDisciplinaApenasTurmaProprioUnidadeEnsino) {
		this.incluirDisciplinaApenasTurmaProprioUnidadeEnsino = incluirDisciplinaApenasTurmaProprioUnidadeEnsino;
	}

	public Boolean getIncluirDisciplinaApenasTurmaProprioMatrizCurricular() {
		if (incluirDisciplinaApenasTurmaProprioMatrizCurricular == null) {
			incluirDisciplinaApenasTurmaProprioMatrizCurricular = false;
		}
		return incluirDisciplinaApenasTurmaProprioMatrizCurricular;
	}

	public void setIncluirDisciplinaApenasTurmaProprioMatrizCurricular(Boolean incluirDisciplinaApenasTurmaProprioMatrizCurricular) {
		this.incluirDisciplinaApenasTurmaProprioMatrizCurricular = incluirDisciplinaApenasTurmaProprioMatrizCurricular;
	}

	private Boolean permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario;

	/**
	 * @return the permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario
	 */
	public Boolean getPermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario() {
		if (permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario == null) {
			permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario = false;
		}
		return permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario;
	}

	/**
	 * @param permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario the permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario to set
	 */
	public void setPermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario(Boolean permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario) {
		this.permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario = permiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario;
	}

	public Boolean getPermitirImprimirMatrizCurricularAluno() {
		return permitirImprimirMatrizCurricularAluno;
	}

	public void setPermitirImprimirMatrizCurricularAluno(Boolean permitirImprimirMatrizCurricularAluno) {
		this.permitirImprimirMatrizCurricularAluno = permitirImprimirMatrizCurricularAluno;
	}

	private Boolean permiteAlterarQuestoesOnlineAtivas;
	private Boolean permiteAlterarQuestoesExercicioAtivas;
	private Boolean permiteAlterarQuestoesPresencialAtivas;
	private Boolean permiteAlterarQuestoesAtividadeDiscursivaAtivas;

	public Boolean getPermiteAlterarQuestoesAtividadeDiscursivaAtivas() {
		if (permiteAlterarQuestoesAtividadeDiscursivaAtivas == null) {
			permiteAlterarQuestoesAtividadeDiscursivaAtivas = false;
		}
		return permiteAlterarQuestoesAtividadeDiscursivaAtivas;
	}

	public void setPermiteAlterarQuestoesAtividadeDiscursivaAtivas(Boolean permiteAlterarQuestoesAtividadeDiscursivaAtivas) {
		this.permiteAlterarQuestoesAtividadeDiscursivaAtivas = permiteAlterarQuestoesAtividadeDiscursivaAtivas;
	}

	public Boolean getPermiteAlterarQuestoesOnlineAtivas() {
		if (permiteAlterarQuestoesOnlineAtivas == null) {
			permiteAlterarQuestoesOnlineAtivas = false;
		}
		return permiteAlterarQuestoesOnlineAtivas;
	}

	public void setPermiteAlterarQuestoesOnlineAtivas(Boolean permiteAlterarQuestoesOnlineAtivas) {
		this.permiteAlterarQuestoesOnlineAtivas = permiteAlterarQuestoesOnlineAtivas;
	}

	public Boolean getPermiteAlterarQuestoesExercicioAtivas() {
		if (permiteAlterarQuestoesExercicioAtivas == null) {
			permiteAlterarQuestoesExercicioAtivas = false;
		}
		return permiteAlterarQuestoesExercicioAtivas;
	}

	public void setPermiteAlterarQuestoesExercicioAtivas(Boolean permiteAlterarQuestoesExercicioAtivas) {
		this.permiteAlterarQuestoesExercicioAtivas = permiteAlterarQuestoesExercicioAtivas;
	}

	public Boolean getPermiteAlterarQuestoesPresencialAtivas() {
		if (permiteAlterarQuestoesPresencialAtivas == null) {
			permiteAlterarQuestoesPresencialAtivas = false;
		}
		return permiteAlterarQuestoesPresencialAtivas;
	}

	public void setPermiteAlterarQuestoesPresencialAtivas(Boolean permiteAlterarQuestoesPresencialAtivas) {
		this.permiteAlterarQuestoesPresencialAtivas = permiteAlterarQuestoesPresencialAtivas;
	}

	public Boolean getNotaNaoLancadaProfessorRel() {
		return notaNaoLancadaProfessorRel;
	}

	public void setNotaNaoLancadaProfessorRel(Boolean notaNaoLancadaProfessorRel) {
		this.notaNaoLancadaProfessorRel = notaNaoLancadaProfessorRel;
	}

	public Boolean getNotaNaoLancadaProfessorRelFavorito() {
		return notaNaoLancadaProfessorRelFavorito;
	}

	public void setNotaNaoLancadaProfessorRelFavorito(Boolean notaNaoLancadaProfessorRelFavorito) {
		this.notaNaoLancadaProfessorRelFavorito = notaNaoLancadaProfessorRelFavorito;
	}

	private Boolean permitirGerarRelatorioSeiDecidirReceitaApenasDados;
	private Boolean permitirGerarRelatorioSeiDecidirDespesaApenasDados;
	private Boolean permitirGerarRelatorioSeiDecidirAcademicoApenasDados;
	private Boolean permitirGerarRelatorioSeiDecidirCrmApenasDados;

	public Boolean getPermitirGerarRelatorioSeiDecidirCrmApenasDados() {
		if (permitirGerarRelatorioSeiDecidirCrmApenasDados == null) {
			permitirGerarRelatorioSeiDecidirCrmApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirCrmApenasDados;
	}

	public void setPermitirGerarRelatorioSeiDecidirCrmApenasDados(Boolean permitirGerarRelatorioSeiDecidirCrmApenasDados) {
		this.permitirGerarRelatorioSeiDecidirCrmApenasDados = permitirGerarRelatorioSeiDecidirCrmApenasDados;
	}

	/**
	 * @return the permitirGerarRelatorioSeiDecidirReceitaApenasDados
	 */
	public Boolean getPermitirGerarRelatorioSeiDecidirReceitaApenasDados() {
		if (permitirGerarRelatorioSeiDecidirReceitaApenasDados == null) {
			permitirGerarRelatorioSeiDecidirReceitaApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirReceitaApenasDados;
	}

	/**
	 * @param permitirGerarRelatorioSeiDecidirReceitaApenasDados the permitirGerarRelatorioSeiDecidirReceitaApenasDados to set
	 */
	public void setPermitirGerarRelatorioSeiDecidirReceitaApenasDados(Boolean permitirGerarRelatorioSeiDecidirReceitaApenasDados) {
		this.permitirGerarRelatorioSeiDecidirReceitaApenasDados = permitirGerarRelatorioSeiDecidirReceitaApenasDados;
	}

	/**
	 * @return the permitirGerarRelatorioSeiDecidirDespesaApenasDados
	 */
	public Boolean getPermitirGerarRelatorioSeiDecidirDespesaApenasDados() {
		if (permitirGerarRelatorioSeiDecidirDespesaApenasDados == null) {
			permitirGerarRelatorioSeiDecidirDespesaApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirDespesaApenasDados;
	}

	/**
	 * @param permitirGerarRelatorioSeiDecidirDespesaApenasDados the permitirGerarRelatorioSeiDecidirDespesaApenasDados to set
	 */
	public void setPermitirGerarRelatorioSeiDecidirDespesaApenasDados(Boolean permitirGerarRelatorioSeiDecidirDespesaApenasDados) {
		this.permitirGerarRelatorioSeiDecidirDespesaApenasDados = permitirGerarRelatorioSeiDecidirDespesaApenasDados;
	}

	/**
	 * @return the permitirGerarRelatorioSeiDecidirAcademicoApenasDados
	 */
	public Boolean getPermitirGerarRelatorioSeiDecidirAcademicoApenasDados() {
		if (permitirGerarRelatorioSeiDecidirAcademicoApenasDados == null) {
			permitirGerarRelatorioSeiDecidirAcademicoApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirAcademicoApenasDados;
	}

	/**
	 * @param permitirGerarRelatorioSeiDecidirAcademicoApenasDados the permitirGerarRelatorioSeiDecidirAcademicoApenasDados to set
	 */
	public void setPermitirGerarRelatorioSeiDecidirAcademicoApenasDados(Boolean permitirGerarRelatorioSeiDecidirAcademicoApenasDados) {
		this.permitirGerarRelatorioSeiDecidirAcademicoApenasDados = permitirGerarRelatorioSeiDecidirAcademicoApenasDados;
	}

	public Boolean getConfiguracaoRecebimentoCartaoOnline() {
		if (configuracaoRecebimentoCartaoOnline == null) {
			configuracaoRecebimentoCartaoOnline = false;
		}
		return configuracaoRecebimentoCartaoOnline;
	}

	public void setConfiguracaoRecebimentoCartaoOnline(Boolean configuracaoRecebimentoCartaoOnline) {
		this.configuracaoRecebimentoCartaoOnline = configuracaoRecebimentoCartaoOnline;
	}

	public Boolean getConfiguracaoRecebimentoCartaoOnlineFavorito() {
		if (configuracaoRecebimentoCartaoOnlineFavorito == null) {
			configuracaoRecebimentoCartaoOnlineFavorito = false;
		}
		return configuracaoRecebimentoCartaoOnlineFavorito;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineFavorito(Boolean configuracaoRecebimentoCartaoOnlineFavorito) {
		this.configuracaoRecebimentoCartaoOnlineFavorito = configuracaoRecebimentoCartaoOnlineFavorito;
	}

	public Boolean getControleAcessoAlunoCatracaRel() {
		return controleAcessoAlunoCatracaRel;
	}

	public void setControleAcessoAlunoCatracaRel(Boolean controleAcessoAlunoCatracaRel) {
		this.controleAcessoAlunoCatracaRel = controleAcessoAlunoCatracaRel;
	}

	public Boolean getControleAcessoAlunoCatracaRelFavorito() {
		return controleAcessoAlunoCatracaRelFavorito;
	}

	public void setControleAcessoAlunoCatracaRelFavorito(Boolean controleAcessoAlunoCatracaRelFavorito) {
		this.controleAcessoAlunoCatracaRelFavorito = controleAcessoAlunoCatracaRelFavorito;
	}

	public Boolean getProcessoSeletivoRedacaoRel() {
		return processoSeletivoRedacaoRel;
	}

	public void setProcessoSeletivoRedacaoRel(Boolean processoSeletivoRedacaoRel) {
		this.processoSeletivoRedacaoRel = processoSeletivoRedacaoRel;
	}

	public Boolean getProcessoSeletivoRedacaoRelFavorito() {
		return processoSeletivoRedacaoRelFavorito;
	}

	public void setProcessoSeletivoRedacaoRelFavorito(Boolean processoSeletivoRedacaoRelFavorito) {
		this.processoSeletivoRedacaoRelFavorito = processoSeletivoRedacaoRelFavorito;
	}

	public Boolean getPermitirAlterarMatrizCurricularConstrucao() {
		return permitirAlterarMatrizCurricularConstrucao;
	}

	public void setPermitirAlterarMatrizCurricularConstrucao(Boolean permitirAlterarMatrizCurricularConstrucao) {
		this.permitirAlterarMatrizCurricularConstrucao = permitirAlterarMatrizCurricularConstrucao;
	}

	public Boolean getPermitirCancelarDCCPrevisto() {
		if (permitirCancelarDCCPrevisto == null) {
			permitirCancelarDCCPrevisto = false;
		}
		return permitirCancelarDCCPrevisto;
	}

	public void setPermitirCancelarDCCPrevisto(Boolean permitirCancelarDCCPrevisto) {
		this.permitirCancelarDCCPrevisto = permitirCancelarDCCPrevisto;
	}

	public Boolean getPermitirApresentarValorRecursoPatrimonioRel() {
		if (permitirApresentarValorRecursoPatrimonioRel == null) {
			permitirApresentarValorRecursoPatrimonioRel = false;
		}
		return permitirApresentarValorRecursoPatrimonioRel;
	}

	public void setPermitirApresentarValorRecursoPatrimonioRel(Boolean permitirApresentarValorRecursoPatrimonioRel) {
		this.permitirApresentarValorRecursoPatrimonioRel = permitirApresentarValorRecursoPatrimonioRel;
	}

	public Boolean getArquivoAssinado() {
		return arquivoAssinado;
	}

	public void setArquivoAssinado(Boolean arquivoAssinado) {
		this.arquivoAssinado = arquivoAssinado;
	}

	public Boolean getArquivoAssinadoFavorito() {
		return arquivoAssinadoFavorito;
	}

	public void setArquivoAssinadoFavorito(Boolean arquivoAssinadoFavorito) {
		this.arquivoAssinadoFavorito = arquivoAssinadoFavorito;
	}

	public Boolean getPermitirSimularAcessoVisaoAluno() {
		if (permitirSimularAcessoVisaoAluno == null) {
			permitirSimularAcessoVisaoAluno = false;
		}
		return permitirSimularAcessoVisaoAluno;
	}

	public void setPermitirSimularAcessoVisaoAluno(Boolean permitirSimularAcessoVisaoAluno) {
		this.permitirSimularAcessoVisaoAluno = permitirSimularAcessoVisaoAluno;
	}

	public Boolean getPermitirCadastrarConvenioMatricula() {
		return permitirCadastrarConvenioMatricula;
	}

	public void setPermitirCadastrarConvenioMatricula(Boolean permitirCadastrarConvenioMatricula) {
		this.permitirCadastrarConvenioMatricula = permitirCadastrarConvenioMatricula;
	}

	public Boolean getPermiteAlterarInformacoesAdicionaisConvenio() {
		return permiteAlterarInformacoesAdicionaisConvenio;
	}

	public void setPermiteAlterarInformacoesAdicionaisConvenio(Boolean permiteAlterarInformacoesAdicionaisConvenio) {
		this.permiteAlterarInformacoesAdicionaisConvenio = permiteAlterarInformacoesAdicionaisConvenio;
	}

	public Boolean getPermiteRegistrarEstagioSeguradora() {
		if (permiteRegistrarEstagioSeguradora == null) {
			permiteRegistrarEstagioSeguradora = false;
		}
		return permiteRegistrarEstagioSeguradora;
	}

	public void setPermiteRegistrarEstagioSeguradora(Boolean permiteRegistrarEstagioSeguradora) {
		this.permiteRegistrarEstagioSeguradora = permiteRegistrarEstagioSeguradora;
	}

	public Boolean getPermiteExcluirAulaProgramadaGravarFeriado() {
		if (permiteExcluirAulaProgramadaGravarFeriado == null) {
			permiteExcluirAulaProgramadaGravarFeriado = false;
		}
		return permiteExcluirAulaProgramadaGravarFeriado;
	}

	public void setPermiteExcluirAulaProgramadaGravarFeriado(Boolean permiteExcluirAulaProgramadaGravarFeriado) {
		this.permiteExcluirAulaProgramadaGravarFeriado = permiteExcluirAulaProgramadaGravarFeriado;
	}

	public Boolean getMotivoEmprestimoPorHora() {
		return motivoEmprestimoPorHora;
	}

	public void setMotivoEmprestimoPorHora(Boolean motivoEmprestimoPorHora) {
		this.motivoEmprestimoPorHora = motivoEmprestimoPorHora;
	}

	public Boolean getMotivoEmprestimoPorHoraFavorito() {
		return motivoEmprestimoPorHoraFavorito;
	}

	public void setMotivoEmprestimoPorHoraFavorito(Boolean motivoEmprestimoPorHoraFavorito) {
		this.motivoEmprestimoPorHoraFavorito = motivoEmprestimoPorHoraFavorito;
	}

	public Boolean getMapaEmail() {
		return mapaEmail;
	}

	public void setMapaEmail(Boolean mapaEmail) {
		this.mapaEmail = mapaEmail;
	}

	public Boolean getMapaEmailFavorito() {
		return mapaEmailFavorito;
	}

	public void setMapaEmailFavorito(Boolean mapaEmailFavorito) {
		this.mapaEmailFavorito = mapaEmailFavorito;
	}

	public Boolean getInclusaoHistoricoAluno() {
		return inclusaoHistoricoAluno;
	}

	public void setInclusaoHistoricoAluno(Boolean inclusaoHistoricoAluno) {
		this.inclusaoHistoricoAluno = inclusaoHistoricoAluno;
	}

	public Boolean getInclusaoHistoricoAlunoFavorito() {
		return inclusaoHistoricoAlunoFavorito;
	}

	public void setInclusaoHistoricoAlunoFavorito(Boolean inclusaoHistoricoAlunoFavorito) {
		this.inclusaoHistoricoAlunoFavorito = inclusaoHistoricoAlunoFavorito;
	}

	public Boolean getGestaoEventoConteudoTurmaVisaoProfessor() {
		if (gestaoEventoConteudoTurmaVisaoProfessor == null) {
			gestaoEventoConteudoTurmaVisaoProfessor = false;
		}
		return gestaoEventoConteudoTurmaVisaoProfessor;
	}

	public void setGestaoEventoConteudoTurmaVisaoProfessor(Boolean gestaoEventoConteudoTurmaVisaoProfessor) {
		this.gestaoEventoConteudoTurmaVisaoProfessor = gestaoEventoConteudoTurmaVisaoProfessor;
	}

	public Boolean getPermiteAlterarNotaAlunoAvaliacaoPBL() {
		if (permiteAlterarNotaAlunoAvaliacaoPBL == null) {
			permiteAlterarNotaAlunoAvaliacaoPBL = false;
		}
		return permiteAlterarNotaAlunoAvaliacaoPBL;
	}

	public void setPermiteAlterarNotaAlunoAvaliacaoPBL(Boolean permiteAlterarNotaAlunoAvaliacaoPBL) {
		this.permiteAlterarNotaAlunoAvaliacaoPBL = permiteAlterarNotaAlunoAvaliacaoPBL;
	}

	public Boolean getLivroMatriculaRel() {
		return livroMatriculaRel;
	}

	public void setLivroMatriculaRel(Boolean livroMatriculaRel) {
		this.livroMatriculaRel = livroMatriculaRel;
	}

	public Boolean getLivroMatriculaRelFavorito() {
		return livroMatriculaRelFavorito;
	}

	public void setLivroMatriculaRelFavorito(Boolean livroMatriculaRelFavorito) {
		this.livroMatriculaRelFavorito = livroMatriculaRelFavorito;
	}

	public Boolean getCalendarioAberturaRequerimento() {
		return calendarioAberturaRequerimento;
	}

	public void setCalendarioAberturaRequerimento(Boolean calendarioAberturaRequerimento) {
		this.calendarioAberturaRequerimento = calendarioAberturaRequerimento;
	}

	public Boolean getCalendarioAberturaRequerimentoFavorito() {
		return calendarioAberturaRequerimentoFavorito;
	}

	public void setCalendarioAberturaRequerimentoFavorito(Boolean calendarioAberturaRequerimentoFavorito) {
		this.calendarioAberturaRequerimentoFavorito = calendarioAberturaRequerimentoFavorito;
	}

	public Boolean getInutilizacaoNotaFiscal() {
		return inutilizacaoNotaFiscal;
	}

	public void setInutilizacaoNotaFiscal(Boolean inutilizacaoNotaFiscal) {
		this.inutilizacaoNotaFiscal = inutilizacaoNotaFiscal;
	}

	public Boolean getInutilizacaoNotaFiscalFavorito() {
		return inutilizacaoNotaFiscalFavorito;
	}

	public void setInutilizacaoNotaFiscalFavorito(Boolean inutilizacaoNotaFiscalFavorito) {
		this.inutilizacaoNotaFiscalFavorito = inutilizacaoNotaFiscalFavorito;
	}

	public Boolean getPermiteLancarNotaAlunoTransferenciaMatriz() {
		return permiteLancarNotaAlunoTransferenciaMatriz;
	}

	public void setPermiteLancarNotaAlunoTransferenciaMatriz(Boolean permiteLancarNotaAlunoTransferenciaMatriz) {
		this.permiteLancarNotaAlunoTransferenciaMatriz = permiteLancarNotaAlunoTransferenciaMatriz;
	}

	public Boolean getPermiteRegistrarAulaAlunoTransferenciaMatriz() {
		return permiteRegistrarAulaAlunoTransferenciaMatriz;
	}

	public void setPermiteRegistrarAulaAlunoTransferenciaMatriz(Boolean permiteRegistrarAulaAlunoTransferenciaMatriz) {
		this.permiteRegistrarAulaAlunoTransferenciaMatriz = permiteRegistrarAulaAlunoTransferenciaMatriz;
	}

	public Boolean getPermiteEstornarTransferenciaSaida() {
		if (permiteEstornarTransferenciaSaida == null) {
			permiteEstornarTransferenciaSaida = false;
		}
		return permiteEstornarTransferenciaSaida;
	}

	public void setPermiteEstornarTransferenciaSaida(Boolean permiteEstornarTransferenciaSaida) {
		this.permiteEstornarTransferenciaSaida = permiteEstornarTransferenciaSaida;
	}

	public Boolean getPermiteEstornarTransferenciaInterna() {
		if (permiteEstornarTransferenciaInterna == null) {
			permiteEstornarTransferenciaInterna = false;
		}
		return permiteEstornarTransferenciaInterna;
	}

	public void setPermiteEstornarTransferenciaInterna(Boolean permiteEstornarTransferenciaInterna) {
		this.permiteEstornarTransferenciaInterna = permiteEstornarTransferenciaInterna;
	}

	public Boolean getTipoJustificativaFalta() {
		return tipoJustificativaFalta;
	}

	public void setTipoJustificativaFalta(Boolean tipoJustificativaFalta) {
		this.tipoJustificativaFalta = tipoJustificativaFalta;
	}

	public Boolean getTipoJustificativaFaltaFavorito() {
		return tipoJustificativaFaltaFavorito;
	}

	public void setTipoJustificativaFaltaFavorito(Boolean tipoJustificativaFaltaFavorito) {
		this.tipoJustificativaFaltaFavorito = tipoJustificativaFaltaFavorito;
	}

	public Boolean getConfiguracaoMobile() {
		return configuracaoMobile;
	}

	public void setConfiguracaoMobile(Boolean configuracaoMobile) {
		this.configuracaoMobile = configuracaoMobile;
	}

	public Boolean getConfiguracaoMobileFavorito() {
		return configuracaoMobileFavorito;
	}

	public void setConfiguracaoMobileFavorito(Boolean configuracaoMobileFavorito) {
		this.configuracaoMobileFavorito = configuracaoMobileFavorito;
	}

	public Boolean getConfiguracaoSeiGsuite() {
		return configuracaoSeiGsuite;
	}

	public void setConfiguracaoSeiGsuite(Boolean configuracaoSeiGsuite) {
		this.configuracaoSeiGsuite = configuracaoSeiGsuite;
	}

	public Boolean getConfiguracaoSeiGsuiteFavorito() {
		return configuracaoSeiGsuiteFavorito;
	}

	public void setConfiguracaoSeiGsuiteFavorito(Boolean configuracaoSeiGsuiteFavorito) {
		this.configuracaoSeiGsuiteFavorito = configuracaoSeiGsuiteFavorito;
	}

	public Boolean getConfiguracaoSeiBlackboard() {
		return configuracaoSeiBlackboard;
	}

	public void setConfiguracaoSeiBlackboard(Boolean configuracaoSeiBlackboard) {
		this.configuracaoSeiBlackboard = configuracaoSeiBlackboard;
	}

	public Boolean getConfiguracaoSeiBlackboardFavorito() {
		return configuracaoSeiBlackboardFavorito;
	}

	public void setConfiguracaoSeiBlackboardFavorito(Boolean configuracaoSeiBlackboardFavorito) {
		this.configuracaoSeiBlackboardFavorito = configuracaoSeiBlackboardFavorito;
	}

	public Boolean getGestaoTurmaRel() {
		return gestaoTurmaRel;
	}

	public void setGestaoTurmaRel(Boolean gestaoTurmaRel) {
		this.gestaoTurmaRel = gestaoTurmaRel;
	}

	public Boolean getGestaoTurmaRelFavorito() {
		return gestaoTurmaRelFavorito;
	}

	public void setGestaoTurmaRelFavorito(Boolean gestaoTurmaRelFavorito) {
		this.gestaoTurmaRelFavorito = gestaoTurmaRelFavorito;
	}

	public Boolean getPreInscricaoLog() {
		return preInscricaoLog;
	}

	public void setPreInscricaoLog(Boolean preInscricaoLog) {
		this.preInscricaoLog = preInscricaoLog;
	}

	public Boolean getPreInscricaoLogFavorito() {
		return preInscricaoLogFavorito;
	}

	public void setPreInscricaoLogFavorito(Boolean preInscricaoLogFavorito) {
		this.preInscricaoLogFavorito = preInscricaoLogFavorito;
	}

	public Boolean getImpressora() {
		return impressora;
	}

	public void setImpressora(Boolean impressora) {
		this.impressora = impressora;
	}

	public Boolean getImpressoraFavorito() {
		return impressoraFavorito;
	}

	public void setImpressoraFavorito(Boolean impressoraFavorito) {
		this.impressoraFavorito = impressoraFavorito;
	}

	public Boolean getImpressaoTextoPadraoProcessoSeletivo() {
		return impressaoTextoPadraoProcessoSeletivo;
	}

	public void setImpressaoTextoPadraoProcessoSeletivo(Boolean impressaoTextoPadraoProcessoSeletivo) {
		this.impressaoTextoPadraoProcessoSeletivo = impressaoTextoPadraoProcessoSeletivo;
	}

	public Boolean getTextoPadraoProcessoSeletivo() {
		return textoPadraoProcessoSeletivo;
	}

	public void setTextoPadraoProcessoSeletivo(Boolean textoPadraoProcessoSeletivo) {
		this.textoPadraoProcessoSeletivo = textoPadraoProcessoSeletivo;
	}

	public Boolean getTextoPadraoProcessoSeletivoFavorito() {
		return textoPadraoProcessoSeletivoFavorito;
	}

	public void setTextoPadraoProcessoSeletivoFavorito(Boolean textoPadraoProcessoSeletivoFavorito) {
		this.textoPadraoProcessoSeletivoFavorito = textoPadraoProcessoSeletivoFavorito;
	}

	public Boolean getImpressaoTextoPadraoProcessoSeletivoFavorito() {
		return impressaoTextoPadraoProcessoSeletivoFavorito;
	}

	public void setImpressaoTextoPadraoProcessoSeletivoFavorito(Boolean impressaoTextoPadraoProcessoSeletivoFavorito) {
		this.impressaoTextoPadraoProcessoSeletivoFavorito = impressaoTextoPadraoProcessoSeletivoFavorito;
	}

	public Boolean getPermitirCadastrarOcorrenciaReservaUnidade() {
		return permitirCadastrarOcorrenciaReservaUnidade;
	}

	public void setPermitirCadastrarOcorrenciaReservaUnidade(Boolean permitirCadastrarOcorrenciaReservaUnidade) {
		this.permitirCadastrarOcorrenciaReservaUnidade = permitirCadastrarOcorrenciaReservaUnidade;
	}

	public Boolean getPermitirCadastrarOcorrenciaReservaLocal() {
		return permitirCadastrarOcorrenciaReservaLocal;
	}

	public void setPermitirCadastrarOcorrenciaReservaLocal(Boolean permitirCadastrarOcorrenciaReservaLocal) {
		this.permitirCadastrarOcorrenciaReservaLocal = permitirCadastrarOcorrenciaReservaLocal;
	}

	private Boolean permissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante = false;

	public Boolean getPermissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante() {
		return permissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante;
	}

	public void setPermissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante(Boolean permissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante) {
		this.permissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante = permissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante;
	}

	private Boolean permissaoUsuarioLiberarReservaForaLimiteDataMaxima = false;

	public Boolean getPermissaoUsuarioLiberarReservaForaLimiteDataMaxima() {
		return permissaoUsuarioLiberarReservaForaLimiteDataMaxima;
	}

	public void setPermissaoUsuarioLiberarReservaForaLimiteDataMaxima(Boolean permissaoUsuarioLiberarReservaForaLimiteDataMaxima) {
		this.permissaoUsuarioLiberarReservaForaLimiteDataMaxima = permissaoUsuarioLiberarReservaForaLimiteDataMaxima;
	}

	public Boolean getGestaoReservaPatrimonio() {
		return gestaoReservaPatrimonio;
	}

	public void setGestaoReservaPatrimonio(Boolean gestaoReservaPatrimonio) {
		this.gestaoReservaPatrimonio = gestaoReservaPatrimonio;
	}

	public Boolean getGestaoReservaPatrimonioFavorito() {
		return gestaoReservaPatrimonioFavorito;
	}

	public void setGestaoReservaPatrimonioFavorito(Boolean gestaoReservaPatrimonioFavorito) {
		this.gestaoReservaPatrimonioFavorito = gestaoReservaPatrimonioFavorito;
	}

	private Boolean ocorrenciaPatrimonioVisaoProfessor = false;

	private Boolean ocorrenciaPatrimonioVisaoCoordenador = false;

	public Boolean getOcorrenciaPatrimonioVisaoProfessor() {
		return ocorrenciaPatrimonioVisaoProfessor;
	}

	public void setOcorrenciaPatrimonioVisaoProfessor(Boolean ocorrenciaPatrimonioVisaoProfessor) {
		this.ocorrenciaPatrimonioVisaoProfessor = ocorrenciaPatrimonioVisaoProfessor;
	}

	public Boolean getOcorrenciaPatrimonioVisaoCoordenador() {
		return ocorrenciaPatrimonioVisaoCoordenador;
	}

	public void setOcorrenciaPatrimonioVisaoCoordenador(Boolean ocorrenciaPatrimonioVisaoCoordenador) {
		this.ocorrenciaPatrimonioVisaoCoordenador = ocorrenciaPatrimonioVisaoCoordenador;
	}

	public void setControleRemessaContaPagar(Boolean controleRemessaContaPagar) {
		this.controleRemessaContaPagar = controleRemessaContaPagar;
	}

	public Boolean getControleRemessaContaPagar() {
		return controleRemessaContaPagar;
	}

	public Boolean getControleRemessaContaPagarFavorito() {
		return controleRemessaContaPagarFavorito;
	}

	public void setControleRemessaContaPagarFavorito(Boolean controleRemessaContaPagarFavorito) {
		this.controleRemessaContaPagarFavorito = controleRemessaContaPagarFavorito;
	}

	public Boolean getControleCobrancaPagar() {
		return controleCobrancaPagar;
	}

	public void setControleCobrancaPagar(Boolean controleCobrancaPagar) {
		this.controleCobrancaPagar = controleCobrancaPagar;
	}

	public Boolean getControleCobrancaPagarFavorito() {
		return controleCobrancaPagarFavorito;
	}

	public void setControleCobrancaPagarFavorito(Boolean controleCobrancaPagarFavorito) {
		this.controleCobrancaPagarFavorito = controleCobrancaPagarFavorito;
	}

	public Boolean getPermiteExcluirPlanoEnsino() {
		return permiteExcluirPlanoEnsino;
	}

	public void setPermiteExcluirPlanoEnsino(Boolean permiteExcluirPlanoEnsino) {
		this.permiteExcluirPlanoEnsino = permiteExcluirPlanoEnsino;
	}

	public Boolean getPermiteReplicarNotaOutraDisciplina() {
		return permiteReplicarNotaOutraDisciplina;
	}

	public void setPermiteReplicarNotaOutraDisciplina(Boolean permiteReplicarNotaOutraDisciplina) {
		this.permiteReplicarNotaOutraDisciplina = permiteReplicarNotaOutraDisciplina;
	}

	public Boolean getIndiceReajuste() {
		return indiceReajuste;
	}

	public Boolean getCategoriaGED() {
		return categoriaGED;
	}

	public void setIndiceReajuste(Boolean indiceReajuste) {
		this.indiceReajuste = indiceReajuste;
	}

	public Boolean getIndiceReajusteFavorito() {
		return indiceReajusteFavorito;
	}

	public void setIndiceReajusteFavorito(Boolean indiceReajusteFavorito) {
		this.indiceReajusteFavorito = indiceReajusteFavorito;
	}

	public void setCategoriaGED(Boolean categoriaGED) {
		this.categoriaGED = categoriaGED;
	}

	public Boolean getCategoriaGEDFavorito() {
		return categoriaGEDFavorito;
	}

	public void setCategoriaGEDFavorito(Boolean categoriaGEDFavorito) {
		this.categoriaGEDFavorito = categoriaGEDFavorito;
	}

	public Boolean getRegistroNegativacaoCobrancaContaReceber() {
		return registroNegativacaoCobrancaContaReceber;
	}

	public void setRegistroNegativacaoCobrancaContaReceber(Boolean registroNegativacaoCobrancaContaReceber) {
		this.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber;
	}

	public Boolean getRegistroNegativacaoCobrancaContaReceberFavorito() {
		return registroNegativacaoCobrancaContaReceberFavorito;
	}

	public void setRegistroNegativacaoCobrancaContaReceberFavorito(Boolean registroNegativacaoCobrancaContaReceberFavorito) {
		this.registroNegativacaoCobrancaContaReceberFavorito = registroNegativacaoCobrancaContaReceberFavorito;
	}

	public Boolean getAgenteNegativacaoCobrancaContaReceber() {
		return agenteNegativacaoCobrancaContaReceber;
	}

	public void setAgenteNegativacaoCobrancaContaReceber(Boolean agenteNegativacaoCobrancaContaReceber) {
		this.agenteNegativacaoCobrancaContaReceber = agenteNegativacaoCobrancaContaReceber;
	}

	public Boolean getAgenteNegativacaoCobrancaContaReceberFavorito() {
		return agenteNegativacaoCobrancaContaReceberFavorito;
	}

	public void setAgenteNegativacaoCobrancaContaReceberFavorito(Boolean agenteNegativacaoCobrancaContaReceberFavorito) {
		this.agenteNegativacaoCobrancaContaReceberFavorito = agenteNegativacaoCobrancaContaReceberFavorito;
	}

	public Boolean getMapaNegativacaoCobrancaContaReceber() {
		return mapaNegativacaoCobrancaContaReceber;
	}

	public void setMapaNegativacaoCobrancaContaReceber(Boolean mapaNegativacaoCobrancaContaReceber) {
		this.mapaNegativacaoCobrancaContaReceber = mapaNegativacaoCobrancaContaReceber;
	}

	public Boolean getMapaNegativacaoCobrancaContaReceberFavorito() {
		return mapaNegativacaoCobrancaContaReceberFavorito;
	}

	public void setMapaNegativacaoCobrancaContaReceberFavorito(Boolean mapaNegativacaoCobrancaContaReceberFavorito) {
		this.mapaNegativacaoCobrancaContaReceberFavorito = mapaNegativacaoCobrancaContaReceberFavorito;
	}

	public Boolean getMapaDocumentacaoDigitalizadoGED() {
		return mapaDocumentacaoDigitalizadoGED;
	}

	public void setMapaDocumentacaoDigitalizadoGED(Boolean mapaDocumentacaoDigitalizadoGED) {
		this.mapaDocumentacaoDigitalizadoGED = mapaDocumentacaoDigitalizadoGED;
	}

	public Boolean getMapaDocumentacaoDigitalizadoGEDFavorito() {
		return mapaDocumentacaoDigitalizadoGEDFavorito;
	}

	public void setMapaDocumentacaoDigitalizadoGEDFavorito(Boolean mapaDocumentacaoDigitalizadoGEDFavorito) {
		this.mapaDocumentacaoDigitalizadoGEDFavorito = mapaDocumentacaoDigitalizadoGEDFavorito;
	}

	public Boolean getLinkVisoesMoodle() {
		return linkVisoesMoodle;
	}

	public void setLinkVisoesMoodle(Boolean linkVisoesMoodle) {
		this.linkVisoesMoodle = linkVisoesMoodle;
	}

	public Boolean getNegociacaoContaPagar() {
		if (negociacaoContaPagar == null) {
			negociacaoContaPagar = false;
		}
		return negociacaoContaPagar;
	}

	public void setNegociacaoContaPagar(Boolean negociacaoContaPagar) {
		this.negociacaoContaPagar = negociacaoContaPagar;
	}

	public Boolean getNegociacaoContaPagarFavorito() {
		if (negociacaoContaPagarFavorito == null) {
			negociacaoContaPagarFavorito = false;
		}
		return negociacaoContaPagarFavorito;
	}

	public void setNegociacaoContaPagarFavorito(Boolean negociacaoContaPagarFavorito) {
		this.negociacaoContaPagarFavorito = negociacaoContaPagarFavorito;
	}

	/**
	 * @return the impostosRetidosContaReceberRel
	 */
	public Boolean getImpostosRetidosContaReceberRel() {
		return impostosRetidosContaReceberRel;
	}

	/**
	 * @param impostosRetidosContaReceberRel the impostosRetidosContaReceberRel to set
	 */
	public void setImpostosRetidosContaReceberRel(Boolean impostosRetidosContaReceberRel) {
		this.impostosRetidosContaReceberRel = impostosRetidosContaReceberRel;
	}

	/**
	 * @return the impostosRetidosContaReceberRelFavorito
	 */
	public Boolean getImpostosRetidosContaReceberRelFavorito() {
		return impostosRetidosContaReceberRelFavorito;
	}

	/**
	 * @param impostosRetidosContaReceberRelFavorito the impostosRetidosContaReceberRelFavorito to set
	 */
	public void setImpostosRetidosContaReceberRelFavorito(Boolean impostosRetidosContaReceberRelFavorito) {
		this.impostosRetidosContaReceberRelFavorito = impostosRetidosContaReceberRelFavorito;
	}

	public Boolean getCentralAluno() {
		return centralAluno;
	}

	public void setCentralAluno(Boolean centralAluno) {
		this.centralAluno = centralAluno;
	}

	public Boolean getPermitirVisualizarDadosMatricula() {
		return permitirVisualizarDadosMatricula;
	}

	public void setPermitirVisualizarDadosMatricula(Boolean permitirVisualizarDadosMatricula) {
		this.permitirVisualizarDadosMatricula = permitirVisualizarDadosMatricula;
	}

	public Boolean getPermitirVisualizarDadosFinanceiro() {
		return permitirVisualizarDadosFinanceiro;
	}

	public void setPermitirVisualizarDadosFinanceiro(Boolean permitirVisualizarDadosFinanceiro) {
		this.permitirVisualizarDadosFinanceiro = permitirVisualizarDadosFinanceiro;
	}

	public Boolean getPermitirVisualizarDadosRequerimento() {
		return permitirVisualizarDadosRequerimento;
	}

	public void setPermitirVisualizarDadosRequerimento(Boolean permitirVisualizarDadosRequerimento) {
		this.permitirVisualizarDadosRequerimento = permitirVisualizarDadosRequerimento;
	}

	public Boolean getPermitirVisualizarDadosBiblioteca() {
		return permitirVisualizarDadosBiblioteca;
	}

	public void setPermitirVisualizarDadosBiblioteca(Boolean permitirVisualizarDadosBiblioteca) {
		this.permitirVisualizarDadosBiblioteca = permitirVisualizarDadosBiblioteca;
	}

	public Boolean getPermitirVisualizarDadosCRM() {
		return permitirVisualizarDadosCRM;
	}

	public void setPermitirVisualizarDadosCRM(Boolean permitirVisualizarDadosCRM) {
		this.permitirVisualizarDadosCRM = permitirVisualizarDadosCRM;
	}

	public Boolean getPermitirVisualizarDadosProcessoSeletivo() {
		return permitirVisualizarDadosProcessoSeletivo;
	}

	public void setPermitirVisualizarDadosProcessoSeletivo(Boolean permitirVisualizarDadosProcessoSeletivo) {
		this.permitirVisualizarDadosProcessoSeletivo = permitirVisualizarDadosProcessoSeletivo;
	}

	public Boolean getPermitirVisualizarDadosComunicacaoInterna() {
		return permitirVisualizarDadosComunicacaoInterna;
	}

	public void setPermitirVisualizarDadosComunicacaoInterna(Boolean permitirVisualizarDadosComunicacaoInterna) {
		this.permitirVisualizarDadosComunicacaoInterna = permitirVisualizarDadosComunicacaoInterna;
	}

	/**
	 * @return the impostosRetidosNotaFiscalEntradaRel
	 */
	public Boolean getImpostosRetidosNotaFiscalEntradaRel() {
		return impostosRetidosNotaFiscalEntradaRel;
	}

	/**
	 * @param impostosRetidosNotaFiscalEntradaRel the impostosRetidosNotaFiscalEntradaRel to set
	 */
	public void setImpostosRetidosNotaFiscalEntradaRel(Boolean impostosRetidosNotaFiscalEntradaRel) {
		this.impostosRetidosNotaFiscalEntradaRel = impostosRetidosNotaFiscalEntradaRel;
	}

	/**
	 * @return the impostosRetidosNotaFiscalEntradaRelFavorito
	 */
	public Boolean getImpostosRetidosNotaFiscalEntradaRelFavorito() {
		return impostosRetidosNotaFiscalEntradaRelFavorito;
	}

	/**
	 * @param impostosRetidosNotaFiscalEntradaRelFavorito the impostosRetidosNotaFiscalEntradaRelFavorito to set
	 */
	public void setImpostosRetidosNotaFiscalEntradaRelFavorito(Boolean impostosRetidosNotaFiscalEntradaRelFavorito) {
		this.impostosRetidosNotaFiscalEntradaRelFavorito = impostosRetidosNotaFiscalEntradaRelFavorito;
	}

	public Boolean getValorReferenciaFolhaPagamento() {
		return valorReferenciaFolhaPagamento;
	}

	public void setValorReferenciaFolhaPagamento(Boolean valorReferenciaFolhaPagamento) {
		this.valorReferenciaFolhaPagamento = valorReferenciaFolhaPagamento;
	}

	public Boolean getValorReferenciaFolhaPagamentoFavorito() {
		return valorReferenciaFolhaPagamentoFavorito;
	}

	public void setValorReferenciaFolhaPagamentoFavorito(Boolean valorReferenciaFolhaPagamentoFavorito) {
		this.valorReferenciaFolhaPagamentoFavorito = valorReferenciaFolhaPagamentoFavorito;
	}

	public Boolean getFormulaFolhaPagamento() {
		return formulaFolhaPagamento;
	}

	public void setFormulaFolhaPagamento(Boolean formulaFolhaPagamento) {
		this.formulaFolhaPagamento = formulaFolhaPagamento;
	}

	public Boolean getFormulaFolhaPagamentoFavorito() {
		return formulaFolhaPagamentoFavorito;
	}

	public void setFormulaFolhaPagamentoFavorito(Boolean formulaFolhaPagamentoFavorito) {
		this.formulaFolhaPagamentoFavorito = formulaFolhaPagamentoFavorito;
	}

	public Boolean getEventoFolhaPagamento() {
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(Boolean eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}

	public Boolean getEventoFolhaPagamentoFavorito() {
		return eventoFolhaPagamentoFavorito;
	}

	public void setEventoFolhaPagamentoFavorito(Boolean eventoFolhaPagamentoFavorito) {
		this.eventoFolhaPagamentoFavorito = eventoFolhaPagamentoFavorito;
	}

	public Boolean getGrupoLancamentoFolhaPagamento() {
		return grupoLancamentoFolhaPagamento;
	}

	public void setGrupoLancamentoFolhaPagamento(Boolean grupoLancamentoFolhaPagamento) {
		this.grupoLancamentoFolhaPagamento = grupoLancamentoFolhaPagamento;
	}

	public Boolean getGrupoLancamentoFolhaPagamentoFavorito() {
		return grupoLancamentoFolhaPagamentoFavorito;
	}

	public void setGrupoLancamentoFolhaPagamentoFavorito(Boolean grupoLancamentoFolhaPagamentoFavorito) {
		this.grupoLancamentoFolhaPagamentoFavorito = grupoLancamentoFolhaPagamentoFavorito;
	}

	public Boolean getLancamentoFolhaPagamento() {
		return lancamentoFolhaPagamento;
	}

	public void setLancamentoFolhaPagamento(Boolean lancamentoFolhaPagamento) {
		this.lancamentoFolhaPagamento = lancamentoFolhaPagamento;
	}

	public Boolean getLancamentoFolhaPagamentoFavorito() {
		return lancamentoFolhaPagamentoFavorito;
	}

	public void setLancamentoFolhaPagamentoFavorito(Boolean lancamentoFolhaPagamentoFavorito) {
		this.lancamentoFolhaPagamentoFavorito = lancamentoFolhaPagamentoFavorito;
	}

	public Boolean getEventoFixoCargoFuncionario() {
		return eventoFixoCargoFuncionario;
	}

	public void setEventoFixoCargoFuncionario(Boolean eventoFixoCargoFuncionario) {
		this.eventoFixoCargoFuncionario = eventoFixoCargoFuncionario;
	}

	public Boolean getEventoFixoCargoFuncionarioFavorito() {
		return eventoFixoCargoFuncionarioFavorito;
	}

	public void setEventoFixoCargoFuncionarioFavorito(Boolean eventoFixoCargoFuncionarioFavorito) {
		this.eventoFixoCargoFuncionarioFavorito = eventoFixoCargoFuncionarioFavorito;
	}

	public Boolean getSecaoFolhaPagamento() {
		return secaoFolhaPagamento;
	}

	public void setSecaoFolhaPagamento(Boolean secaoFolhaPagamento) {
		this.secaoFolhaPagamento = secaoFolhaPagamento;
	}

	public Boolean getSecaoFolhaPagamentoFavorito() {
		return secaoFolhaPagamentoFavorito;
	}

	public void setSecaoFolhaPagamentoFavorito(Boolean secaoFolhaPagamentoFavorito) {
		this.secaoFolhaPagamentoFavorito = secaoFolhaPagamentoFavorito;
	}

	public Boolean getNivelSalarial() {
		return nivelSalarial;
	}

	public void setNivelSalarial(Boolean nivelSalarial) {
		this.nivelSalarial = nivelSalarial;
	}

	public Boolean getNivelSalarialFavorito() {
		return nivelSalarialFavorito;
	}

	public void setNivelSalarialFavorito(Boolean nivelSalarialFavorito) {
		this.nivelSalarialFavorito = nivelSalarialFavorito;
	}

	public Boolean getFaixaSalarial() {
		return faixaSalarial;
	}

	public void setFaixaSalarial(Boolean faixaSalarial) {
		this.faixaSalarial = faixaSalarial;
	}

	public Boolean getFaixaSalarialFavorito() {
		return faixaSalarialFavorito;
	}

	public void setFaixaSalarialFavorito(Boolean faixaSalarialFavorito) {
		this.faixaSalarialFavorito = faixaSalarialFavorito;
	}

	public Boolean getProgressaoSalarial() {
		return progressaoSalarial;
	}

	public void setProgressaoSalarial(Boolean progressaoSalarial) {
		this.progressaoSalarial = progressaoSalarial;
	}

	public Boolean getProgressaoSalarialFavorito() {
		return progressaoSalarialFavorito;
	}

	public void setProgressaoSalarialFavorito(Boolean progressaoSalarialFavorito) {
		this.progressaoSalarialFavorito = progressaoSalarialFavorito;
	}

	public Boolean getCompetenciaFolhaPagamento() {
		return competenciaFolhaPagamento;
	}

	public void setCompetenciaFolhaPagamento(Boolean competenciaFolhaPagamento) {
		this.competenciaFolhaPagamento = competenciaFolhaPagamento;
	}

	public Boolean getCompetenciaFolhaPagamentoFavorito() {
		return competenciaFolhaPagamentoFavorito;
	}

	public void setCompetenciaFolhaPagamentoFavorito(Boolean competenciaFolhaPagamentoFavorito) {
		this.competenciaFolhaPagamentoFavorito = competenciaFolhaPagamentoFavorito;
	}

	public Boolean getFichaFinanceira() {
		return fichaFinanceira;
	}

	public void setFichaFinanceira(Boolean fichaFinanceira) {
		this.fichaFinanceira = fichaFinanceira;
	}

	public Boolean getFichaFinanceiraFavorito() {
		return fichaFinanceiraFavorito;
	}

	public void setFichaFinanceiraFavorito(Boolean fichaFinanceiraFavorito) {
		this.fichaFinanceiraFavorito = fichaFinanceiraFavorito;
	}

	public Boolean getSubMenuRhBasico() {
		return getValorReferenciaFolhaPagamento() || getFormulaFolhaPagamento();
	}

	public Boolean getSubMenuRhFolhaPagamento() {
		return getGrupoLancamentoFolhaPagamento() || getLancamentoFolhaPagamento();
	}

	public Boolean getSubMenuRhRecursosHumanos() {
		return getNivelSalarial() || getFaixaSalarial() || getProgressaoSalarial();
	}

	/**
	 * @return the compraRel
	 */
	public Boolean getCompraRel() {
		return compraRel;
	}

	/**
	 * @param compraRel the compraRel to set
	 */
	public void setCompraRel(Boolean compraRel) {
		this.compraRel = compraRel;
	}

	/**
	 * @return the compraRelFavorito
	 */
	public Boolean getCompraRelFavorito() {
		return compraRelFavorito;
	}

	/**
	 * @param compraRelFavorito the compraRelFavorito to set
	 */
	public void setCompraRelFavorito(Boolean compraRelFavorito) {
		this.compraRelFavorito = compraRelFavorito;
	}

	/**
	 * @return the produtoServicoRel
	 */
	public Boolean getProdutoServicoRel() {
		return produtoServicoRel;
	}

	/**
	 * @param produtoServicoRel the produtoServicoRel to set
	 */
	public void setProdutoServicoRel(Boolean produtoServicoRel) {
		this.produtoServicoRel = produtoServicoRel;
	}

	/**
	 * @return the produtoServicoRelFavorito
	 */
	public Boolean getProdutoServicoRelFavorito() {
		return produtoServicoRelFavorito;
	}

	/**
	 * @param produtoServicoRelFavorito the produtoServicoRelFavorito to set
	 */
	public void setProdutoServicoRelFavorito(Boolean produtoServicoRelFavorito) {
		this.produtoServicoRelFavorito = produtoServicoRelFavorito;
	}

	/**
	 * @return the adiantamentoRel
	 */
	public Boolean getAdiantamentoRel() {
		return adiantamentoRel;
	}

	/**
	 * @param adiantamentoRel the adiantamentoRel to set
	 */
	public void setAdiantamentoRel(Boolean adiantamentoRel) {
		this.adiantamentoRel = adiantamentoRel;
	}

	/**
	 * @return the adiantamentoRelFavorito
	 */
	public Boolean getAdiantamentoRelFavorito() {
		return adiantamentoRelFavorito;
	}

	/**
	 * @param adiantamentoRelFavorito the adiantamentoRelFavorito to set
	 */
	public void setAdiantamentoRelFavorito(Boolean adiantamentoRelFavorito) {
		this.adiantamentoRelFavorito = adiantamentoRelFavorito;
	}

	public Boolean getTipoEmprestimo() {
		return tipoEmprestimo;
	}

	public void setTipoEmprestimo(Boolean tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}

	public Boolean getTipoEmprestimoFavorito() {
		return tipoEmprestimoFavorito;
	}

	public void setTipoEmprestimoFavorito(Boolean tipoEmprestimoFavorito) {
		this.tipoEmprestimoFavorito = tipoEmprestimoFavorito;
	}

	public Boolean getParametroValeTransporte() {
		return parametroValeTransporte;
	}

	public void setParametroValeTransporte(Boolean parametroValeTransporte) {
		this.parametroValeTransporte = parametroValeTransporte;
	}

	public Boolean getParametroValeTransporteFavorito() {
		return parametroValeTransporteFavorito;
	}

	public void setParametroValeTransporteFavorito(Boolean parametroValeTransporteFavorito) {
		this.parametroValeTransporteFavorito = parametroValeTransporteFavorito;
	}

	public Boolean getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(Boolean tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public Boolean getTipoTransporteFavorito() {
		return tipoTransporteFavorito;
	}

	public void setTipoTransporteFavorito(Boolean tipoTransporteFavorito) {
		this.tipoTransporteFavorito = tipoTransporteFavorito;
	}

	public Boolean getFichaFinanceiraRel() {
		return fichaFinanceiraRel;
	}

	public void setFichaFinanceiraRel(Boolean fichaFinanceiraRel) {
		this.fichaFinanceiraRel = fichaFinanceiraRel;
	}

	public Boolean getFichaFinanceiraRelFavorito() {
		return fichaFinanceiraRelFavorito;
	}

	public void setFichaFinanceiraRelFavorito(Boolean fichaFinanceiraRelFavorito) {
		this.fichaFinanceiraRelFavorito = fichaFinanceiraRelFavorito;
	}

	public Boolean getSalarioComposto() {
		return salarioComposto;
	}

	public void setSalarioComposto(Boolean salarioComposto) {
		this.salarioComposto = salarioComposto;
	}

	public Boolean getSalarioCompostoFavorito() {
		return salarioCompostoFavorito;
	}

	public void setSalarioCompostoFavorito(Boolean salarioCompostoFavorito) {
		this.salarioCompostoFavorito = salarioCompostoFavorito;
	}

	public Boolean getEventoEmprestimoCargoFuncionario() {
		if (eventoEmprestimoCargoFuncionario == null)
			eventoEmprestimoCargoFuncionario = false;
		return eventoEmprestimoCargoFuncionario;
	}

	public void setEventoEmprestimoCargoFuncionario(Boolean eventoEmprestimoCargoFuncionario) {
		this.eventoEmprestimoCargoFuncionario = eventoEmprestimoCargoFuncionario;
	}

	public Boolean getEventoEmprestimoCargoFuncionarioFavorito() {
		if (eventoEmprestimoCargoFuncionarioFavorito == null)
			eventoEmprestimoCargoFuncionarioFavorito = false;
		return eventoEmprestimoCargoFuncionarioFavorito;
	}

	public void setEventoEmprestimoCargoFuncionarioFavorito(Boolean eventoEmprestimoCargoFuncionarioFavorito) {
		this.eventoEmprestimoCargoFuncionarioFavorito = eventoEmprestimoCargoFuncionarioFavorito;
	}

	public Boolean getEventoValeTransporteFuncionarioCargo() {
		return eventoValeTransporteFuncionarioCargo;
	}

	public void setEventoValeTransporteFuncionarioCargo(Boolean eventoValeTransporteFuncionarioCargo) {
		this.eventoValeTransporteFuncionarioCargo = eventoValeTransporteFuncionarioCargo;
	}

	public Boolean getEventoValeTransporteFuncionarioCargoFavorito() {
		return eventoValeTransporteFuncionarioCargoFavorito;
	}

	public void setEventoValeTransporteFuncionarioCargoFavorito(Boolean eventoValeTransporteFuncionarioCargoFavorito) {
		this.eventoValeTransporteFuncionarioCargoFavorito = eventoValeTransporteFuncionarioCargoFavorito;
	}

	public Boolean getFaltasFuncionario() {
		return faltasFuncionario;
	}

	public void setFaltasFuncionario(Boolean faltasFuncionario) {
		this.faltasFuncionario = faltasFuncionario;
	}

	public Boolean getFaltasFuncionarioFavorito() {
		return faltasFuncionarioFavorito;
	}

	public void setFaltasFuncionarioFavorito(Boolean faltasFuncionarioFavorito) {
		this.faltasFuncionarioFavorito = faltasFuncionarioFavorito;

	}

	/*
	 * @return the artefatoEntregaAluno
	 */
	public Boolean getArtefatoEntregaAluno() {
		return artefatoEntregaAluno;
	}

	/**
	 * @param artefatoEntregaAluno the artefatoEntregaAluno to set
	 */
	public void setArtefatoEntregaAluno(Boolean artefatoEntregaAluno) {
		this.artefatoEntregaAluno = artefatoEntregaAluno;
	}

	/**
	 * @return the artefatoEntregaAlunoFavorito
	 */
	public Boolean getArtefatoEntregaAlunoFavorito() {
		return artefatoEntregaAlunoFavorito;
	}

	/**
	 * @param artefatoEntregaAlunoFavorito the artefatoEntregaAlunoFavorito to set
	 */
	public void setArtefatoEntregaAlunoFavorito(Boolean artefatoEntregaAlunoFavorito) {
		this.artefatoEntregaAlunoFavorito = artefatoEntregaAlunoFavorito;
	}

	/**
	 * @return the registroEntregaArtefatoAluno
	 */
	public Boolean getRegistroEntregaArtefatoAluno() {
		return registroEntregaArtefatoAluno;
	}

	/**
	 * @param registroEntregaArtefatoAluno the registroEntregaArtefatoAluno to set
	 */
	public void setRegistroEntregaArtefatoAluno(Boolean registroEntregaArtefatoAluno) {
		this.registroEntregaArtefatoAluno = registroEntregaArtefatoAluno;
	}

	/**
	 * @return the regristroEntregaArtefatoAlunoFavorito
	 */
	public Boolean getRegistroEntregaArtefatoAlunoFavorito() {
		return registroEntregaArtefatoAlunoFavorito;
	}

	/**
	 * @param regristroEntregaArtefatoAlunoFavorito the regristroEntregaArtefatoAlunoFavorito to set
	 */
	public void setRegistroEntregaArtefatoAlunoFavorito(Boolean registroEntregaArtefatoAlunoFavorito) {
		this.registroEntregaArtefatoAlunoFavorito = registroEntregaArtefatoAlunoFavorito;
	}

	/**
	 * @return the alunoArtefatosEntregue
	 */
	public Boolean getAlunoArtefatosEntregue() {
		return alunoArtefatosEntregue;
	}

	/**
	 * @param alunoArtefatosEntregue the alunoArtefatosEntregue to set
	 */
	public void setAlunoArtefatosEntregue(Boolean alunoArtefatosEntregue) {
		this.alunoArtefatosEntregue = alunoArtefatosEntregue;
	}

	public Boolean getCondicaoDescontoRenegociacao() {
		return condicaoDescontoRenegociacao;
	}

	public void setCondicaoDescontoRenegociacao(Boolean condicaoDescontoRenegociacao) {
		this.condicaoDescontoRenegociacao = condicaoDescontoRenegociacao;
	}

	public Boolean getCondicaoDescontoRenegociacaoFavorito() {
		return condicaoDescontoRenegociacaoFavorito;
	}

	public void setCondicaoDescontoRenegociacaoFavorito(Boolean condicaoDescontoRenegociacaoFavorito) {
		this.condicaoDescontoRenegociacaoFavorito = condicaoDescontoRenegociacaoFavorito;
	}

	/**
	 * @return the permitirCriarScriptArtefatoEntregaAluno
	 */
	public Boolean getPermitirCriarScriptArtefatoEntregaAluno() {
		return permitirCriarScriptArtefatoEntregaAluno;
	}

	/**
	 * @param permitirCriarScriptArtefatoEntregaAluno the permitirCriarScriptArtefatoEntregaAluno to set
	 */
	public void setPermitirCriarScriptArtefatoEntregaAluno(Boolean permitirCriarScriptArtefatoEntregaAluno) {
		this.permitirCriarScriptArtefatoEntregaAluno = permitirCriarScriptArtefatoEntregaAluno;
	}

	public Boolean getValoresCursoGinfes() {
		return valoresCursoGinfes;
	}

	public void setValoresCursoGinfes(Boolean valoresCursoGinfes) {
		this.valoresCursoGinfes = valoresCursoGinfes;
	}

	public Boolean getValoresCursoGinfesFavorito() {
		return valoresCursoGinfesFavorito;
	}

	public void setValoresCursoGinfesFavorito(Boolean valoresCursoGinfesFavorito) {
		this.valoresCursoGinfesFavorito = valoresCursoGinfesFavorito;
	}

	public Boolean getIntegracaoGinfesCurso() {
		return integracaoGinfesCurso;
	}

	public void setIntegracaoGinfesCurso(Boolean integracaoGinfesCurso) {
		this.integracaoGinfesCurso = integracaoGinfesCurso;
	}

	public Boolean getIntegracaoGinfesCursoFavorito() {
		return integracaoGinfesCursoFavorito;
	}

	public void setIntegracaoGinfesCursoFavorito(Boolean integracaoGinfesCursoFavorito) {
		this.integracaoGinfesCursoFavorito = integracaoGinfesCursoFavorito;
	}

	public Boolean getIntegracaoGinfesAluno() {
		return integracaoGinfesAluno;
	}

	public void setIntegracaoGinfesAluno(Boolean integracaoGinfesAluno) {
		this.integracaoGinfesAluno = integracaoGinfesAluno;
	}

	public Boolean getIntegracaoGinfesAlunoFavorito() {
		return integracaoGinfesAlunoFavorito;
	}

	public void setIntegracaoGinfesAlunoFavorito(Boolean integracaoGinfesAlunoFavorito) {
		this.integracaoGinfesAlunoFavorito = integracaoGinfesAlunoFavorito;
	}

	public Boolean getPeriodoAquisitivoFerias() {
		if (periodoAquisitivoFerias == null)
			periodoAquisitivoFerias = false;
		return periodoAquisitivoFerias;
	}

	public void setPeriodoAquisitivoFerias(Boolean periodoAquisitivoFerias) {
		this.periodoAquisitivoFerias = periodoAquisitivoFerias;
	}

	public Boolean getPeriodoAquisitivoFeriasFavorito() {
		if (periodoAquisitivoFeriasFavorito == null)
			periodoAquisitivoFeriasFavorito = false;
		return periodoAquisitivoFeriasFavorito;
	}

	public void setPeriodoAquisitivoFeriasFavorito(Boolean periodoAquisitivoFeriasFavorito) {
		this.periodoAquisitivoFeriasFavorito = periodoAquisitivoFeriasFavorito;
	}

	public Boolean getSindicato() {
		if (sindicato == null)
			sindicato = false;
		return sindicato;
	}

	public void setSindicato(Boolean sindicato) {
		this.sindicato = sindicato;
	}

	public Boolean getSindicatoFavorito() {
		if (sindicatoFavorito == null)
			sindicatoFavorito = false;
		return sindicatoFavorito;
	}

	public void setSindicatoFavorito(Boolean sindicatoFavorito) {
		this.sindicatoFavorito = sindicatoFavorito;
	}

	public Boolean getMarcacaoFerias() {
		if (marcacaoFerias == null)
			marcacaoFerias = false;
		return marcacaoFerias;
	}

	public void setMarcacaoFerias(Boolean marcacaoFerias) {
		this.marcacaoFerias = marcacaoFerias;
	}

	public Boolean getMarcacaoFeriasFavorito() {
		if (marcacaoFeriasFavorito == null)
			marcacaoFeriasFavorito = false;
		return marcacaoFeriasFavorito;
	}

	public void setMarcacaoFeriasFavorito(Boolean marcacaoFeriasFavorito) {
		this.marcacaoFeriasFavorito = marcacaoFeriasFavorito;
	}

	public Boolean getMarcacaoFeriasColetivas() {
		return marcacaoFeriasColetivas;
	}

	public void setMarcacaoFeriasColetivas(Boolean marcacaoFeriasColetivas) {
		this.marcacaoFeriasColetivas = marcacaoFeriasColetivas;
	}

	public Boolean getMarcacaoFeriasColetivasFavorito() {
		return marcacaoFeriasColetivasFavorito;
	}

	public void setMarcacaoFeriasColetivasFavorito(Boolean marcacaoFeriasColetivasFavorito) {
		this.marcacaoFeriasColetivasFavorito = marcacaoFeriasColetivasFavorito;
	}

	public Boolean getHistoricoFuncionario() {
		return historicoFuncionario;
	}

	public void setHistoricoFuncionario(Boolean historicoFuncionario) {
		this.historicoFuncionario = historicoFuncionario;
	}

	public Boolean getHistoricoFuncionarioFavorito() {
		return historicoFuncionarioFavorito;
	}

	public void setHistoricoFuncionarioFavorito(Boolean historicoFuncionarioFavorito) {
		this.historicoFuncionarioFavorito = historicoFuncionarioFavorito;
	}

	public Boolean getAfastamentoFuncionario() {
		return afastamentoFuncionario;
	}

	public void setAfastamentoFuncionario(Boolean afastamentoFuncionario) {
		this.afastamentoFuncionario = afastamentoFuncionario;
	}

	public Boolean getAfastamentoFuncionarioFavorito() {
		return afastamentoFuncionarioFavorito;
	}

	public void setAfastamentoFuncionarioFavorito(Boolean afastamentoFuncionarioFavorito) {
		this.afastamentoFuncionarioFavorito = afastamentoFuncionarioFavorito;
	}

	public Boolean getLiberarValidacaoDadosEnadeCenso() {
		return liberarValidacaoDadosEnadeCenso;
	}

	public void setLiberarValidacaoDadosEnadeCenso(Boolean liberarValidacaoDadosEnadeCenso) {
		this.liberarValidacaoDadosEnadeCenso = liberarValidacaoDadosEnadeCenso;
	}

	public Boolean getRescisao() {
		return rescisao;
	}

	public void setRescisao(Boolean rescisao) {
		this.rescisao = rescisao;
	}

	public Boolean getRescisaoFavorito() {
		return rescisaoFavorito;
	}

	public void setRescisaoFavorito(Boolean rescisaoFavorito) {
		this.rescisaoFavorito = rescisaoFavorito;
	}

	public Boolean getFuncionarioRel() {
		return funcionarioRel;
	}

	public void setFuncionarioRel(Boolean funcionarioRel) {
		this.funcionarioRel = funcionarioRel;
	}

	public Boolean getFuncionarioRelFavorito() {
		return funcionarioRelFavorito;
	}

	public void setFuncionarioRelFavorito(Boolean funcionarioRelFavorito) {
		this.funcionarioRelFavorito = funcionarioRelFavorito;
	}

	/**
	 * @return the permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica
	 */
	public Boolean getPermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica() {
		if (permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica == null) {
			permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica = false;
		}
		return permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica;
	}

	/**
	 * @param permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica the permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica to set
	 */
	public void setPermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica(Boolean permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
		this.permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica = permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica;
	}

	public Boolean getLinkVisoesMoodleVisaoCoordenador() {
		return linkVisoesMoodleVisaoCoordenador;
	}

	public void setLinkVisoesMoodleVisaoCoordenador(Boolean linkVisoesMoodleVisaoCoordenador) {
		this.linkVisoesMoodleVisaoCoordenador = linkVisoesMoodleVisaoCoordenador;
	}

	public Boolean getHistoricoSituacaofuncionarioRel() {
		return historicoSituacaofuncionarioRel;
	}

	public void setHistoricoSituacaofuncionarioRel(Boolean historicoSituacaofuncionarioRel) {
		this.historicoSituacaofuncionarioRel = historicoSituacaofuncionarioRel;
	}

	public Boolean getHistoricoSituacaofuncionarioRelFavorito() {
		return historicoSituacaofuncionarioRelFavorito;
	}

	public void setHistoricoSituacaofuncionarioRelFavorito(Boolean historicoSituacaofuncionarioRelFavorito) {
		this.historicoSituacaofuncionarioRelFavorito = historicoSituacaofuncionarioRelFavorito;
	}

	public Boolean getAtualizarValeTrasnporte() {
		return atualizarValeTrasnporte;
	}

	public void setAtualizarValeTrasnporte(Boolean atualizarValeTrasnporte) {
		this.atualizarValeTrasnporte = atualizarValeTrasnporte;
	}

	public Boolean getAtualizarValeTrasnporteFavorito() {
		return atualizarValeTrasnporteFavorito;
	}

	public void setAtualizarValeTrasnporteFavorito(Boolean atualizarValeTrasnporteFavorito) {
		this.atualizarValeTrasnporteFavorito = atualizarValeTrasnporteFavorito;
	}

	public Boolean getDownloadRel() {
		return downloadRel;
	}

	public void setDownloadRel(Boolean downloadRel) {
		this.downloadRel = downloadRel;
	}

	private Boolean mapaDocumentoAssinadoPessoa = false;

	public Boolean getMapaDocumentoAssinadoPessoa() {
		return mapaDocumentoAssinadoPessoa;
	}

	public void setMapaDocumentoAssinadoPessoa(Boolean mapaDocumentoAssinadoPessoa) {
		this.mapaDocumentoAssinadoPessoa = mapaDocumentoAssinadoPessoa;
	}

	private Boolean mapaDocumentoAssinadoPessoaFavorito = false;

	public Boolean getMapaDocumentoAssinadoPessoaFavorito() {
		return mapaDocumentoAssinadoPessoaFavorito;
	}

	public void setMapaDocumentoAssinadoPessoaFavorito(Boolean mapaDocumentoAssinadoPessoaFavorito) {
		this.mapaDocumentoAssinadoPessoaFavorito = mapaDocumentoAssinadoPessoaFavorito;
	}

	public Boolean getEntregaDocumentoPermiteAnexarArquivo() {
		if (entregaDocumentoPermiteAnexarArquivo == null) {
			entregaDocumentoPermiteAnexarArquivo = false;
		}
		return entregaDocumentoPermiteAnexarArquivo;
	}

	public void setEntregaDocumentoPermiteAnexarArquivo(Boolean entregaDocumentoPermiteAnexarArquivo) {
		this.entregaDocumentoPermiteAnexarArquivo = entregaDocumentoPermiteAnexarArquivo;
	}

	public Boolean getEntregaDocumentoPermiteAnexarArquivoFavorito() {
		if (entregaDocumentoPermiteAnexarArquivoFavorito == null) {
			entregaDocumentoPermiteAnexarArquivoFavorito = false;
		}
		return entregaDocumentoPermiteAnexarArquivoFavorito;
	}

	public void setEntregaDocumentoPermiteAnexarArquivoFavorito(Boolean entregaDocumentoPermiteAnexarArquivoFavorito) {
		this.entregaDocumentoPermiteAnexarArquivoFavorito = entregaDocumentoPermiteAnexarArquivoFavorito;
	}

	public Boolean getRelatorioSEIDecidirRecursosHumanos() {
		return relatorioSEIDecidirRecursosHumanos;
	}

	public void setRelatorioSEIDecidirRecursosHumanos(Boolean relatorioSEIDecidirRecursosHumanos) {
		this.relatorioSEIDecidirRecursosHumanos = relatorioSEIDecidirRecursosHumanos;
	}

	public Boolean getRelatorioSEIDecidirRecursosHumanosFavorito() {
		return relatorioSEIDecidirRecursosHumanosFavorito;
	}

	public void setRelatorioSEIDecidirRecursosHumanosFavorito(Boolean relatorioSEIDecidirRecursosHumanosFavorito) {
		this.relatorioSEIDecidirRecursosHumanosFavorito = relatorioSEIDecidirRecursosHumanosFavorito;
	}

	public Boolean getUnificacaoCadastroPessoa() {
		return unificacaoCadastroPessoa;
	}

	public void setUnificacaoCadastroPessoa(Boolean unificacaoCadastroPessoa) {
		this.unificacaoCadastroPessoa = unificacaoCadastroPessoa;
	}

	public Boolean getUnificacaoCadastroPessoaFavorito() {
		return unificacaoCadastroPessoaFavorito;
	}

	public void setUnificacaoCadastroPessoaFavorito(Boolean unificacaoCadastroPessoaFavorito) {
		this.unificacaoCadastroPessoaFavorito = unificacaoCadastroPessoaFavorito;
	}

	public Boolean getPerguntaPlanoEnsino() {
		return perguntaPlanoEnsino;
	}

	public void setPerguntaPlanoEnsino(Boolean perguntaPlanoEnsino) {
		this.perguntaPlanoEnsino = perguntaPlanoEnsino;
	}

	public Boolean getPerguntaPlanoEnsinoFavorito() {
		return perguntaPlanoEnsinoFavorito;
	}

	public void setPerguntaPlanoEnsinoFavorito(Boolean perguntaPlanoEnsinoFavorito) {
		this.perguntaPlanoEnsinoFavorito = perguntaPlanoEnsinoFavorito;
	}

	public Boolean getFormularioPlanoEnsino() {
		return formularioPlanoEnsino;
	}

	public void setFormularioPlanoEnsino(Boolean formularioPlanoEnsino) {
		this.formularioPlanoEnsino = formularioPlanoEnsino;
	}

	public Boolean getFormularioPlanoEnsinoFavorito() {
		return formularioPlanoEnsinoFavorito;
	}

	public Boolean getGestaoContasPagar() {
		return gestaoContasPagar;
	}

	public void setGestaoContasPagar(Boolean gestaoContasPagar) {
		this.gestaoContasPagar = gestaoContasPagar;
	}

	public Boolean getGestaoContasPagarFavorito() {
		return gestaoContasPagarFavorito;
	}

	public void setGestaoContasPagarFavorito(Boolean gestaoContasPagarFavorito) {
		this.gestaoContasPagarFavorito = gestaoContasPagarFavorito;
	}

	public void setFormularioPlanoEnsinoFavorito(Boolean formularioPlanoEnsinoFavorito) {
		this.formularioPlanoEnsinoFavorito = formularioPlanoEnsinoFavorito;
	}

	public Boolean getSimularAcessoUsuario() {
		return simularAcessoUsuario;
	}

	public void setSimularAcessoUsuario(Boolean simularAcessoUsuario) {
		this.simularAcessoUsuario = simularAcessoUsuario;
	}

	public Boolean getRelatorioSEIDecidirPlanoOrcamentario() {
		return relatorioSEIDecidirPlanoOrcamentario;
	}

	public void setRelatorioSEIDecidirPlanoOrcamentario(Boolean relatorioSEIDecidirPlanoOrcamentario) {
		this.relatorioSEIDecidirPlanoOrcamentario = relatorioSEIDecidirPlanoOrcamentario;
	}

	public Boolean getRelatorioSEIDecidirPlanoOrcamentarioFavorito() {
		return relatorioSEIDecidirPlanoOrcamentarioFavorito;
	}

	public void setRelatorioSEIDecidirPlanoOrcamentarioFavorito(Boolean relatorioSEIDecidirPlanoOrcamentarioFavorito) {
		this.relatorioSEIDecidirPlanoOrcamentarioFavorito = relatorioSEIDecidirPlanoOrcamentarioFavorito;
	}

	public Boolean getQuestionarioRequisicao() {
		return questionarioRequisicao;
	}

	public void setQuestionarioRequisicao(Boolean questionarioRequisicao) {
		this.questionarioRequisicao = questionarioRequisicao;
	}

	public Boolean getQuestionarioRequisicaoFavorito() {
		return questionarioRequisicaoFavorito;
	}

	public void setQuestionarioRequisicaoFavorito(Boolean questionarioRequisicaoFavorito) {
		this.questionarioRequisicaoFavorito = questionarioRequisicaoFavorito;
	}

	public Boolean getPerguntaRequisicao() {
		return perguntaRequisicao;
	}

	public void setPerguntaRequisicao(Boolean perguntaRequisicao) {
		this.perguntaRequisicao = perguntaRequisicao;
	}

	public Boolean getPerguntaRequisicaoFavorito() {
		return perguntaRequisicaoFavorito;
	}

	public void setPerguntaRequisicaoFavorito(Boolean perguntaRequisicaoFavorito) {
		this.perguntaRequisicaoFavorito = perguntaRequisicaoFavorito;
	}

	public Boolean getConfiguracaoGED() {
		return configuracaoGED;
	}

	public void setConfiguracaoGED(Boolean configuracaoGED) {
		this.configuracaoGED = configuracaoGED;
	}

	public Boolean getConfiguracaoGEDFavorito() {
		return configuracaoGEDFavorito;
	}

	public void setConfiguracaoGEDFavorito(Boolean configuracaoGEDFavorito) {
		this.configuracaoGEDFavorito = configuracaoGEDFavorito;
	}

	public Boolean getBibliotecaLexMagister() {
		return bibliotecaLexMagister;
	}

	public void setBibliotecaLexMagister(Boolean bibliotecaLexMagister) {
		this.bibliotecaLexMagister = bibliotecaLexMagister;
	}

	public Boolean getPermitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio() {
		return permitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio;
	}

	public void setPermitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio(Boolean permitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio) {
		this.permitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio = permitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio;
	}

	public Boolean getMapaAtividadeExtraClasseProfessorVisaoCoordenador() {
		return mapaAtividadeExtraClasseProfessorVisaoCoordenador;
	}

	public void setMapaAtividadeExtraClasseProfessorVisaoCoordenador(Boolean mapaAtividadeExtraClasseProfessorVisaoCoordenador) {
		this.mapaAtividadeExtraClasseProfessorVisaoCoordenador = mapaAtividadeExtraClasseProfessorVisaoCoordenador;
	}

	public Boolean getMapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador() {
		return mapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador;
	}

	public void setMapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador(Boolean mapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador) {
		this.mapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador = mapaAtividadeExtraClasseProfessorFavoritoVisaoCoordenador;
	}

	public Boolean getDefinirAtividadeExtraClasseProfessorVisaoCoordenador() {
		return definirAtividadeExtraClasseProfessorVisaoCoordenador;
	}

	public void setDefinirAtividadeExtraClasseProfessorVisaoCoordenador(Boolean definirAtividadeExtraClasseProfessorVisaoCoordenador) {
		this.definirAtividadeExtraClasseProfessorVisaoCoordenador = definirAtividadeExtraClasseProfessorVisaoCoordenador;
	}

	public Boolean getDefinirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito() {
		return definirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito;
	}

	public void setDefinirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito(Boolean definirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito) {
		this.definirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito = definirAtividadeExtraClasseProfessorVisaoCoordenadorFavorito;
	}

	public Boolean getAtividadeExtraClasseProfessor() {
		return atividadeExtraClasseProfessor;
	}

	public void setAtividadeExtraClasseProfessor(Boolean atividadeExtraClasseProfessor) {
		this.atividadeExtraClasseProfessor = atividadeExtraClasseProfessor;
	}

	public Boolean getAtividadeExtraClasseProfessorFavorito() {
		return atividadeExtraClasseProfessorFavorito;
	}

	public void setAtividadeExtraClasseProfessorFavorito(Boolean atividadeExtraClasseProfessorFavorito) {
		this.atividadeExtraClasseProfessorFavorito = atividadeExtraClasseProfessorFavorito;
	}

	public Boolean getAtividadeExtraClasseProfessorPostado() {
		return atividadeExtraClasseProfessorPostado;
	}

	public void setAtividadeExtraClasseProfessorPostado(Boolean atividadeExtraClasseProfessorPostado) {
		this.atividadeExtraClasseProfessorPostado = atividadeExtraClasseProfessorPostado;
	}

	public Boolean getAtividadeExtraClasseProfessorPostadoFavorito() {
		return atividadeExtraClasseProfessorPostadoFavorito;
	}

	public void setAtividadeExtraClasseProfessorPostadoFavorito(Boolean atividadeExtraClasseProfessorPostadoFavorito) {
		this.atividadeExtraClasseProfessorPostadoFavorito = atividadeExtraClasseProfessorPostadoFavorito;
	}

	public Boolean getMapaAtividadeExtraClasseProfessor() {
		return mapaAtividadeExtraClasseProfessor;
	}

	public Boolean getMapaAtividadeExtraClasseProfessorFavorito() {
		return mapaAtividadeExtraClasseProfessorFavorito;
	}

	public void setMapaAtividadeExtraClasseProfessor(Boolean mapaAtividadeExtraClasseProfessor) {
		this.mapaAtividadeExtraClasseProfessor = mapaAtividadeExtraClasseProfessor;
	}

	public void setMapaAtividadeExtraClasseProfessorFavorito(Boolean mapaAtividadeExtraClasseProfessorFavorito) {
		this.mapaAtividadeExtraClasseProfessorFavorito = mapaAtividadeExtraClasseProfessorFavorito;
	}

	public Boolean getAnularQuestaoOnline() {
		return anularQuestaoOnline;
	}

	public void setAnularQuestaoOnline(Boolean anularQuestaoOnline) {
		this.anularQuestaoOnline = anularQuestaoOnline;
	}

	public Boolean getCalendarioLancamentoPlanoEnsino() {
		return calendarioLancamentoPlanoEnsino;
	}

	public Boolean getCalendarioLancamentoPlanoEnsinoFavorito() {
		return calendarioLancamentoPlanoEnsinoFavorito;
	}

	public void setCalendarioLancamentoPlanoEnsino(Boolean calendarioLancamentoPlanoEnsino) {
		this.calendarioLancamentoPlanoEnsino = calendarioLancamentoPlanoEnsino;
	}

	public void setCalendarioLancamentoPlanoEnsinoFavorito(Boolean calendarioLancamentoPlanoEnsinoFavorito) {
		this.calendarioLancamentoPlanoEnsinoFavorito = calendarioLancamentoPlanoEnsinoFavorito;
	}

	public Boolean getPlanoDisciplinaCoordenadorRel() {
		return planoDisciplinaCoordenadorRel;
	}

	public Boolean getPlanoDisciplinaCoordenadorRelFavorito() {
		return planoDisciplinaCoordenadorRelFavorito;
	}

	public void setPlanoDisciplinaCoordenadorRel(Boolean planoDisciplinaCoordenadorRel) {
		this.planoDisciplinaCoordenadorRel = planoDisciplinaCoordenadorRel;
	}

	public void setPlanoDisciplinaCoordenadorRelFavorito(Boolean planoDisciplinaCoordenadorRelFavorito) {
		this.planoDisciplinaCoordenadorRelFavorito = planoDisciplinaCoordenadorRelFavorito;
	}

	public Boolean getRelatorioSEIDecidirRequerimento() {
		if (relatorioSEIDecidirRequerimento == null) {
			relatorioSEIDecidirRequerimento = false;
		}
		return relatorioSEIDecidirRequerimento;
	}

	public void setRelatorioSEIDecidirRequerimento(Boolean relatorioSEIDecidirRequerimento) {
		this.relatorioSEIDecidirRequerimento = relatorioSEIDecidirRequerimento;
	}

	public Boolean getRelatorioSEIDecidirRequerimentoFavorito() {
		if (relatorioSEIDecidirRequerimentoFavorito == null) {
			relatorioSEIDecidirRequerimentoFavorito = false;
		}
		return relatorioSEIDecidirRequerimentoFavorito;
	}

	public void setRelatorioSEIDecidirRequerimentoFavorito(Boolean relatorioSEIDecidirRequerimentoFavorito) {
		this.relatorioSEIDecidirRequerimentoFavorito = relatorioSEIDecidirRequerimentoFavorito;
	}

	private Boolean configuracaoAparenciaSistema;
	private Boolean configuracaoAparenciaSistemaFavorito;

	public Boolean getConfiguracaoAparenciaSistema() {
		if (configuracaoAparenciaSistema == null) {
			configuracaoAparenciaSistema = false;
		}
		return configuracaoAparenciaSistema;
	}

	public void setConfiguracaoAparenciaSistema(Boolean configuracaoAparenciaSistema) {
		this.configuracaoAparenciaSistema = configuracaoAparenciaSistema;
	}

	public Boolean getConfiguracaoAparenciaSistemaFavorito() {
		if (configuracaoAparenciaSistemaFavorito == null) {
			configuracaoAparenciaSistemaFavorito = false;
		}
		return configuracaoAparenciaSistemaFavorito;
	}

	public void setConfiguracaoAparenciaSistemaFavorito(Boolean configuracaoAparenciaSistemaFavorito) {
		this.configuracaoAparenciaSistemaFavorito = configuracaoAparenciaSistemaFavorito;
	}

	public Boolean getMinhaBibliotecaFavorito() {
		if (minhaBibliotecaFavorito == null) {
			minhaBibliotecaFavorito = false;
		}
		return minhaBibliotecaFavorito;
	}

	public void setMinhaBibliotecaFavorito(Boolean minhaBibliotecaFavorito) {
		this.minhaBibliotecaFavorito = minhaBibliotecaFavorito;
	}

	public Boolean getBibliotecaLexMagisterFavorito() {
		if (bibliotecaLexMagisterFavorito == null) {
			bibliotecaLexMagisterFavorito = false;
		}
		return bibliotecaLexMagisterFavorito;
	}

	public void setBibliotecaLexMagisterFavorito(Boolean bibliotecaLexMagisterFavorito) {
		this.bibliotecaLexMagisterFavorito = bibliotecaLexMagisterFavorito;
	}

	public Boolean getGestaoEventoConteudoTurmaVisaoProfessorFavorito() {
		if (gestaoEventoConteudoTurmaVisaoProfessorFavorito == null) {
			gestaoEventoConteudoTurmaVisaoProfessorFavorito = false;
		}
		return gestaoEventoConteudoTurmaVisaoProfessorFavorito;
	}

	public void setGestaoEventoConteudoTurmaVisaoProfessorFavorito(Boolean gestaoEventoConteudoTurmaVisaoProfessorFavorito) {
		this.gestaoEventoConteudoTurmaVisaoProfessorFavorito = gestaoEventoConteudoTurmaVisaoProfessorFavorito;
	}

	public Boolean getAtividadeDiscursivaProfessorFavorito() {
		if (atividadeDiscursivaProfessorFavorito == null) {
			atividadeDiscursivaProfessorFavorito = false;
		}
		return atividadeDiscursivaProfessorFavorito;
	}

	public void setAtividadeDiscursivaProfessorFavorito(Boolean atividadeDiscursivaProfessorFavorito) {
		this.atividadeDiscursivaProfessorFavorito = atividadeDiscursivaProfessorFavorito;
	}

	public Boolean getDownloadRelFavorito() {
		if (downloadRelFavorito == null) {
			downloadRelFavorito = false;
		}
		return downloadRelFavorito;
	}

	public void setDownloadRelFavorito(Boolean downloadRelFavorito) {
		this.downloadRelFavorito = downloadRelFavorito;
	}

	private Boolean relatorioAtividadeExtraClasseProfessorVisaoCoordenador = false;
	private Boolean relatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito = false;

	public Boolean getRelatorioAtividadeExtraClasseProfessorVisaoCoordenador() {
		return relatorioAtividadeExtraClasseProfessorVisaoCoordenador;
	}

	public void setRelatorioAtividadeExtraClasseProfessorVisaoCoordenador(Boolean relatorioAtividadeExtraClasseProfessorVisaoCoordenador) {
		this.relatorioAtividadeExtraClasseProfessorVisaoCoordenador = relatorioAtividadeExtraClasseProfessorVisaoCoordenador;
	}

	public Boolean getRelatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito() {
		return relatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito;
	}

	public void setRelatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito(Boolean relatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito) {
		this.relatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito = relatorioAtividadeExtraClasseProfessorVisaoCoordenadorFavorito;
	}

	public Boolean getPermitirAlterarMatrizCurricularAtivaInativa() {
		return permitirAlterarMatrizCurricularAtivaInativa;
	}

	public void setPermitirAlterarMatrizCurricularAtivaInativa(Boolean permitirAlterarMatrizCurricularAtivaInativa) {
		this.permitirAlterarMatrizCurricularAtivaInativa = permitirAlterarMatrizCurricularAtivaInativa;
	}

	public Boolean getUnificacaoCadastroUsuario() {
		if (unificacaoCadastroUsuario == null) {
			unificacaoCadastroUsuario = false;
		}
		return unificacaoCadastroUsuario;
	}

	public void setUnificacaoCadastroUsuario(Boolean unificacaoCadastroUsuario) {
		this.unificacaoCadastroUsuario = unificacaoCadastroUsuario;
	}

	public Boolean getUnificacaoCadastroUsuarioFavorito() {
		if (unificacaoCadastroUsuarioFavorito == null) {
			unificacaoCadastroUsuarioFavorito = false;
		}
		return unificacaoCadastroUsuarioFavorito;
	}

	public void setUnificacaoCadastroUsuarioFavorito(Boolean unificacaoCadastroUsuarioFavorito) {
		this.unificacaoCadastroUsuarioFavorito = unificacaoCadastroUsuarioFavorito;
	}

	public Boolean getRelatorioSEIDecidirProcessoSeletivo() {
		if (relatorioSEIDecidirProcessoSeletivo == null) {
			relatorioSEIDecidirProcessoSeletivo = false;
		}
		return relatorioSEIDecidirProcessoSeletivo;
	}

	public void setRelatorioSEIDecidirProcessoSeletivo(Boolean relatorioSEIDecidirProcessoSeletivo) {
		this.relatorioSEIDecidirProcessoSeletivo = relatorioSEIDecidirProcessoSeletivo;
	}

	public Boolean getRelatorioSEIDecidirProcessoSeletivoFavorito() {
		if (relatorioSEIDecidirProcessoSeletivoFavorito == null) {
			relatorioSEIDecidirProcessoSeletivoFavorito = false;
		}
		return relatorioSEIDecidirProcessoSeletivoFavorito;
	}

	public void setRelatorioSEIDecidirProcessoSeletivoFavorito(Boolean relatorioSEIDecidirProcessoSeletivoFavorito) {
		this.relatorioSEIDecidirProcessoSeletivoFavorito = relatorioSEIDecidirProcessoSeletivoFavorito;
	}

	public Boolean getBibliotecaPearsonFavorito() {
		return bibliotecaPearsonFavorito;
	}

	public void setBibliotecaPearsonFavorito(Boolean bibliotecaPearsonFavorito) {
		this.bibliotecaPearsonFavorito = bibliotecaPearsonFavorito;
	}

	public Boolean getBibliotecaBvPearson() {
		return bibliotecaBvPearson;
	}

	public void setBibliotecaBvPearson(Boolean bibliotecaBvPearson) {
		this.bibliotecaBvPearson = bibliotecaBvPearson;
	}

	private Boolean permitirCadastrarConfiguracaoHistorico;

	public Boolean getPermitirCadastrarConfiguracaoHistorico() {
		if (permitirCadastrarConfiguracaoHistorico == null) {
			permitirCadastrarConfiguracaoHistorico = false;
		}
		return permitirCadastrarConfiguracaoHistorico;
	}

	public void setPermitirCadastrarConfiguracaoHistorico(Boolean permitirCadastrarConfiguracaoHistorico) {
		this.permitirCadastrarConfiguracaoHistorico = permitirCadastrarConfiguracaoHistorico;
	}
	
	private Boolean permitirCadastrarConfiguracaoAtaResultadosFinais;
	
	public Boolean getPermitirCadastrarConfiguracaoAtaResultadosFinais() {
		if(permitirCadastrarConfiguracaoAtaResultadosFinais == null) {
			permitirCadastrarConfiguracaoAtaResultadosFinais = false;
		}
		return permitirCadastrarConfiguracaoAtaResultadosFinais;
	}

	public void setPermitirCadastrarConfiguracaoAtaResultadosFinais(
			Boolean permitirCadastrarConfiguracaoAtaResultadosFinais) {
		this.permitirCadastrarConfiguracaoAtaResultadosFinais = permitirCadastrarConfiguracaoAtaResultadosFinais;
	}
	public Boolean getImportarCandidatoInscricaoProcessoSeletivo() {
		return importarCandidatoInscricaoProcessoSeletivo;
	}

	public void setImportarCandidatoInscricaoProcessoSeletivo(Boolean importarCandidatoInscricaoProcessoSeletivo) {
		this.importarCandidatoInscricaoProcessoSeletivo = importarCandidatoInscricaoProcessoSeletivo;
	}

	public Boolean getImportarCandidatoInscricaoProcessoSeletivoFavorito() {
		return importarCandidatoInscricaoProcessoSeletivoFavorito;
	}

	public void setImportarCandidatoInscricaoProcessoSeletivoFavorito(Boolean importarCandidatoInscricaoProcessoSeletivoFavorito) {
		this.importarCandidatoInscricaoProcessoSeletivoFavorito = importarCandidatoInscricaoProcessoSeletivoFavorito;
	}

	public Boolean getRegistroLdap() {
		if (registroLdap == null) {
			registroLdap = false;
		}
		return registroLdap;
	}

	public void setRegistroLdap(Boolean registroLdap) {
		this.registroLdap = registroLdap;
	}

	public Boolean getRegistroLdapFavorito() {
		if (registroLdapFavorito == null) {
			registroLdapFavorito = false;
		}
		return registroLdapFavorito;
	}

	public void setRegistroLdapFavorito(Boolean registroLdapFavorito) {
		this.registroLdapFavorito = registroLdapFavorito;
	}

	public Boolean getGestaoSalasNotasBlackboard() {
		if (gestaoSalasNotasBlackboard == null) {
			gestaoSalasNotasBlackboard = false;
		}
		return gestaoSalasNotasBlackboard;
	}

	public void setGestaoSalasNotasBlackboard(Boolean gestaoSalasNotasBlackboard) {
		this.gestaoSalasNotasBlackboard = gestaoSalasNotasBlackboard;
	}

	public Boolean getGestaoSalasNotasBlackboardFavorito() {
		if (gestaoSalasNotasBlackboardFavorito == null) {
			gestaoSalasNotasBlackboardFavorito = false;
		}
		return gestaoSalasNotasBlackboardFavorito;
	}

	public void setGestaoSalasNotasBlackboardFavorito(Boolean gestaoSalasNotasBlackboardFavorito) {
		this.gestaoSalasNotasBlackboardFavorito = gestaoSalasNotasBlackboardFavorito;
	}
	
	private Boolean permiteCriarSalaBlackboard;

	public Boolean getPermiteCriarSalaBlackboard() {
		if(permiteCriarSalaBlackboard == null) {
			permiteCriarSalaBlackboard = false;
		}
		return permiteCriarSalaBlackboard;
	}

	public void setPermiteCriarSalaBlackboard(Boolean permiteCriarSalaBlackboard) {
		this.permiteCriarSalaBlackboard = permiteCriarSalaBlackboard;
	}

	public Boolean getPermiteApurarNotasBlackboard() {
		if(permiteApurarNotasBlackboard == null) {
			permiteApurarNotasBlackboard = false;
		}
		return permiteApurarNotasBlackboard;
	}

	public void setPermiteApurarNotasBlackboard(Boolean permiteApurarNotasBlackboard) {
		this.permiteApurarNotasBlackboard = permiteApurarNotasBlackboard;
	}
	
	public Boolean getPermiteCopiarConteudoBlackboard() {
		if(permiteCopiarConteudoBlackboard == null) {
			permiteCopiarConteudoBlackboard = false;
		}
		return permiteCopiarConteudoBlackboard;
	}
	
	public void setPermiteCopiarConteudoBlackboard(Boolean permiteCopiarConteudoBlackboard) {
		this.permiteCopiarConteudoBlackboard = permiteCopiarConteudoBlackboard;
	}
	
	
	
	public Boolean getPermitirComunicacaoInternaEnviarCopiaPorEmailIsn() {
		return permitirComunicacaoInternaEnviarCopiaPorEmail;
	}

	public Boolean getApresentarDocumentacaoGED() {
		return ApresentarDocumentacaoGED;
	}

	public void setApresentarDocumentacaoGED(Boolean apresentarDocumentacaoGED) {
		ApresentarDocumentacaoGED = apresentarDocumentacaoGED;
	}

	public Boolean getPermitirGerarRelatorioSeiDecidirRequerimentoApenasDados() {
		if(permitirGerarRelatorioSeiDecidirRequerimentoApenasDados == null) {
			permitirGerarRelatorioSeiDecidirRequerimentoApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirRequerimentoApenasDados;
	}


	public void setPermitirGerarRelatorioSeiDecidirRequerimentoApenasDados(Boolean permitirGerarRelatorioSeiDecidirRequerimentoApenasDados) {
		this.permitirGerarRelatorioSeiDecidirRequerimentoApenasDados = permitirGerarRelatorioSeiDecidirRequerimentoApenasDados;
	}
	
	public Boolean getEixoCurso() {
		if(eixoCurso == null) {
			eixoCurso = false;
		}
		return eixoCurso;
	}

	public void setEixoCurso(Boolean eixoCurso) {
		this.eixoCurso = eixoCurso;
	}


	public Boolean getPermitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados() {
		if(permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados == null) {
			permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados;
	}


	public void setPermitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados(Boolean permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados) {
		this.permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados = permitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados;
	}

	public Boolean getEixoCursoFavorito() {
		if(eixoCursoFavorito == null) {
			eixoCursoFavorito = false;
		}
		return eixoCursoFavorito;
	}
	public void setEixoCursoFavorito(Boolean eixoCursoFavorito) {
		this.eixoCursoFavorito = eixoCursoFavorito;
	}

	public Boolean getPermitirGerarRelatorioSeiDecidirRHApenasDados() {
		if(permitirGerarRelatorioSeiDecidirRHApenasDados == null) {
			permitirGerarRelatorioSeiDecidirRHApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirRHApenasDados;
	}

	public void setPermitirGerarRelatorioSeiDecidirRHApenasDados(Boolean permitirGerarRelatorioSeiDecidirRHApenasDados) {
		this.permitirGerarRelatorioSeiDecidirRHApenasDados = permitirGerarRelatorioSeiDecidirRHApenasDados;
	}

	public Boolean getPermitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados() {
		if(permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados == null) {
			permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados = false;
		}
		return permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados;
	}

	public void setPermitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados(
			Boolean permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados) {
		this.permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados = permitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados;
	}
	
	public Boolean getLinksUteis() {
		if(linksUteis == null) {
			linksUteis = false;
		}
		return linksUteis;
	}
	
	public void setLinksUteis(Boolean linksUteis) {
		this.linksUteis = linksUteis;
	}
	
	public Boolean getLinksUteisFavorito() {
		return linksUteisFavorito;
	}
	
	public void setLinksUteisFavorito(Boolean linksUteisFavorito) {
		this.linksUteisFavorito = linksUteisFavorito;
	}
	
	public Boolean getLogHistorico() {
		if (logHistorico == null) {
			logHistorico = false;
		}
		return logHistorico;
	}

	public void setLogHistorico(Boolean logHistorico) {
		this.logHistorico = logHistorico;
	}

	public Boolean getLogHistoricoFavorito() {
		if (logHistoricoFavorito == null) {
			logHistoricoFavorito = false;
		}
		return logHistoricoFavorito;
	}

	public void setLogHistoricoFavorito(Boolean logHistoricoFavorito) {
		this.logHistoricoFavorito = logHistoricoFavorito;
	}

	public Boolean getRequerimentoOperacaoLoteFavorito() {
		if(requerimentoOperacaoLoteFavorito == null) {
			requerimentoOperacaoLoteFavorito =  false;
		}
		return requerimentoOperacaoLoteFavorito;
	}

	public void setRequerimentoOperacaoLoteFavorito(Boolean requerimentoOperacaoLoteFavorito) {
		this.requerimentoOperacaoLoteFavorito = requerimentoOperacaoLoteFavorito;
	}
	
	private Boolean ofertaDisciplina;
	private Boolean ofertaDisciplinaFavorito;

	public Boolean getMotivoIndeferimentoDocumentoAluno() {
		if (motivoIndeferimentoDocumentoAluno == null) {
			motivoIndeferimentoDocumentoAluno = false;
		}
		return motivoIndeferimentoDocumentoAluno;
	}

	public void setMotivoIndeferimentoDocumentoAluno(Boolean motivoIndeferimentoDocumentoAluno) {
		this.motivoIndeferimentoDocumentoAluno = motivoIndeferimentoDocumentoAluno;
	}
	
	public Boolean getMotivoIndeferimentoDocumentoAlunoFavorito() {
		if(motivoIndeferimentoDocumentoAlunoFavorito == null) {
			motivoIndeferimentoDocumentoAlunoFavorito = false;
		}
		return motivoIndeferimentoDocumentoAlunoFavorito;
	}
	
	public void setMotivoIndeferimentoDocumentoAlunoFavorito(Boolean motivoIndeferimentoDocumentoAlunoFavorito) {
		this.motivoIndeferimentoDocumentoAlunoFavorito = motivoIndeferimentoDocumentoAlunoFavorito;
	}
	public Boolean getOfertaDisciplina() {
		if(ofertaDisciplina == null) {
			ofertaDisciplina =  false;
		}
		return ofertaDisciplina;
	}

	public void setOfertaDisciplina(Boolean ofertaDisciplina) {
		this.ofertaDisciplina = ofertaDisciplina;
	}

	public Boolean getOfertaDisciplinaFavorito() {
		if(ofertaDisciplinaFavorito == null) {
			ofertaDisciplinaFavorito =  false;
		}
		return ofertaDisciplinaFavorito;
	}

	public void setOfertaDisciplinaFavorito(Boolean ofertaDisciplinaFavorito) {
		this.ofertaDisciplinaFavorito = ofertaDisciplinaFavorito;
	}
	
	private Boolean permiteExcluirOfertaDisciplinaComAluno;

	public Boolean getPermiteExcluirOfertaDisciplinaComAluno() {
		if(permiteExcluirOfertaDisciplinaComAluno == null) {
			permiteExcluirOfertaDisciplinaComAluno =  false;
		}
		return permiteExcluirOfertaDisciplinaComAluno;
	}

	public void setPermiteExcluirOfertaDisciplinaComAluno(Boolean permiteExcluirOfertaDisciplinaComAluno) {
		this.permiteExcluirOfertaDisciplinaComAluno = permiteExcluirOfertaDisciplinaComAluno;
	}
	
	private Boolean permiteIncluirOfertaDisciplinaHistoricoAluno;

	public Boolean getPermiteIncluirOfertaDisciplinaHistoricoAluno() {
		if(permiteIncluirOfertaDisciplinaHistoricoAluno == null) {
			permiteIncluirOfertaDisciplinaHistoricoAluno =  false;
		}
		return permiteIncluirOfertaDisciplinaHistoricoAluno;
	}

	public void setPermiteIncluirOfertaDisciplinaHistoricoAluno(Boolean permiteIncluirOfertaDisciplinaHistoricoAluno) {
		this.permiteIncluirOfertaDisciplinaHistoricoAluno = permiteIncluirOfertaDisciplinaHistoricoAluno;
	}

	private Boolean permiteCriarOfertaDisciplinaTurma;

	public Boolean getPermiteCriarOfertaDisciplinaTurma() {
		if(permiteCriarOfertaDisciplinaTurma == null) {
			permiteCriarOfertaDisciplinaTurma =  false;
		}
		return permiteCriarOfertaDisciplinaTurma;
	}

	public void setPermiteCriarOfertaDisciplinaTurma(Boolean permiteCriarOfertaDisciplinaTurma) {
		this.permiteCriarOfertaDisciplinaTurma = permiteCriarOfertaDisciplinaTurma;
	}

	public Boolean getPermitirVisualizarScriptSqlRelatorioSeiDecidirReceita() {
		if (permitirVisualizarScriptSqlRelatorioSeiDecidirReceita == null) {
			permitirVisualizarScriptSqlRelatorioSeiDecidirReceita = false;
		}
		return permitirVisualizarScriptSqlRelatorioSeiDecidirReceita;
	}

	public void setPermitirVisualizarScriptSqlRelatorioSeiDecidirReceita(
			Boolean permitirVisualizarScriptSqlRelatorioSeiDecidirReceita) {
		this.permitirVisualizarScriptSqlRelatorioSeiDecidirReceita = permitirVisualizarScriptSqlRelatorioSeiDecidirReceita;
	}

	public Boolean getPermitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca() {
		if (permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca == null) {
			permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca = false;
		}
		return permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca;
	}

	public void setPermitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca(
			Boolean permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca) {
		this.permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca = permitirVisualizarScriptSqlRelatorioSeiDecidirBiblioteca;
	}

	private Boolean apresentarDataDisponibilizacaoMaterial;
	private Boolean permitirUsuarioRealizarUpload;
	private Boolean postarMaterialComTurmaObrigatoriamenteInformado;

	public Boolean getApresentarDataDisponibilizacaoMaterial() {
		if(apresentarDataDisponibilizacaoMaterial == null) {
			apresentarDataDisponibilizacaoMaterial = false;
		}
		return apresentarDataDisponibilizacaoMaterial;
	}

	public void setApresentarDataDisponibilizacaoMaterial(Boolean apresentarDataDisponibilizacaoMaterial) {
		this.apresentarDataDisponibilizacaoMaterial = apresentarDataDisponibilizacaoMaterial;
	}

	public Boolean getPermitirUsuarioRealizarUpload() {
		if(permitirUsuarioRealizarUpload == null) {
			permitirUsuarioRealizarUpload = false;
		}
		return permitirUsuarioRealizarUpload;
	}

	public void setPermitirUsuarioRealizarUpload(Boolean permitirUsuarioRealizarUpload) {
		this.permitirUsuarioRealizarUpload = permitirUsuarioRealizarUpload;
	}

	public Boolean getPostarMaterialComTurmaObrigatoriamenteInformado() {
		if(postarMaterialComTurmaObrigatoriamenteInformado == null) {
			postarMaterialComTurmaObrigatoriamenteInformado = false;
		}
		return postarMaterialComTurmaObrigatoriamenteInformado;
	}

	public void setPostarMaterialComTurmaObrigatoriamenteInformado(
			Boolean postarMaterialComTurmaObrigatoriamenteInformado) {
		this.postarMaterialComTurmaObrigatoriamenteInformado = postarMaterialComTurmaObrigatoriamenteInformado;
	}

	private Boolean requerimento_PermitirEnviarDepartamentoAnterior;

	public Boolean getRequerimento_PermitirEnviarDepartamentoAnterior() {
		if(requerimento_PermitirEnviarDepartamentoAnterior == null) {
			requerimento_PermitirEnviarDepartamentoAnterior = false;
		}
		return requerimento_PermitirEnviarDepartamentoAnterior;
	}

	public void setRequerimento_PermitirEnviarDepartamentoAnterior(
			Boolean requerimento_PermitirEnviarDepartamentoAnterior) {
		this.requerimento_PermitirEnviarDepartamentoAnterior = requerimento_PermitirEnviarDepartamentoAnterior;
	}


	public Boolean getPermitirAlterarFaltasProvenienteTransferencia() {
		if (permitirAlterarFaltasProvenienteTransferencia == null) {
			permitirAlterarFaltasProvenienteTransferencia = false;
		}
		return permitirAlterarFaltasProvenienteTransferencia;
	}
	
	public Boolean getConfiguracaoDiplomaDigital() {
		return configuracaoDiplomaDigital;
	}
	
	public void setConfiguracaoDiplomaDigital(Boolean configuracaoDiplomaDigital) {
		this.configuracaoDiplomaDigital = configuracaoDiplomaDigital;
	}
	
	public Boolean getConfiguracaoDiplomaDigitalFavorito() {
		return configuracaoDiplomaDigitalFavorito;
	}
	
	public void setConfiguracaoDiplomaDigitalFavorito(Boolean configuracaoDiplomaDigitalFavorito) {
		this.configuracaoDiplomaDigitalFavorito = configuracaoDiplomaDigitalFavorito;
	}

	public Boolean getGestaoXmlGradeCurricular() {
		return gestaoXmlGradeCurricular;
	}
	
	public void setGestaoXmlGradeCurricular(Boolean gestaoXmlGradeCurricular) {
		this.gestaoXmlGradeCurricular = gestaoXmlGradeCurricular;
	}
	
	public Boolean getGestaoXmlGradeCurricularFavorito() {
		return gestaoXmlGradeCurricularFavorito;
	}
	
	public void setGestaoXmlGradeCurricularFavorito(Boolean gestaoXmlGradeCurricularFavorito) {
		this.gestaoXmlGradeCurricularFavorito = gestaoXmlGradeCurricularFavorito;
	}


	public Boolean getRelatorioSEIDecidirAdministrativo() {
		if (relatorioSEIDecidirAdministrativo == null) {
			relatorioSEIDecidirAdministrativo = false;
		}
		return relatorioSEIDecidirAdministrativo;
	}


	public void setPermitirAlterarFaltasProvenienteTransferencia(Boolean permitirAlterarFaltasProvenienteTransferencia) {
		this.permitirAlterarFaltasProvenienteTransferencia = permitirAlterarFaltasProvenienteTransferencia;
	}
	

	public void setRelatorioSEIDecidirAdministrativo(Boolean relatorioSEIDecidirAdministrativo) {
		this.relatorioSEIDecidirAdministrativo = relatorioSEIDecidirAdministrativo;

	}

	public Boolean getRelatorioSEIDecidirAdministrativoFavorito() {
		if (relatorioSEIDecidirAdministrativoFavorito == null) {
			relatorioSEIDecidirAdministrativoFavorito = false;
		}
		return relatorioSEIDecidirAdministrativoFavorito;
	}

	public void setRelatorioSEIDecidirAdministrativoFavorito(Boolean relatorioSEIDecidirAdministrativoFavorito) {
		this.relatorioSEIDecidirAdministrativoFavorito = relatorioSEIDecidirAdministrativoFavorito;
	}

	public Boolean getRelatorioSEIDecidirAvaliacaoInstitucional() {
		if (relatorioSEIDecidirAvaliacaoInstitucional == null) {
			relatorioSEIDecidirAvaliacaoInstitucional = false;
		}
		return relatorioSEIDecidirAvaliacaoInstitucional;
	}

	public void setRelatorioSEIDecidirAvaliacaoInstitucional(Boolean relatorioSEIDecidirAvaliacaoInstitucional) {
		this.relatorioSEIDecidirAvaliacaoInstitucional = relatorioSEIDecidirAvaliacaoInstitucional;
	}

	public Boolean getRelatorioSEIDecidirAvaliacaoInstitucionalFavorito() {
		if (relatorioSEIDecidirAvaliacaoInstitucionalFavorito == null) {
			relatorioSEIDecidirAvaliacaoInstitucionalFavorito = false;
		}
		return relatorioSEIDecidirAvaliacaoInstitucionalFavorito;
	}

	public void setRelatorioSEIDecidirAvaliacaoInstitucionalFavorito(
			Boolean relatorioSEIDecidirAvaliacaoInstitucionalFavorito) {
		this.relatorioSEIDecidirAvaliacaoInstitucionalFavorito = relatorioSEIDecidirAvaliacaoInstitucionalFavorito;
	}

	public Boolean getRelatorioSEIDecidirCompra() {
		if (relatorioSEIDecidirCompra == null) {
			relatorioSEIDecidirCompra = false;
		}
		return relatorioSEIDecidirCompra;
	}

	public void setRelatorioSEIDecidirCompra(Boolean relatorioSEIDecidirCompra) {
		this.relatorioSEIDecidirCompra = relatorioSEIDecidirCompra;
	}

	public Boolean getRelatorioSEIDecidirCompraFavorito() {
		if (relatorioSEIDecidirCompraFavorito == null) {
			relatorioSEIDecidirCompraFavorito = false;
		}
		return relatorioSEIDecidirCompraFavorito;
	}

	public void setRelatorioSEIDecidirCompraFavorito(Boolean relatorioSEIDecidirCompraFavorito) {
		this.relatorioSEIDecidirCompraFavorito = relatorioSEIDecidirCompraFavorito;
	}

	public Boolean getRelatorioSEIDecidirEAD() {
		if (relatorioSEIDecidirEAD == null) {
			relatorioSEIDecidirEAD = false;
		}
		return relatorioSEIDecidirEAD;
	}

	public void setRelatorioSEIDecidirEAD(Boolean relatorioSEIDecidirEAD) {
		this.relatorioSEIDecidirEAD = relatorioSEIDecidirEAD;
	}

	public Boolean getRelatorioSEIDecidirEADFavorito() {
		if (relatorioSEIDecidirEADFavorito == null) {
			relatorioSEIDecidirEADFavorito = false;
		}
		return relatorioSEIDecidirEADFavorito;
	}

	public void setRelatorioSEIDecidirEADFavorito(Boolean relatorioSEIDecidirEADFavorito) {
		this.relatorioSEIDecidirEADFavorito = relatorioSEIDecidirEADFavorito;
	}

	
	public Boolean getRelatorioSEIDecidirNotaFiscal() {
		if (relatorioSEIDecidirNotaFiscal == null) {
			relatorioSEIDecidirNotaFiscal = false;
		}
		return relatorioSEIDecidirNotaFiscal;
	}

	public void setRelatorioSEIDecidirNotaFiscal(Boolean relatorioSEIDecidirNotaFiscal) {
		this.relatorioSEIDecidirNotaFiscal = relatorioSEIDecidirNotaFiscal;
	}

	public Boolean getRelatorioSEIDecidirNotaFiscalFavorito() {
		if (relatorioSEIDecidirNotaFiscalFavorito == null) {
			relatorioSEIDecidirNotaFiscalFavorito = false;
		}
		return relatorioSEIDecidirNotaFiscalFavorito;
	}

	public void setRelatorioSEIDecidirNotaFiscalFavorito(Boolean relatorioSEIDecidirNotaFiscalFavorito) {
		this.relatorioSEIDecidirNotaFiscalFavorito = relatorioSEIDecidirNotaFiscalFavorito;
	}

	public Boolean getRelatorioSEIDecidirPatrimonio() {
		if (relatorioSEIDecidirPatrimonio == null) {
			relatorioSEIDecidirPatrimonio = false;
		}
		return relatorioSEIDecidirPatrimonio;
	}

	public void setRelatorioSEIDecidirPatrimonio(Boolean relatorioSEIDecidirPatrimonio) {
		this.relatorioSEIDecidirPatrimonio = relatorioSEIDecidirPatrimonio;
	}

	public Boolean getRelatorioSEIDecidirPatrimonioFavorito() {
		if (relatorioSEIDecidirPatrimonioFavorito == null) {
			relatorioSEIDecidirPatrimonioFavorito = false;
		}
		return relatorioSEIDecidirPatrimonioFavorito;
	}

	public void setRelatorioSEIDecidirPatrimonioFavorito(Boolean relatorioSEIDecidirPatrimonioFavorito) {
		this.relatorioSEIDecidirPatrimonioFavorito = relatorioSEIDecidirPatrimonioFavorito;
	}
	
	public Boolean getPermitirFechamentoNotaBlackBoard() {
		if (permitirFechamentoNotaBlackBoard == null) {
			permitirFechamentoNotaBlackBoard = Boolean.FALSE;
		}
		return permitirFechamentoNotaBlackBoard;
	}
	
	public void setPermitirFechamentoNotaBlackBoard(Boolean permitirFechamentoNotaBlackBoard) {
		this.permitirFechamentoNotaBlackBoard = permitirFechamentoNotaBlackBoard;
	}
	

	public Boolean getPermitirUploadDeferimentoIndeferimentoDocumento() {
		if(permitirUploadDeferimentoIndeferimentoDocumento == null ) {
			permitirUploadDeferimentoIndeferimentoDocumento = false;
		}
		return permitirUploadDeferimentoIndeferimentoDocumento;
	}

	public void setPermitirUploadDeferimentoIndeferimentoDocumento(
			Boolean permitirUploadDeferimentoIndeferimentoDocumento) {
		this.permitirUploadDeferimentoIndeferimentoDocumento = permitirUploadDeferimentoIndeferimentoDocumento;
	}

	public Boolean getArquivoAssinadoHistorico() {
		return arquivoAssinadoHistorico;
	}

	public void setArquivoAssinadoHistorico(Boolean arquivoAssinadoHistorico) {
		this.arquivoAssinadoHistorico = arquivoAssinadoHistorico;
	}
	
	public Boolean getArquivoAssinadoHistoricoFavorito() {
		return arquivoAssinadoHistoricoFavorito;
	}

	public void setArquivoAssinadoHistoricoFavorito(Boolean arquivoAssinadoHistoricoFavorito) {
		this.arquivoAssinadoHistoricoFavorito = arquivoAssinadoHistoricoFavorito;
	}

	public Boolean getArquivoAssinadoDiploma() {
		return arquivoAssinadoDiploma;
	}

	public void setArquivoAssinadoDiploma(Boolean arquivoAssinadoDiploma) {
		this.arquivoAssinadoDiploma = arquivoAssinadoDiploma;
	}
	
	public Boolean getArquivoAssinadoDiplomaFavorito() {
		return arquivoAssinadoDiplomaFavorito;
	}

	public void setArquivoAssinadoDiplomaFavorito(Boolean arquivoAssinadoDiplomaFavorito) {
		this.arquivoAssinadoDiplomaFavorito = arquivoAssinadoDiplomaFavorito;
	}

	public Boolean getFormularioRelatorioFacilitador() {
		if (formularioRelatorioFacilitador == null) {
			formularioRelatorioFacilitador = false;
		}
		return formularioRelatorioFacilitador;
	}

	public void setFormularioRelatorioFacilitador(Boolean formularioRelatorioFacilitador) {
		this.formularioRelatorioFacilitador = formularioRelatorioFacilitador;
	}

	public Boolean getFormularioRelatorioFacilitadorFavorito() {
		if (formularioRelatorioFacilitadorFavorito == null) {
			formularioRelatorioFacilitadorFavorito = false;
		}
		return formularioRelatorioFacilitadorFavorito;
	}

	public void setFormularioRelatorioFacilitadorFavorito(Boolean formularioRelatorioFacilitadorFavorito) {
		this.formularioRelatorioFacilitadorFavorito = formularioRelatorioFacilitadorFavorito;
	}

	public Boolean getCamposFormularioRelatorioFacilitador() {
		if (camposFormularioRelatorioFacilitador == null) {
			camposFormularioRelatorioFacilitador = false;
		}
		return camposFormularioRelatorioFacilitador;
	}

	public void setCamposFormularioRelatorioFacilitador(Boolean camposFormularioRelatorioFacilitador) {
		this.camposFormularioRelatorioFacilitador = camposFormularioRelatorioFacilitador;
	}

	public Boolean getCamposFormularioRelatorioFacilitadorFavorito() {
		if (camposFormularioRelatorioFacilitadorFavorito == null) {
			camposFormularioRelatorioFacilitadorFavorito = false;
		}
		return camposFormularioRelatorioFacilitadorFavorito;
	}

	public void setCamposFormularioRelatorioFacilitadorFavorito(Boolean camposFormularioRelatorioFacilitadorFavorito) {
		this.camposFormularioRelatorioFacilitadorFavorito = camposFormularioRelatorioFacilitadorFavorito;
	}

	public Boolean getCalendarioRelatorioFinalFacilitador() {
		if (calendarioRelatorioFinalFacilitador == null) {
			calendarioRelatorioFinalFacilitador = false;
		}
		return calendarioRelatorioFinalFacilitador;
	}

	public void setCalendarioRelatorioFinalFacilitador(Boolean calendarioRelatorioFinalFacilitador) {
		this.calendarioRelatorioFinalFacilitador = calendarioRelatorioFinalFacilitador;
	}

	public Boolean getCalendarioRelatorioFinalFacilitadorFavorito() {
		if (calendarioRelatorioFinalFacilitadorFavorito == null) {
			calendarioRelatorioFinalFacilitadorFavorito = false;
		}
		return calendarioRelatorioFinalFacilitadorFavorito;
	}

	public void setCalendarioRelatorioFinalFacilitadorFavorito(Boolean calendarioRelatorioFinalFacilitadorFavorito) {
		this.calendarioRelatorioFinalFacilitadorFavorito = calendarioRelatorioFinalFacilitadorFavorito;
	}

	public Boolean getMapaRelatorioFacilitador() {
		if (mapaRelatorioFacilitador == null) {
			mapaRelatorioFacilitador = false;
		}
		return mapaRelatorioFacilitador;
	}

	public void setMapaRelatorioFacilitador(Boolean mapaRelatorioFacilitador) {
		this.mapaRelatorioFacilitador = mapaRelatorioFacilitador;
	}

	public Boolean getMapaRelatorioFacilitadorFavorito() {
		if (mapaRelatorioFacilitadorFavorito == null) {
			mapaRelatorioFacilitadorFavorito = false;
		}
		return mapaRelatorioFacilitadorFavorito;
	}

	public void setMapaRelatorioFacilitadorFavorito(Boolean mapaRelatorioFacilitadorFavorito) {
		this.mapaRelatorioFacilitadorFavorito = mapaRelatorioFacilitadorFavorito;
	}

	public Boolean getPermiteConfigurarIntegracaoMestreGR() {
		if (permiteConfigurarIntegracaoMestreGR == null){
			permiteConfigurarIntegracaoMestreGR = false;
		}
		return permiteConfigurarIntegracaoMestreGR;
	}

	public void setPermiteConfigurarIntegracaoMestreGR(Boolean permiteConfigurarIntegracaoMestreGR) {
		this.permiteConfigurarIntegracaoMestreGR = permiteConfigurarIntegracaoMestreGR;
	}
}