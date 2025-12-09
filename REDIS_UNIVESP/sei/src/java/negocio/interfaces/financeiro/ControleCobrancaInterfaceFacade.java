package negocio.interfaces.financeiro;

import java.io.File;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ControleCobrancaInterfaceFacade {

    public ControleCobrancaVO novo() throws Exception;

    public void incluir(ControleCobrancaVO obj, List<ContaReceberVO> contaReceberVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception;

    public void alterar(ControleCobrancaVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(ControleCobrancaVO obj, UsuarioVO usuarioVO) throws Exception;

    public ControleCobrancaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public ControleCobrancaVO consultarBasicaPorCodigo(Integer codigoPrm, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoControle(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
    
    public void processarArquivoProgressBarVO(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO,  UsuarioVO usuarioVO) throws Exception;

    public List<ContaReceberVO> criarContaReceberVOs(RegistroArquivoVO registroArquivo, ControleCobrancaVO controleCobrancaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    public void estornarConta(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public void incluirSemBaixarContas(final ControleCobrancaVO obj, final RegistroArquivoVO arquivo, UsuarioVO usuario) throws Exception;
    
    public void alterarSemBaixarContas(final ControleCobrancaVO obj, final RegistroArquivoVO arquivo, UsuarioVO usuario) throws Exception;

    public ControleCobrancaVO consultarPorNomeArquivo(String nomeArquivo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ControleCobrancaVO consultarPorNomeArquivoAnoProcessamento(String nomeArquivo, String anoProcessamento, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ControleCobrancaVO consultarPorChavePrimariaCompleto(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void baixarContas(ControleCobrancaVO obj, List<ContaReceberVO> contaReceberVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception;

    public void baixarContasRegistroArquivo(ControleCobrancaVO obj, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<ControleCobrancaVO> consultaRapidaPorCodigo(Boolean movimentacaoRemessaRetorno, Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ControleCobrancaVO> consultaRapidaPorTipoControle(Boolean movimentacaoRemessaRetorno, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ControleCobrancaVO> consultaRapidaPorDataProcessamento(Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ControleCobrancaVO> consultaRapidaPorNomeArquivo(Boolean movimentacaoRemessaRetorno, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public ControleCobrancaVO consultarPorNomeArquivoAnoProcessamentoUnidadeEnsino(String nomeArquivo, String anoProcessamento, int unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirControleRemessaContaReceber(ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception;

	void realizarRegistroLoteEmProcessamento(Integer controleCobranca, Integer loteAtual, UsuarioVO usuario) throws Exception;
	
	void realizarRegistroErroProcessamento(Integer controleCobranca, SituacaoProcessamentoArquivoRetornoEnum situacao, String erro, Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception;

	void realizarRegistroTerminoProcessamento(Integer controleCobranca, Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception;

	void realizarRegistroProcessamentoInterrompido() throws Exception;

	void realizarRegistroInicioProcessamento(Integer controleCobranca, Integer qtdeLote, Date dataInicioProcessamento, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoDadosProcessamento(ControleCobrancaVO controleCobrancaVO) throws Exception;

	List<ControleCobrancaVO> consultaRapidaPorNossoNumero(String nossonumero, Boolean movimentacaoRemessaRetorno, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ControleCobrancaVO> consultaRapidaPorMatricula(String matricula, Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal,  Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ControleCobrancaVO> consultaRapidaPorSacado(String nomeSacado, Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal,  Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<ControleCobrancaVO> consultaRapidaPorContaCorrenteDataProcessamento(Integer contaCorrente, Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public File realizarGeracaoRelatorioExcel(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoDuplicidadeVOs, String urlLogoPadraoRelatorio) throws Exception;
	
	public void alterarContaCorrenteControleCobranca(final Integer controleCobranca, final Integer codigoContaCorrente, UsuarioVO usuario) throws Exception;
	
}
