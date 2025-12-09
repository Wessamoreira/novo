package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEntidadeSindicalEnum;

public class SindicatoVO extends SuperVO {

	private static final long serialVersionUID = -3883353325940487642L;
	
	private Integer codigo;
	private ParceiroVO parceiroVO;
	private TipoEntidadeSindicalEnum tipoEntidadeSindical;
	private BigDecimal percentualDescontoVT;
	private EventoFolhaPagamentoVO eventoAdiantamentoFerias;
	private EventoFolhaPagamentoVO eventoDescontoAdiantamentoFerias;
	private EventoFolhaPagamentoVO eventoLancamentoFalta;
	private EventoFolhaPagamentoVO eventoDSRPerdida;
	private List<SindicatoMediaFeriasVO> mediaDasFerias;
	
	private List<SindicatoMedia13VO> media13;
	private List<SindicatoMediaRescisaoVO> sindicatoMediaRescisaoVOs;
	
	//Sprint 6
	private Boolean validarFaltas;
	private Boolean considerarFaltasFerias;
	private EventoFolhaPagamentoVO eventoDevolucaoFalta;

	private EventoFolhaPagamentoVO eventoPrimeiraParcela13;
	private EventoFolhaPagamentoVO eventoDescontoPrimeiraParcela13;
	
	public enum EnumCampoConsultaSindicato {
		SINDICATO, PARCEIRO;
	}

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null)
			parceiroVO = new ParceiroVO();
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public TipoEntidadeSindicalEnum getTipoEntidadeSocial() {
		if (tipoEntidadeSindical == null)
			tipoEntidadeSindical = TipoEntidadeSindicalEnum.SINDICATO;
		return tipoEntidadeSindical;
	}

	public void setTipoEntidadeSocial(TipoEntidadeSindicalEnum tipoEntidadeSocial) {
		this.tipoEntidadeSindical = tipoEntidadeSocial;
	}

	public BigDecimal getPercentualDescontoVT() {
		if (percentualDescontoVT == null)
			percentualDescontoVT = BigDecimal.ZERO;
		return percentualDescontoVT;
	}

	public void setPercentualDescontoVT(BigDecimal percentualDescontoVT) {
		this.percentualDescontoVT = percentualDescontoVT;
	}

	public EventoFolhaPagamentoVO getEventoAdiantamentoFerias() {
		if (eventoAdiantamentoFerias == null)
			eventoAdiantamentoFerias = new EventoFolhaPagamentoVO();
		return eventoAdiantamentoFerias;
	}

	public void setEventoAdiantamentoFerias(EventoFolhaPagamentoVO eventoAdiantamentoFerias) {
		this.eventoAdiantamentoFerias = eventoAdiantamentoFerias;
	}

	public EventoFolhaPagamentoVO getEventoDescontoAdiantamentoFerias() {
		if (eventoDescontoAdiantamentoFerias == null)
			eventoDescontoAdiantamentoFerias = new EventoFolhaPagamentoVO();
		return eventoDescontoAdiantamentoFerias;
	}

	public void setEventoDescontoAdiantamentoFerias(EventoFolhaPagamentoVO eventoDescontoAdiantamentoFerias) {
		this.eventoDescontoAdiantamentoFerias = eventoDescontoAdiantamentoFerias;
	}

	public EventoFolhaPagamentoVO getEventoLancamentoFalta() {
		if (eventoLancamentoFalta == null)
			eventoLancamentoFalta = new EventoFolhaPagamentoVO();
		return eventoLancamentoFalta;
	}

	public void setEventoLancamentoFalta(EventoFolhaPagamentoVO eventoLancamentoFalta) {
		this.eventoLancamentoFalta = eventoLancamentoFalta;
	}

	public EventoFolhaPagamentoVO getEventoDSRPerdida() {
		if (eventoDSRPerdida == null)
			eventoDSRPerdida = new EventoFolhaPagamentoVO();
		return eventoDSRPerdida;
	}

	public void setEventoDSRPerdida(EventoFolhaPagamentoVO eventoDSRPerdida) {
		this.eventoDSRPerdida = eventoDSRPerdida;
	}

	public List<SindicatoMediaFeriasVO> getMediaDasFerias() {
		if (mediaDasFerias == null)
			mediaDasFerias = new ArrayList<>();
		return mediaDasFerias;
	}

	public void setMediaDasFerias(List<SindicatoMediaFeriasVO> mediaDasFerias) {
		this.mediaDasFerias = mediaDasFerias;
	}

	public TipoEntidadeSindicalEnum getTipoEntidadeSindical() {
		if (tipoEntidadeSindical == null)
			tipoEntidadeSindical = TipoEntidadeSindicalEnum.SINDICATO;
		return tipoEntidadeSindical;
	}

	public void setTipoEntidadeSindical(TipoEntidadeSindicalEnum tipoEntidadeSindical) {
		this.tipoEntidadeSindical = tipoEntidadeSindical;
	}

	public Boolean getValidarFaltas() {
		if (validarFaltas == null)
			validarFaltas = false;
		return validarFaltas;
	}

	public void setValidarFaltas(Boolean validarFaltas) {
		this.validarFaltas = validarFaltas;
	}

	public Boolean getConsiderarFaltasFerias() {
		if (considerarFaltasFerias == null)
			considerarFaltasFerias = false;
		return considerarFaltasFerias;
	}

	public void setConsiderarFaltasFerias(Boolean considerarFaltasFerias) {
		this.considerarFaltasFerias = considerarFaltasFerias;
	}

	public EventoFolhaPagamentoVO getEventoDevolucaoFalta() {
		if (eventoDevolucaoFalta == null)
			eventoDevolucaoFalta = new EventoFolhaPagamentoVO();
		return eventoDevolucaoFalta;
	}

	public void setEventoDevolucaoFalta(EventoFolhaPagamentoVO eventoDevolucaoFalta) {
		this.eventoDevolucaoFalta = eventoDevolucaoFalta;
	}

	public EventoFolhaPagamentoVO getEventoPrimeiraParcela13() {
		if(eventoPrimeiraParcela13 == null)
			eventoPrimeiraParcela13 = new EventoFolhaPagamentoVO();
		
		return eventoPrimeiraParcela13;
	}

	public void setEventoPrimeiraParcela13(EventoFolhaPagamentoVO eventoPrimeiraParcela13) {
		this.eventoPrimeiraParcela13 = eventoPrimeiraParcela13;
	}

	public List<SindicatoMedia13VO> getMedia13() {
		if(media13 == null)
			media13 = new ArrayList<>();
		return media13;
	}

	public void setMedia13(List<SindicatoMedia13VO> media13) {
		this.media13 = media13;
	}

	public EventoFolhaPagamentoVO getEventoDescontoPrimeiraParcela13() {
		if (eventoDescontoPrimeiraParcela13 == null)
			eventoDescontoPrimeiraParcela13 = new EventoFolhaPagamentoVO();
		return eventoDescontoPrimeiraParcela13;
	}

	public void setEventoDescontoPrimeiraParcela13(EventoFolhaPagamentoVO eventoDescontoPrimeiraParcela13) {
		this.eventoDescontoPrimeiraParcela13 = eventoDescontoPrimeiraParcela13;
	}public List<SindicatoMediaRescisaoVO> getSindicatoMediaRescisaoVOs() {
		if (sindicatoMediaRescisaoVOs == null) {
			sindicatoMediaRescisaoVOs = new ArrayList<>();
		}
		return sindicatoMediaRescisaoVOs;
	}

	public void setSindicatoMediaRescisaoVOs(List<SindicatoMediaRescisaoVO> sindicatoMediaRescisaoVOs) {
		this.sindicatoMediaRescisaoVOs = sindicatoMediaRescisaoVOs;
	}}