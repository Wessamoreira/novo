package negocio.interfaces.financeiro;

import java.io.IOException;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public interface CartaoCreditoDebitoRecorrenciaPessoaInterfaceFacade {

	void incluir(CartaoCreditoDebitoRecorrenciaPessoaVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarCriacaoRecorrenciaCartaoCreditoDebito(ContaReceberVO contaReceberVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws IOException, Exception;

	List<CartaoCreditoDebitoRecorrenciaPessoaVO> consultarContasRecorrenciaAptasPagamento();

	List<CartaoCreditoDebitoRecorrenciaPessoaVO> consultarContasRecorrenciaPorMatricula(String matricula, Boolean manterCriptografiaCartao, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoPorCodigo(Integer codigo, SituacaoEnum situacao, UsuarioVO usuario) throws Exception;

	void adicionarCartaoCreditoDebitoRecorrenciaPessoa(CartaoCreditoDebitoRecorrenciaPessoaVO obj, List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs, UsuarioVO usuarioVO) throws Exception;

	void removerCartaoCreditoDebitoRecorrenciaPessoa(CartaoCreditoDebitoRecorrenciaPessoaVO obj, List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs);

	void inicializarDadosAlunoCartaoCreditoDebito(CartaoCreditoDebitoRecorrenciaPessoaVO obj, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

	CartaoCreditoDebitoRecorrenciaPessoaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;

	String getCaminhoChavePublica() throws Exception;

	String getCaminhoChavePrivada() throws Exception;

	CartaoCreditoDebitoRecorrenciaPessoaVO consultarPorMatriculaPrimeiroCartaoCadastrado(String matricula, UsuarioVO usuarioVO) throws Exception;

	List<CartaoCreditoDebitoRecorrenciaPessoaVO> consultarPorFiltrosMapaPendenciaCartaoCreditoRecorrenciaDCC(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<OperadoraCartaoVO> operadoraCartaoVOs, String matricula, PessoaVO responsavelFinanceiro, TipoPessoa tipoPessoa, String nomeCartao, Integer mesValidade, String anoValidade, FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente, Integer diaPadraoPagamento, String numeroFinalCartaoCredito, UsuarioVO usuarioVO);	
}
