package negocio.interfaces.planoorcamentario;

import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;

/**
 *
 * @author Carlos
 */
public interface DetalhamentoPlanoOrcamentarioInterfaceFacade {
    public void incluir(final DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,  int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public DetalhamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void incluirDetalhamentoPlanoOrcamentario(Integer planoOrcamentarioPrm, List<DetalhamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception;
    public void alterarDetalhamentoPlanoOrcamentario(Integer planoOrcamentario, List objetos, UsuarioVO usuario) throws Exception;
    public void excluirDetalhamentoPlanoOrcamentario(Integer planoOrcamentario, UsuarioVO usuario) throws Exception;
    public List consultarDetalhamentoPorPlanoOrcamentario(Integer planoOrcamentario,  int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void validarDadosValorOrcamentoTotal(Double valorOrcamentoTotal) throws Exception;    
    public Double consultarValorOcamentoTotalDetapartamentoPorDataDepartamentoUnidadeEnsino(Date data, Integer departamento, Integer unidadeEnsino);
    public void atualizarValoConsumidoDetalhamentoPlanoOrcamentario(Double valorAtualizar, Integer departamento, Integer unidadeEnsino, Date dataAutorizacao, UsuarioVO usuario) throws Exception;
    public DetalhamentoPlanoOrcamentarioVO consultarDetalhamentoPlanoOrcamentarioPorPlanoOrcamentarioDepartamentoUnidadeEnsino(Integer planoOrcamentario, Integer departamento, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Boolean consultarExistenciaPorPlanoOrcamentario(Integer planoOrcamentario);
    public DetalhamentoPlanoOrcamentarioVO consultarPorPlanoOrcamentarioDepartamentoUnidadeEnsino(Integer departamento, Integer unidadeensino, Date dataAutorizacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<DetalhamentoPlanoOrcamentarioVO> consultarDetalhamentoPorSolicitacaoOrcamentoPlanoOrcamentario(Integer codigoSolicitacaoOrcamentoPlanoOrcamentario, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
	public void atualizarEstornoValoConsumidoDetalhamentoPlanoOrcamentario(Double valorTotal, Integer codigo, Integer codigo2, Date dataAutorizacao, UsuarioVO usuario) throws Exception;
	public List<DepartamentoVO> consultarDepartantoPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario);
}
