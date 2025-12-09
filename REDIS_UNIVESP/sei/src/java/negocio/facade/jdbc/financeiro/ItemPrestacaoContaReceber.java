package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaReceberVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.financeiro.enumerador.TipoPrestacaoContaEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ItemPrestacaoContaReceberInterfaceFacade;

@Service
@Lazy
public class ItemPrestacaoContaReceber extends ControleAcesso implements ItemPrestacaoContaReceberInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8694194051684225588L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemPrestacaoContaReceberVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO ItemPrestacaoContaReceber( contaReceber, itemPrestacaoContaOrigemContaReceber) VALUES ( ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getContaReceber().getCodigo());
					sqlInserir.setInt(2, obj.getItemPrestacaoContaOrigemContaReceber().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemPrestacaoContaReceberVO obj) throws Exception {
		try {
			final String sql = "UPDATE ItemPrestacaoContaReceber set contaReceber=?, itemPrestacaoContaOrigemContaReceber=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getContaReceber().getCodigo());
					sqlAlterar.setInt(2, obj.getItemPrestacaoContaOrigemContaReceber().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void incluirItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) throws Exception {
		for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaReceberVOs) {
			itemPrestacaoContaReceberVO.setItemPrestacaoContaOrigemContaReceber(itemPrestacaoContaOrigemContaReceberVO);
			incluir(itemPrestacaoContaReceberVO);
		}

	}

	@Override
	public void alterarItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) throws Exception {
		excluirItemPrestacaoContaReceberVOs(itemPrestacaoContaReceberVOs, itemPrestacaoContaOrigemContaReceberVO);
		for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaReceberVOs) {
			itemPrestacaoContaReceberVO.setItemPrestacaoContaOrigemContaReceber(itemPrestacaoContaOrigemContaReceberVO);
			if (itemPrestacaoContaReceberVO.getCodigo() == null || itemPrestacaoContaReceberVO.getCodigo() == 0) {
				incluir(itemPrestacaoContaReceberVO);
			} else {
				alterar(itemPrestacaoContaReceberVO);
			}
		}

	}

	@Override
	public void excluirItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("DELETE FROM ItemPrestacaoContaReceber where itemPrestacaoContaOrigemContaReceber = ").append(itemPrestacaoContaOrigemContaReceberVO.getCodigo());
			int x = 0;
			for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaReceberVOs) {
				if (x == 0) {
					sql.append(" and codigo not in (").append(itemPrestacaoContaReceberVO.getCodigo());
					x++;
				} else {
					sql.append(", ").append(itemPrestacaoContaReceberVO.getCodigo());
				}
			}
			if (x > 0) {
				sql.append(") ");
			}
			getConexao().getJdbcTemplate().execute(sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {

			sql = null;
		}

	}

	@Override
	public List<ItemPrestacaoContaReceberVO> consultarItemPrestacaoContaReceberPorItemPrestacaoContaOrigemContaReceber(ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceber, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectSqlDadosCompletos() + " WHERE  itemPrestacaoContaOrigemContaReceber = ").append(itemPrestacaoContaOrigemContaReceber.getCodigo());
		int x = 0;
		for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaOrigemContaReceber.getItemPrestacaoContaReceberVOs()) {
			if (x == 0) {
				sql.append(" and itemPrestacaoContaReceber.codigo not in (").append(itemPrestacaoContaReceberVO.getCodigo());
				x++;
			} else {
				sql.append(", ").append(itemPrestacaoContaReceberVO.getContaReceber().getCodigo());
			}
		}

		if (x > 0) {
			sql.append(") ");
		}
		return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<ItemPrestacaoContaReceberVO> montarDadosConsultaCompleta(SqlRowSet rs) {
		List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs = new ArrayList<ItemPrestacaoContaReceberVO>(0);
		while (rs.next()) {
			itemPrestacaoContaReceberVOs.add(montarDadosCompleta(rs));
		}
		return itemPrestacaoContaReceberVOs;
	}

	public StringBuilder getSelectSqlDadosCompletos() {
		StringBuilder sql = new StringBuilder("SELECT ItemPrestacaoContaReceber.*,   ");
		sql.append(" contaReceber.codigo as \"contaReceber.codigo\", contaReceber.dataVencimento, contaReceber.valor, contaReceber.tipoPessoa, ");
		sql.append(" contaReceber.tipoOrigem, contaReceber.valorRecebido,");
		sql.append(" pessoafuncionario.nome as \"pessoafuncionario.nome\", contaReceber.matriculaAluno, ");
		sql.append(" pessoa.nome as \"pessoa.nome\", parceiro.nome as  \"parceiro.nome\",parceiro.cpf as  \"parceiro.cpf\", parceiro.cnpj as  \"parceiro.cnpj\", funcionario.matricula as \"funcionario.matricula\", ");
		sql.append(" responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", aluno.nome as \"aluno.nome\", candidato.nome as \"candidato.nome\", candidato.cpf as \"candidato.cpf\" ");
		sql.append(" from ContaReceber");
		sql.append(" left join ItemPrestacaoContaReceber on ContaReceber.codigo = ItemPrestacaoContaReceber.contaReceber ");
		sql.append(" left join funcionario on funcionario.codigo = contaReceber.funcionario ");
		sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append(" left join pessoa on pessoa.codigo = contaReceber.pessoa ");
		sql.append(" left join parceiro on parceiro.codigo = contaReceber.parceiro ");
		sql.append(" left join Matricula on Matricula.matricula = contaReceber.matriculaAluno ");
		sql.append(" left join Pessoa as aluno on Matricula.aluno = aluno.codigo ");
		sql.append(" left join Pessoa as candidato on candidato.codigo = contaReceber.candidato ");
		sql.append(" LEFT JOIN Pessoa as responsavelFinanceiro ON contaReceber.responsavelFinanceiro = responsavelFinanceiro.codigo ");
		return sql;
	}

	private ItemPrestacaoContaReceberVO montarDadosCompleta(SqlRowSet rs) {
		ItemPrestacaoContaReceberVO obj = new ItemPrestacaoContaReceberVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.getItemPrestacaoContaOrigemContaReceber().setCodigo(rs.getInt("itemPrestacaoContaOrigemContaReceber"));
		obj.getContaReceber().setCodigo(rs.getInt("contaReceber.codigo"));
		obj.getContaReceber().setDataVencimento(rs.getDate("dataVencimento"));
		obj.getContaReceber().setValor(new Double(rs.getDouble("valor")));
		obj.getContaReceber().setTipoPessoa(rs.getString("tipoPessoa"));
		obj.getContaReceber().getMatriculaAluno().setMatricula(rs.getString("matriculaAluno"));
		obj.getContaReceber().getMatriculaAluno().getAluno().setNome(rs.getString("aluno.nome"));
		obj.getContaReceber().setTipoOrigem(rs.getString("tipoOrigem"));
		obj.getContaReceber().setValorRecebido(new Double(rs.getDouble("valorRecebido")));
		obj.getContaReceber().getFuncionario().getPessoa().setNome(rs.getString("pessoafuncionario.nome"));
		obj.getContaReceber().getFuncionario().setMatricula(rs.getString("funcionario.matricula"));
		obj.getContaReceber().getResponsavelFinanceiro().setNome(rs.getString("responsavelFinanceiro.nome"));
		obj.getContaReceber().getParceiroVO().setNome(rs.getString("parceiro.nome"));
		obj.getContaReceber().getParceiroVO().setCPF(rs.getString("parceiro.cpf"));
		obj.getContaReceber().getParceiroVO().setCNPJ(rs.getString("parceiro.cnpj"));
		obj.getContaReceber().getPessoa().setNome(rs.getString("pessoa.nome"));
		obj.getContaReceber().getCandidato().setNome(rs.getString("candidato.nome"));
		obj.getContaReceber().getCandidato().setCPF(rs.getString("candidato.cpf"));
		obj.setNovoObj(false);
		return obj;
	}

	@Override
	public List<ItemPrestacaoContaReceberVO> consultarContaReceberDisponivelPrestacaoConta(String favorecido, String tipoFiltroData, Date dataInicio, Date dataTermino, String tipoOrigem, PrestacaoContaVO prestacaoContaVO) {
		StringBuilder sql = new StringBuilder(getSelectSqlDadosCompletos());
		if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
			sql.append(" left join matriculaPeriodo on matriculaPeriodo.codigo = ( case when contareceber.matriculaperiodo is not null then (contareceber.matriculaperiodo) else ((select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula limit 1) ) end) ");
		}
		if (tipoFiltroData != null && tipoFiltroData.equals("DATA_RECEBIMENTO")) {
			sql.append(" inner join ContaReceberNegociacaoRecebimento on ContaReceberNegociacaoRecebimento.contaReceber = contaReceber.codigo");
			sql.append(" inner join negociacaoRecebimento on negociacaoRecebimento.codigo = ContaReceberNegociacaoRecebimento.negociacaoRecebimento");
		}
		sql.append(" WHERE (sem_acentos(UPPER(pessoafuncionario.nome)) LIKE sem_acentos(UPPER(?)) OR ");
		sql.append(" sem_acentos(UPPER(parceiro.nome)) LIKE sem_acentos(UPPER(?)) OR ");
		sql.append(" sem_acentos(UPPER(aluno.nome)) LIKE sem_acentos(UPPER(?)) OR ");
		sql.append(" sem_acentos(UPPER(candidato.nome)) LIKE sem_acentos(UPPER(?)) OR ");
		sql.append(" sem_acentos(UPPER(responsavelFinanceiro.nome)) LIKE sem_acentos(UPPER(?)) OR ");
		sql.append(" sem_acentos(UPPER(pessoa.nome)) LIKE sem_acentos(UPPER(?)))  ");
		if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)
				&& prestacaoContaVO.getTurma().getCodigo() != null
				&& prestacaoContaVO.getTurma().getCodigo() > 0) {
			sql.append(" and matriculaPeriodo.turma = ").append(prestacaoContaVO.getTurma().getCodigo());
		}
		if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO)
				&& prestacaoContaVO.getUnidadeEnsino().getCodigo() != null
				&& prestacaoContaVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" and contaReceber.unidadeEnsino = ").append(prestacaoContaVO.getUnidadeEnsino().getCodigo());
		}
		if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO)) {
			sql.append(" and matriculaPeriodo is null ");
		}
		if (tipoFiltroData != null && tipoFiltroData.equals("DATA_VENCIMENTO")) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", false));
		}
		if (tipoFiltroData != null && tipoFiltroData.equals("DATA_RECEBIMENTO")) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaoRecebimento.data", false));
		}
		if (tipoOrigem != null && !tipoOrigem.trim().isEmpty() && !tipoOrigem.equals("TO")) {
			sql.append(" and contaReceber.tipoOrigem = '").append(tipoOrigem.toUpperCase()).append("' ");
		}
		sql.append(" and contaReceber.situacao = 'RE' ");
		sql.append(" and not exists ( select ItemPrestacaoContaReceber.contaReceber from ItemPrestacaoContaReceber where contaReceber.codigo = ItemPrestacaoContaReceber.contaReceber) ");
		int x = 0;
		for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : prestacaoContaVO.getItemPrestacaoContaOrigemContaReceberVOs()) {
			for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs()) {
				if (x == 0) {
					sql.append(" and contaReceber.codigo not in (").append(itemPrestacaoContaReceberVO.getContaReceber().getCodigo());
					x++;
				} else {
					sql.append(", ").append(itemPrestacaoContaReceberVO.getContaReceber().getCodigo());
				}
			}
		}
		if (x > 0) {
			sql.append(") ");
		}

		return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), favorecido + "%", favorecido + "%", favorecido + "%", favorecido + "%", favorecido + "%", favorecido + "%"));
	}

}
