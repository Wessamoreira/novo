package negocio.facade.jdbc.financeiro;

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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.EstatisticasLancamentosFuturosVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.MapaLancamentoFuturoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MapaLancamentoFuturoVO</code>. Responsável por implementar operações como
 * incluir, alterar, excluir e consultar pertinentes a classe <code>MapaLancamentoFuturoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MapaLancamentoFuturoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MapaLancamentoFuturo extends ControleAcesso implements MapaLancamentoFuturoInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -6762840281771152044L;
    protected static String idEntidade;

    public MapaLancamentoFuturo() throws Exception {
        super();
        setIdEntidade("MapaLancamentoFuturo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MapaLancamentoFuturoVO</code>.
     */
    public MapaLancamentoFuturoVO novo() throws Exception {
        MapaLancamentoFuturo.incluir(getIdEntidade());
        MapaLancamentoFuturoVO obj = new MapaLancamentoFuturoVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void baixarCheque(List<MapaLancamentoFuturoVO> lista, UsuarioVO usuario) throws Exception {
        List<Integer> chequePagarVOs = new ArrayList<Integer>(0);
        List<Integer> chequeReceberVOs = new ArrayList<Integer>(0);
        try {

            ChequeVO chequeVO;
            for (MapaLancamentoFuturoVO mapaLancamentoFuturoVO : lista) {

                if (mapaLancamentoFuturoVO.getBaixarCheque()) {
                    chequeVO = (ChequeVO) getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(mapaLancamentoFuturoVO.getCodigoCheque(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
                    if (mapaLancamentoFuturoVO.getTipoMapaLancamentoFuturo().equals(TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.getValor())) {
                    	
    					if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(mapaLancamentoFuturoVO.getDataBaixa(), chequeVO.getContaCorrente().getNumero(), usuario)) {
    						throw new Exception("Não é possível realizar a baixa do cheque na conta corrente " + chequeVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(mapaLancamentoFuturoVO.getDataBaixa()) + ", pois a conciliação bancária já está finalizada.");
    					}
                    	
                    	chequeVO.setDataBaixa(mapaLancamentoFuturoVO.getDataBaixa());
                        efetuarBaixaPagamentoDoCheque(usuario, chequeVO);
                        if (chequeVO.getChequeProprio()) {
                            chequePagarVOs.add(chequeVO.getCodigo());
                            getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().alterarDataFimApresentacaoSituacaoExtratoPorCodigoChequeSituacao(chequeVO.getCodigo(), mapaLancamentoFuturoVO.getDataBaixa(), TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.name(), Boolean.TRUE, usuario);
                        }
                    } else if (mapaLancamentoFuturoVO.getTipoMapaLancamentoFuturo().equals(TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.getValor())) {
                    	
    					if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(mapaLancamentoFuturoVO.getDataBaixa(), chequeVO.getLocalizacaoCheque().getNumero(), usuario)) {
    						throw new Exception("Não é possível realizar a baixa do cheque na conta corrente " + chequeVO.getLocalizacaoCheque().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(mapaLancamentoFuturoVO.getDataBaixa()) + ", pois a conciliação bancária já está finalizada.");
    					}
    					
                        chequeVO.setTarifaAntecipacao(mapaLancamentoFuturoVO.getTaxaDescontoCheque());
                        chequeVO.setValorDescontoAntecipacao(mapaLancamentoFuturoVO.getValorTaxaDescontoCheque());
                        chequeVO.setDataBaixa(mapaLancamentoFuturoVO.getDataBaixa());
                        efetuarBaixaRecebimentoDoCheque(usuario, chequeVO);
                        chequeReceberVOs.add(chequeVO.getCodigo());
                        getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().alterarDataFimApresentacaoSituacaoExtratoPorCodigoChequeSituacao(chequeVO.getCodigo(), mapaLancamentoFuturoVO.getDataBaixa(), TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.name(), Boolean.TRUE, usuario);
                    }
                    excluirSemComit(mapaLancamentoFuturoVO);
                }
            }
            criarMovimentacaoCaixaContaCorrentePagamento(chequePagarVOs, usuario);
            criarMovimentacaoCaixaContaCorrenteRecebimento(chequeReceberVOs, usuario);
            getFacadeFactory().getLancamentoContabilFacade().realizarAtualizacaoLancamentoContabilPorCheque(chequePagarVOs, false, usuario);
            getFacadeFactory().getLancamentoContabilFacade().realizarAtualizacaoLancamentoContabilPorCheque(chequeReceberVOs, true, usuario);
        } catch (Exception e) {
            throw e;
        } finally {
            chequePagarVOs.clear();
            chequePagarVOs = null;
            chequeReceberVOs.clear();
            chequeReceberVOs = null;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixaContaCorrenteRecebimento(List<Integer> cheques, UsuarioVO usuario) throws Exception {

        if (!cheques.isEmpty()) {
            StringBuilder sql = new StringBuilder("INSERT INTO ExtratoContaCorrente( valor, data, origemExtratoContaCorrente, tipoMovimentacaoFinanceira, codigoOrigem, ");
            sql.append(" codigoCheque, sacadoCheque, numeroCheque, bancoCheque, contaCorrenteCheque, ");
            sql.append(" agenciaCheque, dataPrevisaoCheque, nomeSacado, codigoSacado, tipoSacado,  ");
            sql.append(" contaCorrente, unidadeEnsino, responsavel, formaPagamento) ");
            sql.append(" select cheque.valor, cheque.dataBaixa, 'COMPENSACAO_CHEQUE' as origemExtratoContaCorrente, 'ENTRADA' as tipoMovimentacaoFinanceira,  negociacaorecebimento.codigo, ");
            sql.append(" cheque.codigo, cheque.sacado, cheque.numero, cheque.banco, cheque.numeroContaCorrente , cheque.agencia, cheque.dataPrevisao,  ");
            sql.append(" case when pessoa.codigo > 0 then pessoa.nome else case when parceiro.codigo > 0 then parceiro.nome else fornecedor.nome end end as nomeSacado, ");
            sql.append(" case when pessoa.codigo > 0 then pessoa.codigo else case when parceiro.codigo > 0 then  parceiro.codigo else fornecedor.codigo end end as codigoSacado, ");
            sql.append(" case negociacaorecebimento.tipoPessoa  ");
            sql.append(" when 'FO' then 'FORNECEDOR' ");
            sql.append(" when 'FU' then 'FUNCIONARIO_PROFESSOR' ");
            sql.append(" when 'AL' then 'ALUNO'  ");
            sql.append(" when 'RF' then 'RESPONSAVEL_FINANCEIRO' ");
            sql.append(" when 'BA' then 'BANCO'  ");
            sql.append(" when 'CA' then 'CANDIDATO' ");
            sql.append(" when 'RE' then 'REQUERENTE'  ");
            sql.append(" when 'PA' then 'PARCEIRO' ");
            sql.append(" else 'ALUNO' end as tipoSacado, cheque.localizacaocheque AS contacorrente, cheque.unidadeEnsino, " + usuario.getCodigo() + ", ");
            sql.append(" case when formapagamentonegociacaorecebimento.formaPagamento is null then (select codigo from formaPagamento where tipo = 'CH' limit 1) else formapagamentonegociacaorecebimento.formaPagamento ");
            sql.append(" end as formaPagamento ");
            sql.append(" from cheque ");
            sql.append(" left join formapagamentonegociacaorecebimento  on cheque.codigo = formapagamentonegociacaorecebimento.cheque ");
            sql.append(" left join negociacaorecebimento on negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaoRecebimento ");
            sql.append(" left join pessoa on pessoa.codigo = cheque.pessoa ");
            sql.append(" left join parceiro on parceiro.codigo = cheque.parceiro ");
            sql.append(" left join fornecedor on fornecedor.codigo = cheque.fornecedor ");
            sql.append(" inner join contacorrente on contacorrente.codigo = cheque.localizacaocheque ");
            sql.append(" where contacorrente.contaCaixa = false  ");
            sql.append(" and cheque.codigo in ( ");
            int x = 0;
            for (Integer cdg : cheques) {
                if (x == 0) {
                    sql.append(cdg);
                    x++;
                } else {
                    sql.append(", ").append(cdg);
                }
            }
            sql.append(") ");
            getConexao().getJdbcTemplate().execute(sql.toString()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixaContaCorrentePagamento(List<Integer> cheques, UsuarioVO usuario) throws Exception {

        if (!cheques.isEmpty()) {
            StringBuilder sql = new StringBuilder("INSERT INTO ExtratoContaCorrente( valor, data, origemExtratoContaCorrente, tipoMovimentacaoFinanceira, codigoOrigem,");
            sql.append(" codigoCheque, sacadoCheque, numeroCheque, bancoCheque, contaCorrenteCheque,");
            sql.append(" agenciaCheque, dataPrevisaoCheque, nomeSacado, codigoSacado, tipoSacado,");
            sql.append(" contaCorrente, unidadeEnsino, responsavel, formaPagamento) ");
            sql.append(" select formapagamentonegociacaopagamento.valor, cheque.dataBaixa, 'PAGAMENTO' as origemExtratoContaCorrente, 'SAIDA' as tipoMovimentacaoFinanceira,  negociacaopagamento.codigo, ");
            sql.append(" cheque.codigo, cheque.sacado, cheque.numero, cheque.banco, cheque.numeroContaCorrente , cheque.agencia, cheque.dataPrevisao, ");
            sql.append(" case negociacaopagamento.tipoSacado ");
            sql.append(" when 'FO' then fornecedor.nome  ");
            sql.append(" when 'FU' then pessoaFuncionario.nome ");
            sql.append(" when 'AL' then aluno.nome  ");
            sql.append(" when 'RF' then responsavelFinanceiro.nome ");
            sql.append(" when 'BA' then banco.nome  ");
            sql.append(" when 'PA' then parceiro.nome end as nomeSacado, ");
            sql.append(" case negociacaopagamento.tipoSacado  ");
            sql.append(" when 'FO' then fornecedor.codigo  ");
            sql.append(" when 'FU' then pessoaFuncionario.codigo ");
            sql.append(" when 'AL' then aluno.codigo  ");
            sql.append(" when 'RF' then responsavelFinanceiro.codigo ");
            sql.append(" when 'BA' then banco.codigo  ");
            sql.append(" when 'PA' then parceiro.codigo end as codigoSacado, ");
            sql.append(" case negociacaopagamento.tipoSacado ");
            sql.append(" when 'FO' then 'FORNECEDOR' ");
            sql.append(" when 'FU' then 'FUNCIONARIO_PROFESSOR' ");
            sql.append(" when 'AL' then 'ALUNO'  ");
            sql.append(" when 'RF' then 'RESPONSAVEL_FINANCEIRO' ");
            sql.append(" when 'BA' then 'BANCO'  ");
            sql.append(" when 'PA' then 'PARCEIRO' end as tipoSacado, cheque.contacorrente, cheque.unidadeEnsino, " + usuario.getCodigo() + ", formaPagamento.codigo ");
            sql.append(" from formapagamentonegociacaopagamento   ");
            sql.append(" inner join formaPagamento on   formaPagamento.codigo = formapagamentonegociacaopagamento.formaPagamento ");
            sql.append(" inner join negociacaopagamento on negociacaopagamento.codigo = negociacaoContaPagar ");
            sql.append(" left join fornecedor on fornecedor.codigo = negociacaopagamento.fornecedor ");
            sql.append(" left join funcionario on funcionario.codigo = negociacaopagamento.funcionario ");
            sql.append(" left join pessoa pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
            sql.append(" left join pessoa aluno on aluno.codigo = negociacaopagamento.aluno ");
            sql.append(" left join pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = negociacaopagamento.responsavelFinanceiro ");
            sql.append(" left join banco on banco.codigo = negociacaopagamento.banco ");
            sql.append(" left join parceiro on parceiro.codigo = negociacaopagamento.parceiro ");
            sql.append(" inner join cheque on cheque.pagamento = formapagamentonegociacaopagamento.codigo ");
            sql.append(" inner join contacorrente on contacorrente.codigo = cheque.contacorrente ");
            sql.append(" where formaPagamento.tipo in ('CH') and contacorrente.contaCaixa = false AND cheque.chequeproprio = true ");
            sql.append(" and   cheque.codigo in ( ");
            int x = 0;
            for (Integer cdg : cheques) {
                if (x == 0) {
                    sql.append(cdg);
                    x++;
                } else {
                    sql.append(", ").append(cdg);
                }
            }
            sql.append(") ");
            getConexao().getJdbcTemplate().execute(sql.toString()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void efetuarBaixaPagamentoDoCheque(UsuarioVO usuario, ChequeVO chequeVO) throws Exception {
        //chequeVO.setDataBaixa(new Date());
        chequeVO.setResponsavelBaixa(usuario);
        chequeVO.setSituacao(SituacaoCheque.PAGAMENTO.getValor());
        chequeVO.setTarifaAntecipacao(0.0);
        chequeVO.setDataAntecipacao(null);
        chequeVO.setValorDescontoAntecipacao(0.0);
        chequeVO.setPago(true);
        getFacadeFactory().getChequeFacade().alterarChequeBaixa(chequeVO);
        getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(chequeVO.getContaCorrente().getCodigo(), "SA", chequeVO.getValor(), usuario);

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void efetuarBaixaDevolucaoDoCheque(UsuarioVO usuarioVO, ChequeVO chequeVO) throws Exception {
        chequeVO.setDataBaixa(new Date());
        chequeVO.setResponsavelBaixa(usuarioVO);
        /**
         * Alterada a situacao do cheque de devolvido para devolvido sacado devido ao fato de esta sendo feito um recebimento do cheque devolvido.
         * Sendo assim, o cheque foi devolvido ao sacado e nao poderia estar com a situacao devolvido.
         * Autorizado pelo Thyago Jayme.
         */
        chequeVO.setSituacao(SituacaoCheque.DEVOLVIDO_AO_SACADO.getValor());
        chequeVO.setLocalizacaoCheque(new ContaCorrenteVO());
        chequeVO.setTarifaAntecipacao(0.0);
        chequeVO.setDataAntecipacao(null);
        chequeVO.setValorDescontoAntecipacao(0.0);
        /**
         * Alterada cheque pago para false devido ao fato do cheque nunca ter sido compensado. Autorizado pelo Thyago Jayme.
         */
        chequeVO.setPago(false);
        getFacadeFactory().getChequeFacade().alterarChequeBaixa(chequeVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void baixarPendenciaAtravezDeNegociacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
        Iterator<ContaReceberNegociacaoRecebimentoVO> i = negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().iterator();
        while (i.hasNext()) {
            ContaReceberVO contaReceberVO = i.next().getContaReceber();
            if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())) {
                DevolucaoChequeVO devolucaoChequeVO = getFacadeFactory().getDevolucaoChequeFacade().consultarPorChavePrimaria(Uteis.getValorInteiro(contaReceberVO.getCodOrigem()), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
                FormaPagamentoVO formaPagamentoVO = getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo("CH", Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
                getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(devolucaoChequeVO.getCheque().getValor(), devolucaoChequeVO.getCheque().getLocalizacaoCheque().getCodigo(), TipoMovimentacaoFinanceira.SAIDA.getValor(), TipoOrigemMovimentacaoCaixa.DEVOLUCAO_CHEQUE.getValor(), formaPagamentoVO.getCodigo(), negociacaoRecebimentoVO.getCodigo(), negociacaoRecebimentoVO.getResponsavel().getCodigo(), negociacaoRecebimentoVO.getPessoa().getCodigo(), negociacaoRecebimentoVO.getFornecedor().getCodigo(), 0, negociacaoRecebimentoVO.getParceiroVO().getCodigo(), devolucaoChequeVO.getCheque(), 0, usuario);
                efetuarBaixaDevolucaoDoCheque(negociacaoRecebimentoVO.getResponsavel(), devolucaoChequeVO.getCheque());
                excluirSemComit(consultarPorOrigem(devolucaoChequeVO.getCodigo(), OrigemMapaLancamentoFuturo.DEVOLUCAO_CHEQUE.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
                getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().alterarDataFimApresentacaoExtratoPorCodigoChequeSituacao(devolucaoChequeVO.getCheque().getCodigo(), new Date(), TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.name(), usuario);
            }
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void baixarPendenciaAtravezDeRenegociacaoRecebimento(NegociacaoContaReceberVO negociacaoContaReceberVO, ContaReceberNegociadoVO contaReceberNegociadoVO, UsuarioVO usuario) throws Exception {
    	DevolucaoChequeVO devolucaoChequeVO = getFacadeFactory().getDevolucaoChequeFacade().consultarPorChavePrimaria(Uteis.getValorInteiro(contaReceberNegociadoVO.getContaReceber().getCodOrigem()), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        FormaPagamentoVO formaPagamentoVO = getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo("CH", Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
        getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(devolucaoChequeVO.getCheque().getValor(), devolucaoChequeVO.getCheque().getLocalizacaoCheque().getCodigo(), TipoMovimentacaoFinanceira.SAIDA.getValor(), TipoOrigemMovimentacaoCaixa.RENEGOCIACAO_RECEBER.getValor(), formaPagamentoVO.getCodigo(), negociacaoContaReceberVO.getCodigo(), negociacaoContaReceberVO.getResponsavel().getCodigo(), negociacaoContaReceberVO.getPessoa().getCodigo(), negociacaoContaReceberVO.getFornecedor().getCodigo(), 0, negociacaoContaReceberVO.getParceiro().getCodigo(), devolucaoChequeVO.getCheque(), 0, usuario);
        efetuarBaixaDevolucaoDoCheque(negociacaoContaReceberVO.getResponsavel(), devolucaoChequeVO.getCheque());
        excluirSemComit(consultarPorOrigem(devolucaoChequeVO.getCodigo(), OrigemMapaLancamentoFuturo.DEVOLUCAO_CHEQUE.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().alterarDataFimApresentacaoExtratoPorCodigoChequeSituacao(devolucaoChequeVO.getCheque().getCodigo(), new Date(), TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.name(), usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void efetuarBaixaRecebimentoDoCheque(UsuarioVO usuario, ChequeVO chequeVO) throws Exception {
        //chequeVO.setDataBaixa(new Date());
        chequeVO.setResponsavelBaixa(usuario);
        chequeVO.setSituacao(SituacaoCheque.BANCO.getValor());
        chequeVO.setPago(true);
        getFacadeFactory().getChequeFacade().alterarChequeBaixa(chequeVO);
        Double valorCorrigido = (chequeVO.getValor() - chequeVO.getValorDescontoAntecipacao());
        getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(chequeVO.getLocalizacaoCheque().getCodigo(), "EN", valorCorrigido, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarCheque(MapaLancamentoFuturoVO mapaLancamentoFuturoVO, ChequeVO chequeVO, MovimentacaoFinanceiraVO movimentacaoFinanceiraVO) throws Exception {
        chequeVO.setTarifaAntecipacao(mapaLancamentoFuturoVO.getTaxaDescontoCheque()); // se tiver antecipacao
        chequeVO.setValorDescontoAntecipacao(mapaLancamentoFuturoVO.getValorTaxaDescontoCheque()); // se tiver antecipacao
        chequeVO.setDataAntecipacao(mapaLancamentoFuturoVO.getDataAntecipacao());
        chequeVO.setDataBaixa(mapaLancamentoFuturoVO.getDataAutorizacao());
        chequeVO.setResponsavelBaixa(mapaLancamentoFuturoVO.getResponsavel());
        chequeVO.setContaCorrente(movimentacaoFinanceiraVO.getContaCorrenteDestino()); // => Alterar para conta corrente destino
        if (movimentacaoFinanceiraVO.getContaCorrenteDestino().getContaCaixa().booleanValue()) {
            chequeVO.setSituacao("EC");
        } else {
            chequeVO.setSituacao("BA");
        }
        getFacadeFactory().getChequeFacade().alterarChequeBaixa(chequeVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarSaldo(MapaLancamentoFuturoVO obj, ChequeVO c, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(c.getContaCorrente().getCodigo(), "EN", obj.getValor(), usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void registrarPagamentoTaxa(MapaLancamentoFuturoVO obj, ChequeVO c, MovimentacaoFinanceiraVO mF, UsuarioVO usuario, ConfiguracaoFinanceiroVO confFinan) throws Exception {
        FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
        // ConfiguracaoFinanceiroVO confFinan = (ConfiguracaoFinanceiroVO)
        // getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, usuario, null);
        if (confFinan == null) {
            throw new Exception("Não Existe uma Configuracão Financeira Salva!");
        }

        ContaPagarVO conta = new ContaPagarVO();
        conta.setBanco(mF.getContaCorrenteOrigem().getAgencia().getBanco());
        if (confFinan.getCategoriaDespesaPadraoAntecipacaoCheque().getCodigo() == 0) {
            throw new Exception("Não Existe uma Categoria de Despesa Padrão para Antecipação Cheque Definida na Configuração do Financeiro!");
        }
        conta.setCodOrigem(c.getCodigo().toString());
        conta.setData(new Date());
        conta.setDataVencimento(new Date());
        conta.setSituacao("AP");
        conta.setTipoOrigem("");
        if (mF.getContaCorrenteDestino().getContaCaixa().booleanValue()) {
            conta.setTipoSacado("EC");
        } else {
            conta.setTipoSacado("BA");
        }
        // conta.setUnidadeEnsino(mF.getContaCorrenteDestino().getUnidadeEnsino());
        conta.setValor(obj.getValorTaxaDescontoCheque());
        conta.setValorPagamento(obj.getValorTaxaDescontoCheque());
        conta.setValorPago(obj.getValorTaxaDescontoCheque());
        CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
		cro.setCategoriaDespesaVO(confFinan.getCategoriaDespesaPadraoAntecipacaoCheque());
		cro.setUnidadeEnsinoVO(conta.getUnidadeEnsino());
		cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		cro.setQuantidade(1.00);
		cro.setPorcentagem(100.00);
		cro.setValor(obj.getValor());
		conta.getListaCentroResultadoOrigemVOs().add(cro);
        getFacadeFactory().getContaPagarFacade().incluir(conta, false, true, usuario);

        NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();

        ContaPagarNegociacaoPagamentoVO neg = new ContaPagarNegociacaoPagamentoVO();
        neg.setContaPagar(conta);
        neg.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
        neg.setValorContaPagar(conta.getValor());

        if (confFinan.getFormaPagamentoPadraoCheque().getCodigo() == 0) {
            throw new Exception("Não Existe uma Forma de Pagamento Padrão para o Cheque Definida na Configuração do Financeiro!");
        }
        formaPagamentoNegociacaoPagamentoVO.setFormaPagamento(confFinan.getFormaPagamentoPadraoCheque());
        formaPagamentoNegociacaoPagamentoVO.setContaCorrente(mF.getContaCorrenteDestino());
        formaPagamentoNegociacaoPagamentoVO.setValor(conta.getValor());
        formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());

        negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(neg);
        negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().add(formaPagamentoNegociacaoPagamentoVO);

        negociacaoPagamentoVO.setData(new Date());
        // negociacaoPagamentoVO.setUnidadeEnsino(mF.getContaCorrenteDestino().getUnidadeEnsino());
        if (confFinan.getDepartamentoPadraoAntecipacaoCheque().getCodigo() == 0) {
            throw new Exception("Não Existe uma Departamento Padrão para Antecipação de Cheque Definida na Configuração do Financeiro!");
        }
        negociacaoPagamentoVO.setDepartamento(confFinan.getDepartamentoPadraoAntecipacaoCheque());
        if (mF.getContaCorrenteDestino().getContaCaixa().booleanValue()) {
            negociacaoPagamentoVO.setTipoSacado("EC");
        } else {
            negociacaoPagamentoVO.setTipoSacado("BA");
        }
        negociacaoPagamentoVO.setValorTotal(conta.getValor());
        negociacaoPagamentoVO.setValorTotalPagamento(conta.getValor());
        negociacaoPagamentoVO.setResponsavel(obj.getResponsavel());
        getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, usuario);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MapaLancamentoFuturoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do
     * objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     * 
     * @param mapaLancamentoFuturoVO Objeto da classe <code>MapaLancamentoFuturoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception {
        try {
            //MapaLancamentoFuturo.incluir(getIdEntidade(), true, usuario);
            MapaLancamentoFuturoVO.validarDados(obj);
            final String sql = "INSERT INTO MapaLancamentoFuturo( codigoOrigem, tipoOrigem, numeroCheque, sacado, dataPrevisao, valor, codigoCheque, banco, dataEmissao, tipoMapaLancamentoFuturo, matriculaOrigem, dataBaixa) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getCodigoOrigem().intValue());
                    sqlInserir.setString(2, obj.getTipoOrigem());
                    sqlInserir.setString(3, obj.getNumeroCheque());
                    sqlInserir.setString(4, obj.getSacado());
                    sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataPrevisao()));
                    sqlInserir.setDouble(6, obj.getValor().doubleValue());
                    sqlInserir.setInt(7, obj.getCodigoCheque().intValue());
                    sqlInserir.setString(8, obj.getBanco());
                    sqlInserir.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataEmissao()));
                    sqlInserir.setString(10, obj.getTipoMapaLancamentoFuturo());
                    sqlInserir.setString(11, obj.getMatriculaOrigem());
                    sqlInserir.setDate(12, Uteis.getDataJDBC(obj.getDataBaixa()));
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
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
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MapaLancamentoFuturoVO</code>. Sempre utiliza a chave primária da classe como atributo para
     * localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param mapaLancamentoFuturoVO Objeto da classe <code>MapaLancamentoFuturoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception {
        try {
            MapaLancamentoFuturoVO.validarDados(obj);
            //MapaLancamentoFuturo.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE MapaLancamentoFuturo set codigoOrigem=?, tipoOrigem=?,  numeroCheque=?, sacado=?, dataPrevisao=?, valor=?, codigoCheque=?, banco=?, dataEmissao=?, tipoMapaLancamentoFuturo=?, matriculaOrigem=?, dataBaixa=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getCodigoOrigem().intValue());
                    sqlAlterar.setString(2, obj.getTipoOrigem());
                    sqlAlterar.setString(3, obj.getNumeroCheque());
                    sqlAlterar.setString(4, obj.getSacado());
                    sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataPrevisao()));
                    sqlAlterar.setDouble(6, obj.getValor().doubleValue());
                    sqlAlterar.setInt(7, obj.getCodigoCheque().intValue());
                    sqlAlterar.setString(8, obj.getBanco());
                    sqlAlterar.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataEmissao()));
                    sqlAlterar.setString(10, obj.getTipoMapaLancamentoFuturo());
                    sqlAlterar.setString(11, obj.getMatriculaOrigem());
                    sqlAlterar.setDate(12, Uteis.getDataJDBC(obj.getDataBaixa()));
                    sqlAlterar.setInt(13, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MapaLancamentoFuturoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da
     * entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     * 
     * @param mapaLancamentoFuturoVO Objeto da classe <code>MapaLancamentoFuturoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception {
        try {
            //MapaLancamentoFuturo.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM MapaLancamentoFuturo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorCodigoCheque(Integer cheque, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {
            //MapaLancamentoFuturo.excluir(getIdEntidade(), verificarAcesso, usuario);
            String sql = "DELETE FROM MapaLancamentoFuturo WHERE ( codigocheque = ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] { cheque });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorCodigoOrigem(Integer codigoOrigem, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
    	try {
    		//MapaLancamentoFuturo.excluir(getIdEntidade(), verificarAcesso, usuario);
    		String sql = "DELETE FROM MapaLancamentoFuturo WHERE ( codigoOrigem = ? ) and tipoMapaLancamentofuturo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    		getConexao().getJdbcTemplate().update(sql, new Object[] { codigoOrigem, "CD" });
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirSemComit(MapaLancamentoFuturoVO obj) throws Exception {
        String sql = "DELETE FROM MapaLancamentoFuturo WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
    }

    public List listarTodasPendencias(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "select * from mapalancamentofuturo";
        sql += " where ((tipomapaLancamentofuturo in ('CR', 'CD', 'CP') and mapalancamentofuturo.codigoCheque in (select codigo from cheque where codigo = mapalancamentofuturo.codigoCheque))";
        sql += " or (tipomapaLancamentofuturo in ('MA') and matriculaOrigem = (select matricula from matricula where matricula = matriculaOrigem)";
        sql += " or tipomapaLancamentofuturo in ('PC'))";
        sql += " )";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return montarDadosConsulta(resultado, nivelMontarDados);
    }
    
    @Override
    public List<EstatisticasLancamentosFuturosVO> consultarEstatisticasLancamentosFuturosVO(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("select * from (");        
        sql.append("select tipomapaLancamentofuturo, sum(case when mapalancamentofuturo.dataprevisao::date < current_date then 1 else 0 end) as qtdeAnterior, sum(case when mapalancamentofuturo.dataprevisao::date = current_date then 1 else 0 end) as qtdeHoje, sum(case when mapalancamentofuturo.dataprevisao::date > current_date then 1 else 0 end) as qtdeFuturo from mapalancamentofuturo ");
        sql.append(" where ((tipomapaLancamentofuturo in ('CR', 'CD', 'CP') and mapalancamentofuturo.codigoCheque in (select codigo from cheque where codigo = mapalancamentofuturo.codigoCheque ");
        if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
        	sql.append(" and cheque.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sql.append(" ))");
        sql.append(" or (tipomapaLancamentofuturo in ('MA') and matriculaOrigem = (select matricula from matricula where matricula = matriculaOrigem ");
        if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
        	sql.append(" and matricula.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sql.append(" )) ");
        sql.append(" or (tipomapaLancamentofuturo in ('PC') and exists (select provisaocusto.codigo from provisaocusto  ");
       	if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
       		sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = provisaocusto.requisitante ");
       		sql.append(" and funcionariocargo.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sql.append(" where provisaocusto.mapalancamentofuturo = mapalancamentofuturo.codigo limit 1 )) ");
        sql.append(" ) ");
        sql.append(" group by tipomapaLancamentofuturo ");
        sql.append(" union ");
        sql.append("select 'CC' as tipomapaLancamentofuturo, sum(case when formapagamentonegociacaorecebimentocartaocredito.datavencimento::date < current_date then 1 else 0 end) as qtdeAnterior, sum(case when formapagamentonegociacaorecebimentocartaocredito.datavencimento::date = current_date then 1 else 0 end) as qtdeHoje, sum(case when formapagamentonegociacaorecebimentocartaocredito.datavencimento::date > current_date then 1 else 0 end) as qtdeFuturo from formapagamentonegociacaorecebimentocartaocredito ");
        if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
        	sql.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito =  formapagamentonegociacaorecebimentocartaocredito.codigo ");
        	sql.append(" inner join negociacaorecebimento on formapagamentonegociacaorecebimento.negociacaorecebimento =  negociacaorecebimento.codigo ");
        	sql.append(" and negociacaorecebimento.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sql.append(" where formapagamentonegociacaorecebimentocartaocredito.situacao = 'AR' "); 
        sql.append(" union ");
        sql.append("select 'MF' as tipomapaLancamentofuturo, sum(case when movimentacaofinanceira.data::date < current_date then 1 else 0 end) as qtdeAnterior, sum(case when movimentacaofinanceira.data::date = current_date then 1 else 0 end) as qtdeHoje, 0 as qtdeFuturo from movimentacaofinanceira ");
        sql.append(" where movimentacaofinanceira.situacao = 'PE' "); 
        if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
        	sql.append(" and movimentacaofinanceira.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sql.append(" union ");
        sql.append("select 'DP' as tipomapaLancamentofuturo, sum(case when contapagar.datavencimento::date < current_date then 1 else 0 end) as qtdeAnterior, sum(case when contapagar.datavencimento::date = current_date then 1 else 0 end) as qtdeHoje, sum(case when contapagar.datavencimento::date > current_date then 1 else 0 end) as qtdeFuturo from contapagar ");
        sql.append(" where contapagar.situacao = 'AP' "); 
        if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
        	sql.append(" and contapagar.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sql.append(" ) as t ");
        sql.append(" where ((qtdeAnterior is not null and qtdeAnterior > 0) or (qtdeHoje is not null and qtdeHoje > 0) or (qtdeFuturo is not null and qtdeFuturo >0)) ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<EstatisticasLancamentosFuturosVO> estatisticasLancamentosFuturosVOs = new ArrayList<EstatisticasLancamentosFuturosVO>(0);
        boolean permiteAcessarMapaLancamentoFuturo = false;
        try {
        	consultar("MapaLancamentoFuturo", true, usuario);
        	permiteAcessarMapaLancamentoFuturo = true;
        }catch (Exception e) {

		}
        boolean permiteAcessarMapaPendenciaCartao = false;
        try {
        	consultar("MapaPendenciaCartaoCredito", true, usuario);
        	permiteAcessarMapaPendenciaCartao = true;
        }catch (Exception e) {

		}
        boolean permiteAcessarMapaMovimentacaoFinanceira = false;
        try {
        	consultar("MapaPendenciaMovimentacaoFinanceira", true, usuario);
        	permiteAcessarMapaMovimentacaoFinanceira = true;
        }catch (Exception e) {

		}
        boolean permiteAcessarContaPagar = false;
        try {
        	consultar("ContaPagar", true, usuario);
        	permiteAcessarContaPagar = true;
        }catch (Exception e) {
        	
        }
        
        while(resultado.next()) {
        	EstatisticasLancamentosFuturosVO estatisticasLancamentosFuturosVO =  new EstatisticasLancamentosFuturosVO();
        	estatisticasLancamentosFuturosVO.setQtdAnterior(resultado.getInt("qtdeAnterior"));
        	estatisticasLancamentosFuturosVO.setQtdHoje(resultado.getInt("qtdeHoje"));
        	estatisticasLancamentosFuturosVO.setQtdFuturos(resultado.getInt("qtdeFuturo"));
        	estatisticasLancamentosFuturosVO.setTipoMapaLancamento(TipoMapaLancamentoFuturo.getEnum(resultado.getString("tipomapaLancamentofuturo")));
        	if(estatisticasLancamentosFuturosVO.getTipoMapaLancamento().equals(TipoMapaLancamentoFuturo.CARTOES_A_RECEBER)) {
        		estatisticasLancamentosFuturosVO.setPopup("abrirPopup('../financeiro/mapaPendenciaCartaoCreditoCons.xhtml', 'mapaPendenciaCartaoCredito', 930, 595);");
        		estatisticasLancamentosFuturosVO.setDesabilitar(!permiteAcessarMapaPendenciaCartao);
        	}else if(estatisticasLancamentosFuturosVO.getTipoMapaLancamento().equals(TipoMapaLancamentoFuturo.MOVIMENTACAO_FINANCEIRA)) {
        		estatisticasLancamentosFuturosVO.setPopup("abrirPopup('../financeiro/mapaPendenciaMovimentacaoFinanceiraCons.xhtml', 'mapaPendenciaMovimentacaoFinanceira', 930, 595);");
        		estatisticasLancamentosFuturosVO.setDesabilitar(!permiteAcessarMapaMovimentacaoFinanceira);
        	}else if(estatisticasLancamentosFuturosVO.getTipoMapaLancamento().equals(TipoMapaLancamentoFuturo.CONTA_PAGAR)) {
        		estatisticasLancamentosFuturosVO.setPopup("abrirPopup('../financeiro/contaPagarCons.xhtml', 'contaPagar', 930, 595);");
        		estatisticasLancamentosFuturosVO.setDesabilitar(!permiteAcessarContaPagar);
        	}else {
        		estatisticasLancamentosFuturosVO.setPopup("abrirPopup('../financeiro/mapaLancamentoFuturoCons.xhtml', 'mapaLancamentoFuturo', 930, 595);");
        		estatisticasLancamentosFuturosVO.setDesabilitar(!permiteAcessarMapaLancamentoFuturo);
        	}
        	estatisticasLancamentosFuturosVOs.add(estatisticasLancamentosFuturosVO);
        }
        return estatisticasLancamentosFuturosVOs;
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceira</code> através do valor do atributo <code>nome</code> da classe <code>Usuario</code> Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>MapaLancamentoFuturoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MapaLancamentoFuturo.* FROM MapaLancamentoFuturo, Usuario WHERE MapaLancamentoFuturo.responsavel  = Usuario.codigo and (lower) Usuario.nome like ('" + valorConsulta.toLowerCase() + "%') ORDER BY Usuario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    public List<MapaLancamentoFuturoVO> consultarPorCodigoChequeETipoMapa(int codigoCheque, String tipoMapa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from mapalancamentofuturo where codigocheque = ? and tipomapalancamentofuturo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoCheque, tipoMapa });
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    public MapaLancamentoFuturoVO consultarPorOrigem(int codigoOrigem, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from mapalancamentofuturo where codigoorigem = ? and tipoorigem = ? limit 1";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoOrigem, tipoOrigem });
        if (resultado.next()) {
            return montarDados(resultado, nivelMontarDados);
        }
        return new MapaLancamentoFuturoVO();
    }

    public MapaLancamentoFuturoVO consultarPorMatriculaOrigem(String matriculaOrigem, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from mapalancamentofuturo where matriculaOrigem = ? and tipoorigem = ? limit 1";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { matriculaOrigem, tipoOrigem });
        if (resultado.next()) {
            return montarDados(resultado, nivelMontarDados);
        }
        return new MapaLancamentoFuturoVO();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List consultarPorTodosParametros(MapaLancamentoFuturoVO obj, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select MapaLancamentoFuturo.* from MapaLancamentoFuturo inner join cheque on cheque.codigo = mapalancamentofuturo.codigocheque ";
        sqlStr += " where 1=1 ";
        if (obj.getDataPrevisao() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataprevisao >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataPrevisao(), 0, 0, 0)) + "' ";
        }
        if (obj.getDataPrevisaoFinal() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataPrevisao <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataPrevisaoFinal(), 23, 59, 59)) + "' ";
        }
        if (obj.getDataEmissao() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataemissao >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataEmissao(), 0, 0, 0)) + "' ";
        }
        if (obj.getDataEmissaoFinal() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataEmissao <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataEmissaoFinal(), 23, 59, 59)) + "' ";
        }
        if (Uteis.isAtributoPreenchido(obj.getTipoMapaLancamentoFuturo())) {
            sqlStr += " and MapaLancamentoFuturo.tipomapalancamentofuturo = '" + obj.getTipoMapaLancamentoFuturo() + "' ";
            if (obj.getTipoMapaLancamentoFuturo().equals("CD")) {
            	sqlStr += " and (cheque.pago = false) ";
            }
        }
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sqlStr += " and cheque.unidadeensino = " + unidadeEnsino + " ";
        }
        sqlStr += " and upper( MapaLancamentoFuturo.numeroCheque ) like('" + obj.getNumeroCheque() + "%')";
        sqlStr += " and upper( sem_acentos(MapaLancamentoFuturo.sacado )) like(upper( sem_acentos('" + obj.getSacado() + "%')))";
        sqlStr += " and upper( sem_acentos(MapaLancamentoFuturo.banco )) like(upper( sem_acentos('" + obj.getBanco() + "%'))) order by MapaLancamentoFuturo.dataprevisao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void reapresentarCheque(final MapaLancamentoFuturoVO obj, final Date dataReapresentacao, UsuarioVO usuarioVO) throws Exception {
    	try {
    		if (obj.getDataReapresentacaoCheque1() != null && obj.getDataReapresentacaoCheque2() != null) {
    			throw new Exception("Não é possível reapresentar o cheque, pois o mesmo já foi reapresentado duas vezes.");
    		}
    		if (obj.getTipoOrigem().equals("DC")) {
	    		DevolucaoChequeVO devCheque = getFacadeFactory().getDevolucaoChequeFacade().consultarPorCodigoCheque(obj.getCodigoCheque(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	    		ChequeVO cheque = new ChequeVO();
	    		cheque = getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(obj.getCodigoCheque(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
	    		cheque.setCodigo(obj.getCodigoCheque());
	    		cheque.setSituacao("PE");
	    		cheque.setLocalizacaoCheque(devCheque.getContaCorrente());
	    		getFacadeFactory().getChequeFacade().alterarChequeReapresentacao(cheque);
	    		getFacadeFactory().getFluxoCaixaFacade().alterarSaldoFluxoCaixaPorMovimentacaoCaixaEstornoMovimentacaoFinanceira(devCheque, obj.getValor(), usuarioVO);
	    		getFacadeFactory().getContaReceberFacade().excluirPorChequeDevolvido(devCheque.getCodigo(), usuarioVO);
	    		realizarCriacaoExtratoMapaLancamentoFuturo(cheque, dataReapresentacao, devCheque.getContaCorrente().getCodigo(), usuarioVO);
    		}
    		final String sql;
            if (obj.getDataReapresentacaoCheque1() == null) {
            	obj.setDataReapresentacaoCheque1(dataReapresentacao);
            	sql = "UPDATE MapaLancamentoFuturo set tipomapalancamentofuturo=?, dataReapresentacaoCheque1=? WHERE ((codigocheque = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            } else {
            	obj.setDataReapresentacaoCheque2(dataReapresentacao);
            	sql = "UPDATE MapaLancamentoFuturo set tipomapalancamentofuturo=?, dataReapresentacaoCheque2=? WHERE ((codigocheque = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            }
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    sqlAlterar.setString(1, "CR");
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(dataReapresentacao));
                    sqlAlterar.setInt(3, obj.getCodigoCheque());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void realizarCriacaoExtratoMapaLancamentoFuturo(ChequeVO chequeVO, Date dataReapresentacao, Integer contaCorrente, UsuarioVO usuarioVO) throws Exception {
    	getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().alterarDataFimApresentacaoExtratoPorCodigoChequeSituacao(chequeVO.getCodigo(), dataReapresentacao, TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.name() , usuarioVO);
    	getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().realizarCriacaoInclusaoExtratoMapaLancamentoFuturoDataReapresentacao(chequeVO, dataReapresentacao, contaCorrente, TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER, usuarioVO);
    }

    
    public void reDevolverCheque(final Integer codigoCheque, final Integer codigoOrigem) throws Exception {
    	try {
    		final String sql = "UPDATE MapaLancamentoFuturo set tipomapalancamentofuturo=?, codigoOrigem=? WHERE ((codigocheque = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    sqlAlterar.setString(1, "CD");
                    sqlAlterar.setInt(2, codigoOrigem);
                    sqlAlterar.setInt(3, codigoCheque);
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    public Double consultarPorTodosParametrosValorTotal(MapaLancamentoFuturoVO obj, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "select SUM(MapaLancamentoFuturo.valor) as soma from MapaLancamentoFuturo inner join cheque on cheque.codigo = mapalancamentofuturo.codigocheque where 1=1";
        if (obj.getDataPrevisao() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataprevisao >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataPrevisao(), 0, 0, 0)) + "' ";
        }
        if (obj.getDataPrevisaoFinal() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataPrevisao <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataPrevisaoFinal(), 23, 59, 59)) + "' ";
        }
        if (obj.getDataEmissao() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataemissao >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataEmissao(), 0, 0, 0)) + "' ";
        }
        if (obj.getDataEmissaoFinal() != null) {
            sqlStr += " and MapaLancamentoFuturo.dataEmissao <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataEmissaoFinal(), 23, 59, 59)) + "' ";
        }
        if (Uteis.isAtributoPreenchido(obj.getTipoMapaLancamentoFuturo())) {
            sqlStr += " and MapaLancamentoFuturo.tipomapalancamentofuturo = '" + obj.getTipoMapaLancamentoFuturo() + "' ";
            if (obj.getTipoMapaLancamentoFuturo().equals("CD")) {
            	sqlStr += " and (cheque.pago = false) ";
            }
        }
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sqlStr += " and cheque.unidadeensino = " + unidadeEnsino + " ";
        }
        sqlStr += " and upper( MapaLancamentoFuturo.numeroCheque ) like('" + obj.getNumeroCheque() + "%')";
        sqlStr += " and upper( sem_acentos(MapaLancamentoFuturo.sacado )) like(upper(sem_acentos('" + obj.getSacado() + "%')))";
        sqlStr += " and upper( sem_acentos(MapaLancamentoFuturo.banco )) like(upper(sem_acentos('" + obj.getBanco() + "%')))";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return new Double(tabelaResultado.getDouble("soma"));
        } else {
            return 0.0;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceira</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais
     * ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>MapaLancamentoFuturoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MapaLancamentoFuturo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que
     * realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>MapaLancamentoFuturoVO</code> resultantes da consulta.
     */
    public static List<MapaLancamentoFuturoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MapaLancamentoFuturoVO> vetResultado = new ArrayList<MapaLancamentoFuturoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>MapaLancamentoFuturoVO</code>.
     * 
     * @return O objeto da classe <code>MapaLancamentoFuturoVO</code> com os dados devidamente montados.
     */
    public static MapaLancamentoFuturoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MapaLancamentoFuturoVO obj = new MapaLancamentoFuturoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setSacado(dadosSQL.getString("sacado"));
        obj.setNumeroCheque(dadosSQL.getString("numeroCheque"));
        obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
        obj.setDataPrevisao(dadosSQL.getTimestamp("dataPrevisao"));
        obj.setDataEmissao(dadosSQL.getTimestamp("dataEmissao"));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setBanco(dadosSQL.getString("banco"));
        obj.setCodigoCheque(new Integer(dadosSQL.getInt("codigoCheque")));
        obj.setCodigoOrigem(new Integer(dadosSQL.getInt("codigoOrigem")));
        obj.setTipoMapaLancamentoFuturo(dadosSQL.getString("tipoMapaLancamentoFuturo"));
        obj.setMatriculaOrigem(dadosSQL.getString("matriculaOrigem"));
        obj.setDataReapresentacaoCheque1(dadosSQL.getDate("dataReapresentacaoCheque1"));
        obj.setDataReapresentacaoCheque2(dadosSQL.getDate("dataReapresentacaoCheque2"));
        obj.setDataBaixa(dadosSQL.getDate("dataBaixa"));
        obj.setTaxaDescontoCheque(0.0);
        obj.setValorTaxaDescontoCheque(0.0);
        obj.setValorFinalCheque(0.0);
        obj.setBaixarCheque(false);
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        // montarDadosResponsavel(obj, nivelMontarDados);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>MapaLancamentoFuturoVO</code>. Faz uso da chave primária
     * da classe <code>UsuarioVO</code> para realizar a consulta.
     * 
     * @param mapaLancamentoFuturoVO Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(MapaLancamentoFuturoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MapaLancamentoFuturoVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MapaLancamentoFuturoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM MapaLancamentoFuturo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MapaLancamentoFuturo ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorCodigoChequeTipoOrigemETipoMapalancamentoFuturo(Integer codigoCheque, String tipoOrigem, String tipoMapaLancamentoFuturo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	//ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" DELETE FROM mapalancamentofuturo WHERE codigo IN ( ");
        sqlStr.append(" 	SELECT codigo FROM mapalancamentofuturo WHERE codigocheque = ").append(codigoCheque);
        sqlStr.append(" 	AND tipomapalancamentofuturo = '").append(tipoMapaLancamentoFuturo).append("' AND tipoorigem = '").append(tipoOrigem).append("' ");
        sqlStr.append(" ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        getConexao().getJdbcTemplate().update(sqlStr.toString());
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MapaLancamentoFuturo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com
     * objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        MapaLancamentoFuturo.idEntidade = idEntidade;
    }
    
    public void alterarDataBaixa(final MapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception {
        try {
        	if (Uteis.isAtributoPreenchido(obj.getDataBaixa()) && Uteis.isAtributoPreenchido(obj.getCodigo())) {
        		final String sql = "UPDATE MapaLancamentoFuturo set dataBaixa=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
        			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        				PreparedStatement sqlAlterar = connection.prepareStatement(sql);
        				sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataBaixa()));
        				sqlAlterar.setInt(2, obj.getCodigo());
        				return sqlAlterar;
        			}
        		});
        	}
        } catch (Exception e) {
            throw e;
        }
    }
}
