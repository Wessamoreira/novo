package negocio.interfaces.academico;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TemaAssuntoDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TemaAssuntoDisciplinaInterfaceFacade {

	void incluir(TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	TemaAssuntoDisciplinaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void incluirTemasAssuntoDisciplina(List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinaVOs, UsuarioVO usuarioVO) throws Exception;

	void excluir(Integer codigoDisciplina, Integer codigoTemaAssunto, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluirTodosTemaAssuntoDisciplina(Integer codigoDisciplina, List<TemaAssuntoVO> temaAssuntoVOs, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void adicionarTemaAssuntoTemaAssuntoDisciplina(TemaAssuntoVO temaAssuntoVO, List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinaVOs, List<TemaAssuntoVO> temaAssuntoVOs, DisciplinaVO disciplinaVO, UsuarioVO usuarioVO) throws Exception;
}
