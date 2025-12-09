package negocio.comuns.utilitarias.boleto.arquivos.bancos;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.ControleMovimentacaoRemessaVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.facade.jdbc.financeiro.ControleMovimentacaoRemessa;

/**
 * Interface padrão para os layouts de todos os bancos
 * @author Diego
 *
 */
public interface LayoutBancos {

    public void validarDadosRemessa();

    public void validarDadosRetorno();
    
    public void processarArquivoProgressBarVO(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO,  UsuarioVO usuarioVO) throws Exception;

    public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public List<ContaReceberVO> listarContaReceberVOs(RegistroArquivoVO registroArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public void criarNegociacaoRecebimentoVOs(List<ContaReceberVO> contaReceberVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel,ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception;

    public void estornarConta(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public void criarNegociacaoRecebimentoVOsBaixandoContas(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleCobrancaVO controleCobrancaVO, UsuarioVO usuario) throws Exception;    
    
    
}
