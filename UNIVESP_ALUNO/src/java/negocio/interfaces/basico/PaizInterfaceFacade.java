package negocio.interfaces.basico;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PaizVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PaizInterfaceFacade {
	

//    public PaizVO novo() throws Exception;
    public void incluir(PaizVO obj,UsuarioVO usuario) throws Exception;
    public void alterar(PaizVO obj) throws Exception;
    public void excluir(PaizVO obj) throws Exception;
    public PaizVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<PaizVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<PaizVO> consultarPorNome(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void imprimirObjeto() throws Exception;
	PaizVO consultarPorNacionalidade(String nacionalidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	PaizVO consultarPorChavePrimariaUnico(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
}