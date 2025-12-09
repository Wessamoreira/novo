package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberLogVO;
import negocio.comuns.financeiro.ContaReceberVO;

public interface ContaReceberLogInterfaceFacade {

    public void incluir(ContaReceberLogVO obj) throws Exception;

    public void preencherContaReceberLog(ContaReceberVO obj, String operacao, UsuarioVO usuario) throws Exception;

    

    
}
