package negocio.comuns.elastic;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.utilitarias.Uteis;

public class SincronismoElasticVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Date inicio;
	private Date fim;
	private Integer registros;
	private String mensagem;
	private Boolean ativa;
	
	public String getResumo() {
		StringBuilder resumo = new StringBuilder();
		if (getAtiva()) {
			resumo.append("<b>Em execução, iniciado em ").append(Uteis.getDataComHora(getInicio())).append("</b>");
		} else if (getRegistros().intValue() > 0) {
			resumo.append(getRegistros()).append(" registro(s) sincronizado(s) entre ").append(Uteis.getDataComHora(getInicio())).append(" e ").append(Uteis.getDataComHora(getFim()));
		} else if (getMensagem().isEmpty()) {
			resumo.append("Nenhum registro sincronizado em ").append(Uteis.getDataComHora(getInicio()));
		} else {
			resumo.append(Uteis.getDataComHora(getInicio())).append(" Falha: ").append(getMensagem());
		}
		return resumo.toString();
	}
	
	public Date getInicio() {
		return inicio;
	}
	
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	
	public Date getFim() {
		return fim;
	}
	
	public void setFim(Date fim) {
		this.fim = fim;
	}
	
	public Integer getRegistros() {
		if (registros == null) {
			registros = 0;
		}
		return registros;
	}
	
	public void setRegistros(Integer registros) {
		this.registros = registros;
	}
	
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public Boolean getAtiva() {
		if (ativa == null) {
			ativa = false;
		}
		return ativa;
	}
	
	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}
	
}
