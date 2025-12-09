package negocio.comuns.eventos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.eventos.InscricaoEvento;

/**
 * Reponsável por manter os dados da entidade InscricaoCursoEvento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see InscricaoEvento
 */
public class InscricaoCursoEventoVO extends SuperVO {

    private Integer codigo;
    private Integer inscricaoEvento;
    private Double valorInscricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>CursosEvento </code>.
     */
    private CursosEventoVO cursosEvento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>InscricaoCursoEvento</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public InscricaoCursoEventoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>InscricaoCursoEventoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(InscricaoCursoEventoVO obj) throws ConsistirException {
        if ((obj.getCursosEvento() == null) || (obj.getCursosEvento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSOS EVENTO (Inscrição Curso Evento) deve ser informado.");
        }
        if (obj.getValorInscricao().intValue() == 0) {
            throw new ConsistirException("O campo VALOR INSCRIÇÃO (Inscrição Curso Evento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setValorInscricao(0.0);
    }

    /**
     * Retorna o objeto da classe <code>CursosEvento</code> relacionado com (
     * <code>InscricaoCursoEvento</code>).
     */
    public CursosEventoVO getCursosEvento() {
        if (cursosEvento == null) {
            cursosEvento = new CursosEventoVO();
        }
        return (cursosEvento);
    }

    /**
     * Define o objeto da classe <code>CursosEvento</code> relacionado com (
     * <code>InscricaoCursoEvento</code>).
     */
    public void setCursosEvento(CursosEventoVO obj) {
        this.cursosEvento = obj;
    }

    public Double getValorInscricao() {
        return (valorInscricao);
    }

    public void setValorInscricao(Double valorInscricao) {
        this.valorInscricao = valorInscricao;
    }

    public Integer getInscricaoEvento() {
        return (inscricaoEvento);
    }

    public void setInscricaoEvento(Integer inscricaoEvento) {
        this.inscricaoEvento = inscricaoEvento;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
