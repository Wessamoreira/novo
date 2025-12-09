package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.MaterialAlunoVO;
import negocio.comuns.academico.MaterialProfessorVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;

public interface LocalAulaInterfaceFacade {
	
	void persistir(LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
	
	void inativar(LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
	
	void ativar(LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	void adicionarSalaLocalAulaVO(LocalAulaVO localAulaVO, SalaLocalAulaVO salaLocalAulaVO) throws Exception;
	
	void removerSalaLocalAulaVO(LocalAulaVO localAulaVO, SalaLocalAulaVO salaLocalAulaVO) throws Exception;
	
	void validarDados(LocalAulaVO localAulaVO) throws ConsistirException;
	
	List<LocalAulaVO> consulta(String campoConsulta, String valorConsulta, Integer unidadeEnsinoConsulta,Integer unidadeEnsino, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	LocalAulaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	public void realizarAlteracaoOrdem(LocalAulaVO localAulaVO, SalaLocalAulaVO salaLocalAulaVO, boolean subir);
	
	public List<LocalAulaVO> consultaLocalSalaAulaPorSituacao(StatusAtivoInativoEnum situacao, Integer unidadeEnsino, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	List<LocalAulaVO> consultarPorLocal(String local, Integer unidadeEnsino, StatusAtivoInativoEnum situacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	void validarMaterialAluno(MaterialAlunoVO materialAlunoVO) throws ConsistirException;
	
	void validarMaterialProfessor(MaterialProfessorVO materialProfessorVO) throws ConsistirException;
}
