package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.TramiteCotacaoCompraVO;

public interface TramiteCotacaoCompraInterfaceFacade {	

	public void incluir(TramiteCotacaoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterar(TramiteCotacaoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(TramiteCotacaoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<TramiteCotacaoCompraVO> consultarPornome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<TramiteCotacaoCompraVO> consultarSituacaoAtivaTramitePadrao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);
	
	public List<TramiteCotacaoCompraVO> consultarPorId(int valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void popularListaDepartamentoTransite(TramiteCotacaoCompraVO obj, UsuarioVO usuario) throws Exception;
	
	public DepartamentoTramiteCotacaoCompraVO consultarDepartamentoTramitePorCodigo(int codigo, UsuarioVO usuario) throws Exception;

	public boolean consultarUsoTramite(TramiteCotacaoCompraVO obj, UsuarioVO usuarioLogado);

	List<TramiteCotacaoCompraVO> consultarSituacaoAtiva(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	TramiteCotacaoCompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}