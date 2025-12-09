package negocio.comuns.crm;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Segmentacao. Que permiti
 * segmentar os prospects, por interesse ou característica. Garantindo uma
 * melhor gestão de seus interesses.
 * @see SuperVO
*/

public class SegmentacaoItemVO extends SuperVO {
	
    private Integer codigo;
    private SegmentacaoVO segmentacao;
    private String valor;
    
	
    /**
     * Construtor padrão da classe <code>ProspectIndicacaoVO</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public SegmentacaoItemVO() {
        super();
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the segmentacao
     */
    public SegmentacaoVO getSegmentacao() {
        if (segmentacao == null) {
            segmentacao = new SegmentacaoVO();
        }
        return segmentacao;
    }

    /**
     * @param segmentacao the segmentacao to set
     */
    public void setSegmentacao(SegmentacaoVO segmentacao) {
        this.segmentacao = segmentacao;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        if (valor == null) {
            valor = "";
        }
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
  
}