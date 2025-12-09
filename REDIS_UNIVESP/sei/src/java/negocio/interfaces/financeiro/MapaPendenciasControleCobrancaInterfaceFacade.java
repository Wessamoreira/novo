package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaVO;

public interface MapaPendenciasControleCobrancaInterfaceFacade {

    public abstract MapaPendenciasControleCobrancaVO novo() throws Exception;

    public abstract void validarDados(MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public abstract void incluir(final MapaPendenciasControleCobrancaVO obj, UsuarioVO usuario) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public abstract void alterar(final MapaPendenciasControleCobrancaVO obj, UsuarioVO usuario) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public abstract void excluir(MapaPendenciasControleCobrancaVO obj, UsuarioVO usuario) throws Exception;

    public abstract List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public abstract List consultarPorContaReceber(Integer codigoContaReceber, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public abstract List consultarPorCodigo(Integer codigoMapa, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public abstract MapaPendenciasControleCobrancaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public abstract void setIdEntidade(String idEntidade);

    public List<MapaPendenciasControleCobrancaVO> consultarPorListaContaReceber(List<ContaReceberVO> contaReceberVOs, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

    public void executarAplicacaoDescontosContaReceber(MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO, List<ContaReceberVO> contaReceberVOs, PlanoDescontoVO planoDescontoVO, DescontoProgressivoVO descontoProgressivoVO, UsuarioVO usuarioVO) throws Exception;

    public List<MapaPendenciasControleCobrancaVO> consultarTodasPendencias(String matricula, String nome, Boolean todoPeriodo, Date dataInicio, Date dataFim, String ano, String semestre, String campoConsultaPeriodo, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void excluirPorContaReceber(ContaReceberVO obj, UsuarioVO usuario) throws Exception;

    public List<MapaPendenciasControleCobrancaVO> consultarPorContaReceberRegistroArquivoSelecionado(Integer registroArquivo, Integer qtde, Integer inicio, Boolean selecionado, UsuarioVO usuario) throws Exception;

    public Integer consultarQtdeMapaPendenciaPorRegistroArquivo(Integer registroArquivo, UsuarioVO usuario) throws Exception;

    public void alterarSelecionado(Integer codigo, Boolean selecionado, UsuarioVO usuario) throws Exception;

    public void alterarSelecionadoPorRegistroArquivo(Integer registroArquivo, Boolean selecionado, UsuarioVO usuario) throws Exception;
    
    public void excluirPorMatriculaPeriodoContasNaoPagasENaoNegociadas(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;
}
