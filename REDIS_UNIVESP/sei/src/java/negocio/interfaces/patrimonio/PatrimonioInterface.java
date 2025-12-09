package negocio.interfaces.patrimonio;
/**
 * 
 * @author Leonardo Riciolle
 */

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface PatrimonioInterface {

	public void excluir(PatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void alterar(PatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void persistir(PatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<PatrimonioVO> consultar(String valorConsulta, String campoConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception;

	public PatrimonioVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistro(String valorConsulta, String campoConsulta) throws Exception;
		
	public void adicionarPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO obj, Integer quantidadePatrimonioUnidadeGerar, String codigoBarraInicial) throws Exception;

	void realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException;

	void removerPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO obj);

	void realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException;

	/**
	 * @author Rodrigo Wind - 20/05/2016
	 * @param patrimonioVO
	 * @return
	 */
	List<LocalArmazenamentoVO> realizarSeparacaoPatrimonioUnidadePorLocalArmazenamento(PatrimonioVO patrimonioVO);

}
