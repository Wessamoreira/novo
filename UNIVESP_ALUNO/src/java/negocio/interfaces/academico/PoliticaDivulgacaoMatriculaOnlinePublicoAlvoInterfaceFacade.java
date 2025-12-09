package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface PoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade {

	public void excluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(PoliticaDivulgacaoMatriculaOnlineVO obj, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> lista, String publicoAlvo) throws Exception;

	public void incluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(PoliticaDivulgacaoMatriculaOnlineVO politica, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> lista, UsuarioVO usuarioVO) throws Exception;

	public void alterarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(PoliticaDivulgacaoMatriculaOnlineVO obj, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> lista, UsuarioVO usuarioVO, String publicoAlvo) throws Exception;

	public void removerPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(PoliticaDivulgacaoMatriculaOnlineVO politica, PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaPublicoAlvo, String publicoAlvo);

	public List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> consultarPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(Integer politicaDivulgacaoMatriculaOnline, int nivelMontarDados, String publicoAlvo, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario) throws Exception;

	public void adicionarObjPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaPublicoAlvo,PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, String publicoAlvo, String nivelEducacional, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario, Integer codigoCurso) throws Exception;

	public void excluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj, UsuarioVO usuarioVO) throws Exception;


}