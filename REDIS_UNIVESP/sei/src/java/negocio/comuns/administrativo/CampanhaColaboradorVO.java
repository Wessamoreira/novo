package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;

public class CampanhaColaboradorVO extends SuperVO {

    private Integer codigo;
    private CampanhaVO campanha;
    private FuncionarioCargoVO funcionarioCargoVO;
    private Integer qtdContato;
    private Integer qtdSucesso;
    private Integer qtdCaptacao;
    //Campos usado somente para apresentar dados na tela.
    private Integer qtdCompromissoCampanha;
    private Integer qtdCompromissoPeriodo;
    private List<CampanhaColaboradorCursoVO> listaCampanhaColaboradorCursoVOs;
    private String horaInicioGerarAgenda;
    private String horaFinalGerarAgenda;
    private String horaInicioIntervalo;
    private String horaFimIntervalo;
    
    
    // INICIO CAMPOS TRANSIENTES
    /**
     * Campos utilizados para controlar a geracao de agenda para o consultor
     */
    private Date dataUltimaAgendaGerada;
    private Date dataGeracaoProximaAgenda;
    private String horaGeracaoProximaAgenda;
    private Integer numeroAgendasGeradasParaData; 
    private TipoDistribuicaoProspectCampanhaPublicoAlvoEnum TipoDistribuicaoProspectCampanhaPublicoAlvo;
	private Integer quantidadeProspectInicouAgenda;
	private Integer quantidadeProspectSemAgenda;
	private Integer quantidadeProspectComAgendaNaoRealizada;
	private List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaIniciouCampanhaVOs;
	private List<CampanhaPublicoAlvoProspectVO> listaProspectSemAgendaVOs;
	private List<CampanhaPublicoAlvoProspectVO> listaProspectComAgendaNaoRealizadaVOs;
    // FIM DOS CAMPOS TRANSIENTES

    
    public CampanhaColaboradorVO(CampanhaVO campanha) {
        super();
        this.horaInicioGerarAgenda = campanha.getHoraInicial();
        this.horaFinalGerarAgenda = campanha.getHoraFinal();
        this.horaInicioIntervalo = campanha.getHoraInicioIntervalo();
        this.horaFimIntervalo = campanha.getHoraFimIntervalo();
    }
    
    public CampanhaColaboradorVO() {
        super();
    }
    
    public CampanhaVO getCampanha() {
        if (campanha == null) {
            campanha = new CampanhaVO();
        }
        return campanha;
    }

    public void setCampanha(CampanhaVO campanha) {
        this.campanha = campanha;
    }

    public Integer getQtdContato() {
        if (qtdContato == null) {
            qtdContato = 0;
        }
        return qtdContato;
    }

    public void setQtdContato(Integer qtdContatos) {
        this.qtdContato = qtdContatos;
    }

    public Integer getQtdSucesso() {
        if (qtdSucesso == null) {
            qtdSucesso = 0;
        }
        return qtdSucesso;
    }

    public void setQtdSucesso(Integer qtdSucesso) {
        this.qtdSucesso = qtdSucesso;
    }

    public Integer getQtdCaptacao() {
        if (qtdCaptacao == null) {
            qtdCaptacao = 0;
        }
        return qtdCaptacao;
    }

    public void setQtdCaptacao(Integer qtdCaptacao) {
        this.qtdCaptacao = qtdCaptacao;
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

    public FuncionarioCargoVO getFuncionarioCargoVO() {
        if (funcionarioCargoVO == null) {
            funcionarioCargoVO = new FuncionarioCargoVO();
        }
        return funcionarioCargoVO;
    }

    public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
        this.funcionarioCargoVO = funcionarioCargoVO;
    }

    public Integer getQtdCompromissoCampanha() {
        if (qtdCompromissoCampanha == null) {
            qtdCompromissoCampanha = 0;
        }
        return qtdCompromissoCampanha;
    }

    public void setQtdCompromissoCampanha(Integer qtdCompromissoCampanha) {
        this.qtdCompromissoCampanha = qtdCompromissoCampanha;
    }

    public Integer getQtdCompromissoPeriodo() {
        if (qtdCompromissoPeriodo == null) {
            qtdCompromissoPeriodo = 0;
        }
        return qtdCompromissoPeriodo;
    }

    public void setQtdCompromissoPeriodo(Integer qtdCompromissoPeriodo) {
        this.qtdCompromissoPeriodo = qtdCompromissoPeriodo;
    }

    public List<CampanhaColaboradorCursoVO> getListaCampanhaColaboradorCursoVOs() {
        if (listaCampanhaColaboradorCursoVOs == null) {
            listaCampanhaColaboradorCursoVOs = new ArrayList<CampanhaColaboradorCursoVO>(0);
        }
        return listaCampanhaColaboradorCursoVOs;
    }

    public void setListaCampanhaColaboradorCursoVOs(List<CampanhaColaboradorCursoVO> listaCampanhaColaboradorCursoVOs) {
        this.listaCampanhaColaboradorCursoVOs = listaCampanhaColaboradorCursoVOs;
    }

    /**
     * @return the dataGeracaoProximaAgenda
     */
    public Date getDataGeracaoProximaAgenda() {
        return dataGeracaoProximaAgenda;
    }

    /**
     * @param dataGeracaoProximaAgenda the dataGeracaoProximaAgenda to set
     */
    public void setDataGeracaoProximaAgenda(Date dataGeracaoProximaAgenda) {
        this.dataGeracaoProximaAgenda = dataGeracaoProximaAgenda;
    }

    /**
     * @return the horaGeracaoProximaAgenda
     */
    public String getHoraGeracaoProximaAgenda() {
        return horaGeracaoProximaAgenda;
    }

    /**
     * @param horaGeracaoProximaAgenda the horaGeracaoProximaAgenda to set
     */
    public void setHoraGeracaoProximaAgenda(String horaGeracaoProximaAgenda) {
        this.horaGeracaoProximaAgenda = horaGeracaoProximaAgenda;
    }

    /**
     * @return the numeroAgendasGeradasParaData
     */
    public Integer getNumeroAgendasGeradasParaData() {
        if (numeroAgendasGeradasParaData == null) {
            numeroAgendasGeradasParaData = 0;
        }
        return numeroAgendasGeradasParaData;
    }

    /**
     * @param numeroAgendasGeradasParaData the numeroAgendasGeradasParaData to set
     */
    public void setNumeroAgendasGeradasParaData(Integer numeroAgendasGeradasParaData) {
        this.numeroAgendasGeradasParaData = numeroAgendasGeradasParaData;
    }

    /**
     * @return the horaInicioGerarAgenda
     */
    public String getHoraInicioGerarAgenda() {
        if (horaInicioGerarAgenda == null) {
            horaInicioGerarAgenda = "08:00";
        }
        return horaInicioGerarAgenda;
    }

    /**
     * @param horaInicioGerarAgenda the horaInicioGerarAgenda to set
     */
    public void setHoraInicioGerarAgenda(String horaInicioGerarAgenda) {
        this.horaInicioGerarAgenda = horaInicioGerarAgenda;
    }

    /**
     * @return the horaFinalGerarAgenda
     */
    public String getHoraFinalGerarAgenda() {
        if (horaFinalGerarAgenda == null) {
            horaFinalGerarAgenda = "18:00";
        }
        return horaFinalGerarAgenda;
    }

    /**
     * @param horaFinalGerarAgenda the horaFinalGerarAgenda to set
     */
    public void setHoraFinalGerarAgenda(String horaFinalGerarAgenda) {
        this.horaFinalGerarAgenda = horaFinalGerarAgenda;
    }

/**
     * @return the horaInicioIntervalo
     */
    public String getHoraInicioIntervalo() {
        if (horaInicioIntervalo == null) {
            horaInicioIntervalo = "12:00";
        }
        return horaInicioIntervalo;
    }

    /**
     * @param horaInicioIntervalo the horaInicioIntervalo to set
     */
    public void setHoraInicioIntervalo(String horaInicioIntervalo) {
        this.horaInicioIntervalo = horaInicioIntervalo;
    }

    /**
     * @return the horaFimIntervalo
     */
    public String getHoraFimIntervalo() {
        if (horaFimIntervalo == null) {
            horaFimIntervalo = "14:00";
        }
        return horaFimIntervalo;
    }

    /**
     * @param horaFimIntervalo the horaFimIntervalo to set
     */
    public void setHoraFimIntervalo(String horaFimIntervalo) {
        this.horaFimIntervalo = horaFimIntervalo;
    }

    /**
     * @return the dataUltimaAgendaGerada
     */
    public Date getDataUltimaAgendaGerada() {
        if (dataUltimaAgendaGerada == null) {
            dataUltimaAgendaGerada = new Date();
        }
        return dataUltimaAgendaGerada;
    }

    /**
     * @param dataUltimaAgendaGerada the dataUltimaAgendaGerada to set
     */
    public void setDataUltimaAgendaGerada(Date dataUltimaAgendaGerada) {
        this.dataUltimaAgendaGerada = dataUltimaAgendaGerada;
    }
    
    public TipoDistribuicaoProspectCampanhaPublicoAlvoEnum getTipoDistribuicaoProspectCampanhaPublicoAlvo() {
		if (TipoDistribuicaoProspectCampanhaPublicoAlvo == null) {
			TipoDistribuicaoProspectCampanhaPublicoAlvo = TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR;
		}
		return TipoDistribuicaoProspectCampanhaPublicoAlvo;
	}

	public void setTipoDistribuicaoProspectCampanhaPublicoAlvo(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum tipoDistribuicaoProspectCampanhaPublicoAlvo) {
		TipoDistribuicaoProspectCampanhaPublicoAlvo = tipoDistribuicaoProspectCampanhaPublicoAlvo;
	}

	public Integer getQuantidadeProspectInicouAgenda() {
		if (quantidadeProspectInicouAgenda == null) {
			quantidadeProspectInicouAgenda = 0;
		}
		return quantidadeProspectInicouAgenda;
	}

	public void setQuantidadeProspectInicouAgenda(Integer quantidadeProspectInicouAgenda) {
		this.quantidadeProspectInicouAgenda = quantidadeProspectInicouAgenda;
	}

	public Integer getQuantidadeProspectSemAgenda() {
		if (quantidadeProspectSemAgenda == null) {
			quantidadeProspectSemAgenda = 0;
		}
		return quantidadeProspectSemAgenda;
	}

	public void setQuantidadeProspectSemAgenda(Integer quantidadeProspectSemAgenda) {
		this.quantidadeProspectSemAgenda = quantidadeProspectSemAgenda;
	}

	public Integer getQuantidadeProspectComAgendaNaoRealizada() {
		if (quantidadeProspectComAgendaNaoRealizada == null) {
			quantidadeProspectComAgendaNaoRealizada = 0;
		}
		return quantidadeProspectComAgendaNaoRealizada;
	}

	public void setQuantidadeProspectComAgendaNaoRealizada(Integer quantidadeProspectComAgendaNaoRealizada) {
		this.quantidadeProspectComAgendaNaoRealizada = quantidadeProspectComAgendaNaoRealizada;
	}
	
	public String getNomeConsultor() {
		return getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome();
	}

	public List<CompromissoAgendaPessoaHorarioVO> getListaCompromissoAgendaIniciouCampanhaVOs() {
		if (listaCompromissoAgendaIniciouCampanhaVOs == null) {
			listaCompromissoAgendaIniciouCampanhaVOs = new ArrayList<CompromissoAgendaPessoaHorarioVO>(0);
		}
		return listaCompromissoAgendaIniciouCampanhaVOs;
	}

	public void setListaCompromissoAgendaIniciouCampanhaVOs(List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaIniciouCampanhaVOs) {
		this.listaCompromissoAgendaIniciouCampanhaVOs = listaCompromissoAgendaIniciouCampanhaVOs;
	}

	public List<CampanhaPublicoAlvoProspectVO> getListaProspectSemAgendaVOs() {
		if (listaProspectSemAgendaVOs == null) {
			listaProspectSemAgendaVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		}
		return listaProspectSemAgendaVOs;
	}

	public void setListaProspectSemAgendaVOs(List<CampanhaPublicoAlvoProspectVO> listaProspectSemAgendaVOs) {
		this.listaProspectSemAgendaVOs = listaProspectSemAgendaVOs;
	}

	public List<CampanhaPublicoAlvoProspectVO> getListaProspectComAgendaNaoRealizadaVOs() {
		if (listaProspectComAgendaNaoRealizadaVOs == null) {
			listaProspectComAgendaNaoRealizadaVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		}
		return listaProspectComAgendaNaoRealizadaVOs;
	}

	public void setListaProspectComAgendaNaoRealizadaVOs(List<CampanhaPublicoAlvoProspectVO> listaProspectComAgendaNaoRealizadaVOs) {
		this.listaProspectComAgendaNaoRealizadaVOs = listaProspectComAgendaNaoRealizadaVOs;
	}
}
