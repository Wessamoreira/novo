package negocio.interfaces.secretaria;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMapaConvocacaoEnadeEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface MapaConvocacaoEnadeInterfaceFacade {
	public void incluir(final MapaConvocacaoEnadeVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final MapaConvocacaoEnadeVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(MapaConvocacaoEnadeVO obj,  MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuario) throws Exception;

	public void persistir(MapaConvocacaoEnadeVO obj, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaVO> consultarAlunosMapaConvocacaoEnade(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;

	public void carregarDados(MapaConvocacaoEnadeVO obj, UsuarioVO usuario, Boolean arquivoTXT) throws Exception;

	public void carregarDados(MapaConvocacaoEnadeVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario, Boolean arquivoTXT) throws Exception;

	public void realizarMudancaAlunoIngressanteParaDispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception ;

	public void realizarMudancaAlunoDispensadoParaIngressante(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception;
	
	public void realizarMudancaAlunoDispensadoParaConcluinte(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception;

	public void realizarMudancaAlunoConcluinteParadispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<MapaConvocacaoEnadeVO> consultar(String campoConsulta, String valorConsulta, Date dataInicial, Date dataFinal, Integer unidadeEnsino, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados) throws Exception;
	
	public void validarDadosConsulta(Integer curso) throws Exception;
	
	public void inicializarDadosTotalizadorLista(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO);
	
	public void alterarSituacaoMApaConvocacaoPorCodigo(final Integer mapaConvocacaoEnade, final SituacaoMapaConvocacaoEnadeEnum situacao, UsuarioVO usuario) throws Exception;
	
	public void montarListaMapaconvocacaoPorEnade(EnadeVO enadeVO, List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOs,MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO,  List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOErros , UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;
	
	public void realizarGeracaoArquivoMapaConvocacaoEnade(final MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario,	ConfiguracaoGeralSistemaVO configuracaoGeralSistema, EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception;
	
	public MapaConvocacaoEnadeVO consultarEnadeCursoParaTXT(String campoConsulta, Integer codigoEnade, Date dataInicial, Date dataFinal, Integer unidadeEnsino, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados,  Integer codigoCurso) throws Exception;

	public MapaConvocacaoEnadeVO consultaPorChavePrimariaDadosCompletos(Integer codMapaConvocacaEnade, UsuarioVO usuario) throws Exception;
	
	public MapaConvocacaoEnadeVO consultaRapidaPorEnadeParaTXT(Integer codigoEnade, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer codigoCurso) throws Exception;
	
	public List<MapaConvocacaoEnadeVO> consultaRapidaPorEnadeCurso(Integer enade, Integer enadeCurso, Integer mapaConvocacaoEnade,  boolean controlarAcesso, boolean validarDados, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarMapaConvocacaoEnadeArquivoAlunoTxt(final MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario, Integer arquivoalunoingressante,  Integer arquivoAlunoconcluinte,  ConfiguracaoGeralSistemaVO configuracaoGeralSistema, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO) throws Exception;

	public void excluirArquivoTxt(MapaConvocacaoEnadeVO obj, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuario) throws Exception;
	
	public void processarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao(List<MatriculaVO> listaMatriculaVOs, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception;
}

