package negocio.comuns.extensao;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.extensao.CursoExtensao;

/**
 * Reponsável por manter os dados da entidade ProfessorCursoExtensao. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see CursoExtensao
 */
public class ProfessorCursoExtensaoVO extends SuperVO {

    private Integer codigo;
    private String tipoProfessor;
    private Integer cursoExtensao;
    private Integer cargaHoraria;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoaProfessorCursoExtensao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProfessorCursoExtensao</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ProfessorCursoExtensaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProfessorCursoExtensaoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProfessorCursoExtensaoVO obj) throws ConsistirException {
        if (obj.getTipoProfessor().equals("")) {
            throw new ConsistirException("O campo TIPO PROFESSOR (Professor Curso Extensão) deve ser informado.");
        }
        if (obj.getCargaHoraria().intValue() == 0) {
            throw new ConsistirException("O campo CARGA HORÁRIA (Professor Curso Extensão) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTipoProfessor("");
        setCargaHoraria(0);
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ProfessorCursoExtensao</code>).
     */
    public PessoaVO getPessoaProfessorCursoExtensao() {
        if (pessoaProfessorCursoExtensao == null) {
            pessoaProfessorCursoExtensao = new PessoaVO();
        }
        return (pessoaProfessorCursoExtensao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ProfessorCursoExtensao</code>).
     */
    public void setPessoaProfessorCursoExtensao(PessoaVO obj) {
        this.pessoaProfessorCursoExtensao = obj;
    }

    public Integer getCargaHoraria() {
        return (cargaHoraria);
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Integer getCursoExtensao() {
        return (cursoExtensao);
    }

    public void setCursoExtensao(Integer cursoExtensao) {
        this.cursoExtensao = cursoExtensao;
    }

    public String getTipoProfessor() {
        return (tipoProfessor);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoProfessor_Apresentar() {
        if (tipoProfessor.equals("PR")) {
            return "Professor";
        }
        if (tipoProfessor.equals("AL")) {
            return "Aluno";
        }
        if (tipoProfessor.equals("FU")) {
            return "Funcionário";
        }
        if (tipoProfessor.equals("CO")) {
            return "Contrato";
        }
        return (tipoProfessor);
    }

    public void setTipoProfessor(String tipoProfessor) {
        this.tipoProfessor = tipoProfessor;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
