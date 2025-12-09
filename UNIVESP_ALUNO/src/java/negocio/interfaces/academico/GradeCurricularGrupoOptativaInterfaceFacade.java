package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface GradeCurricularGrupoOptativaInterfaceFacade {
	
	void incluirGradeCurricularGrupoOptativaVOs(GradeCurricularVO gradeCurricularVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void alterarGradeCurricularGrupoOptativaVOs(GradeCurricularVO gradeCurricularVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void excluirGradeCurricularGrupoOptativaVOs(GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;
	
	List<GradeCurricularGrupoOptativaVO> consultarPorGradeCurricular(Integer gradeCurricular, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	GradeCurricularGrupoOptativaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void validarDados(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) throws ConsistirException;

	void adicionarGradeCurricularGrupoOptativaDisciplina(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) throws Exception;

	void removerGradeCurricularGrupoOptativaDisciplina(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) throws Exception;

	GradeCurricularGrupoOptativaVO consultarPorPeriodoLetivo(Integer periodoLetivo, UsuarioVO usuarioVO);
	
	
	

}
