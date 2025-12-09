package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;

public interface AvaliacaoOnlineTemaAssuntoInterfaceFacade {
	
	void persistir(AvaliacaoOnlineVO avaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception;
	
	List<AvaliacaoOnlineTemaAssuntoVO> consultarAvaliacaoOnlineTemaAssuntoPorAvaliacaoOnline(Integer codigoAvaliacaoOnline, UsuarioVO usuarioVO) throws Exception;
	
	List<AvaliacaoOnlineTemaAssuntoVO> consultarAvaliacaoOnlineTemaAssuntoAptoParaAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO,  DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UsuarioVO usuarioVO) throws Exception;

}
