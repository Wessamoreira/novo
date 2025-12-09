package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoIncidenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.IncidenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface EventoIncidenciaFolhaPagamentoInterfaceFacade {

	public void excluirPorEvento(Integer codigoEvento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<EventoIncidenciaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void incluirIncidencias(EventoFolhaPagamentoVO obj, UsuarioVO usuario) throws Exception;

	public void alterarIncidencias(EventoFolhaPagamentoVO obj, UsuarioVO usuario) throws Exception;

	public void validarDadosPorIncidenciaFolhaPagamento(IncidenciaFolhaPagamentoVO obj) throws ConsistirException;
}