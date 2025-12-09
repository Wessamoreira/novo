package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroAlunoVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroVO;

public interface ProcessamentoArquivoRetornoParceiroInterfaceFacade {

	void persistir(ProcessamentoArquivoRetornoParceiroVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ProcessamentoArquivoRetornoParceiroVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ProcessamentoArquivoRetornoParceiroVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarLeituraArquivoExcel(FileUploadEvent upload, ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuarioLogado) throws Exception;

	List<ProcessamentoArquivoRetornoParceiroVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ProcessamentoArquivoRetornoParceiroVO> consultaRapidaPorDataGeracao(Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void realizarRegistroInicioProcessamento(Integer processamentoArquivo, Integer qtdeLote, Date dataInicioProcessamento, UsuarioVO usuario) throws Exception;

	void realizarRegistroLoteEmProcessamento(Integer processamentoArquivo, Integer loteAtual, UsuarioVO usuario) throws Exception;

	void baixarContasRegistroArquivo(ProcessamentoArquivoRetornoParceiroVO processamento, List<ProcessamentoArquivoRetornoParceiroAlunoVO> contaReceberArquivoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void realizarRegistroTerminoProcessamento(Integer processamentoArquivo, Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception;

	void realizarRegistroErroProcessamento(Integer processamentoArquivo, String erro, Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoDadosProcessamento(ProcessamentoArquivoRetornoParceiroVO obj) throws Exception;	

	void preencherNegociacaoPagamentoVO(ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuario) throws Exception;	

}
