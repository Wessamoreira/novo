package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.LogImpactoMatrizCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
*
* @author Carlos
*/
public interface LogImpactoMatrizCurricularInterfaceFacade {

    public void incluir(final LogImpactoMatrizCurricularVO obj, final UsuarioVO usuario);

	void incluirLogImpactosGradeDisciplina(Integer logGradeCurricular, List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs, UsuarioVO usuarioVO);

	void inicializarDadosJsonImpactosHistorico(LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO);

	void inicializarDadosJsonImpactos(List<LogImpactoMatrizCurricularVO> listaLogImpactoMatrizCurricularVOs);

	void inicializarDadosJsonImpactosTurmaDisciplina(LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO);

	void inicializarDadosJsonImpactosMatricula(LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO);
	
}
