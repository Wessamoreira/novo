package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoAvaliacaoInstitucionalEnum implements PerfilAcessoPermissaoEnumInterface {
	
	/**
	* Avaliacao Institucional Analitico Rel
	*
	*/
	AVALIACAO_INSTITUCIONAL_ANALITICO_REL("AvaliacaoInstitucionalAnaliticoRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AvaliacaoInstitucionalAnaliticoRel_titulo"),UteisJSF.internacionalizar("per_AvaliacaoInstitucionalAnaliticoRel_ajuda"), new String[]{"avaliacaoInstitucionalAnaliticoRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL),
	/**
	* Avaliacao Institucional Rel
	*
	*/
	AVALIACAO_INSTITUCIONAL_REL("AvaliacaoInstitucionalRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AvaliacaoInstitucionalRel_titulo"),UteisJSF.internacionalizar("per_AvaliacaoInstitucionalRel_ajuda"), new String[]{"avaliacaoInstitucionalRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL),
	/**
	* Questionario Rel
	*
	*/
	QUESTIONARIO_REL("QuestionarioRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_QuestionarioRel_titulo"),UteisJSF.internacionalizar("per_QuestionarioRel_ajuda"), new String[]{"questionarioRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL),
	/**
	* Avaliacao Institucional
	*
	*/
	AVALIACAO_INSTITUCIONAL("AvaliacaoInstitucional", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AvaliacaoInstitucional_titulo"),UteisJSF.internacionalizar("per_AvaliacaoInstitucional_ajuda"), new String[]{"avaliacaoInstitucionalCons.xhtml","avaliacaoInstitucionalForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_AVALIACAO_INSTITUCIONAL),
	/**
	* Avaliacao Institucional Presencial Resposta
	*
	*/
	AVALIACAO_INSTITUCIONAL_PRESENCIAL_RESPOSTA("AvaliacaoInstitucionalPresencialResposta", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AvaliacaoInstitucionalPresencialResposta_titulo"),UteisJSF.internacionalizar("per_AvaliacaoInstitucionalPresencialResposta_ajuda"), new String[]{"avaliacaoInstitucionalPresencialRespostaCons.xhtml","avaliacaoInstitucionalPresencialRespostaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_AVALIACAO_INSTITUCIONAL),
	/**
	* Questionario Avaliacao Institucional
	*
	*/
	QUESTIONARIO_AVALIACAO_INSTITUCIONAL("QuestionarioAvaliacaoInstitucional", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_QuestionarioAvaliacaoInstitucional_titulo"),UteisJSF.internacionalizar("per_QuestionarioAvaliacaoInstitucional_ajuda"), new String[]{"questionarioCons.xhtml","questionarioForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_AVALIACAO_INSTITUCIONAL),
	/**
	* Pergunta Avaliacao Institucional
	*
	*/
	PERGUNTA_AVALIACAO_INSTITUCIONAL("PerguntaAvaliacaoInstitucional", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PerguntaAvaliacaoInstitucional_titulo"),UteisJSF.internacionalizar("per_PerguntaAvaliacaoInstitucional_ajuda"), new String[]{"perguntaCons.xhtml","perguntaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_AVALIACAO_INSTITUCIONAL),
	
	/**
	 * Relatorio SEI Decidir Avaliação Institucional
	 *
	 */
	RELATORIO_SEIDECIDIR_AVALIACAO_INSTITUCIONAL("RelatorioSEIDecidirAvaliacaoInstitucional",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirAvaliacaoInstitucional_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirAvaliacaoInstitucional_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_AVALIACAO_INSTITUCIONAL_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirAvaliacaoInstitucionalApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAvaliacaoInstitucionalApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAvaliacaoInstitucionalApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_AVALIACAO_INSTITUCIONAL,
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_AVALIACAO_INSTITUCIONAL("PermitirVisualizarScriptSqlRelatorioSeiDecidirAvaliacaoInstitucional",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_AVALIACAO_INSTITUCIONAL,
			PerfilAcessoSubModuloEnum.AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL);


	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for funcionalidade 
	 */
	private PerfilAcessoPermissaoAvaliacaoInstitucionalEnum(String valor, PermissaoVisao[] permissaoVisao, 
			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
			PerfilAcessoSubModuloEnum perfilAcessoSubModulo			
			) {
		this.valor = valor;				
		this.permissaoVisao = permissaoVisao;				
		this.tipoPerfilAcesso = tipoPerfilAcesso;
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;		
	}
	
	private String valor;
	private PermissaoVisao[] permissaoVisao;		
	private TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso;	
	private PerfilAcessoSubModuloEnum perfilAcessoSubModulo;
	private Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum;
	
			
	/**
	 * @return the tipoPerfilAcesso
	 */
	public TipoPerfilAcessoPermissaoEnum getTipoPerfilAcesso() {
		if (tipoPerfilAcesso == null) {
			tipoPerfilAcesso = TipoPerfilAcessoPermissaoEnum.ENTIDADE;
		}
		return tipoPerfilAcesso;
	}
	/**
	 * @param tipoPerfilAcesso the tipoPerfilAcesso to set
	 */
	public void setTipoPerfilAcesso(TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso) {
		this.tipoPerfilAcesso = tipoPerfilAcesso;
	}
	
	/**
	 * @return the perfilAcessoSubModulo
	 */
	public PerfilAcessoSubModuloEnum getPerfilAcessoSubModulo() {
		if (perfilAcessoSubModulo == null) {
			perfilAcessoSubModulo = PerfilAcessoSubModuloEnum.TODOS;
		}
		return perfilAcessoSubModulo;
	}
	/**
	 * @param perfilAcessoSubModulo the perfilAcessoSubModulo to set
	 */
	public void setPerfilAcessoSubModulo(PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}


	/**
	 * @return the permissaoSuperiorEnum
	 */
	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissaoSuperiorEnum() {		
		return permissaoSuperiorEnum;
	}

	/**
	 * @param permissaoSuperiorEnum the permissaoSuperiorEnum to set
	 */
	public void setPermissaoSuperiorEnum(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum) {
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
	}

	/**
	 * @return the PermissaoVisao
	 */
	public PermissaoVisao[] getPermissaoVisao() {
		if (permissaoVisao == null) {
			permissaoVisao = new PermissaoVisao[0];
		}
		return permissaoVisao;
	}

	/**
	 * @param PermissaoVisao the PermissaoVisao to set
	 */
	public void setPermissaoVisao(PermissaoVisao[] PermissaoVisao) {
		this.permissaoVisao = PermissaoVisao;
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public Boolean getUtilizaVisao(TipoVisaoEnum tipoVisaoEnum) {
		for(PermissaoVisao permissaoVisao: getPermissaoVisao()){
			if(permissaoVisao.equals(tipoVisaoEnum)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if(getUtilizaVisao(tipoVisaoEnum)){
			return getPermissaoVisao(tipoVisaoEnum).getDescricao();
		}
		return "";
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum) {
		if(getUtilizaVisao(tipoVisaoEnum)){
			return getPermissaoVisao(tipoVisaoEnum).getAjuda();
		}
		return "";
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if(getUtilizaVisao(tipoVisaoEnum)){
			return Arrays.asList(getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso());
		}
		return null;
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum) {		
		if(getUtilizaVisao(tipoVisaoEnum)){
			for(PermissaoVisao permissaoVisao2: getPermissaoVisao()){
				if(permissaoVisao2.equals(tipoVisaoEnum)){
					return permissaoVisao2;
				}
			}			 
		}
		return null;
	}
	
	public Boolean getIsApresentarApenasPermissaoTotal(){
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE) && getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
	}
	
	
	public String descricaoModulo;

	@Override
		public String getDescricaoModulo() {
			if(descricaoModulo == null) {
			if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
				descricaoModulo += getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar();				
			}else {
				descricaoModulo ="";
			}
			}
			return descricaoModulo;
		}

		public String descricaoSubModulo;
		@Override
		public String getDescricaoSubModulo() {
			if(descricaoSubModulo == null) {
				if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {							
						descricaoSubModulo = getPerfilAcessoSubModulo().getDescricao();	
					
				}else {
					descricaoSubModulo ="";
				}
				}
				return descricaoSubModulo;
		}	
		
		public String descricaoModuloSubModulo;

		@Override
		public String getDescricaoModuloSubModulo() {
			if(descricaoModuloSubModulo == null) {
			if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {						
					descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()+" - "+getPerfilAcessoSubModulo().getDescricao();				
			}else {
				descricaoModuloSubModulo ="";
			}
			}
			return descricaoModuloSubModulo;
		}	 
	
}
