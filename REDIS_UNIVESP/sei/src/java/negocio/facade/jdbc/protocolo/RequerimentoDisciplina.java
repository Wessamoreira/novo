package negocio.facade.jdbc.protocolo;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoDisciplinaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.RequerimentoDisciplinaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RequerimentoDisciplina extends ControleAcesso implements RequerimentoDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8213624481606928122L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {		
		validarSeRegistroForamExcluidoDasListaSubordinadas(requerimentoVO.getRequerimentoDisciplinaVOs(), "requerimentoDisciplina", "requerimento", requerimentoVO.getCodigo(), usuarioVO);
		for(RequerimentoDisciplinaVO requerimentoDisciplinaVO: requerimentoVO.getRequerimentoDisciplinaVOs()) {
			requerimentoDisciplinaVO.setRequerimento(requerimentoVO);
			if(requerimentoDisciplinaVO.isNovoObj()) {
				incluir(requerimentoDisciplinaVO, "requerimentoDisciplina", 
						new AtributoPersistencia().add("requerimento", requerimentoVO.getCodigo())
						.add("disciplina", requerimentoDisciplinaVO.getDisciplina().getCodigo())
						.add("situacao", requerimentoDisciplinaVO.getSituacao())
						.add("variavelNota", requerimentoDisciplinaVO.getVariavelNota())
						.add("tituloNota", requerimentoDisciplinaVO.getTituloNota())
						.add("dataDeferimentoIndeferimento", Uteis.getDataJDBCTimestamp(requerimentoDisciplinaVO.getDataDeferimentoIndeferimento()))
						.add("usuarioDeferimentoIndeferimento", requerimentoDisciplinaVO.getUsuarioDeferimentoIndeferimento())
						.add("motivoIndeferimento", requerimentoDisciplinaVO.getMotivoIndeferimento()), usuarioVO);
			}else {
				alterar(requerimentoDisciplinaVO, "requerimentoDisciplina", 
						new AtributoPersistencia().add("requerimento", requerimentoVO.getCodigo())
						.add("disciplina", requerimentoDisciplinaVO.getDisciplina().getCodigo())
						.add("situacao", requerimentoDisciplinaVO.getSituacao())
						.add("variavelNota", requerimentoDisciplinaVO.getVariavelNota())
						.add("tituloNota", requerimentoDisciplinaVO.getTituloNota())
						.add("dataDeferimentoIndeferimento", Uteis.getDataJDBCTimestamp(requerimentoDisciplinaVO.getDataDeferimentoIndeferimento()))
						.add("usuarioDeferimentoIndeferimento", requerimentoDisciplinaVO.getUsuarioDeferimentoIndeferimento())
						.add("motivoIndeferimento", requerimentoDisciplinaVO.getMotivoIndeferimento()), new AtributoPersistencia().add("codigo", requerimentoDisciplinaVO.getCodigo()), usuarioVO);
			}
		}

	}

	@Override
	public void consultarPorRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO)
			throws Exception {
		StringBuilder sql = new StringBuilder("select requerimentoDisciplina.codigo, requerimentoDisciplina.tituloNota, requerimentoDisciplina.requerimento, requerimentoDisciplina.disciplina, requerimentoDisciplina.variavelNota, requerimentoDisciplina.situacao, ");
		sql.append(" requerimentoDisciplina.dataDeferimentoIndeferimento, requerimentoDisciplina.usuarioDeferimentoIndeferimento, usuarioDeferimentoIndeferimento.nome as usuarioDeferimentoIndeferimento_nome, requerimentoDisciplina.motivoIndeferimento, ");
		sql.append(" disciplina.nome as disciplina_nome, disciplina.abreviatura as disciplina_abreviatura  ");
		sql.append(" from requerimentoDisciplina inner join disciplina on disciplina.codigo = requerimentoDisciplina.disciplina ");
		sql.append(" left join usuario as usuarioDeferimentoIndeferimento on usuarioDeferimentoIndeferimento.codigo = requerimentoDisciplina.usuarioDeferimentoIndeferimento ");
		sql.append(" where requerimentoDisciplina.requerimento = ? order by disciplina.nome ");
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), requerimentoVO.getCodigo());
		requerimentoVO.getRequerimentoDisciplinaVOs().clear();
		while(rs.next()){
			RequerimentoDisciplinaVO requerimentoDisciplinaVO = new RequerimentoDisciplinaVO();
			requerimentoDisciplinaVO.setNovoObj(false);
			requerimentoDisciplinaVO.setCodigo(rs.getInt("codigo"));
			requerimentoDisciplinaVO.getRequerimento().setCodigo(rs.getInt("requerimento"));
			requerimentoDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
			requerimentoDisciplinaVO.getDisciplina().setNome(rs.getString("disciplina_nome"));
			requerimentoDisciplinaVO.getDisciplina().setAbreviatura(rs.getString("disciplina_abreviatura"));
			requerimentoDisciplinaVO.setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum.valueOf(rs.getString("situacao")));
			requerimentoDisciplinaVO.setVariavelNota(rs.getString("variavelNota"));
			requerimentoDisciplinaVO.setTituloNota(rs.getString("tituloNota"));
			requerimentoDisciplinaVO.setDataDeferimentoIndeferimento(rs.getTimestamp("dataDeferimentoIndeferimento"));
			requerimentoDisciplinaVO.getUsuarioDeferimentoIndeferimento().setCodigo(rs.getInt("usuarioDeferimentoIndeferimento"));
			requerimentoDisciplinaVO.getUsuarioDeferimentoIndeferimento().setNome(rs.getString("usuarioDeferimentoIndeferimento_nome"));
			requerimentoDisciplinaVO.setMotivoIndeferimento(rs.getString("motivoIndeferimento"));
			requerimentoVO.getRequerimentoDisciplinaVOs().add(requerimentoDisciplinaVO);
		}
	
	}

}
