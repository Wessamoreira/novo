package negocio.comuns.pesquisa;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.pesquisa.ProjetoPesquisa;

/**
 * Reponsável por manter os dados da entidade PesquisadorProjetoPesquisa. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ProjetoPesquisa
 */
public class PesquisadorProjetoPesquisaVO extends SuperVO {

    private Integer codigo;
    private Integer projetoPesquisa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PesquisadorLinhaPesquisa </code>.
     */
    private PesquisadorLinhaPesquisaVO pesquisadorLinhaPesquisa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PesquisadorProjetoPesquisa</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PesquisadorProjetoPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PesquisadorProjetoPesquisaVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PesquisadorProjetoPesquisaVO obj) throws ConsistirException {
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    /**
     * Retorna o objeto da classe <code>PesquisadorLinhaPesquisa</code>
     * relacionado com (<code>PesquisadorProjetoPesquisa</code>).
     */
    public PesquisadorLinhaPesquisaVO getPesquisadorLinhaPesquisa() {
        if (pesquisadorLinhaPesquisa == null) {
            pesquisadorLinhaPesquisa = new PesquisadorLinhaPesquisaVO();
        }
        return (pesquisadorLinhaPesquisa);
    }

    /**
     * Define o objeto da classe <code>PesquisadorLinhaPesquisa</code>
     * relacionado com (<code>PesquisadorProjetoPesquisa</code>).
     */
    public void setPesquisadorLinhaPesquisa(PesquisadorLinhaPesquisaVO obj) {
        this.pesquisadorLinhaPesquisa = obj;
    }

    public Integer getProjetoPesquisa() {
        return (projetoPesquisa);
    }

    public void setProjetoPesquisa(Integer projetoPesquisa) {
        this.projetoPesquisa = projetoPesquisa;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
