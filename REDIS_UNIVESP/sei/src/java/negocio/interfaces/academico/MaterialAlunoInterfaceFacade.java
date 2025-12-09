package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MaterialAlunoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MaterialAlunoInterfaceFacade {

    public void incluir(final MaterialAlunoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterar(final MaterialAlunoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluir(MaterialAlunoVO obj) throws Exception;

    public MaterialAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;

    public List consultarPorLocalAula(Integer codigolocalAula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirMaterialAlunos(Integer localAulaPrm, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterarMaterialAlunos(Integer localAula, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluirMaterialAlunos(Integer localAula, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
}
