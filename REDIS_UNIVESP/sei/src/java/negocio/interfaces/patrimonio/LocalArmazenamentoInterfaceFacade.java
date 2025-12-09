package negocio.interfaces.patrimonio;

import java.util.Date;
import java.util.List;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface LocalArmazenamentoInterfaceFacade {

	void persistir(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<LocalArmazenamentoVO> consultar(TipoConsultaLocalArmazenamentoEnum consultarPor, String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoLogado, Integer limit, Integer pagina, Boolean apenasLocalPermiteReserva) throws Exception;

	Integer consultarTotalRegistro(TipoConsultaLocalArmazenamentoEnum consultarPor, String valorConsulta, UnidadeEnsinoVO unidadeEnsinoLogado, Boolean apenasLocalPermiteReserva) throws Exception;

	List<LocalArmazenamentoVO> consultarPorChavePrimaria(Integer codigo, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	TreeNodeCustomizado consultarArvoreLocalArmazenamentoSuperior(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	TreeNodeCustomizado consultarArvoreLocalArmazenamentoInferior(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(LocalArmazenamentoVO localArmazenamentoVO) throws ConsistirException, Exception;

	void validarDadosLocalSuperior(LocalArmazenamentoVO localArmazenamentoVO, LocalArmazenamentoVO localArmazenamentoSuperiorVO) throws ConsistirException;

	/**
	 * @author Leonardo Riciolle - 02/06/2015
	 * @param localArmazenamento
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<LocalArmazenamentoVO> consultarPorLocalArmazenamento(String localArmazenamento, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Leonardo Riciolle - 16/06/2015
	 * @param codigo
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	public LocalArmazenamentoVO consultarPorCodigo(Integer codigo, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<LocalArmazenamentoVO> consultarPorLocalDataUnidadeEnsinoSolicitanteParaListagemDeOcorrenciasPorLocal(Integer local, UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, FuncionarioVO solicitante);

}
