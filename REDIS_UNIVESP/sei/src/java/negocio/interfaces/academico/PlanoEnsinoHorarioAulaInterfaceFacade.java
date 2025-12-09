package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface PlanoEnsinoHorarioAulaInterfaceFacade {

	void incluirPlanoEnsinoHorarioAulaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	void alterarPlanoEnsinoHorarioAulaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	void excluirPlanoEnsinoHorarioAulaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	void validarDados(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO) throws ConsistirException;
	void incluir(final PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO, final UsuarioVO usuarioVO) throws Exception;
	void alterar(final PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO, final UsuarioVO usuarioVO) throws Exception;
	List<PlanoEnsinoHorarioAulaVO> consultarPorPlanoEnsino(Integer planoEnsino, UsuarioVO usuarioVO) throws Exception;
}
