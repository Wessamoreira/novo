package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface MarcacaoFeriasInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {
	
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario, String situacaoMarcacao) throws Exception;

	public MarcacaoFeriasVO consultarPorChavePrimaria(Integer id, int nivelMontarDados) throws Exception;

	public MarcacaoFeriasVO consultarMarcacaoDiferenteDeFechadaPorFuncionario(String matriculaCargo) throws Exception;
	
	public MarcacaoFeriasVO consultarMarcacaoPorFuncionarioESituacao(String matriculaCargo, SituacaoMarcacaoFeriasEnum situacao);
	
	public MarcacaoFeriasVO consultarMarcacaoPorFuncionarioSituacaoEDataFerias(String matriculaCargo, SituacaoMarcacaoFeriasEnum situacao, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO);

	public void calcularRecibo(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO reciboFeriasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void cancelarRecibo(MarcacaoFeriasVO marcacaoFeriasVO, boolean b, UsuarioVO usuarioLogado) throws Exception;
	
	//Metodo comentado
	//Simplificar o processo ao centralizar o lancamento do adiantamento de ferias na tela de Lancamento
	//public void lancarAdiantamendoNaFolha(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO reciboFeriasVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception;

	//Metodo comentado
	//Simplificar o processo ao centralizar o lancamento do recibo de ferias na tela de Lancamento
	//public void lancarEventosDoReciboNoContraCheque(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO reciboFeriasVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception;
	
	public void alterarSituacaoMarcacao(MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, Boolean lancadoAdiantamento, Boolean lancadoDescontoAdiantamento);
	
	public List<Integer> consultarFuncionariosMarcacaoFerias();

	public List<Integer> consultarFuncionariosMarcacaoRetornoFerias(Date dataCompetencia);

	public EventoFolhaPagamentoVO consultaEventoDeAdiantamentoDoContraCheque(FuncionarioCargoVO funcionarioCargoVO, ReciboFeriasVO reciboFeriasVO) throws Exception;
	
	public EventoFolhaPagamentoVO consultaEventoDeDescontoDoAdiantamentoDoContraCheque(FuncionarioCargoVO funcionarioCargoVO, ReciboFeriasVO reciboFeriasVO) throws Exception;

	public MarcacaoFeriasVO inicializarMarcacaoDeFeriasPrimeiroPeriodoAquisitivoAbertoDoFuncionario(String matriculaFuncionarioCargo) throws Exception;
	
	public void alterarSituacaoFerias(MarcacaoFeriasVO marcacaoFerias, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void montarDadosPorMarcacaoFeriasColetiva(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, FuncionarioCargoVO funcionarioCargoVO, MarcacaoFeriasVO marcacaoFerias);

	public MarcacaoFeriasVO inicializarMarcacaoDeFeriasPrimeiroPeriodoAquisitivoVencidoDoFuncionario(String matriculaCargo);
	
	public List<MarcacaoFeriasVO> consultarListaDeMarcacaoDeFeriasPorFiltroDosFuncionarioESituacao(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO, SituacaoMarcacaoFeriasEnum situacao);

	void excluirMarcacaoFerias(MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, UsuarioVO usuarioVO) throws Exception;

	public void alterarLancadoAdiantamentoPorFuncionarioCargo(Integer codigoFuncionario, boolean lancadoAdiantamento) throws Exception;
	
	public void finalizarMarcacaoDeFerias(MarcacaoFeriasVO marcacaoFeriasVO, boolean encerrarPeriodoAquisitivo, Date dataFechamento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void persistirMarcacaoFerias(MarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void calcularRecibo(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void finalizarMarcacaoDeFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, boolean encerrarPeriodoAquisitivo, Date dataFechamento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void validarDadosAdiantamento(MarcacaoFeriasVO obj) throws Exception;

	public void validarDadosLancarContraCheque(MarcacaoFeriasVO obj) throws Exception;

	public void processaSituacaoFuncionarioFerias() throws Exception;

	public MarcacaoFeriasVO consultarMarcacaoPorPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO,
			boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}