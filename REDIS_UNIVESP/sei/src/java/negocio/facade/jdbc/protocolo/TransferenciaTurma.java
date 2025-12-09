package negocio.facade.jdbc.protocolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TransferenciaTurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.protocolo.TransferenciaTurmaInterfaceFacade;

@Repository
public class TransferenciaTurma extends ControleAcesso implements TransferenciaTurmaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TransferenciaTurma() throws Exception {
		setIdEntidade("TransferenciaTurma");
	}

	public static void validarDados(TransferenciaTurmaVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getMatriculaVO().getMatricula())) {
			throw new Exception("O campo MATRICULA (Transferência Turma) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getDisciplinaVO())) {
			throw new Exception("O campo DISCIPLINA (Transferência Turma) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTurmaOrigem())) {
			throw new Exception("O campo TURMA ORIGEM (Transferência Turma) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTurmaDestino())) {
			throw new Exception("O campo TURMA DESTINO (Transferência Turma) deve ser informado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final TransferenciaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransferenciaTurmaVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TransferenciaTurma.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO TransferenciaTurma (turmaOrigem, turmaDestino, turmaPraticaOrigem, turmaPraticaDestino, turmaTeoricaOrigem, turmaTeoricaDestino, matricula, disciplina, observacao, data, usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						int x = 0;
						sqlInserir.setInt(++x, obj.getTurmaOrigem().getCodigo());
						sqlInserir.setInt(++x, obj.getTurmaDestino().getCodigo());
						if (Uteis.isAtributoPreenchido(obj.getTurmaPraticaOrigem())) {
							sqlInserir.setInt(++x, obj.getTurmaPraticaOrigem().getCodigo());
						} else {
							sqlInserir.setNull(++x, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getTurmaPraticaDestino())) {
							sqlInserir.setInt(++x, obj.getTurmaPraticaDestino().getCodigo());
						} else {
							sqlInserir.setNull(++x, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getTurmaTeoricaOrigem())) {
							sqlInserir.setInt(++x, obj.getTurmaTeoricaOrigem().getCodigo());
						} else {
							sqlInserir.setNull(++x, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getTurmaTeoricaDestino())) {
							sqlInserir.setInt(++x, obj.getTurmaTeoricaDestino().getCodigo());
						} else {
							sqlInserir.setNull(++x, 0);
						}
						sqlInserir.setString(++x, obj.getMatriculaVO().getMatricula());
						sqlInserir.setInt(++x, obj.getDisciplinaVO().getCodigo());
						sqlInserir.setString(++x, obj.getObservacao());
						sqlInserir.setTimestamp(++x, Uteis.getDataJDBCTimestamp(obj.getData()));
						sqlInserir.setInt(++x, usuarioVO.getCodigo());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(false);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioConsiderandoTurmaTeoricaEPratica(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TransferenciaTurmaVO transferenciaTurmaVO, UsuarioVO usuarioVO) throws ConsistirException {
		final ConsistirException ex = new ConsistirException();
		try {
			if (!transferenciaTurmaVO.getTurmaOrigem().getCodigo().equals(transferenciaTurmaVO.getTurmaDestino().getCodigo())) {
				executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(transferenciaTurmaVOs, transferenciaTurmaVO.getTurmaDestino(), transferenciaTurmaVO.getDisciplinaVO(), transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva(), TipoSubTurmaEnum.GERAL, usuarioVO);
			}
		} catch (Exception e) {
			ex.adicionarListaMensagemErro(e.getMessage());
		}
		if (Uteis.isAtributoPreenchido(transferenciaTurmaVO.getTurmaPraticaOrigem()) && !transferenciaTurmaVO.getTurmaPraticaOrigem().getCodigo().equals(transferenciaTurmaVO.getTurmaPraticaDestino().getCodigo())) {
			try {
				executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(transferenciaTurmaVOs, transferenciaTurmaVO.getTurmaPraticaDestino(), transferenciaTurmaVO.getDisciplinaVO(), transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva(), TipoSubTurmaEnum.PRATICA, usuarioVO);
			} catch (Exception e) {
				ex.adicionarListaMensagemErro(e.getMessage());
			}
		}
		if (Uteis.isAtributoPreenchido(transferenciaTurmaVO.getTurmaTeoricaOrigem()) && !transferenciaTurmaVO.getTurmaTeoricaOrigem().getCodigo().equals(transferenciaTurmaVO.getTurmaTeoricaDestino().getCodigo())) {
			try {
				executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(transferenciaTurmaVOs, transferenciaTurmaVO.getTurmaTeoricaDestino(), transferenciaTurmaVO.getDisciplinaVO(), transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva(), TipoSubTurmaEnum.TEORICA, usuarioVO);
			} catch (Exception e) {
				ex.adicionarListaMensagemErro(e.getMessage());
			}
		}
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		}
	}

	@Override
	public void executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, TipoSubTurmaEnum tipoSubTurmaEnum, UsuarioVO usuarioVO) throws Exception {
		turmaDestino = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaDestino.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(turmaDestino.getCodigo(), disciplinaVO.getCodigo(), ultimaMatriculaPeriodoAtiva.getAno(), ultimaMatriculaPeriodoAtiva.getSemestre(),  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if (Uteis.isAtributoPreenchido(vagaTurmaDisciplinaVO)) {
			turmaDestino.setNrMaximoMatricula(vagaTurmaDisciplinaVO.getNrMaximoMatricula());
		}
		int qtdeAlunosTurma = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarNrAlunosMatriculadosTurmaDisciplina(turmaDestino.getCodigo(), disciplinaVO.getCodigo(), ultimaMatriculaPeriodoAtiva.getAno(), ultimaMatriculaPeriodoAtiva.getSemestre(), true, TipoSubTurmaEnum.GERAL, "", false, turmaDestino.getTurmaAgrupada());
		if (qtdeAlunosTurma + 1 > turmaDestino.getNrMaximoMatricula()) {
			throw new Exception("Número máximo de matrícula para a turma " + turmaDestino.getIdentificadorTurma() + " excedido.");
		}
		verificarChoqueHorario(transferenciaTurmaVOs, turmaDestino, disciplinaVO, ultimaMatriculaPeriodoAtiva, usuarioVO);
	}

	private void verificarChoqueHorario(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = ultimaMatriculaPeriodoAtiva.getMatriculaPeriodoTumaDisciplinaVOs().iterator(); iterator.hasNext();) {
			MatriculaPeriodoTurmaDisciplinaVO mptdVO = iterator.next();
			for (TransferenciaTurmaVO ttVO : transferenciaTurmaVOs) {
				if (mptdVO.getCodigo().equals(ttVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())) {
					matriculaPeriodoTurmaDisciplinaVOs.add((MatriculaPeriodoTurmaDisciplinaVO) mptdVO.clone());
					iterator.remove();
					break;
				}
			}
		}
		getFacadeFactory().getMatriculaPeriodoFacade().executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(ultimaMatriculaPeriodoAtiva, turmaDestino, disciplinaVO, null, ultimaMatriculaPeriodoAtiva.getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), usuarioVO);
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVOs)) {
			ultimaMatriculaPeriodoAtiva.getMatriculaPeriodoTumaDisciplinaVOs().addAll(matriculaPeriodoTurmaDisciplinaVOs);
		}
	}

	public StringBuilder getSqlPadraoConsulta() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select TransferenciaTurma.* from TransferenciaTurma ");
		sqlStr.append("inner join Matricula on Matricula.matricula = TransferenciaTurma.matricula ");
		sqlStr.append("inner join Pessoa on Pessoa.codigo = Matricula.aluno ");
		sqlStr.append("inner join Disciplina on Disciplina.codigo = TransferenciaTurma.disciplina ");
		sqlStr.append("left join Turma as TurmaOrigem on TurmaOrigem.codigo = TransferenciaTurma.turmaOrigem ");
		sqlStr.append("left join Turma as TurmaDestino on TurmaDestino.codigo = TransferenciaTurma.turmaDestino ");
		return sqlStr;
	}

	@Override
	public List<TransferenciaTurmaVO> consultar(String valorConsulta, String campoConsulta, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr = getSqlPadraoConsulta();
		StringBuilder valorConsultaValidado = new StringBuilder(valorConsulta);
		if (campoConsulta.equals("matricula")) {
			sqlStr.append(" where TransferenciaTurma.matricula like ? ");
		} else if (campoConsulta.equals("disciplina")) {
			sqlStr.append(" where Disciplina.nome like ? ");
			valorConsultaValidado.append(PERCENT);
		} else if (campoConsulta.equals("turmaOrigem")) {
			sqlStr.append(" where TurmaOrigem.identificadorTurma like ? ");
			valorConsultaValidado.append(PERCENT);
		} else if (campoConsulta.equals("turmaDestino")) {
			sqlStr.append(" where TurmaDestino.identificadorTurma like ? ");
			valorConsultaValidado.append(PERCENT);
		} else if (campoConsulta.equals("aluno")) {
			sqlStr.append(" where Pessoa.nome ilike ? ");
			valorConsultaValidado = new StringBuilder(PERCENT);
			valorConsultaValidado.append(valorConsulta).append(PERCENT);
		}
		sqlStr.append(" order by Pessoa.nome, TransferenciaTurma.codigo ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsultaValidado.toString());
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}

	@Override
	public TransferenciaTurmaVO consultaRapidaPorChavePrimaria(Integer codigo, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsulta();
		sqlStr.append(" where TransferenciaTurma.codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, unidadeEnsino, usuarioVO);
		}
		return new TransferenciaTurmaVO();
	}

	public TransferenciaTurmaVO montarDados(SqlRowSet tabelaResultado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		TransferenciaTurmaVO obj = new TransferenciaTurmaVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaVO().getMatricula(), unidadeEnsino, NivelMontarDados.BASICO, usuarioVO));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getTurmaOrigem().setCodigo(tabelaResultado.getInt("turmaOrigem"));
		obj.setTurmaOrigem(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaOrigem().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getTurmaDestino().setCodigo(tabelaResultado.getInt("turmaDestino"));
		obj.setTurmaDestino(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaDestino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.setObservacao(tabelaResultado.getString("observacao"));
		obj.setData(tabelaResultado.getDate("data"));
		obj.getUsuarioVO().setCodigo(tabelaResultado.getInt("usuario"));
		obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("turmaTeoricaOrigem"))) {
			obj.setTurmaTeoricaOrigem(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turmaTeoricaOrigem"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("turmaTeoricaDestino"))) {
			obj.setTurmaTeoricaDestino(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turmaTeoricaDestino"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("turmaPraticaOrigem"))) {
			obj.setTurmaPraticaOrigem(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turmaPraticaOrigem"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("turmaPraticaDestino"))) {
			obj.setTurmaPraticaDestino(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turmaPraticaDestino"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		return obj;
	}

	public List<TransferenciaTurmaVO> montarDadosBasicos(SqlRowSet tabelaResultado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<TransferenciaTurmaVO> transferenciaTurmaVOs = new ArrayList<TransferenciaTurmaVO>(0);
		while (tabelaResultado.next()) {
			transferenciaTurmaVOs.add(montarDados(tabelaResultado, unidadeEnsino, usuarioVO));
		}
		return transferenciaTurmaVOs;
	}

	@Override
	public Boolean imprimirDeclaracaoTransferenciaTurma(TransferenciaTurmaVO transferenciaTurmaVO, Integer textoPadraoDeclaracao, UsuarioVO usuario) throws Exception {
		try {
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setMatriculaVO(transferenciaTurmaVO.getMatriculaVO());
			impressaoContratoVO.setTurmaOrigem(transferenciaTurmaVO.getTurmaOrigem());
			impressaoContratoVO.setTurmaDestino(transferenciaTurmaVO.getTurmaDestino());
			impressaoContratoVO.setDisciplinaVO(transferenciaTurmaVO.getDisciplinaVO());
			impressaoContratoVO.setMatriculaPeriodoVO(transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva());
			impressaoContratoVO.setObservacao(transferenciaTurmaVO.getObservacao());
			impressaoContratoVO.setTextoPadraoDeclaracao(textoPadraoDeclaracaoVO.getCodigo());
			textoPadraoDeclaracaoVO.substituirTag(impressaoContratoVO, usuario);
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoPadraoDeclaracaoVO.getTexto());
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TransferenciaTurma.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTransferenciaTurmaVOs(List<TransferenciaTurmaVO> transferenciaTurmaVOs, boolean alterarTurmaOrigem, Integer turmaDestino, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception, ConsistirException {
		for (TransferenciaTurmaVO obj : transferenciaTurmaVOs) {
			executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioConsiderandoTurmaTeoricaEPratica(transferenciaTurmaVOs, obj, usuarioVO);
			executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplina(obj, usuarioVO);
			persistir(obj, verificarAcesso, usuarioVO);
		}
		if (alterarTurmaOrigem) {
			getFacadeFactory().getMatriculaPeriodoFacade().alterarTurmaBaseMatriculaPeriodo(transferenciaTurmaVOs.get(0).getUltimaMatriculaPeriodoAtiva().getCodigo(), turmaDestino);
		}
	}

	@Override
	public boolean verificarRegistroAulaParaAbono(List<TransferenciaTurmaVO> transferenciaTurmaVOs, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		boolean existeRegistroAulaParaSerAbonado = false;
		for (TransferenciaTurmaVO obj : transferenciaTurmaVOs) {
			obj.setQtdeHorasRegistrarAbono(executarVerificacaoExisteRegistroAulaParaAbono(obj.getMatriculaVO().getMatricula(), obj.getUltimaMatriculaPeriodoAtiva().getAno(), obj.getUltimaMatriculaPeriodoAtiva().getSemestre(), obj.getTurmaOrigem().getCodigo(), obj.getTurmaDestino().getCodigo(), obj.getDisciplinaVO().getCodigo(), usuarioVO));
			getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaDestino(), NivelMontarDados.BASICO, usuarioVO);
			if (Uteis.isAtributoPreenchido(obj.getTurmaTeoricaOrigem())) {
				obj.setQtdeHorasRegistrarAbonoTurmaTeorica(executarVerificacaoExisteRegistroAulaParaAbono(obj.getMatriculaVO().getMatricula(), obj.getUltimaMatriculaPeriodoAtiva().getAno(), obj.getUltimaMatriculaPeriodoAtiva().getSemestre(), obj.getTurmaTeoricaOrigem().getCodigo(), obj.getTurmaTeoricaDestino().getCodigo(), obj.getDisciplinaVO().getCodigo(), usuarioVO));
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaTeoricaDestino(), NivelMontarDados.BASICO, usuarioVO);
			}
			if (Uteis.isAtributoPreenchido(obj.getTurmaPraticaOrigem())) {
				obj.setQtdeHorasRegistrarAbonoTurmaPratica(executarVerificacaoExisteRegistroAulaParaAbono(obj.getMatriculaVO().getMatricula(), obj.getUltimaMatriculaPeriodoAtiva().getAno(), obj.getUltimaMatriculaPeriodoAtiva().getSemestre(), obj.getTurmaPraticaOrigem().getCodigo(), obj.getTurmaPraticaDestino().getCodigo(), obj.getDisciplinaVO().getCodigo(), usuarioVO));
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaPraticaDestino(), NivelMontarDados.BASICO, usuarioVO);
			}
			if (obj.getExisteRegistroAula() || obj.isExisteRegistroAulaTurmaTeorica() || obj.isExisteRegistroAulaTurmaPratica()) {
				existeRegistroAulaParaSerAbonado = true;
			}
		}
		return existeRegistroAulaParaSerAbonado;
	}

	private Integer executarVerificacaoExisteRegistroAulaParaAbono(String matricula, String ano, String semestre, Integer turmaOrigem, Integer turmaDestino, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		Integer qtdeHorasRegistroAulaTurmaOrigem = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(matricula, turmaOrigem, disciplina, ano, semestre, false, usuarioVO);
		Integer qtdeHorasRegistroAulaTurmaDestino = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma("", turmaDestino, disciplina, ano, semestre, false, usuarioVO);
		if (qtdeHorasRegistroAulaTurmaOrigem < qtdeHorasRegistroAulaTurmaDestino) {
			qtdeHorasRegistroAulaTurmaDestino = qtdeHorasRegistroAulaTurmaOrigem;
		}
		if (qtdeHorasRegistroAulaTurmaDestino > 0 && !turmaOrigem.equals(turmaDestino)) {
			return qtdeHorasRegistroAulaTurmaDestino;
		}
		return 0;
	}

	@Override
	public List<TransferenciaTurmaVO> executarMontagemDisciplinasParaTransferencia(TransferenciaTurmaVO transferenciaTurmaVO, boolean controlarAcesso, UsuarioVO usuarioVO, Boolean permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais) throws Exception {
		List<TransferenciaTurmaVO> transferenciaTurmaVOs = new ArrayList<TransferenciaTurmaVO>(0);
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaDisciplinaCompostaPorMatriculaPeriodo(transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva().getCodigo(), false, usuarioVO);
		for (MatriculaPeriodoTurmaDisciplinaVO obj : matriculaPeriodoTurmaDisciplinaVOs) {
			if(Uteis.isAtributoPreenchido(obj.getGradeDisciplinaVO().getCodigo())){
				obj.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(obj.getGradeDisciplinaVO().getCodigo(), usuarioVO));
			}else if(Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplinaVO())){
				obj.setGradeCurricularGrupoOptativaDisciplinaVO(getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuarioVO));
			}else if(Uteis.isAtributoPreenchido(obj.getGradeDisciplinaCompostaVO())){
				obj.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(obj.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}
			transferenciaTurmaVOs.add(executarGeracaoTransferenciaTurmaVO(transferenciaTurmaVO, obj, usuarioVO, permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais));
			if (Uteis.isAtributoPreenchido(obj.getGradeDisciplinaVO()) && obj.getGradeDisciplinaVO().getDisciplinaComposta()) {
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVO2s = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeDisciplina(transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva().getCodigo(), obj.getGradeDisciplinaVO().getCodigo(), false, usuarioVO);
				for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVO2s) {
					matriculaPeriodoTurmaDisciplinaVO.setDisciplinaFazParteComposicao(true);
					if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO())){
						matriculaPeriodoTurmaDisciplinaVO.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
						
					}
					transferenciaTurmaVOs.add(executarGeracaoTransferenciaTurmaVO(transferenciaTurmaVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO, permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais));
				}
			}else if (Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplinaVO()) && obj.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaComposta()) {
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVO2s = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeCurricularGrupoOptativaDisciplina(transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva().getCodigo(), obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, usuarioVO);
				for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVO2s) {
					matriculaPeriodoTurmaDisciplinaVO.setDisciplinaFazParteComposicao(true);
					if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO())){
						matriculaPeriodoTurmaDisciplinaVO.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
					}
					transferenciaTurmaVOs.add(executarGeracaoTransferenciaTurmaVO(transferenciaTurmaVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO, permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais));
				}
			}
		}
		return transferenciaTurmaVOs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplina(TransferenciaTurmaVO transferenciaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(transferenciaTurmaVO, transferenciaTurmaVO.getMatriculaPeriodoTurmaDisciplinaVO(), usuarioVO);
		// executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaFazParteComposicao(transferenciaTurmaVO, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaFazParteComposicao(TransferenciaTurmaVO transferenciaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		transferenciaTurmaVO.getMatriculaPeriodoTurmaDisciplinaVO().setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(transferenciaTurmaVO.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().getCodigo(), usuarioVO));
		if (transferenciaTurmaVO.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().getDisciplinaComposta()) {
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeDisciplina(transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva().getCodigo(), transferenciaTurmaVO.getMatriculaPeriodoTurmaDisciplinaVO().getGradeDisciplinaVO().getCodigo(), false, usuarioVO);
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
				executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(transferenciaTurmaVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
				TransferenciaTurmaVO obj = (TransferenciaTurmaVO) transferenciaTurmaVO.clone();
				obj.setDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO.getDisciplina());
				obj.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
				persistir(obj, false, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoTurmaMatriculaPeriodoTurmaDisciplinaAbonoRegistroAula(TransferenciaTurmaVO obj, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (obj.getExisteRegistroAula() && !obj.getTurmaOrigem().getCodigo().equals(obj.getTurmaDestino().getCodigo())) {
			executarGeracaoAbonoFaltaExclusaoFrequenciaAula(obj.getUltimaMatriculaPeriodoAtiva(), obj.getTurmaOrigem(), obj.getTurmaDestino(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), obj.getQtdeHorasRegistrarAbono(), obj.getRealizarAbonoRegistroAula(), usuarioVO);
		}
		if (obj.isExisteRegistroAulaTurmaTeorica() && !obj.getTurmaTeoricaOrigem().getCodigo().equals(obj.getTurmaTeoricaDestino().getCodigo())) {
			executarGeracaoAbonoFaltaExclusaoFrequenciaAula(obj.getUltimaMatriculaPeriodoAtiva(), obj.getTurmaTeoricaOrigem(), obj.getTurmaTeoricaDestino(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), obj.getQtdeHorasRegistrarAbonoTurmaTeorica(), obj.getRealizarAbonoRegistroAula(), usuarioVO);
		}
		if (obj.isExisteRegistroAulaTurmaPratica() && !obj.getTurmaPraticaOrigem().getCodigo().equals(obj.getTurmaPraticaDestino().getCodigo())) {
			executarGeracaoAbonoFaltaExclusaoFrequenciaAula(obj.getUltimaMatriculaPeriodoAtiva(), obj.getTurmaPraticaOrigem(), obj.getTurmaPraticaDestino(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), obj.getQtdeHorasRegistrarAbonoTurmaPratica(), obj.getRealizarAbonoRegistroAula(), usuarioVO);
		}
		Integer turmaBase = !obj.getTurmaOrigem().getCodigo().equals(obj.getTurmaDestino().getCodigo()) ?  obj.getTurmaDestino().getCodigo() : 0;
		Integer turmaPratica = Uteis.isAtributoPreenchido(obj.getTurmaPraticaOrigem()) && !obj.getTurmaPraticaOrigem().getCodigo().equals(obj.getTurmaPraticaDestino().getCodigo()) ?  obj.getTurmaPraticaDestino().getCodigo() : 0;
		Integer turmaTeorica =Uteis.isAtributoPreenchido(obj.getTurmaTeoricaOrigem()) && !obj.getTurmaTeoricaOrigem().getCodigo().equals(obj.getTurmaTeoricaDestino().getCodigo()) ?  obj.getTurmaTeoricaDestino().getCodigo() : 0;		
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().executarAtualizacaoTurmaBaseTurmaPraticaTurmaTeoricaPorTransferenciaTurma(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), turmaBase, turmaPratica, turmaTeorica, usuarioVO);
		
		// incluir historiconotaparcial caso já tenha uma estrutura definida
		
		HistoricoVO historicoVO = new HistoricoVO();
		historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplinaDadosBasicos(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), false, usuarioVO);
		
		
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica())) {
			List<TurmaDisciplinaNotaParcialVO> listTurmaDisciplinaNotaParcial = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
			listTurmaDisciplinaNotaParcial = getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().consultarPorTurmaDisciplina(obj.getTurmaPraticaDestino(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), historicoVO.getConfiguracaoAcademico().getCodigo(), usuarioVO, Uteis.NIVELMONTARDADOS_TODOS);
			if(Uteis.isAtributoPreenchido(listTurmaDisciplinaNotaParcial)) {
				for(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO : listTurmaDisciplinaNotaParcial) {					
					HistoricoNotaParcialVO historicoNotaParcialVO = new HistoricoNotaParcialVO();					
					historicoNotaParcialVO.setHistorico(historicoVO);
					historicoNotaParcialVO.setTurmaDisciplinaNotaParcial(turmaDisciplinaNotaParcialVO);
					historicoNotaParcialVO.setTipoNota(turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().getNota());
					List<HistoricoNotaParcialVO> listHistoricoNotaParcialVO = new ArrayList<HistoricoNotaParcialVO>(0);
					listHistoricoNotaParcialVO = getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistoricoTurmaDisciplinaNotaParcialTurmaDisciplinaNotaTitulo(historicoNotaParcialVO, usuarioVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
					if(!Uteis.isAtributoPreenchido(listHistoricoNotaParcialVO)) {
						getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().incluir(historicoNotaParcialVO, usuarioVO);
					}
				}
			}
			
		}
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica())) {
			List<TurmaDisciplinaNotaParcialVO> listTurmaDisciplinaNotaParcial = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
			listTurmaDisciplinaNotaParcial = getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().consultarPorTurmaDisciplina(obj.getTurmaTeoricaDestino(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), historicoVO.getConfiguracaoAcademico().getCodigo(), usuarioVO, Uteis.NIVELMONTARDADOS_TODOS);
			if(Uteis.isAtributoPreenchido(listTurmaDisciplinaNotaParcial)) {
				for(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO : listTurmaDisciplinaNotaParcial) {
					HistoricoNotaParcialVO historicoNotaParcialVO = new HistoricoNotaParcialVO();
					historicoNotaParcialVO.setHistorico(historicoVO);
					historicoNotaParcialVO.setTurmaDisciplinaNotaParcial(turmaDisciplinaNotaParcialVO);
					historicoNotaParcialVO.setTipoNota(turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().getNota());
					List<HistoricoNotaParcialVO> listHistoricoNotaParcialVO = new ArrayList<HistoricoNotaParcialVO>(0);
					listHistoricoNotaParcialVO = getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistoricoTurmaDisciplinaNotaParcialTurmaDisciplinaNotaTitulo(historicoNotaParcialVO, usuarioVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
					if(!Uteis.isAtributoPreenchido(listHistoricoNotaParcialVO)) {
						getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().incluir(historicoNotaParcialVO, usuarioVO);
					}
				}
			}
			
		}
		
		if(!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica()) && !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica())) {
			List<TurmaDisciplinaNotaParcialVO> listTurmaDisciplinaNotaParcial = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
			listTurmaDisciplinaNotaParcial = getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().consultarPorTurmaDisciplina(obj.getTurmaDestino(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), historicoVO.getConfiguracaoAcademico().getCodigo(), usuarioVO, Uteis.NIVELMONTARDADOS_TODOS);
			if(Uteis.isAtributoPreenchido(listTurmaDisciplinaNotaParcial)) {
				for(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO : listTurmaDisciplinaNotaParcial) {
					HistoricoNotaParcialVO historicoNotaParcialVO = new HistoricoNotaParcialVO();
					historicoNotaParcialVO.setHistorico(historicoVO);
					historicoNotaParcialVO.setTurmaDisciplinaNotaParcial(turmaDisciplinaNotaParcialVO);
					historicoNotaParcialVO.setTipoNota(turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().getNota());
					List<HistoricoNotaParcialVO> listHistoricoNotaParcialVO = new ArrayList<HistoricoNotaParcialVO>(0);
					listHistoricoNotaParcialVO = getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistoricoTurmaDisciplinaNotaParcialTurmaDisciplinaNotaTitulo(historicoNotaParcialVO, usuarioVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
					if(!Uteis.isAtributoPreenchido(listHistoricoNotaParcialVO)) {
						getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().incluir(historicoNotaParcialVO, usuarioVO);
					}
				}
			}
			
		}
	}

	private void executarGeracaoAbonoFaltaExclusaoFrequenciaAula(MatriculaPeriodoVO matriculaPeriodoVO, TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, Integer qtdeHorasRegistrarAbono, Boolean realizarAbonoRegistroAula, UsuarioVO usuarioVO) throws Exception {
		if (realizarAbonoRegistroAula && qtdeHorasRegistrarAbono > 0) {
			getFacadeFactory().getAbonoFaltaFacade().executarGeracaoAbonoRegistroAulaTransferenciaTurma(matriculaPeriodoVO, turmaOrigem, turmaDestino, disciplinaVO, qtdeHorasRegistrarAbono, false, usuarioVO);
		}
		getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulaPorMatriculaMatriculaPeriodoTurmaDisciplina(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getCodigo(), turmaOrigem.getCodigo(), disciplinaVO.getCodigo(), false, usuarioVO);
	}

	@Override
	public void removerTransferenciaTurmaVOs(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TransferenciaTurmaVO objRemover) throws Exception {
		transferenciaTurmaVOs.removeIf(ttvo -> ttvo.getDisciplinaVO().getCodigo().equals(objRemover.getDisciplinaVO().getCodigo()));
	}

	private TransferenciaTurmaVO executarGeracaoTransferenciaTurmaVO(TransferenciaTurmaVO transferenciaTurmaVO, MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuarioVO, Boolean permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais) throws Exception {
		TransferenciaTurmaVO ttVO = new TransferenciaTurmaVO();
		ttVO.setMatriculaVO(transferenciaTurmaVO.getMatriculaVO());
		ttVO.setDisciplinaVO(obj.getDisciplina());
		ttVO.setTurmaOrigem(obj.getTurma());
		ttVO.setUltimaMatriculaPeriodoAtiva(transferenciaTurmaVO.getUltimaMatriculaPeriodoAtiva());
		ttVO.setMatriculaPeriodoTurmaDisciplinaVO(obj);
		List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsinoSituacao(ttVO.getDisciplinaVO().getCodigo(), obj.getCargaHorariaDisciplina(), ttVO.getUltimaMatriculaPeriodoAtiva().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), "AB", false, 0, false, "", "", true, false, false, false, false, usuarioVO, permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais);
		ttVO.getTurmaDestino().setCodigo(executarMontagemTurmaDestino(ttVO.getTurmaOrigem(), transferenciaTurmaVO.getTurmaDestino(), ttVO.getListaSelectItemTurma(), turmaVOs));
		executarMontagemTurmaTeoricaPraticaDestino(ttVO, transferenciaTurmaVO, obj, usuarioVO);
		return ttVO;
	}

	@Override
	public void executarMontagemTurmaTeoricaPraticaDestino(TransferenciaTurmaVO ttVO, TransferenciaTurmaVO transferenciaTurmaVO, MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getTurmaTeorica())) {
			ttVO.setTurmaTeoricaOrigem(obj.getTurmaTeorica());
			getFacadeFactory().getTurmaFacade().carregarDados(ttVO.getTurmaTeoricaOrigem(), NivelMontarDados.BASICO, usuarioVO);
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultarSubturmasRealizarTransferencia(ttVO.getTurmaDestino().getCodigo(), ttVO.getDisciplinaVO().getCodigo(), obj.getDisciplinaFazParteComposicao(), TipoSubTurmaEnum.TEORICA, obj.getAno(), obj.getSemestre(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			executarMontagemTurmaDestino(ttVO.getTurmaTeoricaOrigem(), transferenciaTurmaVO.getTurmaDestino(), ttVO.getListaSelectItemTurmaTeorica(), turmaVOs);
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaPratica())) {
			ttVO.setTurmaPraticaOrigem(obj.getTurmaPratica());
			getFacadeFactory().getTurmaFacade().carregarDados(ttVO.getTurmaPraticaOrigem(), NivelMontarDados.BASICO, usuarioVO);
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultarSubturmasRealizarTransferencia(ttVO.getTurmaDestino().getCodigo(), ttVO.getDisciplinaVO().getCodigo(), obj.getDisciplinaFazParteComposicao(), TipoSubTurmaEnum.PRATICA, obj.getAno(), obj.getSemestre(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			executarMontagemTurmaDestino(ttVO.getTurmaPraticaOrigem(), transferenciaTurmaVO.getTurmaDestino(), ttVO.getListaSelectItemTurmaPratica(), turmaVOs);
		}
	}

	private Integer executarMontagemTurmaDestino(TurmaVO turmaOrigem, TurmaVO turmaDestino, List<SelectItem> listaSelectItemTurma, List<TurmaVO> turmaVOs) throws Exception {
		Integer turmaDestinoFinal = turmaOrigem.getCodigo();
		if (turmaVOs.contains(turmaDestino)) {
			turmaDestinoFinal = turmaDestino.getCodigo();
		}
		listaSelectItemTurma.clear();
		if (Uteis.isAtributoPreenchido(turmaVOs)) {
			for (TurmaVO turmaVO : turmaVOs) {
				listaSelectItemTurma.add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
			}
		} else {
			listaSelectItemTurma.add(new SelectItem(turmaOrigem.getCodigo(), turmaOrigem.getIdentificadorTurma()));
		}
		return turmaDestinoFinal;
	}
	
	private StringBuilder getSqlPadraoConsultaQuantidadeRegistros() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select count(TransferenciaTurma.codigo) as quantidade from TransferenciaTurma ");
		sqlStr.append(" inner join Matricula on Matricula.matricula = TransferenciaTurma.matricula ");
		sqlStr.append(" inner join Pessoa on Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" inner join Disciplina on Disciplina.codigo = TransferenciaTurma.disciplina ");
		sqlStr.append(" left join Turma as TurmaOrigem on TurmaOrigem.codigo = TransferenciaTurma.turmaOrigem ");
		sqlStr.append(" left join Turma as TurmaDestino on TurmaDestino.codigo = TransferenciaTurma.turmaDestino ");
		return sqlStr;
	}

	public Integer consultarQuantidadeRegistros(String valorConsulta, String campoConsulta) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsultaQuantidadeRegistros();
		StringBuilder valorConsultaValidado = new StringBuilder(valorConsulta);
		if (campoConsulta.equals("matricula")) {
			sqlStr.append(" where TransferenciaTurma.matricula like ? ");
		} else if (campoConsulta.equals("disciplina")) {
			sqlStr.append(" where Disciplina.nome like ? ");
			valorConsultaValidado.append(PERCENT);
		} else if (campoConsulta.equals("turmaOrigem")) {
			sqlStr.append(" where TurmaOrigem.identificadorTurma like ? ");
			valorConsultaValidado.append(PERCENT);
		} else if (campoConsulta.equals("turmaDestino")) {
			sqlStr.append(" where TurmaDestino.identificadorTurma like ? ");
			valorConsultaValidado.append(PERCENT);
		} else if (campoConsulta.equals("aluno")) {
			sqlStr.append(" where Pessoa.nome ilike ? ");
			valorConsultaValidado = new StringBuilder(PERCENT);
			valorConsultaValidado.append(valorConsulta).append(PERCENT);
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsultaValidado.toString());
		if (resultado.next()) {
			Integer quantidade = new Integer(resultado.getInt("quantidade"));
			return quantidade;
		}
		return new Integer(0);
	}
}
