package negocio.interfaces.pesquisa;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.PublicacaoPesquisaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PublicacaoPesquisaInterfaceFacade {
	

    public PublicacaoPesquisaVO novo() throws Exception;
    public void incluir(PublicacaoPesquisaVO obj) throws Exception;
    public void alterar(PublicacaoPesquisaVO obj) throws Exception;
    public void excluir(PublicacaoPesquisaVO obj) throws Exception;
    public PublicacaoPesquisaVO consultarPorChavePrimaria(Integer codigo,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorTituloPublicacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorTipoPesquisador(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorTipoPublicacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeProjetoPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeLinhaPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}