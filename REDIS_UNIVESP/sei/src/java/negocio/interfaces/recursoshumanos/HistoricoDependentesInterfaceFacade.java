package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoDependentesInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public void persistirTodos(FuncionarioVO funcionarioVO, List<FuncionarioDependenteVO> dependenteVOs, UsuarioVO usuario) throws Exception;
	
	public void alterarFuncionarioHistoricoDependentesUnificacaoFuncionario(Integer funcionarioAntigo, Integer funcionarioNovo) throws Exception;
}