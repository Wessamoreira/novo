package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.NotaConceitoIndicadorAvaliacaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.NotaConceitoIndicadorAvaliacaoInterfaceFacade;

@Repository
@Lazy
public class NotaConceitoIndicadorAvaliacao extends ControleAcesso implements NotaConceitoIndicadorAvaliacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8609167147175381702L;
	private static String idEntidade = "NotaConceitoIndicadorAvaliacao"; 

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		realizarCopiaImagem(notaConceitoIndicadorAvaliacaoVO, configuracaoGeralSistemaVO, usuario);
		if(notaConceitoIndicadorAvaliacaoVO.isNovoObj()){
			incluir(notaConceitoIndicadorAvaliacaoVO, verificarAcesso, usuario);
		}else{
			alterar(notaConceitoIndicadorAvaliacaoVO, verificarAcesso, usuario);
		}		
		
	}
	
	@Override
	public void uploadArquivo(FileUploadEvent uploadEvent, NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception{
		if(!notaConceitoIndicadorAvaliacaoVO.getNomeArquivo().trim().isEmpty() && notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.INDICADOR_AVALIACAO)){
			notaConceitoIndicadorAvaliacaoVO.setNomeArquivoAnt(notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
		}else if(!notaConceitoIndicadorAvaliacaoVO.getNomeArquivo().trim().isEmpty() && notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.INDICADOR_AVALIACAO_TMP)){
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().getValue()+File.separator+notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());			
			ArquivoHelper.delete(arquivo);
			arquivo = null;
		}
		notaConceitoIndicadorAvaliacaoVO.setNomeArquivo(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(uploadEvent.getUploadedFile().getName())));
		notaConceitoIndicadorAvaliacaoVO.setNomeArquivoApresentar(uploadEvent.getUploadedFile().getName());
		notaConceitoIndicadorAvaliacaoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.INDICADOR_AVALIACAO_TMP);	
		
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, notaConceitoIndicadorAvaliacaoVO.getNomeArquivo(), notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().getValue(), configuracaoGeralSistemaVO, usuarioVO);		
		notaConceitoIndicadorAvaliacaoVO.setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()+"/"+notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().getValue()+"/"+notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
	}
	
	private void realizarCopiaImagem(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception{
		if(notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.INDICADOR_AVALIACAO_TMP) && !notaConceitoIndicadorAvaliacaoVO.getNomeArquivo().trim().isEmpty()){
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+PastaBaseArquivoEnum.INDICADOR_AVALIACAO_TMP.getValue()+File.separator+notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
			
			ArquivoHelper.salvarArquivoNaPastaTemp(arquivo, notaConceitoIndicadorAvaliacaoVO.getNomeArquivo(), PastaBaseArquivoEnum.INDICADOR_AVALIACAO.getValue(), configuracaoGeralSistemaVO, usuario);
			ArquivoHelper.delete(arquivo);
			notaConceitoIndicadorAvaliacaoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.INDICADOR_AVALIACAO);
			notaConceitoIndicadorAvaliacaoVO.setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()+"/"+PastaBaseArquivoEnum.INDICADOR_AVALIACAO.getValue()+"/"+notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());			
			if(!notaConceitoIndicadorAvaliacaoVO.getNomeArquivoAnt().trim().isEmpty()){
				arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+PastaBaseArquivoEnum.INDICADOR_AVALIACAO.getValue()+File.separator+notaConceitoIndicadorAvaliacaoVO.getNomeArquivoAnt());
				ArquivoHelper.delete(arquivo);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		incluir(getIdEntidade(), verificarAcesso, usuario);
		validarDados(notaConceitoIndicadorAvaliacaoVO);
		
		try {
			notaConceitoIndicadorAvaliacaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO notaConceitoIndicadorAvaliacao (");
					sql.append(" descricao, nomeArquivo, nomeArquivoApresentar, pastaBaseArquivo, situacao ) ");
					sql.append(" VALUES (? , ?,  ?, ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getDescricao());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getNomeArquivoApresentar());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().name());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getSituacao().name());
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
			
			notaConceitoIndicadorAvaliacaoVO.setNovoObj(false);
		} catch (Exception e) {
			notaConceitoIndicadorAvaliacaoVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		alterar(getIdEntidade(), verificarAcesso, usuario);
		validarDados(notaConceitoIndicadorAvaliacaoVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE notaConceitoIndicadorAvaliacao SET ");
					sql.append(" descricao = ?, nomeArquivo = ?, nomeArquivoApresentar = ?, pastaBaseArquivo = ?, situacao = ?  ");
					sql.append(" where codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getDescricao());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getNomeArquivoApresentar());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().name());
					ps.setString(x++, notaConceitoIndicadorAvaliacaoVO.getSituacao().name());
					ps.setInt(x++, notaConceitoIndicadorAvaliacaoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(notaConceitoIndicadorAvaliacaoVO,verificarAcesso,usuario);
				return;
			}
			
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		excluir(getIdEntidade(), verificarAcesso, usuario);
		getConexao().getJdbcTemplate().update("DELETE FROM notaConceitoIndicadorAvaliacao where codigo = ? ", notaConceitoIndicadorAvaliacaoVO.getCodigo());
		if(!notaConceitoIndicadorAvaliacaoVO.getNomeArquivo().trim().isEmpty()){
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().getValue()+File.separator+notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
			ArquivoHelper.delete(arquivo);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void inativar(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try{			
			notaConceitoIndicadorAvaliacaoVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
			persistir(notaConceitoIndicadorAvaliacaoVO, configuracaoGeralSistemaVO, verificarAcesso, usuario);
		}catch(Exception e){
			notaConceitoIndicadorAvaliacaoVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void ativar(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try{			
			notaConceitoIndicadorAvaliacaoVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
			persistir(notaConceitoIndicadorAvaliacaoVO, configuracaoGeralSistemaVO, verificarAcesso, usuario);
		}catch(Exception e){
			notaConceitoIndicadorAvaliacaoVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
		}
		
	}

	@Override
	public List<NotaConceitoIndicadorAvaliacaoVO> consultar(String consultarPor, String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select * from notaConceitoIndicadorAvaliacao ");
		if(consultarPor.equals("descricao")){
			sql.append(" where sem_acentos(descricao) ilike ('").append(valorConsulta).append("%') order by descricao ");
		}else if(consultarPor.equals("situacao")){
			sql.append(" where situacao = '").append(valorConsulta).append("' order by descricao ");
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<NotaConceitoIndicadorAvaliacaoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<NotaConceitoIndicadorAvaliacaoVO> criterioAvaliacaoNotaConceitoVOs = new ArrayList<NotaConceitoIndicadorAvaliacaoVO>(0);
		while(rs.next()){
			criterioAvaliacaoNotaConceitoVOs.add(montarDados(rs));
		}
		return criterioAvaliacaoNotaConceitoVOs;
	}
	private NotaConceitoIndicadorAvaliacaoVO montarDados(SqlRowSet rs) throws Exception{
		NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO = new NotaConceitoIndicadorAvaliacaoVO();
		notaConceitoIndicadorAvaliacaoVO.setNovoObj(false);
		notaConceitoIndicadorAvaliacaoVO.setCodigo(rs.getInt("codigo"));		
		notaConceitoIndicadorAvaliacaoVO.setDescricao(rs.getString("descricao"));
		notaConceitoIndicadorAvaliacaoVO.setNomeArquivo(rs.getString("nomeArquivo"));
		notaConceitoIndicadorAvaliacaoVO.setNomeArquivoApresentar(rs.getString("nomeArquivoApresentar"));
		if(rs.getString("pastaBaseArquivo") != null && !rs.getString("pastaBaseArquivo").trim().isEmpty()){
			notaConceitoIndicadorAvaliacaoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(rs.getString("pastaBaseArquivo")));
		}
		notaConceitoIndicadorAvaliacaoVO.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
		return notaConceitoIndicadorAvaliacaoVO;
	}


	@Override
	public NotaConceitoIndicadorAvaliacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select * from notaConceitoIndicadorAvaliacao ");
		sql.append(" where codigo = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if(rs.next()){
			return montarDados(rs);
		}
		throw new Exception("Dados não encontrados Nota Conceito Criterio Avaliação.");
	}

	@Override
	public void validarDados(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO) throws ConsistirException {
		if(notaConceitoIndicadorAvaliacaoVO.getDescricao().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_NotaConceitoIndicadorAvaliacao_descricao"));
		}
		
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "NotaConceitoIndicadorAvaliacao";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaConceitoIndicadorAvaliacao.idEntidade = idEntidade;
	}
	
	

}
