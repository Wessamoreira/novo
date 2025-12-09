package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.ControleConcorrenciaHorarioTurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class HorarioTurmaVO extends SuperVO {

    private DisciplinaVO disciplina;
    private DisciplinaVO disciplinaSubstituta;
    private DisciplinaVO disciplinaAtual;
    private PessoaVO professor;
    private PessoaVO professorSubstituto;
    private PessoaVO professorAtual;
    private TurnoVO turno;
    private Integer codigo;
    private TurmaVO turma;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<HorarioTurmaProfessorDisciplinaVO> listaProfessorDisciplina;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<HorarioTurmaDiaVO> horarioTurmaDiaVOs;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<Integer> listaCodigoProfessor;
    private Date dia;
    private Date diaInicio;
    private Date diaFim;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<CalendarioHorarioAulaVO<HorarioTurmaDiaVO>> calendarioHorarioAulaVOs;
    private String anoVigente;
    private String semestreVigente;
    private String observacao;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<ControleConcorrenciaHorarioTurmaVO> controleConcorrenciaHorarioTurmaVOs;
    
    private Integer minutosInicioTurnoADarAula;
    private Integer minutosFinalTurnoADarAula;
    public static final long serialVersionUID = 1L;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs;
    /**
     * transient
     */    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<HorarioTurmaDiaItemVO> listaTemporariaHorarioTurmaDiaItemVOLog;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemGoogleMeet;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean gerarEventoAulaOnLineGoogleMeet;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private boolean gerarClassroomGoogle = false;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private boolean gerarSalaBlackboard = false;
    
    //transient usado somente em programaçao aula controle para controle do fluxo de notifição via comunicado interno 
    private List<FuncionarioVO> responsaveisInternoAptoReceberNotificacaoCronogramaAula;
    

    public HorarioTurmaVO() {
        inicializarDados();
    }

    public void inicializarDados() {
        setCodigo(0);
        setTurma(new TurmaVO());
        setNovoObj(true);
        setDia(new Date());
    }

    public static void validarDadosAlteracaoHorario(HorarioTurmaVO obj) throws ConsistirException {
        if (obj.getDisciplinaSubstituta() == null || obj.getDisciplinaSubstituta().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA SUBSTITUTA (Programação Aula) deve ser informado.");
        }

        if (obj.getProfessorSubstituto() == null || obj.getProfessorSubstituto().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo PROFESSOR SUBSTITUTO (Programação Aula) deve ser informado.");
        }
        if (obj.getProfessorSubstituto().getCodigo().intValue() == obj.getProfessorAtual().getCodigo() 
        		&& obj.getDisciplinaSubstituta().getCodigo().intValue() == obj.getDisciplinaAtual().getCodigo().intValue()
        		&& obj.getSalaAtual().getCodigo().equals(obj.getSalaSubstituta().getCodigo().intValue())) {
            throw new ConsistirException("O campo PROFESSOR SUBSTITUTO ou DISCIPLINA SUBSTITUTA ou SALA SUBSTITUTA (Programação Aula) deve ser diferentes da ATUAL.");
        }

    }

    public void adicionarListaProfessorDisciplina(Integer horario, DisciplinaVO disciplina, PessoaVO professor, Integer turma) throws Exception {
        if (disciplina.getCodigo().intValue() != 0 && professor.getCodigo().intValue() != 0 && turma.intValue() != 0) {
            int index = 0;
            Iterator<HorarioTurmaProfessorDisciplinaVO> i = getListaProfessorDisciplina().iterator();
            while (i.hasNext()) {
                HorarioTurmaProfessorDisciplinaVO objExistente = (HorarioTurmaProfessorDisciplinaVO) i.next();
                if (objExistente.getTurma().getCodigo().equals(turma) && objExistente.getDisciplina().getCodigo().equals(disciplina.getCodigo()) && objExistente.getProfessor().getCodigo().equals(professor.getCodigo())) {
                    objExistente.setHorarios(objExistente.getHorarios() + "," + String.valueOf(horario));
                    getListaProfessorDisciplina().set(index, objExistente);
                    return;
                }
                index++;
            }
            HorarioTurmaProfessorDisciplinaVO obj = new HorarioTurmaProfessorDisciplinaVO();
            obj.getTurma().setCodigo(turma);
            obj.setProfessor(professor);
            obj.setDisciplina(disciplina);
            obj.setHorarios(String.valueOf(horario));
            getListaProfessorDisciplina().add(obj);
        }
    }

    public List<HorarioAlunoDiaVO> consultarHorarioAlunoDiaADiaPorDisciplina(DisciplinaVO disciplina) {
        List<HorarioAlunoDiaVO> objs = new ArrayList<HorarioAlunoDiaVO>(0);
        for (HorarioTurmaDiaVO obj : getHorarioTurmaDiaVOs()) {
            List<HorarioAlunoDiaItemVO> itens = obj.consultarDisciplinaHorarioAlunoDia(disciplina, getTurma());
            if (!itens.isEmpty()) {
                HorarioAlunoDiaVO objDia = new HorarioAlunoDiaVO();
                objDia.setData(obj.getData());
                objDia.setHorarioAlunoDiaItemVOs(itens);
                objs.add(objDia);
            }
        }
        return objs;
    }

    public List<HorarioAlunoDiaVO> consultarHorarioAlunoDiaADiaPorDisciplinaTurma(DisciplinaVO disciplina, TurmaVO turmaMatriculaPeriodoTurmaDisciplina) {
        List<HorarioAlunoDiaVO> objs = new ArrayList<HorarioAlunoDiaVO>(0);
        for (HorarioTurmaDiaVO obj : getHorarioTurmaDiaVOs()) {
            List<HorarioAlunoDiaItemVO> itens = obj.consultarDisciplinaTurmaHorarioAlunoDia(disciplina, getTurma(), turmaMatriculaPeriodoTurmaDisciplina);
            if (!itens.isEmpty()) {
                HorarioAlunoDiaVO objDia = new HorarioAlunoDiaVO();
                objDia.setData(obj.getData());
                objDia.setHorarioAlunoDiaItemVOs(itens);
                objs.add(objDia);
            }
        }
        return objs;
    }

    public void montarDadosHorarioTurma() throws Exception {
        setListaProfessorDisciplina(new ArrayList<>());
        montarDadosHorarioDisciplinaProfessorDia();
    }

    public Integer getCodigoDisciplina(Integer nrAula, Date dia) {

        if (dia != null) {
            return getCodigoDisciplinaPorDia(nrAula, dia);
        }
        return 0;

    }

    public Integer getCodigoProfessor(Integer nrAula, Date dia) {

        if (dia != null) {
            return getCodigoProfessorPorDia(nrAula, dia);
        }
        return 0;

    }

    private Integer getCodigoDisciplinaPorDia(Integer nrAula, Date dia) {
        for (HorarioTurmaDiaVO item : getHorarioTurmaDiaVOs()) {
            if (Uteis.getData(item.getData()).equals(Uteis.getData(dia))) {
                return item.getCodigoDisciplina(nrAula);
            }
        }
        return 0;
    }

    private Integer getCodigoProfessorPorDia(Integer nrAula, Date dia) {
        for (HorarioTurmaDiaVO item : getHorarioTurmaDiaVOs()) {
            if (Uteis.getData(item.getData()).equals(Uteis.getData(dia))) {
                return item.getCodigoProfessor(nrAula);
            }
        }
        return 0;
    }

    public void montarDadosHorarioDisciplinaProfessorDia() throws Exception {
    	getListaProfessorDisciplina().clear();
        for (HorarioTurmaDiaVO item : getHorarioTurmaDiaVOs()) {
            adicionarListaProfessorDisciplinaPorDia(item);
        }
    }

    public void adicionarListaProfessorDisciplinaPorDia(HorarioTurmaDiaVO item) throws Exception {
        for (HorarioTurmaDiaItemVO diaItem : item.getHorarioTurmaDiaItemVOs()) {
            if (!diaItem.getDisciplinaLivre() && !diaItem.getProfessorLivre()) {
                adicionarListaProfessorDisciplina(diaItem.getNrAula(), diaItem.getDisciplinaVO(), diaItem.getFuncionarioVO(), getTurma().getCodigo());
            }
        }
    }


    public void removerHorarioTurmaPorDiaPorDia(HorarioTurmaDiaVO obj) {
        int index = 0;
        for (HorarioTurmaDiaVO objExistente : getHorarioTurmaDiaVOs()) {
            if (Uteis.getDateTime(objExistente.getData(), 23, 59, 59).compareTo(Uteis.getDateTime(obj.getData(), 23, 59, 59)) == 0) {
                getHorarioTurmaDiaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public void adicinarHorarioTurmaPorDiaPorDia(HorarioTurmaDiaVO obj, Boolean substituir, FeriadoVO feriadoVO) throws Exception {
        int index = 0;
        for (HorarioTurmaDiaVO objExistente : getHorarioTurmaDiaVOs()) {
            if (Uteis.getDataJDBC(objExistente.getData()).equals(Uteis.getDataJDBC(obj.getData())) ) {
                if (substituir) {
                    getHorarioTurmaDiaVOs().set(index, obj);
                }
                return;
            }
            index++;
        }
        if (feriadoVO != null) {            
            obj.setFeriado(feriadoVO);
            obj.setHorarioTurma(null);
            obj.setHorarioTurmaDiaItemVOs(null);
        }
        getHorarioTurmaDiaVOs().add(obj);
        Ordenacao.ordenarLista(getHorarioTurmaDiaVOs(), "data");

    }
    
    public void montarLogResultadoAcao(String operacao, Integer nrAula, String horario, Date dataBase, DisciplinaVO disciplina, PessoaVO professor )  {
    	HorarioTurmaDiaItemVO htdi = new HorarioTurmaDiaItemVO();
    	htdi.setOperacaoLog(operacao);
    	htdi.setNrAula(nrAula);
    	htdi.setHorario(horario);
    	htdi.setData(dataBase);
    	htdi.setDisciplinaVO(disciplina);
    	htdi.setFuncionarioVO(professor);
    	getListaTemporariaHorarioTurmaDiaItemVOLog().add(htdi);
    }

    public boolean getProfessorJaAdicionadoListaCodigoProfessor(Integer codigoProfessor) {
        for (Integer codigoProfessorExistente : getListaCodigoProfessor()) {
            if (codigoProfessorExistente.intValue() == codigoProfessor.intValue()) {
                return true;
            }
        }
        getListaCodigoProfessor().add(codigoProfessor);
        return false;
    }

    public DisciplinaVO getDisciplinaAtual() {
        if (disciplinaAtual == null) {
            disciplinaAtual = new DisciplinaVO();
        }
        return disciplinaAtual;
    }

    public void setDisciplinaAtual(DisciplinaVO disciplinaAtual) {
        this.disciplinaAtual = disciplinaAtual;
    }

    public PessoaVO getProfessorAtual() {
        if (professorAtual == null) {
            professorAtual = new PessoaVO();
        }
        return professorAtual;
    }

    public void setProfessorAtual(PessoaVO professorAtual) {
        this.professorAtual = professorAtual;
    }

    public DisciplinaVO getDisciplinaSubstituta() {
        if (disciplinaSubstituta == null) {
            disciplinaSubstituta = new DisciplinaVO();
        }
        return disciplinaSubstituta;
    }

    public void setDisciplinaSubstituta(DisciplinaVO disciplinaSubstituta) {
        this.disciplinaSubstituta = disciplinaSubstituta;
    }

    public PessoaVO getProfessorSubstituto() {
        if (professorSubstituto == null) {
            professorSubstituto = new PessoaVO();
        }
        return professorSubstituto;
    }

    public void setProfessorSubstituto(PessoaVO professorSubstituto) {
        this.professorSubstituto = professorSubstituto;
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
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

    public Boolean getIsExisteHorarioProgramadoDia() {
        for (HorarioTurmaDiaVO horarioTurmaDiaVO : getHorarioTurmaDiaVOs()) {
            if (horarioTurmaDiaVO.getIsAulaProgramada()) {
                return true;
            }
        }
        return false;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public List<HorarioTurmaProfessorDisciplinaVO> getListaProfessorDisciplina() {
        if (listaProfessorDisciplina == null) {
            listaProfessorDisciplina = new ArrayList<>();
        }
        return listaProfessorDisciplina;
    }

    public void setListaProfessorDisciplina(List<HorarioTurmaProfessorDisciplinaVO> listaProfessorDisciplina) {
        this.listaProfessorDisciplina = listaProfessorDisciplina;
    }

    public List<HorarioTurmaDiaVO> getHorarioTurmaDiaVOs() {
        if (horarioTurmaDiaVOs == null) {
            horarioTurmaDiaVOs = new ArrayList<HorarioTurmaDiaVO>(0);
        }
        return horarioTurmaDiaVOs;
    }

    public void setHorarioTurmaDiaVOs(List<HorarioTurmaDiaVO> horarioTurmaDiaVOs) {
        this.horarioTurmaDiaVOs = horarioTurmaDiaVOs;
    }

//    public TipoHorarioTurma getTipoHorarioTurma() {
//        if (tipoHorarioTurma == null) {
//            tipoHorarioTurma = TipoHorarioTurma.SEMANAL;
//        }
//        return tipoHorarioTurma;
//    }
//
//    public void setTipoHorarioTurma(TipoHorarioTurma tipoHorarioTurma) {
//        this.tipoHorarioTurma = tipoHorarioTurma;
//    }
//    public boolean getSemanal() {
//        if (getTipoHorarioTurma().equals(TipoHorarioTurma.SEMANAL)) {
//            return true;
//        }
//        return false;
//    }
    
     public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public List<Integer> getListaCodigoProfessor() {
        if (listaCodigoProfessor == null) {
            listaCodigoProfessor = new ArrayList<Integer>();
        }
        return listaCodigoProfessor;
    }

    public void setListaCodigoProfessor(List<Integer> listaCodigoProfessor) {
        this.listaCodigoProfessor = listaCodigoProfessor;
    }

    public Date getDiaFim() {
        if (diaFim == null) {
            diaFim = new Date();
        }
        return diaFim;
    }

    public void setDiaFim(Date diaFim) {
        this.diaFim = diaFim;
    }

    public Date getDiaInicio() {
        if (diaInicio == null) {
            diaInicio = new Date();
        }
        return diaInicio;
    }

    public void setDiaInicio(Date diaInicio) {
        this.diaInicio = diaInicio;
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaVOs().toArray());
    }

    public List<CalendarioHorarioAulaVO<HorarioTurmaDiaVO>> getCalendarioHorarioAulaVOs() {
        if (calendarioHorarioAulaVOs == null) {
            calendarioHorarioAulaVOs = new ArrayList<CalendarioHorarioAulaVO<HorarioTurmaDiaVO>>();
        }
//        Ordenacao.ordenarLista(calendarioHorarioAulaVOs, "campoOrdenacao");
        return calendarioHorarioAulaVOs;
    }

    public void setCalendarioHorarioAulaVOs(List<CalendarioHorarioAulaVO<HorarioTurmaDiaVO>> calendarioHorarioAulaVOs) {
        this.calendarioHorarioAulaVOs = calendarioHorarioAulaVOs;
    }

    public String getAnoVigente() {
        if (anoVigente == null) {
            anoVigente = "";
        }
        return anoVigente;
    }

    public void setAnoVigente(String anoVigente) {
        this.anoVigente = anoVigente;
    }

    public String getSemestreVigente() {
        if (semestreVigente == null) {
            semestreVigente = "";
        }
        return semestreVigente;
    }

    public void setSemestreVigente(String semestreVigente) {
        this.semestreVigente = semestreVigente;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    /**
     * @return the controleConcorrenciaHorarioTurmaVOs
     */
    public List<ControleConcorrenciaHorarioTurmaVO> getControleConcorrenciaHorarioTurmaVOs() {
        if (controleConcorrenciaHorarioTurmaVOs == null) {
            controleConcorrenciaHorarioTurmaVOs = new ArrayList<>(0);
        }
        return controleConcorrenciaHorarioTurmaVOs;
    }

    /**
     * @param controleConcorrenciaHorarioTurmaVOs the controleConcorrenciaHorarioTurmaVOs to set
     */
    public void setControleConcorrenciaHorarioTurmaVOs(List<ControleConcorrenciaHorarioTurmaVO> controleConcorrenciaHorarioTurmaVOs) {
        this.controleConcorrenciaHorarioTurmaVOs = controleConcorrenciaHorarioTurmaVOs;
    }
    
    public Integer getMinutosInicioTurnoADarAula() {
		if (minutosInicioTurnoADarAula == null) {
			minutosInicioTurnoADarAula = 0;
		}
		return minutosInicioTurnoADarAula;
	}

	public void setMinutosInicioTurnoADarAula(Integer minutosInicioTurnoADarAula) {
		this.minutosInicioTurnoADarAula = minutosInicioTurnoADarAula;
	}

	public Integer getMinutosFinalTurnoADarAula() {
		if (minutosFinalTurnoADarAula == null) {
			minutosFinalTurnoADarAula = 0;
		}
		return minutosFinalTurnoADarAula;
	}

	public void setMinutosFinalTurnoADarAula(Integer minutosFinalTurnoADarAula) {
		this.minutosFinalTurnoADarAula = minutosFinalTurnoADarAula;
	}

	public List<HorarioTurmaDisciplinaProgramadaVO> getHorarioTurmaDisciplinaProgramadaVOs() {
		if (horarioTurmaDisciplinaProgramadaVOs == null) {
			horarioTurmaDisciplinaProgramadaVOs = new ArrayList<HorarioTurmaDisciplinaProgramadaVO>(0);
		}
		return horarioTurmaDisciplinaProgramadaVOs;
	}

	public void setHorarioTurmaDisciplinaProgramadaVOs(List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs) {
		this.horarioTurmaDisciplinaProgramadaVOs = horarioTurmaDisciplinaProgramadaVOs;
	}
	
	
	
	public List<HorarioTurmaDiaItemVO> getListaTemporariaHorarioTurmaDiaItemVOLog() {
		if (listaTemporariaHorarioTurmaDiaItemVOLog == null) {
			listaTemporariaHorarioTurmaDiaItemVOLog = new ArrayList<>();
		}
		return listaTemporariaHorarioTurmaDiaItemVOLog;
	}

	public void setListaTemporariaHorarioTurmaDiaItemVOLog(
			List<HorarioTurmaDiaItemVO> listaTemporariaHorarioTurmaDiaItemVOLog) {
		this.listaTemporariaHorarioTurmaDiaItemVOLog = listaTemporariaHorarioTurmaDiaItemVOLog;
	}
	
	public StringBuilder getTextoAlteracaoHorarioTurmaLog() {
		StringBuilder listaHorariosAlterados = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(getListaTemporariaHorarioTurmaDiaItemVOLog())) {
			listaHorariosAlterados.append("<table style=\"width:100%\" border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\">");
			listaHorariosAlterados.append("<tr><th>Operação</th><th>Nr Aula</th><th>Horário</th><th>Disciplina</th><th>Professor</th></tr>");
			getListaTemporariaHorarioTurmaDiaItemVOLog().stream()
			.forEach(p->{
				listaHorariosAlterados.append("<tr  align=center > ");
				listaHorariosAlterados.append("<td>").append(p.getOperacaoLog()).append("</td> ");
				listaHorariosAlterados.append("<td>").append(p.getNrAula()).append("</td> ");
				listaHorariosAlterados.append("<td>").append(p.getHorario()).append("</td> ");
				listaHorariosAlterados.append("<td>").append(p.getDisciplinaVO().getNome()).append("</td> ");
				listaHorariosAlterados.append("<td>").append(p.getFuncionarioVO().getNome()).append("</td> ");
				listaHorariosAlterados.append("</tr>");
			});
			listaHorariosAlterados.append("</table>");
		}
		return listaHorariosAlterados;
	}



	/**
	 * transiente 
	 */
    private SalaLocalAulaVO sala;
    private SalaLocalAulaVO salaSubstituta;
    private SalaLocalAulaVO salaAtual;

	/**
	 * @return the sala
	 */
	public SalaLocalAulaVO getSala() {
		if (sala == null) {
			sala = new SalaLocalAulaVO();
		}
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(SalaLocalAulaVO sala) {
		this.sala = sala;
	}

	/**
	 * @return the salaSubstituta
	 */
	public SalaLocalAulaVO getSalaSubstituta() {
		if (salaSubstituta == null) {
			salaSubstituta = new SalaLocalAulaVO();
		}
		return salaSubstituta;
	}

	/**
	 * @param salaSubstituta the salaSubstituta to set
	 */
	public void setSalaSubstituta(SalaLocalAulaVO salaSubstituta) {
		this.salaSubstituta = salaSubstituta;
	}

	/**
	 * @return the salaAtual
	 */
	public SalaLocalAulaVO getSalaAtual() {
		if (salaAtual == null) {
			salaAtual = new SalaLocalAulaVO();
		}
		return salaAtual;
	}

	/**
	 * @param salaAtual the salaAtual to set
	 */
	public void setSalaAtual(SalaLocalAulaVO salaAtual) {
		this.salaAtual = salaAtual;
	}

	public List<HorarioTurmaDiaItemVO> getHorarioTurmaDiaItemGoogleMeet() {
		if (horarioTurmaDiaItemGoogleMeet == null) {
			horarioTurmaDiaItemGoogleMeet = new ArrayList<>();
		}
		return horarioTurmaDiaItemGoogleMeet;
	}

	public void setHorarioTurmaDiaItemGoogleMeet(List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemGoogleMeet) {
		this.horarioTurmaDiaItemGoogleMeet = horarioTurmaDiaItemGoogleMeet;
	}
	
	public Boolean getGerarEventoAulaOnLineGoogleMeet() {
		if (gerarEventoAulaOnLineGoogleMeet == null) {
			gerarEventoAulaOnLineGoogleMeet = Boolean.FALSE;
		}
		return gerarEventoAulaOnLineGoogleMeet;
	}

	public void setGerarEventoAulaOnLineGoogleMeet(Boolean gerarEventoAulaOnLineGoogleMeet) {
		this.gerarEventoAulaOnLineGoogleMeet = gerarEventoAulaOnLineGoogleMeet;
	}

	public boolean isGerarClassroomGoogle() {
		return gerarClassroomGoogle;
	}

	public void setGerarClassroomGoogle(boolean gerarClassroomGoogle) {
		this.gerarClassroomGoogle = gerarClassroomGoogle;
	}

	public boolean isGerarSalaBlackboard() {
		return gerarSalaBlackboard;
	}

	public void setGerarSalaBlackboard(boolean gerarSalaBlackboard) {
		this.gerarSalaBlackboard = gerarSalaBlackboard;
	}
	
	
	
	
	
	

	public List<FuncionarioVO> getResponsaveisInternoAptoReceberNotificacaoCronogramaAula() {
		if(responsaveisInternoAptoReceberNotificacaoCronogramaAula == null) {
			responsaveisInternoAptoReceberNotificacaoCronogramaAula = new ArrayList<FuncionarioVO>(0);
		}
		return responsaveisInternoAptoReceberNotificacaoCronogramaAula;
	}

	public void setResponsaveisInternoAptoReceberNotificacaoCronogramaAula(
			List<FuncionarioVO> responsaveisInternoAptoReceberNotificacaoCronogramaAula) {
		this.responsaveisInternoAptoReceberNotificacaoCronogramaAula = responsaveisInternoAptoReceberNotificacaoCronogramaAula;
	}

		

}
