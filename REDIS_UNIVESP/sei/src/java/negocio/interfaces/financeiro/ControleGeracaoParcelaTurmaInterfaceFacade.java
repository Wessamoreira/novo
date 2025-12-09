package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ControleGeracaoParcelaTurmaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ControleGeracaoParcelaTurmaInterfaceFacade {
	

    public ControleGeracaoParcelaTurmaVO novo() throws Exception;
    public void incluir(ControleGeracaoParcelaTurmaVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(ControleGeracaoParcelaTurmaVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ControleGeracaoParcelaTurmaVO obj, UsuarioVO usuario) throws Exception;
    public ControleGeracaoParcelaTurmaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}