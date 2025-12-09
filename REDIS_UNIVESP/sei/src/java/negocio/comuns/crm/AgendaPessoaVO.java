package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.enumerador.TipoVisaoAgendaEnum;

/**
 * Reponsável por manter os dados da entidade AgendaPessoa. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class AgendaPessoaVO extends SuperVO {

    private Integer codigo;
    /** Atributo responsável por manter os objetos da classe <code>AgendaPessoaHorario</code>. */
    private List<AgendaPessoaHorarioVO> agendaPessoaHorarioVOs;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.*/
    protected PessoaVO pessoa;
    protected TipoVisaoAgendaEnum tipoVisaoAgenda;
    protected Integer qtdContatosPendentesNaoIniciados;
    protected Integer qtdContatosPendentesNaoFinalizados;
    
    //Atributos transient para apresentar estatisticas na visao do consultor
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

    /**
     * Construtor padrão da classe <code>AgendaPessoa</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public AgendaPessoaVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (<code>AgendaPessoa</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (<code>AgendaPessoa</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>AgendaPessoaHorario</code>. */
    public List<AgendaPessoaHorarioVO> getAgendaPessoaHorarioVOs() {
        if (agendaPessoaHorarioVOs == null) {
            agendaPessoaHorarioVOs = new ArrayList(0);
        }
        return (agendaPessoaHorarioVOs);
    }

    /** Define Atributo responsável por manter os objetos da classe <code>AgendaPessoaHorario</code>. */
    public void setAgendaPessoaHorarioVOs(List<AgendaPessoaHorarioVO> agendaPessoaHorarioVOs) {
        this.agendaPessoaHorarioVOs = agendaPessoaHorarioVOs;
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

    public TipoVisaoAgendaEnum getTipoVisaoAgenda() {
        if (tipoVisaoAgenda == null) {
            tipoVisaoAgenda = TipoVisaoAgendaEnum.DIA;
        }
        return tipoVisaoAgenda;
    }

    public void setTipoVisaoAgenda(TipoVisaoAgendaEnum tipoVisaoAgenda) {
        this.tipoVisaoAgenda = tipoVisaoAgenda;
    }

    public Boolean getIsVisaoAgendaDia() {
        if (getTipoVisaoAgenda().equals(TipoVisaoAgendaEnum.DIA)) {
            return true;
        }
        return false;
    }

    public Boolean getIsVisaoAgendaSemana() {
        if (getTipoVisaoAgenda().equals(TipoVisaoAgendaEnum.SEMANA)) {
            return true;
        }
        return false;
    }

    public Boolean getIsVisaoAgendaMes() {
        if (getTipoVisaoAgenda().equals(TipoVisaoAgendaEnum.MES)) {
            return true;
        }
        return false;
    }

    public Integer getQtdContatosPendentesNaoFinalizados() {
        if (qtdContatosPendentesNaoFinalizados == null) {
            qtdContatosPendentesNaoFinalizados = 0;
        }
        return qtdContatosPendentesNaoFinalizados;
    }

    public void setQtdContatosPendentesNaoFinalizados(Integer qtdContatosPendentesNaoFinalizados) {
        this.qtdContatosPendentesNaoFinalizados = qtdContatosPendentesNaoFinalizados;
    }

    public Integer getQtdContatosPendentesNaoIniciados() {
        if (qtdContatosPendentesNaoIniciados == null) {
            qtdContatosPendentesNaoIniciados = 0;
        }
        return qtdContatosPendentesNaoIniciados;
    }

    public void setQtdContatosPendentesNaoIniciados(Integer qtdContatosPendentesNaoIniciados) {
        this.qtdContatosPendentesNaoIniciados = qtdContatosPendentesNaoIniciados;
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
 	
}