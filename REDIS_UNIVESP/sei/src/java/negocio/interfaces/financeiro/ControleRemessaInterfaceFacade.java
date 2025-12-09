package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface ControleRemessaInterfaceFacade {

	public void gerarDadosArquivoRemessa(List<ContaReceberVO> contaReceberVOs, ControleRemessaVO controleRemessaVO, String caminhoPasta,
			UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,
			UnidadeEnsinoVO unidadeEnsinoVO, Boolean arquivoTeste) throws Exception;

	public ControleRemessaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

	public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluir(ControleRemessaVO obj, UsuarioVO usuario) throws Exception;

	public void incluir(final ControleRemessaVO obj, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void executarGeracaoArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBaseArquivo, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
    
    public List consultarPorMatricula(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorNossoNumero(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorContaCorrente(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorCPFSacado(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorNomeSacado(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void realizarEstorno(ControleRemessaVO controleRemessaVO, UsuarioVO usuarioVO) throws Exception;
    
    void realizarAtualizacaoControleRemessaPorArquivoRetorno(RegistroDetalheVO registro, ControleRemessaContaReceberVO obj, HashMap<String, String> listaRejeicao, UsuarioVO usuarioVO) throws Exception;
    
    HashMap<String, String> consultarMotivoRejeicaoBanco(Integer codigoBanco, String cnab) throws Exception;
    
    public FiltroRelatorioFinanceiroVO consultarFiltrosRelatorioPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public ControleRemessaVO processarArquivo(ControleRemessaVO controleRemessaVO, List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
    
    public void incluirControleOnline(ContaReceberVO contaReceber, ControleRemessaContaReceberVO crcr, UsuarioVO usuarioVO) throws Exception;
    
    public void executarEnvioRemessaOnline(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBaseArquivo, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	public void realizarVerificacaoUltimaRemessaCriadaAtualizandoIncrementalMXPorControleRemessa(ControleRemessaVO controleRemessaVO,	UsuarioVO usuarioLogado) throws Exception;

	
}