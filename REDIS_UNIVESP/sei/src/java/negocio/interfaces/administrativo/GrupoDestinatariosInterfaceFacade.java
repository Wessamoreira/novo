package negocio.interfaces.administrativo;

import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;

public interface GrupoDestinatariosInterfaceFacade {

	public abstract void incluir(final GrupoDestinatariosVO obj) throws Exception;

	public abstract void alterar(final GrupoDestinatariosVO obj) throws Exception;

	public abstract void excluir(GrupoDestinatariosVO obj) throws Exception;

	public abstract List<GrupoDestinatariosVO> consultarPorNomeGrupo(String nomeGrupo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public abstract List<GrupoDestinatariosVO> consultarPorResponsavelCadastro(Integer codigoResponsavelCadastro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public abstract List<GrupoDestinatariosVO> consultarPorCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public abstract GrupoDestinatariosVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<SelectItem> consultarDadosListaSelectItem(Obrigatorio obrigatorio);

	GrupoDestinatariosVO consultarGrupoDestinatarioQuandoOuvidoriaForMalAvaliadaPorUnidadeEnsino(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	SqlRowSet consultarGrupoDestinatariosNotificacaoDuvidasNaoRespondidasProfessor() throws Exception;

}