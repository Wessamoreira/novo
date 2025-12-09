package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoSalarialVO;
import negocio.comuns.recursoshumanos.SalarioCompostoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface SalarioCompostoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception;

	public void persistirTodos(List<SalarioCompostoVO> listaSalarioComposto, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public List<SalarioCompostoVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	public void adicionarEventosDoFuncionarioNoContraCheque(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo);
	
	/**
	 * Realiza a soma dos valores da propriedade valorMensal da lista de salario composto passada como parametro
	 * @param listaSalarioComposto
	 * @return
	 */
	public BigDecimal realizarSomaDoValorMensalDoSalarioComposto(List<SalarioCompostoVO> listaSalarioComposto);

	/**
	 * Realiza a soma dos valores da propriedade jornada da lista de salario composto passada como parametro
	 * @param listaSalarioComposto
	 * @return
	 */
	public Integer realizarSomaDasJornadasDoSalarioComposto(List<SalarioCompostoVO> listaSalarioComposto);

	public void persistir(List<SalarioCompostoVO> listaSalarioComposto, HistoricoSalarialVO historicoSalarialVO, FuncionarioCargoVO funcionarioCargoVO,
			boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
}