package negocio.comuns.eventos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade MembroComissaoCientifica. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MembroComissaoCientificaVO extends SuperVO {

    private Integer codigo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO avaliador;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Evento </code>.
     */
    private EventoVO evento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>MembroComissaoCientifica</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public MembroComissaoCientificaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MembroComissaoCientificaVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MembroComissaoCientificaVO obj) throws ConsistirException {
        if ((obj.getAvaliador() == null) || (obj.getAvaliador().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo AVALIADOR (Membro Comissão Científica) deve ser informado.");
        }
        if ((obj.getEvento() == null) || (obj.getEvento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo EVENTO (Membro Comissão Científica) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    /**
     * Retorna o objeto da classe <code>Evento</code> relacionado com (
     * <code>MembroComissaoCientifica</code>).
     */
    public EventoVO getEvento() {
        if (evento == null) {
            evento = new EventoVO();
        }
        return (evento);
    }

    /**
     * Define o objeto da classe <code>Evento</code> relacionado com (
     * <code>MembroComissaoCientifica</code>).
     */
    public void setEvento(EventoVO obj) {
        this.evento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>MembroComissaoCientifica</code>).
     */
    public PessoaVO getAvaliador() {
        if (avaliador == null) {
            avaliador = new PessoaVO();
        }
        return (avaliador);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>MembroComissaoCientifica</code>).
     */
    public void setAvaliador(PessoaVO obj) {
        this.avaliador = obj;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
