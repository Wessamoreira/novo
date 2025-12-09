package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;

public class ReciboFeriasVO extends SuperVO {

	private static final long serialVersionUID = -1006609218987542034L;
	
	private Integer codigo;
	private MarcacaoFeriasVO marcacaoFerias;
	private BigDecimal totalProvento;
	private BigDecimal totalDesconto;
	private BigDecimal totalReceber;

	private TipoRecebimentoEnum tipoRecebimento;
	private BigDecimal salario;
	private PrevidenciaEnum previdencia;
	private Boolean optanteTotal;
	
	private List<ReciboFeriasEventoVO> listaReciboEvento;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public BigDecimal getTotalProvento() {
		if (totalProvento == null)
			totalProvento = BigDecimal.ZERO;
		return totalProvento;
	}
	public void setTotalProvento(BigDecimal totalProvento) {
		this.totalProvento = totalProvento;
	}
	
	public BigDecimal getTotalDesconto() {
		if (totalDesconto == null)
			totalDesconto = BigDecimal.ZERO;
		return totalDesconto;
	}
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
	
	public BigDecimal getTotalReceber() {
		if (totalReceber == null)
			totalReceber = BigDecimal.ZERO;
		return totalReceber;
	}
	public void setTotalReceber(BigDecimal totalReceber) {
		this.totalReceber = totalReceber;
	}

	public TipoRecebimentoEnum getTipoRecebimento() {
		if (tipoRecebimento == null)
			tipoRecebimento = TipoRecebimentoEnum.MENSALISTA;
		return tipoRecebimento;
	}
	public void setTipoRecebimento(TipoRecebimentoEnum tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}
	public BigDecimal getSalario() {
		if (salario == null)
			salario = BigDecimal.ZERO;
		return salario;
	}
	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
	
	public PrevidenciaEnum getPrevidencia() {
		if (previdencia == null)
			previdencia = PrevidenciaEnum.INSS;
		return previdencia;
	}
	public void setPrevidencia(PrevidenciaEnum previdencia) {
		this.previdencia = previdencia;
	}
	
	public Boolean getOptanteTotal() {
		if (optanteTotal == null)
			optanteTotal = false;
		return optanteTotal;
	}
	public void setOptanteTotal(Boolean optanteTotal) {
		this.optanteTotal = optanteTotal;
	}
	public MarcacaoFeriasVO getMarcacaoFerias() {
		if (marcacaoFerias == null)
			marcacaoFerias = new MarcacaoFeriasVO();
		return marcacaoFerias;
	}
	public void setMarcacaoFerias(MarcacaoFeriasVO marcacaoFerias) {
		this.marcacaoFerias = marcacaoFerias;
	}
	public List<ReciboFeriasEventoVO> getListaReciboEvento() {
		if (listaReciboEvento == null)
			listaReciboEvento = new ArrayList<>();
		return listaReciboEvento;
	}
	public void setListaReciboEvento(List<ReciboFeriasEventoVO> listaReciboEvento) {
		this.listaReciboEvento = listaReciboEvento;
	}
	
	public void atualizarValoresDoCalculoNoRecibo(CalculoContraCheque calculoContraCheque) {
		setTipoRecebimento(calculoContraCheque.getFuncionarioCargoVO().getTipoRecebimento());
		setSalario(calculoContraCheque.getFuncionarioCargoVO().getSalario());
		setPrevidencia(calculoContraCheque.getFuncionarioCargoVO().getPrevidencia());
		setOptanteTotal(calculoContraCheque.getFuncionarioCargoVO().getOptanteTotal());
		
		setTotalProvento(calculoContraCheque.getProvento());
		setTotalDesconto(calculoContraCheque.getDesconto());
		setTotalReceber(calculoContraCheque.getProvento().subtract(calculoContraCheque.getDesconto()));
	}

	/**
	 * Retorna lista de eventos que estao no Recibo De Ferias
	 * 
	 * @return
	 */
	public Collection<EventoFolhaPagamentoVO> getEventosPreenchidosDoReciboDeFerias() {
		return getEventosPreenchidosDoReciboDeFeriasSetadosComInformacaoManual(false);
	}
	
	/**
	 * Retorna lista de eventos que estao no Recibo De Ferias
	 * 
	 * @return
	 */
	public Collection<EventoFolhaPagamentoVO> getEventosPreenchidosDoReciboDeFeriasSetadosComInformacaoManual(Boolean setarInformadoManual) {
		
		List<EventoFolhaPagamentoVO> listaDeEventos = new ArrayList<>();
		EventoFolhaPagamentoVO evento;
		
		for(ReciboFeriasEventoVO reciboFeriasEventoVO : getListaReciboEvento()) {
			
			evento = reciboFeriasEventoVO.getEvento();
			evento.setReferencia(reciboFeriasEventoVO.getReferencia());
			evento.setReciboFeriasEventoVO(reciboFeriasEventoVO);
			evento.setValorInformado(reciboFeriasEventoVO.getInformadoManual() || setarInformadoManual);
			
			if(evento.getValorInformado())
				evento.setValorTemporario(reciboFeriasEventoVO.getValorDoEventoTratado());
			
			listaDeEventos.add(evento);
		}
		
		return listaDeEventos;
	}
	
}