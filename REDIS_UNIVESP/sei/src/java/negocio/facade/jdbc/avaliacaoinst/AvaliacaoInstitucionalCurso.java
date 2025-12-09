package negocio.facade.jdbc.avaliacaoinst;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalCursoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalCursoInterfaceFacade;

@Repository
@Lazy
public class AvaliacaoInstitucionalCurso extends ControleAcesso implements AvaliacaoInstitucionalCursoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -647213603494841210L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
			UsuarioVO usuarioVO) throws Exception {
		
		for(AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVO: avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs()) {
			avaliacaoInstitucionalCursoVO.setAvaliacaoInstitucionalVO(avaliacaoInstitucionalVO);
			incluir(avaliacaoInstitucionalCursoVO, usuarioVO);
		}

	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVO, UsuarioVO usuarioVO) throws Exception {		
		incluir(avaliacaoInstitucionalCursoVO, "avaliacaoInstitucionalCurso", new AtributoPersistencia()
				.add("curso", avaliacaoInstitucionalCursoVO.getCursoVO())
				.add("avaliacaoInstitucional", avaliacaoInstitucionalCursoVO.getAvaliacaoInstitucionalVO()), usuarioVO);

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVO, UsuarioVO usuarioVO) throws Exception {		
		alterar(avaliacaoInstitucionalCursoVO, "avaliacaoInstitucionalCurso", new AtributoPersistencia()
				.add("curso", avaliacaoInstitucionalCursoVO.getCursoVO())
				.add("avaliacaoInstitucional", avaliacaoInstitucionalCursoVO.getAvaliacaoInstitucionalVO()), new AtributoPersistencia().add("codigo", avaliacaoInstitucionalCursoVO.getCodigo()), usuarioVO);
	}

	@Override
	public void alterarAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
			UsuarioVO usuarioVO) throws Exception {
		excluirAvaliacaoInstitucionalCurso(avaliacaoInstitucionalVO, usuarioVO);
		for(AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVO: avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs()) {
			avaliacaoInstitucionalCursoVO.setAvaliacaoInstitucionalVO(avaliacaoInstitucionalVO);
			alterar(avaliacaoInstitucionalCursoVO, usuarioVO);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
			UsuarioVO usuarioVO) throws Exception {
		excluirListaSubordinada(avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs(), "avaliacaoInstitucionalCurso", 
				new AtributoPersistencia().add("avaliacaoInstitucional", avaliacaoInstitucionalVO.getCodigo()), usuarioVO);

	}

	@Override
	public List<AvaliacaoInstitucionalCursoVO> consultarPorAvaliacaoInstitucional(Integer avaliacaoInstitucional,
			UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("select avaliacaoInstitucionalCurso.*, curso.nome as curso_nome from avaliacaoInstitucionalCurso inner join curso on curso.codigo = avaliacaoInstitucionalCurso.curso where avaliacaoInstitucionalCurso.avaliacaoInstitucional = ? order by curso_nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), avaliacaoInstitucional));
	}
	
	
	public List<AvaliacaoInstitucionalCursoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<AvaliacaoInstitucionalCursoVO> avaliacaoInstitucionalCursoVOs =  new ArrayList<AvaliacaoInstitucionalCursoVO>();
		while(rs.next()) {
			avaliacaoInstitucionalCursoVOs.add(montarDados(rs));
		}
		return avaliacaoInstitucionalCursoVOs;
	}
	public AvaliacaoInstitucionalCursoVO montarDados(SqlRowSet rs) throws Exception {
		AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVO =  new AvaliacaoInstitucionalCursoVO();
		avaliacaoInstitucionalCursoVO.setNovoObj(false);
		avaliacaoInstitucionalCursoVO.setCodigo(rs.getInt("codigo"));
		avaliacaoInstitucionalCursoVO.getCursoVO().setCodigo(rs.getInt("curso"));
		avaliacaoInstitucionalCursoVO.getCursoVO().setNome(rs.getString("curso_nome"));
		avaliacaoInstitucionalCursoVO.getAvaliacaoInstitucionalVO().setCodigo(rs.getInt("avaliacaoInstitucional"));
		return avaliacaoInstitucionalCursoVO;
	}

}
