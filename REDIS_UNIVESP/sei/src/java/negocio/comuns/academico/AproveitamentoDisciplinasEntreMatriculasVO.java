package negocio.comuns.academico;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class AproveitamentoDisciplinasEntreMatriculasVO extends SuperVO {

    private Integer codigo;
    
    private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO; 
    private String tipoAproveitamentoDisciplinasEntreMatriculas;
    
    private MatriculaVO matriculaOrigemAproveitamentoVO;
    private PeriodoLetivoVO periodoLetivoUtilizarDisciplinasCursar;

    private MatriculaVO matriculaDestinoAproveitamentoVO;
    private ConfiguracaoAcademicoVO configuracaoAcademicoCursoDestino;
    
    private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaUtilizadoAproveitamento;
    
    private Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
    private Boolean aproveitamentoPorIsencao;
	private Boolean utilizarAnoSemestreAtualDisciplinaAprovada;    
	private String anoPadrao;
	private String semestrePadrao;
	
	private String descricaoComplementacao;
	private String situacaoHistoricoConcessaoEntreMatriculas;
    
    /**
     * Aguardando_Processamento
     * Realizada_Com_Sucesso
     * Erro_Durante_Processamento
     * Cancelada_Transferencia
     */
    private String situacao;
    private String observacoes;
    private Integer nrAlertasCriticos;
    private String resultadoProcessamento;
    private UsuarioVO responsavel;
    private Date dataProcessamento;

    /**
     * ATRIBUTOS TRANSIENT´S UTILIZADOS NO CONTROLE
     * DA TRANSFERENCIA DE MATRIZ CURRICULAR
     */
    private List<HistoricoVO> historicoNaoAproveitados;
    private List<HistoricoVO> historicoAproveitados;
    private List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAproveitadosComHistoricos;
    private List<HistoricoVO> historicoAlocadosMapaEquivalenciaAproveitados;
    private List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAplicarComposicao;
            
    private static final long serialVersionUID = 1L;

    public AproveitamentoDisciplinasEntreMatriculasVO() {
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

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaOrigemAproveitamentoVO() {
        if (matriculaOrigemAproveitamentoVO == null) {
        	matriculaOrigemAproveitamentoVO = new MatriculaVO();
        }
        return matriculaOrigemAproveitamentoVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaOrigemAproveitamentoVO(MatriculaVO matriculaOrigemAproveitamentoVO) {
        this.matriculaOrigemAproveitamentoVO = matriculaOrigemAproveitamentoVO;
    }

    /**
     * @return the historicoNaoAproveitadosTransferencia
     */
    public List<HistoricoVO> getHistoricoNaoAproveitados() {
        if (historicoNaoAproveitados == null) {
            historicoNaoAproveitados = new ArrayList<HistoricoVO>(0);
        }
        return historicoNaoAproveitados;
    }

    /**
     * @param historicoNaoAproveitadosTransferencia the historicoNaoAproveitadosTransferencia to set
     */
    public void setHistoricoNaoAproveitados(List<HistoricoVO> historicoNaoAproveitados) {
        this.historicoNaoAproveitados = historicoNaoAproveitados;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        if (situacao == null) {
            situacao = "AP";
        }
        return situacao;
    }
    
    public boolean getPodeSerRemovida() {
        if (getSituacao().equals("AP")) {
            return true;
        }
        return false;
    }
    
    public boolean getPodeSerCancelada() {
        if (getSituacao().equals("RE")) {
            return true;
        }
        return false;
    }
    
    public boolean getJaProcessada() {
        if (getSituacao().equals("RE")) {
            return true;
        }
        if (getSituacao().equals("ER")) {
            return true;
        }
        if (getSituacao().equals("CA")) {
            return true;
        }
        return false;
    }
    
    public boolean getPodeSerProcessada() {
        if (getSituacao().equals("AP")) {
            return true;
        }
        if (getSituacao().equals("ER")) {
            return true;
        }
        if (getSituacao().equals("CA")) {
            return true;
        }
        return false;
    }
    
    public String getSituacao_Apresentar() {
        if (getSituacao().equals("AP")) {
            return "Aguardando Processamento";
        }
        if (getSituacao().equals("ER")) {
            return "Erro no Processamento";
        }
        if (getSituacao().equals("CA")) {
            return "Cancelada";
        }
        if (getSituacao().equals("RE")) {
            return "Realizada Com Sucesso";
        }
        return "";
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the observacoes
     */
    public String getObservacoes() {
        if (observacoes == null) {
            observacoes = "";
        }
        return observacoes;
    }

    /**
     * @param observacoes the observacoes to set
     */
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    /**
     * @return the resultadoProcessamento
     */
    public String getResultadoProcessamento() {
        if (resultadoProcessamento == null) {
            resultadoProcessamento = "";
        }
        return resultadoProcessamento;
    }

    /**
     * @param resultadoProcessamento the resultadoProcessamento to set
     */
    public void setResultadoProcessamento(String resultadoProcessamento) {
        this.resultadoProcessamento = resultadoProcessamento;
    }

    /**
     * @return the responsavel
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    /**
     * @param responsavel the responsavel to set
     */
    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    /**
     * @return the dataProcessamento
     */
    public Date getDataProcessamento() {
        if (dataProcessamento == null) {
            dataProcessamento = new Date();
        }
        return dataProcessamento;
    }

    public void setDataProcessamento(Date dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }
    
    public void adicionarHistoricoResultadoProcessamento(Date data, String responsavel, String descricao) {
        adicionarHistoricoResultadoProcessamento(data, responsavel, descricao, false);
    }
    
    public void adicionarHistoricoListaHistoricoNaoAproveitados(HistoricoVO historicoAdicionar) {
        if ((!historicoAdicionar.getAprovado()) &&
            (!historicoAdicionar.getReprovado()) &&
            (!historicoAdicionar.getCursando())) {
            //se trata-se de um histórico que nao foi APROVADO, REPROVADO E CURSANDO
            //Não há por que adicionar para lista de pendências
            return;
        }
        for (HistoricoVO historicoExistente : this.getHistoricoNaoAproveitados()) {
            if (historicoExistente.getCodigo().equals(historicoAdicionar.getCodigo())) {
                // se já adicionado, nao adiciona-se novamente
                return;
            }
        }
        this.getHistoricoNaoAproveitados().add(historicoAdicionar);
    }
    
    public void adicionarHistoricoResultadoProcessamento(Date data, String responsavel, String descricao, Boolean subordinado) {
        String base = this.getResultadoProcessamento();
        if (base.equals("")) {
            base = Uteis.getDataComHora(data) + " - " + descricao + " - Usuário: " + responsavel;
        } else {
            if (!subordinado) {
                base = base + "\n" + "--------------------------------------------------------------------------" + 
                              "\n" + Uteis.getDataComHora(data) + " - " + descricao + " - Usuário: " + responsavel;
            } else {
                base = base + "\n" + "              -> " + descricao + " - Usuário: " + responsavel;
            }
        }
        this.setResultadoProcessamento(base);
    }

    /**
     * @return the historicoAproveitadosTransferencia
     */
    public List<HistoricoVO> getHistoricoAproveitados() {
        if (historicoAproveitados == null) {
            historicoAproveitados = new ArrayList<HistoricoVO>(0);
        }        
        return historicoAproveitados;
    }

    /**
     * @param historicoAproveitadosTransferencia the historicoAproveitadosTransferencia to set
     */
    public void setHistoricoAproveitados(List<HistoricoVO> historicoAproveitados) {
        this.historicoAproveitados = historicoAproveitados;
    }

    /**
     * @return the mapasEquivalenciaAproveitadosTransferenciaComHistoricos
     */
    public List<MapaEquivalenciaDisciplinaVO> getMapasEquivalenciaAproveitadosComHistoricos() {
        if (mapasEquivalenciaAproveitadosComHistoricos == null) {
            mapasEquivalenciaAproveitadosComHistoricos = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
        }
        return mapasEquivalenciaAproveitadosComHistoricos;
    }

    /**
     * @param mapasEquivalenciaAproveitadosTransferenciaComHistoricos the mapasEquivalenciaAproveitadosTransferenciaComHistoricos to set
     */
    public void setMapasEquivalenciaAproveitadosComHistoricos(List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAproveitadosComHistoricos) {
        this.mapasEquivalenciaAproveitadosComHistoricos = mapasEquivalenciaAproveitadosComHistoricos;
    }
    
    public void incremetarNrAlertasCriticos() {
        setNrAlertasCriticos(getNrAlertasCriticos() + 1);
    }

    /**
     * @return the nrAlertasCriticos
     */
    public Integer getNrAlertasCriticos() {
        if (nrAlertasCriticos == null) {
            nrAlertasCriticos = 0;
        }
        return nrAlertasCriticos;
    }

    /**
     * @param nrAlertasCriticos the nrAlertasCriticos to set
     */
    public void setNrAlertasCriticos(Integer nrAlertasCriticos) {
        this.nrAlertasCriticos = nrAlertasCriticos;
    }

    /**
     * @return the periodoLetivoUtilizarDisciplinasCursar
     */
    public PeriodoLetivoVO getPeriodoLetivoUtilizarDisciplinasCursar() {
        if (periodoLetivoUtilizarDisciplinasCursar == null) {
            periodoLetivoUtilizarDisciplinasCursar = new PeriodoLetivoVO();
        }
        return periodoLetivoUtilizarDisciplinasCursar;
    }

    /**
     * @param periodoLetivoUtilizarDisciplinasCursar the periodoLetivoUtilizarDisciplinasCursar to set
     */
    public void setPeriodoLetivoUtilizarDisciplinasCursar(PeriodoLetivoVO periodoLetivoUtilizarDisciplinasCursar) {
        this.periodoLetivoUtilizarDisciplinasCursar = periodoLetivoUtilizarDisciplinasCursar;
    }

	public MatriculaVO getMatriculaDestinoAproveitamentoVO() {
		if (matriculaDestinoAproveitamentoVO == null) {
			matriculaDestinoAproveitamentoVO = new MatriculaVO();
		}
		return matriculaDestinoAproveitamentoVO;
	}

	public void setMatriculaDestinoAproveitamentoVO(MatriculaVO matriculaDestinoAproveitamentoVO) {
		this.matriculaDestinoAproveitamentoVO = matriculaDestinoAproveitamentoVO;
	}

	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaUtilizadoAproveitamento() {
		if (mapaEquivalenciaUtilizadoAproveitamento == null) {
			mapaEquivalenciaUtilizadoAproveitamento = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaUtilizadoAproveitamento;
	}

	public void setMapaEquivalenciaUtilizadoAproveitamento(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaUtilizadoAproveitamento) {
		this.mapaEquivalenciaUtilizadoAproveitamento = mapaEquivalenciaUtilizadoAproveitamento;
	}

	public Boolean getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento() {
		if (realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento == null) {
			realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = Boolean.TRUE;
		}
		return realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public void setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento) {
		this.realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public Boolean getUtilizarAnoSemestreAtualDisciplinaAprovada() {
		if (utilizarAnoSemestreAtualDisciplinaAprovada == null) {
			utilizarAnoSemestreAtualDisciplinaAprovada = Boolean.FALSE;
		}
		return utilizarAnoSemestreAtualDisciplinaAprovada;
	}

	public void setUtilizarAnoSemestreAtualDisciplinaAprovada(Boolean utilizarAnoSemestreAtualDisciplinaAprovada) {
		this.utilizarAnoSemestreAtualDisciplinaAprovada = utilizarAnoSemestreAtualDisciplinaAprovada;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoCursoDestino() {
		if (configuracaoAcademicoCursoDestino == null) {
			configuracaoAcademicoCursoDestino = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoCursoDestino;
	}

	public void setConfiguracaoAcademicoCursoDestino(ConfiguracaoAcademicoVO configuracaoAcademicoCursoDestino) {
		this.configuracaoAcademicoCursoDestino = configuracaoAcademicoCursoDestino;
	}

	public AproveitamentoDisciplinaVO getAproveitamentoDisciplinaVO() {
		if (aproveitamentoDisciplinaVO == null) {
			aproveitamentoDisciplinaVO = new AproveitamentoDisciplinaVO();
		}
		return aproveitamentoDisciplinaVO;
	}

	public void setAproveitamentoDisciplinaVO(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) {
		this.aproveitamentoDisciplinaVO = aproveitamentoDisciplinaVO;
	}

	public String getAnoPadrao() {
		if (anoPadrao == null) {
			anoPadrao = "";
		}
		return anoPadrao;
	}

	public void setAnoPadrao(String anoPadrao) {
		this.anoPadrao = anoPadrao;
	}

	public String getSemestrePadrao() {
		if (semestrePadrao == null) {
			semestrePadrao = "";
		}
		return semestrePadrao;
	}

	public void setSemestrePadrao(String semestrePadrao) {
		this.semestrePadrao = semestrePadrao;
	}

	public Boolean getAproveitamentoPorIsencao() {
		if (aproveitamentoPorIsencao == null) {
			aproveitamentoPorIsencao = Boolean.FALSE;
		}
		return aproveitamentoPorIsencao;
	}

	public void setAproveitamentoPorIsencao(Boolean aproveitamentoPorIsencao) {
		this.aproveitamentoPorIsencao = aproveitamentoPorIsencao;
	}

	public String getDescricaoComplementacao() {
		if (descricaoComplementacao == null) {
			descricaoComplementacao = "";
		}
		return descricaoComplementacao;
	}

	public void setDescricaoComplementacao(String descricaoComplementacao) {
		this.descricaoComplementacao = descricaoComplementacao;
	}

	public String getSituacaoHistoricoConcessaoEntreMatriculas() {
		if (situacaoHistoricoConcessaoEntreMatriculas == null) {
			situacaoHistoricoConcessaoEntreMatriculas = "";
		}
		return situacaoHistoricoConcessaoEntreMatriculas;
	}

	public void setSituacaoHistoricoConcessaoEntreMatriculas(String situacaoHistoricoConcessaoEntreMatriculas) {
		this.situacaoHistoricoConcessaoEntreMatriculas = situacaoHistoricoConcessaoEntreMatriculas;
	}

	public String getTipoAproveitamentoDisciplinasEntreMatriculas() {
		if (tipoAproveitamentoDisciplinasEntreMatriculas == null) {
			tipoAproveitamentoDisciplinasEntreMatriculas = "AP";
		}
		return tipoAproveitamentoDisciplinasEntreMatriculas;
	}

	public void setTipoAproveitamentoDisciplinasEntreMatriculas(String tipoAproveitamentoDisciplinasEntreMatriculas) {
		this.tipoAproveitamentoDisciplinasEntreMatriculas = tipoAproveitamentoDisciplinasEntreMatriculas;
	}

	public List<HistoricoVO> getHistoricoAlocadosMapaEquivalenciaAproveitados() {
		if (historicoAlocadosMapaEquivalenciaAproveitados == null) {
			historicoAlocadosMapaEquivalenciaAproveitados = new ArrayList<HistoricoVO>(0);
		}
		return historicoAlocadosMapaEquivalenciaAproveitados;
	}

	public void setHistoricoAlocadosMapaEquivalenciaAproveitados(List<HistoricoVO> historicoAlocadosMapaEquivalenciaAproveitados) {
		this.historicoAlocadosMapaEquivalenciaAproveitados = historicoAlocadosMapaEquivalenciaAproveitados;
	}

	public List<MapaEquivalenciaDisciplinaVO> getMapasEquivalenciaAplicarComposicao() {
		if (mapasEquivalenciaAplicarComposicao == null) {
			mapasEquivalenciaAplicarComposicao = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
		}
		return mapasEquivalenciaAplicarComposicao;
	}

	public void setMapasEquivalenciaAplicarComposicao(List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAplicarComposicao) {
		this.mapasEquivalenciaAplicarComposicao = mapasEquivalenciaAplicarComposicao;
	}
	
}
