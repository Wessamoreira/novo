package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PerguntaChecklistOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PerguntaChecklistOrigemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy 
public class PerguntaChecklistOrigem extends ControleAcesso implements PerguntaChecklistOrigemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6420733327641594059L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<PerguntaChecklistOrigemVO> lista, UsuarioVO usuarioVO) {
		try {
			for (PerguntaChecklistOrigemVO obj : lista) {
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
	public void incluir(final PerguntaChecklistOrigemVO obj, UsuarioVO usuario) {
		try {
			incluir(obj, "PerguntaChecklistOrigem", new AtributoPersistencia()
//					.add("perguntaChecklist", obj.getPerguntaChecklistVO())
//					.add("perguntaRespostaOrigem", obj.getPerguntaRespostaOrigemVO())
					.add("checklist", obj.isChecklist())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PerguntaChecklistOrigemVO obj, UsuarioVO usuario) {
		try {
			alterar(obj, "PerguntaChecklistOrigem", new AtributoPersistencia()
//					.add("perguntaChecklist", obj.getPerguntaChecklistVO())
					.add("perguntaRespostaOrigem", obj.getPerguntaRespostaOrigemVO())
					.add("checklist", obj.isChecklist())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public List<PerguntaChecklistOrigemVO> consultarPorPerguntaRespostaOrigem(Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder str = new StringBuilder();
		str.append(" select  ");
		str.append(" PerguntaChecklistOrigem.codigo as \"PerguntaChecklistOrigem.codigo\",  ");
		str.append(" PerguntaChecklistOrigem.checklist as \"PerguntaChecklistOrigem.checklist\",  ");
		str.append(" PerguntaChecklistOrigem.perguntaRespostaOrigem as \"PerguntaChecklistOrigem.perguntaRespostaOrigem\",  ");
		str.append(" perguntaChecklist.codigo as \"perguntaChecklist.codigo\",  ");
		str.append(" perguntaChecklist.descricao as \"perguntaChecklist.descricao\"  ");
		str.append(" from PerguntaChecklistOrigem ");
		str.append(" inner join perguntaChecklist on perguntaChecklist.codigo = perguntaChecklistOrigem.perguntaChecklist ");
		str.append(" WHERE PerguntaChecklistOrigem.perguntaRespostaOrigem = ").append(codPerguntaRespostaOrigem);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<PerguntaChecklistOrigemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PerguntaChecklistOrigemVO> vetResultado = new ArrayList<PerguntaChecklistOrigemVO>(0);
		while (tabelaResultado.next()) {
			PerguntaChecklistOrigemVO obj = new PerguntaChecklistOrigemVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDados(PerguntaChecklistOrigemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("PerguntaChecklistOrigem.codigo")));
		obj.setChecklist(dadosSQL.getBoolean("PerguntaChecklistOrigem.checklist"));
		obj.getPerguntaRespostaOrigemVO().setCodigo(dadosSQL.getInt("PerguntaChecklistOrigem.perguntarespostaorigem"));
//		obj.getPerguntaChecklistVO().setCodigo(dadosSQL.getInt("perguntaChecklist.codigo"));
//		obj.getPerguntaChecklistVO().setDescricao(dadosSQL.getString("perguntaChecklist.descricao"));
		
								
	}	

}
