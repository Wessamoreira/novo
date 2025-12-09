package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

import controle.financeiro.RelatorioSEIDecidirControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.MetaVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.crm.enumerador.PoliticaRedistribuicaoProspectAgendaEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.utilitarias.Uteis;

public class CampanhaVO extends SuperVO {

    private PoliticaGerarAgendaEnum politicaGerarAgenda;
    private Integer codigo;
    private String descricao;
    private UnidadeEnsinoVO unidadeEnsino;
    private CursoVO curso;
    private Date periodoInicio;
    private Date periodoFim;
    private String horaInicial;
    private String horaFinal;
    private String horaInicioIntervalo;
    private String horaFimIntervalo;
    
//    private Boolean naoGerarAgendaParaProspectsComAgendaJaExistente;
    private Date dataInicialVerificarJaExisteAgendaProspect;
    private Date dataFinalVerificarJaExisteAgendaProspect;
    
    private WorkflowVO workflow;
    private MetaVO meta;
    private TipoCampanhaEnum tipoCampanha;
    private String objetivo;
    private List<CampanhaPublicoAlvoVO> listaCampanhaPublicoAlvo;
    private CampanhaPublicoAlvoVO campanhaPublicoAlvo;
    private String publicoAlvo;
    private String situacao;
    private List<CampanhaColaboradorVO> listaCampanhaColaborador;
    private List<CampanhaMidiaVO> listaCampanhaMidia;
    private Boolean possuiAgenda;
//    private Boolean gerarAgendaConsultorRespProspect;
    private Boolean considerarSabado;
    private Boolean considerarFeriados;
    
    // INÍCIO DOS ATRIBUTOS TRANSIENTES
    /**
     * Atributos transientes (não persistidos) utilizados somente
     * durante o processo de geração de agenda para uma determinada 
     * campanha
     */
    private Integer consultorDistribuir;
    private Integer numeroProspectsDistribuidos;
    private HashMap<String,Integer> mapaProspectsDistribuidos;
    private Double metaContatosASeremRealizadosPorDiaPorConsultor;
    // FIM DOS ATRIBUTOS TRANSIENTES
    
    private Boolean recorrente;
    private String tipoRecorrencia;
    private Date dataRecorrencia;
    private PoliticaRedistribuicaoProspectAgendaEnum politicaRedistribuicaoProspectAgenda;
    /*
     * Atributo serve para saber se estamos realizando a redistribuição dos prospects 
     */
    private Boolean realizandoRedistribuicaoProspectAgenda;
    
    /**
     * Usado na classe {@link RelatorioSEIDecidirControle}
     */
	private Boolean filtrarCampanhaVO;
    
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidade) {
        this.unidadeEnsino = unidade;
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

    public Date getPeriodoInicio() {
        if (periodoInicio == null) {
            periodoInicio = new Date();
        }
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFim() {
        if (periodoFim == null) {
            periodoFim = new Date();
        }
        return periodoFim;
    }

    public void setPeriodoFim(Date periodoFim) {
        this.periodoFim = periodoFim;
    }

    public WorkflowVO getWorkflow() {
        if (workflow == null) {
            workflow = new WorkflowVO();
        }
        return workflow;
    }

    public void setWorkflow(WorkflowVO workflow) {
        this.workflow = workflow;
    }

    public MetaVO getMeta() {
        if (meta == null) {
            meta = new MetaVO();
        }
        return meta;
    }

    public void setMeta(MetaVO meta) {
        this.meta = meta;
    }

    public TipoCampanhaEnum getTipoCampanha() {
        if (tipoCampanha == null) {
            tipoCampanha = TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES;
        }
        return tipoCampanha;
    }
    
    public String getTipoCampanha_Apresentar() {
    	return getTipoCampanha().getDescricao();
    }

    public void setTipoCampanha(TipoCampanhaEnum tipoCampanha) {
        this.tipoCampanha = tipoCampanha;
    }

    public String getObjetivo() {
        if (objetivo == null) {
            objetivo = "";
        }
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "EC";
        }
        return situacao;
    }

    public String getSituacao_Apresentar() {
        if (this.getSituacao().equals("CA")) {
            return "Cancelada";
        }
        if (this.getSituacao().equals("FI")) {
            return "Finalizada";
        }
        if (this.getSituacao().equals("AT")) {
            return "Ativada";
        }
        return "Em Construção";
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public List<CampanhaColaboradorVO> getListaCampanhaColaborador() {
        if (listaCampanhaColaborador == null) {
            listaCampanhaColaborador = new ArrayList<CampanhaColaboradorVO>(0);
        }
        return listaCampanhaColaborador;
    }

    public void setListaCampanhaColaborador(List<CampanhaColaboradorVO> listaCampanhaColaborador) {
        this.listaCampanhaColaborador = listaCampanhaColaborador;
    }
    
    public List<SelectItem> getListaSelectItemFuncionarioCargo(){
    	List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(0, ""));
    	getListaCampanhaColaborador().stream().forEach(p->{
    		itens.add(new SelectItem(p.getFuncionarioCargoVO().getCodigo(), p.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome()));
    	});
		return itens;
    }

    public String getPeriodoInicio_Apresentar() {
        return Uteis.getData(periodoInicio);
    }

    public String getPeriodoFim_Apresentar() {
        return Uteis.getData(periodoFim);
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public List<CampanhaMidiaVO> getListaCampanhaMidia() {
        if (listaCampanhaMidia == null) {
            listaCampanhaMidia = new ArrayList<CampanhaMidiaVO>(0);
        }
        return listaCampanhaMidia;
    }

    public void setListaCampanhaMidia(List<CampanhaMidiaVO> listaCampanhaMidia) {
        this.listaCampanhaMidia = listaCampanhaMidia;
    }

    public CampanhaPublicoAlvoVO getCampanhaPublicoAlvo() {
        if (campanhaPublicoAlvo == null) {
            campanhaPublicoAlvo = new CampanhaPublicoAlvoVO();
        }
        return campanhaPublicoAlvo;
    }

    public void setCampanhaPublicoAlvo(CampanhaPublicoAlvoVO campanhaPublicoAlvo) {
        this.campanhaPublicoAlvo = campanhaPublicoAlvo;
    }

    public List<CampanhaPublicoAlvoVO> getListaCampanhaPublicoAlvo() {
        if (listaCampanhaPublicoAlvo == null) {
            listaCampanhaPublicoAlvo = new ArrayList(0);
        }
        return listaCampanhaPublicoAlvo;
    }

    public void setListaCampanhaPublicoAlvo(List<CampanhaPublicoAlvoVO> listaCampanhaPublicoAlvo) {
        this.listaCampanhaPublicoAlvo = listaCampanhaPublicoAlvo;
    }

    public String getPublicoAlvo() {
        if (publicoAlvo == null) {
            publicoAlvo = "";
        }
        return publicoAlvo;
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public Boolean getPossuiAgenda() {
        if (possuiAgenda == null) {
            possuiAgenda = Boolean.FALSE;
        }
        return possuiAgenda;
    }

    public void setPossuiAgenda(Boolean possuiAgenda) {
        this.possuiAgenda = possuiAgenda;
    }

    public String getHoraInicial() {
        if (horaInicial == null) {
            horaInicial = "08:00";
        }
        return horaInicial;
    }

    public void setHoraInicial(String horaInicio) {
        this.horaInicial = horaInicio;
    }

    public Boolean getCampanhaReceptiva() {
        if (getTipoCampanha().equals(TipoCampanhaEnum.LIGACAO_RECEPTIVA)) {
            return true;
        }
        return false;
    }

    public Boolean getCampanhaLigacaoAtivaSemAgenda() {
        if (getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA) || getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA)) {
            return true;
        }
        return false;
    }
    
    public Boolean getCampanhaProcessoSeletivo() {
    	if (getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_INSCRITOS_PROCSELETIVO)) {
    		return true;
    	}
    	return false;
    }

    public Boolean getCampanhaPreInscricao() {
        if (getTipoCampanha().equals(TipoCampanhaEnum.PRE_INSCRICAO)) {
            return true;
        }
        return false;
    }
    
    public Boolean getCursoInformado() {
        if (getCurso().getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }
    
    public Boolean getCampanhaSemPeriodo() {
        if (getCampanhaPreInscricao() || getCampanhaReceptiva() || getCampanhaLigacaoAtivaSemAgenda() || getCampanhaProcessoSeletivo()) {
            return true;
        }
        return false;
    }

    /**
     * @return the considerarSabado
     */
    public Boolean getConsiderarSabado() {
        if (considerarSabado == null) {
            considerarSabado = Boolean.FALSE;
        }
        return considerarSabado;
    }

    /**
     * @param considerarSabado the considerarSabado to set
     */
    public void setConsiderarSabado(Boolean considerarSabado) {
        this.considerarSabado = considerarSabado;
    }

    /**
     * @return the considerarFeriados
     */
    public Boolean getConsiderarFeriados() {
        if (considerarFeriados == null) {
            considerarFeriados = Boolean.FALSE;
        }
        return considerarFeriados;
    }

    /**
     * @param considerarFeriados the considerarFeriados to set
     */
    public void setConsiderarFeriados(Boolean considerarFeriados) {
        this.considerarFeriados = considerarFeriados;
    }

    /**
     * @return the consultorDistribuir
     */
    public Integer getConsultorDistribuir() {
        if (consultorDistribuir == null) {
            consultorDistribuir = 0;
        }
        return consultorDistribuir;
    }

    /**
     * @param consultorDistribuir the consultorDistribuir to set
     */
    public void setConsultorDistribuir(Integer consultorDistribuir) {
        this.consultorDistribuir = consultorDistribuir;
    }

    /**
     * @return the numeroProspectsDistribuidos
     */
    public Integer getNumeroProspectsDistribuidos() {
        if (numeroProspectsDistribuidos == null) {
            numeroProspectsDistribuidos = 0;
        }
        return numeroProspectsDistribuidos;
    }

    /**
     * @param numeroProspectsDistribuidos the numeroProspectsDistribuidos to set
     */
    public void setNumeroProspectsDistribuidos(Integer numeroProspectsDistribuidos) {
        this.numeroProspectsDistribuidos = numeroProspectsDistribuidos;
    }

    /**
     * @return the mapaProspectsDistribuidos
     */
    public HashMap<String,Integer> getMapaProspectsDistribuidos() {
        if (mapaProspectsDistribuidos == null) {
            mapaProspectsDistribuidos = new HashMap<String,Integer>();
        }
        return mapaProspectsDistribuidos;
    }

    /**
     * @param mapaProspectsDistribuidos the mapaProspectsDistribuidos to set
     */
    public void setMapaProspectsDistribuidos(HashMap<String,Integer> mapaProspectsDistribuidos) {
        this.mapaProspectsDistribuidos = mapaProspectsDistribuidos;
    }

    /**
     * @return the metaContatosASeremRealizadosPorDiaPorConsultor
     */
    public Double getMetaContatosASeremRealizadosPorDiaPorConsultor() {
        if (metaContatosASeremRealizadosPorDiaPorConsultor == null) {
            metaContatosASeremRealizadosPorDiaPorConsultor = 0.0;
        }
        return metaContatosASeremRealizadosPorDiaPorConsultor;
    }

    /**
     * @param metaContatosASeremRealizadosPorDiaPorConsultor the metaContatosASeremRealizadosPorDiaPorConsultor to set
     */
    public void setMetaContatosASeremRealizadosPorDiaPorConsultor(Double metaContatosASeremRealizadosPorDiaPorConsultor) {
        this.metaContatosASeremRealizadosPorDiaPorConsultor = metaContatosASeremRealizadosPorDiaPorConsultor;
    }

    /**
     * @return the horaFinal
     */
    public String getHoraFinal() {
        if (horaFinal == null) {
            horaFinal = "18:00";
        }
        return horaFinal;
    }

    /**
     * @param horaFinal the horaFinal to set
     */
    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
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
     * @return the dataInicialVerificarJaExisteAgendaProspect
     */
    public Date getDataInicialVerificarJaExisteAgendaProspect() {
        if (dataInicialVerificarJaExisteAgendaProspect == null) {
            dataInicialVerificarJaExisteAgendaProspect = new Date();
        }
        return dataInicialVerificarJaExisteAgendaProspect;
    }

    /**
     * @param dataInicialVerificarJaExisteAgendaProspect the dataInicialVerificarJaExisteAgendaProspect to set
     */
    public void setDataInicialVerificarJaExisteAgendaProspect(Date dataInicialVerificarJaExisteAgendaProspect) {
        this.dataInicialVerificarJaExisteAgendaProspect = dataInicialVerificarJaExisteAgendaProspect;
    }

    /**
     * @return the dataFinalVerificarJaExisteAgendaProspect
     */
    public Date getDataFinalVerificarJaExisteAgendaProspect() {
        if (dataFinalVerificarJaExisteAgendaProspect == null) {
            dataFinalVerificarJaExisteAgendaProspect = new Date();
        }
        return dataFinalVerificarJaExisteAgendaProspect;
    }

    /**
     * @param dataFinalVerificarJaExisteAgendaProspect the dataFinalVerificarJaExisteAgendaProspect to set
     */
    public void setDataFinalVerificarJaExisteAgendaProspect(Date dataFinalVerificarJaExisteAgendaProspect) {
        this.dataFinalVerificarJaExisteAgendaProspect = dataFinalVerificarJaExisteAgendaProspect;
    }

	public Boolean getRecorrente() {
		if (recorrente == null) {
			recorrente = Boolean.FALSE;
		}
		return recorrente;
	}

	public void setRecorrente(Boolean recorrente) {
		this.recorrente = recorrente;
	}

	public String getTipoRecorrencia() {
		if (tipoRecorrencia == null) {
			tipoRecorrencia = "";
		}
		return tipoRecorrencia;
	}

	public void setTipoRecorrencia(String tipoRecorrencia) {
		this.tipoRecorrencia = tipoRecorrencia;
	}

	public Date getDataRecorrencia() {
		return dataRecorrencia;
	}

	public void setDataRecorrencia(Date dataRecorrencia) {
		this.dataRecorrencia = dataRecorrencia;
	}

    public Boolean getFiltrarCampanhaVO() {
    	if (filtrarCampanhaVO == null) {
    		filtrarCampanhaVO = false;
		}
		return filtrarCampanhaVO;
	}

	public void setFiltrarCampanhaVO(Boolean filtrarCampanhaVO) {
		this.filtrarCampanhaVO = filtrarCampanhaVO;
	}

		public Boolean getCampanhaInscritosProcessoSeletivo() {
            if (getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_INSCRITOS_PROCSELETIVO)) {
                return true;
            }
            return false;
        }	
        
        public PoliticaGerarAgendaEnum getPoliticaGerarAgenda() {
            if (politicaGerarAgenda == null) {
                politicaGerarAgenda = PoliticaGerarAgendaEnum.GERAR_AO_LANCAR_RESULTADO_INSCRICAO;
            }
            return politicaGerarAgenda;
        }

        public void setPoliticaGerarAgenda(PoliticaGerarAgendaEnum politicaGerarAgendaEnum) {
            this.politicaGerarAgenda = politicaGerarAgendaEnum;
        }
        
        public static PoliticaGerarAgendaEnum getPoliticaGerarAgendaInicial(String valor) {
            if ((valor == null) || (valor.trim().equals(""))) {
                return PoliticaGerarAgendaEnum.GERAR_AO_LANCAR_RESULTADO_INSCRICAO;
            }
            return PoliticaGerarAgendaEnum.valueOf(valor);
        }
        
        public Integer getTotalGeralProspectPublicoAlvo() {
			Integer total = 0;
			for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : getListaCampanhaPublicoAlvo()) {
				total = total + campanhaPublicoAlvoVO.getTotalProspectsSelecionadosCampanha();
			}
			return total;
		}
		
		public Integer getTotalGeralCompromissoUltrapassouDataFinalLimiteCampanha() {
			Integer total = 0;
			for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : getListaCampanhaPublicoAlvo()) {
				total = total + campanhaPublicoAlvoVO.getQuantidadeCompromissoUltrapassaramLimiteDataFinalCampanha();
			}
			return total;
		}
		
		public Integer getMediaGeralProspectPorColaborador() {
			if (!getListaCampanhaColaborador().isEmpty()) {
				return getTotalGeralProspectPublicoAlvo() / getListaCampanhaColaborador().size(); 
			}
			return 0;
		}
		
		public Integer getTempoMedioGeralExecutaoWorkflowPorColaborador() {
			if (getListaCampanhaPublicoAlvo().isEmpty()) {
				return 0;
			}
			return ((getTotalGeralProspectPublicoAlvo() * getWorkflow().getTempoMedioGerarAgenda()) / 480) == 0 ? 1 : (getTotalGeralProspectPublicoAlvo() * getWorkflow().getTempoMedioGerarAgenda()) / 480;
		}

		public PoliticaRedistribuicaoProspectAgendaEnum getPoliticaRedistribuicaoProspectAgenda() {
			if (politicaRedistribuicaoProspectAgenda == null) {
				politicaRedistribuicaoProspectAgenda = PoliticaRedistribuicaoProspectAgendaEnum.TODOS;
			}
			return politicaRedistribuicaoProspectAgenda;
		}

		public void setPoliticaRedistribuicaoProspectAgenda(PoliticaRedistribuicaoProspectAgendaEnum politicaRedistribuicaoProspectAgenda) {
			this.politicaRedistribuicaoProspectAgenda = politicaRedistribuicaoProspectAgenda;
		}
		
		public Boolean getRealizandoRedistribuicaoProspectAgenda() {
			if (realizandoRedistribuicaoProspectAgenda == null) {
				realizandoRedistribuicaoProspectAgenda = false;
			}
			return realizandoRedistribuicaoProspectAgenda;
		}

		public void setRealizandoRedistribuicaoProspectAgenda(Boolean realizandoRedistribuicaoProspectAgenda) {
			this.realizandoRedistribuicaoProspectAgenda = realizandoRedistribuicaoProspectAgenda;
		}

		public Integer getTamanhoListaRedistribuicao() {
			Integer qtde = 0;
			for (CampanhaColaboradorVO campanhaColaboradorVO : getListaCampanhaColaborador()) {
				if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)) {
					qtde++;
				}
			}
			return qtde;
		}
		
		private Integer totalGeralProspectSemConsultor;

		public Integer getTotalGeralProspectSemConsultor() {
			if (totalGeralProspectSemConsultor == null) {
				totalGeralProspectSemConsultor = 0;
				for(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : getListaCampanhaPublicoAlvo()) {
					totalGeralProspectSemConsultor = totalGeralProspectSemConsultor + campanhaPublicoAlvoVO.getTotalProspectSemConsultor();
				}
			}
			return totalGeralProspectSemConsultor;
		}

		public void setTotalGeralProspectSemConsultor(Integer totalGeralProspectSemConsultor) {
			this.totalGeralProspectSemConsultor = totalGeralProspectSemConsultor;
		}

		public Boolean getApresentarBotaoAdicionarCursoColaborador() {
			return !getCursoInformado() && (getCampanhaPreInscricao() || getCampanhaInscritosProcessoSeletivo());
		}
		
		public Integer getQuantidadePublicoAlvo(){
			return getListaCampanhaPublicoAlvo().size(); 
		}
	    
}
