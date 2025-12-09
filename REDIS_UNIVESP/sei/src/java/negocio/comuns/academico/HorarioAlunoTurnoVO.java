package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class HorarioAlunoTurnoVO implements Serializable {

	private TurnoVO turno;
	private List<DisponibilidadeHorarioAlunoVO> disponibilidadeHorarioAlunoVOs;
	private List<HorarioAlunoDiaVO> horarioAlunoDiaVOs;
	private List<HorarioAlunoDiaItemVO> horarioAlunoDiaItemVOs;
	private List<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>> calendarioHorarioAulaVOs;
	private List<HorarioAlunoTurnoNumeroAulaVO> horarioAlunoTurnoNumeroAulaVOs;
	private Date dataSegunda;
	private Date dataTerca;
	private Date dataQuarta;
	private Date dataQuinta;
	private Date dataSexta;
	private Date dataSabado;
	private Date dataDomingo;
	private Date dataInicio;
	private Date dataTermino;
	private FeriadoVO feriadoSegunda;
	private FeriadoVO feriadoTerca;
	private FeriadoVO feriadoQuarta;
	private FeriadoVO feriadoQuinta;
	private FeriadoVO feriadoSexta;
	private FeriadoVO feriadoSabado;
	private FeriadoVO feriadoDomingo;
	
	private Integer index;

	private Boolean naoApresentarProfessorVisaoAluno;
	
	/**
	 * Este Map é utilizado para manter as informações de cada semana do mês de um turno especifico, onde a String<Key> representa o periodo da semana
	 * no seguinte formado <CodigoTurno> - <DataInicioSemana> a <DataTerminoSemana>, depois este Map é convertido em um List e apresentado na visão do aluno
	 */
	private Map<String, HorarioAlunoTurnoVO> horarioAlunoTurnoSemanaVOs;
	
	private boolean possuiAulaDomingo =  true;
	private boolean possuiAulaSegunda =  true;
	private boolean possuiAulaTerca =  true;
	private boolean possuiAulaQuarta =  true;
	private boolean possuiAulaQuinta =  true;
	private boolean possuiAulaSexta =  true;
	private boolean possuiAulaSabado =  true;
	
	/**
	 * campos usados para listagem das disciplinas no mapa de suspensão de matricula;
	 */
	
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private Date dataInicioModulo;
	private Date dataFimModulo;
	private Integer numeroModulo;
	
	
	public static final long serialVersionUID = 1L;

	public List<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>> getCalendarioHorarioAulaVOs() {
		if (calendarioHorarioAulaVOs == null) {
			calendarioHorarioAulaVOs = new ArrayList<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>>(0);
		}
		return calendarioHorarioAulaVOs;
	}

	public void setCalendarioHorarioAulaVOs(List<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>> calendarioHorarioAulaVOs) {
		this.calendarioHorarioAulaVOs = calendarioHorarioAulaVOs;
	}

	public void adicinarHorarioAlunoPorDiaPorDia(HorarioAlunoDiaVO obj) {
		for (HorarioAlunoDiaVO objExistente : getHorarioAlunoDiaVOs()) {
			if (Uteis.getDateHoraFinalDia(objExistente.getData()).compareTo(Uteis.getDateHoraFinalDia(obj.getData())) == 0) {
				return;
			}
		}
		getHorarioAlunoDiaVOs().add(obj);
		Ordenacao.ordenarLista(getHorarioAlunoDiaVOs(), "data");

	}

	public HorarioAlunoTurnoVO() {
	}

	public Integer getElement() {
		if (getHorarioAlunoDiaVOs().size() < 6) {
			return getHorarioAlunoDiaVOs().size();
		}
		return 6;
	}

	public Integer getColumns() {
		if (getHorarioAlunoDiaVOs().size() < 2) {
			return getHorarioAlunoDiaVOs().size();
		}
		return 2;
	}

	public Integer getElementResumido() {
		if (getHorarioAlunoDiaVOs().size() < 6) {
			return getHorarioAlunoDiaVOs().size();
		}
		return 6;
	}

	public Integer getColumnsResumido() {
		if (getHorarioAlunoDiaVOs().size() < 3) {
			return getHorarioAlunoDiaVOs().size();
		}
		return 3;
	}

	public HorarioAlunoDiaVO consultarHorarioAlunoDia(Date data) {
		for (HorarioAlunoDiaVO objExistente : getHorarioAlunoDiaVOs()) {
			if (data.compareTo(objExistente.getData()) == 0) {
				return objExistente;
			}
		}
		return new HorarioAlunoDiaVO();
	}

	public PessoaVO getProfessor(Integer codigo) {
		PessoaVO obj = consultarProfessorHorarioSemanal(codigo);
		if (obj == null) {
			return consultarProfessorHorarioDiario(codigo);
		}
		return obj;
	}

	private PessoaVO consultarProfessorHorarioDiario(Integer codigo) {
		for (HorarioAlunoDiaVO obj : getHorarioAlunoDiaVOs()) {
			return obj.getProfessor(codigo);
		}
		return null;
	}

	private PessoaVO consultarProfessorHorarioSemanal(Integer codigo) {
		for (DisponibilidadeHorarioAlunoVO horarioAluno : getDisponibilidadeHorarioAlunoVOs()) {
			if (horarioAluno.getProfessorDomingo().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorDomingo().getNome().equals("")) {
				return horarioAluno.getProfessorDomingo();
			}
			if (horarioAluno.getProfessorSegunda().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorSegunda().getNome().equals("")) {
				return horarioAluno.getProfessorSegunda();
			}
			if (horarioAluno.getProfessorTerca().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorTerca().getNome().equals("")) {
				return horarioAluno.getProfessorTerca();
			}
			if (horarioAluno.getProfessorQuarta().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorQuarta().getNome().equals("")) {
				return horarioAluno.getProfessorQuarta();
			}
			if (horarioAluno.getProfessorQuinta().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorQuinta().getNome().equals("")) {
				return horarioAluno.getProfessorQuinta();
			}
			if (horarioAluno.getProfessorSexta().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorSexta().getNome().equals("")) {
				return horarioAluno.getProfessorSexta();
			}
			if (horarioAluno.getProfessorSabado().getCodigo().intValue() == codigo.intValue() && !horarioAluno.getProfessorSabado().getNome().equals("")) {
				return horarioAluno.getProfessorSabado();
			}
		}
		return null;
	}

	public void removerDisciplinaHorarioAluno(Integer disciplina) {
		removerDisciplinaHorarioAlunoDiario(disciplina);
		removerDisciplinaHorarioAlunoSemanal(disciplina);
	}

	public void removerDisciplinaHorarioAlunoDiario(Integer disciplina) {
		for (HorarioAlunoDiaVO obj : getHorarioAlunoDiaVOs()) {
			obj.removerHorarioAlunoDiaItem(disciplina);
		}

	}

	public void removerDisciplinaHorarioAlunoSemanal(Integer disciplina) {
		for (DisponibilidadeHorarioAlunoVO objExistente : getDisponibilidadeHorarioAlunoVOs()) {
			removerDisponibilidadeAlunoDomingo(objExistente, disciplina);
			removerDisponibilidadeAlunoSegunda(objExistente, disciplina);
			removerDisponibilidadeAlunoTerca(objExistente, disciplina);
			removerDisponibilidadeAlunoQuarta(objExistente, disciplina);
			removerDisponibilidadeAlunoQuinta(objExistente, disciplina);
			removerDisponibilidadeAlunoSexta(objExistente, disciplina);
			removerDisponibilidadeAlunoSabado(objExistente, disciplina);
		}
	}

	public void removerDisponibilidadeAlunoDomingo(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaDomingo().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaDomingo(new DisciplinaVO());
			obj.setTurmaDomingo(new TurmaVO());
			obj.setProfessorDomingo(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoSegunda(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaSegunda().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaSegunda(new DisciplinaVO());
			obj.setTurmaSegunda(new TurmaVO());
			obj.setProfessorSegunda(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoTerca(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaTerca().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaTerca(new DisciplinaVO());
			obj.setTurmaTerca(new TurmaVO());
			obj.setProfessorTerca(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoQuarta(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaQuarta().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaQuarta(new DisciplinaVO());
			obj.setTurmaQuarta(new TurmaVO());
			obj.setProfessorQuarta(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoQuinta(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaQuinta().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaQuinta(new DisciplinaVO());
			obj.setTurmaQuinta(new TurmaVO());
			obj.setProfessorQuinta(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoSexta(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaSexta().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaSexta(new DisciplinaVO());
			obj.setTurmaSexta(new TurmaVO());
			obj.setProfessorSexta(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoSabado(DisponibilidadeHorarioAlunoVO obj, Integer disciplina) {
		if (obj.getDisciplinaSabado().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaSabado(new DisciplinaVO());
			obj.setTurmaSabado(new TurmaVO());
			obj.setProfessorSabado(new PessoaVO());
		}
	}

	public List<DisponibilidadeHorarioAlunoVO> getDisponibilidadeHorarioAlunoVOs() {
		if (disponibilidadeHorarioAlunoVOs == null) {
			disponibilidadeHorarioAlunoVOs = new ArrayList<DisponibilidadeHorarioAlunoVO>(0);
		}
		return disponibilidadeHorarioAlunoVOs;
	}

	public void setDisponibilidadeHorarioAlunoVOs(List<DisponibilidadeHorarioAlunoVO> disponibilidadeHorarioAlunoVOs) {
		this.disponibilidadeHorarioAlunoVOs = disponibilidadeHorarioAlunoVOs;
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

	public List<HorarioAlunoDiaVO> getHorarioAlunoDiaVOs() {
		if (horarioAlunoDiaVOs == null) {
			horarioAlunoDiaVOs = new ArrayList<HorarioAlunoDiaVO>(0);
		}
		return horarioAlunoDiaVOs;
	}

	public void setHorarioAlunoDiaVOs(List<HorarioAlunoDiaVO> horarioAlunoDiaVOs) {
		this.horarioAlunoDiaVOs = horarioAlunoDiaVOs;
	}

	public List processarListaParaApresentacaoVisaoAluno(List<HorarioAlunoDiaItemVO> lista) {
		Iterator i = this.getCalendarioHorarioAulaVOs().iterator();
		while (i.hasNext()) {
			CalendarioHorarioAulaVO calendario = (CalendarioHorarioAulaVO) i.next();
			// Domingo
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaDomingo(), lista);
			// Segunda
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaSegunda(), lista);
			// Terca
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaTerca(), lista);
			// Quarta
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaQuarta(), lista);
			// Quinta
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaQuinta(), lista);
			// Sexta
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaSexta(), lista);
			// Sabado
			this.processarListaParaApresentacaoVisaoAlunoDiaSemana(calendario.getCalendarioHorarioAulaSabado(), lista);
		}
		return lista;
	}

	public void processarListaParaApresentacaoVisaoAlunoDiaSemana(List calendarioDiaSemana, List lista) {
		Iterator h = calendarioDiaSemana.iterator();
		while (h.hasNext()) {
			HorarioAlunoDiaVO horarioAula = (HorarioAlunoDiaVO) h.next();
			// Replica data Objeto HorarioAlunoDiaVO, para permitir ordenação na
			// visão do aluno.
			horarioAula.replicarDataHorarioAlunoDia();
			Iterator j = horarioAula.getHorarioAlunoDiaItemVOs().iterator();
			while (j.hasNext()) {
				HorarioAlunoDiaItemVO horarioAlunoDiaItemVO = (HorarioAlunoDiaItemVO) j.next();
				lista.add(horarioAlunoDiaItemVO);
			}
		}
	}
	
	public HorarioAlunoTurnoNumeroAulaVO getObterHorarioAlunoTurnoNumeroAulaVO(Integer numeroAula, DiaSemana diaSemana){
		for(HorarioAlunoTurnoNumeroAulaVO horarioAlunoTurnoNumeroAulaVO : getHorarioAlunoTurnoNumeroAulaVOs()){
			if(numeroAula == null){
				if(diaSemana.equals(DiaSemana.DOMINGO) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.SEGUNGA) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.TERCA) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaTercaApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.QUARTA) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.QUINTA) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.SEXTA) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaSextaApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.SABADO) && horarioAlunoTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().isEmpty()){
					return horarioAlunoTurnoNumeroAulaVO;
				}
			}else if(horarioAlunoTurnoNumeroAulaVO.getNumeroAula().equals(numeroAula)){
				return horarioAlunoTurnoNumeroAulaVO;
			}
		}

		return new HorarioAlunoTurnoNumeroAulaVO();
	}
	

	public List<HorarioAlunoTurnoNumeroAulaVO> getHorarioAlunoTurnoNumeroAulaVOs() {
		if (horarioAlunoTurnoNumeroAulaVOs == null) {
			horarioAlunoTurnoNumeroAulaVOs = new ArrayList<HorarioAlunoTurnoNumeroAulaVO>();
		}
		return horarioAlunoTurnoNumeroAulaVOs;
	}

	public void setHorarioAlunoTurnoNumeroAulaVOs(List<HorarioAlunoTurnoNumeroAulaVO> horarioAlunoTurnoNumeroAulaVOs) {
		this.horarioAlunoTurnoNumeroAulaVOs = horarioAlunoTurnoNumeroAulaVOs;
	}

	public Boolean getApresentarDomingo(){
		return !getTurno().getTurnoHorarioDomingo().isEmpty();
	}
	public Boolean getApresentarSegunda(){
		return !getTurno().getTurnoHorarioSegunda().isEmpty();
	}
	public Boolean getApresentarTerca(){
		return !getTurno().getTurnoHorarioTerca().isEmpty();
	}
	public Boolean getApresentarQuarta(){
		return !getTurno().getTurnoHorarioQuarta().isEmpty();
	}
	public Boolean getApresentarQuinta(){
		return !getTurno().getTurnoHorarioQuinta().isEmpty();
	}
	public Boolean getApresentarSexta(){
		return !getTurno().getTurnoHorarioSexta().isEmpty();
	}
	public Boolean getApresentarSabado(){
		return !getTurno().getTurnoHorarioSabado().isEmpty();
	}

	public Date getDataSegunda() {
		
		return dataSegunda;
	}

	public void setDataSegunda(Date dataSegunda) {
		this.dataSegunda = dataSegunda;
	}

	public Date getDataTerca() {
		
		return dataTerca;
	}

	public void setDataTerca(Date dataTerca) {
		this.dataTerca = dataTerca;
	}

	public Date getDataQuarta() {
		
		return dataQuarta;
	}

	public void setDataQuarta(Date dataQuarta) {
		this.dataQuarta = dataQuarta;
	}

	public Date getDataQuinta() {
		
		return dataQuinta;
	}

	public void setDataQuinta(Date dataQuinta) {
		this.dataQuinta = dataQuinta;
	}

	public Date getDataSexta() {
		
		return dataSexta;
	}

	public void setDataSexta(Date dataSexta) {
		this.dataSexta = dataSexta;
	}

	public Date getDataSabado() {
		
		return dataSabado;
	}

	public void setDataSabado(Date dataSabado) {
		this.dataSabado = dataSabado;
	}

	public Date getDataDomingo() {
		
		return dataDomingo;
	}

	public void setDataDomingo(Date dataDomingo) {
		this.dataDomingo = dataDomingo;
	}

	public Date getDataInicio() {
		
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public FeriadoVO getFeriadoSegunda() {
		if (feriadoSegunda == null) {
			feriadoSegunda = new FeriadoVO();
		}
		return feriadoSegunda;
	}

	public void setFeriadoSegunda(FeriadoVO feriadoSegunda) {
		this.feriadoSegunda = feriadoSegunda;
	}

	public FeriadoVO getFeriadoTerca() {
		if (feriadoTerca == null) {
			feriadoTerca = new FeriadoVO();
		}
		return feriadoTerca;
	}

	public void setFeriadoTerca(FeriadoVO feriadoTerca) {
		this.feriadoTerca = feriadoTerca;
	}

	public FeriadoVO getFeriadoQuarta() {
		if (feriadoQuarta == null) {
			feriadoQuarta = new FeriadoVO();
		}
		return feriadoQuarta;
	}

	public void setFeriadoQuarta(FeriadoVO feriadoQuarta) {
		this.feriadoQuarta = feriadoQuarta;
	}

	public FeriadoVO getFeriadoQuinta() {
		if (feriadoQuinta == null) {
			feriadoQuinta = new FeriadoVO();
		}
		return feriadoQuinta;
	}

	public void setFeriadoQuinta(FeriadoVO feriadoQuinta) {
		this.feriadoQuinta = feriadoQuinta;
	}

	public FeriadoVO getFeriadoSexta() {
		if (feriadoSexta == null) {
			feriadoSexta = new FeriadoVO();
		}
		return feriadoSexta;
	}

	public void setFeriadoSexta(FeriadoVO feriadoSexta) {
		this.feriadoSexta = feriadoSexta;
	}

	public FeriadoVO getFeriadoSabado() {
		if (feriadoSabado == null) {
			feriadoSabado = new FeriadoVO();
		}
		return feriadoSabado;
	}

	public void setFeriadoSabado(FeriadoVO feriadoSabado) {
		this.feriadoSabado = feriadoSabado;
	}

	public FeriadoVO getFeriadoDomingo() {
		if (feriadoDomingo == null) {
			feriadoDomingo = new FeriadoVO();
		}
		return feriadoDomingo;
	}

	public void setFeriadoDomingo(FeriadoVO feriadoDomingo) {
		this.feriadoDomingo = feriadoDomingo;
	}

	public Boolean getNaoApresentarProfessorVisaoAluno() {
		if (naoApresentarProfessorVisaoAluno == null) {
			naoApresentarProfessorVisaoAluno = false;
		}
		return naoApresentarProfessorVisaoAluno;
	}

	public void setNaoApresentarProfessorVisaoAluno(Boolean naoApresentarProfessorVisaoAluno) {
		this.naoApresentarProfessorVisaoAluno = naoApresentarProfessorVisaoAluno;
	}
	
	public Integer getIndex() {
		if (index == null) {
			index = 0;
		}
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getTituloScroll() {
		
		if (getIndex() - 1 < 0) {
			setIndex(1);
		}		
		if (getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getCalendarioHorarioAulaVOs().get(getIndex() - 1).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScrollNext() {
		
		if (getIndex() == getCalendarioHorarioAulaVOs().size()) {
			return "";
		}
		if (getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getCalendarioHorarioAulaVOs().get(getIndex()).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScrollFirst() {
		
		if (getIndex() - 1 < 1) {
			return "";
		}
		if (getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getCalendarioHorarioAulaVOs().get(getIndex() - 2).getTituloCalendarioAbreviado();
		}
		return "";
	}
	
private String mesAnoApresentar;
	
	

	public void setMesAnoApresentar(String mesAnoApresentar) {
		this.mesAnoApresentar = mesAnoApresentar;
	}

	public String getMesAnoApresentar() {
		if (mesAnoApresentar == null) {
			int mesInicio = Uteis.getMesData(getDataInicio());
			int mesTermino = Uteis.getMesData(getDataTermino());
			if (mesInicio != mesTermino) {
				Date ultimaDataMes = Uteis.getDataUltimoDiaMes(getDataInicio());
				Date primeiraDataMes = Uteis.getDataPrimeiroDiaMes(getDataTermino());
				long nrDias1 = UteisData.nrDiasEntreDatas(ultimaDataMes, getDataInicio());
				long nrDias2 = UteisData.nrDiasEntreDatas(getDataTermino(), primeiraDataMes);
				if (UteisData.nrDiasEntreDatas(primeiraDataMes, ultimaDataMes) > 27) {
					return Uteis.getMesReferenciaData(Uteis.obterDataFutura(ultimaDataMes, 10));
				}
				if (nrDias1 > nrDias2) {
					mesAnoApresentar = Uteis.getMesReferenciaData(ultimaDataMes);
				} else {
					mesAnoApresentar = Uteis.getMesReferenciaData(primeiraDataMes);
				}

			}else{
				mesAnoApresentar = Uteis.getMesReferenciaData(getDataInicio());
			}
		}
		return mesAnoApresentar;
	}
	
	public Integer getQtdeDiaSemana(){
		return (getTurno().getTurnoHorarioDomingo().isEmpty()?0:1)+
				(getTurno().getTurnoHorarioSegunda().isEmpty()?0:1)+
				(getTurno().getTurnoHorarioTerca().isEmpty()?0:1)+
				(getTurno().getTurnoHorarioQuarta().isEmpty()?0:1)+
				(getTurno().getTurnoHorarioQuinta().isEmpty()?0:1)+
				(getTurno().getTurnoHorarioSexta().isEmpty()?0:1)+
				(getTurno().getTurnoHorarioSabado().isEmpty()?0:1);
	}
	
	/*
	 * Transiente apenas para retornar uma lista de horario do aluno no turno em uma determinada semana;
	 */
	private List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs;
	public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
		if((horarioAlunoTurnoVOs == null || horarioAlunoTurnoVOs.isEmpty()) && !getHorarioAlunoTurnoSemanaVOs().values().isEmpty()){
			horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);
			horarioAlunoTurnoVOs.addAll(getHorarioAlunoTurnoSemanaVOs().values());
		}
		return horarioAlunoTurnoVOs;
	}

	public Map<String, HorarioAlunoTurnoVO> getHorarioAlunoTurnoSemanaVOs() {
		if (horarioAlunoTurnoSemanaVOs == null) {
			horarioAlunoTurnoSemanaVOs = new HashMap<String, HorarioAlunoTurnoVO>(0);
		}
		return horarioAlunoTurnoSemanaVOs;
	}

	public void setHorarioAlunoTurnoVOs(Map<String, HorarioAlunoTurnoVO> horarioAlunoTurnoSemanaVOs) {
		this.horarioAlunoTurnoSemanaVOs = horarioAlunoTurnoSemanaVOs;
	}

	public boolean isPossuiAulaDomingo() {
		return possuiAulaDomingo;
	}

	public void setPossuiAulaDomingo(boolean possuiAulaDomingo) {
		this.possuiAulaDomingo = possuiAulaDomingo;
	}

	public boolean isPossuiAulaSegunda() {
		return possuiAulaSegunda;
	}

	public void setPossuiAulaSegunda(boolean possuiAulaSegunda) {
		this.possuiAulaSegunda = possuiAulaSegunda;
	}

	public boolean isPossuiAulaTerca() {
		return possuiAulaTerca;
	}

	public void setPossuiAulaTerca(boolean possuiAulaTerca) {
		this.possuiAulaTerca = possuiAulaTerca;
	}

	public boolean isPossuiAulaQuarta() {
		return possuiAulaQuarta;
	}

	public void setPossuiAulaQuarta(boolean possuiAulaQuarta) {
		this.possuiAulaQuarta = possuiAulaQuarta;
	}

	public boolean isPossuiAulaQuinta() {
		return possuiAulaQuinta;
	}

	public void setPossuiAulaQuinta(boolean possuiAulaQuinta) {
		this.possuiAulaQuinta = possuiAulaQuinta;
	}

	public boolean isPossuiAulaSexta() {
		return possuiAulaSexta;
	}

	public void setPossuiAulaSexta(boolean possuiAulaSexta) {
		this.possuiAulaSexta = possuiAulaSexta;
	}

	public boolean isPossuiAulaSabado() {
		return possuiAulaSabado;
	}

	public void setPossuiAulaSabado(boolean possuiAulaSabado) {
		this.possuiAulaSabado = possuiAulaSabado;
	}
	

	public HorarioAlunoDiaItemVO obterHorarioAlunoDiaItemPorDataNrAula(Date data, int nrAula) {
		for(HorarioAlunoDiaItemVO horarioAlunoDiaItemVO: getHorarioAlunoDiaItemVOs()) {
			if(horarioAlunoDiaItemVO.getData().equals(data) && horarioAlunoDiaItemVO.getNrAula().equals(nrAula)) {
				return horarioAlunoDiaItemVO;
			}
		}
		return new HorarioAlunoDiaItemVO();
	}

	public List<HorarioAlunoDiaItemVO> getHorarioAlunoDiaItemVOs() {
		if (horarioAlunoDiaItemVOs == null) {
			horarioAlunoDiaItemVOs = new ArrayList<HorarioAlunoDiaItemVO>(0);
		}
		return horarioAlunoDiaItemVOs;
	}

	public void setHorarioAlunoDiaItemVOs(List<HorarioAlunoDiaItemVO> horarioAlunoDiaItemVOs) {
		this.horarioAlunoDiaItemVOs = horarioAlunoDiaItemVOs;
	}
	
	private Boolean horarioIntegral;

	public Boolean getHorarioIntegral() {
		if (horarioIntegral == null) {
			horarioIntegral = false;
		}
		return horarioIntegral;
	}

	public void setHorarioIntegral(Boolean horarioIntegral) {
		this.horarioIntegral = horarioIntegral;
	}

	public TurmaVO getTurmaVO() {
		if(turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public Date getDataInicioModulo() {
		return dataInicioModulo;
	}

	public void setDataInicioModulo(Date dataInicioModulo) {
		this.dataInicioModulo = dataInicioModulo;
	}

	public Date getDataFimModulo() {
		return dataFimModulo;
	}

	public void setDataFimModulo(Date dataFimModulo) {
		this.dataFimModulo = dataFimModulo;
	}

	public Integer getNumeroModulo() {
		return numeroModulo;
	}

	public void setNumeroModulo(Integer numeroModulo) {
		this.numeroModulo = numeroModulo;
	}
	
	

}
