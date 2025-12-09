package negocio.interfaces.biblioteca;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.AutorVariacaoNomeVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface AutorInterfaceFacade {
    

    public AutorVO novo() throws Exception;
    public void incluir(AutorVO obj, UsuarioVO usuario) throws Exception;
    public void incluir(final AutorVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
    public void alterar(AutorVO obj, final List<AutorVariacaoNomeVO> lista) throws Exception;
    public void excluir(AutorVO obj) throws Exception;
    public AutorVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeAutorOuVariacaoNomeAutor(String nomeAutor, int nivelMontarDados) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List consultarPorCodigoCatalogo(Integer codigoCatalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<AutorVO> consultaRapidaNivelComboBoxPorCodigoCatalogo(Integer catalogo, UsuarioVO usuario);
    public List<AutorVO> consultaRapidaNivelComboBoxPorNome(String valorConsulta, UsuarioVO usuario);
    public List<AutorVO> consultaRapidaNivelComboBoxPorCodigo(Integer valorConsulta, UsuarioVO usuario);
    public AutorVO consultarPorNomeRegistroUnico(String nome, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
}