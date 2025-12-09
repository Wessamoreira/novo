package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CensoUnidadeEnsinoVO;
import negocio.comuns.academico.CensoVO;
import negocio.comuns.arquitetura.UsuarioVO;	

public interface CensoUnidadeEnsinoInterfaceFacade {

	void persistir(CensoVO censoVO, UsuarioVO usuarioVO) throws Exception;
	void incluir(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;	
	void excluir(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;	
	List<CensoUnidadeEnsinoVO> consultarPorCenso(Integer censo) throws Exception;
	void carregarUnidadeEnsinoNaoSelecionado(CensoVO censoVO, Integer unidadeEnsinoLogado) throws Exception;
}
