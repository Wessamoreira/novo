package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.TransferenciaTurnoVO;

public interface TransferenciaTurnoInterfaceFacade {

	public void persistir(TransferenciaTurnoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaTurnoVO> consultar(String valorConsulta, String campoConsulta, Integer limite, Integer offset, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public TransferenciaTurnoVO consultarPorChavePrimaria(Integer codigo, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 06/03/2015
	 * @param matriculaPeriodoDestino
	 * @param usuarioLogado
	 */
	public void validarDados(MatriculaPeriodoVO matriculaPeriodoDestino, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Wellington Rodrigues - 06/03/2015
	 * @param transferenciaTurnoVO
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	boolean verificarRegistroAulaParaAbono(TransferenciaTurnoVO transferenciaTurnoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 09/03/2015
	 * @param matriculaPeriodoOrigem
	 * @param matriculaPeriodoDestino
	 * @param usuario
	 * @throws Exception
	 */
	void realizarPreenchimentoObjetosParaPersistencia(MatriculaPeriodoVO matriculaPeriodoOrigem, MatriculaPeriodoVO matriculaPeriodoDestino, UsuarioVO usuario) throws Exception;
	
	public void validarDadosPermiteTransferenciaTurno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
	
	public Integer consultarQuantidadeTotalRegistros(String valorConsulta, String campoConsulta) throws Exception;
}
