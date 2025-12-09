package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MaterialCursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MaterialCursoInterfaceFacade {

    public void incluir(final MaterialCursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterar(final MaterialCursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluir(MaterialCursoVO obj) throws Exception;

    public MaterialCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;

    public List consultarPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirMaterialCursos(Integer cursoPrm, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterarMaterialCursos(Integer curso, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluirMaterialCursos(Integer curso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
}
