package negocio.facade.jdbc.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.GestaoContasPagarVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.enumerador.GestaoContasPagarOperacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.GestaoContasPagarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class GestaoContasPagar extends ControleAcesso implements GestaoContasPagarInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5903372093499610701L;
	private static String idEntidade = "GestaoContasPagar";
	
	public static String getIdEntidade() {
		return GestaoContasPagar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		GestaoContasPagar.idEntidade = idEntidade;
	}
	
	private void validarPermissaoOperacaoGestaoContaPagar(GestaoContasPagarVO obj, UsuarioVO usuario) throws AcessoException{
		switch (obj.getGestaoContasPagarOperacaoEnum()) {
		case ALTERACAO:
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_OPERACAO_ALTERACAO_GESTAO_CONTAS_PAGAR, usuario);
			break;
		case EXCLUSAO:
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_OPERACAO_EXCLUSAO_GESTAO_CONTAS_PAGAR, usuario);
			break;
		case PAGAMENTO:
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_OPERACAO_PAGAMENTO_GESTAO_CONTAS_PAGAR, usuario);
			break;
		case ESTORNO_PAGAMENTO:
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_OPERACAO_ESTORNO_PAGAMENTO_GESTAO_CONTAS_PAGAR, usuario);
			break;
		case CANCELAMENTO:
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_OPERACAO_CANCELAMENTO_GESTAO_CONTAS_PAGAR, usuario);
			break;
		case ESTORNO_CANCELAMENTO:
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_OPERACAO_ESTORNO_CANCELAMENTO_GESTAO_CONTAS_PAGAR, usuario);
			break;
		}
	}
	
	private void validarDados(GestaoContasPagarVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getGestaoContasPagarOperacaoEnum()), "O campo Operação deve ser informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isPagamento() && !Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento()), "O campo Forma Pagamento deve ser informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isPagamento() && obj.getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().isCartaoCredito() && !Uteis.isAtributoPreenchido(obj.getConfiguracaoFinanceiroCartaoVO()), "O campo Operadora deve ser informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isPagamento() && !obj.getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().isDinheiro() && !Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente()), "O campo Conta Corrente deve ser informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isPagamento() && obj.getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().isDinheiro() && !Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente()), "O campo Caixa deve ser informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isEstornoPagamento() && !Uteis.isAtributoPreenchido(obj.getMotivoAlteracao()), "O campo Motivo Estorno deve ser informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isCancelamento() && !Uteis.isAtributoPreenchido(obj.getMotivoAlteracao()), "O campo Motivo Cancelamento deve ser informado.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GestaoContasPagarVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {			
			incluir(obj, "GestaoContasPagar", new AtributoPersistencia()
					.add("gestaoContasPagarOperacaoEnum", obj.getGestaoContasPagarOperacaoEnum())
					.add("unidadeEnsino", obj.getUnidadeEnsinoVO())
					.add("usuarioOperacao", obj.getUsuarioOperacao())
					.add("dataRegistro", obj.getDataRegistro())
					.add("descricaoOperacao", obj.getDescricaoOperacao())
					,usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public void persitir(GestaoContasPagarVO obj, List<ContaPagarVO> lista, ProgressBarVO progressBarVO, boolean verificarAcesso , UsuarioVO usuario) throws Exception {
		GestaoContasPagar.incluir(getIdEntidade(), verificarAcesso, usuario);
		validarPermissaoOperacaoGestaoContaPagar(obj, usuario);
		validarDados(obj);				
		obj.inicializarDados(usuario);
		List<ContaPagarVO> listaContaPagar = lista.stream().filter(ContaPagarVO::isSelecionado).collect(Collectors.toList());
		executarOperacaoContasPagarEmLote(obj, listaContaPagar, progressBarVO, usuario);
		if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			executarInicializacaoDeLogs(obj, obj.getUnidadeEnsinoVO().getCodigo(), usuario);
			obj.getUnidadeEnsinoVO().setNome(listaContaPagar.isEmpty() ? "": listaContaPagar.get(0).getUnidadeEnsino().getNome());
			getFacadeFactory().getGestaoContasPagarFacade().incluir(obj, verificarAcesso, usuario);	
			obj.getListaGestaoContasPagarLogs().add((GestaoContasPagarVO) Uteis.clonar(obj));
		}else {
			executarSeparacaoContaPagarPorUnidadeEnsino(obj, listaContaPagar, verificarAcesso, usuario);
		}		
	}
	
	public void executarSeparacaoContaPagarPorUnidadeEnsino(GestaoContasPagarVO obj, List<ContaPagarVO> listaContaPagar, boolean verificarAcesso , UsuarioVO usuario) throws Exception {
		Map<Integer, List<ContaPagarVO>> mapa = listaContaPagar.stream().collect(Collectors.groupingBy(p -> p.getUnidadeEnsino().getCodigo()));
		mapa.entrySet().stream().forEach(p-> {
			GestaoContasPagarVO novoObj = new GestaoContasPagarVO();
			novoObj.setDescricaoOperacao(obj.getDescricaoOperacao());
			novoObj.setGestaoContasPagarOperacaoEnum(obj.getGestaoContasPagarOperacaoEnum());
			novoObj.setUsuarioOperacao(obj.getUsuarioOperacao());			
			novoObj.setMapaUnidadeEnsinoLog(obj.getMapaUnidadeEnsinoLog());
			novoObj.setMapaUnidadeEnsinoLogErro(obj.getMapaUnidadeEnsinoLogErro());
			novoObj.getUnidadeEnsinoVO().setCodigo(p.getKey());
			novoObj.getUnidadeEnsinoVO().setNome(p.getValue().isEmpty() ? "": p.getValue().get(0).getUnidadeEnsino().getNome());
			executarInicializacaoDeLogs(novoObj, novoObj.getUnidadeEnsinoVO().getCodigo(), usuario);
			getFacadeFactory().getGestaoContasPagarFacade().incluir(novoObj, verificarAcesso, usuario);
			obj.getListaGestaoContasPagarLogs().add(novoObj);
		});	
	}
	
	private void executarOperacaoContasPagarEmLote(GestaoContasPagarVO obj, List<ContaPagarVO> lista, ProgressBarVO progressBarVO, UsuarioVO usuario) {
		for (ContaPagarVO contaPagarVO : lista) {
			if (contaPagarVO.isSelecionado()) {
				try {
					progressBarVO.setStatus(obj.getPreencherStatusProgressBarVO(progressBarVO, contaPagarVO));
					switch (obj.getGestaoContasPagarOperacaoEnum()) {
					case ALTERACAO:
						executarOperacaoContasPagarEmLoteAlteracao(obj, contaPagarVO, usuario);
						break;
					case EXCLUSAO:
						getFacadeFactory().getContaPagarFacade().excluir(contaPagarVO, false, usuario);
						break;					
					case PAGAMENTO:
						executarOperacaoContasPagarEmLotePagamento(obj, contaPagarVO, usuario);
						break;
					case ESTORNO_PAGAMENTO:
						executarOperacaoContasPagarEmLoteEstornoPagamento(obj, contaPagarVO, usuario);
						break;
					case CANCELAMENTO:
						executarOperacaoContasPagarEmLoteCancelamento(obj, contaPagarVO, usuario);
						break;
					case ESTORNO_CANCELAMENTO:	
						getFacadeFactory().getContaPagarFacade().reativarContaPagarCancelada(contaPagarVO, false, usuario);
						break;
					}
					obj.getPreencherMapaUnidadeEnsino(obj.getMapaUnidadeEnsinoLog(), contaPagarVO.getUnidadeEnsino().getCodigo(), obj.getPreencherCamposDescricaoOperacao(contaPagarVO));
				} catch (Exception e) {
					String msg = obj.getPreencherCamposDescricaoOperacaoLog(contaPagarVO) + " - "+ e.getMessage();
					obj.getPreencherMapaUnidadeEnsino(obj.getMapaUnidadeEnsinoLogErro(), contaPagarVO.getUnidadeEnsino().getCodigo(), msg);
				} finally {
					progressBarVO.incrementar();
				}
			}
		}
	}
	
	private boolean validarAlteracaoContaPagarValor(ContaPagarVO contaPagarVO, ContaPagarVO cpTemp , UsuarioVO usuario) {
		boolean isAtualizarCentroResultado = false;
		if(!cpTemp.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento().equals(contaPagarVO.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento()) && !cpTemp.getTipoOrigemEnum().isAdiantamento() && Uteis.isAtributoPreenchido(cpTemp.getListaCentroResultadoOrigemVOs())) {
			isAtualizarCentroResultado = true;
			cpTemp.setValor(contaPagarVO.getValor());
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(cpTemp.getListaCentroResultadoOrigemVOs(), cpTemp.getPrevisaoValorPago(), usuario);	
		}else if (!cpTemp.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento().equals(contaPagarVO.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento()) && (cpTemp.getTipoOrigemEnum().isAdiantamento() || !Uteis.isAtributoPreenchido(cpTemp.getListaCentroResultadoOrigemVOs()))){
			cpTemp.setValor(contaPagarVO.getValor());
		}
		return isAtualizarCentroResultado;
	}
	
	private void executarOperacaoContasPagarEmLoteAlteracao(GestaoContasPagarVO obj, ContaPagarVO contaPagarVO, UsuarioVO usuario) throws Exception {
		ContaPagarVO cpTemp = getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(contaPagarVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		obj.setDataVencimentoAntesAlteracaoLog(cpTemp.getDataVencimento());
		obj.setValorAntesAlteracaoLog(cpTemp.getValor());
		boolean isAtualizarCentroResultado = validarAlteracaoContaPagarValor(contaPagarVO, cpTemp, usuario);
		cpTemp.setDataVencimento(contaPagarVO.getDataVencimento());
		getFacadeFactory().getContaPagarFacade().alterarContaPagarPorGestaoContasPagar(cpTemp, isAtualizarCentroResultado, usuario);
	}
	
	private void executarOperacaoContasPagarEmLotePagamento(GestaoContasPagarVO obj, ContaPagarVO contaPagarVO, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagarVO.getDataExecutarOperacaoTemp()), "O campo Data de Pagamento deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagarVO.getDataVencimento()), "O campo Data de Vencimento deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagarVO.getValor()), "O campo Valor deve ser informado.");
		ContaPagarVO cpTemp = getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(contaPagarVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		obj.setDataVencimentoAntesAlteracaoLog(cpTemp.getDataVencimento());
		obj.setValorAntesAlteracaoLog(cpTemp.getValor());
		validarAlteracaoContaPagarValor(contaPagarVO, cpTemp, usuario);
		cpTemp.setDataVencimento(contaPagarVO.getDataVencimento());
		cpTemp.setDataExecutarOperacaoTemp(contaPagarVO.getDataExecutarOperacaoTemp());
		getFacadeFactory().getNegociacaoPagamentoFacade().gerarNegociacaoContaPagarPorContaPagarPorFormaPagamentoNegociacaoPagamentoVO(cpTemp, obj.getFormaPagamentoNegociacaoPagamentoVO(), cpTemp.getDataExecutarOperacaoTemp(), false, usuario);
	}
	
	private void executarOperacaoContasPagarEmLoteEstornoPagamento(GestaoContasPagarVO obj, ContaPagarVO contaPagarVO, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagarVO.getDataExecutarOperacaoTemp()), "O campo Data de Estorno deve ser informado.");
		NegociacaoPagamentoVO neg =  getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorCodigoContaPagar(contaPagarVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		neg.setDataEstorno(contaPagarVO.getDataExecutarOperacaoTemp());
		neg.setMotivoAlteracao(obj.getMotivoAlteracao());
		neg.setDesconsiderarConciliacaoBancaria(obj.isDesconsiderarConciliacaoBancaria());
		getFacadeFactory().getNegociacaoPagamentoFacade().excluir(neg, usuario);
	}
	
	private void executarOperacaoContasPagarEmLoteCancelamento(GestaoContasPagarVO obj, ContaPagarVO contaPagarVO, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagarVO.getDataExecutarOperacaoTemp()), "O campo Data de Cancelamento deve ser informado.");
		contaPagarVO.setMotivoCancelamento(obj.getMotivoAlteracao());
		contaPagarVO.setDataCancelamento(contaPagarVO.getDataExecutarOperacaoTemp());
		getFacadeFactory().getContaPagarFacade().cancelarContaPagar(contaPagarVO, false, usuario);
	}
	
	private void executarInicializacaoDeLogs(GestaoContasPagarVO obj,  Integer unidadeEnsino, UsuarioVO usuario) {
		if(Uteis.isAtributoPreenchido(obj.getMapaUnidadeEnsinoLog()) &&
				obj.getMapaUnidadeEnsinoLog().entrySet().stream().anyMatch(p-> p.getKey().equals(unidadeEnsino))) {
			obj.preencherCabecalhoOperacao("Log de "+obj.getGestaoContasPagarOperacaoEnum().name()+" Conta Pagar");
			obj.getMapaUnidadeEnsinoLog().entrySet().stream().filter(p-> p.getKey().equals(unidadeEnsino)).flatMap(p-> p.getValue().stream()).forEach(erro->{
				obj.setDescricaoOperacao(obj.getDescricaoOperacao() + System.lineSeparator() + erro);
			});
		}
		if(Uteis.isAtributoPreenchido(obj.getMapaUnidadeEnsinoLogErro()) &&
				obj.getMapaUnidadeEnsinoLogErro().entrySet().stream().anyMatch(p-> p.getKey().equals(unidadeEnsino))) {
			obj.preencherCabecalhoOperacao("Log de "+obj.getGestaoContasPagarOperacaoEnum().name()+" Conta Pagar com erro");
			obj.getMapaUnidadeEnsinoLogErro().entrySet().stream().filter(p-> p.getKey().equals(unidadeEnsino)).flatMap(p-> p.getValue().stream()).forEach(erro->{
				obj.setDescricaoOperacao(obj.getDescricaoOperacao() + System.lineSeparator() + erro);
			});	
		}
	}
	
	@Override
	public void executarAplicacaoDaAlteracaoDataVencimento(GestaoContasPagarVO obj, List<ContaPagarVO> lista, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataVencimentoAlterar()), "O campo Data Vencimento Aplicar não foi informado.");
		if(!obj.isContasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente()) {
			lista.stream().filter(ContaPagarVO::isSelecionado).forEach(p-> p.setDataVencimento(obj.getDataVencimentoAlterar()));	
		}else {
			Map<String, List<ContaPagarVO>> mapa= lista.stream().filter(ContaPagarVO::isSelecionado).collect(Collectors.groupingBy(p -> p.getFavorecido()	+ p.getTipoOrigem_Apresentar() + p.getCodOrigem()));
			mapa.entrySet().stream().forEach(p-> executarAplicacaoDaAlteracaoDataVencimentoPorMesSubsquente(obj.getDataVencimentoAlterar(), p.getValue(), usuario));
		}
	}
	
	private void executarAplicacaoDaAlteracaoDataVencimentoPorMesSubsquente(Date dataBase, List<ContaPagarVO> lista, UsuarioVO usuario) {
		Integer nrMesAvancar = 0;
		for (ContaPagarVO contaPagarVO : lista) {
			contaPagarVO.setDataVencimento(Uteis.obterDataAvancadaPorMes(dataBase, nrMesAvancar));
			nrMesAvancar++;
		}
	}
	
	@Override
	public void executarAplicacaoDaAlteracaoDataOperacao(GestaoContasPagarVO obj, List<ContaPagarVO> lista, UsuarioVO usuario) {
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isPagamento() && !Uteis.isAtributoPreenchido(obj.getDataOperacaoAlterar()), "O campo Data Pagamento Aplicar não foi informado.");
		Uteis.checkState(obj.getGestaoContasPagarOperacaoEnum().isEstornoPagamento() && !Uteis.isAtributoPreenchido(obj.getDataOperacaoAlterar()), "O campo Data Estorno Pagamento Aplicar não foi informado.");
		lista.stream().filter(ContaPagarVO::isSelecionado).forEach(p-> p.setDataExecutarOperacaoTemp(obj.getDataOperacaoAlterar()));
	}
	
	@Override
	public void executarAplicacaoDaAlteracaoValor(GestaoContasPagarVO obj, List<ContaPagarVO> lista, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorAlterar()), "O campo Valor Aplicar não foi informado.");
		lista.stream().filter(ContaPagarVO::isSelecionado).forEach(p-> p.setValor(obj.getValorAlterar()));
	}
	
	
	private StringBuilder getSQLPadraoConsultaBasicaContaPagar() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\", fornecedor.cpf as \"fornecedor.cpf\", fornecedor.cnpj as \"fornecedor.cnpj\", ");
		sql.append(" fornecedor.nomePessoaFisica as \"fornecedor.nomePessoaFisica\", fornecedor.isTemMei as \"fornecedor.isTemMei\", fornecedor.cpfFornecedorMei as \"fornecedor.cpfFornecedorMei\", ");
		sql.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", parceiro.cpf as \"parceiro.cpf\", parceiro.cnpj as \"parceiro.cnpj\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.cpf as \"pessoa.cpf\",  ");
		sql.append(" pessoafuncionario.codigo as \"pessoafuncionario.codigo\", pessoafuncionario.nome as \"pessoafuncionario.nome\", pessoafuncionario.cpf as \"pessoafuncionario.cpf\",  ");
		sql.append(" responsavelFinanceiro.codigo as \"responsavelFinanceiro.codigo\", responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", responsavelFinanceiro.cpf as \"responsavelFinanceiro.cpf\", ");
		sql.append(" banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");
		sql.append(" operadoracartao.codigo as \"operadoracartao.codigo\", operadoracartao.nome as \"operadoracartao.nome\", ");
		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sql.append(" contapagar.codigo as \"contapagar.codigo\", contapagar.parcela as \"contapagar.parcela\", ");
		sql.append(" contapagar.situacao as \"contapagar.situacao\", contapagar.nrdocumento as \"contapagar.nrdocumento\", ");
		sql.append(" contapagar.datavencimento as \"contapagar.datavencimento\", contapagar.valor as \"contapagar.valor\", ");
		sql.append(" contapagar.tipoSacado as \"contapagar.tipoSacado\", contapagar.data as \"contapagar.data\", ");
		sql.append(" contapagar.unidadeEnsino as \"contapagar.unidadeEnsino\", contapagar.descricao as \"contapagar.descricao\", ");
		sql.append(" contapagar.dataFatoGerador as \"contapagar.dataFatoGerador\",  ");
		sql.append(" contapagar.codOrigem as \"contapagar.codOrigem\", contapagar.tipoOrigem as \"contapagar.tipoOrigem\", ");
		sql.append(" (select negociacaopagamento.data from negociacaopagamento ");
		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar  = negociacaopagamento.codigo ");
		sql.append(" where contapagarnegociacaopagamento.contapagar = contapagar.codigo) as dataNegociacaoPagamento ");
		sql.append(" FROM contapagar ");
		sql.append(" left join unidadeEnsino on unidadeEnsino.codigo = contapagar.unidadeEnsino ");
		sql.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sql.append(" left join funcionario on funcionario.codigo = contapagar.funcionario  ");
		sql.append(" left join parceiro on parceiro.codigo = contapagar.parceiro  ");
		sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
		sql.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
		sql.append(" left join banco on banco.codigo = contapagar.banco ");
		sql.append(" left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");		
		return sql;
	}
	
	private void preencherDadosParaConsultaFiltrosContaPagar(StringBuilder sql, GestaoContasPagarVO obj, List<Object> filtros) {
		sql.append("  and contapagar.situacao = ? ");
		if(obj.getGestaoContasPagarOperacaoEnum().isEstornoPagamento()) {
			filtros.add("PA");
		}else if(obj.getGestaoContasPagarOperacaoEnum().isEstornoCancelamento()) {
			filtros.add("CF");
		}else {
			filtros.add("AP");
		}		
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			sql.append(" AND contapagar.unidadeensino = ? ");
			filtros.add(obj.getUnidadeEnsinoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getDataVencimentoInicio()) && Uteis.isAtributoPreenchido(obj.getDataVencimentoFim())) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(obj.getDataVencimentoInicio(), obj.getDataVencimentoFim(), "contapagar.datavencimento", false));
		}
		if (obj.getGestaoContasPagarOperacaoEnum().isEstornoPagamento() && (Uteis.isAtributoPreenchido(obj.getDataOperacaoInicio()) || Uteis.isAtributoPreenchido(obj.getDataOperacaoFim()))) {
			sql.append(" and exists (select negociacaopagamento.codigo from negociacaopagamento ");
			sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar  = negociacaopagamento.codigo ");
			sql.append(" where contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
			sql.append(" and ").append(realizarGeracaoWherePeriodo(obj.getDataOperacaoInicio(), obj.getDataOperacaoFim(), "negociacaopagamento.data", false));
			sql.append(" )");
		}
		if (obj.getGestaoContasPagarOperacaoEnum().isEstornoCancelamento() && (Uteis.isAtributoPreenchido(obj.getDataOperacaoInicio()) || Uteis.isAtributoPreenchido(obj.getDataOperacaoFim()))) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(obj.getDataOperacaoInicio(), obj.getDataOperacaoFim(), "contapagar.dataCancelamento", false));
		}
		if (Uteis.isAtributoPreenchido(obj.getValorFaixaInicio()) && Uteis.isAtributoPreenchido(obj.getValorFaixaFim())) {
			sql.append(" and contapagar.valor >= ? and contapagar.valor <= ?  ");
			filtros.add(obj.getValorFaixaInicio());
			filtros.add(obj.getValorFaixaFim());
		}else if (Uteis.isAtributoPreenchido(obj.getValorFaixaInicio()) && !Uteis.isAtributoPreenchido(obj.getValorFaixaFim())) {
			sql.append(" and contapagar.valor >= ? ");
			filtros.add(obj.getValorFaixaInicio());
		} else if (!Uteis.isAtributoPreenchido(obj.getValorFaixaInicio()) && Uteis.isAtributoPreenchido(obj.getValorFaixaFim())) {
			sql.append(" and contapagar.valor <= ? ");
			filtros.add(obj.getValorFaixaFim());
		}
		preencherDadosParaFiltrosCampoConsulta(sql, obj, filtros);
	}

	private void preencherDadosParaFiltrosCampoConsulta(StringBuilder sql, GestaoContasPagarVO obj, List<Object> filtros) {
		if (Uteis.isAtributoPreenchido(obj.getCampoConsulta())) {
			ContaPagarVO.enumCampoConsultaContaPagar enumCampoConsulta = ContaPagarVO.enumCampoConsultaContaPagar.valueOf(obj.getCampoConsulta());
			switch (enumCampoConsulta) {
			case CODIGO:
				sql.append(" AND contapagar.codigo = ? ");
				filtros.add(Uteis.getValorInteiro(obj.getValorConsulta()));
				break;
			case FAVORECIDO:
				sql.append(" and (upper(sem_acentos(fornecedor.nome)) ilike(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) ilike(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(parceiro.nome)) ilike(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(banco.nome)) ilike(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(pessoa.nome)) ilike(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(operadoracartao.nome)) ilike(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(responsavelFinanceiro.nome)) ilike(sem_acentos(?)) ");
				sql.append("  ) ");
				filtros.add(obj.getValorConsulta()+PERCENT);
				filtros.add(obj.getValorConsulta()+PERCENT);
				filtros.add(obj.getValorConsulta()+PERCENT);
				filtros.add(obj.getValorConsulta()+PERCENT);
				filtros.add(obj.getValorConsulta()+PERCENT);
				filtros.add(obj.getValorConsulta()+PERCENT);
				filtros.add(obj.getValorConsulta()+PERCENT);
				break;
			case CNPJ_FORNECEDOR:
				sql.append(" and ((fornecedor.CNPJ like(?) and fornecedor.tipoempresa = 'JU') or (parceiro.CNPJ like(?) and parceiro.tipoempresa = 'JU')) ");
				filtros.add(obj.getValorConsulta());
				filtros.add(obj.getValorConsulta());
				break;
			case CPF_FAVORECIDO:
				sql.append(" and ( ");
				sql.append(" (fornecedor.cpf like(?) and fornecedor.tipoempresa = 'FI')  ");
				sql.append(" or (parceiro.cpf like(?) and parceiro.tipoempresa = 'FI') ");
				sql.append(" or (pessoafuncionario.cpf like(?)) ");
				sql.append(" or (pessoa.cpf like(?)) ");
				sql.append(" or (responsavelFinanceiro.cpf like(?)) ");
				sql.append(" ) ");
				filtros.add(obj.getValorConsulta());
				filtros.add(obj.getValorConsulta());
				filtros.add(obj.getValorConsulta());
				filtros.add(obj.getValorConsulta());
				filtros.add(obj.getValorConsulta());
				break;
			case TIPO_ORIGEM:
				Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorConsulta()), "O campo Código de Origem deve ser informado.");
				sql.append(" AND contapagar.tipoorigem=? ");
				filtros.add(obj.getOrigemContaPagarEnum().getValor().toUpperCase());			
				sql.append(" AND (sem_acentos(contapagar.codorigem)) ilike(sem_acentos(?)) ");
				filtros.add(obj.getValorConsulta().toUpperCase());			
				break;
			case NR_DOCUMENTO:
				sql.append(" AND (sem_acentos(contapagar.nrdocumento)) ilike(sem_acentos(?)) ");
				filtros.add(obj.getValorConsulta().toUpperCase()+PERCENT);			
				break;
			case CODIGO_PAGAMENTO:
				sql.append(" and contapagar.codigo in (select contapagar from contapagarnegociacaopagamento where negociacaocontapagar = ? )");
				filtros.add(Uteis.getValorInteiro(obj.getValorConsulta()));
				break;
			case TURMA:
				sql.append(" and exists (select centroresultadoorigem.codigo from centroresultadoorigem inner join turma on turma.codigo = centroresultadoorigem.turma ");
				sql.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
				sql.append(" and (sem_acentos(turma.identificadorTurma)) ilike(sem_acentos(?))) ");
				filtros.add(obj.getValorConsulta().toUpperCase()+PERCENT);
				break;
			case DEPARTAMENTO:
				sql.append(" and exists (select centroresultadoorigem.codigo from centroresultadoorigem inner join departamento on departamento.codigo = centroresultadoorigem.departamento ");
				sql.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
				sql.append(" and (sem_acentos(departamento.nome)) ilike(sem_acentos(?))) ");
				filtros.add(obj.getValorConsulta().toUpperCase()+PERCENT);
				break;
			case NUMERO_NOTA_FISCAL_ENTRADA:
				sql.append(" and contapagar.tipoorigem = 'NE' and exists (select notafiscalentrada.codigo from notafiscalentrada ");
				sql.append(" where notafiscalentrada.codigo = contapagar.codorigem::integer ");
				sql.append(" and notafiscalentrada.numero =?) ");
				filtros.add(Uteis.getValorInteiro(obj.getValorConsulta()));
				break;
			case CODIGO_NOTA_FISCAL_ENTRADA:
				sql.append(" and (sem_acentos(contapagar.codigonotafiscalentrada)) ilike(sem_acentos(?)) ");
				filtros.add(PERCENT+obj.getValorConsulta().toUpperCase()+PERCENT);
				break;
			default:
				throw new StreamSeiException("Não foi encontrado implementado o campo consulta para essa opção.");			
			}
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ContaPagarVO> consultar(GestaoContasPagarVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		validarPermissaoOperacaoGestaoContaPagar(obj, usuario);
		List<Object> filtros = new ArrayList<>();
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaContaPagar();
		sqlStr.append(" where 1=1 ");
		preencherDadosParaConsultaFiltrosContaPagar(sqlStr, obj, filtros);
		sqlStr.append(" order by ");
		sqlStr.append(" ( case when contapagar.tiposacado = 'FO' then fornecedor.nome ");
		sqlStr.append(" 	   when contapagar.tiposacado = 'FU' then pessoafuncionario.nome ");
		sqlStr.append(" 	   when contapagar.tiposacado = 'BA' then banco.nome ");
		sqlStr.append(" 	   when contapagar.tiposacado = 'AL' then pessoa.nome ");
		sqlStr.append(" 	   when contapagar.tiposacado = 'OC' then operadoracartao.nome ");
		sqlStr.append(" 	   when contapagar.tiposacado = 'RF' then responsavelFinanceiro.nome ");
		sqlStr.append(" 	   else '' end),");
		sqlStr.append(" contapagar.tipoorigem , contapagar.codorigem, contapagar.datavencimento ");
		
		
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		return montarDadosConsultaResumida(tabelaResultado);
	}
	
	private List<ContaPagarVO> montarDadosConsultaResumida(SqlRowSet tabelaResultado) {
		List<ContaPagarVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosResumido(tabelaResultado));
		}
		return vetResultado;
	}	
	
	private ContaPagarVO montarDadosResumido(SqlRowSet dadosSQL) {
		ContaPagarVO obj = new ContaPagarVO();
		obj.setCodigo((dadosSQL.getInt("contapagar.codigo")));
		obj.setSituacao(dadosSQL.getString("contapagar.situacao"));
		obj.setDataVencimento(dadosSQL.getDate("contapagar.datavencimento"));
		obj.setNrDocumento(dadosSQL.getString("contapagar.nrdocumento"));
		obj.setValor(dadosSQL.getDouble("contapagar.valor"));
		obj.setParcela(dadosSQL.getString("contapagar.parcela"));
		obj.setTipoSacado(dadosSQL.getString("contapagar.tipoSacado"));
		obj.setData(dadosSQL.getDate("contapagar.data"));
		obj.setDataFatoGerador(dadosSQL.getDate("contapagar.dataFatoGerador"));
		obj.setDescricao(dadosSQL.getString("contapagar.descricao"));
		obj.setCodOrigem(dadosSQL.getString("contapagar.codOrigem"));
		obj.setTipoOrigem(dadosSQL.getString("contapagar.tipoOrigem"));
		
		
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeensino.codigo")));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getPessoa().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getPessoa().setCPF(dadosSQL.getString("pessoa.cpf"));
		
		obj.getResponsavelFinanceiro().setCodigo((dadosSQL.getInt("responsavelFinanceiro.codigo")));
		obj.getResponsavelFinanceiro().setNome(dadosSQL.getString("responsavelFinanceiro.nome"));
		obj.getResponsavelFinanceiro().setCPF(dadosSQL.getString("responsavelFinanceiro.cpf"));
		
		obj.getFuncionario().getPessoa().setCodigo((dadosSQL.getInt("pessoafuncionario.codigo")));
		obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("pessoafuncionario.nome"));
		obj.getFuncionario().getPessoa().setCPF(dadosSQL.getString("pessoafuncionario.cpf"));
		
		
		obj.getFornecedor().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
		obj.getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));
		obj.getFornecedor().setCPF(dadosSQL.getString("fornecedor.cpf"));
		obj.getFornecedor().setCNPJ(dadosSQL.getString("fornecedor.cnpj"));
		obj.getFornecedor().setNomePessoaFisica(dadosSQL.getString("fornecedor.nomePessoaFisica"));
		obj.getFornecedor().setCpfFornecedor(dadosSQL.getString("fornecedor.cpfFornecedorMei"));
		obj.getFornecedor().setIsTemMei(dadosSQL.getBoolean("fornecedor.isTemMei"));
		
		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro.codigo"));
		obj.getParceiro().setNome(dadosSQL.getString("parceiro.nome"));
		obj.getParceiro().setCPF(dadosSQL.getString("parceiro.cpf"));
		obj.getParceiro().setCNPJ(dadosSQL.getString("parceiro.cnpj"));
		
		obj.getBanco().setCodigo((dadosSQL.getInt("banco.codigo")));
		obj.getOperadoraCartao().setNome(dadosSQL.getString("banco.nome"));
		
		obj.getOperadoraCartao().setCodigo(dadosSQL.getInt("operadoracartao.codigo"));
		obj.getOperadoraCartao().setNome(dadosSQL.getString("operadoracartao.nome"));	
		obj.setDataNegociacaoPagamentoTemp(dadosSQL.getDate("dataNegociacaoPagamento"));

	
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, GestaoContasPagarVO obj) {
		List<GestaoContasPagarVO> objs =  consultaRapidaPorFiltros(obj, dataModelo);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
		dataModelo.setListaConsulta(objs);
	}	
	
	private List<GestaoContasPagarVO> consultaRapidaPorFiltros(GestaoContasPagarVO obj, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY gestaocontaspagar.dataregistro desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Integer consultarTotalPorFiltros(GestaoContasPagarVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void montarFiltrosParaConsulta(GestaoContasPagarVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if(Uteis.isAtributoPreenchido(obj.getCodigo())){
			sqlStr.append(" and gestaocontaspagar.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());	
		}
		if(Uteis.isAtributoPreenchido(obj.getGestaoContasPagarOperacaoEnum())){
			sqlStr.append(" and gestaocontaspagar.gestaocontaspagaroperacaoenum = ? ");
			dataModelo.getListaFiltros().add(obj.getGestaoContasPagarOperacaoEnum().name());	
		}
		if (Uteis.isAtributoPreenchido(dataModelo.getDataIni())) {
			sqlStr.append(" and gestaocontaspagar.dataregistro >= '").append(Uteis.getDataBD0000(dataModelo.getDataIni())).append("' ");			
		}
		if (Uteis.isAtributoPreenchido(dataModelo.getDataFim())) {
			sqlStr.append(" and gestaocontaspagar.dataregistro <= '").append(Uteis.getDataBD2359(dataModelo.getDataFim())).append("' ");
		}
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			sqlStr.append(" and gestaocontaspagar.unidadeensino = ? ");
			dataModelo.getListaFiltros().add(obj.getUnidadeEnsinoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getUsuarioOperacao().getNome())) {
			sqlStr.append(" and lower(sem_acentos(usuario.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getUsuarioOperacao().getNome().toLowerCase() + PERCENT);
		}
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" gestaocontaspagar.codigo as \"gestaocontaspagar.codigo\", gestaocontaspagar.gestaocontaspagaroperacaoenum as \"gestaocontaspagar.gestaocontaspagaroperacaoenum\", ");
		sql.append(" gestaocontaspagar.dataregistro as \"gestaocontaspagar.dataregistro\", gestaocontaspagar.descricaooperacao as \"gestaocontaspagar.descricaooperacao\", ");		
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",  ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", usuario.username as \"usuario.username\" ");
		sql.append(" FROM gestaocontaspagar ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = gestaocontaspagar.unidadeensino ");
		sql.append(" inner join usuario on usuario.codigo = gestaocontaspagar.usuariooperacao ");				
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(gestaocontaspagar.codigo) as qtde FROM gestaocontaspagar  ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = gestaocontaspagar.unidadeensino ");
		sql.append(" inner join usuario on usuario.codigo = gestaocontaspagar.usuariooperacao ");
		return sql;
	}
	
	private List<GestaoContasPagarVO> montarDadosConsulta(SqlRowSet tabelaResultado) {
		List<GestaoContasPagarVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}	
	
	private GestaoContasPagarVO montarDados(SqlRowSet dadosSQL) {
		GestaoContasPagarVO obj = new GestaoContasPagarVO();
		obj.setCodigo((dadosSQL.getInt("gestaocontaspagar.codigo")));
		obj.setGestaoContasPagarOperacaoEnum(GestaoContasPagarOperacaoEnum.valueOf(dadosSQL.getString("gestaocontaspagar.gestaocontaspagaroperacaoenum")));
		obj.setDescricaoOperacao(dadosSQL.getString("gestaocontaspagar.descricaoOperacao"));
		obj.setDataRegistro(dadosSQL.getDate("gestaocontaspagar.dataregistro"));
		obj.getUsuarioOperacao().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getUsuarioOperacao().setUsername(dadosSQL.getString("usuario.username"));
		obj.getUsuarioOperacao().setNome(dadosSQL.getString("usuario.nome"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		return obj;
	}
	
	
	
	

}
