package negocio.comuns.eventos;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CursosEvento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CursosEventoVO extends SuperVO {

    private Integer codigo;
    private String titulo;
    private Date data;
    private String hora;
    private String ministrante;
    private String miniCurriculoMinistrante;
    private String tipoMinicurso;
    private String descricaoMinicurso;
    private String conteudoProgramatico;
    private Integer nrVagasCurso;
    private Integer nrMaximoVagasExcedentes;
    private String localCurso;
    private String duracao;
    private Double valorAluno;
    private Double valorProfessor;
    private Double valorFuncionario;
    private Double valorComunidade;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Evento </code>.
     */
    private EventoVO evento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CursosEvento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CursosEventoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CursosEventoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CursosEventoVO obj) throws ConsistirException {
        if (obj.getTitulo().equals("")) {
            throw new ConsistirException("O campo TÍTULO (Cursos Evento) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Cursos Evento) deve ser informado.");
        }
        if (obj.getTipoMinicurso().equals("")) {
            throw new ConsistirException("O campo TIPO MINICURSO (Cursos Evento) deve ser informado.");
        }
        if (obj.getNrVagasCurso().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO DE VAGAS CURSO (Cursos Evento) deve ser informado.");
        }
        if (obj.getNrMaximoVagasExcedentes().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO MÁXIMO DE VAGAS EXCEDENTES (Cursos Evento) deve ser informado.");
        }
        if (obj.getValorAluno().intValue() == 0) {
            throw new ConsistirException("O campo VALOR ALUNO (Cursos Evento) deve ser informado.");
        }
        if (obj.getValorProfessor().intValue() == 0) {
            throw new ConsistirException("O campo VALOR PROFESSOR (Cursos Evento) deve ser informado.");
        }
        if (obj.getValorFuncionario().intValue() == 0) {
            throw new ConsistirException("O campo VALOR FUNCIONÁRIO (Cursos Evento) deve ser informado.");
        }
        if (obj.getValorComunidade().intValue() == 0) {
            throw new ConsistirException("O campo VALOR COMUNIDADE (Cursos Evento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTitulo("");
        setData(new Date());
        setHora("");
        setMinistrante("");
        setMiniCurriculoMinistrante("");
        setTipoMinicurso("");
        setDescricaoMinicurso("");
        setConteudoProgramatico("");
        setNrVagasCurso(0);
        setNrMaximoVagasExcedentes(0);
        setLocalCurso("");
        setDuracao("");
        setValorAluno(0.0);
        setValorProfessor(0.0);
        setValorFuncionario(0.0);
        setValorComunidade(0.0);
    }

    /**
     * Retorna o objeto da classe <code>Evento</code> relacionado com (
     * <code>CursosEvento</code>).
     */
    public EventoVO getEvento() {
        if (evento == null) {
            evento = new EventoVO();
        }
        return (evento);
    }

    /**
     * Define o objeto da classe <code>Evento</code> relacionado com (
     * <code>CursosEvento</code>).
     */
    public void setEvento(EventoVO obj) {
        this.evento = obj;
    }

    public Double getValorComunidade() {
        return (valorComunidade);
    }

    public void setValorComunidade(Double valorComunidade) {
        this.valorComunidade = valorComunidade;
    }

    public Double getValorFuncionario() {
        return (valorFuncionario);
    }

    public void setValorFuncionario(Double valorFuncionario) {
        this.valorFuncionario = valorFuncionario;
    }

    public Double getValorProfessor() {
        return (valorProfessor);
    }

    public void setValorProfessor(Double valorProfessor) {
        this.valorProfessor = valorProfessor;
    }

    public Double getValorAluno() {
        return (valorAluno);
    }

    public void setValorAluno(Double valorAluno) {
        this.valorAluno = valorAluno;
    }

    public String getDuracao() {
        return (duracao);
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getLocalCurso() {
        return (localCurso);
    }

    public void setLocalCurso(String localCurso) {
        this.localCurso = localCurso;
    }

    public Integer getNrMaximoVagasExcedentes() {
        return (nrMaximoVagasExcedentes);
    }

    public void setNrMaximoVagasExcedentes(Integer nrMaximoVagasExcedentes) {
        this.nrMaximoVagasExcedentes = nrMaximoVagasExcedentes;
    }

    public Integer getNrVagasCurso() {
        return (nrVagasCurso);
    }

    public void setNrVagasCurso(Integer nrVagasCurso) {
        this.nrVagasCurso = nrVagasCurso;
    }

    public String getConteudoProgramatico() {
        return (conteudoProgramatico);
    }

    public void setConteudoProgramatico(String conteudoProgramatico) {
        this.conteudoProgramatico = conteudoProgramatico;
    }

    public String getDescricaoMinicurso() {
        return (descricaoMinicurso);
    }

    public void setDescricaoMinicurso(String descricaoMinicurso) {
        this.descricaoMinicurso = descricaoMinicurso;
    }

    public String getTipoMinicurso() {
        return (tipoMinicurso);
    }

    public void setTipoMinicurso(String tipoMinicurso) {
        this.tipoMinicurso = tipoMinicurso;
    }

    public String getMiniCurriculoMinistrante() {
        return (miniCurriculoMinistrante);
    }

    public void setMiniCurriculoMinistrante(String miniCurriculoMinistrante) {
        this.miniCurriculoMinistrante = miniCurriculoMinistrante;
    }

    public String getMinistrante() {
        return (ministrante);
    }

    public void setMinistrante(String ministrante) {
        this.ministrante = ministrante;
    }

    public String getHora() {
        return (hora);
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTitulo() {
        return (titulo);
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
