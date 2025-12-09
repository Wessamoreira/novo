package negocio.comuns.crm;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Segmentacao. Que permiti
 * segmentar os prospects, por interesse ou característica. Garantindo uma
 * melhor gestão de seus interesses.
 * @see SuperVO
*/

public class SegmentacaoVO extends SuperVO {
	
    private Integer codigo;
    private String descricao;
    private Boolean multiplaOpcao;
    private Boolean obrigatorio;
    
	
    /**
     * Construtor padrão da classe <code>ProspectIndicacaoVO</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public SegmentacaoVO() {
        super();
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the multiplaOpcao
     */
    public Boolean getMultiplaOpcao() {
        return multiplaOpcao;
    }

    /**
     * @param multiplaOpcao the multiplaOpcao to set
     */
    public void setMultiplaOpcao(Boolean multiplaOpcao) {
        this.multiplaOpcao = multiplaOpcao;
    }

    /**
     * @return the obrigatorio
     */
    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    /**
     * @param obrigatorio the obrigatorio to set
     */
    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }
  
}