package negocio.comuns.protocolo;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.gson.annotations.Expose;

import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade Departamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */



@XmlRootElement(name = "requerimentoHistoricoVO")
public class RequerimentoHistoricoVO extends SuperVO {

    private Integer codigo;
    private Integer requerimento;
    private Date dataEntradaDepartamento;
    private Date dataInicioExecucaoDepartamento;
    private Date dataConclusaoDepartamento;
    private String observacaoDepartamento;
    private DepartamentoVO departamento;
    private PessoaVO responsavelRequerimentoDepartamento;
    private Boolean dptoResposanvelPeloIndeferimento;
    private Integer ordemExecucaoTramite;
    private QuestionarioVO questionario;
    private Boolean retorno;
    private String  motivoRetorno;
    private String  logAlteracaoSituacao;
    private Boolean enviouDepartamentoAnterior;
    private DepartamentoVO departamentoAnterior;
    private Integer ordemExecucaoTramiteAnterior;
    private SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO; 
    private Boolean abrirDetalheHistoricoRequerimento;
    private Double notaTCC;
    
    /**
     * transiente
     * */
  
	private List<MaterialRequerimentoVO> materialRequerimentoVOs;
	private List<InteracaoRequerimentoHistoricoVO> interacaoRequerimentoHistoricoVOs;
	@ExcluirJsonAnnotation
	@Expose(serialize = false, deserialize = false)
	private RequerimentoVO requerimentoVO;
	private Integer prazoExecucao;
	private Date previsaoDevolucao;
	private Boolean podeInserirNota;
    
    
    public static final long serialVersionUID = 1L;
    
    // Transiente
    private Boolean gravarRespostaQuestionario;
    private Boolean questionarioJaRespondido;

    /**
     * Construtor padrão da classe <code>Departamento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RequerimentoHistoricoVO() {
        super();
    }
    
    public static void validarDados(RequerimentoHistoricoVO obj) throws ConsistirException {
        if (obj.getRequerimento().equals(0)) {
            throw new ConsistirException("O campo REQUERIMENTO (RequerimentoHistorico) deve ser informado.");
        }
        if (obj.getDepartamento().getCodigo().equals(0)) {
            throw new ConsistirException("O campo DEPARTAMENTO (RequerimentoHistorico) deve ser informado.");
        }
    }

    /**
     * @return the requerimento
     */
    public Integer getRequerimento() {
        if (requerimento == null) {
            requerimento = 0;
        }
        return requerimento;
    }

    /**
     * @param requerimento the requerimento to set
     */
    public void setRequerimentoVO(RequerimentoVO requerimento) {
        this.setRequerimento(requerimento.getCodigo());
        this.requerimentoVO = requerimento;
    }
    
    public String getDataEntradaDepartamento_Apresentar() {
        if (dataEntradaDepartamento == null) {
            return "";
        }
        return Uteis.getData(dataEntradaDepartamento) + " " + Uteis.getHoraMinutoComMascara(dataEntradaDepartamento);
    }

    /**
     * @return the dataEntradaDepartamento
     */
    @XmlElement(name = "dataEntradaDepartamento")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataEntradaDepartamento() {
        return dataEntradaDepartamento;
    }

    /**
     * @param dataEntradaDepartamento the dataEntradaDepartamento to set
     */
    public void setDataEntradaDepartamento(Date dataEntradaDepartamento) {
        this.dataEntradaDepartamento = dataEntradaDepartamento;
    }

    /**
     * @return the observacaoDepartamento
     */
    public String getObservacaoDepartamento() {
        if (observacaoDepartamento == null) {
            observacaoDepartamento = "";
        }
        return observacaoDepartamento;
    }

    /**
     * @param observacaoDepartamento the observacaoDepartamento to set
     */
    public void setObservacaoDepartamento(String observacaoDepartamento) {
        this.observacaoDepartamento = observacaoDepartamento;
    }

    /**
     * @return the departamento
     */
    @XmlElement(name = "departamento")
    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the responsavelRequerimentoDepartamento
     */
    public PessoaVO getResponsavelRequerimentoDepartamento() {
        if (responsavelRequerimentoDepartamento == null) {
            responsavelRequerimentoDepartamento = new PessoaVO();
        }
        return responsavelRequerimentoDepartamento;
    }

    /**
     * @param responsavelRequerimentoDepartamento the responsavelRequerimentoDepartamento to set
     */
    public void setResponsavelRequerimentoDepartamento(PessoaVO responsavelRequerimentoDepartamento) {
        this.responsavelRequerimentoDepartamento = responsavelRequerimentoDepartamento;
    }

    /**
     * @return the resposanvelPeloIndeferimento
     */
    public Boolean getDptoResposanvelPeloIndeferimento() {
        if (dptoResposanvelPeloIndeferimento == null) {
            dptoResposanvelPeloIndeferimento = Boolean.FALSE;
        }
        return dptoResposanvelPeloIndeferimento;
    }

    /**
     * @param resposanvelPeloIndeferimento the resposanvelPeloIndeferimento to set
     */
    public void setDptoResposanvelPeloIndeferimento(Boolean dptoResposanvelPeloIndeferimento) {
        this.dptoResposanvelPeloIndeferimento = dptoResposanvelPeloIndeferimento;
    }

    /**
     * @return the codigo
     */
    @XmlElement(name = "codigo")
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
    
    public String getDataConclusaoDepartamento_Apresentar() {
        if (dataConclusaoDepartamento == null) {
            return "";
        }
        return Uteis.getData(dataConclusaoDepartamento) + " " + Uteis.getHoraMinutoComMascara(dataConclusaoDepartamento);
    }


    /**
     * @return the dataConclusaoDepartamento
     */
    public Date getDataConclusaoDepartamento() {
        return dataConclusaoDepartamento;
    }
    
    /**
     * @param dataConclusaoDepartamento the dataConclusaoDepartamento to set
     */
    public void setDataConclusaoDepartamento(Date dataConclusaoDepartamento) {
        this.dataConclusaoDepartamento = dataConclusaoDepartamento;
    }

    /**
     * @param requerimento the requerimento to set
     */
    public void setRequerimento(Integer requerimento) {
        this.requerimento = requerimento;
    }

    /**
     * @return the dataInicioExecucaoDepartamento
     */
    public Date getDataInicioExecucaoDepartamento() {
        return dataInicioExecucaoDepartamento;
    }
    
    public String getDataInicioExecucaoDepartamento_Apresentar() {
        if (dataInicioExecucaoDepartamento == null) {
            return "";
        }
        return Uteis.getData(dataInicioExecucaoDepartamento) + " " + Uteis.getHoraMinutoComMascara(dataInicioExecucaoDepartamento);
    }

    /**
     * @param dataInicioExecucaoDepartamento the dataInicioExecucaoDepartamento to set
     */
    public void setDataInicioExecucaoDepartamento(Date dataInicioExecucaoDepartamento) {
        this.dataInicioExecucaoDepartamento = dataInicioExecucaoDepartamento;
    }

	public QuestionarioVO getQuestionario() {
		if (questionario == null) {
			questionario = new QuestionarioVO();
		}
		return questionario;
	}

	public void setQuestionario(QuestionarioVO questionario) {
		this.questionario = questionario;
	}

	public Integer getOrdemExecucaoTramite() {
		if (ordemExecucaoTramite == null) {
			ordemExecucaoTramite = 0;
		}
		return ordemExecucaoTramite;
	}

	public void setOrdemExecucaoTramite(Integer ordemExecucaoTramite) {
		this.ordemExecucaoTramite = ordemExecucaoTramite;
	}

	public Boolean getGravarRespostaQuestionario() {
		if (gravarRespostaQuestionario == null) {
			gravarRespostaQuestionario = false;
		}
		return gravarRespostaQuestionario;
	}

	public void setGravarRespostaQuestionario(Boolean gravarRespostaQuestionario) {
		this.gravarRespostaQuestionario = gravarRespostaQuestionario;
	}

	public Boolean getQuestionarioJaRespondido() {
		if (questionarioJaRespondido == null) {
			questionarioJaRespondido = false;
		}
		return questionarioJaRespondido;
	}

	public void setQuestionarioJaRespondido(Boolean questionarioJaRespondido) {
		this.questionarioJaRespondido = questionarioJaRespondido;
	}

	public Boolean getRetorno() {
		if (retorno == null) {
			retorno = false;
		}
		return retorno;
	}

	public void setRetorno(Boolean retorno) {
		this.retorno = retorno;
	}

	public String getMotivoRetorno() {
		if (motivoRetorno == null) {
			motivoRetorno = "";
		}
		return motivoRetorno;
	}

	public void setMotivoRetorno(String motivoRetorno) {
		this.motivoRetorno = motivoRetorno;
	}

	public Boolean getEnviouDepartamentoAnterior() {
		if (enviouDepartamentoAnterior == null) {
			enviouDepartamentoAnterior = false;
		}
		return enviouDepartamentoAnterior;
	}

	public void setEnviouDepartamentoAnterior(Boolean enviouDepartamentoAnterior) {
		this.enviouDepartamentoAnterior = enviouDepartamentoAnterior;
	}

	public DepartamentoVO getDepartamentoAnterior() {
		if (departamentoAnterior == null) {
			departamentoAnterior = new DepartamentoVO();
		}
		return departamentoAnterior;
	}

	public void setDepartamentoAnterior(DepartamentoVO departamentoAnterior) {
		this.departamentoAnterior = departamentoAnterior;
	}

	public Integer getOrdemExecucaoTramiteAnterior() {
		if (ordemExecucaoTramiteAnterior == null) {
			ordemExecucaoTramiteAnterior = 0;
		}
		return ordemExecucaoTramiteAnterior;
	}

	public void setOrdemExecucaoTramiteAnterior(Integer ordemExecucaoTramiteAnterior) {
		this.ordemExecucaoTramiteAnterior = ordemExecucaoTramiteAnterior;
	}

	public SituacaoRequerimentoDepartamentoVO getSituacaoRequerimentoDepartamentoVO() {
		if (situacaoRequerimentoDepartamentoVO == null) {
			situacaoRequerimentoDepartamentoVO = new SituacaoRequerimentoDepartamentoVO();
		}
		return situacaoRequerimentoDepartamentoVO;
	}

	public void setSituacaoRequerimentoDepartamentoVO(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) {
		this.situacaoRequerimentoDepartamentoVO = situacaoRequerimentoDepartamentoVO;
	}

	public String getLogAlteracaoSituacao() {
		if (logAlteracaoSituacao == null) {
			logAlteracaoSituacao = "";
		}
		return logAlteracaoSituacao;
	}

	public void setLogAlteracaoSituacao(String logAlteracaoSituacao) {
		this.logAlteracaoSituacao = logAlteracaoSituacao;
	}
    
	public String getLogAlteracaoSituacaoApresentar() {		
		return getLogAlteracaoSituacao().replaceAll("/n", "</br>");
	}

	public List<MaterialRequerimentoVO> getMaterialRequerimentoVOs() {
		if (materialRequerimentoVOs == null) {
			materialRequerimentoVOs = new ArrayList<MaterialRequerimentoVO>(0);
		}
		return materialRequerimentoVOs;
	}

	public void setMaterialRequerimentoVOs(List<MaterialRequerimentoVO> materialRequerimentoVOs) {
		this.materialRequerimentoVOs = materialRequerimentoVOs;
	}

	@XmlElement(name = "interacaoRequerimentoHistoricoVOs")
	public List<InteracaoRequerimentoHistoricoVO> getInteracaoRequerimentoHistoricoVOs() {
		if(interacaoRequerimentoHistoricoVOs == null) {
			interacaoRequerimentoHistoricoVOs = new ArrayList<InteracaoRequerimentoHistoricoVO>(0);
		}
		return interacaoRequerimentoHistoricoVOs;
	}

	public void setInteracaoRequerimentoHistoricoVOs(
			List<InteracaoRequerimentoHistoricoVO> interacaoRequerimentoHistoricoVOs) {
		this.interacaoRequerimentoHistoricoVOs = interacaoRequerimentoHistoricoVOs;
	}

	public Boolean getAbrirDetalheHistoricoRequerimento() {
		if(abrirDetalheHistoricoRequerimento == null) {
			abrirDetalheHistoricoRequerimento = Boolean.TRUE;
		}
		return abrirDetalheHistoricoRequerimento;
	}

	public void setAbrirDetalheHistoricoRequerimento(Boolean abrirDetalheHistoricoRequerimento) {
		this.abrirDetalheHistoricoRequerimento = abrirDetalheHistoricoRequerimento;
	}

	public Double getNotaTCC() {
		return notaTCC;
	}

	public void setNotaTCC(Double notaTCC) {
		this.notaTCC = notaTCC;
	}

	public RequerimentoVO getRequerimentoVO() {
		if(requerimentoVO == null) {
			requerimentoVO =  new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public Integer getPrazoExecucao() {
		if(prazoExecucao == null) {
			prazoExecucao = 0;
		}
		return prazoExecucao;
	}

	public void setPrazoExecucao(Integer prazoExecucao) {
		this.prazoExecucao = prazoExecucao;
	}

	public Date getPrevisaoDevolucao() {
		if(previsaoDevolucao == null) {
			previsaoDevolucao = new Date();
		}
		return previsaoDevolucao;
	}

	public void setPrevisaoDevolucao(Date previsaoDevolucao) {
		this.previsaoDevolucao = previsaoDevolucao;
	}

	public String getCorrecaoDentroPrazo() {
		if(Uteis.isAtributoPreenchido(this.dataConclusaoDepartamento) && previsaoDevolucao.compareTo(dataConclusaoDepartamento) <= -1) {
			return "SIM";
		}else if(!Uteis.isAtributoPreenchido(this.dataConclusaoDepartamento)) {
			return "";
		}
		return "NÃO";
	}

	public Boolean getPodeInserirNota() {
		if(podeInserirNota == null) {
			podeInserirNota = Boolean.FALSE;
		}
		return podeInserirNota;
	}

	public void setPodeInserirNota(Boolean podeInserirNota) {
		this.podeInserirNota = podeInserirNota;
	}
	
}
