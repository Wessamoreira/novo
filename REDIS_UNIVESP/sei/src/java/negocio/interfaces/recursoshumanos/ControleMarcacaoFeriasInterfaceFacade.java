package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ControleMarcacaoFeriasInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public List<ControleMarcacaoFeriasVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, int nivelMontarDados) throws Exception ;

	public void excluirHistoricoPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO,	UsuarioVO usuarioVO) throws Exception;
	
	public List<ControleMarcacaoFeriasVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum situacao, int nivelMontarDados) throws Exception;

	public void excluirHistoricoPorMarcacaoDeFeriasEFuncionarioCargo(MarcacaoFeriasVO marcacaoFerias, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO);

	public ControleMarcacaoFeriasVO montarDadosControleMarcacaoFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFerias, FuncionarioCargoVO funcionarioCargoVO);
	
	public ControleMarcacaoFeriasVO consultarDadosPorMarcacaoFerias(MarcacaoFeriasVO marcacaoFeriasVO) throws Exception;
	
	public void salvarControleMarcacaoFerias(MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
		
	public void salvarControleMarcacaoFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void salvarControleMarcacaoFerias(ControleMarcacaoFeriasVO controle, MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public ControleMarcacaoFeriasVO consultarControlePorFuncionarioCargoEPeriodoGozo(FuncionarioCargoVO funcionarioCargoVO, Date dataFinalGozo);
	
	public List<ControleMarcacaoFeriasVO> consultarFinalDasFeriasDoFuncionarios(Date dataCompetencia, int nivelMontarDados);

	public void cancelarLancamentoDoAdiantamentoDoReciboNoContraCheque(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO templateFP, UsuarioVO usuarioVO);
	
	public void cancelarLancamentoDoReciboDeFeriasNoContraCheque(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO templateFP, UsuarioVO usuarioVO);

	public List<ControleMarcacaoFeriasVO> consultarPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(Date dataCompetencia, boolean lancadorecibonocontracheque) throws Exception;

	public List<ControleMarcacaoFeriasVO> consultarFuncionariosComFinalDasFeriasMaioQueCompetencia(Date dataCompetencia, int nivelMontarDados);
	
	public List<ControleMarcacaoFeriasVO> consultarDadosPorFiltrosMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO obj, SituacaoMarcacaoFeriasEnum situacao, int nivelMontarDados) throws Exception; 
}