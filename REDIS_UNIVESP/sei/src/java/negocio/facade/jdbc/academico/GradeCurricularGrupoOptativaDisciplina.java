package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.GradeCurricularGrupoOptativaDisciplinaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class GradeCurricularGrupoOptativaDisciplina extends ControleAcesso implements GradeCurricularGrupoOptativaDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850996705466791441L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		if (gradeCurricularGrupoOptativaDisciplinaVO.isNovoObj()) {
			incluir(gradeCurricularGrupoOptativaDisciplinaVO, situacaoGradeCurricular, usuario);
		} else {
			alterar(gradeCurricularGrupoOptativaDisciplinaVO, situacaoGradeCurricular, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final GradeCurricularGrupoOptativaDisciplinaVO obj, String situacaoGradeCurricular, final UsuarioVO usuario) throws Exception {
		validarDados(obj);
		try {
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO GradeCurricularGrupoOptativaDisciplina ");
					sql.append(" ( disciplina, cargaHoraria, cargaHorariaPratica, modalidadeDisciplina, nrCreditos, ");
					sql.append(" configuracaoAcademico, diversificada, gradeCurricularGrupoOptativa, nrCreditoFinanceiro, disciplinaComposta, formulaCalculoNota, formulaCalculo, horaAula, disciplinaEstagio, ");
					sql.append("  controlarRecuperacaoPelaDisciplinaPrincipal, condicaoUsoRecuperacao, variavelNotaCondicaoUsoRecuperacao, formulaCalculoNotaRecuperada, variavelNotaFormulaCalculoNotaRecuperada, variavelNotaRecuperacao, formulaCalculoNotaRecuperacao, bimestre, utilizarEmissaoXmlDiploma )");
					sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, obj.getDisciplina().getCodigo());
					ps.setInt(x++, obj.getCargaHoraria());
					ps.setInt(x++, obj.getCargaHorariaPratica());
					ps.setString(x++, obj.getModalidadeDisciplina().name());
					ps.setInt(x++, obj.getNrCreditos());
					if (obj.getConfiguracaoAcademico().getCodigo() != 0) {
						ps.setInt(x++, obj.getConfiguracaoAcademico().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setBoolean(x++, obj.getDiversificada());
					ps.setInt(x++, obj.getGradeCurricularGrupoOptativa().getCodigo());
					ps.setDouble(x++, obj.getNrCreditoFinanceiro());
					ps.setBoolean(x++, obj.getDisciplinaComposta());
					ps.setString(x++, obj.getFormulaCalculoNota().name());
					ps.setString(x++, obj.getFormulaCalculo());
					ps.setInt(x++, obj.getHoraAula());
					ps.setBoolean(x++, obj.getDisciplinaEstagio());
					ps.setBoolean(x++, obj.getControlarRecuperacaoPelaDisciplinaPrincipal());
					ps.setString(x++, obj.getCondicaoUsoRecuperacao());
					ps.setString(x++, obj.getVariavelNotaCondicaoUsoRecuperacao());
					ps.setString(x++, obj.getFormulaCalculoNotaRecuperada());
					ps.setString(x++, obj.getVariavelNotaFormulaCalculoNotaRecuperada());
					ps.setString(x++, obj.getVariavelNotaRecuperacao());
					ps.setString(x++, obj.getFormulaCalculoNotaRecuperacao());
					ps.setInt(x++, obj.getBimestre());
					ps.setBoolean(x++, obj.getUtilizarEmissaoXmlDiploma());
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getGradeDisciplinaCompostaFacade().incluirGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(obj, situacaoGradeCurricular, usuario);
			getFacadeFactory().getDisciplinaPreRequisitoFacade().incluirDisciplinaGrupoOptativaPreRequisitos(obj.getCodigo(), obj.getDisciplinaRequisitoVOs(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final GradeCurricularGrupoOptativaDisciplinaVO obj, String situacaoGradeCurricular, final UsuarioVO usuario) throws Exception {
		validarDados(obj);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE GradeCurricularGrupoOptativaDisciplina ");
				sql.append(" SET disciplina = ?, cargaHoraria = ?, cargaHorariaPratica = ?,  modalidadeDisciplina = ?, nrCreditos = ?, ");
				sql.append(" configuracaoAcademico = ?, diversificada = ?, gradeCurricularGrupoOptativa = ?, nrCreditoFinanceiro = ?, disciplinaComposta = ?, formulaCalculoNota = ?, formulaCalculo = ?, horaAula = ?, disciplinaEstagio = ?, ");
				sql.append("  controlarRecuperacaoPelaDisciplinaPrincipal=?, condicaoUsoRecuperacao=?, variavelNotaCondicaoUsoRecuperacao=?, formulaCalculoNotaRecuperada=?, variavelNotaFormulaCalculoNotaRecuperada=?, variavelNotaRecuperacao = ?, formulaCalculoNotaRecuperacao=?, bimestre = ?, utilizarEmissaoXmlDiploma=? ");
				sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, obj.getDisciplina().getCodigo());
				ps.setInt(x++, obj.getCargaHoraria());
				ps.setInt(x++, obj.getCargaHorariaPratica());
				ps.setString(x++, obj.getModalidadeDisciplina().name());
				ps.setInt(x++, obj.getNrCreditos());
				if (obj.getConfiguracaoAcademico().getCodigo() != 0) {
					ps.setInt(x++, obj.getConfiguracaoAcademico().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setBoolean(x++, obj.getDiversificada());
				ps.setInt(x++, obj.getGradeCurricularGrupoOptativa().getCodigo());
				ps.setDouble(x++, obj.getNrCreditoFinanceiro());
				ps.setBoolean(x++, obj.getDisciplinaComposta());
				ps.setString(x++, obj.getFormulaCalculoNota().name());
				ps.setString(x++, obj.getFormulaCalculo());
				ps.setInt(x++, obj.getHoraAula());
				ps.setBoolean(x++, obj.getDisciplinaEstagio());
				ps.setBoolean(x++, obj.getControlarRecuperacaoPelaDisciplinaPrincipal());
				ps.setString(x++, obj.getCondicaoUsoRecuperacao());
				ps.setString(x++, obj.getVariavelNotaCondicaoUsoRecuperacao());
				ps.setString(x++, obj.getFormulaCalculoNotaRecuperada());
				ps.setString(x++, obj.getVariavelNotaFormulaCalculoNotaRecuperada());
				ps.setString(x++, obj.getVariavelNotaRecuperacao());
				ps.setString(x++, obj.getFormulaCalculoNotaRecuperacao());
				ps.setInt(x++, obj.getBimestre());
				ps.setBoolean(x++, obj.getUtilizarEmissaoXmlDiploma());
				ps.setInt(x++, obj.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(obj, situacaoGradeCurricular, usuario);
			return;
		}
		getFacadeFactory().getGradeDisciplinaCompostaFacade().alterarGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(obj, situacaoGradeCurricular, usuario);
		getFacadeFactory().getDisciplinaPreRequisitoFacade().alterarDisciplinaGrupoOptativaPreRequisitos(obj.getCodigo(), obj.getDisciplinaRequisitoVOs(), usuario);
	}

	@Override
	public void validarDados(GradeCurricularGrupoOptativaDisciplinaVO obj) throws ConsistirException {
		if (obj.getDisciplina().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeCurricularGrupoOptativa_disciplina"));
		}
		if (obj.getCargaHoraria() == null || obj.getCargaHoraria() <= 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeCurricularGrupoOptativa_cargaHoraria"));
		}
		if (obj.getDisciplinaComposta() && obj.getGradeDisciplinaCompostaVOs().isEmpty()) {
			throw new ConsistirException("O campo DISCIPLINA COMPOSTA (Disciplinas Grupo Optativa) deve ser informado.");
		} else if (obj.getDisciplinaComposta() && obj.getGradeDisciplinaCompostaVOs().size() == 1) {
			throw new ConsistirException("Deve ser informado no mínimo 2 DISCIPLINAS COMPOSTAS.");
		} else if (!obj.getDisciplinaComposta()) {
			obj.getGradeDisciplinaCompostaVOs().clear();
		}
		if(!obj.getDisciplinaComposta()){
			obj.setControlarRecuperacaoPelaDisciplinaPrincipal(false);
			obj.setCondicaoUsoRecuperacao("");
			obj.setVariavelNotaCondicaoUsoRecuperacao("");
			obj.setFormulaCalculoNotaRecuperada("");
			obj.setVariavelNotaRecuperacao("");
			obj.setVariavelNotaFormulaCalculoNotaRecuperada("");
		}
		if(obj.getControlarRecuperacaoPelaDisciplinaPrincipal()){
			if(obj.getVariavelNotaRecuperacao().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNotaRecuperacao").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getCondicaoUsoRecuperacao().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_condicaoUsoRecuperacao").replace("{0}",  obj.getDisciplina().getNome()));
			}								
			if(obj.getVariavelNotaCondicaoUsoRecuperacao().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNotaCondicaoUsoRecuperacao").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getFormulaCalculoNotaRecuperada().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_formulaCalculoNotaRecuperada").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getVariavelNotaFormulaCalculoNotaRecuperada().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNotaFormulaCalculoNotaRecuperada").replace("{0}",  obj.getDisciplina().getNome()));
			}
		}
		validarDadosFormulaCalculoComposicao(obj);
	}

	@Override
	public void validarDadosFormulaCalculoComposicao(GradeCurricularGrupoOptativaDisciplinaVO obj) throws ConsistirException {
		if (obj.getDisciplinaComposta() && (obj.getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO) || obj.getControlarRecuperacaoPelaDisciplinaPrincipal())) {
			Map<String, GradeDisciplinaCompostaVO> variaveis = new HashMap<String, GradeDisciplinaCompostaVO>();
			if (obj.getFormulaCalculo().trim().isEmpty() && obj.getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO)) {
				throw new ConsistirException("O campo FÓRMULA CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " deve ser informado.");
			}
			obj.setVariavelNotaFormulaCalculoNotaRecuperada(obj.getVariavelNotaFormulaCalculoNotaRecuperada().trim());
			obj.setVariavelNotaCondicaoUsoRecuperacao(obj.getVariavelNotaCondicaoUsoRecuperacao().trim());
			obj.setVariavelNotaRecuperacao(obj.getVariavelNotaRecuperacao().trim());
			obj.setFormulaCalculo(obj.getFormulaCalculo().replace(",", "."));
			obj.setCondicaoUsoRecuperacao(obj.getCondicaoUsoRecuperacao().replace(",", "."));
			obj.setFormulaCalculoNotaRecuperada(obj.getFormulaCalculoNotaRecuperada().replace(",", "."));
			obj.setFormulaCalculoNotaRecuperacao(obj.getFormulaCalculoNotaRecuperacao().replace(",", "."));
			String formula = obj.getFormulaCalculo();
			String formulaRec = obj.getFormulaCalculoNotaRecuperada();
			String condicaoRec = obj.getCondicaoUsoRecuperacao();
			String formulaCalculoRec = obj.getFormulaCalculoNotaRecuperacao();
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : obj.getGradeDisciplinaCompostaVOs()) {
				if (gradeDisciplinaCompostaVO.getVariavelNota().trim().isEmpty()) {
					throw new ConsistirException("O campo VARIAVEL NOTA da disciplina " + gradeDisciplinaCompostaVO.getDisciplina().getNome() + " que faz parte da composição da disciplina " + obj.getDisciplina().getNome() + " deve ser informado.");
				}
				for (String key : variaveis.keySet()) {
					if (key.contains(gradeDisciplinaCompostaVO.getVariavelNota().trim())) {
						throw new ConsistirException("O campo VARIAVEL NOTA da disciplina " + gradeDisciplinaCompostaVO.getDisciplina().getNome() + " que faz parte da composição da disciplina " + obj.getDisciplina().getNome() + " possui a mesma sequencia de caracteres da disciplina " + variaveis.get(key).getDisciplina().getNome() + " inviabilizando o uso desta variável.");
					}
				}
				variaveis.put(gradeDisciplinaCompostaVO.getVariavelNota().trim(), gradeDisciplinaCompostaVO);				
				formula = formula.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
				formulaRec = formulaRec.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
				condicaoRec = condicaoRec.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
				formulaCalculoRec = formulaCalculoRec.replaceAll(gradeDisciplinaCompostaVO.getVariavelNota().trim(), "0.0");
			}
			if(obj.getControlarRecuperacaoPelaDisciplinaPrincipal()){
				Boolean resultado = Uteis.realizarFormulaCondicaoUso(condicaoRec);
				if (resultado == null) {
					throw new ConsistirException("A CONDIÇÃO DE USO NOTA RECUPERAÇÃO da disciplina " + obj.getDisciplina().getNome() + " (Grupo Optativa) informado está incorreta.");
				}
				resultado = Uteis.realizarFormulaCondicaoUso(formulaRec);
				if (resultado == null) {
					throw new ConsistirException("A CONDIÇÃO DE CÁLCULO NOTA RECUPERADA da disciplina " + obj.getDisciplina().getNome() + " (Grupo Optativa) informado está incorreta.");
				}
				if(!formulaCalculoRec.trim().isEmpty()){
				Double nota = Uteis.realizarCalculoFormula(formulaCalculoRec);
				if (nota == null) {
					throw new ConsistirException("A FORMULA DE CÁLCULO NOTA RECUPERAÇÃO da disciplina " + obj.getDisciplina().getNome() + " (Grupo Optativa) informado está incorreta.");
				}
				}
			}

			if(obj.getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO)){
			try {			
				Double resultado = Uteis.realizarCalculoFormulaCalculo(formula);
				if (resultado == null) {
					throw new ConsistirException("A FORMULA DE CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " (Grupo Optativa) informado está incorreta.");
				}
			} catch (ScriptException e) {
				throw new ConsistirException("A FORMULA DE CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " (Grupo Optativa) informado está incorreta.");
			} catch (Exception ex) {
				throw new ConsistirException("A FORMULA DE CÁLCULO da disciplina " + obj.getDisciplina().getNome() + " (Grupo Optativa) informado está incorreta.");
			}
			}
		}
	}

	@Override
	public void incluirGradeCurricularGrupoOptativaDisciplinaVOs(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		for (GradeCurricularGrupoOptativaDisciplinaVO obj : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()) {
			obj.setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
			incluir(obj, situacaoGradeCurricular, usuario);
		}

	}

	@Override
	public void alterarGradeCurricularGrupoOptativaDisciplinaVOs(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		excluirGradeCurricularGrupoOptativaDisciplinaVOs(gradeCurricularGrupoOptativaVO, usuario);
		for (GradeCurricularGrupoOptativaDisciplinaVO obj : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()) {
			obj.setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
			persistir(obj, situacaoGradeCurricular, usuario);
		}
	}

	@Override
	public void excluirGradeCurricularGrupoOptativaDisciplinaVOs(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM GradeCurricularGrupoOptativaDisciplina WHERE gradeCurricularGrupoOptativa =  ").append(gradeCurricularGrupoOptativaVO.getCodigo()).append(" and codigo not in (0 ");
		for (GradeCurricularGrupoOptativaDisciplinaVO obj : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}

	private StringBuilder getSqlConsultaCompleta() {
		// gradeCurricularGrupoOptativa
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.codigo, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.disciplina, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.bimestre, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.cargaHoraria, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.cargaHorariaPratica, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.nrCreditos, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.configuracaoAcademico, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.diversificada, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.gradeCurricularGrupoOptativa, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.disciplinaComposta, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculoNota, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculo, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.horaAula, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.disciplinaEstagio, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.controlarRecuperacaoPelaDisciplinaPrincipal, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.condicaoUsoRecuperacao, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.variavelNotaCondicaoUsoRecuperacao, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculoNotaRecuperada, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.variavelNotaFormulaCalculoNotaRecuperada, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.variavelNotaRecuperacao, ");		
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculoNotaRecuperacao, ");		
		sql.append(" GradeCurricularGrupoOptativaDisciplina.utilizarEmissaoXmlDiploma, ");
		sql.append(" Disciplina.nome as \"Disciplina.nome\", disciplina.abreviatura \"disciplina.abreviatura\" ");
		sql.append(" FROM GradeCurricularGrupoOptativaDisciplina ");
		sql.append(" inner join Disciplina on GradeCurricularGrupoOptativaDisciplina.disciplina = Disciplina.codigo ");

		return sql;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" WHERE GradeCurricularGrupoOptativaDisciplina.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Disciplina Optativa).");
		}
		return montarDados(tabelaResultado, usuario);
	}

	@Override
	public Integer consultarPrimeiroPeriodoLetivoComDisciplinaGrupoOptativaMatrizCurricular(Integer codigoDisciplina, Integer matrizCurricular, Integer codigoPeriodoLetivoPrivilegiar, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT periodoLetivo.codigo FROM GradeCurricularGrupoOptativaDisciplina ");
		sql.append(" INNER JOIN GradeCurricularGrupoOptativa on (GradeCurricularGrupoOptativa.codigo = GradeCurricularGrupoOptativaDisciplina.GradeCurricularGrupoOptativa)");
		sql.append(" INNER JOIN PeriodoLetivo on (PeriodoLetivo.GradeCurricularGrupoOptativa = GradeCurricularGrupoOptativa.codigo)");
		sql.append(" WHERE GradeCurricularGrupoOptativaDisciplina.disciplina = ?");
		sql.append("   AND periodoLetivo.gradeCurricular = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoDisciplina, matrizCurricular });
		Integer codigoPeriodoRetornar = null;
		while (tabelaResultado.next()) {
			Integer periodoLetivo = tabelaResultado.getInt("codigo");
			if (codigoPeriodoRetornar == null) {
				// se tiver nulo já define o primeiro período como sendo o
				// periodo a ser retornado.
				codigoPeriodoRetornar = periodoLetivo;
			}
			if (periodoLetivo.equals(codigoPeriodoLetivoPrivilegiar)) {
				// caso nas proximas interacoes, encontre o periodo
				// que deve ser privilegiado, entao assumisse este como
				// sendo o periodo a ser retornado. Isto é importante, pois
				// um mesmo grupo de optativa pode estar em diversos periodos
				// Assim, procura-se privilegir o periodoLetivo que o aluno esta
				// cursando o mapa de equivalencia, como sendo o periodo em que
				// a disciplina
				// do grupo de optativa será definida como cursada, caso não
				// seja possível
				// assumimos o primeiro periodo da matriz, na qual o grupo de
				// optativa faz parte.
				codigoPeriodoRetornar = periodoLetivo;
			}
		}

		return codigoPeriodoRetornar;
	}

	@Override
	public GradeCurricularGrupoOptativaDisciplinaVO consultarPorDisciplinaMatrizCurricularPeriodoLetivo(Integer codigoDisciplina, Integer matrizCurricular, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" INNER JOIN GradeCurricularGrupoOptativa on (GradeCurricularGrupoOptativa.codigo = GradeCurricularGrupoOptativaDisciplina.GradeCurricularGrupoOptativa)");
		sql.append(" INNER JOIN PeriodoLetivo on (PeriodoLetivo.GradeCurricularGrupoOptativa = GradeCurricularGrupoOptativa.codigo)");
		sql.append(" WHERE GradeCurricularGrupoOptativaDisciplina.disciplina = ?");
		sql.append("   AND periodoLetivo.gradeCurricular = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoDisciplina, matrizCurricular });
		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, usuario);
	}

	@Override
	public List<GradeCurricularGrupoOptativaDisciplinaVO> consultarPorGradeCurricularGrupoOptativa(Integer gradeCurricularGrupoOptativa, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" WHERE GradeCurricularGrupoOptativaDisciplina.gradeCurricularGrupoOptativa = ").append(gradeCurricularGrupoOptativa).append(" ORDER BY Disciplina.nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), usuarioVO);
	}

	private List<GradeCurricularGrupoOptativaDisciplinaVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<GradeCurricularGrupoOptativaDisciplinaVO> gradeCurricularGrupoOptativaDisciplinaVOs = new ArrayList<GradeCurricularGrupoOptativaDisciplinaVO>(0);
		while (rs.next()) {
			gradeCurricularGrupoOptativaDisciplinaVOs.add(montarDados(rs, usuario));
		}
		return gradeCurricularGrupoOptativaDisciplinaVOs;

	}

	// Toda vez que adicionar campo abaixo alterar o metodo
	// getSqlConsultaCompleta em GradeCurricularGrupoOptativa
	private GradeCurricularGrupoOptativaDisciplinaVO montarDados(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		gradeCurricularGrupoOptativaDisciplinaVO.setNovoObj(false);
		gradeCurricularGrupoOptativaDisciplinaVO.setCodigo(rs.getInt("codigo"));
		gradeCurricularGrupoOptativaDisciplinaVO.setBimestre(rs.getInt("bimestre"));
		gradeCurricularGrupoOptativaDisciplinaVO.setCargaHoraria(rs.getInt("cargaHoraria"));
		gradeCurricularGrupoOptativaDisciplinaVO.setCargaHorariaPratica(rs.getInt("cargaHorariaPratica"));
		gradeCurricularGrupoOptativaDisciplinaVO.setNrCreditos(rs.getInt("nrCreditos"));
		gradeCurricularGrupoOptativaDisciplinaVO.setNrCreditoFinanceiro(rs.getDouble("nrCreditoFinanceiro"));
		gradeCurricularGrupoOptativaDisciplinaVO.setHoraAula(rs.getInt("horaAula"));
		gradeCurricularGrupoOptativaDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(rs.getString("modalidadeDisciplina")));
		gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().setNovoObj(false);
		gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().setCodigo(rs.getInt("configuracaoAcademico"));
		gradeCurricularGrupoOptativaDisciplinaVO.getGradeCurricularGrupoOptativa().setCodigo(rs.getInt("gradeCurricularGrupoOptativa"));
		gradeCurricularGrupoOptativaDisciplinaVO.setDiversificada(rs.getBoolean("diversificada"));
		gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaComposta(rs.getBoolean("disciplinaComposta"));
		gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setNovoObj(false);
		gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
		gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setNome(rs.getString("Disciplina.nome"));
		gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setAbreviatura(rs.getString("disciplina.abreviatura"));
		gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaEstagio(rs.getBoolean("disciplinaEstagio"));
		gradeCurricularGrupoOptativaDisciplinaVO.setControlarRecuperacaoPelaDisciplinaPrincipal(rs.getBoolean("controlarRecuperacaoPelaDisciplinaPrincipal"));
		gradeCurricularGrupoOptativaDisciplinaVO.setCondicaoUsoRecuperacao(rs.getString("condicaoUsoRecuperacao"));
		gradeCurricularGrupoOptativaDisciplinaVO.setVariavelNotaCondicaoUsoRecuperacao(rs.getString("variavelNotaCondicaoUsoRecuperacao"));
		gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculoNotaRecuperada(rs.getString("formulaCalculoNotaRecuperada"));
		gradeCurricularGrupoOptativaDisciplinaVO.setVariavelNotaFormulaCalculoNotaRecuperada(rs.getString("variavelNotaFormulaCalculoNotaRecuperada"));
		gradeCurricularGrupoOptativaDisciplinaVO.setVariavelNotaRecuperacao(rs.getString("variavelNotaRecuperacao"));
		gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculoNotaRecuperacao(rs.getString("formulaCalculoNotaRecuperacao"));
		gradeCurricularGrupoOptativaDisciplinaVO.setUtilizarEmissaoXmlDiploma(rs.getBoolean("utilizarEmissaoXmlDiploma"));
		if (Uteis.isAtributoPreenchido(rs.getString("formulaCalculoNota"))) {
			gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculoNota(FormulaCalculoNotaEnum.getEnum(rs.getString("formulaCalculoNota")));
		}
		gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculo(rs.getString("formulaCalculo"));
//		if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaComposta()) {
//			gradeCurricularGrupoOptativaDisciplinaVO.setGradeDisciplinaCompostaVOs(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
//		}
//		gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaRequisitoVOs(DisciplinaPreRequisito.consultarGrupoOptativaPreRequisitos(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo(), false, usuario));
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	@Override
	public void adicionarGradeDisciplinaCompostaVOs(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular) throws Exception {
		getFacadeFactory().getGradeDisciplinaCompostaFacade().validarDados(gradeDisciplinaCompostaVO, situacaoGradeCurricular, gradeCurricularGrupoOptativaDisciplinaVO.getFormulaCalculoNota());
		Integer cargaHorariaTotal = 0;
		int x = 0;
		boolean disciplinaAdd = false;
		for (GradeDisciplinaCompostaVO objExistente : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			if (objExistente.getDisciplina().getCodigo().intValue() == gradeDisciplinaCompostaVO.getDisciplina().getCodigo()) {
				gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs().set(x, gradeDisciplinaCompostaVO);
				disciplinaAdd = true;
			}
			x++;
			cargaHorariaTotal += objExistente.getCargaHoraria();
		}
		if (!disciplinaAdd) {
			cargaHorariaTotal += gradeDisciplinaCompostaVO.getCargaHoraria();
		}
		if (gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria() < cargaHorariaTotal) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_cargaHorariaMaiorCargaHorariaDisciplinaPrincipal"));
		}
		if (!disciplinaAdd) {
			gradeDisciplinaCompostaVO.setOrdem(gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs().size());
			gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs().add(gradeDisciplinaCompostaVO);
		}
	}

	@Override
	public void removerGradeDisciplinaCompostaVOs(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) throws Exception {
		int x = 0;
		for (GradeDisciplinaCompostaVO objExistente : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			if (objExistente.getDisciplina().getCodigo().intValue() == gradeDisciplinaCompostaVO.getDisciplina().getCodigo()) {
				gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs().remove(x);
				return;
			}
			x++;
		}
		x = 0;
		for (GradeDisciplinaCompostaVO objExistente : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			objExistente.setOrdem(x);
			x++;
		}

	}
	
	public void excluirObjDisciplinaPreRequisitoVOs(Integer preRequisito, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, UsuarioVO usuario) throws Exception {
		int index = 0;
		Iterator<DisciplinaPreRequisitoVO> i = gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaRequisitoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(preRequisito)) {
//				if (!objExistente.getCodigo().equals(0)) {
//					getFacadeFactory().getDisciplinaPreRequisitoFacade().excluir(objExistente, usuario);
//				}
				gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaRequisitoVOs().remove(index);
				return;
			}
			index++;
		}
	}
	
	@Override
	public List<GradeCurricularGrupoOptativaDisciplinaVO> consultarPorMatrizCurricularGrupoOptativaComposta(Integer matrizCurricular, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" INNER JOIN GradeCurricularGrupoOptativa on (GradeCurricularGrupoOptativa.codigo = GradeCurricularGrupoOptativaDisciplina.GradeCurricularGrupoOptativa)");
		sql.append(" INNER JOIN PeriodoLetivo on (PeriodoLetivo.GradeCurricularGrupoOptativa = GradeCurricularGrupoOptativa.codigo)");
		sql.append(" WHERE GradeCurricularGrupoOptativaDisciplina.disciplinaComposta = true");
		sql.append("   AND periodoLetivo.gradeCurricular = ").append(matrizCurricular);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), usuario);
	}	

}
