package negocio.interfaces.ead;

import java.util.Date;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.protocolo.RequerimentoVO;

public interface AtividadeDiscursivaInterfaceFacade {

	void incluir(AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AtividadeDiscursivaVO atividadeDiscursivaVO) throws Exception;

	void persistir(AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	void alterar(AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AtividadeDiscursivaVO atividadeDiscursivaVO, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	AtividadeDiscursivaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AtividadeDiscursivaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	List<AtividadeDiscursivaVO> consultarDadosTelaConsulta(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;

	AtividadeDiscursivaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Rodrigo Wind - 16/10/2015
	 * @param atividadeDiscursivaVO
	 * @param requerimentoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void realizarCriacaoAtividadeDiscursivaPorRequerimento(AtividadeDiscursivaVO atividadeDiscursivaVO, RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 16/10/2015
	 * @param atividadeDiscursivaVO
	 * @param usuario
	 * @throws Exception
	 */
	void realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursivaVO, UsuarioVO usuario) throws Exception;

	SqlRowSet consultarNovasAtividadeDiscursivaParaPresencialPorJob(Date data) throws Exception;

	SqlRowSet consultarNovasAtividadeDiscursivaParaEadPorJob(Date data) throws Exception;

	List<AtividadeDiscursivaVO> consultarAtividadeDiscursivaParaGeracaoCalendarioAtividade(Integer matriculaPeriodoTurmaDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;

	void removerMaterialApoio(ArquivoVO arquivo, AtividadeDiscursivaVO obj, UsuarioVO usuarioVO) throws Exception;

	void uploadMaterialApoio(FileUploadEvent upload, AtividadeDiscursivaVO obj,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	List<AtividadeDiscursivaVO> consultarInteracoesNaoLidasAlunosPorCodigoProfessor(Integer codigoProfessor, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> consultarVariavelTituloConfiguracaoAcademicoEAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursivaVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<AtividadeDiscursivaVO> consultarAtividadeDiscursivaPorEnunciado(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public AtividadeDiscursivaVO consultarPorCodigoAtividadeDisciplina(Integer codigo, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public List<AtividadeDiscursivaVO> consultarAtividadeDiscursivaPorEnunciadoDisciplina(String valorConsulta, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	StringBuilder getSQLConsultarInteracoesNaoLidasAlunosPorCodigoProfessor(Integer codigoProfessor) throws Exception;
}
