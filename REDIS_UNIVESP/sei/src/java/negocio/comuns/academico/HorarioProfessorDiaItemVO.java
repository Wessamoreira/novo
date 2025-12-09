package negocio.comuns.academico;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class HorarioProfessorDiaItemVO extends SuperVO implements Serializable, Cloneable {

    private Integer codigo;
    private HorarioProfessorDiaVO horarioProfessorDiaVO;
	private Integer nrAula;
    private Integer duracaoAula;
    private String horario;
    private DisciplinaVO disciplinaVO;
    private TurmaVO turmaVO;
    //Usado na tela de meus horários professor
    private Date data;
    private Date dataFinal;
    private String professor;
    private Integer codProfessor;
	//Atributos usados no relatório Cronograma de aula
    private String telefoneProfessor;
    private String celularProfessor;
    private String emailProfessor;
    private String titulacaoProfessor;
    private String horarioInicio;
    private String horarioTermino;
    private SalaLocalAulaVO sala;
    
    private Boolean aulaJaRegistrada;
    public static final long serialVersionUID = 1L;
    private HorarioTurmaDiaItemVO horarioTurmaDiaItemVO;
    private UsuarioVO usuarioLiberacaoChoqueHorario;
    private Boolean registroAulaAutomaticoSucesso;
    private String motivoErroRegistroAulaAutomatico;
    
    //TRANSIENT
    private String conteudoRegistroAulaAutomatico;
    private String nivelEducacional;

    public HorarioProfessorDiaItemVO() {
        inicializarDados();
    }

    public void inicializarDados() {
        setNrAula(0);
        setHorario("");
    }

   
    public boolean getDisciplinaLivre() {
        if (getDisciplinaVO().getCodigo().intValue() == 0) {
            return true;
        }
        return false;
    }

    public boolean getTurmaLivre() {
        if (getTurmaVO().getCodigo().intValue() == 0) {
            return true;
        }
        return false;
    }

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public Integer getNrAula() {
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * @return the data
     */
    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    public String getData_Apresentar() {
        if (getData() != null) {
            return Uteis.getData(getData());
        }
        return "";
    }

    public String getDataFinal_Apresentar() {
        if (getDataFinal() != null) {
            return Uteis.getData(getDataFinal());
        }
        return "...";
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getProfessor() {
        if (professor == null) {
            professor = "";
        }
        return professor;
    }

    public Date getDataFinal() {

        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataModulo() {
		if (getTurmaVO().getCurso().getPeriodicidade() != null) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("IN")) {
				if (getDataFinal() != null) {
					return Uteis.getData(getData()) + " à " + Uteis.getData(getDataFinal());
				}

				return Uteis.getData(getData());
			}
		}
        return Uteis.getDiaSemana_Apresentar(getData())+" "+Uteis.getData(getData()) + " - " + getHorario();
    }
    
    public String getDataModuloApresentarProfessor() {
		if (getTurmaVO().getCurso().getPeriodicidade() != null) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("IN")) {
				if (getDataFinal() != null) {
					return Uteis.getData(getData()) + " à " + Uteis.getData(getDataFinal());
				}

				return Uteis.getData(getData());
			}
		}
        return Uteis.getDiaSemana_Apresentar(getData())+" "+Uteis.getData(getData());
    }

    public String getTelefoneProfessor() {
        if (telefoneProfessor == null) {
            telefoneProfessor = "";
        }
        return telefoneProfessor;
    }

    public void setTelefoneProfessor(String telefoneProfessor) {
        this.telefoneProfessor = telefoneProfessor;
    }

    public String getCelularProfessor() {
        if (celularProfessor == null) {
            celularProfessor = "";
        }
        return celularProfessor;
    }

    public void setCelularProfessor(String celularProfessor) {
        this.celularProfessor = celularProfessor;
    }

    public String getEmailProfessor() {
        if (emailProfessor == null) {
            emailProfessor = "";
        }
        return emailProfessor;
    }

    public void setEmailProfessor(String emailProfessor) {
        this.emailProfessor = emailProfessor;
    }

    public HorarioProfessorDiaItemVO getClone() throws Exception {
        HorarioProfessorDiaItemVO obj = (HorarioProfessorDiaItemVO) super.clone();
        return obj;
    }

	public String getTitulacaoProfessor() {
		if(titulacaoProfessor == null){
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}

	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}
    
    public Integer getCodProfessor() {
    	if (codProfessor == null) {
    		codProfessor = 0;
    	}
		return codProfessor;
	}

	public void setCodProfessor(Integer codProfessor) {
		this.codProfessor = codProfessor;
	}

	public Integer getDuracaoAula() {
		if (duracaoAula == null) {
			duracaoAula = 0;
		}
		return duracaoAula;
	}

	public void setDuracaoAula(Integer duracaoAula) {
		this.duracaoAula = duracaoAula;
	}
	
	
	public String getHorarioInicio() {
		if (horarioInicio == null) {
			horarioInicio = "";
		}
		return horarioInicio;
	}

	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public String getHorarioTermino() {
		if (horarioTermino == null) {
			horarioTermino = "";
		}
		return horarioTermino;
	}

	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}

	public Boolean getAulaJaRegistrada() {
		if (aulaJaRegistrada == null) {
			aulaJaRegistrada = false;
		}
		return aulaJaRegistrada;
	}

	public void setAulaJaRegistrada(Boolean aulaJaRegistrada) {
		this.aulaJaRegistrada = aulaJaRegistrada;
	}

	/**
	 * @return the salaLocalAulaVO
	 */
	public SalaLocalAulaVO getSala() {
		if (sala == null) {
			sala = new SalaLocalAulaVO();
		}
		return sala;
	}

	/**
	 * @param salaLocalAulaVO the salaLocalAulaVO to set
	 */
	public void setSala(SalaLocalAulaVO salaLocalAulaVO) {
		this.sala = salaLocalAulaVO;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	

	/**
	 * @return the horarioProfessorDiaVO
	 */
	public HorarioProfessorDiaVO getHorarioProfessorDiaVO() {
		if (horarioProfessorDiaVO == null) {
			horarioProfessorDiaVO = new HorarioProfessorDiaVO();
		}
		return horarioProfessorDiaVO;
	}

	/**
	 * @param horarioProfessorDiaVO the horarioProfessorDiaVO to set
	 */
	public void setHorarioProfessorDiaVO(HorarioProfessorDiaVO horarioProfessorDiaVO) {
		this.horarioProfessorDiaVO = horarioProfessorDiaVO;
	}

	public HorarioTurmaDiaItemVO getHorarioTurmaDiaItemVO() {
		if (horarioTurmaDiaItemVO == null) {
			horarioTurmaDiaItemVO = new HorarioTurmaDiaItemVO();
		}
		return horarioTurmaDiaItemVO;
	}

	public void setHorarioTurmaDiaItemVO(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO) {
		this.horarioTurmaDiaItemVO = horarioTurmaDiaItemVO;
	}

	public UsuarioVO getUsuarioLiberacaoChoqueHorario() {
		if (usuarioLiberacaoChoqueHorario == null) {
			usuarioLiberacaoChoqueHorario = new UsuarioVO();
		}
		return usuarioLiberacaoChoqueHorario;
	}

	public void setUsuarioLiberacaoChoqueHorario(UsuarioVO usuarioLiberacaoChoqueHorario) {
		this.usuarioLiberacaoChoqueHorario = usuarioLiberacaoChoqueHorario;
	}

	public String getHorarioModulo() {
        return Uteis.getDiaSemana_Apresentar(getData())+" - "+String.valueOf(nrAula) + "º Aula - " + getHorario();
    }
	
	public boolean isHorarioProfessorDiaItemDentroDoLimiteFinalDeRealizacao() throws ParseException {
		if(!Uteis.isAtributoPreenchido(getData()) 
				|| !Uteis.isAtributoPreenchido(getHorarioTermino())
					|| !getHorarioTermino().contains(":")
					|| getHorarioTermino().length() != 5) {
			return false;
		}
		int hora =  Integer.parseInt(getHorarioTermino().substring(0, getHorarioTermino().indexOf(":")));
		int minuto =  Integer.parseInt(getHorarioTermino().substring(getHorarioTermino().indexOf(":")+1, getHorarioTermino().length()));
		Date dataTermino = UteisData.getDateTime(getData(), hora, minuto, 0); 
		return Uteis.isAtributoPreenchido(dataTermino) &&  UteisData.validarDataInicialMaiorFinalComHora(dataTermino , new Date());
	}

	public String getConteudoRegistroAulaAutomatico() {
		if (conteudoRegistroAulaAutomatico == null) {
			conteudoRegistroAulaAutomatico = "";
		}
		return conteudoRegistroAulaAutomatico;
	}

	public void setConteudoRegistroAulaAutomatico(String conteudoRegistroAulaAutomatico) {
		this.conteudoRegistroAulaAutomatico = conteudoRegistroAulaAutomatico;
	}
	
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public Boolean getRegistroAulaAutomaticoSucesso() {
		return registroAulaAutomaticoSucesso;
	}

	public void setRegistroAulaAutomaticoSucesso(Boolean registroAulaAutomaticoSucesso) {
		this.registroAulaAutomaticoSucesso = registroAulaAutomaticoSucesso;
	}

	public String getMotivoErroRegistroAulaAutomatico() {
		if (motivoErroRegistroAulaAutomatico == null) {
			motivoErroRegistroAulaAutomatico = "";
		}
		return motivoErroRegistroAulaAutomatico;
	}

	public void setMotivoErroRegistroAulaAutomatico(String motivoErroRegistroAulaAutomatico) {
		this.motivoErroRegistroAulaAutomatico = motivoErroRegistroAulaAutomatico;
	}	
	
	
	
}
