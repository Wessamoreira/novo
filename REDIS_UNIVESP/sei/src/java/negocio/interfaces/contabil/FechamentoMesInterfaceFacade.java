package negocio.interfaces.contabil;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;

public interface FechamentoMesInterfaceFacade {
	
    public FechamentoMesVO novo() throws Exception;
    public void incluir(FechamentoMesVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
    public void alterar(FechamentoMesVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
    public void excluir(FechamentoMesVO obj, UsuarioVO usuarioVO) throws Exception;
    public FechamentoMesVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<FechamentoMesVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<FechamentoMesVO> consultarPorNomeFantasiaUnidadeEnsino(String valorConsulta, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<FechamentoMesVO> consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<FechamentoMesVO> consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
	public List<FechamentoMesVO> consultarPorCompetencia(Integer mes, Integer ano, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public FechamentoMesVO consultarMesFechado( Integer mes, Integer ano, Integer nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public FechamentoMesVO consultarExisteItemMesAno( Integer mes, Integer ano, Integer nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public Boolean verificarMesFechado( Integer mes, Integer ano,UsuarioVO usuario) throws Exception;
    public void fecharMes(FechamentoMesVO obj) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void verificarEGerarHistoricoModificacoesFechamentoMes(FechamentoMesVO fechamentoMesAlterado, FechamentoMesVO fechamentoMesAnterior, UsuarioVO usuario) throws Exception;
    public void fecharCompetencia(FechamentoMesVO obj, Boolean gerarPrevisaoFaturamento, UsuarioVO usuario) throws Exception;
    public FechamentoMesVO verificarCompetenciaFechada(Date dataVerificar, Date dataCompVerificar, TipoOrigemHistoricoBloqueioEnum tipoOrigem, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    public FechamentoMesVO verificarCompetenciaFechada(Date dataVerificar, Date dataCompVerificar, TipoOrigemHistoricoBloqueioEnum tipoOrigem, Integer unidadeEnsino, Integer contaCaixa, UsuarioVO usuario) throws Exception;
    public void reabrirCompetencia(FechamentoMesVO obj, Boolean removerPrevisaoFaturamento, UsuarioVO usuario) throws Exception;
    public FechamentoMesVO verificarCompetenciaFechada(Date dataVerificar, Date dataCompVerificar, TipoOrigemHistoricoBloqueioEnum tipoOrigem, List<Integer> listaUnidadesEnsino, Integer contaCaixa, UsuarioVO usuario) throws Exception;
    public List<FechamentoMesVO> consultarCompetenciaEmAbertoPorPeriodoPorUnidadeEnsinoPorCodigoIntegracaoContabil(Date dataInicio, Date dataFim, Integer unidadeEnsino, String codigoIntegracaoContabil) throws Exception;
}