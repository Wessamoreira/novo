package negocio.interfaces.pesquisa;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface AreaConhecimentoInterfaceFacade {

	public List<AreaConhecimentoVO> consultarSecaoNivelComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public AreaConhecimentoVO novo() throws Exception;

	public void incluir(AreaConhecimentoVO obj, final Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void alterar(AreaConhecimentoVO obj, final Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(AreaConhecimentoVO obj, final Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public AreaConhecimentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

	public List<AreaConhecimentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<AreaConhecimentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);
	
	public List<AreaConhecimentoVO> consultarDisciplinasPorGradeCurricularPeriodoLetivo(Integer gradeCurricular, Integer periodoLetivo, UsuarioVO usuario) throws Exception;

	AreaConhecimentoVO consultarPorChavePrimariaUnico(Integer codigoPrm, UsuarioVO usuario) throws Exception;

}