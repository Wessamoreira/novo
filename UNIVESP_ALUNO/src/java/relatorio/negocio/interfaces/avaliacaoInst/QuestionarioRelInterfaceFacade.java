package relatorio.negocio.interfaces.avaliacaoInst;

import java.util.List;

import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.QuestionarioVO;

public interface QuestionarioRelInterfaceFacade {

	public List<QuestionarioVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception;
	public List<QuestionarioRelVO> criarObjeto(QuestionarioVO questionarioVO, UsuarioVO usuarioVO) throws Exception;
}
