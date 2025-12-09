package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;

public interface DefinirResponsavelFinanceiroInterfaceFacade {
	
	public void executarDefinirAlterarResponsavelFinanceiro(List<ContaReceberVO> contaReceberVOs, PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception;

}
