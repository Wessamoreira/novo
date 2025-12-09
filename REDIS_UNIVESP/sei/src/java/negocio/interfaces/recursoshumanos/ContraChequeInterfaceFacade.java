package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FichaFinanceiraRelVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ContraChequeInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, Date dataCompetenciaInicial, Date dataCompetenciaFinal)  throws Exception;

	public void validarEventoContraChequeItem(ContraChequeEventoVO contraChequeEvento, List<ContraChequeEventoVO> listaContraChequeEvento) throws ConsistirException;

	public ContraChequeVO consultarPorCodigoECompetenciaAtiva(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public void excluirContraChequePorLancamento(LancamentoFolhaPagamentoVO lancamento, UsuarioVO usuarioVO) throws Exception;

	public ContraChequeVO consultarPorFuncionarioCargoEPeriodo(String matricula, Integer codigoCompetenciaFolha) throws Exception;

	public ContraChequeVO consultarUltimoContraCheque() throws Exception;
	
	public FichaFinanceiraRelVO montarDadosFichaFinanceira(ContraChequeVO contraChequeVO) throws Exception;

	public List<FichaFinanceiraRelVO> montarDadosRelatorioFichaFinanceira(TemplateLancamentoFolhaPagamentoVO obj, CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo) throws Exception;
	
	public void gerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento, UsuarioVO usuarioLogado, List<FuncionarioCargoVO> listaDeFuncionarios) throws Exception;

	public ContraChequeVO consultarPorFuncionarioCargoECompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception;

	public ContraChequeVO consultarPorFuncionarioCargoCompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception;

	public void realizarCalculoContraChequeDoFuncionario(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioLogado, ScriptEngine engine) throws Exception;

	public void cancelarFolhaDePagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado, ScriptEngine engine) throws Exception;

	public CalculoContraCheque realizarSomaDosValoresJaCalculadosDoContraCheque(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, ContraChequeVO contraChequeVO, boolean calculado, ScriptEngine engine, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception;

	public void realizarCalculoContraChequeDaListaDeFuncionariosFiltrados(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado,
			BigDecimal valorDependente, ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF, List<String> identificadores, ScriptEngine engine,
			CalculoContraCheque calculoContraCheque, FuncionarioCargoVO funcionarioCargo) throws Exception;

	public List<Integer> consultarIDsPorFuncionarioCargoCompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception;
	
	public List<ContraChequeVO> consultarContraChequePorCompetenciaEFiltrosDoTemplate(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento, TemplateLancamentoFolhaPagamentoVO templateFP);	
	
	public List<Integer> consultarFuncionarioCargoPorLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception;
	
	/**
	 * Chamada do Recalculo do Contracheque na tela de Contracheque
	 * @param contraChequeVO
	 * @param listaDeEventosDoFuncionario
	 * @param calculoContraCheque
	 * @param usuarioLogado
	 * @throws Exception
	 */
	public void realizarRecalculoContraChequeDoFuncionario(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioLogado, ScriptEngine engine, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception;

	public int consultarTotalContraChequePorFuncionarioECompetencia(FuncionarioCargoVO funcionarioCargoVO, CompetenciaFolhaPagamentoVO competenciaFolhaPagamento);

	public void validarSeExisteContraChequeParaFuncionarioCargo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, FuncionarioCargoVO funcionarioCargoVO) throws Exception;
	
	public void adicionarEventosFixoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias);

	public void adicionarEventosDeEmprestimo(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias);

	public void adicionarEventosDePensaoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario);
	
	public void adicionarEventosVinculados(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo);

	public List<ContraChequeVO> consultarContraChequePorTemplateLancamentoFolha(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO) throws Exception ;

	public void gerarContaPagar(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO, UsuarioVO usuarioLogado, List<String> errosContaPagar) throws Exception;

	public void atualizarContaPagar(Integer codigo, UsuarioVO usuario) throws Exception;
}