package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ItemCondicaoDescontoRenegociacaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ItemCondicaoDescontoRenegociacao extends ControleAcesso implements ItemCondicaoDescontoRenegociacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7722426184896282784L;
	protected static String idEntidade;

	public ItemCondicaoDescontoRenegociacao() {
		super();
		setIdEntidade("CondicaoDescontoRenegociacao");
	}

	public void validarDados(ItemCondicaoDescontoRenegociacaoVO obj) {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getQuantidadeDiasAtrasoParcela()), "O Campo Quantidade Dias Atraso Parcela ( ItemCondicaoDescontoRenegociacao ) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getQuantidadeDiasAtrasoParcelaFinal()), "O Campo Quantidade Dias Atraso Parcela Final ( ItemCondicaoDescontoRenegociacao ) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNomeUnidadeEnsisnoSelecionadas()), "O Campo Unidade Ensino ( ItemCondicaoDescontoRenegociacao ) deve ser informado para os " + obj.getQuantidadeDiasAtrasoParcela() + " dias atraso parcela até " + obj.getQuantidadeDiasAtrasoParcelaFinal() + " .");
		if(!obj.getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao() && Uteis.isAtributoPreenchido(obj.getPercentualDescontoProgressivo().doubleValue())){
			obj.setPercentualDescontoProgressivo(BigDecimal.ZERO);
		}
		if(!obj.getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao() && Uteis.isAtributoPreenchido(obj.getPercentualDescontoAluno().doubleValue())){
			obj.setPercentualDescontoAluno(BigDecimal.ZERO);
		}
		if(!obj.getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao() && Uteis.isAtributoPreenchido(obj.getPercentualPlanoDesconto().doubleValue())){
			obj.setPercentualPlanoDesconto(BigDecimal.ZERO);
		}
		for (ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue : obj.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs()) {
			getFacadeFactory().getItemCondicaoDescontoRenegociacaoUnidadeEnsinoFacade().validarDados(icdrue);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void ativarItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		SituacaoEnum situacaoAnterior = obj.getSituacao();
		try {
			obj.setSituacao(SituacaoEnum.ATIVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigo()), "A condição desconto renegociação ainda não foi gravada para realizar a ativação da regra.");
			Uteis.checkState(validarAtivacaoItemCondicaoDescontoRenegociacaoVO(obj), "Já existe uma regra Para Desconto Renegociação ativada com essa mesma quantidade de Dias Atraso Parcela e unidade de ensino informada.");
			obj.setResponsavelAlteracaoVO(usuarioVO);
			obj.setDataUltimaAlteracao(new Date());
			if(Uteis.isAtributoPreenchido(obj)) {
				alterar(obj);
			}
		} catch (Exception e) {
			obj.setSituacao(situacaoAnterior);
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inativarItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		SituacaoEnum situacaoAnterior = obj.getSituacao();
		try {
			obj.setSituacao(SituacaoEnum.INATIVO);
			obj.setResponsavelAlteracaoVO(usuarioVO);
			obj.setDataUltimaAlteracao(new Date());
			if(Uteis.isAtributoPreenchido(obj)) {
				alterar(obj);
			}
		} catch (Exception e) {
			obj.setSituacao(situacaoAnterior);
			throw new StreamSeiException(e);
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CondicaoDescontoRenegociacaoVO obj, List<ItemCondicaoDescontoRenegociacaoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(lista, "itemCondicaoDescontoRenegociacao", "condicaoDescontoRenegociacao", obj.getCodigo(), usuarioVO);
		for (ItemCondicaoDescontoRenegociacaoVO icdr : lista) {
			validarDados(icdr);
			if (icdr.getCodigo() == 0) {
				incluir(icdr);
			} else {
				alterar(icdr);
			}
			getFacadeFactory().getItemCondicaoDescontoRenegociacaoUnidadeEnsinoFacade().persistir(icdr, icdr.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(), verificarAcesso, usuarioVO);
		}
	}

	public void incluir(final ItemCondicaoDescontoRenegociacaoVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO ItemCondicaoDescontoRenegociacao( condicaoDescontoRenegociacao, quantidadeDiasAtrasoParcela, juroIsencao, multaIsencao, considerarPlanoDescontoPerdidoDevidoDataAntecipacao," 
		+ "considerarDescontoProgressivoPerdidoDevidoDataAntecipacao, considerarDescontoAlunoPerdidoDevidoDataAntecipacao, situacao, responsavelCriacao, dataCriacao," 
		+ "responsavelAlteracao, dataUltimaAlteracao, quantidadeDiasAtrasoParcelaFinal, " 
		+ "percentualPlanoDesconto, percentualDescontoProgressivo, percentualDescontoAluno )" 
		+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);

					if (obj.getCondicaoDescontoRenegociacaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getCondicaoDescontoRenegociacaoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setInt(2, obj.getQuantidadeDiasAtrasoParcela());
					sqlInserir.setBigDecimal(3, obj.getJuroIsencao());
					sqlInserir.setBigDecimal(4, obj.getMultaIsencao());
					sqlInserir.setBoolean(5, obj.getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao());
					sqlInserir.setBoolean(6, obj.getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao());
					sqlInserir.setBoolean(7, obj.getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao());
					sqlInserir.setString(8, obj.getSituacao().name());
					if (obj.getResponsavelCriacaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getResponsavelCriacaoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					sqlInserir.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getDataCriacao()));
					if (obj.getResponsavelAlteracaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getResponsavelAlteracaoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(obj.getDataUltimaAlteracao()));
					sqlInserir.setInt(13, obj.getQuantidadeDiasAtrasoParcelaFinal());
					int i= 13;
					Uteis.setValuePreparedStatement(obj.getPercentualPlanoDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPercentualDescontoProgressivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPercentualDescontoAluno(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
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

	public void alterar(final ItemCondicaoDescontoRenegociacaoVO obj) throws Exception {
		try {
			final String sql = "UPDATE ItemCondicaoDescontoRenegociacao set condicaoDescontoRenegociacao=?, quantidadeDiasAtrasoParcela=?, juroIsencao=?, multaIsencao=?, considerarPlanoDescontoPerdidoDevidoDataAntecipacao=?," 
		+ "considerarDescontoProgressivoPerdidoDevidoDataAntecipacao=?, considerarDescontoAlunoPerdidoDevidoDataAntecipacao=?, situacao=?, responsavelCriacao=?, dataCriacao=?," 
					+ "responsavelAlteracao=?, dataUltimaAlteracao=?, quantidadeDiasAtrasoParcelaFinal=?, "
		            + "percentualPlanoDesconto=?, percentualDescontoProgressivo=?, percentualDescontoAluno=? "
					+ " WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getCondicaoDescontoRenegociacaoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getCondicaoDescontoRenegociacaoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getQuantidadeDiasAtrasoParcela());
					sqlAlterar.setBigDecimal(3, obj.getJuroIsencao());
					sqlAlterar.setBigDecimal(4, obj.getMultaIsencao());
					sqlAlterar.setBoolean(5, obj.getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao());
					sqlAlterar.setBoolean(6, obj.getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao());
					sqlAlterar.setBoolean(7, obj.getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao());
					sqlAlterar.setString(8, obj.getSituacao().name());
					if (obj.getResponsavelCriacaoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getResponsavelCriacaoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					sqlAlterar.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getDataCriacao()));
					if (obj.getResponsavelAlteracaoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getResponsavelAlteracaoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setTimestamp(12, Uteis.getDataJDBCTimestamp(obj.getDataUltimaAlteracao()));
					sqlAlterar.setInt(13, obj.getQuantidadeDiasAtrasoParcelaFinal());
					int i =13;
					Uteis.setValuePreparedStatement(obj.getPercentualPlanoDesconto(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPercentualDescontoProgressivo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPercentualDescontoAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT icdr.codigo as \"icdr.codigo\",  ");
		sql.append(" icdr.condicaodescontorenegociacao as \"icdr.condicaodescontorenegociacao\",  ");
		sql.append(" icdr.juroisencao as \"icdr.juroisencao\", ");
		sql.append(" icdr.multaisencao as \"icdr.multaisencao\", ");
		sql.append(" icdr.quantidadediasatrasoparcela as \"icdr.quantidadediasatrasoparcela\", ");
		sql.append(" icdr.quantidadediasatrasoparcelaFinal as \"icdr.quantidadediasatrasoparcelaFinal\", ");
		sql.append(" icdr.considerarplanodescontoperdidodevidodataantecipacao as \"icdr.considerarplanodescontoperdidodevidodataantecipacao\", ");
		sql.append(" icdr.considerardescontoprogressivoperdidodevidodataantecipacao as \"icdr.considerardescontoprogressivoperdidodevidodataantecipacao\", ");
		sql.append(" icdr.considerardescontoalunoperdidodevidodataantecipacao as \"icdr.considerardescontoalunoperdidodevidodataantecipacao\", ");
		sql.append(" icdr.percentualPlanoDesconto as \"icdr.percentualPlanoDesconto\", ");
		sql.append(" icdr.percentualDescontoProgressivo as \"icdr.percentualDescontoProgressivo\", ");
		sql.append(" icdr.percentualDescontoAluno as \"icdr.percentualDescontoAluno\", ");
		sql.append(" icdr.situacao as \"icdr.situacao\", icdr.responsavelcriacao as \"icdr.responsavelcriacao\", ");
		sql.append(" icdr.datacriacao as \"icdr.datacriacao\", icdr.responsavelalteracao as \"icdr.responsavelalteracao\", ");
		sql.append(" icdr.dataultimaalteracao  as \"icdr.dataultimaalteracao\", ");
		sql.append(" condicaoDescontoRenegociacao.codigo as \"condicaoDescontoRenegociacao.codigo\", ");
		sql.append(" condicaoDescontoRenegociacao.descricao as \"condicaoDescontoRenegociacao.descricao\" ");
		sql.append(" FROM itemcondicaodescontorenegociacao as icdr ");
		sql.append(" inner join condicaoDescontoRenegociacao on condicaoDescontoRenegociacao.codigo = icdr.condicaoDescontoRenegociacao ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ItemCondicaoDescontoRenegociacaoVO> consultarItemCondicaoDescontoRenegociacaoPorCondicaoDescontoRenegociacao(Integer condicaoDescontoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemCondicaoDescontoRenegociacao.consultar(getIdEntidade());
		List<ItemCondicaoDescontoRenegociacaoVO> objetos = new ArrayList<>();
		String sql = "SELECT * FROM ItemCondicaoDescontoRenegociacao WHERE condicaoDescontoRenegociacao = " + condicaoDescontoRenegociacao;
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (resultado.next()) {
			ItemCondicaoDescontoRenegociacaoVO novoObj = new ItemCondicaoDescontoRenegociacaoVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuarioVO);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ItemCondicaoDescontoRenegociacaoVO consultarItemCondicaoDescontoRenegociacaoDisponivel(Long nrDiasAtraso, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSQLPadraoConsultaBasica());
		sb.append(" inner join itemCondicaoDescontoRenegociacaounidadeensino on itemCondicaoDescontoRenegociacaounidadeensino.itemCondicaoDescontoRenegociacao = icdr.codigo ");
		sb.append(" WHERE ").append(nrDiasAtraso).append(" between icdr.quantidadeDiasAtrasoParcela and  icdr.quantidadeDiasAtrasoParcelaFinal ");
		sb.append(" and icdr.situacao = '").append(SituacaoEnum.ATIVO.name()).append("' ");
		sb.append(" and itemCondicaoDescontoRenegociacaounidadeensino.unidadeensino in ( ").append(unidadeEnsino).append(" ) ");
		sb.append(" limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new ItemCondicaoDescontoRenegociacaoVO();
		}
		return (montarDadosBasico(tabelaResultado, usuarioLogado));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ItemCondicaoDescontoRenegociacaoVO consultarItemCondicaoDescontoRenegociacaoPorNegociacaoContaReceber(Integer negociacaoContaReceber, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSQLPadraoConsultaBasica());
		sb.append(" inner join negociacaocontareceber on negociacaocontareceber.itemCondicaoDescontoRenegociacao = icdr.codigo ");
		sb.append(" WHERE negociacaocontareceber.codigo = ").append(negociacaoContaReceber);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new ItemCondicaoDescontoRenegociacaoVO();
		}
		return (montarDadosBasico(tabelaResultado, usuarioLogado));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ItemCondicaoDescontoRenegociacaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCondicaoDescontoRenegociacao.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ItemCondicaoDescontoRenegociacao WHERE codigo = " + codigoPrm;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ItemCondicaoDescontoRenegociacao ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	private boolean validarAtivacaoItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT itemCondicaoDescontoRenegociacao.codigo FROM itemCondicaoDescontoRenegociacao ");
		sql.append(" inner join itemCondicaoDescontoRenegociacaounidadeensino on itemCondicaoDescontoRenegociacaounidadeensino.itemCondicaoDescontoRenegociacao = itemCondicaoDescontoRenegociacao.codigo ");
		sql.append(" WHERE ((").append(obj.getQuantidadeDiasAtrasoParcela()).append(" between itemCondicaoDescontoRenegociacao.quantidadeDiasAtrasoParcela and itemCondicaoDescontoRenegociacao.quantidadeDiasAtrasoParcelaFinal) ");
		sql.append(" or (").append(obj.getQuantidadeDiasAtrasoParcelaFinal()).append(" between itemCondicaoDescontoRenegociacao.quantidadeDiasAtrasoParcela and itemCondicaoDescontoRenegociacao.quantidadeDiasAtrasoParcelaFinal))");
		sql.append(" and itemCondicaoDescontoRenegociacao.situacao = '").append(SituacaoEnum.ATIVO.name()).append("' ");
		sql.append(" and itemCondicaoDescontoRenegociacaounidadeensino.unidadeensino in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(obj.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(), "unidadeEnsinoVO.codigo")).append(") ");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and itemCondicaoDescontoRenegociacao.codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	public ItemCondicaoDescontoRenegociacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemCondicaoDescontoRenegociacaoVO obj = new ItemCondicaoDescontoRenegociacaoVO();
		obj.setNovoObj((false));
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCondicaoDescontoRenegociacaoVO().setCodigo(dadosSQL.getInt("condicaoDescontoRenegociacao"));
		obj.setQuantidadeDiasAtrasoParcela(dadosSQL.getInt("quantidadeDiasAtrasoParcela"));
		obj.setQuantidadeDiasAtrasoParcelaFinal(dadosSQL.getInt("quantidadeDiasAtrasoParcelaFinal"));
		obj.setJuroIsencao(dadosSQL.getBigDecimal("juroIsencao"));
		obj.setMultaIsencao(dadosSQL.getBigDecimal("multaIsencao"));
		obj.setConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao(dadosSQL.getBoolean("considerarPlanoDescontoPerdidoDevidoDataAntecipacao"));
		obj.setConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao(dadosSQL.getBoolean("considerarDescontoProgressivoPerdidoDevidoDataAntecipacao"));
		obj.setConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao(dadosSQL.getBoolean("considerarDescontoAlunoPerdidoDevidoDataAntecipacao"));
		obj.setPercentualPlanoDesconto(dadosSQL.getBigDecimal("percentualPlanoDesconto"));
		obj.setPercentualDescontoProgressivo(dadosSQL.getBigDecimal("percentualDescontoProgressivo"));
		obj.setPercentualDescontoAluno(dadosSQL.getBigDecimal("percentualDescontoAluno"));
		obj.setSituacao(SituacaoEnum.valueOf(dadosSQL.getString("situacao")));
		obj.getResponsavelCriacaoVO().setCodigo(dadosSQL.getInt("responsavelCriacao"));
		obj.setDataCriacao(dadosSQL.getDate("dataCriacao"));
		obj.getResponsavelAlteracaoVO().setCodigo(dadosSQL.getInt("responsavelAlteracao"));
		obj.setDataUltimaAlteracao(dadosSQL.getDate("dataUltimaAlteracao"));
		obj.setItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(getFacadeFactory().getItemCondicaoDescontoRenegociacaoUnidadeEnsinoFacade().consultarItemCondicaoDescontoRenegociacaoUnidadeEnsinoPoritemCondicaoDescontoRenegociacao(obj.getCodigo(), nivelMontarDados, usuarioVO));
		obj.preencherNomeUnidadeEnsisnoSelecionadas();
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setResponsavelCriacaoVO(Uteis.montarDadosVO(dadosSQL.getInt("responsavelCriacao"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO)));
		obj.setResponsavelAlteracaoVO(Uteis.montarDadosVO(dadosSQL.getInt("responsavelAlteracao"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO)));

		return obj;
	}
	
	public ItemCondicaoDescontoRenegociacaoVO montarDadosBasico(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		ItemCondicaoDescontoRenegociacaoVO obj = new ItemCondicaoDescontoRenegociacaoVO();
		obj.setNovoObj((false));
		obj.setCodigo((dadosSQL.getInt("icdr.codigo")));
		obj.setQuantidadeDiasAtrasoParcela(dadosSQL.getInt("icdr.quantidadeDiasAtrasoParcela"));
		obj.setQuantidadeDiasAtrasoParcelaFinal(dadosSQL.getInt("icdr.quantidadeDiasAtrasoParcelaFinal"));
		obj.setJuroIsencao(dadosSQL.getBigDecimal("icdr.juroIsencao"));
		obj.setMultaIsencao(dadosSQL.getBigDecimal("icdr.multaIsencao"));
		obj.setConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao(dadosSQL.getBoolean("icdr.considerarplanodescontoperdidodevidodataantecipacao"));
		obj.setConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao(dadosSQL.getBoolean("icdr.considerardescontoprogressivoperdidodevidodataantecipacao"));
		obj.setConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao(dadosSQL.getBoolean("icdr.considerardescontoalunoperdidodevidodataantecipacao"));
		obj.setPercentualPlanoDesconto(dadosSQL.getBigDecimal("icdr.percentualPlanoDesconto"));
		obj.setPercentualDescontoProgressivo(dadosSQL.getBigDecimal("icdr.percentualDescontoProgressivo"));
		obj.setPercentualDescontoAluno(dadosSQL.getBigDecimal("icdr.percentualDescontoAluno"));
		obj.setSituacao(SituacaoEnum.valueOf(dadosSQL.getString("icdr.situacao")));
		obj.getResponsavelCriacaoVO().setCodigo(dadosSQL.getInt("icdr.responsavelCriacao"));
		obj.setDataCriacao(dadosSQL.getDate("icdr.dataCriacao"));
		obj.getResponsavelAlteracaoVO().setCodigo(dadosSQL.getInt("icdr.responsavelAlteracao"));
		obj.setDataUltimaAlteracao(dadosSQL.getDate("icdr.dataUltimaAlteracao"));
		obj.getCondicaoDescontoRenegociacaoVO().setCodigo(dadosSQL.getInt("condicaoDescontoRenegociacao.codigo"));
		obj.getCondicaoDescontoRenegociacaoVO().setDescricao(dadosSQL.getString("condicaoDescontoRenegociacao.descricao"));
		return obj;
	}

	public static String getIdEntidade() {
		return ItemCondicaoDescontoRenegociacao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ItemCondicaoDescontoRenegociacao.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ItemCondicaoDescontoRenegociacaoVO inicializarDadosCondicaoRenegociacaoNovo(UsuarioVO usuario) throws Exception {
		ItemCondicaoDescontoRenegociacaoVO obj = new ItemCondicaoDescontoRenegociacaoVO();
		obj.setResponsavelCriacaoVO(usuario);
		obj.setDataCriacao(new Date());
		obj.setResponsavelAlteracaoVO(usuario);
		obj.setDataUltimaAlteracao(new Date());
		obj.setSituacao(SituacaoEnum.EM_CONSTRUCAO);
		obj.setNovoObj(Boolean.TRUE);
		return obj;
	}

	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(CondicaoDescontoRenegociacaoVO cdr, ItemCondicaoDescontoRenegociacaoVO icre, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue) {
		icdrue.setItemCondicaoDescontoRenegociacaoVO(icre);
		for (ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrExistente : icre.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs()) {
			if (icdrExistente.getUnidadeEnsinoVO().getCodigo().equals(icdrue.getUnidadeEnsinoVO().getCodigo())) {
				return;
			}
		}
		icre.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs().add(icdrue);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(ItemCondicaoDescontoRenegociacaoVO obj, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue) {
		Iterator<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> i = obj.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs().iterator();
		while (i.hasNext()) {
			ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrExistente = i.next();
			if (icdrExistente.getUnidadeEnsinoVO().getCodigo().equals(icdrue.getUnidadeEnsinoVO().getCodigo())) {
				i.remove();
			}
		}
	}

}