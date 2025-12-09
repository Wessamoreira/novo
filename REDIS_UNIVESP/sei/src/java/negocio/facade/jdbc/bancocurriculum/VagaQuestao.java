package negocio.facade.jdbc.bancocurriculum;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.bancocurriculum.enumeradores.TipoVagaQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.VagaQuestaoInterfaceFacade;

@Repository
@Lazy
public class VagaQuestao extends ControleAcesso implements VagaQuestaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2237620829245633999L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirVagaQuestao(VagasVO vaga, UsuarioVO usuarioVO) throws Exception {
		for (VagaQuestaoVO vagaQuestaoVO : vaga.getVagaQuestaoVOs()) {
			vagaQuestaoVO.setVaga(vaga);			
			incluir(vagaQuestaoVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final VagaQuestaoVO vagaQuestaoVO) throws Exception {
		validarDados(vagaQuestaoVO);
		vagaQuestaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO VagaQuestao ");
				sql.append(" (vaga, enunciado, tipoVagaQuestao, ordemApresentacao ) ");
				sql.append(" VALUES (?, ?, ?, ?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, vagaQuestaoVO.getVaga().getCodigo());
				ps.setString(x++, vagaQuestaoVO.getEnunciado());
				ps.setString(x++, vagaQuestaoVO.getTipoVagaQuestao().toString());
				ps.setInt(x++, vagaQuestaoVO.getOrdemApresentacao());
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
		getFacadeFactory().getOpcaoRespostaVagaQuestaoFacade().incluirOpcaoRespostaVagaQuestao(vagaQuestaoVO, null);
		vagaQuestaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final VagaQuestaoVO vagaQuestaoVO) throws Exception {
		validarDados(vagaQuestaoVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE VagaQuestao ");
				sql.append(" SET vaga = ?, enunciado=?, tipoVagaQuestao=?, ordemApresentacao=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, vagaQuestaoVO.getVaga().getCodigo());
				ps.setString(x++, vagaQuestaoVO.getEnunciado());
				ps.setString(x++, vagaQuestaoVO.getTipoVagaQuestao().toString());
				ps.setInt(x++, vagaQuestaoVO.getOrdemApresentacao());
				ps.setInt(x++, vagaQuestaoVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(vagaQuestaoVO);
			return;
		}
		getFacadeFactory().getOpcaoRespostaVagaQuestaoFacade().alteraOpcaoRespostaVagaQuestao(vagaQuestaoVO, null);
		vagaQuestaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirVagaQuestao(VagasVO vaga, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM vagaQuestao where vaga = ").append(vaga.getCodigo());
		sql.append(" and codigo not in (0");
		for (VagaQuestaoVO vagaQuestaoVO : vaga.getVagaQuestaoVOs()) {
			sql.append(", ").append(vagaQuestaoVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarVagaQuestao(VagasVO vaga, UsuarioVO usuarioVO) throws Exception {
		excluirVagaQuestao(vaga, usuarioVO);
		for (VagaQuestaoVO vagaQuestaoVO : vaga.getVagaQuestaoVOs()) {
			vagaQuestaoVO.setVaga(vaga);
			if (vagaQuestaoVO.isNovoObj()) {
				incluir(vagaQuestaoVO);
			} else {
				alterar(vagaQuestaoVO);
			}
		}

	}

	@Override
	public List<VagaQuestaoVO> consultarPorVagas(Integer vaga) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM VagaQuestao where vaga = ").append(vaga).append(" order by ordemApresentacao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<VagaQuestaoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<VagaQuestaoVO> vagaQuestaoVOs = new ArrayList<VagaQuestaoVO>(0);
		while (rs.next()) {
			vagaQuestaoVOs.add(montarDados(rs));
		}
		return vagaQuestaoVOs;
	}

	private VagaQuestaoVO montarDados(SqlRowSet rs) throws Exception {
		VagaQuestaoVO vagaQuestaoVO = new VagaQuestaoVO();
		vagaQuestaoVO.setNovoObj(false);
		vagaQuestaoVO.setCodigo(rs.getInt("codigo"));
		vagaQuestaoVO.getVaga().setCodigo(rs.getInt("vaga"));
		vagaQuestaoVO.setOrdemApresentacao(rs.getInt("ordemApresentacao"));
		vagaQuestaoVO.setEnunciado(rs.getString("enunciado"));
		vagaQuestaoVO.setTipoVagaQuestao(TipoVagaQuestaoEnum.valueOf(rs.getString("tipoVagaQuestao")));
		vagaQuestaoVO.setOpcaoRespostaVagaQuestaoVOs(getFacadeFactory().getOpcaoRespostaVagaQuestaoFacade().consultarPorVagaQuestao(vagaQuestaoVO.getCodigo()));
		return vagaQuestaoVO;
	}

	@Override
	public void alterarOrdemOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1, OpcaoRespostaVagaQuestaoVO opc2) throws Exception {
		Integer ordem1 = opc1.getOrdemApresentacao();
		opc1.setOrdemApresentacao(opc2.getOrdemApresentacao());
		opc2.setOrdemApresentacao(ordem1);
		opc1.setLetraCorrespondente(null);
		opc2.setLetraCorrespondente(null);
		Ordenacao.ordenarLista(vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs(), "ordemApresentacao");
	}

	@Override
	public void adicionarOrdemOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1, Boolean validarDados)  throws Exception {
		if(validarDados){
			getFacadeFactory().getOpcaoRespostaVagaQuestaoFacade().validarDados(opc1);
		}
		if (opc1.getOrdemApresentacao() > 0) {
			vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs().set(opc1.getOrdemApresentacao() - 1, opc1);
		} else {
			opc1.setOrdemApresentacao(vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs().size() + 1);
			vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs().add(opc1);
		}
	}

	@Override
	public void removerOrdemOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1)   throws Exception{
		if (opc1.getOrdemApresentacao() > 0) {
			vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs().remove(opc1.getOrdemApresentacao() - 1);
			int x = 1;
			for (OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO : vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs()) {
				opcaoRespostaVagaQuestaoVO.setOrdemApresentacao(x++);
			}
		}
	}

	@Override
	public void validarDados(VagaQuestaoVO vagaQuestaoVO) throws ConsistirException {
		ConsistirException ce = null;
		if (vagaQuestaoVO.getEnunciado().trim().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_VagaQuestao_enunciado"));
		}
		if (!vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL) && vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_VagaQuestao_opcaoResposta"));
		}
		if(vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL)){
			vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs().clear();
		}

		if (ce != null) {
			throw ce;
		}

	}
	
	@Override
	public VagaQuestaoVO novo() throws Exception{
		VagaQuestaoVO vagaQuestaoVO = new VagaQuestaoVO();
		vagaQuestaoVO.setTipoVagaQuestao(TipoVagaQuestaoEnum.UNICA_ESCOLHA);
		adicionarOrdemOpcaoRespostaVagaQuestao(vagaQuestaoVO, new OpcaoRespostaVagaQuestaoVO(), false);
		adicionarOrdemOpcaoRespostaVagaQuestao(vagaQuestaoVO, new OpcaoRespostaVagaQuestaoVO(), false);
		return vagaQuestaoVO;
	}

}
