package negocio.comuns.administrativo;

import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.SuperVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.administrativo.Cargo;

/**
 * Reponsável por manter os dados da entidade FuncionarioCargo. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see Cargo
 */
public class FuncionarioCargoVO extends SuperVO {

	private static final long serialVersionUID = -8849304728091667158L;

    private Integer codigo;
    private FuncionarioVO funcionarioVO;
    private CargoVO cargo;
    private UnidadeEnsinoVO unidade;
    private Boolean consultor;
    private Boolean gerente;
    private Boolean ativo;
    
    //RH - Folha de Pagamento
    private String matriculaCargo;
    private Date dataAdmissao;
    private Date dataDemissao;
    private TipoRecebimentoEnum tipoRecebimento;
    private FormaContratacaoFuncionarioEnum formaContratacao;
    private String situacaoFuncionario;
    private Integer jornada;
    private BigDecimal salario;
    private Integer quantidadeCargoFuncionario;
    private Boolean utilizaRH;
    
    private Boolean comissionado;
    private CargoVO cargoAtual;
 
    
    private Date dataBaseQuinquenio;
    private PrevidenciaEnum previdencia;
    private Boolean optanteTotal;
    private DepartamentoVO departamento;
    private BigDecimal salarioCargoAtual;
    

    private Boolean itemEmEdicao;
    private Boolean salarioComposto;
    
    private CursoVO cursoVO;
    
  //transient - nao salva no BD
    private Date inicioGozoFerias;
    private Date finalGozoFerias;
   
	
    private Boolean filtrarFuncionarioCargo;

    /**
     * Construtor padrão da classe <code>FuncionarioCargo</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FuncionarioCargoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FuncionarioCargoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(FuncionarioCargoVO obj) throws ConsistirException {
    	
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (!Uteis.isAtributoPreenchido(obj.getCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_CargoNaoPreenchido"));
		}
//        if ((obj.getCargo().getControlaNivelExperiencia() && (obj.getNivelExperiencia().equals(NivelExperienciaCargoEnum.NENHUM)))) {
//            throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_CampoNivelExperienciaNaoPreenchido"));
//        }
    	if (!Uteis.isAtributoPreenchido(obj.getUnidade().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_UnidadeDeEnsinoNaoPreenchido"));
		}
    	if (obj.getDataDemissao() != null && !obj.getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.DEMITIDO.getValor())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_DataDemissaoESituacaoInvalido"));
		}
    	if (obj.getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.DEMITIDO.getValor()) && obj.getDataDemissao() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_SituacaoDemitidaEDataDemissaoNaoPreenchida"));
		}

    	if (!Uteis.isAtributoPreenchido(obj.getDataAdmissao())) {
    		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_DataAdimissaoNaoPreenchidos"));
    	}

        if(obj.getUtilizaRH()) {
        	
        	if (!Uteis.isAtributoPreenchido(obj.getSituacaoFuncionario())) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_SituacaoFuncionarioNaoPreenchidos"));
        	}
        	
        	if (!Uteis.isAtributoPreenchido(obj.getFormaContratacao())) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_FormaContratacaoNaoPreenchidos"));
        	}
        	
        	if (!Uteis.isAtributoPreenchido(obj.getTipoRecebimento())) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_TipoRecebimentoNaoPreenchidos"));
        	}
        	
        	if (!Uteis.isAtributoPreenchido(obj.getJornada())) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_JornadaNaoPreenchidos"));
        	}
        	
        	if (!Uteis.isAtributoPreenchido(obj.getSalario()) || obj.getSalario().equals(BigDecimal.ZERO)) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_SalarioNaoPreenchidos"));
        	}
        	
        }

        if (obj.getComissionado()) {
        	if (!Uteis.isAtributoPreenchido(obj.getCargoAtual().getCodigo())) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_CargoAtual"));
        	}

//        	if (!Uteis.isAtributoPreenchido(obj.getTipoContratacaoComissionado())) {
//        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_TipoComissionado"));
//        	}

        	if (!Uteis.isAtributoPreenchido(obj.getSalarioCargoAtual()) || obj.getSalarioCargoAtual().equals(BigDecimal.ZERO)) {
        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_SalarioCargoAtual"));
        	}
        }

        if (Uteis.isAtributoPreenchido(obj.getFormaContratacao()) && 
    		obj.getFormaContratacao().equals(FormaContratacaoFuncionarioEnum.ESTATUTARIO)) {

//        	if (!Uteis.isAtributoPreenchido(obj.getFaixaSalarial().getCodigo())) {
//        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_FaixaSalarial"));
//        	}
//        	
//        	if (!Uteis.isAtributoPreenchido(obj.getNivelSalarial().getCodigo())) {
//        		throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_NivelSalarial"));
//        	}
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }



    /**
     * @return the cargo
     */
    public CargoVO getCargo() {
        if (cargo == null) {
            cargo = new CargoVO();
        }
        return cargo;
    }

    /**
     * @param cargo
     *            the cargo to set
     */
    public void setCargo(CargoVO cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the unidade
     */
    public UnidadeEnsinoVO getUnidade() {
        if (unidade == null) {
            unidade = new UnidadeEnsinoVO();
        }
        return unidade;
    }

    /**
     * @param unidade
     *            the unidade to set
     */
    public void setUnidade(UnidadeEnsinoVO unidade) {
        this.unidade = unidade;
    }

    

    public FuncionarioVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new FuncionarioVO();
        }
        return funcionarioVO;
    }

    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

  

    public Boolean getConsultor() {
        if (consultor == null) {
            consultor = Boolean.FALSE;
        }
        return consultor;
    }

    public String getConsultorApresentar() {
    	return getCargo().getConsultorVendas() && getConsultor() ? "SIM" : "NÃO";
    }

    public String getGerenteApresentar() {
    	return getGerente() ? "SIM" : "NÃO";
    }
    
    public String getUtilizaRhApresentar() {
    	return getUtilizaRH() ? "SIM" : "NÃO";  
    }
    
    public String getDataAdmissaoApresentar() {
    	return (Uteis.getData(getDataAdmissao()));
    }
    
    public String getDataDemissaoApresentar() {
    	if(getDataDemissao() != null)
    		return (Uteis.getData(getDataDemissao()));
    	else
    		return "";
    }

    public void setConsultor(Boolean consultor) {
        this.consultor = consultor;
    }

    public Boolean getGerente() {
        if (gerente == null) {
            gerente = Boolean.FALSE;
        }
        return gerente;
    }

    public void setGerente(Boolean gerente) {
        this.gerente = gerente;
    }

	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = true;
		}
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public String getMatriculaCargo() {
		if (matriculaCargo == null)
			matriculaCargo = "";
		return matriculaCargo;
	}

	public Date getDataAdmissao() {
		if (dataAdmissao == null)
			dataAdmissao = new Date();
		return dataAdmissao;
	}

	public Date getDataDemissao() {
		return dataDemissao;
	}

	public TipoRecebimentoEnum getTipoRecebimento() {
		if(tipoRecebimento == null)
			tipoRecebimento = TipoRecebimentoEnum.MENSALISTA;
		return tipoRecebimento;
	}

	public FormaContratacaoFuncionarioEnum getFormaContratacao() {
		return formaContratacao;
	}

	public String getSituacaoFuncionario() {
		if (situacaoFuncionario == null)
			situacaoFuncionario = "";
		return situacaoFuncionario;
	}

	public Integer getJornada() {
		if (jornada == null)
			jornada = 0;
		return jornada;
	}

	public BigDecimal getSalario() {
		if (salario == null)
			salario = new BigDecimal(0);
		return salario;
	}

	public void setMatriculaCargo(String matriculaCargo) {
		this.matriculaCargo = matriculaCargo;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public void setTipoRecebimento(TipoRecebimentoEnum tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	public void setFormaContratacao(FormaContratacaoFuncionarioEnum formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public void setSituacaoFuncionario(String situacaoFuncionario) {
		this.situacaoFuncionario = situacaoFuncionario;
	}

	public void setJornada(Integer jornada) {
		this.jornada = jornada;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public Integer getQuantidadeCargoFuncionario() {
		if (quantidadeCargoFuncionario == null)
			quantidadeCargoFuncionario = 0;
		return quantidadeCargoFuncionario;
	}

	public void setQuantidadeCargoFuncionario(Integer quantidadeCargoFuncionario) {
		this.quantidadeCargoFuncionario = quantidadeCargoFuncionario;
	}

	public Boolean getUtilizaRH() {
		if (utilizaRH == null)
			utilizaRH = false;
		return utilizaRH;
	}

	public void setUtilizaRH(Boolean utilizaRH) {
		this.utilizaRH = utilizaRH;
	}
	
	public Boolean getComissionado() {
		if (comissionado == null) {
			comissionado = false;
		}
		return comissionado;
	}

	public void setComissionado(Boolean comissionado) {
		this.comissionado = comissionado;
	}

	

	public CargoVO getCargoAtual() {
		if (cargoAtual == null) {
			cargoAtual = new CargoVO();
		}
		return cargoAtual;
	}

	public void setCargoAtual(CargoVO cargoAtual) {
		this.cargoAtual = cargoAtual;
	}

	
	
	public String getSituacaoFuncionarioApresentar() {
		if(getSituacaoFuncionario().isEmpty())
			return "";
		
		return SituacaoFuncionarioEnum.getEnumPorName(getSituacaoFuncionario()).getDescricao();
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = false;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public Date getDataBaseQuinquenio() {
		return dataBaseQuinquenio;
	}

	public void setDataBaseQuinquenio(Date dataBaseQuinquenio) {
		this.dataBaseQuinquenio = dataBaseQuinquenio;
	}

	public PrevidenciaEnum getPrevidencia() {
		if(previdencia == null)
			previdencia = PrevidenciaEnum.INSS;
		return previdencia;
	}

	public void setPrevidencia(PrevidenciaEnum previdencia) {
		this.previdencia = previdencia;
	}

	public Boolean getOptanteTotal() {
		if (optanteTotal == null) {
			optanteTotal = false;
		}
		return optanteTotal;
	}

	public void setOptanteTotal(Boolean optanteTotal) {
		this.optanteTotal = optanteTotal;
	}

	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}
	
	

	public String getCargoExercidoFuncionarioCargo() {
		
		if(getCargoAtual().getNome().trim().isEmpty()) {
			return getCargo().getNome();
		} else {
			return getCargoAtual().getNome();
		}
		
	}
	
	public BigDecimal getSalarioCargoAtual() {
		if (salarioCargoAtual == null || salarioCargoAtual.intValue() == 0) {
			salarioCargoAtual = getSalario();
		}
		return salarioCargoAtual;
	}

	public void setSalarioCargoAtual(BigDecimal salarioCargoAtual) {
		this.salarioCargoAtual = salarioCargoAtual;
	}

	
	public Date getInicioGozoFerias() {
		return inicioGozoFerias;
	}

	public void setInicioGozoFerias(Date inicioGozoFerias) {
		this.inicioGozoFerias = inicioGozoFerias;
	}

	public Date getFinalGozoFerias() {
		return finalGozoFerias;
	}

	public void setFinalGozoFerias(Date finalGozoFerias) {
		this.finalGozoFerias = finalGozoFerias;
	}
	
	

	public Boolean getSalarioComposto() {
		if (salarioComposto == null) {
			salarioComposto = Boolean.FALSE;
		}
		return salarioComposto;
	}

	public void setSalarioComposto(Boolean salarioComposto) {
		this.salarioComposto = salarioComposto;
	}

	public Boolean getFiltrarFuncionarioCargo() {
		if (filtrarFuncionarioCargo == null) {
			filtrarFuncionarioCargo = Boolean.FALSE;
		}
		return filtrarFuncionarioCargo;
	}

	public void setFiltrarFuncionarioCargo(Boolean filtrarFuncionarioCargo) {
		this.filtrarFuncionarioCargo = filtrarFuncionarioCargo;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	
 }
