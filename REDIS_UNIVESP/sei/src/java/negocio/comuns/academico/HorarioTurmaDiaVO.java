package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import negocio.comuns.arquitetura.JsonDateDeserializer;
import negocio.comuns.arquitetura.JsonDateSerializer;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class HorarioTurmaDiaVO extends SuperVO {

	private static final long serialVersionUID = -2031562448900895117L;

	private Integer codigo;
    private HorarioTurmaVO horarioTurma;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date data;
    private FeriadoVO feriado;
    //Formato nº Aula[Codigo Disciplina]{Codigo Professor} ex: 1(234){567};2(3){87}...
//    private String horarioDisciplinaProfessor;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean isLancadoRegistro;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean possuiAulaMaisDeUmProfessor;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean ocultarDataAula;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean horarioAlterado = false;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private UsuarioVO usuarioResp;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private FuncionarioCargoVO funcionarioCargoVO;

    //Transiente
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean gerarEventoAulaOnLineGoogleMeet;
    private Boolean aulaReposicao;

    public static void validarDados(HorarioTurmaDiaVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Programação Aula) deve ser informado.");
        }
//		if (obj.getCodigo().intValue() == 0 && obj.getData().compareTo(new Date()) == -1) {
//			throw new ConsistirException("O campo DATA (Programação Aula) deve ser maior que o dia atual.");
//		}
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

    public Boolean getIsLancadoRegistro() {
        if (isLancadoRegistro == null) {        	
            isLancadoRegistro = false;
        }        
        return isLancadoRegistro;
    }

    public void setIsLancadoRegistro(Boolean isLancadoRegistro) {
        this.isLancadoRegistro = isLancadoRegistro;
    }

    public void setFeriado(FeriadoVO feriado) {
        this.feriado = feriado;
    }

    public HorarioTurmaDiaVO() {
        incializarDados();
    }

    public Integer consultarProfessorLecionaDisciplina(Integer nrDeAulas, Integer disciplina) {
        int x = 1;
        while (x <= nrDeAulas) {
            if (getCodigoDisciplina(x).intValue() == disciplina.intValue()) {
                return getCodigoProfessor(x);
            }
            x++;
        }
        return 0;
    }

    public boolean getIsAulaProgramada() {
        for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : getHorarioTurmaDiaItemVOs()) {
            if (!horarioTurmaDiaItemVO.getDisciplinaLivre() && !horarioTurmaDiaItemVO.getProfessorLivre()) {
                return true;
            }
        }
        return false;
    }

    public boolean getExisteAula(Integer nrAula) {
        int i = 0;
        while (i <= nrAula) {
            if (getCodigoDisciplina(i).intValue() != 0) {
                return true;
            }
            i++;
        }
        return false;
    }

    public Integer getCodigoDisciplina(Integer nrAula) {    
    	if(!getHorarioTurmaDiaItemVOs().isEmpty()){
    		for(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO:getHorarioTurmaDiaItemVOs()){
    			if(horarioTurmaDiaItemVO.getNrAula().equals(nrAula)){
    				return horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo();
    			}
    		}
    	}
    	return 0;
    }

    public Integer getCodigoProfessor(Integer nrAula) {
    	if(!getHorarioTurmaDiaItemVOs().isEmpty()){
    		for(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO:getHorarioTurmaDiaItemVOs()){
    			if(horarioTurmaDiaItemVO.getNrAula().equals(nrAula)){
    				return horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo();
    			}
    		}
    	}
    	return 0;
    }
    
    public Integer getCodigoGoogleMeet(Integer nrAula) {
    	if(!getHorarioTurmaDiaItemVOs().isEmpty()){
    		for(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO:getHorarioTurmaDiaItemVOs()){
    			if(horarioTurmaDiaItemVO.getNrAula().equals(nrAula)){
    				return horarioTurmaDiaItemVO.getGoogleMeetVO().getCodigo();
    			}
    		}
    	}
    	return 0;
    }
    

    public void substituirDisciplina(Integer nrAula, Integer disciplina, DisciplinaVO novaDisciplina) {
        int index = 0;
        for (HorarioTurmaDiaItemVO obj : getHorarioTurmaDiaItemVOs()) {
            if (obj.getDisciplinaVO().getCodigo().intValue() == disciplina.intValue() && obj.getNrAula().intValue() == nrAula.intValue()) {
                obj.setDisciplinaVO(novaDisciplina);
                getHorarioTurmaDiaItemVOs().set(index, obj);
                return;
            }
            index++;
        }
    }

    public void substituirProfessor(Integer nrAula, Integer professor, PessoaVO novoProfessor) {
        int index = 0;
        for (HorarioTurmaDiaItemVO obj : getHorarioTurmaDiaItemVOs()) {
            if (obj.getFuncionarioVO().getCodigo().intValue() == professor.intValue() && obj.getNrAula().intValue() == nrAula.intValue()) {
                obj.setFuncionarioVO(novoProfessor);
                getHorarioTurmaDiaItemVOs().set(index, obj);                
                return;
            }
            index++;
        }
    }
    
    public void substituirSala(Integer nrAula, Integer sala, SalaLocalAulaVO novaSala) {
        int index = 0;
        for (HorarioTurmaDiaItemVO obj : getHorarioTurmaDiaItemVOs()) {
            if (obj.getSala().getCodigo().intValue() == sala.intValue() && obj.getNrAula().intValue() == nrAula.intValue()) {
            	obj.getSala().setSala(novaSala.getSala());
                obj.getSala().setCodigo(novaSala.getCodigo());
                obj.getSala().getLocalAula().setLocal(novaSala.getLocalAula().getLocal());
                getHorarioTurmaDiaItemVOs().set(index, obj);                
                return;
            }
            index++;
        }
    }

    public Boolean adicinarProfessorEDisciplina(Integer nrAula, DisciplinaVO disciplina, PessoaVO novoProfessor, SalaLocalAulaVO sala, boolean substituir, boolean horarioLivre) {
        int index = 0;

        for (HorarioTurmaDiaItemVO obj : getHorarioTurmaDiaItemVOs()) {
            if (obj.getNrAula().intValue() == nrAula.intValue() && (substituir || (obj.getDisciplinaLivre() && obj.getProfessorLivre()))) {
                obj.getFuncionarioVO().setNome(novoProfessor.getNome());
                obj.getFuncionarioVO().setFormacaoAcademicaVOs(novoProfessor.getFormacaoAcademicaVOs());
                obj.getFuncionarioVO().setCodigo(novoProfessor.getCodigo());
                obj.getDisciplinaVO().setNome(disciplina.getNome());
                obj.getDisciplinaVO().setCodigo(disciplina.getCodigo());
                obj.getSala().setSala(sala.getSala());
                obj.getSala().setCodigo(sala.getCodigo());
                obj.getSala().getLocalAula().setLocal(sala.getLocalAula().getLocal());
                obj.setGerarEventoAulaOnLineGoogleMeet(horarioLivre);
                getHorarioTurmaDiaItemVOs().set(index, obj);                
                return true;
            }
            index++;
        }
        return false;
    }

    public List<Integer> getListaProfessor() {
        List<Integer> listaProfessor = new ArrayList<Integer>();
        for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : getHorarioTurmaDiaItemVOs()) {
            adicionarCodigoProfessorLista(listaProfessor, horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo());
        }
        return listaProfessor;
    }

    public void adicionarCodigoProfessorLista(List<Integer> listaProfessor, Integer codigo) {
        if (codigo.intValue() != 0) {
            for (Integer x : listaProfessor) {
                if (x.intValue() == codigo.intValue()) {
                    return;
                }
            }
            listaProfessor.add(codigo);
        }
    }

    public List<HorarioAlunoDiaItemVO> consultarDisciplinaHorarioAlunoDia(DisciplinaVO disciplina, TurmaVO turma) {
        List<HorarioAlunoDiaItemVO> objs = new ArrayList<HorarioAlunoDiaItemVO>(0);
        for (HorarioTurmaDiaItemVO diaItem : getHorarioTurmaDiaItemVOs()) {
            if (diaItem.getDisciplinaVO().getCodigo().intValue() == disciplina.getCodigo().intValue()) {
                objs.add(montarHorarioAlunoDiaItemVO(diaItem, disciplina, turma));
            }
        }
        return objs;
    }

    public List<HorarioAlunoDiaItemVO> consultarDisciplinaTurmaHorarioAlunoDia(DisciplinaVO disciplina, TurmaVO turma, TurmaVO turmaMatriculaPeriodoTurmaDisciplina) {
        List<HorarioAlunoDiaItemVO> objs = new ArrayList<HorarioAlunoDiaItemVO>(0);
        for (HorarioTurmaDiaItemVO diaItem : getHorarioTurmaDiaItemVOs()) {
            if ((diaItem.getDisciplinaVO().getCodigo().intValue() == disciplina.getCodigo().intValue()) 
                    && (turma.getCodigo().intValue() == turmaMatriculaPeriodoTurmaDisciplina.getCodigo().intValue() || turma.getIsPossuiCodigoTurmaAgrupada(turmaMatriculaPeriodoTurmaDisciplina.getCodigo()))) {
                objs.add(montarHorarioAlunoDiaItemVO(diaItem, disciplina, turma));
            }
        }
        return objs;
    }

    public HorarioAlunoDiaItemVO montarHorarioAlunoDiaItemVO(HorarioTurmaDiaItemVO obj, DisciplinaVO disciplina, TurmaVO turma) {
        HorarioAlunoDiaItemVO objItem = new HorarioAlunoDiaItemVO();
        objItem.setDisciplina(disciplina);
        objItem.getDisciplina().setCodigo(disciplina.getCodigo());
        objItem.getDisciplina().setNome(disciplina.getNome());
        objItem.setNrAula(obj.getNrAula());
        objItem.setHorario(obj.getHorario());
        objItem.setHorarioInicio(obj.getHorarioInicio());
        objItem.setHorarioTermino(obj.getHorarioTermino());
        objItem.getProfessor().setCodigo(obj.getFuncionarioVO().getCodigo());
        objItem.getProfessor().setNome(obj.getFuncionarioVO().getNome());
        objItem.getSala().setSala(obj.getSala().getSala());
        objItem.getSala().setCodigo(obj.getSala().getCodigo());
        objItem.getSala().getLocalAula().setLocal(obj.getSala().getLocalAula().getLocal());
        return objItem;
    }

    public void incializarDados() {
        codigo = 0;
        data = null;        
    }

    public JRBeanArrayDataSource getHorarioTurmaDiaItemJR() {
        return new JRBeanArrayDataSource(getHorarioTurmaDiaItemVOs().toArray());
    }

    public List<HorarioTurmaDiaItemVO> getHorarioTurmaDiaItemVOs() {
        if (horarioTurmaDiaItemVOs == null) {
            horarioTurmaDiaItemVOs = new ArrayList<>(0);
        }
        return horarioTurmaDiaItemVOs;
    }

    public void setHorarioTurmaDiaItemVOs(List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs) {
        this.horarioTurmaDiaItemVOs = horarioTurmaDiaItemVOs;
    }	

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
        return DiaSemana.NENHUM.getValor();
    }

    public DiaSemana getDiaSemanaEnum() {
        if (data != null) {
            return Uteis.getDiaSemanaEnum(data);
        }
        return DiaSemana.NENHUM;
    }

    public String getDiaSemana_Apresentar() {
        if (data != null) {
            return DiaSemana.getAbreviatura(DiaSemana.getValor(Uteis.getDiaSemana(data)));
        }
        return DiaSemana.NENHUM.getAbreviatura(DiaSemana.NENHUM.getValor());
    }

    public HorarioTurmaVO getHorarioTurma() {
    	if(horarioTurma == null){
    		horarioTurma = new HorarioTurmaVO();
    	}
        return horarioTurma;
    }

    public void setHorarioTurma(HorarioTurmaVO horarioTurma) {
        this.horarioTurma = horarioTurma;
    }

    public Boolean getPossuiAulaMaisDeUmProfessor() {
        if (possuiAulaMaisDeUmProfessor == null) {
            possuiAulaMaisDeUmProfessor = false;
        }
        return possuiAulaMaisDeUmProfessor;
    }

    public void setPossuiAulaMaisDeUmProfessor(Boolean possuiAulaMaisDeUmProfessor) {
        this.possuiAulaMaisDeUmProfessor = possuiAulaMaisDeUmProfessor;
    }

	public Boolean getHorarioAlterado() {
		if (horarioAlterado == null) {
			horarioAlterado = false;
		}
		return horarioAlterado;
	}

	public void setHorarioAlterado(Boolean horarioAlterado) {
		this.horarioAlterado = horarioAlterado;
	}
    

	public Boolean getOcultarDataAula() {
		if (ocultarDataAula == null) {
			ocultarDataAula = Boolean.FALSE;
		}
		return ocultarDataAula;
	}

	public void setOcultarDataAula(Boolean ocultarDataAula) {
		this.ocultarDataAula = ocultarDataAula;
	}

	public UsuarioVO getUsuarioResp() {
		if (usuarioResp == null) {
			usuarioResp = new UsuarioVO();
		}
		return usuarioResp;
	}

	public void setUsuarioResp(UsuarioVO usuarioResp) {
		this.usuarioResp = usuarioResp;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
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
	
	public Boolean getAulaReposicao() {
		if (aulaReposicao == null) {
			aulaReposicao = Boolean.FALSE;
		}
		return aulaReposicao;
	}

	public void setAulaReposicao(Boolean aulaReposicao) {
		this.aulaReposicao = aulaReposicao;
	}
}