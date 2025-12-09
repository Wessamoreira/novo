package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade
 * EventoValeTransporteFuncionarioCargoVO. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em
 * memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EventoValeTransporteFuncionarioCargoVO extends SuperVO {

	private static final long serialVersionUID = -670487501327595545L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private ParametroValeTransporteVO parametroValeTransporte;
	private Integer numeroViagensDia;
	private Integer diasUteisMes;
	
	//Campo removido dessa tabela e transportado para a tabela de ParametroValeTransporte
	@Deprecated
	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	private Boolean utilizaSalarioNominal;
	private Integer numeroViagensMeioExpediente;
	private Integer quantidadeViagensMeioExpediente;
	
	//calculado dinamicamente
	private BigDecimal valorAReceber;
	private Integer quantidadeDeViagens;

	// Transiente
	private Boolean itemEmEdicao;

	public enum EnumCampoConsultaSalarioComposto {
		FUNCIONARIO, MATRICULA_CARGO, MATRICULA_FUNCIONARIO, CARGO;
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

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public ParametroValeTransporteVO getParametroValeTransporte() {
		if (parametroValeTransporte == null) {
			parametroValeTransporte = new ParametroValeTransporteVO();
		}
		return parametroValeTransporte;
	}

	public void setParametroValeTransporte(ParametroValeTransporteVO parametroValeTransporte) {
		this.parametroValeTransporte = parametroValeTransporte;
	}

	public Integer getNumeroViagensDia() {
		if (numeroViagensDia == null) {
			numeroViagensDia = 0;
		}
		return numeroViagensDia;
	}

	public void setNumeroViagensDia(Integer numeroViagensDia) {
		this.numeroViagensDia = numeroViagensDia;
	}

	public Integer getDiasUteisMes() {
		if (diasUteisMes == null) {
			diasUteisMes = 0;
		}
		return diasUteisMes;
	}

	public void setDiasUteisMes(Integer diasUteisMes) {
		this.diasUteisMes = diasUteisMes;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if (eventoFolhaPagamento == null) {
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = Boolean.FALSE;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public Boolean getUtilizaSalarioNominal() {
		if(utilizaSalarioNominal == null)
			utilizaSalarioNominal = false;
		return utilizaSalarioNominal;
	}

	public void setUtilizaSalarioNominal(Boolean utilizaSalarioNominal) {
		this.utilizaSalarioNominal = utilizaSalarioNominal;
	}

	public Integer getNumeroViagensMeioExpediente() {
		if (numeroViagensMeioExpediente == null) {
			numeroViagensMeioExpediente = 0;
		}
		return numeroViagensMeioExpediente;
	}

	public void setNumeroViagensMeioExpediente(Integer numeroViagensMeioExpediente) {
		this.numeroViagensMeioExpediente = numeroViagensMeioExpediente;
	}

	public Integer getQuantidadeViagensMeioExpediente() {
		if(quantidadeViagensMeioExpediente == null) {
			quantidadeViagensMeioExpediente = 0;
		}
		return quantidadeViagensMeioExpediente;
	}

	public void setQuantidadeViagensMeioExpediente(Integer quantidadeViagensMeioExpediente) {
		this.quantidadeViagensMeioExpediente = quantidadeViagensMeioExpediente;
	}

	public BigDecimal getValorAReceber() {

		BigDecimal numeroViagensIntegral = new BigDecimal(getNumeroViagensDia()).multiply(new BigDecimal(getDiasUteisMes()));
		BigDecimal numeroViagensMeioExpediente = new BigDecimal(getNumeroViagensMeioExpediente()).multiply(new BigDecimal(getQuantidadeViagensMeioExpediente()));

		valorAReceber = numeroViagensIntegral.add(numeroViagensMeioExpediente);

		return valorAReceber = valorAReceber.multiply(getParametroValeTransporte().getValor());
	}

	public void setValorAReceber(BigDecimal valorAReceber) {
		this.valorAReceber = valorAReceber;
	}

	public Integer getQuantidadeDeViagens() {
		
		Integer numeroViagensIntegral = getNumeroViagensDia() * getDiasUteisMes();
		Integer numeroViagensMeioExpediente = getNumeroViagensMeioExpediente() * getQuantidadeViagensMeioExpediente();
			
		quantidadeDeViagens = numeroViagensIntegral + numeroViagensMeioExpediente;
		
		return quantidadeDeViagens;
			
	}
		
	public void setQuantidadeDeViagens(Integer quantidadeDeViagens) {
		this.quantidadeDeViagens = quantidadeDeViagens;
	}
}