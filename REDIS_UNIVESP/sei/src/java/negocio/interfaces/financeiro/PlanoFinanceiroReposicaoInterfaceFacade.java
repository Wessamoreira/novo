package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.PlanoFinanceiroReposicaoVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Carlos
 */
public interface PlanoFinanceiroReposicaoInterfaceFacade {

    public void incluir(final PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception;
    public void carregarDados(PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception;
    public void carregarDados(PlanoFinanceiroReposicaoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void realizarAtivacaoCondicaoPagamento(PlanoFinanceiroReposicaoVO planoFinaceiroReposicaoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception;
    public void realizarInativacaoCondicaoPagamento(PlanoFinanceiroReposicaoVO planoFinaceiroReposicaoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception;
    public List<PlanoFinanceiroReposicaoVO> consultaRapidaPorDescricao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<PlanoFinanceiroReposicaoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<PlanoFinanceiroReposicaoVO> consultaRapidaPorSituacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;


}
