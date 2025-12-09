package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DisciplinasGradeDisciplinasRelVO;
import relatorio.negocio.comuns.academico.DisciplinasGradeRelVO;
import relatorio.negocio.interfaces.academico.DisciplinaGradeRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DisciplinasGradeRel extends SuperRelatorio implements DisciplinaGradeRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public DisciplinasGradeRel() throws Exception {
	}

	@Override
	public void validarDados(String tipoRelatorio, Integer unidadeEnsinoCurso, Integer curso, Integer turma, Integer gradeCurricular) throws Exception {
		if (unidadeEnsinoCurso.equals(0)) {
			throw new Exception("O Campo Unidade Ensino (DISCIPLINA MATRIZ CURRICULAR) deve ser Informado.");
		}
		if (curso.equals(0) && tipoRelatorio.equals("curso")) {
			throw new Exception("O Campo Curso (DISCIPLINA MATRIZ CURRICULAR) deve ser Informado.");
		}
		if (turma.equals(0) && tipoRelatorio.equals("turma")) {
			throw new Exception("O Campo Turma (DISCIPLINA MATRIZ CURRICULAR) deve ser Informado.");
		}
		if (gradeCurricular.equals(0)) {
			throw new Exception("O Campo Matriz Curricular (DISCIPLINA MATRIZ CURRICULAR) deve ser Informado.");
		}
	}

	public List<GradeCurricularVO> criarObjeto(Integer codigoGradeCurricular, Boolean apresentarDisciplinaComposta, String tipoLayout) throws Exception {
		List<GradeCurricularVO> gradeCurricularVOs = new ArrayList<GradeCurricularVO>(0);
		GradeCurricularVO gradeUtilizada = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(codigoGradeCurricular, Uteis.NIVELMONTARDADOS_TODOS, null);
		if (gradeUtilizada.getPeriodoLetivosVOs().isEmpty()) {
			throw new ConsistirException("A Grade Curricular selecionada não possui disciplinas vinculadas a ela.");
		}
		if(apresentarDisciplinaComposta && !tipoLayout.equals("DisciplinasGrade3Rel")){
			realizarCriacaoGradeDisciplinaCompostaPorGradeCurricularPeriodoLetivo(gradeUtilizada);
			realizarCriacaoGradeDisciplinaCompostaPorGradeCurricularGrupoOptativa(gradeUtilizada);
		}
		gradeCurricularVOs.add(gradeUtilizada);
		return gradeCurricularVOs;
	}
	
	private void realizarCriacaoGradeDisciplinaCompostaPorGradeCurricularPeriodoLetivo(GradeCurricularVO gradeCurricularVO){
		
		for(PeriodoLetivoVO periodoLetivoVO: gradeCurricularVO.getPeriodoLetivosVOs()){
			int index = 0;
			for(GradeDisciplinaVO gradeDisciplinaVO: periodoLetivoVO.getGradeDisciplinaVOs()){
				if(gradeDisciplinaVO.getDisciplinaComposta()){
					periodoLetivoVO.getGradeDisciplinaVOs().remove(index);
					for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()){
						GradeDisciplinaVO obj = new GradeDisciplinaVO();
						obj.setDisciplina(gradeDisciplinaCompostaVO.getDisciplina());
						obj.setCargaHoraria(gradeDisciplinaCompostaVO.getCargaHoraria());
						obj.setCargaHorariaPratica(gradeDisciplinaCompostaVO.getCargaHorariaPratica());
						obj.setCargaHorariaTeorica(gradeDisciplinaCompostaVO.getCargaHorariaTeorica());
						obj.setNrCreditos(gradeDisciplinaCompostaVO.getNrCreditos());
						obj.setModalidadeDisciplina(gradeDisciplinaCompostaVO.getModalidadeDisciplina());
						obj.setTipoDisciplina(gradeDisciplinaVO.getTipoDisciplina());
						obj.setDisciplinaRequisitoVOs(gradeDisciplinaCompostaVO.getDisciplinaRequisitoVOs());
						periodoLetivoVO.getGradeDisciplinaVOs().add(index, obj);
						index++;
					}
					realizarCriacaoGradeDisciplinaCompostaPorGradeCurricularPeriodoLetivo(gradeCurricularVO);
					return;
				}
				index++;
			}
		}
	}
	private void realizarCriacaoGradeDisciplinaCompostaPorGradeCurricularGrupoOptativa(GradeCurricularVO gradeCurricularVO){
		
		for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO: gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()){
			int index = 0;
			for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO: gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
				if(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaComposta()){
					gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs().remove(index);
					for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()){
						GradeCurricularGrupoOptativaDisciplinaVO obj = new GradeCurricularGrupoOptativaDisciplinaVO();
						obj.setDisciplina(gradeDisciplinaCompostaVO.getDisciplina());
						obj.setCargaHoraria(gradeDisciplinaCompostaVO.getCargaHoraria());
						obj.setCargaHorariaPratica(gradeDisciplinaCompostaVO.getCargaHorariaPratica());						
						obj.setNrCreditos(gradeDisciplinaCompostaVO.getNrCreditos());
						obj.setModalidadeDisciplina(gradeDisciplinaCompostaVO.getModalidadeDisciplina());						
						gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs().add(index, obj);
						index++;
					}
					realizarCriacaoGradeDisciplinaCompostaPorGradeCurricularPeriodoLetivo(gradeCurricularVO);
					return;
				}
				index++;
			}
		}
	}

	@Override
	public Map<String, Object> criarObjetoLayout2(Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO) throws Exception {
		List<DisciplinasGradeRelVO> disciplinasGradeRelVOs = null;
		DisciplinasGradeRelVO disciplinasGradeRelVO = null;
		try {
			disciplinasGradeRelVOs = new ArrayList<DisciplinasGradeRelVO>(0);
			disciplinasGradeRelVO = new DisciplinasGradeRelVO();
			montarDadosDisciplinasGradeRelVO(disciplinasGradeRelVO, gradeCurricular, unidadeEnsinoCurso, curso, turma, usuarioVO);
			SqlRowSet rs = getFacadeFactory().getGradeCurricularFacade().consultarDadosGeracaoRelatorioDisciplinasGradeDisciplinasRel(gradeCurricular, unidadeEnsinoCurso, curso, turma, apresentarDisciplinaComposta, usuarioVO);
			Map<String, String> mapPeriodo = new HashMap<String, String>(0);
			while (rs.next()) {
				disciplinasGradeRelVO.getDisciplinasGradeDisciplinasRelVOs().add(montarDadosDisciplinasGradeDisciplinasRelVO(rs, disciplinasGradeRelVO));
				mapPeriodo.put(rs.getString("periodoLetivo"), rs.getString("periodoLetivo"));
			}
			Map<String, Object> mapRetorno = new HashMap<String, Object>(0);
			if(mapPeriodo.size() % 3 == 0){
				mapRetorno.put("LAYOUT", "DisciplinasGrade2_3ColunasRel");
			}else{
//				if(mapPeriodo.size() % 4 == 0)
				mapRetorno.put("LAYOUT", "DisciplinasGrade2Rel");
			}			
			disciplinasGradeRelVOs.add(disciplinasGradeRelVO);
			mapRetorno.put("LISTA", disciplinasGradeRelVOs);
			return mapRetorno;
		} catch (Exception e) {
			throw e;
		}
	}

	private void montarDadosDisciplinasGradeRelVO(DisciplinasGradeRelVO disciplinasGradeRelVO, Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet rs = getFacadeFactory().getGradeCurricularFacade().consultarDadosGeracaoRelatorioDisciplinasGradeRel(gradeCurricular, unidadeEnsinoCurso, curso, turma, usuarioVO);
		while (rs.next()) {
			disciplinasGradeRelVO.setGradeCurricular(rs.getString("gradeCurricular"));
			disciplinasGradeRelVO.setCurso(rs.getInt("curso"));
			disciplinasGradeRelVO.setNomeCurso(rs.getString("nomeCurso"));
			disciplinasGradeRelVO.setTotalCargaHoraria(rs.getInt("cargahoraria"));
			disciplinasGradeRelVO.setTotalCredito(rs.getInt("creditos"));
			disciplinasGradeRelVO.setTotalEstagio(rs.getInt("totalcargahorariaestagio"));
			disciplinasGradeRelVO.setAutorizacao(rs.getString("autorizacao"));
			disciplinasGradeRelVO.setReconhecimento(rs.getString("reconhecimento"));
			disciplinasGradeRelVO.setDataReconhecimento(rs.getDate("dataReconhecimento"));
			disciplinasGradeRelVO.setDataAutorizacao(rs.getDate("dataAutorizacao"));
			disciplinasGradeRelVO.setTotalAtividadeComplementar(rs.getInt("totalcargahorariaatividadecomplementar"));
		}
	}

	private DisciplinasGradeDisciplinasRelVO montarDadosDisciplinasGradeDisciplinasRelVO(SqlRowSet rs, DisciplinasGradeRelVO disciplinasGradeRelVO) {
		DisciplinasGradeDisciplinasRelVO obj = null;
		if (rs.getBoolean("disciplinaDiversificada")) {
			obj = new DisciplinasGradeDisciplinasRelVO();
			obj.setChDisciplina(String.valueOf(rs.getInt("chDisciplina")));
			obj.setChDisciplinaCalculada(rs.getInt("chDisciplina"));
			if (disciplinasGradeRelVO.getTotalSemanaLetivaAno() > 0) {
				obj.setChSemana(Uteis.arredondarDivisaoEntreNumeros(Double.valueOf(obj.getChDisciplina()) / disciplinasGradeRelVO.getTotalSemanaLetivaAno()).toString());
				obj.setChSemanaCalculada(Uteis.arredondarDivisaoEntreNumeros(obj.getChDisciplinaCalculada().doubleValue() / disciplinasGradeRelVO.getTotalSemanaLetivaAno()));
			}
			obj.setDisciplina(rs.getInt("disciplina"));
			obj.setDisciplinaDiversificada(rs.getBoolean("disciplinaDiversificada"));
			obj.setNomeDisciplina(rs.getString("nomeDisciplina"));
			obj.setOrdem(rs.getInt("ordem"));
			obj.setPeriodoLetivo(rs.getString("periodoLetivo"));
			obj.setNrPeriodoLetivo(rs.getInt("periodo"));
		} else {
			obj = new DisciplinasGradeDisciplinasRelVO();
			obj.setChDisciplina(String.valueOf(rs.getInt("chDisciplina")));
			obj.setChDisciplinaCalculada(rs.getInt("chDisciplina"));
			if (disciplinasGradeRelVO.getTotalSemanaLetivaAno() > 0) {
				obj.setChSemana(Uteis.arredondarDivisaoEntreNumeros(Double.valueOf(obj.getChDisciplina()) / disciplinasGradeRelVO.getTotalSemanaLetivaAno()).toString());
				obj.setChSemanaCalculada(Uteis.arredondarDivisaoEntreNumeros(obj.getChDisciplinaCalculada().doubleValue() / disciplinasGradeRelVO.getTotalSemanaLetivaAno()));
			}
			obj.setDisciplina(rs.getInt("disciplina"));
			obj.setDisciplinaDiversificada(rs.getBoolean("disciplinaDiversificada"));
			obj.setNomeDisciplina(rs.getString("nomeDisciplina"));
			obj.setOrdem(rs.getInt("ordem"));
			obj.setPeriodoLetivo(rs.getString("periodoLetivo"));
			obj.setNrPeriodoLetivo(rs.getInt("periodo"));
		}
		if (obj.getChDisciplina().equals("") || obj.getChDisciplina().equals("0")) {
			obj.setChDisciplina("-");
		}
		if (obj.getChSemana().equals("") || obj.getChSemana().equals("0")) {
			obj.setChSemana("-");
		}
		return obj;
	}

	public static String getDesignIReportRelatorio(String tipoLayout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + tipoLayout + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DisciplinasGradeRel");
	}
}
