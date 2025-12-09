package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

@XmlRootElement(name = "horarioProfessorDia")
public class HorarioProfessorDiaVO extends SuperVO {

    private HorarioProfessorVO horarioProfessor;
    private Date data;    
//    private String horarioDisciplinaTurma;
    private Integer codigo;
    private FeriadoVO feriado;
    private List<HorarioProfessorDiaItemVO> horarioProfessorDiaItemVOs;
    private Boolean isLancadoRegistro;
    //
    private Boolean horarioAlterado;
    
    //ATRIBUTOS TRANSIENTES
    private String anoVigente;
    private String semestreVigente;
    
    public static final long serialVersionUID = 1L;

    public static void validarDados(HorarioTurmaDiaVO obj) throws ConsistirException {
    }

    public String getDia() {
        if (getData() == null) {
            return "-";
        }
        if (Uteis.getDiaMesData(getData()) < 10) {
            return "0" + Uteis.getDiaMesData(getData());
        }
        return "" + Uteis.getDiaMesData(getData());
    }

    @XmlElement(name = "feriado")
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

    public HorarioProfessorDiaVO() {
        incializarDados();
    }

    public Integer getCodigoDisciplina(Integer nrAula) {  
		if (!getHorarioProfessorDiaItemVOs().isEmpty()) {
			for (HorarioProfessorDiaItemVO obj : getHorarioProfessorDiaItemVOs()) {
				if (obj.getNrAula().equals(nrAula)) {
					return obj.getDisciplinaVO().getCodigo();
				}
			}
		}
		return 0;
    }

    public Integer getCodigoTurma(Integer nrAula) { 
    	if(!getHorarioProfessorDiaItemVOs().isEmpty()){
    		for (HorarioProfessorDiaItemVO obj : getHorarioProfessorDiaItemVOs()) {
    			if(obj.getNrAula().equals(nrAula)){
    				return obj.getTurmaVO().getCodigo();
    			}
    		}
    	}
    	return 0;
    }

    public void substituirDisciplina(Integer nrAula, Integer disciplina, DisciplinaVO novaDisciplina) {
        int index = 0;
        for (HorarioProfessorDiaItemVO obj : getHorarioProfessorDiaItemVOs()) {
            if (obj.getDisciplinaVO().getCodigo().intValue() == disciplina.intValue() && obj.getNrAula().intValue() == nrAula.intValue()) {
                obj.setDisciplinaVO(novaDisciplina);
                getHorarioProfessorDiaItemVOs().set(index, obj);
                return;
            }
            index++;
        }
    }

    public void adicinarTurmaEDisciplina(Integer nrAula, DisciplinaVO disciplina, TurmaVO novaTurma, SalaLocalAulaVO sala, boolean alterar) throws ConsistirException {
        int index = 0;
        for (HorarioProfessorDiaItemVO obj : getHorarioProfessorDiaItemVOs()) {
            if (obj.getNrAula().intValue() == nrAula.intValue()) {
                if (!alterar && !obj.getDisciplinaLivre() && (obj.getDisciplinaVO().getCodigo().intValue() != disciplina.getCodigo().intValue() || obj.getTurmaVO().getCodigo().intValue() != novaTurma.getCodigo().intValue())) {
                    throw new ConsistirException("O horário '" + obj.getHorario() + " do dia " + getData_Apresentar() + " não está mais disponível no horário do professor.");
                }
                if (novaTurma != null) {
                    obj.getTurmaVO().setIdentificadorTurma(novaTurma.getIdentificadorTurma());
                    obj.getTurmaVO().setCodigo(novaTurma.getCodigo());
                } else {
                    obj.getTurmaVO().setCodigo(0);
                    obj.getTurmaVO().setIdentificadorTurma("");
                }
                if (disciplina != null) {
                    obj.getDisciplinaVO().setNome(disciplina.getNome());
                    obj.getDisciplinaVO().setCodigo(disciplina.getCodigo());
                } else {
                    obj.getDisciplinaVO().setNome("");
                    obj.getDisciplinaVO().setCodigo(0);
                }
                if (Uteis.isAtributoPreenchido(sala)) {
                    obj.getSala().setSala(sala.getSala());
                    obj.getSala().setCodigo(sala.getCodigo());
                    obj.getSala().getLocalAula().setLocal(sala.getLocalAula().getLocal());
                } else {
                    obj.getSala().setSala("");
                    obj.getSala().setCodigo(0);
                    obj.getSala().getLocalAula().setLocal("");
                }
                getHorarioProfessorDiaItemVOs().set(index, obj);
                return;
            }
            index++;
        }

        HorarioProfessorDiaItemVO item = new HorarioProfessorDiaItemVO();
        item.setNrAula(nrAula);
        item.setDisciplinaVO(disciplina);
        item.setTurmaVO(novaTurma);
        item.getSala().setSala(sala.getSala());
        item.getSala().setCodigo(sala.getCodigo());
        item.getSala().getLocalAula().setLocal(sala.getLocalAula().getLocal());
        getHorarioProfessorDiaItemVOs().add(item);

    }

    public boolean getExisteAula(Integer nrAula) {
    	if(!getHorarioProfessorDiaItemVOs().isEmpty()){
    		for(HorarioProfessorDiaItemVO horarioProfessorDiaItemVO:getHorarioProfessorDiaItemVOs()){
    			if(horarioProfessorDiaItemVO.getNrAula().equals(nrAula)){
    				return Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getDisciplinaVO()) && Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getTurmaVO());
    			}
    		}
    	}else{
        int i = 0;
        while (i <= nrAula) {
            if (getCodigoDisciplina(i).intValue() != 0) {
                return true;
            }
            i++;
        }
    	}
        return false;
    }

    @XmlElement(name = "lancadoRegistro")
    public Boolean getIsLancadoRegistro() {
        if (isLancadoRegistro == null) {
            isLancadoRegistro = false;
        }
        return isLancadoRegistro;
    }

    public void setIsLancadoRegistro(Boolean isLancadoRegistro) {
        this.isLancadoRegistro = isLancadoRegistro;
    }

    public void removerVinculoTurma(Integer turma) throws ConsistirException {
        int index = 0;
        for (HorarioProfessorDiaItemVO obj : getHorarioProfessorDiaItemVOs()) {
            if (obj.getTurmaVO().getCodigo().intValue() == turma) {
                obj.getTurmaVO().setCodigo(0);
                obj.getTurmaVO().setIdentificadorTurma("");
                obj.getDisciplinaVO().setNome("");
                obj.getDisciplinaVO().setCodigo(0);
                getHorarioProfessorDiaItemVOs().set(index, obj);

            }
            index++;
        }

    }

    public boolean getIsAulaProgramada() {
        for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : getHorarioProfessorDiaItemVOs()) {
            if (!horarioProfessorDiaItemVO.getDisciplinaLivre() && !horarioProfessorDiaItemVO.getTurmaLivre()) {
                return true;
            }
        }
        return false;
    }

    public void incializarDados() {
        codigo = 0;
        data = null;        
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getData_Apresentar() {
        if (data != null) {
            return Uteis.getData(data);
        }
        return "";
    }

    @XmlElement(name = "data")
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDiaSemana() {
        if (data != null) {
            return DiaSemana.getValor(Uteis.getDiaSemana(data));
        }
        return "00";
    }

    public DiaSemana getDiaSemanaEnum() {
        if (data != null) {
            return DiaSemana.getEnum(DiaSemana.getValor(Uteis.getDiaSemana(data)));
        }
        return DiaSemana.NENHUM;
    }

    public String getDiaSemana_Apresentar() {
        if (data != null) {
            return DiaSemana.getAbreviatura(DiaSemana.getValor(Uteis.getDiaSemana(data)));
        }
        return "";
    }

    public String getDiaSemana_ApresentarTotal() {
        if (data != null) {
            return DiaSemana.getDescricao(DiaSemana.getValor(Uteis.getDiaSemana(data)));
        }
        return "";
    }
   
    @XmlElement(name = "horarioProfessorDiaItemVOs")
    public List<HorarioProfessorDiaItemVO> getHorarioProfessorDiaItemVOs() {
        if (horarioProfessorDiaItemVOs == null) {
            horarioProfessorDiaItemVOs = new ArrayList<HorarioProfessorDiaItemVO>(0);
        }
        return horarioProfessorDiaItemVOs;
    }

    public void setHorarioProfessorDiaItemVOs(List<HorarioProfessorDiaItemVO> horarioProfessorDiaItemVOs) {
        this.horarioProfessorDiaItemVOs = horarioProfessorDiaItemVOs;
    }

    @XmlElement(name = "horarioProfessor")
    public HorarioProfessorVO getHorarioProfessor() {
    	if(horarioProfessor == null){
    		horarioProfessor = new HorarioProfessorVO();
    	}
        return horarioProfessor;
    }

    public void setHorarioProfessor(HorarioProfessorVO horarioProfessor) {
        this.horarioProfessor = horarioProfessor;
    }

    @XmlElement(name = "horarioAlterado")
	public Boolean getHorarioAlterado() {
		if (horarioAlterado == null) {
			horarioAlterado = false;
		}
		return horarioAlterado;
	}

	public void setHorarioAlterado(Boolean horarioAlterado) {
		this.horarioAlterado = horarioAlterado;
	}
    
	public PessoaVO getProfessor(Integer codigo) {
        for (HorarioProfessorDiaItemVO obj : getHorarioProfessorDiaItemVOs()) {
        	PessoaVO professorVO = obj.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor();
            if (professorVO.getCodigo().intValue() == codigo.intValue()
                    && !professorVO.getNome().equals("")) {
                return professorVO;
            }
        }
        return null;
    }
	
	@SuppressWarnings("element-type-mismatch")
    public void removerHorarioProfessorDiaItem(Integer disciplina) {
        int index = 0;
        for (HorarioProfessorDiaItemVO objExistente : getHorarioProfessorDiaItemVOs()) {
            if (objExistente.getDisciplinaVO().getCodigo().intValue() == disciplina.intValue()) {
                getHorarioProfessorDiaItemVOs().remove(index);
                removerHorarioProfessorDiaItem(disciplina);
                return;
            }
            index++;
        }
    }

	public void replicarDataHorarioProfessorDia() {
        Iterator i = getHorarioProfessorDiaItemVOs().iterator();
        while (i.hasNext()) {
            HorarioProfessorDiaItemVO horarioProfessor = (HorarioProfessorDiaItemVO) i.next();
            horarioProfessor.setData(getData());
        }
    }

	public String getAnoVigente() {
		if (anoVigente == null) {
			anoVigente= "";
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
	
	
    
}
