package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoRelVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ContraChequeEventoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{

	List<ContraChequeEventoVO> consultarPorContraCheque(Integer codigo, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	T montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception ;

	void persistirTodos(ContraChequeVO obj, boolean b, UsuarioVO usuario) throws Exception ;

	public void excluirContraChequeEventoPorLancamentoDoContraCheque (LancamentoFolhaPagamentoVO lancamento, UsuarioVO usuario) throws Exception;

	public void excluirContraChequeEventoPorContraCheque (ContraChequeVO contraCheque, UsuarioVO usuario) throws Exception;

	public List<ContraChequeEventoRelVO> consultarContraChequeEventos(TemplateLancamentoFolhaPagamentoVO obj, CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo, SecaoFolhaPagamentoVO secaoFolhaPagamentoVO) throws Exception;

	public void excluirContraChequeEventoPorCompetenciaFiltroTemplateLancamentoEPeriodo(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO template, CompetenciaPeriodoFolhaPagamentoVO periodo, UsuarioVO usuarioLogado);

	/**
	 * Cria objeto ContraChequeEventoVO a partir do EventoFolhaPagamentoVO
	 * 
	 * @param evento
	 * @param valorResultadoFormula
	 * @param textoResultadoFormula
	 * @return
	 */
	public ContraChequeEventoVO montarContraChequeEventoAPartirDoEvento(EventoFolhaPagamentoVO evento, ContraChequeVO contraChequeVO);

	/**
	 * Cancelar os contrachque dos filtros selecionados no lancamento
	 * @param lancamentoFolhaPagamento
	 * @param usuarioLogado
	 */
	public void cancelarContraCheque(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado);

	public void excluirContraChequeEventoQueEstaoZerados(ContraChequeVO obj, boolean b, UsuarioVO usuarioVO);

	public List<ContraChequeEventoVO> consultarPorCompetenciaEventoEmprestimo(Long codigoCompetencia) throws Exception;

	public List<ContraChequeEventoVO> consultarPorCompetenciaEventoFixo(Long codigoCompetencia) throws Exception;

	/**
	 * Retorna o valor da media dos eventos dento da data especificada
	 * 
	 * @param funcionarioCargoVO
	 * @param grupo
	 * @param incidencia
	 * @param dataInicial
	 * @param DataFinal
	 * @return
	 */
	public BigDecimal consultarValorDaMediaDosEventosDoGrupoPorFuncionarioEPeriodo(FuncionarioCargoVO funcionarioCargoVO, String grupo, String incidencia, String dataInicial, String dataFinal);

	List<ContraChequeEventoRelVO> consultarContraChequeEventosSecao(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento,
			CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo) throws Exception;

	List<ContraChequeEventoVO> consultarPorIDsContraCheque(Integer codigo, List<Integer> listaIdsContraCheque) throws Exception;
	
	/**
	 * Consulta os dados do ContraCheque pelos parametros
	 * @param contraChequeVO
	 * @param competenciaPeriodoFolhaPagamentoVO
	 * @return
	 * @throws Exception
	 */
	public List<ContraChequeEventoVO> consultarDados(ContraChequeVO contraChequeVO, CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO) throws Exception;

	public BigDecimal consultarValorDoEventoDeIRRFDoContraCheque(ContraChequeVO contraChequeVO);
	
	public BigDecimal consultarValorDaBaseCalculoIRRFDoContraCheque(ContraChequeVO contraChequeVO);
	
	public void excluirTodosQueNaoEstaoNaListaContraChequeEventoEPeriodo(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	void excluirContraChequeEventoDoContrachequeEPeriodo(ContraChequeVO obj, CompetenciaPeriodoFolhaPagamentoVO periodo, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public BigDecimal consultarValorDoEventoDaPrimeiraParcela13(ContraChequeVO contraCheque, EventoFolhaPagamentoVO eventoFolhaPagamentoVO);

	public SqlRowSet consultarEventosDePensaoLancadosNoContraCheque(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO);
}