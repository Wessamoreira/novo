package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jca.cci.InvalidResultSetAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.secretaria.enumeradores.SituacaoConvocadosEnadeEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.academico.DisciplinaEquivalente;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.MapaConvocacaoEnadeMatriculaInterfaceFacade;

@Repository
@Scope("singleton")
public class MapaConvocacaoEnadeMatricula extends ControleAcesso implements MapaConvocacaoEnadeMatriculaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public MapaConvocacaoEnadeMatricula() {
		super();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDados(MapaConvocacaoEnadeMatriculaVO obj) throws Exception {
		if (obj.getMapaConvocacaoEnadeVO().getCodigo().equals(0)) {
			throw new Exception("Ocorreu um problema na integridade do dado, o campo (MAPA CONVOCAÇÃO ENADE) deve ser informado!");
		}
		if (obj.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("Ocorreu um problema na integridade do dado, o campo (MATRÍCULA) deve ser informado!");
		}
		if (obj.getPeriodoLetivoAtual().getCodigo().equals(0)) {
			throw new Exception("Ocorreu um problema na integridade do dado, o campo (PERÍODO LETIVO ATUAL) de " + obj.getMatriculaVO().getAluno().getNome() + " deve ser informado!");
		}
//		if (obj.getPercentualIntegralizacaoPrevistoDataEnade().equals(0.0)) {
//			throw new Exception("Ocorreu um problema na integridade do dado, o campo (PERCENTUAL INTEGRALIZAÇÃO PREVISTO DATA ENADE) de " + obj.getMatriculaVO().getAluno().getNome() + " deve ser informado!");
//		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MapaConvocacaoEnadeMatriculaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO MapaConvocacaoEnadeMatricula( mapaConvocacaoEnade, matricula, observacao, situacaoConvocadosEnade, periodoLetivoAtual, percentualIntegralizacaoPrevistoDataEnade, percentualIntegralizacaoAtual, percentualIntegralizacaoPossivelCursar, anoIngresso, semestreIngresso, anoAtual, semestreAtual, cargaHorariaAtual ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getMapaConvocacaoEnadeVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getMapaConvocacaoEnadeVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setString(2, obj.getMatriculaVO().getMatricula());
					sqlInserir.setString(3, obj.getObservacao());
					sqlInserir.setString(4, obj.getSituacaoConvocadosEnade().name());
					if (obj.getPeriodoLetivoAtual().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getPeriodoLetivoAtual().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setDouble(6, obj.getPercentualIntegralizacaoPrevistoDataEnade());
					sqlInserir.setDouble(7, obj.getPercentualIntegralizacaoAtual());
					sqlInserir.setDouble(8, obj.getPercentualIntegralizacaoPossivelCursar());
					sqlInserir.setString(9, obj.getAnoIngresso());
					sqlInserir.setString(10, obj.getSemestreIngresso());
					sqlInserir.setString(11, obj.getAnoAtual());
					sqlInserir.setString(12, obj.getSemestreAtual());
					sqlInserir.setDouble(13, obj.getCargaHorariaAtual());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MapaConvocacaoEnadeMatriculaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE MapaConvocacaoEnadeMatricula set mapaConvocacaoEnade=?, matricula=?, observacao=?, situacaoConvocadosEnade=?, periodoLetivoAtual=?, percentualIntegralizacaoPrevistoDataEnade=?, percentualIntegralizacaoAtual=?, percentualIntegralizacaoPossivelCursar=?, anoIngresso=?, semestreIngresso=?, anoAtual=?, semestreAtual=?, cargaHorariaAtual=? WHERE (codigo=?)";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getMapaConvocacaoEnadeVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getMapaConvocacaoEnadeVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setString(2, obj.getMatriculaVO().getMatricula());
				sqlAlterar.setString(3, obj.getObservacao());
				sqlAlterar.setString(4, obj.getSituacaoConvocadosEnade().name());
				if (obj.getPeriodoLetivoAtual().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(5, obj.getPeriodoLetivoAtual().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(5, 0);
				}
				sqlAlterar.setDouble(6, obj.getPercentualIntegralizacaoPrevistoDataEnade());
				sqlAlterar.setDouble(7, obj.getPercentualIntegralizacaoAtual());
				sqlAlterar.setDouble(8, obj.getPercentualIntegralizacaoPossivelCursar());
				sqlAlterar.setString(9, obj.getAnoIngresso());
				sqlAlterar.setString(10, obj.getSemestreIngresso());
				sqlAlterar.setString(11, obj.getAnoAtual());
				sqlAlterar.setString(12, obj.getSemestreAtual());
				sqlAlterar.setDouble(13, obj.getCargaHorariaAtual());
				sqlAlterar.setInt(14, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MapaConvocacaoEnadeMatriculaVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		String sql = "DELETE FROM MapaConvocacaoEnadeMatricula WHERE (codigo=?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMapaConvocacaoEnade(Integer mapaConvocacaoEnade, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		String sql = "DELETE FROM MapaConvocacaoEnadeMatricula WHERE (mapaConvocacaoEnade=?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { mapaConvocacaoEnade });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMapaConvocacaoEnadeMatricula(List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaVOs, Integer mapaConvocacaoEnade, SituacaoConvocadosEnadeEnum situacaoConvocadosEnade, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		if (!mapaConvocacaoEnadeMatriculaVOs.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM mapaConvocacaoEnadeMatricula WHERE mapaConvocacaoEnade = ").append(mapaConvocacaoEnade);
			sb.append(" and codigo not in(");
			boolean virgula = false;
			for (MapaConvocacaoEnadeMatriculaVO mapa : mapaConvocacaoEnadeMatriculaVOs) {
				if (!virgula) {
					sb.append(mapa.getCodigo());
					virgula = true;
				} else {
					sb.append(", ").append(mapa.getCodigo());
				}
			}
			sb.append(") and situacaoConvocadosEnade = '").append(situacaoConvocadosEnade.name()).append("' ");
			getConexao().getJdbcTemplate().update(sb.toString());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMapaConvocacaoEnadeMatriculaVOs(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaVOs, SituacaoConvocadosEnadeEnum situacaoConvocadosEnade, UsuarioVO usuario) throws Exception {
		Iterator<MapaConvocacaoEnadeMatriculaVO> e = mapaConvocacaoEnadeMatriculaVOs.iterator();
		while (e.hasNext()) {
			MapaConvocacaoEnadeMatriculaVO obj = e.next();
			if (obj != null) {
				obj.setMapaConvocacaoEnadeVO(mapaConvocacaoEnadeVO);
				obj.setSituacaoConvocadosEnade(situacaoConvocadosEnade);
				incluir(obj, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMapaConvocacaoEnadeMatriculaVOs(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaVOs, SituacaoConvocadosEnadeEnum situacaoConvocadosEnade, UsuarioVO usuario) throws Exception {
		Iterator<MapaConvocacaoEnadeMatriculaVO> e = mapaConvocacaoEnadeMatriculaVOs.iterator();
		while (e.hasNext()) {
			MapaConvocacaoEnadeMatriculaVO obj = e.next();
			obj.getMapaConvocacaoEnadeVO().setCodigo(mapaConvocacaoEnadeVO.getCodigo());
			obj.setSituacaoConvocadosEnade(situacaoConvocadosEnade);
			incluir(obj, usuario);
		}
	}

	public void carregarDados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario, String ano) throws Exception {
		consultaRapidaPorMapaConvocacaoEnade(mapaConvocacaoEnadeVO, usuario, ano);
	}

	public void consultaRapidaPorMapaConvocacaoEnade(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuarioVO, String ano) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select matricula.*, mapaconvocacaoenadematricula.codigo, mapaconvocacaoenade.codigo mapaconvocacaoenade,  ");
		sb.append(" matricula.matricula, matriculaperiodo.situacaomatriculaperiodo, ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		sb.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");		
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" pessoa.cpf AS \"pessoa.cpf\", pessoa.datanasc AS \"pessoa.datanasc\", ");
		sb.append(" pessoa.rg as \"pessoa.rg\",");
		sb.append(" pessoa.orgaoemissor as \"pessoa.orgaoemissor\",");
		sb.append("	pessoa.endereco as \"pessoa.endereco\",");
		sb.append("	pessoa.cep as \"pessoa.cep\",");
		sb.append("	cidade.nome as \"cidade.nome\",");
		sb.append("	pessoa.setor as \"pessoa.setor\",");
		sb.append(" estado.sigla as \"estado.sigla\",");
		sb.append("	pessoa.email as \"pessoa.email\",");
		sb.append("	pessoa.telefoneres as \"pessoa.telefoneres\",");
		sb.append("	pessoa.telefonerecado  as \"pessoa.telefonerecado\",");
		sb.append("	pessoa.telefonecomer  as \"pessoa.telefonecomer\",");
		sb.append("	pessoa.celular as \"pessoa.celular\",");
		sb.append(" periodoLetivoAtual.codigo AS \"periodoLetivoAtual.codigo\", periodoLetivoAtual.periodoletivo AS \"periodoLetivoAtual.periodoletivo\", ");
		sb.append(" percentualIntegralizacaoAtual, percentualIntegralizacaoPossivelCursar, percentualIntegralizacaoPrevistoDataEnade, mapaconvocacaoenadematricula.observacao, situacaoconvocadosenade, ");
		sb.append(" mapaconvocacaoenadematricula.anoIngresso, mapaconvocacaoenadematricula.semestreIngresso, mapaconvocacaoenadematricula.anoAtual, mapaconvocacaoenadematricula.semestreAtual, mapaconvocacaoenadematricula.cargaHorariaAtual, mapaconvocacaoenade.arquivoalunoingressante AS \"mapaconvocacaoenade.arquivoalunoingressante\", mapaconvocacaoenade.arquivoalunoconcluinte AS \"mapaconvocacaoenade.arquivoalunoconcluinte\", arquivoconcluinte.nome as \"arquivoconcluinte\", arquivoingressante.nome as \"arquivoingressante\", turma.identificadorturma as \"turma\", mae.nome as \"mae.nome\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", unidadeEnsino.codigoies AS \"unidadeEnsino.codigoies\", unidadeEnsino.cidade AS \"unidadeEnsino.cidade\" ");
		sb.append(" from mapaconvocacaoenadematricula ");
		sb.append(" inner join matricula on matricula.matricula = mapaconvocacaoenadematricula.matricula ");
		sb.append(" inner  join curso on matricula.curso = curso.codigo ");
		sb.append(" inner  join turno on matricula.turno = turno.codigo ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" left join filiacao on filiacao.aluno = matricula.aluno and filiacao.tipo = 'MA'");
		sb.append(" left join pessoa as mae on mae.codigo = filiacao.pais ");
		sb.append(" left join cidade on cidade.codigo = pessoa.cidade ");
		sb.append(" left join estado on estado.codigo = cidade.estado ");
		sb.append(" inner join mapaconvocacaoenade on mapaconvocacaoenade.codigo = mapaconvocacaoenadematricula.mapaconvocacaoenade ");
		sb.append(" left join arquivo arquivoconcluinte on arquivoconcluinte.codigo = mapaconvocacaoenade.arquivoalunoconcluinte ");
		sb.append(" left join arquivo arquivoingressante on arquivoingressante.codigo = mapaconvocacaoenade.arquivoalunoingressante ");
		sb.append(" left join periodoletivo periodoLetivoAtual on periodoLetivoAtual.codigo = mapaconvocacaoenadematricula.periodoletivoatual ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = mapaconvocacaoenadematricula.matricula and matriculaperiodo.codigo = ");
		sb.append(" (select matriculaperiodo.codigo from matriculaperiodo where matriculaperiodo.matricula = mapaconvocacaoenadematricula.matricula and matriculaperiodo.ano = '").append(ano).append("' order by matriculaperiodo.semestre desc limit 1)");
		sb.append(" left join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sb.append(" where mapaconvocacaoenadematricula.mapaconvocacaoenade = ").append(mapaConvocacaoEnadeVO.getCodigo());
		sb.append(" order by matricula.unidadeensino, matricula.curso, matricula.turno, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			MapaConvocacaoEnadeMatriculaVO obj = new MapaConvocacaoEnadeMatriculaVO();
			FiliacaoVO filiacao = new FiliacaoVO();
			PessoaVO mae = new PessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMapaConvocacaoEnadeVO().setCodigo(tabelaResultado.getInt("mapaConvocacaoEnade"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaVO().setTurma(tabelaResultado.getString("turma"));
			obj.getMatriculaVO().setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.getEnumPorValor(tabelaResultado.getString("situacaomatriculaperiodo")).getDescricao());
			obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getMatriculaVO().getAluno().setCPF(tabelaResultado.getString("pessoa.cpf"));
			obj.getMatriculaVO().getAluno().setDataNasc(tabelaResultado.getDate("pessoa.datanasc"));
			obj.getMatriculaVO().getAluno().setRG(tabelaResultado.getString("pessoa.RG"));
			obj.getMatriculaVO().getAluno().setOrgaoEmissor(tabelaResultado.getString("pessoa.orgaoemissor"));
			obj.getMatriculaVO().getAluno().setEndereco(tabelaResultado.getString("pessoa.endereco"));
			obj.getMatriculaVO().getAluno().setSetor(tabelaResultado.getString("pessoa.setor"));
			obj.getMatriculaVO().getAluno().setCEP(tabelaResultado.getString("pessoa.cep"));
			obj.getMatriculaVO().getAluno().getCidade().setNome(tabelaResultado.getString("cidade.nome"));
			obj.getMatriculaVO().getAluno().getCidade().getEstado().setSigla(tabelaResultado.getString("estado.sigla"));
			obj.getMatriculaVO().getAluno().setEmail(tabelaResultado.getString("pessoa.email"));
			obj.getMatriculaVO().getAluno().setTelefoneRes(tabelaResultado.getString("pessoa.telefoneres"));
			obj.getMatriculaVO().getAluno().setTelefoneRecado(tabelaResultado.getString("pessoa.telefonerecado"));
			obj.getMatriculaVO().getAluno().setTelefoneComer(tabelaResultado.getString("pessoa.telefonecomer"));
			obj.getMatriculaVO().getAluno().setCelular(tabelaResultado.getString("pessoa.celular"));
			mae.setNome(tabelaResultado.getString("mae.nome"));
			filiacao.setTipo("MA");
			filiacao.setPais(mae);
			obj.getMatriculaVO().getAluno().getFiliacaoVOs().add(filiacao);
			obj.getMatriculaVO().getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getMatriculaVO().getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getMatriculaVO().getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
			obj.getMatriculaVO().getTurno().setNome(tabelaResultado.getString("turno.nome"));
			obj.getMatriculaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));
			obj.getMatriculaVO().getUnidadeEnsino().setCodigoIES(tabelaResultado.getInt("unidadeEnsino.codigoies"));
			obj.setFormacaoAcademicaVO(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorPessoaEEscolaridade(obj.getMatriculaVO().getAluno().getCodigo(), NivelFormacaoAcademica.MEDIO, false, usuarioVO));
			obj.getPeriodoLetivoAtual().setCodigo(tabelaResultado.getInt("periodoLetivoAtual.codigo"));
			obj.getPeriodoLetivoAtual().setPeriodoLetivo(tabelaResultado.getInt("periodoLetivoAtual.periodoletivo"));
			obj.setPercentualIntegralizacaoAtual(tabelaResultado.getDouble("percentualIntegralizacaoAtual"));
			obj.setPercentualIntegralizacaoPossivelCursar(tabelaResultado.getDouble("percentualIntegralizacaoPossivelCursar"));
			obj.setPercentualIntegralizacaoPrevistoDataEnade(tabelaResultado.getDouble("percentualIntegralizacaoPrevistoDataEnade"));
			obj.setObservacao(tabelaResultado.getString("observacao"));
			obj.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.valueOf(tabelaResultado.getString("situacaoConvocadosEnade")));
			obj.setAnoIngresso(tabelaResultado.getString("anoIngresso"));
			obj.setSemestreIngresso(tabelaResultado.getString("semestreIngresso"));
			obj.setAnoAtual(tabelaResultado.getString("anoAtual"));
			obj.setSemestreAtual(tabelaResultado.getString("semestreAtual"));
			obj.setCargaHorariaAtual(tabelaResultado.getDouble("cargaHorariaAtual"));
			obj.setUnidadeEnsino(obj.getMatriculaVO().getUnidadeEnsino().getNome());
			mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setCodigo(tabelaResultado.getInt("mapaconvocacaoenade.arquivoalunoingressante"));
			if(Uteis.isAtributoPreenchido(mapaConvocacaoEnadeVO.getArquivoAlunoIngressante())){
				mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setNome(tabelaResultado.getString("arquivoingressante"));
			}
			mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setCodigo(tabelaResultado.getInt("mapaconvocacaoenade.arquivoalunoconcluinte"));
			if(Uteis.isAtributoPreenchido(mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte())){
				mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setNome(tabelaResultado.getString("arquivoconcluinte"));
			}
			if(obj.getSituacaoConvocadosEnade().name().equals(SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE.name())) {
				mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().add(obj);
			}else if(obj.getSituacaoConvocadosEnade().name().equals(SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO.name())) {
				mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().add(obj);
			}else{
				mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().add(obj);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCargaHorariaAtual(final MapaConvocacaoEnadeMatriculaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE MapaConvocacaoEnadeMatricula set cargaHorariaAtual=? WHERE (codigo=?)";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getCargaHorariaAtual());
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}
//	public void montarDadosAlunosEnade(final MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuario) throws Exception {
//		List<MapaConvocacaoEnadeMatriculaVO> listaAlunosEnade = new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0);
//		if (mapaConvocacaoEnadeMatriculaVO.getLayout().equals("GRADUACAO") || mapaConvocacaoEnadeMatriculaVO.getLayout().equals("GRADUACAO_TECNOLOGICA")) {
//			listaAlunosEnade = consultarAlunoPorAnoSemestreUnidadeEnsinoEnadeSituacao(mapaConvocacaoEnadeMatriculaVO.getAnoAtual(), mapaConvocacaoEnadeMatriculaVO.getSemestreAtual(),  mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getUnidadeEnsinoVO().getCodigo(), usuario, mapaConvocacaoEnadeMatriculaVO);
//		} 
//		List<EnadeCursoVO> cursosDoAlunoCorrente = new ArrayList<EnadeCursoVO>(0);
//		if (!listaAlunosEnade.isEmpty()) {
//			if (mapaConvocacaoEnadeMatriculaVO.getLayout().equals("GRADUACAO") || mapaConvocacaoEnadeMatriculaVO.getLayout().equals("GRADUACAO_TECNOLOGICA")) {
//				cursosDoAlunoCorrente = consultarCursosPorAluno(listaAlunosEnade, mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getUnidadeEnsinoVO().getCodigo(), mapaConvocacaoEnadeMatriculaVO.getAnoAtual(), mapaConvocacaoEnadeMatriculaVO.getSemestreAtual(), usuario, mapaConvocacaoEnadeMatriculaVO);
//			}
//			for (EnadeCursoVO enadeCursoVO : cursosDoAlunoCorrente) {
//				for (MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatricula : listaAlunosEnade) {
//					if (mapaConvocacaoEnadeMatricula.getMatriculaVO().getCurso().getCodigo().equals(enadeCursoVO.getCodigo()) && mapaConvocacaoEnadeMatricula.getMatriculaVO().getEnadeVO().getCodigo().equals(enadeCursoVO.getEnadeVO().getCodigo())) {
//						mapaConvocacaoEnadeMatricula.getListaCursoEnade().add(enadeCursoVO);
//					}
//				}
//			}
//		}
//		mapaConvocacaoEnadeMatriculaVO.setListaAlunoEnade(listaAlunosEnade);
//	}
	

	 
	public List<MapaConvocacaoEnadeMatriculaVO> consultarAlunoPorAnoSemestreUnidadeEnsinoEnadeSituacao(String ano, String semestre,
			Integer unidadeEnsino, UsuarioVO usuario, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from mapaconvocacaoenadematricula  ");
		sqlStr.append(" inner join mapaconvocacaoenadematricula on mapaconvocacaoenadematricula.mapaconvocacaoenade = mapaconvocacaoenade.codigo ");
		sqlStr.append(" inner join enadecurso on mapaconvocacaoenade.enadecurso = enadecurso.codigo  ");
		sqlStr.append(" inner join enade on enadecurso.enade = enade.codigo  ");
		sqlStr.append(" inner join matricula on mapaconvocacaoenadematricula.matricula = matricula.matricula   ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  ");
		sqlStr.append(" inner join curso on curso.codigo = enadecurso.curso   ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino  ");
		sqlStr.append(" where mapaconvocacaoenade.ano = '").append(ano).append("')");
		sqlStr.append(" and mapaconvocacaoenade.semestre =  '").append(semestre).append("') ");
		sqlStr.append(" and unidadeensino.codigo =  ").append(unidadeEnsino);
		sqlStr.append(" and enade.codigo =   ").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo());
		sqlStr.append(" and curso.codigo =  ").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getCodigo());
		sqlStr.append(" and mapaconvocacaoenadematricula.situacaoconvocadosenade = <>  'ALUNO_DISPENSADO'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MapaConvocacaoEnadeMatriculaVO> listaAlunosEnade = new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0);
		while (tabelaResultado.next()) {
			MapaConvocacaoEnadeMatriculaVO alunoEnade = new MapaConvocacaoEnadeMatriculaVO();
			montarDadosEnadeAluno(mapaConvocacaoEnadeMatriculaVO, tabelaResultado, usuario);
			listaAlunosEnade.add(alunoEnade);
		}
		return listaAlunosEnade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static MapaConvocacaoEnadeMatriculaVO montarDadosEnadeAluno(MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, SqlRowSet dadosSQL, UsuarioVO usuario
			) throws Exception {
		mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.codigo"));
		return mapaConvocacaoEnadeMatriculaVO;
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static MapaConvocacaoEnadeMatriculaVO montarDadosEnadeCursoAluno(MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatricula,  SqlRowSet dadosSQL,
			UsuarioVO usuario) throws Exception {
		mapaConvocacaoEnadeMatricula.getMapaConvocacaoEnadeVO().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.codigo"));
				
		return mapaConvocacaoEnadeMatricula;
	}
	public List<EnadeCursoVO> consultarCursosPorAluno(List<EnadeCursoVO> alunoEnadeVOs, int codigoUnidadeEnsino,
			String ano, String semestre, UsuarioVO usuario, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from mapaconvocacaoenadematricula  ");
		sqlStr.append(" inner join mapaconvocacaoenadematricula on mapaconvocacaoenadematricula.mapaconvocacaoenade = mapaconvocacaoenade.codigo ");
		sqlStr.append(" inner join enadecurso on mapaconvocacaoenade.enadecurso = enadecurso.codigo  ");
		sqlStr.append(" inner join enade on enadecurso.enade = enade.codigo  ");
		sqlStr.append(" inner join matricula on mapaconvocacaoenadematricula.matricula = matricula.matricula   ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  ");
		sqlStr.append(" inner join curso on curso.codigo = enadecurso.curso   ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino  ");
		sqlStr.append(" where mapaconvocacaoenade.ano = '").append(ano).append("')");
		sqlStr.append(" and mapaconvocacaoenade.semestre =  '").append(semestre).append("') ");
		sqlStr.append(" and unidadeensino.codigo =  ").append(codigoUnidadeEnsino);
		sqlStr.append(" and enade.codigo =   ").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo());
		sqlStr.append(" and curso.codigo =  ").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getCodigo());
		sqlStr.append(" and matricula.matricula =  '").append(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getMatricula()).append("') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<EnadeCursoVO> listaCursoEnade = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			EnadeCursoVO cursoEnade = new EnadeCursoVO();
			montarDadosEnadeCursoAluno(mapaConvocacaoEnadeMatriculaVO, tabelaResultado, usuario);
			listaCursoEnade.add(cursoEnade);
		}
		return listaCursoEnade;
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaVO> consultaRapidaMapaconvocacaoEnadeMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO,  Integer curso, String situacaoMatricula, UsuarioVO usuarioVO) throws InvalidResultSetAccessException, Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct  matricula.matricula AS \"matricula\", matricula.anoIngresso AS \"anoIngresso\", matricula.semestreIngresso AS \"semestreIngresso\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\", pessoa.rg AS \"pessoa.rg\", pessoa.endereco AS \"pessoa.endereco\", pessoa.endereco AS \"pessoa.endereco\", pessoa.orgaoemissor AS \"pessoa.orgaoemissor\", pessoa.email AS \"pessoa.email\", pessoa.setor AS \"pessoa.setor\", pessoa.cep AS \"pessoa.cep\",  ");
		sb.append(" case when mapaconvocacaoenadematricula.situacaoconvocadosenade = 'ALUNO_CONCLUINTE' then 'Concluinte' ");
		sb.append(" when mapaconvocacaoenadematricula.situacaoconvocadosenade = 'ALUNO_DISPENSADO' then 'Dispensado'  ");
		sb.append(" when mapaconvocacaoenadematricula.situacaoconvocadosenade = 'ALUNO_INGRESSANTE' then 'Ingressante' end as  \"mapaconvocacaoenadematricula.situacaoconvocadosenade\",  ");
		sb.append(" cidade.nome AS \"cidade.nome\", pessoa.celular as \"pessoa.celular\",  matricula.turno AS \"matricula.turno\" from matricula  ");
		sb.append(" inner join mapaconvocacaoenadematricula  on mapaconvocacaoenadematricula.matricula = matricula.matricula ");
		sb.append(" inner join mapaconvocacaoenade on mapaconvocacaoenade.codigo = mapaconvocacaoenadematricula.mapaconvocacaoenade  ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma  ");
		sb.append(" left join cidade on cidade.codigo = pessoa.cidade  ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sb.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(ue -> ue.getFiltrarUnidadeEnsino())) {
			sb.append(" AND matricula.unidadeensino IN (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sb.append(ue.getCodigo()).append(", ");
				}
			}
			sb.append("0) ");
		}
		if (!curso.equals(0)) {
			sb.append(" and matricula.curso = ").append(curso);
		}
		if(Uteis.isAtributoPreenchido(situacaoMatricula) && situacaoMatricula.equals(SituacaoVinculoMatricula.ATIVA.getValor())){
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'CO', 'FO') ");
		}
		sb.append(" and matricula.situacao = '").append(situacaoMatricula).append("' ");
		sb.append(" order by matricula.unidadeensino, matricula.curso, matricula.turno, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setNovoObj(false);
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.setAnoIngresso(tabelaResultado.getString("anoIngresso"));
			mapaConvocacaoEnadeMatriculaVO.getSituacaoConvocadosEnade().setDescricao(tabelaResultado.getString("mapaconvocacaoenadematricula.situacaoconvocadosenade"));
			obj.setSemestreIngresso(tabelaResultado.getString("semestreIngresso"));
			obj.getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getAluno().setCPF(tabelaResultado.getString("pessoa.cpf"));
			obj.getAluno().setRG(tabelaResultado.getString("pessoa.rg"));
			obj.getAluno().setEndereco(tabelaResultado.getString("pessoa.endereco"));
			obj.getAluno().setCEP(tabelaResultado.getString("pessoa.cep"));
			obj.getAluno().setEmail(tabelaResultado.getString("pessoa.email"));
			obj.getAluno().getCidade().setNome(tabelaResultado.getString("cidade.nome"));
			obj.getAluno().setCelular(tabelaResultado.getString("pessoa.celular"));
			obj.getTurno().setCodigo(tabelaResultado.getInt("matricula.turno"));
			listaMatriculaVOs.add(obj);
		}
		return listaMatriculaVOs;
	}
		
	public MapaConvocacaoEnadeMatriculaVO consultaPorMapaConvocacaoEnade(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuarioVO, String ano) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select mapaconvocacaoenadematricula.codigo as \"mapaconvocacaoenadematricula.codigo\", mapaconvocacaoenadematricula.mapaconvocacaoenade as \"mapaconvocacaoenadematricula.mapaconvocacaoenade\",  ");
		sb.append(" matricula.matricula, matriculaperiodo.situacaomatriculaperiodo, ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		sb.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");		
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" pessoa.cpf AS \"pessoa.cpf\", pessoa.datanasc AS \"pessoa.datanasc\", ");
		sb.append(" periodoLetivoAtual.codigo AS \"periodoLetivoAtual.codigo\", periodoLetivoAtual.periodoletivo AS \"periodoLetivoAtual.periodoletivo\", ");
		sb.append(" percentualIntegralizacaoAtual, percentualIntegralizacaoPossivelCursar, percentualIntegralizacaoPrevistoDataEnade, observacao, situacaoconvocadosenade, ");
		sb.append(" mapaconvocacaoenadematricula.anoIngresso, mapaconvocacaoenadematricula.semestreIngresso, mapaconvocacaoenadematricula.anoAtual, mapaconvocacaoenadematricula.semestreAtual, mapaconvocacaoenadematricula.cargaHorariaAtual");
		sb.append(" from mapaconvocacaoenadematricula ");
		sb.append(" inner join matricula on matricula.matricula = mapaconvocacaoenadematricula.matricula ");
		sb.append(" left  join curso on matricula.curso = curso.codigo ");
		sb.append(" left  join turno on matricula.turno = turno.codigo ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" left join periodoletivo periodoLetivoAtual on periodoLetivoAtual.codigo = mapaconvocacaoenadematricula.periodoletivoatual ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = mapaconvocacaoenadematricula.matricula and matriculaperiodo.codigo = ");
		sb.append(" (select matriculaperiodo.codigo from matriculaperiodo where matriculaperiodo.matricula = mapaconvocacaoenadematricula.matricula and matriculaperiodo.ano = '").append(ano).append("' order by matriculaperiodo.semestre desc limit 1)");
		sb.append(" where mapaconvocacaoenadematricula.mapaconvocacaoenade = ").append(mapaConvocacaoEnadeVO.getCodigo());
		sb.append(" order by pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDados(tabelaResultado, usuarioVO, ano));
		}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static MapaConvocacaoEnadeMatriculaVO montarDados(SqlRowSet tabelaResultado, UsuarioVO usuarioVO, String ano) throws Exception {
		MapaConvocacaoEnadeMatriculaVO obj = new MapaConvocacaoEnadeMatriculaVO();
		obj.setNovoObj(false);
		if (obj.getSituacaoConvocadosEnade().name().equals(SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE.name())) {
			obj.getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().add(obj);
		} else if (obj.getSituacaoConvocadosEnade().name().equals(SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO.name())) {
			obj.getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().add(obj);
		} else {
			obj.getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().add(obj);
		}
		return obj;
		}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMapaConvocacaoEnadeMatriculaRel(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaVOs, SituacaoConvocadosEnadeEnum situacaoConvocadosEnade, UsuarioVO usuario) throws Exception {
		Iterator<MapaConvocacaoEnadeMatriculaVO> e = mapaConvocacaoEnadeMatriculaVOs.iterator();
		while (e.hasNext()) {
			MapaConvocacaoEnadeMatriculaVO obj = e.next();
			obj.getMapaConvocacaoEnadeVO().setCodigo(mapaConvocacaoEnadeVO.getCodigo());
			obj.setSituacaoConvocadosEnade(situacaoConvocadosEnade);
			alterar(obj, usuario);
			
		}
	}
	
	}

