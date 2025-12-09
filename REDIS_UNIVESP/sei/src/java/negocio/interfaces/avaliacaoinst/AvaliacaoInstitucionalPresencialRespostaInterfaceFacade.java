package negocio.interfaces.avaliacaoinst;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface AvaliacaoInstitucionalPresencialRespostaInterfaceFacade {

    public AvaliacaoInstitucionalPresencialRespostaVO novo() throws Exception;

    public void incluir(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorAvaliacaoInstitucional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorPublicoAlvo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDataInicio(Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDataFinal(Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorUnidadeEnsino(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorQuestionario(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorSituacao(String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void carregarDados(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorUnidadeEnsinoDataSituacaoPublicoAlvo(Integer unidadeEnsino, Date data, String situacao, String publicoAlvo, Boolean avaliacaoPresencial, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDataCriacao(Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorCurso(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorTurma(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDisciplina(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorProfessor(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Boolean verificarAvaliacaoInstitucionalPresencialGravada(Integer avaliacaoInstitucional, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Integer professor) throws Exception;
}
