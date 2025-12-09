package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface QuestaoListaExercicioInterfaceFacade {

    void incluirQuestaoListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception;
    
    void alterarQuestaoListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception;
    
    List<QuestaoListaExercicioVO> consultarPorListaExercicio(Integer listaExercicio, NivelMontarDados nivelMontarDados);
    
    void validarDados(QuestaoListaExercicioVO questaoListaExercicioVO) throws ConsistirException;
    
	void consultarPorListaExercicioParaRespostaAluno(ListaExercicioVO listaExercicio, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoUnidadeConteudo, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception;
    
    
}
