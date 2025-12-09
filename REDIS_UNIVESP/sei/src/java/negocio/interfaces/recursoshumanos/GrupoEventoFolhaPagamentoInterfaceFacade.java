package negocio.interfaces.recursoshumanos;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.GrupoEventoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface GrupoEventoFolhaPagamentoInterfaceFacade  {
	
	public void incluir(GrupoEventoFolhaPagamentoVO GrupoEventoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(GrupoEventoFolhaPagamentoVO GrupoEventoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void validarDadosPorEventoFolhaPagamento(EventoFolhaPagamentoVO obj) throws ConsistirException;

}
