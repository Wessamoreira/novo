package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.enumeradores.OrigemFiltroPersonalizadoEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface FiltroPersonalizadoInterfaceFacade {

	List<FiltroPersonalizadoVO> consultarPorOrigemCodigoOrigem(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem,
			UsuarioVO usuarioVO);

	void adicionarFiltroPersonalizado(FiltroPersonalizadoVO objAdicionar,
			List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception;

	void removerFiltroPersonalizado(FiltroPersonalizadoVO objAdicionar,
			List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO);

	void alterarOrdemFiltroOpcao(FiltroPersonalizadoVO filtroPersonalizadoVO, FiltroPersonalizadoOpcaoVO filtroOpcaoVO1,
			FiltroPersonalizadoOpcaoVO filtroOpcaoVO2) throws Exception;

	void incluirFiltroPersonalizado(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem,
			List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception;

	void alterarFiltroPersonalizado(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem,
			List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception;

	void inicializarDadosComboBoxCustomizavel(FiltroPersonalizadoVO obj, Boolean utilizarCodigoComoKey,
			UsuarioVO usuarioVO);

	void inicializarDadosCombobox(FiltroPersonalizadoVO objAdicionar, UsuarioVO usuarioVO);

	void excluirFiltroPersonalizadoPorCodigoOrigemEOrigem(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem,
			UsuarioVO usuarioVO) throws Exception;

}
