package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface InclusaoHistoricoAlunoInterfaceFacade {

	void persistir(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	List<InclusaoHistoricoAlunoVO> consultar(String campoConsulta, String valorConsulta, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception;
	
	InclusaoHistoricoAlunoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, boolean validarAcesso,  UsuarioVO usuarioVO) throws Exception;
	
	void realizarMontagemMatriculaComHistoricoAlunoVO(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarInclusaoDisciplinaOutraMatriculaAluno(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO) throws ConsistirException;	
	
	Boolean adicionarDisciplinaHistoricoAlunoPorGradeDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO, boolean retornarExcecao) throws Exception;
	
	Boolean adicionarDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO, boolean retornarExcecao) throws Exception;
	
	void removerDisciplinaHistoricoAlunoPorGradeDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO, HistoricoVO historicoRemoverVO) throws Exception;
	
	void removerDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, HistoricoVO historicoRemoverVO) throws Exception;

	void excluir(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, Integer unidadeEnsino) throws Exception;

	void realizarPreparacaoDadosInclusaoHistorico(HistoricoVO historicoAtual, GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, MatriculaVO matriculaVO,
			GradeCurricularVO gradeCurricularVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO)
			throws Exception;
	
}
