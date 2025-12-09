package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.MetaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface MetaInterfaceFacade {
	

    public void persistir(MetaVO obj, List objetos, UsuarioVO usuarioVO) throws Exception;
    public void excluir(MetaVO obj, UsuarioVO usuarioVO) throws Exception;
    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void validarDados(MetaVO obj) throws Exception;
    public void validarDadosExclusao(MetaVO obj) throws Exception;
    public MetaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}