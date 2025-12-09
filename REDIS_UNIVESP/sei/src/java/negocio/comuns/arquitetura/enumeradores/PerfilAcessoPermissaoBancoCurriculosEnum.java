package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoBancoCurriculosEnum { //implements PerfilAcessoPermissaoEnumInterface {

	/**
	* Link
	*
	*/	
//	LINK("Link", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Link_titulo"),UteisJSF.internacionalizar("per_Link_ajuda"), new String[]{"linkCons.xhtml","linkForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_BASICO),
//	/**
//	* Area Profissional
//	*
//	*/	
//	AREA_PROFISSIONAL("AreaProfissional", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AreaProfissional_titulo"),UteisJSF.internacionalizar("per_AreaProfissional_ajuda"), new String[]{"areaProfissionalCons.xhtml","areaProfissionalForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_BASICO),
//	/**
//	* Alunos Candidatados Vaga Rel
//	*
//	*/	
//	ALUNOS_CANDIDATADOS_VAGA_REL("AlunosCandidatadosVagaRel", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AlunosCandidatadosVagaRel_titulo"),UteisJSF.internacionalizar("per_AlunosCandidatadosVagaRel_ajuda"), new String[]{"alunosCandidatadosVagaRel.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_RELATORIO),
//	/**
//	* Empresas Rel
//	*
//	*/	
//	EMPRESAS_REL("EmpresasRel", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EmpresasRel_titulo"),UteisJSF.internacionalizar("per_EmpresasRel_ajuda"), new String[]{"empresasRel.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_RELATORIO),
//	/**
//	* Contato Aniversariante
//	*
//	*/	
//	CONTATO_ANIVERSARIANTE("ContatoAniversariante", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ContatoAniversariante_titulo"),UteisJSF.internacionalizar("per_ContatoAniversariante_ajuda"), new String[]{"contatoAniversarianteCons.xhtml","contatoAniversarianteForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_RELATORIO),
//	/**
//	* Empresa Por Vagas Rel
//	*
//	*/	
//	EMPRESA_POR_VAGAS_REL("EmpresaPorVagasRel", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EmpresaPorVagasRel_titulo"),UteisJSF.internacionalizar("per_EmpresaPorVagasRel_ajuda"), new String[]{"empresaBancoTalentoRel.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_RELATORIO),
//	/**
//	* Candidatos Para Vaga Rel
//	*
//	*/	
//	CANDIDATOS_PARA_VAGA_REL("CandidatosParaVagaRel", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CandidatosParaVagaRel_titulo"),UteisJSF.internacionalizar("per_CandidatosParaVagaRel_ajuda"), new String[]{"candidatosParaVagaRel.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_RELATORIO),
// Este painel fica em bancoCurriculum.xhtml porém está desativado o seu uso. by rodrigo wind
//	PAINEL_GESTOR_BANCO_CURRICULO("PainelGestorBancoCurriculo", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PainelGestorBancoCurriculo_titulo"),UteisJSF.internacionalizar("per_PainelGestorBancoCurriculo_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_BASICO),
	/**
	* Empresa Vaga Banco Talento Rel
	*
	*/	
//	EMPRESA_VAGA_BANCO_TALENTO_REL("EmpresaVagaBancoTalentoRel", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EmpresaVagaBancoTalentoRel_titulo"),UteisJSF.internacionalizar("per_EmpresaVagaBancoTalentoRel_ajuda"), new String[]{"empresaPorVagasRelCons.xhtml","empresaPorVagasRelForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_RELATORIO),
//	/**
//	* Painel Aluno
//	*
//	*/	
//	PAINEL_ALUNO("PainelAluno", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PainelAluno_titulo"),UteisJSF.internacionalizar("per_PainelAluno_ajuda"), new String[]{"painelAlunoCons.xhtml","painelAlunoForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_BASICO),
//	/**
//	* Vagas
//	*
//	*/	
//	VAGAS("Vagas", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Vagas_titulo"),UteisJSF.internacionalizar("per_Vagas_ajuda"), new String[]{"vagasCons.xhtml","vagasForm.xhtml"}),
//			 new PermissaoVisao(TipoVisaoEnum.PARCEIRO, UteisJSF.internacionalizar("per_Vagas_titulo"),UteisJSF.internacionalizar("per_Vagas_ajuda"), new String[]{""})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_BASICO),
//	/**
//	* Texto Padrao Banco Curriculum
//	*
//	*/	
//	TEXTO_PADRAO_BANCO_CURRICULUM("TextoPadraoBancoCurriculum", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_TextoPadraoBancoCurriculum_titulo"),UteisJSF.internacionalizar("per_TextoPadraoBancoCurriculum_ajuda"), new String[]{"textoPadraoBancoCurriculumCons.xhtml","textoPadraoBancoCurriculumForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.BANCO_DE_CURRICULOS_BASICO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */
//	private PerfilAcessoPermissaoBancoCurriculosEnum(String valor, PermissaoVisao[] permissaoVisao, 
//			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
//			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
//			PerfilAcessoSubModuloEnum[] perfilAcessoSubModulo			
//			) {
//		this.valor = valor;				
//		this.permissaoVisao = permissaoVisao;				
//		this.tipoPerfilAcesso = tipoPerfilAcesso;
//		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
//		this.perfilAcessoSubModulo = perfilAcessoSubModulo;		
//	}
//	
//	private PerfilAcessoPermissaoBancoCurriculosEnum(String valor, PermissaoVisao[] permissaoVisao, 
//			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
//			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
//			PerfilAcessoSubModuloEnum perfilAcessoSubModulo			
//			) {
//		this.valor = valor;				
//		this.permissaoVisao = permissaoVisao;				
//		this.tipoPerfilAcesso = tipoPerfilAcesso;
//		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
//		this.perfilAcessoSubModulo = new PerfilAcessoSubModuloEnum[] {perfilAcessoSubModulo};		
//	}
//	
//	private String valor;
//	private PermissaoVisao[] permissaoVisao;		
//	private TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso;	
//	private PerfilAcessoSubModuloEnum[] perfilAcessoSubModulo;
//	private Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum;
	
			
	/**
	 * @return the tipoPerfilAcesso
	 */
//	public TipoPerfilAcessoPermissaoEnum getTipoPerfilAcesso() {
//		if (tipoPerfilAcesso == null) {
//			tipoPerfilAcesso = TipoPerfilAcessoPermissaoEnum.ENTIDADE;
//		}
//		return tipoPerfilAcesso;
//	}
//	/**
//	 * @param tipoPerfilAcesso the tipoPerfilAcesso to set
//	 */
//	public void setTipoPerfilAcesso(TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso) {
//		this.tipoPerfilAcesso = tipoPerfilAcesso;
//	}
//	
//	/**
//	 * @return the perfilAcessoSubModulo
//	 */
//	public PerfilAcessoSubModuloEnum[] getPerfilAcessoSubModulo() {
//		if (perfilAcessoSubModulo == null) {
//			perfilAcessoSubModulo = new PerfilAcessoSubModuloEnum[] {PerfilAcessoSubModuloEnum.TODOS};
//		}
//		return perfilAcessoSubModulo;
//	}
//	/**
//	 * @param perfilAcessoSubModulo the perfilAcessoSubModulo to set
//	 */
//	public void setPerfilAcessoSubModulo(PerfilAcessoSubModuloEnum[] perfilAcessoSubModulo) {
//		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
//	}
//
//	/**
//	 * @return the valor
//	 */
//	public String getValor() {
//		if (valor == null) {
//			valor = "";
//		}
//		return valor;
//	}
//
//	/**
//	 * @param valor the valor to set
//	 */
//	public void setValor(String valor) {
//		this.valor = valor;
//	}
//
//
//	/**
//	 * @return the permissaoSuperiorEnum
//	 */
//	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissaoSuperiorEnum() {		
//		return permissaoSuperiorEnum;
//	}
//
//	/**
//	 * @param permissaoSuperiorEnum the permissaoSuperiorEnum to set
//	 */
//	public void setPermissaoSuperiorEnum(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum) {
//		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
//	}
//
//	/**
//	 * @return the PermissaoVisao
//	 */
//	public PermissaoVisao[] getPermissaoVisao() {
//		if (permissaoVisao == null) {
//			permissaoVisao = new PermissaoVisao[0];
//		}
//		return permissaoVisao;
//	}
//
//	/**
//	 * @param PermissaoVisao the PermissaoVisao to set
//	 */
//	public void setPermissaoVisao(PermissaoVisao[] PermissaoVisao) {
//		this.permissaoVisao = PermissaoVisao;
//	}
//	
//	/**
//	 * @return the utilizarVisaoProfessor
//	 */
//	public Boolean getUtilizaVisao(TipoVisaoEnum tipoVisaoEnum) {
//		for(PermissaoVisao permissaoVisao: getPermissaoVisao()){
//			if(permissaoVisao.equals(tipoVisaoEnum)){
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	/**
//	 * @return the utilizarVisaoProfessor
//	 */
//	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum) {
//		if(getUtilizaVisao(tipoVisaoEnum)){
//			return getPermissaoVisao(tipoVisaoEnum).getDescricao();
//		}
//		return "";
//	}
//	
//	/**
//	 * @return the utilizarVisaoProfessor
//	 */
//	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum) {
//		if(getUtilizaVisao(tipoVisaoEnum)){
//			return getPermissaoVisao(tipoVisaoEnum).getAjuda();
//		}
//		return "";
//	}
//	
//	/**
//	 * @return the utilizarVisaoProfessor
//	 */
//	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum) {
//		if(getUtilizaVisao(tipoVisaoEnum)){
//			return Arrays.asList(getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso());
//		}
//		return null;
//	}
//	
//	/**
//	 * @return the utilizarVisaoProfessor
//	 */
//	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum) {		
//		if(getUtilizaVisao(tipoVisaoEnum)){
//			for(PermissaoVisao permissaoVisao2: getPermissaoVisao()){
//				if(permissaoVisao2.equals(tipoVisaoEnum)){
//					return permissaoVisao2;
//				}
//			}			 
//		}
//		return null;
//	}
//	
//	public Boolean getIsApresentarApenasPermissaoTotal(){
//		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE) && getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
//	}
//	
//
//
//	public String descricaoModulo;
//
//	@Override
//	public String getDescricaoModulo() {
//		if(descricaoModulo == null) {
//		if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
//			for(PerfilAcessoSubModuloEnum perfilAcessoSubModuloEnum: getPerfilAcessoSubModulo()) {
//				if(descricaoModulo != null) {
//					descricaoModulo += ", ";
//				}
//				if(descricaoModulo == null){
//					descricaoModulo = "";
//				}
//				descricaoModulo += perfilAcessoSubModuloEnum.getPerfilAcessoModuloEnum().getValorApresentar();	
//			}
//		}else {
//			descricaoModulo ="";
//		}
//		}
//		return descricaoModulo;
//	}
//
//	public String descricaoSubModulo;
//	@Override
//	public String getDescricaoSubModulo() {
//		if(descricaoSubModulo == null) {
//			if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
//				for(PerfilAcessoSubModuloEnum perfilAcessoSubModuloEnum: getPerfilAcessoSubModulo()) {
//					if(descricaoSubModulo != null) {
//						descricaoSubModulo += ", ";
//					}
//					if(descricaoSubModulo == null){
//						descricaoSubModulo = "";
//					}
//					descricaoSubModulo += perfilAcessoSubModuloEnum.getDescricao();	
//				}
//			}else {
//				descricaoSubModulo ="";
//			}
//			}
//			return descricaoSubModulo;
//	}	
//	
//	public String descricaoModuloSubModulo;
//
//	@Override
//	public String getDescricaoModuloSubModulo() {
//		if(descricaoModuloSubModulo == null) {
//		if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
//			for(PerfilAcessoSubModuloEnum perfilAcessoSubModuloEnum: getPerfilAcessoSubModulo()) {
//				if(descricaoModuloSubModulo != null) {
//					descricaoModuloSubModulo += ", ";
//				}
//				if(descricaoModuloSubModulo == null){
//					descricaoModuloSubModulo = "";
//				}
//				descricaoModuloSubModulo += perfilAcessoSubModuloEnum.getPerfilAcessoModuloEnum().getValorApresentar()+" - "+perfilAcessoSubModuloEnum.getDescricao();	
//			}
//		}else {
//			descricaoModuloSubModulo ="";
//		}
//		}
//		return descricaoModuloSubModulo;
//	}	
}
