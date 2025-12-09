package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import relatorio.negocio.comuns.academico.PlanoDisciplinaRelVO;

public interface PlanoDisciplinaRelInterfaceFacade {
	
	public List<PlanoEnsinoVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, Integer gradeCurricular, Integer disciplina, String ano, String semetre, UsuarioVO usuarioVO,
			PessoaVO professor, PeriodicidadeEnum periodicidadeEnum, String situacao, Integer codigoQuestionarioPlanoEnsino) throws Exception;
	public List consultarDisciplina(Integer gradeCurricular, String campoConsultaDisciplina, String valorConsultaDisciplina, UsuarioVO usuarioVO) throws Exception;
	List<PlanoDisciplinaRelVO> realizarGeracaoRelatorioPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	void realizarPreenchimentoDadosGradeCurricularPlanoEnsino(PlanoEnsinoVO planoEnsinoVO) throws Exception;
	
	public PlanoDisciplinaRelVO realizarGeracaoRelatorioPlanoEnsinoSintetico(PlanoEnsinoVO planoEnsinoVO,
			UsuarioVO usuarioVO) throws Exception;

}
