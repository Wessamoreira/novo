package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFixoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;

public interface EventoFixoCargoFuncionarioInterfaceFacade {
	
	public void persistir(List<EventoFixoCargoFuncionarioVO> listaDeEventoFixos, FuncionarioCargoVO funcionarioCargo, boolean verificarAcesso, UsuarioVO usuarioVO);
	
	public List<FuncionarioCargoVO> consultarPorEnumCampoConsultaEventoFixoCargoFuncionario(DataModelo dataModelo, String situacaoFuncionario) throws Exception;
	
	public void excluir(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario);
	
	public void excluir(EventoFixoCargoFuncionarioVO eventoFixoCargoFuncionarioVO, boolean verificarAcesso, UsuarioVO usuario);
	
	public List<EventoFixoCargoFuncionarioVO> consultarPorCargoFuncionario(FuncionarioCargoVO funcionarioCargo, DataModelo dataModelo) throws Exception;

	public List<EventoFolhaPagamentoVO> consultarEventoFixoDoFuncionario(FuncionarioCargoVO funcionarioCargoVO);

	public void adicionarEventosDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias);

	public List<EventoFixoCargoFuncionarioVO> consultarPorCompetenciaEventoFixo(long longValue, UsuarioVO usuario);

	public void alterarNumeroLancamentoEventoFixo(EventoFixoCargoFuncionarioVO eventoFixoCargoFuncionarioVO, UsuarioVO usuario);
	
	public List<EventoFixoCargoFuncionarioVO> consultarEventoFixoZerado(UsuarioVO usuario);
}