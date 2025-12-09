package negocio.comuns.academico;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Nota. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "nota")
public class NotaVO extends SuperVO {

    protected String titulo;
    protected Double valor;
    protected String disciplina;
    protected Double frequencia;
    protected Double mediaFinal;
    private String valorTexto;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Nota</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public NotaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setTitulo("");
        setValor(0.0);
    }

    @XmlElement(name = "titulo")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @XmlElement(name = "valor")
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @XmlElement(name = "disciplina")
	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	@XmlElement(name = "frequencia")
	public Double getFrequencia() {
		if (frequencia == null) {
			frequencia = 0.0;
		}
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	@XmlElement(name = "mediaFinal")
	public Double getMediaFinal() {
		if (mediaFinal == null) {
			mediaFinal = 0.0;
		}
		return mediaFinal;
	}

	public void setMediaFinal(Double mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

	@XmlElement(name = "valorTexto")
	public String getValorTexto() {
		if (valorTexto == null) {
			valorTexto = "";
		}
		return valorTexto;
	}

	public void setValorTexto(String valorTexto) {
		this.valorTexto = valorTexto;
	}
}
