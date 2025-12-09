package negocio.interfaces.ead;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.enumeradores.ArtefatoEntregaEnum;
import negocio.comuns.ead.enumeradores.SituacaoRespostaAtividadeDiscursivaEnum;

public interface AtividadeDiscursivaRespostaAlunoInterfaceFacade {

	void validarDados(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO) throws Exception;

	void incluir(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AtividadeDiscursivaRespostaAlunoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AtividadeDiscursivaRespostaAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AtividadeDiscursivaRespostaAlunoVO> consultarDadosTelaConsultaProfessorAluno(Integer disciplina, Integer turma, String ano, String semestre, Integer atividadeDiscursiva, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	AtividadeDiscursivaRespostaAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void uploadArquivo(FileUploadEvent uploadEvent, AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void persistir(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void realizarAvaliarRespostaAlunoAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, HistoricoVO historicoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	SqlRowSet consultarAlunosEProfessoresNotificacaoAtividadeDiscursiva() throws Exception;

	void incluirDataNotificacaoAtividadeDiscursivaRespostaAluno(Integer codigoAtividadeDiscursivaRespostaAlunoVO, Date dataNotificacao, String campoDataNotificacao) throws Exception;

	SqlRowSet consultarAtividadesDiscursivasRespostaAlunoIniciandoNaDataAtual() throws Exception;

	List<AtividadeDiscursivaRespostaAlunoVO> consultarAtividadeDiscursivasPorMatriculaOuCodigoMatriculaPeriodoTurmaDisciplina(String matricula, Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO, String ano, String semestre) throws Exception;
	
	public void persistirAtividadeDiscursivaInteracaoAlunoProfessor(AtividadeDiscursivaRespostaAlunoVO obj, AtividadeDiscursivaInteracaoVO atividadeInteracao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, boolean registrarLog, UsuarioVO usuarioVO) throws Exception;

	AtividadeDiscursivaRespostaAlunoVO realizarCriacaoAtividadeDiscursivaRespostaAluno(AtividadeDiscursivaRespostaAlunoVO obj, UsuarioVO usuario) throws Exception;

	void atualizarPeriodoAtividadeDiscursivaRespostaAluno(AtividadeDiscursivaVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public void atualizarPeriodoAtividadeDiscursivaRespostaAlunoPorCodigo(final AtividadeDiscursivaRespostaAlunoVO obj,  UsuarioVO usuarioVO) throws Exception;
	
	public AtividadeDiscursivaRespostaAlunoVO consultarAtividadeRespostaAlunoPorCodAtividadediscursivaMatriculaperiodoturmadisciplina(String codigoAtividadeDiscursiva , Integer matriculaperiodoturmadisciplina   , UsuarioVO usuarioVO) throws Exception;
	
	public Boolean consultarAtividadeDiscursivaRespondida(Integer codigoAtividadeDiscursiva) throws Exception;
	
	void removerArquivo(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	void registrarLogAtividadeDiscursivaRespostaAluno(int atividadeDiscursiva, int atividadeDiscursivaRespostaAluno, int matriculaPeriodoTurmaDisciplina, String acao, Date data,
			String erro, SituacaoRespostaAtividadeDiscursivaEnum situacaoRespostaAtividadeDiscursivaEnum, ArtefatoEntregaEnum artefatoEntregaEnum, String arquivo, int usuario);
	
	void persistirAtividadeDiscursivaInteracaoSolicitandoAvaliacaoProfessor(AtividadeDiscursivaRespostaAlunoVO obj, AtividadeDiscursivaInteracaoVO atividadeInteracao, 
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}
