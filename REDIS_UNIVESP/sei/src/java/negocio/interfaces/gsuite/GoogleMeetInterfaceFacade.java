package negocio.interfaces.gsuite;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;

public interface GoogleMeetInterfaceFacade {
	
	public void realizarExclusaoEventoGoogleMeetVisaoProfessor(DataEventosRSVO dataEvento, AgendaAlunoRSVO agenda, boolean isTodosHorarios, UsuarioVO usuario) throws Exception;
	
	public void realizarGeracaoEventoGoogleMeetVisaoProfessor(DataEventosRSVO dataEvento, AgendaAlunoRSVO agenda, boolean isTodosHorarios, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;
	
	public void realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs, boolean isExclusao, UsuarioVO usuarioVO) throws Exception;

	public List<DataEventosRSVO> consultarCalendarioPorProfessor(Integer professor); 
	
	public List<DataEventosRSVO> consultarCalendarioPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs);
	
	public List<GoogleMeetVO> consultarPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date data); 
	
	public List<GoogleMeetVO> consultarPorProfessorTurmaDiscipinaData(Integer professor, Integer turma, Integer disciplina, Date data);

	public GoogleMeetVO consultarPorHorarioTurmaDiaItem(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, int nivelMontarDados);

	void realizarOperacaoGoogleMeetVisaoAdministrativa(HorarioTurmaDiaItemVO htdiSelecionado,  HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO, boolean isExclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarGeracaoEventoGoogleMeetConfiguracaoSeiGsuite(GoogleMeetVO googleMeetVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;
	
	void realizarExclusaoGoogleMeet(GoogleMeetVO obj , UsuarioVO usuarioVO) throws Exception;	
	
	public GoogleMeetVO realizarPersistenciaGoogleMeetAvulso(GoogleMeetVO googleMeetVO, TurmaVO turma, String ano, String semestre, DisciplinaVO disciplina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	 

	void realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(HorarioTurmaVO horarioTurma, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO, Integer professor, Integer disciplina,  boolean isExclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	public void consultarGoogleMeetPorDataModelo(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;	

	void correcaoDonoMeet(UsuarioVO usuarioVO) throws Exception;

}
