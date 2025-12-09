package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.PerfilTurmaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.parametroRelatorio.academico.PerfilTurmaSuperParametroRelVO;

public interface PerfilTurmaRelInterfaceFacade {

	public List<PerfilTurmaRelVO> criarObjeto(Integer curso, Integer turma, String ano, String semestre, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean apresentarFoto, String caminhoImagemPadrao, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarAlunoPendenteFinanceiramente, boolean permitirRealizarLancamentoAlunosPreMatriculados, Integer codigoUnidadeEnsino) throws Exception;

	public String getDesignIReportRelatorioPerfilTurma();

	public String getIdEntidadePerfilTurmaRel();

	public String getCaminhoBaseRelatorioPerfilTurma();

	public void validarDados(Integer unidadeEnsino, String identificarTurma) throws Exception;

	public String getDesignIReportRelatorioPerfilTurmaFoto();
}