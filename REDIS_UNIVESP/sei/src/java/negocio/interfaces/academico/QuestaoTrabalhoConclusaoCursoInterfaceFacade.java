package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.QuestaoTrabalhoConclusaoCursoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface QuestaoTrabalhoConclusaoCursoInterfaceFacade {
	
	public void incluirQuestaoFormatacao(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception;
	
	public void incluirQuestaoConteudo(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception ;
	
	void alterarQuestaoConteudo(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception;
	
	void alterarQuestaoFormatacao(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception;
	
	public List<QuestaoTrabalhoConclusaoCursoVO> consultarPorTrabalhoConclusaoCurso(Integer trabalhoConclusaoCurso, String origemQuestao) throws Exception;
	
	void validarDados(QuestaoTrabalhoConclusaoCursoVO questaoTrabalhoConclusaoCursoVO) throws ConsistirException;

	QuestaoTrabalhoConclusaoCursoVO novo() throws Exception;

}
