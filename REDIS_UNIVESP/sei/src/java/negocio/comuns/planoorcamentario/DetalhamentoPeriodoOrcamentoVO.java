package negocio.comuns.planoorcamentario;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
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
public class DetalhamentoPeriodoOrcamentoVO extends SuperVO {

    private Integer codigo;
    private MesAnoEnum mes;
    private String ano;
    private Double orcamentoRequeridoGestor;
    private Double orcamentoTotal;
    private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO;

    public static final long serialVersionUID = 1L;

    /* Transites 
     * 
     */
    private Double valorRemanejar;
    private Double valorConsumido;
    private List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs;
    
    /**
     * Construtor padrão da classe <code>ItensProvisao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DetalhamentoPeriodoOrcamentoVO() {
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
    public static void validarDados(DetalhamentoPeriodoOrcamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
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
     * @return the orcamentoRequeridoGestor
     */
    public Double getOrcamentoRequeridoGestor() {
        if (orcamentoRequeridoGestor == null) {
            orcamentoRequeridoGestor = 0.0;
        }
        return orcamentoRequeridoGestor;
    }

    /**
     * @param orcamentoRequeridoGestor the orcamentoRequeridoGestor to set
     */
    public void setOrcamentoRequeridoGestor(Double orcamentoRequeridoGestor) {
        this.orcamentoRequeridoGestor = orcamentoRequeridoGestor;
    }

    /**
     * @return the orcamentoTotalDepartamento
     */
    public Double getOrcamentoTotal() {
        if (orcamentoTotal == null) {
            orcamentoTotal = getValorRemanejar();
        }
        return orcamentoTotal;
    }

    /**
     * @param orcamentoTotalDepartamento the orcamentoTotalDepartamento to set
     */
    public void setOrcamentoTotal(Double orcamentoTotal) {
        this.orcamentoTotal = orcamentoTotal;
    }

    /**
     * @return the mes
     */
    public MesAnoEnum getMes() {
        if (mes == null) {
            mes = MesAnoEnum.JANEIRO;
        }
        return mes;
    }
    
    public String getMesApresentar() {
    	return getMes().getMes();
    }

    /**
     * @param mes the mes to set
     */
    public void setMes(MesAnoEnum mes) {
        this.mes = mes;
    }

    /**
     * @return the ano
     */
    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(String ano) {
        this.ano = ano;
    }

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if(itemSolicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioVO = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public List<DetalhamentoPeriodoOrcamentoVO> getDetalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs() {
		if(detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs == null) {
			detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs = new ArrayList<DetalhamentoPeriodoOrcamentoVO>(0);
		}
		return detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs;
	}

	public void setDetalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs(
			List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs) {
		this.detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs = detalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs;
	}

	public String getOrdenacao() {
		return getAno() + MesAnoEnum.valueOf(getMes().name()).getKey();
	}

	public Double getValorRemanejar() {
		if(valorRemanejar == null) {
			valorRemanejar = 0.0;
		}
		return valorRemanejar;
	}

	public void setValorRemanejar(Double valorRemanejar) {
		this.valorRemanejar = valorRemanejar;
	}

	public Double getValorDisponivel() {
		return getOrcamentoTotal() - getValorConsumido() >= 0 ? getOrcamentoTotal() - getValorConsumido() : 0.0 ;
	}
	
	public Double getValorConsumido() {
		if(valorConsumido == null) {
			valorConsumido = 0.0;
		}
		return valorConsumido;
	}

	public void setValorConsumido(Double valorConsumido) {
		this.valorConsumido = valorConsumido;
	}


    
}
