package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.ExtratoContaCorrenteRelVO;


public interface ExtratoContaCorrenteRelInterfaceFacade {

    List<ExtratoContaCorrenteRelVO> consultarDadosGeracaoRelatorio(Integer unidadeEnsino, Integer contaCorrente, Date dataInicio, Date dataTermino, Boolean listaTela) throws Exception;

	void alterarTodosExtratoContaCorrente(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO, UsuarioVO usuarioVO) throws Exception;

	void removerExtratoContaCorrente(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs, ExtratoContaCorrenteVO extratoContaCorrente);

	boolean removerExtratoContaCorrente(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO, ExtratoContaCorrenteVO extratoContaCorrente);

	void adicionarExtratoContaCorrente(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs, ExtratoContaCorrenteVO extratoContaCorrente);

	void validarDados(Date dataInicio, Date dataTermino, Integer contaCorrente, boolean consultaTela) throws ConsistirException;

	void realizarCalculoSaldoDiaDia(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs);

	void realizarCalculoSaldoDiaDia(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO);
	
	Double consultarSaldoAnterior(Integer contaCorrente, Date dataInicio);
}
