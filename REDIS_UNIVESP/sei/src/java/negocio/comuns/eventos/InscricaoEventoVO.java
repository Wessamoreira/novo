package negocio.comuns.eventos;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade InscricaoEvento. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class InscricaoEventoVO extends SuperVO {

    private Integer nrInscricao;
    private Date data;
    private String hora;
    private Double valorTotal;
    private String tipoInscricao;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>InscricaoCursoEvento</code>.
     */
    private List inscricaoCursoEventoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Evento </code>.
     */
    private EventoVO evento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoaInscricao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>InscricaoEvento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public InscricaoEventoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>InscricaoEventoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(InscricaoEventoVO obj) throws ConsistirException {
        if ((obj.getEvento() == null) || (obj.getEvento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo EVENTO (Inscrição Evento) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Inscrição Evento) deve ser informado.");
        }
        if (obj.getValorTotal().intValue() == 0) {
            throw new ConsistirException("O campo VALOR TOTAL (Inscrição Evento) deve ser informado.");
        }
        if (obj.getTipoInscricao().equals("")) {
            throw new ConsistirException("O campo TIPO INSCRIÇÃO (Inscrição Evento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setNrInscricao(0);
        setData(new Date());
        setHora("");
        setValorTotal(0.0);
        setTipoInscricao("");
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>InscricaoCursoEventoVO</code> ao List
     * <code>inscricaoCursoEventoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>InscricaoCursoEvento</code> -
     * getCursosEvento().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param obj
     *            Objeto da classe <code>InscricaoCursoEventoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjInscricaoCursoEventoVOs(InscricaoCursoEventoVO obj) throws Exception {
        InscricaoCursoEventoVO.validarDados(obj);
        int index = 0;
        Iterator i = getInscricaoCursoEventoVOs().iterator();
        while (i.hasNext()) {
            InscricaoCursoEventoVO objExistente = (InscricaoCursoEventoVO) i.next();
            if (objExistente.getCursosEvento().getCodigo().equals(obj.getCursosEvento().getCodigo())) {
                getInscricaoCursoEventoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getInscricaoCursoEventoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>InscricaoCursoEventoVO</code> no List
     * <code>inscricaoCursoEventoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>InscricaoCursoEvento</code> -
     * getCursosEvento().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param cursosEvento
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjInscricaoCursoEventoVOs(Integer cursosEvento) throws Exception {
        int index = 0;
        Iterator i = getInscricaoCursoEventoVOs().iterator();
        while (i.hasNext()) {
            InscricaoCursoEventoVO objExistente = (InscricaoCursoEventoVO) i.next();
            if (objExistente.getCursosEvento().getCodigo().equals(cursosEvento)) {
                getInscricaoCursoEventoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>InscricaoCursoEventoVO</code> no List
     * <code>inscricaoCursoEventoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>InscricaoCursoEvento</code> -
     * getCursosEvento().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param cursosEvento
     *            Parâmetro para localizar o objeto do List.
     */
    public InscricaoCursoEventoVO consultarObjInscricaoCursoEventoVO(Integer cursosEvento) throws Exception {
        Iterator i = getInscricaoCursoEventoVOs().iterator();
        while (i.hasNext()) {
            InscricaoCursoEventoVO objExistente = (InscricaoCursoEventoVO) i.next();
            if (objExistente.getCursosEvento().getCodigo().equals(cursosEvento)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>InscricaoEvento</code>).
     */
    public PessoaVO getPessoaInscricao() {
        if (pessoaInscricao == null) {
            pessoaInscricao = new PessoaVO();
        }
        return (pessoaInscricao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>InscricaoEvento</code>).
     */
    public void setPessoaInscricao(PessoaVO obj) {
        this.pessoaInscricao = obj;
    }

    /**
     * Retorna o objeto da classe <code>Evento</code> relacionado com (
     * <code>InscricaoEvento</code>).
     */
    public EventoVO getEvento() {
        if (evento == null) {
            evento = new EventoVO();
        }
        return (evento);
    }

    /**
     * Define o objeto da classe <code>Evento</code> relacionado com (
     * <code>InscricaoEvento</code>).
     */
    public void setEvento(EventoVO obj) {
        this.evento = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>InscricaoCursoEvento</code>.
     */
    public List getInscricaoCursoEventoVOs() {
        if (inscricaoCursoEventoVOs == null) {
            inscricaoCursoEventoVOs = new ArrayList();
        }
        return (inscricaoCursoEventoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>InscricaoCursoEvento</code>.
     */
    public void setInscricaoCursoEventoVOs(List inscricaoCursoEventoVOs) {
        this.inscricaoCursoEventoVOs = inscricaoCursoEventoVOs;
    }

    public String getTipoInscricao() {
        return (tipoInscricao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoInscricao_Apresentar() {
        if (tipoInscricao.equals("PR")) {
            return "Professor";
        }
        if (tipoInscricao.equals("AL")) {
            return "Aluno";
        }
        if (tipoInscricao.equals("FU")) {
            return "Funcionário";
        }
        if (tipoInscricao.equals("CO")) {
            return "Comunidade";
        }
        return (tipoInscricao);
    }

    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }

    public Double getValorTotal() {
        return (valorTotal);
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
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

    public Integer getNrInscricao() {
        return (nrInscricao);
    }

    public void setNrInscricao(Integer nrInscricao) {
        this.nrInscricao = nrInscricao;
    }
}
