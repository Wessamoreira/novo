package negocio.facade.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoAtaResultadosFinaisVO;
import negocio.comuns.academico.ConfiguracaoLayoutAtaResultadosFinaisVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoLayoutAtaResultadosFinaisInterfaceFacade;
import relatorio.controle.academico.AtaResultadosFinaisRelControle;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AtaResultadosFinaisRel;


@Repository
@Scope("singleton")
public class ConfiguracaoLayoutAtaResultadosFinais extends ControleAcesso implements ConfiguracaoLayoutAtaResultadosFinaisInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2981092274380348841L;
	private static final String nomeTabela = "configuracaoLayoutAtaResultadosFinais";	
	private static final String extensaoJasper = ".jasper";

	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception {
		for(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO : configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs()) {
			configuracaoLayoutAtaResultadosFinaisVO.setConfiguracaoAtaResultadosFinaisVO(configuracaoAtaResultadosFinaisVO);			
			persistir(configuracaoAtaResultadosFinaisVO, configuracaoLayoutAtaResultadosFinaisVO, usuarioVO, null);
		}

	}
	
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO,ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
			validarDados(configuracaoAtaResultadosFinaisVO, configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
			if(Uteis.isAtributoPreenchido(configuracaoLayoutAtaResultadosFinaisVO)) {
				alterar(configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
			}else {
				incluir(configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
			}
			persistirArquivoLayout(configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
			if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) && !configuracaoLayoutAtaResultadosFinaisVO.getLayoutFixoSistema()) {
				String caminhoBase;
				if (Uteis.isAtributoPreenchido(configuracaoLayoutAtaResultadosFinaisVO.getPastaBaseArquivoPdfPrincipal())) {
					caminhoBase = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + configuracaoLayoutAtaResultadosFinaisVO.getPastaBaseArquivoPdfPrincipal() + File.separator;
				} else {
					caminhoBase = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + "layoutAtaResultadosFinaisTMP" + File.separator + "PR" + File.separator + "PDF" + File.separator;
				}
				List<String> nomeSubrel =  new ArrayList<String>(0);
				String layout = configuracaoLayoutAtaResultadosFinaisVO.getNomeArquivoPdfPrincipal();
				try {
					nomeSubrel = UteisTextoPadrao.carregarNomeSubrelatorio(caminhoBase, layout, nomeSubrel);
				} catch (Exception e) {
					if (e.getMessage().contains("FileNotFoundException")) {
						throw new Exception("Não Foi Localizado o Sub-Relatório " + e.getMessage().substring(e.getMessage().lastIndexOf(File.separator) + 1) + " Dentro do Layout Principal. Verifique os Nomes dos Sub-Relatórios na Listagem.");
					} else {
						throw new Exception(e.getMessage());
					}
				}				
			}
			if(!configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().stream().anyMatch(l -> Uteis.isAtributoPreenchido(l) && Uteis.isAtributoPreenchido(l.getTitulo()) && l.getTitulo().equals(configuracaoLayoutAtaResultadosFinaisVO.getTitulo()))) {
				configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().add(configuracaoLayoutAtaResultadosFinaisVO);
			}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception {		
		incluir(configuracaoLayoutAtaResultadosFinaisVO, nomeTabela, new AtributoPersistencia()
				.add("titulo", configuracaoLayoutAtaResultadosFinaisVO.getTitulo())
				.add("chave", configuracaoLayoutAtaResultadosFinaisVO.getChave())
				.add("layoutFixoSistema", configuracaoLayoutAtaResultadosFinaisVO.getLayoutFixoSistema())
				.add("configuracaoAtaResultadosFinais", configuracaoLayoutAtaResultadosFinaisVO.getConfiguracaoAtaResultadosFinaisVO().getCodigo())
				.add("InativarLayout", configuracaoLayoutAtaResultadosFinaisVO.getInativarLayout())
				.add("layoutPadrao", configuracaoLayoutAtaResultadosFinaisVO.getLayoutPadrao())
				, usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception {
		alterar(configuracaoLayoutAtaResultadosFinaisVO, nomeTabela, new AtributoPersistencia()
				.add("titulo", configuracaoLayoutAtaResultadosFinaisVO.getTitulo())
				.add("chave", configuracaoLayoutAtaResultadosFinaisVO.getChave())
				.add("layoutFixoSistema", configuracaoLayoutAtaResultadosFinaisVO.getLayoutFixoSistema())
				.add("inativarLayout", configuracaoLayoutAtaResultadosFinaisVO.getInativarLayout())
				.add("layoutPadrao", configuracaoLayoutAtaResultadosFinaisVO.getLayoutPadrao())
				.add("configuracaoAtaResultadosFinais", configuracaoLayoutAtaResultadosFinaisVO.getConfiguracaoAtaResultadosFinaisVO()), 
				new AtributoPersistencia().add("codigo", configuracaoLayoutAtaResultadosFinaisVO.getCodigo()),
				usuarioVO);

	}
	
	@Override	
	public void validarDados(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws ConsistirException {
		if(configuracaoLayoutAtaResultadosFinaisVO.getTitulo().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutAtaResultadosFinais_titulo"));
		}
		if(configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().stream().filter(t -> Uteis.isAtributoPreenchido(t) && Uteis.isAtributoPreenchido(t.getTitulo()) && Uteis.removerAcentos(configuracaoLayoutAtaResultadosFinaisVO.getTitulo()).equalsIgnoreCase(Uteis.removerAcentos(t.getTitulo())) && (configuracaoLayoutAtaResultadosFinaisVO.getCodigo() != t.getCodigo())).count() >= 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutAtaResultadosFinais_duplicidadeDescricao").replace("{0}", configuracaoLayoutAtaResultadosFinaisVO.getTitulo()));
		}
		if(configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfPrincipal() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutAtaResultadosFinais_layoutPrincipalPDF").replace("{0}", configuracaoLayoutAtaResultadosFinaisVO.getTitulo()));
		}
		if(configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().stream().filter(t -> t.getArquivoIreportPrincipal()).count() > 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutAtaResultadosFinais_duplicidadeLayoutPrincipalPdf").replace("{0}", configuracaoLayoutAtaResultadosFinaisVO.getTitulo()));
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception {
		if(!configuracaoLayoutAtaResultadosFinaisVO.getLayoutFixoSistema()) {
			try {
				getConexao().getJdbcTemplate().update("delete from configuracaoLayoutAtaResultadosFinais where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), configuracaoLayoutAtaResultadosFinaisVO.getCodigo());
				configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().clear();
				deletarArquivoLayout(configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
				configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().remove(configuracaoLayoutAtaResultadosFinaisVO);
			}catch (Exception e) {
				configuracaoLayoutAtaResultadosFinaisVO.setArquivoPdfVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutAtaResultadosFinaisVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name(), TipoRelatorioEnum.PDF, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
				throw e;
			}
		}else {
			throw new Exception("Não é possível excluir um layout fixo do sistema, utilize a opção ocultar.");
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistirArquivoLayout(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception{
		if(!configuracaoLayoutAtaResultadosFinaisVO.getLayoutFixoSistema()) {
			deletarArquivoLayout(configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
			for(ArquivoVO arquivoVO: configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs()) {
				arquivoVO.setValidarDados(false);
				arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name());
        		arquivoVO.setCodOrigem(configuracaoLayoutAtaResultadosFinaisVO.getCodigo());        		
        		arquivoVO.setTipoRelatorio(TipoRelatorioEnum.PDF);
				getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO));
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void deletarArquivoLayout(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception{
		List<ArquivoVO> arquivoVOs = (getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutAtaResultadosFinaisVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name(), TipoRelatorioEnum.PDF, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));

		for (Iterator<ArquivoVO> iterator = arquivoVOs.iterator(); iterator.hasNext();) {
			ArquivoVO arquivoVO = (ArquivoVO) iterator.next();			
			if(!configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().stream().anyMatch(t -> t.getCodigo().equals(arquivoVO.getCodigo()))) {
				getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO));
			}
		}
		
	}


	@Override
	public List<ConfiguracaoLayoutAtaResultadosFinaisVO> consultarPorConfiguracaoAtaResultadosFinais(Integer configuracaoAtaResultadosFinais,
			UsuarioVO usuarioVO) throws Exception {
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select configuracaoLayoutAtaResultadosFinais.* from configuracaoLayoutAtaResultadosFinais inner join configuracaoAtaResultadosFinais on configuracaoLayoutAtaResultadosFinais.configuracaoAtaResultadosFinais = configuracaoAtaResultadosFinais.codigo where configuracaoAtaResultadosFinais.codigo = ? order by configuracaoLayoutAtaResultadosFinais.titulo", configuracaoAtaResultadosFinais), usuarioVO);
	}
	
	@Override	
	public ConfiguracaoLayoutAtaResultadosFinaisVO consultarPorChavePrimaria(Integer configuracaoLayoutAtaResultadosFinais, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select configuracaoLayoutAtaResultadosFinais.* from configuracaoLayoutAtaResultadosFinais inner join configuracaoataresultadosfinais on configuracaoataresultadosfinais.codigo = configuracaoLayoutAtaResultadosFinais.configuracaoataresultadosfinais where configuracaoLayoutAtaResultadosFinais.codigo = ? ", configuracaoLayoutAtaResultadosFinais);
		if(rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		throw new Exception("Não foi possível encontrar o layout selecionado ("+configuracaoLayoutAtaResultadosFinais+").");
	}
	
	private List<ConfiguracaoLayoutAtaResultadosFinaisVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<ConfiguracaoLayoutAtaResultadosFinaisVO> configuracaoLayoutAtaResultadosFinaisVOs =  new ArrayList<ConfiguracaoLayoutAtaResultadosFinaisVO>(0);
		while(rs.next()) {
			configuracaoLayoutAtaResultadosFinaisVOs.add(montarDados(rs, usuarioVO));
		}
		return configuracaoLayoutAtaResultadosFinaisVOs;
		
	} 
		
	@Override
	public void adicionarLayout(FileUploadEvent uploadEvent, TipoRelatorioEnum tipoRelatorio, ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO  configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception{
		if((tipoRelatorio.equals(TipoRelatorioEnum.PDF) && configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().stream().anyMatch(l -> l.getNome().equals(uploadEvent.getUploadedFile().getName())))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutAtaResultadosFinais_layoutAdicionado"));
		}
		ArquivoVO arquivoVO =  new ArquivoVO();
		if((tipoRelatorio.equals(TipoRelatorioEnum.PDF) && configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfPrincipal() != null)) {
			arquivoVO.setArquivoIreportPrincipal(false);
		}else {
			arquivoVO.setArquivoIreportPrincipal(true);
		}
		arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP);
		arquivoVO.setNome(uploadEvent.getUploadedFile().getName());
		arquivoVO.setDescricao(uploadEvent.getUploadedFile().getName());
		arquivoVO.setExtensao(extensaoJasper);
		arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name());		
		arquivoVO.setValidarDados(false);
		arquivoVO.setCodOrigem(configuracaoLayoutAtaResultadosFinaisVO.getCodigo());
		arquivoVO.setTipoRelatorio(tipoRelatorio);
		getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, arquivoVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO) ,PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP, usuarioVO);
		if(tipoRelatorio.equals(TipoRelatorioEnum.PDF)) {
			configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().add(arquivoVO);
		}
		
	}
		
	@Override
	public void removerLayout(ArquivoVO arquivoVO, ConfiguracaoLayoutAtaResultadosFinaisVO  configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception{
		if(arquivoVO.getTipoRelatorio().equals(TipoRelatorioEnum.PDF)) {
			configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().removeIf(a -> a.getNome().equals(arquivoVO.getNome()));
		}
		if(!Uteis.isAtributoPreenchido(arquivoVO)) {
			ArquivoHelper.delete(new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO)+File.separator+arquivoVO.getPastaBaseArquivo()+File.separator+arquivoVO.getNome()));
		}				
	}
	
	private ConfiguracaoLayoutAtaResultadosFinaisVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO =  new ConfiguracaoLayoutAtaResultadosFinaisVO();
		configuracaoLayoutAtaResultadosFinaisVO.setNovoObj(false);
		configuracaoLayoutAtaResultadosFinaisVO.setCodigo(rs.getInt("codigo"));
		configuracaoLayoutAtaResultadosFinaisVO.setTitulo(rs.getString("titulo"));
		configuracaoLayoutAtaResultadosFinaisVO.setChave(rs.getString("chave"));
		configuracaoLayoutAtaResultadosFinaisVO.setLayoutFixoSistema(rs.getBoolean("layoutFixoSistema"));
		configuracaoLayoutAtaResultadosFinaisVO.setInativarLayout(rs.getBoolean("inativarLayout"));
		configuracaoLayoutAtaResultadosFinaisVO.setLayoutPadrao(rs.getBoolean("layoutPadrao"));	
		configuracaoLayoutAtaResultadosFinaisVO.getConfiguracaoAtaResultadosFinaisVO().setCodigo(rs.getInt("configuracaoAtaResultadosFinais"));
		configuracaoLayoutAtaResultadosFinaisVO.setArquivoPdfVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutAtaResultadosFinaisVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name(), TipoRelatorioEnum.PDF, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
		return configuracaoLayoutAtaResultadosFinaisVO;
	} 

	@Override
	public void adicionarConfiguracaoLayoutAtaResultadosFinaisVO(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO,
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().stream()
				.anyMatch(l -> Uteis.removerAcentos(l.getTitulo())
						.equalsIgnoreCase(Uteis.removerAcentos(configuracaoLayoutAtaResultadosFinaisVO.getTitulo())))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ataResultadosFinais_layout_existente"));
		}
		configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().add(configuracaoLayoutAtaResultadosFinaisVO);
	}
	
	@Override
	public List<ConfiguracaoLayoutAtaResultadosFinaisVO> consultar(UsuarioVO usuarioVO) throws Exception {
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select configuracaoLayoutAtaResultadosFinais.* from configuracaoLayoutAtaResultadosFinais inner join configuracaoAtaResultadosFinais on configuracaoLayoutAtaResultadosFinais.configuracaoAtaResultadosFinais = configuracaoAtaResultadosFinais.codigo order by configuracaoLayoutAtaResultadosFinais.titulo  "), usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoLayoutPadraoAtaResultadosFinais(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, String caminhoBase)  throws Exception {		
		AtaResultadosFinaisRelControle ataResultadosFinaisRelControle =  new AtaResultadosFinaisRelControle();
		List<SelectItem> layouts = ataResultadosFinaisRelControle.getListaTipoLayoutAtaResultadosFinais("PADRAO");
		for(SelectItem layout: layouts) {
			if(layout.getValue() instanceof String && !configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().stream().anyMatch(l -> l.getChave().equals(layout.getValue()))) {				
				try {
					ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO = new ConfiguracaoLayoutAtaResultadosFinaisVO();
					configuracaoLayoutAtaResultadosFinaisVO.setTitulo(layout.getLabel());
					configuracaoLayoutAtaResultadosFinaisVO.setChave((String)layout.getValue());
					configuracaoLayoutAtaResultadosFinaisVO.setConfiguracaoAtaResultadosFinaisVO(configuracaoAtaResultadosFinaisVO);
					configuracaoLayoutAtaResultadosFinaisVO.setLayoutFixoSistema(true);		
					incluir(configuracaoLayoutAtaResultadosFinaisVO, null);
					realizarGeracaoLayout(configuracaoLayoutAtaResultadosFinaisVO, caminhoBase, (String)layout.getValue());
					
					
					for(ArquivoVO arquivoVO: configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs()) {
						arquivoVO.setValidarDados(false);
						arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name());
		        		arquivoVO.setCodOrigem(configuracaoLayoutAtaResultadosFinaisVO.getCodigo());
		        		arquivoVO.setTipoRelatorio(TipoRelatorioEnum.PDF);
						getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, null, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null));
					}

				} 
				catch (Exception e) {
					e.getMessage();
					e.printStackTrace();
				}
			}
		}		 
	}
	
	private void realizarGeracaoLayout(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, String caminhoBase, String layout) throws Exception{
		List<String> nomeSubrel =  new ArrayList<String>(0);					
		String layoutCompleto = AtaResultadosFinaisRel.getDesignIReportRelatorio(layout);
		if(!Uteis.isAtributoPreenchido(layoutCompleto)) {
			return;
		}
		layout = (layoutCompleto.substring(layoutCompleto.lastIndexOf(File.separator)+1, layoutCompleto.length()).replace(".jrxml", extensaoJasper));		
		nomeSubrel.add(layout);
		nomeSubrel = UteisTextoPadrao.carregarNomeSubrelatorio(caminhoBase, layout, nomeSubrel);
		for(String nomeLayout : nomeSubrel) {					
			ArquivoVO arquivoVO =  new ArquivoVO();
			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS);
			arquivoVO.setPastaBaseArquivo(AtaResultadosFinaisRel.getCaminhoBaseRelatorio());
			arquivoVO.setNome(nomeLayout.contains(extensaoJasper) ? nomeLayout : layout);
			arquivoVO.setDescricao(arquivoVO.getNome());
			arquivoVO.setExtensao(extensaoJasper);
			arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name());
			arquivoVO.setArquivoIreportPrincipal(nomeLayout.equals(layout));
			configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().add(arquivoVO);
	
		}
	}
	
	@Override		
	public void realizarDefinicaoArquivoPrincipalConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws Exception{
		if(arquivoVO.getTipoRelatorio().equals(TipoRelatorioEnum.PDF)) {
			configuracaoLayoutAtaResultadosFinaisVO.getArquivoPdfVOs().forEach(t -> {if(!t.getNome().equals(arquivoVO.getNome())) {
				t.setArquivoIreportPrincipal(false);
			}else{
				t.setArquivoIreportPrincipal(true);
			}});			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDefinicaoLayoutPadraoConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception{
			if(configuracaoLayoutAtaResultadosFinaisVO.getLayoutPadrao() && configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().stream().anyMatch(o -> o.getLayoutPadrao() && !o.getCodigo().equals(configuracaoLayoutAtaResultadosFinaisVO.getCodigo()))) {
				ConfiguracaoLayoutAtaResultadosFinaisVO  configuracaoLayoutAtaResultadosFinaisVO2 = configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().stream().filter(o -> o.getLayoutPadrao() && !o.getCodigo().equals(configuracaoLayoutAtaResultadosFinaisVO.getCodigo())).findFirst().get();
				configuracaoLayoutAtaResultadosFinaisVO2.setLayoutPadrao(false);
				alterar(configuracaoLayoutAtaResultadosFinaisVO2, usuarioVO);
			}
			alterar(configuracaoLayoutAtaResultadosFinaisVO, usuarioVO);
	}

}
