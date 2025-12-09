package negocio.comuns.faturamento.nfe;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.faturamento.nfe.NotaFiscalSaida;

/**
 * Responsável por manter os dados da entidade NotaFiscalSaidaServico. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os mï¿½todos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see NotaFiscalSaida
*/
public class NotaFiscalSaidaServicoVO extends SuperVO {
	
    protected Integer codigo;
    protected NotaFiscalSaidaVO notaFiscalSaida;
    protected Double quantidade;
    protected Double metragem;
    protected Double precoUnitario;
    protected Double precoTotal;
    protected Double aliquotaIpi;
    protected Double baseIpi;
    protected Double totalIpi;
    protected Double aliquotaReduzidaIcms;
    protected Double aliquotaIcms;
    protected Double baseIcms;
    protected Double totalIcms;
    protected Double substituicaoTributaria;
    protected Double baseSubstituicaoTributaria;
    protected Double totalSubstituicaoTributaria;
    protected Double aliquotaIssqn;
    protected Double baseIssqn;
    protected Double totalIssqn;
    protected Double aliquotaIss;
    protected Double baseIss;
    protected Double totalIss;
    private String descricao;
    private String codigoNCM;
    private Double aliquotaPIS;
    private Double aliquotaCOFINS;
    private Double totalPIS;
    private Double totalCOFINS;
	private Double totalINSS;
	private Double totalIRRF;
	private Double totalCSLL;
    
	//NATUREZA OPERAÇÃO
	private String codigoNaturezaOperacao;
	private String nomeNaturezaOperacao;

	// Transient
	private ContaReceberRecebimentoVO contaReceberRecebimentoVO;
	private ContaReceberVO contaReceberVO;
	// Responsável por realizar o agrupamento de ContaReceberRecebimentoVOs com NotaFiscalSaidaServicoVO
	private List<Integer> listaCodigoContaReceberRecebimento;
	private List<ContaReceberVO> listaCodigoContaReceber;
	
	private Integer codigoProduto;
    /**
     * Construtor padrão da classe <code>NotaFiscalSaidaServico</code>.
     * Cria uma nova instï¿½ncia desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public NotaFiscalSaidaServicoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>NotaFiscalSaidaServicoVO</code>.
     * Todos os tipos de consistência de dados sï¿½o e devem ser implementadas neste mï¿½todo.
     * Sï¿½o validaï¿½ï¿½es tï¿½picas: verificaï¿½ï¿½o de campos obrigatï¿½rios, verificaï¿½ï¿½o de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceï¿½ï¿½o descrevendo
     *                               o atributo e o erro ocorrido.
    */
    public static void validarDados(NotaFiscalSaidaServicoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
            }
        if (obj.getQuantidade().doubleValue() == 0) { 
            throw new ConsistirException("O campo QUANTIDADE (Nota Fiscal Saida Serviço) deve ser informado.");
        }
        if (obj.getPrecoUnitario().doubleValue() == 0) { 
            throw new ConsistirException("O campo PREÇO UNITÁRIO (Nota Fiscal Saida Serviço) deve ser informado.");
        }
        if (obj.getPrecoTotal().doubleValue() == 0) { 
            throw new ConsistirException("O campo PREÇO TOTAL (Nota Fiscal Saida Serviço) deve ser informado.");
        }
    }
     
    /**
     * Operação responsável por realizar o UpperCase dos atributos do tipo String.
    */
    public void realizarUpperCaseDados() {
        
    }
     
    /**
     * Operação responsável por inicializar os atributos da classe.
    */
    public void inicializarDados() {
        setCodigo( 0 );
        setQuantidade( 0.0 );
        setMetragem( 0.0 );
        setPrecoUnitario( 0.0 );
        setPrecoTotal( 0.0 );
        setAliquotaIpi( 0.0 );
        setBaseIpi( 0.0 );
        setTotalIpi( 0.0 );
        setAliquotaIcms( 0.0 );
        setBaseIcms( 0.0 );
        setTotalIcms( 0.0 );
        setSubstituicaoTributaria( 0.0 );
        setBaseSubstituicaoTributaria( 0.0 );
        setTotalSubstituicaoTributaria( 0.0 );
        setAliquotaReduzidaIcms(0.0);
        setBaseIssqn(0.0);
        setAliquotaIssqn(0.0);
        setTotalIssqn(0.0);
    }

    public void realizarCalculosFiscais(String aplicarSubstituicaoTrubutaria, Boolean consumidorFinal, Double baseReduzida) {
        //realizarCalculoIpi();
        realizarCalcularIssqn();
        //realizarCalculoIcms(baseReduzida, consumidorFinal);
        //if (aplicarSubstituicaoTrubutaria.equals("S") && !consumidorFinal) {
          //  realizarCalculoSubstituicaoTributaria();
        //} else {
//            setSubstituicaoTributaria(0.0);
//            setBaseSubstituicaoTributaria(0.0);
//            setTotalSubstituicaoTributaria(0.0);
//        }
    }

    public void realizarCalcularIssqn(){
        setBaseIssqn(getPrecoTotal());
        setTotalIssqn(Uteis.arrendondarForcando2CasasDecimais(getBaseIssqn() * (getAliquotaIssqn() / 100)));
        if(getTotalIssqn().doubleValue() == 0.0){
            setBaseIssqn(0.0);
        }
    }

    public void realizarCalculoIpi() {
        if (aliquotaIpi.doubleValue() > 0) {
            setBaseIpi(getPrecoTotal());
            setTotalIpi(Uteis.arrendondarForcando2CasasDecimais(getBaseIpi() * (getAliquotaIpi() / 100)));
        } else {
            setBaseIpi(0.0);
            setTotalIpi(0.0);
        }
    }

    public void realizarCalculoIcms(Double baseReduzida, Boolean consumidorFinal) {
        setBaseIcms(getPrecoTotal());
        if (consumidorFinal) {
            setBaseIcms(getBaseIcms() + getTotalIpi());
        }
        if (baseReduzida.doubleValue() > 0 ) {
            setAliquotaReduzidaIcms(baseReduzida);
            setBaseIcms(Uteis.arrendondarForcando2CasasDecimais((getBaseIcms() * (baseReduzida / 100)) / (getAliquotaIcms() / 100)));
        }
        setTotalIcms(Uteis.arrendondarForcando2CasasDecimais(getBaseIcms() * (getAliquotaIcms() / 100)));
    }

    public void realizarCalculoSubstituicaoTributaria() {
        if (getSubstituicaoTributaria().doubleValue() > 0) {
            setBaseSubstituicaoTributaria(Uteis.arrendondarForcando2CasasDecimais(getPrecoTotal() + (getPrecoTotal() * (getSubstituicaoTributaria() / 100))));
            setTotalSubstituicaoTributaria(Uteis.arrendondarForcando2CasasDecimais((getBaseSubstituicaoTributaria() * (getSubstituicaoTributaria() / 100)) - getTotalIcms()));
        }
    }

    /**
     * Retorna o objeto da classe <code>NaturezaOperacao</code> relacionado com (<code>NotaFiscalSaidaServico</code>).
    */

    public Double getTotalSubstituicaoTributaria() {
        if (totalSubstituicaoTributaria == null) {
            totalSubstituicaoTributaria = 0.0;
        }
        return (totalSubstituicaoTributaria);
    }
     
    public void setTotalSubstituicaoTributaria( Double totalSubstituicaoTributaria ) {
        this.totalSubstituicaoTributaria = totalSubstituicaoTributaria;
    }

    public Double getBaseSubstituicaoTributaria() {
        if (baseSubstituicaoTributaria == null) {
            baseSubstituicaoTributaria = 0.0;
        }
        return (baseSubstituicaoTributaria);
    }
     
    public void setBaseSubstituicaoTributaria( Double baseSubstituicaoTributaria ) {
        this.baseSubstituicaoTributaria = baseSubstituicaoTributaria;
    }

    public Double getSubstituicaoTributaria() {
        if (substituicaoTributaria == null) {
            substituicaoTributaria = 0.0;
        }
        return (substituicaoTributaria);
    }
     
    public void setSubstituicaoTributaria( Double substituicaoTributaria ) {
        this.substituicaoTributaria = substituicaoTributaria;
    }

    public Double getTotalIcms() {
        if (totalIcms == null) {
            totalIcms = 0.0;
        }
        return (totalIcms);
    }
     
    public void setTotalIcms( Double totalIcms ) {
        this.totalIcms = totalIcms;
    }

    public Double getBaseIcms() {
        if (baseIcms == null) {
            baseIcms = 0.0;
        }
        return (baseIcms);
    }
     
    public void setBaseIcms( Double baseIcms ) {
        this.baseIcms = baseIcms;
    }

    public Double getAliquotaIcms() {
        if (aliquotaIcms == null) {
            aliquotaIcms = 0.0;
        }
        return (aliquotaIcms);
    }
     
    public void setAliquotaIcms( Double aliquotaIcms ) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public Double getTotalIpi() {
        if (totalIpi == null) {
            totalIpi = 0.0;
        }
        return (totalIpi);
    }
     
    public void setTotalIpi( Double totalIpi ) {
        this.totalIpi = totalIpi;
    }

    public Double getBaseIpi() {
        if (baseIpi == null) {
            baseIpi = 0.0;
        }
        return (baseIpi);
    }
     
    public void setBaseIpi( Double baseIpi ) {
        this.baseIpi = baseIpi;
    }

    public Double getAliquotaIpi() {
        if (aliquotaIpi == null) {
            aliquotaIpi = 0.0;
        }
        return (aliquotaIpi);
    }
     
    public void setAliquotaIpi( Double aliquotaIpi ) {
        this.aliquotaIpi = aliquotaIpi;
    }

    public Double getPrecoTotal() {
        if (precoTotal == null) {
            precoTotal = 0.0;
        }
        return (precoTotal);
    }
     
    public void setPrecoTotal( Double precoTotal ) {
        this.precoTotal = precoTotal;
    }

    public Double getPrecoUnitario() {
        if (precoUnitario == null) {
            precoUnitario = 0.0;
        }
        return (precoUnitario);
    }
     
    public void setPrecoUnitario( Double precoUnitario ) {
        this.precoUnitario = precoUnitario;
    }

    public Double getMetragem() {
        if (metragem == null) {
            metragem = 0.0;
        }
        return (metragem);
    }
     
    public void setMetragem( Double metragem ) {
        this.metragem = metragem;
    }

    public Double getQuantidade() {
        if (quantidade == null) {
            quantidade = 0.0;
        }
        return (quantidade);
    }
     
    public void setQuantidade( Double quantidade ) {
        this.quantidade = quantidade;
    }

    public NotaFiscalSaidaVO getNotaFiscalSaida() {
        if (notaFiscalSaida == null) {
            notaFiscalSaida = new NotaFiscalSaidaVO();
        }
        return (notaFiscalSaida);
    }
     
    public void setNotaFiscalSaida(NotaFiscalSaidaVO notaFiscalSaida ) {
        this.notaFiscalSaida = notaFiscalSaida;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }
     
    public void setCodigo( Integer codigo ) {
        this.codigo = codigo;
    }

    public Double getAliquotaReduzidaIcms() {
        return aliquotaReduzidaIcms;
    }

    public void setAliquotaReduzidaIcms(Double aliquotaReduzidaIcms) {
        this.aliquotaReduzidaIcms = aliquotaReduzidaIcms;
    }

    public Double getAliquotaIssqn() {
        return aliquotaIssqn;
    }

    public void setAliquotaIssqn(Double aliquotaIssqn) {
        this.aliquotaIssqn = aliquotaIssqn;
    }

    public Double getBaseIssqn() {
        return baseIssqn;
    }

    public void setBaseIssqn(Double baseIssqn) {
        this.baseIssqn = baseIssqn;
    }

    public Double getTotalIssqn() {
        return totalIssqn;
    }

    public void setTotalIssqn(Double totalIssqn) {
        this.totalIssqn = totalIssqn;
    }
    
    public NotaFiscalSaidaServicoVO clonar() {
    	NotaFiscalSaidaServicoVO servico = new NotaFiscalSaidaServicoVO();
    	
    	servico.setTotalSubstituicaoTributaria(getTotalSubstituicaoTributaria());
    	servico.setBaseSubstituicaoTributaria(getSubstituicaoTributaria());
    	servico.setSubstituicaoTributaria(getSubstituicaoTributaria());
    	servico.setTotalIcms(getTotalIcms());
    	servico.setBaseIcms(getBaseIcms());
    	servico.setAliquotaIcms(getAliquotaIcms());
    	servico.setTotalIpi(getTotalIpi());
    	servico.setBaseIpi(getBaseIpi());
    	servico.setAliquotaIpi(getAliquotaIpi());
    	servico.setPrecoTotal(getPrecoTotal());
    	servico.setPrecoUnitario(getPrecoUnitario());
    	servico.setMetragem(getMetragem());
    	servico.setQuantidade(getQuantidade());
    	servico.setAliquotaReduzidaIcms(getAliquotaReduzidaIcms());
    	servico.setAliquotaIssqn(getAliquotaIssqn());
    	servico.setTotalIssqn(getTotalIssqn());
    
    	return servico;
    }

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ContaReceberRecebimentoVO getContaReceberRecebimentoVO() {
		if (contaReceberRecebimentoVO == null) {
			contaReceberRecebimentoVO = new ContaReceberRecebimentoVO();
		}
		return contaReceberRecebimentoVO;
	}

	public void setContaReceberRecebimentoVO(ContaReceberRecebimentoVO contaReceberRecebimentoVO) {
		this.contaReceberRecebimentoVO = contaReceberRecebimentoVO;
	}

	public Double getAliquotaIss() {
		if (aliquotaIss == null) {
			aliquotaIss = 0.0;
		}
		return aliquotaIss;
	}

	public void setAliquotaIss(Double aliquotaIss) {
		this.aliquotaIss = aliquotaIss;
	}

	public Double getBaseIss() {
		if (baseIss == null) {
			baseIss = 0.0;
		}
		return baseIss;
	}

	public void setBaseIss(Double baseIss) {
		this.baseIss = baseIss;
	}

	public Double getTotalIss() {
		if (totalIss == null) {
			totalIss = 0.0;
		}
		return totalIss;
	}

	public void setTotalIss(Double totalIss) {
		this.totalIss = totalIss;
	}

	public String getCodigoNaturezaOperacao() {
		if (codigoNaturezaOperacao == null) {
			codigoNaturezaOperacao = "";
		}
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(String codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public String getNomeNaturezaOperacao() {
		if (nomeNaturezaOperacao == null) {
			nomeNaturezaOperacao = "";
		}
		return nomeNaturezaOperacao;
	}

	public void setNomeNaturezaOperacao(String nomeNaturezaOperacao) {
		this.nomeNaturezaOperacao = nomeNaturezaOperacao;
	}

	public List<Integer> getListaCodigoContaReceberRecebimento() {
		if (listaCodigoContaReceberRecebimento == null) {
			listaCodigoContaReceberRecebimento = new ArrayList<Integer>(0);
		}
		return listaCodigoContaReceberRecebimento;
	}

	public void setListaCodigoContaReceberRecebimento(List<Integer> listaCodigoContaReceberRecebimento) {
		this.listaCodigoContaReceberRecebimento = listaCodigoContaReceberRecebimento;
	}

	public String getCodigoNCM() {
		if (codigoNCM == null) {
			codigoNCM = "";
		}
		return codigoNCM;
	}

	public void setCodigoNCM(String codigoNCM) {
		this.codigoNCM = codigoNCM;
	}

	public Double getAliquotaPIS() {
		if (aliquotaPIS == null) {
			aliquotaPIS = 0.0;
		}
		return aliquotaPIS;
	}

	public void setAliquotaPIS(Double aliquotaPIS) {
		this.aliquotaPIS = aliquotaPIS;
	}

	public Double getAliquotaCOFINS() {
		if (aliquotaCOFINS == null) {
			aliquotaCOFINS = 0.0;
		}
		return aliquotaCOFINS;
	}

	public void setAliquotaCOFINS(Double aliquotaCOFINS) {
		this.aliquotaCOFINS = aliquotaCOFINS;
	}

	public Double getTotalPIS() {
		if (totalPIS == null) {
			totalPIS = 0.0;
		}
		return totalPIS;
	}

	public void setTotalPIS(Double totalPIS) {
		this.totalPIS = totalPIS;
	}

	public Double getTotalCOFINS() {
		if (totalCOFINS == null) {
			totalCOFINS = 0.0;
		}
		return totalCOFINS;
	}

	public void setTotalCOFINS(Double totalCOFINS) {
		this.totalCOFINS = totalCOFINS;
	}


	public Double getTotalINSS() {
		if(totalINSS == null){
			totalINSS = 0.0;
		}
		return totalINSS;
	}

	public void setTotalINSS(Double totalINSS) {
		this.totalINSS = totalINSS;
	}

	public Double getTotalIRRF() {
		if(totalIRRF == null){
			totalIRRF = 0.0;
		}
		return totalIRRF;
	}

	public void setTotalIRRF(Double totalIRRF) {
		this.totalIRRF = totalIRRF;
	}

	public Double getTotalCSLL() {
		if(totalCSLL == null ){
			totalCSLL= 0.0;
		}
		return totalCSLL;
	}

	public void setTotalCSLL(Double totalCSLL) {
		this.totalCSLL = totalCSLL;
	}

	public List<ContaReceberVO> getListaCodigoContaReceber() {
		if(listaCodigoContaReceber == null){
			listaCodigoContaReceber = new ArrayList<ContaReceberVO>(0);
		}
		return listaCodigoContaReceber;
	}

	public void setListaCodigoContaReceber(List<ContaReceberVO> listaCodigoContaReceber) {
		this.listaCodigoContaReceber = listaCodigoContaReceber;
	}

	public ContaReceberVO getContaReceberVO() {
		if(contaReceberVO == null){
			contaReceberVO =  new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public Integer getCodigoProduto() {
		if (codigoProduto == null) {
			codigoProduto = 0;
		}
		return codigoProduto;
	}

	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
}