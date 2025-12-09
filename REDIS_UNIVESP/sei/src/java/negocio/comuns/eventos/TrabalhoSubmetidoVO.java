package negocio.comuns.eventos;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade TrabalhoSubmetido. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TrabalhoSubmetidoVO extends SuperVO {

    private Integer codigo;
    private Integer evento;
    private Date dataSubmissao;
    private String titulo;
    private String introducao;
    private String metodologia;
    private String resultado;
    private String conclusao;
    private String palavrasChave;
    private String parecerComissao;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>AutorTrabalhoSubmetido</code>.
     */
    private List autorTrabalhoSubmetidoVOs;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TrabalhoSubmetido</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TrabalhoSubmetidoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TrabalhoSubmetidoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TrabalhoSubmetidoVO obj) throws ConsistirException {
        if (obj.getEvento().intValue() == 0) {
            throw new ConsistirException("O campo EVENTO (TrabalhoSubmetido) deve ser informado.");
        }
        if (obj.getDataSubmissao() == null) {
            throw new ConsistirException("O campo DATA SUBMISSÃO (TrabalhoSubmetido) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setEvento(0);
        setDataSubmissao(new Date());
        setTitulo("");
        setIntroducao("");
        setMetodologia("");
        setResultado("");
        setConclusao("");
        setPalavrasChave("");
        setParecerComissao("");
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>AutorTrabalhoSubmetidoVO</code> ao List
     * <code>autorTrabalhoSubmetidoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>AutorTrabalhoSubmetido</code> -
     * getPessoaAutorTrabalhoSubmetido().getCodigo() - como identificador (key)
     * do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>AutorTrabalhoSubmetidoVO</code> que
     *            será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjAutorTrabalhoSubmetidoVOs(AutorTrabalhoSubmetidoVO obj) throws Exception {
        AutorTrabalhoSubmetidoVO.validarDados(obj);
        int index = 0;
        Iterator i = getAutorTrabalhoSubmetidoVOs().iterator();
        while (i.hasNext()) {
            AutorTrabalhoSubmetidoVO objExistente = (AutorTrabalhoSubmetidoVO) i.next();
            if (objExistente.getPessoaAutorTrabalhoSubmetido().getCodigo().equals(obj.getPessoaAutorTrabalhoSubmetido().getCodigo())) {
                getAutorTrabalhoSubmetidoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getAutorTrabalhoSubmetidoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>AutorTrabalhoSubmetidoVO</code> no List
     * <code>autorTrabalhoSubmetidoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>AutorTrabalhoSubmetido</code> -
     * getPessoaAutorTrabalhoSubmetido().getCodigo() - como identificador (key)
     * do objeto no List.
     *
     * @param pessoaAutorTrabalhoSubmetido
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjAutorTrabalhoSubmetidoVOs(Integer pessoaAutorTrabalhoSubmetido) throws Exception {
        int index = 0;
        Iterator i = getAutorTrabalhoSubmetidoVOs().iterator();
        while (i.hasNext()) {
            AutorTrabalhoSubmetidoVO objExistente = (AutorTrabalhoSubmetidoVO) i.next();
            if (objExistente.getPessoaAutorTrabalhoSubmetido().getCodigo().equals(pessoaAutorTrabalhoSubmetido)) {
                getAutorTrabalhoSubmetidoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>AutorTrabalhoSubmetidoVO</code> no List
     * <code>autorTrabalhoSubmetidoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>AutorTrabalhoSubmetido</code> -
     * getPessoaAutorTrabalhoSubmetido().getCodigo() - como identificador (key)
     * do objeto no List.
     *
     * @param pessoaAutorTrabalhoSubmetido
     *            Parâmetro para localizar o objeto do List.
     */
    public AutorTrabalhoSubmetidoVO consultarObjAutorTrabalhoSubmetidoVO(Integer pessoaAutorTrabalhoSubmetido) throws Exception {
        Iterator i = getAutorTrabalhoSubmetidoVOs().iterator();
        while (i.hasNext()) {
            AutorTrabalhoSubmetidoVO objExistente = (AutorTrabalhoSubmetidoVO) i.next();
            if (objExistente.getPessoaAutorTrabalhoSubmetido().getCodigo().equals(pessoaAutorTrabalhoSubmetido)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>AutorTrabalhoSubmetido</code>.
     */
    public List getAutorTrabalhoSubmetidoVOs() {
        if (autorTrabalhoSubmetidoVOs == null) {
            autorTrabalhoSubmetidoVOs = new ArrayList();
        }
        return (autorTrabalhoSubmetidoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>AutorTrabalhoSubmetido</code>.
     */
    public void setAutorTrabalhoSubmetidoVOs(List autorTrabalhoSubmetidoVOs) {
        this.autorTrabalhoSubmetidoVOs = autorTrabalhoSubmetidoVOs;
    }

    public String getParecerComissao() {
        return (parecerComissao);
    }

    public void setParecerComissao(String parecerComissao) {
        this.parecerComissao = parecerComissao;
    }

    public String getPalavrasChave() {
        return (palavrasChave);
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    public String getConclusao() {
        return (conclusao);
    }

    public void setConclusao(String conclusao) {
        this.conclusao = conclusao;
    }

    public String getResultado() {
        return (resultado);
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getMetodologia() {
        return (metodologia);
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getIntroducao() {
        return (introducao);
    }

    public void setIntroducao(String introducao) {
        this.introducao = introducao;
    }

    public String getTitulo() {
        return (titulo);
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataSubmissao() {
        return (dataSubmissao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataSubmissao_Apresentar() {
        return (Uteis.getData(dataSubmissao));
    }

    public void setDataSubmissao(Date dataSubmissao) {
        this.dataSubmissao = dataSubmissao;
    }

    public Integer getEvento() {
        return (evento);
    }

    public void setEvento(Integer evento) {
        this.evento = evento;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
