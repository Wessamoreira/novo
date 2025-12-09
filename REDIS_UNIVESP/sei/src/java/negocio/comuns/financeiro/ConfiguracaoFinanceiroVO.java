package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.HistoricoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.financeiro.enumerador.AmbienteCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.EmpresaOperadoraCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoParcelaNegociarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoFinanceiro. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ConfiguracaoFinanceiroVO extends SuperVO {

	private Integer codigo;
	private Double percentualJuroPadrao;
	private Double percentualMultaPadrao;
	private String tipoCalculoJuro;
	private Integer diaVencimentoParcelasPadrao;
	private Integer diaVencimentoMatriculaPadrao;
	private DescontoProgressivoVO descontoProgressivoPadrao;
	private Integer nrNiveisPlanoConta;
	private String mascaraPlanoConta;
	private Integer nrNiveisCategoriaDespesa;
	private String mascaraCategoriaDespesa;
	private Integer nrNiveisCentroReceita;
	private String mascaraCentroReceita;
	private CentroReceitaVO centroReceitaNegociacaoPadrao;
	private CentroReceitaVO centroReceitaMensalidadePadrao;
	private CentroReceitaVO centroReceitaMaterialDidaticoPadrao;
	private CentroReceitaVO centroReceitaMatriculaPadrao;
	private CentroReceitaVO centroReceitaBibliotecaPadrao;
	private CentroReceitaVO centroReceitaRequerimentoPadrao;
	private CentroReceitaVO centroReceitaReposicaoPadrao;
	private CentroReceitaVO centroReceitaInscricaoProcessoSeletivoPadrao;
	private CentroReceitaVO centroReceitaParcelaAvulsaControleCobranca;
	private Integer contaCorrentePadraoProcessoSeletivo;
	private Integer contaCorrentePadraoMatricula;
	private Integer contaCorrentePadraoMensalidade;
	private Integer contaCorrentePadraoMaterialDidatico;
	private Integer contaCorrentePadraoBiblioteca;
	private Integer contaCorrentePadraoRequerimento;
	private Integer contaCorrentePadraoNegociacao;
	private Integer contaCorrentePadraoDevolucaoCheque;
	private FormaPagamentoVO formaPagamentoPadraoCheque;
	private FormaPagamentoVO formaPagamentoPadraoProvisaoCusto;
	private CategoriaDespesaVO categoriaDespesaPadraoAntecipacaoCheque;
	private CategoriaDespesaVO categoriaDespesaPadraoRestituicaoAluno;
	private CategoriaDespesaVO categoriaDespesaOperadoraCartao;
	private DepartamentoVO departamentoPadraoAntecipacaoCheque;
	private String mensagemPadraoNotificacao;
	private String assuntoPadraoNotificacao;
	private ConfiguracoesVO configuracoesVO;
	private ModeloBoletoVO modeloBoletoMatricula;
	private ModeloBoletoVO modeloBoletoMensalidade;
	private ModeloBoletoVO modeloBoletoMaterialDidatico;
	private ModeloBoletoVO modeloBoletoRequerimento;
	private ModeloBoletoVO modeloBoletoProcessoSeletivo;
	private ModeloBoletoVO modeloBoletoOutros;
	private ModeloBoletoVO modeloBoletoRenegociacao;
	private ModeloBoletoVO modeloBoletoBiblioteca;
	private FormaPagamentoVO formaPagamentoPadraoControleCobranca;
	private ContaCorrenteVO contaCorrentePadraoControleCobranca;
	private PlanoContaVO planoContaPagarPadraoJuro;
	private PlanoContaVO planoContaPagarPadraoDesconto;
	private PlanoContaVO planoContaPagarPadraoCredito;
	private PlanoContaVO planoContaPagarPadraoDebito;
	private PlanoContaVO planoContaReceberPadraoJuro;
	private PlanoContaVO planoContaReceberPadraoDesconto;
	private PlanoContaVO planoContaReceberPadraoCredito;
	private PlanoContaVO planoContaReceberPadraoDebito;
	private HistoricoContabilVO historicoContaPagarPadraoJuro;
	private HistoricoContabilVO historicoContaPagarPadraoDesconto;
	private HistoricoContabilVO historicoContaReceberPadraoJuro;
	private HistoricoContabilVO historicoContaReceberPadraoDesconto;
	private Integer ordemDescontoAluno;
	private Boolean obrigatorioSelecionarUnidadeEnsinoControleCobranca;
	private Boolean ordemDescontoAlunoValorCheio;
	private Integer ordemPlanoDesconto;
	private Boolean ordemPlanoDescontoValorCheio;
	private Integer ordemConvenio;
	private Boolean ordemConvenioValorCheio;
	private Integer ordemDescontoProgressivo;
	private Boolean ordemDescontoProgressivoValorCheio;
	private Integer qtdeParcelasNegativacaoSerasa;
	private Integer qtdeMinimaDiasAntesNegativacaoSerasa;
	private Boolean usaChancela;
	private Boolean cobrarReimpressaoBoletos;
	private ModeloBoletoVO modeloBoletoReimpressao;
	private ContaCorrenteVO contaCorrenteReimpressaoBoletos;
	private CentroReceitaVO centroReceitaReimpressaoBoletos;
	private Double valorCobrarReimpressaoBoletos;
	private Boolean usaDescontoCompostoPlanoDesconto;
	private List<ConfiguracaoFinanceiroCartaoVO> listaConfiguracaoFinanceiroCartaoVO;
	private Boolean usaPlanoOrcamentario;
	private Boolean gerarBoletoComDescontoSemValidade;
	private Boolean vencimentoDescontoProgressivoDiaUtil;
	private Boolean vencimentoParcelaDiaUtil;
	private Boolean utilizaPlanoFinanceiroReposicao;
	private Boolean utilizaPlanoFinanceiroInclusao;
	private Boolean imprimirBoletoComLogoBanco;
	private Boolean usarContaCorrenteTurmaIncluida;
	private String recomendacaoRenegociacaoVisaoAluno;
	private Integer numeroDiasNotificarVencimentoContaReceber;
	private Boolean enviarNotificacaoConsultorMatricula;
	private String emailEnviarNotificacaoConsultorMatricula;
	private Integer numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor;
	private Boolean ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula;
	private Boolean confirmarMatricPendFinanCasoNaoControleMatricula;
	private String textoPadraoCartaCobranca;
	private Integer numeroDiasBloquearAcessoAlunoInadimplente;
	private Boolean imprimirBoletoComImagemLinhaDigitavel;
	private Integer quantidadeDiasEnviarMensagemCobrancaInadimplente;
	private Integer periodicidadeDiasEnviarMensagemCobrancaInadimplente;
	private GrupoDestinatariosVO grupoDestinatarioMensagemCobrancaInadimplente;
	private Boolean permitirGerarParcelaPreMatricula;
	private Boolean naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor;
	private Boolean naoApresAlunoInadimplenteDiarioEspelhoNota;
	private String tipoEnvioInadimplencia;
	private Integer quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente;
	private Integer quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente;
	private Integer quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente;
	private Integer quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente;
	private Integer quantidadeDiasEnviarAvisoDesconto;
	private Integer quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno;
	private Integer quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno;
	private Boolean permitiVisualizarContaReceberVisaoAlunoPreMatricula;
	private Boolean cancelarContaReceberCandidatoInadimplenteAposDataProva;
	private Integer qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente;
	private Boolean excluirNegociacaoRecebimentoVencida;
	private Boolean cobrarJuroMultaSobreValorCheioConta;
	private Boolean realizarMatriculaComFinanceiroManualAtivo;
	private Boolean utilizarIntegracaoFinanceira;
	private Boolean permiteNegociarParcelaMatricula;
	private Boolean permiteNegociarParcelaMensalidade;
	private boolean permiteNegociarParcelaMaterialDidatico = false;
	private Boolean permiteNegociarParcelaBiblioteca;
	private Boolean permiteNegociarParcelaOutras;
	private Boolean permiteNegociarParcelaContratoReceita;
	private Boolean permiteNegociarParcelaDevolucaoCheque;
	private Boolean permiteNegociarParcelaNegociacao;
	private Boolean permiteNegociarParcelaInclusaoReposicao;
	private Double valorMaximoCompraDiretaRequisicao;
	private IndiceReajusteVO indiceReajustePadraoContasPorAtrasoVO;
	private Integer qtdDiasAplicarIndireReajustePorAtrasoContaReceber;
	public static final long serialVersionUID = 1L;
	private Boolean alterarDataVencimentoParcelaDiaUtil;
	private Boolean bloquearCalouroPagarMatriculaVisaoAluno;
	private Boolean bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente;
	private boolean apresentarFormaRecebimentoContaReceberVisaoAluno = false;
	private Boolean criarContaReceberPendenciaArquivoRetornoAutomaticamente;
	private ArquivoVO arquivoIreportMovFin;
	private EmpresaOperadoraCartaoEnum operadora;
	private String tokenRede;
	private String pvRede;
	private String merchantKeyCielo;
	private String merchantIdCielo;
	private String nomeParcelaMatriculaApresentarAluno;
	private String siglaParcelaMatriculaApresentarAluno;
	private String nomeParcelaMaterialDidaticoApresentarAluno;
	private String siglaParcelaMaterialDidaticoApresentarAluno;
	private String observacaoComprovanteRecebimento;
	private Double valorMinimoGerarPendenciaControleCobranca;
	private Integer qtdeDiasExcluirNegociacaoContaReceberVencida;

	private CategoriaDespesaVO categoriaDespesaVO;
	private BancoVO bancoPadraoRemessa;
	private FormaPagamentoVO formaPagamentoPadrao;
	private Boolean tipoOrigemMatriculaRotinaInadimplencia;
	private Boolean tipoOrigemBibliotecaRotinaInadimplencia;
	private Boolean tipoOrigemMensalidadeRotinaInadimplencia;
	private Boolean tipoOrigemDevolucaoChequeRotinaInadimplencia;
	private Boolean tipoOrigemNegociacaoRotinaInadimplencia;
	private Boolean tipoOrigemContratoReceitaRotinaInadimplencia;
	private Boolean tipoOrigemOutrosRotinaInadimplencia;
	private Boolean tipoOrigemMaterialDidaticoRotinaInadimplencia;
	private Boolean tipoOrigemInclusaoReposicaoRotinaInadimplencia;
	private Boolean bloquearEmissaoBoletoPagamentoVencidoVisaoAluno;
	private Integer quantidadeDiasAtrasos;
	private Boolean bloquearDemaisParcelasVencidas;
	private Boolean bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco;
	private TipoParcelaNegociarEnum tipoParcelaNegociar;
	private String filtroPadraoContaReceberVisaoAluno;

	/**
	 * Construtor padrão da classe <code>ConfiguracaoFinanceiro</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ConfiguracaoFinanceiroVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ConfiguracaoFinanceiroVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(ConfiguracaoFinanceiroVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getConfiguracoesVO().getCodigo().intValue() == 0) {
			throw new ConsistirException("Esta configuração não pode ser salva, (CONFIGURAÇÕES) ainda não foi salvo");
		}
		if (obj.getEnviarNotificacaoConsultorMatricula()
				&& obj.getNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor() == 0) {
			throw new ConsistirException(
					"O campo 'Número de Dias para Enviar uma Notificação para o Consultor sobre as Matrículas Não Pagas' deve ser maior do que zero");
		}
		if (obj.getOrdemDescontoAluno().intValue() >= 4) {
			throw new ConsistirException("O campo 'Ordem de desconto do Aluno' deve ser um número entre 0 e 3");
		}
		if (obj.getOrdemPlanoDesconto().intValue() >= 4) {
			throw new ConsistirException("O campo 'Ordem do plano de Desconto' deve ser um número entre 0 e 3");
		}
		if (obj.getOrdemConvenio().intValue() >= 4) {
			throw new ConsistirException("O campo 'Ordem do convênio' deve ser um número entre 0 e 3");
		}
		if (obj.getOrdemDescontoProgressivo().intValue() >= 4) {
			throw new ConsistirException("O campo 'Ordem do desconto progressivo' deve ser um número entre 0 e 3");
		}
		Uteis.checkState(
				obj.isAplicarIndireReajustePorAtrasoContaReceber()
						&& !Uteis.isAtributoPreenchido(obj.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber()),
				"Como esta marcada a opção para \"Aplicar Índice de Reajuste por Atraso Conta Receber\" o Campo \"Quantidade de Dias Aplicar Índice de Reajuste por Atraso Conta Receber\" deve ser informado.  ");

		if (!obj.isAplicarIndireReajustePorAtrasoContaReceber()
				&& Uteis.isAtributoPreenchido(obj.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber())) {
			obj.setQtdDiasAplicarIndireReajustePorAtrasoContaReceber(0);
		}
		if (obj.getExcluirNegociacaoRecebimentoVencida()
				&& obj.getQtdeDiasExcluirNegociacaoContaReceberVencida() <= 0) {
			throw new ConsistirException("O campo " + UteisJSF
					.internacionalizar("prt_ConfiguracaoFinanceiro_qtdeDiasExcluirNegociacaoContaReceberVencida")
					.toUpperCase() + " deve ser maior que 0.");
		}
		if (obj.getQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente() > 0 || obj.getQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente() > 0
        		|| obj.getQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente() > 0 || obj.getQuantidadeDiasEnviarQuartaMensagemCobrancaInadimplente() > 0) {
        	if (!obj.getTipoOrigemMatriculaRotinaInadimplencia() && !obj.getTipoOrigemBibliotecaRotinaInadimplencia() && !obj.getTipoOrigemMensalidadeRotinaInadimplencia()
        			&& !obj.getTipoOrigemDevolucaoChequeRotinaInadimplencia() && !obj.getTipoOrigemNegociacaoRotinaInadimplencia() && !obj.getTipoOrigemContratoReceitaRotinaInadimplencia()
        			&& !obj.getTipoOrigemOutrosRotinaInadimplencia() && !obj.getTipoOrigemMaterialDidaticoRotinaInadimplencia() && !obj.getTipoOrigemInclusaoReposicaoRotinaInadimplencia()) {
        		throw new ConsistirException("Deve ser selecionado ao menos um Tipo de Origem Envio Inadimplência (Controle de Cobrança)");
        	}
        }
	}

	public ModeloBoletoVO getModeloBoleto(String tipoModelo) throws Exception {
		ModeloBoletoVO modeloBoleto = (ModeloBoletoVO) UtilReflexao.invocarMetodoGet(this, tipoModelo);
		return modeloBoleto;
	}

	public CentroReceitaVO getCentroReceitaInscricaoProcessoSeletivoPadrao() {
		if (centroReceitaInscricaoProcessoSeletivoPadrao == null) {
			centroReceitaInscricaoProcessoSeletivoPadrao = new CentroReceitaVO();
		}
		return centroReceitaInscricaoProcessoSeletivoPadrao;
	}

	public void setCentroReceitaInscricaoProcessoSeletivoPadrao(
			CentroReceitaVO centroReceitaInscricaoProcessoSeletivoPadrao) {
		this.centroReceitaInscricaoProcessoSeletivoPadrao = centroReceitaInscricaoProcessoSeletivoPadrao;
	}

	public CentroReceitaVO getCentroReceitaBibliotecaPadrao() {
		if (centroReceitaBibliotecaPadrao == null) {
			centroReceitaBibliotecaPadrao = new CentroReceitaVO();
		}
		return centroReceitaBibliotecaPadrao;
	}

	public void setCentroReceitaBibliotecaPadrao(CentroReceitaVO centroReceitaBibliotecaPadrao) {
		this.centroReceitaBibliotecaPadrao = centroReceitaBibliotecaPadrao;
	}

	public CentroReceitaVO getCentroReceitaMatriculaPadrao() {
		if (centroReceitaMatriculaPadrao == null) {
			centroReceitaMatriculaPadrao = new CentroReceitaVO();
		}
		return centroReceitaMatriculaPadrao;
	}

	public void setCentroReceitaMatriculaPadrao(CentroReceitaVO centroReceitaMatriculaPadrao) {
		this.centroReceitaMatriculaPadrao = centroReceitaMatriculaPadrao;
	}

	public CentroReceitaVO getCentroReceitaMensalidadePadrao() {
		if (centroReceitaMensalidadePadrao == null) {
			centroReceitaMensalidadePadrao = new CentroReceitaVO();
		}
		return centroReceitaMensalidadePadrao;
	}

	public void setCentroReceitaMensalidadePadrao(CentroReceitaVO centroReceitaMensalidadePadrao) {
		this.centroReceitaMensalidadePadrao = centroReceitaMensalidadePadrao;
	}

	public CentroReceitaVO getCentroReceitaRequerimentoPadrao() {
		if (centroReceitaRequerimentoPadrao == null) {
			centroReceitaRequerimentoPadrao = new CentroReceitaVO();
		}
		return centroReceitaRequerimentoPadrao;
	}

	public void setCentroReceitaRequerimentoPadrao(CentroReceitaVO centroReceitaRequerimentoPadrao) {
		this.centroReceitaRequerimentoPadrao = centroReceitaRequerimentoPadrao;
	}

	/**
	 * Retorna o objeto da classe <code>DescontoProgressivo</code> relacionado com
	 * (<code>ConfiguracaoFinanceiro</code> ).
	 */
	public DescontoProgressivoVO getDescontoProgressivoPadrao() {
		if (descontoProgressivoPadrao == null) {
			descontoProgressivoPadrao = new DescontoProgressivoVO();
		}
		return (descontoProgressivoPadrao);
	}

	/**
	 * Define o objeto da classe <code>DescontoProgressivo</code> relacionado com
	 * (<code>ConfiguracaoFinanceiro</code>).
	 */
	public void setDescontoProgressivoPadrao(DescontoProgressivoVO obj) {
		this.descontoProgressivoPadrao = obj;
	}

	public Integer getDiaVencimentoMatriculaPadrao() {
		if (diaVencimentoMatriculaPadrao == null) {
			diaVencimentoMatriculaPadrao = 0;
		}
		return (diaVencimentoMatriculaPadrao);
	}

	public void setDiaVencimentoMatriculaPadrao(Integer diaVencimentoMatriculaPadrao) {
		this.diaVencimentoMatriculaPadrao = diaVencimentoMatriculaPadrao;
	}

	public Integer getDiaVencimentoParcelasPadrao() {
		if (diaVencimentoParcelasPadrao == null) {
			diaVencimentoParcelasPadrao = 0;
		}
		return (diaVencimentoParcelasPadrao);
	}

	public void setDiaVencimentoParcelasPadrao(Integer diaVencimentoParcelasPadrao) {
		this.diaVencimentoParcelasPadrao = diaVencimentoParcelasPadrao;
	}

	public String getTipoCalculoJuro() {
		if (tipoCalculoJuro == null) {
			tipoCalculoJuro = "SI";
		}
		return (tipoCalculoJuro);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com
	 * um domínio específico. Com base no valor de armazenamento do atributo esta
	 * função é capaz de retornar o de apresentação correspondente. Útil para campos
	 * como sexo, escolaridade, etc.
	 */
	public String getTipoCalculoJuro_Apresentar() {
		if (tipoCalculoJuro.equals("SI")) {
			return "Simples";
		}
		if (tipoCalculoJuro.equals("CO")) {
			return "Composto";
		}
		return (tipoCalculoJuro);
	}

	public String getMascaraCategoriaDespesa() {
		if (mascaraCategoriaDespesa == null) {
			mascaraCategoriaDespesa = "xxx.xxx.xxx.xxx";
		}
		return mascaraCategoriaDespesa;
	}

	public void setMascaraCategoriaDespesa(String mascaraCategoriaDespesa) {
		this.mascaraCategoriaDespesa = mascaraCategoriaDespesa;
	}

	public String getMascaraCentroReceita() {
		if (mascaraCentroReceita == null) {
			mascaraCentroReceita = "xxx.xxx.xxx.xxx";
		}
		return mascaraCentroReceita;
	}

	public void setMascaraCentroReceita(String mascaraCentroReceita) {
		this.mascaraCentroReceita = mascaraCentroReceita;
	}

	public String getMascaraPlanoConta() {
		if (mascaraPlanoConta == null) {
			mascaraPlanoConta = "xxx.xxx.xxx";
		}
		return mascaraPlanoConta;
	}

	public void setMascaraPlanoConta(String mascaraPlanoConta) {
		this.mascaraPlanoConta = mascaraPlanoConta;
	}

	public Integer getNrNiveisCategoriaDespesa() {
		if (nrNiveisCategoriaDespesa == null) {
			nrNiveisCategoriaDespesa = 4;
		}
		return nrNiveisCategoriaDespesa;
	}

	public void setNrNiveisCategoriaDespesa(Integer nrNiveisCategoriaDespesa) {
		this.nrNiveisCategoriaDespesa = nrNiveisCategoriaDespesa;
	}

	public Integer getNrNiveisCentroReceita() {
		if (nrNiveisCentroReceita == null) {
			nrNiveisCentroReceita = 4;
		}
		return nrNiveisCentroReceita;
	}

	public void setNrNiveisCentroReceita(Integer nrNiveisCentroReceita) {
		this.nrNiveisCentroReceita = nrNiveisCentroReceita;
	}

	public Integer getNrNiveisPlanoConta() {
		if (nrNiveisPlanoConta == null) {
			nrNiveisPlanoConta = 3;
		}
		return nrNiveisPlanoConta;
	}

	public void setNrNiveisPlanoConta(Integer nrNiveisPlanoConta) {
		this.nrNiveisPlanoConta = nrNiveisPlanoConta;
	}

	public void setTipoCalculoJuro(String tipoCalculoJuro) {
		this.tipoCalculoJuro = tipoCalculoJuro;
	}

	public Double getPercentualMultaPadrao() {
		if (percentualMultaPadrao == null) {
			percentualMultaPadrao = 0.0;
		}
		return (percentualMultaPadrao);
	}

	public void setPercentualMultaPadrao(Double percentualMultaPadrao) {
		this.percentualMultaPadrao = percentualMultaPadrao;
	}

	public Double getPercentualJuroPadrao() {
		if (percentualJuroPadrao == null) {
			percentualJuroPadrao = 0.0;
		}
		return (percentualJuroPadrao);
	}

	public void setPercentualJuroPadrao(Double percentualJuroPadrao) {
		this.percentualJuroPadrao = percentualJuroPadrao;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getContaCorrentePadraoBiblioteca() {
		if (contaCorrentePadraoBiblioteca == null) {
			contaCorrentePadraoBiblioteca = 0;
		}
		return contaCorrentePadraoBiblioteca;
	}

	public void setContaCorrentePadraoBiblioteca(Integer contaCorrentePadraoBiblioteca) {
		this.contaCorrentePadraoBiblioteca = contaCorrentePadraoBiblioteca;
	}

	public Integer getContaCorrentePadraoMatricula() {
		if (contaCorrentePadraoMatricula == null) {
			contaCorrentePadraoMatricula = 0;
		}
		return contaCorrentePadraoMatricula;
	}

	public void setContaCorrentePadraoMatricula(Integer contaCorrentePadraoMatricula) {
		this.contaCorrentePadraoMatricula = contaCorrentePadraoMatricula;
	}

	public Integer getContaCorrentePadraoMensalidade() {
		if (contaCorrentePadraoMensalidade == null) {
			contaCorrentePadraoMensalidade = 0;
		}
		return contaCorrentePadraoMensalidade;
	}

	public void setContaCorrentePadraoMensalidade(Integer contaCorrentePadraoMensalidade) {
		this.contaCorrentePadraoMensalidade = contaCorrentePadraoMensalidade;
	}

	public Integer getContaCorrentePadraoProcessoSeletivo() {
		if (contaCorrentePadraoProcessoSeletivo == null) {
			contaCorrentePadraoProcessoSeletivo = 0;
		}
		return contaCorrentePadraoProcessoSeletivo;
	}

	public void setContaCorrentePadraoProcessoSeletivo(Integer contaCorrentePadraoProcessoSeletivo) {
		this.contaCorrentePadraoProcessoSeletivo = contaCorrentePadraoProcessoSeletivo;
	}

	public Integer getContaCorrentePadraoRequerimento() {
		if (contaCorrentePadraoRequerimento == null) {
			contaCorrentePadraoRequerimento = 0;
		}
		return contaCorrentePadraoRequerimento;
	}

	public void setContaCorrentePadraoRequerimento(Integer contaCorrentePadraoRequerimento) {
		this.contaCorrentePadraoRequerimento = contaCorrentePadraoRequerimento;
	}

	public CentroReceitaVO getCentroReceitaNegociacaoPadrao() {
		if (centroReceitaNegociacaoPadrao == null) {
			centroReceitaNegociacaoPadrao = new CentroReceitaVO();
		}
		return centroReceitaNegociacaoPadrao;
	}

	public void setCentroReceitaNegociacaoPadrao(CentroReceitaVO centroReceitaNegociacaoPadrao) {
		this.centroReceitaNegociacaoPadrao = centroReceitaNegociacaoPadrao;
	}

	public Integer getContaCorrentePadraoNegociacao() {
		if (contaCorrentePadraoNegociacao == null) {
			contaCorrentePadraoNegociacao = 0;
		}
		return contaCorrentePadraoNegociacao;
	}

	public void setContaCorrentePadraoNegociacao(Integer contaCorrentePadraoNegociacao) {
		this.contaCorrentePadraoNegociacao = contaCorrentePadraoNegociacao;
	}

	/**
	 * @return the formaPagamentoPadraoCheque
	 */
	public FormaPagamentoVO getFormaPagamentoPadraoCheque() {
		if (formaPagamentoPadraoCheque == null) {
			formaPagamentoPadraoCheque = new FormaPagamentoVO();
		}
		return formaPagamentoPadraoCheque;
	}

	/**
	 * @param formaPagamentoPadraoCheque
	 *            the formaPagamentoPadraoCheque to set
	 */
	public void setFormaPagamentoPadraoCheque(FormaPagamentoVO formaPagamentoPadraoCheque) {
		this.formaPagamentoPadraoCheque = formaPagamentoPadraoCheque;
	}

	/**
	 * @return the categoriaDEspesaPadraoAntecipacaoCheque
	 */
	public CategoriaDespesaVO getCategoriaDespesaPadraoAntecipacaoCheque() {
		if (categoriaDespesaPadraoAntecipacaoCheque == null) {
			categoriaDespesaPadraoAntecipacaoCheque = new CategoriaDespesaVO();
		}
		return categoriaDespesaPadraoAntecipacaoCheque;
	}

	/**
	 * @param categoriaDEspesaPadraoAntecipacaoCheque
	 *            the categoriaDEspesaPadraoAntecipacaoCheque to set
	 */
	public void setCategoriaDespesaPadraoAntecipacaoCheque(CategoriaDespesaVO categoriaDespesaPadraoAntecipacaoCheque) {
		this.categoriaDespesaPadraoAntecipacaoCheque = categoriaDespesaPadraoAntecipacaoCheque;
	}

	/**
	 * @return the departamentoPadraoAntecipacaoCheque
	 */
	public DepartamentoVO getDepartamentoPadraoAntecipacaoCheque() {
		if (departamentoPadraoAntecipacaoCheque == null) {
			departamentoPadraoAntecipacaoCheque = new DepartamentoVO();
		}
		return departamentoPadraoAntecipacaoCheque;
	}

	/**
	 * @param departamentoPadraoAntecipacaoCheque
	 *            the departamentoPadraoAntecipacaoCheque to set
	 */
	public void setDepartamentoPadraoAntecipacaoCheque(DepartamentoVO departamentoPadraoAntecipacaoCheque) {
		this.departamentoPadraoAntecipacaoCheque = departamentoPadraoAntecipacaoCheque;
	}

	/**
	 * @return the formaPagamentoPadraoProvisaoCusto
	 */
	public FormaPagamentoVO getFormaPagamentoPadraoProvisaoCusto() {
		if (formaPagamentoPadraoProvisaoCusto == null) {
			formaPagamentoPadraoProvisaoCusto = new FormaPagamentoVO();
		}
		return formaPagamentoPadraoProvisaoCusto;
	}

	/**
	 * @param formaPagamentoPadraoProvisaoCusto
	 *            the formaPagamentoPadraoProvisaoCusto to set
	 */
	public void setFormaPagamentoPadraoProvisaoCusto(FormaPagamentoVO formaPagamentoPadraoProvisaoCusto) {
		this.formaPagamentoPadraoProvisaoCusto = formaPagamentoPadraoProvisaoCusto;
	}

	/**
	 * @return the mensagemPadraoNotificacao
	 */
	public String getMensagemPadraoNotificacao() {
		if (mensagemPadraoNotificacao == null) {
			mensagemPadraoNotificacao = "";
		}
		return mensagemPadraoNotificacao;
	}

	/**
	 * @param mensagemPadraoNotificacao
	 *            the mensagemPadraoNotificacao to set
	 */
	public void setMensagemPadraoNotificacao(String mensagemPadraoNotificacao) {
		this.mensagemPadraoNotificacao = mensagemPadraoNotificacao;
	}

	/**
	 * @return the assuntoPadraoNotificacao
	 */
	public String getAssuntoPadraoNotificacao() {
		if (assuntoPadraoNotificacao == null) {
			assuntoPadraoNotificacao = "";
		}
		return assuntoPadraoNotificacao;
	}

	/**
	 * @param assuntoPadraoNotificacao
	 *            the assuntoPadraoNotificacao to set
	 */
	public void setAssuntoPadraoNotificacao(String assuntoPadraoNotificacao) {
		this.assuntoPadraoNotificacao = assuntoPadraoNotificacao;
	}

	/**
	 * @return the configuracoesVO
	 */
	public ConfiguracoesVO getConfiguracoesVO() {
		if (configuracoesVO == null) {
			configuracoesVO = new ConfiguracoesVO();
		}
		return configuracoesVO;
	}

	/**
	 * @param configuracoesVO
	 *            the configuracoesVO to set
	 */
	public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
		this.configuracoesVO = configuracoesVO;
	}

	/**
	 * @return the modeloBoletoMatricula
	 */
	public ModeloBoletoVO getModeloBoletoMatricula() {
		if (modeloBoletoMatricula == null) {
			modeloBoletoMatricula = new ModeloBoletoVO();
		}
		return modeloBoletoMatricula;
	}

	/**
	 * @param modeloBoletoMatricula
	 *            the modeloBoletoMatricula to set
	 */
	public void setModeloBoletoMatricula(ModeloBoletoVO modeloBoletoMatricula) {
		this.modeloBoletoMatricula = modeloBoletoMatricula;
	}

	/**
	 * @return the modeloBoletoMensalidade
	 */
	public ModeloBoletoVO getModeloBoletoMensalidade() {
		if (modeloBoletoMensalidade == null) {
			modeloBoletoMensalidade = new ModeloBoletoVO();
		}
		return modeloBoletoMensalidade;
	}

	/**
	 * @param modeloBoletoMensalidade
	 *            the modeloBoletoMensalidade to set
	 */
	public void setModeloBoletoMensalidade(ModeloBoletoVO modeloBoletoMensalidade) {
		this.modeloBoletoMensalidade = modeloBoletoMensalidade;
	}

	public ModeloBoletoVO getModeloBoletoMaterialDidatico() {
		if (modeloBoletoMaterialDidatico == null) {
			modeloBoletoMaterialDidatico = new ModeloBoletoVO();
		}
		return modeloBoletoMaterialDidatico;
	}

	public void setModeloBoletoMaterialDidatico(ModeloBoletoVO modeloBoletoMaterialDidatico) {
		this.modeloBoletoMaterialDidatico = modeloBoletoMaterialDidatico;
	}

	/**
	 * @return the modeloBoletoRequerimento
	 */
	public ModeloBoletoVO getModeloBoletoRequerimento() {
		if (modeloBoletoRequerimento == null) {
			modeloBoletoRequerimento = new ModeloBoletoVO();
		}
		return modeloBoletoRequerimento;
	}

	/**
	 * @param modeloBoletoRequerimento
	 *            the modeloBoletoRequerimento to set
	 */
	public void setModeloBoletoRequerimento(ModeloBoletoVO modeloBoletoRequerimento) {
		this.modeloBoletoRequerimento = modeloBoletoRequerimento;
	}

	/**
	 * @return the modeloBoletoProcessoSeletivo
	 */
	public ModeloBoletoVO getModeloBoletoProcessoSeletivo() {
		if (modeloBoletoProcessoSeletivo == null) {
			modeloBoletoProcessoSeletivo = new ModeloBoletoVO();
		}
		return modeloBoletoProcessoSeletivo;
	}

	/**
	 * @param modeloBoletoProcessoSeletivo
	 *            the modeloBoletoProcessoSeletivo to set
	 */
	public void setModeloBoletoProcessoSeletivo(ModeloBoletoVO modeloBoletoProcessoSeletivo) {
		this.modeloBoletoProcessoSeletivo = modeloBoletoProcessoSeletivo;
	}

	/**
	 * @return the modeloBoletoOutros
	 */
	public ModeloBoletoVO getModeloBoletoOutros() {
		if (modeloBoletoOutros == null) {
			modeloBoletoOutros = new ModeloBoletoVO();
		}
		return modeloBoletoOutros;
	}

	/**
	 * @param modeloBoletoOutros
	 *            the modeloBoletoOutros to set
	 */
	public void setModeloBoletoOutros(ModeloBoletoVO modeloBoletoOutros) {
		this.modeloBoletoOutros = modeloBoletoOutros;
	}

	public FormaPagamentoVO getFormaPagamentoPadraoControleCobranca() {
		if (formaPagamentoPadraoControleCobranca == null) {
			formaPagamentoPadraoControleCobranca = new FormaPagamentoVO();
		}
		return formaPagamentoPadraoControleCobranca;
	}

	public void setFormaPagamentoPadraoControleCobranca(FormaPagamentoVO formaPagamentoPadraoControleCobranca) {
		this.formaPagamentoPadraoControleCobranca = formaPagamentoPadraoControleCobranca;
	}

	public ContaCorrenteVO getContaCorrentePadraoControleCobranca() {
		if (contaCorrentePadraoControleCobranca == null) {
			contaCorrentePadraoControleCobranca = new ContaCorrenteVO();
		}
		return contaCorrentePadraoControleCobranca;
	}

	public void setContaCorrentePadraoControleCobranca(ContaCorrenteVO contaCorrentePadraoControleCobranca) {
		this.contaCorrentePadraoControleCobranca = contaCorrentePadraoControleCobranca;
	}

	public PlanoContaVO getPlanoContaPagarPadraoJuro() {
		if (planoContaPagarPadraoJuro == null) {
			planoContaPagarPadraoJuro = new PlanoContaVO();
		}
		return planoContaPagarPadraoJuro;
	}

	public void setPlanoContaPagarPadraoJuro(PlanoContaVO planoContaPagarPadraoJuro) {
		this.planoContaPagarPadraoJuro = planoContaPagarPadraoJuro;
	}

	public PlanoContaVO getPlanoContaPagarPadraoDesconto() {
		if (planoContaPagarPadraoDesconto == null) {
			planoContaPagarPadraoDesconto = new PlanoContaVO();
		}
		return planoContaPagarPadraoDesconto;
	}

	public void setPlanoContaPagarPadraoDesconto(PlanoContaVO planoContaPagarPadraoDesconto) {
		this.planoContaPagarPadraoDesconto = planoContaPagarPadraoDesconto;
	}

	public PlanoContaVO getPlanoContaPagarPadraoCredito() {
		if (planoContaPagarPadraoCredito == null) {
			planoContaPagarPadraoCredito = new PlanoContaVO();
		}
		return planoContaPagarPadraoCredito;
	}

	public void setPlanoContaPagarPadraoCredito(PlanoContaVO planoContaPagarPadraoCredito) {
		this.planoContaPagarPadraoCredito = planoContaPagarPadraoCredito;
	}

	public PlanoContaVO getPlanoContaPagarPadraoDebito() {
		if (planoContaPagarPadraoDebito == null) {
			planoContaPagarPadraoDebito = new PlanoContaVO();
		}
		return planoContaPagarPadraoDebito;
	}

	public void setPlanoContaPagarPadraoDebito(PlanoContaVO planoContaPagarPadraoDebito) {
		this.planoContaPagarPadraoDebito = planoContaPagarPadraoDebito;
	}

	public PlanoContaVO getPlanoContaReceberPadraoJuro() {
		if (planoContaReceberPadraoJuro == null) {
			planoContaReceberPadraoJuro = new PlanoContaVO();
		}
		return planoContaReceberPadraoJuro;
	}

	public void setPlanoContaReceberPadraoJuro(PlanoContaVO planoContaReceberPadraoJuro) {
		this.planoContaReceberPadraoJuro = planoContaReceberPadraoJuro;
	}

	public PlanoContaVO getPlanoContaReceberPadraoDesconto() {
		if (planoContaReceberPadraoDesconto == null) {
			planoContaReceberPadraoDesconto = new PlanoContaVO();
		}
		return planoContaReceberPadraoDesconto;
	}

	public void setPlanoContaReceberPadraoDesconto(PlanoContaVO planoContaReceberPadraoDesconto) {
		this.planoContaReceberPadraoDesconto = planoContaReceberPadraoDesconto;
	}

	public PlanoContaVO getPlanoContaReceberPadraoCredito() {
		if (planoContaReceberPadraoCredito == null) {
			planoContaReceberPadraoCredito = new PlanoContaVO();
		}
		return planoContaReceberPadraoCredito;
	}

	public void setPlanoContaReceberPadraoCredito(PlanoContaVO planoContaReceberPadraoCredito) {
		this.planoContaReceberPadraoCredito = planoContaReceberPadraoCredito;
	}

	public PlanoContaVO getPlanoContaReceberPadraoDebito() {
		if (planoContaReceberPadraoDebito == null) {
			planoContaReceberPadraoDebito = new PlanoContaVO();
		}
		return planoContaReceberPadraoDebito;
	}

	public void setPlanoContaReceberPadraoDebito(PlanoContaVO planoContaReceberPadraoDebito) {
		this.planoContaReceberPadraoDebito = planoContaReceberPadraoDebito;
	}

	public HistoricoContabilVO getHistoricoContaPagarPadraoDesconto() {
		if (historicoContaPagarPadraoDesconto == null) {
			historicoContaPagarPadraoDesconto = new HistoricoContabilVO();
		}
		return historicoContaPagarPadraoDesconto;
	}

	public void setHistoricoContaPagarPadraoDesconto(HistoricoContabilVO historicoContaPagarPadraoDesconto) {
		this.historicoContaPagarPadraoDesconto = historicoContaPagarPadraoDesconto;
	}

	public HistoricoContabilVO getHistoricoContaPagarPadraoJuro() {
		if (historicoContaPagarPadraoJuro == null) {
			historicoContaPagarPadraoJuro = new HistoricoContabilVO();
		}
		return historicoContaPagarPadraoJuro;
	}

	public void setHistoricoContaPagarPadraoJuro(HistoricoContabilVO historicoContaPagarPadraoJuro) {
		this.historicoContaPagarPadraoJuro = historicoContaPagarPadraoJuro;
	}

	public HistoricoContabilVO getHistoricoContaReceberPadraoDesconto() {
		if (historicoContaReceberPadraoDesconto == null) {
			historicoContaReceberPadraoDesconto = new HistoricoContabilVO();
		}
		return historicoContaReceberPadraoDesconto;
	}

	public void setHistoricoContaReceberPadraoDesconto(HistoricoContabilVO historicoContaReceberPadraoDesconto) {
		this.historicoContaReceberPadraoDesconto = historicoContaReceberPadraoDesconto;
	}

	public HistoricoContabilVO getHistoricoContaReceberPadraoJuro() {
		if (historicoContaReceberPadraoJuro == null) {
			historicoContaReceberPadraoJuro = new HistoricoContabilVO();
		}
		return historicoContaReceberPadraoJuro;
	}

	public void setHistoricoContaReceberPadraoJuro(HistoricoContabilVO historicoContaReceberPadraoJuro) {
		this.historicoContaReceberPadraoJuro = historicoContaReceberPadraoJuro;
	}

	/**
	 * @return the ordemDescontoAluno
	 */
	public Integer getOrdemDescontoAluno() {
		if (ordemDescontoAluno == null) {
			ordemDescontoAluno = 0;
		}
		return ordemDescontoAluno;
	}

	/**
	 * @param ordemDescontoAluno
	 *            the ordemDescontoAluno to set
	 */
	public void setOrdemDescontoAluno(Integer ordemDescontoAluno) {
		this.ordemDescontoAluno = ordemDescontoAluno;
	}

	/**
	 * @return the ordemDescontoAlunoValorCheio
	 */
	public Boolean getOrdemDescontoAlunoValorCheio() {
		if (ordemDescontoAlunoValorCheio == null) {
			ordemDescontoAlunoValorCheio = Boolean.FALSE;
		}
		return ordemDescontoAlunoValorCheio;
	}

	/**
	 * @param ordemDescontoAlunoValorCheio
	 *            the ordemDescontoAlunoValorCheio to set
	 */
	public void setOrdemDescontoAlunoValorCheio(Boolean ordemDescontoAlunoValorCheio) {
		this.ordemDescontoAlunoValorCheio = ordemDescontoAlunoValorCheio;
	}

	/**
	 * @return the ordemPlanoDesconto
	 */
	public Integer getOrdemPlanoDesconto() {
		if (ordemPlanoDesconto == null) {
			ordemPlanoDesconto = 0;
		}
		return ordemPlanoDesconto;
	}

	/**
	 * @param ordemPlanoDesconto
	 *            the ordemPlanoDesconto to set
	 */
	public void setOrdemPlanoDesconto(Integer ordemPlanoDesconto) {
		this.ordemPlanoDesconto = ordemPlanoDesconto;
	}

	/**
	 * @return the ordemPlanoDescontoValorCheio
	 */
	public Boolean getOrdemPlanoDescontoValorCheio() {
		if (ordemPlanoDescontoValorCheio == null) {
			ordemPlanoDescontoValorCheio = Boolean.FALSE;
		}
		return ordemPlanoDescontoValorCheio;
	}

	/**
	 * @param ordemPlanoDescontoValorCheio
	 *            the ordemPlanoDescontoValorCheio to set
	 */
	public void setOrdemPlanoDescontoValorCheio(Boolean ordemPlanoDescontoValorCheio) {
		this.ordemPlanoDescontoValorCheio = ordemPlanoDescontoValorCheio;
	}

	/**
	 * @return the ordemConvenio
	 */
	public Integer getOrdemConvenio() {
		if (ordemConvenio == null) {
			ordemConvenio = 0;
		}
		return ordemConvenio;
	}

	/**
	 * @param ordemConvenio
	 *            the ordemConvenio to set
	 */
	public void setOrdemConvenio(Integer ordemConvenio) {
		this.ordemConvenio = ordemConvenio;
	}

	/**
	 * @return the ordemConvenioValorCheio
	 */
	public Boolean getOrdemConvenioValorCheio() {
		if (ordemConvenioValorCheio == null) {
			ordemConvenioValorCheio = Boolean.FALSE;
		}
		return ordemConvenioValorCheio;
	}

	/**
	 * @param ordemConvenioValorCheio
	 *            the ordemConvenioValorCheio to set
	 */
	public void setOrdemConvenioValorCheio(Boolean ordemConvenioValorCheio) {
		this.ordemConvenioValorCheio = ordemConvenioValorCheio;
	}

	/**
	 * @return the ordemDescontoProgressivo
	 */
	public Integer getOrdemDescontoProgressivo() {
		if (ordemDescontoProgressivo == null) {
			ordemDescontoProgressivo = 0;
		}
		return ordemDescontoProgressivo;
	}

	/**
	 * @param ordemDescontoProgressivo
	 *            the ordemDescontoProgressivo to set
	 */
	public void setOrdemDescontoProgressivo(Integer ordemDescontoProgressivo) {
		this.ordemDescontoProgressivo = ordemDescontoProgressivo;
	}

	/**
	 * @return the ordemDescontoProgressivoValorCheio
	 */
	public Boolean getOrdemDescontoProgressivoValorCheio() {
		if (ordemDescontoProgressivoValorCheio == null) {
			ordemDescontoProgressivoValorCheio = Boolean.FALSE;
		}
		return ordemDescontoProgressivoValorCheio;
	}

	/**
	 * @param ordemDescontoProgressivoValorCheio
	 *            the ordemDescontoProgressivoValorCheio to set
	 */
	public void setOrdemDescontoProgressivoValorCheio(Boolean ordemDescontoProgressivoValorCheio) {
		this.ordemDescontoProgressivoValorCheio = ordemDescontoProgressivoValorCheio;
	}

	public void setQtdeParcelasNegativacaoSerasa(Integer qtdeParcelasNegativacaoSerasa) {
		this.qtdeParcelasNegativacaoSerasa = qtdeParcelasNegativacaoSerasa;
	}

	public Integer getQtdeParcelasNegativacaoSerasa() {
		if (qtdeParcelasNegativacaoSerasa == null) {
			qtdeParcelasNegativacaoSerasa = 0;
		}
		return qtdeParcelasNegativacaoSerasa;
	}

	public void setQtdeMinimaDiasAntesNegativacaoSerasa(Integer qtdeMinimaDiasAntesNegativacaoSerasa) {
		this.qtdeMinimaDiasAntesNegativacaoSerasa = qtdeMinimaDiasAntesNegativacaoSerasa;
	}

	public Integer getQtdeMinimaDiasAntesNegativacaoSerasa() {
		if (qtdeMinimaDiasAntesNegativacaoSerasa == null) {
			qtdeMinimaDiasAntesNegativacaoSerasa = 0;
		}
		return qtdeMinimaDiasAntesNegativacaoSerasa;
	}

	public Boolean getUsaChancela() {
		if (usaChancela == null) {
			usaChancela = Boolean.FALSE;
		}
		return usaChancela;
	}

	public void setUsaChancela(Boolean usaChancela) {
		this.usaChancela = usaChancela;
	}

	public void setCobrarReimpressaoBoletos(Boolean cobrarReimpressaoBoletos) {
		this.cobrarReimpressaoBoletos = cobrarReimpressaoBoletos;
	}

	public Boolean getCobrarReimpressaoBoletos() {
		if (cobrarReimpressaoBoletos == null) {
			cobrarReimpressaoBoletos = Boolean.FALSE;
		}
		return cobrarReimpressaoBoletos;
	}

	public void setContaCorrenteReimpressaoBoletos(ContaCorrenteVO contaCorrenteReimpressaoBoletos) {
		this.contaCorrenteReimpressaoBoletos = contaCorrenteReimpressaoBoletos;
	}

	public ContaCorrenteVO getContaCorrenteReimpressaoBoletos() {
		if (contaCorrenteReimpressaoBoletos == null) {
			contaCorrenteReimpressaoBoletos = new ContaCorrenteVO();
		}
		return contaCorrenteReimpressaoBoletos;
	}

	public void setCentroReceitaReimpressaoBoletos(CentroReceitaVO centroReceitaReimpressaoBoletos) {
		this.centroReceitaReimpressaoBoletos = centroReceitaReimpressaoBoletos;
	}

	public CentroReceitaVO getCentroReceitaReimpressaoBoletos() {
		if (centroReceitaReimpressaoBoletos == null) {
			centroReceitaReimpressaoBoletos = new CentroReceitaVO();
		}
		return centroReceitaReimpressaoBoletos;
	}

	public void setValorCobrarReimpressaoBoletos(Double valorCobrarReimpressaoBoletos) {
		this.valorCobrarReimpressaoBoletos = valorCobrarReimpressaoBoletos;
	}

	public Double getValorCobrarReimpressaoBoletos() {
		if (valorCobrarReimpressaoBoletos == null) {
			valorCobrarReimpressaoBoletos = 0.0;
		}
		return valorCobrarReimpressaoBoletos;
	}

	public void setModeloBoletoReimpressao(ModeloBoletoVO modeloBoletoReimpressao) {
		this.modeloBoletoReimpressao = modeloBoletoReimpressao;
	}

	public ModeloBoletoVO getModeloBoletoReimpressao() {
		if (modeloBoletoReimpressao == null) {
			modeloBoletoReimpressao = new ModeloBoletoVO();
		}
		return modeloBoletoReimpressao;
	}

	public Boolean getUsaDescontoCompostoPlanoDesconto() {
		if (usaDescontoCompostoPlanoDesconto == null) {
			usaDescontoCompostoPlanoDesconto = Boolean.TRUE;
		}
		return usaDescontoCompostoPlanoDesconto;
	}

	public void setUsaDescontoCompostoPlanoDesconto(Boolean usaDescontoCompostoPlanoDesconto) {
		this.usaDescontoCompostoPlanoDesconto = usaDescontoCompostoPlanoDesconto;
	}

	public List<ConfiguracaoFinanceiroCartaoVO> getListaConfiguracaoFinanceiroCartaoVO() {
		if (listaConfiguracaoFinanceiroCartaoVO == null) {
			listaConfiguracaoFinanceiroCartaoVO = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		}
		return listaConfiguracaoFinanceiroCartaoVO;
	}

	public void setListaConfiguracaoFinanceiroCartaoVO(
			List<ConfiguracaoFinanceiroCartaoVO> listaConfiguracaoFinanceiroCartaoVO) {
		this.listaConfiguracaoFinanceiroCartaoVO = listaConfiguracaoFinanceiroCartaoVO;
	}

	/**
	 * @return the usaPlanoOrcamentario
	 */
	public Boolean getUsaPlanoOrcamentario() {
		if (usaPlanoOrcamentario == null) {
			usaPlanoOrcamentario = Boolean.FALSE;
		}
		return usaPlanoOrcamentario;
	}

	/**
	 * @param usaPlanoOrcamentario
	 *            the usaPlanoOrcamentario to set
	 */
	public void setUsaPlanoOrcamentario(Boolean usaPlanoOrcamentario) {
		this.usaPlanoOrcamentario = usaPlanoOrcamentario;
	}

	public Boolean getGerarBoletoComDescontoSemValidade() {
		if (gerarBoletoComDescontoSemValidade == null) {
			gerarBoletoComDescontoSemValidade = Boolean.FALSE;
		}
		return gerarBoletoComDescontoSemValidade;
	}

	public void setGerarBoletoComDescontoSemValidade(Boolean gerarBoletoComDescontoSemValidade) {
		this.gerarBoletoComDescontoSemValidade = gerarBoletoComDescontoSemValidade;
	}

	public Boolean getVencimentoDescontoProgressivoDiaUtil() {
		if (vencimentoDescontoProgressivoDiaUtil == null) {
			vencimentoDescontoProgressivoDiaUtil = Boolean.FALSE;
		}
		return vencimentoDescontoProgressivoDiaUtil;
	}

	public void setVencimentoDescontoProgressivoDiaUtil(Boolean vencimentoDescontoProgressivoDiaUtil) {
		this.vencimentoDescontoProgressivoDiaUtil = vencimentoDescontoProgressivoDiaUtil;
	}

	public Boolean getObrigatorioSelecionarUnidadeEnsinoControleCobranca() {
		if (obrigatorioSelecionarUnidadeEnsinoControleCobranca == null) {
			obrigatorioSelecionarUnidadeEnsinoControleCobranca = Boolean.FALSE;
		}
		return obrigatorioSelecionarUnidadeEnsinoControleCobranca;
	}

	public void setObrigatorioSelecionarUnidadeEnsinoControleCobranca(
			Boolean obrigatorioSelecionarUnidadeEnsinoControleCobranca) {
		this.obrigatorioSelecionarUnidadeEnsinoControleCobranca = obrigatorioSelecionarUnidadeEnsinoControleCobranca;
	}

	public Boolean getUtilizaPlanoFinanceiroReposicao() {
		if (utilizaPlanoFinanceiroReposicao == null) {
			utilizaPlanoFinanceiroReposicao = Boolean.FALSE;
		}
		return utilizaPlanoFinanceiroReposicao;
	}

	public void setUtilizaPlanoFinanceiroReposicao(Boolean utilizaPlanoFinanceiroReposicao) {
		this.utilizaPlanoFinanceiroReposicao = utilizaPlanoFinanceiroReposicao;
	}

	public Boolean getUtilizaPlanoFinanceiroInclusao() {
		if (utilizaPlanoFinanceiroInclusao == null) {
			utilizaPlanoFinanceiroInclusao = Boolean.FALSE;
		}
		return utilizaPlanoFinanceiroInclusao;
	}

	public void setUtilizaPlanoFinanceiroInclusao(Boolean utilizaPlanoFinanceiroInclusao) {
		this.utilizaPlanoFinanceiroInclusao = utilizaPlanoFinanceiroInclusao;
	}

	public Boolean getImprimirBoletoComLogoBanco() {
		if (imprimirBoletoComLogoBanco == null) {
			imprimirBoletoComLogoBanco = Boolean.FALSE;
		}
		return imprimirBoletoComLogoBanco;
	}

	public void setImprimirBoletoComLogoBanco(Boolean imprimirBoletoComLogoBanco) {
		this.imprimirBoletoComLogoBanco = imprimirBoletoComLogoBanco;
	}

	public Boolean getUsarContaCorrenteTurmaIncluida() {
		if (usarContaCorrenteTurmaIncluida == null) {
			usarContaCorrenteTurmaIncluida = Boolean.FALSE;
		}
		return usarContaCorrenteTurmaIncluida;
	}

	public void setUsarContaCorrenteTurmaIncluida(Boolean usarContaCorrenteTurmaIncluida) {
		this.usarContaCorrenteTurmaIncluida = usarContaCorrenteTurmaIncluida;
	}

	public String getRecomendacaoRenegociacaoVisaoAluno() {
		if (recomendacaoRenegociacaoVisaoAluno == null) {
			recomendacaoRenegociacaoVisaoAluno = "";
		}
		return recomendacaoRenegociacaoVisaoAluno;
	}

	public void setRecomendacaoRenegociacaoVisaoAluno(String recomendacaoRenegociacaoVisaoAluno) {
		this.recomendacaoRenegociacaoVisaoAluno = recomendacaoRenegociacaoVisaoAluno;
	}

	public Integer getNumeroDiasNotificarVencimentoContaReceber() {
		if (numeroDiasNotificarVencimentoContaReceber == null) {
			numeroDiasNotificarVencimentoContaReceber = 0;
		}
		return numeroDiasNotificarVencimentoContaReceber;
	}

	public void setNumeroDiasNotificarVencimentoContaReceber(Integer numeroDiasNotificarVencimentoContaReceber) {
		this.numeroDiasNotificarVencimentoContaReceber = numeroDiasNotificarVencimentoContaReceber;
	}

	public Boolean getEnviarNotificacaoConsultorMatricula() {
		if (enviarNotificacaoConsultorMatricula == null) {
			enviarNotificacaoConsultorMatricula = Boolean.FALSE;
		}
		return enviarNotificacaoConsultorMatricula;
	}

	public void setEnviarNotificacaoConsultorMatricula(Boolean enviarNotificacaoConsultorMatricula) {
		this.enviarNotificacaoConsultorMatricula = enviarNotificacaoConsultorMatricula;
	}

	public Integer getNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor() {
		if (numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor == null) {
			numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor = 0;
		}
		return numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor;
	}

	public void setNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor(
			Integer numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor) {

		this.numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor = numeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor;
	}

	public Boolean getVencimentoParcelaDiaUtil() {
		if (vencimentoParcelaDiaUtil == null) {
			vencimentoParcelaDiaUtil = Boolean.FALSE;
		}
		return vencimentoParcelaDiaUtil;
	}

	public void setVencimentoParcelaDiaUtil(Boolean vencimentoParcelaDiaUtil) {
		this.vencimentoParcelaDiaUtil = vencimentoParcelaDiaUtil;
	}

	/**
	 * @return the ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula
	 */
	public Boolean getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula() {
		if (ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula == null) {
			ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula = Boolean.TRUE;
		}
		return ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula;
	}

	/**
	 * @param ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula
	 *            the ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula to
	 *            set
	 */
	public void setAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula(
			Boolean ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula) {
		this.ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula = ativarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula;
	}

	public String getTextoPadraoCartaCobranca() {
		if (textoPadraoCartaCobranca == null) {
			textoPadraoCartaCobranca = "";
		}
		return textoPadraoCartaCobranca;
	}

	public void setTextoPadraoCartaCobranca(String textoPadraoCartaCobranca) {
		this.textoPadraoCartaCobranca = textoPadraoCartaCobranca;
	}

	public Integer getQuantidadeDiasEnviarMensagemCobrancaInadimplente() {
		if (quantidadeDiasEnviarMensagemCobrancaInadimplente == null) {
			quantidadeDiasEnviarMensagemCobrancaInadimplente = 30;
		}
		return quantidadeDiasEnviarMensagemCobrancaInadimplente;
	}

	public void setQuantidadeDiasEnviarMensagemCobrancaInadimplente(
			Integer quantidadeDiasEnviarMensagemCobrancaInadimplente) {
		this.quantidadeDiasEnviarMensagemCobrancaInadimplente = quantidadeDiasEnviarMensagemCobrancaInadimplente;
	}

	public Integer getPeriodicidadeDiasEnviarMensagemCobrancaInadimplente() {
		if (periodicidadeDiasEnviarMensagemCobrancaInadimplente == null) {
			periodicidadeDiasEnviarMensagemCobrancaInadimplente = 30;
		}
		return periodicidadeDiasEnviarMensagemCobrancaInadimplente;
	}

	public void setPeriodicidadeDiasEnviarMensagemCobrancaInadimplente(
			Integer periodicidadeDiasEnviarMensagemCobrancaInadimplente) {
		this.periodicidadeDiasEnviarMensagemCobrancaInadimplente = periodicidadeDiasEnviarMensagemCobrancaInadimplente;
	}

	public GrupoDestinatariosVO getGrupoDestinatarioMensagemCobrancaInadimplente() {
		if (grupoDestinatarioMensagemCobrancaInadimplente == null) {
			grupoDestinatarioMensagemCobrancaInadimplente = new GrupoDestinatariosVO();
		}
		return grupoDestinatarioMensagemCobrancaInadimplente;
	}

	public void setGrupoDestinatarioMensagemCobrancaInadimplente(
			GrupoDestinatariosVO grupoDestinatarioMensagemCobrancaInadimplente) {
		this.grupoDestinatarioMensagemCobrancaInadimplente = grupoDestinatarioMensagemCobrancaInadimplente;
	}

	public ModeloBoletoVO getModeloBoletoRenegociacao() {
		if (modeloBoletoRenegociacao == null) {
			modeloBoletoRenegociacao = new ModeloBoletoVO();
		}
		return modeloBoletoRenegociacao;
	}

	public void setModeloBoletoRenegociacao(ModeloBoletoVO modeloBoletoRenegociacao) {
		this.modeloBoletoRenegociacao = modeloBoletoRenegociacao;
	}

	public Boolean getPermitirGerarParcelaPreMatricula() {
		if (permitirGerarParcelaPreMatricula == null) {
			permitirGerarParcelaPreMatricula = Boolean.TRUE;
		}
		return permitirGerarParcelaPreMatricula;
	}

	public void setPermitirGerarParcelaPreMatricula(Boolean permitirGerarParcelaPreMatricula) {
		this.permitirGerarParcelaPreMatricula = permitirGerarParcelaPreMatricula;
	}

	public String getEmailEnviarNotificacaoConsultorMatricula() {
		if (emailEnviarNotificacaoConsultorMatricula == null) {
			emailEnviarNotificacaoConsultorMatricula = "";
		}
		return emailEnviarNotificacaoConsultorMatricula;
	}

	public void setEmailEnviarNotificacaoConsultorMatricula(String emailEnviarNotificacaoConsultorMatricula) {
		this.emailEnviarNotificacaoConsultorMatricula = emailEnviarNotificacaoConsultorMatricula;
	}

	public Integer getNumeroDiasBloquearAcessoAlunoInadimplente() {
		if (numeroDiasBloquearAcessoAlunoInadimplente == null) {
			numeroDiasBloquearAcessoAlunoInadimplente = 0;
		}
		return numeroDiasBloquearAcessoAlunoInadimplente;
	}

	public void setNumeroDiasBloquearAcessoAlunoInadimplente(Integer numeroDiasBloquearAcessoAlunoInadimplente) {
		this.numeroDiasBloquearAcessoAlunoInadimplente = numeroDiasBloquearAcessoAlunoInadimplente;
	}

	public String getTipoEnvioInadimplencia() {
		if (tipoEnvioInadimplencia == null) {
			tipoEnvioInadimplencia = "fixo";
		}
		return tipoEnvioInadimplencia;
	}

	public void setTipoEnvioInadimplencia(String tipoEnvioInadimplencia) {
		this.tipoEnvioInadimplencia = tipoEnvioInadimplencia;
	}

	public Integer getQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente() {
		if (quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente == null) {
			quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente = 0;
		}
		return quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente;
	}

	public void setQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente(
			Integer quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente) {
		this.quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente = quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente;
	}

	public Integer getQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente() {
		if (quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente == null) {
			quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente = 0;
		}
		return quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente;
	}

	public void setQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente(
			Integer quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente) {
		this.quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente = quantidadeDiasEnviarSegundaMensagemCobrancaInadimplente;
	}

	public Integer getQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente() {
		if (quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente == null) {
			quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente = 0;
		}
		return quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente;
	}

	public void setQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente(
			Integer quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente) {
		this.quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente = quantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente;
	}

	public Integer getQuantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno() {
		if (quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno == null) {
			quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno = 0;
		}
		return quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno;
	}

	public void setQuantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno(
			Integer quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno) {
		this.quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno = quantidadeDiasAntesVencimentoApresentarContaReceberVisaoAluno;
	}

	public Boolean getNaoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor() {
		if (naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor == null) {
			naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor = Boolean.FALSE;
		}
		return naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor;
	}

	public void setNaoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor(
			Boolean naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor) {
		this.naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor = naoApresAlunoInadimplenteDiarioEspelhoNotaProfCoor;
	}

	public Boolean getNaoApresAlunoInadimplenteDiarioEspelhoNota() {
		if (naoApresAlunoInadimplenteDiarioEspelhoNota == null) {
			naoApresAlunoInadimplenteDiarioEspelhoNota = Boolean.FALSE;
		}
		return naoApresAlunoInadimplenteDiarioEspelhoNota;
	}

	public void setNaoApresAlunoInadimplenteDiarioEspelhoNota(Boolean naoApresAlunoInadimplenteDiarioEspelhoNota) {
		this.naoApresAlunoInadimplenteDiarioEspelhoNota = naoApresAlunoInadimplenteDiarioEspelhoNota;
	}

	public Integer getQuantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno() {
		if (quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno == null) {
			quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno = 0;
		}
		return quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno;
	}

	public void setQuantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno(
			Integer quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno) {
		this.quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno = quantidadeDiasAntesVencimentoApresentarContaReceberMatVisaoAluno;
	}

	public CentroReceitaVO getCentroReceitaReposicaoPadrao() {
		if (centroReceitaReposicaoPadrao == null) {
			centroReceitaReposicaoPadrao = new CentroReceitaVO();
		}
		return centroReceitaReposicaoPadrao;
	}

	public void setCentroReceitaReposicaoPadrao(CentroReceitaVO centroReceitaReposicaoPadrao) {
		this.centroReceitaReposicaoPadrao = centroReceitaReposicaoPadrao;
	}

	public Integer getQuantidadeDiasEnviarQuartaMensagemCobrancaInadimplente() {
		if (quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente == null) {
			quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente = 0;
		}
		return quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente;
	}

	public void setQuantidadeDiasEnviarQuartaMensagemCobrancaInadimplente(
			Integer quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente) {
		this.quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente = quantidadeDiasEnviarQuartaMensagemCobrancaInadimplente;
	}

	public Integer getQuantidadeDiasEnviarAvisoDesconto() {
		quantidadeDiasEnviarAvisoDesconto = Optional.ofNullable(quantidadeDiasEnviarAvisoDesconto).orElse(0);
		return quantidadeDiasEnviarAvisoDesconto;
	}

	public void setQuantidadeDiasEnviarAvisoDesconto(Integer quantidadeDiasEnviarAvisoDesconto) {
		this.quantidadeDiasEnviarAvisoDesconto = quantidadeDiasEnviarAvisoDesconto;
	}

	public CentroReceitaVO getCentroReceitaParcelaAvulsaControleCobranca() {
		if (centroReceitaParcelaAvulsaControleCobranca == null) {
			centroReceitaParcelaAvulsaControleCobranca = new CentroReceitaVO();
		}
		return centroReceitaParcelaAvulsaControleCobranca;
	}

	public void setCentroReceitaParcelaAvulsaControleCobranca(
			CentroReceitaVO centroReceitaParcelaAvulsaControleCobranca) {
		this.centroReceitaParcelaAvulsaControleCobranca = centroReceitaParcelaAvulsaControleCobranca;
	}

	public Boolean getCancelarContaReceberCandidatoInadimplenteAposDataProva() {
		if (cancelarContaReceberCandidatoInadimplenteAposDataProva == null) {
			cancelarContaReceberCandidatoInadimplenteAposDataProva = false;
		}
		return cancelarContaReceberCandidatoInadimplenteAposDataProva;
	}

	public void setCancelarContaReceberCandidatoInadimplenteAposDataProva(
			Boolean cancelarContaReceberCandidatoInadimplenteAposDataProva) {
		this.cancelarContaReceberCandidatoInadimplenteAposDataProva = cancelarContaReceberCandidatoInadimplenteAposDataProva;
	}

	public Integer getQtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente() {
		if (qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente == null) {
			qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente = 0;
		}
		return qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente;
	}

	public void setQtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente(
			Integer qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente) {
		this.qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente = qtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente;
	}

	public CategoriaDespesaVO getCategoriaDespesaOperadoraCartao() {
		if (categoriaDespesaOperadoraCartao == null) {
			categoriaDespesaOperadoraCartao = new CategoriaDespesaVO();
		}
		return categoriaDespesaOperadoraCartao;
	}

	public void setCategoriaDespesaOperadoraCartao(CategoriaDespesaVO categoriaDespesaOperadoraCartao) {
		this.categoriaDespesaOperadoraCartao = categoriaDespesaOperadoraCartao;
	}

	public Boolean getExcluirNegociacaoRecebimentoVencida() {
		if (excluirNegociacaoRecebimentoVencida == null) {
			excluirNegociacaoRecebimentoVencida = Boolean.FALSE;
		}
		return excluirNegociacaoRecebimentoVencida;
	}

	public void setExcluirNegociacaoRecebimentoVencida(Boolean excluirNegociacaoRecebimentoVencida) {
		this.excluirNegociacaoRecebimentoVencida = excluirNegociacaoRecebimentoVencida;
	}

	public Boolean getPermitiVisualizarContaReceberVisaoAlunoPreMatricula() {
		if (permitiVisualizarContaReceberVisaoAlunoPreMatricula == null) {
			permitiVisualizarContaReceberVisaoAlunoPreMatricula = Boolean.FALSE;
		}
		return permitiVisualizarContaReceberVisaoAlunoPreMatricula;
	}

	public void setPermitiVisualizarContaReceberVisaoAlunoPreMatricula(
			Boolean permitiVisualizarContaReceberVisaoAlunoPreMatricula) {
		this.permitiVisualizarContaReceberVisaoAlunoPreMatricula = permitiVisualizarContaReceberVisaoAlunoPreMatricula;
	}

	public Boolean getCobrarJuroMultaSobreValorCheioConta() {
		if (cobrarJuroMultaSobreValorCheioConta == null) {
			cobrarJuroMultaSobreValorCheioConta = Boolean.FALSE;
		}
		return cobrarJuroMultaSobreValorCheioConta;
	}

	public void setCobrarJuroMultaSobreValorCheioConta(Boolean cobrarJuroMultaSobreValorCheioConta) {
		this.cobrarJuroMultaSobreValorCheioConta = cobrarJuroMultaSobreValorCheioConta;
	}

	/**
	 * @return the contaCorrentePadraoDevolucaoCheque
	 */
	public Integer getContaCorrentePadraoDevolucaoCheque() {
		if (contaCorrentePadraoDevolucaoCheque == null) {
			contaCorrentePadraoDevolucaoCheque = 0;
		}
		return contaCorrentePadraoDevolucaoCheque;
	}

	/**
	 * @param contaCorrentePadraoDevolucaoCheque
	 *            the contaCorrentePadraoDevolucaoCheque to set
	 */
	public void setContaCorrentePadraoDevolucaoCheque(Integer contaCorrentePadraoDevolucaoCheque) {
		this.contaCorrentePadraoDevolucaoCheque = contaCorrentePadraoDevolucaoCheque;
	}

	/**
	 * @author Victor Hugo de Paula Costa 20/05/2015 11:25
	 */
	private String idClienteMundiPag;

	public Boolean getRealizarMatriculaComFinanceiroManualAtivo() {
		if (realizarMatriculaComFinanceiroManualAtivo == null) {
			realizarMatriculaComFinanceiroManualAtivo = false;
		}
		return realizarMatriculaComFinanceiroManualAtivo;
	}

	public void setRealizarMatriculaComFinanceiroManualAtivo(Boolean realizarMatriculaComFinanceiroManualAtivo) {
		this.realizarMatriculaComFinanceiroManualAtivo = realizarMatriculaComFinanceiroManualAtivo;
	}

	public Boolean getUtilizarIntegracaoFinanceira() {
		if (utilizarIntegracaoFinanceira == null) {
			utilizarIntegracaoFinanceira = false;
		}
		return utilizarIntegracaoFinanceira;
	}

	public void setUtilizarIntegracaoFinanceira(Boolean utilizarIntegracaoFinanceira) {
		this.utilizarIntegracaoFinanceira = utilizarIntegracaoFinanceira;
	}

	public String getIdClienteMundiPag() {
		if (idClienteMundiPag == null) {
			idClienteMundiPag = "";
		}
		return idClienteMundiPag;
	}

	public void setIdClienteMundiPag(String idClienteMundiPag) {
		this.idClienteMundiPag = idClienteMundiPag;
	}

	public Boolean getConfirmarMatricPendFinanCasoNaoControleMatricula() {
		if (confirmarMatricPendFinanCasoNaoControleMatricula == null) {
			confirmarMatricPendFinanCasoNaoControleMatricula = Boolean.FALSE;
		}
		return confirmarMatricPendFinanCasoNaoControleMatricula;
	}

	public void setConfirmarMatricPendFinanCasoNaoControleMatricula(
			Boolean confirmarMatricPendFinanCasoNaoControleMatricula) {
		this.confirmarMatricPendFinanCasoNaoControleMatricula = confirmarMatricPendFinanCasoNaoControleMatricula;
	}

	private AmbienteCartaoCreditoEnum ambienteCartaoCreditoEnum;

	public AmbienteCartaoCreditoEnum getAmbienteCartaoCreditoEnum() {
		if (ambienteCartaoCreditoEnum == null) {
			ambienteCartaoCreditoEnum = AmbienteCartaoCreditoEnum.HOMOLOGACAO;
		}
		return ambienteCartaoCreditoEnum;
	}

	public void setAmbienteCartaoCreditoEnum(AmbienteCartaoCreditoEnum ambienteCartaoCreditoEnum) {
		this.ambienteCartaoCreditoEnum = ambienteCartaoCreditoEnum;
	}

	public Boolean getImprimirBoletoComImagemLinhaDigitavel() {
		if (imprimirBoletoComImagemLinhaDigitavel == null) {
			imprimirBoletoComImagemLinhaDigitavel = Boolean.FALSE;
		}
		return imprimirBoletoComImagemLinhaDigitavel;
	}

	public void setImprimirBoletoComImagemLinhaDigitavel(Boolean imprimirBoletoComImagemLinhaDigitavel) {
		this.imprimirBoletoComImagemLinhaDigitavel = imprimirBoletoComImagemLinhaDigitavel;
	}

	/**
	 * @author Victor Hugo de Paula Costa 03/11/2015 15:19
	 */
	private String chaveContaMundipagg;
	private Boolean utilizarv1;

	public String getChaveContaMundipagg() {
		if (chaveContaMundipagg == null) {
			chaveContaMundipagg = "";
		}
		return chaveContaMundipagg;
	}

	public void setChaveContaMundipagg(String chaveContaMundipagg) {
		this.chaveContaMundipagg = chaveContaMundipagg;
	}

	public Boolean getUtilizarv1() {
		if (utilizarv1 == null) {
			utilizarv1 = true;
		}
		return utilizarv1;
	}

	public void setUtilizarv1(Boolean utilizarv1) {
		this.utilizarv1 = utilizarv1;
	}

	/**
	 * @return the bloquearCalouroPagarMatriculaVisaoAluno
	 */
	public Boolean getBloquearCalouroPagarMatriculaVisaoAluno() {
		if (bloquearCalouroPagarMatriculaVisaoAluno == null) {
			bloquearCalouroPagarMatriculaVisaoAluno = Boolean.FALSE;
		}
		return bloquearCalouroPagarMatriculaVisaoAluno;
	}

	/**
	 * @param bloquearCalouroPagarMatriculaVisaoAluno
	 *            the bloquearCalouroPagarMatriculaVisaoAluno to set
	 */
	public void setBloquearCalouroPagarMatriculaVisaoAluno(Boolean bloquearCalouroPagarMatriculaVisaoAluno) {
		this.bloquearCalouroPagarMatriculaVisaoAluno = bloquearCalouroPagarMatriculaVisaoAluno;
	}

	/**
	 * @return the bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente
	 */
	public Boolean getBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente() {
		if (bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente == null) {
			bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente = Boolean.FALSE;
		}
		return bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente;
	}

	/**
	 * @param bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente
	 *            the bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente to set
	 */
	public void setBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente(
			Boolean bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente) {
		this.bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente = bloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente;
	}

	public Boolean getPermiteNegociarParcelaMatricula() {
		if (permiteNegociarParcelaMatricula == null) {
			permiteNegociarParcelaMatricula = Boolean.TRUE;
		}
		return permiteNegociarParcelaMatricula;
	}

	public void setPermiteNegociarParcelaMatricula(Boolean permiteNegociarParcelaMatricula) {
		this.permiteNegociarParcelaMatricula = permiteNegociarParcelaMatricula;
	}

	public Boolean getPermiteNegociarParcelaMensalidade() {
		if (permiteNegociarParcelaMensalidade == null) {
			permiteNegociarParcelaMensalidade = Boolean.TRUE;
		}
		return permiteNegociarParcelaMensalidade;
	}

	public void setPermiteNegociarParcelaMensalidade(Boolean permiteNegociarParcelaMensalidade) {
		this.permiteNegociarParcelaMensalidade = permiteNegociarParcelaMensalidade;
	}

	public boolean isPermiteNegociarParcelaMaterialDidatico() {
		return permiteNegociarParcelaMaterialDidatico;
	}

	public void setPermiteNegociarParcelaMaterialDidatico(boolean permiteNegociarParcelaMaterialDidatico) {
		this.permiteNegociarParcelaMaterialDidatico = permiteNegociarParcelaMaterialDidatico;
	}

	public Boolean getPermiteNegociarParcelaBiblioteca() {
		if (permiteNegociarParcelaBiblioteca == null) {
			permiteNegociarParcelaBiblioteca = Boolean.FALSE;
		}
		return permiteNegociarParcelaBiblioteca;
	}

	public void setPermiteNegociarParcelaBiblioteca(Boolean permiteNegociarParcelaBiblioteca) {
		this.permiteNegociarParcelaBiblioteca = permiteNegociarParcelaBiblioteca;
	}

	public Boolean getPermiteNegociarParcelaOutras() {
		if (permiteNegociarParcelaOutras == null) {
			permiteNegociarParcelaOutras = Boolean.FALSE;
		}
		return permiteNegociarParcelaOutras;
	}

	public void setPermiteNegociarParcelaOutras(Boolean permiteNegociarParcelaOutras) {
		this.permiteNegociarParcelaOutras = permiteNegociarParcelaOutras;
	}

	public Boolean getPermiteNegociarParcelaContratoReceita() {
		if (permiteNegociarParcelaContratoReceita == null) {
			permiteNegociarParcelaContratoReceita = Boolean.FALSE;
		}
		return permiteNegociarParcelaContratoReceita;
	}

	public void setPermiteNegociarParcelaContratoReceita(Boolean permiteNegociarParcelaContratoReceita) {
		this.permiteNegociarParcelaContratoReceita = permiteNegociarParcelaContratoReceita;
	}

	public Boolean getPermiteNegociarParcelaDevolucaoCheque() {
		if (permiteNegociarParcelaDevolucaoCheque == null) {
			permiteNegociarParcelaDevolucaoCheque = Boolean.FALSE;
		}
		return permiteNegociarParcelaDevolucaoCheque;
	}

	public void setPermiteNegociarParcelaDevolucaoCheque(Boolean permiteNegociarParcelaDevolucaoCheque) {
		this.permiteNegociarParcelaDevolucaoCheque = permiteNegociarParcelaDevolucaoCheque;
	}

	public Boolean getPermiteNegociarParcelaNegociacao() {
		if (permiteNegociarParcelaNegociacao == null) {
			permiteNegociarParcelaNegociacao = Boolean.FALSE;
		}
		return permiteNegociarParcelaNegociacao;
	}

	public void setPermiteNegociarParcelaNegociacao(Boolean permiteNegociarParcelaNegociacao) {
		this.permiteNegociarParcelaNegociacao = permiteNegociarParcelaNegociacao;
	}

	public Boolean getPermiteNegociarParcelaInclusaoReposicao() {
		if (permiteNegociarParcelaInclusaoReposicao == null) {
			permiteNegociarParcelaInclusaoReposicao = Boolean.FALSE;
		}
		return permiteNegociarParcelaInclusaoReposicao;
	}

	public void setPermiteNegociarParcelaInclusaoReposicao(Boolean permiteNegociarParcelaInclusaoReposicao) {
		this.permiteNegociarParcelaInclusaoReposicao = permiteNegociarParcelaInclusaoReposicao;
	}

	public ModeloBoletoVO getModeloBoletoBiblioteca() {
		if (modeloBoletoBiblioteca == null) {
			modeloBoletoBiblioteca = new ModeloBoletoVO();
		}
		return modeloBoletoBiblioteca;
	}

	public void setModeloBoletoBiblioteca(ModeloBoletoVO modeloBoletoBiblioteca) {
		this.modeloBoletoBiblioteca = modeloBoletoBiblioteca;
	}

	public boolean isApresentarFormaRecebimentoContaReceberVisaoAluno() {
		return apresentarFormaRecebimentoContaReceberVisaoAluno;
	}

	public void setApresentarFormaRecebimentoContaReceberVisaoAluno(
			boolean apresentarFormaRecebimentoContaReceberVisaoAluno) {
		this.apresentarFormaRecebimentoContaReceberVisaoAluno = apresentarFormaRecebimentoContaReceberVisaoAluno;
	}

	public CategoriaDespesaVO getCategoriaDespesaPadraoRestituicaoAluno() {
		if (categoriaDespesaPadraoRestituicaoAluno == null) {
			categoriaDespesaPadraoRestituicaoAluno = new CategoriaDespesaVO();
		}
		return categoriaDespesaPadraoRestituicaoAluno;
	}

	public void setCategoriaDespesaPadraoRestituicaoAluno(CategoriaDespesaVO categoriaDespesaPadraoRestituicaoAluno) {
		this.categoriaDespesaPadraoRestituicaoAluno = categoriaDespesaPadraoRestituicaoAluno;
	}

	public Boolean getCriarContaReceberPendenciaArquivoRetornoAutomaticamente() {
		if (criarContaReceberPendenciaArquivoRetornoAutomaticamente == null) {
			criarContaReceberPendenciaArquivoRetornoAutomaticamente = false;
		}
		return criarContaReceberPendenciaArquivoRetornoAutomaticamente;
	}

	public void setCriarContaReceberPendenciaArquivoRetornoAutomaticamente(
			Boolean criarContaReceberPendenciaArquivoRetornoAutomaticamente) {
		this.criarContaReceberPendenciaArquivoRetornoAutomaticamente = criarContaReceberPendenciaArquivoRetornoAutomaticamente;
	}

	public CentroReceitaVO getCentroReceitaMaterialDidaticoPadrao() {
		if (centroReceitaMaterialDidaticoPadrao == null) {
			centroReceitaMaterialDidaticoPadrao = new CentroReceitaVO();
		}
		return centroReceitaMaterialDidaticoPadrao;
	}

	public void setCentroReceitaMaterialDidaticoPadrao(CentroReceitaVO centroReceitaMaterialDidaticoPadrao) {
		this.centroReceitaMaterialDidaticoPadrao = centroReceitaMaterialDidaticoPadrao;
	}

	public Integer getContaCorrentePadraoMaterialDidatico() {
		if (contaCorrentePadraoMaterialDidatico == null) {
			contaCorrentePadraoMaterialDidatico = 0;
		}
		return contaCorrentePadraoMaterialDidatico;
	}

	public void setContaCorrentePadraoMaterialDidatico(Integer contaCorrentePadraoMaterialDidatico) {
		this.contaCorrentePadraoMaterialDidatico = contaCorrentePadraoMaterialDidatico;
	}

	public ArquivoVO getArquivoIreportMovFin() {
		if (arquivoIreportMovFin == null) {
			arquivoIreportMovFin = new ArquivoVO();
		}
		return arquivoIreportMovFin;
	}

	public void setArquivoIreportMovFin(ArquivoVO arquivoIreportMovFin) {
		this.arquivoIreportMovFin = arquivoIreportMovFin;
	}

	public Boolean getAlterarDataVencimentoParcelaDiaUtil() {
		if (alterarDataVencimentoParcelaDiaUtil == null) {
			alterarDataVencimentoParcelaDiaUtil = Boolean.FALSE;
		}
		return alterarDataVencimentoParcelaDiaUtil;
	}

	public void setAlterarDataVencimentoParcelaDiaUtil(Boolean alterarDataVencimentoParcelaDiaUtil) {
		this.alterarDataVencimentoParcelaDiaUtil = alterarDataVencimentoParcelaDiaUtil;
	}

	public String getMerchantKeyCielo() {
		if (merchantKeyCielo == null) {
			merchantKeyCielo = "";
		}
		return merchantKeyCielo;
	}

	public void setMerchantKeyCielo(String merchantKeyCielo) {
		this.merchantKeyCielo = merchantKeyCielo;
	}

		public String getNomeParcelaMatriculaApresentarAluno() {
			if(nomeParcelaMatriculaApresentarAluno == null){
				nomeParcelaMatriculaApresentarAluno = "";
    		}
			return nomeParcelaMatriculaApresentarAluno;
		}		

		public void setNomeParcelaMatriculaApresentarAluno(String nomeParcelaMatriculaApresentarAluno) {
			this.nomeParcelaMatriculaApresentarAluno = nomeParcelaMatriculaApresentarAluno;
		}
		
		public String getSiglaParcelaMatriculaApresentarAluno() {
			if(siglaParcelaMatriculaApresentarAluno == null){
				siglaParcelaMatriculaApresentarAluno = "";
    		}
			return siglaParcelaMatriculaApresentarAluno;
		}

		public void setSiglaParcelaMatriculaApresentarAluno(String siglaParcelaMatriculaApresentarAluno) {
			this.siglaParcelaMatriculaApresentarAluno = siglaParcelaMatriculaApresentarAluno;
		}

		public String getNomeParcelaMaterialDidaticoApresentarAluno() {
			if(nomeParcelaMaterialDidaticoApresentarAluno == null){
				nomeParcelaMaterialDidaticoApresentarAluno = "";
    		}
			return nomeParcelaMaterialDidaticoApresentarAluno;
		}

		public void setNomeParcelaMaterialDidaticoApresentarAluno(String nomeParcelaMaterialDidaticoApresentarAluno) {
			this.nomeParcelaMaterialDidaticoApresentarAluno = nomeParcelaMaterialDidaticoApresentarAluno;
		}
		
		public String getSiglaParcelaMaterialDidaticoApresentarAluno() {
			if(siglaParcelaMaterialDidaticoApresentarAluno == null){
				siglaParcelaMaterialDidaticoApresentarAluno = "";
    		}
			return siglaParcelaMaterialDidaticoApresentarAluno;
		}

		public void setSiglaParcelaMaterialDidaticoApresentarAluno(String siglaParcelaMaterialDidaticoApresentarAluno) {
			this.siglaParcelaMaterialDidaticoApresentarAluno = siglaParcelaMaterialDidaticoApresentarAluno;
		}

	public String getMerchantIdCielo() {
		if (merchantIdCielo == null) {
			merchantIdCielo = "";
		}
		return merchantIdCielo;
	}

	public void setMerchantIdCielo(String merchantIdCielo) {
		this.merchantIdCielo = merchantIdCielo;
	}

	public String getObservacaoComprovanteRecebimento() {
		if (observacaoComprovanteRecebimento == null) {
			observacaoComprovanteRecebimento = "";
		}
		return observacaoComprovanteRecebimento;
	}

	public void setObservacaoComprovanteRecebimento(String observacaoComprovanteRecebimento) {
		this.observacaoComprovanteRecebimento = observacaoComprovanteRecebimento;
	}

	public Double getValorMaximoCompraDiretaRequisicao() {
		valorMaximoCompraDiretaRequisicao = Optional.ofNullable(valorMaximoCompraDiretaRequisicao).orElse(0.0);
		return valorMaximoCompraDiretaRequisicao;
	}

	public void setValorMaximoCompraDiretaRequisicao(Double valorMaximoCompraDiretaRequisicao) {
		this.valorMaximoCompraDiretaRequisicao = valorMaximoCompraDiretaRequisicao;
	}

	public IndiceReajusteVO getIndiceReajustePadraoContasPorAtrasoVO() {
		if (indiceReajustePadraoContasPorAtrasoVO == null) {
			indiceReajustePadraoContasPorAtrasoVO = new IndiceReajusteVO();
		}
		return indiceReajustePadraoContasPorAtrasoVO;
	}

	public boolean isAplicarIndireReajustePorAtrasoContaReceber() {
		return Uteis.isAtributoPreenchido(getIndiceReajustePadraoContasPorAtrasoVO());
	}

	public void setIndiceReajustePadraoContasPorAtrasoVO(IndiceReajusteVO indiceReajustePadraoContasPorAtrasoVO) {
		this.indiceReajustePadraoContasPorAtrasoVO = indiceReajustePadraoContasPorAtrasoVO;
	}

	public Integer getQtdDiasAplicarIndireReajustePorAtrasoContaReceber() {
		if (qtdDiasAplicarIndireReajustePorAtrasoContaReceber == null) {
			qtdDiasAplicarIndireReajustePorAtrasoContaReceber = 0;
		}
		return qtdDiasAplicarIndireReajustePorAtrasoContaReceber;
	}

	public void setQtdDiasAplicarIndireReajustePorAtrasoContaReceber(
			Integer qtdDiasAplicarIndireReajustePorAtrasoContaReceber) {
		this.qtdDiasAplicarIndireReajustePorAtrasoContaReceber = qtdDiasAplicarIndireReajustePorAtrasoContaReceber;
	}

	public Double getValorMinimoGerarPendenciaControleCobranca() {
		if (valorMinimoGerarPendenciaControleCobranca == null) {
			valorMinimoGerarPendenciaControleCobranca = 0.01;
		}
		return valorMinimoGerarPendenciaControleCobranca;
	}

	public void setValorMinimoGerarPendenciaControleCobranca(Double valorMinimoGerarPendenciaControleCobranca) {
		this.valorMinimoGerarPendenciaControleCobranca = valorMinimoGerarPendenciaControleCobranca;
	}

	public Integer getQtdeDiasExcluirNegociacaoContaReceberVencida() {
		if (qtdeDiasExcluirNegociacaoContaReceberVencida == null) {
			qtdeDiasExcluirNegociacaoContaReceberVencida = 1;
		}
		return qtdeDiasExcluirNegociacaoContaReceberVencida;
	}

	public void setQtdeDiasExcluirNegociacaoContaReceberVencida(Integer qtdeDiasExcluirNegociacaoContaReceberVencida) {
		this.qtdeDiasExcluirNegociacaoContaReceberVencida = qtdeDiasExcluirNegociacaoContaReceberVencida;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public BancoVO getBancoPadraoRemessa() {
		if (bancoPadraoRemessa == null) {
			bancoPadraoRemessa = new BancoVO();
		}
		return bancoPadraoRemessa;
	}

	public void setBancoPadraoRemessa(BancoVO bancoPadraoRemessa) {
		this.bancoPadraoRemessa = bancoPadraoRemessa;
	}

	public FormaPagamentoVO getFormaPagamentoPadrao() {
		if (formaPagamentoPadrao == null) {
			formaPagamentoPadrao = new FormaPagamentoVO();
		}
		return formaPagamentoPadrao;
	}

	public void setFormaPagamentoPadrao(FormaPagamentoVO formaPagamentoPadrao) {
		this.formaPagamentoPadrao = formaPagamentoPadrao;
	}

	public EmpresaOperadoraCartaoEnum getOperadora() {
		if (operadora == null) {
			operadora = EmpresaOperadoraCartaoEnum.NENHUM;
		}
		return operadora;
	}

	public void setOperadora(EmpresaOperadoraCartaoEnum operadora) {
		this.operadora = operadora;
	}

	public String getTokenRede() {
		if (tokenRede == null) {
			tokenRede = "";
		}
		return tokenRede;
	}

	public void setTokenRede(String tokenRede) {
		this.tokenRede = tokenRede;
	}

	public String getPvRede() {
		if (pvRede == null) {
			pvRede = "";
		}
		return pvRede;
	}

	public void setPvRede(String pvRede) {
		this.pvRede = pvRede;
	}
	
	public boolean getIsOperadoraCielo() {
		return EmpresaOperadoraCartaoEnum.CIELO.equals(getOperadora());
	}
	
	public boolean getIsOperadoraRede() {
		return EmpresaOperadoraCartaoEnum.REDE.equals(getOperadora());
	}
	
	public Boolean getTipoOrigemMatriculaRotinaInadimplencia() {
		if (tipoOrigemMatriculaRotinaInadimplencia == null) {
			tipoOrigemMatriculaRotinaInadimplencia = true;
		}
		return tipoOrigemMatriculaRotinaInadimplencia;
	}

	public void setTipoOrigemMatriculaRotinaInadimplencia(Boolean tipoOrigemMatriculaRotinaInadimplencia) {
		this.tipoOrigemMatriculaRotinaInadimplencia = tipoOrigemMatriculaRotinaInadimplencia;
	}

	public Boolean getTipoOrigemBibliotecaRotinaInadimplencia() {
		if (tipoOrigemBibliotecaRotinaInadimplencia == null) {
			tipoOrigemBibliotecaRotinaInadimplencia = true;
		}
		return tipoOrigemBibliotecaRotinaInadimplencia;
	}

	public void setTipoOrigemBibliotecaRotinaInadimplencia(Boolean tipoOrigemBibliotecaRotinaInadimplencia) {
		this.tipoOrigemBibliotecaRotinaInadimplencia = tipoOrigemBibliotecaRotinaInadimplencia;
	}

	public Boolean getTipoOrigemMensalidadeRotinaInadimplencia() {
		if (tipoOrigemMensalidadeRotinaInadimplencia == null) {
			tipoOrigemMensalidadeRotinaInadimplencia = true;
		}
		return tipoOrigemMensalidadeRotinaInadimplencia;
	}

	public void setTipoOrigemMensalidadeRotinaInadimplencia(Boolean tipoOrigemMensalidadeRotinaInadimplencia) {
		this.tipoOrigemMensalidadeRotinaInadimplencia = tipoOrigemMensalidadeRotinaInadimplencia;
	}

	public Boolean getTipoOrigemDevolucaoChequeRotinaInadimplencia() {
		if (tipoOrigemDevolucaoChequeRotinaInadimplencia == null) {
			tipoOrigemDevolucaoChequeRotinaInadimplencia = true;
		}
		return tipoOrigemDevolucaoChequeRotinaInadimplencia;
	}

	public void setTipoOrigemDevolucaoChequeRotinaInadimplencia(Boolean tipoOrigemDevolucaoChequeRotinaInadimplencia) {
		this.tipoOrigemDevolucaoChequeRotinaInadimplencia = tipoOrigemDevolucaoChequeRotinaInadimplencia;
	}

	public Boolean getTipoOrigemNegociacaoRotinaInadimplencia() {
		if (tipoOrigemNegociacaoRotinaInadimplencia == null) {
			tipoOrigemNegociacaoRotinaInadimplencia = true;
		}
		return tipoOrigemNegociacaoRotinaInadimplencia;
	}

	public void setTipoOrigemNegociacaoRotinaInadimplencia(Boolean tipoOrigemNegociacaoRotinaInadimplencia) {
		this.tipoOrigemNegociacaoRotinaInadimplencia = tipoOrigemNegociacaoRotinaInadimplencia;
	}

	public Boolean getTipoOrigemContratoReceitaRotinaInadimplencia() {
		if (tipoOrigemContratoReceitaRotinaInadimplencia == null) {
			tipoOrigemContratoReceitaRotinaInadimplencia = true;
		}
		return tipoOrigemContratoReceitaRotinaInadimplencia;
	}

	public void setTipoOrigemContratoReceitaRotinaInadimplencia(Boolean tipoOrigemContratoReceitaRotinaInadimplencia) {
		this.tipoOrigemContratoReceitaRotinaInadimplencia = tipoOrigemContratoReceitaRotinaInadimplencia;
	}

	public Boolean getTipoOrigemOutrosRotinaInadimplencia() {
		if (tipoOrigemOutrosRotinaInadimplencia == null) {
			tipoOrigemOutrosRotinaInadimplencia = true;
		}
		return tipoOrigemOutrosRotinaInadimplencia;
	}

	public void setTipoOrigemOutrosRotinaInadimplencia(Boolean tipoOrigemOutrosRotinaInadimplencia) {
		this.tipoOrigemOutrosRotinaInadimplencia = tipoOrigemOutrosRotinaInadimplencia;
	}

	public Boolean getTipoOrigemMaterialDidaticoRotinaInadimplencia() {
		if (tipoOrigemMaterialDidaticoRotinaInadimplencia == null) {
			tipoOrigemMaterialDidaticoRotinaInadimplencia = true;
		}
		return tipoOrigemMaterialDidaticoRotinaInadimplencia;
	}

	public void setTipoOrigemMaterialDidaticoRotinaInadimplencia(Boolean tipoOrigemMaterialDidaticoRotinaInadimplencia) {
		this.tipoOrigemMaterialDidaticoRotinaInadimplencia = tipoOrigemMaterialDidaticoRotinaInadimplencia;
	}

	public Boolean getTipoOrigemInclusaoReposicaoRotinaInadimplencia() {
		if (tipoOrigemInclusaoReposicaoRotinaInadimplencia == null) {
			tipoOrigemInclusaoReposicaoRotinaInadimplencia = true;
		}
		return tipoOrigemInclusaoReposicaoRotinaInadimplencia;
	}

	public void setTipoOrigemInclusaoReposicaoRotinaInadimplencia(Boolean tipoOrigemInclusaoReposicaoRotinaInadimplencia) {
		this.tipoOrigemInclusaoReposicaoRotinaInadimplencia = tipoOrigemInclusaoReposicaoRotinaInadimplencia;
	}	
	
	public void realizarMarcarTodasOrigens(){
		realizarSelecaoTodasOrigens(true);
	}
	
	public void realizarDesmarcarTodasOrigens(){
		realizarSelecaoTodasOrigens(false);
	}
	public Boolean getBloquearEmissaoBoletoPagamentoVencidoVisaoAluno() {
		if(bloquearEmissaoBoletoPagamentoVencidoVisaoAluno == null) {
			bloquearEmissaoBoletoPagamentoVencidoVisaoAluno = false;
		}
		return bloquearEmissaoBoletoPagamentoVencidoVisaoAluno;
	}

	public void setBloquearEmissaoBoletoPagamentoVencidoVisaoAluno(
			Boolean bloquearEmissaoBoletoPagamentoVencidoVisaoAluno) {
		this.bloquearEmissaoBoletoPagamentoVencidoVisaoAluno = bloquearEmissaoBoletoPagamentoVencidoVisaoAluno;
	}
	
	

	public Integer getQuantidadeDiasAtrasos() {
		if(quantidadeDiasAtrasos == null) {
			quantidadeDiasAtrasos = 1;
		}
		return quantidadeDiasAtrasos;
	}

	public void setQuantidadeDiasAtrasos(Integer quantidadeDiasAtrasos) {
		this.quantidadeDiasAtrasos = quantidadeDiasAtrasos;
	}
	
	

	public Boolean getBloquearDemaisParcelasVencidas() {
		if(bloquearDemaisParcelasVencidas == null) {
			bloquearDemaisParcelasVencidas = false;
		}
		return bloquearDemaisParcelasVencidas;
	}

	public void setBloquearDemaisParcelasVencidas(Boolean bloquearDemaisParcelasVencidas) {
		this.bloquearDemaisParcelasVencidas = bloquearDemaisParcelasVencidas;
	}
	
	public boolean getIsBloquearDemaisParcelasVencidas() {
		if(!this.bloquearEmissaoBoletoPagamentoVencidoVisaoAluno) {
			return false;
		}
		return bloquearDemaisParcelasVencidas;
	}
	
	

	public Boolean getBloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco() {
		if(bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco == null) {
			bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco = false;
		}
		return bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco;
	}

	public void setBloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco(
			Boolean bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco) {
		this.bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco = bloquearEmissaodeBoletoPagamentoParcelasPendenteReajustePreco;
	}
	
	public void realizarSelecaoTodasOrigens(boolean selecionado){
		setTipoOrigemMatriculaRotinaInadimplencia(selecionado);
		setTipoOrigemBibliotecaRotinaInadimplencia(selecionado);
		setTipoOrigemMensalidadeRotinaInadimplencia(selecionado);
		setTipoOrigemDevolucaoChequeRotinaInadimplencia(selecionado);
		setTipoOrigemNegociacaoRotinaInadimplencia(selecionado);
		setTipoOrigemContratoReceitaRotinaInadimplencia(selecionado);
		setTipoOrigemOutrosRotinaInadimplencia(selecionado);
		setTipoOrigemMaterialDidaticoRotinaInadimplencia(selecionado);
		setTipoOrigemInclusaoReposicaoRotinaInadimplencia(selecionado);
	}
	
	public TipoParcelaNegociarEnum getTipoParcelaNegociar() {
		if (tipoParcelaNegociar == null) {
			tipoParcelaNegociar = TipoParcelaNegociarEnum.VENCIDAS;
		}
		return tipoParcelaNegociar;
	}

	public void setTipoParcelaNegociar(TipoParcelaNegociarEnum tipoParcelaNegociar) {
		this.tipoParcelaNegociar = tipoParcelaNegociar;
	}

	public String getFiltroPadraoContaReceberVisaoAluno() {
		if (filtroPadraoContaReceberVisaoAluno == null) {
			filtroPadraoContaReceberVisaoAluno = "MA";
		}
		return filtroPadraoContaReceberVisaoAluno;
	}

	public void setFiltroPadraoContaReceberVisaoAluno(String filtroPadraoContaReceberVisaoAluno) {
		this.filtroPadraoContaReceberVisaoAluno = filtroPadraoContaReceberVisaoAluno;
	}
	
	
}