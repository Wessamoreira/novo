package negocio.facade.jdbc.financeiro;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroDetalhamentoValorVO;
import negocio.comuns.financeiro.enumerador.OrigemDetalhamentoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FechamentoFinanceiroDetalhamentoValorInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class FechamentoFinanceiroDetalhamentoValor extends ControleAcesso implements FechamentoFinanceiroDetalhamentoValorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5833886231020959520L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(FechamentoFinanceiroDetalhamentoValorVO fechamentoFinanceiroDetalhamentoValorVO,
			UsuarioVO usuarioVO) throws Exception {
		incluir(fechamentoFinanceiroDetalhamentoValorVO, "fechamentoFinanceiroDetalhamentoValor",
				new AtributoPersistencia().add("fechamentoFinanceiroConta", fechamentoFinanceiroDetalhamentoValorVO.getFechamentoFinanceiroContaVO())
						.add("tipoCentroResultadoOrigemDetalhe", fechamentoFinanceiroDetalhamentoValorVO.getTipoCentroResultadoOrigemDetalhe())
						.add("faixaDescontoProgressivo", fechamentoFinanceiroDetalhamentoValorVO.getFaixaDescontoProgressivo())
						.add("codOrigemDoTipoDetalhe", fechamentoFinanceiroDetalhamentoValorVO.getCodOrigemDoTipoDetalhe())
						.add("nomeOrigemDoTipoDetalhe", fechamentoFinanceiroDetalhamentoValorVO.getNomeOrigemDoTipoDetalhe())
						.add("valor", fechamentoFinanceiroDetalhamentoValorVO.getValor())
						.add("dataLimiteAplicacaoDesconto", fechamentoFinanceiroDetalhamentoValorVO.getDataLimiteAplicacaoDesconto())
						.add("ordemApresentacao", fechamentoFinanceiroDetalhamentoValorVO.getOrdemApresentacao())
						.add("utilizado", fechamentoFinanceiroDetalhamentoValorVO.isUtilizado())
						.add("tipoValor", fechamentoFinanceiroDetalhamentoValorVO.getTipoValor())
						.add("valorTipoValor", fechamentoFinanceiroDetalhamentoValorVO.getValorTipoValor()),
				usuarioVO);

	}

	public StringBuilder getSQLPadraoConsultaProcessamento() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM detalhamentovalorconta ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarDadosParaFechamentoFinanceiroDetalhamentoValorVO(FechamentoFinanceiroContaVO ffc, UsuarioVO usuario) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamento();
		sb.append(" where  detalhamentovalorconta.codigoorigem = '").append(ffc.getCodOrigemFechamentoFinanceiro()).append("' ");
		sb.append(" and  detalhamentovalorconta.origemdetalhamentoconta = '").append(OrigemDetalhamentoContaEnum.CONTA_RECEBER.name()).append("' ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (dadosSQL.next()) {
			FechamentoFinanceiroDetalhamentoValorVO obj = new FechamentoFinanceiroDetalhamentoValorVO();
			obj.setCodOrigemDoTipoDetalhe(dadosSQL.getInt("codOrigemDoTipoDetalhe"));
			obj.setNomeOrigemDoTipoDetalhe(dadosSQL.getString("nomeOrigemDoTipoDetalhe"));
			obj.setOrdemApresentacao(dadosSQL.getInt("ordemApresentacao"));
			obj.setValor(dadosSQL.getDouble("valor"));
			obj.setValorTipoValor(dadosSQL.getDouble("valortipovalor"));
			obj.setUtilizado(dadosSQL.getBoolean("utilizado"));
			obj.setDataLimiteAplicacaoDesconto(dadosSQL.getDate("dataLimiteAplicacaoDesconto"));
			obj.setTipoCentroResultadoOrigemDetalhe(TipoCentroResultadoOrigemDetalheEnum.valueOf(dadosSQL.getString("tipoCentroResultadoOrigemDetalhe")));
			obj.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.valueOf(dadosSQL.getString("faixaDescontoProgressivo")));
			obj.setTipoValor(TipoDescontoAluno.valueOf(dadosSQL.getString("tipovalor")));
			obj.setFechamentoFinanceiroContaVO(ffc);
			incluir(obj, usuario);
			ffc.getFechamentoFinanceiroDetalhamentoValorVOs().add(obj);
		}
	}

	@Override
	public List<FechamentoFinanceiroDetalhamentoValorVO> consultarPorFechamentoFinanceiroCentroResultado(
			Integer fechamentoFinanceiroCentroResultado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
