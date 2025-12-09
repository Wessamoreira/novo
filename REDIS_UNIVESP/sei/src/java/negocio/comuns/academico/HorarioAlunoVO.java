package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Ordenacao;

public class HorarioAlunoVO extends SuperVO {

    private MatriculaVO matricula;
    private MatriculaPeriodoVO matriculaPeriodo;
    private List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs;
    private List<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>> calendarioHorarioAulaVOs;
    public static final long serialVersionUID = 1L;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public HorarioAlunoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    public void adicionarHorarioAlunoTurno(HorarioAlunoTurnoVO horarioAlunoTurnoVO) {
        int index = 0;
        for (HorarioAlunoTurnoVO obj : getHorarioAlunoTurnoVOs()) {
            if (obj.getTurno().getCodigo().intValue() == horarioAlunoTurnoVO.getTurno().getCodigo().intValue()) {
                Ordenacao.ordenarLista(horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs(), "nrAula");
                Ordenacao.ordenarLista(horarioAlunoTurnoVO.getHorarioAlunoDiaVOs(), "data");
                // getHorarioAlunoTurnoVOs().set(index, horarioAlunoTurnoVO);
                return;
            }
            index++;
        }
        Ordenacao.ordenarLista(horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs(), "nrAula");
        Ordenacao.ordenarLista(horarioAlunoTurnoVO.getHorarioAlunoDiaVOs(), "data");
        getHorarioAlunoTurnoVOs().add(horarioAlunoTurnoVO);
    }

    public HorarioAlunoTurnoVO consultarHorarioAlunoTurno(Integer turno) {
        for (HorarioAlunoTurnoVO obj : getHorarioAlunoTurnoVOs()) {
            if (obj.getTurno().getCodigo().intValue() == turno.intValue()) {
                return obj;
            }
        }
        return new HorarioAlunoTurnoVO();
    }

    public Integer getTamanhoListaTurno() {
        if (getHorarioAlunoTurnoVOs() == null) {
            return 0;
        }
        return getHorarioAlunoTurnoVOs().size();
    }

    @SuppressWarnings("element-type-mismatch")
    public void removerDisponibilidadeHorarioAlunoVOs(Integer disciplina) {
        int index = 0;
        for (HorarioAlunoTurnoVO horarioAlunoTurnoVO : getHorarioAlunoTurnoVOs()) {
            horarioAlunoTurnoVO.removerDisciplinaHorarioAluno(disciplina);
            if (horarioAlunoTurnoVO.getHorarioAlunoDiaVOs().isEmpty()) {
                getHorarioAlunoTurnoVOs().remove(index);
                removerDisponibilidadeHorarioAlunoVOs(disciplina);
                return;
            }
            index++;
        }
    }

    public PessoaVO getProfessor(Integer codigo) {
        PessoaVO prof = null;
        for (HorarioAlunoTurnoVO obj : getHorarioAlunoTurnoVOs()) {
            prof = obj.getProfessor(codigo);
            if (prof != null) {
                return prof;
            }
        }
        return prof;
    }

//	
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return matricula;
    }

    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
        if (horarioAlunoTurnoVOs == null) {
            horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);
        }
        return horarioAlunoTurnoVOs;
    }

    public void setHorarioAlunoTurnoVOs(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
        this.horarioAlunoTurnoVOs = horarioAlunoTurnoVOs;
    }

    public List<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>> getCalendarioHorarioAulaVOs() {
        if (calendarioHorarioAulaVOs == null) {
            calendarioHorarioAulaVOs = new ArrayList<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>>(0);
        }
        return calendarioHorarioAulaVOs;
    }

    public void setCalendarioHorarioAulaVOs(List<CalendarioHorarioAulaVO<HorarioAlunoDiaVO>> calendarioHorarioAulaVOs) {
        this.calendarioHorarioAulaVOs = calendarioHorarioAulaVOs;
    }
}
