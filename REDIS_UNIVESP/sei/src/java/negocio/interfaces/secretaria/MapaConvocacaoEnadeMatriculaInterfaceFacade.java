package negocio.interfaces.secretaria;

import java.util.List;

import org.springframework.jca.cci.InvalidResultSetAccessException;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.secretaria.enumeradores.SituacaoConvocadosEnadeEnum;

public interface MapaConvocacaoEnadeMatriculaInterfaceFacade {
	
	public void excluirPorMapaConvocacaoEnade(Integer mapaConvocacaoEnade, UsuarioVO usuario) throws Exception;
	
	public void incluirMapaConvocacaoEnadeMatriculaVOs(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaVOs, SituacaoConvocadosEnadeEnum situacaoConvocadosEnade, UsuarioVO usuario) throws Exception;
	
	public void alterarMapaConvocacaoEnadeMatriculaVOs(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaVOs, SituacaoConvocadosEnadeEnum situacaoConvocadosEnade, UsuarioVO usuario) throws Exception;
	
	public void carregarDados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario, String ano) throws Exception;
	public void consultaRapidaPorMapaConvocacaoEnade(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuarioVO, String ano) throws Exception;
//	public void montarDadosAlunosEnade(final MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuario) throws Exception;
	public List<MatriculaVO> consultaRapidaMapaconvocacaoEnadeMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO,  Integer curso, String situacaoMatricula, UsuarioVO usuarioVO) throws InvalidResultSetAccessException, Exception;
	public MapaConvocacaoEnadeMatriculaVO consultaPorMapaConvocacaoEnade(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuarioVO, String ano) throws Exception;
}
