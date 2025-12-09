package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;


public interface ControleCobrancaPagarInterfaceFacade {

	List<ControleCobrancaPagarVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ControleCobrancaPagarVO> consultaRapidaPorDataProcessamento(Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	ControleCobrancaPagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void excluir(ControleCobrancaPagarVO obj, UsuarioVO usuarioVO) throws Exception;

	void processarArquivo(ControleCobrancaPagarVO controleCobrancaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	void persistir(ControleCobrancaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void realizarAtualizacaoDadosProcessamento(ControleCobrancaPagarVO controleCobrancaVO) throws Exception;

	void realizarRegistroInicioProcessamento(ControleCobrancaPagarVO controleCobrancaVO, UsuarioVO usuario) throws Exception;

	void realizarRegistroTerminoProcessamento(Integer controleCobranca, Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception;

	public void baixarContasPagarArquivoRetorno(ControleCobrancaPagarVO obj, List<ContaPagarRegistroArquivoVO> listaContaPagarRegistroDetalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	void realizarRegistroLoteEmProcessamento(Integer controleCobranca, Integer loteAtual, UsuarioVO usuario) throws Exception;

	void realizarRegistroErroProcessamento(Integer controleCobranca, String erro, Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoContaCorrente(Integer controleCobranca, Integer contaCorrente, UsuarioVO usuario) throws Exception;

}
