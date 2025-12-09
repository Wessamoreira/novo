package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;

/**
 * Reponsável por manter os dados da entidade RegistroEntradaAcervo. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class RegistroEntradaAcervoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String tipoEntrada;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ItemRegistroEntradaAcervo</code>.
     */
    private List<ItemRegistroEntradaAcervoVO> itemRegistroEntradaAcervoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO funcionario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Biblioteca </code>.
     */
    private BibliotecaVO biblioteca;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroEntradaAcervo</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public RegistroEntradaAcervoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroEntradaAcervoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(RegistroEntradaAcervoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Registro Entrada Acervo) deve ser informado.");
        }
//		if (obj.getTipoEntrada().equals("")) {
//			throw new ConsistirException("O campo TIPO ENTRADA (Registro Entrada Acervo) deve ser informado.");
//		}
        if ((obj.getFuncionario() == null) || (obj.getFuncionario().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FUNCIONÁRIO (Registro Entrada Acervo) deve ser informado.");
        }
        if ((obj.getBiblioteca() == null) || (obj.getBiblioteca().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BIBLIOTECA (Registro Entrada Acervo) deve ser informado.");
        }
        if ((obj.getItemRegistroEntradaAcervoVOs().size() == 0)) {
            throw new ConsistirException("É obrigatório informar um ou mais exemplares.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setTipoEntrada(getTipoEntrada().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setTipoEntrada("");
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ItemRegistroEntradaAcervoVO</code> ao List
     * <code>itemRegistroEntradaAcervoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemRegistroEntradaAcervo</code> -
     * getExemplar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ItemRegistroEntradaAcervoVO</code> que
     *            será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjItemRegistroEntradaAcervoVOs(ItemRegistroEntradaAcervoVO obj) throws Exception {
        ItemRegistroEntradaAcervoVO.validarDados(obj);
        int index = 0;
        Iterator i = getItemRegistroEntradaAcervoVOs().iterator();
        while (i.hasNext()) {
            ItemRegistroEntradaAcervoVO objExistente = (ItemRegistroEntradaAcervoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(obj.getExemplar().getCodigo())) {
                getItemRegistroEntradaAcervoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getItemRegistroEntradaAcervoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ItemRegistroEntradaAcervoVO</code> no List
     * <code>itemRegistroEntradaAcervoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemRegistroEntradaAcervo</code> -
     * getExemplar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param exemplar
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItemRegistroEntradaAcervoVOs(Integer exemplar) throws Exception {
        int index = 0;
        Iterator i = getItemRegistroEntradaAcervoVOs().iterator();
        while (i.hasNext()) {
            ItemRegistroEntradaAcervoVO objExistente = (ItemRegistroEntradaAcervoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(exemplar)) {
                getItemRegistroEntradaAcervoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ItemRegistroEntradaAcervoVO</code> no List
     * <code>itemRegistroEntradaAcervoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemRegistroEntradaAcervo</code> -
     * getExemplar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param exemplar
     *            Parâmetro para localizar o objeto do List.
     */
    public ItemRegistroEntradaAcervoVO consultarObjItemRegistroEntradaAcervoVO(Integer exemplar) throws Exception {
        Iterator i = getItemRegistroEntradaAcervoVOs().iterator();
        while (i.hasNext()) {
            ItemRegistroEntradaAcervoVO objExistente = (ItemRegistroEntradaAcervoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(exemplar)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Biblioteca</code> relacionado com (
     * <code>RegistroEntradaAcervo</code>).
     */
    public BibliotecaVO getBiblioteca() {
        if (biblioteca == null) {
            biblioteca = new BibliotecaVO();
        }
        return (biblioteca);
    }

    /**
     * Define o objeto da classe <code>Biblioteca</code> relacionado com (
     * <code>RegistroEntradaAcervo</code>).
     */
    public void setBiblioteca(BibliotecaVO obj) {
        this.biblioteca = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>RegistroEntradaAcervo</code>).
     */
    public UsuarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new UsuarioVO();
        }
        return (funcionario);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>RegistroEntradaAcervo</code>).
     */
    public void setFuncionario(UsuarioVO obj) {
        this.funcionario = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ItemRegistroEntradaAcervo</code>.
     */
    public List<ItemRegistroEntradaAcervoVO> getItemRegistroEntradaAcervoVOs() {
        if (itemRegistroEntradaAcervoVOs == null) {
            itemRegistroEntradaAcervoVOs = new ArrayList<ItemRegistroEntradaAcervoVO>(0);
        }
        return (itemRegistroEntradaAcervoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ItemRegistroEntradaAcervo</code>.
     */
    public void setItemRegistroEntradaAcervoVOs(List<ItemRegistroEntradaAcervoVO> itemRegistroEntradaAcervoVOs) {
        this.itemRegistroEntradaAcervoVOs = itemRegistroEntradaAcervoVOs;
    }

    public String getTipoEntrada() {
        if (tipoEntrada == null) {
            tipoEntrada = "";
        }
        return (tipoEntrada);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoEntrada_Apresentar() {
        return TipoEntradaAcervo.getDescricao(tipoEntrada);
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
