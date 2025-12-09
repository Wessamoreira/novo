package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface AtividadeExtraClasseProfessorInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public List<AtividadeExtraClasseProfessorVO> consultarAtividadeExtraClassePorFuncionarioCargo(Integer codigoFuncinoarioCargo, Date dataInicio, Date dataFim) throws Exception;

	public void persistirTodos(List<AtividadeExtraClasseProfessorVO> listaHoraAtividadeExtraClasseProfessor, FuncionarioCargoVO funcionarioCargo, boolean b, UsuarioVO usuarioLogado) throws Exception;

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public List<AtividadeExtraClasseProfessorVO> validarDadosPeriodo(Date dataInicio, Date dataFinal, Integer horasPrevistas,
			List<AtividadeExtraClasseProfessorVO> listaHoraAtividadeExtraClasseProfessor) throws Exception;

	public AtividadeExtraClasseProfessorVO consultarPorMesAnoDataAtividade(Integer codigoFuncionarioCargo, Date dataAtividade) throws Exception;

	public void alterarHoraPrevista(Integer codigo, Integer horaPrevista) throws Exception;

	public void atualizarValorHoraPrevista(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor);

	void excluirTodos(List<AtividadeExtraClasseProfessorVO> atividadeExtraClasseProfessorVOs, boolean validarAcesso,
			UsuarioVO usuarioVO) throws Exception;
}
