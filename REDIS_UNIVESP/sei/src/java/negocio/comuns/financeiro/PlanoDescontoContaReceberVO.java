package negocio.comuns.financeiro;

import java.util.Date;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.facade.jdbc.financeiro.ProvisaoCusto;

/**
 * Reponsável por manter os dados da entidade ItensProvisao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see ProvisaoCusto
 */
public class PlanoDescontoContaReceberVO extends SuperVO {

    private Integer codigo;
    private Integer contaReceber;
    private PlanoDescontoVO planoDescontoVO;    
    private Boolean utilizado;
    private String tipoItemPlanoFinanceiro; //"PD" - plano de desconto "CO" - convenio "DM" - plano de desconto manual
    private ConvenioVO convenio;
    /**
     * Atributo utilizado para armazenar o valor de um convenio/plano desconto
     * já calculado para uma determinada conta a receber. O mesmo é preenchido 
     * com este valor, mesmo antes da conta a receber ser recebida. Isto é importante
     * para os relatórios e cálculo de informações relativas à descontos no painel gestor.
     */
    private Double valorUtilizadoRecebimento;
    private boolean suspensoFinanciamentoProprio=false;
    private Boolean regerarConta =false;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ItensProvisao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PlanoDescontoContaReceberVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ItensProvisaoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static void validarDados(PlanoDescontoContaReceberVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTipoItemPlanoFinanceiro().equals("PD") && ((obj.getPlanoDescontoVO() == null) || (obj.getPlanoDescontoVO().getCodigo().intValue() == 0))) {
            throw new ConsistirException("O campo PLANO DESCONTO (Plano Desconto Conta Receber) deve ser informado.");
        }
        if (obj.getTipoItemPlanoFinanceiro().equals("DM") && obj.getValorDesconto().equals(0.0)) {
        	throw new ConsistirException("O campo DESCONTO (Plano Desconto Conta Receber) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
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
     * @return the contaReceber
     */
    public Integer getContaReceber() {
        if (contaReceber == null) {
            contaReceber = 0;
        }
        return contaReceber;
    }

    /**
     * @param contaReceber the contaReceber to set
     */
    public void setContaReceber(Integer contaReceber) {
        this.contaReceber = contaReceber;
    }

    /**
     * @return the planoDescontoVO
     */
    public PlanoDescontoVO getPlanoDescontoVO() {
        if (planoDescontoVO == null) {
            planoDescontoVO = new PlanoDescontoVO();
        }
        return planoDescontoVO;
    }

    /**
     * @param planoDescontoVO the planoDescontoVO to set
     */
    public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
        this.planoDescontoVO = planoDescontoVO;
    }

    /**
     * @return the utilizado
     */
    public Boolean getUtilizado() {
        if (utilizado == null) {
            utilizado = false;
        }
        return utilizado;
    }

    /**
     * @param utilizado the utilizado to set
     */
    public void setUtilizado(Boolean utilizado) {
        this.utilizado = utilizado;
    }

    /**
     * @return the tipoItemPlanoFinanceiro
     */
    public String getTipoItemPlanoFinanceiro() {
        if (tipoItemPlanoFinanceiro == null) {
            tipoItemPlanoFinanceiro = "";
        }
        return tipoItemPlanoFinanceiro;
    }

    /**
     * @param tipoItemPlanoFinanceiro the tipoItemPlanoFinanceiro to set
     */
    public void setTipoItemPlanoFinanceiro(String tipoItemPlanoFinanceiro) {
        this.tipoItemPlanoFinanceiro = tipoItemPlanoFinanceiro;
    }

    /**
     * @return the convenio
     */
    public ConvenioVO getConvenio() {
        if (convenio == null) {
            convenio = new ConvenioVO();
        }
        return convenio;
    }

    /**
     * @param convenio the convenio to set
     */
    public void setConvenio(ConvenioVO convenio) {
        this.convenio = convenio;
    }

    public Boolean getIsPlanoDescontoInstitucional() {
        if (this.getTipoItemPlanoFinanceiro().equals("PD")) { //"PD" - plano de desconto "CO" - convenio "DM" Desconto manual
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean getIsPlanoDescontoManual() {
    	if (this.getTipoItemPlanoFinanceiro().equals("DM")) { //"PD" - plano de desconto "CO" - convenio "DM" Desconto manual
            return true;
        } else {
            return false;
        }
    }

    public Boolean getIsConvenio() {
        if (this.getTipoItemPlanoFinanceiro().equals("CO")) { //"PD" - plano de desconto "CO" - convenio
            return true;
        } else {
            return false;
        }
    }

    public void setValorUtilizadoRecebimento(Double valorUtilizadoRecebimento) {
        this.valorUtilizadoRecebimento = valorUtilizadoRecebimento;
    }

    public Double getValorUtilizadoRecebimento() {
        if (valorUtilizadoRecebimento == null) {
            valorUtilizadoRecebimento = 0.0;
        }
        return valorUtilizadoRecebimento;
    }
    
    public String getOrdenacao(){
    	if (getIsConvenio()) {
    		return getConvenio().getOrdenacao();
    	} else  {
    		return getPlanoDescontoVO().getOrdenacao();
    	}
    }

    /**
     * INICIO MERGE EDIGAR 28-03-18
     */
    
    /**
     * Abaixo seguem atributos específicos do planoDescontoVO que podem ser alterados
     * em uma determinada conta a receber, quando a mesma estiver sendo editada manualmente.
     * Por isto, o mesmo é replicado nesta entidade, para que o valor gravado aqui sobreponha 
     * (prevaleça) sobre o valor padrão do PlanoDescontoVO. Por exemplo, se um plano de desconto
     * vence a 10 dias antes do vcto e ao editar a conta a receber o usuário diz que para esta conta
     * o desconto deve vencer 5 dias antes do vcto. Este numero 5 ficará salvo neste campo, prevalencendo
     * assim sobre o 10 informado no planoDesconto original.
     * 
     * Para o diasValidadeVencimento campo temos q considerar três possibilidades. Lembrando que este campo
     * na entidade PlanoDescontoContaReceberVO só será preenchido.
     * utilizarDiaFixo (true) : Neste caso esta data traz um dia fixo limite para o desconto. Neste caso, este dia
     *                          fixo poderá ser modificado pelo usuário na conta a receber em edicao;
     * utilizarDiaUtil (true) : Neste caso este campo representa quantos dias uteis antes do vencimento o desconto
     *                          permanece valido.
     * utilizarDiasAntesVctoPadrao (true) : Neste caso este campo representa quantos dias corridos antes do vencimento
     *                          o desconto permanece valido. 
     * 
     */
    private Integer diasValidadeVencimento;
    
    /**
     * Campo para registrar um tipo de desconto especifico, para um PlanoDescontoContaReceber do tipo DescontoManual
     */
    private TipoDescontoAluno tipoDesconto;
    /**
     * Campo para armazenar o valor ou percentual de desconto a ser utilizado em PlanoDescontoContaReceber do tipo DescontoManual
     */
    private Double valorDesconto;
    /**
     * Campo utilizado para armazenar a ordem de prioridade definida pelo usuário, que irá sobrepor a ordem definida
     * no plano de desconto padrao.
     */
    private Integer ordemPrioridadeParaCalculo;
    /**
     * Para contas a receber referente à bolsa custeada (vinculada a um parceiro conveniado) deve ser possível
     * informar em um desconto manual (o imposto a que se refere
     */
    private ImpostoVO imposto;
	private Boolean aplicarSobreValorCheio;
	private Boolean aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto; 
	private Boolean utilizarDiaUtil;
	private Boolean utilizarDiaFixo;
	 private Boolean utilizarAvancoDiaUtil;
	private Boolean descontoValidoAteDataVencimento;
	private UsuarioVO usuarioResponsavel;
	private Date dataConcessaoDesconto;
	private String observacaoDesconto;
	private String nome;

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public Date getDataConcessaoDesconto() {
		if (dataConcessaoDesconto == null) {
			dataConcessaoDesconto = new Date();
		}
		return dataConcessaoDesconto;
	}

	public void setDataConcessaoDesconto(Date dataConcessaoDesconto) {
		this.dataConcessaoDesconto = dataConcessaoDesconto;
	}

	public String getObservacaoDesconto() {
		if (observacaoDesconto == null) {
			observacaoDesconto = "";
		}
		return observacaoDesconto;
	}

	public void setObservacaoDesconto(String observacaoDesconto) {
		this.observacaoDesconto = observacaoDesconto;
	}
	
	public Integer getDiasValidadeVencimento() {
		if (diasValidadeVencimento == null) {
			//diasValidadeVencimento = 0;
		}
		return diasValidadeVencimento;
	}

	public void setDiasValidadeVencimento(Integer diasValidadeVencimento) {
		this.diasValidadeVencimento = diasValidadeVencimento;
	}

	public TipoDescontoAluno getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = TipoDescontoAluno.PORCENTO;
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(TipoDescontoAluno tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Integer getOrdemPrioridadeParaCalculo() {
		if (ordemPrioridadeParaCalculo == null) {
			ordemPrioridadeParaCalculo = 0;
		}
		return ordemPrioridadeParaCalculo;
	}

	public void setOrdemPrioridadeParaCalculo(Integer ordemPrioridadeParaCalculo) {
		this.ordemPrioridadeParaCalculo = ordemPrioridadeParaCalculo;
	}

	public ImpostoVO getImposto() {
		if (imposto == null) {
			imposto = new ImpostoVO();
		}
		return imposto;
	}

	public void setImposto(ImpostoVO imposto) {
		this.imposto = imposto;
	}
	
	
	public Boolean getAplicarSobreValorCheio() {
		if (aplicarSobreValorCheio == null) {
			aplicarSobreValorCheio = Boolean.FALSE;
		}
		return aplicarSobreValorCheio;
	}

	public void setAplicarSobreValorCheio(Boolean aplicarSobreValorCheio) {
		this.aplicarSobreValorCheio = aplicarSobreValorCheio;
	}

	public Boolean getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto() {
		if (aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto == null) {
			aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto = Boolean.TRUE;
		}
		return aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto;
	}

	public void setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(Boolean aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto) {
		this.aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto = aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto;
	}

	public Boolean getUtilizarDiaUtil() {
		if (utilizarDiaUtil == null) {
			utilizarDiaUtil = Boolean.FALSE;
		}
		return utilizarDiaUtil;
	}

	public void setUtilizarDiaUtil(Boolean utilizarDiaUtil) {
		this.utilizarDiaUtil = utilizarDiaUtil;
	}

	public Boolean getUtilizarAvancoDiaUtil() {
		if (utilizarAvancoDiaUtil == null) {
			utilizarAvancoDiaUtil = false;
		}
		return utilizarAvancoDiaUtil;
	}

	public void setUtilizarAvancoDiaUtil(Boolean utilizarAvancoDiaUtil) {
		this.utilizarAvancoDiaUtil = utilizarAvancoDiaUtil;
	}

	public Boolean getUtilizarDiaFixo() {
		if (utilizarDiaFixo == null) {
			utilizarDiaFixo = Boolean.FALSE;
		}
		return utilizarDiaFixo;
	}

	public void setUtilizarDiaFixo(Boolean utilizarDiaFixo) {
		this.utilizarDiaFixo = utilizarDiaFixo;
	}

	public Boolean getDescontoValidoAteDataVencimento() {
		if (descontoValidoAteDataVencimento == null) {
			descontoValidoAteDataVencimento = Boolean.FALSE;
		}
		return descontoValidoAteDataVencimento;
	}

	public void setDescontoValidoAteDataVencimento(Boolean descontoValidoAteDataVencimento) {
		this.descontoValidoAteDataVencimento = descontoValidoAteDataVencimento;
	}
	
	

	public boolean isSuspensoFinanciamentoProprio() {
		return suspensoFinanciamentoProprio;
	}

	public void setSuspensoFinanciamentoProprio(boolean suspensoFinanciamentoProprio) {
		this.suspensoFinanciamentoProprio = suspensoFinanciamentoProprio;
	}	
	
	public void setRegerarConta(Boolean regerarConta) {
        this.regerarConta = regerarConta;
    }

    public Boolean getRegerarConta() {
        if (regerarConta == null) {
            regerarConta = Boolean.FALSE;
        }
        return regerarConta;
    }

	public void inicializarAtributosPlanoDescontoComDadosDescontoManualContaReceber() {
		if (getIsPlanoDescontoManual()) {
			this.getPlanoDescontoVO().setNome(this.getNome());
			this.getPlanoDescontoVO().setDescricao("Desconto Registrado Manualmente");
			this.getPlanoDescontoVO().setTipoDescontoMatricula(this.getTipoDesconto().getValor().toString());
			this.getPlanoDescontoVO().setTipoDescontoParcela(this.getTipoDesconto().getValor().toString());
			this.getPlanoDescontoVO().setPercDescontoMatricula(this.getValorDesconto());
			this.getPlanoDescontoVO().setPercDescontoParcela(this.getValorDesconto());
			this.getPlanoDescontoVO().setDiasValidadeVencimento(this.getDiasValidadeVencimento());
			this.getPlanoDescontoVO().setAplicarSobreValorCheio(this.getAplicarSobreValorCheio());
			this.getPlanoDescontoVO().setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(this.getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto());
			this.getPlanoDescontoVO().setUtilizarDiaFixo(this.getUtilizarDiaFixo());
			this.getPlanoDescontoVO().setUtilizarAvancoDiaUtil(this.getUtilizarAvancoDiaUtil());
			this.getPlanoDescontoVO().setUtilizarDiaUtil(this.getUtilizarDiaUtil());
			this.getPlanoDescontoVO().setDescontoValidoAteDataVencimento(this.getDescontoValidoAteDataVencimento());
			this.getPlanoDescontoVO().setOrdemPrioridadeParaCalculo(this.getOrdemPrioridadeParaCalculo());
			this.getPlanoDescontoVO().setValorBaseAplicarDesconto(null);
			this.getPlanoDescontoVO().setDescricaoValidade(null);
			this.getPlanoDescontoVO().setAtivo(Boolean.TRUE);
		}else {
			if(!this.getPlanoDescontoVO().getUtilizarDescontoSemLimiteValidade()) {
				this.getPlanoDescontoVO().setDiasValidadeVencimento(this.getDiasValidadeVencimento());
			}
			this.setNome(this.getPlanoDescontoVO().getNome());
		}
	}
	

    /**
     * INICIO MERGE EDIGAR 15-05-18
     */
	private Boolean replicarPlanoDescontoOutrasContas;
	private Boolean planoDescontoJaExistenteContaReplicar;

	public Boolean getReplicarPlanoDescontoOutrasContas() {
		if (replicarPlanoDescontoOutrasContas == null) {
			replicarPlanoDescontoOutrasContas = Boolean.FALSE;
		}
		return replicarPlanoDescontoOutrasContas;
	}

	public void setReplicarPlanoDescontoOutrasContas(Boolean replicarPlanoDescontoOutrasContas) {
		this.replicarPlanoDescontoOutrasContas = replicarPlanoDescontoOutrasContas;
	}

	public Boolean getPlanoDescontoJaExistenteContaReplicar() {
		if (planoDescontoJaExistenteContaReplicar == null) {
			planoDescontoJaExistenteContaReplicar = Boolean.FALSE;
		}
		return planoDescontoJaExistenteContaReplicar;
	}

	public void setPlanoDescontoJaExistenteContaReplicar(Boolean planoDescontoJaExistenteContaReplicar) {
		this.planoDescontoJaExistenteContaReplicar = planoDescontoJaExistenteContaReplicar;
	}
	
    public PlanoDescontoContaReceberVO obterClone() {
    	PlanoDescontoContaReceberVO novoClone = new PlanoDescontoContaReceberVO();
    	novoClone.setCodigo(0);
    	novoClone.setNovoObj(Boolean.TRUE);
    	novoClone.setContaReceber(this.getContaReceber());
        novoClone.setPlanoDescontoVO(this.getPlanoDescontoVO());
        novoClone.setUtilizado(this.getUtilizado());
        novoClone.setTipoItemPlanoFinanceiro(this.getTipoItemPlanoFinanceiro());
        novoClone.setConvenio(this.getConvenio());
        novoClone.setValorUtilizadoRecebimento(this.getValorUtilizadoRecebimento());
        novoClone.setDiasValidadeVencimento(this.getDiasValidadeVencimento());
        novoClone.setTipoDesconto(this.getTipoDesconto());
        novoClone.setValorDesconto(this.getValorDesconto());
        novoClone.setOrdemPrioridadeParaCalculo(this.getOrdemPrioridadeParaCalculo());
        novoClone.setImposto(this.getImposto());
        novoClone.setAplicarSobreValorCheio(this.getAplicarSobreValorCheio());
        novoClone.setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(this.getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto());
        novoClone.setUtilizarDiaFixo(this.getUtilizarDiaFixo());
        novoClone.setUtilizarAvancoDiaUtil(this.getUtilizarAvancoDiaUtil());
        novoClone.setUtilizarDiaUtil(this.getUtilizarDiaUtil());
        novoClone.setDescontoValidoAteDataVencimento(this.getDescontoValidoAteDataVencimento());
        novoClone.setUsuarioResponsavel(null);
        novoClone.setDataConcessaoDesconto(new Date());
        novoClone.setObservacaoDesconto(this.getObservacaoDesconto());
        novoClone.setNome(this.getNome());
        novoClone.setSuspensoFinanciamentoProprio(this.isSuspensoFinanciamentoProprio());
    	return novoClone;
    }
    
    public String getTipoDesconto_Apresentar() {
    	return getTipoDesconto().getSimbolo();
    }
    
    public Boolean getUtilizarDescontoSemLimiteValidade() {
		if ((getTipoItemPlanoFinanceiro().equals("DM") && !getUtilizarDiaFixo() && !getUtilizarDiaUtil() && !getDescontoValidoAteDataVencimento() && !Uteis.isAtributoPreenchido(getDiasValidadeVencimento())) 
			|| (getTipoItemPlanoFinanceiro().equals("PD") && getPlanoDescontoVO().getUtilizarDescontoSemLimiteValidade())
			|| getTipoItemPlanoFinanceiro().equals("CO")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
