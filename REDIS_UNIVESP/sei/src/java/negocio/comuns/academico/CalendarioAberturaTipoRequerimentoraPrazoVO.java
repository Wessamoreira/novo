package negocio.comuns.academico;

import java.text.SimpleDateFormat;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;

/**
 * Reponsável por manter os dados da entidade Arquivo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CalendarioAberturaTipoRequerimentoraPrazoVO extends SuperVO {

	private Integer codigo;
	private TipoRequerimentoVO tipoRequerimentoVO;
	// private CalendarioAberturaRequerimentoVO
	// calendarioAberturaRequerimentoVO;
	private Date dataInicio;
	private Date dataFim;

	public CalendarioAberturaTipoRequerimentoraPrazoVO() {
		super();

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

	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if (tipoRequerimentoVO == null) {
			tipoRequerimentoVO = new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}

	/*
	 * public CalendarioAberturaRequerimentoVO
	 * getCalendarioAberturaRequerimentoVO() { if
	 * (calendarioAberturaRequerimentoVO == null) {
	 * calendarioAberturaRequerimentoVO = new
	 * CalendarioAberturaRequerimentoVO(); } return
	 * calendarioAberturaRequerimentoVO; }
	 * 
	 * public void
	 * setCalendarioAberturaRequerimentoVO(CalendarioAberturaRequerimentoVO
	 * calendarioAberturaRequerimentoVO) { this.calendarioAberturaRequerimentoVO
	 * = calendarioAberturaRequerimentoVO; }
	 */

	public Date getDataInicio() {

		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {

		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getDataInicoApresentar() {
		String data = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (this.getDataInicio() != null) {
				data = sdf.format(this.getDataInicio()).toString();
			}
			return data;
		} catch (Exception e) {
			return data;
		}
	}

	public String getDataFimApresentar() {
		String data = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (this.getDataFim() != null) {
				data = sdf.format(this.getDataFim()).toString();
			}
			return data;

		} catch (Exception e) {
			return data;
		}
	}

	public String getSituacao() {
		String situacao = "";

		if (dataInicio != null && dataFim != null) {
			if (dataFim.before(new Date())) {
				situacao = "Vencido";
			}
			if (dataFim.after(new Date()) || dataFim.equals(new Date())) {
				situacao = "No Prazo";
			}
			if (dataInicio.after(new Date())) {
				situacao = "Aguardando Inicio";
			}

		}
		return situacao;
	}
	
	public Boolean getSituacaoTipoRequerimentoInativo() {
		return getTipoRequerimentoVO().getSituacao().equals("IN");	
	}

}
