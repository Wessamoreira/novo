package negocio.interfaces.financeiro;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.TotalizadorPorFormaPagamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.ParametrizarOperacoesAutomaticasConciliacaoItemVO;

public interface ConciliacaoContaCorrenteInterfaceFacade {

	List<ConciliacaoContaCorrenteDiaExtratoVO> validarUnicidadeParaConciliacaoContaCorrenteExtratoDia(ConciliacaoContaCorrenteVO obj);
	
	void realizarCorrecaoParaRegistroDuplicadosConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception;
	
	void uploadArquivo(ConciliacaoContaCorrenteVO obj, FileUploadEvent upload, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuarioLogado) throws Exception;	

	public void realizarLinkConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei, UsuarioVO usuario) throws Exception; 

	public void realizarRemocaoLinkConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, UsuarioVO usuarioVO) throws Exception;

	public void realizarCalculoTotalizadoresDias(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario);	

	void realizarCriacaoDeNovoDocumentosParaConciliacaoAutomatica(ConciliacaoContaCorrenteVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception;

	void persistir(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception;

	List<ConciliacaoContaCorrenteVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ConciliacaoContaCorrenteVO> consultaRapidaPorDataGeracao(Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ConciliacaoContaCorrenteVO> consultaRapidaPorResponsavel(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ConciliacaoContaCorrenteVO> consultaRapidaPorContaCorrente(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	ConciliacaoContaCorrenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	

	void gerarNegociacaoContaPagarPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

	void gerarNegociacaoContaReceberPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void excluirConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, UsuarioVO usuario);
	
	void realizarDesagrupamentoConciliacaoContaCorrenteDiaExtratoConjunta(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, UsuarioVO usuario);
	
	void realizarDesagrupamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, ExtratoContaCorrenteVO extratoContaCorrente, UsuarioVO usuario) throws Exception;
	
    void realizarDesagrupamentoPorFormaPagamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, TotalizadorPorFormaPagamentoVO totalizadorPorFormaPagamentoVO, UsuarioVO usuario) throws Exception;
    
    void realizarDesagrupamentoPorValorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, UsuarioVO usuario) throws Exception;
    
    void realizarDesagrupamentoPorValorAproximadoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, String listaCodigoExtrato, UsuarioVO usuario) throws Exception;

	void realizarAgrupamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoDrag, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoDrop, UsuarioVO usuario) throws Exception;

	void finalizarConciliacao(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void abrirConciliacao(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void gravarContaPagarPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

	void gravarContaReceberPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void removeParametrizarOperacoesAutomaticasConciliacao(ConciliacaoContaCorrenteVO obj, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception;

	void addParametrizarOperacoesAutomaticasConciliacao(ConciliacaoContaCorrenteVO obj, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception;

	void gravarMovimentacaoFinanceiraPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, MovimentacaoFinanceiraVO mf, FormaPagamentoVO formaPagamento, UsuarioVO usuario) throws Exception;

	Integer validarSeExisteConciliacaoContaCorrenteParaEstorno(Integer codigoConciliacaoContaCorrenteExtratoDia, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception;

	File realizarValidacaoArquivoRetornoComExtratoContaCorrente(String nrBanco, String contaCorrenteArquivo, Date dataConciliacao, String urlLogoPadraoRelatorio,UsuarioVO usuario) throws Exception;
	
	public boolean validarConciliacaoContaCorrenteFinalizada(Date data, String contaCorrente, UsuarioVO usuario) throws Exception;

	void preencherDetalheExtratoContaCorrente(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO, UsuarioVO usuario) throws Exception;
	

}
