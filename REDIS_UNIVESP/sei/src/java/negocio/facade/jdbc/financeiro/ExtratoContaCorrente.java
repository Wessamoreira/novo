package negocio.facade.jdbc.financeiro;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ExtratoContaCorrenteInterfaceFacade;


@Repository
@Lazy
public class ExtratoContaCorrente extends ControleAcesso implements ExtratoContaCorrenteInterfaceFacade, Serializable {

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ExtratoContaCorrenteVO obj, UsuarioVO usuarioVO) throws Exception {
		try {			
			validarDados(obj);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuarioVO);
			final String sqlIncluir = "INSERT INTO ExtratoContaCorrente( valor, data, origemExtratoContaCorrente, tipoMovimentacaoFinanceira, codigoOrigem," 
			+ " codigoCheque, sacadoCheque, numeroCheque, bancoCheque, contaCorrenteCheque," 
			+ " agenciaCheque, dataPrevisaoCheque, nomeSacado, codigoSacado, tipoSacado, " 
			+ " contaCorrente, unidadeEnsino, responsavel, formaPagamento, formapagamentonegociacaorecebimento, operadoraCartao, desconsiderarConciliacaoBancaria, valorTaxaBancaria ) " 
			+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sql = arg0.prepareStatement(sqlIncluir);
					sql.setDouble(x++, obj.getFormaPagamento().isCartaoDebito() ? obj.getValorSemTaxaBancaria() : obj.getValor());
					sql.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getData()));
					sql.setString(x++, obj.getOrigemExtratoContaCorrente().toString());
					sql.setString(x++, obj.getTipoMovimentacaoFinanceira().toString());
					if(Uteis.isAtributoPreenchido(obj.getCodigoOrigem())) {
						sql.setInt(x++, obj.getCodigoOrigem());
					}else {
						sql.setNull(x++, 0);
					}
					if (obj.getCodigoCheque() != null && obj.getCodigoCheque() > 0) {
						sql.setInt(x++, obj.getCodigoCheque());
						sql.setString(x++, obj.getSacadoCheque());
						sql.setString(x++, obj.getNumeroCheque());
						sql.setString(x++, obj.getBancoCheque());
						sql.setString(x++, obj.getContaCorrenteCheque());
						sql.setString(x++, obj.getAgenciaCheque());
						sql.setDate(x++, Uteis.getDataJDBC(obj.getDataPrevisaoCheque()));
					} else {
						sql.setNull(x++, 0);
						sql.setNull(x++, 0);
						sql.setNull(x++, 0);
						sql.setNull(x++, 0);
						sql.setNull(x++, 0);
						sql.setNull(x++, 0);
						sql.setNull(x++, 0);
					}
					sql.setString(x++, obj.getNomeSacado());
					if(Uteis.isAtributoPreenchido(obj.getCodigoSacado())) {
						sql.setInt(x++, obj.getCodigoSacado());
					}else {
						sql.setNull(x++, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getTipoSacado())) {
						sql.setString(x++, obj.getTipoSacado().toString());
					}else {
						sql.setNull(x++, 0);
					}
					sql.setInt(x++, obj.getContaCorrente().getCodigo());
					if (obj.getUnidadeEnsino() != null && obj.getUnidadeEnsino().getCodigo() != null && obj.getUnidadeEnsino().getCodigo() > 0) {
						sql.setInt(x++, obj.getUnidadeEnsino().getCodigo());
					} else {
						sql.setNull(x++, 0);
					}					
					sql.setInt(x++, obj.getResponsavel().getCodigo());
					sql.setInt(x++, obj.getFormaPagamento().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoRecebimentoVO())){
						sql.setInt(x++, obj.getFormaPagamentoNegociacaoRecebimentoVO().getCodigo());
					}else{
						sql.setNull(x++, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())){
						sql.setInt(x++, obj.getOperadoraCartaoVO().getCodigo());
					}else{
						sql.setNull(x++, 0);
					}
					sql.setBoolean(x++, obj.isDesconsiderarConciliacaoBancaria());
					sql.setDouble(x++, obj.getValorTaxaBancaria());
					return sql;
				}
			}, new ResultSetExtractor<Integer>() {

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

	/**
     * 
     */
	private static final long serialVersionUID = 3781920416436843499L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoExtratoContaCorrente(Double valor, Date data, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, Integer codigoOrigem, ChequeVO cheque, String nomeSacado, Integer codigoSacado, TipoSacadoExtratoContaCorrenteEnum tipoSacado, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, FormaPagamentoVO formaPagamento, ContaCorrenteVO contaCorrente, UnidadeEnsinoVO unidadeEnsino, OperadoraCartaoVO operadoraCartaoVO, boolean desconsiderarConciliacaoBancaria, Double valorTaxaBancaria,  Boolean bloqueioPorFechamentoMesLiberado, UsuarioVO responsavel) throws Exception {
		ExtratoContaCorrenteVO obj = new ExtratoContaCorrenteVO();
		if(bloqueioPorFechamentoMesLiberado) {
			obj.liberarVerificacaoBloqueioFechamentoMes();	
		}
		obj.setValor(valor);
		obj.setValorTaxaBancaria(valorTaxaBancaria);
		obj.setData(data);
		obj.setOrigemExtratoContaCorrente(origemExtratoContaCorrente);
		obj.setTipoMovimentacaoFinanceira(tipoMovimentacaoFinanceira);
		obj.setDesconsiderarConciliacaoBancaria(desconsiderarConciliacaoBancaria);
		obj.setCodigoOrigem(codigoOrigem);
		obj.setNomeSacado(nomeSacado);
		obj.setCodigoSacado(codigoSacado);
		obj.setTipoSacado(tipoSacado);
		obj.setFormaPagamento(formaPagamento);
		obj.setFormaPagamentoNegociacaoRecebimentoVO(fpnrVO);
		obj.getContaCorrente().setCodigo(contaCorrente.getCodigo());
		if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
			obj.getUnidadeEnsino().setCodigo(unidadeEnsino.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(operadoraCartaoVO)){
			obj.setOperadoraCartaoVO(operadoraCartaoVO);
		}
		obj.getResponsavel().setCodigo(responsavel.getCodigo());
		if (cheque != null && cheque.getCodigo() > 0) {
			obj.setCodigoCheque(cheque.getCodigo());
			obj.setSacadoCheque(cheque.getSacado());
			obj.setBancoCheque(cheque.getBanco());
			obj.setContaCorrenteCheque(cheque.getNumeroContaCorrente());
			obj.setAgenciaCheque(cheque.getAgencia());
			obj.setNumeroCheque(cheque.getNumero());
			obj.setDataPrevisaoCheque(cheque.getDataPrevisao());

		}
		incluir(obj, responsavel);

	}

	private void validarDados(ExtratoContaCorrenteVO obj) throws Exception {
		if (obj.getNomeSacado().equals("")) {
			throw new Exception("O campo NOME SACADO deve ser informado.");
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFormaPagamento().getTipo()), "O campo Tipo da Forma Pagamento deve ser informado.");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaExtratoContaCorrente(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			sqlStr.append(" UPDATE extratocontacorrente SET codigosacado = ").append(contaReceberVO.getPessoa().getCodigo());
			sqlStr.append(", nomesacado = '").append(contaReceberVO.getPessoa().getNome()).append("' ");
			sqlStr.append(", tiposacado = '").append(TipoSacadoExtratoContaCorrenteEnum.ALUNO.name()).append("' ");
		} else {
			sqlStr.append(" UPDATE extratocontacorrente SET codigosacado = ").append(contaReceberVO.getResponsavelFinanceiro().getCodigo());
			sqlStr.append(", nomesacado = '").append(contaReceberVO.getResponsavelFinanceiro().getNome()).append("' ");
			sqlStr.append(", tiposacado = '").append(TipoSacadoExtratoContaCorrenteEnum.RESPONSAVEL_FINANCEIRO.name()).append("' ");
		}
		sqlStr.append(" WHERE codigo IN ( ");
		sqlStr.append(" 	SELECT extratocontacorrente.codigo FROM contarecebernegociacaorecebimento ");
		sqlStr.append(" 	INNER JOIN extratocontacorrente ON origemextratocontacorrente = 'RECEBIMENTO' AND codigoorigem = negociacaorecebimento ");
		sqlStr.append(" 	WHERE contareceber = ").append(contaReceberVO.getCodigo());
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaExtratoVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE extratocontacorrente SET conciliacaocontacorrentediaextrato = ").append(obj.getCodigo());
		sqlStr.append(" WHERE codigo in ( ").append(obj.getCodigoSei()).append(") ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularExtratoContaCorrentePorConciliacaoContaCorrenteQueNaoFazemMaisParte(ConciliacaoContaCorrenteVO obj,  UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(" ");
		sb.append(" UPDATE extratocontacorrente SET conciliacaocontacorrentediaextrato = null  ");
		sb.append(" WHERE conciliacaocontacorrentediaextrato = any ( ");
		sb.append(" (select array_agg(conciliacaocontacorrentediaextrato.codigo::text)");
		sb.append(" from conciliacaocontacorrentedia ");
		sb.append(" inner join conciliacaocontacorrentediaextrato on conciliacaocontacorrentediaextrato.conciliacaocontacorrentedia = conciliacaocontacorrentedia.codigo ");
		sb.append(" where conciliacaocontacorrentedia.conciliacaocontacorrente = ").append(obj.getCodigo());
		sb.append(" and conciliacaocontacorrentedia.codigo not in ( 0  ");
		obj.getListaConciliacaoContaCorrenteDia().stream().filter(p-> Uteis.isAtributoPreenchido(p.getCodigo())).forEach(p-> sb.append(", ").append(p.getCodigo()));
		sb.append(" ))::int[]) ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sb.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularExtratoContaCorrentePorConciliacaoContaCorrenteDiaQueNaoFazemMaisParte(ConciliacaoContaCorrenteDiaVO objDia ,  UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(" ");
		sb.append(" UPDATE extratocontacorrente SET conciliacaocontacorrentediaextrato = null  ");
		sb.append(" WHERE conciliacaocontacorrentediaextrato = any ( ");
		sb.append(" (select array_agg(cccde.codigo::text)");
		sb.append(" from conciliacaocontacorrentediaextrato as cccde ");
		sb.append(" where cccde.conciliacaocontacorrentedia = ").append(objDia.getCodigo());
		sb.append(" and cccde.codigo not in ( 0  ");
		objDia.getListaConciliacaoContaCorrenteExtrato().stream().filter(p-> Uteis.isAtributoPreenchido(p.getCodigo())).forEach(p-> sb.append(", ").append(p.getCodigo()));
		sb.append(" ))::int[]) ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sb.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularConciliacaoContaCorrenteDiaExtratoPorConciliacaoExtratoDia(String listaCodigo, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE extratocontacorrente SET conciliacaocontacorrentediaextrato = null  ");
		sqlStr.append(" WHERE conciliacaocontacorrentediaextrato in ( ").append(listaCodigo).append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularConciliacaoContaCorrenteDiaExtratoPorCodigoExtrato(String listaCodigo, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE extratocontacorrente SET conciliacaocontacorrentediaextrato = null  ");
		sqlStr.append(" WHERE codigo in ( ").append(listaCodigo).append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	
	private void atualizarConciliacaoContaCorrenteDiaExtratoParaEstorno(Integer codigo, boolean desconsiderarConciliacaoBancaria, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE extratocontacorrente SET conciliacaocontacorrentediaextrato = null,  ");
		sqlStr.append(" desconsiderarConciliacaoBancaria =  ").append(desconsiderarConciliacaoBancaria).append(" ");
		sqlStr.append(" WHERE codigo =  ").append(codigo).append("  ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularExtratoContaCorrentePorConciliacaoContaCorrente(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(obj.getListaConciliacaoContaCorrenteDia())){
			StringBuilder sb = new StringBuilder(" update extratocontacorrente set conciliacaocontacorrentediaextrato = null where conciliacaocontacorrentediaextrato in (");	
			sb.append(" select codigo FROM ConciliacaoContaCorrenteDiaExtrato where ConciliacaoContaCorrenteDia in ( ");
			sb.append(" select codigo FROM ConciliacaoContaCorrenteDia where conciliacaoContaCorrente =  ").append(obj.getCodigo());
			sb.append(")) ");
			sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().execute(sb.toString());
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum origem, Integer codigoOrigem, boolean desconsiderarConciliacaoBancaria, Integer cheque, boolean alterarSomenteUltimoRegistro, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT codigo, conciliacaocontacorrentediaextrato  from  extratocontacorrente ");
		sb.append(" where origemextratocontacorrente='").append(origem.name()).append("'  ");		
		sb.append(" and desconsiderarConciliacaoBancaria  =  false ");
		if (origem.isCompensacaoCheque()) {
			sb.append(" AND tipomovimentacaofinanceira = 'ENTRADA' ");
			if (Uteis.isAtributoPreenchido(cheque)) {
				sb.append(" AND codigocheque = ").append(cheque);
			}
		}
		if(origem.isCompensacaoCartao()) {
			sb.append(" and formapagamentonegociacaorecebimento = ").append(codigoOrigem).append("  ");
		}else {
			sb.append(" and codigoorigem = ").append(codigoOrigem).append("  ");	
		}
		if (alterarSomenteUltimoRegistro) {
			sb.append(" ORDER BY codigo DESC LIMIT 1 ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Uteis.checkState(origem.isCompensacaoCartao() && Uteis.isQtdLinhaSql(rs) > 1, "Não foi possível realizar essa operação por favor entra em contato com Administrador. (Mapapendencia de cartao existe mais de 1 resultado para essa operação) ");
		while (rs.next()) {
			atualizarConciliacaoContaCorrenteDiaExtratoParaEstorno(rs.getInt("codigo"), desconsiderarConciliacaoBancaria, usuario);
			if(Uteis.isAtributoPreenchido(rs.getInt("conciliacaocontacorrentediaextrato"))){
				getFacadeFactory().getConciliacaoContaCorrenteDiaExtratoInterfaceFacade().atualizarConciliacaoContaCorrenteDiaExtratoCampoCodigoSei(rs.getInt("conciliacaocontacorrentediaextrato"), usuario);
			}
		}
	}
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(ExtratoContaCorrenteVO extrato, UsuarioVO usuario) throws Exception {
		atualizarConciliacaoContaCorrenteDiaExtratoParaEstorno(extrato.getCodigo(), true, usuario);
		if(Uteis.isAtributoPreenchido(extrato.getConciliacaoContaCorrenteDiaExtratoVO().getCodigo())){
			getFacadeFactory().getConciliacaoContaCorrenteDiaExtratoInterfaceFacade().atualizarConciliacaoContaCorrenteDiaExtratoCampoCodigoSei(extrato.getConciliacaoContaCorrenteDiaExtratoVO().getCodigo(), usuario);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Double consultarSaldoAnterior(String numeroContaCorrente , String digitoContaCorrente, Date dataInicio, UsuarioVO usuario) throws Exception {
		if (dataInicio == null) {
			return 0.0;
		}
		StringBuilder sql = new StringBuilder("");
		sql.append(" select (sum(entrada) - sum(saida)) AS total from ( ");
		sql.append(" select contaCorrente, case when tipomovimentacaofinanceira = 'ENTRADA' then sum(valor-(case when valortaxabancaria is null then 0.0 else valortaxabancaria end)) else  0 end as entrada, ");
		sql.append(" case when tipomovimentacaofinanceira = 'SAIDA' then sum(valor-(case when valortaxabancaria is null then 0.0 else valortaxabancaria end)) else 0 end as saida ");
		sql.append(" from extratocontacorrente   ");
		sql.append(" INNER JOIN contacorrente ON extratocontacorrente.contacorrente = contacorrente.codigo ");
		sql.append(" inner join agencia on contacorrente.agencia = agencia.codigo ");
		sql.append(" where extratocontacorrente.data < '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio))).append("' ");
		sql.append(" and (extratocontacorrente.desconsiderarconciliacaobancaria is null or extratocontacorrente.desconsiderarconciliacaobancaria = false )");
		
		sql.append(" and ( trim(leading '0' from contacorrente.numero) = '").append(StringUtils.stripStart(numeroContaCorrente, "0")).append("' ");
		sql.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(StringUtils.stripStart(numeroContaCorrente, "0")).append("' ");
		sql.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(StringUtils.stripStart(numeroContaCorrente, "0")).append("' ");
		sql.append(" ) ");
		
		if(digitoContaCorrente != null && !digitoContaCorrente.isEmpty()){
			sql.append(" and contacorrente.digito = '").append(digitoContaCorrente).append("' ");	
		}
		sql.append(" and contacorrente.tipoContaCorrente = '").append(TipoContaCorrenteEnum.CORRENTE).append("' ");
		sql.append(" group by extratocontacorrente.contaCorrente, extratocontacorrente.tipomovimentacaofinanceira ");
		sql.append(" ) as t group by contacorrente ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getDouble("total");
		}
		return 0.0;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Double consultarSaldoPorConciliacaoContaCorrentePorDataMovimentacao(ConciliacaoContaCorrenteVO obj, Date data, UsuarioVO usuario) throws Exception {		
		StringBuilder sql = new StringBuilder("");
		sql.append(" select (sum(entrada) - sum(saida)) AS total from ( ");
		sql.append(" select case when tipomovimentacaofinanceira = 'ENTRADA' then sum(valor-(case when valortaxabancaria is null then 0.0 else valortaxabancaria end)) else  0 end as entrada, ");
		sql.append(" case when tipomovimentacaofinanceira = 'SAIDA' then sum(valor-(case when valortaxabancaria is null then 0.0 else valortaxabancaria end)) else 0 end as saida ");
		sql.append(" from extratocontacorrente   ");
		sql.append(" inner join conciliacaocontacorrentediaextrato cccde on cccde.codigo = extratocontacorrente.conciliacaocontacorrentediaextrato ");
		sql.append(" inner join conciliacaocontacorrentedia cccd on cccde.conciliacaocontacorrentedia = cccd.codigo ");
		sql.append(" where cccd.conciliacaocontacorrente  = ").append(obj.getCodigo());
		sql.append(" and cccd.data ='").append(Uteis.getDataJDBC(data)).append("' ");
		sql.append(" group by extratocontacorrente.tipomovimentacaofinanceira ");
		sql.append(" ) as t  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getDouble("total");
		}
		return 0.0;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ExtratoContaCorrenteVO consultarCodigoOrigemExtratoContaCorrente(Integer codigo, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select  codigo, codigoorigem, origemextratocontacorrente, tipomovimentacaofinanceira, conciliacaocontacorrentediaextrato  from extratocontacorrente  where codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			ExtratoContaCorrenteVO obj = new ExtratoContaCorrenteVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setCodigoOrigem(rs.getInt("codigoorigem"));
			obj.getConciliacaoContaCorrenteDiaExtratoVO().setCodigo(rs.getInt("conciliacaocontacorrentediaextrato"));
			obj.setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.valueOf(rs.getString("origemextratocontacorrente")));
			obj.setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira.valueOf(rs.getString("tipomovimentacaofinanceira")));
			return obj;
		}
		throw new Exception("Não foi encontrado um extrato conta corrente de código " + codigo);
	}

	
	private StringBuilder getSQLPadraoConsultaProcessamentoArquivoOFX() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT extratocontacorrente.codigo as \"extratocontacorrente.codigo\", extratocontacorrente.valor as \"extratocontacorrente.valor\", extratocontacorrente.data as \"extratocontacorrente.data\",  ");
		sb.append(" extratocontacorrente.origemextratocontacorrente as \"extratocontacorrente.origemextratocontacorrente\", extratocontacorrente.tipomovimentacaofinanceira as \"extratocontacorrente.tipomovimentacaofinanceira\",  ");
		sb.append(" extratocontacorrente.codigoorigem as \"extratocontacorrente.codigoorigem\", extratocontacorrente.codigocheque as \"extratocontacorrente.codigocheque\", extratocontacorrente.sacadocheque as \"extratocontacorrente.sacadocheque\",  ");
		sb.append(" extratocontacorrente.numerocheque as \"extratocontacorrente.numerocheque\", extratocontacorrente.bancocheque as \"extratocontacorrente.bancocheque\",  ");
		sb.append(" extratocontacorrente.contacorrentecheque as \"extratocontacorrente.contacorrentecheque\", extratocontacorrente.agenciacheque as \"extratocontacorrente.agenciacheque\", ");
		sb.append(" extratocontacorrente.dataprevisaocheque as \"extratocontacorrente.dataprevisaocheque\", extratocontacorrente.nomesacado as \"extratocontacorrente.nomesacado\", ");
		sb.append(" extratocontacorrente.codigosacado as \"extratocontacorrente.codigosacado\", extratocontacorrente.tiposacado as \"extratocontacorrente.tiposacado\",  ");
		sb.append(" extratocontacorrente.contacorrente as \"extratocontacorrente.contacorrente\", extratocontacorrente.unidadeensino as \"extratocontacorrente.unidadeensino\", extratocontacorrente.responsavel as \"extratocontacorrente.responsavel\",  ");
		sb.append(" extratocontacorrente.formapagamento as \"extratocontacorrente.formapagamento\", ");
		sb.append(" extratocontacorrente.operadoracartao as \"extratocontacorrente.operadoracartao\", ");
		sb.append(" extratocontacorrente.valorTaxaBancaria as \"extratocontacorrente.valorTaxaBancaria\", ");
		sb.append(" extratocontacorrente.conciliacaocontacorrentediaextrato as \"extratocontacorrente.conciliacaocontacorrentediaextrato\", ");
		sb.append(" case extratocontacorrente.origemExtratoContaCorrente ");
		sb.append(" when '").append(OrigemExtratoContaCorrenteEnum.RECEBIMENTO.toString()).append("' then exists (select codigo from negociacaorecebimento where codigo = extratocontacorrente.codigoOrigem and  recebimentoBoletoAutomatico = false ) ");
		sb.append(" else false end as lancamentoManual, ");
		sb.append(" contacorrente.numero as \"contacorrente.numero\", contacorrente.digito as \"contacorrente.digito\", ");
		sb.append(" formaPagamento.nome as \"formaPagamento.nome\", formaPagamento.tipo as \"formaPagamento.tipo\", ");
		sb.append(" operadoracartao.nome as \"operadoracartao.nome\"  ");
		
		sb.append(" FROM extratocontacorrente ");
		sb.append(" INNER JOIN contacorrente ON extratocontacorrente.contacorrente = contacorrente.codigo ");
		sb.append(" inner join agencia on contacorrente.agencia = agencia.codigo ");
		sb.append(" inner join formaPagamento on formaPagamento.codigo = extratocontacorrente.formaPagamento ");
		sb.append(" left join operadoracartao on operadoracartao.codigo = extratocontacorrente.operadoracartao ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ExtratoContaCorrenteVO> consultarExtratoContaCorrenteProcessamentoPorContaCorrentePorDataAndCodigoQueNaoEstaoNaLista(ConciliacaoContaCorrenteDiaVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoOFX();
		sb.append(" where conciliacaocontacorrentediaextrato is null and desconsiderarConciliacaoBancaria  =  false ");
		
		sb.append(" and ( trim(leading '0' from contacorrente.numero) = '").append(StringUtils.stripStart(obj.getConciliacaoContaCorrente().getContaCorrenteArquivo(), "0")).append("' ");
    	sb.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(StringUtils.stripStart(obj.getConciliacaoContaCorrente().getContaCorrenteArquivo(), "0")).append("' ");
    	sb.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(StringUtils.stripStart(obj.getConciliacaoContaCorrente().getContaCorrenteArquivo(), "0")).append("' ");
    	sb.append(" ) ");
    	
    	sb.append(" and contacorrente.tipoContaCorrente = '").append(TipoContaCorrenteEnum.CORRENTE).append("' ");
		
		if(!obj.getConciliacaoContaCorrente().getDigitoContaCorrenteArquivo().isEmpty()){
			sb.append(" and contacorrente.digito = '").append(obj.getConciliacaoContaCorrente().getDigitoContaCorrenteArquivo()).append("' ");	
		}
		
		if(Uteis.isAtributoPreenchido(obj.getListaCodigoExtratoContaCorrenteExistente())){
			sb.append(" and extratocontacorrente.codigo not in(").append(obj.getListaCodigoExtratoContaCorrenteExistente()).append(")  ");	
		}
		
/*		if(Uteis.isAtributoPreenchido(obj.getData()) && UteisData.isDiaSegunda(obj.getData())){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(UteisData.obterDataFuturaUsandoCalendar(obj.getData(), -2))).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(obj.getData())).append(" 23:59:59' ");
		}else if(Uteis.isAtributoPreenchido(obj.getData())){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(obj.getData())).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(obj.getData())).append(" 23:59:59' ");	
		}*/
		if(Uteis.isAtributoPreenchido(obj.getData())){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(obj.getData())).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(obj.getData())).append(" 23:59:59' ");	
		}
		
		
		sb.append(" order by extratocontacorrente.data, extratocontacorrente.tipomovimentacaofinanceira, formaPagamento.tipo  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ExtratoContaCorrenteVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			ExtratoContaCorrenteVO extrato  = new ExtratoContaCorrenteVO();
			montarDadosBasico(extrato, tabelaResultado);
			lista.add(extrato);
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ExtratoContaCorrenteVO> consultarExtratoContaCorrentePorOrigemPorCodigoOrigem(TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum origem, Integer codigoOrigem, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoOFX();
		sb.append(" where extratocontacorrente.desconsiderarConciliacaoBancaria  =  false and extratocontacorrente.origemextratocontacorrente='").append(origem.name()).append("'  ");
		sb.append(" and extratocontacorrente.codigoorigem = ").append(codigoOrigem).append("  ");
		sb.append(" and extratocontacorrente.tipomovimentacaofinanceira = '").append(tipoMovimentacaoFinanceira).append("'  ");
		sb.append(" order by extratocontacorrente.data, extratocontacorrente.tipomovimentacaofinanceira, formaPagamento.tipo  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ExtratoContaCorrenteVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			ExtratoContaCorrenteVO extrato  = new ExtratoContaCorrenteVO();
			montarDadosBasico(extrato, tabelaResultado);
			lista.add(extrato);
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ExtratoContaCorrenteVO> consultarExtratoContaCorrentePorListaCodigos(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoOFX();
		sb.append(" where extratocontacorrente.codigo in(").append(conciliacaoContaCorrenteDiaExtratoVO.getCodigoSei()).append(")  ");
		sb.append(" order by extratocontacorrente.data, extratocontacorrente.tipomovimentacaofinanceira, formaPagamento.tipo  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ExtratoContaCorrenteVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			ExtratoContaCorrenteVO extrato  = new ExtratoContaCorrenteVO();
			montarDadosBasico(extrato, tabelaResultado);
			lista.add(extrato);
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ExtratoContaCorrenteVO> consultarExtratoContaCorrenteDesconsiderarConciliacaoBancaria(String listaCodigos, boolean desconsiderarconciliacaobancaria, UsuarioVO usuarioVO) throws Exception {
		List<ExtratoContaCorrenteVO> lista = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(listaCodigos)) {
			StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoOFX();
			sb.append(" where extratocontacorrente.codigo in(").append(listaCodigos).append(")  ");
			sb.append(" and extratocontacorrente.desconsiderarconciliacaobancaria = ").append(desconsiderarconciliacaobancaria).append("  ");
			sb.append(" order by extratocontacorrente.data, extratocontacorrente.tipomovimentacaofinanceira, formaPagamento.tipo  ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			while (tabelaResultado.next()) {
				ExtratoContaCorrenteVO extrato  = new ExtratoContaCorrenteVO();
				montarDadosBasico(extrato, tabelaResultado);
				lista.add(extrato);
			}
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ExtratoContaCorrenteVO> consultarExtratoContaCorrenteQueNaoExistemMaisPoremEstaNaConciliacao(String listaCodigos,  UsuarioVO usuarioVO) throws Exception {
		List<ExtratoContaCorrenteVO> lista = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(listaCodigos)) {
			StringBuilder sb = new StringBuilder();
			sb.append(" select t.codigo_temp from extratocontacorrente ");
			sb.append(" 	right join ( ");
			sb.append(" 		select unnest(string_to_array('").append(listaCodigos).append("', ','))::integer as codigo_temp ");
			sb.append(" 	) as t on t.codigo_temp=extratocontacorrente.codigo ");
			sb.append(" 	where codigo_temp is not null and extratocontacorrente.codigo is null ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			while (tabelaResultado.next()) {
				ExtratoContaCorrenteVO extrato  = new ExtratoContaCorrenteVO();
				extrato.setCodigo(tabelaResultado.getInt("codigo_temp"));
				lista.add(extrato);
			}
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public  List<ExtratoContaCorrenteVO> consultarExtratosContaCorrenteComValorMenores(String contaCorrente, String digito, Date data, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrenteEnum, TipoFormaPagamento tipoFormaPagamento, Double valor, String listaCodigoExtrato, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoOFX();
		sb.append(" where conciliacaocontacorrentediaextrato is null and  desconsiderarConciliacaoBancaria  =  false ");		
		sb.append(" and ( trim(leading '0' from contacorrente.numero) = '").append(contaCorrente).append("' ");
		
    	sb.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(contaCorrente).append("' ");
    	sb.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(contaCorrente).append("' ");
    	sb.append(" ) ");
		
    	sb.append(" and contacorrente.tipoContaCorrente = '").append(TipoContaCorrenteEnum.CORRENTE).append("' ");
    	sb.append(" and extratocontacorrente.tipomovimentacaofinanceira = '").append(tipoMovimentacaoFinanceira.name()).append("' ");
		if(!listaCodigoExtrato.isEmpty()){
			sb.append(" and extratocontacorrente.codigo not in(").append(listaCodigoExtrato).append(")  ");	
		}
		if(!digito.isEmpty()){
			sb.append(" and contacorrente.digito = '").append(digito).append("' ");	
		}
		if(valor != null && valor < 0){
			sb.append(" and (extratocontacorrente.valor - coalesce(extratocontacorrente.valortaxabancaria,0.0))::numeric(20,2) > ").append(valor).append("  and (extratocontacorrente.valor - coalesce(extratocontacorrente.valortaxabancaria,0.0))::numeric(20,2) < 0.0 ");	
		}else if(valor != null){
			sb.append(" and (extratocontacorrente.valor- coalesce(extratocontacorrente.valortaxabancaria,0.0))::numeric(20,2) < ").append(valor).append("  and (extratocontacorrente.valor - coalesce(extratocontacorrente.valortaxabancaria,0.0))::numeric(20,2) > 0.0 ");
		}
		 
		if(Uteis.isAtributoPreenchido(tipoFormaPagamento)){
			sb.append(" and formaPagamento.tipo = '").append(tipoFormaPagamento.getValor()).append("' ");	
		}
		if(Uteis.isAtributoPreenchido(origemExtratoContaCorrenteEnum)){
			sb.append(" and extratocontacorrente.origemextratocontacorrente = '").append(origemExtratoContaCorrenteEnum.name()).append("' ");	
		}
		
		/*if(Uteis.isAtributoPreenchido(data) && UteisData.isDiaSegunda(data)){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(UteisData.obterDataFuturaUsandoCalendar(data, -2))).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(data)).append(" 23:59:59' ");
		}else if(Uteis.isAtributoPreenchido(data)){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(data)).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(data)).append(" 23:59:59' ");	
		}*/
		if(Uteis.isAtributoPreenchido(data)){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(data)).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(data)).append(" 23:59:59' ");	
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ExtratoContaCorrenteVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			ExtratoContaCorrenteVO extrato  = new ExtratoContaCorrenteVO();
			montarDadosBasico(extrato, tabelaResultado);
			lista.add(extrato);
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ExtratoContaCorrenteVO consultarExtratoContaCorrentePorValor(String contaCorrente, String digito, Date data, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrenteEnum, TipoFormaPagamento tipoFormaPagamento, Double valor,  String listaCodigoExtrato, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamentoArquivoOFX();
		sb.append(" where conciliacaocontacorrentediaextrato is null and desconsiderarConciliacaoBancaria  =  false ");
		
		sb.append(" and ( trim(leading '0' from contacorrente.numero) = '").append(contaCorrente).append("' ");
    	sb.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(contaCorrente).append("' ");
    	sb.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(contaCorrente).append("' ");
    	sb.append(" ) ");
    	
		sb.append(" and contacorrente.tipoContaCorrente = '").append(TipoContaCorrenteEnum.CORRENTE).append("' ");
		sb.append(" and extratocontacorrente.tipomovimentacaofinanceira = '").append(tipoMovimentacaoFinanceira.name()).append("' ");
		if(!listaCodigoExtrato.isEmpty()){
			sb.append(" and extratocontacorrente.codigo not in (").append(listaCodigoExtrato).append(") ");	
		}
		if(!digito.isEmpty()){
			sb.append(" and contacorrente.digito = '").append(digito).append("' ");	
		}
		if(valor != null && valor < 0.0){
			sb.append(" and (extratocontacorrente.valor - coalesce(extratocontacorrente.valortaxabancaria,0.0))::numeric(20,2) = ").append(valor * -1).append(" ");	
		}else if(valor != null ){
			sb.append(" and (extratocontacorrente.valor - coalesce(extratocontacorrente.valortaxabancaria,0.0))::numeric(20,2) = ").append(valor).append(" ");
		}
		if(Uteis.isAtributoPreenchido(tipoFormaPagamento)){
			sb.append(" and formaPagamento.tipo = '").append(tipoFormaPagamento.getValor()).append("' ");	
		}
		if(Uteis.isAtributoPreenchido(origemExtratoContaCorrenteEnum)){
			sb.append(" and extratocontacorrente.origemextratocontacorrente = '").append(origemExtratoContaCorrenteEnum.name()).append("' ");	
		}
		
		/*if(Uteis.isAtributoPreenchido(data) && UteisData.isDiaSegunda(data)){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(UteisData.obterDataFuturaUsandoCalendar(data, -2))).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(data)).append(" 23:59:59' ");
		}else if(Uteis.isAtributoPreenchido(data)){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(data)).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(data)).append(" 23:59:59' ");	
		}*/
		if(Uteis.isAtributoPreenchido(data)){
			sb.append(" and extratocontacorrente.data between  '").append(Uteis.getDataJDBC(data)).append(" 00:00:00' and  '").append(Uteis.getDataJDBC(data)).append(" 23:59:59' ");	
		}
		sb.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		ExtratoContaCorrenteVO extratoContaCorrente = new ExtratoContaCorrenteVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(extratoContaCorrente, tabelaResultado);
		}
		return extratoContaCorrente;
	}
	

	public void montarDadosBasico(ExtratoContaCorrenteVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setCodigo(dadosSQL.getInt("extratocontacorrente.codigo"));
		obj.setValor(dadosSQL.getDouble("extratocontacorrente.valor"));
		obj.setData(dadosSQL.getDate("extratocontacorrente.data"));
		obj.setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.valueOf(dadosSQL.getString("extratocontacorrente.origemextratocontacorrente")));
		obj.setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira.valueOf(dadosSQL.getString("extratocontacorrente.tipomovimentacaofinanceira")));
		obj.setCodigoOrigem(dadosSQL.getInt("extratocontacorrente.codigoorigem"));
		obj.setCodigoCheque(dadosSQL.getInt("extratocontacorrente.codigocheque"));
		obj.setSacadoCheque(dadosSQL.getString("extratocontacorrente.sacadocheque"));
		obj.setNumeroCheque(dadosSQL.getString("extratocontacorrente.numerocheque"));
		obj.setBancoCheque(dadosSQL.getString("extratocontacorrente.bancocheque"));
		obj.setContaCorrenteCheque(dadosSQL.getString("extratocontacorrente.contacorrentecheque"));
		obj.setAgenciaCheque(dadosSQL.getString("extratocontacorrente.agenciacheque"));
		obj.setDataPrevisaoCheque(dadosSQL.getDate("extratocontacorrente.dataprevisaocheque"));
		obj.setNomeSacado(dadosSQL.getString("extratocontacorrente.nomesacado"));
		obj.setTipoSacado(TipoSacadoExtratoContaCorrenteEnum.valueOf(dadosSQL.getString("extratocontacorrente.tiposacado")));
		obj.setCodigoSacado(dadosSQL.getInt("extratocontacorrente.codigosacado"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("extratocontacorrente.responsavel"));
		obj.getFormaPagamento().setCodigo(dadosSQL.getInt("extratocontacorrente.formapagamento"));
		obj.getFormaPagamento().setNome(dadosSQL.getString("formaPagamento.nome"));
		obj.getFormaPagamento().setTipo(dadosSQL.getString("formaPagamento.tipo"));
		obj.setValorTaxaBancaria(dadosSQL.getDouble("extratocontacorrente.valorTaxaBancaria"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("extratocontacorrente.operadoracartao"));
		obj.getOperadoraCartaoVO().setNome(dadosSQL.getString("operadoracartao.nome"));
		obj.getContaCorrente().setCodigo(dadosSQL.getInt("extratocontacorrente.contacorrente"));
		obj.getContaCorrente().setNumero(dadosSQL.getString("contacorrente.numero"));
		obj.getContaCorrente().setDigito(dadosSQL.getString("contacorrente.digito"));
		obj.getConciliacaoContaCorrenteDiaExtratoVO().setCodigo(dadosSQL.getInt("extratocontacorrente.conciliacaocontacorrentediaextrato"));
		if (obj.getTipoMovimentacaoFinanceira().isMovimentacaoSaida()) {
			obj.setValor(obj.getValor() * -1);
		}
		obj.setLancamentoManual(dadosSQL.getBoolean("lancamentoManual"));
	
		 
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ExtratoContaCorrenteVO obj, UsuarioVO usuarioVO) throws Exception {		
			validarDados(obj);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataAlterar(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuarioVO);
			
			final String sqlAlterar = "UPDATE ExtratoContaCorrente set data=?, desconsiderarConciliacaoBancaria=?, conciliacaocontacorrentediaextrato=?, valorTaxaBancaria=? where codigo=?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
					getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

						public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
							int x = 1;
							PreparedStatement sql = arg0.prepareStatement(sqlAlterar);
							
							sql.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataAlterar()));
							sql.setBoolean(x++, obj.isDesconsiderarConciliacaoBancaria());
							sql.setNull(x++, 0);
							sql.setDouble(x++, obj.getValorTaxaBancaria());
							sql.setInt(x++, obj.getCodigo());
							return sql;
						}
					});
	}

	@Override
	public void validarExclusaoExtratoContaCorrente(ExtratoContaCorrenteVO extratoContaCorrenteVO, UsuarioVO usuarioVO) throws ConsistirException {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirExcluirExtratoContaCorrenteOrigemNaoLocalizada", usuarioVO);
			if(extratoContaCorrenteVO.getIsConciliado() || validarConciliacaoContaCorrenteDiaExtratoExistente(extratoContaCorrenteVO.getCodigo(), usuarioVO)) {
				throw new ConsistirException("Este lançamento já foi conciliado pela tela de conciliação bancária portanto não é possível realizar a exclusão deste registro.");		
			}
		} catch (Exception e) {
			throw new ConsistirException(e.getMessage());
		}
		if(extratoContaCorrenteVO.getPossuiOrigem()) {
			throw new ConsistirException("Este lançamento é da origem "+extratoContaCorrenteVO.getOrigemExtratoContaCorrente().getDescricao()+" de código "+extratoContaCorrenteVO.getCodigo()+" como este registro ainda existe no sistema é necessário realizar o estorno deste lançamento.");		
		}
	}
	
	
	private boolean validarConciliacaoContaCorrenteDiaExtratoExistente(Integer codigo, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select conciliacaocontacorrentediaextrato from extratocontacorrente ");
		sqlStr.append(" WHERE codigo =  ").append(codigo).append("  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, "conciliacaocontacorrentediaextrato", TipoCampoEnum.INTEIRO);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorFormaPagamentoNegociacaoRecebimento(Integer codigo, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		ExtratoContaCorrente.excluir(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(" Select extratocontacorrente.conciliacaocontacorrentediaextrato,  ");
		sb.append(" extratocontacorrente.origemExtratoContaCorrente,  ");
		sb.append(" extratocontacorrente.nomesacado,  ");
		sb.append(" conciliacaocontacorrente.codigo as conciliacaocontacorrente  ");
		sb.append(" FROM extratocontacorrente ");
		sb.append(" inner join conciliacaocontacorrentediaextrato  on conciliacaocontacorrentediaextrato.codigo = extratocontacorrente.conciliacaocontacorrentediaextrato ");
		sb.append(" inner join conciliacaocontacorrentedia on conciliacaocontacorrentediaextrato.conciliacaocontacorrentedia = conciliacaocontacorrentedia.codigo ");
		sb.append(" inner join conciliacaocontacorrente on conciliacaocontacorrente.codigo = conciliacaocontacorrentedia.conciliacaocontacorrente ");
		sb.append(" WHERE origemExtratoContaCorrente = '").append(origemExtratoContaCorrente.name()).append("' ");
		sb.append(" and extratocontacorrente.formapagamentonegociacaorecebimento = ").append(codigo).append(" ");
		sb.append(" and extratocontacorrente.conciliacaocontacorrentediaextrato is not null  ");
		sb.append(" and extratocontacorrente.desconsiderarconciliacaobancaria = false ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		validarSeExtratoContaCorrenteAptaExclusao(usuario, rs);
		String sql = "DELETE FROM extratocontacorrente WHERE formapagamentonegociacaorecebimento = ? and origemExtratoContaCorrente = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, codigo, origemExtratoContaCorrente.name());
	}

	private void validarSeExtratoContaCorrenteAptaExclusao(UsuarioVO usuario, SqlRowSet rs) {
		if (rs.next()) {
			StringBuilder sb = new StringBuilder("");
			sb.append( "Este lançamento do extrato da conta corrente  esta na conciliação bancária de código ").append(rs.getInt("conciliacaocontacorrente"));
			sb.append(" portanto não é possível realizar a exclusão deste registro ");
			sb.append(" de origem ").append(UteisJSF.internacionalizarEnum(OrigemExtratoContaCorrenteEnum.valueOf(rs.getString("origemExtratoContaCorrente"))));
			sb.append(" do sacado ").append(rs.getString("nomesacado"));
			throw new StreamSeiException(sb.toString());
		}
	}
		

}
