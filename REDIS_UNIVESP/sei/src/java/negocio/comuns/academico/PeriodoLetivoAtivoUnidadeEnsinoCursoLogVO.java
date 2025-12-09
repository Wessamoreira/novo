package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class PeriodoLetivoAtivoUnidadeEnsinoCursoLogVO extends SuperVO {

	private Integer codigo;
    private String situacao;
    private CursoVO cursoVO;
    private TurnoVO turnoVO;
    private String semestreReferenciaPeriodoLetivo;
    private String anoReferenciaPeriodoLetivo;
    private String tipoPeriodoLetivo;
    private Date dataInicioPeriodoLetivo;
    private Date dataFimPeriodoLetivo;
    private Date dataAbertura;
    private UsuarioVO reponsavelAbertura;
    private Date dataFechamento;
    private UsuarioVO responsavelFechamento;
    private TurmaVO turma;
    private String operacao;
    private UsuarioVO responsavel;
    private Integer codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO;
    
    public PeriodoLetivoAtivoUnidadeEnsinoCursoLogVO() {
    	inicializarDados();
    }
    
    public void inicializarDados() {
        setCodigo(0);
        setSituacao("AT");
        setSemestreReferenciaPeriodoLetivo("");
        setAnoReferenciaPeriodoLetivo("");
        setTipoPeriodoLetivo("");
        setDataAbertura(new Date());
        setDataFechamento(new Date());
    }
    
    public UsuarioVO getResponsavelFechamento() {
        if (responsavelFechamento == null) {
            responsavelFechamento = new UsuarioVO();
        }
        return (responsavelFechamento);
    }

    public void setResponsavelFechamento(UsuarioVO responsavelFechamento) {
        this.responsavelFechamento = responsavelFechamento;
    }

    public Date getDataFechamento() {
        return (dataFechamento);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataFechamento_Apresentar() {
        return (Uteis.getData(dataFechamento));
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public UsuarioVO getReponsavelAbertura() {
        if (reponsavelAbertura == null) {
            reponsavelAbertura = new UsuarioVO();
        }
        return (reponsavelAbertura);
    }

    public void setReponsavelAbertura(UsuarioVO reponsavelAbertura) {
        this.reponsavelAbertura = reponsavelAbertura;
    }

    public Date getDataAbertura() {
        return (dataAbertura);
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataFimPeriodoLetivo() {
        return (dataFimPeriodoLetivo);
    }

    public void setDataFimPeriodoLetivo(Date dataFimPeriodoLetivo) {
        this.dataFimPeriodoLetivo = dataFimPeriodoLetivo;
    }

    public Date getDataInicioPeriodoLetivo() {
        return (dataInicioPeriodoLetivo);
    }

    public void setDataInicioPeriodoLetivo(Date dataInicioPeriodoLetivo) {
        this.dataInicioPeriodoLetivo = dataInicioPeriodoLetivo;
    }


    public String getTipoPeriodoLetivo() {
        if (tipoPeriodoLetivo == null) {
            tipoPeriodoLetivo = "";
        }
        return (tipoPeriodoLetivo);
    }

    public void setTipoPeriodoLetivo(String tipoPeriodoLetivo) {
        this.tipoPeriodoLetivo = tipoPeriodoLetivo;
    }

    public String getAnoReferenciaPeriodoLetivo() {
        return (anoReferenciaPeriodoLetivo);
    }

    public void setAnoReferenciaPeriodoLetivo(String anoReferenciaPeriodoLetivo) {
        this.anoReferenciaPeriodoLetivo = anoReferenciaPeriodoLetivo;
    }

    public String getSemestreReferenciaPeriodoLetivo() {
        return (semestreReferenciaPeriodoLetivo);
    }

    public void setSemestreReferenciaPeriodoLetivo(String semestreReferenciaPeriodoLetivo) {
        this.semestreReferenciaPeriodoLetivo = semestreReferenciaPeriodoLetivo;
    }


    public String getSituacao() {
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

	public String getOperacao() {
		if (operacao == null) {
			operacao = "";
		}
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Integer getCodigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO() {
		if (codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO == null) {
			codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO = 0;
		}
		return codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public void setCodigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO(
			Integer codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO) {
		this.codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO = codigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO =  new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if(turnoVO == null) {
			turnoVO =  new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}
	
	

}
