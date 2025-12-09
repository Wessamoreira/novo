package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaAtualizacaoDisciplinaLogVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public interface TurmaAtualizacaoDisciplinaLogInterfaceFacade {
    
    public void incluir(final TurmaAtualizacaoDisciplinaLogVO obj) throws Exception;
    public void executarLogAtualizacaoDisciplinaTurma(TurmaVO obj, List<TurmaDisciplinaVO> listaTurmaDisciplinaVOs, UsuarioVO usuarioLogado) throws Exception;
	void registrarInstrucaoLog(TurmaAtualizacaoDisciplinaLogVO tadLog, String instrucao, boolean quebraLinha);
	void registrarLinhaTracejada(TurmaAtualizacaoDisciplinaLogVO tadLog, boolean quebraLinha);
	void executarInclusaoLog(TurmaAtualizacaoDisciplinaLogVO log, TurmaVO turma, UsuarioVO usuarioLogado) throws Exception;
	List<TurmaAtualizacaoDisciplinaLogVO> consultaRapidaPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
    
}
