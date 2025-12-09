package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.RescisaoIndividualVO;
import negocio.comuns.recursoshumanos.RescisaoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;


public interface RescisaoIndividualInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	public RescisaoIndividualVO consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) ;
	
	public List<RescisaoIndividualVO> consultarPorRescisao(RescisaoVO rescisaoVO) throws Exception;

	public List<Integer> consultarFuncionariosCargoPorRescisao(RescisaoVO rescisaoVO) throws Exception;
	
	public void consultar(DataModelo dataModelo) throws Exception;

	public boolean validarSeExisteRescisaoParaFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception; 
	
	public RescisaoIndividualVO consultarPorFuncionarioCargoECompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO);
	
	public List<RescisaoIndividualVO> consultarPorFormaContratacaoFuncionario(RescisaoVO rescisaoVO) throws Exception ;
	
	public RescisaoIndividualVO montarDados(RescisaoVO rescisaoVO, FuncionarioCargoVO funcionarioCargoVO, HistoricoSituacaoVO historicoSituacaoVO);
}