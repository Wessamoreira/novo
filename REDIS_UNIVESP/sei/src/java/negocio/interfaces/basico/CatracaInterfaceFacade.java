package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CatracaVO;

public interface CatracaInterfaceFacade {

	public void persistir(CatracaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(final CatracaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public CatracaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<CatracaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<CatracaVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<CatracaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public CatracaVO consultarPorNumeroSerie(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public CatracaVO consultarPorEnderecoIP(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	public List<CatracaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	public List<CatracaVO> consultarCatracaComboBox(String situacao, UsuarioVO usuarioVO);

}
