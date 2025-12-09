package negocio.comuns.academico;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.JsonDateDeserializer;
import negocio.comuns.arquitetura.JsonDateSerializer;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class HorarioTurmaDiaItemVO extends SuperVO implements Serializable {

	private Integer codigo;
    private Integer nrAula;
    private Integer duracaoAula;
    private String horario;
    private String horarioInicio;
    private String horarioTermino;
    private PessoaVO funcionarioVO;
    private DisciplinaVO disciplinaVO;
    private SalaLocalAulaVO sala;
    private HorarioTurmaDiaVO horarioTurmaDiaVO;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date data;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private FuncionarioCargoVO funcionarioCargoVO;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean gerarEventoAulaOnLineGoogleMeet;
    @ExcluirJsonAnnotation    
    @JsonIgnore
    private GoogleMeetVO googleMeetVO;
    @ExcluirJsonAnnotation    
    @JsonIgnore
    private SalaAulaBlackboardVO salaAulaBlackboardVO;
    
    public static final long serialVersionUID = 1L;
    @ExcluirJsonAnnotation
    private Boolean possuiAulaRegistrada;
    /**
     * Variavel utilizada no controle de alteração da sala de aula
     */
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean possuiChoqueSala;    
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Boolean possuiChoqueAulaExcesso;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private Timestamp dataUltimaAlteracao;
    @ExcluirJsonAnnotation
    @JsonIgnore
    private String operacaoLog;
    
    private Boolean aulaReposicao;

    public HorarioTurmaDiaItemVO() {
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

    public boolean getProfessorLivre() {
        if (getFuncionarioVO().getCodigo().intValue() == 0) {
            return true;
        }
        return false;
    }

    public Boolean getIsHorarioOcupado() {
        if (getDisciplinaLivre() || getProfessorLivre()) {
            return false;
        }
        return true;
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

    public PessoaVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new PessoaVO();
        }
        return funcionarioVO;
    }

    public void setFuncionarioVO(PessoaVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

    public Integer getNrAula() {
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
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

	public Integer getDuracaoAula() {
		if(duracaoAula == null){
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

	public Boolean getPossuiAulaRegistrada() {
		if (possuiAulaRegistrada == null) {
			possuiAulaRegistrada = false;
		}
		return possuiAulaRegistrada;
	}

	public void setPossuiAulaRegistrada(Boolean possuiAulaRegistrada) {
		this.possuiAulaRegistrada = possuiAulaRegistrada;
	}

	/**
	 * @return the sala
	 */
	public SalaLocalAulaVO getSala() {
		if (sala == null) {
			sala = new SalaLocalAulaVO();
		}
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(SalaLocalAulaVO sala) {
		this.sala = sala;
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
	 * @return the horarioTurmaDiaVO
	 */
	public HorarioTurmaDiaVO getHorarioTurmaDiaVO() {
		if (horarioTurmaDiaVO == null) {
			horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		}
		return horarioTurmaDiaVO;
	}

	/**
	 * @param horarioTurmaDiaVO the horarioTurmaDiaVO to set
	 */
	public void setHorarioTurmaDiaVO(HorarioTurmaDiaVO horarioTurmaDiaVO) {
		this.horarioTurmaDiaVO = horarioTurmaDiaVO;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
//		if (data == null) {
//			data = new Date();
//		}
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the possuiChoqueSala
	 */
	public Boolean getPossuiChoqueSala() {
		if (possuiChoqueSala == null) {
			possuiChoqueSala = false;
		}
		return possuiChoqueSala;
	}

	/**
	 * @param possuiChoqueSala the possuiChoqueSala to set
	 */
	public void setPossuiChoqueSala(Boolean possuiChoqueSala) {
		this.possuiChoqueSala = possuiChoqueSala;
	}

	/**
	 * @return the possuiChoqueAulaExcesso
	 */
	public Boolean getPossuiChoqueAulaExcesso() {
		if (possuiChoqueAulaExcesso == null) {
			possuiChoqueAulaExcesso = false;
		}
		return possuiChoqueAulaExcesso;
	}

	/**
	 * @param possuiChoqueAulaExcesso the possuiChoqueAulaExcesso to set
	 */
	public void setPossuiChoqueAulaExcesso(Boolean possuiChoqueAulaExcesso) {
		this.possuiChoqueAulaExcesso = possuiChoqueAulaExcesso;
	}

	public Timestamp getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null)
			dataUltimaAlteracao = Uteis.getDataJDBCTimestamp(new Date());
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Timestamp dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public String getOperacaoLog() {
		if (operacaoLog == null) {
			operacaoLog = "";
		}
		return operacaoLog;
	}

	public void setOperacaoLog(String operacaoLog) {
		this.operacaoLog = operacaoLog;
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
	
	public GoogleMeetVO getGoogleMeetVO() {
		if (googleMeetVO == null) {
			googleMeetVO = new GoogleMeetVO();
		}
		return googleMeetVO;
	}

	public void setGoogleMeetVO(GoogleMeetVO googleMeetVO) {
		this.googleMeetVO = googleMeetVO;
	}
	
	
	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public Date getDataHorarioTermino() {
		if(!Uteis.isAtributoPreenchido(getData()) 
				|| !Uteis.isAtributoPreenchido(getHorarioTermino())
					|| !getHorarioTermino().contains(":")
					|| getHorarioTermino().length() != 5) {
			return getData();
		}
		int hora =  Integer.parseInt(getHorarioTermino().substring(0, getHorarioTermino().indexOf(":")));
		int minuto =  Integer.parseInt(getHorarioTermino().substring(getHorarioTermino().indexOf(":")+1, getHorarioTermino().length()));
		return UteisData.getDateTime(getData(), hora, minuto, 0);
	}
	
	public boolean isHorarioTurmaDiaItemDentroDoLimiteFinalDeRealizacao() throws ParseException {
		return UteisData.validarDataInicialMaiorFinalComHora(getDataHorarioTermino() , new Date());
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