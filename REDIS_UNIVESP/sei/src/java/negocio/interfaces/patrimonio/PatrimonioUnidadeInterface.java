package negocio.interfaces.patrimonio;
/**
 * 
 * @author Leonardo Riciolle
 */

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface PatrimonioUnidadeInterface {

	public void persistir(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void persistirPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;	

	public void realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException;

	public List<PatrimonioUnidadeVO> consultarPorChavePrimariaPatrimonio(Integer codigo, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluirPorPatrimonio(Integer patrimonio, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;	

	String consultarSugestaoCodigoBarra(PatrimonioVO patrimonioVO) throws Exception;

	void alterarSituacaoELocalArmazenamentoPatrimonioUnidade(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarLocalArmazenamentoPatrimonioUnidade(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoPatrimonioUnidade(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(PatrimonioUnidadeVO obj) throws ConsistirException;

	PatrimonioUnidadeVO consultarPatrimonioUnidadePorCodigoBarra(String codigoBarra, UnidadeEnsinoVO unidadeEnsinoLogado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	Boolean consultarExistenciaPatrimonioUnidadePorCodigoBarra(String codigoBarra) throws Exception;

	void realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException;

	/**
	 * @author Rodrigo Wind - 25/05/2015
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @param unidadeEnsinoLogado
	 * @param limite
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	List<PatrimonioUnidadeVO> consultar(String campoConsulta, String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoLogado, Integer limite, Integer offset) throws Exception;

	/**
	 * @author Rodrigo Wind - 25/05/2015
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param unidadeEnsinoLogado
	 * @return
	 * @throws Exception
	 */
	Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception;
	
	List<PatrimonioUnidadeVO> consultarPorTipoPatrimonioParaListagemDeOcorrenciasPorUnidade(Integer tipopatrimonio, Date dataReserva, UnidadeEnsinoVO unidadeEnsinoLogado);

	List<PatrimonioUnidadeVO> consultarPorTipoPatrimonioDataUnidadeEnsinoSolicitanteParaListagemDeOcorrenciasPorUnidade(Integer tipopatrimonio, UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, FuncionarioVO solicitante);


}
