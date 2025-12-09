package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.MapaNotasDisciplinaAlunosRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.interfaces.academico.MapaNotasDisciplinaAlunosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Danilo
 * 
 */
@Repository
@Scope("singleton")
@Lazy
public class MapaNotasDisciplinaAlunosRel extends SuperRelatorio implements MapaNotasDisciplinaAlunosRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public static void validarDados(UnidadeEnsinoVO unidadeEnsino, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_unidadeEnsino"));
		}
		if (!Uteis.isAtributoPreenchido(unidadeEnsinoCurso)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_curso"));
		}
		if (!Uteis.isAtributoPreenchido(turma)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_turma"));
		}
		if (!Uteis.isAtributoPreenchido(disciplina)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Conteudo_disciplina"));
		}
		if ((unidadeEnsinoCurso.getCurso().getPeriodicidade().equals("AN") || unidadeEnsinoCurso.getCurso().getPeriodicidade().equals("SE")) && !Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_ano"));
		}
		if (unidadeEnsinoCurso.getCurso().getPeriodicidade().equals("SE") && !Uteis.isAtributoPreenchido(semestre)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorarioTurma_semestre"));
		}
	}

	/**
	 * Método Responsável por chamar o método de consulta
	 * 
	 * @param unidadeEnsino
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * 
	 * @return List<MapaNotasDisciplinaAlunosRelVO> Lista de Notas de Alunos.
	 * @author Danilo
	 * @since 02.02.2011
	 */
	public List<MapaNotasDisciplinaAlunosRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, UsuarioVO usuarioVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		MapaNotasDisciplinaAlunosRel.emitirRelatorio(getIdEntidade(), false, null);
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorTurmaUnidadeEnsinoCursoDisciplinaAnoSemestreDataHistoricoOrdenarDisciplina(unidadeEnsinoVO.getCodigo(), cursoVO.getCodigo(), disciplinaVO.getCodigo(), turmaVO, ano, semestre, "", "", true, false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, filtroRelatorioAcademicoVO, "", usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);		
		return montarDadosMapaNotasAlunosDisciplina(historicoVOs);
	}

	private List<MapaNotasDisciplinaAlunosRelVO> montarDadosMapaNotasAlunosDisciplina(List<HistoricoVO> historicoVOs) throws Exception {
		List<MapaNotasDisciplinaAlunosRelVO> mapaNotasDisciplinaAlunosRelVOs = new ArrayList<MapaNotasDisciplinaAlunosRelVO>(0);
		MapaNotasDisciplinaAlunosRelVO mapaNotasDisciplinaAlunosRelVO = new MapaNotasDisciplinaAlunosRelVO();
		int ordemLinha = 1;
		for (HistoricoVO historicoVO : historicoVOs) {
			int ordemColuna = 1;
			mapaNotasDisciplinaAlunosRelVO.setDataInicioModulo(historicoVO.getDataInicioModulo());
			mapaNotasDisciplinaAlunosRelVO.getCrosstabVOs().add(montarDadosFrequenciaHistoricoCrosstab(historicoVO, ordemLinha, ordemColuna));
			ordemColuna++;
			mapaNotasDisciplinaAlunosRelVO.getCrosstabVOs().add(montarDadosSituacaoHistoricoCrosstab(historicoVO, ordemLinha, ordemColuna));
			List<CrosstabVO> crosstabVOs = montarDadosMapaNotasAlunosDisciplinaCrosstab(historicoVO, ordemLinha, ordemColuna);
			mapaNotasDisciplinaAlunosRelVO.getCrosstabVOs().addAll(crosstabVOs);
			mapaNotasDisciplinaAlunosRelVO.getCrosstabVOs().addAll(preencherColunasFinalPagina(historicoVO, ordemLinha, ordemColuna + crosstabVOs.size()));
			ordemLinha++;
		}
		mapaNotasDisciplinaAlunosRelVOs.add(mapaNotasDisciplinaAlunosRelVO);
		return mapaNotasDisciplinaAlunosRelVOs;
	}

	public List<CrosstabVO> montarDadosMapaNotasAlunosDisciplinaCrosstab(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) throws Exception {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		int cont = 1;
		for (int x = 1; x <= 40; x++) {
			boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "utilizarNota" + x);
			boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "apresentarNota" + x);
			boolean notaMediaFinal = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "nota" + x + "MediaFinal");
			ConfiguracaoAcademicoNotaConceitoVO notaConceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x + "Conceito");
			if ((utilizarNota && apresentarNota) || (utilizarNota && notaMediaFinal)) {
				CrosstabVO crosstabVO = new CrosstabVO();
				crosstabVO.setOrdemLinha(ordemLinha);
				crosstabVO.setLabelLinha(historicoVO.getMatricula().getMatricula());
				crosstabVO.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
				crosstabVO.setOrdemColuna(cont + ordemColuna);
				crosstabVO.setLabelColuna((String) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "tituloNotaApresentar" + x));				
				if (Uteis.isAtributoPreenchido(notaConceito)) {
					crosstabVO.setValorString(notaConceito.getAbreviaturaConceitoNota());
				} else {
					if (UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x) != null) {
						crosstabVO.setValorDouble(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble((Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
					} else {
						crosstabVO.setValorString("--");
					}
				}
				crosstabVOs.add(crosstabVO);
				cont++;
			}
		}
		CrosstabVO crosstabVO = new CrosstabVO();
		crosstabVO.setOrdemLinha(ordemLinha);
		crosstabVO.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstabVO.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstabVO.setOrdemColuna(cont + ordemColuna);
		crosstabVO.setLabelColuna("MF");				
		if(historicoVO.getMediaFinal() == null){
			crosstabVO.setValorString("--");
		}else{
			if (historicoVO.getMediaFinalConceito().getCodigo() > 0) {
				crosstabVO.setValorString(historicoVO.getMediaFinalConceito().getAbreviaturaConceitoNota());
	        }else if(historicoVO.getUtilizaNotaFinalConceito()){
	        	crosstabVO.setValorString(historicoVO.getNotaFinalConceito());
	        } else{
	        	crosstabVO.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(historicoVO.getMediaFinal(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
	        }       	      		
		}
		crosstabVOs.add(crosstabVO);
		return crosstabVOs;
	}

	public CrosstabVO montarDadosSituacaoHistoricoCrosstab(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setLabelColuna("Situação");
		crosstab.setValorString(historicoVO.getSituacao());
		return crosstab;

	}

	public CrosstabVO montarDadosFrequenciaHistoricoCrosstab(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setLabelColuna("Freq.(%)");
		crosstab.setValorDouble(historicoVO.getFreguencia());
		return crosstab;
	}

	public List<CrosstabVO> preencherColunasFinalPagina(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		if ((ordemColuna % 30) > 0) {
			for (int x = 1; x < (ordemColuna % 30); x++) {
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
				crosstab.setOrdemColuna(ordemColuna + x);
				crosstab.setOrdemLinha(ordemLinha);
				crosstab.setLabelColuna("");
				crosstabVOs.add(crosstab);
				
			}
		}
		return crosstabVOs;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("MapaNotasDisciplinaAlunosRel");
	}

}
