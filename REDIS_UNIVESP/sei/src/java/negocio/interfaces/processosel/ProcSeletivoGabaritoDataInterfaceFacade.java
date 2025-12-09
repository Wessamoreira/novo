package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface ProcSeletivoGabaritoDataInterfaceFacade {

	public void incluirProcessoSeletivoGabaritoData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception;
	public void alteraProcessoSeletivoGabaritoData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception;
	public List<ProcSeletivoGabaritoDataVO> consultarPorItemProcSeletivoDataProva(Integer codigo) throws Exception;
	public void validarDados(ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) throws ConsistirException;
    
}
