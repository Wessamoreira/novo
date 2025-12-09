package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoFuncionarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.enumerador.LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CondicaoRenegociacaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CondicaoRenegociacaoVO</code>. Responsável por implementar operações como
 * incluir, alterar, excluir e consultar pertinentes a classe <code>CondicaoRenegociacaoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CondicaoRenegociacaoVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class CondicaoRenegociacao extends ControleAcesso implements CondicaoRenegociacaoInterfaceFacade {

    protected static String idEntidade;

    public CondicaoRenegociacao() throws Exception {
        super();
        setIdEntidade("CondicaoRenegociacao");

    }

    public CondicaoRenegociacaoVO inicializarDadosCondicaoRenegociacaoNovo(UsuarioVO usuario) throws Exception {
        CondicaoRenegociacaoVO obj = new CondicaoRenegociacaoVO();
        obj.setUsuarioCriacao(usuario);
        obj.setDataCriacao(new Date());
        obj.setUsuarioUltimaAlteracao(usuario);
        obj.setDataUltimaAlteracao(new Date());
        obj.setStatus("AT");
        obj.setNovoObj(Boolean.TRUE);
        return obj;
    }

    public void inicializarDadosCondicaoRenegociacaoEditar(CondicaoRenegociacaoVO obj, UsuarioVO usuario) throws Exception {
        obj.setUsuarioUltimaAlteracao(usuario);
        obj.setDataUltimaAlteracao(new Date());

    }
    
    @Override
    public void realizarClonagem(CondicaoRenegociacaoVO condicaoRenegociacaoVO){
        condicaoRenegociacaoVO.setCodigo(0);
        condicaoRenegociacaoVO.setNovoObj(true);
        condicaoRenegociacaoVO.setStatus("AT");
        for(ItemCondicaoRenegociacaoVO item:condicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()){
            item.setCodigo(0);
            item.getCondicaoRenegociacao().setCodigo(0);
            item.setNovoObj(true);
            item.setStatus("AT");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CondicaoRenegociacaoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do
     * objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     * 
     * @param obj Objeto da classe <code>CondicaoRenegociacaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void incluir(final CondicaoRenegociacaoVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
            validarDados(obj);
            validarUnicidade(obj, false);
            CondicaoRenegociacao.incluir(getIdEntidade(), usuarioLogado);

            final String sql = "INSERT INTO CondicaoRenegociacao( dataCriacao, usuarioCriacao, usuarioUltimaAlteracao, " +
                    "descricao, turno, curso, turma, contaCorrentePadrao, descontoProgressivo, " +
                    "descontoEspecifico, juroEspecifico, status, dataUltimaAlteracao, perfileconomico, datainiciovigencia, dataterminovigencia, grupodestinatario, "+
                    " liberarRenovacaoAposPagamentoPrimeiraParcela, liberarRenovacaoAposPagamentoTodasParcelas, layoutPadraoTermoReconhecimentoDivida, textoPadraoDeclaracao, "
                    + "utilizarContaCorrenteEspecifica, permitirPagamentoCartaoCreditoVisaoAluno ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataCriacao()));
                    if (obj.getUsuarioCriacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getUsuarioCriacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getUsuarioUltimaAlteracao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getUsuarioUltimaAlteracao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getDescricao());
                    if (obj.getTurno().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getTurno().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }                    
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getCurso().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getTurma().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getContaCorrentePadrao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getContaCorrentePadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getDescontoProgressivo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    sqlInserir.setDouble(10, obj.getDescontoEspecifico().doubleValue());
                    sqlInserir.setDouble(11, obj.getJuroEspecifico().doubleValue());
                    sqlInserir.setString(12, obj.getStatus());
                    sqlInserir.setDate(13, Uteis.getDataJDBC(obj.getDataUltimaAlteracao()));
                    if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(14, obj.getPerfilEconomico().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(14, 0);
                    }
                    sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataInicioVigencia()));
                    sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataTerminoVigencia()));
                    if (obj.getGrupoDestinatario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(17, obj.getGrupoDestinatario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(17, 0);
                    }
                    sqlInserir.setBoolean(18, obj.getLiberarRenovacaoAposPagamentoPrimeiraParcela());
                    sqlInserir.setBoolean(19, obj.getLiberarRenovacaoAposPagamentoTodasParcelas());
					if (obj.getLayoutPadraoTermoReconhecimentoDivida() != null) {
						sqlInserir.setString(20, obj.getLayoutPadraoTermoReconhecimentoDivida().name());
					} else {
						sqlInserir.setNull(20, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracaoVO())) {
						sqlInserir.setInt(21, obj.getTextoPadraoDeclaracaoVO().getCodigo());
					} else {
						sqlInserir.setNull(21, 0);
					}
					sqlInserir.setBoolean(22, obj.getUtilizarContaCorrenteEspecifica());
					sqlInserir.setBoolean(23, obj.getPermitirPagamentoCartaoCreditoVisaoAluno());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getItemCondicaoRenegociacaoFacade().incluirItemCondicaoRenegociacaos(obj.getCodigo(), obj.getItemCondicaoRenegociacaoVOs());
            getFacadeFactory().getCondicaoRenegociacaoUnidadeEnsinoFacade().incluirCondicaoRenegociacaoUnidadeEnsinoVOs(obj.getCodigo(), obj.getListaCondicaoRenegociacaoUnidadeEnsinoVOs());
            getFacadeFactory().getCondicaoRenegociacaoFuncionarioCargoFacade().incluirCondicaoRenegociacaoFuncionarioVOs(obj.getCodigo(), obj.getListaCondicaoRenegociacaoFuncionarioVOs());
            for (ItemCondicaoRenegociacaoVO item : obj.getItemCondicaoRenegociacaoVOs()) {
                item.setNovoObj(Boolean.FALSE);
            }
        } catch (Exception e) {
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            for (ItemCondicaoRenegociacaoVO item : obj.getItemCondicaoRenegociacaoVOs()) {
                item.setNovoObj(Boolean.TRUE);
                item.setCodigo(0);
            }
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CondicaoRenegociacaoVO</code>. Sempre utiliza a chave primária da classe como atributo para
     * localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>CondicaoRenegociacaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CondicaoRenegociacaoVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
            validarDados(obj);
            if(obj.getIsAtivo()){
            	validarUnicidade(obj, true);	
            }
            CondicaoRenegociacao.alterar(getIdEntidade(), usuarioLogado);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE CondicaoRenegociacao set dataCriacao=?," +
                    " usuarioCriacao=?, usuarioUltimaAlteracao=?, descricao=?," +
                    " turno=?, curso=?, turma=?, contaCorrentePadrao=?," +
                    " descontoProgressivo=?, descontoEspecifico=?, juroEspecifico=?, status=?," +
                    " dataUltimaAlteracao=?, perfileconomico=?, datainiciovigencia=?, "
                    + "dataterminovigencia=?, grupodestinatario = ?, liberarRenovacaoAposPagamentoPrimeiraParcela=?, "
                    + "liberarRenovacaoAposPagamentoTodasParcelas=?, layoutPadraoTermoReconhecimentoDivida=?, textoPadraoDeclaracao=?, "
                    + "utilizarContaCorrenteEspecifica = ?, permitirPagamentoCartaoCreditoVisaoAluno = ? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataCriacao()));
                    if (obj.getUsuarioCriacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getUsuarioCriacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getUsuarioUltimaAlteracao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getUsuarioUltimaAlteracao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setString(4, obj.getDescricao());
                    if (obj.getTurno().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getTurno().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }                     
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getCurso().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getTurma().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (obj.getContaCorrentePadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getContaCorrentePadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getDescontoProgressivo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setDouble(10, obj.getDescontoEspecifico().doubleValue());
                    sqlAlterar.setDouble(11, obj.getJuroEspecifico().doubleValue());
                    sqlAlterar.setString(12, obj.getStatus());
                    sqlAlterar.setDate(13, Uteis.getDataJDBC(obj.getDataUltimaAlteracao()));
                    if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(14, obj.getPerfilEconomico().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(14, 0);
                    }
                    sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataInicioVigencia()));
                    sqlAlterar.setDate(16, Uteis.getDataJDBC(obj.getDataTerminoVigencia()));
                    if (obj.getGrupoDestinatario().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(17, obj.getGrupoDestinatario().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(17, 0);
                    }
                    sqlAlterar.setBoolean(18, obj.getLiberarRenovacaoAposPagamentoPrimeiraParcela());
                    sqlAlterar.setBoolean(19, obj.getLiberarRenovacaoAposPagamentoTodasParcelas());
                    if (obj.getLayoutPadraoTermoReconhecimentoDivida() != null) {
                    	sqlAlterar.setString(20, obj.getLayoutPadraoTermoReconhecimentoDivida().name());
					} else {
						sqlAlterar.setNull(20, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracaoVO())) {
						sqlAlterar.setInt(21, obj.getTextoPadraoDeclaracaoVO().getCodigo());
					} else {
						sqlAlterar.setNull(21, 0);
					}
					sqlAlterar.setBoolean(22, obj.getUtilizarContaCorrenteEspecifica());
					sqlAlterar.setBoolean(23, obj.getPermitirPagamentoCartaoCreditoVisaoAluno());
                    sqlAlterar.setInt(24, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getItemCondicaoRenegociacaoFacade().alterarItemCondicaoRenegociacaos(obj.getCodigo(), obj.getItemCondicaoRenegociacaoVOs());
            getFacadeFactory().getCondicaoRenegociacaoUnidadeEnsinoFacade().alterarCondicaoRenegociacaoUnidadeEnsinoVOs(obj.getCodigo(), obj.getListaCondicaoRenegociacaoUnidadeEnsinoVOs());
            getFacadeFactory().getCondicaoRenegociacaoFuncionarioCargoFacade().alterarCondicaoRenegociacaoFuncionarioVOs(obj.getCodigo(), obj.getListaCondicaoRenegociacaoFuncionarioVOs());
            for (ItemCondicaoRenegociacaoVO item : obj.getItemCondicaoRenegociacaoVOs()) {
                item.setNovoObj(Boolean.FALSE);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CondicaoRenegociacaoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da
     * entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>CondicaoRenegociacaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CondicaoRenegociacaoVO obj) throws Exception {
        try {
            CondicaoRenegociacao.excluir(getIdEntidade());
            String sql = "DELETE FROM CondicaoRenegociacao WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * 
     * @param CondicaoRenegociacaoVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(CondicaoRenegociacaoVO obj, UsuarioVO usuarioLogado) throws Exception {
        obj.setDataUltimaAlteracao(new Date());
        obj.setUsuarioUltimaAlteracao(usuarioLogado);
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuarioLogado);
        } else {
            alterar(obj, usuarioLogado);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>CondicaoRenegociacaoVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas
     * neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * 
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public void validarDados(CondicaoRenegociacaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        ConsistirException consistir = new ConsistirException();
        if ((obj.getUsuarioUltimaAlteracao() == null) ||
                (obj.getUsuarioUltimaAlteracao().getCodigo().intValue() == 0)) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_usuarioUltimaAlteracao"));
        }
        if ((obj.getDescricao() == null) ||(obj.getDescricao().trim().isEmpty())) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_descricao"));
        }
        boolean possuiUnidade = false;
        for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoRenegociacaoUnidadeEnsinoVO : obj.getListaCondicaoRenegociacaoUnidadeEnsinoVOs()) {
        	if (condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
        		possuiUnidade = true;
        	}
        }
        if (!possuiUnidade) {
        	consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_unidadeEnsino"));
        }
        if ((obj.getUtilizarContaCorrenteEspecifica() && (obj.getContaCorrentePadrao() == null || obj.getContaCorrentePadrao().getCodigo().intValue() == 0))) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_contaCorrentePadrao"));
        }
        if (obj.getDataInicioVigencia() == null || obj.getDataTerminoVigencia() == null) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_periodoVigencia"));
        }
        if(obj.getDataInicioVigencia() != null && consistir != null && obj.getDataInicioVigencia().compareTo(obj.getDataTerminoVigencia())>0){
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_periodoVigenciaMaior"));
        }
		if (obj.getLayoutPadraoTermoReconhecimentoDivida()!= null && obj.getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.TEXTO_PADRAO) && !Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracaoVO())) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_textoPadraoDeclaracao"));
		}
		if(!Uteis.isAtributoPreenchido(obj.getItemCondicaoRenegociacaoVOs())) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_itemCondicaoRenegociacao"));
		}
        if(obj.getLiberarRenovacaoAposPagamentoTodasParcelas()){
            obj.setLiberarRenovacaoAposPagamentoPrimeiraParcela(false); 
        }
        for(ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO: obj.getItemCondicaoRenegociacaoVOs()) {
        	if(obj.getItemCondicaoRenegociacaoVOs().stream().filter(t -> t.getDescricao().trim().equalsIgnoreCase(itemCondicaoRenegociacaoVO.getDescricao().trim())).count() > 1) {
        		consistir.adicionarListaMensagemErro("Existem mais de um ITEM DA CONDIÇÃO DE NEGOCIÇÃO com a mesma DESCRIÇÃO igual a "+itemCondicaoRenegociacaoVO.getDescricao()+".");
        	
        	}
        }
        if (consistir.existeErroListaMensagemErro()) {
            throw consistir;
        }

    }
    
    public void validarUnicidade(CondicaoRenegociacaoVO obj, boolean alterar) throws ConsistirException {
        StringBuilder sb = new StringBuilder("select * from CondicaoRenegociacao where 1=1 ");
        if(obj.getTurno() == null || obj.getTurno().getCodigo() == null || obj.getTurno().getCodigo().intValue() == 0){
            sb.append(" and turno is null ");
        }else{
            sb.append(" and turno = ").append(obj.getTurno().getCodigo());
        }
        if(obj.getCurso() == null || obj.getCurso().getCodigo() == null || obj.getCurso().getCodigo().intValue() == 0){
            sb.append(" and curso is null ");
        }else{
            sb.append(" and curso = ").append(obj.getCurso().getCodigo());
        }
        if(obj.getTurma() == null || obj.getTurma().getCodigo() == null || obj.getTurma().getCodigo().intValue() == 0){
            sb.append(" and turma is null ");
        }else{
            sb.append(" and turma = ").append(obj.getTurma().getCodigo());
        }
        if(obj.getPerfilEconomico() == null || obj.getPerfilEconomico().getCodigo() == null || obj.getPerfilEconomico().getCodigo().intValue() == 0){
            sb.append(" and perfilEconomico is null ");
        }else{
            sb.append(" and perfilEconomico = ").append(obj.getPerfilEconomico().getCodigo());
        }
        if(alterar){
            sb.append(" and codigo != ").append(obj.getCodigo());
        }
        sb.append(" and exists (select condicaoRenegociacaoUnidadeEnsino.codigo from condicaoRenegociacaoUnidadeEnsino where condicaoRenegociacaoUnidadeEnsino.CondicaoRenegociacao = CondicaoRenegociacao.codigo ");
        sb.append(" and condicaoRenegociacaoUnidadeEnsino.unidadeensino in (0");
        for(CondicaoRenegociacaoUnidadeEnsinoVO condicaoRenegociacaoUnidadeEnsinoVO: obj.getListaCondicaoRenegociacaoUnidadeEnsinoVOs()) {
        	if(condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
        		sb.append(", ").append(condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
        	}
        }
        sb.append(")) ");
        sb.append(" and CondicaoRenegociacao.status = 'AT' ");
        if(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()).next()){
            throw new StreamSeiException(UteisJSF.internacionalizar("msg_CondicaoRenegociacao_unicidade"));
        }
    }
    

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(CondicaoRenegociacaoVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }

    }
    
    public String getSqlDadosBasico(){
        StringBuilder sb = new StringBuilder(" SELECT distinct CondicaoRenegociacao.*, ");
        sb.append(" turno.nome as turno_nome,");
    	sb.append(" curso.nome as curso_nome,");
    	sb.append(" turma.identificadorturma as turma_identificadorturma,");
    	sb.append(" contacorrente.numero as contacorrente_numero,");
    	sb.append(" contacorrente.digito as contacorrente_digito,");
    	sb.append(" descontoprogressivo.nome as descontoprogressivo_nome, perfileconomico.nome as perfileconomico_nome,");
    	sb.append(" usuariocriacao.nome as usuariocriacao_nome, usuarioUltimaAlteracao.nome as usuarioultimaalteracao_nome, grupoDestinatarios.nomeGrupo as grupoDestinatario_nomeGrupo");
        sb.append(" from CondicaoRenegociacao ");
    	sb.append(" inner join condicaorenegociacaounidadeensino on condicaorenegociacaounidadeensino.condicaorenegociacao = condicaorenegociacao.codigo ");
        sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = condicaorenegociacaounidadeensino.unidadeEnsino");
        sb.append(" left join turno on turno.codigo = CondicaoRenegociacao.turno");
        sb.append(" left join curso on curso.codigo = CondicaoRenegociacao.curso");
        sb.append(" left join turma on turma.codigo = CondicaoRenegociacao.turma");
        sb.append(" left join contacorrente on contacorrente.codigo = CondicaoRenegociacao.contacorrentepadrao");
        sb.append(" left join descontoprogressivo on descontoprogressivo.codigo = CondicaoRenegociacao.descontoprogressivo");
        sb.append(" left join perfileconomico on perfileconomico.codigo = CondicaoRenegociacao.perfileconomico");
        sb.append(" left join usuario as usuariocriacao on usuariocriacao.codigo = CondicaoRenegociacao.usuariocriacao");        
    	sb.append(" left join usuario as usuarioUltimaAlteracao on usuarioUltimaAlteracao.codigo = CondicaoRenegociacao.usuarioUltimaAlteracao");
    	sb.append(" left join grupoDestinatarios on grupoDestinatarios.codigo = CondicaoRenegociacao.grupoDestinatario");
        return sb.toString();
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela CondicaoRenegociacaoCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado
     * campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<CondicaoRenegociacaoVO> consultar(String descricao, Integer unidadeEnsino, Integer turno, Integer curso, Integer turma, 
            Integer contaCorrente, Integer perfilEconomico, String status, boolean controlarAcesso,  UsuarioVO usuario) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);        
        StringBuilder sb = new StringBuilder(getSqlDadosBasico());        
        sb.append(" where 1=1 "); 
        if(descricao != null && !descricao.trim().isEmpty()){
            sb.append(" and upper(sem_acentos(CondicaoRenegociacao.descricao)) like(sem_acentos('%").append(descricao.toUpperCase()).append("%')) ");
        }
        if(unidadeEnsino  != null && unidadeEnsino > 0){
            sb.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
        }
        if(turno != null && turno.intValue() > 0){
            sb.append(" and turno.codigo = ").append(turno);
        }
        if(curso != null && curso.intValue() > 0){
            sb.append(" and curso.codigo = ").append(curso);
        }
        if(turma != null && turma.intValue() > 0){
            sb.append(" and turma.codigo = ").append(turma);
        }
        if(contaCorrente != null && contaCorrente.intValue() > 0){            
            sb.append(" and contaCorrente.codigo = ").append(contaCorrente);
        }
        if(perfilEconomico != null && perfilEconomico.intValue() > 0){            
            sb.append(" and perfilEconomico.codigo = ").append(perfilEconomico);
        }
        if(Uteis.isAtributoPreenchido(status)) {
        	if(status.equals("IN")) {
        		sb.append(" and CondicaoRenegociacao.status = 'IN'");
        		
        	}else if(!status.equals("IN")) {
        		sb.append(" and CondicaoRenegociacao.status = 'AT'");
        		if(status.equals("ATDP")) {
        			sb.append(" and CondicaoRenegociacao.dataInicioVigencia <= current_date");
        			sb.append(" and CondicaoRenegociacao.dataTerminoVigencia >= current_date");
        		}else if(status.equals("ATFP")) {
        			sb.append(" and CondicaoRenegociacao.dataInicioVigencia > current_date");        			
        		}else if(status.endsWith("ATPE")) {
        			sb.append(" and CondicaoRenegociacao.dataTerminoVigencia < current_date ");
        		}
        	}
        }
        sb.append(" order by CondicaoRenegociacao.codigo");       
        return montarDadosConsultaBasico(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), false, usuario);        
    }
    
    /**Esta consulta segue a seguinte regra de busca:
     *  Trazer a condição de renegociação no periodo vigente que tenha a faixa de valor solicitado em uma unidade de ensino e que possua
     *  a maior quantidade de combinação entre Perfil Econômico, Turma, Curso, Turno
     * @param perfilEconomino
     * @param turma
     * @param curso
     * @param turno
     * @param unidadeEnsino
     * @param valor
     * @param usuarioLogado
     * @return
     * @throws Exception
     */
    @Override
    public CondicaoRenegociacaoVO consultarCondicaoRenegociacaoDisponivelAluno(Integer perfilEconomino, Integer turma, Integer curso, Integer turno, int unidadeEnsino, double valor, Long nrDiasAtraso, Boolean visaoAdministrativa, UsuarioVO usuarioLogado) throws Exception{
    	StringBuilder sb = new StringBuilder("select condicaorenegociacao.* from condicaorenegociacao ");  
        sb.append(" inner join itemcondicaorenegociacao on condicaorenegociacao.codigo = itemcondicaorenegociacao.condicaorenegociacao");
        sb.append(" inner join condicaorenegociacaounidadeensino on condicaorenegociacaounidadeensino.condicaorenegociacao = condicaorenegociacao.codigo ");
        sb.append(" where condicaorenegociacao.status = 'AT' and itemcondicaorenegociacao.status = 'AT' ");
        if (visaoAdministrativa) {
        	sb.append(" and itemcondicaorenegociacao.utilizarVisaoAdministrativa ");
        } else {
        	sb.append(" and itemcondicaorenegociacao.utilizarVisaoAluno ");
        }
        sb.append(" ");
        sb.append(" and valorinicial  <= ").append(Uteis.arredondarDecimal(valor,2)).append(" and valorfinal  >= ").append(Uteis.arredondarDecimal(valor,2)).append(" and condicaorenegociacaounidadeensino.unidadeensino = ").append(unidadeEnsino);
        sb.append(" and qtdeInicialDiasAtraso  <= ").append(nrDiasAtraso).append(" and qtdeFinalDiasAtraso  >= ").append(nrDiasAtraso);
        sb.append(" and (datainiciovigencia<= current_date and  dataterminovigencia >= current_date)");
        sb.append(" and (");         
        if(perfilEconomino != null && perfilEconomino > 0){
            //Combina Perfil Economico, Turma, Curso, Turno
            sb.append(" (perfilEconomico = ").append(perfilEconomino).append(" and turma = ").append(turma).append(" and curso = ").append(curso).append(" and turno = ").append(turno).append(")");
            //Combina Turma, Curso, Turno
            sb.append(" or (perfilEconomico is null and turma = ").append(turma).append(" and curso = ").append(curso).append(" and turno =").append(turno).append(")");
           //Combina Perfil Economico, Curso, Turno
            sb.append(" or (perfilEconomico = ").append(perfilEconomino).append(" and curso = ").append(curso).append(" and turno =").append(turno).append(" and turma is null)");
            //Combina Curso, Turno
            sb.append(" or (perfilEconomico is null and curso = ").append(curso).append(" and turno =").append(turno).append(" and turma is null)");
            //Combina Perfil Economico, Curso
            sb.append(" or (perfilEconomico = ").append(perfilEconomino).append(" and curso = ").append(curso).append(" and turno is null and turma is null)");
           //Combina Curso
            sb.append(" or (perfilEconomico is null and curso = ").append(curso).append(" and turno is null and turma is null)");
            //Combina Perfil Economico, Turno
            sb.append(" or (perfilEconomico = ").append(perfilEconomino).append(" and turno = ").append(turno).append(" and curso is null and turma is null)");
            //Combina Turno
            sb.append(" or (perfilEconomico is null and turno = ").append(turno).append(" and curso is null and turma is null)");
            //Combina Perfil Economico
            sb.append(" or (perfilEconomico = ").append(perfilEconomino).append(" and turma is null and curso is null and turno is null)");
           //Combina Tudo Null
            sb.append(" or (perfilEconomico is null and turno is null and curso is null and turma is null)");
        }else{        
            //Combina Turma, Curso, Turno
            sb.append(" (perfilEconomico is null and turma = ").append(turma).append(" and curso = ").append(curso).append(" and turno =").append(turno).append(")");
            //Combina Curso, Turno
            sb.append(" or (perfilEconomico is null and curso = ").append(curso).append(" and turno = ").append(turno).append(" and turma is null)");
            //Combina Curso
            sb.append(" or (perfilEconomico is null and curso = ").append(curso).append(" and turno is null and turma is null)");
           //Combina Turno
            sb.append(" or (perfilEconomico is null and turno = ").append(turno).append(" and curso is null and turma is null)");
            //Combina Tudo Null
            sb.append(" or (perfilEconomico is null and turno is null and curso is null and turma is null)");
        }        
        sb.append(" )");         
        sb.append(" group by condicaorenegociacao.status, condicaorenegociacao.unidadeEnsino,"); 
        sb.append(" condicaorenegociacao.codigo, perfilEconomico, turma, curso, turno, juroespecifico,");
        sb.append(" descontoespecifico, contacorrentepadrao, descontoprogressivo, condicaorenegociacao.descricao, usuarioultimaalteracao, usuariocriacao,"); 
        sb.append(" datacriacao, dataultimaalteracao, datainiciovigencia, dataterminovigencia, condicaorenegociacao.grupodestinatario, liberarrenovacaoapospagamentoprimeiraparcela, liberarrenovacaoapospagamentotodasparcelas");
        sb.append(" order by perfilEconomico, turma, curso, turno limit 1");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            CondicaoRenegociacaoVO obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
            obj.setItemCondicaoRenegociacaoVOs(getFacadeFactory().getItemCondicaoRenegociacaoFacade().consultarItemCondicaoRenegociacaoAptaAlunoUtilizar(obj.getCodigo(), valor));
            obj.setListaCondicaoRenegociacaoFuncionarioVOs(getFacadeFactory().getCondicaoRenegociacaoFuncionarioCargoFacade().consultarCondicaoRenegociacaoFuncionarioPorCondicaoRenegociacao(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
            return obj;
        }
        return null;
    }
    
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public CondicaoRenegociacaoVO consultarCondicaoRenegociacaoPorUnidadeEnsino(Integer unidadeEnsino, boolean visaoAdministrativa,  UsuarioVO usuarioLogado) throws Exception{
    	StringBuilder sb = new StringBuilder("select condicaorenegociacao.* from condicaorenegociacao ");  
    	sb.append(" inner join itemcondicaorenegociacao on condicaorenegociacao.codigo = itemcondicaorenegociacao.condicaorenegociacao");
    	sb.append(" inner join condicaorenegociacaounidadeensino on condicaorenegociacaounidadeensino.condicaorenegociacao = condicaorenegociacao.codigo ");
    	sb.append(" where condicaorenegociacao.status = 'AT' and itemcondicaorenegociacao.status = 'AT' ");
    	if (visaoAdministrativa) {
    		sb.append(" and itemcondicaorenegociacao.utilizarVisaoAdministrativa  = true ");
    	}
    	sb.append(" and condicaorenegociacaounidadeensino.unidadeensino = ").append(unidadeEnsino);
    	sb.append(" and (datainiciovigencia<= current_date and  dataterminovigencia >= current_date)");
    	sb.append(" order by turma, curso, turno limit 1");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if(rs.next()){
    		CondicaoRenegociacaoVO obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
    		obj.getListaCondicaoRenegociacaoUnidadeEnsinoVOs().add(getFacadeFactory().getCondicaoRenegociacaoUnidadeEnsinoFacade().consultarCondicaoRenegociacaoUnidadeEnsinoPorCondicaoRenegociacaoPorUnidadeEnsino(obj.getCodigo(), unidadeEnsino,  Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
    		return obj;
    	}
    	return new CondicaoRenegociacaoVO();
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoRenegociacao</code> através do valor do atributo <code>codigo</code> da classe <code>DescontoProgressivo</code> Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoDescontoProgressivo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CondicaoRenegociacao.* FROM CondicaoRenegociacao, DescontoProgressivo WHERE CondicaoRenegociacao.descontoProgressivo = DescontoProgressivo.codigo and DescontoProgressivo.codigo >= ? ORDER BY DescontoProgressivo.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoRenegociacao</code> através do valor do atributo <code>codigo</code> da classe <code>ContaCorrente</code> Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoContaCorrente(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CondicaoRenegociacao.* FROM CondicaoRenegociacao, ContaCorrente WHERE CondicaoRenegociacao.contaCorrentePadrao = ContaCorrente.codigo and ContaCorrente.codigo >= ? ORDER BY ContaCorrente.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoRenegociacao</code> através do valor do atributo <code>codigo</code> da classe <code>Turma</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CondicaoRenegociacao.* FROM CondicaoRenegociacao, Turma WHERE CondicaoRenegociacao.turma = Turma.codigo and Turma.codigo >= ? ORDER BY Turma.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoRenegociacao</code> através do valor do atributo <code>codigo</code> da classe <code>Curso</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CondicaoRenegociacao.* FROM CondicaoRenegociacao, Curso WHERE CondicaoRenegociacao.curso = Curso.codigo and Curso.codigo >= ? ORDER BY Curso.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoRenegociacao</code> através do valor do atributo <code>codigo</code> da classe <code>UnidadeEnsino</code> Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CondicaoRenegociacao.* FROM CondicaoRenegociacao, UnidadeEnsino WHERE CondicaoRenegociacao.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo >= ? ORDER BY UnidadeEnsino.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoRenegociacao</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        CondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CondicaoRenegociacao WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }
    
    public List<CursoVO> consultarCursoPorUnidadeEnsinoCondicaoRenegociacao(String campoConsultarCurso, String valorConsultarCurso, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer codigoTurno, UsuarioVO usuario) throws Exception {
        List<CursoVO> objs = new ArrayList<CursoVO>(0);
        if (campoConsultarCurso.equals("nome")) {
            objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorCodigoUnidadeEnsinoCondicaoRenegociacao(valorConsultarCurso, listaCondicaoUnidadeEnsinoVOs, codigoTurno, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        }
        else if (campoConsultarCurso.equals("codigo")) {
            objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoPorCodigoUnidadeEnsinoCondicaoRenegociacao(Integer.valueOf(valorConsultarCurso), listaCondicaoUnidadeEnsinoVOs, codigoTurno, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        }
        return objs;
    }

    public List<CursoVO> consultarCurso(String campoConsultarCurso, String valorConsultarCurso, Integer codigoUnidadeEnsino, Integer codigoTurno, UsuarioVO usuario) throws Exception {
        List<CursoVO> objs = new ArrayList<CursoVO>(0);
        if (campoConsultarCurso.equals("nome")) {
            objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorCodigoUnidadeEnsinoTurno(valorConsultarCurso, codigoUnidadeEnsino, codigoTurno, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        }
        else if (campoConsultarCurso.equals("codigo")) {
            objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoPorCodigoUnidadeEnsinoTurno(Integer.valueOf(valorConsultarCurso), codigoUnidadeEnsino, codigoTurno, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        }
        return objs;
    }

    public List<TurmaVO> consultarTurma(String campoConsultarCurso, String valorConsultarCurso, Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, UsuarioVO usuario) throws Exception {
        List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
        if (campoConsultarCurso.equals("identificador")) {
            objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(valorConsultarCurso, codigoUnidadeEnsino, codigoCurso, codigoTurno, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,"", usuario);
        }
        return objs;
    }
    
    public List<TurmaVO> consultarTurmaPorUnidadeEnsinoCondicaoRenegociacao(String campoConsultarCurso, String valorConsultarCurso, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer codigoCurso, Integer codigoTurno, UsuarioVO usuario) throws Exception {
        List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
        if (campoConsultarCurso.equals("identificador")) {
            objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurmaUnidadeEnsinoCondicaoRenegociacao(valorConsultarCurso, listaCondicaoUnidadeEnsinoVOs, codigoCurso, codigoTurno, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        }
        return objs;
    }
    
    public static List<CondicaoRenegociacaoVO> montarDadosConsultaBasico(SqlRowSet tabelaResultado, boolean trazerEntidadesSubordinadas, UsuarioVO usuario) throws Exception {
        List<CondicaoRenegociacaoVO> vetResultado = new ArrayList<CondicaoRenegociacaoVO>(0);
        CondicaoRenegociacaoVO condicaoRenegociacaoVO = null;
        while (tabelaResultado.next()) {
            if(!trazerEntidadesSubordinadas){
                condicaoRenegociacaoVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }else{
                condicaoRenegociacaoVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
            }
            if(condicaoRenegociacaoVO.getTurno().getCodigo()>0){
                condicaoRenegociacaoVO.getTurno().setNovoObj(false);
                condicaoRenegociacaoVO.getTurno().setNome(tabelaResultado.getString("turno_nome"));
            }
            if(condicaoRenegociacaoVO.getCurso().getCodigo()>0){
                condicaoRenegociacaoVO.getCurso().setNovoObj(false);
                condicaoRenegociacaoVO.getCurso().setNome(tabelaResultado.getString("curso_nome"));
            }
            if(condicaoRenegociacaoVO.getTurma().getCodigo()>0){
                condicaoRenegociacaoVO.getTurma().setNovoObj(false);
                condicaoRenegociacaoVO.getTurma().setIdentificadorTurma(tabelaResultado.getString("turma_identificadorturma"));
            }            
            condicaoRenegociacaoVO.getContaCorrentePadrao().setNumero(tabelaResultado.getString("contacorrente_numero"));
            condicaoRenegociacaoVO.getContaCorrentePadrao().setDigito(tabelaResultado.getString("contacorrente_digito"));
            condicaoRenegociacaoVO.getContaCorrentePadrao().setNovoObj(false);
            if(condicaoRenegociacaoVO.getDescontoProgressivo().getCodigo() >0){
                condicaoRenegociacaoVO.getDescontoProgressivo().setNome(tabelaResultado.getString("descontoprogressivo_nome"));
                condicaoRenegociacaoVO.getDescontoProgressivo().setNovoObj(false);
            }            
            if(condicaoRenegociacaoVO.getPerfilEconomico().getCodigo() >0){
                condicaoRenegociacaoVO.getPerfilEconomico().setNome(tabelaResultado.getString("perfileconomico_nome"));
                condicaoRenegociacaoVO.getPerfilEconomico().setNovoObj(false);
            }
            condicaoRenegociacaoVO.getUsuarioCriacao().setNome(tabelaResultado.getString("usuariocriacao_nome"));
            condicaoRenegociacaoVO.getUsuarioCriacao().setNovoObj(false);
            if(condicaoRenegociacaoVO.getUsuarioUltimaAlteracao().getCodigo()>0){
                condicaoRenegociacaoVO.getUsuarioUltimaAlteracao().setNome(tabelaResultado.getString("usuarioultimaalteracao_nome"));
                condicaoRenegociacaoVO.getUsuarioUltimaAlteracao().setNovoObj(false);
            }
            if(condicaoRenegociacaoVO.getGrupoDestinatario().getCodigo()>0){
                condicaoRenegociacaoVO.getGrupoDestinatario().setNomeGrupo(tabelaResultado.getString("grupoDestinatario_nomeGrupo"));
                condicaoRenegociacaoVO.getGrupoDestinatario().setNovoObj(false);
            }
            
            
            
            vetResultado.add(condicaoRenegociacaoVO);
        }
        tabelaResultado = null;
        return vetResultado;   
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que
     * realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>CondicaoRenegociacaoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>CondicaoRenegociacaoVO</code>.
     * 
     * @return O objeto da classe <code>CondicaoRenegociacaoVO</code> com os dados devidamente montados.
     */
    public static CondicaoRenegociacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CondicaoRenegociacaoVO obj = new CondicaoRenegociacaoVO();
        obj.setDataCriacao(dadosSQL.getDate("dataCriacao"));
        obj.setDataUltimaAlteracao(dadosSQL.getDate("dataUltimaAlteracao"));
        obj.getUsuarioCriacao().setCodigo(new Integer(dadosSQL.getInt("usuarioCriacao")));
        obj.getUsuarioUltimaAlteracao().setCodigo(new Integer(dadosSQL.getInt("usuarioUltimaAlteracao")));
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setUtilizarContaCorrenteEspecifica(dadosSQL.getBoolean("utilizarContaCorrenteEspecifica"));
        obj.getTurno().setCodigo((dadosSQL.getInt("turno")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getContaCorrentePadrao().setCodigo(new Integer(dadosSQL.getInt("contaCorrentePadrao")));
        obj.getDescontoProgressivo().setCodigo(new Integer(dadosSQL.getInt("descontoProgressivo")));
        obj.getPerfilEconomico().setCodigo(new Integer(dadosSQL.getInt("perfilEconomico")));
        obj.getGrupoDestinatario().setCodigo(new Integer(dadosSQL.getInt("grupoDestinatario")));
        obj.setDescontoEspecifico(new Double(dadosSQL.getDouble("descontoEspecifico")));
        obj.setJuroEspecifico(new Double(dadosSQL.getDouble("juroEspecifico")));
        obj.setStatus(dadosSQL.getString("status"));
        obj.setDataInicioVigencia(dadosSQL.getDate("dataInicioVigencia"));
        obj.setDataTerminoVigencia(dadosSQL.getDate("dataTerminoVigencia"));
        obj.setLiberarRenovacaoAposPagamentoPrimeiraParcela(dadosSQL.getBoolean("liberarRenovacaoAposPagamentoPrimeiraParcela"));
        obj.setLiberarRenovacaoAposPagamentoTodasParcelas(dadosSQL.getBoolean("liberarRenovacaoAposPagamentoTodasParcelas"));
        obj.setPermitirPagamentoCartaoCreditoVisaoAluno(dadosSQL.getBoolean("permitirPagamentoCartaoCreditoVisaoAluno"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("layoutPadraoTermoReconhecimentoDivida"))) {
			obj.setLayoutPadraoTermoReconhecimentoDivida(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.valueOf(dadosSQL.getString("layoutPadraoTermoReconhecimentoDivida")));
		}
		obj.getTextoPadraoDeclaracaoVO().setCodigo(dadosSQL.getInt("textoPadraoDeclaracao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setItemCondicaoRenegociacaoVOs(ItemCondicaoRenegociacao.consultarItemCondicaoRenegociacaos(obj.getCodigo(), nivelMontarDados));
        obj.setListaCondicaoRenegociacaoUnidadeEnsinoVOs(getFacadeFactory().getCondicaoRenegociacaoUnidadeEnsinoFacade().consultarCondicaoRenegociacaoUnidadeEnsinoPorCondicaoRenegociacao(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        obj.setListaCondicaoRenegociacaoFuncionarioVOs(getFacadeFactory().getCondicaoRenegociacaoFuncionarioCargoFacade().consultarCondicaoRenegociacaoFuncionarioPorCondicaoRenegociacao(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        montarDadosUsuarioCriacao(obj, nivelMontarDados, usuario);
        montarDadosUsuarioUltimaAlteracao(obj, nivelMontarDados, usuario);
//        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosCurso(obj, nivelMontarDados, usuario);
        montarDadosTurma(obj, nivelMontarDados, usuario);
        montarDadosTurno(obj, nivelMontarDados, usuario);
        montarDadosContaCorrentePadrao(obj, nivelMontarDados, usuario);
        montarDadosDescontoProgressivo(obj, nivelMontarDados, usuario);
        montarDadosGrupoDestinatario(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>ItemCondicaoRenegociacaoVO</code> ao List <code>itemCondicaoRenegociacaoVOs</code>. Utiliza o atributo
     * padrão de consulta da classe <code>ItemCondicaoRenegociacao</code> - getCodigo() - como identificador (key) do objeto no List.
     * 
     * @param obj Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjItemCondicaoRenegociacaoVOs(CondicaoRenegociacaoVO objCondicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO obj) throws ConsistirException, Exception {
        getFacadeFactory().getItemCondicaoRenegociacaoFacade().validarDados(obj);
        obj.setCondicaoRenegociacao(objCondicaoRenegociacaoVO);
        verificarRegrasInclusao(obj, objCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs());
        objCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs().add(obj);
        Ordenacao.ordenarLista(objCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs(), "valorInicial");

    }

    public void verificarRegrasInclusao(ItemCondicaoRenegociacaoVO obj, List<ItemCondicaoRenegociacaoVO> lista) throws ConsistirException {    	
    	if (obj.getFaixaEntradaInicial() > obj.getFaixaEntradaFinal()) {
    		throw new ConsistirException("A faixa de entrada inicial não pode ser maior que a faixa de entrada final.");
    	}

    	if (obj.getValorInicial() > obj.getValorFinal()) {
    		throw new ConsistirException("A faixa de valor inicial não pode ser maior que a faixa de valor final.");
    	}

        if (Uteis.isAtributoPreenchido(lista)) {
        	for (ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO : lista) {
				if (itemCondicaoRenegociacaoVO.getIsAtivo()) {
					if(obj.getDescricao().trim().equalsIgnoreCase(itemCondicaoRenegociacaoVO.getDescricao().trim())) {
						throw new ConsistirException("Já existe uma condição de renegociação com a DESCRIÇÃO informada.");
					}
					if ( obj.getFaixaEntradaInicial().equals(itemCondicaoRenegociacaoVO.getFaixaEntradaInicial()) && 
							obj.getFaixaEntradaFinal().equals(itemCondicaoRenegociacaoVO.getFaixaEntradaFinal())) {
						throw new ConsistirException("Já existe uma condição de renegociação com a faixa de entrada inicial e faixa de entrada final com os valores informados.");
					}
				}
			}
        }
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>ItemCondicaoRenegociacaoVO</code> no List <code>itemCondicaoRenegociacaoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemCondicaoRenegociacao</code> - getCodigo() - como identificador (key) do objeto no List.
     * 
     * @param codigo Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItemCondicaoRenegociacaoVOs(CondicaoRenegociacaoVO objCondicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO obj) throws Exception {
        int index = 0;
        for (ItemCondicaoRenegociacaoVO objExistente : objCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()) {
        	if (objExistente.getValorInicial().doubleValue() == obj.getValorInicial().doubleValue()
                    && objExistente.getValorFinal().doubleValue() == obj.getValorFinal().doubleValue()
                    && objExistente.getParcelaInicial().intValue() == obj.getParcelaInicial().intValue()
                    && objExistente.getParcelaFinal().intValue() == obj.getParcelaFinal().intValue()
                    && objExistente.getQtdeInicialDiasAtraso().intValue() == obj.getQtdeInicialDiasAtraso().intValue()
                    && objExistente.getQtdeFinalDiasAtraso().intValue() == obj.getQtdeFinalDiasAtraso().intValue()
                    ) {
                objCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    @Override
    public void realizarAtivacaoItemCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception {
    	verificarRegrasInclusao(itemCondicaoRenegociacaoVO, condicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs());

    	String status = itemCondicaoRenegociacaoVO.getStatus();
        try {
            itemCondicaoRenegociacaoVO.setStatus("AT");
            condicaoRenegociacaoVO.setUsuarioUltimaAlteracao(usuarioLogado);
            condicaoRenegociacaoVO.setDataUltimaAlteracao(new Date());
            if(Uteis.isAtributoPreenchido(itemCondicaoRenegociacaoVO)) {
            	alterar(condicaoRenegociacaoVO, usuarioLogado);
            }
        } catch (Exception e) {
            itemCondicaoRenegociacaoVO.setStatus(status);
            throw e;
        }
    }

    @Override
    public void realizarInativacaoItemCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception {
    	String status = itemCondicaoRenegociacaoVO.getStatus();
        try {
            itemCondicaoRenegociacaoVO.setStatus("IN");
            condicaoRenegociacaoVO.setUsuarioUltimaAlteracao(usuarioLogado);
            condicaoRenegociacaoVO.setDataUltimaAlteracao(new Date());
            if(Uteis.isAtributoPreenchido(itemCondicaoRenegociacaoVO)) {
            	alterar(condicaoRenegociacaoVO, usuarioLogado);
            }
        } catch (Exception e) {
            itemCondicaoRenegociacaoVO.setStatus(status);
            throw e;
        }
    }

    @Override
    public void realizarAtivacaoCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception {
    	String status = condicaoRenegociacaoVO.getStatus();
        try {
            condicaoRenegociacaoVO.setStatus("AT");
            for (ItemCondicaoRenegociacaoVO item : condicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()) {
                item.setStatusAnterior(item.getStatus());
                item.setStatus("AT");
            }
            condicaoRenegociacaoVO.setUsuarioUltimaAlteracao(usuarioLogado);
            condicaoRenegociacaoVO.setDataUltimaAlteracao(new Date());            
            alterar(condicaoRenegociacaoVO, usuarioLogado);
            
        } catch (Exception e) {
            condicaoRenegociacaoVO.setStatus(status);
            for (ItemCondicaoRenegociacaoVO item : condicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()) {
                item.setStatus(item.getStatusAnterior());
            }
            throw e;
        }
    }

    @Override
    public void realizarInativacaoCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception {
        try {
            condicaoRenegociacaoVO.setStatus("IN");
            for (ItemCondicaoRenegociacaoVO item : condicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()) {
                item.setStatusAnterior(item.getStatus());
                item.setStatus("IN");
            }
            condicaoRenegociacaoVO.setUsuarioUltimaAlteracao(usuarioLogado);
            condicaoRenegociacaoVO.setDataUltimaAlteracao(new Date());
            alterar(condicaoRenegociacaoVO, usuarioLogado);
        } catch (Exception e) {
            condicaoRenegociacaoVO.setStatus("AT");
            for (ItemCondicaoRenegociacaoVO item : condicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()) {
                item.setStatus(item.getStatusAnterior());
            }
            throw e;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>ItemCondicaoRenegociacaoVO</code> no List <code>itemCondicaoRenegociacaoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemCondicaoRenegociacao</code> - getCodigo() - como identificador (key) do objeto no List.
     * 
     * @param codigo Parâmetro para localizar o objeto do List.
     */
    public ItemCondicaoRenegociacaoVO consultarObjItemCondicaoRenegociacaoVO(CondicaoRenegociacaoVO objCondicaoRenegociacaoVO, Integer codigo) throws Exception {
        for (ItemCondicaoRenegociacaoVO objExistente : objCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVOs()) {
            if (objExistente.getCodigo().equals(codigo)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DescontoProgressivoVO</code> relacionado ao objeto <code>CondicaoRenegociacaoVO</code>. Faz uso da
     * chave primária da classe <code>DescontoProgressivoVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDescontoProgressivo(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivo().getCodigo().intValue() == 0) {
            obj.setDescontoProgressivo(new DescontoProgressivoVO());
            return;
        }
        obj.setDescontoProgressivo(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivo().getCodigo(), usuario));
    }
    
    public static void montarDadosGrupoDestinatario(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getGrupoDestinatario().getCodigo().intValue() == 0) {
            obj.setGrupoDestinatario(new GrupoDestinatariosVO());
            return;
        }
        obj.setGrupoDestinatario(getFacadeFactory().getGrupoDestinatariosFacade().consultarPorChavePrimaria(obj.getGrupoDestinatario().getCodigo(),false,nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>CondicaoRenegociacaoVO</code>. Faz uso da chave
     * primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrentePadrao(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrentePadrao().getCodigo().intValue() == 0) {
            obj.setContaCorrentePadrao(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrentePadrao(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrentePadrao().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurmaVO</code> relacionado ao objeto <code>CondicaoRenegociacaoVO</code>. Faz uso da chave primária da
     * classe <code>TurmaVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurma(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
    }
    
    public static void montarDadosTurno(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurno().getCodigo().intValue() == 0) {
            obj.setTurno(new TurnoVO());
            return;
        }
        obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>CondicaoRenegociacaoVO</code>. Faz uso da chave primária da
     * classe <code>CursoVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }
    

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>CondicaoRenegociacaoVO</code>. Faz uso da chave primária
     * da classe <code>UsuarioVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUsuarioUltimaAlteracao(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuarioUltimaAlteracao().getCodigo().intValue() == 0) {
            obj.setUsuarioUltimaAlteracao(new UsuarioVO());
            return;
        }
        obj.setUsuarioUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioUltimaAlteracao().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>CondicaoRenegociacaoVO</code>. Faz uso da chave primária
     * da classe <code>UsuarioVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUsuarioCriacao(CondicaoRenegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuarioCriacao().getCodigo().intValue() == 0) {
            obj.setUsuarioCriacao(new UsuarioVO());
            return;
        }
        obj.setUsuarioCriacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioCriacao().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por adicionar um objeto da <code>ItemCondicaoRenegociacaoVO</code> no Hashtable <code>ItemCondicaoRenegociacaos</code>. Neste Hashtable são mantidos
     * todos os objetos de ItemCondicaoRenegociacao de uma determinada CondicaoRenegociacao.
     * 
     * @param obj Objeto a ser adicionado no Hashtable.
     */
    public void adicionarObjItemCondicaoRenegociacaos(ItemCondicaoRenegociacaoVO obj) throws Exception {

    }

    /**
     * Operação responsável por remover um objeto da classe <code>ItemCondicaoRenegociacaoVO</code> do Hashtable <code>ItemCondicaoRenegociacaos</code>. Neste Hashtable são
     * mantidos todos os objetos de ItemCondicaoRenegociacao de uma determinada CondicaoRenegociacao.
     * 
     * @param Codigo Atributo da classe <code>ItemCondicaoRenegociacaoVO</code> utilizado como apelido (key) no Hashtable.
     */

    /**
     * Operação responsável por localizar um objeto da classe <code>CondicaoRenegociacaoVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CondicaoRenegociacaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CondicaoRenegociacao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CondicaoRenegociacao ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<SelectItem> montarListSelectItemUnidadeEnsino(UsuarioVO usuario) throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        List<UnidadeEnsinoVO> listUniEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        objs.add(new SelectItem(0, ""));
        if (listUniEnsino != null && !listUniEnsino.isEmpty()) {
            for (UnidadeEnsinoVO unidade : listUniEnsino) {
                objs.add(new SelectItem(unidade.getCodigo(), unidade.getNome()));
            }
        }

        return objs;
    }

    public List<SelectItem> montarListSelectItemTurno(List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem(0, "Todos"));
        List<TurnoVO> listTurno = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCondicaoRenegociacao(listaCondicaoUnidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        if (listTurno != null && !listTurno.isEmpty()) {
            for (TurnoVO obj : listTurno) {
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
        }

        return objs;
    }
    
    @Override
    public List<SelectItem> montarListSelectItemTurno(Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem(0, "Todos"));
        List<TurnoVO> listTurno = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        if (listTurno != null && !listTurno.isEmpty()) {
            for (TurnoVO obj : listTurno) {
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
        }

        return objs;
    }

    public List<SelectItem> montarListSelectItemContaCorrente(Integer codigoUnidadeEnsino, Boolean obrigatorio, UsuarioVO usuario) throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        List<ContaCorrenteVO> listContaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipoSituacao(false, false, codigoUnidadeEnsino, "AT", usuario);
        if (!obrigatorio) {
        	objs.add(new SelectItem(0, ""));
        }
        if (listContaCorrente != null && !listContaCorrente.isEmpty()) {
            for (ContaCorrenteVO obj : listContaCorrente) {
            	if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
            		objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
            	} else {
            		objs.add(new SelectItem(obj.getCodigo(), obj.getNumeroDigito()));
            	}
            }
        }
        Uteis.liberarListaMemoria(listContaCorrente);
        return objs;
    }

    public List<SelectItem> montarListSelectItemDescontoProgressivo(UsuarioVO usuario) throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem(0, ""));
        List<DescontoProgressivoVO> listDesconto = getFacadeFactory().getDescontoProgressivoFacade().consultarDescontoProgressivoAtivosComboBox(usuario);
        if (listDesconto != null && !listDesconto.isEmpty()) {
            for (DescontoProgressivoVO obj : listDesconto) {
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
        }

        return objs;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CondicaoRenegociacao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com
     * objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CondicaoRenegociacao.idEntidade = idEntidade;
    }
    
    @Override
    public void inicializarDadosUnidadeEnsinoSelecionada(CondicaoRenegociacaoVO condicaoRenegociacaoVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO) {
    	condicaoRenegociacaoVO.getListaCondicaoRenegociacaoUnidadeEnsinoVOs().clear();
		for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
			CondicaoRenegociacaoUnidadeEnsinoVO condicaoRenegociacaoUnidadeEnsinoVO = new CondicaoRenegociacaoUnidadeEnsinoVO();
			condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(unidadeEnsinoVO.getCodigo());
			condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(unidadeEnsinoVO.getNome());
			condicaoRenegociacaoVO.getListaCondicaoRenegociacaoUnidadeEnsinoVOs().add(condicaoRenegociacaoUnidadeEnsinoVO);
		}
    }
    
    @Override
    public void adicionarCondicaoRenegociacaoFuncionarVOs(List<CondicaoRenegociacaoFuncionarioVO> listaCondicaoFuncionarioVOs, CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioAdicionarVO, UsuarioVO usuarioVO) {
    	int index = 0;
    	for (CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioVO : listaCondicaoFuncionarioVOs) {
    		if (condicaoRenegociacaoFuncionarioVO.getFuncionarioVO().getCodigo().equals(condicaoRenegociacaoFuncionarioAdicionarVO.getFuncionarioVO().getCodigo())) {
    			listaCondicaoFuncionarioVOs.set(index, condicaoRenegociacaoFuncionarioAdicionarVO);
    			return;
    		}
    		index++;
		}
    	listaCondicaoFuncionarioVOs.add(index, condicaoRenegociacaoFuncionarioAdicionarVO);
    }
    
    @Override
    public void removerCondicaoRenegociacaoFuncionarVOs(List<CondicaoRenegociacaoFuncionarioVO> listaCondicaoFuncionarioVOs, CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioAdicionarVO, UsuarioVO usuarioVO) {
    	int index = 0;
    	for (CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioVO : listaCondicaoFuncionarioVOs) {
    		if (condicaoRenegociacaoFuncionarioVO.getFuncionarioVO().getCodigo().equals(condicaoRenegociacaoFuncionarioAdicionarVO.getFuncionarioVO().getCodigo())) {
    			listaCondicaoFuncionarioVOs.remove(index);
    			return;
    		}
    		index++;
		}
    }
    
    @Override
    public void inicializarDadosUnidadeEnsinoSelecionadaEdicao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO) {
    	HashMap<Integer, CondicaoRenegociacaoUnidadeEnsinoVO> mapCondicaoUnidadeEnsinoVOs = new HashMap<Integer, CondicaoRenegociacaoUnidadeEnsinoVO>(0);
    	for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoRenegociacaoUnidadeEnsinoEdicaoVO : condicaoRenegociacaoVO.getListaCondicaoRenegociacaoUnidadeEnsinoVOs()) {
    		mapCondicaoUnidadeEnsinoVOs.put(condicaoRenegociacaoUnidadeEnsinoEdicaoVO.getUnidadeEnsinoVO().getCodigo(), condicaoRenegociacaoUnidadeEnsinoEdicaoVO);
    	}
    	condicaoRenegociacaoVO.getListaCondicaoRenegociacaoUnidadeEnsinoVOs().clear();
    	for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
			CondicaoRenegociacaoUnidadeEnsinoVO condicaoRenegociacaoUnidadeEnsinoVO = new CondicaoRenegociacaoUnidadeEnsinoVO();
			if (mapCondicaoUnidadeEnsinoVOs.containsKey(unidadeEnsinoVO.getCodigo())) {
				condicaoRenegociacaoUnidadeEnsinoVO = mapCondicaoUnidadeEnsinoVOs.get(unidadeEnsinoVO.getCodigo());
			} else {
				condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(false);
				condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(unidadeEnsinoVO.getCodigo());
				condicaoRenegociacaoUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(unidadeEnsinoVO.getNome());
			}
			
			condicaoRenegociacaoVO.getListaCondicaoRenegociacaoUnidadeEnsinoVOs().add(condicaoRenegociacaoUnidadeEnsinoVO);
		}
		
    }
}