package negocio.interfaces.avaliacaoinst;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaVO;

public interface AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade {
	

	AvaliacaoInstitucionalPessoaAvaliadaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void persistir(List<AvaliacaoInstitucionalPessoaAvaliadaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<AvaliacaoInstitucionalPessoaAvaliadaVO> consultarPorAvaliacaoInstitucional(Integer avaliacaoInstitucional, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
