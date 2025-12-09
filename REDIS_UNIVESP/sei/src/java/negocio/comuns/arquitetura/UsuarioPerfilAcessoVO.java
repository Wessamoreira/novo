/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author Administrador
 */
public class UsuarioPerfilAcessoVO extends SuperVO {

    protected Integer codigo;
    private Integer usuario;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    protected PerfilAcessoVO perfilAcesso;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>EnderecoCobranca</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public UsuarioPerfilAcessoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>UsuarioPerfilAcessoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDadosEspecial(UsuarioPerfilAcessoVO obj) throws ConsistirException {
        if ((obj.getPerfilAcesso() == null) || (obj.getPerfilAcesso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PERFIL ACESSO (Perfil Acesso Usuário) deve ser informado.");
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>UsuarioPerfilAcessoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(UsuarioPerfilAcessoVO obj) throws ConsistirException {
        if ((obj.getPerfilAcesso() == null)
                || (obj.getPerfilAcesso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PERFIL ACESSO (Perfil Acesso Usuário) deve ser informado.");
        }
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

    public PerfilAcessoVO getPerfilAcesso() {
        if (perfilAcesso == null) {
            perfilAcesso = new PerfilAcessoVO();
        }
        return perfilAcesso;
    }

    public void setPerfilAcesso(PerfilAcessoVO perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }

    public Integer getUsuario() {
        if (usuario == null) {
            usuario = 0;
        }
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }
}
