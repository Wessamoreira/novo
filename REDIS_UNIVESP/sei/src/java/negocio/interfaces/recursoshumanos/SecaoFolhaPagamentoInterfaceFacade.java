package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;;

public interface SecaoFolhaPagamentoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;
	
	public T consultarPorChavePrimaria(Long codigo) throws Exception;
	
	public List<SecaoFolhaPagamentoVO> consultar(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public SecaoFolhaPagamentoVO consultarPorIdentificador(String identificadorSecao) throws Exception;
}
