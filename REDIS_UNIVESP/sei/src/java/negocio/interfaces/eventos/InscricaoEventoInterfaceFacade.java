package negocio.interfaces.eventos;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.eventos.InscricaoEventoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface InscricaoEventoInterfaceFacade {
	

    public InscricaoEventoVO novo() throws Exception;
    public void incluir(InscricaoEventoVO obj) throws Exception;
    public void alterar(InscricaoEventoVO obj) throws Exception;
    public void excluir(InscricaoEventoVO obj) throws Exception;
    public InscricaoEventoVO consultarPorChavePrimaria(Integer nrInscricao,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNrInscricao(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeEvento(String valorConsulta,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorTotal(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorTipoInscricao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}