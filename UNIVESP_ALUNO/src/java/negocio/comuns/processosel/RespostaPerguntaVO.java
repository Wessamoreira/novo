package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.Pergunta;

/**
 * Reponsável por manter os dados da entidade RespostaPergunta. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pergunta
 */
@XmlRootElement(name = "respostaPergunta")
public class RespostaPerguntaVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private String tipoPergunta;
    private Integer pergunta;
    private String texto;
    private Boolean selecionado;
    private Boolean apresentarRespostaAdicional;
    private Integer disciplina;
    private Integer qtdeRespostas;
    private Integer avaliacaoInstitucionalPresencialItemResposta;
    private Integer ordem;
    private Integer agrupador;
    private Double nota;
    
    /***
     * Transiente
     */
    private String respostaAdicional;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RespostaPergunta</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RespostaPerguntaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RespostaPerguntaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(RespostaPerguntaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Resposta Pergunta) deve ser informado.");
        }
        // if (obj.getTexto().equals("")) {
        // throw new
        // ConsistirException("O campo TEXTO (Resposta Pergunta) deve ser informado.");
        // }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setTexto("");
        setTipoPergunta("");
    }

    @XmlElement(name = "multiplaEscolha")
    public Boolean getMultiplaEscolha() {
        if (tipoPergunta.equals("ME")) {
            return true;
        }
        return false;
    }

    @XmlElement(name = "tipoPergunta")
    public String getTipoPergunta() {
		if (tipoPergunta == null) {
    		tipoPergunta = "";
    	}
        return tipoPergunta;
    }

    public void setTipoPergunta(String tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    @XmlElement(name = "texto")
    public String getTexto() {
		if (texto == null) {
    		texto = "";
    	}
        return (texto);
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @XmlElement(name = "pergunta")
    public Integer getPergunta() {
		if (pergunta == null) {
    		pergunta = 0;
    	}
        return (pergunta);
    }

    public void setPergunta(Integer pergunta) {
        this.pergunta = pergunta;
    }

    @XmlElement(name = "descricao")
    public String getDescricao() {
		if (descricao == null) {
    		descricao = "";
    	}
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
		if (codigo == null) {
    		codigo = 0;
    	}
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "selecionado")
    public Boolean getSelecionado() {
        if (selecionado == null){
            selecionado = Boolean.FALSE;
        }
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    @XmlElement(name = "disciplina")
    public Integer getDisciplina() {
        if (disciplina == null) {
            disciplina = 0;
        }
        return disciplina;
    }

    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    @XmlElement(name = "qtdeRespostas")
    public Integer getQtdeRespostas() {
        if (qtdeRespostas == null) {
            qtdeRespostas = 0;
        }
        return qtdeRespostas;
    }

    public void setQtdeRespostas(Integer qtdeRespostas) {
        this.qtdeRespostas = qtdeRespostas;
    }

    @XmlElement(name = "avaliacaoInstitucionalPresencialItemResposta")
    public Integer getAvaliacaoInstitucionalPresencialItemResposta() {
        if (avaliacaoInstitucionalPresencialItemResposta == null) {
            avaliacaoInstitucionalPresencialItemResposta = 0;
        }
        return avaliacaoInstitucionalPresencialItemResposta;
    }

    public void setAvaliacaoInstitucionalPresencialItemResposta(Integer avaliacaoInstitucionalPresencialItemResposta) {
        this.avaliacaoInstitucionalPresencialItemResposta = avaliacaoInstitucionalPresencialItemResposta;
    }

    @XmlElement(name = "apresentarRespostaAdicional")
	public Boolean getApresentarRespostaAdicional() {
		if(apresentarRespostaAdicional == null){
			apresentarRespostaAdicional = false;
		}
		return apresentarRespostaAdicional;
	}

	public void setApresentarRespostaAdicional(Boolean apresentarRespostaAdicional) {
		this.apresentarRespostaAdicional = apresentarRespostaAdicional;
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

	@XmlElement(name = "respostaAdicional")
	public String getRespostaAdicional() {
		if(respostaAdicional == null){
			respostaAdicional = "";
		}
		return respostaAdicional;
	}

	public void setRespostaAdicional(String respostaAdicional) {
		this.respostaAdicional = respostaAdicional;
	}
    
	@XmlElement(name = "agrupador")
	public Integer getAgrupador() {
		if (agrupador == null) {
			agrupador = 0;
		}
		return agrupador;
	}

	public void setAgrupador(Integer agrupador) {
		this.agrupador = agrupador;
	}

	@XmlElement(name = "nota")
	public Double getNota() {
		if(nota == null){
			nota = 0.0;
		}
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}
	
}
