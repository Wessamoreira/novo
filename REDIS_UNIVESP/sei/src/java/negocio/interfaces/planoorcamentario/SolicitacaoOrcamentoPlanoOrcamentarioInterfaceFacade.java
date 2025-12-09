package negocio.interfaces.planoorcamentario;

import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.enumeradores.SituacaoPlanoOrcamentarioEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */

public interface SolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade {

	public SolicitacaoOrcamentoPlanoOrcamentarioVO novo() throws Exception;

	public void incluir(final SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorPessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public SolicitacaoOrcamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorPlanoOrcamentario(Integer planoOrcamentario, List<UnidadesPlanoOrcamentarioVO> unidadesPlanoOrcamentarioVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorFuncionario(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorSituacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario ,boolean permiteConsultarTodos) throws Exception;

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorNomePlanoOrcamentario(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception;

    public void carregarDados(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public SolicitacaoOrcamentoPlanoOrcamentarioVO consultarDados(RequisicaoVO requisicaoVO, DepartamentoVO departamentoVO, UnidadeEnsinoVO unidadeEnsinoVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Double consultarTotalRestantePlanoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO);

	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception ;

	public void alterarSituacao(int codigo, String situacao, UsuarioVO usuarioVO);
	
	public void alterarSituacaoValorAprovado(int codigo, String situacao, double valorAprovado, UsuarioVO usuarioVO);
	
	public List<CategoriaDespesaVO> consultarCategoriaDespesaPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario);

	public List<CategoriaProdutoVO> consultarCategoriaProdutoPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario);

	public List<CentroResultadoVO> consultarCentroResultadoPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario);

	void adicionarObjItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, boolean remanejamento) throws Exception;

	void excluirObjItensProvisaoVOs(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO,
			Integer codCategoriaDespesa) throws Exception;

	void realizarCriacaoDetalhamentoPeriodoGeral(
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) throws Exception;

	void enviarEmailResponsavelDepartamento(DepartamentoVO departamento, UnidadeEnsinoVO unidadeEnsinoVO,
			Boolean enviarComunicadoPorEmail, String mensagemPadraoNotificacao, UsuarioVO usuarioLogado,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void enviarEmailResponsavelDepartamento(DepartamentoVO departamento, UnidadeEnsinoVO unidadeEnsinoVO,
			Boolean enviarComunicadoPorEmail, String mensagemPadraoNotificacao, UsuarioVO usuarioLogado,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Integer codigoTipoOrigemComunicacaoInterna, PessoaVO remetente, PessoaVO destinatario)
			throws Exception;

	void adicionarDestinatarioResponsavelDepartamento(ComunicacaoInternaVO comunicacaoInternaVO, PessoaVO responsavel)
			throws Exception;

	void alterarValorAprovado(PlanoOrcamentarioVO planoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception;

	void realizarCalculoValorAprovadoPorCategoriaDespesa(
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) throws Exception;

	public void alterarSolicitacaoOrcamentoPlanoOrcamentarioPorSituacao(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, 
			SituacaoPlanoOrcamentarioEnum situacaoPlanoOrcamentarioEnum, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarSituacaoSolicitacaoPlanoOrcamentario(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO,
			String situacao, UsuarioVO usuarioVO) throws Exception;

	public void realizarCalculoValorAprovadoPorDetalhamentoPeriodo(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) throws Exception;
	
	public void alterarAguardandoAprovacao(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, String situacao,  UsuarioVO usuarioVO) throws Exception;

}
