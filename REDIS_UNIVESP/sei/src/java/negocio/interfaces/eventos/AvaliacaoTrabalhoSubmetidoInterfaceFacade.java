package negocio.interfaces.eventos;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.eventos.AvaliacaoTrabalhoSubmetidoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface AvaliacaoTrabalhoSubmetidoInterfaceFacade {
	

    public AvaliacaoTrabalhoSubmetidoVO novo() throws Exception;
    public void incluir(AvaliacaoTrabalhoSubmetidoVO obj) throws Exception;
    public void alterar(AvaliacaoTrabalhoSubmetidoVO obj) throws Exception;
    public void excluir(AvaliacaoTrabalhoSubmetidoVO obj) throws Exception;
    public AvaliacaoTrabalhoSubmetidoVO consultarPorChavePrimaria(Integer codigo,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigoMembroComissaoCientifica(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorEventoTrabalhoSubmetido(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}