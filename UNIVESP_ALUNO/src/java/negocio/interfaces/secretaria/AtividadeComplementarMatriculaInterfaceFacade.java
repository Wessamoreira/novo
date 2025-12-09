/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.AtividadeComplementarMatriculaVO;
import negocio.comuns.secretaria.AtividadeComplementarVO;

/**
 *
 * @author Otimize-04
 */
public interface AtividadeComplementarMatriculaInterfaceFacade {
    public List<AtividadeComplementarMatriculaVO> consultarHoraComplementarAluno(AtividadeComplementarVO atividadeComplementarVO, TurmaVO turma, Integer disciplina, String ano, String semestre, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean permitirRealizarLancamentoAlunoPendenteFinanceiro, boolean trazerAlunoTranferencia, UsuarioVO usuarioVO) throws Exception;
    public void incluirAtividadeComplementarMatricula(Integer atividadeComplementar, List<AtividadeComplementarMatriculaVO> atividadeComplementarMatriculaVOs, UsuarioVO usuario) throws Exception;
    public void alterarAtividadeComplementarMatricula(Integer atividadeComplementar, List<AtividadeComplementarMatriculaVO> atividadeComplementarMatriculaVOs, UsuarioVO usuario) throws Exception;
    public void excluirAtividadeComplementarMatriculaVOs(Integer atividadeComplementar,  List<AtividadeComplementarMatriculaVO> atividadeComplementarVOs, UsuarioVO usuario) throws Exception;
    public AtividadeComplementarMatriculaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;
    public List consultarAtividadeComplementarMatriculaPorAtividadeComplementar(Integer atividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public void excluirComBaseNaMatricula(String matricula, UsuarioVO usuario) throws Exception;    	
}
