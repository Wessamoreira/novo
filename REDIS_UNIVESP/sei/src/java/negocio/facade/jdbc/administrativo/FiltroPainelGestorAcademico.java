package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.EvolucaoAcademicaNivelEducacionalVO;
import negocio.comuns.administrativo.FiltroPainelGestorAcademicoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoAcademicoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoProcessoSeletivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum;
import negocio.comuns.administrativo.enumeradores.TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FiltroPainelGestorAcademicoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class FiltroPainelGestorAcademico extends ControleAcesso implements FiltroPainelGestorAcademicoInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = -2592102644313630913L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO) throws Exception {
		if (filtroPainelGestorAcademicoVO.isNovoObj()) {
			incluir(filtroPainelGestorAcademicoVO);
		} else {
			alterar(filtroPainelGestorAcademicoVO);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void incluir(final FiltroPainelGestorAcademicoVO obj) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder("INSERT INTO FiltroPainelGestorAcademico ");
			sql.append(" (preMatriculado, matriculado,  calouro, reingresso, reabertura,");
			sql.append(" portadorDiploma, transferenciaInterna, transferenciaExterna, veterano, possivelFormando,");
			sql.append(" naoMatriculadoTotal, naoMatriculadoAdimplente, naoMatriculadoInadimplente, rematriculado, trancado,");
			sql.append(" cancelado, transferenciaSaida, numeroAlunoTurno, numeroAlunoSexo, numeroAlunoTurma,");
			sql.append(" numeroTurma, numeroTurmaTurno, numeroProfessor, numeroProfessorTurno, numeroCursoMinistrado,");
			sql.append(" apresentarGrafico, visualizarAnaliticamente, usuario, monitoramentoImpressaoDeclaracao, ");
			sql.append(" abandonoCurso, formado, monitoramentoProcessoSeletivo, tipoNivelEducacional, periodicidadeCurso, jubilado) ");
			sql.append(" VALUES ( ?, ?, ?, ? ,?, ?, ?, ?, ? ,?, ?, ?, ?, ? ,?, ?, ?, ?, ? , ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setBoolean(x++, obj.getPreMatriculado());
					sqlInserir.setBoolean(x++, obj.getMatriculado());
					sqlInserir.setBoolean(x++, obj.getCalouro());
					sqlInserir.setBoolean(x++, obj.getReingresso());
					sqlInserir.setBoolean(x++, obj.getReabertura());
					sqlInserir.setBoolean(x++, obj.getPortadorDiploma());
					sqlInserir.setBoolean(x++, obj.getTransferenciaInterna());
					sqlInserir.setBoolean(x++, obj.getTransferenciaExterna());
					sqlInserir.setBoolean(x++, obj.getVeterano());
					sqlInserir.setBoolean(x++, obj.getPossivelFormando());
					sqlInserir.setBoolean(x++, obj.getNaoMatriculadoTotal());
					sqlInserir.setBoolean(x++, obj.getNaoMatriculadoAdimplente());
					sqlInserir.setBoolean(x++, obj.getNaoMatriculadoInadimplente());
					sqlInserir.setBoolean(x++, obj.getRematriculado());
					sqlInserir.setBoolean(x++, obj.getTrancado());
					sqlInserir.setBoolean(x++, obj.getCancelado());
					sqlInserir.setBoolean(x++, obj.getTransferenciaSaida());
					sqlInserir.setBoolean(x++, obj.getNumeroAlunoTurno());
					sqlInserir.setBoolean(x++, obj.getNumeroAlunoSexo());
					sqlInserir.setBoolean(x++, obj.getNumeroAlunoTurma());
					sqlInserir.setBoolean(x++, obj.getNumeroTurma());
					sqlInserir.setBoolean(x++, obj.getNumeroTurmaTurno());
					sqlInserir.setBoolean(x++, obj.getNumeroProfessor());
					sqlInserir.setBoolean(x++, obj.getNumeroProfessorTurno());
					sqlInserir.setBoolean(x++, obj.getNumeroCursoMinistrado());
					sqlInserir.setBoolean(x++, obj.getApresentarGrafico());
					sqlInserir.setBoolean(x++, obj.getVisualizarAnaliticamente());
					sqlInserir.setInt(x++, obj.getUsuario().getCodigo());
					sqlInserir.setBoolean(x++, obj.getMonitoramentoImpressaoDeclaracao());					
					sqlInserir.setBoolean(x++, obj.getAbandonoCurso());
					sqlInserir.setBoolean(x++, obj.getFormado());
					sqlInserir.setBoolean(x++, obj.getMonitoramentoProcessoSeletivo());
					sqlInserir.setString(x++, obj.getTipoNivelEducacional());
					sqlInserir.setString(x++, obj.getPeriodicidadeCurso().name().toString());
					sqlInserir.setBoolean(x++, obj.getJubilado());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	private void alterar(final FiltroPainelGestorAcademicoVO obj) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder("UPDATE FiltroPainelGestorAcademico set ");
			sql.append(" preMatriculado= ?, matriculado =?,  calouro = ?, reingresso = ?, reabertura = ?,");
			sql.append(" portadorDiploma = ?, transferenciaInterna = ?, transferenciaExterna = ?, veterano = ?, possivelFormando = ?,");
			sql.append(" naoMatriculadoTotal = ?, naoMatriculadoAdimplente = ?, naoMatriculadoInadimplente = ?, rematriculado = ?, trancado = ?,");
			sql.append(" cancelado = ?, transferenciaSaida = ?, numeroAlunoTurno = ?, numeroAlunoSexo = ?, numeroAlunoTurma = ?,");
			sql.append(" numeroTurma = ?, numeroTurmaTurno = ?, numeroProfessor = ?, numeroProfessorTurno = ?, numeroCursoMinistrado = ?, apresentarGrafico = ?, ");
			sql.append(" visualizarAnaliticamente = ?, usuario = ?, monitoramentoImpressaoDeclaracao = ?, abandonoCurso = ?, formado = ?, monitoramentoProcessoSeletivo = ?, tipoNivelEducacional = ?, ");
			sql.append(" periodicidadeCurso = ?, jubilado = ? ");
			sql.append(" where codigo = ? ");

			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				int x = 1;

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setBoolean(x++, obj.getPreMatriculado());
					sqlAlterar.setBoolean(x++, obj.getMatriculado());
					sqlAlterar.setBoolean(x++, obj.getCalouro());
					sqlAlterar.setBoolean(x++, obj.getReingresso());
					sqlAlterar.setBoolean(x++, obj.getReabertura());
					sqlAlterar.setBoolean(x++, obj.getPortadorDiploma());
					sqlAlterar.setBoolean(x++, obj.getTransferenciaInterna());
					sqlAlterar.setBoolean(x++, obj.getTransferenciaExterna());
					sqlAlterar.setBoolean(x++, obj.getVeterano());
					sqlAlterar.setBoolean(x++, obj.getPossivelFormando());
					sqlAlterar.setBoolean(x++, obj.getNaoMatriculadoTotal());
					sqlAlterar.setBoolean(x++, obj.getNaoMatriculadoAdimplente());
					sqlAlterar.setBoolean(x++, obj.getNaoMatriculadoInadimplente());
					sqlAlterar.setBoolean(x++, obj.getRematriculado());
					sqlAlterar.setBoolean(x++, obj.getTrancado());
					sqlAlterar.setBoolean(x++, obj.getCancelado());
					sqlAlterar.setBoolean(x++, obj.getTransferenciaSaida());
					sqlAlterar.setBoolean(x++, obj.getNumeroAlunoTurno());
					sqlAlterar.setBoolean(x++, obj.getNumeroAlunoSexo());
					sqlAlterar.setBoolean(x++, obj.getNumeroAlunoTurma());
					sqlAlterar.setBoolean(x++, obj.getNumeroTurma());
					sqlAlterar.setBoolean(x++, obj.getNumeroTurmaTurno());
					sqlAlterar.setBoolean(x++, obj.getNumeroProfessor());
					sqlAlterar.setBoolean(x++, obj.getNumeroProfessorTurno());
					sqlAlterar.setBoolean(x++, obj.getNumeroCursoMinistrado());
					sqlAlterar.setBoolean(x++, obj.getApresentarGrafico());
					sqlAlterar.setBoolean(x++, obj.getVisualizarAnaliticamente());
					sqlAlterar.setInt(x++, obj.getUsuario().getCodigo());
					sqlAlterar.setBoolean(x++, obj.getMonitoramentoImpressaoDeclaracao());
					sqlAlterar.setBoolean(x++, obj.getAbandonoCurso());
					sqlAlterar.setBoolean(x++, obj.getFormado());
					sqlAlterar.setBoolean(x++, obj.getMonitoramentoProcessoSeletivo());
					sqlAlterar.setString(x++, obj.getTipoNivelEducacional());
					sqlAlterar.setString(x++, obj.getPeriodicidadeCurso().name().toString());
					sqlAlterar.setBoolean(x++, obj.getJubilado());					
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj);
			}
			;

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public FiltroPainelGestorAcademicoVO consultarFiltroPorUsuario(UsuarioVO usuario) {
		String sqlStr = "SELECT * FROM FiltroPainelGestorAcademico WHERE usuario = " + usuario.getCodigo();
		SqlRowSet tabelaResultado = null;
		FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO = null;
		try {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (tabelaResultado.next()) {
				filtroPainelGestorAcademicoVO = (montarDados(tabelaResultado));
			} else {
				filtroPainelGestorAcademicoVO = new FiltroPainelGestorAcademicoVO();
				filtroPainelGestorAcademicoVO.setUsuario(usuario);
			}
			return filtroPainelGestorAcademicoVO;
		} catch (Exception e) {
			filtroPainelGestorAcademicoVO = new FiltroPainelGestorAcademicoVO();
			filtroPainelGestorAcademicoVO.setUsuario(usuario);
			return filtroPainelGestorAcademicoVO;
		} finally {
			sqlStr = null;
			tabelaResultado = null;
		}
	}

	private FiltroPainelGestorAcademicoVO montarDados(SqlRowSet rs) {
		FiltroPainelGestorAcademicoVO obj = new FiltroPainelGestorAcademicoVO();
		obj.setPreMatriculado(rs.getBoolean("preMatriculado"));
		obj.setMatriculado(rs.getBoolean("matriculado"));
		obj.setTipoNivelEducacional(rs.getString("tipoNivelEducacional"));
		obj.setCalouro(rs.getBoolean("calouro"));
		obj.setReingresso(rs.getBoolean("reingresso"));
		obj.setReabertura(rs.getBoolean("reabertura"));
		obj.setPortadorDiploma(rs.getBoolean("portadorDiploma"));
		obj.setTransferenciaInterna(rs.getBoolean("transferenciaInterna"));
		obj.setTransferenciaExterna(rs.getBoolean("transferenciaExterna"));
		obj.setVeterano(rs.getBoolean("veterano"));
		obj.setPossivelFormando(rs.getBoolean("possivelFormando"));
		obj.setNaoMatriculadoTotal(rs.getBoolean("naoMatriculadoTotal"));
		obj.setNaoMatriculadoAdimplente(rs.getBoolean("naoMatriculadoAdimplente"));
		obj.setNaoMatriculadoInadimplente(rs.getBoolean("naoMatriculadoInadimplente"));
		obj.setRematriculado(rs.getBoolean("rematriculado"));
		obj.setTrancado(rs.getBoolean("trancado"));
		obj.setCancelado(rs.getBoolean("cancelado"));
		obj.setAbandonoCurso(rs.getBoolean("abandonoCurso"));
		obj.setFormado(rs.getBoolean("formado"));
		obj.setTransferenciaSaida(rs.getBoolean("transferenciaSaida"));
		obj.setNumeroAlunoTurno(rs.getBoolean("numeroAlunoTurno"));
		obj.setNumeroAlunoSexo(rs.getBoolean("numeroAlunoSexo"));
		obj.setNumeroAlunoTurma(rs.getBoolean("numeroAlunoTurma"));
		obj.setNumeroTurma(rs.getBoolean("numeroTurma"));
		obj.setNumeroTurmaTurno(rs.getBoolean("numeroTurmaTurno"));
		obj.setNumeroProfessor(rs.getBoolean("numeroProfessor"));
		obj.setNumeroProfessorTurno(rs.getBoolean("numeroProfessorTurno"));
		obj.setNumeroCursoMinistrado(rs.getBoolean("numeroCursoMinistrado"));
		obj.setMonitoramentoImpressaoDeclaracao(rs.getBoolean("monitoramentoImpressaoDeclaracao"));
		obj.setVisualizarAnaliticamente(rs.getBoolean("visualizarAnaliticamente"));
		obj.setApresentarGrafico(rs.getBoolean("apresentarGrafico"));
		obj.setMonitoramentoProcessoSeletivo(rs.getBoolean("monitoramentoProcessoSeletivo"));
		obj.getUsuario().setCodigo(rs.getInt("usuario"));
		obj.setCodigo(rs.getInt("codigo"));
		obj.setJubilado(rs.getBoolean("jubilado"));
		if(Uteis.isAtributoPreenchido(rs.getString("periodicidadeCurso"))) {
			obj.setPeriodicidadeCurso(PeriodicidadeEnum.valueOf(rs.getString("periodicidadeCurso")));
		}		
		obj.setNovoObj(false);
		return obj;

	}

	@Override
	public void consultarDadosDetalheMonitoramentoAcademico(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao, Integer codigo, Integer limit, Integer offset) throws Exception {
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		filtroPainelGestorAcademico.getPainelGestorDetalheMonitoramentoAcademicoVOs().clear();
		filtroPainelGestorAcademico.setOpcaoAtual(opcao);
		filtroPainelGestorAcademico.setCodigoBase(codigo);
		filtroPainelGestorAcademico.setTituloApresentar("");
		
//		PRE-MATRÍCULA
		if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculado"));
			consultarDetalheMonitoramentoAcademicoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoPreMatriculadoPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_FORMAINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculado"));
			consultarDetalheMonitoramentoAcademicoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculado"));
			consultarDetalheMonitoramentoAcademicoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);

//			PRÉ-MATRICULA CALOURO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_CALOURO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculadoCalouro"));
			consultarDetalheMonitoramentoAcademicoPreMatriculadoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoPreMatriculadoCalouroPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoPreMatriculadoCalouroPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CALOURO_FORMAINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculadoCalouro"));
			consultarDetalheMonitoramentoAcademicoPreMatriculadoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CALOURO_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculadoCalouro"));
			consultarDetalheMonitoramentoAcademicoPreMatriculadoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);

//			PRE-MATRÍCULA VETERANO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_VETERANO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculadoVeterano"));
			consultarDetalheMonitoramentoAcademicoPreMatriculadoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoPreMatriculadoVeteranoPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoPreMatriculadoVeteranoPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_VETERANO_FORMAINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculadoVeterano"));
			consultarDetalheMonitoramentoAcademicoPreMatriculadoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_preMatriculadoVeterano"));
			consultarDetalheMonitoramentoAcademicoPreMatriculadoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
//			NÃO RENOVADOS - NÃO PRÉ-MATRICULADOS
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_NAO_PRE_MATRICULADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoRenovadoNaoPreMatriculado"));
			consultarDetalheMonitoramentoAcademicoNaoRenovadoNaoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoNaoRenovadoNaoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoNaoRenovadoNaoPreMatriculadoPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_NAO_PRE_MATRICULADO_FORMA_INGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoRenovadoNaoPreMatriculado"));
			consultarDetalheMonitoramentoAcademicoNaoRenovadoNaoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_NAO_PRE_MATRICULADO_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoRenovadoNaoPreMatriculado"));
			consultarDetalheMonitoramentoAcademicoNaoRenovadoNaoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
		
//			MATRÍCULA
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MATRICULA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_matriculado"));
			consultarDetalheMonitoramentoAcademicoMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoMatriculaPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoMatriculaPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_MATRICULA_FORMAINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_matriculado"));
			consultarDetalheMonitoramentoAcademicoMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_MATRICULA_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_matriculado"));
			consultarDetalheMonitoramentoAcademicoMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);

//			CALOURO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CALOURO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_calouro"));
			consultarDetalheMonitoramentoAcademicoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoCalouroPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoCalouroPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CALOURO_FORMAINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_calouro"));
			consultarDetalheMonitoramentoAcademicoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CALOURO_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_calouro"));
			consultarDetalheMonitoramentoAcademicoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
//			REINGRESSO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_reingresso"));
			consultarDetalheMonitoramentoAcademicoReingresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			REABERTURA
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REABERTURA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_reabertura"));
			consultarDetalheMonitoramentoAcademicoReabertura(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			PORTADOR DIPLOMA
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PORTADOR_DIPLOMA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_portadorDiploma"));
			consultarDetalheMonitoramentoAcademicoPortadorDiploma(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			TRANSFERENCIA INTERNA
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_INTERNA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_transferenciaInterna"));
			consultarDetalheMonitoramentoAcademicoTransferenciaInterna(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			TRANSFERENCIA EXTERNA
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_EXTERNA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_transferenciaExterna"));
			consultarDetalheMonitoramentoAcademicoTransferenciaExterna(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			VETERANO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.VETERANO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_veterano"));
			consultarDetalheMonitoramentoAcademicoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoVeteranoPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoVeteranoPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_VETERANO_FORMAINGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_veterano"));
			consultarDetalheMonitoramentoAcademicoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_VETERANO_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_veterano"));
			consultarDetalheMonitoramentoAcademicoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
//			POSSÍVEL FORMANDO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POSSIVEL_FORMANDO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_possivelFormando"));
			consultarDetalheMonitoramentoAcademicoPossivelFormando(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			NÃO REMATRICULADO TOTAL
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_TOTAL)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoTotal"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoTotal(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoNaoRenovadosTotalPorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoNaoRenovadosTotalPorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_FORMA_INGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoTotal"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoTotal(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoTotal"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoTotal(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
//			NÃO REMATRICULADO ADIMPLENTE
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_ADIMPLENTE)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoAdimplente"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoAdimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoNaoRenovadosAdimplentePorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoNaoRenovadosAdimplentePorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_FORMA_INGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoTotal"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoAdimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoTotal"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoAdimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
//			NÃO REMATRICULADO INADIMPLENTE
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_INADIMPLENTE)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoInadimplente"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoInadimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, null, limit, offset);
			consultarMonitoramentoAcademicoNaoRenovadosInadimplentePorFormaIngresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
            consultarMonitoramentoAcademicoNaoRenovadosInadimplentePorCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_FORMA_INGRESSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoInadimplente"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoInadimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, codigo, null, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_CURSO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_naoMatriculadoInadimplente"));
			consultarDetalheMonitoramentoAcademicoNaoRematriculadoInadimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, null, codigo, limit, offset);
			
//			REMATRICULADO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REMATRICULADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_rematriculado"));
			consultarDetalheMonitoramentoAcademicoRematriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			TRANCADO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANCADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_trancado"));
			consultarDetalheMonitoramentoAcademicoTrancado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			ABANDONO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.ABANDONO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_abandonoCurso"));
			consultarDetalheMonitoramentoAcademicoAbandonoCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			FORMADO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FORMADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_formado"));
			consultarDetalheMonitoramentoAcademicoFormado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			CANCELADO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CANCELADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_cancelado"));
			consultarDetalheMonitoramentoAcademicoCancelado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, null, ano, semestre, limit, offset);
			consultarMonitoramentoAcademicoCanceladoPorTipoJustificativa(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CANCELAMENTO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_cancelado"));
			consultarDetalheMonitoramentoAcademicoCancelado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
			
//			SAIDA
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_SAIDA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_transferenciaSaida"));
			consultarDetalheMonitoramentoAcademicoTransferenciaSaida(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
			
//			NUMERO ALUNO TURNO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_TURNO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroAlunoTurno"));
			consultarDetalheMonitoramentoAcademicoNumeroAlunoTurno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_SEXO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroAlunoSexo"));
			consultarDetalheMonitoramentoAcademicoNumeroAlunoSexo(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset, codigo);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_TURMA) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NUMERO_TURMA_ALUNO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NUMERO_TURMA_TURNO_ALUNO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroAlunoTurma"));
			consultarDetalheMonitoramentoAcademicoNumeroAlunoTurma(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA_TURNO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroTurma"));
			consultarDetalheMonitoramentoAcademicoNumeroTurmaTurno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroTurmaTurno"));
			consultarDetalheMonitoramentoAcademicoNumeroTurma(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_PROFESSOR)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroProfessor"));
			consultarDetalheMonitoramentoAcademicoNumeroProfessor(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_PROFESSOR_TURNO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroProfessorTurno"));
			consultarDetalheMonitoramentoAcademicoNumeroProfessorTurno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_CURSO_MINISTRADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroCursoMinistrado"));
			consultarDetalheMonitoramentoAcademicoNumeroCursoMinistrado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CURSO_MINISTRADO_ALUNO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_numeroAlunoCurso"));
			consultarDetalheMonitoramentoAcademicoNumeroCursoMinistradoAluno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.IMPRESSAO_DECLARACAO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_monitoramentoImpressaoDeclaracao"));
			if (codigo > 0) {
				try {
					TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
					filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_monitoramentoImpressaoDeclaracao") + " - " + textoPadraoDeclaracaoVO.getDescricao());
				} catch (Exception e) {
					filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_monitoramentoImpressaoDeclaracao"));
				}
			} else {
				filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_monitoramentoImpressaoDeclaracao"));
			}
			consultarDetalheMonitoramentoAcademicoImpressaoDeclaracao(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_IMPRESSAO_DECLARACAO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_monitoramentoImpressaoDeclaracao"));

			consultarDetalheMonitoramentoAcademicoImpressaoDeclaracao(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, codigo, ano, semestre, limit, offset);
		
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO)) {
			consultarMonitoramentoProcessoSeletivo(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, opcao);
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_APROVADO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_APROVADO_NAO_MATRICULADO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_CONFIRMADO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_GERAL)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_MATRICULADO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_NAO_CONFIRMADO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_PRE_MATRICULADO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_REPROVADO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_NAO_COMPARECEU) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_DETALHE_INSCRITO_SEM_RESULTADO)) {			
			consultarDetalheMonitoramentoProcessoSeletivo(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset, opcao, codigo);
			
//		JUBILADO
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.JUBILADO)) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_jubilado"));
			consultarDetalheMonitoramentoAcademicoAbandonoCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, limit, offset);
		}
		
//		EVOLUCAO ACADÊMICA
//		CALOURO
		if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INFANTIL_CALOURO_INSTITUICAO.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_infantil_calouro"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "IN", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INFANTIL_VETERANO_NIVEL_ATUAL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_infantil_veterano_nivel_atual"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "IN", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INFANTIL_VETERANO_NIVEL_ANTERIOR.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_infantil_veterano_nivel_anterior"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "IN", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INFANTIL_NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_infantil_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "IN", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INFANTIL_NAO_RENOVARAM_MESMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_infantil_naoRenovaramMesmoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "IN", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL, null);
		}
//		FUNDAMENTAL
		if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FUNDAMENTAL_CALOURO_INSTITUICAO.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_fundamental_calouro"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "BA", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FUNDAMENTAL_VETERANO_NIVEL_ATUAL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_fundamental_veterano_nivel_atual"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "BA", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FUNDAMENTAL_VETERANO_NIVEL_ANTERIOR.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_fundamental_veterano_nivel_anterior"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "BA", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FUNDAMENTAL_NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_fundamental_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "BA", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FUNDAMENTAL_NAO_RENOVARAM_MESMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_fundamental_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "BA", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL, null);
		}
		
//		MEDIO
		if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MEDIO_CALOURO_INSTITUICAO.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_medio_calouro"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "ME", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MEDIO_VETERANO_NIVEL_ATUAL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_medio_veterano_nivel_atual"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "ME", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MEDIO_VETERANO_NIVEL_ANTERIOR.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_medio_veterano_nivel_anterior"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "ME", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MEDIO_NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_medio_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "ME", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MEDIO_NAO_RENOVARAM_MESMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_medio_naoRenovaramMesmoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.ANUAL, "ME", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL, null);
		}
		
//		SUPERIOR
		if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.SUPERIOR_CALOURO_INSTITUICAO.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_superior_calouro"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "SU", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.SUPERIOR_VETERANO_NIVEL_ATUAL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_superior_veterano_nivel_atual"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "SU", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.SUPERIOR_VETERANO_NIVEL_ANTERIOR.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_superior_veterano_nivel_anterior"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "SU", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.SUPERIOR_NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_superior_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "SU", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.SUPERIOR_NAO_RENOVARAM_MESMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_superior_naoRenovaramMesmoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "SU", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL, null);
		}
		
//		POS-GRADUAÇÃO
		if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POS_GRADUACAO_CALOURO_INSTITUICAO.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_pos_graduacao_calouro"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.INTEGRAL, "PO", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POS_GRADUACAO_VETERANO_NIVEL_ATUAL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_pos_graduacao_veterano_nivel_atual"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.INTEGRAL, "PO", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POS_GRADUACAO_VETERANO_NIVEL_ANTERIOR.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_pos_graduacao_veterano_nivel_anterior"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.INTEGRAL, "PO", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POS_GRADUACAO_NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_pos_graduacao_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.INTEGRAL, "PO", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POS_GRADUACAO_NAO_RENOVARAM_MESMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_pos_graduacao_naoRenovaramMesmoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.INTEGRAL, "PO", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL, null);
		}
//		GRADUACAO-TECNOLOGICA
		if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.GRADUACAO_TECNOLOGICA_CALOURO_INSTITUICAO.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_graduacao_tecnologica_calouro"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "GT", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.GRADUACAO_TECNOLOGICA_VETERANO_NIVEL_ATUAL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_graduacao_tecnologica_veterano_nivel_atual"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "GT", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.GRADUACAO_TECNOLOGICA_VETERANO_NIVEL_ANTERIOR.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_graduacao_tecnologica_veterano_nivel_anterior"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "GT", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.GRADUACAO_TECNOLOGICA_NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_graduacao_tecnologica_naoRenovaramProximoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "GT", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL, null);
		} else if (opcao.toString().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.GRADUACAO_TECNOLOGICA_NAO_RENOVARAM_MESMO_NIVEL.toString())) {
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_FiltroPainelGestorAcademico_graduacao_tecnologica_naoRenovaramMesmoNivel"));
			consultarEvolucaoAcademicaNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, PeriodicidadeEnum.SEMESTRAL, "GT", TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL, null);
		}		
		
		
	}

	@Override
	public void consultarDadosMonitoramentoAcademico(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, PeriodicidadeEnum periodicidadeCurso, String ano, String semestre) throws Exception {
		if (periodicidadeCurso.equals(PeriodicidadeEnum.INTEGRAL)) {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoAlunoTurmaVOs().clear();
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademico("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoSexo("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao("");
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoAlunoSexoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoAlunoTurnoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoProfessorTurnoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoTurmaTurnoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoSituacaoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs().clear();
		filtroPainelGestorAcademico.getPainelGestorMonitoramentoProcessoSeletivoVOs().clear();
		filtroPainelGestorAcademico.setPainelGestorMonitoramentoProcessoSeletivoVO(null);
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida("");
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado("");
		if (filtroPainelGestorAcademico.getMatriculado()) {
			consultarMonitoramentoAcademicoMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		} else {
                    if (filtroPainelGestorAcademico.getCalouro()) {
			consultarMonitoramentoAcademicoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
                    }
                    if (filtroPainelGestorAcademico.getVeterano()) {
			consultarMonitoramentoAcademicoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
                    }
                }
		if (filtroPainelGestorAcademico.getTransferenciaInterna()) {
			consultarMonitoramentoAcademicoTransferenciaInterna(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getTransferenciaExterna()) {
			consultarMonitoramentoAcademicoTransferenciaExterna(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getPreMatriculado()) {
			consultarMonitoramentoAcademicoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoPreMatriculadoCalouro(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
			consultarMonitoramentoAcademicoPreMatriculadoVeterano(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getPortadorDiploma()) {
			consultarMonitoramentoAcademicoPortadorDiploma(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getRematriculado()) {
			consultarMonitoramentoAcademicoRematriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getReabertura()) {
			consultarMonitoramentoAcademicoReabertura(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getReingresso()) {
			consultarMonitoramentoAcademicoReingresso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
//		if (filtroPainelGestorAcademico.getNaoMatriculadoTotal()) {
//			consultarMonitoramentoAcademicoNaoRematriculadoTotal(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, filtrarPorAnoSemestre, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNaoMatriculadoAdimplente()) {
//			consultarMonitoramentoAcademicoNaoRematriculadoAdimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, filtrarPorAnoSemestre, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNaoMatriculadoInadimplente()) {
//			consultarMonitoramentoAcademicoNaoRematriculadoInadimplente(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, filtrarPorAnoSemestre, ano, semestre);
//		}
		if (filtroPainelGestorAcademico.getNaoMatriculadoNaoPreMatriculado()) {
			consultarMonitoramentoAcademicoNaoRematriculadoNaoPreMatriculado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
//		if (filtroPainelGestorAcademico.getPossivelFormando()) {
//			consultarMonitoramentoAcademicoPossivelFormando(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, filtrarPorAnoSemestre, ano, semestre);
//		}
		if (filtroPainelGestorAcademico.getFormado()) {
			consultarMonitoramentoAcademicoFormado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		
		if (filtroPainelGestorAcademico.getTrancado()) {
			consultarMonitoramentoAcademicoTrancado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getCancelado()) {
			consultarMonitoramentoAcademicoCancelado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getAbandonoCurso()) {
			consultarMonitoramentoAcademicoAbandonoCurso(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		if (filtroPainelGestorAcademico.getTransferenciaSaida()) {
			consultarMonitoramentoAcademicoTransferenciaSaida(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}

//		if (filtroPainelGestorAcademico.getNumeroAlunoTurno()) {
			consultarMonitoramentoAcademicoNumeroAlunoTurno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroAlunoSexo()) {
			consultarMonitoramentoAcademicoNumeroAlunoSexo(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroAlunoTurma()) {
			consultarMonitoramentoAcademicoNumeroAlunoTurma(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroTurmaTurno()) {
			consultarMonitoramentoAcademicoNumeroTurmaTurno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroTurma()) {
			consultarMonitoramentoAcademicoNumeroTurma(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroProfessor()) {
			consultarMonitoramentoAcademicoNumeroProfessor(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroProfessorTurno()) {
			consultarMonitoramentoAcademicoNumeroProfessorTurno(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getNumeroCursoMinistrado()) {
			consultarMonitoramentoAcademicoNumeroCursoMinistrado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getMonitoramentoImpressaoDeclaracao()) {
			consultarMonitoramentoAcademicoImpressaoDeclaracao(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
//		}
//		if (filtroPainelGestorAcademico.getMonitoramentoProcessoSeletivo()) {
			consultarMonitoramentoProcessoSeletivo(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INICIAL);
//		}
		if (filtroPainelGestorAcademico.getJubilado()) {
			consultarMonitoramentoAcademicoJubilado(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre);
		}
		executarCriacaoGraficoEvolucaoPorNivelEducacional(filtroPainelGestorAcademico, unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, periodicidadeCurso, null);
	}

	private void consultarMonitoramentoAcademicoNumeroCursoMinistrado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.curso) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}			
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Número de Cursos Ministrados", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_CURSO_MINISTRADO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroCursoMinistrado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT curso.codigo as cursoCodigo, curso.nome as cursoNome, curso.codigo as codigo, count(distinct matricula.matricula) as quantidade from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" group by curso.codigo, curso.nome ");
		sb.append(" order by curso.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroCursoMinistradoAluno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoCurso, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}			
		sb.append(" and curso.codigo = ").append(codigoCurso);
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNumeroProfessorTurno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder(" select count(distinct professor) AS qtde, turno.codigo AS codigoTurno, turno.nome AS nomeTurno from horarioturma ");
		sb.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadiaitem.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where extract(year from horarioturmadiaitem.data)::VARCHAR = '").append(ano).append("' and (case when extract(month from horarioturmadiaitem.data) > 7 then '2' else '1' end) = '").append(semestre).append("' ");
		} else {
			sb.append(" where extract(year from horarioturmadiaitem.data)::VARCHAR = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "turma.unidadeEnsino", "and"));
		sb.append(" and horarioturmadiaitem.professor is not null ");
		sb.append(" group by turno.codigo, turno.nome ");
		sb.append(" order by turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("nomeTurno"), tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_PROFESSOR_TURNO, tabelaResultado.getInt("codigoTurno"));
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroProfessorTurno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurno, String ano, String semestre, Integer limit, Integer offset) {

		StringBuilder sb = new StringBuilder(" select pessoa.codigo as professorcodigo, pessoa.nome as professornome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo,turno.codigo as turnoCodigo, turno.nome as turnoNome from horarioturma ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo  ");
		sb.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor  ");
		sb.append(" inner join turma on turma.codigo = horarioturma.turma");
		sb.append(" inner join turno on turno.codigo = turma.turno");
		sb.append(" where 1 = 1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "turma.unidadeEnsino", "and"));
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and turma.anual = false and turma.semestral = false and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadia.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		} else {
			sb.append(" and turma.anual and horarioturma.anovigente = '").append(ano).append("' ");
		}
		sb.append(" and turno.codigo = ").append(codigoTurno);
		sb.append(" group by pessoa.codigo, pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, pessoa.sexo, turno.codigo, turno.nome");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNumeroProfessor(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder(" select count(distinct professor) AS qtde from horarioturma ");
		sb.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where turma.anual = false and turma.semestral = false and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadiaitem.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");			
		} else {
			sb.append(" where turma.anual and horarioturma.anovigente = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "turma.unidadeEnsino", "and"));
		sb.append(" and horarioturmadiaitem.professor is not null  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Número de Professores", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_PROFESSOR, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroProfessor(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder(" select pessoa.codigo as professorCodigo, pessoa.nome as professorNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo from horarioturma ");		
		sb.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sb.append(" inner join pessoa on horarioturmadiaitem.professor = pessoa.codigo ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where turma.anual = false and turma.semestral = false and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadiaitem.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");			
		} else {
			sb.append(" where turma.anual and horarioturma.anovigente = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "turma.unidadeEnsino", "and"));		
		sb.append(" group by pessoa.codigo, pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, pessoa.sexo");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoNumeroTurma(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct turma.codigo) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join turma on matriculaPeriodo.turma = turma.codigo");
		sb.append(" where 1 = 1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Número de Turmas", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroTurma(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma, ");
		sb.append(" count(distinct matricula.matricula)  as quantidade, Turma.codigo as codigo ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by Turma.identificadorTurma ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNumeroTurmaTurno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct turma.codigo) as qtde, turno.codigo as codigoTurno, turno.nome as nomeTurno from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join turma on matriculaPeriodo.turma = turma.codigo");
		sb.append(" inner join turno on turno.codigo = matricula.turno");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by turno.codigo, turno.nome order by turno.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("nomeTurno"), tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA_TURNO, tabelaResultado.getInt("codigoTurno"));
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroTurmaTurno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurno, String ano, String semestre, Integer limit, Integer offset) {

		StringBuilder sb = new StringBuilder("SELECT unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma, ");
		sb.append(" count(distinct matricula.matricula)  as quantidade, Turma.codigo as codigo ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where 1 = 1");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}			
		sb.append(" and turno.codigo = ").append(codigoTurno);
		sb.append(" group by unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by Turma.identificadorTurma ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNumeroAlunoTurma(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde, turma.codigo as codigoTurma, turma.identificadorTurma from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join turma on matriculaPeriodo.turma = turma.codigo");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by turma.codigo, turma.identificadorTurma order by  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("identificadorTurma"), tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_TURMA, tabelaResultado.getInt("codigoTurma"));
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroAlunoTurma(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurma, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where 1 = 1");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and turma.codigo = ").append(codigoTurma);
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNumeroAlunoSexo(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde, case when trim(pessoa.sexo) = '' or pessoa.sexo is null then 'Não Informado' else case when pessoa.sexo = 'F' then 'Feminino' else 'Masculino' end end as sexo from matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" where 1 = 1");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" group by case when trim(pessoa.sexo) = '' or pessoa.sexo is null then 'Não Informado' else case when pessoa.sexo = 'F' then 'Feminino' else 'Masculino' end end");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Integer sexo = 0;
		while (tabelaResultado.next()) {
			if (tabelaResultado.getString("sexo").equals("Masculino")) {
				sexo = 1;
			} else if (tabelaResultado.getString("sexo").equals("Feminino")) {
				sexo = 2;
			} else {
				sexo = 0;
			}
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("sexo"), tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_SEXO, sexo);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroAlunoSexo(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset, Integer sexo) {
		StringBuilder sb = new StringBuilder("SELECT pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo ");
		sb.append(" from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}	
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" where 1 = 1");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (sexo == 0) {
			sb.append(" and (sexo = '' or sexo is null) ");
		} else if (sexo == 1) {
			sb.append(" and (sexo = 'M')");
		} else if (sexo == 2) {
			sb.append(" and (sexo = 'F')");
		}
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by pessoa.codigo, pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, pessoa.sexo ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNumeroAlunoTurno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde, turno.codigo as codigoTurno, turno.nome as nomeTurno from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}	
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join turno on turno.codigo = matricula.turno ");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}			
		sb.append(" group by turno.codigo, turno.nome order by turno.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("nomeTurno"), tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_TURNO, tabelaResultado.getInt("codigoTurno"));
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNumeroAlunoTurno(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurno, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");

		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where 1=1 ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}			
		sb.append(" and turno.codigo = ").append(codigoTurno);
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoTransferenciaSaida(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'TS' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" WHERE 1 = 1");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Transferência Outras Instituições", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_SAIDA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Transferência Outras Instituições", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_SAIDA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoTransferenciaSaida(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {

		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'TS' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
			sb.append(" order by ano||'/'||semestre desc limit 1)");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" WHERE 1 = 1");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoCanceladoPorTipoJustificativa(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoCancelamento("");
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde, MotivoCancelamentoTrancamento.nome as tipoJustificativa, MotivoCancelamentoTrancamento.codigo as codigo_MotivoCancelamentoTrancamento from matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'CA'  ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" left join Cancelamento on Cancelamento.matricula = matricula.matricula   and Cancelamento.situacao =  'FD' ");
		sb.append(" left join MotivoCancelamentoTrancamento on MotivoCancelamentoTrancamento.codigo = cancelamento.MotivoCancelamentoTrancamento ");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by MotivoCancelamentoTrancamento.nome,  MotivoCancelamentoTrancamento.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("tipoJustificativa") == null ? "Não Informado" : tabelaResultado.getString("tipoJustificativa"), tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CANCELAMENTO, tabelaResultado.getInt("codigo_MotivoCancelamentoTrancamento") == 0 ? -1 : tabelaResultado.getInt("codigo_MotivoCancelamentoTrancamento"));
		}
	}

    private void consultarMonitoramentoAcademicoPreMatriculaPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, boolean filtrarPorAnoSemestre, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso("");

		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (!filtrarPorAnoSemestre) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");
                
                SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
                    String formaIngressoStr = tabelaResultado.getString("formaingresso");
                    if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
                        formaIngressoStr = "Não Informado";
                        codigoFormaIngresso = 0;
                    } else {
                        formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
                        codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
                    }
		    adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULA_CALOURO, codigoFormaIngresso );
		}
    }

	private void consultarMonitoramentoAcademicoCalouroPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matricula.situacao = 'AT' ");
		sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp  where  mp.matricula = matricula.matricula  ");
		sb.append(" and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" and matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI')   ");
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
                    String formaIngressoStr = tabelaResultado.getString("formaingresso");
                    if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
                        formaIngressoStr = "Não Informado";
                        codigoFormaIngresso = 0;
                    } else {
                        formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
                        codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
                    }
		    adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CALOURO_FORMAINGRESSO, codigoFormaIngresso );
		}
	}
	
	private void consultarMonitoramentoAcademicoVeteranoPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoVeteranoPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp  ");
		sb.append(" where mp.matricula = matricula.matricula   and mp.situacaomatriculaperiodo not in ('PC', 'PR')   ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_VETERANO_FORMAINGRESSO, codigoFormaIngresso);
		}

	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadosTotalPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_FORMA_INGRESSO, codigoFormaIngresso);
		}

	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadosAdimplentePorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in ( select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 = (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_ADIMPLENTE_FORMA_INGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadosInadimplentePorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI','AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 < (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_INADIMPLENTE_FORMA_INGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_FORMAINGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoMatriculaPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoMatriculaPorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' and matricula.situacao = 'AT'  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_MATRICULA_FORMAINGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoCalouroPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
                
            

                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CALOURO_FORMAINGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoVeteranoPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		
		 
                
                sb.append(" and 0 < (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_VETERANO_FORMAINGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadoNaoPreMatriculadoPorFormaIngresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.formaIngresso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.formaIngresso ");
		sb.append(" order by matricula.formaIngresso ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int codigoFormaIngresso = 0;
		while (tabelaResultado.next()) {
			String formaIngressoStr = tabelaResultado.getString("formaingresso");
			if ((formaIngressoStr == null) || (formaIngressoStr.trim().equals(""))) {
				formaIngressoStr = "Não Informado";
				codigoFormaIngresso = 0;
			} else {
				formaIngressoStr = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getDescricao();
				codigoFormaIngresso = FormaIngresso.getEnum(tabelaResultado.getString("formaingresso")).getCodigoCenso();
			}
			adicionarResultado(filtroPainelGestorAcademico, formaIngressoStr, tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_NAO_PRE_MATRICULADO_FORMA_INGRESSO, codigoFormaIngresso);
		}
	}
	
	private void consultarMonitoramentoAcademicoCalouroPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matricula.situacao = 'AT' ");
		sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp  where  mp.matricula = matricula.matricula  ");
		sb.append(" and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" and matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI')   ");
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CALOURO_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoVeteranoPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp  ");
		sb.append(" where mp.matricula = matricula.matricula   and mp.situacaomatriculaperiodo not in ('PC', 'PR')   ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_VETERANO_CURSO, codigoCurso );
		}

	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadosTotalPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_CURSO, codigoCurso );
		}

	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadosAdimplentePorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in ( select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 = (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_ADIMPLENTE_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadosInadimplentePorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI','AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 < (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_INADIMPLENTE_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
	
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoMatriculaPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoMatriculaPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' and matricula.situacao = 'AT'  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_MATRICULA_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoCalouroPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
                
               
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CALOURO_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoNaoRenovadoNaoPreMatriculadoPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_NAO_PRE_MATRICULADO_CURSO, codigoCurso );
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoVeteranoPorCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso("");
		StringBuilder sb = new StringBuilder("SELECT matricula.curso, curso.nome as nomeCurso, count(matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		   
                sb.append(" and 0 < (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.curso, curso.nome ");
		sb.append(" order by qtde desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
                    Integer codigoCurso = tabelaResultado.getInt("curso");
                    String nomeCurso = tabelaResultado.getString("nomeCurso");
		    adicionarResultado(filtroPainelGestorAcademico, nomeCurso , tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_VETERANO_CURSO, codigoCurso );
		}
	}
        
	private void consultarMonitoramentoAcademicoCancelado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso");
		}
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'CA'  ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		// sb.append(" and (alunotransferidounidade = false or alunotransferidounidade is null) ");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Cancelamento", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CANCELADO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Cancelamento", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CANCELADO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoCancelado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer tipoJustificativa, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, Cancelamento.data as data, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma, Cancelamento.justificativa as informacaoAdicional, pfunc.nome as consultor  ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo = 'CA'  ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		// sb.append(" and (alunotransferidounidade = false or alunotransferidounidade is null) ");
		sb.append(" left join Cancelamento on Cancelamento.matricula = matricula.matricula  and Cancelamento.situacao =  'FD' ");
		sb.append(" left join motivoCancelamentoTrancamento on motivoCancelamentoTrancamento.codigo = cancelamento.motivoCancelamentoTrancamento ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa pfunc on pfunc.codigo = funcionario.pessoa ");
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		if (tipoJustificativa != null) {
			if (tipoJustificativa < 0) {
				sb.append(" and Cancelamento.MotivoCancelamentoTrancamento is null");
			} else {
				sb.append(" and MotivoCancelamentoTrancamento.codigo = ").append(tipoJustificativa).append(" ");
			}
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, Cancelamento.data , ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma, Cancelamento.justificativa, pfunc.nome ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoTrancado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'TR' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		// sb.append(" and (alunotransferidounidade = false or alunotransferidounidade is null) ");
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Trancamento", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANCADO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Trancamento", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANCADO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoTrancado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'TR' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoAbandonoCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AC' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Abandono de Curso", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.ABANDONO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Abandono de Curso", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.ABANDONO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoAbandonoCurso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AC' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoFormado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'FO' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Formado", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FORMADO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Formado", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FORMADO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoFormado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'FO' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoRematriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join matriculaperiodo mpant on mpant.matricula = matricula.matricula ");
		sb.append(" and mpant.codigo in (select mp.codigo from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC', 'PR') and (mp.ano||'/'||mp.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) order by (mp.ano||'/'||mp.semestre) desc limit 1 ) ");

		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI') ");
		sb.append(" and mpant.situacaomatriculaperiodo not in ('TR', 'CA', 'TS', 'TI', 'AC', 'PC', 'PR') ");

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Renovações", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REMATRICULADO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Renovações", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REMATRICULADO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoRematriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" inner join matriculaperiodo mpant on mpant.matricula = matricula.matricula ");
		sb.append(" and mpant.codigo in (select mp.codigo from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC', 'PR') and (mp.ano||'/'||mp.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) order by (mp.ano||'/'||mp.semestre) desc limit 1 ) ");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI') ");
		sb.append(" and mpant.situacaomatriculaperiodo not in ('TR', 'CA', 'TS', 'TI', 'AC', 'PC', 'PR') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoNaoRematriculadoInadimplente(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where  mp.situacaomatriculaperiodo not in ('PC', 'PR') and  mp.matricula = matricula.matricula and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 < (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados -> Inadimplente", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_INADIMPLENTE, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados -> Inadimplente", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_INADIMPLENTE, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNaoRematriculadoInadimplente(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semest re = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (codigoFormaIngresso != null) {
            if (codigoFormaIngresso.equals(0)) {
                sb.append(" and TRIM(matricula.formaingresso) = ''");
            } else {
                sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
            } 
        }
		if (codigoCurso != null && !codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI','AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 < (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoNaoRematriculadoAdimplente(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 = (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados -> Adimplente", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_ADIMPLENTE, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados -> Adimplente", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_ADIMPLENTE, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNaoRematriculadoAdimplente(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {

		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (codigoFormaIngresso != null) {
            if (codigoFormaIngresso.equals(0)) {
                sb.append(" and TRIM(matricula.formaingresso) = ''");
            } else {
                sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
            } 
        }
		if (codigoCurso != null && !codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in ( select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(" and 0 = (select count(codigo) from contareceber where situacao = 'AR' and dataVencimento < current_date and contareceber.matriculaaluno = matricula.matricula )");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoNaoRematriculadoTotal(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {

		StringBuilder sb = new StringBuilder(" SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados Total: "+tabelaResultado.getInt("qtde"), 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_TOTAL, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados Total: "+0, 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_TOTAL, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoNaoRematriculadoTotal(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append("  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		
		if (codigoFormaIngresso != null) {
            if (codigoFormaIngresso.equals(0)) {
                sb.append(" and TRIM(matricula.formaingresso) = ''");
            } else {
                sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
            } 
        }
		if (codigoCurso != null && !codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}
	
	private void consultarMonitoramentoAcademicoNaoRematriculadoNaoPreMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {

		StringBuilder sb = new StringBuilder(" SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));

		// Possiveis Formandos não entra no resultado
		sb.append(" and matricula.matricula not in ( ");
		sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sb.append(" ) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados - Não Pré-Matriculados: ", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_NAO_PRE_MATRICULADO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Não Renovados - Não Pré-Matriculados: "+0, 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_NAO_PRE_MATRICULADO, 0);
		}
	}

	@Override
	public String getSqlPossiveisFormandos(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, String tipoNivelEducacional) {
		return "'0'";
//		StringBuilder sqlStr = new StringBuilder();
//		sqlStr.append(" select distinct matricula.matricula ");
//		sqlStr.append(" from matricula  ");
//		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");  		
//		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  ");
//		if (!filtrarPorAnoSemestre) {
//			sqlStr.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
//			sqlStr.append(" and matricula.situacao = 'AT'  and mp.situacaomatriculaperiodo not in ('PC', 'PC')  ");
//			sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
//		} else {
//			sqlStr.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC')  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
//		}
//		sqlStr.append(" where 0 = (select count(codigo) from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula   ");
//		sqlStr.append(" and mp.situacaomatriculaperiodo not in ('PC')  and mp.codigo != matriculaperiodo.codigo ");
//		sqlStr.append(" and (mp.ano||'/'||mp.semestre) > (matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) limit 1) ");
//		sqlStr.append(" and matricula.situacao = 'AT'  ");
//		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI') ");
//		sqlStr.append(" and (select count(distinct disciplina.codigo) from historico ");
//		sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
//		sqlStr.append(" where disciplina.tipoDisciplina in ('OB', 'LG')  ");
//		sqlStr.append(" and historico.matricula = matricula.matricula ");
//		sqlStr.append(" and historico.situacao in ('AA', 'AP', 'CS', 'IS', 'CC', 'CH') )  ");
//		sqlStr.append(" >= ( ");
//		sqlStr.append(" select count(distinct disciplina.codigo) from  matriculaperiodo mp2 ");
//		sqlStr.append(" inner join gradecurricular on mp2.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append(" INNER JOIN periodoletivo AS pr ON gradecurricular.codigo = pr.gradecurricular ");
//		sqlStr.append(" INNER join gradedisciplina AS grDisc on grDisc.periodoletivo = pr.codigo  ");
//		sqlStr.append(" INNER join disciplina on grDisc.disciplina = disciplina.codigo  ");
//		sqlStr.append(" where mp2.codigo = matriculaperiodo.codigo ");
//		sqlStr.append(" and disciplina.tipoDisciplina in ('OB', 'LG') ");
//		sqlStr.append(") ");
//		sqlStr.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
//		if (!tipoNivelEducacional.equals("")) {
//			sqlStr.append(" and curso.niveleducacional = '").append(tipoNivelEducacional).append("'");
//		}
//		return sqlStr.toString();
	}

	private void consultarMonitoramentoAcademicoPossivelFormando(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count(distinct matricula) as qtde ");
		sqlStr.append(" from ( ");
		sqlStr.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		sqlStr.append(") as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Possíveis Formandos", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POSSIVEL_FORMANDO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Possíveis Formandos", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.POSSIVEL_FORMANDO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoPossivelFormando(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {

		StringBuilder sqlStr = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sqlStr.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sqlStr.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sqlStr.append(" from matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
		sqlStr.append(" and mp.situacaomatriculaperiodo not in ('PC', 'PC')  order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" where matricula.matricula in (");
		sqlStr.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sqlStr.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sqlStr.append(" ) ");

		sqlStr.append(" group by matricula.matricula, pessoa.email, pessoa.nome, pessoa.telefoneres, pessoa.codigo, pessoa.celular, ");
		sqlStr.append(" pessoa.telefonerecado, pessoa.telefonecomer, curso.nome, unidadeensino.nome, unidadeensino.codigo, unidadeensino.codigo, curso.codigo, turno.codigo, turma.codigo, turma.identificadorturma ");

		sqlStr.append("");
		if (limit != null && limit > 0) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);

		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	/*
	 * Neste desconsidera os retornos de evasao
	 */
	private void consultarMonitoramentoAcademicoVeterano(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC')  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC')  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp  ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')    ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Veterano", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.VETERANO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Veterano", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.VETERANO, 0);
		}
	}

	/**
	 * Neste desconsidera os retornos de evasao
	 * 
	 * @param filtroPainelGestorAcademico
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @param filtrarPorAnoSemestre
	 * @param ano
	 * @param semestre
	 * @param limit
	 * @param offset
	 */
	private void consultarDetalheMonitoramentoAcademicoVeterano(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append("  and matriculaPeriodo.situacaomatriculaperiodo not in ('PC', 'PR')  and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp  ");
		sb.append(" where mp.matricula = matricula.matricula   and mp.situacaomatriculaperiodo not in ('PC', 'PR')   ");
		
		if (codigoFormaIngresso != null) {
            if (codigoFormaIngresso.equals(0)) {
                sb.append(" and TRIM(matricula.formaingresso) = ''");
            } else {
                sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
            } 
        }
		if (codigoCurso != null && !codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoTransferenciaExterna(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false)).append(" ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" left join TransferenciaEntrada on matriculaPeriodo.TransferenciaEntrada = TransferenciaEntrada.codigo and TransferenciaEntrada.tipoTransferenciaEntrada =	'").append(TipoTransferenciaEntrada.EXTERNA.getValor()).append("' ");
		sb.append(" where ");
		sb.append(" (matricula.formaingresso = 'TE' or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('EX') and matricula is not null and transferenciaentrada.situacao = 'EF')) ");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp ");
		sb.append("          where  mp.matricula = matricula.matricula ");   
		sb.append("           and (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) "); 
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
                
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Transferência Entrada", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_EXTERNA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Transferência Entrada", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_EXTERNA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoTransferenciaExterna(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false)).append(" ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(" (matricula.formaingresso = 'TE' or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('EX') and matricula is not null and transferenciaentrada.situacao = 'EF')) ");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp ");
		sb.append("          where  mp.matricula = matricula.matricula ");   
		sb.append("           and (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) "); 
                
                sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoTransferenciaInterna(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" where (matricula.formaingresso in ('TI') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN') and matricula is not null and transferenciaentrada.situacao = 'EF'))");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp ");
		sb.append("          where  mp.matricula = matricula.matricula ");   
		sb.append("           and (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) "); 
                
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Transferência Interna", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_INTERNA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Transferência Interna", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_INTERNA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoTransferenciaInterna(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where (matricula.formaingresso in ('TI') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN') and matricula is not null and transferenciaentrada.situacao = 'EF'))");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp ");
		sb.append("          where  mp.matricula = matricula.matricula ");   
		sb.append("           and (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) "); 

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoPortadorDiploma(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" left join PortadorDiploma on PortadorDiploma.Matricula = Matricula.matricula ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");

                sb.append(" and matricula.formaingresso in ('PD') ");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp ");
		sb.append("          where  mp.matricula = matricula.matricula ");   
		sb.append("           and (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) "); 
                       
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Portador de Diploma", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PORTADOR_DIPLOMA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Portador de Diploma", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PORTADOR_DIPLOMA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoPortadorDiploma(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" left join PortadorDiploma on PortadorDiploma.Matricula = Matricula.matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");

                sb.append(" and matricula.formaingresso in ('PD') ");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp ");
		sb.append("          where  mp.matricula = matricula.matricula ");   
		sb.append("           and (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) "); 
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	/**
	 * É considerado REINGRESSO o aluno que trancou, cancelou, abandonou ou
	 * transferiu para outra instituição, e o mesmo deve estar matriculado em um
	 * novo curso com a situação ativo ou finalizado. e deve ser a sua 1ª
	 * matricula periodo neste novo curso
	 * 
	 * @param filtroPainelGestorAcademico
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @param filtrarPorAnoSemestre
	 * @param ano
	 * @param semestre
	 */
	private void consultarMonitoramentoAcademicoReingresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula  ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select m.codigo from matriculaPeriodo as m ");
			sb.append(" where m.matricula = matricula.matricula and m.situacaomatriculaperiodo not in ('PR', 'PC')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "m.data", false));
			sb.append(" order by m.ano||'/'||m.semestre desc limit 1) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and 0 = (select count(codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.ano||'/'||mp.semestre < matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) ");
		sb.append(" and 0 < (select count(m.matricula) from matricula m where m.aluno = matricula.aluno ");
		sb.append(" and m.matricula != matricula.matricula and m.curso != matricula.curso and m.situacao in ('FO', 'TR', 'CA', 'TS', 'AC')) ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false));
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Reingresso", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REINGRESSO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Reingresso", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REINGRESSO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoReingresso(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select m.codigo from matriculaPeriodo as m ");
			sb.append(" where m.matricula = matricula.matricula  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "m.data", false));
			sb.append(" order by m.ano||'/'||m.semestre desc limit 1) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and 0 = (select count(codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.ano||'/'||mp.semestre < matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) ");
		sb.append(" and 0 < (select count(m.matricula) from matricula m where m.aluno = matricula.aluno ");
		sb.append(" and m.matricula != matricula.matricula and m.curso != matricula.curso and m.situacao in ('FO', 'TR', 'CA', 'TS', 'AC')) ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	private void consultarMonitoramentoAcademicoReabertura(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on  curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo not in ('PR', 'PC')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula and mpt.situacaoMatriculaPeriodo in ('TR', 'CA', 'TS', 'AC') ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp  ");
		sb.append(" where mp.matricula = matricula.matricula    ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) and mp.situacaoMatriculaPeriodo not in ('PR', 'PC') order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Reabertura", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REABERTURA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Reabertura", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REABERTURA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoReabertura(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo not in ('PR', 'PC')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula and mpt.situacaoMatriculaPeriodo in ('TR', 'CA', 'TS', 'AC') ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp  ");
		sb.append(" where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo not in ('PR', 'PC')   ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) order by mp.ano||'/'||mp.semestre desc limit 1) ");

		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoCalouro(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') and matriculaperiodo.situacao in ('CO', 'AT') ");
//		sb.append(" and formaingresso not in ('TE', 'TI') ");
		sb.append(" and (matricula.formaingresso not in ('TE', 'TI') and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF'))");
		sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp  where  mp.matricula = matricula.matricula  ");
		sb.append(" and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Calouros", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CALOURO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Matriculas ->  Calouros", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CALOURO, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoCalouro(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matricula.situacao = 'AT' ");
		if (codigoFormaIngresso != null) {
			if (codigoFormaIngresso.equals(0)) {
				sb.append(" and TRIM(matricula.formaingresso) = ''");
			} else {
				sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
			}
		}
		if (codigoCurso != null && !codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		sb.append(" and (matricula.formaingresso not in ('TE', 'TI') and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF'))");
		sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp  where  mp.matricula = matricula.matricula  ");
		sb.append(" and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI')   ");
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' and matricula.situacao = 'AT'  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') and matriculaperiodo.situacao in ('CO', 'AT') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Matrículas", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MATRICULA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Matrículas", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MATRICULA, 0);
		}
	}

	private void consultarDetalheMonitoramentoAcademicoMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone,  pessoa.sexo as sexo, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' and matricula.situacao = 'AT'  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaPeriodo.data", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI') ");
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		
		if (codigoFormaIngresso != null) {
            if (codigoFormaIngresso.equals(0)) {
                sb.append(" and TRIM(matricula.formaingresso) = ''");
            } else {
                sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
            } 
        }
		
		if (codigoCurso != null && !codigoCurso.equals(0)) {
            sb.append(" and matricula.curso = ").append(codigoCurso);
        }
		
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma, pessoa.sexo ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}

	private void consultarMonitoramentoAcademicoPreMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Pré-Matrículas - "+tabelaResultado.getInt("qtde"), 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Pré-Matrículas", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA, 0);
		}
	}
	
	private void consultarMonitoramentoAcademicoPreMatriculadoCalouro(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Pré-Matrículas -> Calouros", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_CALOURO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Pré-Matrículas -> Calouros", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_CALOURO, 0);
		}
	}
        
	private void consultarMonitoramentoAcademicoPreMatriculadoVeterano(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
                sb.append(" and 0 < (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Pré-Matrículas -> Veteranos", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Pré-Matrículas -> Veteranos", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_VETERANO, 0);
		}
	}        

	private void consultarDetalheMonitoramentoAcademicoPreMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		if (codigoFormaIngresso != null) {
            if (codigoFormaIngresso.equals(0)) {
                sb.append(" and TRIM(matricula.formaingresso) = ''");
            } else {
                sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
            } 
        }
		if (codigoCurso != null && !codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma, pessoa.sexo ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}
	
	private void consultarDetalheMonitoramentoAcademicoPreMatriculadoCalouro(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso, Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
                
                if (codigoFormaIngresso != null) {
                    if (codigoFormaIngresso.equals(0)) {
                        sb.append(" and TRIM(matricula.formaingresso) = ''");
                    } else {
                        sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
                    } 
                }
                
                if (codigoCurso != null && !codigoCurso.equals(0)) {
                	sb.append(" and matricula.curso = ").append(codigoCurso);
                }

                sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma, pessoa.sexo ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}
        
	private void consultarDetalheMonitoramentoAcademicoPreMatriculadoVeterano(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso,  Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
                
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		
		 if (codigoFormaIngresso != null) {
             if (codigoFormaIngresso.equals(0)) {
                 sb.append(" and TRIM(matricula.formaingresso) = ''");
             } else {
                 sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
             } 
         }
		 if (codigoCurso != null && !codigoCurso.equals(0)) {
			 sb.append(" and matricula.curso = ").append(codigoCurso);
		 }
                
                sb.append(" and 0 < (select count(codigo) from matriculaPeriodo mp "); 
		sb.append("      where  mp.matricula = matricula.matricula ");  
		sb.append("        and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma, pessoa.sexo ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}
	
	
	private void consultarDetalheMonitoramentoAcademicoNaoRenovadoNaoPreMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer codigoFormaIngresso,  Integer codigoCurso, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestre = "2";
			} else {
				semestre = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
		
		 if (codigoFormaIngresso != null) {
             if (codigoFormaIngresso.equals(0)) {
                 sb.append(" and TRIM(matricula.formaingresso) = ''");
             } else {
                 sb.append(" and matricula.formaingresso = '").append(FormaIngresso.getEnum(codigoFormaIngresso).getValor()).append("' ");
             } 
         }
		 if (codigoCurso != null && !codigoCurso.equals(0)) {
			 sb.append(" and matricula.curso = ").append(codigoCurso);
		 }
                
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, Turma.codigo, Turma.identificadorTurma, pessoa.sexo ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}
	}
	
	
//	StringBuilder sb = new StringBuilder(" SELECT count(distinct matricula.matricula) as qtde ");
//	sb.append(" from matricula ");
//	sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
//	if (!filtrarPorAnoSemestre) {
//		sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
//		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
//		sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
//	} else {
//		if (semestre.equals("1")) {
//			ano = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
//			semestre = "2";
//		} else {
//			semestre = "1";
//		}
//		sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
//	}
//	sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
//	sb.append(" inner join Curso on Curso.codigo = matricula.curso");
//	sb.append(" where curso.nivelEducacional not in ('PO', 'EX') ");
//	if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
//		sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
//	}
//	sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
//	sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
//	sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC')  and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ) ");
//	sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
//
//	// Possiveis Formandos não entra no resultado
//	sb.append(" and matricula.matricula not in ( ");
//	sb.append(getSqlPossiveisFormandos(unidadeEnsinoVOs, dataInicio, dataTermino, filtrarPorAnoSemestre, ano, semestre, filtroPainelGestorAcademico.getTipoNivelEducacional()));
//	sb.append(" ) ");

	private void adicionarResultadoDetalhado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, SqlRowSet rs) {
		PainelGestorMonitoramentoAcademicoVO obj = new PainelGestorMonitoramentoAcademicoVO();

		if (filtroPainelGestorAcademico.getOpcaoAtual().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA)) {
			obj.setOpcao(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NUMERO_TURMA_ALUNO);
		} else if (filtroPainelGestorAcademico.getOpcaoAtual().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA_TURNO)) {
			obj.setOpcao(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NUMERO_TURMA_TURNO_ALUNO);
		} else if (filtroPainelGestorAcademico.getOpcaoAtual().equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_CURSO_MINISTRADO)) {
			obj.setOpcao(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CURSO_MINISTRADO_ALUNO);
		}

		List<String> colunas = Arrays.asList(rs.getMetaData().getColumnNames());
		if (colunas.contains("pessoacodigo")) {
			obj.setCodigoAluno(rs.getInt("pessoaCodigo"));
		}

		if (colunas.contains("codigo")) {
			obj.setCodigo(rs.getInt("codigo"));
		}
		if (colunas.contains("quantidade")) {
			obj.setQuantidade(rs.getInt("quantidade"));
		}

		if (colunas.contains("sexo")) {
			if (rs.getString("sexo") != null && rs.getString("sexo").trim().equals("M")) {
				obj.setSexo("Masculino");
			} else if (rs.getString("sexo") != null && rs.getString("sexo").trim().equals("F")) {
				obj.setSexo("Feminino");
			} else {
				obj.setSexo("Não Informado");
			}
		}
		if (colunas.contains("pessoanome")) {
			obj.setNomeAluno(rs.getString("pessoanome"));
		}
		if (colunas.contains("telefone")) {
			obj.setTelefone(rs.getString("telefone"));
		}
		if (colunas.contains("email")) {
			obj.setEmail(rs.getString("email"));
		}
		if (colunas.contains("matricula")) {
			obj.setMatricula(rs.getString("matricula"));
		}
		if (colunas.contains("cursocodigo")) {
			obj.setCodigoCurso(rs.getInt("cursocodigo"));
		}
		if (colunas.contains("cursonome")) {
			obj.setNomeCurso(rs.getString("cursonome"));
		}
		if (colunas.contains("turnocodigo")) {
			obj.setCodigoTurno(rs.getInt("turnocodigo"));
		}
		if (colunas.contains("turnonome")) {
			obj.setNomeTurno(rs.getString("turnonome"));
		}
		if (colunas.contains("unidadeensinocodigo")) {
			obj.setCodigoUnidadeEnsino(rs.getInt("unidadeensinocodigo"));
		}
		if (colunas.contains("unidadeensinonome")) {
			obj.setNomeUnidadeEnsino(rs.getString("unidadeensinonome"));
		}
		if (colunas.contains("turmacodigo")) {
			obj.setCodigoTurma(rs.getInt("turmacodigo"));
		}
		if (colunas.contains("identificadorturma")) {
			obj.setNomeTurma(rs.getString("identificadorturma"));
		}
		if (colunas.contains("professorcodigo")) {
			obj.setCodigoProfessor(rs.getInt("professorcodigo"));
		}
		if (colunas.contains("professornome")) {
			obj.setNomeProfessor(rs.getString("professornome"));
		}
		if (colunas.contains("informacaoadicional")) {
			obj.setInformacaoAdicional(rs.getString("informacaoadicional"));
		}
		if (colunas.contains("consultor")) {
			obj.setConsultor(rs.getString("consultor"));
		}
		if (colunas.contains("data")) {
			obj.setData(rs.getDate("data"));
		}
		if (colunas.contains("textopadraodeclaracao_descricao")) {
			obj.setTextoPadraoDeclaracao_Descricao(rs.getString("textoPadraoDeclaracao_Descricao"));
		}
		filtroPainelGestorAcademico.getPainelGestorDetalheMonitoramentoAcademicoVOs().add(obj);
	}

	private void adicionarResultado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, String titulo, Integer quantidade, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao, Integer codigo) {

		StringBuilder resultado = new StringBuilder("");
		resultado.append("{");
		resultado.append("name: '").append(titulo).append("', y:").append(quantidade).append(", campo: '").append(opcao.toString()).append("', id: ").append(codigo);
		resultado.append("}");
		if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_NAO_PRE_MATRICULADO_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_NAO_PRE_MATRICULADO_FORMA_INGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CANCELAMENTO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoCancelamento().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoCancelamento(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoCancelamento() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoCancelamento(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoCancelamento() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CALOURO_FORMAINGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoCalouroPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoCalouroPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoCalouroPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CALOURO_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoCalouroPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoCalouroPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoCalouroPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoCalouroPorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_VETERANO_FORMAINGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoVeteranoPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoVeteranoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoVeteranoPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoVeteranoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoVeteranoPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_VETERANO_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoVeteranoPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoVeteranoPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoVeteranoPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoVeteranoPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoVeteranoPorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_MATRICULA_FORMAINGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoMatriculaPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoMatriculaPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoMatriculaPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoMatriculaPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoMatriculaPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_MATRICULA_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoMatriculaPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoMatriculaPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoMatriculaPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoMatriculaPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoMatriculaPorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_ADIMPLENTE_FORMA_INGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_ADIMPLENTE_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_INADIMPLENTE_FORMA_INGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_INADIMPLENTE_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_FORMAINGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorCurso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CALOURO_FORMAINGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULADO_CALOURO_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_PRE_MATRICULA_CALOURO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_PROFESSOR) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_CURSO_MINISTRADO)) {
			filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_PROFESSOR_TURNO)) {
			
				if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno().isEmpty()) {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno() + ", " + resultado.toString());
				} else {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno() + resultado.toString());
				}
			
				filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoProfessorTurnoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
			

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_FORMA_INGRESSO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_NAO_REMATRICULADO_TOTAL_CURSO)) {
			resultado = new StringBuilder("");
			resultado.append("{name: '").append(titulo + " (" + quantidade + ")").append("', data: [").append(quantidade).append("], campo: '").append(opcao.toString()).append("', id: '").append(codigo).append("', quantidade: '").append(quantidade).append("'}");
			if (!filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso(filtroPainelGestorAcademico.getResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso() + resultado.toString());
			}

		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_TURMA_TURNO)) {
			
				if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno().isEmpty()) {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno() + ", " + resultado.toString());
				} else {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno() + resultado.toString());
				}
			
				filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoTurmaTurnoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
			
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.IMPRESSAO_DECLARACAO)) {
			
				if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao().isEmpty()) {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao() + ", " + resultado.toString());
				} else {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao() + resultado.toString());
				}
			
				filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
			
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_TURNO)) {
			
				if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno().isEmpty()) {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno() + ", " + resultado.toString());
				} else {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno() + resultado.toString());
				}
			
				filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoAlunoTurnoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
			
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_TURMA)) {
			filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoAlunoTurmaVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
			// if
			// (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma().isEmpty())
			// {
			// filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma()
			// + ", " + resultado.toString());
			// }else{
			// filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma()
			// + resultado.toString());
			// }
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NUMERO_ALUNO_SEXO)) {
			
				if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoSexo().isEmpty()) {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoSexo(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoSexo() + ", " + resultado.toString());
				} else {
					filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoSexo(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoSexo() + resultado.toString());
				}
			
				filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoAlunoSexoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
			
		} else  {
			realizarDefinicaoGraficoSituacaoAcademica(filtroPainelGestorAcademico, opcao, resultado);
			filtroPainelGestorAcademico.getPainelGestorMonitoramentoAcademicoSituacaoVOs().add(new PainelGestorMonitoramentoAcademicoVO(opcao, titulo, quantidade, codigo));
		}
	}
	
	public void realizarDefinicaoGraficoSituacaoAcademica(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao, StringBuilder resultado) {
		if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.MATRICULA) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CALOURO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.VETERANO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_INTERNA)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_EXTERNA)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PORTADOR_DIPLOMA) 
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REMATRICULADO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REABERTURA)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.REINGRESSO)
				 
				) {
			
			if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademico().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademico(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademico() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademico(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademico() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.FORMADO)  
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANCADO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CANCELADO) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.ABANDONO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.TRANSFERENCIA_SAIDA)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.JUBILADO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida() + resultado.toString());
			}
		} else if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_TOTAL) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_ADIMPLENTE)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_INADIMPLENTE) 
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.NAO_REMATRICULADO_NAO_PRE_MATRICULADO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA) || opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_CALOURO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA_VETERANO)) {
			if (!filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado().isEmpty()) {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado() + ", " + resultado.toString());
			} else {
				filtroPainelGestorAcademico.setResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado(filtroPainelGestorAcademico.getResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado() + resultado.toString());
			}
			
		}
		
		
	}

	private String getFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String campo, String andOr) {
		StringBuilder sb = new StringBuilder("");
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sb.append(" ").append(andOr).append(" ").append(campo).append(" in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sb.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sb.append(") ");
		}
		return sb.toString();
	}

	private void consultarMonitoramentoAcademicoImpressaoDeclaracao(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT textopadraodeclaracao.descricao as titulo, textopadraodeclaracao.codigo as codigo, count(impressaodeclaracao.codigo) as quantidade from impressaodeclaracao ");
		sb.append(" inner join matricula on matricula.matricula = impressaodeclaracao.matricula");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
		}	
		sb.append(" inner join textopadraodeclaracao on textopadraodeclaracao.codigo = impressaodeclaracao.textopadraodeclaracao");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datageracao", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where extract(year from datageracao)::VARCHAR =  '").append(ano).append("' and (case when extract(month from datageracao) > 7 then '2' else '1' end) = '").append(semestre).append("' ");
		} else {
			sb.append(" where extract(year from datageracao)::VARCHAR =  '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		sb.append(" group by textopadraodeclaracao.codigo , textopadraodeclaracao.descricao ");
		sb.append(" order by textopadraodeclaracao.descricao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, tabelaResultado.getString("titulo"), tabelaResultado.getInt("quantidade"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.IMPRESSAO_DECLARACAO, tabelaResultado.getInt("codigo"));
		}
	}

	private void consultarDetalheMonitoramentoAcademicoImpressaoDeclaracao(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoImpressaodeclaracao, String ano, String semestre, Integer limit, Integer offset) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo,");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, ");
		sb.append(" textopadraodeclaracao.descricao as textopadraodeclaracao_descricao, count(impressaodeclaracao.codigo) as quantidade from impressaodeclaracao ");
		sb.append(" inner join matricula on matricula.matricula = impressaodeclaracao.matricula");
		sb.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turno on matricula.turno = turno.codigo");
		sb.append(" inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo");
		sb.append(" inner join textopadraodeclaracao on textopadraodeclaracao.codigo = impressaodeclaracao.textopadraodeclaracao");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datageracao", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" where extract(year from datageracao)::VARCHAR =  '").append(ano).append("' and (case when extract(month from datageracao) > 7 then '2' else '1' end) = '").append(semestre).append("' ");
		} else {
			sb.append(" where extract(year from datageracao)::VARCHAR =  '").append(ano).append("' ");
		}

		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		sb.append(" and textopadraodeclaracao.codigo = ").append(codigoImpressaodeclaracao);
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, pessoa.sexo, textopadraodeclaracao.descricao ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}

	@Override
	public void consultarMonitoramentoProcessoSeletivo(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(inscricao.codigo) as qtdeInscritoGeral, ");
		sql.append(" sum(case when inscricao.situacao = 'CO' then 1 else 0 end) as qtdeInscritoConfirmado, ");
		sql.append(" sum(case when inscricao.situacao != 'CO' then 1 else 0 end) as qtdeInscritoNaoConfirmado, ");
		sql.append(" sum(case when matricula.matricula is not null and matriculaperiodo.situacaomatriculaperiodo = 'PR' then 1 else 0 end) as qtdeInscritoPreMatriculado, ");
		sql.append(" sum(case when matricula.matricula is not null and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') then 1 else 0 end) as qtdeInscritoMatriculado, ");
		sql.append(" sum(case when matricula.matricula is null and resultadoprocessoseletivo.codigo is not null and (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'AP'  ");
		sql.append(" or (resultadoprocessoseletivo.resultadoSegundaOpcao = 'AP' and procseletivo.nropcoescurso::INT > 1)  ");
		sql.append(" or (resultadoprocessoseletivo.resultadoTerceiraOpcao = 'AP' and procseletivo.nropcoescurso::INT > 2)) then 1 else 0 end) as qtdeInscritoAprovadoNaoMatriculado, ");
		sql.append(" sum(case when resultadoprocessoseletivo.codigo is null and inscricao.situacaoinscricao = 'ATIVO' and inscricao.situacao = 'CO' then 1 else 0 end) as qtdeInscritoSemResultado, ");
		sql.append(" sum(case when resultadoprocessoseletivo.codigo is null and inscricao.situacaoinscricao = 'NAO_COMPARECEU' and inscricao.situacao = 'CO' then 1 else 0 end) as qtdeInscritoNaoCompareceu, ");
		sql.append(" sum(case when resultadoprocessoseletivo.codigo is not null and (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'AP'  ");
		sql.append(" or (resultadoprocessoseletivo.resultadoSegundaOpcao = 'AP' and procseletivo.nropcoescurso::INT > 1)  ");
		sql.append(" or (resultadoprocessoseletivo.resultadoTerceiraOpcao = 'AP' and procseletivo.nropcoescurso::INT > 2)) then 1 else 0 end) as qtdeInscritoAprovado, ");
		sql.append(" sum(case when resultadoprocessoseletivo.codigo is not null and ((resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and procseletivo.nropcoescurso::INT = 1) ");
		sql.append(" or (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and resultadoprocessoseletivo.resultadoSegundaOpcao = 'RE' and procseletivo.nropcoescurso::INT = 2)  ");
		sql.append(" or (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and resultadoprocessoseletivo.resultadoSegundaOpcao = 'RE' and resultadoprocessoseletivo.resultadoTerceiraOpcao = 'RE'  ");
		sql.append(" and procseletivo.nropcoescurso::INT = 3))  then 1 else 0 end) as qtdeInscritoReprovado ");
		if (opcao != null && opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO)) {
			sql.append(", unidadeensinocurso.codigo as unidadeensinocursoCodigo, unidadeensino.nome as unidadeensinoNome, unidadeensino.codigo as unidadeensinoCodigo,  ");
			sql.append(" curso.codigo as cursoCodigo, curso.nome as cursoNome,  ");
			sql.append(" turno.codigo as turnoCodigo, turno.nome as turnoNome ");
		}
		sql.append(" from procseletivo   ");
		sql.append(" inner join inscricao on inscricao.procseletivo = procseletivo.codigo ");
		sql.append(" left join matricula on inscricao.codigo = matricula.inscricao ");
		
        sql.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sql.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('CA', 'TR', 'PC', 'AB') order by mp.ano||'/'||mp.semestre limit 1 ) ");
		
		sql.append(" left join resultadoprocessoseletivo on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		if (opcao != null && opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO)) {
			
			sql.append(" inner join unidadeensinocurso on inscricao.cursoopcao1 = unidadeensinocurso.codigo ");
			sql.append(" inner join unidadeensino on inscricao.unidadeensino = unidadeensino.codigo ");
			sql.append(" inner join curso on unidadeensinocurso.curso = curso.codigo ");
			sql.append(" inner join turno on unidadeensinocurso.turno = turno.codigo ");
		}
		sql.append(" where 1 = 1 and situacaoInscricao <> 'CANCELADO_OUTRA_INSCRICAO' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sql.append(" and procseletivo.ano = '" + ano + "' and procseletivo.semestre = '" + semestre + "' ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "inscricao.data", false));
		} else {
			sql.append(" and procseletivo.ano = '" + ano + "' ");
		}
		sql.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "inscricao.unidadeEnsino", "and"));
		switch (opcao) {
		case PS_INSCRITO_APROVADO:
			sql.append(" and resultadoprocessoseletivo.codigo is not null and (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'AP' ");
			sql.append(" or (resultadoprocessoseletivo.resultadoSegundaOpcao = 'AP' and procseletivo.nropcoescurso::INT > 1) ");
			sql.append(" or (resultadoprocessoseletivo.resultadoTerceiraOpcao = 'AP' and procseletivo.nropcoescurso::INT > 2)) ");
			break;
		case PS_INSCRITO_APROVADO_NAO_MATRICULADO:
			sql.append(" and case when matricula.matricula is null and resultadoprocessoseletivo.codigo is not null and (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'AP' "); 
			sql.append(" or (resultadoprocessoseletivo.resultadoSegundaOpcao = 'AP' and procseletivo.nropcoescurso::INT > 1) "); 
			sql.append(" or (resultadoprocessoseletivo.resultadoTerceiraOpcao = 'AP' and procseletivo.nropcoescurso::INT > 2)) ");
			break;
		case PS_INSCRITO_CONFIRMADO:
			sql.append(" and inscricao.situacao = 'CO' ");
			break;
		case PS_INSCRITO_MATRICULADO:
			sql.append(" and matricula.matricula is not null and matricula.situacao not in ('PR', 'PC') ");
			break;
		case PS_INSCRITO_NAO_CONFIRMADO:
			sql.append(" inscricao.situacao != 'CO' ");
			break;
		case PS_INSCRITO_PRE_MATRICULADO:
			sql.append(" and matricula.matricula is not null and matricula.situacao = 'PR' ");
			break;
		case PS_INSCRITO_REPROVADO:
			sql.append(" and  resultadoprocessoseletivo.codigo is not null and ((resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and procseletivo.nropcoescurso::INT = 1) ");
			sql.append(" or (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and resultadoprocessoseletivo.resultadoSegundaOpcao = 'RE' and procseletivo.nropcoescurso::INT = 2)  ");
			sql.append(" or (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and resultadoprocessoseletivo.resultadoSegundaOpcao = 'RE' and resultadoprocessoseletivo.resultadoTerceiraOpcao = 'RE' "); 
			sql.append(" and procseletivo.nropcoescurso::INT = 3))");
			break;
		case PS_INSCRITO_SEM_RESULTADO:
			sql.append(" and resultadoprocessoseletivo.codigo is null and inscricao.situacaoinscricao = 'ATIVO' and inscricao.situacao = 'CO' ");
			break;
        case PS_INSCRITO_NAO_COMPARECEU:
			sql.append(" and resultadoprocessoseletivo.codigo is null and inscricao.situacaoinscricao = 'NAO_COMPARECEU' and inscricao.situacao = 'CO' ");
			break;    
		default:
			break;
		}
		if (opcao != null && opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO)) {
			sql.append(" group by  unidadeensinocurso.codigo, unidadeensino.nome, unidadeensino.codigo,  ");
			sql.append(" curso.codigo, curso.nome,  ");
			sql.append(" turno.codigo, turno.nome ");
			sql.append(" order by  qtdeInscritoConfirmado desc, unidadeensino.nome, curso.nome, turno.nome ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		PainelGestorMonitoramentoProcessoSeletivoVO obj = null;
		if (opcao != null && opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO)) {
			filtroPainelGestorAcademico.getPainelGestorMonitoramentoProcessoSeletivoVOs().clear();
		}
		while(rs.next()){
			obj = new PainelGestorMonitoramentoProcessoSeletivoVO();
			obj.setOpcoesFiltroPainelGestorMonitoramentoAcademico(opcao);
			obj.setQtdeInscritoAprovado(rs.getInt("qtdeInscritoAprovado"));
			obj.setQtdeInscritoAprovadoNaoMatriculado(rs.getInt("qtdeInscritoAprovadoNaoMatriculado"));
			obj.setQtdeInscritoConfirmado(rs.getInt("qtdeInscritoConfirmado"));
			obj.setQtdeInscritoGeral(rs.getInt("qtdeInscritoGeral"));
			obj.setQtdeInscritoMatriculado(rs.getInt("qtdeInscritoMatriculado"));
			obj.setQtdeInscritoNaoConfirmado(rs.getInt("qtdeInscritoNaoConfirmado"));
			obj.setQtdeInscritoPreMatriculado(rs.getInt("qtdeInscritoPreMatriculado"));
			obj.setQtdeInscritoReprovado(rs.getInt("qtdeInscritoReprovado"));
			obj.setQtdeInscritoSemResultado(rs.getInt("qtdeInscritoSemResultado"));
            obj.setQtdeInscritoNaoCompareceu(rs.getInt("qtdeInscritoNaoCompareceu"));
			
			Double percMatriculado = obj.getQtdeInscritoAprovado().doubleValue() > 0 ? Uteis.arrendondarForcando2CadasDecimais((obj.getQtdeInscritoMatriculado().doubleValue()*100)/+obj.getQtdeInscritoAprovado().doubleValue()) : 0.0;
			Double percPreMatriculado = obj.getQtdeInscritoAprovado().doubleValue() > 0 ? Uteis.arrendondarForcando2CadasDecimais(obj.getQtdeInscritoPreMatriculado().doubleValue()*100/+obj.getQtdeInscritoAprovado().doubleValue()) : 0.0;				
			obj.setGraficoGeral("{name: 'Aprovados Processo Seletivo ("+obj.getQtdeInscritoAprovado()+" - "+Uteis.getDoubleFormatado(100-percMatriculado-percPreMatriculado)+"%)', data: ["+(100-percMatriculado-percPreMatriculado)+"], color:'#003399', index:0, zIndex:1 }");				
			obj.setGraficoGeral(obj.getGraficoGeral()+", {name: 'Matriculados ("+obj.getQtdeInscritoMatriculado()+" - "+Uteis.getDoubleFormatado(percMatriculado)+"%)', data: ["+percMatriculado+"], color:'#006600', index:2, zIndex:100 }");
			obj.setGraficoGeral(obj.getGraficoGeral()+", {name: 'Pré-Matriculados ("+obj.getQtdeInscritoPreMatriculado()+" - "+Uteis.getDoubleFormatado(percPreMatriculado)+"%)', data: ["+percPreMatriculado+"], color:'#666666', index:2, zIndex:100 }");
			if (opcao != null && !opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INICIAL)) {
				StringBuilder grafico = new StringBuilder("");
				grafico.append("<div id=\"container").append(rs.getInt("unidadeEnsinoCursoCodigo")).append("\" class=\"col-md-12\" ></div> ");
				grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
				grafico.append("	(function($) { ");				
				grafico.append("	$.ajaxSetup({");
				grafico.append("		cache : false");
				grafico.append("	});");
				grafico.append("	var options"+rs.getInt("unidadeEnsinoCursoCodigo")+" = {	");
				grafico.append("		chart: {type: 'bar', renderTo : \"container"+rs.getInt("unidadeEnsinoCursoCodigo")+"\", height:200,spacingLeft: 0, marginTop: 40},");
				grafico.append("		title: {text: null},");
				grafico.append("		credits : {enabled : false},");
				grafico.append("		exporting: { enabled: false },");
				grafico.append("		xAxis: {categories: ['']},");
				grafico.append("		yAxis: {min: 0,title: {text: null}},");				
				grafico.append("		tooltip: {");
				grafico.append("			useHTML:true,");
				grafico.append("			pointFormat: '<span style=\"color:{series.color}\">{series.name}</span>',");
				grafico.append("			shared: false");
				grafico.append("		},");
				grafico.append("		plotOptions: {bar: {dataLabels: {enabled: true}}},");
				grafico.append("		series: ["+obj.getGraficoGeral()+"]");
				grafico.append("	};");
				grafico.append("	var chart"+rs.getInt("unidadeEnsinoCursoCodigo")+" = new Highcharts.Chart(options"+rs.getInt("unidadeEnsinoCursoCodigo")+");");								
				grafico.append(" 	}(jQuery));");
				grafico.append(" </script>");
				obj.setGraficoPorCurso(grafico.toString());
				obj.getUnidadeEnsinoCursoVO().setCodigo(rs.getInt("unidadeEnsinoCursoCodigo"));	
				obj.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeEnsinoCodigo"));
				obj.getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsinoNome"));
				obj.getUnidadeEnsinoCursoVO().getCurso().setCodigo(rs.getInt("cursoCodigo"));
				obj.getUnidadeEnsinoCursoVO().getCurso().setNome(rs.getString("cursoNome"));
				obj.getUnidadeEnsinoCursoVO().getTurno().setCodigo(rs.getInt("turnoCodigo"));
				obj.getUnidadeEnsinoCursoVO().getTurno().setNome(rs.getString("turnoNome"));
				filtroPainelGestorAcademico.getPainelGestorMonitoramentoProcessoSeletivoVOs().add(obj);
			}else{
				filtroPainelGestorAcademico.setPainelGestorMonitoramentoProcessoSeletivoVO(obj);
				return;
			}			
		}
	}

	private void consultarDetalheMonitoramentoProcessoSeletivo(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, Integer limit, Integer offset, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao, Integer unidadeEnsinoCurso) {
		StringBuilder sb = new StringBuilder("SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo,");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, itemprocseletivodataprova.dataprova as data  ");
		sb.append(" from procseletivo ");
		sb.append(" inner join inscricao on inscricao.procseletivo = procseletivo.codigo ");
		sb.append(" inner join pessoa on inscricao.candidato = pessoa.codigo ");
		sb.append(" inner join unidadeensinocurso on inscricao.cursoopcao1 = unidadeensinocurso.codigo ");
		sb.append(" inner join curso on unidadeensinocurso.curso = curso.codigo ");
		sb.append(" inner join turno on unidadeensinocurso.turno = turno.codigo ");
		sb.append(" inner join unidadeEnsino on inscricao.unidadeEnsino = unidadeEnsino.codigo ");		
		sb.append(" left join matricula on inscricao.codigo = matricula.inscricao ");
                
                sb.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
                sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('CA', 'TR', 'PC', 'AB') order by mp.ano||'/'||mp.semestre limit 1 ) ");
                
		sb.append(" left join resultadoprocessoseletivo on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sb.append(" left join itemprocseletivodataprova on inscricao.itemprocessoseletivodataprova = itemprocseletivodataprova.codigo ");
		sb.append(" where 1 = 1 and situacaoInscricao <> 'CANCELADO_OUTRA_INSCRICAO' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and procseletivo.ano = '" + ano + "' and procseletivo.semestre = '" + semestre + "' ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "inscricao.data", false));
		} else {
			sb.append(" and procseletivo.ano = '" + ano + "' ");
		}
		if(unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0){
			sb.append(" and unidadeEnsinoCurso.codigo = " + unidadeEnsinoCurso);
		}
		
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "inscricao.unidadeEnsino", "and"));
		switch (opcao) {
		case PS_DETALHE_INSCRITO_APROVADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoAprovado"));
			sb.append(" and resultadoprocessoseletivo.codigo is not null and (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'AP' ");
			sb.append(" or (resultadoprocessoseletivo.resultadoSegundaOpcao = 'AP' and procseletivo.nropcoescurso::INT > 1) ");
			sb.append(" or (resultadoprocessoseletivo.resultadoTerceiraOpcao = 'AP' and procseletivo.nropcoescurso::INT > 2)) ");
			break;
		case PS_DETALHE_INSCRITO_APROVADO_NAO_MATRICULADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoAprovadoNaoMatriculado"));
			sb.append(" and matricula.matricula is null and resultadoprocessoseletivo.codigo is not null and (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'AP' "); 
			sb.append(" or (resultadoprocessoseletivo.resultadoSegundaOpcao = 'AP' and procseletivo.nropcoescurso::INT > 1) "); 
			sb.append(" or (resultadoprocessoseletivo.resultadoTerceiraOpcao = 'AP' and procseletivo.nropcoescurso::INT > 2)) ");
			break;
		case PS_DETALHE_INSCRITO_CONFIRMADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoConfirmado"));
			sb.append(" and inscricao.situacao = 'CO' ");
			break;
		case PS_DETALHE_INSCRITO_MATRICULADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoMatriculado"));
			sb.append(" and matricula.matricula is not null and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");
			break;
		case PS_DETALHE_INSCRITO_NAO_CONFIRMADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoNaoConfirmado"));
			sb.append(" inscricao.situacao != 'CO' ");
			break;
		case PS_DETALHE_INSCRITO_PRE_MATRICULADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoPreMatriculado"));
			sb.append(" and matricula.matricula is not null and matriculaperiodo.situacaomatriculaperiodo = 'PR' ");
			break;
		case PS_DETALHE_INSCRITO_REPROVADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoReprovados"));
			sb.append(" and  resultadoprocessoseletivo.codigo is not null and ((resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and procseletivo.nropcoescurso::INT = 1) ");
			sb.append(" or (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and resultadoprocessoseletivo.resultadoSegundaOpcao = 'RE' and procseletivo.nropcoescurso::INT = 2)  ");
			sb.append(" or (resultadoprocessoseletivo.resultadoPrimeiraOpcao = 'RE' and resultadoprocessoseletivo.resultadoSegundaOpcao = 'RE' and resultadoprocessoseletivo.resultadoTerceiraOpcao = 'RE' "); 
			sb.append(" and procseletivo.nropcoescurso::INT = 3))");
			break;
		case PS_DETALHE_INSCRITO_SEM_RESULTADO:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoSemResultado"));
			sb.append(" and resultadoprocessoseletivo.codigo is null and inscricao.situacaoinscricao = 'ATIVO' and inscricao.situacao = 'CO' ");
			break;
        case PS_DETALHE_INSCRITO_NAO_COMPARECEU:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoNaoCompareceu"));
			sb.append(" and resultadoprocessoseletivo.codigo is null and inscricao.situacaoinscricao = 'NAO_COMPARECEU' and inscricao.situacao = 'CO' ");
			break;                    
		default:
			filtroPainelGestorAcademico.setTituloApresentar(UteisJSF.internacionalizar("prt_MonitoramentoAcademico_inscritoGeral"));
			break;
		}
		sb.append(" group by matricula.matricula, pessoa.codigo , pessoa.nome, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer, ");
		sb.append(" unidadeEnsino.codigo, unidadeEnsino.nome, Curso.codigo, Curso.nome, ");
		sb.append(" Turno.codigo, Turno.codigo, Turno.nome, pessoa.sexo, itemprocseletivodataprova.dataprova ");
		sb.append(" order by pessoa.nome ");
		if (limit != null && limit > 0) {
			sb.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademico, tabelaResultado);
		}

	}
	
	public Integer consultarQuantidadeMonitoramentoAcademicoPreMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde from matricula ");
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" inner join curso on curso.codigo = matricula.curso");
		}
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		sb.append(" where matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from MatriculaPeriodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false)).append(" ");
			sb.append(" order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		} 
		return 0;
	}
	
	/**
	 * Método responsável por criar o gráfico de Evolução Academica Educação Infantil
	 * 
	 * @author CarlosEugenio 24/01/2014
	 */
	public void executarCriacaoGraficoEvolucaoPorNivelEducacional(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, PeriodicidadeEnum periodicidadeCurso, UsuarioVO usuarioVO) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);

		filtroPainelGestorAcademicoVO.setLegendaGraficoEvolucaoPorNivelEducacionalVOs(null);
		filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaCalouroInstituicao("");
		filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaVeteranoNivelAtual("");
		filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaVeteranoNivelAnterior("");
		filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaNaoRenovaramProximoNivel("");
		filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaNaoRenovaramMesmoNivel("");

		SqlRowSet rs = null;
		StringBuilder sb = new StringBuilder();
		List<String> listaNivelEducacionalVOs = consultarNivelEducacionalExistenteInstituicao(filtroPainelGestorAcademicoVO.getTipoNivelEducacional());
		if (listaNivelEducacionalVOs.size() == 1) {
			sb.append(consultarGraficoEvolucaoAcademicaNivelEducacional(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, periodicidadeCurso, filtroPainelGestorAcademicoVO.getTipoNivelEducacional(), usuarioVO));
			sb.append(" order by tipo, ordem ");
		}else {
			int index = 1;
			for (String nivelEduc : listaNivelEducacionalVOs) {
				if (index > 1) {
					sb.append(" union all ");
				}
				sb.append(consultarGraficoEvolucaoAcademicaNivelEducacional(unidadeEnsinoVOs, dataInicio, dataTermino, ano, semestre, periodicidadeCurso, nivelEduc, usuarioVO));
				if (index == listaNivelEducacionalVOs.size()) {
					sb.append(" order by tipo, ordem ");
				}
				index++;
			}
		
		}
		LegendaGraficoVO calouro = new LegendaGraficoVO("Calouro", 0, "#3399FF");
		LegendaGraficoVO veteranoNivelAtual = new LegendaGraficoVO("Veterano Nível Atual", 0, "#3399FF");
		LegendaGraficoVO veteranoNivelAnterior = new LegendaGraficoVO("Veterano Nível Anterior", 0, "#3399FF");
		LegendaGraficoVO naoRenovaramProximoNivel = new LegendaGraficoVO("Não Renovaram Próximo Nível", 0, "#3399FF");
		LegendaGraficoVO naoRenovaramMesmoNivel = new LegendaGraficoVO("Não Renovaram Mesmo Nível", 0, "#3399FF");
		
//		System.out.println(sb.toString());
		rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		HashMap<String, List<EvolucaoAcademicaNivelEducacionalVO>> mapEvolucaoAcademicaVOs = montarDadosEvolucaoAcademica(filtroPainelGestorAcademicoVO, listaNivelEducacionalVOs, rs);
		
			List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaCalouroInstituicaoVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO.toString());
			Ordenacao.ordenarLista(listaEvolucaoAcademicaCalouroInstituicaoVOs, "ordem");
			for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO : listaEvolucaoAcademicaCalouroInstituicaoVOs) {
				if (filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaCalouroInstituicao().isEmpty()) {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaCalouroInstituicao(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaCalouroInstituicao() + "{id:'"+evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getQuantidade() + "}");
				} else {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaCalouroInstituicao(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaCalouroInstituicao() + "," + "{id:'"+evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getQuantidade() + "}");
				}
				calouro.setQuantidade(calouro.getQuantidade() + evolucaoAcademicaNivelEducacionalCalouroInstituicaoVO.getQuantidade());
				
			}
		
			List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVeteranoNivelAtualVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL.toString());
			Ordenacao.ordenarLista(listaEvolucaoAcademicaVeteranoNivelAtualVOs, "ordem");
			for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO : listaEvolucaoAcademicaVeteranoNivelAtualVOs) {
				if (filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaVeteranoNivelAtual().isEmpty()) {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaVeteranoNivelAtual(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaVeteranoNivelAtual() + "" + "{id:'"+evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getQuantidade() + "}");
				} else {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaVeteranoNivelAtual(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaVeteranoNivelAtual() + "," + "{id:'"+evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getQuantidade() + "}");
				}
				veteranoNivelAtual.setQuantidade(veteranoNivelAtual.getQuantidade() + evolucaoAcademicaNivelEducacionalVeteranoNivelAtualVO.getQuantidade());
			}

		
			List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVeteranoNivelAnteriorVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR.toString());
			Ordenacao.ordenarLista(listaEvolucaoAcademicaVeteranoNivelAnteriorVOs, "ordem");
			for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO : listaEvolucaoAcademicaVeteranoNivelAnteriorVOs) {
				if (filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaVeteranoNivelAnterior().isEmpty()) {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaVeteranoNivelAnterior(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaVeteranoNivelAnterior() + "" + "{id:'"+evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getQuantidade() + "}");
				} else {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaVeteranoNivelAnterior(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaVeteranoNivelAnterior() + "," + "{id:'"+evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getQuantidade() + "}");
				}
				veteranoNivelAnterior.setQuantidade(veteranoNivelAnterior.getQuantidade() + evolucaoAcademicaNivelEducacionalVeteranoNivelAnteriorVO.getQuantidade());
			}

			List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaNaoRenovaramProximoNivelVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL.toString());
			Ordenacao.ordenarLista(listaEvolucaoAcademicaNaoRenovaramProximoNivelVOs, "ordem");
			for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO : listaEvolucaoAcademicaNaoRenovaramProximoNivelVOs) {
				if (filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaNaoRenovaramProximoNivel().isEmpty()) {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaNaoRenovaramProximoNivel(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaNaoRenovaramProximoNivel() + "" + "{id:'"+evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getQuantidade() + "}");
				} else {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaNaoRenovaramProximoNivel(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaNaoRenovaramProximoNivel() + "," + "{id:'"+evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getQuantidade() + "}");
				}
				naoRenovaramProximoNivel.setQuantidade(naoRenovaramProximoNivel.getQuantidade() + evolucaoAcademicaNivelEducacionaNaoRenovaramProximoNivellVO.getQuantidade());
			}
		
			List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaNaoRenovaramMesmoNivelVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL.toString());
			Ordenacao.ordenarLista(listaEvolucaoAcademicaNaoRenovaramMesmoNivelVOs, "ordem");
			for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO : listaEvolucaoAcademicaNaoRenovaramMesmoNivelVOs) {
				if (filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaNaoRenovaramMesmoNivel().isEmpty()) {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaNaoRenovaramMesmoNivel(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaNaoRenovaramMesmoNivel() + "" + "{id:'"+evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getQuantidade() + "}");
				} else {
					filtroPainelGestorAcademicoVO.setDadosEvolucaoAcademicaNaoRenovaramMesmoNivel(filtroPainelGestorAcademicoVO.getDadosEvolucaoAcademicaNaoRenovaramMesmoNivel() + "," + "{id:'"+evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getNivelEducacionalNomeCompleto()+"_"+evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getTipo().toString()+"', y:" + evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getQuantidade() + "}");
				}
				naoRenovaramMesmoNivel.setQuantidade(naoRenovaramMesmoNivel.getQuantidade() + evolucaoAcademicaNivelEducacionalNaoRenovaramMesmoNivelVO.getQuantidade());
			}

			calouro.setLegenda(calouro.getNome());
			veteranoNivelAtual.setLegenda(veteranoNivelAtual.getNome());
			veteranoNivelAnterior.setLegenda(veteranoNivelAnterior.getNome());
			naoRenovaramProximoNivel.setLegenda(naoRenovaramProximoNivel.getNome());
			naoRenovaramMesmoNivel.setLegenda(naoRenovaramMesmoNivel.getNome());
		

		filtroPainelGestorAcademicoVO.getLegendaGraficoEvolucaoPorNivelEducacionalVOs().add(calouro);
		filtroPainelGestorAcademicoVO.getLegendaGraficoEvolucaoPorNivelEducacionalVOs().add(veteranoNivelAtual);
		filtroPainelGestorAcademicoVO.getLegendaGraficoEvolucaoPorNivelEducacionalVOs().add(veteranoNivelAnterior);
		filtroPainelGestorAcademicoVO.getLegendaGraficoEvolucaoPorNivelEducacionalVOs().add(naoRenovaramProximoNivel);
		filtroPainelGestorAcademicoVO.getLegendaGraficoEvolucaoPorNivelEducacionalVOs().add(naoRenovaramMesmoNivel);

	}
	
	public HashMap<String, List<EvolucaoAcademicaNivelEducacionalVO>> montarDadosEvolucaoAcademica(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO, List<String> listaNivelEducacionalVOs, SqlRowSet tabelaResultado) {
		adicionarCategoriaEvolucaoAcademicaOrdenado(filtroPainelGestorAcademicoVO, listaNivelEducacionalVOs);
		HashMap<String, List<EvolucaoAcademicaNivelEducacionalVO>> mapEvolucaoAcademicaVOs = new HashMap<String, List<EvolucaoAcademicaNivelEducacionalVO>>(0);
		for (String nivelEducacional : listaNivelEducacionalVOs) {
			
			if (!mapEvolucaoAcademicaVOs.containsKey(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO.toString())) {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoCalouroVOs = new ArrayList<EvolucaoAcademicaNivelEducacionalVO>(0);
				listaEvolucaoCalouroVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO.toString(), listaEvolucaoCalouroVOs);
			} else {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoCalouroVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO.toString());
				listaEvolucaoCalouroVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO.toString(), listaEvolucaoCalouroVOs);
			}
			
			if (!mapEvolucaoAcademicaVOs.containsKey(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR.toString())) {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = new ArrayList<EvolucaoAcademicaNivelEducacionalVO>(0);
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR.toString(), listaEvolucaoAcademicaVOs);
			} else {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR.toString());
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR.toString(), listaEvolucaoAcademicaVOs);
			}
			
			if (!mapEvolucaoAcademicaVOs.containsKey(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL.toString())) {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = new ArrayList<EvolucaoAcademicaNivelEducacionalVO>(0);
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL.toString(), listaEvolucaoAcademicaVOs);
			} else {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL.toString());
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL.toString(), listaEvolucaoAcademicaVOs);
			}
			
			if (!mapEvolucaoAcademicaVOs.containsKey(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL.toString())) {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = new ArrayList<EvolucaoAcademicaNivelEducacionalVO>(0);
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL.toString(), listaEvolucaoAcademicaVOs);
			} else {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL.toString());
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL.toString(), listaEvolucaoAcademicaVOs);
			}
			
			if (!mapEvolucaoAcademicaVOs.containsKey(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL.toString())) {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = new ArrayList<EvolucaoAcademicaNivelEducacionalVO>(0);
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL.toString(), listaEvolucaoAcademicaVOs);
			} else {
				List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = mapEvolucaoAcademicaVOs.get(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL.toString());
				listaEvolucaoAcademicaVOs.add(inicializarDadosEvolucaoAcademica(nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL));
				mapEvolucaoAcademicaVOs.put(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL.toString(), listaEvolucaoAcademicaVOs);
			}
			
		}
		
		while (tabelaResultado.next()) {
			List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaVOs = mapEvolucaoAcademicaVOs.get(tabelaResultado.getString("tipo"));
			for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO : listaEvolucaoAcademicaVOs) {
				if (evolucaoAcademicaNivelEducacionalVO.getNivelEducacional().equals(tabelaResultado.getString("nivelEducacional"))) {
					evolucaoAcademicaNivelEducacionalVO.setQuantidade(tabelaResultado.getInt("qtde"));
					evolucaoAcademicaNivelEducacionalVO.setOrdem(tabelaResultado.getInt("ordem"));
					break;
				}
			}
		}
		
		return mapEvolucaoAcademicaVOs;
	}
	
	public EvolucaoAcademicaNivelEducacionalVO inicializarDadosEvolucaoAcademica(String nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum tipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum) {
		EvolucaoAcademicaNivelEducacionalVO obj = new EvolucaoAcademicaNivelEducacionalVO();
		obj.setNivelEducacional(nivelEducacional);
		obj.setTipo(tipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.toString());
		if (nivelEducacional.equals("IN")) {
			obj.setOrdem(1);
		} else if (nivelEducacional.equals("BA")) {
			obj.setOrdem(2);
		} else if (nivelEducacional.equals("ME")) {
			obj.setOrdem(3);
		} else if (nivelEducacional.equals("SU")) {
			obj.setOrdem(4);
		} else if (nivelEducacional.equals("PO")) {
			obj.setOrdem(5);
		}
		return obj;
	}
	
	public StringBuilder consultarGraficoEvolucaoAcademicaNivelEducacional(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, PeriodicidadeEnum periodicidadeCurso, String nivelEducacional, UsuarioVO usuarioVO) throws Exception {
		//		CALOURO INSTITUICAO
		StringBuilder sb = new StringBuilder(" SELECT count(distinct matricula.matricula) as qtde, curso.nivelEducacional, 'CALOURO_INSTITUICAO' AS tipo, ");
		sb.append(" case ");
		sb.append(" when curso.nivelEducacional = 'IN' then 1 ");
		sb.append(" when curso.nivelEducacional = 'BA' then 2 ");
		sb.append(" when curso.nivelEducacional = 'ME' then 3 ");
		sb.append(" when curso.nivelEducacional = 'SU' then 4 ");
		sb.append(" when curso.nivelEducacional = 'PO' then 5 ");
		sb.append(" end AS ordem ");
		sb.append(" from matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		}
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp  where  mp.matricula = matricula.matricula ");
		sb.append(" and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
		sb.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
		sb.append(" group by curso.nivelEducacional ");
		
		sb.append(" union all ");

		//		VETERANO NIVEL ATUAL
		sb.append(" SELECT count(distinct matricula.matricula) as qtde, curso.nivelEducacional, 'VETERANO_NIVEL_ATUAL' AS tipo, ");
		sb.append(" case ");
		sb.append(" when curso.nivelEducacional = 'IN' then 1 ");
		sb.append(" when curso.nivelEducacional = 'BA' then 2 ");
		sb.append(" when curso.nivelEducacional = 'ME' then 3 ");
		sb.append(" when curso.nivelEducacional = 'SU' then 4 ");
		sb.append(" when curso.nivelEducacional = 'PO' then 5 ");
		sb.append(" end AS ordem ");
		sb.append(" from matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		}
		sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula ");
		sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp ");
		sb.append(" inner join matricula m on m.matricula = mp.matricula ");
		sb.append(" inner join curso c on c.codigo = m.curso ");
		sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) ");
		sb.append(" and c.niveleducacional = curso.niveleducacional order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
		sb.append(" group by curso.nivelEducacional ");
		
		sb.append(" union all ");
//		VETERANO NIVEL ANTERIOR
		sb.append(" SELECT count(distinct matricula.matricula) as qtde, curso.nivelEducacional, 'VETERANO_NIVEL_ANTERIOR' AS tipo, ");
		sb.append(" case ");
		sb.append(" when curso.nivelEducacional = 'IN' then 1 ");
		sb.append(" when curso.nivelEducacional = 'BA' then 2 ");
		sb.append(" when curso.nivelEducacional = 'ME' then 3 ");
		sb.append(" when curso.nivelEducacional = 'SU' then 4 ");
		sb.append(" when curso.nivelEducacional = 'PO' then 5 ");
		sb.append(" end AS ordem ");
		sb.append(" from matricula ");
		
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		}
		sb.append(" and matricula.aluno in (select m.aluno from matriculaPeriodo mp ");
		sb.append(" inner join matricula m on m.matricula = mp.matricula ");
		sb.append(" inner join curso c on c.codigo = m.curso ");
		sb.append(" where mp.matricula != matricula.matricula ");
		sb.append(" and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
		sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) ");
		sb.append(" and c.niveleducacional = '").append(getNivelEducacionalAnteriorUtilizar(nivelEducacional)).append("' ");
		sb.append(" and m.aluno = matricula.aluno ");
		sb.append(" order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
		sb.append(" group by curso.nivelEducacional ");
		
		sb.append(" union all ");
//		NAO RENOVARAM PROXIMO NÍVEL
		sb.append(" SELECT count(distinct matricula.matricula) as qtde, curso.nivelEducacional, 'NAO_RENOVARAM_PROXIMO_NIVEL' AS tipo, ");
		sb.append(" case ");
		sb.append(" when curso.nivelEducacional = 'IN' then 1 ");
		sb.append(" when curso.nivelEducacional = 'BA' then 2 ");
		sb.append(" when curso.nivelEducacional = 'ME' then 3 ");
		sb.append(" when curso.nivelEducacional = 'SU' then 4 ");
		sb.append(" when curso.nivelEducacional = 'PO' then 5 ");
		sb.append(" end AS ordem ");
		sb.append(" from matricula ");
		
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		String anoTemp = ano;
		String semestreTemp = semestre;
		if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
			anoTemp = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(anoTemp).append("' ");
		} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				anoTemp = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestreTemp = "2";
			} else {
				semestreTemp = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(anoTemp).append("' and matriculaPeriodo.semestre = '").append(semestreTemp).append("' ");
		} else {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino ");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso ");
		sb.append(" where curso.nivelEducacional in ('").append(nivelEducacional).append("') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and case when exists (select codigo from curso where niveleducacional = '").append(getNivelEducacionalProximoUtilizar(nivelEducacional)).append("' limit 1) then ");
		sb.append(" matricula.aluno not in (select m.aluno from MatriculaPeriodo mp ");
		sb.append(" inner join matricula m on m.matricula = mp.matricula ");
		sb.append(" inner join curso c on c.codigo = m.curso ");
		sb.append(" where mp.matricula != matricula.matricula ");
		sb.append(" and m.aluno = matricula.aluno ");
		sb.append(" and mp.situacaomatriculaperiodo in ('AT', 'PR') ");
		sb.append(" and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ");
		sb.append(" and c.niveleducacional = '").append(getNivelEducacionalProximoUtilizar(nivelEducacional)).append("') end ");
		
		sb.append(" and not exists (");
		sb.append(" select gradedisciplina.codigo from gradedisciplina ");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sb.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual ");
		sb.append(" and gradedisciplina.tipodisciplina in ('OB', 'LG') ");
		sb.append(" and not exists (select historico.gradedisciplina from historico ");
		sb.append(" where matricula  = matricula.matricula and matrizcurricular = matricula.gradecurricularatual ");
		sb.append(" and gradedisciplina is not null and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') and historico.gradedisciplina = gradedisciplina.codigo) ");
		sb.append(" ) ");

		sb.append(" group by curso.nivelEducacional ");
		
		sb.append(" union all ");
//		NAO RENOVARAM MESMO NIVEL
		sb.append(" SELECT count(distinct matricula.matricula) as qtde, curso.nivelEducacional, 'NAO_RENOVARAM_MESMO_NIVEL' AS tipo, ");
		sb.append(" case ");
		sb.append(" when curso.nivelEducacional = 'IN' then 1 ");
		sb.append(" when curso.nivelEducacional = 'BA' then 2 ");
		sb.append(" when curso.nivelEducacional = 'ME' then 3 ");
		sb.append(" when curso.nivelEducacional = 'SU' then 4 ");
		sb.append(" when curso.nivelEducacional = 'PO' then 5 ");
		sb.append(" end AS ordem ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		String anoTempMesmoNivel = ano;
		String semestreTempMesmoNivel = semestre;
		if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
			anoTempMesmoNivel = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
			sb.append(" and matriculaPeriodo.ano = '").append(anoTempMesmoNivel).append("' ");
		} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (semestre.equals("1")) {
				anoTempMesmoNivel = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				semestreTempMesmoNivel = "2";
			} else {
				semestreTempMesmoNivel = "1";
			}
			sb.append(" and matriculaPeriodo.ano = '").append(anoTempMesmoNivel).append("' and matriculaPeriodo.semestre = '").append(semestreTempMesmoNivel).append("' ");
		} else {
			sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
		}
		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino ");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso ");
		sb.append(" where curso.nivelEducacional in ('").append(nivelEducacional).append("') ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
		sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
		sb.append(" where mp.matricula = matricula.matricula  ");
		sb.append(" and mp.situacaomatriculaperiodo in ('AT', 'PR') ");
		sb.append(" and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre) ");
		
		sb.append(" and exists (");
		sb.append(" select gradedisciplina.codigo from gradedisciplina ");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sb.append(" where periodoLetivo.gradecurricular = matricula.gradecurricularatual ");
		sb.append(" and gradedisciplina.tipodisciplina in ('OB', 'LG') ");
		sb.append(" and not exists (select historico.gradedisciplina from historico ");
		sb.append(" where matricula  = matricula.matricula and matrizcurricular = matricula.gradecurricularatual ");
		sb.append(" and gradedisciplina is not null and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') and historico.gradedisciplina = gradedisciplina.codigo)  ");
		sb.append(" ) ");
		
		sb.append(" and matricula.aluno not in(");
		sb.append(" select m.aluno from matricula m ");
		sb.append(" inner join matriculaperiodo mp on mp.matricula = m.matricula ");
		sb.append(" inner join curso on curso.codigo = m.curso ");
		sb.append(" where m.aluno = matricula.aluno ");
		sb.append(" and curso.niveleducacional = '").append(getNivelEducacionalProximoUtilizar(nivelEducacional)).append("' ");
		sb.append(" and mp.situacaomatriculaperiodo in ('AT', 'PR') ");
		sb.append(" order by (mp.ano||'/'||mp.semestre) asc, mp.codigo asc limit 1) ");
		
		sb.append(" group by curso.nivelEducacional ");
		return sb;
	}

	@Override
	public List<String> consultarNivelEducacionalExistenteInstituicao(String tipoNivelEducacional) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct niveleducacional from curso ");
		sb.append(" inner join matricula on matricula.curso = curso.codigo ");
		if (tipoNivelEducacional.equals("")) {
			sb.append(" where nivelEducacional in('IN', 'BA', 'ME', 'SU', 'PO') ");
		} else {
			sb.append(" where nivelEducacional in('").append(tipoNivelEducacional).append("') ");
		}
		sb.append(" order by niveleducacional ");
		List<String> listaNivelEducacionalVOs = new ArrayList<String>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaNivelEducacionalVOs.add(tabelaResultado.getString("nivelEducacional"));
		}
		return listaNivelEducacionalVOs;
	}
	
	public String getNivelEducacionalAnteriorUtilizar(String nivelEducacional) {
		if (nivelEducacional.equals("IN")) {
			return "";
		} else if (nivelEducacional.equals("BA")) {
			return "IN";
		} else if (nivelEducacional.equals("ME")) {
			return "BA";
		} else if (nivelEducacional.equals("SU")) {
			return "ME";
		} else if (nivelEducacional.equals("PO")) {
			return "SU";
		} else {
			return "";
		}
		
	}
	
	public String getNivelEducacionalProximoUtilizar(String nivelEducacional) {
		if (nivelEducacional.equals("IN")) {
			return "BA";
		} else if (nivelEducacional.equals("BA")) {
			return "ME";
		} else if (nivelEducacional.equals("ME")) {
			return "SU";
		} else if (nivelEducacional.equals("SU")) {
			return "PO";
		} else if (nivelEducacional.equals("PO")) {
			return "";
		} else {
			return "";
		}
	}
	
	public void consultarEvolucaoAcademicaNivelEducacional(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, PeriodicidadeEnum periodicidadeCurso, String nivelEducacional, TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum tipo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT matricula.matricula, pessoa.codigo as pessoaCodigo, pessoa.nome as pessoaNome, pessoa.email as email, agruparTelefone(pessoa.telefoneres, pessoa.celular, pessoa.telefonerecado, pessoa.telefonecomer) as telefone, pessoa.sexo as sexo, ");
		sb.append(" unidadeEnsino.codigo as unidadeEnsinoCodigo, unidadeEnsino.nome as unidadeEnsinoNome, Curso.codigo as codigoCurso, Curso.nome as cursoNome, ");
		sb.append(" Turno.codigo as turnoCodigo, Turno.codigo as turnoCodigo, Turno.nome as turnoNome, Turma.codigo as turmaCodigo, Turma.identificadorTurma as identificadorTurma ");
		sb.append(" from matricula ");
		if (tipo.equals(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.CALOURO_INSTITUICAO)) {
//			CALOURO INSTITUICAO
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
			sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
			if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
				sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
			} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
			} else {
				sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
				sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
				sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
			}
			sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
			sb.append(" inner join Turno on Turno.codigo = matricula.turno");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
			sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
			
			sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
			sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
			sb.append(" and 0 = (select count(codigo) from matriculaPeriodo mp  where  mp.matricula = matricula.matricula ");
			sb.append(" and  (mp.ano||'/'||mp.semestre) <  (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)) ");
			sb.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
			sb.append(" order by pessoa.nome");
		} else if (tipo.equals(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ATUAL)) {
//			VETERANO NIVEL ATUAL
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
			sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
			if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
				sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
			} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
			} else {
				sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
				sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
				sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
			}
			sb.append(" inner join matriculaPeriodo mpt on mpt.matricula = matricula.matricula ");
			sb.append(" and mpt.codigo = (select mp.codigo from matriculaPeriodo mp ");
			sb.append(" inner join matricula m on m.matricula = mp.matricula ");
			sb.append(" inner join curso c on c.codigo = m.curso ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
			sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) ");
			sb.append(" and c.niveleducacional = curso.niveleducacional order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
			
			sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
			sb.append(" inner join Turno on Turno.codigo = matricula.turno");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
			sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
			
			sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
			sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
			sb.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
			sb.append(" order by pessoa.nome");
		} else if (tipo.equals(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.VETERANO_NIVEL_ANTERIOR)) {
//			VETERANO NIVEL ANTERIOR
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
			sb.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
			if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
				sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
			} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
			} else {
				sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
				sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
				sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
			}
			sb.append(" and matricula.aluno in (select m.aluno from matriculaPeriodo mp ");
			sb.append(" inner join matricula m on m.matricula = mp.matricula ");
			sb.append(" inner join curso c on c.codigo = m.curso ");
			sb.append(" where mp.matricula != matricula.matricula ");
			sb.append(" and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
			sb.append(" and mp.ano||'/'||mp.semestre < (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre) ");
			sb.append(" and c.niveleducacional = '").append(getNivelEducacionalAnteriorUtilizar(nivelEducacional)).append("' ");
			sb.append(" and m.aluno = matricula.aluno ");
			sb.append(" order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
			
			sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
			sb.append(" inner join Turno on Turno.codigo = matricula.turno");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
			sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
			
			sb.append(" where matriculaPeriodo.situacaomatriculaPeriodo in ('AT', 'FI') ");
			sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
			sb.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
			sb.append(" order by pessoa.nome");
		} else if (tipo.equals(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_PROXIMO_NIVEL)) {
//			NAO RENOVARAM PROXIMO NÍVEL
			sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			String anoTemp = ano;
			String semestreTemp = semestre;
			if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
				anoTemp = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				sb.append(" and matriculaPeriodo.ano = '").append(anoTemp).append("' ");
			} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
				if (semestre.equals("1")) {
					anoTemp = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
					semestreTemp = "2";
				} else {
					semestreTemp = "1";
				}
				sb.append(" and matriculaPeriodo.ano = '").append(anoTemp).append("' and matriculaPeriodo.semestre = '").append(semestreTemp).append("' ");
			} else {
				sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
				sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
				sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
			}
			sb.append(" inner join Curso on Curso.codigo = matricula.curso ");
			sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
			sb.append(" inner join Turno on Turno.codigo = matricula.turno");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
			sb.append(" left join Turma on Turma.codigo = matriculaPeriodo.turma");
			
			sb.append(" where curso.nivelEducacional in ('").append(nivelEducacional).append("') ");
			sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
			sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
			sb.append(" and case when exists (select codigo from curso where niveleducacional = '").append(getNivelEducacionalProximoUtilizar(nivelEducacional)).append("' limit 1) then ");
			sb.append(" matricula.aluno not in (select m.aluno from MatriculaPeriodo mp ");
			sb.append(" inner join matricula m on m.matricula = mp.matricula ");
			sb.append(" inner join curso c on c.codigo = m.curso ");
			sb.append(" where mp.matricula != matricula.matricula ");
			sb.append(" and m.aluno = matricula.aluno ");
			sb.append(" and mp.situacaomatriculaperiodo in ('AT', 'PR') ");
			sb.append(" and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre ");
			sb.append(" and c.niveleducacional = '").append(getNivelEducacionalProximoUtilizar(nivelEducacional)).append("') end ");
			
			sb.append(" and not exists (");
			sb.append(" select gradedisciplina.codigo from gradedisciplina ");
			sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sb.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sb.append(" and gradedisciplina.tipodisciplina in ('OB', 'LG') ");
			sb.append(" and gradedisciplina.codigo not in (select historico.gradedisciplina from historico ");
			sb.append(" where matricula  = matricula.matricula and matrizcurricular = matricula.gradecurricularatual ");
			sb.append(" and gradedisciplina is not null and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') and historico.gradedisciplina = gradedisciplina.codigo ) limit 1 ) ");
			sb.append(" ");
			sb.append(" order by pessoaNome");
		} else if (tipo.equals(TipoNivelEducacionalEvolucaoAcademicaPainelGestorEnum.NAO_RENOVARAM_MESMO_NIVEL)) {
//			NAO RENOVARAM MESMO NIVEL
			sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			String anoTempMesmoNivel = ano;
			String semestreTempMesmoNivel = semestre;
			if (periodicidadeCurso.equals(PeriodicidadeEnum.ANUAL)) {
				anoTempMesmoNivel = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
				sb.append(" and matriculaPeriodo.ano = '").append(anoTempMesmoNivel).append("' ");
			} else if (periodicidadeCurso.equals(PeriodicidadeEnum.SEMESTRAL)) {
				if (semestre.equals("1")) {
					anoTempMesmoNivel = Integer.valueOf((Integer.parseInt(ano) - 1)).toString();
					semestreTempMesmoNivel = "2";
				} else {
					semestreTempMesmoNivel = "1";
				}
				sb.append(" and matriculaPeriodo.ano = '").append(anoTempMesmoNivel).append("' and matriculaPeriodo.semestre = '").append(semestreTempMesmoNivel).append("' ");
			} else {
				sb.append(" and matriculaperiodo.codigo = ( select codigo from matriculaPeriodo mp ");
				sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
				sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.datafechamentomatriculaperiodo", false)).append(" order by mp.ano||'/'||mp.semestre asc limit 1 ) ");
			}
			sb.append(" inner join Curso on Curso.codigo = matricula.curso ");
			sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
			sb.append(" inner join Turno on Turno.codigo = matricula.turno");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
			sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
			
			sb.append(" where curso.nivelEducacional in ('").append(nivelEducacional).append("') ");
			sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", "and"));
			sb.append(" and matriculaperiodo.situacaoMatriculaPeriodo in ('FI', 'AT') ");
			sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  ");
			sb.append(" and mp.situacaomatriculaperiodo in ('AT', 'PR') ");
			sb.append(" and mp.ano||'/'||mp.semestre > matriculaperiodo.ano||'/'||matriculaperiodo.semestre) ");
			
			sb.append(" and exists (");
			sb.append(" select gradedisciplina.codigo from gradedisciplina ");
			sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sb.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sb.append(" and gradedisciplina.tipodisciplina in ('OB', 'LG') ");
			sb.append(" and gradedisciplina.codigo not in (select historico.gradedisciplina from historico ");
			sb.append(" where matricula  = matricula.matricula and matrizcurricular = matricula.gradecurricularatual ");
			sb.append(" and gradedisciplina is not null and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') and historico.gradedisciplina = gradedisciplina.codigo ) limit 1 ");
			sb.append(" ) ");
			
			sb.append(" and matricula.aluno not in(");
			sb.append(" select m.aluno from matricula m ");
			sb.append(" inner join matriculaperiodo mp on mp.matricula = m.matricula ");
			sb.append(" inner join curso on curso.codigo = m.curso ");
			sb.append(" where m.aluno = matricula.aluno ");
			sb.append(" and curso.niveleducacional = '").append(getNivelEducacionalProximoUtilizar(nivelEducacional)).append("' ");
			sb.append(" and mp.situacaomatriculaperiodo in ('AT', 'PR') ");
			sb.append(" order by (mp.ano||'/'||mp.semestre) asc, mp.codigo asc limit 1) ");
			
			sb.append(" order by pessoaNome");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			adicionarResultadoDetalhado(filtroPainelGestorAcademicoVO, tabelaResultado);
		}
	}
	
	public String getNivelEducacionalApresentar(String nivelEducacional) {
		if (nivelEducacional.equals("IN")) {
			return "Infantil";
		} else if (nivelEducacional.equals("BA")) {
			return "Fundamental";
		} else if (nivelEducacional.equals("ME")) {
			return "Médio";
		} else if (nivelEducacional.equals("SU")) {
			return "Graduação";
		} else if (nivelEducacional.equals("PO")) {
			return "Pós-Graduação";
		} else {
			return "";
		}
	}
	
	public void adicionarCategoriaEvolucaoAcademicaOrdenado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO, List<String> listaNivelEducacionalVOs) {
		List<EvolucaoAcademicaNivelEducacionalVO> listaEvolucaoAcademicaNivelEducacionalVOs = new ArrayList<EvolucaoAcademicaNivelEducacionalVO>(0);
		filtroPainelGestorAcademicoVO.setCategoriaEvolucaoAcademica("");
		
		for (String nivelEducacional : listaNivelEducacionalVOs) {
			if (nivelEducacional.equals("IN")) {
				EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO = new EvolucaoAcademicaNivelEducacionalVO();
				evolucaoAcademicaNivelEducacionalVO.setNivelEducacional("IN");
				evolucaoAcademicaNivelEducacionalVO.setOrdem(1);
				listaEvolucaoAcademicaNivelEducacionalVOs.add(evolucaoAcademicaNivelEducacionalVO);
			} else if (nivelEducacional.equals("BA")) {
				EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO = new EvolucaoAcademicaNivelEducacionalVO();
				evolucaoAcademicaNivelEducacionalVO.setNivelEducacional("BA");
				evolucaoAcademicaNivelEducacionalVO.setOrdem(2);
				listaEvolucaoAcademicaNivelEducacionalVOs.add(evolucaoAcademicaNivelEducacionalVO);
			} else if (nivelEducacional.equals("ME")) {
				EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO = new EvolucaoAcademicaNivelEducacionalVO();
				evolucaoAcademicaNivelEducacionalVO.setNivelEducacional("ME");
				evolucaoAcademicaNivelEducacionalVO.setOrdem(3);
				listaEvolucaoAcademicaNivelEducacionalVOs.add(evolucaoAcademicaNivelEducacionalVO);
			} else if (nivelEducacional.equals("SU")) {
				EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO = new EvolucaoAcademicaNivelEducacionalVO();
				evolucaoAcademicaNivelEducacionalVO.setNivelEducacional("SU");
				evolucaoAcademicaNivelEducacionalVO.setOrdem(4);
				listaEvolucaoAcademicaNivelEducacionalVOs.add(evolucaoAcademicaNivelEducacionalVO);
			} else if (nivelEducacional.equals("PO")) {
				EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO = new EvolucaoAcademicaNivelEducacionalVO();
				evolucaoAcademicaNivelEducacionalVO.setNivelEducacional("PO");
				evolucaoAcademicaNivelEducacionalVO.setOrdem(5);
				listaEvolucaoAcademicaNivelEducacionalVOs.add(evolucaoAcademicaNivelEducacionalVO);
			}
		}
		Ordenacao.ordenarLista(listaEvolucaoAcademicaNivelEducacionalVOs, "ordem");
		for (EvolucaoAcademicaNivelEducacionalVO evolucaoAcademicaNivelEducacionalVO : listaEvolucaoAcademicaNivelEducacionalVOs) {
			if (filtroPainelGestorAcademicoVO.getCategoriaEvolucaoAcademica().equals("")) {
				filtroPainelGestorAcademicoVO.setCategoriaEvolucaoAcademica("'"+getNivelEducacionalApresentar(evolucaoAcademicaNivelEducacionalVO.getNivelEducacional())+"'");
			} else {
				filtroPainelGestorAcademicoVO.setCategoriaEvolucaoAcademica(filtroPainelGestorAcademicoVO.getCategoriaEvolucaoAcademica()+ ", " + "'"+getNivelEducacionalApresentar(evolucaoAcademicaNivelEducacionalVO.getNivelEducacional())+"'");
			}
		}
		
	}

	private void consultarMonitoramentoAcademicoJubilado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre) {
		StringBuilder sb = new StringBuilder("SELECT count(distinct matricula.matricula) as qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'JU' ");
		if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.datafechamentomatriculaperiodo", false));
		} else if (filtroPainelGestorAcademico.getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		} else {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}

		sb.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = matricula.unidadeEnsino");
		sb.append(" inner join Curso on Curso.codigo = matricula.curso");
		sb.append(" inner join Turno on Turno.codigo = matricula.turno");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sb.append(" inner join Turma on Turma.codigo = matriculaPeriodo.turma");
		sb.append(" where ");
		sb.append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeEnsino", ""));
		if (!filtroPainelGestorAcademico.getTipoNivelEducacional().equals("")) {
			sb.append(" and curso.niveleducacional = '").append(filtroPainelGestorAcademico.getTipoNivelEducacional()).append("'");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			adicionarResultado(filtroPainelGestorAcademico, "Jubilado", tabelaResultado.getInt("qtde"), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.JUBILADO, 0);
		} else {
			adicionarResultado(filtroPainelGestorAcademico, "Jubilado", 0, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.JUBILADO, 0);
		}
	}
}
