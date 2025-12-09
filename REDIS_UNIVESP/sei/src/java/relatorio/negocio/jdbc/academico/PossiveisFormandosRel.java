package relatorio.negocio.jdbc.academico;

import java.io.File;
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
import relatorio.negocio.comuns.academico.PossiveisFormandosRelVO;
import relatorio.negocio.interfaces.academico.PossiveisFormandosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PossiveisFormandosRel extends SuperRelatorio implements PossiveisFormandosRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4507054590187585959L;

	public PossiveisFormandosRel() throws Exception {
	}

	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano,
			String semestre, TurmaVO turmaVO, String filtrarPor) throws Exception {
		if (unidadeEnsinoVOs.isEmpty()) {
			throw new ConsistirException("O campo UNIDADE ENSINO deve ser informado.");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && semestre.trim().isEmpty()) {
			throw new ConsistirException("O campo SEMESTRE deve ser informado.");
		}
		if (!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && (ano.trim().isEmpty() || ano.length() != 4)) {
			throw new ConsistirException("O campo ANO deve ser informado.");
		}

		if (filtrarPor.equals("turma")) {
			if (turmaVO == null || turmaVO.getCodigo().equals(0)) {
				throw new ConsistirException("O campo TURMA deve ser informado.");
			}
		}
	}

//	public String extencaoCurso(List<CursoVO> cursoVOs) throws Exception {
//		StringBuilder cursos = new StringBuilder("");
//		cursoVOs.forEach(u -> {
//			if (u.getFiltrarCursoVO()) {
//				if (cursos.length() > 10) {
//					cursos.append("...");
//				}
//			}
//		});
//		if (cursos.length() > 10) {
//			return cursos.substring(0, 9) + "...";
//		}
//		return cursos.toString();
//	}

	public List<PossiveisFormandosRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, String consultarPor, TurmaVO turmaVO, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs) throws Exception {
		List<PossiveisFormandosRelVO> listaPossiveisFormandosRelVO = new ArrayList<PossiveisFormandosRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(unidadeEnsinoVOs, periodicidadeEnum, ano, semestre, consultarPor, turmaVO, cursoVOs, turnoVOs, new UsuarioVO());
		while (dadosSQL.next()) {
			listaPossiveisFormandosRelVO.add(montarDados(dadosSQL, turmaVO));
		}
		return listaPossiveisFormandosRelVO;
	}

	public PossiveisFormandosRelVO montarDados(SqlRowSet dadosSQL, TurmaVO turmaVO) throws Exception {
		PossiveisFormandosRelVO possiveisFormandosRelVO = new PossiveisFormandosRelVO();
		possiveisFormandosRelVO.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		possiveisFormandosRelVO.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nomealuno"));
		possiveisFormandosRelVO.getMatriculaVO().getAluno().setEmail(dadosSQL.getString("email"));
		possiveisFormandosRelVO.getMatriculaVO().getAluno().setTelefoneRes(dadosSQL.getString("telefones"));
		possiveisFormandosRelVO.getMatriculaVO().getCurso().setNome(dadosSQL.getString("nomecurso"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("nomeunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setCEP(dadosSQL.getString("cepunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().getCidade().setNome(dadosSQL.getString("cidadeunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().getCidade().getEstado().setSigla(dadosSQL.getString("estadounidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setSite(dadosSQL.getString("siteunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setEmail(dadosSQL.getString("emailunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setEndereco(dadosSQL.getString("enderecounidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setNumero(dadosSQL.getString("numerounidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setSetor(dadosSQL.getString("setorunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setMantenedora(dadosSQL.getString("mantenedoraunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setCnpjMantenedora(dadosSQL.getString("cnpjmantenedoraunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setCredenciamento(dadosSQL.getString("credenciamentounidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setCredenciamentoPortaria(dadosSQL.getString("credenciamentoportariaunidadeensino"));
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().getDiretorGeral().getPessoa().setNome(dadosSQL.getString("diretorgeralunidadeensino"));
		
		
		
		possiveisFormandosRelVO.getMatriculaVO().getUnidadeEnsino().setTelefones(dadosSQL.getString("telefonesUnidadeEnsino"));
		possiveisFormandosRelVO.setIdentificadorTurma(turmaVO.getIdentificadorTurma());
		possiveisFormandosRelVO.setTelefones(dadosSQL.getString("telefones"));
		possiveisFormandosRelVO.setEmail(dadosSQL.getString("email"));
		return possiveisFormandosRelVO;
	}

	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, String consultarPor, TurmaVO turmaVO, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select distinct matricula, nomealuno, nomecurso, nomeunidadeensino, cepunidadeensino, cidadeunidadeensino, estadounidadeensino, siteunidadeensino, "); 
		sqlStr.append(" emailunidadeensino, enderecounidadeensino, numerounidadeensino, setorunidadeensino, mantenedoraunidadeensino, telcomercial1unidadeensino, telcomercial2unidadeensino, telcomercial3unidadeensino, cnpjmantenedoraunidadeensino, credenciamentounidadeensino, credenciamentoportariaunidadeensino, diretorgeralunidadeensino, discObgNaoCumprida, cumpriuCargaHorariaDisciplinaOptativa, telefones, telefonesUnidadeEnsino, email ");	
	    sqlStr.append(" from ( select distinct matricula.matricula as matricula, pessoa.nome as nomealuno, curso.nome as nomecurso, unidadeensino.nome as nomeunidadeensino, ");
	    sqlStr.append("  unidadeensino.cep as cepunidadeensino,  cidade.nome as cidadeunidadeensino, estado.sigla as estadounidadeensino, unidadeensino.site as siteunidadeensino, unidadeensino.email as emailunidadeensino, unidadeensino.endereco as enderecounidadeensino,");
	    sqlStr.append(" unidadeensino.numero as numerounidadeensino, unidadeensino.setor as setorunidadeensino, unidadeensino.mantenedora as mantenedoraunidadeensino, unidadeensino.telcomercial1 as telcomercial1unidadeensino, unidadeensino.telcomercial2 as telcomercial2unidadeensino, unidadeensino.telcomercial3 as telcomercial3unidadeensino, unidadeensino.cnpjmantenedora as cnpjmantenedoraunidadeensino, unidadeensino.credenciamento as credenciamentounidadeensino, unidadeensino.credenciamentoportaria as credenciamentoportariaunidadeensino, diretorgeralpessoa.nome as diretorgeralunidadeensino, ");
	
	    sqlStr.append(" agrupartelefone(formatartelefone('', 10, '(99)9999-9999', 11, '(99)99999-9999'), ");
	    sqlStr.append(" formatartelefone(unidadeensino.telcomercial1, 10, '(99)9999-9999', 11, '(99)99999-9999'), ");
	    sqlStr.append(" formatartelefone(unidadeensino.telcomercial2, 10, '(99)9999-9999', 11, '(99)99999-9999'),  ");
	    sqlStr.append(" formatartelefone(unidadeensino.telcomercial3, 10, '(99)9999-9999', 11, '(99)99999-9999'))  as telefonesUnidadeEnsino, ");
		sqlStr.append(
				      " agrupartelefone(formatartelefone(pessoa.telefoneres, 10, '(99)9999-9999', 11, '(99)99999-9999'), ");
		sqlStr.append(" formatartelefone(pessoa.telefonecomer, 10, '(99)9999-9999', 11, '(99)99999-9999'), ");
		sqlStr.append(" formatartelefone(pessoa.telefonerecado, 10, '(99)9999-9999', 11, '(99)99999-9999'),  ");
		sqlStr.append(" formatartelefone(pessoa.celular, 10, '(99)9999-9999', 11, '(99)99999-9999'))  as telefones, pessoa.email, ");
		sqlStr.append(" (select case when count(t.codigo) > 0 then true else false end from (select gradedisciplina.codigo from gradedisciplina ");
		sqlStr.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sqlStr.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual and gradedisciplina.tipodisciplina in ('OB', 'LG') and not exists ( ");
		sqlStr.append(" select historico.codigo from historico ");
		sqlStr.append(" where historico.matricula  = matricula.matricula and historico.matrizcurricular = matricula.gradecurricularatual ");
		sqlStr.append(" and historico.gradedisciplina = gradedisciplina.codigo and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') ) limit 1) as t) as discObgNaoCumprida, ");
		sqlStr.append(" (select case when (cargaHorariaOptativaCumprida >= 0 or cargaHorariaOptativaCumprida IS NULL) then case when (CASE WHEN cargaHorariaOptativaCumprida IS NULL THEN 0 ELSE cargaHorariaOptativaCumprida END) >= cargaHoraria - totalcargahorariaestagio - totalcargahorariaatividadecomplementar - cargahorariaobrigatoria  ");
		sqlStr.append(" then true else false end else true end   ");
		sqlStr.append(" from (select cargahoraria, totalcargahorariaestagio, totalcargahorariaatividadecomplementar, ( ");
		sqlStr.append(" select sum(gradedisciplina.cargahoraria) from gradedisciplina  ");
		sqlStr.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sqlStr.append(" where periodoletivo.gradecurricular = gradecurricular.codigo and gradedisciplina.tipodisciplina not in ('OP', 'LO') ");
		sqlStr.append(" ) as cargahorariaobrigatoria, (select sum(cargahoraria) from  ");
		sqlStr.append(" (select gradedisciplina.cargahoraria from historico   ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sqlStr.append(" where matricula  = matricula.matricula and matrizcurricular = gradecurricular.codigo ");
		sqlStr.append(" and gradedisciplina.tipodisciplina in ('OP', 'LO') and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS', 'CH', 'AE', 'AB', 'CE') ");
		sqlStr.append(" union all ");
		sqlStr.append(" select gradecurriculargrupooptativadisciplina.cargahoraria from historico ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStr.append(" where historico.matricula  = matricula.matricula and historico.matrizcurricular = gradecurricular.codigo ");
		sqlStr.append(" and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS','CH', 'AE' , 'AB' ,'CE'))as t) as cargaHorariaOptativaCumprida from gradecurricular where gradecurricular.codigo = matricula.gradecurricularatual ) as o ");
		sqlStr.append(" ) as cumpriuCargaHorariaDisciplinaOptativa ");
		sqlStr.append(" from matricula    ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula and mp.situacaoMatriculaPeriodo != 'PC' ");
		sqlStr.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" left join cidade on cidade.codigo = unidadeensino.cidade  left join estado on estado.codigo = cidade.estado ");
		sqlStr.append(" left join funcionario diretorgeral on unidadeensino.diretorgeral = diretorgeral.codigo "); 
		sqlStr.append(" left join pessoa diretorgeralpessoa on diretorgeralpessoa.codigo = diretorgeral.pessoa ");
		sqlStr.append(" where matricula.situacao in ('AT', 'FI') and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI') ");
		if (consultarPor.equalsIgnoreCase("turma") && turmaVO != null && turmaVO.getCodigo() != 0) {
			sqlStr.append(" AND matriculaperiodo.turma = ").append(turmaVO.getCodigo()).append(" ");
		}

		if (consultarPor.equalsIgnoreCase("curso")) {
			if (!cursoVOs.isEmpty()) {
				boolean cursoSelecionado = false;
				for (CursoVO cursoVO : cursoVOs) {
					if (cursoVO.getFiltrarCursoVO()) {
						if (!cursoSelecionado) {
							sqlStr.append(" AND matricula.curso in (");
						}
						cursoSelecionado = true;
						sqlStr.append(cursoVO.getCodigo()).append(", ");
					}
				}
				if (cursoSelecionado) {
					sqlStr.append("0)");
				}
			}
		}
		if (consultarPor.equalsIgnoreCase("curso") || consultarPor.equalsIgnoreCase("unidadeensino")) {
			if (!turnoVOs.isEmpty()) {
				boolean turnoSelecionado = false;
				for (TurnoVO turnoVO : turnoVOs) {
					if (turnoVO.getFiltrarTurnoVO()) {
						if (!turnoSelecionado) {
							sqlStr.append(" AND matricula.turno in (");
						}
						turnoSelecionado = true;
						sqlStr.append(turnoVO.getCodigo()).append(", ");
					}
				}
				if (turnoSelecionado) {
					sqlStr.append("0)");
				}
			}
		}
		
		// testar lista preenchida
		if (!unidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" AND matricula.unidadeensino in (");
			for (UnidadeEnsinoVO obj: unidadeEnsinoVOs) {
				sqlStr.append(obj.getCodigo() + ", ");
			}
			sqlStr.append("0)");
		if (!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
			sqlStr.append("AND matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sqlStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		sqlStr.append(" AND curso.periodicidade = '").append(periodicidadeEnum.getValor()).append("' ");
		sqlStr.append(
				" ) as res where res.discObgNaoCumprida = false and res.cumpriuCargaHorariaDisciplinaOptativa = true ");
		sqlStr.append(" order by nomeunidadeensino, nomecurso, nomealuno, matricula ");
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		System.out.println(sqlStr.toString());
		return tabelaResultado;
	}

	public static String getDesignIReportRelatorio(String layout) throws Exception {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator
				+ layout + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("PossiveisFormandosRel");
	}
	
//	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
////		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)){
////			return unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)
////					.map(UnidadeEnsinoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", ", " AND unidadeEnsino.codigo IN (", ") "));
////		}
////		return "";
////	}

	public static StringBuilder getSqlPossiveisFormandos(String andOr, boolean existe, String apelidoMatricula) {
		StringBuilder sqlStrSituacao = new StringBuilder("");
		sqlStrSituacao.append(" ").append(andOr);
		if (existe) {
			sqlStrSituacao.append(" exists ( ");
		} else {
			sqlStrSituacao.append(" not exists (");
		}
		sqlStrSituacao.append(" select m.matricula as matricula ");
		sqlStrSituacao.append(" from matricula  m   ");
		sqlStrSituacao.append(" inner join gradecurricular on  gradecurricular.codigo = m.gradecurricularatual  ");
		sqlStrSituacao.append(" where m.situacao = 'AT' ");
		sqlStrSituacao.append(" and m.matricula = ").append(apelidoMatricula).append(".matricula ");
		sqlStrSituacao.append(
				" group by m.matricula, gradecurricular.cargahoraria,  gradecurricular.totalcargahorariaestagio, gradecurricular.totalcargahorariaatividadecomplementar, gradecurricular.codigo ");
		sqlStrSituacao.append(" having not exists (select gradedisciplina.codigo from gradedisciplina ");
		sqlStrSituacao.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sqlStrSituacao.append(
				" where periodoletivo.gradecurricular = m.gradecurricularatual and gradedisciplina.tipodisciplina in ('OB', 'LG') and gradedisciplina.codigo not in ( ");
		sqlStrSituacao.append(
				" select gd.codigo from gradedisciplina gd inner join historico  on gd.codigo = historico.gradedisciplina ");
		sqlStrSituacao.append(
				" where historico.matricula  = m.matricula and historico.matrizcurricular = m.gradecurricularatual and gd.codigo = gradedisciplina.codigo ");
		sqlStrSituacao.append(
				" and gd.tipodisciplina in ('OB', 'LG') and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') ) limit 1)  ");
		sqlStrSituacao.append(
				" and ((gradecurricular.cargahoraria - (case when gradecurricular.totalcargahorariaestagio is null then 0 else gradecurricular.totalcargahorariaestagio end) ");
		sqlStrSituacao.append(
				" - (case when gradecurricular.totalcargahorariaatividadecomplementar is null then 0 else gradecurricular.totalcargahorariaatividadecomplementar end) = 0 ) ");
		sqlStrSituacao.append(
				" or (gradecurricular.cargahoraria - (case when gradecurricular.totalcargahorariaestagio is null then 0 else gradecurricular.totalcargahorariaestagio end) ");
		sqlStrSituacao.append(
				" - (case when gradecurricular.totalcargahorariaatividadecomplementar is null then 0 else gradecurricular.totalcargahorariaatividadecomplementar end) <=   ");
		sqlStrSituacao.append(
				" (select sum(cargahoraria) from (select sum(gradedisciplina.cargahoraria) as cargahoraria from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sqlStrSituacao.append(
				" where periodoletivo.gradecurricular = gradecurricular.codigo and gradedisciplina.tipodisciplina not in ('OP', 'LO') ");
		sqlStrSituacao.append(
				" and exists (select historico.codigo from historico where historico.matricula = m.matricula and historico.matrizcurricular = gradecurricular.codigo and historico.gradedisciplina  = gradedisciplina.codigo and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS', 'CH', 'AE', 'AB', 'CE') ) ");
		sqlStrSituacao.append(" union all ");
		sqlStrSituacao.append(" select historico.cargahorariadisciplina from historico ");
		sqlStrSituacao.append(
				" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStrSituacao.append(
				" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStrSituacao.append(
				" where historico.matricula  = m.matricula and historico.matrizcurricular = gradecurricular.codigo ");
		sqlStrSituacao.append(
				" and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS','CH', 'AE') and gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");

		sqlStrSituacao.append(" ) as optativas ))) ");
		sqlStrSituacao.append(" ) ");
		return sqlStrSituacao;
	}
}