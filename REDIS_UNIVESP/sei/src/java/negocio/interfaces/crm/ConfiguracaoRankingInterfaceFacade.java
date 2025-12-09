/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ConfiguracaoRankingVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;

/**
 *
 * @author Paulo Taucci
 */
public interface ConfiguracaoRankingInterfaceFacade {

    public void persistir(ConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception;

    public void excluir(ConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception;

    public List<ConfiguracaoRankingVO> consultar(String valorConsulta, Date dataIni, Date dataFim, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalRegistrosEncontrados(String valorConsulta, Date dataIni, Date dataFim, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDados(ConfiguracaoRankingVO obj) throws Exception;

    public ConfiguracaoRankingVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalRegistrosEncontradosPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalRegistrosEncontradosPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalRegistrosEncontradosPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalRegistrosEncontradosPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorPeriodo(Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalRegistrosEncontradosPorPeriodo(Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void removerPercentual(List<PercentualConfiguracaoRankingVO> listaPercentual, PercentualConfiguracaoRankingVO obj) throws Exception;

    public void adicionarPercentual(List<PercentualConfiguracaoRankingVO> listaPercentual, PercentualConfiguracaoRankingVO obj) throws Exception;

    public List<ConfiguracaoRankingVO> consultaRapidaNivelComboBox(Integer unidadeEnsino, String situacao, UsuarioVO usuarioVO);
}
