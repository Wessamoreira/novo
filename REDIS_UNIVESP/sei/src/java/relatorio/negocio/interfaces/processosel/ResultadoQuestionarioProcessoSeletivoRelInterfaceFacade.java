package relatorio.negocio.interfaces.processosel;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ResultadoQuestionarioProcessoSeletivoRelInterfaceFacade {

	public AvaliacaoInstucionalRelVO consultarDadosGeracaoRelatorio(Integer processoSeletivo, Integer unidadeEnsinoCurso, Integer dataProva, Integer sala, Boolean trazerSomenteCandidatosConfirmados, UsuarioVO usuario) throws Exception;
	
	public QuestionarioRelVO consultarDadosGeracaoRelatorioGrafico(Integer processoSeletivo, Integer unidadeEnsinoCurso, Integer dataProva, Integer sala, Boolean trazerSomenteCandidatosConfirmados, UsuarioVO usuario) throws Exception;
	
}
