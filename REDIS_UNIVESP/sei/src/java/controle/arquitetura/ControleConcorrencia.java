package controle.arquitetura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.ControleConcorrenciaHorarioTurmaVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Scope("singleton")
@Lazy
public class ControleConcorrencia extends SuperFacadeJDBC {

	public static List<Integer> listaProfessorHorario;
	public static HashMap<String, MatriculaVO> hashMapMatricula;
	private static List<ControleConcorrenciaHorarioTurmaVO> controleConcorrenciaHorarioTurmaVOs;

	public ControleConcorrencia() {
		setListaProfessorHorario(new ArrayList<Integer>());
	}

	public static synchronized void adicionarMatriculaHashMap(MatriculaVO matriculaVO) throws Exception {
		if (!getHashMapMatricula().containsKey(matriculaVO.getMatricula()) && !matriculaVO.getMatricula().equals("")) {
			ControleConcorrencia.getHashMapMatricula().put(matriculaVO.getMatricula(), matriculaVO);
		} else {
			throw new Exception("A matrícula " + matriculaVO.getMatricula() + " está sendo manipulada por outro usuário, tente dentro de alguns minutos.");
		}
	}

	public static synchronized void removerMatriculaHashMap(MatriculaVO matriculaVO) {
		getHashMapMatricula().remove(matriculaVO.getMatricula());
	}

	public static synchronized void adicionarProfessorListaProfessorHorario(ControleConcorrenciaHorarioTurmaVO concorrenciaHorarioTurmaVO) throws Exception {
		for (ControleConcorrenciaHorarioTurmaVO obj : ControleConcorrencia.getControleConcorrenciaHorarioTurmaVOs()) {
			if (concorrenciaHorarioTurmaVO.getCodigoProfessor().equals(obj.getCodigoProfessor())) {
				throw new Exception("O horário do professor " + concorrenciaHorarioTurmaVO.getNomeProfessor() + " está sendo manipulado por outro usuário (" + obj.getUsuarioVO().getNome() + "), tente dentro de alguns minutos.");
			}
		}
		ControleConcorrencia.getControleConcorrenciaHorarioTurmaVOs().add(concorrenciaHorarioTurmaVO);
	}

	public static synchronized void removerProfessorListaProfessorHorario(Integer professor) {
		Iterator<ControleConcorrenciaHorarioTurmaVO> it = getControleConcorrenciaHorarioTurmaVOs().iterator();
		int index = 0;
		while (it.hasNext()) {
			ControleConcorrenciaHorarioTurmaVO controleConcorrenciaHorarioTurmaVO = it.next();
			if (controleConcorrenciaHorarioTurmaVO.getCodigoProfessor().equals(professor)) {
				getControleConcorrenciaHorarioTurmaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public static synchronized void liberarHorarioProfessor(HorarioTurmaVO horarioTurmaVO) {
		for (ControleConcorrenciaHorarioTurmaVO obj : horarioTurmaVO.getControleConcorrenciaHorarioTurmaVOs()) {
			removerProfessorListaProfessorHorario(obj.getCodigoProfessor());
		}
		horarioTurmaVO.getControleConcorrenciaHorarioTurmaVOs().clear();
	}

	public static List<Integer> getListaProfessorHorario() {
		if (listaProfessorHorario == null) {
			listaProfessorHorario = new ArrayList<Integer>();
		}
		return ControleConcorrencia.listaProfessorHorario;
	}

	public void setListaProfessorHorario(List<Integer> listaProfessorHorario) {
		ControleConcorrencia.listaProfessorHorario = listaProfessorHorario;
	}

	public static HashMap<String, MatriculaVO> getHashMapMatricula() {
		if (hashMapMatricula == null) {
			hashMapMatricula = new HashMap<String, MatriculaVO>(0);
		}
		return hashMapMatricula;
	}

	public static void setHashMapMatricula(HashMap<String, MatriculaVO> hashMapMatricula) {
		ControleConcorrencia.hashMapMatricula = hashMapMatricula;
	}

	/**
	 * @return the controleConcorrenciaHorarioTurmaVOs
	 */
	public static List<ControleConcorrenciaHorarioTurmaVO> getControleConcorrenciaHorarioTurmaVOs() {
		if (controleConcorrenciaHorarioTurmaVOs == null) {
			controleConcorrenciaHorarioTurmaVOs = new ArrayList(0);
		}
		return controleConcorrenciaHorarioTurmaVOs;
	}

	/**
	 * @param aControleConcorrenciaHorarioTurmaVOs
	 *            the controleConcorrenciaHorarioTurmaVOs to set
	 */
	public static void setControleConcorrenciaHorarioTurmaVOs(List<ControleConcorrenciaHorarioTurmaVO> aControleConcorrenciaHorarioTurmaVOs) {
		setControleConcorrenciaHorarioTurmaVOs(aControleConcorrenciaHorarioTurmaVOs);
	}

}
