package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface PeriodoAquisitivoFeriasInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception;

	public void persistirTodos(List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivoFeriasFuncionario, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public List<PeriodoAquisitivoFeriasVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	public PeriodoAquisitivoFeriasVO consultarPrimeiroPeriodoAquisitivoAbertoPorFuncionarioCargo(String matriculaCargo);

	public PeriodoAquisitivoFeriasVO montarDadosAPartirFuncionarioCargo(FuncionarioCargoVO obj);

	public void preencherFinalPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO);

	public void inativarPeriodoAquisitivoFeriasDoFuncionarioCargo(FuncionarioCargoVO obj) throws Exception;

	public void alterarSituacao(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;	

	public void realizarAtualizacaoDoPeriodoAquisitivoDoFuncionario(CompetenciaFolhaPagamentoVO competencia, FuncionarioCargoVO funcionarioCargoVO) throws Exception;

	public void realizarAbrirNovoPeriodoAquisitivoParaFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) throws Exception;

	public PeriodoAquisitivoFeriasVO consultarPrimeiroPeriodoAquisitivoVencidoPorFuncionarioCargo(String matriculaCargo);
	
	public void encerrarPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivo, boolean encerrarPeriodoAquisitivo, Date dataFechamento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Consulta PeriodoAquisitivo de ferias do funcionario valido para marcacao de ferias (Vencido ou Aberto)
	 * 
	 * @param funcionarioCargo
	 * @return
	 */
	public PeriodoAquisitivoFeriasVO consultarPeriodoAquisitivoValidoParaFerias(FuncionarioCargoVO funcionarioCargo);

	public void validarDadosAdicionarPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO, List<PeriodoAquisitivoFeriasVO> listaPeriodosAquisitivos) throws ConsistirException;
	
	public List<PeriodoAquisitivoFeriasVO> consultarPeriodoAquisitivoAbertoEVencido() throws Exception;

	public Integer consultarQuantidadeFaltasPorPeriodoAquisitivo(Integer codigo);
	
}