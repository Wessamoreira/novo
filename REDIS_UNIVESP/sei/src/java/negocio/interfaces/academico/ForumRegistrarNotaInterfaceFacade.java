package negocio.interfaces.academico;

import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ForumRegistrarNotaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ForumRegistrarNotaInterfaceFacade {

	void persistirForumRegistrarNota(ForumVO obj, HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ForumRegistrarNotaVO> consultarForumRegistrarNota(Integer forum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ForumRegistrarNotaVO> consultarForumRegistrarNotaRapidaPorTurmaAnoSemestre(ForumVO forum, TurmaVO turma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ForumRegistrarNotaVO> consultarPessoaInteracaoForumRapidaPorTurmaAnoSemestre(ForumVO forum, TurmaVO turma, String ano, String semestre, UsuarioVO usuario) throws Exception;

	void carregarDadosConfiguracaoAcadimicoPeloTipoDaNota(ForumVO forum, String variavel, ConfiguracaoAcademicoVO configuracaoAcademicoVigenteNota, Boolean isNotaPorConceito, UsuarioVO usuarioLogado) throws Exception;

	void alterarForumRegistrarNotaParaRegistarNotaHistorico(ForumRegistrarNotaVO forumRegistrar, UsuarioVO usuario) throws Exception;

	List<SelectItem> consultarVariavelTituloJaRegistradasForumRegistroNotaPorForumTurmaAnoSemestre(List<SelectItem> listaSelectItemTipoInformarNota, ForumVO forum, TurmaVO turma, String ano, String semestre, UsuarioVO usuarioVO);

	void alterarForumRegistrarNotaParaRegistrarNotaHistorico(List<ForumRegistrarNotaVO> forumRegistrarNotaVOs, 
			HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO, UsuarioVO usuarioVO) throws Exception;
}
