package negocio.comuns.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Download. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class DownloadVO extends SuperVO {

    private Integer codigo;
    private Date dataDownload;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Arquivo </code>.
     */
    private ArquivoVO arquivo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoa;
    private Integer turma;
    private Integer disciplina;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Download</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DownloadVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>DownloadVO</code>.
     */
    public static void validarUnicidade(List<DownloadVO> lista, DownloadVO obj) throws ConsistirException {
        for (DownloadVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DownloadVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DownloadVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getArquivo() == null) || (obj.getArquivo().getCodigo().intValue() == 0)) {
            throw new ConsistirException("Selecione o ARQUIVO para DOWNLOAD!");
        }
        if ((obj.getPessoa() == null) || (obj.getPessoa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("A PESSOA deve ser informada!");
        }
        if (obj.getDataDownload() == null) {
            throw new ConsistirException("A DATA de DOWNLOAD deve ser informada!");
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
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
        setDataDownload(new Date());
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Download</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Download</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    /**
     * Retorna o objeto da classe <code>Arquivo</code> relacionado com (
     * <code>Download</code>).
     */
    public ArquivoVO getArquivo() {
        if (arquivo == null) {
            arquivo = new ArquivoVO();
        }
        return (arquivo);
    }

    /**
     * Define o objeto da classe <code>Arquivo</code> relacionado com (
     * <code>Download</code>).
     */
    public void setArquivo(ArquivoVO obj) {
        this.arquivo = obj;
    }

    public Date getDataDownload() {
        if (dataDownload == null) {
            dataDownload = new Date();
        }
        return (dataDownload);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataDownload_Apresentar() {
        return (Uteis.getData(dataDownload));
    }

    public void setDataDownload(Date dataDownload) {
        this.dataDownload = dataDownload;
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

    /**
     * @return the turma
     */
    public Integer getTurma() {
        if (turma == null) {
            turma = 0;
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    /**
     * @return the disciplina
     */
    public Integer getDisciplina() {
        if (disciplina == null) {
            disciplina = 0;
        }
        return disciplina;
    }

    /**
     * @param disciplina the disciplina to set
     */
    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null){
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
    
    
}
