/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

/**
 * Reponsável por manter os dados da entidade AgendaPessoaHorario. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see AgendaPessoa
 */
/**
 *
 * @author edigarjr
 */
public class AgendaPessoaHorarioVO extends SuperVO {

	private Integer codigo;
	private AgendaPessoaVO agendaPessoa;
	private Integer dia;
	private Integer mes;
	private Integer ano;
	private Integer quantidadeContatos;
	private Integer quantidadeTarefa;
	// Atributos transient para apresentar estatisticas na visao do consultor
	private Integer quantidadeTotalAgendas;
	private Integer quantidadeNovasAgendas;
	private Integer quantidadeNovasAgendasRealizadas;
	private Integer quantidadeNovasAgendasRealizadasComInsucesso;
	private Integer quantidadeNovasAgendasPendentes;
	private Integer quantidadeReagendas;
	private Integer quantidadeReagendasRealizadas;
	private Integer quantidadeReagendasRealizadasComInsucesso;
	private Integer quantidadeReagendasPendentes;
	// fim atributos transient´s
	private DiaSemana diaSemanaEnum;
	private Boolean isAtivo;
	private List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaPessoaHorarioVOs;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Integer quantidadeCancelados;

	/**
	 * Construtor padrão da classe <code>AgendaPessoaHorario</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public AgendaPessoaHorarioVO() {
		super();
	}

	public AgendaPessoaHorarioVO(AgendaPessoaVO agendaPessoa, Integer dia, Integer mes, Integer ano,
			DiaSemana diaSemana, Boolean ativo) {
		this.agendaPessoa = agendaPessoa;
		this.dia = dia;
		this.mes = mes;
		this.ano = ano;
		this.diaSemanaEnum = diaSemana;
		this.isAtivo = ativo;
	}

	public Integer getAno() {
		if (ano == null) {
			ano = 0;
		}
		return (ano);
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		if (mes == null) {
			mes = 0;
		}
		return (mes);
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getDia() {
		if (dia == null) {
			dia = 0;
		}
		return (dia);
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public AgendaPessoaVO getAgendaPessoa() {
		if (agendaPessoa == null) {
			agendaPessoa = new AgendaPessoaVO();
		}
		return (agendaPessoa);
	}

	public void setAgendaPessoa(AgendaPessoaVO agendaPessoa) {
		this.agendaPessoa = agendaPessoa;
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

	public List<CompromissoAgendaPessoaHorarioVO> getListaCompromissoAgendaPessoaHorarioVOs() {
		if (listaCompromissoAgendaPessoaHorarioVOs == null) {
			listaCompromissoAgendaPessoaHorarioVOs = new ArrayList<CompromissoAgendaPessoaHorarioVO>();
		}
		return listaCompromissoAgendaPessoaHorarioVOs;
	}

	public void setListaCompromissoAgendaPessoaHorarioVOs(
			List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaPessoaHorarioVOs) {
		this.listaCompromissoAgendaPessoaHorarioVOs = listaCompromissoAgendaPessoaHorarioVOs;
	}

	public DiaSemana getDiaSemanaEnum() {
		return diaSemanaEnum;
	}

	public void setDiaSemanaEnum(DiaSemana diaSemanaEnum) {
		this.diaSemanaEnum = diaSemanaEnum;
	}

	public Boolean getIsAtivo() {
		if (isAtivo == null) {
			isAtivo = false;
		}
		return isAtivo;
	}

	public void setIsAtivo(Boolean isAtivo) {
		this.isAtivo = isAtivo;
	}

	public Integer getQuantidadeContatos() {
		if (quantidadeContatos == null) {
			quantidadeContatos = 0;
		}
		return quantidadeContatos;
	}

	public void setQuantidadeContatos(Integer quantidadeContatos) {
		this.quantidadeContatos = quantidadeContatos;
	}

	public Integer getQuantidadeTarefa() {
		if (quantidadeTarefa == null) {
			quantidadeTarefa = 0;
		}
		return quantidadeTarefa;
	}

	public void setQuantidadeTarefa(Integer quantidadeTarefa) {
		this.quantidadeTarefa = quantidadeTarefa;
	}

	public String getCss_Dia() throws Exception {
		if (getDataCompromisso().compareTo(new Date()) == 0) {
			return "bg-success text-white";
		} else if (getDataCompromisso().compareTo(new Date()) > 0) {
			return "bg-white text-dark";
		} else {
			return "bg-gray text-dark";

		}
	}

	public Boolean getApresentarAdicionarCompromisso() throws Exception {
		if (getDataCompromisso().compareTo(new Date()) < 0) {
			return false;
		}
		return true;
	}

	public Boolean getApresentarNumeroContatos() throws Exception {
		if (getQuantidadeContatos() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getApresentarNumeroTarefas() throws Exception {
		if (getQuantidadeTarefa() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getApresentarVisualizarCompromissos() throws Exception {
		if (getQuantidadeContatos() == 0 && getQuantidadeTarefa() == 0) {
			return false;
		}
		return true;
	}

	public Date getDataCompromisso() throws Exception {
		if (getDia() == 0 || getMes() == 0 || getAno() == 0) {
			throw new Exception(
					"Não foi possivél gera data de cadastro, pois um dos campos dia , mes ou ano esta vazio.");
		}
		return Uteis.obterDataAvancadaPorDiaPorMesPorAno(getDia(), getMes(), getAno(), 0);
	}

	public String getDataCompromisso_Apresentar() throws Exception {
		return Uteis.getData(getDataCompromisso(), "dd/MM/yyyy");
	}

	public Boolean getIsApresentarDadoMes() {
		if (getQuantidadeContatos() > 0 || getQuantidadeTarefa() > 0) {
			return true;
		}
		return false;

	}

	public String getApresentarDiaMes_Texto() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDiaSemanaEnum().getDescricao().toLowerCase());
		sb.append(" ");
		if (getDia() < 10) {
			sb.append("0").append(getDia().toString());
		} else {
			sb.append(getDia().toString());
		}
		sb.append(" / ");
		if (getMes() < 10) {
			sb.append("0").append(getMes().toString());
		} else {
			sb.append(getMes().toString());
		}

		return sb.toString();

	}

	public String getApresentarDiaSemanaEnum() {
		return getDiaSemanaEnum().getDescricao().toLowerCase();
	}

	public String getApresentarDia() {
		return getDia().toString();

	}

	public Integer getOrdemApresentacao() {
		Integer a = 0;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(getAno().toString());
			if (getMes() < 10) {
				sb.append("0").append(getMes().toString());
			} else {
				sb.append(getMes().toString());
			}
			if (getDia() < 10) {
				sb.append("0").append(getDia().toString());
			} else {
				sb.append(getDia().toString());
			}
			a = Integer.parseInt(sb.toString());
		} finally {
			sb = null;
			return a;
		}
	}

	public Integer getQuantidadeNovasAgendas() {
		if (quantidadeNovasAgendas == null) {
			quantidadeNovasAgendas = 0;
		}
		return quantidadeNovasAgendas;
	}

	public void setQuantidadeNovasAgendas(Integer quantidadeNovasAgendas) {
		this.quantidadeNovasAgendas = quantidadeNovasAgendas;
	}

	public Integer getQuantidadeNovasAgendasRealizadas() {
		if (quantidadeNovasAgendasRealizadas == null) {
			quantidadeNovasAgendasRealizadas = 0;
		}
		return quantidadeNovasAgendasRealizadas;
	}

	public void setQuantidadeNovasAgendasRealizadas(Integer quantidadeNovasAgendasRealizadas) {
		this.quantidadeNovasAgendasRealizadas = quantidadeNovasAgendasRealizadas;
	}

	public Integer getQuantidadeNovasAgendasPendentes() {
		if (quantidadeNovasAgendasPendentes == null) {
			quantidadeNovasAgendasPendentes = 0;
		}
		return quantidadeNovasAgendasPendentes;
	}

	public void setQuantidadeNovasAgendasPendentes(Integer quantidadeNovasAgendasPendentes) {
		this.quantidadeNovasAgendasPendentes = quantidadeNovasAgendasPendentes;
	}

	public Integer getQuantidadeReagendas() {
		if (quantidadeReagendas == null) {
			quantidadeReagendas = 0;
		}
		return quantidadeReagendas;
	}

	public void setQuantidadeReagendas(Integer quantidadeReagendas) {
		this.quantidadeReagendas = quantidadeReagendas;
	}

	public Integer getQuantidadeReagendasRealizadas() {
		if (quantidadeReagendasRealizadas == null) {
			quantidadeReagendasRealizadas = 0;
		}
		return quantidadeReagendasRealizadas;
	}

	public void setQuantidadeReagendasRealizadas(Integer quantidadeReagendasRealizadas) {
		this.quantidadeReagendasRealizadas = quantidadeReagendasRealizadas;
	}

	public Integer getQuantidadeReagendasPendentes() {
		if (quantidadeReagendasPendentes == null) {
			quantidadeReagendasPendentes = 0;
		}
		return quantidadeReagendasPendentes;
	}

	public void setQuantidadeReagendasPendentes(Integer quantidadeReagendasPendentes) {
		this.quantidadeReagendasPendentes = quantidadeReagendasPendentes;
	}

	public Integer getQuantidadeTotalAgendas() {
		if (quantidadeTotalAgendas == null) {
			quantidadeTotalAgendas = 0;
		}
		return quantidadeTotalAgendas;
	}

	public void setQuantidadeTotalAgendas(Integer quantidadeTotalAgendas) {
		this.quantidadeTotalAgendas = quantidadeTotalAgendas;
	}

	public Integer getQuantidadeNovasAgendasRealizadasComInsucesso() {
		if (quantidadeNovasAgendasRealizadasComInsucesso == null) {
			quantidadeNovasAgendasRealizadasComInsucesso = 0;
		}
		return quantidadeNovasAgendasRealizadasComInsucesso;
	}

	public void setQuantidadeNovasAgendasRealizadasComInsucesso(Integer quantidadeNovasAgendasRealizadasComInsucesso) {
		this.quantidadeNovasAgendasRealizadasComInsucesso = quantidadeNovasAgendasRealizadasComInsucesso;
	}

	public Integer getQuantidadeReagendasRealizadasComInsucesso() {
		if (quantidadeReagendasRealizadasComInsucesso == null) {
			quantidadeReagendasRealizadasComInsucesso = 0;
		}
		return quantidadeReagendasRealizadasComInsucesso;
	}

	public void setQuantidadeReagendasRealizadasComInsucesso(Integer quantidadeReagendasRealizadasComInsucesso) {
		this.quantidadeReagendasRealizadasComInsucesso = quantidadeReagendasRealizadasComInsucesso;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Integer getQuantidadeCancelados() {
		if (quantidadeCancelados == null) {
			quantidadeCancelados = 0;
		}
		return quantidadeCancelados;
	}

	public void setQuantidadeCancelados(Integer quantidadeCancelados) {
		this.quantidadeCancelados = quantidadeCancelados;
	}

	public long getQtdeContatoNaoRealizadoDia() {
		return getListaCompromissoAgendaPessoaHorarioVOs().stream()
				.filter(t -> t.getPermitirAlteracaoCompromisso() && t.getCompromissoNaoRealizado()).count();
	}

	public long getQtdeContatoParalizadoDia() {
		return getListaCompromissoAgendaPessoaHorarioVOs().stream().filter(t -> t.getCompromissoParalizado()).count();
	}

	public long getQtdeContatoConcluidoDia() {
		return getListaCompromissoAgendaPessoaHorarioVOs().stream()
				.filter(t -> !t.getPermitirAlteracaoCompromisso() && t.getRealizado()).count();
	}

	public long getQtdeContatoReagendadoDia() {
		return getListaCompromissoAgendaPessoaHorarioVOs().stream()
				.filter(t -> t.getCompromissoRealizadoComRemarcacao() && t.getReagendado()).count();
	}

	public long getQtdeContatoCanceladoDia() {
		return getListaCompromissoAgendaPessoaHorarioVOs().stream().filter(t -> t.getCancelado()).count();
	}

	private Integer qtdeCompromisso;
	private Integer qtdeCompromissoNaoRealizado;
	private Integer qtdeCompromissoARealizar;
	private Integer qtdeCompromissoConcluido;
	private Integer qtdeCompromissoReagendado;
	private Integer qtdeCompromissoCancelado;
	private Integer qtdeCompromissoParalizado;

	public Integer getQtdeCompromisso() {
		if (qtdeCompromisso == null) {
			qtdeCompromisso = 0;
		}
		return qtdeCompromisso;
	}

	public void setQtdeCompromisso(Integer qtdeCompromisso) {
		this.qtdeCompromisso = qtdeCompromisso;
	}

	public Integer getQtdeCompromissoConcluido() {
		if (qtdeCompromissoConcluido == null) {
			qtdeCompromissoConcluido = 0;
		}
		return qtdeCompromissoConcluido;
	}

	public void setQtdeCompromissoConcluido(Integer qtdeCompromissoConcluido) {
		this.qtdeCompromissoConcluido = qtdeCompromissoConcluido;
	}

	public Integer getQtdeCompromissoReagendado() {
		if (qtdeCompromissoReagendado == null) {
			qtdeCompromissoReagendado = 0;
		}
		return qtdeCompromissoReagendado;
	}

	public void setQtdeCompromissoReagendado(Integer qtdeCompromissoReagendado) {
		this.qtdeCompromissoReagendado = qtdeCompromissoReagendado;
	}

	public Integer getQtdeCompromissoCancelado() {
		if (qtdeCompromissoCancelado == null) {
			qtdeCompromissoCancelado = 0;
		}
		return qtdeCompromissoCancelado;
	}

	public void setQtdeCompromissoCancelado(Integer qtdeCompromissoCancelado) {
		this.qtdeCompromissoCancelado = qtdeCompromissoCancelado;
	}

	public Integer getQtdeCompromissoParalizado() {
		if (qtdeCompromissoParalizado == null) {
			qtdeCompromissoParalizado = 0;
		}
		return qtdeCompromissoParalizado;
	}

	public void setQtdeCompromissoParalizado(Integer qtdeCompromissoParalizado) {
		this.qtdeCompromissoParalizado = qtdeCompromissoParalizado;
	}

	public Integer getQtdeCompromissoNaoRealizado() {
		if (qtdeCompromissoNaoRealizado == null) {
			qtdeCompromissoNaoRealizado = 0;
		}
		return qtdeCompromissoNaoRealizado;
	}

	public void setQtdeCompromissoNaoRealizado(Integer qtdeCompromissoNaoRealizado) {
		this.qtdeCompromissoNaoRealizado = qtdeCompromissoNaoRealizado;
	}

	public Integer getQtdeCompromissoARealizar() {
		if (qtdeCompromissoARealizar == null) {
			qtdeCompromissoARealizar = 0;
		}
		return qtdeCompromissoARealizar;
	}

	public void setQtdeCompromissoARealizar(Integer qtdeCompromissoARealizar) {
		this.qtdeCompromissoARealizar = qtdeCompromissoARealizar;
	}

}
