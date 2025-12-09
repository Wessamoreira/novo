package negocio.comuns.planoorcamentario;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.TimeSeriesCollection;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.sad.LegendaGraficoVO;
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
public class DetalhamentoPlanoOrcamentarioVO extends SuperVO {

	private static final long serialVersionUID = 1647771994585441566L;

	private Integer codigo;
    private PlanoOrcamentarioVO planoOrcamentario;
    private Integer departamentoSuperior;
    private DepartamentoVO departamento;
    private Double orcamentoRequeridoGestor;
    private Double orcamentoTotalDepartamento;
    private Double valorConsumido;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List<ItemMensalDetalhamentoPlanoOrcamentarioVO> listaItemMensalDetalhamentoPlanoOrcamentarioVOs;

    private List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> listaItemSolicitacaoOrcamentoVOs;
    private TimeSeriesCollection graficoPlanejadoRealizadoLinhaTempo;
    private List<LegendaGraficoVO> legendaPlanejadoRealizadoDespesaVOs;

    /**
     * Construtor padrão da classe <code>ItensProvisao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DetalhamentoPlanoOrcamentarioVO() {
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
    public static void validarDados(DetalhamentoPlanoOrcamentarioVO obj) throws ConsistirException {
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

    public PlanoOrcamentarioVO getPlanoOrcamentario() {
        if (planoOrcamentario == null) {
            planoOrcamentario = new PlanoOrcamentarioVO();
        }
        return planoOrcamentario;
    }

    public void setPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentario) {
        this.planoOrcamentario = planoOrcamentario;
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
    public Double getOrcamentoTotalDepartamento() {
        if (orcamentoTotalDepartamento == null) {
            orcamentoTotalDepartamento = 0.0;
        }
        return orcamentoTotalDepartamento;
    }

    /**
     * @param orcamentoTotalDepartamento the orcamentoTotalDepartamento to set
     */
    public void setOrcamentoTotalDepartamento(Double orcamentoTotalDepartamento) {
        this.orcamentoTotalDepartamento = orcamentoTotalDepartamento;
    }

    /**
     * @return the departamentoSuperior
     */
    public Integer getDepartamentoSuperior() {
        if (departamentoSuperior == null) {
            departamentoSuperior = 0;
        }
        return departamentoSuperior;
    }

    /**
     * @param departamentoSuperior the departamentoSuperior to set
     */
    public void setDepartamentoSuperior(Integer departamentoSuperior) {
        this.departamentoSuperior = departamentoSuperior;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public String getUnidadeEnsinoEDepartamento() {
        return getUnidadeEnsinoVO().getNome() + " - " + getDepartamento().getNome();
    }

    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> getListaItemMensalDetalhamentoPlanoOrcamentarioVOs() {
        if (listaItemMensalDetalhamentoPlanoOrcamentarioVOs == null) {
            listaItemMensalDetalhamentoPlanoOrcamentarioVOs = new ArrayList<ItemMensalDetalhamentoPlanoOrcamentarioVO>();
        }
        return listaItemMensalDetalhamentoPlanoOrcamentarioVOs;
    }

    public void setListaItemMensalDetalhamentoPlanoOrcamentarioVOs(List<ItemMensalDetalhamentoPlanoOrcamentarioVO> listaItemMensalDetalhamentoPlanoOrcamentarioVOs) {
        this.listaItemMensalDetalhamentoPlanoOrcamentarioVOs = listaItemMensalDetalhamentoPlanoOrcamentarioVOs;
    }

    public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> getListaItemSolicitacaoOrcamentoVOs() {
        if (listaItemSolicitacaoOrcamentoVOs == null) {
            listaItemSolicitacaoOrcamentoVOs = new ArrayList<>(0);
        }
        return listaItemSolicitacaoOrcamentoVOs;
    }

    public void setListaItemSolicitacaoOrcamentoVOs(List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> listaItemSolicitacaoOrcamentoVOs) {
        this.listaItemSolicitacaoOrcamentoVOs = listaItemSolicitacaoOrcamentoVOs;
    }

    public Double getValorConsumido() {
        if (valorConsumido == null) {
            valorConsumido = 0.0;
        }
        return valorConsumido;
    }

    public void setValorConsumido(Double valorConsumido) {
        this.valorConsumido = valorConsumido;
    }

    public TimeSeriesCollection getGraficoPlanejadoRealizadoLinhaTempo() {
        if (graficoPlanejadoRealizadoLinhaTempo == null) {
            graficoPlanejadoRealizadoLinhaTempo = new TimeSeriesCollection();
        }
        return graficoPlanejadoRealizadoLinhaTempo;
    }

    public void setGraficoPlanejadoRealizadoLinhaTempo(TimeSeriesCollection graficoPlanejadoRealizadoLinhaTempo) {
        this.graficoPlanejadoRealizadoLinhaTempo = graficoPlanejadoRealizadoLinhaTempo;
    }

    public List<LegendaGraficoVO> getLegendaPlanejadoRealizadoDespesaVOs() {
        if (legendaPlanejadoRealizadoDespesaVOs == null) {
            legendaPlanejadoRealizadoDespesaVOs = new ArrayList<>(0);
        }
        return legendaPlanejadoRealizadoDespesaVOs;
    }

    public void setLegendaPlanejadoRealizadoDespesaVOs(List<LegendaGraficoVO> legendaPlanejadoRealizadoDespesaVOs) {
        this.legendaPlanejadoRealizadoDespesaVOs = legendaPlanejadoRealizadoDespesaVOs;
    }

    
    
}
