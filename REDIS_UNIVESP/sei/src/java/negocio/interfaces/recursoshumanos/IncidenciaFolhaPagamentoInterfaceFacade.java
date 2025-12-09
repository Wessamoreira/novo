package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.IncidenciaFolhaPagamentoVO;

public interface IncidenciaFolhaPagamentoInterfaceFacade {

	public void persistir(IncidenciaFolhaPagamentoVO incidencia, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(IncidenciaFolhaPagamentoVO incidencia, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<IncidenciaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<IncidenciaFolhaPagamentoVO> consultarPorNome(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public IncidenciaFolhaPagamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

	
}