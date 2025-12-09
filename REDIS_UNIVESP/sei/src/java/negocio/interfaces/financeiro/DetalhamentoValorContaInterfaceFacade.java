package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.DetalhamentoValorContaVO;
import negocio.comuns.financeiro.enumerador.OrigemDetalhamentoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;

public interface DetalhamentoValorContaInterfaceFacade {

	void persistir(List<DetalhamentoValorContaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	DetalhamentoValorContaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	List<DetalhamentoValorContaVO> consultaRapidaPorOrigemCodigoOrigemConta(OrigemDetalhamentoContaEnum origemDetalhamentoContaEnum, Integer codigoConta, int nivelMontarDados, UsuarioVO usuario);

	void validarGeracaoCentroResultadoOrigemDetalhePadrao(ContaReceberVO obj, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception;

	void realizarProcessamentoDeAtualizacaoDetalhamentoValorContaReceber(ContaReceberVO contaReceberVO, Date dataBase, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception;

	void atualizarCentroResultadoOrigemDetalhe(DetalhamentoValorContaVO obj, UsuarioVO usuario) throws Exception;

	void excluirPorTipoCentroResultadoOrigemDetalhePorOrigemDetalhamentoConta(Integer codigoContaReceber, TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalhe, OrigemDetalhamentoContaEnum origemDetalhamentoConta, UsuarioVO usuario) throws Exception;
	
	public Double somarDescontoIncondicionalContaReceber(Integer idContaReceber) throws Exception;

}
