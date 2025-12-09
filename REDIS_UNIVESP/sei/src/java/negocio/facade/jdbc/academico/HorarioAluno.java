/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.ChoqueHorarioAlunoDetalheVO;
import negocio.comuns.academico.ChoqueHorarioAlunoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisponibilidadeHorarioAlunoVO;
import negocio.comuns.academico.HorarioAlunoDiaItemVO;
import negocio.comuns.academico.HorarioAlunoDiaVO;
import negocio.comuns.academico.HorarioAlunoTurnoNumeroAulaVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.HorarioAlunoVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaProfessorDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioAlunoInterfaceFacade;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;
import webservice.servicos.objetos.enumeradores.OrigemAgendaAlunoEnum;

/**
 *
 * @author Otimize-Not
 */
@Repository
public class HorarioAluno extends ControleAcesso implements HorarioAlunoInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public boolean montarDadosHorarioAlunoDiario(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioTurmaVO horarioTurmaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception {
		boolean alterouHorario = false;
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
			try {
				List<HorarioAlunoDiaVO> horarioAlunoDiaVOs = horarioTurmaVO.consultarHorarioAlunoDiaADiaPorDisciplinaTurma(matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), matriculaPeriodoTurmaDisciplinaVO.getTurma());
				// List<HorarioAlunoDiaVO> horarioAlunoDiaVOs =
				// horarioTurmaVO.consultarHorarioAlunoDiaADiaPorDisciplina(matriculaPeriodoTurmaDisciplinaVO.getDisciplina());
				adicionarHorarioAlunoDia(horarioAlunoTurnoVO, horarioAlunoDiaVOs);
				if (horarioAlunoDiaVOs.size() > 0) {
					alterouHorario = true;
				}
				horarioAlunoDiaVOs = null;
			} catch (Exception e) {
				horarioAlunoTurnoVO.removerDisciplinaHorarioAluno(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
				throw e;
			}
		}
		return alterouHorario;
	}

	// public boolean montarDadosHorarioAlunoSemanal(HorarioAlunoTurnoVO
	// horarioAlunoTurnoVO, HorarioTurmaVO horarioTurmaVO,
	// List<MatriculaPeriodoTurmaDisciplinaVO>
	// matriculaPeriodoTurmaDisciplinaVOs) throws Exception {
	// boolean alterouHorario = false;
	// for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO
	// : matriculaPeriodoTurmaDisciplinaVOs) {
	// List<HorarioDisciplinaVO> horarioDisciplinaVOs =
	// consultarDisciplinaSemana(horarioTurmaVO,
	// matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
	// adicionarHorarioAlunoSemanal(horarioAlunoTurnoVO, horarioDisciplinaVOs,
	// matriculaPeriodoTurmaDisciplinaVO.getDisciplina(),
	// horarioTurmaVO.getTurma());
	// if (horarioDisciplinaVOs.size() > 0) {
	// alterouHorario = true;
	// }
	// horarioDisciplinaVOs = null;
	// }
	// return alterouHorario;
	// }
	// public List<HorarioDisciplinaVO> consultarDisciplinaSemana(HorarioTurmaVO
	// horarioTurmaVO, Integer codigoDisciplina) throws Exception {
	// Integer nrAula = 1;
	// List<HorarioDisciplinaVO> horarioDisciplinaVOs = new
	// ArrayList<HorarioDisciplinaVO>(0);
	// int nrAulas = 0;
	// if
	// (horarioTurmaVO.getTurno().getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO))
	// {
	// nrAulas = horarioTurmaVO.getTurno().getNrAulas();
	// } else {
	// }
	// for (DiaSemana diaSemana : DiaSemana.values()) {
	// if(diaSemana.equals(DiaSemana.NENHUM)){
	// continue;
	// }
	// if
	// (!horarioTurmaVO.getTurno().getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO))
	// {
	// nrAulas =
	// getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioTurmaVO.getTurno(),
	// diaSemana);
	// }
	// nrAula = 1;
	// while (nrAula <= nrAulas) {
	// horarioDisciplinaVOs =
	// montarDadosHorarioDisciplina(horarioTurmaVO.getCodigoDisciplina(nrAula,
	// diaSemana, null, TipoHorarioTurma.SEMANAL),
	// codigoDisciplina, nrAula, diaSemana, horarioDisciplinaVOs);
	// nrAula++;
	// }
	// }
	//
	// return horarioDisciplinaVOs;
	// }
	public boolean montarHorarioAluno(HorarioAlunoVO horarioAlunoVO, List<HorarioTurmaVO> horarioTurmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception {
		boolean alterouHorario = false;
		for (HorarioTurmaVO horarioTurmaVO : horarioTurmaVOs) {
			HorarioAlunoTurnoVO horarioAlunoTurnoVO = horarioAlunoVO.consultarHorarioAlunoTurno(horarioTurmaVO.getTurno().getCodigo());
			getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, matriculaPeriodoTurmaDisciplinaVOs);
			if (horarioAlunoTurnoVO.getTurno().getCodigo().intValue() == 0) {
				horarioAlunoTurnoVO.setTurno(horarioTurmaVO.getTurno());
			}
			if (montarDadosHorarioAlunoDiario(horarioAlunoTurnoVO, horarioTurmaVO, matriculaPeriodoTurmaDisciplinaVOs) && !alterouHorario) {
				alterouHorario = true;
			}
			horarioAlunoVO.adicionarHorarioAlunoTurno(horarioAlunoTurnoVO);
		}
		for (HorarioAlunoTurnoVO horarioAlunoTurnoVO : horarioAlunoVO.getHorarioAlunoTurnoVOs()) {
			inicializarDadosCalendario(horarioAlunoTurnoVO, usuario);
		}
		return alterouHorario;
	}

	public void adicionarDisponibilidadeHorarioAlunoVOs(HorarioAlunoTurnoVO horarioAlunoTurnoVO, Integer nrAula, String horario, DiaSemana diaSemana, DisciplinaVO disciplina, TurmaVO turma, Integer professor) throws Exception {
		int index = 0;
		for (DisponibilidadeHorarioAlunoVO objExistente : horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs()) {
			if (objExistente.getNrAula().intValue() == nrAula.intValue()) {
				if (objExistente.verificaDisponibilidadeAluno(diaSemana.getValor(), disciplina, turma, professor)) {
					horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs().set(index, objExistente);
					return;
				}
				horarioAlunoTurnoVO.removerDisciplinaHorarioAluno(disciplina.getCodigo());
				throw new Exception("O horário do Aluno esta indisponível para os horarios da aula da turma/disciplina a ser adicionada.");
			}
			index++;
		}
		DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = new DisponibilidadeHorarioAlunoVO();
		disponibilidadeHorarioAlunoVO.setHorario(horario);
		disponibilidadeHorarioAlunoVO.setDiaSemana(diaSemana.getValor());
		disponibilidadeHorarioAlunoVO.setNrAula(nrAula);
		if (disponibilidadeHorarioAlunoVO.verificaDisponibilidadeAluno(diaSemana.getValor(), disciplina, turma, professor)) {
			horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs().add(disponibilidadeHorarioAlunoVO);
		}
	}

	public void adicionarHorarioAlunoDia(HorarioAlunoTurnoVO horarioAlunoTurnoVO, List<HorarioAlunoDiaVO> horarioAlunoDiaVOs) throws Exception {
		for (HorarioAlunoDiaVO objs : horarioAlunoDiaVOs) {
			adicionarHorarioAlunoDia(horarioAlunoTurnoVO, objs);
		}
	}

	public void adicionarHorarioAlunoDia(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioAlunoDiaVO obj) throws Exception {

		for (HorarioAlunoDiaVO objExistente : horarioAlunoTurnoVO.getHorarioAlunoDiaVOs()) {
			if (obj.getData().compareTo(objExistente.getData()) == 0) {
				adicionarHorarioAlunoDiaItem(obj, objExistente);
				return;
			}
		}
		horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().add(obj);
	}

	public void adicionarHorarioAlunoDiaItem(HorarioAlunoDiaVO obj, HorarioAlunoDiaVO objExistente) throws Exception {
		for (HorarioAlunoDiaItemVO objItem : obj.getHorarioAlunoDiaItemVOs()) {
			objExistente.adicionarHorarioAlunoDiaItem(objItem);
		}
	}

	public void inicializarDadosCalendario(HorarioAlunoTurnoVO horarioAlunoTurnoVO, UsuarioVO usuario) throws Exception {
		horarioAlunoTurnoVO.setCalendarioHorarioAulaVOs(new ArrayList<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>>());
		if (!horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().isEmpty()) {
			Ordenacao.ordenarLista(horarioAlunoTurnoVO.getHorarioAlunoDiaVOs(), "data");
			for (HorarioAlunoDiaVO horarioAlunoDiaVO : horarioAlunoTurnoVO.getHorarioAlunoDiaVOs()) {
				adicionarHorarioAlunoDiaEmCalendarioHorarioTurma(horarioAlunoTurnoVO, horarioAlunoDiaVO);
			}
			Date menorData = horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().get(0).getData();
			Date maiorData = horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().get(horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().size() - 1).getData();
			List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(Uteis.getDate("01/" + Uteis.getMesData(menorData) + "/" + Uteis.getAnoData(menorData)), Uteis.getDate(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(maiorData)) + "/" + Uteis.getMesData(maiorData) + "/" + Uteis.getAnoData(maiorData)), 0, ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

			executarCriacaoDiasMesCalendario(horarioAlunoTurnoVO, feriadoVOs);
		}
	}

	public void adicionarHorarioAlunoDiaEmCalendarioHorarioTurma(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioAlunoDiaVO horarioAlunoDiaVO) {
		for (CalendarioHorarioAulaVO calendarioHorarioAulaVO : horarioAlunoTurnoVO.getCalendarioHorarioAulaVOs()) {
			if (Integer.parseInt(calendarioHorarioAulaVO.getAno()) == (Uteis.getAnoData(horarioAlunoDiaVO.getData())) && calendarioHorarioAulaVO.getMesAno().getMesData(horarioAlunoDiaVO.getData()).equals(calendarioHorarioAulaVO.getMesAno())) {
				adicionarDiaComAulaProgramadaCalendario(calendarioHorarioAulaVO, horarioAlunoDiaVO);
				return;
			}
		}
		CalendarioHorarioAulaVO<HorarioAlunoDiaVO> calendarioHorarioAulaVO = new CalendarioHorarioAulaVO<HorarioAlunoDiaVO>();
		calendarioHorarioAulaVO.setAno("" + (Uteis.getAnoData(horarioAlunoDiaVO.getData())));
		calendarioHorarioAulaVO.setMesAno(calendarioHorarioAulaVO.getMesAno().getMesData(horarioAlunoDiaVO.getData()));
		adicionarDiaComAulaProgramadaCalendario(calendarioHorarioAulaVO, horarioAlunoDiaVO);
		horarioAlunoTurnoVO.getCalendarioHorarioAulaVOs().add(calendarioHorarioAulaVO);
	}

	public void adicionarDiaComAulaProgramadaCalendario(CalendarioHorarioAulaVO calendarioHorarioAulaVO, HorarioAlunoDiaVO horarioAlunoDiaVO) {
		if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.DOMINGO)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo().add(horarioAlunoDiaVO);
		} else if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.SEGUNGA)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda().add(horarioAlunoDiaVO);
		} else if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.TERCA)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca().add(horarioAlunoDiaVO);
		} else if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.QUARTA)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta().add(horarioAlunoDiaVO);
		} else if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.QUINTA)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta().add(horarioAlunoDiaVO);
		} else if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.SEXTA)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta().add(horarioAlunoDiaVO);
		} else if (horarioAlunoDiaVO.getDiaSemana().equals(DiaSemana.SABADO)) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado().add(horarioAlunoDiaVO);
		}
	}

	public void executarCriacaoDiasMesCalendario(HorarioAlunoTurnoVO horarioAlunoTurnoVO, List<FeriadoVO> feriadoVOs) throws Exception {
		if (!horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().isEmpty()) {

			String diaString;
			DiaSemana diaSemana;
			for (CalendarioHorarioAulaVO calendarioHorarioAulaVO : horarioAlunoTurnoVO.getCalendarioHorarioAulaVOs()) {
				int dia = 1;
				int ultimoDia = Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(Uteis.getDate("01/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
				while (dia <= ultimoDia) {
					if (dia < 10) {
						diaString = "0" + dia;
					} else {
						diaString = "" + dia;
					}
					diaSemana = Uteis.getDiaSemanaEnum(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
					calendarioHorarioAulaVO = adicionarDiaCalendario(horarioAlunoTurnoVO, calendarioHorarioAulaVO, diaString, diaSemana, executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					dia++;
				}
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado(), "dia");
			}
		}
	}

	public FeriadoVO executarValidacaoDataFeriado(List<FeriadoVO> feriadoVOs, Date data) {
		if (feriadoVOs != null) {
			for (FeriadoVO feriadoVO : feriadoVOs) {
				if (!feriadoVO.getRecorrente() && Uteis.getDateHoraFinalDia(feriadoVO.getData()).equals(Uteis.getDateHoraFinalDia(data))) {
					return feriadoVO;
				} else if (feriadoVO.getRecorrente() && Uteis.getDiaMesData(feriadoVO.getData()) == Uteis.getDiaMesData(data) && Uteis.getMesData(feriadoVO.getData()) == Uteis.getMesData(data)) {
					return feriadoVO;
				}
			}
		}
		return null;
	}

	public CalendarioHorarioAulaVO<HorarioAlunoDiaVO> adicionarDiaCalendario(HorarioAlunoTurnoVO horarioAlunoTurnoVO, CalendarioHorarioAulaVO<HorarioAlunoDiaVO> calendarioHorarioAulaVO, String dia, DiaSemana diaSemana, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01") && !diaSemana.equals(DiaSemana.DOMINGO)) {
			for (DiaSemana diaSemana1 : DiaSemana.values()) {
				if (diaSemana1.equals(DiaSemana.NENHUM)) {
					continue;
				}
				if (Integer.valueOf(diaSemana1.getValor()) < Integer.valueOf(diaSemana.getValor())) {
					calendarioHorarioAulaVO = adicionarDiaCalendario(horarioAlunoTurnoVO, calendarioHorarioAulaVO, "", diaSemana1, null);
				} else {
					break;
				}
			}
		}
		List<HorarioAlunoDiaVO> horarioAlunoDiaVOs = calendarioHorarioAulaVO.consultarListaCalendarioPorDiaSemana(diaSemana);
		for (HorarioAlunoDiaVO horarioAlunoDiaVO : horarioAlunoDiaVOs) {
			if (horarioAlunoDiaVO.getDia().equals(dia)) {
				return calendarioHorarioAulaVO;
			}
		}
		HorarioAlunoDiaVO horarioAlunoDiaVO = new HorarioAlunoDiaVO();
		if (dia.isEmpty()) {
			horarioAlunoDiaVO.setData(null);
		} else {
			horarioAlunoDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		horarioAlunoDiaVOs.add(horarioAlunoDiaVO);
		if (horarioAlunoDiaVO.getData() != null) {
			if (feriadoVO != null) {
				horarioAlunoDiaVO.setFeriado(feriadoVO);
			}
			// horarioAlunoTurnoVO.adicinarHorarioAlunoPorDiaPorDia(horarioAlunoDiaVO);
		}
		calendarioHorarioAulaVO.adicionarListaCalendarioPorDiaSemana(horarioAlunoDiaVOs, diaSemana);
		return calendarioHorarioAulaVO;
	}

	@Override
	public List<HorarioAlunoTurnoVO> consultarHorarioAlunoPorMatriculaPeriodoDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception {
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			StringBuilder sql = new StringBuilder("");
			sql.append(" select distinct turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
			sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\", ");
			sql.append(" salalocalaula.sala, localaula.local, ");
			sql.append(" horarioturmadiaitem.duracaoaula, horarioturmadiaitem.horarioinicio as horarioinicioaula, horarioturmadiaitem.horariotermino as horariofinalaula, TO_CHAR(horarioturmadia.data, 'D')::INT as diasemana, horarioturmadiaitem.nraula,  array_to_string(array_agg(horarioturmadiaitem.data order by horarioturmadiaitem.data), ',') as datas, configuracaogeralsistema.naoApresentarProfessorVisaoAluno, horarioturmadiaitem.aulareposicao, ");
			if(matriculaPeriodoTurmaDisciplinaVOs.get(0).getMatriculaObjetoVO().getCurso().getIntegral()) {
				sql.append(" horarioturmadia.data as data ");
			}else {
				sql.append(" null as data ");
			}
			sql.append(" from horarioturma ");
			sql.append(" INNER JOIN horarioturmadia ON horarioturma.codigo = horarioturmadia.horarioturma ");
			sql.append(" INNER JOIN horarioturmadiaitem ON horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = horarioturmadiaitem.professor ");
			sql.append(" INNER JOIN disciplina ON disciplina.codigo = horarioturmadiaitem.disciplina");
			sql.append(" INNER JOIN turma ON turma.codigo = horarioturma.turma");
			sql.append(" left JOIN salalocalaula ON salalocalaula.codigo = horarioturmadiaitem.sala");
			sql.append(" left JOIN localaula ON salalocalaula.localaula = localaula.codigo");
			sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
			sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");
			sql.append(" inner join turno on turno.codigo = turma.turno  ");
			sql.append(" where ((turma.anual and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("') or (turma.semestral and horarioturma.anovigente =  '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' and horarioturma.semestrevigente =  '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("')");
			sql.append(" or (turma.anual = false and turma.semestral = false)) and ( ");
			int x = 0;
			boolean possuiTurma = false;
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				String turmaFiltrar = "";
				if(Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())){
					turmaFiltrar += mp.getTurmaPratica().getCodigo();
				}
				if(Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurmaTeorica().getCodigo();
				}
				if(!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurma().getCodigo();
				}

				if (mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta()) {
					if (x > 0) {
						sql.append(" or ");
					}
					if(!turmaFiltrar.trim().isEmpty()){
						possuiTurma = true;
					}
					sql.append(" (((turma.codigo in (").append(turmaFiltrar).append("))  ");
					if(!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
						possuiTurma = true;
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaFiltrar + ")))");
					}
					if(Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())){
						possuiTurma = true;
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaPratica().getCodigo() + ")))");
					}
					if(Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
						possuiTurma = true;
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaTeorica().getCodigo() + ")))");
					}
					sql.append(" ) ");
					sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo = ").append(mp.getDisciplina().getCodigo()).append(" ) ");
					sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
					sql.append(" select ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
					sql.append(" ))) ");
					if(!mp.isNovoObj()){
						/**
						 * Adicionada regra para resolver impactos relacionados a transferencia de matriz curricular pois estava
						 * validando choque de horário com disciplinas da grade anterior
						 */
						sql.append(" and exists (");
						sql.append(" select disciplina from historico ");
						sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
						sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
						sql.append(" where matricula.matricula = '").append(mp.getMatricula()).append("' ");
						sql.append(" and historico.disciplina = ").append(mp.getDisciplina().getCodigo());
						sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
						sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
						sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
						if(!mp.getAno().isEmpty()){
							sql.append(" and historico.anohistorico = '").append(mp.getAno()).append("' ");
						}
						if(!mp.getSemestre().isEmpty()){
							sql.append(" and historico.semestrehistorico = '").append(mp.getSemestre()).append("' ");
						}
						sql.append(" ) ");
					}


					sql.append(" ) ");
					x++;
				}
			}
			sql.append(") ");

			sql.append("  group by turma.codigo, turma.identificadorturma, ");
			sql.append("  pessoa.codigo, pessoa.nome,  disciplina.codigo, disciplina.nome, ");
			sql.append("  turno.codigo, turno.nome,  horarioturmadiaitem.duracaoaula, ");
			sql.append("  horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, horarioturmadiaitem.aulareposicao,");
			sql.append("  TO_CHAR(horarioturmadia.data, 'D')::INT, horarioturmadiaitem.nraula,");
			sql.append("  configuracaogeralsistema.naoApresentarProfessorVisaoAluno, salalocalaula.sala, localaula.local ");
			if(matriculaPeriodoTurmaDisciplinaVOs.get(0).getMatriculaObjetoVO().getCurso().getIntegral()) {
				sql.append(", horarioturmadia.data ");
				sql.append(" order by turno.codigo, data, nraula, \"disciplina.nome\"  ");
			}else {
				sql.append(" order by diasemana, nraula, horarioturmadiaitem.aulareposicao asc, \"disciplina.nome\"  ");
			}
			if(!possuiTurma){
				return new ArrayList<HorarioAlunoTurnoVO>(0);
			}
			return montarDadosHorarioAlunoPorMatriculaPeriodoDisciplinaVOs(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), matriculaPeriodoTurmaDisciplinaVOs, usuario, false);
		}
		return new ArrayList<HorarioAlunoTurnoVO>(0);
	}

	@Override
	public Date consultarPrimeiroDiaAulaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			sql.append(" select min(data) as data from ( ");
			int x = 0;
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				if (mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta()) {
					if (x > 0) {
						sql.append(" union all ");
					}
					sql.append(" select min(distinct horarioturmadetalhado.data) as data ");
					sql.append(" from horarioturmadetalhado(null, " + mp.getTurma().getCodigo() + ", ");
					sql.append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().trim().isEmpty() ? "null" : "'" + matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno() + "'").append(", ");
					sql.append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().trim().isEmpty() ? "null" : "'" + matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre() + "'").append(", ");
					sql.append("null").append(", ");
					sql.append(mp.getDisciplina().getCodigo()).append(", ");
					sql.append("null,");
					sql.append("null) ");
					sql.append(" as horarioturmadetalhado ");
					x++;
				}
			}
			sql.append(") as t ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (rs.next()) {
				return rs.getDate("data");
			}
		}
		return null;
	}

	@Override
	public Date consultarUltimoDiaAulaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			sql.append(" select max(data) as data from ( ");
			int x = 0;
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				if (mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta()) {
					if (x > 0) {
						sql.append(" union all ");
					}
					sql.append(" select max(distinct horarioturmadetalhado.data) as data ");
					sql.append(" from horarioturmadetalhado(null, " + mp.getTurma().getCodigo() + ", ");
					sql.append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().trim().isEmpty() ? "null" : "'" + matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno() + "'").append(", ");
					sql.append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().trim().isEmpty() ? "null" : "'" + matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre() + "'").append(", ");
					sql.append("null").append(", ");
					sql.append(mp.getDisciplina().getCodigo()).append(", ");
					sql.append("null,");
					sql.append("null) ");
					sql.append(" as horarioturmadetalhado ");
					x++;
				}
			}
			sql.append(") as t ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (rs.next()) {
				return rs.getDate("data");
			}
		}
		return null;
	}


	@Override
	public List<HorarioAlunoTurnoVO> consultarMeusHorariosAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, Integer turno, boolean visaoMensal, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, boolean validarAulaNaoRegistrada, Integer codigoUnidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			Date dataInicio = dataBase;
			Date dataTermino = null;
			if (dataBase != null) {
				if (visaoMensal) {
//					dataInicio = Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(Uteis.getDataPrimeiroDiaMes(dataBase))), 1);
					dataInicio = Uteis.obterDataPassada(new Date(), 1000);
//					dataTermino = Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(Uteis.getDataUltimoDiaMes(dataBase))), 1);
					dataTermino = Uteis.obterDataFutura(new Date(), 1000);
				} else {
					dataInicio = (UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(dataBase)), 1));
					dataTermino = (UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(dataBase), 1));
				}
			}
			sql.append(" select horarioturma.turma as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
			sql.append(" horarioturmadia.data,  horarioturmadiaitem.disciplina as \"disciplina.codigo\", horarioturmadiaitem.professor as \"pessoa.codigo\", ");
			sql.append(" horarioturmadiaitem.nraula, horarioturmadiaitem.duracaoaula, horarioturmadia.ocultardataaula, ");
			sql.append(" horarioturmadiaitem.horarioinicio as horarioinicioaula,  horarioturmadiaitem.horariotermino as horariofinalaula, to_char(horarioturmadia.data, 'D')::INT as diasemana, turno.codigo as \"turno.codigo\", ");
			sql.append(" case when configuracaogeralsistema.naoApresentarProfessorVisaoAluno and ").append(usuario.getIsApresentarVisaoAluno()||usuario.getIsApresentarVisaoPais() ? " 1 = 1" : " 1 = 0 ").append(" then '' else professor.nome end as \"pessoa.nome\",  ");
			sql.append(" disciplina.nome as \"disciplina.nome\",");
			sql.append(" turno.nome as \"turno.nome\", configuracaogeralsistema.naoApresentarProfessorVisaoAluno, ");
			sql.append(" sala.codigo as \"sala.codigo\", sala.sala as \"sala.sala\", localaula.local as \"localaula.local\" ");
			sql.append(" from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
			sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
			sql.append(" inner join turma on turma.codigo = horarioturma.turma");
			sql.append(" inner join turno on turno.codigo = turma.turno");
			sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
			if (codigoUnidadeEnsino != null && codigoUnidadeEnsino > 0) {
				sql.append(" and unidadeEnsino.codigo = ").append(codigoUnidadeEnsino).append(" ");
			}
			sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");

			sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
			sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
			sql.append(" left join salalocalaula sala on sala.codigo = horarioturmadiaitem.sala ");
			sql.append(" left join localaula on localaula.codigo = sala.localaula ");
			sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadia.data", false));
			sql.append(" and ((turma.anual = false and turma.semestral = false) ");
			sql.append(" or (turma.anual and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("') ");
			sql.append(" or (turma.semestral and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
			sql.append(" and horarioturma.semestrevigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("')) ");

			int x = 0;
			sql.append(" and ( ");
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				String turmaFiltrar = "";
				if(Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())){
					turmaFiltrar += mp.getTurmaPratica().getCodigo();
				}
				if(Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurmaTeorica().getCodigo();
				}
				if(!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurma().getCodigo();
				}
				if ((mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta())) {
					if (x > 0) {
						sql.append(" or ");
					}
					sql.append(" (((turma.codigo in (").append(turmaFiltrar).append("))  ");
					if (!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaFiltrar + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaPratica().getCodigo() + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaTeorica().getCodigo() + ")))");
					}
					sql.append(" ) ");
					sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo = ").append(mp.getDisciplina().getCodigo()).append(" ) ");
					sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
					sql.append(" select ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
					sql.append(" ))) ");
					if (!mp.isNovoObj()) {
						/**
						 * Adicionada regra para resolver impactos relacionados
						 * a transferencia de matriz curricular pois estava
						 * validando choque de horário com disciplinas da grade
						 * anterior
						 */
						sql.append(" and exists (");
						sql.append(" select disciplina from historico ");
						sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
						sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
						sql.append(" where matricula.matricula = '").append(mp.getMatricula()).append("' ");
						sql.append(" and historico.disciplina = ").append(mp.getDisciplina().getCodigo());
						sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
						sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
						sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().isEmpty()) {
							sql.append(" and historico.anohistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
						}
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().isEmpty()) {
							sql.append(" and historico.semestrehistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
						}
					}
					sql.append(" )) ");
					x++;
				}

			}
			sql.append(") ");
			if (turno != null && turno > 0) {
				sql.append(" and turno.codigo = ").append(turno);
			}
			sql.append(" and horarioturmadia.ocultardataaula = false ");
			if(validarAulaNaoRegistrada) {
				sql.append(" and not exists (select registroaula.codigo from registroaula where registroaula.turma = turma.codigo and registroaula.disciplina = disciplina.codigo and registroaula.nraula = horarioturmadiaitem.nraula and registroaula.data = horarioturmadia.data ) ");
			}


			sql.append(" order by data, nrAula");
//			System.out.println(sql);
			if(validarAulaNaoRegistrada) {
				sql.append(" limit 1 ");
			}
			if(validarAulaNaoRegistrada && getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next()) {
				throw new Exception("Existem aulas com registro pendente na sua turma. Por favor, entre em contato com a Secretaria Acadêmica.");
			}else {
			List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs = montarDadosHorarioAlunoPorMatriculaPeriodoDisciplinaSemanal(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), matriculaPeriodoTurmaDisciplinaVOs, dataInicio, dataTermino, usuario, visaoMensal);
			if (dataBase != null) {
				List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(dataInicio, dataTermino, unidadeEnsinoVO.getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				for (HorarioAlunoTurnoVO horarioAlunoTurnoVO : horarioAlunoTurnoVOs) {
					horarioAlunoTurnoVO.setDataInicio(dataInicio);
					horarioAlunoTurnoVO.setDataTermino(dataTermino);
					for (HorarioAlunoTurnoVO horarioAlunoTurnoSemanaVO : horarioAlunoTurnoVO.getHorarioAlunoTurnoVOs()) {
						horarioAlunoTurnoSemanaVO.setDataSegunda(horarioAlunoTurnoSemanaVO.getDataInicio());
						horarioAlunoTurnoSemanaVO.setDataTerca(UteisData.obterDataFutura(horarioAlunoTurnoSemanaVO.getDataInicio(), 1));
						horarioAlunoTurnoSemanaVO.setDataQuarta(UteisData.obterDataFutura(horarioAlunoTurnoSemanaVO.getDataInicio(), 2));
						horarioAlunoTurnoSemanaVO.setDataQuinta(UteisData.obterDataFutura(horarioAlunoTurnoSemanaVO.getDataInicio(), 3));
						horarioAlunoTurnoSemanaVO.setDataSexta(UteisData.obterDataFutura(horarioAlunoTurnoSemanaVO.getDataInicio(), 4));
						horarioAlunoTurnoSemanaVO.setDataSabado(UteisData.obterDataFutura(horarioAlunoTurnoSemanaVO.getDataInicio(), 5));
						horarioAlunoTurnoSemanaVO.setDataDomingo(UteisData.obterDataFutura(horarioAlunoTurnoSemanaVO.getDataInicio(), 6));
						horarioAlunoTurnoSemanaVO.setFeriadoDomingo(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataDomingo()));
						horarioAlunoTurnoSemanaVO.setFeriadoSegunda(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataSegunda()));
						horarioAlunoTurnoSemanaVO.setFeriadoTerca(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataTerca()));
						horarioAlunoTurnoSemanaVO.setFeriadoQuarta(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataQuarta()));
						horarioAlunoTurnoSemanaVO.setFeriadoQuinta(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataQuinta()));
						horarioAlunoTurnoSemanaVO.setFeriadoSexta(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataSexta()));
						horarioAlunoTurnoSemanaVO.setFeriadoSabado(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioAlunoTurnoSemanaVO.getDataSabado()));
					}
				}
			}
			return horarioAlunoTurnoVOs;
			}
		}
		return new ArrayList<HorarioAlunoTurnoVO>(0);
	}

	public void realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(HorarioAlunoTurnoVO horarioAlunoTurnoVO, List<TurnoHorarioVO> turnoHorarioVOs, DiaSemana diaSemana) throws Exception {
		for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
			HorarioAlunoTurnoNumeroAulaVO horarioAlunoTurnoNumeroAulaVO = horarioAlunoTurnoVO.getObterHorarioAlunoTurnoNumeroAulaVO(turnoHorarioVO.getNumeroAula(), diaSemana);
			horarioAlunoTurnoNumeroAulaVO.setNumeroAula(turnoHorarioVO.getNumeroAula());

			switch (diaSemana) {
			case SEGUNGA:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioSegunda(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoSegunda(turnoHorarioVO.getHorarioFinalAula());
				break;
			case TERCA:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioTerca(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoTerca(turnoHorarioVO.getHorarioFinalAula());
				break;
			case QUARTA:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioQuarta(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoQuarta(turnoHorarioVO.getHorarioFinalAula());
				break;
			case QUINTA:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioQuinta(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoQuinta(turnoHorarioVO.getHorarioFinalAula());
				break;
			case SEXTA:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioSexta(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoSexta(turnoHorarioVO.getHorarioFinalAula());
				break;
			case SABADO:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioSabado(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoSabado(turnoHorarioVO.getHorarioFinalAula());
				break;
			case DOMINGO:
				horarioAlunoTurnoNumeroAulaVO.setHorarioInicioDomingo(turnoHorarioVO.getHorarioInicioAula());
				horarioAlunoTurnoNumeroAulaVO.setHorarioTerminoDomingo(turnoHorarioVO.getHorarioFinalAula());
				break;

			default:
				return;
			}
			adicionarHorarioAlunoTurnoNumeroAulaVO(horarioAlunoTurnoVO, horarioAlunoTurnoNumeroAulaVO);

		}
	}

	public void adicionarHorarioAlunoTurnoNumeroAulaVO(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioAlunoTurnoNumeroAulaVO horarioAlunoTurnoNumeroAulaVO) {
		int index = 0;
		for (HorarioAlunoTurnoNumeroAulaVO obj : horarioAlunoTurnoVO.getHorarioAlunoTurnoNumeroAulaVOs()) {
			if (horarioAlunoTurnoNumeroAulaVO.getNumeroAula().equals(obj.getNumeroAula())) {
				horarioAlunoTurnoVO.getHorarioAlunoTurnoNumeroAulaVOs().set(index, horarioAlunoTurnoNumeroAulaVO);
				return;
			}
			index++;
		}
		horarioAlunoTurnoVO.getHorarioAlunoTurnoNumeroAulaVOs().add(horarioAlunoTurnoNumeroAulaVO);
	}

	public HorarioAlunoTurnoVO realizarInicializarHorarioAlunoTurno(Integer turno, UsuarioVO usuario) throws Exception {
		HorarioAlunoTurnoVO horarioAlunoTurnoVO = new HorarioAlunoTurnoVO();
		horarioAlunoTurnoVO.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(turno, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioSegunda(), DiaSemana.SEGUNGA);
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioTerca(), DiaSemana.TERCA);
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioQuarta(), DiaSemana.QUARTA);
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioQuinta(), DiaSemana.QUINTA);
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioSexta(), DiaSemana.SEXTA);
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioSabado(), DiaSemana.SABADO);
		realizarInicializarHorarioAlunoTurnoHorarioDiaSemana(horarioAlunoTurnoVO, horarioAlunoTurnoVO.getTurno().getTurnoHorarioDomingo(), DiaSemana.DOMINGO);
		return horarioAlunoTurnoVO;

	}
	 /**
	  * Monta os dados do calendario da visão do aluno, onde se o boolean visaoMensal for true irá separar o horario em semanas do mês selecionado;
	  * @param rs
	  * @param matriculaPeriodoTurmaDisciplinaVOs
	  * @param usuario
	  * @param visaoMensal
	  * @return
	  * @throws Exception
	  */
	public List<HorarioAlunoTurnoVO> montarDadosHorarioAlunoPorMatriculaPeriodoDisciplinaSemanal(SqlRowSet rs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataInicioPeriodo, Date dataTerminoPeriodo,  UsuarioVO usuario, boolean visaoMensal) throws Exception {
		Map<Integer, HorarioAlunoTurnoVO> horarioAlunoTurnoVOs = new HashMap<Integer, HorarioAlunoTurnoVO>(0);
		TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO = null;
		HorarioAlunoTurnoVO horarioAlunoTurnoVO = null;
		HorarioAlunoTurnoVO horarioAlunoTurnoSemanalVO = null;
		while (rs.next()) {
			Date dataInicio = rs.getDate("data");
			if(Uteis.getDiaSemana(dataInicio) == 1){
				dataInicio = UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataInicio), -1);
			}
			dataInicio =  UteisData.obterDataFutura(UteisData.getPrimeiroDiaSemana(dataInicio),+1);
			Date dataTermino = UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(dataInicio)), +1);
			String key = rs.getInt("turno.codigo")+"-"+Uteis.getData(dataInicio)+" a "+Uteis.getData(dataTermino);
			//Montar o horario do aluno no turno especifico
			if(!horarioAlunoTurnoVOs.containsKey(rs.getInt("turno.codigo"))){
				horarioAlunoTurnoVO = realizarInicializarHorarioAlunoTurno(rs.getInt("turno.codigo"), usuario);
				horarioAlunoTurnoVO.setNaoApresentarProfessorVisaoAluno(rs.getBoolean("naoApresentarProfessorVisaoAluno"));
				horarioAlunoTurnoVOs.put(rs.getInt("turno.codigo"), horarioAlunoTurnoVO);
			}else{
				horarioAlunoTurnoVO = horarioAlunoTurnoVOs.get(rs.getInt("turno.codigo"));
			}
			//Montar o horario do aluno no turno e semana especifico
			if (!horarioAlunoTurnoVO.getHorarioAlunoTurnoSemanaVOs().containsKey(key)) {
				horarioAlunoTurnoSemanalVO = realizarInicializarHorarioAlunoTurno(rs.getInt("turno.codigo"), usuario);
				horarioAlunoTurnoSemanalVO.setDataInicio(dataInicio);
				horarioAlunoTurnoSemanalVO.setDataTermino(dataTermino);
				horarioAlunoTurnoSemanalVO.setNaoApresentarProfessorVisaoAluno(rs.getBoolean("naoApresentarProfessorVisaoAluno"));
				horarioAlunoTurnoVO.getHorarioAlunoTurnoSemanaVOs().put(key, horarioAlunoTurnoSemanalVO);
			}else{
				horarioAlunoTurnoSemanalVO = horarioAlunoTurnoVO.getHorarioAlunoTurnoSemanaVOs().get(key);
			}

			turmaProfessorDisciplinaVO = new TurmaProfessorDisciplinaVO();
			turmaProfessorDisciplinaVO.getTurmaVO().setCodigo(rs.getInt("turma.codigo"));
			turmaProfessorDisciplinaVO.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			turmaProfessorDisciplinaVO.getProfessorVO().setCodigo(rs.getInt("pessoa.codigo"));
			turmaProfessorDisciplinaVO.getProfessorVO().setNome(rs.getString("pessoa.nome"));
			turmaProfessorDisciplinaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina.codigo"));
			turmaProfessorDisciplinaVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
			turmaProfessorDisciplinaVO.getSala().setCodigo(rs.getInt("sala.codigo"));
			turmaProfessorDisciplinaVO.getSala().setSala(rs.getString("sala.sala"));
			turmaProfessorDisciplinaVO.getSala().getLocalAula().setLocal(rs.getString("localaula.local"));
			adicionarTurmaProfessorDisciplinaVO(horarioAlunoTurnoSemanalVO, turmaProfessorDisciplinaVO, horarioAlunoTurnoVO.getTurno().getNaoApresentarHorarioVisaoAluno() ? null : rs.getInt("nrAula"), DiaSemana.getEnum(rs.getInt("diasemana")), rs.getDate("data"));
		}
		List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs2 = new ArrayList<HorarioAlunoTurnoVO>(horarioAlunoTurnoVOs.values());
		for (Iterator iterator = horarioAlunoTurnoVOs2.iterator(); iterator.hasNext();) {
			HorarioAlunoTurnoVO horarioAlunoTurnoVO2 = (HorarioAlunoTurnoVO) iterator.next();
			horarioAlunoTurnoVO2.setPossuiAulaDomingo(false);
			horarioAlunoTurnoVO2.setPossuiAulaSegunda(false);
			horarioAlunoTurnoVO2.setPossuiAulaTerca(false);
			horarioAlunoTurnoVO2.setPossuiAulaQuarta(false);
			horarioAlunoTurnoVO2.setPossuiAulaQuinta(false);
			horarioAlunoTurnoVO2.setPossuiAulaSexta(false);
			horarioAlunoTurnoVO2.setPossuiAulaSabado(false);
			for (Iterator iterator3 = horarioAlunoTurnoVO2.getHorarioAlunoTurnoVOs().iterator(); iterator3.hasNext();) {
				HorarioAlunoTurnoVO horarioAlunoTurnoSemanal = (HorarioAlunoTurnoVO) iterator3.next();
				horarioAlunoTurnoSemanal.setPossuiAulaDomingo(false);
				horarioAlunoTurnoSemanal.setPossuiAulaSegunda(false);
				horarioAlunoTurnoSemanal.setPossuiAulaTerca(false);
				horarioAlunoTurnoSemanal.setPossuiAulaQuarta(false);
				horarioAlunoTurnoSemanal.setPossuiAulaQuinta(false);
				horarioAlunoTurnoSemanal.setPossuiAulaSexta(false);
				horarioAlunoTurnoSemanal.setPossuiAulaSabado(false);
			for (Iterator iterator2 = horarioAlunoTurnoSemanal.getHorarioAlunoTurnoNumeroAulaVOs().iterator(); iterator2.hasNext();) {
				HorarioAlunoTurnoNumeroAulaVO horarioAlunoTurnoNumeroAulaVO = (HorarioAlunoTurnoNumeroAulaVO) iterator2.next();
				if(horarioAlunoTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().isEmpty() && horarioAlunoTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().isEmpty()
						&& horarioAlunoTurnoNumeroAulaVO.getDisciplinaTercaApresentar().isEmpty() && horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().isEmpty()
						&& horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().isEmpty() && horarioAlunoTurnoNumeroAulaVO.getDisciplinaSextaApresentar().isEmpty()
						&& horarioAlunoTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().isEmpty()){
					iterator2.remove();
				}else{
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaDomingo(true);
						horarioAlunoTurnoSemanal.setPossuiAulaDomingo(true);
					}
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaSegunda(true);
						horarioAlunoTurnoSemanal.setPossuiAulaSegunda(true);
					}
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaTercaApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaTerca(true);
						horarioAlunoTurnoSemanal.setPossuiAulaTerca(true);
					}
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaQuarta(true);
						horarioAlunoTurnoSemanal.setPossuiAulaQuarta(true);
					}
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaQuinta(true);
						horarioAlunoTurnoSemanal.setPossuiAulaQuinta(true);
					}
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaSextaApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaSexta(true);
						horarioAlunoTurnoSemanal.setPossuiAulaSexta(true);
					}
					if(!horarioAlunoTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().isEmpty()){
						horarioAlunoTurnoVO2.setPossuiAulaSabado(true);
						horarioAlunoTurnoSemanal.setPossuiAulaSabado(true);
					}
				}
			}
			}
		}
		if (!horarioAlunoTurnoVOs2.isEmpty()) {
			Ordenacao.ordenarLista(horarioAlunoTurnoVOs2.get(0).getHorarioAlunoTurnoVOs(), "dataInicio");
		}
		return horarioAlunoTurnoVOs2;
	}

	public List<HorarioAlunoTurnoVO> montarDadosHorarioAlunoPorMatriculaPeriodoDisciplinaVOs(SqlRowSet rs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario, boolean visaoMensal) throws Exception {
		Map<String, HorarioAlunoTurnoVO> horarioAlunoTurnoVOs = new HashMap<String, HorarioAlunoTurnoVO>(0);
		TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO = null;
		HorarioAlunoTurnoVO horarioAlunoTurnoVO = null;
		if(!matriculaPeriodoTurmaDisciplinaVOs.isEmpty() && matriculaPeriodoTurmaDisciplinaVOs.get(0).getMatriculaObjetoVO().getCurso().getIntegral()) {
			return montarDadosHorarioAlunoPorMatriculaPeriodoDisciplinaCursoIntegralVOs(rs, matriculaPeriodoTurmaDisciplinaVOs, usuario, visaoMensal);
		}
		while (rs.next()) {
			String key = "" + rs.getInt("turno.codigo");
			if (!horarioAlunoTurnoVOs.containsKey(key)) {
				horarioAlunoTurnoVO = realizarInicializarHorarioAlunoTurno(rs.getInt("turno.codigo"), usuario);
				if (rs.getDate("data") != null) {
					Date dataInicio = UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(rs.getDate("data"))), 1);
					Date dataTermino = UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(rs.getDate("data"))), 1);
					horarioAlunoTurnoVO.setDataInicio(dataInicio);
					horarioAlunoTurnoVO.setDataTermino(dataTermino);
				}
				horarioAlunoTurnoVOs.put(key, horarioAlunoTurnoVO);
			}else{
				horarioAlunoTurnoVO = horarioAlunoTurnoVOs.get(key);
			}

			horarioAlunoTurnoVO.setNaoApresentarProfessorVisaoAluno(rs.getBoolean("naoApresentarProfessorVisaoAluno"));
			turmaProfessorDisciplinaVO = new TurmaProfessorDisciplinaVO();
			turmaProfessorDisciplinaVO.getTurmaVO().setCodigo(rs.getInt("turma.codigo"));
			turmaProfessorDisciplinaVO.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			turmaProfessorDisciplinaVO.getProfessorVO().setCodigo(rs.getInt("pessoa.codigo"));
			turmaProfessorDisciplinaVO.getProfessorVO().setNome(rs.getString("pessoa.nome"));
			turmaProfessorDisciplinaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina.codigo"));
			turmaProfessorDisciplinaVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
			turmaProfessorDisciplinaVO.getSala().setSala(rs.getString("sala"));
			turmaProfessorDisciplinaVO.getSala().getLocalAula().setLocal(rs.getString("local"));
			HorarioAlunoTurnoNumeroAulaVO obj = horarioAlunoTurnoVO.getObterHorarioAlunoTurnoNumeroAulaVO(rs.getInt("nrAula"), DiaSemana.getEnum(rs.getInt("diasemana")));
			String[] datas = rs.getString("datas").split(",");
			for(String data:datas){
				Date dataBase = Uteis.getData(data.trim(), "yyyy-MM-dd");
				if(obj.getDataAulaTurmaProfessorDisciplinaVOs().containsKey(dataBase)){
					TurmaProfessorDisciplinaVO td2 = obj.getDataAulaTurmaProfessorDisciplinaVOs().get(dataBase);
					StringBuilder descricaoChoque = new StringBuilder("");
					descricaoChoque.append("O horário da disciplina '").append(rs.getString("disciplina.nome")).append("' na turma '");
					descricaoChoque.append(rs.getString("turma.identificadorTurma")).append("' é o mesmo horário da disciplina '");
					descricaoChoque.append(td2.getDisciplinaVO().getNome()).append("' na turma '").append(td2.getTurmaVO().getIdentificadorTurma()).append("'.");
					switch (DiaSemana.getEnum(rs.getInt("diasemana"))) {
					case DOMINGO:
						obj.setChoqueHorarioDomingo(true);
						obj.setMensagemChoqueHorarioDomingo(descricaoChoque.toString());
						break;
					case SEGUNGA:
						obj.setChoqueHorarioSegunda(true);
						obj.setMensagemChoqueHorarioSegunda(descricaoChoque.toString());
						break;
					case TERCA:
						obj.setChoqueHorarioTerca(true);
						obj.setMensagemChoqueHorarioTerca(descricaoChoque.toString());
						break;
					case QUARTA:
						obj.setChoqueHorarioQuarta(true);
						obj.setMensagemChoqueHorarioQuarta(descricaoChoque.toString());
						break;
					case QUINTA:
						obj.setChoqueHorarioQuinta(true);
						obj.setMensagemChoqueHorarioQuinta(descricaoChoque.toString());
						break;
					case SEXTA:
						obj.setChoqueHorarioSexta(true);
						obj.setMensagemChoqueHorarioSexta(descricaoChoque.toString());
						break;
					case SABADO:
						obj.setChoqueHorarioSabado(true);
						obj.setMensagemChoqueHorarioSabado(descricaoChoque.toString());
						break;
					default:
						break;
					}
					break;
				}else{
					obj.getDataAulaTurmaProfessorDisciplinaVOs().put(dataBase, turmaProfessorDisciplinaVO);
				}
			}
			adicionarTurmaProfessorDisciplinaVO(horarioAlunoTurnoVO, turmaProfessorDisciplinaVO, rs.getInt("nrAula"), DiaSemana.getEnum(rs.getInt("diasemana")), rs.getDate("data"));
		}
		List<HorarioAlunoTurnoVO> horarioAlunoTurnoList = new ArrayList<HorarioAlunoTurnoVO>(horarioAlunoTurnoVOs.values());
		return horarioAlunoTurnoList;
	}

	public void adicionarTurmaProfessorDisciplinaVO(HorarioAlunoTurnoVO horarioAlunoTurnoVO, TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO, Integer numeroAula, DiaSemana diaSemana, Date dataAula) {
		HorarioAlunoTurnoNumeroAulaVO obj = null;
		obj = horarioAlunoTurnoVO.getObterHorarioAlunoTurnoNumeroAulaVO(numeroAula, diaSemana);
		switch (diaSemana) {
		case SEGUNGA:
			obj.getTurmaProfessorDisciplinaSegundaVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataSegunda(dataAula);
			break;
		case TERCA:
			obj.getTurmaProfessorDisciplinaTercaVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataTerca(dataAula);
			break;
		case QUARTA:
			obj.getTurmaProfessorDisciplinaQuartaVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataQuarta(dataAula);
			break;
		case QUINTA:
			obj.getTurmaProfessorDisciplinaQuintaVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataQuinta(dataAula);
			break;
		case SEXTA:
			obj.getTurmaProfessorDisciplinaSextaVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataSexta(dataAula);
			break;
		case SABADO:
			obj.getTurmaProfessorDisciplinaSabadoVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataSabado(dataAula);
			break;
		case DOMINGO:
			obj.getTurmaProfessorDisciplinaDomingoVOs().add(turmaProfessorDisciplinaVO);
			horarioAlunoTurnoVO.setDataDomingo(dataAula);
			break;

		default:
			return;
		}
	}

	public void adicionarChoqueHoraTurmaProfessorDisciplinaVO(HorarioAlunoTurnoVO horarioAlunoTurnoVO, TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO, Integer numeroAula, DiaSemana diaSemana, String msg) {
		HorarioAlunoTurnoNumeroAulaVO obj = horarioAlunoTurnoVO.getObterHorarioAlunoTurnoNumeroAulaVO(numeroAula, diaSemana);
		switch (diaSemana) {
		case SEGUNGA:
			obj.setChoqueHorarioSegunda(true);
			if (!obj.getMensagemChoqueHorarioSegunda().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioSegunda().contains(msg)) {
					obj.setMensagemChoqueHorarioSegunda(obj.getMensagemChoqueHorarioSegunda() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioSegunda(msg);
			}
			break;
		case TERCA:
			obj.setChoqueHorarioTerca(true);
			if (!obj.getMensagemChoqueHorarioTerca().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioTerca().contains(msg)) {
					obj.setMensagemChoqueHorarioTerca(obj.getMensagemChoqueHorarioTerca() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioTerca(msg);
			}
			break;
		case QUARTA:
			obj.setChoqueHorarioQuarta(true);
			if (!obj.getMensagemChoqueHorarioQuarta().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioQuarta().contains(msg)) {
					obj.setMensagemChoqueHorarioQuarta(obj.getMensagemChoqueHorarioQuarta() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioQuarta(msg);
			}
			break;
		case QUINTA:
			obj.setChoqueHorarioQuinta(true);
			if (!obj.getMensagemChoqueHorarioQuinta().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioQuinta().contains(msg)) {
					obj.setMensagemChoqueHorarioQuinta(obj.getMensagemChoqueHorarioQuinta() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioQuinta(msg);
			}
			break;
		case SEXTA:
			obj.setChoqueHorarioSexta(true);
			if (!obj.getMensagemChoqueHorarioSexta().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioSexta().contains(msg)) {
					obj.setMensagemChoqueHorarioSexta(obj.getMensagemChoqueHorarioSexta() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioSexta(msg);
			}
			break;
		case SABADO:
			if (!obj.getMensagemChoqueHorarioSabado().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioSabado().contains(msg)) {
					obj.setMensagemChoqueHorarioSabado(obj.getMensagemChoqueHorarioSabado() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioSabado(msg);
			}
			break;
		case DOMINGO:
			if (!obj.getMensagemChoqueHorarioDomingo().trim().isEmpty()) {
				if (!obj.getMensagemChoqueHorarioDomingo().contains(msg)) {
					obj.setMensagemChoqueHorarioDomingo(obj.getMensagemChoqueHorarioDomingo() + "<br/>" + msg);
				}
			} else {
				obj.setMensagemChoqueHorarioDomingo(msg);
			}
			break;

		default:
			return;
		}
	}

	/**
	 * Este metodo verifica choque de horario ao realizar a inclusao de uma
	 * disciplina no ato da renovação ou nova matricula
	 *
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param turma
	 * @param disciplina
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public Boolean realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(MatriculaPeriodoVO matriculaPeriodoVO, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Integer turma, Integer turmaPratica, Integer turmaTeorica, Integer disciplina, UsuarioVO usuarioVO, Boolean retornarExcecao, Boolean validarChoqueComOutrasMatriculaAluno) throws Exception {
//		String msg = "";
//		realizarLimpezaRegistroChoqueHorario(horarioAlunoTurnoVOs);
//		matriculaPeriodoVO.setMensagemErro("");
//		if(validarChoqueComOutrasMatriculaAluno) {
//			if (matriculaPeriodoVO.getMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs().isEmpty()) {
//				List<MatriculaVO> matriculaVOs =  getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaESituacao(matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo(), "'AT', 'PR'", matriculaPeriodoVO.getMatricula(), 0, false, usuarioVO);
//				for(MatriculaVO matriculaVO: matriculaVOs) {
//					MatriculaPeriodoVO matriculaPeriodoVO2 = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuarioVO);
//					if(Uteis.isAtributoPreenchido(matriculaPeriodoVO2)) {
//						matriculaPeriodoVO2.setMatriculaVO(matriculaVO);
//						matriculaPeriodoVO.getMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs().put(matriculaPeriodoVO2, getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestre(matriculaVO.getMatricula(), 0, 0, matriculaPeriodoVO2.getAno(), matriculaPeriodoVO2.getSemestre(), false, false, false, false, false, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//					}
//				}
//			}
//			if(!matriculaPeriodoVO.getMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs().isEmpty()) {
//				for(MatriculaPeriodoVO matriculaPeriodoVO2: matriculaPeriodoVO.getMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs().keySet()) {
//					if(realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO2, horarioAlunoTurnoVOs, matriculaPeriodoVO.getMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs().get(matriculaPeriodoVO2), turma, turmaPratica, turmaTeorica, disciplina, usuarioVO, retornarExcecao, false)) {
//						matriculaPeriodoVO.setMensagemErro(matriculaPeriodoVO2.getMensagemErro());
//						return true;
//					}
//				}
//			}
//		}
//
//		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
//
//			StringBuilder sql = new StringBuilder("");
//			sql.append(" select distinct horarioAluno.\"turma.codigo\" as \"turma.codigo\", horarioAluno.\"turma.identificadorturma\" as \"turma.identificadorturma\", horarioAluno.\"pessoa.codigo\" as \"pessoa.codigo\", horarioAluno.\"pessoa.nome\" as \"pessoa.nome\", ");
//			sql.append(" horarioAluno.\"disciplina.codigo\" as \"disciplina.codigo\", horarioAluno.\"disciplina.nome\" as \"disciplina.nome\", horarioAluno.\"turno.codigo\" as \"turno.codigo\", horarioAluno.\"turno.nome\" as \"turno.nome\", ");
//			sql.append(" horarioAluno.duracaoaula, horarioAluno.horarioinicioaula, horarioAluno.horariofinalaula, horarioAluno.diasemana, horarioAluno.nraula, horariodisciplinaincluida.disciplinaIncluida, horariodisciplinaincluida.turmaincluida ");
//			if (horarioAlunoTurnoVOs != null && !horarioAlunoTurnoVOs.isEmpty() && horarioAlunoTurnoVOs.get(0).getHorarioIntegral()) {
//				sql.append(", horarioAluno.data ");
//			}
//			sql.append(" from  ");
//			sql.append(" (select turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\",   ");
//			sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",   ");
//			sql.append(" horarioturmadiaitem.duracaoaula, horarioturmadiaitem.horarioinicio as horarioinicioaula, horarioturmadiaitem.horariotermino as horariofinalaula, TO_CHAR(horarioturmadia.data, 'D')::INT AS diasemana, ");
//			sql.append(" horarioturmadiaitem.nraula, horarioturmadia.data ");
//			sql.append(" FROM horarioturma ");
//			sql.append(" INNER JOIN horarioturmadia ON horarioturma.codigo = horarioturmadia.horarioturma");
//			sql.append(" INNER JOIN horarioturmadiaitem ON horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
//			sql.append(" INNER JOIN pessoa ON pessoa.codigo = horarioturmadiaitem.professor ");
//			sql.append(" INNER JOIN disciplina ON disciplina.codigo = horarioturmadiaitem.disciplina");
//			sql.append(" INNER JOIN turma ON turma.codigo = horarioturma.turma ");
//			sql.append(" INNER JOIN turno ON turno.codigo = turma.turno");
//			sql.append(" WHERE ");
//			sql.append(" ((turma.anual and horarioturma.anovigente = '").append(matriculaPeriodoVO.getAno()).append("') or (turma.semestral and horarioturma.anovigente =  '").append(matriculaPeriodoVO.getAno()).append("' and horarioturma.semestrevigente =  '").append(matriculaPeriodoVO.getSemestre()).append("')");
//			sql.append(" or (turma.anual = false and turma.semestral = false)) and ( ");
//			int x = 0;
//			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
//				if (mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta()) {
//					if (x > 0) {
//						sql.append(" or ");
//					}
//					String turmaFiltrar = "";
//					if (Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())) {
//						turmaFiltrar += mp.getTurmaPratica().getCodigo();
//					}
//					if (Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
//						turmaFiltrar += (!turmaFiltrar.isEmpty() ? "," : "") + mp.getTurmaTeorica().getCodigo();
//					}
//					if (!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
//						turmaFiltrar += (!turmaFiltrar.isEmpty() ? "," : "") + mp.getTurma().getCodigo();
//					}
//					sql.append(" ((turma.codigo in (").append(turmaFiltrar).append(") ");
//					if (!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
//						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaFiltrar + ")))");
//					}
//					if (Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())) {
//						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaPratica().getCodigo() + ")))");
//					}
//					if (Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
//						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaTeorica().getCodigo() + ")))");
//					}
//					sql.append(" ) ");
//					sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo = ").append(mp.getDisciplina().getCodigo()).append(" ) ");
//					sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
//					sql.append(" select ").append(mp.getDisciplina().getCodigo());
//					sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
//					sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
//					sql.append(" ))) ");
//					if (!mp.isNovoObj()) {
//						/**
//						 * Adicionada regra para resolver impactos relacionados
//						 * a transferencia de matriz curricular pois estava
//						 * validando choque de horário com disciplinas da grade
//						 * anterior
//						 */
//						sql.append(" and exists (");
//						sql.append(" select disciplina from historico ");
//						sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
//						sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
//						sql.append(" where matricula.matricula = '").append(mp.getMatricula()).append("' ");
//						sql.append(" and historico.disciplina  = ").append(mp.getDisciplina().getCodigo());
//						sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
//						sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
//						sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
//						if (!mp.getAno().isEmpty()) {
//							sql.append(" and historico.anohistorico = '").append(mp.getAno()).append("' ");
//						}
//						if (!mp.getSemestre().isEmpty()) {
//							sql.append(" and historico.semestrehistorico = '").append(mp.getSemestre()).append("' ");
//						}
//						sql.append(" ) ");
//					}
//					sql.append(") ");
//					x++;
//				}
//			}
//			if (x == 0) {
//				return false;
//			}
//			sql.append(") ");
//
//
//
//
//			/**
//			 * Adicionado regra para desconsiderar no ato da validação de choque
//			 * de horario disciplinas que tenha sido incluidas atraves de
//			 * aproveitamento ou de transferenciaentrada.
//			 */
//			sql.append("and disciplina.codigo not in (");
//			sql.append("select da.disciplina from aproveitamentodisciplina ad ");
//			sql.append("inner join disciplinasaproveitadas da on da.aproveitamentodisciplina = ad.codigo ");
//			sql.append("where ad.matricula = '").append(matriculaPeriodoVO.getMatricula()).append("' ");
//			sql.append("and da.disciplina in (0");
//			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
//				sql.append(", ").append(mp.getDisciplina().getCodigo());
//			}
//			sql.append(")");
//			sql.append(" union ");
//			sql.append("select teda.disciplina from transferenciaentrada te ");
//			sql.append("inner join transferenciaentradadisciplinasaproveitadas teda on teda.transferenciaentrada = te.codigo ");
//			sql.append("where te.matricula = '").append(matriculaPeriodoVO.getMatricula()).append("' ");
//			sql.append("and teda.disciplina in (0");
//			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
//				sql.append(", ").append(mp.getDisciplina().getCodigo());
//			}
//			sql.append(")");
//			sql.append(")) as horarioAluno ");
//			sql.append(" inner join (select horarioturmadiaitem.horarioinicio as horarioinicioaula, horarioturmadiaitem.horariotermino as horariofinalaula, horarioturmadia.data, disciplina.nome as disciplinaIncluida, turma.identificadorturma as turmaincluida ");
//			sql.append(" FROM horarioturma ");
//			sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
//			sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
//			sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
//			sql.append(" INNER JOIN disciplina ON disciplina.codigo = horarioturmadiaitem.disciplina  ");
//			sql.append(" where ((turma.anual and horarioturma.anovigente = '").append(matriculaPeriodoVO.getAno()).append("') or (turma.semestral and horarioturma.anovigente =  '").append(matriculaPeriodoVO.getAno()).append("' and horarioturma.semestrevigente =  '").append(matriculaPeriodoVO.getSemestre()).append("')");
//			sql.append(" or (turma.anual = false and turma.semestral = false)) ");
//			if (Uteis.isAtributoPreenchido(turmaPratica) && Uteis.isAtributoPreenchido(turmaTeorica)) {
//				sql.append(" and (horarioturma.turma in (" + turmaPratica + "," + turmaTeorica + ") ");
//				sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + +turmaPratica + "," + turmaTeorica + "))))");
//			} else if (Uteis.isAtributoPreenchido(turmaPratica) && !Uteis.isAtributoPreenchido(turmaTeorica)) {
//				sql.append(" and (horarioturma.turma = " + turmaPratica);
//				sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + turmaPratica + "))))");
//			} else if (!Uteis.isAtributoPreenchido(turmaPratica) && Uteis.isAtributoPreenchido(turmaTeorica)) {
//				sql.append(" and (horarioturma.turma = " + turmaTeorica);
//				sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + turmaTeorica + "))))");
//			} else {
//				sql.append(" and (horarioturma.turma = ").append(turma).append(" or horarioturma.turma in (select TurmaAgrupada.turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaorigem where TurmaAgrupada.turma = " + turma + " and t.subturma = false )) ");
//			}
//			sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo in ( ");
//			sql.append(" select ").append(disciplina);
//			sql.append(" union ");
//			sql.append(" select gdc.disciplina from gradecurricular ");
//			sql.append(" inner join gradecurriculargrupooptativa gco on gco.gradecurricular = gradecurricular.codigo ");
//			sql.append(" inner join gradecurriculargrupooptativadisciplina gcod on gco.codigo = gcod.gradecurriculargrupooptativa ");
//			sql.append(" inner join gradedisciplinacomposta gdc on gcod.codigo = gdc.gradecurriculargrupooptativadisciplina ");
//			sql.append(" where gcod.disciplina = ").append(disciplina).append(" and gradecurricular.codigo = ").append(matriculaPeriodoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo());
//			sql.append(" union ");
//			sql.append(" select gdc.disciplina from gradecurricular ");
//			sql.append(" inner join periodoletivo pl on pl.gradecurricular = gradecurricular.codigo ");
//			sql.append(" inner join  gradedisciplina gd on pl.codigo = gd.periodoletivo ");
//			sql.append(" inner join gradedisciplinacomposta gdc on gd.codigo = gdc.gradedisciplina and gd.tipocontrolecomposicao = '").append(TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS.getValor()).append("' ");
//			sql.append(" where gd.disciplina =  ").append(disciplina).append(" and gradecurricular.codigo = ").append(matriculaPeriodoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo());
//			sql.append("     ))  ");
//			sql.append("     or (turma.turmaagrupada = true and disciplina.codigo in ( ");
//			sql.append(" 	select ").append(disciplina);
//			sql.append(" 	union select disciplina from disciplinaequivalente  where equivalente = ").append(disciplina);
//			sql.append(" 	union select equivalente from disciplinaequivalente  where disciplina = ").append(disciplina);
//
//			sql.append(" )) ");
//			sql.append(" ) ");
//			sql.append("  ) as horariodisciplinaincluida ");
//			sql.append(" on horariodisciplinaincluida.data = horarioAluno.data  ");
//			sql.append(" and ((horariodisciplinaincluida.horarioinicioaula >= horarioAluno.horarioinicioaula and horariodisciplinaincluida.horarioinicioaula < horarioAluno.horariofinalaula) ");
//			sql.append(" or (horariodisciplinaincluida.horarioinicioaula < horarioAluno.horarioinicioaula and horariodisciplinaincluida.horariofinalaula > horarioAluno.horarioinicioaula) ");
//			sql.append(" or (horariodisciplinaincluida.horariofinalaula > horarioAluno.horarioinicioaula and horariodisciplinaincluida.horariofinalaula <= horarioAluno.horariofinalaula) ");
//			sql.append(" ) ");
////			System.out.println(sql.toString());
//			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//			while (rs.next()) {
//				if (horarioAlunoTurnoVOs != null && !horarioAlunoTurnoVOs.isEmpty()) {
//					HorarioAlunoTurnoVO horarioAlunoTurnoVO = null;
//					for (HorarioAlunoTurnoVO obj : horarioAlunoTurnoVOs) {
//						if (obj.getTurno().getCodigo().equals(rs.getInt("turno.codigo"))) {
//							horarioAlunoTurnoVO = obj;
//							break;
//						}
//					}
//					if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatricula())){
//						msg = "O horário da disciplina '" + rs.getString("disciplinaIncluida") + "' na turma '"+rs.getString("turmaIncluida")+"' é o mesmo horário da disciplina '" + rs.getString("disciplina.nome") + "' na turma '" + rs.getString("turma.identificadorTurma") + "' da matrícula '"+matriculaPeriodoVO.getMatricula()+"'.";
//					}else {
//						msg = "O horário da disciplina '" + rs.getString("disciplinaIncluida") + "' na turma '"+rs.getString("turmaIncluida")+"' é o mesmo horário da disciplina '" + rs.getString("disciplina.nome") + "' na turma '" + rs.getString("turma.identificadorTurma") + "'.";
//					}
//					if(horarioAlunoTurnoVO.getHorarioIntegral()) {
//						HorarioAlunoDiaItemVO horarioAlunoDiaItemVO = horarioAlunoTurnoVO.obterHorarioAlunoDiaItemPorDataNrAula(rs.getDate("data"), rs.getInt("nrAula"));
//						if(!horarioAlunoDiaItemVO.getDisciplinaLivre()) {
//							HorarioAlunoDiaItemVO horarioAlunoDiaItemVO2 = new HorarioAlunoDiaItemVO();
//							horarioAlunoDiaItemVO2.getTurma().setIdentificadorTurma(rs.getString("turmaIncluida"));
//							horarioAlunoDiaItemVO2.getDisciplina().setCodigo(disciplina);
//							horarioAlunoDiaItemVO2.getDisciplina().setNome(rs.getString("disciplinaincluida"));
//							horarioAlunoDiaItemVO2.setNrAula(rs.getInt("nrAula"));
//							horarioAlunoDiaItemVO2.setData(rs.getDate("data"));
//							horarioAlunoDiaItemVO.getHorarioAlunoDiaItemVOs().add(horarioAlunoDiaItemVO2);
//						}
//					} else {
//						TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO = new TurmaProfessorDisciplinaVO();
//						turmaProfessorDisciplinaVO.getTurmaVO().setCodigo(rs.getInt("turma.codigo"));
//						turmaProfessorDisciplinaVO.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
//						turmaProfessorDisciplinaVO.getProfessorVO().setCodigo(rs.getInt("pessoa.codigo"));
//						turmaProfessorDisciplinaVO.getProfessorVO().setNome(rs.getString("pessoa.nome"));
//						turmaProfessorDisciplinaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina.codigo"));
//						turmaProfessorDisciplinaVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
//						adicionarChoqueHoraTurmaProfessorDisciplinaVO(horarioAlunoTurnoVO, turmaProfessorDisciplinaVO, rs.getInt("nrAula"), DiaSemana.getEnum(rs.getInt("diasemana")), msg);
//					}
//				} else {
//					if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatricula())){
//						msg = "O horário da disciplina '" + rs.getString("disciplinaIncluida") + "' na turma '"+rs.getString("turmaIncluida")+"' é o mesmo horário da disciplina '" + rs.getString("disciplina.nome") + "' na turma '" + rs.getString("turma.identificadorTurma") + "' da matrícula '"+matriculaPeriodoVO.getMatricula()+ "'.";
//					}else {
//						msg = "O horário da disciplina '" + rs.getString("disciplinaIncluida") + "' na turma '"+rs.getString("turmaIncluida")+"' é o mesmo horário da disciplina '" + rs.getString("disciplina.nome") + "' na turma '" + rs.getString("turma.identificadorTurma") + "'.";
//					}
//					break;
//				}
//			}
//
//		}
//		if (!msg.trim().isEmpty()) {
//			if(retornarExcecao){
//				ConsistirException horarioException = new ConsistirException(msg);
//				horarioException.setReferenteChoqueHorario(true);
//				throw horarioException;
//			}else {
//				matriculaPeriodoVO.setMensagemErro(msg);
//			}
//			return true;
//		}
		return false;
	}

	@Override
	public void realizarLimpezaRegistroChoqueHorario(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
		if (horarioAlunoTurnoVOs != null && !horarioAlunoTurnoVOs.isEmpty()) {
			for (HorarioAlunoTurnoVO horarioAlunoTurnoVO : horarioAlunoTurnoVOs) {
				if (horarioAlunoTurnoVO.getHorarioIntegral()) {
					for (HorarioAlunoDiaItemVO horarioAlunoDiaItemVO : horarioAlunoTurnoVO.getHorarioAlunoDiaItemVOs()) {
						horarioAlunoDiaItemVO.getHorarioAlunoDiaItemVOs().removeIf(filter -> !filter.getDisciplinaIncluida());
					}
				} else {
					for (HorarioAlunoTurnoNumeroAulaVO horarioAlunoTurnoNumeroAulaVO : horarioAlunoTurnoVO.getHorarioAlunoTurnoNumeroAulaVOs()) {
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioDomingo(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioDomingo("");
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioSegunda(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioSegunda("");
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioTerca(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioTerca("");
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioQuarta(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioQuarta("");
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioQuinta(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioQuinta("");
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioSexta(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioSexta("");
						horarioAlunoTurnoNumeroAulaVO.setChoqueHorarioSabado(false);
						horarioAlunoTurnoNumeroAulaVO.setMensagemChoqueHorarioSabado("");
					}
				}
			}
		}
	}

	@Override
	public List<ChoqueHorarioAlunoVO> consultarChoqueHorarioAlunoPorTurmaAnoSemestre(TurmaVO turma, String ano, String semestre, Integer disciplina){
		StringBuilder sql = new StringBuilder("select matricula, aluno, count(distinct data) as qtdeDiaComChoque, count(distinct to_char(data, 'dd/MM/YY')||horarioinicio) as qtdeAulaComChoque, array_to_string(array_agg(turmaDisciplinas), '<DISICIPLINA>') as turmaDisciplinas from ( ");
		sql.append(" select matricula, aluno, DATA, (to_char (DATA,'dd/MM/YY' )|| horarioinicio ) AS horarioinicio, unnest(turmaDisciplinas) as turmaDisciplinas from ( ");
		sql.append(" select matricula.matricula, pessoa.nome as aluno,  ");
		sql.append(" horarioturmadia.data, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, ");
		sql.append(" array_agg(distinct disciplina.nome||'<TURMA>'|| turma.identificadorturma) as turmaDisciplinas");
		sql.append(" FROM matricula");
		sql.append(" INNER JOIN pessoa 							ON pessoa.codigo 				= matricula.aluno");
		sql.append(" INNER JOIN matriculaperiodo 				ON matriculaperiodo.matricula   = matricula.matricula");
		sql.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" INNER JOIN historico 						ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
		sql.append(" INNER JOIN LATERAL ( ");
		sql.append(" SELECT t.codigo AS turmaauxiliar ");
		sql.append(" FROM turma t ");
		sql.append(" WHERE t.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" AND matriculaperiodoturmadisciplina.turmapratica is null   ");
        sql.append("  and matriculaperiodoturmadisciplina.turmateorica is null   ");
		sql.append("  UNION ");
		sql.append("  SELECT t.codigo as turmaauxiliar FROM turma t WHERE t.codigo = matriculaperiodoturmadisciplina.turmapratica  ");
		sql.append("  UNION ");
		sql.append("  SELECT t.turmaorigem as turmaauxiliar FROM turmaagrupada t WHERE t.turma = matriculaperiodoturmadisciplina.turmateorica  ");
		sql.append("  UNION ");
		sql.append("  SELECT t.turmaorigem as turmaauxiliar FROM turmaagrupada t WHERE t.turma = matriculaperiodoturmadisciplina.turmapratica ");
		sql.append(" UNION ");
		sql.append("  SELECT t.codigo as turmaauxiliar FROM turma t WHERE t.codigo = matriculaperiodoturmadisciplina.turmateorica  ");
		sql.append("  UNION ");
		sql.append("   SELECT t.turmaorigem as turmaauxiliar ");
		sql.append("   FROM turmaagrupada t ");
		sql.append("  where t.turma = matriculaperiodoturmadisciplina.turma ");
		sql.append("  AND matriculaperiodoturmadisciplina.turmapratica is null ");
		sql.append("  AND matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" ) AS tabelaauxiliarturma ON 1=1 ");
		sql.append(" INNER JOIN horarioturma ON horarioturma.turma = tabelaauxiliarturma.turmaauxiliar ");
		sql.append("   AND horarioturma.anovigente = matriculaperiodoturmadisciplina.ano ");
		sql.append("   AND horarioturma.semestrevigente = matriculaperiodoturmadisciplina.semestre");
		sql.append(" INNER JOIN turma 							ON horarioturma.turma 				   = turma.codigo");
		sql.append(" INNER JOIN horarioturmadia 				ON horarioturma.codigo 				   = horarioturmadia.horarioturma");
		sql.append(" INNER JOIN horarioturmadiaitem 			ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append(" and ((turma.turmaagrupada = false and horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina) ");
		sql.append(" or (turma.turmaagrupada and horarioturmadiaitem.disciplina in ( ");
		sql.append(" select d.codigo from disciplina as d where d.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" union");
		sql.append(" select d.disciplina from disciplinaequivalente as d where d.equivalente = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" union");
		sql.append(" select d.equivalente from disciplinaequivalente as d where d.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" )))");

		sql.append(" INNER JOIN disciplina ON disciplina.codigo = horarioturmadiaitem.disciplina");
		sql.append(" where ");
		sql.append(" matriculaperiodo.codigo = (");
		sql.append(" select mptd.matriculaperiodo from  matriculaperiodoturmadisciplina mptd where mptd.matriculaperiodo = matriculaperiodo.codigo ");
		if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
			sql.append(" and mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma in ( select turma from turmaagrupada where turmaorigem = ").append(turma.getCodigo()).append(")");
		} else if (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sql.append(" and (mptd.turmapratica = ").append(turma.getCodigo());
			sql.append(" or (mptd.turmapratica in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + turma.getCodigo() + "))))");
		} else if (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sql.append(" and (mptd.turmateorica = ").append(turma.getCodigo());
			sql.append(" or (mptd.turmateorica in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + turma.getCodigo() + "))))");
		} else {
			sql.append(" and mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma = ").append(turma.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			if(turma.getTurmaAgrupada() ){
				sql.append(" and (mptd.disciplina in ( ");
				sql.append(" select d.codigo from disciplina as d where d.codigo = ").append(disciplina);
				sql.append(" union");
				sql.append(" select d.disciplina from disciplinaequivalente as d where d.equivalente = ").append(disciplina);
				sql.append(" union");
				sql.append(" select d.equivalente from disciplinaequivalente as d where d.disciplina = ").append(disciplina);
				sql.append(" )) ");
			}else{
				sql.append(" and mptd.disciplina = ").append(disciplina);
			}
		}
		sql.append(" limit 1 )");
		sql.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'PR')");
		if(!turma.getIntegral()){
			sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
			if(turma.getSemestral()){
				sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
		}
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append("  group by matricula.matricula, pessoa.nome, horarioturmadia.data, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino ");
		sql.append("  having count(distinct disciplina.codigo||'[T]'||turma.codigo ) > 1  ");
		sql.append("  and  (((select unnest(array_agg(disciplina.codigo))) intersect (select unnest(array_agg(case when horarioturma.turma = ").append(turma.getCodigo()).append(" then disciplina.codigo else 0 end )))) limit 1) is not null ");
		sql.append("  ) as t group by matricula, aluno,  DATA, to_char (DATA,'dd/MM/YY' )|| horarioinicio, unnest(turmaDisciplinas) ");
		sql.append(" ) as t2  group by matricula, aluno order by aluno ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ChoqueHorarioAlunoVO> choqueHorarioAlunoVOs = new ArrayList<ChoqueHorarioAlunoVO>(0);
		ChoqueHorarioAlunoVO choqueHorarioAlunoVO = null;
		TurmaDisciplinaVO turmaDisciplinaVO = null;
		while (rs.next()) {
			choqueHorarioAlunoVO = new ChoqueHorarioAlunoVO();
			choqueHorarioAlunoVO.getMatricula().setMatricula(rs.getString("matricula"));
			choqueHorarioAlunoVO.getMatricula().getAluno().setNome(rs.getString("aluno"));
			choqueHorarioAlunoVO.setQtdeAulasComChoque(rs.getInt("qtdeAulaComChoque"));
			choqueHorarioAlunoVO.setQtdeDiaComChoque(rs.getInt("qtdeDiaComChoque"));
			String arrayTurmaDisciplinas = rs.getString("turmaDisciplinas");
			Map<String, String> turmaDis = new HashMap<String, String>(0);
			for(String item: arrayTurmaDisciplinas.split("<DISICIPLINA>")){
				String[] td = item.split("<TURMA>");
				if(!turmaDis.containsKey(td[1]+td[0])){
					turmaDisciplinaVO = new TurmaDisciplinaVO();
					turmaDisciplinaVO.getTurmaDescricaoVO().setIdentificadorTurma(td[1]);
					turmaDisciplinaVO.getDisciplina().setNome(td[0]);
					choqueHorarioAlunoVO.getTurmaDisciplinaVOs().add(turmaDisciplinaVO);
					turmaDis.put(td[1]+td[0], td[1]+td[0]);
				}
			}
			choqueHorarioAlunoVOs.add(choqueHorarioAlunoVO);
		}
		return choqueHorarioAlunoVOs;
	}

	@Override
	public List<ChoqueHorarioAlunoDetalheVO> consultarChoqueHorarioAlunoDetalhePorTurmaAnoSemestre(TurmaVO turma, String matricula, String ano, String semestre, Integer disciplina){
		StringBuilder sql = new StringBuilder("");
		sql.append(" select matricula.matricula, pessoa.nome as aluno,");
		sql.append(" horarioturmadia.data, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, ");
		sql.append(" array_to_string(array_agg(disciplina.nome||'<TURMA>'||turma.identificadorturma), '<DISCIPLINA>') as turmaDisciplinas");
		sql.append(" from matricula");
		sql.append(" INNER JOIN pessoa 							ON pessoa.codigo 			  = matricula.aluno");
		sql.append(" INNER JOIN matriculaperiodo 				ON matriculaperiodo.matricula = matricula.matricula");
		sql.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" INNER JOIN historico 						ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina	");
		sql.append(" INNER JOIN LATERAL (");
		sql.append(" SELECT t.codigo AS turmaauxiliar FROM turma t WHERE t.codigo = matriculaperiodoturmadisciplina.turma AND matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" UNION ");
		sql.append(" SELECT t.codigo AS turmaauxiliar FROM turma t WHERE t.codigo = matriculaperiodoturmadisciplina.turmapratica ");
		sql.append(" UNION ");
		sql.append(" SELECT t.codigo AS turmaauxiliar FROM turma t WHERE t.codigo = matriculaperiodoturmadisciplina.turmateorica ");
		sql.append(" UNION ");
		sql.append(" SELECT t.turmaorigem AS turmaauxiliar FROM turmaagrupada t WHERE t.turma = matriculaperiodoturmadisciplina.turma AND matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" ) AS tabelaauxiliarturma ON 1=1 ");
		sql.append(" INNER JOIN horarioturma                   ON horarioturma.turma = tabelaauxiliarturma.turmaauxiliar ");
		sql.append(" AND horarioturma.anovigente 	  = matriculaperiodoturmadisciplina.ano ");
		sql.append(" AND horarioturma.semestrevigente = matriculaperiodoturmadisciplina.semestre");

		sql.append(" INNER JOIN turma 							ON horarioturma.turma = turma.codigo");
		sql.append(" INNER JOIN horarioturmadia 				ON horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append(" INNER JOIN horarioturmadiaitem 			ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append(" and horarioturmadiaitem.disciplina in ( ");
		sql.append(" select d.codigo from disciplina as d where d.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" union");
		sql.append(" select d.disciplina from disciplinaequivalente as d where d.equivalente = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" union");
		sql.append(" select d.equivalente from disciplinaequivalente as d where d.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" )");
		sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
		sql.append(" where matricula.matricula = '").append(matricula).append("' ");
		sql.append(" and matriculaperiodo.codigo = (");
		sql.append(" select mptd.matriculaperiodo from  matriculaperiodoturmadisciplina mptd where mptd.matriculaperiodo = matriculaperiodo.codigo ");
		if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
			sql.append(" and mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma in ( select turma from turmaagrupada where turmaorigem = ").append(turma.getCodigo()).append(") ");
		} else if (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sql.append(" and (mptd.turmapratica = ").append(turma.getCodigo());
			sql.append(" or (mptd.turmapratica in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + turma.getCodigo() + "))))");
		} else if (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sql.append(" and (mptd.turmateorica = ").append(turma.getCodigo());
			sql.append(" or (mptd.turmateorica in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + turma.getCodigo() + "))))");
		} else {
			sql.append(" and mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma = ").append(turma.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			if(turma.getTurmaAgrupada() ){
				sql.append(" or (mptd.disciplina in ( ");
				sql.append(" select d.codigo from disciplina as d where d.codigo = ").append(disciplina);
				sql.append(" union");
				sql.append(" select d.disciplina from disciplinaequivalente as d where d.equivalente = ").append(disciplina);
				sql.append(" union");
				sql.append(" select d.equivalente from disciplinaequivalente as d where d.disciplina = ").append(disciplina);
				sql.append(" )) ");
			}else{
				sql.append(" and mptd.disciplina = ").append(disciplina);
			}
		}
		sql.append(" limit 1 )");

		sql.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'PR')");
		if(!turma.getIntegral()){
			sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
			if(turma.getSemestral()){
				sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
		}
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append("  group by matricula.matricula, pessoa.nome, horarioturmadia.data, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino ");
		sql.append("  having count(distinct disciplina.codigo||'[T]'||turma.codigo ) > 1 ");
		sql.append("  and  (((select unnest(array_agg(disciplina.codigo))) intersect (select unnest(array_agg(case when horarioturma.turma = ").append(turma.getCodigo()).append(" then disciplina.codigo else 0 end )))) limit 1) is not null ");
		sql.append("  order by matricula.matricula, horarioturmadia.data, horarioturmadiaitem.horarioinicio ");

		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ChoqueHorarioAlunoDetalheVO> choqueHorarioAlunoDetalheVOs = new ArrayList<ChoqueHorarioAlunoDetalheVO>(0);
		ChoqueHorarioAlunoDetalheVO choqueHorarioAlunoDetalheVO = null;
		TurmaDisciplinaVO turmaDisciplinaVO = null;
		while(rs.next()){
			choqueHorarioAlunoDetalheVO = new ChoqueHorarioAlunoDetalheVO();
			choqueHorarioAlunoDetalheVO.setData(rs.getDate("data"));
			choqueHorarioAlunoDetalheVO.setHorarioInicio(rs.getString("horarioinicio"));
			choqueHorarioAlunoDetalheVO.setHorarioTermino(rs.getString("horariotermino"));
			String arrayTurmaDisciplinas = rs.getString("turmaDisciplinas");
			for(String turmaDisciplina: arrayTurmaDisciplinas.split("<DISCIPLINA>")){
				String[] td = turmaDisciplina.split("<TURMA>");
				turmaDisciplinaVO = new TurmaDisciplinaVO();
				turmaDisciplinaVO.getTurmaDescricaoVO().setIdentificadorTurma(td[1]);
				turmaDisciplinaVO.getDisciplina().setNome(td[0]);
				choqueHorarioAlunoDetalheVO.getTurmaDisciplinaVOs().add(turmaDisciplinaVO);
			}
			choqueHorarioAlunoDetalheVOs.add(choqueHorarioAlunoDetalheVO);
		}
		return choqueHorarioAlunoDetalheVOs;
	}
	
	@Override
	public List<ChoqueHorarioAlunoVO> consultarChoqueHorarioCalendarioAluno(Integer turma, String ano, String semestre, Integer disciplina, Integer professor, Date dataBase, String horarioInicial, String horarioFinal){
		StringBuilder sql = new StringBuilder("");
		sql.append("select distinct t.matricula, disciplina.nome as disciplina, pessoa.nome as nomealuno from (  ");
		sql.append(" select matricula.matricula, matricula.aluno as codaluno, matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" from matriculaperiodoturmadisciplina ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo =  matriculaperiodoturmadisciplina.matriculaperiodo  ");
		sql.append(" inner join matricula on matriculaperiodo.matricula =  matricula.matricula ");
		sql.append(" inner join curso on curso.codigo =  matricula.curso ");
		sql.append(" inner join historico on historico.matricula =  matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" where exists ( ");
		sql.append("	select matriculaperiodo.codigo from matriculaperiodoturmadisciplina as mptd ");
		sql.append("	inner join matriculaperiodo as mp on mp.codigo =  mptd.matriculaperiodo  ");
		sql.append("	inner join matricula as m on mp.matricula =  m.matricula  ");
		sql.append("    inner join curso as c on c.codigo =  matricula.curso ");
		sql.append("	inner join historico as his on his.matricula =  m.matricula and his.matriculaperiodoturmadisciplina = mptd.codigo  ");
		sql.append("	where mptd.turma =  ").append(turma);
		sql.append("	and mptd.disciplina  = ").append(disciplina);
		sql.append("	and mptd.professor = ").append(professor);
		sql.append("    and ((c.periodicidade = 'IN' AND mptd.ano = '' AND mptd.semestre = '' ) OR ");
		sql.append("         (c.periodicidade = 'SE' AND mptd.ano = '").append(ano).append("' AND mptd.semestre = '").append(semestre).append("') OR ");
		sql.append("         (c.periodicidade = 'AN' AND mptd.ano = '").append(ano).append("' AND mptd.semestre = '') ");
		sql.append("       )");
		sql.append("	and matriculaperiodoturmadisciplina.matriculaperiodo = mp.codigo ");
		sql.append(" )  ");
		sql.append(" and ((curso.periodicidade = 'IN' AND matriculaperiodoturmadisciplina.ano = '' AND matriculaperiodoturmadisciplina.semestre = '' ) OR ");
		sql.append("         (curso.periodicidade = 'SE' AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') OR ");
		sql.append("         (curso.periodicidade = 'AN' AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' AND matriculaperiodoturmadisciplina.semestre = '') ");
		sql.append("       )");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append(" and exists ( ");
		sql.append("	/* turma base */");
		sql.append("	select horarioturma.codigo from horarioturma ");
		sql.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
		sql.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("	where horarioturma.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica  is null ");
		sql.append("	and horarioturma.anovigente  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and horarioturma.semestrevigente  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and horarioturmadiaitem.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoHorarioTurmaDiaItem(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/* turma patrica */ ");
		sql.append("	union all  ");
		sql.append("	select horarioturma.codigo from horarioturma ");
		sql.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
		sql.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("	where horarioturma.turma = matriculaperiodoturmadisciplina.turmapratica and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and horarioturma.anovigente  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and horarioturma.semestrevigente  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and horarioturmadiaitem.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoHorarioTurmaDiaItem(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/* turma teorica */");
		sql.append("	union all  ");
		sql.append("	select horarioturma.codigo from horarioturma ");
		sql.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
		sql.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("	where horarioturma.turma = matriculaperiodoturmadisciplina.turmateorica and matriculaperiodoturmadisciplina.turmateorica  is not null ");
		sql.append("	and horarioturma.anovigente  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and horarioturma.semestrevigente  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and horarioturmadiaitem.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoHorarioTurmaDiaItem(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/* turma agrupada */");
		sql.append("	union all  ");
		sql.append("	select horarioturma.codigo from horarioturma ");
		sql.append("	inner join turma on horarioturma.turma = turma.codigo ");
		sql.append("	inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sql.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("	where turma.turmaagrupada and turma.subturma =  false and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmateorica  is not null and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and horarioturma.anovigente  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and horarioturma.semestrevigente  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and horarioturmadiaitem.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoHorarioTurmaDiaItem(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/* turma agrupada com equivalencia na equivalente*/ ");
		sql.append("	union all  ");
		sql.append("	select horarioturma.codigo from horarioturma ");
		sql.append("	inner join turma on horarioturma.turma = turma.codigo ");
		sql.append("	inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sql.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("	inner join disciplinaequivalente on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina  ");
		sql.append("	where turma.turmaagrupada and turma.subturma =  false and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmateorica  is not null and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and horarioturma.anovigente  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and horarioturma.semestrevigente  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and disciplinaequivalente.equivalente  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoHorarioTurmaDiaItem(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/* turma agrupada com equivalencia na disciplina*/ ");
		sql.append("	union all  ");
		sql.append("	select horarioturma.codigo from horarioturma ");
		sql.append("	inner join turma on horarioturma.turma = turma.codigo ");
		sql.append("	inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sql.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append("	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("	inner join disciplinaequivalente on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina  ");
		sql.append("	where turma.turmaagrupada and turma.subturma =  false and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmateorica  is not null and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and horarioturma.anovigente  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and horarioturma.semestrevigente  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and disciplinaequivalente.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoHorarioTurmaDiaItem(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/*google meet turma base*/ ");
		sql.append("	union all ");
		sql.append("	select googlemeet.codigo from googlemeet ");
		sql.append("	where googlemeet.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica  is null ");
		sql.append("	and matriculaperiodoturmadisciplina.professor  = googlemeet.professor ");
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.ano  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and googlemeet.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and googlemeet.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoGoogleMeet(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/*google meet turma patrica*/ ");
		sql.append("	union all  ");
		sql.append("	select googlemeet.codigo from googlemeet ");
		sql.append("	where googlemeet.turma = matriculaperiodoturmadisciplina.turmapratica and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and matriculaperiodoturmadisciplina.professor  = googlemeet.professor ");
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.ano  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and googlemeet.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and googlemeet.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoGoogleMeet(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/*google meet turma teorica */");
		sql.append("	union all  ");
		sql.append("	select googlemeet.codigo from googlemeet ");
		sql.append("	where googlemeet.turma = matriculaperiodoturmadisciplina.turmateorica and matriculaperiodoturmadisciplina.turmateorica  is not null ");
		sql.append("	and matriculaperiodoturmadisciplina.professor  = googlemeet.professor ");
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.ano  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and googlemeet.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and googlemeet.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoGoogleMeet(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/*google meet turma agrupada */");
		sql.append("	union all  ");
		sql.append("	select googlemeet.codigo from googlemeet ");
		sql.append("	inner join turma on googlemeet.turma = turma.codigo ");
		sql.append("	inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sql.append("	where turma.turmaagrupada and turma.subturma =  false and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmateorica  is not null and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and matriculaperiodoturmadisciplina.professor  = googlemeet.professor ");
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.ano  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and googlemeet.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and googlemeet.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoGoogleMeet(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/*google meet turma agrupada com equivalencia na equivalente*/ ");
		sql.append("	union all  ");
		sql.append("	select googlemeet.codigo from googlemeet ");
		sql.append("	inner join turma on googlemeet.turma = turma.codigo ");
		sql.append("	inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sql.append("	inner join disciplinaequivalente on disciplinaequivalente.disciplina = googlemeet.disciplina  ");
		sql.append("	where turma.turmaagrupada and turma.subturma =  false and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmateorica  is not null and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and matriculaperiodoturmadisciplina.professor  = googlemeet.professor ");
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.ano  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and googlemeet.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and disciplinaequivalente.equivalente  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoGoogleMeet(sql, dataBase, horarioInicial, horarioFinal);
		sql.append("	/*google meet turma agrupada com equivalencia na disciplina */");
		sql.append("	union all  ");
		sql.append("	select googlemeet.codigo from googlemeet ");
		sql.append("	inner join turma on googlemeet.turma = turma.codigo ");
		sql.append("	inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sql.append("	inner join disciplinaequivalente on disciplinaequivalente.equivalente = googlemeet.disciplina  ");
		sql.append("	where turma.turmaagrupada and turma.subturma =  false and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma  ");
		sql.append("	and matriculaperiodoturmadisciplina.turmateorica  is not null and matriculaperiodoturmadisciplina.turmapratica  is not null ");
		sql.append("	and matriculaperiodoturmadisciplina.professor  = googlemeet.professor ");
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.ano  = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and googlemeet.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append("	and disciplinaequivalente.disciplina  = matriculaperiodoturmadisciplina.disciplina  ");
		adicionarFiltroPeriodoDoGoogleMeet(sql, dataBase, horarioInicial, horarioFinal);
		sql.append(" ) ");
		sql.append(" ) as t ");
		sql.append(" inner join disciplina on disciplina.codigo = t.disciplina ");
		sql.append(" inner join pessoa on pessoa.codigo = t.codaluno ");
		sql.append(" order by nomealuno ");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ChoqueHorarioAlunoVO> choqueHorarioAlunoVOs = new ArrayList<ChoqueHorarioAlunoVO>(0);
		while (rs.next()) {
			ChoqueHorarioAlunoVO choqueHorarioAlunoVO = consultarChoqueHorarioAluno(choqueHorarioAlunoVOs, rs.getString("matricula"));
			if(choqueHorarioAlunoVO.getMatricula().getMatricula().isEmpty()) {
				choqueHorarioAlunoVO.getMatricula().setMatricula(rs.getString("matricula"));
				choqueHorarioAlunoVO.getMatricula().getAluno().setNome(rs.getString("nomealuno"));				
			}
			TurmaDisciplinaVO td = new TurmaDisciplinaVO();
			td.getDisciplina().setNome(rs.getString("disciplina"));
			choqueHorarioAlunoVO.getTurmaDisciplinaVOs().add(td);
			adicionarChoqueHorarioAluno(choqueHorarioAlunoVOs, choqueHorarioAlunoVO);
		}
		return choqueHorarioAlunoVOs;
	}
	
	
	public ChoqueHorarioAlunoVO consultarChoqueHorarioAluno(List<ChoqueHorarioAlunoVO> choqueHorarioAlunoVOs, String matricula ) {
		Optional<ChoqueHorarioAlunoVO> findFirst = choqueHorarioAlunoVOs.stream().filter(p-> p.getMatricula().getMatricula().equals(matricula)).findFirst();
		if(findFirst.isPresent() && !findFirst.get().getMatricula().getMatricula().isEmpty()) {
			findFirst.get();
		}
		return new ChoqueHorarioAlunoVO();
	}
	
	public void adicionarChoqueHorarioAluno(List<ChoqueHorarioAlunoVO> choqueHorarioAlunoVOs, ChoqueHorarioAlunoVO obj ) {
		int index = 0;
		for (ChoqueHorarioAlunoVO objExistente : choqueHorarioAlunoVOs) {
			if(objExistente.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula())) {
				choqueHorarioAlunoVOs.set(index, obj);
				return;
			}
		}
		choqueHorarioAlunoVOs.add( obj);
	}

	private void adicionarFiltroPeriodoDoGoogleMeet(StringBuilder sql, Date dataBase, String horarioInicial, String horarioFinal) {
		sql.append("	and googlemeet.diaevento = '").append(Uteis.getDataJDBC(dataBase)).append("' ");
		sql.append("	and ((googlemeet.horarioinicio <= '").append(horarioInicial).append("' and googlemeet.horarioinicio   >= '").append(horarioFinal).append("') ");
		sql.append("	or (googlemeet.horariotermino   > '").append(horarioInicial).append("'	and googlemeet.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	or (googlemeet.horarioinicio   <= '").append(horarioInicial).append("'	and googlemeet.horariotermino >= '").append(horarioFinal).append("') ");
		sql.append("   	or (googlemeet.horarioinicio   >= '").append(horarioInicial).append("'	and googlemeet.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	) ");
	}

	private void adicionarFiltroPeriodoDoHorarioTurmaDiaItem(StringBuilder sql, Date dataBase, String horarioInicial, String horarioFinal) {
		sql.append("	and horarioturmadiaitem.\"data\" = '").append(Uteis.getDataJDBC(dataBase)).append("' ");
		sql.append("	and ((horarioturmadiaitem.horarioinicio <= '").append(horarioInicial).append("' and horarioturmadiaitem.horarioinicio   >= '").append(horarioFinal).append("') ");
		sql.append("	or (horarioturmadiaitem.horariotermino   > '").append(horarioInicial).append("'	and horarioturmadiaitem.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	or (horarioturmadiaitem.horarioinicio   <= '").append(horarioInicial).append("'	and horarioturmadiaitem.horariotermino >= '").append(horarioFinal).append("') ");
		sql.append("   	or (horarioturmadiaitem.horarioinicio   >= '").append(horarioInicial).append("'	and horarioturmadiaitem.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	) ");
	}
	
	@Override
    public CalendarioHorarioAulaVO<DataEventosRSVO> realizarGeracaoCalendarioAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, String matricula, UnidadeEnsinoVO unidadeEnsinoVO, MesAnoEnum mesAno, Integer ano, UsuarioVO usuarioVO) throws Exception {
    	CalendarioHorarioAulaVO<DataEventosRSVO> calendarioHorarioAula = new CalendarioHorarioAulaVO<DataEventosRSVO>();
    	calendarioHorarioAula.setAno(ano.toString());
    	calendarioHorarioAula.setMesAno(mesAno);
    	Date data = Uteis.getData("01/" + mesAno.getKey() + "/" + ano, "dd/MM/yyyy");
    	Date dataFim = Uteis.getDataUltimoDiaMes(data);
    	calendarioHorarioAula.executarMontagemSemanaInicialCalendarioHorarioAulaDataEventoRSVO(calendarioHorarioAula, data);    	
		List<DataEventosRSVO> listaDataEventosRSVOs = new ArrayList<>();
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			listaDataEventosRSVOs = getFacadeFactory().getHorarioAlunoFacade().consultarDatasMeusHorariosAlunoEspecificoAplicativo(matriculaPeriodoTurmaDisciplinaVOs, data, null, true, null, usuarioVO);
			listaDataEventosRSVOs.addAll(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosEADAplicativo(matriculaPeriodoTurmaDisciplinaVOs));
			listaDataEventosRSVOs.addAll(getFacadeFactory().getGoogleMeetInterfaceFacade().consultarCalendarioPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs));
			listaDataEventosRSVOs.addAll(getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodoPorDataEventos(data, dataFim, unidadeEnsinoVO.getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			 
		}
        DataEventosRSVO dataEventoRSVO = null;
        Integer ultimoDia = Uteis.getDiaMesData(dataFim);
        for (int dia = 1; dia <= ultimoDia; dia++) {
        	dataEventoRSVO = consultaDataEventoRSVOExistente(listaDataEventosRSVOs, dia, calendarioHorarioAula.getMesAno());
            if (dataEventoRSVO.getData() != null) {                 
                dataEventoRSVO.setData(Uteis.gerarDataDiaMesAno(dia, Integer.valueOf(mesAno.getKey()) , ano));
                if(dataEventoRSVO.getStyleClass().equals("horarioFeriado")) {
                	Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dataEventoRSVO.getData());
                    if(listaDataEventosRSVOs.stream().anyMatch(p-> p.getDia().equals(calendar.get(Calendar.DAY_OF_MONTH)) && p.getMes().equals(calendar.get(Calendar.MONTH)) && p.getAno().equals(calendar.get(Calendar.YEAR)) && p.getStyleClass().isEmpty())) {
                    	dataEventoRSVO.setStyleClass("horarioRegistroLancado");	
                    }
                }               
            } else {
            	dataEventoRSVO = calendarioHorarioAula.executarMontagemDataEventoRSVOLivre(dia, mesAno, ano);
            }            
            if(UteisData.getCompararDatas(dataEventoRSVO.getData(), new Date())) {
            	calendarioHorarioAula.setObjetoSelecionado(dataEventoRSVO);
            }
            calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(dataEventoRSVO, Uteis.getDiaSemanaEnum(dataEventoRSVO.getData()));
        }
        calendarioHorarioAula.executarMontagemSemanaFinalCalendarioHorarioAulaDataEventoRSVO(calendarioHorarioAula, dataFim);
    	return calendarioHorarioAula;
    }
	
	public DataEventosRSVO consultaDataEventoRSVOExistente(List<DataEventosRSVO> listaDataEventosRSVOs, int dia, MesAnoEnum mesAno) {
    	for (DataEventosRSVO obj : listaDataEventosRSVOs) {
    		if(MesAnoEnum.getMesData(obj.getData()).equals(mesAno) && obj.getDia().equals(dia)) {
    			return obj;
    		}
		}
    	return new DataEventosRSVO();
    }
	
	@Override
	public List<DataEventosRSVO> consultarDatasMeusHorariosAlunoEspecificoAplicativo(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, Integer turno, boolean visaoMensal, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			Date dataInicio = dataBase;
			Date dataTermino = null;
			if (dataBase != null) {
				if (visaoMensal) {
					dataInicio = Uteis.getDataPrimeiroDiaMes(dataBase);
					dataTermino = Uteis.getDataUltimoDiaMes(dataBase);
				} else {
					dataInicio = (UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(dataBase)), 1));
					dataTermino = (UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(dataBase), 1));
				}
			}
			sql.append(" select distinct horarioturmadia.data ");
			sql.append(" from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
			sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
			sql.append(" inner join turma on turma.codigo = horarioturma.turma");
			sql.append(" inner join turno on turno.codigo = turma.turno");
			sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
			if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() > 0) {
				sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
			}
			sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");

			sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
			sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
			sql.append(" left join salalocalaula sala on sala.codigo = horarioturmadiaitem.sala ");
			sql.append(" left join localaula on localaula.codigo = sala.localaula ");
			sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadia.data", false));
			if(!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().trim().isEmpty()){
				sql.append(" and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
				if(!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().trim().isEmpty()){
					sql.append(" and horarioturma.semestrevigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
				}
			}

			int x = 0;
			sql.append(" and ( ");
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				String turmaFiltrar = "";
				if(Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())){
					turmaFiltrar += mp.getTurmaPratica().getCodigo();
				}
				if(Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurmaTeorica().getCodigo();
				}
				if(!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurma().getCodigo();
				}
				if ((mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta())) {
					if (x > 0) {
						sql.append(" or ");
					}
					sql.append(" (((turma.codigo in (").append(turmaFiltrar).append("))  ");
					if (!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaFiltrar + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaPratica().getCodigo() + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaTeorica().getCodigo() + ")))");
					}
					sql.append(" ) ");
					sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo = ").append(mp.getDisciplina().getCodigo()).append(" ) ");
					sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
					sql.append(" select ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
					sql.append(" ))) ");
					if (!mp.isNovoObj()) {
						/**
						 * Adicionada regra para resolver impactos relacionados
						 * a transferencia de matriz curricular pois estava
						 * validando choque de horário com disciplinas da grade
						 * anterior
						 */
						sql.append(" and exists (");
						sql.append(" select disciplina from historico ");
						sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
						sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
						sql.append(" where matricula.matricula = '").append(mp.getMatricula()).append("' ");
						sql.append(" and historico.disciplina = ").append(mp.getDisciplina().getCodigo());
						sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
						sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
						sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().isEmpty()) {
							sql.append(" and historico.anohistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
						}
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().isEmpty()) {
							sql.append(" and historico.semestrehistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
						}
						sql.append(" ) ");
					}
					sql.append(" ) ");
					x++;
				}

			}
			sql.append(") ");
			if (turno != null && turno > 0) {
				sql.append(" and turno.codigo = ").append(turno);
			}
			sql.append(" and horarioturmadia.ocultardataaula = false ");
			sql.append(" order by data");
			//System.out.println(sql);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<DataEventosRSVO> dataEventosRSVOs = new ArrayList<>();
			DataEventosRSVO dataEventosRSVO = null;
			while (rs.next()) {
				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("data"));
				dataEventosRSVO = new DataEventosRSVO();
				dataEventosRSVO.setAno(data.get(Calendar.YEAR));
				dataEventosRSVO.setMes(data.get(Calendar.MONTH));
				dataEventosRSVO.setDia(data.get(Calendar.DAY_OF_MONTH));
				dataEventosRSVO.setData(rs.getDate("data"));
				dataEventosRSVO.setColor("#b8b8b8");
				dataEventosRSVO.setTextColor("#000000");
				dataEventosRSVO.setStyleClass("horarioRegistroLancado");
				dataEventosRSVOs.add(dataEventosRSVO);
			}
			return dataEventosRSVOs;
		}
		return new ArrayList<DataEventosRSVO>(0);
	}

	@Override
	public List<AgendaAlunoRSVO> consultarAgendaAlunoDia(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		List<AgendaAlunoRSVO> agendaAlunoRSVOs = new ArrayList<AgendaAlunoRSVO>(0);
		agendaAlunoRSVOs.addAll(consultarAgendaHorarioAlunoDiaItem(matriculaPeriodoTurmaDisciplinaVOs, dataBase, usuario));
		agendaAlunoRSVOs.addAll(consultarAgendaFeriadoDiaItem(dataBase, unidadeEnsinoVO, usuario));
		agendaAlunoRSVOs.addAll(consultarAgendaCalendariAlunoEad(matriculaPeriodoTurmaDisciplinaVOs, dataBase, usuario));
		Ordenacao.ordenarLista(agendaAlunoRSVOs, "dataOrdenacao");
		return agendaAlunoRSVOs;
	}

	@Override
	public List<AgendaAlunoRSVO> consultarAgendaFeriadoDiaItem(Date dataBase, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(dataBase, dataBase, unidadeEnsinoVO.getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		List<AgendaAlunoRSVO> agendaAlunoRSVOs = new ArrayList<AgendaAlunoRSVO>(0);
		AgendaAlunoRSVO agendaAlunoRSVO = null;
		for (FeriadoVO feriadoVO : feriadoVOs) {
			agendaAlunoRSVO = new AgendaAlunoRSVO();
			agendaAlunoRSVO.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.FERIADO);
			agendaAlunoRSVO.setFeriadoVO(feriadoVO);
			agendaAlunoRSVOs.add(agendaAlunoRSVO);
		}
		return agendaAlunoRSVOs;
	}

	@Override
	public List<AgendaAlunoRSVO> consultarAgendaCalendariAlunoEad(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, UsuarioVO usuario) throws Exception {
		List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosDoDiaPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs, dataBase);		
		List<AgendaAlunoRSVO> agendaAlunoRSVOs = new ArrayList<AgendaAlunoRSVO>(0);
		for (CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO : calendarioAtividadeMatriculaVOs) {
			AgendaAlunoRSVO agendaAlunoRSVO = new AgendaAlunoRSVO();
			agendaAlunoRSVO.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.CALENDARIO_EAD);
			agendaAlunoRSVO.setCalendarioAtividadeMatriculaVO(calendarioAtividadeMatriculaVO);
			agendaAlunoRSVOs.add(agendaAlunoRSVO);
		}
		List<GoogleMeetVO> listaGoogleMeet = getFacadeFactory().getGoogleMeetInterfaceFacade().consultarPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs, dataBase);
    	for (GoogleMeetVO googleMeet : listaGoogleMeet) {
    		AgendaAlunoRSVO agenda = new AgendaAlunoRSVO();
    		agenda.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.CALENDARIO_EAD);
    		agenda.setGoogleMeetVO(googleMeet);
    		agenda.getGoogleMeetVO().setGoogleMeetAvulso(true);
    		agendaAlunoRSVOs.add(agenda);
    	}
		return agendaAlunoRSVOs;
	}

	@Override
	public List<AgendaAlunoRSVO> consultarAgendaHorarioAlunoDiaItem(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			Date dataInicio = dataBase;
			Date dataTermino = dataBase;
			sql.append(" select horarioturma.turma as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
			sql.append(" horarioturmadia.data,  horarioturmadiaitem.disciplina as \"disciplina.codigo\", horarioturmadiaitem.professor as \"pessoa.codigo\", ");
			sql.append(" horarioturmadiaitem.nraula, horarioturmadiaitem.duracaoaula, horarioturmadia.ocultardataaula, turno.naoApresentarHorarioVisaoAluno, ");
			sql.append(" horarioturmadiaitem.horario as horario_aula, horarioturmadiaitem.horarioinicio as horarioinicioaula,  horarioturmadiaitem.horariotermino as horariofinalaula, to_char(horarioturmadia.data, 'D')::INT as diasemana, turno.codigo as \"turno.codigo\", ");
			sql.append(" professor.nome as \"pessoa.nome\",  ");
			sql.append(" googlemeet.codigo as \"googlemeet.codigo\",");
			sql.append(" googlemeet.ideventocalendar as \"googlemeet.ideventocalendar\",");
			sql.append(" googlemeet.linkGoogleMeet as \"googlemeet.linkGoogleMeet\",");
			sql.append(" googlemeet.diaEvento as \"googlemeet.diaEvento\",");
			sql.append(" googlemeet.horarioInicio as \"googlemeet.horarioInicio\",");
			sql.append(" googlemeet.horarioTermino as \"googlemeet.horarioTermino\",");
			sql.append(" classroomGoogle.codigo as \"classroomGoogle.codigo\",  classroomGoogle.linkClassroom as \"classroomGoogle.linkClassroom\", ");
			sql.append(" classroomGoogle.idClassRoomGoogle as \"classroomGoogle.idClassRoomGoogle\",  classroomGoogle.idCalendario as \"classroomGoogle.idCalendario\", ");
			sql.append(" classroomGoogle.professorEad as \"classroomGoogle.professorEad\",   ");
			
			sql.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\",  salaaulablackboard.linkSalaAulaBlackboard as \"salaaulablackboard.linkSalaAulaBlackboard\", ");
			sql.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\",  salaaulablackboard.id as \"salaaulablackboard.id\", ");
			sql.append(" salaaulablackboard.programacaoTutoriaOnline as \"salaaulablackboard.programacaoTutoriaOnline\",   ");
			
			sql.append(" disciplina.nome as \"disciplina.nome\",");
			sql.append(" turno.nome as \"turno.nome\", configuracaogeralsistema.naoApresentarProfessorVisaoAluno, ");
			sql.append(" sala.codigo as \"sala.codigo\", sala.sala as \"sala.sala\", localaula.local as \"localaula.local\" ");
			sql.append(" from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
			sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
			sql.append(" inner join turma on turma.codigo = horarioturma.turma");
			sql.append(" inner join turno on turno.codigo = turma.turno");
			sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
			sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");

			sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
			sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
			sql.append(" left join salalocalaula sala on sala.codigo = horarioturmadiaitem.sala ");
			sql.append(" left join localaula on localaula.codigo = sala.localaula ");
			sql.append(" left join googlemeet on googlemeet.codigo = horarioturmadiaitem.googlemeet ");
			sql.append(" left join classroomgoogle  on ((classroomGoogle.codigo = googlemeet.classroomGoogle) or "); 
			sql.append(" (classroomGoogle.ano  = horarioturma.anovigente  and classroomGoogle.semestre  = horarioturma.semestrevigente ");  
			sql.append(" and classroomGoogle.disciplina = horarioturmadiaitem.disciplina and classroomGoogle.turma  = horarioturma.turma )) ");
			sql.append(" left join salaaulablackboard  on ( salaaulablackboard.ano  = horarioturma.anovigente  and salaaulablackboard.semestre  = horarioturma.semestrevigente ");  
			sql.append(" and salaaulablackboard.disciplina = horarioturmadiaitem.disciplina and salaaulablackboard.turma  = horarioturma.turma ) ");
			sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadia.data", false));
			if(!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().trim().isEmpty()){
				sql.append(" and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
				if(!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().trim().isEmpty()){
					sql.append(" and horarioturma.semestrevigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
				}
			}
			int x = 0;
			sql.append(" and ( ");
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				String turmaFiltrar = "";
				if(Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())){
					turmaFiltrar += mp.getTurmaPratica().getCodigo();
				}
				if(Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurmaTeorica().getCodigo();
				}
				if(!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurma().getCodigo();
				}
				if ((mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta())) {
					if (x > 0) {
						sql.append(" or ");
					}
					sql.append(" (((turma.codigo in (").append(turmaFiltrar).append("))  ");
					if (!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaFiltrar + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaPratica().getCodigo() + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaTeorica().getCodigo() + ")))");
					}
					sql.append(" ) ");
					sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo = ").append(mp.getDisciplina().getCodigo()).append(" ) ");
					sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
					sql.append(" select ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
					sql.append(" ))) ");
					if (!mp.isNovoObj()) {
						/**
						 * Adicionada regra para resolver impactos relacionados
						 * a transferencia de matriz curricular pois estava
						 * validando choque de horário com disciplinas da grade
						 * anterior
						 */
						sql.append(" and exists (");
						sql.append(" select disciplina from historico ");
						sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
						sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
						sql.append(" where matricula.matricula = '").append(mp.getMatricula()).append("' ");
						sql.append(" and historico.disciplina = ").append(mp.getDisciplina().getCodigo());
						sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
						sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
						sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().isEmpty()) {
							sql.append(" and historico.anohistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
						}
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().isEmpty()) {
							sql.append(" and historico.semestrehistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
						}
						sql.append(" ) ");
					}
					sql.append(" ) ");
					x++;
				}

			}
			sql.append(") ");
			sql.append(" and horarioturmadia.ocultardataaula = false ");
			sql.append(" order by data, nrAula");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<AgendaAlunoRSVO> agendaAlunoRSVOs = new ArrayList<AgendaAlunoRSVO>(0);
			AgendaAlunoRSVO agendaAlunoRSVO = null;
				while (rs.next()) {
					agendaAlunoRSVO = new AgendaAlunoRSVO();
					agendaAlunoRSVO.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.HORARIO_AULA);
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().setNrAula(rs.getInt("nraula"));
					if(rs.getBoolean("naoApresentarHorarioVisaoAluno")){
						agendaAlunoRSVO.getHorarioAlunoDiaItemVO().setHorario("");
					}else{
						agendaAlunoRSVO.getHorarioAlunoDiaItemVO().setHorario(rs.getString("horario_aula"));
						agendaAlunoRSVO.getHorarioAlunoDiaItemVO().setHorarioInicio(rs.getString("horarioinicioaula"));
						agendaAlunoRSVO.getHorarioAlunoDiaItemVO().setHorarioTermino(rs.getString("horariofinalaula"));
					}
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getTurma().setCodigo(rs.getInt("turma.codigo"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getProfessor().setCodigo(rs.getInt("pessoa.codigo"));
					if(rs.getBoolean("naoApresentarProfessorVisaoAluno")){
						agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getProfessor().setNome("");
					}else{
						agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getProfessor().setNome(rs.getString("pessoa.nome"));
					}
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getSala().setCodigo(rs.getInt("sala.codigo"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getSala().setSala(rs.getString("sala.sala"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().getSala().getLocalAula().setLocal(rs.getString("localaula.local"));
					agendaAlunoRSVO.getHorarioAlunoDiaItemVO().setData(rs.getDate("data"));
					agendaAlunoRSVO.getGoogleMeetVO().setCodigo(rs.getInt("googlemeet.codigo"));
					agendaAlunoRSVO.getGoogleMeetVO().setIdEventoCalendar(rs.getString("googlemeet.ideventocalendar"));
					agendaAlunoRSVO.getGoogleMeetVO().setLinkGoogleMeet(rs.getString("googlemeet.linkGoogleMeet"));
					agendaAlunoRSVO.getGoogleMeetVO().setDiaEvento(rs.getDate("googlemeet.diaEvento"));
					agendaAlunoRSVO.getGoogleMeetVO().setHorarioInicio(rs.getString("googlemeet.horarioInicio"));
					agendaAlunoRSVO.getGoogleMeetVO().setHorarioTermino(rs.getString("googlemeet.horarioTermino"));
					if(Uteis.isAtributoPreenchido(rs.getInt("classroomGoogle.codigo"))) {
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().setCodigo((rs.getInt("classroomGoogle.codigo")));						
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().setLinkClassroom(rs.getString("classroomGoogle.linkClassroom"));
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().setIdClassRoomGoogle(rs.getString("classroomGoogle.idClassRoomGoogle"));
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().setIdCalendario(rs.getString("classroomGoogle.idCalendario"));
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().getProfessorEad().setCodigo(rs.getInt("classroomGoogle.professorEad"));
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().setTurmaVO(agendaAlunoRSVO.getGoogleMeetVO().getTurmaVO());
						agendaAlunoRSVO.getGoogleMeetVO().getClassroomGoogleVO().setDisciplinaVO(agendaAlunoRSVO.getGoogleMeetVO().getDisciplinaVO());
					}
					if(Uteis.isAtributoPreenchido(rs.getInt("salaaulablackboard.codigo"))) {
						agendaAlunoRSVO.getSalaAulaBlackboardVO().setCodigo((rs.getInt("salaaulablackboard.codigo")));						
						agendaAlunoRSVO.getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard(rs.getString("salaaulablackboard.linkSalaAulaBlackboard"));
						agendaAlunoRSVO.getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(rs.getString("salaaulablackboard.idSalaAulaBlackboard"));
						agendaAlunoRSVO.getSalaAulaBlackboardVO().setId(rs.getString("salaaulablackboard.id"));
						agendaAlunoRSVO.getSalaAulaBlackboardVO().getProgramacaoTutoriaOnlineVO().setCodigo(rs.getInt("salaaulablackboard.programacaoTutoriaOnline"));
						agendaAlunoRSVO.getSalaAulaBlackboardVO().setTurmaVO(agendaAlunoRSVO.getGoogleMeetVO().getTurmaVO());
						agendaAlunoRSVO.getSalaAulaBlackboardVO().setDisciplinaVO(agendaAlunoRSVO.getGoogleMeetVO().getDisciplinaVO());
					}
					
					agendaAlunoRSVOs.add(agendaAlunoRSVO);
				}

			return agendaAlunoRSVOs;
		}
		return new ArrayList<AgendaAlunoRSVO>(0);
	}


	@Override
	public List<CronogramaDeAulasRelVO> criarObjetoRelatorio(List<HorarioAlunoTurnoVO> lista, UsuarioVO usuarioVO) {
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			HorarioAlunoTurnoVO horario = (HorarioAlunoTurnoVO)i.next();
			List<HorarioAlunoTurnoVO> horarioSemana = new ArrayList<HorarioAlunoTurnoVO>(horario.getHorarioAlunoTurnoSemanaVOs().values());
			Iterator o = horarioSemana.iterator();
			while (o.hasNext()) {
				HorarioAlunoTurnoVO h = (HorarioAlunoTurnoVO)o.next();

				Iterator j = h.getHorarioAlunoTurnoNumeroAulaVOs().iterator();
				while (j.hasNext()) {
					HorarioAlunoTurnoNumeroAulaVO horarioAlunoTurnoNumeroAulaVO = (HorarioAlunoTurnoNumeroAulaVO)j.next();

					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorDomingoApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaDomingoApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaDomingoApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaDomingoApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioDomingo());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoDomingo());
						}
						cronograma.setData(h.getDataDomingo());
						cronograma.setDataInicio(h.getDataDomingo());
						cronograma.setModulo("0Domingo");
						listaObjetos.add(cronograma);
					}
					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorSegundaApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaSegundaApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaSegundaApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaSegundaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioSegunda());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoSegunda());
						}
						cronograma.setData(h.getDataSegunda());
						cronograma.setDataInicio(h.getDataSegunda());
						cronograma.setModulo("1Segunda");
						listaObjetos.add(cronograma);
					}
					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaTercaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorTercaApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaTercaApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaTercaApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaTercaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioTerca());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoTerca());
						}
						cronograma.setDataInicio(h.getDataTerca());
						cronograma.setModulo("2Terça");
						listaObjetos.add(cronograma);
					}
					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorQuartaApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuartaApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaQuartaApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaQuartaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioQuarta());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoQuarta());
						}
						cronograma.setData(h.getDataQuarta());
						cronograma.setDataInicio(h.getDataQuarta());
						cronograma.setModulo("3Quarta");
						listaObjetos.add(cronograma);
					}
					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorQuintaApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaQuintaApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaQuintaApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaQuintaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioQuinta());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoQuinta());
						}
						cronograma.setData(h.getDataQuinta());
						cronograma.setDataInicio(h.getDataQuinta());
						cronograma.setModulo("4Quinta");
						listaObjetos.add(cronograma);
					}
					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaSextaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorSextaApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaSextaApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaSextaApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaSextaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioSexta());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoSexta());
						}
						cronograma.setData(h.getDataSexta());
						cronograma.setDataInicio(h.getDataSexta());
						cronograma.setModulo("5Sexta");
						listaObjetos.add(cronograma);
					}
					if (!horarioAlunoTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioAlunoTurnoNumeroAulaVO.getProfessorSabadoApresentar());
						cronograma.setDisciplina(horarioAlunoTurnoNumeroAulaVO.getDisciplinaSabadoApresentar());
						cronograma.setTurma(horarioAlunoTurnoNumeroAulaVO.getTurmaSabadoApresentar());
						cronograma.setLocal(horarioAlunoTurnoNumeroAulaVO.getSalaSabadoApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioAlunoTurnoNumeroAulaVO.getHorarioInicioSabado());
							cronograma.setHorarioTermino(horarioAlunoTurnoNumeroAulaVO.getHorarioTerminoSabado());
						}
						cronograma.setData(h.getDataSabado());
						cronograma.setDataInicio(h.getDataSabado());
						cronograma.setModulo("6Sábado");
						listaObjetos.add(cronograma);
					}
				}
			}
		}
		return listaObjetos;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HorarioAulaAlunoRel.jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	@Override
	public List<CronogramaDeAulasRelVO> realizarCriacaoHorarioAlunoModeloMatricula(List<HorarioAlunoTurnoVO> lista){
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		for(HorarioAlunoTurnoVO horarioAlunoTurnoVO :lista){
			for(HorarioAlunoTurnoNumeroAulaVO aula: horarioAlunoTurnoVO.getHorarioAlunoTurnoNumeroAulaVOs()){
					if (!aula.getProfessorDomingoApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorDomingoApresentar());
						cronograma.setDisciplina(aula.getDisciplinaDomingoApresentar());
						cronograma.setTurma(aula.getTurmaDomingoApresentar());
						cronograma.setLocal(aula.getSalaDomingoApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioDomingo());
						cronograma.setHorarioTermino(aula.getHorarioTerminoDomingo());
						cronograma.setModulo("0Domingo");
						listaObjetos.add(cronograma);
					}
					if (!aula.getProfessorSegundaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorSegundaApresentar());
						cronograma.setDisciplina(aula.getDisciplinaSegundaApresentar());
						cronograma.setTurma(aula.getTurmaSegundaApresentar());
						cronograma.setLocal(aula.getSalaSegundaApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioSegunda());
						cronograma.setHorarioTermino(aula.getHorarioTerminoSegunda());

						cronograma.setModulo("1Segunda");
						listaObjetos.add(cronograma);
					}
					if (!aula.getProfessorTercaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorTercaApresentar());
						cronograma.setDisciplina(aula.getDisciplinaTercaApresentar());
						cronograma.setTurma(aula.getTurmaTercaApresentar());
						cronograma.setLocal(aula.getSalaTercaApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioTerca());
						cronograma.setHorarioTermino(aula.getHorarioTerminoTerca());

						cronograma.setModulo("2Terça");
						listaObjetos.add(cronograma);
					}
					if (!aula.getProfessorQuartaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorQuartaApresentar());
						cronograma.setDisciplina(aula.getDisciplinaQuartaApresentar());
						cronograma.setTurma(aula.getTurmaQuartaApresentar());
						cronograma.setLocal(aula.getSalaQuartaApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioQuarta());
						cronograma.setHorarioTermino(aula.getHorarioTerminoQuarta());

						cronograma.setModulo("3Quarta");
						listaObjetos.add(cronograma);
					}
					if (!aula.getProfessorQuintaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorQuintaApresentar());
						cronograma.setDisciplina(aula.getDisciplinaQuintaApresentar());
						cronograma.setTurma(aula.getTurmaQuintaApresentar());
						cronograma.setLocal(aula.getSalaQuintaApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioQuinta());
						cronograma.setHorarioTermino(aula.getHorarioTerminoQuinta());

						cronograma.setModulo("4Quinta");
						listaObjetos.add(cronograma);
					}
					if (!aula.getProfessorSextaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorSextaApresentar());
						cronograma.setDisciplina(aula.getDisciplinaSextaApresentar());
						cronograma.setTurma(aula.getTurmaSextaApresentar());
						cronograma.setLocal(aula.getSalaSextaApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioSexta());
						cronograma.setHorarioTermino(aula.getHorarioTerminoSexta());

						cronograma.setModulo("5Sexta");
						listaObjetos.add(cronograma);
					}
					if (!aula.getProfessorSabadoApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(aula.getProfessorSabadoApresentar());
						cronograma.setDisciplina(aula.getDisciplinaSabadoApresentar());
						cronograma.setTurma(aula.getTurmaSabadoApresentar());
						cronograma.setLocal(aula.getSalaSabadoApresentar());
						cronograma.setHorarioInicio(aula.getHorarioInicioSabado());
						cronograma.setHorarioTermino(aula.getHorarioTerminoSabado());

						cronograma.setModulo("6Sábado");
						listaObjetos.add(cronograma);
					}

			}
		}
		return listaObjetos;
	}


	public List<HorarioAlunoTurnoVO> montarDadosHorarioAlunoPorMatriculaPeriodoDisciplinaCursoIntegralVOs(SqlRowSet rs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario, boolean visaoMensal) throws Exception {
		Map<String, HorarioAlunoTurnoVO> horarioAlunoTurnoVOs = new HashMap<String, HorarioAlunoTurnoVO>(0);
		HorarioAlunoTurnoVO horarioAlunoTurnoVO = null;
		while (rs.next()) {
			String key = "" + rs.getInt("turno.codigo");
			if (!horarioAlunoTurnoVOs.containsKey(key)) {
				horarioAlunoTurnoVO = realizarInicializarHorarioAlunoTurno(rs.getInt("turno.codigo"), usuario);
				horarioAlunoTurnoVO.setDataInicio(rs.getDate("data"));
				horarioAlunoTurnoVO.setHorarioIntegral(true);
				horarioAlunoTurnoVOs.put(key, horarioAlunoTurnoVO);
			} else {
				horarioAlunoTurnoVO = horarioAlunoTurnoVOs.get(key);
				horarioAlunoTurnoVO.setDataTermino(rs.getDate("data"));
			}

			horarioAlunoTurnoVO.setNaoApresentarProfessorVisaoAluno(rs.getBoolean("naoApresentarProfessorVisaoAluno"));
			HorarioAlunoDiaItemVO horarioAlunoDiaItemVO = horarioAlunoTurnoVO.obterHorarioAlunoDiaItemPorDataNrAula(rs.getDate("data"), rs.getInt("nrAula"));
			if (horarioAlunoDiaItemVO.getDisciplinaLivre()) {
				horarioAlunoDiaItemVO.setData(rs.getDate("data"));
				horarioAlunoDiaItemVO.setNrAula(rs.getInt("nrAula"));
				horarioAlunoDiaItemVO.getTurma().setCodigo(rs.getInt("turma.codigo"));
				horarioAlunoDiaItemVO.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
				horarioAlunoDiaItemVO.getProfessor().setCodigo(rs.getInt("pessoa.codigo"));
				horarioAlunoDiaItemVO.getProfessor().setNome(rs.getString("pessoa.nome"));
				horarioAlunoDiaItemVO.getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
				horarioAlunoDiaItemVO.getDisciplina().setNome(rs.getString("disciplina.nome"));
				horarioAlunoDiaItemVO.getSala().setSala(rs.getString("sala"));
				horarioAlunoDiaItemVO.getSala().getLocalAula().setLocal(rs.getString("local"));
				horarioAlunoDiaItemVO.setHorario(rs.getString("horarioinicioaula")+" à "+rs.getString("horariofinalaula"));
				horarioAlunoDiaItemVO.setHorarioInicio(rs.getString("horarioinicioaula"));
				horarioAlunoDiaItemVO.setHorarioTermino(rs.getString("horariofinalaula"));
				horarioAlunoDiaItemVO.setDisciplinaIncluida(true);
				horarioAlunoTurnoVO.getHorarioAlunoDiaItemVOs().add(horarioAlunoDiaItemVO);
			} else {
				HorarioAlunoDiaItemVO horarioAlunoDiaItemVO2 = new HorarioAlunoDiaItemVO();
				horarioAlunoDiaItemVO2.setData(rs.getDate("data"));
				horarioAlunoDiaItemVO2.setNrAula(rs.getInt("nrAula"));
				horarioAlunoDiaItemVO2.getTurma().setCodigo(rs.getInt("turma.codigo"));
				horarioAlunoDiaItemVO2.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
				horarioAlunoDiaItemVO2.getProfessor().setCodigo(rs.getInt("pessoa.codigo"));
				horarioAlunoDiaItemVO2.getProfessor().setNome(rs.getString("pessoa.nome"));
				horarioAlunoDiaItemVO2.getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
				horarioAlunoDiaItemVO2.getDisciplina().setNome(rs.getString("disciplina.nome"));
				horarioAlunoDiaItemVO2.getSala().setSala(rs.getString("sala"));
				horarioAlunoDiaItemVO2.getSala().getLocalAula().setLocal(rs.getString("local"));
				horarioAlunoDiaItemVO2.setDisciplinaIncluida(true);
				horarioAlunoDiaItemVO.getHorarioAlunoDiaItemVOs().add(horarioAlunoDiaItemVO2);
			}
		}
		List<HorarioAlunoTurnoVO> horarioAlunoTurnoList = new ArrayList<HorarioAlunoTurnoVO>(horarioAlunoTurnoVOs.values());
		Ordenacao.ordenarLista(horarioAlunoTurnoList, "dataInicio");
		for(HorarioAlunoTurnoVO HorarioAlunoTurnoVO: horarioAlunoTurnoList) {
			Ordenacao.ordenarLista(HorarioAlunoTurnoVO.getHorarioAlunoDiaItemVOs(), "data");
		}
		return horarioAlunoTurnoList;
	}

	@Override
	public List<HorarioAlunoTurnoVO> consultarMeusHorariosDisciplinaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, boolean validarAulaNaoRegistrada) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {

			sql.append(" select dense_rank() over(order by datainicio) as nrModulo, * ");
			sql.append(" from ( ");
			sql.append(" select distinct ");
			sql.append(" ( select min(htd.\"data\") from horarioturmadiaitem htdi ");
			sql.append(" inner join horarioturmadia htd on htd.codigo =  htdi.horarioturmadia ");
			sql.append(" inner join horarioturma ht on ht.codigo = htd.horarioturma ");
			sql.append(" where ht.codigo = horarioturma.codigo ");
			sql.append(" and htdi.disciplina = horarioturmadiaitem.disciplina ");
			sql.append(" ) as dataInicio, ");
			sql.append(" ( select max(htd.\"data\") from horarioturmadiaitem htdi ");
			sql.append(" inner join horarioturmadia htd on htd.codigo =  htdi.horarioturmadia ");
			sql.append(" inner join horarioturma ht on ht.codigo = htd.horarioturma ");
			sql.append(" where ht.codigo = horarioturma.codigo ");
			sql.append(" and htdi.disciplina = horarioturmadiaitem.disciplina ");
			sql.append(" ) as dataFim, ");
			sql.append(" horarioturma.turma as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
			sql.append(" horarioturmadiaitem.disciplina as \"disciplina_codigo\",");
			sql.append(" disciplina.nome as \"disciplina.nome\"");
			sql.append(" from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
			sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
			sql.append(" inner join turma on turma.codigo = horarioturma.turma");
			sql.append(" inner join turno on turno.codigo = turma.turno");
			sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
			if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() > 0) {
				sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
			}
			sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");

			sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
			sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
			sql.append(" left join salalocalaula sala on sala.codigo = horarioturmadiaitem.sala ");
			sql.append(" left join localaula on localaula.codigo = sala.localaula ");
			sql.append(" where 1 = 1 ");
			sql.append(" and ((turma.anual = false and turma.semestral = false) ");
			sql.append(" or (turma.anual and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("') ");
			sql.append(" or (turma.semestral and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
			sql.append(" and horarioturma.semestrevigente = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("')) ");

			int x = 0;
			sql.append(" and ( ");
			for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
				String turmaFiltrar = "";
				if(Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())){
					turmaFiltrar += mp.getTurmaPratica().getCodigo();
				}
				if(Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurmaTeorica().getCodigo();
				}
				if(!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())){
					turmaFiltrar += (!turmaFiltrar.isEmpty()?",":"")+ mp.getTurma().getCodigo();
				}
				if ((mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta())) {
					if (x > 0) {
						sql.append(" or ");
					}
					sql.append(" (((turma.codigo in (").append(turmaFiltrar).append("))  ");
					if (!Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaFiltrar + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaPratica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaPratica().getCodigo() + ")))");
					}
					if (Uteis.isAtributoPreenchido(mp.getTurmaTeorica().getCodigo())) {
						sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where t.subturma = false and TurmaAgrupada.turma in (" + mp.getTurmaTeorica().getCodigo() + ")))");
					}
					sql.append(" ) ");
					sql.append(" and ((turma.turmaagrupada = false and disciplina.codigo = ").append(mp.getDisciplina().getCodigo()).append(" ) ");
					sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
					sql.append(" select ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
					sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
					sql.append(" ))) ");
					if (!mp.isNovoObj()) {
						/**
						 * Adicionada regra para resolver impactos relacionados
						 * a transferencia de matriz curricular pois estava
						 * validando choque de horário com disciplinas da grade
						 * anterior
						 */
						sql.append(" and exists (");
						sql.append(" select disciplina from historico ");
						sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
						sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
						sql.append(" where matricula.matricula = '").append(mp.getMatricula()).append("' ");
						sql.append(" and historico.disciplina = ").append(mp.getDisciplina().getCodigo());
						sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
						sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
						sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().isEmpty()) {
							sql.append(" and historico.anohistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
						}
						if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().isEmpty()) {
							sql.append(" and historico.semestrehistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
						}
					}
					sql.append(" )) ");
					x++;
				}

			}
			sql.append(") ");
			sql.append(" and horarioturmadia.ocultardataaula = false ");


			sql.append(" ) as t ");
			sql.append(" order by datainicio, disciplina_codigo ");

			SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

			List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);


			while(rs.next()){
				HorarioAlunoTurnoVO horarioAlunoTurnoVO = new HorarioAlunoTurnoVO();

				horarioAlunoTurnoVO.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorturma"));
				horarioAlunoTurnoVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
				horarioAlunoTurnoVO.setDataInicioModulo(rs.getDate("dataInicio"));
				horarioAlunoTurnoVO.setDataFimModulo(rs.getDate("dataFim"));
				horarioAlunoTurnoVO.setNumeroModulo(rs.getInt("nrModulo"));

				horarioAlunoTurnoVOs.add(horarioAlunoTurnoVO);
			}
			return horarioAlunoTurnoVOs;

		}
		return new ArrayList<HorarioAlunoTurnoVO>(0);
	}
}
