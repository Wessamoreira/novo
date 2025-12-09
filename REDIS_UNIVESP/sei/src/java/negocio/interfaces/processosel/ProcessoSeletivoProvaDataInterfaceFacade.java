package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface ProcessoSeletivoProvaDataInterfaceFacade {

    void incluirProcessoSeletivoProvaData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception;
    void alteraProcessoSeletivoProvaData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception;
    List<ProcessoSeletivoProvaDataVO> consultarPorItemProcSeletivoDataProva(Integer codigo) throws Exception;
    void validarDados(ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) throws ConsistirException;
    public void validarDadosGabaritoData(ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) throws ConsistirException;
    
}
