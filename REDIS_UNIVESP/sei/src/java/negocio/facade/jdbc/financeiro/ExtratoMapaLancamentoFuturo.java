package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ExtratoMapaLancamentoFuturoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ExtratoMapaLancamentoFuturoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy

public class ExtratoMapaLancamentoFuturo extends ControleAcesso implements ExtratoMapaLancamentoFuturoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	public ExtratoMapaLancamentoFuturo() {
		
	}

	public void validarDados(ExtratoMapaLancamentoFuturoVO obj) throws Exception {
		if (obj.getChequeVO().getCodigo().equals(0)) {
			throw new Exception("O campo CHEQUE deve ser informado.");
		}
		if (obj.getDataInicioApresentacao() == null) {
			throw new Exception("O campo DATA INÍCIO APRESENTAÇÃO deve ser informado.");
		}
		if (obj.getResponsavel().getCodigo().equals(0)) {
			throw new Exception("O campo RESPONSÁVEL deve ser informado.");
		}
	}
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ExtratoMapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception {
        try {
//            MapaLancamentoFuturo.incluir(getIdEntidade(), true, usuario);
            validarDados(obj);
            final String sql = "INSERT INTO ExtratoMapaLancamentoFuturo( cheque, dataInicioApresentacao, dataFimApresentacao, tipoMapaLancamentoFuturo, responsavel, contaCorrenteCheque) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    if (obj.getChequeVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getChequeVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicioApresentacao()));
                    if (obj.getDataFimApresentacao() != null) {
                        sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataFimApresentacao()));
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getTipoMapaLancamentoFuturo().name().toString());
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (obj.getContaCorrenteCheque().getCodigo().intValue() != 0) {
                    	sqlInserir.setInt(6, obj.getContaCorrenteCheque().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(6, 0);
                    }
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
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataFimApresentacaoExtratoPorCodigoChequeSituacao(final Integer cheque, final Date data, final String tipoMapaLancamentoFuturo, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE ExtratoMapaLancamentoFuturo set dataFimApresentacao=? WHERE cheque = ? and tipoMapaLancamentoFuturo = ? AND dataFimApresentacao IS NULL"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlAlterar.setDate(++i, Uteis.getDataJDBC(data));
                    sqlAlterar.setInt(++i, cheque.intValue());
                    sqlAlterar.setString(++i, tipoMapaLancamentoFuturo);
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataFimApresentacaoSituacaoExtratoPorCodigoChequeSituacao(final Integer cheque, final Date data, final String tipoMapaLancamentoFuturo, final Boolean chequeCompensado, UsuarioVO usuario) throws Exception {
    	try {
    		final String sql = "UPDATE ExtratoMapaLancamentoFuturo set dataFimApresentacao=?, chequeCompensado=? WHERE cheque = ? and tipoMapaLancamentoFuturo = ? AND dataFimApresentacao IS NULL"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
    			
    			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
    				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
    				int i = 0;
    				sqlAlterar.setDate(++i, Uteis.getDataJDBC(data));
    				sqlAlterar.setBoolean(++i, chequeCompensado);
    				sqlAlterar.setInt(++i, cheque.intValue());
    				sqlAlterar.setString(++i, tipoMapaLancamentoFuturo);
    				return sqlAlterar;
    			}
    		});
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorCodigoCheque(Integer cheque, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {
            MapaLancamentoFuturo.excluir(getIdEntidade(), verificarAcesso, usuario);
            String sql = "DELETE FROM ExtratoMapaLancamentoFuturo WHERE ( cheque = ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] { cheque });
        } catch (Exception e) {
            throw e;
        }
    }
    
    public ExtratoMapaLancamentoFuturoVO realizarCriacaoExtratoMapaLancamentoFuturo(ChequeVO chequeVO, Integer contaCorrente, TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo, UsuarioVO usuarioVO) {
    	ExtratoMapaLancamentoFuturoVO obj = new ExtratoMapaLancamentoFuturoVO();
    	obj.getChequeVO().setCodigo(chequeVO.getCodigo());
    	obj.getContaCorrenteCheque().setCodigo(contaCorrente);
    	obj.setDataInicioApresentacao(new Date());
    	obj.setDataFimApresentacao(null);
    	obj.setTipoMapaLancamentoFuturo(tipoMapaLancamentoFuturo);
    	obj.getResponsavel().setCodigo(usuarioVO.getCodigo());
    	return obj;
    }

    public void realizarCriacaoInclusaoExtratoMapaLancamentoFuturo(ChequeVO chequeVO, Date data, Integer contaCorrente, TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo, UsuarioVO usuarioVO) throws Exception {
    	ExtratoMapaLancamentoFuturoVO obj = new ExtratoMapaLancamentoFuturoVO();
    	obj.getChequeVO().setCodigo(chequeVO.getCodigo());
    	obj.getContaCorrenteCheque().setCodigo(contaCorrente);
    	obj.setDataInicioApresentacao(data);
    	obj.setDataFimApresentacao(null);
    	obj.setTipoMapaLancamentoFuturo(tipoMapaLancamentoFuturo);
    	obj.getResponsavel().setCodigo(usuarioVO.getCodigo());
    	incluir(obj, usuarioVO);
    }
    
    public void realizarCriacaoInclusaoExtratoMapaLancamentoFuturoDataReapresentacao(ChequeVO chequeVO, Date dataReapresentacao, Integer contaCorrente, TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo, UsuarioVO usuarioVO) throws Exception {
    	ExtratoMapaLancamentoFuturoVO obj = new ExtratoMapaLancamentoFuturoVO();
    	obj.getChequeVO().setCodigo(chequeVO.getCodigo());
    	obj.getContaCorrenteCheque().setCodigo(contaCorrente);
    	obj.setDataInicioApresentacao(dataReapresentacao);
    	obj.setDataFimApresentacao(null);
    	obj.setTipoMapaLancamentoFuturo(tipoMapaLancamentoFuturo);
    	obj.getResponsavel().setCodigo(usuarioVO.getCodigo());
    	incluir(obj, usuarioVO);
    }
    
}
