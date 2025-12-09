package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PerguntaChecklistOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PerguntaChecklistOrigemInterfaceFacade {

	void persistir(List<PerguntaChecklistOrigemVO> lista, UsuarioVO usuarioVO);

	List<PerguntaChecklistOrigemVO> consultarPorPerguntaRespostaOrigem(Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
