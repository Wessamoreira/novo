package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.segmentacao.SegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;

public interface SegmentacaoProspectInterfaceFacade {

	public void adicionarObjSegmentacaoVOs(SegmentacaoProspectVO segmentacaoProspect, SegmentacaoOpcaoVO segmentacaoOpcao) throws Exception;

	public void incluir(SegmentacaoProspectVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<SegmentacaoProspectVO> consultarSegmento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirSegmentacaoProspect(SegmentacaoProspectVO obj, UsuarioVO usuarioVO) throws Exception;

	public Boolean consultarSegmentacaoPorDescricao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public SegmentacaoProspectVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<SegmentacaoProspectVO> consultarSegmentosAtivos(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public SegmentacaoProspectVO consultaProspectVinculado(SegmentacaoProspectVO segmentacaoProspect) throws Exception;

	public void validarDados(SegmentacaoProspectVO segmentacaoProspectVO) throws Exception;

	void alterar(SegmentacaoProspectVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void persistir(SegmentacaoProspectVO segmentacaoProspectVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<SegmentacaoProspectVO> consultarSegmentacaoProspect(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Integer unidadeEspecifica) throws Exception;

}
