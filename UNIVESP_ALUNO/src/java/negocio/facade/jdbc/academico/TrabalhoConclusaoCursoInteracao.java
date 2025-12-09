package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.TrabalhoConclusaoCursoInteracaoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TrabalhoConclusaoCursoInteracaoInterfaceFacade;

@Repository
@Lazy
@Scope
public class TrabalhoConclusaoCursoInteracao extends ControleAcesso implements TrabalhoConclusaoCursoInteracaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6016229638399737638L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO, UsuarioVO usuarioVO) throws Exception {

		validarDados(trabalhoConclusaoCursoInteracaoVO);
		trabalhoConclusaoCursoInteracaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO TrabalhoConclusaoCursoInteracao ( ");
				sql.append(" dataInteracao, interacao, responsavelInteracao, etapaTCC, trabalhoConclusaoCurso ");
				sql.append(" ) values (?,?,?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoInteracaoVO.getDataInteracao()));
				ps.setString(x++, trabalhoConclusaoCursoInteracaoVO.getInteracao());
				ps.setInt(x++, trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().getCodigo());
				ps.setString(x++, trabalhoConclusaoCursoInteracaoVO.getEtapaTCC().name());
//				ps.setInt(x++, trabalhoConclusaoCursoInteracaoVO.getTrabalhoConclusaoCurso().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		trabalhoConclusaoCursoInteracaoVO.setNovoObj(false);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM TrabalhoConclusaoCursoInteracao WHERE codigo = ").append(trabalhoConclusaoCursoInteracaoVO.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());

	}

	@Override
	public void validarDados(TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO) throws ConsistirException {
		if (trabalhoConclusaoCursoInteracaoVO.getInteracao().trim().isEmpty() || trabalhoConclusaoCursoInteracaoVO.getInteracao().trim().equals("Envie uma nova interação")) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCursoInteracao_interacao"));
		}

	}

	private String getSelectCompleto() {
		StringBuilder sb = new StringBuilder("SELECT TrabalhoConclusaoCursoInteracao.*, ");
		sb.append(" usuario.nome AS \"responsavelInteracao.nome\", ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\" ");
		sb.append(" FROM TrabalhoConclusaoCursoInteracao ");
		sb.append(" inner join Usuario  as usuario on TrabalhoConclusaoCursoInteracao.responsavelInteracao = usuario.codigo ");
		sb.append(" left join Pessoa  on pessoa.codigo = usuario.pessoa ");
		sb.append(" left join Arquivo on Arquivo.codigo = pessoa.arquivoImagem ");
		return sb.toString();
	}

	@Override
	public List<TrabalhoConclusaoCursoInteracaoVO> consultarPorTCCEtapa(int tcc, EtapaTCCEnum etapaTCCEnum, Integer limite, Integer offset) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append(" where trabalhoConclusaoCurso = ").append(tcc);
		if (etapaTCCEnum != null) {
			sql.append(" and etapaTCC = '").append(etapaTCCEnum).append("' ");
		}
		sql.append(" order by dataInteracao desc ");
		if (limite != null && limite > 0) {
			sql.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<TrabalhoConclusaoCursoInteracaoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<TrabalhoConclusaoCursoInteracaoVO> trabalhoConclusaoCursoInteracaoVOs = new ArrayList<TrabalhoConclusaoCursoInteracaoVO>(0);
		while (rs.next()) {
			trabalhoConclusaoCursoInteracaoVOs.add(montarDados(rs));
		}
		return trabalhoConclusaoCursoInteracaoVOs;
	}

	private TrabalhoConclusaoCursoInteracaoVO montarDados(SqlRowSet rs) throws Exception {
		TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO = new TrabalhoConclusaoCursoInteracaoVO();
		trabalhoConclusaoCursoInteracaoVO.setNovoObj(false);
		trabalhoConclusaoCursoInteracaoVO.setCodigo(rs.getInt("codigo"));
		trabalhoConclusaoCursoInteracaoVO.setInteracao(rs.getString("interacao"));
		trabalhoConclusaoCursoInteracaoVO.setDataInteracao(rs.getDate("dataInteracao"));
		trabalhoConclusaoCursoInteracaoVO.setEtapaTCC(EtapaTCCEnum.valueOf(rs.getString("etapaTCC")));
//		trabalhoConclusaoCursoInteracaoVO.getTrabalhoConclusaoCurso().setCodigo(rs.getInt("trabalhoConclusaoCurso"));
		trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().setCodigo(rs.getInt("responsavelInteracao"));
		trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().setNome(rs.getString("responsavelInteracao.nome"));
		trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().getPessoa().setNome(rs.getString("pessoa.nome"));
		trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().getPessoa().setCodigo(rs.getInt("pessoa.codigo"));
		if (rs.getInt("arquivo.codigo") > 0) {
			trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().getPessoa().getArquivoImagem().setCodigo(rs.getInt("arquivo.codigo"));
			trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().getPessoa().getArquivoImagem().setPastaBaseArquivo(rs.getString("arquivo.pastaBaseArquivo").replaceAll("\\\\", "/"));
			trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().getPessoa().getArquivoImagem().setNome(rs.getString("arquivo.nome"));
		}
		return trabalhoConclusaoCursoInteracaoVO;
	}

	@Override
	public Integer consultarTotalRegistroPorTCCEtapa(int tcc, EtapaTCCEnum etapaTCCEnum) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(codigo) qtde from TrabalhoConclusaoCursoInteracao ");
		sql.append(" where trabalhoConclusaoCurso = ").append(tcc);
		if (etapaTCCEnum != null) {
			sql.append(" and etapaTCC = '").append(etapaTCCEnum).append("' ");
		}
		
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

}
