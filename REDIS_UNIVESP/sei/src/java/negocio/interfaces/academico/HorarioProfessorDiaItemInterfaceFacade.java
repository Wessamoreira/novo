/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public interface HorarioProfessorDiaItemInterfaceFacade {

    public String designIReportRelatorio();
    public String designIReportRelatorioExcel();
    public String caminhoBaseRelatorio();
	/**
	 * @author Rodrigo Wind - 09/09/2015
	 * @param horarioProfessorDia
	 * @return
	 * @throws Exception
	 */
	List<HorarioProfessorDiaItemVO> consultarPorHorarioProfessorDia(Integer horarioProfessorDia) throws Exception;
	/**
	 * @author Rodrigo Wind - 09/09/2015
	 * @param horarioProfessorDiaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirHorarioTurmaDiaItem(HorarioProfessorDiaVO horarioProfessorDiaVO, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Rodrigo Wind - 09/09/2015
	 * @param turma
	 * @param disciplina
	 * @param professor
	 * @param sala
	 * @param numeroAula
	 * @param dataInicio
	 * @param dataTermino
	 * @return
	 * @throws Exception
	 */
	List<HorarioProfessorDiaItemVO> consultarParametrizada(Integer turma, Integer disciplina, Integer professor, Integer sala, Integer numeroAula, Date dataInicio, Date dataTermino) throws Exception;
	 
	public String designIReportRelatorio(String layout);
	public String designIReportRelatorioExcel (String layout);
	void excluirPorTurmaPorDisciplina(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception;
	void atualizarNovaDisciplinaHorarioProfessorDiaItemPorAlteracaoGradeCurricularIntegral(Integer turma, Integer disciplina, Integer novaDisciplina, UsuarioVO usuario) throws Exception;
	public void atualizarSituacaoRegistroAulaAutomatico(HorarioProfessorDiaItemVO horarioProfessorDiaItemVO) throws Exception;
}
