package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
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

import negocio.comuns.academico.TrabalhoConclusaoCursoArquivoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.TipoArquivoTCCEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TrabalhoConclusaoCursoArquivoInterfaceFacade;

@Repository
@Lazy
@Scope
public class TrabalhoConclusaoCursoArquivo extends ControleAcesso implements TrabalhoConclusaoCursoArquivoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9095251337528131931L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final TrabalhoConclusaoCursoArquivoVO trabalhoConclusaoCursoArquivoVO) throws Exception {
		trabalhoConclusaoCursoArquivoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO TrabalhoConclusaoCursoArquivo ( ");
				sql.append(" dataPostagem, enviadoPeloAluno, responsavel, tipoArquivoTCC, etapaTCC, nomeArquivo, nomeArquivoOriginal,  trabalhoConclusaoCurso ");
				sql.append(" ) values (?,?,?,?,?, ?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoArquivoVO.getDataPostagem()));
				ps.setBoolean(x++, trabalhoConclusaoCursoArquivoVO.getEnviadoPeloAluno());				
				ps.setInt(x++, trabalhoConclusaoCursoArquivoVO.getResponsavel().getCodigo());
				ps.setString(x++, trabalhoConclusaoCursoArquivoVO.getTipoArquivoTCC().name());
				ps.setString(x++, trabalhoConclusaoCursoArquivoVO.getEtapaTCC().name());
				ps.setString(x++, trabalhoConclusaoCursoArquivoVO.getNomeArquivo());
				ps.setString(x++, trabalhoConclusaoCursoArquivoVO.getNomeArquivoOriginal());								
				ps.setInt(x++, trabalhoConclusaoCursoArquivoVO.getTrabalhoConclusaoCurso().getCodigo());
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
		trabalhoConclusaoCursoArquivoVO.setNovoObj(false);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarPostagemArquivo(FileUploadEvent uploadEvent, TipoArquivoTCCEnum tipoArquivoTCCEnum, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		TrabalhoConclusaoCursoArquivoVO trabalhoConclusaoCursoArquivoVO = new TrabalhoConclusaoCursoArquivoVO();
		trabalhoConclusaoCursoArquivoVO.setDataPostagem(new Date());
		trabalhoConclusaoCursoArquivoVO.setEnviadoPeloAluno(usuarioVO.getIsApresentarVisaoAluno());
		trabalhoConclusaoCursoArquivoVO.setEtapaTCC(trabalhoConclusaoCursoVO.getEtapaTCC());
		trabalhoConclusaoCursoArquivoVO.setTipoArquivoTCC(tipoArquivoTCCEnum);
		trabalhoConclusaoCursoArquivoVO.getTrabalhoConclusaoCurso().setCodigo(trabalhoConclusaoCursoVO.getCodigo());
		trabalhoConclusaoCursoArquivoVO.getResponsavel().setCodigo(usuarioVO.getCodigo());
		trabalhoConclusaoCursoArquivoVO.getResponsavel().getPessoa().setNome(usuarioVO.getPessoa().getNome());
		if (trabalhoConclusaoCursoArquivoVO.getResponsavel().getPessoa().getNome().trim().isEmpty()) {
			trabalhoConclusaoCursoArquivoVO.getResponsavel().getPessoa().setNome(usuarioVO.getNome());
		}
		trabalhoConclusaoCursoArquivoVO.setNomeArquivoOriginal(uploadEvent.getUploadedFile().getName());
		trabalhoConclusaoCursoArquivoVO.setNomeArquivo(usuarioVO.getCodigo()+"_"+new Date().getTime()+uploadEvent.getUploadedFile().getName().substring(
				uploadEvent.getUploadedFile().getName().lastIndexOf("."),
				uploadEvent.getUploadedFile().getName().length()));
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, trabalhoConclusaoCursoArquivoVO.getNomeArquivo(), PastaBaseArquivoEnum.TCC.getValue()+"/"+trabalhoConclusaoCursoVO.getCodigo(), configuracaoGeralSistemaVO, usuarioVO);
		
		incluir(trabalhoConclusaoCursoArquivoVO);		
	}

	private String getSelectCompleto(){
		StringBuilder sql  = new StringBuilder("SELECT TrabalhoConclusaoCursoArquivo.*, responsavel.nome as \"responsavel.nome\", pessoa.nome as \"pessoa.nome\" ");
		sql.append(" from TrabalhoConclusaoCursoArquivo ");
		sql.append(" inner join Usuario responsavel on  responsavel.codigo = TrabalhoConclusaoCursoArquivo.responsavel ");
		sql.append(" left join pessoa as pessoa on responsavel.pessoa =  pessoa.codigo ");
		return sql.toString();
	}
	
	@Override
	public List<TrabalhoConclusaoCursoArquivoVO> consultarPorTCCEtapa(Integer tcc, EtapaTCCEnum etapaTCCEnum, TipoArquivoTCCEnum tipoArquivoTCCEnum, Integer limite, Integer offset) throws Exception {
		StringBuilder sql  = new StringBuilder(getSelectCompleto());
		sql.append(" where trabalhoConclusaoCurso = ").append(tcc);
		if(etapaTCCEnum != null){
			sql.append(" and etapaTCC = '").append(etapaTCCEnum.name()).append("' ");
		}
		if(tipoArquivoTCCEnum != null){
			sql.append(" and tipoArquivoTCC = '").append(tipoArquivoTCCEnum.name()).append("' ");
		}
		sql.append(" order by dataPostagem desc ");
		if(limite != null && limite > 0){
			sql.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<TrabalhoConclusaoCursoArquivoVO> montarDadosConsulta(SqlRowSet rs){
		List<TrabalhoConclusaoCursoArquivoVO> trabalhoConclusaoCursoArquivoVOs = new ArrayList<TrabalhoConclusaoCursoArquivoVO>(0);
		while(rs.next()){
			trabalhoConclusaoCursoArquivoVOs.add(montarDados(rs));
		}		
		return trabalhoConclusaoCursoArquivoVOs;
	}
	private TrabalhoConclusaoCursoArquivoVO montarDados(SqlRowSet rs){
		TrabalhoConclusaoCursoArquivoVO trabalhoConclusaoCursoArquivoVO = new TrabalhoConclusaoCursoArquivoVO();
		trabalhoConclusaoCursoArquivoVO.setNovoObj(false);
		trabalhoConclusaoCursoArquivoVO.setCodigo(rs.getInt("codigo"));
		trabalhoConclusaoCursoArquivoVO.setDataPostagem(rs.getDate("dataPostagem"));
		trabalhoConclusaoCursoArquivoVO.setEnviadoPeloAluno(rs.getBoolean("enviadoPeloAluno"));
		trabalhoConclusaoCursoArquivoVO.setNomeArquivo(rs.getString("nomeArquivo"));
		trabalhoConclusaoCursoArquivoVO.setNomeArquivoOriginal(rs.getString("nomeArquivoOriginal"));
		trabalhoConclusaoCursoArquivoVO.setEtapaTCC(EtapaTCCEnum.valueOf(rs.getString("etapaTCC")));
		trabalhoConclusaoCursoArquivoVO.setTipoArquivoTCC(TipoArquivoTCCEnum.valueOf(rs.getString("tipoArquivoTCC")));
		trabalhoConclusaoCursoArquivoVO.getTrabalhoConclusaoCurso().setCodigo(rs.getInt("trabalhoConclusaoCurso"));
		trabalhoConclusaoCursoArquivoVO.getResponsavel().setCodigo(rs.getInt("responsavel"));
		trabalhoConclusaoCursoArquivoVO.getResponsavel().getPessoa().setNome(rs.getString("pessoa.nome"));
		if (trabalhoConclusaoCursoArquivoVO.getResponsavel().getPessoa().getNome().trim().isEmpty()) {
			trabalhoConclusaoCursoArquivoVO.getResponsavel().getPessoa().setNome(rs.getString("responsavel.nome"));
		}
		
		return trabalhoConclusaoCursoArquivoVO;
	}
	
	@Override
	public Integer consultarTotalRegistroPorTCCEtapa(Integer tcc, EtapaTCCEnum etapaTCCEnum, TipoArquivoTCCEnum tipoArquivoTCCEnum) throws Exception {
		StringBuilder sql  =  new StringBuilder("SELECT count(codigo) as qtde from TrabalhoConclusaoCursoArquivo ");
		sql.append(" where trabalhoConclusaoCurso = ").append(tcc);
		if(etapaTCCEnum != null){
			sql.append(" and etapaTCC = '").append(etapaTCCEnum.name()).append("' ");
		}
		if(tipoArquivoTCCEnum != null){
			sql.append(" and tipoArquivoTCC = '").append(tipoArquivoTCCEnum.name()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(TrabalhoConclusaoCursoArquivoVO trabalhoConclusaoCursoArquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("DELETE FROM TrabalhoConclusaoCursoArquivo WHERE codigo = "+trabalhoConclusaoCursoArquivoVO.getCodigo() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+trabalhoConclusaoCursoArquivoVO.getCaminhoBaseFisicoArquivo()));
	}

}
