/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisponibilidadeHorarioTurmaProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaLOGVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Rodrigo
 */
public interface HorarioTurmaLOGInterfaceFacade {

    public void incluir(final HorarioTurmaLOGVO obj) throws Exception;

    public List<HorarioTurmaLOGVO> consultarLogPorTurmaPeriodoAnoSemestre(Integer turma, Date dataInicio, Date dataFim,
            String ano, String semestre) throws Exception;

    public void executarLogRemocaoHorarioTurmaDiaItemTelaHorarioDetalhadoDoDiaTelaPrincipal(HorarioTurmaDiaItemVO hrProfDiaItem, DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioProfessorVO, HorarioTurmaVO horarioTurmaVO, Integer professorRemovido, Integer disciplinaRemovida, String nomeProfessor, String nomeDisciplina, UsuarioVO usuarioLogado) throws Exception;

    public void executarLogAlteracaoDisciplinaProfessorAtualParaDisciplinaProfessorSubstituto(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuarioLogado, Integer nrAula) throws Exception;

    public void executarLogGravarAlteracao(HorarioTurmaDiaVO obj, UsuarioVO usuarioLogado) throws Exception;

    public void executarLogGravarInclusao(HorarioTurmaDiaVO obj, UsuarioVO usuarioLogado) throws Exception;

    public void executarLogExclusaoProgramacaoAula(HorarioTurmaVO obj, UsuarioVO usuarioLogado) throws Exception;
    
    public void executarLogExclusaoHorarioTurmaDiaTelaAlterarExcluirHorarioPorProfessorTelaModal(HorarioTurmaDiaVO obj, HorarioTurmaDiaItemVO horarioItem,  UsuarioVO usuarioLogado) throws Exception;

	void realizarCriacaoLogHorarioTurma(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuarioLogado, String acao, String resultadoAcao) throws Exception;
}
