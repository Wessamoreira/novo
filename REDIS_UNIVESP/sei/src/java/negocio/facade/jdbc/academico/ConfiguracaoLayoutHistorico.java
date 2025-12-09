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
import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.ConfiguracaoObservacaoHistoricoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;		
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoLayoutHistoricoInterfaceFacade;
import relatorio.controle.academico.HistoricoAlunoRelControle;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;


@Repository
@Scope("singleton")
public class ConfiguracaoLayoutHistorico extends ControleAcesso implements ConfiguracaoLayoutHistoricoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2981092274380348841L;
	private static final String nomeTabela = "configuracaoLayoutHistorico";	
	private static final String extensaoJasper = ".jasper";

	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		for(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO : configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs()) {
			configuracaoLayoutHistoricoVO.setConfiguracaoHistoricoVO(configuracaoHistoricoVO);			
			persistir(configuracaoHistoricoVO, configuracaoLayoutHistoricoVO, usuarioVO, null);
		}

	}
	
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO,ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
			validarDados(configuracaoHistoricoVO, configuracaoLayoutHistoricoVO, usuarioVO);
			if(Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO)) {
				alterar(configuracaoLayoutHistoricoVO, usuarioVO);
			}else {
				incluir(configuracaoLayoutHistoricoVO, usuarioVO);
			}
			persistirArquivoLayout(configuracaoLayoutHistoricoVO, usuarioVO);
			if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) && !configuracaoLayoutHistoricoVO.getLayoutFixoSistema()) {
				String caminhoBase;
				if (Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO.getPastaBaseArquivoPdfPrincipal())) {
					caminhoBase = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + configuracaoLayoutHistoricoVO.getPastaBaseArquivoPdfPrincipal() + File.separator;
				} else {
					caminhoBase = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + "layoutHistoricoTMP" + File.separator + "PR" + File.separator + "PDF" + File.separator;
				}
				List<String> nomeSubrel =  new ArrayList<String>(0);
				String layout = configuracaoLayoutHistoricoVO.getNomeArquivoPdfPrincipal();
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
			if(!configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().anyMatch(l -> Uteis.isAtributoPreenchido(l) && Uteis.isAtributoPreenchido(l.getDescricao()) && l.getDescricao().equals(configuracaoLayoutHistoricoVO.getDescricao()))) {
				configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().add(configuracaoLayoutHistoricoVO);
			}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception {		
		incluir(configuracaoLayoutHistoricoVO, nomeTabela, new AtributoPersistencia()
				.add("descricao", configuracaoLayoutHistoricoVO.getDescricao())
				.add("chave", configuracaoLayoutHistoricoVO.getChave())
				.add("layoutFixoSistema", configuracaoLayoutHistoricoVO.getLayoutFixoSistema())
				.add("configuracaoHistorico", configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().getCodigo())
				.add("ocultarLayout", configuracaoLayoutHistoricoVO.getOcultarLayout())
				.add("layoutPadrao", configuracaoLayoutHistoricoVO.getLayoutPadrao())
				, usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		alterar(configuracaoLayoutHistoricoVO, nomeTabela, new AtributoPersistencia()
				.add("descricao", configuracaoLayoutHistoricoVO.getDescricao())
				.add("chave", configuracaoLayoutHistoricoVO.getChave())
				.add("layoutFixoSistema", configuracaoLayoutHistoricoVO.getLayoutFixoSistema())
				.add("ocultarLayout", configuracaoLayoutHistoricoVO.getOcultarLayout())
				.add("layoutPadrao", configuracaoLayoutHistoricoVO.getLayoutPadrao())
				.add("configuracaoHistorico", configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO()), 
				new AtributoPersistencia().add("codigo", configuracaoLayoutHistoricoVO.getCodigo()),
				usuarioVO);

	}
	
	@Override	
	public void validarDados(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws ConsistirException {
		if(configuracaoLayoutHistoricoVO.getDescricao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_descricao"));
		}
		if(configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().filter(t -> Uteis.isAtributoPreenchido(t) && Uteis.isAtributoPreenchido(t.getDescricao()) && Uteis.removerAcentos(configuracaoLayoutHistoricoVO.getDescricao()).equalsIgnoreCase(Uteis.removerAcentos(t.getDescricao()))).count() > 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_duplicidadeDescricao").replace("{0}", configuracaoLayoutHistoricoVO.getDescricao()));
		}
		if(configuracaoLayoutHistoricoVO.getArquivoPdfPrincipal() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_layoutPrincipalPDF").replace("{0}", configuracaoLayoutHistoricoVO.getDescricao()));
		}
		if(!configuracaoLayoutHistoricoVO.getArquivoExcelVOs().isEmpty() && configuracaoLayoutHistoricoVO.getArquivoExcelPrincipal() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_layoutPrincipalExcel").replace("{0}", configuracaoLayoutHistoricoVO.getDescricao()));
		}
		if(configuracaoLayoutHistoricoVO.getArquivoPdfVOs().stream().filter(t -> t.getArquivoIreportPrincipal()).count() > 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_duplicidadeLayoutPrincipalPdf").replace("{0}", configuracaoLayoutHistoricoVO.getDescricao()));
		}
		if(!configuracaoLayoutHistoricoVO.getArquivoExcelVOs().isEmpty() && configuracaoLayoutHistoricoVO.getArquivoExcelVOs().stream().filter(t -> t.getArquivoIreportPrincipal()).count() > 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_duplicidadeLayoutPrincipalExcel").replace("{0}", configuracaoLayoutHistoricoVO.getDescricao()));
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		if(!configuracaoLayoutHistoricoVO.getLayoutFixoSistema()) {
			try {
				getConexao().getJdbcTemplate().update("delete from configuracaoLayoutHistorico where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), configuracaoLayoutHistoricoVO.getCodigo());
				configuracaoLayoutHistoricoVO.getArquivoExcelVOs().clear();
				configuracaoLayoutHistoricoVO.getArquivoPdfVOs().clear();
				deletarArquivoLayout(configuracaoLayoutHistoricoVO, usuarioVO);
				configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().remove(configuracaoLayoutHistoricoVO);
			}catch (Exception e) {
				configuracaoLayoutHistoricoVO.setArquivoPdfVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutHistoricoVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.name(), TipoRelatorioEnum.PDF, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
				configuracaoLayoutHistoricoVO.setArquivoExcelVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutHistoricoVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.name(), TipoRelatorioEnum.EXCEL, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
				throw e;
			}
		}else {
			throw new Exception("Não é possível excluir um layout fixo do sistema, utilize a opção ocultar.");
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistirArquivoLayout(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception{
		if(!configuracaoLayoutHistoricoVO.getLayoutFixoSistema()) {
			deletarArquivoLayout(configuracaoLayoutHistoricoVO, usuarioVO);
			for(ArquivoVO arquivoVO: configuracaoLayoutHistoricoVO.getArquivoPdfVOs()) {
				arquivoVO.setValidarDados(false);
				arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name());
        		arquivoVO.setCodOrigem(configuracaoLayoutHistoricoVO.getCodigo());        		
        		arquivoVO.setTipoRelatorio(TipoRelatorioEnum.PDF);
				getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO));
			}
			for(ArquivoVO arquivoVO: configuracaoLayoutHistoricoVO.getArquivoExcelVOs()) {
				arquivoVO.setValidarDados(false);
				arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name());				
        		arquivoVO.setCodOrigem(configuracaoLayoutHistoricoVO.getCodigo());
        		arquivoVO.setTipoRelatorio(TipoRelatorioEnum.EXCEL);
				getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO));
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void deletarArquivoLayout(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception{
		List<ArquivoVO> arquivoVOs = (getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutHistoricoVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.name(), TipoRelatorioEnum.PDF, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
		arquivoVOs.addAll(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutHistoricoVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.name(), TipoRelatorioEnum.EXCEL, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
		for (Iterator<ArquivoVO> iterator = arquivoVOs.iterator(); iterator.hasNext();) {
			ArquivoVO arquivoVO = (ArquivoVO) iterator.next();			
			if(!configuracaoLayoutHistoricoVO.getArquivoExcelVOs().stream().anyMatch(t -> t.getCodigo().equals(arquivoVO.getCodigo()))
					&& !configuracaoLayoutHistoricoVO.getArquivoPdfVOs().stream().anyMatch(t -> t.getCodigo().equals(arquivoVO.getCodigo()))) {
				getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO));
			}
		}
		
	}

	@Override
	public List<ConfiguracaoLayoutHistoricoVO> consultarPorNivelEducacional(TipoNivelEducacional tipoNivelEducacional,
			UsuarioVO usuarioVO) throws Exception {
		if(tipoNivelEducacional == null) {
			return new ArrayList<ConfiguracaoLayoutHistoricoVO>(0);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select configuracaoLayoutHistorico.*, configuracaoHistorico.nivelEducacional from configuracaoLayoutHistorico inner join configuracaoHistorico on configuracaoLayoutHistorico.configuracaoHistorico = configuracaoHistorico.codigo where configuracaoHistorico.niveleducacional  = ? order by configuracaoLayoutHistorico.descricao  ", tipoNivelEducacional.name()), usuarioVO);
	}

	@Override
	public List<ConfiguracaoLayoutHistoricoVO> consultarPorConfiguracaoHistorico(Integer configuracaoHistorico,
			UsuarioVO usuarioVO) throws Exception {
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select configuracaoLayoutHistorico.*, configuracaoHistorico.nivelEducacional from configuracaoLayoutHistorico inner join configuracaoHistorico on configuracaoLayoutHistorico.configuracaoHistorico = configuracaoHistorico.codigo where configuracaoHistorico.codigo = ? order by configuracaoLayoutHistorico.descricao", configuracaoHistorico), usuarioVO);
	}
	
	@Override	
	public ConfiguracaoLayoutHistoricoVO consultarPorChavePrimaria(Integer configuracaoLayoutHistorico, UsuarioVO usuarioVO) throws Exception {
		return consultarPorChavePrimaria(configuracaoLayoutHistorico, Boolean.TRUE, usuarioVO);
	}
	
	@Override	
	public ConfiguracaoLayoutHistoricoVO consultarPorChavePrimaria(Integer configuracaoLayoutHistorico, Boolean retornaException, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select configuracaoLayoutHistorico.*, configuracaoHistorico.nivelEducacional from configuracaoLayoutHistorico inner join configuracaohistorico on configuracaohistorico.codigo = configuracaoLayoutHistorico.configuracaohistorico where configuracaoLayoutHistorico.codigo = ? ", configuracaoLayoutHistorico);
		if(rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		if (retornaException) {
			throw new Exception("Não foi possível encontrar o layout selecionado ("+configuracaoLayoutHistorico+").");
		} else {
			return new ConfiguracaoLayoutHistoricoVO();
		}
	}
	
	private List<ConfiguracaoLayoutHistoricoVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<ConfiguracaoLayoutHistoricoVO> configuracaoLayoutHistoricoVOs =  new ArrayList<ConfiguracaoLayoutHistoricoVO>(0);
		while(rs.next()) {
			configuracaoLayoutHistoricoVOs.add(montarDados(rs, usuarioVO));
		}
		return configuracaoLayoutHistoricoVOs;
		
	} 
		
	@Override
	public void adicionarLayout(FileUploadEvent uploadEvent, TipoRelatorioEnum tipoRelatorio, ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO  configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception{
		if((tipoRelatorio.equals(TipoRelatorioEnum.EXCEL) && configuracaoLayoutHistoricoVO.getArquivoExcelVOs().stream().anyMatch(l -> l.getNome().equals(uploadEvent.getUploadedFile().getName()))) 
				|| (tipoRelatorio.equals(TipoRelatorioEnum.PDF) && configuracaoLayoutHistoricoVO.getArquivoPdfVOs().stream().anyMatch(l -> l.getNome().equals(uploadEvent.getUploadedFile().getName())))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoLayoutHistorico_layoutAdicionado"));
		}
		ArquivoVO arquivoVO =  new ArquivoVO();
		if(((tipoRelatorio.equals(TipoRelatorioEnum.EXCEL) && configuracaoLayoutHistoricoVO.getArquivoExcelVOs().stream().anyMatch(l -> l.getArquivoIreportPrincipal()))) 
				|| (tipoRelatorio.equals(TipoRelatorioEnum.PDF) && configuracaoLayoutHistoricoVO.getArquivoPdfPrincipal() != null)) {
			arquivoVO.setArquivoIreportPrincipal(false);
		}else {
			arquivoVO.setArquivoIreportPrincipal(true);
		}
		arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP);
		arquivoVO.setNivelEducacional(configuracaoHistoricoVO.getNivelEducacional().getValor());
		arquivoVO.setNome(uploadEvent.getUploadedFile().getName());
		arquivoVO.setDescricao(uploadEvent.getUploadedFile().getName());
		arquivoVO.setExtensao(extensaoJasper);
		arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name());		
		arquivoVO.setValidarDados(false);
		arquivoVO.setCodOrigem(configuracaoLayoutHistoricoVO.getCodigo());
		arquivoVO.setTipoRelatorio(tipoRelatorio);
		getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, arquivoVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO) ,PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP, usuarioVO);
		if(tipoRelatorio.equals(TipoRelatorioEnum.PDF)) {
			configuracaoLayoutHistoricoVO.getArquivoPdfVOs().add(arquivoVO);
		}else {
			configuracaoLayoutHistoricoVO.getArquivoExcelVOs().add(arquivoVO);
		}
		
	}
		
	@Override
	public void removerLayout(ArquivoVO arquivoVO, ConfiguracaoLayoutHistoricoVO  configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception{
		if(arquivoVO.getTipoRelatorio().equals(TipoRelatorioEnum.EXCEL)) {
			configuracaoLayoutHistoricoVO.getArquivoExcelVOs().removeIf(a -> a.getNome().equals(arquivoVO.getNome()));
		}else {
			configuracaoLayoutHistoricoVO.getArquivoPdfVOs().removeIf(a -> a.getNome().equals(arquivoVO.getNome()));
		}
		if(!Uteis.isAtributoPreenchido(arquivoVO)) {
			ArquivoHelper.delete(new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO)+File.separator+arquivoVO.getPastaBaseArquivo()+File.separator+arquivoVO.getNome()));
		}				
	}
	
	private ConfiguracaoLayoutHistoricoVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO =  new ConfiguracaoLayoutHistoricoVO();
		configuracaoLayoutHistoricoVO.setNovoObj(false);
		configuracaoLayoutHistoricoVO.setCodigo(rs.getInt("codigo"));
		configuracaoLayoutHistoricoVO.setDescricao(rs.getString("descricao"));
		configuracaoLayoutHistoricoVO.setChave(rs.getString("chave"));
		configuracaoLayoutHistoricoVO.setLayoutFixoSistema(rs.getBoolean("layoutFixoSistema"));
		configuracaoLayoutHistoricoVO.setOcultarLayout(rs.getBoolean("ocultarLayout"));
		configuracaoLayoutHistoricoVO.setLayoutPadrao(rs.getBoolean("layoutPadrao"));	
		configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().setCodigo(rs.getInt("configuracaoHistorico"));	
		configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().setNivelEducacional(TipoNivelEducacional.valueOf(rs.getString("nivelEducacional")));
		configuracaoLayoutHistoricoVO.setArquivoPdfVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutHistoricoVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.name(), TipoRelatorioEnum.PDF, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
		configuracaoLayoutHistoricoVO.setArquivoExcelVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigemTipoRelatorio(configuracaoLayoutHistoricoVO.getCodigo(), PastaBaseArquivoEnum.LAYOUT_HISTORICO.name(), TipoRelatorioEnum.EXCEL, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
		return configuracaoLayoutHistoricoVO;
	} 

	@Override
	public void adicionarConfiguracaoLayoutHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream()
				.anyMatch(l -> Uteis.removerAcentos(l.getDescricao())
						.equalsIgnoreCase(Uteis.removerAcentos(configuracaoLayoutHistoricoVO.getDescricao())))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historico_layout_existente"));
		}
		configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().add(configuracaoLayoutHistoricoVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoLayoutPadraoHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO, String caminhoBase)  throws Exception {		
		HistoricoAlunoRelControle historicoAlunoRelControle =  new HistoricoAlunoRelControle();
		List<SelectItem> layouts = historicoAlunoRelControle.getListaTipoLayoutHistorico("PADRAO", configuracaoHistoricoVO.getNivelEducacional().getValor());
		for(SelectItem layout: layouts) {
			if(layout.getValue() instanceof String && !configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().anyMatch(l -> l.getChave().equals(layout.getValue()))) {				
				
				ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO = new ConfiguracaoLayoutHistoricoVO();
				configuracaoLayoutHistoricoVO.setDescricao(layout.getLabel());
				configuracaoLayoutHistoricoVO.setChave((String)layout.getValue());
				configuracaoLayoutHistoricoVO.setConfiguracaoHistoricoVO(configuracaoHistoricoVO);
				configuracaoLayoutHistoricoVO.setLayoutFixoSistema(true);		
				incluir(configuracaoLayoutHistoricoVO, null);
				realizarGeracaoLayout(configuracaoLayoutHistoricoVO, configuracaoHistoricoVO.getNivelEducacional(), TipoRelatorioEnum.PDF, caminhoBase, (String)layout.getValue());
				realizarGeracaoLayout(configuracaoLayoutHistoricoVO, configuracaoHistoricoVO.getNivelEducacional(), TipoRelatorioEnum.EXCEL, caminhoBase, (String)layout.getValue());
				
				
				for(ArquivoVO arquivoVO: configuracaoLayoutHistoricoVO.getArquivoPdfVOs()) {
					arquivoVO.setValidarDados(false);
					arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name());
					arquivoVO.setNivelEducacional(configuracaoHistoricoVO.getNivelEducacional().getValor());
	        		arquivoVO.setCodOrigem(configuracaoLayoutHistoricoVO.getCodigo());
	        		arquivoVO.setTipoRelatorio(TipoRelatorioEnum.PDF);
					getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, null, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null));
				}
				for(ArquivoVO arquivoVO: configuracaoLayoutHistoricoVO.getArquivoExcelVOs()) {
					arquivoVO.setValidarDados(false);
					arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name());
					arquivoVO.setNivelEducacional(configuracaoHistoricoVO.getNivelEducacional().getValor());
	        		arquivoVO.setCodOrigem(configuracaoLayoutHistoricoVO.getCodigo());
	        		arquivoVO.setTipoRelatorio(TipoRelatorioEnum.EXCEL);
					getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, null, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null));
				}
			}
		}		 
	}
	
	private void realizarGeracaoLayout(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, TipoNivelEducacional tipoNivelEducacional,  TipoRelatorioEnum tipoRelatorioEnum, String caminhoBase, String layout) throws Exception{
		List<String> nomeSubrel =  new ArrayList<String>(0);					
		String layoutCompleto = HistoricoAlunoRel.getDesignIReportRelatorio(tipoNivelEducacional, layout, tipoRelatorioEnum.equals(TipoRelatorioEnum.EXCEL));
		if(!Uteis.isAtributoPreenchido(layoutCompleto)) {
			return;
		}
		layout = (layoutCompleto.substring(layoutCompleto.lastIndexOf(File.separator)+1, layoutCompleto.length()).replace(".jrxml", extensaoJasper));		
		nomeSubrel.add(layout);
		nomeSubrel = UteisTextoPadrao.carregarNomeSubrelatorio(caminhoBase, layout, nomeSubrel);
		for(String nomeLayout : nomeSubrel) {					
			ArquivoVO arquivoVO =  new ArquivoVO();
			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.LAYOUT_HISTORICO);
			arquivoVO.setPastaBaseArquivo(HistoricoAlunoRel.getCaminhoBaseRelatorio());
			arquivoVO.setNivelEducacional(tipoNivelEducacional.getValor());
			arquivoVO.setNome(nomeLayout.contains(extensaoJasper) ? nomeLayout : layout);
			arquivoVO.setDescricao(arquivoVO.getNome());
			arquivoVO.setExtensao(extensaoJasper);
			arquivoVO.setOrigem(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name());
			arquivoVO.setArquivoIreportPrincipal(nomeLayout.equals(layout));
			if(tipoRelatorioEnum.equals(TipoRelatorioEnum.PDF)) {
				configuracaoLayoutHistoricoVO.getArquivoPdfVOs().add(arquivoVO);
			}else {
				configuracaoLayoutHistoricoVO.getArquivoExcelVOs().add(arquivoVO);
			}
		}
	}
	
	@Override		
	public void realizarDefinicaoArquivoPrincipalConfiguracaoLayoutHistorico(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws Exception{
		if(arquivoVO.getTipoRelatorio().equals(TipoRelatorioEnum.PDF)) {
			configuracaoLayoutHistoricoVO.getArquivoPdfVOs().forEach(t -> {if(!t.getNome().equals(arquivoVO.getNome())) {
				t.setArquivoIreportPrincipal(false);
			}else{
				t.setArquivoIreportPrincipal(true);
			}});			
		}else {
			configuracaoLayoutHistoricoVO.getArquivoExcelVOs().forEach(t -> {if(!t.getNome().equals(arquivoVO.getNome())) {
				t.setArquivoIreportPrincipal(false);
			}else{
				t.setArquivoIreportPrincipal(true);
			}});
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDefinicaoLayoutPadraoConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception{
			if(configuracaoLayoutHistoricoVO.getLayoutPadrao() && configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().anyMatch(o -> o.getLayoutPadrao() && !o.getCodigo().equals(configuracaoLayoutHistoricoVO.getCodigo()))) {
				ConfiguracaoLayoutHistoricoVO  configuracaoLayoutHistoricoVO2 = configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().filter(o -> o.getLayoutPadrao() && !o.getCodigo().equals(configuracaoLayoutHistoricoVO.getCodigo())).findFirst().get();
				configuracaoLayoutHistoricoVO2.setLayoutPadrao(false);
				alterar(configuracaoLayoutHistoricoVO2, usuarioVO);
			}
			alterar(configuracaoLayoutHistoricoVO, usuarioVO);
	}

}
