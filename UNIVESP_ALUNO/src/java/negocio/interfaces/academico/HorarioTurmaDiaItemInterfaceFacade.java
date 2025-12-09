/**
 * 
 */
package negocio.interfaces.academico;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

//import negocio.comuns.academico.ChoqueHorarioVO;
//import negocio.comuns.academico.HorarioTurmaDiaItemVO;
//import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * @author Rodrigo Wind
 *
 */
public interface HorarioTurmaDiaItemInterfaceFacade {
	/**
	 * @author Rodrigo Wind - 04/09/2015
	 */

//	public void persistir(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, UsuarioVO usuarioVO) throws Exception;
//
//	public void persistirHorarioTurmaDiaItem(HorarioTurmaDiaVO horarioTurmaDiaVO, UsuarioVO usuarioVO) throws Exception;
//	
//	public List<HorarioTurmaDiaItemVO> consultarPorHorarioTurmaDia(Integer horarioTurmaDia) throws Exception;
//
//	public List<HorarioTurmaDiaItemVO> consultarParametrizada(Integer turma, Integer disciplina, Integer professor, Integer sala, Integer numeroAula, String ano, String semestre, Date dataInicio, Date dataTermino) throws Exception;
//
//	public List<ChoqueHorarioVO> realizarVerificacaoChoqueHorarioSalaAula(Integer horarioTurma, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, boolean retornarExcecao) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/09/2015
	 * @param horarioTurma
	 * @param sala
	 * @param data
	 * @param horaInicio
	 * @param horaTermino
	 * @param retornarExcecao
	 * @return
	 * @throws Exception
	 */
	Boolean realizarVerificacaoChoqueHorarioSalaAula(Integer horarioTurma, Integer sala, Boolean controlarChoqueSala, Date data, String horaInicio, String horaTermino, boolean retornarExcecao) throws Exception;

	/**
	 * @author Wellington - 29 de dez de 2015
	 * @param turma
	 * @param ano
	 * @param semestre
	 * @param dataInicio
	 * @param dataTermino
	 * @return
	 * @throws Exception
	 */
	SqlRowSet consultarProgramacaoAulaHorarioTurmaRel(Integer turma, String ano, String semestre, Date dataInicio, Date dataTermino) throws Exception;
	

	/** 
	 * @author Wellington - 9 de mar de 2016 
	 * @param turma
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
//	HorarioTurmaDiaItemVO consultarAulaProgramadaPorTurmaDisciplinaAnoSemestreDistribuicaoSubturma(Integer turma, Integer disciplina, String ano, String semestre, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluirPorTurmaPorDisciplina(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception;

	void atualizarNovaDisciplinaHorarioTurmaDiaItemPorAlteracaoGradeCurricularIntegral(Integer turma, Integer disciplina, Integer novaDisiplna, UsuarioVO usuario) throws Exception;
	
	public HashMap<String, String> consultarMenorHorarioMaiorHorarioProgramacaoAulaDisciplinasAluno(String matricula, String ano, String semestre, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
//	public HorarioTurmaDiaItemVO consultarFuncionarioCargoPorCodigo(Integer codigo, UsuarioVO usuarioVO) throws Exception;
//
//	HorarioTurmaDiaItemVO consultarPorCodigo(Integer codigo) throws Exception;
//
//	public void alterar(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, UsuarioVO usuarioVO) throws Exception;
	 

//	public List<HorarioTurmaDiaItemVO> consultarPorGoogleMeet(GoogleMeetVO googleMeetVO) throws Exception;

	void alterarAulaResposicao(boolean aulaResposicao, Integer codigo, UsuarioVO usuarioVO);

	Integer consultarQtdeAulaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO);
}
