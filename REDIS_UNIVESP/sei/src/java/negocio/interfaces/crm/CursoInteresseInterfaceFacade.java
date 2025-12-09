package negocio.interfaces.crm;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoInteresseVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CursoInteresseInterfaceFacade {
	

    public CursoInteresseVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public CursoInteresseVO consultarPorCodigoProspect(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CursoInteresseVO> consultarCursosPorCodigoProspect(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void incluirCursoInteresses(Integer codigo, List objetos, UsuarioVO usuario) throws Exception;
    public void alterarCursoInteresses(Integer codigo, List objetos, UsuarioVO usuario) throws Exception;
    public void excluirCursoInteresses(Integer codigo, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void excluirCursoInteressePorProspect(Integer codProspect, UsuarioVO usuario) throws Exception;
    public void alteraCursoInteresse(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception;
    public CursoInteresseVO consultarPorCodigoProspectCodigoCurso(Integer codigoProspect, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void incluir(final CursoInteresseVO obj, UsuarioVO usuario) throws Exception;	
}