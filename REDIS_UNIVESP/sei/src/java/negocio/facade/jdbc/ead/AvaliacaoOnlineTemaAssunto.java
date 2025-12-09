package negocio.facade.jdbc.ead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AvaliacaoOnlineTemaAssuntoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoOnlineTemaAssunto extends ControleAcesso implements AvaliacaoOnlineTemaAssuntoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 110706922244270261L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AvaliacaoOnlineVO avaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		deletar(avaliacaoOnlineVO, usuarioVO);
		for(AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO: avaliacaoOnlineVO.getAvaliacaoOnlineTemaAssuntoVOs()) {
			if(avaliacaoOnlineTemaAssuntoVO.getSelecionado()) {
				avaliacaoOnlineTemaAssuntoVO.setAvaliacaoOnlineVO(avaliacaoOnlineVO);
				if(Uteis.isAtributoPreenchido(avaliacaoOnlineTemaAssuntoVO.getCodigo())) {
					alterar(avaliacaoOnlineTemaAssuntoVO, usuarioVO);
				}else {
					incluir(avaliacaoOnlineTemaAssuntoVO, usuarioVO);
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO, UsuarioVO usuarioVO) throws Exception {
		if(alterar(avaliacaoOnlineTemaAssuntoVO, "avaliacaoonlinetemaassunto", new AtributoPersistencia().add("avaliacaoonline", avaliacaoOnlineTemaAssuntoVO.getAvaliacaoOnlineVO()).add("temaAssunto", avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO()),
				new AtributoPersistencia().add("avaliacaoonline", avaliacaoOnlineTemaAssuntoVO.getAvaliacaoOnlineVO()).add("temaAssunto", avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO()), usuarioVO) != 1) {
			incluir(avaliacaoOnlineTemaAssuntoVO, usuarioVO);
		};
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO, UsuarioVO usuarioVO) throws Exception {
		incluir(avaliacaoOnlineTemaAssuntoVO, "avaliacaoonlinetemaassunto", new AtributoPersistencia().add("avaliacaoonline", avaliacaoOnlineTemaAssuntoVO.getAvaliacaoOnlineVO()).add("temaAssunto", avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO()), usuarioVO); 		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void deletar(AvaliacaoOnlineVO avaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("delete from avaliacaoonlinetemaassunto where avaliacaoonline = ?  and codigo not in (0 ");
		avaliacaoOnlineVO.getAvaliacaoOnlineTemaAssuntoVOs().forEach( t -> {
			if(Uteis.isAtributoPreenchido(t) && t.getSelecionado()) {				
				sql.append(",").append(t.getCodigo());
			}
		});
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), avaliacaoOnlineVO.getCodigo());
	}

	@Override
	public List<AvaliacaoOnlineTemaAssuntoVO> consultarAvaliacaoOnlineTemaAssuntoPorAvaliacaoOnline(Integer codigoAvaliacaoOnline, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("select avaliacaoonlinetemaassunto.codigo, avaliacaoonlinetemaassunto.avaliacaoonline, temaassunto.codigo as temaassunto, temaassunto.nome, temaassunto.abreviatura from avaliacaoonlinetemaassunto ");
		sql.append(" inner join temaassunto on temaassunto.codigo = avaliacaoonlinetemaassunto.temaassunto ");
		sql.append(" where avaliacaoonlinetemaassunto.avaliacaoonline = ?  order by temaassunto.nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoAvaliacaoOnline));
	}
	
	private AvaliacaoOnlineTemaAssuntoVO montarDados(SqlRowSet rs){
		AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO =  new AvaliacaoOnlineTemaAssuntoVO();
		avaliacaoOnlineTemaAssuntoVO.setCodigo(rs.getInt("codigo"));
		avaliacaoOnlineTemaAssuntoVO.setSelecionado(rs.getInt("codigo") != 0);
		avaliacaoOnlineTemaAssuntoVO.setNovoObj(rs.getInt("codigo") == 0);
		avaliacaoOnlineTemaAssuntoVO.getAvaliacaoOnlineVO().setCodigo(rs.getInt("avaliacaoOnline"));
		avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().setCodigo(rs.getInt("temaassunto"));
		avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().setNome(rs.getString("nome"));
		avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().setAbreviatura(rs.getString("abreviatura"));
		return avaliacaoOnlineTemaAssuntoVO;
	}
	
	private List<AvaliacaoOnlineTemaAssuntoVO> montarDadosConsulta(SqlRowSet rs){
		List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs = new ArrayList<AvaliacaoOnlineTemaAssuntoVO>(0);
		while(rs.next()) {
			avaliacaoOnlineTemaAssuntoVOs.add(montarDados(rs));
		}		
		return avaliacaoOnlineTemaAssuntoVOs;
	}

	@Override
	public List<AvaliacaoOnlineTemaAssuntoVO> consultarAvaliacaoOnlineTemaAssuntoAptoParaAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UsuarioVO usuarioVO) throws Exception {
		
		StringBuilder sql  = new StringBuilder("select avaliacaoonlinetemaassunto.codigo as codigo, ");
		sql.append(avaliacaoOnlineVO.getCodigo()).append(" as avaliacaoonline, temaassunto.codigo as temaassunto, temaassunto.nome, temaassunto.abreviatura ");
		sql.append(" from temaassunto ");
		sql.append(" left join avaliacaoonlinetemaassunto on avaliacaoonlinetemaassunto.temaassunto = temaassunto.codigo and avaliacaoonlinetemaassunto.avaliacaoonline =  ").append(avaliacaoOnlineVO.getCodigo());
		sql.append(" where (exists "); 
		if(Uteis.isAtributoPreenchido(conteudoVO)) {
			sql.append(" (select unidadeconteudo.codigo from unidadeconteudo where unidadeconteudo.conteudo = ").append(conteudoVO.getCodigo()).append(" and unidadeconteudo.temaassunto = temaassunto.codigo ) ");
		}else {
			sql.append(" (select temaassuntodisciplina.codigo from temaassuntodisciplina where temaassuntodisciplina.disciplina = ").append(disciplinaVO.getCodigo()).append(" and temaassuntodisciplina.temaassunto = temaassunto.codigo) ");
		}
		sql.append(" or avaliacaoonlinetemaassunto.codigo is not null) ");
		sql.append(" order by nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

}
