/**
 * 
 */
package relatorio.negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import relatorio.negocio.comuns.administrativo.PatrimonioRelVO;

/**
 * @author Leonardo Riciolle
 *
 */
public interface PatrimonioRelInterfaceFacade {

	/**
	 * @author Leonardo Riciolle - 02/06/2015
	 * @param patrimonioRelVO
	 * @param unidadeEnsinoVO
	 * @param patrimonioVO
	 * @param tipoPatrimonioVO
	 * @param localArmazenamentoVO
	 * @param usuarioVO
	 * @param ordenarPor
	 * @return
	 * @throws Exception
	 */
	List<PatrimonioRelVO> criarObjetoAnalitico(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, UsuarioVO usuarioVO, String ordenarPor) throws Exception;

	/**
	 * @author Leonardo Riciolle - 03/06/2015
	 * @return
	 */
	String designIReportRelatorioAnalitico();

	/**
	 * @author Leonardo Riciolle - 03/06/2015
	 * @return
	 */
	String designIReportRelatorioSinteticoPatrimoio();

	/**
	 * @author Leonardo Riciolle - 08/06/2015
	 * @param patrimonioRelVO
	 * @param unidadeEnsinoVO
	 * @param patrimonioVO
	 * @param tipoPatrimonioVO
	 * @param localArmazenamentoVO
	 * @param usuarioVO
	 * @param ordenarPor
	 * @return
	 * @throws Exception
	 */
	List<PatrimonioRelVO> executarConsultaParametrizadaSintetico(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, UsuarioVO usuarioVO, String ordenarPor) throws Exception;

	/**
	 * @author Leonardo Riciolle - 08/06/2015
	 * @return
	 */
	String designIReportRelatorioSinteticoPorTipoPatrimoio();

	/**
	 * @author Leonardo Riciolle - 08/06/2015
	 * @return
	 */
	String designIReportRelatorioSinteticoPorLocalArmazenamento();

	/**
	 * @author Leonardo Riciolle - 08/06/2015
	 * @param patrimonioRelVO
	 * @param unidadeEnsinoVO
	 * @param patrimonioVO
	 * @param tipoPatrimonioVO
	 * @param localArmazenamentoVO
	 * @param usuarioVO
	 * @param ordenarPor
	 * @return
	 * @throws Exception
	 */
	List<PatrimonioRelVO> executarConsultaParametrizadaSinteticoLocalArmazenamento(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, UsuarioVO usuarioVO, String ordenarPor) throws Exception;

	/**
	 * @author Leonardo Riciolle - 09/06/2015
	 * @param patrimonioRelVO
	 * @param unidadeEnsinoVO
	 * @param tipoPatrimonioVO
	 * @param localArmazenamentoVO
	 * @throws Exception
	 */
	void validarDados(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO) throws Exception;

	String designIReportRelatorioAnaliticoExcel();

	String designIReportRelatorioSinteticoPatrimoioExcel();

	String designIReportRelatorioSinteticoPorTipoPatrimoioExcel();

	String designIReportRelatorioSinteticoPorLocalArmazenamentoExcel();
}
