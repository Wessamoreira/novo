package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.facade.jdbc.biblioteca.RegistroEntradaAcervo;

/**
 * Reponsável por manter os dados da entidade ItemRegistroEntradaAcervo. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see RegistroEntradaAcervo
 */
public class ItemRegistroEntradaAcervoVO extends SuperVO {

    private Integer codigo;
    private Integer registroEntradaAcervo;
    private String tipoEntrada;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Exemplar </code>.
     */
    private ExemplarVO exemplar;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ItemRegistroEntradaAcervo</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ItemRegistroEntradaAcervoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ItemRegistroEntradaAcervoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ItemRegistroEntradaAcervoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getExemplar() == null) || (obj.getExemplar().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo EXEMPLAR (Item Registro Entrada Acervo) deve ser informado.");
        }
        if (!("BA, EX, CE, DE, ER, IT".contains(obj.getExemplar().getSituacaoAtual())) && !obj.getTipoEntrada().equals(TipoEntradaAcervo.ENTRADA_SIMPLES.getValor())) {
            throw new ConsistirException("Não é possível registrar a entrada de um EXEMPLAR com a situação "+obj.getExemplar().getSituacaoAtualApresentar()+".");
        }
        if (obj.getTipoEntrada().equals("")) {
            throw new ConsistirException("O campo TIPO ENTRADA (Item Registro Entrada Acervo) deve ser informado.");
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
        setTipoEntrada(getTipoEntrada().toUpperCase());
    }

    /**
     * Retorna o objeto da classe <code>Exemplar</code> relacionado com (
     * <code>ItemRegistroEntradaAcervo</code>).
     */
    public ExemplarVO getExemplar() {
        if (exemplar == null) {
            exemplar = new ExemplarVO();
        }
        return (exemplar);
    }

    /**
     * Define o objeto da classe <code>Exemplar</code> relacionado com (
     * <code>ItemRegistroEntradaAcervo</code>).
     */
    public void setExemplar(ExemplarVO obj) {
        this.exemplar = obj;
    }

    public String getTipoEntrada() {
        if (tipoEntrada == null) {
            tipoEntrada = "";
        }
        return (tipoEntrada);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoEntrada_Apresentar() {
        return TipoEntradaAcervo.getDescricao(tipoEntrada);
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public Integer getRegistroEntradaAcervo() {
        if (registroEntradaAcervo == null) {
            registroEntradaAcervo = 0;
        }
        return (registroEntradaAcervo);
    }

    public void setRegistroEntradaAcervo(Integer registroEntradaAcervo) {
        this.registroEntradaAcervo = registroEntradaAcervo;
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
