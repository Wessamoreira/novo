package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

/**
 * 
 * @author rodrigo
 */
public class QuadroHorarioVO implements Serializable {

    private TurnoVO turno;
    private DisciplinaVO disciplina;
    private TurmaVO turma;
    private List listaTurnoSegunda;
    private List listaTurnoTerca;
    private List listaTurnoQuarta;
    private List listaTurnoQuinta;
    private List listaTurnoSexta;
    private List listaTurnoSabado;
    private List listaTurnoDomingo;
    private List listaHorarioTurno;
    private HorarioProfessorVO horarioProfessorVO;
    private HorarioAlunoVO horarioAlunoVO;
    public static final long serialVersionUID = 1L;

    public QuadroHorarioVO() {
        inicializarDados();
    }

    public void inicializarDados() {
    }

    public Boolean getExisteTurnoSelecionado() {
        if (getTurno().getCodigo().intValue() != 0) {
            return true;
        }
        return false;

    }

    public boolean getIsExisteHorarioDomingo() {
        return !getListaTurnoDomingo().isEmpty();
    }

    public boolean getIsExisteHorarioSegunda() {
        return !getListaTurnoSegunda().isEmpty();
    }

    public boolean getIsExisteHorarioTerca() {
        return !getListaTurnoTerca().isEmpty();
    }

    public boolean getIsExisteHorarioQuarta() {
        return !getListaTurnoQuarta().isEmpty();
    }

    public boolean getIsExisteHorarioQuinta() {
        return !getListaTurnoQuinta().isEmpty();
    }

    public boolean getIsExisteHorarioSexta() {
        return !getListaTurnoSexta().isEmpty();
    }

    public boolean getIsExisteHorarioSabado() {
        return !getListaTurnoSabado().isEmpty();
    }

    public void montarListaHorarioDomingo(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.DOMINGO);
        atualizarListaDomingo(novoObj);
    }

    public void montarListaHorarioSegunda(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.SEGUNGA);
        atualizarListaSegunda(novoObj);
    }

    public void montarListaHorarioTerca(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.TERCA);
        atualizarListaTerca(novoObj);
    }

    public void montarListaHorarioQuarta(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.QUARTA);
        atualizarListaQuarta(novoObj);
    }

    public void montarListaHorarioQuinta(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.QUINTA);
        atualizarListaQuinta(novoObj);
    }

    public void montarListaHorarioSexta(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.SEXTA);
        atualizarListaSexta(novoObj);
    }

    public void montarListaHorarioSabado(DisponibilidadeHorarioVO obj) {
        DisponibilidadeHorarioVO novoObj = obj.getClone();
        novoObj.setDiaSemana(DiaSemana.SABADO);
        atualizarListaSabado(novoObj);
    }

    public String calculaIntervaloHora(Integer nrAula, String horaInicio) {
        String horaFinal;
        horaFinal = Uteis.getCalculodeHoraSemIntervalo(horaInicio, nrAula, getTurno().getDuracaoAula());
        return horaFinal;
    }

    public void ordenarListas() {

        Ordenacao.ordenarLista(getListaTurnoSegunda(), "nrAula");
        Ordenacao.ordenarLista(getListaTurnoTerca(), "nrAula");
        Ordenacao.ordenarLista(getListaTurnoQuarta(), "nrAula");
        Ordenacao.ordenarLista(getListaTurnoQuinta(), "nrAula");
        Ordenacao.ordenarLista(getListaTurnoSexta(), "nrAula");
        Ordenacao.ordenarLista(getListaTurnoSabado(), "nrAula");
        Ordenacao.ordenarLista(getListaTurnoDomingo(), "nrAula");
    }

    public void atualizarValoresQuadroHorario() throws Exception {
        atualizarValoresQuadroHorarioSegunda();
        atualizarValoresQuadroHorarioTerca();
        atualizarValoresQuadroHorarioQuarta();
        atualizarValoresQuadroHorarioQuinta();
        atualizarValoresQuadroHorarioSexta();
        atualizarValoresQuadroHorarioSabado();
        atualizarValoresQuadroHorarioDomingo();

    }

    public void atualizarValoresQuadroHorarioSegunda() throws Exception {
        Iterator i = getListaTurnoSegunda().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaSegunda(obj);
        }
    }

    public void atualizarValoresQuadroHorarioTerca() throws Exception {
        Iterator i = getListaTurnoTerca().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaTerca(obj);
        }
    }

    public void atualizarValoresQuadroHorarioQuarta() throws Exception {
        Iterator i = getListaTurnoQuarta().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaQuarta(obj);
        }
    }

    public void atualizarValoresQuadroHorarioQuinta() throws Exception {
        Iterator i = getListaTurnoQuinta().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaQuinta(obj);
        }
    }

    public void atualizarValoresQuadroHorarioSexta() throws Exception {
        Iterator i = getListaTurnoSexta().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaSexta(obj);
        }
    }

    public void atualizarValoresQuadroHorarioSabado() throws Exception {
        Iterator i = getListaTurnoSabado().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaSabado(obj);
        }
    }

    public void atualizarValoresQuadroHorarioDomingo() throws Exception {
        Iterator i = getListaTurnoDomingo().iterator();
        while (i.hasNext()) {
            DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) i.next();
            obj = validarDadosDisponibilidadeHorario(obj);
            atualizarListaDomingo(obj);
        }
    }

    public DisponibilidadeHorarioVO validarDadosDisponibilidadeHorario(DisponibilidadeHorarioVO disponibilidadeHorario) throws Exception {
        // DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO)
        // context().getExternalContext().getRequestMap().get("domingo");
        if (disponibilidadeHorario.getDisciplina().getCodigo().intValue() == 0 && disponibilidadeHorario.getDisponivelHorario().equals(true)) {
            disponibilidadeHorario.getDisciplina().setCodigo(new Integer(-1));
            disponibilidadeHorario.getTurma().setCodigo(-1);
            disponibilidadeHorario.getDisciplina().setNome("Indisponivel");
            disponibilidadeHorario.getTurma().setIdentificadorTurma("Indisponivel");
        } else if (disponibilidadeHorario.getDisponivelHorario().equals(false)) {
            disponibilidadeHorario.setDisciplina(new DisciplinaVO());
            disponibilidadeHorario.setTurma(new TurmaVO());
            disponibilidadeHorario.getDisciplina().setNome("Livre");
            disponibilidadeHorario.getTurma().setIdentificadorTurma("Livre");
        }
        return disponibilidadeHorario;

    }

    public void atualizarListaDomingo(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoDomingo().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoDomingo().set(index, objExistente);
                return;
            }
            index++;
        }
        getListaTurnoDomingo().add(disponibilidadeHorario);
    }

    public void atualizarListaSegunda(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoSegunda().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoSegunda().set(index, disponibilidadeHorario);
                return;
            }
            index++;
        }
        getListaTurnoSegunda().add(disponibilidadeHorario);
    }

    public void atualizarListaTerca(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoTerca().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoTerca().set(index, objExistente);
                return;
            }
            index++;
        }
        getListaTurnoTerca().add(disponibilidadeHorario);
    }

    public void atualizarListaQuarta(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoQuarta().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoQuarta().set(index, objExistente);
                return;
            }
            index++;
        }
        getListaTurnoQuarta().add(disponibilidadeHorario);
    }

    public void atualizarListaQuinta(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoQuinta().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoQuinta().set(index, objExistente);
                return;
            }
            index++;
        }
        getListaTurnoQuinta().add(disponibilidadeHorario);
    }

    public void atualizarListaSexta(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoSexta().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoSexta().set(index, objExistente);
                return;
            }
            index++;
        }
        getListaTurnoSexta().add(disponibilidadeHorario);
    }

    public void atualizarListaSabado(DisponibilidadeHorarioVO disponibilidadeHorario) {
        Iterator i = getListaTurnoSabado().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(disponibilidadeHorario.getTurno().getCodigo())
                    && objExistente.getNrAula().equals(disponibilidadeHorario.getNrAula())
                    && objExistente.getDiaSemana().equals(disponibilidadeHorario.getDiaSemana())) {
                objExistente.setDisponivelHorario(disponibilidadeHorario.getDisponivelHorario());
                objExistente.setExisteAula(disponibilidadeHorario.getExisteAula());
                objExistente.setDisciplina(disponibilidadeHorario.getDisciplina());
                objExistente.setTurma(disponibilidadeHorario.getTurma());
                getListaTurnoSabado().set(index, objExistente);
                return;
            }
            index++;
        }
        getListaTurnoSabado().add(disponibilidadeHorario);
    }

    public String getDadosHorarioSegunda() {
        Iterator i = getListaTurnoSegunda().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getDisciplina().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public String getDadosHorarioTerca() {
        Iterator i = getListaTurnoTerca().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getDisciplina().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public String getDadosHorarioQuarta() {
        Iterator i = getListaTurnoQuarta().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getDisciplina().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public String getDadosHorarioQuinta() {
        Iterator i = getListaTurnoQuinta().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getDisciplina().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public String getDadosHorarioSexta() {
        Iterator i = getListaTurnoSexta().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getDisciplina().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public String getDadosHorarioSabado() {
        Iterator i = getListaTurnoSabado().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getDisciplina().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public String getDadosHorarioDomingo() {
        Iterator i = getListaTurnoDomingo().iterator();
        String dadosHorario = "";
        while (i.hasNext()) {
            DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
            dadosHorario = dadosHorario
                    + HorarioProfessorVO.montarDadosHorarioProfessor(objExistente.getNrAula(), objExistente.getTurma().getCodigo(), objExistente.getTurma().getCodigo());
        }
        return dadosHorario;
    }

    public List getListaHorarioTurno() {
        if (listaHorarioTurno == null) {
            listaHorarioTurno = new ArrayList();
        }
        return listaHorarioTurno;
    }

    public void setListaHorarioTurno(List listaHorarioTurno) {
        this.listaHorarioTurno = listaHorarioTurno;
    }

    public List getListaTurnoDomingo() {
        if (listaTurnoDomingo == null) {
            listaTurnoDomingo = new ArrayList();
        }
        return listaTurnoDomingo;
    }

    public void setListaTurnoDomingo(List listaTurnoDomingo) {
        this.listaTurnoDomingo = listaTurnoDomingo;
    }

    public List getListaTurnoQuarta() {
        if (listaTurnoQuarta == null) {
            listaTurnoQuarta = new ArrayList();
        }
        return listaTurnoQuarta;
    }

    public void setListaTurnoQuarta(List listaTurnoQuarta) {
        this.listaTurnoQuarta = listaTurnoQuarta;
    }

    public List getListaTurnoQuinta() {
        if (listaTurnoQuinta == null) {
            listaTurnoQuinta = new ArrayList();
        }
        return listaTurnoQuinta;
    }

    public void setListaTurnoQuinta(List listaTurnoQuinta) {
        this.listaTurnoQuinta = listaTurnoQuinta;
    }

    public List getListaTurnoSabado() {
        if (listaTurnoSabado == null) {
            listaTurnoSabado = new ArrayList();
        }
        return listaTurnoSabado;
    }

    public void setListaTurnoSabado(List listaTurnoSabado) {
        this.listaTurnoSabado = listaTurnoSabado;
    }

    public List getListaTurnoSegunda() {
        if (listaTurnoSegunda == null) {
            listaTurnoSegunda = new ArrayList();
        }
        return listaTurnoSegunda;
    }

    public void setListaTurnoSegunda(List listaTurnoSegunda) {
        this.listaTurnoSegunda = listaTurnoSegunda;
    }

    public List getListaTurnoSexta() {
        if (listaTurnoSexta == null) {
            listaTurnoSexta = new ArrayList();
        }
        return listaTurnoSexta;
    }

    public void setListaTurnoSexta(List listaTurnoSexta) {
        this.listaTurnoSexta = listaTurnoSexta;
    }

    public List getListaTurnoTerca() {
        if (listaTurnoTerca == null) {
            listaTurnoTerca = new ArrayList();
        }
        return listaTurnoTerca;
    }

    public void setListaTurnoTerca(List listaTurnoTerca) {
        this.listaTurnoTerca = listaTurnoTerca;
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

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
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

    public HorarioProfessorVO getHorarioProfessorVO() {
        if (horarioProfessorVO == null) {
            horarioProfessorVO = new HorarioProfessorVO();
        }
        return horarioProfessorVO;
    }

    public void setHorarioProfessorVO(HorarioProfessorVO horarioProfessorVO) {
        this.horarioProfessorVO = horarioProfessorVO;
    }

    public HorarioAlunoVO getHorarioAlunoVO() {
        if (horarioAlunoVO == null) {
            horarioAlunoVO = new HorarioAlunoVO();
        }
        return horarioAlunoVO;
    }

    public void setHorarioAlunoVO(HorarioAlunoVO horarioAlunoVO) {
        this.horarioAlunoVO = horarioAlunoVO;
    }
}
