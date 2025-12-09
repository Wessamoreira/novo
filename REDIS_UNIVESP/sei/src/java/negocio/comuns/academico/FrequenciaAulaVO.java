package negocio.comuns.academico;

import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.RegistroAula;

/**
 * Reponsável por manter os dados da entidade FrequenciaAula. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see RegistroAula
 */
@XmlRootElement(name = "frequenciaAula")
public class FrequenciaAulaVO extends SuperVO {

    private Boolean presente;
    private Boolean abonado;
    private Boolean justificado;
    private Boolean bloqueadoDevidoDataMatricula;
    
    /**
     * TRANSIENT - UTILIZADO SOMENTE PARA CONTROLE DE ABONO DURANTE O REGISTRO DA AULA
     */
    private DisciplinaAbonoVO disciplinaAbonoVO;
    
    private Boolean editavel;
    private Integer registroAula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO matricula;
    private String turma;
    /**
     * Atributo Transiente que somente será usado para a tela de registro de aula/nota
     * na visão do professor para turmas de Pós-Graduação.
     */
    private HistoricoVO historicoVO;
    private String cssInputTextRegistroAulaNota;
    private String cssSelectBooleanCheckboxRegistroAulaNota;
    private Boolean frequenciaOculta;
    private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
    //Transient
    private Date dataRegistroAula;
    private AbonoFaltaVO abonoFaltaVO;
    public static final long serialVersionUID = 1L;

    
    /*
     * Transiente usado no diário
     */        
    private Integer faltasGeral;
    
    /*
     * Como o booleano de novoObj do superVO não é enviado no xml do aplicativo, foi criado este apenas para ser enviado no xml do mobile.
     */    
    private Boolean novoObjetoMobile;
    private Boolean removerAbono;
    
	/**
     * Construtor padrão da classe <code>FrequenciaAula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FrequenciaAulaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setPresente(Boolean.TRUE);
        setEditavel(Boolean.TRUE);
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>FrequenciaAula</code>).
     */
    @XmlElement(name = "matricula")
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    @XmlElement(name = "editavel")
    public Boolean getEditavel() {
        return editavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>FrequenciaAula</code>).
     */
    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    @XmlElement(name = "registroAula")
    public Integer getRegistroAula() {
        return (registroAula);
    }

    public void setRegistroAula(Integer registroAula) {
        this.registroAula = registroAula;
    }

    @XmlElement(name = "presente")
    public Boolean getPresente() {
    	if (presente == null) {
    		presente = Boolean.FALSE;
    	}
        return (presente);
    }

    @XmlElement(name = "ispresente")
    public Boolean isPresente() {
    	if (presente == null) {
    		presente = Boolean.FALSE;
    	}
        return (presente);
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public String getOrdenacao() {        
           return getMatricula().getAluno().getNome();        
    }

    public String getOrdenacaoSemAcentuacaoNome() {        
           return Uteis.removerAcentuacao(getMatricula().getAluno().getNome());        
    }

    public String getOrdenacaoSemAcentuacaoNomeResumido() {
        if (getMatricula().getTipoMatricula().equals("EX")) {
        return "ZZ" +  Uteis.removerAcentuacao(getMatricula().getAluno().getNomeResumido());
        } else {
            return Uteis.removerAcentuacao(getMatricula().getAluno().getNomeResumido());
        }
    }

    public void setHistoricoVO(HistoricoVO historicoVO) {
        this.historicoVO = historicoVO;
    }

    //@XmlElement(name = "historico")
    public HistoricoVO getHistoricoVO() {
        if (historicoVO == null) {
            historicoVO = new HistoricoVO();
        }
        return historicoVO;
    }

    /**
     * @return the turma
     */
    @XmlElement(name = "turma")
    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(String turma) {
        this.turma = turma;
    }

    @XmlElement(name = "abonado")
    public Boolean getAbonado() {
        if (abonado == null) {
            abonado = Boolean.FALSE;
        }
        return abonado;
    }

    public void setAbonado(Boolean abonado) {
        this.abonado = abonado;
    }

    @XmlElement(name = "justificado")
    public Boolean getJustificado() {
        if (justificado == null) {
            justificado = Boolean.FALSE;
        }
        return justificado;
    }

    public void setJustificado(Boolean justificado) {
        this.justificado = justificado;
    }

    public Boolean getDesabilitarCampoFrequenciaAbonoFalta() {
        if (getAbonado() || getJustificado() || getFrequenciaOculta()) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarMensagemAbonaFalta() {
        return getAbonado() || getJustificado();
    }

    public String getMensagemAbonoFalta() {
        if (getAbonado()) {
            return "Frequência Abonada";
        }
        if (getJustificado()) {
            return "Frequência Justificada";
        }        
        return "";
    }
    
    public String getMensagemBloqueadoDevidoDataMatricula() {        
        if(getBloqueadoDevidoDataMatricula()) {
        	return "Data Inclusão Histórico Posterior a Data Aula ("+Uteis.getData(getHistoricoVO().getDataRegistro())+")";
        }
        return "";
    }
    
    public boolean isNaoRegistarFrequenciaPorSituacao(){
    	return  getHistoricoVO().getMatricula().getSituacao().equals("TS"); 
    }

    public String getCssInputTextRegistroAulaNota() {
        if (cssInputTextRegistroAulaNota == null) {
            return "camposReduzidosVisaoProfessor";
        }
        return cssInputTextRegistroAulaNota;
    }

    public void setCssInputTextRegistroAulaNota(String cssInputTextRegistroAulaNota) {
        this.cssInputTextRegistroAulaNota = cssInputTextRegistroAulaNota;
    }

    public String getCssSelectBooleanCheckboxRegistroAulaNota() {
        if (cssSelectBooleanCheckboxRegistroAulaNota == null) {
            cssSelectBooleanCheckboxRegistroAulaNota = "";
        }
        return cssSelectBooleanCheckboxRegistroAulaNota;
    }

    public void setCssSelectBooleanCheckboxRegistroAulaNota(String cssSelectBooleanCheckboxRegistroAulaNota) {
        this.cssSelectBooleanCheckboxRegistroAulaNota = cssSelectBooleanCheckboxRegistroAulaNota;
    }

    @XmlElement(name = "frequenciaOculta")
    public Boolean getFrequenciaOculta() {
        if (frequenciaOculta == null) {
            frequenciaOculta = false;
        }
        return frequenciaOculta;
    }

    public void setFrequenciaOculta(Boolean frequenciaOculta) {
        this.frequenciaOculta = frequenciaOculta;
    }


    /**
     * @return the disciplinaAbonoVO
     */
    @XmlElement(name = "disciplinaAbonoVO")
    public DisciplinaAbonoVO getDisciplinaAbonoVO() {
        if (disciplinaAbonoVO == null) {
            disciplinaAbonoVO = new DisciplinaAbonoVO();
        }
        return disciplinaAbonoVO;
    }

    /**
     * @param disciplinaAbonoVO the disciplinaAbonoVO to set
     */
    public void setDisciplinaAbonoVO(DisciplinaAbonoVO disciplinaAbonoVO) {
        this.disciplinaAbonoVO = disciplinaAbonoVO;
    }

    public Integer getMatriculaPeriodoTurmaDisciplina() {		
		return getMatriculaPeriodoTurmaDisciplinaVO().getCodigo();
	}

	public void setMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina) {
		getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(matriculaPeriodoTurmaDisciplina);
	}
    
    public Integer getFaltasGeral() {
    	if (faltasGeral == null) {
    		faltasGeral = 0;
    	}
		return faltasGeral;
	}

	public void setFaltasGeral(Integer faltasGeral) {
		this.faltasGeral = faltasGeral;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FrequenciaAulaVO [presente=" + presente + ", abonado=" + abonado + ", justificado=" + justificado + ", registroAula=" + registroAula + ", matricula=" + matricula + ", turma=" + turma + ", historicoVO=" + historicoVO + "]";
	}

    
	@XmlElement(name = "bloqueadoDevidoDataMatricula")
	public Boolean getBloqueadoDevidoDataMatricula() {
		if (bloqueadoDevidoDataMatricula == null) {
			bloqueadoDevidoDataMatricula = Boolean.FALSE;
		}
		return bloqueadoDevidoDataMatricula;
	}

	public void setBloqueadoDevidoDataMatricula(Boolean bloqueadoDevidoDataMatricula) {
		this.bloqueadoDevidoDataMatricula = bloqueadoDevidoDataMatricula;
	}

	/**
	 * @return the matriculaPeriodoTurmaDisciplinaVO
	 */
	@XmlElement(name = "matriculaPeriodoTurmaDisciplinaVO")
	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	/**
	 * @param matriculaPeriodoTurmaDisciplinaVO the matriculaPeriodoTurmaDisciplinaVO to set
	 */
	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	@XmlElement(name = "novoObjetoMobile")
	public Boolean getNovoObjetoMobile() {
		if (novoObjetoMobile == null) {
			novoObjetoMobile = true;
		}
		return novoObjetoMobile;
	}


	public void setNovoObjetoMobile(Boolean novoObjetoMobile) {
		this.novoObjetoMobile = novoObjetoMobile;
	}
	
	
	public Boolean getOcultarFrequenciaDevidoDataCancelamento() {
		try {
		if ((Uteis.isAtributoPreenchido(getHistoricoVO().getMatriculaPeriodo().getDataFechamentoMatriculaPeriodo()) 
				&& UteisData.getCompareData(Uteis.getDataJDBC(getHistoricoVO().getMatriculaPeriodo().getDataFechamentoMatriculaPeriodo()), Uteis.getDataJDBC(getDataRegistroAula())) < 0)
				&& (getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getValor())
				|| getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor())
				|| getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor())
				|| getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor())
				|| getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor())
				)) {
			frequenciaOculta = true;
			return true;
			} 
		} catch (ParseException e) {
			e.printStackTrace();
		}
			return false; 
		}
	
    public String getMensagemBloqueadoDevidoDataTrancamentoCancelamentoMatricula() {        
    	if(getOcultarFrequenciaDevidoDataCancelamento()) {
    		return "Data "+SituacaoMatriculaPeriodoEnum.getEnumPorValor(getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo()).getDescricaoComPrefixo()+" Anterior a Data Aula ("+Uteis.getData(getHistoricoVO().getMatriculaPeriodo().getDataFechamentoMatriculaPeriodo())+")";
    	}
    	return "";
    }

    public Date getDataRegistroAula() {
    	if (dataRegistroAula == null) {
    		dataRegistroAula = new Date();
    	}
		return dataRegistroAula;
	}

	public void setDataRegistroAula(Date dataRegistroAula) {
		this.dataRegistroAula = dataRegistroAula;
	}
	
	
	public String getAlerta() {
		if(getApresentarMensagemAbonaFalta()) {
			return getMensagemAbonoFalta();
		}
		if(getOcultarFrequenciaDevidoDataCancelamento()) {
			return getMensagemBloqueadoDevidoDataTrancamentoCancelamentoMatricula();   
		}
		if(getBloqueadoDevidoDataMatricula()) {
			return getMensagemBloqueadoDevidoDataMatricula();
		}  
		if(!getMatricula().getSituacao().equals("AT")) {
			return getMatricula().getSituacao_Apresentar();
		}
		return Uteis.STRING_VAZIA;
	}

	public AbonoFaltaVO getAbonoFaltaVO() {
		if (abonoFaltaVO == null) {
			abonoFaltaVO = new AbonoFaltaVO();
		}
		return abonoFaltaVO;
	}

	public void setAbonoFaltaVO(AbonoFaltaVO abonoFaltaVO) {
		this.abonoFaltaVO = abonoFaltaVO;
	}
	
	

	
	public Boolean getRemoverAbono() {
		return removerAbono;
	}

	public void setRemoverAbono(Boolean removerAbono) {
		this.removerAbono = removerAbono;
	}
	
}
