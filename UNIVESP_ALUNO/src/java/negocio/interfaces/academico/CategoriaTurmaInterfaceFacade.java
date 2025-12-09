package negocio.interfaces.academico;
import java.util.List;

import negocio.comuns.academico.CategoriaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CategoriaTurmaInterfaceFacade {
	

    public CategoriaTurmaVO novo() throws Exception;
    public void incluir(CategoriaTurmaVO obj,UsuarioVO usuario) throws Exception;
    public void alterar(CategoriaTurmaVO obj,UsuarioVO usuario) throws Exception;
    public void excluir(CategoriaTurmaVO obj, UsuarioVO usuarioVO) throws Exception;
    public CategoriaTurmaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CategoriaTurmaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List <CategoriaTurmaVO> consultarPorTurma(String turma, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception ;
    public List <CategoriaTurmaVO> consultarPorTipoCategoria(String tipoCategoria, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception ;
    public void setIdEntidade(String aIdEntidade);
}