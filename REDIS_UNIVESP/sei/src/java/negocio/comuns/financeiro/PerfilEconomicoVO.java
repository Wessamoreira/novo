package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade PerfilEconomico. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
@XmlRootElement(name = "perfilEconomico")
public class PerfilEconomicoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code>.
     */
    private List perfilEconomicoCondicaoNegociacaoVOs;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PerfilEconomico</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PerfilEconomicoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PerfilEconomicoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PerfilEconomicoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Perfil Economico) deve ser informado.");
        }
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>PerfilEconomicoCondicaoNegociacaoVO</code> ao List
     * <code>perfilEconomicoCondicaoNegociacaoVOs</code>. Utiliza o atributo
     * padrão de consulta da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code> -
     * getCondicaoNegociacao().getCodigo() - como identificador (key) do objeto
     * no List.
     *
     * @param obj
     *            Objeto da classe
     *            <code>PerfilEconomicoCondicaoNegociacaoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjPerfilEconomicoCondicaoNegociacaoVOs(PerfilEconomicoCondicaoNegociacaoVO obj) throws Exception {
        PerfilEconomicoCondicaoNegociacaoVO.validarDados(obj);
        int index = 0;
        Iterator i = getPerfilEconomicoCondicaoNegociacaoVOs().iterator();
        while (i.hasNext()) {
            PerfilEconomicoCondicaoNegociacaoVO objExistente = (PerfilEconomicoCondicaoNegociacaoVO) i.next();
            if (objExistente.getCondicaoNegociacao().getCodigo().equals(obj.getCondicaoNegociacao().getCodigo())) {
                getPerfilEconomicoCondicaoNegociacaoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getPerfilEconomicoCondicaoNegociacaoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>PerfilEconomicoCondicaoNegociacaoVO</code> no List
     * <code>perfilEconomicoCondicaoNegociacaoVOs</code>. Utiliza o atributo
     * padrão de consulta da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code> -
     * getCondicaoNegociacao().getCodigo() - como identificador (key) do objeto
     * no List.
     *
     * @param condicaoNegociacao
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjPerfilEconomicoCondicaoNegociacaoVOs(Integer condicaoNegociacao) throws Exception {
        int index = 0;
        Iterator i = getPerfilEconomicoCondicaoNegociacaoVOs().iterator();
        while (i.hasNext()) {
            PerfilEconomicoCondicaoNegociacaoVO objExistente = (PerfilEconomicoCondicaoNegociacaoVO) i.next();
            if (objExistente.getCondicaoNegociacao().getCodigo().equals(condicaoNegociacao)) {
                getPerfilEconomicoCondicaoNegociacaoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>PerfilEconomicoCondicaoNegociacaoVO</code> no List
     * <code>perfilEconomicoCondicaoNegociacaoVOs</code>. Utiliza o atributo
     * padrão de consulta da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code> -
     * getCondicaoNegociacao().getCodigo() - como identificador (key) do objeto
     * no List.
     *
     * @param condicaoNegociacao
     *            Parâmetro para localizar o objeto do List.
     */
    public PerfilEconomicoCondicaoNegociacaoVO consultarObjPerfilEconomicoCondicaoNegociacaoVO(Integer condicaoNegociacao) throws Exception {
        Iterator i = getPerfilEconomicoCondicaoNegociacaoVOs().iterator();
        while (i.hasNext()) {
            PerfilEconomicoCondicaoNegociacaoVO objExistente = (PerfilEconomicoCondicaoNegociacaoVO) i.next();
            if (objExistente.getCondicaoNegociacao().getCodigo().equals(condicaoNegociacao)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code>.
     */
    @XmlElement(name = "perfilEconomicoCondicaoNegociacaoVOs")
    public List getPerfilEconomicoCondicaoNegociacaoVOs() {
        if (perfilEconomicoCondicaoNegociacaoVOs == null) {
            perfilEconomicoCondicaoNegociacaoVOs = new ArrayList(0);
        }
        return (perfilEconomicoCondicaoNegociacaoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code>.
     */
    public void setPerfilEconomicoCondicaoNegociacaoVOs(List perfilEconomicoCondicaoNegociacaoVOs) {
        this.perfilEconomicoCondicaoNegociacaoVOs = perfilEconomicoCondicaoNegociacaoVOs;
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
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
