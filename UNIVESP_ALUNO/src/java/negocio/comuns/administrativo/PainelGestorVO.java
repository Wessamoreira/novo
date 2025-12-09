/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 *
 * @author Otimize-Not
 */
public class PainelGestorVO extends SuperVO {

 
    private String coresLegendaGraficoAcademicoFinanceiro;
    
  
    /**
     * Atributos Abaixo Utilizado no Painel Gestor Financeiro - Mapa Receitas
     */
    private Double totalReceberAnteriorPeriodo;
    private Double totalProvisaoPeriodoPrimeiroDesconto;
    private Double totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade;
    private Double totalProvisaoPeriodoPrimeiroDescontoOutras;
    private Double totalProvisaoPeriodo;
    private Double totalProvisaoPeriodoOutros;
    private Double totalValorCalculadoInadimplenciaPeriodo;    
    private Double totalRecebidoDoPeriodo;
    private Double totalRecebidoNoPeriodo;
    private Double saldoReceberPeriodo;
    private Double saldoReceberPeriodoMatricula;
    private Double saldoReceberPeriodoMaterialDidatico;
    private Double saldoReceberPeriodoMensalidade;
    private Double saldoReceberPeriodoRequerimento;
    private Double saldoReceberPeriodoBiblioteca;
    private Double saldoReceberPeriodoDevolucaoCheque;    
    private Double saldoReceberPeriodoNegociacao;
    private Double saldoReceberPeriodoInscricao;
    private Double saldoReceberPeriodoBolsaCusteada;
    private Double saldoReceberPeriodoContratoReceita;
    private Double saldoReceberPeriodoInclusaoReposicao;
    private Double saldoReceberPeriodoOutros;
    
    private Double saldoReceberVencidoPeriodo;
    private Double saldoReceberVencidoMatricula;
    private Double saldoReceberVencidoMaterialDidatico;
    private Double saldoReceberVencidoMensalidade;
    private Double saldoReceberVencidoRequerimento;
    private Double saldoReceberVencidoBiblioteca;
    private Double saldoReceberVencidoDevolucaoCheque;    
    private Double saldoReceberVencidoNegociacao;
    private Double saldoReceberVencidoInscricao;
    private Double saldoReceberVencidoBolsaCusteada;
    private Double saldoReceberVencidoContratoReceita;
    private Double saldoReceberVencidoInclusaoReposicao;
    private Double saldoReceberVencidoOutros;
    
    
    
    private Double saldoReceberDoPeriodoPrimeiroDesconto;
    private Double saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade;
    private Double saldoReceberDoPeriodoPrimeiroDescontoOutras;
    private Double mediaInadimplenciaNoPeriodo;
    private Double mediaInadimplenciaNoPeriodoSemAcrescimo;
    private Double mediaInadimplenciaDoPeriodo;
    private Double mediaInadimplenciaDoPeriodoSemAcrescimo;
    private Double provisaoReceberMesMatricula;
    private Double provisaoReceberMesMaterialDidatico;
    private Double provisaoReceberMesMensalidade;
    private Double provisaoReceberMesRequerimento;
    private Double provisaoReceberMesBiblioteca;
    private Double provisaoReceberMesDevolucaoCheque;    
    private Double provisaoReceberMesNegociacao;
    private Double provisaoReceberMesInscricao;
    private Double provisaoReceberMesBolsaCusteada;
    private Double provisaoReceberMesContratoReceita;
    private Double provisaoReceberMesInclusaoReposicao;
    private Double provisaoReceberMesOutros;
    
    /**
     * Atributos Abaixo Utilizado no Painel Gestor Financeiro - Mapa Despesas
     */
    private Double totalProvisaoContaPagarPeriodo;
    private Double totalPagoNoPeriodo;
    private Double totalPagoDoPeriodo;
    private Double saldoPagarDoPeriodo;
    private Double totalVencidoDoPeriodo;
    private Double mediaInadimplenciaDespesaDoPeriodo;
    private Double mediaInadimplenciaDespesaNoPeriodo;
    private Double totalPagarAnteriorPeriodo;
    private List<PainelGestorContaReceberMesAnoVO> painelGestorContaReceberMesAnoVOs;
    private List<PainelGestorContaReceberMesAnoVO> painelGestorContaReceberFluxoCaixaMesAnoVOs;
    private List<PainelGestorContaReceberMesAnoVO> painelGestorContaReceberMesAnoTurmaVOs;
    private List<PainelGestorContaPagarMesAnoVO> painelGestorContaPagarMesAnoVOs;
    private PainelGestorVO painelGestorPorNivelEducacionalVO;
    private PainelGestorVO painelGestorPorTurmaVO;
    
    private TipoNivelEducacional tipoNivelEducacional;
//    private Date dataInicioPorNivelEducacional;
//    private Date dataFinalPorNivelEducacional;
//    private Date dataInicioPorCurso;
//    private Date dataFinalPorCurso;
//    private Date dataInicioPorTurma;
//    private Date dataFinalPorTurma;
//    private Date dataInicioPorCategoriaDespesa;
//    private Date dataFinalPorCategoriaDespesa;
    private Integer codigoCurso;
    /**
     * Atributos Abaixo Utilizado no Painel Gestor Financeiro Acadêmico
     */
    private List<PainelGestorFinanceiroAcademicoMesAnoVO> painelGestorFinanceiroAcademicoMesAnoVOs;
    private Double totalAlunoAtivo;
    private Integer totalAlunoAptoFormar;
    private Integer totalNovoAluno;
    private Integer totalAlunoRenovado;
    private Integer totalAlunoRetornoEvasao;
    private Integer totalAlunoCancelado;
    private Integer totalAlunoTrancado;
    private Integer totalAlunoTransferenciaSaida;
    private Integer totalAlunoTransferenciaInterna;
    private Integer totalAlunoPreMatricula;
    private Integer totalAlunoAbandonado;
    private Integer totalAlunoFormado;
    
    
    private Double totalReceita;
    private Double totalDespesa;
    private Double totalMediaReceita;
    private Double totalMediaDespesa;
    
    private BigDecimal totalDescontoDetalhamento;
    private Integer totalNumeroAlunoComDesconto;
    
    /**Atributos Utilizados no Painel de Monitoramento de Descontos Inicial
     *
     * @return
     */
    private List<PainelGestorMonitoramentoDescontoMesAnoVO> painelGestorMonitoramentoDescontoMesAnoVOs;
    private Double totalFaturadoPeriodoMonitoramentoDesconto;
    private Double totalRecebidoPeriodoMonitoramentoDesconto;
    private Double totalDescontoPeriodo;
    private Double totalDescontoProgressivo;
    private Double totalDescontoAluno;
    private Double totalDescontoInstituicao;
    private Double totalDescontoConvenio;
    private Double totalDescontoRateio;
    private Double totalDescontoRecebimento;
    private Integer totalAlunoReceberamDesconto;
    private PainelGestorMonitoramentoDescontoMesAnoVO painelGestorMonitoramentoDescontoMesAnoVO;
    /**
     * Atributos utilizados no detalhamnto do desconto da instituição
     * @return
     */
    private Double totalDescontoDetalhamentoInstituicao;
    private Double percentualDescontoRelacaoTotalDesconto;
    private Double percentualDescontoRelacaoFaturado;
    private Double totalFaturadoComDescontoInstituicao;
    private Integer numeroAlunoComDescontoInstituicao;
    private List<PainelGestorMonitoramentoDescontoInstituicaoVO> painelGestorMonitoramentoDescontoInstituicaoVOs;
    private List<PainelGestorDetalhamentoDescontoVO> painelGestorDetalhamentoDescontoVOs;
    
    
    /**
     * Atributos utilizado no detalhamento de desconto de convênio
     *
     * @return
     */
    private Double totalFaturadoConvenio;
    private Double totalFaturadoPeriodo;
    private Double totalDescontoRecebidoConvenio;
    private Integer totalMatriculaUtilizamConvenio;
    private Double percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio;
    private Double percentualInadimplenciaConvenio;
    private Double totalRecebidoConvenio;
    private Double totalReceberConvenio;
    private Double totalAtrazadoConvenio;
    private List<PainelGestorMonitoramentoDescontoConvenioVO> painelGestorMonitoramentoDescontoConvenioVOs;
    /**
     * Atributos utilizado no detalhamento de desconto progressivo
     * @return
     */
    private Integer totalPessoasUsouDescontoProgressivo;
    private Integer totalPessoasUsouPrimeiroDescontoProgressivo;
    private Integer totalPessoasUsouSegundoDescontoProgressivo;
    private Integer totalPessoasUsouTerceiroDescontoProgressivo;
    private Integer totalPessoasUsouQuartoDescontoProgressivo;
    private Double valorTotalDescontoProgressivo;
    private Double totalPrimeiroDescontoProgressivo;
    private Double totalSegundoDescontoProgressivo;
    private Double totalTerceiroDescontoProgressivo;
    private Double totalQuartoDescontoProgressivo;
    private Double percentualPrimeiroDescontoProgressivo;
    private Double percentualSegundoDescontoProgressivo;
    private Double percentualTerceiroDescontoProgressivo;
    private Double percentualQuartoDescontoProgressivo;
    private Double percentualTituloPrimeiroDescontoProgressivo;
    private Double percentualTituloSegundoDescontoProgressivo;
    private Double percentualTituloTerceiroDescontoProgressivo;
    private Double percentualTituloQuartoDescontoProgressivo;
    private List<PainelGestorMonitoramentoDescontoProgressivoVO> painelGestorMonitoramentoDescontoProgressivoVOs;
    private String listaCoresGraficoCategoriaDespesa = "";
    private String nivelAtualCategoriaDespesa = "";
    private List<String> listaNivelGraficoCategoriaDespesa;
    private Boolean trazerContasPagas;
    private Boolean trazerContasAPagar;
    private Double valorTotalPagarPorCategoriaDespesa;
    private Double valorTotalPagoPorCategoriaDespesa;
    private Double valorTotalPorCategoriaDespesa;
    private Integer quantidadeAlunosAtualmente;
    private String quantidadeAlunosNaoConsideradosAtualmente;
    /**
     * Utilizado no monitoramento de consultores
     */
    private List<PainelGestorMonitoramentoConsultorVO> painelGestorMonitoramentoConsultorVOs;
    private List<PainelGestorMonitoramentoConsultorDetalhamentoVO> painelGestorMonitoramentoConsultorDetalhamentoVOs;
    private Boolean apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs;
    private String graficoMonitoramentoFinanceiroConsultor;
    private String graficoMonitoramentoSituacaoAcademicaConsultor;
    private String graficoMonitoramentoDocumentacaoConsultor;
    private String graficoMonitoramentoMatriculaAtivaConsultor;
    private Integer qtdMatRecebidaMonitoramentoConsultor;
    private Integer qtdMatAReceberMonitoramentoConsultor;
    private Integer qtdMatVencidaMonitoramentoConsultor;
    private Integer qtdMatAVencerMonitoramentoConsultor;
    private Integer qtdMatPreMonitoramentoConsultor;
    private Integer qtdMatAtivoMonitoramentoConsultor;
    private Integer qtdMatCanceladoMonitoramentoConsultor;
    private Integer qtdMatExcluidoMonitoramentoConsultor;    
    private Integer qtdMatExtensaoMonitoramentoConsultor;
    private Integer qtdMatPendenciaDocMonitoramentoConsultor;
    
    /**
     * Painel Gestor Financeiro Por Competência
     */
    private BigDecimal totalReceitaDoMes;
    private BigDecimal totalAcrescimoDoMes;
    private BigDecimal totalJuroMultaDoMes;
    private BigDecimal totalDescontoDoMes;
    private BigDecimal totalReceitaComDescontoAcrescimoDoMes;
    private BigDecimal totalSaldoAReceberDoMes;
    private BigDecimal totalRecebidoDoMes;
    
    private BigDecimal receitaDoMesMatricula;
    private BigDecimal receitaDoMesMaterialDidatico;
    private BigDecimal receitaDoMesMensalidade;
    private BigDecimal receitaDoMesRequerimento;
    private BigDecimal receitaDoMesBiblioteca;
    private BigDecimal receitaDoMesDevolucaoCheque;
    private BigDecimal receitaDoMesNegociacao;
    private BigDecimal receitaDoMesBolsaCusteada;
    private BigDecimal receitaDoMesInscricao;
    private BigDecimal receitaDoMesContratoReceita;
    private BigDecimal receitaDoMesInclusaoReposicao;
    private BigDecimal receitaDoMesOutros;
    
    private BigDecimal descontoDoMesMatricula;
    private BigDecimal descontoDoMesMaterialDidatico;
    private BigDecimal descontoDoMesMensalidade;
    private BigDecimal descontoDoMesRequerimento;
    private BigDecimal descontoDoMesBiblioteca;
    private BigDecimal descontoDoMesDevolucaoCheque;
    private BigDecimal descontoDoMesNegociacao;
    private BigDecimal descontoDoMesBolsaCusteada;
    private BigDecimal descontoDoMesInscricao;
    private BigDecimal descontoDoMesContratoReceita;
    private BigDecimal descontoDoMesInclusaoReposicao;
    private BigDecimal descontoDoMesOutros;
    
    private BigDecimal acrescimoDoMesMatricula;
    private BigDecimal acrescimoDoMesMaterialDidatico;
    private BigDecimal acrescimoDoMesMensalidade;
    private BigDecimal acrescimoDoMesRequerimento;
    private BigDecimal acrescimoDoMesBiblioteca;
    private BigDecimal acrescimoDoMesDevolucaoCheque;
    private BigDecimal acrescimoDoMesNegociacao;
    private BigDecimal acrescimoDoMesBolsaCusteada;
    private BigDecimal acrescimoDoMesInscricao;
    private BigDecimal acrescimoDoMesContratoReceita;
    private BigDecimal acrescimoDoMesInclusaoReposicao;
    private BigDecimal acrescimoDoMesOutros;
    
    private BigDecimal receitaComDescontoAcrescimoDoMesMatricula;
    private BigDecimal receitaComDescontoAcrescimoDoMesMaterialDidatico;
    private BigDecimal receitaComDescontoAcrescimoDoMesMensalidade;
    private BigDecimal receitaComDescontoAcrescimoDoMesRequerimento;
    private BigDecimal receitaComDescontoAcrescimoDoMesBiblioteca;
    private BigDecimal receitaComDescontoAcrescimoDoMesDevolucaoCheque;
    private BigDecimal receitaComDescontoAcrescimoDoMesNegociacao;
    private BigDecimal receitaComDescontoAcrescimoDoMesBolsaCusteada;
    private BigDecimal receitaComDescontoAcrescimoDoMesInscricao;
    private BigDecimal receitaComDescontoAcrescimoDoMesContratoReceita;
    private BigDecimal receitaComDescontoAcrescimoDoMesInclusaoReposicao;
    private BigDecimal receitaComDescontoAcrescimoDoMesOutros;
    
    private BigDecimal valorRecebidoDoMesMatricula;
    private BigDecimal valorRecebidoDoMesMaterialDidatico;
    private BigDecimal valorRecebidoDoMesMensalidade;
    private BigDecimal valorRecebidoDoMesRequerimento;
    private BigDecimal valorRecebidoDoMesBiblioteca;
    private BigDecimal valorRecebidoDoMesDevolucaoCheque;
    private BigDecimal valorRecebidoDoMesNegociacao;
    private BigDecimal valorRecebidoDoMesBolsaCusteada;
    private BigDecimal valorRecebidoDoMesInscricao;
    private BigDecimal valorRecebidoDoMesContratoReceita;
    private BigDecimal valorRecebidoDoMesInclusaoReposicao;
    private BigDecimal valorRecebidoDoMesOutros;
    
    private BigDecimal saldoReceberDoMesMatricula;
    private BigDecimal saldoReceberDoMesMaterialDidatico;
    private BigDecimal saldoReceberDoMesMensalidade;
    private BigDecimal saldoReceberDoMesRequerimento;
    private BigDecimal saldoReceberDoMesBiblioteca;
    private BigDecimal saldoReceberDoMesDevolucaoCheque;
    private BigDecimal saldoReceberDoMesNegociacao;
    private BigDecimal saldoReceberDoMesBolsaCusteada;
    private BigDecimal saldoReceberDoMesInscricao;
    private BigDecimal saldoReceberDoMesContratoReceita;
    private BigDecimal saldoReceberDoMesInclusaoReposicao;
    private BigDecimal saldoReceberDoMesOutros;
    
    private BigDecimal totalVencidoDoMes;
    private BigDecimal totalVencidoDoMesMatricula;
    private BigDecimal totalVencidoDoMesMaterialDidatico;
    private BigDecimal totalVencidoDoMesMensalidade;
    private BigDecimal totalVencidoDoMesRequerimento;
    private BigDecimal totalVencidoDoMesBiblioteca;
    private BigDecimal totalVencidoDoMesDevolucaoCheque;
    private BigDecimal totalVencidoDoMesNegociacao;
    private BigDecimal totalVencidoDoMesBolsaCusteada;
    private BigDecimal totalVencidoDoMesInscricao;
    private BigDecimal totalVencidoDoMesContratoReceita;
    private BigDecimal totalVencidoDoMesInclusaoReposicao;
    private BigDecimal totalVencidoDoMesOutros;
    
    private BigDecimal totalDescontoConvenioDoMes;
    private BigDecimal totalDescontoInstituicaoDoMes;
    private BigDecimal totalDescontoProgressivoDoMes;
    private BigDecimal totalDescontoAlunoDoMes;
    private BigDecimal totalDescontoRateioDoMes;
    private BigDecimal totalDescontoRecebimentoDoMes;
    
    private BigDecimal totalDescontoConvenioNoMes;
    private BigDecimal totalDescontoInstituicaoNoMes;
    private BigDecimal totalDescontoProgressivoNoMes;
    private BigDecimal totalDescontoAlunoNoMes;
    private BigDecimal totalDescontoRateioNoMes;
    private BigDecimal totalDescontoRecebimentoNoMes;
    
    /**
     * Painel Gestor Financeiro Por Fluxo de Caixa
     */
    private BigDecimal totalReceitaNoMes;
    private BigDecimal totalAcrescimoNoMes;
    private BigDecimal totalDescontoNoMes;
    
    private BigDecimal receitaNoMesMatricula;
    private BigDecimal receitaNoMesMaterialDidatico;
    private BigDecimal receitaNoMesMensalidade;
    private BigDecimal receitaNoMesRequerimento;
    private BigDecimal receitaNoMesBiblioteca;
    private BigDecimal receitaNoMesDevolucaoCheque;
    private BigDecimal receitaNoMesNegociacao;
    private BigDecimal receitaNoMesBolsaCusteada;
    private BigDecimal receitaNoMesInscricao;
    private BigDecimal receitaNoMesContratoReceita;
    private BigDecimal receitaNoMesInclusaoReposicao;
    private BigDecimal receitaNoMesOutros;
    
    private BigDecimal descontoNoMesMatricula;
    private BigDecimal descontoNoMesMaterialDidatico;
    private BigDecimal descontoNoMesMensalidade;
    private BigDecimal descontoNoMesRequerimento;
    private BigDecimal descontoNoMesBiblioteca;
    private BigDecimal descontoNoMesDevolucaoCheque;
    private BigDecimal descontoNoMesNegociacao;
    private BigDecimal descontoNoMesBolsaCusteada;
    private BigDecimal descontoNoMesInscricao;
    private BigDecimal descontoNoMesContratoReceita;
    private BigDecimal descontoNoMesInclusaoReposicao;
    private BigDecimal descontoNoMesOutros;
    
    private BigDecimal acrescimoNoMesMatricula;
    private BigDecimal acrescimoNoMesMaterialDidatico;
    private BigDecimal acrescimoNoMesMensalidade;
    private BigDecimal acrescimoNoMesRequerimento;
    private BigDecimal acrescimoNoMesBiblioteca;
    private BigDecimal acrescimoNoMesDevolucaoCheque;
    private BigDecimal acrescimoNoMesNegociacao;
    private BigDecimal acrescimoNoMesBolsaCusteada;
    private BigDecimal acrescimoNoMesInscricao;
    private BigDecimal acrescimoNoMesContratoReceita;
    private BigDecimal acrescimoNoMesInclusaoReposicao;
    private BigDecimal acrescimoNoMesOutros;
	
	/**
	 * Atributos Abaixo Utilizado no Gráfico de Categoria Consumo
	 */
	private Double valorTotalPagarConsumo;
	private String listaCoresGraficoConsumo = "";
	private String listaValoresConsumoInicial;
	private String listaValoresConsumo;
	private Integer codigoDepartamento;
	private String nomeDepartamento;
	private String legendaCategoriaPainelAcademicoNivelEducacional; 

    
    /**
     * Atributos utilizado no monitoramento de consultor do CRM
     * @return
     */
    public static final long serialVersionUID = 1L;
    

    public Double getPercentualTituloPrimeiroDescontoProgressivo() {
        if (percentualTituloPrimeiroDescontoProgressivo == null) {
            percentualTituloPrimeiroDescontoProgressivo = 0.0;
        }
        return percentualTituloPrimeiroDescontoProgressivo;
    }

    public void setPercentualTituloPrimeiroDescontoProgressivo(Double percentualTituloPrimeiroDescontoProgressivo) {
        this.percentualTituloPrimeiroDescontoProgressivo = percentualTituloPrimeiroDescontoProgressivo;
    }

    public Double getPercentualTituloQuartoDescontoProgressivo() {
        if (percentualTituloQuartoDescontoProgressivo == null) {
            percentualTituloQuartoDescontoProgressivo = 0.0;
        }
        return percentualTituloQuartoDescontoProgressivo;
    }

    public void setPercentualTituloQuartoDescontoProgressivo(Double percentualTituloQuartoDescontoProgressivo) {
        this.percentualTituloQuartoDescontoProgressivo = percentualTituloQuartoDescontoProgressivo;
    }

    public Double getPercentualTituloSegundoDescontoProgressivo() {
        if (percentualTituloSegundoDescontoProgressivo == null) {
            percentualTituloSegundoDescontoProgressivo = 0.0;
        }
        return percentualTituloSegundoDescontoProgressivo;
    }

    public void setPercentualTituloSegundoDescontoProgressivo(Double percentualTituloSegundoDescontoProgressivo) {
        this.percentualTituloSegundoDescontoProgressivo = percentualTituloSegundoDescontoProgressivo;
    }

    public Double getPercentualTituloTerceiroDescontoProgressivo() {
        if (percentualTituloTerceiroDescontoProgressivo == null) {
            percentualTituloTerceiroDescontoProgressivo = 0.0;
        }
        return percentualTituloTerceiroDescontoProgressivo;
    }

    public void setPercentualTituloTerceiroDescontoProgressivo(Double percentualTituloTerceiroDescontoProgressivo) {
        this.percentualTituloTerceiroDescontoProgressivo = percentualTituloTerceiroDescontoProgressivo;
    }

    public Double getPercentualPrimeiroDescontoProgressivo() {
        if (percentualPrimeiroDescontoProgressivo == null) {
            percentualPrimeiroDescontoProgressivo = 0.0;
        }
        return percentualPrimeiroDescontoProgressivo;
    }

    public void setPercentualPrimeiroDescontoProgressivo(Double percentualPrimeiroDescontoProgressivo) {
        this.percentualPrimeiroDescontoProgressivo = percentualPrimeiroDescontoProgressivo;
    }

    public Double getPercentualQuartoDescontoProgressivo() {
        if (percentualQuartoDescontoProgressivo == null) {
            percentualQuartoDescontoProgressivo = 0.0;
        }
        return percentualQuartoDescontoProgressivo;
    }

    public void setPercentualQuartoDescontoProgressivo(Double percentualQuartoDescontoProgressivo) {
        this.percentualQuartoDescontoProgressivo = percentualQuartoDescontoProgressivo;
    }

    public Double getPercentualSegundoDescontoProgressivo() {
        if (percentualSegundoDescontoProgressivo == null) {
            percentualSegundoDescontoProgressivo = 0.0;
        }
        return percentualSegundoDescontoProgressivo;
    }

    public void setPercentualSegundoDescontoProgressivo(Double percentualSegundoDescontoProgressivo) {
        this.percentualSegundoDescontoProgressivo = percentualSegundoDescontoProgressivo;
    }

    public Double getPercentualTerceiroDescontoProgressivo() {
        if (percentualTerceiroDescontoProgressivo == null) {
            percentualTerceiroDescontoProgressivo = 0.0;
        }
        return percentualTerceiroDescontoProgressivo;
    }

    public void setPercentualTerceiroDescontoProgressivo(Double percentualTerceiroDescontoProgressivo) {
        this.percentualTerceiroDescontoProgressivo = percentualTerceiroDescontoProgressivo;
    }

    public List<PainelGestorMonitoramentoDescontoProgressivoVO> getPainelGestorMonitoramentoDescontoProgressivoVOs() {
        if (painelGestorMonitoramentoDescontoProgressivoVOs == null) {
            painelGestorMonitoramentoDescontoProgressivoVOs = new ArrayList<PainelGestorMonitoramentoDescontoProgressivoVO>(0);
        }
        return painelGestorMonitoramentoDescontoProgressivoVOs;
    }

    public void setPainelGestorMonitoramentoDescontoProgressivoVOs(List<PainelGestorMonitoramentoDescontoProgressivoVO> painelGestorMonitoramentoDescontoProgressivoVOs) {
        this.painelGestorMonitoramentoDescontoProgressivoVOs = painelGestorMonitoramentoDescontoProgressivoVOs;
    }

    public Double getValorTotalDescontoProgressivo() {
        if (valorTotalDescontoProgressivo == null) {
            valorTotalDescontoProgressivo = 0.0;
        }
        return valorTotalDescontoProgressivo;
    }

    public void setValorTotalDescontoProgressivo(Double valorTotalDescontoProgressivo) {
        this.valorTotalDescontoProgressivo = valorTotalDescontoProgressivo;
    }

    public Integer getTotalPessoasUsouDescontoProgressivo() {
        if (totalPessoasUsouDescontoProgressivo == null) {
            totalPessoasUsouDescontoProgressivo = 0;
        }
        return totalPessoasUsouDescontoProgressivo;
    }

    public void setTotalPessoasUsouDescontoProgressivo(Integer totalPessoasUsouDescontoProgressivo) {
        this.totalPessoasUsouDescontoProgressivo = totalPessoasUsouDescontoProgressivo;
    }

    public Integer getTotalPessoasUsouPrimeiroDescontoProgressivo() {
        if (totalPessoasUsouPrimeiroDescontoProgressivo == null) {
            totalPessoasUsouPrimeiroDescontoProgressivo = 0;
        }
        return totalPessoasUsouPrimeiroDescontoProgressivo;
    }

    public void setTotalPessoasUsouPrimeiroDescontoProgressivo(Integer totalPessoasUsouPrimeiroDescontoProgressivo) {
        this.totalPessoasUsouPrimeiroDescontoProgressivo = totalPessoasUsouPrimeiroDescontoProgressivo;
    }

    public Integer getTotalPessoasUsouQuartoDescontoProgressivo() {
        if (totalPessoasUsouQuartoDescontoProgressivo == null) {
            totalPessoasUsouQuartoDescontoProgressivo = 0;
        }
        return totalPessoasUsouQuartoDescontoProgressivo;
    }

    public void setTotalPessoasUsouQuartoDescontoProgressivo(Integer totalPessoasUsouQuartoDescontoProgressivo) {
        this.totalPessoasUsouQuartoDescontoProgressivo = totalPessoasUsouQuartoDescontoProgressivo;
    }

    public Integer getTotalPessoasUsouSegundoDescontoProgressivo() {
        if (totalPessoasUsouSegundoDescontoProgressivo == null) {
            totalPessoasUsouSegundoDescontoProgressivo = 0;
        }
        return totalPessoasUsouSegundoDescontoProgressivo;
    }

    public void setTotalPessoasUsouSegundoDescontoProgressivo(Integer totalPessoasUsouSegundoDescontoProgressivo) {
        this.totalPessoasUsouSegundoDescontoProgressivo = totalPessoasUsouSegundoDescontoProgressivo;
    }

    public Integer getTotalPessoasUsouTerceiroDescontoProgressivo() {
        if (totalPessoasUsouTerceiroDescontoProgressivo == null) {
            totalPessoasUsouTerceiroDescontoProgressivo = 0;
        }
        return totalPessoasUsouTerceiroDescontoProgressivo;
    }

    public void setTotalPessoasUsouTerceiroDescontoProgressivo(Integer totalPessoasUsouTerceiroDescontoProgressivo) {
        this.totalPessoasUsouTerceiroDescontoProgressivo = totalPessoasUsouTerceiroDescontoProgressivo;
    }

    public Double getTotalPrimeiroDescontoProgressivo() {
        if (totalPrimeiroDescontoProgressivo == null) {
            totalPrimeiroDescontoProgressivo = 0.0;
        }
        return totalPrimeiroDescontoProgressivo;
    }

    public void setTotalPrimeiroDescontoProgressivo(Double totalPrimeiroDescontoProgressivo) {
        this.totalPrimeiroDescontoProgressivo = totalPrimeiroDescontoProgressivo;
    }

    public Double getTotalQuartoDescontoProgressivo() {
        if (totalQuartoDescontoProgressivo == null) {
            totalQuartoDescontoProgressivo = 0.0;
        }
        return totalQuartoDescontoProgressivo;
    }

    public void setTotalQuartoDescontoProgressivo(Double totalQuartoDescontoProgressivo) {
        this.totalQuartoDescontoProgressivo = totalQuartoDescontoProgressivo;
    }

    public Double getTotalSegundoDescontoProgressivo() {
        if (totalSegundoDescontoProgressivo == null) {
            totalSegundoDescontoProgressivo = 0.0;
        }
        return totalSegundoDescontoProgressivo;
    }

    public void setTotalSegundoDescontoProgressivo(Double totalSegundoDescontoProgressivo) {
        this.totalSegundoDescontoProgressivo = totalSegundoDescontoProgressivo;
    }

    public Double getTotalTerceiroDescontoProgressivo() {
        if (totalTerceiroDescontoProgressivo == null) {
            totalTerceiroDescontoProgressivo = 0.0;
        }
        return totalTerceiroDescontoProgressivo;
    }

    public void setTotalTerceiroDesconto(Double totalTerceiroDescontoProgressivo) {
        this.totalTerceiroDescontoProgressivo = totalTerceiroDescontoProgressivo;
    }

    public Double getTotalFaturadoPeriodo() {
        if (totalFaturadoPeriodo == null) {
            totalFaturadoPeriodo = 0.0;
        }
        return totalFaturadoPeriodo;
    }

    public void setTotalFaturadoPeriodo(Double totalFaturadoPeriodo) {
        this.totalFaturadoPeriodo = totalFaturadoPeriodo;
    }

    public List<PainelGestorMonitoramentoDescontoConvenioVO> getPainelGestorMonitoramentoDescontoConvenioVOs() {
        if (painelGestorMonitoramentoDescontoConvenioVOs == null) {
            painelGestorMonitoramentoDescontoConvenioVOs = new ArrayList<PainelGestorMonitoramentoDescontoConvenioVO>(0);
        }
        return painelGestorMonitoramentoDescontoConvenioVOs;
    }

    public void setPainelGestorMonitoramentoDescontoConvenioVOs(List<PainelGestorMonitoramentoDescontoConvenioVO> painelGestorMonitoramentoDescontoConvenioVOs) {
        this.painelGestorMonitoramentoDescontoConvenioVOs = painelGestorMonitoramentoDescontoConvenioVOs;
    }

    public Double getPercentualFaturadoConvenioRelacaoTotalFaturadoValorCheio() {
        if (percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio == null) {
            percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio = 0.0;
        }
        return percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio;
    }

    public void setPercentualFaturadoConvenioRelacaoTotalFaturadoValorCheio(Double percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio) {
        this.percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio = percentualFaturadoConvenioRelacaoTotalFaturadoValorCheio;
    }

    public Double getPercentualInadimplenciaConvenio() {
        if (percentualInadimplenciaConvenio == null) {
            percentualInadimplenciaConvenio = 0.0;
        }
        return percentualInadimplenciaConvenio;
    }

    public void setPercentualInadimplenciaConvenio(Double percentualInadimplenciaConvenio) {
        this.percentualInadimplenciaConvenio = percentualInadimplenciaConvenio;
    }

    public Double getTotalAtrazadoConvenio() {
        if (totalAtrazadoConvenio == null) {
            totalAtrazadoConvenio = 0.0;
        }
        return totalAtrazadoConvenio;
    }

    public void setTotalAtrazadoConvenio(Double totalAtrazadoConvenio) {
        this.totalAtrazadoConvenio = totalAtrazadoConvenio;
    }

    public Double getTotalFaturadoConvenio() {
        if (totalFaturadoConvenio == null) {
            totalFaturadoConvenio = 0.0;
        }
        return totalFaturadoConvenio;
    }

    public void setTotalFaturadoConvenio(Double totalFaturadoConvenio) {
        this.totalFaturadoConvenio = totalFaturadoConvenio;
    }

    public Integer getTotalMatriculaUtilizamConvenio() {
        if (totalMatriculaUtilizamConvenio == null) {
            totalMatriculaUtilizamConvenio = 0;
        }
        return totalMatriculaUtilizamConvenio;
    }

    public void setTotalMatriculaUtilizamConvenio(Integer totalMatriculaUtilizamConvenio) {
        this.totalMatriculaUtilizamConvenio = totalMatriculaUtilizamConvenio;
    }

    public Double getTotalReceberConvenio() {
        if (totalReceberConvenio == null) {
            totalReceberConvenio = 0.0;
        }
        return totalReceberConvenio;
    }

    public void setTotalReceberConvenio(Double totalReceberConvenio) {
        this.totalReceberConvenio = totalReceberConvenio;
    }

    public Double getTotalRecebidoConvenio() {
        if (totalRecebidoConvenio == null) {
            totalRecebidoConvenio = 0.0;
        }
        return totalRecebidoConvenio;
    }

    public void setTotalRecebidoConvenio(Double totalRecebidoConvenio) {
        this.totalRecebidoConvenio = totalRecebidoConvenio;
    }

    public Double getPercentualDescontoRelacaoFaturado() {
        if (percentualDescontoRelacaoFaturado == null) {
            percentualDescontoRelacaoFaturado = 0.0;
        }
        return percentualDescontoRelacaoFaturado;
    }

    public void setPercentualDescontoRelacaoFaturado(Double percentualDescontoRelacaoFaturado) {
        this.percentualDescontoRelacaoFaturado = percentualDescontoRelacaoFaturado;
    }

    public Double getTotalFaturadoComDescontoInstituicao() {
        if (totalFaturadoComDescontoInstituicao == null) {
            totalFaturadoComDescontoInstituicao = 0.0;
        }
        return totalFaturadoComDescontoInstituicao;
    }

    public void setTotalFaturadoComDescontoInstituicao(Double totalFaturadoComDescontoInstituicao) {
        this.totalFaturadoComDescontoInstituicao = totalFaturadoComDescontoInstituicao;
    }

    public List<PainelGestorMonitoramentoDescontoInstituicaoVO> getPainelGestorMonitoramentoDescontoInstituicaoVOs() {
        if (painelGestorMonitoramentoDescontoInstituicaoVOs == null) {
            painelGestorMonitoramentoDescontoInstituicaoVOs = new ArrayList<PainelGestorMonitoramentoDescontoInstituicaoVO>(0);
        }
        return painelGestorMonitoramentoDescontoInstituicaoVOs;
    }

    public void setPainelGestorMonitoramentoDescontoInstituicaoVOs(List<PainelGestorMonitoramentoDescontoInstituicaoVO> painelGestorMonitoramentoDescontoInstituicaoVOs) {
        this.painelGestorMonitoramentoDescontoInstituicaoVOs = painelGestorMonitoramentoDescontoInstituicaoVOs;
    }

    public Double getTotalDescontoDetalhamentoInstituicao() {
        if (totalDescontoDetalhamentoInstituicao == null) {
            totalDescontoDetalhamentoInstituicao = 0.0;
        }
        return totalDescontoDetalhamentoInstituicao;
    }

    public void setTotalDescontoDetalhamentoInstituicao(Double totalDescontoDetalhamentoInstituicao) {
        this.totalDescontoDetalhamentoInstituicao = totalDescontoDetalhamentoInstituicao;
    }

    public Integer getNumeroAlunoComDescontoInstituicao() {
        if (numeroAlunoComDescontoInstituicao == null) {
            numeroAlunoComDescontoInstituicao = 0;
        }
        return numeroAlunoComDescontoInstituicao;
    }

    public void setNumeroAlunoComDescontoInstituicao(Integer numeroAlunoComDescontoInstituicao) {
        this.numeroAlunoComDescontoInstituicao = numeroAlunoComDescontoInstituicao;
    }

    public Double getPercentualDescontoRelacaoTotalDesconto() {
        if (percentualDescontoRelacaoTotalDesconto == null) {
            percentualDescontoRelacaoTotalDesconto = 0.0;
        }
        return percentualDescontoRelacaoTotalDesconto;
    }

    public void setPercentualDescontoRelacaoTotalDesconto(Double percentualDescontoRelacaoTotalDesconto) {
        this.percentualDescontoRelacaoTotalDesconto = percentualDescontoRelacaoTotalDesconto;
    }

    public PainelGestorMonitoramentoDescontoMesAnoVO getPainelGestorMonitoramentoDescontoMesAnoVO() {
        if (painelGestorMonitoramentoDescontoMesAnoVO == null) {
            painelGestorMonitoramentoDescontoMesAnoVO = new PainelGestorMonitoramentoDescontoMesAnoVO();
        }
        return painelGestorMonitoramentoDescontoMesAnoVO;
    }

    public void setPainelGestorMonitoramentoDescontoMesAnoVO(PainelGestorMonitoramentoDescontoMesAnoVO painelGestorMonitoramentoDescontoMesAnoVO) {
        this.painelGestorMonitoramentoDescontoMesAnoVO = painelGestorMonitoramentoDescontoMesAnoVO;
    }

    public Double getPercentualDescontoEfetivado() {
        return Uteis.arrendondarForcando2CadasDecimais(getTotalDescontoPeriodo() * 100 / getTotalFaturadoPeriodoMonitoramentoDesconto());
    }

    public Double getTotalFaturadoPeriodoMonitoramentoDesconto() {
        if (totalFaturadoPeriodoMonitoramentoDesconto == null) {
            totalFaturadoPeriodoMonitoramentoDesconto = 0.0;
        }
        return totalFaturadoPeriodoMonitoramentoDesconto;
    }

    public void setTotalFaturadoPeriodoMonitoramentoDesconto(Double totalFaturadoPeriodoMonitoramentoDesconto) {
        this.totalFaturadoPeriodoMonitoramentoDesconto = totalFaturadoPeriodoMonitoramentoDesconto;
    }

    public Double getTotalRecebidoPeriodoMonitoramentoDesconto() {
        if (totalRecebidoPeriodoMonitoramentoDesconto == null) {
            totalRecebidoPeriodoMonitoramentoDesconto = 0.0;
        }
        return totalRecebidoPeriodoMonitoramentoDesconto;
    }

    public void setTotalRecebidoPeriodoMonitoramentoDesconto(Double totalRecebidoPeriodoMonitoramentoDesconto) {
        this.totalRecebidoPeriodoMonitoramentoDesconto = totalRecebidoPeriodoMonitoramentoDesconto;
    }

    public List<PainelGestorMonitoramentoDescontoMesAnoVO> getPainelGestorMonitoramentoDescontoMesAnoVOs() {
        if (painelGestorMonitoramentoDescontoMesAnoVOs == null) {
            painelGestorMonitoramentoDescontoMesAnoVOs = new ArrayList<PainelGestorMonitoramentoDescontoMesAnoVO>(0);
        }
        return painelGestorMonitoramentoDescontoMesAnoVOs;
    }

    public void setPainelGestorMonitoramentoDescontoMesAnoVOs(List<PainelGestorMonitoramentoDescontoMesAnoVO> painelGestorMonitoramentoDescontoMesAnoVOs) {
        this.painelGestorMonitoramentoDescontoMesAnoVOs = painelGestorMonitoramentoDescontoMesAnoVOs;
    }

    public Integer getTotalAlunoReceberamDesconto() {
        if (totalAlunoReceberamDesconto == null) {
            totalAlunoReceberamDesconto = 0;
        }
        return totalAlunoReceberamDesconto;
    }

    public void setTotalAlunoReceberamDesconto(Integer totalAlunoReceberamDesconto) {
        this.totalAlunoReceberamDesconto = totalAlunoReceberamDesconto;
    }

    public Double getTotalDescontoPeriodo() {
        if (totalDescontoPeriodo == null) {
            totalDescontoPeriodo = 0.0;
        }
        return totalDescontoPeriodo;
    }

    public void setTotalDescontoPeriodo(Double totalDesconto) {
        this.totalDescontoPeriodo = totalDesconto;
    }

    public Double getTotalDescontoAluno() {
        if (totalDescontoAluno == null) {
            totalDescontoAluno = 0.0;
        }
        return totalDescontoAluno;
    }

    public void setTotalDescontoAluno(Double totalDescontoAluno) {
        this.totalDescontoAluno = totalDescontoAluno;
    }

    public Double getTotalDescontoConvenio() {
        if (totalDescontoConvenio == null) {
            totalDescontoConvenio = 0.0;
        }
        return totalDescontoConvenio;
    }

    public void setTotalDescontoConvenio(Double totalDescontoConvenio) {
        this.totalDescontoConvenio = totalDescontoConvenio;
    }

    public Double getTotalDescontoInstituicao() {
        if (totalDescontoInstituicao == null) {
            totalDescontoInstituicao = 0.0;
        }
        return totalDescontoInstituicao;
    }

    public void setTotalDescontoInstituicao(Double totalDescontoInstituicao) {
        this.totalDescontoInstituicao = totalDescontoInstituicao;
    }

    public Double getTotalDescontoProgressivo() {
        if (totalDescontoProgressivo == null) {
            totalDescontoProgressivo = 0.0;
        }
        return totalDescontoProgressivo;
    }

    public void setTotalDescontoProgressivo(Double totalDescontoProgressivo) {
        this.totalDescontoProgressivo = totalDescontoProgressivo;
    }

    public Double getTotalDescontoRecebimento() {
        if (totalDescontoRecebimento == null) {
            totalDescontoRecebimento = 0.0;
        }
        return totalDescontoRecebimento;
    }

    public void setTotalDescontoRecebimento(Double totalDescontoRecebimento) {
        this.totalDescontoRecebimento = totalDescontoRecebimento;
    }

    public String getCoresLegendaGraficoAcademicoFinanceiro() {
        if (coresLegendaGraficoAcademicoFinanceiro == null) {
            coresLegendaGraficoAcademicoFinanceiro = "";
        }
        return coresLegendaGraficoAcademicoFinanceiro;
    }

    public void setCoresLegendaGraficoAcademicoFinanceiro(String coresLegendaGraficoAcademicoFinanceiro) {
        this.coresLegendaGraficoAcademicoFinanceiro = coresLegendaGraficoAcademicoFinanceiro;
    }

  

    

    /**
     *
     *
     */
    public List<PainelGestorFinanceiroAcademicoMesAnoVO> getPainelGestorFinanceiroAcademicoMesAnoVOs() {
        if (painelGestorFinanceiroAcademicoMesAnoVOs == null) {
            painelGestorFinanceiroAcademicoMesAnoVOs = new ArrayList<PainelGestorFinanceiroAcademicoMesAnoVO>(0);
        }
        return painelGestorFinanceiroAcademicoMesAnoVOs;
    }

    public void setPainelGestorFinanceiroAcademicoMesAnoVOs(List<PainelGestorFinanceiroAcademicoMesAnoVO> painelGestorFinanceiroAcademicoMesAnoVOs) {
        this.painelGestorFinanceiroAcademicoMesAnoVOs = painelGestorFinanceiroAcademicoMesAnoVOs;
    }

    public Double getTotalAlunoAtivo() {
        if (totalAlunoAtivo == null) {
            totalAlunoAtivo = 0.0;
        }
        return totalAlunoAtivo;
    }

    public void setTotalAlunoAtivo(Double totalAlunoAtivo) {
        this.totalAlunoAtivo = totalAlunoAtivo;
    }

    public Integer getTotalAlunoCancelado() {
        if (totalAlunoCancelado == null) {
            totalAlunoCancelado = 0;
        }
        return totalAlunoCancelado;
    }

    public void setTotalAlunoCancelado(Integer totalAlunoCancelado) {
        this.totalAlunoCancelado = totalAlunoCancelado;
    }

    public Integer getTotalAlunoRenovado() {
        if (totalAlunoRenovado == null) {
            totalAlunoRenovado = 0;
        }
        return totalAlunoRenovado;
    }

    public void setTotalAlunoRenovado(Integer totalAlunoRenovado) {
        this.totalAlunoRenovado = totalAlunoRenovado;
    }

    public Integer getTotalAlunoTrancado() {
        if (totalAlunoTrancado == null) {
            totalAlunoTrancado = 0;
        }
        return totalAlunoTrancado;
    }

    public void setTotalAlunoTrancado(Integer totalAlunoTrancado) {
        this.totalAlunoTrancado = totalAlunoTrancado;
    }

    public Integer getTotalAlunoTransferenciaSaida() {
        if (totalAlunoTransferenciaSaida == null) {
            totalAlunoTransferenciaSaida = 0;
        }
        return totalAlunoTransferenciaSaida;
    }

    public void setTotalAlunoTransferenciaSaida(Integer totalAlunoTransferenciaSaida) {
        this.totalAlunoTransferenciaSaida = totalAlunoTransferenciaSaida;
    }

    public Integer getTotalNovoAluno() {
        if (totalNovoAluno == null) {
            totalNovoAluno = 0;
        }
        return totalNovoAluno;
    }

    public void setTotalNovoAluno(Integer totalNovoAluno) {
        this.totalNovoAluno = totalNovoAluno;
    }

    public Double getTotalDespesa() {
        if (totalDespesa == null) {
            totalDespesa = 0.0;
        }
        return totalDespesa;
    }

    public void setTotalDespesa(Double totalDespesa) {
        this.totalDespesa = totalDespesa;
    }

    public Double getTotalMediaDespesa() {
        if (totalMediaDespesa == null) {
            totalMediaDespesa = 0.0;
        }
        return totalMediaDespesa;
    }

    public void setTotalMediaDespesa(Double totalMediaDespesa) {
        this.totalMediaDespesa = totalMediaDespesa;
    }

    public Double getTotalMediaReceita() {
        if (totalMediaReceita == null) {
            totalMediaReceita = 0.0;
        }
        return totalMediaReceita;
    }

    public void setTotalMediaReceita(Double totalMediaReceita) {
        this.totalMediaReceita = totalMediaReceita;
    }

    public Double getTotalReceita() {
        if (totalReceita == null) {
            totalReceita = 0.0;
        }
        return totalReceita;
    }

    public void setTotalReceita(Double totalReceita) {
        this.totalReceita = totalReceita;
    }

    public Double getTotalPagarAnteriorPeriodo() {
        if (totalPagarAnteriorPeriodo == null) {
            totalPagarAnteriorPeriodo = 0.0;
        }
        return totalPagarAnteriorPeriodo;
    }

    public void setTotalPagarAnteriorPeriodo(Double totalPagarAnteriorPeriodo) {
        this.totalPagarAnteriorPeriodo = totalPagarAnteriorPeriodo;
    }

    public Double getMediaInadimplenciaDespesaDoPeriodo() {
        if (mediaInadimplenciaDespesaDoPeriodo == null) {
            mediaInadimplenciaDespesaDoPeriodo = 0.0;
        }
        return mediaInadimplenciaDespesaDoPeriodo;
    }

    public void setMediaInadimplenciaDespesaDoPeriodo(Double mediaInadimplenciaDespesaDoPeriodo) {
        this.mediaInadimplenciaDespesaDoPeriodo = mediaInadimplenciaDespesaDoPeriodo;
    }

    public Double getMediaInadimplenciaDespesaNoPeriodo() {
        if (mediaInadimplenciaDespesaNoPeriodo == null) {
            mediaInadimplenciaDespesaNoPeriodo = 0.0;
        }
        return mediaInadimplenciaDespesaNoPeriodo;
    }

    public void setMediaInadimplenciaDespesaNoPeriodo(Double mediaInadimplenciaDespesaNoPeriodo) {
        this.mediaInadimplenciaDespesaNoPeriodo = mediaInadimplenciaDespesaNoPeriodo;
    }

    public Double getSaldoPagarDoPeriodo() {
        if (saldoPagarDoPeriodo == null) {
            saldoPagarDoPeriodo = 0.0;
        }
        return saldoPagarDoPeriodo;
    }

    public void setSaldoPagarDoPeriodo(Double saldoPagarDoPeriodo) {
        this.saldoPagarDoPeriodo = saldoPagarDoPeriodo;
    }

    public Double getTotalPagoDoPeriodo() {
        if (totalPagoDoPeriodo == null) {
            totalPagoDoPeriodo = 0.0;
        }
        return totalPagoDoPeriodo;
    }

    public void setTotalPagoDoPeriodo(Double totalPagoDoPeriodo) {
        this.totalPagoDoPeriodo = totalPagoDoPeriodo;
    }

    public Double getTotalPagoNoPeriodo() {
        if (totalPagoNoPeriodo == null) {
            totalPagoNoPeriodo = 0.0;
        }
        return totalPagoNoPeriodo;
    }

    public void setTotalPagoNoPeriodo(Double totalPagoNoPeriodo) {
        this.totalPagoNoPeriodo = totalPagoNoPeriodo;
    }

    public Double getTotalProvisaoContaPagarPeriodo() {
        if (totalProvisaoContaPagarPeriodo == null) {
            totalProvisaoContaPagarPeriodo = 0.0;
        }
        return totalProvisaoContaPagarPeriodo;
    }

    public void setTotalProvisaoContaPagarPeriodo(Double totalProvisaoContaPagarPeriodo) {
        this.totalProvisaoContaPagarPeriodo = totalProvisaoContaPagarPeriodo;
    }

    public Double getTotalVencidoDoPeriodo() {
        if (totalVencidoDoPeriodo == null) {
            totalVencidoDoPeriodo = 0.0;
        }
        return totalVencidoDoPeriodo;
    }

    public void setTotalVencidoDoPeriodo(Double totalVencidoDoPeriodo) {
        this.totalVencidoDoPeriodo = totalVencidoDoPeriodo;
    }

    public List<PainelGestorContaPagarMesAnoVO> getPainelGestorContaPagarMesAnoVOs() {
        if (painelGestorContaPagarMesAnoVOs == null) {
            painelGestorContaPagarMesAnoVOs = new ArrayList<PainelGestorContaPagarMesAnoVO>(0);
        }
        return painelGestorContaPagarMesAnoVOs;
    }

    public void setPainelGestorContaPagarMesAnoVOs(List<PainelGestorContaPagarMesAnoVO> painelGestorContaPagarMesAnoVOs) {
        this.painelGestorContaPagarMesAnoVOs = painelGestorContaPagarMesAnoVOs;
    }

//    public Date getDataFinalPorNivelEducacional() {
//        return dataFinalPorNivelEducacional;
//    }
//
//    public void setDataFinalPorNivelEducacional(Date dataFinalPorNivelEducacional) {
//        this.dataFinalPorNivelEducacional = dataFinalPorNivelEducacional;
//    }
//
//    public Date getDataInicioPorNivelEducacional() {
//        return dataInicioPorNivelEducacional;
//    }
//
//    public void setDataInicioPorNivelEducacional(Date dataInicioPorNivelEducacional) {
//        this.dataInicioPorNivelEducacional = dataInicioPorNivelEducacional;
//    }

    public TipoNivelEducacional getTipoNivelEducacional() {
        if (tipoNivelEducacional == null) {
            tipoNivelEducacional = TipoNivelEducacional.SUPERIOR;
        }
        return tipoNivelEducacional;
    }

    public void setTipoNivelEducacional(TipoNivelEducacional tipoNivelEducacional) {
        this.tipoNivelEducacional = tipoNivelEducacional;
    }

    public PainelGestorVO getPainelGestorPorNivelEducacionalVO() {
        if (painelGestorPorNivelEducacionalVO == null) {
            painelGestorPorNivelEducacionalVO = new PainelGestorVO();
        }
        return painelGestorPorNivelEducacionalVO;
    }

    public void setPainelGestorPorNivelEducacionalVO(PainelGestorVO painelGestorPorNivelEducacionalVO) {
        this.painelGestorPorNivelEducacionalVO = painelGestorPorNivelEducacionalVO;
    }

    public List<PainelGestorContaReceberMesAnoVO> getPainelGestorContaReceberMesAnoVOs() {
        if (painelGestorContaReceberMesAnoVOs == null) {
            painelGestorContaReceberMesAnoVOs = new ArrayList<PainelGestorContaReceberMesAnoVO>(0);
        }
        return painelGestorContaReceberMesAnoVOs;
    }

    public void setPainelGestorContaReceberMesAnoVOs(List<PainelGestorContaReceberMesAnoVO> painelGestorContaReceberMesAnoVOs) {
        this.painelGestorContaReceberMesAnoVOs = painelGestorContaReceberMesAnoVOs;
    }

    public List<PainelGestorContaReceberMesAnoVO> getPainelGestorContaReceberMesAnoTurmaVOs() {
        if (painelGestorContaReceberMesAnoTurmaVOs == null) {
            painelGestorContaReceberMesAnoTurmaVOs = new ArrayList<PainelGestorContaReceberMesAnoVO>(0);
        }
        return painelGestorContaReceberMesAnoTurmaVOs;
    }

    public void setPainelGestorContaReceberMesAnoTurmaVOs(List<PainelGestorContaReceberMesAnoVO> painelGestorContaReceberMesAnoTurmaVOs) {
        this.painelGestorContaReceberMesAnoTurmaVOs = painelGestorContaReceberMesAnoTurmaVOs;
    }

    public Double getMediaInadimplenciaDoPeriodo() {
        if (mediaInadimplenciaDoPeriodo == null) {
            mediaInadimplenciaDoPeriodo = 0.0;
        }
        return mediaInadimplenciaDoPeriodo;
    }

    public void setMediaInadimplenciaDoPeriodo(Double mediaInadimplenciaDoPeriodo) {
        this.mediaInadimplenciaDoPeriodo = mediaInadimplenciaDoPeriodo;
    }

    public Double getMediaInadimplenciaNoPeriodo() {
        if (mediaInadimplenciaNoPeriodo == null) {
            mediaInadimplenciaNoPeriodo = 0.0;
        }
        return mediaInadimplenciaNoPeriodo;
    }

    public void setMediaInadimplenciaNoPeriodo(Double mediaInadimplenciaNoPeriodo) {
        this.mediaInadimplenciaNoPeriodo = mediaInadimplenciaNoPeriodo;
    }

    public Double getSaldoReceberDoPeriodoPrimeiroDesconto() {
        if (saldoReceberDoPeriodoPrimeiroDesconto == null) {
            saldoReceberDoPeriodoPrimeiroDesconto = 0.0;
        }
        return saldoReceberDoPeriodoPrimeiroDesconto;
    }

    public void setSaldoReceberDoPeriodoPrimeiroDesconto(Double saldoReceberDoPeriodoPrimeiroDesconto) {
        this.saldoReceberDoPeriodoPrimeiroDesconto = saldoReceberDoPeriodoPrimeiroDesconto;
    }

    public Double getSaldoReceberPeriodo() {
        if (saldoReceberPeriodo == null) {
            saldoReceberPeriodo = 0.0;
        }
        return saldoReceberPeriodo;
    }

    public void setSaldoReceberPeriodo(Double saldoReceberPeriodo) {
        this.saldoReceberPeriodo = saldoReceberPeriodo;
    }

    public Double getTotalProvisaoPeriodo() {
        if (totalProvisaoPeriodo == null) {
            totalProvisaoPeriodo = 0.0;
        }
        return totalProvisaoPeriodo;
    }

    public void setTotalProvisaoPeriodo(Double totalProvisaoPeriodo) {
        this.totalProvisaoPeriodo = totalProvisaoPeriodo;
    }

    public Double getTotalProvisaoPeriodoPrimeiroDesconto() {
        if (totalProvisaoPeriodoPrimeiroDesconto == null) {
            totalProvisaoPeriodoPrimeiroDesconto = 0.0;
        }
        return totalProvisaoPeriodoPrimeiroDesconto;
    }

    public void setTotalProvisaoPeriodoPrimeiroDesconto(Double totalProvisaoPeriodoPrimeiroDesconto) {
        this.totalProvisaoPeriodoPrimeiroDesconto = totalProvisaoPeriodoPrimeiroDesconto;
    }

    public Double getTotalRecebidoDoPeriodo() {
        if (totalRecebidoDoPeriodo == null) {
            totalRecebidoDoPeriodo = 0.0;
        }
        return totalRecebidoDoPeriodo;
    }

    public void setTotalRecebidoDoPeriodo(Double totalRecebidoDoPeriodo) {
        this.totalRecebidoDoPeriodo = totalRecebidoDoPeriodo;
    }

    public Double getTotalRecebidoNoPeriodo() {
        if (totalRecebidoNoPeriodo == null) {
            totalRecebidoNoPeriodo = 0.0;
        }
        return totalRecebidoNoPeriodo;
    }

    public void setTotalRecebidoNoPeriodo(Double totalRecebidoNoPeriodo) {
        this.totalRecebidoNoPeriodo = totalRecebidoNoPeriodo;
    }

    public Double getTotalReceberAnteriorPeriodo() {
        if (totalReceberAnteriorPeriodo == null) {
            totalReceberAnteriorPeriodo = 0.0;
        }
        return totalReceberAnteriorPeriodo;
    }

    public void setTotalReceberAnteriorPeriodo(Double totalRecebeAnteriorPeriodo) {
        this.totalReceberAnteriorPeriodo = totalRecebeAnteriorPeriodo;
    }

    public Double getTotalReceberPeriodoPrimeiroDesconto() {
        if (totalProvisaoPeriodoPrimeiroDesconto == null) {
            totalProvisaoPeriodoPrimeiroDesconto = 0.0;
        }
        return totalProvisaoPeriodoPrimeiroDesconto;
    }

    public void setTotalReceberPeriodoPrimeiroDesconto(Double totalReceberPeriodoPrimeiroDesconto) {
        this.totalProvisaoPeriodoPrimeiroDesconto = totalReceberPeriodoPrimeiroDesconto;
    }

   

    
    public String getListaCoresGraficoCategoriaDespesa() {
        if(listaCoresGraficoCategoriaDespesa == null){
            listaCoresGraficoCategoriaDespesa = "";
        }
        return listaCoresGraficoCategoriaDespesa;
    }

    
    public void setListaCoresGraficoCategoriaDespesa(String listaCoresGraficoCategoriaDespesa) {
        this.listaCoresGraficoCategoriaDespesa = listaCoresGraficoCategoriaDespesa;
    }

    
    public String getNivelAtualCategoriaDespesa() {
        return nivelAtualCategoriaDespesa;
    }

    
    public void setNivelAtualCategoriaDespesa(String nivelAtualCategoriaDespesa) {
        this.nivelAtualCategoriaDespesa = nivelAtualCategoriaDespesa;
    }

    
    public Boolean getTrazerContasPagas() {
        if(trazerContasPagas == null){
            trazerContasPagas = true;
        }
        return trazerContasPagas;
    }

    
    public void setTrazerContasPagas(Boolean trazerContasPagas) {
        this.trazerContasPagas = trazerContasPagas;
    }

    
    public Boolean getTrazerContasAPagar() {
        if(trazerContasAPagar == null){
            trazerContasAPagar = true;
        }
        return trazerContasAPagar;
    }

    
    public void setTrazerContasAPagar(Boolean trazerContasAPagar) {
        this.trazerContasAPagar = trazerContasAPagar;
    }
    
    

  

    
    public Double getValorTotalPagarPorCategoriaDespesa() {
        if(valorTotalPagarPorCategoriaDespesa == null){
            valorTotalPagarPorCategoriaDespesa = 0.0;
        }
        return valorTotalPagarPorCategoriaDespesa;
    }

    
    public void setValorTotalPagarPorCategoriaDespesa(Double valorTotalPagarPorCategoriaDespesa) {
        this.valorTotalPagarPorCategoriaDespesa = valorTotalPagarPorCategoriaDespesa;
    }

    
    public Double getValorTotalPagoPorCategoriaDespesa() {
        if(valorTotalPagoPorCategoriaDespesa == null){
            valorTotalPagoPorCategoriaDespesa = 0.0;
        }
        return valorTotalPagoPorCategoriaDespesa;
    }

    
    public void setValorTotalPagoPorCategoriaDespesa(Double valorTotalPagoPorCategoriaDespesa) {
        this.valorTotalPagoPorCategoriaDespesa = valorTotalPagoPorCategoriaDespesa;
    }

    
    public Double getValorTotalPorCategoriaDespesa() {
        if(valorTotalPorCategoriaDespesa == null){
            valorTotalPorCategoriaDespesa = 0.0;
        }
        return valorTotalPorCategoriaDespesa;
    }

    
    public void setValorTotalPorCategoriaDespesa(Double valorTotalPorCategoriaDespesa) {
        this.valorTotalPorCategoriaDespesa = valorTotalPorCategoriaDespesa;
    }
    
    private String listaValoresReceitas;
    private String listaValoresDespesas;
    private String listaValoresReceitaDespesasSaldo;
    public String getListaValoresReceitas(){
        if(listaValoresReceitas == null){
            listaValoresReceitas = "";
        }
            
        return listaValoresReceitas;
    }

    
    public void setListaValoresReceitas(String listaValoresReceitas) {
        this.listaValoresReceitas = listaValoresReceitas;
    }

    
    public String getListaValoresDespesas() {
        if(listaValoresDespesas == null){
            listaValoresDespesas = "";
        }         
        return listaValoresDespesas;
    }

    
    public void setListaValoresDespesas(String listaValoresDespesas) {
        this.listaValoresDespesas = listaValoresDespesas;
    }
    
    
    
    
    public String getListaValoresReceitaDespesasSaldo() {
        if(listaValoresReceitaDespesasSaldo == null){
            listaValoresReceitaDespesasSaldo = "";
        }
        return listaValoresReceitaDespesasSaldo;
    }

    
    public void setListaValoresReceitaDespesasSaldo(String listaValoresReceitaDespesasSaldo) {
        this.listaValoresReceitaDespesasSaldo = listaValoresReceitaDespesasSaldo;
    }

    private String listaValoresAcademicoNovo;
    private String listaValoresAcademicoAptoFormar;
    private String listaValoresAcademicoAtivo;
    private String listaValoresAcademicoEvasao;
    private String listaValoresAcademicoPreMatricula;


    
    
    public String getListaValoresAcademicoPreMatricula() {
    	if(listaValoresAcademicoPreMatricula == null){
    		listaValoresAcademicoPreMatricula = "";
    	}
		return listaValoresAcademicoPreMatricula;
	}

	public void setListaValoresAcademicoPreMatricula(String listaValoresAcademicoPreMatricula) {
		this.listaValoresAcademicoPreMatricula = listaValoresAcademicoPreMatricula;
	}

	public Integer getTotalAlunoAptoFormar() {
        if(totalAlunoAptoFormar == null){
            totalAlunoAptoFormar = 0;
        }
        return totalAlunoAptoFormar;
    }

    
    public void setTotalAlunoAptoFormar(Integer totalAlunoAptoFormar) {
        this.totalAlunoAptoFormar = totalAlunoAptoFormar;
    }

    
    public String getListaValoresAcademicoAptoFormar() {
        if(listaValoresAcademicoAptoFormar == null){
            listaValoresAcademicoAptoFormar = "";
        } 
        return listaValoresAcademicoAptoFormar;
    }

    
    public void setListaValoresAcademicoAptoFormar(String listaValoresAcademicoAptoFormar) {
        this.listaValoresAcademicoAptoFormar = listaValoresAcademicoAptoFormar;
    }

    public String getListaValoresAcademicoNovo() {
        if(listaValoresAcademicoNovo == null){
            listaValoresAcademicoNovo = "";
        } 
        return listaValoresAcademicoNovo;
    }

    
    public void setListaValoresAcademicoNovo(String listaValoresAcademicoNovo) {
        this.listaValoresAcademicoNovo = listaValoresAcademicoNovo;
    }

    
    public String getListaValoresAcademicoAtivo() {
        if(listaValoresAcademicoAtivo == null){
            listaValoresAcademicoAtivo = "";
        } 
        return listaValoresAcademicoAtivo;
    }

    
    public void setListaValoresAcademicoAtivo(String listaValoresAcademicoAtivo) {
        this.listaValoresAcademicoAtivo = listaValoresAcademicoAtivo;
    }

    
    public String getListaValoresAcademicoEvasao() {
        if(listaValoresAcademicoEvasao == null){
            listaValoresAcademicoEvasao = "";
        }
        return listaValoresAcademicoEvasao;
    }

    
    public void setListaValoresAcademicoEvasao(String listaValoresAcademicoEvasao) {
        this.listaValoresAcademicoEvasao = listaValoresAcademicoEvasao;
    }
    
    private String listaValoresCategoriaDespesa;
    
    public String getListaValoresCategoriaDespesa() {
        if(listaValoresCategoriaDespesa == null){
            listaValoresCategoriaDespesa = "";
        }
        return listaValoresCategoriaDespesa;
    }

    
    public void setListaValoresCategoriaDespesa(String listaValoresCategoriaDespesa) {
        this.listaValoresCategoriaDespesa = listaValoresCategoriaDespesa;
    }
    
    
   private String listaValoresCategoriaDespesaInicial;
    
    public String getListaValoresCategoriaDespesaInicial() {
        if(listaValoresCategoriaDespesaInicial == null){
            listaValoresCategoriaDespesaInicial = "";
        }
        return listaValoresCategoriaDespesaInicial;
    }

    
    public void setListaValoresCategoriaDespesaInicial(String listaValoresCategoriaDespesaInicial) {
        this.listaValoresCategoriaDespesaInicial = listaValoresCategoriaDespesaInicial;
    }

    
    public List<String> getListaNivelGraficoCategoriaDespesa() {
        if(listaNivelGraficoCategoriaDespesa == null){
            listaNivelGraficoCategoriaDespesa = new ArrayList<String>(0);
        }
        return listaNivelGraficoCategoriaDespesa;
    }

    
    public void setListaNivelGraficoCategoriaDespesa(List<String> listaNivelGraficoCategoriaDespesa) {
        this.listaNivelGraficoCategoriaDespesa = listaNivelGraficoCategoriaDespesa;
    }

//	public Date getDataInicioPorCurso() {
//		return dataInicioPorCurso;
//	}
//
//	public void setDataInicioPorCurso(Date dataInicioPorCurso) {
//		this.dataInicioPorCurso = dataInicioPorCurso;
//	}
//
//	public Date getDataFinalPorCurso() {
//		return dataFinalPorCurso;
//	}
//
//	public void setDataFinalPorCurso(Date dataFinalPorCurso) {
//		this.dataFinalPorCurso = dataFinalPorCurso;
//	}
//
//	public Date getDataInicioPorTurma() {
//		return dataInicioPorTurma;
//	}
//
//	public void setDataInicioPorTurma(Date dataInicioPorTurma) {
//		this.dataInicioPorTurma = dataInicioPorTurma;
//	}
//
//	public Date getDataFinalPorTurma() {
//		return dataFinalPorTurma;
//	}
//
//	public void setDataFinalPorTurma(Date dataFinalPorTurma) {
//		this.dataFinalPorTurma = dataFinalPorTurma;
//	}

	public Integer getCodigoCurso() {
		if(codigoCurso == null){
			codigoCurso = 0;
		}
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public PainelGestorVO getPainelGestorPorTurmaVO() {
		if(painelGestorPorTurmaVO == null){
			painelGestorPorTurmaVO = new PainelGestorVO();
		}
		return painelGestorPorTurmaVO;
	}

	public void setPainelGestorPorTurmaVO(PainelGestorVO painelGestorPorTurmaVO) {
		this.painelGestorPorTurmaVO = painelGestorPorTurmaVO;
	}

	public Integer getTotalAlunoPreMatricula() {
		if(totalAlunoPreMatricula == null){
			totalAlunoPreMatricula = 0;
		}
		return totalAlunoPreMatricula;
	}

	public void setTotalAlunoPreMatricula(Integer totalAlunoPreMatricula) {
		this.totalAlunoPreMatricula = totalAlunoPreMatricula;
	}

//	public Date getDataInicioPorCategoriaDespesa() {
//		return dataInicioPorCategoriaDespesa;
//	}
//
//	public void setDataInicioPorCategoriaDespesa(Date dataInicioPorCategoriaDespesa) {
//		this.dataInicioPorCategoriaDespesa = dataInicioPorCategoriaDespesa;
//	}
//
//	public Date getDataFinalPorCategoriaDespesa() {
//		return dataFinalPorCategoriaDespesa;
//	}
//
//	public void setDataFinalPorCategoriaDespesa(Date dataFinalPorCategoriaDespesa) {
//		this.dataFinalPorCategoriaDespesa = dataFinalPorCategoriaDespesa;
//	}

	public Integer getQuantidadeAlunosAtualmente() {
		if(quantidadeAlunosAtualmente == null){
			quantidadeAlunosAtualmente = 0;
		}
		return quantidadeAlunosAtualmente;
	}

	public void setQuantidadeAlunosAtualmente(Integer quantidadeAlunosAtualmente) {
		this.quantidadeAlunosAtualmente = quantidadeAlunosAtualmente;
	}

	public Integer getTotalAlunoAbandonado() {
		if(totalAlunoAbandonado == null){
			totalAlunoAbandonado = 0;
		}
		return totalAlunoAbandonado;
	}

	public void setTotalAlunoAbandonado(Integer totalAlunoAbandonado) {
		this.totalAlunoAbandonado = totalAlunoAbandonado;
	}

	public Integer getTotalAlunoFormado() {
		if(totalAlunoFormado == null){
			totalAlunoFormado = 0;
		}
		return totalAlunoFormado;
	}

	public void setTotalAlunoFormado(Integer totalAlunoFormado) {
		this.totalAlunoFormado = totalAlunoFormado;
	}


    public Integer getTotalEvasao(){
    	return getTotalAlunoAbandonado()+getTotalAlunoCancelado()+getTotalAlunoTrancado()+getTotalAlunoTransferenciaSaida()+getTotalAlunoTransferenciaInterna();
    }

	public Integer getTotalAlunoRetornoEvasao() {
		if(totalAlunoRetornoEvasao == null){
			totalAlunoRetornoEvasao = 0;
		}
		return totalAlunoRetornoEvasao;
	}

	public void setTotalAlunoRetornoEvasao(Integer totalAlunoRetornoEvasao) {
		this.totalAlunoRetornoEvasao = totalAlunoRetornoEvasao;
	}

	public String getQuantidadeAlunosNaoConsideradosAtualmente() {
		if(quantidadeAlunosNaoConsideradosAtualmente ==  null){
			quantidadeAlunosNaoConsideradosAtualmente = "";
		}
		return quantidadeAlunosNaoConsideradosAtualmente;
	}

	public void setQuantidadeAlunosNaoConsideradosAtualmente(String quantidadeAlunosNaoConsideradosAtualmente) {
		this.quantidadeAlunosNaoConsideradosAtualmente = quantidadeAlunosNaoConsideradosAtualmente;
	}

	public Integer getTotalAlunoTransferenciaInterna() {
		if(totalAlunoTransferenciaInterna == null){
			totalAlunoTransferenciaInterna = 0;
		}
		return totalAlunoTransferenciaInterna;
	}

	public void setTotalAlunoTransferenciaInterna(Integer totalAlunoTransferenciaInterna) {
		this.totalAlunoTransferenciaInterna = totalAlunoTransferenciaInterna;
	}

	public List<PainelGestorMonitoramentoConsultorVO> getPainelGestorMonitoramentoConsultorVOs() {
		if (painelGestorMonitoramentoConsultorVOs == null) {
			painelGestorMonitoramentoConsultorVOs = new ArrayList<PainelGestorMonitoramentoConsultorVO>();
		}
		return painelGestorMonitoramentoConsultorVOs;
	}

	public void setPainelGestorMonitoramentoConsultorVOs(List<PainelGestorMonitoramentoConsultorVO> painelGestorMonitoramentoConsultorVOs) {
		this.painelGestorMonitoramentoConsultorVOs = painelGestorMonitoramentoConsultorVOs;
	}

	public List<PainelGestorMonitoramentoConsultorDetalhamentoVO> getPainelGestorMonitoramentoConsultorDetalhamentoVOs() {
		if (painelGestorMonitoramentoConsultorDetalhamentoVOs == null) {
			painelGestorMonitoramentoConsultorDetalhamentoVOs = new ArrayList();
		}
		return painelGestorMonitoramentoConsultorDetalhamentoVOs;
	}

	public void setPainelGestorMonitoramentoConsultorDetalhamentoVOs(List<PainelGestorMonitoramentoConsultorDetalhamentoVO> painelGestorMonitoramentoConsultorDetalhamentoVOs) {
		this.painelGestorMonitoramentoConsultorDetalhamentoVOs = painelGestorMonitoramentoConsultorDetalhamentoVOs;
	}

	public Boolean getApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs() {
		if (apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs == null ) {
			apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs = Boolean.FALSE;
		}
		return apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs;
	}

	public void setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs) {
		this.apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs = apresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs;
	}

	public String getGraficoMonitoramentoFinanceiroConsultor() {
		if(graficoMonitoramentoFinanceiroConsultor == null){
			graficoMonitoramentoFinanceiroConsultor ="";
		}
		return graficoMonitoramentoFinanceiroConsultor;
	}

	public void setGraficoMonitoramentoFinanceiroConsultor(String graficoMonitoramentoFinanceiroConsultor) {
		this.graficoMonitoramentoFinanceiroConsultor = graficoMonitoramentoFinanceiroConsultor;
	}

	public String getGraficoMonitoramentoSituacaoAcademicaConsultor() {
		if(graficoMonitoramentoSituacaoAcademicaConsultor == null){
			graficoMonitoramentoSituacaoAcademicaConsultor ="";
		}
		return graficoMonitoramentoSituacaoAcademicaConsultor;
	}

	public void setGraficoMonitoramentoSituacaoAcademicaConsultor(String graficoMonitoramentoSituacaoAcademicaConsultor) {
		this.graficoMonitoramentoSituacaoAcademicaConsultor = graficoMonitoramentoSituacaoAcademicaConsultor;
	}

	public String getGraficoMonitoramentoDocumentacaoConsultor() {
		if(graficoMonitoramentoDocumentacaoConsultor == null){
			graficoMonitoramentoDocumentacaoConsultor ="";
		}
		return graficoMonitoramentoDocumentacaoConsultor;
	}

	public void setGraficoMonitoramentoDocumentacaoConsultor(String graficoMonitoramentoDocumentacaoConsultor) {
		this.graficoMonitoramentoDocumentacaoConsultor = graficoMonitoramentoDocumentacaoConsultor;
	}

	public Integer getQtdMatRecebidaMonitoramentoConsultor() {
		return qtdMatRecebidaMonitoramentoConsultor;
	}

	public void setQtdMatRecebidaMonitoramentoConsultor(Integer qtdMatRecebidaMonitoramentoConsultor) {
		this.qtdMatRecebidaMonitoramentoConsultor = qtdMatRecebidaMonitoramentoConsultor;
	}

	public Integer getQtdMatAReceberMonitoramentoConsultor() {
		return qtdMatAReceberMonitoramentoConsultor;
	}

	public void setQtdMatAReceberMonitoramentoConsultor(Integer qtdMatAReceberMonitoramentoConsultor) {
		this.qtdMatAReceberMonitoramentoConsultor = qtdMatAReceberMonitoramentoConsultor;
	}

	public Integer getQtdMatVencidaMonitoramentoConsultor() {
		return qtdMatVencidaMonitoramentoConsultor;
	}

	public void setQtdMatVencidaMonitoramentoConsultor(Integer qtdMatVencidaMonitoramentoConsultor) {
		this.qtdMatVencidaMonitoramentoConsultor = qtdMatVencidaMonitoramentoConsultor;
	}

	public Integer getQtdMatAVencerMonitoramentoConsultor() {
		return qtdMatAVencerMonitoramentoConsultor;
	}

	public void setQtdMatAVencerMonitoramentoConsultor(Integer qtdMatAVencerMonitoramentoConsultor) {
		this.qtdMatAVencerMonitoramentoConsultor = qtdMatAVencerMonitoramentoConsultor;
	}

	public Integer getQtdMatPreMonitoramentoConsultor() {
		return qtdMatPreMonitoramentoConsultor;
	}

	public void setQtdMatPreMonitoramentoConsultor(Integer qtdMatPreMonitoramentoConsultor) {
		this.qtdMatPreMonitoramentoConsultor = qtdMatPreMonitoramentoConsultor;
	}

	public Integer getQtdMatAtivoMonitoramentoConsultor() {
		return qtdMatAtivoMonitoramentoConsultor;
	}

	public void setQtdMatAtivoMonitoramentoConsultor(Integer qtdMatAtivoMonitoramentoConsultor) {
		this.qtdMatAtivoMonitoramentoConsultor = qtdMatAtivoMonitoramentoConsultor;
	}

	public Integer getQtdMatCanceladoMonitoramentoConsultor() {
		return qtdMatCanceladoMonitoramentoConsultor;
	}

	public void setQtdMatCanceladoMonitoramentoConsultor(Integer qtdMatCanceladoMonitoramentoConsultor) {
		this.qtdMatCanceladoMonitoramentoConsultor = qtdMatCanceladoMonitoramentoConsultor;
	}

	public Integer getQtdMatExcluidoMonitoramentoConsultor() {
		return qtdMatExcluidoMonitoramentoConsultor;
	}

	public void setQtdMatExcluidoMonitoramentoConsultor(Integer qtdMatExcluidoMonitoramentoConsultor) {
		this.qtdMatExcluidoMonitoramentoConsultor = qtdMatExcluidoMonitoramentoConsultor;
	}

	public Integer getQtdMatExtensaoMonitoramentoConsultor() {
		return qtdMatExtensaoMonitoramentoConsultor;
	}

	public void setQtdMatExtensaoMonitoramentoConsultor(Integer qtdMatExtensaoMonitoramentoConsultor) {
		this.qtdMatExtensaoMonitoramentoConsultor = qtdMatExtensaoMonitoramentoConsultor;
	}

	public Integer getQtdMatPendenciaDocMonitoramentoConsultor() {
		return qtdMatPendenciaDocMonitoramentoConsultor;
	}

	public void setQtdMatPendenciaDocMonitoramentoConsultor(Integer qtdMatPendenciaDocMonitoramentoConsultor) {
		this.qtdMatPendenciaDocMonitoramentoConsultor = qtdMatPendenciaDocMonitoramentoConsultor;
	}

	public String getGraficoMonitoramentoMatriculaAtivaConsultor() {
		if(graficoMonitoramentoMatriculaAtivaConsultor == null){
			graficoMonitoramentoMatriculaAtivaConsultor = "";
		}
		return graficoMonitoramentoMatriculaAtivaConsultor;
	}

	public void setGraficoMonitoramentoMatriculaAtivaConsultor(String graficoMonitoramentoMatriculaAtivaConsultor) {
		this.graficoMonitoramentoMatriculaAtivaConsultor = graficoMonitoramentoMatriculaAtivaConsultor;
	}

	public Double getTotalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade() {
		if(totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade == null){
			totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade = 0.0;
		}
		return totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade;
	}

	public void setTotalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade(Double totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade) {
		this.totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade = totalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade;
	}

	public Double getTotalProvisaoPeriodoPrimeiroDescontoOutras() {
		if(totalProvisaoPeriodoPrimeiroDescontoOutras == null){
			totalProvisaoPeriodoPrimeiroDescontoOutras = 0.0;
		}
		return totalProvisaoPeriodoPrimeiroDescontoOutras;
	}

	public void setTotalProvisaoPeriodoPrimeiroDescontoOutras(Double totalProvisaoPeriodoPrimeiroDescontoOutras) {
		this.totalProvisaoPeriodoPrimeiroDescontoOutras = totalProvisaoPeriodoPrimeiroDescontoOutras;
	}

	public Double getSaldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade() {
		if(saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade == null){
			saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade = 0.0;
		}
		return saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade;
	}

	public void setSaldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade(Double saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade) {
		this.saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade = saldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade;
	}

	public Double getSaldoReceberDoPeriodoPrimeiroDescontoOutras() {
		if(saldoReceberDoPeriodoPrimeiroDescontoOutras == null){
			saldoReceberDoPeriodoPrimeiroDescontoOutras = 0.0;
		}
		return saldoReceberDoPeriodoPrimeiroDescontoOutras;
	}

	public void setSaldoReceberDoPeriodoPrimeiroDescontoOutras(Double saldoReceberDoPeriodoPrimeiroDescontoOutras) {
		this.saldoReceberDoPeriodoPrimeiroDescontoOutras = saldoReceberDoPeriodoPrimeiroDescontoOutras;
	}
    
	public Double getProvisaoReceberMesMatricula() {
		if(provisaoReceberMesMatricula == null){
			provisaoReceberMesMatricula = 0.0;
		}
		return provisaoReceberMesMatricula;
	}

	public void setProvisaoReceberMesMatricula(Double provisaoReceberMesMatricula) {
		this.provisaoReceberMesMatricula = provisaoReceberMesMatricula;
	}
	
	

	public Double getProvisaoReceberMesMaterialDidatico() {
		if(provisaoReceberMesMaterialDidatico == null){
			provisaoReceberMesMaterialDidatico = 0.0;
		}
		return provisaoReceberMesMaterialDidatico;
	}

	public void setProvisaoReceberMesMaterialDidatico(Double provisaoReceberMesMaterialDidatico) {
		this.provisaoReceberMesMaterialDidatico = provisaoReceberMesMaterialDidatico;
	}

	public Double getProvisaoReceberMesMensalidade() {
		if(provisaoReceberMesMensalidade == null){
			provisaoReceberMesMensalidade = 0.0;
		}
		return provisaoReceberMesMensalidade;
	}

	public void setProvisaoReceberMesMensalidade(Double provisaoReceberMesMensalidade) {
		this.provisaoReceberMesMensalidade = provisaoReceberMesMensalidade;
	}

	public Double getProvisaoReceberMesRequerimento() {
		if(provisaoReceberMesRequerimento == null){
			provisaoReceberMesRequerimento = 0.0;
		}
		return provisaoReceberMesRequerimento;
	}

	public void setProvisaoReceberMesRequerimento(Double provisaoReceberMesRequerimento) {
		this.provisaoReceberMesRequerimento = provisaoReceberMesRequerimento;
	}

	public Double getProvisaoReceberMesBiblioteca() {
		if(provisaoReceberMesBiblioteca == null){
			provisaoReceberMesBiblioteca = 0.0;
		}
		return provisaoReceberMesBiblioteca;
	}

	public void setProvisaoReceberMesBiblioteca(Double provisaoReceberMesBiblioteca) {
		this.provisaoReceberMesBiblioteca = provisaoReceberMesBiblioteca;
	}

	public Double getProvisaoReceberMesDevolucaoCheque() {
		if(provisaoReceberMesDevolucaoCheque == null){
			provisaoReceberMesDevolucaoCheque = 0.0;
		}
		return provisaoReceberMesDevolucaoCheque;
	}

	public void setProvisaoReceberMesDevolucaoCheque(Double provisaoReceberMesDevolucaoCheque) {
		this.provisaoReceberMesDevolucaoCheque = provisaoReceberMesDevolucaoCheque;
	}

	public Double getProvisaoReceberMesNegociacao() {
		if(provisaoReceberMesNegociacao == null){
			provisaoReceberMesNegociacao = 0.0;
		}
		return provisaoReceberMesNegociacao;
	}

	public void setProvisaoReceberMesNegociacao(Double provisaoReceberMesNegociacao) {
		this.provisaoReceberMesNegociacao = provisaoReceberMesNegociacao;
	}

	public Double getProvisaoReceberMesInscricao() {
		if(provisaoReceberMesInscricao == null){
			provisaoReceberMesInscricao = 0.0;
		}
		return provisaoReceberMesInscricao;
	}

	public void setProvisaoReceberMesInscricao(Double provisaoReceberMesInscricao) {
		this.provisaoReceberMesInscricao = provisaoReceberMesInscricao;
	}

	public Double getProvisaoReceberMesBolsaCusteada() {
		if(provisaoReceberMesBolsaCusteada == null){
			provisaoReceberMesBolsaCusteada = 0.0;
		}
		return provisaoReceberMesBolsaCusteada;
	}

	public void setProvisaoReceberMesBolsaCusteada(Double provisaoReceberMesBolsaCusteada) {
		this.provisaoReceberMesBolsaCusteada = provisaoReceberMesBolsaCusteada;
	}

	public Double getProvisaoReceberMesContratoReceita() {
		if(provisaoReceberMesContratoReceita == null){
			provisaoReceberMesContratoReceita = 0.0;
		}
		return provisaoReceberMesContratoReceita;
	}

	public void setProvisaoReceberMesContratoReceita(Double provisaoReceberMesContratoReceita) {
		this.provisaoReceberMesContratoReceita = provisaoReceberMesContratoReceita;
	}

	public Double getProvisaoReceberMesInclusaoReposicao() {
		if(provisaoReceberMesInclusaoReposicao == null){
			provisaoReceberMesInclusaoReposicao = 0.0;
		}
		return provisaoReceberMesInclusaoReposicao;
	}

	public void setProvisaoReceberMesInclusaoReposicao(Double provisaoReceberMesInclusaoReposicao) {
		this.provisaoReceberMesInclusaoReposicao = provisaoReceberMesInclusaoReposicao;
	}

	public Double getProvisaoReceberMesOutros() {
		if(provisaoReceberMesOutros == null){
			provisaoReceberMesOutros = 0.0;
		}
		return provisaoReceberMesOutros;
	}

	public void setProvisaoReceberMesOutros(Double provisaoReceberMesOutros) {
		this.provisaoReceberMesOutros = provisaoReceberMesOutros;
	}

	public Double getTotalValorCalculadoInadimplenciaPeriodo() {
		if(totalValorCalculadoInadimplenciaPeriodo == null){
			totalValorCalculadoInadimplenciaPeriodo = 0.0;
		}
		return totalValorCalculadoInadimplenciaPeriodo;
	}

	public void setTotalValorCalculadoInadimplenciaPeriodo(Double totalValorCalculadoInadimplenciaPeriodo) {
		this.totalValorCalculadoInadimplenciaPeriodo = totalValorCalculadoInadimplenciaPeriodo;
	}

	public Double getSaldoReceberPeriodoMatricula() {
		if(saldoReceberPeriodoMatricula == null){
			saldoReceberPeriodoMatricula = 0.0;
		}
		return saldoReceberPeriodoMatricula;
	}

	public void setSaldoReceberPeriodoMatricula(Double saldoReceberPeriodoMatricula) {
		this.saldoReceberPeriodoMatricula = saldoReceberPeriodoMatricula;
	}
	
	

	public Double getSaldoReceberPeriodoMaterialDidatico() {
		if(saldoReceberPeriodoMaterialDidatico == null){
			saldoReceberPeriodoMaterialDidatico = 0.0;
		}
		return saldoReceberPeriodoMaterialDidatico;
	}

	public void setSaldoReceberPeriodoMaterialDidatico(Double saldoReceberPeriodoMaterialDidatico) {
		this.saldoReceberPeriodoMaterialDidatico = saldoReceberPeriodoMaterialDidatico;
	}

	public Double getSaldoReceberVencidoMaterialDidatico() {
		if(saldoReceberVencidoMaterialDidatico == null){
			saldoReceberVencidoMaterialDidatico = 0.0;
		}
		return saldoReceberVencidoMaterialDidatico;
	}

	public void setSaldoReceberVencidoMaterialDidatico(Double saldoReceberVencidoMaterialDidatico) {
		this.saldoReceberVencidoMaterialDidatico = saldoReceberVencidoMaterialDidatico;
	}

	public Double getSaldoReceberPeriodoMensalidade() {
		if(saldoReceberPeriodoMensalidade == null){
			saldoReceberPeriodoMensalidade = 0.0;
		}
		return saldoReceberPeriodoMensalidade;
	}

	public void setSaldoReceberPeriodoMensalidade(Double saldoReceberPeriodoMensalidade) {
		this.saldoReceberPeriodoMensalidade = saldoReceberPeriodoMensalidade;
	}

	public Double getSaldoReceberPeriodoRequerimento() {
		if(saldoReceberPeriodoRequerimento == null){
			saldoReceberPeriodoRequerimento = 0.0;
		}
		return saldoReceberPeriodoRequerimento;
	}

	public void setSaldoReceberPeriodoRequerimento(Double saldoReceberPeriodoRequerimento) {
		this.saldoReceberPeriodoRequerimento = saldoReceberPeriodoRequerimento;
	}

	public Double getSaldoReceberPeriodoBiblioteca() {
		if(saldoReceberPeriodoBiblioteca == null){
			saldoReceberPeriodoBiblioteca = 0.0;
		}
		return saldoReceberPeriodoBiblioteca;
	}

	public void setSaldoReceberPeriodoBiblioteca(Double saldoReceberPeriodoBiblioteca) {
		this.saldoReceberPeriodoBiblioteca = saldoReceberPeriodoBiblioteca;
	}

	public Double getSaldoReceberPeriodoDevolucaoCheque() {
		if(saldoReceberPeriodoDevolucaoCheque == null){
			saldoReceberPeriodoDevolucaoCheque = 0.0;
		}
		return saldoReceberPeriodoDevolucaoCheque;
	}

	public void setSaldoReceberPeriodoDevolucaoCheque(Double saldoReceberPeriodoDevolucaoCheque) {
		this.saldoReceberPeriodoDevolucaoCheque = saldoReceberPeriodoDevolucaoCheque;
	}

	public Double getSaldoReceberPeriodoNegociacao() {
		if(saldoReceberPeriodoNegociacao == null){
			saldoReceberPeriodoNegociacao = 0.0;
		}
		return saldoReceberPeriodoNegociacao;
	}

	public void setSaldoReceberPeriodoNegociacao(Double saldoReceberPeriodoNegociacao) {
		this.saldoReceberPeriodoNegociacao = saldoReceberPeriodoNegociacao;
	}

	public Double getSaldoReceberPeriodoInscricao() {
		if(saldoReceberPeriodoInscricao == null){
			saldoReceberPeriodoInscricao = 0.0;
		}
		return saldoReceberPeriodoInscricao;
	}

	public void setSaldoReceberPeriodoInscricao(Double saldoReceberPeriodoInscricao) {
		this.saldoReceberPeriodoInscricao = saldoReceberPeriodoInscricao;
	}

	public Double getSaldoReceberPeriodoBolsaCusteada() {
		if(saldoReceberPeriodoBolsaCusteada == null){
			saldoReceberPeriodoBolsaCusteada = 0.0;
		}
		return saldoReceberPeriodoBolsaCusteada;
	}

	public void setSaldoReceberPeriodoBolsaCusteada(Double saldoReceberPeriodoBolsaCusteada) {
		this.saldoReceberPeriodoBolsaCusteada = saldoReceberPeriodoBolsaCusteada;
	}

	public Double getSaldoReceberPeriodoContratoReceita() {
		if(saldoReceberPeriodoContratoReceita == null){
			saldoReceberPeriodoContratoReceita = 0.0;
		}
		return saldoReceberPeriodoContratoReceita;
	}

	public void setSaldoReceberPeriodoContratoReceita(Double saldoReceberPeriodoContratoReceita) {
		this.saldoReceberPeriodoContratoReceita = saldoReceberPeriodoContratoReceita;
	}

	public Double getSaldoReceberPeriodoInclusaoReposicao() {
		if(saldoReceberPeriodoInclusaoReposicao == null){
			saldoReceberPeriodoInclusaoReposicao = 0.0;
		}
		return saldoReceberPeriodoInclusaoReposicao;
	}

	public void setSaldoReceberPeriodoInclusaoReposicao(Double saldoReceberPeriodoInclusaoReposicao) {
		this.saldoReceberPeriodoInclusaoReposicao = saldoReceberPeriodoInclusaoReposicao;
	}

	public Double getSaldoReceberPeriodoOutros() {
		if(saldoReceberPeriodoOutros == null){
			saldoReceberPeriodoOutros = 0.0;
		}
		return saldoReceberPeriodoOutros;
	}

	public void setSaldoReceberPeriodoOutros(Double saldoReceberPeriodoOutros) {
		this.saldoReceberPeriodoOutros = saldoReceberPeriodoOutros;
	}

	public Double getSaldoReceberVencidoPeriodo() {
		if(saldoReceberVencidoPeriodo == null){
			saldoReceberVencidoPeriodo = 0.0;
	}
		return saldoReceberVencidoPeriodo;
	}

	public void setSaldoReceberVencidoPeriodo(Double saldoReceberVencidoPeriodo) {
		this.saldoReceberVencidoPeriodo = saldoReceberVencidoPeriodo;
	}

	public Double getSaldoReceberVencidoMatricula() {
		if(saldoReceberVencidoMatricula == null){
			saldoReceberVencidoMatricula = 0.0;
		}
		return saldoReceberVencidoMatricula;
	}

	public void setSaldoReceberVencidoMatricula(Double saldoReceberVencidoMatricula) {
		this.saldoReceberVencidoMatricula = saldoReceberVencidoMatricula;
	}

	public Double getSaldoReceberVencidoMensalidade() {
		if(saldoReceberVencidoMensalidade == null){
			saldoReceberVencidoMensalidade = 0.0;
		}
		return saldoReceberVencidoMensalidade;
	}

	public void setSaldoReceberVencidoMensalidade(Double saldoReceberVencidoMensalidade) {
		this.saldoReceberVencidoMensalidade = saldoReceberVencidoMensalidade;
	}

	public Double getSaldoReceberVencidoRequerimento() {
		if(saldoReceberVencidoRequerimento == null){
			saldoReceberVencidoRequerimento = 0.0;
		}
		return saldoReceberVencidoRequerimento;
	}

	public void setSaldoReceberVencidoRequerimento(Double saldoReceberVencidoRequerimento) {
		this.saldoReceberVencidoRequerimento = saldoReceberVencidoRequerimento;
	}

	public Double getSaldoReceberVencidoBiblioteca() {
		if(saldoReceberVencidoBiblioteca == null){
			saldoReceberVencidoBiblioteca = 0.0;
		}
		return saldoReceberVencidoBiblioteca;
	}

	public void setSaldoReceberVencidoBiblioteca(Double saldoReceberVencidoBiblioteca) {
		this.saldoReceberVencidoBiblioteca = saldoReceberVencidoBiblioteca;
	}

	public Double getSaldoReceberVencidoDevolucaoCheque() {
		if(saldoReceberVencidoDevolucaoCheque == null){
			saldoReceberVencidoDevolucaoCheque = 0.0;
		}
		return saldoReceberVencidoDevolucaoCheque;
	}

	public void setSaldoReceberVencidoDevolucaoCheque(Double saldoReceberVencidoDevolucaoCheque) {
		this.saldoReceberVencidoDevolucaoCheque = saldoReceberVencidoDevolucaoCheque;
	}

	public Double getSaldoReceberVencidoNegociacao() {
		if(saldoReceberVencidoNegociacao == null){
			saldoReceberVencidoNegociacao = 0.0;
		}
		return saldoReceberVencidoNegociacao;
	}

	public void setSaldoReceberVencidoNegociacao(Double saldoReceberVencidoNegociacao) {
		this.saldoReceberVencidoNegociacao = saldoReceberVencidoNegociacao;
	}

	public Double getSaldoReceberVencidoInscricao() {
		if(saldoReceberVencidoInscricao == null){
			saldoReceberVencidoInscricao = 0.0;
		}
		return saldoReceberVencidoInscricao;
	}

	public void setSaldoReceberVencidoInscricao(Double saldoReceberVencidoInscricao) {
		this.saldoReceberVencidoInscricao = saldoReceberVencidoInscricao;
	}

	public Double getSaldoReceberVencidoBolsaCusteada() {
		if(saldoReceberVencidoBolsaCusteada == null){
			saldoReceberVencidoBolsaCusteada = 0.0;
		}
		return saldoReceberVencidoBolsaCusteada;
	}

	public void setSaldoReceberVencidoBolsaCusteada(Double saldoReceberVencidoBolsaCusteada) {
		this.saldoReceberVencidoBolsaCusteada = saldoReceberVencidoBolsaCusteada;
	}

	public Double getSaldoReceberVencidoContratoReceita() {
		if(saldoReceberVencidoContratoReceita == null){
			saldoReceberVencidoContratoReceita = 0.0;
		}
		return saldoReceberVencidoContratoReceita;
	}

	public void setSaldoReceberVencidoContratoReceita(Double saldoReceberVencidoContratoReceita) {
		this.saldoReceberVencidoContratoReceita = saldoReceberVencidoContratoReceita;
	}

	public Double getSaldoReceberVencidoInclusaoReposicao() {
		if(saldoReceberVencidoInclusaoReposicao == null){
			saldoReceberVencidoInclusaoReposicao = 0.0;
		}
		return saldoReceberVencidoInclusaoReposicao;
	}

	public void setSaldoReceberVencidoInclusaoReposicao(Double saldoReceberVencidoInclusaoReposicao) {
		this.saldoReceberVencidoInclusaoReposicao = saldoReceberVencidoInclusaoReposicao;
	}

	public Double getSaldoReceberVencidoOutros() {
		if(saldoReceberVencidoOutros == null){
			saldoReceberVencidoOutros = 0.0;
		}
		return saldoReceberVencidoOutros;
	}

	public void setSaldoReceberVencidoOutros(Double saldoReceberVencidoOutros) {
		this.saldoReceberVencidoOutros = saldoReceberVencidoOutros;
	}

	public Double getTotalDescontoRecebidoConvenio() {
		if(totalDescontoRecebidoConvenio == null){
			totalDescontoRecebidoConvenio = 0.0;
		}
		return totalDescontoRecebidoConvenio;
	}

	public void setTotalDescontoRecebidoConvenio(Double totalDescontoRecebidoConvenio) {
		this.totalDescontoRecebidoConvenio = totalDescontoRecebidoConvenio;
	}

	public Double getTotalProvisaoPeriodoOutros() {
		if (totalProvisaoPeriodoOutros == null) {
			totalProvisaoPeriodoOutros = 0.0;
		}
		return totalProvisaoPeriodoOutros;
	}

	public void setTotalProvisaoPeriodoOutros(Double totalProvisaoPeriodoOutros) {
		this.totalProvisaoPeriodoOutros = totalProvisaoPeriodoOutros;
	}

	public BigDecimal getTotalReceitaDoMes() {
		if (totalReceitaDoMes == null) {
			totalReceitaDoMes = BigDecimal.ZERO;
		}
		return totalReceitaDoMes;
	}

	public void setTotalReceitaDoMes(BigDecimal totalReceitaDoMes) {
		this.totalReceitaDoMes = totalReceitaDoMes;
	}

	public BigDecimal getTotalAcrescimoDoMes() {
		if (totalAcrescimoDoMes == null) {
			totalAcrescimoDoMes = BigDecimal.ZERO;
		}
		return totalAcrescimoDoMes;
	}

	public void setTotalAcrescimoDoMes(BigDecimal totalAcrescimoDoMes) {
		this.totalAcrescimoDoMes = totalAcrescimoDoMes;
	}

	public BigDecimal getTotalJuroMultaDoMes() {
		if (totalJuroMultaDoMes == null) {
			totalJuroMultaDoMes = BigDecimal.ZERO;
		}
		return totalJuroMultaDoMes;
	}
	
	public void setTotalJuroMultaDoMes(BigDecimal totalJuroMultaDoMes) {
		this.totalJuroMultaDoMes = totalJuroMultaDoMes;
	}
	
	public BigDecimal getTotalDescontoDoMes() {
		if (totalDescontoDoMes == null) {
			totalDescontoDoMes = BigDecimal.ZERO;
		}
		return totalDescontoDoMes;
	}

	public void setTotalDescontoDoMes(BigDecimal totalDescontoDoMes) {
		this.totalDescontoDoMes = totalDescontoDoMes;
	}

	public BigDecimal getTotalReceitaComDescontoAcrescimoDoMes() {
		if (totalReceitaComDescontoAcrescimoDoMes == null) {
			totalReceitaComDescontoAcrescimoDoMes = BigDecimal.ZERO;
		}
		return totalReceitaComDescontoAcrescimoDoMes;
	}

	public void setTotalReceitaComDescontoAcrescimoDoMes(BigDecimal totalReceitaComDescontoAcrescimoDoMes) {
		this.totalReceitaComDescontoAcrescimoDoMes = totalReceitaComDescontoAcrescimoDoMes;
	}

	public BigDecimal getTotalSaldoAReceberDoMes() {
		if (totalSaldoAReceberDoMes == null) {
			totalSaldoAReceberDoMes = BigDecimal.ZERO;
		}
		return totalSaldoAReceberDoMes;
	}

	public void setTotalSaldoAReceberDoMes(BigDecimal totalSaldoAReceberDoMes) {
		this.totalSaldoAReceberDoMes = totalSaldoAReceberDoMes;
	}

	public BigDecimal getTotalRecebidoDoMes() {
		if (totalRecebidoDoMes == null) {
			totalRecebidoDoMes = BigDecimal.ZERO;
		}
		return totalRecebidoDoMes;
	}

	public void setTotalRecebidoDoMes(BigDecimal totalRecebidoDoMes) {
		this.totalRecebidoDoMes = totalRecebidoDoMes;
	}

	public BigDecimal getReceitaDoMesMatricula() {
		if (receitaDoMesMatricula == null) {
			receitaDoMesMatricula = BigDecimal.ZERO;
		}
		return receitaDoMesMatricula;
	}

	public void setReceitaDoMesMatricula(BigDecimal receitaDoMesMatricula) {
		this.receitaDoMesMatricula = receitaDoMesMatricula;
	}

	public BigDecimal getReceitaDoMesMensalidade() {
		if (receitaDoMesMensalidade == null) {
			receitaDoMesMensalidade = BigDecimal.ZERO;
		}
		return receitaDoMesMensalidade;
	}

	public void setReceitaDoMesMensalidade(BigDecimal receitaDoMesMensalidade) {
		this.receitaDoMesMensalidade = receitaDoMesMensalidade;
	}

	public BigDecimal getReceitaDoMesRequerimento() {
		if (receitaDoMesRequerimento == null) {
			receitaDoMesRequerimento = BigDecimal.ZERO;
		}
		return receitaDoMesRequerimento;
	}

	public void setReceitaDoMesRequerimento(BigDecimal receitaDoMesRequerimento) {
		this.receitaDoMesRequerimento = receitaDoMesRequerimento;
	}

	public BigDecimal getReceitaDoMesBiblioteca() {
		if (receitaDoMesBiblioteca == null) {
			receitaDoMesBiblioteca = BigDecimal.ZERO;
		}
		return receitaDoMesBiblioteca;
	}

	public void setReceitaDoMesBiblioteca(BigDecimal receitaDoMesBiblioteca) {
		this.receitaDoMesBiblioteca = receitaDoMesBiblioteca;
	}

	public BigDecimal getReceitaDoMesDevolucaoCheque() {
		if (receitaDoMesDevolucaoCheque == null) {
			receitaDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return receitaDoMesDevolucaoCheque;
	}

	public void setReceitaDoMesDevolucaoCheque(BigDecimal receitaDoMesDevolucaoCheque) {
		this.receitaDoMesDevolucaoCheque = receitaDoMesDevolucaoCheque;
	}

	public BigDecimal getReceitaDoMesNegociacao() {
		if (receitaDoMesNegociacao == null) {
			receitaDoMesNegociacao = BigDecimal.ZERO;
		}
		return receitaDoMesNegociacao;
	}

	public void setReceitaDoMesNegociacao(BigDecimal receitaDoMesNegociacao) {
		this.receitaDoMesNegociacao = receitaDoMesNegociacao;
	}

	public BigDecimal getReceitaDoMesBolsaCusteada() {
		if (receitaDoMesBolsaCusteada == null) {
			receitaDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return receitaDoMesBolsaCusteada;
	}

	public void setReceitaDoMesBolsaCusteada(BigDecimal receitaDoMesBolsaCusteada) {
		this.receitaDoMesBolsaCusteada = receitaDoMesBolsaCusteada;
	}

	public BigDecimal getReceitaDoMesInscricao() {
		if (receitaDoMesInscricao == null) {
			receitaDoMesInscricao = BigDecimal.ZERO;
		}
		return receitaDoMesInscricao;
	}

	public void setReceitaDoMesInscricao(BigDecimal receitaDoMesInscricao) {
		this.receitaDoMesInscricao = receitaDoMesInscricao;
	}

	public BigDecimal getReceitaDoMesContratoReceita() {
		if (receitaDoMesContratoReceita == null) {
			receitaDoMesContratoReceita = BigDecimal.ZERO;
		}
		return receitaDoMesContratoReceita;
	}

	public void setReceitaDoMesContratoReceita(BigDecimal receitaDoMesContratoReceita) {
		this.receitaDoMesContratoReceita = receitaDoMesContratoReceita;
	}

	public BigDecimal getReceitaDoMesInclusaoReposicao() {
		if (receitaDoMesInclusaoReposicao == null) {
			receitaDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return receitaDoMesInclusaoReposicao;
	}

	public void setReceitaDoMesInclusaoReposicao(BigDecimal receitaDoMesInclusaoReposicao) {
		this.receitaDoMesInclusaoReposicao = receitaDoMesInclusaoReposicao;
	}

	public BigDecimal getReceitaDoMesOutros() {
		if (receitaDoMesOutros == null) {
			receitaDoMesOutros = BigDecimal.ZERO;
		}
		return receitaDoMesOutros;
	}

	public void setReceitaDoMesOutros(BigDecimal receitaDoMesOutros) {
		this.receitaDoMesOutros = receitaDoMesOutros;
	}
	
	public BigDecimal getDescontoDoMesMatricula() {
		if (descontoDoMesMatricula == null) {
			descontoDoMesMatricula = BigDecimal.ZERO;
		}
		return descontoDoMesMatricula;
	}

	public void setDescontoDoMesMatricula(BigDecimal descontoDoMesMatricula) {
		this.descontoDoMesMatricula = descontoDoMesMatricula;
	}

	public BigDecimal getDescontoDoMesMensalidade() {
		if (descontoDoMesMensalidade == null) {
			descontoDoMesMensalidade = BigDecimal.ZERO;
		}
		return descontoDoMesMensalidade;
	}

	public void setDescontoDoMesMensalidade(BigDecimal descontoDoMesMensalidade) {
		this.descontoDoMesMensalidade = descontoDoMesMensalidade;
	}

	public BigDecimal getDescontoDoMesRequerimento() {
		if (descontoDoMesRequerimento == null) {
			descontoDoMesRequerimento = BigDecimal.ZERO;
		}
		return descontoDoMesRequerimento;
	}

	public void setDescontoDoMesRequerimento(BigDecimal descontoDoMesRequerimento) {
		this.descontoDoMesRequerimento = descontoDoMesRequerimento;
	}

	public BigDecimal getDescontoDoMesBiblioteca() {
		if (descontoDoMesBiblioteca == null) {
			descontoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return descontoDoMesBiblioteca;
	}

	public void setDescontoDoMesBiblioteca(BigDecimal descontoDoMesBiblioteca) {
		this.descontoDoMesBiblioteca = descontoDoMesBiblioteca;
	}

	public BigDecimal getDescontoDoMesDevolucaoCheque() {
		if (descontoDoMesDevolucaoCheque == null) {
			descontoDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return descontoDoMesDevolucaoCheque;
	}

	public void setDescontoDoMesDevolucaoCheque(BigDecimal descontoDoMesDevolucaoCheque) {
		this.descontoDoMesDevolucaoCheque = descontoDoMesDevolucaoCheque;
	}

	public BigDecimal getDescontoDoMesNegociacao() {
		if (descontoDoMesNegociacao == null) {
			descontoDoMesNegociacao = BigDecimal.ZERO;
		}
		return descontoDoMesNegociacao;
	}

	public void setDescontoDoMesNegociacao(BigDecimal descontoDoMesNegociacao) {
		this.descontoDoMesNegociacao = descontoDoMesNegociacao;
	}

	public BigDecimal getDescontoDoMesBolsaCusteada() {
		if (descontoDoMesBolsaCusteada == null) {
			descontoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return descontoDoMesBolsaCusteada;
	}

	public void setDescontoDoMesBolsaCusteada(BigDecimal descontoDoMesBolsaCusteada) {
		this.descontoDoMesBolsaCusteada = descontoDoMesBolsaCusteada;
	}

	public BigDecimal getDescontoDoMesInscricao() {
		if (descontoDoMesInscricao == null) {
			descontoDoMesInscricao  = BigDecimal.ZERO;
		}
		return descontoDoMesInscricao;
	}

	public void setDescontoDoMesInscricao(BigDecimal descontoDoMesInscricao) {
		this.descontoDoMesInscricao = descontoDoMesInscricao;
	}

	public BigDecimal getDescontoDoMesContratoReceita() {
		if (descontoDoMesContratoReceita == null) {
			descontoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return descontoDoMesContratoReceita;
	}

	public void setDescontoDoMesContratoReceita(BigDecimal descontoDoMesContratoReceita) {
		this.descontoDoMesContratoReceita = descontoDoMesContratoReceita;
	}

	public BigDecimal getDescontoDoMesInclusaoReposicao() {
		if (descontoDoMesInclusaoReposicao == null) {
			descontoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return descontoDoMesInclusaoReposicao;
	}

	public void setDescontoDoMesInclusaoReposicao(BigDecimal descontoDoMesInclusaoReposicao) {
		this.descontoDoMesInclusaoReposicao = descontoDoMesInclusaoReposicao;
	}

	public BigDecimal getDescontoDoMesOutros() {
		if (descontoDoMesOutros == null) {
			descontoDoMesOutros = BigDecimal.ZERO;
		}
		return descontoDoMesOutros;
	}

	public void setDescontoDoMesOutros(BigDecimal descontoDoMesOutros) {
		this.descontoDoMesOutros = descontoDoMesOutros;
	}

	public BigDecimal getAcrescimoDoMesMatricula() {
		if (acrescimoDoMesMatricula == null) {
			acrescimoDoMesMatricula = BigDecimal.ZERO;
		}
		return acrescimoDoMesMatricula;
	}

	public void setAcrescimoDoMesMatricula(BigDecimal acrescimoDoMesMatricula) {
		this.acrescimoDoMesMatricula = acrescimoDoMesMatricula;
	}

	public BigDecimal getAcrescimoDoMesMensalidade() {
		if (acrescimoDoMesMensalidade == null) {
			acrescimoDoMesMensalidade = BigDecimal.ZERO;
		}
		return acrescimoDoMesMensalidade;
	}

	public void setAcrescimoDoMesMensalidade(BigDecimal acrescimoDoMesMensalidade) {
		this.acrescimoDoMesMensalidade = acrescimoDoMesMensalidade;
	}

	public BigDecimal getAcrescimoDoMesRequerimento() {
		if (acrescimoDoMesRequerimento == null) {
			acrescimoDoMesRequerimento = BigDecimal.ZERO;
		}
		return acrescimoDoMesRequerimento;
	}

	public void setAcrescimoDoMesRequerimento(BigDecimal acrescimoDoMesRequerimento) {
		this.acrescimoDoMesRequerimento = acrescimoDoMesRequerimento;
	}

	public BigDecimal getAcrescimoDoMesBiblioteca() {
		if (acrescimoDoMesBiblioteca == null) {
			acrescimoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return acrescimoDoMesBiblioteca;
	}

	public void setAcrescimoDoMesBiblioteca(BigDecimal acrescimoDoMesBiblioteca) {
		this.acrescimoDoMesBiblioteca = acrescimoDoMesBiblioteca;
	}

	public BigDecimal getAcrescimoDoMesDevolucaoCheque() {
		if (acrescimoDoMesDevolucaoCheque == null) {
			acrescimoDoMesDevolucaoCheque =  BigDecimal.ZERO;
		}
		return acrescimoDoMesDevolucaoCheque;
	}

	public void setAcrescimoDoMesDevolucaoCheque(BigDecimal acrescimoDoMesDevolucaoCheque) {
		this.acrescimoDoMesDevolucaoCheque = acrescimoDoMesDevolucaoCheque;
	}

	public BigDecimal getAcrescimoDoMesNegociacao() {
		if (acrescimoDoMesNegociacao == null) {
			acrescimoDoMesNegociacao = BigDecimal.ZERO;
		}
		return acrescimoDoMesNegociacao;
	}

	public void setAcrescimoDoMesNegociacao(BigDecimal acrescimoDoMesNegociacao) {
		this.acrescimoDoMesNegociacao = acrescimoDoMesNegociacao;
	}

	public BigDecimal getAcrescimoDoMesBolsaCusteada() {
		if (acrescimoDoMesBolsaCusteada == null) {
			acrescimoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return acrescimoDoMesBolsaCusteada;
	}

	public void setAcrescimoDoMesBolsaCusteada(BigDecimal acrescimoDoMesBolsaCusteada) {
		this.acrescimoDoMesBolsaCusteada = acrescimoDoMesBolsaCusteada;
	}

	public BigDecimal getAcrescimoDoMesInscricao() {
		if (acrescimoDoMesInscricao == null) {
			acrescimoDoMesInscricao = BigDecimal.ZERO;
		}
		return acrescimoDoMesInscricao;
	}

	public void setAcrescimoDoMesInscricao(BigDecimal acrescimoDoMesInscricao) {
		this.acrescimoDoMesInscricao = acrescimoDoMesInscricao;
	}

	public BigDecimal getAcrescimoDoMesContratoReceita() {
		if (acrescimoDoMesContratoReceita == null) {
			acrescimoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return acrescimoDoMesContratoReceita;
	}

	public void setAcrescimoDoMesContratoReceita(BigDecimal acrescimoDoMesContratoReceita) {
		this.acrescimoDoMesContratoReceita = acrescimoDoMesContratoReceita;
	}

	public BigDecimal getAcrescimoDoMesInclusaoReposicao() {
		if (acrescimoDoMesInclusaoReposicao== null) {
			acrescimoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return acrescimoDoMesInclusaoReposicao;
	}

	public void setAcrescimoDoMesInclusaoReposicao(BigDecimal acrescimoDoMesInclusaoReposicao) {
		this.acrescimoDoMesInclusaoReposicao = acrescimoDoMesInclusaoReposicao;
	}
	
	

	public BigDecimal getAcrescimoDoMesOutros() {
		if (acrescimoDoMesOutros == null) {
			acrescimoDoMesOutros = BigDecimal.ZERO;
		}
		return acrescimoDoMesOutros;
	}

	public void setAcrescimoDoMesOutros(BigDecimal acrescimoDoMesOutros) {
		this.acrescimoDoMesOutros = acrescimoDoMesOutros;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesMatricula() {
		if (receitaComDescontoAcrescimoDoMesMatricula == null) {
			receitaComDescontoAcrescimoDoMesMatricula = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesMatricula;
	}

	public BigDecimal getReceitaDoMesMaterialDidatico() {
		if (receitaDoMesMaterialDidatico == null) {
			receitaDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return receitaDoMesMaterialDidatico;
	}

	public void setReceitaDoMesMaterialDidatico(BigDecimal receitaDoMesMaterialDidatico) {
		this.receitaDoMesMaterialDidatico = receitaDoMesMaterialDidatico;
	}

	public BigDecimal getDescontoDoMesMaterialDidatico() {
		if (descontoDoMesMaterialDidatico == null) {
			descontoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return descontoDoMesMaterialDidatico;
	}

	public void setDescontoDoMesMaterialDidatico(BigDecimal descontoDoMesMaterialDidatico) {
		this.descontoDoMesMaterialDidatico = descontoDoMesMaterialDidatico;
	}

	public BigDecimal getAcrescimoDoMesMaterialDidatico() {
		if (acrescimoDoMesMaterialDidatico == null) {
			acrescimoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return acrescimoDoMesMaterialDidatico;
	}

	public void setAcrescimoDoMesMaterialDidatico(BigDecimal acrescimoDoMesMaterialDidatico) {
		this.acrescimoDoMesMaterialDidatico = acrescimoDoMesMaterialDidatico;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesMaterialDidatico() {
		if (receitaComDescontoAcrescimoDoMesMaterialDidatico == null) {
			receitaComDescontoAcrescimoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesMaterialDidatico;
	}

	public void setReceitaComDescontoAcrescimoDoMesMaterialDidatico(BigDecimal receitaComDescontoAcrescimoDoMesMaterialDidatico) {
		this.receitaComDescontoAcrescimoDoMesMaterialDidatico = receitaComDescontoAcrescimoDoMesMaterialDidatico;
	}

	public BigDecimal getValorRecebidoDoMesMaterialDidatico() {
		if (valorRecebidoDoMesMaterialDidatico == null) {
			valorRecebidoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesMaterialDidatico;
	}

	public void setValorRecebidoDoMesMaterialDidatico(BigDecimal valorRecebidoDoMesMaterialDidatico) {
		this.valorRecebidoDoMesMaterialDidatico = valorRecebidoDoMesMaterialDidatico;
	}

	public BigDecimal getSaldoReceberDoMesMaterialDidatico() {
		if (saldoReceberDoMesMaterialDidatico == null) {
			saldoReceberDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return saldoReceberDoMesMaterialDidatico;
	}

	public void setSaldoReceberDoMesMaterialDidatico(BigDecimal saldoReceberDoMesMaterialDidatico) {
		this.saldoReceberDoMesMaterialDidatico = saldoReceberDoMesMaterialDidatico;
	}

	public BigDecimal getTotalVencidoDoMesMaterialDidatico() {
		if (totalVencidoDoMesMaterialDidatico == null) {
			totalVencidoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return totalVencidoDoMesMaterialDidatico;
	}

	public void setTotalVencidoDoMesMaterialDidatico(BigDecimal totalVencidoDoMesMaterialDidatico) {
		this.totalVencidoDoMesMaterialDidatico = totalVencidoDoMesMaterialDidatico;
	}

	public BigDecimal getReceitaNoMesMaterialDidatico() {
		if (receitaNoMesMaterialDidatico == null) {
			receitaNoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return receitaNoMesMaterialDidatico;
	}

	public void setReceitaNoMesMaterialDidatico(BigDecimal receitaNoMesMaterialDidatico) {
		this.receitaNoMesMaterialDidatico = receitaNoMesMaterialDidatico;
	}

	public BigDecimal getDescontoNoMesMaterialDidatico() {
		if (descontoNoMesMaterialDidatico == null) {
			descontoNoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return descontoNoMesMaterialDidatico;
	}

	public void setDescontoNoMesMaterialDidatico(BigDecimal descontoNoMesMaterialDidatico) {
		this.descontoNoMesMaterialDidatico = descontoNoMesMaterialDidatico;
	}

	public BigDecimal getAcrescimoNoMesMaterialDidatico() {
		if (acrescimoNoMesMaterialDidatico == null) {
			acrescimoNoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return acrescimoNoMesMaterialDidatico;
	}

	public void setAcrescimoNoMesMaterialDidatico(BigDecimal acrescimoNoMesMaterialDidatico) {
		this.acrescimoNoMesMaterialDidatico = acrescimoNoMesMaterialDidatico;
	}

	public void setReceitaComDescontoAcrescimoDoMesMatricula(BigDecimal receitaComDescontoAcrescimoDoMesMatricula) {
		this.receitaComDescontoAcrescimoDoMesMatricula = receitaComDescontoAcrescimoDoMesMatricula;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesMensalidade() {
		if (receitaComDescontoAcrescimoDoMesMensalidade == null) {
			receitaComDescontoAcrescimoDoMesMensalidade = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesMensalidade;
	}

	public void setReceitaComDescontoAcrescimoDoMesMensalidade(BigDecimal receitaComDescontoAcrescimoDoMesMensalidade) {
		this.receitaComDescontoAcrescimoDoMesMensalidade = receitaComDescontoAcrescimoDoMesMensalidade;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesRequerimento() {
		if (receitaComDescontoAcrescimoDoMesRequerimento == null) {
			receitaComDescontoAcrescimoDoMesRequerimento= BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesRequerimento;
	}

	public void setReceitaComDescontoAcrescimoDoMesRequerimento(BigDecimal receitaComDescontoAcrescimoDoMesRequerimento) {
		this.receitaComDescontoAcrescimoDoMesRequerimento = receitaComDescontoAcrescimoDoMesRequerimento;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesBiblioteca() {
		if (receitaComDescontoAcrescimoDoMesBiblioteca == null) {
			receitaComDescontoAcrescimoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesBiblioteca;
	}

	public void setReceitaComDescontoAcrescimoDoMesBiblioteca(BigDecimal receitaComDescontoAcrescimoDoMesBiblioteca) {
		this.receitaComDescontoAcrescimoDoMesBiblioteca = receitaComDescontoAcrescimoDoMesBiblioteca;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesDevolucaoCheque() {
		if (receitaComDescontoAcrescimoDoMesDevolucaoCheque == null) {
			receitaComDescontoAcrescimoDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesDevolucaoCheque;
	}

	public void setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(BigDecimal receitaComDescontoAcrescimoDoMesDevolucaoCheque) {
		this.receitaComDescontoAcrescimoDoMesDevolucaoCheque = receitaComDescontoAcrescimoDoMesDevolucaoCheque;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesNegociacao() {
		if (receitaComDescontoAcrescimoDoMesNegociacao == null) {
			receitaComDescontoAcrescimoDoMesNegociacao = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesNegociacao;
	}

	public void setReceitaComDescontoAcrescimoDoMesNegociacao(BigDecimal receitaComDescontoAcrescimoDoMesNegociacao) {
		this.receitaComDescontoAcrescimoDoMesNegociacao = receitaComDescontoAcrescimoDoMesNegociacao;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesBolsaCusteada() {
		if (receitaComDescontoAcrescimoDoMesBolsaCusteada == null) {
			receitaComDescontoAcrescimoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesBolsaCusteada;
	}

	public void setReceitaComDescontoAcrescimoDoMesBolsaCusteada(BigDecimal receitaComDescontoAcrescimoDoMesBolsaCusteada) {
		this.receitaComDescontoAcrescimoDoMesBolsaCusteada = receitaComDescontoAcrescimoDoMesBolsaCusteada;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesInscricao() {
		if (receitaComDescontoAcrescimoDoMesInscricao == null) {
			receitaComDescontoAcrescimoDoMesInscricao = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesInscricao;
	}

	public void setReceitaComDescontoAcrescimoDoMesInscricao(BigDecimal receitaComDescontoAcrescimoDoMesInscricao) {
		this.receitaComDescontoAcrescimoDoMesInscricao = receitaComDescontoAcrescimoDoMesInscricao;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesContratoReceita() {
		if (receitaComDescontoAcrescimoDoMesContratoReceita == null) {
			receitaComDescontoAcrescimoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesContratoReceita;
	}

	public void setReceitaComDescontoAcrescimoDoMesContratoReceita(BigDecimal receitaComDescontoAcrescimoDoMesContratoReceita) {
		this.receitaComDescontoAcrescimoDoMesContratoReceita = receitaComDescontoAcrescimoDoMesContratoReceita;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesInclusaoReposicao() {
		if (receitaComDescontoAcrescimoDoMesInclusaoReposicao == null) {
			receitaComDescontoAcrescimoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesInclusaoReposicao;
	}

	public void setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(BigDecimal receitaComDescontoAcrescimoDoMesInclusaoReposicao) {
		this.receitaComDescontoAcrescimoDoMesInclusaoReposicao = receitaComDescontoAcrescimoDoMesInclusaoReposicao;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesOutros() {
		if (receitaComDescontoAcrescimoDoMesOutros == null) {
			receitaComDescontoAcrescimoDoMesOutros = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesOutros;
	}

	public void setReceitaComDescontoAcrescimoDoMesOutros(BigDecimal receitaComDescontoAcrescimoDoMesOutros) {
		this.receitaComDescontoAcrescimoDoMesOutros = receitaComDescontoAcrescimoDoMesOutros;
	}

	public BigDecimal getValorRecebidoDoMesMatricula() {
		if (valorRecebidoDoMesMatricula == null) {
			valorRecebidoDoMesMatricula = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesMatricula;
	}

	public void setValorRecebidoDoMesMatricula(BigDecimal valorRecebidoDoMesMatricula) {
		this.valorRecebidoDoMesMatricula = valorRecebidoDoMesMatricula;
	}

	public BigDecimal getValorRecebidoDoMesMensalidade() {
		if (valorRecebidoDoMesMensalidade == null) {
			valorRecebidoDoMesMensalidade = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesMensalidade;
	}

	public void setValorRecebidoDoMesMensalidade(BigDecimal valorRecebidoDoMesMensalidade) {
		this.valorRecebidoDoMesMensalidade = valorRecebidoDoMesMensalidade;
	}

	public BigDecimal getValorRecebidoDoMesRequerimento() {
		if (valorRecebidoDoMesRequerimento == null) {
			valorRecebidoDoMesRequerimento = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesRequerimento;
	}

	public void setValorRecebidoDoMesRequerimento(BigDecimal valorRecebidoDoMesRequerimento) {
		this.valorRecebidoDoMesRequerimento = valorRecebidoDoMesRequerimento;
	}

	public BigDecimal getValorRecebidoDoMesBiblioteca() {
		if (valorRecebidoDoMesBiblioteca == null) {
			valorRecebidoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesBiblioteca;
	}

	public void setValorRecebidoDoMesBiblioteca(BigDecimal valorRecebidoDoMesBiblioteca) {
		this.valorRecebidoDoMesBiblioteca = valorRecebidoDoMesBiblioteca;
	}

	public BigDecimal getValorRecebidoDoMesDevolucaoCheque() {
		if (valorRecebidoDoMesDevolucaoCheque == null) {
			valorRecebidoDoMesDevolucaoCheque= BigDecimal.ZERO;
		}
		return valorRecebidoDoMesDevolucaoCheque;
	}

	public void setValorRecebidoDoMesDevolucaoCheque(BigDecimal valorRecebidoDoMesDevolucaoCheque) {
		this.valorRecebidoDoMesDevolucaoCheque = valorRecebidoDoMesDevolucaoCheque;
	}

	public BigDecimal getValorRecebidoDoMesNegociacao() {
		if (valorRecebidoDoMesNegociacao == null) {
			valorRecebidoDoMesNegociacao = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesNegociacao;
	}

	public void setValorRecebidoDoMesNegociacao(BigDecimal valorRecebidoDoMesNegociacao) {
		this.valorRecebidoDoMesNegociacao = valorRecebidoDoMesNegociacao;
	}

	public BigDecimal getValorRecebidoDoMesBolsaCusteada() {
		if (valorRecebidoDoMesBolsaCusteada == null) {
			valorRecebidoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesBolsaCusteada;
	}

	public void setValorRecebidoDoMesBolsaCusteada(BigDecimal valorRecebidoDoMesBolsaCusteada) {
		this.valorRecebidoDoMesBolsaCusteada = valorRecebidoDoMesBolsaCusteada;
	}

	public BigDecimal getValorRecebidoDoMesInscricao() {
		if (valorRecebidoDoMesInscricao == null) {
			valorRecebidoDoMesInscricao = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesInscricao;
	}

	public void setValorRecebidoDoMesInscricao(BigDecimal valorRecebidoDoMesInscricao) {
		this.valorRecebidoDoMesInscricao = valorRecebidoDoMesInscricao;
	}

	public BigDecimal getValorRecebidoDoMesContratoReceita() {
		if (valorRecebidoDoMesContratoReceita == null) {
			valorRecebidoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesContratoReceita;
	}

	public void setValorRecebidoDoMesContratoReceita(BigDecimal valorRecebidoDoMesContratoReceita) {
		this.valorRecebidoDoMesContratoReceita = valorRecebidoDoMesContratoReceita;
	}

	public BigDecimal getValorRecebidoDoMesInclusaoReposicao() {
		if (valorRecebidoDoMesInclusaoReposicao == null) {
			valorRecebidoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesInclusaoReposicao;
	}

	public void setValorRecebidoDoMesInclusaoReposicao(BigDecimal valorRecebidoDoMesInclusaoReposicao) {
		this.valorRecebidoDoMesInclusaoReposicao = valorRecebidoDoMesInclusaoReposicao;
	}

	public BigDecimal getValorRecebidoDoMesOutros() {
		if (valorRecebidoDoMesOutros == null) {
			valorRecebidoDoMesOutros = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesOutros;
	}

	public void setValorRecebidoDoMesOutros(BigDecimal valorRecebidoDoMesOutros) {
		this.valorRecebidoDoMesOutros = valorRecebidoDoMesOutros;
	}

	public BigDecimal getSaldoReceberDoMesMatricula() {
		if (saldoReceberDoMesMatricula == null) {
			saldoReceberDoMesMatricula = BigDecimal.ZERO;
		}
		return saldoReceberDoMesMatricula;
	}

	public void setSaldoReceberDoMesMatricula(BigDecimal saldoReceberDoMesMatricula) {
		this.saldoReceberDoMesMatricula = saldoReceberDoMesMatricula;
	}

	public BigDecimal getSaldoReceberDoMesMensalidade() {
		if (saldoReceberDoMesMensalidade == null) {
			saldoReceberDoMesMensalidade = BigDecimal.ZERO;
		}
		return saldoReceberDoMesMensalidade;
	}

	public void setSaldoReceberDoMesMensalidade(BigDecimal saldoReceberDoMesMensalidade) {
		this.saldoReceberDoMesMensalidade = saldoReceberDoMesMensalidade;
	}

	public BigDecimal getSaldoReceberDoMesRequerimento() {
		if (saldoReceberDoMesRequerimento == null) {
			saldoReceberDoMesRequerimento = BigDecimal.ZERO;
		}
		return saldoReceberDoMesRequerimento;
	}

	public void setSaldoReceberDoMesRequerimento(BigDecimal saldoReceberDoMesRequerimento) {
		this.saldoReceberDoMesRequerimento = saldoReceberDoMesRequerimento;
	}

	public BigDecimal getSaldoReceberDoMesBiblioteca() {
		if (saldoReceberDoMesBiblioteca == null) {
			saldoReceberDoMesBiblioteca = BigDecimal.ZERO;
		}
		return saldoReceberDoMesBiblioteca;
	}

	public void setSaldoReceberDoMesBiblioteca(BigDecimal saldoReceberDoMesBiblioteca) {
		this.saldoReceberDoMesBiblioteca = saldoReceberDoMesBiblioteca;
	}

	public BigDecimal getSaldoReceberDoMesDevolucaoCheque() {
		if (saldoReceberDoMesDevolucaoCheque == null) {
			saldoReceberDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return saldoReceberDoMesDevolucaoCheque;
	}

	public void setSaldoReceberDoMesDevolucaoCheque(BigDecimal saldoReceberDoMesDevolucaoCheque) {
		this.saldoReceberDoMesDevolucaoCheque = saldoReceberDoMesDevolucaoCheque;
	}

	public BigDecimal getSaldoReceberDoMesNegociacao() {
		if (saldoReceberDoMesNegociacao == null) {
			saldoReceberDoMesNegociacao = BigDecimal.ZERO;
		}
		return saldoReceberDoMesNegociacao;
	}

	public void setSaldoReceberDoMesNegociacao(BigDecimal saldoReceberDoMesNegociacao) {
		this.saldoReceberDoMesNegociacao = saldoReceberDoMesNegociacao;
	}

	public BigDecimal getSaldoReceberDoMesBolsaCusteada() {
		if (saldoReceberDoMesBolsaCusteada == null) {
			saldoReceberDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return saldoReceberDoMesBolsaCusteada;
	}

	public void setSaldoReceberDoMesBolsaCusteada(BigDecimal saldoReceberDoMesBolsaCusteada) {
		this.saldoReceberDoMesBolsaCusteada = saldoReceberDoMesBolsaCusteada;
	}

	public BigDecimal getSaldoReceberDoMesInscricao() {
		if (saldoReceberDoMesInscricao == null) {
			saldoReceberDoMesInscricao = BigDecimal.ZERO;
		}
		return saldoReceberDoMesInscricao;
	}

	public void setSaldoReceberDoMesInscricao(BigDecimal saldoReceberDoMesInscricao) {
		this.saldoReceberDoMesInscricao = saldoReceberDoMesInscricao;
	}

	public BigDecimal getSaldoReceberDoMesContratoReceita() {
		if (saldoReceberDoMesContratoReceita == null) {
			saldoReceberDoMesContratoReceita = BigDecimal.ZERO;
		}
		return saldoReceberDoMesContratoReceita;
	}

	public void setSaldoReceberDoMesContratoReceita(BigDecimal saldoReceberDoMesContratoReceita) {
		this.saldoReceberDoMesContratoReceita = saldoReceberDoMesContratoReceita;
	}

	public BigDecimal getSaldoReceberDoMesInclusaoReposicao() {
		if (saldoReceberDoMesInclusaoReposicao == null) {
			saldoReceberDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return saldoReceberDoMesInclusaoReposicao;
	}

	public void setSaldoReceberDoMesInclusaoReposicao(BigDecimal saldoReceberDoMesInclusaoReposicao) {
		this.saldoReceberDoMesInclusaoReposicao = saldoReceberDoMesInclusaoReposicao;
	}

	public BigDecimal getSaldoReceberDoMesOutros() {
		if (saldoReceberDoMesOutros== null) {
			saldoReceberDoMesOutros = BigDecimal.ZERO;
		}
		return saldoReceberDoMesOutros;
	}

	public void setSaldoReceberDoMesOutros(BigDecimal saldoReceberDoMesOutros) {
		this.saldoReceberDoMesOutros = saldoReceberDoMesOutros;
	}

	public BigDecimal getTotalVencidoDoMes() {
		if (totalVencidoDoMes == null) {
			totalVencidoDoMes = BigDecimal.ZERO;
		}
		return totalVencidoDoMes;
	}

	public void setTotalVencidoDoMes(BigDecimal totalVencidoDoMes) {
		this.totalVencidoDoMes = totalVencidoDoMes;
	}

	public BigDecimal getTotalVencidoDoMesMatricula() {
		if (totalVencidoDoMesMatricula == null) {
			totalVencidoDoMesMatricula = BigDecimal.ZERO;
		}
		return totalVencidoDoMesMatricula;
	}

	public void setTotalVencidoDoMesMatricula(BigDecimal totalVencidoDoMesMatricula) {
		this.totalVencidoDoMesMatricula = totalVencidoDoMesMatricula;
	}

	public BigDecimal getTotalVencidoDoMesMensalidade() {
		if (totalVencidoDoMesMensalidade == null) {
			totalVencidoDoMesMensalidade = BigDecimal.ZERO;
		}
		return totalVencidoDoMesMensalidade;
	}

	public void setTotalVencidoDoMesMensalidade(BigDecimal totalVencidoDoMesMensalidade) {
		this.totalVencidoDoMesMensalidade = totalVencidoDoMesMensalidade;
	}

	public BigDecimal getTotalVencidoDoMesRequerimento() {
		if (totalVencidoDoMesRequerimento == null) {
			totalVencidoDoMesRequerimento = BigDecimal.ZERO;
		}
		return totalVencidoDoMesRequerimento;
	}

	public void setTotalVencidoDoMesRequerimento(BigDecimal totalVencidoDoMesRequerimento) {
		this.totalVencidoDoMesRequerimento = totalVencidoDoMesRequerimento;
	}

	public BigDecimal getTotalVencidoDoMesBiblioteca() {
		if (totalVencidoDoMesBiblioteca == null) {
			totalVencidoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return totalVencidoDoMesBiblioteca;
	}

	public void setTotalVencidoDoMesBiblioteca(BigDecimal totalVencidoDoMesBiblioteca) {
		this.totalVencidoDoMesBiblioteca = totalVencidoDoMesBiblioteca;
	}

	public BigDecimal getTotalVencidoDoMesDevolucaoCheque() {
		if (totalVencidoDoMesDevolucaoCheque == null) {
			totalVencidoDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return totalVencidoDoMesDevolucaoCheque;
	}

	public void setTotalVencidoDoMesDevolucaoCheque(BigDecimal totalVencidoDoMesDevolucaoCheque) {
		this.totalVencidoDoMesDevolucaoCheque = totalVencidoDoMesDevolucaoCheque;
	}

	public BigDecimal getTotalVencidoDoMesNegociacao() {
		if (totalVencidoDoMesNegociacao == null) {
			totalVencidoDoMesNegociacao = BigDecimal.ZERO;
		}
		return totalVencidoDoMesNegociacao;
	}

	public void setTotalVencidoDoMesNegociacao(BigDecimal totalVencidoDoMesNegociacao) {
		this.totalVencidoDoMesNegociacao = totalVencidoDoMesNegociacao;
	}

	public BigDecimal getTotalVencidoDoMesBolsaCusteada() {
		if (totalVencidoDoMesBolsaCusteada == null) {
			totalVencidoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return totalVencidoDoMesBolsaCusteada;
	}

	public void setTotalVencidoDoMesBolsaCusteada(BigDecimal totalVencidoDoMesBolsaCusteada) {
		this.totalVencidoDoMesBolsaCusteada = totalVencidoDoMesBolsaCusteada;
	}

	public BigDecimal getTotalVencidoDoMesInscricao() {
		if (totalVencidoDoMesInscricao == null) {
			totalVencidoDoMesInscricao = BigDecimal.ZERO;
		}
		return totalVencidoDoMesInscricao;
	}

	public void setTotalVencidoDoMesInscricao(BigDecimal totalVencidoDoMesInscricao) {
		this.totalVencidoDoMesInscricao = totalVencidoDoMesInscricao;
	}

	public BigDecimal getTotalVencidoDoMesContratoReceita() {
		if (totalVencidoDoMesContratoReceita == null) {
			totalVencidoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return totalVencidoDoMesContratoReceita;
	}

	public void setTotalVencidoDoMesContratoReceita(BigDecimal totalVencidoDoMesContratoReceita) {
		this.totalVencidoDoMesContratoReceita = totalVencidoDoMesContratoReceita;
	}

	public BigDecimal getTotalVencidoDoMesInclusaoReposicao() {
		if (totalVencidoDoMesInclusaoReposicao == null) {
			totalVencidoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return totalVencidoDoMesInclusaoReposicao;
	}

	public void setTotalVencidoDoMesInclusaoReposicao(BigDecimal totalVencidoDoMesInclusaoReposicao) {
		this.totalVencidoDoMesInclusaoReposicao = totalVencidoDoMesInclusaoReposicao;
	}

	public BigDecimal getTotalVencidoDoMesOutros() {
		if (totalVencidoDoMesOutros == null) {
			totalVencidoDoMesOutros = BigDecimal.ZERO;
		}
		return totalVencidoDoMesOutros;
	}

	public void setTotalVencidoDoMesOutros(BigDecimal totalVencidoDoMesOutros) {
		this.totalVencidoDoMesOutros = totalVencidoDoMesOutros;
	}

	public List<PainelGestorContaReceberMesAnoVO> getPainelGestorContaReceberFluxoCaixaMesAnoVOs() {
		if (painelGestorContaReceberFluxoCaixaMesAnoVOs == null) {
			painelGestorContaReceberFluxoCaixaMesAnoVOs = new ArrayList<PainelGestorContaReceberMesAnoVO>(0);
		}
		return painelGestorContaReceberFluxoCaixaMesAnoVOs;
	}

	public void setPainelGestorContaReceberFluxoCaixaMesAnoVOs(List<PainelGestorContaReceberMesAnoVO> painelGestorContaReceberFluxoCaixaMesAnoVOs) {
		this.painelGestorContaReceberFluxoCaixaMesAnoVOs = painelGestorContaReceberFluxoCaixaMesAnoVOs;
	}

	public BigDecimal getTotalReceitaNoMes() {
		if (totalReceitaNoMes == null) {
			totalReceitaNoMes = BigDecimal.ZERO;
		}
		return totalReceitaNoMes;
	}

	public void setTotalReceitaNoMes(BigDecimal totalReceitaNoMes) {
		this.totalReceitaNoMes = totalReceitaNoMes;
	}

	public BigDecimal getTotalAcrescimoNoMes() {
		if (totalAcrescimoNoMes == null) {
			totalAcrescimoNoMes = BigDecimal.ZERO;
		}
		return totalAcrescimoNoMes;
	}

	public void setTotalAcrescimoNoMes(BigDecimal totalAcrescimoNoMes) {
		this.totalAcrescimoNoMes = totalAcrescimoNoMes;
	}

	public BigDecimal getTotalDescontoNoMes() {
		if (totalDescontoNoMes == null) {
			totalDescontoNoMes = BigDecimal.ZERO;
		}
		return totalDescontoNoMes;
	}

	public void setTotalDescontoNoMes(BigDecimal totalDescontoNoMes) {
		this.totalDescontoNoMes = totalDescontoNoMes;
	}

	public BigDecimal getReceitaNoMesMatricula() {
		if (receitaNoMesMatricula == null) {
			receitaNoMesMatricula = BigDecimal.ZERO;
		}
		return receitaNoMesMatricula;
	}

	public void setReceitaNoMesMatricula(BigDecimal receitaNoMesMatricula) {
		this.receitaNoMesMatricula = receitaNoMesMatricula;
	}

	public BigDecimal getReceitaNoMesMensalidade() {
		if (receitaNoMesMensalidade == null) {
			receitaNoMesMensalidade = BigDecimal.ZERO;
		}
		return receitaNoMesMensalidade;
	}

	public void setReceitaNoMesMensalidade(BigDecimal receitaNoMesMensalidade) {
		this.receitaNoMesMensalidade = receitaNoMesMensalidade;
	}

	public BigDecimal getReceitaNoMesRequerimento() {
		if (receitaNoMesRequerimento == null) {
			receitaNoMesRequerimento = BigDecimal.ZERO;
		}
		return receitaNoMesRequerimento;
	}

	public void setReceitaNoMesRequerimento(BigDecimal receitaNoMesRequerimento) {
		this.receitaNoMesRequerimento = receitaNoMesRequerimento;
	}

	public BigDecimal getReceitaNoMesBiblioteca() {
		if (receitaNoMesBiblioteca == null) {
			receitaNoMesBiblioteca = BigDecimal.ZERO;
		}
		return receitaNoMesBiblioteca;
	}

	public void setReceitaNoMesBiblioteca(BigDecimal receitaNoMesBiblioteca) {
		this.receitaNoMesBiblioteca = receitaNoMesBiblioteca;
	}

	public BigDecimal getReceitaNoMesDevolucaoCheque() {
		if (receitaNoMesDevolucaoCheque == null) {
			receitaNoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return receitaNoMesDevolucaoCheque;
	}

	public void setReceitaNoMesDevolucaoCheque(BigDecimal receitaNoMesDevolucaoCheque) {
		this.receitaNoMesDevolucaoCheque = receitaNoMesDevolucaoCheque;
	}

	public BigDecimal getReceitaNoMesNegociacao() {
		if (receitaNoMesNegociacao == null) {
			receitaNoMesNegociacao = BigDecimal.ZERO;
		}
		return receitaNoMesNegociacao;
	}

	public void setReceitaNoMesNegociacao(BigDecimal receitaNoMesNegociacao) {
		this.receitaNoMesNegociacao = receitaNoMesNegociacao;
	}

	public BigDecimal getReceitaNoMesBolsaCusteada() {
		if (receitaNoMesBolsaCusteada == null) {
			receitaNoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return receitaNoMesBolsaCusteada;
	}

	public void setReceitaNoMesBolsaCusteada(BigDecimal receitaNoMesBolsaCusteada) {
		this.receitaNoMesBolsaCusteada = receitaNoMesBolsaCusteada;
	}

	public BigDecimal getReceitaNoMesInscricao() {
		if (receitaNoMesInscricao == null) {
			receitaNoMesInscricao = BigDecimal.ZERO;
		}
		return receitaNoMesInscricao;
	}

	public void setReceitaNoMesInscricao(BigDecimal receitaNoMesInscricao) {
		this.receitaNoMesInscricao = receitaNoMesInscricao;
	}

	public BigDecimal getReceitaNoMesContratoReceita() {
		if (receitaNoMesContratoReceita == null) {
			receitaNoMesContratoReceita = BigDecimal.ZERO;
		}
		return receitaNoMesContratoReceita;
	}

	public void setReceitaNoMesContratoReceita(BigDecimal receitaNoMesContratoReceita) {
		this.receitaNoMesContratoReceita = receitaNoMesContratoReceita;
	}

	public BigDecimal getReceitaNoMesInclusaoReposicao() {
		if (receitaNoMesInclusaoReposicao == null) {
			receitaNoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return receitaNoMesInclusaoReposicao;
	}

	public void setReceitaNoMesInclusaoReposicao(BigDecimal receitaNoMesInclusaoReposicao) {
		this.receitaNoMesInclusaoReposicao = receitaNoMesInclusaoReposicao;
	}

	public BigDecimal getReceitaNoMesOutros() {
		if (receitaNoMesOutros == null) {
			receitaNoMesOutros = BigDecimal.ZERO;
		}
		return receitaNoMesOutros;
	}

	public void setReceitaNoMesOutros(BigDecimal receitaNoMesOutros) {
		this.receitaNoMesOutros = receitaNoMesOutros;
	}

	public BigDecimal getDescontoNoMesMatricula() {
		if (descontoNoMesMatricula == null) {
			descontoNoMesMatricula = BigDecimal.ZERO;
		}
		return descontoNoMesMatricula;
	}

	public void setDescontoNoMesMatricula(BigDecimal descontoNoMesMatricula) {
		this.descontoNoMesMatricula = descontoNoMesMatricula;
	}

	public BigDecimal getDescontoNoMesMensalidade() {
		if (descontoNoMesMensalidade == null) {
			descontoNoMesMensalidade = BigDecimal.ZERO;
		}
		return descontoNoMesMensalidade;
	}

	public void setDescontoNoMesMensalidade(BigDecimal descontoNoMesMensalidade) {
		this.descontoNoMesMensalidade = descontoNoMesMensalidade;
	}

	public BigDecimal getDescontoNoMesRequerimento() {
		if (descontoNoMesRequerimento == null) {
			descontoNoMesRequerimento = BigDecimal.ZERO;
		}
		return descontoNoMesRequerimento;
	}

	public void setDescontoNoMesRequerimento(BigDecimal descontoNoMesRequerimento) {
		this.descontoNoMesRequerimento = descontoNoMesRequerimento;
	}

	public BigDecimal getDescontoNoMesBiblioteca() {
		if (descontoNoMesBiblioteca == null) {
			descontoNoMesBiblioteca = BigDecimal.ZERO;
		}
		return descontoNoMesBiblioteca;
	}

	public void setDescontoNoMesBiblioteca(BigDecimal descontoNoMesBiblioteca) {
		this.descontoNoMesBiblioteca = descontoNoMesBiblioteca;
	}

	public BigDecimal getDescontoNoMesDevolucaoCheque() {
		if (descontoNoMesDevolucaoCheque == null) {
			descontoNoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return descontoNoMesDevolucaoCheque;
	}

	public void setDescontoNoMesDevolucaoCheque(BigDecimal descontoNoMesDevolucaoCheque) {
		this.descontoNoMesDevolucaoCheque = descontoNoMesDevolucaoCheque;
	}

	public BigDecimal getDescontoNoMesNegociacao() {
		if (descontoNoMesNegociacao == null) {
			descontoNoMesNegociacao = BigDecimal.ZERO;
		}
		return descontoNoMesNegociacao;
	}

	public void setDescontoNoMesNegociacao(BigDecimal descontoNoMesNegociacao) {
		this.descontoNoMesNegociacao = descontoNoMesNegociacao;
	}

	public BigDecimal getDescontoNoMesBolsaCusteada() {
		if (descontoNoMesBolsaCusteada == null) {
			descontoNoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return descontoNoMesBolsaCusteada;
	}

	public void setDescontoNoMesBolsaCusteada(BigDecimal descontoNoMesBolsaCusteada) {
		this.descontoNoMesBolsaCusteada = descontoNoMesBolsaCusteada;
	}

	public BigDecimal getDescontoNoMesInscricao() {
		if (descontoNoMesInscricao == null) {
			descontoNoMesInscricao = BigDecimal.ZERO;
		}
		return descontoNoMesInscricao;
	}

	public void setDescontoNoMesInscricao(BigDecimal descontoNoMesInscricao) {
		this.descontoNoMesInscricao = descontoNoMesInscricao;
	}

	public BigDecimal getDescontoNoMesContratoReceita() {
		if (descontoNoMesContratoReceita == null) {
			descontoNoMesContratoReceita = BigDecimal.ZERO;
		}
		return descontoNoMesContratoReceita;
	}

	public void setDescontoNoMesContratoReceita(BigDecimal descontoNoMesContratoReceita) {
		this.descontoNoMesContratoReceita = descontoNoMesContratoReceita;
	}

	public BigDecimal getDescontoNoMesInclusaoReposicao() {
		if (descontoNoMesInclusaoReposicao == null) {
			descontoNoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return descontoNoMesInclusaoReposicao;
	}

	public void setDescontoNoMesInclusaoReposicao(BigDecimal descontoNoMesInclusaoReposicao) {
		this.descontoNoMesInclusaoReposicao = descontoNoMesInclusaoReposicao;
	}

	public BigDecimal getDescontoNoMesOutros() {
		if (descontoNoMesOutros == null) {
			descontoNoMesOutros = BigDecimal.ZERO;
		}
		return descontoNoMesOutros;
	}

	public void setDescontoNoMesOutros(BigDecimal descontoNoMesOutros) {
		this.descontoNoMesOutros = descontoNoMesOutros;
	}

	public BigDecimal getAcrescimoNoMesMatricula() {
		if (acrescimoNoMesMatricula == null) {
			acrescimoNoMesMatricula = BigDecimal.ZERO;
		}
		return acrescimoNoMesMatricula;
	}

	public void setAcrescimoNoMesMatricula(BigDecimal acrescimoNoMesMatricula) {
		this.acrescimoNoMesMatricula = acrescimoNoMesMatricula;
	}

	public BigDecimal getAcrescimoNoMesMensalidade() {
		if (acrescimoNoMesMensalidade == null) {
			acrescimoNoMesMensalidade = BigDecimal.ZERO;
		}
		return acrescimoNoMesMensalidade;
	}

	public void setAcrescimoNoMesMensalidade(BigDecimal acrescimoNoMesMensalidade) {
		this.acrescimoNoMesMensalidade = acrescimoNoMesMensalidade;
	}

	public BigDecimal getAcrescimoNoMesRequerimento() {
		if (acrescimoNoMesRequerimento == null) {
			acrescimoNoMesRequerimento = BigDecimal.ZERO;
		}
		return acrescimoNoMesRequerimento;
	}

	public void setAcrescimoNoMesRequerimento(BigDecimal acrescimoNoMesRequerimento) {
		this.acrescimoNoMesRequerimento = acrescimoNoMesRequerimento;
	}

	public BigDecimal getAcrescimoNoMesBiblioteca() {
		if (acrescimoNoMesBiblioteca == null) {
			acrescimoNoMesBiblioteca = BigDecimal.ZERO;
		}
		return acrescimoNoMesBiblioteca;
	}

	public void setAcrescimoNoMesBiblioteca(BigDecimal acrescimoNoMesBiblioteca) {
		this.acrescimoNoMesBiblioteca = acrescimoNoMesBiblioteca;
	}

	public BigDecimal getAcrescimoNoMesDevolucaoCheque() {
		if (acrescimoNoMesDevolucaoCheque == null) {
			acrescimoNoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return acrescimoNoMesDevolucaoCheque;
	}

	public void setAcrescimoNoMesDevolucaoCheque(BigDecimal acrescimoNoMesDevolucaoCheque) {
		this.acrescimoNoMesDevolucaoCheque = acrescimoNoMesDevolucaoCheque;
	}

	public BigDecimal getAcrescimoNoMesNegociacao() {
		if (acrescimoNoMesNegociacao == null) {
			acrescimoNoMesNegociacao = BigDecimal.ZERO;
		}
		return acrescimoNoMesNegociacao;
	}

	public void setAcrescimoNoMesNegociacao(BigDecimal acrescimoNoMesNegociacao) {
		this.acrescimoNoMesNegociacao = acrescimoNoMesNegociacao;
	}

	public BigDecimal getAcrescimoNoMesBolsaCusteada() {
		if (acrescimoNoMesBolsaCusteada == null) {
			acrescimoNoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return acrescimoNoMesBolsaCusteada;
	}

	public void setAcrescimoNoMesBolsaCusteada(BigDecimal acrescimoNoMesBolsaCusteada) {
		this.acrescimoNoMesBolsaCusteada = acrescimoNoMesBolsaCusteada;
	}

	public BigDecimal getAcrescimoNoMesInscricao() {
		if (acrescimoNoMesInscricao == null) {
			acrescimoNoMesInscricao = BigDecimal.ZERO;
		}
		return acrescimoNoMesInscricao;
	}

	public void setAcrescimoNoMesInscricao(BigDecimal acrescimoNoMesInscricao) {
		this.acrescimoNoMesInscricao = acrescimoNoMesInscricao;
	}

	public BigDecimal getAcrescimoNoMesContratoReceita() {
		if (acrescimoNoMesContratoReceita == null) {
			acrescimoNoMesContratoReceita = BigDecimal.ZERO;
		}
		return acrescimoNoMesContratoReceita;
	}

	public void setAcrescimoNoMesContratoReceita(BigDecimal acrescimoNoMesContratoReceita) {
		this.acrescimoNoMesContratoReceita = acrescimoNoMesContratoReceita;
	}

	public BigDecimal getAcrescimoNoMesInclusaoReposicao() {
		if (acrescimoNoMesInclusaoReposicao == null) {
			acrescimoNoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return acrescimoNoMesInclusaoReposicao;
	}

	public void setAcrescimoNoMesInclusaoReposicao(BigDecimal acrescimoNoMesInclusaoReposicao) {
		this.acrescimoNoMesInclusaoReposicao = acrescimoNoMesInclusaoReposicao;
	}

	public BigDecimal getAcrescimoNoMesOutros() {
		if (acrescimoNoMesOutros == null) {
			acrescimoNoMesOutros = BigDecimal.ZERO;
		}
		return acrescimoNoMesOutros;
	}

	public void setAcrescimoNoMesOutros(BigDecimal acrescimoNoMesOutros) {
		this.acrescimoNoMesOutros = acrescimoNoMesOutros;
	}

	public BigDecimal getTotalDescontoConvenioDoMes() {
		if (totalDescontoConvenioDoMes == null) {
			totalDescontoConvenioDoMes = BigDecimal.ZERO;
		}
		return totalDescontoConvenioDoMes;
	}

	public void setTotalDescontoConvenioDoMes(BigDecimal totalDescontoConvenioDoMes) {
		this.totalDescontoConvenioDoMes = totalDescontoConvenioDoMes;
	}

	public BigDecimal getTotalDescontoInstituicaoDoMes() {
		if (totalDescontoInstituicaoDoMes == null) {
			totalDescontoInstituicaoDoMes = BigDecimal.ZERO;
		}
		return totalDescontoInstituicaoDoMes;
	}

	public void setTotalDescontoInstituicaoDoMes(BigDecimal totalDescontoInstituicaoDoMes) {
		this.totalDescontoInstituicaoDoMes = totalDescontoInstituicaoDoMes;
	}

	public BigDecimal getTotalDescontoProgressivoDoMes() {
		if (totalDescontoProgressivoDoMes == null) {
			totalDescontoProgressivoDoMes = BigDecimal.ZERO;
		}
		return totalDescontoProgressivoDoMes;
	}

	public void setTotalDescontoProgressivoDoMes(BigDecimal totalDescontoProgressivoDoMes) {
		this.totalDescontoProgressivoDoMes = totalDescontoProgressivoDoMes;
	}

	public BigDecimal getTotalDescontoAlunoDoMes() {
		if (totalDescontoAlunoDoMes == null) {
			totalDescontoAlunoDoMes = BigDecimal.ZERO;
		}
		return totalDescontoAlunoDoMes;
	}

	public void setTotalDescontoAlunoDoMes(BigDecimal totalDescontoAlunoDoMes) {
		this.totalDescontoAlunoDoMes = totalDescontoAlunoDoMes;
	}

	public BigDecimal getTotalDescontoRecebimentoDoMes() {
		if (totalDescontoRecebimentoDoMes == null) {
			totalDescontoRecebimentoDoMes = BigDecimal.ZERO;
		}
		return totalDescontoRecebimentoDoMes;
	}

	public void setTotalDescontoRecebimentoDoMes(BigDecimal totalDescontoRecebimentoDoMes) {
		this.totalDescontoRecebimentoDoMes = totalDescontoRecebimentoDoMes;
	}

	public BigDecimal getTotalDescontoConvenioNoMes() {
		if (totalDescontoConvenioNoMes == null) {
			totalDescontoConvenioNoMes = BigDecimal.ZERO;
		}
		return totalDescontoConvenioNoMes;
	}

	public void setTotalDescontoConvenioNoMes(BigDecimal totalDescontoConvenioNoMes) {
		this.totalDescontoConvenioNoMes = totalDescontoConvenioNoMes;
	}

	public BigDecimal getTotalDescontoInstituicaoNoMes() {
		if (totalDescontoInstituicaoNoMes == null) {
			totalDescontoInstituicaoNoMes = BigDecimal.ZERO;
		}
		return totalDescontoInstituicaoNoMes;
	}

	public void setTotalDescontoInstituicaoNoMes(BigDecimal totalDescontoInstituicaoNoMes) {
		this.totalDescontoInstituicaoNoMes = totalDescontoInstituicaoNoMes;
	}

	public BigDecimal getTotalDescontoProgressivoNoMes() {
		if (totalDescontoProgressivoNoMes == null) {
			totalDescontoProgressivoNoMes = BigDecimal.ZERO;
		}
		return totalDescontoProgressivoNoMes;
	}

	public void setTotalDescontoProgressivoNoMes(BigDecimal totalDescontoProgressivoNoMes) {
		this.totalDescontoProgressivoNoMes = totalDescontoProgressivoNoMes;
	}

	public BigDecimal getTotalDescontoAlunoNoMes() {
		if (totalDescontoAlunoNoMes == null) {
			totalDescontoAlunoNoMes = BigDecimal.ZERO;
		}
		return totalDescontoAlunoNoMes;
	}

	public void setTotalDescontoAlunoNoMes(BigDecimal totalDescontoAlunoNoMes) {
		this.totalDescontoAlunoNoMes = totalDescontoAlunoNoMes;
	}

	public BigDecimal getTotalDescontoRecebimentoNoMes() {
		if (totalDescontoRecebimentoNoMes == null) {
			totalDescontoRecebimentoNoMes = BigDecimal.ZERO;
		}
		return totalDescontoRecebimentoNoMes;
	}

	public void setTotalDescontoRecebimentoNoMes(BigDecimal totalDescontoRecebimentoNoMes) {
		this.totalDescontoRecebimentoNoMes = totalDescontoRecebimentoNoMes;
	}

	public List<PainelGestorDetalhamentoDescontoVO> getPainelGestorDetalhamentoDescontoVOs() {
		if (painelGestorDetalhamentoDescontoVOs == null) {
			painelGestorDetalhamentoDescontoVOs = new ArrayList<PainelGestorDetalhamentoDescontoVO>(0);
		}
		return painelGestorDetalhamentoDescontoVOs;
	}

	public void setPainelGestorDetalhamentoDescontoVOs(List<PainelGestorDetalhamentoDescontoVO> painelGestorDetalhamentoDescontoVOs) {
		this.painelGestorDetalhamentoDescontoVOs = painelGestorDetalhamentoDescontoVOs;
	}

	public BigDecimal getTotalDescontoDetalhamento() {
		if (totalDescontoDetalhamento == null) {
			totalDescontoDetalhamento = BigDecimal.ZERO;
		}
		return totalDescontoDetalhamento;
	}

	public void setTotalDescontoDetalhamento(BigDecimal totalDescontoDetalhamento) {
		this.totalDescontoDetalhamento = totalDescontoDetalhamento;
	}

	public Integer getTotalNumeroAlunoComDesconto() {
		if (totalNumeroAlunoComDesconto == null) {
			totalNumeroAlunoComDesconto = 0;
		}
		return totalNumeroAlunoComDesconto;
	}

	public void setTotalNumeroAlunoComDesconto(Integer totalNumeroAlunoComDesconto) {
		this.totalNumeroAlunoComDesconto = totalNumeroAlunoComDesconto;
	}

	public Double getMediaInadimplenciaNoPeriodoSemAcrescimo() {
		if (mediaInadimplenciaNoPeriodoSemAcrescimo == null) {
			mediaInadimplenciaNoPeriodoSemAcrescimo = 0.0;
		}
		return mediaInadimplenciaNoPeriodoSemAcrescimo;
	}

	public void setMediaInadimplenciaNoPeriodoSemAcrescimo(Double mediaInadimplenciaNoPeriodoSemAcrescimo) {
		this.mediaInadimplenciaNoPeriodoSemAcrescimo = mediaInadimplenciaNoPeriodoSemAcrescimo;
	}

	public Double getMediaInadimplenciaDoPeriodoSemAcrescimo() {
		if (mediaInadimplenciaDoPeriodoSemAcrescimo == null) {
			mediaInadimplenciaDoPeriodoSemAcrescimo = 0.0;
		}
		return mediaInadimplenciaDoPeriodoSemAcrescimo;
	}

	public void setMediaInadimplenciaDoPeriodoSemAcrescimo(Double mediaInadimplenciaDoPeriodoSemAcrescimo) {
		this.mediaInadimplenciaDoPeriodoSemAcrescimo = mediaInadimplenciaDoPeriodoSemAcrescimo;
	}

	


	public Double getValorTotalPagarConsumo() {
		if(valorTotalPagarConsumo == null){
			valorTotalPagarConsumo = 0.0;
		}
		return valorTotalPagarConsumo;
	}

	public void setValorTotalPagarConsumo(Double valorTotalPagarConsumo) {
		this.valorTotalPagarConsumo = valorTotalPagarConsumo;
	}

	public String getListaCoresGraficoConsumo() {
		if(listaCoresGraficoConsumo == null){
			listaCoresGraficoConsumo = "";
		}
		return listaCoresGraficoConsumo;
	}

	public void setListaCoresGraficoConsumo(String listaCoresGraficoConsumo) {
		this.listaCoresGraficoConsumo = listaCoresGraficoConsumo;
	}

	public String getListaValoresConsumoInicial() {
		if(listaValoresConsumoInicial == null){
			listaValoresConsumoInicial = "";
		}
		return listaValoresConsumoInicial;
	}

	public void setListaValoresConsumoInicial(String listaValoresConsumoInicial) {
		this.listaValoresConsumoInicial = listaValoresConsumoInicial;
	}

	public String getListaValoresConsumo() {
		if(listaValoresConsumo == null){
			listaValoresConsumo = "";
		}
		return listaValoresConsumo;
	}

	public void setListaValoresConsumo(String listaValoresConsumo) {
		this.listaValoresConsumo = listaValoresConsumo;
	}

	public Integer getCodigoDepartamento() {
		if(codigoDepartamento == null){
			codigoDepartamento = 0;
		}
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(Integer codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getLegendaCategoriaPainelAcademicoNivelEducacional() {
		if (legendaCategoriaPainelAcademicoNivelEducacional == null) {
			legendaCategoriaPainelAcademicoNivelEducacional = "";
		}
		return legendaCategoriaPainelAcademicoNivelEducacional;
	}

	public void setLegendaCategoriaPainelAcademicoNivelEducacional(String legendaCategoriaPainelAcademicoNivelEducacional) {
		this.legendaCategoriaPainelAcademicoNivelEducacional = legendaCategoriaPainelAcademicoNivelEducacional;
	}
	

	public Double getTotalDescontoRateio() {
		if(totalDescontoRateio == null){
			totalDescontoRateio = 0.0;
		}
		return totalDescontoRateio;
	}

	public void setTotalDescontoRateio(Double totalDescontoRateio) {
		this.totalDescontoRateio = totalDescontoRateio;
	}

	public BigDecimal getTotalDescontoRateioDoMes() {
		if(totalDescontoRateioDoMes == null){
			totalDescontoRateioDoMes =BigDecimal.ZERO;
		}
		return totalDescontoRateioDoMes;
	}

	public void setTotalDescontoRateioDoMes(BigDecimal totalDescontoRateioDoMes) {
		this.totalDescontoRateioDoMes = totalDescontoRateioDoMes;
	}

	public BigDecimal getTotalDescontoRateioNoMes() {
		if(totalDescontoRateioNoMes == null){
			totalDescontoRateioNoMes = BigDecimal.ZERO;
		}
		return totalDescontoRateioNoMes;
	}

	public void setTotalDescontoRateioNoMes(BigDecimal totalDescontoRateioNoMes) {
		this.totalDescontoRateioNoMes = totalDescontoRateioNoMes;
	}

	public String getNomeDepartamento() {
		if (nomeDepartamento == null) {
			nomeDepartamento = "";
		}
		return nomeDepartamento;
	}

	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}
	
	
	
}
