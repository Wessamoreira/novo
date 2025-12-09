package negocio.interfaces.blackboard;

import java.util.List;
import java.util.Map;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.SalaAulaBlackboardGrupoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.SalaAulaBlackboardOperacaoVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface SalaAulaBlackboardOperacaoInterfaceFacade {
	
	void realizarExecucaoSalaAulaOperacao(SalaAulaBlackboardOperacaoVO obj, UsuarioVO usuarioVO) throws Exception;
	
	void realizarFechamentoGrupoSalaAulaBlackboardPorTCC(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento, UsuarioVO usuarioVO) throws Exception;
	
	void realizarFechamentoGrupoSalaAulaBlackboardPorProjetoIntegrador(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento, UsuarioVO usuarioVO) throws Exception;
	
	void executarFechamentoGrupoSalaAulaBlackboardIndividual(SalaAulaBlackboardGrupoVO sala, Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd, UsuarioVO usuarioVO) throws Exception;
	
	public void realizarOperacaoDeApuracaoNotasAva(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre,
			Integer curso, Integer turma, Integer supervisor, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean realizarCalculoMediaApuracaoNotas, boolean realizarBuscarNotaBlackboard, UsuarioVO usuarioLogado, String nivelEducacionalApresentar) throws Exception;
	
	void realizarGeracaoListaGrupoSalaAulaBlackboardVO(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVO, ClassificacaoDisciplinaEnum classificacaoDisciplina, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	void realizarGeracaoGrupoSalaAulaBlackboardVO(SalaAulaBlackboardGrupoVO obj, ClassificacaoDisciplinaEnum classificacaoDisciplina, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	Integer consultarQtdAlunosNaoEnsaladosProjetoIntegrador(SalaAulaBlackboardGrupoVO sabGrupo) throws Exception;
	
	void incluirPessoaSalaBlack(SalaAulaBlackboardVO salaAulaBlackVO, Integer pessoa,
			TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String email, UsuarioVO usuarioVO) throws Exception;

	void incluirPessoaSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String idSalaAulaBlackboard, String email, UsuarioVO usuarioVO) throws Exception;
	
	void incluirPessoaSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String idSalaAulaBlackboard, String msgNotificacao, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, String email, UsuarioVO usuarioVO) throws Exception;
	
	void updatePessoaSalaBlack(Integer codigoOrigem, Integer codigoDestiono, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String idSalaAulaBlackboard, String msgNotificacao, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, String email, UsuarioVO usuarioVO) throws Exception;
	
	void excluirPessoaSalaBlack(Integer salaAulaBlack, Integer pessoa,
			TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String email, UsuarioVO usuarioVO) throws Exception;
	
	void excluirPessoaSalaBlack(Integer salaAulaBlack, Integer pessoa,
								TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String idSalaAulaBlackboard, String email, UsuarioVO usuarioVO) throws Exception;

	
	void excluirSalaBlackPorFechamento(SalaAulaBlackboardVO salaAulaBlackboardVO, UsuarioVO usuarioVO) throws Exception;
	
	Integer incluirCopiaConteudoSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, String tipoOrigem, UsuarioVO usuarioVO) throws Exception;
	
	Integer incluirSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO,String tipoOrigem, Integer codigoOrigem, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception;

	boolean incluirSalaBlack(List<SalaAulaBlackboardVO> salaAulaBlackboardVOs, UsuarioVO usuarioVO) throws Exception;

	void consultarSeExisteProcessamentoPendente();
	
	void consultarEnsalamentoPendenteProcessamento(ProgressBarVO progressBarVO);

	void consultarLogErroProcessamento(DataModelo controleConsulta, SalaAulaBlackboardOperacaoVO salaAulaBlackboardOperacaoFiltroVO) throws Exception;
	
	void executarEnvioMensagemSalaAulaBlackboardOperacao(Integer codigoOperacao, UsuarioVO usuarioVO) throws Exception;
	
	void realizarExclusaoAlunoDaSalaAulaBlackboardTipoDisciplina(String matricula, Integer disciplina, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoCadastralPessoaBlack(String email, UsuarioVO usuarioVO) throws Exception;

	void realizarAtivacaoPessoaBlack(String email, UsuarioVO usuarioVO) throws Exception;

	void realizarInativacaoPessoaBlack(String email, UsuarioVO usuarioVO) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(String listaAlunos)
			throws Exception;

	void atualizarCampoMsgNotificacaoExecutadaSalaAulaBlackboardOperacao(Integer codigo, String erro);
	
	void atualizarSalaAulaBlackboardOperacaoExecutada(Integer codigo);
	
	void atualizarSalaAulaBlackboardOperacao(Integer codigo);

	void atualizarCampoErroSalaAulaBlackboardOperacao(Integer codigo, String erro);

	void incluirLogErro(SalaAulaBlackboardVO salaAulaBlackboardVO,
			TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, Integer pessoa, String email,
			String matricula, Integer matriculaPeriodoTurmaDisciplina, String ano, String semestre, String idSalaAulaBlackboard, String tipoOrigem,
			String operacao, String erro, UsuarioVO usuarioVO) ;


	Integer consultarQuantidadeOperacaoRestantesPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO);
}
