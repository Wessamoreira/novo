package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoValeTransporteFuncionarioCargoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface EventoValeTransporteFuncionarioCargoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception;

	public void persistirTodos(List<EventoValeTransporteFuncionarioCargoVO> listaEventoValeTransporteFuncionario, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public List<EventoValeTransporteFuncionarioCargoVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	public void adicionarEventosDeValeTransporte(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo);

	public void alterarDiasValeTransporte(Integer quantidadeDiasUteis, Integer quantidadeDiasUteisMeioExpediente, UsuarioVO usuario);

	public void atualizarValeTransportePorQuantidadeDias(Integer quantidadeDiasUteisDe, Integer quantidadeDiasUteisPara,
			Integer quantidadeDiasUteisMeioExpedienteDe, Integer quantidadeDiasUteisMeioExpedientePara, UsuarioVO usuarioVO) throws Exception;
	
}