package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ReciboFeriasEventoVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ReciboFeriasEventoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void persistirTodos(ReciboFeriasVO obj, boolean b, UsuarioVO usuario);

	public List<ReciboFeriasEventoVO> consultarPorReciboFerias(Integer p, int nivelmontardadosTodos, UsuarioVO usuario);

	public void excluirEventosDoRecibo(ReciboFeriasVO recibo, boolean b, UsuarioVO usuarioLogado) throws Exception;

	public void validarDadosReciboEvento(ReciboFeriasEventoVO obj, ReciboFeriasVO reciboFeriasVO) throws ConsistirException;
}