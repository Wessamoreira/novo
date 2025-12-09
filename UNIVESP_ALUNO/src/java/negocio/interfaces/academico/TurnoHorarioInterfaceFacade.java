/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.DiaSemana;

/**
 *
 * @author Otimize-Not
 */
@Repository
@Scope("singleton")
@Lazy
public interface TurnoHorarioInterfaceFacade {

    public void persistir(TurnoHorarioVO turnoHorarioVO)throws Exception;
    public void incluir(TurnoHorarioVO turnoHorarioVO)throws Exception;
    public void alterar(TurnoHorarioVO turnoHorarioVO)throws Exception;
    public void excluir(TurnoHorarioVO turnoHorarioVO)throws Exception;
    public void excluirTurnoHorarioVOs(Integer turno)throws Exception;
    public void incluirTurnoHorarioVOs(Integer turno, List<TurnoHorarioVO> turnoHorarioVOs)throws Exception;
    public void alterarTurnoHorarioVOs(Integer turno, List<TurnoHorarioVO> turnoHorarioVOs, DiaSemana diaSemana) throws Exception ;
    public List<TurnoHorarioVO> consultarTurnoHorarioVOs(Integer turno, boolean controlarAcesso, UsuarioVO usuario)throws Exception;

    public void validarDadosTurnoHorarioVO(List<TurnoHorarioVO> turnoHorarioVOs, DiaSemana diaSemana) throws ConsistirException ;

    public void consultarTurnoHorarioVOsSeparadoPorDiaSemana(TurnoVO turno) throws Exception ;

    public void realizarCalculoHorarioFinal(TurnoVO turnoVO, TurnoHorarioVO obj) throws ConsistirException ;

    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca(Integer turno, String horario, String diaSemana);
    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxHorario(Integer turno, String horario, String diaSemana);
    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_UltimoHorarioDia(Integer turno, String horario, String diaSemana);
    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxDiaHorario(Integer turno, String diaSemana);
    
    public List<String> consultarDiasSemanaHorarioTurnoPorTurmaCenso(Integer turma, boolean controlarAcesso, UsuarioVO usuario)throws Exception;
    
}
