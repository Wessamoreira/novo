package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;

/*
 * @author Victor Hugo 17/09/2014
 */
public interface AtividadeDiscursivaInteracaoInterfaceFacade {

	void incluir(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO) throws Exception;

	void persistir(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AtividadeDiscursivaInteracaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AtividadeDiscursivaInteracaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AtividadeDiscursivaInteracaoVO> consultarInteracoesPorChaveAtividadeDiscursivaAluno(Integer atividadeDiscursivaAluno, Integer limite, Integer offSet, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Integer consultarTotalRegistroInteracao(Integer atividadeDiscursivaAluno, Integer usuario) throws Exception;

	void alterarInteracaoJaLidaAoVisualizarATela(Integer codigoAtividadeDiscursivaRespostaAluno, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AtividadeDiscursivaInteracaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Integer consultarQtdeInteracoesAlunosPorCodigoProfessor(Integer codigoProfessor, UsuarioVO usuarioVO) throws Exception;

	Integer consultarQtdeInteracoesPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;

	Integer consultarQtdeInteracoesProfessorPorMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplina, Boolean interacoesAluno, UsuarioVO usuarioVO) throws Exception;
	
}
