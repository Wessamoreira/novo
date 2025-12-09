package negocio.interfaces.academico;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import webservice.servicos.BannerObject;

public interface PoliticaDivulgacaoMatriculaOnlineInterfaceFacade {

	public PoliticaDivulgacaoMatriculaOnlineVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws Exception;

	public void persistir(PoliticaDivulgacaoMatriculaOnlineVO obj, UsuarioVO usuarioVO, String publicoAlvo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<PoliticaDivulgacaoMatriculaOnlineVO> consultarPorNomePolitica(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws Exception;

	public void excluirPoliticaDivulgacaoMatriculaOnline(PoliticaDivulgacaoMatriculaOnlineVO obj, UsuarioVO usuario) throws Exception;

	public void upLoadLogoPoliticaDivulgacaoMatriculaOnline(FileUploadEvent upload, PoliticaDivulgacaoMatriculaOnlineVO politica, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoPoliticaDivulgacaoMatriculaOnlineAtivo(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoPoliticaDivulgacaoMatriculaOnlineInativo(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<PoliticaDivulgacaoMatriculaOnlineVO> consultarPorCodigoCurso(Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PoliticaDivulgacaoMatriculaOnlineVO> consultarBanners(UsuarioVO usuario) throws Exception;

	Integer consultarCodigoCurso(Integer codigoBanner, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<BannerObject> consultarBannersAtivosDivulgacaoParaComunidadeMatriculaOnlineExterna(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

}