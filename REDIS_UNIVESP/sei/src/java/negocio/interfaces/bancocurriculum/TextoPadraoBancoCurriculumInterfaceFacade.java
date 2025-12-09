package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;

public interface TextoPadraoBancoCurriculumInterfaceFacade {

    public TextoPadraoBancoCurriculumVO novo() throws Exception;
    public void incluir(TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception;
    public TextoPadraoBancoCurriculumVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public TextoPadraoBancoCurriculumVO consultarPorTipoUnica(String valorConsulta, boolean controlarAcesso, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void gravarSituacao(TextoPadraoBancoCurriculumVO obj, UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
