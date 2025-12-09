package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumPessoaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoForumEnum;
import negocio.comuns.arquitetura.UsuarioVO;


public interface ForumInterfaceFacade {
    
	 void persistir(ForumVO forum, String idEntidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	    
	    List<ForumVO> consultarForumPorDisciplinaTurma(Integer turma, Integer disciplina, SituacaoForumEnum situacaoForum, Integer limite, Integer pagina, 
	            boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	    
	    ForumVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;
	    
	    Integer consultarQtdeAtualizacaoForumPorUsuarioAluno(String matricula, Integer disciplina, String anoSemestre, Integer usuario) ;
	    
	    Integer consultarQtdeAtualizacaoForumPorUsuarioProfessor(Integer disciplina, Integer unidadeEnsino, UsuarioVO usuario);

	    List<ForumVO> consultarForumPorMatricula(String matricula, Integer disciplina, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum,  String anoSemestre, Integer usuario, Integer limite, Integer pagina, int nivelMontarDados) throws Exception;

	    void ativar(ForumVO forumVO, String idEntidade, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	    
	    void inativar(ForumVO forumVO, String idEntidade, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	    Integer consultarTotalRegistroForumPorMatricula(String matricula, Integer disciplina, String tema,  String anoSemestre, Integer usuario) throws Exception;

	    public List<ForumVO> consultarForumPorConteudoDisciplina(DisciplinaVO obj, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer usuario, Integer limite, Integer pagina, int nivelMontarDados) throws Exception;
	    
	    List<ForumVO> consultarForumPorDisciplina(ForumVO obj, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer usuario, Integer limite, Integer pagina, int nivelMontarDados) throws Exception;

		Integer consultarTotalRegistroForumPorDisciplina(ForumVO obj, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum,  Integer usuario) throws Exception;	

		List<ForumVO> consultarForumVisaoProfessor(ForumVO obj, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer limite, Integer pagina, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		Integer consultarTotalRegistroForumVisaoProfessor(ForumVO obj, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, UsuarioVO usuario) throws Exception;

		void adiconarForumPessoa(ForumVO forum, ForumPessoaVO forumPessoa) throws Exception;

		void removerForumPessoa(ForumVO forum, ForumPessoaVO forumPessoa) throws Exception;

		void excluir(ForumVO forum, UsuarioVO usuarioVO) throws Exception;
		
		public List<ForumVO> consultarAtualizacaoForumPorProfessor(Integer professor, Integer unidadeEnsino,Integer limite, Integer pagina, UsuarioVO usuario) throws Exception;
		
		public Integer consultarTotalRegistroAtualizacaoForumVisaoProfessor(ForumVO obj, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;



}
