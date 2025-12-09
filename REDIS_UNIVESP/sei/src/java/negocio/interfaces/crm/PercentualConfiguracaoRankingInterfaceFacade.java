/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;

/**
 *
 * @author Paulo Taucci
 */
public interface PercentualConfiguracaoRankingInterfaceFacade {

    public void persistir(PercentualConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception;

    public void excluir(PercentualConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception;

    public void validarDados(PercentualConfiguracaoRankingVO obj) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public PercentualConfiguracaoRankingVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirPercentualConfiguracaoRankingVOs(Integer configuracaoRanking, UsuarioVO usuario) throws Exception;

    public void incluirPercentualConfiguracaoRankingVOs(Integer configuracaoRanking, List<PercentualConfiguracaoRankingVO> percentualVOs) throws Exception;

    public void alterarPercentualConfiguracaoRankingVOs(Integer configuracaoRanking, List<PercentualConfiguracaoRankingVO> percentualVOs, UsuarioVO usuario) throws Exception;

    public List<PercentualConfiguracaoRankingVO> consultarPorConfiguracaoRanking(Integer configuracaoRanking, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTurmaComissionamentoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
