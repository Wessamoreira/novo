package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.RegistroDetalhePagarVO;
import negocio.comuns.financeiro.RegistroHeaderLotePagarVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;

public interface ControleRemessaContaPagarInterfaceFacade {

	public ControleRemessaContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

	public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluir(ControleRemessaContaPagarVO obj, UsuarioVO usuario) throws Exception;

	public void incluir(final ControleRemessaContaPagarVO obj, UsuarioVO usuario) throws Exception;

    public void executarGeracaoArquivoRemessaContaPagar(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, Boolean agruparContasMesmoFornecedor ,  ControleRemessaContaPagarVO controleRemessaContaPagarVO, String caminhoBaseArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, ContaPagarVO contaPagarVO) throws Exception;
    
    public List consultarPorContaCorrente(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void realizarEstorno(ControleRemessaContaPagarVO controleRemessaVO, UsuarioVO usuarioVO) throws Exception;

    public List consultarPorNossoNumero(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorNomeSacado(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
   
	void addContaPagarControleRemessaContaPagarVO(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaSemBancoVOs, List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaOutroBancoVOs, ContaPagarControleRemessaContaPagarVO obj);
    
	

	public void realizarAgrupamentoContasPagar(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);
	public void realizarDesagruparContasPagar(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);
	public void realizarRemoverAgrupamentoContasPagar(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO ,List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);
	public List<ContaPagarControleRemessaContaPagarVO> realizarVizualizarContasMesmoAgrupador(String codigoAgrupador ,List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);	
	public void realizarAdicionarAgrupamentoContasPagar(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO ,List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);

	public Map<String, List<ContaPagarControleRemessaContaPagarVO>> realizarSepararContaPagarAgrupadaNaoAgrupada(
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);

	public Map<String, List<ContaPagarControleRemessaContaPagarVO>> realizarSepararListaContaPagarAgrupadas(
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs);

	public void executarGeracaoContaPagarRegistrosArquivoAgrupados(String linha, ControleCobrancaPagarVO obj,	RegistroDetalhePagarVO registroDetalhe,RegistroHeaderLotePagarVO registroHeaderLote , UsuarioVO usuarioVO) throws Exception;

	public void realizarValidarExistenciaContaComFormaPagamentoBoletoCodigoBarrasIgualSemAgrupador(List<ContaPagarControleRemessaContaPagarVO> listaRemessa) throws Exception;
	public String realizarMontarCodigoTransmissaoRemessaContaPagar(int qtd);

}