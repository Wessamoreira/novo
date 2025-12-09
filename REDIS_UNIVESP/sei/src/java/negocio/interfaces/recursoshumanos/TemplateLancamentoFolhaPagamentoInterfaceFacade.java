package negocio.interfaces.recursoshumanos;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;

public interface TemplateLancamentoFolhaPagamentoInterfaceFacade {

	public void persistir(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public TemplateLancamentoFolhaPagamentoVO consultarTemplatePorLancamentoFolha(Integer codigo) throws Exception;

	
	/**
	 * Retorna a String de consulta de acordo com o template informado
	 *  
	 * @param obj
	 * 
	 * @return ex.: " AND fc.codigo = obj.getFuncionarioCargoVO().getCodigo() "
	 */
	public String getFiltrosDoTemplate(TemplateLancamentoFolhaPagamentoVO obj);
	
	public TemplateLancamentoFolhaPagamentoVO consultarPorChavePrimaria(Integer id, int nivelMontarDados) throws Exception;
}