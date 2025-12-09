package negocio.comuns.administrativo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade TipoMidiaCaptacao. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "tipoMidiaCaptacao")
public class TipoMidiaCaptacaoVO extends SuperVO {

    private Integer codigo;
    private String nomeMidia;
    private String descricaoMidia;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TipoMidiaCaptacao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TipoMidiaCaptacaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TipoMidiaCaptacaoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TipoMidiaCaptacaoVO obj) throws ConsistirException {
        if (obj.getNomeMidia().equals("")) {
            throw new ConsistirException("O campo NOME (Tipo Mídia Captação) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNomeMidia("");
        setDescricaoMidia("");
    }

    @XmlElement(name = "descricaoMidia")
    public String getDescricaoMidia() {
        return (descricaoMidia);
    }

    public void setDescricaoMidia(String descricaoMidia) {
        this.descricaoMidia = descricaoMidia;
    }

    @XmlElement(name = "nomeMidia")
    public String getNomeMidia() {
        return (nomeMidia);
    }

    public void setNomeMidia(String nomeMidia) {
        this.nomeMidia = nomeMidia;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
