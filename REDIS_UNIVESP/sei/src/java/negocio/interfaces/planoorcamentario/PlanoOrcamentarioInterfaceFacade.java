package negocio.interfaces.planoorcamentario;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */

public interface PlanoOrcamentarioInterfaceFacade {

	public PlanoOrcamentarioVO novo() throws Exception;

	public void incluir(final PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterar(final PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public void incluir(final PlanoOrcamentarioVO obj, UsuarioVO usuario, boolean permitirRealizarManejamentoSaldoAprovado) throws Exception;

	public void alterar(final PlanoOrcamentarioVO obj, UsuarioVO usuario, boolean permitirRealizarManejamentoSaldoAprovado) throws Exception;

	public void excluir(PlanoOrcamentarioVO obj) throws Exception;

	public PlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
        
	public void setIdEntidade(String aIdEntidade);

    public void consultarDadosOrcamentarios(PlanoOrcamentarioVO planoOrcamentarioVO, Date dataInicio, Date dataFim) throws Exception;

    public PlanoOrcamentarioVO clonar(PlanoOrcamentarioVO obj) throws CloneNotSupportedException;
    public void validarDadosPeriodoCriacoPlanoOrcamentario(List<UnidadesPlanoOrcamentarioVO> listaUnidades, Date dataInicio, Date dataFim, Integer planoOrcamentario) throws Exception;

    public void validarDadosPreenchimentoListaOrcamentoTotalNoPeriodo(List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentoVOs) throws Exception;

    public Double executarCalculoSaldoAtualMes(RequisicaoVO requisicaoVO, Date data, Integer departamento, Integer unidadeEnsino);
    public void atualizarOrcamentoTotalRealizado(Double valorAtualizar, Date dataAutorizacao, Integer departamento, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

    public void validarDadosExistenciaPlanoOrcamentarioPorDepartamentoUnidadeEnsinoEPeriodo(DepartamentoVO departamentoVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

    public void validarDadosSaldoPlanoOrcamentario(RequisicaoVO requisicaoVO, Double valorAtualizar, DepartamentoVO departamentoVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> consultaRapidaPorSituacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void carregarDados(PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

    public List<PlanoOrcamentarioVO> executarVerificacaoUnidadesPainelGestor(List<UnidadeEnsinoVO> unidadeEnsinoGestorVOs, List<PlanoOrcamentarioVO> planoOrcamentarioVOs);

    public void finalizar(final PlanoOrcamentarioVO obj, UsuarioVO usuario, boolean permitirRealizarManejamentoSaldoAprovado) throws Exception;

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, UsuarioVO usuarioVO) throws Exception;

	public void atualizarEstornoOrcamentoTotalRealizado(Double valorTotal, Date dataAutorizacao, Integer codigo, Integer codigo2, UsuarioVO usuario) throws Exception;

	public void validarSeTodasSolicitacoesOrcamentoAprovadas(PlanoOrcamentarioVO planoOrcamentarioVO) throws Exception;

	List<PlanoOrcamentarioVO> consultarDadosPainelGestor(List<UnidadeEnsinoVO> unidadeEnsinoVOs);

}
