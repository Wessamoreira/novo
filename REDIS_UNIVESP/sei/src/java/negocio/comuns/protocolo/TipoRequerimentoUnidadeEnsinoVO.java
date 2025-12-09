package negocio.comuns.protocolo;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade TipoRequerimento. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoRequerimentoUnidadeEnsinoVO extends SuperVO {

    private Integer codigo;
    private TipoRequerimentoVO tipoRequerimento;
    private UnidadeEnsinoVO unidadeEnsino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TipoRequerimento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TipoRequerimentoUnidadeEnsinoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TipoRequerimentoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TipoRequerimentoUnidadeEnsinoVO obj) throws ConsistirException {
        if ((obj.getUnidadeEnsino() == null)
                || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Tipo Requerimento) deve ser informado.");
        }
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public TipoRequerimentoVO getTipoRequerimento() {
        if (tipoRequerimento == null) {
            tipoRequerimento = new TipoRequerimentoVO();
        }
        return (tipoRequerimento);
    }

    public void setTipoRequerimento(TipoRequerimentoVO obj) {
        this.tipoRequerimento = obj;
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
