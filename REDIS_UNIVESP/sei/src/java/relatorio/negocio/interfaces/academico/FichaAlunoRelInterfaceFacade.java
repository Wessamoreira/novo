package relatorio.negocio.interfaces.academico;

import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

import relatorio.negocio.comuns.academico.FichaAlunoRelVO;

public interface FichaAlunoRelInterfaceFacade {

	public List<FichaAlunoRelVO> criarObjeto(String matricula,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

}