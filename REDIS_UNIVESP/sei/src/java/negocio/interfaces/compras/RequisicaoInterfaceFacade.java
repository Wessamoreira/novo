package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.processosel.QuestionarioVO;


public interface RequisicaoInterfaceFacade {	

	public void excluir(RequisicaoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterarSituacao(Integer requisicao, String situacao) throws Exception;	

	public String consultarSituacaoAutorizacaoPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public RequisicaoVO consultarPorChavePrimaria(Integer codigo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RequisicaoVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<RequisicaoVO> consultarPorNomeUsuario(String valorConsulta, Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<RequisicaoVO> consultarPorNomeDepartamento(String valorConsulta, Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void verificarPermissaoAutorizarIndeferir(UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void verificarBloqueioRequisicao(Integer tipoBloqueio, final boolean bloqueio, RequisicaoVO requisicaoVO) throws Exception;

	public void alterarSituacaoEntrega(Integer codigo) throws Exception;

	Boolean getPermiteAlterarCategoriaDespesa(UsuarioVO usuario);

	Boolean getPermiteAlterarRequisitante(UsuarioVO usuario);

	Boolean getPermiteCadastrarRequisicaoTodasUnidadesEnsino(UsuarioVO usuario);

	void consultar(String codigo, String responsavel, String departamento, String solicitante, String categoria, String produtoCategoria, String autorizacao, String entrega, Integer unidadeEnsino, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void preencherDadosPorCategoriaDespesa(RequisicaoVO obj, UsuarioVO usuario) throws Exception;

	List<RequisicaoVO> consultarPorCategoriaProdutoPorUnidadeEnsinoPorTipoAutorizacaoRequisicaoComSituacaoAutorizadaComSituacaoEntreguePendente(Integer categoriaProduto, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	RequisicaoVO consultarRapidaPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void persistir(RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void atualizarSituacaoRequisicaoPorOperacaoEmCotacao(CotacaoVO obj, UsuarioVO usuario) throws Exception;	
	
	public String consultarSituacaoEntregaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RequisicaoVO> consultarPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;

	public void validarDadosBaixaPlanoOrcamentario(RequisicaoVO obj, UsuarioVO usuario, DepartamentoVO departamentoVO, boolean validarQuantidadeSolicitada) throws Exception;

	public void persistir(RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, boolean movimentarPlanoOrcamentario);

	public Double validarDadosEstornoBaixaPlanoOrcamentario(RequisicaoVO obj, UsuarioVO usuario, DepartamentoVO departamentoVO, Double valorTotal) throws Exception;

	Double consultarValorConsumidoPlanoOrcamentario(Integer planoOrcamentario, Integer solicitacaoPlanoOrcamentario,
			Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano,
			UsuarioVO usuarioVO) throws Exception;

	StringBuilder getSqlBaseValorConsumidoPlanoOrcamentario(Integer planoOrcamentario,
			Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa,
			MesAnoEnum mesAno, String ano);

	QuestionarioRespostaOrigemVO realizarCriacaoQuestionarioRespostaOrigem(RequisicaoVO requisicaoVO,
			QuestionarioVO questionarioVO, UsuarioVO usuarioVO) throws Exception;

	List<RequisicaoVO> consultarRequisicaoRespostaQuestionarioFechamento(UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;

	void alterarQuestionarioAberturaRequisicao(RequisicaoVO obj, UsuarioVO usuarioVO) throws Exception;

	void alterarQuestionarioFechamentoRequisicao(RequisicaoVO obj, UsuarioVO usuarioVO) throws Exception;

	void validarDados(RequisicaoVO obj, UsuarioVO usuario);

	void persistirComQuestionarioAbertura(RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO,
			boolean movimentarPlanoOrcamentario) throws Exception;
			
	StringBuilder getSqlBaseValorConsumidoPlanoOrcamentario(Integer planoOrcamentario,
			Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa,
			MesAnoEnum mesAno, String ano, Integer itemsolicitacaoorcamentoplanoorcamentario);
	
	Double consultarValorConsumidoPlanoOrcamentario(Integer planoOrcamentario, Integer solicitacaoPlanoOrcamentario,
			Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano,
			UsuarioVO usuarioVO, Integer itemsolicitacaoorcamentoplanoorcamentario) throws Exception;
	
	
	void incluirQuestionarioRespostaAlterandoQuestionarioRespostaOrigemFechamentoRequisicao(final RequisicaoVO requisicaoVO, UsuarioVO usuarioVO) throws Exception;
}
