/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ChoqueHorarioVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisponibilidadeHorarioTurmaProfessorVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaSemanalVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ProgramacaoAulaResumoSemanaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoValidacaoChoqueHorarioEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author rodrigo
 */
public interface HorarioTurmaDisciplinaProgramadaInterfaceFacade {
	
	public void persistir(HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO, UsuarioVO usuarioVO) throws Exception;
	public void incluir(final HorarioTurmaDisciplinaProgramadaVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final HorarioTurmaDisciplinaProgramadaVO obj, UsuarioVO usuario) throws Exception;
	public HorarioTurmaDisciplinaProgramadaVO consultarPorDisciplinaHorarioTurma(HorarioTurmaDisciplinaProgramadaVO obj, UsuarioVO usuario) throws Exception;
    
}