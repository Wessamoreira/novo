package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface AfastamentoFuncionarioInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public List<AfastamentoFuncionarioVO> consultarAfastamentoPorCodigoFuncionarioCargo(Integer codigoFuncionarioCargo) throws Exception;

	public void persistirTodos(List<AfastamentoFuncionarioVO> listaAfastamentosFuncionario, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluirPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluirPorFuncionarioCargo(List<AfastamentoFuncionarioVO> listaAfastamentosFuncionario,
			FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public String validarSituacaoFuncionarioPorTipoAfastamento(String tipoAfastamento);

	public SqlRowSet consultarQuantidadeDeDiasAfastados(FuncionarioCargoVO funcionarioCargoVO, Integer anoCompetencia, Integer mesCompetencia);
	
	public List<AfastamentoFuncionarioVO> consultarUltimpoAfastamentoPorFuncionarioEDataComparacao(FuncionarioCargoVO funcionarioCargoVO, Date dataComparacao) throws Exception ;

	public List<AfastamentoFuncionarioVO> consultarAfastamentoPorFuncionarioEDataComparacaoEMotivoAfastamento(FuncionarioCargoVO funcionarioCargoVO, Date dataComparacao, String motivo) throws Exception; 
}