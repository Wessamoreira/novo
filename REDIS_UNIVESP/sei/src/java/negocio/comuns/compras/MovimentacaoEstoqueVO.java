package negocio.comuns.compras;

import java.util.Date;
import java.util.Optional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade MovimentacaoEstoque. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MovimentacaoEstoqueVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1301553348689672098L;
	private Integer codigo;
    private Date data;
    private String tipoMovimentacao;
    private Double quantidade;
    private Double precoUnitario;
    private String motivo;    
    private UsuarioVO responsavel;    
    private ProdutoServicoVO produto;    
    private UnidadeEnsinoVO unidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsinoDestino;
    private CentroResultadoVO centroResultadoEstoque;
    private CentroResultadoVO centroResultadoEstoqueDestino;
    private FuncionarioCargoVO funcionarioCargoVO;

    /**
     * Construtor padrão da classe <code>MovimentacaoEstoque</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public MovimentacaoEstoqueVO() {
        super();
        inicializarDados();
    }

  
    public static void validarDados(MovimentacaoEstoqueVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Movimentação Estoque) deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Movimentação Estoque) deve ser informado.");
        }
        
        if (obj.getProduto() == null || obj.getProduto().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo PRODUTO (Movimentação Estoque) deve ser informado.");
        }
        
        if (obj.getTipoMovimentacao().equals("")) {
            throw new ConsistirException("O campo TIPO MOVIMENTAÇÃO (Movimentação Estoque) deve ser informado.");
        }

        if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Movimentação Estoque) deve ser informado.");
        }
        
        Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoEstoque()), "O campo Centro Resultado Estoque (Movimentação Estoque) deve ser informado.");
        Uteis.checkState(obj.isTipoMovimentacaoTransferencia() && !Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoDestino()), "O campo UNIDADE ENSINO DESTINO (Movimentação Estoque) deve ser informado.");
        Uteis.checkState(obj.isTipoMovimentacaoTransferencia() && !Uteis.isAtributoPreenchido(obj.getCentroResultadoEstoqueDestino()), "O campo Centro Resultado Estoque Destino (Movimentação Estoque) deve ser informado.");
        Uteis.checkState(obj.isTipoMovimentacaoTransferencia() && obj.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsinoDestino().getCodigo()), "Para realizar uma Movimentação Estoque as Unidade de Ensino deve ser Diferentes.");
        if (obj.getQuantidade().doubleValue() == 0) {
            throw new ConsistirException("O campo QUANTIDADE (Movimentação Estoque) deve ser informado.");
        }
        if (obj.isTipoMovimentacaoEntrada() &&  obj.getPrecoUnitario().doubleValue() == 0) {
            throw new ConsistirException("O campo PREÇO UNITÁRIO (Movimentação Estoque) deve ser informado.");
        }
        if (obj.getMotivo().equals("")) {
            throw new ConsistirException("O campo MOTIVO (Movimentação Estoque) deve ser informado.");
        }
        if (!obj.isTipoMovimentacaoEntrada()){
        	obj.setPrecoUnitario(0.0);
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setTipoMovimentacao(tipoMovimentacao.toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setTipoMovimentacao("EP");
        setQuantidade(0.0);
        setPrecoUnitario(0.0);
        setMotivo("");
    }

    /**
     * Retorna o objeto da classe <code>Produto</code> relacionado com (
     * <code>MovimentacaoEstoque</code>).
     */
    public ProdutoServicoVO getProduto() {
        if (produto == null) {
            produto = new ProdutoServicoVO();
        }
        return (produto);
    }

    /**
     * Define o objeto da classe <code>Produto</code> relacionado com (
     * <code>MovimentacaoEstoque</code>).
     */
    public void setProduto(ProdutoServicoVO obj) {
        this.produto = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>MovimentacaoEstoque</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
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
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>MovimentacaoEstoque</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }
    
    public boolean isTipoMovimentacaoEntrada(){
    	return Uteis.isAtributoPreenchido(getTipoMovimentacao()) && getTipoMovimentacao().equals("EP");
    }
    
    public boolean isTipoMovimentacaoSaida(){
    	return Uteis.isAtributoPreenchido(getTipoMovimentacao()) && getTipoMovimentacao().equals("SP");
    }
    
    public boolean isTipoMovimentacaoTransferencia(){
    	return Uteis.isAtributoPreenchido(getTipoMovimentacao()) && getTipoMovimentacao().equals("TP");
    }

    public String getTipoMovimentacao() {
        return (tipoMovimentacao);
    }

   
    public String getTipoMovimentacao_Apresentar() {
        if (tipoMovimentacao.equals("EP")) {
            return "Entrada Produto";
        }else if (tipoMovimentacao.equals("SP")) {
            return "Saída Produto";
        }else if (tipoMovimentacao.equals("TP")) {
        	return "Transferência Produto";
        }
        return (tipoMovimentacao);
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public Date getData() {
        return (data);
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        if (data == null) {
            return "";
        }
        return (Uteis.getDataComHora(data));
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

    public UnidadeEnsinoVO getUnidadeEnsinoDestino() {
		unidadeEnsinoDestino = Optional.ofNullable(unidadeEnsinoDestino).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoDestino;
	}


	public void setUnidadeEnsinoDestino(UnidadeEnsinoVO unidadeEnsinoDestino) {
		this.unidadeEnsinoDestino = unidadeEnsinoDestino;
	}


	public CentroResultadoVO getCentroResultadoEstoque() {
		centroResultadoEstoque = Optional.ofNullable(centroResultadoEstoque).orElse(new CentroResultadoVO());
		return centroResultadoEstoque;
	}


	public void setCentroResultadoEstoque(CentroResultadoVO centroResultadoEstoque) {
		this.centroResultadoEstoque = centroResultadoEstoque;
	}


	public CentroResultadoVO getCentroResultadoEstoqueDestino() {
		centroResultadoEstoqueDestino = Optional.ofNullable(centroResultadoEstoqueDestino).orElse(new CentroResultadoVO());
		return centroResultadoEstoqueDestino;
	}


	public void setCentroResultadoEstoqueDestino(CentroResultadoVO centroResultadoEstoqueDestino) {
		this.centroResultadoEstoqueDestino = centroResultadoEstoqueDestino;
	}


	public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}
}
