package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EntregaRequisicaoItemVO;
import negocio.comuns.compras.EntregaRequisicaoVO;

public interface EntregaRequisicaoItemInterfaceFacade {

	public void incluirEntregaRequisicaoItems(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception;	
	
	public List<EntregaRequisicaoItemVO> consultarEntregaRequisicaoItems(Integer entregaRequisicao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public EntregaRequisicaoItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

}