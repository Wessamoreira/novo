/**
 * 
 */
package relatorio.negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import relatorio.negocio.comuns.administrativo.OcorrenciaPatrimonioRelVO;

/**
 * @author Leonardo Riciolle
 *
 */
public interface OcorrenciaPatrimonioRelInterfaceFacede {


	/**
	 * @author Leonardo Riciolle - 16/06/2015
	 * @return
	 */
	public String designIReportRelatorioOcorrenciaPatrimonioRel();

	/**
	 * @author Leonardo Riciolle - 16/06/2015
	 * @param ocorrenciaPatrimonioRelVO
	 * @param unidadeEnsinoVO
	 * @param tipoPatrimonioVO
	 * @param localArmazenamentoVO
	 * @throws Exception
	 */
	public void validarDados(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO) throws Exception;

	/**
	 * @author Leonardo Riciolle - 17/06/2015
	 * @param ocorrenciaPatrimonioRelVO
	 * @param unidadeEnsinoVO
	 * @param patrimonioVO
	 * @param tipoPatrimonioVO
	 * @param localArmazenamentoVO
	 * @param usuarioVO
	 * @param dataInicio
	 * @param dataFim
	 * @param ordenarPor
	 * @return
	 * @throws Exception
	 */
	List<OcorrenciaPatrimonioRelVO> criarObjeto(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, UsuarioVO usuarioVO, Date dataInicio, Date dataFim, String ordenarPor) throws Exception;
}
