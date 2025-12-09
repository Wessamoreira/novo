package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;


public class RecebimentoCompraVO extends SuperVO {

    private Integer codigo;
    private Date data;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>RecebimentoCompraItem</code>.
     */
    private List<RecebimentoCompraItemVO> recebimentoCompraItemVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Compra </code>.
     */
    private CompraVO compra;
    private NotaFiscalEntradaRecebimentoCompraVO notaFiscalEntradaCompraVO;
    private String situacao;
    private UnidadeEnsinoVO unidadeEnsino;
    private Double valorTotal;
    private TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavel;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RecebimentoCompra</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RecebimentoCompraVO() {
        super();
        inicializarDados();
    }
    
    public RecebimentoCompraVO(String situacao, CompraVO compra) {
        setCompra(compra);
        setResponsavel(compra.getResponsavel());
        setSituacao(situacao);
        setTipoCriacaoContaPagarEnum(compra.getTipoCriacaoContaPagarEnum());
        getUnidadeEnsino().setCodigo(compra.getUnidadeEnsino().getCodigo());
    }

   
    public static void validarDados(RecebimentoCompraVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getCompra() == null) || (obj.getCompra().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo COMPRA (Recebimento Compra) deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Recebimento Compra) deve ser informado.");
        }
        if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
           throw new ConsistirException("O campo UNIDADE DE ENSINO (Recebimento Compra) deve ser informado.");
        }
        if (!Uteis.isAtributoPreenchido(obj.getRecebimentoCompraItemVOs())) {
            throw new ConsistirException("Deve existir ao menos um PRODUTO (Recebimento Compra)  a ser entregue.");
        }
        
    }

    public void realizarUpperCaseDados() {
    }


    public void inicializarDados() {
        setCodigo(0);
        setSituacao("");
        setValorTotal(0.0);
    }


    public void adicionarObjRecebimentoCompraItemVOs(RecebimentoCompraItemVO obj) throws Exception {
        int index = 0;
        Iterator i = getRecebimentoCompraItemVOs().iterator();
        while (i.hasNext()) {
            RecebimentoCompraItemVO objExistente = (RecebimentoCompraItemVO) i.next();
            if (objExistente.getCompraItem().getCodigo().equals(obj.getCompraItem().getCodigo())) {
                getRecebimentoCompraItemVOs().set(index, obj);
                return;
            }
            index++;
        }
        getRecebimentoCompraItemVOs().add(obj);
    }

    
    public void excluirObjRecebimentoCompraItemVOs(Integer compraItem) throws Exception {
        Iterator i = getRecebimentoCompraItemVOs().iterator();
        while (i.hasNext()) {
            RecebimentoCompraItemVO objExistente = (RecebimentoCompraItemVO) i.next();
            if (objExistente.getCompraItem().getCodigo().equals(compraItem)) {
                i.remove();
                return;
            }
        }
    }

    public boolean getRecebimentoFinalizado() {
    	return Uteis.isAtributoPreenchido(getSituacao()) && getSituacao().equals("EF");
    }

    public String getDescricaoQuantidadeRecebida() {
    	return !getRecebimentoFinalizado() ? "Qtd. A Receber" : "Qtd. Recebida";
    }
    
    public void atualizarValorTotal() {
    	setValorTotal(getRecebimentoCompraItemVOs().stream().mapToDouble(RecebimentoCompraItemVO::getValorTotal).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
    }

    public Double getValorTotal() {
    	if(valorTotal == null){
    		valorTotal =0.0;
    	}
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public Double getValorTotalUtilizado(){
    	if(getCompra().getTipoCriacaoContaPagarEnum().isNotaFiscalEntrada() && getNotaFiscalEntradaCompraVO().isSelecionado()) {
    		return getRecebimentoCompraItemVOs().stream().filter(RecebimentoCompraItemVO::isSelecionado).mapToDouble(RecebimentoCompraItemVO::getValorTotal).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
    	} else if (getCompra().getTipoCriacaoContaPagarEnum().isRecebimentoCompra() || getCompra().getTipoCriacaoContaPagarEnum().isCompra()) {
    		return getRecebimentoCompraItemVOs().stream().filter(RecebimentoCompraItemVO::isSelecionado).mapToDouble(RecebimentoCompraItemVO::getValorTotal).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
    	} else {
    		return 0.0;
    	}
		
	}

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>RecebimentoCompra</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>RecebimentoCompra</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna o objeto da classe <code>Compra</code> relacionado com (
     * <code>RecebimentoCompra</code>).
     */
    public CompraVO getCompra() {
        if (compra == null) {
            compra = new CompraVO();
        }
        return (compra);
    }        

	/**
     * Define o objeto da classe <code>Compra</code> relacionado com (
     * <code>RecebimentoCompra</code>).
     */
    public void setCompra(CompraVO obj) {
        this.compra = obj;
    }
    
    public TipoCriacaoContaPagarEnum getTipoCriacaoContaPagarEnum() {
		if (tipoCriacaoContaPagarEnum == null) {
			tipoCriacaoContaPagarEnum = TipoCriacaoContaPagarEnum.COMPRA;
		}
		return tipoCriacaoContaPagarEnum;
	}

	public void setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum) {
		this.tipoCriacaoContaPagarEnum = tipoCriacaoContaPagarEnum;
	}

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>RecebimentoCompraItem</code>.
     */
    public List<RecebimentoCompraItemVO> getRecebimentoCompraItemVOs() {
        if (recebimentoCompraItemVOs == null) {
            recebimentoCompraItemVOs = new ArrayList<RecebimentoCompraItemVO>();
        }
        return (recebimentoCompraItemVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>RecebimentoCompraItem</code>.
     */
    public void setRecebimentoCompraItemVOs(List<RecebimentoCompraItemVO> recebimentoCompraItemVOs) {
        this.recebimentoCompraItemVOs = recebimentoCompraItemVOs;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return Uteis.isAtributoPreenchido(getData()) ? (Uteis.getData(data)) : "";
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigo() {
    	if(codigo == null){
    		codigo = 0;
    	}
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getSituacao_Apresentar() {
    	return getRecebimentoFinalizado() ? "Efetivada" :"Previsão";
    }

    public String getSituacao() {
    	if(situacao == null){
    		situacao = "PR";
    	}
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the receberCompra
     */
    public Boolean getReceberCompra() {
    	return  getRecebimentoFinalizado() ? false : true;
    }
   
	public NotaFiscalEntradaRecebimentoCompraVO getNotaFiscalEntradaCompraVO() {
		if (notaFiscalEntradaCompraVO == null) {
			notaFiscalEntradaCompraVO = new NotaFiscalEntradaRecebimentoCompraVO();
		}
		return notaFiscalEntradaCompraVO;
	}

	public void setNotaFiscalEntradaCompraVO(NotaFiscalEntradaRecebimentoCompraVO notaFiscalEntradaCompraVO) {
		this.notaFiscalEntradaCompraVO = notaFiscalEntradaCompraVO;
	}
	
	
	public String getNumeroDocumentoPorRecebimentoCompra() {
		return "." + getCodigo();
	}
    

	
    
    
}
