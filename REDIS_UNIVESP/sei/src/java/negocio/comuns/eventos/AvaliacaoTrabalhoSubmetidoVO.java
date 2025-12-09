package negocio.comuns.eventos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade AvaliacaoTrabalhoSubmetido. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class AvaliacaoTrabalhoSubmetidoVO extends SuperVO {

    private Integer codigo;
    private String parecerComissao;
    private String justificativa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>MembroComissaoCientifica </code>.
     */
    private MembroComissaoCientificaVO membroComissaoCientifica;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>TrabalhoSubmetido </code>.
     */
    private TrabalhoSubmetidoVO trabalhoSubmetido;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>AvaliacaoTrabalhoSubmetido</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public AvaliacaoTrabalhoSubmetidoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AvaliacaoTrabalhoSubmetidoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AvaliacaoTrabalhoSubmetidoVO obj) throws ConsistirException {
        if ((obj.getMembroComissaoCientifica() == null)
                || (obj.getMembroComissaoCientifica().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo MEMBRO COMISSÃO CIENTÍFICA (Avaliação Trabalho Submetido) deve ser informado.");
        }
        if ((obj.getTrabalhoSubmetido() == null) || (obj.getTrabalhoSubmetido().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TRABALHO SUBMETIDO (Avaliação Trabalho Submetido) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>TrabalhoSubmetido</code> relacionado com
     * (<code>AvaliacaoTrabalhoSubmetido</code>).
     */
    public TrabalhoSubmetidoVO getTrabalhoSubmetido() {
        if (trabalhoSubmetido == null) {
            trabalhoSubmetido = new TrabalhoSubmetidoVO();
        }
        return (trabalhoSubmetido);
    }

    /**
     * Define o objeto da classe <code>TrabalhoSubmetido</code> relacionado com
     * (<code>AvaliacaoTrabalhoSubmetido</code>).
     */
    public void setTrabalhoSubmetido(TrabalhoSubmetidoVO obj) {
        this.trabalhoSubmetido = obj;
    }

    /**
     * Retorna o objeto da classe <code>MembroComissaoCientifica</code>
     * relacionado com (<code>AvaliacaoTrabalhoSubmetido</code>).
     */
    public MembroComissaoCientificaVO getMembroComissaoCientifica() {
        if (membroComissaoCientifica == null) {
            membroComissaoCientifica = new MembroComissaoCientificaVO();
        }
        return (membroComissaoCientifica);
    }

    /**
     * Define o objeto da classe <code>MembroComissaoCientifica</code>
     * relacionado com (<code>AvaliacaoTrabalhoSubmetido</code>).
     */
    public void setMembroComissaoCientifica(MembroComissaoCientificaVO obj) {
        this.membroComissaoCientifica = obj;
    }

    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return (justificativa);
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getParecerComissao() {
        if (parecerComissao == null) {
            parecerComissao = "";
        }
        return (parecerComissao);
    }

    public void setParecerComissao(String parecerComissao) {
        this.parecerComissao = parecerComissao;
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
