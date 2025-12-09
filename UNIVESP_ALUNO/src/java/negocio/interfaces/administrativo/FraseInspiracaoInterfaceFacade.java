package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface FraseInspiracaoInterfaceFacade {
	

    public FraseInspiracaoVO novo() throws Exception;
    public void incluir(FraseInspiracaoVO obj, boolean controleAcesso,UsuarioVO usuario) throws Exception;
    public void alterar(FraseInspiracaoVO obj) throws Exception;
    public void excluir(FraseInspiracaoVO obj) throws Exception;
    public FraseInspiracaoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorFrase(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorAutor(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public FraseInspiracaoVO consultarFraseRandom(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}