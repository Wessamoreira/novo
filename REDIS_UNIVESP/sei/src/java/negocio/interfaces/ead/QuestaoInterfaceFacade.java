package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.ead.enumeradores.UsoQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;


public interface QuestaoInterfaceFacade {
                    
    void alterarOrdemOpcaoRespostaQuestao(QuestaoVO questaoVO, OpcaoRespostaQuestaoVO opcaoRespostaQuestao1, OpcaoRespostaQuestaoVO opcaoRespostaQuestao2) throws Exception;
        
    void adicionarOpcaoRespostaQuestao(QuestaoVO questaoVO, Boolean validarDados, OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) throws Exception;
    
    void removerOpcaoRespostaQuestao(QuestaoVO questaoVO, OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) throws Exception;
        
    QuestaoVO consultarPorChavePrimaria(Integer codigo) throws Exception;

    void excluir(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;

    void novo(QuestaoVO questaoVO, Boolean usoAtividadeDiscursiva, Boolean usoExercicio, Boolean usoOnline, Boolean usoPresencial);

    void realizarVerificacaoQuestaoRespondida(QuestaoVO questaoVO);

    void realizarCorrecaoQuestao(QuestaoVO questaoVO);

	List<QuestaoVO> consultar(String enunciado, TemaAssuntoVO temaAssuntoVO, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Boolean controleAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina, Integer codigoConteudo, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, boolean simulandoAvaliacao) throws Exception;

	Integer consultarTotalResgistro(String enunciado, TemaAssuntoVO temaAssuntoVO, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Integer codigoConteudo, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum,Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, boolean simulandoAvaliacao) throws Exception;

	List<QuestaoVO> consultarQuestoesPorUsuario(String enunciado, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Boolean controleAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina, Integer codigoConteudo) throws Exception;

	Integer consultarTotalResgistroPorUsuario(String enunciado, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Integer codigoConteudo, UsuarioVO usuario) throws Exception;

	void validarDados(QuestaoVO questaoVO, String idEntidade,  UsuarioVO usuarioVO) throws ConsistirException, Exception;

	void persistir(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception;

	void ativarQuestao(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception;

	void inativarQuestao(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception;

	void cancelarQuestao(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception;

	public Integer consultarQuantidadeQuestoesPorDisciplinaNivelComplexidadeETemaAssunto(Integer codigoDisciplina, Integer codigoTemaAssunto, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva) throws Exception;

	List<QuestaoVO> consultarQuestoesPorDisciplinaRandomicamente(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoTemaAssunto, Integer codigoUnidadeConteudo, Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Integer qtdeQualquerComplexidade, UsoQuestaoEnum usoQuestao, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, Integer codigoConteudo, Integer codigoListaExercicio, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception;

	QuestaoVO clonarQuestao(QuestaoVO questaoVO, List<String> tipoCloneQuestao,  Boolean telaExercicio, Boolean telaOnline, Boolean telaPresencial, String idEntidade, UsuarioVO usuarioVO, Boolean clonarComoAtiva) throws Exception;

	QuestaoVO realizarClonagemReaOutraDisciplinaQuestao(ConteudoVO obj, QuestaoVO questaoVO, boolean isConsultaQuestao, UsuarioVO usuarioVO) throws Exception;

	List<QuestaoVO> consultarQuestaoParaClonagemReaOutraDisciplina(ConteudoVO obj, ConteudoUnidadePaginaRecursoEducacionalVO cupre) throws Exception;

	QuestaoVO consultarQuestaoExistenteParaClonagemPorCodigoQuestaoPorDisciplina(Integer questao, Integer disciplina, boolean isOnline, boolean isExercicio) throws Exception;
	
	public QuestaoVO consultarPorChavePrimariaUsuario(Integer codigo, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio,
			Boolean usoAtividadeDiscursiva, UsuarioVO usuario) throws Exception;
	
	void realizarAnulacaoQuestao(QuestaoVO questaoVO, List<MatriculaVO> listaMatriculaCorrigirNotaVOs, Boolean simularAnulacao, UsuarioVO usuarioVO) throws Exception;

	QuestaoVO consultarPorChavePrimaria(Integer codigo, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio,
			Boolean usoAtividadeDiscursiva) throws Exception;

	
	
}
