package negocio.comuns.administrativo;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.basico.Pessoa;

/**
 * Reponsável por manter os dados da entidade FormacaoAcademica. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pessoa
 */
public class PessoaPreInscricaoCursoVO extends SuperVO {

    private Integer codigo;
    private CursoVO curso;
    private Integer pessoa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>FormacaoAcademica</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PessoaPreInscricaoCursoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FormacaoAcademicaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PessoaPreInscricaoCursoVO obj) throws ConsistirException {
        if (obj.getCurso() == null || obj.getCurso().getCodigo() == 0) {
            throw new ConsistirException("O campo CURSO (Formação Acadêmica) deve ser informado.");
        }
    }

    public Integer getPessoa() {
        if (pessoa == null) {
            pessoa = 0;
        }
        return (pessoa);
    }

    public void setPessoa(Integer pessoa) {
        this.pessoa = pessoa;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
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
