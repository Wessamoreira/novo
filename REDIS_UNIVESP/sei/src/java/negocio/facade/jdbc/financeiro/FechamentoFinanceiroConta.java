package negocio.facade.jdbc.financeiro;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroDetalhamentoValorVO;
import negocio.comuns.financeiro.FechamentoFinanceiroFormaPagamentoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroContaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FechamentoFinanceiroContaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class FechamentoFinanceiroConta extends ControleAcesso implements FechamentoFinanceiroContaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4047360151727448699L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO, UsuarioVO usuarioVO) throws Exception {
		incluir(fechamentoFinanceiroContaVO, "fechamentoFinanceiroConta",
				new AtributoPersistencia().add("tipoOrigemContaReceber", fechamentoFinanceiroContaVO.getTipoOrigemContaReceber())
						.add("codigoOrigem", fechamentoFinanceiroContaVO.getCodigoOrigem())
						.add("nossoNumero", fechamentoFinanceiroContaVO.getNossoNumero())
						.add("parcela", fechamentoFinanceiroContaVO.getParcela())
						.add("situacaoContaReceber", fechamentoFinanceiroContaVO.getSituacaoContaReceber())
						.add("dataVencimento", fechamentoFinanceiroContaVO.getDataVencimento())
						.add("dataCompetencia", fechamentoFinanceiroContaVO.getDataCompetencia())
						.add("dataCancelamento", fechamentoFinanceiroContaVO.getDataCancelamento())
						.add("dataRecebimento", fechamentoFinanceiroContaVO.getDataRecebimento())
						.add("dataNegociacao", fechamentoFinanceiroContaVO.getDataNegociacao())
						.add("matriculaPeriodo", fechamentoFinanceiroContaVO.getMatriculaPeriodo())
						.add("pessoa", fechamentoFinanceiroContaVO.getPessoa())
						.add("responsavelFinanceiro", fechamentoFinanceiroContaVO.getResponsavelFinanceiro())
						.add("parceiro", fechamentoFinanceiroContaVO.getParceiro())
						.add("fornecedor", fechamentoFinanceiroContaVO.getFornecedor())
						.add("funcionario", fechamentoFinanceiroContaVO.getFuncionario())
						.add("unidadeEnsinoFinanceira", fechamentoFinanceiroContaVO.getUnidadeEnsinoFinanceira())
						.add("unidadeEnsinoAcademica", fechamentoFinanceiroContaVO.getUnidadeEnsinoAcademica())
						.add("nomeSacado", fechamentoFinanceiroContaVO.getNomeSacado())
						.add("cpfCnpjSacado", fechamentoFinanceiroContaVO.getCpfCnpjSacado())
						.add("matricula", fechamentoFinanceiroContaVO.getMatricula().getMatricula())
						.add("fechamentoFinanceiro", fechamentoFinanceiroContaVO.getFechamentoFinanceiro())
						.add("valor", fechamentoFinanceiroContaVO.getValor())
						.add("tipoPessoa", fechamentoFinanceiroContaVO.getTipoPessoa())
						.add("origemFechamentoFinanceiroConta", fechamentoFinanceiroContaVO.getOrigemFechamentoFinanceiroConta())
						.add("codOrigemFechamentoFinanceiro", fechamentoFinanceiroContaVO.getCodOrigemFechamentoFinanceiro()),
				usuarioVO);
		/*
		 * incluirFechamentoFinanceiroCentroResultado(fechamentoFinanceiroContaVO, usuarioVO); incluirFechamentoFinanceiroFormaPagamento(fechamentoFinanceiroContaVO, usuarioVO); incluirFechamentoFinanceiroDetalhamentoValorVOs(fechamentoFinanceiroContaVO, usuarioVO);
		 */
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirFechamentoFinanceiroDetalhamentoValorVOs(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO, UsuarioVO usuarioVO) throws Exception {
		for (FechamentoFinanceiroDetalhamentoValorVO fechamentoFinanceiroDetalhamentoValorVO : fechamentoFinanceiroContaVO.getFechamentoFinanceiroDetalhamentoValorVOs()) {
			fechamentoFinanceiroDetalhamentoValorVO.setFechamentoFinanceiroContaVO(fechamentoFinanceiroContaVO);
			getFacadeFactory().getFechamentoFinanceiroDetalhamentoValorFacade().incluir(fechamentoFinanceiroDetalhamentoValorVO, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirFechamentoFinanceiroCentroResultado(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO, UsuarioVO usuarioVO) throws Exception {
		for (FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO : fechamentoFinanceiroContaVO.getFechamentoFinanceiroCentroResultadoVOs()) {
			fechamentoFinanceiroCentroResultadoVO.setOrigemFechamentoFinanceiroCentroResultadoOrigem(OrigemFechamentoFinanceiroCentroResultadoEnum.FECHAMENTO_FINANCEIRO_CONTA);
			fechamentoFinanceiroCentroResultadoVO.setCodOrigemFechamentoFinanceiro(fechamentoFinanceiroContaVO.getCodigo());
			getFacadeFactory().getFechamentoFinanceiroCentroResultadoFacade().incluir(fechamentoFinanceiroCentroResultadoVO, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirFechamentoFinanceiroFormaPagamento(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO, UsuarioVO usuarioVO) throws Exception {
		for (FechamentoFinanceiroFormaPagamentoVO fechamentoFinanceiroFormaPagamentoVO : fechamentoFinanceiroContaVO.getFechamentoFinanceiroFormaPagamentoVOs()) {
			fechamentoFinanceiroFormaPagamentoVO.setFechamentoFinanceiroContaVO(fechamentoFinanceiroContaVO);
			getFacadeFactory().getFechamentoFinanceiroFormaPagamentoFacade().incluir(fechamentoFinanceiroFormaPagamentoVO, usuarioVO);
		}
	}

	public StringBuilder getSQLPadraoConsultaProcessamento() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT contareceber.codigo as \"contareceber.codigo\", contareceber.tipopessoa as \"contareceber.tipopessoa\", contareceber.nossonumero as \"contareceber.nossonumero\", contareceber.pessoa as \"contareceber.pessoa\", contareceber.parcela as \"contareceber.parcela\",    ");
		sb.append(" contareceber.valorrecebercalculado as \"contareceber.valorrecebercalculado\", contareceber.valorRecebido as \"contareceber.valorRecebido\",  contareceber.dataVencimento as \"contareceber.dataVencimento\", contareceber.dataCompetencia as \"contareceber.dataCompetencia\",  ");
		sb.append(" contareceber.matriculaaluno as \"contareceber.matriculaaluno\", contareceber.pessoa as \"contareceber.pessoa\",   contareceber.responsavelfinanceiro as \"contareceber.responsavelfinanceiro\",  contareceber.situacao as \"contareceber.situacao\", contareceber.funcionario as \"contareceber.funcionario\", ");
		sb.append(" contareceber.fornecedor as \"contareceber.fornecedor\", contareceber.parceiro as \"contareceber.parceiro\", contareceber.matriculaPeriodo as \"contareceber.matriculaPeriodo\", contareceber.codorigem as \"contareceber.codorigem\", contareceber.tipoorigem as \"contareceber.tipoorigem\", ");

		sb.append(" negociacaorecebimento.data as  \"contareceber.dataRecebimento\",  ");
		sb.append(" case when  contareceber.situacao = 'CF' and  contareceber.datacancelamento is not null then contareceber.datacancelamento ");
		sb.append("      when  contareceber.situacao = 'CF' and  contareceber.datacompetencia is not null then contareceber.datacompetencia ");
		sb.append("      else null end as  \"contareceber.dataCancelamento\",  ");
		sb.append(" negociacaocontareceber.data as  \"contareceber.dataNegociacao\",  ");

		sb.append(" matricula.matricula AS \"Matricula.matricula\",  ");

		sb.append(" pessoamatricula.codigo AS \"pessoamatricula.codigo\", pessoamatricula.nome AS \"pessoamatricula.nome\",  pessoamatricula.cpf AS \"pessoamatricula.cpf\",  ");
		sb.append(" responsavelFinanceiro.codigo AS \"responsavelFinanceiro.codigo\", responsavelFinanceiro.nome AS \"responsavelFinanceiro.nome\",  responsavelFinanceiro.cpf AS \"responsavelFinanceiro.cpf\",  ");
		sb.append(" pessoacandidato.codigo AS \"pessoacandidato.codigo\", pessoacandidato.nome AS \"pessoacandidato.nome\",  pessoacandidato.cpf AS \"pessoacandidato.cpf\",  ");
		sb.append(" pessoafuncionario.codigo AS \"pessoafuncionario.codigo\", pessoafuncionario.nome AS \"pessoafuncionario.nome\",  pessoafuncionario.cpf AS \"pessoafuncionario.cpf\",  ");
		sb.append(" fornecedor.codigo AS \"fornecedor.codigo\", fornecedor.nome AS \"fornecedor.nome\",  fornecedor.cpf AS \"fornecedor.cpf\",  fornecedor.cnpj AS \"fornecedor.cnpj\", ");
		sb.append(" parceiro.codigo AS \"parceiro.codigo\", parceiro.nome AS \"Parceiro.nome\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.cnpj AS \"Parceiro.cnpj\", ");

		sb.append(" unidadeEnsino.codigo AS \"UnidadeEnsino.codigo\",  ");

		sb.append(" unidadeEnsinoFinanceira.codigo AS \"unidadeEnsinoFinanceira.codigo\" ");

		sb.append(" FROM contareceber ");
		sb.append(" LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) ");
		sb.append(" LEFT JOIN pessoa AS pessoamatricula ON (matricula.aluno = pessoamatricula.codigo) ");
		sb.append(" LEFT JOIN pessoa as responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo) ");
		sb.append(" LEFT JOIN pessoa AS pessoacandidato ON (contareceber.candidato = pessoacandidato.codigo) ");
		sb.append(" LEFT JOIN fornecedor ON (fornecedor.codigo = contareceber.fornecedor) ");
		sb.append(" LEFT JOIN parceiro on parceiro.codigo = contareceber.parceiro ");
		sb.append(" LEFT JOIN funcionario ON (funcionario.codigo = contareceber.funcionario) ");
		sb.append(" LEFT JOIN pessoa AS pessoafuncionario ON (pessoafuncionario.codigo = funcionario.pessoa) ");
		sb.append(" LEFT JOIN unidadeensino ON (unidadeensino.codigo = contareceber.unidadeensino) ");
		sb.append(" LEFT JOIN unidadeensino as unidadeEnsinoFinanceira ON (unidadeEnsinoFinanceira.codigo = contareceber.unidadeEnsinoFinanceira) ");
		sb.append(" LEFT JOIN turma ON (turma.codigo = contareceber.turma) ");
		sb.append(" LEFT JOIN contarecebernegociacaorecebimento ON (contareceber.codigo = contarecebernegociacaorecebimento.contareceber ) ");
		sb.append(" LEFT JOIN negociacaorecebimento ON ( contarecebernegociacaorecebimento.negociacaorecebimento =  negociacaorecebimento.codigo ) ");
		sb.append(" LEFT JOIN contarecebernegociado ON (contareceber.codigo = contarecebernegociado.contareceber ) ");
		sb.append(" LEFT JOIN negociacaocontareceber ON ( contarecebernegociado.negociacaocontareceber =  negociacaocontareceber.codigo ) ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarDadosParaFechamentoFinanceiroConta(FechamentoFinanceiroVO ff, UsuarioVO usuario) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamento();
		sb.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(ff.getFechamentoMesVO().getListaUnidadesEnsinoVOs())) {
			sb.append(" and unidadeEnsinoFinanceira.codigo in ( ");
			sb.append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(ff.getFechamentoMesVO().getListaUnidadesEnsinoVOs(), "unidadeEnsino.codigo"));
			sb.append(" ) ");
		}
		
		sb.append("and ( ");
		sb.append("		 (");
		sb.append(" 		extract(month from contareceber.datacompetencia) = ").append(ff.getFechamentoMesVO().getMes());
		sb.append(" 		and extract(year from contareceber.datacompetencia) = ").append(ff.getFechamentoMesVO().getAno());
		sb.append(" 	 ) or (");
		sb.append(" 		contareceber.situacao = 'RE' ");
		sb.append(" 		and negociacaorecebimento.data >= '").append(Uteis.getDataBD0000(UteisData.getPrimeiroDataMes(ff.getFechamentoMesVO().getMes(), ff.getFechamentoMesVO().getAno()))).append("' ");
		sb.append(" 		and negociacaorecebimento.data <= '").append(Uteis.getDataBD2359(UteisData.getUltimaDataMes(ff.getFechamentoMesVO().getMes(), ff.getFechamentoMesVO().getAno()))).append("' ");
		sb.append("      ) or (");
		sb.append(" 		contareceber.situacao = 'NE' ");
		sb.append(" 		and negociacaocontareceber.data >= '").append(Uteis.getDataBD0000(UteisData.getPrimeiroDataMes(ff.getFechamentoMesVO().getMes(), ff.getFechamentoMesVO().getAno()))).append("' ");
		sb.append(" 		and negociacaocontareceber.data <= '").append(Uteis.getDataBD2359(UteisData.getUltimaDataMes(ff.getFechamentoMesVO().getMes(), ff.getFechamentoMesVO().getAno()))).append("' ");
		sb.append("      ) or (");
		sb.append(" 		contareceber.situacao = 'CF' and contareceber.datacancelamento is not null ");
		sb.append(" 		and contareceber.datacancelamento >= '").append(Uteis.getDataBD0000(UteisData.getPrimeiroDataMes(ff.getFechamentoMesVO().getMes(), ff.getFechamentoMesVO().getAno()))).append("' ");
		sb.append(" 		and contareceber.datacancelamento <= '").append(Uteis.getDataBD2359(UteisData.getUltimaDataMes(ff.getFechamentoMesVO().getMes(), ff.getFechamentoMesVO().getAno()))).append("' ");
		sb.append(" 	 )");
		sb.append(" )  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			FechamentoFinanceiroContaVO obj = new FechamentoFinanceiroContaVO();
			obj.setFechamentoFinanceiro(ff);
			montarDadosFechamentoFinanceiroConta(rs, obj);
			incluir(obj, usuario);
			getFacadeFactory().getFechamentoFinanceiroCentroResultadoFacade().processarDadosParaFechamentoFinanceiroCentroResultadoVO(obj, usuario);
			getFacadeFactory().getFechamentoFinanceiroDetalhamentoValorFacade().processarDadosParaFechamentoFinanceiroDetalhamentoValorVO(obj, usuario);
			getFacadeFactory().getFechamentoFinanceiroFormaPagamentoFacade().processarDadosParaFechamentoFinanceiroFormaPagamentoVO(obj, usuario);
			ff.getFechamentoFinanceiroContaVOs().add(obj);
		}
	}

	private void montarDadosFechamentoFinanceiroConta(SqlRowSet rs, FechamentoFinanceiroContaVO obj) {
		obj.setTipoOrigemContaReceber(TipoOrigemContaReceber.getEnum(rs.getString("contareceber.tipoorigem")));
		obj.setCodigoOrigem(rs.getString("contareceber.codorigem"));
		obj.setNossoNumero(rs.getString("contareceber.nossonumero"));
		obj.setParcela(rs.getString("contareceber.parcela"));
		obj.setSituacaoContaReceber(SituacaoContaReceber.getEnum(rs.getString("contareceber.situacao")));
		obj.setTipoPessoa(TipoPessoa.getEnum(rs.getString("contareceber.tipoPessoa")));
		obj.setDataVencimento(rs.getDate("contareceber.dataVencimento"));
		obj.setDataCompetencia(rs.getDate("contareceber.dataCompetencia"));
		obj.setDataRecebimento(rs.getDate("contareceber.datarecebimento"));
		obj.setDataCancelamento(rs.getDate("contareceber.datacancelamento"));
		obj.setDataNegociacao(rs.getDate("contareceber.datanegociacao"));
		obj.getMatricula().setMatricula(rs.getString("contareceber.matriculaaluno"));
		obj.getMatriculaPeriodo().setCodigo(rs.getInt("contareceber.matriculaPeriodo"));
		obj.getPessoa().setCodigo(rs.getInt("contareceber.pessoa"));
		obj.getUnidadeEnsinoAcademica().setCodigo(rs.getInt("UnidadeEnsino.codigo"));
		obj.getUnidadeEnsinoFinanceira().setCodigo(rs.getInt("unidadeEnsinoFinanceira.codigo"));
		obj.setValor(obj.getSituacaoContaReceber().isNegociado() || obj.getSituacaoContaReceber().isRecebido() ?  rs.getDouble("contareceber.valorRecebido") : rs.getDouble("contareceber.valorrecebercalculado"));
		obj.setCodOrigemFechamentoFinanceiro(rs.getInt("contareceber.codigo"));
		obj.setOrigemFechamentoFinanceiroConta(OrigemFechamentoFinanceiroContaEnum.CONTA_RECEBER);

		switch (obj.getTipoPessoa()) {
		case ALUNO:
			obj.setNomeSacado(rs.getString("pessoamatricula.nome"));
			obj.setCpfCnpjSacado(rs.getString("pessoamatricula.cpf"));
			break;
		case RESPONSAVEL_FINANCEIRO:
			obj.getResponsavelFinanceiro().setCodigo(rs.getInt("contareceber.responsavelfinanceiro"));
			obj.setNomeSacado(rs.getString("responsavelFinanceiro.nome"));
			obj.setCpfCnpjSacado(rs.getString("responsavelFinanceiro.cpf"));
			break;
		case CANDIDATO:
			obj.setNomeSacado(rs.getString("pessoacandidato.nome"));
			obj.setCpfCnpjSacado(rs.getString("pessoacandidato.cpf"));
			break;
		case FUNCIONARIO:
			obj.getFuncionario().setCodigo(rs.getInt("contareceber.funcionario"));
			obj.setNomeSacado(rs.getString("pessoafuncionario.nome"));
			obj.setCpfCnpjSacado(rs.getString("pessoafuncionario.cpf"));
			break;
		case PARCEIRO:
			obj.getParceiro().setCodigo(rs.getInt("contareceber.parceiro"));
			obj.setNomeSacado(rs.getString("parceiro.nome"));
			obj.setCpfCnpjSacado(Uteis.isAtributoPreenchido(rs.getString("parceiro.cpf")) ? rs.getString("parceiro.cpf") : rs.getString("parceiro.cnpj"));
			break;
		case FORNECEDOR:
			obj.getFornecedor().setCodigo(rs.getInt("contareceber.fornecedor"));
			obj.setNomeSacado(rs.getString("fornecedor.nome"));
			obj.setCpfCnpjSacado(Uteis.isAtributoPreenchido(rs.getString("fornecedor.cpf")) ? rs.getString("fornecedor.cpf") : rs.getString("fornecedor.cnpj"));
			break;
		}
	}

	@Override
	public FechamentoFinanceiroContaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
