package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import controle.compras.CotacaoControle.EnumSituacaoTramitacao;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.EstatisticaCotacaoVO;
import negocio.comuns.compras.ItemCotacaoVO;
import negocio.comuns.compras.RequisicaoVO;

public interface CotacaoInterfaceFacade {

	

	public void incluir(CotacaoVO obj, UsuarioVO usuarioVO) throws Exception;	

	public void alterar(CotacaoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterarSituacaoCotacao(Integer codigo, String situacao, Date data, UsuarioVO responsavel, String motivoRevisao) throws Exception;

	public void excluir(CotacaoVO obj, UsuarioVO usuarioVO) throws Exception;
	
	void consultar(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, Integer requisicao, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo , boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento);

	public List consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoCotacao(Integer cotacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public CotacaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);		

	public CotacaoVO montarListaCotacao(CotacaoVO cotacaoVO, ItemCotacaoVO itemCotacaoVO, boolean atualizar, UsuarioVO usuarioLogado) throws Exception;

	public void adicionarNovaCotacaoFornecedor(CotacaoFornecedorVO obj, CotacaoVO cotacaoVO) throws CloneNotSupportedException, Exception;
	
	void excluirObjCotacaoFornecedorVOs(CotacaoVO cotacaoVO, Integer fornecedor, UsuarioVO usuarioLogado);

	CotacaoVO consultarCompletaPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception;	

	void preencherDadosPorCategoriaDespesa(CotacaoVO obj, UsuarioVO usuario);

	CotacaoVO removerItemCotacaoVO(CotacaoVO cotacaoVO, ItemCotacaoVO itemCotacaoVO, UsuarioVO usuario);

	void atualizarCssItemCotacao(CotacaoVO cotacaoVO);

	void atualizarCotacaoFornecedorEscolha(CotacaoVO cotacaoVO, ItemCotacaoVO item, UsuarioVO usuarioLogado);

	void gerarCentroResultadoOrigem(CotacaoVO obj, UsuarioVO usuarioLogado);

	void removerUnidadeEnsinoVO(CotacaoVO cotacaoVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioLogado);

	void liberarCotacaoParaMapaComTramitacao(CotacaoVO obj, UsuarioVO usuarioResponsavelDepartamento, UsuarioVO usuarioLogado) throws Exception;

	void adicionarProdutoServicoNaCotacao(List<RequisicaoVO> listaRequisicao, CotacaoVO cotacaoVO, ItemCotacaoVO itemCotacaoVO, UsuarioVO usuario) throws Exception;

	void indeferirCotacao(CotacaoVO obj, CotacaoHistoricoVO cotHistorico, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;
	
	public EstatisticaCotacaoVO consultarEstatisticaCotacoesAtualizada(UsuarioVO usuario) throws Exception;

	

	
}
