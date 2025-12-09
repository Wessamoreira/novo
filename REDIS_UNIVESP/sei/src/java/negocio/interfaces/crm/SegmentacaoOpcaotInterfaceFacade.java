package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.segmentacao.SegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface SegmentacaoOpcaotInterfaceFacade {

	public void validarDadosSegmentacaoOpcao(SegmentacaoOpcaoVO segmentacaoOpcao) throws ConsistirException;

	public void removerSegmentacaoOpcaoVOs(SegmentacaoProspectVO segmentacaoProspectVO, SegmentacaoOpcaoVO segmentacaoOpcao) throws Exception;

	public void incluirSegmentacaoOpcoes(SegmentacaoProspectVO segmentacaoProspectVO, UsuarioVO usuario) throws Exception;

	public void incluir(SegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception;

	public List consultarOpcoesSegmentacao(Integer segmentacao, int nivelMontarDados) throws Exception;

	public void excluirSegmentcaoOpcaoVOs(SegmentacaoProspectVO segmentacaoProspectVO, UsuarioVO usuario) throws Exception;

	public void alterarOpcoesSegmentacao(SegmentacaoProspectVO segmentoProspectVO, UsuarioVO usuario) throws Exception;

	public void alterarOpcaoSegmentacao(SegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception;

	public void excluirSegmentacaoOpcao(SegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception;

	SegmentacaoOpcaoVO consultarPorChavePrimaria(Integer segmentacaoOpcao) throws Exception;
}
