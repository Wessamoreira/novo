package negocio.interfaces.gsuite;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;

public interface PessoaGsuiteInterfaceFacade {

	void consultarPessoaGsuiteImportada(DataModelo dataModelo, PessoaGsuiteVO obj); 
		
	
	void consultar(DataModelo dataModelo, PessoaGsuiteVO obj);

	void alterarStatusPessoaGSuite(PessoaGsuiteVO obj, UsuarioVO usuarioLogado);

	boolean validarUnicidadePorEmail(String email);

	boolean validarUnicidadePorPessoaPorUnidadeEnsino(Integer pessoa, Integer unidadeEnsino);

	PessoaGsuiteVO consultarPorPessoaPorUnidadeEnsino(Integer pessoa, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario);	

	PessoaGsuiteVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario);

	void persistir(PessoaVO pessoaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void excluir(PessoaGsuiteVO obj, UsuarioVO usuarioLogado) throws Exception;
	
	public List<PessoaGsuiteVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivelGsuite(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, boolean isSomentePessoaGsuite, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;


	void alterarAtualizacaoPessoaGsuiteImportada(PessoaGsuiteVO obj, UsuarioVO usuarioLogado);


	List<PessoaGsuiteVO> consultarAlunosDoEadTurmaDisciplinaDisponivelGsuite(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer codigoProfessor, boolean isSomentePessoaGsuite, UsuarioVO usuarioVO) throws Exception;

}
