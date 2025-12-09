package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.FavoritoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a utilização desta interface é possível
 * substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de
 * sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface FavoritoInterfaceFacade {

    public FavoritoVO novo() throws Exception;

    public void excluir(FavoritoVO obj) throws Exception;

    public void incluir(final FavoritoVO obj, final boolean verificarPermissao, UsuarioVO usuario) throws Exception;

    public FavoritoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public FavoritoVO consultarPorPagina(String valorConsulta, Integer codUsuario, TipoVisaoEnum tipoVisaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<FavoritoVO> consultarPorUsuario(Integer codUsuario, TipoVisaoEnum tipoVisaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void setIdEntidade(String aIdEntidade);
}
