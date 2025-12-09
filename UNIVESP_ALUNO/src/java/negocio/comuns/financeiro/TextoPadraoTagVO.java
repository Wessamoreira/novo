package negocio.comuns.financeiro;

import jakarta.faces. context.FacesContext;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade TextoPadrao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class TextoPadraoTagVO extends SuperVO {

    private Integer codigo;
    private String tag;
    private Integer textoPadrao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TextoPadrao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TextoPadraoTagVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TextoPadraoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TextoPadraoTagVO obj) throws ConsistirException {
        // if (!obj.isValidarDados().booleanValue()) {
        // return;
        // }
        // if (obj.getTag().equals("")) {
        // throw new
        // ConsistirException("O campo TAG ( Texto Padrao Tag) deve ser informado.");
        // }
    }

    protected FacesContext context() {
        return (FacesContext.getCurrentInstance());
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTag() {
        if (tag == null) {
            tag = "";
        }
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTextoPadrao() {
        if (textoPadrao == null) {
            textoPadrao = 0;
        }
        return textoPadrao;
    }

    public void setTextoPadrao(Integer TextoPadrao) {
        this.textoPadrao = TextoPadrao;
    }
}
