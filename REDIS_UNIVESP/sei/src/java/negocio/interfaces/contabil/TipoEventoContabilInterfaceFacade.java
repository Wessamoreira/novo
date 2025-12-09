package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.TipoEventoContabilVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface TipoEventoContabilInterfaceFacade {

	public TipoEventoContabilVO novo() throws Exception;

	public void incluir(TipoEventoContabilVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(TipoEventoContabilVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(TipoEventoContabilVO obj, UsuarioVO usuarioVO) throws Exception;

	public TipoEventoContabilVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorPlanoConta(String valorConsulta, String tipoConta, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorTipoPlanoContaCredito(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorTipoPlanoContaDebito(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDescricaoHistorico(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarPorNome(String valorConsulta, String ordenacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

}