package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.Questionario;

/**
 * Reponsável por manter os dados da entidade PerguntaQuestionario. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Questionario
 */
@XmlRootElement(name = "perguntaQuestionario")
public class PerguntaQuestionarioVO extends SuperVO {

    private Integer codigo;
    private Integer questionario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pergunta </code>.
     */
    public PerguntaVO pergunta;
    public Boolean respostaObrigatoria;
    public Boolean perguntaRestrita;
    private Integer ordem;
    public static final long serialVersionUID = 1L;
    
    /**
     * TRansiente- usando para persistir a resposta a responder
     */
    private RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO;

    /**
     * Construtor padrão da classe <code>PerguntaQuestionario</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PerguntaQuestionarioVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PerguntaQuestionarioVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PerguntaQuestionarioVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getPergunta() == null) || (obj.getPergunta().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PERGUNTA (Pergunta Questionário) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    @XmlElement(name = "respostaObrigatoria")
    public Boolean getRespostaObrigatoria() {
        if (respostaObrigatoria == null) {
            respostaObrigatoria = true;
        }
        return respostaObrigatoria;
    }

    public void setRespostaObrigatoria(Boolean respostaObrigatoria) {
        this.respostaObrigatoria = respostaObrigatoria;
    }

    /**
     * Retorna o objeto da classe <code>Pergunta</code> relacionado com (
     * <code>PerguntaQuestionario</code>).
     */
    @XmlElement(name = "pergunta")
    public PerguntaVO getPergunta() {
        if (pergunta == null) {
            pergunta = new PerguntaVO();
        }
        return (pergunta);
    }

    /**
     * Define o objeto da classe <code>Pergunta</code> relacionado com (
     * <code>PerguntaQuestionario</code>).
     */
    public void setPergunta(PerguntaVO obj) {
        this.pergunta = obj;
    }

    @XmlElement(name = "questionario")
    public Integer getQuestionario() {
        return (questionario);
    }

    public void setQuestionario(Integer questionario) {
        this.questionario = questionario;
    }

    
    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "ordem")
	public Integer getOrdem() {
		if(ordem == null){
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@XmlElement(name = "perguntaRestrita")
	public Boolean getPerguntaRestrita() {
		if (perguntaRestrita == null) {
			perguntaRestrita = true;
		}
		return perguntaRestrita;
	}

	public void setPerguntaRestrita(Boolean perguntaRestrita) {
		this.perguntaRestrita = perguntaRestrita;
	}
    

	@XmlElement(name = "respostaAvaliacaoInstitucionalDWVO")
	public RespostaAvaliacaoInstitucionalDWVO getRespostaAvaliacaoInstitucionalDWVO() {
		if(respostaAvaliacaoInstitucionalDWVO == null){
			respostaAvaliacaoInstitucionalDWVO = new RespostaAvaliacaoInstitucionalDWVO();
		}
		return respostaAvaliacaoInstitucionalDWVO;
	}

	public void setRespostaAvaliacaoInstitucionalDWVO(RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO) {
		this.respostaAvaliacaoInstitucionalDWVO = respostaAvaliacaoInstitucionalDWVO;
	}
}
