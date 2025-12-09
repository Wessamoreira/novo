package negocio.facade.jdbc.basico;

import java.util.*;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.enumeradores.TipoProvedorAssinaturaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.basico.ConfiguracaoGEDInterfaceFacade;
import webservice.nfse.generic.AmbienteEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ConfiguracaoGEDVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ConfiguracaoGEDVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ConfiguracaoGEDVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoGED extends ControleAcesso implements ConfiguracaoGEDInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2110831695616522285L;
	protected static String idEntidade;

	public ConfiguracaoGED() throws Exception {
		super();
		setIdEntidade("ConfiguracaoGED");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ConfiguracaoGEDVO</code>.
	 */
	public ConfiguracaoGEDVO novo() throws Exception {
		ConfiguracaoGED.incluir(getIdEntidade());
		ConfiguracaoGEDVO obj = new ConfiguracaoGEDVO();
		return obj;
	}

	public void validarDados(ConfiguracaoGEDVO obj, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getNome())) {
			throw new Exception("O campo NOME deve ser informado!");
		}		
//		if (!Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeEnsinoVO().getNome())) {
//			throw new Exception("O campo CERTIFICADO DIGITAL UNIDADE DE ENSINO deve ser informado!");
//		}
//		if (!Uteis.isAtributoPreenchido(obj.getSenhaCertificadoDigitalUnidadeEnsino())) {
//			throw new Exception("O campo SENHA CERTIFICADO DIGITAL UNIDADE DE ENSINO deve ser informado!");
//		}
//		if (obj.getUtilizarAssinaturaUnidadeCertificadora()) {
//			if (!Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeCertificadora().getNome())) {
//				throw new Exception("O campo CERTIFICADO DIGITAL UNIDADE CERTIFICADORA deve ser informado!");
//			}
//			if (!Uteis.isAtributoPreenchido(obj.getSenhaCertificadoDigitalUnidadeCertificadora())) {
//				throw new Exception("O campo SENHA CERTIFICADO DIGITAL UNIDADE CERTIFICADORA deve ser informado!");
//			}
//		}
		if(!Uteis.isAtributoPreenchido(obj.getSeloAssinaturaEletronicaVO().getNome()) && 
				((obj.getConfiguracaoGedAtaResultadosFinaisVO().getAssinarDocumento() && obj.getConfiguracaoGedAtaResultadosFinaisVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedBoletimAcademicoVO().getAssinarDocumento() && obj.getConfiguracaoGedBoletimAcademicoVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedCertificadoVO().getAssinarDocumento() && obj.getConfiguracaoGedCertificadoVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedContratoVO().getAssinarDocumento() && obj.getConfiguracaoGedContratoVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedDeclaracaoVO().getAssinarDocumento() && obj.getConfiguracaoGedDeclaracaoVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedDiarioVO().getAssinarDocumento() && obj.getConfiguracaoGedDiarioVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedDiplomaVO().getAssinarDocumento() && obj.getConfiguracaoGedDiplomaVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento() && obj.getConfiguracaoGedDocumentoAlunoVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedDocumentoProfessorVO().getAssinarDocumento() && obj.getConfiguracaoGedDocumentoProfessorVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedHistoricoVO().getAssinarDocumento() && obj.getConfiguracaoGedHistoricoVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedImpostoRendaVO().getAssinarDocumento() && obj.getConfiguracaoGedImpostoRendaVO().getApresentarSelo())
				|| (obj.getConfiguracaoGedAtaColacaoGrauVO().getAssinarDocumento() && obj.getConfiguracaoGedAtaColacaoGrauVO().getApresentarSelo())
				)) {
			throw new Exception("O campo SELO ASSINATURA ELETRÔNICA deve ser informado.");
		}
//		Uteis.checkState(obj.getHabilitarIntegracaoCertisign() && !Uteis.isAtributoPreenchido(obj.getUsuarioProvedorDeAssinatura()), "O campo USUÁRIO PROVEDOR de ASSINATURA deve ser informado!");
//		Uteis.checkState(obj.getHabilitarIntegracaoCertisign() && !Uteis.isAtributoPreenchido(obj.getSenhaProvedorDeAssinatura()), "O campo SENHA PROVEDOR de ASSINATURA deve ser informado!");
		Uteis.checkState(obj.getHabilitarIntegracaoCertisign() && !Uteis.isAtributoPreenchido(obj.getTokenProvedorDeAssinatura()), "O campo TOKEN PROVEDOR de ASSINATURA deve ser informado!");
//		if (AmbienteEnum.PRODUCAO == obj.getAmbienteTechCertEnum()){
//			Uteis.checkState(obj.getHabilitarIntegracaoTechCert() && !Uteis.isAtributoPreenchido(obj.getUrlIntegracaoTechCertProducao()), "O campo URL BASE PRODUÇÃO deve ser informado!");
//			Uteis.checkState(obj.getHabilitarIntegracaoTechCert() && !Uteis.isAtributoPreenchido(obj.getTokenTechCert()), "O campo TOKEN PRODUÇÃO deve ser informado!");
//		}
//		if (AmbienteEnum.HOMOLOGACAO == obj.getAmbienteTechCertEnum()){
//			Uteis.checkState(obj.getHabilitarIntegracaoTechCert() && !Uteis.isAtributoPreenchido(obj.getUrlIntegracaoTechCertHomologacao()), "O campo URL BASE TECHCERT deve ser informado!");
//			Uteis.checkState(obj.getHabilitarIntegracaoTechCert() && !Uteis.isAtributoPreenchido(obj.getTokenTechCertHomologacao()), "O campo TOKEN HOMOLOGAÇÃO deve ser informado!");
//		}
		Uteis.checkState(obj.getHabilitarIntegracaoTechCert() && !Uteis.isAtributoPreenchido(obj.getApikeyTechCert()), "O campo API KEY deve ser informado!");

		Uteis.checkState(obj.getHabilitarIntegracaoImprensaOficial() && !Uteis.isAtributoPreenchido(obj.getTokenImprensaOficial()), "O campo TOKEN INTEGRAÇÃO IMPRENSA OFICIAL deve ser informado!");
		Uteis.checkState(obj.getHabilitarIntegracaoImprensaOficial() && !Uteis.isAtributoPreenchido(obj.getUrlIntegracaoImprensaOficialHomologacao()), "O campo URL HOMOLOGAÇÃO DE INTEGRAÇÃO IMPRENSA OFICIAL deve ser informado!");
		Uteis.checkState(obj.getHabilitarIntegracaoImprensaOficial() && !Uteis.isAtributoPreenchido(obj.getUrlIntegracaoImprensaOficialProducao()), "O campo URL PRODUÇÃO INTEGRAÇÃO IMPRENSA OFICIAL deve ser informado!");
		
	}
	
	@Override
	public void persistir(ConfiguracaoGEDVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo().equals(0)) {
			incluir(obj, usuarioVO, configuracaoGeralSistemaVO);
		} else {
			alterar(obj, usuarioVO, configuracaoGeralSistemaVO);
			getAplicacaoControle().removerConfiguracaoGEDVO(obj.getCodigo());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoGEDVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			validarDados(obj, usuarioVO);
			incluir(getIdEntidade(), true, usuarioVO);
			if (Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeEnsinoVO().getNome())) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getCertificadoDigitalUnidadeEnsinoVO(), usuarioVO, configuracaoGeralSistemaVO);
			}
			if (Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeCertificadora().getNome())) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getCertificadoDigitalUnidadeCertificadora(), usuarioVO, configuracaoGeralSistemaVO);
			}
			if (Uteis.isAtributoPreenchido(obj.getSeloAssinaturaEletronicaVO())) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getSeloAssinaturaEletronicaVO(), usuarioVO, configuracaoGeralSistemaVO);
			}
			AtributoPersistencia atributoPersistencia = new  AtributoPersistencia();
			atributoPersistencia
					.add("nome", obj.getNome())
					.add("usuarioProvedorDeAssinatura", obj.getUsuarioProvedorDeAssinatura())
					.add("senhaProvedorDeAssinatura", obj.getSenhaProvedorDeAssinatura())
					.add("tokenProvedorDeAssinatura", obj.getTokenProvedorDeAssinatura())
					.add("tipoprovedorassinaturaenum", obj.getTipoProvedorAssinaturaEnum())
					.add("ambienteProvedorAssinaturaEnum", obj.getAmbienteProvedorAssinaturaEnum())
					.add("certificadoDigitalUnidadeEnsino", obj.getCertificadoDigitalUnidadeEnsinoVO())
					.add("senhaCertificadoDigitalUnidadeEnsino", obj.getSenhaCertificadoDigitalUnidadeEnsino())
					.add("utilizarAssinaturaUnidadeCertificadora", obj.getUtilizarAssinaturaUnidadeCertificadora())
					.add("certificadoDigitalUnidadeCertificadora", obj.getCertificadoDigitalUnidadeCertificadora())
					.add("senhaCertificadoDigitalUnidadeCertificadora", obj.getSenhaCertificadoDigitalUnidadeCertificadora())
					.add("habilitarIntegracaoCertisign", obj.getHabilitarIntegracaoCertisign())
					.add("habilitarIntegracaoImprensaOficial", obj.getHabilitarIntegracaoImprensaOficial())
					.add("ambienteImpresaOficialEnum", obj.getAmbienteImprensaOficialEnum())
					.add("tokenImprensaOficial", obj.getTokenImprensaOficial())
					.add("seloAssinaturaEletronica", obj.getSeloAssinaturaEletronicaVO())
					.add("habilitarIntegracaoTechCert", obj.getHabilitarIntegracaoTechCert())
//					.add("ambienteImpresaOficialEnumTechCert", obj.getAmbienteTechCertEnum())
//					.add("tokenImprensaOficialTechCert", obj.getTokenTechCert())
					.add("tokenImprensaOficialTechCertHomologacao", obj.getTokenTechCertHomologacao())
					.add("apikeyTechCert", obj.getApikeyTechCert())
					.add("tipoprovedorassinaturaTechCertenum", obj.getTipoProvedorAssinaturaTechCertEnum());
//			if (AmbienteEnum.HOMOLOGACAO == obj.getAmbienteTechCertEnum()) {
//				atributoPersistencia.add("urlIntegracaoImprensaOficialHomologacaoTechCert", obj.getUrlIntegracaoTechCertHomologacao());
//			}
//			if (AmbienteEnum.PRODUCAO == obj.getAmbienteTechCertEnum()) {
//				atributoPersistencia.add("urlIntegracaoImprensaOficialProducaoTechCert", obj.getUrlIntegracaoTechCertProducao());
//			}
			incluir(obj, "configuracaoGED", atributoPersistencia, usuarioVO);
			obj.getSeloAssinaturaEletronicaVO().setCodOrigem(obj.getCodigo());
			getFacadeFactory().getArquivoFacade().alterarCodigoOrigemArquivo(obj.getSeloAssinaturaEletronicaVO(), usuarioVO);
			getFacadeFactory().getConfiguracaoGedOrigemInterfaceFacade().incluirConfiguracaoGedOrigemVOs(obj, usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoGEDVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			validarDados(obj, usuarioVO);
			alterar(getIdEntidade(), true, usuarioVO);
			if (Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeEnsinoVO().getNome())) {
				if (!Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo())) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getCertificadoDigitalUnidadeEnsinoVO(), usuarioVO, configuracaoGeralSistemaVO);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getCertificadoDigitalUnidadeEnsinoVO(), usuarioVO, configuracaoGeralSistemaVO);
				}
			}
			if (Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeCertificadora().getNome())) {
				if (!Uteis.isAtributoPreenchido(obj.getCertificadoDigitalUnidadeCertificadora().getCodigo())) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getCertificadoDigitalUnidadeCertificadora(), usuarioVO, configuracaoGeralSistemaVO);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getCertificadoDigitalUnidadeCertificadora(), usuarioVO, configuracaoGeralSistemaVO);
				}
			}
			if (Uteis.isAtributoPreenchido(obj.getSeloAssinaturaEletronicaVO().getNome())) {
				if (!Uteis.isAtributoPreenchido(obj.getSeloAssinaturaEletronicaVO().getCodigo())) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getSeloAssinaturaEletronicaVO(), usuarioVO, configuracaoGeralSistemaVO);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getSeloAssinaturaEletronicaVO(), usuarioVO, configuracaoGeralSistemaVO);
				}
				
			}
			AtributoPersistencia atributoPersistencia = new AtributoPersistencia();
			atributoPersistencia
					.add("nome", obj.getNome())
					.add("usuarioProvedorDeAssinatura", obj.getUsuarioProvedorDeAssinatura())
					.add("senhaProvedorDeAssinatura", obj.getSenhaProvedorDeAssinatura())
					.add("tokenProvedorDeAssinatura", obj.getTokenProvedorDeAssinatura())
					.add("tipoprovedorassinaturaenum", obj.getTipoProvedorAssinaturaEnum())
					.add("ambienteProvedorAssinaturaEnum", obj.getAmbienteProvedorAssinaturaEnum())
					.add("certificadoDigitalUnidadeEnsino", obj.getCertificadoDigitalUnidadeEnsinoVO())
					.add("senhaCertificadoDigitalUnidadeEnsino", obj.getSenhaCertificadoDigitalUnidadeEnsino())
					.add("utilizarAssinaturaUnidadeCertificadora", obj.getUtilizarAssinaturaUnidadeCertificadora())
					.add("certificadoDigitalUnidadeCertificadora", obj.getCertificadoDigitalUnidadeCertificadora())
					.add("senhaCertificadoDigitalUnidadeCertificadora", obj.getSenhaCertificadoDigitalUnidadeCertificadora())
					.add("habilitarIntegracaoCertisign", obj.getHabilitarIntegracaoCertisign())
					.add("habilitarIntegracaoImprensaOficial", obj.getHabilitarIntegracaoImprensaOficial())
					.add("ambienteImpresaOficialEnum", obj.getAmbienteImprensaOficialEnum())
					.add("tokenImprensaOficial", obj.getTokenImprensaOficial())
					.add("urlIntegracaoImprensaOficialHomologacao", obj.getUrlIntegracaoImprensaOficialHomologacao())
					.add("urlIntegracaoImprensaOficialProducao", obj.getUrlIntegracaoImprensaOficialProducao())
					.add("seloAssinaturaEletronica", obj.getSeloAssinaturaEletronicaVO())
					.add("habilitarIntegracaoTechCert", obj.getHabilitarIntegracaoTechCert())
//					.add("ambienteImpresaOficialEnumTechCert", obj.getAmbienteTechCertEnum())
//					.add("tokenImprensaOficialTechCert", obj.getTokenTechCert())
					.add("tokenImprensaOficialTechCertHomologacao", obj.getTokenTechCertHomologacao())
					.add("apikeyTechCert", obj.getApikeyTechCert())
					.add("tipoprovedorassinaturaTechCertenum", obj.getTipoProvedorAssinaturaTechCertEnum());
//			if (AmbienteEnum.HOMOLOGACAO == obj.getAmbienteTechCertEnum()) {
//				atributoPersistencia.add("urlIntegracaoImprensaOficialHomologacaoTechCert", obj.getUrlIntegracaoTechCertHomologacao());
//			}
//			if (AmbienteEnum.PRODUCAO == obj.getAmbienteTechCertEnum()) {
//				atributoPersistencia.add("urlIntegracaoImprensaOficialProducaoTechCert", obj.getUrlIntegracaoTechCertProducao());
//			}
			alterar(obj, "configuracaoGED", atributoPersistencia, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
			getFacadeFactory().getConfiguracaoGedOrigemInterfaceFacade().alterarConfiguracaoGedOrigemVOs(obj, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoGEDVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM ConfiguracaoGED WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getArquivoFacade().excluir(obj.getCertificadoDigitalUnidadeEnsinoVO(), usuarioVO, configuracaoGeralSistemaVO);
			getFacadeFactory().getArquivoFacade().excluir(obj.getSeloAssinaturaEletronicaVO(), usuarioVO, configuracaoGeralSistemaVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacaoTokenProvedorDeAssinatura(ConfiguracaoGEDVO obj, UsuarioVO usuarioVO) {
		try {
			
		} catch (Exception e) {
			
		}
	}

	@Override
	public List<ConfiguracaoGEDVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoGED WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?)) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT configuracaoGED.codigo, configuracaoGED.nome, certificadoDigitalUnidadeEnsino, senhaCertificadoDigitalUnidadeEnsino, ");
		str.append(" configuracaoGED.provedorDeAssinaturaEnum, configuracaoGED.usuarioProvedorDeAssinatura, configuracaoGED.senhaProvedorDeAssinatura, ");
		str.append(" configuracaoGED.tokenProvedorDeAssinatura, configuracaoGED.tipoProvedorAssinaturaEnum, ");
		str.append(" configuracaoGED.ambienteProvedorAssinaturaEnum, ");
		str.append(" utilizarAssinaturaUnidadeCertificadora, certificadoDigitalUnidadeCertificadora, senhaCertificadoDigitalUnidadeCertificadora, ");
		str.append(" seloAssinaturaEletronica");
		str.append(" FROM configuracaoGED ");
		return str;
	}
	
	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT configuracaoGED.codigo, configuracaoGED.nome, certificadoDigitalUnidadeEnsino, senhaCertificadoDigitalUnidadeEnsino, ");
		str.append(" configuracaoGED.provedorDeAssinaturaEnum, configuracaoGED.usuarioProvedorDeAssinatura, configuracaoGED.senhaProvedorDeAssinatura, ");
		str.append(" configuracaoGED.tokenProvedorDeAssinatura, configuracaoGED.tipoProvedorAssinaturaEnum, ");
		str.append(" configuracaoGED.ambienteProvedorAssinaturaEnum,  ");
		
		str.append(" configuracaoGED.habilitarIntegracaoCertisign, configuracaoGED.habilitarIntegracaoImprensaOficial, ");
		str.append(" configuracaoGED.ambienteImpresaOficialEnum, configuracaoGED.tokenImprensaOficial, ");
		str.append(" configuracaoGED.urlIntegracaoImprensaOficialHomologacao, configuracaoGED.urlIntegracaoImprensaOficialProducao,  ");
		str.append(" configuracaoGED.habilitarIntegracaoTechCert, configuracaoGED.tokenImprensaOficialTechCert, ");
		str.append(" configuracaoGED.urlIntegracaoImprensaOficialHomologacaoTechCert, configuracaoGED.urlIntegracaoImprensaOficialProducaoTechCert, ");
		str.append(" configuracaoGED.tipoprovedorassinaturaTechCertenum, configuracaoGED.tokenImprensaOficialTechCertHomologacao, configuracaoGED.apikeyTechCert, ");
		str.append(" configuracaoGED.ambienteimpresaoficialenumtechcert, ");

		str.append(" utilizarAssinaturaUnidadeCertificadora, certificadoDigitalUnidadeCertificadora, senhaCertificadoDigitalUnidadeCertificadora, ");
		str.append(" seloAssinaturaEletronica ");
		str.append(" FROM configuracaoGED ");
		return str;
	}

	@Override
	public List<ConfiguracaoGEDVO> consultaRapidaPorCodigo(Integer codCidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE configuracaoGED.codigo = ");
		sqlStr.append(codCidade.intValue());
		sqlStr.append(" ORDER BY configuracaoGED.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<ConfiguracaoGEDVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(configuracaoGED.nome) ilike(sem_acentos(?)) ");
		sqlStr.append(" ORDER BY configuracaoGED.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<ConfiguracaoGEDVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<ConfiguracaoGEDVO> vetResultado = new ArrayList<ConfiguracaoGEDVO>(0);
		while (tabelaResultado.next()) {
			ConfiguracaoGEDVO obj = new ConfiguracaoGEDVO();
			montarDadosBasico(obj, tabelaResultado, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Override
	public void carregarDados(ConfiguracaoGEDVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((ConfiguracaoGEDVO) obj, NivelMontarDados.TODOS, usuario);
	}

	@Override
	public void carregarDados(ConfiguracaoGEDVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((ConfiguracaoGEDVO) obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((ConfiguracaoGEDVO) obj, resultado, usuario);
		}
	}
	
	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (configuracaoGED.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Configuração GED).");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (configuracaoGED.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Configuração GED).");
		}
		return tabelaResultado;
	}

	private void montarDadosBasico(ConfiguracaoGEDVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setUsuarioProvedorDeAssinatura(dadosSQL.getString("usuarioProvedorDeAssinatura"));
		obj.setSenhaProvedorDeAssinatura(dadosSQL.getString("senhaProvedorDeAssinatura"));
		obj.setTokenProvedorDeAssinatura(dadosSQL.getString("tokenProvedorDeAssinatura"));
		obj.setTipoProvedorAssinaturaEnum(TipoProvedorAssinaturaEnum.valueOf(dadosSQL.getString("tipoProvedorAssinaturaEnum")));
//		obj.setAmbienteProvedorAssinaturaEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteProvedorAssinaturaEnum")));
		obj.getCertificadoDigitalUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeEnsino"));
		if (!obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeEnsinoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		obj.setSenhaCertificadoDigitalUnidadeEnsino(dadosSQL.getString("senhaCertificadoDigitalUnidadeEnsino"));
		obj.setUtilizarAssinaturaUnidadeCertificadora(dadosSQL.getBoolean("utilizarAssinaturaUnidadeCertificadora"));
		
		obj.getCertificadoDigitalUnidadeCertificadora().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeCertificadora"));
		if (!obj.getCertificadoDigitalUnidadeCertificadora().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeCertificadora(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeCertificadora().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		obj.setSenhaCertificadoDigitalUnidadeCertificadora(dadosSQL.getString("senhaCertificadoDigitalUnidadeCertificadora"));
		obj.getSeloAssinaturaEletronicaVO().setCodigo(dadosSQL.getInt("seloAssinaturaEletronica"));
		getFacadeFactory().getConfiguracaoGedOrigemInterfaceFacade().carregarDadosConfiguracaoGedOrigemVOs(obj, usuarioVO);
		
	}
	
	private void montarDadosCompleto(ConfiguracaoGEDVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setUsuarioProvedorDeAssinatura(dadosSQL.getString("usuarioProvedorDeAssinatura"));
		obj.setSenhaProvedorDeAssinatura(dadosSQL.getString("senhaProvedorDeAssinatura"));
		obj.setTokenProvedorDeAssinatura(dadosSQL.getString("tokenProvedorDeAssinatura"));
//		obj.setAmbienteProvedorAssinaturaEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteProvedorAssinaturaEnum")));
		obj.getCertificadoDigitalUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeEnsino"));
		if (!obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeEnsinoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		obj.setSenhaCertificadoDigitalUnidadeEnsino(dadosSQL.getString("senhaCertificadoDigitalUnidadeEnsino"));
		obj.setUtilizarAssinaturaUnidadeCertificadora(dadosSQL.getBoolean("utilizarAssinaturaUnidadeCertificadora"));
		obj.getCertificadoDigitalUnidadeCertificadora().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeCertificadora"));
		if (!obj.getCertificadoDigitalUnidadeCertificadora().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeCertificadora(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeCertificadora().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		obj.setSenhaCertificadoDigitalUnidadeCertificadora(dadosSQL.getString("senhaCertificadoDigitalUnidadeCertificadora"));
		obj.getSeloAssinaturaEletronicaVO().setCodigo(dadosSQL.getInt("seloAssinaturaEletronica"));		
		if (!obj.getSeloAssinaturaEletronicaVO().getCodigo().equals(0)) {
			obj.setSeloAssinaturaEletronicaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getSeloAssinaturaEletronicaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		
		obj.setHabilitarIntegracaoCertisign(dadosSQL.getBoolean("habilitarIntegracaoCertisign"));
		obj.setHabilitarIntegracaoTechCert(dadosSQL.getBoolean("habilitarIntegracaoTechCert"));
		obj.setTokenTechCert(dadosSQL.getString("tokenImprensaOficialTechCert"));
		obj.setTokenTechCertHomologacao(dadosSQL.getString("tokenImprensaOficialTechCertHomologacao"));
		obj.setApikeyTechCert(dadosSQL.getString("apikeyTechCert"));
		obj.setUrlIntegracaoTechCertHomologacao(dadosSQL.getString("urlIntegracaoImprensaOficialHomologacaoTechCert"));
		obj.setUrlIntegracaoTechCertProducao(dadosSQL.getString("urlIntegracaoImprensaOficialProducaoTechCert"));
		obj.setTipoProvedorAssinaturaTechCertEnum(TipoProvedorAssinaturaEnum.valueOf(dadosSQL.getString("tipoprovedorassinaturaTechCertenum")));
//		obj.setAmbienteImprensaOficialEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteImpresaOficialEnum")));
//		obj.setAmbienteTechCertEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteimpresaoficialenumtechcert")));
		obj.setTokenImprensaOficial(dadosSQL.getString("tokenImprensaOficial"));
		obj.setUrlIntegracaoImprensaOficialHomologacao(dadosSQL.getString("urlIntegracaoImprensaOficialHomologacao"));
		obj.setUrlIntegracaoImprensaOficialProducao(dadosSQL.getString("urlIntegracaoImprensaOficialProducao"));
		
		getFacadeFactory().getConfiguracaoGedOrigemInterfaceFacade().carregarDadosConfiguracaoGedOrigemVOs(obj, usuarioVO);
	}

	public  List<ConfiguracaoGEDVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ConfiguracaoGEDVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return vetResultado;
	}

	public  ConfiguracaoGEDVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoGEDVO obj = new ConfiguracaoGEDVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setUsuarioProvedorDeAssinatura(dadosSQL.getString("usuarioProvedorDeAssinatura"));
		obj.setSenhaProvedorDeAssinatura(dadosSQL.getString("senhaProvedorDeAssinatura"));
		obj.setTokenProvedorDeAssinatura(dadosSQL.getString("tokenProvedorDeAssinatura"));
		obj.setTipoProvedorAssinaturaEnum(TipoProvedorAssinaturaEnum.valueOf(dadosSQL.getString("tipoProvedorAssinaturaEnum")));
//		obj.setAmbienteProvedorAssinaturaEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteProvedorAssinaturaEnum")));
		obj.getCertificadoDigitalUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeEnsino"));
		if (!obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeEnsinoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.setSenhaCertificadoDigitalUnidadeEnsino(dadosSQL.getString("senhaCertificadoDigitalUnidadeEnsino"));
		obj.setUtilizarAssinaturaUnidadeCertificadora(dadosSQL.getBoolean("utilizarAssinaturaUnidadeCertificadora"));
		obj.getCertificadoDigitalUnidadeCertificadora().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeCertificadora"));
		if (!obj.getCertificadoDigitalUnidadeCertificadora().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeCertificadora(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeCertificadora().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.setSenhaCertificadoDigitalUnidadeCertificadora(dadosSQL.getString("senhaCertificadoDigitalUnidadeCertificadora"));
		obj.getSeloAssinaturaEletronicaVO().setCodigo(dadosSQL.getInt("seloAssinaturaEletronica"));
		if (!obj.getSeloAssinaturaEletronicaVO().getCodigo().equals(0)) {
			obj.setSeloAssinaturaEletronicaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getSeloAssinaturaEletronicaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		if (!obj.getSeloAssinaturaEletronicaVO().getCodigo().equals(0)) {
			obj.setSeloAssinaturaEletronicaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getSeloAssinaturaEletronicaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		
		obj.setHabilitarIntegracaoCertisign(dadosSQL.getBoolean("habilitarIntegracaoCertisign"));
		obj.setHabilitarIntegracaoTechCert(dadosSQL.getBoolean("habilitarIntegracaoTechCert"));
		obj.setTokenTechCert(dadosSQL.getString("tokenImprensaOficialTechCert"));
		obj.setTokenTechCertHomologacao(dadosSQL.getString("tokenImprensaOficialTechCertHomologacao"));
		obj.setApikeyTechCert(dadosSQL.getString("apikeyTechCert"));
		obj.setUrlIntegracaoTechCertHomologacao(dadosSQL.getString("urlIntegracaoImprensaOficialHomologacaoTechCert"));
		obj.setUrlIntegracaoTechCertProducao(dadosSQL.getString("urlIntegracaoImprensaOficialProducaoTechCert"));
		obj.setTipoProvedorAssinaturaTechCertEnum(TipoProvedorAssinaturaEnum.valueOf(dadosSQL.getString("tipoprovedorassinaturaTechCertenum")));
		obj.setHabilitarIntegracaoImprensaOficial(dadosSQL.getBoolean("habilitarIntegracaoImprensaOficial"));
//		obj.setAmbienteImprensaOficialEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteImpresaOficialEnum")));
//		obj.setAmbienteTechCertEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteImpresaOficialEnumTechCert")));
		obj.setTokenImprensaOficial(dadosSQL.getString("tokenImprensaOficial"));
		obj.setUrlIntegracaoImprensaOficialHomologacao(dadosSQL.getString("urlIntegracaoImprensaOficialHomologacao"));
		obj.setUrlIntegracaoImprensaOficialProducao(dadosSQL.getString("urlIntegracaoImprensaOficialProducao"));
		
		getFacadeFactory().getConfiguracaoGedOrigemInterfaceFacade().carregarDadosConfiguracaoGedOrigemVOs(obj, usuario);
		
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}
	
	@Override
	public ConfiguracaoGEDVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ConfiguracaoGED WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		return new ConfiguracaoGEDVO();
	}

	public ConfiguracaoGEDVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getConfiguracaoGEDVO(codigoPrm);
	}

	public static String getIdEntidade() {
		return ConfiguracaoGED.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoGED.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarSeloAssinaturaEletronica(ConfiguracaoGEDVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			String sql = "UPDATE ConfiguracaoGED set seloassinaturaeletronica = null WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			
			getFacadeFactory().getArquivoFacade().excluir(obj.getSeloAssinaturaEletronicaVO(), usuario, configuracaoGeralSistemaVO);
			obj.setSeloAssinaturaEletronicaVO(new ArquivoVO());
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Override
	public ConfiguracaoGEDVO consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {	
		return getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsino, usuario);		
	}
	
	@Override
	public ConfiguracaoGEDVO consultarPorUnidadeEnsinoUnica(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {			
		consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select ConfiguracaoGED.codigo from ConfiguracaoGED ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.configuracaoGED = configuracaoGED.codigo ");
		sb.append(" where unidadeEnsino.codigo = ? ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { unidadeEnsino });
		if (tabelaResultado.next()) {
			return getAplicacaoControle().getConfiguracaoGEDVO(tabelaResultado.getInt("codigo"));
		}
		return new ConfiguracaoGEDVO();
	}
	
	@Override
	public Map<Integer, ConfiguracaoGEDVO> consultarConfiguracaoGED(UsuarioVO usuario) throws Exception {
		Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGEDPorUnidadeEnsino = new HashMap<Integer, ConfiguracaoGEDVO>();
		StringBuilder sql = new StringBuilder();
		sql.append("select unidadeensino.codigo as codUnidadeensino, configuracaoged.*  from configuracaoged inner join unidadeensino on unidadeensino.configuracaoged = configuracaoged.codigo  ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
		while (tabelaResultado.next()) {
			montarDados(tabelaResultado, mapConfiguracaoGEDPorUnidadeEnsino,usuario);
		}
		return mapConfiguracaoGEDPorUnidadeEnsino;
	}
	
	
	private void montarDados(SqlRowSet dadosSQL, Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGEDPorUnidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoGEDVO obj = new ConfiguracaoGEDVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setUsuarioProvedorDeAssinatura(dadosSQL.getString("usuarioProvedorDeAssinatura"));
		obj.setSenhaProvedorDeAssinatura(dadosSQL.getString("senhaProvedorDeAssinatura"));
		obj.setTokenProvedorDeAssinatura(dadosSQL.getString("tokenProvedorDeAssinatura"));
//		obj.setAmbienteProvedorAssinaturaEnum(AmbienteEnum.valueOf(dadosSQL.getString("ambienteProvedorAssinaturaEnum")));
		obj.getCertificadoDigitalUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeEnsino"));
		obj.setSenhaCertificadoDigitalUnidadeEnsino(dadosSQL.getString("senhaCertificadoDigitalUnidadeEnsino"));
		obj.setUtilizarAssinaturaUnidadeCertificadora(dadosSQL.getBoolean("utilizarAssinaturaUnidadeCertificadora"));
		obj.getCertificadoDigitalUnidadeCertificadora().setCodigo(dadosSQL.getInt("certificadoDigitalUnidadeCertificadora"));
		obj.setSenhaCertificadoDigitalUnidadeCertificadora(dadosSQL.getString("senhaCertificadoDigitalUnidadeCertificadora"));
		obj.getSeloAssinaturaEletronicaVO().setCodigo(dadosSQL.getInt("seloAssinaturaEletronica"));
	
		obj.setNovoObj(Boolean.FALSE);
		if (!obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeEnsinoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		if (!obj.getCertificadoDigitalUnidadeCertificadora().getCodigo().equals(0)) {
			obj.setCertificadoDigitalUnidadeCertificadora(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoDigitalUnidadeCertificadora().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		if (!obj.getSeloAssinaturaEletronicaVO().getCodigo().equals(0)) {
			obj.setSeloAssinaturaEletronicaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getSeloAssinaturaEletronicaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		getFacadeFactory().getConfiguracaoGedOrigemInterfaceFacade().carregarDadosConfiguracaoGedOrigemVOs(obj, usuarioVO);
		mapConfiguracaoGEDPorUnidadeEnsino.put(dadosSQL.getInt("codUnidadeensino"), obj);
	}

	@Override
	public Boolean consultarExisteConfiguracaoGedComAtaColacaoGrau(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cg.codigo FROM ConfiguracaoGED  ");
		sql.append("INNER JOIN configuracaogedorigem cgo ON cg.codigo = cgo.configuracaoged ");
		sql.append("WHERE (cgo.tipoorigemdocumentoassinado = 'ATA_COLACAO_GRAU' and cgo.assinardocumento) ORDER BY nome LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	
	}

	@Override
	public Map<Boolean, Boolean> consultarPermitirAlunoAssinarColacaoGrau(UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cgo.permitiralunoassinardigitalmente as \"cgo.permitiralunoassinardigitalmente\" FROM ConfiguracaoGED cg ");
		sql.append("INNER JOIN configuracaogedorigem cgo ON cg.codigo = cgo.configuracaoged ");
		sql.append("WHERE (cgo.tipoorigemdocumentoassinado = 'ATA_COLACAO_GRAU' and cgo.assinardocumento) ORDER BY nome LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Map<Boolean, Boolean> lista = new HashMap<Boolean, Boolean>();
		if (tabelaResultado.next()) {
			lista.put(true, tabelaResultado.getBoolean("cgo.permitiralunoassinardigitalmente"));
		}
		return lista;
	}
	
}
