package negocio.interfaces.ead;

import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.utilitarias.ProgressBarVO;

/*
 * @author Victor Hugo 10/10/2014
 */
public interface AvaliacaoOnlineInterfaceFacade {

	void incluir(AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AvaliacaoOnlineVO avaliacaoOnlineVO) throws Exception;

	void persistir(AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	AvaliacaoOnlineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorNomeDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void adicionarQuestaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO) throws Exception;

	void alterarOrdemApresentacaoQuestaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2);

	List<AvaliacaoOnlineVO> consultarAvaliacoesOnlinesPorCodigoDisciplinaETipoUsoGeral(Integer codigoDisciplina, UsuarioVO usuarioLogado) throws Exception;

	void alterarAvaliacaoOnlineJaAtivada(AvaliacaoOnlineVO obj, boolean verificarAcesso, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception;
	
	void alterarSituacaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception;	

	List<AvaliacaoOnlineVO> consultarAvaliacoesOnlinesPorDisciplinaEConteudoEAvaliacoesOnlinesRandomicas(Integer codigoDisciplina, Integer codigoConteudo, Boolean consultarPorProfessor, Integer codigoProfessor) throws Exception;

	AvaliacaoOnlineVO clonar(AvaliacaoOnlineVO avaliacaoOnlineVO) throws Exception;	

	void removerQuestaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineVO consultarAvaliacaoOnlineDaTurmaDisciplinaAnoSemestreOuDefaultTurma(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioLogado) throws Exception;

	void atualizarSituacaoAvaliacaoOnlinePorConteudo(SituacaoEnum situacao, ConteudoVO conteudoVO,
			UsuarioVO usuarioLogado) throws Exception;

	AvaliacaoOnlineVO consultarAvaliacaoOnlineMatriculaAlunoDeveResponder(Integer matriculaPeriodoTurmaDisciplina, String codigoOrigemAvaliacaoOnline, UsuarioVO usuarioLogado) throws Exception;
	
	List<AvaliacaoOnlineVO> consultarAvaliacaoOnlinePorTurmaAlunoDeveResponder(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioLogado) throws Exception;
	
	Boolean verificarAvaliacaoOnlineMatriculaExistente(Integer codigoAvaliacaoOnline) throws Exception;
	
	void validarQuantidadeQuestoesPorNivel(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> consultarVariavelTituloConfiguracaoAcademicoEAvaliacaoOnline(Integer avaliacaoOnline, UsuarioVO usuarioVO);
	
	public List<AvaliacaoOnlineVO> consultarAvaliacaoOnlinePorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception ;
	
	public AvaliacaoOnlineVO consultarPorCodigoAvaliacaoCodigoDisciplina(Integer codigo, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public List<AvaliacaoOnlineVO> consultarAvaliacaoOnlinePorNomeAvaliacaoDisciplina(String valorConsulta, Integer disciplina,  int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception ;
	
	List<AvaliacaoOnlineVO> consultarPorQuestaoFixa(Integer questao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	AvaliacaoOnlineMatriculaVO realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, Boolean simulandoAvaliacao) throws Exception;

	StringBuilder consultarAvaliacaoOnlineEstaVinculada(Integer avaliacaoOnline) throws Exception;

	void inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	public AvaliacaoOnlineVO consultarAvaliacaoOnlinePorConteudoUnidadePaginaRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
}
