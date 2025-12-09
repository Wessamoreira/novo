package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;

/**
 * Reponsável por manter os dados da entidade ContraCheque. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ContraChequeVO extends SuperVO {

	private static final long serialVersionUID = -7735319448491704653L;

	private Integer codigo;
	@Deprecated
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamento;
	private CompetenciaPeriodoFolhaPagamentoVO periodo;
	private FuncionarioCargoVO funcionarioCargo;
	private BigDecimal totalProvento;
	private BigDecimal totalDesconto;
	private BigDecimal totalReceber;
	private String tipoLancamento;

	// Incidências
	private String tipoRecebimento;
	private BigDecimal salario;
	private PrevidenciaEnum previdencia;
	private Boolean optanteTotal;

	private BigDecimal FGTS;
	private BigDecimal DSR;
	private BigDecimal salarioFamilia;
	private Integer numeroDependenteSalarioFamilia;
	private BigDecimal previdenciaPropria;
	private BigDecimal planoSaude;

	private BigDecimal baseCalculoINSS;
	private BigDecimal valorINSS;

	private BigDecimal baseCalculoIRRF;
	private BigDecimal faixa;
	private BigDecimal dedutivel;
	private Integer numerosDependentes;
	private BigDecimal valorDependente;
	private BigDecimal valorIRRF;

	private BigDecimal valorIRRFFerias;
	private BigDecimal baseCalculoIRRFFerias;

	private BigDecimal informeRendimento;
	private BigDecimal RAIS;

	private List<ContraChequeEventoVO> contraChequeEventos;

	@Deprecated
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO;

	// Transiente
	private Boolean lancadoAdiantamentoFerias;
	private Boolean lancadoReciboFerias;

	public enum EnumCampoConsultaContraCheque {
		FUNCIONARIO, CARGO, MATRICULA, MATRICULA_CARGO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamento() {
		if (competenciaFolhaPagamento == null) {
			competenciaFolhaPagamento = new CompetenciaFolhaPagamentoVO();
		}
		return competenciaFolhaPagamento;
	}

	public void setCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		this.competenciaFolhaPagamento = competenciaFolhaPagamento;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getPeriodo() {
		if (periodo == null) {
			periodo = new CompetenciaPeriodoFolhaPagamentoVO();
		}
		return periodo;
	}

	public void setPeriodo(CompetenciaPeriodoFolhaPagamentoVO periodo) {
		this.periodo = periodo;
	}

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public BigDecimal getTotalProvento() {
		if (totalProvento == null) {
			totalProvento = BigDecimal.ZERO;
		}
		return totalProvento;
	}

	public void setTotalProvento(BigDecimal totalProvento) {
		this.totalProvento = totalProvento;
	}

	public BigDecimal getTotalDesconto() {
		if (totalDesconto == null) {
			totalDesconto = BigDecimal.ZERO;
		}
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public BigDecimal getTotalReceber() {
		if (totalReceber == null) {
			totalReceber = BigDecimal.ZERO;
		}
		return totalReceber;
	}

	public void setTotalReceber(BigDecimal totalReceber) {
		this.totalReceber = totalReceber;
	}

	public List<ContraChequeEventoVO> getContraChequeEventos() {
		if (contraChequeEventos == null) {
			contraChequeEventos = new ArrayList<>();
		}
		return contraChequeEventos;
	}

	public void setContraChequeEventos(List<ContraChequeEventoVO> contraChequeEventos) {
		this.contraChequeEventos = contraChequeEventos;
	}

	public String getTipoRecebimento() {
		return tipoRecebimento;
	}

	public void setTipoRecebimento(String tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	public BigDecimal getSalario() {
		if (salario == null) {
			salario = BigDecimal.ZERO;
		}
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public PrevidenciaEnum getPrevidencia() {
		return previdencia;
	}

	public void setPrevidencia(PrevidenciaEnum previdencia) {
		this.previdencia = previdencia;
	}

	public Boolean getOptanteTotal() {
		if (optanteTotal == null) {
			optanteTotal = Boolean.FALSE;
		}
		return optanteTotal;
	}

	public void setOptanteTotal(Boolean optanteTotal) {
		this.optanteTotal = optanteTotal;
	}

	public BigDecimal getFGTS() {
		if (FGTS == null) {
			FGTS = BigDecimal.ZERO;
		}
		return FGTS;
	}

	public void setFGTS(BigDecimal fGTS) {
		FGTS = fGTS;
	}

	public BigDecimal getDSR() {
		if (DSR == null) {
			DSR = BigDecimal.ZERO;
		}
		return DSR;
	}

	public void setDSR(BigDecimal dSR) {
		DSR = dSR;
	}

	public BigDecimal getSalarioFamilia() {
		if (salarioFamilia == null) {
			salarioFamilia = BigDecimal.ZERO;
		}
		return salarioFamilia;
	}

	public void setSalarioFamilia(BigDecimal salarioFamilia) {
		this.salarioFamilia = salarioFamilia;
	}

	public Integer getNumeroDependenteSalarioFamilia() {
		if (numeroDependenteSalarioFamilia == null) {
			numeroDependenteSalarioFamilia = 0;
		}
		return numeroDependenteSalarioFamilia;
	}

	public void setNumeroDependenteSalarioFamilia(Integer numeroDependenteSalarioFamilia) {
		this.numeroDependenteSalarioFamilia = numeroDependenteSalarioFamilia;
	}

	public BigDecimal getPrevidenciaPropria() {
		if (previdenciaPropria == null) {
			previdenciaPropria = BigDecimal.ZERO;
		}
		return previdenciaPropria;
	}

	public void setPrevidenciaPropria(BigDecimal previdenciaPropria) {
		this.previdenciaPropria = previdenciaPropria;
	}

	public BigDecimal getPlanoSaude() {
		if (planoSaude == null) {
			planoSaude = BigDecimal.ZERO;
		}
		return planoSaude;
	}

	public void setPlanoSaude(BigDecimal planoSaude) {
		this.planoSaude = planoSaude;
	}

	public BigDecimal getBaseCalculoINSS() {
		if (baseCalculoINSS == null) {
			baseCalculoINSS = BigDecimal.ZERO;
		}
		return baseCalculoINSS;
	}

	public void setBaseCalculoINSS(BigDecimal baseCalculoINSS) {
		this.baseCalculoINSS = baseCalculoINSS;
	}

	public BigDecimal getValorINSS() {
		if (valorINSS == null) {
			valorINSS = BigDecimal.ZERO;
		}
		return valorINSS;
	}

	public void setValorINSS(BigDecimal valorINSS) {
		this.valorINSS = valorINSS;
	}

	public BigDecimal getBaseCalculoIRRF() {
		if (baseCalculoIRRF == null) {
			baseCalculoIRRF = BigDecimal.ZERO;
		}
		return baseCalculoIRRF;
	}

	public void setBaseCalculoIRRF(BigDecimal baseCalculoIRRF) {
		this.baseCalculoIRRF = baseCalculoIRRF;
	}

	public BigDecimal getFaixa() {
		if (faixa == null) {
			faixa = BigDecimal.ZERO;
		}
		return faixa;
	}

	public void setFaixa(BigDecimal faixa) {
		this.faixa = faixa;
	}

	public BigDecimal getDedutivel() {
		if (dedutivel == null) {
			dedutivel = BigDecimal.ZERO;
		}
		return dedutivel;
	}

	public void setDedutivel(BigDecimal dedutivel) {
		this.dedutivel = dedutivel;
	}

	public Integer getNumerosDependentes() {
		if (numerosDependentes == null) {
			numerosDependentes = 0;
		}
		return numerosDependentes;
	}

	public void setNumerosDependentes(Integer numerosDependentes) {
		this.numerosDependentes = numerosDependentes;
	}

	public BigDecimal getValorDependente() {
		if (valorDependente == null) {
			valorDependente = BigDecimal.ZERO;
		}
		return valorDependente;
	}

	public void setValorDependente(BigDecimal valorDependente) {
		this.valorDependente = valorDependente;
	}

	public BigDecimal getValorIRRF() {
		if (valorIRRF == null) {
			valorIRRF = BigDecimal.ZERO;
		}
		return valorIRRF;
	}

	public void setValorIRRF(BigDecimal valorIRRF) {
		this.valorIRRF = valorIRRF;
	}

	public BigDecimal getInformeRendimento() {
		if (informeRendimento == null) {
			informeRendimento = BigDecimal.ZERO;
		}
		return informeRendimento;
	}

	public void setInformeRendimento(BigDecimal informeRendimento) {
		this.informeRendimento = informeRendimento;
	}

	public BigDecimal getRAIS() {
		if (RAIS == null) {
			RAIS = BigDecimal.ZERO;
		}
		return RAIS;
	}

	public void setRAIS(BigDecimal rAIS) {
		RAIS = rAIS;
	}

	public String getTipoLancamento() {
		if (tipoLancamento == null) {
			tipoLancamento = "";
		}
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getCargoAtual() {
		if (funcionarioCargo.getComissionado()) {
			return funcionarioCargo.getCargoAtual().getNome();
		} else {
			return funcionarioCargo.getCargo().getNome();
		}
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamentoVO() {
		if (templateLancamentoFolhaPagamentoVO == null)
			templateLancamentoFolhaPagamentoVO = new TemplateLancamentoFolhaPagamentoVO();
		return templateLancamentoFolhaPagamentoVO;
	}

	public void setTemplateLancamentoFolhaPagamentoVO(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO) {
		this.templateLancamentoFolhaPagamentoVO = templateLancamentoFolhaPagamentoVO;
	}

	public Boolean getLancadoAdiantamentoFerias() {
		if (lancadoAdiantamentoFerias == null) {
			lancadoAdiantamentoFerias = Boolean.FALSE;
		}
		return lancadoAdiantamentoFerias;
	}

	public void setLancadoAdiantamentoFerias(Boolean lancadoAdiantamentoFerias) {
		this.lancadoAdiantamentoFerias = lancadoAdiantamentoFerias;
	}

	public Boolean getLancadoReciboFerias() {
		if (lancadoReciboFerias == null) {
			lancadoReciboFerias = Boolean.FALSE;
		}
		return lancadoReciboFerias;
	}

	public void setLancadoReciboFerias(Boolean lancadoReciboFerias) {
		this.lancadoReciboFerias = lancadoReciboFerias;
	}

	public Collection<EventoFolhaPagamentoVO> montarDadosEventosPreenchidosDoContraChequeEvento() {
		List<EventoFolhaPagamentoVO> listaDeEventos = new ArrayList<>();
		EventoFolhaPagamentoVO evento;

		for (ContraChequeEventoVO contraChequeEventoVO : getContraChequeEventos()) {
			if (contraChequeEventoVO.getEventoFolhaPagamento().getSituacao().equals(AtivoInativoEnum.ATIVO)) {
				evento = contraChequeEventoVO.getEventoFolhaPagamento();
				evento.setReferencia(contraChequeEventoVO.getReferencia());
				evento.setContraChequeEventoVO(contraChequeEventoVO);
				evento.setValorInformado(contraChequeEventoVO.getValorInformado());
				evento.setInformadoManual(contraChequeEventoVO.getInformadoManual());

				if (evento.getValorInformado() || evento.getTipoLancamento().equals(TipoLancamentoFolhaPagamentoEnum.BASE_CALCULO)) {
					if (evento.getTipoLancamento().equals(TipoLancamentoFolhaPagamentoEnum.BASE_CALCULO)) {
						evento.setValorTemporario(contraChequeEventoVO.getValorReferencia());
					} else {
						evento.setValorTemporario(contraChequeEventoVO.recuperarValorDoEventoTratado());
					}
				}

				listaDeEventos.add(evento);
			}
		}

		return listaDeEventos;
	}

	public BigDecimal getValorIRRFFerias() {
		if (valorIRRFFerias == null)
			valorIRRFFerias = BigDecimal.ZERO;
		return valorIRRFFerias;
	}

	public void setValorIRRFFerias(BigDecimal valorIRRFFerias) {
		this.valorIRRFFerias = valorIRRFFerias;
	}

	public BigDecimal getBaseCalculoIRRFFerias() {
		if (baseCalculoIRRFFerias == null)
			baseCalculoIRRFFerias = BigDecimal.ZERO;
		return baseCalculoIRRFFerias;
	}

	public void setBaseCalculoIRRFFerias(BigDecimal baseCalculoIRRFFerias) {
		this.baseCalculoIRRFFerias = baseCalculoIRRFFerias;
	}
}