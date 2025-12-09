package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Estoque. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EstoqueVO extends SuperVO {

    private Integer codigo;
    private Double quantidade;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Produto </code>.
     */
    private ProdutoServicoVO produto;
    private Double estoqueMinimo;
    private Double estoqueMaximo;
    private Double precoUnitario;
    private Date dataEntrada;
    private Double divergencia;
    private List<EstoqueVO> estoqueVOs;
    public static final long serialVersionUID = 1L;

   
   
    public EstoqueVO() {
        super();
        inicializarDados();
    }
   
    public static void validarDados(EstoqueVO obj) throws ConsistirException {
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Estoque) deve ser informado.");
        }
        if ((obj.getProduto() == null) || (obj.getProduto().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PRODUTO (Estoque) deve ser informado.");
        }
        Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPrecoUnitario()), "O campo PREÇO UNITÁRIO (Estoque) deve ser informado.");
   }

    
    public void inicializarDados() {
        setCodigo(0);
        setQuantidade(0.0);
        setEstoqueMinimo(0.0);
        setDataEntrada(new Date());
        setPrecoUnitario(0.0);
    }
    
    public void atualizarEstoqueMinino() {
    	getEstoqueVOs().forEach(p-> p.setEstoqueMinimo(getEstoqueMinimo()));
	}
	
	public void atualizarEstoqueMaximo() {
		getEstoqueVOs().forEach(p-> p.setEstoqueMaximo(getEstoqueMaximo()));
	}

    public List<EstoqueVO> getEstoqueVOs() {
        if (estoqueVOs == null) {
            estoqueVOs = new ArrayList<EstoqueVO>(0);
        }
        return estoqueVOs;
    }

    public void setEstoqueVOs(List<EstoqueVO> estoqueVOs) {
        this.estoqueVOs = estoqueVOs;
    }

    public boolean getApresentarEstoque() {
        if (getValorTotal().doubleValue() == 0) {
            return false;
        }
        return true;
    }

    public boolean getEditarEstoque() {
        if (quantidade.doubleValue() == 0) {
            return false;
        }
        return true;
    }

    public Double getValorTotal() {
        if (!getEstoqueVOs().isEmpty() && getPrecoUnitario().doubleValue() == 0.0) {
            return calcularTotal();
        }
        return Uteis.arrendondarForcando2CadasDecimais(getQuantidade() * getPrecoUnitario());
    }

    public Double calcularTotal() {
        Double total = 0.0;
        for (EstoqueVO obj : getEstoqueVOs()) {
            total = total + obj.getValorTotal();
        }
        return total;
    }

    /**
     * Retorna o objeto da classe <code>Produto</code> relacionado com (
     * <code>Estoque</code>).
     */
    public ProdutoServicoVO getProduto() {
        if (produto == null) {
            produto = new ProdutoServicoVO();
        }
        return (produto);
    }

    /**
     * Define o objeto da classe <code>Produto</code> relacionado com (
     * <code>Estoque</code>).
     */
    public void setProduto(ProdutoServicoVO obj) {
        this.produto = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Estoque</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Estoque</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public Double getQuantidadeTotal() {
        return getEstoqueVOs().stream().map(EstoqueVO::getQuantidade).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
    }
    
    public boolean isQuantidadeExistente(){
    	return Uteis.isAtributoPreenchido(getQuantidade());
    }
    
    public Double getQuantidade() {
    	return (quantidade);
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Double getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Double estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getDataEntrada_Apresentar() {
        return Uteis.getData(dataEntrada);
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    /**
     * @return the estoqueMaximo
     */
    public Double getEstoqueMaximo() {
        if (estoqueMaximo == null) {
            estoqueMaximo = 0.0;
        }
        return estoqueMaximo;
    }

    /**
     * @param estoqueMaximo the estoqueMaximo to set
     */
    public void setEstoqueMaximo(Double estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

	public Double getDivergencia() {
		if (divergencia == null) {
			divergencia = 0.0;
		}
		return divergencia;
	}

	public void setDivergencia(Double divergencia) {
		this.divergencia = divergencia;
	}
}
