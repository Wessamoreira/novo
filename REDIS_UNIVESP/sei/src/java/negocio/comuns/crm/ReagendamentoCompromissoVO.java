package negocio.comuns.crm;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class ReagendamentoCompromissoVO extends SuperVO {

	private Integer codigo;
	private Date dataModificacaoReagendamento;
	private Date dataInicioCompromisso;
	private Date dataReagendamentoCompromisso;
	private Integer compromissoAgendaPessoaHorario;
	private Integer agendaPessoaHorario;
	private Integer campanha;
	private String responsavelReagendamento;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Date getDataModificacaoReagendamento() {
		if (dataModificacaoReagendamento == null) {
			dataModificacaoReagendamento = new Date();
		}
		return dataModificacaoReagendamento;
	}
	public void setDataModificacaoReagendamento(Date dataModificacaoReagendamento) {
		this.dataModificacaoReagendamento = dataModificacaoReagendamento;
	}
	public Date getDataInicioCompromisso() {
		if (dataInicioCompromisso == null) {
			dataInicioCompromisso = new Date();
		}
		return dataInicioCompromisso;
	}
	public void setDataInicioCompromisso(Date dataInicioCompromisso) {
		this.dataInicioCompromisso = dataInicioCompromisso;
	}
	public Date getDataReagendamentoCompromisso() {
		if (dataReagendamentoCompromisso == null) {
			dataReagendamentoCompromisso = new Date();
		}
		return dataReagendamentoCompromisso;
	}
	public void setDataReagendamentoCompromisso(Date dataReagendamentoCompromisso) {
		this.dataReagendamentoCompromisso = dataReagendamentoCompromisso;
	}
	public Integer getCompromissoAgendaPessoaHorario() {
		if (compromissoAgendaPessoaHorario == null) {
			compromissoAgendaPessoaHorario = 0;
		}
		return compromissoAgendaPessoaHorario;
	}
	public void setCompromissoAgendaPessoaHorario(Integer compromissoAgendaPessoaHorario) {
		this.compromissoAgendaPessoaHorario = compromissoAgendaPessoaHorario;
	}
	public Integer getAgendaPessoaHorario() {
		if (agendaPessoaHorario == null) {
			agendaPessoaHorario = 0;
		}
		return agendaPessoaHorario;
	}
	public void setAgendaPessoaHorario(Integer agendaPessoaHorario) {
		this.agendaPessoaHorario = agendaPessoaHorario;
	}
	public Integer getCampanha() {
		if (campanha == null) {
			campanha = 0;
		}
		return campanha;
	}
	public void setCampanha(Integer campanha) {
		this.campanha = campanha;
	}
	public String getResponsavelReagendamento() {
		if (responsavelReagendamento == null) {
			responsavelReagendamento = "";
		}
		return responsavelReagendamento;
	}
	public void setResponsavelReagendamento(String responsavelReagendamento) {
		this.responsavelReagendamento = responsavelReagendamento;
	}
	
	
	
}
