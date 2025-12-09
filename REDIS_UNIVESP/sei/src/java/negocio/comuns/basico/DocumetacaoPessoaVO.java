package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Matricula;

/**
 * Reponsável por manter os dados da entidade DocumetacaoMatricula. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
public class DocumetacaoPessoaVO extends SuperVO {

    private Integer codigo;
    private String situacao;
    private Integer pessoa;
    private Boolean entregue;
    private Date dataEntrega;
    private UsuarioVO usuario;
    private TipoDocumentoVO tipoDeDocumentoVO;
    
    private ArquivoVO arquivoVO;
    private Boolean excluirArquivo;
    private ArquivoVO arquivoVOVerso;
    private Boolean assinarDigitalmente;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DocumetacaoMatricula</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DocumetacaoPessoaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DocumetacaoMatriculaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DocumetacaoPessoaVO obj) throws ConsistirException {
        if ((obj.getTipoDeDocumentoVO() == null) || (obj.getTipoDeDocumentoVO().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TIPO DE DOCUMENTO deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Documetação Apresentada Matrícula) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setSituacao(new String("OK"));
        setEntregue(Boolean.FALSE);
        setDataEntrega(new Date());
    }

    public Boolean getEntregue() {
        return (entregue);
    }

    public Boolean isEntregue() {
        return (entregue);
    }

    public void setEntregue(Boolean entregue) {
        this.entregue = entregue;
    }

    public String getSituacao() {
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("PE")) {
            return "Pendente";
        }
        if (situacao.equals("OK")) {
            return "OK";
        }
        if (situacao.equals("EI")) {
            return "Entregue Incorretamente";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

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
     * @return the dataEntrega
     */
    public Date getDataEntrega() {
        return dataEntrega;
    }

    /**
     * @param dataEntrega
     *            the dataEntrega to set
     */
    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    /**
     * @return the usuario
     */
    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    /**
     * @param usuario
     *            the usuario to set
     */
    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }

    public Boolean getExcluirArquivo() {
        if (excluirArquivo == null) {
            excluirArquivo = false;
        }
        return excluirArquivo;
    }

    public void setExcluirArquivo(Boolean excluirArquivo) {
        this.excluirArquivo = excluirArquivo;
    }

    public boolean getIsPossuiArquivo() {
        return !getArquivoVO().getNome().equals("");
    }

    public boolean getIsPossuiArquivoBanco() {
        return !getArquivoVO().getCodigo().equals(0);
    }

    public boolean getDocumentoEntregue() {
        return getEntregue();
    }

    /**
     * @return the pessoa
     */
    public Integer getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Integer pessoa) {
        this.pessoa = pessoa;
    }

    public String getOrdenacaoNomeTipoDeDocumento(){
        return getTipoDeDocumentoVO().getNome();
    }
    
	public ArquivoVO getArquivoVOVerso() {
		if(arquivoVOVerso == null) {
			arquivoVOVerso = new ArquivoVO();
		}
		return arquivoVOVerso;
	}

	public void setArquivoVOVerso(ArquivoVO arquivoVOVerso) {
		this.arquivoVOVerso = arquivoVOVerso;
	}

	public boolean getIsPossuiArquivoVerso() {
	   return !getArquivoVOVerso().getNome().equals("");
	}
	

	public Boolean getAssinarDigitalmente() {
		if(assinarDigitalmente == null){
			assinarDigitalmente = false;
		}
		return assinarDigitalmente;
	}

	public void setAssinarDigitalmente(Boolean assinarDigitalmente) {
		this.assinarDigitalmente = assinarDigitalmente;
	}
}
