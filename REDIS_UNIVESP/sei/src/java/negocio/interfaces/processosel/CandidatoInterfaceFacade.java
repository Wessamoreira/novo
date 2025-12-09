package negocio.interfaces.processosel;
import java.util.List;

import negocio.comuns.processosel.CandidatoVOpp;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CandidatoInterfaceFacade {
	

    public CandidatoVOpp novo() throws Exception;
    public void incluir(CandidatoVOpp obj) throws Exception;
    public void alterar(CandidatoVOpp obj) throws Exception;
    public void excluir(CandidatoVOpp obj) throws Exception;
    public CandidatoVOpp consultarPorChavePrimaria(Integer codigo) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}