package negocio.interfaces.secretaria;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;

public interface CalendarioAgrupamentoTccInterfaceFacade {

	void persistir(CalendarioAgrupamentoTccVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(CalendarioAgrupamentoTccVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void consultar(DataModelo dataModelo, CalendarioAgrupamentoTccVO obj) throws Exception;

	CalendarioAgrupamentoTccVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
	
	CalendarioAgrupamentoTccVO consultarPorClassificacaoPorAnoPorSemestre(ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	List<CalendarioAgrupamentoTccVO> consultarPorClassificacaoAnoSemestre(
			ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario);

	
	CalendarioAgrupamentoTccVO consultarPorClassificacaoPorAnoPorSemestrePorMatriculaPeriodoTurmaDisciplina(
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs,
			ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario);

	CalendarioAgrupamentoTccVO consultarPorClassificacaoPorAnoPorSemestrePorDisciplina(Integer disciplina,
			ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario);
	
	public CalendarioAgrupamentoTccVO consultarPorClassificacaoPorHistoricoAnoSemestreAprovadoPorDisciplina(Integer disciplina, String  matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

}
