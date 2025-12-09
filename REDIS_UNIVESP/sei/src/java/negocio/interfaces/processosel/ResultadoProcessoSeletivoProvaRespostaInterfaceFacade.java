package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoProvaRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;

public interface ResultadoProcessoSeletivoProvaRespostaInterfaceFacade {

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @param usuario
	 * @throws Exception
	 */
	void incluirResultadoProcessoSeletivoProvaRespostaVOs(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivo
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<ResultadoProcessoSeletivoProvaRespostaVO> consultarPorResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @return
	 * @throws Exception
	 */
	void executarGeracaoResultadoProcessoSeletivoProvaRespostaVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) throws Exception;

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @param usuario
	 * @throws Exception
	 */
	void alterarResultadoProcessoSeletivoProvaRespostaVOs(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuario) throws Exception;
	
	public void executarExclusaoGeracaoResultadoProcessoSeletivoProvaRespostaVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarResultadoProcessoSeletivoProvaRespostaVOs(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception;
}

