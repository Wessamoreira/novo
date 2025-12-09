package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;


import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.academico.enumeradores.VariaveisNotaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoAcademicoNotaInterfaceFacade;

@Service
@Lazy
public class ConfiguracaoAcademicoNota extends ControleAcesso implements ConfiguracaoAcademicoNotaInterfaceFacade {

	/**
	 *
	 */
	private static final long serialVersionUID = 257064130526168396L;

	@Override
	public void validarDados(ConfiguracaoAcademicoVO configuracaoAcademicoVO, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO) throws ConsistirException{
		if(configuracaoAcademicaNotaVO.getNotaRecuperacao() && configuracaoAcademicaNotaVO.getFormulaCalculoVerificaNotaRecuperada().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNota_formulaCalculoVerificaNotaRecuperada").replace("{0}", "NOTA "+configuracaoAcademicaNotaVO.getNota().getNumeroNota()));
		}
		if(configuracaoAcademicaNotaVO.getNotaRecuperacao() && !configuracaoAcademicaNotaVO.getFormulaCalculoVerificaNotaRecuperada().trim().isEmpty()){
			String formula = configuracaoAcademicaNotaVO.getFormulaCalculoVerificaNotaRecuperada();
			for(int i = 1; i <= 40; i++){
				ConfiguracaoAcademicaNotaVO obj = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota"+i+"VO");
				if (obj.getUtilizarNota()) {
					if (obj.getUtilizarNota() && formula.contains(obj.getVariavel())) {
						formula = formula.replaceAll(obj.getVariavel(), "0.0");
					}
					for (VariaveisNotaEnum variavelNota : VariaveisNotaEnum.values()) {
						if (obj.getVariavel().contains(variavelNota.getValor())) {
							throw new ConsistirException("A VARIÁVEL DA NOTA " + i + " esta em conflito com a VARIÁVEL RESERVADA " + variavelNota + ", ambas não podem ter a mesma sequência de caracteres.(" + configuracaoAcademicoVO.getNome() + ")");
						}
					}
					if (obj.getVariavel().contains("MAIOR") || "MAIOR".contains(obj.getVariavel())) {
						throw new ConsistirException("A VARIÁVEL DA NOTA " + i + " esta em conflito com a FUNÇÃO RESERVADA - MAIOR, ambas não podem ter a mesma sequência de caracteres.(" + configuracaoAcademicoVO.getNome() + ")");
					}
				}
			}
			if(!Uteis.verificarFormulaEstaCorreta(formula, true)){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNota_formulaCalculoVerificaNotaRecuperada").replace("{0}", "NOTA "+configuracaoAcademicaNotaVO.getNota().getNumeroNota()));
			}
		}
		if(configuracaoAcademicaNotaVO.getPermiteReplicarNotaOutraDisciplina() && (configuracaoAcademicaNotaVO.getUtilizarComoMediaFinal()
				|| !configuracaoAcademicaNotaVO.getFormulaCalculo().trim().isEmpty())){
			configuracaoAcademicaNotaVO.setPermiteReplicarNotaOutraDisciplina(false);
		}
	}

	private void incluir(final ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO) throws Exception {

		configuracaoAcademicaNotaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoAcademicoNota (configuracaoAcademico, nota, notaRecuperacao, qtdeDisciplinaRecuperacao, ");
				sql.append(" utilizarNota, utilizarNotaPorConceito, variavel, titulo, formulaCalculo, formulaUso, utilizarComoMediaFinal, agrupamentoNota, faixaNotaMenor, faixaNotaMaior,");
				sql.append(" apresentarNota, utilizarComoSubstitutiva, politicaSubstitutiva, regraArredondamentoNota, permiteRealizarRecuperacaoDisciplinaDependencia, permiteRealizarRecuperacaoDisciplinaAdaptacao, ");
				sql.append(" formulaCalculoVerificaNotaRecuperada, apresentarNotaBoletim, permiteReplicarNotaOutraDisciplina, tipoUsoNota) ");
				sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?) returning codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, configuracaoAcademicaNotaVO.getConfiguracaoAcademico().getCodigo());
				ps.setString(x++, configuracaoAcademicaNotaVO.getNota().name());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getNotaRecuperacao());
				ps.setInt(x++, configuracaoAcademicaNotaVO.getQtdeDisciplinaRecuperacao());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarNota());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarNotaPorConceito());
				ps.setString(x++, configuracaoAcademicaNotaVO.getVariavel());
				ps.setString(x++, configuracaoAcademicaNotaVO.getTitulo());
				ps.setString(x++, configuracaoAcademicaNotaVO.getFormulaCalculo());
				ps.setString(x++, configuracaoAcademicaNotaVO.getFormulaUso());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarComoMediaFinal());
				ps.setString(x++, configuracaoAcademicaNotaVO.getAgrupamentoNota().name());
				ps.setDouble(x++, configuracaoAcademicaNotaVO.getFaixaNotaMenor());
				ps.setDouble(x++, configuracaoAcademicaNotaVO.getFaixaNotaMaior());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getApresentarNota());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarComoSubstitutiva());
				ps.setString(x++, configuracaoAcademicaNotaVO.getPoliticaSubstitutiva());
				ps.setString(x++, configuracaoAcademicaNotaVO.getRegraArredondamentoNota());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getPermiteRealizarRecuperacaoDisciplinaDependencia());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getPermiteRealizarRecuperacaoDisciplinaAdaptacao());
				ps.setString(x++, configuracaoAcademicaNotaVO.getFormulaCalculoVerificaNotaRecuperada());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getApresentarNotaBoletim());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getPermiteReplicarNotaOutraDisciplina());
//				ps.setString(x++, configuracaoAcademicaNotaVO.getTipoUsoNota().name());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				configuracaoAcademicaNotaVO.setNovoObj(false);
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
	}

	private void alterar(final ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO) throws Exception {
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoAcademicoNota SET configuracaoAcademico = ? , nota = ? , notaRecuperacao = ? , qtdeDisciplinaRecuperacao = ? , ");
				sql.append(" utilizarNota = ? , utilizarNotaPorConceito = ? , variavel = ? , titulo = ? , formulaCalculo = ? , formulaUso = ? , utilizarComoMediaFinal = ? , agrupamentoNota = ? , faixaNotaMenor = ? , faixaNotaMaior = ? ,");
				sql.append(" apresentarNota = ? , utilizarComoSubstitutiva = ? , politicaSubstitutiva = ? , regraArredondamentoNota = ?, permiteRealizarRecuperacaoDisciplinaDependencia=?, permiteRealizarRecuperacaoDisciplinaAdaptacao=?,  ");
				sql.append(" formulaCalculoVerificaNotaRecuperada = ?, apresentarNotaBoletim = ?, permiteReplicarNotaOutraDisciplina = ?, tipoUsoNota=?  ");
				sql.append(" WHERE codigo = ?  ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, configuracaoAcademicaNotaVO.getConfiguracaoAcademico().getCodigo());
				ps.setString(x++, configuracaoAcademicaNotaVO.getNota().name());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getNotaRecuperacao());
				ps.setInt(x++, configuracaoAcademicaNotaVO.getQtdeDisciplinaRecuperacao());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarNota());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarNotaPorConceito());
				ps.setString(x++, configuracaoAcademicaNotaVO.getVariavel());
				ps.setString(x++, configuracaoAcademicaNotaVO.getTitulo());
				ps.setString(x++, configuracaoAcademicaNotaVO.getFormulaCalculo());
				ps.setString(x++, configuracaoAcademicaNotaVO.getFormulaUso());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarComoMediaFinal());
				ps.setString(x++, configuracaoAcademicaNotaVO.getAgrupamentoNota().name());
				ps.setDouble(x++, configuracaoAcademicaNotaVO.getFaixaNotaMenor());
				ps.setDouble(x++, configuracaoAcademicaNotaVO.getFaixaNotaMaior());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getApresentarNota());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getUtilizarComoSubstitutiva());
				ps.setString(x++, configuracaoAcademicaNotaVO.getPoliticaSubstitutiva());
				ps.setString(x++, configuracaoAcademicaNotaVO.getRegraArredondamentoNota());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getPermiteRealizarRecuperacaoDisciplinaDependencia());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getPermiteRealizarRecuperacaoDisciplinaAdaptacao());
				ps.setString(x++, configuracaoAcademicaNotaVO.getFormulaCalculoVerificaNotaRecuperada());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getApresentarNotaBoletim());
				ps.setBoolean(x++, configuracaoAcademicaNotaVO.getPermiteReplicarNotaOutraDisciplina());
//				ps.setString(x++, configuracaoAcademicaNotaVO.getTipoUsoNota().name());

				ps.setInt(x++, configuracaoAcademicaNotaVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(configuracaoAcademicaNotaVO);
			return;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void replicarInformacaoConfiguracaoAcademicoParaConfiguracaoAcademicoNota(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		for (int numeroNota = 1; numeroNota <= 40; numeroNota++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + numeroNota)) {
				ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota" + numeroNota + "VO");
				configuracaoAcademicaNotaVO.setAgrupamentoNota( (BimestreEnum) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "bimestreNota" + numeroNota));
				configuracaoAcademicaNotaVO.setUtilizarNota((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + numeroNota));
				configuracaoAcademicaNotaVO.setApresentarNota((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "apresentarNota" + numeroNota));
				configuracaoAcademicaNotaVO.setUtilizarNotaPorConceito((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota"+numeroNota+"PorConceito"));
				configuracaoAcademicaNotaVO.setUtilizarComoMediaFinal((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "nota"+numeroNota+"MediaFinal"));
				configuracaoAcademicaNotaVO.setUtilizarComoSubstitutiva((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarComoSubstitutiva" + numeroNota));
				configuracaoAcademicaNotaVO.setFaixaNotaMaior((Double) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "faixaNota"+numeroNota+"Maior"));
				configuracaoAcademicaNotaVO.setFaixaNotaMenor((Double) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "faixaNota"+numeroNota+"Menor"));
				configuracaoAcademicaNotaVO.setFormulaCalculo((String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "formulaCalculoNota"+numeroNota));
				configuracaoAcademicaNotaVO.setFormulaUso((String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "formulaUsoNota"+numeroNota));
				configuracaoAcademicaNotaVO.setPoliticaSubstitutiva((String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "politicaSubstitutiva"+numeroNota));
				configuracaoAcademicaNotaVO.setRegraArredondamentoNota((String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "regraArredondamentoNota"+numeroNota));
				configuracaoAcademicaNotaVO.setTitulo((String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNotaApresentar"+numeroNota));
				configuracaoAcademicaNotaVO.setVariavel((String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNota"+numeroNota));
				configuracaoAcademicaNotaVO.setConfiguracaoAcademicoNotaConceitoVOs((List<ConfiguracaoAcademicoNotaConceitoVO>) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicoNota"+numeroNota+"ConceitoVOs"));
			}else {
				UtilReflexao.invocarMetodoSetParametroNull(configuracaoAcademicoVO, "configuracaoAcademicaNota" + numeroNota + "VO");
			}
		}

	}

	@Override
	public void incluirConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		replicarInformacaoConfiguracaoAcademicoParaConfiguracaoAcademicoNota(configuracaoAcademicoVO);
		for (int y = 1; y <= 40; y++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + y)) {
				ConfiguracaoAcademicaNotaVO conf = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota" + y + "VO");
				conf.getConfiguracaoAcademico().setCodigo(configuracaoAcademicoVO.getCodigo());
				validarDados(configuracaoAcademicoVO, conf);
				incluir(conf);
			}
		}
	}

	@Override
	public void alterarConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		excluirConfiguracaoAcademicoNotaVOs(configuracaoAcademicoVO);
		replicarInformacaoConfiguracaoAcademicoParaConfiguracaoAcademicoNota(configuracaoAcademicoVO);
		for (int y = 1; y <= 40; y++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + y)) {
				ConfiguracaoAcademicaNotaVO conf = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota" + y + "VO");
				validarDados(configuracaoAcademicoVO, conf);
				conf.getConfiguracaoAcademico().setCodigo(configuracaoAcademicoVO.getCodigo());
				if (conf.getCodigo() > 0) {
					alterar(conf);
				} else {
					incluir(conf);
				}
			}
		}
	}

	@Override
	public void excluirConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ConfiguracaoAcademicoNota  ");
		sql.append(" WHERE configuracaoAcademico = ").append(configuracaoAcademicoVO.getCodigo()).append(" and codigo not in (0");
		for (int y = 1; y <= 40; y++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + y)) {
				ConfiguracaoAcademicaNotaVO conf = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota" + y + "VO");
				sql.append(", ").append(conf.getCodigo());
			}
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public List<ConfiguracaoAcademicaNotaVO> consultarPorConfiguracaoAcademico(Integer configuracaoAcademicoVO) throws Exception {
		StringBuilder sql  =new StringBuilder("SELECT replace(nota, 'NOTA_', '')::INT as ordem, * FROM ConfiguracaoAcademicoNota where configuracaoAcademico = ? order by ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), configuracaoAcademicoVO));
	}

	private List<ConfiguracaoAcademicaNotaVO> montarDadosConsulta(SqlRowSet rs){
		List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs = new ArrayList<ConfiguracaoAcademicaNotaVO>(0);
		while(rs.next()){
			configuracaoAcademicaNotaVOs.add(montarDados(rs));
		}
		return configuracaoAcademicaNotaVOs;
	}

	private ConfiguracaoAcademicaNotaVO montarDados(SqlRowSet rs){
		ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.valueOf(rs.getString("nota")));
		configuracaoAcademicaNotaVO.setNovoObj(false);
		configuracaoAcademicaNotaVO.setCodigo(rs.getInt("codigo"));
		configuracaoAcademicaNotaVO.getConfiguracaoAcademico().setCodigo(rs.getInt("configuracaoAcademico"));
		configuracaoAcademicaNotaVO.setNotaRecuperacao(rs.getBoolean("notaRecuperacao"));
		configuracaoAcademicaNotaVO.setQtdeDisciplinaRecuperacao(rs.getInt("qtdeDisciplinaRecuperacao"));
		configuracaoAcademicaNotaVO.setUtilizarComoMediaFinal(rs.getBoolean("utilizarComoMediaFinal"));
		configuracaoAcademicaNotaVO.setUtilizarComoSubstitutiva(rs.getBoolean("utilizarComoSubstitutiva"));
		configuracaoAcademicaNotaVO.setUtilizarNota(rs.getBoolean("utilizarNota"));
		configuracaoAcademicaNotaVO.setUtilizarNotaPorConceito(rs.getBoolean("utilizarNotaPorConceito"));
		configuracaoAcademicaNotaVO.setPermiteRealizarRecuperacaoDisciplinaAdaptacao(rs.getBoolean("permiteRealizarRecuperacaoDisciplinaAdaptacao"));
		configuracaoAcademicaNotaVO.setPermiteRealizarRecuperacaoDisciplinaDependencia(rs.getBoolean("permiteRealizarRecuperacaoDisciplinaDependencia"));
		configuracaoAcademicaNotaVO.setApresentarNota(rs.getBoolean("apresentarNota"));
		configuracaoAcademicaNotaVO.setApresentarNotaBoletim(rs.getBoolean("apresentarNotaBoletim"));
		configuracaoAcademicaNotaVO.setFormulaCalculoVerificaNotaRecuperada(rs.getString("formulaCalculoVerificaNotaRecuperada"));
		if(rs.getString("agrupamentoNota")!= null){
			configuracaoAcademicaNotaVO.setAgrupamentoNota(BimestreEnum.valueOf(rs.getString("agrupamentoNota")));
		}else{
			configuracaoAcademicaNotaVO.setAgrupamentoNota(BimestreEnum.NAO_CONTROLA);
		}
		configuracaoAcademicaNotaVO.setPoliticaSubstitutiva(rs.getString("politicaSubstitutiva"));
		configuracaoAcademicaNotaVO.setVariavel(rs.getString("variavel"));
		configuracaoAcademicaNotaVO.setTitulo(rs.getString("titulo"));
		configuracaoAcademicaNotaVO.setFormulaCalculo(rs.getString("formulaCalculo"));
		configuracaoAcademicaNotaVO.setFormulaUso(rs.getString("formulaUso"));
		configuracaoAcademicaNotaVO.setRegraArredondamentoNota(rs.getString("regraArredondamentoNota"));
		configuracaoAcademicaNotaVO.setFaixaNotaMaior(rs.getDouble("faixaNotaMaior"));
		configuracaoAcademicaNotaVO.setFaixaNotaMenor(rs.getDouble("faixaNotaMenor"));
		configuracaoAcademicaNotaVO.setPermiteReplicarNotaOutraDisciplina(rs.getBoolean("permiteReplicarNotaOutraDisciplina"));
//		if(rs.getString("tipoUsoNota")!= null){
//			configuracaoAcademicaNotaVO.setTipoUsoNota(TipoUsoNotaEnum.valueOf(rs.getString("tipoUsoNota")));
//		}else{
//			configuracaoAcademicaNotaVO.setTipoUsoNota(TipoUsoNotaEnum.NENHUM);
//		}

		return configuracaoAcademicaNotaVO;
	}


	@Override
	public void carregarDadosConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicoNotaVOs =  consultarPorConfiguracaoAcademico(configuracaoAcademicoVO.getCodigo());
		for(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO: configuracaoAcademicoNotaVOs){
			UtilReflexao.invocarMetodo(configuracaoAcademicoVO, "setConfiguracaoAcademicaNota"+configuracaoAcademicaNotaVO.getNota().getNumeroNota()+"VO", configuracaoAcademicaNotaVO);
		}
	}

	@Override
	public List<ConfiguracaoAcademicaNotaVO> consultarPorConfiguracaoAcademicoNotaRecuperacao(Integer configuracaoAcademicoVO, boolean notaRecuperacao) throws Exception {
		StringBuilder sql  =new StringBuilder("SELECT replace(nota, 'NOTA_', '')::INT as ordem, * FROM ConfiguracaoAcademicoNota where configuracaoAcademico = ? and notaRecuperacao = ? order by ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), configuracaoAcademicoVO, notaRecuperacao));
	}

	@Override
	public List<ConfiguracaoAcademicaNotaVO> consultarPorConfiguracaoAcademicoNotaPermiteLancamentoNota(Integer configuracaoAcademicoVO, boolean notaRecuperacao) throws Exception {
		StringBuilder sql  =new StringBuilder("SELECT replace(nota, 'NOTA_', '')::INT as ordem, * FROM ConfiguracaoAcademicoNota where configuracaoAcademico = ?  ");
		sql.append("and utilizarnota and utilizarcomomediafinal = false and length(formulacalculo) = 0 ");
		if(Uteis.isAtributoPreenchido(notaRecuperacao) && notaRecuperacao){
			sql.append("and notaRecuperacao = true ");
		}
//		if (Uteis.isAtributoPreenchido(tipoUsoNotaEnum)) {
//			sql.append(" and tipousonota = '").append(tipoUsoNotaEnum.name()).append("' ");
//		}
		sql.append(" order by ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), configuracaoAcademicoVO));
	}

	@Override
	public ConfiguracaoAcademicaNotaVO consultarPorConfiguracaoAcademicoPorTipoUsoNotaEnum(ConfiguracaoAcademicoVO configuracaoAcademico) {
		StringBuilder sqlStr  =new StringBuilder("SELECT * FROM ConfiguracaoAcademicoNota where configuracaoAcademico = ?  ");
//		if (Uteis.isAtributoPreenchido(tipoUsoNotaEnum)) {
//			sqlStr.append(" and tipousonota = '").append(tipoUsoNotaEnum.name()).append("' ");
//		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), configuracaoAcademico.getCodigo());
		if (!tabelaResultado.next()) {
			return new ConfiguracaoAcademicaNotaVO();
		}
		return montarDados(tabelaResultado);
	}
	
	@Override
	public List<SelectItem> consultarConfiguracaoAcademicaNotaPorMatriculaDisciplina(String matricula, Integer disciplina) throws Exception{
		List<Object> listaParametrosConsulta = new ArrayList<Object>(0);
		StringBuilder sql =  new StringBuilder(" select distinct configuracaoacademiconota.variavel, configuracaoacademiconota.titulo, configuracaoacademiconota.nota from configuracaoacademiconota where 1=1 ");
		if (Uteis.isAtributoPreenchido(matricula) && Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and configuracaoacademico = (");
			sql.append(" select historico.configuracaoacademico from historico ");
			sql.append(" inner join matricula on matricula.matricula = historico.matricula and historico.matrizcurricular = matricula.gradecurricularatual ");
			sql.append(" where matricula.matricula = ? and historico.disciplina = ? ");
			sql.append(" order by historico.anohistorico desc, historico.semestrehistorico desc, historico.codigo desc limit 1) ");
			listaParametrosConsulta.add(matricula);
			listaParametrosConsulta.add(disciplina);
		}
		sql.append(" and configuracaoacademiconota.utilizarNota and  configuracaoacademiconota.apresentarNotaBoletim and trim(configuracaoacademiconota.formulaCalculo) = '' and configuracaoacademiconota.tipousonota != 'BLACKBOARD' order by configuracaoacademiconota.nota ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaParametrosConsulta.toArray());
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		while (rs.next()) {
			if(lista.stream().noneMatch(l -> l.getValue().equals(rs.getString("variavel")))) {
				lista.add(new SelectItem(rs.getString("variavel"), rs.getString("titulo")));
			}
        }
		return lista;
	}

	@Override
	public List<SelectItem> consultarConfiguracaoAcademicaNotaPorDisciplina(Integer disciplina, String ano, String semestre) throws Exception{
		StringBuilder sql =  new StringBuilder(" select distinct configuracaoacademiconota.variavel, configuracaoacademiconota.titulo, configuracaoacademiconota.nota ");
			sql.append(" from configuracaoacademiconota ");
			sql.append(" inner join configuracaoacademico on configuracaoacademico.codigo = configuracaoacademiconota.configuracaoacademico");
			sql.append(" inner join historico on historico.configuracaoacademico = configuracaoacademico.codigo");
			sql.append(" inner join disciplina on historico.disciplina = disciplina.codigo");
			sql.append(" where 1 = 1 ");
			sql.append(" and configuracaoacademiconota.utilizarNota and configuracaoacademiconota.apresentarNotaBoletim ");
			sql.append(" and trim(configuracaoacademiconota.formulaCalculo) = '' and configuracaoacademiconota.tipousonota != 'BLACKBOARD' ");
			if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and historico.disciplina = ").append(disciplina);
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sql.append(" and historico.anohistorico = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" and historico.semestrehistorico = '").append(semestre).append("' ");
			}
		sql.append(" order by configuracaoacademiconota.nota ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		while (rs.next()) {
			if(lista.stream().noneMatch(l -> l.getValue().equals(rs.getString("variavel")))) {
				lista.add(new SelectItem(rs.getString("variavel"), rs.getString("titulo")));
			}
        }
		return lista;
	}

	@Override
	public ConfiguracaoAcademicaNotaVO consultarPorConfiguracaoAcademicoPorTipoUsoNotaEnum(
			ConfiguracaoAcademicoVO configuracaoAcademico, TipoUsoNotaEnum tipoUsoNotaEnum) {
		// TODO Auto-generated method stub
		return null;
	}
}
