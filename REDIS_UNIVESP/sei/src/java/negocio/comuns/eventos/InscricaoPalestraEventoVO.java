package negocio.comuns.eventos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade InscricaoPalestraEvento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class InscricaoPalestraEventoVO extends SuperVO {

    private Integer codigo;
    private Double valorInscricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>InscricaoEvento </code>.
     */
    private InscricaoEventoVO inscricaoEvento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PalestraEvento </code>.
     */
    private PalestraEventoVO palestraEvento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>InscricaoPalestraEvento</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public InscricaoPalestraEventoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>InscricaoPalestraEventoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(InscricaoPalestraEventoVO obj) throws ConsistirException {
        if ((obj.getInscricaoEvento() == null) || (obj.getInscricaoEvento().getNrInscricao().intValue() == 0)) {
            throw new ConsistirException("O campo INSCRIÇÃO EVENTO (Inscrição Palestra Evento) deve ser informado.");
        }
        if ((obj.getPalestraEvento() == null) || (obj.getPalestraEvento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PALESTRA EVENTO (Inscrição Palestra Evento) deve ser informado.");
        }
        if (obj.getValorInscricao().intValue() == 0) {
            throw new ConsistirException("O campo VALOR INSCRIÇÃO (Inscrição Palestra Evento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setValorInscricao(0.0);
    }

    /**
     * Retorna o objeto da classe <code>PalestraEvento</code> relacionado com (
     * <code>InscricaoPalestraEvento</code>).
     */
    public PalestraEventoVO getPalestraEvento() {
        if (palestraEvento == null) {
            palestraEvento = new PalestraEventoVO();
        }
        return (palestraEvento);
    }

    /**
     * Define o objeto da classe <code>PalestraEvento</code> relacionado com (
     * <code>InscricaoPalestraEvento</code>).
     */
    public void setPalestraEvento(PalestraEventoVO obj) {
        this.palestraEvento = obj;
    }

    /**
     * Retorna o objeto da classe <code>InscricaoEvento</code> relacionado com (
     * <code>InscricaoPalestraEvento</code>).
     */
    public InscricaoEventoVO getInscricaoEvento() {
        if (inscricaoEvento == null) {
            inscricaoEvento = new InscricaoEventoVO();
        }
        return (inscricaoEvento);
    }

    /**
     * Define o objeto da classe <code>InscricaoEvento</code> relacionado com (
     * <code>InscricaoPalestraEvento</code>).
     */
    public void setInscricaoEvento(InscricaoEventoVO obj) {
        this.inscricaoEvento = obj;
    }

    public Double getValorInscricao() {
        return (valorInscricao);
    }

    public void setValorInscricao(Double valorInscricao) {
        this.valorInscricao = valorInscricao;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
