package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.HistoricoMarcacaoFeriasColetivaVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoMarcacaoFeriasColetivaInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public List<HistoricoMarcacaoFeriasColetivaVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, int nivelMontarDados) throws Exception ;

	public void excluirHistoricoPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO,	UsuarioVO usuarioVO) throws Exception;
	
	public List<HistoricoMarcacaoFeriasColetivaVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum situacao, int nivelMontarDados) throws Exception;

	public void excluirHistoricoPorMarcacaoDeFeriasEFuncionarioCargo(MarcacaoFeriasVO marcacaoFerias, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO);
	
}