package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface BoletimAcademicoRelInterfaceFacade {

	List<BoletimAcademicoRelVO> criarObjeto(MatriculaVO matriculaVO, String tipoLayout, boolean filtrarPorAluno, String anoSemestre, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO, 
			boolean apresentarCampoAssinatura, boolean apresentarQuantidadeFaltasAluno, FuncionarioVO funcionarioPrincipalVO, CargoVO cargoFuncionarioPrincial, FuncionarioVO funcionarioSecundarioVO, CargoVO cargoFuncionarioSecundario, BimestreEnum bimestre, 
			SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean apresentarApenasNotaMedia, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarCampoAssinaturaResponsavel, boolean apresentarReprovadoPorFaltaComoReprovado, 
			FiltroRelatorioAcademicoVO filtroAcademicoVO, GradeCurricularVO gradeCurricularVO, boolean considerarSituacaoAcademicaHistorico, boolean apresentarCargaHorariaCursada, List<String> listaNotas, Boolean trazerAlunoTransferencia) throws Exception;
	
	public void realizarConsultaRegistroPorMatriculaAnoSemestre(BoletimAcademicoRelVO obj, String ano, String semestre, BimestreEnum bimestreEnum, List<DisciplinaVO> disciplinaVOs, UsuarioVO usuarioVO) throws Exception;

	public String realizarCriacaoLegendaSituacaoDisciplinaHistorico(BoletimAcademicoRelVO boletimAcademicoRelVO, MatriculaVO matriculaVO);
	
}