package negocio.interfaces.pesquisa;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.GrupoPesquisaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface GrupoPesquisaInterfaceFacade {
	

    public GrupoPesquisaVO novo() throws Exception;
    public void incluir(GrupoPesquisaVO obj) throws Exception;
    public void alterar(GrupoPesquisaVO obj) throws Exception;
    public void excluir(GrupoPesquisaVO obj) throws Exception;
    public GrupoPesquisaVO consultarPorChavePrimaria(Integer codigo,int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataCriacao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigoFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}