package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Visao. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class VisaoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String valorCssTopoLogo;
    private String valorCssBackground;
    private String valorCssMenu;
    public static final long serialVersionUID = 1L;
    //private String csspadrao;

    // private String imagemBackground;
    // private String imagemTemp;
    // private String nomeImagemBackground;
    /**
     * Construtor padrão da classe <code>Visao</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public VisaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>VisaoVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(VisaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Visão) deve ser informado.");
        }
        if (obj.getValorCssTopoLogo().equalsIgnoreCase("#FFFFFF")) {
            throw new ConsistirException("A Cor não pode ser selecionada para a opção (TOPO LOGOMARCA), por favor, escolha outra cor.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        // setImagemBackground( "" );
        // setNomeImagemBackground( "" );
        // setImagemTemp("");
    }

    // public String getNomeImagemBackground() {
    // return (nomeImagemBackground);
    // }
    //
    // public void setNomeImagemBackground( String nomeImagemBackground ) {
    // this.nomeImagemBackground = nomeImagemBackground;
    // }
    //
    // public String getImagemBackground() {
    // return (imagemBackground);
    // }
    //
    // public void setImagemBackground( String imagemBackground ) {
    // this.imagemBackground = imagemBackground;
    // }
    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            return 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    // public String getImagemTemp() {
    // return imagemTemp;
    // }
    //
    // public void setImagemTemp(String imagemTemp) {
    // this.imagemTemp = imagemTemp;
    // }

    /**
     * @return the valorCssTopoLogo
     */
    public String getValorCssTopoLogo() {
        if (valorCssTopoLogo == null) {
            valorCssTopoLogo = "";
        }
        return valorCssTopoLogo;
    }

    /**
     * @param valorCssTopoLogo the valorCssTopoLogo to set
     */
    public void setValorCssTopoLogo(String valorCssTopoLogo) {
        this.valorCssTopoLogo = valorCssTopoLogo;
    }

    /**
     * @return the valorCssBackground
     */
    public String getValorCssBackground() {
        if (valorCssBackground == null) {
            valorCssBackground = "";
        }
        return valorCssBackground;
    }

    /**
     * @param valorCssBackground the valorCssBackground to set
     */
    public void setValorCssBackground(String valorCssBackground) {
        this.valorCssBackground = valorCssBackground;
    }

    /**
     * @return the valorCssMenu
     */
    public String getValorCssMenu() {
        if (valorCssMenu == null) {
            valorCssMenu = "";
        }
        return valorCssMenu;
    }

    /**
     * @param valorCssMenu the valorCssMenu to set
     */
    public void setValorCssMenu(String valorCssMenu) {
        this.valorCssMenu = valorCssMenu;
    }
}
