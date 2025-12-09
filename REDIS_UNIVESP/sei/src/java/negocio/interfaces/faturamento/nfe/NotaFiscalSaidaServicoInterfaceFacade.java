package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;

public interface NotaFiscalSaidaServicoInterfaceFacade {

	NotaFiscalSaidaServicoVO novo(UsuarioVO usuarioLogado) throws Exception;

	void incluir(NotaFiscalSaidaServicoVO obj, UsuarioVO usuarioLogado) throws Exception;

	void alterar(NotaFiscalSaidaServicoVO obj, UsuarioVO usuarioLogado) throws Exception;

	void excluir(NotaFiscalSaidaServicoVO obj, UsuarioVO usuarioLogado) throws Exception;

	List<NotaFiscalSaidaServicoVO> consultarNotaFiscalSaidaServicos(Integer notaFiscalSaida, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Wellington - 8 de abr de 2016 
	 * @param codigoPrm
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	NotaFiscalSaidaServicoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 5 de set de 2016 
	 * @param notaFiscalSaidaPrm
	 * @param objetos
	 * @param usuarioLogado
	 * @throws Exception 
	 */
	void incluirNotaFiscalSaidaServicos(Integer notaFiscalSaidaPrm, List<NotaFiscalSaidaServicoVO> objetos, UsuarioVO usuarioLogado) throws Exception;

}
