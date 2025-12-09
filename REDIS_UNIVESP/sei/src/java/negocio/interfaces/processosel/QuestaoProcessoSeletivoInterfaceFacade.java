package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface QuestaoProcessoSeletivoInterfaceFacade {

	void persistir(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void ativarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void inativarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void cancelarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarOrdemOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestao1, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestao2) throws Exception;

	QuestaoProcessoSeletivoVO clonarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception;

	public List<QuestaoProcessoSeletivoVO> consultar(String enunciado, Integer disciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Boolean controleAcesso, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception;

	public Integer consultarTotalResgistro(String enunciado, Integer disciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum) throws Exception;

	void adicionarOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean validarDados, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws Exception;

	void removerOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws Exception;

	void validarDados(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws ConsistirException;

	QuestaoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo) throws Exception;

	void excluir(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void novo(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO);

	List<QuestaoProcessoSeletivoVO> consultarQuestoesPorDisciplinaRandimicamente(Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Integer qtdeQualquerComplexidade) throws Exception;

	void realizarVerificacaoQuestaoRespondida(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO);

	void realizarCorrecaoQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO);

	/**
	 * @author Wellington - 17 de dez de 2015
	 * @param enunciado
	 * @param disciplinaProcSeletivo
	 * @param grupoDisciplinaProcSeletivo
	 * @param situacaoQuestaoProcessoSeletivoEnum
	 * @param nivelComplexidadeQuestaoProcessoSeletivoEnum
	 * @param controleAcesso
	 * @param usuario
	 * @param limite
	 * @param pagina
	 * @return
	 * @throws Exception
	 */
	List<QuestaoProcessoSeletivoVO> consultarPoGrupoDisciplinaProcSeletivo(String enunciado, Integer disciplinaProcSeletivo, Integer grupoDisciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoProcessoSeletivoEnum, Boolean controleAcesso, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception;

	/**
	 * @author Wellington - 17 de dez de 2015
	 * @param enunciado
	 * @param disciplinaProcSeletivo
	 * @param grupoDisciplinaProcSeletivo
	 * @param situacaoQuestaoProcessoSeletivoEnum
	 * @param nivelComplexidadeQuestaoProcessoSeletivoEnum
	 * @return
	 * @throws Exception
	 */
	Integer consultarTotalResgistroPoGrupoDisciplinaProcSeletivo(String enunciado, Integer disciplinaProcSeletivo, Integer grupoDisciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoProcessoSeletivoEnum) throws Exception;

}
