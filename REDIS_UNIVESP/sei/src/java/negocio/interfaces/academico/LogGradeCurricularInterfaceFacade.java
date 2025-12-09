package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.LogGradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
*
* @author Allan
*/
public interface LogGradeCurricularInterfaceFacade {

    public void incluir(final LogGradeCurricularVO obj, final UsuarioVO usuario);

	public List<LogGradeCurricularVO> consultarPorGradeCurricular(Integer codigo, UsuarioVO usuario);

	void realizarCriacaoLogMatrizCurricularAlteracaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaVO, String logAlteracao, UsuarioVO usuarioVO);

	void realizarCriacaoLogMatrizCurricularInclusaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaVO, String logInclusao, UsuarioVO usuarioVO);

	void realizarCriacaoLogExclusaoGradeDisciplina(Integer gradeCurricular, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaExcluirVO, UsuarioVO usuarioVO);
}
