package relatorio.negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.crm.ProdutividadeConsultorRelVO;

public interface ProdutividadeConsultorRelInterfaceFacade {

	List<ProdutividadeConsultorRelVO> criarObjeto(ProdutividadeConsultorRelVO filtro, UsuarioVO usuarioVO) throws Exception;

}
