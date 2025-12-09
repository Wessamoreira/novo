package relatorio.negocio.interfaces.academico;

import java.util.List;

import relatorio.negocio.comuns.academico.InformacaoProfessorRelVO;

public interface InformacaoProfessorRelInterfaceFacade {

	public List<InformacaoProfessorRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer pessoa, Integer disciplina);
	
}
