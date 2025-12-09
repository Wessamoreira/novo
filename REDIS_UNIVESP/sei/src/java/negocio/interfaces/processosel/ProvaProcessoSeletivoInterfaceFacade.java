package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoProvaProcessoSeletivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface ProvaProcessoSeletivoInterfaceFacade {

	void persistir(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;

	ProvaProcessoSeletivoVO novo();

	void excluir(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;

	void ativar(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;

	void inativar(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;

	List<ProvaProcessoSeletivoVO> consultar(String descricao, SituacaoProvaProcessoSeletivoEnum situacaoProcessoSeletivo, Boolean controlarAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception;

	Integer consultarTotalRegistro(String descricao, SituacaoProvaProcessoSeletivoEnum situacaoProcessoSeletivo) throws Exception;

	void adicionarQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws Exception;

	void removerQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws Exception;

	void alterarOrdemApresentacaoQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoListaExercicio1, QuestaoProvaProcessoSeletivoVO questaoListaExercicio2);

	ProvaProcessoSeletivoVO clonarProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception;

	void realizarVerificacaoQuestaoUnicaEscolha(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO);

	void realizarGeracaoGabarito(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception;

	void validarDados(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws ConsistirException, Exception;

	public String executarDownloadProvaPDF(UnidadeEnsinoVO unidadeEnsino, ProvaProcessoSeletivoVO prova, ProcSeletivoVO procSeletivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	ProvaProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados) throws Exception;

	public String getDesignIReportRelatorio();

	public String caminhoBaseIReportRelatorio();

	/**
	 * Responsável por executar a definição do provaprocessoseletivo a ser utilizado levando em consideração se o mesmo está vínculado a um
	 * grupoDisciplinaProcSeletivo, se o ProcSeletivoCurso também está vínculado a um grupoDisciplinaProcSeletivo e se os dois tem o
	 * grupoDisciplinaProcSeletivo em comum.
	 * 
	 * @author Wellington - 5 de jan de 2016
	 * @param inscricao
	 * @return
	 * @throws Exception
	 */
	Integer consultarCodigoProvaPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(Integer inscricao) throws Exception;

}
