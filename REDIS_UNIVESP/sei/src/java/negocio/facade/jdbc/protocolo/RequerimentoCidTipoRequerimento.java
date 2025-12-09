package negocio.facade.jdbc.protocolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoCidTipoRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.RequerimentoCidTipoRequerimentoIntefaceFacade;
/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>RequerimentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>RequerimentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RequerimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy

public class RequerimentoCidTipoRequerimento extends ControleAcesso implements RequerimentoCidTipoRequerimentoIntefaceFacade   {
	private static final long serialVersionUID = 7288447026204415838L;
	protected static String idEntidade;
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirRequerimentoCidTipoRequerimentoVOs(RequerimentoVO requerimento,  UsuarioVO usuario) throws Exception {
//		Iterator<CidTipoRequerimentoVO> e = requerimento.getRequerimentoCidTipoRequerimentoVO().getCidTipoRequerimentoVOs().iterator();
//		while (e.hasNext()) {
//			CidTipoRequerimentoVO obj = (CidTipoRequerimentoVO) e.next();
//			obj.setCodigo(requerimento.getRequerimentoCidTipoRequerimentoVO().getCidtiporequerimento());
//			incluir(requerimento.getRequerimentoCidTipoRequerimentoVO(), requerimento, obj, usuario);
//		}
		for(CidTipoRequerimentoVO cidTipoRequerimentoVO : requerimento.getRequerimentoCidTipoRequerimentoVO().getCidTipoRequerimentoVOs()) {
			incluir(requerimento.getRequerimentoCidTipoRequerimentoVO(), requerimento, cidTipoRequerimentoVO, usuario);
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RequerimentoCidTipoRequerimentoVO obj, RequerimentoVO requerimento, CidTipoRequerimentoVO cidTipoRequerimentoVO, UsuarioVO usuario) throws Exception {
		final String sql = "INSERT INTO Requerimentocidtiporequerimento( " + " cidtiporequerimento, requerimento, tiporequerimento ) " + "VALUES ( ?, ?, ?) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				int i = 1;
				sqlInserir.setInt(i++, cidTipoRequerimentoVO.getCodigo().intValue());
				sqlInserir.setInt(i++, requerimento.getCodigo().intValue());
				sqlInserir.setInt(i++, requerimento.getTipoRequerimento().getCodigo().intValue());
				return sqlInserir;
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
		obj.setNovoObj(Boolean.FALSE);
	}
	}
