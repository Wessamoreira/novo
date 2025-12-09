package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.TransferenciaTurnoDisciplinaVO;
import negocio.comuns.secretaria.TransferenciaTurnoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilNavegacao;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.TransferenciaTurnoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TransferenciaTurno extends ControleAcesso implements TransferenciaTurnoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TransferenciaTurno() throws Exception {
		super();
		setIdEntidade("TransferenciaTurno");
	}

	public StringBuilder getSqlPadraoConsulta() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select transferenciaturno.codigo, transferenciaturno.matricula, matriculaperiodo.codigo as \"matriculaperiodo.codigo\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", matriculaperiodo.periodoletivomatricula, cursoorigem, turnoorigem, turmaorigem, planofinanceirocursoorigem, turnodestino, ");
		sqlStr.append(" transferenciaturno.turmadestino, planofinanceirocursodestino, condicaopagamentoplanofinanceirocursodestino, unidadeensinocursodestino, responsavel, transferenciaturno.data ");
		sqlStr.append(" from transferenciaturno ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = transferenciaturno.matriculaperiodoorigem ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		return sqlStr;
	}

	@Override
	public List<TransferenciaTurnoVO> consultar(String valorConsulta, String campoConsulta, Integer limite, Integer offset, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsulta();
		String parametro = "";
		if (campoConsulta.equals("matricula")) {
			sqlStr.append(" where transferenciaturno.matricula like ? ");
			parametro = "%" + valorConsulta + "%";
		} else if (campoConsulta.equals("aluno")) {
			sqlStr.append(" where upper(pessoa.nome) like ? ");
			parametro =  "%" + valorConsulta.toUpperCase() + "%";
		} else if (campoConsulta.equals("codigo")) {
			sqlStr.append(" where transferenciaturno.codigo = ? ");
			parametro = valorConsulta;
		}
		sqlStr.append(" ORDER BY trim(pessoa.nome), transferenciaturno.data ");
		if (limite != null && offset != null) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametro);
		return montarDadosBasicos(dadosSQL, nivelMontarDados, usuarioVO);
	}

	@Override
	public TransferenciaTurnoVO consultarPorChavePrimaria(Integer codigo, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr = getSqlPadraoConsulta();
		sqlStr.append(" where transferenciaturno.codigo = ").append(codigo);
		sqlStr.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		return new TransferenciaTurnoVO();
	}

	public List<TransferenciaTurnoVO> montarDadosBasicos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<TransferenciaTurnoVO> transferenciaTurnoVOs = new ArrayList<TransferenciaTurnoVO>(0);
		while (dadosSQL.next()) {
			transferenciaTurnoVOs.add(montarDados(dadosSQL, nivelMontarDados, usuarioVO));
		}
		return transferenciaTurnoVOs;
	}

	public TransferenciaTurnoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		TransferenciaTurnoVO obj = new TransferenciaTurnoVO();
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatriculaPeriodoOrigem().setCodigo(dadosSQL.getInt("matriculaperiodo.codigo"));
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(dadosSQL.getInt("turnoorigem"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getMatriculaPeriodoDestino().getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(dadosSQL.getInt("turnodestino"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.setData(dadosSQL.getDate("data"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getMatriculaPeriodoOrigem().getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(dadosSQL.getInt("cursoorigem"), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioVO));
		obj.getMatriculaPeriodoOrigem().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(dadosSQL.getInt("turmaorigem"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getMatriculaPeriodoOrigem().setPlanoFinanceiroCurso(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(dadosSQL.getInt("planofinanceirocursoorigem"), "", Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getMatriculaPeriodoDestino().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(dadosSQL.getInt("turmadestino"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getMatriculaPeriodoDestino().setPlanoFinanceiroCurso(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(dadosSQL.getInt("planofinanceirocursodestino"), "", Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getMatriculaPeriodoDestino().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(dadosSQL.getInt("condicaopagamentoplanofinanceirocursodestino"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("responsavel"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getMatriculaPeriodoOrigem().setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("periodoletivomatricula"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.setTransferenciaTurnoDisciplinaVOs(getFacadeFactory().getTransferenciaTurnoDisciplinaFacade().consultarPorTransferenciaTurno(obj.getCodigo(), false, nivelMontarDados, usuarioVO));
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransferenciaTurnoVO obj, UsuarioVO usuario) throws Exception {
		TransferenciaTurno.incluir(getIdEntidade(), true, usuario);
		final StringBuilder sqlStr = new StringBuilder("INSERT INTO TransferenciaTurno (matricula, matriculaperiodoorigem, cursoorigem, turnoorigem, turmaorigem, planofinanceirocursoorigem, unidadeensinocursoorigem, turnodestino, turmadestino, planofinanceirocursodestino, condicaopagamentoplanofinanceirocursodestino, unidadeensinocursodestino, responsavel, data) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning codigo");
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());
				sqlInserir.setString(1, obj.getMatriculaPeriodoOrigem().getMatriculaVO().getMatricula());
				sqlInserir.setInt(2, obj.getMatriculaPeriodoOrigem().getCodigo());
				sqlInserir.setInt(3, obj.getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getCodigo());
				sqlInserir.setInt(4, obj.getMatriculaPeriodoOrigem().getMatriculaVO().getTurno().getCodigo());
				sqlInserir.setInt(5, obj.getMatriculaPeriodoOrigem().getTurma().getCodigo());
				sqlInserir.setInt(6, obj.getMatriculaPeriodoOrigem().getPlanoFinanceiroCurso().getCodigo());
				sqlInserir.setInt(7, obj.getMatriculaPeriodoOrigem().getUnidadeEnsinoCursoVO().getCodigo());

				sqlInserir.setInt(8, obj.getMatriculaPeriodoDestino().getMatriculaVO().getTurno().getCodigo());
				sqlInserir.setInt(9, obj.getMatriculaPeriodoDestino().getTurma().getCodigo());
				sqlInserir.setInt(10, obj.getMatriculaPeriodoDestino().getPlanoFinanceiroCurso().getCodigo());
				sqlInserir.setInt(11, obj.getMatriculaPeriodoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
				sqlInserir.setInt(12, obj.getMatriculaPeriodoDestino().getUnidadeEnsinoCursoVO().getCodigo());
				sqlInserir.setInt(13, obj.getResponsavel().getCodigo());
				sqlInserir.setTimestamp(14, UteisData.getDataJDBCTimestamp(obj.getData()));
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return 0;
			}
		}));
		getFacadeFactory().getTransferenciaTurnoDisciplinaFacade().persistirTransferenciaTurnoDisciplinaVOs(obj.getTransferenciaTurnoDisciplinaVOs(), obj.getCodigo(), usuario);
	}

	/**
	 * Método que contém toda a regra de negócio para o funcionamento da
	 * transferência de turno. Nesse método fazemos as seguintes operações:
	 * Atualizamos o turno na matrícula do aluno. Atualizamos a
	 * unidadeEnsinoCurso e a turma do aluno em seu registro de
	 * matriculaPeriodo. Atualiza os registros de
	 * matriculaPeriodoTurmaDisciplina de acordo com a nova lista de disciplinas
	 * e turma. Se o planoFinanceiroCurso do aluno foi alterado, atualizamos o
	 * mesmo na matriculaPeriodo e usamos a rotina da matrícula para fazer a
	 * geração das novas parcelas de acordo com o novo planoFinaceiro.
	 * 
	 * @param transferenciaTurnoVOs
	 *            - Lista que traz os registros de disciplinas e turmas novas do
	 *            aluno.
	 * @param MatriculaPeriodoOrigem
	 *            - MatrículaPeriodo sem as modificações.
	 * @param MatriculaPeriodoDestino
	 *            - MatrículaPeriodo com os novos dados.
	 * @param novaUnidadeEnsinoCursoVO
	 *            - UnidadeEnsinoCurso com os novos dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TransferenciaTurnoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (obj.getTransferenciaTurnoDisciplinaVOs().isEmpty()) {
			throw new Exception("Não foi encontrado nenhum disciplina/turma para realizar a transferência da matrícula!");
		} else if (obj.getTransferenciaTurnoDisciplinaVOs().stream().map(TransferenciaTurnoDisciplinaVO::getListaSelectItemTurma).anyMatch(List::isEmpty)) {
			throw new Exception("O campo Turma Transferir deve ser informado.");
		}
		validarDados(obj.getMatriculaPeriodoDestino(), usuario);
		if (obj.getNovoObj()) {
			executarAlteracaoTurnoMatriculaEUnidadeEnsinoCursoMatriculaPeriodo(obj, usuario);
			executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplina(obj, usuario);
			executarGeracaoParcelasNovaMatriculaPeriodo(obj, configuracaoFinanceiroVO, usuario);
			incluir(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarGeracaoParcelasNovaMatriculaPeriodo(TransferenciaTurnoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (!obj.getMatriculaPeriodoDestino().getPlanoFinanceiroCurso().getCodigo().equals(obj.getMatriculaPeriodoOrigem().getPlanoFinanceiroCurso().getCodigo()) && !obj.getMatriculaPeriodoOrigem().getFinanceiroManual()) {
			getFacadeFactory().getMatriculaPeriodoFacade().atualizarPlanoFinanceiroCursoECondicaoPagamentoDaMatricula(obj.getMatriculaPeriodoOrigem().getCodigo(), obj.getMatriculaPeriodoDestino().getPlanoFinanceiroCurso().getCodigo(), obj.getMatriculaPeriodoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
			MatriculaPeriodoVO matriculaPeriodoClone = new MatriculaPeriodoVO();
			matriculaPeriodoClone.setCodigo(obj.getMatriculaPeriodoOrigem().getCodigo());
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodoClone, NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, configuracaoFinanceiroVO, usuario);
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().alterarPlanoFinanceiroAlunoDescontoProgressivoCondicaoPagamentoPlanoCursoPorMatriculaPeriodo(obj.getMatriculaPeriodoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadrao().getCodigo(), obj.getMatriculaPeriodoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadraoMatricula().getCodigo(), obj.getMatriculaPeriodoDestino().getPlanoFinanceiroCurso().getCodigo(), obj.getMatriculaPeriodoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), obj.getMatriculaPeriodoDestino().getCodigo());
			matriculaPeriodoClone.getTurma().setCodigo(obj.getMatriculaPeriodoDestino().getTurma().getCodigo());
			matriculaPeriodoClone.setUnidadeEnsinoCurso(obj.getMatriculaPeriodoDestino().getUnidadeEnsinoCursoVO().getCodigo());
			UtilNavegacao.executarMetodoControle("RenovarMatriculaControle", "processarEdicaoAutomaticaMatricula", matriculaPeriodoClone, false);
			if (!matriculaPeriodoClone.getMensagemErro().isEmpty() && !matriculaPeriodoClone.getMensagemErro().equals("Dados Atualizados com Sucesso!")) {
				throw new Exception(matriculaPeriodoClone.getMensagemErro());
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoTurnoMatriculaEUnidadeEnsinoCursoMatriculaPeriodo(TransferenciaTurnoVO obj, UsuarioVO usuarioVO) throws Exception {
		obj.getMatriculaPeriodoDestino().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoDestino().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		getFacadeFactory().getMatriculaFacade().atualizarTurnoMatricula(obj.getMatriculaPeriodoOrigem().getMatriculaVO().getMatricula(), obj.getMatriculaPeriodoDestino().getMatriculaVO().getTurno().getCodigo());
		getFacadeFactory().getMatriculaPeriodoFacade().atualizarUnidadeEnsinoCursoETurma(obj.getMatriculaPeriodoOrigem().getCodigo(), obj.getMatriculaPeriodoDestino().getTurma().getCodigo(), obj.getMatriculaPeriodoDestino().getUnidadeEnsinoCursoVO().getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplina(TransferenciaTurnoVO obj, UsuarioVO usuarioVO) throws Exception {
		for (TransferenciaTurnoDisciplinaVO ttd : obj.getTransferenciaTurnoDisciplinaVOs()) {
			boolean alterou = false;
			ttd.getMatriculaPeriodoTurmaDisciplinaVO().setHistoricoVO(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), false, false, usuarioVO));;
			ttd.setTurmaTransferida(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getTurma());
			
			if(Uteis.isAtributoPreenchido(ttd.getTurmaAntiga()) && !ttd.getTurmaAntiga().getCodigo().equals(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo())){
				executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(obj, ttd, usuarioVO);
				alterou = Boolean.TRUE;
			}
			if(Uteis.isAtributoPreenchido(ttd.getTurmaPraticaAntiga()) && !ttd.getTurmaPraticaAntiga().getCodigo().equals(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo())){
				ttd.setTurmaPraticaTransferida(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica());
				executarAlteracaoTurmaPraticaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(obj, ttd, usuarioVO);
				alterou = Boolean.TRUE;
			}
			if(Uteis.isAtributoPreenchido(ttd.getTurmaTeoricaAntiga()) && !ttd.getTurmaTeoricaAntiga().getCodigo().equals(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo())){
				ttd.setTurmaTeoricaTransferida(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica());
				executarAlteracaoTurmaTeoricaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(obj, ttd, usuarioVO);
				alterou = Boolean.TRUE;
			} 
			if (alterou) {
				getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getHistoricoVO(), ttd.getMatriculaPeriodoTurmaDisciplinaVO().getHistoricoVO().getConfiguracaoAcademico(), usuarioVO);
				getFacadeFactory().getHistoricoFacade().alterarFaltaEFrequenciaHistorico(ttd.getMatriculaPeriodoTurmaDisciplinaVO().getHistoricoVO(), false, usuarioVO);
			}
		}
	}

	/**
	 * Preenche os dados relevantes para a operação de transferência utilizando
	 * de consultas específicas.
	 * 
	 * @param MatriculaPeriodoOrigem
	 * @param MatriculaPeriodoDestino
	 * @param novaUnidadeEnsinoCursoVO
	 * @throws Exception
	 */
	@Override
	public void realizarPreenchimentoObjetosParaPersistencia(MatriculaPeriodoVO matriculaPeriodoOrigem, MatriculaPeriodoVO matriculaPeriodoDestino, UsuarioVO usuario) throws Exception {
		if (!matriculaPeriodoOrigem.getFinanceiroManual()) {
			matriculaPeriodoDestino.getUnidadeEnsinoCursoVO().getPlanoFinanceiroCurso().setCodigo(matriculaPeriodoDestino.getPlanoFinanceiroCurso().getCodigo());
			matriculaPeriodoDestino.setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaPeriodoDestino.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		matriculaPeriodoDestino.getUnidadeEnsinoCursoVO().getCurso().setCodigo(matriculaPeriodoOrigem.getMatriculaVO().getCurso().getCodigo());
		matriculaPeriodoDestino.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoTurnoPlanoFinanceiro(matriculaPeriodoDestino.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), matriculaPeriodoDestino.getMatriculaVO().getTurno().getCodigo(), matriculaPeriodoOrigem.getMatriculaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		matriculaPeriodoDestino.setProcessoMatricula(matriculaPeriodoOrigem.getProcessoMatricula());
	}

	/**
	 * Valida os dados da tela para verificar se não há alguma inconsistência e
	 * se realmente a regra de negócio pode continuar a ser persistida.
	 * 
	 * @param matriculaPeriodoVO
	 */
	public void validarDados(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTurno())) {
			throw new Exception("O campo Novo Turno (Transferência de Turno) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma())) {
			throw new Exception("O campo Nova Turma (Transferência de Turno) deve ser informado.");
		}
		if (!matriculaPeriodoVO.getFinanceiroManual()) {
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getPlanoFinanceiroCurso())) {
				throw new Exception("O campo Novo Plano Financeiro (Transferência de Turno) deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso())) {
				throw new Exception("O campo Condição Pagamento (Transferência de Turno) deve ser informado.");
			}
		}
		/**
		 * Adicionada regra para validar se existe Processo Matrícula Calendário
		 * vinculado a UnidadeEnsinoCurso e ProcessoMatricula
		 */
		boolean existeProcessoMatriculaCalendario = getFacadeFactory().getProcessoMatriculaCalendarioFacade().executarVerificarProcessoMatriculaCalendarioTransferenciaTurno(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getTurno().getCodigo(), matriculaPeriodoVO.getProcessoMatricula(), usuarioVO);
		if (!existeProcessoMatriculaCalendario) {
			throw new Exception(UteisJSF.internacionalizar("msg_TransferenciaTurno_calendarioMatriculaCurso"));
		}
	}

	public void validarDadosPermiteTransferenciaTurno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		if (getFacadeFactory().getMatriculaPeriodoFacade().consultarSeAlunoPossuiMaisDeUmaMatriculaPeriodoAtiva(matriculaVO.getMatricula())) {
			throw new Exception("A matrícula: " + matriculaVO.getMatricula() + " possui mais de uma matrícula período ativa. Não é possível transferir um aluno com mais de uma matrícula período ativa.");
		}
		if (!matriculaVO.getGradeCurricularAtual().getCodigo().equals(matriculaPeriodoVO.getGradeCurricular().getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("prt_TransferenciaTurno_realizadoTransferenciaMatrizCurricular"));
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TransferenciaTurno.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TransferenciaTurno.idEntidade = idEntidade;
	}

	@Override
	public boolean verificarRegistroAulaParaAbono(TransferenciaTurnoVO transferenciaTurnoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		boolean existeRegistroAulaParaSerAbonado = false;
		for (TransferenciaTurnoDisciplinaVO obj : transferenciaTurnoVO.getTransferenciaTurnoDisciplinaVOs()) {
			int qtdeHorasRegistroAulaTurmaOrigem = getFacadeFactory().getAbonoFaltaFacade()
					.verificarQtdeHorasRegistroAulaTransferenciaTurma(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getMatricula(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getTurma().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getAno(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getSemestre(), false, usuarioVO);
			int qtdeHorasRegistroAulaTurmaPraticaOrigem = getFacadeFactory().getAbonoFaltaFacade()
					.verificarQtdeHorasRegistroAulaTransferenciaTurma(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getMatricula(), 
							obj.getTurmaPraticaAntiga().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getAno(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getSemestre(), false, usuarioVO);
			int qtdeHorasRegistroAulaTurmaTeoricaOrigem = getFacadeFactory().getAbonoFaltaFacade()
					.verificarQtdeHorasRegistroAulaTransferenciaTurma(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getMatricula(), 
							obj.getTurmaTeoricaAntiga().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getAno(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getSemestre(), false, usuarioVO);
			int qtdeHorasRegistroAulaTurmaDestino = getFacadeFactory().getAbonoFaltaFacade()
					.verificarQtdeHorasRegistroAulaTransferenciaTurma("", 
							obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getAno(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getSemestre(), false, usuarioVO);
			int qtdeHorasRegistroAulaTurmaPraticaDestino = getFacadeFactory().getAbonoFaltaFacade()
					.verificarQtdeHorasRegistroAulaTransferenciaTurma("", 
							obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getAno(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getSemestre(), false, usuarioVO);
			int qtdeHorasRegistroAulaTurmaTeoricaDestino = getFacadeFactory().getAbonoFaltaFacade()
					.verificarQtdeHorasRegistroAulaTransferenciaTurma("", 
							obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							transferenciaTurnoVO.getMatriculaPeriodoOrigem().getAno(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getSemestre(), false, usuarioVO);
			int qtdHorasRegistroAulaTotalDestino = qtdeHorasRegistroAulaTurmaDestino + qtdeHorasRegistroAulaTurmaPraticaDestino + qtdeHorasRegistroAulaTurmaTeoricaDestino;
			
			obj.setQtdeHorasRegistrarAbono(calcularHorasAbono(qtdeHorasRegistroAulaTurmaOrigem, qtdeHorasRegistroAulaTurmaDestino, transferenciaTurnoVO.getMatriculaPeriodoOrigem().getTurma().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo()));
			obj.setQtdeHorasAbonoTurmaPratica(calcularHorasAbono(qtdeHorasRegistroAulaTurmaPraticaOrigem, qtdeHorasRegistroAulaTurmaPraticaDestino, obj.getTurmaPraticaAntiga().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo()));
			obj.setQtdeHorasAbonoTurmaTeorica(calcularHorasAbono(qtdeHorasRegistroAulaTurmaTeoricaOrigem, qtdeHorasRegistroAulaTurmaTeoricaDestino, obj.getTurmaTeoricaAntiga().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo()));
			
			obj.setExisteRegistroAula(obj.getQtdeHorasRegistrarAbono() > 0);
			obj.setExisteRegistroAulaPratica(obj.getQtdeHorasAbonoTurmaPratica() > 0);
			obj.setExisteRegistroAulaTeorica(obj.getQtdeHorasAbonoTurmaTeorica() > 0);
			
			if (!existeRegistroAulaParaSerAbonado) {
				existeRegistroAulaParaSerAbonado = qtdHorasRegistroAulaTotalDestino > 0;
			}
		}
		return existeRegistroAulaParaSerAbonado;
	}
	
	private void executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(TransferenciaTurnoVO transferenciaTurnoVO, TransferenciaTurnoDisciplinaVO transferenciaTurnoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (transferenciaTurnoDisciplinaVO.getExisteRegistroAula()) {
			executarGeracaoAbonoFaltaExclusaoFrequenciaAula(transferenciaTurnoVO.getMatriculaPeriodoOrigem(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getTurma(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), transferenciaTurnoDisciplinaVO.getQtdeHorasRegistrarAbono(), transferenciaTurnoDisciplinaVO.getRealizarAbonoRegistroAula(), usuarioVO);
		}
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarTurmaPorMatriculaPeriodoTurmaDisciplina(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), usuarioVO);
	}
	
	private void executarAlteracaoTurmaPraticaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(TransferenciaTurnoVO transferenciaTurnoVO, TransferenciaTurnoDisciplinaVO transferenciaTurnoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (transferenciaTurnoDisciplinaVO.getExisteRegistroAulaPratica()) {
			executarGeracaoAbonoFaltaExclusaoFrequenciaAula(transferenciaTurnoVO.getMatriculaPeriodoOrigem(), transferenciaTurnoDisciplinaVO.getTurmaPraticaAntiga(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), transferenciaTurnoDisciplinaVO.getQtdeHorasAbonoTurmaPratica(), transferenciaTurnoDisciplinaVO.getRealizarAbonoRegistroAula(), usuarioVO);
		}
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo(), usuarioVO);
	}
	
	private void executarAlteracaoTurmaTeoricaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(TransferenciaTurnoVO transferenciaTurnoVO, TransferenciaTurnoDisciplinaVO transferenciaTurnoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (transferenciaTurnoDisciplinaVO.getExisteRegistroAulaTeorica()) {
			executarGeracaoAbonoFaltaExclusaoFrequenciaAula(transferenciaTurnoVO.getMatriculaPeriodoOrigem(), transferenciaTurnoVO.getMatriculaPeriodoOrigem().getTurma(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), transferenciaTurnoDisciplinaVO.getQtdeHorasAbonoTurmaTeorica(), transferenciaTurnoDisciplinaVO.getRealizarAbonoRegistroAula(), usuarioVO);
		}
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo(), usuarioVO);
	}
	
	private void executarGeracaoAbonoFaltaExclusaoFrequenciaAula(MatriculaPeriodoVO matriculaPeriodoVO, TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, Integer qtdeHorasRegistrarAbono, Boolean realizarAbonoRegistroAula, UsuarioVO usuarioVO) throws Exception {
		if (realizarAbonoRegistroAula && qtdeHorasRegistrarAbono > 0) {
			getFacadeFactory().getAbonoFaltaFacade().executarGeracaoAbonoRegistroAulaTransferenciaTurma(matriculaPeriodoVO, turmaOrigem, turmaDestino, disciplinaVO, qtdeHorasRegistrarAbono, false, usuarioVO);
		}
		getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulaPorMatriculaMatriculaPeriodoTurmaDisciplina(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getCodigo(), turmaOrigem.getCodigo(), disciplinaVO.getCodigo(), false, usuarioVO);
	}
	
	private Integer calcularHorasAbono(Integer horasOrigem, Integer horasDestino, Integer turmaOrigem, Integer turmaDestino) {
		return turmaOrigem.equals(turmaDestino) ? 0 : horasDestino > horasOrigem ? horasOrigem : horasDestino;
	}
	
	public Integer consultarQuantidadeTotalRegistros(String valorConsulta, String campoConsulta) throws Exception {
		String parametro = "";
		StringBuilder sqlStr = new StringBuilder("select count (transferenciaturno.codigo) as quantidade from transferenciaturno ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = transferenciaturno.matriculaperiodoorigem ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		if (campoConsulta.equals("matricula")) {
			sqlStr.append(" where transferenciaturno.matricula like ? ");
			parametro = "%" + valorConsulta + "%";
		} else if (campoConsulta.equals("aluno")) {
			sqlStr.append(" where upper(pessoa.nome) like ? ");
			parametro =  "%" + valorConsulta.toUpperCase() + "%";
		} else if (campoConsulta.equals("codigo")) {
			sqlStr.append(" where transferenciaturno.codigo = ? ");
			parametro = valorConsulta;
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametro);
		return dadosSQL.next() ? new Integer(dadosSQL.getInt("quantidade")) : 0;
	}
}