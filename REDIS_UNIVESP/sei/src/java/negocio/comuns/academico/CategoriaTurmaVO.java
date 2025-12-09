package negocio.comuns.academico;

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
public class CategoriaTurmaVO extends SuperVO {

    private Integer codigo;
    private TurmaVO turma;
    private TipoCategoriaVO tipoCategoria;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TipoMidiaCaptacao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CategoriaTurmaVO() {
        super();
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
    public static void validarDados(CategoriaTurmaVO obj) throws ConsistirException {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo TURMA (Categoria Turma) deve ser informado.");
        }
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

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public TipoCategoriaVO getTipoCategoria() {
		if (tipoCategoria == null) {
			tipoCategoria = new TipoCategoriaVO();
		}
		return tipoCategoria;
	}

	public void setTipoCategoria(TipoCategoriaVO tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}
}
