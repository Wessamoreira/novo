package negocio.interfaces.avaliacaoinst;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;

public interface AvaliacaoInstitucionalRespondenteInterfaceFacade {
	
	public void persistir(List<AvaliacaoInstitucionalAnaliticoRelVO> obj, Integer codigoAvaliacaoIntitucional,  UsuarioVO usuarioVO) throws Exception;
	
	public void alterarSituacaoAvaliacaoRespondente(RespostaAvaliacaoInstitucionalDWVO avaliacaoInstitucionalDWVO, final Boolean situacao, UsuarioVO usuario) throws Exception;
	
	public boolean consultarAvaliacaoInstitucionalRespondenteExistentePorAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer pessoa, UsuarioVO usuario) throws Exception;
	
	public void atualizarBaseAvaliacaoInstitucionalRespondente(List<AvaliacaoInstitucionalVO> institucionalVOs, UsuarioVO usuarioVO) throws Exception;
	
	public void excluirAvaliacaoRespondenteNaoRespondido(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioLogado) throws Exception;

}
