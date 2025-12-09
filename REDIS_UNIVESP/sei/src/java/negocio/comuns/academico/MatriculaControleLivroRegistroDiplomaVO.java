package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade MatriculaControleLivroRegistroDiploma. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class MatriculaControleLivroRegistroDiplomaVO extends SuperVO {

    private Date dataEntregaRecibo;
    private Integer responsavelEntregaRecibo;
    private Boolean utilizouProcuracao;
    private Boolean certificadoRecebido;
    private String nomePessoaProcuracao;
    private String cpfPessoaProcuracao;
    protected MatriculaVO matricula;
    protected ControleLivroFolhaReciboVO controleLivroFolhaRecibo;
    public static final long serialVersionUID = 1L;

    public MatriculaControleLivroRegistroDiplomaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(MatriculaControleLivroRegistroDiplomaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getMatricula().getMatricula() == null)
                || (obj.getMatricula().getMatricula().equals(""))) {
            throw new ConsistirException("O campo MATRICULA (Matricula Controle Livro Registro) deve ser informado.");
        }
        if ((obj.getControleLivroFolhaRecibo() == null)
                || (obj.getControleLivroFolhaRecibo().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTROLE LIVRO FOLHA RECIBO (Matricula Controle Livro Registro) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setNomePessoaProcuracao(getNomePessoaProcuracao().toUpperCase());
        setCpfPessoaProcuracao(getCpfPessoaProcuracao().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setResponsavelEntregaRecibo(0);
        setUtilizouProcuracao(Boolean.FALSE);
        setNomePessoaProcuracao("");
        setCpfPessoaProcuracao("");
    }

    /**
     * Retorna o objeto da classe <code>ControleLivroFolhaRecibo</code> relacionado com (<code>MatriculaControleLivroRegistroDiploma</code>).
     */
    public ControleLivroFolhaReciboVO getControleLivroFolhaRecibo() {
        if (controleLivroFolhaRecibo == null) {
            controleLivroFolhaRecibo = new ControleLivroFolhaReciboVO();
        }
        return (controleLivroFolhaRecibo);
    }

    /**
     * Define o objeto da classe <code>ControleLivroFolhaRecibo</code> relacionado com (<code>MatriculaControleLivroRegistroDiploma</code>).
     */
    public void setControleLivroFolhaRecibo(ControleLivroFolhaReciboVO obj) {
        this.controleLivroFolhaRecibo = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (<code>MatriculaControleLivroRegistroDiploma</code>).
     */
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (<code>MatriculaControleLivroRegistroDiploma</code>).
     */
    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    public String getCpfPessoaProcuracao() {
        if (cpfPessoaProcuracao == null) {
            cpfPessoaProcuracao = "";
        }
        return (cpfPessoaProcuracao);
    }

    public void setCpfPessoaProcuracao(String cpfPessoaProcuracao) {
        this.cpfPessoaProcuracao = cpfPessoaProcuracao;
    }

    public String getNomePessoaProcuracao() {
        if (nomePessoaProcuracao == null) {
            nomePessoaProcuracao = "";
        }
        return (nomePessoaProcuracao);
    }

    public void setNomePessoaProcuracao(String nomePessoaProcuracao) {
        this.nomePessoaProcuracao = nomePessoaProcuracao;
    }

    public Boolean getUtilizouProcuracao() {
        return (utilizouProcuracao);
    }

    public Boolean isUtilizouProcuracao() {
        if (utilizouProcuracao == null) {
            utilizouProcuracao = Boolean.FALSE;
        }
        return (utilizouProcuracao);
    }

    public Boolean isCertificadoRecebido() {
        if (certificadoRecebido == null) {
            certificadoRecebido = Boolean.FALSE;
        }
        return (certificadoRecebido);
    }

    public void setUtilizouProcuracao(Boolean utilizouProcuracao) {
        this.utilizouProcuracao = utilizouProcuracao;
    }

    public Integer getResponsavelEntregaRecibo() {
        if (responsavelEntregaRecibo == null) {
            responsavelEntregaRecibo = 0;
        }
        return (responsavelEntregaRecibo);
    }

    public void setResponsavelEntregaRecibo(Integer responsavelEntregaRecibo) {
        this.responsavelEntregaRecibo = responsavelEntregaRecibo;
    }

    public Date getDataEntregaRecibo() {
        return (dataEntregaRecibo);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
     */
    public String getDataEntregaRecibo_Apresentar() {
        return (Uteis.getData(dataEntregaRecibo));
    }

    public void setDataEntregaRecibo(Date dataEntregaRecibo) {
        this.dataEntregaRecibo = dataEntregaRecibo;
    }

    public Boolean getCertificadoRecebido() {
        return certificadoRecebido;
    }

    public void setCertificadoRecebido(Boolean certificadoRecebido) {
        this.certificadoRecebido = certificadoRecebido;
    }
}
