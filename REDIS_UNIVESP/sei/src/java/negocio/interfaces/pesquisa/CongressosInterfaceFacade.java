package negocio.interfaces.pesquisa;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.CongressosVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CongressosInterfaceFacade {
	

    public CongressosVO novo() throws Exception;
    public void incluir(CongressosVO obj) throws Exception;
    public void alterar(CongressosVO obj) throws Exception;
    public void excluir(CongressosVO obj) throws Exception;
    public CongressosVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorDataInicialRealizacao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorDataFinalRealizacao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorDataInicialInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorDataFinalInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorPromotor(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorSite(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorLocalRealizacao(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}