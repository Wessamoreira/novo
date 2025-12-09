package negocio.interfaces.processosel;

import java.math.BigDecimal;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoGabaritoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoVO;

public interface GabaritoInterfaceFacade {
	List<GabaritoVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO);

	List<GabaritoVO> consultaRapidaPorDescricao(String descricao, UsuarioVO usuarioVO);

	GabaritoVO consultaRapidaPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;

	void excluir(GabaritoVO obj, UsuarioVO usuario) throws Exception;

	void alterar(final GabaritoVO obj, UsuarioVO usuario) throws Exception;

	void incluir(final GabaritoVO obj, UsuarioVO usuario) throws Exception;

	void adicionarGabaritoResposta(GabaritoVO obj, BigDecimal valorNotaPorQuestao, UsuarioVO usuarioVO);

	void persistir(GabaritoVO obj, UsuarioVO usuarioVO) throws Exception;

	void validarDados(GabaritoVO obj) throws Exception;

	void editarColunaGabaritoResposta(GabaritoVO obj, UsuarioVO usuarioVO);

	GabaritoVO consultarCodigoGabaritoPorInscricao(Integer inscricao, UsuarioVO usuarioVO) throws Exception;

	void iniciarlizarDadosListaGabaritoResposta(GabaritoVO obj, UsuarioVO usuarioVO);

	List<GabaritoVO> consultaRapidaPorDescricaoTipoGabarito(String descricao, TipoGabaritoEnum tipoGabaritoEnum, UsuarioVO usuarioVO);

	/**
	 * Responsável por executar a definição do gabarito a ser utilizado levando em consideração se o mesmo está vínculado a um
	 * grupoDisciplinaProcSeletivo, se o ProcSeletivoCurso também está vínculado a um grupoDisciplinaProcSeletivo e se os dois tem o
	 * grupoDisciplinaProcSeletivo em comum.
	 * 
	 * @author Wellington - 5 de jan de 2016
	 * @param inscricao
	 * @return
	 * @throws Exception
	 */
	Integer consultarCodigoGabaritoPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(Integer inscricao) throws Exception;
}
