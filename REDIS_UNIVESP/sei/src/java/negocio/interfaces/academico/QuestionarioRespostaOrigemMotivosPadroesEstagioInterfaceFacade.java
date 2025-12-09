package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface QuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade {
	
	/**
	 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
	 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
	 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
	 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
	 */
	public void persistir(List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> lista, UsuarioVO usuarioVO);
	
    public QuestionarioRespostaOrigemMotivosPadroesEstagioVO novo() throws Exception;

	public QuestionarioRespostaOrigemMotivosPadroesEstagioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> consultarPorQuestionarioOrigem(Integer codQuestionario, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
}
