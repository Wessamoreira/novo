package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DisciplinaAproveitadaAlteradaMatriculaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class DisciplinaAproveitadaAlteradaMatricula extends ControleAcesso implements DisciplinaAproveitadaAlteradaMatriculaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public DisciplinaAproveitadaAlteradaMatricula() {
		super();
	}
	
	public void incluirDisciplinaAproveitadaAlteradaMatricula(List<DisciplinaAproveitadaAlteradaMatriculaVO> disciplinaAproveitadaAlteradaMatriculaVOs, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		validarDados(matriculaVO, disciplinaAproveitadaAlteradaMatriculaVOs);
		for (DisciplinaAproveitadaAlteradaMatriculaVO obj : disciplinaAproveitadaAlteradaMatriculaVOs) {
			obj.getResponsavel().setCodigo(usuario.getCodigo());
			incluir(obj, usuario);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception {
        final String sql = "INSERT INTO DisciplinaAproveitadaAlteradaMatricula( matricula, disciplina, gradeCurricular, media, frequencia, cargaHoraria, ano, "
        		+ "semestre, cidade, instituicao, codigoOrigem, tipoOrigemDisciplinaAproveitadaAlteradaMatricula, dataAlteracao,"
        		+ " responsavel, acao, codigoHistorico, codigoDisciplinaForaGrade, situacao, cargaHorariaCursada, isentarMediaFinal,"
        		+ " tipoHistorico, utilizaNotaConceito, mediaFinalConceito, apresentarAprovadoHistorico, nomeprofessor, titulacaoprofessor, sexoprofessor) "
        		+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
        		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
        		+ " ?, ?, ?, ?, ?, ?, ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
                sqlInserir.setInt(2, obj.getDisciplinaVO().getCodigo().intValue());
                sqlInserir.setInt(3, obj.getGradeCurricularVO().getCodigo().intValue());
                sqlInserir.setDouble(4, obj.getMedia());
                sqlInserir.setDouble(5, obj.getFrequencia());
                sqlInserir.setInt(6, obj.getCargaHoraria());
                sqlInserir.setString(7, obj.getAno());
                sqlInserir.setString(8, obj.getSemestre());
                sqlInserir.setInt(9, obj.getCidadeVO().getCodigo().intValue());
                sqlInserir.setString(10, obj.getInstituicao());
                sqlInserir.setInt(11, obj.getCodigoOrigem().intValue());
                sqlInserir.setString(12, obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().name());
                sqlInserir.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
                sqlInserir.setInt(14, obj.getResponsavel().getCodigo().intValue());
                sqlInserir.setString(15, obj.getAcao());
                sqlInserir.setInt(16, obj.getCodigoHistorico());
                sqlInserir.setInt(17, obj.getCodigoDisciplinaForaGrade());
                sqlInserir.setString(18, obj.getSituacao());
                sqlInserir.setInt(19, obj.getCargaHorariaCursada());
                sqlInserir.setBoolean(20, obj.getIsentarMediaFinal());
                sqlInserir.setString(21, obj.getTipoHistorico());
                sqlInserir.setBoolean(22, obj.getUtilizaNotaConceito());
                sqlInserir.setString(23, obj.getMediaFinalConceito());
                sqlInserir.setBoolean(24, obj.getApresentarAprovadoHistorico());
                sqlInserir.setString(25, obj.getNomeProfessor());
                sqlInserir.setString(26, obj.getTitulacaoProfessor());
                sqlInserir.setString(27, obj.getSexoProfessor());
                return sqlInserir;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
        alterarHistoricoEOrigem(obj, usuario);
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHistoricoEOrigem(DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getIsentarMediaFinal()) {
			obj.setSituacao("IS");
		} else {
			obj.setSituacao("AA");
		}
		if (obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.APROVEITAMENTO_DISCIPLINA)) {
			if (obj.getAcao().equals("ALTERACAO")) {
				getFacadeFactory().getHistoricoFacade().alterarHistoricoAlteracaoAproveitamentoDisciplina(obj.getCodigoHistorico(), obj, usuario);
				getFacadeFactory().getDisciplinaAproveitadasFacade().alterarDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(obj.getCodigoOrigem(), obj, usuario);
			} else if (obj.getAcao().equals("EXCLUSAO")) {
				HistoricoVO historicoExcluir = new HistoricoVO();
				historicoExcluir.setCodigo(obj.getCodigoHistorico());
				getFacadeFactory().getHistoricoFacade().excluir(historicoExcluir, false, usuario);
				DisciplinasAproveitadasVO disciplinasAproveitadasExcluir = new DisciplinasAproveitadasVO();
				disciplinasAproveitadasExcluir.setCodigo(obj.getCodigoOrigem());			
				getFacadeFactory().getDisciplinaAproveitadasFacade().excluir(historicoExcluir.getMatricula().getMatricula(), disciplinasAproveitadasExcluir, usuario);
			}
		} else if (obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.TRANSFERENCIA_ENTRADA)) {
			if (obj.getAcao().equals("ALTERACAO")) {
				getFacadeFactory().getHistoricoFacade().alterarHistoricoAlteracaoAproveitamentoDisciplina(obj.getCodigoHistorico(), obj, usuario);
				getFacadeFactory().getTransferenciaEntradaDisciplinasAproveitadasFacade().alterarTransferenciaEntradaDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(obj.getCodigoOrigem(), obj, usuario);
			} else if (obj.getAcao().equals("EXCLUSAO")) {
				HistoricoVO historicoExcluir = new HistoricoVO();
				historicoExcluir.setCodigo(obj.getCodigoHistorico());
				TransferenciaEntradaDisciplinasAproveitadasVO transferenciaEntradaExcluir = new TransferenciaEntradaDisciplinasAproveitadasVO();
				transferenciaEntradaExcluir.setCodigo(obj.getCodigoOrigem());
				getFacadeFactory().getHistoricoFacade().excluir(historicoExcluir, false, usuario);
				getFacadeFactory().getTransferenciaEntradaDisciplinasAproveitadasFacade().excluir(transferenciaEntradaExcluir);
			}
		} else if (obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.DISCIPLINA_FORA_GRADE)){
			if (obj.getAcao().equals("ALTERACAO")) {
				getFacadeFactory().getDisciplinaForaGradeFacade().alterarDisciplinaForaGradeAlteracaoAproveitamentoDisciplina(obj.getCodigoDisciplinaForaGrade(), obj, usuario);
			} else if (obj.getAcao().equals("EXCLUSAO")) {
				DisciplinaForaGradeVO disciplinaForaGradeExcluir = new DisciplinaForaGradeVO();
				disciplinaForaGradeExcluir.setCodigo(obj.getCodigoDisciplinaForaGrade());
				getFacadeFactory().getDisciplinaForaGradeFacade().excluir(disciplinaForaGradeExcluir, usuario);
			}
		} else if (obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.DISCIPLINA_APROVEITADA_FORA_GRADE)){
			if (obj.getAcao().equals("ALTERACAO")) {
				getFacadeFactory().getDisciplinaAproveitadasFacade().alterarDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(obj.getCodigoOrigem(), obj, usuario);
			} else if (obj.getAcao().equals("EXCLUSAO")) {
				DisciplinasAproveitadasVO disciplinasAproveitadasExcluir = new DisciplinasAproveitadasVO();
				disciplinasAproveitadasExcluir.setCodigo(obj.getCodigoOrigem());				
				getFacadeFactory().getDisciplinaAproveitadasFacade().excluir(obj.getMatriculaVO().getMatricula(), disciplinasAproveitadasExcluir, usuario);
			}
		} else if (obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.HISTORICO)) {
			if (obj.getAcao().equals("ALTERACAO")) {
				getFacadeFactory().getHistoricoFacade().alterarHistoricoAlteracaoAproveitamentoDisciplina(obj.getCodigoHistorico(), obj, usuario);
				getFacadeFactory().getDisciplinaAproveitadasFacade().alterarDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(obj.getCodigoOrigem(), obj, usuario);
			} else if (obj.getAcao().equals("EXCLUSAO")) {
				HistoricoVO historicoExcluir = new HistoricoVO();
				historicoExcluir.setCodigo(obj.getCodigoHistorico());
				getFacadeFactory().getHistoricoFacade().excluir(historicoExcluir, false, usuario);
			}
		}
	}
	
	public void validarDados(MatriculaVO matriculaVO, List<DisciplinaAproveitadaAlteradaMatriculaVO> disciplinaAproveitadaAlteradaMatriculaVOs) throws Exception {
		if (matriculaVO.getMatricula().equals("")) {
			throw new Exception("O campo Matrícula deve ser informado.");
		}
		if (disciplinaAproveitadaAlteradaMatriculaVOs.isEmpty()) {
			throw new Exception("Nenhum aproveitamento encontrado.");
		}
		for (DisciplinaAproveitadaAlteradaMatriculaVO discAproveitada : disciplinaAproveitadaAlteradaMatriculaVOs) {
			if (Uteis.isAtributoPreenchido(discAproveitada.getDataInicioAula()) && Uteis.isAtributoPreenchido(discAproveitada.getDataFimAula())) {
				if (discAproveitada.getDataInicioAula().compareTo(discAproveitada.getDataFimAula()) > 0) {
					throw new Exception("O campo Data Início Aula é superior à Data Fim Aula da disciplina: "+ discAproveitada.getDisciplinaVO().getNome().toUpperCase() +".");
				}
			}
		}
	}
	
	public void validarDadosMatricula(String matricula) throws Exception {
		if (matricula.equals("")) {
			throw new Exception("O campo Matrícula deve ser informado.");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<DisciplinaAproveitadaAlteradaMatriculaVO> consultarAproveitamentoPorMatricula(MatriculaVO matricula, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosMatricula(matricula.getMatricula());
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct historico.codigo, historico.situacao, historico.isentarMediaFinal, matricula.matricula, disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		sb.append("historico.tipoHistorico,  ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then disciplinasaproveitadas.frequencia ");
		sb.append(" when (historico.transferenciaentradadisciplinasaproveitadas is not null and historico.transferenciaentradadisciplinasaproveitadas != 0) then transferenciaentradadisciplinasaproveitadas.frequencia ");
		sb.append(" when (disciplinasaproveitadas.codigo is null and historico.situacao in('AA', 'AE')) then historico.freguencia ");
		sb.append(" end AS frequencia, ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then disciplinasaproveitadas.nota ");
		sb.append(" when (historico.transferenciaentradadisciplinasaproveitadas is not null and historico.transferenciaentradadisciplinasaproveitadas != 0) then transferenciaentradadisciplinasaproveitadas.nota ");
		sb.append(" when (disciplinasaproveitadas.codigo is null and historico.situacao = 'AA') then historico.mediaFinal ");
		sb.append(" end AS nota, ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then CAST(disciplinasaproveitadas.cargaHoraria AS integer) ");
		sb.append(" when (historico.transferenciaentradadisciplinasaproveitadas is not null and historico.transferenciaentradadisciplinasaproveitadas != 0) then CAST(transferenciaentradadisciplinasaproveitadas.cargaHoraria AS integer) ");
		sb.append(" when (disciplinasaproveitadas.codigo is null and historico.situacao = 'AA') then historico.cargaHorariaAproveitamentodisciplina ");
		sb.append(" end AS cargaHoraria, ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then CAST(disciplinasaproveitadas.cargaHorariaCursada AS integer) ");
		sb.append(" when (disciplinasaproveitadas.codigo is null and historico.situacao = 'AA') then historico.cargaHorariaCursada ");
		sb.append(" end AS cargaHorariaCursada, ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then disciplinasaproveitadas.ano ");
		sb.append(" when (disciplinasaproveitadas.codigo is null and historico.situacao = 'AA') then historico.anoHistorico ");
		sb.append(" end AS ano, ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then disciplinasaproveitadas.semestre ");
		sb.append(" when (disciplinasaproveitadas.codigo is null and historico.situacao = 'AA') then historico.semestrehistorico ");
		sb.append(" end AS semestre, ");
		sb.append(" case ");
		sb.append(" when disciplinasaproveitadas.codigo is not null then disciplinasaproveitadas.cidade else historico.cidade   ");
		sb.append(" end AS \"cidade.codigo\",");
		sb.append(" case ");
		sb.append(" when disciplinasaproveitadas.codigo is not null then  cidadeDisciplinaAproveitada.nome else  cidadeHistorico.nome ");
		sb.append(" end AS \"cidade.nome\", ");
		sb.append("");
		sb.append(" case ");
		sb.append(" when disciplinasaproveitadas.codigo is not null then  disciplinasaproveitadas.instituicao else historico.instituicao ");
		sb.append(" end AS instituicaoensino, ");
		sb.append(" case ");
		sb.append(" when (historico.disciplinasaproveitadas is not null and historico.disciplinasaproveitadas != 0 and historico.situacao in('AA', 'AE')) then disciplinasaproveitadas.codigo ");
		sb.append(" when (historico.transferenciaentradadisciplinasaproveitadas is not null and historico.transferenciaentradadisciplinasaproveitadas != 0) then transferenciaentradadisciplinasaproveitadas.codigo ");
		sb.append(" end AS codigoorigem, ");
		sb.append(" case ");
		sb.append(" when (disciplinasaproveitadas.codigo is not null and historico.situacao in('AA', 'AE')) then 'APROVEITAMENTO_DISCIPLINA' ");
		sb.append(" when (historico.transferenciaentradadisciplinasaproveitadas is not null and historico.transferenciaentradadisciplinasaproveitadas != 0) then 'TRANSFERENCIA_ENTRADA' ");
		sb.append(" else 'HISTORICO'");
		sb.append(" end AS tipoOrigem, ");
		sb.append(" case ");
		sb.append(" when disciplinasaproveitadas.codigo is not null then  disciplinasaproveitadas.utilizaNotaConceito else historico.utilizanotafinalconceito ");
		sb.append(" end AS utilizaNotaConceito, ");
		sb.append(" case ");
		sb.append(" when disciplinasaproveitadas.codigo is not null then  disciplinasaproveitadas.mediaFinalConceito else historico.notafinalconceito ");
		sb.append(" end AS mediaFinalConceito, ");
		sb.append(" case ");
		sb.append(" when disciplinasaproveitadas.codigo is not null then  disciplinasaproveitadas.apresentarAprovadoHistorico else historico.apresentarAprovadoHistorico ");
		sb.append(" end AS apresentarAprovadoHistorico,  ");

		sb.append(" disciplinasaproveitadas.nomeprofessor, disciplinasaproveitadas.titulacaoprofessor, disciplinasaproveitadas.sexoprofessor, ");
		sb.append(" historico.dataInicioAula, historico.dataFimAula ");
		
		sb.append(" from historico ");
		sb.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
		sb.append(" inner join matricula on matricula.matricula = historico.matricula and matricula.gradecurricularatual = historico.matrizcurricular ");
		sb.append(" left join disciplinasaproveitadas on disciplinasaproveitadas.codigo = historico.disciplinasaproveitadas ");
		sb.append(" left join transferenciaentradadisciplinasaproveitadas on transferenciaentradadisciplinasaproveitadas.codigo = historico.transferenciaentradadisciplinasaproveitadas ");
		sb.append(" left join cidade cidadeDisciplinaAproveitada on cidadeDisciplinaAproveitada.codigo = disciplinasaproveitadas.cidade ");
		sb.append(" left join cidade cidadeHistorico on cidadeHistorico.codigo = historico.cidade ");		
//		sb.append(" where matricula.matricula = '").append(matricula.getMatricula()).append("' and (historico.situacao in ('AA', 'CC', 'CH', 'IS') or historico.situacao = 'AE' and historico.disciplinasaproveitadas is not null)");
		sb.append(" where matricula.matricula = '").append(matricula.getMatricula()).append("' and (historico.situacao in ('AA', 'CC', 'CH', 'IS') or historico.situacao = 'AE')");
//		sb.append(" and ((disciplinasaproveitadas.codigo is not null and disciplinasaproveitadas.codigo > 0) or (transferenciaentradadisciplinasaproveitadas.codigo is not null and transferenciaentradadisciplinasaproveitadas.codigo > 0)) ");
		sb.append(" union all ");
		sb.append(" select distinct disciplinaforagrade.codigo, disciplinaforagrade.situacao, false AS insentarMediaFinal, inclusaodisciplinaforagrade.matricula, 0 AS \"disciplina.codigo\", disciplina AS \"disciplina.nome\", '' as tipoHistorico, frequencia, nota, cargahoraria, cargahorariaCursada, ano, semestre, ");
		sb.append(" cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", disciplinaforagrade.instituicaoensino, disciplinaforagrade.codigo AS codigoorigem, 'DISCIPLINA_FORA_GRADE', false, '', false, '','', '', cast(null as date), cast(null as date) ");
		sb.append(" from disciplinaforagrade ");
		sb.append(" inner join inclusaodisciplinaforagrade on inclusaodisciplinaforagrade.codigo = disciplinaforagrade.inclusaodisciplinaforagrade ");
		sb.append(" left join cidade on cidade.codigo = disciplinaforagrade.cidade ");
		sb.append(" where matricula = '").append(matricula.getMatricula()).append("' ");		
		sb.append(" union all ");
		sb.append(" select distinct disciplinasaproveitadas.codigo, 'AA' as situacao, false AS insentarMediaFinal, aproveitamentodisciplina.matricula, disciplinasaproveitadas.disciplina AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", '' as tipoHistorico, disciplinasaproveitadas.frequencia, disciplinasaproveitadas.nota, disciplinasaproveitadas.cargahoraria as cargahoraria, disciplinasaproveitadas.cargaHorariaCursada as  cargahorariaCursada, disciplinasaproveitadas.ano, disciplinasaproveitadas.semestre, ");
		sb.append(" cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", disciplinasaproveitadas.instituicao as instituicaoensino, disciplinasaproveitadas.codigo AS codigoorigem, 'DISCIPLINA_APROVEITADA_FORA_GRADE', false, '', false, '','', '', cast(null as date), cast(null as date)  ");
		sb.append(" from aproveitamentodisciplina ");
		sb.append(" inner join disciplinasaproveitadas on disciplinasaproveitadas.aproveitamentodisciplina = aproveitamentodisciplina.codigo  ");
		sb.append(" left join cidade on cidade.codigo = disciplinasaproveitadas.cidade ");
		sb.append(" inner join disciplina on disciplinasaproveitadas.disciplina = disciplina.codigo ");		
		sb.append(" where aproveitamentodisciplina.matricula = '").append(matricula.getMatricula()).append("' ");
		sb.append(" AND disciplinasaproveitadas.disciplinaforagrade = true ");
		sb.append(" and not exists (select historico.codigo from historico where historico.matricula = aproveitamentodisciplina.matricula and disciplinasaproveitadas = disciplinasaproveitadas.codigo ) ");
		sb.append(" order by ano, semestre, \"disciplina.nome\" ");
		List<DisciplinaAproveitadaAlteradaMatriculaVO> listaDisciplinaAproveitadaAlteradaMatriculaVOs = new ArrayList<DisciplinaAproveitadaAlteradaMatriculaVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			DisciplinaAproveitadaAlteradaMatriculaVO obj = new DisciplinaAproveitadaAlteradaMatriculaVO();
			montarDados(tabelaResultado, obj, usuarioVO);
			listaDisciplinaAproveitadaAlteradaMatriculaVOs.add(obj);
		}
		return listaDisciplinaAproveitadaAlteradaMatriculaVOs;
	}
	
	public void montarDados(SqlRowSet dadosSQL, DisciplinaAproveitadaAlteradaMatriculaVO obj,  UsuarioVO usuarioVO) {
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
		obj.setFrequencia(dadosSQL.getDouble("frequencia"));
		obj.setMedia(dadosSQL.getDouble("nota"));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		obj.setCargaHorariaCursada(dadosSQL.getInt("cargaHorariaCursada"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getCidadeVO().setCodigo(dadosSQL.getInt("cidade.codigo"));
		obj.getCidadeVO().setNome(dadosSQL.getString("cidade.nome"));
		obj.setInstituicao(dadosSQL.getString("instituicaoEnsino"));
		obj.setCodigoOrigem(dadosSQL.getInt("codigoOrigem"));
		obj.setTipoOrigemDisciplinaAproveitadaAlteradaMatricula(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.getEnum(dadosSQL.getString("tipoOrigem")));
		obj.setIsentarMediaFinal(dadosSQL.getBoolean("isentarMediaFinal"));
		obj.setTipoHistorico(dadosSQL.getString("tipoHistorico"));
		obj.setNomeProfessor(dadosSQL.getString("nomeprofessor"));
		obj.setTitulacaoProfessor(dadosSQL.getString("titulacaoprofessor"));
		obj.setSexoProfessor(dadosSQL.getString("sexoprofessor"));
		obj.setUtilizaNotaConceito(dadosSQL.getBoolean("utilizaNotaConceito"));
		obj.setMediaFinalConceito(dadosSQL.getString("mediaFinalConceito"));
		obj.setApresentarAprovadoHistorico(dadosSQL.getBoolean("apresentarAprovadoHistorico"));
		obj.setDataInicioAula(dadosSQL.getDate("dataInicioAula"));
		obj.setDataFimAula(dadosSQL.getDate("dataFimAula"));
		if (obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.DISCIPLINA_FORA_GRADE)
				|| obj.getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.DISCIPLINA_APROVEITADA_FORA_GRADE)) {
			obj.setCodigoDisciplinaForaGrade(dadosSQL.getInt("codigo"));
		} else {
			obj.setCodigoHistorico(dadosSQL.getInt("codigo"));
		}
	}

	@Override
	public DisciplinaAproveitadaAlteradaMatriculaVO consultarAproveitamentoPorMatricula(String matricula,
			Integer codigoDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select codigo, nomeprofessor, titulacaoprofessor, sexoprofessor from disciplinaaproveitadaalteradamatricula ");
		sql.append(" where matricula = ? and disciplina = ? order by codigo desc limit 1");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, codigoDisciplina);
		DisciplinaAproveitadaAlteradaMatriculaVO obj = new DisciplinaAproveitadaAlteradaMatriculaVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNomeProfessor(tabelaResultado.getString("nomeprofessor"));
			obj.setTitulacaoProfessor(tabelaResultado.getString("titulacaoprofessor"));
			obj.setSexoProfessor(tabelaResultado.getString("sexoprofessor"));
		}

		return obj;
	}

}
