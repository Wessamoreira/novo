package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.basico.Pessoa;

/**
 * Reponsável por manter os dados da entidade DisciplinasInteresse. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pessoa
 */
public class DisciplinasInteresseVO extends SuperVO {

    protected Integer professor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Disciplina </code>.
     */
    protected DisciplinaVO disciplina;
    public static final long serialVersionUID = 1L;
    
    /**
     * Atributo criado somente para apresentacao de relatorio
     */
    private String nomeDisciplina;

    /**
     * Construtor padrão da classe <code>DisciplinasInteresse</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisciplinasInteresseVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DisciplinasInteresseVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DisciplinasInteresseVO obj) throws ConsistirException {
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Disciplinas Interesse) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    /**
     * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>DisciplinasInteresse</code>).
     */
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return (disciplina);
    }

    /**
     * Define o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>DisciplinasInteresse</code>).
     */
    public void setDisciplina(DisciplinaVO obj) {
        this.disciplina = obj;
    }

    public Integer getProfessor() {
        return (professor);
    }

    public void setProfessor(Integer professor) {
        this.professor = professor;
    }

    /**
     * @return the nomeDisciplina
     */
    public String getNomeDisciplina() {
        if (nomeDisciplina == null) {
            nomeDisciplina = "";
        }
        return nomeDisciplina;
    }

    /**
     * @param nomeDisciplina the nomeDisciplina to set
     */
    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
}
