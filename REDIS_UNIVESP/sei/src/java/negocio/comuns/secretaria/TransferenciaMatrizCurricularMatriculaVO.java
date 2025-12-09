package negocio.comuns.secretaria;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "transferenciaMatrizCurricularMatriculaVO")
public class TransferenciaMatrizCurricularMatriculaVO extends SuperVO {

    private Integer codigo;
    private MatriculaVO matriculaVO;
    private MatriculaPeriodoVO matriculaPeriodoUltimoPeriodoVO;
    private TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO;
    
    /**
     * Atributo TRANSIENT que será utilizado para armzanar todos os historicos que
     * o aluno esta cursando e que podem ser migrados utilizando o controle CURSANDO
     * POR CORRESPODENCIA.
     */
    private List<HistoricoVO> disciplinasAlunoCursando;
    
    /**
     * TRANSIENT 
     * atributo transient e utilizado para manter todos os históricos que aluno já possui na matriz
     * destino para o qual o mesmo está indo. Controle criado para permitir que um aluno possa retornar
     * para uma matriz na qual ele já pertenceu. Isto ocorre, por exemplo, quando o aluno migra de uma curso
     * A para um curso B. Estuda por um período no curso B e depois decide retornar para o cursa A.
     */
    private MatriculaComHistoricoAlunoVO matriculaDestinoComHistoricoVO;
    
    private Boolean validarHistoricoJaEstaAprovadoMatrizDestino;
    
    /**
     * TRANSIENT - utilizados no controle de removacao das disciplinas cursando por correspondencia
     * realizada por meio de integracao com a tela de RenovacaoMatriculaControle
     */
    private Boolean eliminandoDisciplinasCursandoPorCorrespodencia;
    private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorCorrespondencia;
    private List<HistoricoVO> listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina;
    private List<MatriculaPeriodoTurmaDisciplinaVO> listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs;
    private List<MatriculaPeriodoTurmaDisciplinaVO> listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs;
    
    /**
     * TRANSIENT 
     * Turno para o qual as disciplinas que estao sendo cursadas por correspodencia serao migradas.
     * Com esta opcao será possível ao usuario definir um novo turno / turma para qual o aluno irá.
     * As vezes, ao migrar de matriz, o aluno terá que também optar por outro turno. Exemplo, para 
     * colégio, que possui o mesmo curso na modalidade tradicional e integral (neste caso, o integral
     * sempre terá um turno diferenciado para ele. 
     */
    private TurnoVO turnoMigrarMatricula;	
	
    /**
     * Aguardando_Processamento
     * Realizada_Com_Sucesso
     * Realizada_Com_Sucesso_Migrada
     * Erro_Durante_Processamento
     * Cancelada_Transferencia
     */	 
    private String situacao;
    private String observacoes;
    private Integer nrAlertasCriticos;
    private String resultadoProcessamento;
    private UsuarioVO responsavel;
    private Date dataProcessamento;
    private Integer nrDisciplinasCursandoPorCorrespondencia;

    /**
     * ATRIBUTOS TRANSIENT´S UTILIZADOS NO CONTROLE
     * DA TRANSFERENCIA DE MATRIZ CURRICULAR
     */
    private List<HistoricoVO> historicoNaoAproveitadosTransferencia;
    private List<HistoricoVO> historicoAproveitadosTransferencia;
    private List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAproveitadosTransferenciaComHistoricos;
    private PeriodoLetivoVO periodoLetivoUtilizarDisciplinasCursar;

    private List<HistoricoVO> historicoAlocadosMapaEquivalenciaAproveitados;
    private List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAproveitadosComHistoricos;
    private List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAplicarComposicao;
    
    private Integer nrDisciplinasCursandoPorEquivalencia;
    private Boolean eliminandoDisciplinasCursandoPorEquivalencia;
    private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorEquivalenciaVOs;
	
    private static final long serialVersionUID = 1L;

    public TransferenciaMatrizCurricularMatriculaVO() {
    }

    @XmlElement(name = "codigo")
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
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the historicoNaoAproveitadosTransferencia
     */
    public List<HistoricoVO> getHistoricoNaoAproveitadosTransferencia() {
        if (historicoNaoAproveitadosTransferencia == null) {
            historicoNaoAproveitadosTransferencia = new ArrayList<HistoricoVO>(0);
        }
        return historicoNaoAproveitadosTransferencia;
    }

    /**
     * @param historicoNaoAproveitadosTransferencia the historicoNaoAproveitadosTransferencia to set
     */
    public void setHistoricoNaoAproveitadosTransferencia(List<HistoricoVO> historicoNaoAproveitadosTransferencia) {
        this.historicoNaoAproveitadosTransferencia = historicoNaoAproveitadosTransferencia;
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
        if (getSituacao().equals("RM")) { //realizada e migrada em definitivo para a matriz destino
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
        if (getSituacao().equals("RM")) {
            return "Realizada Com Sucesso - Migrada Nova Matriz";
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

    /**
     * @param dataProcessamento the dataProcessamento to set
     */
    public void setDataProcessamento(Date dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    /**
     * @return the matriculaPeriodoUltimoPeriodoVO
     */
    public MatriculaPeriodoVO getMatriculaPeriodoUltimoPeriodoVO() {
        if (matriculaPeriodoUltimoPeriodoVO == null) {
            matriculaPeriodoUltimoPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoUltimoPeriodoVO;
    }

    /**
     * @param matriculaPeriodoUltimoPeriodoVO the matriculaPeriodoUltimoPeriodoVO to set
     */
    public void setMatriculaPeriodoUltimoPeriodoVO(MatriculaPeriodoVO matriculaPeriodoUltimoPeriodoVO) {
        this.matriculaPeriodoUltimoPeriodoVO = matriculaPeriodoUltimoPeriodoVO;
    }
    
    public void adicionarHistoricoResultadoProcessamento(Date data, String responsavel, String descricao) {
        adicionarHistoricoResultadoProcessamento(data, responsavel, descricao, false);
    }
    
    public void adicionarHistoricoListaHistoricoNaoAproveitadosTransferencia(HistoricoVO historicoAdicionar) {
        if ((!historicoAdicionar.getAprovado()) &&
            (!historicoAdicionar.getReprovado()) &&
            (!historicoAdicionar.getCursando())) {
            //se trata-se de um histórico que nao foi APROVADO, REPROVADO E CURSANDO
            //Não há por que adicionar para lista de pendências
            return;
        }
        for (HistoricoVO historicoExistente : this.getHistoricoNaoAproveitadosTransferencia()) {
            if (historicoExistente.getCodigo().equals(historicoAdicionar.getCodigo())) {
                // se já adicionado, nao adiciona-se novamente
                return;
            }
        }
        this.getHistoricoNaoAproveitadosTransferencia().add(historicoAdicionar);
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
    public List<HistoricoVO> getHistoricoAproveitadosTransferencia() {
        if (historicoAproveitadosTransferencia == null) {
            historicoAproveitadosTransferencia = new ArrayList<HistoricoVO>(0);
        }        
        return historicoAproveitadosTransferencia;
    }

    /**
     * @param historicoAproveitadosTransferencia the historicoAproveitadosTransferencia to set
     */
    public void setHistoricoAproveitadosTransferencia(List<HistoricoVO> historicoAproveitadosTransferencia) {
        this.historicoAproveitadosTransferencia = historicoAproveitadosTransferencia;
    }

    /**
     * @return the mapasEquivalenciaAproveitadosTransferenciaComHistoricos
     */
    public List<MapaEquivalenciaDisciplinaVO> getMapasEquivalenciaAproveitadosTransferenciaComHistoricos() {
        if (mapasEquivalenciaAproveitadosTransferenciaComHistoricos == null) {
            mapasEquivalenciaAproveitadosTransferenciaComHistoricos = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
        }
        return mapasEquivalenciaAproveitadosTransferenciaComHistoricos;
    }

    /**
     * @param mapasEquivalenciaAproveitadosTransferenciaComHistoricos the mapasEquivalenciaAproveitadosTransferenciaComHistoricos to set
     */
    public void setMapasEquivalenciaAproveitadosTransferenciaComHistoricos(List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAproveitadosTransferenciaComHistoricos) {
        this.mapasEquivalenciaAproveitadosTransferenciaComHistoricos = mapasEquivalenciaAproveitadosTransferenciaComHistoricos;
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
     * @return the transferenciaMatrizCurricularVO
     */
    public TransferenciaMatrizCurricularVO getTransferenciaMatrizCurricularVO() {
        if (transferenciaMatrizCurricularVO == null) {
            transferenciaMatrizCurricularVO = new TransferenciaMatrizCurricularVO();
        }
        return transferenciaMatrizCurricularVO;
    }

    /**
     * @param transferenciaMatrizCurricularVO the transferenciaMatrizCurricularVO to set
     */
    public void setTransferenciaMatrizCurricularVO(TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO) {
        this.transferenciaMatrizCurricularVO = transferenciaMatrizCurricularVO;
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


	public List<HistoricoVO> getHistoricoAlocadosMapaEquivalenciaAproveitados() {
		if (historicoAlocadosMapaEquivalenciaAproveitados == null) {
			historicoAlocadosMapaEquivalenciaAproveitados = new ArrayList<HistoricoVO>(0);
		}
		return historicoAlocadosMapaEquivalenciaAproveitados;
	}

	public void setHistoricoAlocadosMapaEquivalenciaAproveitados(List<HistoricoVO> historicoAlocadosMapaEquivalenciaAproveitados) {
		this.historicoAlocadosMapaEquivalenciaAproveitados = historicoAlocadosMapaEquivalenciaAproveitados;
	}

	public List<MapaEquivalenciaDisciplinaVO> getMapasEquivalenciaAproveitadosComHistoricos() {
		if (mapasEquivalenciaAproveitadosComHistoricos == null) {
			mapasEquivalenciaAproveitadosComHistoricos = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
		}
		return mapasEquivalenciaAproveitadosComHistoricos;
	}

	public void setMapasEquivalenciaAproveitadosComHistoricos(List<MapaEquivalenciaDisciplinaVO> mapasEquivalenciaAproveitadosComHistoricos) {
		this.mapasEquivalenciaAproveitadosComHistoricos = mapasEquivalenciaAproveitadosComHistoricos;
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

	public Integer getNrDisciplinasCursandoPorCorrespondencia() {
		if (nrDisciplinasCursandoPorCorrespondencia == null) {
			nrDisciplinasCursandoPorCorrespondencia = 0;
		}
		return nrDisciplinasCursandoPorCorrespondencia;
	}

	public void setNrDisciplinasCursandoPorCorrespondencia(Integer nrDisciplinasCursandoPorCorrespondencia) {
		this.nrDisciplinasCursandoPorCorrespondencia = nrDisciplinasCursandoPorCorrespondencia;
	}

	public Boolean getEliminandoDisciplinasCursandoPorCorrespodencia() {
		if (eliminandoDisciplinasCursandoPorCorrespodencia == null) {
			eliminandoDisciplinasCursandoPorCorrespodencia = Boolean.FALSE;
		}
		return eliminandoDisciplinasCursandoPorCorrespodencia;
	}

	public void setEliminandoDisciplinasCursandoPorCorrespodencia(Boolean eliminandoDisciplinasCursandoPorCorrespodencia) {
		this.eliminandoDisciplinasCursandoPorCorrespodencia = eliminandoDisciplinasCursandoPorCorrespodencia;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasPorCorrespondencia() {
		if (listaDisciplinasPorCorrespondencia == null) {
			listaDisciplinasPorCorrespondencia = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasPorCorrespondencia;
	}

	public void setListaDisciplinasPorCorrespondencia(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorCorrespondencia) {
		this.listaDisciplinasPorCorrespondencia = listaDisciplinasPorCorrespondencia;
	}

	public TurnoVO getTurnoMigrarMatricula() {
		if (turnoMigrarMatricula == null) {
			turnoMigrarMatricula = null;
		}
		return turnoMigrarMatricula;
	}

	public void setTurnoMigrarMatricula(TurnoVO turnoMigrarMatricula) {
		this.turnoMigrarMatricula = turnoMigrarMatricula;
	}

	public List<HistoricoVO> getListaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina() {
		if (listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina == null) {
			listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina;
	}

	public void setListaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina(List<HistoricoVO> listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina) {
		this.listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina = listaHistoricosAprovadosManterSemVinculoMatriculaPeriodoTurmaDiscipina;
	}

	public Boolean getValidarHistoricoJaEstaAprovadoMatrizDestino() {
		if (validarHistoricoJaEstaAprovadoMatrizDestino == null) {
			validarHistoricoJaEstaAprovadoMatrizDestino = Boolean.FALSE;
		}
		return validarHistoricoJaEstaAprovadoMatrizDestino;
	}

	public void setValidarHistoricoJaEstaAprovadoMatrizDestino(Boolean validarHistoricoJaEstaAprovadoMatrizDestino) {
		this.validarHistoricoJaEstaAprovadoMatrizDestino = validarHistoricoJaEstaAprovadoMatrizDestino;
	}

	public MatriculaComHistoricoAlunoVO getMatriculaDestinoComHistoricoVO() {
		if (matriculaDestinoComHistoricoVO == null) {
			matriculaDestinoComHistoricoVO = new MatriculaComHistoricoAlunoVO();
		}
		return matriculaDestinoComHistoricoVO;
	}

	public void setMatriculaDestinoComHistoricoVO(MatriculaComHistoricoAlunoVO matriculaDestinoComHistoricoVO) {
		this.matriculaDestinoComHistoricoVO = matriculaDestinoComHistoricoVO;
	}

	public List<HistoricoVO> getDisciplinasAlunoCursando() {
		if (disciplinasAlunoCursando == null) {
			disciplinasAlunoCursando = new ArrayList<HistoricoVO>(0);
		}
		return disciplinasAlunoCursando;
	}

	public void setDisciplinasAlunoCursando(List<HistoricoVO> disciplinasAlunoCursando) {
		this.disciplinasAlunoCursando = disciplinasAlunoCursando;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs() {
		if (listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs == null) {
			listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs;
	}

	public void setListaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs) {
		this.listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs = listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs;
	}
	
	public Integer getNrDisciplinasCursandoPorEquivalencia() {
		if (nrDisciplinasCursandoPorEquivalencia == null) {
			nrDisciplinasCursandoPorEquivalencia = 0;
		}
		return nrDisciplinasCursandoPorEquivalencia;
	}

	public void setNrDisciplinasCursandoPorEquivalencia(Integer nrDisciplinasCursandoPorEquivalencia) {
		this.nrDisciplinasCursandoPorEquivalencia = nrDisciplinasCursandoPorEquivalencia;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs() {
		if (listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs == null) {
			listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs;
	}

	public void setListaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs) {
		this.listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs = listaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs;
	}

	public Boolean getEliminandoDisciplinasCursandoPorEquivalencia() {
		if (eliminandoDisciplinasCursandoPorEquivalencia == null) {
			eliminandoDisciplinasCursandoPorEquivalencia = false;
		}
		return eliminandoDisciplinasCursandoPorEquivalencia;
	}

	public void setEliminandoDisciplinasCursandoPorEquivalencia(Boolean eliminandoDisciplinasCursandoPorEquivalencia) {
		this.eliminandoDisciplinasCursandoPorEquivalencia = eliminandoDisciplinasCursandoPorEquivalencia;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasPorEquivalenciaVOs() {
		if (listaDisciplinasPorEquivalenciaVOs == null) {
			listaDisciplinasPorEquivalenciaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasPorEquivalenciaVOs;
	}

	public void setListaDisciplinasPorEquivalenciaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorEquivalenciaVOs) {
		this.listaDisciplinasPorEquivalenciaVOs = listaDisciplinasPorEquivalenciaVOs;
	}
}
