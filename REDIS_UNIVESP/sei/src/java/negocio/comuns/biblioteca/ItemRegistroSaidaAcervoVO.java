package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSaidaAcervo;
import negocio.facade.jdbc.biblioteca.RegistroSaidaAcervo;

/**
 * Reponsável por manter os dados da entidade ItemRegistroSaidaAcervo. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see RegistroSaidaAcervo
 */
public class ItemRegistroSaidaAcervoVO extends SuperVO {

    private Integer codigo;
    private Integer registroSaidaAcervo;
    private String tipoSaida;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Exemplar </code>.
     */
    private ExemplarVO exemplar;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ItemRegistroSaidaAcervo</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ItemRegistroSaidaAcervoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ItemRegistroSaidaAcervoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ItemRegistroSaidaAcervoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getExemplar() == null) || (obj.getExemplar().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo EXEMPLAR (Item Registro Saida Acervo) deve ser informado.");
        }
        if (obj.getTipoSaida().equals("")) {
            throw new ConsistirException("O campo TIPO SAIDA (Item Registro Saida Acervo) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setTipoSaida(getTipoSaida().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTipoSaida("");
    }

    /**
     * Retorna o objeto da classe <code>Exemplar</code> relacionado com (
     * <code>ItemRegistroSaidaAcervo</code>).
     */
    public ExemplarVO getExemplar() {
        if (exemplar == null) {
            exemplar = new ExemplarVO();
        }
        return (exemplar);
    }

    /**
     * Define o objeto da classe <code>Exemplar</code> relacionado com (
     * <code>ItemRegistroSaidaAcervo</code>).
     */
    public void setExemplar(ExemplarVO obj) {
        this.exemplar = obj;
    }

    public String getTipoSaida() {
        if (tipoSaida == null) {
            tipoSaida = "";
        }
        return (tipoSaida);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoSaida_Apresentar() {
        return TipoSaidaAcervo.getDescricao(tipoSaida);
    }

    public void setTipoSaida(String tipoSaida) {
        this.tipoSaida = tipoSaida;
    }

    public Integer getRegistroSaidaAcervo() {
        if (registroSaidaAcervo == null) {
            registroSaidaAcervo = 0;
        }
        return (registroSaidaAcervo);
    }

    public void setRegistroSaidaAcervo(Integer registroSaidaAcervo) {
        this.registroSaidaAcervo = registroSaidaAcervo;
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
}
