package negocio.comuns.eventos;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PalestraEvento. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PalestraEventoVO extends SuperVO {

    private Integer codigo;
    private String tituloPalestra;
    private Date data;
    private String hora;
    private String palestrante;
    private String miniCurriculoPalestrante;
    private String tipoPalestra;
    private String descricaoPalestra;
    private Integer nrVagasPalestra;
    private Integer nrMaximoVagasExcedentes;
    private String localPalestra;
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
     * Construtor padrão da classe <code>PalestraEvento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PalestraEventoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PalestraEventoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PalestraEventoVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Palestra Evento) deve ser informado.");
        }
        if ((obj.getEvento() == null) || (obj.getEvento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo EVENTO (Palestra Evento) deve ser informado.");
        }
        if (obj.getTipoPalestra().equals("")) {
            throw new ConsistirException("O campo TIPO PALESTRA (Palestra Evento) deve ser informado.");
        }
        if (obj.getNrVagasPalestra().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO DE VAGAS PALESTRA (Palestra Evento) deve ser informado.");
        }
        if (obj.getNrMaximoVagasExcedentes().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO MÁXIMO DE VAGAS EXCEDENTES (Palestra Evento) deve ser informado.");
        }
        if (obj.getDuracao().equals("")) {
            throw new ConsistirException("O campo DURAÇÃO (Palestra Evento) deve ser informado.");
        }
        if (obj.getValorAluno().intValue() == 0) {
            throw new ConsistirException("O campo VALOR ALUNO (Palestra Evento) deve ser informado.");
        }
        if (obj.getValorProfessor().intValue() == 0) {
            throw new ConsistirException("O campo VALOR PROFESSOR (Palestra Evento) deve ser informado.");
        }
        if (obj.getValorFuncionario().intValue() == 0) {
            throw new ConsistirException("O campo VALOR FUNCIONÁRIO (Palestra Evento) deve ser informado.");
        }
        if (obj.getValorComunidade().intValue() == 0) {
            throw new ConsistirException("O campo VALOR COMUNIDADE (Palestra Evento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTituloPalestra("");
        setData(new Date());
        setHora("");
        setPalestrante("");
        setMiniCurriculoPalestrante("");
        setTipoPalestra("");
        setDescricaoPalestra("");
        setNrVagasPalestra(0);
        setNrMaximoVagasExcedentes(0);
        setLocalPalestra("");
        setDuracao("");
        setValorAluno(0.0);
        setValorProfessor(0.0);
        setValorFuncionario(0.0);
        setValorComunidade(0.0);
    }

    /**
     * Retorna o objeto da classe <code>Evento</code> relacionado com (
     * <code>PalestraEvento</code>).
     */
    public EventoVO getEvento() {
        if (evento == null) {
            evento = new EventoVO();
        }
        return (evento);
    }

    /**
     * Define o objeto da classe <code>Evento</code> relacionado com (
     * <code>PalestraEvento</code>).
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

    public String getLocalPalestra() {
        return (localPalestra);
    }

    public void setLocalPalestra(String localPalestra) {
        this.localPalestra = localPalestra;
    }

    public Integer getNrMaximoVagasExcedentes() {
        return (nrMaximoVagasExcedentes);
    }

    public void setNrMaximoVagasExcedentes(Integer nrMaximoVagasExcedentes) {
        this.nrMaximoVagasExcedentes = nrMaximoVagasExcedentes;
    }

    public Integer getNrVagasPalestra() {
        return (nrVagasPalestra);
    }

    public void setNrVagasPalestra(Integer nrVagasPalestra) {
        this.nrVagasPalestra = nrVagasPalestra;
    }

    public String getDescricaoPalestra() {
        return (descricaoPalestra);
    }

    public void setDescricaoPalestra(String descricaoPalestra) {
        this.descricaoPalestra = descricaoPalestra;
    }

    public String getTipoPalestra() {
        return (tipoPalestra);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoPalestra_Apresentar() {
        if (tipoPalestra.equals("DE")) {
            return "Debate";
        }
        if (tipoPalestra.equals("EC")) {
            return "Exposição de Case";
        }
        if (tipoPalestra.equals("MR")) {
            return "Mesa Redonda";
        }
        if (tipoPalestra.equals("PA")) {
            return "Palestra";
        }
        if (tipoPalestra.equals("VI")) {
            return "Virtual";
        }
        return (tipoPalestra);
    }

    public void setTipoPalestra(String tipoPalestra) {
        this.tipoPalestra = tipoPalestra;
    }

    public String getMiniCurriculoPalestrante() {
        return (miniCurriculoPalestrante);
    }

    public void setMiniCurriculoPalestrante(String miniCurriculoPalestrante) {
        this.miniCurriculoPalestrante = miniCurriculoPalestrante;
    }

    public String getPalestrante() {
        return (palestrante);
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
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

    public String getTituloPalestra() {
        return (tituloPalestra);
    }

    public void setTituloPalestra(String tituloPalestra) {
        this.tituloPalestra = tituloPalestra;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
