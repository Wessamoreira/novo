package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaEventoFolhaEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisCalculoFolhaPagamento;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public class CalculoContraCheque extends SuperFacadeJDBC{
	
	private static final long serialVersionUID = 5903343279755941159L;

	private FuncionarioCargoVO funcionarioCargoVO;
	
	// IRRF
	private BigDecimal baseCalculoIRRF;
	private BigDecimal incideIRRF;
	private BigDecimal dedutivelIRRF;
	private BigDecimal estornaIRRF;
	
	private Integer numerosDependentesIrrf;
	private Integer numerosDependentesSalFamilia;
	private BigDecimal valorDependente;
	private BigDecimal valorIRRF;
	private BigDecimal percentualIRRF;

	// Previdencia
	private BigDecimal baseCalculoPrevidencia;
	private BigDecimal incideINSS;
	private BigDecimal estornaINSS;
	private BigDecimal incidePrevidenciaPropria;
	private BigDecimal estornaPrevidenciaPropria;
	private BigDecimal incidePrevidenciaPropriaObrigatoria;
	private BigDecimal estornaPrevidenciaPropriaObrigatoria;
	
	private BigDecimal incideINSSFerias;
	private BigDecimal incidePrevidenciaPropriaFerias;
	private BigDecimal incidePrevidenciaPropriaObrigatoriaFerias;
	
	//Outros
	private BigDecimal fgts;
	private BigDecimal dsr;
	private BigDecimal planoSaude;
	private BigDecimal salarioFamilia;
	private BigDecimal informeRendimento;
	private BigDecimal rais;
	
	private ValorReferenciaFolhaPagamentoVO valorReferencia;

	private BigDecimal provento;
	private BigDecimal desconto;
	private BigDecimal baseCalculo;

	private BigDecimal incideValeTransporte;

	private BigDecimal incideAdicionalTempoServico;
	private BigDecimal incideAssociacaoSindicato;
	private BigDecimal incideAssociacaoSindicatoFerias;
	private BigDecimal incideAdicionalTempoServicoFerias;
	private BigDecimal incideAdicionalTempoServicoRescisao;

	Map<String, Object> eventos;

	//Valor cadastrado na Marcacao de Ferias
	private Integer numeroDiasFerias;
	private Integer numeroDiasAbono;

	// IRRF
	private BigDecimal baseCalculoIRRFFerias;
	private BigDecimal valorIRRFFerias;

	//Provento Decimo Terceiro
	private BigDecimal incideINSSDecimoTerceiro;
	private BigDecimal incideIRRFDecimoTerceiro;
	private BigDecimal incideFGTSDecimoTerceiro;
	private BigDecimal incidePrevidenciaPropriaDecimoTerceiro;
	private BigDecimal incidePlanoSaudeDecimoTerceiro;
	private BigDecimal incideAdicionalTempoServicoDecimoTerceiro;
	private BigDecimal incidePrevidenciaObrigatoriaDecimoTerceiro;
	private BigDecimal incideAssociacaoSindicatoDecimoTerceiro;

	//Desconto Decimo Terceiro
	private BigDecimal incideEstornoINSSDecimoTerceiro;
	private BigDecimal incideEstornoFGTSDecimoTerceiro;
	private BigDecimal incideEstornoIRRFDecimoTerceiro;
	private BigDecimal incideEstornoDedutivelIRRFDecimoTerceiro;
	
	private Integer anoCompetencia;
	private Integer mesCompetencia;
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO;

	private Integer numeroDiasTrabalhados;
	
	private BigDecimal adicionalFerias;
	
	//Campo que vai receber o evento de IRRF que ja foi lancado
	private BigDecimal valorIRRFJaLancado;
	
	private Integer avos13;
	private Integer avosFeriasProporcionais;
	
	private String referenciaEvento;
	
	private BigDecimal incidePlanoSaudeFerias;
	
	//Valor calculado de acordo com a data de inicio ou final do gozo
	private Integer diasFeriasMes;
	
	//Transiente
	private ContraChequeVO contraChequeVO;
	private CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO;
	private String sql;
	
	public CalculoContraCheque () {
		super();
	}

	public CalculoContraCheque(FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		super();
	
		try {
			List<String> identificadores = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarListaDeIdentificadoresAtivo();
			BigDecimal vlrDependente = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorFixoPorReferencia(ValorFixoEnum.VALOR_DEDUZIR_DEPENDENTE_IRRF, new Date());
			ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorReferenciaPorReferencia(ValorFixoEnum.IRRF.name(), new Date());
			
			Integer qtdDependentesIRRF = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarQuantidadeDependentesDoFuncinarioNoIRRF(funcionarioCargoVO.getFuncionarioVO().getCodigo());
			Integer qtdDependentesSalarioFamilia = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarQuantidadeDependentesDoFuncinarioNoSalarioFamilia(funcionarioCargoVO.getFuncionarioVO().getCodigo());

			setFuncionarioCargoVO(funcionarioCargoVO);
			setValorDependente(vlrDependente);
			setValorReferencia(valorReferenciaIRRF);
			setNumerosDependentesIrrf(qtdDependentesIRRF);
			setNumerosDependentesSalFamilia(qtdDependentesSalarioFamilia);
			calcularNumeroDiasTrabalhados();
			
			inicializarMapaDeEventos(identificadores);
		} catch (Exception e) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CalculoContraChequeNaoInformados"));
		}
	}

	/**
	 * Calculas as incidencias de Decimo terceiro do {@link EventoFolhaPagamentoVO}
	 * 
	 * @param evento
	 */
	public void calcularIncidenciasFolhaNormal(EventoFolhaPagamentoVO evento) {
		BigDecimal valorDoEvento = evento.getValorTemporario();

		if(evento.getIrrfFolhaNormal()) {
			setIncideIRRF(getIncideIRRF().add(valorDoEvento));
		}

		if(Uteis.isAtributoPreenchido(evento.getCategoria()) && evento.getCategoria().equals(CategoriaEventoFolhaEnum.IRRF)) {
			setValorIRRF(getValorIRRF().add(valorDoEvento));
		}

		if(evento.getDedutivelIrrf()) {
			//setDedutivelIRRF(getDedutivelIRRF().add(valorDoEvento));
			setIncideIRRF(getIncideIRRF().subtract(valorDoEvento));
		}

		if(evento.getEstornaIrrf()) {
			setBaseCalculoIRRF(getEstornaIRRF().add(valorDoEvento));
		}

		if(evento.getInssFolhaNormal()) {
			setIncideINSS(getIncideINSS().add(valorDoEvento));
		}
		
		if(evento.getEstornaInss()) {
			setEstornaINSS(getEstornaINSS().add(valorDoEvento));
		}
		
		if(evento.getFgtsFolhaNormal()) {
			setFgts(getFgts().add(valorDoEvento));
		}

		if(evento.getDsrFolhaNormal()) {
			setDsr(getDsr().add(valorDoEvento));
		}
		
		if(evento.getPlanoSaude()) {
			setPlanoSaude(getPlanoSaude().add(valorDoEvento));
		}
		
		if(evento.getSalarioFamiliaFolhaNormal()) {
			setSalarioFamilia(getSalarioFamilia().add(valorDoEvento));
		}
		
		if(evento.getPrevidenciaPropria()) {
			setIncidePrevidenciaPropria(getIncidePrevidenciaPropria().add(valorDoEvento));
		}
		
		if(evento.getEstornaPrevidenciaPropria()) {
			setEstornaPrevidenciaPropria(getEstornaPrevidenciaPropria().add(valorDoEvento));
		}
		
		if(evento.getPrevidenciaObrigatoria()) {
			setIncidePrevidenciaPropriaObrigatoria(getIncidePrevidenciaPropriaObrigatoria().add(valorDoEvento));
		}
		
		if(evento.getEstornaPrevidenciaPropriaObrigatoria()) {
			setEstornaPrevidenciaPropriaObrigatoria(getEstornaPrevidenciaPropriaObrigatoria().add(valorDoEvento));
		}
		
		if(evento.getInformeRendimento()) {
			setInformeRendimento(getInformeRendimento().add(valorDoEvento));
		}
		
		if(evento.getRais()) {
			setRais(getRais().add(valorDoEvento));
		}
		
		if(evento.getValeTransporte()) {
			setIncideValeTransporte(getIncideValeTransporte().add(valorDoEvento));
		}

		if(evento.getValeTransporteDesconto()) { 
			setIncideValeTransporte(getIncideValeTransporte().subtract(valorDoEvento));
		}
	
		if(evento.getIncideAdicionalTempoServico()) {
			setIncideAdicionalTempoServico(getIncideAdicionalTempoServico().add(valorDoEvento));
		}
		
		if(evento.getIncideAssociacaoSindicato()) {
			setIncideAssociacaoSindicato(getIncideAssociacaoSindicato().add(valorDoEvento));
		}
	}

	/**
	 * Calculas as incidencias de Ferias do {@link EventoFolhaPagamentoVO}}
	 * 
	 * @param evento
	 */
	public void calcularIncidenciasFolhaFerias(EventoFolhaPagamentoVO evento) {
		BigDecimal valorDoEvento = evento.getValorTemporario();

		if(evento.getIrrfFerias())
			setBaseCalculoIRRFFerias(getBaseCalculoIRRFFerias().add(valorDoEvento));
		
		if(Uteis.isAtributoPreenchido(evento.getCategoria()) && evento.getCategoria().equals(CategoriaEventoFolhaEnum.IRRF))
			setValorIRRFFerias(getValorIRRFFerias().add(valorDoEvento));
		
		if(evento.getDedutivelIrrfFerias())
			setBaseCalculoIRRFFerias(getBaseCalculoIRRFFerias().subtract(valorDoEvento));
		
		if (evento.getAdicionalFerias())
			setAdicionalFerias(getAdicionalFerias().add(valorDoEvento));
		
		if (evento.getIncideAdicionalTempoServicoFerias())
			setIncideAdicionalTempoServicoFerias(getIncideAdicionalTempoServicoFerias().add(valorDoEvento));
		
		if (evento.getInssFerias())
			setIncideINSSFerias(getIncideINSSFerias().add(valorDoEvento));
		
		if (evento.getPrevidenciaPropriaFerias())
			setIncidePrevidenciaPropriaFerias(getIncidePrevidenciaPropriaFerias().add(valorDoEvento));
		
		if (evento.getPrevidenciaObrigatoriaFerias())
			setIncidePrevidenciaPropriaObrigatoriaFerias(getIncidePrevidenciaPropriaObrigatoriaFerias().add(valorDoEvento));
		
		if (evento.getIncideAssociacaoSindicatoFerias())
			setIncideAssociacaoSindicatoFerias(getIncideAssociacaoSindicatoFerias().add(valorDoEvento));
		
		if (evento.getIncidePlanoSaudeFerias())
			setIncidePlanoSaudeFerias(getIncidePlanoSaudeFerias().add(valorDoEvento));
	}

	/**
	 * Calculas as incidencias de Rescisao do {@link EventoFolhaPagamentoVO}}
	 * 
	 * @param evento
	 */
	private void calcularIncidenciasFolhaRescisao(EventoFolhaPagamentoVO evento) {
		BigDecimal valorDoEvento = evento.getValorTemporario();

		if(evento.getIncideAdicionalTempoServicoRescisao())
			setIncideAdicionalTempoServicoRescisao(getIncideAdicionalTempoServicoRescisao().add(valorDoEvento));
	}

	/**
	 * Calculas as incidencias de Decimo terceiro do {@link EventoFolhaPagamentoVO}}
	 * 
	 * @param evento
	 */
	private void calcularIncidenciasFolhaDecimoTerceiro(EventoFolhaPagamentoVO evento) {
		// Proventos
		BigDecimal valorDoEvento = evento.getValorTemporario();

		if(evento.getInssDecimoTerceiro())
			setIncideINSSDecimoTerceiro(getIncideINSSDecimoTerceiro().add(valorDoEvento));

		if(evento.getIrrfDecimoTerceiro())
			setIncideIRRFDecimoTerceiro(getIncideIRRFDecimoTerceiro().add(valorDoEvento));

		if(evento.getFgtsDecimoTerceiro())
			setIncideFGTSDecimoTerceiro(getIncideFGTSDecimoTerceiro().add(valorDoEvento));

		if(evento.getDecimoTerceiroPrevidenciaPropria())
			setIncidePrevidenciaPropriaDecimoTerceiro(getIncidePrevidenciaPropriaDecimoTerceiro().add(valorDoEvento));

		if(evento.getDecimoTerceiroPlanoSaude())
			setIncidePlanoSaudeDecimoTerceiro(getIncidePlanoSaudeDecimoTerceiro().add(valorDoEvento));

		if(evento.getIncideAdicionalTempoServico13())
			setIncideAdicionalTempoServicoDecimoTerceiro(getIncideAdicionalTempoServicoDecimoTerceiro().add(valorDoEvento));

		if (evento.getIncideAssociacaoSindicatoDecimoTerceiro()) {
			setIncideAssociacaoSindicatoDecimoTerceiro(getIncideAssociacaoSindicatoDecimoTerceiro().add(valorDoEvento));
		}

		if (evento.getIncidePrevidenciaObrigatoriaDecimoTerceiro()) {
			setIncidePrevidenciaObrigatoriaDecimoTerceiro(getIncidePrevidenciaObrigatoriaDecimoTerceiro().add(valorDoEvento));
		}

		//Desconto
		if(evento.getEstornaInssDecimoTerceiro())
			setIncideEstornoINSSDecimoTerceiro(getIncideEstornoINSSDecimoTerceiro().subtract(valorDoEvento));

		if(evento.getEstornaFgtsDecimoTerceiro())
			setIncideEstornoFGTSDecimoTerceiro(getIncideEstornoFGTSDecimoTerceiro().subtract(valorDoEvento));

		if(evento.getEstornaIrrfDecimoTerceiro())
			setIncideEstornoIRRFDecimoTerceiro(getIncideEstornoIRRFDecimoTerceiro().subtract(valorDoEvento));

		if(evento.getDedutivelIrrfDecimoTerceiro())
			setIncideIRRFDecimoTerceiro(getIncideIRRFDecimoTerceiro().subtract(valorDoEvento));
	}

	public BigDecimal getBaseCalculoPrevidencia() {

		switch (PrevidenciaEnum.valueOf(getFuncionarioCargoVO().getPrevidencia().name())) {
		case INSS:
			baseCalculoPrevidencia = getIncideINSS().subtract(getEstornaINSS());
			break;
			
		case PREVIDENCIA_PROPRIA:
			
			if(getFuncionarioCargoVO().getOptanteTotal())
				baseCalculoPrevidencia = getIncidePrevidenciaPropria().subtract(getEstornaPrevidenciaPropria());
			else
				baseCalculoPrevidencia = getIncidePrevidenciaPropriaObrigatoria().subtract(getEstornaPrevidenciaPropriaObrigatoria());
			break;
			
		default:
			baseCalculoPrevidencia = new BigDecimal(0);
			break;
		}
		
		if (baseCalculoPrevidencia == null || baseCalculoPrevidencia.compareTo(new BigDecimal(0)) <= 0)
			baseCalculoPrevidencia = new BigDecimal(0);
		
		return baseCalculoPrevidencia;
	}

	/**
	 * Seta os valores de IRRF
	 * 
	 * @param contraChequeVO
	 */
	public void preencherValoresDeIRRF() {
		
		for(FaixaValorVO faixaValor : getValorReferencia().getListaFaixaValores()) {
			
			if((faixaValor.getLimiteInferior().compareTo(getBaseCalculoIRRF()) <= 0) &&
				faixaValor.getLimiteSuperior().compareTo(getBaseCalculoIRRF()) >= 0){
					setPercentualIRRF(faixaValor.getPercentual());
					setDedutivelIRRF(faixaValor.getValorDeduzir());
			}
		}
	}

	public BigDecimal getBaseCalculoIRRF() {

		if (baseCalculoIRRF == null|| baseCalculoIRRF.compareTo(new BigDecimal(0)) == 0) {
			baseCalculoIRRF = getIncideIRRF().subtract(getDedutivelIRRF().add(getEstornaIRRF()));

			if (baseCalculoIRRF == null || baseCalculoIRRF.compareTo(new BigDecimal(0)) <= 0)
				baseCalculoIRRF = new BigDecimal(0);
		}
		return baseCalculoIRRF;
	}

	

	public void somarValores(EventoFolhaPagamentoVO evento) {
		switch (TipoLancamentoFolhaPagamentoEnum.valueOf(evento.getTipoLancamento().name())) {
		case PROVENTO:
			setProvento(getProvento().add(evento.getValorTemporario()));
			break;
		case DESCONTO:
			setDesconto(getDesconto().add(evento.getValorTemporario()));
			break;
		case BASE_CALCULO:
			setBaseCalculo(getBaseCalculo().add(evento.getValorTemporario()));
			break;
		default:
			break;
		}
	}

	public BigDecimal getProvento() {
		if(provento == null)
			provento = BigDecimal.ZERO;
		return provento;
	}

	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}

	public BigDecimal getDesconto() {
		if(desconto == null)
			desconto = BigDecimal.ZERO;
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getBaseCalculo() {
		if(baseCalculo == null)
			baseCalculo = BigDecimal.ZERO;
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public Map<String, Object> getEventos() {
		return eventos;
	}

	public void setEventos(Map<String, Object> eventos) {
		this.eventos = eventos;
	}

	public void inicializarMapaDeEventos(List<String> listaDeIdentificadoresAtivo) {
		Map<String, Object> mapaEventos = new HashMap<>();
		for(String identificador : listaDeIdentificadoresAtivo) {
			//Valor do evento
			mapaEventos.put("e"+identificador, BigDecimal.ZERO);
			//Referencia do evento
			mapaEventos.put("r"+identificador, "");
		}
		setEventos(mapaEventos);
	}

	public void atualizarValorDoEvento(EventoFolhaPagamentoVO evento) {
		getEventos().put("e"+evento.getIdentificador(), evento.getValorTemporario());
		getEventos().put("r"+evento.getIdentificador(), evento.getReferencia());
	}

	public Integer getMesesTrabalhadosQuinquenio() {

		if(getFuncionarioCargoVO().getDataBaseQuinquenio() != null) {
			return UteisData.obterQuantidadeMesesPeriodo(funcionarioCargoVO.getDataBaseQuinquenio(), new Date()) - 1;
		} else {
			return UteisData.obterQuantidadeMesesPeriodo(funcionarioCargoVO.getDataAdmissao(), new Date()) - 1;
		}
	}
	
	public Integer getMesesTrabalhados() {
		return UteisData.obterQuantidadeMesesPeriodo(funcionarioCargoVO.getDataAdmissao(), new Date());
	}

	public BigDecimal getIncideValeTransporte() {
		if(incideValeTransporte == null)
			incideValeTransporte = BigDecimal.ZERO;
		return incideValeTransporte;
	}

	public void setIncideValeTransporte(BigDecimal incideValeTransporte) {
		this.incideValeTransporte = incideValeTransporte;
	}

	public BigDecimal getIncideAdicionalTempoServico() {
		if(incideAdicionalTempoServico == null)
			incideAdicionalTempoServico = BigDecimal.ZERO;
		return incideAdicionalTempoServico;
	}

	public void setIncideAdicionalTempoServico(BigDecimal incideAdicionalTempoServico) {
		this.incideAdicionalTempoServico = incideAdicionalTempoServico;
	}

	public BigDecimal getIncideAssociacaoSindicato() {
		if(incideAssociacaoSindicato == null)
			incideAssociacaoSindicato = BigDecimal.ZERO;
		return incideAssociacaoSindicato;
	}

	public void setIncideAssociacaoSindicato(BigDecimal incideAssociacaoSindicato) {
		this.incideAssociacaoSindicato = incideAssociacaoSindicato;
	}

	public Integer getNumerosDependentesIrrf() {
		if (numerosDependentesIrrf == null)
			numerosDependentesIrrf = 0;
		return numerosDependentesIrrf;
	}

	public void setNumerosDependentesIrrf(Integer numerosDependentesIrrf) {
		this.numerosDependentesIrrf = numerosDependentesIrrf;
	}

	public Integer getNumerosDependentesSalFamilia() {
		if (numerosDependentesSalFamilia == null)
			numerosDependentesSalFamilia = 0;
		return numerosDependentesSalFamilia;
	}

	public void setNumerosDependentesSalFamilia(Integer numerosDependentesSalFamilia) {
		this.numerosDependentesSalFamilia = numerosDependentesSalFamilia;
	}

	public static CalculoContraCheque inicializarCalculoContraCheque(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento){

		BigDecimal valorDependente = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorFixoPorReferencia(ValorFixoEnum.VALOR_DEDUZIR_DEPENDENTE_IRRF, new Date());

		ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorReferenciaPorReferencia(ValorFixoEnum.IRRF.name(), new Date());
		List<String> identificadoresEventos = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarListaDeIdentificadoresAtivo();

		return inicializarCalculoContraCheque(funcionarioCargo, valorDependente, valorReferenciaIRRF, identificadoresEventos, competenciaFolhaPagamentoVO, lancamentoFolhaPagamento);
	}
	
	public static CalculoContraCheque inicializarCalculoContraCheque(FuncionarioCargoVO funcionarioCargo, BigDecimal valorDependente, ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF, List<String> identificadores, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) {
		
		CalculoContraCheque calculo = new CalculoContraCheque();

		calculo.setFuncionarioCargoVO(funcionarioCargo);
		calculo.setValorDependente(valorDependente);
		calculo.setValorReferencia(valorReferenciaIRRF);
		
		calculo.setCompetenciaFolhaPagamentoVO(competenciaFolhaPagamentoVO);
		calculo.setAnoCompetencia(UteisData.getAnoData(competenciaFolhaPagamentoVO.getDataCompetencia()));
		calculo.setMesCompetencia(UteisData.getMesData(competenciaFolhaPagamentoVO.getDataCompetencia()));
		
		calculo.inicializarMapaDeEventos(identificadores);
		
		Integer qtdDependentesIRRF = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarQuantidadeDependentesDoFuncinarioNoIRRF(funcionarioCargo.getFuncionarioVO().getCodigo());
		calculo.setNumerosDependentesIrrf(qtdDependentesIRRF);
		
		Integer qtdDependentesSalarioFamilia = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarQuantidadeDependentesDoFuncinarioNoSalarioFamilia(funcionarioCargo.getFuncionarioVO().getCodigo());
		calculo.setNumerosDependentesSalFamilia(qtdDependentesSalarioFamilia);
		
		calculo.setCompetenciaPeriodoFolhaPagamentoVO(lancamentoFolhaPagamento.getPeriodo());
		return calculo;
	}

	public void atualizarValoresDeCalculo(EventoFolhaPagamentoVO evento) {
		somarValores(evento);
		calcularIncidenciasDaFolha(evento);
		atualizarValorDoEvento(evento);
	}

	/**
	 * Calcula as incidencia dos eventos da folha de pagamento.
	 * 
	 * @param evento
	 */
	private void calcularIncidenciasDaFolha(EventoFolhaPagamentoVO evento) {
		calcularIncidenciasFolhaNormal(evento);

		calcularIncidenciasFolhaFerias(evento);

		calcularIncidenciasFolhaDecimoTerceiro(evento);

		calcularIncidenciasFolhaRescisao(evento);
	}

	public Integer getNumeroDiasFerias() {
		if (numeroDiasFerias == null)
			numeroDiasFerias = 0;
		return numeroDiasFerias;
	}

	public void setNumeroDiasFerias(Integer numeroDiasFerias) {
		this.numeroDiasFerias = numeroDiasFerias;
	}

	public Integer getNumeroDiasAbono() {
		if (numeroDiasAbono == null)
			numeroDiasAbono = 0;
		return numeroDiasAbono;
	}

	public void setNumeroDiasAbono(Integer numeroDiasAbono) {
		this.numeroDiasAbono = numeroDiasAbono;
	}

	public BigDecimal getBaseCalculoIRRFFerias() {
		if (baseCalculoIRRFFerias == null)
			baseCalculoIRRFFerias = BigDecimal.ZERO;
		return baseCalculoIRRFFerias;
	}

	public void setBaseCalculoIRRFFerias(BigDecimal baseCalculoIRRFFerias) {
		this.baseCalculoIRRFFerias = baseCalculoIRRFFerias;
	}

	public BigDecimal getValorIRRFFerias() {
		if (valorIRRFFerias == null)
			valorIRRFFerias = BigDecimal.ZERO;
		return valorIRRFFerias;
	}

	public void setValorIRRFFerias(BigDecimal valorIRRFFerias) {
		this.valorIRRFFerias = valorIRRFFerias;
	}

	/**
	 * Atualiza os dias trabalhados para o calculo da proporcionalidade
	 * 
	 * @param evento
	 */
	public void calcularNumeroDiasTrabalhados() {

		Long qtdDiasTrabalhados = 0L;
		if (Uteis.isAtributoPreenchido(getCompetenciaFolhaPagamentoVO())) {
			qtdDiasTrabalhados = (long) UteisData.obterNrDiasNoMes(getCompetenciaFolhaPagamentoVO().getDataCompetencia());
		} else {
			qtdDiasTrabalhados = (long) UteisData.getUltimoDiaMes(new Date());
		}

		/**
		 * TODO
		 * 1- Verificar se o evento e proporcionalizado(Objeto:EventoFolhaPagamentoVO Propriedades:  admissao/demissao/fereturn mapa.DIAS_TRABALHADOSrias/afastamento) 
		 */
		
		//PROPORCIONALIDADE DE ADMISSAO
		if(getAnoCompetencia().equals(UteisData.getAnoData(getFuncionarioCargoVO().getDataAdmissao())) &&
				getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getDataAdmissao()))){
			//Diminui dos 30 dias o periodo ate a contratacao do funcionario
			qtdDiasTrabalhados += UteisData.nrDiasEntreDatas(UteisData.getPrimeiroDataMes(getFuncionarioCargoVO().getDataAdmissao()), getFuncionarioCargoVO().getDataAdmissao());
		}
		
		//PROPORCIONALIDADE DE DEMISSAO
		if(Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getDataDemissao())) {
			if(getAnoCompetencia().equals(UteisData.getAnoData(getFuncionarioCargoVO().getDataDemissao())) &&
					getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getDataDemissao()))){
				/**
				 * TODO
				 * Validar se o funcionario foi admitido no mes, caso seja, contabilizar a partir da data de admissao
				 */
				//Diminui dos 30 dias o periodo ate a contratacao do funcionario
				qtdDiasTrabalhados = UteisData.nrDiasEntreDatas(getFuncionarioCargoVO().getDataDemissao(), UteisData.getPrimeiroDataMes(getFuncionarioCargoVO().getDataDemissao()))+1;
			}			
		}
				
		//PROPORCIONALIDADE DE FERIAS				
			
		//Caso o funcionario esteja de ferias
		if(getFuncionarioCargoVO().getInicioGozoFerias() != null && getFuncionarioCargoVO().getFinalGozoFerias() != null) {

			//A ferias do funcionario inicia no mes e ano da competencia
			if(getAnoCompetencia().equals(UteisData.getAnoData(getFuncionarioCargoVO().getInicioGozoFerias())) &&
					getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getInicioGozoFerias()))){
				
				//A ferias do funcionario termina no mes e ano da competencia
				if(getAnoCompetencia().equals(UteisData.getAnoData(getFuncionarioCargoVO().getFinalGozoFerias())) &&
						getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getFinalGozoFerias()))){						
				
					//Diminui dos 30 dias o perido de gozo de ferias do funcionario 
					qtdDiasTrabalhados += UteisData.nrDiasEntreDatas(getFuncionarioCargoVO().getInicioGozoFerias(), getFuncionarioCargoVO().getFinalGozoFerias()) - 1;
					
				} else {
					//Diminui dos 30 dias o perido inicial do gozo de ferias do funcionario ate o ultimo dia do mes
					qtdDiasTrabalhados += UteisData.nrDiasEntreDatas(getFuncionarioCargoVO().getInicioGozoFerias(), UteisData.getUltimaDataMes(getFuncionarioCargoVO().getInicioGozoFerias()));
				}

				//A ferias do funcionario termina no mes e ano da competencia
			} else if(getAnoCompetencia().equals(UteisData.getAnoData(getFuncionarioCargoVO().getFinalGozoFerias())) &&
						getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getFinalGozoFerias()))){
			
					//Recebe os dias entre o final do periodo de gozo de ferias do funcionario ate o ultimo dia do mes
					qtdDiasTrabalhados = UteisData.nrDiasEntreDatas(UteisData.getUltimaDataMes(getFuncionarioCargoVO().getFinalGozoFerias()), getFuncionarioCargoVO().getFinalGozoFerias());
			}
		}

		SqlRowSet pesquisaAfastamentos = getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().consultarQuantidadeDeDiasAfastados(getFuncionarioCargoVO(),getAnoCompetencia(), getMesCompetencia());
		
		if(pesquisaAfastamentos != null) {
			Date dataInicio = UteisData.getDataSemHora(UteisData.getPrimeiroDataMes(getMesCompetencia(), getAnoCompetencia()));
			Date dataFinal = UteisData.getDataSemHora(UteisData.getUltimaDataMes(getMesCompetencia(), getAnoCompetencia()));
			qtdDiasTrabalhados -= calcularQuantidadeDeDiasAfastamentoPorPeriodo(pesquisaAfastamentos, dataInicio, dataFinal);	
		}
		
		setNumeroDiasTrabalhados(qtdDiasTrabalhados.intValue());
		
	}

	public void calcularNumeroDiasFerias() {

		Integer qdtDiasFerias = 0;

		if(Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getInicioGozoFerias()) && Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getFinalGozoFerias())) {
		
			if(UteisData.getAnoData(getFuncionarioCargoVO().getInicioGozoFerias()) == UteisData.getAnoData(getFuncionarioCargoVO().getFinalGozoFerias())) {
				
				if(UteisData.getMesData(getFuncionarioCargoVO().getInicioGozoFerias()) == UteisData.getMesData(getFuncionarioCargoVO().getFinalGozoFerias())) {
					qdtDiasFerias = UteisData.getDiaMesData(getFuncionarioCargoVO().getFinalGozoFerias()) - UteisData.getDiaMesData(getFuncionarioCargoVO().getInicioGozoFerias()) + 1;
				} else {
					
					if(getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getInicioGozoFerias()))){
						qdtDiasFerias = UteisData.getDiaMesData(UteisData.getUltimaDataMes(getMesCompetencia(), getAnoCompetencia())) - UteisData.getDiaMesData(getFuncionarioCargoVO().getInicioGozoFerias()) + 1;
					} 
					
					if(getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getFinalGozoFerias()))){
						qdtDiasFerias = UteisData.getDiaMesData(getFuncionarioCargoVO().getFinalGozoFerias());
					}
					
				}
				
			} else {
				
				if(getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getInicioGozoFerias()))){
					qdtDiasFerias = UteisData.getDiaMesData(UteisData.getUltimaDataMes(getMesCompetencia(), getAnoCompetencia())) - UteisData.getDiaMesData(getFuncionarioCargoVO().getInicioGozoFerias()) + 1;
				} 
				
				if(getMesCompetencia().equals(UteisData.getMesData(getFuncionarioCargoVO().getFinalGozoFerias()))){
					qdtDiasFerias = UteisData.getDiaMesData(getFuncionarioCargoVO().getFinalGozoFerias());
				}
			}
		}
		
		setDiasFeriasMes(qdtDiasFerias);
	}
	
	
	/**
	 * Adiciona os valores de avos no calculo do contracheque
	 * 
	 * @param funcionarioCargoVO
	 * @param dataCompetencia
	 */
	public void realizarMapasDeAvos(Date dataCompetencia, Date dataInicioPeriodoAquisitivoAberto, Integer qtdFaltas, Integer periodosPerdidos) {
		
		Date dataInicialAno = UteisData.getPrimeiroDataMes(01, UteisData.getAnoData(dataCompetencia));
		Date dataAdmissao = getFuncionarioCargoVO().getDataAdmissao();
		Date dataFinal = Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getDataDemissao()) ? getFuncionarioCargoVO().getDataDemissao() : UteisData.getUltimaDataMes(12, UteisData.getAnoData(dataCompetencia));
		
		setAvos13(UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataFinal, periodosPerdidos));
		setAvosFeriasProporcionais(UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPeriodoAquisitivoAberto, dataFinal, qtdFaltas));
		
	}
	
	public Integer getAnoCompetencia() {
		if (anoCompetencia == null)
			anoCompetencia = 0;
		return anoCompetencia;
	}

	public void setAnoCompetencia(Integer anoCompetencia) {
		this.anoCompetencia = anoCompetencia;
	}

	public Integer getMesCompetencia() {
		if (mesCompetencia == null)
			mesCompetencia = 0;
		return mesCompetencia;
	}

	public void setMesCompetencia(Integer mesCompetencia) {
		this.mesCompetencia = mesCompetencia;
	}

	public Integer getNumeroDiasTrabalhados() {
			calcularNumeroDiasTrabalhados();
		return numeroDiasTrabalhados;
	}

	public void setNumeroDiasTrabalhados(Integer numeroDiasTrabalhados) {
		this.numeroDiasTrabalhados = numeroDiasTrabalhados;
	}

	public BigDecimal getIncideAdicionalTempoServicoFerias() {
		if (incideAdicionalTempoServicoFerias == null) {
			incideAdicionalTempoServicoFerias = BigDecimal.ZERO;
		}
		return incideAdicionalTempoServicoFerias;
	}

	public void setIncideAdicionalTempoServicoFerias(BigDecimal incideAdicionalTempoServicoFerias) {
		this.incideAdicionalTempoServicoFerias = incideAdicionalTempoServicoFerias;
	}

	public BigDecimal getIncideAdicionalTempoServicoRescisao() {
		if (incideAdicionalTempoServicoRescisao == null) {
			incideAdicionalTempoServicoRescisao = BigDecimal.ZERO;
		}
		return incideAdicionalTempoServicoRescisao;
	}

	public void setIncideAdicionalTempoServicoRescisao(BigDecimal incideAdicionalTempoServicoRescisao) {
		this.incideAdicionalTempoServicoRescisao = incideAdicionalTempoServicoRescisao;
	}

	public BigDecimal getValorIRRFJaLancado() {
		if (valorIRRFJaLancado == null)
			valorIRRFJaLancado = BigDecimal.ZERO;
		return valorIRRFJaLancado;
	}

	public void setValorIRRFJaLancado(BigDecimal valorIRRFJaLancado) {
		this.valorIRRFJaLancado = valorIRRFJaLancado;
	}

	public BigDecimal getAdicionalFerias() {
		if (adicionalFerias == null) {
			adicionalFerias = BigDecimal.ZERO;
		}
		return adicionalFerias;
	}

	public void setAdicionalFerias(BigDecimal adicionalFerias) {
		this.adicionalFerias = adicionalFerias;
	}

	public BigDecimal getIncideINSSFerias() {
		if(incideINSSFerias == null)
			incideINSSFerias = BigDecimal.ZERO;
		return incideINSSFerias;
	}

	public void setIncideINSSFerias(BigDecimal incideINSSFerias) {
		this.incideINSSFerias = incideINSSFerias;
	}

	public BigDecimal getIncidePrevidenciaPropriaFerias() {
		if(incidePrevidenciaPropriaFerias == null)
			incidePrevidenciaPropriaFerias = BigDecimal.ZERO;
		return incidePrevidenciaPropriaFerias;
	}

	public void setIncidePrevidenciaPropriaFerias(BigDecimal incidePrevidenciaPropriaFerias) {
		this.incidePrevidenciaPropriaFerias = incidePrevidenciaPropriaFerias;
	}

	public BigDecimal getIncidePrevidenciaPropriaObrigatoriaFerias() {
		if(incidePrevidenciaPropriaObrigatoriaFerias == null)
			incidePrevidenciaPropriaObrigatoriaFerias = BigDecimal.ZERO;
		return incidePrevidenciaPropriaObrigatoriaFerias;
	}

	public void setIncidePrevidenciaPropriaObrigatoriaFerias(BigDecimal incidePrevidenciaPropriaObrigatoriaFerias) {
		this.incidePrevidenciaPropriaObrigatoriaFerias = incidePrevidenciaPropriaObrigatoriaFerias;
	}

	public BigDecimal getIncideAssociacaoSindicatoFerias() {
		if(incideAssociacaoSindicatoFerias == null)
			incideAssociacaoSindicatoFerias = BigDecimal.ZERO;
		return incideAssociacaoSindicatoFerias;
	}

	public void setIncideAssociacaoSindicatoFerias(BigDecimal incideAssociacaoSindicatoFerias) {
		this.incideAssociacaoSindicatoFerias = incideAssociacaoSindicatoFerias;
	}

	public Integer getAvos13() {
		if (avos13 == null)
			avos13 = 12;
		return avos13;
	}

	public void setAvos13(Integer avos13) {
		this.avos13 = avos13;
	}

	public Integer getAvosFeriasProporcionais() {
		if (avosFeriasProporcionais == null)
			avosFeriasProporcionais = 12;
		return avosFeriasProporcionais;
	}

	public void setAvosFeriasProporcionais(Integer avosFeriasProporcionais) {
		this.avosFeriasProporcionais = avosFeriasProporcionais;
	}
	
	//Decimo Terceiro

	public BigDecimal getIncideINSSDecimoTerceiro() {
		if (incideINSSDecimoTerceiro == null) {
			incideINSSDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideINSSDecimoTerceiro;
	}

	public void setIncideINSSDecimoTerceiro(BigDecimal incideINSSDecimoTerceiro) {
		this.incideINSSDecimoTerceiro = incideINSSDecimoTerceiro;
	}

	public BigDecimal getIncideIRRFDecimoTerceiro() {
		if (incideIRRFDecimoTerceiro == null) {
			incideIRRFDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideIRRFDecimoTerceiro;
	}

	public void setIncideIRRFDecimoTerceiro(BigDecimal incideIRRFDecimoTerceiro) {
		this.incideIRRFDecimoTerceiro = incideIRRFDecimoTerceiro;
	}

	public BigDecimal getIncideFGTSDecimoTerceiro() {
		if (incideFGTSDecimoTerceiro == null) {
			incideFGTSDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideFGTSDecimoTerceiro;
	}

	public void setIncideFGTSDecimoTerceiro(BigDecimal incideFGTSDecimoTerceiro) {
		this.incideFGTSDecimoTerceiro = incideFGTSDecimoTerceiro;
	}

	public BigDecimal getIncidePrevidenciaPropriaDecimoTerceiro() {
		if (incidePrevidenciaPropriaDecimoTerceiro == null) {
			incidePrevidenciaPropriaDecimoTerceiro = BigDecimal.ZERO;
		}
		return incidePrevidenciaPropriaDecimoTerceiro;
	}

	public void setIncidePrevidenciaPropriaDecimoTerceiro(BigDecimal incidePrevidenciaPropriaDecimoTerceiro) {
		this.incidePrevidenciaPropriaDecimoTerceiro = incidePrevidenciaPropriaDecimoTerceiro;
	}

	public BigDecimal getIncidePlanoSaudeDecimoTerceiro() {
		if (incidePlanoSaudeDecimoTerceiro == null) {
			incidePlanoSaudeDecimoTerceiro = BigDecimal.ZERO;
		}
		return incidePlanoSaudeDecimoTerceiro;
	}

	public void setIncidePlanoSaudeDecimoTerceiro(BigDecimal incidePlanoSaudeDecimoTerceiro) {
		this.incidePlanoSaudeDecimoTerceiro = incidePlanoSaudeDecimoTerceiro;
	}

	public BigDecimal getIncideAdicionalTempoServicoDecimoTerceiro() {
		if (incideAdicionalTempoServicoDecimoTerceiro == null) {
			incideAdicionalTempoServicoDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideAdicionalTempoServicoDecimoTerceiro;
	}

	public void setIncideAdicionalTempoServicoDecimoTerceiro(BigDecimal incideAdicionalTempoServicoDecimoTerceiro) {
		this.incideAdicionalTempoServicoDecimoTerceiro = incideAdicionalTempoServicoDecimoTerceiro;
	}

	public BigDecimal getIncideEstornoINSSDecimoTerceiro() {
		if (incideEstornoINSSDecimoTerceiro == null) {
			incideEstornoINSSDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideEstornoINSSDecimoTerceiro;
	}

	public void setIncideEstornoINSSDecimoTerceiro(BigDecimal incideEstornoINSSDecimoTerceiro) {
		this.incideEstornoINSSDecimoTerceiro = incideEstornoINSSDecimoTerceiro;
	}

	public BigDecimal getIncideEstornoFGTSDecimoTerceiro() {
		if (incideEstornoFGTSDecimoTerceiro == null) {
			incideEstornoFGTSDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideEstornoFGTSDecimoTerceiro;
	}

	public void setIncideEstornoFGTSDecimoTerceiro(BigDecimal incideEstornoFGTSDecimoTerceiro) {
		this.incideEstornoFGTSDecimoTerceiro = incideEstornoFGTSDecimoTerceiro;
	}

	public BigDecimal getIncideEstornoIRRFDecimoTerceiro() {
		if (incideEstornoIRRFDecimoTerceiro == null) {
			incideEstornoIRRFDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideEstornoIRRFDecimoTerceiro;
	}

	public void setIncideEstornoIRRFDecimoTerceiro(BigDecimal incideEstornoIRRFDecimoTerceiro) {
		this.incideEstornoIRRFDecimoTerceiro = incideEstornoIRRFDecimoTerceiro;
	}

	public BigDecimal getIncideEstornoDedutivelIRRFDecimoTerceiro() {
		if (incideEstornoDedutivelIRRFDecimoTerceiro == null) {
			incideEstornoDedutivelIRRFDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideEstornoDedutivelIRRFDecimoTerceiro;
	}

	public void setIncideEstornoDedutivelIRRFDecimoTerceiro(BigDecimal incideEstornoDedutivelIRRFDecimoTerceiro) {
		this.incideEstornoDedutivelIRRFDecimoTerceiro = incideEstornoDedutivelIRRFDecimoTerceiro;
	}

	public String getReferenciaEvento() {
		if (referenciaEvento == null)
			referenciaEvento = "";
		return referenciaEvento;
	}

	public void setReferenciaEvento(String referenciaEvento) {
		this.referenciaEvento = referenciaEvento;
	}

	public Integer getDiasFeriasMes() {
		if (diasFeriasMes == null)
			diasFeriasMes = 0;
		return diasFeriasMes;
	}

	public void setDiasFeriasMes(Integer diasFeriasMes) {
		this.diasFeriasMes = diasFeriasMes;
	}
	
	
	/**
	 * Calcula o saldo de periodos perdidos do 13. (Conta do inicio do ano ou a partir da data de admissao do funcionario, caso o mesmo tenha seido admitido no ano)
	 * 
	 * Periodo perdido deve contabilizar mes a mes, os dias de falta (pesquisaAfastamentos) + os dias de afastamento (pesquisaAfastamentos)
	 * Pontos de observacao:
	 * 1- O funcionario pode ter mais de um afastamento no ano
	 * 2- O funcionario pode iniciar o ano ja afastado
	 * 3- O funcionario pode terminar um afastamento e iniciar o outro logo em seguida
	 * 
	 * @param pesquisaAfastamentos
	 * @param pesquisaFaltas
	 * @param dataFinal
	 * @return
	 */
	public Integer calcularQuantidadePeriodoPerdido13(SqlRowSet pesquisaAfastamentos, SqlRowSet pesquisaFaltas, Date dataFinal) {

		Integer mes = 1;
		Integer diaInicio = 1;
		int saldoDeDias = 0;
		int diasNaoContabilizados = 0;
		int diasFaltas = 0;
		int qtdPeriodosPerdidos = 0;
		boolean iniciouAfastamento = false;
		Date dataFinalAfastamento = dataFinal;
		
		if(getAnoCompetencia().equals(UteisData.getAnoData(getFuncionarioCargoVO().getDataAdmissao()))) {
			mes = UteisData.getMesData(getFuncionarioCargoVO().getDataAdmissao());
		}
		
		int mesAfastamento;
		if (pesquisaAfastamentos == null)
			mesAfastamento = 0;
		else if(UteisData.getAnoData(pesquisaAfastamentos.getDate("dataInicialAfastamento")) == getAnoCompetencia()) {
			mesAfastamento = pesquisaAfastamentos.getInt("mesInicio");
			diaInicio = pesquisaAfastamentos.getInt("diaInicio");
		} else {
			mesAfastamento = 1;
			diaInicio = 1;
		}
			
		
		int mesFalta;
		if (pesquisaFaltas == null)
			mesFalta = 0;
		else {
			mesFalta = pesquisaFaltas.getInt("mesFalta");
			diasFaltas = pesquisaFaltas.getInt("faltas");
		}
			
		do {

			diasNaoContabilizados = 0;
			
			/////////////////AFASTAMENTOS/////////////////////////
			if(mesAfastamento == mes && !iniciouAfastamento) {
				diasNaoContabilizados = UteisData.getDiaMesData(UteisData.getUltimaDataMes(pesquisaAfastamentos.getDate("dataInicialAfastamento"))) - diaInicio + saldoDeDias + 1;
				saldoDeDias = 0;
				iniciouAfastamento = true;
				
				if(pesquisaAfastamentos.getDate("dataFinalAfastamento") != null) {
					dataFinalAfastamento = pesquisaAfastamentos.getDate("dataFinalAfastamento");
				} else {
					dataFinalAfastamento = dataFinal;
				}
				if(mesAfastamento == UteisData.getMesData(dataFinalAfastamento)) {
					if(pesquisaAfastamentos.next()) {
						iniciouAfastamento = false;
						mesAfastamento = UteisData.getMesData(dataFinalAfastamento);
					} else {
						iniciouAfastamento = false;
						mesAfastamento = 0;
					}

				}
				
			} else	if(iniciouAfastamento) {
				if(pesquisaAfastamentos.getDate("dataFinalAfastamento") != null) {
					dataFinalAfastamento = pesquisaAfastamentos.getDate("dataFinalAfastamento");
				} else {
					dataFinalAfastamento = dataFinal;
				}
				if(mes == UteisData.getMesData(dataFinalAfastamento)) {
					diasNaoContabilizados += UteisData.getDiaMesData(dataFinalAfastamento);
					if(pesquisaAfastamentos.next()) {
						iniciouAfastamento = false;
						mesAfastamento = pesquisaAfastamentos.getInt("mesInicio");
						diaInicio = pesquisaAfastamentos.getInt("diaInicio");
						dataFinal = pesquisaAfastamentos.getDate("dataFinalAfastamento") != null ? pesquisaAfastamentos.getDate("dataFinalAfastamento") : dataFinal;
						saldoDeDias = diasNaoContabilizados;
						continue;
					} else {
						iniciouAfastamento = false;
						mesAfastamento = 0;
					}

				} else {
					//Soma aos dias de afastamento a quantidade de dias do mes
					diasNaoContabilizados += UteisData.getDiaMesData(UteisData.getUltimaDataMes(mes, getAnoCompetencia()));
				}				
			}
			
			
			/////////////////FALTAS/////////////////////////
			
			if(mesFalta == mes) {
				diasNaoContabilizados += diasFaltas;
				
				if(pesquisaFaltas != null && pesquisaFaltas.next()) {
					mesFalta = pesquisaFaltas.getInt("mesFalta");
					diasFaltas = pesquisaFaltas.getInt("faltas");
				}
			}
			
			if(diasNaoContabilizados > 15)
				qtdPeriodosPerdidos++;
			
			mes++;
		} while(mes <= 12);
		
		return qtdPeriodosPerdidos;
	}

	public void calcularAvos13(Date dataCompetenciaAtual, Boolean consideraAfastamentoPrevidencia) {

		Date dataFinal = Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getDataDemissao()) ? getFuncionarioCargoVO().getDataDemissao() : UteisData.getUltimaDataMes(UteisData.getMesData(dataCompetenciaAtual), UteisData.getAnoData(dataCompetenciaAtual)); 
		Date dataInicialAno = UteisData.getPrimeiroDataMes(01, UteisData.getAnoData(dataCompetenciaAtual));
		Date dataAdmissao = getFuncionarioCargoVO().getDataAdmissao();

		Integer qtdPeriodoPerdidoPro13 = 0;

		SqlRowSet pesquisaAfastamentos = getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().consultarAfastamentoDoFuncionarioCargo(getFuncionarioCargoVO(), dataInicialAno, dataFinal, consideraAfastamentoPrevidencia);

		SqlRowSet pesquisaFaltas = getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarQuantidadeDeFaltasDoFuncionarioCargoPorPeriodo(getFuncionarioCargoVO(), dataInicialAno, dataFinal);

		if(pesquisaAfastamentos != null || pesquisaFaltas != null) {
			qtdPeriodoPerdidoPro13 = calcularQuantidadePeriodoPerdido13(pesquisaAfastamentos, pesquisaFaltas, dataFinal);
		}

		setAvos13(UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataFinal, qtdPeriodoPerdidoPro13));
	}

	/**
	 * Realiza o calculo de avos de fefiras do {@link FuncionarioCargoVO}
	 * 
	 * @param dataCompetenciaAtual
	 * @param dataManualInformada
	 * @param consideraAfastamentoPrevidencia
	 */
	public void calcularAvosFerias(Date dataCompetenciaAtual, Date dataManualInformada, Boolean consideraAfastamentoPrevidencia) {
		PeriodoAquisitivoFeriasVO periodoAquisitivoAberto = getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPrimeiroPeriodoAquisitivoAbertoPorFuncionarioCargo(getFuncionarioCargoVO().getMatriculaCargo());
		Date dataFinal;

		if(dataManualInformada ==null) {
			dataFinal = Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getDataDemissao()) ? getFuncionarioCargoVO().getDataDemissao() : periodoAquisitivoAberto.getFinalPeriodo(); 
		} else {
			dataFinal = dataManualInformada;
		}

		Integer qtdFaltas = getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarQtdFaltasDoPeriodo(getFuncionarioCargoVO(), periodoAquisitivoAberto.getInicioPeriodo(), periodoAquisitivoAberto.getFinalPeriodo());

		Integer qtdAfastamento = getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().consultarQuantidadeDeDiasDeAfastamentoDoFuncionarioCargo(
				getFuncionarioCargoVO(), periodoAquisitivoAberto.getInicioPeriodo(), periodoAquisitivoAberto.getFinalPeriodo(), consideraAfastamentoPrevidencia); 

		setAvosFeriasProporcionais(UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(periodoAquisitivoAberto.getInicioPeriodo(), dataFinal, qtdFaltas+qtdAfastamento));
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamentoVO() {
		if (competenciaFolhaPagamentoVO == null)
			competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();
		return competenciaFolhaPagamentoVO;
	}

	public void setCompetenciaFolhaPagamentoVO(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		this.competenciaFolhaPagamentoVO = competenciaFolhaPagamentoVO;
	}

	
	private Integer calcularQuantidadeDeDiasAfastamentoPorPeriodo(SqlRowSet pesquisaAfastamento, Date dataInicial, Date dataFinal) {
		
		Integer qtdDias = 0;

		do {
			
			Date dataInicialAfastamento = pesquisaAfastamento.getDate("dataInicialAfastamento");
			Date dataFinalAfastamento = pesquisaAfastamento.getDate("dataFinalAfastamento");

			if(dataInicialAfastamento.before(dataInicial))
				dataInicialAfastamento = dataInicial;
			
			if(dataFinalAfastamento.after(dataFinal))
				dataFinalAfastamento = dataFinal;
			
			qtdDias += (int) UteisData.getCalculaDiferencaEmDias(dataInicialAfastamento, dataFinalAfastamento) +1;
			
		} while (pesquisaAfastamento.next());
		
		return qtdDias;
	}

	public BigDecimal getIncidePlanoSaudeFerias() {
		if (incidePlanoSaudeFerias == null) {
			incidePlanoSaudeFerias = BigDecimal.ZERO;
		}
		return incidePlanoSaudeFerias;
	}

	public void setIncidePlanoSaudeFerias(BigDecimal incidePlanoSaudeFerias) {
		this.incidePlanoSaudeFerias = incidePlanoSaudeFerias;
	}

	public BigDecimal getIncideIRRF() {
		if (incideIRRF == null)
			incideIRRF = new BigDecimal(0);
		return incideIRRF;
	}

	public BigDecimal getDedutivelIRRF() {
		if (dedutivelIRRF == null)
			dedutivelIRRF = new BigDecimal(0);
		return dedutivelIRRF;
	}

	public BigDecimal getValorDependente() {
		if (valorDependente == null)
			valorDependente = new BigDecimal(0);
		return valorDependente;
	}

	public BigDecimal getValorIRRF() {
		if (valorIRRF == null)
			valorIRRF = new BigDecimal(0);
		return valorIRRF;
	}

	public BigDecimal getIncideINSS() {
		if (incideINSS == null)
			incideINSS = new BigDecimal(0);
		return incideINSS;
	}

	public BigDecimal getIncidePrevidenciaPropria() {
		if (incidePrevidenciaPropria == null)
			incidePrevidenciaPropria = new BigDecimal(0);
		return incidePrevidenciaPropria;
	}

	public BigDecimal getIncidePrevidenciaPropriaObrigatoria() {
		if (incidePrevidenciaPropriaObrigatoria == null)
			incidePrevidenciaPropriaObrigatoria = new BigDecimal(0);
		return incidePrevidenciaPropriaObrigatoria;
	}

	public void setBaseCalculoIRRF(BigDecimal baseCalculoIRRF) {
		this.baseCalculoIRRF = baseCalculoIRRF;
	}

	public void setIncideIRRF(BigDecimal incideIRRF) {
		this.incideIRRF = incideIRRF;
	}

	public void setDedutivelIRRF(BigDecimal dedutivelIRRF) {
		this.dedutivelIRRF = dedutivelIRRF;
	}

	public void setValorDependente(BigDecimal valorDependente) {
		this.valorDependente = valorDependente;
	}

	public void setValorIRRF(BigDecimal valorIRRF) {
		this.valorIRRF = valorIRRF;
	}

	public void setIncideINSS(BigDecimal incideINSS) {
		this.incideINSS = incideINSS;
	}

	public void setIncidePrevidenciaPropria(BigDecimal incidePrevidenciaPropria) {
		this.incidePrevidenciaPropria = incidePrevidenciaPropria;
	}

	public void setIncidePrevidenciaPropriaObrigatoria(BigDecimal incidePrevidenciaPropriaObrigatoria) {
		this.incidePrevidenciaPropriaObrigatoria = incidePrevidenciaPropriaObrigatoria;
	}

	public BigDecimal getEstornaINSS() {
		if (estornaINSS == null)
			estornaINSS = new BigDecimal(0);
		return estornaINSS;
	}

	public BigDecimal getEstornaPrevidenciaPropria() {
		if (estornaPrevidenciaPropria == null)
			estornaPrevidenciaPropria = new BigDecimal(0);
		return estornaPrevidenciaPropria;
	}

	public BigDecimal getEstornaPrevidenciaPropriaObrigatoria() {
		if (estornaPrevidenciaPropriaObrigatoria == null)
			estornaPrevidenciaPropriaObrigatoria = new BigDecimal(0);
		return estornaPrevidenciaPropriaObrigatoria;
	}

	public void setEstornaINSS(BigDecimal estornaINSS) {
		this.estornaINSS = estornaINSS;
	}

	public void setEstornaPrevidenciaPropria(BigDecimal estornaPrevidenciaPropria) {
		this.estornaPrevidenciaPropria = estornaPrevidenciaPropria;
	}

	public void setEstornaPrevidenciaPropriaObrigatoria(BigDecimal estornaPrevidenciaPropriaObrigatoria) {
		this.estornaPrevidenciaPropriaObrigatoria = estornaPrevidenciaPropriaObrigatoria;
	}

	public void setBaseCalculoPrevidencia(BigDecimal baseCalculoPrevidencia) {
		this.baseCalculoPrevidencia = baseCalculoPrevidencia;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null)
			funcionarioCargoVO = new FuncionarioCargoVO();
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public ValorReferenciaFolhaPagamentoVO getValorReferencia() {
		if (valorReferencia == null)
			valorReferencia = new ValorReferenciaFolhaPagamentoVO();
		return valorReferencia;
	}

	public void setValorReferencia(ValorReferenciaFolhaPagamentoVO valorReferencia) {
		this.valorReferencia = valorReferencia;
	}

	public BigDecimal getFgts() {
		if (fgts == null)
			fgts = new BigDecimal(0);
		return fgts;
	}

	public BigDecimal getDsr() {
		if (dsr == null)
			dsr = new BigDecimal(0);
		return dsr;
	}

	public BigDecimal getPlanoSaude() {
		if (planoSaude == null)
			planoSaude = new BigDecimal(0);
		return planoSaude;
	}

	public BigDecimal getSalarioFamilia() {
		if (salarioFamilia == null)
			salarioFamilia = new BigDecimal(0);
		return salarioFamilia;
	}

	public void setFgts(BigDecimal fgts) {
		this.fgts = fgts;
	}

	public void setDsr(BigDecimal dsr) {
		this.dsr = dsr;
	}

	public void setPlanoSaude(BigDecimal planoSaude) {
		this.planoSaude = planoSaude;
	}

	public void setSalarioFamilia(BigDecimal salarioFamilia) {
		this.salarioFamilia = salarioFamilia;
	}

	public BigDecimal getInformeRendimento() {
		if (informeRendimento == null)
			informeRendimento = new BigDecimal(0);
		return informeRendimento;
	}

	public BigDecimal getRais() {
		if (rais == null)
			rais = new BigDecimal(0);
		return rais;
	}

	public void setInformeRendimento(BigDecimal informeRendimento) {
		this.informeRendimento = informeRendimento;
	}

	public void setRais(BigDecimal rais) {
		this.rais = rais;
	}

	public BigDecimal getPercentualIRRF() {
		if (percentualIRRF == null)
			percentualIRRF = new BigDecimal(0);
		return percentualIRRF;
	}

	public void setPercentualIRRF(BigDecimal percentualIRRF) {
		this.percentualIRRF = percentualIRRF;
	}

	public BigDecimal getEstornaIRRF() {
		if (estornaIRRF == null)
			estornaIRRF = new BigDecimal(0);
		return estornaIRRF;
	}

	public void setEstornaIRRF(BigDecimal estornaIRRF) {
		this.estornaIRRF = estornaIRRF;
	}

	public BigDecimal getIncidePrevidenciaObrigatoriaDecimoTerceiro() {
		if (incidePrevidenciaObrigatoriaDecimoTerceiro == null) {
			incidePrevidenciaObrigatoriaDecimoTerceiro = BigDecimal.ZERO;
		}
		return incidePrevidenciaObrigatoriaDecimoTerceiro;
	}

	public void setIncidePrevidenciaObrigatoriaDecimoTerceiro(BigDecimal incidePrevidenciaObrigatoriaDecimoTerceiro) {
		this.incidePrevidenciaObrigatoriaDecimoTerceiro = incidePrevidenciaObrigatoriaDecimoTerceiro;
	}

	public BigDecimal getIncideAssociacaoSindicatoDecimoTerceiro() {
		if (incideAssociacaoSindicatoDecimoTerceiro == null) {
			incideAssociacaoSindicatoDecimoTerceiro = BigDecimal.ZERO;
		}
		return incideAssociacaoSindicatoDecimoTerceiro;
	}

	public void setIncideAssociacaoSindicatoDecimoTerceiro(BigDecimal incideAssociacaoSindicatoDecimoTerceiro) {
		this.incideAssociacaoSindicatoDecimoTerceiro = incideAssociacaoSindicatoDecimoTerceiro;
	}

	public ContraChequeVO getContraChequeVO() {
		if (contraChequeVO == null) {
			contraChequeVO = new ContraChequeVO();
		}
		return contraChequeVO;
	}

	public void setContraChequeVO(ContraChequeVO contraChequeVO) {
		this.contraChequeVO = contraChequeVO;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getCompetenciaPeriodoFolhaPagamentoVO() {
		if (competenciaPeriodoFolhaPagamentoVO == null) {
			competenciaPeriodoFolhaPagamentoVO = new CompetenciaPeriodoFolhaPagamentoVO();
		}
		return competenciaPeriodoFolhaPagamentoVO;
	}

	public void setCompetenciaPeriodoFolhaPagamentoVO(
			CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO) {
		this.competenciaPeriodoFolhaPagamentoVO = competenciaPeriodoFolhaPagamentoVO;
	}

	public String getSql() {
		if (sql == null) {
			sql = "";
		}
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public static CalculoContraCheque inicializarCalculoContraChequeParaRecalculoFolha(CalculoContraCheque calculo, FuncionarioCargoVO funcionarioCargo, BigDecimal valorDependente,
			ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) {
		calculo.setFuncionarioCargoVO(funcionarioCargo);
		calculo.setValorDependente(valorDependente);
		calculo.setValorReferencia(valorReferenciaIRRF);
		
		calculo.setCompetenciaFolhaPagamentoVO(competenciaFolhaPagamentoVO);
		calculo.setAnoCompetencia(UteisData.getAnoData(competenciaFolhaPagamentoVO.getDataCompetencia()));
		calculo.setMesCompetencia(UteisData.getMesData(competenciaFolhaPagamentoVO.getDataCompetencia()));
		
		Integer qtdDependentesIRRF = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarQuantidadeDependentesDoFuncinarioNoIRRF(funcionarioCargo.getFuncionarioVO().getCodigo());
		calculo.setNumerosDependentesIrrf(qtdDependentesIRRF);
		
		Integer qtdDependentesSalarioFamilia = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarQuantidadeDependentesDoFuncinarioNoSalarioFamilia(funcionarioCargo.getFuncionarioVO().getCodigo());
		calculo.setNumerosDependentesSalFamilia(qtdDependentesSalarioFamilia);
		
		calculo.setCompetenciaPeriodoFolhaPagamentoVO(lancamentoFolhaPagamento.getPeriodo());
		calculo.setAdicionalFerias(BigDecimal.ZERO);
		calculo.setProvento(BigDecimal.ZERO);
		calculo.setDesconto(BigDecimal.ZERO);
		calculo.setRais(BigDecimal.ZERO);
		calculo.setBaseCalculo(BigDecimal.ZERO);
		calculo.setBaseCalculoIRRF(BigDecimal.ZERO);
		calculo.setBaseCalculoIRRFFerias(BigDecimal.ZERO);
		calculo.setBaseCalculoPrevidencia(BigDecimal.ZERO);
		calculo.setDedutivelIRRF(BigDecimal.ZERO);
		calculo.setIncideEstornoDedutivelIRRFDecimoTerceiro(BigDecimal.ZERO);

		calculo.setIncideAdicionalTempoServico(BigDecimal.ZERO);
		calculo.setIncideAdicionalTempoServicoDecimoTerceiro(BigDecimal.ZERO);
		calculo.setIncideAdicionalTempoServicoFerias(BigDecimal.ZERO);
		calculo.setIncideAdicionalTempoServicoRescisao(BigDecimal.ZERO);

		calculo.setIncideEstornoFGTSDecimoTerceiro(BigDecimal.ZERO);
		calculo.setIncideAssociacaoSindicatoDecimoTerceiro(BigDecimal.ZERO);
		calculo.setIncideEstornoINSSDecimoTerceiro(BigDecimal.ZERO);
		calculo.setIncideEstornoIRRFDecimoTerceiro(BigDecimal.ZERO);
		calculo.setPlanoSaude(BigDecimal.ZERO);
		calculo.setIncidePlanoSaudeFerias(BigDecimal.ZERO);
		calculo.setIncidePlanoSaudeDecimoTerceiro(BigDecimal.ZERO);
		
		calculo.setIncideIRRFDecimoTerceiro(BigDecimal.ZERO);
		calculo.setIncideEstornoIRRFDecimoTerceiro(BigDecimal.ZERO);
		
		calculo.setIncidePrevidenciaPropria(BigDecimal.ZERO);
		calculo.setIncidePrevidenciaPropriaObrigatoria(BigDecimal.ZERO);
		calculo.setIncideAssociacaoSindicato(BigDecimal.ZERO);
		calculo.setInformeRendimento(BigDecimal.ZERO);
		calculo.setSalarioFamilia(BigDecimal.ZERO);
		calculo.setFgts(BigDecimal.ZERO);
		calculo.setValorIRRF(BigDecimal.ZERO);
		
		return calculo;
	}
}