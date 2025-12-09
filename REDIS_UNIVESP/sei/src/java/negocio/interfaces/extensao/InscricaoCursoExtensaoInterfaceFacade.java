package negocio.interfaces.extensao;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.extensao.InscricaoCursoExtensaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface InscricaoCursoExtensaoInterfaceFacade {
	

    public InscricaoCursoExtensaoVO novo() throws Exception;
    public void incluir(InscricaoCursoExtensaoVO obj) throws Exception;
    public void alterar(InscricaoCursoExtensaoVO obj) throws Exception;
    public void excluir(InscricaoCursoExtensaoVO obj) throws Exception;
    public InscricaoCursoExtensaoVO consultarPorChavePrimaria(Integer nrInscricao,UsuarioVO usuario) throws Exception;
    public List consultarPorNrInscricao(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorCursoExtensao(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorHora(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorValorTotal(Double valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorTipoInscricao(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}