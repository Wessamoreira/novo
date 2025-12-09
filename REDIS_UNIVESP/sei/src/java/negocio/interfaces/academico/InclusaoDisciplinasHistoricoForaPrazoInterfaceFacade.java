package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.InclusaoDisciplinasHistoricoForaPrazoVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface InclusaoDisciplinasHistoricoForaPrazoInterfaceFacade {

    public void setIdEntidade(String idEntidade);

    public void incluirListaInclusaoDisciplinasHistoricoForaPrazoVO(InclusaoHistoricoForaPrazoVO obj) throws Exception;

    public void excluirPorInclusaoHistoricoForaPrazo(InclusaoHistoricoForaPrazoVO obj) throws Exception;

    public List<InclusaoDisciplinasHistoricoForaPrazoVO> consultarPorInclusaoHistoricoForaPrazo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public void validarDados(InclusaoDisciplinasHistoricoForaPrazoVO obj) throws Exception;
}
