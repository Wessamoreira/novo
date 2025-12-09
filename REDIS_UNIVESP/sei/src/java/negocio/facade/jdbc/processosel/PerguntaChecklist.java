package negocio.facade.jdbc.processosel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.PerguntaChecklistInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy 
public class PerguntaChecklist extends ControleAcesso implements PerguntaChecklistInterfaceFacade  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5248480888046216182L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<PerguntaChecklistVO> lista, UsuarioVO usuarioVO) {
		try {
			for (PerguntaChecklistVO obj : lista) {
				if (obj.getCodigo() == 0) {
					incluir(obj, usuarioVO);
				} else {
					alterar(obj, usuarioVO);
				}	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PerguntaChecklistVO obj,  UsuarioVO usuario) {
		try {			
			incluir(obj, "perguntaChecklist", new AtributoPersistencia()
					.add("pergunta", obj.getPerguntaVO())
					.add("descricao", obj.getDescricao())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PerguntaChecklistVO obj, UsuarioVO usuario) {
		try {			
			alterar(obj, "perguntaChecklist", new AtributoPersistencia()
					.add("pergunta", obj.getPerguntaVO())
					.add("descricao", obj.getDescricao())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsulta() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" perguntaChecklist.codigo as \"perguntaChecklist.codigo\",  ");
		sql.append(" perguntaChecklist.descricao as \"perguntaChecklist.descricao\",  ");
		sql.append(" perguntaChecklist.statusAtivoInativoEnum as \"perguntaChecklist.statusAtivoInativoEnum\"  ");
		sql.append(" FROM perguntaChecklist ");
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PerguntaChecklistVO> consultarPerguntaChecklistPorPerguntaVO(PerguntaVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where perguntaChecklist.pergunta = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			List<PerguntaChecklistVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				PerguntaChecklistVO gpi = new PerguntaChecklistVO();
				montarDados(gpi, tabelaResultado, nivelMontarDados, usuario);
				gpi.setPerguntaVO(obj);
				vetResultado.add(gpi);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PerguntaChecklistVO> montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<PerguntaChecklistVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			PerguntaChecklistVO obj = new PerguntaChecklistVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDados(PerguntaChecklistVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("perguntaChecklist.codigo"));
		obj.setDescricao(dadosSQL.getString("perguntaChecklist.descricao"));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("perguntaChecklist.statusAtivoInativoEnum")));
		
	}

}
