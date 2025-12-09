package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade RegistroSaidaAcervo. Classe do tipo VO - Value Object composta pelos
 * atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class RegistroSaidaAcervoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String justificativa;
    /**
     * Atributo responsável por manter os objetos da classe <code>ItemRegistroSaidaAcervo</code>.
     */
    private List<ItemRegistroSaidaAcervoVO> itemRegistroSaidaAcervoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Usuario </code>.
     */
    private UsuarioVO funcionario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Biblioteca </code>.
     */
    private BibliotecaVO biblioteca;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroSaidaAcervo</code>. Cria uma nova instância desta entidade,
     * inicializando automaticamente seus atributos (Classe VO).
     */
    public RegistroSaidaAcervoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>RegistroSaidaAcervoVO</code>. Todos os
     * tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o
     *                erro ocorrido.
     */
    public static void validarDados(RegistroSaidaAcervoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getFuncionario() == null) || (obj.getFuncionario().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FUNCIONÁRIO (Registro Saída Acervo) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Registro Saída Acervo) deve ser informado.");
        }
        if (obj.getJustificativa().equals("")) {
            throw new ConsistirException("O campo JUSTIFICATIVA (Registro Saída Acervo) deve ser informado.");
        }
        if ((obj.getBiblioteca() == null) || (obj.getBiblioteca().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BIBLIOTECA (Registro Saída Acervo) deve ser informado.");
        }        
        if ((obj.getItemRegistroSaidaAcervoVOs().size() == 0)) {
            throw new ConsistirException("É obrigatório informar um ou mais exemplares.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setJustificativa(getJustificativa().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setJustificativa("");
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>ItemRegistroSaidaAcervoVO</code> ao List
     * <code>itemRegistroSaidaAcervoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>ItemRegistroSaidaAcervo</code> - getExemplar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ItemRegistroSaidaAcervoVO</code> que será adiocionado ao Hashtable
     *            correspondente.
     */
    public void adicionarObjItemRegistroSaidaAcervoVOs(ItemRegistroSaidaAcervoVO obj) throws Exception {
        ItemRegistroSaidaAcervoVO.validarDados(obj);
        int index = 0;
        Iterator i = getItemRegistroSaidaAcervoVOs().iterator();
        while (i.hasNext()) {
            ItemRegistroSaidaAcervoVO objExistente = (ItemRegistroSaidaAcervoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(obj.getExemplar().getCodigo())) {
                getItemRegistroSaidaAcervoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getItemRegistroSaidaAcervoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>ItemRegistroSaidaAcervoVO</code> no List
     * <code>itemRegistroSaidaAcervoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>ItemRegistroSaidaAcervo</code> - getExemplar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param exemplar
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItemRegistroSaidaAcervoVOs(Integer exemplar) throws Exception {
        int index = 0;
        Iterator i = getItemRegistroSaidaAcervoVOs().iterator();
        while (i.hasNext()) {
            ItemRegistroSaidaAcervoVO objExistente = (ItemRegistroSaidaAcervoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(exemplar)) {
                getItemRegistroSaidaAcervoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>ItemRegistroSaidaAcervoVO</code> no List
     * <code>itemRegistroSaidaAcervoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>ItemRegistroSaidaAcervo</code> - getExemplar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param exemplar
     *            Parâmetro para localizar o objeto do List.
     */
    public ItemRegistroSaidaAcervoVO consultarObjItemRegistroSaidaAcervoVO(Integer exemplar) throws Exception {
        Iterator i = getItemRegistroSaidaAcervoVOs().iterator();
        while (i.hasNext()) {
            ItemRegistroSaidaAcervoVO objExistente = (ItemRegistroSaidaAcervoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(exemplar)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Biblioteca</code> relacionado com ( <code>RegistroSaidaAcervo</code>).
     */
    public BibliotecaVO getBiblioteca() {
        if (biblioteca == null) {
            biblioteca = new BibliotecaVO();
        }
        return (biblioteca);
    }

    /**
     * Define o objeto da classe <code>Biblioteca</code> relacionado com ( <code>RegistroSaidaAcervo</code>).
     */
    public void setBiblioteca(BibliotecaVO obj) {
        this.biblioteca = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com ( <code>RegistroSaidaAcervo</code>).
     */
    public UsuarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new UsuarioVO();
        }
        return (funcionario);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com ( <code>RegistroSaidaAcervo</code>).
     */
    public void setFuncionario(UsuarioVO obj) {
        this.funcionario = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe <code>ItemRegistroSaidaAcervo</code>.
     */
    public List<ItemRegistroSaidaAcervoVO> getItemRegistroSaidaAcervoVOs() {
        if (itemRegistroSaidaAcervoVOs == null) {
            itemRegistroSaidaAcervoVOs = new ArrayList<ItemRegistroSaidaAcervoVO>(0);
        }
        return (itemRegistroSaidaAcervoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe <code>ItemRegistroSaidaAcervo</code>.
     */
    public void setItemRegistroSaidaAcervoVOs(List<ItemRegistroSaidaAcervoVO> itemRegistroSaidaAcervoVOs) {
        this.itemRegistroSaidaAcervoVOs = itemRegistroSaidaAcervoVOs;
    }

    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return (justificativa);
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
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
