package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoEmprestimoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface EventoEmprestimoCargoFuncionarioInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{
	
	public void persistir(List<EventoEmprestimoCargoFuncionarioVO> listaDeEventoDeEmprestimo, FuncionarioCargoVO funcionarioCargo, boolean verificarAcesso, UsuarioVO usuarioVO);
	
	public void consultarPorEnumCampoConsultaEventoEmprestimoCargoFuncionario(DataModelo dataModelo, String situacaoFuncionario) throws Exception;
	
	public void excluir(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario);
	
	public List<EventoEmprestimoCargoFuncionarioVO> consultarPorCargoFuncionario(FuncionarioCargoVO funcionarioCargo, DataModelo dataModelo) throws Exception;

	public void adicionarEventosDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias);

	public List<EventoEmprestimoCargoFuncionarioVO> consultarPorCompetenciaEventoEmprestimo(long longValue) throws Exception;

	public void alterarParcelaEmprestimo(EventoEmprestimoCargoFuncionarioVO eventoEmprestimo, UsuarioVO usuarioVO) throws Exception;

	public void alterarSituacaoQuitado(UsuarioVO usuarioVO) throws Exception;
}