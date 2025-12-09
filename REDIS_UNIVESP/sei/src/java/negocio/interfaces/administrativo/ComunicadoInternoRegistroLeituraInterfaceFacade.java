package negocio.interfaces.administrativo;
import java.util.List;

import negocio.comuns.administrativo.ComunicadoInternoRegistroLeituraVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ComunicadoInternoRegistroLeituraInterfaceFacade {
	

    public ComunicadoInternoRegistroLeituraVO novo() throws Exception;
    public void incluir(ComunicadoInternoRegistroLeituraVO obj) throws Exception;
    public void alterar(ComunicadoInternoRegistroLeituraVO obj) throws Exception;
    public void excluir(ComunicadoInternoRegistroLeituraVO obj) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorComunicadoInterno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDestinatario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}