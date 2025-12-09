package negocio.interfaces.contabil;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.DREVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface DREInterfaceFacade {
	

    public DREVO novo() throws Exception;
    public void incluir(DREVO obj) throws Exception;
    public void incluirDRE(List<DREVO> lista, Integer empresa) throws Exception;
    public void alterar(DREVO obj) throws Exception;
    public void excluir(DREVO obj) throws Exception;
    public void moverParaBaixo(DREVO obj, List listaDRE);
    public void moverParaCima(DREVO obj, List listaDRE);
    public DREVO consultarPorChavePrimaria(Integer codigo, Integer unidadeEnsino, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigoAuxililar(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List consultarPorOrdem(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List consultarPorDescricaoPlanoConta(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List consultarPorUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}