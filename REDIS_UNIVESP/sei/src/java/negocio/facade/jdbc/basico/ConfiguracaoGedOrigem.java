package negocio.facade.jdbc.basico;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.ConfiguracaoGedOrigemInterfaceFacade;

@Repository
@Scope("singleton")
public class ConfiguracaoGedOrigem extends ControleAcesso implements ConfiguracaoGedOrigemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5754241443617840451L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class, Exception.class }, propagation = Propagation.REQUIRED)
	public void incluirConfiguracaoGedOrigemVOs(ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO)
			throws Exception {
		for(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO: configuracaoGEDVO.getConfiguracaoGedOrigemVOs()) {
			incluir(configuracaoGEDVO, configuracaoGedOrigemVO, usuarioVO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class, Exception.class }, propagation = Propagation.REQUIRED)
	public void incluir(ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGedOrigemVO configuracaoGedOrigemVO, UsuarioVO usuarioVO)
			throws Exception {
		incluir(configuracaoGedOrigemVO, "configuracaoGedOrigem", new AtributoPersistencia()
				.add("configuracaoGED", configuracaoGEDVO)
				.add("tipoOrigemDocumentoAssinado", configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado())
				.add("assinarDocumento", configuracaoGedOrigemVO.getAssinarDocumento())
				.add("apresentarAssinaturaDigitalizadoFuncionario", configuracaoGedOrigemVO.getApresentarAssinaturaDigitalizadoFuncionario())
				.add("apresentarSelo", configuracaoGedOrigemVO.getApresentarSelo())
				.add("alturaSelo", configuracaoGedOrigemVO.getAlturaSelo())
				.add("larguraSelo", configuracaoGedOrigemVO.getLarguraSelo())
				.add("posicaoXSelo", configuracaoGedOrigemVO.getPosicaoXSelo())
				.add("posicaoYSelo", configuracaoGedOrigemVO.getPosicaoYSelo())
				.add("ultimaPaginaSelo", configuracaoGedOrigemVO.isUltimaPaginaSelo())
				.add("apresentarQrCode", configuracaoGedOrigemVO.getApresentarQrCode())
				.add("alturaQrCode", configuracaoGedOrigemVO.getAlturaQrCode())
				.add("larguraQrCode", configuracaoGedOrigemVO.getLarguraQrCode())
				.add("posicaoXQrCode", configuracaoGedOrigemVO.getPosicaoXQrCode())
				.add("posicaoYQrCode", configuracaoGedOrigemVO.getPosicaoYQrCode())
				.add("ultimaPaginaQrCode", configuracaoGedOrigemVO.isUltimaPaginaQrCode())
				.add("assinaturaUnidadeEnsino", configuracaoGedOrigemVO.getAssinaturaUnidadeEnsino())
				.add("apresentarAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getApresentarAssinaturaUnidadeEnsino())
				.add("alturaAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getAlturaAssinaturaUnidadeEnsino())
				.add("larguraAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getLarguraAssinaturaUnidadeEnsino())
				.add("posicaoXAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getPosicaoXAssinaturaUnidadeEnsino())
				.add("posicaoYAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getPosicaoYAssinaturaUnidadeEnsino())
				.add("ultimaPaginaAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaUnidadeEnsino())
				.add("apresentarAssinaturaFuncionario1", configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario1())
				.add("alturaAssinaturaFuncionario1", configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario1())
				.add("larguraAssinaturaFuncionario1", configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario1())
				.add("posicaoXAssinaturaFuncionario1", configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario1())
				.add("posicaoYAssinaturaFuncionario1", configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario1())
				.add("ultimaPaginaAssinaturaFuncionario1", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario1())
				.add("apresentarAssinaturaFuncionario2", configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario2())
				.add("alturaAssinaturaFuncionario2", configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario2())
				.add("larguraAssinaturaFuncionario2", configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario2())
				.add("posicaoXAssinaturaFuncionario2", configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario2())
				.add("posicaoYAssinaturaFuncionario2", configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario2())
				.add("ultimaPaginaAssinaturaFuncionario2", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario2())
				.add("apresentarAssinaturaFuncionario3", configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario3())
				.add("alturaAssinaturaFuncionario3", configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario3())
				.add("larguraAssinaturaFuncionario3", configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario3())
				.add("posicaoXAssinaturaFuncionario3", configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario3())
				.add("posicaoYAssinaturaFuncionario3", configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario3())
				.add("ultimaPaginaAssinaturaFuncionario3", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario3())
				.add("assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada", configuracaoGedOrigemVO.getAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada())
				.add("assinarDocumentoFuncionarioResponsavel", configuracaoGedOrigemVO.getAssinarDocumentoFuncionarioResponsavel())
				.add("apresentarAssinaturaAluno", configuracaoGedOrigemVO.getApresentarAssinaturaAluno())
				.add("alturaAssinaturaAluno", configuracaoGedOrigemVO.getAlturaAssinaturaAluno())
				.add("larguraAssinaturaAluno", configuracaoGedOrigemVO.getLarguraAssinaturaAluno())
				.add("posicaoXAssinaturaAluno", configuracaoGedOrigemVO.getPosicaoXAssinaturaAluno())
				.add("posicaoYAssinaturaAluno", configuracaoGedOrigemVO.getPosicaoYAssinaturaAluno())
				.add("ultimaPaginaAssinaturaAluno", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaAluno())
				.add("provedorAssinaturaPadraoEnum", configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum())
				.add("permitirAlunoAssinarDigitalmente", configuracaoGedOrigemVO.getPermitirAlunoAssinarDigitalmente())
				, usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class, Exception.class }, propagation = Propagation.REQUIRED)
	public void alterar(ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGedOrigemVO configuracaoGedOrigemVO, UsuarioVO usuarioVO)
			throws Exception {
		alterar(configuracaoGedOrigemVO, "configuracaoGedOrigem", new AtributoPersistencia()
				.add("configuracaoGED", configuracaoGEDVO)
				.add("tipoOrigemDocumentoAssinado", configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado())
				.add("assinarDocumento", configuracaoGedOrigemVO.getAssinarDocumento())
				.add("apresentarAssinaturaDigitalizadoFuncionario", configuracaoGedOrigemVO.getApresentarAssinaturaDigitalizadoFuncionario())
				.add("apresentarSelo", configuracaoGedOrigemVO.getApresentarSelo())
				.add("alturaSelo", configuracaoGedOrigemVO.getAlturaSelo())
				.add("larguraSelo", configuracaoGedOrigemVO.getLarguraSelo())
				.add("posicaoXSelo", configuracaoGedOrigemVO.getPosicaoXSelo())
				.add("posicaoYSelo", configuracaoGedOrigemVO.getPosicaoYSelo())
				.add("ultimaPaginaSelo", configuracaoGedOrigemVO.isUltimaPaginaSelo())
				.add("apresentarQrCode", configuracaoGedOrigemVO.getApresentarQrCode())
				.add("alturaQrCode", configuracaoGedOrigemVO.getAlturaQrCode())
				.add("larguraQrCode", configuracaoGedOrigemVO.getLarguraQrCode())
				.add("posicaoXQrCode", configuracaoGedOrigemVO.getPosicaoXQrCode())
				.add("posicaoYQrCode", configuracaoGedOrigemVO.getPosicaoYQrCode())
				.add("ultimaPaginaQrCode", configuracaoGedOrigemVO.isUltimaPaginaQrCode())
				.add("assinaturaUnidadeEnsino", configuracaoGedOrigemVO.getAssinaturaUnidadeEnsino())
				.add("apresentarAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getApresentarAssinaturaUnidadeEnsino())
				.add("alturaAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getAlturaAssinaturaUnidadeEnsino())
				.add("larguraAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getLarguraAssinaturaUnidadeEnsino())
				.add("posicaoXAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getPosicaoXAssinaturaUnidadeEnsino())
				.add("posicaoYAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.getPosicaoYAssinaturaUnidadeEnsino())
				.add("ultimaPaginaAssinaturaUnidadeEnsino", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaUnidadeEnsino())
				.add("apresentarAssinaturaFuncionario1", configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario1())
				.add("alturaAssinaturaFuncionario1", configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario1())
				.add("larguraAssinaturaFuncionario1", configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario1())
				.add("posicaoXAssinaturaFuncionario1", configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario1())
				.add("posicaoYAssinaturaFuncionario1", configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario1())
				.add("ultimaPaginaAssinaturaFuncionario1", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario1())
				.add("apresentarAssinaturaFuncionario2", configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario2())
				.add("alturaAssinaturaFuncionario2", configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario2())
				.add("larguraAssinaturaFuncionario2", configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario2())
				.add("posicaoXAssinaturaFuncionario2", configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario2())
				.add("posicaoYAssinaturaFuncionario2", configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario2())
				.add("ultimaPaginaAssinaturaFuncionario2", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario2())
				.add("apresentarAssinaturaFuncionario3", configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario3())
				.add("alturaAssinaturaFuncionario3", configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario3())
				.add("larguraAssinaturaFuncionario3", configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario3())
				.add("posicaoXAssinaturaFuncionario3", configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario3())
				.add("posicaoYAssinaturaFuncionario3", configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario3())
				.add("ultimaPaginaAssinaturaFuncionario3", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario3())
				.add("assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada", configuracaoGedOrigemVO.getAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada())
				.add("assinarDocumentoFuncionarioResponsavel", configuracaoGedOrigemVO.getAssinarDocumentoFuncionarioResponsavel())
				.add("apresentarAssinaturaAluno", configuracaoGedOrigemVO.getApresentarAssinaturaAluno())
				.add("alturaAssinaturaAluno", configuracaoGedOrigemVO.getAlturaAssinaturaAluno())
				.add("larguraAssinaturaAluno", configuracaoGedOrigemVO.getLarguraAssinaturaAluno())
				.add("posicaoXAssinaturaAluno", configuracaoGedOrigemVO.getPosicaoXAssinaturaAluno())
				.add("posicaoYAssinaturaAluno", configuracaoGedOrigemVO.getPosicaoYAssinaturaAluno())
				.add("ultimaPaginaAssinaturaAluno", configuracaoGedOrigemVO.isUltimaPaginaAssinaturaAluno())
				.add("provedorAssinaturaPadraoEnum", configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum())
				.add("permitirAlunoAssinarDigitalmente", configuracaoGedOrigemVO.getPermitirAlunoAssinarDigitalmente())
				, new AtributoPersistencia()
				.add("codigo", configuracaoGedOrigemVO.getCodigo()), usuarioVO);
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class, Exception.class }, propagation = Propagation.REQUIRED)
	public void alterarConfiguracaoGedOrigemVOs(ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO)
			throws Exception {
		for(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO: configuracaoGEDVO.getConfiguracaoGedOrigemVOs()) {
			if(!Uteis.isAtributoPreenchido(configuracaoGedOrigemVO)) {
				incluir(configuracaoGEDVO, configuracaoGedOrigemVO, usuarioVO);
			}else {
				alterar(configuracaoGEDVO, configuracaoGedOrigemVO, usuarioVO);
			}
		}

	}

	@Override	
	public void carregarDadosConfiguracaoGedOrigemVOs(ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO)
			throws Exception {
		StringBuilder sql  = new StringBuilder("select * from configuracaoGedOrigem where configuracaoGED = ? order by codigo");
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), configuracaoGEDVO.getCodigo());
		while(rs.next()) {
			ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configuracaoGEDVO.getConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.valueOf(rs.getString("tipoOrigemDocumentoAssinado")));
			configuracaoGedOrigemVO.setNovoObj(false);
			configuracaoGedOrigemVO.setCodigo(rs.getInt("codigo"));
			configuracaoGedOrigemVO.setConfiguracaoGEDVO(configuracaoGEDVO);
			configuracaoGedOrigemVO.setTipoOrigemDocumentoAssinado(TipoOrigemDocumentoAssinadoEnum.valueOf(rs.getString("tipoOrigemDocumentoAssinado")));
			configuracaoGedOrigemVO.setAssinarDocumento(rs.getBoolean("assinarDocumento"));
			configuracaoGedOrigemVO.setApresentarAssinaturaDigitalizadoFuncionario(rs.getBoolean("apresentarAssinaturaDigitalizadoFuncionario"));
			configuracaoGedOrigemVO.setApresentarSelo(rs.getBoolean("apresentarSelo"));
			configuracaoGedOrigemVO.setAlturaSelo(rs.getInt("alturaSelo"));
			configuracaoGedOrigemVO.setLarguraSelo(rs.getInt("larguraSelo"));
			configuracaoGedOrigemVO.setPosicaoXSelo(rs.getInt("posicaoXSelo"));
			configuracaoGedOrigemVO.setPosicaoYSelo(rs.getInt("posicaoYSelo"));
			configuracaoGedOrigemVO.setUltimaPaginaSelo(rs.getBoolean("ultimaPaginaSelo"));
			configuracaoGedOrigemVO.setApresentarQrCode(rs.getBoolean("apresentarQrCode"));
			configuracaoGedOrigemVO.setAlturaQrCode(rs.getInt("alturaQrCode"));
			configuracaoGedOrigemVO.setLarguraQrCode(rs.getInt("larguraQrCode"));
			configuracaoGedOrigemVO.setPosicaoXQrCode(rs.getInt("posicaoXQrCode"));
			configuracaoGedOrigemVO.setPosicaoYQrCode(rs.getInt("posicaoYQrCode"));
			configuracaoGedOrigemVO.setUltimaPaginaQrCode(rs.getBoolean("ultimaPaginaQrCode"));
			configuracaoGedOrigemVO.setAssinaturaUnidadeEnsino(rs.getBoolean("assinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setApresentarAssinaturaUnidadeEnsino(rs.getBoolean("apresentarAssinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setAlturaAssinaturaUnidadeEnsino(rs.getInt("alturaAssinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setLarguraAssinaturaUnidadeEnsino(rs.getInt("larguraAssinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setPosicaoXAssinaturaUnidadeEnsino(rs.getInt("posicaoXAssinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setPosicaoYAssinaturaUnidadeEnsino(rs.getInt("posicaoYAssinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setUltimaPaginaAssinaturaUnidadeEnsino(rs.getBoolean("ultimaPaginaAssinaturaUnidadeEnsino"));
			configuracaoGedOrigemVO.setApresentarAssinaturaFuncionario1(rs.getBoolean("apresentarAssinaturaFuncionario1"));
			configuracaoGedOrigemVO.setAlturaAssinaturaFuncionario1(rs.getInt("alturaAssinaturaFuncionario1"));
			configuracaoGedOrigemVO.setLarguraAssinaturaFuncionario1(rs.getInt("larguraAssinaturaFuncionario1"));
			configuracaoGedOrigemVO.setPosicaoXAssinaturaFuncionario1(rs.getInt("posicaoXAssinaturaFuncionario1"));
			configuracaoGedOrigemVO.setPosicaoYAssinaturaFuncionario1(rs.getInt("posicaoYAssinaturaFuncionario1"));
			configuracaoGedOrigemVO.setUltimaPaginaAssinaturaFuncionario1(rs.getBoolean("ultimaPaginaAssinaturaFuncionario1"));
			configuracaoGedOrigemVO.setApresentarAssinaturaFuncionario2(rs.getBoolean("apresentarAssinaturaFuncionario2"));
			configuracaoGedOrigemVO.setAlturaAssinaturaFuncionario2(rs.getInt("alturaAssinaturaFuncionario2"));
			configuracaoGedOrigemVO.setLarguraAssinaturaFuncionario2(rs.getInt("larguraAssinaturaFuncionario2"));
			configuracaoGedOrigemVO.setPosicaoXAssinaturaFuncionario2(rs.getInt("posicaoXAssinaturaFuncionario2"));
			configuracaoGedOrigemVO.setPosicaoYAssinaturaFuncionario2(rs.getInt("posicaoYAssinaturaFuncionario2"));
			configuracaoGedOrigemVO.setUltimaPaginaAssinaturaFuncionario2(rs.getBoolean("ultimaPaginaAssinaturaFuncionario2"));
			configuracaoGedOrigemVO.setApresentarAssinaturaFuncionario3(rs.getBoolean("apresentarAssinaturaFuncionario3"));
			configuracaoGedOrigemVO.setAlturaAssinaturaFuncionario3(rs.getInt("alturaAssinaturaFuncionario3"));
			configuracaoGedOrigemVO.setLarguraAssinaturaFuncionario3(rs.getInt("larguraAssinaturaFuncionario3"));
			configuracaoGedOrigemVO.setPosicaoXAssinaturaFuncionario3(rs.getInt("posicaoXAssinaturaFuncionario3"));
			configuracaoGedOrigemVO.setPosicaoYAssinaturaFuncionario3(rs.getInt("posicaoYAssinaturaFuncionario3"));
			configuracaoGedOrigemVO.setUltimaPaginaAssinaturaFuncionario3(rs.getBoolean("ultimaPaginaAssinaturaFuncionario3"));
			configuracaoGedOrigemVO.setAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada(rs.getBoolean("assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada"));
			configuracaoGedOrigemVO.setAssinarDocumentoFuncionarioResponsavel(rs.getBoolean("assinarDocumentoFuncionarioResponsavel"));
			configuracaoGedOrigemVO.setApresentarAssinaturaAluno(rs.getBoolean("apresentarAssinaturaAluno"));
			configuracaoGedOrigemVO.setAlturaAssinaturaAluno(rs.getInt("alturaAssinaturaAluno"));
			configuracaoGedOrigemVO.setLarguraAssinaturaAluno(rs.getInt("larguraAssinaturaAluno"));
			configuracaoGedOrigemVO.setPosicaoXAssinaturaAluno(rs.getInt("posicaoXAssinaturaAluno"));
			configuracaoGedOrigemVO.setPosicaoYAssinaturaAluno(rs.getInt("posicaoYAssinaturaAluno"));
			configuracaoGedOrigemVO.setUltimaPaginaAssinaturaAluno(rs.getBoolean("ultimaPaginaAssinaturaAluno"));
			configuracaoGedOrigemVO.setProvedorAssinaturaPadraoEnum(ProvedorDeAssinaturaEnum.valueOf(rs.getString("provedorAssinaturaPadraoEnum")));
			configuracaoGedOrigemVO.setPermitirAlunoAssinarDigitalmente(rs.getBoolean("permitirAlunoAssinarDigitalmente"));
			
		}

	}

}
