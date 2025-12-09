package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialItemVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ProgressaoSalarialItemInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {
	
	public List<ProgressaoSalarialItemVO> consultarProgressaoTabelaItem(Long id) throws Exception;

	public void excluirTodosQueNaoEstaoNaLista(ProgressaoSalarialVO progressaoSalarialVO, List<ProgressaoSalarialItemVO> objetos, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void excluirTodos(ProgressaoSalarialVO progressaoSalarialVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void persistirTodos(ProgressaoSalarialVO progressaoSalarialVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<ProgressaoSalarialItemVO> consultarPorProgressaoSalarial(Integer codigoProgressaoSalarial) throws Exception ;

	public List<ProgressaoSalarialItemVO> consultarPorProgressaoSalarialPorNivel(Integer codigo) throws Exception;

	public BigDecimal consultarSalarioPorNivelFaixaProgressao(Integer nivelSalarial, Integer faixaSalarial, Integer progressaoSalarial );
	
}