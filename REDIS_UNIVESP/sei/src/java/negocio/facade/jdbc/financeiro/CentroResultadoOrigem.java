package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaRateioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CentroResultadoOrigemInterfaceFacade;

/**
 *
 * @author Pedro Otimize
 */
@Repository
@Scope("singleton")
@Lazy
public class CentroResultadoOrigem extends ControleAcesso implements CentroResultadoOrigemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8862004344613845755L;
	protected static String idEntidade = "CentroResultadoOrigem";

	private void validarDados(CentroResultadoOrigemVO obj, Boolean permitirGravarContaPagarIsenta) {
		if (Uteis.isAtributoPreenchido(obj.getTipoCentroResultadoOrigemEnum()) && obj.getTipoCentroResultadoOrigemEnum().isContaReceber()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroReceitaVO()), "O campo Centro Receita (CentroResultadoOrigem) deve ser informado.");
		} else {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO()), "O campo Categoria Despesa (CentroResultadoOrigem) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNivelCentroResultadoEnum()), "O campo Centro Resultado Para (CentroResultadoOrigem) deve ser informado.");
			Uteis.checkState(obj.getCategoriaDespesaVO().getExigeCentroCustoRequisitante() && !Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO()), "O campo Centro de Custo (CentroResultadoOrigem) deve ser informado.");
		}

		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isUnidadeEnsino() && !Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isDepartamento() && !Uteis.isAtributoPreenchido(obj.getDepartamentoVO()), "O campo Departamento (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCurso() && !Uteis.isAtributoPreenchido(obj.getCursoVO()), "O campo Curso (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCursoTurno() && !Uteis.isAtributoPreenchido(obj.getTurnoVO()), "O campo Turno (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isTurma() && !Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoAdministrativo()), "O campo Centro Resultado (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getQuantidade()), "O campo Quantidade (CentroResultadoOrigem) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPorcentagem()), "O campo Porcentagem (CentroResultadoOrigem) deve ser informado.");
		if (!permitirGravarContaPagarIsenta) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValor()) && Uteis.isAtributoPreenchido(obj.getTipoCentroResultadoOrigemEnum()) && !obj.getTipoCentroResultadoOrigemEnum().isContaReceber(), "O campo Valor (CentroResultadoOrigem) deve ser informado.");
		}
		Uteis.checkState(obj.getPorcentagem() > 100.0000, "O campo Porcentagem (CentroResultadoOrigem) não pode ser maior que 100%.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<CentroResultadoOrigemVO> lista, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, boolean verificarAcesso, UsuarioVO usuarioVO, Boolean permitirGravarContaPagarIsenta) throws Exception {
		excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(lista, codOrigem, tipoCentroResultadoOrigemEnum, usuarioVO);
		for (CentroResultadoOrigemVO obj : lista) {
			persistirDados(codOrigem, tipoCentroResultadoOrigemEnum, verificarAcesso, usuarioVO, obj, permitirGravarContaPagarIsenta);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CentroResultadoOrigemVO obj, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, boolean isValidarCentroResultadoExistente, boolean verificarAcesso, UsuarioVO usuarioVO, Boolean permitirGravarContaPagarIsenta) throws Exception {
		if (isValidarCentroResultadoExistente) {
			excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, codOrigem, tipoCentroResultadoOrigemEnum, usuarioVO);
		}
		persistirDados(codOrigem, tipoCentroResultadoOrigemEnum, verificarAcesso, usuarioVO, obj, permitirGravarContaPagarIsenta);

	}

	private void persistirDados(String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, boolean verificarAcesso, UsuarioVO usuarioVO, CentroResultadoOrigemVO obj, Boolean permitirGravarContaPagarIsenta) throws Exception{
		obj.setCodOrigem(codOrigem);
		obj.setTipoCentroResultadoOrigemEnum(tipoCentroResultadoOrigemEnum);
		validarDados(obj, permitirGravarContaPagarIsenta);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CentroResultadoOrigemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultadoOrigem.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO CentroResultadoOrigem (categoriadespesa, quantidade, valor, tipoCentroResultadoOrigem, codOrigem,  ");
			sql.append(" centroResultadoAdministrativo, tipoNivelCentroResultadoEnum, unidadeensino, departamento, funcionariocargo,  ");
			sql.append(" curso, turno, turma, porcentagem , tipoMovimentacaoCentroResultadoOrigemEnum, dataMovimentacao, centroReceita )");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoCentroResultadoOrigemEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodOrigem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDepartamentoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCursoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTurnoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTurmaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPorcentagem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoMovimentacaoCentroResultadoOrigemEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataMovimentacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCentroReceitaVO(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CentroResultadoOrigemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultadoOrigem.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE CentroResultadoOrigem ");
			sql.append(" SET categoriadespesa=?, quantidade=?, valor=?, tipoCentroResultadoOrigem=?, codOrigem=?,  ");
			sql.append(" centroResultadoAdministrativo=?, tipoNivelCentroResultadoEnum =?,  unidadeensino=?, departamento=?,  ");
			sql.append(" funcionariocargo=?,  curso=?, turno=?, turma=?, porcentagem=?, ");
			sql.append(" tipoMovimentacaoCentroResultadoOrigemEnum=?, dataMovimentacao=?, centroReceita=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoCentroResultadoOrigemEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodOrigem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDepartamentoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCursoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTurnoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTurmaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPorcentagem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoMovimentacaoCentroResultadoOrigemEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataMovimentacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCentroReceitaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCentroResultadoOrigemVOCampoValor(final CentroResultadoOrigemVO obj,  UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE CentroResultadoOrigem set valor=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getValor());
				sqlAlterar.setInt(2, obj.getCodigo());
				return sqlAlterar;
			}
		});

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCentroResultadoOrigemVOCampoCentroReceita(final CentroResultadoOrigemVO obj,  UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE CentroResultadoOrigem set centroReceita=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getCentroReceitaVO().getCodigo());
				sqlAlterar.setInt(2, obj.getCodigo());
				return sqlAlterar;
			}
		});
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(List<CentroResultadoOrigemVO> lista, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(lista)) {
			StringBuilder sb = new StringBuilder("DELETE FROM CentroResultadoOrigem where codorigem ='").append(codOrigem).append("' ");
			sb.append(" and tipoCentroResultadoOrigem = '").append(tipoCentroResultadoOrigemEnum.name()).append("' ");
			sb.append(" and codigo not in ( 0  ");
			for (Object obj : lista) {
				Integer codigo = (Integer) UtilReflexao.invocarMetodoGet(obj, "codigo");
				if (Uteis.isAtributoPreenchido(codigo)) {
					sb.append(", ").append(codigo);
				}
			}
			sb.append(") ");
			sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().execute(sb.toString());
		} else {
			StringBuilder sb = new StringBuilder("DELETE FROM CentroResultadoOrigem where codorigem ='").append(codOrigem).append("' ");
			sb.append(" and tipoCentroResultadoOrigem = '").append(tipoCentroResultadoOrigemEnum.name()).append("' ");
			sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().execute(sb.toString());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void geracaoCentroResultadoOrigemPadraoPorContaReceber(final ContaReceberVO obj, boolean isAtualizarCentroReceita, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(obj.getListaCentroResultadoOrigem())
				&& obj.getListaCentroResultadoOrigem().stream().noneMatch(p-> p.getCentroReceitaVO().getCodigo().equals(obj.getCentroReceita().getCodigo()))
				&& obj.getListaCentroResultadoOrigem().size() == 1){
			for (CentroResultadoOrigemVO cro : obj.getListaCentroResultadoOrigem()) {
				cro.setCentroReceitaVO(obj.getCentroReceita());
				if(isAtualizarCentroReceita) {
					atualizarCentroResultadoOrigemVOCampoCentroReceita(cro, usuario);	
				}
			}
		}else if(!Uteis.isAtributoPreenchido(obj.getListaCentroResultadoOrigem())) {
			validarCodOrigemContaReceberManualOrigemBiblioteca(obj);
			preencherCentroResultadoOrigem(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void preencherCentroResultadoOrigem(final ContaReceberVO obj, UsuarioVO usuario) throws Exception {
		CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
		cro.setUnidadeEnsinoVO(obj.isContaReceberReferenteMaterialDidatico() ? obj.getUnidadeEnsinoFinanceira() : obj.getUnidadeEnsino());
		if (obj.getTipoOrigemContaReceber().isMatricula()
				|| obj.getTipoOrigemContaReceber().isMensalidade()
				|| obj.getTipoOrigemContaReceber().isMaterialDidatico()
				|| obj.getTipoOrigemContaReceber().isBolsaCusteadaConvenio()
				|| obj.getTipoOrigemContaReceber().isInclusaoReposicao()
				|| (obj.getTipoOrigemContaReceber().isContratoReceita() && (obj.getTipoAluno() || obj.getTipoResponsavelFinanceiro()))
				|| (obj.getTipoOrigemContaReceber().isOutros() && Uteis.isAtributoPreenchido(obj.getTurma()))
				|| (obj.getTipoOrigemContaReceber().isDevolucaoCheque() && Uteis.isAtributoPreenchido(obj.getTurma()))
				|| (obj.getTipoOrigemContaReceber().isRequerimento() && Uteis.isAtributoPreenchido(obj.getTurma()) 
					&& getFacadeFactory().getTipoRequerimentoFacade().validarSeTipoRequerimentoUsaCentroResultadoTurma(Integer.parseInt(obj.getCodOrigem()), usuario))) {
			
			
			cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorTurma(obj.getTurma().getCodigo(), usuario));
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.TURMA);
			cro.setTurmaVO(obj.getTurma());

		} else if (obj.getTipoOrigemContaReceber().isRequerimento()) {
			cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPadraoRequerimentoPorUnidadeEnsino(cro.getUnidadeEnsinoVO().getCodigo(), Integer.parseInt(obj.getCodOrigem()), usuario));
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);

		} else if (obj.getTipoOrigemContaReceber().isNegociacao()) {
			cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(cro.getUnidadeEnsinoVO().getCodigo(), usuario));
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		} else if (obj.getTipoOrigemContaReceber().isBiblioteca()) {
			cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPadraoBibliotecaPorEmprestimo(Integer.parseInt(obj.getCodOrigem()), usuario));
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		} else if (obj.getTipoOrigemContaReceber().isIncricaoProcessoSeletivo()) {
			CursoVO curso = getFacadeFactory().getContaReceberFacade().obterCursoVerificandoTurmaOrMatricula(obj, usuario);
			cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsinoCurso(cro.getUnidadeEnsinoVO().getCodigo(), curso.getCodigo(), usuario));
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.CURSO);
			if(!Uteis.isAtributoPreenchido(cro.getCentroResultadoAdministrativo())){
				cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(cro.getUnidadeEnsinoVO().getCodigo(), usuario));
				cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);	
			}
		} else if (obj.getTipoOrigemContaReceber().isContratoReceita() && obj.getTipoFuncionario()) {
			if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
				cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorDepartamento(obj.getDepartamentoVO().getCodigo(), usuario));
				cro.setDepartamentoVO(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.DEPARTAMENTO);
		} else if ((obj.getTipoOrigemContaReceber().isOutros() && Uteis.isAtributoPreenchido(cro.getUnidadeEnsinoVO()))
				|| (obj.getTipoOrigemContaReceber().isDevolucaoCheque() && Uteis.isAtributoPreenchido(cro.getUnidadeEnsinoVO()))
				|| (obj.getTipoOrigemContaReceber().isContratoReceita() && (obj.getTipoFornecedor() || obj.getTipoParceiro()))) {
			cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(cro.getUnidadeEnsinoVO().getCodigo(), usuario));
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		}

		cro.setCentroReceitaVO(obj.getCentroReceita());
		cro.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
		cro.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_RECEBER);
		cro.setQuantidade(1.0);
		cro.setPorcentagem(100.0);
		cro.setValor(obj.getSituacaoEQuitada() ? obj.getValorRecebido() : obj.getValorReceberCalculado());
		obj.getListaCentroResultadoOrigem().add(cro);
	}

	@Override
	public void preencherDadosPorCategoriaDespesa(CentroResultadoOrigemVO obj, UsuarioVO usuario) throws Exception {
		try {
			CentroResultadoVO crAdministrativo = null;
			CursoVO cursoFiltro = null;
			TurmaVO turmaFiltro = null;
			if (Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())) {
				obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
				obj.setUnidadeEnsinoVO(obj.getFuncionarioCargoVO().getUnidade());
				if(Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO().getDepartamento())) {
					obj.setDepartamentoVO(obj.getFuncionarioCargoVO().getDepartamento());
				} else {
					obj.setDepartamentoVO(obj.getFuncionarioCargoVO().getCargo().getDepartamento());
				}				
			}
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNivelCentroResultadoEnum()), "Não foi localizado o Tipo Nível Centro Resultado para geração do Centro Resultado Origem da Categoria Despesa "+obj.getCategoriaDespesaVO().getDescricao());

			if (obj.getTipoNivelCentroResultadoEnum().isUnidadeEnsino() && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(obj.getUnidadeEnsinoVO().getCodigo(), usuario);
			}
			if (obj.getTipoNivelCentroResultadoEnum().isDepartamento() && Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorDepartamento(obj.getDepartamentoVO().getCodigo(), usuario);
			}

			if ((obj.getTipoNivelCentroResultadoEnum().isCurso() || obj.getTipoNivelCentroResultadoEnum().isCursoTurno()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()) && Uteis.isAtributoPreenchido(obj.getCursoVO())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), usuario);
				cursoFiltro = obj.getCursoVO();
				turmaFiltro = null;
			}
			if (obj.getTipoNivelCentroResultadoEnum().isTurma() && Uteis.isAtributoPreenchido(obj.getTurmaVO())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorTurma(obj.getTurmaVO().getCodigo(), usuario);
				turmaFiltro = obj.getTurmaVO();
				cursoFiltro = null;
			}
			if (Uteis.isAtributoPreenchido(crAdministrativo) && getFacadeFactory().getCentroResultadoFacade().validarRestricaoUsoCentroResultado(crAdministrativo, obj.getDepartamentoVO(), cursoFiltro, turmaFiltro, usuario)) {
				obj.setCentroResultadoAdministrativo(crAdministrativo);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public void validarDadosCentroResultadoOrigem(CentroResultadoOrigemVO obj, Double valoTotalCalculoPercentual) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(valoTotalCalculoPercentual), "O valor Total do Lançamento deve ser informado.");
		if (Uteis.isAtributoPreenchido(obj.getValor())) {
			obj.calcularPorcentagem(valoTotalCalculoPercentual);
		} else if (Uteis.isAtributoPreenchido(obj.getPorcentagem())) {
			obj.calcularValor(valoTotalCalculoPercentual);
		}
		validarDados(obj, false);
	}

	@Override
	public void validarDadosTotalizadoresAposAlteracao(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, Double valoTotalCalculoPercentual, boolean validarComparadoComValorTotal, UsuarioVO usuario) {
		Double totalValor = Uteis.arrendondarForcando2CadasDecimais(listaCentroResultadoOrigem.stream().map(p -> p.getValor()).reduce(0D, (a, b) -> a + b));
		Double totalPorcentagem = Uteis.arrendondarForcandoCadasDecimais(listaCentroResultadoOrigem.stream().mapToDouble(p -> p.getPorcentagem()).reduce(0D, (a, b) -> a + b), 8);
		if (Uteis.isAtributoPreenchido(totalPorcentagem) && !totalPorcentagem.equals(100.00000000) && totalValor.equals(valoTotalCalculoPercentual)) {
			if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalPorcentagem < 100.00000000) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setPorcentagem(ultimoCentroResultado.getPorcentagem()+(100.00 - totalPorcentagem));
			} else if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalPorcentagem > 100.00000000) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setPorcentagem(ultimoCentroResultado.getPorcentagem()-(totalPorcentagem - 100.00));
			}	
			realizarDistribuicaoValoresCentroResultado(listaCentroResultadoOrigem, valoTotalCalculoPercentual, usuario);
		}else if(Uteis.isAtributoPreenchido(totalPorcentagem) && totalPorcentagem.equals(100.00000000) && !totalValor.equals(valoTotalCalculoPercentual)) {
			realizarDistribuicaoValoresCentroResultado(listaCentroResultadoOrigem, valoTotalCalculoPercentual, usuario);
		}else if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalPorcentagem > 100.00000000) {
			throw new StreamSeiException("O total da PORCENTAGEM para o CENTRO RESULTADO não pode ser maior que 100%.");
		}
		if (validarComparadoComValorTotal && Uteis.isAtributoPreenchido(totalPorcentagem) && totalPorcentagem.equals(100.00000000)) {
			totalValor = listaCentroResultadoOrigem.stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
			Uteis.checkState(!totalValor.equals(valoTotalCalculoPercentual), "O VALOR TOTAL (" + Uteis.getDoubleFormatado(totalValor) + ") para o CENTRO RESULTADO não pode ser diferente do VALOR DO LANÇAMENTO (" + Uteis.getDoubleFormatado(valoTotalCalculoPercentual) + ").");	
		}
		
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual, boolean validarDados, UsuarioVO usuario) {
		adicionarCentroResultadoOrigem(listaCentroResultadoOrigem, centroResultadoOrigem, valoTotalCalculoPercentual, true, validarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual, boolean validarTotal, boolean validarDados, UsuarioVO usuario) {
		try {
			int index = 0;
			centroResultadoOrigem.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(centroResultadoOrigem.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			if (validarDados) {
				validarDadosCentroResultadoOrigem(centroResultadoOrigem, valoTotalCalculoPercentual);
			}			
			for (CentroResultadoOrigemVO objExistente : listaCentroResultadoOrigem) {
				if (objExistente.equalsCentroResultadoOrigem(centroResultadoOrigem)) {
					listaCentroResultadoOrigem.set(index, centroResultadoOrigem);
					if(validarTotal) {
					validarDadosTotalizadoresAposAlteracao(listaCentroResultadoOrigem, valoTotalCalculoPercentual, validarDados, usuario);
					}
					return;
				}
				index++;
			}
			listaCentroResultadoOrigem.add(centroResultadoOrigem);
			if(validarTotal) {
			validarDadosTotalizadoresAposAlteracao(listaCentroResultadoOrigem, valoTotalCalculoPercentual, validarDados, usuario);
			}
		} catch (Exception e) {
			removerCentroResultadoOrigem(listaCentroResultadoOrigem, centroResultadoOrigem, usuario);
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, UsuarioVO usuario) {
		Iterator<CentroResultadoOrigemVO> i = listaCentroResultadoOrigem.iterator();
		while (i.hasNext()) {
			CentroResultadoOrigemVO objExistente = i.next();
			if (objExistente.equalsCentroResultadoOrigem(centroResultadoOrigem)) {
				i.remove();
				return;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarValidacaoPorcentagem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, Double valoTotalCalculoPercentual, UsuarioVO usuario) {
		Double totalPorcentagem = listaCentroResultadoOrigem.stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
		Double totalValor = listaCentroResultadoOrigem.stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
		if (!totalPorcentagem.equals(100.0000) && totalValor.equals(valoTotalCalculoPercentual)) {
			if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalPorcentagem < 100.0000) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setPorcentagem(ultimoCentroResultado.getPorcentagem()+(100.00 - totalPorcentagem));				
			} else if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalPorcentagem > 100.0000) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setPorcentagem(ultimoCentroResultado.getPorcentagem()-(totalPorcentagem - 100.00));
			}
			realizarDistribuicaoValoresCentroResultado(listaCentroResultadoOrigem, valoTotalCalculoPercentual, usuario);
		}else if(totalPorcentagem.equals(100.0000) && !totalValor.equals(valoTotalCalculoPercentual)) {			
			if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalValor < valoTotalCalculoPercentual) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setValor(Uteis.arrendondarForcando2CadasDecimais(ultimoCentroResultado.getValor() + (Uteis.arrendondarForcando2CadasDecimais(valoTotalCalculoPercentual - totalValor))));
				ultimoCentroResultado.calcularPorcentagem(valoTotalCalculoPercentual);
			} else if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalValor > valoTotalCalculoPercentual) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setValor(Uteis.arrendondarForcando2CadasDecimais(ultimoCentroResultado.getValor() - (Uteis.arrendondarForcando2CadasDecimais(totalValor - valoTotalCalculoPercentual))));
				ultimoCentroResultado.calcularPorcentagem(valoTotalCalculoPercentual);
		}
	}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarDistribuicaoValoresCentroResultado(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, Double valoTotalCalculoPercentual, UsuarioVO usuario) {
		Double totalPorcentagem = listaCentroResultadoOrigem.stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
		if (totalPorcentagem.equals(100.0000)) {
			listaCentroResultadoOrigem.forEach(p -> p.calcularValor(valoTotalCalculoPercentual));
			Double totalValor = listaCentroResultadoOrigem.stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
			if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalValor < valoTotalCalculoPercentual) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setValor(Uteis.arrendondarForcando2CadasDecimais(ultimoCentroResultado.getValor() + (Uteis.arrendondarForcandoCadasDecimais(valoTotalCalculoPercentual - totalValor, 8))));
				ultimoCentroResultado.calcularPorcentagem(valoTotalCalculoPercentual);
				totalPorcentagem = listaCentroResultadoOrigem.stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
				if(totalPorcentagem<(100.0000)) {
					ultimoCentroResultado.setPorcentagem(Uteis.arrendondarForcandoCadasDecimais(ultimoCentroResultado.getPorcentagem()+(100.0000-totalPorcentagem), 8));
				}else if(totalPorcentagem>(100.0000)) {
					ultimoCentroResultado.setPorcentagem(Uteis.arrendondarForcandoCadasDecimais(ultimoCentroResultado.getPorcentagem()-(totalPorcentagem-100.0000), 8));
				}
			} else if (Uteis.isAtributoPreenchido(listaCentroResultadoOrigem) && totalValor > valoTotalCalculoPercentual) {
				CentroResultadoOrigemVO ultimoCentroResultado = listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size() - 1);
				ultimoCentroResultado.setValor(Uteis.arrendondarForcando2CadasDecimais(ultimoCentroResultado.getValor() - (Uteis.arrendondarForcando2CadasDecimais(totalValor - valoTotalCalculoPercentual))));
				ultimoCentroResultado.calcularPorcentagem(valoTotalCalculoPercentual);				
				totalPorcentagem = listaCentroResultadoOrigem.stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
				if(totalPorcentagem<(100.0000)) {
					ultimoCentroResultado.setPorcentagem(Uteis.arrendondarForcandoCadasDecimais(ultimoCentroResultado.getPorcentagem()+(100.0000-totalPorcentagem), 8));
				}else if(totalPorcentagem>(100.0000)) {
					ultimoCentroResultado.setPorcentagem(Uteis.arrendondarForcandoCadasDecimais(ultimoCentroResultado.getPorcentagem()-(totalPorcentagem-100.0000),8));
			}
		}
		}else {
			Double totalValor = listaCentroResultadoOrigem.stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
			if(valoTotalCalculoPercentual.equals(totalValor)) {
				listaCentroResultadoOrigem.forEach(p -> p.calcularPorcentagem(valoTotalCalculoPercentual));				
	}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void atualizarListasDeCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaExistente, List<CentroResultadoOrigemVO> novaLista) {
		if (Uteis.isAtributoPreenchido(listaExistente)) {
			Iterator<CentroResultadoOrigemVO> i = listaExistente.iterator();
			while (i.hasNext()) {
				CentroResultadoOrigemVO objExistente = i.next();
				if (isRemoverCentroResultadoOrigemAgrupado(objExistente, novaLista)) {
					i.remove();
				}
			}
			novaLista.stream().forEach(p -> atualizarCentroResultadoOrigemAgrupado(p, listaExistente, false));
		} else {
			listaExistente.addAll(novaLista);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO cro, List<CentroResultadoOrigemVO> lista) {
		int index = 0;
		for (CentroResultadoOrigemVO objExistente : lista) {
			if (objExistente.equalsAgrupadoCentroResultadoOrigem(cro)) {
				objExistente.setQuantidade(objExistente.getQuantidade() + cro.getQuantidade());
				objExistente.setValor(objExistente.getValor() + cro.getValor());
				objExistente.setPorcentagem(Uteis.arrendondarForcandoCadasDecimais(objExistente.getPorcentagem() + cro.getPorcentagem(), 8));
				lista.set(index, objExistente);
				return;
			}
			index++;
		}
		lista.add(cro);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean isRemoverCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO objExistente, List<CentroResultadoOrigemVO> novaLista) {
		for (CentroResultadoOrigemVO novo : novaLista) {
			if (novo.equalsAgrupadoCentroResultadoOrigem(objExistente)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO objExistente, List<CentroResultadoOrigemVO> novaLista) {
		Iterator<CentroResultadoOrigemVO> i = novaLista.iterator();
		while (i.hasNext()) {
			CentroResultadoOrigemVO novo = i.next();
			if (novo.equalsAgrupadoCentroResultadoOrigem(objExistente)) {
				i.remove();
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void atualizarCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO novo, List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs, Boolean somarValores) {
		for (CentroResultadoOrigemVO objExistente : listaCentroResultadoOrigemVOs) {
			if (objExistente.equalsAgrupadoCentroResultadoOrigem(novo)) {
				objExistente.setQuantidade(somarValores ? (objExistente.getQuantidade() + novo.getQuantidade()) : novo.getQuantidade());
				objExistente.setValor(somarValores ? (objExistente.getValor() + novo.getValor()) : novo.getValor());
				objExistente.setPorcentagem(somarValores ? (objExistente.getPorcentagem() + novo.getPorcentagem()) : novo.getPorcentagem());
				return;
			}
		}
		listaCentroResultadoOrigemVOs.add(novo);
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CentroResultadoOrigem.codigo as \"CentroResultadoOrigem.codigo\",  ");
		sql.append(" CentroResultadoOrigem.quantidade as \"CentroResultadoOrigem.quantidade\", ");
		sql.append(" CentroResultadoOrigem.valor as \"CentroResultadoOrigem.valor\", ");
		sql.append(" CentroResultadoOrigem.porcentagem as \"CentroResultadoOrigem.porcentagem\", ");
		sql.append(" CentroResultadoOrigem.tipoCentroResultadoOrigem as \"CentroResultadoOrigem.tipoCentroResultadoOrigem\", ");
		sql.append(" CentroResultadoOrigem.codOrigem as \"CentroResultadoOrigem.codOrigem\", ");
		sql.append(" CentroResultadoOrigem.tipoNivelCentroResultadoEnum as \"CentroResultadoOrigem.tipoNivelCentroResultadoEnum\", ");
		sql.append(" CentroResultadoOrigem.tipoMovimentacaoCentroResultadoOrigemEnum as \"CentroResultadoOrigem.tipoMovimentacaoCentroResultadoOrigemEnum\", ");
		sql.append(" CentroResultadoOrigem.dataMovimentacao as \"CentroResultadoOrigem.dataMovimentacao\", ");

		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		sql.append(" categoriaDespesa.informarTurma as \"categoriaDespesa.informarTurma\", categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\",   ");
		sql.append(" categoriaDespesa.exigeCentroCustoRequisitante as \"categoriaDespesa.exigeCentroCustoRequisitante\", ");
		sql.append(" categoriaDespesa.identificadorcategoriadespesa as \"categoriaDespesa.identificadorcategoriadespesa\", ");
		sql.append(" categoriaDespesa.categoriadespesaprincipal as \"categoriaDespesa.categoriadespesaprincipal\", ");

		sql.append(" centroreceita.codigo as \"centroreceita.codigo\", centroreceita.descricao as \"centroreceita.descricao\",   ");
		sql.append(" centroreceita.identificadorCentroReceita as \"centroreceita.identificadorCentroReceita\",  ");

		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",   ");

		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\",   ");

		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");

		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");

		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");

		sql.append(" funcionariocargo.codigo as \"funcionariocargo.codigo\", funcionariocargo.cargo as \"funcionariocargo.cargo\",  ");
		sql.append(" funcionario.codigo as \"funcionario.codigo\", funcionario.matricula as \"funcionario.matricula\",  ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\",  ");

		sql.append(" crad.codigo as \"crad.codigo\", crad.identificadorCentroResultado as \"crad.identificadorCentroResultado\",  ");
		sql.append(" crad.descricao as \"crad.descricao\"   ");

		sql.append(" FROM CentroResultadoOrigem ");
		sql.append(" left join centroreceita on centroreceita.codigo = CentroResultadoOrigem.centroreceita ");
		sql.append(" left join categoriadespesa on categoriadespesa.codigo = CentroResultadoOrigem.categoriadespesa ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = CentroResultadoOrigem.unidadeensino");
		sql.append(" left join departamento on departamento.codigo = CentroResultadoOrigem.departamento");
		sql.append(" left join funcionariocargo on funcionariocargo.codigo = CentroResultadoOrigem.funcionariocargo");
		sql.append(" left join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" left join pessoa on pessoa.codigo = funcionario.pessoa");
		sql.append(" left join curso on curso.codigo = CentroResultadoOrigem.curso");
		sql.append(" left join turno on turno.codigo = CentroResultadoOrigem.turno");
		sql.append(" left join turma on turma.codigo = CentroResultadoOrigem.turma");
		sql.append(" left join centroresultado as crad on crad.codigo = CentroResultadoOrigem.centroResultadoAdministrativo ");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoOrigemVO> consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE CentroResultadoOrigem.codOrigem = '").append(codOrigem).append("' ");
			sqlStr.append(" and CentroResultadoOrigem.tipoCentroResultadoOrigem = '").append(tipoCentroResultadoOrigemEnum.name()).append("' ");
			sqlStr.append(" ORDER BY CentroResultadoOrigem.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoOrigemVO consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE CentroResultadoOrigem.codOrigem = '").append(codOrigem).append("' ");
			sqlStr.append(" and CentroResultadoOrigem.tipoCentroResultadoOrigem = '").append(tipoCentroResultadoOrigemEnum.name()).append("' ");
			sqlStr.append(" ORDER BY CentroResultadoOrigem.codigo  limit 1");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( CentroResultadoOrigemVO ).");
			}
			CentroResultadoOrigemVO obj = new CentroResultadoOrigemVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}*/

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoOrigemVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE CentroResultadoOrigem.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( CentroResultadoOrigemVO ).");
			}
			CentroResultadoOrigemVO obj = new CentroResultadoOrigemVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoOrigemVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CentroResultadoOrigemVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			CentroResultadoOrigemVO obj = new CentroResultadoOrigemVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(CentroResultadoOrigemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("CentroResultadoOrigem.codigo"));
		obj.setCodOrigem(dadosSQL.getString("CentroResultadoOrigem.codOrigem"));
		obj.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.valueOf(dadosSQL.getString("CentroResultadoOrigem.tipoCentroResultadoOrigem")));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("CentroResultadoOrigem.tipoNivelCentroResultadoEnum"))) {
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("CentroResultadoOrigem.tipoNivelCentroResultadoEnum")));
		}
		obj.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.valueOf(dadosSQL.getString("CentroResultadoOrigem.tipoMovimentacaoCentroResultadoOrigemEnum")));

		obj.setDataMovimentacao(dadosSQL.getTimestamp("CentroResultadoOrigem.dataMovimentacao"));
		obj.setValor(dadosSQL.getDouble("CentroResultadoOrigem.valor"));
		obj.setPorcentagem(dadosSQL.getDouble("CentroResultadoOrigem.porcentagem"));
		obj.setQuantidade(dadosSQL.getDouble("CentroResultadoOrigem.quantidade"));

		obj.getCategoriaDespesaVO().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getCategoriaDespesaVO().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));
		obj.getCategoriaDespesaVO().setInformarTurma((dadosSQL.getString("categoriaDespesa.informarTurma")));
		obj.getCategoriaDespesaVO().setNivelCategoriaDespesa((dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa")));
		obj.getCategoriaDespesaVO().setExigeCentroCustoRequisitante((dadosSQL.getBoolean("categoriaDespesa.exigeCentroCustoRequisitante")));
		obj.getCategoriaDespesaVO().setCategoriaDespesaPrincipal((dadosSQL.getInt("categoriaDespesa.categoriadespesaprincipal")));
		obj.getCategoriaDespesaVO().setIdentificadorCategoriaDespesa((dadosSQL.getString("categoriaDespesa.identificadorcategoriadespesa")));

		obj.getCentroReceitaVO().setCodigo((dadosSQL.getInt("centroreceita.codigo")));
		obj.getCentroReceitaVO().setDescricao((dadosSQL.getString("centroreceita.descricao")));
		obj.getCentroReceitaVO().setIdentificadorCentroReceita((dadosSQL.getString("centroreceita.identificadorcentroreceita")));

		obj.getCursoVO().setCodigo((dadosSQL.getInt("curso.codigo")));
		obj.getCursoVO().setNome((dadosSQL.getString("curso.nome")));

		obj.getTurnoVO().setCodigo((dadosSQL.getInt("turno.codigo")));
		obj.getTurnoVO().setNome((dadosSQL.getString("turno.nome")));

		obj.getTurmaVO().setCodigo((dadosSQL.getInt("turma.codigo")));
		obj.getTurmaVO().setIdentificadorTurma((dadosSQL.getString("turma.identificadorturma")));

		obj.getUnidadeEnsinoVO().setCodigo((dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getUnidadeEnsinoVO().setNome((dadosSQL.getString("unidadeEnsino.nome")));

		obj.getDepartamentoVO().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamentoVO().setNome((dadosSQL.getString("departamento.nome")));

		obj.getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionariocargo.codigo")));
		obj.getFuncionarioCargoVO().getCargo().setCodigo((dadosSQL.getInt("funcionariocargo.cargo")));
		obj.getFuncionarioCargoVO().getFuncionarioVO().setCodigo((dadosSQL.getInt("funcionario.codigo")));
		obj.getFuncionarioCargoVO().getFuncionarioVO().setMatricula((dadosSQL.getString("funcionario.matricula")));
		obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setNome((dadosSQL.getString("pessoa.nome")));

		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("crad.codigo")));
		obj.getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("crad.descricao")));
		obj.getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("crad.identificadorCentroResultado")));		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return;
		}
		getFacadeFactory().getCategoriaDespesaRateioFacade().consultaRapidaPorCategoriaDespesaVO(obj.getCategoriaDespesaVO(), false, Uteis.NIVELMONTARDADOS_TODOS, null);

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return CentroResultadoOrigem.idEntidade;
	}

	@Override
	public CentroResultadoOrigemVO consultarCentroResultadoOrigemExistenteLista(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem) {
		for (CentroResultadoOrigemVO objExistente : listaCentroResultadoOrigem) {
			if (objExistente.equalsCentroResultadoOrigem(centroResultadoOrigem)) {
				return objExistente;
			}
		}
		return null;
	}

	@Override
	public void realizarAgrupamentoCentroResultadoOrigemVOSomandoValor(List<CentroResultadoOrigemVO> centroResultadoOrigemFinalVOs, List<CentroResultadoOrigemVO> centroResultadoOrigemAdicionarVOs, boolean adicionarComoClone) throws Exception {
		for (CentroResultadoOrigemVO centroResultadoOrigemVO : centroResultadoOrigemAdicionarVOs) {
			CentroResultadoOrigemVO centroResultadoOrigemVO2 = consultarCentroResultadoOrigemExistenteLista(centroResultadoOrigemFinalVOs, centroResultadoOrigemVO);
			if (centroResultadoOrigemVO2 == null) {
				if (!adicionarComoClone) {
					centroResultadoOrigemFinalVOs.add(centroResultadoOrigemVO);
				} else {
					centroResultadoOrigemFinalVOs.add(centroResultadoOrigemVO.getClone());
				}
			} else {
				centroResultadoOrigemVO2.setValor(centroResultadoOrigemVO2.getValor() + centroResultadoOrigemVO.getValor());
			}
		}
	}

	/**
	 *  Excluir os {@link CentroResultadoOrigemVO} da {@link ContaPagarVO} informada.
	 *  
	 * @param CodOrigem - Código da Conta à Pagar
	 * @param tipoCentroResultadoOrigemEnum - O valor {@link TipoCentroResultadoOrigemEnum} Exemplo: TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()
	 */
	@Override
	public void excluirPorContaPagarETipoOrigem(String CodOrigem, String tipoCentroResultadoOrigemEnum, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from centroresultadoorigem where codigo in (");
		sql.append(" select codigo from centroresultadoorigem where codorigem = ? and tipocentroresultadoorigem = ? )");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { CodOrigem, tipoCentroResultadoOrigemEnum });
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarCentroResultadoOrigemPorRateioCategoriaDespesa(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual, boolean validarDados, UsuarioVO usuario) throws Exception {
		ConsistirException consistirException = new ConsistirException();
		centroResultadoOrigem.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(centroResultadoOrigem.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		adicionarCentroResultadoOrigemPorRateioCategoriaDespesaAcademico(listaCentroResultadoOrigem, centroResultadoOrigem, valoTotalCalculoPercentual,  consistirException, validarDados, usuario);
		adicionarCentroResultadoOrigemPorRateioCategoriaDespesaAdministrativo(listaCentroResultadoOrigem, centroResultadoOrigem, valoTotalCalculoPercentual, consistirException, validarDados, usuario);
		if(!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
}
	}
	
	private void adicionarCentroResultadoOrigemPorRateioCategoriaDespesaAcademico(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual, ConsistirException consistirException, boolean validarDados, UsuarioVO usuario) throws Exception {
		for(CategoriaDespesaRateioVO categoriaDespesaRateioVO: centroResultadoOrigem.getCategoriaDespesaVO().getListaCategoriaDespesaRateioAcademico()) {
			CentroResultadoOrigemVO centroResultadoOrigemVO = realizarCriacaoCentroResultadoOrigemComBaseRateioCategoriaDespesa(centroResultadoOrigem, categoriaDespesaRateioVO, valoTotalCalculoPercentual, TipoNivelCentroResultadoEnum.CURSO, usuario);
			if(Uteis.isAtributoPreenchido(centroResultadoOrigemVO.getCentroResultadoAdministrativo())) {					
				adicionarCentroResultadoOrigem(listaCentroResultadoOrigem, centroResultadoOrigemVO, valoTotalCalculoPercentual, validarDados, usuario);
			}else {				
				consistirException.adicionarListaMensagemErro("O CENTRO DE RESULTADO do CURSO: "+centroResultadoOrigemVO.getCursoVO().getNome()+" não foi encontrado.");				
			}
		}		
	}
	
	private CentroResultadoOrigemVO realizarCriacaoCentroResultadoOrigemComBaseRateioCategoriaDespesa(CentroResultadoOrigemVO centroResultadoOrigem, CategoriaDespesaRateioVO categoriaDespesaRateioVO, Double valoTotalCalculoPercentual, TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum, UsuarioVO usuario) throws Exception{
		CentroResultadoOrigemVO centroResultadoOrigemVO =  new CentroResultadoOrigemVO();
		centroResultadoOrigemVO.setUnidadeEnsinoVO((UnidadeEnsinoVO)centroResultadoOrigem.getUnidadeEnsinoVO().clone());
		centroResultadoOrigemVO.setQuantidade(centroResultadoOrigem.getQuantidade());
		centroResultadoOrigemVO.setCategoriaDespesaVO((CategoriaDespesaVO)centroResultadoOrigem.getCategoriaDespesaVO().clone());
		centroResultadoOrigemVO.setTipoCentroResultadoOrigemEnum(centroResultadoOrigem.getTipoCentroResultadoOrigemEnum());
		centroResultadoOrigemVO.setTipoMovimentacaoCentroResultadoOrigemEnum(centroResultadoOrigem.getTipoMovimentacaoCentroResultadoOrigemEnum());
		centroResultadoOrigemVO.setTipoNivelCentroResultadoEnum(tipoNivelCentroResultadoEnum);
		if(Uteis.isAtributoPreenchido(categoriaDespesaRateioVO.getUnidadeEnsinoVO().getCodigo())) {
			centroResultadoOrigemVO.getUnidadeEnsinoVO().setCodigo(categoriaDespesaRateioVO.getUnidadeEnsinoVO().getCodigo());		
			centroResultadoOrigemVO.getUnidadeEnsinoVO().setNome(categoriaDespesaRateioVO.getUnidadeEnsinoVO().getNome());
		}
		if(tipoNivelCentroResultadoEnum.equals(TipoNivelCentroResultadoEnum.CURSO)) {
			centroResultadoOrigemVO.getCursoVO().setCodigo(categoriaDespesaRateioVO.getCursoVO().getCodigo());
			centroResultadoOrigemVO.getCursoVO().setNome(categoriaDespesaRateioVO.getCursoVO().getNome());
			centroResultadoOrigemVO.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsinoCurso(centroResultadoOrigemVO.getUnidadeEnsinoVO().getCodigo(), centroResultadoOrigemVO.getCursoVO().getCodigo(), usuario));
			
		}else {
			centroResultadoOrigemVO.getDepartamentoVO().setCodigo(categoriaDespesaRateioVO.getDepartamentoVO().getCodigo());
			centroResultadoOrigemVO.getDepartamentoVO().setNome(categoriaDespesaRateioVO.getDepartamentoVO().getNome());
			centroResultadoOrigemVO.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorDepartamento(centroResultadoOrigemVO.getDepartamentoVO().getCodigo(), usuario));
		}
		centroResultadoOrigemVO.setPorcentagem(categoriaDespesaRateioVO.getPorcentagem());
		centroResultadoOrigemVO.calcularValor(valoTotalCalculoPercentual);
		
		return centroResultadoOrigemVO;
	}
	
	private void adicionarCentroResultadoOrigemPorRateioCategoriaDespesaAdministrativo(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual, ConsistirException consistirException, boolean validarDados, UsuarioVO usuario) throws Exception {		
		for(CategoriaDespesaRateioVO categoriaDespesaRateioVO: centroResultadoOrigem.getCategoriaDespesaVO().getListaCategoriaDespesaRateioAdministrativo()) {
			CentroResultadoOrigemVO centroResultadoOrigemVO =  realizarCriacaoCentroResultadoOrigemComBaseRateioCategoriaDespesa(centroResultadoOrigem, categoriaDespesaRateioVO, valoTotalCalculoPercentual, TipoNivelCentroResultadoEnum.DEPARTAMENTO, usuario);
			if(Uteis.isAtributoPreenchido(centroResultadoOrigemVO.getCentroResultadoAdministrativo())) {	
				adicionarCentroResultadoOrigem(listaCentroResultadoOrigem, centroResultadoOrigemVO, valoTotalCalculoPercentual, validarDados, usuario);
			} else {
				consistirException.adicionarListaMensagemErro("O CENTRO DE RESULTADO do DEPARTAMENTO: "+centroResultadoOrigemVO.getDepartamentoVO().getNome()+" não foi encontrado.");				
			}
		}
				
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUnidadeEnsinoCentroResultadoOrigem (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		 final StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append("UPDATE centroresultadoorigem SET unidadeensino  = t.unidadeensino  FROM ( ");
		 sqlStr.append("SELECT ");
		 sqlStr.append("  centroresultadoorigem.codigo as centroresultadoorigem,turma.unidadeensino   ");
		 sqlStr.append("FROM centroresultadoorigem ");
		 sqlStr.append(" INNER JOIN turma      ON centroresultadoorigem.turma = turma.codigo ");
		 sqlStr.append("WHERE turma.codigo = ? AND centroresultadoorigem.unidadeensino IS NOT NULL ");
		 sqlStr.append(") as t WHERE centroresultadoorigem.codigo = t.centroresultadoorigem;");
		 sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
	}
	private void validarCodOrigemContaReceberManualOrigemBiblioteca(ContaReceberVO obj) throws ConsistirException {
		if (obj.getTipoOrigemContaReceber().isBiblioteca() && obj.getCodOrigem().isEmpty() && !Uteis.isAtributoPreenchido(obj.getListaCentroResultadoOrigem())) {
			throw new ConsistirException("O Centro Resultado deve ser informado manualmente (Conta Receber).");
		}
	}
}
