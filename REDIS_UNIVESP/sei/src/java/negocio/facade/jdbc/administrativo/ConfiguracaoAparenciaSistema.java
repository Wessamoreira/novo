package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.ConfiguracaoAparenciaSistemaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoAparenciaSistema extends ControleAcesso
		implements ConfiguracaoAparenciaSistemaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2909642186404235771L;
	protected static String idEntidade = "ConfiguracaoAparenciaSistema";

	public static String getIdEntidade() {
		if (ConfiguracaoAparenciaSistema.idEntidade == null) {
			ConfiguracaoAparenciaSistema.idEntidade = "ConfiguracaoAparenciaSistema";
		}
		return ConfiguracaoAparenciaSistema.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoAparenciaSistema.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {

		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,
				usuarioVO);
		if (configuracaoAparenciaSistemaVO.getCaminhoBaseImagemTopo()
				.equals(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP)
				&& !configuracaoAparenciaSistemaVO.getNomeImagemTopo().trim().isEmpty()) {
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(
					PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(),
					PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue(),
					configuracaoAparenciaSistemaVO.getNomeImagemTopo(),
					configuracaoAparenciaSistemaVO.getNomeImagemTopo(), configuracaoGeralSistemaVO, false);
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue() + File.separator
					+ configuracaoAparenciaSistemaVO.getNomeImagemTopo()));
			configuracaoAparenciaSistemaVO.setCaminhoBaseImagemTopo(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO);
		}

		if (configuracaoAparenciaSistemaVO.isNovoObj()) {
			incluir(configuracaoAparenciaSistemaVO, usuarioVO);
		} else {
			alterar(configuracaoAparenciaSistemaVO, usuarioVO);
		}
		excluirBanner(configuracaoAparenciaSistemaVO, usuarioVO);
		persistirBanner(configuracaoAparenciaSistemaVO, usuarioVO);
		realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
		realizarGeracaoScriptBannerLogin(configuracaoAparenciaSistemaVO);
		if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getCodigo())
				&& getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getCodigo()
						.equals(configuracaoAparenciaSistemaVO.getCodigo())) {
			getAplicacaoControle().setConfiguracaoAparenciaSistemaVO(
					(ConfiguracaoAparenciaSistemaVO) Uteis.clonar(configuracaoAparenciaSistemaVO));
		}
		configuracaoAparenciaSistemaVO.setBannerLoginExcluirVOs(new ArrayList<ArquivoVO>());
		if (Uteis.isAtributoPreenchido(configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior()) && !configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior().contentEquals(configuracaoAparenciaSistemaVO.getNomeImagemTopo())) {
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator
					+ configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior()));
			configuracaoAparenciaSistemaVO.setNomeImagemTopoAnterior(null);
			configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior();
		}		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {
		super.excluir(getIdEntidade(), true, usuarioVO);
		Integer codigo = configuracaoAparenciaSistemaVO.getCodigo();
		configuracaoAparenciaSistemaVO.getBannerLoginExcluirVOs()
				.addAll(configuracaoAparenciaSistemaVO.getBannerLoginVOs());
		configuracaoAparenciaSistemaVO.setBannerLoginVOs(new ArrayList<ArquivoVO>(0));
		excluirBanner(configuracaoAparenciaSistemaVO, usuarioVO);
		getConexao().getJdbcTemplate()
				.update("delete from configuracaoAparenciaSistema where codigo = ? "
						+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO),
						configuracaoAparenciaSistemaVO.getCodigo());
		if (Uteis.isAtributoPreenchido(configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior())) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle()
					.getConfiguracaoGeralSistemaVO(0, usuarioVO);
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator
					+ configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior()));
			configuracaoAparenciaSistemaVO.setNomeImagemTopoAnterior(null);
			configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior();
		}
		configuracaoAparenciaSistemaVO = new ConfiguracaoAparenciaSistemaVO();
		realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
		realizarGeracaoScriptBannerLogin(configuracaoAparenciaSistemaVO);
		if (getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getCodigo().equals(codigo)) {
			getAplicacaoControle().setConfiguracaoAparenciaSistemaVO(configuracaoAparenciaSistemaVO);
		}

	}

	@Override
	public void adicionarBanner(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, FileUploadEvent upload,
			UsuarioVO usuarioVO) throws Exception {
		if (configuracaoAparenciaSistemaVO.getNovoObj()) {
			super.incluir(getIdEntidade(), true, usuarioVO);
		} else {
			super.alterar(getIdEntidade(), true, usuarioVO);
		}
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,
				null);
		ArquivoVO arquivoVO = new ArquivoVO();
		arquivoVO.setNome(
				new Date().getTime() + "_" + usuarioVO.getCodigo() + "." + upload.getUploadedFile().getFileExtension());
		arquivoVO.setDescricao("");
		arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.BANNER_LOGIN_TMP);
		arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.BANNER_LOGIN_TMP.getValue());
		arquivoVO.setExtensao(upload.getUploadedFile().getFileExtension());
		arquivoVO.setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
		arquivoVO.getResponsavelUpload().setCodigo(usuarioVO.getCodigo());
		arquivoVO.getResponsavelUpload().setNome(usuarioVO.getNome());
		arquivoVO.setDataUpload(new Date());
		arquivoVO.setManterDisponibilizacao(true);
		ArquivoHelper.salvarArquivoNaPastaTemp(upload, arquivoVO.getNome(),
				PastaBaseArquivoEnum.BANNER_LOGIN_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
		configuracaoAparenciaSistemaVO.getBannerLoginVOs().add(arquivoVO);
		realizarGeracaoScriptBannerLogin(configuracaoAparenciaSistemaVO);

	}

	@Override
	public void removerBanner(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, ArquivoVO arquivoVO,
			UsuarioVO usuarioVO) throws Exception {
		if (configuracaoAparenciaSistemaVO.getNovoObj()) {
			super.incluir(getIdEntidade(), true, usuarioVO);
		} else {
			super.alterar(getIdEntidade(), true, usuarioVO);
		}
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,
				null);
		if (arquivoVO.getCodigo() > 0) {
			configuracaoAparenciaSistemaVO.getBannerLoginExcluirVOs().add(arquivoVO);
		} else {
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome()));
		}
		configuracaoAparenciaSistemaVO.getBannerLoginVOs().removeIf(t -> t.getNome().equals(arquivoVO.getNome()));
		realizarGeracaoScriptBannerLogin(configuracaoAparenciaSistemaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void excluirBanner(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {
		for (ArquivoVO arquivoVO : configuracaoAparenciaSistemaVO.getBannerLoginExcluirVOs()) {
			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuarioVO,
					getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistirBanner(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {
		List<ArquivoVO> arquivosIncluido = new ArrayList<ArquivoVO>(0);
		try {
			for (ArquivoVO arquivoVO : configuracaoAparenciaSistemaVO.getBannerLoginVOs()) {
				if (arquivoVO.getCodigo().equals(0)) {
					arquivoVO.setCodOrigem(configuracaoAparenciaSistemaVO.getCodigo());
					arquivoVO.setOrigem("BALO");
					getFacadeFactory().getArquivoFacade().incluir(arquivoVO, usuarioVO,
							getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO));
					arquivosIncluido.add(arquivoVO);
				} else {
					getFacadeFactory().getArquivoFacade().alterarDescricaoArquivo(arquivoVO.getCodigo(),
							arquivoVO.getDescricao(), usuarioVO);
				}
			}
		} catch (Exception e) {
			for (ArquivoVO arquivoVO : arquivosIncluido) {
				arquivoVO.setCodigo(0);
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {
		super.incluir(ConfiguracaoAparenciaSistema.getIdEntidade(), true, usuarioVO);
		incluir(configuracaoAparenciaSistemaVO, "configuracaoAparenciaSistema",
				new AtributoPersistencia().add("nome", configuracaoAparenciaSistemaVO.getNome())
						.add("corBackAreaConteudo", configuracaoAparenciaSistemaVO.getCorBackAreaConteudo())
						.add("corBackBannerLogin", configuracaoAparenciaSistemaVO.getCorBackBannerLogin())
						.add("corBackHeaderDashboard", configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard())
						.add("corBackIndex", configuracaoAparenciaSistemaVO.getCorBackIndex())
						.add("corBackLogin", configuracaoAparenciaSistemaVO.getCorBackLogin())
						.add("corBackMenu", configuracaoAparenciaSistemaVO.getCorBackMenu())
						.add("corBackTopo", configuracaoAparenciaSistemaVO.getCorBackTopo())
						.add("corFonteIndex", configuracaoAparenciaSistemaVO.getCorFonteIndex())
						.add("corFonteLogin", configuracaoAparenciaSistemaVO.getCorFonteLogin())
						.add("corFonteMenu", configuracaoAparenciaSistemaVO.getCorFonteMenu())
						.add("corFonteTopo", configuracaoAparenciaSistemaVO.getCorFonteTopo())
						.add("corFonteTituloFormulario", configuracaoAparenciaSistemaVO.getCorFonteTituloFormulario())
						.add("corFonteTituloCampos", configuracaoAparenciaSistemaVO.getCorFonteTituloCampos())
						.add("corBackCampos", configuracaoAparenciaSistemaVO.getCorBackCampos())
						.add("corFonteHeaderTabela", configuracaoAparenciaSistemaVO.getCorFonteHeaderTabela())
						.add("corBackHeaderTabela", configuracaoAparenciaSistemaVO.getCorBackHeaderTabela())
						.add("corFonteLinhaTabela", configuracaoAparenciaSistemaVO.getCorFonteLinhaTabela())
						.add("corBackLinhaTabela", configuracaoAparenciaSistemaVO.getCorBackLinhaTabela())
						.add("corFonteHeaderAbas", configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas())
						.add("corFonteHeaderAbas", configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas())
						.add("corFontHeaderDashboard", configuracaoAparenciaSistemaVO.getCorFontHeaderDashboard())
						.add("corBackGeral", configuracaoAparenciaSistemaVO.getCorBackGeral())
						.add("tempoBanner", configuracaoAparenciaSistemaVO.getTempoBanner())
						.add("LinkVideosBanner", configuracaoAparenciaSistemaVO.getLinkVideosBanner())
						.add("disponibilizarUsuario", configuracaoAparenciaSistemaVO.getDisponibilizarUsuario())
						.add("css", configuracaoAparenciaSistemaVO.getCss())
						.add("nomeImagemTopo", configuracaoAparenciaSistemaVO.getNomeImagemTopo())
						.add("caminhoBaseImagemTopo",
								configuracaoAparenciaSistemaVO.getCaminhoBaseImagemTopo().toString()),
				usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {
		super.alterar(ConfiguracaoAparenciaSistema.idEntidade, true, usuarioVO);
		alterar(configuracaoAparenciaSistemaVO, "configuracaoAparenciaSistema", new AtributoPersistencia()
				.add("nome", configuracaoAparenciaSistemaVO.getNome())
				.add("corBackAreaConteudo", configuracaoAparenciaSistemaVO.getCorBackAreaConteudo())
				.add("corBackBannerLogin", configuracaoAparenciaSistemaVO.getCorBackBannerLogin())
				.add("corBackHeaderDashboard", configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard())
				.add("corBackIndex", configuracaoAparenciaSistemaVO.getCorBackIndex())
				.add("corBackLogin", configuracaoAparenciaSistemaVO.getCorBackLogin())
				.add("corBackMenu", configuracaoAparenciaSistemaVO.getCorBackMenu())
				.add("corBackTopo", configuracaoAparenciaSistemaVO.getCorBackTopo())
				.add("corFonteIndex", configuracaoAparenciaSistemaVO.getCorFonteIndex())
				.add("corFonteLogin", configuracaoAparenciaSistemaVO.getCorFonteLogin())
				.add("corFonteMenu", configuracaoAparenciaSistemaVO.getCorFonteMenu())
				.add("corFonteTopo", configuracaoAparenciaSistemaVO.getCorFonteTopo())
				.add("corFonteTituloFormulario", configuracaoAparenciaSistemaVO.getCorFonteTituloFormulario())
				.add("corFonteTituloCampos", configuracaoAparenciaSistemaVO.getCorFonteTituloCampos())
				.add("corBackCampos", configuracaoAparenciaSistemaVO.getCorBackCampos())
				.add("corFonteHeaderTabela", configuracaoAparenciaSistemaVO.getCorFonteHeaderTabela())
				.add("corBackHeaderTabela", configuracaoAparenciaSistemaVO.getCorBackHeaderTabela())
				.add("corFonteLinhaTabela", configuracaoAparenciaSistemaVO.getCorFonteLinhaTabela())
				.add("corBackLinhaTabela", configuracaoAparenciaSistemaVO.getCorBackLinhaTabela())
				.add("corFonteHeaderAbas", configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas())
				.add("corBackHeaderAbas", configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
				.add("corBackGeral", configuracaoAparenciaSistemaVO.getCorBackGeral())
				.add("corFontHeaderDashboard", configuracaoAparenciaSistemaVO.getCorFontHeaderDashboard())
				.add("tempoBanner", configuracaoAparenciaSistemaVO.getTempoBanner())
				.add("LinkVideosBanner", configuracaoAparenciaSistemaVO.getLinkVideosBanner())
				.add("disponibilizarUsuario", configuracaoAparenciaSistemaVO.getDisponibilizarUsuario())
				.add("css", configuracaoAparenciaSistemaVO.getCss())
				.add("nomeImagemTopo", configuracaoAparenciaSistemaVO.getNomeImagemTopo())
				.add("caminhoBaseImagemTopo", configuracaoAparenciaSistemaVO.getCaminhoBaseImagemTopo().toString()),
				new AtributoPersistencia().add("codigo", configuracaoAparenciaSistemaVO.getCodigo()), usuarioVO);
	}

	@Override
	public void realizarGeracaoScriptBannerLogin(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO)
			throws Exception {
		StringBuilder script = new StringBuilder("");
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,null);
		for (ArquivoVO banner : configuracaoAparenciaSistemaVO.getBannerLoginVOs()) {
			script.append("<div class=\"col-md-12 pn\" style=\"max-height:358px; min-height:358px;height:358px\" >");
//			if (!banner.getDescricao().isEmpty()) {
//				script.append("<a class=\"linkBanner\"  style=\"max-height:358px; min-height:358px;height:358px; width:100%;display:flex;cursor:pointer;z-index:200\" href=\"").append(banner.getDescricao())
//						.append("\" target=\"_blank\" value=\""+banner.getDescricao()+"\" >");
//			}
			script.append(" <img src=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/")
					.append(banner.getPastaBaseArquivo().trim()).append("/").append(banner.getNome().trim())
					.append("\" width=\"100%\"  style=\"max-height:358px; min-height:358px;height:358px;z-index:100;cursor:pointer\" ");
			if (!banner.getDescricao().isEmpty()) {
				script.append(" onclick=\"window.open('").append(banner.getDescricao()).append("', '_blank_')\" ");
			}
			script.append(" />");
//			if (!banner.getDescricao().isEmpty()) {
//				script.append("</a>");
//			}
			script.append("</div>");
		}
		String[] urls = configuracaoAparenciaSistemaVO.getLinkVideosBanner().split(";");
		for (String urlvideo : urls) {
			if (!urlvideo.trim().isEmpty()) {

				script.append("<div class=\"col-md-12 pn\" style=\"max-height:358px; min-height:358px;height:358px\">");
				if (urlvideo.contains("<iframe") || urlvideo.contains("<object") || urlvideo.contains("<OBJECT")) {
					script.append(urlvideo);
				} else if (urlvideo.contains("youtube") && urlvideo.indexOf("v=") > 0) {
					if (urlvideo.contains("&")) {
						urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2, urlvideo.indexOf("&"));
					} else {
						urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2);
					}
					script.append(
							"<iframe wmode=\"transparent\" style=\"z-index:1;max-height:358px; min-height:358px;height:358px\"   width=\"100%\" src=\"https://www.youtube.com/embed/")
							.append(urlvideo)
							.append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else if (urlvideo.contains("youtu.be")) {
					urlvideo = urlvideo.substring(urlvideo.lastIndexOf("/") + 1);
					script.append(
							"<iframe wmode=\"transparent\" style=\"z-index:1;max-height:358px; min-height:358px;height:358px\"   width=\"100%\" src=\"https://www.youtube.com/embed/")
							.append(urlvideo)
							.append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else if (!urlvideo.contains("http") && !urlvideo.contains("youtu") && !urlvideo.contains("www")) {
					script.append(
							"<iframe wmode=\"transparent\" style=\"z-index:1;max-height:358px; min-height:358px;height:358px\"  width=\"100%\" src=\"https://www.youtube.com/embed/")
							.append(urlvideo)
							.append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else {
					script.append(
							"<iframe wmode=\"transparent\" style=\"z-index:1;max-height:358px; min-height:358px;height:358px\"   width=\"100%\" src=\"")
							.append(urlvideo)
							.append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				}

				script.append("</div>");
			}
		}
		configuracaoAparenciaSistemaVO.setScriptBannerGerado(script.toString());
	}

	@Override
	public void realizarGeracaoScriptCss(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO) {
		StringBuilder css = new StringBuilder("");
		if (!configuracaoAparenciaSistemaVO.getCorBackAreaConteudo().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackAreaConteudo().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackAreaConteudo().contains("#")) {
				css.append(".tabconteudo{background-color:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackAreaConteudo()).append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackAreaConteudo())
						.append(" !important}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackAreaConteudo().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackAreaConteudo().contains("#")) {
				css.append(".tabconteudo{background-color:")
						.append(configuracaoAparenciaSistemaVO.getCorBackAreaConteudo()).append(" !important;");
				css.append("border-color:").append(configuracaoAparenciaSistemaVO.getCorBackAreaConteudo())
						.append(" !important}");
			} else {
				css.append(".tabconteudo{background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackAreaConteudo()).append(" !important}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackGeral().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackGeral().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackGeral().contains("#")) {
				css.append("#content-wrapper{background-color:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackGeral()).append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackGeral())
						.append(" !important}");
				css.append(".backGeral{background-color:#").append(configuracaoAparenciaSistemaVO.getCorBackGeral())
						.append(" !important;}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackGeral().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackGeral().contains("#")) {
				css.append("#content-wrapper{background-color:")
						.append(configuracaoAparenciaSistemaVO.getCorBackGeral()).append(" !important;");
				css.append("border-color:").append(configuracaoAparenciaSistemaVO.getCorBackGeral())
						.append(" !important}");
				css.append(".backGeral{background-color:").append(configuracaoAparenciaSistemaVO.getCorBackGeral())
						.append(" !important;}");
			} else {
				css.append("#content-wrapper{background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackGeral()).append(" !important;}");
				css.append(".backGeral{background-image:").append(configuracaoAparenciaSistemaVO.getCorBackGeral())
						.append(" !important;}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackBannerLogin().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackBannerLogin().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackBannerLogin().contains("#")) {
				css.append(".backBannerLogin{background-color:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackBannerLogin()).append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackBannerLogin())
						.append(" !important}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackBannerLogin().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackBannerLogin().contains("#")) {
				css.append(".backBannerLogin{background-color:")
						.append(configuracaoAparenciaSistemaVO.getCorBackBannerLogin()).append(" !important;");
				css.append("border-color:").append(configuracaoAparenciaSistemaVO.getCorBackBannerLogin())
						.append(" !important}");
			} else {
				css.append(".backBannerLogin{background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackBannerLogin()).append(" !important}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard().contains("#")) {
				css.append(".card-header{background-color:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard()).append(" !important;");
				css.append("border-bootom-color:#").append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard())
						.append(" !important}");
				css.append(".rf-p-hdr{background-color:#")
				.append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard()).append(" !important;");
				css.append("border-bootom-color:#").append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard())
				.append(" !important}");
				
							
			} else if (!configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard().contains("#")) {
				css.append(".card-header{background-color:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard()).append(" !important; ");
				css.append("border-bootom-color:#").append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard())
						.append(" !important}");
				css.append(".rf-p-hdr{background-color:")
				.append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard()).append(" !important; ");
				css.append("border-bootom-color:#").append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard())
				.append(" !important}");
			} else {
				css.append(".card-header{background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard()).append(" !important}");
				css.append(".rf-p-hdr{background-image:")
				.append(configuracaoAparenciaSistemaVO.getCorBackHeaderDashboard()).append(" !important}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackIndex().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackIndex().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackIndex().contains("#")) {
				css.append(".backIndex{background-color:#").append(configuracaoAparenciaSistemaVO.getCorBackIndex())
						.append(" !important;");

				css.append(" border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackIndex())
						.append(" !important}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackIndex().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackIndex().contains("#")) {
				css.append(".backIndex{background-color:").append(configuracaoAparenciaSistemaVO.getCorBackIndex())
						.append(" !important;");

				css.append(" border-color:").append(configuracaoAparenciaSistemaVO.getCorBackIndex())
						.append(" !important}");
			} else {
				css.append(".backIndex{background-image:").append(configuracaoAparenciaSistemaVO.getCorBackIndex())
						.append(" !important;}");

			}

		}
		if (!configuracaoAparenciaSistemaVO.getCorBackLogin().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackLogin().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackLogin().contains("#")) {
				css.append(".backLogin{background-color:#").append(configuracaoAparenciaSistemaVO.getCorBackLogin())
						.append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackLogin())
						.append(" !important}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackLogin().contains("gradient")) {
				css.append(".backLogin{background-color:").append(configuracaoAparenciaSistemaVO.getCorBackLogin())
						.append(" !important;");
				css.append("border-color:").append(configuracaoAparenciaSistemaVO.getCorBackLogin())
						.append(" !important}");
			} else {
				css.append(".backLogin{background-image:").append(configuracaoAparenciaSistemaVO.getCorBackLogin())
						.append(" ;}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackMenu().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackMenu().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackMenu().contains("#")) {
				css.append(".backMenu{background-color:#").append(configuracaoAparenciaSistemaVO.getCorBackMenu())
						.append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackMenu())
						.append(" !important}");
			}
			if (!configuracaoAparenciaSistemaVO.getCorBackMenu().contains("gradient")) {
				css.append(".backMenu{background-color:#").append(configuracaoAparenciaSistemaVO.getCorBackMenu())
						.append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackMenu())
						.append(" !important}");
			} else {
				css.append(".backMenu{background-image:").append(configuracaoAparenciaSistemaVO.getCorBackMenu())
						.append(" !important;}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackTopo().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackTopo().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackTopo().contains("#")) {
				css.append(".backTopo{background-color:#").append(configuracaoAparenciaSistemaVO.getCorBackTopo())
						.append(" !important;");
				css.append("border-color:#").append(configuracaoAparenciaSistemaVO.getCorBackTopo())
						.append(" !important}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackTopo().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackTopo().contains("#")) {
				css.append(".backTopo{background-color:").append(configuracaoAparenciaSistemaVO.getCorBackTopo())
						.append(" !important;");
				css.append("border-color:").append(configuracaoAparenciaSistemaVO.getCorBackTopo())
						.append(" !important}");
			} else {
				css.append(".backTopo{background-image:").append(configuracaoAparenciaSistemaVO.getCorBackTopo())
						.append(" !important;}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteIndex().isEmpty()) {
			css.append(".fonteIndex{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteIndex())
					.append(" !important}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorFontHeaderDashboard().isEmpty()) {
			css.append(".fonteHeaderDashboard{color:#")
					.append(configuracaoAparenciaSistemaVO.getCorFontHeaderDashboard()).append(" !important}");
			css.append(".rf-p-hdr{color:#")
			.append(configuracaoAparenciaSistemaVO.getCorFontHeaderDashboard()).append(" !important}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteLogin().isEmpty()) {
			css.append(".fonteLogin{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteLogin())
					.append(" !important;");
			css.append("-webkit-text-fill-color:#").append(configuracaoAparenciaSistemaVO.getCorFonteLogin())
					.append(" !important}");
			css.append(".borderInputLogin{border-bottom:1px solid #")
					.append(configuracaoAparenciaSistemaVO.getCorFonteLogin()).append(" !important}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteTopo().isEmpty()) {
			css.append(".fonteTopo{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteTopo())
					.append(" !important}");
		}
		
		if (!configuracaoAparenciaSistemaVO.getCorBackHeaderTabela().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackHeaderTabela().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackHeaderTabela().contains("#")) {
				css.append(".rf-dt-thd {background-color:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
				css.append(".rf-dt-tft {background-color:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
				css.append(".rf-edt-hdr-c {background-color:#")
				.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
			} else if (!configuracaoAparenciaSistemaVO.getCorBackHeaderTabela().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackTopo().contains("#")) {
				css.append(".rf-dt-thd {background-color:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
				css.append(".rf-dt-tft {background-color:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
				css.append(".rf-edt-hdr-c {background-color:")
				.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
			} else {
				css.append(".rf-dt-thd {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
				css.append(".rf-dt-tft {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
				css.append(".rf-edt-hdr-c {background-image:")
				.append(configuracaoAparenciaSistemaVO.getCorBackHeaderTabela()).append(" !important}");
			}
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteHeaderTabela().isEmpty()) {
			css.append(".rf-dt-shdr-c {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteHeaderTabela())
					.append(" !important}");
			css.append(".rf-dt-hdr-c {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteHeaderTabela())
			.append(" !important}");
			css.append(".rf-edt-c-cnt {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteHeaderTabela())
			.append(" !important}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteTituloCampos().isEmpty()) {
			css.append(".tituloCampos {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteTituloCampos())
					.append(" !important}");
			css.append(".rf-dt-c {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteTituloCampos())
					.append(" !important}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteTituloFormulario().isEmpty()) {
			css.append(".fonteTituloPagina {color:#")
					.append(configuracaoAparenciaSistemaVO.getCorFonteTituloFormulario()).append(" !important}");
			css.append(".title {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteTituloFormulario())
					.append(" !important; border-bottom-color:#")
					.append(configuracaoAparenciaSistemaVO.getCorFonteTituloFormulario()).append("}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorBackHeaderAbas().isEmpty()) {
			if (!configuracaoAparenciaSistemaVO.getCorBackHeaderAbas().contains("gradient")
					&& !configuracaoAparenciaSistemaVO.getCorBackHeaderAbas().contains("#")) {
				css.append("*.rf-tab-hdr-act {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 1 !important}");
				css.append("*.rf-tab-hdr-act.rf-tab-hdr-top {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 1 !important}");
				css.append("*.rf-tab-hdr {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append("*.rf-tab-hdr-inact {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append("*.rf-tab-hdr-inact.rf-tab-hdr-top {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append(".timeline-anoSemestre.select {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; border:none !important}");
				css.append(".timeline-anoSemestre.info {background-image:none; background:#")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3; border:none !important }");

			} else if (!configuracaoAparenciaSistemaVO.getCorBackHeaderAbas().contains("gradient")
					&& configuracaoAparenciaSistemaVO.getCorBackHeaderAbas().contains("#")) {
				css.append("*.rf-tab-hdr-act {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 1 !important}");
				css.append("*.rf-tab-hdr-act.rf-tab-hdr-top {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 1 !important}");
				css.append("*.rf-tab-hdr {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append("*.rf-tab-hdr-inact {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append("*.rf-tab-hdr-inact.rf-tab-hdr-top {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append(".timeline-anoSemestre.select {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; border:none !important  }");
				css.append(".timeline-anoSemestre.info {background-image:none; background:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3; border:none !important }");
			} else {
				css.append("*.rf-tab-hdr-act {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 1 !important}");
				css.append("*.rf-tab-hdr-act.rf-tab-hdr-top {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 1 !important}");
				css.append("*.rf-tab-hdr {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append("*.rf-tab-hdr-inact {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append("*.rf-tab-hdr-inact.rf-tab-hdr-top {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3 }");
				css.append(".timeline-anoSemestre.select {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; border:none !important; }");
				css.append(".timeline-anoSemestre.info {background-image:")
						.append(configuracaoAparenciaSistemaVO.getCorBackHeaderAbas())
						.append(" !important; opacity: 0.3; border:none !important }");
			}

		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas().isEmpty()) {
			css.append(".rf-tab-lbl {color:#").append(configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas())
					.append(" !important}");
			css.append(".timeline-anoSemestre.select {color:#")
					.append(configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas()).append(" !important}");
			css.append(".timeline-anoSemestre.info {color:#")
					.append(configuracaoAparenciaSistemaVO.getCorFonteHeaderAbas()).append(" !important}");
		}
		if (!configuracaoAparenciaSistemaVO.getCorFonteMenu().isEmpty()) {
			css.append(".nav-link{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteMenu())
					.append(" !important}");
			css.append(".nav-link:hover{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteMenu())
					.append(" !important; opacity:0.5 !important}");
			css.append(".iconeMenu{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteMenu())
					.append(" !important}");
			css.append(".iconeMenu:hover{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteMenu())
					.append(" !important; opacity:0.5 !important}");
			css.append(".fonteMenu{color:#").append(configuracaoAparenciaSistemaVO.getCorFonteMenu())
					.append(" !important}");
		}
		css.append(configuracaoAparenciaSistemaVO.getCss());
		configuracaoAparenciaSistemaVO.setScriptCssGerado(css.toString());
	}

	@Override
	public ConfiguracaoAparenciaSistemaVO consultarConfiguracaoAparenciaSistema(boolean validarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO != null) {
			super.consultar(getIdEntidade(), validarAcesso, usuarioVO);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(
				"select configuracaoAparenciaSistema.* from configuracoes inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = configuracoes.codigo inner join configuracaoAparenciaSistema on configuracaogeralsistema.configuracaoAparenciaSistema = configuracaoAparenciaSistema.codigo where configuracoes.padrao ");
		if (rs.next()) {
			return montarDados(rs, NivelMontarDados.TODOS, usuarioVO);
		}
		ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO = new ConfiguracaoAparenciaSistemaVO();
		realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
		return configuracaoAparenciaSistemaVO;
	}

	@Override
	public ConfiguracaoAparenciaSistemaVO consultarPorChavePrimaria(Integer codigo, boolean validarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		super.consultar(getIdEntidade(), validarAcesso, usuarioVO);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(
				"select configuracaoAparenciaSistema.* from configuracaoAparenciaSistema where codigo = ? ", codigo);
		if (rs.next()) {
			return montarDados(rs, NivelMontarDados.TODOS, usuarioVO);
		}
		throw new Exception("Dados Não Encontrados (Configuração Aparência Sistema - " + codigo + ").");

	}

	@Override
	public List<ConfiguracaoAparenciaSistemaVO> consultarAparenciaDisponibilizadoUsuario(
			NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		super.consultar(getIdEntidade(), validarAcesso, usuarioVO);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(
				"select configuracaoAparenciaSistema.* from configuracaoAparenciaSistema where disponibilizarUsuario order by nome ");
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<ConfiguracaoAparenciaSistemaVO> consultar(String campoConsulta, String nome,
			NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		super.consultar(getIdEntidade(), validarAcesso, usuarioVO);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(
				"select configuracaoAparenciaSistema.* from configuracaoAparenciaSistema where sem_acentos(configuracaoAparenciaSistema.nome) ilike sem_acentos(?) order by nome ",
				nome + "%");
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	public List<ConfiguracaoAparenciaSistemaVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados,
			UsuarioVO usuarioVO) throws Exception {
		List<ConfiguracaoAparenciaSistemaVO> configuracaoAparenciaSistemaVOs = new ArrayList<ConfiguracaoAparenciaSistemaVO>(
				0);
		while (rs.next()) {
			configuracaoAparenciaSistemaVOs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return configuracaoAparenciaSistemaVOs;
	}

	public ConfiguracaoAparenciaSistemaVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados,
			UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO = new ConfiguracaoAparenciaSistemaVO();
		configuracaoAparenciaSistemaVO.setCodigo(rs.getInt("codigo"));
		configuracaoAparenciaSistemaVO.setNovoObj(false);
		configuracaoAparenciaSistemaVO.setNome(rs.getString("nome"));
		configuracaoAparenciaSistemaVO.setCorBackMenu(rs.getString("corBackMenu"));
		if (nivelMontarDados != null && nivelMontarDados.equals(NivelMontarDados.COMBOBOX)) {
			realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
			return configuracaoAparenciaSistemaVO;
		}
		configuracaoAparenciaSistemaVO.setCorBackGeral(rs.getString("corBackGeral"));
		configuracaoAparenciaSistemaVO.setCorBackAreaConteudo(rs.getString("corBackAreaConteudo"));
		configuracaoAparenciaSistemaVO.setCorBackBannerLogin(rs.getString("corBackBannerLogin"));
		configuracaoAparenciaSistemaVO.setCorBackHeaderDashboard(rs.getString("corBackHeaderDashboard"));
		configuracaoAparenciaSistemaVO.setCorBackIndex(rs.getString("corBackIndex"));
		configuracaoAparenciaSistemaVO.setCorBackLogin(rs.getString("corBackLogin"));
		configuracaoAparenciaSistemaVO.setCorBackTopo(rs.getString("corBackTopo"));
		configuracaoAparenciaSistemaVO.setCorFonteIndex(rs.getString("corFonteIndex"));
		configuracaoAparenciaSistemaVO.setCorFonteLogin(rs.getString("corFonteLogin"));
		configuracaoAparenciaSistemaVO.setCorFonteMenu(rs.getString("corFonteMenu"));
		configuracaoAparenciaSistemaVO.setCorFonteTopo(rs.getString("corFonteTopo"));
		configuracaoAparenciaSistemaVO.setCorFontHeaderDashboard(rs.getString("corFontHeaderDashboard"));
		configuracaoAparenciaSistemaVO.setCorFonteTituloFormulario(rs.getString("corFonteTituloFormulario"));
		configuracaoAparenciaSistemaVO.setCorFonteTituloCampos(rs.getString("corFonteTituloCampos"));
		configuracaoAparenciaSistemaVO.setCorBackCampos(rs.getString("corBackCampos"));
		configuracaoAparenciaSistemaVO.setCorFonteHeaderTabela(rs.getString("corFonteHeaderTabela"));
		configuracaoAparenciaSistemaVO.setCorBackHeaderTabela(rs.getString("corBackHeaderTabela"));
		configuracaoAparenciaSistemaVO.setCorFonteLinhaTabela(rs.getString("corFonteLinhaTabela"));
		configuracaoAparenciaSistemaVO.setCorBackLinhaTabela(rs.getString("corBackLinhaTabela"));
		configuracaoAparenciaSistemaVO.setCorFonteHeaderAbas(rs.getString("corFonteHeaderAbas"));
		configuracaoAparenciaSistemaVO.setCorBackHeaderAbas(rs.getString("corBackHeaderAbas"));
		configuracaoAparenciaSistemaVO.setTempoBanner(rs.getInt("tempoBanner"));
		configuracaoAparenciaSistemaVO.setLinkVideosBanner(rs.getString("linkVideosBanner"));
		configuracaoAparenciaSistemaVO.setDisponibilizarUsuario(rs.getBoolean("disponibilizarUsuario"));
		configuracaoAparenciaSistemaVO.setCss(rs.getString("css"));
		configuracaoAparenciaSistemaVO.setNomeImagemTopo(rs.getString("nomeImagemTopo"));
		if (Uteis.isAtributoPreenchido(rs.getString("caminhoBaseImagemTopo"))) {
			configuracaoAparenciaSistemaVO
					.setCaminhoBaseImagemTopo(PastaBaseArquivoEnum.valueOf(rs.getString("caminhoBaseImagemTopo")));
		}
		configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior();

		if (nivelMontarDados != null && nivelMontarDados.equals(NivelMontarDados.BASICO)) {
			return configuracaoAparenciaSistemaVO;
		}
		configuracaoAparenciaSistemaVO
				.setBannerLoginVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(
						configuracaoAparenciaSistemaVO.getCodigo(), "BALO", Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
		realizarGeracaoScriptBannerLogin(configuracaoAparenciaSistemaVO);

		return configuracaoAparenciaSistemaVO;
	}

	@Override
	public void realizarDefinicaoPadraoSistema(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO) {
		configuracaoAparenciaSistemaVO.setCorBackAreaConteudo(null);
		configuracaoAparenciaSistemaVO.setCorBackBannerLogin(null);
		configuracaoAparenciaSistemaVO.setCorBackCampos(null);
		configuracaoAparenciaSistemaVO.setCorBackGeral(null);
		configuracaoAparenciaSistemaVO.setCorBackHeaderAbas(null);
		configuracaoAparenciaSistemaVO.setCorBackHeaderDashboard(null);
		configuracaoAparenciaSistemaVO.setCorBackHeaderTabela(null);
		configuracaoAparenciaSistemaVO.setCorBackIndex(null);
		configuracaoAparenciaSistemaVO.setCorBackLinhaTabela(null);
		configuracaoAparenciaSistemaVO.setCorBackLogin(null);
		configuracaoAparenciaSistemaVO.setCorBackMenu(null);
		configuracaoAparenciaSistemaVO.setCorBackTopo(null);
		configuracaoAparenciaSistemaVO.setCorFonteHeaderAbas(null);
		configuracaoAparenciaSistemaVO.setCorFonteHeaderTabela(null);
		configuracaoAparenciaSistemaVO.setCorFonteIndex(null);
		configuracaoAparenciaSistemaVO.setCorFonteLinhaTabela(null);
		configuracaoAparenciaSistemaVO.setCorFonteLogin(null);
		configuracaoAparenciaSistemaVO.setCorFonteMenu(null);
		configuracaoAparenciaSistemaVO.setCorFonteTituloCampos(null);
		configuracaoAparenciaSistemaVO.setCorFonteTituloFormulario(null);
		configuracaoAparenciaSistemaVO.setCorFonteTopo(null);
		configuracaoAparenciaSistemaVO.setCss(null);
		realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
	}

	@Override
	public void uploadLogoTopo(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, FileUploadEvent upload,
			UsuarioVO usuarioVO) throws Exception {

		String nomeImagem = String.valueOf(new Date().getTime()) + upload.getUploadedFile().getName().substring(
				upload.getUploadedFile().getName().lastIndexOf("."), upload.getUploadedFile().getName().length());
		ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeImagem,
				PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(),
				getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO), usuarioVO);
		configuracaoAparenciaSistemaVO.setNomeImagemTopo(nomeImagem);
		configuracaoAparenciaSistemaVO.setCaminhoBaseImagemTopo(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirLogoTopo(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception {

		if (Uteis.isAtributoPreenchido(configuracaoAparenciaSistemaVO.getNomeImagemTopo())) {
			super.alterar(ConfiguracaoAparenciaSistema.idEntidade, true, usuarioVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle()
					.getConfiguracaoGeralSistemaVO(0, usuarioVO);
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ configuracaoAparenciaSistemaVO.getCaminhoBaseImagemTopo().getValue() + File.separator
					+ configuracaoAparenciaSistemaVO.getNomeImagemTopo()));
			configuracaoAparenciaSistemaVO.setNomeImagemTopo("");
			configuracaoAparenciaSistemaVO.setCaminhoBaseImagemTopo(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP);
			configuracaoAparenciaSistemaVO.setNomeImagemTopoAnterior(null);
			configuracaoAparenciaSistemaVO.getNomeImagemTopoAnterior();
			alterar(configuracaoAparenciaSistemaVO, "configuracaoAparenciaSistema",
					new AtributoPersistencia().add("nomeImagemTopo", configuracaoAparenciaSistemaVO.getNomeImagemTopo())
							.add("caminhoBaseImagemTopo",
									configuracaoAparenciaSistemaVO.getCaminhoBaseImagemTopo().toString()),
					new AtributoPersistencia().add("codigo", configuracaoAparenciaSistemaVO.getCodigo()), usuarioVO);
		}

	}

}
