package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoSituacaoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public HistoricoSituacaoVO montarDadosPorAfastamentoFuncionario(AfastamentoFuncionarioVO afastamentoFuncionarioVO,FuncionarioCargoVO funcionarioCargo);

	public HistoricoSituacaoVO montarDadosPorSituacaoFuncionario(FuncionarioCargoVO funcionarioCargo, String situacao);

	public HistoricoSituacaoVO montarDadosSituacaoDemitido(FuncionarioCargoVO funcionarioCargoVO, Date dataDemissao);

	public List<HistoricoSituacaoVO> consultaHistoricoSituacao(Date dataInicial, Date datafinal, FuncionarioCargoVO funcionarioCargoVO, String situacao) throws Exception;

	public void persistirPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso) throws Exception;
}