package negocio.interfaces.recursoshumanos;

import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoAfastamentoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public HistoricoAfastamentoVO montarDadosPorAfastamentoFuncionario(AfastamentoFuncionarioVO afastamentoFuncionarioVO, FuncionarioCargoVO funcionarioCargo);

	public void processaSituacaoFuncionarioAfastado(UsuarioVO usuarioLogado) throws Exception;

	public SqlRowSet consultarAfastamentoDoFuncionarioCargo(FuncionarioCargoVO funcionarioCargo, Date dataInicio, Date dataFinal, Boolean consideraAfastamentoPrevidencia);

	public int consultarQuantidadeDeDiasDeAfastamentoDoFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, Date inicioPeriodo, Date finalPeriodo, Boolean consideraAfastamentoPrevidencia);
	
}