package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.EstatisticasLancamentosFuturosVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MapaLancamentoFuturoInterfaceFacade {

    public MapaLancamentoFuturoVO novo() throws Exception;

    public void incluir(MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception;

    public void baixarCheque(List<MapaLancamentoFuturoVO> lista, UsuarioVO usuario) throws Exception;

    public void alterar(MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception;

    public MapaLancamentoFuturoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTodosParametros(MapaLancamentoFuturoVO mapaLancamentoFuturoVO, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Double consultarPorTodosParametrosValorTotal(MapaLancamentoFuturoVO mapaLancamentoFuturoVO, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List listarTodasPendencias(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void baixarPendenciaAtravezDeNegociacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception;
    
    public void baixarPendenciaAtravezDeRenegociacaoRecebimento(NegociacaoContaReceberVO negociacaoContaReceberVO, ContaReceberNegociadoVO contaReceberNegociadoVO, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<MapaLancamentoFuturoVO> consultarPorCodigoChequeETipoMapa(int codigoCheque, String tipoMapa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public MapaLancamentoFuturoVO consultarPorMatriculaOrigem(String matriculaOrigem, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirPorCodigoCheque(Integer cheque, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;
    
    public void reapresentarCheque(final MapaLancamentoFuturoVO obj, Date dataReapresentacao, UsuarioVO usuarioVO) throws Exception;
    
    public void reDevolverCheque(final Integer codigoCheque, final Integer codigoOrigem) throws Exception;
    
    public void excluirPorCodigoOrigem(Integer codigoOrigem, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;
    
    public void excluirPorCodigoChequeTipoOrigemETipoMapalancamentoFuturo(Integer codigoCheque, String tipoOrigem, String tipoMapaLancamentoFuturo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public void alterarDataBaixa(final MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception;

	List<EstatisticasLancamentosFuturosVO> consultarEstatisticasLancamentosFuturosVO(UnidadeEnsinoVO unidadeEnsinoVO,
			UsuarioVO usuario) throws Exception;
}
