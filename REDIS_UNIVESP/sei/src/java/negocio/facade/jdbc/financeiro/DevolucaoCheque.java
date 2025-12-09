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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.DevolucaoChequeInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DevolucaoChequeVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>DevolucaoChequeVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see DevolucaoChequeVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class DevolucaoCheque extends ControleAcesso implements DevolucaoChequeInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3947713324152215393L;
	protected static String idEntidade;

    public DevolucaoCheque() throws Exception {
        super();
        setIdEntidade("DevolucaoCheque");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>DevolucaoChequeVO</code>.
     */
    public DevolucaoChequeVO novo() throws Exception {
        DevolucaoCheque.incluir(getIdEntidade());
        DevolucaoChequeVO obj = new DevolucaoChequeVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DevolucaoChequeVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param devolucaoChequeVO  Objeto da classe <code>DevolucaoChequeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DevolucaoChequeVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        try {
            DevolucaoChequeVO.validarDados(obj);
            DevolucaoCheque.incluir(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO DevolucaoCheque( data, responsavel, pessoa, cheque, motivo, contacaixa, contacorrente, centroreceita, unidadeEnsino, fornecedor, parceiro, desconsiderarConciliacaoBancaria ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getCheque().getPessoa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getCheque().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getCheque().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getMotivo());
                    if (obj.getContaCaixa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getContaCaixa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getCentroReceita().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                    	sqlInserir.setInt(10, obj.getCheque().getFornecedor().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(10, 0);
                    }
                    if (obj.getParceiro().getCodigo().intValue() != 0) {
                    	sqlInserir.setInt(11, obj.getCheque().getParceiro().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(11, 0);
                    }
                    sqlInserir.setBoolean(12, obj.getDesconsiderarConciliacaoBancaria());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
            String tipoPessoa = "";
            NegociacaoRecebimentoVO neg = getFacadeFactory().getNegociacaoRecebimentoFacade().consultarMatriculaPeloChequeFormaPagamentoNegociacaoRecebimento(obj.getCheque().getCodigo());
			if (Uteis.isAtributoPreenchido(neg)) {
				obj.getContaReceberVO().getMatriculaAluno().setMatricula(neg.getMatricula());
				tipoPessoa = neg.getTipoPessoa();
			} else {
				obj.getCheque().setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCheque().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
				tipoPessoa = obj.getCheque().getPessoa().getTipoPessoa();
			}
            if (tipoPessoa.equals("FU")) {
                obj.getPessoa().setAluno(Boolean.FALSE);
                obj.getPessoa().setProfessor(Boolean.FALSE);
                obj.getPessoa().setCandidato(Boolean.FALSE);
                obj.getPessoa().setFuncionario(Boolean.TRUE);
				obj.getPessoa().setRequisitante(Boolean.FALSE);
            }
            if (tipoPessoa.equals("PR")) {
                obj.getPessoa().setAluno(Boolean.FALSE);
                obj.getPessoa().setProfessor(Boolean.TRUE);
                obj.getPessoa().setCandidato(Boolean.FALSE);
                obj.getPessoa().setFuncionario(Boolean.FALSE);
				obj.getPessoa().setRequisitante(Boolean.FALSE);
            }
            if (tipoPessoa.equals("AL")) {
            	obj.getPessoa().setPossuiAcessoVisaoPais(Boolean.FALSE);
            	obj.getPessoa().setPossuiAcessoVisaoPais(Boolean.FALSE);
                obj.getPessoa().setAluno(Boolean.TRUE);
                obj.getPessoa().setProfessor(Boolean.FALSE);
                obj.getPessoa().setCandidato(Boolean.FALSE);
                obj.getPessoa().setFuncionario(Boolean.FALSE);
				obj.getPessoa().setRequisitante(Boolean.FALSE);
            }
            if (tipoPessoa.equals("RF")) {
            	obj.getPessoa().setAluno(Boolean.FALSE);
            	obj.getPessoa().setPossuiAcessoVisaoPais(Boolean.TRUE);
            	obj.getPessoa().setProfessor(Boolean.FALSE);
            	obj.getPessoa().setCandidato(Boolean.FALSE);
            	obj.getPessoa().setFuncionario(Boolean.FALSE);
				obj.getPessoa().setRequisitante(Boolean.FALSE);
            }
            if (tipoPessoa.equals("PA")) {
            	obj.getPessoa().setAluno(Boolean.FALSE);
            	obj.getPessoa().setProfessor(Boolean.FALSE);
            	obj.getPessoa().setCandidato(Boolean.FALSE);
            	obj.getPessoa().setFuncionario(Boolean.FALSE);
				obj.getPessoa().setRequisitante(Boolean.FALSE);
            }
            if (tipoPessoa.equals("FO")) {
            	obj.getPessoa().setAluno(Boolean.FALSE);
            	obj.getPessoa().setProfessor(Boolean.FALSE);
            	obj.getPessoa().setCandidato(Boolean.FALSE);
            	obj.getPessoa().setFuncionario(Boolean.FALSE);
				obj.getPessoa().setRequisitante(Boolean.FALSE);
            }
            if (tipoPessoa.equals("RE")) {
            	obj.getPessoa().setAluno(Boolean.FALSE);
            	obj.getPessoa().setProfessor(Boolean.FALSE);
            	obj.getPessoa().setCandidato(Boolean.FALSE);
            	obj.getPessoa().setFuncionario(Boolean.FALSE);
            	obj.getPessoa().setRequisitante(Boolean.TRUE);
            }
            getFacadeFactory().getContaReceberFacade().criarContaReceber(obj, configuracaoFinanceiro, usuario);
            if (obj.getCheque().getSituacao().equals("BA")){
            	if (obj.getDesconsiderarConciliacaoBancaria()) {
            		getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CHEQUE, neg.getCodigo(), obj.getDesconsiderarConciliacaoBancaria(), obj.getCheque().getCodigo(), true, usuario);
            	} else {
            		criarMovimentacaoCaixaContaCorrente(obj, usuario);                
            	}
            }
            criarPendenciaChequeDevolvido(obj,usuario);
            excluirPendenciaChequeRecebimento(obj, usuario);
            realizarCriacaoExtratoMapaLancamentoFuturo(obj.getCheque(), obj.getData(), obj.getContaCorrente().getCodigo(), usuario);
            List<Integer> listaCodigoCheque = new ArrayList<Integer>();
            listaCodigoCheque.add(obj.getCheque().getCodigo());
            getFacadeFactory().getLancamentoContabilFacade().realizarAtualizacaoLancamentoContabilPorCheque(listaCodigoCheque, true, usuario);
            movimentacaoCaixa(obj, usuario);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    public void realizarCriacaoExtratoMapaLancamentoFuturo(ChequeVO chequeVO, Date dataDevolucao, Integer contaCorrente, UsuarioVO usuarioVO) throws Exception {
    	getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().alterarDataFimApresentacaoExtratoPorCodigoChequeSituacao(chequeVO.getCodigo(), dataDevolucao, TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.name() , usuarioVO);
    	getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().realizarCriacaoInclusaoExtratoMapaLancamentoFuturo(chequeVO, dataDevolucao, contaCorrente, TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO, usuarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPendenciaChequeRecebimento(DevolucaoChequeVO devolucaoChequeVO, UsuarioVO usuario) throws Exception {        
        List<MapaLancamentoFuturoVO> listaDeMapas = getFacadeFactory().getMapaLancamentoFuturoFacade().consultarPorCodigoChequeETipoMapa(devolucaoChequeVO.getCheque().getCodigo(), TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        Iterator<MapaLancamentoFuturoVO> i = listaDeMapas.iterator();
        MapaLancamentoFuturoVO mapaLancamentoFuturoVO;
        while (i.hasNext()) {
            mapaLancamentoFuturoVO = i.next();
            getFacadeFactory().getMapaLancamentoFuturoFacade().excluir(mapaLancamentoFuturoVO,usuario);
        }
    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarPendenciaChequeDevolvido(DevolucaoChequeVO devolucaoChequeVO, UsuarioVO usuario) throws Exception {
		if (devolucaoChequeVO.getDataReapresentacaoCheque1() == null) {
			MapaLancamentoFuturoVO mapaLancamentoFuturoVO = devolucaoChequeVO.getCheque().criarMapaLancamentoFuturo(devolucaoChequeVO.getCodigo(), OrigemMapaLancamentoFuturo.DEVOLUCAO_CHEQUE.getValor(), TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.getValor(), devolucaoChequeVO.getResponsavel());
			getFacadeFactory().getMapaLancamentoFuturoFacade().incluir(mapaLancamentoFuturoVO, usuario);
		} else {
			getFacadeFactory().getMapaLancamentoFuturoFacade().reDevolverCheque(devolucaoChequeVO.getCheque().getCodigo(), devolucaoChequeVO.getCodigo());
		}
		alterarSituacaoDoChequeDevolvido(devolucaoChequeVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoDoChequeDevolvido(DevolucaoChequeVO devolucaoChequeVO) throws Exception {
		devolucaoChequeVO.getCheque().setLocalizacaoCheque(devolucaoChequeVO.getContaCaixa());
		devolucaoChequeVO.getCheque().setSituacao(SituacaoCheque.EM_CAIXA.getValor());
		getFacadeFactory().getChequeFacade().alterarChequeBaixa(devolucaoChequeVO.getCheque());
	}

	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void movimentacaoCaixa(DevolucaoChequeVO obj, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getCheque().getValor(), obj.getContaCaixa().getCodigo(), "EN", "DC", consultarFormaPagamento(usuario).getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getPessoa().getCodigo(), obj.getFornecedor().getCodigo(), 0, obj.getParceiro().getCodigo(), obj.getCheque(), 0, usuario);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixaContaCorrente(DevolucaoChequeVO devolucaoChequeVO, UsuarioVO usuario) throws Exception {       
            StringBuilder sql = new StringBuilder("INSERT INTO ExtratoContaCorrente( valor, data, origemExtratoContaCorrente, tipoMovimentacaoFinanceira, codigoOrigem, ");
            sql.append(" codigoCheque, sacadoCheque, numeroCheque, bancoCheque, contaCorrenteCheque, ");
            sql.append(" agenciaCheque, dataPrevisaoCheque, nomeSacado, codigoSacado, tipoSacado,  ");
            sql.append(" contaCorrente, unidadeEnsino, responsavel, formaPagamento) ");
            sql.append(" select cheque.valor, '" + Uteis.getDataJDBCTimestamp(devolucaoChequeVO.getData()) +"', 'DEVOLUCAO_CHEQUE' as origemExtratoContaCorrente, 'SAIDA' as tipoMovimentacaoFinanceira,  "+devolucaoChequeVO.getCodigo()+", ");
            sql.append(" cheque.codigo, cheque.sacado, cheque.numero, cheque.banco, cheque.numeroContaCorrente , cheque.agencia, cheque.dataPrevisao,  ");
            sql.append(" case when pessoa.codigo > 0 then pessoa.nome else case when  parceiro.codigo > 0 then parceiro.nome else fornecedor.nome end end as nomeSacado, ");
            sql.append(" case when pessoa.codigo > 0 then pessoa.codigo else case when  parceiro.codigo > 0 then  parceiro.codigo else fornecedor.codigo end end as codigoSacado, ");
            sql.append(" case negociacaorecebimento.tipoPessoa  ");
            sql.append(" when 'FO' then 'FORNECEDOR' ");
            sql.append(" when 'FU' then 'FUNCIONARIO_PROFESSOR' ");
            sql.append(" when 'AL' then 'ALUNO'  ");
            sql.append(" when 'RF' then 'RESPONSAVEL_FINANCEIRO' ");
            sql.append(" when 'BA' then 'BANCO'  ");
            sql.append(" when 'CA' then 'CANDIDATO' ");
            sql.append(" when 'RE' then 'REQUERENTE'  ");
            sql.append(" when 'PA' then 'PARCEIRO' ");
            sql.append(" else 'ALUNO' end as tipoSacado, cheque.localizacaocheque, cheque.unidadeEnsino, "+usuario.getCodigo()+", ");
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
            sql.append(" and cheque.codigo = ").append(devolucaoChequeVO.getCheque().getCodigo());            
            getConexao().getJdbcTemplate().execute(sql.toString()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        
    }

//    public EmpresaPadraoVO consultarEmpresaPadrao(String identificador) throws Exception {
//        EmpresaPadraoVO empresa = new EmpresaPadrao().consultarPorEmpresaMatrizIdentificador(identificador, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//        return empresa;
//    }
    public FormaPagamentoVO consultarFormaPagamento(UsuarioVO usuario) throws Exception {
        List<FormaPagamentoVO> listaContaReceber = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        Iterator<FormaPagamentoVO> i = listaContaReceber.iterator();
        while (i.hasNext()) {
            FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
            if (obj.getTipo().equals("CH")) {
                return obj;
            }
        }
        return new FormaPagamentoVO();
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DevolucaoChequeVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param devolucaoChequeVO    Objeto da classe <code>DevolucaoChequeVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DevolucaoChequeVO obj,UsuarioVO usuario) throws Exception {
        try {
            DevolucaoChequeVO.validarDados(obj);
            DevolucaoCheque.alterar(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE DevolucaoCheque set data=?, responsavel=?, pessoa=?, cheque=?, motivo=?, contacaixa=?, contacorrente=?, centroreceita=?, unidadeEnsino=?, fornecedor = ?, parceiro = ?  WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    if (obj.getCheque().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getCheque().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setString(5, obj.getMotivo());
                    if (obj.getContaCaixa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getContaCaixa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getCentroReceita().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                    	sqlAlterar.setInt(10, obj.getFornecedor().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(10, 0);
                    }
                    if (obj.getParceiro().getCodigo().intValue() != 0) {
                    	sqlAlterar.setInt(11, obj.getParceiro().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(11, 0);
                    }
                    sqlAlterar.setInt(12, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        } 
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DevolucaoChequeVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param devolucaoChequeVO    Objeto da classe <code>DevolucaoChequeVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DevolucaoChequeVO obj,UsuarioVO usuario) throws Exception {
        try {
            DevolucaoCheque.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM DevolucaoCheque WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCheque</code> através do valor do atributo
     * <code>numero</code> da classe <code>ChqRLog</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DevolucaoChequeVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoChequeVO> consultarPorNumeroCheque(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DevolucaoCheque.* FROM DevolucaoCheque, Cheque WHERE DevolucaoCheque.cheque = Cheque.codigo and upper( Cheque.numero ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Cheque.numero";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCheque</code> através do valor do atributo
     * <code>nome</code> da classe <code>Cliente</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DevolucaoChequeVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoChequeVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT DevolucaoCheque.* FROM DevolucaoCheque ");
        sqlStr.append(" left join Pessoa on DevolucaoCheque.pessoa = pessoa.codigo ");
        sqlStr.append(" left join Parceiro on DevolucaoCheque.Parceiro = Parceiro.codigo ");
        sqlStr.append(" left join Fornecedor on DevolucaoCheque.Fornecedor = Fornecedor.codigo ");
        sqlStr.append(" WHERE (lower(sem_acentos(pessoa.nome)) like (sem_acentos('" + valorConsulta.toLowerCase() + "%')) ");
        sqlStr.append(" or lower(sem_acentos(parceiro.nome)) like (sem_acentos('" + valorConsulta.toLowerCase() + "%')) ");
        sqlStr.append(" or lower(sem_acentos(fornecedor.nome)) like (sem_acentos('" + valorConsulta.toLowerCase() + "%'))) ");
        sqlStr.append(" ORDER BY pessoa.nome||parceiro.nome||fornecedor.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCheque</code> através do valor do atributo
     * <code>nome</code> da classe <code>Usuario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DevolucaoChequeVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoChequeVO> consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DevolucaoCheque.* FROM DevolucaoCheque, Usuario WHERE DevolucaoCheque.responsavel = Usuario.codigo and lower(Usuario.nome) like ('" + valorConsulta.toLowerCase() + "%') ORDER BY usuario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCheque</code> através do valor do atributo
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DevolucaoChequeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoChequeVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DevolucaoCheque WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>DevolucaoCheque</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>DevolucaoChequeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DevolucaoChequeVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DevolucaoCheque WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>DevolucaoChequeVO</code> resultantes da consulta.
     */
    public static List<DevolucaoChequeVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DevolucaoChequeVO> vetResultado = new ArrayList<DevolucaoChequeVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>DevolucaoChequeVO</code>.
     * @return  O objeto da classe <code>DevolucaoChequeVO</code> com os dados devidamente montados.
     */
    public static DevolucaoChequeVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DevolucaoChequeVO obj = new DevolucaoChequeVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
        obj.getCheque().setCodigo(new Integer(dadosSQL.getInt("cheque")));
        montarDadosCheque(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        obj.setPessoa(obj.getCheque().getPessoa());
        obj.setFornecedor(obj.getCheque().getFornecedor());
        obj.setParceiro(obj.getCheque().getParceiro());
        obj.setDesconsiderarConciliacaoBancaria(dadosSQL.getBoolean("desconsiderarConciliacaoBancaria"));
//        montarDadosPessoa(obj, nivelMontarDados, usuario);
//        montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
//        montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.getContaCaixa().setCodigo(new Integer(dadosSQL.getInt("contacaixa")));
        obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contacorrente")));
        obj.getCentroReceita().setCodigo(new Integer(dadosSQL.getInt("centroreceita")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosContaCaixa(obj, nivelMontarDados, usuario);
        montarDadosContaCorrente(obj, nivelMontarDados, usuario);
        montarDadosCentroReceita(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ChqRLogVO</code> relacionado ao objeto <code>DevolucaoChequeVO</code>.
     * Faz uso da chave primária da classe <code>ChqRLogVO</code> para realizar a consulta.
     * @param devolucaoChequeVO  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCheque(DevolucaoChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCheque().getCodigo().intValue() == 0) {
            obj.setCheque(new ChequeVO());
            return;
        }
        obj.setCheque(getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(obj.getCheque().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosCentroReceita(DevolucaoChequeVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceita().getCodigo().intValue() == 0) {
            obj.setCentroReceita(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceita(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceita().getCodigo(), false, nivelMontarDados,usuario));
    }

    public static void montarDadosContaCaixa(DevolucaoChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCaixa().getCodigo().intValue() == 0) {
            obj.setContaCaixa(new ContaCorrenteVO());
            return;
        }
        obj.setContaCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCaixa().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosContaCorrente(DevolucaoChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrente().getCodigo().intValue() == 0) {
            obj.setContaCorrente(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ClienteVO</code> relacionado ao objeto <code>DevolucaoChequeVO</code>.
     * Faz uso da chave primária da classe <code>ClienteVO</code> para realizar a consulta.
     * @param devolucaoChequeVO  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPessoa(DevolucaoChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCheque().getPessoa().getCodigo(), false, nivelMontarDados, usuario));
    }
    public static void montarDadosFornecedor(DevolucaoChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getFornecedor().getCodigo().intValue() == 0) {
    		obj.setFornecedor(new FornecedorVO());
    		return;
    	}
    	obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getCheque().getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
    }
    public static void montarDadosParceiro(DevolucaoChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getParceiro().getCodigo().intValue() == 0) {
    		obj.setParceiro(new ParceiroVO());
    		return;
    	}
    	obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getCheque().getParceiro().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>DevolucaoChequeVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param devolucaoChequeVO  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(DevolucaoChequeVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DevolucaoChequeVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DevolucaoChequeVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DevolucaoCheque WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( DevolucaoCheque ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public DevolucaoChequeVO consultarPorCodigoCheque(Integer codigoCheque, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DevolucaoCheque WHERE cheque = " + codigoCheque + " order by codigo desc limit 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
//            throw new ConsistirException("Dados Não Encontrados ( DevolucaoCheque ).");
        	return new DevolucaoChequeVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DevolucaoCheque.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        DevolucaoCheque.idEntidade = idEntidade;
    }
}
