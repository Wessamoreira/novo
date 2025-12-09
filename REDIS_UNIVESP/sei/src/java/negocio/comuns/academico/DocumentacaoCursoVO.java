package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Curso;

/**
 * Reponsável por manter os dados da entidade DocumentacaoCurso. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Curso
 */
public class DocumentacaoCursoVO extends SuperVO {

    private Integer codigo;
    private Integer curso;
    private Boolean gerarSuspensaoMatricula;
    /**
     * Boolean utilizado para impedir uma renovação de matrícula
     * caso o aluno esteja devendo este documento.
     */
    private Boolean impedirRenovacaoMatricula;
    private TipoDocumentoVO tipoDeDocumentoVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DocumentacaoCurso</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DocumentacaoCursoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DocumentacaoCursoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DocumentacaoCursoVO obj) throws ConsistirException {
        if ((obj.getTipoDeDocumentoVO() == null) || (obj.getTipoDeDocumentoVO().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TIPO DE DOCUMENTO deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    public Integer getCurso() {
        return (curso);
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the tipoDeDocumentoVO
     */
    public TipoDocumentoVO getTipoDeDocumentoVO() {
        if (tipoDeDocumentoVO == null) {
            tipoDeDocumentoVO = new TipoDocumentoVO();
        }
        return tipoDeDocumentoVO;
    }

    /**
     * @param tipoDeDocumentoVO
     *            the tipoDeDocumentoVO to set
     */
    public void setTipoDeDocumentoVO(TipoDocumentoVO tipoDeDocumentoVO) {
        this.tipoDeDocumentoVO = tipoDeDocumentoVO;
    }

    /**
     * @return the gerarSuspensaoMatricula
     */
    public Boolean getGerarSuspensaoMatricula() {
        if (gerarSuspensaoMatricula == null) {
            gerarSuspensaoMatricula = Boolean.FALSE;
        }
        return gerarSuspensaoMatricula;
    }

    /**
     * @param gerarSuspensaoMatricula the gerarSuspensaoMatricula to set
     */
    public void setGerarSuspensaoMatricula(Boolean gerarSuspensaoMatricula) {
        this.gerarSuspensaoMatricula = gerarSuspensaoMatricula;
    }

    /**
     * @return the impedirRenovacaoMatricula
     */
    public Boolean getImpedirRenovacaoMatricula() {
        if (impedirRenovacaoMatricula == null) {
            impedirRenovacaoMatricula = Boolean.FALSE;
        }
        return impedirRenovacaoMatricula;
    }

    /**
     * @param impedirRenovacaoMatricula the impedirRenovacaoMatricula to set
     */
    public void setImpedirRenovacaoMatricula(Boolean impedirRenovacaoMatricula) {
        this.impedirRenovacaoMatricula = impedirRenovacaoMatricula;
    }
}
