package negocio.interfaces.eventos;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.eventos.MembroComunidadeVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface MembroComunidadeInterfaceFacade {
	

    public MembroComunidadeVO novo() throws Exception;
    public void incluir(MembroComunidadeVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void alterar(MembroComunidadeVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void excluir(MembroComunidadeVO obj) throws Exception;
    public MembroComunidadeVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public MembroComunidadeVO consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}