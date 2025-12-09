package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.model.SelectItem;

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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.TransferenciaTurnoDisciplinaVO;
import negocio.comuns.secretaria.TransferenciaTurnoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.TransferenciaTurnoDisciplinaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TransferenciaTurnoDisciplina extends ControleAcesso implements TransferenciaTurnoDisciplinaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TransferenciaTurnoDisciplina() throws Exception {
		super();
		setIdEntidade("TransferenciaTurnoDisciplina");
	}

	@Override
	public List<TransferenciaTurnoDisciplinaVO> consultarPorTransferenciaTurno(Integer transferenciaTurno, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("")
				.append(" SELECT transferenciaturnodisciplina.codigo, transferenciaturno, matriculaperiodoturmadisciplina, qtdehorasregistrarabono, turmaantiga, turmatransferida,")
				.append(" turmaPraticaAntiga, turmaPraticatTransferida, turmaTeoricaAntiga, turmaTeoricaTransferida, ")
				.append(" case when matriculaperiodoturmadisciplina.disciplina is not null then matriculaperiodoturmadisciplina.disciplina else transferenciaturnodisciplina.disciplina end as disciplina")
				.append(" FROM transferenciaturnodisciplina")
				.append(" left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = transferenciaturnodisciplina.matriculaperiodoturmadisciplina")
				.append(" where transferenciaturno = ").append(transferenciaTurno);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosBasicos(dadosSQL, nivelMontarDados, usuarioVO);
	}

	private List<TransferenciaTurnoDisciplinaVO> montarDadosBasicos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<TransferenciaTurnoDisciplinaVO> transferenciaTurnoDisciplinaVOs = new ArrayList<TransferenciaTurnoDisciplinaVO>(0);
		while (dadosSQL.next()) {
			transferenciaTurnoDisciplinaVOs.add(montarDados(dadosSQL, nivelMontarDados, usuarioVO));
		}
		return transferenciaTurnoDisciplinaVOs;
	}

	private TransferenciaTurnoDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		TransferenciaTurnoDisciplinaVO obj = new TransferenciaTurnoDisciplinaVO();
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getTurmaAntiga().setCodigo(dadosSQL.getInt("turmaAntiga"));
		obj.getTurmaTransferida().setCodigo(dadosSQL.getInt("turmaTransferida"));
		obj.getTurmaPraticaAntiga().setCodigo(dadosSQL.getInt("turmaPraticaAntiga"));
		obj.getTurmaPraticaTransferida().setCodigo(dadosSQL.getInt("turmaPraticatTransferida"));
		obj.getTurmaTeoricaAntiga().setCodigo(dadosSQL.getInt("turmaTeoricaAntiga"));
		obj.getTurmaTeoricaTransferida().setCodigo(dadosSQL.getInt("turmaTeoricaTransferida"));
		obj.setQtdeHorasRegistrarAbono(dadosSQL.getInt("qtdeHorasRegistrarAbono"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(dadosSQL.getInt("disciplina"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		if (Uteis.isAtributoPreenchido(obj.getTurmaPraticaAntiga())) {
			obj.setTurmaPraticaAntiga(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaPraticaAntiga().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaPraticaTransferida())) {
			obj.setTurmaPraticaTransferida(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaPraticaTransferida().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaTeoricaAntiga())) {
			obj.setTurmaTeoricaAntiga(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaTeoricaAntiga().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaTeoricaTransferida())) {
			obj.setTurmaTeoricaTransferida(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaTeoricaTransferida().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaAntiga())) {
			obj.setTurmaAntiga(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaAntiga().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaTransferida())) {
			obj.setTurmaTransferida(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaTransferida().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final TransferenciaTurnoDisciplinaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		/**
		 * @author Leonardo Riciolle Comentado 30/10/2014 Classe Subordinada
		 */
		final StringBuilder sqlStr = new StringBuilder("INSERT INTO TransferenciaTurnoDisciplina (transferenciaturno, matriculaperiodoturmadisciplina, qtdeHorasRegistrarAbono, turmaAntiga, turmaTransferida, disciplina, ")
				.append(" turmaPraticaAntiga, turmaPraticatTransferida, turmaTeoricaAntiga, turmaTeoricaTransferida )")
				.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo");
		obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());
				sqlInserir.setInt(1, obj.getTransferenciaTurno());
				sqlInserir.setInt(2, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
				sqlInserir.setInt(3, obj.getQtdeHorasRegistrarAbono() + obj.getQtdeHorasAbonoTurmaPratica() + obj.getQtdeHorasAbonoTurmaTeorica());
				sqlInserir.setInt(4, obj.getTurmaAntiga().getCodigo());
				sqlInserir.setInt(5, obj.getTurmaTransferida().getCodigo());
				sqlInserir.setInt(6, obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
				sqlInserir.setInt(7, obj.getTurmaPraticaAntiga().getCodigo());
				sqlInserir.setInt(8, obj.getTurmaPraticaTransferida().getCodigo());
				sqlInserir.setInt(9, obj.getTurmaTeoricaAntiga().getCodigo());
				sqlInserir.setInt(10, obj.getTurmaTeoricaTransferida().getCodigo());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return 0;
			}
		}));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTransferenciaTurnoDisciplinaVOs(List<TransferenciaTurnoDisciplinaVO> transferenciaTurnoDisciplinaVOs, Integer transferenciaTurno, UsuarioVO usuarioVO) throws Exception {
		for (TransferenciaTurnoDisciplinaVO obj : transferenciaTurnoDisciplinaVOs) {
			obj.setTransferenciaTurno(transferenciaTurno);
			persistir(obj, false, usuarioVO);
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TransferenciaTurnoDisciplina.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TransferenciaTurnoDisciplina.idEntidade = idEntidade;
	}

	@Override
	public void montarDadosTransferenciaTurnoDisciplina(TransferenciaTurnoVO transferenciaTurnoVO, UsuarioVO usuario) throws Exception {
		transferenciaTurnoVO.getMatriculaPeriodoDestino().setFinanceiroManual(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getFinanceiroManual());
		getFacadeFactory().getTransferenciaTurnoFacade().validarDados(transferenciaTurnoVO.getMatriculaPeriodoDestino(), usuario);
		transferenciaTurnoVO.setTransferenciaTurnoDisciplinaVOs(null);
		transferenciaTurnoVO.getMatriculaPeriodoDestino().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(transferenciaTurnoVO.getMatriculaPeriodoDestino().getTurma().getCodigo(), usuario));
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		if (transferenciaTurnoVO.getMatriculaPeriodoOrigem().getMatriculaVO().getGradeCurricularAtual().getCodigo().equals(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getGradeCurricular().getCodigo())) {
			matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaDisciplinaCompostaPorMatriculaPeriodo(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getCodigo(), false, usuario);
		} else{
			matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaPeriodoGradeCurricularAtual(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
			if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO())){
				matriculaPeriodoTurmaDisciplinaVO.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimariaSemExcecao(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
			} else if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO())){
				matriculaPeriodoTurmaDisciplinaVO.setGradeCurricularGrupoOptativaDisciplinaVO(getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuario));
			}
			transferenciaTurnoVO.getTransferenciaTurnoDisciplinaVOs().add(executarGeracaoTransferenciaTurnoDisciplinaVO(transferenciaTurnoVO, matriculaPeriodoTurmaDisciplinaVO, usuario));
			if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO()) && matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getDisciplinaComposta()) {
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComposicaoVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeDisciplina(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getCodigo(), false, usuario);
				for (MatriculaPeriodoTurmaDisciplinaVO mptdvo : matriculaPeriodoTurmaDisciplinaComposicaoVOs) {
					mptdvo.setDisciplinaFazParteComposicao(true);
					if(Uteis.isAtributoPreenchido(mptdvo.getGradeDisciplinaCompostaVO())){
						mptdvo.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(mptdvo.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					}
					transferenciaTurnoVO.getTransferenciaTurnoDisciplinaVOs().add(executarGeracaoTransferenciaTurnoDisciplinaVO(transferenciaTurnoVO, mptdvo, usuario));
				}
			} else if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO()) && matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaComposta()) {
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComposicaoOptativaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeCurricularGrupoOptativaDisciplina(transferenciaTurnoVO.getMatriculaPeriodoOrigem().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, usuario);
				for (MatriculaPeriodoTurmaDisciplinaVO mptdvo : matriculaPeriodoTurmaDisciplinaComposicaoOptativaVOs) {
					mptdvo.setDisciplinaFazParteComposicao(true);
					if(Uteis.isAtributoPreenchido(mptdvo.getGradeDisciplinaCompostaVO())){
						mptdvo.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(mptdvo.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					}
					transferenciaTurnoVO.getTransferenciaTurnoDisciplinaVOs().add(executarGeracaoTransferenciaTurnoDisciplinaVO(transferenciaTurnoVO, mptdvo, usuario));
				}
			}
		}
	}
	
	private TransferenciaTurnoDisciplinaVO executarGeracaoTransferenciaTurnoDisciplinaVO(TransferenciaTurnoVO transferenciaTurnoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		TransferenciaTurnoDisciplinaVO	transferenciaTurnoDisciplinaVO = new TransferenciaTurnoDisciplinaVO();
		transferenciaTurnoDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);			
		transferenciaTurnoDisciplinaVO.setTurmaAntiga(matriculaPeriodoTurmaDisciplinaVO.getTurma().clone());
		transferenciaTurnoDisciplinaVO.setTurmaPraticaAntiga(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getClone());
		transferenciaTurnoDisciplinaVO.setTurmaTeoricaAntiga(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getClone());
		Integer cargaHoraria = matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
		if (!Uteis.isAtributoPreenchido(cargaHoraria)) {
			cargaHoraria = getFacadeFactory().getHistoricoFacade().consultarCargaHorariaHistPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		}
		List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsinoSituacao(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(),
						cargaHoraria, transferenciaTurnoVO.getMatriculaPeriodoDestino().getUnidadeEnsinoCursoVO().getUnidadeEnsino(),
						"AB", false, 0, false, "", "", true, false, false, false, false, usuarioVO, false);
		turmaVOs.stream()
			.filter(t -> t.getCodigo().equals(transferenciaTurnoVO.getMatriculaPeriodoDestino().getTurma().getCodigo()))
			.findFirst()
			.ifPresent(t -> transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().setTurma(t));
		executarGeracaoListaSelectItemTurmaBaseTeoricaPraticaTransferenciaTurnoDisciplinaVO(transferenciaTurnoDisciplinaVO, turmaVOs, usuarioVO);
		return transferenciaTurnoDisciplinaVO;
	}
	
	private void executarGeracaoListaSelectItemTurmaBaseTeoricaPraticaTransferenciaTurnoDisciplinaVO(TransferenciaTurnoDisciplinaVO transferenciaTurnoDisciplinaVO, List<TurmaVO> turmaVOs, UsuarioVO usuarioVO) throws Exception {
		transferenciaTurnoDisciplinaVO.setListaSelectItemTurma(montarListaSelectItemTurma(turmaVOs));
		executarGeracaoListaSelectItemTurmaTeoricaPraticaTransferenciaTurnoDisciplinaVO(transferenciaTurnoDisciplinaVO, usuarioVO);
	}
	
	public void executarGeracaoListaSelectItemTurmaTeoricaPraticaTransferenciaTurnoDisciplinaVO(TransferenciaTurnoDisciplinaVO transferenciaTurnoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma())) {
			transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(transferenciaTurnoDisciplinaVO.getTurmaTeoricaAntiga())) {
			transferenciaTurnoDisciplinaVO.getListaSelectItemTurmaTeorica().clear();
			Stream<TurmaVO> turmaTeoricaVOs = getFacadeFactory().getTurmaFacade().consultarSubturmasRealizarTransferencia(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), 
					transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplinaFazParteComposicao(), TipoSubTurmaEnum.TEORICA, 
					transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getAno(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO).stream();
			transferenciaTurnoDisciplinaVO.getListaSelectItemTurmaTeorica().add(new SelectItem(transferenciaTurnoDisciplinaVO.getTurmaTeoricaAntiga().getCodigo(), transferenciaTurnoDisciplinaVO.getTurmaTeoricaAntiga().getIdentificadorTurma()));
			transferenciaTurnoDisciplinaVO.getListaSelectItemTurmaTeorica().addAll(montarListaSelectItemTurma(turmaTeoricaVOs.filter(t -> !t.getCodigo().equals(transferenciaTurnoDisciplinaVO.getTurmaTeoricaAntiga().getCodigo())).collect(Collectors.toList())));
		}
		if (Uteis.isAtributoPreenchido(transferenciaTurnoDisciplinaVO.getTurmaPraticaAntiga())) {
			transferenciaTurnoDisciplinaVO.getListaSelectItemTurmaPratica().clear();
			Stream<TurmaVO> turmaPraticaVOs = getFacadeFactory().getTurmaFacade().consultarSubturmasRealizarTransferencia(transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), 
					transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplinaFazParteComposicao(), TipoSubTurmaEnum.PRATICA, 
					transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getAno(), transferenciaTurnoDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO).stream();
			transferenciaTurnoDisciplinaVO.getListaSelectItemTurmaPratica().add(new SelectItem(transferenciaTurnoDisciplinaVO.getTurmaPraticaAntiga().getCodigo(), transferenciaTurnoDisciplinaVO.getTurmaPraticaAntiga().getIdentificadorTurma()));
			transferenciaTurnoDisciplinaVO.getListaSelectItemTurmaPratica().addAll(montarListaSelectItemTurma(turmaPraticaVOs.filter(t -> !t.getCodigo().equals(transferenciaTurnoDisciplinaVO.getTurmaPraticaAntiga().getCodigo())).collect(Collectors.toList())));
		}
	}
	
	private List<SelectItem> montarListaSelectItemTurma(List<TurmaVO> turmaVOs) throws Exception {
		return turmaVOs.stream().map(t -> new SelectItem(t.getCodigo(), t.getIdentificadorTurma())).collect(Collectors.toList());
	}
}
