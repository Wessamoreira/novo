package negocio.interfaces.basico;
import java.util.List;

import negocio.comuns.basico.EmpresaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface EmpresaInterfaceFacade {
	

    public EmpresaVO novo() throws Exception;
    public void incluir(EmpresaVO obj) throws Exception;
    public void alterar(EmpresaVO obj) throws Exception;
    public void excluir(EmpresaVO obj) throws Exception;
    public EmpresaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorRazaoSocial(String valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorNomeCidade(String valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorCNPJ(String valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorInscEstadual(String valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorRG(String valorConsulta, boolean controlarAcesso) throws Exception;
    public List consultarPorCPF(String valorConsulta, boolean controlarAcesso) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}