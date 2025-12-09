package negocio.interfaces.basico;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.UsuarioLinksUteisVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface UsuarioLinksUteisInterfaceFacade {
	
    
    public void incluir(UsuarioLinksUteisVO obj,  UsuarioVO usuarioVO) throws Exception;
    
    public void alterar(UsuarioLinksUteisVO obj,  UsuarioVO usuarioVO) throws Exception;
   
    public void excluir(UsuarioLinksUteisVO obj,  UsuarioVO usuarioVO) throws Exception;
    
    void persistir(List<UsuarioLinksUteisVO> usuarioLinksUteisVOs, LinksUteisVO linksUteisVO, UsuarioVO usuarioVO) throws Exception;

	List<UsuarioLinksUteisVO> consultarPorUsuarioLinksUteis(Integer linksUteis, UsuarioVO usuarioVO) throws Exception;
	
}