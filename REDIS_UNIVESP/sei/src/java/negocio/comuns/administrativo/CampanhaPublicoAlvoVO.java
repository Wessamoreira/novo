package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.enumeradores.TipoGerarAgendaCampanhaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.enumerador.RendaProspectEnum;
import negocio.comuns.crm.enumerador.TipoEmpresaProspectEnum;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Ordenacao;

public class CampanhaPublicoAlvoVO extends SuperVO {

    private Integer codigo;
    private UnidadeEnsinoVO unidadeEnsino;
    private RegistroEntradaVO registroEntrada;
    private CursoInteresseVO cursoInteresse;
    private CursoVO curso;
    private String ano;
    private String semestre;
//    private Boolean buscarMatriculadosTodosCursos;
    private RendaProspectEnum renda;
    private CampanhaVO campanha;
    private CampanhaVO campanhaCaptacao;
    private TipoEmpresaProspectEnum tipoEmpresa;
    private FormacaoAcademicaVO formacaoAcademica;
  //FILTROS SITUAÇÃO ACADÊMICA
    private Boolean formandos;
    private Boolean possiveisFormandos;
    private Boolean cursando;
    private Boolean preMatriculados;
    private Boolean trancados;
    private Boolean cancelado;
    private Boolean inadimplentes;
    
    private Boolean abandonado;
    private Boolean transferenciaInterna;
    private Boolean transferenciaExterna;
    private Boolean preMatriculaCancelada;
    
//    private Boolean inadimplentesDias;
    private Integer diasInadimplencia;
    private Integer diasSemInteracao;		
//    private Boolean inadimplentesNoPeriodo;
    private Date dataInicialInadimplencia;
    private Date dataFinalInadimplencia;
    private Integer totalProspectsSelecionadosCampanha;
    private Integer tempoMedioExecucaoWorkflowColaborador;
    private Integer mediaProspectColaborador;
    private String tipoPublicoAlvo;
    private List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspectVOs;
    private HashMap<Integer, List<ProspectsVO>> listaProspectsPorColaborador;
    private Boolean alunosComSerasa;
    private Boolean alunosSemSerasa;
    
    private ProcSeletivoVO processoSeletivoVO;;
    private Boolean aprovado;
    private Boolean reprovado;
    private Date dataInicioProcessoSeletivo;
    private Date dataTerminoProcessoSeletivo;
    private Boolean todasOpcoesCurso;
    private Boolean segmentacao;
    private List<FuncionarioVO> listaCampanhaConsultorProspectVOs;
    private List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVisualizacaoVOs;
    private List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs;
    
    private HashMap<Integer, FuncionarioVO> mapConsultorVOs = new HashMap<Integer, FuncionarioVO>(0);
    
    private Boolean naoGerarAgendaParaProspectsComAgendaJaExistente;
    private TipoGerarAgendaCampanhaEnum tipoGerarAgendaCampanha;
    private HashMap<Integer, CampanhaPublicoAlvoProspectVO> mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs;
    private Boolean apresentarBotaoRegerarAgendaPublicoAlvoEspecifico;
    private String situacaoAgenda;
    private Boolean ultimoCursoInteresse;    		
    private List<PessoaVO> listaResponsavelFinanceiroDuplicadoVOs;
    private Boolean adicionadoDinamicamente;     
    
    /*
     * Campos transient
     */
    private boolean trazerProspectAluno = false;
    private boolean trazerProspectCandidado = false;
    private boolean trazerProspectFuncionario = false;
    private boolean trazerProspectFiliacao = false;
    
    

    public CampanhaPublicoAlvoVO() {
		super();
		getFormacaoAcademica().setEscolaridade("");
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public RegistroEntradaVO getRegistroEntrada() {
        if (registroEntrada == null) {
            registroEntrada = new RegistroEntradaVO();
        }
        return registroEntrada;
    }

    public void setRegistroEntrada(RegistroEntradaVO registroEntrada) {
        this.registroEntrada = registroEntrada;
    }

    public CursoInteresseVO getCursoInteresse() {
        if (cursoInteresse == null) {
            cursoInteresse = new CursoInteresseVO();
        }
        return cursoInteresse;
    }

    public void setCursoInteresse(CursoInteresseVO cursoInteresse) {
        this.cursoInteresse = cursoInteresse;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public RendaProspectEnum getRenda() {
        if (renda == null) {
            renda = RendaProspectEnum.NENHUM;
        }
        return renda;
    }

    public void setRenda(RendaProspectEnum renda) {
        this.renda = renda;
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

    public TipoEmpresaProspectEnum getTipoEmpresa() {
        if (tipoEmpresa == null) {
            tipoEmpresa = TipoEmpresaProspectEnum.NENHUM;
        }
        return tipoEmpresa;
    }

    public void setTipoEmpresa(TipoEmpresaProspectEnum tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public FormacaoAcademicaVO getFormacaoAcademica() {
        if (formacaoAcademica == null) {
            formacaoAcademica = new FormacaoAcademicaVO();
        }
        return formacaoAcademica;
    }

    public void setFormacaoAcademica(FormacaoAcademicaVO formacaoAcademica) {
        this.formacaoAcademica = formacaoAcademica;
    }

    public Boolean getFormandos() {
        if (formandos == null) {
            formandos = Boolean.FALSE;
        }
        return formandos;
    }

    public void setFormandos(Boolean formandos) {
        this.formandos = formandos;
    }

    public Boolean getCursando() {
        if (cursando == null) {
            cursando = Boolean.FALSE;
        }
        return cursando;
    }

    public void setCursando(Boolean cursando) {
        this.cursando = cursando;
    }

    public Boolean getPreMatriculados() {
        if(preMatriculados == null) {
            preMatriculados = Boolean.FALSE;
        }
        return preMatriculados;
    }

    public void setPreMatriculados(Boolean preMatriculados) {
        this.preMatriculados = preMatriculados;
    }

    public Boolean getTrancados() {
        if (trancados == null) {
            trancados = Boolean.FALSE;
        }
        return trancados;
    }

    public void setTrancados(Boolean trancados) {
        this.trancados = trancados;
    }

    public Boolean getCancelado() {
        if(cancelado == null) {
            cancelado = Boolean.FALSE;
        }
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getInadimplentes() {
        if(inadimplentes == null) {
            inadimplentes = Boolean.FALSE;
        }
        return inadimplentes;
    }

    public void setInadimplentes(Boolean inadimplentes) {
        this.inadimplentes = inadimplentes;
    }

    public Integer getDiasInadimplencia() {
        if (diasInadimplencia == null) {
            diasInadimplencia = 0;
        }
        return diasInadimplencia;
    }

    public void setDiasInadimplencia(Integer diasInadimplencia) {
        this.diasInadimplencia = diasInadimplencia;
    }

    public Boolean getPossiveisFormandos() {
        if (possiveisFormandos == null) {
            possiveisFormandos = Boolean.FALSE;
        }
        return possiveisFormandos;
    }

    public void setPossiveisFormandos(Boolean possiveisFormandos) {
        this.possiveisFormandos = possiveisFormandos;
    }

    public Integer getTotalProspectsSelecionadosCampanha() {
        if (totalProspectsSelecionadosCampanha == null) {
            totalProspectsSelecionadosCampanha = 0;
        }
        return totalProspectsSelecionadosCampanha;
    }

    public void setTotalProspectsSelecionadosCampanha(Integer totalProspectsSelecionadosCampanha) {
        this.totalProspectsSelecionadosCampanha = totalProspectsSelecionadosCampanha;
    }

    public Integer getTempoMedioExecucaoWorkflowColaborador() {
        if (tempoMedioExecucaoWorkflowColaborador == null) {
            tempoMedioExecucaoWorkflowColaborador = 0;
        }
        return tempoMedioExecucaoWorkflowColaborador;
    }

    public String getTempoMedioExecucaoWorkflowColaborador_Apresentar() {
        return getTempoMedioExecucaoWorkflowColaborador().intValue() + " dias Úteis" ;
    }

    public void setTempoMedioExecucaoWorkflowColaborador(Integer tempoMedioExecucaoWorkflowColaborador) {
        this.tempoMedioExecucaoWorkflowColaborador = tempoMedioExecucaoWorkflowColaborador;
    }

    public Integer getMediaProspectColaborador() {
        if (mediaProspectColaborador == null) {
            mediaProspectColaborador = 0;
        }
        return mediaProspectColaborador;
    }

    public void setMediaProspectColaborador(Integer mediaProspectColaborador) {
        this.mediaProspectColaborador = mediaProspectColaborador;
    }

    public CampanhaVO getCampanhaCaptacao() {
         if (campanhaCaptacao == null) {
            campanhaCaptacao = new CampanhaVO();
        }
        return campanhaCaptacao;
    }

    public void setCampanhaCaptacao(CampanhaVO campanhaCaptacao) {
        this.campanhaCaptacao = campanhaCaptacao;
    }

    public List<CampanhaPublicoAlvoProspectVO> getCampanhaPublicoAlvoProspectVOs() {
        if (campanhaPublicoAlvoProspectVOs == null) {
            campanhaPublicoAlvoProspectVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
        }
        return campanhaPublicoAlvoProspectVOs;
    }

    public void setCampanhaPublicoAlvoProspectVOs(List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspectVOs) {
        this.campanhaPublicoAlvoProspectVOs = campanhaPublicoAlvoProspectVOs;
    }

    /**
     * @return the tipoPublicoAlvo
     */
    public String getTipoPublicoAlvo() {
        if (tipoPublicoAlvo == null) {
            tipoPublicoAlvo = "PR";
        }
        return tipoPublicoAlvo;
    }

    /**
     * @param tipoPublicoAlvo the tipoPublicoAlvo to set
     */
    public void setTipoPublicoAlvo(String tipoPublicoAlvo) {
        this.tipoPublicoAlvo = tipoPublicoAlvo;
    }
    
    public String getTipoPublicoAlvo_Apresentar() {
        if (getTipoPublicoAlvo().equals("AL")) {
            return "Aluno";
        }
        if (getTipoPublicoAlvo().equals("PR")) {
        	return "Prospect";
        }
        if (getTipoPublicoAlvo().equals("CD")) {
            return "Candidato";
        }
        if (getTipoPublicoAlvo().equals("RF")) {
            return "Responsável Financeiro";
        }
        return tipoPublicoAlvo;
    }

    
/**
     * @return the listaProspectsPorColaborador
     */
    public HashMap<Integer, List<ProspectsVO>> getListaProspectsPorColaborador() {
        if (listaProspectsPorColaborador == null) {
            listaProspectsPorColaborador = new HashMap<Integer, List<ProspectsVO>>(0);
        }
        return listaProspectsPorColaborador;
    }

    /**
     * @param listaProspectsPorColaborador the listaProspectsPorColaborador to set
     */
    public void setListaProspectsPorColaborador(HashMap<Integer, List<ProspectsVO>> listaProspectsPorColaborador) {
        this.listaProspectsPorColaborador = listaProspectsPorColaborador;
    }

    public boolean getApresentarFiltrosParaMatriculadosNoCurso() {
        if ((this.getCurso().getCodigo() > 0)) {
            return true;
        }
        return false;
    }
    
    public boolean getAplicarFiltrosParaMatriculadosNoCurso() {
        return getApresentarFiltrosParaMatriculadosNoCurso();
    }

    /**
     * @return the dataInicialInadimplencia
     */
    public Date getDataInicialInadimplencia() {
        return dataInicialInadimplencia;
    }

    /**
     * @param dataInicialInadimplencia the dataInicialInadimplencia to set
     */
    public void setDataInicialInadimplencia(Date dataInicialInadimplencia) {
        this.dataInicialInadimplencia = dataInicialInadimplencia;
    }

    /**
     * @return the dataFinalInadimplencia
     */
    public Date getDataFinalInadimplencia() {
        return dataFinalInadimplencia;
    }

    /**
     * @param dataFinalInadimplencia the dataFinalInadimplencia to set
     */
    public void setDataFinalInadimplencia(Date dataFinalInadimplencia) {
        this.dataFinalInadimplencia = dataFinalInadimplencia;
    }

	public Boolean getAlunosComSerasa() {
		if (alunosComSerasa == null) {
			alunosComSerasa = Boolean.FALSE;
		}
		return alunosComSerasa;
	}

	public void setAlunosComSerasa(Boolean alunosComSerasa) {
		this.alunosComSerasa = alunosComSerasa;
	}

	public Boolean getAlunosSemSerasa() {
		if (alunosSemSerasa == null) {
			alunosSemSerasa = Boolean.FALSE;
		}
		return alunosSemSerasa;
	}

	public void setAlunosSemSerasa(Boolean alunosSemSerasa) {
		this.alunosSemSerasa = alunosSemSerasa;
	}

	public ProcSeletivoVO getProcessoSeletivoVO() {
		if (processoSeletivoVO == null) {
			processoSeletivoVO = new ProcSeletivoVO();
		}
		return processoSeletivoVO;
	}

	public void setProcessoSeletivoVO(ProcSeletivoVO processoSeletivoVO) {
		this.processoSeletivoVO = processoSeletivoVO;
	}

	public Boolean getAprovado() {
		if (aprovado == null) {
			aprovado = Boolean.FALSE;
		}
		return aprovado;
	}

	public void setAprovado(Boolean aprovado) {
		this.aprovado = aprovado;
	}

	public Boolean getReprovado() {
		if (reprovado == null) {
			reprovado = Boolean.FALSE;
		}
		return reprovado;
	}

	public void setReprovado(Boolean reprovado) {
		this.reprovado = reprovado;
	}

	public Date getDataInicioProcessoSeletivo() {
//		if (dataInicioProcessoSeletivo == null) {
//			dataInicioProcessoSeletivo = Uteis.getDataPrimeiroDiaMes(new Date());
//		}
		return dataInicioProcessoSeletivo;
	}

	public void setDataInicioProcessoSeletivo(Date dataInicioProcessoSeletivo) {
		this.dataInicioProcessoSeletivo = dataInicioProcessoSeletivo;
	}

	public Date getDataTerminoProcessoSeletivo() {
//		if (dataTerminoProcessoSeletivo == null) {
//			dataTerminoProcessoSeletivo = Uteis.getDataUltimoDiaMes(new Date());
//		}
		return dataTerminoProcessoSeletivo;
	}

	public void setDataTerminoProcessoSeletivo(Date dataTerminoProcessoSeletivo) {
		this.dataTerminoProcessoSeletivo = dataTerminoProcessoSeletivo;
	}

	public Boolean getTodasOpcoesCurso() {
		if (todasOpcoesCurso == null) {
			todasOpcoesCurso = Boolean.FALSE;
		}
		return todasOpcoesCurso;
	}

	public void setTodasOpcoesCurso(Boolean todasOpcoesCurso) {
		this.todasOpcoesCurso = todasOpcoesCurso;
	}

	public Boolean getSegmentacao() {
		if (segmentacao == null) {
			segmentacao = Boolean.FALSE;
		}
		return segmentacao;
	}

	public void setSegmentacao(Boolean segmentacao) {
		this.segmentacao = segmentacao;
	}

    /**
     * @return the ano
     */
    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(String ano) {
        this.ano = ano;
    }

    /**
     * @return the semestre
     */
    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public List<FuncionarioVO> getListaCampanhaConsultorProspectVOs() {
		if (listaCampanhaConsultorProspectVOs == null) {
			listaCampanhaConsultorProspectVOs = new ArrayList<FuncionarioVO>(0);
		}
		return listaCampanhaConsultorProspectVOs;
	}

	public void setListaCampanhaConsultorProspectVOs(List<FuncionarioVO> listaCampanhaConsultorProspectVOs) {
		this.listaCampanhaConsultorProspectVOs = listaCampanhaConsultorProspectVOs;
	}

	public List<CampanhaPublicoAlvoProspectVO> getListaCampanhaPublicoAlvoProspectVisualizacaoVOs() {
		if (listaCampanhaPublicoAlvoProspectVisualizacaoVOs == null) {
			listaCampanhaPublicoAlvoProspectVisualizacaoVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		}
		return listaCampanhaPublicoAlvoProspectVisualizacaoVOs;
	}

	public void setListaCampanhaPublicoAlvoProspectVisualizacaoVOs(List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVisualizacaoVOs) {
		this.listaCampanhaPublicoAlvoProspectVisualizacaoVOs = listaCampanhaPublicoAlvoProspectVisualizacaoVOs;
	}

	public Boolean getAbandonado() {
		if (abandonado == null) {
			abandonado = false;
		}
		return abandonado;
	}

	public void setAbandonado(Boolean abandonado) {
		this.abandonado = abandonado;
	}

	public Boolean getTransferenciaInterna() {
		if (transferenciaInterna == null) {
			transferenciaInterna = false;
		}
		return transferenciaInterna;
	}

	public void setTransferenciaInterna(Boolean transferenciaInterna) {
		this.transferenciaInterna = transferenciaInterna;
	}

	public Boolean getTransferenciaExterna() {
		if (transferenciaExterna == null) {
			transferenciaExterna = false;
		}
		return transferenciaExterna;
	}

	public void setTransferenciaExterna(Boolean transferenciaExterna) {
		this.transferenciaExterna = transferenciaExterna;
	}

	public Boolean getPreMatriculaCancelada() {
		if (preMatriculaCancelada == null) {
			preMatriculaCancelada = false;
		}
		return preMatriculaCancelada;
	}

	public void setPreMatriculaCancelada(Boolean preMatriculaCancelada) {
		this.preMatriculaCancelada = preMatriculaCancelada;
	}
     
	public String getDataPrimeiroCompromisso_Apresentar() {
		if (!getCampanhaPublicoAlvoProspectVOs().isEmpty()) {
			Ordenacao.ordenarLista(getCampanhaPublicoAlvoProspectVOs(), "dataCompromisso");
			return getCampanhaPublicoAlvoProspectVOs().get(0).getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso_Apresentar();
		}
		return "";
	}
	
	public String getDataUltimoCompromisso_Apresentar() {
		if (!getCampanhaPublicoAlvoProspectVOs().isEmpty()) {
			Ordenacao.ordenarLista(getCampanhaPublicoAlvoProspectVOs(), "dataCompromisso");
			return getCampanhaPublicoAlvoProspectVOs().get(getCampanhaPublicoAlvoProspectVOs().size() - 1).getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso_Apresentar();
		}
		return "";
	}

	public List<CampanhaPublicoAlvoProspectVO> getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs() {
		if (listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs == null) {
			listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		}
		return listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs;
	}

	public void setListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs(List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs) {
		this.listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs = listaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs;
	}

	public Integer getQuantidadeCompromissoUltrapassaramLimiteDataFinalCampanha() {
		return getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs().size();
	}

	public HashMap<Integer, FuncionarioVO> getMapConsultorVOs() {
		if (mapConsultorVOs == null) {
			mapConsultorVOs = new HashMap<Integer, FuncionarioVO>(0);
		}
		return mapConsultorVOs;
	}

	public void setMapConsultorVOs(HashMap<Integer, FuncionarioVO> mapConsultorVOs) {
		this.mapConsultorVOs = mapConsultorVOs;
	}

	/**
     * @return the naoGerarAgendaParaProspectsComAgendaJaExistente
     */
    public Boolean getNaoGerarAgendaParaProspectsComAgendaJaExistente() {
        if (naoGerarAgendaParaProspectsComAgendaJaExistente == null) {
            naoGerarAgendaParaProspectsComAgendaJaExistente = Boolean.TRUE;
        }
        return naoGerarAgendaParaProspectsComAgendaJaExistente;
    }

    /**
     * @param naoGerarAgendaParaProspectsComAgendaJaExistente the naoGerarAgendaParaProspectsComAgendaJaExistente to set
     */
    public void setNaoGerarAgendaParaProspectsComAgendaJaExistente(Boolean naoGerarAgendaParaProspectsComAgendaJaExistente) {
        this.naoGerarAgendaParaProspectsComAgendaJaExistente = naoGerarAgendaParaProspectsComAgendaJaExistente;
    }

    public TipoGerarAgendaCampanhaEnum getTipoGerarAgendaCampanha() {
		if (tipoGerarAgendaCampanha == null) {
			tipoGerarAgendaCampanha = TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_DISTRIBUINDO_IGUALITARIAMENTE_ENTRE_CONSULTORES_CAMPANHA;
		}
		return tipoGerarAgendaCampanha;
	}

	public void setTipoGerarAgendaCampanha(TipoGerarAgendaCampanhaEnum tipoGerarAgendaCampanha) {
		this.tipoGerarAgendaCampanha = tipoGerarAgendaCampanha;
	}

	public HashMap<Integer, CampanhaPublicoAlvoProspectVO> getMapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs() {
		if (mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs == null) {
			mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs = new HashMap<Integer, CampanhaPublicoAlvoProspectVO>(0);
		}
		return mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs;
	}

	public void setMapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs(HashMap<Integer, CampanhaPublicoAlvoProspectVO> mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs) {
		this.mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs = mapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs;
	}

	public Boolean getApresentarBotaoRegerarAgendaPublicoAlvoEspecifico() {
		if (apresentarBotaoRegerarAgendaPublicoAlvoEspecifico == null) {
			apresentarBotaoRegerarAgendaPublicoAlvoEspecifico = false;
		}
		return apresentarBotaoRegerarAgendaPublicoAlvoEspecifico;
	}

	public void setApresentarBotaoRegerarAgendaPublicoAlvoEspecifico(Boolean apresentarBotaoRegerarAgendaPublicoAlvoEspecifico) {
		this.apresentarBotaoRegerarAgendaPublicoAlvoEspecifico = apresentarBotaoRegerarAgendaPublicoAlvoEspecifico;
	}
	
	private Integer totalProspectSemConsultor;

	public Integer getTotalProspectSemConsultor() {
		if (totalProspectSemConsultor == null) {
			totalProspectSemConsultor = 0;
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoProspectVOs) {
				if (campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(0)) {
					totalProspectSemConsultor++;
				}
			}
		}
		return totalProspectSemConsultor;
	}
	
	public void setTotalProspectSemConsultor(Integer totalProspectSemConsultor) {
		this.totalProspectSemConsultor = totalProspectSemConsultor;
	}

	public String getSituacaoAgenda() {
		if (situacaoAgenda == null) {
			situacaoAgenda = "";
		}
		return situacaoAgenda;
	}

	public void setSituacaoAgenda(String situacaoAgenda) {
		this.situacaoAgenda = situacaoAgenda;
	}

	public Boolean getUltimoCursoInteresse() {
		if (ultimoCursoInteresse == null) {
			ultimoCursoInteresse = Boolean.FALSE;
		}
		return ultimoCursoInteresse;
	}

	public void setUltimoCursoInteresse(Boolean ultimoCursoInteresse) {
		this.ultimoCursoInteresse = ultimoCursoInteresse;
	}

	public Integer getDiasSemInteracao() {
		if (diasSemInteracao == null) {
			diasSemInteracao = 0;
		}
		return diasSemInteracao;
	}

	public void setDiasSemInteracao(Integer diasSemInteracao) {
		this.diasSemInteracao = diasSemInteracao;
	}
    
	public List<PessoaVO> getListaResponsavelFinanceiroDuplicadoVOs() {
		if (listaResponsavelFinanceiroDuplicadoVOs == null) {
			listaResponsavelFinanceiroDuplicadoVOs = new ArrayList<PessoaVO>(0);
		}
		return listaResponsavelFinanceiroDuplicadoVOs;
	}

	public void setListaResponsavelFinanceiroDuplicadoVOs(List<PessoaVO> listaResponsavelFinanceiroDuplicadoVOs) {
		this.listaResponsavelFinanceiroDuplicadoVOs = listaResponsavelFinanceiroDuplicadoVOs;
	}
	

	public Boolean getAdicionadoDinamicamente() {
		if(adicionadoDinamicamente == null){
			adicionadoDinamicamente = false;
		}
		return adicionadoDinamicamente;
	}

	public void setAdicionadoDinamicamente(Boolean adicionadoDinamicamente) {
		this.adicionadoDinamicamente = adicionadoDinamicamente;
	}

	public boolean isTrazerProspectAluno() {		
		return trazerProspectAluno;
	}

	public void setTrazerProspectAluno(boolean trazerProspectAluno) {
		this.trazerProspectAluno = trazerProspectAluno;
	}

	public boolean isTrazerProspectCandidado() {
		return trazerProspectCandidado;
	}

	public void setTrazerProspectCandidado(boolean trazerProspectCandidado) {
		this.trazerProspectCandidado = trazerProspectCandidado;
	}

	public boolean isTrazerProspectFuncionario() {
		return trazerProspectFuncionario;
	}

	public void setTrazerProspectFuncionario(boolean trazerProspectFuncionario) {
		this.trazerProspectFuncionario = trazerProspectFuncionario;
	}

	public boolean isTrazerProspectFiliacao() {		
		return trazerProspectFiliacao;
	}

	public void setTrazerProspectFiliacao(boolean trazerProspectFiliacao) {
		this.trazerProspectFiliacao = trazerProspectFiliacao;
	}
	
	
	
	
	
}
