package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.compras.DevolucaoCompra;

/**
 * Reponsável por manter os dados da entidade DevolucaoCompraItem. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see DevolucaoCompra
 */
public class DevolucaoCompraItemVO extends SuperVO {

    private Integer codigo;
    private Integer devolucaoCompra;
    private CompraItemVO compraItem;
    private Double quantidade;
    private String motivo;
    private List devolucaoCompraItemImagemVOs;
	private ArquivoVO arquivo;
	
	public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DevolucaoCompraItem</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DevolucaoCompraItemVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DevolucaoCompraItemVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DevolucaoCompraItemVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getCompraItem().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo COMPRA ITEM (Itens da Devolução) deve ser informado.");
        }
        if (obj.getQuantidade().doubleValue() == 0) {
            throw new ConsistirException("O campo QUANTIDADE (Itens da Devolução) deve ser informado.");
        }
        if (obj.getQuantidade().intValue() > obj.getCompraItem().getQuantidadeRecebida().intValue()) {
            throw new ConsistirException("O campo QUANTIDADE (" + obj.getCompraItem().getProduto().getNome()
                    + ") não pode ser maior que a quandidade recebida (" + obj.getCompraItem().getQuantidadeRecebida()
                    + ").");
        }
        if (obj.getMotivo().equals("")) {
            throw new ConsistirException("O campo MOTIVO (Itens da Devolução) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setMotivo(motivo.toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setCompraItem(new CompraItemVO());
        setQuantidade(0.0);
        setMotivo("");
        setArquivo(new ArquivoVO());
    }

    public void adicionarObjDevolucaoCompraItemImagemVOs(DevolucaoCompraItemImagemVO obj) throws Exception {

        int index = 0;
        Iterator i = getDevolucaoCompraItemImagemVOs().iterator();
        while (i.hasNext()) {
            DevolucaoCompraItemImagemVO objExistente = (DevolucaoCompraItemImagemVO) i.next();
            if (objExistente.getNomeImagem().equals(obj.getNomeImagem())) {
                getDevolucaoCompraItemImagemVOs().set(index, obj);
                return;
            }
            index++;
        }
        getDevolucaoCompraItemImagemVOs().add(obj);
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
    public void excluirObjDevolucaoCompraItemImagemVOs(String nomeImagem) throws Exception {
        int index = 0;
        Iterator i = getDevolucaoCompraItemImagemVOs().iterator();
        while (i.hasNext()) {
            DevolucaoCompraItemImagemVO objExistente = (DevolucaoCompraItemImagemVO) i.next();
            if (objExistente.getNomeImagem().equals(nomeImagem)) {
                getDevolucaoCompraItemImagemVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public String getMotivo() {
        return (motivo);
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getQuantidade() {
        return (quantidade);
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public CompraItemVO getCompraItem() {
        if (compraItem == null) {
            compraItem = new CompraItemVO();
        }
        return (compraItem);
    }

    public void setCompraItem(CompraItemVO compraItem) {
        this.compraItem = compraItem;
    }

    public Integer getDevolucaoCompra() {
        return (devolucaoCompra);
    }

    public void setDevolucaoCompra(Integer devolucaoCompra) {
        this.devolucaoCompra = devolucaoCompra;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public List getDevolucaoCompraItemImagemVOs() {
        if (devolucaoCompraItemImagemVOs == null) {
            devolucaoCompraItemImagemVOs = new ArrayList();
        }
        return devolucaoCompraItemImagemVOs;
    }

    public void setDevolucaoCompraItemImagemVOs(List devolucaoCompraItemImagemVOs) {
        this.devolucaoCompraItemImagemVOs = devolucaoCompraItemImagemVOs;
    }
    
    public ArquivoVO getArquivo() {
    	return arquivo;
    }

    public void setArquivo(ArquivoVO arquivo) {
    	this.arquivo = arquivo;
    }
}


