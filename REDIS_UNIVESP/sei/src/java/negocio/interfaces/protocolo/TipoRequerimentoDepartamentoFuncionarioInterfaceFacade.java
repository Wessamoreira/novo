package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoFuncionarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface TipoRequerimentoDepartamentoFuncionarioInterfaceFacade {

	void incluir(final TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, UsuarioVO usuarioVO) throws Exception;
	void excluir(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, UsuarioVO usuarioVO) throws Exception;
	void incluirListaTipoRequerimentoDepartamentoFuncionarioVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception;
	List<TipoRequerimentoDepartamentoFuncionarioVO> consultarPorTipoRequerimentoDepartamento(Integer tipoRequerimentoDepartamento, UsuarioVO usuarioVO) throws Exception;
	void validarDados(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO) throws ConsistirException;
	void alterarListaTipoRequerimentoDepartamentoFuncionarioVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception;
	
}
