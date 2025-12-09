package negocio.comuns.protocolo;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade RegistroEntrada. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
*/

@XmlRootElement(name = "tipoRequerimentoDepartamentoVO")
public class TipoRequerimentoDepartamentoVO extends SuperVO {
	   
	private static final long serialVersionUID = -5315376904329407420L;
	private Integer codigo;
    private Integer tipoRequerimento;
    private DepartamentoVO departamento;
    private PessoaVO responsavelRequerimentoDepartamento;
    private Integer prazoExecucao;
    private Integer ordemExecucao;
    private Boolean observacaoObrigatoria;
    private Boolean podeIndeferirRequerimento;
    private TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavel;
    private TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao;
    private CargoVO cargo;
    private QuestionarioVO questionario;
    private String orientacaoDepartamento;
    private List<TipoRequerimentoDepartamentoFuncionarioVO> tipoRequerimentoDepartamentoFuncionarioVOs;   
    private List<TipoRequerimentoSituacaoDepartamentoVO> tipoRequerimentoSituacaoDepartamentoVOs;
    private Boolean podeInserirNota;
    private Double notaMaxima;

    
    /**
     * Construtor padrão da classe <code>ProspectIndicacaoVO</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public TipoRequerimentoDepartamentoVO() {
        super();
    }
    
    public static void validarDados(TipoRequerimentoDepartamentoVO obj) throws ConsistirException {
        if ((obj.getDepartamento() == null) || (obj.getDepartamento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DEPARTAMENTO (TipoRequerimentoDepartamento) deve ser informado.");
        }
        if ((obj.getPrazoExecucao() == null) || (obj.getPrazoExecucao().compareTo(0) <= 0)) {
            throw new ConsistirException("O campo PRAZO EXECUÇÃO (TipoRequerimentoDepartamento) deve ser informado.");
        }
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

    /**
     * @return the tipoRequerimento
     */
    public Integer getTipoRequerimento() {
        if (tipoRequerimento == null) {
            tipoRequerimento = 0;
        }
        return tipoRequerimento;
    }

    /**
     * @param tipoRequerimento the tipoRequerimento to set
     */
    public void setTipoRequerimento(Integer tipoRequerimento) {
        this.tipoRequerimento = tipoRequerimento;
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
     * @return the prazoExecucao
     */
    public Integer getPrazoExecucao() {
        if (prazoExecucao == null) {
            prazoExecucao = 0;
        }
        return prazoExecucao;
    }

    /**
     * @param prazoExecucao the prazoExecucao to set
     */
    public void setPrazoExecucao(Integer prazoExecucao) {
        this.prazoExecucao = prazoExecucao;
    }

    /**
     * @return the ordemExecucao
     */
    @XmlElement(name = "ordemExecucao")
    public Integer getOrdemExecucao() {
        if (ordemExecucao == null) {
            ordemExecucao = 0;
        }
        return ordemExecucao;
    }

    /**
     * @param ordemExecucao the ordemExecucao to set
     */
    public void setOrdemExecucao(Integer ordemExecucao) {
        this.ordemExecucao = ordemExecucao;
    }

    /**
     * @return the observacaoObrigatoria
     */
    public Boolean getObservacaoObrigatoria() {
        if (observacaoObrigatoria == null) {
            observacaoObrigatoria = Boolean.FALSE;
        }
        return observacaoObrigatoria;
    }

    /**
     * @param observacaoObrigatoria the observacaoObrigatoria to set
     */
    public void setObservacaoObrigatoria(Boolean observacaoObrigatoria) {
        this.observacaoObrigatoria = observacaoObrigatoria;
    }

    /**
     * @return the podeIndeferirRequerimento
     */
    public Boolean getPodeIndeferirRequerimento() {
        if (podeIndeferirRequerimento == null) {
            podeIndeferirRequerimento = Boolean.TRUE;
        }
        return podeIndeferirRequerimento;
    }

    /**
     * @param podeIndeferirRequerimento the podeIndeferirRequerimento to set
     */
    public void setPodeIndeferirRequerimento(Boolean podeIndeferirRequerimento) {
        this.podeIndeferirRequerimento = podeIndeferirRequerimento;
    }
    
    public boolean getIsExigePoliticaDistribuicao(){
    	return getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) ||
    			getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_DEPARTAMENTO);
    }
    
    public boolean getIsInformarFuncionarioEspecifico(){
    	return getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO);
    }
    
    public boolean getIsInformarFuncionarioEspecificoTramite(){
    	return getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO_NO_TRAMITE);
    }
    
    public boolean getIsExigeCargo(){
    	return getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO);
    }

    @XmlElement(name = "tipoDistribuicaoResponsavel")
	public TipoDistribuicaoResponsavelEnum getTipoDistribuicaoResponsavel() {
		if(tipoDistribuicaoResponsavel == null){
			tipoDistribuicaoResponsavel = TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO;
		}
		return tipoDistribuicaoResponsavel;
	}

	public void setTipoDistribuicaoResponsavel(TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavel) {
		this.tipoDistribuicaoResponsavel = tipoDistribuicaoResponsavel;
	}

	public TipoPoliticaDistribuicaoEnum getTipoPoliticaDistribuicao() {
		if(tipoPoliticaDistribuicao == null){
			tipoPoliticaDistribuicao = TipoPoliticaDistribuicaoEnum.DISTRIBUICAO_CIRCULAR;
		}
		return tipoPoliticaDistribuicao;
	}

	public void setTipoPoliticaDistribuicao(TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao) {
		this.tipoPoliticaDistribuicao = tipoPoliticaDistribuicao;
	}

	public CargoVO getCargo() {
		if(cargo == null){
			cargo = new CargoVO();
		}
		return cargo;
	}

	public void setCargo(CargoVO cargo) {
		this.cargo = cargo;
	}

	
	@XmlElement(name = "questionario")
	public QuestionarioVO getQuestionario() {
		if (questionario == null) {
			questionario = new QuestionarioVO();
		}
		return questionario;
	}

	public void setQuestionario(QuestionarioVO questionario) {
		this.questionario = questionario;
	}

	public String getOrientacaoDepartamento() {
		if (orientacaoDepartamento == null) {
			orientacaoDepartamento = "";
		}
		return orientacaoDepartamento;
	}

	public void setOrientacaoDepartamento(String orientacaoDepartamento) {
		this.orientacaoDepartamento = orientacaoDepartamento;
	}

	public List<TipoRequerimentoDepartamentoFuncionarioVO> getTipoRequerimentoDepartamentoFuncionarioVOs() {
		if (tipoRequerimentoDepartamentoFuncionarioVOs == null) {
			tipoRequerimentoDepartamentoFuncionarioVOs = new ArrayList<TipoRequerimentoDepartamentoFuncionarioVO>(0);
		}
		return tipoRequerimentoDepartamentoFuncionarioVOs;
	}

	public void setTipoRequerimentoDepartamentoFuncionarioVOs(List<TipoRequerimentoDepartamentoFuncionarioVO> tipoRequerimentoDepartamentoFuncionarioVOs) {
		this.tipoRequerimentoDepartamentoFuncionarioVOs = tipoRequerimentoDepartamentoFuncionarioVOs;
	}
     

	public Boolean getIsInformarListaFuncionario() {
		return getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO);
	}

	public List<TipoRequerimentoSituacaoDepartamentoVO> getTipoRequerimentoSituacaoDepartamentoVOs() {
		if (tipoRequerimentoSituacaoDepartamentoVOs == null) {
			tipoRequerimentoSituacaoDepartamentoVOs = new ArrayList<TipoRequerimentoSituacaoDepartamentoVO>(0);
		}
		return tipoRequerimentoSituacaoDepartamentoVOs;
	}

	public void setTipoRequerimentoSituacaoDepartamentoVOs(List<TipoRequerimentoSituacaoDepartamentoVO> tipoRequerimentoSituacaoDepartamentoVOs) {
		this.tipoRequerimentoSituacaoDepartamentoVOs = tipoRequerimentoSituacaoDepartamentoVOs;
	}

	@XmlElement(name = "podeInserirNota")
	public Boolean getPodeInserirNota() {
		if (podeInserirNota == null) {
			podeInserirNota = Boolean.FALSE;
		}
		return podeInserirNota;
	}

	public void setPodeInserirNota(Boolean podeInserirNota) {
		this.podeInserirNota = podeInserirNota;
	}

	public Double getNotaMaxima() {
		return notaMaxima;
	}

	public void setNotaMaxima(Double notaMaxima) {
		this.notaMaxima = notaMaxima;
	}
}