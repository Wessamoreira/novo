package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CategoriaGEDInterfaceFacade {

	public void persistir(CategoriaGEDVO categoriaGEDVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(CategoriaGEDVO categoriaGEDVO, UsuarioVO usuarioVO) throws Exception;

	public List<CategoriaGEDVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public int consultarTotalRegistrosPorDescricaoOuIdentificador(CategoriaGEDVO obj);

	public CategoriaGEDVO consultarPorIdentificador(String identificador) throws Exception;

	@SuppressWarnings("rawtypes")
	public List consultar(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	CategoriaGEDVO consultarPorChavePrimaria(Integer codigo) throws Exception;

	CategoriaGEDVO consultarPorChavePrimariaUnico(Integer codigo) throws Exception;
}