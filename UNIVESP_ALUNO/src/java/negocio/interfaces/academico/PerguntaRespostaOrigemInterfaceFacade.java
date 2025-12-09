package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface PerguntaRespostaOrigemInterfaceFacade {
	
	/**
	 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
	 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
	 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
	 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
	 */
	
    public PerguntaRespostaOrigemVO novo() throws Exception;

    public void incluir(PerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(PerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

	void validarDadosPerguntaRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO) throws ConsistirException;
	
	public PerguntaRespostaOrigemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<PerguntaRespostaOrigemVO> consultarPorQuestionarioOrigem(Integer codQuestionario, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<PerguntaRespostaOrigemVO> consultarPorQuestionarioPlanoEnsino(Integer codQuestionario, Integer codPlanoEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluir(PerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

	List<PerguntaRespostaOrigemVO> consultarPorQuestionarioRequisicao(Integer codQuestionario, Integer codRequisicao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void corrigirEncondingEstagio(Date dataInicioCorrecaoEncode, Date dataFimCorrecaoEncode, UsuarioVO usuarioVO) throws Exception;
	
	

}
