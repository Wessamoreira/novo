package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;

/**
 * Reponsável por manter os dados da entidade RegistroArquivo. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class RegistroArquivoVO extends SuperVO {

    private Integer codigo;
    private RegistroHeaderVO registroHeader;
    private RegistroTrailerVO registroTrailer;
    private boolean arquivoProcessado;
    private boolean contasBaixadas;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>RegistroDetalhe</code>.
     */
    private List<RegistroDetalheVO> registroDetalheVOs;
    private List<RegistroDetalheVO> registroDetalheContaAgrupadaVOs;
    private List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs;
    private List<ContaReceberRegistroArquivoVO> contaReceberNegociadaRegistroArquivoVOs;
    private List<ContaReceberRegistroArquivoVO> contaReceberNaoLocalizadaRegistroArquivoVOs;
    private List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoRejeitadasVOs;
    private List<ContaReceberRegistroArquivoVO> contaReceberDataCreditoLocalizadaRegistroArquivoVOs;
    private String situacao;
    
    private Double totalTaxaBoletoCobradoBanco;
    private Double totalTaxaPagaAluno;
    private Date dataCredito;
    private Integer quantidadeConta;
    private Double valorTotalConta;
    private ContaCorrenteVO contaCorrenteVO;
    private List<ContaCorrenteVO> listaContaCorrenteVOs;
    
  
    
    
    public Double getTotalTaxaBoletoCobradoBanco() {
        if(totalTaxaBoletoCobradoBanco == null){
            totalTaxaBoletoCobradoBanco = 0.0;
        }
        return totalTaxaBoletoCobradoBanco;
    }


    
    public void setTotalTaxaBoletoCobradoBanco(Double totalTaxaBoletoCobradoBanco) {
        this.totalTaxaBoletoCobradoBanco = totalTaxaBoletoCobradoBanco;
    }


    public Double getTotalTaxaPagaAluno() {
        if(totalTaxaPagaAluno == null){
            totalTaxaPagaAluno = 0.0;
        }
        return totalTaxaPagaAluno;
    }

    
    public void setTotalTaxaPagaAluno(Double totalTaxaPagaAluno) {
        this.totalTaxaPagaAluno = totalTaxaPagaAluno;
    }
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroArquivo</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RegistroArquivoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroArquivoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(RegistroArquivoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getRegistroHeader().getCodigo().intValue() == 0) {
            throw new ConsistirException("O Registro Header (Registro Arquivo) deve ser informado.");
        }
        if (obj.getRegistroTrailer().getCodigo().intValue() == 0) {
            throw new ConsistirException("O Registro Trailer (Registro Arquivo) deve ser informado.");
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

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>RegistroDetalheVO</code> ao List <code>registroDetalheVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>RegistroDetalhe</code> - getCodigo() - como identificador (key) do
     * objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>RegistroDetalheVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjRegistroDetalheVOs(RegistroDetalheVO obj) throws Exception {
        RegistroDetalheVO.validarDados(obj);
        obj.setRegistroArquivo(this);
        int index = 0;
        Iterator i = getRegistroDetalheVOs().iterator();
        while (i.hasNext()) {
            RegistroDetalheVO objExistente = (RegistroDetalheVO) i.next();
            if (objExistente.getCodigo().equals(obj.getCodigo())) {
                getRegistroDetalheVOs().set(index, obj);
                return;
            }
            index++;
        }
        getRegistroDetalheVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>RegistroDetalheVO</code> no List <code>registroDetalheVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>RegistroDetalhe</code> - getCodigo() - como identificador (key) do
     * objeto no List.
     *
     * @param codigo
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjRegistroDetalheVOs(Integer codigo) throws Exception {
        int index = 0;
        Iterator i = getRegistroDetalheVOs().iterator();
        while (i.hasNext()) {
            RegistroDetalheVO objExistente = (RegistroDetalheVO) i.next();
            if (objExistente.getCodigo().equals(codigo)) {
                getRegistroDetalheVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>RegistroDetalheVO</code> no List <code>registroDetalheVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>RegistroDetalhe</code> - getCodigo() - como identificador (key) do
     * objeto no List.
     *
     * @param codigo
     *            Parâmetro para localizar o objeto do List.
     */
    public RegistroDetalheVO consultarObjRegistroDetalheVO(Integer codigo) throws Exception {
        Iterator i = getRegistroDetalheVOs().iterator();
        while (i.hasNext()) {
            RegistroDetalheVO objExistente = (RegistroDetalheVO) i.next();
            if (objExistente.getCodigo().equals(codigo)) {
                return objExistente;
            }
        }
        return null;
    }

    public RegistroDetalheVO getRegistroDetalhe(Integer codigoContaReceber) {
        for (RegistroDetalheVO detalhe : registroDetalheVOs) {
            if (codigoContaReceber.intValue()==(detalhe.getCodigoContaReceber().intValue())) {
                return detalhe;
            }
        }
        return null;
    }
	
	public RegistroDetalheVO getRegistroDetalhe(Integer codigoContaReceber, Double valorPago) {
    	RegistroDetalheVO detalheFinal = null;
    	for (RegistroDetalheVO detalhe : registroDetalheVOs) {
    		if (codigoContaReceber.intValue()==(detalhe.getCodigoContaReceber().intValue())) {
    			if (!detalhe.getSelecionado().booleanValue() && (valorPago.doubleValue() == detalhe.getValorPago().doubleValue())) {
    				detalhe.setSelecionado(Boolean.TRUE);
    				detalheFinal = detalhe;
    				break;
				}
    		}
    	}
    	return detalheFinal;
    }
	
    public RegistroDetalheVO getRegistroDetalhe(String nossoNumeroContaReceber) {
    	for (RegistroDetalheVO detalhe : registroDetalheVOs) {
    		if (nossoNumeroContaReceber.equals(detalhe.getIdentificacaoTituloEmpresa())) {
    			return detalhe;
    		}
    	}
    	return null;
    }

    public RegistroDetalheVO getRegistroDetalheContaAgrupada(Integer codigoContaReceber) {
    	for (RegistroDetalheVO detalhe : registroDetalheContaAgrupadaVOs) {
    		if (codigoContaReceber.intValue()==(detalhe.getCodigoContaReceber().intValue())) {
    			return detalhe;
    		}
    	}
    	return null;
    }
    
    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>RegistroDetalhe</code>.
     */
    public List<RegistroDetalheVO> getRegistroDetalheVOs() {
        if (registroDetalheVOs == null) {
            registroDetalheVOs = new ArrayList<RegistroDetalheVO>(0);
        }
        return (registroDetalheVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>RegistroDetalhe</code>.
     */
    public void setRegistroDetalheVOs(List<RegistroDetalheVO> registroDetalheVOs) {
        this.registroDetalheVOs = registroDetalheVOs;
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

    public boolean getArquivoProcessado() {
        return arquivoProcessado;
    }

    public void setArquivoProcessado(boolean arquivoProcessado) {
        this.arquivoProcessado = arquivoProcessado;
    }

    public boolean getContasBaixadas() {
        return contasBaixadas;
    }

    public void setContasBaixadas(boolean contasBaixadas) {
        this.contasBaixadas = contasBaixadas;
    }

    public RegistroHeaderVO getRegistroHeader() {
        if (registroHeader == null) {
            registroHeader = new RegistroHeaderVO();
        }
        return registroHeader;
    }

    public void setRegistroHeader(RegistroHeaderVO registroHeader) {
        this.registroHeader = registroHeader;
    }

    public RegistroTrailerVO getRegistroTrailer() {
        if (registroTrailer == null) {
            registroTrailer = new RegistroTrailerVO();
        }
        return registroTrailer;
    }

    public void setRegistroTrailer(RegistroTrailerVO registroTrailer) {
        this.registroTrailer = registroTrailer;
    }

    public List<ContaReceberRegistroArquivoVO> getContaReceberRegistroArquivoVOs() {
        if (contaReceberRegistroArquivoVOs == null) {
            contaReceberRegistroArquivoVOs = new ArrayList(0);
        }
        return contaReceberRegistroArquivoVOs;
    }

    public void setContaReceberRegistroArquivoVOs(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs) {
        this.contaReceberRegistroArquivoVOs = contaReceberRegistroArquivoVOs;
    }

    public List<ContaReceberRegistroArquivoVO> getContaReceberNaoLocalizadaRegistroArquivoVOs() {
    	if (contaReceberNaoLocalizadaRegistroArquivoVOs == null) {
    		contaReceberNaoLocalizadaRegistroArquivoVOs = new ArrayList(0);
    	}
    	return contaReceberNaoLocalizadaRegistroArquivoVOs;
    }
    
    public void setContaReceberNaoLocalizadaRegistroArquivoVOs(List<ContaReceberRegistroArquivoVO> contaReceberNaoLocalizadaRegistroArquivoVOs) {
    	this.contaReceberNaoLocalizadaRegistroArquivoVOs = contaReceberNaoLocalizadaRegistroArquivoVOs;
    }

    public List<ContaReceberRegistroArquivoVO> getContaReceberNegociadaRegistroArquivoVOs() {
    	if(!this.getContaReceberRegistroArquivoVOs().isEmpty()) {
    		List<ContaReceberRegistroArquivoVO> lista = new ArrayList(0);
    		for (ContaReceberRegistroArquivoVO obj : getContaReceberRegistroArquivoVOs()) {
    			if (obj.getContaRecebidaNegociada()) {
    				lista.add(obj);
    			}
    		}
    		return lista;
    	}
		return new ArrayList(0);
    }
    
    public void setContaReceberNegociadaRegistroArquivoVOs(List<ContaReceberRegistroArquivoVO> contaReceberNegociadaRegistroArquivoVOs) {
    	this.contaReceberNegociadaRegistroArquivoVOs = contaReceberNegociadaRegistroArquivoVOs;
    }

    public List<ContaReceberRegistroArquivoVO> getContaReceberRegistroArquivoRejeitadasVOs() {
        if (contaReceberRegistroArquivoRejeitadasVOs == null) {
            contaReceberRegistroArquivoRejeitadasVOs = new ArrayList(0);
        }
        return contaReceberRegistroArquivoRejeitadasVOs;
    }

    public void setContaReceberRegistroArquivoRejeitadasVOs(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoRejeitadasVOs) {
        this.contaReceberRegistroArquivoRejeitadasVOs = contaReceberRegistroArquivoRejeitadasVOs;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao_Apresentar() {
        return SituacaoArquivoRetorno.getDescricao(getSituacao());
    }

    


	public Integer getQuantidadeConta() {
		if(quantidadeConta == null){
			quantidadeConta = 0;
		}
		return quantidadeConta;
	}



	public void setQuantidadeConta(Integer quantidadeConta) {
		this.quantidadeConta = quantidadeConta;
	}



	public Double getValorTotalConta() {
		if(valorTotalConta == null){
			valorTotalConta = 0.0;
		}
		return valorTotalConta;
	}



	public void setValorTotalConta(Double valorTotalConta) {
		this.valorTotalConta = valorTotalConta;
	}



	public Date getDataCredito() {
		return dataCredito;
	}

	public String getDataCredito_Apresentar() {
        return (Uteis.getData(dataCredito));
    }

	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}



	public List<RegistroDetalheVO> getRegistroDetalheContaAgrupadaVOs() {
		if (registroDetalheContaAgrupadaVOs == null) {
			registroDetalheContaAgrupadaVOs = new ArrayList<RegistroDetalheVO>(0);
		}
		return registroDetalheContaAgrupadaVOs;
	}
	
	public void setRegistroDetalheContaAgrupadaVOs(List<RegistroDetalheVO> registroDetalheContaAgrupadaVOs) {
		this.registroDetalheContaAgrupadaVOs = registroDetalheContaAgrupadaVOs;
	}



	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}



	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}
	
	public List<ContaCorrenteVO> getListaContaCorrenteVOs() {
		if (listaContaCorrenteVOs == null) {
			listaContaCorrenteVOs = new ArrayList<ContaCorrenteVO>(0);
		}
		return listaContaCorrenteVOs;
	}

	public void setListaContaCorrenteVOs(List<ContaCorrenteVO> listaContaCorrenteVOs) {
		this.listaContaCorrenteVOs = listaContaCorrenteVOs;
	}

	public List<ContaReceberRegistroArquivoVO> getContaReceberDataCreditoLocalizadaRegistroArquivoVOs() {
		if (contaReceberDataCreditoLocalizadaRegistroArquivoVOs == null) {
			contaReceberDataCreditoLocalizadaRegistroArquivoVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
		}
		return contaReceberDataCreditoLocalizadaRegistroArquivoVOs;
	}

	public void setContaReceberDataCreditoLocalizadaRegistroArquivoVOs(List<ContaReceberRegistroArquivoVO> contaReceberDataCreditoLocalizadaRegistroArquivoVOs) {
		this.contaReceberDataCreditoLocalizadaRegistroArquivoVOs = contaReceberDataCreditoLocalizadaRegistroArquivoVOs;
	}
	
	public void preencherContaReceberDataCreditoLocalizadaRegistroArquivoVO() {
		Map<Date, List<ContaReceberRegistroArquivoVO>>  mapa = getContaReceberRegistroArquivoVOs().stream().collect(Collectors.groupingBy(p -> p.getContaReceberVO().getDataCredito()));
		for (Map.Entry<Date, List<ContaReceberRegistroArquivoVO>> mapaDataCredito : mapa.entrySet()) {
			ContaReceberRegistroArquivoVO crraDataCredito = new ContaReceberRegistroArquivoVO();
			crraDataCredito.setDataPrimeiroPagamento(mapaDataCredito.getKey());
			crraDataCredito.setValorPago(mapaDataCredito.getValue().stream().mapToDouble(p-> p.getContaReceberVO().getValorRecebido()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
			crraDataCredito.setQtdRestripoPorDataPrimeiroPagamento(mapaDataCredito.getValue().size());
			getContaReceberDataCreditoLocalizadaRegistroArquivoVOs().add(crraDataCredito);
		}
		
		
	}
	
	
}
