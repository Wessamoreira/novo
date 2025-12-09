/**
 * 
 */
package relatorio.negocio.jdbc.basico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.OrdenadorVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FiltroSituacaoDisciplina;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.basico.AcessoCatracaRelVO;
import relatorio.negocio.interfaces.basico.AcessoCatracaRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Carlos Eugênio
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class AcessoCatracaRel extends SuperRelatorio implements AcessoCatracaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public AcessoCatracaRel() {
		super();
	}
	
	public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Date periodoAcessoInicio, Date periodoAcessoFinal, Integer turma, String matricula, String ano, String semestre, String campoFiltro, String tipoRelatorio, Integer catraca, String periodicidade, String cursoApresentar, UsuarioVO usuarioVO) throws Exception {
		if (periodoAcessoInicio == null) {
			throw new Exception("O campo PERÍODO INÍCIO ACESSO deve ser informado.");
		}
		if (periodoAcessoFinal == null) {
			throw new Exception("O campo PERÍODO FINAL ACESSO deve ser informado.");
		}
		if (periodicidade.equals("AN")) {
			if (ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
		}
		if (periodicidade.equals("SE")) {
			if (ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			if (semestre.equals("")) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}
		}
		if (listaUnidadeEnsinoVOs.isEmpty()) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		} else {
			boolean possuiUnidadeSelecionada = false;
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					possuiUnidadeSelecionada = true;
					break;
				}
			}
			if (!possuiUnidadeSelecionada) {
				throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
			}
		}
		if (campoFiltro.equals("curso")) {
			if (cursoApresentar.isEmpty()) {
				throw new Exception("O campo CURSO deve ser informado.");
			}
		} else if (campoFiltro.equals("turma")) {
			if (turma.equals(0)) {
				throw new Exception("O campo TURMA deve ser informado.");
			}
		} else if (campoFiltro.equals("matricula")) {
			if (matricula.equals("")) {
				throw new Exception("O campo MATRÍCULA deve ser informado.");
			}
		}
	}
	
	public List<AcessoCatracaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Date periodoAcessoInicio, Date periodoAcessoFinal, Integer turma, String matricula, String ano, String semestre, String campoFiltro, String tipoRelatorio, Integer catraca, String periodicidade, List<OrdenadorVO> listaOrdenadorVOs, String cursoApresentar, String turnoApresentar, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(listaUnidadeEnsinoVOs, listaCursoVOs, listaTurnoVOs, periodoAcessoInicio, periodoAcessoFinal, turma, matricula, ano, semestre, campoFiltro, tipoRelatorio, catraca, periodicidade, cursoApresentar, usuarioVO);
		List<AcessoCatracaRelVO> listaAcessoCatracaRelVOs = executarConsultaParametrizada(listaUnidadeEnsinoVOs, listaCursoVOs, listaTurnoVOs, periodoAcessoInicio, periodoAcessoFinal, turma, matricula, ano, semestre, campoFiltro, tipoRelatorio, catraca, periodicidade, listaOrdenadorVOs, turnoApresentar, filtroRelatorioAcademicoVO, usuarioVO);
		return listaAcessoCatracaRelVOs;
	}

	public List<AcessoCatracaRelVO> executarConsultaParametrizada(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Date periodoAcessoInicio, Date periodoAcessoFinal, Integer turma, String matricula, String ano, String semestre, String campoFiltro, String tipoRelatorio, Integer catraca, String periodicidade, List<OrdenadorVO> listaOrdenadorVOs, String turnoApresentar, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from (");
		//SQL QUE ACESSARAM A CATRACA
		sb.append("select distinct horarioturmadia.data AS dataHora, acessocatraca.direcao, acessocatraca.catraca AS catraca, catraca.descricao AS \"catraca_descricao\", true AS acessouCatraca, to_char( acessoCatraca.dataAcessoHora, 'HH24:MI') AS hora, ");
		sb.append(" pessoa.codigo AS \"pessoa_codigo\", pessoa.nome AS \"pessoa_nome\", matricula.matricula, ");
		sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma_identificadorturma\", ");
		sb.append(" matricula.unidadeEnsino, matricula.curso, matricula.turno, matriculaperiodo.ano, matriculaperiodo.semestre, matriculaperiodo.turma, 'ACESSARAM_CATRACA' AS tipoRelatorio  ");
		sb.append(" from pessoa ");
		sb.append(" inner join acessoCatraca on acessocatraca.pessoa = pessoa.codigo ");
		sb.append(" inner join catraca on catraca.codigo = acessoCatraca.catraca ");
		sb.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.codigo in(");
		sb.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ") ;
		sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join horarioturma on horarioturma.turma = turma.codigo and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadia.data = cast(acessoCatraca.dataAcessoHora as date) ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo and horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sb.append(" where 1=1 ");
		sb.append(" and cast(horarioturmadia.data as date) >= '").append(Uteis.getDataJDBC(periodoAcessoInicio)).append("' and cast(horarioturmadia.data as date) <= '").append(Uteis.getDataJDBC(periodoAcessoFinal)).append("' ");
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		
		sb.append(" union all ");
		
		//SQL QUE NÃO ACESSARAM A CATRACA
		sb.append(" select distinct horarioturmadia.data AS datahora, cast(null as integer) AS direcao, cast(null as integer) AS catraca, cast(null as varchar) AS \"catraca_descricao\", false AS acessouCatraca, null AS hora, ");
		sb.append(" pessoa.codigo AS \"pessoa_codigo\", pessoa.nome AS \"pessoa_nome\", matricula.matricula, ");
		sb.append(" turma.codigo AS \"turma_codigo\", turma.identificadorturma AS \"turma_identificadorturma\", ");
		sb.append(" matricula.unidadeEnsino, matricula.curso, matricula.turno, matriculaperiodo.ano, matriculaperiodo.semestre, matriculaperiodo.turma, 'NAO_ACESSARAM_CATRACA' AS tipoRelatorio  ");
		sb.append(" from pessoa ");
		sb.append(" inner join matricula on matricula.aluno = pessoa.codigo  ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.codigo in(");
		sb.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
		sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join horarioturma on horarioturma.turma = turma.codigo and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo and horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sb.append(" where 1=1 ");
		sb.append(" and pessoa.codigo not in(");
		sb.append(" select acessoCatraca.pessoa from acessoCatraca ");
		sb.append(" where cast(acessocatraca.dataAcessoHora as date) >= '").append(Uteis.getDataJDBC(periodoAcessoInicio)).append("' and cast(acessocatraca.dataAcessoHora as date) <= '").append(Uteis.getDataJDBC(periodoAcessoFinal)).append("' ");
		sb.append(" and acessoCatraca.pessoa = pessoa.codigo) ");
		sb.append(" and cast(horarioturmadia.data as date) >= '").append(Uteis.getDataJDBC(periodoAcessoInicio)).append("' and cast(horarioturmadia.data as date) <= '").append(Uteis.getDataJDBC(periodoAcessoFinal)).append("' ");
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sb.append(" ) as t where 1=1 ");

		sb.append(" and unidadeEnsino in(");
		for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sb.append(unidadeEnsinoVO.getCodigo()).append(", ");
			}
		}
		sb.append("0) ");
		
		//CAMPO FILTRO POR CURSO
		if (campoFiltro.equals("curso")) {
			sb.append(" and curso in(");
			for (CursoVO cursoVO : listaCursoVOs) {
				if (cursoVO.getFiltrarCursoVO()) {
					sb.append(cursoVO.getCodigo()).append(", ");
				}
			}
			sb.append("0) ");

			if (!listaTurnoVOs.isEmpty() && !turnoApresentar.isEmpty()) {
				sb.append(" and turno in(");
				for (TurnoVO turnoVO : listaTurnoVOs) {
					if (turnoVO.getFiltrarTurnoVO()) {
						sb.append(turnoVO.getCodigo()).append(", ");
					}
				}
				sb.append("0) ");
			}
		}

		//CAMPO FILTRO POR TURMA
		if (campoFiltro.equals("turma")) {
			if (!turma.equals(0)) {
				sb.append(" and turma = ").append(turma);
			}
		}

		//CAMPO FILTRO POR MATRÍCULA
		if (campoFiltro.equals("matricula")) {
			if (!matricula.equals("")) {
				sb.append(" and matricula = '").append(matricula).append("' ");
			}
		}
		
		if (periodicidade.equals("AN") || periodicidade.equals("SE")) {
			if (!ano.equals("")) {
				sb.append(" and ano = '").append(ano).append("' ");
			}
		}
		if (periodicidade.equals("SE")) {
			if (!semestre.equals("")) {
				sb.append(" and semestre = '").append(semestre).append("' ");
			}
		}
		if (!catraca.equals(0)) {
			sb.append(" and catraca = ").append(catraca);
		}
		if (tipoRelatorio.equals("ACESSARAM_CATRACA")) {
			sb.append(" and tipoRelatorio = 'ACESSARAM_CATRACA' ");
		}
		if (tipoRelatorio.equals("NAO_ACESSARAM_CATRACA")) {
			sb.append(" and tipoRelatorio = 'NAO_ACESSARAM_CATRACA' ");
		}
		sb.append(" order by ");
		boolean acessou = false;
		
		for (OrdenadorVO ordenadorVO : listaOrdenadorVOs) {
			
			if (ordenadorVO.getUtilizar()) {
				if (!acessou) {
					sb.append(ordenadorVO.getCampoOrdenar());
					acessou = true;
				} else {
					sb.append(", ").append(ordenadorVO.getCampoOrdenar());
				}
			}
		}
		if (!acessou) {
			sb.append(" turma_identificadorturma, pessoa_nome ");
		}
		

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<AcessoCatracaRelVO> listaAcessoCatracaRelVOs = new ArrayList<AcessoCatracaRelVO>(0);
		while (tabelaResultado.next()) {
			listaAcessoCatracaRelVOs.add(montarDados(tabelaResultado));
		}
		return listaAcessoCatracaRelVOs;
	}

	public AcessoCatracaRelVO montarDados(SqlRowSet dadosSQL) {
		AcessoCatracaRelVO obj = new AcessoCatracaRelVO();
		if (dadosSQL.getString("hora") != null) {
			obj.setHoraAcesso(dadosSQL.getString("hora"));
		}
		obj.setDataAcesso(Uteis.getData(dadosSQL.getDate("dataHora")));
		obj.setCatraca(dadosSQL.getString("catraca_descricao"));
		obj.setNomePessoa(dadosSQL.getString("pessoa_nome"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setIdentificadorTurma(dadosSQL.getString("turma_identificadorTurma"));
		obj.setDirecao(dadosSQL.getInt("direcao"));
		obj.setAcessouCatraca(dadosSQL.getBoolean("acessouCatraca"));
		return obj;
	}
	
	public static String getDesignIReportRelatorio(String layout) throws Exception {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "basico" + File.separator + getIdEntidade(layout) + ".jrxml";
	}
	
	public static String getIdEntidade(String layout) {
		if (layout.equals("ANALITICO_TURMA_DIA")) {
			return "AcessoCatracaAnaliticoTurmaRel";
		} else if (layout.equals("ANALITICO_DIA")) {
			return "AcessoCatracaAnaliticoDiaRel";
		} else {
			return "";
		}
	}
	
	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "basico" + File.separator;
	}
}
