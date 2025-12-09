package negocio.interfaces.pesquisa;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.PesquisadorLinhaPesquisaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PesquisadorLinhaPesquisaInterfaceFacade {
	

    public PesquisadorLinhaPesquisaVO novo() throws Exception;
    public void incluir(PesquisadorLinhaPesquisaVO obj) throws Exception;
    public void alterar(PesquisadorLinhaPesquisaVO obj) throws Exception;
    public void excluir(PesquisadorLinhaPesquisaVO obj) throws Exception;
    public PesquisadorLinhaPesquisaVO consultarPorChavePrimaria(Integer codigo,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorTipoPesquisador(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigoPesquisadorConvidado(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDataFiliacao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeLinhaPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}