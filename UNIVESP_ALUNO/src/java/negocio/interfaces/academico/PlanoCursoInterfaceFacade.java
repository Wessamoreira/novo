package negocio.interfaces.academico;
import java.util.List;

import negocio.comuns.academico.PlanoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PlanoCursoInterfaceFacade {
	

    public PlanoCursoVO novo() throws Exception;
    public void incluir(PlanoCursoVO obj) throws Exception;    
    public void alterar(PlanoCursoVO obj) throws Exception;    
    public void excluir(PlanoCursoVO obj) throws Exception;
    public PlanoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorObjetivosEspecificos(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorObjetivosGerais(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorEstrategiasAvaliacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorMetodologia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void incluirAlterarPlanoCurso(PlanoCursoVO obj, UsuarioVO usuarioVO) throws Exception;
}