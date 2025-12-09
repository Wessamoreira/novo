package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;

/**
 * @author Wellington - 25 de set de 2015
 *
 */
public interface OperacaoFuncionalidadeInterfaceFacade {

	/**
	 * Responsável por mater o registro de operações realizadas por funcionalidades que solicitam usuário e senha para confirmação.
	 * 
	 * @author Wellington - 25 de set de 2015
	 * @param obj
	 * @throws Exception
	 */
	void incluir(OperacaoFuncionalidadeVO obj) throws Exception;

	/**
	 * Responsável por executar a geração da operação funcionalidade de acordo com os parâmetros passado.
	 * 
	 * @author Wellington - 25 de set de 2015
	 * @param origem
	 * @param codigoOrigem
	 * @param operacao
	 * @param responsavel
	 * @param observacao
	 * @return
	 * @throws Exception
	 */
	OperacaoFuncionalidadeVO executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum origem, String codigoOrigem, OperacaoFuncionalidadeEnum operacao, UsuarioVO responsavel, String observacao) throws Exception;

	/**
	 * @author Wellington - 25 de set de 2015
	 * @param origem
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<OperacaoFuncionalidadeVO> consultarPorOrigem(OrigemOperacaoFuncionalidadeEnum origem, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 25 de set de 2015
	 * @param operacao
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<OperacaoFuncionalidadeVO> consultarPorOperacao(OperacaoFuncionalidadeEnum operacao, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 25 de set de 2015
	 * @param responsavel
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<OperacaoFuncionalidadeVO> consultarPorResponsavel(Integer responsavel, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 25 de set de 2015
	 * @param codigoOrigem
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<OperacaoFuncionalidadeVO> consultarPorCodigoOrigem(Integer codigoOrigem, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	List<OperacaoFuncionalidadeVO> consultarOperacaoFuncionalidadeVOPorCodigoOrigemPorOrigemOperacaoFuncionalidadePorOperacaoFuncionalidade(Integer codigoOrigem, OrigemOperacaoFuncionalidadeEnum oofe, OperacaoFuncionalidadeEnum ofe, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	boolean consultarPorCodigoOrigemPorOrigemOperacaoFuncionalidadePorOperacaoFuncionalidade(Integer codigoOrigem, OrigemOperacaoFuncionalidadeEnum oofe, OperacaoFuncionalidadeEnum ofe, UsuarioVO usuario) throws Exception;
}
