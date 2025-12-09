package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.enumerador.TipoInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade InteracaoWorkflow. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class InteracaoWorkflowVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3670440592710269980L;
	private Integer codigo;
    private TipoMidiaCaptacaoVO tipoMidia;
    private MotivoInsucessoVO motivoInsucesso;
    private String observacao;
    private String motivo;
    private TipoInteracaoEnum tipoInteracao;
//    private Integer percentualEfetivaVendaHistorica;
    private EtapaWorkflowVO etapaWorkflow;
//    private Long tempoMinimo;
//    private Long tempoMaximo;
    private String horaInicio;
    private String horaTermino;
    private String tempoDecorrido;
    private Date dataInicio;
    private Date dataTermino;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Prospects </code>.*/
    protected ProspectsVO prospect;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Campanha </code>.*/
    protected CampanhaVO campanha;
    /** Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.*/
    protected UnidadeEnsinoVO unidadeEnsino;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Turno </code>.*/
    protected TurnoVO turno;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Workflow </code>.*/
    protected WorkflowVO workflow;
    /** Atributo responsável por manter o objeto relacionado da classe <code>CompromissoAgendaPessoaHorario </code>.*/
    protected CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorario;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Funcionario </code>.*/
    protected UsuarioVO responsavel;
    // Controle de tela
    private Boolean gravada;
    
    private MatriculaVO matriculaVO;
    
    /** Atributo transient */
	protected CompromissoAgendaPessoaHorarioVO reagendarCompromisso;
	private List<PessoaVO> listaFilhosResponsavelFinanceiroVOs;

    /**
     * Construtor padrão da classe <code>InteracaoWorkflow</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public InteracaoWorkflowVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna o objeto da classe <code>CompromissoAgendaPessoaHorario</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorario() {
        if (compromissoAgendaPessoaHorario == null) {
            compromissoAgendaPessoaHorario = new CompromissoAgendaPessoaHorarioVO();
        }
        return (compromissoAgendaPessoaHorario);
    }

    /**
     * Define o objeto da classe <code>CompromissoAgendaPessoaHorario</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setCompromissoAgendaPessoaHorario(CompromissoAgendaPessoaHorarioVO obj) {
        this.compromissoAgendaPessoaHorario = obj;
    }

    /**
     * Retorna o objeto da classe <code>Workflow</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public WorkflowVO getWorkflow() {
        if (workflow == null) {
            workflow = new WorkflowVO();
        }
        return (workflow);
    }

    /**
     * Define o objeto da classe <code>Workflow</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setWorkflow(WorkflowVO obj) {
        this.workflow = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Campanha</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public CampanhaVO getCampanha() {
        if (campanha == null) {
            campanha = new CampanhaVO();
        }
        return (campanha);
    }

    /**
     * Define o objeto da classe <code>Campanha</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setCampanha(CampanhaVO obj) {
        this.campanha = obj;
    }

    /**
     * Retorna o objeto da classe <code>Prospects</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return (prospect);
    }

    /**
     * Define o objeto da classe <code>Prospects</code> relacionado com (<code>InteracaoWorkflow</code>).
     */
    public void setProspect(ProspectsVO obj) {
        this.prospect = obj;
    }

    public Date getDataTermino() {
        return (dataTermino);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
     */
    public String getDataTermino_Apresentar() {
        return (Uteis.getData(dataTermino));
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Date getDataInicio() {
        return (dataInicio);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
     */
    public String getDataInicio_Apresentar() {
        return (Uteis.getData(dataInicio));
    }

    public String getDataHora_Apresentar() {
        return Uteis.obterDataFormatoTextoddMMyyyy(getDataInicio()) + " - "
                + getHoraInicio();
    }


    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getHoraTermino() {
        return (horaTermino);
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getHoraInicio() {
        if (horaInicio == null) {
            return "";
        }
        return (horaInicio);
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

//    public Long getTempoMaximo() {
//        if (tempoMaximo == null) {
//            return 0L;
//        }
//        return (tempoMaximo);
//    }
//     
//    public void setTempoMaximo( Long tempoMaximo ) {
//        this.tempoMaximo = tempoMaximo;
//    }
//
//    public Long getTempoMinimo() {
//        if (tempoMinimo == null) {
//            return 0L;
//        }
//        return (tempoMinimo);
//    }
//     
//    public void setTempoMinimo( Long tempoMinimo ) {
//        this.tempoMinimo = tempoMinimo;
//    }
    public EtapaWorkflowVO getEtapaWorkflow() {
        if (etapaWorkflow == null) {
            etapaWorkflow = new EtapaWorkflowVO();
        }
        return (etapaWorkflow);
    }

    public void setEtapaWorkflow(EtapaWorkflowVO etapaWorkflow) {
        this.etapaWorkflow = etapaWorkflow;
    }

//    public Integer getPercentualEfetivaVendaHistorica() {
//        if (percentualEfetivaVendaHistorica == null) {
//            percentualEfetivaVendaHistorica = 0;
//        }
//        return (percentualEfetivaVendaHistorica);
//    }
//     
//    public void setPercentualEfetivaVendaHistorica( Integer percentualEfetivaVendaHistorica ) {
//        this.percentualEfetivaVendaHistorica = percentualEfetivaVendaHistorica;
//    }
    public TipoInteracaoEnum getTipoInteracao() {
        if (tipoInteracao == null) {
            return TipoInteracaoEnum.TELEFONE;
        }
        return (tipoInteracao);
    }

    public void setTipoInteracao(TipoInteracaoEnum tipoInteracao) {
        this.tipoInteracao = tipoInteracao;
    }

    public String getObservacao_Apresentar() {
        if (getObservacao().length() > 200) {
            return getObservacao().substring(0, 200);
        } else {
            return getObservacao();
        }
    }

    public String getObservacao() {
        if (observacao == null) {
            return "";
        }
        return (observacao);
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public MotivoInsucessoVO getMotivoInsucesso() {
        if (motivoInsucesso == null) {
            motivoInsucesso = new MotivoInsucessoVO();
        }
        return (motivoInsucesso);
    }

    public void setMotivoInsucesso(MotivoInsucessoVO motivoInsucesso) {
        this.motivoInsucesso = motivoInsucesso;
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

    public String getTempoDecorrido() {
        if (tempoDecorrido == null || tempoDecorrido.equals("")) {
            tempoDecorrido = "00:00:00";
        }
        return tempoDecorrido;
    }

    public void setTempoDecorrido(String tempoDecorrido) {
        this.tempoDecorrido = tempoDecorrido;
    }

    public String getTipoInteracao_Apresentar() {
        if (getTipoInteracao().equals(TipoInteracaoEnum.TELEFONE)) {
            return "Telefone";
        } else if (getTipoInteracao().equals(TipoInteracaoEnum.PRESENCIAL)) {
            return "Presencial";
        } else if (getTipoInteracao().equals(TipoInteracaoEnum.PORTALALUNO)) {
            return "Portal Aluno";
        } else {
            return "E-Mail";
        }
    }

    public String getScriptCurso() {
        if (!getEtapaWorkflow().getCodigo().equals(0) && !getCurso().getCodigo().equals(0)) {
            for (CursoEtapaWorkflowVO cursoEtapaWorkflowVO : getEtapaWorkflow().getCursoEtapaWorkflowVOs()) {
                if (cursoEtapaWorkflowVO.getCurso().getCodigo().intValue() == getCurso().getCodigo().intValue()) {
                    return cursoEtapaWorkflowVO.getScript_Apresentar();
                }
            }
        }
        return "";
    }

    public TipoMidiaCaptacaoVO getTipoMidia() {
        if (tipoMidia == null) {
            tipoMidia = new TipoMidiaCaptacaoVO();
        }
        return tipoMidia;
    }

    public void setTipoMidia(TipoMidiaCaptacaoVO tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    public Boolean getGravada() {
        if (gravada == null) {
            gravada = Boolean.FALSE;
        }
        return gravada;
    }

    public void setGravada(Boolean gravada) {
        this.gravada = gravada;
    }

	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}

    
    public String getMotivo() {
        if(motivo == null){
            motivo = "";
        }
        return motivo;
    }

    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public CompromissoAgendaPessoaHorarioVO getReagendarCompromisso() {
		if (reagendarCompromisso == null) {
			reagendarCompromisso = new CompromissoAgendaPessoaHorarioVO();
		}
		return reagendarCompromisso;
	}

	public void setReagendarCompromisso(CompromissoAgendaPessoaHorarioVO reagendarCompromisso) {
		this.reagendarCompromisso = reagendarCompromisso;
	}

	public List<PessoaVO> getListaFilhosResponsavelFinanceiroVOs() {
		if (listaFilhosResponsavelFinanceiroVOs == null) {
			listaFilhosResponsavelFinanceiroVOs = new ArrayList<PessoaVO>(0);
		}
		return listaFilhosResponsavelFinanceiroVOs;
	}

	public void setListaFilhosResponsavelFinanceiroVOs(List<PessoaVO> listaFilhosResponsavelFinanceiroVOs) {
		this.listaFilhosResponsavelFinanceiroVOs = listaFilhosResponsavelFinanceiroVOs;
	}
	
	/**
	 * INICO MERGE EDIGAR 
	 */
	/**
	 * 
	 */
    private TipoOrigemInteracaoEnum tipoOrigemInteracao;
	private String identificadorOrigem;    
	private Integer codigoEntidadeOrigem;
    
    public TipoOrigemInteracaoEnum getTipoOrigemInteracao() {
		if (tipoOrigemInteracao == null) {
			tipoOrigemInteracao = TipoOrigemInteracaoEnum.NENHUM;
		}
		return tipoOrigemInteracao;
	}

	public void setTipoOrigemInteracao(TipoOrigemInteracaoEnum tipoOrigemInteracao) {
		this.tipoOrigemInteracao = tipoOrigemInteracao;
	}

	public String getIdentificadorOrigem() {
		if (identificadorOrigem == null) {
			identificadorOrigem = "";
		}
		return identificadorOrigem;
	}

	public void setIdentificadorOrigem(String identificadorOrigem) {
		this.identificadorOrigem = identificadorOrigem;
	}

	public Integer getCodigoEntidadeOrigem() {
		return codigoEntidadeOrigem;
	}

	public void setCodigoEntidadeOrigem(Integer codigoOrigem) {
		this.codigoEntidadeOrigem = codigoOrigem;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
}
