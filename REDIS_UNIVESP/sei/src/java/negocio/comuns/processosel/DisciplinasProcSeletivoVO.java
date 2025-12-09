package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade DisciplinasProcSeletivo. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement
public class DisciplinasProcSeletivoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String tipoDisciplina;
    private String descricao;
    private String requisitos;
    private String bibliografia;
    private Boolean disciplinaIdioma;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DisciplinasProcSeletivo</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisciplinasProcSeletivoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DisciplinasProcSeletivoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DisciplinasProcSeletivoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Disciplinas Processo Seletivo) deve ser informado.");
        }
        if ((obj.getTipoDisciplina() == null) || (obj.getTipoDisciplina().equals(""))) {
            throw new ConsistirException("O campo TIPO DISCIPLINAS (Disciplinas Processo Seletivo) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setTipoDisciplina("");
        setDescricao("");
        setRequisitos("");
        setBibliografia("");
    }

    public String getBibliografia() {
        return (bibliografia);
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    public String getRequisitos() {
        return (requisitos);
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoDisciplina() {
        return (tipoDisciplina);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDisciplina_Apresentar() {
        if (tipoDisciplina.equals("OP")) {
            return "Opcional";
        }
        if (tipoDisciplina.equals("AP")) {
            return "Aptidão";
        }
        if (tipoDisciplina.equals("NO")) {
            return "Normal";
        }
        return (tipoDisciplina);
    }

    public void setTipoDisciplina(String tipoDisciplina) {
        this.tipoDisciplina = tipoDisciplina;
    }

    @XmlElement(name = "nome")
    public String getNome() {
    	if (nome == null) {
    		nome = "";
    	}
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            return 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean getDisciplinaIdioma() {
    	if (disciplinaIdioma == null) {
    		disciplinaIdioma = Boolean.FALSE;
    	}
        return disciplinaIdioma;
    }

    public void setDisciplinaIdioma(Boolean disciplinaIdioma) {
        this.disciplinaIdioma = disciplinaIdioma;
    }
}
