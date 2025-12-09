/**
 * 
 */
package negocio.interfaces.patrimonio;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * @author Rodrigo Wind
 *
 */
public interface OcorrenciaPatrimonioInterfaceFacade {
	
    	public void persistir(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante, boolean permiteLiberarReservaForaLimiteDataMaxima, UsuarioVO usuarioVO) throws Exception;
	
	public void validarDados(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante, boolean permiteLiberarReservaForaLimiteDataMaxima, UsuarioVO usuarioVO) throws ConsistirException, Exception;
	
	public void realizarRegistroDevolucaoPatrimonio(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void excluir(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public List<OcorrenciaPatrimonioVO> consultarOcorrenciaPatrimonio(String campoConsulta, String valorConsulta, TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonio, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado, boolean controlarAcesso, Integer limit, Integer offset, PermissaoAcessoMenuVO permissaoAcessoMenuVO) throws Exception;
	
	public Integer consultarTotalOcorrenciaPatrimonio(String campoConsulta, String valorConsulta, TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonio, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado, PermissaoAcessoMenuVO permissaoAcessoMenuVO) throws Exception;
	
	public OcorrenciaPatrimonioVO consultarPorChavePrimaria(Integer codigo, boolean validarAcesso, UsuarioVO usuarioVO ) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/05/2015
	 * @param ocorrenciaPatrimonioVO
	 * @param patrimonioUnidadeVO
	 * @param usuarioVO
	 * @throws ConsistirException
	 */
	void validarDadosPatrimonioAptoTipoOcorrencia(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO, UsuarioVO usuarioVO) throws ConsistirException;

	/**
	 * @author Rodrigo Wind - 25/05/2015
	 * @param ocorrenciaPatrimonioVO
	 * @param patrimonioUnidadeVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void inicializarDadosPatrimonioUnidade(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO, UsuarioVO usuarioVO) throws Exception;
	
	List<OcorrenciaPatrimonioVO> consultarOcorrenciaPatrimonioPorUnidadePatrimonio(int codigoPatrimonioUnidade, Date dataReserva);

	List<OcorrenciaPatrimonioVO> consultarOcorrenciaPatrimonioPorLocalReservado(int codigoLocalReservado, Date dataReserva);

	List<OcorrenciaPatrimonioVO> consultarOcorrenciaUnidadePatrimioParaGestao(int patrimonioUnidade, Date dataInicial, Date dataFinal, FuncionarioVO solicitante);

	List<OcorrenciaPatrimonioVO> consultarOcorrenciaLocalParaGestao(int local, Date dataInicial, Date dataFinal, FuncionarioVO solicitante);

	String designOcorrenciaReservaRelatorio();

	String caminhoRelatorio();
	
}
