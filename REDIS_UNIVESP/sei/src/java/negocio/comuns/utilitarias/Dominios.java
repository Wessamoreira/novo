package negocio.comuns.utilitarias;

import java.util.Hashtable;

/**
 * Classe estática responsável por manter os domínios de valores utilizados pela
 * aplicação. Um domínio define um conjunto de valores que um determinado
 * atributo pode assumir. Exemplo de domínios são: Estados Federativos; Tipos de
 * Sexo; Estado Cívil. Ao alterar um domínio neste classe, o valor modificado
 * estará automaticamente disponível em toda a aplicação.
 */
@SuppressWarnings("rawtypes")
public abstract class Dominios {

	protected static Hashtable nivelCategoriaDespesa = new Hashtable();
    protected static Hashtable tipoMovimentacaoEstoque = new Hashtable();
    protected static Hashtable situacaoEntregaRecebimento = new Hashtable();
    protected static Hashtable situacaoFinanceira = new Hashtable();
    protected static Hashtable situacaoAutorizacao = new Hashtable();
    protected static Hashtable alunoFuncionarioCandidatoParceiro = new Hashtable();
    protected static Hashtable contaPagarTipoJuroTipoMulta = new Hashtable();
    protected static Hashtable planoFinanceiroCursoTipoCalculoParcela = new Hashtable();
    protected static Hashtable planoFinanceiroCursoModeloGeracaoParcelas = new Hashtable();
    protected static Hashtable configuracaoFinanceiroTipoCalcJuros = new Hashtable();
    protected static Hashtable convenioFormaRecebimentoParceiro = new Hashtable();
    protected static Hashtable convenioSituacao = new Hashtable();
    protected static Hashtable convenioTipoCobertura = new Hashtable();
    protected static Hashtable situacaoCampanhaMarketing = new Hashtable();
    protected static Hashtable situacaoProcessoSeletivo = new Hashtable();
    protected static Hashtable opcaoResultadoProcessoSeletivo = new Hashtable();
    protected static Hashtable tipoReferenciareferenciaBibliografica = new Hashtable();
    protected static Hashtable situacaoMatricula = new Hashtable();
    protected static Hashtable situacaoHistorico = new Hashtable();
    protected static Hashtable tipoHistoricoHistorico = new Hashtable();
    protected static Hashtable tipoFiliacao = new Hashtable();
    protected static Hashtable tipoDocumentoDocumentacaoMatricula = new Hashtable();
    protected static Hashtable tipoDisciplinaDisciplina = new Hashtable();
    protected static Hashtable diaSemanaDisponibilidadeHorario = new Hashtable();
    protected static Hashtable regimeAprovacaoCurso = new Hashtable();
    protected static Hashtable regimeCurso = new Hashtable();
    protected static Hashtable marcadorTituloBancoCurriculum = new Hashtable();
    protected static Hashtable tipoOrigemReceita = new Hashtable();
    protected static Hashtable tipoOrigemDebitoTerceiros = new Hashtable();
    protected static Hashtable tipoOrigemDespesa = new Hashtable();
    protected static Hashtable tipoValorConvenio = new Hashtable();
    protected static Hashtable tipoContratoDespesas = new Hashtable();
    protected static Hashtable tipoJustificativaAlteracaoMatricula = new Hashtable();
    protected static Hashtable tipoItemPlanoFinanceiroAluno = new Hashtable();
    protected static Hashtable escopoLink = new Hashtable();
    protected static Hashtable situacaoEntregaDocumentacao = new Hashtable();
    protected static Hashtable diaSemana = new Hashtable();
    protected static Hashtable situacaoProcessoMatricula = new Hashtable();
    protected static Hashtable periodicidadeCurso = new Hashtable();
    protected static Hashtable nivelEducacionalCurso = new Hashtable();
    protected static Hashtable situacaoAcademicoCancelamento = new Hashtable();
    protected static Hashtable modalidadeEnsinoMedioProcSel = new Hashtable();
    protected static Hashtable meioTransporteUtilizadoParaIntituicaoProcSel = new Hashtable();
    protected static Hashtable tipoEsportePraticaProcSel = new Hashtable();
    protected static Hashtable freguentaCursoExtracurricularProcSel = new Hashtable();
    protected static Hashtable conhecimentoLingEspanholaProcSel = new Hashtable();
    protected static Hashtable conhecimentoLingInglesaProcSel = new Hashtable();
    protected static Hashtable corProcSel = new Hashtable();
    protected static Hashtable situacaoTurma = new Hashtable();
    protected static Hashtable escolaridadeFormacaoAcademica = new Hashtable();
    protected static Hashtable situacaoFormacaoAcademica = new Hashtable();
    protected static Hashtable tipoInstFormacaoAcademica = new Hashtable();
    protected static Hashtable situacaoCursoAcademico = new Hashtable();
    protected static Hashtable tipoPessoaBasicoPessoa = new Hashtable();
    protected static Hashtable tipoPesquisadorPublicacao = new Hashtable();
    protected static Hashtable tipoInsentivoInsentivoPesquisa = new Hashtable();
    protected static Hashtable cotacaoAutorizadaItemSolicitacaoCompra = new Hashtable();
    protected static Hashtable religiaoProcSel = new Hashtable();
    protected static Hashtable assuntoJornalLeituraProcSel = new Hashtable();
    protected static Hashtable leJornalProcSel = new Hashtable();
    protected static Hashtable meioAtualizacaoAcontecimentoProcSel = new Hashtable();
    protected static Hashtable tipoLivroLeituraProcSel = new Hashtable();
    protected static Hashtable existeMicrocomputadorResProcSel = new Hashtable();
    protected static Hashtable formaPagamentoPgtoServicoAcademico = new Hashtable();
    protected static Hashtable qtdLivroLeituraAnoProcSel = new Hashtable();
    protected static Hashtable situacaoPesquisadorLinhaPesquisa = new Hashtable();
    protected static Hashtable esperaCursoSuperiorProcSel = new Hashtable();
    protected static Hashtable tipoPesquisadorPesquisadorLinhaPesquisa = new Hashtable();
    protected static Hashtable pqPrestarVestibularInstituicao = new Hashtable();
    protected static Hashtable tipoPesqiosadorPublicacao = new Hashtable();
    protected static Hashtable conhecimentoProcessoSeletivo = new Hashtable();
    protected static Hashtable tipoPublicacaoPublicacaoPesquisa = new Hashtable();
    protected static Hashtable tipoParceiroParceiro = new Hashtable();
    protected static Hashtable situacaoDebitoFuncionarioFinanceiro = new Hashtable();
    protected static Hashtable tipoCustoCFGCustoAdministrativo = new Hashtable();
    protected static Hashtable tipoInscricaoInscricaoCursoExtensao = new Hashtable();
    protected static Hashtable tipoProfessorProfessorCursoExtensao = new Hashtable();
    protected static Hashtable motivoEscolhaCursoProcSel = new Hashtable();
    protected static Hashtable iniciouCursoSuperiorProcSel = new Hashtable();
    protected static Hashtable qtdFaculdadePrestouVestib = new Hashtable();
    protected static Hashtable anoConclusaoEnsinoMedioProcSel = new Hashtable();
    protected static Hashtable tipoEscolaCursouEnsinoMedioProcSel = new Hashtable();
    protected static Hashtable situacaoFinanceiraExtensao = new Hashtable();
    protected static Hashtable grauEscolaridadeMaeProcSel = new Hashtable();
    protected static Hashtable situacaoCursoExtensao = new Hashtable();
    protected static Hashtable grauEscolaridadePaiProcSel = new Hashtable();
    protected static Hashtable tipoPalestraPalestraEvento = new Hashtable();
    protected static Hashtable comoSeManterDuranteCursoProcSel = new Hashtable();
    protected static Hashtable tipoPessoaInscricaoEvento = new Hashtable();
    protected static Hashtable participacaoVidaEconomicaFamiliaProcSel = new Hashtable();
    protected static Hashtable situacaoFinanceiraEvento = new Hashtable();
    protected static Hashtable situacaoFinanceiraMatricula = new Hashtable();
    protected static Hashtable somaRendaFamiliaProcSel = new Hashtable();
    protected static Hashtable tipoSubmissaoEvento = new Hashtable();
    protected static Hashtable formasInscricaoEvento = new Hashtable();
    protected static Hashtable tipoInscricaoEvento = new Hashtable();
    protected static Hashtable tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic = new Hashtable();
    protected static Hashtable situacaoSolicitacaoPgtoServicoAcademico = new Hashtable();
    protected static Hashtable tipoCorrespondencia = new Hashtable();
    protected static Hashtable situacao = new Hashtable();
    protected static Hashtable tipoEmpresa = new Hashtable();
    protected static Hashtable nivelAcesso = new Hashtable();
    protected static Hashtable estadoCivil = new Hashtable();
    protected static Hashtable escolaridade = new Hashtable();
    protected static Hashtable simNao = new Hashtable();
    protected static Hashtable estado = new Hashtable();
    protected static Hashtable sexo = new Hashtable();
    protected static Hashtable rendaMensalProcSel = new Hashtable();
    protected static Hashtable ramoAtividadeProcSel = new Hashtable();
    protected static Hashtable hrsTrabalhoSemanaProcSel = new Hashtable();
    protected static Hashtable residenteEmProcSel = new Hashtable();
    protected static Hashtable qtdFilhosTemProcSel = new Hashtable();
    protected static Hashtable qtdIrmaosProcSel = new Hashtable();
    protected static Hashtable situacaoSolicitacaoCompra = new Hashtable();
    protected static Hashtable tipoUnidadeProduto = new Hashtable();
    protected static Hashtable tipoDisciplinaProcessoSel = new Hashtable();
    protected static Hashtable tipoDestinacaoCustosPrevisaoCustos = new Hashtable();
    protected static Hashtable situacaoFinanceiraProtocolo = new Hashtable();
    protected static Hashtable tipoDestinatarioPgtServicoAcademico = new Hashtable();
    protected static Hashtable formaPagamentoPgtServicoAcademico = new Hashtable();
    protected static Hashtable situacaoProtocolo = new Hashtable();
    protected static Hashtable situacaoPgtServicoAcademico = new Hashtable();
    protected static Hashtable situacaoCotacao = new Hashtable();
    protected static Hashtable situacaoCompra = new Hashtable();
    protected static Hashtable situacaoCheque = new Hashtable();
    protected static Hashtable tipoCalculoResultadoQuestaoAvaliacao = new Hashtable();
    protected static Hashtable tipoRespostaQuestaoAvaliacao = new Hashtable();
    protected static Hashtable creditoDebito = new Hashtable();
    protected static Hashtable receberRecebidoNegociado = new Hashtable();
    protected static Hashtable pagarPagoNegociado = new Hashtable();
    protected static Hashtable procSeletivoNrOpcoesCurso = new Hashtable();
    protected static Hashtable matriculaPeriodoSituacao = new Hashtable();
    protected static Hashtable escopoAvaliacaoInstitucional = new Hashtable();
    protected static Hashtable situacaoItemEmprestimo = new Hashtable();
    protected static Hashtable tipoEntradaAcervo = new Hashtable();
    protected static Hashtable tipoPublicacaoPublicacao = new Hashtable();
    protected static Hashtable tipoSaidaItemRegistroSaidaAcervo = new Hashtable();
    protected static Hashtable situacaoEmprestimo = new Hashtable();
    protected static Hashtable tipoExemplarExemplar = new Hashtable();
    protected static Hashtable periodicidadeBiblioteca = new Hashtable();
    protected static Hashtable situacaoTrancamento = new Hashtable();
    protected static Hashtable tipoPublicacaoReferenciaBibliografica = new Hashtable();
    protected static Hashtable inscricaoOpcaoLinguaEstrangeira = new Hashtable();
    protected static Hashtable inscricaoFormaAcesso = new Hashtable();
    protected static Hashtable tipoOrigemContaPagar = new Hashtable();
    protected static Hashtable tipoOrigemContaReceber = new Hashtable();
    protected static Hashtable tipoOrigemContaReceberRelatorio = new Hashtable();
    protected static Hashtable conteudoPlanejamento = new Hashtable();
    protected static Hashtable situacaoGradeCurricular = new Hashtable();
    protected static Hashtable tipoComunicadoInterno = new Hashtable();
    protected static Hashtable tipoComunicadoInternoAluno = new Hashtable();
    protected static Hashtable tipoDestinatarioComunicadoInternoAluno = new Hashtable();
    protected static Hashtable tipoDestinatarioComunicadoInternoProfessor = new Hashtable();
    protected static Hashtable tipoDestinatarioComunicadoInternoCoordenador = new Hashtable();
    protected static Hashtable tipoDestinatarioComunicadoInterno = new Hashtable();
    protected static Hashtable prioridadeComunicadoInterno = new Hashtable();
    protected static Hashtable tipoUsuario = new Hashtable();
    protected static Hashtable tipoDespesaFinanceira = new Hashtable();
    protected static Hashtable tipoFormaPagamento = new Hashtable();
    protected static Hashtable tipoDesconto = new Hashtable();
    protected static Hashtable tipoRespostaQuestionario = new Hashtable();
    protected static Hashtable situacaoQuestionario = new Hashtable();
    protected static Hashtable tipoRequerimento = new Hashtable();
    protected static Hashtable tipoContasAPagar = new Hashtable();
    protected static Hashtable tipoEscopoQuestionario = new Hashtable();
    protected static Hashtable publicoAlvo = new Hashtable();
    protected static Hashtable tipoAutoria = new Hashtable();
    protected static Hashtable tipoSacado = new Hashtable();
    protected static Hashtable tipoSacadoContratosReceitas = new Hashtable();
    protected static Hashtable informarTurma = new Hashtable();
    protected static Hashtable situacaoRecebimentoCompra = new Hashtable();
    protected static Hashtable situacaoContratosDespesas = new Hashtable();
    protected static Hashtable situacaoContratosReceitas = new Hashtable();
    protected static Hashtable situacaoProvisaoDeCusto = new Hashtable();
    protected static Hashtable marcadoCandidato = new Hashtable();
    protected static Hashtable marcadoInscricao = new Hashtable();
    protected static Hashtable marcadoProcessoSeletivo = new Hashtable();
    protected static Hashtable marcadoResultadoProcessoSeletivo = new Hashtable();
    protected static Hashtable marcadoListaProcessoSeletivo = new Hashtable();
    protected static Hashtable atuaComoDocente = new Hashtable();
    protected static Hashtable marcadoAluno = new Hashtable();
    protected static Hashtable marcadoProfessor = new Hashtable();
    protected static Hashtable marcadorAlunoBancoCurriculum = new Hashtable();
    protected static Hashtable marcadorEmpresaBancoCurriculum = new Hashtable();
    protected static Hashtable marcadorVagaBancoCurriculum = new Hashtable();
    protected static Hashtable marcadoUnidadeEnsino = new Hashtable();
    protected static Hashtable marcadoDisciplinaDeclaracao = new Hashtable();
    protected static Hashtable marcadoOutras = new Hashtable();
    protected static Hashtable marcadoCurso = new Hashtable();
    protected static Hashtable marcadoMatricula = new Hashtable();
    protected static Hashtable marcadoDisciplina = new Hashtable();
    protected static Hashtable marcadoEstagio = new Hashtable();
    protected static Hashtable marcadoInscProcSeletivo = new Hashtable();
    protected static Hashtable marcadoContaReceber = new Hashtable();
    protected static Hashtable situacaoTextoPadrao = new Hashtable();
    protected static Hashtable situacaoMatriculaPeriodoPreMatriculada = new Hashtable();
    protected static Hashtable tipoDeclaracao = new Hashtable();
    protected static Hashtable situacaoNotaFiscalSaida = new Hashtable();

    static {
        inicializarDominioPublicoAlvo();
        inicializarDominioSituacaoAutorizacao();
        inicializarDominioSituacaoEntregaRecebimento();
        inicializarDominioSituacaoFinanceira();
        inicializarDominioTipoDespesaFinanceira();
        inicializarDominioSituacaoGradeCurricular();
        inicializarDominioTipoFormaPagamento();
        inicializarDominioContaPagarTipoJuroTipoMulta();
        inicializarDominioInscricaoFormaAcesso();
        inicializarDominioInscricaoOpcaoLinguaEstrangeira();
        inicializarDominioPlanoFinanceiroCursoTipoCalculoParcela();
        inicializarDominioPlanoFinanceiroCursoModeloGeracaoParcelas();
        inicializarDominioConfiguracaoFinanceiroTipoCalcJuros();
        inicializarDominioSituacaoMatriculaPeriodoPreMatriculada();
        inicializarDominioConvenioFormaRecebimentoParceiro();
        inicializarDominioConvenioSituacao();
        inicializarDominioConvenioTipoCobertura();
        inicializarDominioSituacaoCampanhaMarketing();
        inicializarDominioSituacaoProcessoSeletivo();
        inicializarDominioOpcaoResultadoProcessoSeletivo();
        inicializarDominioTipoReferenciareferenciaBibliografica();
        inicializarDominioSituacaoMatricula();
        inicializarDominioSituacaoHistorico();
        inicializarDominioTipoHistoricoHistorico();
        inicializarDominioTipoFiliacao();
        inicializarDominioTipoDocumentoDocumentacaoMatricula();
        inicializarDominioTipoDisciplinaDisciplina();
        inicializarDominioDiaSemanaDisponibilidadeHorario();
        inicializarDominioRegimeAprovacaoCurso();
        inicializarDominioRegimeCurso();
        inicializarDominioTipoOrigemReceita();
        inicializarDominioTipoOrigemDebitoTerceiros();
        inicializarDominioTipoOrigemDespesa();
        inicializarDominioTipoValorConvenio();
        inicializarDominioTipoContratoDespesas();
        inicializarDominioTipoJustificativaAlteracaoMatricula();
        inicializarDominioTipoItemPlanoFinanceiroAluno();
        inicializarDominioEscopoLink();
        inicializarDominioSituacaoEntregaDocumentacao();
        inicializarDominioDiaSemana();
        inicializarDominioSituacaoProcessoMatricula();
        inicializarDominioPeriodicidadeCurso();
        inicializarDominioNivelEducacionalCurso();
        inicializarDominioSituacaoAcademicoCancelamento();
        inicializarDominioModalidadeEnsinoMedioProcSel();
        inicializarDominioMeioTransporteUtilizadoParaIntituicaoProcSel();
        inicializarDominioTipoEsportePraticaProcSel();
        inicializarDominioFreguentaCursoExtracurricularProcSel();
        inicializarDominioConhecimentoLingEspanholaProcSel();
        inicializarDominioConhecimentoLingInglesaProcSel();
        inicializarDominioCorProcSel();
        inicializarDominioSituacaoTurma();
        inicializarDominioEscolaridadeFormacaoAcademica();
        inicializarDominioSituacaoFormacaoAcademica();
        inicializarDominioTipoInstFormacaoAcademica();
        inicializarDominioSituacaoCursoAcademico();
        inicializarDominioTipoPessoaBasicoPessoa();
        inicializarDominioTipoPesquisadorPublicacao();
        inicializarDominioTipoInsentivoInsentivoPesquisa();
        inicializarDominioCotacaoAutorizadaItemSolicitacaoCompra();
        inicializarDominioReligiaoProcSel();
        inicializarDominioAssuntoJornalLeituraProcSel();
        inicializarDominioLeJornalProcSel();
        inicializarDominioMeioAtualizacaoAcontecimentoProcSel();
        inicializarDominioTipoLivroLeituraProcSel();
        inicializarDominioExisteMicrocomputadorResProcSel();
        inicializarDominioFormaPagamentoPgtoServicoAcademico();
        inicializarDominioQtdLivroLeituraAnoProcSel();
        inicializarDominioSituacaoPesquisadorLinhaPesquisa();
        inicializarDominioEsperaCursoSuperiorProcSel();
        inicializarDominioTipoPesquisadorPesquisadorLinhaPesquisa();
        inicializarDominioPqPrestarVestibularInstituicao();
        inicializarDominioTipoPesqiosadorPublicacao();
        inicializarDominioConhecimentoProcessoSeletivo();
        inicializarDominioTipoPublicacaoPublicacaoPesquisa();
        inicializarDominioTipoParceiroParceiro();
        inicializarDominioSituacaoDebitoFuncionarioFinanceiro();
        inicializarDominioTipoCustoCFGCustoAdministrativo();
        inicializarDominioTipoInscricaoInscricaoCursoExtensao();
        inicializarDominioTipoProfessorProfessorCursoExtensao();
        inicializarDominioMotivoEscolhaCursoProcSel();
        inicializarDominioIniciouCursoSuperiorProcSel();
        inicializarDominioQtdFaculdadePrestouVestib();
        inicializarDominioAnoConclusaoEnsinoMedioProcSel();
        inicializarDominioTipoEscolaCursouEnsinoMedioProcSel();
        inicializarDominioSituacaoFinanceiraExtensao();
        inicializarDominioGrauEscolaridadeMaeProcSel();
        inicializarDominioSituacaoCursoExtensao();
        inicializarDominioGrauEscolaridadePaiProcSel();
        inicializarDominioTipoPalestraPalestraEvento();
        inicializarDominioComoSeManterDuranteCursoProcSel();
        inicializarDominioTipoPessoaInscricaoEvento();
        inicializarDominioParticipacaoVidaEconomicaFamiliaProcSel();
        inicializarDominioSituacaoFinanceiraEvento();
        inicializarDominioSomaRendaFamiliaProcSel();
        inicializarDominioTipoSubmissaoEvento();
        inicializarDominioFormasInscricaoEvento();
        inicializarDominioTipoInscricaoEvento();
        inicializarDominioTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic();
        inicializarDominioSituacaoSolicitacaoPgtoServicoAcademico();
        inicializarDominioTipoCorrespondencia();
        inicializarDominioSituacao();
        inicializarDominioTipoEmpresa();
        inicializarDominioNivelAcesso();
        inicializarDominioEstadoCivil();
        inicializarDominioEscolaridade();
        inicializarDominioSimNao();
        inicializarDominioEstado();
        inicializarDominioSexo();
        inicializarDominioRendaMensalProcSel();
        inicializarDominioRamoAtividadeProcSel();
        inicializarDominioHrsTrabalhoSemanaProcSel();
        inicializarDominioResidenteEmProcSel();
        inicializarDominioQtdFilhosTemProcSel();
        inicializarDominioQtdIrmaosProcSel();
        inicializarDominioSituacaoSolicitacaoCompra();
        inicializarDominioTipoUnidadeProduto();
        inicializarDominioTipoDisciplinaProcessoSel();
        inicializarDominioTipoDestinacaoCustosPrevisaoCustos();
        inicializarDominioSituacaoFinanceiraProtocolo();
        inicializarDominioTipoDestinatarioPgtServicoAcademico();
        inicializarDominioFormaPagamentoPgtServicoAcademico();
        inicializarDominioSituacaoProtocolo();
        inicializarDominioSituacaoPgtServicoAcademico();
        inicializarDominioSituacaoCotacao();
        inicializarDominioSituacaoCompra();
        inicializarDominioTipoCalculoResultadoQuestaoAvaliacao();
        inicializarDominioTipoRespostaQuestaoAvaliacao();
        inicializarDominioCreditoDebito();
        inicializarDominioReceberRecebidoNegociado();
        inicializarDominioPagarPagoNegociado();
        inicializarDominioProcSeletivoNrOpcoesCurso();
        inicializarDominioMatriculaPeriodoSituacao();
        inicializarDominioEscopoAvaliacaoInstitucional();
        inicializarDominioSituacaoItemEmprestimo();
        inicializarDominioTipoEntradaAcervo();
        inicializarDominioTipoPublicacaoPublicacao();
        inicializarDominioTipoSaidaItemRegistroSaidaAcervo();
        inicializarDominioSituacaoEmprestimo();
        inicializarDominioTipoExemplarExemplar();
        inicializarDominioPeriodicidadeBiblioteca();
        inicializarDominioSituacaoTrancamento();
        inicializarDominioTipoPublicacaoReferenciaBibliografica();
        inicializarDominioTipoOrigemContaPagar();
        inicializarDominioTipoOrigemContaReceber();
        inicializarDominioTipoOrigemContaReceberRelatorio();
        inicializarDominioConteudoPlanejamento();
        inicializarDominioAlunoFuncionarioCandidatoParceiro();
//        inicializarDominioTipoComunicadoInterno();
        inicializarDominioTipoComunicadoInternoAluno();
        inicializarDominioTipoDestinatarioComunicadoInternoAluno();
        inicializarDominioTipoDestinatarioComunicadoInternoProfessor();
        inicializarDominioTipoDestinatarioComunicadoInternoCoordenador();
//        inicializarDominioTipoDestinatarioComunicadoInterno();
        inicializarDominioPrioridadeComunicadoInterno();
        inicializarDominioTipoUsuario();
        inicializarDominioTipoMovimentacaoEstoque();
        inicializarDominioTipoDesconto();
        inicializarDominioTipoRespostaQuestionario();
        inicializarDominioSituacaoQuestionario();
        inicializarDominioTipoRequerimento();
        inicializarDominioTipoContasAPagar();
//        inicializarDominioTipoEscopoQuestionario();
        inicializarDominioTipoAutoria();
        inicializarDominioSituacaoFinanceiraMatricula();
        inicializarDominioSituacaoCheque();
        inicializarDominioNivelCategoriaDespesa();
        inicializarDominioTipoSacado();
        inicializarDominioTipoSacadoContratosReceitas();
        inicializarDominioInformarTurma();
        inicializarDominioSituacaoRecebimentoCompra();
        inicializarDominioSituacaoContratosDespesas();
        inicializarDominioSituacaoProvisaoDeCusto();
        inicializarDominioAtuaComoDocente();
        inicializarDominioMarcadoInscricao();
        inicializarDominioMarcadoCandidato();
        inicializarDominioMarcadoProcessoSeletivo();
        inicializarDominioMarcadoResultadoProcessoSeletivo();
        inicializarDominioMarcadoListaProcessoSeletivo();
        inicializarDominioMarcadoAluno();
        inicializarDominioMarcadoProfessor();
        inicializarDominioMarcadorAlunoBancoCurriculum();
        inicializarDominioMarcadorEmpresaBancoCurriculum();
        inicializarDominioMarcadorVagaBancoCurriculum();
        inicializarDominioMarcadoUnidadeEnsino();
        inicializarDominioMarcadoDisciplinaDeclaracao();
        inicializarDominioMarcadoEstagio();
        inicializarDominioMarcadoInscProcSeletivo();
        inicializarDominioMarcadorTituloBancoCurriculum();
        inicializarDominioMarcadoOutras();
        inicializarDominioMarcadoMatricula();
        inicializarDominioMarcadoDisciplina();
        inicializarDominioMarcadoCurso();
        inicializarDominioMarcadoContaReceber();
        inicializarDominioSituacaoTextoPadrao();
        inicializarDominioTipoDeclaracao();
    }

    private static void inicializarDominioSituacaoTextoPadrao() {
        getSituacaoTextoPadrao().put("IN", "Inativa");
        getSituacaoTextoPadrao().put("AT", "Ativa");
        getSituacaoTextoPadrao().put("EM", "Em construção");
    }

    private static void inicializarDominioMarcadoInscricao() {
        getMarcadoInscricao().put("[(10){}Numero_Inscricao]", "Numero_Inscricao");
        getMarcadoInscricao().put("[(70){}UnidadeEnsino_Inscricao]", "UnidadeEnsino_Inscricao");
        getMarcadoInscricao().put("[(70){}Curso_Inscricao]", "Curso_Inscricao");
        getMarcadoInscricao().put("[(50){}Turno_Inscricao]", "Turno_Inscricao");
        getMarcadoInscricao().put("[(50){}Situacao_Inscricao]", "Situacao_Inscricao");
        getMarcadoInscricao().put("[(50){}SituacaoInscricao_Inscricao]", "SituacaoInscricao_Inscricao");
        getMarcadoInscricao().put("[(10){}Classificacao_Inscricao]", "Classificacao_Inscricao");
        getMarcadoInscricao().put("[(10){}Chamada_Inscricao]", "Chamada_Inscricao");
        getMarcadoInscricao().put("[(20){}DataProva_Inscricao]", "DataProva_Inscricao");

    }

    private static void inicializarDominioMarcadoCandidato() {
    	getMarcadoCandidato().put("[(70){}Nome_Candidato]", "Nome_Candidato");
    	getMarcadoCandidato().put("[(50){}Endereco_Candidato]", "Endereco_Candidato");
    	getMarcadoCandidato().put("[(50){}ComplementoEnd_Candidato]", "ComplementoEnd_Candidato");
    	getMarcadoCandidato().put("[(50){}NumeroEnd_Candidato]", "NumeroEnd_Candidato");
        getMarcadoCandidato().put("[(40){}Cidade_Candidato]", "Cidade_Candidato");
        getMarcadoCandidato().put("[(40){}Estado_Candidato]", "Estado_Candidato");
        getMarcadoCandidato().put("[(40){}Bairro_Candidato]", "Bairro_Candidato");
        getMarcadoCandidato().put("[(15){}Sexo_Candidato]", "Sexo_Candidato");
        getMarcadoCandidato().put("[(20){}Naturalidade_Candidato]", "Naturalidade_Candidato");
        getMarcadoCandidato().put("[(20){}Nacionalidade_Candidato]", "Nacionalidade_Candidato");
        getMarcadoCandidato().put("[(20){}EstadoCivil_Candidato]", "EstadoCivil_Candidato");
        getMarcadoCandidato().put("[(20){}Rg_Candidato]", "Rg_Candidato");
        getMarcadoCandidato().put("[(20){}OrgaoEmissor_Candidato]", "OrgaoEmissor_Candidato");
        getMarcadoCandidato().put("[(20){}DataEmissaoRG_Candidato]", "DataEmissaoRG_Candidato");
        getMarcadoCandidato().put("[(10){}EstadoEmissor_Candidato]", "EstadoEmissor_Candidato");
        getMarcadoCandidato().put("[(14){}Cpf_Candidato]", "Cpf_Candidato");
        getMarcadoCandidato().put("[(20){}DataNasc_Candidato]", "DataNasc_Candidato");
        getMarcadoCandidato().put("[(3){}Idade_Candidato]", "Idade_Candidato");
        getMarcadoCandidato().put("[(10){}Codigo_Candidato]", "Codigo_Candidato");
        getMarcadoCandidato().put("[(16){}Telefone_Candidato]", "Telefone_Candidato");
        getMarcadoCandidato().put("[(16){}Celular_Candidato]", "Celular_Candidato");
        getMarcadoCandidato().put("[(16){}Comercial_Candidato]", "Comercial_Candidato");
        getMarcadoCandidato().put("[(10){}CEP_Candidato]", "CEP_Candidato");
        getMarcadoCandidato().put("[(40){}Email_Candidato]", "Email_Candidato");
        getMarcadoCandidato().put("[(40){}Email2_Candidato]", "Email2_Candidato");
        getMarcadoCandidato().put("[(70){}NomePai_Candidato]", "NomePai_Candidato");
        getMarcadoCandidato().put("[(70){}NomeMae_Candidato]", "NomeMae_Candidato");

    }

    private static void inicializarDominioMarcadoProcessoSeletivo() {
    	getMarcadoProcessoSeletivo().put("[(70){}Nome_ProcessoSeletivo]", "Nome_ProcessoSeletivo");
    }
    
    private static void inicializarDominioMarcadoResultadoProcessoSeletivo() {
    	getMarcadoResultadoProcessoSeletivo().put("[(70){}Situacao_ResultadoProcessoSeletivo]", "Situacao_ResultadoProcessoSeletivo");
    }
    
    private static void inicializarDominioMarcadoListaProcessoSeletivo() {
    	getMarcadoListaProcessoSeletivo().put("[(){}Lista_Insc_NomeCand_Classif_Sit_NrCham_ListaProcessoSeletivo]", "Lista_Insc_NomeCand_Classif_Sit_NrCham_ListaProcessoSeletivo");
    	getMarcadoListaProcessoSeletivo().put("[(){}Lista_Insc_NomeCand_LocalProva_ListaProcessoSeletivo]", "Lista_Insc_NomeCand_LocalProva_ListaProcessoSeletivo");
    	getMarcadoListaProcessoSeletivo().put("[(){}Lista_Insc_NomeCand_ListaProcessoSeletivo]", "Lista_Insc_NomeCand_ListaProcessoSeletivo");
    	//getMarcadoAluno().put("[(){}ListaEnade_Aluno]", "ListaEnade_Aluno");
    }
	
    private static void inicializarDominioMarcadoAluno() {
        getMarcadoAluno().put("[(70){}Nome_Aluno]", "Nome_Aluno");
        getMarcadoAluno().put("[(70){}Banco_Aluno]", "Banco_Aluno");
        getMarcadoAluno().put("[(70){}Agencia_Aluno]", "Agencia_Aluno");
        getMarcadoAluno().put("[(70){}ContaCorrente_Aluno]", "ContaCorrente_Aluno");
        getMarcadoAluno().put("[(50){}Endereco_Aluno]", "Endereco_Aluno");
        getMarcadoAluno().put("[(50){}ComplementoEnd_Aluno]", "ComplementoEnd_Aluno");
        getMarcadoAluno().put("[(50){}NumeroEnd_Aluno]", "NumeroEnd_Aluno");
        getMarcadoAluno().put("[(40){}Cidade_Aluno]", "Cidade_Aluno");
        getMarcadoAluno().put("[(40){}Estado_Aluno]", "Estado_Aluno");
        getMarcadoAluno().put("[(40){}Bairro_Aluno]", "Bairro_Aluno");
        getMarcadoAluno().put("[(15){}Sexo_Aluno]", "Sexo_Aluno");
        getMarcadoAluno().put("[(20){}Naturalidade_Aluno]", "Naturalidade_Aluno");
        getMarcadoAluno().put("[(20){}Nacionalidade_Aluno]", "Nacionalidade_Aluno");
        getMarcadoAluno().put("[(20){}EstadoCivil_Aluno]", "EstadoCivil_Aluno");
        getMarcadoAluno().put("[(20){}Rg_Aluno]", "Rg_Aluno");
        getMarcadoAluno().put("[(20){}OrgaoEmissor_Aluno]", "OrgaoEmissor_Aluno");
        getMarcadoAluno().put("[(20){}DataEmissaoRG_Aluno]", "DataEmissaoRG_Aluno");
        getMarcadoAluno().put("[(10){}EstadoEmissor_Aluno]", "EstadoEmissor_Aluno");
        getMarcadoAluno().put("[(14){}Cpf_Aluno]", "Cpf_Aluno");
        getMarcadoAluno().put("[(20){}DataNasc_Aluno]", "DataNasc_Aluno");
        getMarcadoAluno().put("[(3){}Idade_Aluno]", "Idade_Aluno");
        getMarcadoAluno().put("[(10){}Codigo_Aluno]", "Codigo_Aluno");
        getMarcadoAluno().put("[(16){}Telefone_Aluno]", "Telefone_Aluno");
        getMarcadoAluno().put("[(16){}Celular_Aluno]", "Celular_Aluno");
        getMarcadoAluno().put("[(16){}Comercial_Aluno]", "Comercial_Aluno");
        getMarcadoAluno().put("[(10){}CEP_Aluno]", "CEP_Aluno");
        getMarcadoAluno().put("[(40){}Email_Aluno]", "Email_Aluno");
        getMarcadoAluno().put("[(40){}Email2_Aluno]", "Email2_Aluno");
        getMarcadoAluno().put("[(70){}NomePai_Aluno]", "NomePai_Aluno");
        getMarcadoAluno().put("[(70){}NomeMae_Aluno]", "NomeMae_Aluno");

        getMarcadoAluno().put("[(40){}CursoFormacao_Aluno]", "CursoFormacao_Aluno");
        getMarcadoAluno().put("[(40){}EscolaridadeFormacao_Aluno]", "EscolaridadeFormacao_Aluno");
        getMarcadoAluno().put("[(40){}InstituicaoFormacao_Aluno]", "InstituicaoFormacao_Aluno");
        getMarcadoAluno().put("[(40){}CidadeFormacao_Aluno]", "CidadeFormacao_Aluno");
        getMarcadoAluno().put("[(40){}AreaConhecimentoFormacao_Aluno]", "AreaConhecimentoFormacao_Aluno");
        getMarcadoAluno().put("[(40){}SituacaoFormacao_Aluno]", "SituacaoFormacao_Aluno");
        getMarcadoAluno().put("[(40){}DataFinalFormacao_Aluno]", "DataFinalFormacao_Aluno");
        getMarcadoAluno().put("[(4){}AnoDataFinalFormacao_Aluno]", "AnoDataFinalFormacao_Aluno");

        getMarcadoAluno().put("[(50){}NomeEmpresa_Aluno]", "NomeEmpresa_Aluno");
        getMarcadoAluno().put("[(50){}EnderecoEmpresa_Aluno]", "EnderecoEmpresa_Aluno");
        getMarcadoAluno().put("[(40){}ComplementoEmpresa_Aluno]", "ComplementoEmpresa_Aluno");
        getMarcadoAluno().put("[(40){}BairroEmpresa_Aluno]", "BairroEmpresa_Aluno");
        getMarcadoAluno().put("[(10){}CepEmpresa_Aluno]", "CepEmpresa_Aluno");
        getMarcadoAluno().put("[(40){}CidadeEmpresa_Aluno]", "CidadeEmpresa_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneEmpresa_Aluno]", "TelefoneEmpresa_Aluno");

        getMarcadoAluno().put("[(50){}NomeResponsavelFinanceiro_Aluno]", "NomeResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(14){}CPFResponsavelFinanceiro_Aluno]", "CPFResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(20){}RGResponsavelFinanceiro_Aluno]", "RGResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneComerResponsavelFinanceiro_Aluno]", "TelefoneComerResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneResResponsavelFinanceiro_Aluno]", "TelefoneResResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneRecadoResponsavelFinanceiro_Aluno]", "TelefoneRecadoResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(16){}CelularResponsavelFinanceiro_Aluno]", "CelularResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(70){}EmailResponsavelFinanceiro_Aluno]", "EmailResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(15){}CepResponsavelFinanceiro_Aluno]", "CepResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(70){}EnderecoResponsavelFinanceiro_Aluno]", "EnderecoResponsavelFinanceiro_Aluno");
		getMarcadoAluno().put("[(10){}NumeroEnderecoResponsavelFinanceiro_Aluno]", "NumeroEnderecoResponsavelFinanceiro_Aluno");
		getMarcadoAluno().put("[(70){}ComplementoEnderecoResponsavelFinanceiro_Aluno]", "ComplementoEnderecoResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(50){}EstadoResponsavelFinanceiro_Aluno]", "EstadoResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(70){}CidadeResponsavelFinanceiro_Aluno]", "CidadeResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(15){}DataNascResponsavelFinanceiro_Aluno]", "DataNascResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(30){}OrgaoEmissoResponsavelFinanceiro_Aluno]", "OrgaoEmissoResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(50){}SetorResponsavelFinanceiro_Aluno]", "SetorResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(20){}NacionalidadeResponsavelFinanceiro_Aluno]", "NacionalidadeResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(20){}EstadoCivilResponsavelFinanceiro_Aluno]", "EstadoCivilResponsavelFinanceiro_Aluno");
        getMarcadoAluno().put("[(30){}GrauParentescoResponsavelFinanceiro_Aluno]", "GrauParentescoResponsavelFinanceiro_Aluno");

        getMarcadoAluno().put("[(50){}NomeResponsavelFinanceiro2_Aluno]", "NomeResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(14){}CPFResponsavelFinanceiro2_Aluno]", "CPFResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(20){}RGResponsavelFinanceiro2_Aluno]", "RGResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneComerResponsavelFinanceiro2_Aluno]", "TelefoneComerResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneResResponsavelFinanceiro2_Aluno]", "TelefoneResResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneRecadoResponsavelFinanceiro2_Aluno]", "TelefoneRecadoResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(15){}CepResponsavelFinanceiro2_Aluno]", "CepResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(16){}CelularResponsavelFinanceiro2_Aluno]", "CelularResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(70){}EmailResponsavelFinanceiro2_Aluno]", "EmailResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(15){}CepResponsavelFinanceiro2_Aluno]", "CepResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(70){}EnderecoResponsavelFinanceiro2_Aluno]", "EnderecoResponsavelFinanceiro2_Aluno");
		getMarcadoAluno().put("[(10){}NumeroEnderecoResponsavelFinanceiro2_Aluno]", "NumeroEnderecoResponsavelFinanceiro2_Aluno");
		getMarcadoAluno().put("[(70){}ComplementoEnderecoResponsavelFinanceiro2_Aluno]", "ComplementoEnderecoResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(50){}EstadoResponsavelFinanceiro2_Aluno]", "EstadoResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(70){}CidadeResponsavelFinanceiro2_Aluno]", "CidadeResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(15){}DataNascResponsavelFinanceiro2_Aluno]", "DataNascResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(30){}OrgaoEmissoResponsavelFinanceiro2_Aluno]", "OrgaoEmissoResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(50){}SetorResponsavelFinanceiro2_Aluno]", "SetorResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(20){}NacionalidadeResponsavelFinanceiro2_Aluno]", "NacionalidadeResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(20){}EstadoCivilResponsavelFinanceiro2_Aluno]", "EstadoCivilResponsavelFinanceiro2_Aluno");
        getMarcadoAluno().put("[(30){}GrauParentescoResponsavelFinanceiro2_Aluno]", "GrauParentescoResponsavelFinanceiro2_Aluno");
        
        getMarcadoAluno().put("[(70){}Justificativa_Aluno]", "Justificativa_Aluno");
        getMarcadoAluno().put("[(250){}Observacoes_Aluno]", "observacoes_Aluno");
        
        getMarcadoAluno().put("[(40){}DataNascExtenso_Aluno]", "DataNascExtenso_Aluno");
        getMarcadoAluno().put("[(80){}DataNascExtenso2_Aluno]", "DataNascExtenso2_Aluno");        
        
        getMarcadoAluno().put("[(40){}NumeroRegistroDiploma_Aluno]", "NumeroRegistroDiploma_Aluno");
        getMarcadoAluno().put("[(80){}NaturalidadeEstado_Aluno]", "NaturalidadeEstado_Aluno");
        getMarcadoAluno().put("[(80){}SemestreIngresso_Aluno]", "SemestreIngresso_Aluno");
        getMarcadoAluno().put("[(80){}AnoIngresso_Aluno]", "AnoIngresso_Aluno");
        getMarcadoAluno().put("[(80){}UltimoSemestreCursado_Aluno]", "UltimoSemestreCursado_Aluno");
        getMarcadoAluno().put("[(80){}UltimoAnoCursado_Aluno]", "UltimoAnoCursado_Aluno");
        getMarcadoAluno().put("[(){}ListaEnade_Aluno]", "ListaEnade_Aluno");
        getMarcadoAluno().put("[(80){}CoeficienteRendimentoPeriodoLetivo_Aluno]", "CoeficienteRendimentoPeriodoLetivo_Aluno");
        getMarcadoAluno().put("[(80){}CoeficienteRendimentoGeral_Aluno]", "CoeficienteRendimentoGeral_Aluno");
        getMarcadoAluno().put("[(40){}FolhaRegistroDiploma_Aluno]", "FolhaRegistroDiploma_Aluno");
        getMarcadoAluno().put("[(40){}LivroRegistroDiploma_Aluno]", "LivroRegistroDiploma_Aluno");
        getMarcadoAluno().put("[(20){}EstadoFormacao_Aluno]", "EstadoFormacao_Aluno");
        getMarcadoAluno().put("[(20){}DiaNascimento_Aluno]", "DiaNascimento_Aluno");
        getMarcadoAluno().put("[(80){}MesNascimento_Aluno]", "MesNascimento_Aluno");
        getMarcadoAluno().put("[(20){}AnoNascimento_Aluno]", "AnoNascimento_Aluno");
        getMarcadoAluno().put("[(5){}PreposicaoAouO_Aluno]", "PreposicaoAouO_Aluno");
        getMarcadoAluno().put("[(40){}DataPublicacaoDiploma_Aluno]", "DataPublicacaoDiploma_Aluno");
        getMarcadoAluno().put("[(){}ListaDemostrativoDebito_Aluno]", "ListaDemostrativoDebito_Aluno");
        getMarcadoAluno().put("[(){}ListaDemostrativoDebitoNegociado_Aluno]", "ListaDemostrativoDebitoNegociado_Aluno");
        getMarcadoAluno().put("[(40){}ValorTotalDebito_Aluno]", "ValorTotalDebito_Aluno");
        getMarcadoAluno().put("[(255){}ValorTotalDebitoExtenso_Aluno]", "ValorTotalDebitoExtenso_Aluno");
        getMarcadoAluno().put("[(40){}NumeroContratoDemostrativoDebito_Aluno]", "NumeroContratoDemostrativoDebito_Aluno");
        getMarcadoAluno().put("[(255){}PaginaPessoal_Aluno]", "PaginaPessoal_Aluno");

        getMarcadoAluno().put("[(30){}TituloEleitoral_Aluno]", "TituloEleitoral_Aluno");
        getMarcadoAluno().put("[(30){}ZonaEleitoral_Aluno]", "ZonaEleitoral_Aluno");
        getMarcadoAluno().put("[(30){}Deficiencia_Aluno]", "Deficiencia_Aluno");
        getMarcadoAluno().put("[(30){}CorRaca_Aluno]", "CorRaca_Aluno");
        
        getMarcadoAluno().put("[(80){}TipoAdvertencia_Aluno]", "TipoAdvertencia_Aluno");
        getMarcadoAluno().put("[(300){}DescricaoTipoAdvertencia_Aluno]", "DescricaoTipoAdvertencia_Aluno");
        getMarcadoAluno().put("[(300){}ObservacaoAdvertencia_Aluno]", "ObservacaoAdvertencia_Aluno");
        getMarcadoAluno().put("[(40){}DataAdvertencia_Aluno]", "DataAdvertencia_Aluno");
        getMarcadoAluno().put("[(300){}ResponsavelLegal_Aluno]", "ResponsavelLegal_Aluno");
        
        getMarcadoAluno().put("[(40){}NumeroRegistroCertificado_Aluno]", "NumeroRegistroCertificado_Aluno");
        getMarcadoAluno().put("[(40){}FolhaRegistroCertificado_Aluno]", "FolhaRegistroCertificado_Aluno");
        getMarcadoAluno().put("[(40){}LivroRegistroCertificado_Aluno]", "LivroRegistroCertificado_Aluno");
        getMarcadoAluno().put("[(40){}DataPublicacaoCertificado_Aluno]", "DataPublicacaoCertificado_Aluno");
        getMarcadoAluno().put("[(10){}ViaCertificado_Aluno]", "ViaCertificado_Aluno");
        
        getMarcadoAluno().put("[(40){}ValorTotalDebitoInicial_Aluno]", "ValorTotalDebitoInicial_Aluno");
        getMarcadoAluno().put("[(40){}ValorTotalDebitoFinal_Aluno]", "ValorTotalDebitoFinal_Aluno");
        getMarcadoAluno().put("[(40){}PercentualJuroDebito_Aluno]", "PercentualJuroDebito_Aluno");
        getMarcadoAluno().put("[(40){}ValorJuroDebito_Aluno]", "ValorJuroDebito_Aluno");
        getMarcadoAluno().put("[(40){}AcrescimoDebito_Aluno]", "AcrescimoDebito_Aluno");
        getMarcadoAluno().put("[(40){}DescontoDebito_Aluno]", "DescontoDebito_Aluno");
        getMarcadoAluno().put("[(40){}ValorEntradaDebito_Aluno]", "ValorEntradaDebito_Aluno");
        getMarcadoAluno().put("[(40){}ValorParceladoDebito_Aluno]", "ValorParceladoDebito_Aluno");
        getMarcadoAluno().put("[(40){}JustificativaDebito_Aluno]", "JustificativaDebito_Aluno");
        getMarcadoAluno().put("[(){}AnoConclusaoGraduacao_Aluno]", "AnoConclusaoGraduacao_Aluno");
        getMarcadoAluno().put("[(){}AnoConclusaoEnsinoMedio_Aluno]", "AnoConclusaoEnsinoMedio_Aluno");
        
        getMarcadoAluno().put("[(100){}FiliacaoMaeNome_Aluno]", "FiliacaoMaeNome_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoMaeCPF_Aluno]", "FiliacaoMaeCPF_Aluno");
        getMarcadoAluno().put("[(10){}FiliacaoMaeCEP_Aluno]", "FiliacaoMaeCEP_Aluno");
        getMarcadoAluno().put("[(10){}FiliacaoMaeRG_Aluno]", "FiliacaoMaeRG_Aluno");
        getMarcadoAluno().put("[(150){}FiliacaoMaeEndereco_Aluno]", "FiliacaoMaeEndereco_Aluno");
        getMarcadoAluno().put("[(20){}FiliacaoMaeNumero_Aluno]", "FiliacaoMaeNumero_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoMaeSetor_Aluno]", "FiliacaoMaeSetor_Aluno");
        getMarcadoAluno().put("[(150){}FiliacaoMaeComplementoEndereco_Aluno]", "FiliacaoMaeComplementoEndereco_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoMaeEstado_Aluno]", "FiliacaoMaeEstado_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoMaeCidade_Aluno]", "FiliacaoMaeCidade_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoMaeUF_Aluno]", "FiliacaoMaeUF_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoMaeTelefoneComercial_Aluno]", "FiliacaoMaeTelefoneComercial_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoMaeTelefoneResidencial_Aluno]", "FiliacaoMaeTelefoneResidencial_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoMaeTelefoneRecado_Aluno]", "FiliacaoMaeTelefoneRecado_Aluno");
        getMarcadoAluno().put("[(100){}FiliacaoMaeEmail_Aluno]", "FiliacaoMaeEmail_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoMaeCelular_Aluno]", "FiliacaoMaeCelular_Aluno");
        getMarcadoAluno().put("[(10){}FiliacaoMaeDataNascimento_Aluno]", "FiliacaoMaeDataNascimento_Aluno");
        getMarcadoAluno().put("[(30){}FiliacaoMaeNacionalidade_Aluno]", "FiliacaoMaeNacionalidade_Aluno");
        getMarcadoAluno().put("[(20){}FiliacaoMaeEstadoCivil_Aluno]", "FiliacaoMaeEstadoCivil_Aluno");
        getMarcadoAluno().put("[(30){}FiliacaoMaeOrgaoEmissor_Aluno]", "FiliacaoMaeOrgaoEmissor_Aluno");
        
        getMarcadoAluno().put("[(100){}FiliacaoPaiNome_Aluno]", "FiliacaoPaiNome_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoPaiCPF_Aluno]", "FiliacaoPaiCPF_Aluno");
        getMarcadoAluno().put("[(10){}FiliacaoPaiCEP_Aluno]", "FiliacaoPaiCEP_Aluno");
        getMarcadoAluno().put("[(10){}FiliacaoPaiRG_Aluno]", "FiliacaoPaiRG_Aluno");
        getMarcadoAluno().put("[(150){}FiliacaoPaiEndereco_Aluno]", "FiliacaoPaiEndereco_Aluno");
        getMarcadoAluno().put("[(20){}FiliacaoPaiNumero_Aluno]", "FiliacaoPaiNumero_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoPaiSetor_Aluno]", "FiliacaoPaiSetor_Aluno");
        getMarcadoAluno().put("[(150){}FiliacaoPaiComplementoEndereco_Aluno]", "FiliacaoPaiComplementoEndereco_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoPaiEstado_Aluno]", "FiliacaoPaiEstado_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoPaiCidade_Aluno]", "FiliacaoPaiCidade_Aluno");
        getMarcadoAluno().put("[(50){}FiliacaoPaiUF_Aluno]", "FiliacaoPaiUF_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoPaiTelefoneComercial_Aluno]", "FiliacaoPaiTelefoneComercial_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoPaiTelefoneResidencial_Aluno]", "FiliacaoPaiTelefoneResidencial_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoPaiTelefoneRecado_Aluno]", "FiliacaoPaiTelefoneRecado_Aluno");
        getMarcadoAluno().put("[(100){}FiliacaoPaiEmail_Aluno]", "FiliacaoPaiEmail_Aluno");
        getMarcadoAluno().put("[(15){}FiliacaoPaiCelular_Aluno]", "FiliacaoPaiCelular_Aluno");
        getMarcadoAluno().put("[(10){}FiliacaoPaiDataNascimento_Aluno]", "FiliacaoPaiDataNascimento_Aluno");
        getMarcadoAluno().put("[(30){}FiliacaoPaiNacionalidade_Aluno]", "FiliacaoPaiNacionalidade_Aluno");
        getMarcadoAluno().put("[(20){}FiliacaoPaiEstadoCivil_Aluno]", "FiliacaoPaiEstadoCivil_Aluno");
        getMarcadoAluno().put("[(30){}FiliacaoPaiOrgaoEmissor_Aluno]", "FiliacaoPaiOrgaoEmissor_Aluno");
        
        getMarcadoAluno().put("[(50){}NomeFiador_Aluno]", "NomeFiador_Aluno");
        getMarcadoAluno().put("[(14){}CPFFiador_Aluno]", "CPFFiador_Aluno");
        getMarcadoAluno().put("[(10){}CEPFiador_Aluno]", "CEPFiador_Aluno");
        getMarcadoAluno().put("[(100){}EnderecoFiador_Aluno]", "EnderecoFiador_Aluno");
        getMarcadoAluno().put("[(50){}BairroFiador_Aluno]", "BairroFiador_Aluno");
        getMarcadoAluno().put("[(10){}NumeroEnderecoFiador_Aluno]", "NumeroEnderecoFiador_Aluno");
        getMarcadoAluno().put("[(100){}ComplementoEnderecoFiador_Aluno]", "ComplementoEnderecoFiador_Aluno");
        getMarcadoAluno().put("[(40){}CidadeFiador_Aluno]", "CidadeFiador_Aluno");
        getMarcadoAluno().put("[(30){}EstadoFiador_Aluno]", "EstadoFiador_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneFiador_Aluno]", "TelefoneFiador_Aluno");
        getMarcadoAluno().put("[(16){}CelularFiador_Aluno]", "CelularFiador_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneComercial_Aluno]", "TelefoneComercial_Aluno");
        
        getMarcadoAluno().put("[(50){}RGFiador_Aluno]", "RGFiador_Aluno");
        getMarcadoAluno().put("[(100){}ProfissaoFiador_Aluno]", "ProfissaoFiador_Aluno");
        getMarcadoAluno().put("[(20){}EstadoCivilFiador_Aluno]", "EstadoCivilFiador_Aluno");
        getMarcadoAluno().put("[(100){}PaisFiador_Aluno]", "PaisFiador_Aluno");
        getMarcadoAluno().put("[(10){}DataNascimentoFiador_Aluno]", "DataNascimentoFiador_Aluno");
        
        getMarcadoAluno().put("[(150){}NomeParceiroConvenio_Aluno]", "NomeParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(18){}CNPJParceiroConvenio_Aluno]", "CNPJParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(150){}NomeResponsavelParceiroConvenio_Aluno]", "NomeResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(10){}RGResponsavelParceiroConvenio_Aluno]", "RGResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(14){}CPFResponsavelParceiroConvenio_Aluno]", "CPFResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(150){}EnderecoResponsavelParceiroConvenio_Aluno]", "EnderecoResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(150){}BairroResponsavelParceiroConvenio_Aluno]", "BairroResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(150){}CidadeResponsavelParceiroConvenio_Aluno]", "CidadeResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(10){}CEPResponsavelParceiroConvenio_Aluno]", "CEPResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(16){}TelefoneResponsavelParceiroConvenio_Aluno]", "TelefoneResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(150){}EmailResponsavelParceiroConvenio_Aluno]", "EmailResponsavelParceiroConvenio_Aluno");
        getMarcadoAluno().put("[(){}ListaPlanoEnsinoDisciplina_Aluno]", "ListaPlanoEnsinoDisciplina_Aluno");
        getMarcadoAluno().put("[(150){}TituloMonografia_Aluno]", "TituloMonografia_Aluno");
        getMarcadoAluno().put("[(150){}NotaMonografia_Aluno]", "NotaMonografia_Aluno");
        getMarcadoAluno().put("[(150){}TipoTrabalhoConclusaoCurso_Aluno]", "TipoTrabalhoConclusaoCurso_Aluno");
        getMarcadoAluno().put("[(150){}OrientadorMonografia_Aluno]", "OrientadorMonografia_Aluno");
        getMarcadoAluno().put("[(){}ListaDeclaracaoImpostoRenda_Aluno]", "ListaDeclaracaoImpostoRenda_Aluno");
        
        getMarcadoAluno().put("[(10){}ValorParcelaNegociacao_Aluno]", "ValorParcelaNegociacao_Aluno");
        getMarcadoAluno().put("[(100){}ValorParcelaNegociacaoExtenso_Aluno]", "ValorParcelaNegociacaoExtenso_Aluno");
        getMarcadoAluno().put("[(10){}QuantidadeParcelaNegociacao_Aluno]", "QuantidadeParcelaNegociacao_Aluno");
        getMarcadoAluno().put("[(100){}QuantidadeParcelaNegociacaoExtenso_Aluno]", "QuantidadeParcelaNegociacaoExtenso_Aluno");
        getMarcadoAluno().put("[(10){}DiaPrimeiroVencimento_Aluno]", "DiaPrimeiroVencimento_Aluno");
        getMarcadoAluno().put("[(100){}DiaPrimeiroVencimentoExtenso_Aluno]", "DiaPrimeiroVencimentoExtenso_Aluno");
        getMarcadoAluno().put("[(100){}UltimoCargoAluno_Aluno]", "UltimoCargoAluno_Aluno");
        getMarcadoAluno().put("[(100){}EmpresaAluno_Aluno]", "EmpresaAluno_Aluno");
        getMarcadoAluno().put("[(500){}CampoJustificativaNegociacao_Aluno]", "CampoJustificativaNegociacao_Aluno");
        
        getMarcadoAluno().put("[(100){}ValorParceladoDebitoExtenso_Aluno]", "ValorParceladoDebitoExtenso_Aluno");
        getMarcadoAluno().put("[(10){}ValorResidual_Aluno]", "ValorResidual_Aluno");
        getMarcadoAluno().put("[(100){}ValorResidualExtenso_Aluno]", "ValorResidualExtenso_Aluno");
        getMarcadoAluno().put("[(10){}DiaSegundoVencimento_Aluno]", "DiaSegundoVencimento_Aluno");
        getMarcadoAluno().put("[(100){}DiaSegundoVencimentoExtenso_Aluno]", "DiaSegundoVencimentoExtenso_Aluno");
        getMarcadoAluno().put("[(){}ObservacaoComplementar_Aluno]", "ObservacaoComplementar_Aluno");
        
        getMarcadoAluno().put("[(50){}NumeroRegistroDiplomaPrimeiraVia_Aluno]", "NumeroRegistroDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(50){}NumeroProcessoDiplomaPrimeiraVia_Aluno]", "NumeroProcessoDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(10){}DataExpedicaoDiplomaPrimeiraVia_Aluno]", "DataExpedicaoDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(255){}AssinaturaPrimeiroFuncionarioDiplomaPrimeiraVia_Aluno]", "AssinaturaPrimeiroFuncionarioDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(255){}CargoPrimeiroFuncionarioDiplomaPrimeiraVia_Aluno]", "CargoPrimeiroFuncionarioDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(255){}AssinaturaSegundoFuncionarioDiplomaPrimeiraVia_Aluno]", "AssinaturaSegundoFuncionarioDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(255){}CargoSegundoFuncionarioDiplomaPrimeiraVia_Aluno]", "CargoSegundoFuncionarioDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(255){}AssinaturaTerceiroFuncionarioDiplomaPrimeiraVia_Aluno]", "AssinaturaTerceiroFuncionarioDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(255){}CargoTerceiroFuncionarioDiplomaPrimeiraVia_Aluno]", "CargoTerceiroFuncionarioDiplomaPrimeiraVia_Aluno");
        getMarcadoAluno().put("[(500){}UrlFoto_Aluno]", "UrlFoto_Aluno");
        getMarcadoAluno().put("[(80){}EmailInstitucional_Aluno]", "EmailInstitucional_Aluno");
        getMarcadoAluno().put("[(70){}RegistroAcademico_Aluno]", "RegistroAcademico_Aluno");
    }

    private static void inicializarDominioMarcadoProfessor() {
        getMarcadoProfessor().put("[(70){}Nome_Professor]", "Nome_Professor");
        getMarcadoProfessor().put("[(50){}Endereco_Professor]", "Endereco_Professor");
        getMarcadoProfessor().put("[(50){}ComplementoEnd_Professor]", "ComplementoEnd_Professor");
        getMarcadoProfessor().put("[(50){}NumeroEnd_Professor]", "NumeroEnd_Professor");
        getMarcadoProfessor().put("[(40){}Cidade_Professor]", "Cidade_Professor");
        getMarcadoProfessor().put("[(40){}Estado_Professor]", "Estado_Professor");
        getMarcadoProfessor().put("[(40){}Bairro_Professor]", "Bairro_Professor");
        getMarcadoProfessor().put("[(15){}Sexo_Professor]", "Sexo_Professor");
        getMarcadoProfessor().put("[(20){}Naturalidade_Professor]", "Naturalidade_Professor");
        getMarcadoProfessor().put("[(20){}Nacionalidade_Professor]", "Nacionalidade_Professor");
        getMarcadoProfessor().put("[(20){}EstadoCivil_Professor]", "EstadoCivil_Professor");
        getMarcadoProfessor().put("[(20){}Rg_Professor]", "Rg_Professor");
        getMarcadoProfessor().put("[(20){}OrgaoEmissor_Professor]", "OrgaoEmissor_Professor");
        getMarcadoProfessor().put("[(10){}EstadoEmissor_Professor]", "EstadoEmissor_Professor");
        getMarcadoProfessor().put("[(14){}Cpf_Professor]", "Cpf_Professor");
        getMarcadoProfessor().put("[(20){}DataNasc_Professor]", "DataNasc_Professor");
        getMarcadoProfessor().put("[(10){}Codigo_Professor]", "Codigo_Professor");
        getMarcadoProfessor().put("[(16){}Telefone_Professor]", "Telefone_Professor");
        getMarcadoProfessor().put("[(16){}Celular_Professor]", "Celular_Professor");
        getMarcadoProfessor().put("[(16){}Comercial_Professor]", "Comercial_Professor");
        getMarcadoProfessor().put("[(10){}CEP_Professor]", "CEP_Professor");
        getMarcadoProfessor().put("[(40){}Email_Professor]", "Email_Professor");
        getMarcadoProfessor().put("[(40){}Email2_Professor]", "Email2_Professor");
        getMarcadoProfessor().put("[(70){}NomePai_Professor]", "NomePai_Professor");
        getMarcadoProfessor().put("[(70){}NomeMae_Professor]", "NomeMae_Professor");
        
        getMarcadoAluno().put("[(50){}NomeEmpresa_Professor]", "NomeEmpresa_Professor");
        getMarcadoAluno().put("[(50){}EnderecoEmpresa_Professor]", "EnderecoEmpresa_Professor");
        getMarcadoAluno().put("[(40){}ComplementoEmpresa_Professor]", "ComplementoEmpresa_Professor");
        getMarcadoAluno().put("[(40){}BairroEmpresa_Professor]", "BairroEmpresa_Professor");
        getMarcadoAluno().put("[(10){}CepEmpresa_Professor]", "CepEmpresa_Professor");
        getMarcadoAluno().put("[(40){}CidadeEmpresa_Professor]", "CidadeEmpresa_Professor");
        getMarcadoAluno().put("[(16){}TelefoneEmpresa_Professor]", "TelefoneEmpresa_Professor");

        getMarcadoProfessor().put("[(40){}CursoFormacao_Professor]", "CursoFormacao_Professor");
        getMarcadoProfessor().put("[(40){}EscolaridadeFormacao_Professor]", "EscolaridadeFormacao_Professor");
        getMarcadoProfessor().put("[(40){}InstituicaoFormacao_Professor]", "InstituicaoFormacao_Professor");
        getMarcadoProfessor().put("[(40){}CidadeFormacao_Professor]", "CidadeFormacao_Professor");
        getMarcadoProfessor().put("[(40){}AreaConhecimentoFormacao_Professor]", "AreaConhecimentoFormacao_Professor");
        getMarcadoProfessor().put("[(40){}SituacaoFormacao_Professor]", "SituacaoFormacao_Professor");
        getMarcadoProfessor().put("[(40){}DataFinalFormacao_Professor]", "DataFinalFormacao_Professor");
        getMarcadoAluno().put("[(){}Lista_AulasMinistradasProfessor]", "Lista_AulasMinistradasProfessor");
        getMarcadoAluno().put("[(){}Lista_AulasMinistradasProfessorComCargaHoraria]", "Lista_AulasMinistradasProfessorComCargaHoraria");
    }

    private static void inicializarDominioMarcadorAlunoBancoCurriculum() {
        getMarcadorAlunoBancoCurriculum().put("#Nome_Aluno", "Nome_Aluno");
    }

    private static void inicializarDominioMarcadorEmpresaBancoCurriculum() {
        getMarcadorEmpresaBancoCurriculum().put("#Nome_Empresa", "Nome_Empresa");
    }

    private static void inicializarDominioMarcadorVagaBancoCurriculum() {
        getMarcadorVagaBancoCurriculum().put("#Codigo_Vaga", "Codigo_Vaga");
        getMarcadorVagaBancoCurriculum().put("#Cargo_Vaga", "Cargo_Vaga");
        getMarcadorVagaBancoCurriculum().put("#AreaProfissional_Vaga", "AreaProfissional_Vaga");
        getMarcadorVagaBancoCurriculum().put("#Local_Vaga", "Local_Vaga");
        getMarcadorVagaBancoCurriculum().put("#Horario_Vaga", "Horario_Vaga");
    }

    private static void inicializarDominioMarcadorTituloBancoCurriculum() {
        getMarcadorTituloBancoCurriculum().put("#Titulo_BancoCurriculum", "Titulo_BancoCurriculum");
    }

    private static void inicializarDominioMarcadoContaReceber() {
        getMarcadoContaReceber().put("[(10){}Codigo_ContaReceber]", "Codigo_ContaReceber");
        // getMarcadoContaReceber().put("[(50){}NrDocumento_ContaReceber]",
        // "NrDocumento_ContaReceber");
        getMarcadoContaReceber().put("[(8){}Parcela_ContaReceber]", "Parcela_ContaReceber");
        
        getMarcadoContaReceber().put("[(50){}DataVcto_ContaReceber]", "DataVcto_ContaReceber");
        
        getMarcadoContaReceber().put("[(250){}DescricaoPagamento_ContaReceber]", "DescricaoPagamento_ContaReceber");
        // getMarcadoContaReceber().put("[(50){}TipoOrigem_ContaReceber]",
        // "TipoOrigem_ContaReceber");
        getMarcadoContaReceber().put("[(15){}Valor_ContaReceber]", "Valor_ContaReceber");
        getMarcadoContaReceber().put("[(100){}ValorDivididoPorDois_ContaReceber]", "ValorDivididoPorDois_ContaReceber");
        getMarcadoContaReceber().put("[(100){}ValorDivididoPorTres_ContaReceber]", "ValorDivididoPorTres_ContaReceber");
        getMarcadoContaReceber().put("[(15){}ValorDesconto_ContaReceber]", "ValorDesconto_ContaReceber");
        getMarcadoContaReceber().put("[(15){}DescontoInstituicao_ContaReceber]", "DescontoInstituicao_ContaReceber");
        getMarcadoContaReceber().put("[(15){}DescontoProgressivo_ContaReceber]", "DescontoProgressivo_ContaReceber");
        getMarcadoContaReceber().put("[(15){}DescontoConvenio_ContaReceber]", "DescontoConvenio_ContaReceber");
        getMarcadoContaReceber().put("[(15){}Total_ContaReceber]", "Total_ContaReceber");
        getMarcadoContaReceber().put("[(15){}TotalDesconto_ContaReceber]", "TotalDesconto_ContaReceber");
        getMarcadoContaReceber().put("[(15){}Parcela_InclusaoReposicao]", "Parcela_InclusaoReposicao");
        getMarcadoContaReceber().put("[(15){}ValorCusteado_ContaReceber]", "ValorCusteado_ContaReceber");
        getMarcadoContaReceber().put("[(15){}ValorBaseContareceber_ContaReceber]", "ValorBaseContareceber_ContaReceber");
        getMarcadoContaReceber().put("[(15){}ValorContareceberMenosDecontoInstituicao_ContaReceber]", "ValorContareceberMenosDecontoInstituicao_ContaReceber");
        getMarcadoContaReceber().put("[(15){}ValorTotalParcela_InclusaoReposicao]", "ValorTotalParcela_InclusaoReposicao");
        getMarcadoContaReceber().put("[(15){}ValorTotal_InclusaoReposicao]", "ValorTotal_InclusaoReposicao");
        getMarcadoContaReceber().put("[(15){}ValorBase_ContaReceber]", "ValorBase_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorJuro_ContaReceber]", "ValorJuro_ContaReceber");	
//        getMarcadoContaReceber().put("[(15){}ValorDesconto_ContaReceber]", "ValorDesconto_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorAcrescimo_ContaReceber]", "ValorAcrescimo_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorTotal_ContaReceber]", "ValorTotal_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorBaseContaReceber_ContaReceber]", "ValorBaseContaReceber_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorJuroContaReceber_ContaReceber]", "ValorJuroContaReceber_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorMultaContaReceber_ContaReceber]", "ValorMultaContaReceber_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorAcrescimoContaReceber_ContaReceber]", "ValorAcrescimoContaReceber_ContaReceber");	
        getMarcadoContaReceber().put("[(15){}ValorDescontoContaReceber_ContaReceber]", "ValorDescontoContaReceber_ContaReceber");
        getMarcadoContaReceber().put("[(60){}ResponsavelNegociacao_ContaReceber]", "ResponsavelNegociacao_ContaReceber");
    }

    private static void inicializarDominioMarcadoUnidadeEnsino() {
        getMarcadoUnidadeEnsino().put("[(10){}Codigo_UnidadeEnsino]", "Codigo_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(70){}Descricao_UnidadeEnsino]", "Descricao_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(14){}Fax_UnidadeEnsino]", "Fax_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(50){}Site_UnidadeEnsino]", "Site_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(50){}Email_UnidadeEnsino]", "Email_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(14){}TelComercial1_UnidadeEnsino]", "TelComercial1_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(14){}TelComercial2_UnidadeEnsino]", "TelComercial2_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(14){}TelComercial3_UnidadeEnsino]", "TelComercial3_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(20){}InscEstadual_UnidadeEnsino]", "InscEstadual_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(18){}Cnpj_UnidadeEnsino]", "Cnpj_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(10){}Cep_UnidadeEnsino]", "Cep_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(50){}Complemento_UnidadeEnsino]", "Complemento_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(5){}Numero_UnidadeEnsino]", "Numero_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(20){}Bairro_UnidadeEnsino]", "Bairro_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(50){}Endereco_UnidadeEnsino]", "Endereco_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(70){}RazaoSocial_UnidadeEnsino]", "RazaoSocial_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(50){}Cidade_UnidadeEnsino]", "Cidade_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(20){}Estado_UnidadeEnsino]", "Estado_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(70){}Descricao_UnidadeEnsinoOrigem]", "Descricao_UnidadeEnsinoOrigem");
        getMarcadoUnidadeEnsino().put("[(70){}Descricao_UnidadeEnsinoDestino]", "Descricao_UnidadeEnsinoDestino");
        getMarcadoUnidadeEnsino().put("[(255){}Credenciamento_UnidadeEnsino]", "Credenciamento_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(255){}LogoPadrao_UnidadeEnsino]", "LogoPadrao_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(255){}NomeExpedicaoDiploma_UnidadeEnsino]", "NomeExpedicaoDiploma_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(10){}CodigoIES_UnidadeEnsino]", "codigoIES_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(255){}NomeMantenedora_UnidadeEnsino]", "nomeMantenedora_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(10){}CodigoIesMantenedora_UnidadeEnsino]", "codigoIesMantenedora_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(25){}CnpjMantenedora_UnidadeEnsino]", "cnpjMantenedora_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(255){}NomeUnidadeCertificadora_UnidadeEnsino]", "nomeUnidadeCertificadora_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(10){}CodigoIesUnidadeCertificadora_UnidadeEnsino]", "codigoIesUnidadeCertificadora_UnidadeEnsino");
        getMarcadoUnidadeEnsino().put("[(25){}CnpjUnidadeCertificadora_UnidadeEnsino]", "cnpjUnidadeCertificadora_UnidadeEnsino");
    }
    
    private static void inicializarDominioMarcadoEstagio() {
        getMarcadoEstagio().put("[(4){}Ano_Estagio]", "Ano_Estagio");
        getMarcadoEstagio().put("[(2){}Semestre_Estagio]", "Semestre_Estagio");
        getMarcadoEstagio().put("[(3){}CargaHoraria_Estagio]", "CargaHoraria_Estagio");
        getMarcadoEstagio().put("[(3){}CargaHorariaDiaria_Estagio]", "CargaHorariaDiaria_Estagio");
        getMarcadoEstagio().put("[(3){}CargaHorariaSemanal_Estagio]", "CargaHorariaSemanal_Estagio");

        getMarcadoEstagio().put("[(5){}NrAcordoConvenio_Estagio]", "NrAcordoConvenio_Estagio");
        getMarcadoEstagio().put("[(100){}RegistroConselho_Estagio]", "RegistroConselho_Estagio");
        getMarcadoEstagio().put("[(100){}OrgaoRegistroConselho_Estagio]", "OrgaoRegistroConselho_Estagio");
        getMarcadoEstagio().put("[(100){}NomeEmpresa_Estagio]", "NomeEmpresa_Estagio");
        getMarcadoEstagio().put("[(100){}RazaoSocialEmpresa_Estagio]", "RazaoSocialEmpresa_Estagio");
        getMarcadoEstagio().put("[(18){}CNPJEmpresa_Estagio]", "CNPJEmpresa_Estagio");
        getMarcadoEstagio().put("[(14){}CPFEmpresa_Estagio]", "CPFEmpresa_Estagio");
        getMarcadoEstagio().put("[(150){}EnderecoEmpresa_Estagio]", "EnderecoEmpresa_Estagio");
        getMarcadoEstagio().put("[(50){}CidadeEmpresa_Estagio]", "CidadeEmpresa_Estagio");
        getMarcadoEstagio().put("[(2){}EstadoEmpresa_Estagio]", "EstadoEmpresa_Estagio");
        getMarcadoEstagio().put("[(14){}TelefoneEmpresa_Estagio]", "TelefoneEmpresa_Estagio");
        getMarcadoEstagio().put("[(50){}NomeRepresentanteEmpresa_Estagio]", "NomeRepresentanteEmpresa_Estagio");
        getMarcadoEstagio().put("[(14){}CPFRepresentanteEmpresa_Estagio]", "CPFRepresentanteEmpresa_Estagio");
        getMarcadoEstagio().put("[(20){}RGRepresentanteEmpresa_Estagio]", "RGRepresentanteEmpresa_Estagio");
        getMarcadoEstagio().put("[(14){}TelefoneRepresentanteEmpresa_Estagio]", "TelefoneRepresentanteEmpresa_Estagio");
        getMarcadoEstagio().put("[(150){}EnderecoRepresentanteEmpresa_Estagio]", "EnderecoRepresentanteEmpresa_Estagio");
        getMarcadoEstagio().put("[(50){}CidadeRepresentanteEmpresa_Estagio]", "CidadeRepresentanteEmpresa_Estagio");
        getMarcadoEstagio().put("[(2){}EstadoRepresentanteEmpresa_Estagio]", "EstadoRepresentanteEmpresa_Estagio");

        getMarcadoEstagio().put("[(50){}NomeSupervisorEmpresa_Estagio]", "NomeSupervisorEmpresa_Estagio");
        getMarcadoEstagio().put("[(14){}TelefoneSupervisorEmpresa_Estagio]", "TelefoneSupervisorEmpresa_Estagio");
//        getMarcadoEstagio().put("[(14){}CelularSupervisorEmpresa_Estagio]", "CelularSupervisorEmpresa_Estagio");
        getMarcadoEstagio().put("[(20){}EmailSupervisorEmpresa_Estagio]", "EmailSupervisorEmpresa_Estagio");

        getMarcadoEstagio().put("[(200){}FormacaoAcademicaSupervisorEstagio_Estagio]", "FormacaoAcademicaSupervisorEstagio_Estagio");
        getMarcadoEstagio().put("[(20){}RegistroConselhoSupervisorEstagio_Estagio]", "RegistroConselhoSupervisorEstagio_Estagio");
        getMarcadoEstagio().put("[(50){}OrgaoRegistroConselhoSupervisorEstagio_Estagio]", "OrgaoRegistroConselhoSupervisorEstagio_Estagio");
        getMarcadoEstagio().put("[(10){}DataInicioRenovacaoVigencia_Estagio]", "DataInicioRenovacaoVigencia_Estagio");
        getMarcadoEstagio().put("[(10){}DataFinalRenovacaoVigencia_Estagio]", "DataFinalRenovacaoVigencia_Estagio");

        getMarcadoEstagio().put("[(10){}DataInicioVigenciaConvenio_Estagio]", "DataInicioVigenciaConvenio_Estagio");
        getMarcadoEstagio().put("[(10){}DataFinalVigenciaConvenio_Estagio]", "DataFinalVigenciaConvenio_Estagio");
        getMarcadoEstagio().put("[(15){}ValorBolsaEstagioAnterior_Estagio]", "ValorBolsaEstagioAnterior_Estagio");
        
        getMarcadoEstagio().put("[(3){}HorasObrigatorias_Estagio]", "HorasObrigatorias_Estagio");
        getMarcadoEstagio().put("[(3){}HorasCumpridas_Estagio]", "HorasCumpridas_Estagio");
        getMarcadoEstagio().put("[(3){}HorasRestantes_Estagio]", "HorasRestantes_Estagio");
        getMarcadoEstagio().put("[(20){}Tipo_Estagio]", "Tipo_Estagio");
        getMarcadoEstagio().put("[(10){}DataCadastro_Estagio]", "DataCadastro_Estagio");

        getMarcadoEstagio().put("[(100){}NomeSeguradora_Estagio]", "NomeSeguradora_Estagio");
        getMarcadoEstagio().put("[(18){}CNPJSeguradora_Estagio]", "CNPJSeguradora_Estagio");
        getMarcadoEstagio().put("[(14){}TelefoneSeguradora_Estagio]", "TelefoneSeguradora_Estagio");
        getMarcadoEstagio().put("[(50){}ApoliceSeguradora_Estagio]", "ApoliceSeguradora_Estagio");

        getMarcadoEstagio().put("[(10){}DataInicioVigencia_Estagio]", "DataInicioVigencia_Estagio");
        getMarcadoEstagio().put("[(10){}DataFinalVigencia_Estagio]", "DataFinalVigencia_Estagio");
        getMarcadoEstagio().put("[(10){}DataAssinatura_Estagio]", "DataAssinatura_Estagio");
        getMarcadoEstagio().put("[(20){}DataAssinaturaExtenso_Estagio]", "DataAssinaturaExtenso_Estagio");
        getMarcadoEstagio().put("[(80){}DataAssinaturaExtenso2_Estagio]", "DataAssinaturaExtenso2_Estagio");        
        getMarcadoEstagio().put("[(15){}ValorBolsaEstagio_Estagio]", "ValorBolsaEstagio_Estagio");
        getMarcadoEstagio().put("[(15){}AuxilioTransporte_Estagio]", "AuxilioTransporte_Estagio");

        getMarcadoEstagio().put("[(200){}LocalCampoEstagio_Estagio]", "LocalCampoEstagio_Estagio");

        getMarcadoEstagio().put("[(100){}NomeProfessorResponsavel_Estagio]", "NomeProfessorResponsavel_Estagio");
        getMarcadoEstagio().put("[(100){}AreaProfissional_Estagio]", "AreaProfissional_Estagio");
        getMarcadoEstagio().put("[(200){}PrincipalAtividade_Estagio]", "PrincipalAtividade_Estagio");
        getMarcadoEstagio().put("[(200){}AreaAtuacao_Estagio]", "AreaAtuacao_Estagio");
        
        getMarcadoEstagio().put("[(10){}CodigoDisciplina_Estagio]", "CodigoDisciplina_Estagio");
        getMarcadoEstagio().put("[(60){}NomeDisciplina_Estagio]", "NomeDisciplina_Estagio");

        getMarcadoEstagio().put("[(20){}ValorOutraContraprestacao_Estagio]", "ValorOutraContraprestacao_Estagio");
        getMarcadoEstagio().put("[(20){}ValorAuxilioTransporte_Estagio]", "ValorAuxilioTransporte_Estagio");
        getMarcadoEstagio().put("[(6){}MarcadorValorBolsaEstagio_Estagio]", "MarcadorValorBolsaEstagio_Estagio");
        getMarcadoEstagio().put("[(6){}MarcadorValorOutrasContraprestacao_Estagio]", "MarcadorValorOutrasContraprestacao_Estagio");
        getMarcadoEstagio().put("[(6){}MarcadorValorAuxilioTransporte_Estagio]", "MarcadorValorAuxilioTransporte_Estagio");
        
        getMarcadoEstagio().put("[(6){}MarcadorEmpregadoSeletista_Estagio]", "MarcadorEmpregadoSeletista_Estagio");
        getMarcadoEstagio().put("[(6){}MarcadorEstatutario_Estagio]", "MarcadorEstatutario_Estagio");
        getMarcadoEstagio().put("[(6){}MarcadorSocioOuProprietarioEmpresarioIndividual_Estagio]", "MarcadorSocioOuProprietarioEmpresarioIndividual_Estagio");
        getMarcadoEstagio().put("[(6){}MarcadorTrabalhoAutonomo_Estagio]", "MarcadorTrabalhoAutonomo_Estagio");
        
        getMarcadoEstagio().put("[(10){}DataInicioEmpresa_Estagio]", "DataInicioEmpresa_Estagio");
        getMarcadoEstagio().put("[(10){}DataTerminoEmpresa_Estagio]", "DataTerminoEmpresa_Estagio");
        getMarcadoEstagio().put("[(255){}DepartamentoOrgao_Estagio]", "DepartamentoOrgao_Estagio");
        getMarcadoEstagio().put("[(255){}CargoFuncao_Estagio]", "CargoFuncao_Estagio");
        getMarcadoEstagio().put("[(3){}JornadaSemanalTrabalho_Estagio]", "JornadaSemanalTrabalho_Estagio");
        getMarcadoEstagio().put("[(100){}ResponsavelLegalAluno_Estagio]", "ResponsavelLegalAluno_Estagio");
        getMarcadoEstagio().put("[(30){}GrauParentescoResponsavelLegalAluno_Estagio]", "GrauParentescoResponsavelLegalAluno_Estagio");
        getMarcadoEstagio().put("[(15){}RgResponsavelLegalAluno_Estagio]", "RgResponsavelLegalAluno_Estagio");
        getMarcadoEstagio().put("[(15){}CpfResponsavelLegalAluno_Estagio]", "CpfResponsavelLegalAluno_Estagio");
        getMarcadoEstagio().put("[(100){}Disciplina_Estagio]", "Disciplina_Estagio");
        
        getMarcadoEstagio().put("[(30){}inscricaoEstadualEmpresa_Estagio]", "inscricaoEstadualEmpresa_Estagio");
        getMarcadoEstagio().put("[(50){}setorEmpresa_Estagio]", "setorEmpresa_Estagio");
        getMarcadoEstagio().put("[(15){}cepEmpresa_Estagio]", "cepEmpresa_Estagio");
        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "nomeCargoContatoParceiroEmpresa_Estagio");

        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "nomeBeneficiario_Estagio");
        getMarcadoEstagio().put("[(20){}nomeCargoContatoParceiroEmpresa_Estagio]", "rgBeneficiario_Estagio");
        getMarcadoEstagio().put("[(20){}nomeCargoContatoParceiroEmpresa_Estagio]", "cpfBeneficiario_Estagio");
        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "enderecoBeneficiario_Estagio");
        getMarcadoEstagio().put("[(10){}nomeCargoContatoParceiroEmpresa_Estagio]", "numeroBeneficiario_Estagio");
        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "cidadeBeneficiario_Estagio");
        getMarcadoEstagio().put("[(10){}nomeCargoContatoParceiroEmpresa_Estagio]", "estadoBeneficiario_Estagio");
        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "complementoBeneficiario_Estagio");
        getMarcadoEstagio().put("[(10){}nomeCargoContatoParceiroEmpresa_Estagio]", "cepBeneficiario_Estagio");
        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "setorBeneficiario_Estagio");
        getMarcadoEstagio().put("[(100){}nomeCargoContatoParceiroEmpresa_Estagio]", "emailBeneficiario_Estagio");
        getMarcadoEstagio().put("[(40){}nomeCargoContatoParceiroEmpresa_Estagio]", "telefoneBeneficiario_Estagio");
        getMarcadoEstagio().put("[(100){}nomeSupervisor_Estagio]", "nomeSupervisor_Estagio");
        getMarcadoEstagio().put("[(20){}cpfSupervisor_Estagio]", "cpfSupervisor_Estagio");
        getMarcadoEstagio().put("[(100){}codigoEscolaMEC_Estagio]", "codigoEscolaMEC_Estagio");
        
    }
    
    private static void inicializarDominioMarcadoInscProcSeletivo() {
    	getMarcadoInscProcSeletivo().put("[(10){}NumInscCandidado_InscProcSeletivo]", "NumInscCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(100){}NomeCandidado_InscProcSeletivo]", "NomeCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(15){}CpfCandidado_InscProcSeletivo]", "CpfCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(100){}CursoCandidado_InscProcSeletivo]", "CursoCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(60){}TurnoCandidado_InscProcSeletivo]", "TurnoCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(10){}ClassificacaoCandidado_InscProcSeletivo]", "ClassificacaoCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(10){}PontuacaoCandidado_InscProcSeletivo]", "PontuacaoCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(20){}SituacaoResultadoCandidado_InscProcSeletivo]", "SituacaoResultadoCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(100){}UnidadeEnsinoCandidado_InscProcSeletivo]", "UnidadeEnsinoCandidado_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(10){}Data_InscProcSeletivo]", "Data_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(10){}DataProva_InscProcSeletivo]", "DataProva_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(10){}DataResultadoCandidato_InscProcSeletivo]", "DataResultadoCandidato_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(4){}Ano_InscProcSeletivo]", "Ano_InscProcSeletivo");
    	getMarcadoInscProcSeletivo().put("[(2){}Semestre_InscProcSeletivo]", "Semestre_InscProcSeletivo");    
    }

    private static void inicializarDominioMarcadoDisciplinaDeclaracao() {
        getMarcadoDisciplinaDeclaracao().put("[(10){}Codigo_Disciplina]", "Codigo_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(250){}Nome_Disciplina]", "Nome_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(250){}Abreviatura_Disciplina]", "Abreviatura_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(80){}Nome_DisciplinaDeclaracao]", "Nome_DisciplinaDeclaracao");
        getMarcadoDisciplinaDeclaracao().put("[(5){}Frequencia_Disciplina]", "Frequencia_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(2){}DiaInicio_Disciplina]", "DiaInicio_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(2){}DiaFim_Disciplina]", "DiaFim_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(14){}Mes_Disciplina]", "Mes_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(1){}Semestre_Disciplina]", "Semestre_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(1){}Nota_Disciplina]", "Nota_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(30){}Situacao_Disciplina]", "Situacao_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(4){}Ano_Disciplina]", "Ano_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(10){}CargaHoraria_Disciplina]", "CargaHoraria_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasCursadasOuMinistradas_Disciplina]", "ListaDisciplinasCursadasOuMinistradas_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(10){}CargaHorariaDisciplinasCursadasOuMinistradas_Disciplina]", "CargaHorariaDisciplinasCursadasOuMinistradas_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasAprovadasPeriodoLetivo_Disciplina]", "ListaDisciplinasAprovadasPeriodoLetivo_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasHistoricoPeriodoLetivo_Disciplina]", "ListaDisciplinasHistoricoPeriodoLetivo_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasHistoricoDiploma_Disciplina]", "ListaDisciplinasHistoricoDiploma_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasHistoricoCertificado_Disciplina]", "ListaDisciplinasHistoricoCertificado_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinaCertificadoEnsinoMedio_Disciplina]", "ListaDisciplinaCertificadoEnsinoMedio_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(10000){}Conteudo_PlanoEnsino_Disciplina]", "Conteudo_PlanoEnsino_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina]", "ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasHistoricoPeriodo_Disciplina]", "ListaDisciplinasHistoricoPeriodo_Disciplina");
        getMarcadoDisciplinaDeclaracao().put("[(){}ListaDisciplinasVersoDiploma_Disciplina]", "ListaDisciplinasVersoDiploma_Disciplina");
    }

    private static void inicializarDominioMarcadoOutras() {
        getMarcadoOutras().put("[(10){}DataAtual_Outras]", "DataAtual_Outras");
        getMarcadoOutras().put("[(60){}DataAtualExtenso_Outras]", "DataAtualExtenso_Outras");
        getMarcadoOutras().put("[(80){}DataAtualExtenso2_Outras]", "DataAtualExtenso2_Outras");        
        getMarcadoOutras().put("[(50){}UsuarioLogado_Outras]", "UsuarioLogado_Outras");
        getMarcadoOutras().put("[(350){}Observacao_Outras]", "Observacao_Outras");
        getMarcadoOutras().put("[(255){}Assinatura_PrimeiroFuncionario]", "Assinatura_PrimeiroFuncionario");
        getMarcadoOutras().put("[(255){}Cargo_PrimeiroFuncionario]", "Cargo_PrimeiroFuncionario");
        getMarcadoOutras().put("[(255){}Assinatura_SegundoFuncionario]", "Assinatura_SegundoFuncionario");
        getMarcadoOutras().put("[(255){}Cargo_SegundoFuncionario]", "Cargo_SegundoFuncionario");
        getMarcadoOutras().put("[(40){}DataAtualExtensoSemCidade_Outras]", "DataAtualExtensoSemCidade_Outras");
        getMarcadoOutras().put("[(40){}DataAtualExtensoSemCidade2_Outras]", "DataAtualExtensoSemCidade2_Outras");        
        getMarcadoOutras().put("[(250){}Descricao_AreaProfissional]", "Descricao_AreaProfissional");
        getMarcadoOutras().put("[(600){}TituloTcc_Outras]", "TituloTcc_Outras");
        getMarcadoOutras().put("[(255){}AssinaturaTerceiroFuncionario_Outras]", "AssinaturaTerceiroFuncionario_Outras");
        getMarcadoOutras().put("[(255){}CargoTerceiroFuncionario_Outras]", "CargoTerceiroFuncionario_Outras");
    }

    private static void inicializarDominioMarcadoMatricula() {
        getMarcadoMatricula().put("[(20){}Matricula_Matricula]", "Matricula_Matricula");
        getMarcadoMatricula().put("[(10){}Data_Matricula]", "Data_Matricula");
        getMarcadoMatricula().put("[(10){}AnoSemestrePrevisaoTerminoCurso_Matricula]", "AnoSemestrePrevisaoTerminoCurso_Matricula");
        getMarcadoMatricula().put("[(10){}DataMatriculaPeriodo_Matricula]", "DataMatriculaPeriodo_Matricula");
        getMarcadoMatricula().put("[(10){}AnoSemestrePrevisaoTerminoCurso_Matricula]", "AnoSemestrePrevisaoTerminoCurso_Matricula");
        getMarcadoMatricula().put("[(50){}DataMatriculaPeriodoExtenso_Matricula]", "DataMatriculaPeriodoExtenso_Matricula");
        getMarcadoMatricula().put("[(80){}DataMatriculaPeriodoExtenso2_Matricula]", "DataMatriculaPeriodoExtenso2_Matricula");        
        getMarcadoMatricula().put("[(60){}DataExtenso_Matricula]", "DataExtenso_Matricula");
        getMarcadoMatricula().put("[(80){}DataExtenso2_Matricula]", "DataExtenso2_Matricula");        
        getMarcadoMatricula().put("[(60){}DataAtualExtenso_Matricula]", "DataAtualExtenso_Matricula");
        getMarcadoMatricula().put("[(80){}DataAtualExtenso2_Matricula]", "DataAtualExtenso2_Matricula");
        getMarcadoMatricula().put("[(40){}Turno_Matricula]", "Turno_Matricula");
        getMarcadoMatricula().put("[(100){}TurnoDescricaoContrato_Matricula]", "TurnoDescricaoContrato_Matricula");
        getMarcadoMatricula().put("[(10){}TurnoHoraInicio_Matricula]", "TurnoHoraInicio_Matricula");
        getMarcadoMatricula().put("[(10){}TurnoHoraFinal_Matricula]", "TurnoHoraFinal_Matricula");
        getMarcadoMatricula().put("[(20){}Turma_Matricula]", "Turma_Matricula");
        getMarcadoMatricula().put("[(50){}TurmaObservacao_Matricula]", "TurmaObservacao_Matricula");
        getMarcadoMatricula().put("[(10){}FinanciamentoEstudantil_Matricula]", "FinanciamentoEstudantil_Matricula");
        getMarcadoMatricula().put("[(10){}NrPeriodoLetivo_Matricula]", "NrPeriodoLetivo_Matricula");
        getMarcadoMatricula().put("[(30){}HorarioTurno_Matricula]", "HorarioTurno_Matricula");
        getMarcadoMatricula().put("[(20){}DataColacaoGrau_Matricula]", "DataColacaoGrau_Matricula");
        getMarcadoMatricula().put("[(4){}AnoTrancamento_Matricula]", "AnoTrancamento_Matricula");
        getMarcadoMatricula().put("[(15){}SemestreTrancamento_Matricula]", "SemestreTrancamento_Matricula");
        getMarcadoMatricula().put("[(20){}Matricula_MatriculaOrigem]", "Matricula_MatriculaOrigem");
        getMarcadoMatricula().put("[(20){}Matricula_MatriculaDestino]", "Matricula_MatriculaDestino");
        getMarcadoMatricula().put("[(20){}TurmaOrigem_Matricula]", "TurmaOrigem_Matricula");
        getMarcadoMatricula().put("[(20){}TurmaDestino_Matricula]", "TurmaDestino_Matricula");
        getMarcadoMatricula().put("[(10){}Turno_MatriculaOrigem]", "Turno_MatriculaOrigem");
        getMarcadoMatricula().put("[(10){}Turno_MatriculaDestino]", "Turno_MatriculaDestino");
        getMarcadoMatricula().put("[(80){}NrOrdinarioPeriodoLetivo_Matricula]", "NrOrdinarioPeriodoLetivo_Matricula");
        getMarcadoMatricula().put("[(80){}DataExtensoColacaoGrau_Matricula]", "DataExtensoColacaoGrau_Matricula");
        getMarcadoMatricula().put("[(80){}DataExtensoColacaoGrau2_Matricula]", "DataExtensoColacaoGrau2_Matricula");        
        getMarcadoMatricula().put("[(300){}ObservacaoDiploma_Matricula]", "ObservacaoDiploma_Matricula");       
        getMarcadoMatricula().put("[(30){}Situacao_Matricula]", "Situacao_Matricula");        
        getMarcadoMatricula().put("[(30){}Situacao_MatriculaPeriodo]", "SituacaoMatriculaPeriodo_Matricula");		
        getMarcadoMatricula().put("[(30){}Situacao_Matricula]", "Situacao_Matricula");        
        getMarcadoMatricula().put("[(30){}Situacao_MatriculaPeriodo]", "SituacaoMatriculaPeriodo_Matricula");
        getMarcadoMatricula().put("[(80){}SituacaoFinalPeriodoLetivo_Matricula]", "SituacaoFinalPeriodoLetivo_Matricula");
        getMarcadoMatricula().put("[(10){}DataRetornoTrancamento_Matricula]", "DataRetornoTrancamento_Matricula");
        getMarcadoMatricula().put("[(50){}DataRetornoTrancamentoExtenso_Matricula]", "DataRetornoTrancamentoExtenso_Matricula");
        getMarcadoMatricula().put("[(80){}DataRetornoTrancamentoExtenso2_Matricula]", "DataRetornoTrancamentoExtenso2_Matricula");        
        getMarcadoMatricula().put("[(100){}Responsavel_Matricula]", "Responsavel_Matricula");                
        getMarcadoMatricula().put("[(100){}Consultor_Matricula]", "Consultor_Matricula");                
        getMarcadoMatricula().put("[(30){}RgConsultor_Matricula]", "RgConsultor_Matricula");                		
        getMarcadoMatricula().put("[(5000){}TabelaAcompanhamentoFinanceiro_Matricula]", "TabelaAcompanhamentoFinanceiro_Matricula");
        getMarcadoMatricula().put("[(20){}DataAceitouTermoContratoRenovacaoOnline_Matricula]", "DataAceitouTermoContratoRenovacaoOnline_Matricula");
        getMarcadoMatricula().put("[(20){}NumeroProcessoExpedicaoDiploma_Matricula]", "NumeroProcessoExpedicaoDiploma_Matricula");
        getMarcadoMatricula().put("[(10){}DataExpedicaoDiploma_Matricula]", "DataExpedicaoDiploma_Matricula");
        getMarcadoMatricula().put("[(10){}SemestreConclusaoCurso_Matricula]", "SemestreConclusaoCurso_Matricula");
        getMarcadoMatricula().put("[(50){}DataAceitouTermoContratoRenovacaoOnlineExtenso_Matricula]", "DataAceitouTermoContratoRenovacaoOnlineExtenso_Matricula");
        getMarcadoMatricula().put("[(20){}CodigoIntegracaoFinanceiroMatricula_Matricula]", "CodigoIntegracaoFinanceiroMatricula_Matricula");
        getMarcadoMatricula().put("[(10){}MediaGlobal_Matricula]", "MediaGlobal_Matricula");
        getMarcadoMatricula().put("[(50){}SerialExpedicaoDiploma_Matricula]", "SerialExpedicaoDiploma_Matricula");
        getMarcadoMatricula().put("[(2){}Nr_Periodo_Letivo_Ingresso_Matricula]", "Nr_Periodo_Letivo_Ingresso_Matricula");
        getMarcadoMatricula().put("[(20){}Nr_Extenso_Periodo_Letivo_Ingresso_Matricula]", "Nr_Extenso_Periodo_Letivo_Ingresso_Matricula");
        getMarcadoMatricula().put("[(50){}Descricao_Periodo_Letivo_Ingresso_Matricula]", "Descricao_Periodo_Letivo_Ingresso_Matricula");
        getMarcadoMatricula().put("[(50){}Nome_Certificacao_Periodo_Letivo_Ingresso_Matricula]", "Nome_Certificacao_Periodo_Letivo_Ingresso_Matricula");
        getMarcadoMatricula().put("[(){}ListaDocumentosEntregues_Matricula]", "ListaDocumentosEntregues_Matricula");
        getMarcadoMatricula().put("[(80){}MatriculaEnadeData_matricula]", "MatriculaEnadeData_matricula");
        
        getMarcadoMatricula().put("[(5){}AutoDeclaracaoPretoPardoIndigena_Matricula]", "AutoDeclaracaoPretoPardoIndigena_Matricula");
        getMarcadoMatricula().put("[(5){}BolsasAuxilios_Matricula]", "BolsasAuxilios_Matricula");
        getMarcadoMatricula().put("[(50){}DiaSemanaAulaDescricao_Matricula]", "DiaSemanaAulaDescricao_Matricula");
        getMarcadoMatricula().put("[(4){}DiaSemanaAulaAbreviatura_Matricula]", "DiaSemanaAulaAbreviatura_Matricula");
        getMarcadoMatricula().put("[(2){}DiaSemanaAulaNumeral_Matricula]", "DiaSemanaAulaNumeral_Matricula");
        getMarcadoMatricula().put("[(50){}TurnoAulaDescricao_Matricula]", "TurnoAulaDescricao_Matricula");
        getMarcadoMatricula().put("[(5){}TurnoAulaAbreviatura_Matricula]", "TurnoAulaAbreviatura_Matricula");
        getMarcadoMatricula().put("[(5){}TurnoAulaInicio_Matricula]", "TurnoAulaInicio_Matricula");
        getMarcadoMatricula().put("[(5){}TurnoAulaTermino_Matricula]", "TurnoAulaTermino_Matricula");
        getMarcadoMatricula().put("[(10){}DataCancelamento_Matricula]", "DataCancelamento_Matricula");
        getMarcadoMatricula().put("[(100){}DataCancelamentoPorExtenso_Matricula]", "DataCancelamentoPorExtenso_Matricula");
        getMarcadoMatricula().put("[(10){}DataTrancamento_Matricula]", "DataTrancamento_Matricula");
        getMarcadoMatricula().put("[(100){}DataTrancamentoPorExtenso_Matricula]", "DataTrancamentoPorExtenso_Matricula");
        getMarcadoMatricula().put("[(10){}DataTransferencia_Matricula]", "DataTransferencia_Matricula");
        getMarcadoMatricula().put("[(100){}DataTransferenciaPorExtenso_Matricula]", "DataTransferenciaPorExtenso_Matricula");   
    }

    private static void inicializarDominioMarcadoDisciplina() {
        getMarcadoDisciplina().put("[(70){}Nome_Disciplina]", "Nome_Disciplina");
        getMarcadoDisciplina().put("[(10){}CargaHoraria_Disciplina]", "CargaHoraria_Disciplina");
        getMarcadoDisciplina().put("[(4){}Ano_Disciplina]", "Ano_Disciplina");
        getMarcadoDisciplina().put("[(4){}Semestre_Disciplina]", "Semestre_Disciplina");
        getMarcadoDisciplina().put("[(4){}Periodo_Disciplina]", "Periodo_Disciplina");
        getMarcadoDisciplina().put("[(30){}DescricaoPeriodoLet_Disciplina]", "DescricaoPeriodoLet_Disciplina");
        getMarcadoDisciplina().put("[(10){}Codigo_Disciplina]", "Codigo_Disciplina");
        getMarcadoDisciplina().put("[(250){}Abreviatura_Disciplina]", "Abreviatura_Disciplina");
        getMarcadoDisciplina().put("[(5){}Frequencia_Disciplina]", "Frequencia_Disciplina");
        getMarcadoDisciplina().put("[(14){}Mes_Disciplina]", "Mes_Disciplina");
        getMarcadoDisciplina().put("[(1){}Nota_Disciplina]", "Nota_Disciplina");
        getMarcadoDisciplina().put("[(30){}Situacao_Disciplina]", "Situacao_Disciplina");
        
    }

    private static void inicializarDominioMarcadoCurso() {
        getMarcadoCurso().put("[(10){}Codigo_Curso]", "Codigo_Curso");
        getMarcadoCurso().put("[(150){}Nome_Curso]", "Nome_Curso");        
        getMarcadoCurso().put("[(150){}NomeDocumentacao_Curso]", "NomeDocumentacao_Curso");
        getMarcadoCurso().put("[(150){}NomeDocumentacao_Curso]", "NomeDocumentacao_Curso");
        getMarcadoCurso().put("[(30){}AreaConhecimento_Curso]", "AreaConhecimento_Curso");
        getMarcadoCurso().put("[(20){}Regime_Curso]", "Regime_Curso");
        getMarcadoCurso().put("[(20){}RegimeAprovacao_Curso]", "RegimeAprovacao_Curso");
        getMarcadoCurso().put("[(10){}Periodicidade_Curso]", "Periodicidade_Curso");
        getMarcadoCurso().put("[(40){}Titulo_Curso]", "Titulo_Curso");
        getMarcadoCurso().put("[(10){}DataCriacao_Curso]", "DataCriacao_Curso");
        getMarcadoCurso().put("[(255){}NrRegistroInterno_Curso]", "NrRegistroInterno_Curso");
        getMarcadoCurso().put("[(10){}codigoInep_Curso]", "codigoInep_Curso");
        getMarcadoCurso().put("[(10){}DataPublicacao_Curso]", "DataPublicacao_Curso");
        getMarcadoCurso().put("[(10){}NrPeriodoLetivo_Curso]", "NrPeriodoLetivo_Curso");
        getMarcadoCurso().put("[(40){}BaseLegal_Curso]", "BaseLegal_Curso");
        getMarcadoCurso().put("[(10){}NivelEducacional_Curso]", "NivelEducacional_Curso");
        getMarcadoCurso().put("[(10){}CargaHoraria_Curso]", "CargaHoraria_Curso");
        getMarcadoCurso().put("[(10){}NrMesesConclusaoMatrizCurricular_Curso]", "NrMesesConclusaoMatrizCurricular_Curso");
        getMarcadoCurso().put("[(10){}CargaHorariaPeriodo_Curso]", "CargaHorariaPeriodo_Curso");
        getMarcadoCurso().put("[(){}ListaDisciplinaOptativa_Curso]", "ListaDisciplinaOptativa_Curso");

        getMarcadoCurso().put("[(100){}TextoDeclaracaoPPICurso_Curso]", "TextoDeclaracaoPPICurso_Curso");
        getMarcadoCurso().put("[(100){}TextoDeclaracaoBolsasAuxilios_Curso]", "TextoDeclaracaoBolsasAuxilios_Curso");
        getMarcadoCurso().put("[(100){}TextoDeclaracaoEscolaridadePublica_Curso]", "TextoDeclaracaoEscolaridadePublica_Curso");
        
        getMarcadoCurso().put("[(10){}ValorParcelaCursoComDescontos_Curso]", "ValorParcelaCursoComDescontos_Curso");
        getMarcadoCurso().put("[(10){}ValorParcelaCursoComDescontoExtenso_Curso]", "ValorParcelaCursoComDescontoExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorParcelaCursoComDescontosSemValidade_Curso]", "ValorParcelaCursoComDescontosSemValidade_Curso");
        getMarcadoCurso().put("[(30){}ValorParcelaCursoComDescontosSemValidadeExtenso_Curso]", "ValorParcelaCursoComDescontosSemValidadeExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorParcelaCursoComPlanoDescontosSemValidade_Curso]", "ValorParcelaCursoComPlanoDescontosSemValidade_Curso");
        getMarcadoCurso().put("[(30){}ValorParcelaCursoComPlanoDescontosSemValidadeExtenso_Curso]", "ValorParcelaCursoComPlanoDescontosSemValidadeExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}PercentualDescontoConvenio_Curso]", "PercentualDescontoConvenio_Curso");
        
        getMarcadoCurso().put("[(10){}ValorParcelaCursoComDescontosComValidade_Curso]", "ValorParcelaCursoComDescontosComValidade_Curso");
        getMarcadoCurso().put("[(10){}ValorParcelaCursoComDescontosComValidadeExtenso_Curso]", "ValorParcelaCursoComDescontosComValidadeExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorPlanoDescontosAteDataVencimento_Curso]", "ValorPlanoDescontosAteDataVencimento_Curso");
        getMarcadoCurso().put("[(70){}ValorPlanoDescontosAteDataVencimentoExtenso_Curso]", "ValorPlanoDescontosAteDataVencimentoExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorMatricula_Curso]", "ValorMatricula_Curso");
        getMarcadoCurso().put("[(30){}ValorMatriculaExtenso_Curso]", "ValorMatriculaExtenso_Curso");

        getMarcadoCurso().put("[(10){}ValorMatriculaCursoComDescontos_Curso]", "ValorMatriculaCursoComDescontos_Curso");
        getMarcadoCurso().put("[(30){}ValorMatriculaCursoComDescontosExtenso_Curso]", "ValorMatriculaCursoComDescontosExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorMatriculaCursoApenasDescontosProgressivo_Curso]", "ValorMatriculaCursoApenasDescontosProgressivo_Curso");
        getMarcadoCurso().put("[(30){}ValorMatriculaCursoApenasDescontosProgressivoExtenso_Curso]", "ValorMatriculaCursoApenasDescontosProgressivoExtenso_Curso");

        getMarcadoCurso().put("[(10){}NumParcela_Curso]", "NumParcela_Curso");
        getMarcadoCurso().put("[(30){}NumParcelaExtenso_Curso]", "NumParcelaExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorParcela_Curso]", "ValorParcela_Curso");
        getMarcadoCurso().put("[(30){}ValorParcelaExtenso_Curso]", "ValorParcelaExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalSemDescontoSemMatricula_Curso]", "ValorTotalSemDescontoSemMatricula_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalSemDescontoSemMatriculaExtenso_Curso]", "ValorTotalSemDescontoSemMatriculaExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorTotalSemDescontoComMatricula_Curso]", "ValorTotalSemDescontoComMatricula_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalSemDescontoComMatriculaExtenso_Curso]", "ValorTotalSemDescontoComMatriculaExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorTotal_Curso]", "ValorTotal_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalCursoSemProgressivo_Curso]", "ValorTotalCursoSemProgressivo_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalExtenso_Curso]", "ValorTotalExtenso_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalCursoApenasDescontosInstituicao_Curso]", "ValorTotalCursoApenasDescontosInstituicao_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalCursoApenasDescontosInstituicaoExtenso_Curso]", "ValorTotalCursoApenasDescontosInstituicaoExtenso_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalDescontosInstituicao_Curso]", "ValorTotalDescontosInstituicao_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalDescontosInstituicaoExtenso_Curso]", "ValorTotalDescontosInstituicaoExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalComConvenioCusteado_Curso]", "ValorTotalComConvenioCusteado_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalExtensoComConvenioCusteado_Curso]", "ValorTotalExtensoComConvenioCusteado_Curso");
        getMarcadoCurso().put("[(10){}ValorParcelaComConvenioCusteado_Curso]", "ValorParcelaComConvenioCusteado_Curso");
        getMarcadoCurso().put("[(30){}ValorParcelaExtensoComConvenioCusteado_Curso]", "ValorParcelaExtensoComConvenioCusteado_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalExtensoDivididoPorDois_Curso]", "ValorTotalExtensoDivididoPorDois_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalExtensoDivididoPorTres_Curso]", "ValorTotalExtensoDivididoPorTres_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalSemMatricula_Curso]", "ValorTotalSemMatricula_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalSemMatriculaExtenso_Curso]", "ValorTotalSemMatriculaExtenso_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalSemMatriculaExtensoDivididoPorDois_Curso]", "ValorTotalSemMatriculaExtensoDivididoPorDois_Curso");
        getMarcadoCurso().put("[(30){}ValorTotalSemMatriculaExtensoDivididoPorTres_Curso]", "ValorTotalSemMatriculaExtensoDivididoPorTres_Curso");
        
        getMarcadoCurso().put("[(10){}DiaVctoParcela_Curso]", "DiaVctoParcela_Curso");
        getMarcadoCurso().put("[(30){}DiaVctoParcelaExtenso_Curso]", "DiaVctoParcelaExtenso_Curso");

        getMarcadoCurso().put("[(10){}DiaVctoAPartirSegundaParcela_Curso]", "DiaVctoAPartirSegundaParcela_Curso");
        getMarcadoCurso().put("[(30){}DiaVctoAPartirSegundaParcelaExtenso_Curso]", "DiaVctoAPartirSegundaParcelaExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}DiaVencimentoPrimeiraParcela_Curso]", "DiaVencimentoPrimeiraParcela_Curso");
        getMarcadoCurso().put("[(30){}DiaVencimentoPrimeiraParcelaExtenso_Curso]", "DiaVencimentoPrimeiraParcelaExtenso_Curso");

        getMarcadoCurso().put("[(10){}NrDiasAntesVctoPlanoDesconto_Curso]", "NrDiasAntesVctoPlanoDesconto_Curso");
        getMarcadoCurso().put("[(10){}NrDiasAntesVcto1_Curso]", "NrDiasAntesVcto1_Curso");
        getMarcadoCurso().put("[(30){}NrDiasAntesVcto1Extenso_Curso]", "NrDiasAntesVcto1Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoDiasAntesVcto1_Curso]", "DescontoDiasAntesVcto1_Curso");
        getMarcadoCurso().put("[(30){}DescontoDiasAntesVcto1Extenso_Curso]", "DescontoDiasAntesVcto1Extenso_Curso");
        getMarcadoCurso().put("[(10){}NrDiasAntesVcto2_Curso]", "NrDiasAntesVcto2_Curso");
        getMarcadoCurso().put("[(30){}NrDiasAntesVcto2Extenso_Curso]", "NrDiasAntesVcto2Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoDiasAntesVcto2_Curso]", "DescontoDiasAntesVcto2_Curso");
        getMarcadoCurso().put("[(30){}DescontoDiasAntesVcto2Extenso_Curso]", "DescontoDiasAntesVcto2Extenso_Curso");
        getMarcadoCurso().put("[(10){}NrDiasAntesVcto3_Curso]", "NrDiasAntesVcto3_Curso");
        getMarcadoCurso().put("[(30){}NrDiasAntesVcto3Extenso_Curso]", "NrDiasAntesVcto3Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoDiasAntesVcto3_Curso]", "DescontoDiasAntesVcto3_Curso");
        getMarcadoCurso().put("[(30){}DescontoDiasAntesVcto3Extenso_Curso]", "DescontoDiasAntesVcto3Extenso_Curso");
        getMarcadoCurso().put("[(10){}NrDiasAntesVcto4_Curso]", "NrDiasAntesVcto4_Curso");
        getMarcadoCurso().put("[(30){}NrDiasAntesVcto4Extenso_Curso]", "NrDiasAntesVcto4Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoDiasAntesVcto4_Curso]", "DescontoDiasAntesVcto4_Curso");
        getMarcadoCurso().put("[(30){}DescontoDiasAntesVcto4Extenso_Curso]", "DescontoDiasAntesVcto4Extenso_Curso");

        getMarcadoCurso().put("[(10){}DescontoProgressivoValorDiasAntesVcto1_Curso]", "DescontoProgressivoValorDiasAntesVcto1_Curso");
        getMarcadoCurso().put("[(30){}DescontoProgressivoValorDiasAntesVcto1Extenso_Curso]", "DescontoProgressivoValorDiasAntesVcto1Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoProgressivoValorDiasAntesVcto2_Curso]", "DescontoProgressivoValorDiasAntesVcto2_Curso");
        getMarcadoCurso().put("[(30){}DescontoProgressivoValorDiasAntesVcto2Extenso_Curso]", "DescontoProgressivoValorDiasAntesVcto2Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoProgressivoValorDiasAntesVcto3_Curso]", "DescontoProgressivoValorDiasAntesVcto3_Curso");
        getMarcadoCurso().put("[(30){}DescontoProgressivoValorDiasAntesVcto3Extenso_Curso]", "DescontoProgressivoValorDiasAntesVcto3Extenso_Curso");
        getMarcadoCurso().put("[(10){}DescontoProgressivoValorDiasAntesVcto4_Curso]", "DescontoProgressivoValorDiasAntesVcto4_Curso");
        getMarcadoCurso().put("[(30){}DescontoProgressivoValorDiasAntesVcto4Extenso_Curso]", "DescontoProgressivoValorDiasAntesVcto4Extenso_Curso");

        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto1_Curso]", "ValorAPagarDescontoDiasAntesVcto1_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto1ApenasProgressivo_Curso]", "ValorAPagarDescontoDiasAntesVcto1ApenasProgressivo_Curso");
        getMarcadoCurso().put("[(40){}ValorAPagarDescontoDiasAntesVcto1Extenso_Curso]", "ValorAPagarDescontoDiasAntesVcto1Extenso_Curso");
        getMarcadoCurso().put("[(10){}DiaVcto1_Curso]", "DiaVcto1_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto2_Curso]", "ValorAPagarDescontoDiasAntesVcto2_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto2ApenasProgressivo_Curso]", "ValorAPagarDescontoDiasAntesVcto2ApenasProgressivo_Curso");
        getMarcadoCurso().put("[(40){}ValorAPagarDescontoDiasAntesVcto2Extenso_Curso]", "ValorAPagarDescontoDiasAntesVcto2Extenso_Curso");
        getMarcadoCurso().put("[(10){}DiaVcto2_Curso]", "DiaVcto2_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto3_Curso]", "ValorAPagarDescontoDiasAntesVcto3_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto3ApenasProgressivo_Curso]", "ValorAPagarDescontoDiasAntesVcto3ApenasProgressivo_Curso");
        getMarcadoCurso().put("[(40){}ValorAPagarDescontoDiasAntesVcto3Extenso_Curso]", "ValorAPagarDescontoDiasAntesVcto3Extenso_Curso");
        getMarcadoCurso().put("[(10){}DiaVcto3_Curso]", "DiaVcto3_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto4_Curso]", "ValorAPagarDescontoDiasAntesVcto4_Curso");
        getMarcadoCurso().put("[(10){}ValorAPagarDescontoDiasAntesVcto4ApenasProgressivo_Curso]", "ValorAPagarDescontoDiasAntesVcto4ApenasProgressivo_Curso");
        getMarcadoCurso().put("[(40){}ValorAPagarDescontoDiasAntesVcto4Extenso_Curso]", "ValorAPagarDescontoDiasAntesVcto4Extenso_Curso");
        getMarcadoCurso().put("[(10){}DiaVcto4_Curso]", "DiaVcto4_Curso");

        getMarcadoCurso().put("[(10){}ValorTotalDesconto_Curso]", "ValorTotalDesconto_Curso");
        getMarcadoCurso().put("[(10){}ValorPorDisciplina_Curso]", "ValorPorDisciplina_Curso");
        getMarcadoCurso().put("[(10){}ValorPorDisciplinaExtenso_Curso]", "ValorPorDisciplinaExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDescontoComValidade_Curso]", "ValorTotalDescontoComValidade_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDescontoComValidadeExtenso_Curso]", "ValorTotalDescontoComValidadeExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDescontoSemValidade_Curso]", "ValorTotalDescontoSemValidade_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalDescontoExtenso_Curso]", "ValorTotalDescontoExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDesconto1Antecipacao_Curso]", "ValorTotalDesconto1Antecipacao_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalDescontoExtenso1Antecipacao_Curso]", "ValorTotalDescontoExtenso1Antecipacao_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDesconto2Antecipacao_Curso]", "ValorTotalDesconto2Antecipacao_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalDescontoExtenso2Antecipacao_Curso]", "ValorTotalDescontoExtenso2Antecipacao_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDesconto3Antecipacao_Curso]", "ValorTotalDesconto3Antecipacao_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalDescontoExtenso3Antecipacao_Curso]", "ValorTotalDescontoExtenso3Antecipacao_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalDesconto4Antecipacao_Curso]", "ValorTotalDesconto4Antecipacao_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalDescontoExtenso4Antecipacao_Curso]", "ValorTotalDescontoExtenso4Antecipacao_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalDescontoSemValidadeExtenso_Curso]", "ValorTotalDescontoSemValidadeExtenso_Curso");
        
        getMarcadoCurso().put("[(10){}ValorTotalPlanoDescontoSemValidade_Curso]", "ValorTotalPlanoDescontoSemValidade_Curso");
        getMarcadoCurso().put("[(40){}ValorTotalPlanoDescontoSemValidadeExtenso_Curso]", "ValorTotalPlanoDescontoSemValidadeExtenso_Curso");

        getMarcadoCurso().put("[(10){}ValorDescontoDiasAntesVcto4_Curso]", "ValorDescontoDiasAntesVcto4_Curso");
        getMarcadoCurso().put("[(40){}ValorDescontoDiasAntesVcto4Extenso_Curso]", "ValorDescontoDiasAntesVcto4Extenso_Curso");
        getMarcadoCurso().put("[(10){}ValorDescontoDiasAntesVcto3_Curso]", "ValorDescontoDiasAntesVcto3_Curso");
        getMarcadoCurso().put("[(40){}ValorDescontoDiasAntesVcto3Extenso_Curso]", "ValorDescontoDiasAntesVcto3Extenso_Curso");
        getMarcadoCurso().put("[(10){}ValorDescontoDiasAntesVcto2_Curso]", "ValorDescontoDiasAntesVcto2_Curso");
        getMarcadoCurso().put("[(40){}ValorDescontoDiasAntesVcto2Extenso_Curso]", "ValorDescontoDiasAntesVcto2Extenso_Curso");
        getMarcadoCurso().put("[(10){}ValorDescontoDiasAntesVcto1_Curso]", "ValorDescontoDiasAntesVcto1_Curso");
        getMarcadoCurso().put("[(40){}ValorDescontoDiasAntesVcto1Extenso_Curso]", "ValorDescontoDiasAntesVcto1Extenso_Curso");

        getMarcadoCurso().put("[(20){}Duracao_Curso]", "Duracao_Curso");
        getMarcadoCurso().put("[(120){}DescricaoCondicaoCurso_Curso]", "DescricaoCondicaoCurso_Curso");
        getMarcadoCurso().put("[(20){}NrParcelasPeriodo_Curso]", "NrParcelasPeriodo_Curso");
        getMarcadoCurso().put("[(40){}PeriodoLetivo_Curso]", "PeriodoLetivo_Curso");
        getMarcadoCurso().put("[(40){}CondicaoPagamento_Curso]", "CondicaoPagamento_Curso");
        
        getMarcadoCurso().put("[(10){}DataInicio_Curso]", "DataInicio_Curso");
        getMarcadoCurso().put("[(10){}DataFinal_Curso]", "DataFinal_Curso");
        getMarcadoCurso().put("[(10){}DataInicioProgAula_Curso]", "DataInicioProgAula_Curso");
        getMarcadoCurso().put("[(4){}TotalSemestres_Curso]", "TotalSemestres_Curso");        
        getMarcadoCurso().put("[(10){}DataFinalProgAula_Curso]", "DataFinalProgAula_Curso");
        getMarcadoCurso().put("[(4){}AnoAtual_Curso]", "AnoAtual_Curso");
        getMarcadoCurso().put("[(15){}SemestreAtual_Curso]", "SemestreAtual_Curso");
        getMarcadoCurso().put("[(15){}SemestreAtual2_Curso]", "SemestreAtual2_Curso");        

        getMarcadoCurso().put("[(2){}DiaPrimeiraParcela_Curso]", "DiaPrimeiraParcela_Curso");
        getMarcadoCurso().put("[(20){}MesPrimeiraParcela_Curso]", "MesPrimeiraParcela_Curso");
        getMarcadoCurso().put("[(4){}AnoPrimeiraParcela_Curso]", "AnoPrimeiraParcela_Curso");

        getMarcadoCurso().put("[(10){}DataVencimentoPrimeiraParcela_Curso]", "DataVencimentoPrimeiraParcela_Curso");
        getMarcadoCurso().put("[(10){}DataVencimentoSegundaParcela_Curso]", "DataVencimentoSegundaParcela_Curso");
        getMarcadoCurso().put("[(10){}DataVencimentoUltimaParcela_Curso]", "DataVencimentoUltimaParcela_Curso");

        getMarcadoCurso().put("[(60){}NomeConvenio_Curso]", "NomeConvenio_Curso");
        getMarcadoCurso().put("[(10){}ValorParcelaComDescontoConvenio_Curso]", "ValorParcelaComDescontoConvenio_Curso");
        getMarcadoCurso().put("[(40){}ValorParcelaComDescontoConvenio_CursoExtenso]", "ValorParcelaComDescontoConvenio_CursoExtenso");
        getMarcadoCurso().put("[(10){}ValorParcelaSemDescontoConvenio_Curso]", "ValorParcelaSemDescontoConvenio_Curso");
        getMarcadoCurso().put("[(40){}ValorParcelaSemDescontoConvenio_CursoExtenso]", "ValorParcelaSemDescontoConvenio_CursoExtenso");
        getMarcadoCurso().put("[(10){}ValorDescontoAPagarSemDescontoConvenio_Curso]", "ValorDescontoAPagarSemDescontoConvenio_Curso");
        getMarcadoCurso().put("[(40){}ValorDescontoAPagarSemDescontoConvenio_CursoExtenso]", "ValorDescontoAPagarSemDescontoConvenio_CursoExtenso");
        getMarcadoCurso().put("[(15){}ValorPrimeiraParcelaDescontoComValidade_Curso]", "ValorPrimeiraParcelaDescontoComValidade_Curso");

        getMarcadoCurso().put("[(15){}ValorDescontoInstitucionalPrimeiraParcela_Curso]", "ValorDescontoInstitucionalPrimeiraParcela_Curso");
        getMarcadoCurso().put("[(200){}ValorDescontoInstitucionalPrimeiraParcelaExtenso_Curso]", "ValorDescontoInstitucionalPrimeiraParcelaExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorParcelaPrimeiroDiaAntecipacaoConvenio_Curso]", "ValorParcelaPrimeiroDiaAntecipacaoConvenio_Curso");
        getMarcadoCurso().put("[(40){}ValorParcelaPrimeiroDiaAntecipacaoConvenio_CursoExtenso]", "ValorParcelaPrimeiroDiaAntecipacaoConvenio_CursoExtenso");

        getMarcadoCurso().put("[(60){}NomePlanoDescontos_Curso]", "NomePlanoDescontos_Curso");
        getMarcadoCurso().put("[(250){}Texto_Curso]", "Texto_Curso");
        getMarcadoCurso().put("[(250){}InformacoesAdicionais_Curso]", "InformacoesAdicionais_Curso");
        getMarcadoCurso().put("[(50){}FormaIngresso_Curso]", "FormaIngresso_Curso");
        getMarcadoCurso().put("[(70){}Nome_CursoOrigem]", "Nome_CursoOrigem");
        getMarcadoCurso().put("[(70){}Nome_CursoDestino]", "Nome_CursoDestino");
        getMarcadoCurso().put("[(5000){}TabelaTipoDesconto_Curso]", "TabelaTipoDesconto_Curso");
        getMarcadoCurso().put("[(5000){}TabelaTodosTipoDesconto_Curso]", "TabelaTodosTipoDesconto_Curso");		
        getMarcadoCurso().put("[(5000){}TabelaListaContaReceberComDescontos_Curso]", "TabelaListaContaReceberComDescontos_Curso");		
        getMarcadoCurso().put("[(255){}Nome_Certificacao]", "Nome_Certificacao");
        getMarcadoCurso().put("[(40){}DataFinalExtenso_Curso]", "DataFinalExtenso_Curso");
        getMarcadoCurso().put("[(80){}DataFinalExtenso2_Curso]", "DataFinalExtenso2_Curso");        
        getMarcadoCurso().put("[(255){}TitulacaoFormando_Curso]", "TitulacaoFormando_Curso");
        getMarcadoCurso().put("[(80){}DataExtensoInicioCurso_Curso]", "DataExtensoInicioCurso_Curso");
        getMarcadoCurso().put("[(80){}DataExtensoInicioCurso2_Curso]", "DataExtensoInicioCurso2_Curso");        
        getMarcadoCurso().put("[(4){}AnoConclusao_Curso]", "AnoConclusao_Curso");
        getMarcadoCurso().put("[(60){}Habilitacao_Curso]", "Habilitacao_Curso");
        getMarcadoCurso().put("[(150){}PrimeiroReconhecimento_Curso]", "PrimeiroReconhecimento_Curso");
        getMarcadoCurso().put("[(40){}DataPrimeiroReconhecimento_Curso]", "DataPrimeiroReconhecimento_Curso");
        getMarcadoCurso().put("[(150){}RenovacaoReconhecimento_Curso]", "RenovacaoReconhecimento_Curso");
        getMarcadoCurso().put("[(40){}DataRenovacaoReconhecimento_Curso]", "DataRenovacaoReconhecimento_Curso");
        getMarcadoCurso().put("[(40){}PrevisaoConclusao_Curso]", "PrevisaoConclusao_Curso");
        getMarcadoCurso().put("[(3000){}CompetenciaProfissional_Curso]", "CompetenciaProfissional_Curso");
        getMarcadoCurso().put("[(255){}PerfilEgresso_Curso]", "PerfilEgresso_Curso");
        getMarcadoCurso().put("[(255){}PublicoAlvo_Curso]", "PublicoAlvo_Curso");
        
        getMarcadoCurso().put("[(10){}QuantidadeParcelasMaterialDidatico_Curso]", "QuantidadeParcelasMaterialDidatico_Curso");
        getMarcadoCurso().put("[(10){}ValorParcelaMaterialDidatico_Curso]", "ValorParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}ValorExtensoParcelaMaterialDidatico_Curso]", "ValorExtensoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(10){}ValorDescontoParcelaMaterialDidatico_Curso]", "ValorDescontoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}ValorDescontoExtensoParcelaMaterialDidatico_Curso]", "ValorDescontoExtensoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalParcelaMaterialDidatico_Curso]", "ValorTotalParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}ValorTotalExtensoParcelaMaterialDidatico_Curso]", "ValorTotalExtensoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalComDescontoParcelaMaterialDidatico_Curso]", "ValorTotalComDescontoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}ValorTotalComDescontoExtensoParcelaMaterialDidatico_Curso]", "ValorTotalComDescontoExtensoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(5){}DiaBaseVencimentoParcelaMaterialDidatico_Curso]", "DiaBaseVencimentoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}UnidadeEnsinoMaterialDidatico_Curso]", "UnidadeEnsinoMaterialDidatico_Curso");
        getMarcadoCurso().put("[(255){}EnderecoUnidadeEnsinoMaterialDidatico_Curso]", "EnderecoUnidadeEnsinoMaterialDidatico_Curso");
        getMarcadoCurso().put("[(18){}CNPJUnidadeEnsinoMaterialDidatico_Curso]", "CNPJUnidadeEnsinoMaterialDidatico_Curso");
        getMarcadoCurso().put("[(15){}ValorParcelaMaterialDidaticoComDescontoIncondicional_Curso]", "ValorParcelaMaterialDidaticoComDescontoIncondicional_Curso");
        getMarcadoCurso().put("[(200){}ValorParcelaMaterialDidaticoComDescontoIncondicionalExtenso_Curso]", "ValorParcelaMaterialDidaticoComDescontoIncondicionalExtenso_Curso");
        getMarcadoCurso().put("[(255){}PublicoAlvo_Curso]", "PublicoAlvo_Curso");   
        
        getMarcadoCurso().put("[(15){}ValorIntegralMatriculaCondicaoPagamento_Curso]", "ValorIntegralMatricula_CondicaoPagamentoPlanoFinanceiro");
        getMarcadoCurso().put("[(15){}ValorIntegralParcelaCondicaoPagamento_Curso]", "ValorIntegralParcela_CondicaoPagamentoPlanoFinanceiro");
        getMarcadoCurso().put("[(2){}NrParcelasPeriodoCondicaoPagamento_Curso]", "NrParcelasPeriodo_CondicaoPagamentoPlanoFinanceiro");
        getMarcadoCurso().put("[(200){}ValorIntegralMatriculaCondicaoPagamentoExtenso_Curso]", "ValorIntegralMatricula_CondicaoPagamentoPlanoFinanceiroExtenso");
        getMarcadoCurso().put("[(200){}ValorIntegralParcelaCondicaoPagamentoExtenso_Curso]", "ValorIntegralParcela_CondicaoPagamentoPlanoFinanceiroExtenso");
        getMarcadoCurso().put("[(15){}ValorTotalParcelasCondicaoPagamento_Curso]", "ValorTotalParcelasCondicaoPagamento_Curso");
        getMarcadoCurso().put("[(200){}ValorTotalParcelasCondicaoPagamentoExtenso_Curso]", "ValorTotalParcelasCondicaoPagamentoExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorParcelaComDescontoInstitucional_Curso]", "ValorParcelasComDescontoInstitucional_Curso");
        getMarcadoCurso().put("[(200){}ValorParcelaComDescontoInstitucionalExtenso_Curso]", "ValorParcelasComDescontoInstitucionalExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorTotalParcelasComDescontoIncondicional_Curso]", "ValorTotalParcelasComDescontoIncondicional_Curso");
        getMarcadoCurso().put("[(200){}ValorTotalParcelasComDescontoIncondicionalExtenso_Curso]", "ValorTotalParcelasComDescontoIncondicionalExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorParcelaComDescontoIncondicional_Curso]", "ValorParcelaComDescontoIncondicional_Curso");
        getMarcadoCurso().put("[(200){}ValorParcelaComDescontoIncondicionalExtenso_Curso]", "ValorParcelaComDescontoIncondicionalExtenso_Curso");
        
        getMarcadoCurso().put("[(200){}QuantidadeParcelasExtenso_Curso]", "QuantidadeParcelasExtenso_Curso");
        getMarcadoCurso().put("[(2){}NumeroTotalDisciplinasEAD_Curso]", "NumeroTotalDisciplinasEAD_Curso");
        getMarcadoCurso().put("[(2){}NumeroTotalDisciplinasPresencial_Curso]", "NumeroTotalDisciplinasPresencial_Curso");
        getMarcadoCurso().put("[(2){}NumeroTotalDisciplinas_Curso]", "NumeroTotalDisciplinas_Curso");
        getMarcadoCurso().put("[(15){}ValorDescontoCondicionalMatricula_Curso]", "ValorDescontoCondicionalMatricula_Curso");
        getMarcadoCurso().put("[(200){}ValorDescontoCondicionalMatriculaExtenso_Curso]", "ValorDescontoCondicionalMatriculaExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorDescontoCondicionalParcela_Curso]", "ValorDescontoCondicionalParcela_Curso");
        getMarcadoCurso().put("[(200){}ValorDescontoCondicionalParcelaExtenso_Curso]", "ValorDescontoCondicionalParcelaExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorDescontoCondicionalMaterialDidatico_Curso]", "ValorDescontoCondicionalMaterialDidatico_Curso");
        getMarcadoCurso().put("[(200){}ValorDescontoCondicionalMaterialDidaticoExtenso_Curso]", "ValorDescontoCondicionalMaterialDidaticoExtenso_Curso");
        getMarcadoCurso().put("[(15){}ValorDescontoInCondicionalMaterialDidatico_Curso]", "ValorDescontoInCondicionalMaterialDidatico_Curso");
        
        getMarcadoCurso().put("[(10){}PorcentagemCusteadoConvenioParcela_Curso]", "PorcentagemCusteadoConvenioParcela_Curso");
        getMarcadoCurso().put("[(10){}PorcentagemCusteadoAlunoParcela_Curso]", "PorcentagemCusteadoAlunoParcela_Curso");
        getMarcadoCurso().put("[(10){}ValorTotalCusteadoConvenioParcela_Curso]", "ValorTotalCusteadoConvenioParcela_Curso");
        getMarcadoCurso().put("[(150){}ValorTotalCusteadoConvenioParcelaExtenso_Curso]", "ValorTotalCusteadoConvenioParcelaExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorCusteadoConvenioParcelaMaterialDidatico_Curso]", "ValorCusteadoConvenioParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}ValorCusteadoConvenioParcelaMaterialDidaticoExtenso_Curso]", "ValorCusteadoConvenioParcelaMaterialDidaticoExtenso_Curso");
        getMarcadoCurso().put("[(10){}ValorCusteadoAlunoParcelaMaterialDidatico_Curso]", "ValorCusteadoAlunoParcelaMaterialDidatico_Curso");
        getMarcadoCurso().put("[(150){}ValorCusteadoAlunoParcelaMaterialDidaticoExtenso_Curso]", "ValorCusteadoAlunoParcelaMaterialDidaticoExtenso_Curso");
        getMarcadoCurso().put("[(150){}NomeDocenteResponsavelAssinaturaTermoEstagio_Curso]", "NomeDocenteResponsavelAssinaturaTermoEstagio_Curso");
        getMarcadoCurso().put("[(150){}CpfDocenteResponsavelAssinaturaTermoEstagio_Curso]", "CpfDocenteResponsavelAssinaturaTermoEstagio_Curso");
        getMarcadoCurso().put("[(150){}EmailDocenteResponsavelAssinaturaTermoEstagio_Curso]", "EmailDocenteResponsavelAssinaturaTermoEstagio_Curso");

        getMarcadoUnidadeEnsino().put("[(255){}AssinaturaDigitalfuncionarioPrincipal_Curso]", "AssinaturaDigitalfuncionarioPrincipal_Curso");
        getMarcadoUnidadeEnsino().put("[(255){}AssinaturaDigitalfuncionarioPrincipalIreport_Curso]", "AssinaturaDigitalfuncionarioPrincipalIreport_Curso");
        getMarcadoUnidadeEnsino().put("[(255){}AssinaturaDigitalfuncionarioSecundario_Curso]", "AssinaturaDigitalfuncionarioSecundario_Curso");
        getMarcadoUnidadeEnsino().put("[(255){}AssinaturaDigitalfuncionarioSecundarioIreport_Curso]", "AssinaturaDigitalfuncionarioSecundarioIreport_Curso");
        getMarcadoUnidadeEnsino().put("[(255){}SeloAssinaturaEletronica_Curso]", "SeloAssinaturaEletronica_Curso");
        getMarcadoUnidadeEnsino().put("[(255){}SeloAssinaturaEletronicaIreport_Curso]", "SeloAssinaturaEletronicaIreport_Curso");
    }

    private static void inicializarDominioSituacaoProvisaoDeCusto() {
        getSituacaoProvisaoDeCusto().put("FI", "Finalizado");
        getSituacaoProvisaoDeCusto().put("AT", "Ativo");
    }

    private static void inicializarDominioSituacaoCheque() {
        getSituacaoCheque().put("BA", "Banco");
        getSituacaoCheque().put("EC", "Em Caixa");
        getSituacaoCheque().put("PA", "Pagamento");
        getSituacaoCheque().put("PE", "Pendente");
    }

    private static void inicializarDominioSituacaoRecebimentoCompra() {
        getSituacaoRecebimentoCompra().put("PR", "Previsão");
        getSituacaoRecebimentoCompra().put("EF", "Efetivada");
    }

    private static void inicializarDominioInformarTurma() {
        getInformarTurma().put("NC", "Não controlar");
        getInformarTurma().put("CU", "Curso");
        getInformarTurma().put("CT", "Curso/Turno");
        getInformarTurma().put("TU", "Turma");
    }

    private static void inicializarDominioTipoSacado() {
        getTipoSacado().put("FO", "Fornecedor");
        getTipoSacado().put("FU", "Funcionário/Professor");
        getTipoSacado().put("BA", "Banco");
        getTipoSacado().put("AL", "Aluno");
        getTipoSacado().put("PA", "Parceiro");
        getTipoSacado().put("RF", "Responsável Financeiro");
        getTipoSacado().put("OC", "Operadora Cartão");
    }

    private static void inicializarDominioTipoSacadoContratosReceitas() {
        getTipoSacadoContratosReceitas().put("PA", "Fornecedor");
        getTipoSacadoContratosReceitas().put("FU", "Funcionário/Professor");
        getTipoSacadoContratosReceitas().put("AL", "Aluno");
    }

    private static void inicializarDominioNivelCategoriaDespesa() {
        getNivelCategoriaDespesa().put("UE", "Unidade Ensino");
        getNivelCategoriaDespesa().put("DE", "Departamento");
        getNivelCategoriaDespesa().put("FU", "Funcionário");
        getNivelCategoriaDespesa().put("NC", "Não controlar");

    }

    private static void inicializarDominioTipoAutoria() {
        getTipoAutoria().put("PR", "Principal");
        getTipoAutoria().put("SE", "Secundário");
    }

//    private static void inicializarDominioTipoEscopoQuestionario() {
//        getTipoEscopoQuestionario().put("GE", "Geral");
//        getTipoEscopoQuestionario().put("DI", "Disciplina");
//        getTipoEscopoQuestionario().put("UM", "Último Módulo");
//        getTipoEscopoQuestionario().put("PS", "Processo Seletivo");
//        getTipoEscopoQuestionario().put("BC", "Banco Curriculum");
//        getTipoEscopoQuestionario().put("PR", "Professores");
//        getTipoEscopoQuestionario().put("FG", "Funcionário/Gestor");
//        getTipoEscopoQuestionario().put("CO", "Coordenadores");
//    }

    private static void inicializarDominioPublicoAlvo() {
        getPublicoAlvo().put("PR", "Todos Professores");
        getPublicoAlvo().put("TO", "Todos");
        getPublicoAlvo().put("AL", "Todos Alunos");
        getPublicoAlvo().put("CO", "Todos Coordenadores");
        getPublicoAlvo().put("CU", "Curso");
        getPublicoAlvo().put("TU", "Turma");
        getPublicoAlvo().put("FU", "Funcionário/Gestor");
    }

    private static void inicializarDominioTipoContratoDespesas() {
        getTipoContratoDespesas().put("ME", "Parcelas com Vctos Mensais");
        getTipoContratoDespesas().put("AN", "Parcelas com Vctos Anuais");
        getTipoContratoDespesas().put("ES", "Parcelas com Vctos Específicos");
    }

    private static void inicializarDominioSituacaoFinanceira() {
        getSituacaoFinanceira().put("AP", "A Pagar");
        getSituacaoFinanceira().put("PA", "Pago");
        //getSituacaoFinanceira().put("PP", "Pago Parcial");
    }

    private static void inicializarDominioSituacaoFinanceiraMatricula() {
        getSituacaoFinanceiraMatricula().put("PF", "Pendente Financeiramente");
        getSituacaoFinanceiraMatricula().put("CO", "Confirmado");
    }

    private static void inicializarDominioTipoMovimentacaoEstoque() {
        getTipoMovimentacaoEstoque().put("EP", "Entrada de Produto");
        getTipoMovimentacaoEstoque().put("SP", "Saída de Produto");
        getTipoMovimentacaoEstoque().put("TP", "Transferência de Produto");
    }

    private static void inicializarDominioSituacaoEntregaRecebimento() {
        getSituacaoEntregaRecebimento().put("FI", "Finalizado");
        getSituacaoEntregaRecebimento().put("PE", "Pendente");
        getSituacaoEntregaRecebimento().put("PA", "Parcial");
    }

    private static void inicializarDominioSituacaoAutorizacao() {
        getSituacaoAutorizacao().put("AU", "Autorizado");
        getSituacaoAutorizacao().put("IN", "Indeferido");
        getSituacaoAutorizacao().put("PE", "Pendente");
    }

    private static void inicializarDominioTipoUsuario() {
        getTipoUsuario().put("DM", "Diretor MultiCampus");
        // getTipoUsuario().put("DC", "Diretor Campos");
        // getTipoUsuario().put("CO", "Coordenador");
        getTipoUsuario().put("AL", "Aluno");
        getTipoUsuario().put("PR", "Professor");
        getTipoUsuario().put("FU", "Funcionário");
        getTipoUsuario().put("PA", "Parceiro");
        getTipoUsuario().put("RL", "Responsável Legal");
        getTipoUsuario().put("CA", "Candidato");
        getTipoUsuario().put("VI", "Visitante");

    }

    private static void inicializarDominioSituacaoGradeCurricular() {
        getSituacaoGradeCurricular().put("DE", "Defasada");
        getSituacaoGradeCurricular().put("CO", "Construção");
        getSituacaoGradeCurricular().put("IN", "Inativa");
        getSituacaoGradeCurricular().put("AT", "Ativa");
    }

    private static void inicializarDominioConteudoPlanejamento() {
        getConteudoPlanejamento().put("CO", "Conteúdo");
        getConteudoPlanejamento().put("AV", "Avaliação");
        getConteudoPlanejamento().put("EV", "Evento");
        getConteudoPlanejamento().put("VT", "Visita Técnica");
        getConteudoPlanejamento().put("PR", "Prático");

    }

    private static void inicializarDominioTipoOrigemContaPagar() {
        getTipoOrigemContaPagar().put("CO", "Compra");
        getTipoOrigemContaPagar().put("SE", "Serviço");
        getTipoOrigemContaPagar().put("MU", "Multa");
        getTipoOrigemContaPagar().put("RE", "Requisição");
        getTipoOrigemContaPagar().put("CP", "Registro Manual");
    }

    private static void inicializarDominioTipoOrigemContaReceber() {
        getTipoOrigemContaReceber().put("IPS", "Inscrição Processo Seletivo");
        getTipoOrigemContaReceber().put("MAT", "Matrícula");
        getTipoOrigemContaReceber().put("REQ", "Requerimento");
        getTipoOrigemContaReceber().put("MEN", "Mensalidade");
        getTipoOrigemContaReceber().put("BIB", "Biblioteca");
        getTipoOrigemContaReceber().put("BCC", "Bolsa Custeada Convênio");
        getTipoOrigemContaReceber().put("NCR", "Negociação");
        getTipoOrigemContaReceber().put("CTR", "Contrato Receita");
        getTipoOrigemContaReceber().put("IRE", "Inclusão/Reposição");
        getTipoOrigemContaReceber().put("DCH", "Devolução de Cheque");
        getTipoOrigemContaReceber().put("OUT", "Outros");
    }

    private static void inicializarDominioTipoOrigemContaReceberRelatorio() {
    	getTipoOrigemContaReceberRelatorio().put("MAT", "Matrícula");
    	getTipoOrigemContaReceberRelatorio().put("MEN", "Mensalidade");
    	getTipoOrigemContaReceberRelatorio().put("MAT_MEN", "Matrícula/Mensalidade");
    	getTipoOrigemContaReceberRelatorio().put("IPS", "Inscrição Processo Seletivo");
    	getTipoOrigemContaReceberRelatorio().put("REQ", "Requerimento");
    	getTipoOrigemContaReceberRelatorio().put("BIB", "Biblioteca");
    	getTipoOrigemContaReceberRelatorio().put("BCC", "Bolsa Custeada Convênio");
    	getTipoOrigemContaReceberRelatorio().put("NCR", "Negociação");
    	getTipoOrigemContaReceberRelatorio().put("CTR", "Contrato Receita");
    	getTipoOrigemContaReceberRelatorio().put("IRE", "Inclusão/Reposição");
    	getTipoOrigemContaReceberRelatorio().put("DCH", "Devolução de Cheque");
    	getTipoOrigemContaReceberRelatorio().put("OUT", "Outros");
    }
    
    private static void inicializarDominioTipoPublicacaoReferenciaBibliografica() {
        getTipoPublicacaoReferenciaBibliografica().put("LI", "Livro");
        getTipoPublicacaoReferenciaBibliografica().put("AR", "Artigo");
        getTipoPublicacaoReferenciaBibliografica().put("RE", "Revista");
        getTipoPublicacaoReferenciaBibliografica().put("MO", "Monografia");
        getTipoPublicacaoReferenciaBibliografica().put("VI", "Video");
        getTipoPublicacaoReferenciaBibliografica().put("LK", "Link");
        getTipoPublicacaoReferenciaBibliografica().put("NO", "Norma");
    }

    private static void inicializarDominioContaPagarTipoJuroTipoMulta() {
        getContaPagarTipoJuroTipoMulta().put("M", "Moeda");
        getContaPagarTipoJuroTipoMulta().put("P", "Porcentagem");

    }

    private static void inicializarDominioSituacaoTrancamento() {
        getSituacaoTrancamento().put("FD", "Deferido");
        getSituacaoTrancamento().put("ES", "Estornado");
        getSituacaoTrancamento().put("FI", "Indeferido");
    }

    private static void inicializarDominioPeriodicidadeBiblioteca() {
        getPeriodicidadeBiblioteca().put("SM", "Semestral");
        getPeriodicidadeBiblioteca().put("QI", "Quinzenal");
		getPeriodicidadeBiblioteca().put("QU", "Quadrimestral");
        getPeriodicidadeBiblioteca().put("BI", "Bimestral");
        getPeriodicidadeBiblioteca().put("AN", "Anual");
        getPeriodicidadeBiblioteca().put("ME", "Mensal");
        getPeriodicidadeBiblioteca().put("DI", "Diário");
        getPeriodicidadeBiblioteca().put("TR", "Trimestral");
        getPeriodicidadeBiblioteca().put("SE", "Semanal");
    }

    private static void inicializarDominioTipoExemplarExemplar() {
        getTipoExemplarExemplar().put("PE", "Periódico");
        getTipoExemplarExemplar().put("PU", "Publicação");
        getTipoExemplarExemplar().put("RA", "Recurso Áudio Visual");
    }

    private static void inicializarDominioSituacaoEmprestimo() {
        getSituacaoEmprestimo().put("FI", "Finalizado");
        getSituacaoEmprestimo().put("AT", "Atrasado");
        getSituacaoEmprestimo().put("EA", "Em Andamento");
    }

    private static void inicializarDominioTipoSaidaItemRegistroSaidaAcervo() {
        getTipoSaidaItemRegistroSaidaAcervo().put("DE", "Deteriorado");
        getTipoSaidaItemRegistroSaidaAcervo().put("DO", "Doação");
        getTipoSaidaItemRegistroSaidaAcervo().put("ES", "Estraviado");
        getTipoSaidaItemRegistroSaidaAcervo().put("TR", "Transferência");
    }

    private static void inicializarDominioTipoPublicacaoPublicacao() {
        getTipoPublicacaoPublicacao().put("LI", "Livro");
        getTipoPublicacaoPublicacao().put("AR", "Artigo");
        getTipoPublicacaoPublicacao().put("MO", "Monografia");
    }

    private static void inicializarDominioTipoEntradaAcervo() {
        getTipoEntradaAcervo().put("DO", "Doação");
        getTipoEntradaAcervo().put("TR", "Trasferência");
        getTipoEntradaAcervo().put("CO", "Compra");
    }

    private static void inicializarDominioSituacaoItemEmprestimo() {
        getSituacaoItemEmprestimo().put("DE", "Devolvido");
        getSituacaoItemEmprestimo().put("EE", "Em Empréstimo");
    }

    private static void inicializarDominioSituacaoMatriculaPeriodoPreMatriculada() {
        getSituacaoMatriculaPeriodoPreMatriculada().put("GP", "Pré-Matrícula - Pagamento Confirmado");
        getSituacaoMatriculaPeriodoPreMatriculada().put("PP", "Pré-Matrícula - Pagamento Pendente");
    }

    private static void inicializarDominioEscopoAvaliacaoInstitucional() {
        getEscopoAvaliacaoInstitucional().put("DO", "Docente");
        getEscopoAvaliacaoInstitucional().put("AD", "Administrativo");
        getEscopoAvaliacaoInstitucional().put("DI", "Discente");
        getEscopoAvaliacaoInstitucional().put("CO", "Colaboradores");
    }

    private static void inicializarDominioMatriculaPeriodoSituacao() {
        getMatriculaPeriodoSituacao().put("PF", "Pendente Financeiramente");
        getMatriculaPeriodoSituacao().put("CO", "Confirmada");
    }

    private static void inicializarDominioProcSeletivoNrOpcoesCurso() {
        getProcSeletivoNrOpcoesCurso().put("3", "3");
        getProcSeletivoNrOpcoesCurso().put("2", "2");
        getProcSeletivoNrOpcoesCurso().put("1", "1");
    }

    private static void inicializarDominioPagarPagoNegociado() {
        getPagarPagoNegociado().put("AP", "Contas a pagar");
        getPagarPagoNegociado().put("PA", "Contas pagas");
        getPagarPagoNegociado().put("NE", "Negociado");
        getPagarPagoNegociado().put("CF", "Cancelado");
        //getPagarPagoNegociado().put("PP", "Contas parcialmente pagas");
    }

    private static void inicializarDominioReceberRecebidoNegociado() {
        getReceberRecebidoNegociado().put("AR", "A Receber");
        getReceberRecebidoNegociado().put("RE", "Recebido");
    }

    private static void inicializarDominioCreditoDebito() {
        getCreditoDebito().put("DE", "Débito");
        getCreditoDebito().put("CE", "Crédito");
    }

    private static void inicializarDominioTipoRespostaQuestaoAvaliacao() {
        getTipoRespostaQuestaoAvaliacao().put("NU", "Númerica");
        getTipoRespostaQuestaoAvaliacao().put("OV", "Selecionar Opção Válida");
    }

    private static void inicializarDominioTipoCalculoResultadoQuestaoAvaliacao() {
        getTipoCalculoResultadoQuestaoAvaliacao().put("CM", "Calcular Média");
        getTipoCalculoResultadoQuestaoAvaliacao().put("SO", "Somar");
        getTipoCalculoResultadoQuestaoAvaliacao().put("CO", "Contar");
    }

    private static void inicializarDominioSituacaoCompra() {
        getSituacaoCompra().put("QI", "Quitado");
        getSituacaoCompra().put("EA", "Em Aberto");
    }

    private static void inicializarDominioSituacaoCotacao() {
        getSituacaoCotacao().put("FI", "Finalizada");
        getSituacaoCotacao().put("EX", "Execução");
    }

    private static void inicializarDominioSituacaoPgtServicoAcademico() {
        getSituacaoPgtServicoAcademico().put("QI", "Quitado");
        getSituacaoPgtServicoAcademico().put("AB", "Em Aberto");
    }

    private static void inicializarDominioSituacaoProtocolo() {
        getSituacaoProtocolo().put("PR", "Pronto para Retirada");
        getSituacaoProtocolo().put("AP", "Aguardando Pagamento");
        getSituacaoProtocolo().put("FI", "Finalizado - Indeferido");
        getSituacaoProtocolo().put("FD", "Finalizado - Deferido");
        getSituacaoProtocolo().put("EX", "Em Execução");
        getSituacaoProtocolo().put("PE", "Novo - Aguardando Início Execução");
        getSituacaoProtocolo().put("IS", "Isento");
        getSituacaoProtocolo().put("AT", "Atrasado");
        getSituacaoProtocolo().put("AD", "Atrasado Departamento");
        getSituacaoProtocolo().put("ED", "Aguardando Execução no Departamento");
        getSituacaoProtocolo().put("AA", "Aguardando Tramite Departamento Anterior");
    }

    private static void inicializarDominioFormaPagamentoPgtServicoAcademico() {
        getFormaPagamentoPgtServicoAcademico().put("DB", "Deposito Bancário");
        getFormaPagamentoPgtServicoAcademico().put("CH", "Cheque");
        getFormaPagamentoPgtServicoAcademico().put("DI", "Em Dinheiro");
    }

    private static void inicializarDominioTipoDestinatarioPgtServicoAcademico() {
        getTipoDestinatarioPgtServicoAcademico().put("PR", "Professor");
        getTipoDestinatarioPgtServicoAcademico().put("MC", "Menbro Comunidade");
        getTipoDestinatarioPgtServicoAcademico().put("AL", "Aluno");
        getTipoDestinatarioPgtServicoAcademico().put("FU", "Funcionário");
    }

    private static void inicializarDominioSituacaoFinanceiraProtocolo() {
        getSituacaoFinanceiraProtocolo().put("PG", "Pago");
        getSituacaoFinanceiraProtocolo().put("PE", "Aguardando Pagamento");
        getSituacaoFinanceiraProtocolo().put("IS", "Isento");
        getSituacaoFinanceiraProtocolo().put("AP", "Aguardando Autorização Pagamento");
        getSituacaoFinanceiraProtocolo().put("CA", "Cancelado Financeiramente");
    }

    private static void inicializarDominioTipoDestinacaoCustosPrevisaoCustos() {
        getTipoDestinacaoCustosPrevisaoCustos().put("CU", "Curso");
        getTipoDestinacaoCustosPrevisaoCustos().put("EV", "Evento");
        getTipoDestinacaoCustosPrevisaoCustos().put("CE", "Curso de Extensão");
    }

    private static void inicializarDominioSituacaoProcessoSeletivo() {
        getSituacaoProcessoSeletivo().put("PF", "Pendente Financeiramente");
        getSituacaoProcessoSeletivo().put("CO", "Confirmada");
    }

    private static void inicializarDominioTipoDisciplinaProcessoSel() {
        getTipoDisciplinaProcessoSel().put("OP", "Opcional");
        getTipoDisciplinaProcessoSel().put("AP", "Aptidão");
        getTipoDisciplinaProcessoSel().put("NO", "Normal");
    }

    private static void inicializarDominioTipoUnidadeProduto() {
        getTipoUnidadeProduto().put("CA", "Caixa");
        getTipoUnidadeProduto().put("UN", "Unitário");
        getTipoUnidadeProduto().put("KL", "Kilograma");
    }

    private static void inicializarDominioSituacaoSolicitacaoCompra() {
        getSituacaoSolicitacaoCompra().put("DE", "Deferido");
        getSituacaoSolicitacaoCompra().put("IN", "Indeferido");
        getSituacaoSolicitacaoCompra().put("AG", "Aguardando Deferimento");
    }

    private static void inicializarDominioQtdIrmaosProcSel() {
        getQtdIrmaosProcSel().put("UM", "Um");
        getQtdIrmaosProcSel().put("DO", "Dois");
        getQtdIrmaosProcSel().put("TR", "Três");
        getQtdIrmaosProcSel().put("QM", "Quatro ou mais");
        getQtdIrmaosProcSel().put("NR", "Não resposta");
        getQtdIrmaosProcSel().put("NE", "Nenhum");
    }

    private static void inicializarDominioQtdFilhosTemProcSel() {
        getQtdFilhosTemProcSel().put("UM", "Um");
        getQtdFilhosTemProcSel().put("DO", "Dois");
        getQtdFilhosTemProcSel().put("TR", "Três");
        getQtdFilhosTemProcSel().put("QM", "Quatro ou mais");
        getQtdFilhosTemProcSel().put("NR", "Não resposta");
        getQtdFilhosTemProcSel().put("NE", "Nenhum");
    }

    private static void inicializarDominioResidenteEmProcSel() {
        getResidenteEmProcSel().put("SO", "Sozinho");
        getResidenteEmProcSel().put("PA", "Pais");
        getResidenteEmProcSel().put("EF", "Esposo(a) e/ou filho(s)");
        getResidenteEmProcSel().put("OP", "Outros parentes");
        getResidenteEmProcSel().put("PE", "Pensão");
        getResidenteEmProcSel().put("AM", "Amigos");
        getResidenteEmProcSel().put("NR", "Não resposta");

    }

    private static void inicializarDominioHrsTrabalhoSemanaProcSel() {
        getHrsTrabalhoSemanaProcSel().put("20", "Até 20 horas");
        getHrsTrabalhoSemanaProcSel().put("30", "De 21 a 30 horas");
        getHrsTrabalhoSemanaProcSel().put("40", "De 31 a 40 horas");
        getHrsTrabalhoSemanaProcSel().put("M4", "Mais de 40 horas");
        getHrsTrabalhoSemanaProcSel().put("NT", "Não trabalho");
        getHrsTrabalhoSemanaProcSel().put("NR", "Não resposta");

    }

    private static void inicializarDominioRamoAtividadeProcSel() {
        getRamoAtividadeProcSel().put("AI", "Atividade informal");
        getRamoAtividadeProcSel().put("IN", "Indústria");
        getRamoAtividadeProcSel().put("EP", "Emprego público");
        getRamoAtividadeProcSel().put("ED", "Educação");
        getRamoAtividadeProcSel().put("AG", "Agropecuaria");
        getRamoAtividadeProcSel().put("CO", "Comércio");
        getRamoAtividadeProcSel().put("IF", "Informática");
    }

    private static void inicializarDominioRendaMensalProcSel() {
        getRendaMensalProcSel().put("A3", "Até 3");
        getRendaMensalProcSel().put("10", "De 3 até 10");
        getRendaMensalProcSel().put("20", "De 10 até 20");
        getRendaMensalProcSel().put("30", "De 20 até 30");
        getRendaMensalProcSel().put("MA", "Mais de 30");
        getRendaMensalProcSel().put("NT", "Não tenho renda");
        getRendaMensalProcSel().put("NR", "Não resposta");

    }

    private static void inicializarDominioSexo() {
        getSexo().put("F", "Feminino");
        getSexo().put("M", "Masculino");
    }

    private static void inicializarDominioEstado() {
        getEstado().put("BA", "BA");
        getEstado().put("RS", "RS");
        getEstado().put("RR", "RR");
        getEstado().put("RO", "RO");
        getEstado().put("RN", "RN");
        getEstado().put("RJ", "RJ");
        getEstado().put("CE", "CE");
        getEstado().put("AP", "AP");
        getEstado().put("MT", "MT");
        getEstado().put("MS", "MS");
        getEstado().put("PR", "PR");
        getEstado().put("GO", "GO");
        getEstado().put("AM", "AM");
        getEstado().put("AL", "AL");
        getEstado().put("SP", "SP");
        getEstado().put("DF", "DF");
        getEstado().put("PI", "PI");
        getEstado().put("AC", "AC");
        getEstado().put("MG", "MG");
        getEstado().put("ES", "ES");
        getEstado().put("PE", "PE");
        getEstado().put("SE", "SE");
        getEstado().put("SC", "SC");
        getEstado().put("MA", "MA");
        getEstado().put("PB", "PB");
        getEstado().put("PA", "PA");
        getEstado().put("TO", "TO");
    }

    private static void inicializarDominioSimNao() {
        getSimNao().put("S", "Sim");
        getSimNao().put("N", "Não");
    }

    private static void inicializarDominioEscolaridade() {
        getEscolaridade().put("PR", "Primário");
        getEscolaridade().put("BA", "Básico");
        getEscolaridade().put("DO", "Doutorado");
        getEscolaridade().put("PD", "Pós-Doutorado");
        getEscolaridade().put("SU", "Superior");
        getEscolaridade().put("PL", "Pós-Graduação Lato-Senso (MBA)");
        getEscolaridade().put("ME", "Mestrado");
        getEscolaridade().put("TE", "Técnico");
    }

    private static void inicializarDominioEstadoCivil() {
        getEstadoCivil().put("A", "Amasiado(a)");
        getEstadoCivil().put("C", "Casado(a)");
        getEstadoCivil().put("D", "Divorciado(a)");
        getEstadoCivil().put("S", "Solteiro(a)");
        getEstadoCivil().put("U", "União Estável");
        getEstadoCivil().put("V", "Viúvo(a)");
        getEstadoCivil().put("E", "Separado(a)");
        getEstadoCivil().put("Q", "Desquitado(a)");
    }

    private static void inicializarDominioNivelAcesso() {
        getNivelAcesso().put("(0)(9)(1)", "Incluir e Consultar");
        getNivelAcesso().put("(0)", "Consultar");
        getNivelAcesso().put("(12)", "Relatorio");
        getNivelAcesso().put("(2)", "Alterar");
        getNivelAcesso().put("(0)(1)(2)(9)(12)", "Total (Sem Excluir)");
        getNivelAcesso().put("(0)(1)(2)(3)(9)(12)", "Total");
        getNivelAcesso().put("(3)", "Excluir");
        getNivelAcesso().put("(1)(9)", "Incluir");
    }

    private static void inicializarDominioTipoEmpresa() {
        getTipoEmpresa().put("FI", "Física");
        getTipoEmpresa().put("JU", "Jurídica");
    }

    private static void inicializarDominioSituacao() {
        getSituacao().put("RE", "Recebido");
        getSituacao().put("TR", "Em Tramite");
    }

    private static void inicializarDominioTipoCorrespondencia() {
        getTipoCorrespondencia().put("UR", "Urgente Rápido");
        getTipoCorrespondencia().put("UG", "Urgente");
        getTipoCorrespondencia().put("CO", "Convencional");
    }

    private static void inicializarDominioSituacaoSolicitacaoPgtoServicoAcademico() {
        getSituacaoSolicitacaoPgtoServicoAcademico().put("DE", "Deferido");
        getSituacaoSolicitacaoPgtoServicoAcademico().put("IN", "Indeferido");
        getSituacaoSolicitacaoPgtoServicoAcademico().put("AG", "Aguardando Deferimento");
    }

    private static void inicializarDominioTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic() {
        getTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic().put("PR", "Professor");
        getTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic().put("MC", "Menbro Comunidade");
        getTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic().put("AL", "Aluno");
        getTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic().put("FU", "Funcionário");
    }

    private static void inicializarDominioTipoInscricaoEvento() {
        getTipoInscricaoEvento().put("PC", "Inscrição nas Palestras e nos Cursos");
        getTipoInscricaoEvento().put("EC", "Inscrição no Evento e nos Cursos");
        getTipoInscricaoEvento().put("IE", "Inscrição no Evento");
    }

    private static void inicializarDominioFormasInscricaoEvento() {
        getFormasInscricaoEvento().put("PR", "Presencial");
        getFormasInscricaoEvento().put("IN", "Internet");
        getFormasInscricaoEvento().put("PI", "Presencial e Internet");
    }

    private static void inicializarDominioTipoSubmissaoEvento() {
        getTipoSubmissaoEvento().put("SI", "Submissão Independente");
        getTipoSubmissaoEvento().put("SP", "Submissão Paga");
        getTipoSubmissaoEvento().put("VI", "Submissão Vinculada a Inscrição");
    }

    private static void inicializarDominioSomaRendaFamiliaProcSel() {
        getSomaRendaFamiliaProcSel().put("A3", "Até 3");
        getSomaRendaFamiliaProcSel().put("10", "De 3 até 10");
        getSomaRendaFamiliaProcSel().put("20", "De 10 até 20");
        getSomaRendaFamiliaProcSel().put("30", "De 20 até 30");
        getSomaRendaFamiliaProcSel().put("MA", "Mais de 30");
        getSomaRendaFamiliaProcSel().put("NR", "Não resposta");

    }

    private static void inicializarDominioSituacaoFinanceiraEvento() {
        getSituacaoFinanceiraEvento().put("QI", "Quitado");
        getSituacaoFinanceiraEvento().put("EA", "Em Aberto");
    }

    private static void inicializarDominioParticipacaoVidaEconomicaFamiliaProcSel() {
        getParticipacaoVidaEconomicaFamiliaProcSel().put("TC", "Trabalho e contribuo com a renda fam.");
        getParticipacaoVidaEconomicaFamiliaProcSel().put("NR", "Não resposta");
        getParticipacaoVidaEconomicaFamiliaProcSel().put("EI", "Economicamente independente");
        getParticipacaoVidaEconomicaFamiliaProcSel().put("TF", "Trabalho e recebo ajuda familiar");
        getParticipacaoVidaEconomicaFamiliaProcSel().put("DF", "Dependente da minha familia");
        getParticipacaoVidaEconomicaFamiliaProcSel().put("TP", "Trabalho e sou o principal responsável");
    }

    private static void inicializarDominioTipoPessoaInscricaoEvento() {
        getTipoPessoaInscricaoEvento().put("PR", "Professor");
        getTipoPessoaInscricaoEvento().put("AL", "Aluno");
        getTipoPessoaInscricaoEvento().put("CO", "Comunidade");
        getTipoPessoaInscricaoEvento().put("FU", "Funcionário");
    }

    private static void inicializarDominioComoSeManterDuranteCursoProcSel() {
        getComoSeManterDuranteCursoProcSel().put("RF", "Recursos da familia");
        getComoSeManterDuranteCursoProcSel().put("AO", "Ajuda dos outros");
        getComoSeManterDuranteCursoProcSel().put("BE", "Bolsa de estudos");
        getComoSeManterDuranteCursoProcSel().put("NR", "Não resposta");
        getComoSeManterDuranteCursoProcSel().put("TR", "Trabalhando");
        getComoSeManterDuranteCursoProcSel().put("OF", "Outras fontes de renda");
    }

    private static void inicializarDominioTipoPalestraPalestraEvento() {
        getTipoPalestraPalestraEvento().put("DE", "Debate");
        getTipoPalestraPalestraEvento().put("EC", "Exposição de Case");
        getTipoPalestraPalestraEvento().put("MR", "Mesa Redonda");
        getTipoPalestraPalestraEvento().put("PA", "Palestra");
        getTipoPalestraPalestraEvento().put("VI", "Virtual");
    }

    private static void inicializarDominioGrauEscolaridadePaiProcSel() {
        getGrauEscolaridadePaiProcSel().put("EM", "Ensino Médio");
        getGrauEscolaridadePaiProcSel().put("8S", "Ensino Fundamental 5° a 8° série");
        getGrauEscolaridadePaiProcSel().put("NR", "Não resposta");
        getGrauEscolaridadePaiProcSel().put("4S", "Ensino Fundamental 1° a 4° série");
        getGrauEscolaridadePaiProcSel().put("NE", "Nenhuma Escolaridade");
        getGrauEscolaridadePaiProcSel().put("ES", "Ensino Superior");
    }

    private static void inicializarDominioSituacaoCursoExtensao() {
        getSituacaoCursoExtensao().put("MA", "Matrícula");
        getSituacaoCursoExtensao().put("IN", "Inscrito");
    }

    private static void inicializarDominioGrauEscolaridadeMaeProcSel() {
        getGrauEscolaridadeMaeProcSel().put("EM", "Ensino Médio");
        getGrauEscolaridadeMaeProcSel().put("8S", "Ensino Fundamental 5° a 8° série");
        getGrauEscolaridadeMaeProcSel().put("NR", "Não resposta");
        getGrauEscolaridadeMaeProcSel().put("4S", "Ensino Fundamental 1° a 4° série");
        getGrauEscolaridadeMaeProcSel().put("NE", "Nenhuma Escolaridade");
        getGrauEscolaridadeMaeProcSel().put("ES", "Ensino Superior");
    }

    private static void inicializarDominioSituacaoFinanceiraExtensao() {
        getSituacaoFinanceiraExtensao().put("MA", "Matrícula em Aberto");
        getSituacaoFinanceiraExtensao().put("IA", "Inscrição em Aberto");
        getSituacaoFinanceiraExtensao().put("MQ", "Matrícula Quitada");
        getSituacaoFinanceiraExtensao().put("IQ", "Inscrição Quitada");
    }

    private static void inicializarDominioTipoEscolaCursouEnsinoMedioProcSel() {
        getTipoEscolaCursouEnsinoMedioProcSel().put("MA", "Maior parte em escola particular");
        getTipoEscolaCursouEnsinoMedioProcSel().put("MU", "Maior parte em escola pública");
        getTipoEscolaCursouEnsinoMedioProcSel().put("UA", "Metade pública e metade particular");
        getTipoEscolaCursouEnsinoMedioProcSel().put("PA", "Todo em escola particular");
        getTipoEscolaCursouEnsinoMedioProcSel().put("NR", "Não resposta");
        getTipoEscolaCursouEnsinoMedioProcSel().put("PU", "Todo em escola pública");
    }

    private static void inicializarDominioAnoConclusaoEnsinoMedioProcSel() {
        getAnoConclusaoEnsinoMedioProcSel().put("NA", "Neste ano");
        getAnoConclusaoEnsinoMedioProcSel().put("TA", "Três anos atrás");
        getAnoConclusaoEnsinoMedioProcSel().put("DA", "Dois anos atrás");
        getAnoConclusaoEnsinoMedioProcSel().put("UA", "Um ano atrás");
        getAnoConclusaoEnsinoMedioProcSel().put("NR", "Não resposta");
        getAnoConclusaoEnsinoMedioProcSel().put("QA", "Quatro ou mais anos atrás");
    }

    private static void inicializarDominioQtdFaculdadePrestouVestib() {
        getQtdFaculdadePrestouVestib().put("MU", "Mais uma");
        getQtdFaculdadePrestouVestib().put("MT", "Mais três");
        getQtdFaculdadePrestouVestib().put("NI", "Somente nessa Instituição");
        getQtdFaculdadePrestouVestib().put("MQ", "Quatro ou mais");
        getQtdFaculdadePrestouVestib().put("NR", "Não resposta");
        getQtdFaculdadePrestouVestib().put("MD", "Mais duas");
    }

    private static void inicializarDominioIniciouCursoSuperiorProcSel() {
        getIniciouCursoSuperiorProcSel().put("SA", "Sim, mas abandonei");
        getIniciouCursoSuperiorProcSel().put("CA", "Sim, já conclui e abandonei outro");
        getIniciouCursoSuperiorProcSel().put("NA", "Não");
        getIniciouCursoSuperiorProcSel().put("EC", "Sim, estou cursando");
        getIniciouCursoSuperiorProcSel().put("NR", "Não resposta");
        getIniciouCursoSuperiorProcSel().put("SC", "Sim, já conclui");
    }

    private static void inicializarDominioMotivoEscolhaCursoProcSel() {
        getMotivoEscolhaCursoProcSel().put("CS", "Possibilidade de contribuir p/ sociedade");
        getMotivoEscolhaCursoProcSel().put("OM", "Outros motivos");
        getMotivoEscolhaCursoProcSel().put("NR", "Não resposta");
        getMotivoEscolhaCursoProcSel().put("RF", "Maior retorno financeiro");
        getMotivoEscolhaCursoProcSel().put("PS", "Prestígio social da profissão");
        getMotivoEscolhaCursoProcSel().put("IF", "Influência da familia e/ou de terceiros");
        getMotivoEscolhaCursoProcSel().put("VO", "Vocação");
        getMotivoEscolhaCursoProcSel().put("OT", "Maiores oportunidades de trabalho");
        getMotivoEscolhaCursoProcSel().put("TA", "Já trabalha na área");
        getMotivoEscolhaCursoProcSel().put("CV", "Baixa relação candidato/vaga");
    }

    private static void inicializarDominioTipoProfessorProfessorCursoExtensao() {
        getTipoProfessorProfessorCursoExtensao().put("PR", "Professor");
        getTipoProfessorProfessorCursoExtensao().put("AL", "Aluno");
        getTipoProfessorProfessorCursoExtensao().put("CO", "Contrato");
        getTipoProfessorProfessorCursoExtensao().put("FU", "Funcionário");
    }

    private static void inicializarDominioTipoInscricaoInscricaoCursoExtensao() {
        getTipoInscricaoInscricaoCursoExtensao().put("PR", "Professor");
        getTipoInscricaoInscricaoCursoExtensao().put("MC", "Membro Comunidade");
        getTipoInscricaoInscricaoCursoExtensao().put("AL", "Aluno");
        getTipoInscricaoInscricaoCursoExtensao().put("FU", "Funcionário");
    }

    private static void inicializarDominioTipoCustoCFGCustoAdministrativo() {
        getTipoCustoCFGCustoAdministrativo().put("UE", "Unidade de Ensino");
        getTipoCustoCFGCustoAdministrativo().put("CE", "Curso Específico Unidade de Ensino");
    }

    private static void inicializarDominioSituacaoDebitoFuncionarioFinanceiro() {
        getSituacaoDebitoFuncionarioFinanceiro().put("QI", "Quitado");
        getSituacaoDebitoFuncionarioFinanceiro().put("AT", "Atrasado");
        getSituacaoDebitoFuncionarioFinanceiro().put("AB", "Aberto");
    }

    private static void inicializarDominioTipoParceiroParceiro() {
        getTipoParceiroParceiro().put("PR", "Privado");
        getTipoParceiroParceiro().put("ON", "ONG");
        getTipoParceiroParceiro().put("PU", "Público");
        getTipoParceiroParceiro().put("SI", "Sindicato");
    }

    private static void inicializarDominioTipoPublicacaoPublicacaoPesquisa() {
        getTipoPublicacaoPublicacaoPesquisa().put("LI", "Livro");
        getTipoPublicacaoPublicacaoPesquisa().put("CL", "Capítulo Livro");
        getTipoPublicacaoPublicacaoPesquisa().put("AR", "Artigo");
        getTipoPublicacaoPublicacaoPesquisa().put("MO", "Monografia");
    }

    private static void inicializarDominioAlunoFuncionarioCandidatoParceiro() {
        getAlunoFuncionarioCandidatoParceiro().put("AL", "Aluno");
        getAlunoFuncionarioCandidatoParceiro().put("FU", "Funcionário");
        getAlunoFuncionarioCandidatoParceiro().put("PA", "Parceiro");
        getAlunoFuncionarioCandidatoParceiro().put("CA", "Candidato");
        getAlunoFuncionarioCandidatoParceiro().put("RF", "Responsável Financeiro");
        getAlunoFuncionarioCandidatoParceiro().put("RE", "Requerente");
        getAlunoFuncionarioCandidatoParceiro().put("FO", "Fornecedor");
    }

    private static void inicializarDominioConhecimentoProcessoSeletivo() {
        getConhecimentoProcessoSeletivo().put("TV", "TV");
        getConhecimentoProcessoSeletivo().put("EC", "Através da escola/cursinhos");
        getConhecimentoProcessoSeletivo().put("AI", "Alunos da Instituição");
        getConhecimentoProcessoSeletivo().put("NR", "Não resposta");
        getConhecimentoProcessoSeletivo().put("RA", "Rádio");
        getConhecimentoProcessoSeletivo().put("OB", "Outdoor/busdoor");
        getConhecimentoProcessoSeletivo().put("CD", "Folhetos ou cartazes de divulgação");
        getConhecimentoProcessoSeletivo().put("FC", "Feiras, congressos");
        getConhecimentoProcessoSeletivo().put("OU", "Outros");
        getConhecimentoProcessoSeletivo().put("JO", "Jornais");
        getConhecimentoProcessoSeletivo().put("PI", "Professores da Intituição");
    }

    private static void inicializarDominioTipoPesqiosadorPublicacao() {
        getTipoPesqiosadorPublicacao().put("PR", "Professor");
        getTipoPesqiosadorPublicacao().put("AL", "Aluno");
    }

    private static void inicializarDominioPqPrestarVestibularInstituicao() {
        getPqPrestarVestibularInstituicao().put("IP", "Incerteza ingressa universidade pública");
        getPqPrestarVestibularInstituicao().put("NV", "Pelo número de vagas oferecidas");
        getPqPrestarVestibularInstituicao().put("PF", "Influência dos pais e familiares");
        getPqPrestarVestibularInstituicao().put("AI", "Por ter amigos e parentes na Instituição");
        getPqPrestarVestibularInstituicao().put("NR", "Não resposta");
        getPqPrestarVestibularInstituicao().put("PT", "Por ser próxima do meu trabalho");
        getPqPrestarVestibularInstituicao().put("PR", "Por ser próxima da minha residência");
        getPqPrestarVestibularInstituicao().put("CD", "Pelo conceito que ela desfruta");
        getPqPrestarVestibularInstituicao().put("OU", "Outros");
    }

    private static void inicializarDominioTipoPesquisadorPesquisadorLinhaPesquisa() {
        getTipoPesquisadorPesquisadorLinhaPesquisa().put("PR", "Professor");
        getTipoPesquisadorPesquisadorLinhaPesquisa().put("CO", "Convidado");
        getTipoPesquisadorPesquisadorLinhaPesquisa().put("Al", "Aluno");
    }

    private static void inicializarDominioEsperaCursoSuperiorProcSel() {
        getEsperaCursoSuperiorProcSel().put("DC", "Ter apenas um diploma de curso superior");
        getEsperaCursoSuperiorProcSel().put("VP", "Formação ciêntifica volta p/ pesquisa");
        getEsperaCursoSuperiorProcSel().put("OU", "Outros");
        getEsperaCursoSuperiorProcSel().put("VM", "Formação profissional voltada p/ mercado");
        getEsperaCursoSuperiorProcSel().put("ME", "Melhorar no emprego");
        getEsperaCursoSuperiorProcSel().put("CG", "Aquisição de cultural geral");
        getEsperaCursoSuperiorProcSel().put("NR", "Não resposta");
        getEsperaCursoSuperiorProcSel().put("IE", "Desenvolver um espirito empreendedor");
    }

    private static void inicializarDominioSituacaoPesquisadorLinhaPesquisa() {
        getSituacaoPesquisadorLinhaPesquisa().put("IN", "Inativo");
        getSituacaoPesquisadorLinhaPesquisa().put("AT", "Ativo");
    }

    private static void inicializarDominioQtdLivroLeituraAnoProcSel() {
        getQtdLivroLeituraAnoProcSel().put("MA", "Mais de 10");
        getQtdLivroLeituraAnoProcSel().put("A2", "De 1 a 2");
        getQtdLivroLeituraAnoProcSel().put("10", "De 6 a 10");
        getQtdLivroLeituraAnoProcSel().put("NR", "Não resposta");
        getQtdLivroLeituraAnoProcSel().put("NE", "Nenhum");
        getQtdLivroLeituraAnoProcSel().put("A5", "De 3 a 5");
    }

    private static void inicializarDominioFormaPagamentoPgtoServicoAcademico() {
        getFormaPagamentoPgtoServicoAcademico().put("ED", "Em Dinheiro");
        getFormaPagamentoPgtoServicoAcademico().put("DB", "Deposito Bancário");
        getFormaPagamentoPgtoServicoAcademico().put("CH", "Cheque");
    }

    private static void inicializarDominioExisteMicrocomputadorResProcSel() {
        getExisteMicrocomputadorResProcSel().put("NN", "Não, e eu nunca utilizo microcomputador");
        getExisteMicrocomputadorResProcSel().put("SB", "Sim, e eu o utilizo bastante");
        getExisteMicrocomputadorResProcSel().put("ST", "Não, mas utilizo no meu ambiente trab");
        getExisteMicrocomputadorResProcSel().put("NR", "Não resposta");
        getExisteMicrocomputadorResProcSel().put("SP", "Sim, mas eu pouco o utilizo");
        getExisteMicrocomputadorResProcSel().put("SN", "Sim, mas eu nunca o utilizo");
    }

    private static void inicializarDominioTipoLivroLeituraProcSel() {
        getTipoLivroLeituraProcSel().put("LT", "Livros técnicos");
        getTipoLivroLeituraProcSel().put("OU", "Outros");
        getTipoLivroLeituraProcSel().put("NF", "Obras literárias de não ficção");
        getTipoLivroLeituraProcSel().put("AA", "Livros de auto-ajuda");
        getTipoLivroLeituraProcSel().put("OF", "Obras literárias de ficção");
    }

    private static void inicializarDominioMeioAtualizacaoAcontecimentoProcSel() {
        getMeioAtualizacaoAcontecimentoProcSel().put("IN", "Internet");
        getMeioAtualizacaoAcontecimentoProcSel().put("JO", "Jornais");
        getMeioAtualizacaoAcontecimentoProcSel().put("TV", "TV");
        getMeioAtualizacaoAcontecimentoProcSel().put("RE", "Revistas");
        getMeioAtualizacaoAcontecimentoProcSel().put("NR", "Não resposta");
        getMeioAtualizacaoAcontecimentoProcSel().put("RA", "Rádio");
    }

    private static void inicializarDominioLeJornalProcSel() {
        getLeJornalProcSel().put("VS", "Algumas vezes por semana");
        getLeJornalProcSel().put("NU", "Nunca");
        getLeJornalProcSel().put("RR", "Raramente");
        getLeJornalProcSel().put("DI", "Diariamente");
        getLeJornalProcSel().put("NR", "Não resposta");
        getLeJornalProcSel().put("SD", "Somente aos domingos");
    }

    private static void inicializarDominioAssuntoJornalLeituraProcSel() {
        getAssuntoJornalLeituraProcSel().put("TD", "Todos os assuntos");
        getAssuntoJornalLeituraProcSel().put("CA", "Cultura e arte");
        getAssuntoJornalLeituraProcSel().put("PE", "Politica e/ou econômia");
        getAssuntoJornalLeituraProcSel().put("OU", "Outros");
        getAssuntoJornalLeituraProcSel().put("ES", "Esportes");
    }

    private static void inicializarDominioReligiaoProcSel() {
        getReligiaoProcSel().put("CA", "Católica");
        getReligiaoProcSel().put("OU", "Outra");
        getReligiaoProcSel().put("SF", "Sem religião, com fé em Deus");
        getReligiaoProcSel().put("JU", "Judaica");
        getReligiaoProcSel().put("EV", "Evangélica");
        getReligiaoProcSel().put("NR", "Não resposta");
        getReligiaoProcSel().put("NE", "Nenhuma");
        getReligiaoProcSel().put("ES", "Espirita");
    }

    private static void inicializarDominioTipoFormaPagamento() {
        getTipoFormaPagamento().put("DC", "Depósito");
        getTipoFormaPagamento().put("DE", "Cartão Débito");
        getTipoFormaPagamento().put("CA", "Cartão Crédito");
        getTipoFormaPagamento().put("DI", "Dinheiro");
        getTipoFormaPagamento().put("CH", "Cheque");
        getTipoFormaPagamento().put("BO", "Boleto Bancário");
    }

    private static void inicializarDominioCotacaoAutorizadaItemSolicitacaoCompra() {
        getCotacaoAutorizadaItemSolicitacaoCompra().put("DO", "2");
        getCotacaoAutorizadaItemSolicitacaoCompra().put("UM", "1");
        getCotacaoAutorizadaItemSolicitacaoCompra().put("TR", "3");
    }

    private static void inicializarDominioTipoInsentivoInsentivoPesquisa() {
        getTipoInsentivoInsentivoPesquisa().put("MA", "Materiais");
        getTipoInsentivoInsentivoPesquisa().put("PA", "Passagens");
        getTipoInsentivoInsentivoPesquisa().put("BO", "Bolsas");
        getTipoInsentivoInsentivoPesquisa().put("DG", "Despesas Gerais");
    }

    private static void inicializarDominioTipoPesquisadorPublicacao() {
        getTipoPesquisadorPublicacao().put("PR", "Professor");
        getTipoPesquisadorPublicacao().put("AL", "Aluno");
    }

    private static void inicializarDominioTipoPessoaBasicoPessoa() {
        getTipoPessoaBasicoPessoa().put("PR", "Professor");
        getTipoPessoaBasicoPessoa().put("AL", "Aluno");
        getTipoPessoaBasicoPessoa().put("CO", "Comunidade");
        getTipoPessoaBasicoPessoa().put("FU", "Funcionário");
    }

    private static void inicializarDominioSituacaoCursoAcademico() {
        getSituacaoCursoAcademico().put("IN", "Inativo");
        getSituacaoCursoAcademico().put("AT", "Ativo");
    }

    private static void inicializarDominioSituacaoFormacaoAcademica() {
        getSituacaoFormacaoAcademica().put("CU", "Cursando");
        getSituacaoFormacaoAcademica().put("CO", "Concluído");
    }

    private static void inicializarDominioTipoInstFormacaoAcademica() {
        getTipoInstFormacaoAcademica().put("PU", "PÚBLICA");
        getTipoInstFormacaoAcademica().put("PR", "PRIVADA");
    }

    private static void inicializarDominioEscolaridadeFormacaoAcademica() {
        //getEscolaridadeFormacaoAcademica().put("EF", "Ensino Fundamental");
        getEscolaridadeFormacaoAcademica().put("EM", "Ensino Médio");
        getEscolaridadeFormacaoAcademica().put("DO", "Doutorado");
        getEscolaridadeFormacaoAcademica().put("PD", "Pós-Doutorado");
        getEscolaridadeFormacaoAcademica().put("GR", "Graduação");
        getEscolaridadeFormacaoAcademica().put("ME", "Mestrado");
        getEscolaridadeFormacaoAcademica().put("ES", "Especialização");
    }

    private static void inicializarDominioSituacaoTurma() {
        getSituacaoTurma().put("FE", "Fechada");
        getSituacaoTurma().put("AB", "Aberta");
    }

    private static void inicializarDominioCorProcSel() {
        getCorProcSel().put("IN", "Indígena ou de origem indígena");
        getCorProcSel().put("PM", "Pardo(a)/mulato(a)");
        getCorProcSel().put("BR", "Branco");
        getCorProcSel().put("AO", "Amarelo(a)(de origem oriental)");
        getCorProcSel().put("NR", "Não resposta");
        getCorProcSel().put("NE", "Negro");
    }

    private static void inicializarDominioConhecimentoLingInglesaProcSel() {
        getConhecimentoLingInglesaProcSel().put("ER", "Leio, escrevo e falo razoavelmente");
        getConhecimentoLingInglesaProcSel().put("EF", "Leio, escrevo e falo bem");
        getConhecimentoLingInglesaProcSel().put("PN", "Práticamente nulo");
        getConhecimentoLingInglesaProcSel().put("LE", "Leioe escrevo, mas não falo");
        getConhecimentoLingInglesaProcSel().put("LO", "Leio, mas não escrevo nem falo");
        getConhecimentoLingInglesaProcSel().put("NR", "Não resposta");
    }

    private static void inicializarDominioConhecimentoLingEspanholaProcSel() {
        getConhecimentoLingEspanholaProcSel().put("ER", "Leio, escrevo e falo razoavelmente");
        getConhecimentoLingEspanholaProcSel().put("EF", "Leio, escrevo e falo bem");
        getConhecimentoLingEspanholaProcSel().put("PN", "Práticamente nulo");
        getConhecimentoLingEspanholaProcSel().put("LE", "Leio e escrevo, mas não falo");
        getConhecimentoLingEspanholaProcSel().put("LO", "Leio, mas não escrevo nem falo");
        getConhecimentoLingEspanholaProcSel().put("NR", "Não resposta");
    }

    private static void inicializarDominioFreguentaCursoExtracurricularProcSel() {
        getFreguentaCursoExtracurricularProcSel().put("SG", "Sim, de ginástica, dança ou esporte");
        getFreguentaCursoExtracurricularProcSel().put("NA", "Não");
        getFreguentaCursoExtracurricularProcSel().put("CP", "Cursos profissionalizantes");
        getFreguentaCursoExtracurricularProcSel().put("NR", "Não resposta");
        getFreguentaCursoExtracurricularProcSel().put("SA", "Sim, de artes");
        getFreguentaCursoExtracurricularProcSel().put("CI", "Cursos de informática");
        getFreguentaCursoExtracurricularProcSel().put("AA", "Cursos de auto ajuda");
        getFreguentaCursoExtracurricularProcSel().put("OC", "Outros cursos");
        getFreguentaCursoExtracurricularProcSel().put("SM", "Sim, de música");
        getFreguentaCursoExtracurricularProcSel().put("SL", "Sim, de lingua estrangeira");
    }

    private static void inicializarDominioTipoEsportePraticaProcSel() {
        getTipoEsportePraticaProcSel().put("NA", "Natação");
        getTipoEsportePraticaProcSel().put("CQ", "Esporte coletivo de quadra");
        getTipoEsportePraticaProcSel().put("EA", "Esportes aquáticos");
        getTipoEsportePraticaProcSel().put("OM", "Outras modalidades");
        getTipoEsportePraticaProcSel().put("OL", "Lutas olimpicas");
        getTipoEsportePraticaProcSel().put("NR", "Não resposta");
        getTipoEsportePraticaProcSel().put("NP", "Não pratico esporte");
        getTipoEsportePraticaProcSel().put("AT", "Atletismo");
        getTipoEsportePraticaProcSel().put("CC", "Esporte coletivo de campo");
    }

    private static void inicializarDominioMeioTransporteUtilizadoParaIntituicaoProcSel() {
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("CA", "Carona");
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("BI", "Bicicleta");
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("MT", "Moto própria");
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("OU", "Outros");
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("CR", "Carro próprio");
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("NR", "Não resposta");
        getMeioTransporteUtilizadoParaIntituicaoProcSel().put("CO", "Coletivo");
    }

    private static void inicializarDominioModalidadeEnsinoMedioProcSel() {
        getModalidadeEnsinoMedioProcSel().put("MA", "Magistério");
        getModalidadeEnsinoMedioProcSel().put("OM", "Outra modalidade");
        getModalidadeEnsinoMedioProcSel().put("MR", "Ensino médio regular(de três anos)");
        getModalidadeEnsinoMedioProcSel().put("NR", "Não resposta");
        getModalidadeEnsinoMedioProcSel().put("MC", "Ensino médio compacto(supletivo)");
        getModalidadeEnsinoMedioProcSel().put("TP", "Técnico profissionalizante");
    }

    private static void inicializarDominioSituacaoAcademicoCancelamento() {
        getSituacaoAcademicoCancelamento().put("EF", "Efetivado");
        getSituacaoAcademicoCancelamento().put("QU", "Aguardando quitação");
        getSituacaoAcademicoCancelamento().put("AV", "Em avaliação");
    }

    private static void inicializarDominioNivelEducacionalCurso() {
        getNivelEducacionalCurso().put("BA", "Ensino Básico");
        getNivelEducacionalCurso().put("PO", "Pós-graduação");
        getNivelEducacionalCurso().put("SU", "Ensino Superior");
        getNivelEducacionalCurso().put("ME", "Ensino Médio");
    }

    private static void inicializarDominioPeriodicidadeCurso() {
        getPeriodicidadeCurso().put("IN", "Integral");
        getPeriodicidadeCurso().put("AN", "Anual");
        getPeriodicidadeCurso().put("SE", "Semestral");
    }

    private static void inicializarDominioSituacaoProcessoMatricula() {
        getSituacaoProcessoMatricula().put("FI", "Finalizado");
        getSituacaoProcessoMatricula().put("AT", "Ativo - Matrícula");
        getSituacaoProcessoMatricula().put("PR", "Ativo - Pré-Matrícula");
    }

    private static void inicializarDominioDiaSemana() {
        getDiaSemana().put("06", "Sexta");
        getDiaSemana().put("07", "Sábado");
        getDiaSemana().put("01", "Domingo");
        getDiaSemana().put("02", "Segunda");
        getDiaSemana().put("04", "Quarta");
        getDiaSemana().put("03", "Terça");
        getDiaSemana().put("05", "Quinta");
    }

    private static void inicializarDominioSituacaoEntregaDocumentacao() {
        getSituacaoEntregaDocumentacao().put("PE", "Pendente");
        getSituacaoEntregaDocumentacao().put("OK", "OK");
        getSituacaoEntregaDocumentacao().put("EI", "Entregue Incorretamente");
    }

    private static void inicializarDominioTipoItemPlanoFinanceiroAluno() {
        getTipoItemPlanoFinanceiroAluno().put("PD", "Plano de Desconto");
        getTipoItemPlanoFinanceiroAluno().put("CO", "Convênio");
    }

    private static void inicializarDominioEscopoLink() {
        getEscopoLink().put("AM", "Ambos");
        getEscopoLink().put("AL", "Aluno");
        getEscopoLink().put("PA", "Parceiro");
    }

    private static void inicializarDominioTipoJustificativaAlteracaoMatricula() {
        getTipoJustificativaAlteracaoMatricula().put("BA", "Baixa Qualidade Acadêmica");
        getTipoJustificativaAlteracaoMatricula().put("FI", "Infra-estrutura Fraca");
        getTipoJustificativaAlteracaoMatricula().put("FT", "Falta Tempo");
        getTipoJustificativaAlteracaoMatricula().put("DD", "Deficiência Administração");
        getTipoJustificativaAlteracaoMatricula().put("DC", "Desmotivação Carreira");
        getTipoJustificativaAlteracaoMatricula().put("DA", "Deficiência Atendimento");
        getTipoJustificativaAlteracaoMatricula().put("DF", "Dificuldade Financeira");
        getTipoJustificativaAlteracaoMatricula().put("IP", "Insatisfação Professores");
        getTipoJustificativaAlteracaoMatricula().put("TI", "Transferência Interna");
        getTipoJustificativaAlteracaoMatricula().put("NJ", "Não Justificado - Abandono");
        getTipoJustificativaAlteracaoMatricula().put("DE", "Desistência");
        getTipoJustificativaAlteracaoMatricula().put("OU", "Outros");
    }

    private static void inicializarDominioTipoValorConvenio() {
        getTipoValorConvenio().put("PE", "Percentual");
        getTipoValorConvenio().put("VA", "Valor");
    }

    private static void inicializarDominioTipoOrigemDespesa() {
        getTipoOrigemDespesa().put("PA", "Pagamento Aulas");
        getTipoOrigemDespesa().put("DG", "Despesas Gerais");
        getTipoOrigemDespesa().put("CO", "Compra");
        getTipoOrigemDespesa().put("CD", "ContratoDespesas");
        getTipoOrigemDespesa().put("PS", "Pagamento Serviços");
    }

    private static void inicializarDominioTipoOrigemDebitoTerceiros() {
        getTipoOrigemDebitoTerceiros().put("IC", "Inscrição Curso Extensão");
        getTipoOrigemDebitoTerceiros().put("PC", "Parcela Curso");
        getTipoOrigemDebitoTerceiros().put("RD", "Requisição Documento");
        getTipoOrigemDebitoTerceiros().put("IE", "Inscrição Evento");
        getTipoOrigemDebitoTerceiros().put("MB", "Multa Biblioteca");
    }

    private static void inicializarDominioTipoOrigemReceita() {
        getTipoOrigemReceita().put("IN", "Investimento");
        getTipoOrigemReceita().put("DO", "Doação");
        getTipoOrigemReceita().put("DA", "Débito Aluno");
        getTipoOrigemReceita().put("LE", "Locação Espaço");
        getTipoOrigemReceita().put("OU", "Outros");
        getTipoOrigemReceita().put("RB", "Rendimentos Bancários");
        getTipoOrigemReceita().put("DF", "Débito Funcionário");
    }

    private static void inicializarDominioRegimeCurso() {
        getRegimeCurso().put("CR", "Crédito");
        getRegimeCurso().put("SE", "Seriado");
    }

    private static void inicializarDominioRegimeAprovacaoCurso() {
        getRegimeAprovacaoCurso().put("IN", "Integral");
        getRegimeAprovacaoCurso().put("PE", "Período Letivo");
        getRegimeAprovacaoCurso().put("DI", "Disciplina");
    }

    private static void inicializarDominioDiaSemanaDisponibilidadeHorario() {
        getDiaSemanaDisponibilidadeHorario().put("05", "Quinta");
        getDiaSemanaDisponibilidadeHorario().put("06", "Sexta");
        getDiaSemanaDisponibilidadeHorario().put("01", "Domingo");
        getDiaSemanaDisponibilidadeHorario().put("07", "Sábado");
        getDiaSemanaDisponibilidadeHorario().put("02", "Segunda");
        getDiaSemanaDisponibilidadeHorario().put("04", "Quarta");
        getDiaSemanaDisponibilidadeHorario().put("03", "Terça");
    }

    private static void inicializarDominioTipoDisciplinaDisciplina() {
        getTipoDisciplinaDisciplina().put("OP", "Optativa");
        getTipoDisciplinaDisciplina().put("OB", "Obrigatória");
        getTipoDisciplinaDisciplina().put("LG", "Laboratorial Obrigatória");
        getTipoDisciplinaDisciplina().put("LO", "Laboratorial Optativa");
    }

    private static void inicializarDominioTipoDocumentoDocumentacaoMatricula() {
        getTipoDocumentoDocumentacaoMatricula().put("RM", "Registro Militar");
        getTipoDocumentoDocumentacaoMatricula().put("RC", "Registro Civil");
        getTipoDocumentoDocumentacaoMatricula().put("CP", "CPF");
        getTipoDocumentoDocumentacaoMatricula().put("FO", "Foto 3 x 4");
        getTipoDocumentoDocumentacaoMatricula().put("RG", "RG");
        getTipoDocumentoDocumentacaoMatricula().put("CF", "Diploma Formação Anterior");
        getTipoDocumentoDocumentacaoMatricula().put("CE", "Comprovante Endereço");
        getTipoDocumentoDocumentacaoMatricula().put("TE", "Título Eleitoral");
        getTipoDocumentoDocumentacaoMatricula().put("HF", "Histórico Curso Anterior");
        getTipoDocumentoDocumentacaoMatricula().put("CU", "Curriculum");
    }

    private static void inicializarDominioTipoFiliacao() {

        getTipoFiliacao().put("MA", "Mãe");
        getTipoFiliacao().put("PA", "Pai");
        getTipoFiliacao().put("RL", "Responsável Legal");
    }

    private static void inicializarDominioTipoHistoricoHistorico() {
        getTipoHistoricoHistorico().put("ID", "Insento Portador Diploma");
        getTipoHistoricoHistorico().put("AP", "Histórico Transferência");
        getTipoHistoricoHistorico().put("IS", "Insento Notótio Saber");
        getTipoHistoricoHistorico().put("NO", "Normal");
    }

    private static void inicializarDominioSituacaoHistorico() {
        getSituacaoHistorico().put("AP", "Aprovado");
        getSituacaoHistorico().put("RF", "Reprovado falta");
        getSituacaoHistorico().put("RE", "Reprovado");
        getSituacaoHistorico().put("IS", "Isento");
    }

    private static void inicializarDominioSituacaoMatricula() {
        getSituacaoMatricula().put("DE", "Desligado");
        getSituacaoMatricula().put("IN", "Inativa");
        getSituacaoMatricula().put("AT", "Ativa");
        getSituacaoMatricula().put("CA", "Cancelada");
        getSituacaoMatricula().put("JU", "Jubilado");
        getSituacaoMatricula().put("TS", "Transferida");
        getSituacaoMatricula().put("TR", "Trancada");
        getSituacaoMatricula().put("PL", "Pendente de Liberação");
        getSituacaoMatricula().put("ID", "Indeferida");
    }

    private static void inicializarDominioTipoReferenciareferenciaBibliografica() {
        getTipoReferenciareferenciaBibliografica().put("BA", "Básica");
        getTipoReferenciareferenciaBibliografica().put("CO", "Complementar");
    }

    private static void inicializarDominioOpcaoResultadoProcessoSeletivo() {
        // getOpcaoResultadoProcessoSeletivo().put("A3",
        // "Aprovado Terceira Opção");
        // getOpcaoResultadoProcessoSeletivo().put("A2",
        // "Aprovado Segunda Opção");
        getOpcaoResultadoProcessoSeletivo().put("AP", "Aprovado");
        getOpcaoResultadoProcessoSeletivo().put("RE", "Reprovado");
    }

    private static void inicializarDominioSituacaoCampanhaMarketing() {
        getSituacaoCampanhaMarketing().put("AA", "Aguardando Autorização");
        getSituacaoCampanhaMarketing().put("EE", "Em Execução");
        getSituacaoCampanhaMarketing().put("II", "Indeferida");
        getSituacaoCampanhaMarketing().put("FI", "Finalizada");
    }

    private static void inicializarDominioConvenioTipoCobertura() {
        getConvenioTipoCobertura().put("CD", "Colaborador e Dependentes");
        getConvenioTipoCobertura().put("CO", "Colaborador");
    }

    private static void inicializarDominioConvenioSituacao() {
        getConvenioSituacao().put("FI", "Finalizado");
        getConvenioSituacao().put("AA", "Aguardando Aprovação");
        getConvenioSituacao().put("AT", "Ativo");
    }

    private static void inicializarDominioConvenioFormaRecebimentoParceiro() {
        getConvenioFormaRecebimentoParceiro().put("TB", "Transferência Bancária");
        getConvenioFormaRecebimentoParceiro().put("CH", "Cheque");
        getConvenioFormaRecebimentoParceiro().put("TI", "Títulos");
        getConvenioFormaRecebimentoParceiro().put("BO", "Boleto");
    }

    private static void inicializarDominioConfiguracaoFinanceiroTipoCalcJuros() {
        getConfiguracaoFinanceiroTipoCalcJuros().put("SI", "Simples");
        getConfiguracaoFinanceiroTipoCalcJuros().put("CO", "Composto");
    }

    private static void inicializarDominioPlanoFinanceiroCursoTipoCalculoParcela() {
        getPlanoFinanceiroCursoTipoCalculoParcela().put("VF", "Valor Fixo");
        getPlanoFinanceiroCursoTipoCalculoParcela().put("VC", "Valor por Total Créditos");
        getPlanoFinanceiroCursoTipoCalculoParcela().put("FC", "Valor por Forma Cálculo");
        getPlanoFinanceiroCursoTipoCalculoParcela().put("VD", "Valor por Disciplina");
    }

    private static void inicializarDominioPlanoFinanceiroCursoModeloGeracaoParcelas() {
        getPlanoFinanceiroCursoModeloGeracaoParcelas().put("AM", "Gerar Todas as Parcelas no Ato da Matrícula");
        getPlanoFinanceiroCursoModeloGeracaoParcelas().put("VC", "Gerar as Parcelas somente após o Pagto da Matrícula");
        getPlanoFinanceiroCursoModeloGeracaoParcelas().put("FC", "Gerar as Parcelas Mês a Mês");
    }

    private static void inicializarDominioInscricaoFormaAcesso() {
        getInscricaoFormaAcesso().put("VE", "Vestibular");
        getInscricaoFormaAcesso().put("EN", "ENEM");
        getInscricaoFormaAcesso().put("RE", "Redação");
    }

    private static void inicializarDominioInscricaoOpcaoLinguaEstrangeira() {
        getInscricaoOpcaoLinguaEstrangeira().put("IN", "Inglês");
        getInscricaoOpcaoLinguaEstrangeira().put("ES", "Espanhol");
    }

//    private static void inicializarDominioTipoComunicadoInterno() {
//        try {
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_tipoMural");
//            getTipoComunicadoInterno().put("MU", "Mural");
//            
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_tipoSomenteLeitura");
//            getTipoComunicadoInterno().put("LE", "Somente Leitura");
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_tipoExigeResposta");
//            getTipoComunicadoInterno().put("RE", "Exige Resposta");
//        } catch (Exception e) {
//            ////System.out.println("Dominio Erro:" + e.getMessage());
//        }
//    }

    private static void inicializarDominioTipoComunicadoInternoAluno() {
        getTipoComunicadoInternoAluno().put("LE", "Somente Leitura");
        getTipoComunicadoInternoAluno().put("RE", "Exige Resposta");
    }

    private static void inicializarDominioTipoDestinatarioComunicadoInternoAluno() {
        getTipoDestinatarioComunicadoInternoAluno().put("AL", "Aluno");
        getTipoDestinatarioComunicadoInternoAluno().put("PR", "Professor");
        getTipoDestinatarioComunicadoInternoAluno().put("TU", "Turma");
        getTipoDestinatarioComunicadoInternoAluno().put("RL", "Responsável Legal");
        // getTipoDestinatarioComunicadoInternoAluno().put("CO", "Coordenador");

    }

    private static void inicializarDominioTipoDestinatarioComunicadoInternoProfessor() {
        getTipoDestinatarioComunicadoInternoProfessor().put("PR", "Professor");
        getTipoDestinatarioComunicadoInternoProfessor().put("TU", "Turma");
        getTipoDestinatarioComunicadoInternoProfessor().put("DE", "Departamento");
        getTipoDestinatarioComunicadoInternoProfessor().put("RL", "Responsável Legal");
        getTipoDestinatarioComunicadoInternoProfessor().put("AL", "Aluno");
        getTipoDestinatarioComunicadoInternoProfessor().put("TO", "Todas Turmas");
        // getTipoDestinatarioComunicadoInternoProfessor().put("CO",
        // "Coordenadores");
    }

    private static void inicializarDominioTipoDestinatarioComunicadoInternoCoordenador() {
        getTipoDestinatarioComunicadoInternoCoordenador().put("AL", "Aluno");
        getTipoDestinatarioComunicadoInternoCoordenador().put("PR", "Professor");
        getTipoDestinatarioComunicadoInternoCoordenador().put("TU", "Turma");
        getTipoDestinatarioComunicadoInternoCoordenador().put("DE", "Departamento");
        getTipoDestinatarioComunicadoInternoCoordenador().put("CO", "Coordenadores");
    }

//    private static void inicializarDominioTipoDestinatarioComunicadoInterno() {
//        try {
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaAluno");
//            getTipoDestinatarioComunicadoInterno().put("AL", "Aluno");
//            
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaTodosAlunosAtivos");
//            getTipoDestinatarioComunicadoInterno().put("AA", "Alunos Ativos");
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaProfessor");
//            getTipoDestinatarioComunicadoInterno().put("PR", "Professor");
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaFuncionario");
//            getTipoDestinatarioComunicadoInterno().put("FU", "Funcionário");
//
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaCoordenador");
//            getTipoDestinatarioComunicadoInterno().put("CO", "Coordenador");
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaDepartamento");
//            getTipoDestinatarioComunicadoInterno().put("DE", "Departamento");
//        
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaCargo");
//            getTipoDestinatarioComunicadoInterno().put("CA", "Cargo");
//        
////        try {
////            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaArea");
////            getTipoDestinatarioComunicadoInterno().put("AR", "Área Conhecimento");
////        } catch (Exception e) {
////        }        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaTurma");
//            getTipoDestinatarioComunicadoInterno().put("TU", "Turma");
//
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaTurma");
//            getTipoDestinatarioComunicadoInterno().put("TD", "Turma/Disciplina");
//        
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaTodosAlunos");
//            getTipoDestinatarioComunicadoInterno().put("TA", "Todos Alunos");
//        
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaTodosProfessores");
//            getTipoDestinatarioComunicadoInterno().put("TP", "Todos Professores");
//        
//        
//            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ComunicacaoInterna_enviarParaTodaComunidade");
//            getTipoDestinatarioComunicadoInterno().put("TC", "Toda Comunidade");
//            
//            getTipoDestinatarioComunicadoInterno().put("TR", "Todos Coordenadores");
//            getTipoDestinatarioComunicadoInterno().put("TF", "Todos Funcionários");
//            
//
//
//        } catch (Exception e) {
//            ////System.out.println("Dominio Erro:" + e.getMessage());
//        }
//        //getTipoDestinatarioComunicadoInterno().put("CO", "Coordenadores");
//    }

    private static void inicializarDominioPrioridadeComunicadoInterno() {
        getPrioridadeComunicadoInterno().put("BA", "Baixa");
        getPrioridadeComunicadoInterno().put("NO", "Normal");
        getPrioridadeComunicadoInterno().put("AL", "Alta");
    }

    private static void inicializarDominioTipoDespesaFinanceira() {
        getTipoDespesaFinanceira().put("MA", "+");
        getTipoDespesaFinanceira().put("ME", "-");
    }

    private static void inicializarDominioTipoDesconto() {
        getTipoDesconto().put("VE", "Valor Específico");
        getTipoDesconto().put("PO", "Porcentagem");
    }

    private static void inicializarDominioTipoRespostaQuestionario() {
        getTipoRespostaQuestionario().put("ME", "Multipla Escolha");
        getTipoRespostaQuestionario().put("SE", "Simples Escolha");
        getTipoRespostaQuestionario().put("TE", "Textual");
    }

    private static void inicializarDominioSituacaoQuestionario() {
        getSituacaoQuestionario().put("EC", "Em Construção");
        getSituacaoQuestionario().put("AT", "Ativo");
    }

    private static void inicializarDominioTipoRequerimento() {
        getTipoRequerimento().put("CA", "Cancelamento");
        getTipoRequerimento().put("TR", "Trancamento");
        getTipoRequerimento().put("TE", "Transf. Externa");
        getTipoRequerimento().put("TS", "Transf. Saída");
        getTipoRequerimento().put("TI", "Transf. Interna");
        getTipoRequerimento().put("PO", "Portador de Diploma");
        getTipoRequerimento().put("CG", "Colação de Grau");
        getTipoRequerimento().put("OU", "Outros");
    }

    private static void inicializarDominioTipoContasAPagar() {
        getTipoContasAPagar().put("TO", "Todas");
        getTipoContasAPagar().put("PG", "Pagas");
        getTipoContasAPagar().put("EM", "Em Aberto");
        getTipoContasAPagar().put("MA", "Mês Atual");
        //getTipoContasAPagar().put("AV", "A Vencer");
        //getTipoContasAPagar().put("NE", "Negociada");
    }

    private static void inicializarDominioSituacaoContratosDespesas() {
        getSituacaoContratosDespesas().put("IN", "Indeferido");
        getSituacaoContratosDespesas().put("AP", "Aprovado");
        getSituacaoContratosDespesas().put("AA", "Aguardando Aprovação");
    }

    private static void inicializarDominioAtuaComoDocente() {
        getAtuaComoDocente().put("PR", "Contratado");
        getAtuaComoDocente().put("PA", "Autônomo - Não Efetivo");
    }
    
    public static Boolean getTipoDeclaracaoReferenteSecretaria(String tipoDeclaracao) {
        if (tipoDeclaracao.equals("ES") ||
            tipoDeclaracao.equals("EO") ||
            tipoDeclaracao.equals("EN") ||
            tipoDeclaracao.equals("EC") ||
            tipoDeclaracao.equals("LC")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    private static void inicializarDominioTipoDeclaracao() {
        getTipoDeclaracao().put("OT", "Outros");
        getTipoDeclaracao().put("AD", "Advertência");
        getTipoDeclaracao().put("CA", "Cancelamento");
        getTipoDeclaracao().put("CC", "Conclusão de Curso");
        getTipoDeclaracao().put("FR", "Frequência");
        getTipoDeclaracao().put("PE", "Passe Estudantil");
        getTipoDeclaracao().put("TR", "Trancamento");
        getTipoDeclaracao().put("TS", "Transferência de Saída");
        getTipoDeclaracao().put("TE", "Transferência Extena");
        getTipoDeclaracao().put("TI", "Transferência Interna");
        getTipoDeclaracao().put("TU", "Transferência de Unidade");
        getTipoDeclaracao().put("TT", "Transferência de Turma");
        getTipoDeclaracao().put("DI", "Diploma");
        getTipoDeclaracao().put("CE", "Certificado");
        getTipoDeclaracao().put("RD", "Termo Reconhecimento de Dívida");
        getTipoDeclaracao().put("PS", "Processo Seletivo");
        getTipoDeclaracao().put("ES", "Estágio");
        getTipoDeclaracao().put("EO", "Estágio Obrigatório");
        getTipoDeclaracao().put("EN", "Estágio Não Obrigatório");
        getTipoDeclaracao().put("EC", "Estágio Convênio Empresa/Parceiro");
        getTipoDeclaracao().put("CO", "Carta de Cobrança");
        getTipoDeclaracao().put("LD", "Livro Registro de Diploma");
        getTipoDeclaracao().put("AM", "Aulas Ministradas Professor");
        getTipoDeclaracao().put("DQ", "Declaração Anual de Quitação de Débito");
        getTipoDeclaracao().put("AC", "Ata de Colação de Grau");
        //getTipoDeclaracao().put("LC", "Livro de Controle de Registros de Certificados");
    }

    public static void setSituacaoProvisaoDeCusto(Hashtable situacaoProvisaoDeCustoPrm) {
        situacaoProvisaoDeCusto = situacaoProvisaoDeCustoPrm;
    }

    public static Hashtable getSituacaoProvisaoDeCusto() {
        return situacaoProvisaoDeCusto;
    }

    public static Hashtable getContaPagarTipoJuroTipoMulta() {
        return contaPagarTipoJuroTipoMulta;
    }

    public static void setContaPagarTipoJuroTipoMulta(Hashtable contaPagarTipoJuroTipoMulta) {
        Dominios.contaPagarTipoJuroTipoMulta = contaPagarTipoJuroTipoMulta;
    }

    public static Hashtable getInscricaoFormaAcesso() {
        return inscricaoFormaAcesso;
    }

    public static void setInscricaoFormaAcesso(Hashtable aInscricaoFormaAcesso) {
        inscricaoFormaAcesso = aInscricaoFormaAcesso;
    }

    public static Hashtable getInscricaoOpcaoLinguaEstrangeira() {
        return inscricaoOpcaoLinguaEstrangeira;
    }

    public static void setInscricaoOpcaoLinguaEstrangeira(Hashtable aInscricaoOpcaoLinguaEstrangeira) {
        inscricaoOpcaoLinguaEstrangeira = aInscricaoOpcaoLinguaEstrangeira;
    }

    public static void setTipoPublicacaoReferenciaBibliografica(Hashtable tipoPublicacaoReferenciaBibliograficaPrm) {
        tipoPublicacaoReferenciaBibliografica = tipoPublicacaoReferenciaBibliograficaPrm;
    }

    public static Hashtable getTipoPublicacaoReferenciaBibliografica() {
        return tipoPublicacaoReferenciaBibliografica;
    }

    public static void setSituacaoTrancamento(Hashtable situacaoTrancamentoPrm) {
        situacaoTrancamento = situacaoTrancamentoPrm;
    }

    public static Hashtable getSituacaoTrancamento() {
        return situacaoTrancamento;
    }

    public static void setPeriodicidadeBiblioteca(Hashtable periodicidadeBibliotecaPrm) {
        periodicidadeBiblioteca = periodicidadeBibliotecaPrm;
    }

    public static Hashtable getPeriodicidadeBiblioteca() {
        return periodicidadeBiblioteca;
    }

    public static void setTipoExemplarExemplar(Hashtable tipoExemplarExemplarPrm) {
        tipoExemplarExemplar = tipoExemplarExemplarPrm;
    }

    public static Hashtable getTipoExemplarExemplar() {
        return tipoExemplarExemplar;
    }

    public static void setSituacaoEmprestimo(Hashtable situacaoEmprestimoPrm) {
        situacaoEmprestimo = situacaoEmprestimoPrm;
    }

    public static Hashtable getSituacaoEmprestimo() {
        return situacaoEmprestimo;
    }

    public static void setTipoSaidaItemRegistroSaidaAcervo(Hashtable tipoSaidaItemRegistroSaidaAcervoPrm) {
        tipoSaidaItemRegistroSaidaAcervo = tipoSaidaItemRegistroSaidaAcervoPrm;
    }

    public static Hashtable getTipoSaidaItemRegistroSaidaAcervo() {
        return tipoSaidaItemRegistroSaidaAcervo;
    }

    public static void setTipoPublicacaoPublicacao(Hashtable tipoPublicacaoPublicacaoPrm) {
        tipoPublicacaoPublicacao = tipoPublicacaoPublicacaoPrm;
    }

    public static Hashtable getTipoPublicacaoPublicacao() {
        return tipoPublicacaoPublicacao;
    }

    public static void setTipoEntradaAcervo(Hashtable tipoEntradaAcervoPrm) {
        tipoEntradaAcervo = tipoEntradaAcervoPrm;
    }

    public static Hashtable getTipoEntradaAcervo() {
        return tipoEntradaAcervo;
    }

    public static void setSituacaoItemEmprestimo(Hashtable situacaoItemEmprestimoPrm) {
        situacaoItemEmprestimo = situacaoItemEmprestimoPrm;
    }

    public static Hashtable getSituacaoItemEmprestimo() {
        return situacaoItemEmprestimo;
    }

    public static void setEscopoAvaliacaoInstitucional(Hashtable escopoAvaliacaoInstitucionalPrm) {
        escopoAvaliacaoInstitucional = escopoAvaliacaoInstitucionalPrm;
    }

    public static Hashtable getEscopoAvaliacaoInstitucional() {
        return escopoAvaliacaoInstitucional;
    }

    public static void setMatriculaPeriodoSituacao(Hashtable matriculaPeriodoSituacaoPrm) {
        matriculaPeriodoSituacao = matriculaPeriodoSituacaoPrm;
    }

    public static Hashtable getMatriculaPeriodoSituacao() {
        return matriculaPeriodoSituacao;
    }

    public static void setProcSeletivoNrOpcoesCurso(Hashtable procSeletivoNrOpcoesCursoPrm) {
        procSeletivoNrOpcoesCurso = procSeletivoNrOpcoesCursoPrm;
    }

    public static Hashtable getProcSeletivoNrOpcoesCurso() {
        return procSeletivoNrOpcoesCurso;
    }

    public static void setPagarPagoNegociado(Hashtable pagarPagoNegociadoPrm) {
        pagarPagoNegociado = pagarPagoNegociadoPrm;
    }

    public static Hashtable getPagarPagoNegociado() {
        return pagarPagoNegociado;
    }

    public static void setReceberRecebidoNegociado(Hashtable receberRecebidoNegociadoPrm) {
        receberRecebidoNegociado = receberRecebidoNegociadoPrm;
    }

    public static Hashtable getReceberRecebidoNegociado() {
        return receberRecebidoNegociado;
    }

    public static void setCreditoDebito(Hashtable creditoDebitoPrm) {
        creditoDebito = creditoDebitoPrm;
    }

    public static Hashtable getCreditoDebito() {
        return creditoDebito;
    }

    public static void setTipoRespostaQuestaoAvaliacao(Hashtable tipoRespostaQuestaoAvaliacaoPrm) {
        tipoRespostaQuestaoAvaliacao = tipoRespostaQuestaoAvaliacaoPrm;
    }

    public static Hashtable getTipoRespostaQuestaoAvaliacao() {
        return tipoRespostaQuestaoAvaliacao;
    }

    public static Hashtable getAlunoFuncionarioCandidatoParceiro() {
        return alunoFuncionarioCandidatoParceiro;
    }

    public static void setAlunoFuncionarioCandidatoParceiro(Hashtable alunoFuncionarioCandidatoParceiro) {
        Dominios.alunoFuncionarioCandidatoParceiro = alunoFuncionarioCandidatoParceiro;
    }

    public static void setTipoCalculoResultadoQuestaoAvaliacao(Hashtable tipoCalculoResultadoQuestaoAvaliacaoPrm) {
        tipoCalculoResultadoQuestaoAvaliacao = tipoCalculoResultadoQuestaoAvaliacaoPrm;
    }

    public static Hashtable getTipoCalculoResultadoQuestaoAvaliacao() {
        return tipoCalculoResultadoQuestaoAvaliacao;
    }

    public static void setSituacaoCompra(Hashtable situacaoCompraPrm) {
        situacaoCompra = situacaoCompraPrm;
    }

    public static Hashtable getSituacaoCompra() {
        return situacaoCompra;
    }

    public static void setSituacaoCotacao(Hashtable situacaoCotacaoPrm) {
        situacaoCotacao = situacaoCotacaoPrm;
    }

    public static Hashtable getSituacaoCotacao() {
        return situacaoCotacao;
    }

    public static void setSituacaoPgtServicoAcademico(Hashtable situacaoPgtServicoAcademicoPrm) {
        situacaoPgtServicoAcademico = situacaoPgtServicoAcademicoPrm;
    }

    public static Hashtable getSituacaoPgtServicoAcademico() {
        return situacaoPgtServicoAcademico;
    }

    public static void setSituacaoProtocolo(Hashtable situacaoProtocoloPrm) {
        situacaoProtocolo = situacaoProtocoloPrm;
    }

    public static Hashtable getSituacaoProtocolo() {
        return situacaoProtocolo;
    }

    public static void setFormaPagamentoPgtServicoAcademico(Hashtable formaPagamentoPgtServicoAcademicoPrm) {
        formaPagamentoPgtServicoAcademico = formaPagamentoPgtServicoAcademicoPrm;
    }

    public static Hashtable getFormaPagamentoPgtServicoAcademico() {
        return formaPagamentoPgtServicoAcademico;
    }

    public static void setTipoDestinatarioPgtServicoAcademico(Hashtable tipoDestinatarioPgtServicoAcademicoPrm) {
        tipoDestinatarioPgtServicoAcademico = tipoDestinatarioPgtServicoAcademicoPrm;
    }

    public static Hashtable getTipoDestinatarioPgtServicoAcademico() {
        return tipoDestinatarioPgtServicoAcademico;
    }

    public static void setSituacaoFinanceiraProtocolo(Hashtable situacaoFinanceiraProtocoloPrm) {
        situacaoFinanceiraProtocolo = situacaoFinanceiraProtocoloPrm;
    }

    public static Hashtable<String, String> getSituacaoFinanceiraProtocolo() {
        return situacaoFinanceiraProtocolo;
    }

    public static void setTipoDestinacaoCustosPrevisaoCustos(Hashtable tipoDestinacaoCustosPrevisaoCustosPrm) {
        tipoDestinacaoCustosPrevisaoCustos = tipoDestinacaoCustosPrevisaoCustosPrm;
    }

    public static Hashtable getTipoDestinacaoCustosPrevisaoCustos() {
        return tipoDestinacaoCustosPrevisaoCustos;
    }

    public static void setSituacaoProcessoSeletivo(Hashtable situacaoProcessoSeletivoPrm) {
        situacaoProcessoSeletivo = situacaoProcessoSeletivoPrm;
    }

    public static Hashtable getSituacaoProcessoSeletivo() {
        return situacaoProcessoSeletivo;
    }

    public static void setTipoDisciplinaProcessoSel(Hashtable tipoDisciplinaProcessoSelPrm) {
        tipoDisciplinaProcessoSel = tipoDisciplinaProcessoSelPrm;
    }

    public static Hashtable getTipoDisciplinaProcessoSel() {
        return tipoDisciplinaProcessoSel;
    }

    public static void setTipoUnidadeProduto(Hashtable tipoUnidadeProdutoPrm) {
        tipoUnidadeProduto = tipoUnidadeProdutoPrm;
    }

    public static Hashtable getTipoUnidadeProduto() {
        return tipoUnidadeProduto;
    }

    public static void setSituacaoSolicitacaoCompra(Hashtable situacaoSolicitacaoCompraPrm) {
        situacaoSolicitacaoCompra = situacaoSolicitacaoCompraPrm;
    }

    public static Hashtable getSituacaoSolicitacaoCompra() {
        return situacaoSolicitacaoCompra;
    }

    public static void setQtdIrmaosProcSel(Hashtable qtdIrmaosProcSelPrm) {
        qtdIrmaosProcSel = qtdIrmaosProcSelPrm;
    }

    public static Hashtable getQtdIrmaosProcSel() {
        return qtdIrmaosProcSel;
    }

    public static void setQtdFilhosTemProcSel(Hashtable qtdFilhosTemProcSelPrm) {
        qtdFilhosTemProcSel = qtdFilhosTemProcSelPrm;
    }

    public static Hashtable getQtdFilhosTemProcSel() {
        return qtdFilhosTemProcSel;
    }

    public static void setResidenteEmProcSel(Hashtable residenteEmProcSelPrm) {
        residenteEmProcSel = residenteEmProcSelPrm;
    }

    public static Hashtable getResidenteEmProcSel() {
        return residenteEmProcSel;
    }

    public static void setHrsTrabalhoSemanaProcSel(Hashtable hrsTrabalhoSemanaProcSelPrm) {
        hrsTrabalhoSemanaProcSel = hrsTrabalhoSemanaProcSelPrm;
    }

    public static Hashtable getHrsTrabalhoSemanaProcSel() {
        return hrsTrabalhoSemanaProcSel;
    }

    public static void setRamoAtividadeProcSel(Hashtable ramoAtividadeProcSelPrm) {
        ramoAtividadeProcSel = ramoAtividadeProcSelPrm;
    }

    public static Hashtable getRamoAtividadeProcSel() {
        return ramoAtividadeProcSel;
    }

    public static void setRendaMensalProcSel(Hashtable rendaMensalProcSelPrm) {
        rendaMensalProcSel = rendaMensalProcSelPrm;
    }

    public static Hashtable getRendaMensalProcSel() {
        return rendaMensalProcSel;
    }

    public static void setSexo(Hashtable sexoPrm) {
        sexo = sexoPrm;
    }

    public static Hashtable getSexo() {
        return sexo;
    }

    public static void setEstado(Hashtable estadoPrm) {
        estado = estadoPrm;
    }

    public static Hashtable getEstado() {
        return estado;
    }

    public static void setSimNao(Hashtable simNaoPrm) {
        simNao = simNaoPrm;
    }

    public static Hashtable getSimNao() {
        return simNao;
    }

    public static void setEscolaridade(Hashtable escolaridadePrm) {
        escolaridade = escolaridadePrm;
    }

    public static Hashtable getEscolaridade() {
        return escolaridade;
    }

    public static void setEstadoCivil(Hashtable estadoCivilPrm) {
        estadoCivil = estadoCivilPrm;
    }

    public static Hashtable getEstadoCivil() {
        return estadoCivil;
    }

    public static void setNivelAcesso(Hashtable nivelAcessoPrm) {
        nivelAcesso = nivelAcessoPrm;
    }

    public static Hashtable getNivelAcesso() {
        return nivelAcesso;
    }

    public static void setTipoEmpresa(Hashtable tipoEmpresaPrm) {
        tipoEmpresa = tipoEmpresaPrm;
    }

    public static Hashtable getTipoEmpresa() {
        return tipoEmpresa;
    }

    public static void setSituacao(Hashtable situacaoPrm) {
        situacao = situacaoPrm;
    }

    public static Hashtable getSituacao() {
        return situacao;
    }

    public static void setTipoCorrespondencia(Hashtable tipoCorrespondenciaPrm) {
        tipoCorrespondencia = tipoCorrespondenciaPrm;
    }

    public static Hashtable getTipoCorrespondencia() {
        return tipoCorrespondencia;
    }

    public static void setSituacaoSolicitacaoPgtoServicoAcademico(Hashtable situacaoSolicitacaoPgtoServicoAcademicoPrm) {
        situacaoSolicitacaoPgtoServicoAcademico = situacaoSolicitacaoPgtoServicoAcademicoPrm;
    }

    public static Hashtable getSituacaoSolicitacaoPgtoServicoAcademico() {
        return situacaoSolicitacaoPgtoServicoAcademico;
    }

    public static void setTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic(Hashtable tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademicPrm) {
        tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic = tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademicPrm;
    }

    public static Hashtable getTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic() {
        return tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic;
    }

    public static void setTipoInscricaoEvento(Hashtable tipoInscricaoEventoPrm) {
        tipoInscricaoEvento = tipoInscricaoEventoPrm;
    }

    public static Hashtable getTipoInscricaoEvento() {
        return tipoInscricaoEvento;
    }

    public static void setFormasInscricaoEvento(Hashtable formasInscricaoEventoPrm) {
        formasInscricaoEvento = formasInscricaoEventoPrm;
    }

    public static Hashtable getFormasInscricaoEvento() {
        return formasInscricaoEvento;
    }

    public static void setTipoSubmissaoEvento(Hashtable tipoSubmissaoEventoPrm) {
        tipoSubmissaoEvento = tipoSubmissaoEventoPrm;
    }

    public static Hashtable getTipoSubmissaoEvento() {
        return tipoSubmissaoEvento;
    }

    public static void setSomaRendaFamiliaProcSel(Hashtable somaRendaFamiliaProcSelPrm) {
        somaRendaFamiliaProcSel = somaRendaFamiliaProcSelPrm;
    }

    public static Hashtable getSomaRendaFamiliaProcSel() {
        return somaRendaFamiliaProcSel;
    }

    public static void setSituacaoFinanceiraEvento(Hashtable situacaoFinanceiraEventoPrm) {
        situacaoFinanceiraEvento = situacaoFinanceiraEventoPrm;
    }

    public static Hashtable getSituacaoFinanceiraEvento() {
        return situacaoFinanceiraEvento;
    }

    public static void setParticipacaoVidaEconomicaFamiliaProcSel(Hashtable participacaoVidaEconomicaFamiliaProcSelPrm) {
        participacaoVidaEconomicaFamiliaProcSel = participacaoVidaEconomicaFamiliaProcSelPrm;
    }

    public static Hashtable getParticipacaoVidaEconomicaFamiliaProcSel() {
        return participacaoVidaEconomicaFamiliaProcSel;
    }

    public static void setTipoPessoaInscricaoEvento(Hashtable tipoPessoaInscricaoEventoPrm) {
        tipoPessoaInscricaoEvento = tipoPessoaInscricaoEventoPrm;
    }

    public static Hashtable getTipoPessoaInscricaoEvento() {
        return tipoPessoaInscricaoEvento;
    }

    public static void setComoSeManterDuranteCursoProcSel(Hashtable comoSeManterDuranteCursoProcSelPrm) {
        comoSeManterDuranteCursoProcSel = comoSeManterDuranteCursoProcSelPrm;
    }

    public static Hashtable getComoSeManterDuranteCursoProcSel() {
        return comoSeManterDuranteCursoProcSel;
    }

    public static void setTipoPalestraPalestraEvento(Hashtable tipoPalestraPalestraEventoPrm) {
        tipoPalestraPalestraEvento = tipoPalestraPalestraEventoPrm;
    }

    public static Hashtable getTipoPalestraPalestraEvento() {
        return tipoPalestraPalestraEvento;
    }

    public static void setGrauEscolaridadePaiProcSel(Hashtable grauEscolaridadePaiProcSelPrm) {
        grauEscolaridadePaiProcSel = grauEscolaridadePaiProcSelPrm;
    }

    public static Hashtable getGrauEscolaridadePaiProcSel() {
        return grauEscolaridadePaiProcSel;
    }

    public static void setSituacaoCursoExtensao(Hashtable situacaoCursoExtensaoPrm) {
        situacaoCursoExtensao = situacaoCursoExtensaoPrm;
    }

    public static Hashtable getSituacaoCursoExtensao() {
        return situacaoCursoExtensao;
    }

    public static void setGrauEscolaridadeMaeProcSel(Hashtable grauEscolaridadeMaeProcSelPrm) {
        grauEscolaridadeMaeProcSel = grauEscolaridadeMaeProcSelPrm;
    }

    public static Hashtable getGrauEscolaridadeMaeProcSel() {
        return grauEscolaridadeMaeProcSel;
    }

    public static void setSituacaoFinanceiraExtensao(Hashtable situacaoFinanceiraExtensaoPrm) {
        situacaoFinanceiraExtensao = situacaoFinanceiraExtensaoPrm;
    }

    public static Hashtable getSituacaoFinanceiraExtensao() {
        return situacaoFinanceiraExtensao;
    }

    public static void setTipoEscolaCursouEnsinoMedioProcSel(Hashtable tipoEscolaCursouEnsinoMedioProcSelPrm) {
        tipoEscolaCursouEnsinoMedioProcSel = tipoEscolaCursouEnsinoMedioProcSelPrm;
    }

    public static Hashtable getTipoEscolaCursouEnsinoMedioProcSel() {
        return tipoEscolaCursouEnsinoMedioProcSel;
    }

    public static void setAnoConclusaoEnsinoMedioProcSel(Hashtable anoConclusaoEnsinoMedioProcSelPrm) {
        anoConclusaoEnsinoMedioProcSel = anoConclusaoEnsinoMedioProcSelPrm;
    }

    public static Hashtable getAnoConclusaoEnsinoMedioProcSel() {
        return anoConclusaoEnsinoMedioProcSel;
    }

    public static void setQtdFaculdadePrestouVestib(Hashtable qtdFaculdadePrestouVestibPrm) {
        qtdFaculdadePrestouVestib = qtdFaculdadePrestouVestibPrm;
    }

    public static Hashtable getQtdFaculdadePrestouVestib() {
        return qtdFaculdadePrestouVestib;
    }

    public static void setIniciouCursoSuperiorProcSel(Hashtable iniciouCursoSuperiorProcSelPrm) {
        iniciouCursoSuperiorProcSel = iniciouCursoSuperiorProcSelPrm;
    }

    public static Hashtable getIniciouCursoSuperiorProcSel() {
        return iniciouCursoSuperiorProcSel;
    }

    public static void setMotivoEscolhaCursoProcSel(Hashtable motivoEscolhaCursoProcSelPrm) {
        motivoEscolhaCursoProcSel = motivoEscolhaCursoProcSelPrm;
    }

    public static Hashtable getMotivoEscolhaCursoProcSel() {
        return motivoEscolhaCursoProcSel;
    }

    public static void setTipoProfessorProfessorCursoExtensao(Hashtable tipoProfessorProfessorCursoExtensaoPrm) {
        tipoProfessorProfessorCursoExtensao = tipoProfessorProfessorCursoExtensaoPrm;
    }

    public static Hashtable getTipoProfessorProfessorCursoExtensao() {
        return tipoProfessorProfessorCursoExtensao;
    }

    public static void setTipoInscricaoInscricaoCursoExtensao(Hashtable tipoInscricaoInscricaoCursoExtensaoPrm) {
        tipoInscricaoInscricaoCursoExtensao = tipoInscricaoInscricaoCursoExtensaoPrm;
    }

    public static Hashtable getTipoInscricaoInscricaoCursoExtensao() {
        return tipoInscricaoInscricaoCursoExtensao;
    }

    public static void setTipoCustoCFGCustoAdministrativo(Hashtable tipoCustoCFGCustoAdministrativoPrm) {
        tipoCustoCFGCustoAdministrativo = tipoCustoCFGCustoAdministrativoPrm;
    }

    public static Hashtable getTipoCustoCFGCustoAdministrativo() {
        return tipoCustoCFGCustoAdministrativo;
    }

    public static void setSituacaoDebitoFuncionarioFinanceiro(Hashtable situacaoDebitoFuncionarioFinanceiroPrm) {
        situacaoDebitoFuncionarioFinanceiro = situacaoDebitoFuncionarioFinanceiroPrm;
    }

    public static Hashtable getSituacaoDebitoFuncionarioFinanceiro() {
        return situacaoDebitoFuncionarioFinanceiro;
    }

    public static void setTipoParceiroParceiro(Hashtable tipoParceiroParceiroPrm) {
        tipoParceiroParceiro = tipoParceiroParceiroPrm;
    }

    public static Hashtable getTipoParceiroParceiro() {
        return tipoParceiroParceiro;
    }

    public static void setTipoPublicacaoPublicacaoPesquisa(Hashtable tipoPublicacaoPublicacaoPesquisaPrm) {
        tipoPublicacaoPublicacaoPesquisa = tipoPublicacaoPublicacaoPesquisaPrm;
    }

    public static Hashtable getTipoPublicacaoPublicacaoPesquisa() {
        return tipoPublicacaoPublicacaoPesquisa;
    }

    public static void setConhecimentoProcessoSeletivo(Hashtable conhecimentoProcessoSeletivoPrm) {
        conhecimentoProcessoSeletivo = conhecimentoProcessoSeletivoPrm;
    }

    public static Hashtable getConhecimentoProcessoSeletivo() {
        return conhecimentoProcessoSeletivo;
    }

    public static void setTipoPesqiosadorPublicacao(Hashtable tipoPesqiosadorPublicacaoPrm) {
        tipoPesqiosadorPublicacao = tipoPesqiosadorPublicacaoPrm;
    }

    public static Hashtable getTipoOrigemContaPagar() {
        return tipoOrigemContaPagar;
    }

    public static void setTipoOrigemContaPagar(Hashtable tipoOrigemContaPagar) {
        Dominios.tipoOrigemContaPagar = tipoOrigemContaPagar;
    }

    public static Hashtable getTipoOrigemContaReceber() {
        return tipoOrigemContaReceber;
    }

    public static void setTipoOrigemContaReceber(Hashtable tipoOrigemContaReceber) {
        Dominios.tipoOrigemContaReceber = tipoOrigemContaReceber;
    }

    public static Hashtable getTipoOrigemContaReceberRelatorio() {
    	return tipoOrigemContaReceberRelatorio;
    }
    
    public static void setTipoOrigemContaReceberRelatorio(Hashtable tipoOrigemContaReceberRelatorio) {
    	Dominios.tipoOrigemContaReceberRelatorio = tipoOrigemContaReceberRelatorio;
    }
    
    public static Hashtable getTipoPesqiosadorPublicacao() {
        return tipoPesqiosadorPublicacao;
    }

    public static void setPqPrestarVestibularInstituicao(Hashtable pqPrestarVestibularInstituicaoPrm) {
        pqPrestarVestibularInstituicao = pqPrestarVestibularInstituicaoPrm;
    }

    public static Hashtable getPqPrestarVestibularInstituicao() {
        return pqPrestarVestibularInstituicao;
    }

    public static void setSituacaoGradeCurricular(Hashtable situacaoGradeCurricularPrm) {
        situacaoGradeCurricular = situacaoGradeCurricularPrm;
    }

    public static Hashtable getSituacaoGradeCurricular() {
        return situacaoGradeCurricular;
    }

    public static void setTipoPesquisadorPesquisadorLinhaPesquisa(Hashtable tipoPesquisadorPesquisadorLinhaPesquisaPrm) {
        tipoPesquisadorPesquisadorLinhaPesquisa = tipoPesquisadorPesquisadorLinhaPesquisaPrm;
    }

    public static Hashtable getTipoPesquisadorPesquisadorLinhaPesquisa() {
        return tipoPesquisadorPesquisadorLinhaPesquisa;
    }

    public static void setEsperaCursoSuperiorProcSel(Hashtable esperaCursoSuperiorProcSelPrm) {
        esperaCursoSuperiorProcSel = esperaCursoSuperiorProcSelPrm;
    }

    public static Hashtable getEsperaCursoSuperiorProcSel() {
        return esperaCursoSuperiorProcSel;
    }

    public static void setSituacaoPesquisadorLinhaPesquisa(Hashtable situacaoPesquisadorLinhaPesquisaPrm) {
        situacaoPesquisadorLinhaPesquisa = situacaoPesquisadorLinhaPesquisaPrm;
    }

    public static Hashtable getSituacaoPesquisadorLinhaPesquisa() {
        return situacaoPesquisadorLinhaPesquisa;
    }

    public static void setQtdLivroLeituraAnoProcSel(Hashtable qtdLivroLeituraAnoProcSelPrm) {
        qtdLivroLeituraAnoProcSel = qtdLivroLeituraAnoProcSelPrm;
    }

    public static Hashtable getQtdLivroLeituraAnoProcSel() {
        return qtdLivroLeituraAnoProcSel;
    }

    public static void setFormaPagamentoPgtoServicoAcademico(Hashtable formaPagamentoPgtoServicoAcademicoPrm) {
        formaPagamentoPgtoServicoAcademico = formaPagamentoPgtoServicoAcademicoPrm;
    }

    public static Hashtable getFormaPagamentoPgtoServicoAcademico() {
        return formaPagamentoPgtoServicoAcademico;
    }

    public static void setExisteMicrocomputadorResProcSel(Hashtable existeMicrocomputadorResProcSelPrm) {
        existeMicrocomputadorResProcSel = existeMicrocomputadorResProcSelPrm;
    }

    public static Hashtable getExisteMicrocomputadorResProcSel() {
        return existeMicrocomputadorResProcSel;
    }

    public static void setTipoLivroLeituraProcSel(Hashtable tipoLivroLeituraProcSelPrm) {
        tipoLivroLeituraProcSel = tipoLivroLeituraProcSelPrm;
    }

    public static Hashtable getTipoLivroLeituraProcSel() {
        return tipoLivroLeituraProcSel;
    }

    public static void setMeioAtualizacaoAcontecimentoProcSel(Hashtable meioAtualizacaoAcontecimentoProcSelPrm) {
        meioAtualizacaoAcontecimentoProcSel = meioAtualizacaoAcontecimentoProcSelPrm;
    }

    public static Hashtable getMeioAtualizacaoAcontecimentoProcSel() {
        return meioAtualizacaoAcontecimentoProcSel;
    }

    public static void setLeJornalProcSel(Hashtable leJornalProcSelPrm) {
        leJornalProcSel = leJornalProcSelPrm;
    }

    public static Hashtable getLeJornalProcSel() {
        return leJornalProcSel;
    }

    public static void setAssuntoJornalLeituraProcSel(Hashtable assuntoJornalLeituraProcSelPrm) {
        assuntoJornalLeituraProcSel = assuntoJornalLeituraProcSelPrm;
    }

    public static Hashtable getAssuntoJornalLeituraProcSel() {
        return assuntoJornalLeituraProcSel;
    }

    public static void setReligiaoProcSel(Hashtable religiaoProcSelPrm) {
        religiaoProcSel = religiaoProcSelPrm;
    }

    public static Hashtable getReligiaoProcSel() {
        return religiaoProcSel;
    }

    public static void setCotacaoAutorizadaItemSolicitacaoCompra(Hashtable cotacaoAutorizadaItemSolicitacaoCompraPrm) {
        cotacaoAutorizadaItemSolicitacaoCompra = cotacaoAutorizadaItemSolicitacaoCompraPrm;
    }

    public static Hashtable getCotacaoAutorizadaItemSolicitacaoCompra() {
        return cotacaoAutorizadaItemSolicitacaoCompra;
    }

    public static void setTipoInsentivoInsentivoPesquisa(Hashtable tipoInsentivoInsentivoPesquisaPrm) {
        tipoInsentivoInsentivoPesquisa = tipoInsentivoInsentivoPesquisaPrm;
    }

    public static Hashtable getTipoInsentivoInsentivoPesquisa() {
        return tipoInsentivoInsentivoPesquisa;
    }

    public static void setTipoPesquisadorPublicacao(Hashtable tipoPesquisadorPublicacaoPrm) {
        tipoPesquisadorPublicacao = tipoPesquisadorPublicacaoPrm;
    }

    public static Hashtable getTipoPesquisadorPublicacao() {
        return tipoPesquisadorPublicacao;
    }

    public static void setTipoPessoaBasicoPessoa(Hashtable tipoPessoaBasicoPessoaPrm) {
        tipoPessoaBasicoPessoa = tipoPessoaBasicoPessoaPrm;
    }

    public static Hashtable getTipoPessoaBasicoPessoa() {
        return tipoPessoaBasicoPessoa;
    }

    public static void setSituacaoCursoAcademico(Hashtable situacaoCursoAcademicoPrm) {
        situacaoCursoAcademico = situacaoCursoAcademicoPrm;
    }

    public static Hashtable getSituacaoCursoAcademico() {
        return situacaoCursoAcademico;
    }

    public static void setSituacaoFormacaoAcademica(Hashtable situacaoFormacaoAcademicaPrm) {
        situacaoFormacaoAcademica = situacaoFormacaoAcademicaPrm;
    }

    public static Hashtable getSituacaoFormacaoAcademica() {
        return situacaoFormacaoAcademica;
    }

    public static void setTipoInstFormacaoAcademica(Hashtable tipoInstFormacaoAcademicaPrm) {
        tipoInstFormacaoAcademica = tipoInstFormacaoAcademicaPrm;
    }

    public static Hashtable getTipoInstFormacaoAcademica() {
        return tipoInstFormacaoAcademica;
    }

    public static void setEscolaridadeFormacaoAcademica(Hashtable escolaridadeFormacaoAcademicaPrm) {
        escolaridadeFormacaoAcademica = escolaridadeFormacaoAcademicaPrm;
    }

    public static Hashtable getEscolaridadeFormacaoAcademica() {
        return escolaridadeFormacaoAcademica;
    }

    public static void setSituacaoTurma(Hashtable situacaoTurmaPrm) {
        situacaoTurma = situacaoTurmaPrm;
    }

    public static Hashtable getSituacaoTurma() {
        return situacaoTurma;
    }

    public static void setCorProcSel(Hashtable corProcSelPrm) {
        corProcSel = corProcSelPrm;
    }

    public static Hashtable getCorProcSel() {
        return corProcSel;
    }

    public static void setConhecimentoLingInglesaProcSel(Hashtable conhecimentoLingInglesaProcSelPrm) {
        conhecimentoLingInglesaProcSel = conhecimentoLingInglesaProcSelPrm;
    }

    public static Hashtable getConhecimentoLingInglesaProcSel() {
        return conhecimentoLingInglesaProcSel;
    }

    public static void setConhecimentoLingEspanholaProcSel(Hashtable conhecimentoLingEspanholaProcSelPrm) {
        conhecimentoLingEspanholaProcSel = conhecimentoLingEspanholaProcSelPrm;
    }

    public static Hashtable getConhecimentoLingEspanholaProcSel() {
        return conhecimentoLingEspanholaProcSel;
    }

    public static void setFreguentaCursoExtracurricularProcSel(Hashtable freguentaCursoExtracurricularProcSelPrm) {
        freguentaCursoExtracurricularProcSel = freguentaCursoExtracurricularProcSelPrm;
    }

    public static Hashtable getFreguentaCursoExtracurricularProcSel() {
        return freguentaCursoExtracurricularProcSel;
    }

    public static void setTipoEsportePraticaProcSel(Hashtable tipoEsportePraticaProcSelPrm) {
        tipoEsportePraticaProcSel = tipoEsportePraticaProcSelPrm;
    }

    public static Hashtable getTipoEsportePraticaProcSel() {
        return tipoEsportePraticaProcSel;
    }

    public static void setMeioTransporteUtilizadoParaIntituicaoProcSel(Hashtable meioTransporteUtilizadoParaIntituicaoProcSelPrm) {
        meioTransporteUtilizadoParaIntituicaoProcSel = meioTransporteUtilizadoParaIntituicaoProcSelPrm;
    }

    public static Hashtable getMeioTransporteUtilizadoParaIntituicaoProcSel() {
        return meioTransporteUtilizadoParaIntituicaoProcSel;
    }

    public static void setModalidadeEnsinoMedioProcSel(Hashtable modalidadeEnsinoMedioProcSelPrm) {
        modalidadeEnsinoMedioProcSel = modalidadeEnsinoMedioProcSelPrm;
    }

    public static Hashtable getModalidadeEnsinoMedioProcSel() {
        return modalidadeEnsinoMedioProcSel;
    }

    public static void setSituacaoAcademicoCancelamento(Hashtable situacaoAcademicoCancelamentoPrm) {
        situacaoAcademicoCancelamento = situacaoAcademicoCancelamentoPrm;
    }

    public static Hashtable getSituacaoAcademicoCancelamento() {
        return situacaoAcademicoCancelamento;
    }

    public static void setNivelEducacionalCurso(Hashtable nivelEducacionalCursoPrm) {
        nivelEducacionalCurso = nivelEducacionalCursoPrm;
    }

    public static Hashtable getNivelEducacionalCurso() {
        return nivelEducacionalCurso;
    }

    public static void setPeriodicidadeCurso(Hashtable periodicidadeCursoPrm) {
        periodicidadeCurso = periodicidadeCursoPrm;
    }

    public static Hashtable getPeriodicidadeCurso() {
        return periodicidadeCurso;
    }

    public static void setSituacaoProcessoMatricula(Hashtable situacaoProcessoMatriculaPrm) {
        situacaoProcessoMatricula = situacaoProcessoMatriculaPrm;
    }

    public static Hashtable getSituacaoProcessoMatricula() {
        return situacaoProcessoMatricula;
    }

    public static void setDiaSemana(Hashtable diaSemanaPrm) {
        diaSemana = diaSemanaPrm;
    }

    public static Hashtable getDiaSemana() {
        return diaSemana;
    }

    public static void setSituacaoEntregaDocumentacao(Hashtable situacaoEntregaDocumentacaoPrm) {
        situacaoEntregaDocumentacao = situacaoEntregaDocumentacaoPrm;
    }

    public static Hashtable getSituacaoEntregaDocumentacao() {
        return situacaoEntregaDocumentacao;
    }

    public static void setTipoItemPlanoFinanceiroAluno(Hashtable tipoItemPlanoFinanceiroAlunoPrm) {
        tipoItemPlanoFinanceiroAluno = tipoItemPlanoFinanceiroAlunoPrm;
    }

    public static Hashtable getTipoItemPlanoFinanceiroAluno() {
        return tipoItemPlanoFinanceiroAluno;
    }

    public static void setEscopoLink(Hashtable escopoLinkPrm) {
        escopoLink = escopoLinkPrm;
    }

    public static Hashtable getEscopoLink() {
        return escopoLink;
    }

    public static void setTipoJustificativaAlteracaoMatricula(Hashtable tipoJustificativaAlteracaoMatriculaPrm) {
        tipoJustificativaAlteracaoMatricula = tipoJustificativaAlteracaoMatriculaPrm;
    }

    public static Hashtable getTipoJustificativaAlteracaoMatricula() {
        return tipoJustificativaAlteracaoMatricula;
    }

    public static void setTipoContratoDespesas(Hashtable tipoContratoDespesasPrm) {
        tipoContratoDespesas = tipoContratoDespesasPrm;
    }

    public static Hashtable getTipoContratoDespesas() {
        return tipoContratoDespesas;
    }

    public static void setTipoValorConvenio(Hashtable tipoValorConvenioPrm) {
        tipoValorConvenio = tipoValorConvenioPrm;
    }

    public static Hashtable getTipoValorConvenio() {
        return tipoValorConvenio;
    }

    public static void setTipoOrigemDespesa(Hashtable tipoOrigemDespesaPrm) {
        tipoOrigemDespesa = tipoOrigemDespesaPrm;
    }

    public static Hashtable getTipoOrigemDespesa() {
        return tipoOrigemDespesa;
    }

    public static void setTipoOrigemDebitoTerceiros(Hashtable tipoOrigemDebitoTerceirosPrm) {
        tipoOrigemDebitoTerceiros = tipoOrigemDebitoTerceirosPrm;
    }

    public static Hashtable getTipoOrigemDebitoTerceiros() {
        return tipoOrigemDebitoTerceiros;
    }

    public static void setTipoOrigemReceita(Hashtable tipoOrigemReceitaPrm) {
        tipoOrigemReceita = tipoOrigemReceitaPrm;
    }

    public static Hashtable getTipoOrigemReceita() {
        return tipoOrigemReceita;
    }

    public static void setRegimeCurso(Hashtable regimeCursoPrm) {
        regimeCurso = regimeCursoPrm;
    }

    public static Hashtable getRegimeCurso() {
        return regimeCurso;
    }

    public static void setRegimeAprovacaoCurso(Hashtable regimeAprovacaoCursoPrm) {
        regimeAprovacaoCurso = regimeAprovacaoCursoPrm;
    }

    public static Hashtable getRegimeAprovacaoCurso() {
        return regimeAprovacaoCurso;
    }

    public static void setDiaSemanaDisponibilidadeHorario(Hashtable diaSemanaDisponibilidadeHorarioPrm) {
        diaSemanaDisponibilidadeHorario = diaSemanaDisponibilidadeHorarioPrm;
    }

    public static Hashtable getDiaSemanaDisponibilidadeHorario() {
        return diaSemanaDisponibilidadeHorario;
    }

    public static void setTipoDisciplinaDisciplina(Hashtable tipoDisciplinaDisciplinaPrm) {
        tipoDisciplinaDisciplina = tipoDisciplinaDisciplinaPrm;
    }

    public static Hashtable getTipoDisciplinaDisciplina() {
        return tipoDisciplinaDisciplina;
    }

    public static void setTipoDocumentoDocumentacaoMatricula(Hashtable tipoDocumentoDocumentacaoMatriculaPrm) {
        tipoDocumentoDocumentacaoMatricula = tipoDocumentoDocumentacaoMatriculaPrm;
    }

    public static Hashtable getTipoDocumentoDocumentacaoMatricula() {
        return tipoDocumentoDocumentacaoMatricula;
    }

    public static void setTipoFiliacao(Hashtable tipoFiliacaoPrm) {
        tipoFiliacao = tipoFiliacaoPrm;
    }

    public static Hashtable getTipoFiliacao() {
        return tipoFiliacao;
    }

    public static void setTipoHistoricoHistorico(Hashtable tipoHistoricoHistoricoPrm) {
        tipoHistoricoHistorico = tipoHistoricoHistoricoPrm;
    }

    public static Hashtable getTipoHistoricoHistorico() {
        return tipoHistoricoHistorico;
    }

    public static void setSituacaoHistorico(Hashtable situacaoHistoricoPrm) {
        situacaoHistorico = situacaoHistoricoPrm;
    }

    public static Hashtable getSituacaoHistorico() {
        return situacaoHistorico;
    }

    public static void setSituacaoMatricula(Hashtable situacaoMatriculaPrm) {
        situacaoMatricula = situacaoMatriculaPrm;
    }

    public static Hashtable getSituacaoMatricula() {
        return situacaoMatricula;
    }

    public static void setTipoReferenciareferenciaBibliografica(Hashtable tipoReferenciareferenciaBibliograficaPrm) {
        tipoReferenciareferenciaBibliografica = tipoReferenciareferenciaBibliograficaPrm;
    }

    public static Hashtable getTipoReferenciareferenciaBibliografica() {
        return tipoReferenciareferenciaBibliografica;
    }

    public static void setOpcaoResultadoProcessoSeletivo(Hashtable opcaoResultadoProcessoSeletivoPrm) {
        opcaoResultadoProcessoSeletivo = opcaoResultadoProcessoSeletivoPrm;
    }

    public static Hashtable getOpcaoResultadoProcessoSeletivo() {
        return opcaoResultadoProcessoSeletivo;
    }

    public static void setSituacaoCampanhaMarketing(Hashtable situacaoCampanhaMarketingPrm) {
        situacaoCampanhaMarketing = situacaoCampanhaMarketingPrm;
    }

    public static Hashtable getSituacaoCampanhaMarketing() {
        return situacaoCampanhaMarketing;
    }

    public static void setConvenioTipoCobertura(Hashtable convenioTipoCoberturaPrm) {
        convenioTipoCobertura = convenioTipoCoberturaPrm;
    }

    public static Hashtable getConvenioTipoCobertura() {
        return convenioTipoCobertura;
    }

    public static void setConvenioSituacao(Hashtable convenioSituacaoPrm) {
        convenioSituacao = convenioSituacaoPrm;
    }

    public static Hashtable getConvenioSituacao() {
        return convenioSituacao;
    }

    public static void setConvenioFormaRecebimentoParceiro(Hashtable convenioFormaRecebimentoParceiroPrm) {
        convenioFormaRecebimentoParceiro = convenioFormaRecebimentoParceiroPrm;
    }

    public static Hashtable getConvenioFormaRecebimentoParceiro() {
        return convenioFormaRecebimentoParceiro;
    }

    public static void setConfiguracaoFinanceiroTipoCalcJuros(Hashtable configuracaoFinanceiroTipoCalcJurosPrm) {
        configuracaoFinanceiroTipoCalcJuros = configuracaoFinanceiroTipoCalcJurosPrm;
    }

    public static Hashtable getConfiguracaoFinanceiroTipoCalcJuros() {
        return configuracaoFinanceiroTipoCalcJuros;
    }

    public static void setPlanoFinanceiroCursoTipoCalculoParcela(Hashtable planoFinanceiroCursoTipoCalculoParcelaPrm) {
        planoFinanceiroCursoTipoCalculoParcela = planoFinanceiroCursoTipoCalculoParcelaPrm;
    }

    public static Hashtable getPlanoFinanceiroCursoTipoCalculoParcela() {
        return planoFinanceiroCursoTipoCalculoParcela;
    }

    public static Hashtable getConteudoPlanejamento() {
        return conteudoPlanejamento;
    }

    public static void setConteudoPlanejamento(Hashtable conteudoPlanejamento) {
        Dominios.conteudoPlanejamento = conteudoPlanejamento;
    }

    public static Hashtable getTipoComunicadoInterno() {
        return tipoComunicadoInterno;
    }

    public static void setTipoComunicadoInterno(Hashtable aTipoComunicadoInterno) {
        tipoComunicadoInterno = aTipoComunicadoInterno;
    }

    public static Hashtable getTipoDestinatarioComunicadoInterno() {
        return tipoDestinatarioComunicadoInterno;
    }

    public static void setTipoDestinatarioComunicadoInterno(Hashtable aTipoDestinatarioComunicadoInterno) {
        tipoDestinatarioComunicadoInterno = aTipoDestinatarioComunicadoInterno;
    }

    public static Hashtable getPrioridadeComunicadoInterno() {
        return prioridadeComunicadoInterno;
    }

    public static void setPrioridadeComunicadoInterno(Hashtable aPrioridadeComunicadoInterno) {
        prioridadeComunicadoInterno = aPrioridadeComunicadoInterno;
    }

    public static Hashtable getTipoUsuario() {
        return tipoUsuario;
    }

    public static void setTipoUsuario(Hashtable tipoUsuario) {
        Dominios.tipoUsuario = tipoUsuario;
    }

    public static Hashtable getTipoComunicadoInternoAluno() {
        return tipoComunicadoInternoAluno;
    }

    public static void setTipoComunicadoInternoAluno(Hashtable tipoComunicadoInternoAluno) {
        Dominios.tipoComunicadoInternoAluno = tipoComunicadoInternoAluno;
    }

    public static Hashtable getTipoDestinatarioComunicadoInternoProfessor() {
        return tipoDestinatarioComunicadoInternoProfessor;
    }

    public static void setTipoDestinatarioComunicadoInternoProfessor(Hashtable tipoDestinatarioComunicadoInternoProfessor) {
        Dominios.tipoDestinatarioComunicadoInternoProfessor = tipoDestinatarioComunicadoInternoProfessor;
    }

    public static Hashtable getTipoDestinatarioComunicadoInternoCoordenador() {
        return tipoDestinatarioComunicadoInternoCoordenador;
    }

    public static void setTipoDestinatarioComunicadoInternoCoordenador(Hashtable tipoDestinatarioComunicadoInternoCoordenador) {
        Dominios.tipoDestinatarioComunicadoInternoCoordenador = tipoDestinatarioComunicadoInternoCoordenador;
    }

    public static Hashtable getTipoDestinatarioComunicadoInternoAluno() {
        return tipoDestinatarioComunicadoInternoAluno;
    }

    public static void setTipoDestinatarioComunicadoInternoAluno(Hashtable tipoDestinatarioComunicadoInternoAluno) {
        Dominios.tipoDestinatarioComunicadoInternoAluno = tipoDestinatarioComunicadoInternoAluno;
    }

    public static Hashtable getSituacaoAutorizacao() {
        return situacaoAutorizacao;
    }

    public static void setSituacaoAutorizacao(Hashtable situacaoAutorizacao) {
        Dominios.situacaoAutorizacao = situacaoAutorizacao;
    }

    public static Hashtable getSituacaoEntregaRecebimento() {
        return situacaoEntregaRecebimento;
    }

    public static void setSituacaoEntregaRecebimento(Hashtable situacaoEntregaRecebimento) {
        Dominios.situacaoEntregaRecebimento = situacaoEntregaRecebimento;
    }

    public static Hashtable getSituacaoFinanceira() {
        return situacaoFinanceira;
    }

    public static void setSituacaoFinanceira(Hashtable situacaoFinanceira) {
        Dominios.situacaoFinanceira = situacaoFinanceira;
    }

    public static Hashtable getTipoDespesaFinanceira() {
        return tipoDespesaFinanceira;
    }

    public static void setTipoDespesaFinanceira(Hashtable tipoDespesaFinanceira) {
        Dominios.tipoDespesaFinanceira = tipoDespesaFinanceira;
    }

    public static Hashtable getTipoFormaPagamento() {
        return tipoFormaPagamento;
    }

    public static void setTipoFormaPagamento(Hashtable tipoFormaPagamento) {
        Dominios.tipoFormaPagamento = tipoFormaPagamento;
    }

    public static Hashtable getTipoMovimentacaoEstoque() {
        return tipoMovimentacaoEstoque;
    }

    public static void setTipoMovimentacaoEstoque(Hashtable tipoMovimentacaoEstoque) {
        Dominios.tipoMovimentacaoEstoque = tipoMovimentacaoEstoque;
    }

    public static Hashtable getTipoDesconto() {
        return tipoDesconto;
    }

    public static void setTipoDesconto(Hashtable tipoDesconto) {
        Dominios.tipoDesconto = tipoDesconto;
    }

    public static Hashtable getTipoRespostaQuestionario() {
        return tipoRespostaQuestionario;
    }

    public static void setTipoRespostaQuestionario(Hashtable tipoRespostaQuestionario) {
        Dominios.tipoRespostaQuestionario = tipoRespostaQuestionario;
    }

    public static Hashtable getSituacaoQuestionario() {
        return situacaoQuestionario;
    }

    public static void setSituacaiQuestionario(Hashtable situacaoQuestionario) {
        Dominios.situacaoQuestionario = situacaoQuestionario;
    }

    /**
     * @return the tipoStatusRequerimento
     */
    public static Hashtable getTipoRequerimento() {
        return tipoRequerimento;
    }

    /**
     * @param aTipoStatusRequerimento
     *            the tipoStatusRequerimento to set
     */
    public static void setTipoRequerimento(Hashtable aTipoRequerimento) {
        tipoRequerimento = aTipoRequerimento;
    }

    public static Hashtable getPublicoAlvo() {
        return publicoAlvo;
    }

    public static void setPublicoAlvo(Hashtable publicoAlvo) {
        Dominios.publicoAlvo = publicoAlvo;
    }

    /**
     * @return the tipoContasAPagar
     */
    public static Hashtable getTipoContasAPagar() {
        return tipoContasAPagar;
    }

    /**
     * @param aTipoContasAPagar
     *            the tipoContasAPagar to set
     */
    public static void setTipoContasAPagar(Hashtable aTipoContasAPagar) {
        tipoContasAPagar = aTipoContasAPagar;
    }

//    /**
//     * @return the tipoEscopoQuestionario
//     */
//    public static Hashtable getTipoEscopoQuestionario() {
//        return tipoEscopoQuestionario;
//    }
//
//    /**
//     * @param aTipoEscopoQuestionario
//     *            the tipoEscopoQuestionario to set
//     */
//    public static void setTipoEscopoQuestionario(Hashtable aTipoEscopoQuestionario) {
//        tipoEscopoQuestionario = aTipoEscopoQuestionario;
//    }

    public static Hashtable getTipoAutoria() {
        return tipoAutoria;
    }

    public static void setTipoAutoria(Hashtable tipoAutoria) {
        Dominios.tipoAutoria = tipoAutoria;
    }

    public static Hashtable getSituacaoFinanceiraMatricula() {
        return situacaoFinanceiraMatricula;
    }

    public static void setSituacaoFinanceiraMatricula(Hashtable situacaoFinanceiraMatricula) {
        Dominios.situacaoFinanceiraMatricula = situacaoFinanceiraMatricula;
    }

    public static Hashtable getSituacaoCheque() {
        return situacaoCheque;
    }

    public static void setSituacaoCheque(Hashtable situacaoCheque) {
        Dominios.situacaoCheque = situacaoCheque;
    }

    public static Hashtable getNivelCategoriaDespesa() {
        return nivelCategoriaDespesa;
    }

    public static void setNivelCategoriaDespesa(Hashtable nivelCategoriaDespesa) {
        Dominios.nivelCategoriaDespesa = nivelCategoriaDespesa;
    }

    public static Hashtable getTipoSacado() {
        return tipoSacado;
    }

    public static void setTipoSacado(Hashtable tipoSacado) {
        Dominios.tipoSacado = tipoSacado;
    }

    public static Hashtable getTipoSacadoContratosReceitas() {
        return tipoSacadoContratosReceitas;
    }

    public static void setTipoSacadoContratosReceitas(Hashtable tipoSacadoContratosReceitas) {
        Dominios.tipoSacadoContratosReceitas = tipoSacadoContratosReceitas;
    }

    public static Hashtable getInformarTurma() {
        return informarTurma;
    }

    public static void setInformarTurma(Hashtable informarTurma) {
        Dominios.informarTurma = informarTurma;
    }

    public static Hashtable getSituacaoRecebimentoCompra() {
        return situacaoRecebimentoCompra;
    }

    public static void setSituacaoRecebimentoCompra(Hashtable situacaoRecebimentoCompra) {
        Dominios.situacaoRecebimentoCompra = situacaoRecebimentoCompra;
    }

    /**
     * @return the situacaoContratosDespesas
     */
    public static Hashtable getSituacaoContratosDespesas() {
        return situacaoContratosDespesas;
    }

    /**
     * @param aSituacaoContratosDespesas
     *            the situacaoContratosDespesas to set
     */
    public static void setSituacaoContratosDespesas(Hashtable situacaoContratosDespesas) {
        Dominios.situacaoContratosDespesas = situacaoContratosDespesas;
    }

    /**
     * @return the situacaoContratosReceitas
     */
    public static Hashtable getSituacaoContratosReceitas() {
        return situacaoContratosReceitas;
    }

    /**
     * @param aSituacaoContratosReceitas
     *            the situacaoContratosReceitas to set
     */
    public static void setSituacaoContratosReceitas(Hashtable situacaoContratosReceitas) {
        Dominios.situacaoContratosReceitas = situacaoContratosReceitas;
    }

    public static Hashtable getAtuaComoDocente() {
        return atuaComoDocente;
    }

    public static void setAtuaComoDocente(Hashtable atuaComoDocente) {
        Dominios.atuaComoDocente = atuaComoDocente;
    }

    public static Hashtable getMarcadoAluno() {
        return marcadoAluno;
    }

    public static void setMarcadoAluno(Hashtable marcadoAluno) {
        Dominios.marcadoAluno = marcadoAluno;
    }

    public static Hashtable getMarcadoProfessor() {
        return marcadoProfessor;
    }

    public static void setMarcadoProfessor(Hashtable marcadoProfessor) {
        Dominios.marcadoProfessor = marcadoProfessor;
    }

    public static Hashtable getMarcadoCurso() {
        return marcadoCurso;
    }

    public static void setMarcadoCurso(Hashtable marcadoCurso) {
        Dominios.marcadoCurso = marcadoCurso;
    }

    public static Hashtable getMarcadoMatricula() {
        return marcadoMatricula;
    }
    
    public static void setMarcadoMatricula(Hashtable marcadoMatricula) {
        Dominios.marcadoMatricula = marcadoMatricula;
    }

    public static Hashtable getMarcadoOutras() {
        return marcadoOutras;
    }

    public static void setMarcadoOutras(Hashtable marcadoOutras) {
        Dominios.marcadoOutras = marcadoOutras;
    }

    public static Hashtable getMarcadoDisciplina() {
        return marcadoDisciplina;
    }
    
    public static Hashtable getMarcadoEstagio() {
        return marcadoEstagio;
    }

    public static Hashtable getMarcadoInscProcSeletivo() {
    	return marcadoInscProcSeletivo;
    }
    
    public static void setMarcadoDisciplina(Hashtable marcadoDisciplina) {
        Dominios.marcadoDisciplina = marcadoDisciplina;
    }

    public static Hashtable getMarcadoUnidadeEnsino() {
        return marcadoUnidadeEnsino;
    }

    public static void setMarcadoUnidadeEnsino(Hashtable marcadoUnidadeEnsino) {
        Dominios.marcadoUnidadeEnsino = marcadoUnidadeEnsino;
    }

    public static Hashtable getMarcadoDisciplinaDeclaracao() {
        return marcadoDisciplinaDeclaracao;
    }

    public static void setMarcadoDisciplinaDeclaracao(Hashtable marcadoDisciplinaDeclaracao) {
        Dominios.marcadoDisciplinaDeclaracao = marcadoDisciplinaDeclaracao;
    }

    public static Hashtable getSituacaoTextoPadrao() {
        return situacaoTextoPadrao;
    }

    public static void setSituacaoTextoPadrao(Hashtable situacaoTextoPadrao) {
        Dominios.situacaoTextoPadrao = situacaoTextoPadrao;
    }

    /**
     * @return the marcadoContaReceber
     */
    public static Hashtable getMarcadoContaReceber() {
        return marcadoContaReceber;
    }

    /**
     * @param aMarcadoContaReceber
     *            the marcadoContaReceber to set
     */
    public static void setMarcadoContaReceber(Hashtable aMarcadoContaReceber) {
        marcadoContaReceber = aMarcadoContaReceber;
    }

    /**
     * @return the planoFinanceiroCursoModeloGeracaoParcelas
     */
    public static Hashtable getPlanoFinanceiroCursoModeloGeracaoParcelas() {
        return planoFinanceiroCursoModeloGeracaoParcelas;
    }

    /**
     * @param aPlanoFinanceiroCursoModeloGeracaoParcelas
     *            the planoFinanceiroCursoModeloGeracaoParcelas to set
     */
    public static void setPlanoFinanceiroCursoModeloGeracaoParcelas(Hashtable aPlanoFinanceiroCursoModeloGeracaoParcelas) {
        planoFinanceiroCursoModeloGeracaoParcelas = aPlanoFinanceiroCursoModeloGeracaoParcelas;
    }

    /**
     * @return the situacaoMatriculaPeriodoPreMatriculada
     */
    public static Hashtable getSituacaoMatriculaPeriodoPreMatriculada() {
        return situacaoMatriculaPeriodoPreMatriculada;
    }

    /**
     * @param aSituacaoMatriculaPeriodoPreMatriculada
     *            the situacaoMatriculaPeriodoPreMatriculada to set
     */
    public static void setSituacaoMatriculaPeriodoPreMatriculada(Hashtable aSituacaoMatriculaPeriodoPreMatriculada) {
        situacaoMatriculaPeriodoPreMatriculada = aSituacaoMatriculaPeriodoPreMatriculada;
    }

    public static Hashtable getMarcadorAlunoBancoCurriculum() {
        return marcadorAlunoBancoCurriculum;
    }

    public static void setMarcadorAlunoBancoCurriculum(Hashtable aMarcadorAlunoBancoCurriculum) {
        marcadorAlunoBancoCurriculum = aMarcadorAlunoBancoCurriculum;
    }

    public static Hashtable getMarcadorEmpresaBancoCurriculum() {
        return marcadorEmpresaBancoCurriculum;
    }

    public static void setMarcadorEmpresaBancoCurriculum(Hashtable aMarcadorEmpresaBancoCurriculum) {
        marcadorEmpresaBancoCurriculum = aMarcadorEmpresaBancoCurriculum;
    }

    public static Hashtable getMarcadorVagaBancoCurriculum() {
        return marcadorVagaBancoCurriculum;
    }

    public static void setMarcadorVagaBancoCurriculum(Hashtable marcadorVagaBancoCurriculum) {
        Dominios.marcadorVagaBancoCurriculum = marcadorVagaBancoCurriculum;
    }

    public static Hashtable getMarcadorTituloBancoCurriculum() {
        return marcadorTituloBancoCurriculum;
    }

    public static void setMarcadorTituloBancoCurriculum(Hashtable aMarcadorTituloBancoCurriculum) {
        marcadorTituloBancoCurriculum = aMarcadorTituloBancoCurriculum;
    }

	public static Hashtable getTipoDeclaracao() {
		return tipoDeclaracao;
	}

	public static void setTipoDeclaracao(Hashtable tipoDeclaracao) {
		Dominios.tipoDeclaracao = tipoDeclaracao;
	}

	public static Hashtable getSituacaoNotaFiscalSaida() {
		if (situacaoNotaFiscalSaida == null) {
			situacaoNotaFiscalSaida = new Hashtable();
		}
		return situacaoNotaFiscalSaida;
	}

	public static void setSituacaoNotaFiscalSaida(Hashtable situacaoNotaFiscalSaida) {
		Dominios.situacaoNotaFiscalSaida = situacaoNotaFiscalSaida;
	}

    public static Hashtable getMarcadoProcessoSeletivo() {
        return marcadoProcessoSeletivo;
    }

    public static void setMarcadoProcessoSeletivo(Hashtable marcadoProcessoSeletivo) {
        Dominios.marcadoProcessoSeletivo = marcadoProcessoSeletivo;
    }
    
    public static Hashtable getMarcadoResultadoProcessoSeletivo() {
    	return marcadoResultadoProcessoSeletivo;
    }
    
    public static void setMarcadoResultadoProcessoSeletivo(Hashtable marcadoResultadoProcessoSeletivo) {
    	Dominios.marcadoResultadoProcessoSeletivo = marcadoResultadoProcessoSeletivo;
    }
    
    public static Hashtable getMarcadoListaProcessoSeletivo() {
    	return marcadoListaProcessoSeletivo;
    }
    
    public static void setMarcadoListaProcessoSeletivo(Hashtable marcadoListaProcessoSeletivo) {
    	Dominios.marcadoListaProcessoSeletivo = marcadoListaProcessoSeletivo;
    }
    
    public static Hashtable getMarcadoInscricao() {
    	return marcadoInscricao;
    }
    
    public static void setMarcadoInscricao(Hashtable marcadoInscricao) {
    	Dominios.marcadoInscricao = marcadoInscricao;
    }
    
    public static Hashtable getMarcadoCandidato() {
    	return marcadoCandidato;
    }
    
    public static void setMarcadoCandidato(Hashtable marcadoCandidato) {
    	Dominios.marcadoCandidato = marcadoCandidato;
    }
}
