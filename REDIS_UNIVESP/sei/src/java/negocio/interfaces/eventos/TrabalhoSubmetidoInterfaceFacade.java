package negocio.interfaces.eventos;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.eventos.TrabalhoSubmetidoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface TrabalhoSubmetidoInterfaceFacade {
	

    public TrabalhoSubmetidoVO novo() throws Exception;
    public void incluir(TrabalhoSubmetidoVO obj) throws Exception;
    public void alterar(TrabalhoSubmetidoVO obj) throws Exception;
    public void excluir(TrabalhoSubmetidoVO obj) throws Exception;
    public TrabalhoSubmetidoVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuarios) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorEvento(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorDataSubmissao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}