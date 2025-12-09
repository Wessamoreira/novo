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

public class HorarioProfessorTurnoVO implements Serializable {

	private TurnoVO turno;
	private List<DisponibilidadeHorarioVO> disponibilidadeHorarioVOs;
	private List<HorarioProfessorDiaVO> horarioProfessorDiaVOs;
	private List<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>> calendarioHorarioAulaVOs;
	private List<HorarioProfessorTurnoNumeroAulaVO> horarioProfessorTurnoNumeroAulaVOs;
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
	private Map<String, HorarioProfessorTurnoVO> horarioProfessorTurnoSemanaVOs;
	
	private boolean possuiAulaDomingo =  true;
	private boolean possuiAulaSegunda =  true;
	private boolean possuiAulaTerca =  true;
	private boolean possuiAulaQuarta =  true;
	private boolean possuiAulaQuinta =  true;
	private boolean possuiAulaSexta =  true;
	private boolean possuiAulaSabado =  true;
	
	
	public static final long serialVersionUID = 1L;

	public List<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>> getCalendarioHorarioAulaVOs() {
		if (calendarioHorarioAulaVOs == null) {
			calendarioHorarioAulaVOs = new ArrayList<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>>(0);
		}
		return calendarioHorarioAulaVOs;
	}

	public void setCalendarioHorarioAulaVOs(List<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>> calendarioHorarioAulaVOs) {
		this.calendarioHorarioAulaVOs = calendarioHorarioAulaVOs;
	}

	public void adicinarHorarioProfessorPorDiaPorDia(HorarioProfessorDiaVO obj) {
		for (HorarioProfessorDiaVO objExistente : getHorarioProfessorDiaVOs()) {
			if (Uteis.getDateHoraFinalDia(objExistente.getData()).compareTo(Uteis.getDateHoraFinalDia(obj.getData())) == 0) {
				return;
			}
		}
		getHorarioProfessorDiaVOs().add(obj);
		Ordenacao.ordenarLista(getHorarioProfessorDiaVOs(), "data");

	}

	public HorarioProfessorTurnoVO() {
	}

	public Integer getElement() {
		if (getHorarioProfessorDiaVOs().size() < 6) {
			return getHorarioProfessorDiaVOs().size();
		}
		return 6;
	}

	public Integer getColumns() {
		if (getHorarioProfessorDiaVOs().size() < 2) {
			return getHorarioProfessorDiaVOs().size();
		}
		return 2;
	}

	public Integer getElementResumido() {
		if (getHorarioProfessorDiaVOs().size() < 6) {
			return getHorarioProfessorDiaVOs().size();
		}
		return 6;
	}

	public Integer getColumnsResumido() {
		if (getHorarioProfessorDiaVOs().size() < 3) {
			return getHorarioProfessorDiaVOs().size();
		}
		return 3;
	}

	public HorarioProfessorDiaVO consultarHorarioProfessorDia(Date data) {
		for (HorarioProfessorDiaVO objExistente : getHorarioProfessorDiaVOs()) {
			if (data.compareTo(objExistente.getData()) == 0) {
				return objExistente;
			}
		}
		return new HorarioProfessorDiaVO();
	}

	public PessoaVO getProfessor(Integer codigo) {
		PessoaVO obj = consultarProfessorHorarioSemanal(codigo);
		if (obj == null) {
			return consultarProfessorHorarioDiario(codigo);
		}
		return obj;
	}

	private PessoaVO consultarProfessorHorarioDiario(Integer codigo) {
		for (HorarioProfessorDiaVO obj : getHorarioProfessorDiaVOs()) {
			return obj.getProfessor(codigo);
		}
		return null;
	}

	private PessoaVO consultarProfessorHorarioSemanal(Integer codigo) {
		for (DisponibilidadeHorarioVO horarioProfessor : getDisponibilidadeHorarioVOs()) {
			if (horarioProfessor.getProfessorDomingo().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorDomingo().getNome().equals("")) {
				return horarioProfessor.getProfessorDomingo();
			}
			if (horarioProfessor.getProfessorSegunda().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorSegunda().getNome().equals("")) {
				return horarioProfessor.getProfessorSegunda();
			}
			if (horarioProfessor.getProfessorTerca().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorTerca().getNome().equals("")) {
				return horarioProfessor.getProfessorTerca();
			}
			if (horarioProfessor.getProfessorQuarta().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorQuarta().getNome().equals("")) {
				return horarioProfessor.getProfessorQuarta();
			}
			if (horarioProfessor.getProfessorQuinta().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorQuinta().getNome().equals("")) {
				return horarioProfessor.getProfessorQuinta();
			}
			if (horarioProfessor.getProfessorSexta().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorSexta().getNome().equals("")) {
				return horarioProfessor.getProfessorSexta();
			}
			if (horarioProfessor.getProfessorSabado().getCodigo().intValue() == codigo.intValue() && !horarioProfessor.getProfessorSabado().getNome().equals("")) {
				return horarioProfessor.getProfessorSabado();
			}
		}
		return null;
	}

	public void removerDisciplinaHorarioAluno(Integer disciplina) {
		removerDisciplinaHorarioProfessorDiario(disciplina);
		removerDisciplinaHorarioAlunoSemanal(disciplina);
	}

	public void removerDisciplinaHorarioProfessorDiario(Integer disciplina) {
		for (HorarioProfessorDiaVO obj : getHorarioProfessorDiaVOs()) {
			obj.removerHorarioProfessorDiaItem(disciplina);
		}

	}

	public void removerDisciplinaHorarioAlunoSemanal(Integer disciplina) {
		for (DisponibilidadeHorarioVO objExistente : getDisponibilidadeHorarioVOs()) {
			removerDisponibilidadeAlunoDomingo(objExistente, disciplina);
			removerDisponibilidadeAlunoSegunda(objExistente, disciplina);
			removerDisponibilidadeAlunoTerca(objExistente, disciplina);
			removerDisponibilidadeAlunoQuarta(objExistente, disciplina);
			removerDisponibilidadeAlunoQuinta(objExistente, disciplina);
			removerDisponibilidadeAlunoSexta(objExistente, disciplina);
			removerDisponibilidadeAlunoSabado(objExistente, disciplina);
		}
	}

	public void removerDisponibilidadeAlunoDomingo(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaDomingo().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaDomingo(new DisciplinaVO());
			obj.setTurmaDomingo(new TurmaVO());
			obj.setProfessorDomingo(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoSegunda(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaSegunda().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaSegunda(new DisciplinaVO());
			obj.setTurmaSegunda(new TurmaVO());
			obj.setProfessorSegunda(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoTerca(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaTerca().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaTerca(new DisciplinaVO());
			obj.setTurmaTerca(new TurmaVO());
			obj.setProfessorTerca(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoQuarta(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaQuarta().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaQuarta(new DisciplinaVO());
			obj.setTurmaQuarta(new TurmaVO());
			obj.setProfessorQuarta(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoQuinta(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaQuinta().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaQuinta(new DisciplinaVO());
			obj.setTurmaQuinta(new TurmaVO());
			obj.setProfessorQuinta(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoSexta(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaSexta().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaSexta(new DisciplinaVO());
			obj.setTurmaSexta(new TurmaVO());
			obj.setProfessorSexta(new PessoaVO());
		}
	}

	public void removerDisponibilidadeAlunoSabado(DisponibilidadeHorarioVO obj, Integer disciplina) {
		if (obj.getDisciplinaSabado().getCodigo().intValue() == disciplina) {
			obj.setDisciplinaSabado(new DisciplinaVO());
			obj.setTurmaSabado(new TurmaVO());
			obj.setProfessorSabado(new PessoaVO());
		}
	}

	public List<DisponibilidadeHorarioVO> getDisponibilidadeHorarioVOs() {
		if (disponibilidadeHorarioVOs == null) {
			disponibilidadeHorarioVOs = new ArrayList<DisponibilidadeHorarioVO>(0);
		}
		return disponibilidadeHorarioVOs;
	}

	public void setDisponibilidadeHorarioVOs(List<DisponibilidadeHorarioVO> disponibilidadeHorarioVOs) {
		this.disponibilidadeHorarioVOs = disponibilidadeHorarioVOs;
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

	public List<HorarioProfessorDiaVO> getHorarioProfessorDiaVOs() {
		if (horarioProfessorDiaVOs == null) {
			horarioProfessorDiaVOs = new ArrayList<HorarioProfessorDiaVO>(0);
		}
		return horarioProfessorDiaVOs;
	}

	public void setHorarioProfessorDiaVOs(List<HorarioProfessorDiaVO> horarioProfessorDiaVOs) {
		this.horarioProfessorDiaVOs = horarioProfessorDiaVOs;
	}

	public List processarListaParaApresentacaoVisaoAluno(List<HorarioProfessorDiaItemVO> lista) {
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
			HorarioProfessorDiaVO horarioAula = (HorarioProfessorDiaVO) h.next();
			// Replica data Objeto HorarioProfessorDiaVO, para permitir ordenação na
			// visão do aluno.
			horarioAula.replicarDataHorarioProfessorDia();
			Iterator j = horarioAula.getHorarioProfessorDiaItemVOs().iterator();
			while (j.hasNext()) {
				HorarioProfessorDiaItemVO HorarioProfessorDiaItemVO = (HorarioProfessorDiaItemVO) j.next();
				lista.add(HorarioProfessorDiaItemVO);
			}
		}
	}
	
	public HorarioProfessorTurnoNumeroAulaVO getObterHorarioProfessorTurnoNumeroAulaVO(Integer numeroAula, DiaSemana diaSemana){
		for(HorarioProfessorTurnoNumeroAulaVO horarioProfessorTurnoNumeroAulaVO : getHorarioProfessorTurnoNumeroAulaVOs()){
			if(numeroAula == null){
				if(diaSemana.equals(DiaSemana.DOMINGO) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.SEGUNGA) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.TERCA) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaTercaApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.QUARTA) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.QUINTA) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.SEXTA) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaSextaApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
				if(diaSemana.equals(DiaSemana.SABADO) && horarioProfessorTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().isEmpty()){
					return horarioProfessorTurnoNumeroAulaVO;
				}
			}else if(horarioProfessorTurnoNumeroAulaVO.getNumeroAula().equals(numeroAula)){
				return horarioProfessorTurnoNumeroAulaVO;
			}
		}

		return new HorarioProfessorTurnoNumeroAulaVO();
	}
	

	public List<HorarioProfessorTurnoNumeroAulaVO> getHorarioProfessorTurnoNumeroAulaVOs() {
		if (horarioProfessorTurnoNumeroAulaVOs == null) {
			horarioProfessorTurnoNumeroAulaVOs = new ArrayList<HorarioProfessorTurnoNumeroAulaVO>();
		}
		return horarioProfessorTurnoNumeroAulaVOs;
	}

	public void setHorarioProfessorTurnoNumeroAulaVOs(List<HorarioProfessorTurnoNumeroAulaVO> horarioProfessorTurnoNumeroAulaVOs) {
		this.horarioProfessorTurnoNumeroAulaVOs = horarioProfessorTurnoNumeroAulaVOs;
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
	private List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs;
	public List<HorarioProfessorTurnoVO> getHorarioProfessorTurnoVOs() {
		if((horarioProfessorTurnoVOs == null || horarioProfessorTurnoVOs.isEmpty()) && !getHorarioProfessorTurnoSemanaVOs().values().isEmpty()){
			horarioProfessorTurnoVOs = new ArrayList<HorarioProfessorTurnoVO>(0);
			horarioProfessorTurnoVOs.addAll(getHorarioProfessorTurnoSemanaVOs().values());
		}
		return horarioProfessorTurnoVOs;
	}

	public Map<String, HorarioProfessorTurnoVO> getHorarioProfessorTurnoSemanaVOs() {
		if (horarioProfessorTurnoSemanaVOs == null) {
			horarioProfessorTurnoSemanaVOs = new HashMap<String, HorarioProfessorTurnoVO>(0);
		}
		return horarioProfessorTurnoSemanaVOs;
	}

	public void setHorarioProfessorTurnoVOs(Map<String, HorarioProfessorTurnoVO> horarioProfessorTurnoSemanaVOs) {
		this.horarioProfessorTurnoSemanaVOs = horarioProfessorTurnoSemanaVOs;
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

}
