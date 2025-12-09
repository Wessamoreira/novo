package negocio.comuns.crm;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade RegistroEntrada. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
*/

public class ProspectSegmentacaoVO extends SuperVO {
	
    private Integer codigo;
    private SegmentacaoVO segmentacao;
    private SegmentacaoItemVO segmentacaoItem;
    private ProspectsVO prospects;
	
    /**
     * Construtor padrão da classe <code>ProspectIndicacaoVO</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public ProspectSegmentacaoVO() {
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
     * @return the prospects
     */
    public ProspectsVO getProspects() {
        if (prospects == null) {
            prospects = new ProspectsVO();
        }
        return prospects;
    }

    /**
     * @param prospects the prospects to set
     */
    public void setProspects(ProspectsVO prospects) {
        this.prospects = prospects;
    }

    /**
     * @return the segmentacaoItem
     */
    public SegmentacaoItemVO getSegmentacaoItem() {
        if (segmentacaoItem == null) {
            segmentacaoItem = new SegmentacaoItemVO();
        }
        return segmentacaoItem;
    }

    /**
     * @param segmentacaoItem the segmentacaoItem to set
     */
    public void setSegmentacaoItem(SegmentacaoItemVO segmentacaoItem) {
        this.segmentacaoItem = segmentacaoItem;
    }
   
}