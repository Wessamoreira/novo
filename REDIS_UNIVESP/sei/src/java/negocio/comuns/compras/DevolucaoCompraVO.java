package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade DevolucaoCompra. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class DevolucaoCompraVO extends SuperVO {

    private Integer codigo;
    private Date data;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>DevolucaoCompraItem</code>.
     */
    private List<DevolucaoCompraItemVO> devolucaoCompraItemVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Compra </code>.
     */
    private CompraVO compra;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaPagar </code>.
     */
    private ContaPagarVO contaPagar;
    private List<ContaPagarVO> consultaContaPagar;
    private String formaReceber;
    public static final long serialVersionUID = 1L;
    private Double totalPago;
	private Double totalPagar;
	private Double totalExcluir;
	private Double totalNaoAbatido;
	private ArquivoVO arquivo;
	
	/**
	 * Ver o que vai ser definido
	 */
	private Double valorTotalCredito;

    /**
     * Construtor padrão da classe <code>DevolucaoCompra</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DevolucaoCompraVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DevolucaoCompraVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DevolucaoCompraVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Devolução Compra) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Devolução Compra) deve ser informado.");
        }
        if (!obj.getNovoObj()) {
            if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
                throw new ConsistirException("O campo UNIDADE DE ENSINO (Devolução Compra) deve ser informado.");
            }
        }
        if ((obj.getCompra() == null) || (obj.getCompra().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo COMPRA (Devolução Compra) deve ser informado.");
        }
        if (obj.getDevolucaoCompraItemVOs() == null || obj.getDevolucaoCompraItemVOs().size() == 0) {
            throw new ConsistirException("Pelo menos um Produto (Itens da Devolução) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setFormaReceber("mercadoria");
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>DevolucaoCompraItemVO</code> ao List
     * <code>devolucaoCompraItemVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>DevolucaoCompraItem</code> - getCompraItem() -
     * como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>DevolucaoCompraItemVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjDevolucaoCompraItemVOs(DevolucaoCompraItemVO obj) throws Exception {
        DevolucaoCompraItemVO.validarDados(obj);
        int index = 0;
        Iterator i = getDevolucaoCompraItemVOs().iterator();
        while (i.hasNext()) {
            DevolucaoCompraItemVO objExistente = (DevolucaoCompraItemVO) i.next();
            if (objExistente.getCompraItem().getCodigo().equals(obj.getCompraItem().getCodigo())) {
                getDevolucaoCompraItemVOs().set(index, obj);
                return;
            }
            index++;
        }
        getDevolucaoCompraItemVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>DevolucaoCompraItemVO</code> no List
     * <code>devolucaoCompraItemVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>DevolucaoCompraItem</code> - getCompraItem() -
     * como identificador (key) do objeto no List.
     *
     * @param compraItem
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjDevolucaoCompraItemVOs(Integer compraItem) throws Exception {
        int index = 0;
        Iterator i = getDevolucaoCompraItemVOs().iterator();
        while (i.hasNext()) {
            DevolucaoCompraItemVO objExistente = (DevolucaoCompraItemVO) i.next();
            if (objExistente.getCompraItem().getCodigo().equals(compraItem)) {
                getDevolucaoCompraItemVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>DevolucaoCompraItemVO</code> no List
     * <code>devolucaoCompraItemVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>DevolucaoCompraItem</code> - getCompraItem() -
     * como identificador (key) do objeto no List.
     *
     * @param compraItem
     *            Parâmetro para localizar o objeto do List.
     */
    public DevolucaoCompraItemVO consultarObjDevolucaoCompraItemVO(Integer compraItem) throws Exception {
        Iterator i = getDevolucaoCompraItemVOs().iterator();
        while (i.hasNext()) {
            DevolucaoCompraItemVO objExistente = (DevolucaoCompraItemVO) i.next();
            if (objExistente.getCompraItem().getCodigo().equals(compraItem)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Compra</code> relacionado com (
     * <code>DevolucaoCompra</code>).
     */
    public CompraVO getCompra() {
        if (compra == null) {
            compra = new CompraVO();
        }
        return (compra);
    }

    /**
     * Define o objeto da classe <code>Compra</code> relacionado com (
     * <code>DevolucaoCompra</code>).
     */
    public void setCompra(CompraVO obj) {
        this.compra = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>DevolucaoCompra</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>DevolucaoCompra</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>DevolucaoCompraItem</code>.
     */
    public List<DevolucaoCompraItemVO> getDevolucaoCompraItemVOs() {
        if (devolucaoCompraItemVOs == null) {
            devolucaoCompraItemVOs = new ArrayList<DevolucaoCompraItemVO>();
        }
        return (devolucaoCompraItemVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>DevolucaoCompraItem</code>.
     */
    public void setDevolucaoCompraItemVOs(List<DevolucaoCompraItemVO> devolucaoCompraItemVOs) {
        this.devolucaoCompraItemVOs = devolucaoCompraItemVOs;
    }

    public Date getData() {
        return (data);
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
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        if (data == null) {
            return "";
        }
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the contaPagar
     */
    public ContaPagarVO getContaPagar() {
        if (contaPagar == null) {
            contaPagar = new ContaPagarVO();
        }
        return contaPagar;
    }

    /**
     * @param contaPagar
     *            the contaPagar to set
     */
    public void setContaPagar(ContaPagarVO contaPagar) {
        this.contaPagar = contaPagar;
    }

    /**
     * @return the excluirContaPagar
     */
    public List<ContaPagarVO> getConsultaContaPagar() {
        if (consultaContaPagar == null) {
            consultaContaPagar = new ArrayList<ContaPagarVO>(0);
        }
        return consultaContaPagar;
    }

    /**
     * @param excluirContaPagar
     *            the excluirContaPagar to set
     */
    public void setConsultaContaPagar(List<ContaPagarVO> consultaContaPagar) {
        this.consultaContaPagar = consultaContaPagar;
    }

    /**
     * @return the formaReceber
     */
    public String getFormaReceber() {
        return formaReceber;
    }

    /**
     * @param formaReceber
     *            the formaReceber to set
     */
    public void setFormaReceber(String formaReceber) {
        this.formaReceber = formaReceber;
    }

	public Double getTotalPago() {
		if (totalPago == null) {
			totalPago = 0.0;
		}
		return totalPago;
	}

	public void setTotalPago(Double totalPago) {
		this.totalPago = totalPago;
	}

	public Double getTotalPagar() {
		if (totalPagar == null) {
			totalPagar = 0.0;
		}
		return totalPagar;
	}

	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}

	public Double getTotalExcluir() {
		if (totalExcluir == null) {
			totalExcluir = 0.0;
		}
		return totalExcluir;
	}

	public void setTotalExcluir(Double totalExcluir) {
		this.totalExcluir = totalExcluir;
	}

	public Double getTotalNaoAbatido() {
		if (totalNaoAbatido == null) {
			totalNaoAbatido = 0.0;
		}
		return totalNaoAbatido;
	}

	public void setTotalNaoAbatido(Double totalNaoAbatido) {
		this.totalNaoAbatido = totalNaoAbatido;
	}

	public Double getValorTotalCredito() {
		if (valorTotalCredito == null) {
			valorTotalCredito = 0.0;
		}
		return valorTotalCredito;
	}

	public void setValorTotalCredito(Double valorTotalCredito) {
		this.valorTotalCredito = valorTotalCredito;
	}

	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}
}
