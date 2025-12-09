package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.TransferenciaTurnoDisciplinaVO;
import negocio.comuns.secretaria.TransferenciaTurnoVO;

public interface TransferenciaTurnoDisciplinaInterfaceFacade {

	public void persistir(final TransferenciaTurnoDisciplinaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void persistirTransferenciaTurnoDisciplinaVOs(List<TransferenciaTurnoDisciplinaVO> transferenciaTurnoDisciplinaVOs, Integer transferenciaTurno, UsuarioVO usuarioVO) throws Exception;

	public List<TransferenciaTurnoDisciplinaVO> consultarPorTransferenciaTurno(Integer transferenciaTurno, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 06/03/2015
	 * @param transferenciaTurnoVO
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param usuario
	 * @throws Exception
	 */
	void montarDadosTransferenciaTurnoDisciplina(TransferenciaTurnoVO transferenciaTurnoVO, UsuarioVO usuario) throws Exception;;
	
	public void executarGeracaoListaSelectItemTurmaTeoricaPraticaTransferenciaTurnoDisciplinaVO(TransferenciaTurnoDisciplinaVO transferenciaTurnoDisciplinaVO, UsuarioVO usuarioVO) throws Exception;
	
}
