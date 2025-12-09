package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface FiltroPersonalizadoOpcaoInterfaceFacade {

	void alterarFiltroPersonalizadoOpcoes(FiltroPersonalizadoVO obj,
			List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception;

	void incluirFiltroPersonalizadoOpcoes(FiltroPersonalizadoVO obj,
			List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception;

	void excluir(FiltroPersonalizadoOpcaoVO obj, UsuarioVO usuarioVO);

	List<FiltroPersonalizadoOpcaoVO> consultarPorFiltroPersonalizado(Integer filtroPersonalizado, UsuarioVO usuarioVO);
	
	void removerFiltroPersonalizadoOpcao(FiltroPersonalizadoOpcaoVO objAdicionar,
			List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs, UsuarioVO usuarioVO);

	void adicionarFiltroPersonalizadoOpcao(FiltroPersonalizadoOpcaoVO objAdicionar,
			FiltroPersonalizadoVO filtroPersonalizadoVO,
			List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs, UsuarioVO usuarioVO) throws Exception;

	String consultarCampoQueryPorCodigo(Integer codigo, UsuarioVO usuarioVO);

}
