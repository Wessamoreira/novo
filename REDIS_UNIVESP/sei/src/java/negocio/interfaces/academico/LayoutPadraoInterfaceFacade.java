package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.OrdenadorVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioContaReceberVO;

public interface LayoutPadraoInterfaceFacade {

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void incluir(final LayoutPadraoVO obj) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void excluir(LayoutPadraoVO obj) throws Exception;

	public abstract List<LayoutPadraoVO> consultarPorEntidade(String entidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public abstract LayoutPadraoVO consultarPorCampo(String campo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public abstract LayoutPadraoVO consultarPorValor(String valor, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadrao(String valor, String entidade, String campo, Integer assinaturaFunc1, Integer assinaturaFunc2, Integer assinaturaFunc3, Boolean apresentarTopoRelatorio,
			 String tituloAssinaturaFunc1, String tituloAssinaturaFunc2, String tituloAssinaturaFunc3, String observacaoComplementarIntegralizado, String textoCertidaoEstudo, UsuarioVO usuario, String nomeCargo1Apresentar, String nomeCargo2Apresentar) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void persistirLayoutPadrao(String valor, String entidade, String campo, UsuarioVO usuario) throws Exception;
	
	public abstract LayoutPadraoVO consultarPorEntidadeCampo(String entidade, String campo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/11/2015
	 * @param campos
	 * @param entidade
	 * @return
	 */
	Map<String, String> consultarValoresPadroes(String[] campos, String entidade);

	/**
	 * @author Rodrigo Wind - 20/11/2015
	 * @param valor
	 * @param entidade
	 * @param campo
	 * @param usuario
	 * @throws Exception
	 */
	void persistirLayoutPadrao2(String valor, String entidade, String campo, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/11/2015
	 * @param valor
	 * @param entidade
	 * @param campo
	 * @param assinaturaFunc1
	 * @param assinaturaFunc2
	 * @param apresentarTopoRelatorio
	 * @param tituloAssinaturaFunc1
	 * @param tituloAssinaturaFunc2
	 * @param observacaoComplementarIntegralizado
	 * @param textoCertidaoEstudo
	 * @param usuario
	 * @throws Exception
	 */
	void persistirLayoutPadrao2(String valor, String entidade, String campo, Integer assinaturaFunc1, Integer assinaturaFunc2, Boolean apresentarTopoRelatorio, String tituloAssinaturaFunc1, String tituloAssinaturaFunc2, String observacaoComplementarIntegralizado, String textoCertidaoEstudo, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 12/04/2016
	 * @param filtroRelatorioAcademicoVO
	 * @param entidade
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception;
	
	void persistirFiltroSituacaoHistorico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 12/04/2016
	 * @param filtroRelatorioAcademicoVO
	 * @param entidade
	 * @param usuarioVO
	 * @throws Exception
	 */
	void consultarPadraoFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception;
	
	void consultarPadraoFiltroSituacaoHistorico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/04/2016
	 * @param filtroRelatorioFinanceiroVO
	 * @param entidade
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String entidade, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/04/2016
	 * @param filtroRelatorioFinanceiroVO
	 * @param entidade
	 * @param usuarioVO
	 * @throws Exception
	 */
	void consultarPadraoFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String entidade, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 02/05/2016
	 * @param ordenadorVOs
	 * @param entidade
	 * @param usuario
	 * @throws Exception
	 */
	void consultarOrdenacaoPadrao(List<OrdenadorVO> ordenadorVOs, String entidade, UsuarioVO usuario) throws Exception;

	/**
	 * @author Carlos Eugênio - 02/05/2016
	 * @param ordenadorVOs
	 * @param entidade
	 * @param usuario
	 * @throws Exception
	 */
	void persistirOrdenacao(List<OrdenadorVO> ordenadorVOs, String entidade, UsuarioVO usuario) throws Exception;

	void excluirLayoutPorAgrupadorEntidade(String entidade, String agrupador, UsuarioVO usuarioVO);

	void persistirLayoutPadraoComAgrupador(String valor, String entidade, String campo, String agrupador,
			UsuarioVO usuario) throws Exception;

	List<LayoutPadraoVO> consultarAgrupadoresPorEntidade(String entidade);

	void persistirFiltroRelatorioContaReceberVO(FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, String entidade, UsuarioVO usuarioVO) throws Exception;

	void consultarPadraoFiltroRelatorioContaReceberVO(FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, String entidade, UsuarioVO usuarioVO) throws Exception;

	public void persistirLayoutPadraoComTituloRelatorio(String valor, String entidade, String campo, String tituloRelatorio, UsuarioVO usuario) throws Exception;
	
	public LayoutPadraoVO consultarPorEntidadeCampoValor(String entidade, String campo, String valor, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void persistirLayoutPadrao(String valor, String entidade, String campo, Integer assinaturaFunc1, Integer assinaturaFunc2, Integer assinaturaFunc3, Integer assinaturaFunc4, Integer assinaturaFunc5, Boolean apresentarTopoRelatorio, String tituloAssinaturaFunc1, String tituloAssinaturaFunc2, String tituloAssinaturaFunc3, String tituloAssinaturaFunc4, String tituloAssinaturaFunc5, String observacaoComplementarIntegralizado, String textoCertidaoEstudo, UsuarioVO usuario, String nomeCargo1Apresentar, String nomeCargo2Apresentar, String nomeCargo3Apresentar, String nomeCargo4Apresentar, String nomeCargo5Apresentar) throws Exception;
}