package negocio.interfaces.gsuite;

import java.util.List;

import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface ClassroomGoogleInterfaceFacade {

	ClassroomGoogleVO consultarSeExisteClassroom(Integer turma, Integer disciplina, String ano, String semestre, Integer professorEad);

    ClassroomGoogleVO realizarGeracaoClassroomGooglePorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO pto, ClassroomGoogleVO obj , UsuarioVO usuarioVO) throws Exception;
	
	ClassroomGoogleVO realizarGeracaoClassroomGoogle(ClassroomGoogleVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarExclusaoClassroomGooglePorHorarioTurmaDisciplinaProgramada(HorarioTurmaDisciplinaProgramadaVO obj, HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDia , UsuarioVO usuarioVO) throws Exception;
	
	void realizarExclusaoClassroomGoogle(ClassroomGoogleVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public void realizarExclusaoClassroomGoogleEad(ClassroomGoogleVO objFiltro , UsuarioVO usuarioVO) throws Exception;

	void realizarBuscaAlunoClassroom(ClassroomGoogleVO obj, ConfiguracaoGeralSistemaVO confGeral, UsuarioVO usuarioVO) throws Exception;
	
	void realizarEnvioConviteAlunoClassroom(ClassroomGoogleVO obj,  PessoaGsuiteVO pessoaGsuite, UsuarioVO usuarioVO) throws Exception;
	
	HttpResponse<JsonNode> realizarAtualizacaoAlunoClassroom(ClassroomGoogleVO obj,  UsuarioVO usuarioVO) throws Exception;
	
	void realizarGeracaoClassroomGooglePorHorarioTurma(HorarioTurmaVO obj , UsuarioVO usuarioVO) throws Exception;

	//void consultarClassroomPorDataModelo(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;
	
	List<ClassroomGoogleVO> consultarClassroom(TurmaVO turmaVO, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void realizarClassroomOperacao(UsuarioVO usuarioVO) throws Exception;

	List<ClassroomGoogleVO> consultarClassroomEad(Integer unidadeEnsino, Integer curso, String nivelEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer professorEad, int nivelMontarDados) throws Exception;
	
	Boolean consultarSeExisteAlgumClassroomGoogleTutoriaOnline(PessoaVO pessoa, String ano, String semestre,UsuarioVO usuarioVO) throws Exception;
	
	List<ClassroomGoogleVO> consultarClassroomGoogleTutoriaOnline(PessoaVO pessoa, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	HttpResponse<JsonNode> realizarProcessamentoLoteClassroom(UsuarioVO usuarioVO) throws Exception;
	
	public void correcaoNomeClassroom(UsuarioVO usuarioVO) throws Exception;


	void realizarRevisaoClassroomPorTurma(TurmaVO obj, UsuarioVO usuarioVO) throws Exception;
	
	void realizarAtualizacaoProfessorAuxiliarClassroomPorLista(List<ClassroomGoogleVO> lista , ProgressBarVO progressBarVO, boolean isIncluir, UsuarioVO usuarioVO) throws Exception;
	
	kong.unirest.HttpResponse<JsonNode> realizarAtualizacaoProfessorAuxiliarClassroom(ClassroomGoogleVO obj , boolean isIncluir, UsuarioVO usuarioVO) throws Exception;

	ClassroomGoogleVO realizarRevisaoClassroom(ClassroomGoogleVO obj, UsuarioVO usuarioVO) throws Exception;

	HttpResponse<JsonNode> realizarProcessamentoLoteClassroomPorHorarioTurma(HorarioTurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarRevisaoClassroomPorTurmaPorDisciplinaPorAnoPorSemestrePorProfessor(TurmaVO obj, DisciplinaVO displina, String ano, String semestre, Integer professor, UsuarioVO usuarioVO) throws Exception;

	void realizarAlteracaoDonoDriveClassroom(ClassroomGoogleVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarVerificacaoSeAlunoEstaVinculadoAoClassroomGoogle(ClassroomGoogleVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuarioVO) throws Exception;
	

}
