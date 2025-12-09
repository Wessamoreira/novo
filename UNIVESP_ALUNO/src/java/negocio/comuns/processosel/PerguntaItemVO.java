package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.Pergunta;

/**
 * Reponsável por manter os dados da entidade PerguntaItem. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pergunta
 */
@XmlRootElement(name = "perguntaItem")
public class PerguntaItemVO extends SuperVO {

    private Integer codigo;    
    private Integer ordem;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pergunta </code>.
     */
    public PerguntaVO perguntaPrincipalVO;
    public PerguntaVO perguntaVO;
    private Boolean respostaObrigatoria;

    public static final long serialVersionUID = 1L;    

    /**
     * Construtor padrão da classe <code>PerguntaItem</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PerguntaItemVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PerguntaItemVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PerguntaItemVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getPerguntaVO() == null) || (obj.getPerguntaVO().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PERGUNTA (Pergunta Item) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }
    
	/**
     * Retorna o objeto da classe <code>Pergunta</code> relacionado com (
     * <code>PerguntaItem</code>).
     */
    @XmlElement(name = "perguntaPrincipalVO")
    public PerguntaVO getPerguntaPrincipalVO() {
    	if(perguntaPrincipalVO == null) {
    		perguntaPrincipalVO = new PerguntaVO();
    	}
		return perguntaPrincipalVO;
	}

    /**
     * Define o objeto da classe <code>Pergunta</code> relacionado com (
     * <code>PerguntaQuestionario</code>).
     */
	public void setPerguntaPrincipalVO(PerguntaVO perguntaPrincipalVO) {
		this.perguntaPrincipalVO = perguntaPrincipalVO;
	}

	/**
     * Retorna o objeto da classe <code>Pergunta</code> relacionado com (
     * <code>PerguntaItem</code>).
     */
    @XmlElement(name = "perguntaVO")
	public PerguntaVO getPerguntaVO() {
    	if(perguntaVO == null) {
    		perguntaVO = new PerguntaVO();
    	}
		return perguntaVO;
	}

    /**
     * Define o objeto da classe <code>Pergunta</code> relacionado com (
     * <code>PerguntaQuestionario</code>).
     */
	public void setPerguntaVO(PerguntaVO perguntaVO) {
		this.perguntaVO = perguntaVO;
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

	public Boolean getRespostaObrigatoria() {
		if(respostaObrigatoria == null) {
			respostaObrigatoria =  true;
		}
		return respostaObrigatoria;
	}

	public void setRespostaObrigatoria(Boolean respostaObrigatoria) {
		this.respostaObrigatoria = respostaObrigatoria;
	}
	
	
}
