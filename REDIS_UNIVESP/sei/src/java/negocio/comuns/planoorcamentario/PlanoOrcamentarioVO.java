package negocio.comuns.planoorcamentario;

/**
 * Reponsável por manter os dados da entidade Agencia. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar
 * e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

import controle.financeiro.RelatorioSEIDecidirControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public class PlanoOrcamentarioVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1641430371866125186L;

	protected Integer codigo;
	private String nome;
	private Date dataInicio;
	private Date dataFinal;
	private String situacao;
	private String objetivo;
	private Double receitaPeriodoAnterior;
	private Double receitaProvisionadaPeriodo;
	private Double receitaPrevistaPeriodo;
	private Double receitaTotalPrevista;
	private Double despesaPeriodoAnterior;
	private Double despesaProvisionadaPeriodo;
	private Double despesaPrevistaPeriodo;
	private Double despesaTotalPrevista;
	private Double crescimentoTurmasAtivas;
	private Double percentualCrescimentoGastos;
	private Integer turmasEncerrandoPeriodo;
	private Integer turmasNaoEncerramPeriodo;
	private Integer turmaPrevistasPeriodo;
	private Double orcamentoTotalPrevisto;
	private Double valorAlteracaoPrevistaPeriodo;
	private Double saldoOrcamentarioPlanejar;
	private Double orcamentoTotalRealizado;
	private Double lucro;
	private Double lucroPrevisto;
	private Double percentualCrescimentoReceita;
	private Double percentualCrescimentoDespesa;
	private Double saldo;
	private UsuarioVO responsavel;

	private DefaultCategoryDataset graficoPorDepartamento;
	private List<LegendaGraficoVO> legendaGraficoVOs;

	// Transiente
	private List<UnidadesPlanoOrcamentarioVO> listaUnidades;
	private List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamento;
	private List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOs;
	private List<RequisicaoItemVO> requisicaoItemVOs;

	 /**
     * Usado na classe {@link RelatorioSEIDecidirControle}
     */
	private Boolean filtrarPlanoOrcamentarioVO;

	public PlanoOrcamentarioVO() {
		super();
	}

	public static void validarDados(PlanoOrcamentarioVO obj, boolean permitirRealizarManejamentoSaldoAprovado) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME deve ser informado.");
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getListaUnidades())) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO deve ser informado.");			
		}
		
		if ( (Uteis.isAtributoPreenchido(obj.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()) && 
				!obj.getSolicitacaoOrcamentoPlanoOrcamentarioVOs().isEmpty()) && !permitirRealizarManejamentoSaldoAprovado) {
			for (SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO : obj.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
				if (solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado() > solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoOrcamentario_orcamentoAprovadoMairoRequeridoGestor"));
				}
			}
		}
	}

	public List<UnidadeEnsinoVO> obterListaUnidadeEnsino() {
		if (!getListaUnidades().isEmpty()) {
			List<UnidadeEnsinoVO> lista = new ArrayList<UnidadeEnsinoVO>();
			for (UnidadesPlanoOrcamentarioVO uni : getListaUnidades()) {
				lista.add(uni.getUnidadeEnsino());
			}
			return lista;
		} else {
			return new ArrayList<UnidadeEnsinoVO>();
		}
	}

	public void adicionarListaUnidades(List<UnidadeEnsinoVO> lista) {
		if (!lista.isEmpty()) {
			this.setListaUnidades(null);
			for (UnidadeEnsinoVO uni : lista) {
				UnidadesPlanoOrcamentarioVO unidades = new UnidadesPlanoOrcamentarioVO();
				unidades.setUnidadeEnsino(uni);
				getListaUnidades().add(unidades);
			}
		}
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		if (dataInicio == null) {
			try {
				String ano = Uteis.getAnoDataAtual();
				dataInicio = Uteis.getDate("01/01/" + ano);
			} catch (Exception e) {
				dataInicio = new Date();
			}
		}
		return dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFinal
	 */
	public Date getDataFinal() {
		if (dataFinal == null) {
			try {
				String ano = Uteis.getAnoDataAtual();
				dataFinal = Uteis.getDate("31/12/" + ano);
			} catch (Exception e) {
				dataFinal = new Date();
			}
		}
		return dataFinal;
	}

	/**
	 * @param dataFinal
	 *            the dataFinal to set
	 */
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	/**
	 * @return the listaUnidades
	 */
	public List<UnidadesPlanoOrcamentarioVO> getListaUnidades() {
		if (listaUnidades == null) {
			listaUnidades = new ArrayList<>();
		}
		return listaUnidades;
	}

	/**
	 * @param listaUnidades
	 *            the listaUnidades to set
	 */
	public void setListaUnidades(List<UnidadesPlanoOrcamentarioVO> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}

	/**
	 * @return the situacao
	 */
	public String getSituacao() {
		if (situacao == null) {
			situacao = "EM";
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {
		if (getSituacao().equals("EM")) {
			return "Em Construção";
		}
		if (getSituacao().equals("AT")) {
			return "Ativo";
		}
		if (getSituacao().equals("FI")) {
			return "Finalizado";
		}
		if (getSituacao().equals("AP")) {
			return "Aprovado";
		}
		if (getSituacao().equals("RE")) {
			return "Revisão";
		}
		return "";
	}

	/**
	 * @param situacao
	 *            the situacao to set
	 */
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	/**
	 * @return the objetivo
	 */
	public String getObjetivo() {
		if (objetivo == null) {
			objetivo = "";
		}
		return objetivo;
	}

	/**
	 * @param objetivo
	 *            the objetivo to set
	 */
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	/**
	 * @return the receitaPeriodoAnterior
	 */
	public Double getReceitaPeriodoAnterior() {
		if (receitaPeriodoAnterior == null) {
			receitaPeriodoAnterior = 0.0;
		}
		return receitaPeriodoAnterior;
	}

	/**
	 * @param receitaPeriodoAnterior
	 *            the receitaPeriodoAnterior to set
	 */
	public void setReceitaPeriodoAnterior(Double receitaPeriodoAnterior) {
		this.receitaPeriodoAnterior = receitaPeriodoAnterior;
	}

	/**
	 * @return the receitaProvisionadaPeriodo
	 */
	public Double getReceitaProvisionadaPeriodo() {
		if (receitaProvisionadaPeriodo == null) {
			receitaProvisionadaPeriodo = 0.0;
		}
		return receitaProvisionadaPeriodo;
	}

	/**
	 * @param receitaProvisionadaPeriodo
	 *            the receitaProvisionadaPeriodo to set
	 */
	public void setReceitaProvisionadaPeriodo(Double receitaProvisionadaPeriodo) {
		this.receitaProvisionadaPeriodo = receitaProvisionadaPeriodo;
	}

	/**
	 * @return the receitaPrevistaPeriodo
	 */
	public Double getReceitaPrevistaPeriodo() {
		if (receitaPrevistaPeriodo == null) {
			receitaPrevistaPeriodo = 0.0;
		}
		return receitaPrevistaPeriodo;
	}

	/**
	 * @param receitaPrevistaPeriodo
	 *            the receitaPrevistaPeriodo to set
	 */
	public void setReceitaPrevistaPeriodo(Double receitaPrevistaPeriodo) {
		this.receitaPrevistaPeriodo = receitaPrevistaPeriodo;
	}

	/**
	 * @return the receitaTotalPrevista
	 */
	public Double getReceitaTotalPrevista() {
		if (receitaTotalPrevista == null) {
			receitaTotalPrevista = 0.0;
		}
		return receitaTotalPrevista;
	}

	/**
	 * @param receitaTotalPrevista
	 *            the receitaTotalPrevista to set
	 */
	public void setReceitaTotalPrevista(Double receitaTotalPrevista) {
		this.receitaTotalPrevista = receitaTotalPrevista;
	}

	/**
	 * @return the despesaPeriodoAnterior
	 */
	public Double getDespesaPeriodoAnterior() {
		if (despesaPeriodoAnterior == null) {
			despesaPeriodoAnterior = 0.0;
		}
		return despesaPeriodoAnterior;
	}

	/**
	 * @param despesaPeriodoAnterior
	 *            the despesaPeriodoAnterior to set
	 */
	public void setDespesaPeriodoAnterior(Double despesaPeriodoAnterior) {
		this.despesaPeriodoAnterior = despesaPeriodoAnterior;
	}

	/**
	 * @return the despesaProvisionadaPeriodo
	 */
	public Double getDespesaProvisionadaPeriodo() {
		if (despesaProvisionadaPeriodo == null) {
			despesaProvisionadaPeriodo = 0.0;
		}
		return despesaProvisionadaPeriodo;
	}

	/**
	 * @param despesaProvisionadaPeriodo
	 *            the despesaProvisionadaPeriodo to set
	 */
	public void setDespesaProvisionadaPeriodo(Double despesaProvisionadaPeriodo) {
		this.despesaProvisionadaPeriodo = despesaProvisionadaPeriodo;
	}

	/**
	 * @return the despesaPrevistaPeriodo
	 */
	public Double getDespesaPrevistaPeriodo() {
		if (despesaPrevistaPeriodo == null) {
			despesaPrevistaPeriodo = 0.0;
		}
		return despesaPrevistaPeriodo;
	}

	/**
	 * @param despesaPrevistaPeriodo
	 *            the despesaPrevistaPeriodo to set
	 */
	public void setDespesaPrevistaPeriodo(Double despesaPrevistaPeriodo) {
		this.despesaPrevistaPeriodo = despesaPrevistaPeriodo;
	}

	/**
	 * @return the despesaTotalPrevista
	 */
	public Double getDespesaTotalPrevista() {
		if (despesaTotalPrevista == null) {
			despesaTotalPrevista = 0.0;
		}
		return despesaTotalPrevista;
	}

	/**
	 * @param despesaTotalPrevista
	 *            the despesaTotalPrevista to set
	 */
	public void setDespesaTotalPrevista(Double despesaTotalPrevista) {
		this.despesaTotalPrevista = despesaTotalPrevista;
	}

	/**
	 * @return the crescimentoTurmasAtivas
	 */
	public Double getCrescimentoTurmasAtivas() {
		if (crescimentoTurmasAtivas == null) {
			return 0.0;
		}
		if (getTurmaPrevistasPeriodo().intValue() == 0) {
			return 0.0;
		}
		if (getTurmasEncerrandoPeriodo().intValue() == 0) {
			return 0.0;
		}
		Integer valor = getTurmaPrevistasPeriodo().intValue() * 100;
		Double valorDouble = new Double((valor) / getTurmasEncerrandoPeriodo().intValue());
		Double valorFinal = valorDouble - 100;
		return valorFinal;

		// return crescimentoTurmasAtivas;
	}

	/**
	 * @param crescimentoTurmasAtivas
	 *            the crescimentoTurmasAtivas to set
	 */
	public void setCrescimentoTurmasAtivas(Double crescimentoTurmasAtivas) {
		this.crescimentoTurmasAtivas = crescimentoTurmasAtivas;
	}

	/**
	 * @return the percentualCrescimentoGastos
	 */
	public void calcularValorPercentualCrescimentoGastos() {
		if (percentualCrescimentoGastos == null) {
			setOrcamentoTotalPrevisto(0.0);
		}
		Double valor = getDespesaTotalPrevista().doubleValue() * getPercentualCrescimentoGastos().doubleValue();
		Double valorFinal = (valor / 100) + getDespesaTotalPrevista().doubleValue();
		setOrcamentoTotalPrevisto(valorFinal);
	}

	public Double getPercentualCrescimentoGastos() {
		if (percentualCrescimentoGastos == null) {
			return 0.0;
		}
		return percentualCrescimentoGastos;
	}

	/**
	 * @param percentualCrescimentoGastos
	 *            the percentualCrescimentoGastos to set
	 */
	public void setPercentualCrescimentoGastos(Double percentualCrescimentoGastos) {
		this.percentualCrescimentoGastos = percentualCrescimentoGastos;
	}

	/**
	 * @return the turmasEncerrandoPeriodo
	 */
	public Integer getTurmasEncerrandoPeriodo() {
		if (turmasEncerrandoPeriodo == null) {
			turmasEncerrandoPeriodo = 0;
		}
		return turmasEncerrandoPeriodo;
	}

	/**
	 * @param turmasEncerrandoPeriodo
	 *            the turmasEncerrandoPeriodo to set
	 */
	public void setTurmasEncerrandoPeriodo(Integer turmasEncerrandoPeriodo) {
		this.turmasEncerrandoPeriodo = turmasEncerrandoPeriodo;
	}

	/**
	 * @return the turmasNaoEncerramPeriodo
	 */
	public Integer getTurmasNaoEncerramPeriodo() {
		if (turmasNaoEncerramPeriodo == null) {
			turmasNaoEncerramPeriodo = 0;
		}
		return turmasNaoEncerramPeriodo;
	}

	/**
	 * @param turmasNaoEncerramPeriodo
	 *            the turmasNaoEncerramPeriodo to set
	 */
	public void setTurmasNaoEncerramPeriodo(Integer turmasNaoEncerramPeriodo) {
		this.turmasNaoEncerramPeriodo = turmasNaoEncerramPeriodo;
	}

	/**
	 * @return the turmaPrevistasPeriodo
	 */
	public Integer getTurmaPrevistasPeriodo() {
		if (turmaPrevistasPeriodo == null) {
			turmaPrevistasPeriodo = 0;
		}
		return turmaPrevistasPeriodo;
	}

	/**
	 * @param turmaPrevistasPeriodo
	 *            the turmaPrevistasPeriodo to set
	 */
	public void setTurmaPrevistasPeriodo(Integer turmaPrevistasPeriodo) {
		this.turmaPrevistasPeriodo = turmaPrevistasPeriodo;
	}

	/**
	 * @return the orcamentoTotalPrevisto
	 */
	public Double getOrcamentoTotalPrevisto() {
		if (orcamentoTotalPrevisto == null) {
			orcamentoTotalPrevisto = 0.0;
		}
		return orcamentoTotalPrevisto;
	}

	/**
	 * @param orcamentoTotalPrevisto
	 *            the orcamentoTotalPrevisto to set
	 */
	public void setOrcamentoTotalPrevisto(Double orcamentoTotalPrevisto) {
		this.orcamentoTotalPrevisto = orcamentoTotalPrevisto;
	}

	/**
	 * @return the valorAlteracaoPrevistaPeriodo
	 */
	public Double getValorAlteracaoPrevistaPeriodo() {
		if (valorAlteracaoPrevistaPeriodo == null) {
			valorAlteracaoPrevistaPeriodo = 0.0;
		}
		return valorAlteracaoPrevistaPeriodo;
	}

	/**
	 * @param valorAlteracaoPrevistaPeriodo
	 *            the valorAlteracaoPrevistaPeriodo to set
	 */
	public void setValorAlteracaoPrevistaPeriodo(Double valorAlteracaoPrevistaPeriodo) {
		this.valorAlteracaoPrevistaPeriodo = valorAlteracaoPrevistaPeriodo;
	}

	/**
	 * @return the saldoOrcamentarioPlanejar
	 */
	public Double getSaldoOrcamentarioPlanejar() {
		if (saldoOrcamentarioPlanejar == null) {
			saldoOrcamentarioPlanejar = 0.0;
		}
		return saldoOrcamentarioPlanejar;
	}

	/**
	 * @param saldoOrcamentarioPlanejar
	 *            the saldoOrcamentarioPlanejar to set
	 */
	public void setSaldoOrcamentarioPlanejar(Double saldoOrcamentarioPlanejar) {
		this.saldoOrcamentarioPlanejar = saldoOrcamentarioPlanejar;
	}

	/**
	 * @return the orcamentoTotalRealizado
	 */
	public Double getOrcamentoTotalRealizado() {
		if (orcamentoTotalRealizado == null) {
			orcamentoTotalRealizado = 0.0;
		}
		return orcamentoTotalRealizado;
	}

	/**
	 * @param orcamentoTotalRealizado
	 *            the orcamentoTotalRealizado to set
	 */
	public void setOrcamentoTotalRealizado(Double orcamentoTotalRealizado) {
		this.orcamentoTotalRealizado = orcamentoTotalRealizado;
	}

	/**
	 * @return the lucro
	 */
	public Double getLucro() {
		if (lucro == null) {
			lucro = 0.0;
		}
		return lucro;
	}

	/**
	 * @param lucro
	 *            the lucro to set
	 */
	public void setLucro(Double lucro) {
		this.lucro = lucro;
	}

	/**
	 * @return the lucroPrevisto
	 */
	public Double getLucroPrevisto() {
		if (lucroPrevisto == null) {
			lucroPrevisto = 0.0;
		}
		return lucroPrevisto;
	}

	/**
	 * @param lucroPrevisto
	 *            the lucroPrevisto to set
	 */
	public void setLucroPrevisto(Double lucroPrevisto) {
		this.lucroPrevisto = lucroPrevisto;
	}

	/**
	 * @return the percentualCrescimentoReceita
	 */
	public Double getPercentualCrescimentoReceita() {
		if (percentualCrescimentoReceita == null) {
			percentualCrescimentoReceita = 0.0;
		}
		return percentualCrescimentoReceita;
	}

	/**
	 * @param percentualCrescimentoReceita
	 *            the percentualCrescimentoReceita to set
	 */
	public void setPercentualCrescimentoReceita(Double percentualCrescimentoReceita) {
		this.percentualCrescimentoReceita = percentualCrescimentoReceita;
	}

	/**
	 * @return the percentualCrescimentoDespesa
	 */
	public Double getPercentualCrescimentoDespesa() {
		if (percentualCrescimentoDespesa == null) {
			percentualCrescimentoDespesa = 0.0;
		}
		return percentualCrescimentoDespesa;
	}

	/**
	 * @param percentualCrescimentoDespesa
	 *            the percentualCrescimentoDespesa to set
	 */
	public void setPercentualCrescimentoDespesa(Double percentualCrescimentoDespesa) {
		this.percentualCrescimentoDespesa = percentualCrescimentoDespesa;
	}

	public void finalizarPlanoOrcamentario() {
		this.setSituacao("FI");
	}

	public void ativarPlanoOrcamentario() {
		this.setSituacao("AT");
	}

	public void voltarParaConstrucao() {
		this.setSituacao("EM");
	}

	/**
	 * @return the detalhamentoPeriodoOrcamento
	 */
	public List<DetalhamentoPeriodoOrcamentoVO> getDetalhamentoPeriodoOrcamento() {
		if (detalhamentoPeriodoOrcamento == null) {
			detalhamentoPeriodoOrcamento = new ArrayList<DetalhamentoPeriodoOrcamentoVO>();
		}
		return detalhamentoPeriodoOrcamento;
	}

	/**
	 * @param detalhamentoPeriodoOrcamento
	 *            the detalhamentoPeriodoOrcamento to set
	 */
	public void setDetalhamentoPeriodoOrcamento(List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamento) {
		this.detalhamentoPeriodoOrcamento = detalhamentoPeriodoOrcamento;
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

	public Double getSaldo() {
		if (saldo == null) {
			saldo = 0.0;
		}
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public DefaultCategoryDataset getGraficoPorDepartamento() {
		if (graficoPorDepartamento == null) {
			graficoPorDepartamento = new DefaultCategoryDataset();
		}
		return graficoPorDepartamento;
	}

	public void setGraficoPorDepartamento(DefaultCategoryDataset graficoPorDepartamento) {
		this.graficoPorDepartamento = graficoPorDepartamento;
	}

	public List<LegendaGraficoVO> getLegendaGraficoVOs() {
		if (legendaGraficoVOs == null) {
			legendaGraficoVOs = new ArrayList<LegendaGraficoVO>(0);
		}
		return legendaGraficoVOs;
	}

	public void setLegendaGraficoVOs(List<LegendaGraficoVO> legendaGraficoVOs) {
		this.legendaGraficoVOs = legendaGraficoVOs;
	}

	public Boolean getFiltrarPlanoOrcamentarioVO() {
		if (filtrarPlanoOrcamentarioVO == null) {
			filtrarPlanoOrcamentarioVO = Boolean.FALSE;
		}
		return filtrarPlanoOrcamentarioVO;
	}

	public void setFiltrarPlanoOrcamentarioVO(Boolean filtrarPlanoOrcamentarioVO) {
		this.filtrarPlanoOrcamentarioVO = filtrarPlanoOrcamentarioVO;
	}

	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> getSolicitacaoOrcamentoPlanoOrcamentarioVOs() {
		if(solicitacaoOrcamentoPlanoOrcamentarioVOs == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVOs = new ArrayList<SolicitacaoOrcamentoPlanoOrcamentarioVO>(0);
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVOs;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVOs(
			List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOs) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVOs = solicitacaoOrcamentoPlanoOrcamentarioVOs;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public List<RequisicaoItemVO> getRequisicaoItemVOs() {
		if (requisicaoItemVOs == null) {
			requisicaoItemVOs = new ArrayList<>(0);
		}
		return requisicaoItemVOs;
	}

	public void setRequisicaoItemVOs(List<RequisicaoItemVO> requisicaoItemVOs) {
		this.requisicaoItemVOs = requisicaoItemVOs;
	}
	
	private Double valorTotalAprovado;
	private Double valorTotalConsumido;
	private Double valorTotalPendente;

	public Double getValorTotalAprovado() {
		return valorTotalAprovado;
	}

	public void setValorTotalAprovado(Double valorTotalAprovado) {
		this.valorTotalAprovado = valorTotalAprovado;
	}

	public Double getValorTotalConsumido() {
		return valorTotalConsumido;
	}

	public void setValorTotalConsumido(Double valorTotalConsumido) {
		this.valorTotalConsumido = valorTotalConsumido;
	}

	public Double getValorTotalPendente() {
		return valorTotalPendente;
	}

	public void setValorTotalPendente(Double valorTotalPendente) {
		this.valorTotalPendente = valorTotalPendente;
	}
	
	
}
