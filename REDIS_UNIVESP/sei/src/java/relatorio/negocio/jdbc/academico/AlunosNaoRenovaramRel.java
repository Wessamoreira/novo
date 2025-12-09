package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.AlunosNaoRenovaramRelVO;
import relatorio.negocio.interfaces.academico.AlunosNaoRenovaramRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Danilo
 * 
 */
@Repository
@Scope("singleton")
@Lazy
public class AlunosNaoRenovaramRel extends SuperRelatorio implements AlunosNaoRenovaramRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1588621219761718716L;

	public AlunosNaoRenovaramRel() {
	}

	@Override
	public void validarDados(UnidadeEnsinoVO unidadeEnsino, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre) throws ConsistirException {
		if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO deve ser informado.");
		}
		if(periodicidadeEnum == null){
			throw new ConsistirException("O campo PERIODICIDADE deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(ano)){
			throw new ConsistirException("O campo ANO deve ser informado.");
		}
		if(ano.trim().length() != 4){
			throw new ConsistirException("O campo ANO deve ser informado com 4 dígitos.");
		}
		if(periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && semestre.trim().isEmpty()){
			throw new ConsistirException("O campo SEMESTRE deve ser informado.");
		}
		
	}

	@Override
	public void validarDadosUnidaEnsino(UnidadeEnsinoVO unidadeEnsino) throws ConsistirException {
		if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO deve ser informado.");
		}
	}
	
	@Override
	public List<AlunosNaoRenovaramRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, String tipoRelatorio, Boolean desconsiderarAlunoPreMatriculado, Boolean desconsiderarPossivelFormando, UsuarioVO usuarioVO) throws Exception {
		AlunosNaoRenovaramRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		getFacadeFactory().getAlunosNaoRenovaramRelFacade().validarDados(unidadeEnsino, periodicidadeEnum, ano, semestre);
		List<AlunosNaoRenovaramRelVO> listaRelatorio = new ArrayList<AlunosNaoRenovaramRelVO>(0);
		if (tipoRelatorio.equals("AN")) {
			listaRelatorio = consultaAlunosNaoRenovaramMatriculaUltimoSemestre(periodicidadeEnum, ano, semestre, unidadeEnsino.getCodigo(), cursoVOs, turnoVOs, turma.getCodigo(), desconsiderarAlunoPreMatriculado, desconsiderarPossivelFormando);
		} else {
			listaRelatorio = consultaAlunosNaoRenovaramMatriculaUltimoSemestreSintetico(periodicidadeEnum, ano, semestre, unidadeEnsino.getCodigo(), cursoVOs, turnoVOs, turma.getCodigo(), desconsiderarAlunoPreMatriculado, desconsiderarPossivelFormando);
		}
		return listaRelatorio;
	}

	/**
	 * Método Responsável por trazer os alunos que não renovaram matrícula no
	 * último semestre.
	 * 
	 * @return List<AlunosNaoRenovaramRelVO> Lista de Alunos que não renovaram
	 *         matricula.
	 * @author Danilo
	 * @since 31.01.2011
	 */
	private List<AlunosNaoRenovaramRelVO> consultaAlunosNaoRenovaramMatriculaUltimoSemestre(PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, Integer unidadeEnsino, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, Integer turma, Boolean desconsiderarAlunoPreMatriculado, Boolean desconsiderarPossivelFormando) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder("");
		String semestreAnterior = "";
		String anoAnterior = "";
		if (semestre.equals("1")) {
			semestreAnterior = "2";
			anoAnterior = String.valueOf(Integer.parseInt(ano) - 1);
		} else {
			semestreAnterior = "1";
			anoAnterior = ano;
		}
		sqlStr.append(" SELECT turma, curso, ultimoanosemestre, aluno, turno, matricula, email,	celular, telefoneres, possivelFormando from ( ");
		sqlStr.append(" SELECT DISTINCT curso.nome as curso, case when matrip.semestre <> '' then matrip.ano  ||'/'|| matrip.semestre else matrip.ano end as ultimoanosemestre,  turma.identificadorturma as turma, ");
		sqlStr.append(" turno.nome as turno, mat1.matricula as matricula,	pes.nome as aluno,	pes.email as email,	pes.celular as celular,	pes.telefoneres as telefoneres, matrip.situacaomatriculaperiodo,  ");
		if(desconsiderarPossivelFormando){
			sqlStr.append(" false as possivelFormando ");
		}else{
			sqlStr.append(" (select ").append(PossiveisFormandosRel.getSqlPossiveisFormandos("", true, "mat1")).append(")  as possivelFormando ");
		}
		sqlStr.append(" FROM matricula mat1	");
		sqlStr.append(" INNER JOIN matriculaperiodo matrip ON matrip.matricula = mat1.matricula	");
		sqlStr.append(" INNER JOIN pessoa pes ON pes.codigo = mat1.aluno ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = mat1.curso ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matrip.turma ");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = turma.turno ");
		sqlStr.append(" WHERE ");
		sqlStr.append(" matrip.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		if(periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)){
			sqlStr.append(" AND curso.periodicidade  = 'AN' and matrip.ano = '").append(anoAnterior).append("'");
		}else{
			sqlStr.append(" and curso.periodicidade = 'SE' and matrip.ano = '").append(anoAnterior).append("'").append(" AND matrip.semestre = '").append(semestreAnterior).append("'");
		}		
		sqlStr.append(" AND not exists ( SELECT mtp2.matricula FROM matriculaperiodo mtp2 ");
		sqlStr.append("  WHERE  (mtp2.ano||'/'||mtp2.semestre) >  (matrip.ano||'/'||matrip.semestre)  ");
		if (!desconsiderarAlunoPreMatriculado) {
			sqlStr.append(" and mtp2.situacaoMatriculaPeriodo not in ('PC', 'PR')  ");
		}
		sqlStr.append(" AND matrip.matricula = mtp2.matricula limit 1 )  ");
		if(desconsiderarPossivelFormando){
			sqlStr.append(PossiveisFormandosRel.getSqlPossiveisFormandos(" and ", false, "mat1"));
		}
		if(!Uteis.isAtributoPreenchido(turma)){
			StringBuilder in = new StringBuilder("");
			for(CursoVO curso: cursoVOs){
				if(curso.getFiltrarCursoVO()){
					if(in.length() > 0){
						in.append(", ");
					}
					in.append(curso.getCodigo());
				}
			}
			if(in.length() > 0){
				sqlStr.append(" AND curso.codigo in (").append(in.toString()).append(") ");
			}
			in = new StringBuilder("");
			for(TurnoVO turnoVO: turnoVOs){
				if(turnoVO.getFiltrarTurnoVO()){
					if(in.length() > 0){
						in.append(", ");
					}
					in.append(turnoVO.getCodigo());
				}
			}
			if(in.length() > 0){
				sqlStr.append(" AND mat1.turno in (").append(in.toString()).append(") ");
			}
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND turma.codigo = ").append(turma).append(" ");
		}
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND mat1.unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" GROUP BY turma.identificadorturma,turno.nome,curso.nome,pes.nome, ultimoanosemestre, mat1.matricula, pes.email, pes.celular, pes.telefoneres, matrip.situacaomatriculaperiodo ");
		sqlStr.append(" ) as t where situacaoMatriculaPeriodo not in ('PC', 'CA', 'TR', 'AC', 'TS', 'PR', 'TI') ");
		sqlStr.append(" GROUP BY turma, curso, ultimoanosemestre, aluno, turno, matricula, email,	celular, telefoneres, possivelFormando ");
		sqlStr.append(" ORDER BY turma, curso, ultimoanosemestre, aluno ");
		try {
			SqlRowSet tabelaResultados = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosAlunoNaoRenovaramMatricula(tabelaResultados);
		} finally {
			sqlStr = null;
			semestreAnterior = null;
		}
	}

	private List<AlunosNaoRenovaramRelVO> consultaAlunosNaoRenovaramMatriculaUltimoSemestreSintetico(PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, Integer unidadeEnsino, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, Integer turma, Boolean desconsiderarAlunoPreMatriculado, Boolean desconsiderarPossivelFormando) throws SQLException, Exception {
 
		StringBuilder sqlStr = new StringBuilder("");
		String semestreAnterior = "";
		String anoAnterior = "";
		if (semestre.equals("1")) {
			semestreAnterior = "2";
			anoAnterior = String.valueOf(Integer.parseInt(ano) - 1);
		} else {
			semestreAnterior = "1";
			anoAnterior = ano;
		}
		sqlStr.append(" select curso, turma, count(distinct matricula) as qtdTurma from ( ");
		sqlStr.append(" SELECT curso.nome as curso, turma.identificadorturma as turma, mat1.matricula as matricula, situacaoMatriculaPeriodo   ");
		sqlStr.append(" FROM  matricula mat1	 ");
		sqlStr.append(" INNER JOIN matriculaperiodo matrip ON matrip.matricula = mat1.matricula ");
		sqlStr.append(" INNER JOIN pessoa pes ON pes.codigo = mat1.aluno  ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = mat1.curso  ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matrip.turma  ");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = turma.turno  ");
		sqlStr.append(" WHERE ");
		sqlStr.append(" matrip.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		if(periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)){
			sqlStr.append(" AND curso.periodicidade  = 'AN' and matrip.ano = '").append(anoAnterior).append("'");
		}else{
			sqlStr.append(" and curso.periodicidade = 'SE' and matrip.ano = '").append(anoAnterior).append("'").append(" AND matrip.semestre = '").append(semestreAnterior).append("'");
		}
		
		sqlStr.append(" AND not exists ( SELECT mtp2.matricula FROM matriculaperiodo mtp2 ");
		sqlStr.append("  WHERE  (mtp2.ano||'/'||mtp2.semestre) >  (matrip.ano||'/'||matrip.semestre)  ");
		if (!desconsiderarAlunoPreMatriculado) {
			sqlStr.append(" and mtp2.situacaoMatriculaPeriodo not in ('PC', 'PR')  ");
		}
		sqlStr.append(" AND matrip.matricula = mtp2.matricula limit 1)  ");
		if(desconsiderarPossivelFormando){
			sqlStr.append(PossiveisFormandosRel.getSqlPossiveisFormandos("and", false, "mat1"));
		}
		
		if(!Uteis.isAtributoPreenchido(turma)){
			StringBuilder in = new StringBuilder("");
			for(CursoVO curso: cursoVOs){
				if(curso.getFiltrarCursoVO()){
					if(in.length() > 0){
						in.append(", ");
					}
					in.append(curso.getCodigo());
				}
			}
			if(in.length() > 0){
				sqlStr.append(" AND curso.codigo in (").append(in.toString()).append(") ");
			}
			in = new StringBuilder("");
			for(TurnoVO turnoVO: turnoVOs){
				if(turnoVO.getFiltrarTurnoVO()){
					if(in.length() > 0){
						in.append(", ");
					}
					in.append(turnoVO.getCodigo());
				}
			}
			if(in.length() > 0){
				sqlStr.append(" AND mat1.turno in (").append(in.toString()).append(") ");
			}
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND turma.codigo = ").append(turma).append(" ");
		}
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND mat1.unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" GROUP BY turma.identificadorturma, mat1.matricula, curso.nome, situacaoMatriculaPeriodo ");
		
		sqlStr.append(" ) as t where situacaoMatriculaPeriodo not in ('PR', 'PC', 'CA', 'TR', 'AC', 'TS', 'TI') ");
		sqlStr.append(" group BY turma,curso ");
		sqlStr.append(" ORDER BY turma,curso ");
		
		try {
			SqlRowSet tabelaResultados = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosAlunoNaoRenovaramMatriculaSintetico(tabelaResultados);
		} finally {
			sqlStr = null;
			semestreAnterior = null;
		}
	}

	private List<AlunosNaoRenovaramRelVO> montarDadosAlunoNaoRenovaramMatricula(SqlRowSet tabelaResultados) throws Exception {
		List<AlunosNaoRenovaramRelVO> listaConsulta = new ArrayList<AlunosNaoRenovaramRelVO>(0);
		while (tabelaResultados.next()) {
			AlunosNaoRenovaramRelVO obj = new AlunosNaoRenovaramRelVO();
			obj.setMatriculaAluno(tabelaResultados.getString("matricula"));
			obj.setNomeAluno(tabelaResultados.getString("aluno"));
			obj.setCurso(tabelaResultados.getString("curso"));
			obj.setTurma(tabelaResultados.getString("turma"));
			obj.setEmail(tabelaResultados.getString("email"));
			obj.setCelular(tabelaResultados.getString("celular"));
			obj.setTurno(tabelaResultados.getString("turno"));
			obj.setTelefoneRes(tabelaResultados.getString("telefoneres"));
			obj.setUltimoAnoSemestre((tabelaResultados.getString("ultimoanosemestre")));
			obj.setPossivelFormando(tabelaResultados.getBoolean("possivelFormando"));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	private List<AlunosNaoRenovaramRelVO> montarDadosAlunoNaoRenovaramMatriculaSintetico(SqlRowSet tabelaResultados) throws Exception {
		List<AlunosNaoRenovaramRelVO> listaConsulta = new ArrayList<AlunosNaoRenovaramRelVO>(0);
		while (tabelaResultados.next()) {
			AlunosNaoRenovaramRelVO obj = new AlunosNaoRenovaramRelVO();
			obj.setCurso(tabelaResultados.getString("curso"));
			obj.setTurma(tabelaResultados.getString("turma"));
			obj.setQtdTurma(tabelaResultados.getString("qtdTurma"));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	@Override
	public String designIReportRelatorioSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeSintetico() + ".jrxml");
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("AlunosNaoRenovaramRel");
	}

	public static String getIdEntidadeSintetico() {
		return ("AlunosNaoRenovaramSinteticoRel");
	}
	


}