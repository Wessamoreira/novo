/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;
import negocio.comuns.crm.ComissionamentoTurmaVO;
import negocio.comuns.crm.ConfiguracaoRankingVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Otimize-04
 */
public interface ComissionamentoTurmaInterfaceFacade {
    public void persistir(ComissionamentoTurmaVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public void incluir(final ComissionamentoTurmaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void alterar(final ComissionamentoTurmaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void excluir(ComissionamentoTurmaVO obj, UsuarioVO usuario) throws Exception;
    public List<ComissionamentoTurmaVO> consultaRapidaPorTurma(String valorConsulta, Boolean jobComissionamento, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<ComissionamentoTurmaVO> consultaRapidaPorCurso(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<ComissionamentoTurmaVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<ComissionamentoTurmaVO> consultaRapidaPorDataPrimeiroPagamento(Date dataPrimeiroPagamento, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void consultar(DataModelo controleConsultaOtimizado, String campoConsulta, String valorConsulta, Date valorConsultaData, UsuarioVO usuarioVO) throws ConsistirException, Exception;
    public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
    public void adicionarObjComissionamentoFaixaValorVOs(ComissionamentoTurmaVO comissionamentoTurmaVO, ComissionamentoTurmaFaixaValorVO obj) throws Exception;
    public void excluirObjComissionamentoFaixaValorVOs(ComissionamentoTurmaVO comissionamentoTurmaVO, ComissionamentoTurmaFaixaValorVO obj) throws Exception;
    public void validarDados(ComissionamentoTurmaVO obj) throws Exception;
    public void realizarInicializacaoDataUltimoPagamento(ComissionamentoTurmaVO obj) throws Exception;
    public void carregarDados(ComissionamentoTurmaVO obj, UsuarioVO usuario) throws Exception;
    public void carregarDados(ComissionamentoTurmaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ConfiguracaoRankingVO> montarListaSelectItemConfiguracaoRanking(Integer unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception;
    public Double consultarTotalAReceberTurma(Integer turma, UsuarioVO usuario) throws Exception;
    public Integer consultarTotalAlunosPagantes(Integer turma) throws Exception;
    public void alterarValorETotalPagantes(final Integer turma, final Double valorTotalAReceberTurma, final Integer totalAlunosPagantes, UsuarioVO usuario) throws Exception;
    public void atualizarComissionamentoTurma(Integer unidadeEnsino, Integer curso, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception;
    public Double consultarValorTotalAReceberTicketMedioCRM(String matricula);
    public void alterarDataProcComissionamento(final Integer codComissionamento, final Boolean comissionamentoProcessado, UsuarioVO usuario) throws Exception;
}
