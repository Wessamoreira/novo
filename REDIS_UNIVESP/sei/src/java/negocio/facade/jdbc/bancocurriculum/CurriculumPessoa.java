package negocio.facade.jdbc.bancocurriculum;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.CurriculumPessoaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.CurriculumPessoaInterfaceFacade;

@Repository
@Lazy
public class CurriculumPessoa extends ControleAcesso implements CurriculumPessoaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -795479639833409528L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		validarDados(curriculumPessoaVO);
//		ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(PastaBaseArquivoEnum.CURRICULUM_TMP.getValue(), curriculumPessoaVO.getNomeRealArquivo(), configuracaoGeralSistemaVO);
//		curriculumPessoaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
//			StringBuilder sql = new StringBuilder("INSERT INTO CurriculumPessoa (pessoa, nomeRealArquivo, nomeApresentacaoArquivo, descricao, dataCadastro ) VALUES (?,?,?,?,?) returning codigo");
//			@Override
//			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//				PreparedStatement ps = arg0.prepareStatement(sql.toString());
//				ps.setInt(1, curriculumPessoaVO.getPessoa().getCodigo());
//				ps.setString(2, curriculumPessoaVO.getNomeRealArquivo());
//				ps.setString(3, curriculumPessoaVO.getNomeApresentacaoArquivo());
//				ps.setString(4, curriculumPessoaVO.getDescricao());
//				ps.setDate(5, Uteis.getDataJDBC(curriculumPessoaVO.getDataCadastro()));
//				return ps;
//			}
//		}, new ResultSetExtractor<Integer>() {
//
//			@Override
//			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
//				if(arg0.next()){
//					return arg0.getInt("codigo");
//				}
//				return null;
//			}
//		}));
//		curriculumPessoaVO.setNovoObj(false);

	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		validarDados(curriculumPessoaVO);		
//		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
//			StringBuilder sql = new StringBuilder("UPDATE CurriculumPessoa SET pessoa=?, nomeRealArquivo=?, nomeApresentacaoArquivo=?, descricao=?, dataCadastro=? WHERE codigo = ?");
//			@Override
//			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//				PreparedStatement ps = arg0.prepareStatement(sql.toString());
//				ps.setInt(1, curriculumPessoaVO.getPessoa().getCodigo());
//				ps.setString(2, curriculumPessoaVO.getNomeRealArquivo());
//				ps.setString(3, curriculumPessoaVO.getNomeApresentacaoArquivo());
//				ps.setString(4, curriculumPessoaVO.getDescricao());
//				ps.setDate(5, Uteis.getDataJDBC(curriculumPessoaVO.getDataCadastro()));
//				ps.setInt(6, curriculumPessoaVO.getCodigo());
//				return ps;
//			}
//		})==0){
//			incluir(curriculumPessoaVO, configuracaoGeralSistemaVO, usuarioVO);
//			return;
//		}
//		curriculumPessoaVO.setNovoObj(false);		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCurriculumPessoa(PessoaVO pessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception{
//		for(CurriculumPessoaVO curriculumPessoaVO:pessoaVO.getCurriculumPessoaVOs()){
//			if(curriculumPessoaVO.isNovoObj()){
//				incluir(curriculumPessoaVO, configuracaoGeralSistemaVO, usuarioVO);
//			}else{
//				alterar(curriculumPessoaVO, configuracaoGeralSistemaVO, usuarioVO);
//			}
//		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+PastaBaseArquivoEnum.CURRICULUM.getValue()+File.separator+curriculumPessoaVO.getNomeRealArquivo()));
//		getConexao().getJdbcTemplate().update("DELETE FROM CurriculumPessoa WHERE codigo = "+curriculumPessoaVO.getCodigo());
	}

	@Override
	public List<CurriculumPessoaVO> consultarPorPessoa(Integer pessoa) throws Exception {
//		StringBuilder sql = new StringBuilder("SELECT * FROM CurriculumPessoa where pessoa = ").append(pessoa).append(" order by dataCadastro desc ");
//		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CurriculumPessoaVO> curriculumPessoaVOs = new ArrayList<CurriculumPessoaVO>(0);
//		CurriculumPessoaVO curriculumPessoaVO = null;
//		while(rs.next()){
//			curriculumPessoaVO = new CurriculumPessoaVO();
//			curriculumPessoaVO.setNovoObj(false);
//			curriculumPessoaVO.setCodigo(rs.getInt("codigo"));
//			curriculumPessoaVO.getPessoa().setCodigo(rs.getInt("pessoa"));
//			curriculumPessoaVO.setDataCadastro(rs.getDate("dataCadastro"));
//			curriculumPessoaVO.setDescricao(rs.getString("descricao"));
//			curriculumPessoaVO.setNomeApresentacaoArquivo(rs.getString("nomeApresentacaoArquivo"));
//			curriculumPessoaVO.setNomeRealArquivo(rs.getString("nomeRealArquivo"));
//			curriculumPessoaVOs.add(curriculumPessoaVO);
//		}
		return curriculumPessoaVOs;
	}

	@Override
	public void validarDados(CurriculumPessoaVO curriculumPessoaVO) throws ConsistirException {
//		if(curriculumPessoaVO.getNomeApresentacaoArquivo().trim().isEmpty()){
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_CurriculumPessoa_nomeApresentacaoArquivo"));
//		}
//		if(curriculumPessoaVO.getNomeRealArquivo().trim().isEmpty()){
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_CurriculumPessoa_nomeRealArquivo"));
//		}

	}

}
