package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

public class HorarioAlunoDiaVO implements Serializable {

    private Date data;
    private FeriadoVO feriado;
    private List<HorarioAlunoDiaItemVO> horarioAlunoDiaItemVOs;
    private boolean isAulaProgramada;
    public static final long serialVersionUID = 1L;
    // Data apenas para apresentação visão Aluno.

    public String getDia() {
        if (getData() == null) {
            return "-";
        }
        if (Uteis.getDiaMesData(getData()) < 10) {
            return "0" + Uteis.getDiaMesData(getData());
        }
        return "" + Uteis.getDiaMesData(getData());
    }

    public FeriadoVO getFeriado() {
        if (feriado == null) {
            feriado = new FeriadoVO();
        }
        return feriado;
    }

    public Boolean getIsFeriado() {
        if (getFeriado().getCodigo().intValue() > 0) {
            return true;
        }
        return false;
    }

    public void setFeriado(FeriadoVO feriado) {
        this.feriado = feriado;
    }

    public PessoaVO getProfessor(Integer codigo) {
        for (HorarioAlunoDiaItemVO obj : getHorarioAlunoDiaItemVOs()) {
            if (obj.getProfessor().getCodigo().intValue() == codigo.intValue()
                    && !obj.getProfessor().getNome().equals("")) {
                return obj.getProfessor();
            }
        }
        return null;
    }

    @SuppressWarnings("element-type-mismatch")
    public void removerHorarioAlunoDiaItem(Integer disciplina) {
        int index = 0;
        for (HorarioAlunoDiaItemVO objExistente : getHorarioAlunoDiaItemVOs()) {
            if (objExistente.getDisciplina().getCodigo().intValue() == disciplina.intValue()) {
                getHorarioAlunoDiaItemVOs().remove(index);
                removerHorarioAlunoDiaItem(disciplina);
                return;
            }
            index++;
        }
    }

    public void adicionarHorarioAlunoDiaItem(HorarioAlunoDiaItemVO obj) throws Exception {
        for (HorarioAlunoDiaItemVO objExistente : getHorarioAlunoDiaItemVOs()) {
//            if (objExistente.getNrAula().intValue() == obj.getNrAula().intValue()) {
//               throw new Exception("O horário do Aluno esta indisponível para os horarios da aula da turma/disciplina a ser adicionada.");
//            }
        }
        getHorarioAlunoDiaItemVOs().add(obj);
    }

    public String getDiaSemana_Apresentar() {
        if (getData() != null) {
            return DiaSemana.getDescricao(DiaSemana.getValor(Uteis.getDiaSemana(getData())));
        }
        return "00";
    }

    public String getDiaSemanaAbreviado_Apresentar() {
        if (getData() != null) {
            return DiaSemana.getAbreviatura(DiaSemana.getValor(Uteis.getDiaSemana(getData())));
        }
        return "00";
    }

    public DiaSemana getDiaSemana() {
        return Uteis.getDiaSemanaEnum(getData());
    }

    public Date getData() {
//        if (data == null) {
//            data = new Date();
//        }
        return data;
    }

    public String getData_Apresentar() {
        return Uteis.getData(getData());
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<HorarioAlunoDiaItemVO> getHorarioAlunoDiaItemVOs() {
        if (horarioAlunoDiaItemVOs == null) {
            horarioAlunoDiaItemVOs = new ArrayList<HorarioAlunoDiaItemVO>(0);
        }
        //Ordenacao.ordenarLista(horarioAlunoDiaItemVOs, "nrAula");
        return horarioAlunoDiaItemVOs;
    }

    public void setHorarioAlunoDiaItemVOs(List<HorarioAlunoDiaItemVO> horarioAlunoDiaItemVO) {
        this.horarioAlunoDiaItemVOs = horarioAlunoDiaItemVO;
    }

    public boolean isAulaProgramada() {
        //return isAulaProgramada;
        for (HorarioAlunoDiaItemVO horarioAlunoDiaItemVO : getHorarioAlunoDiaItemVOs()) {
            if (!horarioAlunoDiaItemVO.getDisciplinaLivre()) {
                return true;
            }
        }
        return false;
    }

    //ADICIONADO DANILO
    public boolean getIsAulaProgramada() {
        for (HorarioAlunoDiaItemVO horarioAlunoDiaItemVO : getHorarioAlunoDiaItemVOs()) {
            if (!horarioAlunoDiaItemVO.getDisciplinaLivre()) {
                return true;
            }
        }
        return false;
    }

    public void setAulaProgramada(boolean isAulaProgramada) {
        this.isAulaProgramada = isAulaProgramada;
    }

    public void replicarDataHorarioAlunoDia() {
        Iterator i = getHorarioAlunoDiaItemVOs().iterator();
        while (i.hasNext()) {
            HorarioAlunoDiaItemVO horarioAluno = (HorarioAlunoDiaItemVO) i.next();
            horarioAluno.setData(getData());
        }
    }
}
