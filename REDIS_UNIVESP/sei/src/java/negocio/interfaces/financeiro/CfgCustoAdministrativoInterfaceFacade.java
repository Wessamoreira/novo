package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CfgCustoAdministrativoVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface CfgCustoAdministrativoInterfaceFacade {

	public CfgCustoAdministrativoVO novo() throws Exception;

	public void incluir(CfgCustoAdministrativoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(CfgCustoAdministrativoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(CfgCustoAdministrativoVO obj, UsuarioVO usuario) throws Exception;

	public CfgCustoAdministrativoVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public CfgCustoAdministrativoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoFuncionario(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorTipoCusto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);
	
    public CfgCustoAdministrativoVO consultaRapidaConfiguracaoASerUsada(int configuracao , NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void carregarDados(CfgCustoAdministrativoVO obj, NivelMontarDados basico, UsuarioVO usuario) throws Exception;    
	
}