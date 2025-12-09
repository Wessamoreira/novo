package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO;

public interface LogExecucaoJobCartaoCreditoDebitoRecorrenciaInterfaceFacade {

	public void incluir(final LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO obj, final UsuarioVO usuarioVO) throws Exception;

	public LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO novo() throws Exception;

	LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO inicializarDadosLogExecucaoJobCartaoCredito(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO, ContaReceberVO contaReceberVO, Boolean erro, String observacao, Boolean execucaoManual, UsuarioVO usuarioVO);
	

}
